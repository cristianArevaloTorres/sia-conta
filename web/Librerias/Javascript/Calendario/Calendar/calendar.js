var timeoutId = false;

function open_calendar(ruta,campo) {
    var newWindow;
    var urlstring = ruta+"?campo="+campo;
    newWindow = window.open(urlstring,'','height=220,width=280,toolbar=no,minimize=no,status=no,memubar=no,location=no,scrollbars=no')
 }