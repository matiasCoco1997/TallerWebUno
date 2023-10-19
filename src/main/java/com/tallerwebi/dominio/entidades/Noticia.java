package com.tallerwebi.dominio.entidades;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private String altDeImagen;
    private LocalDateTime fechaDePublicacion;
    private String rutaDeAudioPodcast;
    private Boolean activa;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "noticiasLikeadas")
    private List<Usuario> likes;

    @ManyToOne(cascade = CascadeType.ALL)
    private Usuario usuario;

    @PrePersist// Esto generararia automáticamente la fecha de creación antes que se persista
    protected void onCreate() {
        fechaDePublicacion = LocalDateTime.now();
        likes = new ArrayList<>(0);
    }

    public void setAltImagenNoticia(String nombreOriginalImagen) {
        this.altDeImagen = nombreOriginalImagen;
    }
}
