$(document).ready(function() {
    // Selecciona el botón por su ID
    $('[id^="likeButton_"]').click(function() {
        var buttonId = $(this).attr('id'); // Obtiene el ID del botón que se ha hecho clic

        // Realiza la solicitud Ajax aquí
        $.ajax({
            type: "POST", // O el método que necesites
            url: "/sunn/noticia/likear", // La URL a la que deseas enviar la solicitud
            data: {
                buttonId: buttonId
            },
            success: function(response) {
                $(this).addClass();
            },
            error: function(error) {
                console.error(error);
            }
        });
    });
});


