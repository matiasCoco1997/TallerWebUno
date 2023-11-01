package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidades.Notificacion;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("repositorioNotificacion")
public class RepositorioNotificacionImpl implements RepositorioNotificacion{
    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioNotificacionImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    @Override
    public void generarNotificacion(Notificacion notificacion) {
        sessionFactory.getCurrentSession().save(notificacion);
    }

    @Override
    public List<Notificacion> obtenerTodasLasNotificaciones() {
        return sessionFactory.getCurrentSession().createQuery("FROM Notificacion").list();
    }
}
