package com.bebida.app.dao.impl;

import com.bebida.app.dao.UsuarioRolDao;
import com.bebida.app.entity.Rol;
import com.bebida.app.entity.Usuario;
import com.bebida.app.entity.UsuarioRol;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UsuarioRolDaoImpl implements UsuarioRolDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void save(UsuarioRol usuarioRol) {
        sessionFactory.getCurrentSession().saveOrUpdate(usuarioRol);
    }

    @Override
    public List<UsuarioRol> findByUsuario(Usuario usuario) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM UsuarioRol ur WHERE ur.usuario = :usuario", UsuarioRol.class)
                .setParameter("usuario", usuario)
                .list();
    }

    @Override
    public List<UsuarioRol> findByRol(Rol rol) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM UsuarioRol ur WHERE ur.rol = :rol", UsuarioRol.class)
                .setParameter("rol", rol)
                .list();
    }

    @Override
    public void deleteByUsuario(Usuario usuario) {
        sessionFactory.getCurrentSession()
                .createQuery("DELETE FROM UsuarioRol ur WHERE ur.usuario = :usuario")
                .setParameter("usuario", usuario)
                .executeUpdate();
    }

    @Override
    public void deleteByRol(Rol rol) {
        sessionFactory.getCurrentSession()
                .createQuery("DELETE FROM UsuarioRol ur WHERE ur.rol = :rol")
                .setParameter("rol", rol)
                .executeUpdate();
    }
    
    @Override
    public void deleteAll(List<UsuarioRol> roles) {
        for (UsuarioRol rol : roles) {
            sessionFactory.getCurrentSession().delete(rol);
        }
    }
}