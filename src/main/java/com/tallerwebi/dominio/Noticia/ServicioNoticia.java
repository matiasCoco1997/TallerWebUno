package com.tallerwebi.dominio.Noticia;

import com.tallerwebi.dominio.Noticia.Noticia;
import com.tallerwebi.dominio.Usuario.Usuario;

public interface ServicioNoticia {
    boolean crearNoticia(Noticia noticia);

    Noticia consultarNoticiaTitulo(String titulo);
}
