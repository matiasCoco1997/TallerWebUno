package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidades.MeGusta;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = { SpringWebTestConfig.class, HibernateTestConfig.class })
public class RepositorioLikeTest {
    @Autowired
    private RepositorioLike repositorioLike;
    @Autowired
    private RepositorioUsuario repositorioUsuario;
    @Autowired
    private RepositorioNoticia repositorioNoticia;
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
        noticia2.setCategoria("Economía");
        noticia2.setTitulo("Titulo Economía");

        noticia3 = new Noticia();
        noticia3.setCategoria("Economía");
        noticia3.setTitulo("Titulo Economía");

        noticia4 = new Noticia();
        noticia4.setCategoria("Economía");
        noticia4.setTitulo("Titulo Economía");

        noticia5 = new Noticia();
        noticia5.setCategoria("Economía");
        noticia5.setTitulo("Titulo Economía");

        noticia6 = new Noticia();
        noticia6.setCategoria("Economía");
        noticia6.setTitulo("Titulo Economía");

        usuario1 = new Usuario();
    }
    @Transactional
    @Rollback
    @Test
    public void queSepuedaObtenerUnaListaCategoriasDeNoticiasConMeGustaDeUnUsuario(){
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

        List<String> Categorias = repositorioLike.traerCategoriasLikeadasPorUnUsuario(usuario1.getIdUsuario());

        assertEquals("Deporte", Categorias.get(0));
        assertEquals("Economía", Categorias.get(1));
    }


}