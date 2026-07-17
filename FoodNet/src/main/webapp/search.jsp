<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="com.mycompany.foodnet.modelo.Receta"%>
<%@page import="com.mycompany.foodnet.modelo.Usuario"%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Buscar - FoodNet</title>
    <link rel="stylesheet" href="css/styles.css">
    <link rel="stylesheet" href="css/dark-styles.css">
    <script src="js/dark-mode.js" defer></script>
</head>
<body>
    <header>
        <h1>Explorar FoodNet</h1>
    </header>

    <div class="container">
        
        <div class="search-card">
            
            <h2 style="text-align: center; margin-bottom: 20px; margin-top: 0;">Buscador</h2>

            <!-- FORMULARIO DE BÚSQUEDA: Aquí usamos GET en lugar de POST. ¿Por qué? Para que la búsqueda se vea en la URL (?q=tortilla) y se pueda compartir el enlace -->
            <form action="buscar" method="GET" class="search-form">
                <input type="text" name="q" class="full-width-input"
                       placeholder="Buscar personas, recetas, ingredientes..." 
                       value="<%= (request.getAttribute("busqueda") != null) ? request.getAttribute("busqueda") : "" %>">
                <button type="submit" class="cta-btn full-width-btn">Buscar</button>
            </form>

            <div style="margin-bottom: 30px;">
                <a href="timeline" class="btn-secondary full-width-btn">Volver al Inicio</a>
            </div>

            <%
//                 RECUPERAR DATOS: El Servlet nos ha mandado tres cosas: lo que buscó el usuario, la lista de personas y la lista de recetas 
                String busqueda = (String) request.getAttribute("busqueda");
                List<Usuario> usuarios = (List<Usuario>) request.getAttribute("listaUsuarios");
                List<Receta> recetas = (List<Receta>) request.getAttribute("listaRecetas");
                
//                 Si 'busqueda' tiene algo, es que ya hemos buscado. Si no, mostramos el mensaje de bienvenida abajo del todo 
                if (busqueda != null && !busqueda.isEmpty()) {
            %>
                <div style="text-align: left; margin-bottom: 20px; border-top: 1px solid #eee; padding-top: 20px;">
                    <p>Resultados para: <strong>"<%= busqueda %>"</strong></p>
                </div>

                <!-- SECCIÓN 1: USUARIOS ENCONTRADOS -->
                <div class="result-section">
                    <h3>👥 Usuarios Encontrados</h3>
                    <% if (usuarios != null && !usuarios.isEmpty()) { %>
                        <div class="user-list">
                            <% for (Usuario u : usuarios) { %>
                                <div class="user-card">
                                    <img src="<%= u.getFoto().startsWith("http") ? u.getFoto() : request.getContextPath() + "/" + u.getFoto() %>" onerror="this.src='img/default_user.png'">
                                    <div style="flex: 1;">
                                        <h4 style="margin: 0;">
                                            <a href="perfil?id=<%= u.getId() %>" style="color: #333; text-decoration: none;">
                                                <%= u.getUsername() %>
                                            </a>
                                        </h4>
                                        <small style="color: #666;"><%= (u.getDescripcion() != null) ? u.getDescripcion() : "Sin descripción" %></small>
                                    </div>
                                    <a href="perfil?id=<%= u.getId() %>" class="btn-secondary" style="font-size: 0.8em; padding: 5px 10px;">Ver</a>
                                </div>
                            <% } %>
                        </div>
                    <% } else { %>
                        <p style="color: #777; font-style: italic;">No se encontraron usuarios.</p>
                    <% } %>
                </div>

                <!-- SECCIÓN 2: RECETAS ENCONTRADAS -->
                <div class="result-section">
                    <h3>🍳 Recetas Encontradas</h3>
                    <% if (recetas != null && !recetas.isEmpty()) { %>
                        <div class="receta-grid">
                            <% for (Receta r : recetas) { 
//                                  PREPARAR COMENTARIOS: Lo mismo de siempre, escapamos caracteres para que no rompan el JavaScript del modal 
                                 StringBuilder htmlComentarios = new StringBuilder();
                                 if (r.getComentarios() != null) {
                                     for (com.mycompany.foodnet.modelo.Comentario c : r.getComentarios()) {
                                         String autor = c.getAutor().replace("'", "\\'");
                                         String texto = c.getTexto().replace("'", "\\'").replace("\"", "&quot;");
                                         htmlComentarios.append("<div><b>").append(autor).append(":</b> ").append(texto).append("</div>");
                                     }
                                 }
                                 String commentsString = htmlComentarios.toString();
                            %>
                                <!-- TARJETA DE RESULTADO: Al hacer click, abrimos el modal de detalle -->
                                <div class="receta-card"
                                     onclick="abrirModalReceta(
                                        '<%= r.getId() %>', 
                                        '<%= r.getTitulo() %>', 
                                        '<%= r.getImagen().trim().startsWith("http") ? r.getImagen() : request.getContextPath() + "/imagenes/" + r.getImagen() %>', 
                                        '<%= r.getFecha() %>', 
                                        `<%= r.getDescripcion() %>`,
                                        '<%= r.getCantidadLikes() %>',
                                        <%= r.isLikeadoPorMi() %>,
                                        `<%= commentsString %>`
                                     )">
                                    
                                    <img src="<%= r.getImagen().trim().startsWith("http") ? r.getImagen() : request.getContextPath() + "/imagenes/" + r.getImagen() %>" 
                                         style="width: 100%; height: 150px; object-fit: cover;"> 
                                    
                                    <div style="padding: 10px;">
                                        <h4 style="margin: 0; font-size: 1em; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;"><%= r.getTitulo() %></h4>
                                        <div style="display: flex; justify-content: space-between; margin-top: 5px;">
                                            <small style="color: #666;"><%= r.getAutor() %></small>
                                            <small style="color: #e74c3c;">❤️ <%= r.getCantidadLikes() %></small>
                                        </div>
                                    </div>
                                </div>
                            <% } %>
                        </div>
                    <% } else { %>
                        <p style="color: #777; font-style: italic;">No se encontraron recetas.</p>
                    <% } %>
                </div>

            <% } else { %>
                <!-- MENSAJE INICIAL: Esto sale solo cuando entras a buscar.jsp y aún no has escrito nada -->
                <div style="text-align: center; color: #888; margin: 20px 0;">
                    <p>Escribe algo arriba para empezar a buscar.</p>
                </div>
            <% } %>

        </div> 
    </div>

    <!-- MODAL DE DETALLE: Oculto por defecto, se muestra al hacer click en una receta -->
    <div id="recipe-modal-backdrop" class="modal-backdrop"></div>   
    
    <div id="recipe-modal" class="modal">
        <span onclick="cerrarModal()" style="float:right; cursor:pointer; font-size:1.5em;">&times;</span>
        
        <h2 id="modal-title" style="color: #e67e22; margin-top: 0;"></h2>
        <p id="modal-date" style="color: #888; font-size: 0.8em;"></p>
        <img id="modal-img" src="" style="width:100%; max-height:300px; object-fit:cover; margin:10px 0; border-radius:8px;">
        <div id="modal-desc" style="margin-bottom: 20px;"></div>

        <hr style="border: 0; border-top: 1px solid #eee; margin: 15px 0;">

        <h4 style="margin: 0 0 10px 0; color: #333;">Comentarios:</h4>
        <div id="modal-existing-comments" style="max-height: 150px; overflow-y: auto; margin-bottom: 15px; background: #fafafa; padding: 10px; border-radius: 5px;"></div>

        <div style="display: flex; gap: 10px;">
            <form action="like" method="POST" style="margin:0;">
                <input type="hidden" name="id" id="modal-id-like">
                <button id="modal-btn-like" type="submit" style="padding:8px 15px; border-radius:5px; border:1px solid #ccc; cursor:pointer;">🤍</button>
            </form>
            <button onclick="document.getElementById('modal-comment-area').style.display='block'" style="padding:8px 15px; border-radius:5px; border:1px solid #ccc; cursor:pointer; background: #f0f2f5;">💬 Comentar</button>
        </div>

        <div id="modal-comment-area" style="display:none; margin-top:10px;">
            <form action="comentar" method="POST" style="display:flex; gap:5px;">
                <input type="hidden" name="id_receta" id="modal-id-comment">
                <input type="text" name="texto" placeholder="Escribe..." required style="flex-grow:1; padding:8px; border:1px solid #ccc; border-radius:4px;">
                <button type="submit" style="background: #007bff; color: white; border: none; padding: 0 15px; border-radius: 4px; cursor: pointer;">Enviar</button>
            </form>
        </div>
    </div>

    <footer>&copy; 2025 David Romero Oñoro. Arquitectura de Sistemas Web y C/S.</footer>

    
    <script>
//         FUNCIONES JS: Para abrir el modal y pintar el corazón del like correctamente 
        function abrirModalReceta(id, titulo, img, fecha, desc, likes, isLiked, commentsHtml) {
            document.getElementById('modal-title').textContent = titulo;
            document.getElementById('modal-img').src = img;
            document.getElementById('modal-date').textContent = fecha;
            document.getElementById('modal-desc').textContent = desc;
            document.getElementById('modal-existing-comments').innerHTML = commentsHtml;
            document.getElementById('modal-id-like').value = id;
            document.getElementById('modal-id-comment').value = id;

            var btnLike = document.getElementById('modal-btn-like');
            if (isLiked === true || isLiked === 'true') {
                btnLike.innerHTML = '❤️ ' + likes;
                btnLike.style.background = '#ffebee';
                btnLike.style.color = '#e74c3c';
                btnLike.style.borderColor = '#e74c3c';
            } else {
                btnLike.innerHTML = '🤍 ' + likes;
                btnLike.style.background = 'white';
                btnLike.style.color = '#555';
            }

            document.getElementById('modal-comment-area').style.display = 'none';
            document.getElementById('recipe-modal').style.display = 'block';
            document.getElementById('recipe-modal-backdrop').style.display = 'block';
        }

        function cerrarModal() {
            document.getElementById('recipe-modal').style.display = 'none';
            document.getElementById('recipe-modal-backdrop').style.display = 'none';
        }
        document.getElementById('recipe-modal-backdrop').onclick = cerrarModal;
    </script>
</body>
</html>