package com.bebida.app.dao;

import com.bebida.app.entity.Rol;
import com.bebida.app.entity.Usuario;
import com.bebida.app.entity.UsuarioRol;
import java.util.List;

public interface UsuarioRolDao {
    void save(UsuarioRol usuarioRol);
    List<UsuarioRol> findByUsuario(Usuario usuario);
    List<UsuarioRol> findByRol(Rol rol);
    void deleteByUsuario(Usuario usuario);
    void deleteByRol(Rol rol);
    void deleteAll(List<UsuarioRol> roles); // Útil para limpiar roles antiguos
}
