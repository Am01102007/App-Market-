package co.edu.uniquindio.ProyectoFinalp3.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import co.edu.uniquindio.ProyectoFinalp3.models.Contact;
import co.edu.uniquindio.ProyectoFinalp3.models.User;
import co.edu.uniquindio.ProyectoFinalp3.services.ContactService;

@RestController
@RequestMapping("/contacts")
public class ContactController {

    @Autowired
    private ContactService contactService;

    // Endpoint para añadir un contacto usando los usernames
    @PostMapping
    public ResponseEntity<Contact> addContact(@RequestParam String userUsername, @RequestParam String contactUsername) {
        Contact contact = contactService.addContact(userUsername, contactUsername);
        return ResponseEntity.ok(contact);
    }

    // Endpoint para obtener la lista de contactos de un usuario específico usando su username
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


