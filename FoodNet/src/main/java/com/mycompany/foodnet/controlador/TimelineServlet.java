package com.mycompany.foodnet.controlador;

import com.mycompany.foodnet.modelo.Receta;
import com.mycompany.foodnet.modelo.RecetaDAO; 
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "TimelineServlet", urlPatterns = {"/timeline"})
public class TimelineServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Comprobación de seguridad habitual: verifico si hay sesión abierta
        HttpSession sesion = request.getSession();
        Object idObj = sesion.getAttribute("id_usuario");
        
        // Si no encuentro el ID del usuario, lo redirijo a la pantalla de login
        if (idObj == null) {
            response.sendRedirect("index.html");
            return;
        }
        int miId = (int) idObj;

        // Instancio el DAO para acceder a los datos de las recetas
        RecetaDAO dao = new RecetaDAO();
        
        try {
            // Llamo al método 'obtenerFeed'. 
            // Esto simplifica mucho el código aquí porque el DAO ya se encarga de hacer la consulta SQL compleja,
            // sacar las recetas de la gente a la que sigo y mirar si yo les he dado like o no.
            List<Receta> listaRecetas = dao.obtenerFeed(miId);
            
            // Guardo la lista completa en la petición para enviarla al JSP
            request.setAttribute("listaRecetas", listaRecetas);
            
            // Paso el control a la vista 'timeline.jsp' para que muestre las tarjetas
            request.getRequestDispatcher("timeline.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            // Si ocurre algún error inesperado en la base de datos, devuelvo al usuario al inicio para que no vea una página de error en blanco
            response.sendRedirect("index.html?error=fatal");
        }
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