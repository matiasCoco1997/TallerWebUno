package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Entidades.Comentario;
import com.tallerwebi.dominio.Servicios.ServicioComentario;
import com.tallerwebi.dominio.excepcion.DescripcionComentarioException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
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
       /* String resultado = String.valueOf(controladorComentarioMock.guardarComentario(1L, comentarioMock));

        assertThat(resultado, containsString("<200 OK OK,Mock for Comentario,[]"));
        verify(servivioComentarioMock, times(1)).guardarComentario(any());*/

    }
  /*  @Test
    public void queAlAgregarUnComentarioFalleYMandeUn500() throws DescripcionComentarioException {
        doThrow(RuntimeException.class).when(servivioComentarioMock).guardarComentario(any(Comentario.class));
        String resultado = String.valueOf(controladorComentarioMock.guardarComentario(1L, comentarioMock));

        assertThat(resultado, is("<500 INTERNAL_SERVER_ERROR Internal Server Error,Error al guardar el comentario,[]>"));
        verify(servivioComentarioMock, times(1)).guardarComentario(any());
    }*/


}
