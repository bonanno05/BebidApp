package com.bebida.app.dao;

import com.bebida.app.entity.Venta;
import java.util.List;
import java.util.Optional;

public interface VentaDao {
    Venta save(Venta venta); 
    Optional<Venta> findById(Long id);
    List<Venta> findAll();
    
    
    List<Venta> findByClienteIdOrderByFechaDesc(Long idCliente);
}