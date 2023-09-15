package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Noticia;
import com.tallerwebi.dominio.RepositorioNoticia;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("repositorioNoticia")
public class RepositorioNoticiaImpl implements RepositorioNoticia {
    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioNoticiaImpl(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }
    @Override
    public Noticia buscarNoticia(String titulo) {
        final Session session = sessionFactory.getCurrentSession();
        return (Noticia) session.createCriteria(Noticia.class)
                .add(Restrictions.eq("titulo", titulo))
                .uniqueResult();//TIENE QUE DEVOLVER UNA LISTA ACA, PUEDE HABER VARIAS NOTAS CON TITULOS PARECIDOS
    }

    @Override
    public void guardar(Noticia noticia) {
        sessionFactory.getCurrentSession().save(noticia);
    }


    @Override
    public void modificar(Noticia noticia) {

    }
}
