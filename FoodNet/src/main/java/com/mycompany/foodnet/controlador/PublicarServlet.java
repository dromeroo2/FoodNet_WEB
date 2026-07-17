package com.mycompany.foodnet.controlador;

import com.mycompany.foodnet.modelo.Receta;
import com.mycompany.foodnet.modelo.RecetaDAO;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.UUID;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

@WebServlet(name = "PublicarServlet", urlPatterns = {"/publicar"})
@MultipartConfig // Esta línea es clave: sin ella el Servlet no sabe leer fotos, solo texto
public class PublicarServlet extends HttpServlet {

    // Defino la carpeta donde voy a guardar las fotos en mi disco duro.
    // La pongo fuera del proyecto (en C:) para que no se borren cada vez que reinicio el servidor.
    private static final String UPLOAD_DIR = "C://foodnet_fotos";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Para que no se rompan las tildes en el título o descripción
        request.setCharacterEncoding("UTF-8");

        HttpSession sesion = request.getSession();
        Object idUsuarioObj = sesion.getAttribute("id_usuario");
        
        // Seguridad básica: si no estás logueado, no puedes publicar nada
        if (idUsuarioObj == null) {
            response.sendRedirect("index.html");
            return;
        }
        
        int idUsuario = (int) idUsuarioObj;

        try {
            // Recojo los campos de texto del formulario
            String titulo = request.getParameter("titulo");
            String descripcion = request.getParameter("descripcion");
            // Por si el usuario decide poner una URL de internet en vez de subir foto
            String imagenUrlExterna = request.getParameter("imagen_url");
            
            String nombreFinalImagen = "";

            // --- GESTIÓN DE LA SUBIDA DE LA FOTO ---
            // 'Part' es la pieza del formulario que contiene el archivo binario
            Part filePart = request.getPart("fichero");
            
            // Compruebo si realmente ha subido un archivo (que tenga tamaño > 0)
            if (filePart != null && filePart.getSize() > 0) {
                
                // 1. Saco el nombre original del archivo (ej: "foto.jpg")
                String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                
                // Saco la extensión (.jpg, .png)
                String extension = "";
                int i = fileName.lastIndexOf('.');
                if (i > 0) { extension = fileName.substring(i); }
                
                // 2. Me invento un nombre único para que no se sobrescriban fotos con el mismo nombre.
                // Uso UUID para generar letras aleatorias y el tiempo actual.
                String uniqueName = "receta_" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 8) + extension;
                
                // 3. Compruebo si existe la carpeta C://foodnet_fotos, y si no, la creo
                File uploadDir = new File(UPLOAD_DIR);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                
                // 4. GUARDO EL ARCHIVO EN EL DISCO
                // Preparo el fichero destino
                File archivoDestino = new File(uploadDir, uniqueName);
                
                // Uso Files.copy para mover los bytes desde la memoria temporal al disco duro
                try (InputStream input = filePart.getInputStream()) {
                    Files.copy(input, archivoDestino.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
                
                // Este es el nombre que guardaré en la base de datos (solo el nombre, no la ruta completa de C:)
                nombreFinalImagen = uniqueName; 
                
            } else if (imagenUrlExterna != null && !imagenUrlExterna.isEmpty()) {
                // Si no subió archivo, miro si puso una URL externa
                nombreFinalImagen = imagenUrlExterna;
            } else {
                // Si no puso nada, le asigno una imagen por defecto
                nombreFinalImagen = "img/default_food.png";
            }
            // -------------------------------------------------------------------

            // Creo el objeto Receta y lo lleno con los datos
            Receta nuevaReceta = new Receta();
            nuevaReceta.setTitulo(titulo);
            nuevaReceta.setDescripcion(descripcion);
            nuevaReceta.setImagen(nombreFinalImagen);
            nuevaReceta.setFecha(LocalDate.now().toString()); // La fecha de hoy
            nuevaReceta.setIdAutor(idUsuario); 

            // Llamo al DAO para guardar la receta en la base de datos
            RecetaDAO dao = new RecetaDAO();
            boolean exito = dao.insertar(nuevaReceta);

            if (exito) {
                // Si todo va bien, vuelvo al muro para ver mi receta nueva
                response.sendRedirect("timeline");
            } else {
                response.sendRedirect("timeline?error=db_error");
            }

        } catch (Exception e) {
            e.printStackTrace(); // Imprimo el error en la consola de NetBeans para depurar
            response.sendRedirect("timeline?error=fatal");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}