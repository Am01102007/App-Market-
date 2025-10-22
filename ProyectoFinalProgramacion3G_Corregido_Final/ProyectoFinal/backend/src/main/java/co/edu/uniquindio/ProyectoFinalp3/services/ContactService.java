package co.edu.uniquindio.ProyectoFinalp3.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniquindio.ProyectoFinalp3.models.Contact;
import co.edu.uniquindio.ProyectoFinalp3.models.User;
import co.edu.uniquindio.ProyectoFinalp3.repository.ContactRepository;
import co.edu.uniquindio.ProyectoFinalp3.repository.UserRepository;

@Service
public class ContactService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    // Método para añadir un usuario a la lista de contactos de otro usuario usando el username
    @Transactional
    public Contact addContact(String userUsername, String contactUsername) {
        if (userUsername.equals(contactUsername)) {
            throw new IllegalArgumentException("A user cannot add themselves as a contact.");
        }

        User user = userRepository.findByUsername(userUsername)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userUsername));

        User contactUser = userRepository.findByUsername(contactUsername)
                .orElseThrow(() -> new IllegalArgumentException("Contact user not found: " + contactUsername));

        // Verifica si la relación ya existe
        Optional<Contact> existingContact = contactRepository.findByUserAndContactUser(user, contactUser);
        if (existingContact.isPresent()) {
            throw new IllegalArgumentException("This user is already in your contacts.");
        }

        // Crea y guarda la relación de contacto
        Contact contact = new Contact(user, contactUser);
        return contactRepository.save(contact);
    }

    // Método para listar contactos de un usuario usando el username
    public List<Contact> getContacts(String userUsername) {
        User user = userRepository.findByUsername(userUsername)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userUsername));

        return contactRepository.findByUser(user);
    }

    // Método para eliminar un contacto usando los usernames
    @Transactional
    public void removeContact(String userUsername, String contactUsername) {
        User user = userRepository.findByUsername(userUsername)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userUsername));

        User contactUser = userRepository.findByUsername(contactUsername)
                .orElseThrow(() -> new IllegalArgumentException("Contact user not found: " + contactUsername));

        Contact contact = contactRepository.findByUserAndContactUser(user, contactUser)
                .orElseThrow(() -> new IllegalArgumentException("Contact not found."));

        contactRepository.delete(contact);
    }
    // Método para obtener sugerencias de contactos basadas en amigos mutuos
    public List<User> getSuggestedContacts(String userUsername) {
    User user = userRepository.findByUsername(userUsername)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + userUsername));
    return contactRepository.findSuggestedContactsByMutualFriends(user);
}
}
