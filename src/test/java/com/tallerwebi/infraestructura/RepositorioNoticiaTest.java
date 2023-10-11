package com.tallerwebi.infraestructura;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.tallerwebi.dominio.entidades.Noticia;

import com.tallerwebi.dominio.servicios.ServicioNoticia;
import com.tallerwebi.presentacion.ControladorNoticia;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.tallerwebi.integracion.config.HibernateTestConfig;
import com.tallerwebi.integracion.config.SpringWebTestConfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = { SpringWebTestConfig.class, HibernateTestConfig.class })
public class RepositorioNoticiaTest {

    @Autowired
    private RepositorioNoticia repositorioNoticia;
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
    public void guardarNoticiaDeberiaPersistirla() {
        Noticia noticia = new Noticia();
        noticia.setTitulo("Titulo de la noticia");
        repositorioNoticia.guardar(noticia);

        Long idNoticiaGuardada = noticia.getIdNoticia();
        Noticia buscada = repositorioNoticia.buscarPorId(idNoticiaGuardada);

        assertThat(buscada, is(notNullValue()));
        assertThat(buscada.getIdNoticia(), is(idNoticiaGuardada));
    }

    @Transactional
    @Rollback
    @Test
    public void buscarPorTituloPuedeTraerMasDeUnaNoticia() {
        Noticia noticia = new Noticia();
        noticia.setTitulo("Titulo de la noticia");
        repositorioNoticia.guardar(noticia);

        Noticia noticia2 = new Noticia();
        noticia2.setTitulo("Titulo de la noticia");
        repositorioNoticia.guardar(noticia2);

        List<Noticia> buscadas = repositorioNoticia.buscarPorTitulo("Titulo de la noticia");

        assertThat(buscadas,hasSize(2));

    }
    @Transactional
    @Rollback
    @Test
    public void queSePuedaEliminarUnaNoticia() {
        Noticia noticia = new Noticia();
        noticia.setTitulo("Titulo de la noticia");
        repositorioNoticia.guardar(noticia);

        repositorioNoticia.borrarNoticia(noticia);
        List<Noticia> buscadas = repositorioNoticia.buscarPorTitulo("Titulo de la noticia");

        assertThat(buscadas,hasSize(0));

    }

    @Transactional
    @Rollback
    @Test
    public void queSePuedaModificarUnaNoticia() {
        Noticia noticia = new Noticia();
        noticia.setTitulo("Título de la noticia");
        noticia.setDescripcion("Descripción de la noticia");
        noticia.setCategoria("Categoría de la noticia");
        noticia.setResumen("Contenido de la noticia");
        noticia.setRutaDeimagen("URL de la imagen");
        noticia.setFechaDePublicacion("Fecha de publicación");
        noticia.setRutaDeAudioPodcast("Ruta del archivo de audio del podcast");
        noticia.setActiva(true);

        repositorioNoticia.guardar(noticia);
        noticia.setTitulo("Titulo de la noticia");

        String nuevoTitulo = "Nuevo título de la noticia";
        noticia.setTitulo(nuevoTitulo);

        repositorioNoticia.modificar(noticia);
        Noticia noticiaModificada = repositorioNoticia.buscarPorId(noticia.getIdNoticia());

        assertThat(noticiaModificada, is(notNullValue()));
        assertThat(nuevoTitulo, is(noticiaModificada.getTitulo()));
        assertThat(noticia.getCategoria(), is(noticiaModificada.getCategoria()));
        assertThat(noticia.getIdNoticia(), is(noticiaModificada.getIdNoticia()));
    }

    @Transactional
    @Rollback
    @Test
    public void queSePuedaBuscarPorCategoriaDeberiaTraerNoticiasDeCategoriaEspecifica() {
        Noticia noticia1 = new Noticia();
        noticia1.setTitulo("Titulo de la noticia");
        noticia1.setCategoria("Deportes");
        repositorioNoticia.guardar(noticia1);

        Noticia noticia2 = new Noticia();
        noticia2.setTitulo("Titulo de la noticia");
        noticia2.setCategoria("Política");
        repositorioNoticia.guardar(noticia2);

        List<Noticia> noticiasDeDeportes = repositorioNoticia.buscarPorCategoria("Deportes");

        assertThat(noticiasDeDeportes, hasSize(1));
        assertThat(noticiasDeDeportes.get(0).getCategoria(), is("Deportes"));
    }

    @Transactional
    @Rollback
    @Test
    public void guardarNoticiaSinTituloDevuelvaFalse() {
        Noticia noticiaSinTitulo = new Noticia();

        Boolean resultado = repositorioNoticia.guardar(noticiaSinTitulo);

        assertFalse(resultado);
    }

    @Transactional
    @Rollback
    @Test
    public void cuandoSeCreaUnaNoticiaNuevaTieneCeroMeGusta() {
        //preparación
        Noticia noticia = new Noticia();
        noticia.setTitulo("Título de la noticia");
        //ejecución
        repositorioNoticia.guardar(noticia);
        Noticia noticiaObtenida=repositorioNoticia.buscarPorId(noticia.getIdNoticia());
        //validación
        assertThat(noticiaObtenida.getLikes(), is(0));
    }
}
