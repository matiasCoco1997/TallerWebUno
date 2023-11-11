$(document).ready(function () {

    function seleccionarUsuario() {
        // Obtener el valor seleccionado
        var selectedValue = $("#selectUsuarios").val();

        // Realizar la llamada AJAX
        $.ajax({
            type: "POST", // Puedes cambiarlo a "GET" si es más apropiado
            url: "/sunn/perfil/filtrarCompartidosPorUsuario", // Reemplaza esto con la URL correcta para tu llamada AJAX
            data: { usuarioSeleccionado: selectedValue },
            success: function (response) {
                console.log(response);
            },
            error: function (error) {
                console.error(error);
            }
        });
    }
});

/*
$(document).on("click", ".dar-like-btn", function () {

    var buttonId = $(this).attr('id');

    $.ajax({
        type: "POST",
        url: "/sunn/noticia/likear",
        data: {
            idNoticia: buttonId
        },
        success: function(response) {

            var cantidadMG = parseInt($('.cantidadMG_'+ buttonId).text(), 10);

            if(isNaN(cantidadMG) || cantidadMG === 0){
                cantidadMG = 0;
                $('.cantidadMG_'+ buttonId).text(cantidadMG);
            }

            if(response){
                cantidadMG++;
                $('.cantidadMG_'+ buttonId).text(cantidadMG);
                $('.like_'+ buttonId).css('fill', 'green');
            } else {
                cantidadMG--;
                $('.cantidadMG_'+ buttonId).text(cantidadMG);
                $('.like_'+ buttonId).css('fill', 'white');
            }

            console.log("cantidad de mg = " + cantidadMG);
        },
        error: function(error) {
            console.error(error);
        }
    });

});
 */
