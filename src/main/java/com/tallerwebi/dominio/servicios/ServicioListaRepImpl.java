package com.tallerwebi.dominio.servicios;

import com.tallerwebi.dominio.entidades.ListaReproduccion;
import com.tallerwebi.infraestructura.RepositorioListaRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("servicioListaRep")
@Transactional
public class ServicioListaRepImpl implements ServicioListaRep {

    private final RepositorioListaRep repositorioListaRep;

    @Autowired
    public ServicioListaRepImpl(RepositorioListaRep repositorioListaRep) {
        this.repositorioListaRep = repositorioListaRep;
    }

    @Override
    public void agregarNoticiaALista(ListaReproduccion lista) {
        repositorioListaRep.agregarNoticiaALista(lista);
    }

    @Override
    public List<ListaReproduccion> obtenerListaReproduccionDelUsuarioLogueado(Long idUsuario) {
        return repositorioListaRep.obtenerListaReproduccionDelUsuarioLogueado(idUsuario);
    }

    @Override
    public ListaReproduccion buscarListaReproduccion(Long idNoticia, Long idUsuario) throws Exception {
        ListaReproduccion listaRep=repositorioListaRep.buscarListaReproduccion(idNoticia,idUsuario);
        if(listaRep==null){
            throw new Exception();
        }
        return listaRep;
    }

    @Override
    public void eliminarNoticiaDeLista(ListaReproduccion lista) throws Exception {
        if(lista==null){
            throw new Exception();
        }
        repositorioListaRep.eliminarNoticiaDeLista(lista);
    }
}
