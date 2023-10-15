package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidades.Noticia;
import com.tallerwebi.dominio.entidades.Notificacion;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = { SpringWebTestConfig.class, HibernateTestConfig.class })
public class RepositorioUsuarioTest {

    @Autowired
    private RepositorioNoticia repositorioNoticia;
    @Autowired
    private RepositorioUsuario repositorioUsuario;
    @Autowired
    private RepositorioNotificacion repositorioNotificacion;
    private Noticia noticia;
    private Noticia noticia2;
    private Usuario usuario;
    private Usuario usuario2;

    @BeforeEach
    public void init(){
        usuario=new Usuario();
        usuario.setIdUsuario(1L);
        usuario2=new Usuario();
        usuario2.setIdUsuario(2L);

        noticia=new Noticia();
        noticia.setTitulo("titulo");
        noticia.setUsuario(usuario);
        noticia.setActiva(true);

        noticia2=new Noticia();
        noticia2.setTitulo("titulo");
        noticia2.setUsuario(usuario2);
        noticia.setActiva(true);
    }

    @Test
    @Transactional
    @Rollback
    public void quePuedaFiltrarNoticiasPorElIDDelUsuario(){
        repositorioUsuario.guardar(usuario);
        repositorioUsuario.guardar(usuario2);
        repositorioNoticia.guardar(noticia);
        repositorioNoticia.guardar(noticia2);
        List<Noticia> noticiasObtenidas=repositorioUsuario.obtenerMisNoticias(usuario.getIdUsuario());
        assertThat(noticiasObtenidas.size(),is(1));
    }

    @Test
    @Transactional
    @Rollback
    public void quePuedaObtenerUnUsuarioPorSuID(){
        repositorioUsuario.guardar(usuario);
        Usuario usuarioObtenido=repositorioUsuario.obtenerUsuarioPorId(usuario.getIdUsuario());
        assertThat(usuarioObtenido,is(notNullValue()));
    }

    @Test
    @Transactional
    @Rollback
    public void quePuedaObtenerMisNotificacionesSinLeer(){
        repositorioUsuario.guardar(usuario);
        Notificacion notificacion=new Notificacion();
        notificacion.setVista(true);
        notificacion.setUsuarioNotificado(usuario);
        Notificacion notificacion2=new Notificacion();
        notificacion.setUsuarioNotificado(usuario);
        repositorioNotificacion.generarNotificacion(notificacion);
        repositorioNotificacion.generarNotificacion(notificacion2);
        List<Notificacion> notificaciones=repositorioUsuario.obtenerMisNotificacionesSinLeer(usuario.getIdUsuario());
        assertThat(notificaciones.size(),is(1));
    }

    @Test
    @Transactional
    @Rollback
    public void quePuedaObtenerMisNotificaciones(){
        repositorioUsuario.guardar(usuario);
        repositorioUsuario.guardar(usuario2);
        Notificacion notificacion=new Notificacion();
        notificacion.setUsuarioNotificado(usuario);
        Notificacion notificacion2=new Notificacion();
        notificacion2.setUsuarioNotificado(usuario2);
        repositorioNotificacion.generarNotificacion(notificacion);
        repositorioNotificacion.generarNotificacion(notificacion2);
        List<Notificacion> notificaciones=repositorioUsuario.obtenerMisNotificaciones(usuario.getIdUsuario());
        assertThat(notificaciones.size(),is(1));
    }

    @Test
    @Transactional
    @Rollback
    public void queLasNotificacionesSePuedanMarcarComoLeidas(){
        repositorioUsuario.guardar(usuario);
        Notificacion notificacion=new Notificacion();
        notificacion.setUsuarioNotificado(usuario);
        Notificacion notificacion2=new Notificacion();
        notificacion2.setUsuarioNotificado(usuario);
        repositorioNotificacion.generarNotificacion(notificacion);
        repositorioNotificacion.generarNotificacion(notificacion2);

        repositorioUsuario.marcarNotificacionesComoLeidas(usuario.getIdUsuario());

        List<Notificacion> notificaciones=repositorioUsuario.obtenerMisNotificacionesSinLeer(usuario.getIdUsuario());

        assertThat(notificaciones.size(),is(0));
    }
}
