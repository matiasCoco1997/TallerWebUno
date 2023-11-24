package com.tallerwebi.dominio.servicios;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;


import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import com.tallerwebi.dominio.entidades.Plan;
import com.tallerwebi.dominio.entidades.Usuario;
//import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional

public class ServicioMercadoPagoImpl implements ServicioMercadoPago{

    @Override
    public Map crearPago(Usuario usuario, Plan plan) throws MPException, MPApiException {
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

        PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                .success("http://localhost:8080/sunn/pago/success")
                //.pending("https://www.seu-site/pending")
                //.failure("https://www.seu-site/failure")
                .build();

        OffsetDateTime fechaExpiracion = OffsetDateTime.now(ZoneId.systemDefault()).plusMinutes(10);


        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .backUrls(backUrls)
                .dateOfExpiration(fechaExpiracion)
                //.purpose("wallet_purchase") // Loguea a mercado pago antes de pagar
                .items(items)
                .build();

        PreferenceClient client = new PreferenceClient();
        Preference preference = client.create(preferenceRequest);

        Map<String, String> map = new HashMap<String, String>();

        map.put("url", "redirect:"+preference.getInitPoint());
        map.put("idPago", preference.getId());
        return map;
    }

}
