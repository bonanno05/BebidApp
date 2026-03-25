package com.bebida.app.dao.impl;

import com.bebida.app.dao.ProductoDao;
import com.bebida.app.entity.Producto;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductosDaoImpl implements ProductoDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Producto> findAll() {
        return sessionFactory.getCurrentSession().createQuery("FROM Producto", Producto.class).list();
    }

    @Override
    public Optional<Producto> findById(Long id) {
        return Optional.ofNullable(sessionFactory.getCurrentSession().get(Producto.class, id));
    }

    @Override
    public void save(Producto producto) {
        sessionFactory.getCurrentSession().saveOrUpdate(producto);
    }

    @Override
    public void deleteById(Long id) {
        Producto p = sessionFactory.getCurrentSession().get(Producto.class, id);
        if (p != null) sessionFactory.getCurrentSession().delete(p);
    }

    @Override
    public List<Producto> findByCategoria(String categoria) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Producto p WHERE p.categoria = :categoria", Producto.class)
                .setParameter("categoria", categoria)
                .list();
    }

    @Override
    public List<Producto> findByStockGreaterThan(int stock) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Producto p WHERE p.stock > :stock", Producto.class)
                .setParameter("stock", stock)
                .list();
    }

    @Override
    public List<Producto> findAllByOrderByPrecioUnitarioAsc() {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Producto p ORDER BY p.precioUnitario ASC", Producto.class)
                .list();
    }

    @Override
    public List<Producto> findAllByOrderByPrecioUnitarioDesc() {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Producto p ORDER BY p.precioUnitario DESC", Producto.class)
                .list();
    }

    @Override
    public List<Producto> findProductosDisponibles() {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Producto p WHERE p.stock > 0", Producto.class)
                .list();
    }
}
