package com.mycompany.foodnet.controlador;

import com.mycompany.foodnet.modelo.Usuario;
import com.mycompany.foodnet.modelo.UsuarioDAO; 
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Recojo lo que ha escrito el usuario en el formulario
        String user = request.getParameter("username");
        String pass = request.getParameter("password");
        
        // Llamo al DAO para comprobar si el usuario y la contraseña coinciden en la base de datos
        UsuarioDAO dao = new UsuarioDAO();
        Usuario usuarioLogueado = dao.login(user, pass);
        
        // Si el DAO me devuelve un usuario, es que todo ha ido bien
        if (usuarioLogueado != null) {
            // Creo la sesión para guardar sus datos y que no tenga que loguearse en cada página
            HttpSession sesion = request.getSession();
            sesion.setAttribute("usuario", usuarioLogueado.getUsername());
            sesion.setAttribute("id_usuario", usuarioLogueado.getId());
            
            // También me guardo el rol (que viene en la descripción) para saber los permisos
            String rol = usuarioLogueado.getDescripcion(); 
            sesion.setAttribute("rol", rol);

            // Dependiendo de si es admin o no, lo mando a un sitio diferente
            if ("admin".equals(rol)) {
                response.sendRedirect("admin");
            } else {
                response.sendRedirect("timeline");
            }
        } else {
            // Si el login falla, lo mando otra vez al principio con un mensaje de error
            response.sendRedirect("index.html?error=1");
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