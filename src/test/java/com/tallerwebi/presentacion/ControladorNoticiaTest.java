package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.Noticia;

import com.tallerwebi.dominio.entidades.Notificacion;
import com.tallerwebi.dominio.excepcion.*;
import com.tallerwebi.dominio.servicios.ServicioComentario;

import com.tallerwebi.dominio.entidades.Usuario;

import com.tallerwebi.dominio.servicios.ServicioNoticia;

import com.tallerwebi.dominio.servicios.ServicioUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.stubbing.OngoingStubbing;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;

import static org.mockito.Mockito.*;



public class ControladorNoticiaTest {

    private ControladorNoticia controladorNoticia;
    private ServicioNoticia servicioNoticiaMock;
    private ServicioComentario servicioComentarioMock;
    private Noticia noticiaMock;
    private Usuario usuarioMock;
    private HttpServletRequest requestMock;
    private HttpSession sessionMock;

    private MultipartFile imgMock;
    private MultipartFile audioMock;
    private ServicioUsuario servicioUsuarioMock;

    @BeforeEach
    public void init(){
        noticiaMock = mock(Noticia.class);
        noticiaMock.setCategoria("1");
        when(noticiaMock.getIdNoticia()).thenReturn(1L);
        when(noticiaMock.getTitulo()).thenReturn("titulo");
        when(noticiaMock.getCategoria()).thenReturn("1");

        usuarioMock=mock(Usuario.class);
        when(usuarioMock.getIdUsuario()).thenReturn(1L);

        imgMock = mock(MultipartFile.class);
        requestMock = mock(HttpServletRequest.class);
        sessionMock = mock(HttpSession.class);

        audioMock = mock(MultipartFile.class);

        servicioNoticiaMock = mock(ServicioNoticia.class);
        servicioUsuarioMock = mock(ServicioUsuario.class);
        servicioComentarioMock = mock(ServicioComentario.class);
        controladorNoticia = new ControladorNoticia(servicioNoticiaMock, servicioComentarioMock,servicioUsuarioMock);
    }


    @Test
    public void queAlEditarUnaNoticiaRedireccioneAlHome() {
        when(sessionMock.getAttribute("sessionUsuarioLogueado")).thenReturn(usuarioMock);
        // ejecucion
        ModelAndView modelAndView = controladorNoticia.editarNoticia(noticiaMock, sessionMock, imgMock, audioMock);

        // validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/home"));
    }

    @Test
    public void queAlEditarUnaNoticiaRetorneUnaExceptionDelTipoCampoVacio() throws TamanioDeArchivoSuperiorALoPermitido, FormatoDeImagenIncorrecto, FormatoDeAudioIncorrecto, CampoVacio, IOException {
        // preparacion
        when(sessionMock.getAttribute("sessionUsuarioLogueado")).thenReturn(usuarioMock);
        doThrow(CampoVacio.class).when(servicioNoticiaMock).editarNoticia(noticiaMock, usuarioMock, imgMock, audioMock);

        // ejecucion
        ModelAndView modelAndView = controladorNoticia.editarNoticia(noticiaMock, sessionMock, imgMock, audioMock);

        // validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("editar_noticia"));
        assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("Error, para crear la nota debe completar todos los campos."));
    }

    @Test
    public void queAlEditarUnaNoticiaRetorneUnaExceptionDelTipoTamanioDeArchivoSuperiorALoPermitido() throws TamanioDeArchivoSuperiorALoPermitido, FormatoDeImagenIncorrecto, FormatoDeAudioIncorrecto, CampoVacio, IOException {
        // preparacion
        when(sessionMock.getAttribute("sessionUsuarioLogueado")).thenReturn(usuarioMock);
        doThrow(TamanioDeArchivoSuperiorALoPermitido.class).when(servicioNoticiaMock).editarNoticia(noticiaMock, usuarioMock, imgMock, audioMock);

        // ejecucion
        ModelAndView modelAndView = controladorNoticia.editarNoticia(noticiaMock, sessionMock, imgMock, audioMock);

        // validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("editar_noticia"));
        assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("Error, El archivo seleccionado es demasiado pesado."));
    }

    @Test
    public void queAlEditarUnaNoticiaRetorneUnaExceptionDelTipoFormatoDeImagenIncorrecto() throws TamanioDeArchivoSuperiorALoPermitido, FormatoDeImagenIncorrecto, FormatoDeAudioIncorrecto, CampoVacio, IOException {
        // preparacion
        when(sessionMock.getAttribute("sessionUsuarioLogueado")).thenReturn(usuarioMock);
        doThrow(FormatoDeImagenIncorrecto.class).when(servicioNoticiaMock).editarNoticia(noticiaMock, usuarioMock, imgMock, audioMock);

        // ejecucion
        ModelAndView modelAndView = controladorNoticia.editarNoticia(noticiaMock, sessionMock, imgMock, audioMock);

        // validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("editar_noticia"));
        assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("Error, el formato de la imagen no esta permitido."));
    }

    @Test
    public void queAlEditarUnaNoticiaRetorneUnaExceptionDelTipoFormatoDeAudioIncorrecto() throws TamanioDeArchivoSuperiorALoPermitido, FormatoDeImagenIncorrecto, FormatoDeAudioIncorrecto, CampoVacio, IOException {
        // preparacion
        when(sessionMock.getAttribute("sessionUsuarioLogueado")).thenReturn(usuarioMock);
        doThrow(FormatoDeAudioIncorrecto.class).when(servicioNoticiaMock).editarNoticia(noticiaMock, usuarioMock, imgMock, audioMock);

        // ejecucion
        ModelAndView modelAndView = controladorNoticia.editarNoticia(noticiaMock, sessionMock, imgMock, audioMock);

        // validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("editar_noticia"));
        assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("Error, el formato del audio no esta permitido, solo es posible un tipo de audio ' .mp3 '."));
    }

    @Test
    public void queAlEditarUnaNoticiaRetorneUnaExceptionDelTipoException() throws TamanioDeArchivoSuperiorALoPermitido, FormatoDeImagenIncorrecto, FormatoDeAudioIncorrecto, CampoVacio, IOException {
        // preparacion
        when(sessionMock.getAttribute("sessionUsuarioLogueado")).thenReturn(usuarioMock);
        doThrow(RuntimeException.class).when(servicioNoticiaMock).editarNoticia(noticiaMock, usuarioMock, imgMock, audioMock);

        // ejecucion
        ModelAndView modelAndView = controladorNoticia.editarNoticia(noticiaMock, sessionMock, imgMock, audioMock);

        // validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("editar_noticia"));
        assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("Error al editar la noticia."));
    }

    @Test
    public void queAlCrearUnaNoticiaRedireccioneAlHome() {
        when(sessionMock.getAttribute("sessionUsuarioLogueado")).thenReturn(usuarioMock);
        // ejecucion
        ModelAndView modelAndView = controladorNoticia.crearNuevaNoticia(noticiaMock, sessionMock, imgMock, audioMock);

        // validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/home"));
    }

    @Test
    public void queAlCrearUnaNoticiaEnEstadoBorradorMeRedirijaAlHome() {
        when(sessionMock.getAttribute("sessionUsuarioLogueado")).thenReturn(usuarioMock);
        when(noticiaMock.getActiva()).thenReturn(false);

        // ejecucion
        ModelAndView modelAndView = controladorNoticia.crearNuevaNoticia(noticiaMock, sessionMock, imgMock, audioMock);

        // validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/home"));
        assertThat(noticiaMock.getActiva(), is(false));
    }

    @Test
    public void queAlCrearUnaNoticiaConCamposVaciosRetorneUnaExceptionDelTipoCampoVacio() throws TamanioDeArchivoSuperiorALoPermitido, FormatoDeImagenIncorrecto, FormatoDeAudioIncorrecto, CampoVacio, IOException {
        // preparacion
        //when(controladorNoticia.crearNuevaNoticia(noticiaMock, sessionMock, imgMock, audioMock)).thenThrow(CampoVacio.class);
        when(sessionMock.getAttribute("sessionUsuarioLogueado")).thenReturn(usuarioMock);
        doThrow(CampoVacio.class).when(servicioNoticiaMock).crearNoticia(noticiaMock, usuarioMock, imgMock, audioMock);

        // ejecucion
        ModelAndView modelAndView = controladorNoticia.crearNuevaNoticia(noticiaMock, sessionMock, imgMock, audioMock);

        // validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("crear_noticia"));
        assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("Error, para crear la nota debe completar todos los campos."));
    }

    @Test
    public void queAlCrearUnaNoticiaConUnaImagenPesadaRetorneUnaExceptionDelTipoTamanioDeArchivoSuperiorALoPermitido() throws TamanioDeArchivoSuperiorALoPermitido, FormatoDeImagenIncorrecto, FormatoDeAudioIncorrecto, CampoVacio, IOException {
        // preparacion
        when(sessionMock.getAttribute("sessionUsuarioLogueado")).thenReturn(usuarioMock);
        doThrow(TamanioDeArchivoSuperiorALoPermitido.class).when(servicioNoticiaMock).crearNoticia(noticiaMock, usuarioMock, imgMock, audioMock);

        // ejecucion
        ModelAndView modelAndView = controladorNoticia.crearNuevaNoticia(noticiaMock, sessionMock, imgMock, audioMock);

        // validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("crear_noticia"));
        assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("Error, El archivo seleccionado es demasiado pesado."));
    }

    @Test
    public void queAlCrearUnaNoticiaConUnFormatoDeImagenIncorrectoRetorneUnaExceptionDelTipoFormatoDeImagenIncorrecto() throws FormatoDeImagenIncorrecto, TamanioDeArchivoSuperiorALoPermitido, FormatoDeAudioIncorrecto, CampoVacio, IOException {
        // preparacion
        //when(controladorNoticia.crearNuevaNoticia(noticiaMock, sessionMock, imgMock, audioMock)).thenThrow(FormatoDeImagenIncorrecto.class);
        when(sessionMock.getAttribute("sessionUsuarioLogueado")).thenReturn(usuarioMock);
        doThrow(FormatoDeImagenIncorrecto.class).when(servicioNoticiaMock).crearNoticia(noticiaMock, usuarioMock, imgMock, audioMock);

        // ejecucion
        ModelAndView modelAndView = controladorNoticia.crearNuevaNoticia(noticiaMock, sessionMock, imgMock, audioMock);

        // validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("crear_noticia"));
        assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("Error, el formato de la imagen no esta permitido."));
    }

    @Test
    public void queAlCrearUnaNoticiaConUnFormatoDeAudioIncorrectoRetorneUnaExceptionDelTipoFormatoDeAudioIncorrecto() throws FormatoDeAudioIncorrecto, TamanioDeArchivoSuperiorALoPermitido, FormatoDeImagenIncorrecto, CampoVacio, IOException {
        // preparacion
        when(sessionMock.getAttribute("sessionUsuarioLogueado")).thenReturn(usuarioMock);
        //when(controladorNoticia.crearNuevaNoticia(noticiaMock, sessionMock, imgMock, audioMock)).thenThrow(FormatoDeAudioIncorrecto.class);
        doThrow(FormatoDeAudioIncorrecto.class).when(servicioNoticiaMock).crearNoticia(noticiaMock, usuarioMock, imgMock, audioMock);

        // ejecucion
        ModelAndView modelAndView = controladorNoticia.crearNuevaNoticia(noticiaMock, sessionMock, imgMock, audioMock);

        // validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("crear_noticia"));
        assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("Error, el formato del audio no esta permitido, solo es posible un tipo de audio ' .mp3 '."));
    }

    @Test
    public void queAlCrearUnaNoticiaRetorneUnaException() throws TamanioDeArchivoSuperiorALoPermitido, FormatoDeImagenIncorrecto, FormatoDeAudioIncorrecto, CampoVacio, IOException {
        // preparacion
        when(sessionMock.getAttribute("sessionUsuarioLogueado")).thenReturn(usuarioMock);
        //when(controladorNoticia.crearNuevaNoticia(noticiaMock, sessionMock, imgMock, audioMock)).thenThrow(RuntimeException.class);
        doThrow(RuntimeException.class).when(servicioNoticiaMock).crearNoticia(noticiaMock, usuarioMock, imgMock, audioMock);

        // ejecucion
        ModelAndView modelAndView = controladorNoticia.crearNuevaNoticia(noticiaMock, sessionMock, imgMock, audioMock);

        // validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("crear_noticia"));
        assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("Error al crear la noticia."));
    }

    @Test
    public void queCuandoSeBorreUnaNoticiaRedireccioneAlHome() {

        when(noticiaMock.getUsuario()).thenReturn(usuarioMock);

        when(usuarioMock.getIdUsuario()).thenReturn(1L);
        when(sessionMock.getAttribute(anyString())).thenReturn(usuarioMock);

        when(noticiaMock.getIdNoticia()).thenReturn(1L);
        when(servicioNoticiaMock.buscarNoticiaPorId(anyLong())).thenReturn(noticiaMock);

        // ejecucion
        ModelAndView modelAndView = controladorNoticia.borrarNoticiaPorId(noticiaMock.getIdNoticia(), sessionMock);

        // validacion
        assertThat(modelAndView.getViewName() , equalToIgnoringCase("redirect:/home"));
    }

    @Test
    public void queCuandoSeBorreUnaNoticiaRetorneUnaException() throws IOException {
        // preparacion
        doThrow(RuntimeException.class).when(servicioNoticiaMock).borrarNoticiaPorId(any());

        // ejecucion
        ModelAndView modelAndView = controladorNoticia.borrarNoticiaPorId(noticiaMock.getIdNoticia(), sessionMock);

        // validacion
        assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("Error al borrar la noticia."));
    }

    @Test
    public void buscarNoticiasPorTituloYLasCargueEnElHome(){
        // ejecucion
        ModelAndView modelAndView = controladorNoticia.buscarNoticiaPorTitulo(noticiaMock.getTitulo());

        // validacion
        assertThat(modelAndView.getViewName() , equalToIgnoringCase("home"));
    }

    @Test
    public void buscarNoticiasPorTituloYRetorneUnaException() {
        // preparacion

        doThrow(RuntimeException.class).when(servicioNoticiaMock).buscarNoticiaPorTitulo(anyString());

        // ejecucion
        ModelAndView modelAndView = controladorNoticia.buscarNoticiaPorTitulo(noticiaMock.getTitulo());

        // validacion
        assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("Error al buscar noticia."));
    }

    @Test
    public void queAlDarMegustaTireUnaExcepcionPorqueLaNoticiaFueEliminada(){

        Noticia n1 = new Noticia();
        controladorNoticia.crearNuevaNoticia(n1,sessionMock,imgMock,audioMock);
        controladorNoticia.borrarNoticiaPorId(n1.getIdNoticia(), sessionMock);

        try {
            //ModelAndView modelo = controladorNoticia.darLike(1L,sessionMock);
        }catch (Exception e){
            assertThat(e.getMessage(),is("La noticia fue eliminada"));
        }
    }

    @Test
    public void queAlGenerarUnaNotificacionSeRedirijaAlHome(){
        when(sessionMock.getAttribute("sessionUsuarioLogueado")).thenReturn(usuarioMock);
        ModelAndView modelAndView = controladorNoticia.crearNuevaNoticia(noticiaMock, sessionMock, imgMock, audioMock);
        // validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/home"));
    }
    @Test
    public void queAlRepublicarMeRedirijaAlHome(){
        when(sessionMock.getAttribute("sessionUsuarioLogueado")).thenReturn(usuarioMock);
        ModelAndView modelAndView = controladorNoticia.republicar(1L,sessionMock);
        // validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/home"));
    }

}
