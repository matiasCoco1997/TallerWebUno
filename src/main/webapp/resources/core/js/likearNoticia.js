$(document).ready(function () {
    $(document).on("click", ".prueba", function (event) {

        var buttonId = $(this).attr('id'); // Obtiene el ID del botón que se ha hecho clic

        $.ajax({
            type: "POST", // O el método que necesites
            url: "/sunn/noticia/likear", // La URL a la que deseas enviar la solicitud
            data: {
                idNoticia: buttonId
            },
            success: function(response) {
                console.log("me gusta");
            },
            error: function(error) {
                console.error(error);
            }
        });

    });
});
















