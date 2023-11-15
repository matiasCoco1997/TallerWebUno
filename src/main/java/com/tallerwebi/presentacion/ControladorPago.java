package com.tallerwebi.presentacion;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import com.tallerwebi.dominio.entidades.Plan;
import com.tallerwebi.dominio.entidades.Rol;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.servicios.ServicioPlan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ControladorPago {
    public final ServicioPlan servicioPlanes;
    @Autowired
    public ControladorPago (ServicioPlan servicioPlan){
        this.servicioPlanes = servicioPlan;
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

        model.put("usuario",usuario);

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
            // Configura la api de MP
            // Usuasrio suscriptor: TESTUSER1908381680 Contraseña: 7x1C8QSd9V
            // Usuario dueño de token de MP: TESTUSER1704554571 Contraseña: 03YIoU4QBM
            MercadoPagoConfig.setAccessToken("TEST-6077136612687865-111518-3da7be54da23bcf12206b21bdeac0099-1551403508");

            // Crea un ítem de la preferencia
            PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                    .title("Plan de Suscripcion: " + plan.getDescripcion().toUpperCase())
                    .description("Plan " + plan.getDescripcion())
                    .pictureUrl("https://upload.wikimedia.org/wikipedia/commons/thumb/3/3a/Sunn_O%29%29%29_%28Logo%29.png/800px-Sunn_O%29%29%29_%28Logo%29.png")
                    .categoryId("plan")
                    .quantity(1)
                    .unitPrice(BigDecimal.valueOf(plan.getPrecio()))
                    .build();

            List<PreferenceItemRequest> items = new ArrayList<>();
            items.add(itemRequest);

            OffsetDateTime fechaExpiracion = OffsetDateTime.now(ZoneId.systemDefault()).plusMinutes(10);


            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .dateOfExpiration(fechaExpiracion)
                    //.purpose("wallet_purchase") // Loguea a mercado pago antes de pagar
                    .items(items)
                    .build();

            PreferenceClient client = new PreferenceClient();
            Preference preference = client.create(preferenceRequest);

            return "redirect:"+preference.getInitPoint();
        } catch (MPException | MPApiException e) {
            e.printStackTrace();
            return "redirect:/error";
        }

    }
}
