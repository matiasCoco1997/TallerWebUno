package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.Rol;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.servicios.ServicioAdmin;
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
    private ServicioAdmin servicioAdminMock;
    @BeforeEach
    public void init(){
        sessionMock = mock(HttpSession.class);
        usuarioMock = mock(Usuario.class);
        servicioAdminMock = mock(ServicioAdmin.class);
        controladorAdminHome = new ControladorAdminHome(servicioAdminMock);
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

    @Test
    public void cuandoElUsuarioEntraAlHomeVeLasNoticiasPorCategoria() {
        //preparación
        when(usuarioMock.getRol()).thenReturn(Rol.ADMIN);
        when(sessionMock.getAttribute("sessionUsuarioLogueado")).thenReturn(usuarioMock);
        Integer totalDeportes = 1;
        Integer totalPolitica = 2;
        Integer totalProgramacion = 3;
        Integer totalArte = 4;
        Integer totalJuegos = 5;
        when(servicioAdminMock.obtenerNroNoticiasPorCategoria("Deportes")).thenReturn(totalDeportes);
        when(servicioAdminMock.obtenerNroNoticiasPorCategoria("Politica")).thenReturn(totalPolitica);
        when(servicioAdminMock.obtenerNroNoticiasPorCategoria("Programacion")).thenReturn(totalProgramacion);
        when(servicioAdminMock.obtenerNroNoticiasPorCategoria("Arte")).thenReturn(totalArte);
        when(servicioAdminMock.obtenerNroNoticiasPorCategoria("Juegos")).thenReturn(totalJuegos);
        //ejecución
        ModelAndView modelAndView = controladorAdminHome.irAHomeAdmin(sessionMock);
        //verificación
        assertThat(modelAndView.getModel().get("deportes"), equalTo(totalDeportes));
        assertThat(modelAndView.getModel().get("politica"), equalTo(totalPolitica));
        assertThat(modelAndView.getModel().get("programacion"), equalTo(totalProgramacion));
        assertThat(modelAndView.getModel().get("arte"), equalTo(totalArte));
        assertThat(modelAndView.getModel().get("juegos"), equalTo(totalJuegos));
    }
}
