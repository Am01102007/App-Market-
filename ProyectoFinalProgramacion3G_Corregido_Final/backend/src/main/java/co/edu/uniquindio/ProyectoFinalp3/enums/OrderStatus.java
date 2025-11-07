/**
 * Enum de estado de orden.
 * Define etapas del ciclo de vida de la compra.
 */
package co.edu.uniquindio.ProyectoFinalp3.enums;

/**
 * Enumeración que define los posibles estados de una orden en el sistema.
 * 
 * Esta enumeración garantiza que las órdenes solo puedan tener estados
 * válidos y predefinidos, mejorando la integridad de los datos y
 * facilitando el control del flujo de órdenes.
 * 
 * Estados disponibles:
 * - PENDING: Orden creada pero pendiente de confirmación
 * - CONFIRMED: Orden confirmada y en proceso
 * - SHIPPED: Orden enviada al cliente
 * - DELIVERED: Orden entregada exitosamente
 * - CANCELLED: Orden cancelada
 * 
 * @author Sistema App Market
 * @version 1.0
 * @since 2024
 */
public enum OrderStatus {
    
    /**
     * Estado inicial de una orden recién creada.
     * La orden está pendiente de confirmación o pago.
     */
    PENDING("Pendiente"),
    
    /**
     * La orden ha sido confirmada y está siendo procesada.
     * El pago ha sido verificado y se procede con el pedido.
     */
    CONFIRMED("Confirmada"),
    
    /**
     * La orden ha sido enviada al cliente.
     * Los productos están en tránsito hacia la dirección de entrega.
     */
    SHIPPED("Enviada"),
    
    /**
     * La orden ha sido entregada exitosamente al cliente.
     * Estado final para órdenes completadas satisfactoriamente.
     */
    DELIVERED("Entregada"),
    
    /**
     * La orden ha sido cancelada.
     * Puede ser cancelada por el cliente o por el sistema.
     */
    CANCELLED("Cancelada");
    
    /**
     * Descripción legible del estado en español.
     */
    private final String descripcion;
    
    /**
     * Constructor del enum.
     * 
     * @param descripcion Descripción legible del estado
     */
    OrderStatus(String descripcion) {
        this.descripcion = descripcion;
    }
    
    /**
     * Obtiene la descripción legible del estado.
     * 
     * @return Descripción en español del estado de la orden
     */
    public String getDescripcion() {
        return descripcion;
    }
    
    /**
     * Verifica si el estado actual permite transición al estado objetivo.
     * 
     * @param targetStatus Estado objetivo al que se quiere transicionar
     * @return true si la transición es válida, false en caso contrario
     */
    public boolean canTransitionTo(OrderStatus targetStatus) {
        switch (this) {
            case PENDING:
                return targetStatus == CONFIRMED || targetStatus == CANCELLED;
            case CONFIRMED:
                return targetStatus == SHIPPED || targetStatus == CANCELLED;
            case SHIPPED:
                return targetStatus == DELIVERED;
            case DELIVERED:
            case CANCELLED:
                return false; // Estados finales, no permiten transiciones
            default:
                return false;
        }
    }
    
    /**
     * Verifica si el estado es un estado final.
     * 
     * @return true si es un estado final (DELIVERED o CANCELLED)
     */
    public boolean isFinalStatus() {
        return this == DELIVERED || this == CANCELLED;
    }
}
