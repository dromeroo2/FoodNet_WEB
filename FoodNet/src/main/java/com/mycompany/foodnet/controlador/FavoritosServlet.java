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

@WebServlet(name = "FavoritosServlet", urlPatterns = {"/favoritos"})
public class FavoritosServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession sesion = request.getSession();
        Object idObj = sesion.getAttribute("id_usuario");
        
        // Comprobación básica de seguridad: si no estás logueado, no puedes ver favoritos
        if (idObj == null) {
            response.sendRedirect("index.html");
            return;
        }
        int miId = (int) idObj;

        // Uso el DAO para sacar solo las recetas a las que les he dado like
        RecetaDAO dao = new RecetaDAO();
        List<Receta> listaFavoritos = dao.obtenerFavoritos(miId);
        
        // Paso la lista a la vista 'favoritos.jsp' para que las muestre igual que el timeline
        request.setAttribute("listaFavoritos", listaFavoritos);
        request.getRequestDispatcher("favoritos.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}