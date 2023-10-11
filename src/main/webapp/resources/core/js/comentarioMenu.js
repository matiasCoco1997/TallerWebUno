//Esto despliega el menú de los comentarios (borrar/editar)
$(document).ready(function () {
    $(document).on("click", ".dropdown-comment-button", function (event) {
        /** Cuando se llama a stopPropagation(), se detiene la propagación del evento, lo que significa que el evento
        no se propagará más allá del elemento actual. Esto puede ser útil en situaciones donde tienes elementos anidados
        y deseas evitar que un evento se propague a elementos superiores.
        */
        event.stopPropagation();

        // Busc el menú que tiene que mostrar
        var dropdownMenu = $(this).siblings(".dropdown-menu");
        dropdownMenu.toggleClass("hidden");
    });

    // Oculta el menú si se hace clic fuera
    $(document).on("click", function (event) {
        // Verifica que el clic no está dentro del menú
        if (!$(event.target).closest(".dropdown-menu").length) {
            $(".dropdown-menu").addClass("hidden");
        }
    });
});
