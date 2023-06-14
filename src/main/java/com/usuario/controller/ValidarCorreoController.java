package com.usuario.controller;

import com.usuario.dto.RequestValidarCorreo;
import com.usuario.dto.responsesDTO.ResponseDTO;
import com.usuario.respuesta.Meta;
import com.usuario.service.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/email")
public class ValidarCorreoController {

    @Autowired
    EmailService emailService;

    final Meta meta = new Meta(UUID.randomUUID().toString(), "OK", 200, "success");

    @GetMapping("/validar")
    public @ResponseBody ResponseDTO consultarUsuarios(
            @RequestParam(value = "codigo", defaultValue = "0")  String codigoVerificador,
            @RequestParam(value = "correo", defaultValue = "x")  String correo
    ){
        //Agregar logs
        return new ResponseDTO(emailService.validarCodigo(codigoVerificador, correo), meta);
    }

    @PostMapping("/enviar")
    public ResponseDTO enviarGmail(@RequestBody RequestValidarCorreo correo) {

        return new ResponseDTO(emailService.enviarCodigoVerificacion(correo), meta);
    }

}
