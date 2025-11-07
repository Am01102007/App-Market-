/**
 * Modelo de contacto.
 * Almacena datos de comunicación de usuarios.
 */
package co.edu.uniquindio.ProyectoFinalp3.models;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "contacts")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JdbcTypeCode(SqlTypes.BINARY)
    private UUID id;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Usuario propietario de este contacto

    @ManyToOne
    @JoinColumn(name = "contact_user_id", nullable = false)
    private User contactUser; // Usuario añadido como contacto

    // Constructor vacío
    public Contact() {
    }

    // Constructor con parámetros
    public Contact(User user, User contactUser) {
        this.user = user;
        this.contactUser = contactUser;
    }

    // Getters y Setters
    public UUID getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getContactUser() {
        return contactUser;
    }

    public void setContactUser(User contactUser) {
        this.contactUser = contactUser;
    }

    @Override
    public String toString() {
        return "Contact{id=" + id + ", user=" + user.getUsername() + ", contactUser=" + contactUser.getUsername() + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Contact contact = (Contact) o;
        return Objects.equals(id, contact.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
