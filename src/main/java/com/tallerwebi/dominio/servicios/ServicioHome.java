package com.tallerwebi.dominio.servicios;

import com.tallerwebi.dominio.entidades.Categoria;
import com.tallerwebi.dominio.entidades.Noticia;
import com.tallerwebi.dominio.entidades.Usuario;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ServicioHome {

     List<Noticia> listarNoticias();

    List<Usuario> listarUsuarios();

    List<Categoria> obtenerCategorias();

    List<Noticia> obtenerNoticiasPorCategoria(String descripcion);
}
