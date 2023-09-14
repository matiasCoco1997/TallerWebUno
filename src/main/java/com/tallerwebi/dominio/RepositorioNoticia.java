package com.tallerwebi.dominio;

public interface RepositorioNoticia {
    Noticia buscarNoticia(String titulo);
    void guardar(Noticia noticia);
    void modificar(Noticia noticia);
}
