package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidades.ListaReproduccion;
import com.tallerwebi.dominio.entidades.Noticia;
import com.tallerwebi.dominio.entidades.Usuario;
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

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = { SpringWebTestConfig.class, HibernateTestConfig.class })
public class RepositorioListaRepTest {

    @Autowired
    private RepositorioListaRep repositorioListaRep;
    @Autowired
    private RepositorioNoticia repositorioNoticia;
    @Autowired
    private RepositorioUsuario repositorioUsuario;

    private ListaReproduccion lista;
    private ListaReproduccion lista2;

    @BeforeEach
    public void init(){
        lista=new ListaReproduccion();
        lista2=new ListaReproduccion();
        lista.setNoticia(new Noticia());
        lista.getNoticia().setTitulo("titulo");
        lista.setUsuario(new Usuario());
        lista2.setNoticia(new Noticia());
        lista2.getNoticia().setTitulo("titulo");
        lista2.setUsuario(new Usuario());
        repositorioNoticia.guardar(lista.getNoticia());
        repositorioNoticia.guardar(lista2.getNoticia());
        repositorioUsuario.guardar(lista.getUsuario());
        repositorioUsuario.guardar(lista2.getUsuario());

    }

    @Transactional
    @Rollback
    @Test
    public void queSePuedaGuardarUnaListaDeReproduccion(){
        repositorioListaRep.agregarNoticiaALista(lista);
        ListaReproduccion listaObtenida= repositorioListaRep.buscarListaPorID(lista.getId());
        assertThat(listaObtenida, is(notNullValue()));
    }

    @Transactional
    @Rollback
    @Test
    public void queSePuedaObtenerLaListaDeUnUsuario(){
        repositorioListaRep.agregarNoticiaALista(lista);
        repositorioListaRep.agregarNoticiaALista(lista2);
        List<ListaReproduccion> listaObtenida= repositorioListaRep.obtenerListaReproduccionDelUsuarioLogueado(lista.getUsuario().getIdUsuario());
        assertThat(listaObtenida.size(), is(1));
    }

    @Transactional
    @Rollback
    @Test
    public void queSePuedaEliminarUnaNoticiaLista(){
        repositorioListaRep.agregarNoticiaALista(lista);
        repositorioListaRep.eliminarNoticiaDeLista(lista);
        List<ListaReproduccion> listaObtenida= repositorioListaRep.obtenerListaReproduccionDelUsuarioLogueado(lista.getUsuario().getIdUsuario());
        assertThat(listaObtenida.size(), is(0));
    }
}
