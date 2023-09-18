package com.tallerwebi.dominio.Noticia;

import com.tallerwebi.dominio.Noticia.Noticia;

public interface RepositorioNoticia {

    Boolean guardar(Noticia noticia);


    Boolean modificar(Noticia noticia);


    void borrarNoticia(Noticia noticia);
}
