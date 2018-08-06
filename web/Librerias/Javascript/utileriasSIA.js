

      function Mostrar( obj )
      {
         obj.style.position = "relative";
         obj.style.visibility = "visible";
      }
      
      function Ocultar( obj )
      {
         obj.style.position = "absolute";
         obj.style.visibility = "hidden";
      }    

       function GenerarEvento( obj, evento )
       {
         obj.name = evento;
         obj.click();
       }


   function isFloat ( dato, entero, deci) 
   {
       var strer = "^(\\d+)((\\.(\\d+))*)$";
   
       if ( entero != null && deci != null )
         strer = "^(\\d{1," + entero + "})\\.(\\d{1," + deci + "})$";
   
       var expr = new RegExp( strer, "gi" );
       return expr.test( dato );
   }

   

   function  isPorcentaje(porcentaje) 
   {

      if (isFloat(porcentaje,3,2) || isInteger(porcentaje, 3))
         if (praseFloat( porcentaje ) <= 100)
            return true; 

         return false;  
   }

   function isInteger ( dato, largo ) 
   {
      var strer = "^(\\d+)$";
      
      if ( largo != null )
         strer = "^(\\d{1," + largo + "})$";
      
      var expr = new RegExp( strer, "gi" );
      if (isEmpty(dato)) 
      {
         return false;
      }
      else {
         return( expr.test(dato) );
      }
    }

 

   function isEmail ( dato ) {
      var strer = "^\\D(\\w+)\\.?(\\w+)\\@(\\D+)\\.(\\D+)";
      var expr = new RegExp( strer, "gi" );
      if (isEmpty(dato)) { 
         return false;
          document.datos.usuario.focus();
      }
      else{
        if (expr.test(dato)) //{
          return  true ;
        else
           return false ;
      }
    } 

   function isEmpty(inputStr) 
   {
      if (inputStr == "" || inputStr == null) 
      {
         return true
      }
      return false
   }
   
   
	function FormatoNum (expr, decplaces) 
	{
		var str = "" + Math.round (eval(expr) * Math.pow(10,decplaces))

		while (str.length <= decplaces) 
		{
			str = "0" + str
		}

		var decpoint = str.length - decplaces

		return str.substring(0,decpoint) + "." + str.substring(decpoint,str.length);
	}
  
  function TrimString(sInString) 
  {
    sInString = sInString.replace( /^\s+/g, "" );// strip leading
    return sInString.replace( /\s+$/g, "" );// strip trailing
  }
  
  function formatTxa(obj,caracteres) {
    obj.value = validaTxa(obj.value,caracteres)
  }
  
  function validaTxa(Valor,totalcaracteres) {
    Valor = reemplazaValor(Valor);
    Valor = trim(Valor);
    Valor2=Valor.substring(0,totalcaracteres);
    return Valor2;  
  }
   
  // Reemplaza los valores no permitidos para ORACLE por nulo
  function reemplazaValor(string) {
        if (string=="") string="";
              add="";
              match="|´[<>\\';&\+?]";
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
   
  // Valida que no existan espacios dobles
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
