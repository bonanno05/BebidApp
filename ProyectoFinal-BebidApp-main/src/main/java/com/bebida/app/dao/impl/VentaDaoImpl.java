package com.bebida.app.dao.impl;

import com.bebida.app.dao.VentaDao;
import com.bebida.app.entity.Venta;
import org.hibernate.Hibernate; 
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class VentaDaoImpl implements VentaDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Venta save(Venta venta) {
        sessionFactory.getCurrentSession().saveOrUpdate(venta);
        return venta;
    }

    @Override
    public Optional<Venta> findById(Long id) {
        return Optional.ofNullable(sessionFactory.getCurrentSession().get(Venta.class, id));
    }
    
    @Override
    public List<Venta> findAll() {
        return sessionFactory.getCurrentSession().createQuery("FROM Venta", Venta.class).list();
    }

    @Override
    public List<Venta> findByClienteIdOrderByFechaDesc(Long idCliente) {
        // 1. Traemos Venta + Pagos + Cliente
        String hql = "SELECT DISTINCT v FROM Venta v " +
                     "LEFT JOIN FETCH v.pagos " +
                     "LEFT JOIN FETCH v.cliente " +
                     "WHERE v.cliente.idUsuario = :idCliente " +
                     "ORDER BY v.fecha DESC";
        
        List<Venta> ventas = sessionFactory.getCurrentSession()
                .createQuery(hql, Venta.class)
                .setParameter("idCliente", idCliente)
                .list();
        
        // 2. Inicializamos los DETALLES y los PRODUCTOS dentro de ellos
        for (Venta v : ventas) {
            // Inicializamos la lista de detalles
            Hibernate.initialize(v.getDetalles()); 
            
            
            for (com.bebida.app.entity.Detalle_venta detalle : v.getDetalles()) {
                Hibernate.initialize(detalle.getProducto());
            }
           
        }
        
        return ventas;
    }
}