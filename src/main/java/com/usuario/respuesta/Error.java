package com.usuario.respuesta;
import lombok.Data;

//Mensajes de error
@Data
public class Error {
    public  final static String	ERROR_INESPERADO = "A ocurrido un error, favor de volver a intentarlo.";
    public  final static String	REQUEST_INCORRECTO = "ERROR -- REQUEST INCORRECTO -- VALIDAR CONTRATO DE INTERFAZ";
    public  final static String	NOENCONTRO_REGISTRO = "No se encontro registro";
    public  final static String	CORREO_INVALIDO = "Correo invalido";
    public  final static String	CORREO_EXISTENTE = "El Correo ya existe";
    public  final static String	NUMEMPLEADO_EXISTENTE = "El numero de empleado ya existe";
    public  final static String	USERNAME_EXISTENTE = "El nombre de usuario ya existe";
    public  final static String	CORREO_NOVERIFICADO = "El correo no ha sido verificado";

    public  final static String	PWD_NOCOINCIDEN = "Las contraseñas no son iguales";

}
