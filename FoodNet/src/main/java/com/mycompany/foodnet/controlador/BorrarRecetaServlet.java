package com.mycompany.foodnet.controlador;

import com.mycompany.foodnet.modelo.Receta;
import com.mycompany.foodnet.modelo.RecetaDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "BorrarRecetaServlet", urlPatterns = {"/borrarReceta"})
public class BorrarRecetaServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Lo primero es comprobar si hay alguien logueado. Si no hay sesión, fuera.
        HttpSession sesion = request.getSession();
        Object idUsuarioObj = sesion.getAttribute("id_usuario");
        Object rolObj = sesion.getAttribute("rol");

        if (idUsuarioObj == null) {
            response.sendRedirect("index.html");
            return;
        }

        // Saco mi ID y compruebo si tengo rol de admin, porque los admin pueden borrar cualquier cosa
        int miId = (int) idUsuarioObj;
        boolean isAdmin = (rolObj != null && "admin".equals(rolObj.toString()));

        // Cojo el parámetro 'id' de la receta que queremos borrar
        String idRecetaStr = request.getParameter("id");
        if (idRecetaStr == null || idRecetaStr.isEmpty()) {
            response.sendRedirect("timeline");
            return;
        }

        int idReceta = Integer.parseInt(idRecetaStr);

        try {
            // Instancio el DAO para hablar con la base de datos
            RecetaDAO recetaDAO = new RecetaDAO();
            
            // Primero busco la receta para asegurarme de que existe y ver de quién es
            Receta receta = recetaDAO.obtenerPorId(idReceta);
            
            if (receta != null) {
                // Aquí hago la comprobación de seguridad: 
                // Solo permito borrarla si soy el autor (getIdAutor) O si soy el Administrador
                if (receta.getIdAutor() == miId || isAdmin) {
                    
                    // Si tengo permiso, llamo al método eliminar. 
                    // El DAO ya se encarga de limpiar los likes y comentarios asociados.
                    boolean exito = recetaDAO.eliminar(idReceta);
                    
                    if(exito) {
                        System.out.println("He borrado la receta " + idReceta);
                    } else {
                        System.out.println("Ha fallado el borrado de la receta " + idReceta);
                    }

                    // Ahora redirijo dependiendo de quién haya hecho el borrado
                    // Si vengo del panel de administración, vuelvo allí
                    if (isAdmin && request.getParameter("admin") != null) {
                        response.sendRedirect("admin");
                        return; 
                    }
                    
                    // Si soy un usuario normal borrando mi propia receta, vuelvo a mi perfil
                    response.sendRedirect("perfil");
                    return; 

                } else {
                    // Si alguien intenta borrar una receta que no es suya (trucando la URL), lo bloqueo
                    System.out.println("SEGURIDAD: Usuario " + miId + " intentó borrar receta ajena " + idReceta);
                    response.sendRedirect("timeline?error=no_autorizado");
                    return;
                }
            } else {
                // Si la receta no existe
                System.out.println("Error: La receta con ID " + idReceta + " no existe.");
                response.sendRedirect("timeline?error=no_existe");
                return;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            // Si hay algún error raro, devuelvo al timeline para que no se quede la pantalla en blanco
            if (!response.isCommitted()) {
                response.sendRedirect("timeline");
            }
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