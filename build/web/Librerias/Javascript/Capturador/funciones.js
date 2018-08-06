var timeoutId = false;

function window_open(campo) {
    var newWindow;
    var urlstring = '../../librerias/funciones/calendar.html?campo='+campo;
    newWindow = window.open(urlstring,'','height=220,width=280,toolbar=no,minimize=no,status=no,memubar=no,location=no,scrollbars=no')
 }

function LlamaPagina(Pagina,W_Target){
    document.forms[0].action=Pagina;
    document.forms[0].target=W_Target;
    document.forms[0].submit();
  }

  function verificaChecks(formulario, grupoChecks, msg) {
   var objeto  = null;
   var total   = 0;
   var contador= 0;
   for(var x=0; x< formulario.elements.length; x++) {
           objeto= formulario.elements[x];
           if(objeto.type=="checkbox" && objeto.name.indexOf(grupoChecks)>=0) {
                         if(objeto.checked)
                     contador++;
                               total++;
                       }; // if
             //          window.status= "Verificando el grupo ["+ grupoChecks+ "]";
   }; // for
               if(contador> 0){
           //            window.status= "Grupo ["+ grupoChecks+ "] Ok.";
                       formulario.submit();
               }
               else
                 alert("! "+ msg+ ". !");
               //  0 = Error no se selecciono ninguno
               //  1 = Se seleciono por lo menos uno
               //  2 = Se selecionaron todos los checks
   //	alert("Grupo ["+ grupoChecks+ "] total:"+ total+ " marcados: "+ contador);
               var regresar= false;
               if(contador== total)
                 regresar= true;
         else
                 if(contador> 0)
                   regresar= true;
//		alert(grupoChecks+ " regresar: "+ regresar);
               return regresar;
       }; // verificaChecks

  function verificaChecksSinSubmit(formulario, grupoChecks, msg) {
   var objeto  = null;
   var total   = 0;
   var contador= 0;
   for(var x=0; x< formulario.elements.length; x++) {
           objeto= formulario.elements[x];
           if(objeto.type=="checkbox" && objeto.name.indexOf(grupoChecks)>=0) {
                         if(objeto.checked)
                     contador++;
                               total++;
                       }; // if
             //          window.status= "Verificando el grupo ["+ grupoChecks+ "]";
   }; // for
               if(contador> 0){
           //            window.status= "Grupo ["+ grupoChecks+ "] Ok.";
                   //    formulario.submit();
               }
               else
                 alert("! "+ msg+ ". !");
               //  0 = Error no se selecciono ninguno
               //  1 = Se seleciono por lo menos uno
               //  2 = Se selecionaron todos los checks
   //	alert("Grupo ["+ grupoChecks+ "] total:"+ total+ " marcados: "+ contador);
               var regresar= false;
               if(contador== total)
                 regresar= true;
         else
                 if(contador> 0)
                   regresar= true;
//		alert(grupoChecks+ " regresar: "+ regresar);
               return regresar;
       }

       function verificaChecksTodos(formulario, grupoChecks, msg) {
        var objeto  = null;
        var total   = 0;
        var contador= 0;
        for(var x=0; x< formulario.elements.length; x++) {
                objeto= formulario.elements[x];
                 if(objeto.type=="checkbox" && objeto.name.indexOf(grupoChecks)>=0) {
                              if(objeto.checked){
                                contador++;
                                total++;
                              }
                              else
                                total--;

                            }; // if
                  //          window.status= "Verificando el grupo ["+ grupoChecks+ "]";
        }; // for
                    var regresar= false;
                    if(contador== total)
                      regresar= true;
                    else
                      alert("! "+ msg+ ". !");
                    return regresar;
            }

  function seleccionarOpcion(formulario,opciones) {
     var objeto= null;
     for(var x=0; x< formulario.elements.length; x++) {
             objeto= formulario.elements[x];
             if(objeto.type=="checkbox" && objeto.name.indexOf(opciones.value.substr(1))>=0) {
                     objeto.checked= opciones.value.substring(0, 1)== "0";
                         }; // if
                         window.status= "Grupo ["+ opciones.value.substr(1)+ "]";
     }; // for
        }; // seleccionarOpcion

  function enviarDatos(formulario, opciones, msg) {
      if(verificaChecks(formulario, opciones, msg)= 0)
        formulario.focus();
        };


  function window_openGen(urlstring,name,parametros,numVentana) {
     window.open(urlstring,name+"yo",parametros);
 }

  function window_open2(rutaCampo) {
    var newWindow;
    var urlstring = rutaCampo;
    newWindow = window.open(urlstring,'','height=220,width=280,toolbar=no,minimize=no,status=no,memubar=no,location=no,scrollbars=no')
 }
// *************************
// * Funciones agregadas por Salvador Muñoz Aguilera
// * 17/Septiembre/2004
// * Funciones y variables utilizadas para el manejo de Capas
// *****************************
function getObjeto( elemID )
{
    var obj
    if (document.all) {
        //alert( elemID + ' - ' + document.all(elemID) );
        obj = document.all(elemID)
    } else if (document.layers) {
        //alert( elemID + ' - ' + document.layers[elemID] );
        obj = document.layers[elemID]
    } else if (document.getElementById) {
        //alert( elemID + ' - ' + document.getElementById(elemID) );
        obj = document.getElementById(elemID)
    }
    
    return obj;
}

ns4 = (document.layers)? true:false
ie4 = (document.all)? true:false
nn6 = (document.getElementById && !document.all);
ie5 = (document.all && document.getElementById);
function loadSource(id,nestref,url,bufferFrame) {
	if (ns4) {
		var lyr = (nestref)? eval('document.'+nestref+'.document.'+id) : document.layers[id]
		lyr.load(url,lyr.clip.width)
	}
	else if (ie4) {
	    bufferFrame.document.location = url
		//parent.bufferFrame.document.location = url
	}
}
function loadSourceFinish(id) {
	if (ie4) {
          document.all[id].innerHTML = bufferFrame.document.body.innerHTML;
        }
        else  {
          if(ie5||nn6) {
            getObjeto(id).innerHTML = document.getElementsByTagName( "iframe" )[ 0 ].contentDocument.documentElement.innerHTML;
          }
       }  //if ie4 
}//loadSourceFinish

function loadSourceFinish(id, bufferFrame) {
	//if (ie4) document.all[id].innerHTML = parent.bufferFrame.document.body.innerHTML
	if (ie4) document.all[id].innerHTML = bufferFrame.document.body.innerHTML
}
// ******************
// * Funcion para mostrar u ocultar un bloque de codigo HTML 
// ******************
function MuestraLyr(TheTR,img)
{
var DataTR = eval('document.all.' + TheTR);
    if (DataTR.style.display=="block" || DataTR.style.display=="" )
    {
        DataTR.style.display="none";
        img.children[1].children[0].src='../../librerias/imagenes/expandir.gif';
    }
    else
    {
        DataTR.style.display="block";
        img.children[1].children[0].src='../../librerias/imagenes/contraer.gif';
    }
}

