<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="com.mycompany.foodnet.modelo.Receta"%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Timeline - FoodNet</title>
    <link rel="stylesheet" href="css/styles.css">
    <link rel="stylesheet" href="css/dark-styles.css">
    <script src="js/dark-mode.js" defer></script>
    
</head>
<body>
    <header>
        <h1>Tu Timeline</h1>
        <p>Bienvenido, <%= session.getAttribute("usuario") %> </p>
    </header>

    <div class="container">
        <h2 id="timeline-nav-anchor">¿Qué deseas hacer?</h2>

        <div class="timeline-nav">
            <a href="#" id="open-post-modal-btn" class="cta-btn">Postear</a>
            <a href="perfil" class="cta-btn">Ver Perfil</a>
            <a href="buscar" class="cta-btn">Buscar</a>
            <a href="mensajes" class="cta-btn">Mensajes</a>
            
            <%-- BOTÓN EXCLUSIVO PARA ADMINISTRADORES --%>
            <% 
               String userRole = (String) session.getAttribute("rol");
               if (userRole != null && userRole.equals("admin")) { 
            %>
               <a href="admin" class="cta-btn" style="background-color: #2c3e50">
                   Panel Control
               </a>
            <% } %>
            
            <a href="logout" class="cta-btn" style="background:#4e4e4e; color:white">Cerrar Sesión</a>
        </div>
        
        <div class="feed">
                
                <%
                    List<Receta> lista = (List<Receta>) request.getAttribute("listaRecetas");
                    
                    // CASO 1: FEED VACÍO (No sigues a nadie o nadie ha publicado)
                    if (lista == null || lista.isEmpty()) {
                %>
                    <div class="card" style="text-align: center; padding: 40px;">
                        <img src="img/chef_icon.png" onerror="this.style.display='none'" style="width: 80px; opacity: 0.5;">
                        <h3 style="color: #666;">¡Tu Timeline está vacío!</h3>
                        <p>Parece que aún no sigues a nadie o tus amigos no han cocinado nada.</p>
                        <br>
                        <a href="search.jsp" class="cta-btn">🔍 Buscar cocineros para seguir</a>
                    </div>
                <%
                    } else {
                        // CASO 2: HAY RECETAS
                        for (Receta r : lista) {
                %>
                    <div class="card">
                        <div class="card-header" style="display: flex; align-items: center; gap: 10px; margin-bottom: 10px;">
                            <img src="${pageContext.request.contextPath}/<%= r.getFotoAutor() %>" 
                                 onerror="this.src='img/default_user.png'"
                                 style="width: 40px; height: 40px; border-radius: 50%; object-fit: cover;">
                            
                            <div>
                                <a href="perfil?id=<%= r.getIdAutor() %>" style="text-decoration: none; color: inherit; font-weight: bold;">
                                    <%= r.getAutor() %>
                                </a>
                                <br>
                                <span style="font-size: 0.8em; color: #888;"><%= r.getFecha() %></span>
                            </div>
                        </div>

                        <img src="<%= r.getImagen().trim().startsWith("http") ? r.getImagen() : request.getContextPath() + "/imagenes/" + r.getImagen() %>" 
                             alt="<%= r.getTitulo() %>" 
                             class="post-image"
                             onclick="abrirModalReceta('<%= r.getTitulo() %>', '<%= r.getImagen().trim().startsWith("http") ? r.getImagen() : request.getContextPath() + "/imagenes/" + r.getImagen() %>', '<%= r.getFecha() %>', `<%= r.getDescripcion() %>`)">
                       
                        <div class="card-content">
                            <h3><%= r.getTitulo() %></h3>
                            <p><%= r.getDescripcion() %></p>      
                            
                            <hr style="border: 0; border-top: 1px solid #eee; margin: 10px 0;">

                            <div class="actions">

                                <div style="display: flex; gap: 10px; align-items: center;">

                                    <form action="like" method="POST" style="margin: 0;">
                                        <input type="hidden" name="id" value="<%= r.getId() %>">

                                        <% if (r.isLikeadoPorMi()) { %>
                                            <button type="submit" class="btn-like liked">
                                                ❤️ <%= r.getCantidadLikes() %>
                                            </button>
                                        <% } else { %>
                                            <button type="submit" class="btn-like">
                                                🤍 <%= r.getCantidadLikes() %>
                                            </button>
                                        <% } %>
                                    </form>

                                    <button onclick="toggleComentario('box-<%= r.getId() %>')" style="background: #f0f2f5; border: 1px solid #ccc; padding: 5px 10px; border-radius: 5px; cursor: pointer; color: #333;">
                                        💬 Comentar
                                    </button>
                                </div>

                                <div id="box-<%= r.getId() %>" style="display: none; margin-top: 15px; background-color: #f9f9f9; padding: 10px; border-radius: 8px;">

                                    <form action="comentar" method="POST" style="display: flex; gap: 8px; margin-bottom: 10px;">
                                        <input type="hidden" name="id_receta" value="<%= r.getId() %>">
                                        <input type="text" name="texto" placeholder="Escribe un comentario..." required 
                                               style="flex-grow: 1; padding: 8px; border: 1px solid #ddd; border-radius: 4px;">
                                        <button type="submit" style="background: #3498db; color: white; border: none; padding: 8px 15px; border-radius: 4px; cursor: pointer;">
                                            Enviar
                                        </button>
                                    </form>

                                    <div style="max-height: 200px; overflow-y: auto;">
                                        <% if (r.getComentarios() != null && !r.getComentarios().isEmpty()) { %>
                                            <% for (com.mycompany.foodnet.modelo.Comentario c : r.getComentarios()) { %>
                                                <div style="border-bottom: 1px solid #eee; padding: 8px 0;">
                                                    <strong style="color: #2c3e50; font-size: 0.9em;"><%= c.getAutor() %></strong>
                                                    <span style="color: #999; font-size: 0.8em; float: right;"><%= c.getFecha() %></span>
                                                    <p style="margin: 3px 0 0 0; color: #555; font-size: 0.95em;"><%= c.getTexto() %></p>
                                                </div>
                                            <% } %>
                                        <% } else { %>
                                            <p style="color: #aaa; font-style: italic; font-size: 0.9em; text-align: center;">Sé el primero en comentar.</p>
                                        <% } %>
                                    </div>
                                </div>
                            </div>                          
                        </div>
                    </div>
                <%
                        }
                    }
                %>
            </div>
    </div>

    <div id="post-modal-backdrop" class="modal-backdrop"></div>
    <div id="post-modal" class="modal">
        <div class="modal-content">
            <span id="post-modal-close" class="modal-close">&times;</span>
            <h2>Postear Nueva Receta</h2>
            <form action="publicar" method="POST" enctype="multipart/form-data">
                <div class="form-group">
                    <label for="post-title">Título de la Receta:</label>
                    <input type="text" id="post-title" name="titulo" placeholder="Ej: Tortilla de Patatas" required>
                </div>

                <div class="form-group">
                    <label for="post-file">Subir Foto:</label>
                    <input type="file" id="post-file" name="fichero" accept="image/*">
                </div>

                <div class="form-group">
                    <label for="post-url">O pegar URL de imagen:</label>
                    <input type="text" id="post-url" name="imagen_url" placeholder="https://ejemplo.com/foto.jpg">
                </div>

                <div class="form-group">
                    <label for="post-description">Descripción e Ingredientes:</label>
                    <textarea id="post-description" name="descripcion" rows="6" placeholder="Pasos, ingredientes..." required></textarea>
                </div>

                <button type="submit" class="cta-btn">Publicar Receta</button>
            </form>
        </div>
    </div>

    <footer>&copy; 2025 David Romero Oñoro. Arquitectura de Sistemas Web y C/S.</footer>

    
    <script>
        const modal = document.getElementById('post-modal');
        const backdrop = document.getElementById('post-modal-backdrop');
        document.getElementById('open-post-modal-btn').onclick = () => {
            modal.style.display = 'block'; backdrop.style.display = 'block';
        };
        document.getElementById('post-modal-close').onclick = () => {
            modal.style.display = 'none'; backdrop.style.display = 'none';
        };
    </script>
    
    
    <script>
        function toggleComentario(id) {
            var box = document.getElementById(id);
            if (box.style.display === "none") {
                box.style.display = "block";
            } else {
                box.style.display = "none";
            }
        }
    </script>
</body>
</html>