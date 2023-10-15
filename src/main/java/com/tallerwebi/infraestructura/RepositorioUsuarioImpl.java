package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidades.Noticia;
import com.tallerwebi.dominio.entidades.Notificacion;
import com.tallerwebi.dominio.entidades.Seguidos;
import com.tallerwebi.dominio.entidades.Usuario;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("repositorioUsuario")
public class RepositorioUsuarioImpl implements RepositorioUsuario {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioUsuarioImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Usuario buscarUsuario(String email, String password) {

        final Session session = sessionFactory.getCurrentSession();
        return (Usuario) session.createCriteria(Usuario.class)
                .add(Restrictions.eq("email", email))
                .add(Restrictions.eq("password", password))
                .uniqueResult();
    }

    @Override
    public void guardar(Usuario usuario) {
        sessionFactory.getCurrentSession().save(usuario);
    }

    @Override
    public Usuario buscar(String email) {
        return (Usuario) sessionFactory.getCurrentSession().createCriteria(Usuario.class)
                .add(Restrictions.eq("email", email))
                .uniqueResult();
    }

    @Override
    public void modificar(Usuario usuario) {
        sessionFactory.getCurrentSession().update(usuario);
    }

    @Override
    public Usuario consultarMailExistente(String email) {
        return (Usuario) sessionFactory.getCurrentSession().createCriteria(Usuario.class)
                .add(Restrictions.eq("email", email))
                .uniqueResult();
    }

    @Override
    public List<Noticia> obtenerMisNoticias(Long idUsuario) {
        return sessionFactory.getCurrentSession().
                createQuery("FROM Noticia WHERE usuario.idUsuario= :idUsuario").
                setParameter("idUsuario",idUsuario).list();
    }

    @Override
    public Usuario obtenerUsuarioPorId(Long id) {
        return (Usuario) sessionFactory.getCurrentSession().
                createQuery("FROM Usuario WHERE idUsuario = :id").
                setParameter("id",id).uniqueResult();
    }

    @Override
    public List<Seguidos> obtenerListaDeSeguidores(Long idUsuario) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Seguidos WHERE idUsuarioPropio_idUsuario = :idUsuarioPropio")
                .setParameter("idUsuarioPropio", idUsuario)
                .list();
    }
    @Override
    public List<Notificacion> obtenerMisNotificaciones(Long idUsuario) {
        final Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM Notificacion WHERE usuarioNotificado_idUsuario = :idUsuario").
                setParameter("idUsuario",idUsuario).list();
    }

    @Override
    public List<Notificacion> obtenerMisNotificacionesSinLeer(Long idUsuario) {
        final Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM Notificacion WHERE vista=false and  usuarioNotificado_idUsuario = :idUsuario").
                setParameter("idUsuario",idUsuario).list();
    }

    @Override
    public void marcarNotificacionesComoLeidas(Long idUsuario) {
        final Session session = sessionFactory.getCurrentSession();
        session.createQuery("UPDATE Notificacion SET vista=true WHERE usuarioNotificado_idUsuario = :idUsuario").
                setParameter("idUsuario",idUsuario).executeUpdate();
    }

}
