package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidades.Comentario;
import com.tallerwebi.dominio.excepcion.ComentarioException;

import java.util.List;

public interface RepositorioComentario {
    void guardar(Comentario comentario) throws ComentarioException;

    List<Comentario> buscarComentariosPorIdNoticia(Long idPublicacion);

    Comentario buscarPorId(Long idComentario);

    Boolean eliminarComentario(Comentario comentario);
}
