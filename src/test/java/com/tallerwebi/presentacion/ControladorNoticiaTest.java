package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Categoria.Categoria;
import com.tallerwebi.dominio.Categoria.RepositorioCategoria;
import com.tallerwebi.dominio.Noticia.Noticia;
import com.tallerwebi.dominio.Noticia.RepositorioNoticia;
import com.tallerwebi.dominio.Noticia.ServicioNoticia;
import com.tallerwebi.dominio.ServicioLogin;
import com.tallerwebi.dominio.Usuario.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.Basic;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class ControladorNoticiaTest {

    private ControladorNoticia controladorNoticia;
    private Usuario usuarioMock;
    private DatosLogin datosLoginMock;
    private ServicioNoticia servicioNoticiaMock;

    private Noticia noticiaMock;

    private RepositorioCategoria repositorioCategoriaMock;
    private HttpServletRequest requestMock;
    private HttpSession sessionMock;

    @BeforeEach
    public void init(){

       datosLoginMock = new DatosLogin("dami@unlam.com", "123");
        usuarioMock = mock(Usuario.class);
        when(usuarioMock.getNombre()).thenReturn("nombre");
        when(usuarioMock.getApellido()).thenReturn("apellido");
        when(usuarioMock.getPais()).thenReturn("pais");
        when(usuarioMock.getCiudad()).thenReturn("ciudad");
        when(usuarioMock.getFechaDeNacimiento()).thenReturn("01/01/2000");
        when(usuarioMock.getEmail()).thenReturn("dami@unlam.com");
        when(usuarioMock.getPassword()).thenReturn("password");
        when(usuarioMock.getFotoPerfil()).thenReturn("/fotoPerfil");
        when(usuarioMock.getRol()).thenReturn("Lector");

        noticiaMock = mock(Noticia.class);
        when(noticiaMock.getTitulo()).thenReturn("titulo");
        when(noticiaMock.getCategoria()).thenReturn("categoria");
        when(noticiaMock.getIdUsuario()).thenReturn(1L);
        when(noticiaMock.getDescripcion()).thenReturn("descripcion");
        when(noticiaMock.getNoticia()).thenReturn("noticia");
        when(noticiaMock.getImagen()).thenReturn("imagen");
        when(noticiaMock.getFechaPublicacion()).thenReturn("fecha");
        when(noticiaMock.getAudioPodcastRuta()).thenReturn("audio");
        when(noticiaMock.getActiva()).thenReturn(true);

        requestMock = mock(HttpServletRequest.class);
        sessionMock = mock(HttpSession.class);

        servicioNoticiaMock = mock(ServicioNoticia.class);

        repositorioCategoriaMock = mock(RepositorioCategoria.class);

        controladorNoticia = new ControladorNoticia(servicioNoticiaMock, repositorioCategoriaMock);

    }


    @Test
    public void queElLectorNoPuedaSubirUnaNoticia(){
        // preparacion
        //Usuario usuarioEncontradoMock = mock(Usuario.class);
        //when(usuarioEncontradoMock.getRol()).thenReturn("Lector");

        when(requestMock.getSession()).thenReturn(sessionMock);

        when(servicioNoticiaMock.crearNoticia(any())).thenReturn(Usuario usuario);

        String resultado = controladorNoticia.crearNuevaNoticia(new Noticia(), sessionMock);

        // ejecucion


        // validacion
        assertThat(resultado, equalToIgnoringCase("Editor"));
    }
}
