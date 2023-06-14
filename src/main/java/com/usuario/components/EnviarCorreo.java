package com.usuario.components;

import jakarta.mail.internet.MimeMessage;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.util.FileCopyUtils;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class EnviarCorreo {
    @Autowired
    JavaMailSender javaMailSender;

    public boolean enviarCodigoVerificacion(String destinatario, String codigo) {

        String asunto = "Código de verificación";
        String contenido = generarContenidoHTML(codigo);

        return enviarCorreo(destinatario, asunto, contenido);

    }

    private boolean enviarCorreo(String destinatario, String asunto, String contenido) {
        MimeMessage mensaje = javaMailSender.createMimeMessage();

        try {

            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true);
            helper.setFrom("Coppel Verify <ja.lizarraga18@info.uas.edu.mx>");
            helper.setTo(destinatario);
            helper.setSubject(asunto);
            helper.setText(contenido, true);
            javaMailSender.send(mensaje);

        }catch (jakarta.mail.MessagingException ex ){
            return false;
        }
       return true;
    }


    @Contract(pure = true)
    private @NotNull String generarContenidoHTML(String codigo) {

        String htmlTemplate = null;
        try {
            ClassPathResource resource = new ClassPathResource("templates/enviarCodigo.html");
            byte[] fileBytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
            htmlTemplate = new String(fileBytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (htmlTemplate != null) {
            return htmlTemplate.replace("{codigo}", codigo);
        }

        return "";
    }
}
