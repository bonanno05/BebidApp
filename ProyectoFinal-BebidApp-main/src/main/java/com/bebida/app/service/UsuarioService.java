package com.bebida.app.service;

import com.bebida.app.dao.RolDao;
import com.bebida.app.dao.UsuarioDao;
import com.bebida.app.dao.UsuarioRolDao;
import com.bebida.app.dto.UsuarioDTO;
import com.bebida.app.entity.Rol;
import com.bebida.app.entity.Tipo_usuario_enum;
import com.bebida.app.entity.Usuario;
import com.bebida.app.entity.UsuarioRol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {
    
    @Autowired
    private UsuarioDao usuarioDao;
    
    @Autowired
    private RolDao rolDao;
    
    @Autowired
    private UsuarioRolDao usuarioRolDao;
    
    @Transactional(readOnly = true)
    public List<UsuarioDTO> obtenerTodos() {
        return usuarioDao.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<UsuarioDTO> obtenerClientesYVendedores() {
        // Obtenemos todos, convertimos a DTO y filtramos los que NO son Administrador
        return usuarioDao.findAll().stream()
                .map(this::convertirADTO)
                .filter(dto -> !dto.getRol().equals("Administrador"))
                .filter(dto -> !dto.getRol().equals("Ceo"))
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public UsuarioDTO obtenerPorDni(String dni) {
        Usuario usuario = usuarioDao.findByDni(dni)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con dni: " + dni));
        return convertirADTO(usuario);
    }
    
    @Transactional(readOnly = true)
    public UsuarioDTO obtenerPorEmail(String email) {
        Usuario usuario = usuarioDao.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));
        return convertirADTO(usuario);
    }
    

    @Transactional
    public UsuarioDTO crear(UsuarioDTO usuarioDTO) {
        validarUsuario(usuarioDTO);
        Usuario usuario = convertirAEntidad(usuarioDTO);
        
        // 1. Guardamos al Usuario en la tabla 'usuarios'
        usuarioDao.save(usuario);
        
        
        Rol rolCliente = rolDao.findByRol(Tipo_usuario_enum.Cliente)
                .orElseThrow(() -> new RuntimeException("Error fatal: El rol 'Cliente' no existe en la base de datos."));
        
        
        UsuarioRol nuevoUsuarioRol = new UsuarioRol();
        nuevoUsuarioRol.setUsuario(usuario); // Vinculamos al usuario
        nuevoUsuarioRol.setRol(rolCliente);  // Vinculamos al rol
        
        
        usuarioRolDao.save(nuevoUsuarioRol);
        
        
        usuario.setUsuarioRoles(List.of(nuevoUsuarioRol));
        
        return convertirADTO(usuario);
    }
    
    
    @Transactional
    public UsuarioDTO actualizar(String dni, UsuarioDTO usuarioDTO) {
        Usuario existente = usuarioDao.findByDni(dni)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con dni: " + dni));
        
        actualizarDatos(existente, usuarioDTO);
        usuarioDao.save(existente);
        return convertirADTO(existente);
    }
    
    @Transactional
    public void eliminar(Long id) {
        if (!usuarioDao.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con id: " + id);
        }
        // Primero borramos sus roles para evitar error de Foreign Key
        Usuario usuario = usuarioDao.findById(id).get();
        usuarioRolDao.deleteByUsuario(usuario);
        
        usuarioDao.deleteById(id);
    }
    
    @Transactional
    public void cambiarRol(Long idUsuario, String nombreRol) {
        Usuario usuario = usuarioDao.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Tipo_usuario_enum rolEnum;
        try {
            rolEnum = Tipo_usuario_enum.valueOf(nombreRol); 
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Rol no válido: " + nombreRol);
        }

        Rol rolNuevo = rolDao.findByRol(rolEnum)
                 .orElseThrow(() -> new RuntimeException("Rol no encontrado en BD: " + nombreRol));

        usuarioRolDao.deleteByUsuario(usuario);

        UsuarioRol nuevoUsuarioRol = new UsuarioRol();
        nuevoUsuarioRol.setUsuario(usuario);
        nuevoUsuarioRol.setRol(rolNuevo);
        usuarioRolDao.save(nuevoUsuarioRol);
    }
    
    private void validarUsuario(UsuarioDTO dto) {
        if (dto.getEmail() == null || dto.getEmail().isEmpty()) {
            throw new RuntimeException("El email es obligatorio");
        }
        if (dto.getDni() == null || dto.getDni().isEmpty()) {
            throw new RuntimeException("El DNI es obligatorio");
        }
    }
    
    private void actualizarDatos(Usuario usuario, UsuarioDTO dto) {
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setTelefono(dto.getTelefono());
        usuario.setFechaNacimiento(dto.getFechaNacimiento());
    }
    
    private UsuarioDTO convertirADTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setDni(usuario.getDni());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setEmail(usuario.getEmail());
        dto.setTelefono(usuario.getTelefono());
        dto.setFechaNacimiento(usuario.getFechaNacimiento());
        dto.setTotalCompras(0);
        dto.setTotalGastado(0.0);
        
        if (usuario.getUsuarioRoles() != null && !usuario.getUsuarioRoles().isEmpty()) {
            dto.setRol(usuario.getUsuarioRoles().get(0).getRol().getRol().toString());
        } else {
            dto.setRol("SIN ROL");
        }
        return dto;
    }
    
    private Usuario convertirAEntidad(UsuarioDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setDni(dto.getDni());
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setEmail(dto.getEmail());
        usuario.setContrasena(dto.getContrasena());
        usuario.setTelefono(dto.getTelefono());
        usuario.setFechaNacimiento(dto.getFechaNacimiento());
        return usuario;
    }
}