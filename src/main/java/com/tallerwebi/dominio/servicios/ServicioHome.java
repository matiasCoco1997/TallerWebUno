package com.tallerwebi.dominio.servicios;

import com.tallerwebi.dominio.entidades.Categoria;
import com.tallerwebi.dominio.entidades.Noticia;
import com.tallerwebi.dominio.entidades.Notificacion;
import com.tallerwebi.dominio.entidades.Usuario;
import org.springframework.stereotype.Service;

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
}
