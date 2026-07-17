<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="com.mycompany.foodnet.modelo.Receta"%>
<%@page import="com.mycompany.foodnet.modelo.Usuario"%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Perfil Público - FoodNet</title>
    <link rel="stylesheet" href="css/styles.css">
    <link rel="stylesheet" href="css/dark-styles.css">
    
    <style>
        .container { width: 95%; max-width: 800px; margin: 40px auto; box-sizing: border-box; text-align: center; }
        .mini-card { border: 1px solid #ddd; border-radius: 8px; overflow: hidden; background: white; transition: transform 0.2s; cursor: pointer; }
        .mini-card:hover { transform: translateY(-5px); box-shadow: 0 5px 15px rgba(0,0,0,0.1); }
        .grid-recetas { display: grid; grid-template-columns: repeat(auto-fill, minmax(200px, 1fr)); gap: 20px; margin-top: 30px; text-align: left; }
        .btn-follow { background-color: #3498db; color: white; border: none; padding: 10px 20px; border-radius: 5px; cursor: pointer; font-size: 1em; }
        .btn-unfollow { background-color: #95a5a6; color: white; border: none; padding: 10px 20px; border-radius: 5px; cursor: pointer; font-size: 1em; }
        .btn-unfollow:hover { background-color: #7f8c8d; }
    </style>
</head>
<body>
    <header>
        <h1>FoodNet</h1>
    </header>

    <div class="container">
        <%
//             RECUPERAR DATOS: Recogemos el usuario del perfil que visitamos, sus recetas y un booleano clave: 'loSigo' 
            Usuario u = (Usuario) request.getAttribute("usuario");
            List<Receta> recetas = (List<Receta>) request.getAttribute("misRecetas");
            
//             Esto nos dice si el botón debe poner 'Seguir' o 'Dejar de seguir' 
            Boolean loSigo = (Boolean) request.getAttribute("loSigo");
            if (loSigo == null) loSigo = false;
            
            if (u != null) {
        %>
            <div style="margin-bottom: 20px;">
<!--                 Lo de siempre, validamos si es URL, ruta local o null -->
                <img src="<%= u.getFoto().startsWith("http") ? u.getFoto() : request.getContextPath() + "/" + u.getFoto() %>" 
                     onerror="this.src='img/default_user.png'"
                     style="width: 120px; height: 120px; border-radius: 50%; object-fit: cover; border: 4px solid #3498db;">
                
                <h2 style="margin: 10px 0 5px;"><%= u.getUsername() %></h2>
                <p style="color: #666; font-style: italic; max-width: 600px; margin: 0 auto;">
                    <%= (u.getDescripcion() != null) ? u.getDescripcion() : "Sin descripción" %>
                </p>

<!--                 LÓGICA DEL BOTÓN SEGUIR: Dependiendo del booleano 'loSigo', mostramos un botón verde (+) o uno gris (Dejar de seguir) -->
                <form action="seguir" method="POST" style="margin-top: 15px;">
                    <input type="hidden" name="id" value="<%= u.getId() %>">
                    <% if (loSigo) { %>
                          <button type="submit" class="btn-unfollow">Dejar de Seguir</button>
                    <% } else { %>
                        <button type="submit" class="btn-follow">+ Seguir</button>
                    <% } %>
                </form>
                
<!--                 BOTÓN CHAT: Este enlace lleva directo al MessagesServlet pasando el ID del usuario para abrir la conversación -->
                <a href="mensajes?chatWith=<%= u.getId() %>" class="cta-btn" 
                    style="background-color: #2ecc71; text-decoration: none; padding: 11px 20px; margin-left: 10px; display: inline-block;">
                   💬 Enviar Mensaje
                </a>
                   
            </div>

            <hr>

            <h3 style="text-align: left; margin-top: 30px;">Recetas de <%= u.getUsername() %> (<%= recetas.size() %>)</h3>

            <% if (recetas != null && !recetas.isEmpty()) { %>
                <div class="grid-recetas">
                     <% for (Receta r : recetas) { 
//                         PREPARAR COMENTARIOS: Igual que en el perfil privado, construimos el HTML de los comentarios aquí para pasarlo al JS 
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
<!--                         TARJETA RECETA: Al hacer click, abrimos el modal con todos los datos -->
                        <div class="mini-card" 
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
                                    <small style="color: #888;"><%= r.getFecha() %></small>
                                    <small style="color: #e74c3c;">❤️ <%= r.getCantidadLikes() %></small>
                                </div>
                            </div>
                        </div>
                     <% } %>
                </div>
            <% } else { %>
                <p style="color: #777; margin-top: 30px;">Este usuario aún no ha publicado nada.</p>
            <% } %>

        <% } else { %>
            <p>Usuario no encontrado.</p>
        <% } %>

        <div style="margin-top: 50px;">
            <a href="timeline" class="btn-secondary">Volver al Timeline</a>
        </div>
    </div>

<!--     MODAL OCULTO: Estructura HTML para mostrar el detalle de la receta al hacer click -->
    <div id="recipe-modal-backdrop" class="modal-backdrop" style="display:none; position:fixed; top:0; left:0; width:100%; height:100%; background:rgba(0,0,0,0.5); z-index:1000;"></div>
    
    <div id="recipe-modal" class="modal" style="display:none; position:fixed; top:50%; left:50%; transform:translate(-50%, -50%); background:white; padding:20px; z-index:1001; max-width:600px; width:90%; border-radius:8px; max-height:90vh; overflow-y:auto;">
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

    <footer>
        &copy; 2025 David Romero Oñoro. Arquitectura de Sistemas Web y C/S.
    </footer>
    
    <script>
//         JAVASCRIPT MODAL: Rellena los datos de la ventana emergente y gestiona el color del corazón de Like 
        function abrirModalReceta(id, titulo, img, fecha, desc, likes, isLiked, commentsHtml) {
            document.getElementById('modal-title').textContent = titulo;
            document.getElementById('modal-img').src = img;
            document.getElementById('modal-date').textContent = fecha;
            document.getElementById('modal-desc').textContent = desc;
            document.getElementById('modal-existing-comments').innerHTML = commentsHtml;
            document.getElementById('modal-id-like').value = id;
            document.getElementById('modal-id-comment').value = id;

            var btnLike = document.getElementById('modal-btn-like');
             // Limpiamos estilos inline antiguos para que mande el CSS
            btnLike.style.background = '';
            btnLike.style.color = '';
            btnLike.style.borderColor = '';

            if (isLiked === true || isLiked === 'true') {
                btnLike.innerHTML = '❤️ ' + likes;
                btnLike.classList.add('liked'); // Usamos clase CSS
            } else {
                btnLike.innerHTML = '🤍 ' + likes;
                btnLike.classList.remove('liked'); // Quitamos clase
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