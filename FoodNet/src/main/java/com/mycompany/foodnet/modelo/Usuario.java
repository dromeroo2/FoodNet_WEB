package com.mycompany.foodnet.modelo;

// Esta clase representa una fila de la tabla 'usuarios' de la base de datos.
// Sirve para mover los datos del usuario entre el DAO, el Servlet y el JSP.
public class Usuario {
    
    private int id;
    private String username;
    private String password; // En un caso real esto debería ir cifrado, pero aquí va en texto plano por sencillez
    private String email;
    private String foto;
    private String descripcion; // A veces uso este campo para guardar el 'rol' (admin/usuario) temporalmente al hacer login

    // Constructor vacío: Necesario para poder crear el objeto y luego ir rellenándolo con setters
    public Usuario() {}

    // --- Getters y Setters ---
    // Son obligatorios para que los JSPs puedan acceder a los datos usando ${usuario.username}, etc.

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getFoto() { 
        // Pequeño truco defensivo: si no tiene foto asignada, devuelvo la imagen por defecto.
        // Así evito que aparezcan iconos de "imagen rota" en la web.
        if (foto == null || foto.isEmpty()) {
            return "img/default_user.png";
        }
        return foto; 
    }
    public void setFoto(String foto) { this.foto = foto; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}