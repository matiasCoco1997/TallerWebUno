package com.tallerwebi.servicios;


import com.tallerwebi.dominio.entidades.*;

import com.tallerwebi.dominio.entidades.Categoria;
import com.tallerwebi.dominio.entidades.Noticia;
import com.tallerwebi.dominio.entidades.Notificacion;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.excepcion.ComentarioException;
import com.tallerwebi.dominio.excepcion.FormatoDeImagenIncorrecto;
import com.tallerwebi.dominio.excepcion.RelacionNoEncontradaException;
import com.tallerwebi.dominio.excepcion.TamanioDeArchivoSuperiorALoPermitido;
import com.tallerwebi.dominio.servicios.ServicioHome;
import com.tallerwebi.dominio.servicios.ServicioHomeImpl;

import com.tallerwebi.dominio.servicios.ServicioUsuario;
import com.tallerwebi.dominio.servicios.ServicioUsuarioImpl;
import com.tallerwebi.infraestructura.RepositorioCategoria;
import com.tallerwebi.infraestructura.RepositorioUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ServicioUsuarioTest {

    private Noticia noticiaMock;
    private Noticia noticiaMockDeportes;
    private Noticia noticiaMockArte;
    private ServicioUsuario servicioUsuarioMock;
    private RepositorioUsuario repositorioUsuarioMock;
    private RepositorioCategoria repositorioCategoriaMock;
    private Usuario usuarioMock;
    private Categoria categoriaMock;
    private Seguidos seguidosMock;
    private Usuario seguidorMock;
    private Usuario seguidoMock;
    private Notificacion notificacionMock;
    private MultipartFile imgMock;


    @BeforeEach
    public void init() throws IOException {
        noticiaMock = mock(Noticia.class);
        noticiaMockArte = mock(Noticia.class);
        noticiaMockDeportes = mock(Noticia.class);
        imgMock = mock(MultipartFile.class);
        when(noticiaMock.getCategoria()).thenReturn("categoria");
        when(noticiaMock.getTitulo()).thenReturn("categoria");
        when(noticiaMockArte.getCategoria()).thenReturn("arte");
        when(noticiaMockArte.getTitulo()).thenReturn("depor");
        when(noticiaMockDeportes.getCategoria()).thenReturn("deportes");
        when(noticiaMockDeportes.getTitulo()).thenReturn("deportes");
        usuarioMock=mock(Usuario.class);
        when(usuarioMock.getIdUsuario()).thenReturn(1L);

        seguidoMock=mock(Usuario.class);
        when(seguidoMock.getIdUsuario()).thenReturn(1L);
        seguidorMock=mock(Usuario.class);
        when(seguidorMock.getIdUsuario()).thenReturn(2L);
        seguidosMock = mock(Seguidos.class);
        when(seguidosMock.getIdUsuarioPropio()).thenReturn(seguidoMock);
        when(seguidosMock.getIdUsuarioSeguidor()).thenReturn(seguidorMock);

        notificacionMock=mock(Notificacion.class);

        Path pathImg = Path.of("src/test/resources/mock_image.png");
        byte[] contentImg = Files.readAllBytes(pathImg);
        imgMock = new MockMultipartFile("mock_image.png", "mock_image.png", "image/png", contentImg);

        categoriaMock=mock(Categoria.class);
        this.repositorioUsuarioMock = mock(RepositorioUsuario.class);
        this.repositorioCategoriaMock = mock(RepositorioCategoria.class);
        this.servicioUsuarioMock = new ServicioUsuarioImpl(this.repositorioUsuarioMock,repositorioCategoriaMock);
    }

    @Test
    public void queSePuedanObtenerLasNoticiasDeUnUsuarioEnEspecifico() {
        List<Noticia> noticias = new ArrayList<>();
        noticias.add(noticiaMock);
        noticias.add(noticiaMockArte);
        noticias.add(noticiaMockDeportes);
        when(repositorioUsuarioMock.obtenerMisNoticias(usuarioMock.getIdUsuario())).thenReturn(noticias);
        List<Noticia> noticiasObtenidas = servicioUsuarioMock.obtenerNoticiasDeUnUsuario(usuarioMock.getIdUsuario());
        assertThat(noticiasObtenidas.size(), is(3));
    }

    @Test
    public void queSePuedanObtenerLosBorradoresDeUnUsuarioEnEspecifico() {
        List<Noticia> noticias = new ArrayList<>();
        when(noticiaMock.getActiva()).thenReturn(false);
        noticias.add(noticiaMock);
        noticias.add(noticiaMock);
        when(repositorioUsuarioMock.obtenerMisNoticiasEnEstadoBorrador(usuarioMock.getIdUsuario())).thenReturn(noticias);

        List<Noticia> noticiasObtenidas = servicioUsuarioMock.obtenerNoticiasDeUnUsuarioEnEstadoBorrador(usuarioMock.getIdUsuario());
        assertThat(noticiasObtenidas.size(), is(2));
    }

    @Test
    public void queSePuedaObtenerUnUsuarioPorSuID() throws Exception {
        when(repositorioUsuarioMock.obtenerUsuarioPorId(usuarioMock.getIdUsuario())).thenReturn(usuarioMock);
        Usuario usuarioObtenido = servicioUsuarioMock.obtenerUsuarioPorId(usuarioMock.getIdUsuario());
        assertThat(usuarioObtenido, is(notNullValue()));
    }

    @Test
    public void testObtenerUsuarioPorIdLanzaExcepcion() {
        assertThrows(Exception.class, () -> {
            servicioUsuarioMock.obtenerUsuarioPorId(usuarioMock.getIdUsuario());
        });
    }

    @Test
    public void crearUnUsuarioDebeUlizarRepositorioUsuarioParaGuardarSeguidos(){
        servicioUsuarioMock.agregarSeguido(usuarioMock, usuarioMock);
        verify(repositorioUsuarioMock, times(1)).crearSeguidos(any(Seguidos.class));
    }
    @Test
    public void queNoSePuedaGuardarUnQueSeQuieraSeguirSiYaLoSigue(){
        List<Seguidos> listaSeguidos = new ArrayList<>();
        listaSeguidos.add(seguidosMock);

        when(repositorioUsuarioMock.obtenerListaDeSeguidos(anyLong())).thenReturn(listaSeguidos);

        servicioUsuarioMock.agregarSeguido(seguidorMock, seguidoMock);

        verify(repositorioUsuarioMock, times(1)).obtenerListaDeSeguidos(anyLong());
        verify(repositorioUsuarioMock, times(0)).crearSeguidos(any(Seguidos.class));

    }
    @Test
    public void quePuedaObtenerMisNotificaciones(){
        List<Notificacion> notificaciones=new ArrayList<>();
        notificaciones.add(notificacionMock);
        notificaciones.add(notificacionMock);
        when(repositorioUsuarioMock.obtenerMisNotificaciones(usuarioMock.getIdUsuario())).thenReturn(notificaciones);

        List<Notificacion> notificacionesObtenidas= servicioUsuarioMock.obtenerMisNotificaciones(usuarioMock.getIdUsuario());
        assertThat(notificacionesObtenidas.size(),is(2));
    }

    @Test
    public void queLasNotificacionesSeMarquenComoLeidas(){
        servicioUsuarioMock.marcarNotificacionesComoLeidas(usuarioMock.getIdUsuario());
        verify(repositorioUsuarioMock,times(1)).marcarNotificacionesComoLeidas(usuarioMock.getIdUsuario());
    }

    @Test
    public void queUnaRelacionEntreSeguidoYSeguidorSePuedaEliminar() throws RelacionNoEncontradaException {
        servicioUsuarioMock.dejarDeSeguirUsuario(1L, 2L);
        verify(repositorioUsuarioMock, times(1)).dejarDeSeguir(anyLong(), anyLong());
    }

    @Test
    public void queUnaRelacionEntreSeguidoYSeguidorSePuedaEliminarLanzaExcepcionPorNoExistirLaRelacion() throws RelacionNoEncontradaException {
        doThrow(RelacionNoEncontradaException.class).when(repositorioUsuarioMock).dejarDeSeguir(anyLong(), anyLong());
        try {
            servicioUsuarioMock.dejarDeSeguirUsuario(1L, 2L);
            fail("No lanzó RelacionNoEncontradaException");
        } catch (RelacionNoEncontradaException e) {
            assertEquals("RelacionNoEncontradaException", e.getClass().getSimpleName());
        }
    }

    @Test
    public void queSePuedaModificarCorrectamenteDatosDeUnUsuario() throws TamanioDeArchivoSuperiorALoPermitido, FormatoDeImagenIncorrecto, IOException {
        Usuario u = new Usuario();
        u.setIdUsuario(1L);
        u.setCiudad("Haedo");
        repositorioUsuarioMock.guardar(u);
        u.setCiudad("Moron");
        servicioUsuarioMock.modificarDatosUsuario(u, imgMock);

        assertThat(u.getCiudad(), is("Moron"));
    }

    @Test
    public void queSePuedaListarUsuariosParaSeguir() throws Exception {
        servicioUsuarioMock.listarUsuarioParaSeguir(anyLong());
        verify(repositorioUsuarioMock, times(1)).listarUsuariosRecomendadosSinSeguir(anyLong());
    }

    @Test
    public void queSePuedaListarUsuariosParaSeguirDevuelveUnaListaDeUsuarios() throws Exception {
        when(repositorioUsuarioMock.listarUsuariosRecomendadosSinSeguir(anyLong())).thenReturn(new ArrayList<>());
        List<Usuario> usuariosRecomendados = servicioUsuarioMock.listarUsuarioParaSeguir(anyLong());
        assertEquals("ArrayList", usuariosRecomendados.getClass().getSimpleName());
    }

    @Test
    public void queSePuedaListarUsuariosParaSeguirLanzaExcepcion() {
        List<Usuario> usuariosRecomendados = servicioUsuarioMock.listarUsuarioParaSeguir(anyLong());

        assertEquals(0, usuariosRecomendados.size());
    }


}
