var timeoutId = false;

function popup(url,ancho,alto,propiedades) {
  var izq = (screen.width-ancho)/2;
  var arr = (screen.height-alto)/2-50;
  window.open(url,'','width='+ancho+',height='+alto+',left='+izq+',top='+arr+','+propiedades);
} 

function open_calendar(ruta,campo) {
  //alert(ruta+" "+ campo);
    
  var newWindow;
  var urlstring = ruta+"?campo="+campo;
   
  //newWindow = window.open(urlstring,'','height=220,width=280,toolbar=no,minimize=no,status=no,menubar=no,location=no,scrollbars=no')
  newWindow = popup(urlstring,280,220,'toolbar=no,minimize=no,status=no,menubar=no,location=no,scrollbars=no,alwaysRaised=yes');
}