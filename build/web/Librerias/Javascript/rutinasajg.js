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

   

  function Justificar( Valor, Caracter, Longitud, Lugar ) {

    if ( Caracter!= "#" ) 

      for(var x= Valor.length; x<= Longitud; x++ ) 

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

    return ( ((c >= "a") && (c <= "z")) || ((c >= "A") && (c <= "Z")) )

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





 function isLogin (s) { 

   var i;

   if (isEmpty(s)) 

      return true;

   for (i = 0; i < s.length; i++) {   

     var c = s.charAt(i);

		 if (!isLetter(c) && (!(c==".")) && (!isDigit(c)) ) 

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

     if (! (isLetter(c) || isDigit(c) ) )

        return false;

   }

   return true;

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

   var num = parseInt (s);

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



 function isDate (year, month, day) {

   if (! (isYear(year, false) && isMonth(month, false) && isDay(day, false))) 

     return false;

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



  // Nota:

  //  Tipo    = # entonces no realizar ningun tipo de validacion.

  //  Caracter= # entonces no realizar ningun justificado de caracterez.

  function ValidarObjeto( Valor, Tipo ) {

    var Correcto= false;

    switch(Tipo) {

      case 'N': // Cadena de tipo numero entero

        Correcto = isInteger( Valor );

        promptStr= "Error: \nNúmero entero incorrecto "+ Valor; 

        break;

      case 'R': // Cadena de tipo numero real o de punto flotante

        Correcto = isFloat( Valor );

        promptStr= "Error: \nNúmero real incorrecto "+ Valor; 

        break;

      case 'C': // Cadena de tipo alfabetica

        Correcto = isAlphabetic( Valor );

        promptStr= "Error: \nCadena de caracteres alfabetica incorrecta "+ Valor; 

        break;

      case 'A': // Cadena de tipo alfanumerica

        Correcto = isAlphanumeric( Valor );

        promptStr= "Error: \nCadena de caracteres alfanumérica incorrecta "+ Valor; 

        break;

      case 'F': // Cadena de tipo fecha con formato AAAAMMDD

        Correcto = isDate( Valor.substr(0,4), Valor.substr(4,2), Valor.substr(6,2) );

        promptStr= "Error: \nLa fecha es incorrecta "+ Valor.substr(0,4)+ "/"+ Valor.substr(4,2)+ "/"+ Valor.substr(6,2); 

        break;

      case 'E': // Cadena  de tipo correo electronico

        Correcto = isEmail( Valor );

        promptStr= "Error: \nEl correo electrónico es incorrecto "+ Valor; 

        break;

      case 'L': // Cadena  de tipo correo electronico

        Correcto = isLogin( Valor );

        promptStr= "Error: \nEl login es incorrecto "+ Valor; 

        break;

      case '#': // Tipo de validación nula

        promptStr= "No realizar ninguna validación"; 

        break;      

      default : // Casos no considerados 

        alert( "Validar \nTexto: "+ Valor+ "\nCaso no conciderado. !" );

   };

   return Correcto;

  }

  

  function SepararObjeto ( Objeto ) {

    var Tokens= Objeto.name.split("_");

//    alert(    "Nombre: "+ Objeto.name+ 

//          "\nRegistro: "+ Tokens[REGISTRO]+ 

//           "\nColumna: "+ Tokens[COLUMNA]+

//           "\nValidar: "+ Tokens[VALIDAR]+

//        "\nJustificar: "+ Tokens[JUSTIFICAR]+

//             "\nLugar: "+ Tokens[LUGAR]+

//          "\nLongitud: "+ Tokens[LONGITUD]+

//             "\nValor: "+ Objeto.value );    

    Objeto.value= stripWhitespace(Objeto.value);

    var Ok= ValidarObjeto( Objeto.value, Tokens[VALIDAR] );

    if( Ok )

      Objeto.value= Justificar( Objeto.value, Tokens[JUSTIFICAR], Tokens[LONGITUD], Tokens[LUGAR] );

    else {

      promptStatus( promptStr );

      alert( promptStr+ "\najg." );

      Objeto.focus();

    };

    return Ok;

  }



  function Recorrer ( Formulario ) {

    for(var x=0; x< Formulario.elements.length- 1; x++) {

      if(Formulario.elements[x].type== "text") {

        if(!SepararObjeto(Formulario.elements[x])) 

          return false;

      }    

    };  

    alert("&iexcl; Formulario correcto. !");

    return true;

  } 



  function MandarFormulario(Formulario) {

//    alert("Página: "+ window.location);

//    Formulario.action=window.location;

    Formulario.action="conceptosarea.jsp?Opcion=<%=Opcion%>";

    Formulario.submit();

  }

  

