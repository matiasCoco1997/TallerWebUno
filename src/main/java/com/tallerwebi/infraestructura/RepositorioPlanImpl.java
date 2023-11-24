package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidades.Categoria;
import com.tallerwebi.dominio.entidades.Comentario;
import com.tallerwebi.dominio.entidades.Plan;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository("repositorioPlan")

public class RepositorioPlanImpl implements RepositorioPlan{
    private final SessionFactory sessionFactory;

    @Autowired
    public RepositorioPlanImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    @Override
    public List<Plan> traerPlanes() {
        final Session session = sessionFactory.getCurrentSession();
        return session.createCriteria(Plan.class).list();
    }

    @Override
    public Plan traerPlanPorId(Long idPlan) {
        final Session session = sessionFactory.getCurrentSession();
        return (Plan) session.createCriteria(Plan.class)
                .add(Restrictions.eq("idPlan", idPlan))
                .uniqueResult();
    }

    @Override
    public void guardarPlan(Plan plan) {
        sessionFactory.getCurrentSession().save(plan);
    }
}
