package com.bebida.app.dao;

import com.bebida.app.entity.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioDao {
    List<Usuario> findAll();
    Optional<Usuario> findById(Long id);
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByDni(String dni);
    void save(Usuario usuario);
    void deleteById(Long id);
    boolean existsById(Long id);
}