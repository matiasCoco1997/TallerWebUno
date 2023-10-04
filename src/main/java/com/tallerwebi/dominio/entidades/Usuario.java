package com.tallerwebi.dominio.entidades;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.servlet.annotation.MultipartConfig;
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
    //@NotNull
    private String nombre;
    //@NotNull
    private String apellido;
    //@NotNull
    private String pais;
    //@NotNull
    private String ciudad;
    //@NotNull
    @DateTimeFormat(iso = ISO.DATE)
    private Date fechaDeNacimiento;
    //@NotNull
    private String email;
    //@NotNull
    private String password;
    //@NotNull
    private String fotoPerfil;
    private Boolean activo = false;
    private Integer idPlan; //Relacionado a la tabla plan, idPlan
    @Transient
    private List<Usuario> seguidos; //revisar
    @Transient
    private List<Usuario> seguidores; //revisar

}
