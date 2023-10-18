package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.excepcion.RelacionNoEncontradaException;
import com.tallerwebi.dominio.servicios.ServicioUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ControladorSeguirTest {
    private ControladorSeguir controladorSeguirMock;
    private HttpSession sessionMock;
    private ServicioUsuario servicioUsuarioMock;
    private Usuario usuarioMock;
    private MockMvc mockMvc;
    @BeforeEach
    public void init(){
        sessionMock = mock(HttpSession.class);
        servicioUsuarioMock = mock(ServicioUsuario.class);
        controladorSeguirMock = new ControladorSeguir(servicioUsuarioMock);
        usuarioMock = mock(Usuario.class);
        mockMvc = MockMvcBuilders.standaloneSetup(controladorSeguirMock).build();
        when(usuarioMock.getIdUsuario()).thenReturn(1L);
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
        when(sessionMock.getAttribute(anyString())).thenReturn(usuarioMock);
        when(servicioUsuarioMock.obtenerUsuarioPorId(anyLong())).thenReturn(usuarioMock);

        ResponseEntity<Void> respuesta = controladorSeguirMock.seguir(1L, sessionMock);

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
    }
    @Test
    public void siSeEnviaUnIdDeUsuarioParaDejarDeSeguirDeFormaCorrectaSeRespondeConUn204() throws Exception {
        when(servicioUsuarioMock.obtenerUsuarioPorId(anyLong())).thenReturn(usuarioMock);
        doNothing().when(servicioUsuarioMock).dejarDeSeguirUsuario(anyLong(),anyLong());
        when(sessionMock.getAttribute(anyString())).thenReturn(usuarioMock);

        ResponseEntity<Void> respuesta = controladorSeguirMock.dejarDeSeguir(3L,sessionMock);

        assertEquals(HttpStatus.NO_CONTENT, respuesta.getStatusCode());
    }
    @Test
    public void siElUsuarioNoEstaLogueadoRetornaUNAUTHORIZED(){
        when(sessionMock.getAttribute(anyString())).thenReturn(null);

        ResponseEntity<Void> respuesta = controladorSeguirMock.
                dejarDeSeguir(1L,sessionMock);

        assertEquals(HttpStatus.UNAUTHORIZED, respuesta.getStatusCode());
    }
    @Test
    public void siSeEnviaUnUsuarioParaDejarDeSeguirConIdNegativoSeRespondeConUn400(){
        when(sessionMock.getAttribute(anyString())).thenReturn(null);

        ResponseEntity<Void> respuesta = controladorSeguirMock.seguir(-1L, sessionMock);

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
    }
    @Test
    public void siElIdDelUsuarioParaDejarDeSeguirEsNullRetornaBadRequest(){
        when(sessionMock.getAttribute(anyString())).thenReturn(usuarioMock);

        ResponseEntity<Void> respuesta = controladorSeguirMock.
                dejarDeSeguir(null,sessionMock);

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
    }
    @Test
    public void siEsUsuarioADejarDeSeguirNoExisteRetornaBadRequest() throws Exception {
        when(sessionMock.getAttribute(anyString())).thenReturn(usuarioMock);
        doThrow(Exception.class).when(servicioUsuarioMock).obtenerUsuarioPorId(anyLong());

        ResponseEntity<Void> respuesta = controladorSeguirMock.
                dejarDeSeguir(1L,sessionMock);

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        verify(servicioUsuarioMock, times(1)).obtenerUsuarioPorId(1L);
    }

}
