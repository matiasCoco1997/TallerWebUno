$(document).ready(function () {
    $(document).on("click", ".editar-comentario", function (event) {
        event.preventDefault();
        let idComentario = $(this).data("id-comentario");
        let formulario = $("#formularioEdicionComentario" + idComentario);

        if (formulario.length > 0) {
            formulario.remove();
        } else {
            $.ajax({
                type: 'get',
                url: "/sunn/comentario/" + idComentario,
                success: function (response) {
                    $('#comentario' + idComentario).append(response);
                },
                error: function (error) {
                    console.error('Error al editar el comentario', error);
                }
            });
        }
    });

    $(document).on("submit", ".formularioEdicionComentario", function (event) {
        event.preventDefault();
        let form = $(this);
        let idComentario = $(this).data("id-comentario");
        $.ajax({
            type: 'post',
            url: "/sunn/comentario/editar",
            data: form.serialize(),
            success: function (response) {
                $('#comment-text-'+idComentario).text( form.find('textarea[name="descripcion"]').val());
                form.remove();
            },
            error: function (error) {
            let comentarioError = $('#comentarioError');
                                comentarioError.empty();
                                comentarioError.append(error.responseText );
                console.error('Error al editar el comentario', error);
            }
        });
    });
});
