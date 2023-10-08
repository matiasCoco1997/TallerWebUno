package com.tallerwebi.dominio.servicios;

import com.tallerwebi.dominio.entidades.Comentario;
import com.tallerwebi.dominio.excepcion.ComentarioException;
import com.tallerwebi.infraestructura.RepositorioComentario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ServicioComentarioImpl implements ServicioComentario{
    private RepositorioComentario repositorioComentario;
    @Autowired
    public ServicioComentarioImpl(RepositorioComentario repositorioComentario) {
        this.repositorioComentario = repositorioComentario;
    }

    @Override
    public void guardarComentario(Comentario comentario) throws ComentarioException {
        comentario.validar();
        repositorioComentario.guardar(comentario);
    }

    @Override
    public List<Comentario> buscarComentarios(Long idPublicacion) {
        return repositorioComentario.buscarComentariosPorIdNoticia(idPublicacion);
    }

    @Override
    public Comentario buscarComentarioPorId(Long idComentario) {
        return repositorioComentario.buscarPorId(idComentario) ;
    }

    @Override
    public Boolean eliminarComentario(Long idComentario, Long idUsuario) {
        Comentario comentario = this.buscarComentarioPorId(idComentario);
        return repositorioComentario.eliminarComentario(comentario);
    }
}
