document.addEventListener('DOMContentLoaded', function() {

    const form = document.querySelector('form');
    const btnRegister = document.getElementById('btn-register');
    const passwordMessage = document.getElementById('password-message');
    
    // --- 1. Validación Visual ---
    function checkPasswords() {
        const p1 = document.getElementById('password').value;
        const p2 = document.getElementById('confirm-password').value;

        if (p2 === '') { 
            passwordMessage.style.display = 'none'; 
            return; 
        }
        passwordMessage.style.display = 'block';

        if (p1 === p2) {
            passwordMessage.textContent = 'Las contraseñas coinciden';
            passwordMessage.style.color = 'green';
        } else {
            passwordMessage.textContent = 'Las contraseñas no coinciden';
            passwordMessage.style.color = 'red';
        }
    }

    // Listeners para feedback visual inmediato
    document.getElementById('password').addEventListener('input', checkPasswords);
    document.getElementById('confirm-password').addEventListener('input', checkPasswords);

    // --- 2. Lógica de Envío del Formulario ---
    if (btnRegister) {
        btnRegister.addEventListener('click', function(event) {
            // Prevenimos comportamiento por defecto para tener control total
            event.preventDefault(); 

            // Leemos valores
            const emailVal = document.getElementById('email').value;
            const pass1Val = document.getElementById('password').value;
            const pass2Val = document.getElementById('confirm-password').value;
            const userVal = document.getElementById('username').value;

            // Validación de campos vacíos
            if(userVal.trim() === "" || emailVal.trim() === "") {
                alert('Por favor, rellena todos los campos.');
                return;
            }

            // Validación A: Email simple
            const re = /\S+@\S+\.\S+/;
            if (!re.test(emailVal)) {
                alert('Error: Por favor, introduce un correo electrónico válido.');
                return;
            }

            // Validación B: Contraseñas
            if (pass1Val !== pass2Val) {
                alert('Error: Las contraseñas no coinciden.');
                return;
            }

            // Si todo es correcto, enviamos el formulario
            form.submit();
        });
    }
});