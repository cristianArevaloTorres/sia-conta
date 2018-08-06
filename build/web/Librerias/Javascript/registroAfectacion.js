 var FechaAfectacion;
 var fechaCierre;
 var txtFechaAfec="frmRegistro:txtFechaAfec";    
 var txtTipoOperacion='frmRegistro:txtTipoOperacion';                       
 var mensaje='--------- ALERTA ---------\n';
 var band=0;
        

function getValor(valor) { 
  var regresar= 0; 
  if(valor!= '' && valor.length!= 0){ 
    regresar= parseFloat(valor);                     
  }  
  return regresar; 
}

function verificarRadio(){
  var objeto;
  var mensaje;
  var encontro=false;
  var i=0;
  var nombreRadio="seleccion";
  while ((i <document.frmRegistro.length) && (encontro==false)){
    objeto=document.frmRegistro.elements[i];
    if(objeto.type=='radio'){ 
      if(objeto.name.indexOf(nombreRadio)>=0){    
       //alert(objeto.name);
       encontro=objeto.checked;
      } 
    }
    i++;
  }
  if(!encontro){
    alert('No existen registros para eliminar');
  }
  return encontro; 
}        

function verificaReferencia(formulario){
  var objeto     = null; 
  var alfaNumerico=true;
  var x          = 0; 
  var nombreReferencia = 'txtReferencia';     
  while((x < formulario.elements.length) && (alfaNumerico)){ 
    objeto= formulario.elements[x]; 
    var texto='';
    if(objeto.type== "text") { 
      if(objeto.name.indexOf(nombreReferencia)>=0 ){                
        texto=objeto.value;              
        alfaNumerico=isAlphanumeric(texto);              
       }
     }           
     x++; 
   }//while
   if(alfaNumerico==false){
     objeto.focus();
   }
   return alfaNumerico;
}

function copiaValor(formulario,obj){
  var encontro=false;
  var tipo=obj.id;
  var indice=tipo.indexOf('txtDebe');    
  var buscaTipoDestino;        
  var totDetalle='frmRegistro:ihTotalDetalleOperacion';
  var x=0;
  var valorOrigen=obj.value;
  var objetoDestino     = null; 
  if(indice!=-1){
    buscaTipoDestino='txtHaber';
  }//if
  else{
    buscaTipoDestino='txtDebe';
  }//else
  if(formulario.elements[totDetalle].value==2){    
    while(x < formulario.elements.length && (encontro==false)){ 
      objetoDestino= formulario.elements[x];     
      if(objetoDestino.type== "text") { 
        if(objetoDestino.name.indexOf(buscaTipoDestino)>=0) {                
          objetoDestino.value=valorOrigen;          
          encontro=true;                    
         }  
        }
        x++;                                      
      }//while
   }//if                   
}//copiaValor;

function limpiaComas(formulario){
  var x=0;
  var objeto=null;
  var nombreDebe = 'txtDebe'; 
  var nombreHaber= 'txtHaber';  
  while(x < formulario.elements.length){ 
    objeto= formulario.elements[x]; 
    if(objeto.type== "text") { 
      if(objeto.name.indexOf(nombreDebe)>=0) {                
         objeto.value=eliminaComas(objeto.value);         
       } // if nombreDebe
       if(objeto.name.indexOf(nombreHaber)>=0) { 
         objeto.value=eliminaComas(objeto.value);  
       } // if nombrehaber                                    
    }//if 
    x++;
  }  
}//limpiaComas


function sumarImportes(formulario) { 
  var nombreDebe = 'txtDebe'; 
  var nombreHaber= 'txtHaber';          
  var sumaDebe   = 0; 
  var sumaHaber  = 0; 
  var x          = 0; 
  var objeto     = null; 
  var cantidadFormateada=0;     
  var numero=0;
  var valor=0;             
  try{
    while(x < formulario.elements.length){ 
      objeto= formulario.elements[x]; 
      if(objeto.type== "text") { 
        if(objeto.name.indexOf(nombreDebe)>=0) {  
          if(!isSignedFloat(eliminaComas(objeto.value))){
            objeto.value=0;                         
         }
         valor=getValor(eliminaComas(objeto.value));
         valor=moneyFormat(valor);               
         valor=parseFloat(valor);
         cuentaCeros= valor;   
         objeto.value=poneComas(valor);
         sumaDebe  += cuentaCeros;                   
     } // if nombreDebe     
     if(objeto.name.indexOf(nombreHaber)>=0) { 
        if(!isSignedFloat(eliminaComas(objeto.value))){
         objeto.value=0;
        
        }
       valor=getValor(eliminaComas(objeto.value)); 
       valor=moneyFormat(valor);
       valor=parseFloat(valor);               
       cuentaCeros= valor;     
       objeto.value=valor;
       objeto.value=poneComas(valor);               
       sumaHaber  += cuentaCeros;                
     } // if nombrehaber                                                            
 
     } // if                        
     x++; 
   } // while                    
   sumaDebe=moneyFormat(Math.round(sumaDebe*Math.pow(10,2))/Math.pow(10,2));
   cantidadFormateada=poneComas(sumaDebe);
   document.getElementById(formulario.name+":totalDebe3").innerHTML='$'+cantidadFormateada;
   sumaHaber=moneyFormat(Math.round(sumaHaber*Math.pow(10,2))/Math.pow(10,2));
   cantidadFormateada=poneComas(sumaHaber);
   document.getElementById(formulario.name+":totalHaber4").innerHTML='$'+cantidadFormateada;   
 }
  catch(e){
    bandera=false;
  }//catch            
} // sumarImportes 



function verificaImporteCheque(totalDebe,totalHaber){
  var coincide=true;
  var importe='frmRegistro:ihImporteCheque';
  var control='frmRegistro:ihControl';
  var valorImporte=0;
  var tiposPol='frmRegistro:ihTipoPoliza';                       
  totalDebe=moneyFormat(totalDebe);
  totalHaber=moneyFormat(totalHaber);        
  if((document.frmRegistro.elements[tiposPol].value==2) && (document.frmRegistro.elements[control].value==2)){
    valorImporte=document.frmRegistro.elements[importe].value;    
    if((valorImporte!=totalDebe)  && (valorImporte!=totalHaber)){
       coincide=false;
      alert('Los importes no coinciden con el importe del cheque, verifíquelos por favor');
    }            
  }        
  return coincide;        
}//verificaImporteCheque



function verificar(formulario){
  var cuentaCeros= -1; 
  var nombreDebe = 'txtDebe'; 
  var nombreHaber= 'txtHaber';          
  var sumaDebe   = 0; 
  var sumaHaber  = 0; 
  var existeDebe = 0; 
  var existeHaber= 0; 
  var x          = 0; 
  var objeto     = null; 
  var cantidadFormateada=0;
  var isAlfaNumerico=true;
  var importeCorrecto=true;
  var numero=0;
  var valor=0;         
  var copiaValor=0;
  var total=0;
  try{
    while(x < formulario.elements.length && cuentaCeros!= 0){ 
     objeto= formulario.elements[x]; 
     if(objeto.type== "text") { 
       if(objeto.name.indexOf(nombreDebe)>=0) {                         
         if(!isSignedFloat(eliminaComas(objeto.value))){
           objeto.value=0;                 
         } 
         valor=getValor(eliminaComas(objeto.value));
         valor=moneyFormat(valor);               
         valor=parseFloat(valor);
         cuentaCeros= valor;                  
         objeto.value=poneComas(valor);
         sumaDebe  = parseFloat(sumaDebe)+ parseFloat(cuentaCeros);
         existeDebe+= 1; 
         total=total+1;
       } // if nombreDebe
       if(objeto.name.indexOf(nombreHaber)>=0) { 
         if(!isSignedFloat(eliminaComas(objeto.value))){
           objeto.value=0;
         }                
         valor=getValor(eliminaComas(objeto.value)); 
         valor=getValor(valor);
         valor=moneyFormat(valor);
         valor=parseFloat(valor);               
         cuentaCeros= valor;     
         objeto.value=valor;               
         objeto.value=poneComas(valor);               
         sumaHaber  = parseFloat(sumaHaber)+ parseFloat(cuentaCeros); 
         existeHaber+= 1; 
         total=total+1;
       } // if nombrehaber                                      
     } // if                        
     x++; 
  } // while          
  if(cuentaCeros== 0) { 
   alert('El importe de una cuenta contable tiene que ser diferente  a cero, verifíquelo por favor. !'); 
   objeto.focus(); 
  }  
  else{ 
   sumaDebe  = moneyFormat(Math.round(sumaDebe*Math.pow(10,2))/Math.pow(10,2));
   sumaHaber = moneyFormat(Math.round(sumaHaber*Math.pow(10,2))/Math.pow(10,2));
   
   if(sumaDebe!= sumaHaber) 
     alert('Los importes no cuadran, verifíquelo por favor. !\nDebe: ['+ sumaDebe+ ']   Haber: ['+ sumaHaber+ ']');
  }  

   //else 
   //  if(existeDebe <= 0) 
       //alert('No existe ninguna cuenta contable que afecte el debe. !'); 
    // else  
     //  if(existeHaber <= 0) 
         //alert('No existe ninguna cuenta contable que afecte el haber. !'); 
      // else  
       // if(sumaDebe== 0 && sumaHaber== 0) 
         // alert('Los importes de las cuenta contables deben ser diferentes de  cero. !\nDebe: ['+ sumaDebe+ ']   Haber: ['+ sumaHaber+ ']');
  
  cantidadFormateada=poneComas(sumaDebe);
  document.getElementById(formulario.name+":totalDebe3").innerHTML='$'+cantidadFormateada;
  cantidadFormateada=poneComas(sumaHaber);
  document.getElementById(formulario.name+":totalHaber4").innerHTML='$'+cantidadFormateada;            
  if(sumaDebe== sumaHaber && existeDebe>0 && existeHaber>0  && cuentaCeros!=0){            
   //isAlfaNumerico=verificaReferencia(formulario); 
   /*if(!isAlfaNumerico){
     if(document.frmRegistro.elements['frmRegistro:ihTipoPoliza'].value!=2){
       alert('La referencia contiene caracteres no permitidos');
     }else{
       alert('El beneficiario contiene caracteres no permitidos'); 
     }  
   }*/
  }
  importeCorrecto=verificaImporteCheque(sumaHaber,sumaDebe);         
 //bandera=sumaDebe==sumaHaber && existeDebe>0 && existeHaber>0  && cuentaCeros!=0  && isAlfaNumerico && importeCorrecto ;       validacion anterior          
   bandera=sumaDebe==sumaHaber && isAlfaNumerico && importeCorrecto  && cuentaCeros!=0 && total>=2;
   if(bandera){
     limpiaComas(formulario); 
   }
 }
  catch(e){
  bandera=false;
  }//catch   
return bandera;         
}


function fechaValida(tis){
  var mesOriginal = null;
  var mesModificado = null;
  var anioOriginal = null;
  var anioModificado = null;
  var tipoControl=1;
  tipoControl = document.frmRegistro.elements['frmRegistro:ihControl'].value;
  if(!valFecha(tis)){               
    if(tis.value!=''){
      tis.value=FechaAfectacion;
      }else{
        tis.value=FechaAfectacion;
      }
  }
  else {
    if(tipoControl==1){         
      if(tis.value!=''){
        if(document.frmRegistro.elements['frmRegistro:ihFechaCierre'].value!=0){
          fechaInicio=document.frmRegistro.elements['frmRegistro:ihFechaCierre'].value;
          auxFecha=formateaFecha(tis.value);                 
          fechaFin=formateaFecha(FechaAfectacion);              
          if(!((auxFecha>=fechaInicio) && (auxFecha<=fechaFin))){
            alert('Fecha fuera de rango, se asignará la fecha de afectación');                    
            tis.value=FechaAfectacion;
           }//if
          }//if
          else{
            tis.value=FechaAfectacion;    
          }  
        }//if
         else{
           tis.value=FechaAfectacion
         }       
     }  
     else {
       mesFecha = document.frmRegistro.elements['frmRegistro:ihFechaPolizaOriginal'].value.split('/');
       mesOriginal = mesFecha[1];
       anioOriginal = mesFecha[2];
       mesFecha  = tis.value.split('/');
       mesModificado = mesFecha[1];
       anioModificado = mesFecha[2];
       if(mesOriginal!= mesModificado || anioModificado != anioOriginal){
          alert('No puedes modificar el mes o año actual de la póliza ');
          tis.value=document.frmRegistro.elements['frmRegistro:ihFechaPolizaOriginal'].value;
        }
     }
   }    
 }//function fechaValida   

 

function existeCuenta(){
  var objeto=null;
  var cuentasDebe="frmRegistro:ihDebe";
  var cuentasHaber="frmRegistro:ihHaber";
  var cuentaContable=document.getElementById('frmRegistro:otCuentaDesc3').innerHTML;
  var objetoDebe=document.frmRegistro.elements[cuentasDebe];
  var objetoHaber=document.frmRegistro.elements[cuentasHaber];
  var verifica=false;                           
  if(objetoDebe!=null){           
    verifica=chechaCuentas(objetoDebe,cuentaContable);                        
  }         
  if((verifica==false) && ( objetoHaber!=null)){           
    verifica=chechaCuentas(objetoHaber,cuentaContable);           
  }         
 return verifica;
}



function validarCuenta(){
  band=0;
  mensaje="";
  // se elimino la validacion si la cuenta ya exist.
  //validarVacio('sorCuentaDesc','tipo operacion');         
 /*if(!verificaCuenta()){
     mensaje=mensaje+'Cuenta invalida,verifiquela por favor..!\n';
     band=1;
   }
   else{*/
    /* if(existeCuenta()){
       mensaje=mensaje+'La cuenta ya existe \n';
       band=1;
       }      */
  //}        
   /*if(band==1){
      alert(mensaje);
   }*/
   return band==1?false:true;
}


function chechaCuentas(objeto,cuentaContable){
 var indice=-1;
 var valor="";
 valor=objeto.value;
 //alert('valor--->'+valor+'cuentaContable'+cuentaContable);
 indice=valor.indexOf(cuentaContable);
 return indice==-1?false:true;
}


function inicial(){
  //FechaAfectacion=document.frmRegistro.elements[txtFechaAfec].value;                        
   FechaAfectacion=document.frmRegistro.elements['frmRegistro:ihFechaAfectacion'].value;
 }    

function verificaActualizacion(){
  var men="Actualización exitosa \n";
  if(document.frmRegistro.elements['frmRegistro:ihUltimo'].value==1){
    men=men+document.frmRegistro.elements['frmRegistro:ihMensaje'].value;                      
    alert(men);
    submitForm('frmRegistro',1,{source:'frmRegistro:clTerminar'});return false;
  }
}       

function eventoFalso(){
    event.returnValue=false;
}

function habilitar(){
  if(document.frmRegistro.elements['frmRegistro:ihControl'].value==1){
     document.frmRegistro.elements[txtFechaAfec].disabled=false;
     document.frmRegistro.elements['frmRegistro:txtReferenciaPoliza'].disabled=false;
     document.frmRegistro.elements['frmRegistro:txtConcepto'].disabled=false;        
  }  
}

function validaTipoOperacion(objeto){//valida solo numeros
  patron=/[^0-1]/; 
  objeto.value=objeto.value.replace(patron,"");         
}

function validarVacio(campo,men){        
  if(document.frmRegistro.elements[campo].value=="") {
    mensaje=mensaje+'Falta capturar el campo '+men+' \n';
    band=1;
  }
}


function validarEnter(e,formulario,objeto){
 var key;          
 if (navigator.appName == "Netscape"){
    key = e.which;
  }
  else{
    key = (typeof event!='undefined')?window.event.keyCode:e.keyCode;
  }          
  if(key == 13){              
    copiaValor(formulario,objeto);
    sumarImportes(formulario);   
  } // end if(key == 13) {
  else {
    if (key < 48 || key > 57)
    e.keyCode=0;
  }
}

  



      


           
     