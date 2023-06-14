package com.usuario.dto.responsesDTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ResponsePaginadoDTO {

    private int totalPaginas;
    private int numPagina;
    private int tamPagina;

}
