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
		//verify(sessionMock, times(0)).setAttribute("ROL", "ADMIN"); falta hacer lo de los planes pagos
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

	@Test
	public void queSeCierreLaSesionYRedirijaAlLogin(){

		//ejecucion
		ModelAndView model = controladorLogin.cerrarSesion(sessionMock);

		//validacion
		verify(sessionMock).invalidate();
		assertThat(model.getViewName(), equalToIgnoringCase("redirect:/login"));
	}

}
