/**
 * Cargador de datos iniciales.
 * Inserta datos de ejemplo en entorno de desarrollo.
 */
package co.edu.uniquindio.ProyectoFinalp3.config;

import co.edu.uniquindio.ProyectoFinalp3.models.*;
import co.edu.uniquindio.ProyectoFinalp3.enums.RoleEnum;
import co.edu.uniquindio.ProyectoFinalp3.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
@Profile("dev")
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MarketPlaceRepository marketPlaceRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductLikeRepository productLikeRepository;

    @Autowired
    private ProductCommentRepository productCommentRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Cargar datos solo si NO hay usuarios y NO hay productos (idempotente)
        long userCount = userRepository.count();
        long productCount = productRepository.count();
        if (userCount == 0 && productCount == 0) {
            loadInitialData();
        }
    }

    private void loadInitialData() {
        // Crear usuarios de forma idempotente (evitar duplicados por email)
        User user1 = userRepository.findByEmail("juan@example.com");
        if (user1 == null) {
            user1 = new User();
            user1.setFirstName("Juan");
            user1.setLastName("Pérez");
            user1.setEmail("juan@example.com");
            user1.setUsername("juan");
            user1.setPassword(passwordEncoder.encode("password123"));
            user1.setCedula("12345678");
            user1.setAddress("Calle 123 #45-67");
            user1.setRole(RoleEnum.USER);
            user1 = userRepository.save(user1);
        }

        User user2 = userRepository.findByEmail("maria@example.com");
        if (user2 == null) {
            user2 = new User();
            user2.setFirstName("María");
            user2.setLastName("García");
            user2.setEmail("maria@example.com");
            user2.setUsername("maria");
            user2.setPassword(passwordEncoder.encode("password123"));
            user2.setCedula("87654321");
            user2.setAddress("Carrera 89 #12-34");
            user2.setRole(RoleEnum.USER);
            user2 = userRepository.save(user2);
        }

        User admin = userRepository.findByEmail("admin@example.com");
        if (admin == null) {
            admin = new User();
            admin.setFirstName("Admin");
            admin.setLastName("Sistema");
            admin.setEmail("admin@example.com");
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setCedula("11111111");
            admin.setAddress("Oficina Central");
            admin.setRole(RoleEnum.ADMIN);
            admin = userRepository.save(admin);
        }

        // Crear categorías
        Category electronics = new Category();
        electronics.setName("Electrónicos");
        electronics.setDescription("Dispositivos electrónicos y tecnología");
        electronics = categoryRepository.save(electronics);

        Category clothing = new Category();
        clothing.setName("Ropa");
        clothing.setDescription("Vestimenta y accesorios");
        clothing = categoryRepository.save(clothing);

        Category home = new Category();
        home.setName("Hogar");
        home.setDescription("Artículos para el hogar");
        home = categoryRepository.save(home);

        // Crear marketplace
        MarketPlace marketplace = new MarketPlace();
        marketplace.setNombre("AppMarket Central");
        marketplace = marketPlaceRepository.save(marketplace);

        // Crear productos
        Product product1 = new Product();
        product1.setName("Smartphone Samsung Galaxy");
        product1.setDescription("Teléfono inteligente de última generación con cámara de 108MP");
        product1.setPrice(new BigDecimal("899.99"));
        product1.setImageUrl("https://example.com/samsung-galaxy.jpg");
        product1.setCategory(electronics);
        product1.setStatus(co.edu.uniquindio.ProyectoFinalp3.enums.ProductStatus.ACTIVE);
        product1.setUser(user2);
        product1.setMarketPlace(marketplace);
        product1 = productRepository.save(product1);

        Product product2 = new Product();
        product2.setName("Camiseta Deportiva");
        product2.setDescription("Camiseta transpirable para actividades deportivas");
        product2.setPrice(new BigDecimal("29.99"));
        product2.setImageUrl("https://example.com/camiseta-deportiva.jpg");
        product2.setCategory(clothing);
        product2.setStatus(co.edu.uniquindio.ProyectoFinalp3.enums.ProductStatus.ACTIVE);
        product2.setUser(user2);
        product2.setMarketPlace(marketplace);
        product2 = productRepository.save(product2);

        Product product3 = new Product();
        product3.setName("Lámpara LED");
        product3.setDescription("Lámpara LED de escritorio con regulador de intensidad");
        product3.setPrice(new BigDecimal("45.50"));
        product3.setImageUrl("https://example.com/lampara-led.jpg");
        product3.setCategory(home);
        product3.setStatus(co.edu.uniquindio.ProyectoFinalp3.enums.ProductStatus.ACTIVE);
        product3.setUser(user2);
        product3.setMarketPlace(marketplace);
        product3 = productRepository.save(product3);

        // Crear likes de productos
        ProductLike like1 = new ProductLike();
        like1.setUser(user1);
        like1.setProduct(product1);
        productLikeRepository.save(like1);

        ProductLike like2 = new ProductLike();
        like2.setUser(user1);
        like2.setProduct(product3);
        productLikeRepository.save(like2);

        // Crear comentarios de productos
        ProductComment comment1 = new ProductComment();
        comment1.setCommentText("Excelente producto, muy recomendado!");
        comment1.setUser(user1);
        comment1.setProduct(product1);
        productCommentRepository.save(comment1);

        ProductComment comment2 = new ProductComment();
        comment2.setCommentText("Buena calidad por el precio");
        comment2.setUser(user1);
        comment2.setProduct(product2);
        productCommentRepository.save(comment2);

        // Crear contactos
        Contact contact1 = new Contact();
        contact1.setUser(user1);
        contact1.setContactUser(user2);
        contactRepository.save(contact1);

        // Crear chats
        Chat chat1 = new Chat();
        chat1.setName("Chat entre Juan y María");
        chatRepository.save(chat1);

        // Crear mensajes
        Message message1 = new Message();
        message1.setChat(chat1);
        message1.setUser(user1);
        message1.setContent("Hola María, ¿cómo estás?");
        messageRepository.save(message1);

        Message message2 = new Message();
        message2.setChat(chat1);
        message2.setUser(user2);
        message2.setContent("¡Hola Juan! Todo bien, gracias por preguntar.");
        messageRepository.save(message2);

        System.out.println("Datos de prueba cargados exitosamente!");
    }
}
