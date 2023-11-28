const compartir = document.getElementsByName("compartir");
const contenedorCompartir=document.getElementById("form-compartir")
const cantidadNotificaciones=document.getElementById("cantidadNotificaciones")
compartir.forEach(c => {
    c.addEventListener("click",()=>{
        var url = "/sunn/obtenerFormCompartir";
        $.post(url).done(function (data) {
            contenedorCompartir.innerHTML=data
            cerrarPopUp();
            var botonCompartirNoticia=document.getElementById("idNoticiaCompartida");

            botonCompartirNoticia.addEventListener("click",()=>{
                var idUsuario=obtenerUsuarioSeleccionado()
                console.log(c.value)
                var data={ idNoticiaCompartida : c.value, receptor : idUsuario}
                var url = "/sunn/compartir";
                $.post(url,data).done(function (data) {
                    cantidadNotificaciones.textContent=parseInt(cantidadNotificaciones.textContent)+1
                    contenedorCompartir.innerHTML=""
                });
            })

        });
    })
})

function cerrarPopUp(){
    const botonCerrar=document.getElementById("cerrarPopUp")
    botonCerrar.addEventListener("click",()=>{
        contenedorCompartir.innerHTML="";
    })
}

function obtenerUsuarioSeleccionado(){
    var usuarios = document.getElementsByName("receptor");
    var usuarioSeleccionado = null;

    usuarios.forEach(u => {
        if (u.checked) {
            usuarioSeleccionado = u.value;
        }
    });

    return usuarioSeleccionado;
}