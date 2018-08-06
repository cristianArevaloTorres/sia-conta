<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.db.sql.Sentencias,sia.db.sql.Vista,java.util.*,sia.rf.contabilidad.registroContable.acciones.ControlRegistro"%>
<%@ page import="sia.db.dao.*, java.sql.*"%>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="abCtasMayor"  class="sun.jdbc.rowset.CachedRowSet" scope="page"/>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
        <title>c727FiltroReporte</title>
        <link rel="stylesheet" href="../../../Librerias/Estilos/siaexplorer.css" type="text/css">
        <script language="JavaScript" src="../../../Librerias/Javascript/Capturador/funciones.js" type="text/javascript"></script>
        <script language="JavaScript" src="../../../Librerias/Javascript/Capturador/calendar.js" type="text/javascript"></script>
        <script language="JavaScript" type='text/JavaScript'>
        
            var xmlHttp
            function mostrarCuentasFechasLibros(opcion){
                
                xmlHttp = new XMLHttpRequest();
                if (xmlHttp == null){
                    alert ("Tu navegador no soporta AJAX!");
                    return;
                }
	
                hoy = new Date();
                id_refrescar = Math.abs(Math.sin(hoy.getTime()));
    
                var url = "c727PintarCuentasFechasLibros.jsp";
                url += "?opcion=" + opcion;
                url += "&id=" + id_refrescar;
                
                //alert("URL:   " + url);
                
                ocultarFirmasAutorizadas();
    
                xmlHttp.onreadystatechange = mostrarCuentasFechasLibrosResultado;
                xmlHttp.open("GET", url, true);
                xmlHttp.send(null);
            }
            
            function mostrarCuentasFechasLibrosResultado(){
                if (xmlHttp.readyState == 4){
                    //alert( xmlHttp.responseText);
                    document.getElementById("DIVCuentasFechasLibros").innerHTML = xmlHttp.responseText;
                }
            }

            ////////
            
            function mostrarCuentasMayor(){
                
                xmlHttp = new XMLHttpRequest();
                if (xmlHttp == null){
                    alert ("Tu navegador no soporta AJAX!");
                    return;
                }
	
                hoy = new Date();
                id_refrescar = Math.abs(Math.sin(hoy.getTime()));
   
                var id_ejercicio= document.getElementById("anioReporte").value;    
                var url = "c727PintarCuentasMayor.jsp";
                url += "?idEjercicio=" + id_ejercicio;
                url += "&id=" + id_refrescar;
                
                //alert("URL:" + url);
                ocultarFirmasAutorizadas();
                
                xmlHttp.onreadystatechange = mostrarCuentasMayorResultado;
                xmlHttp.open("GET", url, true);
                xmlHttp.send(null);
            }
            
            function mostrarCuentasMayorResultado(){
                if (xmlHttp.readyState == 4){
                    document.getElementById("DIVListaCuentasMayor").innerHTML = xmlHttp.responseText;
                }
            }

            function mostrarFirmasAutorizadas(){
                xmlHttp = new XMLHttpRequest();
                if (xmlHttp == null){
                    alert ("Tu navegador no soporta AJAX!");
                    return;
                }
	
                hoy = new Date();
                id_refrescar = Math.abs(Math.sin(hoy.getTime()));
                
                var id_mes = 12; //Se muestra los firmantes del mes de diciembre, los libros son anuales
                var id_documento;
                var id_ejercicio;
                var id_unidad;
                var id_ambito;
                var id_entidad;
    
                var tipoReporteSeleccionado = document.getElementById("LM");    
                
                //alert("Libro Mayor: " + tipoReporteSeleccionado);
                if(tipoReporteSeleccionado.checked) {
                    id_documento = "MAY";
                    id_ejercicio = document.getElementById("anioReporte").value;
                    
                    if(id_ejercicio == 'Seleccionar') {
                        document.getElementById("RBFirmasSin").checked = true;
                        alert("\nSe generar\u00f3n los siguientes errores en la validaci\u00f3n:\n" +
                            "-------------------------------------------------------------------\n\n" +
                            "      - Favor de elegir de la lista desplegable el año del ejercicio \n" +
                            "\n--------------------------------------------------------------------\n" +
                            "Por favor presiona aceptar e intenta nuevamente." );
                          
                        return;
                    }
                    
                } else {
                    var tipoReporteSeleccionado = document.getElementById("LDGC");    
                    //alert("Diario Concentrado: " + tipoReporteSeleccionado);
                    if(tipoReporteSeleccionado.checked) {
                        id_documento = "LDC";
                        id_ejercicio = document.getElementById("txtFecha01").value.toString().substr(6, 9);
                        //alert("Id Ejercicio: " + id_ejercicio);
                        if(id_ejercicio == "") {
                            document.getElementById("RBFirmasSin").checked = true;
                            alert("\nSe generar\u00f3n los siguientes errores en la validaci\u00f3n:\n" +
                                "-------------------------------------------------------------------\n\n" +
                                "      - Favor de elegir la fecha de inicio en el calendario \n" +
                                "\n--------------------------------------------------------------------\n" +
                                "Por favor presiona aceptar e intenta nuevamente." );

                            return;
                        }
                    } else {
                        var tipoReporteSeleccionado = document.getElementById("LDGO");    
                        //alert("Diario General: " + tipoReporteSeleccionado);
                        if(tipoReporteSeleccionado.checked) {
                            id_documento = "LDG";
                            id_ejercicio = document.getElementById("txtFecha01").value.toString().substr(6, 9);
                            //alert("Id Ejercicio: " + id_ejercicio);
                            if(id_ejercicio == "") {
                                document.getElementById("RBFirmasSin").checked = true;
                                alert("\nSe generar\u00f3n los siguientes errores en la validaci\u00f3n:\n" +
                                    "-------------------------------------------------------------------\n\n" +
                                    "      - Favor de elegir la fecha de inicio en el calendario \n" +
                                    "\n--------------------------------------------------------------------\n" +
                                    "Por favor presiona aceptar e intenta nuevamente." );


                                return;
                            }
                        }
                    }
                }

                    // // Libros son generados por Oficinas Centrales
                id_unidad = "002";
                id_ambito = 1; 
                id_entidad = 9;

                var url = "c727PintarFirmasAutorizadas.jsp";
                url += "?idMes=" + id_mes;
                url += "&idDocumento=" + id_documento;
                url += "&idEjercicio=" + id_ejercicio;
                url += "&idUnidad=" + id_unidad;
                url += "&idAmbito=" + id_ambito;
                url += "&idEntidad=" + id_entidad;
                url += "&id=" + id_refrescar;
                
                //alert("URL: " + url);
    
                xmlHttp.onreadystatechange = mostrarFirmasAutorizadasResultado;
                xmlHttp.open("GET", url, true);
                xmlHttp.send(null);
            }
            
            function mostrarFirmasAutorizadasResultado(){
                if (xmlHttp.readyState == 4){
                    //alert( xmlHttp.responseText);
                    document.getElementById("DIVFirmasAutorizadas").innerHTML = xmlHttp.responseText;
                    document.getElementById("DIVFirmasAutorizadas").style.display = "inline";
                }
            }
            //Fin Ajax
            
            function ocultarFirmasAutorizadas(){
                document.getElementById("RBFirmasSin").checked = true;
                document.getElementById("DIVFirmasAutorizadas").innerHTML = "";
                document.getElementById("DIVFirmasAutorizadas").style.display = "none";
            }
            
            function inicializarComponentes() {
                mostrarCuentasFechasLibros(0);
                
            }
            
            function estaVacio(componente){
                return (  (componente == null) || (componente.lenght == 0)  || (componente == "") )
            }
            
            function validadFormulario(){
                var listaErrores="";
                var objeto="";

                if( document.getElementById("LM").checked){ 
                    var ejercicio= document.getElementById("anioReporte").value;
                    var cuenta_mayor= document.getElementById("lstCtasMayor").value;
                   
                    if(  ejercicio == "Seleccionar" ) {
                        listaErrores += "     -  Ejercicio:   Vac\u00edo " + "\n";
            
                        //if( estaVacio(objeto) ) {
                        //    objeto= "anioReporte";
                        //}
                    } 
                    
                    if( cuenta_mayor == "Seleccionar" ) {
                        listaErrores += "     -  Cuenta de mayor:   Vac\u00edo " + "\n";
            
                        //if( estaVacio(objeto) ) {
                        //    objeto= "lstCtasMayor";
                        //}
                    } 
                } else {
                    var fecha_izq= document.getElementById("txtFecha01").value;
                    var fecha_der= document.getElementById("txtFecha02").value;
                    
                    if( estaVacio(fecha_izq) ) {
                        listaErrores += "     -  Fecha Inicio:   Vac\u00edo " + "\n";
            
                        if( estaVacio(objeto) ) {
                            objeto= "txtFecha01";
                        }
                    } 
                    
                    if( estaVacio(fecha_der) ) {
                        listaErrores += "     -  Fecha Fin:   Vac\u00edo " + "\n";
            
                        if( estaVacio(objeto) ) {
                            objeto= "txtFecha02";
                        }
                    }
                } 
                
                
                if( !document.getElementById("RBFirmasSin").checked) { 
                    if( !document.getElementById("RBFirmasCon").checked){ 
                        listaErrores += "     -  Reporte con/sin firmas:   Vac\u00edo " + "\n";
                    }
                }
                
                
                if( !estaVacio(listaErrores) ){
                    
                    listaErrores="\nSe generar\u00f3n los siguientes errores en la validaci\u00f3n:\n" +
                        "-------------------------------------------------------------------\n\n" +
                        listaErrores +
                        "\n--------------------------------------------------------------------\n" +
                        "Por favor presiona aceptar e intenta nuevamente."; 
					 
                    alert(listaErrores);
		
                    if( !estaVacio(objeto) ) {
                        (document.getElementById(objeto)).focus();
                        (document.getElementById(objeto)).select();
                    }
                    
                    return false;

                } else {
                    return true;
                }
            }
            
            
            function deshabilitarSinFirmas(valor) {
                document.getElementById("RBFirmasSin").disabled=valor;
                document.getElementById("RBFirmasSin").checked= !valor;
            }
            
            
        </script>

    </head>
    <body onload="inicializarComponentes()">
        <form id="forma" name="forma" method="post" action="c727ImprimirLibros.jsp" target="_blank">
            <%util.tituloPagina(out, "Contabilidad", " ", "Generacion de reportes de libros contables", "Reportes", true);%>
            <br/>
            <br/>
            <table border="0" width="60%" class='sianoborder' align="center">
                <tr>
                    <td colspan="2">
                        <input id="LM" type="radio" name="reporte"  onclick=" mostrarCuentasFechasLibros(this.value); deshabilitarSinFirmas(false);" value="0" checked > <b>Libro Mayor</b>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <input id="LDGC" type="radio" name="reporte" onclick=" mostrarCuentasFechasLibros(this.value); deshabilitarSinFirmas(false)" value="1"> <b>Diario General Concentrado</b>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <input id="LDGO" type="radio" name="reporte" onclick=" mostrarCuentasFechasLibros(this.value); deshabilitarSinFirmas(true); " value="2"> <b>Libro Diario General</b>
                    </td>
                </tr>
                <tr>
                    <td colspan=2>
                        <div id="DIVCuentasFechasLibros"></div>
                    </td>
                </tr>
            </table>


            <div id="DIVFormatosReportes">
                <hr noshade="noshade" width="60%" align="center" style="border-width: 1px;border-style:solid"> 
                <br/>
                <table width="30%" align="center" border="0" >
                    <tr class="azulCla">
                        <td align="center"><b>FORMATO DEL REPORTE</b></td>
                    </tr>
                </table>

                <table border="0" width="8%" class='sianoborder' align="center">
                    <tr>
                        <td>
                            <input id="pdf" type="radio" name="formato" value="0" checked > PDF
                        </td>
                    </tr>
                    <!--<tr>
                        <td>
                            <input id="xls" type="radio" name="formato" disabled="fase" value="1" onclick="ocultarFirmas()" <%=request.getParameter("formato") != null && request.getParameter("formato").equals("1") ? "checked" : ""%> /> Exel
                        </td>
                    </tr>
                    -->
                </table>
                <br/><br/> 

                <div id="DIVOpcionesFirmas">
                    <table border="0" class='sianoborder' align="center" id="TBLOpcionesFirmas" name="TBLOpcionesFirmas" width="30%">
                        <tr>
                            <td colspan="2">
                                <input id="RBFirmasSin" type="radio" name="RBFirmas" value="Sin" checked onclick="ocultarFirmasAutorizadas()"> <b>Reporte sin firmas</b>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <input id="RBFirmasCon" type="radio" name="RBFirmas" value="Con" onclick="mostrarFirmasAutorizadas()"> <b>Reporte con firmas</b>
                            </td>
                        </tr>

                        <tr> 
                            <td colspan="2">
                                <br/>
                                <div id="DIVFirmasAutorizadas" ></div>
                            </td>
                        </tr> 
                    </table>   
                </div>

                <br/><br/> 
<!--                <div id="DIVNumPaginaInicial"><br/>
                    <table border="1" class='sianoborder' align="center" id="TBLNumeroPaginaInicial" name="NumeroPaginaInicial">
                        <tr>
                            <td>
                                <b>Número de página inicial:</b>
                            </td>
                            <td >
                                <input id="TBNumPagInicial" type="text" name="TBNumPagInicial" >
                            </td>
                        </tr>

                    </table>   
                </div>
            </div>-->

            <br/>
            <hr noshade="noshade" width="60%" align="center" style="border-width: 1px;border-style:solid"> 
            <br/>
            <table cellpadding="5" border="0" width="15%" class="sianoborder" align="center">
                <tr align="center">
                    <td>
                        <input type="submit" class="boton" value="Generar Reporte" onclick="return validadFormulario();" />
                    </td>
                    <td>
                        <input type="button" class="boton" value="Regresar" onClick="javascript:history.back();"/>
                    </td>
                </tr>
                <!--
                <td>
                    <input type="button" class="boton" value="Descargar" onClick="document.forma.action='c727DescargaReportes.jsp'; document.forma.submit();"/>
                </td>
                </tr>
                -->
            </table>

        </form>
    </body>
</html>