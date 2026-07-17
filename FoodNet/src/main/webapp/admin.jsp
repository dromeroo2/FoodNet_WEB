<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="com.mycompany.foodnet.modelo.Usuario"%>
<%@page import="com.mycompany.foodnet.modelo.Receta"%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Panel Admin - FoodNet</title>
    <link rel="stylesheet" href="css/styles.css">
    <link rel="stylesheet" href="css/dark-styles.css">
    <script src="js/dark-mode.js" defer></script>
    
    <style>
        .admin-card {
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
            padding: 20px;
            margin-bottom: 20px;
        }
        .admin-table { width: 100%; border-collapse: collapse; margin-top: 15px; }
        .admin-table th, .admin-table td { padding: 12px; text-align: left; border-bottom: 1px solid #ddd; }
        .admin-table th { background-color: #f8f9fa; color: #333; }
        
        .btn-delete {
            background-color: #e74c3c; color: white; border: none;
            padding: 5px 10px; border-radius: 4px; cursor: pointer; text-decoration: none; font-size: 0.9em;
        }
        .btn-delete:hover { background-color: #c0392b; }
        
        .panel-toggle { display: none; } /* Ocultos por defecto */
        .panel-active { display: block; animation: fadeIn 0.3s; }
        
        @keyframes fadeIn { from { opacity:0; } to { opacity:1; } }
    </style>
</head>
<body>
    <header style="background: #2c3e50;"> <h1>🔧 Panel de Control</h1>
        <p>Administrador: <%= session.getAttribute("usuario") %></p>
    </header>

    <div class="container">
        <div style="display: flex; gap: 10px; margin-bottom: 20px;">
            <button onclick="showPanel('users')" class="cta-btn" style="flex: 1;">👥 Gestionar Usuarios</button>
            <button onclick="showPanel('recipes')" class="cta-btn" style="flex: 1; background: #e67e22;">🍳 Gestionar Recetas</button>
            <a href="timeline" class="btn-secondary" style="text-decoration: none; padding: 12px 20px;">Salir</a>
        </div>

        <div id="panel-users" class="admin-card panel-toggle panel-active">
            <h3>Listado de Usuarios Registrados</h3>
            <table class="admin-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Usuario</th>
                        <th>Email</th>
                        <th>Acción</th>
                    </tr>
                </thead>
                <tbody>
                    <% 
                    List<Usuario> usuarios = (List<Usuario>) request.getAttribute("listaUsuarios");
                    if(usuarios != null) {
                        for(Usuario u : usuarios) { %>
                        <tr>
                            <td>#<%= u.getId() %></td>
                            <td>
                                <img src="<%= u.getFoto().startsWith("http") ? u.getFoto() : request.getContextPath()+"/"+u.getFoto() %>" 
                                     style="width: 30px; height: 30px; border-radius: 50%; vertical-align: middle; margin-right: 10px;"
                                     onerror="this.src='img/default_user.png'">
                                <strong><%= u.getUsername() %></strong>
                            </td>
                            <td><%= u.getEmail() %></td>
                            <td>
                                <a href="borrarUsuario?id=<%= u.getId() %>" 
                                    onclick="return confirm('¡PELIGRO! ¿Estás seguro de eliminar a este usuario y TODOS sus datos (recetas, mensajes, etc)? Esta acción es irreversible.')" 
                                    class="btn-delete">
                                    Eliminar
                                 </a>
                            </td>
                        </tr>
                    <%  } 
                    } %>
                </tbody>
            </table>
        </div>

        <div id="panel-recipes" class="admin-card panel-toggle">
            <h3>Listado de Todas las Recetas</h3>
            <table class="admin-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Título</th>
                        <th>Autor</th>
                        <th>Fecha</th>
                        <th>Acción</th>
                    </tr>
                </thead>
                <tbody>
                    <% 
                    List<Receta> recetas = (List<Receta>) request.getAttribute("listaRecetas");
                    if(recetas != null) {
                        for(Receta r : recetas) { %>
                        <tr>
                            <td>#<%= r.getId() %></td>
                            <td><strong><%= r.getTitulo() %></strong></td>
                            <td><%= r.getAutor() %></td>
                            <td><small><%= r.getFecha() %></small></td>
                            <td>
                                <a href="borrarReceta?id=<%= r.getId() %>&admin=true" 
                                   onclick="return confirm('¿Borrar esta receta permanentemente?')" 
                                   class="btn-delete">Borrar</a>
                            </td>
                        </tr>
                    <%  } 
                    } %>
                </tbody>
            </table>
        </div>

    </div>

    <footer>&copy; 2025 David Romero Oñoro - Arquitectura de Sistemas Web y C/S</footer>

    <script>
        function showPanel(panelName) {
            // Ocultar todos
            document.getElementById('panel-users').style.display = 'none';
            document.getElementById('panel-recipes').style.display = 'none';
            
            // Mostrar el elegido
            if(panelName === 'users') {
                document.getElementById('panel-users').style.display = 'block';
            } else {
                document.getElementById('panel-recipes').style.display = 'block';
            }
        }
    </script>
</body>
</html>