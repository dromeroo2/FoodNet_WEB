package com.mycompany.foodnet.controlador;

import com.mycompany.foodnet.modelo.Mensaje;
import com.mycompany.foodnet.modelo.Usuario;
import com.mycompany.foodnet.modelo.MensajeDAO; 
import com.mycompany.foodnet.modelo.UsuarioDAO; 
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "MensajesServlet", urlPatterns = {"/mensajes"}) 
public class MensajesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Comprobación de seguridad habitual: si no hay sesión, fuera.
        HttpSession sesion = request.getSession();
        Object idObj = sesion.getAttribute("id_usuario");
        
        if (idObj == null) {
            response.sendRedirect("index.html");
            return;
        }
        int miId = (int) idObj;
        
        // Miro si me pasan por la URL el ID del usuario con el que quiero hablar
        String chatWithParam = request.getParameter("chatWith");
        
        // Instancio los DAOs porque voy a necesitar datos de usuarios y de mensajes
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        MensajeDAO mensajeDAO = new MensajeDAO();

        // 1. Cargo la lista de contactos para la barra lateral (gente con la que ya he hablado antes)
        List<Usuario> misContactos = usuarioDAO.obtenerContactosChat(miId);
        request.setAttribute("misContactos", misContactos);
        
        // 2. Lógica para decidir qué conversación abrir:
        // Si no me han dicho con quién hablar (chatWith es null), pero tengo contactos, abro el primero por defecto para que no salga vacío.
        if ((chatWithParam == null || chatWithParam.isEmpty()) && !misContactos.isEmpty()) {
            chatWithParam = String.valueOf(misContactos.get(0).getId());
        }

        // Si ya tengo claro con quién hablar (porque me lo pasaron o porque cogí el primero)
        if (chatWithParam != null && !chatWithParam.isEmpty()) {
            try {
                int idOtro = Integer.parseInt(chatWithParam);
                
                // Cargo la info de ese usuario para poner su nombre y foto en la cabecera del chat
                Usuario otroUsuario = usuarioDAO.obtenerPorId(idOtro);
                request.setAttribute("chatActual", otroUsuario);
                
                // Y uso el DAO para traer toda la lista de mensajes entre él y yo
                List<Mensaje> conversacion = mensajeDAO.obtenerConversacion(miId, idOtro);
                request.setAttribute("listaMensajes", conversacion);
            } catch (NumberFormatException e) {
                // Si el ID no es un número válido, no hago nada
            }
        }
        
        // 3. También cargo la lista de gente a la que sigo.
        // Esto sirve para el botón de "Nuevo Mensaje", para poder elegir a quién escribir de cero.
        List<Usuario> seguidos = usuarioDAO.obtenerSeguidos(miId);
        request.setAttribute("misSeguidos", seguidos);
        
        // Mando todo a la vista
        request.getRequestDispatcher("messages.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Ajusto la codificación para que se guarden bien los acentos
        request.setCharacterEncoding("UTF-8");
        HttpSession sesion = request.getSession();
        Object idObj = sesion.getAttribute("id_usuario");
        
        if (idObj == null) {
            response.sendRedirect("index.html");
            return;
        }
        
        int miId = (int) idObj;
        String idReceptorStr = request.getParameter("id_receptor");
        String texto = request.getParameter("mensaje");

        // Solo envío el mensaje si tengo a quién enviarlo y el texto no está vacío
        if (idReceptorStr != null && texto != null && !texto.trim().isEmpty()) {
            try {
                int idReceptor = Integer.parseInt(idReceptorStr);
                
                // Uso el DAO para guardar el mensaje en la base de datos
                MensajeDAO mensajeDAO = new MensajeDAO();
                mensajeDAO.enviar(miId, idReceptor, texto);
                
                // Recargo la página abriendo directamente el chat con esa persona
                response.sendRedirect("mensajes?chatWith=" + idReceptor);
            } catch (NumberFormatException e) {
                response.sendRedirect("mensajes");
            }
        } else {
            response.sendRedirect("mensajes");
        }
    }
}