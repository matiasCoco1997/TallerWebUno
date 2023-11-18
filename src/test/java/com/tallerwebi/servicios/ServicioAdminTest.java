package com.tallerwebi.servicios;

import com.tallerwebi.dominio.entidades.Noticia;
import com.tallerwebi.dominio.servicios.ServicioAdmin;
import com.tallerwebi.dominio.servicios.ServicioAdminImpl;
import com.tallerwebi.infraestructura.RepositorioNoticia;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServicioAdminTest {

    ServicioAdmin servicioAdmin;
    RepositorioNoticia repositorioNoticiaMock;
    Noticia noticiaMock;

    @BeforeEach
    public void init() {
        repositorioNoticiaMock = mock(RepositorioNoticia.class);
        List<Noticia> noticiasDeportes = new ArrayList<>();
        noticiasDeportes.add(mock(Noticia.class));
        noticiasDeportes.add(mock(Noticia.class));
        noticiasDeportes.add(mock(Noticia.class));
        noticiasDeportes.add(mock(Noticia.class));
        when(repositorioNoticiaMock.buscarPorCategoria("Deportes")).thenReturn(noticiasDeportes);
        List<Noticia> noticiasPolitica = new ArrayList<>();
        noticiasPolitica.add(mock(Noticia.class));
        noticiasPolitica.add(mock(Noticia.class));
        noticiasPolitica.add(mock(Noticia.class));
        when(repositorioNoticiaMock.buscarPorCategoria("Politica")).thenReturn(noticiasPolitica);

        servicioAdmin = new ServicioAdminImpl(repositorioNoticiaMock);
    }

    @Test
    public void cuandoTraigoLasNoticiasDeDeportesMeTrae4() {
        //preparación
        Integer totalNoticiasEsperado = 4;
        //ejecución
        Integer totalNoticias = servicioAdmin.obtenerNroNoticiasPorCategoria("Deportes");
        //verificación
        assertThat(totalNoticias, equalTo(totalNoticiasEsperado));
    }

    @Test
    public void cuandoTraigoLasNoticiasDePoliticaMeTrae3() {
        //preparación
        Integer totalNoticiasEsperado = 3;
        //ejecución
        Integer totalNoticias = servicioAdmin.obtenerNroNoticiasPorCategoria("Politica");
        //verificación
        assertThat(totalNoticias, equalTo(totalNoticiasEsperado));
    }
}
