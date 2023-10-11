package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidades.ListaReproduccion;

import java.util.List;

public interface RepositorioListaRep {
    void agregarNoticiaALista(ListaReproduccion lista);

    List<ListaReproduccion> obtenerListaReproduccionDelUsuarioLogueado(Long idUsuario);
}
