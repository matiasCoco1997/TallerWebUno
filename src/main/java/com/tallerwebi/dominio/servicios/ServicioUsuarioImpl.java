package com.tallerwebi.dominio.servicios;

import com.tallerwebi.dominio.entidades.Categoria;
import com.tallerwebi.dominio.entidades.Noticia;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.infraestructura.RepositorioCategoria;
import com.tallerwebi.infraestructura.RepositorioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("servicioUsuario")
@Transactional
public class ServicioUsuarioImpl implements ServicioUsuario{

    private final RepositorioUsuario repositorioUsuario;
    private final RepositorioCategoria repositorioCategoria;
    @Autowired
    public ServicioUsuarioImpl(RepositorioUsuario repositorioUsuario, RepositorioCategoria repositorioCategoria) {
        this.repositorioUsuario = repositorioUsuario;
        this.repositorioCategoria = repositorioCategoria;
    }

    @Override
    public List<Noticia> obtenerNoticiasDeUnUsuario(Long idUsuario) {
        return repositorioUsuario.obtenerMisNoticias(idUsuario);
    }

    @Override
    public List<Categoria> obtenerCategorias(){
        return repositorioCategoria.obtenerCategorias();
    }

    @Override
    public Usuario obtenerUsuarioPorId(Long id) throws Exception {
        return repositorioUsuario.obtenerUsuarioPorId(id);
    }

    @Override
    public boolean verificarSiLaDescripcionEsNull(String descripcion) {
        return descripcion==null;
    }

    @Override
    public boolean verificarSiElIDEsNull(Long id) {
        return id==null;
    }
}
