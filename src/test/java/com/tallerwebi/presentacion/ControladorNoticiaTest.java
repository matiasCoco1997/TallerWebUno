package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.Noticia;
import com.tallerwebi.dominio.servicios.ServicioNoticia;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;

import static org.mockito.Mockito.*;



public class ControladorNoticiaTest {

    private ControladorNoticia controladorNoticia;
    private ServicioNoticia servicioNoticiaMock;
    private Noticia noticiaMock;
    private HttpServletRequest requestMock;
    private HttpSession sessionMock;

    private MultipartFile imgMock;
    private MultipartFile audioMock;

    @BeforeEach
    public void init(){
        noticiaMock = mock(Noticia.class);
        noticiaMock.setCategoria("1");
        when(noticiaMock.getIdNoticia()).thenReturn(1L);
        when(noticiaMock.getTitulo()).thenReturn("titulo");
        when(noticiaMock.getCategoria()).thenReturn("1");

        imgMock = mock(MultipartFile.class);
        requestMock = mock(HttpServletRequest.class);
        sessionMock = mock(HttpSession.class);

        audioMock = mock(MultipartFile.class);

        servicioNoticiaMock = mock(ServicioNoticia.class);
        controladorNoticia = new ControladorNoticia(servicioNoticiaMock);
    }
/*
    @Test
    public void queAlListarDosNoticiasSeCargueElHome() {

        List<Noticia> noticias = new ArrayList<>();

        noticias.add(noticiaMock);
        noticias.add(noticiaMock);
        when(servicioNoticiaMock.listarNoticias()).thenReturn(noticias);

        // ejecucion
        ModelAndView modelAndView = controladorNoticia.listarNoticias(sessionMock);
        List<Noticia> noticiasEnModelo = (List<Noticia>) modelAndView.getModel().get("noticias");

        // validacion
        assertThat(noticiasEnModelo.size(), equalTo(2));
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("home"));
    }



    @Test
    public void queAlListarLasNoticiasEnElHomeRetorneUnaException() throws Exception {
        // preparacion
        when(controladorNoticia.listarNoticias(sessionMock)).thenThrow(RuntimeException.class);

        // ejecucion
        ModelAndView modelAndView = controladorNoticia.listarNoticias(sessionMock);

        // validacion
        assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("Error al listar las noticias."));
    }

 */

    @Test
    public void queAlCrearUnaNoticiaRedireccioneAlHome() {
        // ejecucion
        ModelAndView modelAndView = controladorNoticia.crearNuevaNoticia(noticiaMock, sessionMock, imgMock, audioMock);

        // validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/home"));
    }

    @Test
    public void queAlCrearUnaNoticiaRetorneUnaException() throws Exception {
        // preparacion
        when(controladorNoticia.crearNuevaNoticia(noticiaMock, sessionMock, imgMock, audioMock)).thenThrow(RuntimeException.class);

        // ejecucion
        ModelAndView modelAndView = controladorNoticia.crearNuevaNoticia(noticiaMock, sessionMock, imgMock, audioMock);

        // validacion
        assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("Error al crear la noticia."));
    }

    @Test
    public void queCuandoSeBorreUnaNoticiaRedireccioneAlHome(){
        // ejecucion
        ModelAndView modelAndView = controladorNoticia.borrarNoticiaPorId(noticiaMock.getIdNoticia());

        // validacion
        assertThat(modelAndView.getViewName() , equalToIgnoringCase("home"));
    }

    @Test
    public void queCuandoSeBorreUnaNoticiaRetorneUnaException() throws Exception {
        // preparacion
        when(controladorNoticia.borrarNoticiaPorId(noticiaMock.getIdNoticia())).thenThrow(RuntimeException.class);

        // ejecucion
        ModelAndView modelAndView = controladorNoticia.borrarNoticiaPorId(noticiaMock.getIdNoticia());

        // validacion
        assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("Error al borrar la noticia."));
    }

    /*
    @Test
    public void buscarNoticiasPorCategoriaYLasCargueEnElHome(){
        // ejecucion
        ModelAndView modelAndView = controladorNoticia.buscarNoticiaPorCategoria(noticiaMock.getCategoria());

        // validacion
        assertThat(modelAndView.getViewName() , equalToIgnoringCase("home"));
    }



    @Test
    public void buscarNoticiasPorCategoriaYRetorneUnaException() throws Exception {
        // preparacion
        when(servicioNoticiaMock.buscarNoticiaPorCategoria(noticiaMock.getCategoria())).thenThrow(RuntimeException.class);

        // ejecucion
        ModelAndView modelAndView = controladorNoticia.buscarNoticiaPorCategoria(noticiaMock.getCategoria());

        // validacion
        assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("Error al buscar noticia por categoria."));
    }
    */
    @Test
    public void buscarNoticiasPorTituloYLasCargueEnElHome(){
        // ejecucion
        ModelAndView modelAndView = controladorNoticia.buscarNoticiaPorTitulo(noticiaMock.getTitulo());

        // validacion
        assertThat(modelAndView.getViewName() , equalToIgnoringCase("home"));
    }

    @Test
    public void buscarNoticiasPorTituloYRetorneUnaException() throws Exception {
        // preparacion
        when(servicioNoticiaMock.buscarNoticiaPorTitulo(noticiaMock.getTitulo())).thenThrow(RuntimeException.class);

        // ejecucion
        ModelAndView modelAndView = controladorNoticia.buscarNoticiaPorTitulo(noticiaMock.getTitulo());

        // validacion
        assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("Error al buscar noticia."));
    }

    @Test
    public void queAlDarMeGustaRedireccioneAlHome(){
        //preparacion
        when(noticiaMock.getLikes()).thenReturn(1);
        when(servicioNoticiaMock.verificarQueNoEsNull(noticiaMock)).thenReturn(false);
        ModelAndView model=new ModelAndView();
        //ejecucion
        try {
             model = controladorNoticia.darLike(1L,sessionMock);
        }catch (Exception e){

        }
        //validacion
        assertThat(model.getViewName(), equalToIgnoringCase("redirect:/home"));
    }

    @Test
    public void queAlDarMegustaTireUnaExcepcionPorqueLaNoticiaFueEliminada(){

        Noticia n1 = new Noticia();
        controladorNoticia.crearNuevaNoticia(n1,sessionMock,imgMock,audioMock);
        controladorNoticia.borrarNoticiaPorId(n1.getIdNoticia());

        try {
            ModelAndView modelo = controladorNoticia.darLike(1L,sessionMock);
        }catch (Exception e){
            assertThat(e.getMessage(),is("La noticia fue eliminada"));
        }
    }
}
