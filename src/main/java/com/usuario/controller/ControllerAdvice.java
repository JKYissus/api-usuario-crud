package com.usuario.controller;
import com.usuario.dto.ErrorDTO;
import com.usuario.exceptions.RequestException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    @ExceptionHandler(value = RequestException.class)
    public ResponseEntity<ErrorDTO> requestExceptionHandler(@NotNull RequestException ex) {
        logger.error(ex.toString());
        return new ResponseEntity<>(ErrorDTO.builder().meta(ex.getMeta()).build(), ex.getStatus());
    }
}
