package com.tallerwebi.dominio.servicios;

import com.tallerwebi.dominio.entidades.Categoria;
import com.tallerwebi.dominio.entidades.Noticia;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.excepcion.*;
import com.tallerwebi.infraestructura.RepositorioCategoria;
import com.tallerwebi.infraestructura.RepositorioNoticia;
import com.tallerwebi.presentacion.DatosLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service("servicioNoticia")
@Transactional
public class ServicioNoticiaImpl implements ServicioNoticia {

    private final RepositorioNoticia repositorioNoticia;
    private final RepositorioCategoria repositorioCategoria;

    @Autowired
    public ServicioNoticiaImpl(RepositorioNoticia repositorioNoticia, RepositorioCategoria repositorioCategoria) {
        this.repositorioNoticia = repositorioNoticia;
        this.repositorioCategoria = repositorioCategoria;
    }

    @Override
    public void crearNoticia(Noticia noticia, Usuario usuarioLogueado, MultipartFile imagen, MultipartFile audio) throws CampoVacio, TamanioDeArchivoSuperiorALoPermitido, IOException, FormatoDeImagenIncorrecto, FormatoDeAudioIncorrecto {

        verificacionCamposVacios(noticia, imagen, audio);

        verificacionDeLaImagenSeleccionada(noticia, imagen);

        verificacionDelAudioSeleccionado(noticia, audio);

        noticia.setUsuario(usuarioLogueado);

        verificacionDeActivacionDeNoticia(noticia);

        repositorioNoticia.guardar(noticia);
    }

    @Override
    public void borrarNoticiaPorId(Long idNoticia) {
        Noticia noticia = this.buscarNoticiaPorId(idNoticia);
        repositorioNoticia.borrarNoticia(noticia);
    }

    @Override
    public List<Noticia> listarNoticias() {

        List<Noticia> noticias = repositorioNoticia.listarNoticias();
        List<Noticia> noticiasActivas = new ArrayList<>();

        for (Noticia noticia : noticias) {
            if (noticia.getActiva()) {
                noticiasActivas.add(noticia);
            }
        }
        return noticiasActivas;
    }

    @Override
    public Noticia buscarNoticiaPorId(Long idNoticia) {
        return repositorioNoticia.buscarPorId(idNoticia);
    }

    @Override
    public List<Noticia> buscarNoticiaPorTitulo(String tituloNoticia) {

        List<Noticia> noticiasEncontradas = repositorioNoticia.buscarPorTitulo(tituloNoticia);

        return noticiasEncontradas;
    }

    @Override
    public List<Noticia> buscarNoticiaPorCategoria(String categoria) {

        List<Noticia> noticiasEncontradas = repositorioNoticia.buscarPorCategoria(categoria);

        return noticiasEncontradas;
    }

    @Override
    public void editarNoticia(Long idNoticia) {

    }
    @Override
    public void darMeGusta(Noticia noticia) {
        noticia.setLikes(noticia.getLikes() + 1);
        repositorioNoticia.modificar(noticia);
    }

    @Override
    public boolean verificarQueNoEsNull(Noticia noticia) {
        return noticia==null;
    }

    @Override
    public List<Categoria> listarCategorias() {
        return repositorioCategoria.obtenerCategorias();
    }


    private void verificacionCamposVacios(Noticia noticia, MultipartFile imagen, MultipartFile audio) throws CampoVacio {
        if(noticia.getTitulo().isBlank()) {
            throw new CampoVacio();
        }

        if(noticia.getCategoria().isBlank() || noticia.getCategoria() == "0") {
            throw new CampoVacio();
        }

        if(noticia.getResumen().isBlank()) {
            throw new CampoVacio();
        }

        if (imagen.isEmpty()) {
            throw new CampoVacio();
        }

        if (audio.isEmpty()) {
            throw new CampoVacio();
        }
    }



    private void verificacionDeLaImagenSeleccionada(Noticia noticia, MultipartFile imagen) throws TamanioDeArchivoSuperiorALoPermitido, IOException, FormatoDeImagenIncorrecto {
        Long tamanioDeImagen = imagen.getSize();
        Long maxTamanioDeImagen = (long) (5 * 1024 * 1024);

        if(tamanioDeImagen > maxTamanioDeImagen){
            throw new TamanioDeArchivoSuperiorALoPermitido();
        }

        String nombreDelArchivo = UUID.randomUUID().toString();
        byte[] bytes = imagen.getBytes();
        String nombreOriginalImagen = imagen.getOriginalFilename();
        noticia.setAltImagenNoticia(nombreOriginalImagen);

        if(! nombreOriginalImagen.endsWith(".jpg") && !nombreOriginalImagen.endsWith(".jpeg") && !nombreOriginalImagen.endsWith(".png")){
            throw new FormatoDeImagenIncorrecto();
        }

        String extensionDelArchivoSubido = nombreOriginalImagen.substring(nombreOriginalImagen.lastIndexOf("."));
        String nuevoNombreDelArchivo = nombreDelArchivo + extensionDelArchivoSubido;

        File folder = new File("src/main/webapp/resources/core/imagenes/imgsNoticias");

        if(!folder.exists()){
            folder.mkdirs();
        }

        Path path = Paths.get("src/main/webapp/resources/core/imagenes/imgsNoticias/" + nuevoNombreDelArchivo);

        noticia.setRutaDeimagen("/imagenes/imgsNoticias/" + nuevoNombreDelArchivo);

        Files.write(path, bytes);
    }

    private void verificacionDelAudioSeleccionado(Noticia noticia, MultipartFile audio) throws TamanioDeArchivoSuperiorALoPermitido, IOException, FormatoDeAudioIncorrecto {
        Long tamanioDeImagen = audio.getSize();
        Long maxTamanioDeImagen = (long) (10 * 1024 * 1024);

        if(tamanioDeImagen > maxTamanioDeImagen){
            throw new TamanioDeArchivoSuperiorALoPermitido();
        }

        String nombreDelArchivo = UUID.randomUUID().toString();
        byte[] bytes = audio.getBytes();
        String nombreOriginalAudio = audio.getOriginalFilename();

        if(! nombreOriginalAudio.endsWith(".mp3")){
            throw new FormatoDeAudioIncorrecto();
        }

        String extensionDelArchivoSubido = nombreOriginalAudio.substring(nombreOriginalAudio.lastIndexOf("."));
        String nuevoNombreDelArchivo = nombreDelArchivo + extensionDelArchivoSubido;

        File folder = new File("src/main/webapp/resources/core/audios_noticias");

        if(!folder.exists()){
            folder.mkdirs();
        }

        Path path = Paths.get("src/main/webapp/resources/core/audios_noticias/" + nuevoNombreDelArchivo);

        noticia.setRutaDeAudioPodcast("/audios_noticias/" + nuevoNombreDelArchivo);

        Files.write(path, bytes);
    }

    private  void verificacionDeActivacionDeNoticia(Noticia noticia) {
        if(!noticia.getActiva()){
            noticia.setActiva(false);
        }
    }

}
