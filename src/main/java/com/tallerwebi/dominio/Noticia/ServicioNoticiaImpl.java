package com.tallerwebi.dominio.Noticia;

import com.tallerwebi.dominio.Noticia.Noticia;
import com.tallerwebi.dominio.Noticia.RepositorioNoticia;
import com.tallerwebi.dominio.Noticia.ServicioNoticia;
import com.tallerwebi.dominio.Usuario.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service("servicioNoticia")
public class ServicioNoticiaImpl implements ServicioNoticia {

    @Autowired
    private RepositorioNoticia repositorioNoticia;

    @Override
    public void crearNoticia(Noticia noticia) {

        repositorioNoticia.guardar(noticia);

    }

}
