 $(document).ready(function () {
    var audios = $(".audio");
    var playlistSize = audios.length;
    var audioAnterior = null;

        $('.playButton').on('click', function () {
            const audioActual = $('.playButton').index(this);

            playTrack(audioActual);
            audioAnterior = audioActual;
        });



    function playTrack(audioActual) {

         if(audioActual >= 0 && audioActual < playlistSize) {
            if(audioActual == audioAnterior){
                          const audio = audios.eq(audioActual)[0];
                                      audio.pause();
                }else{
                    const audio = audios.eq(audioActual)[0];
                            audio.play();


                            audio.addEventListener("ended", function() {
                                audio.pause();


                                audioActual++;;
                                if (audioActual < playlistSize) {
                                    playTrack(audioActual);
                                }
                            });
                }

        }else{
            audioAnterior = -1;
        }
    }


 });