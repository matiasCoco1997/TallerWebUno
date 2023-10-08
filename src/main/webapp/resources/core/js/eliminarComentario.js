$(document).ready(function () {
        $(document).on("click", ".eliminar-comentario", function (event) {
            event.preventDefault();

            let idComentario = $(this).data("id-comentario");


            console.log(idComentario);

            $.ajax({
                type: 'GET',
                url: "/sunn/comentario/"+idComentario,
                success: function (response) {
                    console.log(idComentario);
                    console.log('Comentario eliminado correctamente');
                    $('#comentario'+idComentario).remove();
                },
                error: function (error) {
                    console.error('Error al eliminar el comentario', error);
                }
            });
        });
 });

