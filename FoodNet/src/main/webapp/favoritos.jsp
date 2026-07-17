<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="com.mycompany.foodnet.modelo.Receta"%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mis Favoritos - FoodNet</title>
    <link rel="stylesheet" href="css/styles.css">
    <link rel="stylesheet" href="css/dark-styles.css">
</head>
<body>
    <header>
        <h1>❤️ Mis Recetas Favoritas</h1>
    </header>

    <div class="container">
        <div style="margin-bottom: 20px;">
            <a href="perfil" class="btn-secondary">← Volver a mi Perfil</a>
        </div>

        <!-- Aquí recuperamos la lista que nos ha mandado el Servlet FavoritosServlet -->
        <%
            List<Receta> lista = (List<Receta>) request.getAttribute("listaFavoritos");

            //  Comprobamos si la lista está vacía para mostrar un mensaje o las recetas 
            if (lista == null || lista.isEmpty()) {
        %>
            <div style="text-align: center; padding: 50px; color: #777;">
                <h2>💔 Aún no tienes favoritos</h2>
                <p>Ve al timeline y dale 'me gusta' a lo que te apetezca probar.</p>
                <a href="timeline" class="cta-btn" style="margin-top: 20px; display: inline-block;">Ir al Timeline</a>
            </div>
        <%
            } else {
                //  Si hay recetas, hacemos un bucle para pintar una tarjeta por cada una 
                for (Receta r : lista) {
        %>
            <div class="card">
                <div style="display: flex; align-items: center; gap: 10px; margin-bottom: 10px;">
                    
                    <!-- Esto de aquí es para arreglar la ruta de la foto, por si es una URL de internet o un archivo local -->
                    <img src="<%= r.getFotoAutor().trim().startsWith("http") ? r.getFotoAutor() : request.getContextPath() + "/imagenes/" + r.getFotoAutor() %>" 
                         onerror="this.src='img/default_user.png'"
                         style="width: 40px; height: 40px; border-radius: 50%; object-fit: cover;">
       
                    <div>
                        <a href="perfil?id=<%= r.getIdAutor() %>" style="text-decoration: none; color: inherit; font-weight: bold;">
                            <%= r.getAutor() %>
                        </a>
                        <br><span style="font-size: 0.8em; color: #888;"><%= r.getFecha() %></span>
                    </div>
                </div>

                <!-- Al hacer click en la imagen, llamamos a la función de JS para abrir la ventana modal -->
                <img src="<%= r.getImagen().trim().startsWith("http") ? r.getImagen() : request.getContextPath() + "/imagenes/" + r.getImagen() %>" 
                     alt="<%= r.getTitulo() %>" 
                     class="post-image"
                     onclick="abrirModalReceta('<%= r.getTitulo() %>', '<%= r.getImagen().trim().startsWith("http") ? r.getImagen() : request.getContextPath() + "/imagenes/" + r.getImagen() %>', '<%= r.getFecha() %>', `<%= r.getDescripcion() %>`)">

                 <div style="margin-top: 15px;">
                    <h3 style="margin-top: 0;"><%= r.getTitulo() %></h3>
                    <p><%= r.getDescripcion() %></p>
                    
                    <!-- Aquí el botón sale rojo fijo porque estamos en favoritos. Si le das, quitas el like -->
                    <form action="like" method="POST" style="margin-top: 10px;">
                        <input type="hidden" name="id" value="<%= r.getId() %>">
                        <button type="submit" class="btn-like liked" title="Quitar de favoritos">
                            ❤️ <%= r.getCantidadLikes() %>
                        </button>
                    </form>
                </div>
            </div>
        <%      } 
            } %>
    </div>

    <!-- Esto es el esqueleto de la ventana emergente (modal) que está oculto hasta que hacemos click -->
    <div id="recipe-modal-backdrop" class="modal-backdrop" style="display:none; position:fixed; top:0; left:0; width:100%; height:100%; background:rgba(0,0,0,0.5); z-index:1000;"></div>
    <div id="recipe-modal" class="modal" style="display:none; position:fixed; top:50%; left:50%; transform:translate(-50%, -50%); background:white; padding:20px; z-index:1001; max-width:600px; width:90%; border-radius:8px;">
        <span onclick="cerrarModal()" style="float:right; cursor:pointer; font-size:1.5em;">&times;</span>
        <h2 id="modal-title" style="color: #e67e22;"></h2>
        <p id="modal-date" style="color: #888;"></p>
        <img id="modal-img" src="" style="width:100%; max-height:300px; object-fit:cover; margin:10px 0;">
        <div id="modal-desc"></div>
    </div>

    <footer>&copy; 2025 David Romero Oñoro. Arquitectura de Sistemas Web y C/S.</footer>

    
    <script>
        //  Funciones de JavaScript para rellenar los datos de la modal y mostrarla 
        function abrirModalReceta(titulo, img, fecha, desc) {
            document.getElementById('modal-title').textContent = titulo;
            document.getElementById('modal-img').src = img;
            document.getElementById('modal-date').textContent = fecha;
            document.getElementById('modal-desc').textContent = desc;
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