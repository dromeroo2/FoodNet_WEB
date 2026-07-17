package com.mycompany.foodnet.modelo;

public class Comentario {
    
    // Atributos privados para guardar los datos de un comentario
    private int id; 
    private String autor; // Nombre del usuario que escribió el comentario
    private String texto; // El contenido del comentario
    private String fecha;

    // Constructor vacío: Es necesario para crear el objeto primero y luego rellenarlo con los setters 
    // cuando traemos los datos de la base de datos en el DAO.
    public Comentario() {
    }

    // Constructor completo: Útil si tengo todos los datos y quiero crear el objeto de golpe
    public Comentario(int id, String autor, String texto, String fecha) {
        this.id = id;
        this.autor = autor;
        this.texto = texto;
        this.fecha = fecha;
    }
    
    // Constructor sin ID: Se usa a veces cuando creamos un comentario nuevo que aún no se ha guardado en la BBDD
    public Comentario(String autor, String texto, String fecha) {
        this.autor = autor;
        this.texto = texto;
        this.fecha = fecha;
    }

    // Getters y Setters
    // Son necesarios para poder leer y modificar los datos privados desde otras clases (como los Servlets o DAOs)

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}