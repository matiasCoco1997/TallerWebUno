package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.infraestructura.RepositorioCategoria;
import com.tallerwebi.dominio.Entidades.Noticia;
import com.tallerwebi.dominio.Servicios.ServicioNoticia;
import com.tallerwebi.dominio.Entidades.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.not;


public class ControladorNoticiaTest {

    private ControladorNoticia controladorNoticia;
    private Usuario usuarioMock;
    private ServicioNoticia servicioNoticiaMock;

    private Noticia noticiaMock;

    private RepositorioCategoria repositorioCategoriaMock;
    private HttpServletRequest requestMock;
    private HttpSession sessionMock;

    @BeforeEach
    public void init(){

       usuarioMock = mock(Usuario.class);
        when(usuarioMock.getNombre()).thenReturn("nombre");
        when(usuarioMock.getApellido()).thenReturn("apellido");
        when(usuarioMock.getPais()).thenReturn("pais");
        when(usuarioMock.getCiudad()).thenReturn("ciudad");
        when(usuarioMock.getFechaDeNacimiento()).thenReturn("01/01/2000");
        when(usuarioMock.getEmail()).thenReturn("dami@unlam.com");
        when(usuarioMock.getPassword()).thenReturn("password");
        when(usuarioMock.getFotoPerfil()).thenReturn("/fotoPerfil");
        when(usuarioMock.getRol()).thenReturn("Lector");

        noticiaMock = mock(Noticia.class);
        when(noticiaMock.getIdNoticia()).thenReturn(1L);
        when(noticiaMock.getTitulo()).thenReturn("titulo");
        when(noticiaMock.getCategoria()).thenReturn("categoria");
        when(noticiaMock.getDescripcion()).thenReturn("descripcion");
        when(noticiaMock.getNoticia()).thenReturn("noticia");
        when(noticiaMock.getImagen()).thenReturn("imagen");
        when(noticiaMock.getFechaPublicacion()).thenReturn("fecha");
        when(noticiaMock.getAudioPodcastRuta()).thenReturn("audio");
        when(noticiaMock.getActiva()).thenReturn(true);

        requestMock = mock(HttpServletRequest.class);
        sessionMock = mock(HttpSession.class);

        servicioNoticiaMock = mock(ServicioNoticia.class);

        repositorioCategoriaMock = mock(RepositorioCategoria.class);

        controladorNoticia = new ControladorNoticia(servicioNoticiaMock);
    }


    @Test
    public void queAlCrearUnaNoticiaRedireccioneAlHome() {

        // preparacion

        // ejecucion
        ModelAndView modelAndView = controladorNoticia.crearNuevaNoticia(noticiaMock);

        // validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/home"));

    }



    @Test
    public void queNoRedireccioneAUnaVistaIncorrectaAlCrearUnaNoticia(){
        when(noticiaMock.getTitulo()).thenReturn("titulo");
        // preparacion
        ModelAndView modelAndView = controladorNoticia.crearNuevaNoticia(noticiaMock);

        // ejecucion
        String vista = modelAndView.getViewName() + "11";

        // validacion
        assertThat(vista, not(equalToIgnoringCase("redirect:/home")));
    }

    @Test
    public void queCuandoSeBorreUnaNoticiaMeRedirijaANoticiaBorrada(){
        // preparacion

        // ejecucion
        ModelAndView modelAndView = controladorNoticia.borrarNoticiaPorId(noticiaMock.getIdNoticia());

        // validacion
        assertThat(modelAndView.getViewName() , equalToIgnoringCase("redirect:/noticiaBorrada"));
    }


    @Test
    public void queRedirijaAnoticiaBuscadaLuegoDeBuscarUnaNoticiaPorId(){
        // preparacion

        // ejecucion
        ModelAndView modelAndView = controladorNoticia.buscarNoticiaPorId(noticiaMock.getIdNoticia());

        // validacion
        assertThat(modelAndView.getViewName() , equalToIgnoringCase("redirect:/noticiaBuscada"));
    }







}
