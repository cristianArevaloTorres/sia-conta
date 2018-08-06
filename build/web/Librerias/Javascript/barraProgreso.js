   function actualizarProgreso(porcentaje) {
	   var temporal= porcentaje;
     if(porcentaje== null) 
		   porcentaje= 100;
		 else
		   if(porcentaje<= 0)
			   porcentaje= 1;
 			 else
		    if(porcentaje> 100)
			     porcentaje= 100;
	   llevaProcesado.width= porcentaje+ '%';
		 faltaProcesar.width = (101- porcentaje)+ '%';
     var formulario= null;
     if(document.forms['forma']!= null)
       formulario= document.forms['forma'];
     else
       formulario= document.forms['catalogo'];
		 if(temporal== 0)
       formulario.proceso.value= '0%';
		 else
       formulario.proceso.value= llevaProcesado.width;
	 }; // actualizarProgreso
