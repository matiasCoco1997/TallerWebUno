package com.tallerwebi.dominio.Categoria;

import com.tallerwebi.dominio.Categoria.Categoria;

import java.util.ArrayList;

public interface RepositorioCategoria {
    ArrayList<Categoria> buscarTodasCategoria();
    void guardar(Categoria categoria);

    void modificar(Categoria categoria);

    Categoria buscarPorId(Integer categoriaId);

    Categoria buscarPorDescripcion(String descripcion);
}
