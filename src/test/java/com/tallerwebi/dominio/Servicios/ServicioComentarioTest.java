package com.tallerwebi.dominio.Servicios;

import com.tallerwebi.dominio.Entidades.Comentario;
import com.tallerwebi.dominio.excepcion.DescripcionComentarioException;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.infraestructura.RepositorioCategoria;
import com.tallerwebi.infraestructura.RepositorioComentario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ServicioComentarioTest {
    private RepositorioComentario repositorioComentarioMock;
    private ServicioComentario servicioComentario;
    private Comentario comentarioMock;

    @BeforeEach
    public void init(){
        comentarioMock = mock(Comentario.class);
        repositorioComentarioMock = mock(RepositorioComentario.class); // Corrected
        when(comentarioMock.getDescripcion()).thenReturn("Descripcion");
        when(comentarioMock.getIdNoticia()).thenReturn(1L);
        when(comentarioMock.getIdUsuario()).thenReturn(2L);
        servicioComentario = new ServicioComentarioImpl(repositorioComentarioMock);
    }

    @Test
    public void queSePuedaGuardeUnComentatioDeFormaExitosa() throws DescripcionComentarioException {
        servicioComentario.guardarComentario(comentarioMock);
        verify(repositorioComentarioMock, times(1)).guardar(comentarioMock);
    }

    @Test
    public void queAlGuardarUnComentatioSinDescripcionLanzaUnaException() throws DescripcionComentarioException {

    }
}
