package com.tallerwebi.infraestructura;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasSize;

import java.util.List;

import com.tallerwebi.dominio.Noticia.Noticia;
import com.tallerwebi.dominio.Noticia.RepositorioNoticia;
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
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = { SpringWebTestConfig.class, HibernateTestConfig.class })
public class RepositorioNoticiaTest {
    @Autowired
    private RepositorioNoticia repositorioNoticia;

    @Transactional
    @Rollback
    @Test
    public void guardarEscuelaDeberiaPersistirla() {

        Noticia noticia = new Noticia();

        repositorioNoticia.guardar(noticia);

        Noticia buscada = repositorioNoticia.buscarPorId(1L);

        assertThat(buscada, is(notNullValue()));

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
        repositorioNoticia.guardar(noticia);
        noticia.setTitulo("Titulo de la noticia");

        repositorioNoticia.borrarNoticia(noticia);

        List<Noticia> buscadas = repositorioNoticia.buscarPorTitulo("Titulo de la noticia");

        assertThat(buscadas,hasSize(0));

    }

   /* @Transactional
    @Rollback
    @Test
    public void queSePuedaModificarUnaNoticia() {

        Noticia noticia = new Noticia();
        repositorioNoticia.guardar(noticia);
        noticia.setTitulo("Titulo de la noticia");



    }*/
}
