$(document).ready(function () {

    $("#selectUsuarios").change(function (){

        var selectedValue = $(this).find('option:selected').data('idusuario');

        window.location.href = "/sunn/perfil/misCompartidos/?idUsuarioFiltrado=" + selectedValue;

    });
});
