package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidades.Noticia;
import com.tallerwebi.dominio.entidades.Notificacion;
import com.tallerwebi.dominio.entidades.Seguidos;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.excepcion.RelacionNoEncontradaException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
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
                createQuery("FROM Noticia WHERE usuario.idUsuario= :idUsuario AND activa = true").
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
                .createQuery("FROM Seguidos WHERE idUsuarioPropio.idUsuario = :idUsuarioPropio")
                .setParameter("idUsuarioPropio", idUsuario)
                .list();
    }
    @Override
    public List<Notificacion> obtenerMisNotificaciones(Long idUsuario) {
        final Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM Notificacion WHERE usuarioNotificado_idUsuario = :idUsuario or usuarioEmisor = :idUsuarioEmisor").
                setParameter("idUsuario",idUsuario).setParameter("idUsuarioEmisor",idUsuario).list();
    }

    @Override
    public List<Notificacion> obtenerMisNotificacionesSinLeer(Long idUsuario) {
        final Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM Notificacion WHERE vista=false and usuarioNotificado_idUsuario = :idUsuario").
                setParameter("idUsuario",idUsuario).list();
    }

    @Override
    public void marcarNotificacionesComoLeidas(Long idUsuario) {
        final Session session = sessionFactory.getCurrentSession();
        session.createQuery("UPDATE Notificacion SET vista=true WHERE usuarioNotificado_idUsuario = :idUsuario").
                setParameter("idUsuario",idUsuario).executeUpdate();
    }

    @Override
    public List<Noticia> obtenerMisNoticiasEnEstadoBorrador(Long idUsuario) {
        return sessionFactory.getCurrentSession().
                createQuery("FROM Noticia WHERE usuario.idUsuario= :idUsuario AND activa = false").
                setParameter("idUsuario",idUsuario).list();
    }


    @Override
    public void crearSeguidos(Seguidos seguidos) {
        sessionFactory.getCurrentSession().save(seguidos);
    }

    @Override
    public List<Seguidos> obtenerListaDeSeguidos(Long idUsuarioSeguidor) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Seguidos WHERE idUsuarioSeguidor.idUsuario = :idUsuarioSeguidor")
                .setParameter("idUsuarioSeguidor", idUsuarioSeguidor)
                .list();
    }
    @Override
    public void dejarDeSeguir(Long idSeguido, Long idSeguidor){
        Session session = sessionFactory.getCurrentSession();
        try {
            Query query = sessionFactory.getCurrentSession().createQuery(
                    "FROM Seguidos s WHERE s.idUsuarioPropio.idUsuario = :idSeguido AND s.idUsuarioSeguidor.idUsuario = :idSeguidor");
            query.setParameter("idSeguido", idSeguido);
            query.setParameter("idSeguidor", idSeguidor);

            Seguidos seguidos = (Seguidos) query.uniqueResult();

            if (seguidos != null) {
                sessionFactory.getCurrentSession().delete(seguidos);
            } else {
                throw new RelacionNoEncontradaException("No se encontró la relación Seguidos entre el usuario " + idSeguido + " y el seguidor " + idSeguidor);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error al dejar de seguir", e);
        }
    }

    @Override
    public List<Usuario> listarUsuariosRecomendadosSinSeguir(Long idSeguidor){
        String query = "SELECT u FROM Usuario u WHERE u.idUsuario != :idSeguidor AND u.idUsuario NOT IN " +
                "(SELECT s.idUsuarioPropio.idUsuario FROM Seguidos s WHERE s.idUsuarioSeguidor.idUsuario = :idSeguidor)";

        return sessionFactory.getCurrentSession().createQuery(query, Usuario.class)
                .setParameter("idSeguidor", idSeguidor)
                .getResultList();
    }

    @Override
    public void borrarUsuario(Long idUsuario) {
        final Session session = sessionFactory.getCurrentSession();
        Usuario usuario = this.obtenerUsuarioPorId(idUsuario);
        session.delete(usuario);
    }

    @Override
    public List<Noticia> obtenerNoticiaDeSeguidos(Long idSeguidor) {
        String query = "SELECT n FROM Noticia n WHERE n.usuario.idUsuario != :idSeguidor AND n.usuario.idUsuario IN " +
                "(SELECT s.idUsuarioPropio.idUsuario FROM Seguidos s WHERE s.idUsuarioSeguidor.idUsuario = :idSeguidor)";

        return sessionFactory.getCurrentSession().createQuery(query, Noticia.class)
                .setParameter("idSeguidor", idSeguidor)
                .getResultList();
    }

    @Override
    public List<Usuario> listarUsuariosSeguidos(Long idUsuarioSeguidor) {
        String query = "SELECT u FROM Usuario u WHERE u.idUsuario != :idSeguidor AND u.idUsuario IN " +
                "(SELECT s.idUsuarioPropio.idUsuario FROM Seguidos s WHERE s.idUsuarioSeguidor.idUsuario = :idSeguidor)";

        return sessionFactory.getCurrentSession().createQuery(query, Usuario.class)
                .setParameter("idSeguidor", idUsuarioSeguidor)
                .getResultList();
    }

    @Override
    public List<Usuario> listarUsuariosQueMeSiguen(Long idUsuario) {
        String query = "SELECT u FROM Usuario u WHERE u.idUsuario != :idSeguido AND u.idUsuario IN " +
                "(SELECT s.idUsuarioSeguidor.idUsuario FROM Seguidos s WHERE s.idUsuarioPropio.idUsuario = :idSeguido)";

        return sessionFactory.getCurrentSession().createQuery(query, Usuario.class)
                .setParameter("idSeguido", idUsuario)
                .getResultList();
    }


}
