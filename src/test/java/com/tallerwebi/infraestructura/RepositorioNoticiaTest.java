package com.tallerwebi.infraestructura;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.tallerwebi.dominio.entidades.*;

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
import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = { SpringWebTestConfig.class, HibernateTestConfig.class })
public class RepositorioNoticiaTest {

    @Autowired
    private RepositorioNoticia repositorioNoticia;
    @Autowired
    private RepositorioLike repositorioLike;
    @Autowired
    private RepositorioUsuario repositorioUsuario;
    private Noticia noticia1;
    private Noticia noticia2;
    private Usuario usuario1;
    private Noticia noticia3;
    private Noticia noticia4;
    private Noticia noticia5;
    private Noticia noticia6;

    @BeforeEach
    public void init(){
        noticia1 = new Noticia();
        noticia1.setCategoria("Deporte");
        noticia1.setTitulo("Titulo Deporte");

        noticia2 = new Noticia();
        noticia2.setCategoria("Tecnología");
        noticia2.setTitulo("Titulo Tecnología");

        noticia3 = new Noticia();
        noticia3.setCategoria("Política");
        noticia3.setTitulo("Titulo Política");

        noticia4 = new Noticia();
        noticia4.setCategoria("Economía");
        noticia4.setTitulo("Titulo Economía");

        noticia5 = new Noticia();
        noticia5.setCategoria("Entretenimiento");
        noticia5.setTitulo("Titulo Entretenimiento");

        noticia6 = new Noticia();
        noticia6.setCategoria("Economía");
        noticia6.setTitulo("Titulo Economía");

        usuario1 = new Usuario();
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
        noticia.setFechaDePublicacion(LocalDateTime.now());
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
    public void queSePuedaGuardarUnaRepublicacion(){
        Republicacion republicacion=new Republicacion();
        repositorioNoticia.republicarNoticia(republicacion);
        List<Republicacion> republicaciones=repositorioNoticia.obtenerRepublicaciones();
        assertThat(republicaciones.size(),is(1));
    }

    /*
    @Transactional
    @Rollback
    @Test
    public void cuandoSeCreaUnaNoticiaNuevaTieneCeroMeGusta() {
        //preparación
        Noticia noticia = new Noticia();
        noticia.setTitulo("Título de la noticia");
        //ejecución
        repositorioNoticia.guardar(noticia);
        List<Usuario> likes = repositorioNoticia.obtenerLikes(noticia.getIdNoticia());
        //validación
        assertThat(likes, hasSize(0));
    }

     */

    @Transactional
    @Rollback
    @Test
    public void cuandoUnaNoticiaRecibeUnMeGustaElTotalDeMeGustaEsUno() {
        //preparación
        /*Usuario usuarioOriginal = new Usuario();
        usuarioOriginal.setIdUsuario(1L);
        Noticia noticia = new Noticia();
        noticia.setIdNoticia(1L);
        noticia.setTitulo("Título de la noticia");
        repositorioNoticia.guardar(noticia);
        //ejecución
        repositorioNoticia.darMeGusta(noticia, usuarioOriginal);
        Usuario usuarioObtenido = noticia.getLikes().get(0);
        //validación
        assertThat(usuarioOriginal.getIdUsuario(), is(usuarioObtenido.getIdUsuario()));

         */
    }
    @Transactional
    @Rollback
    @Test
    public void queSepuedaObtenerUnaListaDeNoticiasPorCategoriasDeNoticiasConMeGustaDeUnUsuario(){
        repositorioNoticia.guardar(noticia1);
        repositorioNoticia.guardar(noticia2);

        repositorioUsuario.guardar(usuario1);

        MeGusta meGusta1 = new MeGusta();
        meGusta1.setNoticia(noticia1);
        meGusta1.setUsuario(usuario1);

        MeGusta meGusta2 = new MeGusta();
        meGusta2.setNoticia(noticia2);
        meGusta2.setUsuario(usuario1);

        repositorioLike.guardarLike(meGusta1);
        repositorioLike.guardarLike(meGusta2);

        List<String> categorias = repositorioLike.traerCategoriasLikeadasPorUnUsuario(usuario1.getIdUsuario());

        List<Noticia> noticias = repositorioNoticia.obtenerNoticiasCategoria( 5, categorias);

        assertEquals("Deporte", categorias.get(0));
        assertEquals("Tecnología", categorias.get(1));
    }
    @Transactional
    @Rollback
    @Test
    public void ObtenerNoticiasPorCategoriaMeGustaNoCumpleConLaCantidadRellenaConNoticiasRecienPublicadas(){
        repositorioNoticia.guardar(noticia1);
        repositorioNoticia.guardar(noticia2);
        repositorioNoticia.guardar(noticia3);
        repositorioNoticia.guardar(noticia4);
        repositorioNoticia.guardar(noticia5);
        repositorioNoticia.guardar(noticia6);

        repositorioUsuario.guardar(usuario1);

        MeGusta meGusta1 = new MeGusta();
        meGusta1.setNoticia(noticia1);
        meGusta1.setUsuario(usuario1);

        MeGusta meGusta2 = new MeGusta();
        meGusta2.setNoticia(noticia2);
        meGusta2.setUsuario(usuario1);

        repositorioLike.guardarLike(meGusta1);
        repositorioLike.guardarLike(meGusta2);

        List<String> categorias = repositorioLike.traerCategoriasLikeadasPorUnUsuario(usuario1.getIdUsuario());

        List<Noticia> noticias = repositorioNoticia.obtenerNoticiasCategoria(5, categorias);

        assertEquals(5, noticias.size());
    }
    @Transactional
    @Rollback
    @Test
    public void ObtenerNoticiasPorCategoriaMeGustaNoCumpleConLaCantidadCon5NoticiasDeCategoriasQueTienenLike(){
        repositorioNoticia.guardar(noticia1);
        repositorioNoticia.guardar(noticia2);
        repositorioNoticia.guardar(noticia3);
        repositorioNoticia.guardar(noticia4);
        repositorioNoticia.guardar(noticia5);
        repositorioNoticia.guardar(noticia6);

        repositorioUsuario.guardar(usuario1);

        MeGusta meGusta1 = new MeGusta();
        meGusta1.setNoticia(noticia1);
        meGusta1.setUsuario(usuario1);

        MeGusta meGusta2 = new MeGusta();
        meGusta2.setNoticia(noticia2);
        meGusta2.setUsuario(usuario1);

        MeGusta meGusta3 = new MeGusta();
        meGusta3.setNoticia(noticia3);
        meGusta3.setUsuario(usuario1);

        MeGusta meGusta4 = new MeGusta();
        meGusta4.setNoticia(noticia4);
        meGusta4.setUsuario(usuario1);

        MeGusta meGusta5 = new MeGusta();
        meGusta5.setNoticia(noticia5);
        meGusta5.setUsuario(usuario1);

        MeGusta meGusta6 = new MeGusta();
        meGusta6.setNoticia(noticia6);
        meGusta6.setUsuario(usuario1);

        repositorioLike.guardarLike(meGusta1);
        repositorioLike.guardarLike(meGusta2);
        repositorioLike.guardarLike(meGusta3);
        repositorioLike.guardarLike(meGusta4);
        repositorioLike.guardarLike(meGusta5);
        repositorioLike.guardarLike(meGusta6);

        List<String> categorias = repositorioLike.traerCategoriasLikeadasPorUnUsuario(usuario1.getIdUsuario());

        List<Noticia> noticias = repositorioNoticia.obtenerNoticiasCategoria(5, categorias);

        assertEquals(5, noticias.size());
    }
}
