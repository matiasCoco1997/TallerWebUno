package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidades.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("repositorioHome")
public class RepositorioHomeImpl implements RepositorioHome{
    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioHomeImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public List<Noticia> listarNoticias() {
        final Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM Noticia WHERE activa=true ORDER BY fechaDePublicacion DESC").list();
    }
    @Override
    public List<Republicacion> obtenerRepublicaciones() {
        final Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM Republicacion").list();
    }

    @Override
    public Categoria obtenerCategoriaPorDescripcion(String categoria) {
        return (Categoria) sessionFactory.getCurrentSession().createQuery("FROM Categoria WHERE descripcion = :categoria").setParameter("categoria",categoria).uniqueResult();
    }

    @Override
    public void aumentarCantidadDeVistasDeUnaCategoria(String categoria) {
        sessionFactory.getCurrentSession().
                createQuery("UPDATE Categoria SET vistas=vistas+1 WHERE descripcion = :categoria").
                setParameter("categoria",categoria).executeUpdate();
    }

    @Override
    public List<Categoria> listarCategorias() {
        final Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM Categoria").list();
    }

    @Override
    public List<Categoria> obtenerCategoriasSegunVisitas() {
        final Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM Categoria c ORDER BY c.vistas DESC").list();
    }

    @Override
    public List<Usuario> listarUsuarios(Long idUsuario) {
        final Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM Usuario WHERE id != :id")
                .setParameter("id",idUsuario).list();
    }

    @Override
    public List<Noticia> obtenerNoticiasPorCategoria(String descripcion) {
        final Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM Noticia WHERE activa=true AND categoria = :descripcion ORDER BY fechaDePublicacion DESC")
                .setParameter("descripcion", descripcion)
                .list();
    }

    @Override
    public List<Noticia> obtenerNoticiasPorTitulo(String titulo) {
        final Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM Noticia WHERE activa=true and titulo like :titulo ORDER BY fechaDePublicacion DESC")
                .setParameter("titulo", "%"+titulo+"%")
                .list();
    }

}
