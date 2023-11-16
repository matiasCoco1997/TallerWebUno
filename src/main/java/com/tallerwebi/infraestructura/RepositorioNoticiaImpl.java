package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidades.*;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Collections;
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
    public Boolean marcarNoticiaComoLikeada(Noticia noticia) {
        try {
            final Session session = sessionFactory.getCurrentSession();

            Noticia noticiaPersistente = session.get(Noticia.class, noticia.getIdNoticia());

            if (noticiaPersistente != null) {
                session.merge(noticiaPersistente);
                return true;
            }

            return false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void borrarNoticia(Noticia noticia) {
        final Session session = sessionFactory.getCurrentSession();

        session.createQuery("DELETE FROM MeGusta WHERE noticia = :noticia")
        .setParameter("noticia", noticia).executeUpdate();

        session.createQuery("DELETE FROM Notificacion WHERE noticiaNotificada = :noticia")
                .setParameter("noticia", noticia).executeUpdate();

        session.createQuery("DELETE FROM Comentario WHERE noticia = :noticia")
                .setParameter("noticia", noticia).executeUpdate();

        session.createQuery("DELETE FROM Republicacion WHERE id_noticiaRepublicada = :noticia")
                .setParameter("noticia", noticia).executeUpdate();

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
        return session.createQuery("FROM Noticia WHERE activa=true ORDER BY idNoticia DESC").list();
    }

    @Override
    public List<Noticia> obtenerMisNoticias(Long idUsuario) {
        return sessionFactory.getCurrentSession().
                createQuery("FROM Noticia WHERE usuario.idUsuario= :idUsuario AND activa = true ORDER BY idNoticia DESC").
                setParameter("idUsuario",idUsuario).list();
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
    public List<Republicacion> obtenerRepublicaciones() {
        return sessionFactory.getCurrentSession().createQuery("FROM Republicacion").list();
    }

    @Override
    public void republicarNoticia(Republicacion republicacion) {
        sessionFactory.getCurrentSession().save(republicacion);
    }

    @Override
    public List<Noticia> obtenerNoticiasCategoria(int cantidadNoticias, List<String> categorias) {
        List<Noticia> noticias = sessionFactory.getCurrentSession()
                .createQuery("SELECT n FROM Noticia n WHERE n.categoria IN :categorias")
                .setParameter("categorias", categorias)
                .setMaxResults(cantidadNoticias)
                .list();
        if(noticias.size() == cantidadNoticias){
            return noticias;
        }else{

            noticias.addAll((List<Noticia>) sessionFactory.getCurrentSession().createQuery("SELECT n FROM Noticia n ORDER BY n.fechaDePublicacion DESC")
                    .setMaxResults(cantidadNoticias-noticias.size())
                    .list());
            return noticias;
        }
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
