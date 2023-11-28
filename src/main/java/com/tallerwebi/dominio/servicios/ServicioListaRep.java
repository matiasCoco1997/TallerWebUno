package com.tallerwebi.dominio.servicios;

import com.tallerwebi.dominio.entidades.ListaReproduccion;
import com.tallerwebi.dominio.entidades.Noticia;
import com.tallerwebi.dominio.entidades.Usuario;

import java.util.List;

public interface ServicioListaRep {

    void agregarNoticiaALista(ListaReproduccion listaReproduccion);

    List<ListaReproduccion> obtenerListaReproduccionDelUsuarioLogueado(Long idUsuario);

    ListaReproduccion buscarListaReproduccion(Long idNoticia, Long idUsuario) throws Exception;

    void eliminarNoticiaDeLista(ListaReproduccion lista) throws Exception;
}
