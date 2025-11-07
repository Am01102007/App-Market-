/**
 * DTO de actualización de usuario.
 * Permite modificar datos del perfil.
 */
package co.edu.uniquindio.ProyectoFinalp3.dto;

public class UpdateUserRequest {
    private String firstName;
    private String lastName;
    private String address;
    private String cedula;

    // Getters y Setters
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }
}
