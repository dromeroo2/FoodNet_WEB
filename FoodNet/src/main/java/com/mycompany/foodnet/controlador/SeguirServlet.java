package com.mycompany.foodnet.controlador;

import com.mycompany.foodnet.modelo.UsuarioDAO; 
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "SeguirServlet", urlPatterns = {"/seguir"})
public class SeguirServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Saco de la sesión mi propio ID y del parámetro el ID del usuario al que quiero seguir (o dejar de seguir)
        HttpSession sesion = request.getSession();
        Object idMioObj = sesion.getAttribute("id_usuario");
        String idTargetStr = request.getParameter("id");
        
        // Si hay algún fallo con los datos o no estoy logueado, vuelvo al timeline para evitar errores
        if (idMioObj == null || idTargetStr == null) {
            response.sendRedirect("timeline");
            return;
        }
        
        int idMio = (int) idMioObj;
        int idTarget = Integer.parseInt(idTargetStr);
        
        // Llamo al DAO para que gestione la relación.
        // El método 'toggle' funciona como un interruptor: si ya lo sigo, lo borra (dejar de seguir); si no, lo añade (seguir).
        // Así me ahorro tener dos servlets distintos.
        UsuarioDAO dao = new UsuarioDAO();
        dao.toggleSeguir(idMio, idTarget); 
        
        // Recargo la página del perfil que estaba visitando para que se actualice el botón
        response.sendRedirect("perfil?id=" + idTarget);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}