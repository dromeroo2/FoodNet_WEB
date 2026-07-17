package com.mycompany.foodnet.modelo;

import com.mycompany.foodnet.modelo.Conexion;
import com.mycompany.foodnet.modelo.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    // Método para comprobar el login. 
    // Si el usuario y contraseña coinciden, devuelvo el objeto Usuario lleno; si no, devuelvo null.
    public Usuario login(String username, String password) {
        Usuario u = null;
        Conexion conexion = new Conexion();
        try {
            Connection cx = conexion.conectar();
            String sql = "SELECT * FROM usuarios WHERE username = ? AND password = ?";
            PreparedStatement ps = cx.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                u = new Usuario();
                u.setId(rs.getInt("id_usuario"));
                u.setUsername(rs.getString("username"));
                u.setEmail(rs.getString("email"));
                
                // Truco: uso el campo 'descripcion' del objeto para guardar el rol temporalmente 
                // ya que necesito saber si es admin o usuario normal en el Servlet.
                u.setDescripcion(rs.getString("rol")); 
                
                String foto = rs.getString("foto_perfil");
                // Si no tiene foto, le pongo la ruta de la imagen por defecto
                u.setFoto((foto != null && !foto.isEmpty()) ? foto : "img/default_user.png");
            }
            rs.close(); 
            ps.close(); 
            conexion.desconectar();
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return u;
    }

    // Método para registrar un usuario nuevo desde el formulario de Sign Up
    public boolean registrar(Usuario u) {
        boolean exito = false;
        Conexion conexion = new Conexion();
        try {
            Connection cx = conexion.conectar();
            // Por defecto, todo el que se registra entra con rol de 'usuario' (no admin)
            String sql = "INSERT INTO usuarios (username, email, password, rol) VALUES (?, ?, ?, 'usuario')";
            PreparedStatement ps = cx.prepareStatement(sql);
            ps.setString(1, u.getUsername());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getPassword());
            
            int filas = ps.executeUpdate();
            if (filas > 0) exito = true;
            
            ps.close(); 
            conexion.desconectar();
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return exito;
    }

    // Saca la información de un usuario sabiendo su ID (para ver perfiles)
    public Usuario obtenerPorId(int id) {
        Usuario u = null;
        Conexion conexion = new Conexion();
        try {
            Connection cx = conexion.conectar();
            String sql = "SELECT * FROM usuarios WHERE id_usuario = ?";
            PreparedStatement ps = cx.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                u = new Usuario();
                u.setId(rs.getInt("id_usuario"));
                u.setUsername(rs.getString("username"));
                u.setEmail(rs.getString("email"));
                u.setDescripcion(rs.getString("descripcion"));
                String foto = rs.getString("foto_perfil");
                u.setFoto((foto != null && !foto.isEmpty()) ? foto : "img/default_user.png");
            }
            rs.close(); 
            ps.close(); 
            conexion.desconectar();
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return u;
    }

    // Método complejo para actualizar el perfil.
    // Como el usuario puede que solo cambie la descripción, o solo la foto, o la contraseña...
    // construyo la sentencia SQL dinámicamente usando un StringBuilder.
    public boolean actualizar(Usuario u, boolean cambiarPass, String nuevaPass) {
        boolean exito = false;
        Conexion conexion = new Conexion();
        try {
            Connection cx = conexion.conectar();
            
            // Empiezo la SQL básica
            StringBuilder sql = new StringBuilder("UPDATE usuarios SET descripcion = ?");
            
            // Si hay foto nueva, añado esta parte a la SQL
            if (u.getFoto() != null) sql.append(", foto_perfil = ?");
            // Si quiere cambiar contraseña, añado esta otra
            if (cambiarPass) sql.append(", password = ?");
            
            sql.append(" WHERE id_usuario = ?");
            
            PreparedStatement ps = cx.prepareStatement(sql.toString());
            int i = 1;
            ps.setString(i++, u.getDescripcion());
            
            // Relleno los interrogantes en orden dinámico
            if (u.getFoto() != null) ps.setString(i++, u.getFoto());
            if (cambiarPass) ps.setString(i++, nuevaPass);
            
            ps.setInt(i, u.getId());
            
            int filas = ps.executeUpdate();
            if (filas > 0) exito = true;
            
            ps.close(); 
            conexion.desconectar();
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return exito;
    }
    
    // Método de seguridad: comprueba si la contraseña actual es correcta antes de dejar cambiarla
    public boolean validarPassword(int idUsuario, String passActual) {
        boolean valida = false;
        Conexion conexion = new Conexion();
        try {
            Connection cx = conexion.conectar();
            String sql = "SELECT password FROM usuarios WHERE id_usuario = ?";
            PreparedStatement ps = cx.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getString("password").equals(passActual)) valida = true;
            }
            rs.close(); 
            ps.close(); 
            conexion.desconectar();
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return valida;
    }

    // Sistema de seguidores: funciona como un interruptor.
    // Si ya lo sigue -> Lo borra. Si no lo sigue -> Lo añade.
    public boolean toggleSeguir(int idSeguidor, int idSeguido) {
        boolean ahoraLoSigue = false; 
        Conexion conexion = new Conexion();
        try {
            Connection cx = conexion.conectar();
            
            // 1. Compruebo si ya existe la relación en la tabla
            String checkSql = "SELECT * FROM seguidores WHERE id_seguidor = ? AND id_seguido = ?";
            PreparedStatement psCheck = cx.prepareStatement(checkSql);
            psCheck.setInt(1, idSeguidor);
            psCheck.setInt(2, idSeguido);
            ResultSet rs = psCheck.executeQuery();
            
            if (rs.next()) {
                // YA LO SIGUE -> BORRAR (Unfollow)
                String delSql = "DELETE FROM seguidores WHERE id_seguidor = ? AND id_seguido = ?";
                PreparedStatement psDel = cx.prepareStatement(delSql);
                psDel.setInt(1, idSeguidor);
                psDel.setInt(2, idSeguido);
                psDel.executeUpdate();
                psDel.close();
                ahoraLoSigue = false;
            } else {
                // NO LO SIGUE -> INSERTAR (Follow)
                String insSql = "INSERT INTO seguidores (id_seguidor, id_seguido) VALUES (?, ?)";
                PreparedStatement psIns = cx.prepareStatement(insSql);
                psIns.setInt(1, idSeguidor);
                psIns.setInt(2, idSeguido);
                psIns.executeUpdate();
                psIns.close();
                ahoraLoSigue = true;
            }
            rs.close(); 
            psCheck.close(); 
            conexion.desconectar();
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return ahoraLoSigue;
    }

    // Helper para saber si sigo a alguien (para pintar el botón "Seguir" o "Dejar de seguir")
    public boolean loSigo(int idSeguidor, int idSeguido) {
        boolean sigo = false;
        Conexion conexion = new Conexion();
        try {
            Connection cx = conexion.conectar();
            String sql = "SELECT COUNT(*) FROM seguidores WHERE id_seguidor = ? AND id_seguido = ?";
            PreparedStatement ps = cx.prepareStatement(sql);
            ps.setInt(1, idSeguidor);
            ps.setInt(2, idSeguido);
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) sigo = true;
            rs.close(); 
            ps.close(); 
            conexion.desconectar();
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return sigo;
    }
    
    // Método para la barra de búsqueda de usuarios
    public List<Usuario> buscar(String query) {
        List<Usuario> lista = new ArrayList<>();
        Conexion conexion = new Conexion();
        try {
            Connection cx = conexion.conectar();
            // Si no escriben nada, traigo todos (útil para pruebas), si escriben, filtro con LIKE.
            // Uso UPPER() para que encuentre "Pepe" aunque escriba "pepe".
            String sql = (query == null) ? "SELECT * FROM usuarios" : "SELECT * FROM usuarios WHERE UPPER(username) LIKE UPPER(?)";
            PreparedStatement ps = cx.prepareStatement(sql);
            if (query != null) ps.setString(1, "%" + query + "%");
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id_usuario"));
                u.setUsername(rs.getString("username"));
                u.setEmail(rs.getString("email"));
                u.setDescripcion(rs.getString("descripcion"));
                String foto = rs.getString("foto_perfil");
                u.setFoto((foto != null && !foto.isEmpty()) ? foto : "img/default_user.png");
                lista.add(u);
            }
            rs.close(); 
            ps.close(); 
            conexion.desconectar();
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return lista;
    }
    
    // --- MÉTODOS PARA EL CHAT ---

    // Saca la lista de usuarios con los que ya he hablado alguna vez (para la barra lateral)
    public List<Usuario> obtenerContactosChat(int miId) {
        List<Usuario> lista = new ArrayList<>();
        Conexion conexion = new Conexion();
        try {
            Connection cx = conexion.conectar();
            // Esta consulta busca gente a la que YO envié mensaje OR gente que ME envió mensaje a mí
            String sql = "SELECT id_usuario, username, foto_perfil FROM usuarios WHERE id_usuario IN (" +
                         "    SELECT id_receptor FROM mensajes WHERE id_emisor = ? " +
                         "    UNION " +
                         "    SELECT id_emisor FROM mensajes WHERE id_receptor = ? " +
                         ")";
            PreparedStatement ps = cx.prepareStatement(sql);
            ps.setInt(1, miId);
            ps.setInt(2, miId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id_usuario"));
                u.setUsername(rs.getString("username"));
                String foto = rs.getString("foto_perfil");
                u.setFoto((foto != null && !foto.isEmpty()) ? foto : "img/default_user.png");
                lista.add(u);
            }
            rs.close(); 
            ps.close(); 
            conexion.desconectar();
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return lista;
    }

    // Saca la gente a la que sigo (para poder iniciar un chat nuevo con ellos)
    public List<Usuario> obtenerSeguidos(int miId) {
        List<Usuario> lista = new ArrayList<>();
        Conexion conexion = new Conexion();
        try {
            Connection cx = conexion.conectar();
            String sql = "SELECT u.id_usuario, u.username, u.foto_perfil " +
                         "FROM usuarios u " +
                         "JOIN seguidores s ON u.id_usuario = s.id_seguido " +
                         "WHERE s.id_seguidor = ?";
            PreparedStatement ps = cx.prepareStatement(sql);
            ps.setInt(1, miId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id_usuario"));
                u.setUsername(rs.getString("username"));
                String foto = rs.getString("foto_perfil");
                u.setFoto((foto != null && !foto.isEmpty()) ? foto : "img/default_user.png");
                lista.add(u);
            }
            rs.close(); 
            ps.close(); 
            conexion.desconectar();
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return lista;
    }
    
    // ADMIN: Método para listar TODOS los usuarios en el panel de control
    public List<Usuario> obtenerTodos() {
        List<Usuario> lista = new ArrayList<>();
        Conexion conexion = new Conexion();
        try {
            Connection cx = conexion.conectar();
            String sql = "SELECT * FROM usuarios ORDER BY id_usuario DESC";
            PreparedStatement ps = cx.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id_usuario"));
                u.setUsername(rs.getString("username"));
                u.setEmail(rs.getString("email"));
                // Aquí recupero el rol para que el admin sepa quién es quién
                u.setDescripcion(rs.getString("rol")); 
                String foto = rs.getString("foto_perfil");
                u.setFoto((foto != null && !foto.isEmpty()) ? foto : "img/default_user.png");
                lista.add(u);
            }
            rs.close(); 
            ps.close(); 
            conexion.desconectar();
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return lista;
    }

    // ADMIN: Método crítico para borrar una cuenta entera.
    // Como la base de datos no siempre tiene el "ON DELETE CASCADE" configurado,
    // borro manualmente todo lo relacionado con el usuario para que no queden datos huérfanos.
    public boolean eliminarCuentaCompleta(int idUsuario) {
        boolean exito = false;
        Conexion conexion = new Conexion();
        try {
            Connection cx = conexion.conectar();
            
            // 1. Borro sus interacciones (lo que él hizo)
            borrarDeTabla(cx, "likes", "id_usuario", idUsuario);
            borrarDeTabla(cx, "comentarios", "id_usuario", idUsuario);
            borrarDeTabla(cx, "seguidores", "id_seguidor", idUsuario);
            borrarDeTabla(cx, "seguidores", "id_seguido", idUsuario);
            borrarDeTabla(cx, "mensajes", "id_emisor", idUsuario);
            borrarDeTabla(cx, "mensajes", "id_receptor", idUsuario);
            
            // 2. Borro sus RECETAS. 
            // OJO: Antes de borrar la receta, tengo que borrar los likes y comentarios que tiene esa receta.
            List<Integer> idsRecetas = new ArrayList<>();
            String sqlGetIds = "SELECT id_receta FROM recetas WHERE id_usuario = ?";
            PreparedStatement psGetIds = cx.prepareStatement(sqlGetIds);
            psGetIds.setInt(1, idUsuario);
            ResultSet rsIds = psGetIds.executeQuery();
            while (rsIds.next()) idsRecetas.add(rsIds.getInt("id_receta"));
            rsIds.close(); 
            psGetIds.close();
            
            // Borro dependencias de cada receta
            for (int idReceta : idsRecetas) {
                borrarDeTabla(cx, "likes", "id_receta", idReceta);
                borrarDeTabla(cx, "comentarios", "id_receta", idReceta);
            }
            // Ahora sí, borro las recetas
            borrarDeTabla(cx, "recetas", "id_usuario", idUsuario);
            
            // 3. Finalmente, borro al USUARIO de la tabla usuarios
            borrarDeTabla(cx, "usuarios", "id_usuario", idUsuario);
            
            exito = true;
            conexion.desconectar();
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return exito;
    }

    // Método auxiliar para borrar datos rápidamente y no repetir código SQL
    private void borrarDeTabla(Connection cx, String tabla, String columna, int id) throws Exception {
        String sql = "DELETE FROM " + tabla + " WHERE " + columna + " = ?";
        PreparedStatement ps = cx.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
        ps.close();
    }
}