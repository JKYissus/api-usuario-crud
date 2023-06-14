package com.usuario.mapper;

import com.usuario.dto.CorreoDTO;
import com.usuario.entitie.ModeloCorreo;
import org.modelmapper.ModelMapper;

public class CorreoMapper {

    private CorreoMapper() {
        throw new IllegalStateException("NO EXISTE UN CONSTRUCTOR PARA LA CLASE");
    }
    public static final ModelMapper MAPPER = new ModelMapper();

    public static CorreoDTO mapCorreoDTO(ModeloCorreo correo){
        return MAPPER.map(correo, CorreoDTO.class);
    }

}
