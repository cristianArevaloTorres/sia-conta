var ant;
function over(tis) {
  ant = tis.className;
  tis.className = 'resGrisOscuroOver';
}

function out(tis) {
  tis.className = ant;
}

function selAllJS(tis,nombre,nombreTabla) {
  checks = getElementos(gpn(tis,nombreTabla),nombre);
  inicia = 0;//padre ? 0 : 1;
  for(i = inicia; i < checks.length; i++) {
    if(tis.checked)
      checks[i].checked = true;
    else
      checks[i].checked = false;
  }
}

function selAllSinPadre(tis,nombre) {
  sel(tis,nombre,false);
}

function selAll(tis,nombre,padre) {
  sel(tis,nombre,true);
}

function sel(tis,nombre,padre) {
  checks = document.getElementsByName(nombre);
  inicia = padre ? 0 : 1;
  for(i = inicia; i < checks.length; i++) {
    if(tis.checked)
      checks[i].checked = true;
    else
      checks[i].checked = false;
  }
}

function selTodo(tis,nombre,nombrePadre) {
  var padre = document.getElementById(nombrePadre);
  checks = getElementos(padre,nombre);
  inicia = padre ? 0 : 1;
  for(i = inicia; i < checks.length; i++) {
    if(tis.checked)
      checks[i].checked = true;
    else
      checks[i].checked = false;
  }
}

var arregElimina = new Array();

function eliminarRows(nombreTabla, nombreCheck) {
  tabla = document.getElementById(nombreTabla);
  checks = document.getElementsByName(nombreCheck);
  //alert(checks.length);
  for(i=checks.length-1; i > 0 ; i--) {
    if(checks[i].checked) {
      if(checks[i].value!='' && checks[i].value!='0')
        arregElimina.push(checks[i].value);
      //alert(checks[i].value);
      tabla.deleteRow(checks[i].parentNode.parentNode.rowIndex);
    }
  }
}

function eliminarRowsTabla(nombreTabla, nombreCheck) {
  tabla = document.getElementById(nombreTabla);
  checks = getElementos(tabla,nombreCheck);
  //alert(checks.length);
  for(i=checks.length-1; i >= 0 ; i--) {
    if(checks[i].checked) {
      if(checks[i].value!='' && checks[i].value!='0')
        arregElimina.push(checks[i].value);
      //alert(checks[i].value);
      tabla.deleteRow(checks[i].parentNode.parentNode.rowIndex);
    }
  }
}

function eliminarRow(nombreTabla, objeto) {
  tabla = document.getElementById(nombreTabla);
  if(objeto.value!='' && objeto.value!='0')
    arregElimina.push(objeto.value);
  tabla.deleteRow(objeto.parentNode.parentNode.rowIndex);
}

function limpiaEliminados() {
  arregElimina.splice(0,arregElimina.length);
}

function armaEnvio() {
  return arrayToString(arregElimina);
}

function arrayToString(jsVec) {
  var cad='';
  for(i=0; i < jsVec.length; i++) {
    cad = cad + jsVec[i]+',';
  }
  if(cad.length>0)
    cad = cad.substring(0,cad.length-1);
  return cad;
}

function objsToString(jsVec) {
  var cad='';
  for(i=0; i < jsVec.length; i++) {
    cad = cad + jsVec[i].value + ',';
  }
  if(cad.length>0)
    cad = cad.substring(0,cad.length-1);
  return cad;
}

var tamanio= new Array();
var padrex = new Array();
var ind=-1;

function getObj(objPadre,nombreHijo) {
  ind++;
  padrex[ind] = objPadre; 
  nodos = padrex[ind].childNodes;
  var nodo = objPadre;
  var regresa;
  for(tamanio[ind]=0; tamanio[ind]<nodos.length && nodo.name!=nombreHijo; tamanio[ind]++) {
    if(nodos[tamanio[ind]].nodeName!='#text' || nodos[tamanio[ind]].id==nombreHijo) {
      if(nodos[tamanio[ind]].id==nombreHijo) {
        regresa = nodos[tamanio[ind]]; break;}
      else {
        regresa = getObj(nodos[tamanio[ind]],nombreHijo);
        if(regresa!=undefined && regresa.id==nombreHijo)
          break;
        nodos = padrex[ind].childNodes;
      }
    }
  }
  ind--;
  return regresa;
}

function getElemento(padrex,nombreHijo) {
  var el = getObj(padrex,nombreHijo);
  //alert(el);
  if(el==undefined)
    el = creaTemp();
  return el;
}

function getObjName(objPadre,nombreHijo) {
  ind++;
  padrex[ind] = objPadre; 
  nodos = padrex[ind].childNodes;
  var nodo = objPadre;
  var regresa;
  for(tamanio[ind]=0; tamanio[ind]<nodos.length && nodo.name!=nombreHijo; tamanio[ind]++) {
    if(nodos[tamanio[ind]].nodeName!='#text' || nodos[tamanio[ind]].name==nombreHijo) {
      if(nodos[tamanio[ind]].name==nombreHijo) {
        regresa = nodos[tamanio[ind]]; break;}
      else {
        regresa = getObj(nodos[tamanio[ind]],nombreHijo);
        if(regresa!=undefined && regresa.name==nombreHijo)
          break;
        nodos = padrex[ind].childNodes;
      }
    }
  }
  ind--;
  return regresa;
}

function getEName(padrex,nombreHijo) {
  var el = getObjName(padrex,nombreHijo);
  //alert(el);
  if(el==undefined)
    el = creaTemp();
  return el;
}

var el = new Array();

function getElementos(padrex,nombreHijo) {
  el = new Array();
  getObjs(padrex,nombreHijo);
  //alert(el);
  if(el==undefined)
    el = new Array();
  return el;
}

function getObjs(objPadre,nombreHijo) {
  var nodos = objPadre.childNodes;
  var nodo = objPadre;
  var regresa;
  var i; 
  for(i = 0; i < nodos.length && nodo.name!=nombreHijo; i++) {
    if(nodos[i].nodeName!='#text' || nodos[i].id==nombreHijo) {
      if(nodos[i].id==nombreHijo) {
        el.push(nodos[i]) }
      else {
        getObjs(nodos[i],nombreHijo);
      }
    }
  }
}

function creaTemp() {
  var elemento  = document.createElement("img");
  elemento.src= "";
  elemento.name = "";
  elemento.id   = "";
  return elemento;
}

function duplicar(row,index,nombreTabla) {
  nodes = row.childNodes;
  var tabla = document.getElementById(nombreTabla);
  var fila = tabla.insertRow(index);
  for(i=0; i<nodes.length; i++) {
    fila.appendChild(nodes[i].cloneNode(true));
  }
  return fila;
}


//funcion que duplica elementos hijos en un elemento destino
//table -> table 
//regresa un elemento hijo 
function duplicaObj(objOrigen,objDestino,nombObjRegresa) {
  var nodes = objOrigen.childNodes;
  var regresa;
  for(var i=0; i<nodes.length; i++) {
    var obj = objDestino.appendChild(nodes[i].cloneNode(true));
    if(getElemento(obj,nombObjRegresa).id == nombObjRegresa)
      regresa = getElemento(obj,nombObjRegresa);
  }
  return regresa;
}

function dupObj(objOrigen,objDestino) {
  var nodes = objOrigen.childNodes;
  var regresa;
  for(var i=0; i<nodes.length; i++) {
    var obj = objDestino.appendChild(nodes[i].cloneNode(true));
  }
  return regresa;
}

function gbId(objId) {
  return document.getElementById(objId);
}

function gpn(obj,nombrePadre) {
  var reg;
  if(obj.id!=nombrePadre)
    reg = gpn(obj.parentNode,nombrePadre);
  else
    reg = obj;
  return reg;
}

function pagPagina(tis,emp,tam,nombreTabla) {
  var jstb = gpn(tis,nombreTabla)
  var jstrs = getElementos(jstb,'trPagResultado');
  var jscolor = getElementos(jstb,'pagColorPagina');
  var jscols = jscolor.length / 2;
  var jsini = emp + 1;
  var jsfin = jstrs.length < emp + tam ? jstrs.length : emp + tam;
  var jsPagAct = Math.round((jsfin / tam) * 1)/1;
  for(var i=0; i < jstrs.length; i++) {
    jstrs[i].style.display = 'none';
  }
  for(var i=emp; i < jsfin; i++) {
    jstrs[i].style.display = '';
  }
  for(var i=0; i < jscolor.length; i++) {
    jscolor[i].color = 'white';
  }
  jscolor[jsPagAct-1].color='#19FF05';
  jscolor[jscols-1+jsPagAct].color='#19FF05';
  getElementos(jstb,'pagPregActual')[0].innerHTML = jsini;
  getElementos(jstb,'pagPregActual')[1].innerHTML = jsini;
  getElementos(jstb,'pagRegFin')[0].innerHTML = jsfin;
  getElementos(jstb,'pagRegFin')[1].innerHTML = jsfin;
  getElementos(jstb,'pagPagAct')[0].innerHTML = jsPagAct;
  getElementos(jstb,'pagPagAct')[1].innerHTML = jsPagAct;
}