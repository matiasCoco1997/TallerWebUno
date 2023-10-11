package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.Comentario;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.servicios.ServicioComentario;
import com.tallerwebi.dominio.excepcion.ComentarioException;
import com.tallerwebi.infraestructura.RepositorioComentario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ControladorComentarioTest {
    private ControladorComentario controladorComentarioMock;
    private Comentario comentarioMock;
    private HttpSession sessionMock;
    private ServicioComentario servivioComentarioMock;
    private RepositorioComentario repositorioComentarioMock;
    private Usuario usuarioMock;


    @BeforeEach
    public void init(){
        servivioComentarioMock = mock(ServicioComentario.class);
        controladorComentarioMock = new ControladorComentario(servivioComentarioMock);
        comentarioMock = mock(Comentario.class);
        sessionMock = mock(HttpSession.class);
        usuarioMock = mock(Usuario.class);
        repositorioComentarioMock = mock(RepositorioComentario.class);

        //Comentario
        when(comentarioMock.getDescripcion()).thenReturn("Descripción");
        when(comentarioMock.getIdNoticia()).thenReturn(1L);
        when(comentarioMock.getIdUsuario()).thenReturn(1L);
        when(comentarioMock.getId()).thenReturn(1L);

        //Usuario
        when(usuarioMock.getNombre()).thenReturn("Nombre");
        when(usuarioMock.getIdUsuario()).thenReturn(1L);
        when(usuarioMock.getFotoPerfil()).thenReturn("FotoDePerfil");
    }
    @Test
    public void queSePuedaPersistirUnComentarioMandaLaVistaDeComentario() throws ComentarioException {
        when(sessionMock.getAttribute(anyString())).thenReturn(usuarioMock);
        when(comentarioMock.getFechaCreacion()).thenReturn(LocalDateTime.now());

        ModelAndView respuesta = controladorComentarioMock.guardarComentario(comentarioMock, sessionMock);

        assertEquals("fragment/comentario-response", respuesta.getViewName());
        verify(servivioComentarioMock, times(1)).guardarComentario(comentarioMock);
    }
    @Test
    public void queAlAgregarUnComentarioSinDescripcionFalleYTireUnaExcepcion() throws ComentarioException {
        when(sessionMock.getAttribute(anyString())).thenReturn(usuarioMock);
        doThrow(new ComentarioException("La descripción debe tener entre 1 y 255 caracteres"))
                .when(servivioComentarioMock)
                .guardarComentario(comentarioMock);

        ModelAndView respuesta = controladorComentarioMock.guardarComentario(comentarioMock, sessionMock);

        String mensajeDeError = (String) respuesta.getModelMap().get("error");
        assertEquals("fragment/comentario-response", respuesta.getViewName());
        assertEquals("La descripción debe tener entre 1 y 255 caracteres", mensajeDeError);
        verify(servivioComentarioMock, times(1)).guardarComentario(any());
    }
    @Test
    public void queSePuedaEliminarUnComentarioCorrectamenteMandaUn204() {
        when(sessionMock.getAttribute(anyString())).thenReturn(usuarioMock);
        when(servivioComentarioMock.eliminarComentario(anyLong(),anyLong())).thenReturn(true);

        ResponseEntity<Object> respuesta = controladorComentarioMock.eliminarComentario(1L, sessionMock);

        assertEquals(HttpStatus.NO_CONTENT, respuesta.getStatusCode());
        verify(servivioComentarioMock, times(1)).eliminarComentario(anyLong(), anyLong());

    }
    @Test
    public void queAlIntentarEliminarUnComentarioySeProduceUnErrorDevuelva404() {
        when(sessionMock.getAttribute(anyString())).thenReturn(usuarioMock);

        ResponseEntity<Object> respuesta = controladorComentarioMock.eliminarComentario(anyLong(), sessionMock);

        assertEquals(HttpStatus.NOT_FOUND, respuesta.getStatusCode());
        verify(servivioComentarioMock, times(1)).eliminarComentario(anyLong(), anyLong());
    }
    @Test
    public void queAlEditarUnComentarioCorrectamenteMandeUn204() throws ComentarioException {
        when(sessionMock.getAttribute(anyString())).thenReturn(usuarioMock);
        when(sessionMock.getAttribute("comentarioEnEdicion")).thenReturn(comentarioMock);

        ResponseEntity<Object> respuesta = controladorComentarioMock.modificarComentario(comentarioMock, sessionMock);

        assertEquals(HttpStatus.NO_CONTENT, respuesta.getStatusCode());
        verify(servivioComentarioMock, times(1)).modificarComentario(comentarioMock, usuarioMock.getIdUsuario());
    }
    @Test
    public void queAlEditarUnComentarioIncorrectamentePorCantidadDeCaracteresMandeUnaExcepcionConMensaje() throws ComentarioException {
        when(sessionMock.getAttribute(anyString())).thenReturn(usuarioMock);
        when(sessionMock.getAttribute("comentarioEnEdicion")).thenReturn(comentarioMock);
        doThrow(new ComentarioException("La descripción debe tener entre 1 y 255 caracteres"))
                .when(servivioComentarioMock)
                .modificarComentario(any(), anyLong());

        ResponseEntity<Object> respuesta = controladorComentarioMock.modificarComentario(comentarioMock, sessionMock);

        assertEquals(HttpStatus.NOT_FOUND, respuesta.getStatusCode());
        assertEquals("La descripción debe tener entre 1 y 255 caracteres", respuesta.getBody());
        verify(servivioComentarioMock, times(1)).modificarComentario(comentarioMock, usuarioMock.getIdUsuario());
    }
    @Test
    public void queAlEditarUnComentarioIncorrectamentePorQueNoLoPerteneceAlUsuaruiMandaUnaExcepcionConMensaje() throws ComentarioException {
        when(sessionMock.getAttribute("sessionUsuarioLogueado")).thenReturn(usuarioMock);
        when(sessionMock.getAttribute("comentarioEnEdicion")).thenReturn(comentarioMock);
        doThrow(new ComentarioException("Error al editar comentario"))
                .when(servivioComentarioMock)
                .modificarComentario(any(), anyLong());

        ResponseEntity<Object> respuesta = controladorComentarioMock.modificarComentario(comentarioMock, sessionMock);

        assertEquals("Error al editar comentario", respuesta.getBody());
        assertEquals(HttpStatus.NOT_FOUND, respuesta.getStatusCode());
        verify(servivioComentarioMock, times(1)).modificarComentario(comentarioMock, usuarioMock.getIdUsuario());
    }
    @Test
    public void queAlEditarUnComentarioSeEnvieElFormCorrespondite() {
        ModelAndView respuesta = controladorComentarioMock.enviarFormModificarComentario(anyLong(), sessionMock);

        assertEquals("fragment/form-comentario", respuesta.getViewName());
    }
    @Test
    public void queAlEditarUnComentarioSeEnvieElFormCorresponditeConModelConValorBooleanoParaFormatearElForm() {
        when(sessionMock.getAttribute("sessionUsuarioLogueado")).thenReturn(usuarioMock);
        ModelAndView respuesta = controladorComentarioMock.enviarFormModificarComentario(anyLong(), sessionMock);

        assertEquals("fragment/form-comentario", respuesta.getViewName());
        assertEquals(true, respuesta.getModelMap().get("edicion"));
    }
    @Test
    public void queAlEditarUnComentarioSeEnvieElFormCorresponditeConModelConComentario() {
        when(sessionMock.getAttribute("sessionUsuarioLogueado")).thenReturn(usuarioMock);
        when(servivioComentarioMock.buscarComentarioPorId(anyLong())).thenReturn(comentarioMock);

        ModelAndView respuesta = controladorComentarioMock.enviarFormModificarComentario(comentarioMock.getId(), sessionMock);
        System.out.println(respuesta.getModel());

        assertEquals("fragment/form-comentario", respuesta.getViewName());
        assertEquals("Comentario", respuesta.getModelMap().get("comentario").getClass().getSimpleName());
    }
    @Test
    public void queAlBuscarLosComentariosDeUnaPublicacionRetorneALaPlantillaDeComentario(){
        List<Comentario> comentarios = new ArrayList<>();
        comentarios.add(comentarioMock);
        when(sessionMock.getAttribute("sessionUsuarioLogueado")).thenReturn(usuarioMock);
        when(servivioComentarioMock.buscarComentarios(anyLong())).thenReturn(comentarios);
        ModelAndView respuesta = controladorComentarioMock.listarComentario(anyLong(), sessionMock);
        assertEquals("fragment/comentario-response",respuesta.getViewName());
    }
   @Test
    public void queAlBuscarLosComentariosDeUnaPublicacionRetorneUnaListaDeComentarios(){
        List<Comentario> comentarios = new ArrayList<>();
        comentarios.add(comentarioMock);
        when(sessionMock.getAttribute("sessionUsuarioLogueado")).thenReturn(usuarioMock);
        when(servivioComentarioMock.buscarComentarios(anyLong())).thenReturn(comentarios);
        ModelAndView respuesta = controladorComentarioMock.listarComentario(anyLong(), sessionMock);
        comentarios = (List<Comentario>) respuesta.getModelMap().get("comentarios");
        assertEquals(1, comentarios.size());
    }
}
