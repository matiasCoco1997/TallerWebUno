package com.tallerwebi.dominio.servicios;

import com.tallerwebi.dominio.entidades.*;
import com.tallerwebi.infraestructura.RepositorioHome;
import com.tallerwebi.infraestructura.RepositorioLike;
import com.tallerwebi.infraestructura.RepositorioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
@Service("servicioHome")
@Transactional
public class ServicioHomeImpl implements ServicioHome {

    private final RepositorioHome repositorioHome;
    private final RepositorioUsuario repositorioUsuario;
    private final RepositorioLike repositorioLikeImpl;

    @Autowired
    public ServicioHomeImpl(RepositorioHome repositorioHome, RepositorioUsuario repositorioUsuario, RepositorioLike repositorioLikeImpl) {
        this.repositorioHome = repositorioHome;
        this.repositorioUsuario = repositorioUsuario;
        this.repositorioLikeImpl = repositorioLikeImpl;
    }

    @Override
    public List<Noticia> listarNoticias() {
        return repositorioHome.listarNoticias();
    }

    @Override
    public List<Usuario> listarUsuarios(Long idUsuario) {
        return repositorioHome.listarUsuarios(idUsuario);
    }

    @Override
    public List<Categoria> obtenerCategorias() {
        return repositorioHome.listarCategorias();
    }

    @Override
    public List<Noticia> obtenerNoticiasPorCategoria(String descripcion) {
        return repositorioHome.obtenerNoticiasPorCategoria(descripcion);
    }

    @Override
    public List<Categoria> obtenerCategoriasSegunVisitas(){
        return repositorioHome.obtenerCategoriasSegunVisitas();
    }

    @Override
    public List<Noticia> obtenerNoticiasPorTitulo(String titulo) {
        return repositorioHome.obtenerNoticiasPorTitulo(titulo);
    }

    @Override
    public boolean validarQueHayNoticias(List<Noticia> noticias) {
        return noticias.size()==0;
    }

    @Override
    public List<Notificacion> obtenerMisNotificaciones(Long idUsuario) {
        return repositorioUsuario.obtenerMisNotificaciones(idUsuario);
    }

    @Override
    public List<Notificacion> obtenerMisNotificacionesSinLeer(Long idUsuario) {
        return repositorioUsuario.obtenerMisNotificacionesSinLeer(idUsuario);
    }
    @Override
    public List<Noticia> obtenerNoticiaDeSeguidos(Long idUsuario) {
        return repositorioUsuario.obtenerNoticiaDeSeguidos(idUsuario);
    }


    @Override
    public List<Object> obtenerPosts() {

        List<Noticia> noticias = repositorioHome.listarNoticias();

        List<Republicacion> republicaciones = repositorioHome.obtenerRepublicaciones();

        int cantidadTotal = noticias.size() + republicaciones.size();

        List<Object> listaFinal = new ArrayList<>(Collections.nCopies(cantidadTotal, null));

        for (Noticia noticia : noticias) {
            int numeroRandom;
            do {
                numeroRandom = generarNumeroAleatorio(cantidadTotal);
            } while (listaFinal.get(numeroRandom) != null);
            listaFinal.set(numeroRandom, noticia);
        }

        for (Republicacion republicacion : republicaciones) {
            int numeroRandom;
            do {
                numeroRandom = generarNumeroAleatorio(cantidadTotal);
            } while (listaFinal.get(numeroRandom) != null);
            listaFinal.set(numeroRandom, republicacion);
        }

        return listaFinal;
    }

    @Override
    public Categoria obtenerCategoriaPorDescripcion(String categoria) {
        return repositorioHome.obtenerCategoriaPorDescripcion(categoria);
    }

    @Override
    public void aumentarCantidadDeVistasDeUnaCategoria(String categoriaObtenida) {
        repositorioHome.aumentarCantidadDeVistasDeUnaCategoria(categoriaObtenida);
    }

    public int generarNumeroAleatorio(Integer limite){
        return (int) (Math.random()*limite);
    }

}
