package com.bebida.app.controller;

import com.bebida.app.service.VentaService;
 import com.bebida.app.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ventas/auditor") 
public class AuditorController {

    @Autowired
    private VentaService ventaService;

    @Autowired
     private UsuarioService usuarioService;

    @GetMapping
    public String panelAuditor(Model model) {
        
        // 1. Cargar el historial COMPLETO de ventas
        model.addAttribute("listaVentas", ventaService.obtenerTodos());
        
        // 2. (Opcional) Cargar lista de usuarios para ver quiénes son
        model.addAttribute("listaUsuarios", usuarioService.obtenerTodos());

        return "ventas/auditor"; 
    }
}
