package com.tallerwebi.dominio.Servicios;

import com.tallerwebi.dominio.Entidades.Comentario;
import com.tallerwebi.dominio.excepcion.DescripcionComentarioException;
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
    public void guardarComentario(Comentario comentario) throws DescripcionComentarioException {
        repositorioComentario.guardar(comentario);
    }

    @Override
    public List<Comentario> buscarComentarios(Long idPublicacion) {
        return repositorioComentario.buscarComentariosPorIdNoticia(idPublicacion);
    }
}
