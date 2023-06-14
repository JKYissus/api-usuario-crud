package com.usuario.dto.responsesDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ResponseListUsers {

    @JsonProperty(value = "id")
    private String _id;
    private String username;
    private String correo;
    private String nombres;
    private String apellidos;
    private String rol;
}
