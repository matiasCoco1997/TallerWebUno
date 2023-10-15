package com.tallerwebi.dominio.servicios;

import com.tallerwebi.dominio.entidades.Categoria;
import com.tallerwebi.dominio.entidades.Noticia;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.excepcion.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ServicioNoticia {
    void crearNoticia(Noticia noticia, Usuario usuarioLogueado, MultipartFile imagen, MultipartFile audio) throws CampoVacio, TamanioDeArchivoSuperiorALoPermitido, IOException, FormatoDeImagenIncorrecto, FormatoDeAudioIncorrecto;

    void borrarNoticiaPorId(Long idNoticia);

    Noticia buscarNoticiaPorId(Long idNoticia);

    List<Noticia> buscarNoticiaPorTitulo(String tituloNoticia);

    void editarNoticia(Noticia noticia, Usuario usuarioLogueado, MultipartFile imagen, MultipartFile audio) throws CampoVacio, TamanioDeArchivoSuperiorALoPermitido, IOException, FormatoDeImagenIncorrecto, FormatoDeAudioIncorrecto;

    List<Noticia> listarNoticias();

    List<Noticia> buscarNoticiaPorCategoria(String categoria);

    void darMeGusta(Noticia noticia);

    boolean verificarQueNoEsNull(Noticia noticia);

    List<Categoria> listarCategorias();


    void generarNotificacion(Long idUsuario, String nombre, String titulo, Noticia noticia);
}
