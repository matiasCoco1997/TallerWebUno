package com.tallerwebi.dominio.servicios;

import com.tallerwebi.dominio.entidades.Noticia;

import java.util.List;

public interface ServicioNoticia {
    void crearNoticia(Noticia noticia);

    void borrarNoticiaPorId(Long idNoticia);

    Noticia buscarNoticiaPorId(Long idNoticia);

    List<Noticia> buscarNoticiaPorTitulo(String tituloNoticia);

    void editarNoticia(Long idNoticia);

    List<Noticia> listarNoticias();

    List<Noticia> buscarNoticiaPorCategoria(String categoria);
}