/**
 * DTO de registro de usuario.
 * Reúne datos básicos para crear una cuenta.
 */
package co.edu.uniquindio.ProyectoFinalp3.dto;

import lombok.Data;

@Data
public class RegisterDto {
    private String email;
    private String contrasena;
    private String nombre;
}
