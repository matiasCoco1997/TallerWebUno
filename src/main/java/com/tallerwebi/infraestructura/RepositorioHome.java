package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidades.Categoria;
import com.tallerwebi.dominio.entidades.Noticia;
import com.tallerwebi.dominio.entidades.Usuario;

import java.util.List;

public interface RepositorioHome {
    List<Noticia> listarNoticias();

    List<Usuario> listarUsuarios();

    List<Categoria> listarCategorias();

    List<Noticia> obtenerNoticiasPorCategoria(String descripcion);
}