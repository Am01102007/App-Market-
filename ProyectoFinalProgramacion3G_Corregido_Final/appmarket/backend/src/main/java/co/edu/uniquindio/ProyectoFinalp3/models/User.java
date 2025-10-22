package co.edu.uniquindio.ProyectoFinalp3.models;

import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import co.edu.uniquindio.ProyectoFinalp3.enums.RoleEnum;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String username;

    @Column(unique = true, nullable = false)
    private String email;
    
    private String password;
    private String firstName;
    private String lastName;
    private String cedula;
    private String address;

    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<ChatParticipant> chatParticipants;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Product> products;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Contact> contacts;

    // Constructor vacío
    public User() {}

    public User(UUID id) {
        this.id = id;
    }

    // Constructor con parámetros
    public User(String username, String password, String firstName, String lastName, String cedula, String address, RoleEnum role) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.cedula = cedula;
        this.address = address;
        this.role = role;
    }

    // Getters y Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public RoleEnum getRole() { return role; }
    public void setRole(RoleEnum role) { this.role = role; }

    public List<Product> getProducts() { return products; }
    public void setProducts(List<Product> products) { this.products = products; }

    public List<Contact> getContacts() { return contacts; }
    public void setContacts(List<Contact> contacts) { this.contacts = contacts; }

    public List<ChatParticipant> getChatParticipants() { return chatParticipants; }
    public void setChatParticipants(List<ChatParticipant> chatParticipants) { this.chatParticipants = chatParticipants; }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}