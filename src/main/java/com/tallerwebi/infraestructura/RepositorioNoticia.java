package com.tallerwebi.infraestructura;

import com.itextpdf.text.pdf.PdfPTable;
import com.tallerwebi.dominio.entidades.*;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public interface RepositorioNoticia {

    Boolean modificarLikes(Noticia noticia);

    Boolean guardar(Noticia noticia);

    Boolean modificar(Noticia noticia);

    void borrarNoticia(Noticia noticia);

    Noticia buscarPorId(long idNoticia);

    List<Noticia> buscarPorTitulo(String tituloDeLaNoticia);

    List<Noticia> buscarPorCategoria(String categoria);

    List<Noticia> listarNoticias();

    void editarNoticia(Noticia noticia);

    void generarNotificacion(Notificacion notificacion);

    List<Usuario> obtenerLikes(Long idNoticia);

    List<Republicacion> obtenerRepublicaciones();

    void republicarNoticia(Republicacion republicacion);

    List<Noticia> obtenerNoticiasCategoria(int i, List<String> categorias);

    List<Noticia> obtenerMisNoticias(Long idUsuario);

    Boolean marcarNoticiaComoLikeada(Noticia noticia);


    List<Noticia> obtenerNoticiaPorFecha(String fecha);

    Long obtenerCantidadNoticiasRelacionadas(String categoria);

    List<Comentario> obtenerComentarios(Long idNoticia);

    Long obtenerCantidadDeListasDeUnaNoticia(Long idNoticia);

    List<Noticia> listarNoticiasMasLikeadas();

}
