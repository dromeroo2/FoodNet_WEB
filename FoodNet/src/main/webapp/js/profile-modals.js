// Espera a que todo el contenido HTML esté cargado
document.addEventListener('DOMContentLoaded', function() {


    // Los dos botones que abrirán el modal
    const publishedBtn = document.getElementById('recipes-published');
    const likedBtn = document.getElementById('recipes-liked');

    // Los elementos del nuevo modal de "Aviso"
    const noticeModal = document.getElementById('notice-modal');
    const noticeBackdrop = document.getElementById('notice-modal-backdrop');
    const noticeCloseBtn = document.getElementById('notice-modal-close');
    const noticeText = document.getElementById('notice-modal-text');


    // Funciones para abrir y cerrar
    function openNoticeModal() {
        // Asignamos el texto (aunque ya está en el HTML, por si acaso)
        noticeText.textContent = 'Este apartado se realizará en la siguiente parte de la práctica.';
        
        // Mostramos el modal
        noticeBackdrop.style.display = 'block';
        noticeModal.style.display = 'block';
    }

    // Función para cerrar el modal
    function closeNoticeModal() {
        noticeBackdrop.style.display = 'none';
        noticeModal.style.display = 'none';
    }


    // Asignamos el evento de ABRIR a los dos botones
    if (publishedBtn) {
        publishedBtn.addEventListener('click', openNoticeModal);
    }
    if (likedBtn) {
        likedBtn.addEventListener('click', openNoticeModal);
    }

    // Asignamos los eventos de CERRAR
    if (noticeCloseBtn) {
        noticeCloseBtn.addEventListener('click', closeNoticeModal);
    }
    if (noticeBackdrop) {
        noticeBackdrop.addEventListener('click', closeNoticeModal);
    }

});