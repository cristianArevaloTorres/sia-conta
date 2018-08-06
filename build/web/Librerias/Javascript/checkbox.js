  function getSeleccionados() {
	return getSeleccionados(formulario,0);
  }

  function getSeleccionados(numFormulario) {
	form = document.forms[numFormulario];
	valores = "";
	for(var x=0; x<form.elements.length; x++) {
	  objeto = form.elements[x];
	  if(objeto.type=="checkbox") {
		valores = valores+objeto.value+',';
	  }
	  valores=valores.substring(0,valores.length-1);
	}
	return valores;
  }