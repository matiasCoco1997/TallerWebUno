package com.tallerwebi.dominio;

public interface ServicioNoticia {
    Boolean crearNoticia(Noticia noticia);

    Noticia consultarNoticiaTitulo(String titulo);
}
