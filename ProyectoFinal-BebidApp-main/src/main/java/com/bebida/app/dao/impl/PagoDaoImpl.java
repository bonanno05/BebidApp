package com.bebida.app.dao.impl;

import com.bebida.app.dao.PagoDao;
import com.bebida.app.entity.MetodoPagoEnum;
import com.bebida.app.entity.Pago;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;

@Repository
public class PagoDaoImpl implements PagoDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void save(Pago pago) {
        sessionFactory.getCurrentSession().saveOrUpdate(pago);
    }

    @Override
    public List<Pago> findByVentaId(Long idVenta) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Pago p WHERE p.venta.idVenta = :idVenta ORDER BY p.fecha DESC", Pago.class)
                .setParameter("idVenta", idVenta)
                .list();
    }

    @Override
    public List<Pago> findAllOrderByFechaDesc() {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Pago p ORDER BY p.fecha DESC", Pago.class)
                .list();
    }

    @Override
    public List<Pago> findByMetodoPago(MetodoPagoEnum metodoPago) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Pago p WHERE p.metodoPago = :metodo ORDER BY p.fecha DESC", Pago.class)
                .setParameter("metodo", metodoPago)
                .list();
    }

    @Override
    public List<Pago> findByFechaBetween(Date inicio, Date fin) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Pago p WHERE p.fecha BETWEEN :inicio AND :fin", Pago.class)
                .setParameter("inicio", inicio)
                .setParameter("fin", fin)
                .list();
    }

    @Override
    public Double calcularTotalPagadoEnPeriodo(Date inicio, Date fin) {
        return sessionFactory.getCurrentSession()
                .createQuery("SELECT SUM(p.monto) FROM Pago p WHERE p.fecha BETWEEN :inicio AND :fin", Double.class)
                .setParameter("inicio", inicio)
                .setParameter("fin", fin)
                .uniqueResult();
    }
}
