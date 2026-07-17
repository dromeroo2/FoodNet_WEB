package com.mycompany.foodnet.controlador;

import com.mycompany.foodnet.modelo.UsuarioDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "BorrarUsuarioServlet", urlPatterns = {"/borrarUsuario"})
public class BorrarUsuarioServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession sesion = request.getSession();
        Object rolObj = sesion.getAttribute("rol");
        Object miIdObj = sesion.getAttribute("id_usuario");
        
        // Compruebo seguridad: solo dejo pasar si el usuario tiene el rol de "admin"
        // Si no es admin, lo echo al timeline
        if (rolObj == null || !"admin".equals(rolObj.toString())) {
            response.sendRedirect("timeline");
            return;
        }

        String idStr = request.getParameter("id");
        if (idStr == null) {
            response.sendRedirect("admin");
            return;
        }

        int idUsuarioABorrar = Integer.parseInt(idStr);
        int miId = (int) miIdObj;
        
        // Comprobación importante: no puedo borrarme a mí mismo si estoy conectado
        if (idUsuarioABorrar == miId) {
            response.sendRedirect("admin?error=self");
            return;
        }

        // Llamo al DAO para que borre la cuenta entera.
        // El método eliminarCuentaCompleta se encarga de borrar también sus recetas, comentarios, etc.
        UsuarioDAO dao = new UsuarioDAO();
        boolean exito = dao.eliminarCuentaCompleta(idUsuarioABorrar);

        if (exito) {
            // Si todo ha ido bien, recargo la página de admin con mensaje de éxito
            response.sendRedirect("admin?msg=deleted");
        } else {
            // Si falla, muestro error de base de datos
            response.sendRedirect("admin?error=db");
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