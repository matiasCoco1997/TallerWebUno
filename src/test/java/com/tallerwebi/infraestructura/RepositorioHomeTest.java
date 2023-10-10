package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidades.Categoria;
import com.tallerwebi.dominio.entidades.Noticia;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.integracion.config.HibernateTestConfig;
import com.tallerwebi.integracion.config.SpringWebTestConfig;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = { SpringWebTestConfig.class, HibernateTestConfig.class })
public class RepositorioHomeTest {

    @Autowired
    private RepositorioHome repositorioHome;
    @Autowired
    private RepositorioNoticia repositorioNoticia;
    @Autowired
    private RepositorioUsuario repositorioUsuario;
    @Autowired
    private RepositorioCategoria repositorioCategoria;
    private Noticia noticiaMock;

    @BeforeEach
    public void init(){
        noticiaMock = mock(Noticia.class);
        when(noticiaMock.getIdNoticia()).thenReturn(1L);
        when(noticiaMock.getTitulo()).thenReturn("Titulo de la noticia");
        when(noticiaMock.getCategoria()).thenReturn("categoria");
    }


    @Transactional
    @Rollback
    @Test
    public void queAlListarNoticiasActivasDebeTraermeUnaNoticia(){
        Noticia noticia=new Noticia();
        noticia.setTitulo("Titulo de la noticia");
        noticia.setActiva(true);
        Noticia noticia2=new Noticia();
        noticia2.setTitulo("Titulo de la noticia");
        repositorioNoticia.guardar(noticia);
        repositorioNoticia.guardar(noticia2);

        Integer cantidadDeNoticias=repositorioHome.listarNoticias().size();
        assertThat(cantidadDeNoticias,Is.is(1));
    }

    @Transactional
    @Rollback
    @Test
    public void queAlListarUsuariosDebeTraermeDosUsuarios(){
        Usuario usuario=new Usuario();
        Usuario usuario2=new Usuario();
        repositorioUsuario.guardar(usuario);
        repositorioUsuario.guardar(usuario2);
        Integer cantidadDeUsuarios=repositorioHome.listarUsuarios().size();
        assertThat(cantidadDeUsuarios,Is.is(2));
    }
    
    @Transactional
    @Rollback
    @Test
    public void queAlListarCategoriasDebeTraermeDosCategorias(){
        Categoria categoria=new Categoria();
        Categoria categoria2=new Categoria();
        repositorioCategoria.guardar(categoria);
        repositorioCategoria.guardar(categoria2);
        Integer cantidadDeUsuarios=repositorioHome.listarCategorias().size();
        assertThat(cantidadDeUsuarios,Is.is(2));
    }

    @Transactional
    @Rollback
    @Test
    public void queSePuedanFiltrarLasNoticiasPorCategoria(){
        Noticia noticia=new Noticia();
        noticia.setTitulo("titulo");
        Noticia noticia2=new Noticia();
        noticia2.setTitulo("titulo");
        noticia2.setCategoria("deportes");
        repositorioNoticia.guardar(noticia);
        repositorioNoticia.guardar(noticia2);
        Integer cantidadDeNoticias=repositorioHome.obtenerNoticiasPorCategoria("deportes").size();
        assertThat(cantidadDeNoticias,Is.is(1));
    }

}
