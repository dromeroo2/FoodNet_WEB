// Espera a que todo el contenido HTML esté cargado
document.addEventListener('DOMContentLoaded', function() {

    // Seleccionar los elementos
    const toggleBtn = document.getElementById('dark-mode-toggle');
    const body = document.body;

    // Función para actualizar el texto del botón
    function updateButtonText() {
        if (body.classList.contains('dark-mode')) {
            toggleBtn.textContent = 'Activar Modo Claro ☀️';
        } else {
            toggleBtn.textContent = 'Activar Modo Oscuro 🌙';
        }
    }

    // Comprobar la preferencia guardada al cargar la página
    const currentTheme = localStorage.getItem('theme');
    
    if (currentTheme === 'dark') {
        body.classList.add('dark-mode');
    }
    
    // Función para cambiar al modo oscuro
    if (toggleBtn) {

        // Actualiza el texto del botón si está en la página
        updateButtonText();

        // Añadir el evento de clic al botón
        toggleBtn.addEventListener('click', function() {
            // Alterna la clase en el <body>
            body.classList.toggle('dark-mode');
            
            // Guardar la preferencia en localStorage
            if (body.classList.contains('dark-mode')) {
                localStorage.setItem('theme', 'dark');
            } else {
                localStorage.setItem('theme', 'light');
            }
            
            // Actualizar el texto del botón
            updateButtonText();
        });
    }
});