$(document).ready(function () {

    $("#selectUsuarios").change(function (){

        var selectedValue = $(this).find('option:selected').data('idusuario');

        console.log(selectedValue);

        $.ajax({
            type: "POST",
            url: "/sunn/perfil/misCompartidos",
            data: { idUsuarioBuscado: selectedValue },
            success: function (response) {
                console.log("llegue");
            },
            error: function (error) {
                console.error(error);
            }
        });
    });
});
