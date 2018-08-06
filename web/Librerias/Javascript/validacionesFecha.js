  function esDigito(sChr){
  var sCod = sChr.charCodeAt(0);
  return ((sCod > 47) && (sCod < 58));
  }
  
  function valSep(oTxt){
  var bOk = false;
  bOk = bOk || ((oTxt.value.charAt(2) == "-") && (oTxt.value.charAt(5) == "-"));
  bOk = bOk || ((oTxt.value.charAt(2) == "/") && (oTxt.value.charAt(5) == "/"));
  return bOk;
  }
  
  function finMes(oTxt){
  var nMes = parseInt(oTxt.value.substr(3, 2), 10);
  var nRes = 0;
  switch (nMes){
  case 1: nRes = 31; break;
  case 2: nRes = 29; break;
  case 3: nRes = 31; break;
  case 4: nRes = 30; break;
  case 5: nRes = 31; break;
  case 6: nRes = 30; break;
  case 7: nRes = 31; break;
  case 8: nRes = 31; break;
  case 9: nRes = 30; break;
  case 10: nRes = 31; break;
  case 11: nRes = 30; break;
  case 12: nRes = 31; break;
  }
  return nRes;
  }
  
  function valDia(oTxt){
  var bOk = false;
  var nDia = parseInt(oTxt.value.substr(0, 2), 10);
  bOk = bOk || ((nDia >= 1) && (nDia <= finMes(oTxt)));
  return bOk;
  }
  
  function valMes(oTxt){
  var bOk = false;
  var nMes = parseInt(oTxt.value.substr(3, 2), 10);
  bOk = bOk || ((nMes >= 1) && (nMes <= 12));
  return bOk;
  }
  
  function valAno(oTxt){
  var bOk = true;
  var nAno = oTxt.value.substr(6);
  bOk = bOk && ((nAno.length == 2) || (nAno.length == 4));
  if (bOk){
  for (var i = 0; i < nAno.length; i++){
  bOk = bOk && esDigito(nAno.charAt(i));
  }
  }
  return bOk;
  }
  
  function valFecha(oTxt){
    var bOk = true;
    if (oTxt.value != ""){
      bOk = bOk && (valAno(oTxt));
      bOk = bOk && (valMes(oTxt));
      bOk = bOk && (valDia(oTxt));
      bOk = bOk && (valSep(oTxt));
      if (!bOk){
          alert("Fecha inválida");
          oTxt.value = "";
          oTxt.focus();
      }
     }
    return bOk; 
  }
  
  //yyyyMMdd
  function formateaFecha(fecha){
     var auxFecha;
     var anio=fecha.substr(6,4);
     var mes=fecha.substr(3,2);
     var dia=fecha.substr(0,2);
     auxFecha=anio+mes+dia;
     return auxFecha;   
  }
  
  function getDate(valor) {
   return valor.substr(6,4)+ valor.substr(3,2)+ valor.substr(0,2);
  }
  
  function getDateFormateada(valor) {
   return valor.substr(6,2)+"/"+valor.substr(4,2)+"/"+valor.substr(0,4);
  }

  // ******CODIGO GENERICO*********//
  function validaFechaVacia(tis,fechaPropuesta){
    regresa=true;             
      if(tis.value=='')
        if(fechaPropuesta!=null){
          tis.value=fechaPropuesta;
          regresa=true;
        }
        else{
          alert("El campo fecha no puede ser vacio");
          regresa=false;
        }
      return regresa;
    }
    
  // ******CODIGO GENERICO*********//
  function fechaValida(fechaInicio,fechaFin,tis){
    if(validaFechaVacia(tis,null)){
        auxFecha=getDate(tis.value);
        if(valFecha(tis)){
          if(!((auxFecha>=fechaInicio) && (auxFecha<=fechaFin))){
            alert('Fecha fuera de rango');                    
            tis.value=getDateFormateada(fechaInicio);
          }//if
        }
        else{
          tis.value=getDateFormateada(fechaInicio);
          alert("Formato de fecha no valido");
        }
      }//if
      else{
        tis.value=getDateFormateada(fechaInicio);
      }
 }//function fechaValida
 
 function voltearFecha(arreglo){
  return arreglo[2]+arreglo[1]+arreglo[0];
 }
 
 function compararFechas2(strFechaIni, strFechaFin) {
  return strFechaIni<=strFechaFin;
 }
 
 function compararFechaActual(strFechaIni, strFechaAct) {
  return strFechaIni<strFechaAct;
 }
 
 function obtenerFechaToArreglo(componenteFecha){
  return componenteFecha.value.split("/");
 }
 
 function compararFechas(arrayFechaIni,arrayFechaFin){
  regresa=true;
  if(regresa){
      regresa = arrayFechaIni[2]<=arrayFechaFin[2];
      menor = arrayFechaIni[2]<arrayFechaFin[2];
    }
    if(regresa){
      if(!menor){
        regresa = arrayFechaIni[1]<=arrayFechaFin[1];
        menor = arrayFechaIni[1]<arrayFechaFin[1];
      }
    }
    if(regresa){
      if(!menor){
        regresa = arrayFechaIni[0]<=arrayFechaFin[0];
      }
    }
  return regresa;
 }
 
 /****
  recibe las cajas de texto como tal, Object
  formato valido dd/mm/yyyy
 *///
 function validaFIniFFin(componentefIni,componentefFin){
  regresa=false;
  if((valFecha(componentefIni)) || (valFecha(componentefFin))){
    strFechaIni= voltearFecha(obtenerFechaToArreglo(componentefIni));
    strFechaFin= voltearFecha(obtenerFechaToArreglo(componentefFin));
    regresa=compararFechas2(strFechaIni,strFechaFin);
  }
  return regresa; 
  }
  
  function validaMenorAFechaActual(componentefIni,componentefAct){
  regresa=false;
  if((valFecha(componentefIni)) || (valFecha(componentefAct))){
    strFechaIni= voltearFecha(obtenerFechaToArreglo(componentefIni));
    strFechaAct= voltearFecha(obtenerFechaToArreglo(componentefAct));
    regresa=compararFechaActual(strFechaIni,strFechaAct);
  }
  return regresa; 
  
 }