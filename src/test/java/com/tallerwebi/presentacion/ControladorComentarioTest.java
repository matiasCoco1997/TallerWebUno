package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Entidades.Comentario;
import com.tallerwebi.dominio.Servicios.ServicioComentario;
import com.tallerwebi.dominio.excepcion.DescripcionComentarioException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ControladorComentarioTest {
    private ControladorComentario controladorComentarioMock;
    private Comentario comentarioMock;

    private ServicioComentario servivioComentarioMock;

    @BeforeEach
    public void init(){
        servivioComentarioMock = mock(ServicioComentario.class);
        controladorComentarioMock = new ControladorComentario(servivioComentarioMock);
        comentarioMock = mock(Comentario.class);
        when(comentarioMock.getDescripcion()).thenReturn("Descripción");
        when(comentarioMock.getIdNoticia()).thenReturn(1L);
        when(comentarioMock.getIdUsuario()).thenReturn(1L);
    }
    @Test
    public void queAlAgregarUnComentarioMandeUn200() throws DescripcionComentarioException {
        doNothing().when(servivioComentarioMock).guardarComentario(comentarioMock);
        ResponseEntity<Object> respuesta = controladorComentarioMock.guardarComentario(1L, comentarioMock);
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(comentarioMock, respuesta.getBody());
    }
    @Test
    public void queAlAgregarUnComentarioFalleYMandeUn500() throws DescripcionComentarioException {
        doThrow(RuntimeException.class).when(servivioComentarioMock).guardarComentario(comentarioMock);
        ResponseEntity<Object> resultado = controladorComentarioMock.guardarComentario(1L, comentarioMock);
        assertThat(resultado.getStatusCode(), is(HttpStatus.INTERNAL_SERVER_ERROR));
        verify(servivioComentarioMock, times(1)).guardarComentario(any());
    }
    @Test
    public void queAlBuscarLosComentariosDeUnaPublicacionRetorneUnaListaDeComentarios(){
        List<Comentario> comentarios = new ArrayList<>();
        comentarios.add(comentarioMock);
        when(servivioComentarioMock.buscarComentarios(anyLong())).thenReturn(comentarios);
        ResponseEntity<Object> respuesta = controladorComentarioMock.listarComentario(anyLong());
        assertThat(respuesta.getStatusCode(), is(HttpStatus.OK));
    }
    @Test
    public void testListarComentarioNoEncontrado() {
        Long idPublicacion = 2L;
        when(servivioComentarioMock.buscarComentarios(idPublicacion)).thenReturn(null);
        ResponseEntity<Object> respuesta = controladorComentarioMock.listarComentario(idPublicacion);
        assertThat(respuesta.getStatusCode(), is(HttpStatus.OK));
        assertThat(respuesta.getBody(), is(nullValue()));
    }

}
