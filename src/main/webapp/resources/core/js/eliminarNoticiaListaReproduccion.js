const botonesEliminarNoticiaLista=document.getElementsByName("eliminarNoticiaLista")

botonesEliminarNoticiaLista.forEach(boton =>{
    boton.addEventListener("click",()=>{
        var data={ noticiaEliminar : boton.value}
        var url = "/sunn/listaReproduccion/eliminarNoticia";
        $.post(url,data).done(function (data) {
            console.log(data)
            boton.parentNode.parentNode.classList.remove("p-10","mt-20");
            boton.parentNode.parentNode.innerHTML=""

        });
    })
})