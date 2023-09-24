package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidades.Comentario;
import com.tallerwebi.dominio.excepcion.DescripcionComentarioException;

import java.util.List;

public interface RepositorioComentario {
    void guardar(Comentario comentario) throws DescripcionComentarioException;

    List<Comentario> buscarComentariosPorIdNoticia(Long idPublicacion);
}
