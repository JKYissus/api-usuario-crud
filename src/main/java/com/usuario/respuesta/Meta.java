package com.usuario.respuesta;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

import java.time.LocalDateTime;
@Data public class Meta {

    private String transactionID;

    private String status;

    private int statusCode;

    private String timestamp;

    @JsonInclude(value = Include.NON_NULL)
    private String devMessage;

    @JsonInclude(value = Include.NON_NULL)
    private String message;

    public Meta(String transactionID, String status, int statusCode, String message) {
        this.transactionID = transactionID;
        this.status = status;
        this.statusCode = statusCode;
        this.message = message;
        this.timestamp = LocalDateTime.now().toString();
    }

}
