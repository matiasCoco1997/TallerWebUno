package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Noticia.Noticia;
import com.tallerwebi.dominio.Noticia.RepositorioNoticia;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("repositorioNoticia")
public class RepositorioNoticiaImpl implements RepositorioNoticia {


    @Override
    public Boolean guardar(Noticia noticia) {
        return true;
    }

    @Override
    public Boolean modificar(Noticia noticia) {
        return true;
    }

    @Override
    public void borrarNoticia(Noticia noticia) {

    }
}
