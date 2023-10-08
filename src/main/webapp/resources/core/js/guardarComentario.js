 $(document).ready(function() {
        $('#formularioComentario').submit(function(e) {
            $('#comentarioError').empty();
            e.preventDefault();
            var form = $(this);
            var actionUrl = form.attr('action')

            $.ajax({
                type: 'POST',
                url: actionUrl,
                data:  form.serialize(),
                dataType: 'html',
                success: function(response) {

                    $('#descripcion').val(''); // Esto va limpia rel campo del comtentario
                    mostrarComentario(response);

                },
                error: function(error) {
                    alert('Error al guardar el comentario.');
                }
            });
        });
         function mostrarComentario(comentario) {

                if($(comentario).filter("#error").length > 0){
                    let comentarioError = $('#comentarioError');
                    comentarioError.empty();
                    comentarioError.append(comentario);
                }else{
                    let comentariosContainer = $('#comentariosContainer');

                    comentariosContainer.append(comentario);
                }


        }
   }
  );