package com.tallerwebi.servicios;

import com.tallerwebi.dominio.entidades.Categoria;
import com.tallerwebi.dominio.entidades.Noticia;
import com.tallerwebi.dominio.entidades.Republicacion;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.servicios.ServicioHome;
import com.tallerwebi.dominio.servicios.ServicioHomeImpl;
import com.tallerwebi.infraestructura.RepositorioHome;
import com.tallerwebi.infraestructura.RepositorioLike;
import com.tallerwebi.infraestructura.RepositorioUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class ServicioHomeTest {

    private Noticia noticiaMock;
    private Noticia noticiaMockDeportes;
    private Noticia noticiaMockArte;
    private ServicioHome servicioHomeMock;
    private RepositorioHome repositorioHomeMock;
    private RepositorioUsuario repositorioUsuarioMock;
    private Usuario usuarioMock;
    private Categoria categoriaMock;
    private RepositorioLike repositorioLikeMock;
    private Republicacion republicacionMock;

    @BeforeEach
    public void init(){
        noticiaMock = mock(Noticia.class);
        republicacionMock = mock(Republicacion.class);
        noticiaMockArte = mock(Noticia.class);
        noticiaMockDeportes = mock(Noticia.class);
        when(noticiaMock.getCategoria()).thenReturn("categoria");
        when(noticiaMock.getTitulo()).thenReturn("categoria");
        when(noticiaMockArte.getCategoria()).thenReturn("arte");
        when(noticiaMockArte.getTitulo()).thenReturn("depor");
        when(noticiaMockDeportes.getCategoria()).thenReturn("deportes");
        when(noticiaMockDeportes.getTitulo()).thenReturn("deportes");
        usuarioMock = mock(Usuario.class);
        categoriaMock = mock(Categoria.class);
        repositorioLikeMock = mock(RepositorioLike.class);
        
        this.repositorioHomeMock = mock(RepositorioHome.class);
        this.repositorioUsuarioMock = mock(RepositorioUsuario.class);
        this.servicioHomeMock = new ServicioHomeImpl(this.repositorioHomeMock, repositorioUsuarioMock, repositorioLikeMock);
    }

    @Test
    public void cuandoListoLasNoticiasObtengoTresNoticias(){
        List<Noticia> noticias = new ArrayList<>();
        noticias.add(noticiaMock);
        noticias.add(noticiaMock);
        noticias.add(noticiaMock);

        when(this.repositorioHomeMock.listarNoticias()).thenReturn(noticias);

        List<Noticia> noticiasObtenidas = servicioHomeMock.listarNoticias();

        assertThat(noticiasObtenidas, not(empty()));
        assertThat(noticiasObtenidas.size(), is(3));
    }

    @Test
    public void cuandoListoLosUsuariosObtengoTresUsuarios(){
        List<Usuario> usuarios = new ArrayList<>();
        usuarios.add(usuarioMock);
        usuarios.add(usuarioMock);
        usuarios.add(usuarioMock);

        when(this.repositorioHomeMock.listarUsuarios(1L)).thenReturn(usuarios);

        List<Usuario> usuariosObtenidos = servicioHomeMock.listarUsuarios(1L);

        assertThat(usuariosObtenidos, not(empty()));
        assertThat(usuariosObtenidos.size(), is(3));
    }

    @Test
    public void cuandoListoLasCategoriasObtengoTresCategorias(){
        List<Categoria> categorias = new ArrayList<>();
        categorias.add(categoriaMock);
        categorias.add(categoriaMock);
        categorias.add(categoriaMock);

        when(this.repositorioHomeMock.listarCategorias()).thenReturn(categorias);

        List<Categoria> categoriasObtenidas = servicioHomeMock.obtenerCategorias();

        assertThat(categoriasObtenidas, not(empty()));
        assertThat(categoriasObtenidas.size(), is(3));
    }

    @Test
    public void quePuedaFiltrarLasNoticiasPorCategoria(){
        List<Noticia> noticias= new ArrayList<>();
        noticias.add(noticiaMock);
        noticias.add(noticiaMockArte);
        noticias.add(noticiaMockDeportes);

        List<Noticia> noticiasFiltradas= filtrarPorCategorias(noticias,"deportes");
        when(this.repositorioHomeMock.obtenerNoticiasPorCategoria("deportes")).thenReturn(noticiasFiltradas);

        List<Noticia> noticiasObtenidas= servicioHomeMock.obtenerNoticiasPorCategoria("deportes");
        assertThat(noticiasObtenidas, not(empty()));
        assertThat(noticiasObtenidas.size(), is(1));
    }

    @Test
    public void quePuedaFiltrarLasNoticiasPorTitulo(){
        List<Noticia> noticias= new ArrayList<>();
        noticias.add(noticiaMock);
        noticias.add(noticiaMockArte);
        noticias.add(noticiaMockDeportes);

        List<Noticia> noticiasFiltradas= filtrarPorTitulo(noticias,"depor");
        when(this.repositorioHomeMock.obtenerNoticiasPorTitulo("depor")).thenReturn(noticiasFiltradas);

        List<Noticia> noticiasObtenidas= servicioHomeMock.obtenerNoticiasPorTitulo("depor");
        assertThat(noticiasObtenidas, not(empty()));
        assertThat(noticiasObtenidas.size(), is(2));
    }

    private List<Noticia> filtrarPorCategorias(List<Noticia> noticias, String categoria){
        List<Noticia> noticiasFiltradas=new ArrayList<>();
        for (Noticia noticia: noticias) {
            if(noticia.getCategoria().equals(categoria)){
                noticiasFiltradas.add(noticia);
            }
        }
        return noticiasFiltradas;
    }
    private List<Noticia> filtrarPorTitulo(List<Noticia> noticias, String titulo){
        List<Noticia> noticiasFiltradas=new ArrayList<>();
        for (Noticia noticia: noticias) {
            if(noticia.getTitulo().contains(titulo)){
                noticiasFiltradas.add(noticia);
            }
        }
        return noticiasFiltradas;
    }
    @Test
    public void queSePuedaListarNoticiasDeSeguidos() {
        List<Noticia> noticiasEsperadas = Arrays.asList(new Noticia(), new Noticia());
        when(repositorioUsuarioMock.obtenerNoticiaDeSeguidos(1L)).thenReturn(noticiasEsperadas);
        List<Noticia> usuariosRecomendados = servicioHomeMock.obtenerNoticiaDeSeguidos(anyLong());

        verify(repositorioUsuarioMock, times(1)).obtenerNoticiaDeSeguidos(anyLong());
    }


    @Test
    public void queSePuedaObtenerUnaListaDePublicacionesYRepublicaciones(){
        List<Noticia> noticias= new ArrayList<>();
        noticias.add(noticiaMock);
        noticias.add(noticiaMockArte);
        noticias.add(noticiaMockDeportes);
        List<Republicacion> republicaciones=new ArrayList<>();
        republicaciones.add(republicacionMock);
        republicaciones.add(republicacionMock);

        when(repositorioHomeMock.listarNoticias()).thenReturn(noticias);
        when(repositorioHomeMock.obtenerRepublicaciones()).thenReturn(republicaciones);
        List<Object> listaFinal=servicioHomeMock.obtenerPosts();
        assertThat(listaFinal.size(),is(5));
    }

   /*
   *  @Test
    public void queLaListaDeNoticiasYRepublicacionesEsteOrdenadaDeFormaAleatoria(){
        List<Noticia> noticias= new ArrayList<>();
        noticias.add(noticiaMock);
        noticias.add(noticiaMockArte);
        noticias.add(noticiaMockDeportes);
        List<Republicacion> republicaciones=new ArrayList<>();
        republicaciones.add(new Republicacion());
        republicaciones.add(new Republicacion());

        when(repositorioHomeMock.listarNoticias()).thenReturn(noticias);
        when(repositorioHomeMock.obtenerRepublicaciones()).thenReturn(republicaciones);
        List<Object> listaFinal=servicioHomeMock.obtenerPosts();
        assertThat(listaFinal.get(0),is(noticiaMock));
    }*/
}
