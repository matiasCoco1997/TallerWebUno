package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Categoria;
import com.tallerwebi.dominio.RepositorioCategoria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.ArrayList;

@Repository("repositorioCategoria")
@Transactional
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

    }

    @Override
    public Categoria buscar(String descripcion) {
        return null;
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
