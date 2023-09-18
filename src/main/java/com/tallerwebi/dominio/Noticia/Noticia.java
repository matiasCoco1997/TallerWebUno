package com.tallerwebi.dominio.Noticia;

import com.tallerwebi.dominio.Categoria.Categoria;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
public class Noticia {
    private Long idNoticia;
    private String titulo;
    private String descripcion;
    private String categoria;
    private String noticia;
    private String imagen;
    private String fechaPublicacion;
    private String audioPodcastRuta;
    private Boolean activa;
}
