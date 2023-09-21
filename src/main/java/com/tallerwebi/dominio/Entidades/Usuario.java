package com.tallerwebi.dominio.Entidades;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Date;

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
    private Date fechaDeNacimiento;
    private String email;
    private String password;
    private String fotoPerfil;
    //private String rol;
    private Boolean activo = false;
    private Plan plan; //Relacionado a la tabla plan, idPlan
    private Integer cantidadSeguidores;

}
