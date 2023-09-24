package com.tallerwebi.servicios;

import com.tallerwebi.dominio.entidades.Noticia;
import com.tallerwebi.dominio.servicios.ServicioNoticia;
import com.tallerwebi.dominio.servicios.ServicioNoticiaImpl;
import com.tallerwebi.infraestructura.RepositorioCategoria;
import com.tallerwebi.infraestructura.RepositorioNoticia;
import com.tallerwebi.presentacion.ControladorNoticia;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class ServicioNoticiaTest {

    private ControladorNoticia controladorNoticia;
    private ServicioNoticia servicioNoticiaMock;
    private Noticia noticiaMock;
    private RepositorioNoticia repositorioCategoriaMock;
    private HttpServletRequest requestMock;
    private HttpSession sessionMock;

    private ServicioNoticia servicioNoticia;
    private RepositorioNoticia repositorioNoticia;

    @BeforeEach
    public void init(){
        /*
        noticiaMock = mock(Noticia.class);
        when(noticiaMock.getIdNoticia()).thenReturn(1L);
        when(noticiaMock.getTitulo()).thenReturn("titulo");
        when(noticiaMock.getCategoria()).thenReturn("categoria");

        requestMock = mock(HttpServletRequest.class);
        sessionMock = mock(HttpSession.class);

        servicioNoticiaMock = mock(ServicioNoticia.class);
        controladorNoticia = new ControladorNoticia(servicioNoticiaMock);
        */
        noticiaMock = mock(Noticia.class);
        when(noticiaMock.getIdNoticia()).thenReturn(1L);

        this.repositorioNoticia = mock(RepositorioNoticia.class);
        this.servicioNoticia = new ServicioNoticiaImpl(this.repositorioNoticia);
    }


    @Test
    public void cuandoListoLasNoticiasObtengoTresNoticias(){
        //preparacion
        //creo una coleccion de noticias y cargo 3, luego digo que cuando se ejecute el listarNoticias del repo retorne esa coleccion de noticas
        List<Noticia> noticias = new ArrayList<>();
        noticias.add(noticiaMock);
        noticias.add(noticiaMock);
        noticias.add(noticiaMock);

        when(this.repositorioNoticia.listarNoticias()).thenReturn(noticias);

        //ejecucion (aca se ejecuta el listarNoticias del repo, interno al servicio)
        List<Noticia> noticiasObtenidas = servicioNoticia.listarNoticias();

        //verificacion (evaluo si no esta vacio y si es 3 la cantidad de noticias que retorno)
        assertThat(noticiasObtenidas, not(empty()));
        assertThat(noticiasObtenidas.size(), is(3));
    }

    @Test
    public void cuandoObtengoUnaNoticiaPorSuIdObtengoLaNoticia(){
        //preparacion
        when(this.repositorioNoticia.buscarPorId(noticiaMock.getIdNoticia())).thenReturn(noticiaMock);

        //ejecucion
        Noticia noticiaObtenida = this.servicioNoticia.buscarNoticiaPorId(noticiaMock.getIdNoticia());

        //verificacion
        assertThat(noticiaObtenida.getIdNoticia(), is(1L));
    }


}