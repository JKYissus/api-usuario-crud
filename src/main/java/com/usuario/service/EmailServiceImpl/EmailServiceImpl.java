package com.usuario.service.EmailServiceImpl;

import com.usuario.components.EnviarCorreo;
import com.usuario.dto.CorreoDTO;
import com.usuario.dto.RequestValidarCorreo;
import com.usuario.dto.responsesDTO.ResponseValidarCorreo;
import com.usuario.entitie.ModeloCorreo;
import com.usuario.exceptions.RequestException;
import com.usuario.mapper.CorreoMapper;
import com.usuario.repository.CorreoRepository;
import com.usuario.respuesta.Error;
import com.usuario.respuesta.Meta;
import com.usuario.service.EmailService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    EnviarCorreo sendCode;

    @Autowired
    CorreoRepository correoRepository;

    @Override
    public ResponseValidarCorreo validarCodigo(String codigo, String email) {

        ResponseValidarCorreo valido = new ResponseValidarCorreo();

        long tiempoValido = 6L;
        CorreoDTO correo = CorreoMapper.mapCorreoDTO(this.correoRepository.findByCorreo(email));
        ModeloCorreo correoConfirmado = this.correoRepository.findByCorreo(correo.getCorreo());

        if(!correo.getCodigo().equals(codigo)){
            correoConfirmado.setVerificado(false);
            throw new RequestException(HttpStatus.UNPROCESSABLE_ENTITY,
                    new Meta(UUID.randomUUID().toString(),HttpStatus.UNPROCESSABLE_ENTITY.toString(),
                            422,"Codigo invalido"));
        }

        try {

            Date fechaHoraActual = new Date();
            Date fechaRegistrada = correo.getFecha();

            // Calcular la diferencia en milisegundos
            long diferenciaMilisegundos = fechaHoraActual.getTime() - fechaRegistrada.getTime();

            // Convertir la diferencia de milisegundos a minutos
            long diferenciaMinutos = diferenciaMilisegundos / (60 * 1000);

            valido.setCorreo(email);

            valido.setCorreoValido(false);
            if(tiempoValido >= diferenciaMinutos){
                valido.setCorreoValido(true);
                correoConfirmado.setVerificado(true);
            }

            correoRepository.save(correoConfirmado);

        } catch (Exception ex) {
            throw new RequestException(HttpStatus.INTERNAL_SERVER_ERROR,
                    new Meta(UUID.randomUUID().toString(),HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                            500,Error.ERROR_INESPERADO));
        }

        return valido;
    }

    @Override
    public String enviarCodigoVerificacion(@NotNull RequestValidarCorreo correo) {

        String codigo;
        boolean seEnvio;
        try {
            // Obtener la fecha y hora actual
            Date fechaHoraActual = new Date();

            String destinatario = correo.getCorreo();
            codigo = this.generarCodigoVerificacion();

            seEnvio = sendCode.enviarCodigoVerificacion(destinatario, codigo);

            if(!seEnvio){
                throw new RequestException(HttpStatus.INTERNAL_SERVER_ERROR,
                        new Meta(UUID.randomUUID().toString(),HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                                500,Error.ERROR_INESPERADO));
            }

            ModeloCorreo email = this.correoRepository.findByCorreo(correo.getCorreo());

            if(email == null){

                email = new ModeloCorreo();
                email.setCorreo(destinatario);

            }

            email.setCodigo(codigo);
            email.setFecha(fechaHoraActual);
            email.setVerificado(false);

            correoRepository.save(email);

        } catch (Exception ex) {
            throw new RequestException(HttpStatus.INTERNAL_SERVER_ERROR,
                    new Meta(UUID.randomUUID().toString(),HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                            500,Error.ERROR_INESPERADO));
        }

        return "Correo enviado exitosamente!!";
    }

    @Override
    public String generarCodigoVerificacion() {
        String codigo;
        try {
            UUID uuid = UUID.randomUUID();
            codigo = uuid.toString().replaceAll("-", "").substring(0, 6);
        } catch (Exception ex) {
            throw new RequestException(HttpStatus.INTERNAL_SERVER_ERROR,
                    new Meta(UUID.randomUUID().toString(),HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                            500,Error.ERROR_INESPERADO));
        }

        return codigo;
    }
}
