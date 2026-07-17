<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="com.mycompany.foodnet.modelo.Usuario"%>
<%@page import="com.mycompany.foodnet.modelo.Mensaje"%>

<!-- Aquí pescamos todo lo que el MensajeServlet ha metido en el request (contactos, mensajes antiguos) -->
<%
    // Recuperar ID de forma segura para saber quién soy yo
    Object idObj = session.getAttribute("id_usuario");
    int miId = (idObj != null) ? (int) idObj : 0;
    
    // Listas enviadas desde el Servlet
    List<Usuario> contactos = (List<Usuario>) request.getAttribute("misContactos");
    List<Mensaje> mensajes = (List<Mensaje>) request.getAttribute("listaMensajes");
    Usuario chatActual = (Usuario) request.getAttribute("chatActual"); // Con quién estoy hablando ahora
    List<Usuario> seguidos = (List<Usuario>) request.getAttribute("misSeguidos"); // Para empezar chats nuevos
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mensajes - FoodNet</title>
    <link rel="stylesheet" href="css/styles.css">
    <link rel="stylesheet" href="css/dark-styles.css">
    <script src="js/dark-mode.js" defer></script>
</head>
<body>
    <header>
        <h1>Tus Mensajes</h1>
        <p>Conversaciones con otros usuarios</p>
    </header>

    <div class="container" style="max-width: 1500 px; width: 50%">
        <div class="chat-layout">
            
<!--             BARRA LATERAL: Aquí pintamos la lista de gente con la que ya has hablado antes -->
            <div class="contacts-sidebar">
                <div class="sidebar-header">
                    <h4 style="margin:0; color:#666;">CHATS RECIENTES</h4>
                    <button onclick="abrirModalNuevoChat()" class="btn-new-chat" title="Nuevo Mensaje">+</button>
                </div>

                <% if (contactos != null && !contactos.isEmpty()) { 
                    for (Usuario c : contactos) { 
                        // Si este contacto es el del chat abierto, le añadimos la clase 'active' para resaltarlo 
                        boolean isActive = (chatActual != null && c.getId() == chatActual.getId());
                %>
                    <a href="mensajes?chatWith=<%= c.getId() %>" class="contact-item <%= isActive ? "active" : "" %>">
                        <img src="<%= c.getFoto().startsWith("http") ? c.getFoto() : request.getContextPath() + "/" + c.getFoto() %>" 
                             class="contact-img" onerror="this.src='img/default_user.png'">
                        <div>
                            <strong style="display:block; color: #000000"><%= c.getUsername() %></strong>
                            <small style="color: #888;">Abrir chat</small>
                        </div>
                    </a>
                <%  } 
                   } else { %>
                    <div style="padding: 20px; text-align: center; color: #999;">
                        <p>No tienes chats recientes.</p>
                    </div>
                <% } %>
            </div>

<!--             Esta parte cambia dependiendo de si hemos seleccionado a alguien o no -->
            <div class="chat-area">
                
<!--                 CASO A: TENEMOS UN CHAT ABIERTO (chatActual no es null) -->
                <% if (chatActual != null) { %>
                    <div class="chat-header">
                        <img src="<%= chatActual.getFoto().startsWith("http") ? chatActual.getFoto() : request.getContextPath() + "/" + chatActual.getFoto() %>" 
                             class="contact-img" onerror="this.src='img/default_user.png'" style="width:30px; height:30px;">
                        <span>Chat con 
                              <a href="perfil?id=<%= chatActual.getId() %>" class="link-perfil" style="color: #0000ff">
                              <%= chatActual.getUsername() %>
                              </a></span>
                    </div>

                    <div class="chat-messages" id="msg-container">
                        <% if (mensajes != null && !mensajes.isEmpty()) { 
                            for (Mensaje m : mensajes) { 
                                //  LÓGICA DE BURBUJAS: Si el idEmisor soy yo, el estilo es 'msg-me' (derecha), si no 'msg-other' (izquierda) 
                                boolean soyYo = (m.getIdEmisor() == miId);
                        %>
                            <div class="message-bubble <%= soyYo ? "msg-me" : "msg-other" %>">
                                <%= m.getTexto() %>
                                <span class="msg-date"><%= m.getFecha() %></span>
                            </div>
                        <%  } 
                           } else { %>
                            <p style="text-align: center; color: #999; margin-top: 20px;">No hay mensajes. ¡Saluda! 👋</p>
                        <% } %>
                    </div>

<!--                     El campo hidden es vital, le dice al Servlet A QUIÉN va el mensaje -->
                    <form action="mensajes" method="POST" class="chat-input-area">
                        <input type="hidden" name="id_receptor" value="<%= chatActual.getId() %>">
                        <input type="text" name="mensaje" class="chat-input" placeholder="Escribe un mensaje..." required autocomplete="off" autofocus>
                        <button type="submit" class="btn-send">Enviar</button>
                    </form>

<!--                 CASO B: NO HAY CHAT SELECCIONADO (Pantalla de bienvenida) -->
                <% } else { %>
                    <div style="display: flex; flex-direction: column; justify-content: center; align-items: center; height: 100%; color: #999;">
                        <span style="font-size: 3em;">💬</span>
                        <p>Selecciona un contacto o inicia un</p>
                        <button onclick="abrirModalNuevoChat()" class="cta-btn" style="padding: 8px 15px; font-size:0.9em;">Nuevo Chat</button>
                    </div>
                <% } %>
            </div>
        </div>

        <div style="margin-top: 20px; text-align: center;">
            <a href="timeline" class="btn-secondary">Volver al Timeline</a>
        </div>
    </div>

<!--     Aquí listamos a la gente que sigues (misSeguidos) para poder empezar una conversación de cero -->
    <div id="modal-backdrop" class="modal-backdrop" style="display: none;"></div>
    <div id="compose-modal" class="modal" style="display: none; max-width: 400px;">
        <div class="modal-content" style="padding: 0;">
            <div style="padding: 15px; border-bottom: 1px solid #eee; display:flex; justify-content:space-between; align-items:center;">
                <h3 style="margin:0;">Nuevo Mensaje</h3>
                <span onclick="cerrarModalNuevoChat()" style="cursor:pointer; font-size:1.5em;">&times;</span>
            </div>
            
            <div style="max-height: 300px; overflow-y: auto; padding: 0;">
                <p style="padding: 10px; margin:0; background:#f9f9f9; color:#666; font-size:0.9em;">Usuarios que sigues:</p>
                
                <% if (seguidos != null && !seguidos.isEmpty()) { 
                    for (Usuario u : seguidos) { %>
                    <a href="mensajes?chatWith=<%= u.getId() %>" class="modal-user-item">
                        <img src="<%= u.getFoto().startsWith("http") ? u.getFoto() : request.getContextPath() + "/" + u.getFoto() %>" 
                             class="contact-img" onerror="this.src='img/default_user.png'">
                        <strong><%= u.getUsername() %></strong>
                    </a>
                <%  } 
                   } else { %>
                    <div style="padding: 20px; text-align: center; color: #999;">
                        <p>No sigues a nadie todavía.</p>
                        <a href="search.jsp" style="color:#007bff;">Buscar gente</a>
                    </div>
                <% } %>
            </div>
        </div>
    </div>

    <footer>&copy; 2025 David Romero Oñoro. Arquitectura de Sistemas Web y C/S.</footer>
            
    <script>
        //  AUTO-SCROLL: Este pequeño script hace que el chat baje automáticamente al final para ver el último mensaje 
        var container = document.getElementById("msg-container");
        if (container) container.scrollTop = container.scrollHeight;

        function abrirModalNuevoChat() {
            document.getElementById('compose-modal').style.display = 'block';
            document.getElementById('modal-backdrop').style.display = 'block';
        }
        function cerrarModalNuevoChat() {
            document.getElementById('compose-modal').style.display = 'none';
            document.getElementById('modal-backdrop').style.display = 'none';
        }
        document.getElementById('modal-backdrop').onclick = cerrarModalNuevoChat;
    </script>
</body>
</html>