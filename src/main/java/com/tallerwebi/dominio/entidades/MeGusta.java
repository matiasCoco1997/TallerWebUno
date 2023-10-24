package com.tallerwebi.dominio.entidades;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
public class MeGusta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Usuario usuario;

    @ManyToOne
    private Noticia noticia;

    public MeGusta(Usuario usuario, Noticia noticia) {
        this.usuario = usuario;
        this.noticia = noticia;
    }

    public MeGusta() {

    }
}