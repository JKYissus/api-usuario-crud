package com.usuario.dto.responsesDTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Data
public class ResponseUsers<T> {

    private List<T> usuarios;
    private Object paginado;

}
