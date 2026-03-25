package com.bebida.app.service;

import com.bebida.app.dao.DetalleVentaDao;
import com.bebida.app.dao.ProductoDao;
import com.bebida.app.dao.UsuarioDao;
import com.bebida.app.dao.VentaDao;
import com.bebida.app.dto.DetalleVentaRequestDTO;
import com.bebida.app.dto.VentaDTO;
import com.bebida.app.dto.VentaRequestDTO;
import com.bebida.app.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VentaService {
    
    @Autowired
    private VentaDao ventaDao;
    
    @Autowired
    private DetalleVentaDao detalleVentaDao;
    
    @Autowired
    private ProductoDao productoDao;
    
    @Autowired
    private UsuarioDao usuarioDao;
    
    @Transactional
    public VentaDTO crear(VentaRequestDTO ventaRequest) {
        // 1. Obtener Cliente
        Usuario cliente = usuarioDao.findById(ventaRequest.getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        
        // 2. Crear Venta Base
        Venta venta = new Venta();
        venta.setCliente(cliente);
        venta.setFecha(LocalDateTime.now());
        venta.setTotal(BigDecimal.ZERO); // Se calcula después
        
        // Guardamos para generar ID
        ventaDao.save(venta);
        
        BigDecimal totalVenta = BigDecimal.ZERO;
        List<Detalle_venta> detallesParaGuardar = new ArrayList<>();
        
        // 3. Procesar Detalles (Productos)
        for (DetalleVentaRequestDTO detReq : ventaRequest.getDetalles()) {
            Producto producto = productoDao.findById(detReq.getIdProducto())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado id: " + detReq.getIdProducto()));
            
            // Validar Stock
            if (producto.getStock() < detReq.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para: " + producto.getNombre());
            }
            
            // Actualizar Stock
            producto.setStock(producto.getStock() - detReq.getCantidad());
            productoDao.save(producto);
            
            // Crear Detalle
            Detalle_venta detalle = new Detalle_venta();
            DetalleVentaId id = new DetalleVentaId(venta.getIdVenta(), producto.getIdProducto());
            detalle.setId(id);
            detalle.setVenta(venta);
            detalle.setProducto(producto);
            detalle.setCantidad(detReq.getCantidad());
            detalle.setPrecioUnitario(producto.getPrecioUnitario());
            
            BigDecimal subtotal = producto.getPrecioUnitario().multiply(BigDecimal.valueOf(detReq.getCantidad()));
            detalle.setSubtotal(subtotal);
            
            detallesParaGuardar.add(detalle);
            totalVenta = totalVenta.add(subtotal);
        }
        
        // 4. Guardar Detalles
        detalleVentaDao.saveAll(detallesParaGuardar);
        
        // 5. Actualizar Total de Venta
        venta.setTotal(totalVenta);
        ventaDao.save(venta);
        
        return convertirADTO(venta);
    }
    
    @Transactional(readOnly = true)
    public VentaDTO obtenerPorId(Long id) {
        Venta venta = ventaDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
        return convertirADTO(venta);
    }
    
    @Transactional(readOnly = true)
    public List<VentaDTO> obtenerTodos() {
        return ventaDao.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    private VentaDTO convertirADTO(Venta venta) {
        VentaDTO dto = new VentaDTO();
        dto.setIdVenta(venta.getIdVenta());
        dto.setFecha(venta.getFecha());
        dto.setTotal(venta.getTotal());
        if(venta.getCliente() != null) {
            dto.setNombreCliente(venta.getCliente().getNombre() + " " + venta.getCliente().getApellido());
        }
        return dto;
    }
    
    @Transactional(readOnly = true)
    public List<Venta> obtenerHistorial(Long idCliente) {
        return ventaDao.findByClienteIdOrderByFechaDesc(idCliente);
    }
}