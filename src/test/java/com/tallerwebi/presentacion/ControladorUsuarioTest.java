package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.Categoria;
import com.tallerwebi.dominio.entidades.Noticia;
import com.tallerwebi.dominio.entidades.Notificacion;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.excepcion.FormatoDeImagenIncorrecto;
import com.tallerwebi.dominio.excepcion.TamanioDeArchivoSuperiorALoPermitido;
import com.tallerwebi.dominio.servicios.ServicioNoticia;
import com.tallerwebi.dominio.servicios.ServicioUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;

public class ControladorUsuarioTest {

    private ControladorUsuario controladorUsuario;
    private ServicioUsuario servicioUsuarioMock;
    private Noticia noticiaMock;
    private Noticia noticiaMock2;
    private HttpServletRequest requestMock;
    private HttpSession sessionMock;
    private Usuario usuarioMock;
    private Notificacion notificacionMock;
    private Noticia noticiaBorradorMock;

    private MultipartFile imgMock;
    private ServicioNoticia servicioNoticiaMock;

    @BeforeEach
    public void init(){
        noticiaMock = mock(Noticia.class);
        noticiaMock2 = mock(Noticia.class);
        noticiaBorradorMock = mock(Noticia.class);

        imgMock = mock(MultipartFile.class);

        when(noticiaBorradorMock.getIdNoticia()).thenReturn(3L);
        when(noticiaBorradorMock.getTitulo()).thenReturn("borrador");
        when(noticiaBorradorMock.getActiva()).thenReturn(false);
        when(noticiaBorradorMock.getUsuario()).thenReturn(usuarioMock);

        when(noticiaMock.getIdNoticia()).thenReturn(1L);
        when(noticiaMock.getTitulo()).thenReturn("titulo");
        when(noticiaMock.getActiva()).thenReturn(true);
        when(noticiaMock.getUsuario()).thenReturn(usuarioMock);


        when(noticiaMock2.getIdNoticia()).thenReturn(2L);
        when(noticiaMock2.getTitulo()).thenReturn("titulo");
        when(noticiaMock2.getUsuario()).thenReturn(null);
        when(noticiaMock2.getActiva()).thenReturn(true);

        usuarioMock=mock(Usuario.class);
        when(usuarioMock.getIdUsuario()).thenReturn(1L);

        sessionMock = mock(HttpSession.class);
        when(sessionMock.getAttribute("sessionUsuarioLogueado")).thenReturn(usuarioMock);

        notificacionMock=mock(Notificacion.class);
        requestMock = mock(HttpServletRequest.class);
        servicioUsuarioMock = mock(ServicioUsuario.class);
        servicioNoticiaMock = mock(ServicioNoticia.class);
        controladorUsuario = new ControladorUsuario(servicioUsuarioMock, servicioNoticiaMock);
    }

    @Test
    public void siElIDEsNullDebeTraermeElUsuarioDeLaSesion(){
        when(servicioUsuarioMock.verificarSiElIDEsNull(usuarioMock.getIdUsuario())).thenReturn(true);
        when(sessionMock.getAttribute("sessionUsuarioLogueado")).thenReturn(usuarioMock);

        ModelAndView modelAndView=controladorUsuario.perfil(usuarioMock.getIdUsuario(),sessionMock);
        Usuario usuario = (Usuario) modelAndView.getModel().get("usuarioLogueado");

        assertThat(usuario, is(notNullValue()));
    }

    @Test
    public void siElIDNoEsNullDebeTraermeElUsuarioQueTieneEseID() throws Exception {
        when(servicioUsuarioMock.verificarSiElIDEsNull(usuarioMock.getIdUsuario())).thenReturn(false);
        when(servicioUsuarioMock.obtenerUsuarioPorId(usuarioMock.getIdUsuario())).thenReturn(usuarioMock);
        when(sessionMock.getAttribute("sessionUsuarioLogueado")).thenReturn(usuarioMock);

        ModelAndView modelAndView = controladorUsuario.perfil(usuarioMock.getIdUsuario(),sessionMock);
        Usuario usuario=(Usuario) modelAndView.getModel().get("usuarioBuscado");
        assertThat(usuario, is(notNullValue()));
    }

    @Test
    public void quePuedaObtenerSoloLasNoticiasPertenecientesAlUsuario(){

        when(servicioUsuarioMock.verificarSiElIDEsNull(usuarioMock.getIdUsuario())).thenReturn(true);
        when(sessionMock.getAttribute("sessionUsuarioLogueado")).thenReturn(usuarioMock);

        List<Noticia> noticias= new ArrayList<>();
        noticias.add(noticiaMock);
        noticias.add(noticiaMock2);

        when(servicioNoticiaMock.obtenerNoticiasDeUnUsuario(usuarioMock.getIdUsuario())).thenReturn(noticias);
        when(servicioNoticiaMock.setNoticiasLikeadas(noticias, 1L)).thenReturn(noticias);

        ModelAndView modelAndView = controladorUsuario.perfil(usuarioMock.getIdUsuario(),sessionMock);
        List<Noticia> noticiasObtenidas= (List<Noticia>) modelAndView.getModel().get("noticias");
        assertThat(noticiasObtenidas.size(), is(2));
    }

    @Test
    public void siLaDescripcionEsNullDebeTraermeUnMensajeDeError(){
        when(servicioUsuarioMock.verificarSiElIDEsNull(usuarioMock.getIdUsuario())).thenReturn(true);
        when(sessionMock.getAttribute("sessionUsuarioLogueado")).thenReturn(usuarioMock);
        when(servicioUsuarioMock.verificarSiLaDescripcionEsNull(usuarioMock.getDescripcion())).thenReturn(true);
        ModelAndView modelAndView=controladorUsuario.perfil(usuarioMock.getIdUsuario(),sessionMock);
        String mensajeError=(String) modelAndView.getModel().get("descripcionError");
        assertThat(mensajeError,is("No tiene una descripción."));
    }

    @Test
    public void siNoExisteUnUsuarioConEseIDDebeTraermeUnMensajeDeError() throws Exception {
        when(servicioUsuarioMock.verificarSiElIDEsNull(usuarioMock.getIdUsuario())).thenReturn(false);
            when(servicioUsuarioMock.obtenerUsuarioPorId(usuarioMock.getIdUsuario())).thenReturn(null);
            ModelAndView modelAndView=controladorUsuario.perfil(usuarioMock.getIdUsuario(),sessionMock);
        String mensajeError=(String) modelAndView.getModel().get("errorUsuario");
        assertThat(mensajeError,is("No existe un usuario con ese ID"));
    }

    @Test
    public void queAlListarDosNotificacionesSeCargueEnLaVista(){
        List<Notificacion> notificaciones=new ArrayList<>();
        notificaciones.add(notificacionMock);
        notificaciones.add(notificacionMock);

        when(servicioUsuarioMock.obtenerMisNotificaciones(usuarioMock.getIdUsuario())).thenReturn(notificaciones);
        when(sessionMock.getAttribute("sessionUsuarioLogueado")).thenReturn(usuarioMock);

        List<Notificacion> notificacionesObtenidas= (List<Notificacion>) controladorUsuario.notificaciones(sessionMock).getModel().get("notificaciones");

        assertThat(notificacionesObtenidas.size(),is(2));
    }


    @Test
    public void cuandoVeoMisBorradoresMeTraeLasNoticiasEnEstadoBorrador(){
        when(sessionMock.getAttribute("sessionUsuarioLogueado")).thenReturn(usuarioMock);
        when(noticiaMock.getActiva()).thenReturn(false);
        when(servicioUsuarioMock.verificarSiElIDEsNull(usuarioMock.getIdUsuario())).thenReturn(true);

        List<Noticia> listaDeNoticias = new ArrayList<>();
        listaDeNoticias.add(noticiaMock);
        listaDeNoticias.add(noticiaMock);

        when(servicioUsuarioMock.obtenerNoticiasDeUnUsuarioEnEstadoBorrador(usuarioMock.getIdUsuario())).thenReturn(listaDeNoticias);
        when(servicioNoticiaMock.setNoticiasLikeadas(listaDeNoticias, usuarioMock.getIdUsuario())).thenReturn(listaDeNoticias);

        ModelAndView modelAndView = controladorUsuario.verNoticiasEnEstadoBorrador(usuarioMock.getIdUsuario(),sessionMock);

        Usuario usuario = (Usuario) modelAndView.getModel().get("usuarioLogueado");
        List<Noticia> borradores = (List<Noticia>) modelAndView.getModel().get("noticias");

        assertThat(usuario, is(notNullValue()));
        assertThat(borradores.size(), is(2));
        assertThat(noticiaMock.getActiva(), is(false));
    }


    @Test
    public void cuandoVeoMisBorradoresMeTraeDosNoticiasEnEstadoBorrador(){

        when(servicioUsuarioMock.verificarSiElIDEsNull(usuarioMock.getIdUsuario())).thenReturn(true);
        when(sessionMock.getAttribute("sessionUsuarioLogueado")).thenReturn(usuarioMock);

        List<Noticia> noticiasEnEstadoBorrador = new ArrayList<>();
        noticiasEnEstadoBorrador.add(noticiaBorradorMock);
        noticiasEnEstadoBorrador.add(noticiaBorradorMock);

        when(servicioUsuarioMock.obtenerNoticiasDeUnUsuarioEnEstadoBorrador(usuarioMock.getIdUsuario())).thenReturn(noticiasEnEstadoBorrador);
        when(servicioNoticiaMock.setNoticiasLikeadas(noticiasEnEstadoBorrador, usuarioMock.getIdUsuario())).thenReturn(noticiasEnEstadoBorrador);

        ModelAndView modelAndView = controladorUsuario.verNoticiasEnEstadoBorrador(usuarioMock.getIdUsuario(),sessionMock);
        List<Noticia> noticiasObtenidas= (List<Noticia>) modelAndView.getModel().get("noticias");
        assertThat(noticiasObtenidas.size(), is(2));
    }

    @Test

    public void cuandoBorroMiUsuarioMeRedireccionaAlLogin() {
        ModelAndView modelAndView = this.controladorUsuario.borrarUsuario(sessionMock);
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
    }

    @Test
    public void queSeMuestreElFormularioParModificarLosDatosDelUsuario(){
        //preparacion
        //MockHttpSession session = new MockHttpSession();
        when(sessionMock.getAttribute("sessionUsuarioLogueado")).thenReturn(usuarioMock);

        // ejecucion
        ModelAndView modelAndView = controladorUsuario.mostrarFormularioModificar(sessionMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("modificar-usuario"));

    }

    @Test
    public void queAlModificarLosDatosDelUsuarioRedirijaALaVistaDePerfil() throws FormatoDeImagenIncorrecto, IOException {
        //preparacion


        //ejecucion
        ModelAndView modelAndView = controladorUsuario.modificarUsuario(usuarioMock, sessionMock, imgMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/perfil"));
    }
    /*  @Test
    public void queAlModificarLosDatosDelUsuarioRedirijaALaVistaDePerfil() throws TamanioDeArchivoSuperiorALoPermitido, FormatoDeImagenIncorrecto, IOException {
        //preparacion
        Long userId = 1L;
        Usuario usuario = new Usuario();
        MockHttpSession session = new MockHttpSession();
        usuario.setIdUsuario(userId);

        //ejecucion
        Mockito.doNothing().when(servicioUsuarioMock).modificarDatosUsuario(Mockito.any(Usuario.class), Mockito.any(MultipartFile.class));
        session.setAttribute("UsuarioAEditar", usuario);
        ModelAndView modelAndView = controladorUsuario.modificarUsuario(usuario, session, imgMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/perfil/" + userId));
    }*/

}
