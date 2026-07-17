package com.mycompany.foodnet.controlador;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger; 
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Este Servlet es un puente para poder servir imágenes que están guardadas en una carpeta local (C:)
// ya que el navegador no puede acceder directamente a mi disco duro por seguridad.
@WebServlet(name = "ImagenesServlet", urlPatterns = {"/imagenes/*"})
public class ImagenesServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ImagenesServlet.class.getName());
    
    // Aquí defino la carpeta de mi ordenador donde se guardan las fotos de verdad
    private static final String RUTA_FISICA = "C://foodnet_fotos";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Cojo el nombre del archivo que viene en la URL (detrás de /imagenes/)
        String filename = request.getPathInfo();
        
        // Si no piden nada concreto, devuelvo error
        if (filename == null || filename.equals("/")) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        
        // Decodifico el nombre por si tiene caracteres raros de URL (como %20 en vez de espacios)
        filename = URLDecoder.decode(filename, "UTF-8");
        filename = filename.substring(1); // Quito la barra inicial '/'
        
        LOGGER.log(Level.INFO, "Peticion de imagen recibida: {0}", filename);
        
        // Busco el archivo físico en la carpeta de mi disco C:
        File file = new File(RUTA_FISICA, filename);
        
        if (file.exists()) {
            // Si el archivo existe, configuro el tipo (JPG, PNG...)
            String contentType = getServletContext().getMimeType(file.getName());
            if (contentType == null) contentType = "image/jpeg";
            response.setContentType(contentType);
            
            // Copio los bytes de la imagen directamente a la respuesta para que se vea en el navegador
            Files.copy(file.toPath(), response.getOutputStream());
        } else {
            // Si no encuentro la foto en la carpeta, devuelvo un 404 y aviso en el log
            LOGGER.log(Level.SEVERE, "404 - No encuentro el archivo en: {0}", file.getAbsolutePath());
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}