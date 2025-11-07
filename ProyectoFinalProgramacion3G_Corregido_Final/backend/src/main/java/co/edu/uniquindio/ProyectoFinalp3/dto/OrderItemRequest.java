/**
 * DTO de item de orden.
 * Recibe producto, cantidad y precio para crear el detalle.
 */
package co.edu.uniquindio.ProyectoFinalp3.dto;

import java.util.UUID;

import co.edu.uniquindio.ProyectoFinalp3.models.Product;

public class OrderItemRequest {
    private UUID productId;
    private int quantity;

    public UUID getProductId() {
        return productId;
    }

    public OrderItemRequest(UUID productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


}

