package com.tallerwebi.dominio.entidades;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.servlet.annotation.MultipartConfig;
import javax.validation.constraints.NotBlank;
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

    @NotNull
    //@NotBlank
    private String nombre;

    @NotNull
    //@NotBlank
    private String apellido;

    @NotNull
    //@NotBlank
    private String pais;

    @NotNull
    //@NotBlank
    private String ciudad;

    @NotNull
    //@NotBlank
    private Date fechaDeNacimiento;

    @NotNull
    //@NotBlank
    private String email;

    @NotNull
    //@NotBlank
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
