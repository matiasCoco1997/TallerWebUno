package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidades.Categoria;
import com.tallerwebi.dominio.entidades.Noticia;
import com.tallerwebi.dominio.entidades.Usuario;
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
        return session.createQuery("FROM Noticia where activa=true").list();
    }

    @Override
    public List<Usuario> listarUsuarios() {
        final Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM Usuario").list();
    }

    @Override
    public List<Categoria> listarCategorias() {
        final Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM Categoria").list();
    }

    @Override
    public List<Noticia> obtenerNoticiasPorCategoria(String descripcion) {
        final Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM Noticia WHERE categoria = :descripcion")
                .setParameter("descripcion", descripcion)
                .list();
    }

}
