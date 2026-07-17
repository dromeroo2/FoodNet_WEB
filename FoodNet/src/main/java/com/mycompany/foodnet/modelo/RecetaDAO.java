package com.mycompany.foodnet.modelo;

import com.mycompany.foodnet.modelo.Conexion;
import com.mycompany.foodnet.modelo.Receta;
import com.mycompany.foodnet.modelo.Comentario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class RecetaDAO {

    // Método básico para cargar una receta suelta (para editarla o borrarla)
    public Receta obtenerPorId(int id) {
        Receta r = null;
        Conexion conexion = new Conexion();
        try {
            Connection cx = conexion.conectar();
            String sql = "SELECT * FROM recetas WHERE id_receta = ?";
            PreparedStatement ps = cx.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                r = new Receta();
                r.setId(rs.getInt("id_receta"));
                r.setIdAutor(rs.getInt("id_usuario")); 
                r.setTitulo(rs.getString("titulo"));
                r.setDescripcion(rs.getString("descripcion"));
                // Saco la ruta de la imagen tal cual está en la BBDD
                r.setImagen(rs.getString("imagen_ruta")); 
                r.setFecha(rs.getString("fecha_publicacion"));
            }
            rs.close(); 
            ps.close(); 
            conexion.desconectar();
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return r;
    }

    // Método para borrar una receta.
    // OJO: Hay que borrar primero los comentarios y likes asociados (integridad referencial), 
    // si no la base de datos dará error.
    public boolean eliminar(int idReceta) {
        Conexion conexion = new Conexion();
        boolean exito = false;
        try {
            Connection cx = conexion.conectar();
            
            // 1. Borro los comentarios de esta receta
            String sqlComm = "DELETE FROM comentarios WHERE id_receta = ?";
            PreparedStatement psComm = cx.prepareStatement(sqlComm);
            psComm.setInt(1, idReceta);
            psComm.executeUpdate();
            psComm.close();

            // 2. Borro los likes de esta receta
            String sqlLikes = "DELETE FROM likes WHERE id_receta = ?";
            PreparedStatement psLikes = cx.prepareStatement(sqlLikes);
            psLikes.setInt(1, idReceta);
            psLikes.executeUpdate();
            psLikes.close();
            
            // 3. Y ahora ya puedo borrar la receta sin problemas
            String sqlReceta = "DELETE FROM recetas WHERE id_receta = ?";
            PreparedStatement psReceta = cx.prepareStatement(sqlReceta);
            psReceta.setInt(1, idReceta);
            int filas = psReceta.executeUpdate();
            psReceta.close();
            
            if (filas > 0) exito = true;
            conexion.desconectar();
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return exito;
    }
    
    // Método para publicar una receta nueva
    public boolean insertar(Receta r) {
        boolean exito = false;
        Conexion conexion = new Conexion();
        try {
            Connection cx = conexion.conectar();
            String sql = "INSERT INTO recetas (titulo, descripcion, imagen_ruta, fecha_publicacion, id_usuario) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = cx.prepareStatement(sql);
            ps.setString(1, r.getTitulo());
            ps.setString(2, r.getDescripcion());
            ps.setString(3, r.getImagen());
            // Pongo la fecha/hora actual del sistema
            ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            ps.setInt(5, r.getIdAutor());
            
            if (ps.executeUpdate() > 0) exito = true;
            ps.close(); 
            conexion.desconectar();
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return exito;
    }

    // ESTE ES EL MÉTODO MÁS IMPORTANTE DEL PROYECTO (TIMELINE)
    // Saca las recetas de la gente a la que sigo + mis propias recetas.
    // Además, calcula cuántos likes tiene cada una y si YO le he dado like.
    public List<Receta> obtenerFeed(int miIdUsuario) {
        List<Receta> lista = new ArrayList<>();
        Conexion conexion = new Conexion();
        try {
            Connection cx = conexion.conectar();
            
            // La consulta usa subselects para contar likes y joins para sacar datos del autor.
            // El WHERE filtra solo autores que están en mi lista de seguidos (o yo mismo).
            String sql = "SELECT r.*, u.username, u.foto_perfil, " +
                         "(SELECT COUNT(*) FROM likes WHERE id_receta = r.id_receta) as total_likes, " +
                         "(SELECT COUNT(*) FROM likes WHERE id_receta = r.id_receta AND id_usuario = ?) as likeado_por_mi " +
                         "FROM recetas r " +
                         "JOIN usuarios u ON r.id_usuario = u.id_usuario " +
                         "WHERE r.id_usuario IN (SELECT id_seguido FROM seguidores WHERE id_seguidor = ?) " +
                         "OR r.id_usuario = ? " +
                         "ORDER BY r.fecha_publicacion DESC";

            PreparedStatement ps = cx.prepareStatement(sql);
            // Paso mi ID tres veces porque lo uso en 3 sitios distintos de la SQL
            ps.setInt(1, miIdUsuario); 
            ps.setInt(2, miIdUsuario); 
            ps.setInt(3, miIdUsuario); 
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // Uso el helper 'mapearReceta' para no copiar y pegar este código mil veces
                lista.add(mapearReceta(rs));
            }
            rs.close(); 
            ps.close(); 
            conexion.desconectar();
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return lista;
    }
    
    // Método auxiliar (helper) para convertir una fila de la BBDD en un objeto Receta.
    // Lo uso en todos los métodos de búsqueda para no repetir código.
    private Receta mapearReceta(ResultSet rs) throws Exception {
        Receta r = new Receta();
        r.setId(rs.getInt("id_receta"));
        r.setTitulo(rs.getString("titulo"));
        r.setDescripcion(rs.getString("descripcion"));
        r.setImagen(rs.getString("imagen_ruta")); 

        Timestamp ts = rs.getTimestamp("fecha_publicacion");
        r.setFecha(ts != null ? ts.toString() : "");
        
        // Datos del autor (gracias al JOIN con la tabla usuarios)
        r.setIdAutor(rs.getInt("id_usuario"));
        r.setAutor(rs.getString("username"));
        String foto = rs.getString("foto_perfil");
        // Si no tiene foto, pongo una por defecto
        r.setFotoAutor((foto != null && !foto.isEmpty()) ? foto : "img/default_user.png");
        
        // Datos calculados (subconsultas)
        r.setCantidadLikes(rs.getInt("total_likes"));
        r.setLikeadoPorMi(rs.getInt("likeado_por_mi") > 0);
        
        // También cargo los comentarios de esta receta
        cargarComentarios(r);
        return r;
    }

    // Método privado para llenar la lista de comentarios de una receta concreta
    private void cargarComentarios(Receta r) {
        Conexion conexion = new Conexion();
        try {
            Connection cx = conexion.conectar();
            String sql = "SELECT c.texto, c.fecha_comentario, u.username " +
                         "FROM comentarios c JOIN usuarios u ON c.id_usuario = u.id_usuario " +
                         "WHERE c.id_receta = ? ORDER BY c.fecha_comentario ASC";
            PreparedStatement ps = cx.prepareStatement(sql);
            ps.setInt(1, r.getId());
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
                Comentario c = new Comentario();
                c.setTexto(rs.getString("texto"));
                c.setFecha(rs.getString("fecha_comentario")); 
                c.setAutor(rs.getString("username"));
                r.addComentario(c);
            }
            rs.close(); 
            ps.close(); 
            conexion.desconectar();
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
    }

    // Método para dar o quitar like
    public void toggleLike(int idUsuario, int idReceta) {
        Conexion conexion = new Conexion();
        try {
            Connection cx = conexion.conectar();
            // Primero compruebo si ya existe el like
            String sqlCheck = "SELECT * FROM likes WHERE id_usuario = ? AND id_receta = ?";
            PreparedStatement psCheck = cx.prepareStatement(sqlCheck);
            psCheck.setInt(1, idUsuario);
            psCheck.setInt(2, idReceta);
            ResultSet rs = psCheck.executeQuery();
            
            if (rs.next()) {
                // Si existe, lo borro (dislike)
                String sqlDel = "DELETE FROM likes WHERE id_usuario = ? AND id_receta = ?";
                PreparedStatement psDel = cx.prepareStatement(sqlDel);
                psDel.setInt(1, idUsuario); psDel.setInt(2, idReceta);
                psDel.executeUpdate(); psDel.close();
            } else {
                // Si no existe, lo creo (like)
                String sqlIns = "INSERT INTO likes (id_usuario, id_receta) VALUES (?, ?)";
                PreparedStatement psIns = cx.prepareStatement(sqlIns);
                psIns.setInt(1, idUsuario); psIns.setInt(2, idReceta);
                psIns.executeUpdate(); psIns.close();
            }
            rs.close(); 
            psCheck.close(); 
            conexion.desconectar();
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
    }

    // Guardar un comentario nuevo
    public void comentar(int idUsuario, int idReceta, String texto) {
        Conexion conexion = new Conexion();
        try {
            Connection cx = conexion.conectar();
            String sql = "INSERT INTO comentarios (id_usuario, id_receta, texto, fecha_comentario) VALUES (?, ?, ?, CURRENT_TIMESTAMP)";
            PreparedStatement ps = cx.prepareStatement(sql);
            ps.setInt(1, idUsuario); ps.setInt(2, idReceta); ps.setString(3, texto);
            ps.executeUpdate();
            ps.close(); 
            conexion.desconectar();
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
    }

    // Saca la lista de recetas a las que he dado Like (sección Favoritos)
    public List<Receta> obtenerFavoritos(int idUsuario) {
        List<Receta> lista = new ArrayList<>();
        Conexion conexion = new Conexion();
        try {
            Connection cx = conexion.conectar();
            
            // Hago un JOIN con la tabla 'likes' para filtrar solo las recetas que me gustan.
            // Fuerzo el campo "likeado_por_mi" a 1 porque, obviamente, si están aquí es porque les di like.
            String sql = "SELECT r.*, u.username, u.foto_perfil, " +
                         "(SELECT COUNT(*) FROM likes WHERE id_receta = r.id_receta) as total_likes, " +
                         "1 as likeado_por_mi " + 
                         "FROM recetas r " +
                         "JOIN likes l ON r.id_receta = l.id_receta " +
                         "JOIN usuarios u ON r.id_usuario = u.id_usuario " +
                         "WHERE l.id_usuario = ? " +
                         "ORDER BY r.fecha_publicacion DESC";

            PreparedStatement ps = cx.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(mapearReceta(rs)); 
            }
            rs.close(); 
            ps.close(); 
            conexion.desconectar();
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return lista;
    }
    
    // Saca todas las recetas de un usuario concreto (para ver su perfil)
    public List<Receta> obtenerPorUsuario(int idUsuario, int miIdObservador) {
        List<Receta> lista = new ArrayList<>();
        Conexion conexion = new Conexion();
        try {
            Connection cx = conexion.conectar();
            // Necesito pasar 'miIdObservador' para saber si yo le he dado like a SUS recetas.
            String sql = "SELECT r.*, u.username, u.foto_perfil, " +
                         "(SELECT COUNT(*) FROM likes WHERE id_receta = r.id_receta) as total_likes, " +
                         "(SELECT COUNT(*) FROM likes WHERE id_receta = r.id_receta AND id_usuario = ?) as likeado_por_mi " +
                         "FROM recetas r " +
                         "JOIN usuarios u ON r.id_usuario = u.id_usuario " +
                         "WHERE r.id_usuario = ? " +
                         "ORDER BY r.fecha_publicacion DESC";

            PreparedStatement ps = cx.prepareStatement(sql);
            ps.setInt(1, miIdObservador); // Para el check del like
            ps.setInt(2, idUsuario);      // Para filtrar por autor
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(mapearReceta(rs));
            }
            rs.close(); 
            ps.close(); 
            conexion.desconectar();
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return lista;
    }

    // Buscador: busca por título o descripción
    public List<Receta> buscar(String query, int miIdUsuario) {
        List<Receta> lista = new ArrayList<>();
        Conexion conexion = new Conexion();
        try {
            Connection cx = conexion.conectar();
            // Uso UPPER() y LIKE para que la búsqueda no distinga mayúsculas de minúsculas y encuentre coincidencias parciales
            String sql = "SELECT r.*, u.username, u.foto_perfil, " +
                         "(SELECT COUNT(*) FROM likes WHERE id_receta = r.id_receta) as total_likes, " +
                         "(SELECT COUNT(*) FROM likes WHERE id_receta = r.id_receta AND id_usuario = ?) as likeado_por_mi " +
                         "FROM recetas r " +
                         "JOIN usuarios u ON r.id_usuario = u.id_usuario " +
                         "WHERE UPPER(r.titulo) LIKE UPPER(?) OR UPPER(r.descripcion) LIKE UPPER(?) " +
                         "ORDER BY r.fecha_publicacion DESC";

            PreparedStatement ps = cx.prepareStatement(sql);
            ps.setInt(1, miIdUsuario);
            // Añado los % para que el LIKE funcione
            ps.setString(2, "%" + query + "%");
            ps.setString(3, "%" + query + "%");
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(mapearReceta(rs));
            }
            rs.close(); 
            ps.close(); 
            conexion.desconectar();
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return lista;
    }
    
    // Método para el panel de administración (saca TODAS las recetas del sistema)
    public List<Receta> obtenerTodas() {
        List<Receta> lista = new ArrayList<>();
        Conexion conexion = new Conexion();
        try {
            Connection cx = conexion.conectar();
            String sql = "SELECT r.*, u.username, u.foto_perfil, " +
                         "(SELECT COUNT(*) FROM likes WHERE id_receta = r.id_receta) as total_likes, " +
                         "0 as likeado_por_mi " + // Al admin le da igual si dio like o no aquí
                         "FROM recetas r JOIN usuarios u ON r.id_usuario = u.id_usuario " +
                         "ORDER BY r.fecha_publicacion DESC";

            PreparedStatement ps = cx.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(mapearReceta(rs));
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