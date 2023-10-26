$(document).ready(function () {
    $(document).on("click", ".dar-like-btn", function (event) {

        var buttonId = $(this).attr('id');

        $.ajax({
            type: "POST",
            url: "/sunn/noticia/likear",
            data: {
                idNoticia: buttonId
            },
            success: function(response) {

                var cantidadMG = parseInt($('.cantidadMG_'+ buttonId).text(), 10);

                if(isNaN(cantidadMG)){
                    cantidadMG = 0;
                    $('.cantidadMG_'+ buttonId).text(cantidadMG);
                }
                console.log(response);
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
});
















