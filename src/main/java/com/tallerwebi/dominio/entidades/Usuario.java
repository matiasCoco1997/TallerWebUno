package com.tallerwebi.dominio.entidades;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

import  org.springframework.format.annotation.DateTimeFormat.ISO;

@Entity
@Getter
@Setter
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;
    private String nombre;
    private String apellido;
    private String pais;
    private String ciudad;
    @DateTimeFormat(iso = ISO.DATE)
    private Date fechaDeNacimiento;
    private String email;
    private String password;
    private String fotoPerfil;
    @Transient
    private Boolean activo = false;
    @Transient
    private Integer idPlan; //Relacionado a la tabla plan, idPlan
    @Transient
    private List<Usuario> seguidos; //revisar
    @Transient
    private List<Usuario> seguidores; //revisar

}
