package com.mycompany.foodnet.controlador;

import com.mycompany.foodnet.modelo.RecetaDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "ComentarServlet", urlPatterns = {"/comentar"})
public class ComentarServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Importante para que se guarden bien las tildes y las ñ
        request.setCharacterEncoding("UTF-8");
        
        HttpSession sesion = request.getSession();
        Object idUsuarioObj = sesion.getAttribute("id_usuario");
        
        // Compruebo si el usuario está logueado. Si no, lo mando al login.
        if (idUsuarioObj == null) {
            response.sendRedirect("index.html");
            return;
        }

        String idRecetaStr = request.getParameter("id_receta");
        String texto = request.getParameter("texto");
        
        // Solo guardo si el comentario no está vacío para no meter basura en la BD
        if (idRecetaStr != null && texto != null && !texto.trim().isEmpty()) {
            int miId = (int) idUsuarioObj;
            int idReceta = Integer.parseInt(idRecetaStr);
            
            // Llamo al DAO para que haga el insert en la tabla de comentarios
            RecetaDAO dao = new RecetaDAO();
            dao.comentar(miId, idReceta, texto);
        }
        
        // Uso esto para devolver al usuario a la página donde estaba (el timeline o el detalle de la receta)
        String paginaAnterior = request.getHeader("referer");
        response.sendRedirect(paginaAnterior != null ? paginaAnterior : "timeline");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}