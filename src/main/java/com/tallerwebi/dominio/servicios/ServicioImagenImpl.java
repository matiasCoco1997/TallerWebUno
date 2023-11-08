package com.tallerwebi.dominio.servicios;

import com.tallerwebi.dominio.entidades.Noticia;
import com.tallerwebi.dominio.entidades.Usuario;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
@Service
public class ServicioImagenImpl implements ServicioImagen{
    @Override
    public byte[] cargarImagenComoBytes(String rutaImagen) throws IOException {
        Path imagePath = Paths.get("src/main/webapp/resources/core", rutaImagen);
        File imageFile = imagePath.toFile();

        if (!verificarExistenciaImagen(imageFile)) {
            return null;
        }

        BufferedImage originalImage = cargarImagen(imageFile);

        if (originalImage == null) {
            return null;
        }

        BufferedImage imagenRedimensionada = redimensionarImagen(originalImage);
        byte[] imageBytes = codificarImagenBase64(imagenRedimensionada);

        return imageBytes;
    }

    @Override
    public boolean verificarExistenciaImagen(File imageFile) {
        if (!imageFile.exists()) {
            System.out.println("El archivo de la imagen no existe: " + imageFile.toString());
            return false;
        }
        return true;
    }

    @Override
    public BufferedImage cargarImagen(File imageFile) {
        try (InputStream inputStream = new FileInputStream(imageFile)) {
            return ImageIO.read(inputStream);
        } catch (IOException e) {
            System.out.println("Error al cargar la imagen: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public BufferedImage redimensionarImagen(BufferedImage originalImage) {
        int newWidth = originalImage.getWidth() / 2;
        int newHeight = originalImage.getHeight() / 2;
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, originalImage.getType());
        resizedImage.getGraphics().drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        return resizedImage;
    }

    @Override
    public byte[] codificarImagenBase64(BufferedImage imagen) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            if (!ImageIO.write(imagen, "png", outputStream)) {
                System.out.println("No se pudo codificar la imagen en base64.");
                return null;
            }
            return outputStream.toByteArray();
        } catch (IOException e) {
            System.out.println("Error al codificar la imagen en base64: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
