package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidades.Comentario;
import com.tallerwebi.dominio.entidades.Noticia;
import com.tallerwebi.dominio.entidades.Notificacion;
import com.tallerwebi.dominio.entidades.Usuario;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("repositorioNoticia")
public class RepositorioNoticiaImpl implements RepositorioNoticia {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioNoticiaImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Boolean guardar(Noticia noticia) {
        try {
            sessionFactory.getCurrentSession().save(noticia);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean modificar(Noticia noticia) {
        try {
            final Session session = sessionFactory.getCurrentSession();
            session.update(noticia);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void borrarNoticia(Noticia noticia) {
        final Session session = sessionFactory.getCurrentSession();
        session.delete(noticia);
    }

    @Override
    public Noticia buscarPorId(long idNoticia) {
        final Session session = sessionFactory.getCurrentSession();
        return (Noticia) session.createQuery("FROM Noticia WHERE idNoticia =:idNoticia").
                setParameter("idNoticia",idNoticia).uniqueResult();
    }

    @Override
    public List<Noticia> buscarPorTitulo(String tituloDeLaNoticia) {
        final Session session = sessionFactory.getCurrentSession();
        return session.createCriteria(Noticia.class)
                .add(Restrictions.eq("titulo", tituloDeLaNoticia))
                .list();
    }

    @Override
    public List<Noticia> buscarPorCategoria(String categoria) {
        final Session session = sessionFactory.getCurrentSession();
        return session.createCriteria(Noticia.class)
                .add(Restrictions.eq("categoria", categoria))
                .list();
    }

    @Override
    public List<Noticia> listarNoticias() {
        final Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM Noticia").list();
    }

    @Override
    public void editarNoticia(Noticia noticia) {
        try {
            final Session session = sessionFactory.getCurrentSession();
            session.update(noticia);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void generarNotificacion(Notificacion notificacion) {
        sessionFactory.getCurrentSession().save(notificacion);
    }

    @Override
    public List<Usuario> obtenerLikes(Long idNotiocia) {
        final Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Usuario.class);
        criteria.createAlias("noticiasLikeadas", "n");
        criteria.add(Restrictions.eq("n.idNoticia", idNotiocia));
        return criteria.list();
    }

    @Override
    public void darMeGusta(Noticia noticia, Usuario usuario) {
        final Session session = sessionFactory.getCurrentSession();
        /*try {
            noticia.getLikes().add(usuario);
            session.update(noticia);
            usuario.getNoticiasLikeadas().add(noticia);
            session.update(usuario);
        } catch (NullPointerException e) {
            List<Usuario> likes = new ArrayList<>();
            likes.add(usuario);
            noticia.setLikes(likes);
            session.update(noticia);

            List<Noticia> noticiasLikeadas = new ArrayList<>();
            noticiasLikeadas.add(noticia);
            usuario.setNoticiasLikeadas(noticiasLikeadas);
            session.update(usuario);
        }
        */
    }

    @Override
    public Boolean modificarLikes(Noticia noticia) {
        try {
            sessionFactory.getCurrentSession().merge(noticia);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
