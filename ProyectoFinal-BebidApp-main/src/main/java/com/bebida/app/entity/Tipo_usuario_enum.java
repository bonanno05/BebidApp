package com.bebida.app.entity;

import lombok.Getter;

@Getter
public enum Tipo_usuario_enum {

    Ceo("Ceo"),
    Administrador("Administrador"),
    Auditor("Auditor"),
    Cliente("Cliente"),
    Vendedor("Vendedor");
    

    private final String descripcion;
    
    Tipo_usuario_enum(String descripcion){
        this.descripcion = descripcion;
    }
}
