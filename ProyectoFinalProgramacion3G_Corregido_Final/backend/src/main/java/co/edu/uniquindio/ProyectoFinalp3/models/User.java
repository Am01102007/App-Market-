/**
 * Modelo de usuario.
 * Contiene identidad, credenciales y roles del sistema.
 */
package co.edu.uniquindio.ProyectoFinalp3.models;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import co.edu.uniquindio.ProyectoFinalp3.enums.RoleEnum;

/**
 * Entidad que representa a un usuario en el sistema de marketplace.
 * Contiene toda la información personal y de autenticación del usuario,
 * así como las relaciones con productos, contactos y chats.
 * 
 * Esta clase maneja:
 * - Información básica del usuario (nombre, email, contraseña)
 * - Datos personales (cédula, dirección)
 * - Rol del usuario en el sistema
 * - Relaciones con otras entidades (productos, contactos, chats)
 * 
 * @author Sistema App Market
 * @version 1.0
 */
@Entity
@Table(name = "users")
public class User {

    /**
     * Identificador único del usuario.
     * Se genera automáticamente como UUID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JdbcTypeCode(SqlTypes.BINARY)
    private UUID id;

    /**
     * Nombre de usuario único en el sistema.
     * Se genera automáticamente basado en el email.
     */
    private String username;

    /**
     * Dirección de correo electrónico del usuario.
     * Debe ser única en el sistema y no puede ser nula.
     */
    @Column(unique = true, nullable = false)
    private String email;
    
    /**
     * Contraseña del usuario encriptada.
     * Se almacena usando hash para seguridad.
     */
    private String password;
    
    /**
     * Primer nombre del usuario.
     */
    private String firstName;
    
    /**
     * Apellido del usuario.
     */
    private String lastName;
    
    /**
     * Número de cédula o documento de identidad del usuario.
     */
    private String cedula;
    
    /**
     * Dirección física del usuario.
     */
    private String address;

    /**
     * Rol del usuario en el sistema (USER, ADMIN, etc.).
     * Define los permisos y funcionalidades disponibles.
     */
    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    /**
     * Lista de participaciones en chats del usuario.
     * Relación uno a muchos con ChatParticipant.
     * Se ignora en la serialización JSON para evitar referencias circulares.
     */
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<ChatParticipant> chatParticipants;

    /**
     * Lista de productos que pertenecen al usuario.
     * Relación uno a muchos con Product.
     * Se ignora en la serialización JSON para evitar referencias circulares.
     */
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Product> products;

    /**
     * Lista de contactos del usuario.
     * Relación uno a muchos con Contact.
     * Se ignora en la serialización JSON para evitar referencias circulares.
     */
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Contact> contacts;

    /**
     * Constructor vacío requerido por JPA.
     */
    public User() {}

    /**
     * Constructor que inicializa solo el ID.
     * Útil para crear referencias rápidas al usuario.
     * 
     * @param id UUID del usuario
     */
    public User(UUID id) {
        this.id = id;
    }

    /**
     * Constructor completo para crear un nuevo usuario.
     * 
     * @param username Nombre de usuario
     * @param password Contraseña (será encriptada)
     * @param firstName Primer nombre
     * @param lastName Apellido
     * @param cedula Número de cédula
     * @param address Dirección
     * @param role Rol del usuario
     */
    public User(String username, String password, String firstName, String lastName, String cedula, String address, RoleEnum role) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.cedula = cedula;
        this.address = address;
        this.role = role;
    }

    // Getters y Setters con documentación

    /**
     * Obtiene el ID único del usuario.
     * @return UUID del usuario
     */
    public UUID getId() { return id; }
    
    /**
     * Establece el ID del usuario.
     * @param id UUID del usuario
     */
    public void setId(UUID id) { this.id = id; }

    /**
     * Obtiene el nombre de usuario.
     * @return Nombre de usuario
     */
    public String getUsername() { return username; }
    
    /**
     * Establece el nombre de usuario.
     * @param username Nombre de usuario
     */
    public void setUsername(String username) { this.username = username; }

    /**
     * Obtiene la contraseña encriptada del usuario.
     * @return Contraseña encriptada
     */
    public String getPassword() { return password; }
    
    /**
     * Establece la contraseña del usuario.
     * @param password Contraseña (será encriptada antes de almacenar)
     */
    public void setPassword(String password) { this.password = password; }

    /**
     * Obtiene el primer nombre del usuario.
     * @return Primer nombre
     */
    public String getFirstName() { return firstName; }
    
    /**
     * Establece el primer nombre del usuario.
     * @param firstName Primer nombre
     */
    public void setFirstName(String firstName) { this.firstName = firstName; }

    /**
     * Obtiene el apellido del usuario.
     * @return Apellido
     */
    public String getLastName() { return lastName; }
    
    /**
     * Establece el apellido del usuario.
     * @param lastName Apellido
     */
    public void setLastName(String lastName) { this.lastName = lastName; }

    /**
     * Obtiene el número de cédula del usuario.
     * @return Número de cédula
     */
    public String getCedula() { return cedula; }
    
    /**
     * Establece el número de cédula del usuario.
     * @param cedula Número de cédula
     */
    public void setCedula(String cedula) { this.cedula = cedula; }

    /**
     * Obtiene la dirección del usuario.
     * @return Dirección
     */
    public String getAddress() { return address; }
    
    /**
     * Establece la dirección del usuario.
     * @param address Dirección
     */
    public void setAddress(String address) { this.address = address; }

    /**
     * Obtiene el rol del usuario.
     * @return Rol del usuario
     */
    public RoleEnum getRole() { return role; }
    
    /**
     * Establece el rol del usuario.
     * @param role Rol del usuario
     */
    public void setRole(RoleEnum role) { this.role = role; }

    /**
     * Obtiene la lista de productos del usuario.
     * @return Lista de productos
     */
    public List<Product> getProducts() { return products; }
    
    /**
     * Establece la lista de productos del usuario.
     * @param products Lista de productos
     */
    public void setProducts(List<Product> products) { this.products = products; }

    /**
     * Obtiene la lista de contactos del usuario.
     * @return Lista de contactos
     */
    public List<Contact> getContacts() { return contacts; }
    
    /**
     * Establece la lista de contactos del usuario.
     * @param contacts Lista de contactos
     */
    public void setContacts(List<Contact> contacts) { this.contacts = contacts; }

    /**
     * Obtiene la lista de participaciones en chats del usuario.
     * @return Lista de participaciones en chats
     */
    public List<ChatParticipant> getChatParticipants() { return chatParticipants; }
    
    /**
     * Establece la lista de participaciones en chats del usuario.
     * @param chatParticipants Lista de participaciones en chats
     */
    public void setChatParticipants(List<ChatParticipant> chatParticipants) { this.chatParticipants = chatParticipants; }

    /**
     * Obtiene el email del usuario.
     * @return Email del usuario
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * Establece el email del usuario.
     * @param email Email del usuario
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
