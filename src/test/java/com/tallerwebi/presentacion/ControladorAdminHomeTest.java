package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.Rol;
import com.tallerwebi.dominio.entidades.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ControladorAdminHomeTest {
    private Usuario usuarioMock;
    private ControladorAdminHome controladorAdminHome;
    private HttpSession sessionMock;
    @BeforeEach
    public void init(){
        sessionMock = mock(HttpSession.class);
        usuarioMock = mock(Usuario.class);
        controladorAdminHome = new ControladorAdminHome();
    }
    @Test
    public void siElUsuarioEstaLogueadoRedirigeAlHomeAdmin(){
        when(usuarioMock.getRol()).thenReturn(Rol.ADMIN);
        when(sessionMock.getAttribute("sessionUsuarioLogueado")).thenReturn(usuarioMock);
        ModelAndView resultado= controladorAdminHome.irAHomeAdmin(sessionMock);
        assertThat("home-admin",equalTo(resultado.getViewName()));
    }
    @Test
    public void siElUsuarioEstaLogueadoRedirigeAlLoginSiElRolEsUser(){
        when(usuarioMock.getRol()).thenReturn(Rol.USER);
        when(sessionMock.getAttribute("sessionUsuarioLogueado")).thenReturn(usuarioMock);
        ModelAndView resultado= controladorAdminHome.irAHomeAdmin(sessionMock);
        assertThat("redirect:/login",equalTo(resultado.getViewName()));
    }
    @Test
    public void siElUsuarioNoEstaLogueadoRedirigeAlLogin(){
        when(sessionMock.getAttribute("sessionUsuarioLogueado")).thenReturn(null);
        ModelAndView resultado= controladorAdminHome.irAHomeAdmin(sessionMock);
        assertThat("redirect:/login",equalTo(resultado.getViewName()));
    }
}
