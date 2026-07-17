package com.mycompany.foodnet.controlador;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "LogoutServlet", urlPatterns = {"/logout"})
public class LogoutServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Intento coger la sesión actual. 
        // Le paso 'false' porque solo quiero obtenerla si ya existe; si no hay sesión, no tiene sentido crear una nueva para cerrarla al momento.
        HttpSession sesion = request.getSession(false);
        
        // Si encuentro una sesión activa, la invalido. 
        // Esto borra todos los atributos (usuario, id, rol...) que tenía guardados en el servidor.
        if (sesion != null) {
            sesion.invalidate();
        }
        
        // Una vez cerrada la sesión, mando al usuario de vuelta a la página de inicio (Login)
        response.sendRedirect("index.html");
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