package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.Categoria;
import com.tallerwebi.dominio.entidades.ListaReproduccion;
import com.tallerwebi.dominio.entidades.Noticia;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.servicios.ServicioHome;
import com.tallerwebi.dominio.servicios.ServicioListaRep;
import com.tallerwebi.dominio.servicios.ServicioNoticia;
import com.tallerwebi.dominio.servicios.ServicioUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ControladorListaRepTest {

    private ControladorListaRep controladorListaRep;
    private ServicioListaRep servicioListaRepMock;
    private ServicioNoticia servicioNoticiaMock;

    private Noticia noticiaMock;
    private HttpServletRequest requestMock;
    private HttpSession sessionMock;
    private Categoria categoriaMock;
    private Usuario usuarioMock;
    private ListaReproduccion listaMock;
    private ServicioUsuario servicioUsuarioMock;

    @BeforeEach
    public void init(){
        noticiaMock = mock(Noticia.class);
        when(noticiaMock.getIdNoticia()).thenReturn(1L);
        when(noticiaMock.getTitulo()).thenReturn("titulo");
        when(noticiaMock.getCategoria()).thenReturn("categoria");

        categoriaMock=mock(Categoria.class);
        when(categoriaMock.getIdCategoria()).thenReturn(1);
        when(categoriaMock.getDescripcion()).thenReturn("Deportes");

        usuarioMock=mock(Usuario.class);
        when(usuarioMock.getIdUsuario()).thenReturn(1L);

        listaMock=mock(ListaReproduccion.class);

        requestMock = mock(HttpServletRequest.class);

        sessionMock = mock(HttpSession.class);

        servicioListaRepMock = mock(ServicioListaRep.class);
        servicioNoticiaMock = mock(ServicioNoticia.class);
        servicioUsuarioMock = mock(ServicioUsuario.class);

        controladorListaRep = new ControladorListaRep(servicioListaRepMock,servicioNoticiaMock, servicioUsuarioMock);
    }

    @Test
    public void queRetorneLaVistaDeListasDeReproduccion(){
        List<ListaReproduccion> listaReproduccion = new ArrayList<>();
        when(servicioListaRepMock.obtenerListaReproduccionDelUsuarioLogueado(usuarioMock.getIdUsuario())).thenReturn(listaReproduccion);
        when(sessionMock.getAttribute("sessionUsuarioLogueado")).thenReturn(usuarioMock);
        String modelAndView= controladorListaRep.obtenerListas(sessionMock).getViewName();
        assertThat(modelAndView,is("listasReproduccion"));
    }
    @Test
    public void queAlListarDosListasDeReproduccionSeCargueEnElHome(){
        List<ListaReproduccion> listas= new ArrayList<>();
        listas.add(listaMock);
        listas.add(listaMock);

        when(servicioListaRepMock.obtenerListaReproduccionDelUsuarioLogueado(usuarioMock.getIdUsuario())).thenReturn(listas);
        when(sessionMock.getAttribute("sessionUsuarioLogueado")).thenReturn(usuarioMock);

        List<ListaReproduccion> listasObtenidas= (List<ListaReproduccion>) controladorListaRep.obtenerListas(sessionMock).getModel().get("listaReproduccion");
        assertThat(listasObtenidas.size(),equalTo(2));
    }
    @Test
    public void siSeCargaCorrectamenteUnaNoticiaDebeRedirigirmeAMiListasDeReproduccion(){
        ResponseEntity<String> viewName = controladorListaRep.agregarNoticiaALista(usuarioMock.getIdUsuario(),sessionMock);
        assertThat(viewName,is("Noticia agregada correctamente!"));
    }
}
