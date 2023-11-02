package com.tallerwebi.dominio.servicios;

import com.tallerwebi.dominio.entidades.*;
import com.tallerwebi.dominio.excepcion.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ServicioNoticia {
    void crearNoticia(Noticia noticia, Usuario usuarioLogueado, MultipartFile imagen, MultipartFile audio) throws CampoVacio, TamanioDeArchivoSuperiorALoPermitido, IOException, FormatoDeImagenIncorrecto, FormatoDeAudioIncorrecto;

    void borrarNoticiaPorId(Noticia noticia) throws IOException;

    Noticia buscarNoticiaPorId(Long idNoticia);

    List<Noticia> buscarNoticiaPorTitulo(String tituloNoticia);

    void editarNoticia(Noticia noticia, Usuario usuarioLogueado, MultipartFile imagen, MultipartFile audio) throws CampoVacio, TamanioDeArchivoSuperiorALoPermitido, IOException, FormatoDeImagenIncorrecto, FormatoDeAudioIncorrecto;

    List<Noticia> listarNoticias();

    List<Noticia> buscarNoticiaPorCategoria(String categoria);

    Boolean darMeGusta(Noticia noticia, Usuario usuarioLogueado) throws NoticiaInexistente, UsuarioDeslogueado;

    List<Categoria> listarCategorias();

    void generarNotificacion(Long idUsuario, String nombre, String titulo, Noticia noticia);

    MeGusta buscarNoticiaLikeadaPorUsuario(Long idUsuario, Long idNoticia);

    List<MeGusta> obtenerMeGustas(Long idUsuario);

    List<Noticia> setNoticiasLikeadas(List<Noticia> noticias, Long idUsuario);

    void republicarNoticia(Republicacion republicacion);

    List<Noticia> obtenerNoticiasCategoria(long idUsuario, int candidadNoticias);

    void compartirNoticia(Notificacion notificacion);

    List<Noticia> obtenerNoticiasDeUnUsuario(Long idUsuario);
}
