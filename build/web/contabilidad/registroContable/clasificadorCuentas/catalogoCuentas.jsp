<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page  import="java.util.*,java.sql.*"%>
<%@ page  import="sia.db.dao.*"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>   
<jsp:useBean id="abCierre" class="sun.jdbc.rowset.CachedRowSet" scope="page">
    <%
        Connection conexion = null;
        conexion = DaoFactory.getContabilidad();
        abCierre.setTableName("rf_tc_ejercicios");
        abCierre.setCommand(" select ejercicio,abierto "
            + " from rf_tc_ejercicios  "
            + " where abierto=0 "
            + " order by ejercicio desc ");
        abCierre.execute(conexion);
        conexion.close();
        conexion = null;
    %>
</jsp:useBean> 
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
        <title>catalogoCuentas</title>
        <link rel="stylesheet" href="../../../Librerias/Estilos/siastyle.css" type='text/css'>
        <link rel="stylesheet" href="../../../Librerias/Estilos/siaexplorer.css" type="text/css"></link>
        <script language="JavaScript" src="../../../Librerias/Javascript/Capturador/calendar.js" type="text/javascript"></script>
        <script type="text/javascript" language="javascript">
            function seleccionarArchivo(tis) {
                row = tis.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode;
                var elementos = document.getElementsByName("accion");
                for(var i=0; i<elementos.length; i++) {
                    if(elementos[i].checked)
                        accion = i;
                }//for
                window.open("../../registroContable/clasificadorCuentas/cargarArchivo.jsp?accion="+accion);
            }
      
            function activar() {
                var accionActiva = 0;
                var radio = document.getElementsByName("accion");
                for(var i=0; i<radio.length; i++) {
                    if(radio[i].checked)
                        accionActiva = i;
                }
                if(accionActiva==0){
                    document.getElementById('fechas').style.display ='';
                    document.getElementById('tipoSaldo').style.display ='none';
                    document.getElementById('divEjercicio').style.display ='none';
          
                }else{
                    if(accionActiva==1){
                        document.getElementById('tipoSaldo').style.display ='';
                        document.getElementById('fechas').style.display ='none';
                        document.getElementById('divEjercicio').style.display ='';
                    }else{
                        document.getElementById('tipoSaldo').style.display ='none';
                        document.getElementById('fechas').style.display ='none';
                        document.getElementById('divEjercicio').style.display ='';
                    }
                }
            }
      
            function regresaSeleccionaArchivo(archivo,ruta,accion) {
                if(archivo!='') {
                    document.getElementById('nombreArchivo').value = archivo;
                    document.getElementById('cargado').style.display ='';
                    document.getElementById('rutaArchivo').value = ruta;
                }
            }
      
            function verificar(){
                var seleccionado = true;
                var pasa = true;
                var mensaje = "Error: \n";
                var radio = document.getElementsByName("accion");
                for(var i=0; i<radio.length; i++) {
                    //alert(radio.length);
                    if(radio[i].checked)
                        seleccionado = false;
                }
                if(seleccionado){
                    mensaje += "Se requiere seleccionar un tipo de operación.\n";
                    pasa = false;
                }
                if(document.getElementById('nombreArchivo').value == ""){
                    mensaje += "Se requiere seleccionar el archivo a procesar.\n";
                    pasa = false;
                }
                
                if(!pasa) {
                    alert(mensaje);
                    return pasa;
                } else {
                    from.BTNProcesar.disabled = true;
                    from.submit();
                    submited = true;
                }
                    
                
            }
        </script>
    </head>
    <body>
        <jsp:scriptlet>util.tituloPagina(out, "Contabilidad", " ", "Procesar archivo", "Cargar", true);</jsp:scriptlet>    
            <form id="from" action="controlCatalogoCuentas.jsp">
                <table align="center" cellspacing="5">
                    <tr>
                        <td align="right" class='negrita'>Catalogo:</td>
                        <td>		
                            <select name="cboIdCatalogoCuenta" class="cajaTexto" id="cboMes">
                                <option value="1">1 Institucional</option>
                                <option value="2">2 Bancos IXV</option>
                                <option value="3">3 Costos</option>

                            </select>
                        </td>
                    </tr>
                    <!--<tr>
                    <td align="right" class='negrita'>Nivel máximo:</td>
                    <td>	
                    <select name="cboNivel" class="cajaTexto" id="cboMes">
                    <option value="9">9</option>
                    <option value="10">10</option>
                    <option value="11">11</option>
                    <option value="10">12</option>
                    <option value="11">13</option>
                    </select>
                     </td>
                     </tr>
                    -->
                </table>
                <table width="30%" align="center">
                    <tr class="azulCla">
                        <td>Tipo de operación</td>
                    </tr>
                </table>
                <table align="center" cellspacing="5">
                    <tr><td><input type="radio" name="accion" value="1" onclick="activar()"/>Cargar cuentas</td></tr>
                    <tr><td><input type="radio" name="accion" value="2" onclick="activar()"/>Actualizar saldos</td></tr>
                    <tr><td><input type="radio" name="accion" value="3" onclick="activar()"/>Actualizar descripciones</td></tr>
                    <tr><td><input type="radio" name="accion" value="4" onclick="activar()"/>Eliminar cuentas</td></tr>
                </table>
                <div id="fechas" style="display:none">
                    <table width="30%" align="center">
                        <tr class="azulCla">
                            <td>Selección de fechas</td>
                        </tr>
                    </table>
                    <table align="center" cellspacing="5">
                        <tr>
                            <td align="right" class='negrita'>Fecha inicial:</td>
                            <td><input type='text' name='txtFechaIni' size='10' maxlength='10' class='cajaTexto' VALUE='' onChange=''><a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;"  onClick="open_calendar('../../../Librerias/Javascript/Capturador/calendar.html','document.forms[0].txtFechaIni');"><img src="../../../Librerias/Imagenes/calendar.gif" name="imgCalendar" width="34" height="21" border="0" alt=""></a></td>
                        </tr>
                        <tr>
                            <td align="right" class='negrita'>Fecha final:</td>
                            <td><input type='text' name='txtFechaFin' size='10' maxlength='10' class='cajaTexto' VALUE='' onChange=''><a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;"  onClick="open_calendar('../../../Librerias/Javascript/Capturador/calendar.html','document.forms[0].txtFechaFin');"><img src="../../../Librerias/Imagenes/calendar.gif" name="imgCalendar" width="34" height="21" border="0" alt=""></a></td>
                        </tr>
                        <tr>
                            <td align="right" class='negrita'>Fecha registro:</td>
                            <td><input type='text' name='txtFechaRegistro' size='10' maxlength='10' class='cajaTexto' VALUE='' onChange=''><a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;"  onClick="open_calendar('../../../Librerias/Javascript/Capturador/calendar.html','document.forms[0].txtFechaRegistro');"><img src="../../../Librerias/Imagenes/calendar.gif" name="imgCalendar" width="34" height="21" border="0" alt=""></a></td>
                        </tr>
                    </table>
                </div>



                <div id="divEjercicio" style="display:none">
                    <table width="30%" align="center">
                        <tr class="azulCla">
                            <td>Seleccione</td>
                        </tr>
                    </table>
                    <table align="center" cellspacing="5">
                        <tr><td class='negrita'>Ejercicio:</td>
                            <td>
                                <SELECT id="ejercicio" class="cajaTexto" NAME="lstEjercicio">
                                <%

                                    try {
                                        abCierre.beforeFirst();
                                        while (abCierre.next()) {
                                %>
                                <OPTION VALUE="<%=abCierre.getString("ejercicio")%>"><%=abCierre.getString("ejercicio")%></OPTION>
                                <%
                                        } //Fin while
                                    } catch (Exception e) {
                                        System.out.println("Error en lista de cierre: " + e.getMessage());
                                    }

                                %>
                            </SELECT> 
                        </td>
                    </tr>  
                </table>
            </div>

            <div id="tipoSaldo" style="display:none">
                <table align="center" cellspacing="5" id="meses">
                    <tr><td class='negrita'>Mes:</td>
                        <td>
                            <SELECT id="mes" class="cajaTexto" NAME="mes">
                                <OPTION VALUE="01">Enero</OPTION>
                                <OPTION VALUE="02">Febrero</OPTION>
                                <OPTION VALUE="03">Marzo</OPTION>
                                <OPTION VALUE="04">Abril</OPTION>
                                <OPTION VALUE="05">Mayo</OPTION>
                                <OPTION VALUE="06">Junio</OPTION>
                                <OPTION VALUE="07">Julio</OPTION>
                                <OPTION VALUE="08">Agosto</OPTION>
                                <OPTION VALUE="09">Septiembre</OPTION>
                                <OPTION VALUE="10">Octubre</OPTION>
                                <OPTION VALUE="11">Noviembre</OPTION>
                                <OPTION VALUE="12">Diciembre</OPTION>
                            </SELECT
                        </td></tr>
                </table>
                <!--<table align="center" cellspacing="5">
                <tr><td><input type="radio" name="tipoCarga" value="1" checked="checked"/>Inicial normal</td></tr>
                <tr><td><input type="radio" name="tipoCarga" value="2"/>Inicial eliminación</td></tr>
                </table>-->
            </div>

            <table align="center" cellspacing="5">
                <tr style="cursor:hand">
                    <td ><a onclick="seleccionarArchivo(this)" style="cursor:hand"><img style="cursor:hand" src="../../../Librerias/Imagenes/botonSubirDocto.gif" title="Seleccionar archivo" alt="" > Seleccionar Archivo</img> </a></td>
                </tr>
                <tr>
                    <td style="display:none" class="azulCla" id="cargado" name="cargado" >Se ha cargado el archivo</td>
                    <!--<td style="display:none" id="tdDocImg" name="tdDocImg"><a id="aDocImg" name='aDocImg' target="_blank"><img src="../../../Librerias/Imagenes/botonExcel.gif" title="Descargar archivo" alt=""> Descargar</img></a></td>-->
                </tr>
            </table>

            <br>
            <table width="50%" align="center">
                <thead>
                    <tr>
                        <td><hr noshade="noshade" width="80%" align="center" style="border-width: 1px;border-style:solid"></td>
                    </tr>
                </thead>
            </table>
            <table align="center" cellpadding="3">
                <thead></thead>
                <tbody>
                    <tr>
                        <td><input id="BTNProcesar" name="BTNProcesar" type="submit" class="boton" value="Procesar" onclick=" return verificar()"></td>
                    </tr>
                </tbody>
            </table>
            <input type="hidden" name="nombreArchivo" id="nombreArchivo" value="">
            <input type="hidden" name="rutaArchivo" id="rutaArchivo" value="">
            <input type="hidden" name="accionRealizar" id="accionRealizar" value="">

        </form>
    </body>
</html>