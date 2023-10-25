package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.Categoria;
import com.tallerwebi.dominio.entidades.Noticia;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.servicios.ServicioHome;
import com.tallerwebi.dominio.servicios.ServicioNoticia;
import com.tallerwebi.dominio.servicios.ServicioUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ControladorHomeTest {
    private ControladorHome controladorHome;
    private ServicioHome servicioHomeMock;
    private ServicioUsuario servicioUsuarioMock;
    private Noticia noticiaMock;
    private HttpServletRequest requestMock;
    private HttpSession sessionMock;
    private Categoria categoriaMock;
    private Usuario usuarioMock;
    private ServicioNoticia servicioNoticiaMock;
    //private ServicioMeGusta servicioMeGustaMock;

    @BeforeEach
    public void init(){
        noticiaMock = mock(Noticia.class);
        when(noticiaMock.getIdNoticia()).thenReturn(1L);
        when(noticiaMock.getTitulo()).thenReturn("titulo");
        when(noticiaMock.getCategoria()).thenReturn("categoria");
        categoriaMock=mock(Categoria.class);
        when(categoriaMock.getIdCategoria()).thenReturn(1);
        when(categoriaMock.getDescripcion()).thenReturn("Deportes");
        usuarioMock=mock(Usuario.class);
        requestMock = mock(HttpServletRequest.class);
        sessionMock = mock(HttpSession.class);
        servicioHomeMock = mock(ServicioHome.class);
        servicioUsuarioMock = mock(ServicioUsuario.class);
        servicioNoticiaMock = mock(ServicioNoticia.class);
        controladorHome = new ControladorHome(servicioHomeMock, servicioUsuarioMock, servicioNoticiaMock);
    }

    @Test
    public void queAlListarDosNoticiasSeCargueElHome() {

        List<Noticia> noticias = new ArrayList<>();

        noticias.add(noticiaMock);
        noticias.add(noticiaMock);

        when(servicioHomeMock.listarNoticias()).thenReturn(noticias);
        when(servicioNoticiaMock.setNoticiasLikeadas(noticias, usuarioMock.getIdUsuario())).thenReturn(noticias);
        when(sessionMock.getAttribute("sessionUsuarioLogueado")).thenReturn(usuarioMock);

        // ejecucion
        ModelAndView modelAndView = controladorHome.home(sessionMock);
        List<Noticia> noticiasEnModelo = (List<Noticia>) modelAndView.getModel().get("noticias");

        // validacion
        assertThat(noticiasEnModelo.size(), equalTo(2));
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("home-vista"));
    }

    @Test
    public void queAlListarDosCategoriasSeCargueElHome(){
        List<Categoria> categorias=new ArrayList<>();
        categorias.add(categoriaMock);
        categorias.add(categoriaMock);

        when(servicioHomeMock.obtenerCategorias()).thenReturn(categorias);
        when(sessionMock.getAttribute("sessionUsuarioLogueado")).thenReturn(usuarioMock);

        ModelAndView modelAndView= controladorHome.home(sessionMock);
        List<Categoria> categoriasEnModelo = (List<Categoria>) modelAndView.getModel().get("categorias");

        assertThat(categoriasEnModelo.size(),equalTo(2));
    }

    @Test
    public void queAlListarDosUsuariosSeCargueELHome(){
        List<Usuario> usuarios= new ArrayList<>();
        usuarios.add(usuarioMock);
        usuarios.add(usuarioMock);

        when(servicioUsuarioMock.listarUsuarioParaSeguir(usuarioMock.getIdUsuario())).thenReturn(usuarios);
        when(sessionMock.getAttribute("sessionUsuarioLogueado")).thenReturn(usuarioMock);

        ModelAndView modelAndView = controladorHome.home(sessionMock);

        List<Usuario> usuariosEnModelo= (List<Usuario>) modelAndView.getModel().get("usuarios");

        assertThat(usuariosEnModelo.size(),equalTo(2));
    }

    @Test
    public void queHayaUnUsuarioEnEspecificoEnElModelo(){
        when(sessionMock.getAttribute("sessionUsuarioLogueado")).thenReturn(usuarioMock);
        ModelAndView modelAndView=controladorHome.home(sessionMock);
        Usuario usuarioEnModelo= (Usuario) modelAndView.getModel().get("usuario");
        assertThat(usuarioEnModelo,notNullValue());
    }

    @Test
    public void queAlListarDosNoticiasPorTituloSeCarguenEnElHome(){
        List<Noticia> noticias= new ArrayList<>();
        noticias.add(noticiaMock);
        noticias.add(noticiaMock);
        String titulo="titulo";

        when(sessionMock.getAttribute("sessionUsuarioLogueado")).thenReturn(usuarioMock);
        when(servicioHomeMock.obtenerNoticiasPorTitulo(titulo)).thenReturn(noticias);
        when(servicioHomeMock.validarQueHayNoticias(noticias)).thenReturn(false);


        ModelAndView modelAndView= controladorHome.filtrarPorTitulo(titulo,sessionMock);
        List<Noticia> noticiasEnModelo= (List<Noticia>) modelAndView.getModel().get("noticias");

        assertThat(noticiasEnModelo.size(),equalTo(2));
    }

    @Test
    public void siNoHayNoticiasDebeEnviarteUnMensajeDeError(){
        List<Noticia> noticias= new ArrayList<>();
        String titulo="titulo";

        when(servicioHomeMock.validarQueHayNoticias(noticias)).thenReturn(true);
        when(sessionMock.getAttribute("sessionUsuarioLogueado")).thenReturn(usuarioMock);

        ModelAndView modelAndView= controladorHome.filtrarPorTitulo(titulo,sessionMock);
        String mensajeError= (String) modelAndView.getModel().get("error");

        assertThat(mensajeError,is("No se encontraron noticias con este título: "+titulo));
    }
}
