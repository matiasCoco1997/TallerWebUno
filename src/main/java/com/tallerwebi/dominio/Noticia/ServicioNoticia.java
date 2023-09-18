package com.tallerwebi.dominio.Noticia;

public interface ServicioNoticia {
    void crearNoticia(Noticia noticia);

    void borrarNoticiaPorId(Long idNoticia);

    Noticia buscarNoticiaPorId(Long idNoticia);

}
