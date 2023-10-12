package com.tallerwebi.servicios;

import com.tallerwebi.dominio.entidades.Comentario;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.excepcion.ComentarioException;
import com.tallerwebi.dominio.servicios.ServicioComentario;
import com.tallerwebi.dominio.servicios.ServicioComentarioImpl;
import com.tallerwebi.infraestructura.RepositorioComentario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class    ServicioComentarioTest {
    private RepositorioComentario repositorioComentarioMock;
    private ServicioComentario servicioComentario;
    private Comentario comentarioMock;
    private Long idUsuarioDistinto = 3L;

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
    public void queSePuedaGuardeUnComentatioDeFormaExitosa() throws ComentarioException {
        servicioComentario.guardarComentario(comentarioMock);
        verify(repositorioComentarioMock, times(1)).guardar(comentarioMock);
    }

    @Test
    public void queAlGuardarUnComentatioSinDescripcionLanzaUnaException() throws ComentarioException {
        Comentario comentario = new Comentario();
        comentario.setDescripcion("");

        try {
            servicioComentario.guardarComentario(comentario);
            fail("No lanzó ComentarioException");
        } catch (ComentarioException e) {
            assertEquals("La descripción debe tener entre 1 y 256 caracteres", e.getMessage());
        }

        verify(repositorioComentarioMock, never()).guardar(comentarioMock);
        verify(repositorioComentarioMock, never()).modificar(comentarioMock);
    }
    @Test
    public void queAlGuardarUnComentatioConDescripcionNullLanzaUnaException() throws ComentarioException {
        Comentario comentario = new Comentario();
        comentario.setDescripcion(null);

        try {
            servicioComentario.guardarComentario(comentario);
            fail("No lanzó ComentarioException");
        } catch (ComentarioException e) {
            assertEquals("La descripción debe tener entre 1 y 256 caracteres", e.getMessage());
        }

        verify(repositorioComentarioMock, never()).guardar(comentarioMock);
        verify(repositorioComentarioMock, never()).modificar(comentarioMock);
    }
    @Test
    public void queSePuedaModificarUnComentatioDeFormaExitosa() throws ComentarioException {
        servicioComentario.modificarComentario(comentarioMock, comentarioMock.getIdUsuario());
        verify(repositorioComentarioMock, times(1)).modificar(comentarioMock);
    }
    @Test
    public void queAlModificarUnComentatioConIdDeUsuarioDistintoLanzaUnaException()  {

        try {
            servicioComentario.modificarComentario(comentarioMock, idUsuarioDistinto);
            fail("No lanzó ComentarioException");
        } catch (ComentarioException e) {
            // En este punto, la excepción fue lanzada y la prueba es exitosa
            assertEquals("Error al editar comentario", e.getMessage());
        }
        verify(repositorioComentarioMock, times(0)).modificar(comentarioMock);
    }
    @Test
    public void queAlModificarUnComentatioSinDescripcionLanzaUnaException()  {
        Comentario comentario = new Comentario();
        comentario.setDescripcion("");
        try {
            servicioComentario.modificarComentario(comentario, comentarioMock.getIdUsuario());
            fail("No lanzó ComentarioException");
        } catch (ComentarioException e) {
            // En este punto, la excepción fue lanzada y la prueba es exitosa
            assertEquals("La descripción debe tener entre 1 y 256 caracteres", e.getMessage());
        }
        verify(repositorioComentarioMock, times(0)).modificar(comentarioMock);
    }
    @Test
    public void queAlModificarUnComentatioConDescripcionNullLanzaUnaException()  {
        Comentario comentario = new Comentario();
        comentario.setDescripcion(null);
        try {
            servicioComentario.modificarComentario(comentario, comentarioMock.getIdUsuario());
            fail("No lanzó ComentarioException");
        } catch (ComentarioException e) {
            // En este punto, la excepción fue lanzada y la prueba es exitosa
            assertEquals("La descripción debe tener entre 1 y 256 caracteres", e.getMessage());
        }
        verify(repositorioComentarioMock, times(0)).modificar(comentarioMock);
    }
}
