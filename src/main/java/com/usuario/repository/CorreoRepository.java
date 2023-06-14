package com.usuario.repository;

import com.usuario.entitie.ModeloCorreo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CorreoRepository extends MongoRepository <ModeloCorreo, String> {

    ModeloCorreo findByCorreo(String correo);

}
