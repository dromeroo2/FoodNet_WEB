package com.mycompany.foodnet.controlador;

import com.mycompany.foodnet.modelo.Receta;
import com.mycompany.foodnet.modelo.Usuario;
import com.mycompany.foodnet.modelo.RecetaDAO;
import com.mycompany.foodnet.modelo.UsuarioDAO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "BusquedaServlet", urlPatterns = {"/buscar"})
public class BusquedaServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Pongo esto para que no se rompan las tildes o caracteres raros al recibir el texto
        request.setCharacterEncoding("UTF-8");
        
        // Intento sacar el ID del usuario de la sesión. 
        // Si no está logueado (es null), uso 0 para que no falle la lógica
        HttpSession sesion = request.getSession();
        Object idObj = sesion.getAttribute("id_usuario");
        int miId = (idObj != null) ? (int) idObj : 0;
        
        // Cojo el texto que el usuario ha escrito en la barra de búsqueda
        String query = request.getParameter("q"); 
        
        // Preparo las listas vacías por defecto
        List<Usuario> usuariosEncontrados = new ArrayList<>();
        List<Receta> recetasEncontradas = new ArrayList<>();
        
        // Solo hago la consulta a la base de datos si han escrito algo de verdad
        if (query != null && !query.trim().isEmpty()) {
            // Instancio los DAOs para acceder a los datos
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            RecetaDAO recetaDAO = new RecetaDAO();
            
            // 1. Busco gente que se llame parecida a lo que han escrito
            usuariosEncontrados = usuarioDAO.buscar(query);
            
            // 2. Busco recetas por título o descripción. Paso miId para saber si les di like.
            recetasEncontradas = recetaDAO.buscar(query, miId);
        }
        
        // Guardo las listas y el texto original en la petición para mostrarlos en el JSP
        request.setAttribute("listaUsuarios", usuariosEncontrados);
        request.setAttribute("listaRecetas", recetasEncontradas);
        request.setAttribute("busqueda", query);
        
        // Envío todo a la página de resultados
        request.getRequestDispatcher("search.jsp").forward(request, response);
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