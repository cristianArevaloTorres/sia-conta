<!--

  function marcar(activa)

  {

  if (activa.checked)

      for (i=0; i<document.forma.INDICE.length; i++) document.forma.INDICE[i].checked=true;

  else

     for (i=0; i<document.forma.INDICE.length; i++) document.forma.INDICE[i].checked=false;

  }



  function confirma(Opcion) {

    var name;

    if (Opcion==1)

       name = confirm("Desea darlo de baja?");

    else

       name = confirm("Modificaciones correctas?");

    if (name == true)

      return true;

    else

      return false;

   }



  /***************************************************

  La función CheckBox() Verifica si se ha seleccionado

  un elemento radio o checkbox y si es así lanza el

  alert preguntando si es la opción correcta.

  ***************************************************/





  function CheckBox()

  {

    var boleano = false;

    for(i=0; i<(document.forma.elements.length); i++)

    {

      nombre = document.forma.elements[i].name;

      tipo = document.forma.elements[i].type;

      if (tipo == "checkbox" || tipo == "radio")

      {

        boleano = boleano || document.forma.elements[i].checked;

      }

    }

    if(boleano)

    {

      var name = confirm("¿Es el equipo correcto?");

      if (name)

      {

        return true;

      }

	  else

	  {

	   return false;

	  }

    }

    else

    {

      alert("Al menos debes seleccionar un equipo");

      return false;

    }

  }



  /***************************************************

  La función window_open(campo) abre una ventana nueva

  en la que aparece un calendario para seleccionar una

  fecha. Recibe como parámetro el nombre del campo

  donde se va a colocar la fecha seleccionada y si el

  campo al momento de mandar llamar el calendario lleva

  una fecha coloca al calendario en esa fecha, si no

  pone la fecha actual.

  ***************************************************/





  function window_open(campo)

  {

    var newWindow;

    var urlstring = "/Librerias/Javascript/Calendar/Calendar.htm?campo="+campo;

    newWindow = window.open(urlstring,'','top=200,left=200,dependent=yes,alwaysRaised=yes,height=220,width=280,toolbar=no,minimize=no,status=no,memubar=no,location=no,scrollbars=no');

  }



  function verificaPeriodo(fIni, fFin) {

	   var ok= false;

	   var fIni_= fIni.substr(6)+ fIni.substring(3,5)+ fIni.substring(0,2);

	   var fFin_= fFin.substr(6)+ fFin.substring(3,5)+ fFin.substring(0,2);

		 if(fFin_>= fIni_)

		   ok= true;

		 else

		   alert("La fecha de inicio("+ fIni+ ") del periodo tiene que se menor que la fecha de termino("+ fFin+") del periodo.");

		 return ok;

	};





  function verificaEjercicio(ejer, fIni, fFin) {

//     alert(ejer+" "+fIni+" "+fFin);

	   var ok= false;

	   var fIni_= fIni.substr(6);

	   var fFin_= fFin.substr(6);

		 if(ejer== fFin_ && ejer== fIni_)

		   ok= verificaPeriodo(fIni, fFin);

		 else

		   alert("El año del ejercicio("+ ejer+ ") no coincide con los años de los periodos.");



		 return ok;

	};



 function colocarCero(objeto) {

	   var ok= true;

       objeto.value= "0";

  	   return ok;

     }; // blancos



 function validaAlfabeticos(objeto)

  {

    var ok= true;

    if(objeto.value.length== 0)

    {

      alert("Debe proporcionar la descripción");

      ok= false;

    }

    return ok;

  }; // validaAlfabeticos



  function validaFloat(Objeto)

  {

    if (Objeto.value.indexOf(".") != -1)

    {

      posicion = Objeto.value.indexOf(".");

      LongitudValor = Objeto.length;

      nuevacadena = Objeto.value.substr(posicion + 1, LongitudValor - posicion);

      if (nuevacadena.indexOf(".") != -1)

      {

        alert ("Error en el valor punto flotante que se escribió " + Objeto.value);

        return false;

        // Objeto.focus();

      }

    }

    else

      return true;



    flotantes = /^([0-9]|.)+$/;

    if ((!flotantes.test(Objeto.value)) && (LongitudValor > 0) )

    {

      alert("Formato Inválido.");

        return false;

    }

    else

    {

      return true;

    }

  }



  function validaNumeros(W_campo){

    var ok= true;

    Numeros = /^([0-9])+$/;

    var W_valor=W_campo.value;

      if (!Numeros.test(W_valor)){

        alert("Digite sólo números");

        return false;

      }

      return ok;

   };



  function validaNumericos(objeto) {

        var ok = true;

        if(objeto.value.length== 0) {

          ok= colocarCero(objeto);

        }

        else {

          ok = validaNumeros(objeto);

        }

        return ok;

     }; // validaNumericos



  function validaFlotantes(objeto) {

        var ok = true;

        if(objeto.value.length== 0) {

          ok= colocarCero(objeto);

        }

        else {

          ok = validaFloat(objeto);

        }

        return ok;

     }; // validaNumericos

  function validacion(objeto, enviarFoco)

     {

     // var tipoValidacion= objeto.name.substring(objeto.length-1);

       var tipoValidacion = objeto.name.substring(objeto.name.indexOf("_")+ 1);

       var ok= false;

       switch(tipoValidacion) {

         case "f": // alfabetico

            ok= validaFlotantes(objeto);

          break;

         case "n": // numeros

            ok= validaNumericos(objeto);

          break;

         case "d":  // alfabetico y numeros

            ok= validaAlfabeticos(objeto);

          break;

       };

       if(enviarFoco && !ok)

         objeto.focus();

       return ok;

  }; // validacion



  function cicloValidacion(formulario) {

       var ok= true;

       for(var x= 0; x< formulario.elements.length && ok; x++) {

         var objeto= formulario.elements[x];

         if(objeto.type== "text" && objeto.name.substring(objeto.name.length-2, objeto.name.length-1)== "_") {

           ok = validacion(objeto, true);

         }; // if

       }; // for

       return ok;

  }; // cicloValidacion

  

  function Mensaje(opcion,strCampo)

  {

    switch (opcion)

    {

	    case 2 :

          alert("El campo "+strCampo+" se encuentra vacío");

          break;

	    case 3 :

          alert(strCampo);

          break;

    }

  }

  

    function noExisteElemento(fuente, persona) {

		var x= 0;

		while (x< fuente.options.length && fuente.options[x].value!= persona)

		  x++;

		return x>= fuente.options.length ;

	}; // noExisteElemento

	

  function agregar(fuente, destino, actual,numdes) {

  if (parseInt(numdes) > destino.options.length)  {

		  if(noExisteElemento(destino, fuente.options[fuente.selectedIndex].value) )

		     if(noExisteElemento(actual, fuente.options[fuente.selectedIndex].value) ) {

  	    var nombre= fuente.options[fuente.selectedIndex].value+ ">"+ fuente.options[fuente.selectedIndex].text;

	  	  destino.options[destino.options.length]= new Option(fuente.options[fuente.selectedIndex].text, fuente.options[fuente.selectedIndex].value);

		    //destino.options[i].selected = true;

		  	destino.size= destino.options.length;

   		}  		 

		  else {

		     alert("La persona ya pertenece a la lista. !"); 

			}

	}

		else {

		   alert(" Ya no puedes seguir agregando mas personas. Cupo lleno"); 

		}  		 

	}; // agregar

	

  function eliminar(fuente) {

		fuente.options[fuente.selectedIndex]= null;

		if(fuente.options.length>= 0) {

    		fuente.size= fuente.options.length;

			fuente.selectedIndex= 0;

		};

	};	

  

	

  ns4 = (document.layers)? true:false

  ie4 = (document.all)? true:false

  function loadSource(id,nestref,url,parametros) {

  url= url+ parametros;

	if (ns4) {

		var lyr = (nestref)? eval('document.'+nestref+'.document.'+id) : document.layers[id]

		lyr.load(url,lyr.clip.width)

	}

	else 

	  if (ie4) {

		  bufferCurp.document.location = url

  	}; // if

  }

  

   function loadSourceFinish(id) {

	if (ie4) document.all[id].innerHTML = bufferCurp.document.body.innerHTML

    }

    

   function validaSeleccion(Formulario)

   {

      var ok = false;

	  var c=0, j=0;

      for(var x=0; x< Formulario.elements.length; x++)

	  {

	    Objeto= Formulario.elements[x];

		if(Objeto.type=="select-one" && Objeto.value.indexOf("dent")>=0){

		    c++;

		  if(Objeto.options.length > 0)

		     j++;

		}//if type  

	  }//for

	  if (c==j)

	    ok=true;

	  else

	    alert("Información incompleta");	

	  return ok

   }



	function EnviarDatos(Formulario,pagina) {

 		Formulario.action=pagina;

   	Formulario.submit();	    

	};



-->