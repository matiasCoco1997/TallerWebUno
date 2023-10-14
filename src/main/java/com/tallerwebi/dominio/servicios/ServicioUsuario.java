package com.tallerwebi.dominio.servicios;

import com.tallerwebi.dominio.entidades.Categoria;
import com.tallerwebi.dominio.entidades.Noticia;
import com.tallerwebi.dominio.entidades.Usuario;

import java.util.List;

public interface ServicioUsuario {
    List<Noticia> obtenerNoticiasDeUnUsuario(Long idUsuario);
    List<Categoria> obtenerCategorias();

    Usuario obtenerUsuarioPorId(Long id) throws Exception;

    boolean verificarSiLaDescripcionEsNull(String descripcion);

    boolean verificarSiElIDEsNull(Long id);
}
