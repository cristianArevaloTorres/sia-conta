/// funciones para la pagina de PARTIDAS SELECCIONADAS
function checaDiferencia()
      {
      elemento="form1:table2:otPsSum18";   
      totDif=parseFloat(eliminacomas(document.getElementById(elemento).innerHTML));
            if (totDif==0)
            {
                            
                for ( i=0; i<numFilasPs; i++)
                    {
                        for (j=5; j<17; j++)
                        {
                            elemento="form1:table2:"+i+":otPsCol"+[j]; 
                            valor = parseFloat(eliminacomas(document.form1.elements[elemento].value));
                            document.form1.elements[elemento].value = valor;
                        }
                    }       
            }
            else
            {
                alert("No se puede actualizar calendario porque existen diferencias !!");
                return false;
            }
        }
// funciones establecidas para la pagina PARTIDAS INDIVIDUALES        
        function checaDiferencia1()  //Verifica que la diferencia sea 0, si es cero elimina las comas de los valores
        {
                totDif=0;
                for ( i=0; i < numFilasPi; i=i+4)
                {
                    elemento="form1:table1:"+i+":diferencia";
                    dif=document.getElementById(elemento).innerHTML;
                    dif=parseFloat(eliminacomas(dif));
                    totDif=totDif+dif;
                    
                }
            if (totDif!=0)
            {
                alert("No se puede actualizar calendario porque existen diferencias !!");
                return false;            
            }
            else
            {
                
                for ( i=0; i < numFilasPi; i=i+4)
                    {
                        for (j=0; j<12; j++)
                        {
                            elemento="form1:table1:"+i+":"+mes[j];   
                            valor = parseFloat(eliminacomas(document.form1.elements[elemento].value));
                            document.form1.elements[elemento].value = valor;
                        }
                    }
                
                return true;
            }    
        }
        
        function operacion(tis)
        {
            valor = tis.value;
            valor=eliminacomas(valor);
            valor1=valor.split("-");
            valor2=valor.split("+");
            lon1=valor1.length;
            lon2=valor2.length;
            if((lon1>1) && (lon2>1))
            {
                alert("operacin no valida");
                tis.value=0;
            }
            else
            {
                if(lon1==2)
                {
                    if((valor1[0]==''))
                    valor1[0]='0';                    
                    if((valor1[1]==''))
                    valor1[1]='0';
                    
                     res=(parseInt(valor1[0])-parseInt(valor1[1]));
                     tis.value=res;
                     if(res<0)
                     {
                       tis.value=valor1[0];
                     }
                   
                }  
                
                
                
                if(lon2==2)
                {
                    if((valor2[0]==''))
                    valor2[0]='0';
                    if((valor2[1]==''))
                    valor2[1]='0';
                    
                    res=(parseInt(valor2[0])+parseInt(valor2[1]));
                    tis.value=res;
                }
                
            }
            
        }
        
        function LP_data()
        {   //
        
            var key=window.event.keyCode;//codigo de tecla. 
            if (key < 48 || key > 57)
            {//si no es numero  
                if (key!=43 && key!=45)
                {   //si es '+' o '-'
                window.event.keyCode=0;//anula la entrada de texto. 
                }   
            }
        }
        
        function validaObservaciones()
        {
                elemento="form1:itObserva";
                observa=document.form1.elements[elemento].value;
                if(observa=="")
                {    alert('Debe introucir observaciones');
                    return false;}
                else
                { 
                    return true;}
        }
        
        function concluirPi()
        {
            if((validaObservaciones())&&(checaDiferencia1()))
                return true;
            else
                return false;
        }

        function concluirPs()
        {
            if((validaObservaciones())&&(checaDiferencia()))
                return true;
            else
                return false;
        }
        
        
        
        //*******ajustar seleccionadas *****//////
        
        function convertir(idtabla,titulos,iniciocolumna,fincolumna){
            aux=""+titulos+"";
            for(i=iniciocolumna;fincolumna>=i;i++){
                  aux=aux+parseInt(i);  
                  elemento=idtabla+":"+aux;
                  contenidoTitulo=document.getElementById(elemento).innerHTML;
                  texto=contenidoTitulo.substring(0,1);
                  auxtexto=contenidoTitulo.substring(1);
                  auxtexminus=auxtexto.toLowerCase();
                  textoformat=texto+auxtexminus;
                  document.getElementById(elemento).innerHTML=textoformat;
                  aux=""+titulos+"";
            }
        }
        
        function LP_data()
        {   //
        
            var key=window.event.keyCode;//codigo de tecla. 
            if (key &lt; 48 || key > 57)
            {//si no es numero  
                if (key!=43 &amp;&amp; key!=45)
                {   //si es '+' o '-'
                window.event.keyCode=0;//anula la entrada de texto. 
                }   
            }
        }
      
      function poneComas(_a)
        {   
            _a=eliminacomas(_a);   
            var _ini=_a.toString().split(".");   
            //alert("entero: "+_ini[0]);   
            //alert("fraccion: "+_ini[1]);   
            var _b=_ini[0].toString().split("");   
            var _c="";   var _d=0;   
            for(var i=_b.length-1;i>=0;i--)
            {     
                _d++;         
                _c=_b[i]+_c;     
                //alert("valor: "+_c);     
                if(_d%3==0)
                {        
                    if((i!=0))
                    {        
                        if((_b[i-1]!="-"))
                        _c=","+_c;        
                    }      
                }    
            }   
            if(_ini[1]!=null)  
            return(_c+"."+_ini[1]);  
            else  
            return(_c); 
        }

        function eliminacomas(valor){//elimina las comas de una cantidad 
            var numero= "" ;
            len= valor.length; 
            for(x=0;len>x;++x)
            { 
                if(valor.charAt(x)!=",")
                {
                    numero=numero+valor.charAt(x);
                } 
            } 
                return numero; 
        }
    
  
       function traeNumerico(camp){   //Obtiene el número del nombre del campo
          numero="";
          for (i=0;camp.length>i;i++){
            if(!isNaN(camp.substring(i,i+1))){
              numero=""+numero+""+camp.substring(i,i+1);                   
            }                
          }
          return numero;            
        }
    
    
        function traeNombre(camp)
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
     
        
      var mes=new Array("enero","febrero","marzo","abril","mayo","junio","julio","agosto","septiembre","octubre","noviembre","diciembre");
    var numFilasPs;
    
    
    function load()
    {
        
        
    }
    
    function principal()
    {
        numFilasPs=document.forms['form1'].elements['form1:ihPartidasSeleccionadas'].value;        
        //alert(numFilasPs);
        filaPrincipal=(-1);
        var mes=new Array("enero","febrero","marzo","abril","mayo","junio","julio","agosto","septiembre","octubre","noviembre","diciembre");
        nombreCols="otPsCol";
        nombreSums="otPsSum"  
            
            suma();
            //alert("suma");
            resta();
            //alert("resta");
            sumarCols();
            //alert("sumarCols");
            mensual();
            trimestral();
            semestral();
            ponerComasPS();
            convertir("form1:table3","titulo_",1,13);
    }
    
    function sumarCols()
    {
        var elemento;
        //alert("entra sumarCols");
        for (i=1; 18>=i; i++)
        {
            valor=0;
            if((5>i) || (i>16))
            {
                for (j=0; numFilasPs>j; j++)
                {
                    elemento="form1:table2:"+j+":"+nombreCols+i;
                    //alert("elemento: "+elemento);
                    valor=parseInt(valor)+parseInt(eliminacomas(document.getElementById(elemento).innerHTML));
                    elemento="form1:table2:"+nombreSums+i;
                    //alert(elemento);
                }
               // alert("elemento ="+elemento);
                document.getElementById(elemento).innerHTML=valor;
            }
            else
            {
                for (j=0; numFilasPs>j; j++)
                {
                    elemento="form1:table2:"+j+":"+nombreCols+i;
                    //alert(document.form1.elements[elemento].value);
                    //alert("elemento: "+elemento);
                    valor=parseInt(valor)+parseInt(eliminacomas(document.form1.elements[elemento].value));
                    elemento="form1:table2:"+nombreSums+i;
                    //alert("valor: "+valor);
                    
                }
                document.getElementById(elemento).innerHTML=valor;
            }
        }
    }
    
    var PsSumaNom;
    var PsEstaNom;
    var PsParTableNom;
    var PsEstTableNom;
    var montoPef;
    
    function mensual()
    {
        PsPefNom="otPsSum4";
        PsSumaNom="otPsSum";
        PsEstaNom="otPsEst_";
        PsParTableNom="table2";
        PsEstTableNom="table3";
        elemento="form1:"+PsParTableNom+":"+PsPefNom;
        montoPef=document.getElementById(elemento).innerHTML;
        j=2;
        for(i=5; 16>=i; i++)
        {
            elemento="form1:"+PsParTableNom+":"+PsSumaNom+i;
            //alert("suma: "+elemento);
            valor=document.getElementById(elemento).innerHTML;
            valor=(parseFloat(eliminacomas(valor))*100)/parseFloat(eliminacomas(montoPef));
            elemento="form1:"+PsEstTableNom+":0:"+PsEstaNom+j;
            //alert("estacionalidad: "+ elemento);
            document.getElementById(elemento).innerHTML=Math.round(valor*100)/100;
            j++;
        }
    }
    
    function trimestral()
    {
        x=2;
        for(i=4;13>=i;i=i+3)
        {
            valor=0;            
            for(j=0;3>j;j++)
            {
                elemento="form1:"+PsEstTableNom+":0:"+PsEstaNom+x;
                valor=parseFloat(valor)+parseFloat(document.getElementById(elemento).innerHTML);
                x++;
            }
            elemento="form1:"+PsEstTableNom+":1:"+PsEstaNom+i;
            document.getElementById(elemento).innerHTML=Math.round(valor*100)/100;
        }
    }
    
    function semestral()
    {
        x=2;
        for(i=7;13>=i;i=i+6)
        {
            valor=0;            
            for(j=0;6>j;j++)
            {
                elemento="form1:"+PsEstTableNom+":0:"+PsEstaNom+x;
                valor=parseFloat(valor)+parseFloat(document.getElementById(elemento).innerHTML);
                x++;
            }
            elemento="form1:"+PsEstTableNom+":2:"+PsEstaNom+i;
            document.getElementById(elemento).innerHTML=Math.round(valor*100)/100;
        }
    }
    
    function suma()
    {
        for(i=0;numFilasPs>i;i++)
        {
            valor=0;
            for(j=5;16>=j;j++)
            {
                elemento="form1:table2:"+i+":"+nombreCols+j;
                valor=parseInt(valor)+parseInt(eliminacomas(document.form1.elements[elemento].value));
            }
            elemento="form1:table2:"+i+":"+nombreCols+17;
            document.getElementById(elemento).innerHTML=valor;
        }
    }
    
    function resta()
    {
         for(i=0;numFilasPs>i;i++)
         {
            elemento1="form1:table2:"+i+":"+nombreCols+4;
            elemento1=document.getElementById(elemento1).innerHTML;
            elemento2="form1:table2:"+i+":"+nombreCols+17;
            elemento2=document.getElementById(elemento2).innerHTML;
            valor=parseInt(eliminacomas(elemento1))-parseInt(eliminacomas(elemento2))
            elemento="form1:table2:"+i+":"+nombreCols+18;
            document.getElementById(elemento).innerHTML=valor;
         }
    }
    
    function ponerComasPS()
    {
        for (i=1; 18>=i; i++)
        {
            valor=0;
            if((5>i) || (i>16))
            {
                for (j=0; numFilasPs>j; j++)
                {
                    elemento="form1:table2:"+j+":"+nombreCols+i;
                    //alert("elemento: "+elemento);
                    document.getElementById(elemento).innerHTML=poneComas(document.getElementById(elemento).innerHTML);
                    elemento="form1:table2:"+nombreSums+i;                    
                }
                document.getElementById(elemento).innerHTML=poneComas(document.getElementById(elemento).innerHTML);
            }
            else
            {
                for (j=0; numFilasPs>j; j++)
                {
                    elemento="form1:table2:"+j+":"+nombreCols+i;
                    //alert(document.form1.elements[elemento].value);
                    //alert("elemento: "+elemento);
                    document.form1.elements[elemento].value=poneComas(document.form1.elements[elemento].value);
                    elemento="form1:table2:"+nombreSums+i;
                    //alert("valor: "+valor);
                    
                }
                document.getElementById(elemento).innerHTML=poneComas(document.getElementById(elemento).innerHTML);
            }
        }
    }
    
    function convertir(idtabla,titulos,iniciocolumna,fincolumna){
            aux=""+titulos+"";
            for(i=iniciocolumna;fincolumna>=i;i++){
                  aux=aux+parseInt(i);  
                  elemento=idtabla+":"+aux;
                  //alert("entre");
                  contenidoTitulo=document.getElementById(elemento).innerHTML;
                  texto=contenidoTitulo.substring(0,1);
                  auxtexto=contenidoTitulo.substring(1);
                  auxtexminus=auxtexto.toLowerCase();
                  textoformat=texto+auxtexminus;
                  document.getElementById(elemento).innerHTML=textoformat;
                  aux=""+titulos+"";
            }
        }
        
        function LP_data()
        {   //
        
            var key=window.event.keyCode;//codigo de tecla. 
            if (key &lt; 48 || key > 57)
            {//si no es numero  
                if (key!=43 &amp;&amp; key!=45)
                {   //si es '+' o '-'
                window.event.keyCode=0;//anula la entrada de texto. 
                }   
            }
        }
     
      
      function poneComas(_a)
        {   
            _a=eliminacomas(_a);   
            var _ini=_a.toString().split(".");   
            //alert("entero: "+_ini[0]);   
            //alert("fraccion: "+_ini[1]);   
            var _b=_ini[0].toString().split("");   
            var _c="";   var _d=0;   
            for(var i=_b.length-1;i>=0;i--)
            {     
                _d++;         
                _c=_b[i]+_c;     
                //alert("valor: "+_c);     
                if(_d%3==0)
                {        
                    if((i!=0))
                    {        
                        if((_b[i-1]!="-"))
                        _c=","+_c;        
                    }      
                }    
            }   
            if(_ini[1]!=null)  
            return(_c+"."+_ini[1]);  
            else  
            return(_c); 
        }

        function eliminacomas(valor){//elimina las comas de una cantidad 
            var numero= "" ;
            len= valor.length; 
            for(x=0;len>x;++x)
            { 
                if(valor.charAt(x)!=",")
                {
                    numero=numero+valor.charAt(x);
                } 
            } 
                return numero; 
        }
    
  
       function traeNumerico(camp){   //Obtiene el número del nombre del campo
          numero="";
          for (i=0;camp.length>i;i++){
            if(!isNaN(camp.substring(i,i+1))){
              numero=""+numero+""+camp.substring(i,i+1);                   
            }                
          }
          return numero;            
        }
    
    
        function traeNombre(camp)
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
      
        
        
      var mes=new Array("enero","febrero","marzo","abril","mayo","junio","julio","agosto","septiembre","octubre","noviembre","diciembre");
    var numFilasPs;
    
    
    function load()
    {
        
        
    }
    
    function principal()
    {
        numFilasPs=document.forms['form1'].elements['form1:ihPartidasSeleccionadas'].value;        
        alert(numFilasPs);
        filaPrincipal=(-1);
        var mes=new Array("enero","febrero","marzo","abril","mayo","junio","julio","agosto","septiembre","octubre","noviembre","diciembre");
        nombreCols="otPsCol";
        nombreSums="otPsSum"  
            alert("antes de la suma")
            suma();
            alert("suma");
            resta();
            alert("resta");
            sumarCols();
            alert("sumarCols");
            mensual();
            trimestral();
            semestral();
            ponerComasPS();
            convertir("form1:table3","titulo_",1,13);
    }
    
    function sumarCols()
    {
        var elemento;
        //alert("entra sumarCols");
        for (i=1; 18>=i; i++)
        {
            valor=0;
            if((5>i) || (i>16))
            {
                for (j=0; numFilasPs>j; j++)
                {
                    elemento="form1:table2:"+j+":"+nombreCols+i;
                    //alert("elemento: "+elemento);
                    valor=parseInt(valor)+parseInt(eliminacomas(document.getElementById(elemento).innerHTML));
                    elemento="form1:table2:"+nombreSums+i;
                    //alert(elemento);
                }
               // alert("elemento ="+elemento);
                document.getElementById(elemento).innerHTML=valor;
            }
            else
            {
                for (j=0; numFilasPs>j; j++)
                {
                    elemento="form1:table2:"+j+":"+nombreCols+i;
                    //alert(document.form1.elements[elemento].value);
                    //alert("elemento: "+elemento);
                    valor=parseInt(valor)+parseInt(eliminacomas(document.form1.elements[elemento].value));
                    elemento="form1:table2:"+nombreSums+i;
                    //alert("valor: "+valor);
                    
                }
                document.getElementById(elemento).innerHTML=valor;
            }
        }
    }
    
    var PsSumaNom;
    var PsEstaNom;
    var PsParTableNom;
    var PsEstTableNom;
    var montoPef;
    
    function mensual()
    {
        PsPefNom="otPsSum4";
        PsSumaNom="otPsSum";
        PsEstaNom="otPsEst_";
        PsParTableNom="table2";
        PsEstTableNom="table3";
        elemento="form1:"+PsParTableNom+":"+PsPefNom;
        montoPef=document.getElementById(elemento).innerHTML;
        j=2;
        for(i=5; 16>=i; i++)
        {
            elemento="form1:"+PsParTableNom+":"+PsSumaNom+i;
            //alert("suma: "+elemento);
            valor=document.getElementById(elemento).innerHTML;
            valor=(parseFloat(eliminacomas(valor))*100)/parseFloat(eliminacomas(montoPef));
            elemento="form1:"+PsEstTableNom+":0:"+PsEstaNom+j;
            //alert("estacionalidad: "+ elemento);
            document.getElementById(elemento).innerHTML=Math.round(valor*100)/100;
            j++;
        }
    }
    
    function trimestral()
    {
        x=2;
        for(i=4;13>=i;i=i+3)
        {
            valor=0;            
            for(j=0;3>j;j++)
            {
                elemento="form1:"+PsEstTableNom+":0:"+PsEstaNom+x;
                valor=parseFloat(valor)+parseFloat(document.getElementById(elemento).innerHTML);
                x++;
            }
            elemento="form1:"+PsEstTableNom+":1:"+PsEstaNom+i;
            document.getElementById(elemento).innerHTML=Math.round(valor*100)/100;
        }
    }
    
    function semestral()
    {
        x=2;
        for(i=7;13>=i;i=i+6)
        {
            valor=0;            
            for(j=0;6>j;j++)
            {
                elemento="form1:"+PsEstTableNom+":0:"+PsEstaNom+x;
                valor=parseFloat(valor)+parseFloat(document.getElementById(elemento).innerHTML);
                x++;
            }
            elemento="form1:"+PsEstTableNom+":2:"+PsEstaNom+i;
            document.getElementById(elemento).innerHTML=Math.round(valor*100)/100;
        }
    }
    
    function suma()
    {
        for(i=0;numFilasPs>i;i++)
        {
            valor=0;
            for(j=5;16>=j;j++)
            {
                elemento="form1:table2:"+i+":"+nombreCols+j;
                valor=parseInt(valor)+parseInt(eliminacomas(document.form1.elements[elemento].value));
            }
            elemento="form1:table2:"+i+":"+nombreCols+17;
            document.getElementById(elemento).innerHTML=valor;
        }
    }
    
    function resta()
    {
         for(i=0;numFilasPs>i;i++)
         {
            elemento1="form1:table2:"+i+":"+nombreCols+4;
            elemento1=document.getElementById(elemento1).innerHTML;
            elemento2="form1:table2:"+i+":"+nombreCols+17;
            elemento2=document.getElementById(elemento2).innerHTML;
            valor=parseInt(eliminacomas(elemento1))-parseInt(eliminacomas(elemento2))
            elemento="form1:table2:"+i+":"+nombreCols+18;
            document.getElementById(elemento).innerHTML=valor;
         }
    }
    
    function ponerComasPS()
    {
        for (i=1; 18>=i; i++)
        {
            valor=0;
            if((5>i) || (i>16))
            {
                for (j=0; numFilasPs>j; j++)
                {
                    elemento="form1:table2:"+j+":"+nombreCols+i;
                    //alert("elemento: "+elemento);
                    document.getElementById(elemento).innerHTML=poneComas(document.getElementById(elemento).innerHTML);
                    elemento="form1:table2:"+nombreSums+i;                    
                }
                document.getElementById(elemento).innerHTML=poneComas(document.getElementById(elemento).innerHTML);
            }
            else
            {
                for (j=0; numFilasPs>j; j++)
                {
                    elemento="form1:table2:"+j+":"+nombreCols+i;
                    //alert(document.form1.elements[elemento].value);
                    //alert("elemento: "+elemento);
                    document.form1.elements[elemento].value=poneComas(document.form1.elements[elemento].value);
                    elemento="form1:table2:"+nombreSums+i;
                    //alert("valor: "+valor);
                    
                }
                document.getElementById(elemento).innerHTML=poneComas(document.getElementById(elemento).innerHTML);
            }
        }
    }
    
    
    