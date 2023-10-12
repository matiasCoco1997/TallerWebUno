package com.tallerwebi.servicios;

import com.tallerwebi.dominio.entidades.Categoria;
import com.tallerwebi.dominio.entidades.Noticia;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.servicios.ServicioHome;
import com.tallerwebi.dominio.servicios.ServicioHomeImpl;
import com.tallerwebi.infraestructura.RepositorioHome;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServicioHomeTest {

    private Noticia noticiaMock;
    private Noticia noticiaMockDeportes;
    private Noticia noticiaMockArte;
    private ServicioHome servicioHomeMock;
    private RepositorioHome repositorioHomeMock;
    private Usuario usuarioMock;
    private Categoria categoriaMock;

    @BeforeEach
    public void init(){
        noticiaMock = mock(Noticia.class);
        noticiaMockArte = mock(Noticia.class);
        noticiaMockDeportes = mock(Noticia.class);
        when(noticiaMock.getCategoria()).thenReturn("categoria");
        when(noticiaMockArte.getCategoria()).thenReturn("arte");
        when(noticiaMockDeportes.getCategoria()).thenReturn("deportes");
        usuarioMock=mock(Usuario.class);
        categoriaMock=mock(Categoria.class);
        this.repositorioHomeMock = mock(RepositorioHome.class);
        this.servicioHomeMock = new ServicioHomeImpl(this.repositorioHomeMock);
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

        List<Noticia> noticiasFiltradas=filtrar(noticias,"deportes");
        when(this.repositorioHomeMock.obtenerNoticiasPorCategoria("deportes")).thenReturn(noticiasFiltradas);

        List<Noticia> noticiasObtenidas= servicioHomeMock.obtenerNoticiasPorCategoria("deportes");
        assertThat(noticiasObtenidas, not(empty()));
        assertThat(noticiasObtenidas.size(), is(1));
    }

    private List<Noticia> filtrar(List<Noticia> noticias, String categoria){
        List<Noticia> noticiasFiltradas=new ArrayList<>();
        for (Noticia noticia: noticias) {
            if(noticia.getCategoria().equals(categoria)){
                noticiasFiltradas.add(noticia);
            }
        }
        return noticiasFiltradas;
    }


}
