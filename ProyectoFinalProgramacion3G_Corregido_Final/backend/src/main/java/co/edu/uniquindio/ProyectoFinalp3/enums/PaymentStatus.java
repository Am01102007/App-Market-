/**
 * Enum de estado de pago.
 * Representa el progreso y resultado de una transacción.
 */
package co.edu.uniquindio.ProyectoFinalp3.enums;

/**
 * Enumeración que define los posibles estados de un pago en el sistema.
 * 
 * Esta enumeración garantiza que los pagos solo puedan tener estados
 * válidos y predefinidos, mejorando la integridad de los datos y
 * facilitando el control del flujo de pagos.
 * 
 * Estados disponibles:
 * - PENDING: Pago iniciado pero pendiente de procesamiento
 * - COMPLETED: Pago procesado exitosamente
 * - FAILED: Pago falló durante el procesamiento
 * - REFUNDED: Pago reembolsado al cliente
 * 
 * @author Sistema App Market
 * @version 1.0
 * @since 2024
 */
public enum PaymentStatus {
    
    /**
     * Estado inicial de un pago recién creado.
     * El pago está pendiente de procesamiento por el proveedor de pagos.
     */
    PENDING("Pendiente"),
    
    /**
     * El pago ha sido procesado exitosamente.
     * Los fondos han sido transferidos correctamente.
     */
    COMPLETED("Completado"),
    
    /**
     * El pago falló durante el procesamiento.
     * Puede ser debido a fondos insuficientes, tarjeta rechazada, etc.
     */
    FAILED("Fallido"),
    
    /**
     * El pago ha sido reembolsado al cliente.
     * Los fondos han sido devueltos a la cuenta original.
     */
    REFUNDED("Reembolsado");
    
    /**
     * Descripción legible del estado en español.
     */
    private final String descripcion;
    
    /**
     * Constructor del enum.
     * 
     * @param descripcion Descripción legible del estado
     */
    PaymentStatus(String descripcion) {
        this.descripcion = descripcion;
    }
    
    /**
     * Obtiene la descripción legible del estado.
     * 
     * @return Descripción en español del estado del pago
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
    public boolean canTransitionTo(PaymentStatus targetStatus) {
        switch (this) {
            case PENDING:
                return targetStatus == COMPLETED || targetStatus == FAILED;
            case COMPLETED:
                return targetStatus == REFUNDED;
            case FAILED:
            case REFUNDED:
                return false; // Estados finales, no permiten transiciones
            default:
                return false;
        }
    }
    
    /**
     * Verifica si el estado es un estado final.
     * 
     * @return true si es un estado final (COMPLETED, FAILED o REFUNDED)
     */
    public boolean isFinalStatus() {
        return this == COMPLETED || this == FAILED || this == REFUNDED;
    }
    
    /**
     * Verifica si el estado indica un pago exitoso.
     * 
     * @return true si el pago fue completado exitosamente
     */
    public boolean isSuccessful() {
        return this == COMPLETED;
    }
}
