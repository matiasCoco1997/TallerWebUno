package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidades.Comentario;
import com.tallerwebi.dominio.entidades.Noticia;
import com.tallerwebi.dominio.excepcion.DescripcionComentarioException;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = { SpringWebTestConfig.class, HibernateTestConfig.class })
public class RepositorioComentarioTest {
    @Autowired
    private RepositorioComentario repositorioComentario;
    private Comentario comentario;
    private Comentario comentario2;
    @BeforeEach
    public void init(){
        comentario = new Comentario();
        comentario.setDescripcion("Descripcion");
        comentario.setFechaCreacion(LocalDateTime.now());
        comentario.setIdUsuario(1L);
        comentario.setIdNoticia(2L);

        comentario2 = new Comentario();
        comentario2.setDescripcion("Descripcion");
        comentario2.setFechaCreacion(LocalDateTime.now());
        comentario2.setIdUsuario(1L);
        comentario2.setIdNoticia(2L);
    }
    @Transactional
    @Rollback
    @Test
    public void guardarComentarioDeberiaPersistirla() throws DescripcionComentarioException {
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
        assertThrows(DescripcionComentarioException.class, () -> {
            repositorioComentario.guardar(new Comentario());
        });
    }

    @Transactional
    @Rollback
    @Test
    public void queAlBuscarComentarioPorIdDeNoticiaDevuelvaUnaListaDeComentarios() throws DescripcionComentarioException {
        repositorioComentario.guardar(comentario);
        repositorioComentario.guardar(comentario2);

        Long idNoticia = comentario.getIdNoticia();
        List<Comentario> comentariosEncontrados = repositorioComentario.buscarComentariosPorIdNoticia(idNoticia);

        assertThat(comentariosEncontrados, is(notNullValue()));
        assertThat(comentariosEncontrados.size(), is(2));
    }
    @Transactional
    @Rollback
    @Test
    public void queAlBuscarComentarioPorIdDeNoticiaYNoEncontraloDevuelvaUnaListaVacia() throws DescripcionComentarioException {
        List<Comentario> comentariosEncontrados = repositorioComentario.buscarComentariosPorIdNoticia(1L);

        assertThat(comentariosEncontrados, is(notNullValue()));
        assertThat(comentariosEncontrados.size(), is(0));
    }
}
