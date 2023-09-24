package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidades.Comentario;
import com.tallerwebi.dominio.entidades.Noticia;
import com.tallerwebi.dominio.excepcion.DescripcionComentarioException;
import lombok.SneakyThrows;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("repositorioComentario")
public class RepositorioComentarioImpl implements RepositorioComentario {
    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioComentarioImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public void guardar(Comentario comentario) throws DescripcionComentarioException {
        try {
            sessionFactory.getCurrentSession().save(comentario);
        } catch (Exception e) {
            throw new DescripcionComentarioException("El Comentario no puede ser vacio" + e.getMessage());
        }

    }

    @Override
    public List<Comentario> buscarComentariosPorIdNoticia(Long idPublicacion) {
        final Session session = sessionFactory.getCurrentSession();
        return session.createCriteria(Comentario.class)
                .add(Restrictions.eq("idNoticia", idPublicacion))
                .list();
    }
}
