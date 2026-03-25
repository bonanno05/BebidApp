package com.bebida.app.dao.impl;

import com.bebida.app.dao.RolDao;
import com.bebida.app.entity.Rol;
import com.bebida.app.entity.Tipo_usuario_enum;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class RolDaoImpl implements RolDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Optional<Rol> findById(Long id) {
        return Optional.ofNullable(sessionFactory.getCurrentSession().get(Rol.class, id));
    }

    @Override
    public Optional<Rol> findByRol(Tipo_usuario_enum rol) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Rol r WHERE r.rol = :rol", Rol.class)
                .setParameter("rol", rol)
                .uniqueResultOptional();
    }

    @Override
    public boolean existsByRol(String rolName) {
        Long count = sessionFactory.getCurrentSession()
                .createQuery("SELECT count(r) FROM Rol r WHERE str(r.rol) = :rolName", Long.class)
                .setParameter("rolName", rolName)
                .uniqueResult();
        return count > 0;
    }

    @Override
    public void save(Rol rol) {
        sessionFactory.getCurrentSession().saveOrUpdate(rol);
    }
}
