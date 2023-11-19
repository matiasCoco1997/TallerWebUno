package com.tallerwebi.infraestructura;

import com.itextpdf.text.pdf.PdfPTable;
import com.tallerwebi.dominio.entidades.*;
import com.tallerwebi.dominio.excepcion.RelacionNoEncontradaException;

import java.util.List;

public interface RepositorioUsuario {

    Usuario buscarUsuario(String email, String password);
    void guardar(Usuario usuario);
    Usuario buscar(String email);
    void modificar(Usuario usuario);
    Usuario consultarMailExistente(String email);

    List<Noticia> obtenerMisNoticias(Long idUsuario);

    Usuario obtenerUsuarioPorId(Long id);

    List<Seguidos> obtenerListaDeSeguidores(Long idUsuario);

    List<Notificacion> obtenerMisNotificaciones(Long idUsuario);

    List<Notificacion> obtenerMisNotificacionesSinLeer(Long idUsuario);

    void marcarNotificacionesComoLeidas(Long idUsuario);

    List<Noticia> obtenerMisNoticiasEnEstadoBorrador (Long idUsuario);

    void crearSeguidos(Seguidos seguidos);

    List<Seguidos> obtenerListaDeSeguidos(Long idUsuario);

    void dejarDeSeguir(Long idSeguido, Long isSeguidor) throws RelacionNoEncontradaException;

    List<Usuario> listarUsuariosRecomendadosSinSeguir(Long idSeguidor);

    void borrarUsuario(Long idUsuario);

    List<Noticia> obtenerNoticiaDeSeguidos(Long idUsuario);

    List<Usuario> listarUsuariosSeguidos(Long idUsuarioSeguidor);

    List<Usuario> listarUsuariosQueMeSiguen(Long idUsuario);

    List<Noticia>obtenerNoticiasLikeadasPorElUsuario(Long idUsuario);

    List<Notificacion>obtenerMisNoticiasCompartidas(Long idUsuario);

    List<Notificacion>obtenerMisNoticiasCompartidasDeUnUsuarioEspecifico(Long idUsuario, Long usuarioBuscado);

    List<Usuario> obtenerUsuarios();

    List<ListaReproduccion> obtenerMiListaDeReproduccion(Long idUsuario);
}

