const botonesAgregarNoticiaLista=document.getElementsByName("noticiaAgregar")

botonesAgregarNoticiaLista.forEach(boton =>{
    boton.addEventListener("click",()=>{
        var data={ noticiaAgregar : boton.value }
        var url = "/sunn/listaReproduccion/agregarNoticia";
        $.post(url,data).done(function (data) {

        });
    })
})