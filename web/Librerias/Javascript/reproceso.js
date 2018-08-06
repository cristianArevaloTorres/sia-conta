
  function EnviarDatosPregunta(Obj, Formulario, opcion) {
	  if (opcion=='1'){
	    if(confirm("¿Esta seguro que desea reprocesar la años?")){
		    var x = prompt ('Introduzca el código para reprocesar','');
			  if (x=='1234')
  		    EnviarDatos(Obj,Formulario);
			  else
			    alert('Código incorrecto');
		  }
	  }else if (opcion=='0'){
			if(confirm("¿Esta seguro que desea procesar la años?")){
				var x = prompt ('Introduzca el código para procesar','');
				if (x=='1234')
					EnviarDatos(Obj,Formulario);
				else
					alert('Código incorrecto');
			}
	  }else if (opcion=='6'){
			if(confirm("¿Esta seguro que desea cambiar el estatus de la años a definitiva?")){
				var x = prompt ('Introduzca el código para cambiar estatus de años a definitiva','');
				if (x=='1234')
					EnviarDatos(Obj,Formulario);
				else
					alert('Código incorrecto');
			}
	  }else if (opcion=='120'){
			if(confirm("¿Esta seguro que desea procesar el complemento de años?")){
				var x = prompt ('Introduzca el código para procesar complemento de años','');
				if (x=='1234')
					EnviarDatos(Obj,Formulario);
				else
					alert('Código incorrecto');
			}
		}
		else {
			EnviarDatos(Obj,Formulario);
		}
  };
	
	function preguntarCodigo(msg) {
		var regresar= false;
	  if(confirm("&iquest;"+ msg+ "?")) {
      var x = prompt ('Introduzca el código ...','');
		  if (x=='1234')
  		  regresar= true;
			else
			  alert('El código es incorrecto, verifiquelo por favor. !');
	  }; // if
		return regresar;
	}; // preguntarCodigo
