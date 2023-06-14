package com.usuario.controller;

import com.usuario.dto.PasswordDTO;
import com.usuario.dto.UsuarioActualizarDTO;
import com.usuario.dto.responsesDTO.ResponseDTO;
import com.usuario.dto.UsuarioDTO;
import com.usuario.dto.responsesDTO.ResponseListUsers;
import com.usuario.dto.responsesDTO.ResponseUsers;
import com.usuario.exceptions.RequestException;
import com.usuario.respuesta.Error;
import com.usuario.respuesta.Meta;
import com.usuario.service.UsuarioService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.jetbrains.annotations.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {
    @Autowired
    UsuarioService usuarioService;
    final Meta meta = new Meta(UUID.randomUUID().toString(), "OK", 200, "success");

    public void validData(UsuarioDTO user){

        if ( user == null ){
            throw new RequestException(HttpStatus.BAD_REQUEST, new Meta(UUID.randomUUID().toString(), "BAD REQUEST", 400, Error.ERROR_INESPERADO));
        }

        if ( user.getNombres() == null ||
                user.getApellidos() == null ||
                user.getCorreo() == null  ||
                user.getRol() == null       ||
                user.getUsername() == null  ||
                user.getPassword() == null ) {

            throw new RequestException(HttpStatus.BAD_REQUEST, new Meta(UUID.randomUUID().toString(), "BAD REQUEST", 400, Error.ERROR_INESPERADO));

        }

        if ( user.getNombres().isEmpty() ||
                user.getApellidos().isEmpty() ||
                user.getCorreo().isEmpty()  ||
                user.getRol().isEmpty()       ||
                user.getUsername().isEmpty()  ||
                user.getPassword().isEmpty()  ||
                user.getPassword().length() < 8 ) {

            throw new RequestException(HttpStatus.BAD_REQUEST, new Meta(UUID.randomUUID().toString(), "BAD REQUEST", 400, Error.ERROR_INESPERADO));

        }
    }


    public void validDataUpdate(UsuarioActualizarDTO user){

        if ( user == null ){
            throw new RequestException(HttpStatus.BAD_REQUEST, new Meta(UUID.randomUUID().toString(), "BAD REQUEST", 400, Error.ERROR_INESPERADO));
        }

        if ( user.getNombres() == null ||
                user.getApellidos() == null ||
                user.getCorreo() == null  ||
                user.getUsername() == null ) {

            throw new RequestException(HttpStatus.BAD_REQUEST, new Meta(UUID.randomUUID().toString(), "BAD REQUEST", 400, Error.ERROR_INESPERADO));

        }

        if ( user.getNombres().isEmpty() ||
                user.getApellidos().isEmpty() ||
                user.getCorreo().isEmpty()  ||
                user.getUsername().isEmpty()) {

            throw new RequestException(HttpStatus.BAD_REQUEST, new Meta(UUID.randomUUID().toString(), "BAD REQUEST", 400, Error.ERROR_INESPERADO));

        }
    }

    @GetMapping("")
    public @ResponseBody ResponseDTO consultarUsuarios(HttpServletRequest request,
            @RequestParam(value = "numpagina", defaultValue = "0")  int numeroPagina,
            @RequestParam(value = "numtampagina", defaultValue = "5") int tamanoPagina,
            @RequestParam(value = "usuario", defaultValue = "") String usuario
    ){
        PageRequest pageRequest = PageRequest.of(numeroPagina, tamanoPagina);
        ResponseUsers<ResponseListUsers> response = usuarioService.consultarUsuarios(pageRequest, usuario);
        return new ResponseDTO(response, meta);
    }

    @PostMapping("/registrar")
    public ResponseDTO registrarUsuario(@Valid @RequestBody UsuarioDTO user){

        this.validData(user);
        return new ResponseDTO(usuarioService.registrarUsuario(user), meta);
    }

    @PutMapping("")
    public ResponseDTO actualizarUsuario(@Valid @RequestBody UsuarioActualizarDTO user){

        this.validDataUpdate(user);

        return new ResponseDTO(usuarioService.actualizarUsuario(user), meta);
    }

    @PutMapping("/cambiarcontraseña")
    public ResponseDTO actualizarPassword(@Valid @RequestBody PasswordDTO user){


        return new ResponseDTO(usuarioService.actualizarPassword(user), meta);
    }
}
