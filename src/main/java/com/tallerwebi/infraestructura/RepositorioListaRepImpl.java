package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidades.ListaReproduccion;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("repositorioListaRep")
public class RepositorioListaRepImpl implements RepositorioListaRep{
    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioListaRepImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void agregarNoticiaALista(ListaReproduccion lista) {
        sessionFactory.getCurrentSession().save(lista);
    }

    @Override
    public List<ListaReproduccion> obtenerListaReproduccionDelUsuarioLogueado(Long idUsuario) {
        return sessionFactory.getCurrentSession().
                createQuery("FROM ListaReproduccion WHERE usuario.id= :idUsuario").setParameter("idUsuario",idUsuario).list();
    }

    @Override
    public ListaReproduccion buscarListaPorID(Long id) {
        return (ListaReproduccion) sessionFactory.getCurrentSession().createQuery("FROM ListaReproduccion  where id = :id").setParameter("id",id).uniqueResult();
    }

    @Override
    public ListaReproduccion buscarListaReproduccion(Long idNoticia, Long idUsuario) {
        return (ListaReproduccion) sessionFactory.getCurrentSession().
                createQuery("FROM ListaReproduccion WHERE usuario_idUsuario= :idUsuario and noticia_idNoticia= :idNoticia")
                .setParameter("idUsuario",idUsuario).setParameter("idNoticia",idNoticia).setMaxResults(1).uniqueResult();
    }

    @Override
    public void eliminarNoticiaDeLista(ListaReproduccion lista) {
        sessionFactory.getCurrentSession().
                createQuery("DELETE FROM ListaReproduccion WHERE id= :idLista")
                .setParameter("idLista",lista.getId()).executeUpdate();
    }


}
