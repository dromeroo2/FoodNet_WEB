package com.mycompany.foodnet.modelo;

import java.util.ArrayList;
import java.util.List;      

public class Receta {
    // Campos que coinciden con las columnas de la tabla 'recetas' en la base de datos
    private int id;
    private String titulo;
    private String descripcion;
    private String imagen;
    private String fecha;
    
    // CAMPOS EXTRA (JOINs):
    // Estos atributos no están en la tabla recetas, pero los relleno en el DAO haciendo JOIN con usuarios.
    // Los necesito aquí para poder mostrarlos en el JSP sin hacer más consultas.
    private String nombreAutor;     
    private String fotoAutor;       
    private String autor;      // Nombre del usuario que la subió
    private int idAutor;       // ID del usuario (para poder poner un link a su perfil)
    
    // CAMPOS CALCULADOS (LIKES):
    // Se rellenan con subconsultas (COUNT) en el DAO.
    private int cantidadLikes;
    private boolean likeadoPorMi; // Para saber si pinto el corazón en rojo o en gris

    // Relación 1 a N: Una receta tiene una lista de comentarios
    private List<Comentario> comentarios = new ArrayList<>();
    
    // Constructor vacío: Necesario para instanciar el objeto antes de rellenarlo
    public Receta() {}

    // Constructor completo
    public Receta(int id, String titulo, String descripcion, String imagen, String fecha, String nombreAutor, String fotoAutor) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.fecha = fecha;
        this.nombreAutor = nombreAutor;
        this.fotoAutor = fotoAutor;
    }

    // --- GETTERS Y SETTERS ---
    // Son imprescindibles para que JSP (Expression Language) pueda leer los datos con ${receta.titulo}
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    // Aquí he puesto un pequeño truco:
    // Si la imagen es null o está vacía, devuelvo una URL de una imagen genérica.
    // Así me aseguro de que el diseño de la web no se rompa si falta la foto.
    public String getImagen() { 
        if (this.imagen == null || this.imagen.isEmpty()) {
            return "https://placehold.co/600x400?text=Sin+Foto"; 
        }
        return imagen; 
    }

    public void setImagen(String imagen) { this.imagen = imagen; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getNombreAutor() { return nombreAutor; }
    public void setNombreAutor(String nombreAutor) { this.nombreAutor = nombreAutor; }

    // Lo mismo aquí: si el usuario no tiene foto, devuelvo el avatar por defecto
    public String getFotoAutor() {
            if (this.fotoAutor == null || this.fotoAutor.isEmpty()) {
                return "img/default_user.png";
            }
            return fotoAutor;
        }    
    public void setFotoAutor(String fotoAutor) { this.fotoAutor = fotoAutor; }
    
    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public int getIdAutor() { return idAutor; }
    public void setIdAutor(int idAutor) { this.idAutor = idAutor; }
    
    public int getCantidadLikes() { return cantidadLikes; }
    public void setCantidadLikes(int cantidadLikes) { this.cantidadLikes = cantidadLikes; }

    public boolean isLikeadoPorMi() { return likeadoPorMi; }
    public void setLikeadoPorMi(boolean likeadoPorMi) { this.likeadoPorMi = likeadoPorMi; }
    
    
    public List<Comentario> getComentarios() {
        return comentarios;
    }

    public void setComentarios(List<Comentario> comentarios) {
        this.comentarios = comentarios;
    }
    
    // Método helper: Útil para ir añadiendo comentarios uno a uno mientras recorro el ResultSet en el DAO
    public void addComentario(Comentario c) {
        this.comentarios.add(c);
    }
}