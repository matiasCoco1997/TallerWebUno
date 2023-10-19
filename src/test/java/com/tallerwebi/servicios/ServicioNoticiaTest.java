package com.tallerwebi.servicios;

import com.tallerwebi.dominio.entidades.Noticia;
import com.tallerwebi.dominio.entidades.Notificacion;
import com.tallerwebi.dominio.entidades.Seguidos;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.servicios.ServicioNoticia;
import com.tallerwebi.dominio.servicios.ServicioNoticiaImpl;
import com.tallerwebi.infraestructura.RepositorioCategoria;
import com.tallerwebi.infraestructura.RepositorioNoticia;
import com.tallerwebi.infraestructura.RepositorioNotificacion;
import com.tallerwebi.infraestructura.RepositorioUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;


public class ServicioNoticiaTest {
    private Noticia noticiaMock;
    private ServicioNoticia servicioNoticiaMock;
    private RepositorioNoticia repositorioNoticiaMock;
    private RepositorioUsuario repositorioUsuarioMock;
    private Usuario usuarioMock;
    private Notificacion notificacionMock;

    private Seguidos seguidorMock;
    private MultipartFile imgMock;
    private MockMultipartFile audioMock;
    private RepositorioCategoria repositorioCategoriaMock;
    private RepositorioNotificacion repositorioNotificacionMock;

    @BeforeEach
    public void init() throws IOException {
        seguidorMock=mock(Seguidos.class);
        usuarioMock = mock(Usuario.class);
        notificacionMock=mock(Notificacion.class);
        noticiaMock = mock(Noticia.class);
        when(noticiaMock.getIdNoticia()).thenReturn(1L);
        when(noticiaMock.getTitulo()).thenReturn("titulo");
        when(noticiaMock.getCategoria()).thenReturn("1");
        when(noticiaMock.getResumen()).thenReturn("resumen");
        when(noticiaMock.getRutaDeimagen()).thenReturn("rutaDeImagen");
        when(noticiaMock.getAltDeImagen()).thenReturn("altDeImagen");
        when(noticiaMock.getActiva()).thenReturn(true);

        Path pathImg = Path.of("src/test/resources/mock_image.png");
        byte[] contentImg = Files.readAllBytes(pathImg);
        imgMock = new MockMultipartFile("mock_image.png", "mock_image.png", "image/png", contentImg);

        Path pathAudio = Path.of("src/test/resources/mock_audio.mp3");
        byte[] contentAudio = Files.readAllBytes(pathAudio);
        audioMock = new MockMultipartFile("mock_audio.mp3", "mock_audio.mp3", "audio/mpeg", contentAudio);

        
        this.repositorioNoticiaMock = mock(RepositorioNoticia.class);
        this.repositorioCategoriaMock = mock(RepositorioCategoria.class);
        repositorioUsuarioMock=mock(RepositorioUsuario.class);
        repositorioNotificacionMock=mock(RepositorioNotificacion.class);
        this.servicioNoticiaMock = new ServicioNoticiaImpl(this.repositorioNoticiaMock, repositorioCategoriaMock, repositorioUsuarioMock, repositorioNotificacionMock);
    }


    @Test
    public void cuandoCreoUnaNoticiaSeInvocaLaFuncionGuardarDelRepositorioSoloUnaVez() throws Exception {
        //ejecucion (aca se ejecuta el listarNoticias del repo, interno al servicio)
        servicioNoticiaMock.crearNoticia(noticiaMock, usuarioMock, imgMock, audioMock);

        //verificacion (evaluo si no esta vacio y si es 3 la cantidad de noticias que retorno)
        verify(repositorioNoticiaMock, times(1)).guardar(noticiaMock);
    }

    @Test
    public void cuandoCreoDosNoticiasSeInvocaLaFuncionGuardarDelRepositorioDosVeces() throws Exception {

        //ejecucion (aca se ejecuta el listarNoticias del repo, interno al servicio)
        servicioNoticiaMock.crearNoticia(noticiaMock, usuarioMock, imgMock, audioMock);
        servicioNoticiaMock.crearNoticia(noticiaMock, usuarioMock, imgMock, audioMock);

        //verificacion (evaluo si no esta vacio y si es 2 la cantidad de noticias que retorno)
        verify(repositorioNoticiaMock, times(2)).guardar(noticiaMock);
    }

    @Test
    public void cuandoEditoUnaNoticiaSeInvocaLaFuncionEditarDelRepositorioSoloUnaVez() throws Exception {
        //ejecucion (aca se ejecuta el listarNoticias del repo, interno al servicio)
        servicioNoticiaMock.editarNoticia(noticiaMock, usuarioMock, imgMock, audioMock);

        //verificacion (evaluo si no esta vacio y si es 3 la cantidad de noticias que retorno)
        verify(repositorioNoticiaMock, times(1)).editarNoticia(noticiaMock);
    }

    @Test
    public void cuandoObtengoUnaNoticiaPorSuIdObtengoLaNoticia(){
        //preparacion
        when(repositorioNoticiaMock.buscarPorId(noticiaMock.getIdNoticia())).thenReturn(noticiaMock);

        //ejecucion
        Noticia noticiaObtenida = servicioNoticiaMock.buscarNoticiaPorId(noticiaMock.getIdNoticia());

        //verificacion
        assertThat(noticiaObtenida.getIdNoticia(), is(1L));
    }
/*
    @Test
    public void cuandoDanMeGustaLaCantidadDeMegustaIncrementa() {
        //preparación
        Noticia noticia = new Noticia();
        noticia.setTitulo("Título de la noticia");
        //noticia.setLikes(0);

        //ejecución
        repositorioNoticiaMock.guardar(noticia);
        servicioNoticiaMock.darMeGusta(noticia);

        //validación
        assertThat(noticia.getLikes(), is(1));
    }

    @Test
    public void cuandoDanMeGusta4VecesLaCantidadDeMegustaIncrementa4Veces() {
        //preparación
        Noticia noticia = new Noticia();
        noticia.setTitulo("Título de la noticia");
        //ejecución
        repositorioNoticiaMock.guardar(noticia);
        servicioNoticiaMock.darMeGusta(noticia);
        servicioNoticiaMock.darMeGusta(noticia);
        servicioNoticiaMock.darMeGusta(noticia);
        servicioNoticiaMock.darMeGusta(noticia);
        //validación
        assertThat(noticia.getLikes(), is(4));
    }
*/

        @Test
        public void generarNotificacionDeberiaNotificarSeguidores() {
            when(repositorioUsuarioMock.obtenerListaDeSeguidores(anyLong()))
                    .thenReturn(Arrays.asList(new Seguidos(), new Seguidos()));

            servicioNoticiaMock.generarNotificacion(1L, "UsuarioEjemplo", "Título de Noticia", noticiaMock);

            verify(repositorioNotificacionMock, times(2)).generarNotificacion(any(Notificacion.class));
        }
}