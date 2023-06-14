package com.usuario.service.UsuarioServiceImpl;
import com.usuario.dto.PasswordDTO;
import com.usuario.dto.UsuarioActualizarDTO;
import com.usuario.dto.responsesDTO.ResponseListUsers;
import com.usuario.dto.responsesDTO.ResponseOK;
import com.usuario.dto.responsesDTO.ResponsePaginadoDTO;
import com.usuario.dto.responsesDTO.ResponseUsers;
import com.usuario.dto.UsuarioDTO;
import com.usuario.entitie.ModeloCorreo;
import com.usuario.entitie.ModeloUsuario;
import com.usuario.exceptions.RequestException;
import com.usuario.mapper.UsuarioMapper;
import com.usuario.repository.CorreoRepository;
import com.usuario.repository.UsuarioRepository;
import com.usuario.respuesta.Error;
import com.usuario.respuesta.Meta;
import com.usuario.service.UsuarioService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    UsuarioRepository userRepository;
    @Autowired
    CorreoRepository correoRepository;
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";


    @Override
    public ResponseUsers<ResponseListUsers> consultarUsuarios(PageRequest pageRequest, @NotNull String usuario) {

        ResponseUsers<ResponseListUsers> resultado = new ResponseUsers<>();

        List<ResponseListUsers> users = new ArrayList<>();

        if(!usuario.equals("")){
            return this.consultarUsuario(pageRequest, usuario);
        }

        try {

            Page<ModeloUsuario> response = userRepository.findAll(pageRequest);

            response.forEach(list -> users.add(UsuarioMapper.mapUsuarioListDTO(list)));

            ResponsePaginadoDTO pages = new ResponsePaginadoDTO();
            pages.setTotalPaginas(response.getTotalPages());
            pages.setNumPagina(pageRequest.getPageNumber());
            pages.setTamPagina(pageRequest.getPageSize());

            resultado.setUsuarios(users);
            resultado.setPaginado(pages);

        } catch (Exception ex) {

            throw new RequestException(HttpStatus.INTERNAL_SERVER_ERROR,
                    new Meta(UUID.randomUUID().toString(),HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                            500, ex.getMessage()));
        }

        return resultado;
    }


    public ResponseUsers<ResponseListUsers> consultarUsuario(PageRequest pageRequest, String usuario) {

        ResponseUsers<ResponseListUsers> resultado = new ResponseUsers<>();

        try {

            //Consulta
            List<ResponseListUsers> users = new ArrayList<>();
            Page<ModeloUsuario> response = userRepository.findByUsernameContaining(usuario,pageRequest);

            response.forEach(list -> users.add(UsuarioMapper.mapUsuarioListDTO(list)));
            resultado.setUsuarios(users);

            //Paginado
            ResponsePaginadoDTO pages = new ResponsePaginadoDTO();
            pages.setTotalPaginas(response.getTotalPages());
            pages.setNumPagina(pageRequest.getPageNumber());
            pages.setTamPagina(pageRequest.getPageSize());
            resultado.setPaginado(pages);

        } catch (Exception ex) {

            throw new RequestException(HttpStatus.INTERNAL_SERVER_ERROR,
                    new Meta(UUID.randomUUID().toString(),HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                            500, ex.getMessage()));

        }

        return resultado;
    }


    @Override
    public ResponseOK registrarUsuario(@NotNull UsuarioDTO nuevoUsuario) {

        this.validarDatosUsuario(nuevoUsuario, "");

        try {

            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String passEncryp = passwordEncoder.encode(nuevoUsuario.getPassword());
            nuevoUsuario.setPassword(passEncryp);
            final ModeloUsuario user = UsuarioMapper.mapUsuarioModel(nuevoUsuario);
            UsuarioMapper.mapUsuarioDTO(this.userRepository.save(user));

        } catch (Exception ex) {

            throw new RequestException(HttpStatus.INTERNAL_SERVER_ERROR,
                    new Meta(UUID.randomUUID().toString(),HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                            500, ex.getMessage()));
        }

        return new ResponseOK("Reistro exitosamente el usuario: " + nuevoUsuario.getUsername());
    }

    @Override
    public ResponseOK actualizarUsuario(UsuarioActualizarDTO user) {

        final Optional<ModeloUsuario> userfound = this.userRepository.findById(user.get_id());

        UsuarioDTO user1 = new UsuarioDTO();
        user1.setUsername(user.getUsername());
        user1.setCorreo(user.getCorreo());
        user1.setNombres(user.getNombres());
        user1.setApellidos(user.getApellidos());

        this.validarDatosUsuario(user1, user.get_id());

        if (userfound.isEmpty()) {
            throw new RequestException(HttpStatus.NOT_FOUND,
                    new Meta(UUID.randomUUID().toString(),HttpStatus.NOT_FOUND.toString(),
                            400, Error.NOENCONTRO_REGISTRO));
        }

        try {

            userfound.get().setUsername(user.getUsername());
            userfound.get().setCorreo(user.getCorreo());
            userfound.get().setNombres(user.getNombres());
            userfound.get().setApellidos(user.getApellidos());

            this.userRepository.save(userfound.get());

        } catch (Exception ex) {

            throw new RequestException(HttpStatus.INTERNAL_SERVER_ERROR,
                    new Meta(UUID.randomUUID().toString(),HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                            500, ex.getMessage()));

        }

        return new ResponseOK("Se actualizo exitosamente el usuario: " + user.get_id());
    }

    @Override
    public ResponseOK actualizarPassword(PasswordDTO changePassword) {

        if (!changePassword.getPwdNueva().equals(changePassword.getPwdConfirmar())){
            throw new RequestException(HttpStatus.UNAUTHORIZED,
                    new Meta(UUID.randomUUID().toString(),HttpStatus.UNAUTHORIZED.toString(),
                            401,Error.PWD_NOCOINCIDEN));
        }

        ModeloUsuario user = this.userRepository.findByCorreo(changePassword.getCorreo());

        if(user == null){
            throw new RequestException(HttpStatus.UNPROCESSABLE_ENTITY,
                    new Meta(UUID.randomUUID().toString(),HttpStatus.UNPROCESSABLE_ENTITY.toString(),
                            422, "Correo no registrado"));
        }

        ModeloCorreo correo = correoRepository.findByCorreo(user.getCorreo());

        if( user.getCorreo() == null ) {
            throw new RequestException(HttpStatus.UNAUTHORIZED,
                    new Meta(UUID.randomUUID().toString(),HttpStatus.UNAUTHORIZED.toString(),
                            401,Error.CORREO_NOVERIFICADO));
        }

        Date fechaActual = new Date();

        Date fechaRegistro = correo.getFecha();

        long minutosTranscurridos = (fechaActual.getTime() - fechaRegistro.getTime()) / (60 * 1000);

        if (minutosTranscurridos > 6) {
            throw new RequestException(HttpStatus.UNPROCESSABLE_ENTITY,
                    new Meta(UUID.randomUUID().toString(),HttpStatus.UNPROCESSABLE_ENTITY.toString(),
                            422, "Favor de verificar nuevamente su correo"));
        }



        if( !correo.isVerificado() ) {
            throw new RequestException(HttpStatus.UNAUTHORIZED,
                    new Meta(UUID.randomUUID().toString(),HttpStatus.UNAUTHORIZED.toString(),
                            401,Error.CORREO_NOVERIFICADO));
        }

        try {

            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String passEncryp = passwordEncoder.encode(changePassword.getPwdNueva());
            user.setPassword(passEncryp);
            this.userRepository.save(user);

        } catch (Exception ex) {

            throw new RequestException(HttpStatus.INTERNAL_SERVER_ERROR,
                    new Meta(UUID.randomUUID().toString(),HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                            500, ex.getMessage()));
        }

        return new ResponseOK("Actualización exitosa: " + user.getCorreo());

    }

    public void validarDatosUsuario(@NotNull UsuarioDTO user, String idUser) {

        String idUsuario = "";
        List<String> mensajeDeError = new ArrayList<>();
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(user.getCorreo());

        if(!matcher.matches()){
            throw new RequestException(HttpStatus.UNPROCESSABLE_ENTITY,
                    new Meta(UUID.randomUUID().toString(),HttpStatus.UNPROCESSABLE_ENTITY.toString(),
                            422, Error.CORREO_INVALIDO));
        }

        ModeloCorreo correo = correoRepository.findByCorreo(user.getCorreo());

        if( correo == null ) {
            throw new RequestException(HttpStatus.UNAUTHORIZED,
                    new Meta(UUID.randomUUID().toString(),HttpStatus.UNAUTHORIZED.toString(),
                            401,Error.CORREO_NOVERIFICADO));
        }

        if( !correo.isVerificado() ) {
            throw new RequestException(HttpStatus.UNAUTHORIZED,
                    new Meta(UUID.randomUUID().toString(),HttpStatus.UNAUTHORIZED.toString(),
                            401,Error.CORREO_NOVERIFICADO));
        }

        ModeloUsuario email =  this.userRepository.findByCorreo(user.getCorreo());

        if ( email != null ){
            idUsuario = email.get_id();
            mensajeDeError.add(Error.CORREO_EXISTENTE);
        }

        ModeloUsuario username =  this.userRepository.findByUsername(user.getUsername());

        if ( username != null ){
            idUsuario = username.get_id();
            mensajeDeError.add(Error.USERNAME_EXISTENTE);
        }

        if(mensajeDeError.size() > 0 && !idUsuario.equals(idUser)) {
            throw new RequestException(HttpStatus.UNPROCESSABLE_ENTITY,
                    new Meta(UUID.randomUUID().toString(),HttpStatus.UNPROCESSABLE_ENTITY.toString(),
                            422, mensajeDeError.toString()));
        }
    }
}
