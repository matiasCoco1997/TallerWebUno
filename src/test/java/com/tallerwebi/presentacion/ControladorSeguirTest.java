package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.servicios.ServicioUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ControladorSeguirTest {
    private ControladorSeguir controladorSeguirMock;
    private HttpSession sessionMock;
    private ServicioUsuario servicioUsuarioMock;
    private Usuario usuarioMock;
    @BeforeEach
    public void init(){
        sessionMock = mock(HttpSession.class);
        servicioUsuarioMock = mock(ServicioUsuario.class);
        controladorSeguirMock = new ControladorSeguir(servicioUsuarioMock);
        usuarioMock = mock(Usuario.class);
    }
    @Test
    public void siSeEnviaUnUsuarioParaSeguirDeFormaCorrectaSeRespondeConUn204() throws Exception {
        when(usuarioMock.getIdUsuario()).thenReturn(2L);
        when(sessionMock.getAttribute(anyString())).thenReturn(usuarioMock);

        ResponseEntity<Void> respuesta = controladorSeguirMock.seguir(1L,sessionMock);

        assertEquals(HttpStatus.NO_CONTENT, respuesta.getStatusCode());
        verify(servicioUsuarioMock, times(1)).obtenerUsuarioPorId(1L);

    }
    @Test
    public void siSeEnviaUnUsuarioParaSeguirConIdNullSeRespondeConUn400(){
        ResponseEntity<Void> respuesta = controladorSeguirMock.seguir(null, sessionMock);
        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
    }
    @Test
    public void siSeEnviaUnUsuarioParaSeguirConIdNegativoSeRespondeConUn400(){
        ResponseEntity<Void> respuesta = controladorSeguirMock.seguir(-1L, sessionMock);
        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
    }
    @Test
    public void siSeEnviaUnUsuarioParaSeguirYElUsuarioNoEstaLogueado(){
        ResponseEntity<Void> respuesta = controladorSeguirMock.seguir(1L, sessionMock);
        assertEquals(HttpStatus.UNAUTHORIZED, respuesta.getStatusCode());
    }
    @Test
    public void siElUsuarioNoExisteEnLaBaseDeDatosDevuelve400() throws Exception {
        doThrow(Exception.class).when(servicioUsuarioMock).obtenerUsuarioPorId(anyLong());
        when(sessionMock.getAttribute(anyString())).thenReturn(usuarioMock);

        ResponseEntity<Void> respuesta = controladorSeguirMock.seguir(1L, sessionMock);

        assertEquals(HttpStatus.NOT_FOUND, respuesta.getStatusCode());
    }
  /* @Test //Da verde pero la validacion esta mal
    public void queMoSePuedaSeguirSiElSeguidoAunNoLoSigue() throws Exception {
        when(sessionMock.getAttribute(anyString())).thenReturn(usuarioMock);
        when(servicioUsuarioMock.obtenerUsuarioPorId(anyLong())).thenReturn(usuarioMock);

        ResponseEntity<Void> respuesta = controladorSeguirMock.seguir(1L, sessionMock);

        assertEquals(HttpStatus.NO_CONTENT, respuesta.getStatusCode());
    }*/
    @Test
    public void queElUsuarioNoSePuedaSeguirASiMismoManda400() throws Exception {
        when(usuarioMock.getIdUsuario()).thenReturn(1L);
        when(sessionMock.getAttribute(anyString())).thenReturn(usuarioMock);
        when(servicioUsuarioMock.obtenerUsuarioPorId(anyLong())).thenReturn(usuarioMock);

        ResponseEntity<Void> respuesta = controladorSeguirMock.seguir(1L, sessionMock);

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
    }


}
