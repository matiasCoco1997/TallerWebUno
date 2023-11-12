package com.tallerwebi.dominio.servicios;

import com.tallerwebi.dominio.entidades.Rol;
import com.tallerwebi.dominio.excepcion.CampoVacio;
import com.tallerwebi.dominio.excepcion.FormatoDeImagenIncorrecto;
import com.tallerwebi.dominio.excepcion.TamanioDeArchivoSuperiorALoPermitido;
import com.tallerwebi.infraestructura.RepositorioUsuario;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service("servicioLogin")
@Transactional
public class ServicioLoginImpl implements ServicioLogin {

    private RepositorioUsuario repositorioLogin;


    @Autowired
    public ServicioLoginImpl(RepositorioUsuario servicioLoginDao){
        this.repositorioLogin = servicioLoginDao;

    }

    @Override
    public Usuario consultarUsuario (String email, String password) {
        return repositorioLogin.buscarUsuario(email, password);
    }

    @Override
    public void registrar(Usuario usuario, MultipartFile imagen) throws UsuarioExistente, CampoVacio, IOException, FormatoDeImagenIncorrecto, TamanioDeArchivoSuperiorALoPermitido {

        verificacionCamposVacios(usuario, imagen);

        Usuario usuarioEncontrado = repositorioLogin.consultarMailExistente(usuario.getEmail());

        if(usuarioEncontrado != null){
            throw new UsuarioExistente();
        }

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

        usuario.setFotoPerfil("/imagenes/imgsPerfiles/" + nuevoNombreDelArchivo);

        Files.write(path, bytes);
        usuario.setRol(Rol.USER);
        repositorioLogin.guardar(usuario);

    }

    private void verificacionCamposVacios(Usuario usuario, MultipartFile imagen) throws CampoVacio {
        if(     usuario.getNombre().isBlank() ||
                usuario.getApellido().isBlank() ||
                usuario.getEmail().isBlank() ||
                usuario.getPassword().isBlank() ||
                usuario.getPais().isBlank()||
                usuario.getCiudad().isBlank() ||
                imagen.isEmpty()||
                usuario.getFechaDeNacimiento() == null
        ){

            throw new CampoVacio();
        }
    }

}

