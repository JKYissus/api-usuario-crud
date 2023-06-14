package com.usuario.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class UsuarioDTO {
    @JsonProperty(value = "id")
    private String _id;
    private String username;
    private String password;
    private String correo;
    private String nombres;
    private String apellidos;
    private String rol;
}
