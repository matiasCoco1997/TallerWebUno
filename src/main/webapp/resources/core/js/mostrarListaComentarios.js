$(document).ready(function () {
    $(document).on("click", ".mostrar-comentarios", function (event) {
        event.preventDefault();
        let idNoticia = $(this).data("id-noticia");

        let cajaComentarios = $("#lista-comentarios" + idNoticia);


            $.ajax({
                type: 'get',
                url: "/sunn/comentarios/publicacion/" + idNoticia,
                success: function (response) {
                    cajaComentarios.append(response);
                },
                error: function (error) {
                    console.error('Error al editar el comentario', error);
                }
            });

    });
});
