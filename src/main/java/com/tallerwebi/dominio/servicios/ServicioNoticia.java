package com.tallerwebi.dominio.servicios;

import com.tallerwebi.dominio.entidades.Noticia;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.excepcion.CampoVacio;
import com.tallerwebi.dominio.excepcion.CategoriaInexistente;
import com.tallerwebi.dominio.excepcion.FormatoDeImagenIncorrecto;
import com.tallerwebi.dominio.excepcion.TamanioDeArchivoSuperiorALoPermitido;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ServicioNoticia {
    void crearNoticia(Noticia noticia, Usuario usuarioLogueado, MultipartFile imagen) throws CampoVacio, CategoriaInexistente, TamanioDeArchivoSuperiorALoPermitido, IOException, FormatoDeImagenIncorrecto;

    void borrarNoticiaPorId(Long idNoticia);

    Noticia buscarNoticiaPorId(Long idNoticia);

    List<Noticia> buscarNoticiaPorTitulo(String tituloNoticia);

    void editarNoticia(Long idNoticia);

    List<Noticia> listarNoticias();

    List<Noticia> buscarNoticiaPorCategoria(String categoria);
}
