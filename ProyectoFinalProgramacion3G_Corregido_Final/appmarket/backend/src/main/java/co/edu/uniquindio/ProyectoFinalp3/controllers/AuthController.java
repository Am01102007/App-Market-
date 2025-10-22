package co.edu.uniquindio.ProyectoFinalp3.controllers;

import co.edu.uniquindio.ProyectoFinalp3.enums.RoleEnum;
import co.edu.uniquindio.ProyectoFinalp3.models.User;
import co.edu.uniquindio.ProyectoFinalp3.services.JwtService;
import co.edu.uniquindio.ProyectoFinalp3.services.UserService;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    // Login: Autenticación del usuario
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody User loginRequest) {
        Map<String, Object> response = new HashMap<>();

        try {
            User user = userService.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());

            if (user != null) {
                String token = jwtService.generateToken(user.getId());
                response.put("ok", true);
                response.put("token", token);
                response.put("nombre", user.getUsername());
                response.put("role", user.getRole());
                return ResponseEntity.ok(response);
            } else {
                response.put("ok", false);
                response.put("msg", "Credenciales inválidas");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            // Imprimir o registrar la excepción para entender qué está fallando
            e.printStackTrace(); // Esto imprimirá el error en la consola
            response.put("ok", false);
            response.put("msg", "Error en el proceso de autenticación: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Register: Registro de un nuevo usuario
    // Register: Registro de un nuevo usuario
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody User registerRequest) {
        Map<String, Object> response = new HashMap<>();

        // Validación básica del email
        if (registerRequest.getEmail() == null || !registerRequest.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            response.put("ok", false);
            response.put("msg", "Email no es válido");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Verifica si el correo electrónico ya está registrado
        if (userService.emailExists(registerRequest.getEmail())) {
            response.put("ok", false);
            response.put("msg", "El correo electrónico ya está registrado");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        // Validación de la contraseña
        if (registerRequest.getPassword() == null || registerRequest.getPassword().length() < 8) {
            response.put("ok", false);
            response.put("msg", "La contraseña debe tener al menos 8 caracteres");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Establecer el rol del nuevo usuario
        registerRequest.setRole(RoleEnum.USER);

        // Crea el usuario (asegúrate de que la contraseña esté codificada)
        User newUser = userService.createUser(registerRequest);

        // Genera el JWT para el nuevo usuario
        String token = jwtService.generateToken(newUser.getId());

        // Respuesta exitosa
        response.put("ok", true);
        response.put("msg", "Usuario registrado exitosamente");
        response.put("token", token);
        response.put("role", "USER");

        // Asegúrate de que el return esté al final del método sin ningún error de
        // sintaxis
        return ResponseEntity.ok(response);
    }

}