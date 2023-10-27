package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidades.*;

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

    void darMeGusta(Noticia noticia, Usuario usuario);

    List<Republicacion> obtenerRepublicaciones();

    void republicarNoticia(Republicacion republicacion);
}
