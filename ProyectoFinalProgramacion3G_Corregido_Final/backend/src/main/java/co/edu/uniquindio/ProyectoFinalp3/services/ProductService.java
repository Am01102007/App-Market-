/**
 * Servicio de productos.
 * Maneja creación, actualización, listado y estado de productos.
 */
package co.edu.uniquindio.ProyectoFinalp3.services;

import co.edu.uniquindio.ProyectoFinalp3.enums.ProductStatus;
import co.edu.uniquindio.ProyectoFinalp3.models.Product;
import co.edu.uniquindio.ProyectoFinalp3.repository.UserRepository;
import co.edu.uniquindio.ProyectoFinalp3.models.User;
import co.edu.uniquindio.ProyectoFinalp3.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio para manejar todas las operaciones relacionadas con productos.
 * Proporciona funcionalidades CRUD (Crear, Leer, Actualizar, Eliminar) para productos,
 * así como búsquedas por diferentes criterios como categoría, usuario y estado.
 * 
 * @author Sistema App Market
 * @version 1.0
 */
@Service
public class ProductService {

    /**
     * Repositorio para acceder a los datos de productos en la base de datos.
     * Se inyecta automáticamente por Spring.
     */
    @Autowired
    private ProductRepository productRepository;

    /**
     * Repositorio para acceder a los datos de usuarios en la base de datos.
     * Necesario para asociar productos con sus propietarios.
     */
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Crea un nuevo producto y lo asocia con un usuario específico.
     * Busca al usuario por su nombre de usuario y establece la relación.
     * 
     * @param product Objeto Product con los datos del nuevo producto
     * @param username Nombre de usuario del propietario del producto
     * @return Product creado y guardado en la base de datos
     * @throws IllegalArgumentException si el usuario no existe
     */
    public Product createProduct(Product product, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + username));

        product.setUser(user);  // Asociar el usuario al producto
        return productRepository.save(product);
    }

    /**
     * Obtiene todos los productos registrados en el sistema.
     * 
     * @return Lista completa de todos los productos
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Busca un producto específico por su ID único.
     * 
     * @param id UUID del producto a buscar
     * @return Optional<Product> que contiene el producto si existe, vacío si no existe
     */
    public Optional<Product> getProductById(UUID id) {
        return productRepository.findById(id);
    }

    /**
     * Actualiza un producto existente con nueva información.
     * Verifica que el producto exista antes de realizar la actualización.
     * 
     * @param id UUID del producto a actualizar
     * @param updatedProduct Objeto Product con los nuevos datos
     * @return Product actualizado si existe, null si no se encuentra
     */
    public Product updateProduct(UUID id, Product updatedProduct) {
        if (productRepository.existsById(id)) {
            updatedProduct.setId(id);
            return productRepository.save(updatedProduct);
        }
        return null;
    }

    /**
     * Elimina un producto del sistema.
     * Verifica que el producto exista antes de eliminarlo.
     * 
     * @param id UUID del producto a eliminar
     * @return true si el producto fue eliminado exitosamente, false si no existe
     */
    public boolean deleteProduct(UUID id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Obtiene todos los productos que pertenecen a un usuario específico.
     * Útil para mostrar los productos de un vendedor en particular.
     * 
     * @param username Nombre de usuario del propietario de los productos
     * @return Lista de productos que pertenecen al usuario especificado
     */
    public List<Product> getProductsByUsername(String username) {
        return productRepository.findByUser_Username(username);
    }
    
    /**
     * Busca productos por categoría específica.
     * 
     * @param category Nombre de la categoría a buscar
     * @return Lista de productos que pertenecen a la categoría especificada
     */
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    /**
     * Busca productos por el nombre de la categoría.
     * Utiliza la relación con la entidad Category para buscar por su nombre.
     * 
     * @param categoryName Nombre de la categoría a buscar
     * @return Lista de productos que pertenecen a la categoría con el nombre especificado
     */
    public List<Product> getProductsByCategoryName(String categoryName) {
        return productRepository.findByCategory_Name(categoryName);
    }

    /**
     * Obtiene productos filtrados por su estado (activo, inactivo, etc.).
     * Útil para mostrar solo productos disponibles para la venta.
     * 
     * @param status Estado del producto (ACTIVE, INACTIVE, etc.)
     * @return Lista de productos que tienen el estado especificado
     */
    public List<Product> getActiveProducts(ProductStatus status) {  
        return productRepository.findByStatus(status);
    }
    
    /**
     * Busca productos por término de búsqueda en nombre y descripción.
     * Realiza una búsqueda insensible a mayúsculas y minúsculas.
     * 
     * @param searchTerm Término de búsqueda
     * @return Lista de productos que coinciden con el término de búsqueda
     */
    public List<Product> searchProducts(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllProducts();
        }
        return productRepository.findByNameOrDescriptionContainingIgnoreCase(searchTerm.trim());
    }
    
    /**
     * Busca productos activos por término de búsqueda en nombre y descripción.
     * Realiza una búsqueda insensible a mayúsculas y minúsculas solo en productos activos.
     * 
     * @param searchTerm Término de búsqueda
     * @return Lista de productos activos que coinciden con el término de búsqueda
     */
    public List<Product> searchActiveProducts(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getActiveProducts(ProductStatus.ACTIVE);
        }
        return productRepository.findByStatusAndNameOrDescriptionContainingIgnoreCase(ProductStatus.ACTIVE, searchTerm.trim());
    }
}
