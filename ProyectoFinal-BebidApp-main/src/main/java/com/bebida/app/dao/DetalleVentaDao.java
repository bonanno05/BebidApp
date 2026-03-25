package com.bebida.app.dao;

import com.bebida.app.entity.Detalle_venta; 
import java.util.Date;
import java.util.List;

public interface DetalleVentaDao {
    void save(Detalle_venta detalle);
    void saveAll(List<Detalle_venta> detalles);
    
    List<Detalle_venta> findByVentaId(Long idVenta);
    List<Detalle_venta> findByProductoId(Long idProducto);
    
    // Reportes (Object[] porque devuelve filas personalizadas, no entidades completas)
    List<Object[]> findProductosMasVendidos();
    List<Object[]> findVentasPorProductoEnPeriodo(Date inicio, Date fin);
}