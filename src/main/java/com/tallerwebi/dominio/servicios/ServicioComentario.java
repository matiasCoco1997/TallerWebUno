package com.tallerwebi.dominio.Servicios;

import com.tallerwebi.dominio.Entidades.Comentario;
import com.tallerwebi.dominio.excepcion.DescripcionComentarioException;

import java.util.List;

public interface ServicioComentario {
    void guardarComentario(Comentario comentario) throws DescripcionComentarioException;

    List<Comentario> buscarComentarios(Long idPublicacion);
}
