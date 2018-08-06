// Función para la busqueda 


function filtrarCombo(url, id_contenedor, parametros){
  if (parametros.length >=4) {
        $.post(url, {'param': parametros}, function(data){
          $('#'+id_contenedor).html(data); 
         });
  }
}
     