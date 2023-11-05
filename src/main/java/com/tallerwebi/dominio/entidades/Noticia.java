package com.tallerwebi.dominio.entidades;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "republicada")
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
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate fechaDePublicacion;
    private String rutaDeAudioPodcast;
    private Boolean activa;
    private  Integer likes;
    private  Boolean estaLikeada;
    private  Boolean esAnonima;
    @ManyToOne
    private Usuario usuario;

    @PrePersist// Esto generararia automáticamente la fecha de creación antes que se persista
    protected void onCreate() {
        fechaDePublicacion = LocalDate.now();
        likes = 0;
        estaLikeada=false;
    }

    public void setAltImagenNoticia(String nombreOriginalImagen) {
        this.altDeImagen = nombreOriginalImagen;
    }
}
