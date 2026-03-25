package com.bebida.app.dao;

import com.bebida.app.entity.Rol;
import com.bebida.app.entity.Tipo_usuario_enum;
import java.util.Optional;

public interface RolDao {
    Optional<Rol> findById(Long id);
    Optional<Rol> findByRol(Tipo_usuario_enum rol);
    boolean existsByRol(String rol);
    void save(Rol rol);
}