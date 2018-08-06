/* --------------------------------------------------------
    Nombre : fcgencValida
    Descripción : Función de validación para elementos en HTML
    Fecha de creación : 15 de Enero del 2004
    Autor : SIA
    Fecha de modificación : 15 de Enero del 2004
    Autor última modificación : Jesús Urraca
   -------------------------------------------------------- */
  
  var CONSTANTE     = 0;
  var REGISTRO      = 1;
  var COLUMNA       = 2;
  var VALIDAR       = 3;
  var JUSTIFICAR    = 4;
  var LUGAR         = 5;
  var LONGITUD      = 6;
  var defaultEmptyOK= false;
  var decimalPointDelimiter= ".";
  var promptStr     = "Error: ";
  var whitespace    = " \t\n\r";
  

  // Justifica una cadena de carácteres
  function Justificar( Valor, Caracter, Longitud, Lugar ) {
    if ( Caracter!= "#" ) 
      for(var x= Valor.length; x<= Longitud; x++ ) 
        if(Lugar=="I")
          Valor= Caracter+ Valor; 
        else  
          Valor= Valor+ Caracter; 
     return Valor;
  };

  // Valida si es NULL
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

  // Valida si es Alfabético
  function isLetter (c) {
    return ( ((c >= "a") && (c <= "z")) || ((c >= "A") && (c <= "Z")) )
  }

 // Valida si es Númerico
 function isDigit (c) {
   return ((c >= "0") && (c <= "9"))
 }

 // Valida si es Alfabético o Númerico
 function isLetterOrDigit (c) {
   return (isLetter(c) || isDigit(c))
 }

 // Valida si es Alfabético, utiliza isLetter
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

 // Valida si es Alfanúmerico
 function isAlphanumeric (s){ 
   var i;
   if (isEmpty(s)) 
     if (isAlphanumeric.arguments.length == 1) 
       return defaultEmptyOK;
     else 
       return (isAlphanumeric.arguments[1] == true);
   for (i = 0; i < s.length; i++) {   
     var c = s.charAt(i);
     if (! (isLetter(c) || isDigit(c) ) )
        return false;
   }
   return true;
 }
   
 // Valida si es Decimal o Punto Flotante
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
  

 // Valida si es entero
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
  
 // Valida Correo Electrónico
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

  return (isSignedInteger(s, secondArg) && ( (isEmpty(s) && secondArg)  || (parseInt (s) >= 0) ) );
 }
 
 function isPositiveInteger (s) {
   var secondArg = defaultEmptyOK;
   if (isPositiveInteger.arguments.length > 1)
     secondArg = isPositiveInteger.arguments[1];
   return (isSignedInteger(s, secondArg) && ( (isEmpty(s) && secondArg)  || (parseInt (s) > 0) ) );
 }
 
 function isIntegerInRange (s, a, b) {
  if (isEmpty(s)) 
    if (isIntegerInRange.arguments.length == 1) 
      return defaultEmptyOK; 
    else 
      return (isIntegerInRange.arguments[1] == true);
   if (!isInteger(s, false)) 
     return false;
     var num=parseInt(s,10);
   return ((num >= a) && (num <= b))
 }

 function isYear (s) {
   if (isEmpty(s)) 
     if (isYear.arguments.length == 1) 
       return defaultEmptyOK;
     else 
       return (isYear.arguments[1] == true);
   if (!isNonnegativeInteger(s)) 
    return false;
   return (s.length == 4);
 }

 function isMonth (s) {
   if (isEmpty(s)) 
     if (isMonth.arguments.length == 1) 
        return defaultEmptyOK;
   else return (isMonth.arguments[1] == true);
   return isIntegerInRange (s,1,12);
 }

 function isDay (s) {
   if (isEmpty(s)) 
     if (isDay.arguments.length == 1) 
       return defaultEmptyOK;
     else 
       return (isDay.arguments[1] == true);   
   return isIntegerInRange (s,1,31);
 }

 function daysInFebruary (year) {
   // February has 29 days in any year evenly divisible by four,
   // EXCEPT for centurial years which are not also divisible by 400.
   return (  ((year % 4 == 0) && ( (!(year % 100 == 0)) || (year % 400 == 0) ) ) ? 29 : 28 );
 }

 // Valida Fecha
 function isDate (day, month, year, fecha) {
   if (! (isDay(day, false) && isMonth(month, false) && isYear(year, false))) 
     return false;
   var daysInMonth = new Array
       daysInMonth[1] = 31; daysInMonth[2] = 29; // must programmatically check this 
	   daysInMonth[3] = 31; daysInMonth[4] = 30; 
	   daysInMonth[5] = 31; daysInMonth[6] = 30; 
	   daysInMonth[7] = 31; daysInMonth[8] = 31; 
	   daysInMonth[9] = 30; daysInMonth[10]= 31; 
	   daysInMonth[11]= 30; daysInMonth[12]= 31; 
   var intYear = parseInt(year);
   var intMonth = parseInt(month);
   var intDay = parseInt(day);
   if (intDay > daysInMonth[intMonth]) 
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

// Reemplaza los valores no permitidos para ORACLE por espacio en Blanco
function reemplazaValor(string) {
      if (string=="") string=" ";
	add="";
	match="|´[<>\"\';&\+’]";
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


// Txa - Limpia espacios en blanco y elimina caracteres no validos
function validaTxa(Valor) {
  Valor = reemplazaValor(Valor);
  Valor = trim(Valor); 
  return Valor;   
}

  // Valida objeto dependiendo de al opción seleccionada :
  // ------------------------------------------------------
  //    N        Cadena de tipo número entero
  //    R        Cadena de tipo número real o de punto flotante
  //    C        Cadena de tipo alfabética
  //    A        Cadena de tipo alfanúmerica
  //    F        Cadena de tipo fecha
  //    E        Cadena de tipo correo electrónico
  //    B        Elemento Nulo
  //    default  Casos no considerados
  // ------------------------------------------------------

  
function ValidarObjeto(Valor,Tipo) {
    var Correcto= false;
	switch(Tipo) {
	  case 'N': // Cadena de tipo numero entero
        Correcto = isInteger(Valor);
        if (!(Correcto)) alert("Error: \nNúmero entero incorrecto\nValor : "+ Valor); 
        break;
      case 'R': // Cadena de tipo numero real o de punto flotante
        Correcto = isFloat(Valor);
        if (!(Correcto)) alert("Error: \nNúmero real incorrecto\nValor : "+ Valor); 
        break;
      case 'C': // Cadena de tipo alfabetica
        Correcto = isAlphabetic(Valor);
     	if (!(Correcto)){ alert("Error: \nCadena de caracteres alfabetica incorrecta\nValor :  "+ Valor);} 
        break;
      case 'A': // Cadena de tipo alfanumerica
        Correcto = isAlphanumeric( Valor );
		if (!(Correcto)) alert("Error: \nCadena de caracteres alfanumérica incorrecta\nValor : "+ Valor); 
        break;
      case 'F': // Cadena de tipo fecha con formato AAAAMMDD
        Correcto = isDate( Valor.substr(0,2), Valor.substr(3,2), Valor.substr(6,4), Valor );
     	if (!(Correcto)){ alert("Error: \nLa Fecha es Incorrecta\nValor : "+ Valor.substr(0,2)+ "/"+ Valor.substr(3,2)+ "/"+ Valor.substr(6,4)); } 
        break;
      case 'E': // Cadena  de tipo correo electronico
        Correcto = isEmail( Valor );
        promptStr= "Error: \nEl correo electrónico es incorrecto\nValor : "+ Valor; 
		break;
   	  case 'B': // Elemento Nulo
	    Correcto = isEmpty( Valor);
		promptStr= "Error : \nCampo Vacío, favor de rellenarlo";
		break;
      default : // Casos no considerados 
        alert( "Validar \nTexto: "+ Valor+ "\nCaso no conciderado. !" );
   };
   return Correcto;
}
 
