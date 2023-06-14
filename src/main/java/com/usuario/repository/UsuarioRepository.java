package com.usuario.repository;

import com.usuario.entitie.ModeloUsuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends MongoRepository<ModeloUsuario, String> {
    @Query("{'username': {$regex: ?0, $options: 'i'}}")
    Page<ModeloUsuario> findByUsernameContaining(@Param("usuario") String usuario, PageRequest pageRequest);
    ModeloUsuario findByCorreo(String correo);
    ModeloUsuario findByUsername(String username);
}
