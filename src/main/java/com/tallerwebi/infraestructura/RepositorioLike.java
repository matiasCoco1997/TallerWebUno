package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidades.MeGusta;
import com.tallerwebi.dominio.entidades.Noticia;

import java.util.List;

public interface RepositorioLike {

    void guardarLike(MeGusta meGusta);

    List<MeGusta> verificarSiElMeGustaDelUsuarioYaExiste(Long idNoticia, Long idusuario);

    void borrarLike(MeGusta megustaEnNoticia);

    List<MeGusta> obtenerMegustas(Long idUsuario);

    List<String> traerCategoriasLikeadasPorUnUsuario(Long idUsuario);

}
