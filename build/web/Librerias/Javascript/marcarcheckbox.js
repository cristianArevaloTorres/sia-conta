  function Marcar(Formulario, Cuales, Tabla) {
    var Objeto;
    for(var x=0; x< Formulario.elements.length; x++){
	  Objeto= Formulario.elements[x];
	  if(Objeto.type=="checkbox" && c.indexOf("cCap"+ Tabla)>=0) {
        window.status= "Objeto: "+ Objeto.value;
        Objeto.checked= Objeto.value.indexOf(Cuales)>=0;
	  }
	}
  }
  
  function PorloMenosUno(Formulario, Tabla) {
	var Encontro= false;
	var Objeto;
    var x=0;
    while (x< Formulario.elements.length && !Encontro){
	  Objeto= Formulario.elements[x];
	  if(Objeto.type=="checkbox" && Objeto.value.indexOf("cCap"+ Tabla)>=0) {
        Encontro= Objeto.checked;
	  }
      x++;
	}  
	return Encontro;
  }


	function marcarTodos(Formulario,nombre,marcar) {
		var Objeto;
		var x=0;
		while (x< Formulario.elements.length){
			Objeto= Formulario.elements[x];
			if(Objeto.type=="checkbox" && Objeto.name==nombre) {
				Objeto.checked=marcar;
			}
			x++;
		}  
	}


	function marcarTodos2(Formulario,nombre,valor,marcar) {
		var Objeto;
		var x=0;
		while (x< Formulario.elements.length){
			Objeto= Formulario.elements[x];
			if(Objeto.type=="checkbox" && Objeto.name==nombre && Objeto.value.indexOf(valor)==0) {
				Objeto.checked=marcar;
			}
			x++;
		}  
	}

  //comprobar contra valor 
	function marcarTodos3(Formulario,valor,marcar) {
		var Objeto;
		var x=0;
		while (x< Formulario.elements.length){
			Objeto= Formulario.elements[x];
			if(Objeto.type=="checkbox" && Objeto.value==valor) {
				Objeto.checked=marcar;
			}
			x++;
		}  
	}

  //comprobar contra nombre aproximado
	function habilitaText(Formulario,nombre,marcar) {
		var Objeto;
		var x=0;
		while (x< Formulario.elements.length){
			Objeto= Formulario.elements[x];
			if(Objeto.type=="text" && Objeto.name.indexOf(nombre)==0) {
				Objeto.disabled=!marcar;
			}
			x++;
		}  
	}



  function marcadoUno(Formulario, nombre) {
		var Encontro= false;
		var Objeto;
		var x=0;
		while (x< Formulario.elements.length && !Encontro){
			Objeto= Formulario.elements[x];
			if(Objeto.type=="checkbox" && Objeto.name==nombre) {
				Encontro = Objeto.checked;
			}
			x++;
		}  
		return Encontro;
  }


	function marcarInverso(Formulario,nombre) {
		var Objeto;
		var x=0;
		while (x< Formulario.elements.length){
			Objeto= Formulario.elements[x];
			if(Objeto.type=="checkbox" && Objeto.name==nombre) {
				Objeto.checked= !Objeto.checked;
			}
			x++;
		}  
	}
	
	  function marcarTodosCP(Formulario,nombre,marcar) {
			if (Formulario.Todas_clave_prog_faltantes.checked==true) {
				//marcarTodosCPFaltantes(Formulario,nombre,false);						
				Formulario.Todas_clave_prog.checked=true;								
				Formulario.Todas_clave_prog_faltantes.checked=false;				
			};
			var Objeto;
			var x=0;
			while (x< Formulario.elements.length){
				Objeto= Formulario.elements[x];
				if(Objeto.type=="checkbox" && Objeto.name==nombre) {
					Objeto.checked=marcar;
				}
				x++;
			}  
		}

	  function marcarTodosCPFaltantes(Formulario,nombre,marcar) {
			if (Formulario.Todas_clave_prog.checked==true) {
				marcarTodosCP(Formulario,nombre,false);						
				Formulario.Todas_clave_prog_faltantes.checked=true;				
				Formulario.Todas_clave_prog.checked=false;				
			};
			var Objeto;
			var x=0;
			var sub = "";
			while (x< Formulario.elements.length){
				Objeto= Formulario.elements[x];
				if(Objeto.type=="checkbox" && Objeto.name==nombre) {
					sub = Objeto.value.substring(Objeto.value.length-1,Objeto.value.length+1);
					if (sub==1) {
						Objeto.checked=marcar;
					}
				}
				x++;
			}  
		}
	
	  function marcarTodosCPEliminar(Formulario,nombre,marcar) {
			var Objeto;
			var x=0;
			while (x< Formulario.elements.length){
				Objeto= Formulario.elements[x];
				if(Objeto.type=="checkbox" && Objeto.name==nombre) {
					Objeto.checked=marcar;
				}
				x++;
			}  
		}

	  function marcarSeleccionRango(Formulario,nombre,marcar,tipo,desde,hasta) {  
			var Objeto;
			var x=0;
			var valor  = "";
			var cxl = "";
			var cons_cveprog = "";
			var clave_programatica = "";
			var unidad_ejecutora = "";
			var entidad = "";
			var ambito = "";
			var tipoX = "";
			var valor_tmp = "";
			
			if (desde=="" && hasta == "") {
				alert("Para seleccionar un rango de claves programáticas debe incluir un texto en Desde y/o Hasta.");
				Formulario.desde.focus();
			}
			else {			
				while (x< Formulario.elements.length){
					Objeto= Formulario.elements[x];
					if(Objeto.type=="checkbox" && Objeto.name==nombre) {
						valor = Objeto.value.substring(1,Objeto.value.length+1);
//						cxl = valor.substring(0,valor.indexOf("|"));
//						cons_cveprog = valor.substring(valor.indexOf("|")+1,valor.indexOf("~"));
						clave_programatica = valor.substring(valor.indexOf("~")+1,valor.indexOf("^"));										
						unidad_ejecutora = valor.substring(valor.indexOf("!")+1,valor.indexOf("¡"));
						entidad = valor.substring(valor.indexOf("¡")+1,valor.indexOf(":"));										
						ambito  = valor.substring(valor.indexOf(":")+1,valor.indexOf("¦"));
						tipoX    = valor.substring(valor.length-1,valor.length+1);
//					alert("cxl: "+cxl + "  cons_cveprog: "+cons_cveprog+"   clave_programatica: "+clave_programatica+ "  ue: "+unidad_ejecutora+"  entidad: "+entidad + "   ambito: "+ambito+ "  tipo: "+tipoX);
						valor_tmp = clave_programatica+"-"+unidad_ejecutora+"-"+entidad+"-"+ambito;
						if (tipoX==tipo) {

							if (desde!="" && hasta!="") {
					  		if (valor_tmp >= desde && valor_tmp <= hasta) {
						 			Objeto.checked = marcar;
								}							
							}	//if desde!="" && hasta!=""

							if (desde!="" && hasta=="") {
					  		if (valor_tmp >= desde) {
						 			Objeto.checked = marcar;						
								}	
							}	// desde!="" && hasta==""

							if (desde=="" && hasta!="") {
					  		if (valor_tmp <= hasta) {
						 			Objeto.checked = marcar;						
								}	
							}	// desde!="" && hasta==""

				  	} //if tipoX==1
					} // if Objketo.type
					x++;
				} //while 
			} //if desde=="" && hasta == ""	
		}


	  function marcarSeleccionComienza(Formulario,nombre,marcar,tipo,desde) {  
			var Objeto;
			var x=0;
			var valor  = "";
			var clave_programatica = "";
			var unidad_ejecutora = "";
			var entidad = "";
			var ambito = "";
			var tipoX = "";
			var valor_tmp = "";
			
			if (desde=="") {
				alert("Debe incluir un texto en Desde.");
				Formulario.desde.focus();
			}
			else {			
				while (x< Formulario.elements.length){
					Objeto= Formulario.elements[x];
					if(Objeto.type=="checkbox" && Objeto.name==nombre) {
						valor = Objeto.value.substring(1,Objeto.value.length+1);
						clave_programatica = valor.substring(valor.indexOf("~")+1,valor.indexOf("^"));										
						unidad_ejecutora = valor.substring(valor.indexOf("!")+1,valor.indexOf("¡"));
						entidad = valor.substring(valor.indexOf("¡")+1,valor.indexOf(":"));										
						ambito  = valor.substring(valor.indexOf(":")+1,valor.indexOf("¦"));
						tipoX    = valor.substring(valor.length-1,valor.length+1);
						valor_tmp = clave_programatica+"-"+unidad_ejecutora+"-"+entidad+"-"+ambito;
						if (tipoX==tipo) {							
					  	if (valor_tmp.indexOf(desde)==0) {
						 		Objeto.checked = marcar;
							}							
				  	} //if tipoX==1
					} // if Objketo.type
					x++;
				} //while 
			} //if desde=="" && hasta == ""	
		}



