package com.mycompany.foodnet.controlador;

import com.mycompany.foodnet.modelo.Receta;
import com.mycompany.foodnet.modelo.Usuario;
import com.mycompany.foodnet.modelo.RecetaDAO;
import com.mycompany.foodnet.modelo.UsuarioDAO;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "PerfilServlet", urlPatterns = {"/perfil"})
public class PerfilServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Comprobación de seguridad: necesito saber quién está navegando (miId)
        HttpSession sesion = request.getSession();
        Object idObj = sesion.getAttribute("id_usuario");

        // Si no hay sesión iniciada, lo mando fuera
        if (idObj == null) {
            response.sendRedirect("index.html");
            return;
        }

        int miId = (int) idObj; // Este soy yo (el que está logueado)
        
        // Aquí decido qué perfil se va a mostrar:
        // Si en la URL viene ?id=5, muestro el usuario 5.
        // Si no viene nada (solo /perfil), muestro mi propio perfil.
        int idPerfil = miId; 
        String idParam = request.getParameter("id");
        
        if (idParam != null && !idParam.isEmpty()) {
            try {
                idPerfil = Integer.parseInt(idParam);
            } catch (NumberFormatException e) {
                // Si ponen letras en el ID, ignoro y muestro el mío por defecto
                idPerfil = miId;
            }
        }

        // Preparo los DAOs para sacar toda la info necesaria de la base de datos
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        RecetaDAO recetaDAO = new RecetaDAO();

        // 1. Saco los datos personales del usuario que vamos a ver (nombre, foto, descripción...)
        Usuario usuario = usuarioDAO.obtenerPorId(idPerfil);
        
        // 2. Saco la lista de sus recetas publicadas
        List<Receta> misRecetas = recetaDAO.obtenerPorUsuario(idPerfil, miId);
        
        // 3. Saco también sus recetas favoritas para mostrarlas en otra pestaña
        List<Receta> recetasFavoritas = recetaDAO.obtenerFavoritos(idPerfil);
        
        // 4. Lógica de "Seguir":
        // Si estoy viendo el perfil de otro, compruebo si ya le sigo para pintar el botón correctamente.
        boolean loSigo = false;
        if (miId != idPerfil) {
            loSigo = usuarioDAO.loSigo(miId, idPerfil);
        }

        // Guardo todo en la petición para que el JSP pueda pintarlo
        request.setAttribute("usuario", usuario);
        request.setAttribute("misRecetas", misRecetas);
        request.setAttribute("recetasFavoritas", recetasFavoritas);
        request.setAttribute("loSigo", loSigo);
        
        // Decisión de vista:
        // Si el ID coincide con el mío, cargo 'profile.jsp' (que tiene botón de Editar).
        // Si es otro usuario, cargo 'public-profile.jsp' (que tiene botón de Seguir).
        if (miId == idPerfil) {
            request.getRequestDispatcher("profile.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("public-profile.jsp").forward(request, response);
        }    
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}