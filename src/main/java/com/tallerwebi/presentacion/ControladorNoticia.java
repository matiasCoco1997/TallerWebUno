package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Categoria;
import com.tallerwebi.dominio.Noticia;
import com.tallerwebi.dominio.RepositorioCategoria;
import com.tallerwebi.dominio.ServicioNoticia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

@Controller
public class ControladorNoticia {
    private ServicioNoticia servivioNoticia;
    private RepositorioCategoria repositorioCategoria;
    @Autowired
    public ControladorNoticia(ServicioNoticia servicioNoticia, RepositorioCategoria repositorioCategoria) {
        this.servivioNoticia = servicioNoticia;
        this.repositorioCategoria = repositorioCategoria;
    }

    @RequestMapping("/nueva-noticia")
    public ModelAndView irNuevaNoticia() {
        ModelMap modelo = new ModelMap();
        modelo.put("datosNoticia", new Noticia());
        modelo.put("categorias", repositorioCategoria.buscarTodasCategoria());
        return new ModelAndView("cargar-noticia", modelo);
    }

    @RequestMapping(path = "/nueva-noticia", method = RequestMethod.POST)
    public ModelAndView guardarNuevaNoticia(@ModelAttribute("datosNoticia") Noticia noticia,
                                            @RequestParam("imagenArchivo") MultipartFile multipartFile,
                                            @RequestParam("categoriasLista") ArrayList<Integer>  categoriasSeleccionadas ) {
        try {
            noticia.setImagen(multipartFile.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(categoriasSeleccionadas);

        Set<Categoria> categoriasSet = new HashSet<>();
        for (Integer categoriaId : categoriasSeleccionadas) {
            Categoria categoria = repositorioCategoria.buscarPorId(Integer.valueOf(categoriaId));//Tengo que cambiar el buscarPorId para que busque por la descripción
            if (categoria != null) {
                categoriasSet.add(categoria);
            }
        }
        noticia.setCategorias(categoriasSet);



        ModelMap model = new ModelMap();
        servivioNoticia.crearNoticia(noticia);
        Noticia noticiaGuardada = servivioNoticia.consultarNoticiaTitulo(noticia.getTitulo());
        String imagenBase64 = Base64.getEncoder().encodeToString(noticiaGuardada.getImagen()); // Convierte la imagen a una cadena Base64

        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("noticia", noticia);
        modelAndView.addObject("imagenBase64", imagenBase64);
        return modelAndView;

    }


}
