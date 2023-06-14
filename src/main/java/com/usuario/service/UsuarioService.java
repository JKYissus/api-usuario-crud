package com.usuario.service;

import com.usuario.dto.PasswordDTO;
import com.usuario.dto.UsuarioActualizarDTO;
import com.usuario.dto.responsesDTO.ResponseListUsers;
import com.usuario.dto.responsesDTO.ResponseOK;
import com.usuario.dto.responsesDTO.ResponseUsers;
import com.usuario.dto.UsuarioDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


@Service
public interface UsuarioService {
     //Consult
     ResponseUsers<ResponseListUsers> consultarUsuarios (PageRequest pageRequest, String usuario);
     //Register
     ResponseOK registrarUsuario (UsuarioDTO nuevoUsuario);
     //Update
     ResponseOK actualizarUsuario (UsuarioActualizarDTO user);

     ResponseOK actualizarPassword (PasswordDTO changePassword);
     //Delete
}
