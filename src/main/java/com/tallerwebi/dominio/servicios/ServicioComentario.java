package com.tallerwebi.dominio.servicios;

import com.tallerwebi.dominio.entidades.Comentario;
import com.tallerwebi.dominio.excepcion.ComentarioException;

import java.util.List;

public interface ServicioComentario {
    void guardarComentario(Comentario comentario) throws ComentarioException;

    List<Comentario> buscarComentarios(Long idPublicacion);

    Comentario buscarComentarioPorId(Long id);

    Boolean eliminarComentario(Long l, Long idUsuario);

    void modificarComentario(Comentario comentarioMock, Long idUsuario) throws ComentarioException;
}
