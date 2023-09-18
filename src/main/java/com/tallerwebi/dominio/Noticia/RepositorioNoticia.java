package com.tallerwebi.dominio.Noticia;

import com.tallerwebi.dominio.Noticia.Noticia;

public interface RepositorioNoticia {
    Noticia buscarNoticia(String titulo);
    Boolean guardar(Noticia noticia);
    Boolean modificar(Noticia noticia);

}
