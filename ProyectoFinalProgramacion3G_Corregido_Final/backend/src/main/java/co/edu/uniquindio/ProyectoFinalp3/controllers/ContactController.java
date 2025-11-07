/**
 * Controlador de contactos.
 * Expone endpoints para crear, listar y gestionar contactos.
 */
package co.edu.uniquindio.ProyectoFinalp3.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import co.edu.uniquindio.ProyectoFinalp3.models.Contact;
import co.edu.uniquindio.ProyectoFinalp3.models.User;
import co.edu.uniquindio.ProyectoFinalp3.services.ContactService;

/**
 * Controlador REST para manejar operaciones relacionadas con contactos.
 * Permite añadir contactos y obtener listas de contactos de usuarios.
 * 
 * @author Sistema App Market
 * @version 1.0
 */
@RestController
@RequestMapping("/contacts")
public class ContactController {

    /**
     * Servicio para manejar operaciones relacionadas con contactos.
     */
    @Autowired
    private ContactService contactService;

    /**
     * Añade un contacto usando los nombres de usuario.
     * 
     * @param userUsername El nombre de usuario que añade el contacto
     * @param contactUsername El nombre de usuario del contacto a añadir
     * @return El contacto creado
     */
    @PostMapping
    public ResponseEntity<Contact> addContact(@RequestParam String userUsername, @RequestParam String contactUsername) {
        Contact contact = contactService.addContact(userUsername, contactUsername);
        return ResponseEntity.ok(contact);
    }

    /**
     * Obtiene la lista de contactos de un usuario específico.
     * 
     * @param userUsername El nombre de usuario del cual obtener los contactos
     * @return Lista de contactos del usuario
     */
    @GetMapping("/{userUsername}")
    public ResponseEntity<List<Contact>> getContacts(@PathVariable String userUsername) {
        List<Contact> contacts = contactService.getContacts(userUsername);
        return ResponseEntity.ok(contacts);
    }

    // Endpoint para eliminar un contacto usando los usernames
    @DeleteMapping("/{userUsername}/{contactUsername}")
    public ResponseEntity<Void> removeContact(@PathVariable String userUsername, @PathVariable String contactUsername) {
        contactService.removeContact(userUsername, contactUsername);
        return ResponseEntity.noContent().build();
    }
    // Endpoint para obtener sugerencias de contactos de un usuario
    @GetMapping("/suggestions/{userUsername}")
    public ResponseEntity<List<User>> getSuggestedContacts(@PathVariable String userUsername) {
    List<User> suggestedContacts = contactService.getSuggestedContacts(userUsername);
    return ResponseEntity.ok(suggestedContacts);
}
}


