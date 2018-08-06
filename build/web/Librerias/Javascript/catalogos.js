
  var CONSTANTE     = 0;
  var COLUMNA       = 1;
  var FUNCION       = 2;
  var NONULO        = 3;
  var NOMBRE        = 4;
  var LONGITUD      = 5;
  var defaultEmptyOK= false;
  var decimalPointDelimiter= ".";
  var promptStr     = "Error: ";
  var whitespace    = " \t\n\r";
  var PALABRA       = "ajg_";
  var daysInMonth   = new Array(31,28,31,30,31,30,31,31,30,31,30,31);
  var namesOfMonth  = new Array("Enero", "Febrero", "Marzo", "Abri", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre");

  function ajustarVentana(){
    var width     = screen.width;
    var height    = screen.height;
      parent.resizeTo(width/2,height/2);
      parent.moveTo(width/4,height/4);
  }
  
  function ajustarVentanaJsp(){
    var width     = screen.width;
    var height    = screen.height;
      window.resizeTo(width/2,height/2);
      window.moveTo(width/4,height/4);
  }
  
  
  function validaCURP(Objeto) {
    var CURP = Objeto.value;
    var expresion_curp =/^([A-Za-z]{4})([\d]{6})([Hh|Mm])([A-Za-z]){5}([A-Za-z]|[0-9])([0-9])$/
    Valor = expresion_curp.test(CURP);
    if (!Valor) {
      contador++;
      alert("Formato de CURP Inválido: " + CURP);
      Objeto.focus();
    }; // if
  }; // validaCURP

  function validaOracionAlfa(Objeto, LongitudValor) {
    Valor = Objeto.value;
    Letras = /^([A-Z]|[a-z]|á|é|í|ó|ú|ñ|Á|É|Í|Ó|Ú|Ñ|espacio)+$/;
    if ((!Letras.test(Valor))) {
      alert("Digite sólo letras en " + Objeto.name);
      Objeto.focus();
    }
  }; // validaOracionAlfa

  function obtenerNombreMes(mes) {
	  return namesOfMonth[mes- 1];
	}; // obtenerNombreMes

  function Justificar( Valor, Caracter, Longitud, Lugar ) {
    if ( Caracter!= "#" )
      for(var x= Valor.length; x< Longitud; x++ )
        if(Lugar=="I")
          Valor= Caracter+ Valor;
        else
          Valor= Valor+ Caracter;
     return Valor;
  };

  function isEmpty(s) {
    return ((s == null) || (s.length == 0))
  }

  function isWhitespace (s){
    var i;
    if (isEmpty(s))
      return true;
    for (i = 0; i < s.length; i++) {
      var c = s.charAt(i);
      if (whitespace.indexOf(c) == -1)
        return false;
    }
    return true;
  }

  function isLetter (c) {
	  var caracteresEspeciales= "ñÑáéíóúÁÉÍÓÚüÜ";
    return ( ((c >= "a") && (c <= "z")) || ((c >= "A") && (c <= "Z")) || (caracteresEspeciales.indexOf(c)>= 0) )
  }

 function isDigit (c) {
   return ((c >= "0") && (c <= "9"))
 }

 function isLetterOrDigit (c) {
   return (isLetter(c) || isDigit(c))
 }

 function isAlphabetic (s) {
   var i;
   if (isEmpty(s))
     if (isAlphabetic.arguments.length == 1)
       return defaultEmptyOK;
     else
       return (isAlphabetic.arguments[1] == true);
   for (i = 0; i < s.length; i++) {
     var c = s.charAt(i);
     if (!isLetter(c))
       return false;
   }
   return true;
 }

 function isAlphanumeric (s){
   var i;
   if (isEmpty(s))
     if (isAlphanumeric.arguments.length == 1)
       return defaultEmptyOK;
     else
       return (isAlphanumeric.arguments[1] == true);
   for (i = 0; i < s.length; i++) {
     var c = s.charAt(i);
     if (! (isLetter(c) || isDigit(c) || (c==" ") ) )
        return false;
   }
   return true;
 }

 function isPath (s){
   var i;
   if (isEmpty(s))
     if (isAlphanumeric.arguments.length == 1)
       return defaultEmptyOK;
     else
       return (isAlphanumeric.arguments[1] == true);
   for (i = 0; i < s.length; i++) {
     var c = s.charAt(i);
     if (! (isLetter(c) || isDigit(c) || (c=="/") || (c==".")) )
        return false;
   }
   return true;
 }

 function isSignedFloat(s) {
   if (isEmpty(s))
     if (isSignedFloat.arguments.length == 1)
       return defaultEmptyOK;
     else
       return (isSignedFloat.arguments[1] == true);
   else {
     var startPos = 0;
     var secondArg = defaultEmptyOK;
     if (isSignedFloat.arguments.length > 1)
       secondArg = isSignedFloat.arguments[1];
     if ( (s.charAt(0) == "-") || (s.charAt(0) == "+") )
       startPos = 1;
     return (isFloat(s.substring(startPos, s.length), secondArg))
   }
 }

 function isFloat (s) {
   var i;
   var seenDecimalPoint = false;
   if (isEmpty(s))
     if (isFloat.arguments.length == 1)
       return defaultEmptyOK;
     else
       return (isFloat.arguments[1] == true);
    if (s == decimalPointDelimiter)
      return false;
   for (i = 0; i < s.length; i++) {
     var c = s.charAt(i);
     if ((c == decimalPointDelimiter) && !seenDecimalPoint)
       seenDecimalPoint = true;
     else
       if (!isDigit(c))
        return false;
   }
   return true;
 }

 function isInteger (s) {
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

 function isEmail (s) {
   if (isEmpty(s))
     if (isEmail.arguments.length == 1)
       return defaultEmptyOK;
     else
       return (isEmail.arguments[1] == true);
   if (isWhitespace(s))
     return false;
   var i = 1;
   var sLength = s.length;
   while ((i < sLength) && (s.charAt(i) != "@")) {
     i++;
   }
   if ((i >= sLength) || (s.charAt(i) != "@"))
     return false;
   else
     i += 2;
   while ((i < sLength) && (s.charAt(i) != ".")) {
     i++;
   }
   if ((i >= sLength - 1) || (s.charAt(i) != "."))
     return false;
   else
     return true;
 }

 function isSmallInt( Valor ) {
   return isNaN(Valor) && (Valor.indexOf(".")< 0);
 }

 function isSignedInteger (s) {
   if (isEmpty(s))
     if (isSignedInteger.arguments.length == 1)
       return defaultEmptyOK;
     else
       return (isSignedInteger.arguments[1] == true);
   else {
     var startPos = 0;
     var secondArg = defaultEmptyOK;
     if (isSignedInteger.arguments.length > 1)
       secondArg = isSignedInteger.arguments[1];
     if ( (s.charAt(0) == "-") || (s.charAt(0) == "+") )
       startPos = 1;
     return (isInteger(s.substring(startPos, s.length), secondArg))
   }
 }

 function isNonnegativeInteger (s) {
   var secondArg = defaultEmptyOK;
   if (isNonnegativeInteger.arguments.length > 1)
     secondArg = isNonnegativeInteger.arguments[1];

  return (isSignedInteger(s, secondArg) && ( (isEmpty(s) && secondArg)  || (parseInt (s, 10) >= 0) ) );
 }

 function isPositiveInteger (s) {
   var secondArg = defaultEmptyOK;
   if (isPositiveInteger.arguments.length > 1)
     secondArg = isPositiveInteger.arguments[1];
   return (isSignedInteger(s, secondArg) && ( (isEmpty(s) && secondArg)  || (parseInt (s, 10) > 0) ) );
 }

 function isIntegerInRange (s, a, b) {
  if (isEmpty(s))
    if (isIntegerInRange.arguments.length == 1)
      return defaultEmptyOK;
    else
      return (isIntegerInRange.arguments[1] == true);
   if (!isInteger(s, false))
     return false;
   var num = parseInt (s, 10);
// 	 alert("Mes:"+ num+ " s= ["+ s+ "] => "+ a+ ":"+ b+ " => "+ ((num >= a) && (num <= b)) );
   return ((num >= a) && (num <= b));
 }

 function isYear (s) {
   if (isEmpty(s))
     if (isYear.arguments.length == 1)
       return defaultEmptyOK;
     else
       return (isYear.arguments[1] == true);
   if (!isNonnegativeInteger(s))
    return false;
   return ((s.length == 2) || (s.length == 4));
 }

 function isMonth (s) {
   if (isEmpty(s))
     if (isMonth.arguments.length == 1)
       return defaultEmptyOK;
   else
     return (isMonth.arguments[1] == true);
   return isIntegerInRange (s, 1, 12);
 }

 function isDay (s) {
   if (isEmpty(s))
     if (isDay.arguments.length == 1)
       return defaultEmptyOK;
     else
       return (isDay.arguments[1] == true);
   return isIntegerInRange (s, 1, 31);
 }

 function daysInFebruary (year) {
   // February has 29 days in any year evenly divisible by four,
   // EXCEPT for centurial years which are not also divisible by 400.
   return (  ((year % 4 == 0) && ( (!(year % 100 == 0)) || (year % 400 == 0) ) ) ? 29 : 28 );
 }

 function isFormatDate(valor) {
   return isIntegerComdin(valor, "/") && isDate(valor.substr(6,4), valor.substr(3,2), valor.substr(0,2));
 }
    
 function getDate(valor) {
   return valor.substr(6,4)+ valor.substr(3,2)+ valor.substr(0,2);
 }
    
 
 function isDate(year, month, day) {
// 	 alert("fecha: "+ year+ " / "+ month+ " / "+ day+ " => "+ isYear(year, false) + " && "+ isMonth(month, false)+ " && "+ isDay(day, false));
   if (! (isYear(year, false) && isMonth(month, false) && isDay(day, false)))
     return false;
   var intYear = parseInt(year, 10);
   var intMonth = parseInt(month, 10);
   var intDay = parseInt(day, 10);
   if (intDay > daysInMonth[intMonth- 1])
     return false;
   if ((intMonth == 2) && (intDay > daysInFebruary(intYear)))
     return false;
   return true;
 }

 function isDouble ( Valor ) {
   return isNaN(Valor);
 }


 function promptStatus (s) {
  window.status = s
 }

 function stripCharsInBag (s, bag) {
   var i;
   var returnString = "";
   for (i = 0; i < s.length; i++) {
     var c = s.charAt(i);
     if (bag.indexOf(c)== -1)
       returnString += c;
   }
   return returnString;
 }

 function stripCharsNotInBag (s, bag) {
   var i;
   var returnString = "";
   for (i = 0; i < s.length; i++) {
     var c = s.charAt(i);
     if (bag.indexOf(c)!= -1)
       returnString += c;
   }
   return returnString;
 }

 function stripWhitespace (s) {
   return stripCharsInBag (s, whitespace)
 }

  function msgError(msg) {
    promptStatus(msg);
    alert(msg);
	  return false;
	}

 function isAlphabeticComodin (s, caracters) {
   var i;
   if (isEmpty(s))
     if (isAlphabetic.arguments.length == 1)
       return defaultEmptyOK;
     else
       return (isAlphabetic.arguments[1] == true);
   for (i = 0; i < s.length; i++) {
     var c = s.charAt(i);
     if (!(isLetter(c) || (caracters.indexOf(c)>=0)))
       return false;
   }
   return true;
 }

 function isAlphanumericComodin(s, caracters){
   var i;
   if (isEmpty(s))
     if (isAlphanumeric.arguments.length == 1)
       return defaultEmptyOK;
     else
       return (isAlphanumeric.arguments[1] == true);
   for (i = 0; i < s.length; i++) {
     var c = s.charAt(i);
     if (! (isLetter(c) || isDigit(c) || (caracters.indexOf(c)>=0) ))
       return false;
   }
   return true;
 }

 function isIntegerComdin (s, caracters) {
   var i;
   if (isEmpty(s))
     if (isInteger.arguments.length == 1)
       return defaultEmptyOK;
     else
       return (isInteger.arguments[1] == true);
   for (i = 0; i < s.length; i++) {
     var c = s.charAt(i);
     if (!(isDigit(c) || (caracters.indexOf(c)>=0)))
       return false;
   }
   return true;
 }

  function buscarFecha(fecha, funcion, operador) {
    var regresar= '';
    for(var j= 0; j< document.forms.length; j++) {
      var formulario= document.forms[j];
      for(var i=0; i< formulario.elements.length && regresar.length== 0; i++) {
        var obj= formulario.elements[i];
        if(obj.type== "text" && obj.name.indexOf(PALABRA)>=0 && obj.name.indexOf("_"+ funcion+ "_")>=0 && obj.value.length!= 0) {
          var valor = obj.value;
          var cadena= '!('+ fecha+ operador+ valor.substr(6,4)+valor.substr(3,2)+valor.substr(0,2)+ ')';
          if(eval(cadena)) {
            regresar= valor.substr(0,2)+"/"+valor.substr(3,2)+"/"+valor.substr(6,4);
          }; // if
        }; // if
      }; // for i
    }; // for j
    return regresar;
  }; // buscarFecha

  function buscarEntero(entero, funcion, operador) {
    var regresar= '';
    for(var j= 0; j< document.forms.length; j++) {
      var formulario= document.forms[j];
      for(var i=0; i< formulario.elements.length && regresar.length== 0; i++) {
        var obj= formulario.elements[i];
        if(obj.type== "text" && obj.name.indexOf(PALABRA)>=0 && obj.name.indexOf("_"+ funcion+ "_")>=0 && obj.value.length!= 0) {
          var valor = obj.value;
          var cadena= '!('+ entero+ operador+ valor+')';
          if(eval(cadena)) {
            regresar= valor;
          }; // if
        }; // if
      }; // for i
    }; // for j
    return regresar;
  }; // buscarFecha

  function funcionObjeto(valor, funcion, nombre) {
    switch(funcion) {
      case 'Z': // No realizar ninguna verificación
        break;
      case 'A': // Caracteres permitidos [A-Z b . _]
        if(!isAlphabeticComodin(valor, " ._"))
          return msgError("Error en el campo ["+ nombre+ "]\nExiste algunos caracter no permitido. <"+ valor+ ">");
        break;
      case 'B': // Caracteres permitidos [A-Z a-z 0-9 b . - # + / ? ( ) [ ] , ; : _ ñ Ñ @ ]
        if(!isAlphanumericComodin(valor, " .-#+/?()[],;:'><_%&/+*$¡¿?"))
          return msgError("Error en el campo ["+ nombre+ "]\nExiste algunos caracter no permitido. <"+ valor+ ">");
        break;
      case 'C': // Caracteres permitidos [0-9 /]  y q sea fecha
        if(!isIntegerComdin(valor, "/") || !isDate(valor.substr(6,4), valor.substr(3,2), valor.substr(0,2)))
          return msgError("Error  en el campo ["+ nombre+ "]\nLa fecha es incorrecta. [DD/MM/AAAA] ["+ valor.substr(0,2)+"/"+valor.substr(3,2)+"/"+valor.substr(6,4)+ "]");
        break;
      case 'D': // Caracteres permitidos [S,N]
        if(valor!= 'S' &&  valor!= 'N') //si no es ni N ni S, entonces marca error
          return msgError("Error en el campo ["+ nombre+ "]\nLetra incorrecta, solo 'N' o 'S'. <"+ valor+ ">");
        break;    
      case 'E': // Caracteres permitidos [0-9]
        if(!isInteger(valor))
          return msgError("Error en el campo ["+ nombre+ "]\nNúmero entero incorrecto. <"+ valor+ ">");
        break;
      case 'F': // Caracteres permitidos [0-9 . + -]
        if(!isFloat(valor))
          return msgError("Error en el campo ["+ nombre+ "]\nNúmero de punto flotante incorrecto. <"+ valor+ ">");
        break;
      case 'G': // Caracteres permitidos [A-Z a-z 0-9]
        if(!isAlphanumeric(valor))
          return msgError("Error en el campo ["+ nombre+ "]\nExiste algunos caracter no permitido.<"+ valor+ ">");
        break;
      case 'H': // Caracteres permitidos [A-Z a-z ~]
        if(!isAlphabeticComodin(valor, "~"))
          return msgError("Error en el campo ["+ nombre+ "]\nExiste algunos caracter no permitido.<"+ valor+ ">");
        break;
      case 'I': // Es un correo electronico
        if(!isEmail(valor, "~"))
          return msgError("Error en el correo electrónico ["+ nombre+ "]\nExiste algunos caracter no permitido.<"+ valor+ ">");
        break;
      case 'J': // Caracteres permitidos [0-9 . y tiene que ser menor a 100]
        if(!isFloat(valor) || parseInt(valor, 10)> 99 )
          return msgError("Error en el campo ["+ nombre+ "]\nNúmero de punto flotante tiene que ser menor a 100. <"+ valor+ ">");
        break;
      case 'K': // Caracteres permitidos [0-9 justificado con ceros a la izquierda]
        if(!isInteger(valor))
          return msgError("Error en el campo ["+ nombre+ "]\nExiste algunos caracter no permitido.<"+ valor+ ">");
        break;
      case 'L': // Caracteres permitidos [0-9 y el valor tiene que estar entre el 0 y 11]
        if(!isInteger(valor) || !(parseInt(valor, 10)>=0 && parseInt(valor, 10)< 12))
          return msgError("Error en el campo ["+ nombre+ "]\nExiste algunos caracter no permitido y el valor debe esta entre 0-12.<"+ valor+ ">");
        break;
      case 'M': // Caracteres permitidos [0-9 /] fecha y tiene que ser menor o igual a la fecha con código N
        if(!isIntegerComdin(valor, "/") || !isDate(valor.substr(6,4), valor.substr(3,2), valor.substr(0,2)))
          return msgError("Error  en el campo ["+ nombre+ "]\nLa fecha es incorrecta. [DD/MM/AAAA] ["+ valor.substr(0,2)+"/"+valor.substr(3,2)+"/"+valor.substr(6,4)+ "]");
        else {
          var fecha= buscarFecha(valor.substr(6,4)+valor.substr(3,2)+valor.substr(0,2), 'N', '<=');
          if(fecha.length!= 0)
            return msgError("Error en el campo ["+ nombre+ "]\nLa fecha ["+ valor.substr(0,2)+"/"+valor.substr(3,2)+"/"+valor.substr(6,4)+ "] tiene que ser menor o igual la fecha ["+ fecha+ "]");
        }; // if
        break;
      case 'N': // Caracteres permitidos [0-9 /] fecha y tiene que ser mayor o igual a la fecha con código M
        if(!isIntegerComdin(valor, "/") || !isDate(valor.substr(6,4), valor.substr(3,2), valor.substr(0,2)))
          return msgError("Error  en el campo ["+ nombre+ "]\nLa fecha es incorrecta. [DD/MM/AAAA] ["+ valor.substr(0,2)+"/"+valor.substr(3,2)+"/"+valor.substr(6,4)+ "]");
        else {
          var fecha= buscarFecha(valor.substr(6,4)+valor.substr(3,2)+valor.substr(0,2), 'M', '>=');
          if(fecha.length!= 0)
            return msgError("Error en el campo ["+ nombre+ "]\nLa fecha ["+ valor.substr(0,2)+"/"+valor.substr(3,2)+"/"+valor.substr(6,4)+ "] tiene que ser mayor o igual la fecha ["+ fecha+ "]");
        }; // if
        break;
      case 'O': // Caracteres permitidos [0-9 tiene que ser menor a 0 y menor a 100 para porcentajes %]
        if(!isFloat(valor) || parseInt(valor, 10)< 0 || parseInt(valor, 10)> 100)
          return msgError("Error en el campo ["+ nombre+ "]\nNúmero de punto flotante tiene que ser menor o igual 0 y menor 100. <"+ valor+ ">");
        break;
      case 'P': // Caracteres permitidos [A,D,F,H,I,P,Q,U,W,0123456789]
			  if(valor!= "D" && valor!= "F" && valor!= "H" && valor!= "I" && valor!= "P" && valor!= "Q" && valor!= "V" && valor!= "0") {
					var letra     = valor.substring(0, 1);
					var porcentaje= valor.substring(1);
//					alert(letra+ ":"+ porcentaje+ ":"+ isFloat(porcentaje));
          if(letra!= "A" && letra!= "U" && letra!= "W") {
            return msgError("Error en el campo ["+ nombre+ "]\nEl código de la incidencia es incorrecto para la celda. <"+ valor+ ">");
					}
   				else
					  if(!isFloat(porcentaje)) {
              return msgError("Error en el campo ["+ nombre+ "]\nEl porcentaje es incorrecto. <"+ valor+ ">");
    				}
						else {
						  if((letra== "U" || letra== "W") && (parseInt(porcentaje, 10)< 1 || parseInt(porcentaje, 10)> 25)) {
                return msgError("Error en el campo ["+ nombre+ "]\nEl porcentaje deberá ser menor a 25%. <"+ valor+ ">");
							}
							else {
  						  if(parseInt(porcentaje, 10)< 1 || parseInt(porcentaje, 10)> 99) {
                  return msgError("Error en el campo ["+ nombre+ "]\nEl porcentaje es incorrecto. <"+ valor+ ">");
				  			}; // if
							}; // if
						}; // if
				}; // if
        break;
      case 'Q': // Caracteres permitidos [A-Z a-z 0-9 b .-_]
        if(!isAlphanumericComodin(valor, " .-_"))
          return msgError("Error en el campo ["+ nombre+ "]\nExiste algunos caracter no permitido. <"+ valor+ ">");
        break;
      case 'R': // Caracteres permitidos [A-Z a-z 0-9]
        if(!isAlphanumericComodin(valor, ".-#+/?()[],;:'><_%&/+*$¡¿?"))
          return msgError("Error en el campo ["+ nombre+ "]\nExiste algunos caracter no permitido.<"+ valor+ ">");
        break;
      case 'S': // cuenta HSBC valida [algoritmo HSBC]
        if((!isInteger(valor)) || (!isCuentaHSBC(valor)))
          return msgError("Error en el campo ["+ nombre+ "]\nLa cuenta HSBC no es valida. <"+ valor+ ">");
        break;
      case 'T': // Validación para campo duplicados, acepta C, S, N.
        valor = valor.toUpperCase();
        if( (valor!='C') && (valor!='S') && (valor!='N' ) )
            return msgError("Error  en el campo ["+ nombre+ "]\nEl valor debe ser [C ó S ó N] ");
      break;
      case 'W': // Caracteres permitidos [0-9 /] y tiene que ser menor o igual a la fecha con código W
        if(!isInteger(valor))
          return msgError("Error  en el campo ["+ nombre+ "]\nEl número es incorrecto. ");
        else {
          var entero= buscarEntero(valor, 'X', '<=');
          if(entero.length!= 0)
            return msgError("Error en el campo ["+ nombre+ "]\nEl número ["+ valor + "] tiene que ser menor o igual al número a ["+ entero+ "]");
        }; // if
        break;
      case 'X': // Caracteres permitidos [0-9] y tiene que ser mayor o igual que el valor con código W
        if(!isInteger(valor))
          return msgError("Error  en el campo ["+ nombre+ "]\nEl número es incorrecto. ");
        else {
          var entero= buscarEntero(valor, 'W', '>=');
          if(entero.length!= 0)
            return msgError("Error en el campo ["+ nombre+ "]\nEl número ["+ valor + "] tiene que ser mayor o igual al número a ["+ entero+ "]");
        }; // if
        break;
      default : // Casos no considerados
   			promptStatus("Validar: ! Caso no considerado. !" );
//        alert( "Validar \nTexto: "+ valor+ "\nCaso no conciderado. !" );
   };
  	promptStatus("Campo:"+ nombre+ " ok.");
   return true;
  }; // validarObjeto


function isCuentaHSBC(valor){
  var regla= 2;
  var suma = 0;
  var dec = "";
  for (var i =2; i<=valor.length-2;i++){
    if (regla==2){
      if ((parseInt(valor.charAt(i))*2)>9){
        dec =(parseInt(valor.charAt(i))*2)+"-";
        dec= (parseInt(dec.charAt(0))+parseInt(dec.charAt(1)));
        suma = suma +parseInt(dec);
      }
      else
        suma = suma +parseInt(valor.charAt(i))*2;
      regla= 1;
    }
    else{
      suma = suma +parseInt(valor.charAt(i));
      regla= 2;
    }
  }

  if (suma<=10) 
    dec= 10;
  else{
    dec= suma+"-";
    if (parseInt(dec.charAt(1))>0)
      dec= (parseInt(dec.charAt(0))+1)+"0";
    else
      dec= dec.charAt(0)+"0";
    }

  if ((dec-suma)!=parseInt(valor.charAt(valor.length-1)))
    return false;
  else 
    return true;
}


  function verificaObjeto(objeto, nonulo) {
    var Tokens= objeto.name.split("_");
//    alert(    "Nombre: "+ objeto.name+
//         "\nConstante: "+ Tokens[CONSTANTE]+
//           "\nColumna: "+ Tokens[COLUMNA]+
//           "\nFuncion: "+ Tokens[FUNCION]+
//           "\nNo nulo: "+ Tokens[NONULO]+
//           "\nNombre nulo: "+ Tokens[NOMBRE]+
//           "\nLongitud nulo: "+ Tokens[LONGITUD]);
    var verificaNoNulo= true;
    if(nonulo) {
  	  if(objeto.value.length== 0) {
			  verificaNoNulo= false;
   		  if(Tokens[NONULO]=="S")
			    alert("Error: \nEl campo no puede ir vacio.");
  	    else
				  return true;
			}
		}
		else
		  verificaNoNulo= objeto.value.length> 0;
    if(verificaNoNulo) {
		  if(funcionObjeto(objeto.value, Tokens[FUNCION], Tokens[NOMBRE])) {
			  if(Tokens[FUNCION]== 'K') {
//           alert(objeto.name+ "=>"+ objeto.value+ "=>"+ Justificar(objeto.value, "0", Tokens[LONGITUD], "I"));
           objeto.value= Justificar(objeto.value, "0", Tokens[LONGITUD], "I");
				};
      	promptStatus("["+ Tokens[NOMBRE]+ "] ok. !" );
			}
			else
			  verificaNoNulo= false
    }; // if
	  return verificaNoNulo;
	}; // verificaObjeto

  function habilitaObjeto(objeto, valor) {
		if (objeto.name=='ajg_1_G_S_Curp_18') {
			 objeto.value=valor;
		}
	}; // habilitaObjeto


  function verificaSelect(objeto, nonulo) {
    var Tokens= objeto.name.split("_");
//    alert(    "Nombre: "+ objeto.name+
//         "\nConstante: "+ Tokens[CONSTANTE]+
//           "\nColumna: "+ Tokens[COLUMNA]+
//           "\nFuncion: "+ Tokens[FUNCION]+
//           "\nNo nulo: "+ Tokens[NONULO]+
//           "\nNombre : "+ Tokens[NOMBRE]+;
//           "\nLongitud: "+ Tokens[LONGITUD]);
    var verificaNoNulo= true;
    if(nonulo) {
  	  if(objeto.value.length== 0) {
			  verificaNoNulo= false;
   		  if(Tokens[NONULO]=="S")
			    alert("Error: \nEl campo no puede ir vacio. ["+ Tokens[NOMBRE]+"]");
  	    else
				  return true;
     	//	if(objeto.options[objeto.selectedIndex].text.indexOf("Sin información")>=0) {
			}
		}
		else
		  verificaNoNulo= objeto.value.length> 0;
	  return (verificaNoNulo);
	}; // verificaSelect

  function verificaFormulario(formulario) {
	   for(var x=0; x< formulario.elements.length; x++) {
		   var objeto= formulario.elements[x];
			 if(objeto.type== "text" && objeto.name.indexOf(PALABRA)>=0) {
			   if(!verificaObjeto(objeto, true)) {
				   objeto.focus();
  				 return false;
    		 }; // if
    	 }
       else
  			 if(objeto.type== "select-one" && objeto.name.indexOf(PALABRA)>=0) {
  			   if(!verificaSelect(objeto, true)) {
	  			   objeto.focus();
  	  			 return false;
    	  	 }; // if
         }; // if
		 }; // for
   	 promptStatus("Formulario correcto. !" );
		 return confirm("Los datos capturados en la página son correctos.\n ¿ Desea enviarlos para su almacenamiento ?");
	}; // verificaFormulario

  function validarFormulario(formulario, palabra) {
	   for(var x=0; x< formulario.elements.length; x++) {
		   var objeto= formulario.elements[x];
			 if(objeto.type== "text" && objeto.name.indexOf(palabra)>=0) {
			   if(!verificaObjeto(objeto, true)) {
				   objeto.focus();
  				 return false;
    		 }; // if
    	 }
       else
  			 if(objeto.type== "select-one" && objeto.name.indexOf(PALABRA)>=0) {
  			   if(!verificaSelect(objeto, true)) {
	  			   objeto.focus();
  	  			 return false;
    	  	 }; // if
         }; // if
		 }; // for
   	 promptStatus("Formulario correcto. !" );
		 return confirm("Los datos capturados en la página son correctos.\n ¿ Desea enviarlos para su almacenamiento ?");
	}; // verificaFormulario

  function PorloActivoUno(Formulario, Tipo, Tabla) {
	  var Encontro= false;
	  var Objeto;
    var x=0;
    while (x< Formulario.elements.length && !Encontro) {
	    Objeto= Formulario.elements[x];
//  	  alert(Objeto.type+ " "+ Objeto.name+ " "+ Objeto.checked+ " "+ Objeto.value.indexOf(Tabla)>= 0);
	    if(Objeto.type== Tipo && Objeto.name.indexOf(Tabla)>= 0) {
        Encontro= Objeto.checked;
      }; // if
      x++;
	  }
	  return Encontro;
  }; // PorloActivoUno

  function seleccionoUno(Formulario, Tipo, Tabla) {
	  var Encontro= false;
	  var Objeto= null;
    var x=0;
    while (x< Formulario.elements.length && !Encontro) {
	    Objeto= Formulario.elements[x];
	    if(Objeto.type== Tipo && Objeto.name.indexOf(Tabla)>= 0 && Objeto.checked) {
        Encontro= true;
      }; // if
      x++;
	  }
	  return Encontro;
  }; // seleccionoUno

  function PorloMenosUnoSeleccionado(Formulario, Tipo, Tabla, Valor) {
	  var Encontro= false;
	  var Objeto= null;
    var x=0;
    while (x< Formulario.elements.length && !Encontro) {
	    Objeto= Formulario.elements[x];
//  	  alert(Objeto.type+ " "+ Objeto.name+ " "+ Objeto.checked+ " "+ Objeto.value.indexOf(Tabla)>= 0);
	    if(Objeto.type== Tipo && Objeto.name.indexOf(Tabla)>= 0) {
        Encontro= Objeto.options[Objeto.selectedIndex].value!= Valor;
      }
      x++;
	  }
	  return Encontro;
  }; // PorloMenosUnoSeleccionado

  function PorloMenos(formulario, tipo, tabla, cuantos) {
	  var objeto  = null;
    var x       = 0;
		var checados= 0;
    while (x< formulario.elements.length) {
	    objeto= formulario.elements[x];
//  	  alert(objeto.type+ " "+ tipo+ " "+ objeto.name+ " "+ objeto.checked+ " "+ objeto.name.indexOf(tabla));
	    if(objeto.type== tipo && objeto.name.indexOf(tabla)>= 0 && objeto.checked) {
        checados++;
      }
      x++;
	  }
//    alert(checados+ " "+ cuantos);
	  return checados>= cuantos;
  }; // PorloMenos

  function regresaObjeto(formulario, nombre) {
		var x= 0;
	  var elemento= null;
	  while(x< formulario.elements.length && formulario.elements[x].name.indexOf(nombre)< 0) {
 		  x++;
		}; // while
		if(x< formulario.elements.length)
		  elemento= formulario.elements[x];
		return elemento;
	}; //regresaObjeto

  function fullscreen(url, w, h) {
    var l = (screen.availWidth-500)/2;
	  var t = (screen.availHeight-400)/2;
    var features = 'width='+ w+ ', height='+ h+ ', left='+l+', top='+t+', screenX=0, screenY=0';
    features+= 'location=0, scrollbars=1, resizable=0, menubar=0, toolbar=0, status=0';
    window.open(url, '', features);
  }

  function Trim(strTrim){
  	return RTrim(LTrim(strTrim));
  }

  function LTrim(strTrim){
  	while( strTrim.indexOf(' ') == 0 ){ strTrim = strTrim.substring(1) }
    	return strTrim;
  }

  function RTrim(strTrim){
  	while( strTrim.lastIndexOf(' ') == strTrim.length-1 && strTrim.length > 0 ){ strTrim = strTrim.substring(0,strTrim.length-1) }
    	return strTrim;
  }

  function verificaSinConfirmar(formulario) {
	   for(var x=0; x< formulario.elements.length; x++) {
		   var objeto= formulario.elements[x];
			 if(objeto.type== "text" && objeto.name.indexOf(PALABRA)>=0) {
			   if(!verificaObjeto(objeto, true)) {
				   objeto.focus();
  				 return false;
    		 }; // if
    	 }
       else
  			 if(objeto.type== "select-one" && objeto.name.indexOf(PALABRA)>=0) {
  			   if(!verificaSelect(objeto, true)) {
	  			   objeto.focus();
  	  			 return false;
    	  	 }; // if
         }; // if
		 }; // for
   	 promptStatus("Formulario correcto. !" );
     return true;
	}; // verificaFormulario

	function marcarCheckBox(formulario, tipo, tabla) {
	  var objeto;
    var x= 0;
    while (x< formulario.elements.length) {
	    objeto= formulario.elements[x];
	    if(objeto.type== tipo && objeto.name.indexOf(tabla)>= 0) {
        objeto.checked= !objeto.checked;
      }
      x++;
	  }
  }; // marcarCheckBox
  
  function armarURL(formulario){
    var laForma = formulario;       
    var url ='';
    var ya = 0;  
    for(var i=0; i<laForma.elements.length; i++){
      var obj = laForma.elements[i];
      obj.value=obj.name=="funcion"?2:obj.value;  
      if(obj.type=="text") {  //es un texto pasar value
          url += ya==0?'?':'&';
          url +=obj.name+'='+obj.value; 
          ya=1;
      }
      else {  //checar que sea select 
         if(obj.type=="select-one"){
            url += ya==0?'?':'&';
            url+=obj.name+'='+obj.options[obj.selectedIndex].value;
            ya=1;
         }
      }// if --tipo del objeto--
    }// for
    alert (url);
    return url;
  }// armar URL
  
  // busca en la descripcion del combo la primera opcion que cotenga
  // las letras de la cadena
  function buscarDescripcionEnCombo(combo, cadena){
    for(var i=0; i<combo.options.lenght; i++){
      var opcion = combo.options[i].text;
      if(opcion.indexOf( cadena )>=0){
        combo.selectedIndex = i;
      }
    }
  }
  
  function disabledElements(formulario, tipos) {
    var objeto= null;
    var x=0;
    while (x< formulario.elements.length) {
      objeto= formulario.elements[x];
      if(tipos.indexOf(objeto.type)>= 0) {
        if(document.all)
          objeto.readOnly= true;
        else  
          objeto.disabled= true;
      } // if
      x++;
    } // while
  } // disabledElements
 
  function cleanElements(formulario) {
    var objeto= null;
    var x=0;
    while (x< formulario.elements.length) {
      objeto= formulario.elements[x];
      if('|text|'.indexOf(objeto.type)>= 0 && objeto.value== 'null') {
        objeto.value= '';
      } // if
      if('|select-one|'.indexOf(objeto.type)>= 0) {
        objeto.selectedIndex= 0;
      } // if
      x++;
    } // while
  }; // cleanElements
    
  function addSingleOption(numero, nombreCompleto, destino) {
    var nombre= numero+ " "+ nombreCompleto;
    destino.options[destino.options.length]= new Option(nombre, numero);
    destino.size= destino.options.length;
  }; // addSingleOption
    
  function deleteSelect(fuente) {
    if(fuente.selectedIndex>= 0) 
      fuente.options[fuente.selectedIndex]= null;
    if(fuente.options.length>= 0) {
      fuente.size= fuente.options.length;
      fuente.selectedIndex= 0;
    };
  }; // deleteSelect
    
  function dontExistsOption(fuente, persona) {
	  var x= 0;
	  while (x< fuente.options.length && fuente.options[x].value!= persona)
	    x++;
	  return x>= fuente.options.length ;
  }; // dontExistsOption
	
  function addOptionSelect(fuente, destino) {
    if(noExisteElemento(destino, fuente.options[fuente.selectedIndex].value)) {
  	  var nombre= fuente.options[fuente.selectedIndex].value+ ">"+ fuente.options[fuente.selectedIndex].text;
	    destino.options[destino.options.length]= new Option(fuente.options[fuente.selectedIndex].text, fuente.options[fuente.selectedIndex].value);
	    destino.size= destino.options.length;
    }  		
	  else {
	    alert("El elemento ya se encuentra en la lista. !");
	  }; // if
  }; // addOptionSelect
	
  function activeSelect(fuente) {
	  if (fuente.options.length> 0) {
      for(var x=0; x< fuente.options.length; x++) {
	      fuente.options[x].selected= true;
	    }; // if
  	} // if
  }; // activeSelect
	
 
 