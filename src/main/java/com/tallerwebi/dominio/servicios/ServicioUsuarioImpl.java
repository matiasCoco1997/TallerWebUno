package com.tallerwebi.dominio.servicios;

import com.tallerwebi.dominio.entidades.Categoria;
import com.tallerwebi.dominio.entidades.Noticia;
import com.tallerwebi.dominio.entidades.Seguidos;
import com.tallerwebi.dominio.entidades.Notificacion;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.excepcion.FormatoDeImagenIncorrecto;
import com.tallerwebi.dominio.excepcion.RelacionNoEncontradaException;
import com.tallerwebi.dominio.excepcion.TamanioDeArchivoSuperiorALoPermitido;
import com.tallerwebi.infraestructura.RepositorioCategoria;
import com.tallerwebi.infraestructura.RepositorioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service("servicioUsuario")
@Transactional
public class ServicioUsuarioImpl implements ServicioUsuario{

    private final RepositorioUsuario repositorioUsuario;
    private final RepositorioCategoria repositorioCategoria;
    @Autowired
    public ServicioUsuarioImpl(RepositorioUsuario repositorioUsuario, RepositorioCategoria repositorioCategoria) {
        this.repositorioUsuario = repositorioUsuario;
        this.repositorioCategoria = repositorioCategoria;
    }

    @Override
    public List<Noticia> obtenerNoticiasDeUnUsuario(Long idUsuario) {
        return repositorioUsuario.obtenerMisNoticias(idUsuario);
    }

    @Override
    public List<Categoria> obtenerCategorias(){
        return repositorioCategoria.obtenerCategorias();
    }

    @Override
    public Usuario obtenerUsuarioPorId(Long id) throws Exception {
        Usuario usuario= repositorioUsuario.obtenerUsuarioPorId(id);
        if(verificarSiElUsuarioEsNull(usuario)){
            throw new Exception();
        }
        return usuario;
    }

    public boolean verificarSiElUsuarioEsNull(Usuario usuario) {
        return usuario == null;
    }

    @Override
    public List<Noticia> obtenerNoticiasDeUnUsuarioEnEstadoBorrador(Long idUsuario) {
        return repositorioUsuario.obtenerMisNoticiasEnEstadoBorrador(idUsuario);
    }

    @Override
    public void agregarSeguido(Usuario usuarioLogueado, Usuario usuarioSeguir) {
        Seguidos seguidos = new Seguidos();
        seguidos.setIdUsuarioSeguidor(usuarioLogueado);
        seguidos.setIdUsuarioPropio(usuarioSeguir);
        if (!repositorioUsuario.obtenerListaDeSeguidos(usuarioLogueado.getIdUsuario())
                .stream()
                .anyMatch(seguido -> seguido.getIdUsuarioPropio().getIdUsuario().equals(usuarioSeguir.getIdUsuario()))) {
            repositorioUsuario.crearSeguidos(seguidos);
        }
    }

    @Override
    public List<Notificacion> obtenerMisNotificacionesSinLeer(Long idUsuario) {
        return repositorioUsuario.obtenerMisNotificacionesSinLeer(idUsuario);
    }

    @Override
    public List<Notificacion> obtenerMisNotificaciones(Long idUsuario) {
        return repositorioUsuario.obtenerMisNotificaciones(idUsuario);
    }

    @Override
    public void marcarNotificacionesComoLeidas(Long idUsuario) {
        repositorioUsuario.marcarNotificacionesComoLeidas(idUsuario);
    }
    @Override
    public Map<String,Integer> obtenerMisSeguidoresYSeguidos(Long idUsuario) {
        Map<String,Integer> seguidosSeguidores= new HashMap<>();
        seguidosSeguidores.put("seguidores",repositorioUsuario.obtenerListaDeSeguidores(idUsuario).size());
        seguidosSeguidores.put("seguidos",repositorioUsuario.obtenerListaDeSeguidos(idUsuario).size());
        return seguidosSeguidores;
    }


    @Override
    public void dejarDeSeguirUsuario(Long idSeguido, Long idSeguidor) throws RelacionNoEncontradaException {
        repositorioUsuario.dejarDeSeguir(idSeguido,idSeguidor);
    }

    @Override
    public List<Usuario> listarUsuarioParaSeguir(long idSeguidor){
        return repositorioUsuario.listarUsuariosRecomendadosSinSeguir(idSeguidor);
    }

    @Override
    public boolean verificarSiLaDescripcionEsNull(String descripcion) {
        return descripcion==null;
    }

    @Override
    public boolean verificarSiElIDEsNull(Long id) {
        return id==null;
    }

    @Override
    public void borrarUsuario(Long idUsuario) {
        this.repositorioUsuario.borrarUsuario(idUsuario);
    }

    @Override
    public void modificarDatosUsuario(Usuario usuario, MultipartFile imagen) throws TamanioDeArchivoSuperiorALoPermitido, FormatoDeImagenIncorrecto, IOException {

        if(!imagen.isEmpty()){
            Long tamanioDeImagen = imagen.getSize();
            long maxTamanioDeImagen = 5 * 1024 * 1024;

            if(tamanioDeImagen > maxTamanioDeImagen){
                throw new TamanioDeArchivoSuperiorALoPermitido();
            }

            String nombreDelArchivo = UUID.randomUUID().toString();
            byte[] bytes = imagen.getBytes();
            String nombreOriginalImagen = imagen.getOriginalFilename();
            usuario.setAltFotoPerfil(nombreOriginalImagen);

            if(! nombreOriginalImagen.endsWith(".jpg") && !nombreOriginalImagen.endsWith(".jpeg") && !nombreOriginalImagen.endsWith(".png")){
                throw new FormatoDeImagenIncorrecto();
            }

            String extensionDelArchivoSubido = nombreOriginalImagen.substring(nombreOriginalImagen.lastIndexOf("."));
            String nuevoNombreDelArchivo = nombreDelArchivo + extensionDelArchivoSubido;

            File folder = new File("src/main/webapp/resources/core/imagenes/imgsPerfiles");

            if(!folder.exists()){
                folder.mkdirs();
            }

            Path path = Paths.get("src/main/webapp/resources/core/imagenes/imgsPerfiles/" + nuevoNombreDelArchivo);

            Path imagenABorrar = Paths.get("src/main/webapp/resources/core" + usuario.getFotoPerfil());
            Files.deleteIfExists(imagenABorrar);

            usuario.setFotoPerfil("/imagenes/imgsPerfiles/" + nuevoNombreDelArchivo);

            Files.write(path, bytes);
        }
        repositorioUsuario.modificar(usuario);
    }
}
