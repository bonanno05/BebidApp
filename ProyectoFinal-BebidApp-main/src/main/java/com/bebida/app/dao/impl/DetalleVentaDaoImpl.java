package com.bebida.app.dao.impl;

import com.bebida.app.dao.DetalleVentaDao;
import com.bebida.app.entity.Detalle_venta;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class DetalleVentaDaoImpl implements DetalleVentaDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void save(Detalle_venta detalle) {
        sessionFactory.getCurrentSession().saveOrUpdate(detalle);
    }
    
    @Override
    public void saveAll(List<Detalle_venta> detalles) {
        for (Detalle_venta d : detalles) {
            save(d);
        }
    }

    @Override
    public List<Detalle_venta> findByVentaId(Long idVenta) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Detalle_venta dv WHERE dv.venta.idVenta = :idVenta", Detalle_venta.class)
                .setParameter("idVenta", idVenta)
                .list();
    }

    @Override
    public List<Detalle_venta> findByProductoId(Long idProducto) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Detalle_venta dv WHERE dv.producto.idProducto = :idProducto", Detalle_venta.class)
                .setParameter("idProducto", idProducto)
                .list();
    }

    @Override
    public List<Object[]> findProductosMasVendidos() {
        String hql = "SELECT dv.producto.id, SUM(dv.cantidad) as totalVendido " +
                     "FROM Detalle_venta dv " +
                     "GROUP BY dv.producto.id " +
                     "ORDER BY totalVendido DESC";
                     
        return sessionFactory.getCurrentSession()
                .createQuery(hql, Object[].class)
                .list();
    }

    @Override
    public List<Object[]> findVentasPorProductoEnPeriodo(Date inicio, Date fin) {
        String hql = "SELECT dv.producto.id, SUM(dv.cantidad * dv.precioUnitario) " +
                     "FROM Detalle_venta dv " +
                     "WHERE dv.venta.fecha BETWEEN :inicio AND :fin " +
                     "GROUP BY dv.producto.id";
                     
        return sessionFactory.getCurrentSession()
                .createQuery(hql, Object[].class)
                .setParameter("inicio", inicio)
                .setParameter("fin", fin)
                .list();
    }
}
