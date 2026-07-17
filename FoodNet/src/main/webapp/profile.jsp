<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="com.mycompany.foodnet.modelo.Receta"%>
<%@page import="com.mycompany.foodnet.modelo.Usuario"%>

<%
//     Sacamos del request todo lo que el PerfilServlet nos ha preparado (usuario, sus recetas y sus favoritos) 
    Usuario usuario = (Usuario) request.getAttribute("usuario");
    List<Receta> misRecetas = (List<Receta>) request.getAttribute("misRecetas");
    List<Receta> recetasFavoritas = (List<Receta>) request.getAttribute("recetasFavoritas");
    
//     Si por lo que sea el usuario llega null (raro, pero posible), creamos uno vacío para que no explote la página con un NullPointerException 
    if (usuario == null) { 
        usuario = new Usuario(); 
    }
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Perfil - FoodNet</title>
    <link rel="stylesheet" href="css/styles.css">
    <link rel="stylesheet" href="css/dark-styles.css">
    <script src="js/dark-mode.js" defer></script>
    
    <style>
        /* Estilos específicos para la mini-tarjeta del perfil */
        .mini-card {
            cursor: pointer;
            transition: transform 0.2s, box-shadow 0.2s;
        }
        .mini-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 5px 15px rgba(0,0,0,0.2);
        }
        
        /* ESTILO PARA EL BOTÓN DE BORRAR */
        .btn-borrar-animado {
            position: absolute;
            top: 10px;
            right: 10px;
            width: 30px;
            height: 30px;
            background-color: #007bff;
            color: white;
            border-radius: 50%;
            text-align: center;
            line-height: 30px;
            text-decoration: none;
            z-index: 100;
            box-shadow: 0 2px 5px rgba(0,0,0,0.3);
            transition: all 0.2s ease-in-out;
        }

        .btn-borrar-animado:hover {
            background-color: #0056b3;
            transform: scale(1.15);    
            box-shadow: 0 4px 8px rgba(0,0,0,0.4);
        }
        
        /* Animación de apertura suave para las secciones */
        #mis-recetas-container, #mis-favoritos-container {
            animation: fadeIn 0.5s;
        }
        @keyframes fadeIn {
            from { opacity: 0; }
            to { opacity: 1; }
        }
    </style>
</head>
<body>
    <header>
        <h1>Perfil de Usuario</h1>
    </header>

    <div class="container">
        <div style="text-align: center; margin-bottom: 20px;">
<!--             Un ternario un poco loco para decidir qué foto mostrar: si tiene URL externa, ruta local o la por defecto -->
            <img src="<%= (usuario.getFoto() != null && !usuario.getFoto().isEmpty()) 
              ? (usuario.getFoto().startsWith("http") ? usuario.getFoto() : request.getContextPath() + "/" + usuario.getFoto()) 
              : "img/default_user.png" %>" 
             alt="Foto de perfil"
             style="width: 150px; height: 150px; border-radius: 50%; object-fit: cover; border: 4px solid #3498db; display: block; margin: 0 auto;">
        </div>

        <h2>Tu Información</h2>

<!--         Mostramos los datos con ternarios para poner textos por defecto ("Desconocido", "Sin email") si están vacíos -->
        <p><strong>Usuario:</strong> <%= (usuario.getUsername() != null) ? usuario.getUsername() : "Desconocido" %></p>
        
        <p><strong>Email:</strong> <%= (usuario.getEmail() != null) ? usuario.getEmail() : "Sin email" %></p>
        
        <p><strong>Bio:</strong> 
           <em><%= (usuario.getDescripcion() != null && !usuario.getDescripcion().isEmpty()) ? usuario.getDescripcion() : "Sin descripción" %></em>
        </p>

        <hr style="margin: 20px 0;">

        <p><strong>Gestionar Recetas:</strong></p>
        
<!--         Al hacer clic, llamamos a toggleRecetas() para mostrar/ocultar el div de abajo -->
        <button onclick="toggleRecetas()" class="cta-btn" style="width: 97.5%; display: block; box-sizing: border-box; margin-top: 10px;">
            📂 Tus Recetas Publicadas
        </button>

        <div id="mis-recetas-container" style="display: none; margin-top: 15px; border-top: 1px solid #eee; padding-top: 15px;">
            <% 
//             Si la lista no está vacía, la recorremos. Fíjate en el grid CSS para que quede bonito 
            if (misRecetas != null && !misRecetas.isEmpty()) {
                %>
                <div style="display: grid; grid-template-columns: repeat(auto-fill, minmax(150px, 1fr)); gap: 15px;">
                <%
                for (Receta r : misRecetas) { 
                    
//                     Aquí hacemos un poco de "magia" con Java para escapar comillas simples y dobles.
//                      Esto es vital, porque si el comentario tiene comillas, rompería la función JavaScript del modal más abajo. 
                    StringBuilder htmlComentarios = new StringBuilder();
                    if (r.getComentarios() != null && !r.getComentarios().isEmpty()) {
                        for (com.mycompany.foodnet.modelo.Comentario c : r.getComentarios()) {
                            String autorLimpio = c.getAutor().replace("'", "\\'");
                            String textoLimpio = c.getTexto().replace("'", "\\'").replace("\"", "&quot;");
                            htmlComentarios.append("<div style='border-bottom:1px solid #eee; padding:5px 0; font-size:0.9em;'>")
                                           .append("<strong style='color:#007bff;'>").append(autorLimpio).append(":</strong> ")
                                           .append("<span style='color:#555;'>").append(textoLimpio).append("</span>")
                                           .append("</div>");
                        }
                    } else {
                        htmlComentarios.append("<p style='color:#999; font-style:italic; font-size:0.9em;'>Sin comentarios aún.</p>");
                    }
                    String commentsString = htmlComentarios.toString(); 
                %>
                <div style="position: relative; border-radius: 8px; overflow: hidden; box-shadow: 0 2px 5px rgba(0,0,0,0.1); background: white;">
                    
<!--                     Solo aparece en MIS recetas. Lleva confirmación de JS para evitar sustos -->
                    <a href="borrarReceta?id=<%= r.getId() %>" 
                        onclick="return confirm('¿Seguro que quieres borrar esta receta?')"
                        class="btn-borrar-animado"
                        title="Borrar receta">
                        🗑️
                    </a>

<!--                     Pasamos un montón de parámetros a la función JS. Fíjate cómo pasamos 'commentsString' que preparamos antes -->
                    <div style="cursor: pointer;" 
                         onclick="abrirModalReceta(
                            '<%= r.getId() %>', 
                            '<%= r.getTitulo() %>', 
                            '<%= r.getImagen().trim().startsWith("http") ? r.getImagen() : request.getContextPath() + "/imagenes/" + r.getImagen() %>', 
                            '<%= r.getFecha() %>', 
                            `<%= r.getDescripcion() %>`,
                            '<%= r.getCantidadLikes() %>',
                            <%= r.isLikeadoPorMi() %>,
                            `<%= commentsString %>`,
                            '<%= r.getIdAutor() %>', 
                            '<%= r.getAutor() %>'
                         )">
                        
                        <img src="<%= r.getImagen().trim().startsWith("http") ? r.getImagen() : request.getContextPath() + "/imagenes/" + r.getImagen() %>" 
                             style="width: 100%; height: 120px; object-fit: cover;">
                        
                        <div style="padding: 8px;">
                            <h4 style="margin: 0; font-size: 0.9em; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;"><%= r.getTitulo() %></h4>
                            <small style="color: #e74c3c;">❤️ <%= r.getCantidadLikes() %></small>
                        </div>
                    </div>
                </div>
            <% 
                } 
                %>
                </div>
                <%
            } else { 
            %>
                <p style="text-align: center; color: #777;">No has publicado nada.</p>
            <% } %>
        </div>
        
        <button onclick="toggleFavoritos()" class="cta-btn" style="width: 97.5%; display: block; box-sizing: border-box; margin-top: 10px;">
            ❤️ Recetas Favoritas
        </button>

        <div id="mis-favoritos-container" style="display: none; margin-top: 15px; border-top: 1px solid #eee; padding-top: 15px;">
             <% 
//              BUCLE FAVORITOS Muy parecido al anterior, pero iterando sobre la lista de favoritos 
             if (recetasFavoritas != null && !recetasFavoritas.isEmpty()) { %>
                <div style="display: grid; grid-template-columns: repeat(auto-fill, minmax(150px, 1fr)); gap: 15px;">
                <% 
                for (Receta r : recetasFavoritas) { 
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
                    <div class="mini-card" style="position: relative; border-radius: 8px; overflow: hidden; background: white; box-shadow: 0 2px 5px rgba(0,0,0,0.1);">
                        
                        <div onclick="abrirModalReceta(
                                '<%= r.getId() %>', 
                                '<%= r.getTitulo() %>', 
                                '<%= r.getImagen().trim().startsWith("http") ? r.getImagen() : request.getContextPath() + "/imagenes/" + r.getImagen() %>', 
                                '<%= r.getFecha() %>', 
                                `<%= r.getDescripcion() %>`,
                                '<%= r.getCantidadLikes() %>',
                                <%= r.isLikeadoPorMi() %>,
                                `<%= commentsString %>`,
                                '<%= r.getIdAutor() %>',
                                '<%= r.getAutor() %>'
                             )" style="cursor: pointer;">
                            <img src="<%= r.getImagen().trim().startsWith("http") ? r.getImagen() : request.getContextPath() + "/imagenes/" + r.getImagen() %>" 
                                 style="width: 100%; height: 120px; object-fit: cover;">
                            <div style="padding: 8px;">
                                <h4 style="margin: 0; font-size: 0.9em; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;"><%= r.getTitulo() %></h4>
                                <small style="color: #555;">Por: <%= r.getAutor() %></small>
                            </div>
                        </div>
                    </div>
                <% } %>
                </div>
            <% } else { %>
                <p style="text-align: center; color: #777;">No tienes favoritos aún.</p>
            <% } %>
        </div>


        <div class="profile-buttons" style="margin-top: 30px;">
            <a href="modify.jsp" class="cta-btn">Modificar Perfil</a>
            <a href="timeline" class="btn-secondary">Volver al Timeline</a>
        </div>
        
        <button id="dark-mode-toggle" class="cta-btn" style="background:#444; float: right; margin-top: 15px;">
            Modo Oscuro 🌙
        </button>
    </div>

<!--     Es una ventana oculta (display:none) que se superpone a todo cuando le damos a una receta -->
    <div id="recipe-modal-backdrop" class="modal-backdrop" style="display:none; position:fixed; top:0; left:0; width:100%; height:100%; background:rgba(0,0,0,0.5); z-index:1000;"></div>
    
    <div id="recipe-modal" class="modal" style="display:none; position:fixed; top:50%; left:50%; transform:translate(-50%, -50%); background:white; padding:20px; z-index:1001; max-width:600px; width:90%; border-radius:8px; max-height:90vh; overflow-y:auto;">
        <span onclick="cerrarModal()" style="float:right; cursor:pointer; font-size:1.5em;">&times;</span>
        
        <h2 id="modal-title" style="color: #e67e22; margin-top: 0;"></h2>
        
        <p style="margin: 5px 0;">
            Por: <a id="modal-author-link" href="#" style="color: #3498db; text-decoration: none; font-weight: bold;">Usuario</a>
        </p>
        
        <p id="modal-date" style="color: #888; font-size: 0.8em;"></p>
        <img id="modal-img" src="" style="width:100%; max-height:300px; object-fit:cover; margin:10px 0; border-radius:8px;">
        <div id="modal-desc" style="margin-bottom: 20px;"></div>

        <hr style="border: 0; border-top: 1px solid #eee; margin: 15px 0;">

        <h4 style="margin: 0 0 10px 0; color: #333;">Comentarios:</h4>
        <div id="modal-existing-comments" style="max-height: 150px; overflow-y: auto; margin-bottom: 15px; background: #fafafa; padding: 10px; border-radius: 5px;">
        </div>

        <div style="display: flex; gap: 10px;">
            <form action="like" method="POST" style="margin:0;">
                <input type="hidden" name="id" id="modal-id-like">
                <button id="modal-btn-like" type="submit" style="padding:8px 15px; border-radius:5px; border:1px solid #ccc; cursor:pointer;">🤍</button>
            </form>
            <button onclick="document.getElementById('modal-comment-area').style.display='block'" style="padding:8px 15px; border-radius:5px; border:1px solid #ccc; cursor:pointer; background: #f0f2f5;">💬 Escribir Comentario</button>
        </div>

        <div id="modal-comment-area" style="display:none; margin-top:10px;">
            <form action="comentar" method="POST" style="display:flex; gap:5px;">
                <input type="hidden" name="id_receta" id="modal-id-comment">
                <input type="text" name="texto" placeholder="Escribe tu opinión..." required style="flex-grow:1; padding:8px; border:1px solid #ccc; border-radius:4px;">
                <button type="submit" style="background: #007bff; color: white; border: none; padding: 0 15px; border-radius: 4px; cursor: pointer;">Enviar</button>
            </form>
        </div>
    </div>

    <footer>&copy; 2025 David Romero Oñoro. Arquitectura de Sistemas Web y C/S.</footer>

    
    <script>
//         Funciones sencillas para mostrar/ocultar las secciones de recetas y favoritos 
        function toggleRecetas() {
            var c = document.getElementById("mis-recetas-container");
            var f = document.getElementById("mis-favoritos-container");
            c.style.display = (c.style.display === "none") ? "block" : "none";
            f.style.display = "none"; 
        }
        
        function toggleFavoritos() {
            var c = document.getElementById("mis-recetas-container");
            var f = document.getElementById("mis-favoritos-container");
            f.style.display = (f.style.display === "none") ? "block" : "none";
            c.style.display = "none"; 
        }

//         Esta función recibe todos los datos de la receta y rellena los huecos del HTML del modal 
        function abrirModalReceta(id, titulo, img, fecha, desc, likes, isLiked, commentsHtml, idAutor, nombreAutor) {
            document.getElementById('modal-title').textContent = titulo;
            document.getElementById('modal-img').src = img;
            document.getElementById('modal-date').textContent = fecha;
            document.getElementById('modal-desc').textContent = desc;
            
            // ACTUALIZAR EL ENLACE DEL AUTOR
            var authorLink = document.getElementById('modal-author-link');
            authorLink.textContent = nombreAutor;
            authorLink.href = 'perfil?id=' + idAutor;
            
            document.getElementById('modal-existing-comments').innerHTML = commentsHtml;
            
            document.getElementById('modal-id-like').value = id;
            document.getElementById('modal-id-comment').value = id;

            var btnLike = document.getElementById('modal-btn-like');
            // Limpiamos estilos inline antiguos para que mande el CSS
            btnLike.style.background = '';
            btnLike.style.color = '';
            btnLike.style.borderColor = '';

//             Cambiamos la clase CSS del corazón dependiendo de si isLiked es true o false 
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