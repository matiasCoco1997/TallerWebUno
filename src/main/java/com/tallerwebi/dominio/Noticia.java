package com.tallerwebi.dominio;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Noticia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idNoticia;
    private String titulo;
    private String descripcion;

    @ManyToMany
    @JoinTable(
            name = "noticia_categoria",
            joinColumns = @JoinColumn(name = "noticia_id"),
            inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    private Set<Categoria> categorias = new HashSet<>();
    @Lob
    private String noticia;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] imagen;
    private LocalDate fechaPublicacion;
    private String audioPodcastRuta;
    private Boolean activa; //habria que hacer un boton para que el usuario periodista pueda dar una baja logica de la nota
}
