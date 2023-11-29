package com.tallerwebi.dominio.servicios;

import com.tallerwebi.dominio.entidades.*;

import java.util.List;


public interface ServicioHome {

     List<Noticia> listarNoticias();

    List<Usuario> listarUsuarios(Long idUsuario);

    List<Categoria> obtenerCategorias();

    List<Noticia> obtenerNoticiasPorCategoria(String descripcion);

    List<Noticia> obtenerNoticiasPorTitulo(String titulo);

    boolean validarQueHayNoticias(List<Noticia> noticias);

    List<Notificacion> obtenerMisNotificaciones(Long idUsuario);

    List<Notificacion> obtenerMisNotificacionesSinLeer(Long idUsuario);

    List<Noticia> obtenerNoticiaDeSeguidos(Long idUsuario);

    List<Object> obtenerPosts();

    Categoria obtenerCategoriaPorDescripcion(String categoria);

    void aumentarCantidadDeVistasDeUnaCategoria(String categoriaObtenida);

    List<Categoria> obtenerCategoriasSegunVisitas();

}
