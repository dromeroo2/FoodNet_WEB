package com.mycompany.foodnet.modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Conexion {
    
    // Aquí defino los datos de configuración de mi base de datos Derby (puerto 1527).
    // Los pongo como constantes para que sea fácil cambiarlos si hace falta.
    private static final String URL = "jdbc:derby://localhost:1527/foodnet_db";
    private static final String USER = "app";
    private static final String PASSWORD = "app";
    private static final String DRIVER = "org.apache.derby.jdbc.ClientDriver";
    
    private Connection cx;

    public Conexion() {
    }

    public Connection conectar() {
        try {
            // Cargo el driver de Derby para que Java sepa cómo "hablar" con la BBDD
            Class.forName(DRIVER);
            // Intento conectar usando la URL y las credenciales de arriba
            cx = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("CONEXION EXITOSA A LA BASE DE DATOS");
        } catch (ClassNotFoundException | SQLException ex) {
            // Si falla (por ejemplo, si se me olvida arrancar el servidor de Java DB), lo pinto en consola
            System.out.println("FALLO EN LA CONEXION");
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cx;
    }

    public void desconectar() {
        try {
            // Es importante cerrar la conexión cuando terminamos para liberar recursos del servidor
            if (cx != null && !cx.isClosed()) {
                cx.close();
                System.out.println("DESCONEXION EXITOSA");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // Dejo este método main aquí porque lo usé para probar si conectaba bien antes de hacer la web
    // No se usa en la aplicación real, pero viene bien para testear
    public static void main(String[] args) {
        Conexion conexion = new Conexion();
        conexion.conectar();
    }
}