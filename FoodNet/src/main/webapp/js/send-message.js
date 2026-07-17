// Espera a que todo el contenido HTML esté cargado antes de ejecutar el script
document.addEventListener('DOMContentLoaded', function() {

    // Seleccionar los elementos del DOM
    // Botón que abre el modal
    const openModalBtn = document.querySelector('.cta-btn');
    
    // Modal
    const modal = document.getElementById('compose-modal');
    
    // Fondo oscuro detrás del modal
    const modalBackdrop = document.getElementById('modal-backdrop');
    
    // Botón x para cerrar el modal
    const closeModalBtn = document.querySelector('.modal-close');

    // Funciones para abrir y cerrar el modal
    function openModal() {
        modalBackdrop.style.display = 'block';
        modal.style.display = 'block';
    }

    function closeModal() {
        modalBackdrop.style.display = 'none';
        modal.style.display = 'none';
    }

    // Asignar los eventos
    // Al hacer clic en "Redactar Nuevo Mensaje"
    openModalBtn.addEventListener('click', function(event) {
        // Previene que el enlace <a> cause un salto en la página
        event.preventDefault(); 
        openModal();
    });

    // Al hacer clic en la x para cerrar
    closeModalBtn.addEventListener('click', closeModal);

    // Al hacer clic en el fondo fuera del modal
    modalBackdrop.addEventListener('click', closeModal);

    // Manejar el envío del formulario
    const composeForm = document.getElementById('compose-form');
    composeForm.addEventListener('submit', function(event) {
        event.preventDefault(); // Evita que la página se recargue
        
        // Aquí iría la lógica real para enviar el mensaje...
        
        // Mostramos una alerta de mensaje enviado
        alert('Mensaje enviado');
        
        // Cierra el modal y limpia el formulario
        closeModal();
        composeForm.reset();
    });

});