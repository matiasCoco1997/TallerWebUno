package com.tallerwebi.servicios;

import com.tallerwebi.dominio.entidades.*;
import com.tallerwebi.dominio.servicios.ServicioNoticia;
import com.tallerwebi.dominio.servicios.ServicioNoticiaImpl;
import com.tallerwebi.infraestructura.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.Is.is;
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
    private RepositorioLike repositorioLikeMock;
    private Republicacion republicacionMock;
    private List<Noticia> noticiasEsperadas;

    @BeforeEach
    public void init() throws IOException {

        seguidorMock = mock(Seguidos.class);
        usuarioMock = mock(Usuario.class);
        notificacionMock = mock(Notificacion.class);
        noticiaMock = mock(Noticia.class);
        republicacionMock = mock(Republicacion.class);
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
        repositorioUsuarioMock = mock(RepositorioUsuario.class);
        repositorioNotificacionMock = mock(RepositorioNotificacion.class);
        repositorioLikeMock = mock(RepositorioLike.class);

        this.servicioNoticiaMock = new ServicioNoticiaImpl(this.repositorioNoticiaMock, repositorioCategoriaMock, repositorioUsuarioMock, repositorioNotificacionMock, repositorioLikeMock);

        noticiasEsperadas = new ArrayList<>();
    }

    @Test
    public void cuandoCreoUnaNoticiaSeInvocaLaFuncionGuardarDelRepositorioSoloUnaVez() throws Exception {
        servicioNoticiaMock.crearNoticia(noticiaMock, usuarioMock, imgMock, audioMock);

        verify(repositorioNoticiaMock, times(1)).guardar(noticiaMock);
    }


    @Test
    public void cuandoCreoUnaNoticiaAnonimaSeInvocaLaFuncionGuardarDelRepositorioSoloUnaVez() throws Exception {
        when(noticiaMock.getEsAnonima()).thenReturn(true);

        Noticia noticia = noticiaMock;

        servicioNoticiaMock.crearNoticia(noticia, usuarioMock, imgMock, audioMock);

        assertThat(noticia.getEsAnonima(), is(true));
        verify(repositorioNoticiaMock, times(1)).guardar(noticia);
    }

    @Test
    public void cuandoCreoDosNoticiasSeInvocaLaFuncionGuardarDelRepositorioDosVeces() throws Exception {

        servicioNoticiaMock.crearNoticia(noticiaMock, usuarioMock, imgMock, audioMock);
        servicioNoticiaMock.crearNoticia(noticiaMock, usuarioMock, imgMock, audioMock);

        verify(repositorioNoticiaMock, times(2)).guardar(noticiaMock);
    }

    @Test
    public void cuandoEditoUnaNoticiaSeInvocaLaFuncionEditarDelRepositorioSoloUnaVez() throws Exception {
        servicioNoticiaMock.editarNoticia(noticiaMock, usuarioMock, imgMock, audioMock);

        verify(repositorioNoticiaMock, times(1)).editarNoticia(noticiaMock);
    }

    @Test
    public void cuandoObtengoUnaNoticiaPorSuIdObtengoLaNoticia() {
        //preparacion
        when(repositorioNoticiaMock.buscarPorId(noticiaMock.getIdNoticia())).thenReturn(noticiaMock);

        //ejecucion
        Noticia noticiaObtenida = servicioNoticiaMock.buscarNoticiaPorId(noticiaMock.getIdNoticia());

        //verificacion
        assertThat(noticiaObtenida.getIdNoticia(), is(1L));
    }

    @Test
    public void queSeGuardeLaRepublicacion(){
        servicioNoticiaMock.republicarNoticia(republicacionMock);
        verify(repositorioNoticiaMock,times(1)).republicarNoticia(eq(republicacionMock));
    }

    @Test
    public void queSePuedaObtenerUnaCantidadDeNoticiasPorCategoriasQueElUsuarioDioLikeUtilizeRepositorioNoticia(){
        noticiasEsperadas.add(noticiaMock);
        List<String> categorias = new ArrayList<>();
        when(repositorioLikeMock.traerCategoriasLikeadasPorUnUsuario(anyLong())).thenReturn(categorias);
        when(repositorioNoticiaMock.obtenerNoticiasCategoria(anyInt(), anyList())).thenReturn(noticiasEsperadas);

        List<Noticia> noticiaObtenida = servicioNoticiaMock.obtenerNoticiasCategoria(1L,1);


        verify(repositorioNoticiaMock, times(1)).obtenerNoticiasCategoria(anyInt(), anyList());
        verify(repositorioLikeMock, times(1)).traerCategoriasLikeadasPorUnUsuario(anyLong());
    }

    @Test
    public void queSePuedaCompartirUnaNoticia(){
        servicioNoticiaMock.compartirNoticia(notificacionMock);
        verify(repositorioNotificacionMock,times(1)).generarNotificacion(notificacionMock);
    }
}

