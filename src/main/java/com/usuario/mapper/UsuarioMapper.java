package com.usuario.mapper;

import com.usuario.dto.UsuarioDTO;
import com.usuario.dto.responsesDTO.ResponseListUsers;
import com.usuario.entitie.ModeloUsuario;
import lombok.Data;
import org.modelmapper.ModelMapper;

@Data
public class UsuarioMapper {

    private UsuarioMapper() {
        throw new IllegalStateException("NO EXISTE UN CONSTRUCTOR PARA LA CLASE");
    }
    public static final ModelMapper MAPPER = new ModelMapper();

    public static UsuarioDTO mapUsuarioDTO(ModeloUsuario usuario){
        return MAPPER.map(usuario, UsuarioDTO.class);
    }

    public static ResponseListUsers mapUsuarioListDTO(ModeloUsuario usuario){
        return MAPPER.map(usuario, ResponseListUsers.class);
    }

    public static ModeloUsuario mapUsuarioModel(UsuarioDTO usuarioDto){
        return MAPPER.map(usuarioDto, ModeloUsuario.class);
    }
}
