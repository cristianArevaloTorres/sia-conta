    function soloFlotantes()   //valida la entrada de números y puntos
    {         
        var key=window.event.keyCode;//codigo de tecla. 
        if (key < 48 || key > 57)
        {//si no es numero  
            if (key!=46) //si es '.'
            {
            window.event.keyCode=0;//anula la entrada de texto. 
            }
        }
    }

    function soloFlotantesOp()
    {   
      var key=window.event.keyCode;//codigo de tecla. 
      if (key < 48 || key > 57)
      {//si no es numero  
        if (key!=43 && key!=45)
        {   //si es '+' o '-'
          if (key!=46) //si es '.'
          {
            window.event.keyCode=0;//anula la entrada de texto. 
          }
        }   
      }
    }

    function traeNumerico(camp){   //Obtiene el número del nombre del campo
        try
        {
            numero="";
            for (i=0;camp.length>i;i++){
                if(!isNaN(camp.substring(i,i+1))){
                      numero=""+numero+""+camp.substring(i,i+1);                   
                }                
            }
            return numero; 
            }
            catch(e)
            {
                alert ("ocurrio un error al traer numerico: "+e.name+"-"+e.message);
            }   
    }
        
    function traeNombre(camp)
    {
        try
        {
            numero="";
              for (i=0;camp.length>i;i++)
              {
                if(isNaN(camp.substring(i,i+1)))
                {
                  numero=""+numero+""+camp.substring(i,i+1);                   
                }                
              }
              return numero;  
        }
        catch(e)
        {
            alert ("ocurrio un error al traer nombre: "+e.name+"-"+e.message);
        }  
    }
    
    
function invalidaTexto()   //valida la entrada de números y puntos
{         
  var key=window.event.keyCode;//codigo de tecla. 
  if (key >= 1 || key <= 256)
  {//si no es numero  
     window.event.keyCode=0;//anula la entrada de texto. 
  }
}