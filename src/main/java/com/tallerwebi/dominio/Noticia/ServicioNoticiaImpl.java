package com.tallerwebi.dominio.Noticia;

import com.tallerwebi.dominio.Noticia.Noticia;
import com.tallerwebi.dominio.Noticia.RepositorioNoticia;
import com.tallerwebi.dominio.Noticia.ServicioNoticia;
import com.tallerwebi.dominio.Usuario.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service("servicioNoticia")
//@Transactional
public class ServicioNoticiaImpl implements ServicioNoticia {
    private RepositorioNoticia repositorioNoticia;


    @Override
    public boolean crearNoticia(Noticia noticia, String rol) {

        boolean resultado = false;

        if(rol == "Editor"){
            resultado = true;
        }

        return resultado;
    }

    @Override
    public Noticia consultarNoticiaTitulo(String titulo) {
        return null;
    }
}
