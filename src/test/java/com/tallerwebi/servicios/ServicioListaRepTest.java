package com.tallerwebi.servicios;

import com.tallerwebi.dominio.entidades.Categoria;
import com.tallerwebi.dominio.entidades.ListaReproduccion;
import com.tallerwebi.dominio.entidades.Noticia;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.servicios.ServicioHome;
import com.tallerwebi.dominio.servicios.ServicioHomeImpl;
import com.tallerwebi.dominio.servicios.ServicioListaRep;
import com.tallerwebi.dominio.servicios.ServicioListaRepImpl;
import com.tallerwebi.infraestructura.RepositorioHome;
import com.tallerwebi.infraestructura.RepositorioListaRep;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public class ServicioListaRepTest {

    private Noticia noticiaMock;
    private ServicioListaRep servicioListaRep;
    private RepositorioListaRep repositorioListaRepMock;
    private Usuario usuarioMock;
    private ListaReproduccion listaMock;


    @BeforeEach
    public void init(){
        noticiaMock = mock(Noticia.class);
        usuarioMock=mock(Usuario.class);
        when(usuarioMock.getIdUsuario()).thenReturn(1L);
        listaMock=mock(ListaReproduccion.class);
        when(listaMock.getUsuario()).thenReturn(usuarioMock);
        when(listaMock.getUsuario().getIdUsuario()).thenReturn(1L);
        when(listaMock.getNoticia()).thenReturn(noticiaMock);
        this.repositorioListaRepMock = mock(RepositorioListaRep.class);
        this.servicioListaRep = new ServicioListaRepImpl(this.repositorioListaRepMock);
    }

    @Test
    public void queUnaListaSePuedaAgregarALaBaseDeDatos(){
        servicioListaRep.agregarNoticiaALista(listaMock);
        verify(repositorioListaRepMock,times(1)).agregarNoticiaALista(listaMock);
    }

    @Test
    public void quePuedaObtenerLaListaDeUnSoloUsuario(){
        List<ListaReproduccion> listas= new ArrayList<>();
        listas.add(listaMock);
        listas.add(listaMock);
        when(repositorioListaRepMock.obtenerListaReproduccionDelUsuarioLogueado(usuarioMock.getIdUsuario())).thenReturn(listas);
        Integer sizeListas=repositorioListaRepMock.obtenerListaReproduccionDelUsuarioLogueado(usuarioMock.getIdUsuario()).size();
        assertThat(sizeListas, Matchers.is(2));
    }


}
