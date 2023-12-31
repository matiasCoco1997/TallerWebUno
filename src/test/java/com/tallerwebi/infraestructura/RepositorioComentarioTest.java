package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidades.Comentario;
import com.tallerwebi.dominio.entidades.Noticia;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.excepcion.ComentarioException;
import com.tallerwebi.integracion.config.HibernateTestConfig;
import com.tallerwebi.integracion.config.SpringWebTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.net.UnknownServiceException;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = { SpringWebTestConfig.class, HibernateTestConfig.class })
public class    RepositorioComentarioTest {
    @Autowired
    private RepositorioComentario repositorioComentario;
    @Autowired
    private RepositorioNoticia repositorioNoticia;
    @Autowired
    private RepositorioUsuario repositorioUsuario;
    private Comentario comentario;
    private Comentario comentario2;
    private Usuario usuario;
    private Noticia noticia;
    @BeforeEach
    public void init(){
        Noticia noticia = new Noticia();
        noticia.setTitulo("Titulo");
        repositorioNoticia.guardar(noticia);

        Usuario usuario = new Usuario();
        repositorioUsuario.guardar(usuario);
        comentario = new Comentario();
        comentario.setDescripcion("Descripcion");
        comentario.setFechaCreacion(LocalDateTime.now());
        comentario.setUsuario(usuario);
        comentario.setNoticia(noticia);

        comentario2 = new Comentario();
        comentario2.setDescripcion("Descripcion");
        comentario2.setFechaCreacion(LocalDateTime.now());
        comentario2.setUsuario(usuario);
        comentario2.setNoticia(noticia);
    }
    @Transactional
    @Rollback
    @Test
    public void guardarComentarioDeberiaPersistirla() throws ComentarioException {
        repositorioComentario.guardar(comentario);

        Long idComentarioGuardada = comentario.getId();
        Comentario buscada = repositorioComentario.buscarPorId(idComentarioGuardada);

        assertThat(buscada, is(notNullValue()));
        assertThat(buscada.getId(), is(idComentarioGuardada));
    }
    @Transactional
    @Rollback
    @Test
    public void guardarComentarioSinDescripcionDeberiaLanzarExcepcion(){
        assertThrows(ComentarioException.class, () -> {
            repositorioComentario.guardar(new Comentario());
        });
    }

    @Transactional
    @Rollback
    @Test
    public void queAlBuscarComentarioPorIdDeNoticiaDevuelvaUnaListaDeComentarios() throws ComentarioException {
        repositorioComentario.guardar(comentario);
        repositorioComentario.guardar(comentario2);

        Long idNoticia = comentario.getNoticia().getIdNoticia();
        List<Comentario> comentariosEncontrados = repositorioComentario.buscarComentariosPorIdNoticia(idNoticia);

        assertThat(comentariosEncontrados, is(notNullValue()));
        assertThat(comentariosEncontrados.size(), is(2));
    }
    @Transactional
    @Rollback
    @Test
    public void queAlBuscarComentarioPorIdDeNoticiaYNoEncontraloDevuelvaUnaListaVacia() throws ComentarioException {
        List<Comentario> comentariosEncontrados = repositorioComentario.buscarComentariosPorIdNoticia(1L);

        assertThat(comentariosEncontrados, is(notNullValue()));
        assertThat(comentariosEncontrados.size(), is(0));
    }
    @Transactional
    @Rollback
    @Test
    public void queSePuedaEliminarUnComentario() throws ComentarioException {
        repositorioComentario.guardar(comentario);
        repositorioComentario.eliminarComentario(comentario);

        List<Comentario> comentariosEncontrados = repositorioComentario.buscarComentariosPorIdNoticia(1L);

        assertThat(comentariosEncontrados, is(notNullValue()));
        assertThat(comentariosEncontrados.size(), is(0));
    }
    @Transactional
    @Rollback
    @Test
    public void queSePuedaEliminarUnComentarioRetornaTrue() throws ComentarioException {
        repositorioComentario.guardar(comentario);
        Boolean isDeleted =  repositorioComentario.eliminarComentario(comentario);

        List<Comentario> comentariosEncontrados = repositorioComentario.buscarComentariosPorIdNoticia(1L);

        assertThat(true, is(isDeleted));
        assertThat(comentariosEncontrados, is(notNullValue()));
        assertThat(comentariosEncontrados.size(), is(0));
    }
    @Transactional
    @Rollback
    @Test
    public void queNoSePuedaEliminarUnComentarioSiLePasanUnNull() {
        Boolean isDeleted =  repositorioComentario.eliminarComentario(null);

        List<Comentario> comentariosEncontrados = repositorioComentario.buscarComentariosPorIdNoticia(1L);

        assertThat(false, is(isDeleted));
        assertThat(comentariosEncontrados, is(notNullValue()));
        assertThat(comentariosEncontrados.size(), is(0));
    }

}

