package com.usuario.dto.responsesDTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ResponseValidarCorreo {

   private boolean correoValido;
   private String correo;

}
