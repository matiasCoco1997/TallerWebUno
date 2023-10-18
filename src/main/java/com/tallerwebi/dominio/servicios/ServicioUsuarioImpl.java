package com.tallerwebi.dominio.servicios;

import com.tallerwebi.dominio.entidades.Categoria;
import com.tallerwebi.dominio.entidades.Noticia;
import com.tallerwebi.dominio.entidades.Seguidos;
import com.tallerwebi.dominio.entidades.Notificacion;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.excepcion.RelacionNoEncontradaException;
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
        Usuario usuario= repositorioUsuario.obtenerUsuarioPorId(id);
        if(verificarSiElUsuarioEsNull(usuario)){
            throw new Exception();
        }
        return usuario;
    }

    public boolean verificarSiElUsuarioEsNull(Usuario usuario) {
        return usuario == null;
    }

    @Override
    public List<Noticia> obtenerNoticiasDeUnUsuarioEnEstadoBorrador(Long idUsuario) {
        return repositorioUsuario.obtenerMisNoticiasEnEstadoBorrador(idUsuario);
    }

    @Override

    public void agregarSeguido(Usuario usuarioLogueado, Usuario usuarioSeguir) {
        Seguidos seguidos = new Seguidos();
        seguidos.setIdUsuarioSeguidor(usuarioLogueado);
        seguidos.setIdUsuarioPropio(usuarioSeguir);
        if (!repositorioUsuario.obtenerListaDeSeguidos(usuarioLogueado.getIdUsuario())
                .stream()
                .anyMatch(seguido -> seguido.getIdUsuarioPropio().getIdUsuario().equals(usuarioSeguir.getIdUsuario()))) {
            repositorioUsuario.crearSeguidos(seguidos);
        }
    }
    public List<Notificacion> obtenerMisNotificaciones(Long idUsuario) {
        return repositorioUsuario.obtenerMisNotificaciones(idUsuario);
    }

    @Override
    public void marcarNotificacionesComoLeidas(Long idUsuario) {
        repositorioUsuario.marcarNotificacionesComoLeidas(idUsuario);
    }

    @Override
    public void dejarDeSeguirUsuario(Long idSeguido, Long idSeguidor) throws RelacionNoEncontradaException {
        repositorioUsuario.dejarDeSeguir(idSeguido,idSeguidor);
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
