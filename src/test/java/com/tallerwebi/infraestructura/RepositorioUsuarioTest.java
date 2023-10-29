package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidades.Noticia;
import com.tallerwebi.dominio.entidades.Seguidos;
import com.tallerwebi.dominio.entidades.Notificacion;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.excepcion.RelacionNoEncontradaException;
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

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
    private Seguidos seguidos;
    private Usuario noSeguido;
    private Usuario seguido;
    private Usuario seguido2;
    private Usuario seguidor;

    @BeforeEach
    public void init() {
        usuario = new Usuario();
        usuario2 = new Usuario();

        noticia = new Noticia();
        noticia.setTitulo("titulo");
        noticia.setUsuario(usuario);
        noticia.setActiva(true);

        noticia2=new Noticia();
        noticia2.setTitulo("titulo");
        noticia2.setUsuario(usuario2);
        noticia.setActiva(true);

        seguidos=new Seguidos();
        seguidos.setIdUsuarioSeguidor(usuario);
        seguidos.setIdUsuarioPropio(usuario2);

        seguido = new Usuario();
        seguido.setNombre("seguido");

        seguidor = new Usuario();
        seguidor.setNombre("seguidor");

        seguido2 = new Usuario();
        seguido2.setNombre("seguido2");

        noSeguido = new Usuario();
        noSeguido.setNombre("NoSeguido");
    }

    @Test
    @Transactional
    @Rollback
    public void quePuedaFiltrarNoticiasPorElIDDelUsuario() {
        repositorioUsuario.guardar(usuario);
        repositorioUsuario.guardar(usuario2);
        repositorioNoticia.guardar(noticia);
        repositorioNoticia.guardar(noticia2);
        List<Noticia> noticiasObtenidas = repositorioUsuario.obtenerMisNoticias(usuario.getIdUsuario());
        assertThat(noticiasObtenidas.size(), is(1));
    }

    @Test
    @Transactional
    @Rollback
    public void quePuedaTraerLasNoticiasEnEstadoBorradorPorElIdDelUsuario() {

        repositorioUsuario.guardar(usuario);
        repositorioUsuario.guardar(usuario2);

        noticia.setActiva(false);
        noticia2.setActiva(false);

        repositorioNoticia.guardar(noticia);
        repositorioNoticia.guardar(noticia2);

        List<Noticia> noticiasObtenidas = repositorioUsuario.obtenerMisNoticiasEnEstadoBorrador(usuario.getIdUsuario());
        assertThat(noticiasObtenidas.size(), is(1));
    }

    @Test
    @Transactional
    @Rollback
    public void quePuedaObtenerUnUsuarioPorSuID() {
        repositorioUsuario.guardar(usuario);
        Usuario usuarioObtenido = repositorioUsuario.obtenerUsuarioPorId(usuario.getIdUsuario());
        assertThat(usuarioObtenido, is(notNullValue()));
    }

    @Test
    @Transactional
    @Rollback
    public void quePuedaGuardarSeguidos() {
        repositorioUsuario.guardar(seguido);
        repositorioUsuario.guardar(seguido2);
        repositorioUsuario.guardar(seguidor);

        Seguidos seguidos = new Seguidos();
        seguidos.setIdUsuarioSeguidor(seguidor);
        seguidos.setIdUsuarioPropio(seguido);

        Seguidos seguidos2 = new Seguidos();
        seguidos2.setIdUsuarioSeguidor(seguidor);
        seguidos2.setIdUsuarioPropio(seguido2);

        repositorioUsuario.crearSeguidos(seguidos);
        repositorioUsuario.crearSeguidos(seguidos2);

        List<Seguidos> seguidosList = repositorioUsuario.obtenerListaDeSeguidos(seguidor.getIdUsuario());

        assertNotNull(seguidosList);
        assertEquals(2, seguidosList.size());

        assertEquals(seguido, seguidosList.get(0).getIdUsuarioPropio());
        assertEquals(seguido2, seguidosList.get(1).getIdUsuarioPropio());
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

    @Test
    @Transactional
    @Rollback
    public void queSePuedaDejarDeSeguirUnUsuario() throws RelacionNoEncontradaException {
        repositorioUsuario.guardar(seguido);
        repositorioUsuario.guardar(seguido2);
        repositorioUsuario.guardar(seguidor);

        Seguidos seguidos = new Seguidos();
        seguidos.setIdUsuarioSeguidor(seguidor);
        seguidos.setIdUsuarioPropio(seguido);

        Seguidos seguidos2 = new Seguidos();
        seguidos2.setIdUsuarioSeguidor(seguidor);
        seguidos2.setIdUsuarioPropio(seguido2);

        repositorioUsuario.crearSeguidos(seguidos);
        repositorioUsuario.crearSeguidos(seguidos2);


        repositorioUsuario.dejarDeSeguir(seguido.getIdUsuario(), seguidor.getIdUsuario());
        List<Seguidos> seguidosList = repositorioUsuario.obtenerListaDeSeguidos(seguidor.getIdUsuario());

        assertNotNull(seguidosList);
        assertEquals(1, seguidosList.size());
    }
    @Test
    @Transactional
    @Rollback
    public void queLanceExcepcionAlDejarDeSeguirUnUsuarioDeFormaIncorrecta() throws RelacionNoEncontradaException {
        repositorioUsuario.guardar(seguido);
        repositorioUsuario.guardar(seguidor);

        Seguidos seguidos = new Seguidos();
        seguidos.setIdUsuarioSeguidor(seguidor);
        seguidos.setIdUsuarioPropio(seguido);

        repositorioUsuario.crearSeguidos(seguidos);

        try {
            repositorioUsuario.dejarDeSeguir(7L, seguidor.getIdUsuario());
        } catch (RuntimeException e) {
            assertEquals("RuntimeException", e.getClass().getSimpleName());
        }
    }

    @Transactional
    @Rollback
    @Test
    public void cuandoEliminoUnUsuarioDejaDeExistirEnLaBaseDeDatos() {
        //preparación
        String correo = "unmainunicoeirremplazable@wlove.ar";
        this.usuario.setEmail(correo);
        repositorioUsuario.guardar(usuario);
        //ejecución
        repositorioUsuario.borrarUsuario(usuario.getIdUsuario());
        //verificación
        assertThat(repositorioUsuario.buscar(correo), nullValue());
    }

    @Transactional
    @Rollback
    @Test
    public void queSePuedaListarUsuarioParaSeguirYQueNoLosEsteSiguiendo() {
        repositorioUsuario.guardar(seguido);
        repositorioUsuario.guardar(noSeguido);
        repositorioUsuario.guardar(seguidor);

        Seguidos seguidos = new Seguidos();
        seguidos.setIdUsuarioSeguidor(seguidor);
        seguidos.setIdUsuarioPropio(seguido);
        repositorioUsuario.crearSeguidos(seguidos);

        List<Usuario> usuariosRecomendados = null;
        try {
            usuariosRecomendados = repositorioUsuario.listarUsuariosRecomendadosSinSeguir(seguidor.getIdUsuario());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertEquals(noSeguido.getIdUsuario(), usuariosRecomendados.get(0).getIdUsuario());
        assertEquals(1, usuariosRecomendados.size());
    }

    @Transactional
    @Rollback
    @Test
    public void queRetorneUnaListaVaciaSiNoEncuentraUsuariosParaSeguir() {
        repositorioUsuario.guardar(seguido);
        repositorioUsuario.guardar(seguido2);
        repositorioUsuario.guardar(seguidor);

        Seguidos seguidos = new Seguidos();
        seguidos.setIdUsuarioSeguidor(seguidor);
        seguidos.setIdUsuarioPropio(seguido);

        Seguidos seguidos2 = new Seguidos();
        seguidos2.setIdUsuarioSeguidor(seguidor);
        seguidos2.setIdUsuarioPropio(seguido2);

        repositorioUsuario.crearSeguidos(seguidos);
        repositorioUsuario.crearSeguidos(seguidos2);

        repositorioUsuario.guardar(seguidor);

        List<Usuario> usuariosRecomendados = repositorioUsuario.listarUsuariosRecomendadosSinSeguir(seguidor.getIdUsuario());

        assertEquals(0, usuariosRecomendados.size());

    }

    @Transactional
    @Rollback
    @Test
    public void queSePuedaOntenerNoticiaDeUsuariosQueSigo(){
        repositorioUsuario.guardar(seguido);
        repositorioUsuario.guardar(seguido2);
        repositorioUsuario.guardar(seguidor);

        Seguidos seguidos = new Seguidos();
        seguidos.setIdUsuarioSeguidor(seguidor);
        seguidos.setIdUsuarioPropio(seguido);

        Seguidos seguidos2 = new Seguidos();
        seguidos2.setIdUsuarioSeguidor(seguidor);
        seguidos2.setIdUsuarioPropio(seguido2);

        repositorioUsuario.crearSeguidos(seguidos);
        repositorioUsuario.crearSeguidos(seguidos2);

        noticia.setUsuario(seguido);
        noticia2.setUsuario(seguido2);
        repositorioNoticia.guardar(noticia);
        repositorioNoticia.guardar(noticia2);

        List<Noticia> noticiasObtenidas = repositorioUsuario.obtenerNoticiaDeSeguidos(seguidor.getIdUsuario());

        assertNotNull(noticiasObtenidas);
        assertEquals(2, noticiasObtenidas.size());
    }
    @Transactional
    @Rollback
    @Test
    public void queSePuedaOntenerLosUsuarioSeguidos(){
        repositorioUsuario.guardar(seguido);
        repositorioUsuario.guardar(seguido2);
        repositorioUsuario.guardar(seguidor);

        Seguidos seguidos = new Seguidos();
        seguidos.setIdUsuarioSeguidor(seguidor);
        seguidos.setIdUsuarioPropio(seguido);

        Seguidos seguidos2 = new Seguidos();
        seguidos2.setIdUsuarioSeguidor(seguidor);
        seguidos2.setIdUsuarioPropio(seguido2);

        repositorioUsuario.crearSeguidos(seguidos);
        repositorioUsuario.crearSeguidos(seguidos2);

        List<Usuario> usuariosSeguidos = repositorioUsuario.listarUsuariosSeguidos(seguidor.getIdUsuario());

        assertNotNull(usuariosSeguidos);
        assertEquals(2, usuariosSeguidos.size());
    }
    @Transactional
    @Rollback
    @Test
    public void queSePuedaOntenerLosUsuarioQueMeSiguen(){
        repositorioUsuario.guardar(seguido);
        repositorioUsuario.guardar(seguido2);
        repositorioUsuario.guardar(seguidor);

        Seguidos seguidos = new Seguidos();
        seguidos.setIdUsuarioSeguidor(seguidor);
        seguidos.setIdUsuarioPropio(seguido);

        Seguidos seguidos2 = new Seguidos();
        seguidos2.setIdUsuarioSeguidor(seguidor);
        seguidos2.setIdUsuarioPropio(seguido2);

        repositorioUsuario.crearSeguidos(seguidos);
        repositorioUsuario.crearSeguidos(seguidos2);

        List<Usuario> usuariosSeguidores = repositorioUsuario.listarUsuariosQueMeSiguen(seguido.getIdUsuario());

        assertNotNull(usuariosSeguidores);
        assertEquals(1, usuariosSeguidores.size());
    }
}
