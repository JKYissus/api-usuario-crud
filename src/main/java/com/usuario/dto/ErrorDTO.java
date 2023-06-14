package com.usuario.dto;

import com.usuario.respuesta.Meta;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorDTO {
    private Meta meta;
}
