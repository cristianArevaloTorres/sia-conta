    function upperCase(valor){
       valor.value = valor.value.toUpperCase();
    }

    function capitalLetter(valor){
       var todo = valor.value.toLowerCase();
       var primera = todo.substring(0,1).toUpperCase();
       var lodemas = todo.substring(1);
       valor.value = primera + lodemas;
    }
    
   function convierteOracion(valor){
      var todo = document.getElementById(valor).innerHTML.toLowerCase();
      var iPosicion = 0;
      var primera = todo.substring(0,1).toUpperCase();
      iPosicion = todo.indexOf(" ");
      if (iPosicion != -1){
          var lodemas = todo.substring(1,iPosicion);
          var primero = primera + lodemas;
          var otraCadena = "";
          todo = todo.substring(iPosicion + 1);
          do {
            iPosicion = todo.indexOf(" ");
              May = todo.substring(0,1).toUpperCase();
              if (iPosicion != -1){
              lodemas = todo.substring(1,iPosicion);
              }
              else{
              lodemas = todo.substring(1);
              }
              otraCadena = otraCadena +' '+ May + lodemas;
              todo = todo.substring(iPosicion + 1);
          }// end while(iPosicion !=-1)
          while (iPosicion != -1)
          document.getElementById(valor).innerHTML = primero+' '+otraCadena;
     }
 }    
     
     function capitalLetter2(valor){
       var todo = document.getElementById(valor).innerHTML.toLowerCase();
       var primera = todo.substring(0,1).toUpperCase();
       var lodemas = todo.substring(1);
       document.getElementById(valor).innerHTML = primera + lodemas;
    }

  
    function validarTeclado(){
            event.returnValue = false;
    }
    
    function validarExpresionregular(regex, cadena) {
        return regex.test(cadena);
    }
          
    function habilitarElemento(nombreElemento, habilitar) {
        document.getElementById(nombreElemento).disabled = habilitar;
    }
  
    function toggle(obj) {
	      var el = document.getElementById(obj);
	      if ( el.style.display != 'none' ) {
		        el.style.display = 'none';
	      }
	      else {
		        el.style.display = '';
	      }
    }    

/*    Array.prototype.inArray = function (value) {
	      var i;
	      for (i=0; i &lt; this.length; i++) {
	          if (this[i] === value) {
		            return true;
		        }
	      }
	      return false;
    };        
*/