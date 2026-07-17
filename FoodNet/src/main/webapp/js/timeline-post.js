// Espera a que todo el contenido HTML esté cargado antes de ejecutar el script
document.addEventListener('DOMContentLoaded', function() {

    // 1. Datos de ejemplo para los posts
    const samplePosts = [
        {
            userName: "RecetasDeLaura",
            userImage: "https://placehold.co/40x40/E8AA42/FFFFFF?text=L",
            postImage: "https://placehold.co/600x400/FFD700/000000?text=Paella+Valenciana",
            title: "Mi Paella Valenciana Casera",
            description: "¡Hoy me he atrevido con una paella valenciana tradicional! El secreto está en un buen caldo y paciencia con el arroz. 🥘 \n\nPrimero preparamos el sofrito. Le añadimos la carne para que se dore. Finalmente añadimos el arroz y el caldo y esperamos que este listo! Recuerda dejar que se tueste un poco para conseguir ese socarrat que tanto nos gusta! \n\n#paella #comidaespañola"
        },
        {
            userName: "CocinaConMiguel",
            userImage: "https://placehold.co/40x40/2E8B57/FFFFFF?text=M",
            postImage: "https://placehold.co/600x400/8FBC8F/000000?text=Pasta+Pesto",
            title: "Pasta al Pesto Fresca",
            description: "No hay nada como una pasta con pesto de albahaca fresca del jardín. Rápido, fácil y delicioso. ¡Perfecto para la cena de hoy! 🌿"
        },
        {
            userName: "DulcesDeAna",
            userImage: "https://placehold.co/40x40/DDA0DD/FFFFFF?text=A",
            postImage: "https://placehold.co/600x400/FFC0CB/000000?text=Tarta+Chocolate",
            title: "Tarta de Chocolate y Frambuesas",
            description: "Mi última creación: tarta intensa de chocolate con un toque ácido de frambuesas. ¡Ha sido un éxito total en casa! 🍰\n\nPasos:\n1. Base: Tritura 200g de galletas tipo Oreo (con la crema) y mézclalas con 100g de mantequilla derretida. Presiona en un molde y refrigera 30 minutos.\n\n2. Relleno (Ganache): Calienta 250ml de nata (crema de leche) hasta que casi hierva. Vierte sobre 300g de chocolate negro troceado. Deja reposar 5 min y remueve hasta que esté suave.\n\n3. Montaje: Vierte la ganache sobre la base de galleta.\n\n4. Final: Deja enfriar en la nevera al menos 4 horas (o hasta que esté firme). Antes de servir, decora generosamente con frambuesas frescas.\n\n#postre #chocolate #tarta"
        },
        {
            userName: "CarlosMex",
            userImage: "https://placehold.co/40x40/D2691E/FFFFFF?text=C",
            postImage: "https://placehold.co/600x400/FF4500/FFFFFF?text=Tacos+Pastor",
            title: "Noche de Tacos al Pastor",
            description: "¡Martes de tacos! 🌮 He marinado la carne de cerdo con achiote, piña y especias durante 24 horas. El resultado es increíble. \n\nNo olvidéis acompañarlos con cebolla picada, cilantro fresco y una buena salsa verde. ¡Ándale! 🌶️ \n\n#tacos #mexicanfood #picante"
        },
        {
            userName: "HealthySara",
            userImage: "https://placehold.co/40x40/9ACD32/FFFFFF?text=S",
            postImage: "https://placehold.co/600x400/F0E68C/000000?text=Bowl+Desayuno",
            title: "Bowl de Avena y Frutas",
            description: "Empezando el día con energía 💪. Avena cocida con leche de almendras, topping de plátano, arándanos, semillas de chía y un toque de miel. \n\nEs súper saciante y tardas 5 minutos en hacerlo. ¿Cuál es vuestro desayuno favorito? 🥣🍌 #healthy #desayuno #fitness"
        },
        {
            userName: "ChefKenji",
            userImage: "https://placehold.co/40x40/DC143C/FFFFFF?text=K",
            postImage: "https://placehold.co/600x400/B22222/FFFFFF?text=Ramen+Tonkotsu",
            title: "Ramen Tonkotsu Casero",
            description: "Después de 12 horas hirviendo el caldo... ¡aquí está! 🍜 El esfuerzo merece la pena para conseguir ese caldo espeso y blanquecino típico del Tonkotsu. \n\nHe añadido huevo marinado (Ajitsuke Tamago), cebolleta china y Chashu (panceta braseada). ¡Itadakimasu! 🇯🇵 #ramen #japon #comidaasiatica"
        },
        {
            userName: "BurgerMaster",
            userImage: "https://placehold.co/40x40/8B4513/FFFFFF?text=B",
            postImage: "https://placehold.co/600x400/A0522D/FFFFFF?text=Smash+Burger",
            title: "La Smash Burger Definitiva",
            description: "Doble carne aplastada en la plancha bien caliente para esa costra crujiente, queso cheddar fundido y pan brioche tostado con mantequilla. 🍔🧀 \n\nSin complicaciones, solo sabor. A veces menos es más. \n\n#burger #cheatmeal"
        },
        {
            userName: "VerdeQueTeQuiero",
            userImage: "https://placehold.co/40x40/006400/FFFFFF?text=V",
            postImage: "https://placehold.co/600x400/90EE90/000000?text=Ensalada+Quinoa",
            title: "Ensalada de Quinoa y Aguacate",
            description: "Para contrarrestar los excesos del fin de semana. 🥗 Una ensalada fresca con base de quinoa, aguacate en su punto, tomates cherry, maíz y un aderezo de limón y cilantro.\n\nRefrescante, nutritiva y llena de color. 🌱 #vegano #vegetariano #realfood"
        },
        {
            userName: "AbuelaMaria",
            userImage: "https://placehold.co/40x40/708090/FFFFFF?text=A",
            postImage: "https://placehold.co/600x400/DAA520/000000?text=Lentejas",
            title: "Lentejas con Chorizo",
            description: "Como las de toda la vida. A fuego lento, con su chorizo, su morcilla y sus verduras bien picaditas. 🔥\n\nTruco de la abuela: añadir un chorrito de vinagre al final para asentar el hierro. Ideales para estos días de frío. 🍲 #comidacasera #cuchara #tradicion"
        },
        {
            userName: "BarmanAlex",
            userImage: "https://placehold.co/40x40/4682B4/FFFFFF?text=X",
            postImage: "https://placehold.co/600x400/00CED1/000000?text=Mojito+Cubano",
            title: "Mojito para el Viernes",
            description: "¡Por fin es viernes! 🎉 Y para celebrarlo, un mojito clásico.\n\nIngredientes:\n- Hierbabuena fresca (no menta)\n- Lima exprimida\n- Azúcar blanca\n- Ron blanco\n- Hielo picado y soda.\n\n¡Salud amigos! 🍹 #cocktail #viernes #tragos"
        }
    ];

    // Encontrar el contenedor del feed en el HTML
    const timelineFeed = document.getElementById('timeline-feed');

    // Generar y añadir los posts al HTML
    if (timelineFeed) {
        // Limpiamos el contenedor por si acaso (aunque aquí no haría falta)
        timelineFeed.innerHTML = ''; 

        // Recorremos cada post de ejemplo
        samplePosts.forEach(post => {
            // Creamos un elemento <div> para la tarjeta del post
            const postElement = document.createElement('div');
            postElement.classList.add('post-card'); // Añadimos la clase CSS

            // Procesamos la descripción para que respete saltos de línea
            const formattedDescription = post.description.replace(/\n/g, '<br>');

            // Rellenamos el HTML interno del post con los datos
            postElement.innerHTML = `
                <div class="post-header">
                    <img src="${post.userImage}" alt="Foto de ${post.userName}" class="post-user-img">
                    <span class="post-user-name">${post.userName}</span>
                </div>
                <img src="${post.postImage}" alt="Foto de ${post.title}" class="post-image">
                <div class="post-content">
                    <h3>${post.title}</h3>
                    
                    <p class="post-description">${formattedDescription}</p>
                    <a class="post-read-more">Ver más</a>
                    </div>
                <div class="post-actions">
                    <button class="post-like-btn">❤️ Me gusta</button>
                    <button class="post-comment-btn" data-post-title="${post.title}">💬 Comentar</button>
                    <button>🔗 Compartir</button>
                </div>
            `;

            
            // Buscar el botón "Me gusta" DENTRO del post que acabamos de crear
            const likeButton = postElement.querySelector('.post-like-btn');

            // Buscar el botón "Comentar" DENTRO del post
            const commentButton = postElement.querySelector('.post-comment-btn');

            // Añadir el detector de clics
            likeButton.addEventListener('click', function() {
                // 'this' se refiere al botón que fue pulsado
                
                // Toggle (alternar) una clase CSS para saber si ya tiene "me gusta"
                this.classList.toggle('liked'); 

                // Comprobar si ahora tiene la clase 'liked'
                if (this.classList.contains('liked')) {
                    // Animación/Cambio de "Me gusta"
                    this.innerHTML = '💖 ¡Te gusta!';
                    this.style.transform = 'scale(1.1)'; // Pequeña animación de "pop"
                } else {
                    // Estado original
                    this.innerHTML = '❤️ Me gusta';
                    this.style.transform = 'scale(1.0)'; // Vuelve al tamaño normal
                }
                
                // Quitar la animación de "pop" después de un tiempo
                setTimeout(() => {
                    this.style.transform = 'scale(1.0)';
                }, 150); // 150 milisegundos

            });

            // Añadir el detector de clics para Comentar
            commentButton.addEventListener('click', function() {
                // 'this' es el botón de comentario pulsado
                // Obtenemos el título guardado en el 'data-attribute'
                const postTitle = this.dataset.postTitle;
                
                // Llamamos a la función para abrir el modal (la crearemos abajo)
                openCommentModal(postTitle);
            });


            // CÓDIGO PARA VER MÁS
            // Seleccionar los elementos de Ver más DENTRO del post
            // (Hacemos esto ANTES de añadirlo al DOM)
            const descriptionP = postElement.querySelector('.post-description');
            const readMoreLink = postElement.querySelector('.post-read-more');

            // Añadimos la clase truncada por defecto
            descriptionP.classList.add('post-description-truncated');

            // Añadimos el evento de clic al enlace "Ver más"
            readMoreLink.addEventListener('click', function() {
                // Quita la clase que trunca el texto
                descriptionP.classList.remove('post-description-truncated');
                // Oculta el propio enlace "Ver más"
                readMoreLink.style.display = 'none';
            });

            // Añadimos el nuevo post al contenedor del feed
            timelineFeed.appendChild(postElement);


            // CÓDIGO DE COMPROBACIÓN
            // Comprobar si el texto realmente se está desbordando
            // (scrollHeight es la altura total, clientHeight es la altura visible)
            const isOverflowing = descriptionP.scrollHeight > descriptionP.clientHeight;

            if (isOverflowing) {
                // Si el texto es largo, mostramos el enlace "Ver más"
                readMoreLink.style.display = 'inline';
            } else {
                // Si el texto cabe, quitamos la clase de truncar 
                // (por si acaso) y nos aseguramos de que el enlace esté oculto
                descriptionP.classList.remove('post-description-truncated');
                readMoreLink.style.display = 'none';
            }

        });
    } else {
        console.error('Error: No se encontró el contenedor #timeline-feed');
    }


    // LÓGICA DEL MODAL DE POSTEAR
    // Seleccionar los elementos del DOM para el modal
    const openPostBtn = document.getElementById('open-post-modal-btn');
    const postModal = document.getElementById('post-modal');
    const postModalBackdrop = document.getElementById('post-modal-backdrop');
    const closePostBtn = document.getElementById('post-modal-close');
    const postForm = document.getElementById('post-form');

    // Funciones para abrir y cerrar
    function openPostModal() {
        postModalBackdrop.style.display = 'block';
        postModal.style.display = 'block';
    }

    function closePostModal() {
        postModalBackdrop.style.display = 'none';
        postModal.style.display = 'none';
    }

    // Asignar los eventos (con comprobaciones)
    // Al hacer clic en "Postear"
    if (openPostBtn) {
        openPostBtn.addEventListener('click', function(event) {
            event.preventDefault(); // Evita que el enlace '#' salte
            openPostModal();
        });
    }

    // Al hacer clic en la x
    if (closePostBtn) {
        closePostBtn.addEventListener('click', closePostModal);
    }

    // Al hacer clic en el fondo
    if (postModalBackdrop) {
        postModalBackdrop.addEventListener('click', closePostModal);
    }

    // Al enviar el formulario de "Publicar Receta"
    if (postForm) {
        postForm.addEventListener('submit', function(event) {
            event.preventDefault(); // Evita que la página se recargue

                // Recogemos los datos (simulación)
            const title = document.getElementById('post-title').value;
            const imageInput = document.getElementById('post-image');

            let feedbackMessage = `¡Receta "${title}" publicada!`;

            // Comprobamos si el usuario seleccionó un archivo
            if (imageInput.files.length > 0) {
                // .files[0].name nos da el nombre del archivo
                feedbackMessage += ` (Con la foto: ${imageInput.files[0].name})`;
            } else {

                // Guardo cuando va sin foto para ponerle un placeholder cuando se desarrolle
                // la siguiente parte de la práctica
                feedbackMessage += " (Sin foto adjunta)";
            }

            // Mostramos alerta de éxito mejorada
            alert(feedbackMessage);
            
            // Cerramos y limpiamos el formulario
            closePostModal();
            postForm.reset();
        });
    }


    // Seleccionamos los elementos
    const navBar = document.querySelector('.timeline-nav');
    const navAnchor = document.getElementById('timeline-nav-anchor');

    // Definimos el offset
    const stickyOffset = 20; // 20px

    // Creamos el observador
    if (navBar && navAnchor) {
        const observer = new IntersectionObserver(entries => {
            const entry = entries[0];
            
            // Comprobamos la posición del anchor <h2>
            // Si su borde inferior está por encima del offset, 
            // significa que el nav SÍ está pegado.
            if (entry.boundingClientRect.bottom < stickyOffset) {
                navBar.classList.add('stuck');
            } else {
                // Si no, no está pegado
                navBar.classList.remove('stuck');
            }
        }, {
            // Observamos el viewport entero
            threshold: [0, 1.0],
            root: null
        });

        // Empezamos a observar el anchor <h2>
        observer.observe(navAnchor);
    }


    // LÓGICA DEL MODAL DE COMENTAR
    // Seleccionar los elementos del modal de Comentario
    const commentModal = document.getElementById('comment-modal');
    const commentModalBackdrop = document.getElementById('comment-modal-backdrop');
    const closeCommentBtn = document.getElementById('comment-modal-close');
    const commentForm = document.getElementById('comment-form');

    // Funcion para abrir el modal de comentarios
    function openCommentModal(postTitle) {
        
        // Buscar si ya creamos el título dinámico anteriormente
        let titleEl = document.getElementById('dynamic-timeline-title');

        // Si NO existe (es la primera vez), lo creamos
        if (!titleEl) {
            titleEl = document.createElement('h4');
            titleEl.id = 'dynamic-timeline-title'; // ID único
            
            // Copiamos los estilos
            titleEl.style.color = '#555';
            titleEl.style.fontStyle = 'italic';
            
            // Lo insertamos justo antes del formulario
            commentForm.parentNode.insertBefore(titleEl, commentForm);
        }

        // Actualizamos el texto
        titleEl.textContent = `Comentando en: "${postTitle}"`;
        
        // Mostramos el modal
        commentModalBackdrop.style.display = 'block';
        commentModal.style.display = 'block';
    }

    // Función para cerrar el modal
    function closeCommentModal() {
        commentModalBackdrop.style.display = 'none';
        commentModal.style.display = 'none';
        commentForm.reset(); // Limpia el textarea al cerrar
    }

    // Asignar los eventos de cerrar
    if (closeCommentBtn) {
        closeCommentBtn.addEventListener('click', closeCommentModal);
    }
    if (commentModalBackdrop) {
        commentModalBackdrop.addEventListener('click', closeCommentModal);
    }

    // Manejar el envío del formulario de Comentario
    if (commentForm) {
        commentForm.addEventListener('submit', function(event) {
            event.preventDefault(); // Evita que la página se recargue
            
            const commentText = document.getElementById('comment-body').value;
            
            // Mostramos alerta de éxito
            alert(`Comentario "${commentText}" publicado`);
            
            // Cerramos y limpiamos el formulario
            closeCommentModal();
        });
    }


});