$(document).ready(function () {
        $(document).on("click", ".eliminar-comentario", function (event) {
            event.preventDefault();
            let idComentario = $(this).data("id-comentario");
            $.ajax({
                type: 'DELETE',
                url: "/sunn/comentario/"+idComentario,
                success: function (response) {

                    $('#comentario'+idComentario).remove();
                },
                error: function (error) {
                    console.error('Error al eliminar el comentario', error);
                }
            });
        });
 });

