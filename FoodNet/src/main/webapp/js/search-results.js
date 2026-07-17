document.addEventListener('DOMContentLoaded', function() {

    // Datos de ejemplo
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
            userName: "BurgerMasterMiguel",
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

    // Seleccionar los elementos del DOM
    const searchForm = document.getElementById('search-form');
    const searchInput = document.getElementById('search');
    const resultsContainer = document.getElementById('search-results');

    // Elementos del modal
    const commentModal = document.getElementById('comment-modal');
    const commentModalBackdrop = document.getElementById('comment-modal-backdrop');
    const closeCommentBtn = document.getElementById('comment-modal-close');
    const commentForm = document.getElementById('comment-form');

    // Función para abrir la ventana emergente de comentarios
    function openCommentModal(postTitle) {

        // Buscar si ya creamos el título dinámico anteriormente
        let titleEl = document.getElementById('dynamic-post-title');

        // Si NO existe (es la primera vez que abrimos el modal) lo creamos
        if (!titleEl) {

            titleEl = document.createElement('h4');
            titleEl.id = 'dynamic-post-title'; // Le damos un ID para encontrarlo luego
            
            // Aplicamos los estilos que tenía el HTML
            titleEl.style.color = '#555';
            titleEl.style.fontStyle = 'italic';
            
            // Lo insertamos dentro del contenido del modal, justo antes del formulario
            commentForm.parentNode.insertBefore(titleEl, commentForm);
        }

        // Actualizamos el texto (ahora seguro que el elemento existe)
        titleEl.textContent = `Comentando en: "${postTitle}"`;

        // Mostrar el modal
        commentModalBackdrop.style.display = 'block';
        commentModal.style.display = 'block';
    }

    // Función para cerrar el modal de comentarios
    function closeCommentModal() {
        commentModalBackdrop.style.display = 'none';
        commentModal.style.display = 'none';
        commentForm.reset(); 
    }

    // Asignar eventos de cerrar
    if (closeCommentBtn) {
        closeCommentBtn.addEventListener('click', closeCommentModal);
    }
    if (commentModalBackdrop) {
        commentModalBackdrop.addEventListener('click', closeCommentModal);
    }

    // Manejar envío del comentario
    if (commentForm) {
        commentForm.addEventListener('submit', function(event) {
            event.preventDefault();
            const commentText = document.getElementById('comment-body').value;
            alert(`Comentario "${commentText}" publicado`);
            closeCommentModal();
        });
    }

    // Función reutilizable para mostrar posts
    function renderPosts(postsToRender) {
        resultsContainer.innerHTML = '';

        if (postsToRender.length === 0) {
            resultsContainer.innerHTML = '<p style="text-align:center; color:#777;">No se encontraron resultados para tu búsqueda.</p>';
            return;
        }

        postsToRender.forEach(post => {
            const postElement = document.createElement('div');
            postElement.classList.add('post-card'); 
            
            // Para respetar los saltos de línea
            const formattedDescription = post.description.replace(/\n/g, '<br>');

            // CORRECCIÓN: Añadir clases post-like-btn y post-comment-btn
            // También añadimos data-post-title
            postElement.innerHTML = `
                <div class="post-header">
                    <img src="${post.userImage}" alt="Foto de ${post.userName}" class="post-user-img">
                    <span class="post-user-name">${post.userName}</span>
                </div>
                <img src="${post.postImage}" alt="Foto de ${post.title}" class="post-image">
                <div class="post-content">
                    <h3>${post.title}</h3>
                    <p>${formattedDescription}</p>
                </div>
                <div class="post-actions">
                    <button class="post-like-btn">❤️ Me gusta</button>
                    <button class="post-comment-btn" data-post-title="${post.title}">💬 Comentar</button>
                    <button>🔗 Compartir</button>
                </div>
            `;


            // LÓGICA DE INTERACCIÓN
            // Funcionalidad ME GUSTA
            const likeButton = postElement.querySelector('.post-like-btn');
            if (likeButton) { // Verificamos que existe por seguridad
                likeButton.addEventListener('click', function() {
                    this.classList.toggle('liked'); 
                    if (this.classList.contains('liked')) {
                            this.innerHTML = '💖 ¡Te gusta!';
                            this.style.transform = 'scale(1.1)'; 
                    } else {
                            this.innerHTML = '❤️ Me gusta';
                            this.style.transform = 'scale(1.0)'; 
                    }
                    setTimeout(() => {
                        this.style.transform = 'scale(1.0)';
                    }, 150);
                });
            }

            // Funcionalidad COMENTAR
            const commentButton = postElement.querySelector('.post-comment-btn');
            if (commentButton) {
                commentButton.addEventListener('click', function() {
                    const postTitle = this.dataset.postTitle;
                    // Ahora esta función SÍ existe porque la definimos arriba del todo
                    openCommentModal(postTitle); 
                });
            }

            resultsContainer.appendChild(postElement);
        });
    }

    // Añadir el listener al formulario de búsqueda
    searchForm.addEventListener('submit', function(event) {
        event.preventDefault(); 
        const query = searchInput.value.toLowerCase().trim();

        if (query === "") {
            renderPosts(samplePosts);
            return;
        }

        const filteredPosts = samplePosts.filter(post => {
            const titleMatch = post.title.toLowerCase().includes(query);
            const descriptionMatch = post.description.toLowerCase().includes(query);
            const userMatch = post.userName.toLowerCase().includes(query);
            return titleMatch || descriptionMatch || userMatch;
        });

        renderPosts(filteredPosts);
    });

    // CARGA INICIAL
    renderPosts(samplePosts);

});