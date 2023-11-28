$(window).on("load", function() {
    var audios = $(".audioElemento");
    var duracionElementos = $(".duracion-audio");
    audios.each(function(index, audio) {
        var duracion = audio.duration;
        var duracionElemento = duracionElementos.eq(index);
        duracionElemento.text(segundoAHoras(duracion));
    });

    function segundoAHoras(segundos) {
        var hora = Math.floor(segundos / 3600);
        hora = (hora < 10) ? '0' + hora : hora;

        var minuto = Math.floor((segundos / 60) % 60);
        minuto = (minuto < 10) ? '0' + minuto : minuto;

        var segundo = segundos % 60;
        segundo = (segundo < 10) ? '0' + segundo.toFixed(0) : segundo.toFixed(0);

        return hora + ':' + minuto + ':' + segundo;
    }
});
