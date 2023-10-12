package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidades.Categoria;

import java.util.ArrayList;
import java.util.List;

public interface RepositorioCategoria {
    ArrayList<Categoria> buscarTodasCategoria();
    void guardar(Categoria categoria);

    void modificar(Categoria categoria);

    Categoria buscarPorId(Integer categoriaId);

    Categoria buscarPorDescripcion(String descripcion);

    List<Categoria> obtenerCategorias();
}
