// Espera a que todo el contenido HTML esté cargado
document.addEventListener('DOMContentLoaded', function() {

    // Selecciona el formulario y el contenedor del mensaje
    const profileForm = document.querySelector('form');
    const successMessage = document.getElementById('success-message');

    // Escucha el evento 'submit' del formulario
    profileForm.addEventListener('submit', function(event) {
        
        // Evitar que la página se recargue
        event.preventDefault(); 

        // Mostrar el mensaje de éxito
        // Pone el texto en el div
        successMessage.textContent = '¡Perfil actualizado correctamente!';
        // Hace visible el div
        successMessage.style.display = 'block';

        // Ocultar el mensaje después de 3 segundos
        setTimeout(function() {
            successMessage.style.display = 'none';
        }, 3000); // 3000 milisegundos

        // Aquí irá el código para enviar datos al servidor
    });

});