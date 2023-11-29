package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.Rol;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.servicios.ServicioMercadoPago;
import com.tallerwebi.dominio.servicios.ServicioPlan;
import com.tallerwebi.dominio.servicios.ServicioUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

import java.util.Objects;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ControladorPagoTest {
    private ControladorPago controladorPago;
    private ServicioPlan servicioPlanMock;
    private ServicioMercadoPago servicioMercadoPago;
    private ServicioUsuario servicioUsuario;
    private HttpSession sessionMock;
    private Usuario usuarioMock;

    @BeforeEach
    public void init(){
        sessionMock = mock(HttpSession.class);
        servicioPlanMock = mock(ServicioPlan.class);
        servicioMercadoPago = mock(ServicioMercadoPago.class);
        servicioUsuario = mock(ServicioUsuario.class);
        controladorPago = new ControladorPago(servicioPlanMock, servicioMercadoPago, servicioUsuario);
        usuarioMock = mock(Usuario.class);
    }
    @Test
    public void queSeMuestrenLosTiposDePlanesSiEstoyLogueadoYTengoRolUser(){
        when(usuarioMock.getRol()).thenReturn(Rol.USER);
        when(sessionMock.getAttribute(anyString())).thenReturn(usuarioMock);
        ModelAndView respuesta = controladorPago.irAPagos(sessionMock);
        assertEquals("suscripciones", respuesta.getViewName());
    }
    @Test
    public void queSeRedirijaAlHomeSiEstoyLogueadoComoAdmin(){
        when(usuarioMock.getRol()).thenReturn(Rol.ADMIN);
        when(sessionMock.getAttribute(anyString())).thenReturn(usuarioMock);
        ModelAndView respuesta = controladorPago.irAPagos(sessionMock);
        assertEquals("redirect:/home", respuesta.getViewName());
    }
    @Test
    public void queSeRedirijaAlLoginSiNoEstoyLogueado(){
        when(sessionMock.getAttribute(anyString())).thenReturn(null);
        ModelAndView respuesta = controladorPago.irAPagos(sessionMock);
        assertEquals("redirect:/login", respuesta.getViewName());
    }
}
