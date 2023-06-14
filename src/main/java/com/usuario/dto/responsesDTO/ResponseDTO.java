package com.usuario.dto.responsesDTO;

import com.usuario.respuesta.Meta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO {

    private Object data;
    private Meta meta;

}
