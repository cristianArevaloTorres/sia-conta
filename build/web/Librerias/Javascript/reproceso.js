
  function EnviarDatosPregunta(Obj, Formulario, opcion) {
	  if (opcion=='1'){
	    if(confirm("�Esta seguro que desea reprocesar la a�os?")){
		    var x = prompt ('Introduzca el c�digo para reprocesar','');
			  if (x=='1234')
  		    EnviarDatos(Obj,Formulario);
			  else
			    alert('C�digo incorrecto');
		  }
	  }else if (opcion=='0'){
			if(confirm("�Esta seguro que desea procesar la a�os?")){
				var x = prompt ('Introduzca el c�digo para procesar','');
				if (x=='1234')
					EnviarDatos(Obj,Formulario);
				else
					alert('C�digo incorrecto');
			}
	  }else if (opcion=='6'){
			if(confirm("�Esta seguro que desea cambiar el estatus de la a�os a definitiva?")){
				var x = prompt ('Introduzca el c�digo para cambiar estatus de a�os a definitiva','');
				if (x=='1234')
					EnviarDatos(Obj,Formulario);
				else
					alert('C�digo incorrecto');
			}
	  }else if (opcion=='120'){
			if(confirm("�Esta seguro que desea procesar el complemento de a�os?")){
				var x = prompt ('Introduzca el c�digo para procesar complemento de a�os','');
				if (x=='1234')
					EnviarDatos(Obj,Formulario);
				else
					alert('C�digo incorrecto');
			}
		}
		else {
			EnviarDatos(Obj,Formulario);
		}
  };
	
	function preguntarCodigo(msg) {
		var regresar= false;
	  if(confirm("&iquest;"+ msg+ "?")) {
      var x = prompt ('Introduzca el c�digo ...','');
		  if (x=='1234')
  		  regresar= true;
			else
			  alert('El c�digo es incorrecto, verifiquelo por favor. !');
	  }; // if
		return regresar;
	}; // preguntarCodigo
