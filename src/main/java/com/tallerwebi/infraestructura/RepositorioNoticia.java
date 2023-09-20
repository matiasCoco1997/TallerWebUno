package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Entidades.Noticia;

import java.util.List;

public interface RepositorioNoticia {

    Boolean guardar(Noticia noticia);


    Boolean modificar(Noticia noticia);


    void borrarNoticia(Noticia noticia);

    Noticia buscarPorId(Long idNoticia);

    List<Noticia> buscarPorTitulo(String tituloDeLaNoticia);
}
