package com.tallerwebi.dominio.entidades;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;



@Entity
@Getter
@Setter
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    private String nombre;
    private String apellido;
    private String email;
    private String password;
    private String pais;
    private String ciudad;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaDeNacimiento;
    private String fotoPerfil;
    private Boolean activo = false;
    private Integer idPlan; //Relacionado a la tabla plan, idPlan
    @Transient
    private List<Usuario> seguidos; //revisar
    @Transient
    private List<Usuario> seguidores; //revisar

}
