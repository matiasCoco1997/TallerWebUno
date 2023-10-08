package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidades.Comentario;
import com.tallerwebi.dominio.excepcion.ComentarioException;
import org.hibernate.HibernateException;
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
    public void guardar(Comentario comentario) throws ComentarioException {
        try {
            sessionFactory.getCurrentSession().save(comentario);
        } catch (Exception e) {
            throw new ComentarioException("El Comentario no puede ser nulo" + e.getMessage());
        }

    }

    @Override
    public List<Comentario> buscarComentariosPorIdNoticia(Long idPublicacion) {
        final Session session = sessionFactory.getCurrentSession();
        return session.createCriteria(Comentario.class)
                .add(Restrictions.eq("idNoticia", idPublicacion))
                .list();
    }

    @Override
    public Comentario buscarPorId(Long idComentario) {
        final Session session = sessionFactory.getCurrentSession();
        return (Comentario) session.createCriteria(Comentario.class)
                .add(Restrictions.eq("id", idComentario))
                .uniqueResult();
    }

    @Override
    public Boolean eliminarComentario(Comentario comentario) {
        try {
            sessionFactory.getCurrentSession().delete(comentario);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
