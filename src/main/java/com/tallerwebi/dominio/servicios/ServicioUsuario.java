package com.tallerwebi.dominio.servicios;

import com.tallerwebi.dominio.entidades.Categoria;
import com.tallerwebi.dominio.entidades.Noticia;
import com.tallerwebi.dominio.entidades.Notificacion;
import com.tallerwebi.dominio.entidades.Usuario;

import java.util.List;

public interface ServicioUsuario {
    List<Noticia> obtenerNoticiasDeUnUsuario(Long idUsuario);
    List<Categoria> obtenerCategorias();

    Usuario obtenerUsuarioPorId(Long id) throws Exception;

    boolean verificarSiLaDescripcionEsNull(String descripcion);

    boolean verificarSiElIDEsNull(Long id);
    boolean verificarSiElUsuarioEsNull(Usuario usuario);

    List<Noticia> obtenerNoticiasDeUnUsuarioEnEstadoBorrador(Long idUsuario);

    void agregarSeguido(Usuario usuarioLogueado, Usuario usuarioSeguir);

    List<Notificacion> obtenerMisNotificaciones(Long idUsuario);

    void marcarNotificacionesComoLeidas(Long idUsuario);

}
