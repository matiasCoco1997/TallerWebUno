package com.tallerwebi.dominio.entidades;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;

@Entity
@Setter
@Getter
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    private Usuario usuario;

    @ManyToOne(cascade = CascadeType.ALL)
    private Noticia noticia;
    
    public Like(Usuario usuario, Noticia noticia) {
        this.usuario = usuario;
        this.noticia = noticia;
    }

    public Like() {

    }
}