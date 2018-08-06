/* --------------------------------------------------------
    Nombre : fcgencValida.js
    Descripción : Función de validación para elementos en HTML
    Fecha de creación : 15 de Enero del 2004
    Autor : SIA
    Autor Última Modificación : Jesús Urraca
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
    return ( ((c >= "a") && (c <= "z")) || ((c >= "A") && (c <= "Z")) || c==" " )
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
   var defaultEmptyOK=false;
   var i;
   if (isEmpty(s)) 
     if (isAlphabetic.arguments.length == 1) 
       return defaultEmptyOK;
     else 
       return (isAlphabetic.arguments[1] == true);
   for (i = 0; i < s.length; i++) {   
     var c = s.charAt(i);
     if (!isLetter(c) && (c != ".") && (c != " ")) // Se agrego la validación de punto y espacio. 10/oct/2006 Martha Luna.
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
   
   if (s <0) s = (-1) * s;   
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
       daysInMonth[1] = 31; daysInMonth[2] = 29; 
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

// Reemplaza los valores no permitidos para ORACLE por nulo
function reemplazaValor(string) {
      if (string=="") string="";
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
function validaTxa(Valor,totalcaracteres) {
  Valor = reemplazaValor(Valor);
  Valor = trim(Valor); 
  Valor2=Valor.substring(0,totalcaracteres);
  return Valor2;   
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
        break;
      case 'R': // Cadena de tipo numero real o de punto flotante
        Correcto = isFloat(Valor);
        break;
      case 'C': // Cadena de tipo alfabetica
        Correcto = isAlphabetic(Valor);
        break;
      case 'A': // Cadena de tipo alfanumerica
        Correcto = isAlphanumeric( Valor );
        break;
      case 'F': // Cadena de tipo fecha con formato DD/MM/AAAA ó D/M/AAAA
                var posicion01=0, posicion02=0;
                cadena01=Valor;
                longitud=cadena01.length;
                posicion01=cadena01.indexOf("/");  
                cadena02=cadena01.substring(posicion01+1,longitud);
                posicion02=cadena02.indexOf("/");  
                dia=cadena01.substring(0,posicion01);
                mes=cadena02.substring(0,posicion02);
                anio=cadena02.substring(posicion02+1,longitud);
                Correcto=isDate(dia,mes,anio);
                break;
      case 'E': // Cadena  de tipo correo electronico
        Correcto = isEmail( Valor );
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
 
/* convierte fecha a formato aaaa.mmdd para 
   poder condicionar dos fechas             */
function convierte(cadena01) {
  var dia,mes,anio;
  var posicion01=0, posicion02=0;
  longitud=cadena01.length;
  posicion01=cadena01.indexOf("/");  
  cadena02=cadena01.substring(posicion01+1,longitud);
  posicion02=cadena02.indexOf("/");  
  dia=cadena01.substring(0,posicion01);
  mes=cadena02.substring(0,posicion02);
  anio=cadena02.substring(posicion02+1,longitud);
  dia = dia / 10000;
  mes = mes / 100;
  date=anio - - mes - - dia;
  return date;
}

//Quita Enter de Textarea
function cambiaEnter(cadena) {
  var newcadena="";
  longitud=cadena.length;
  for (i=0; i<longitud; i++ ) {
     caracter=cadena.charAt(i);
	 resultado=escape(caracter);
     //alert(resultado+" : "+typeof(resultado));
	 if (resultado=="%0D" || resultado=="%0A") newcadena=newcadena+" ";
     else newcadena=newcadena+caracter;
  }
  newcadena=validaTxa(newcadena);
  return newcadena;
}

/* Valida formato hora hh:mm
   hh de 0,00,1,01,2,02, hasta 24
   m de 0 hasta 5 y de 0 hasta 9     */
function validaHora(hora)
{
        var er_fh = /^(1|01|2|02|3|03|4|04|5|05|6|06|7|07|8|08|9|09|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24|00)\:([0-5]0|[0-5][1-9])$/
        if (!(er_fh.test(hora)))  return false
        return true
}

/* ------------------------------------------
   Función que redondea una cifra a 2 dígitos
   num = número a redondear
   ------------------------------------------ */
function redondear(numero) {
  var original=parseFloat(numero);
  var result=Math.round(original*1000)/1000 ;
return result;
}

/* ------------------------------------------
   Función que valida onKeyPress

   ------------------------------------------ */
function validaTxt(event,tipo,elemento,longi){
/* key code 
39: comilla doble
34: comilla simple 
13: enter 
45: guion
32: espacio
45: guion simple
95: guion bajo
38: ampersand */

var res=true;
if (tipo==1) {//textArea
    if (event.keyCode == 39 || event.keyCode == 34 || event.keyCode ==13 || (limitaTxa(elemento.value,longi-1)==false)) { 
        alert('Caracter no válido');
        event.returnValue=false;
        }
};
if (tipo==2){//input type="text"
    if (event.keyCode == 39 || event.keyCode == 34 || event.keyCode ==13) {
        alert('Caracter no válido');
        event.returnValue = false;
        res=false;
        }
};

if (tipo==3){//rfc
    //alert('Codigo de tecla pulsada: '+event.keyCode);
    if (!((event.keyCode > 47 && event.keyCode < 58) || //numero
         (event.keyCode > 64 && event.keyCode < 91) || //mayuscula
         (event.keyCode > 96 && event.keyCode < 123) || // minuscula
         (event.keyCode == 38))) // ampersand
    {   //alert('Caracter no válido');
        event.returnValue = false;
        res=false;
        }
}
if (tipo==4){//numerico
    //alert('Codigo de tecla pulsada: '+event.keyCode);
    if (!(event.keyCode > 47 && event.keyCode < 58)  ){
        alert('Sólo números');
        event.returnValue = false;
        res=false;
        }
}

if (tipo==7){//numerico con decimales
    //alert('Codigo de tecla pulsada: '+event.keyCode);
    if (!( (event.keyCode > 47 && event.keyCode < 58) || event.keyCode ==46 )){
        alert('Sólo números');
        event.returnValue = false;
        res=false;
        }
}
}