package com.tallerwebi.presentacion;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.texttospeech.v1.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;

@RestController
public class ControladorTextToSpeech {

    @PostMapping("/texto-a-audio")
    public ResponseEntity<byte[]> convertirTextoAAudio(@RequestBody String texto) {
        try {
            // Cargar las credenciales
            InputStream credentialsStream = getClass().getClassLoader().getResourceAsStream("credentials.json");
            GoogleCredentials credentials = GoogleCredentials.fromStream(credentialsStream);

            TextToSpeechSettings settings = TextToSpeechSettings.newBuilder()
                    .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                    .build();

            try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create(settings)) {

                SynthesisInput input = SynthesisInput.newBuilder().setText(texto).build();

                VoiceSelectionParams voice = VoiceSelectionParams.newBuilder()
                        .setLanguageCode("es-US") // es-ES España, es-US(español Estados Unidos)
                        .setName("es-US-Wavenet-C")
                        .build();


                AudioConfig audioConfig = AudioConfig.newBuilder()
                        .setAudioEncoding(AudioEncoding.LINEAR16)
                        .setSpeakingRate(1.) // velocidad
                        .build();


                SynthesizeSpeechResponse response = textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);

                byte[] audioData = response.getAudioContent().toByteArray();

                return ResponseEntity.ok().body(audioData);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Error al procesar la solicitud".getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error al cargar el archivo de credenciales me quiero matar".getBytes());
        }
    }
}
