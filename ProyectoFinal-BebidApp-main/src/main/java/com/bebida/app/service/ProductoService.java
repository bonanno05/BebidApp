package com.bebida.app.service;

import com.bebida.app.dao.ProductoDao;
import com.bebida.app.dto.ProductoDTO;
import com.bebida.app.entity.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductoService {
    
    @Autowired
    private ProductoDao productoDao;
    
    
    @Transactional(readOnly = true)
    public List<ProductoDTO> obtenerTodos() {
        return productoDao.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    
    @Transactional(readOnly = true)
    public List<ProductoDTO> obtenerDisponibles() {
        return productoDao.findProductosDisponibles().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<ProductoDTO> obtenerPorCategoria(String categoria) {
        return productoDao.findByCategoria(categoria).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    
    @Transactional
    public ProductoDTO crear(ProductoDTO productoDTO) {
        return guardar(productoDTO);
    }

    
    @Transactional
    public ProductoDTO actualizar(Long id, ProductoDTO productoDTO) {
        productoDTO.setIdProducto(id); // Aseguramos el ID
        return guardar(productoDTO);
    }

    
    private ProductoDTO guardar(ProductoDTO productoDTO) {
        Producto producto = new Producto();
        if (productoDTO.getIdProducto() != null) {
            producto = productoDao.findById(productoDTO.getIdProducto()).orElse(new Producto());
        }
        
        producto.setNombre(productoDTO.getNombre());
        producto.setDescripcion(productoDTO.getDescripcion());
        producto.setCategoria(productoDTO.getCategoria());
        producto.setPrecioUnitario(productoDTO.getPrecioUnitario());
        producto.setImagenUrl(productoDTO.getImagenUrl());
        producto.setStock(productoDTO.getStock());
        
        productoDao.save(producto);
        return convertirADTO(producto);
    }
    
    @Transactional
    public void actualizarStock(Long id, int cantidad) {
        Producto p = productoDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        p.setStock(cantidad); 
        productoDao.save(p);
    }

    @Transactional(readOnly = true)
    public List<ProductoDTO> ordenarPorPrecio(String orden) {
        List<Producto> lista;
        if ("asc".equalsIgnoreCase(orden)) {
            lista = productoDao.findAllByOrderByPrecioUnitarioAsc();
        } else {
            lista = productoDao.findAllByOrderByPrecioUnitarioDesc();
        }
        return lista.stream().map(this::convertirADTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductoDTO obtenerPorId(Long id) {
        Producto producto = productoDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        return convertirADTO(producto);
    }
    
    @Transactional
    public void eliminar(Long id) {
        productoDao.deleteById(id);
    }
    
    private ProductoDTO convertirADTO(Producto producto) {
        ProductoDTO dto = new ProductoDTO();
        dto.setIdProducto(producto.getIdProducto());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setCategoria(producto.getCategoria());
        dto.setPrecioUnitario(producto.getPrecioUnitario());
        dto.setImagenUrl(producto.getImagenUrl());
        dto.setStock(producto.getStock());
        return dto;
    }
}