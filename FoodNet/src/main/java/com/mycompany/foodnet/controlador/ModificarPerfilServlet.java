package com.mycompany.foodnet.controlador;

import com.mycompany.foodnet.modelo.Usuario;
import com.mycompany.foodnet.modelo.UsuarioDAO; 
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.nio.file.Paths;

@WebServlet(name = "ModificarPerfilServlet", urlPatterns = {"/modificar"})
@MultipartConfig // Esta anotación es obligatoria para poder recibir archivos (fotos) desde el formulario
public class ModificarPerfilServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Comprobación estándar de seguridad: si no hay sesión, no puedes editar nada
        HttpSession sesion = request.getSession();
        Object idObj = sesion.getAttribute("id_usuario");
        
        if (idObj == null) {
            response.sendRedirect("index.html");
            return;
        }
        int idUsuario = (int) idObj;

        // Recojo todos los campos que me envía el formulario de edición
        String nuevaDesc = request.getParameter("descripcion");
        String passActual = request.getParameter("passActual");
        String passNueva = request.getParameter("passNueva");
        String passConfirm = request.getParameter("passConfirm");
        
        // --- PARTE DE SUBIDA DE FOTO ---
        // Recupero el archivo del formulario
        Part filePart = request.getPart("fichero");
        String rutaFoto = null;

        // Solo entro aquí si el usuario ha seleccionado un archivo y tiene contenido
        if (filePart != null && filePart.getSize() > 0) {
            try {
                // Saco el nombre original del archivo
                String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                // Le pongo un timestamp delante para que no se repitan los nombres si suben dos fotos iguales
                String nuevoNombre = "perfil_" + System.currentTimeMillis() + "_" + fileName;
                
                // Defino la carpeta física donde se van a guardar (fuera del proyecto para no perderlas al recompilar)
                String uploadPath = "C://foodnet_fotos";
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) uploadDir.mkdir(); // Si no existe la carpeta, la creo
                
                // Guardo el archivo byte a byte en el disco duro
                File filePath = new File(uploadDir, nuevoNombre);
                try (InputStream input = filePart.getInputStream();
                     FileOutputStream output = new FileOutputStream(filePath)) {
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = input.read(buffer)) > 0) output.write(buffer, 0, len);
                }
                
                // Preparo la ruta relativa que guardaré en la base de datos (para que luego el ImagenesServlet la encuentre)
                rutaFoto = "imagenes/" + nuevoNombre;
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // --- LÓGICA DE ACTUALIZACIÓN ---
        UsuarioDAO dao = new UsuarioDAO();
        // Compruebo si el usuario ha escrito algo en la contraseña nueva para saber si quiere cambiarla
        boolean cambiarPass = (passNueva != null && !passNueva.trim().isEmpty());
        
        if (cambiarPass) {
            // Primero: que la contraseña nueva y la confirmación coincidan
            if (!passNueva.equals(passConfirm)) {
                response.sendRedirect("modify.jsp?error=match");
                return;
            }
            // Segundo: que la contraseña actual que ha puesto sea la correcta (seguridad)
            if (!dao.validarPassword(idUsuario, passActual)) {
                response.sendRedirect("modify.jsp?error=pass");
                return;
            }
        }
        
        // Creo un objeto Usuario auxiliar con los datos nuevos
        Usuario u = new Usuario();
        u.setId(idUsuario);
        u.setDescripcion(nuevaDesc.isEmpty() ? "Usuario de FoodNet" : nuevaDesc);
        u.setFoto(rutaFoto); // Si esto es null, el DAO está programado para no tocar la foto que ya había
        
        // Llamo al DAO para que haga el UPDATE en la base de datos
        boolean exito = dao.actualizar(u, cambiarPass, passNueva);

        if (exito) {
            response.sendRedirect("perfil"); // Todo ok, vuelta al perfil
        } else {
            response.sendRedirect("modify.jsp?error=db"); // Fallo al guardar
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
        // Si me llaman por GET, simplemente muestro el formulario de edición (JSP)
        request.getRequestDispatcher("modify.jsp").forward(request, response);
    }
}