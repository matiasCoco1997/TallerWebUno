package com.tallerwebi.dominio.Servicios;

import com.tallerwebi.dominio.Entidades.Noticia;

public interface ServicioNoticia {
    void crearNoticia(Noticia noticia);

    void borrarNoticiaPorId(Long idNoticia);

    Noticia buscarNoticiaPorId(Long idNoticia);

    void editarNoticia(Long idNoticia);

}
