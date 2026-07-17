package com.mycompany.foodnet.controlador;

import com.mycompany.foodnet.modelo.Usuario;
import com.mycompany.foodnet.modelo.UsuarioDAO; 
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "RegistroServlet", urlPatterns = {"/registro"})
public class RegistroServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        // Importante para que no haya problemas con caracteres especiales (tildes, ñ...)
        request.setCharacterEncoding("UTF-8");
        
        // Recojo los datos que ha rellenado el usuario en el formulario de registro
        String user = request.getParameter("username");
        String email = request.getParameter("email");
        String pass = request.getParameter("password");

        // Preparo el objeto Usuario con los datos para pasárselo al modelo.
        // Solo relleno lo básico; el ID se genera solo y el rol por defecto será 'usuario' (lo gestiona el DAO).
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername(user);
        nuevoUsuario.setEmail(email);
        nuevoUsuario.setPassword(pass);

        // Instancio el DAO y llamo al método registrar para que haga el INSERT en la base de datos
        UsuarioDAO dao = new UsuarioDAO();
        boolean exito = dao.registrar(nuevoUsuario);

        // Compruebo si se ha guardado bien
        if (exito) {
            // Si todo ha ido bien, lo mando al login avisando de que ya puede entrar
            response.sendRedirect("index.html?registro=ok");
        } else {
            // Si falla (probablemente porque el usuario o email ya existen), lo devuelvo al formulario de registro
            response.sendRedirect("signup.html?error=repetido");
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
        processRequest(request, response);
    }
}