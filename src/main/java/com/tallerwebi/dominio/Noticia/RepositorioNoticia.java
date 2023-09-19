package com.tallerwebi.dominio.Noticia;

import com.tallerwebi.dominio.Noticia.Noticia;

import java.util.List;

public interface RepositorioNoticia {

    Boolean guardar(Noticia noticia);


    Boolean modificar(Noticia noticia);


    void borrarNoticia(Noticia noticia);

    Noticia buscarPorId(long idNoticia);

    List<Noticia> buscarPorTitulo(String tituloDeLaNoticia);
}
