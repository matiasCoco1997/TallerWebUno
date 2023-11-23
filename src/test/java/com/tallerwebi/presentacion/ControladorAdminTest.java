package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.Rol;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.excepcion.FormatoDeImagenIncorrecto;
import com.tallerwebi.dominio.excepcion.UsuarioInexistente;
import com.tallerwebi.dominio.servicios.ServicioAdmin;
import com.tallerwebi.dominio.servicios.ServicioHome;
import com.tallerwebi.dominio.servicios.ServicioNoticia;
import com.tallerwebi.dominio.servicios.ServicioUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class ControladorAdminTest {
    private Usuario usuarioMock;
    private ControladorAdmin controladorAdmin;
    private HttpSession sessionMock;
    private ServicioHome servicioHomeMock;
    private ServicioNoticia servicioNoticiaMock;
    private ServicioUsuario servicioUsuarioMock;
    private ServicioAdmin servicioAdminMock;
    private Rol rolAdminMock;

    @BeforeEach
    public void init(){
        servicioHomeMock = mock(ServicioHome.class);
        servicioUsuarioMock = mock(ServicioUsuario.class);
        servicioNoticiaMock = mock(ServicioNoticia.class);
        servicioAdminMock = mock(ServicioAdmin.class);
        sessionMock = mock(HttpSession.class);
        usuarioMock = mock(Usuario.class);
        rolAdminMock = mock(Rol.class);
        rolAdminMock = Rol.valueOf("ADMIN");
        controladorAdmin = new ControladorAdmin(servicioHomeMock,servicioUsuarioMock,servicioNoticiaMock, servicioAdminMock);
    }
    @Test
    public void siElUsuarioEstaLogueadoRedirigeAlHomeAdmin(){
        when(usuarioMock.getRol()).thenReturn(Rol.ADMIN);
        when(sessionMock.getAttribute("sessionUsuarioLogueado")).thenReturn(usuarioMock);
        ModelAndView resultado= controladorAdmin.irAHomeAdmin(sessionMock);
        assertThat("home-admin",equalTo(resultado.getViewName()));
    }

    @Test
    public void siElUsuarioEstaLogueadoRedirigeAlLoginSiElRolEsUser(){
        when(usuarioMock.getRol()).thenReturn(Rol.USER);
        when(sessionMock.getAttribute("sessionUsuarioLogueado")).thenReturn(usuarioMock);
        ModelAndView resultado= controladorAdmin.irAHomeAdmin(sessionMock);
        assertThat("redirect:/login",equalTo(resultado.getViewName()));
    }
    @Test
    public void siElUsuarioNoEstaLogueadoRedirigeAlLogin(){
        when(sessionMock.getAttribute("sessionUsuarioLogueado")).thenReturn(null);
        ModelAndView resultado= controladorAdmin.irAHomeAdmin(sessionMock);
        assertThat("redirect:/login",equalTo(resultado.getViewName()));
    }

    @Test
    public void elAdminPuedeVerLosUsuarios(){

        when(usuarioMock.getRol()).thenReturn(rolAdminMock);

        when(sessionMock.getAttribute("sessionUsuarioLogueado")).thenReturn(usuarioMock);

        List<Usuario> usuarios = new ArrayList<>();

        usuarios.add(usuarioMock);

        when(servicioHomeMock.listarUsuarios(anyLong())).thenReturn(usuarios);

        ModelAndView vistaObtenida = controladorAdmin.verUsuarios(sessionMock);

        assertThat("usuariosActivos",equalTo(vistaObtenida.getViewName()));
        assertThat(usuarios , equalTo(vistaObtenida.getModel().get("usuariosActivos")));
        assertThat(usuarioMock , equalTo( vistaObtenida.getModel().get("usuario")));
    }

    @Test
    public void elAdminPuedeEliminarUnUsuario() throws Exception {

        when(sessionMock.getAttribute("sessionUsuarioLogueado")).thenReturn(usuarioMock);

        when(usuarioMock.getRol()).thenReturn(rolAdminMock);

        when(servicioUsuarioMock.obtenerUsuarioPorId(anyLong())).thenReturn(usuarioMock);

        ModelAndView vistaObtenida = controladorAdmin.eliminarUsuario(sessionMock, usuarioMock.getIdUsuario());

        assertThat("redirect:/admin/usuarios",equalTo(vistaObtenida.getViewName()));
        verify(servicioUsuarioMock, times(1)).borrarUsuario(usuarioMock);
    }


    @Test
    public void eliminarUnUsuarioRetornaUnaException() throws Exception {

        when(sessionMock.getAttribute("sessionUsuarioLogueado")).thenReturn(usuarioMock);

        when(usuarioMock.getRol()).thenReturn(rolAdminMock);

        when(servicioUsuarioMock.obtenerUsuarioPorId(anyLong())).thenReturn(usuarioMock);

        doThrow(RuntimeException.class).when(servicioUsuarioMock).borrarUsuario(usuarioMock);

        ModelAndView vistaObtenida = controladorAdmin.eliminarUsuario(sessionMock, usuarioMock.getIdUsuario());

        assertThat(vistaObtenida.getViewName(), equalToIgnoringCase("usuariosActivos"));
        assertThat(vistaObtenida.getModel().get("error").toString(), equalToIgnoringCase("Error al borrar usuario."));
    }

    @Test
    public void queSeRedirijaALaVistaCuandoSeQuieraVerLasNoticiasMasLikeadas(){
        when(sessionMock.getAttribute(anyString())).thenReturn(usuarioMock);
        when(usuarioMock.getRol()).thenReturn(Rol.ADMIN);
        ModelAndView model = controladorAdmin.mostrarNoticiasMasLikeadas(sessionMock);
        assertThat(model.getViewName(), equalToIgnoringCase("noticias-mas-likeadas"));
    }

    @Test
    public void queElAdminPuedaDarElRolDeAdmin() throws Exception {
        when(sessionMock.getAttribute("sessionUsuarioLogueado")).thenReturn(usuarioMock);
        when(usuarioMock.getRol()).thenReturn(rolAdminMock);
        when(servicioUsuarioMock.obtenerUsuarioPorId(anyLong())).thenReturn(usuarioMock);

        ModelAndView model = controladorAdmin.darRolAdmin(usuarioMock.getIdUsuario(), sessionMock);

        assertThat(model.getViewName(), equalToIgnoringCase("redirect:/admin/usuarios"));
    }

    @Test
    public void siElUsuarioNoExisteQueLanceUnaExcepcion() throws Exception {
        when(sessionMock.getAttribute("sessionUsuarioLogueado")).thenReturn(usuarioMock);
        when(usuarioMock.getRol()).thenReturn(rolAdminMock);

        Usuario usuarioConRolMock = mock(Usuario.class);
        when(usuarioConRolMock.getRol()).thenReturn(Rol.USER);
        when(servicioUsuarioMock.obtenerUsuarioPorId(anyLong())).thenReturn(usuarioConRolMock);

        doThrow(UsuarioInexistente.class).when(servicioUsuarioMock).darRolAdmin(usuarioConRolMock);

        ModelAndView model = controladorAdmin.darRolAdmin(usuarioMock.getIdUsuario(), sessionMock);

        assertThat(model.getModel().get("error").toString(), equalToIgnoringCase("El usuario no existe"));
        assertThat(model.getViewName(), equalToIgnoringCase("usuariosActivos"));
    }
}
