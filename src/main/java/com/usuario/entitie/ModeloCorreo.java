package com.usuario.entitie;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@Data
@Document(collection = "correo")
public class ModeloCorreo {
    @Id
    private ObjectId id;
    private String correo;
    private String codigo;
    boolean verificado;
    private Date fecha;


    public boolean isVerificado() {
        return verificado;
    }
}
