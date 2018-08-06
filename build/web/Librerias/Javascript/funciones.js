// *************************
// * Funciones agregadas por Salvador Muñoz Aguilera
// * 17/Septiembre/2004
// * Funciones y variables utilizadas para el manejo de Capas
// *****************************
ns4 = (document.layers)? true:false
ie4 = (document.all)? true:false
/*function loadSource(id,nestref,url,bufferFrame) {
	if (ns4) {
		var lyr = (nestref)? eval('document.'+nestref+'.document.'+id) : document.layers[id]
		lyr.load(url,lyr.clip.width)
	}
	else if (ie4) {
	    bufferFrame.document.location = url
		//parent.bufferFrame.document.location = url
	}
}
function loadSourceFinish(id, bufferFrame) {
	//if (ie4) document.all[id].innerHTML = parent.bufferFrame.document.body.innerHTML
	if (ie4) document.all[id].innerHTML = bufferFrame.document.body.innerHTML
}
*/

 // Valida si es entero
 function isInteger (s){
   var i;
   if (isEmpty(s)) 
     if (isInteger.arguments.length == 1) 
       return defaultEmptyOK;
     else 
       return (isInteger.arguments[1] == true);
   for (i = 0; i < s.length; i++) {   
     var c = s.charAt(i);
     if (!isDigit(c)) 
       return false;
   }
   return true;
 } 
 
// Valida si es NULL
  function isEmpty(s) {
    return ((s == null) || (s.length == 0));
  } 
  

 // Valida si es Númerico
 function isDigit (c) {
   return ((c >= "0") && (c <= "9"));
 }  
 
function LlamaPagina(Pagina,W_Target){
        document.forms[0].action=Pagina;
        document.forms[0].target=W_Target;
        document.forms[0].submit();
    }
    
function reemplazaValor(string) {
      if (string=="") string="";
	add="";
	//match="|´[<>\"\';&\+?]";
        match="|´[<>';&\+?]";
	var temp = "" + string;

	for (i=0;i<match.length;i++){
		out=match.charAt(i)

		while (temp.indexOf(out)>-1) {
			pos=temp.indexOf(out) ;
			temp = "" + (temp.substring(0, pos) + add + temp.substring(pos + out.length, temp.length));
		}
	}

	string = temp;
	return string;
}


// Valida que no existan espacion dobles
function trim(string) {
	var newString = "";
	var withspc=true;
	for (var i = 0; i < string.length; i++) {
		if (string.charAt(i) != " " || withspc){
			 newString += string.charAt(i);
			 withspc=(string.charAt(i) != " ");
		}
	}
	return newString ;
}

 function validaTxa(element,totalcaracteres) {
   element.value = reemplazaValor(element.value);
   var longitud = element.value.length;
     
   element.value = trim(element.value);
   Valor2=element.value.substring(0,totalcaracteres);
   //element.value = trimSpac(Valor2,length);
 }

//union de ambas funciones ltrim y rtrim 
function trimSpac(str, chars) {
return ltrim(rtrim(str, chars), chars); 
}
//ltrim quita los espacios o caracteres indicados al inicio de la cadena 
function ltrim(str, chars) {
 chars = chars || "\\s";
  return str.replace(new RegExp("^[" + chars + "]+", "g"), ""); 
  
 } //rtrim quita los espacios o caracteres indicados al final de la cadena  
 
 function rtrim(str, chars) {     chars = chars || "\\s";     
 return str.replace(new RegExp("[" + chars + "]+$", "g"), ""); 
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
       
function verificaTXA(txtArea,valor) {
  //if(eval("formulario."+txtArea+".value.length")>=valor){ 
  if(txtArea.value.length >= valor){ 
    alert('Has superado el tamaño máximo permitido de '+valor+"\n"+"El sistema cortará la información excendente al guardar");
  return false; 
 }
}

function validaTxa2(txtArea,totalcaracteres) {
  var cadena = eliminaCarNoValidos(txtArea.value);
  //cadena = trim(cadena); 
  //alert("cadena.length: "+(cadena.length)+"\ncadena.length-totalcaracteres"+(cadena.length-totalcaracteres));
  if (cadena.length>totalcaracteres){
    //alert("El texto excede longitud permitida por "+(cadena.length-totalcaracteres)+" caracteres y será recortado");
    cadena=cadena.substring(0,totalcaracteres);
  }
  txtArea.value = cadena;
}

function eliminaCarNoValidos(cadena) {
var re =/\r\n/g;
if (cadena=="") cadena="";
cadena = cadena.replace(re,' ');
//alert("cadena: "+ cadena);
re =/'/g;
cadena = cadena.replace(re,'');
//alert("cadena: "+ cadena);
re=/"/g;
cadena = cadena.replace(re,'');
return cadena;
}
