package com.usuario.components.JWT;

import com.usuario.dto.UsuarioDTO;

import io.jsonwebtoken.*;
import io.jsonwebtoken.Jwts;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

@Component
public class JwtUtils {
    @Value("${jwt.secret.key}")
    String secret;

    public UsuarioDTO validarToken(String token) {

        byte[] jwtSecretBytes = secret.getBytes();
        Key secretKey = new SecretKeySpec(jwtSecretBytes, "HmacSHA256");

        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

        String username = claims.get("username", String.class);
        String correo = claims.get("correo", String.class);
        String rol = claims.get("rol", String.class);
        String id = claims.get("id", String.class);


        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setUsername(username);
        usuario.setCorreo(correo);
        usuario.setRol(rol);
        usuario.set_id(id);

        return usuario;
    }
}
