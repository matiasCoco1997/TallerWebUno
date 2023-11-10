package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidades.*;

import java.util.List;

public interface RepositorioHome {
    List<Noticia> listarNoticias();

    List<Usuario> listarUsuarios(Long idUsuario);

    List<Categoria> listarCategorias();

    List<Noticia> obtenerNoticiasPorCategoria(String descripcion);

    List<Noticia> obtenerNoticiasPorTitulo(String titulo);

    List<Republicacion> obtenerRepublicaciones();

    Categoria obtenerCategoriaPorDescripcion(String categoria);

    void aumentarCantidadDeVistasDeUnaCategoria(String categoriaObtenida);

}
