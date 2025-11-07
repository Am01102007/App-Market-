/**
 * DTO de inicio de sesión.
 * Contiene correo y contraseña para autenticación.
 */
package co.edu.uniquindio.ProyectoFinalp3.dto;

import lombok.Data;

@Data
public class LoginDto {
    private String email;
    private String contrasena;
}
