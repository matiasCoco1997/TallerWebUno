package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidades.Categoria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository("repositorioCategoria")
public class RepositorioCategoriaImpl implements RepositorioCategoria {
    private SessionFactory sessionFactory;
    @Autowired
    public RepositorioCategoriaImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public ArrayList<Categoria> buscarTodasCategoria() {
        final Session session = sessionFactory.getCurrentSession();
        return (ArrayList<Categoria>) session.createCriteria(Categoria.class).list();
    }

    @Override
    public void guardar(Categoria categoria) {
        sessionFactory.getCurrentSession().save(categoria);
    }

    @Override
    public Categoria buscarPorDescripcion(String descripcion) {
        final Session session = sessionFactory.getCurrentSession();
        return (Categoria) session.createCriteria(Categoria.class)
                .add(Restrictions.eq("descripcion", descripcion))
                .uniqueResult();
    }

    @Override
    public void modificar(Categoria categoria) {

    }

    @Override
    public Categoria buscarPorId(Integer categoriaId) {
        final Session session = sessionFactory.getCurrentSession();
        return (Categoria) session.createCriteria(Categoria.class)
                .add(Restrictions.eq("idCategoria", categoriaId))
                .uniqueResult();
    }
}