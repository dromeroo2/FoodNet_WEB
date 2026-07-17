package com.mycompany.foodnet.controlador;

import com.mycompany.foodnet.modelo.RecetaDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "LikeServlet", urlPatterns = {"/like"})
public class LikeServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Saco la sesión para saber quién es el usuario que quiere dar like
        HttpSession sesion = request.getSession();
        Object idUsuarioObj = sesion.getAttribute("id_usuario");
        String idRecetaStr = request.getParameter("id");
        
        // Si no estás logueado, no puedes interactuar, así que te mando fuera
        if (idUsuarioObj == null) {
            response.sendRedirect("index.html");
            return;
        }
        
        if (idRecetaStr != null) {
            int idUsuario = (int) idUsuarioObj;
            int idReceta = Integer.parseInt(idRecetaStr);
            
            // Llamo al DAO para gestionar el like.
            // El método se llama 'toggle' porque hace de interruptor: si ya le di like, lo quita; si no, lo pone.
            RecetaDAO dao = new RecetaDAO();
            dao.toggleLike(idUsuario, idReceta);
        }
        
        // Uso la cabecera 'referer' para saber desde dónde ha hecho clic el usuario.
        // Así, si le dio like desde el timeline, vuelve al timeline; si estaba viendo la receta en detalle, se queda ahí.
        String paginaAnterior = request.getHeader("referer");
        response.sendRedirect(paginaAnterior != null ? paginaAnterior : "timeline");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}