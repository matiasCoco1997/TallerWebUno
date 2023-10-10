package com.tallerwebi.dominio.servicios;

import com.tallerwebi.dominio.entidades.Categoria;
import com.tallerwebi.dominio.entidades.Noticia;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.infraestructura.RepositorioHome;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service("servicioHome")
@Transactional
public class ServicioHomeImpl implements ServicioHome {

    private final RepositorioHome repositorioHome;

    @Autowired
    public ServicioHomeImpl(RepositorioHome repositorioHome) {
        this.repositorioHome = repositorioHome;
    }

    @Override
    public List<Noticia> listarNoticias() {
        return repositorioHome.listarNoticias();
    }

    @Override
    public List<Usuario> listarUsuarios() {
        return repositorioHome.listarUsuarios();
    }

    @Override
    public List<Categoria> obtenerCategorias() {
        return repositorioHome.listarCategorias();
    }

    @Override
    public List<Noticia> obtenerNoticiasPorCategoria(String descripcion) {
        return repositorioHome.obtenerNoticiasPorCategoria(descripcion);
    }
}
