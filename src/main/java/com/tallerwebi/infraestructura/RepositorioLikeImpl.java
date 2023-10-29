package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidades.*;
import org.hibernate.SessionFactory;
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
    public void guardarLike(MeGusta meGusta) {
        sessionFactory.getCurrentSession().merge(meGusta);
    }

    @Override
    public List<MeGusta> verificarSiElMeGustaDelUsuarioYaExiste(Long idNoticia, Long idusuario) {
        return (List<MeGusta>) sessionFactory.getCurrentSession()
                .createQuery("FROM MeGusta WHERE usuario.idUsuario = :idusuarioPropio AND noticia.idNoticia = :idNoticiaLikeada")
                .setParameter("idusuarioPropio", idusuario)
                .setParameter("idNoticiaLikeada", idNoticia)
                .list();
    }

    @Override
    public void borrarLike(MeGusta megustaEnNoticia) {
        sessionFactory.getCurrentSession().delete(megustaEnNoticia);
    }

    @Override
    public List<MeGusta> obtenerMegustas(Long idUsuario) {
        return (List<MeGusta>) sessionFactory.getCurrentSession()
                .createQuery("FROM MeGusta WHERE usuario.idUsuario = :idusuarioPropio")
                .setParameter("idusuarioPropio", idUsuario)
                .list();
    }

}
