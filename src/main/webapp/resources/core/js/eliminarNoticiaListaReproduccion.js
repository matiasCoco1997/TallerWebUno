
const botonesEliminarNoticiaLista=document.getElementsByName("eliminarNoticiaLista")

botonesEliminarNoticiaLista.forEach(boton =>{
    boton.addEventListener("click",()=>{
        var data={ noticiaEliminar : boton.value}

        var noticiaElemento = $("#titulo" + data.noticiaEliminar).text();

        if(noticiaElemento == $("#titulo-noticia-headerPlay").text()){
            var idNoticiaActual = $('.bg-gray-900:first').find("audio.audioElemento").attr("id");
                      var audios = $("audio.audioElemento");

                      var siguienteElemento = null;

                      audios.each(function(index) {
                          if ($(this).attr("id") === data.noticiaEliminar) {
                              siguienteElemento = audios.eq(index + 1);
                              if (siguienteElemento.length === 0) {
                                  siguienteElemento = audios.first();
                              }
                              return false;
                          }
                      });

                      if (siguienteElemento !== null) {
                          var idSiguiente = siguienteElemento.attr("id");
                          console.log("if "+ idSiguiente);
                          cambiarAudio(idSiguiente);
                          $(".bg-gray-900:first").removeClass("bg-gray-900");
                      }
        }

        var url = "/sunn/listaReproduccion/eliminarNoticia";
        $.post(url,data).done(function (data) {
            console.log(data)
            document.getElementById("audio"+ boton.value).remove();
            boton.parentNode.parentNode.classList.remove("p-10","mt-20");
            boton.parentNode.parentNode.innerHTML=""
        });
    })
})

function cambiarAudio(id){
         $.ajax({
             type: "GET",
             url: "/sunn/listaReproduccion/" + id,
             success: function (noticia) {
                var headerPlay = $("#headerPlay");
                var tituloNoticia = $("#titulo-noticia-headerPlay");
                var resumenNoticia = $("#resumen-noticia-headerPlay");
                var imgUsuarioNoticia = $("#img-usuario-noticia");
                var nombreUsuarioNoticia = $("#nombre-usuario-noticia");
                var fechaPublicacionNoticia = $("#fecha-publicacion-noticia");
                var audioNoticia = $("#audio-noticia");

                headerPlay.css("background-image", 'url(/sunn/' + noticia.rutaDeimagen + ')');
                tituloNoticia.text(noticia.titulo);
                resumenNoticia.text(noticia.resumen);
                imgUsuarioNoticia.attr("src", "/sunn/" + noticia.usuario.fotoPerfil);
                nombreUsuarioNoticia.text(noticia.usuario.nombre);
                fechaPublicacionNoticia.text(noticia.fechaDePublicacion);

                audioNoticia.attr("src", "/sunn/" + noticia.rutaDeAudioPodcast);
                cambiarAudioNuevo(audioNoticia.attr("src"));

                $("#audio"+noticia.idNoticia).addClass("bg-gray-900");
             }

         });
     }
 function cambiarAudioNuevo(nuevoAudioSrc) {

        if(wavesurfer.isPlaying()){
            var clickedButton = $(".btnReproducirPausa");

                 clickedButton.each(function() {
                       if ($(this).is(":hidden")) {
                           $(this).show();
                       } else {
                           $(this).hide();
                       }
                 });
        }
        wavesurfer.stop(); // Detiene la reproducción del audio actual
        wavesurfer.load(nuevoAudioSrc); // Carga el nuevo archivo de audio


    }