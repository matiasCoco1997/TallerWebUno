package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidades.*;
import com.tallerwebi.dominio.excepcion.RelacionNoEncontradaException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("repositorioLike")
public class RepositorioLikeImpl implements RepositorioLike {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioLikeImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void guardarLike(Like like) {
        sessionFactory.getCurrentSession().save(like);
    }

}
