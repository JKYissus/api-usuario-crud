package com.usuario.service;

import com.usuario.dto.RequestValidarCorreo;
import com.usuario.dto.responsesDTO.ResponseValidarCorreo;
import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    ResponseValidarCorreo validarCodigo(String codigo, String correo);
    String enviarCodigoVerificacion(RequestValidarCorreo correo);
    String generarCodigoVerificacion();


}
