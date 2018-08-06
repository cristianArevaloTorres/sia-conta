function crearInput(nombre, ide, valor) {
  return crearElemento("input", "text", nombre, ide, valor, false, false);
}

function crearInput2(nombre, ide, valor, soloLectura, disabled) {
  return crearElemento("input", "text", nombre, ide, valor, soloLectura, disabled);
}

function crearA(nombre, ide, valor) {
  return crearElemento2("a", nombre, ide, valor);
}

function crearHidden(nombre, ide, valor) {
  return crearElemento("input", "hidden", nombre, ide, valor, false, false);
}

function crearHidden2(nombre, ide, valor, soloLectura, disabled) {
  return crearElemento("input", "hidden", nombre, ide, valor, soloLectura, disabled);
}

function crearFile(nombre, ide, valor) {
  return crearElemento("input", "file", nombre, ide, valor, false, false);
}

function crearFile2(nombre, ide, valor, soloLectura, disabled) {
  return crearElemento("input", "file", nombre, ide, valor, soloLectura, disabled);
}

function crearCheck(nombre, ide, valor) {
  return crearElemento("input", "checkbox", nombre, ide, valor, false, false);
}

function crearCheck2(nombre, ide, valor, soloLectura, disabled) {
  return crearElemento("input", "checkbox", nombre, ide, valor, soloLectura, disabled);
}

function crearRadio(nombre, ide, valor) {
  return crearElemento("input", "radio", nombre, ide, valor, false, false);
}

function crearRadio2(nombre, ide, valor, soloLectura, disabled) {
  return crearElemento("input", "radio", nombre, ide, valor, soloLectura, disabled);
}

function crearImg(ide,name,src) {
  var elemento  = document.createElement("img");
  elemento.src= src;
  elemento.name = name;
  elemento.id   = ide;
  return elemento;
}

function crearElemento2(elem, nombre, ide, valor) {
  var elemento  = document.createElement(elem);
  elemento.name = nombre;
  elemento.id   = ide;
  elemento.href= valor;
  return elemento;
}

function crearElemento(elem, tipo, nombre, ide, valor, soloLectura, deshabilitado) {
  var elemento  = document.createElement(elem);
  elemento.type = tipo;
  elemento.name = nombre;
  elemento.id   = ide;
  elemento.value= valor;
  elemento.readonly = soloLectura;
  elemento.disabled = deshabilitado;
  return elemento;
}