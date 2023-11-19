package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.Rol;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.servicios.ServicioHome;
import com.tallerwebi.dominio.servicios.ServicioNoticia;
import com.tallerwebi.dominio.servicios.ServicioUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ControladorAdminTest {
    private Usuario usuarioMock;
    private ControladorAdmin controladorAdmin;
    private HttpSession sessionMock;
    private ServicioHome servicioHomeMock;
    private ServicioNoticia servicioNoticiaMock;
    private ServicioUsuario servicioUsuarioMock;
    @BeforeEach
    public void init(){
        servicioHomeMock = mock(ServicioHome.class);
        servicioUsuarioMock = mock(ServicioUsuario.class);
        servicioNoticiaMock = mock(ServicioNoticia.class);
        sessionMock = mock(HttpSession.class);
        usuarioMock = mock(Usuario.class);
        controladorAdmin = new ControladorAdmin(servicioHomeMock,servicioUsuarioMock,servicioNoticiaMock);
    }
    @Test
    public void siElUsuarioEstaLogueadoRedirigeAlHomeAdmin(){
        when(usuarioMock.getRol()).thenReturn(Rol.ADMIN);
        when(sessionMock.getAttribute("sessionUsuarioLogueado")).thenReturn(usuarioMock);
        ModelAndView resultado= controladorAdmin.irAHomeAdmin(sessionMock);
        assertThat("home-admin",equalTo(resultado.getViewName()));
    }
    @Test
    public void siElUsuarioEstaLogueadoRedirigeAlLoginSiElRolEsUser(){
        when(usuarioMock.getRol()).thenReturn(Rol.USER);
        when(sessionMock.getAttribute("sessionUsuarioLogueado")).thenReturn(usuarioMock);
        ModelAndView resultado= controladorAdmin.irAHomeAdmin(sessionMock);
        assertThat("redirect:/login",equalTo(resultado.getViewName()));
    }
    @Test
    public void siElUsuarioNoEstaLogueadoRedirigeAlLogin(){
        when(sessionMock.getAttribute("sessionUsuarioLogueado")).thenReturn(null);
        ModelAndView resultado= controladorAdmin.irAHomeAdmin(sessionMock);
        assertThat("redirect:/login",equalTo(resultado.getViewName()));
    }

    @Test
    public void queSeRedirijaALaVistaCuandoSeQuieraVerLasNoticiasMasLikeadas(){
        when(sessionMock.getAttribute(anyString())).thenReturn(usuarioMock);
        when(usuarioMock.getRol()).thenReturn(Rol.ADMIN);
        ModelAndView model = controladorAdmin.mostrarNoticiasMasLikeadas(sessionMock);
        assertThat(model.getViewName(), equalToIgnoringCase("noticias-mas-likeadas"));
    }
}
