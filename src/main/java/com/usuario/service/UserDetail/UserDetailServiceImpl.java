package com.usuario.service.UserDetail;

import com.usuario.entitie.ModeloUsuario;
import com.usuario.exceptions.RequestException;
import com.usuario.repository.UsuarioRepository;
import com.usuario.respuesta.Meta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    UsuarioRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<ModeloUsuario> userid = userRepository.findById(username);

        if ( userid == null ) {
            throw new RequestException(HttpStatus.UNAUTHORIZED,
                    new Meta(UUID.randomUUID().toString(),HttpStatus.UNAUTHORIZED.toString(),
                            423, "No se encontro el usuario" + username));
        }

        ModeloUsuario user = new ModeloUsuario();

        user.set_id(userid.get().get_id());
        user.setUsername(userid.get().getUsername());
        user.setCorreo(userid.get().getCorreo());
        user.setPassword(userid.get().getPassword());
        user.setRol(userid.get().getRol());

        return UserDetailImp.build(user);
    }
}
