package com.tallerwebi.presentacion;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.net.MPResultsResourcesPage;
import com.mercadopago.net.MPSearchRequest;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import com.tallerwebi.dominio.entidades.Notificacion;
import com.tallerwebi.dominio.entidades.Plan;
import com.tallerwebi.dominio.entidades.Rol;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.servicios.ServicioMercadoPago;
import com.tallerwebi.dominio.servicios.ServicioPlan;
import com.tallerwebi.dominio.servicios.ServicioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
public class ControladorPago {
    public final ServicioPlan servicioPlanes;
    public final ServicioMercadoPago servicioMercadoPago;
    public final ServicioUsuario servicioUsuario;
    @Autowired
    public ControladorPago (ServicioPlan servicioPlan,
                            ServicioMercadoPago servicioMercadoPago,
                            ServicioUsuario servicioUsuario){

        this.servicioPlanes = servicioPlan;
        this.servicioMercadoPago = servicioMercadoPago;
        this.servicioUsuario = servicioUsuario;
    }
    @GetMapping("/planes")
    public ModelAndView irAPagos(HttpSession session){
        ModelMap model=new ModelMap();

        Usuario usuario= (Usuario) session.getAttribute("sessionUsuarioLogueado");
        List<Plan> planes = servicioPlanes.traerPlanes();


        model.put("planes", planes);

        if(usuario != null){
            if(usuario.getRol() == (Rol.ADMIN)){
                return new ModelAndView("redirect:/home");
            }
        }else{
            return new ModelAndView("redirect:/login");
        }
        List<Notificacion> notificaciones = servicioUsuario.obtenerMisNotificacionesSinLeer(usuario.getIdUsuario());

        model.put("usuario",usuario);
        model.put("notificaciones", notificaciones.size());


        return new ModelAndView("suscripciones", model);
    }
    @GetMapping("/pagar/{idPlan}")
    public  String pagar(@PathVariable Long idPlan, HttpSession session){
        Usuario usuario= (Usuario) session.getAttribute("sessionUsuarioLogueado");
        if(usuario == null)
            return "redirect:/login";
        if(usuario.getRol() == (Rol.ADMIN))
            return "redirect:/home";
        Plan plan = servicioPlanes.buscarPlanPorID(idPlan);
        try {
            Map resultado = servicioMercadoPago.crearPago(usuario, plan);
            session.setAttribute("idPago", resultado.get("idPago"));
            session.setAttribute("plan", plan);
            return (String) resultado.get("url");
        } catch (MPException | MPApiException e) {
            e.printStackTrace();
            return "redirect:/error";
        }

    }
    @GetMapping("/pago/success")
    public ModelAndView guardarPago(HttpSession session, @RequestParam("status") String status) throws MPException, MPApiException {

        Usuario usuario= (Usuario) session.getAttribute("sessionUsuarioLogueado");
        Plan plan = (Plan) session.getAttribute("plan");


        if(usuario != null){
            if(usuario.getRol() == (Rol.ADMIN)){
                return new ModelAndView("redirect:/home");
            }
        }else{
            return new ModelAndView("redirect:/login");
        }
        if(Objects.equals(status, "approved")){
            servicioUsuario.guardarPlan(usuario.getIdUsuario(), plan);
            return new ModelAndView("redirect:/home");
        }


        return new ModelAndView("redirect:/error");
    }
}
