$(document).ready(function () {
    $(document).on("click", ".dar-like-btn", function (event) {

        var buttonId = $(this).attr('id'); // Obtiene el ID del botón que se ha hecho clic

        $.ajax({
            type: "POST", // O el método que necesites
            url: "/sunn/noticia/likear", // La URL a la que deseas enviar la solicitud
            data: {
                idNoticia: buttonId
            },
            success: function(response) {

                var cantidadMG;
                var colorDelBoton = $('.like_' + buttonId).css('fill');
                console.log('Color actual:', colorDelBoton);

                if(colorDelBoton === "rgb(0, 128, 0)"){
                    $('.like_'+ buttonId).css('fill', 'white');
                } else {
                    $('.like_'+ buttonId).css('fill', 'green');
                }

                if(response){
                    //aca deberia sumar un me gusta a lo que tengo
                    cantidadMG = $('.cantidadMG_'+ buttonId).text();
                    console.log(cantidadMG);
                    cantidadMG++;
                    console.log(cantidadMG);
                } else {
                    //deberia restar
                }
            },
            error: function(error) {
                console.error(error);
            }
        });

    });
});
















