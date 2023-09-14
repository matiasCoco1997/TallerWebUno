package com.tallerwebi.dominio;

import java.util.ArrayList;

public interface RepositorioCategoria {
    ArrayList<Categoria> buscarTodasCategoria();
    void guardar(Categoria categoria);
    Categoria buscar(String descripcion);
    void modificar(Categoria categoria);

    Categoria buscarPorId(Integer categoriaId);
}
