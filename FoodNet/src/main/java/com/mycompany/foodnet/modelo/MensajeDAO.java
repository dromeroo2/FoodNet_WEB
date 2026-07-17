package com.mycompany.foodnet.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MensajeDAO {

    // Método para guardar un mensaje nuevo en la base de datos
    public void enviar(int idEmisor, int idReceptor, String texto) {
        Conexion conexion = new Conexion();
        try {
            Connection cx = conexion.conectar();
            
            // Hago el INSERT normal. 
            // Uso CURRENT_TIMESTAMP directamente en SQL para que la BBDD ponga la hora exacta automáticamente.
            String sql = "INSERT INTO mensajes (id_emisor, id_receptor, texto, fecha_envio) VALUES (?, ?, ?, CURRENT_TIMESTAMP)";
            
            PreparedStatement ps = cx.prepareStatement(sql);
            ps.setInt(1, idEmisor);
            ps.setInt(2, idReceptor);
            ps.setString(3, texto);
            
            ps.executeUpdate();
            
            ps.close(); 
            conexion.desconectar();
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
    }

    // Método para sacar todo el historial de chat con una persona concreta
    public List<Mensaje> obtenerConversacion(int idMio, int idOtro) {
        List<Mensaje> lista = new ArrayList<>();
        Conexion conexion = new Conexion();
        try {
            Connection cx = conexion.conectar();
            
            // Esta consulta tiene truco: necesito los mensajes donde YO envío y ÉL recibe...
            // ...PERO TAMBIÉN los que ÉL envía y YO recibo. Por eso uso el OR y los paréntesis.
            // Además, ordeno por fecha ASC (ascendente) para que la conversación se lea en orden, de antiguo a nuevo.
            String sql = "SELECT * FROM mensajes WHERE (id_emisor = ? AND id_receptor = ?) OR (id_emisor = ? AND id_receptor = ?) ORDER BY fecha_envio ASC";
            
            PreparedStatement ps = cx.prepareStatement(sql);
            // Relleno los interrogantes cruzando los IDs para cubrir los dos casos del OR
            ps.setInt(1, idMio); ps.setInt(2, idOtro);
            ps.setInt(3, idOtro); ps.setInt(4, idMio);
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Mensaje m = new Mensaje();
                m.setId(rs.getInt("id_mensaje"));
                m.setIdEmisor(rs.getInt("id_emisor"));
                m.setIdReceptor(rs.getInt("id_receptor"));
                m.setTexto(rs.getString("texto"));
                try {
                    // Convierto el timestamp de SQL a String para mostrarlo fácil en la vista
                    m.setFecha(rs.getTimestamp("fecha_envio").toString());
                } catch (Exception e) { 
                    m.setFecha(""); 
                }
                lista.add(m);
            }
            rs.close(); 
            ps.close(); 
            conexion.desconectar();
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return lista;
    }
}