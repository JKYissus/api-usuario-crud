package com.usuario.dto;

import lombok.Data;
import org.bson.types.ObjectId;

import java.util.Date;

@Data
public class CorreoDTO {
    private ObjectId id;
    private String correo;
    private String codigo;
    private boolean verificado;
    private Date fecha;
}
