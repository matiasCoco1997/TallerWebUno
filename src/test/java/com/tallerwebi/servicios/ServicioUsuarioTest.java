package com.tallerwebi.servicios;

import com.tallerwebi.dominio.entidades.*;
import com.tallerwebi.dominio.servicios.ServicioUsuario;
import com.tallerwebi.dominio.servicios.ServicioUsuarioImpl;
import com.tallerwebi.infraestructura.RepositorioCategoria;
import com.tallerwebi.infraestructura.RepositorioUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ServicioUsuarioTest {

    private Noticia noticiaMock;
    private Noticia noticiaMockDeportes;
    private Noticia noticiaMockArte;
    private ServicioUsuario servicioUsuarioMock;
    private RepositorioUsuario repositorioUsuarioMock;
    private RepositorioCategoria repositorioCategoriaMock;
    private Usuario usuarioMock;
    private Categoria categoriaMock;
    private Seguidos seguidosMock;
    private Usuario seguidorMock;
    private Usuario seguidoMock;

    @BeforeEach
    public void init(){
        seguidosMock = mock(Seguidos.class);
        noticiaMock = mock(Noticia.class);
        noticiaMockArte = mock(Noticia.class);
        noticiaMockDeportes = mock(Noticia.class);
        when(noticiaMock.getCategoria()).thenReturn("categoria");
        when(noticiaMock.getTitulo()).thenReturn("categoria");
        when(noticiaMockArte.getCategoria()).thenReturn("arte");
        when(noticiaMockArte.getTitulo()).thenReturn("depor");
        when(noticiaMockDeportes.getCategoria()).thenReturn("deportes");
        when(noticiaMockDeportes.getTitulo()).thenReturn("deportes");
        usuarioMock=mock(Usuario.class);
        when(usuarioMock.getIdUsuario()).thenReturn(1L);
        seguidoMock=mock(Usuario.class);
        when(seguidoMock.getIdUsuario()).thenReturn(1L);
        seguidorMock=mock(Usuario.class);
        when(seguidorMock.getIdUsuario()).thenReturn(2L);
        seguidosMock = mock(Seguidos.class);
        when(seguidosMock.getIdUsuarioPropio()).thenReturn(seguidoMock);
        when(seguidosMock.getIdUsuarioSeguidor()).thenReturn(seguidorMock);

        categoriaMock=mock(Categoria.class);
        this.repositorioUsuarioMock = mock(RepositorioUsuario.class);
        this.repositorioCategoriaMock = mock(RepositorioCategoria.class);
        this.servicioUsuarioMock = new ServicioUsuarioImpl(this.repositorioUsuarioMock,repositorioCategoriaMock);
    }

    @Test
    public void queSePuedanObtenerLasNoticiasDeUnUsuarioEnEspecifico(){
        List<Noticia> noticias=new ArrayList<>();
        noticias.add(noticiaMock);
        noticias.add(noticiaMockArte);
        noticias.add(noticiaMockDeportes);
        when(repositorioUsuarioMock.obtenerMisNoticias(usuarioMock.getIdUsuario())).thenReturn(noticias);
        List<Noticia> noticiasObtenidas=servicioUsuarioMock.obtenerNoticiasDeUnUsuario(usuarioMock.getIdUsuario());
        assertThat(noticiasObtenidas.size(),is(3));
    }

    @Test
    public void queSePuedaObtenerUnUsuarioPorSuID() throws Exception {
        when(repositorioUsuarioMock.obtenerUsuarioPorId(usuarioMock.getIdUsuario())).thenReturn(usuarioMock);
        Usuario usuarioObtenido=servicioUsuarioMock.obtenerUsuarioPorId(usuarioMock.getIdUsuario());
        assertThat(usuarioObtenido, is(notNullValue()));
    }

    @Test
    public void testObtenerUsuarioPorIdLanzaExcepcion() {
        assertThrows(Exception.class, () -> {
            servicioUsuarioMock.obtenerUsuarioPorId(usuarioMock.getIdUsuario());
        });
    }

    @Test
    public void crearUnUsuarioDebeUlizarRepositorioUsuarioParaGuardarSeguidos(){
        servicioUsuarioMock.agregarSeguido(usuarioMock, usuarioMock);
        verify(repositorioUsuarioMock, times(1)).crearSeguidos(any(Seguidos.class));
    }
    @Test
    public void queNoSePuedaGuardarUnQueSeQuieraSeguirSiYaLoSigue(){
        List<Seguidos> listaSeguidos = new ArrayList<>();
        listaSeguidos.add(seguidosMock);

        when(repositorioUsuarioMock.obtenerListaDeSeguidos(anyLong())).thenReturn(listaSeguidos);

        servicioUsuarioMock.agregarSeguido(seguidorMock, seguidoMock);

        verify(repositorioUsuarioMock, times(1)).obtenerListaDeSeguidos(anyLong());
        verify(repositorioUsuarioMock, times(0)).crearSeguidos(any(Seguidos.class));

    }

}
