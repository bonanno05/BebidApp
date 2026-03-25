package com.bebida.app.dao;

import com.bebida.app.entity.MetodoPagoEnum;
import com.bebida.app.entity.Pago;
import java.util.Date;
import java.util.List;

public interface PagoDao {
    void save(Pago pago);
    
    List<Pago> findByVentaId(Long idVenta);
    List<Pago> findAllOrderByFechaDesc();
    List<Pago> findByMetodoPago(MetodoPagoEnum metodoPago);
    List<Pago> findByFechaBetween(Date inicio, Date fin);
    Double calcularTotalPagadoEnPeriodo(Date inicio, Date fin);
}
