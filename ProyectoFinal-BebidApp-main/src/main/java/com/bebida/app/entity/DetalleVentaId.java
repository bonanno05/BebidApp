package com.bebida.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor

public class DetalleVentaId implements Serializable{
    @Column (name = "id_venta", nullable = false)
    private Long idVenta;
           
    @Column (name = "id_producto", nullable = false)
    private Long idProducto;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DetalleVentaId that = (DetalleVentaId) o;
        return Objects.equals(idVenta, that.idVenta) && 
               Objects.equals(idProducto, that.idProducto);
    }
    
    @Override   
    public int hashCode() {
        return Objects.hash(idVenta, idProducto);
    }
    
}
