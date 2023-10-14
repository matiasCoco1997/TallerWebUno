package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidades.ListaReproduccion;
import com.tallerwebi.dominio.entidades.Noticia;

import java.util.List;

public interface RepositorioNoticia {

    Boolean guardar(Noticia noticia);

    Boolean modificar(Noticia noticia);

    void borrarNoticia(Noticia noticia);

    Noticia buscarPorId(long idNoticia);

    List<Noticia> buscarPorTitulo(String tituloDeLaNoticia);

    List<Noticia> buscarPorCategoria(String categoria);

    List<Noticia> listarNoticias();

    void editarNoticia(Noticia noticia);
}
