DROP database IF EXISTS Bebidaap;
CREATE database Bebidaap;
USE Bebidaap;

CREATE TABLE usuarios(
	id_usuario INT, 
    dni INT NOT NULL UNIQUE,
    nombre VARCHAR(15) NOT NULL,
    apellido VARCHAR(15) NOT NULL,
    email VARCHAR(30) NOT NULL UNIQUE,
    telefono VARCHAR(15) NOT NULL UNIQUE,
    genero enum('Administrador','Vendedor','Cliente') NOT NULL,
    fecha_nacimiento DATE NOT NULL
);

ALTER TABLE usuarios
MODIFY id_usuario INT AUTO_INCREMENT PRIMARY KEY;

CREATE TABLE productos(
	id_producto INT,
    nombre VARCHAR(20) NOT NULL,
    descripcion VARCHAR(50) NOT NULL,
    categoria VARCHAR(20) NOT NULL,
    stock INT NOT NULL
    check (stock >= 0), 
    precio_unitario DECIMAL(10,2) NOT NULL
);

ALTER TABLE productos
MODIFY id_producto INT AUTO_INCREMENT PRIMARY KEY;

CREATE TABLE ventas(
	id_venta INT,
    fecha DATETIME,
    id_cliente INT,
    total DECIMAL(15,2)
);

ALTER TABLE ventas
MODIFY id_venta INT AUTO_INCREMENT PRIMARY KEY;

ALTER TABLE ventas ADD foreign key (id_cliente)
REFERENCES usuarios (id_usuario);


CREATE TABLE detalle_ventas(
	id_venta INT,
    id_producto INT, 
    cantidad INT,
    precio_unitario DECIMAL(10,2),
    subtotal DECIMAL(30,2)
);

ALTER TABLE detalle_ventas ADD PRIMARY KEY (id_venta, id_producto);

ALTER TABLE detalle_ventas ADD foreign key (id_venta)
REFERENCES ventas (id_venta);

ALTER TABLE detalle_ventas ADD foreign key (id_producto)
REFERENCES productos (id_producto);

CREATE TABLE pagos(
	id_pago INT,
    id_venta INT,
    metodo_pago enum('Efectivo', 'Tarjeta Debito', 'Tarjeta Credito', 'Transferencia'),
    monto DECIMAL(30,2),
    recargo_descuento DECIMAL(10,2),
    fecha DATETIME DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE pagos
MODIFY id_pago INT AUTO_INCREMENT PRIMARY KEY;

ALTER TABLE pagos ADD foreign key (id_venta)
REFERENCES ventas (id_venta);