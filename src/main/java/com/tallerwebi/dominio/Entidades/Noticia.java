package com.tallerwebi.dominio.Entidades;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Noticia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idNoticia;
    @Column(nullable = false)
    private String titulo;
    private String descripcion;
    private String categoria;
    private String resumen;
    private String rutaDeimagen;
    private String fechaDePublicacion;
    private String rutaDeAudioPodcast;
    private Boolean activa;
}
