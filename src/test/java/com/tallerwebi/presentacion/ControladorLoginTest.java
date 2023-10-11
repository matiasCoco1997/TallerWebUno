package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.Categoria;
import com.tallerwebi.dominio.entidades.Noticia;
import com.tallerwebi.dominio.excepcion.CampoVacio;
import com.tallerwebi.dominio.excepcion.FormatoDeImagenIncorrecto;
import com.tallerwebi.dominio.excepcion.TamanioDeArchivoSuperiorALoPermitido;
import com.tallerwebi.dominio.servicios.ServicioHome;
import com.tallerwebi.dominio.servicios.ServicioLogin;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.Mockito.*;

public class ControladorLoginTest {

	private ControladorLogin controladorLogin;
	private Usuario usuarioMock;
	private DatosLogin datosLoginMock;
	private HttpServletRequest requestMock;
	private HttpSession sessionMock;
	private ServicioLogin servicioLoginMock;
	private MultipartFile imgMock;


	@BeforeEach
	public void init(){
		datosLoginMock = new DatosLogin("dami@unlam.com", "123");
		usuarioMock = mock(Usuario.class);
		imgMock = mock(MultipartFile.class);
		when(usuarioMock.getNombre()).thenReturn("nombre");
		when(usuarioMock.getApellido()).thenReturn("apellido");
		when(usuarioMock.getPais()).thenReturn("pais");
		when(usuarioMock.getCiudad()).thenReturn("ciudad");
		when(usuarioMock.getActivo()).thenReturn(true);
		//when(usuarioMock.getFechaDeNacimiento()).thenReturn(new Date(2018,12,9));
		when(usuarioMock.getEmail()).thenReturn("dami@unlam.com");
		when(usuarioMock.getPassword()).thenReturn("password");
		when(usuarioMock.getFotoPerfil()).thenReturn("fotoPerfil");
		requestMock = mock(HttpServletRequest.class);
		sessionMock = mock(HttpSession.class);
		servicioLoginMock = mock(ServicioLogin.class);
		controladorLogin = new ControladorLogin(servicioLoginMock);
	}

	@Test
	public void loginConUsuarioYPasswordInorrectosDeberiaLlevarALoginNuevamente(){
		// preparacion
		when(servicioLoginMock.consultarUsuario(anyString(), anyString())).thenReturn(null);

		// ejecucion
		ModelAndView modelAndView = controladorLogin.validarLogin(datosLoginMock, requestMock);

		// validacion
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("login"));
		assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("Usuario o clave incorrecta"));
		//verify(sessionMock, times(0)).setAttribute("ROL", "ADMIN");
	}
	
	@Test
	public void loginConUsuarioYPasswordCorrectosDeberiaLLevarAHome(){
		// preparacion
		when(requestMock.getSession()).thenReturn(sessionMock);
		when(servicioLoginMock.consultarUsuario(anyString(), anyString())).thenReturn(usuarioMock);

		// ejecucion
		ModelAndView modelAndView = controladorLogin.validarLogin(datosLoginMock, requestMock);
		
		// validacion
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/home"));
	}

	@Test
	public void registrameSiUsuarioNoExisteDeberiaCrearUsuarioYVolverAlLogin() throws UsuarioExistente, CampoVacio, IOException, FormatoDeImagenIncorrecto, TamanioDeArchivoSuperiorALoPermitido {

		// ejecucion
		ModelAndView modelAndView = controladorLogin.registrarme(usuarioMock, imgMock);

		// validacion
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
		verify(servicioLoginMock, times(1)).registrar(usuarioMock, imgMock);
	}

	@Test
	public void registrarmeSiUsuarioExisteDeberiaVolverAFormularioYMostrarError() throws UsuarioExistente, CampoVacio, IOException, TamanioDeArchivoSuperiorALoPermitido, FormatoDeImagenIncorrecto {
		// preparacion
		doThrow(UsuarioExistente.class).when(servicioLoginMock).registrar(usuarioMock, imgMock);

		// ejecucion
		ModelAndView modelAndView = controladorLogin.registrarme(usuarioMock, imgMock);

		// validacion
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("registro"));
		assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("El email ya esta en uso."));
	}

	@Test
	public void registrarmeSiDejoCamposVaciosDeberiaDevolverUnaAlertaDeCamposVacios() throws UsuarioExistente, CampoVacio, IOException, TamanioDeArchivoSuperiorALoPermitido, FormatoDeImagenIncorrecto {
		// preparacion
		doThrow(CampoVacio.class).when(servicioLoginMock).registrar(usuarioMock, imgMock);

		// ejecucion
		ModelAndView modelAndView = controladorLogin.registrarme(usuarioMock, imgMock);

		// validacion
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("registro"));
		assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("Debe completar todos los campos."));
	}

	@Test
	public void registrarmeSiSuboUnaImagenPesadaDeberiaRetornarUnErrorDandoAviso() throws UsuarioExistente, CampoVacio, IOException, TamanioDeArchivoSuperiorALoPermitido, FormatoDeImagenIncorrecto {
		// preparacion
		doThrow(TamanioDeArchivoSuperiorALoPermitido.class).when(servicioLoginMock).registrar(usuarioMock, imgMock);

		// ejecucion
		ModelAndView modelAndView = controladorLogin.registrarme(usuarioMock, imgMock);

		// validacion
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("registro"));
		assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("La imagen ingresada demasiado pesada."));
	}

	@Test
	public void registrarmeConUnTipoDeImagenQueNoEstaPermitidoDeberiaRetornarUnErrorAvisando() throws UsuarioExistente, CampoVacio, IOException, TamanioDeArchivoSuperiorALoPermitido, FormatoDeImagenIncorrecto {
		// preparacion
		doThrow(FormatoDeImagenIncorrecto.class).when(servicioLoginMock).registrar(usuarioMock, imgMock);

		// ejecucion
		ModelAndView modelAndView = controladorLogin.registrarme(usuarioMock, imgMock);

		// validacion
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("registro"));
		assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("El tipo de archivo ingresado no esta permitido."));
	}

	@Test
	public void errorEnRegistrarmeDeberiaVolverAFormularioYMostrarError() throws UsuarioExistente, CampoVacio, IOException, TamanioDeArchivoSuperiorALoPermitido, FormatoDeImagenIncorrecto {
		// preparacion
		doThrow(RuntimeException.class).when(servicioLoginMock).registrar(usuarioMock, imgMock);

		// ejecucion
		ModelAndView modelAndView = controladorLogin.registrarme(usuarioMock, imgMock);

		// validacion
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("registro"));
		assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("Error al registrar el nuevo usuario"));
	}

    public static class ControladorHomeTest {
        private ControladorHome controladorHome;
        private ServicioHome servicioHomeMock;

        private Noticia noticiaMock;
        private HttpServletRequest requestMock;
        private HttpSession sessionMock;
        private Categoria categoriaMock;
        private Usuario usuarioMock;

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
            requestMock = mock(HttpServletRequest.class);
            sessionMock = mock(HttpSession.class);
            servicioHomeMock = mock(ServicioHome.class);
            controladorHome = new ControladorHome(servicioHomeMock);
        }
        @Test
        public void queSePuedaObtenerLaVistaDelHome(){

        }
        @Test
        public void queAlListarDosNoticiasSeCargueElHome() {

            List<Noticia> noticias = new ArrayList<>();

            noticias.add(noticiaMock);
            noticias.add(noticiaMock);

            when(servicioHomeMock.listarNoticias()).thenReturn(noticias);

            // ejecucion
            ModelAndView modelAndView = controladorHome.home(sessionMock);
            List<Noticia> noticiasEnModelo = (List<Noticia>) modelAndView.getModel().get("noticias");

            // validacion
            assertThat(noticiasEnModelo.size(), equalTo(2));
            assertThat(modelAndView.getViewName(), equalToIgnoringCase("home-vista"));
        }

        @Test
        public void queAlListarDosCategoriasSeCargueElHome(){
            List<Categoria> categorias=new ArrayList<>();
            categorias.add(categoriaMock);
            categorias.add(categoriaMock);

            when(servicioHomeMock.obtenerCategorias()).thenReturn(categorias);

            ModelAndView modelAndView= controladorHome.home(sessionMock);
            List<Categoria> categoriasEnModelo= (List<Categoria>) modelAndView.getModel().get("categorias");

            assertThat(categoriasEnModelo.size(),equalTo(2));
        }

        @Test
        public void queAlListarDosUsuariosSeCargueELHome(){
            List<Usuario> usuarios= new ArrayList<>();
            usuarios.add(usuarioMock);
            usuarios.add(usuarioMock);

            when(servicioHomeMock.listarUsuarios()).thenReturn(usuarios);

            ModelAndView modelAndView= controladorHome.home(sessionMock);
            List<Usuario> usuariosEnModelo= (List<Usuario>) modelAndView.getModel().get("usuarios");

            assertThat(usuariosEnModelo.size(),equalTo(2));
        }

        @Test
        public void queHayaUnUsuarioEnEspecificoEnElModelo(){
            when(sessionMock.getAttribute("sessionUsuarioLogueado")).thenReturn(usuarioMock);
            ModelAndView modelAndView=controladorHome.home(sessionMock);
            Usuario usuarioEnModelo= (Usuario) modelAndView.getModel().get("usuario");
            assertThat(usuarioEnModelo,notNullValue());
        }
    }
}
