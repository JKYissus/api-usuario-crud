package com.usuario.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class PasswordDTO {

    private String correo;
    private String pwdNueva;
    private String pwdConfirmar;

}
