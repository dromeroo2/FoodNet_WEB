<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Modificar Perfil - FoodNet</title>
    <link rel="stylesheet" href="css/styles.css">
    <link rel="stylesheet" href="css/dark-styles.css">
</head>
<body>
    <header>
        <h1>Ajustes de Perfil</h1>
    </header>

    <div class="container" style="max-width: 700px;">
        
<!--         Aquí miramos si el Servlet nos ha devuelto a esta página con algún error en la URL (por ejemplo ?error=pass) y mostramos el aviso correspondiente -->
        <% if (request.getParameter("error") != null) { %>
            <div style="background-color: #ffcccc; color: red; padding: 10px; border-radius: 5px; margin-bottom: 15px;">
                <% if (request.getParameter("error").equals("pass")) { %>
                    Error: La contraseña actual es incorrecta.
                <% } else if (request.getParameter("error").equals("match")) { %>
                    Error: Las nuevas contraseñas no coinciden.
                <% } else { %>
                    Ocurrió un error al guardar.
                <% } %>
            </div>
        <% } %>

<!--         El 'enctype="multipart/form-data"' es obligatorio siempre que queramos subir archivos (la foto nueva), si no se pone, la foto no llega al servidor -->
        <form action="modificar" method="POST" enctype="multipart/form-data">
            
            <h3 style="border-bottom: 1px solid #ccc; padding-bottom: 5px;">Perfil Público</h3>
            
            <div class="form-group">
                <label>Foto de Perfil:</label>
                <div style="display: flex; align-items: center; gap: 15px;">
                    <img src="img/default_user.png" style="width: 50px; height: 50px; border-radius: 50%; opacity: 0.6;">
                    <input type="file" name="fichero" accept="image/*">
                </div>
                <p style="font-size: 0.8em; color: #666; margin-top:5px;">Deja vacío para mantener la actual.</p>
            </div>

            <div class="form-group">
                <label>Sobre ti (Descripción):</label>
                <textarea name="descripcion" rows="3" placeholder="Actualiza tu biografía..."></textarea>
            </div>

            <h3 style="border-bottom: 1px solid #ccc; padding-bottom: 5px; margin-top: 30px;">Datos de Cuenta</h3>
            
            <div class="form-group">
                <label>Nombre de Usuario (No editable):</label>
<!--                 El nombre de usuario lo mostramos bloqueado (disabled) porque cambiarlo es un lío para la base de datos -->
                <input type="text" value="<%= session.getAttribute("usuario") %>" disabled style="background: #e9e9e9; cursor: not-allowed;">
            </div>

            <h3 style="border-bottom: 1px solid #ccc; padding-bottom: 5px; margin-top: 30px;">Cambiar Contraseña</h3>

            <div class="form-group">
                <label>Contraseña Actual (Requerida para cambios):</label>
                <p style="font-size: 0.8em; color: #666; margin-top:5px;">Rellena esto <strong>solo</strong> si deseas cambiar tu clave.</p>

<!--                 en el Servlet comprobaremos si estos campos están llenos. Si lo están, intentamos cambiar la contraseña, si no, los ignoramos -->
                <input type="password" name="passActual" placeholder="Tu contraseña actual">
            </div>
            

            <div class="form-group">
                <label>Nueva Contraseña:</label>
                <input type="password" name="passNueva" placeholder="Nueva contraseña">
            </div>

            <div class="form-group">
                <label>Confirmar Nueva Contraseña:</label>
                <input type="password" name="passConfirm" placeholder="Repite la nueva contraseña">
            </div>

            <div style="margin-top: 30px; display: flex; gap: 10px;">
                <button type="submit" class="cta-btn" style="flex: 1;">Guardar Todos los Cambios</button>
                <a href="perfil" class="btn-secondary" style="flex: 1; text-align: center; text-decoration: none; display: inline-block; padding: 30px;">Cancelar</a>
            </div>
        </form>
    </div>
</body>

    <footer>&copy; 2025 David Romero Oñoro. Arquitectura de Sistemas Web y C/S.</footer>

</html>