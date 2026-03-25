package com.bebida.app.service;

import com.bebida.app.dao.PagoDao;
import com.bebida.app.dao.VentaDao;
import com.bebida.app.dto.PagoDTO;
import com.bebida.app.dto.PagoRequestDTO;
import com.bebida.app.entity.MetodoPagoEnum;
import com.bebida.app.entity.Pago;
import com.bebida.app.entity.Venta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PagoService {
    
    @Autowired
    private PagoDao pagoDao;
    
    @Autowired
    private VentaDao ventaDao;
    
    @Transactional
    public void crear(PagoRequestDTO pagoRequest) {
        Venta venta = ventaDao.findById(pagoRequest.getIdVenta())
                .orElseThrow(() -> new RuntimeException("Venta no encontrada para el pago"));
        
        Pago pago = new Pago();
        pago.setVenta(venta);
        pago.setMonto(pagoRequest.getMonto());
        
        try {
            pago.setMetodoPago(MetodoPagoEnum.valueOf(pagoRequest.getMetodoPago()));
        } catch (Exception e) {
            throw new RuntimeException("Método de pago inválido");
        }
        
        pagoDao.save(pago);
    }

    

    @Transactional(readOnly = true)
    public List<PagoDTO> obtenerTodos() {
        return pagoDao.findAllOrderByFechaDesc().stream()
                .map(this::convertirADTO) 
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PagoDTO> obtenerPorVenta(Long idVenta) {
        return pagoDao.findByVentaId(idVenta).stream()
                .map(this::convertirADTO) 
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PagoDTO obtenerPorId(Long id) {
        Pago pago = pagoDao.findAllOrderByFechaDesc().stream()
                .filter(p -> p.getIdPago().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));
        return convertirADTO(pago); 
    }
    
    
    private PagoDTO convertirADTO(Pago pago) {
        PagoDTO dto = new PagoDTO();
        dto.setIdPago(pago.getIdPago());
        dto.setMonto(pago.getMonto());
        dto.setFecha(pago.getFecha());
        if(pago.getMetodoPago() != null) {
            dto.setMetodoPago(pago.getMetodoPago().toString());
        }
        if (pago.getVenta() != null) {
            dto.setIdVenta(pago.getVenta().getIdVenta());
        }
        return dto;
    }
}