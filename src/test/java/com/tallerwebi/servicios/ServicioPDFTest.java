package com.tallerwebi.servicios;

import com.tallerwebi.dominio.entidades.Categoria;
import com.tallerwebi.dominio.servicios.ServicioPDF;
import com.tallerwebi.dominio.servicios.ServicioPDFImpl;
import com.tallerwebi.infraestructura.RepositorioHome;
import com.tallerwebi.infraestructura.RepositorioNoticia;
import com.tallerwebi.infraestructura.RepositorioUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServicioPDFTest {

    private ServicioPDF servicioPDFMock;
    private RepositorioHome repositorioHomeMock;
    private RepositorioNoticia repositorioNoticiaMock;
    private RepositorioUsuario repositorioUsuarioMock;
    private Categoria categoriaMock;

    @BeforeEach
    public void init(){
        categoriaMock = mock(Categoria.class);
        when(categoriaMock.getDescripcion()).thenReturn("Deportes");
        this.repositorioHomeMock = mock(RepositorioHome.class);
        this.repositorioNoticiaMock = mock(RepositorioNoticia.class);
        this.repositorioUsuarioMock = mock(RepositorioUsuario.class);
        this.servicioPDFMock = new ServicioPDFImpl(this.repositorioHomeMock, repositorioNoticiaMock, repositorioUsuarioMock);
    }

    @Test
    public void queSeCompleteLaListaDeEncabezados(){
        List<String> encabezados=servicioPDFMock.completarListaDeEncabezados();
        assertThat(encabezados.size(),is(3));
    }
}
