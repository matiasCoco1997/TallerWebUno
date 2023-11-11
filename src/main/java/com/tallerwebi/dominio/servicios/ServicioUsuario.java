package com.tallerwebi.dominio.servicios;

import com.tallerwebi.dominio.entidades.Categoria;
import com.tallerwebi.dominio.entidades.Noticia;
import com.tallerwebi.dominio.entidades.Notificacion;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.excepcion.FormatoDeImagenIncorrecto;
import com.tallerwebi.dominio.excepcion.RelacionNoEncontradaException;
import com.tallerwebi.dominio.excepcion.TamanioDeArchivoSuperiorALoPermitido;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ServicioUsuario {
    List<Noticia> obtenerNoticiasDeUnUsuario(Long idUsuario);
    List<Categoria> obtenerCategorias();

    Usuario obtenerUsuarioPorId(Long id) throws Exception;

    boolean verificarSiLaDescripcionEsNull(String descripcion);

    boolean verificarSiElIDEsNull(Long id);
    boolean verificarSiElUsuarioEsNull(Usuario usuario);

    List<Noticia> obtenerNoticiasDeUnUsuarioEnEstadoBorrador(Long idUsuario);

    void agregarSeguido(Usuario usuarioLogueado, Usuario usuarioSeguir);

    List<Notificacion> obtenerMisNotificaciones(Long idUsuario);
    List<Notificacion> obtenerMisNotificacionesSinLeer(Long idUsuario);

    void marcarNotificacionesComoLeidas(Long idUsuario);
    Map<String,Integer> obtenerMisSeguidoresYSeguidos(Long idUsuario);

    void borrarUsuario(Long idUsuario);

    void dejarDeSeguirUsuario(Long idSeguido, Long idSeguidor) throws RelacionNoEncontradaException;

    List<Usuario> listarUsuarioParaSeguir(long idSeguidor);

    List<Usuario> listarUsuarioseguidos(Long idUsuario);

    List<Usuario> listarUsuarioQueMeSiguen(Long idUsuario);

    void modificarDatosUsuario(Usuario usuario, MultipartFile imagen) throws TamanioDeArchivoSuperiorALoPermitido, FormatoDeImagenIncorrecto, IOException;

    List<Noticia> obtenerNoticiasLikeadas(Long idUsuario);

    List<Notificacion> obtenerMisNoticiasCompartidas(Long idUsuarioPropio , Long idUsuarioBuscado);

}
