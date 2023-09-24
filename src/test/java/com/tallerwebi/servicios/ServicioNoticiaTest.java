package com.tallerwebi.servicios;

import com.tallerwebi.dominio.entidades.Noticia;
import com.tallerwebi.dominio.servicios.ServicioNoticia;
import com.tallerwebi.dominio.servicios.ServicioNoticiaImpl;
import com.tallerwebi.infraestructura.RepositorioNoticia;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;


public class ServicioNoticiaTest {
    private Noticia noticiaMock;
    private ServicioNoticia servicioNoticiaMock;
    private RepositorioNoticia repositorioNoticiaMock;

    @BeforeEach
    public void init(){
        noticiaMock = mock(Noticia.class);
        when(noticiaMock.getIdNoticia()).thenReturn(1L);
        when(noticiaMock.getTitulo()).thenReturn("titulo");
        when(noticiaMock.getCategoria()).thenReturn("categoria");

        this.repositorioNoticiaMock = mock(RepositorioNoticia.class);
        this.servicioNoticiaMock = new ServicioNoticiaImpl(this.repositorioNoticiaMock);
    }


    @Test
    public void cuandoCreoUnaNoticiaSeInvocaLaFuncionGuardarDelRepositorioSoloUnaVez(){
        //ejecucion (aca se ejecuta el listarNoticias del repo, interno al servicio)
        servicioNoticiaMock.crearNoticia(noticiaMock);

        //verificacion (evaluo si no esta vacio y si es 3 la cantidad de noticias que retorno)
        verify(repositorioNoticiaMock, times(1)).guardar(noticiaMock);
    }

    @Test
    public void cuandoCreoDosNoticiasSeInvocaLaFuncionGuardarDelRepositorioDosVeces(){

        //ejecucion (aca se ejecuta el listarNoticias del repo, interno al servicio)
        servicioNoticiaMock.crearNoticia(noticiaMock);
        servicioNoticiaMock.crearNoticia(noticiaMock);

        //verificacion (evaluo si no esta vacio y si es 3 la cantidad de noticias que retorno)
        verify(repositorioNoticiaMock, times(2)).guardar(noticiaMock);
    }

    @Test
    public void cuandoListoLasNoticiasObtengoTresNoticias(){
        //preparacion
        //creo una coleccion de noticias y cargo 3, luego digo que cuando se ejecute el listarNoticias del repo retorne esa coleccion de noticas
        List<Noticia> noticias = new ArrayList<>();
        noticias.add(noticiaMock);
        noticias.add(noticiaMock);
        noticias.add(noticiaMock);

        when(this.repositorioNoticiaMock.listarNoticias()).thenReturn(noticias);

        //ejecucion (aca se ejecuta el listarNoticias del repo, interno al servicio)
        List<Noticia> noticiasObtenidas = servicioNoticiaMock.listarNoticias();

        //verificacion (evaluo si no esta vacio y si es 3 la cantidad de noticias que retorno)
        assertThat(noticiasObtenidas, not(empty()));
        assertThat(noticiasObtenidas.size(), is(3));
    }

    @Test
    public void cuandoObtengoUnaNoticiaPorSuIdObtengoLaNoticia(){
        //preparacion
        when(this.repositorioNoticiaMock.buscarPorId(noticiaMock.getIdNoticia())).thenReturn(noticiaMock);

        //ejecucion
        Noticia noticiaObtenida = this.servicioNoticiaMock.buscarNoticiaPorId(noticiaMock.getIdNoticia());

        //verificacion
        assertThat(noticiaObtenida.getIdNoticia(), is(1L));
    }

    @Test
    public void cuandoObtengoNoticiasPorUnTituloObtengoDosNoticias(){
        //preparacion
        List<Noticia> noticias = new ArrayList<>();
        noticias.add(noticiaMock);
        noticias.add(noticiaMock);

        when(this.repositorioNoticiaMock.buscarPorTitulo(noticiaMock.getTitulo())).thenReturn(noticias);

        //ejecucion
        List<Noticia> noticiasObtenidas = this.servicioNoticiaMock.buscarNoticiaPorTitulo(noticiaMock.getTitulo());

        //verificacion
        assertThat(noticiasObtenidas, not(empty()));
        assertThat(noticiasObtenidas.size(), is(2));
    }


}