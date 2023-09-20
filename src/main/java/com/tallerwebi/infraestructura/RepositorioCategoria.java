package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Entidades.Categoria;

import java.util.ArrayList;

public interface RepositorioCategoria {
    ArrayList<Categoria> buscarTodasCategoria();
    void guardar(Categoria categoria);

    void modificar(Categoria categoria);

    Categoria buscarPorId(Integer categoriaId);

    Categoria buscarPorDescripcion(String descripcion);
}
