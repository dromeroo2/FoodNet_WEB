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

@WebServlet(name = "AdminServlet", urlPatterns = {"/admin"})
public class AdminServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession sesion = request.getSession();
        Object rolObj = sesion.getAttribute("rol");

        // Lo primero es la seguridad: compruebo si el usuario tiene rol de "admin"
        // Si no es admin (o no está logueado), lo mando de vuelta al timeline para que no entre donde no debe
        if (rolObj == null || !"admin".equals(rolObj.toString())) {
            response.sendRedirect("timeline");
            return;
        }

        // Si pasa el filtro, uso los DAOs para traer TODA la info de la base de datos
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        RecetaDAO recetaDAO = new RecetaDAO();
        
        // Cargo la lista completa de usuarios y recetas para poder gestionarlos (borrarlos si hace falta)
        List<Usuario> listaUsuarios = usuarioDAO.obtenerTodos();
        List<Receta> listaRecetas = recetaDAO.obtenerTodas();

        // Meto las listas en la petición para mostrarlas en la tabla del JSP de administración
        request.setAttribute("listaUsuarios", listaUsuarios);
        request.setAttribute("listaRecetas", listaRecetas);
        
        request.getRequestDispatcher("admin.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}