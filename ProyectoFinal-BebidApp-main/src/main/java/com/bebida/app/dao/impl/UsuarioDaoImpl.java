package com.bebida.app.dao.impl;

import com.bebida.app.dao.UsuarioDao;
import com.bebida.app.entity.Usuario;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class UsuarioDaoImpl implements UsuarioDao {

    @Autowired
    private SessionFactory sessionFactory;

    // Método auxiliar para obtener la sesión actual
    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public List<Usuario> findAll() {
        // HQL (Hibernate Query Language)
        return getCurrentSession().createQuery("from Usuario", Usuario.class).list();
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        Usuario usuario = getCurrentSession().get(Usuario.class, id);
        return Optional.ofNullable(usuario);
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        String hql = "SELECT DISTINCT u FROM Usuario u " +
                     "LEFT JOIN FETCH u.usuarioRoles ur " +
                     "LEFT JOIN FETCH ur.rol r " +
                     "WHERE u.email = :email";
                     
        return sessionFactory.getCurrentSession()
                .createQuery(hql, Usuario.class)
                .setParameter("email", email)
                .uniqueResultOptional();
    }
    
    @Override
    public Optional<Usuario> findByDni(String dni) {
        Query<Usuario> query = getCurrentSession().createQuery("FROM Usuario u WHERE u.dni = :dni", Usuario.class);
        query.setParameter("dni", dni);
        return query.uniqueResultOptional();
    }

    @Override
    public void save(Usuario usuario) {
        getCurrentSession().saveOrUpdate(usuario);
    }

    @Override
    public void deleteById(Long id) {
        Usuario usuario = getCurrentSession().get(Usuario.class, id);
        if (usuario != null) {
            getCurrentSession().delete(usuario);
        }
    }
    
    @Override
    public boolean existsById(Long id) {
        Usuario usuario = getCurrentSession().get(Usuario.class, id);
        return usuario != null;
    }
}
