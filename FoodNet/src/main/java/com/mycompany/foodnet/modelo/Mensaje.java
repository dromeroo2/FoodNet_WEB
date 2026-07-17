package com.mycompany.foodnet.modelo;

public class Mensaje {
    // Atributos que coinciden con las columnas de la tabla 'mensajes' en la BBDD
    private int id;
    private int idEmisor;    // El ID del usuario que escribe
    private int idReceptor;  // El ID del usuario que recibe
    private String texto;
    private String fecha;
    
    // Atributos extra: No están en la tabla de mensajes, pero los añado aquí
    // para poder guardar los nombres de los usuarios cuando hago JOINs y mostrarlos en el JSP.
    private String nombreEmisor;
    private String nombreReceptor;

    // Constructor vacío (necesario para instanciar el objeto antes de rellenarlo en el DAO)
    public Mensaje() {}

    // Constructor rápido para cuando voy a enviar un mensaje nuevo
    public Mensaje(int idEmisor, int idReceptor, String texto) {
        this.idEmisor = idEmisor;
        this.idReceptor = idReceptor;
        this.texto = texto;
    }

    // Getters y Setters
    // Métodos estándar para acceder y modificar las propiedades privadas

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getIdEmisor() { return idEmisor; }
    public void setIdEmisor(int idEmisor) { this.idEmisor = idEmisor; }
    
    public int getIdReceptor() { return idReceptor; }
    public void setIdReceptor(int idReceptor) { this.idReceptor = idReceptor; }
    
    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }
    
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    
    public String getNombreEmisor() { return nombreEmisor; }
    public void setNombreEmisor(String nombreEmisor) { this.nombreEmisor = nombreEmisor; }
    
    public String getNombreReceptor() { return nombreReceptor; }
    public void setNombreReceptor(String nombreReceptor) { this.nombreReceptor = nombreReceptor; }
}