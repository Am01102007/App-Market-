/**
 * Controlador de productos.
 * Permite crear, listar, actualizar y gestionar comentarios/likes.
 */
package co.edu.uniquindio.ProyectoFinalp3.controllers;

import co.edu.uniquindio.ProyectoFinalp3.models.MarketPlace;
import co.edu.uniquindio.ProyectoFinalp3.models.Category;
import co.edu.uniquindio.ProyectoFinalp3.repository.MarketPlaceRepository;
import co.edu.uniquindio.ProyectoFinalp3.repository.CategoryRepository;
import co.edu.uniquindio.ProyectoFinalp3.repository.UserRepository;
import java.math.BigDecimal;
import co.edu.uniquindio.ProyectoFinalp3.models.User;
import co.edu.uniquindio.ProyectoFinalp3.enums.ProductStatus;
import co.edu.uniquindio.ProyectoFinalp3.models.Product;
import co.edu.uniquindio.ProyectoFinalp3.models.ProductComment;
import co.edu.uniquindio.ProyectoFinalp3.services.ProductCommentService;
import co.edu.uniquindio.ProyectoFinalp3.services.ProductLikeService;
import co.edu.uniquindio.ProyectoFinalp3.services.ProductService;
import co.edu.uniquindio.ProyectoFinalp3.services.ImageStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Map;
import java.util.ArrayList;

/**
 * Controlador REST para la gestión de productos en el marketplace.
 * 
 * Esta clase maneja todas las operaciones relacionadas con productos:
 * - Crear nuevos productos
 * - Consultar productos existentes
 * - Actualizar información de productos
 * - Eliminar productos
 * - Gestionar categorías
 * - Manejar comentarios y likes
 * - Cargar datos de prueba
 * 
 * @author Equipo de Desarrollo - Programación 3
 * @version 1.0
 * @since 2024
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    /** Servicio para operaciones de productos */
    @Autowired
    private ProductService productService;
    
    /** Servicio para gestionar likes de productos */
    @Autowired
    private ProductLikeService productLikeService; 

    /** Servicio para gestionar comentarios de productos */
    @Autowired
    private ProductCommentService productCommentService; 

    /** Repositorio para operaciones con usuarios */
    @Autowired
    private UserRepository userRepository;

    /** Repositorio para operaciones con marketplace */
    @Autowired
    private MarketPlaceRepository marketPlaceRepository;

    /** Repositorio para operaciones con categorías */
    @Autowired
    private CategoryRepository categoryRepository;

    /** Servicio para almacenamiento de imágenes */
    @Autowired
    private ImageStorageService imageStorageService;

    /**
     * Crea un nuevo producto en el sistema.
     * 
     * Si la categoría no existe, la crea automáticamente.
     * Asocia el producto con el usuario especificado.
     * 
     * @param product Datos del producto a crear
     * @param username Nombre de usuario del vendedor
     * @return ResponseEntity con el producto creado o mensaje de error
     */
    @PostMapping("/create")
    public ResponseEntity<?> createProduct(@RequestBody Product product, @RequestParam String username) {
        try {
            System.out.println("=== DEBUG: Creando producto ===");
            System.out.println("Username recibido: " + username);
            System.out.println("Producto recibido: " + product.getName());
            System.out.println("Categoría recibida: " + (product.getCategory() != null ? product.getCategory().getName() : "null"));
            
            // Si se proporciona una categoría por nombre, buscarla o crearla
            if (product.getCategory() != null && product.getCategory().getName() != null) {
                String categoryName = product.getCategory().getName();
                System.out.println("Buscando categoría: " + categoryName);
                Category category = categoryRepository.findByName(categoryName)
                    .orElseGet(() -> {
                        System.out.println("Creando nueva categoría: " + categoryName);
                        Category newCategory = new Category();
                        newCategory.setName(categoryName);
                        newCategory.setDescription("Categoría creada automáticamente");
                        return categoryRepository.save(newCategory);
                    });
                product.setCategory(category);
                System.out.println("Categoría asignada: " + category.getName());
            }
            
            // Asignar marketplace por defecto si no se especifica
            if (product.getMarketPlace() == null) {
                MarketPlace defaultMarketPlace = marketPlaceRepository.findByNombre("AppMarket Demo")
                    .orElseGet(() -> {
                        System.out.println("Creando marketplace por defecto");
                        MarketPlace newMarketPlace = new MarketPlace();
                        newMarketPlace.setNombre("AppMarket Demo");
                        return marketPlaceRepository.save(newMarketPlace);
                    });
                product.setMarketPlace(defaultMarketPlace);
                System.out.println("Marketplace asignado: " + defaultMarketPlace.getNombre());
            }
            
            // Asignar estado por defecto si no se especifica
            if (product.getStatus() == null) {
                product.setStatus(ProductStatus.ACTIVE);
                System.out.println("Estado asignado: ACTIVE");
            }
            
            System.out.println("Llamando a productService.createProduct...");
            Product createdProduct = productService.createProduct(product, username);
            System.out.println("Producto creado exitosamente con ID: " + createdProduct.getId());
            return ResponseEntity.ok(createdProduct);
        } catch (Exception e) {
            System.err.println("ERROR al crear producto: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al crear el producto: " + e.getMessage());
        }
    }

    /**
     * Obtiene todos los productos disponibles en el sistema.
     * 
     * @return ResponseEntity con la lista de todos los productos
     */
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    /**
     * Busca un producto específico por su ID único.
     * 
     * @param id Identificador único del producto
     * @return ResponseEntity con el producto encontrado o mensaje de error
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable UUID id) {
        Optional<Product> product = productService.getProductById(id);
        return product.isPresent() ? ResponseEntity.ok(product.get()) 
                                   : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable UUID id, @RequestBody Product product) {
        try {
            // Si se proporciona una categoría por nombre, buscarla o crearla
            if (product.getCategory() != null && product.getCategory().getName() != null) {
                String categoryName = product.getCategory().getName();
                Category category = categoryRepository.findByName(categoryName)
                    .orElseGet(() -> {
                        Category newCategory = new Category();
                        newCategory.setName(categoryName);
                        newCategory.setDescription("Categoría creada automáticamente");
                        return categoryRepository.save(newCategory);
                    });
                product.setCategory(category);
            }
            
            Product updatedProduct = productService.updateProduct(id, product);
            return updatedProduct != null ? ResponseEntity.ok(updatedProduct)
                                          : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado o no se pudo actualizar");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al actualizar el producto: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable UUID id) {
        return productService.deleteProduct(id) ? ResponseEntity.ok("Producto eliminado correctamente")
                                                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado");
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<Product>> getProductsByUsername(@PathVariable String username) {
        return ResponseEntity.ok(productService.getProductsByUsername(username));
    }

    @GetMapping("/category/{categoryName}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable String categoryName) {
        return ResponseEntity.ok(productService.getProductsByCategoryName(categoryName));
    }

    /**
     * Busca productos por término de búsqueda en nombre y descripción.
     * 
     * @param q Término de búsqueda (query parameter)
     * @return ResponseEntity con la lista de productos que coinciden con la búsqueda
     */
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam(name = "q", required = false) String searchTerm) {
        try {
            List<Product> products = productService.searchActiveProducts(searchTerm);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/add-demo-products")
    public ResponseEntity<?> addDemoProducts() {
        try {
            // Crear usuarios demo
            User demoUser1 = userRepository.findByUsername("demo_seller1").orElseGet(() -> {
                User newUser = new User();
                newUser.setUsername("demo_seller1");
                newUser.setEmail("demo1@appmarket.com");
                newUser.setPassword("password123");
                return userRepository.save(newUser);
            });

            User demoUser2 = userRepository.findByUsername("demo_seller2").orElseGet(() -> {
                User newUser = new User();
                newUser.setUsername("demo_seller2");
                newUser.setEmail("demo2@appmarket.com");
                newUser.setPassword("password123");
                return userRepository.save(newUser);
            });

            // Crear marketplace demo
            MarketPlace demoMarketPlace = marketPlaceRepository.findByNombre("AppMarket Demo").orElseGet(() -> {
                MarketPlace newMarketPlace = new MarketPlace();
                newMarketPlace.setNombre("AppMarket Demo");
                return marketPlaceRepository.save(newMarketPlace);
            });

            // Crear categorías
            Category electronicsCategory = categoryRepository.findByName("Electrónicos").orElseGet(() -> {
                Category category = new Category("Electrónicos", "Dispositivos y gadgets electrónicos", "https://cdn-icons-png.flaticon.com/512/3659/3659899.png");
                return categoryRepository.save(category);
            });

            Category fashionCategory = categoryRepository.findByName("Moda").orElseGet(() -> {
                Category category = new Category("Moda", "Ropa, zapatos y accesorios", "https://cdn-icons-png.flaticon.com/512/892/892458.png");
                return categoryRepository.save(category);
            });

            Category homeCategory = categoryRepository.findByName("Hogar").orElseGet(() -> {
                Category category = new Category("Hogar", "Artículos para el hogar y decoración", "https://cdn-icons-png.flaticon.com/512/1946/1946488.png");
                return categoryRepository.save(category);
            });

            Category sportsCategory = categoryRepository.findByName("Deportes").orElseGet(() -> {
                Category category = new Category("Deportes", "Equipos y accesorios deportivos", "https://cdn-icons-png.flaticon.com/512/857/857418.png");
                return categoryRepository.save(category);
            });

            Category booksCategory = categoryRepository.findByName("Libros").orElseGet(() -> {
                Category category = new Category("Libros", "Libros y material educativo", "https://cdn-icons-png.flaticon.com/512/2232/2232688.png");
                return categoryRepository.save(category);
            });

            List<Product> createdProducts = new ArrayList<>();

            // Productos de Electrónicos
            List<Map<String, Object>> electronicsProducts = List.of(
                Map.of("name", "iPhone 15 Pro", "price", new BigDecimal("1299.99"), "description", "El último iPhone con chip A17 Pro, cámara de 48MP y pantalla Super Retina XDR de 6.1 pulgadas. Incluye Dynamic Island y conectividad USB-C.", "image", "https://store.storeimages.cdn-apple.com/4982/as-images.apple.com/is/iphone-15-pro-finish-select-202309-6-1inch-naturaltitanium?wid=5120&hei=2880&fmt=p-jpg&qlt=80&.v=1692895703814", "user", demoUser1),
                Map.of("name", "MacBook Air M2", "price", new BigDecimal("1199.99"), "description", "Laptop ultradelgada con chip M2 de Apple, pantalla Liquid Retina de 13.6 pulgadas, 8GB RAM y 256GB SSD. Perfecta para trabajo y estudio.", "image", "https://store.storeimages.cdn-apple.com/4982/as-images.apple.com/is/macbook-air-midnight-select-20220606?wid=904&hei=840&fmt=jpeg&qlt=90&.v=1653084303665", "user", demoUser2),
                Map.of("name", "Samsung Galaxy S24 Ultra", "price", new BigDecimal("1199.99"), "description", "Smartphone premium con S Pen integrado, cámara de 200MP, pantalla Dynamic AMOLED 2X de 6.8 pulgadas y batería de 5000mAh.", "image", "https://images.samsung.com/is/image/samsung/p6pim/es/2401/gallery/es-galaxy-s24-ultra-s928-sm-s928bztqeub-539573257?$650_519_PNG$", "user", demoUser1),
                Map.of("name", "AirPods Pro 2", "price", new BigDecimal("249.99"), "description", "Auriculares inalámbricos con cancelación activa de ruido, audio espacial personalizado y hasta 6 horas de reproducción.", "image", "https://store.storeimages.cdn-apple.com/4982/as-images.apple.com/is/MQD83?wid=2000&hei=2000&fmt=jpeg&qlt=90&.v=1660803972361", "user", demoUser2),
                Map.of("name", "PlayStation 5", "price", new BigDecimal("499.99"), "description", "Consola de videojuegos de nueva generación con SSD ultrarrápido, gráficos 4K, audio 3D y control DualSense con retroalimentación háptica.", "image", "https://gmedia.playstation.com/is/image/SIEPDC/ps5-product-thumbnail-01-en-14sep21?$facebook$", "user", demoUser1)
            );

            // Productos de Moda
            List<Map<String, Object>> fashionProducts = List.of(
                Map.of("name", "Zapatillas Nike Air Max 270", "price", new BigDecimal("149.99"), "description", "Zapatillas deportivas con tecnología Air Max, suela de espuma y diseño moderno. Perfectas para correr y uso casual.", "image", "https://static.nike.com/a/images/t_PDP_1728_v1/f_auto,q_auto:eco/awjogtdnqxniqqk0wpgf/air-max-270-shoes-2V5C4p.png", "user", demoUser2),
                Map.of("name", "Chaqueta de Cuero", "price", new BigDecimal("199.99"), "description", "Chaqueta de cuero genuino estilo motociclista, con forro interior y múltiples bolsillos. Disponible en negro y marrón.", "image", "https://images.unsplash.com/photo-1551028719-00167b16eac5?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1000&q=80", "user", demoUser1),
                Map.of("name", "Reloj Smartwatch", "price", new BigDecimal("299.99"), "description", "Reloj inteligente con monitor de frecuencia cardíaca, GPS, resistente al agua y batería de 7 días. Compatible con iOS y Android.", "image", "https://images.unsplash.com/photo-1523275335684-37898b6baf30?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1000&q=80", "user", demoUser2)
            );

            // Productos de Hogar
            List<Map<String, Object>> homeProducts = List.of(
                Map.of("name", "Cafetera Espresso", "price", new BigDecimal("89.99"), "description", "Cafetera espresso automática con molinillo integrado, 15 bares de presión y sistema de espuma de leche. Perfecta para café gourmet en casa.", "image", "https://images.unsplash.com/photo-1495474472287-4d71bcdd2085?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1000&q=80", "user", demoUser1),
                Map.of("name", "Aspiradora Robot", "price", new BigDecimal("399.99"), "description", "Aspiradora robótica inteligente con mapeo láser, control por app, programación automática y base de carga. Ideal para limpieza diaria.", "image", "https://images.unsplash.com/photo-1558618666-fcd25c85cd64?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1000&q=80", "user", demoUser2),
                Map.of("name", "Set de Sartenes Antiadherentes", "price", new BigDecimal("79.99"), "description", "Juego de 3 sartenes antiadherentes de diferentes tamaños, aptas para todo tipo de cocinas incluyendo inducción. Libres de PFOA.", "image", "https://images.unsplash.com/photo-1556909114-f6e7ad7d3136?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1000&q=80", "user", demoUser1)
            );

            // Productos de Deportes
            List<Map<String, Object>> sportsProducts = List.of(
                Map.of("name", "Bicicleta de Montaña", "price", new BigDecimal("599.99"), "description", "Bicicleta de montaña con marco de aluminio, 21 velocidades, frenos de disco y suspensión delantera. Ideal para senderos y aventuras.", "image", "https://images.unsplash.com/photo-1558618666-fcd25c85cd64?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1000&q=80", "user", demoUser2),
                Map.of("name", "Set de Pesas Ajustables", "price", new BigDecimal("199.99"), "description", "Juego de mancuernas ajustables de 5 a 25 kg cada una, con sistema de selección rápida. Perfectas para entrenamiento en casa.", "image", "https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1000&q=80", "user", demoUser1)
            );

            // Productos de Libros
            List<Map<String, Object>> bookProducts = List.of(
                Map.of("name", "Curso Completo de Programación", "price", new BigDecimal("49.99"), "description", "Libro completo sobre programación moderna, incluye JavaScript, Python, React y bases de datos. Con ejercicios prácticos y proyectos.", "image", "https://images.unsplash.com/photo-1532012197267-da84d127e765?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1000&q=80", "user", demoUser2),
                Map.of("name", "Manual de Marketing Digital", "price", new BigDecimal("34.99"), "description", "Guía completa sobre marketing digital, SEO, redes sociales y publicidad online. Incluye casos de estudio y estrategias actuales.", "image", "https://images.unsplash.com/photo-1481627834876-b7833e8f5570?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1000&q=80", "user", demoUser1)
            );

            // Crear productos de cada categoría
            for (Map<String, Object> productData : electronicsProducts) {
                Product product = new Product();
                product.setName((String) productData.get("name"));
                product.setImageUrl((String) productData.get("image"));
                product.setDescription((String) productData.get("description"));
                product.setPrice((BigDecimal) productData.get("price"));
                product.setCategory(electronicsCategory);
                product.setStatus(ProductStatus.ACTIVE);
                product.setUser((User) productData.get("user"));
                product.setMarketPlace(demoMarketPlace);
                createdProducts.add(productService.createProduct(product, ((User) productData.get("user")).getUsername()));
            }

            for (Map<String, Object> productData : fashionProducts) {
                Product product = new Product();
                product.setName((String) productData.get("name"));
                product.setImageUrl((String) productData.get("image"));
                product.setDescription((String) productData.get("description"));
                product.setPrice((BigDecimal) productData.get("price"));
                product.setCategory(fashionCategory);
                product.setStatus(ProductStatus.ACTIVE);
                product.setUser((User) productData.get("user"));
                product.setMarketPlace(demoMarketPlace);
                createdProducts.add(productService.createProduct(product, ((User) productData.get("user")).getUsername()));
            }

            for (Map<String, Object> productData : homeProducts) {
                Product product = new Product();
                product.setName((String) productData.get("name"));
                product.setImageUrl((String) productData.get("image"));
                product.setDescription((String) productData.get("description"));
                product.setPrice((BigDecimal) productData.get("price"));
                product.setCategory(homeCategory);
                product.setStatus(ProductStatus.ACTIVE);
                product.setUser((User) productData.get("user"));
                product.setMarketPlace(demoMarketPlace);
                createdProducts.add(productService.createProduct(product, ((User) productData.get("user")).getUsername()));
            }

            for (Map<String, Object> productData : sportsProducts) {
                Product product = new Product();
                product.setName((String) productData.get("name"));
                product.setImageUrl((String) productData.get("image"));
                product.setDescription((String) productData.get("description"));
                product.setPrice((BigDecimal) productData.get("price"));
                product.setCategory(sportsCategory);
                product.setStatus(ProductStatus.ACTIVE);
                product.setUser((User) productData.get("user"));
                product.setMarketPlace(demoMarketPlace);
                createdProducts.add(productService.createProduct(product, ((User) productData.get("user")).getUsername()));
            }

            for (Map<String, Object> productData : bookProducts) {
                Product product = new Product();
                product.setName((String) productData.get("name"));
                product.setImageUrl((String) productData.get("image"));
                product.setDescription((String) productData.get("description"));
                product.setPrice((BigDecimal) productData.get("price"));
                product.setCategory(booksCategory);
                product.setStatus(ProductStatus.ACTIVE);
                product.setUser((User) productData.get("user"));
                product.setMarketPlace(demoMarketPlace);
                createdProducts.add(productService.createProduct(product, ((User) productData.get("user")).getUsername()));
            }

            return ResponseEntity.ok(Map.of(
                "message", "Datos demo creados exitosamente",
                "totalProducts", createdProducts.size(),
                "categories", List.of("Electrónicos", "Moda", "Hogar", "Deportes", "Libros"),
                "users", List.of("demo_seller1", "demo_seller2"),
                "products", createdProducts
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al crear los datos demo: " + e.getMessage());
        }
    }

    @GetMapping("/active")
    public ResponseEntity<List<Product>> getActiveProducts() {
        return ResponseEntity.ok(productService.getActiveProducts(ProductStatus.ACTIVE));  
    }

    // Endpoint para subir imágenes de productos
    @PostMapping("/{id}/upload-image")
    public ResponseEntity<?> uploadProductImage(
            @PathVariable UUID id,
            @RequestParam("image") MultipartFile image) {
        try {
            Optional<Product> productOpt = productService.getProductById(id);
            if (!productOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado");
            }

            Product product = productOpt.get();
            
            // Si hay una imagen anterior local, eliminarla
            if (product.getImageUrl() != null && !product.getImageUrl().startsWith("http")) {
                imageStorageService.deleteImage(product.getImageUrl());
            }

            // Guardar nueva imagen
            String imageUrl = imageStorageService.storeImage(image, "products");
            product.setImageUrl(imageUrl);
            
            Product updatedProduct = productService.updateProduct(id, product);
            return ResponseEntity.ok(updatedProduct);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al subir imagen: " + e.getMessage());
        }
    }

    // Endpoint para obtener todas las categorías
    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryRepository.findAll());
    }

    // Endpoint para crear una nueva categoría
    @PostMapping("/categories")
    public ResponseEntity<?> createCategory(@RequestBody Category category) {
        try {
            if (categoryRepository.existsByName(category.getName())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La categoría ya existe");
            }
            Category createdCategory = categoryRepository.save(category);
            return ResponseEntity.ok(createdCategory);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al crear categoría: " + e.getMessage());
        }
    }

    // Endpoint para subir imagen de categoría
    @PostMapping("/categories/{id}/upload-image")
    public ResponseEntity<?> uploadCategoryImage(
            @PathVariable UUID id,
            @RequestParam("image") MultipartFile image) {
        try {
            Optional<Category> categoryOpt = categoryRepository.findById(id);
            if (!categoryOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Categoría no encontrada");
            }

            Category category = categoryOpt.get();
            
            // Si hay una imagen anterior local, eliminarla
            if (category.getImageUrl() != null && !category.getImageUrl().startsWith("http")) {
                imageStorageService.deleteImage(category.getImageUrl());
            }

            // Guardar nueva imagen
            String imageUrl = imageStorageService.storeImage(image, "categories");
            category.setImageUrl(imageUrl);
            
            Category updatedCategory = categoryRepository.save(category);
            return ResponseEntity.ok(updatedCategory);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al subir imagen: " + e.getMessage());
        }
    }

    // Endpoint para actualizar imágenes de productos de prueba (sin autenticación)
    @PutMapping("/update-test-images")
    public ResponseEntity<?> updateTestProductImages() {
        try {
            List<Product> activeProducts = productService.getActiveProducts(ProductStatus.ACTIVE);
            List<Product> updatedProducts = new ArrayList<>();
            
            for (Product product : activeProducts) {
                if (product.getName().equals("Laptop")) {
                    product.setImageUrl("/images/laptop.svg");
                } else if (product.getName().equals("Teléfono")) {
                    product.setImageUrl("/images/phone.svg");
                } else if (product.getName().equals("Auriculares")) {
                    product.setImageUrl("/images/headphones.svg");
                } else if (product.getName().equals("Cámara")) {
                    product.setImageUrl("/images/camera.svg");
                }
                updatedProducts.add(productService.updateProduct(product.getId(), product));
            }
            
            return ResponseEntity.ok(updatedProducts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al actualizar imágenes: " + e.getMessage());
        }
    }
    // Nueva funcionalidad: Endpoint para agregar un comentario a un producto
    @PostMapping("/{productId}/comments")
    public ResponseEntity<ProductComment> addComment(
            @PathVariable UUID productId,
            @RequestParam UUID userId,
            @RequestParam String commentText) {

        try {
            ProductComment comment = productCommentService.addCommentToProduct(productId, userId, commentText);
            return ResponseEntity.ok(comment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // Nueva funcionalidad: Endpoint para obtener comentarios de un producto
    @GetMapping("/{productId}/comments")
    public ResponseEntity<List<ProductComment>> getCommentsByProduct(@PathVariable UUID productId) {
        List<ProductComment> comments = productCommentService.getCommentsByProduct(productId);
        return ResponseEntity.ok(comments);
    }

    //Endpoint para agregar un "like" a un producto
    @PostMapping("/{productId}/likes")
    public ResponseEntity<String> addLike(
            @PathVariable UUID productId,
            @RequestParam UUID userId) {

        boolean liked = productLikeService.addLikeToProduct(productId, userId);
        if (liked) {
            return ResponseEntity.ok("Like agregado exitosamente.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El usuario ya ha dado like a este producto.");
        }
    }

    // Endpoint para obtener la cantidad de "likes" de un producto
    @GetMapping("/{productId}/likes/count")
    public ResponseEntity<Long> getProductLikesCount(@PathVariable UUID productId) {
        long likeCount = productLikeService.getLikesCountByProduct(productId);
        return ResponseEntity.ok(likeCount);
    }
}
