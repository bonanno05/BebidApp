package com.bebida.app.dao;

import com.bebida.app.entity.Producto;
import java.util.List;
import java.util.Optional;

public interface ProductoDao {
    List<Producto> findAll();
    Optional<Producto> findById(Long id);
    void save(Producto producto);
    void deleteById(Long id);
    
    
    List<Producto> findByCategoria(String categoria);
    List<Producto> findByStockGreaterThan(int stock);
    List<Producto> findAllByOrderByPrecioUnitarioAsc();
    List<Producto> findAllByOrderByPrecioUnitarioDesc();
    List<Producto> findProductosDisponibles();
}

