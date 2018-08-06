<%-- 
    Document   : 750Resultado
    Created on : 17/08/2017, 02:23:28 PM
    Author     : erika.lopezob
--%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page  import="java.util.*,java.sql.*"%>
<%@ page  import="sia.db.dao.*"%>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>  
<jsp:useBean id="Autentifica" class="sia.beans.seguridad.Autentifica" scope="session"/>  
<jsp:useBean id="xPaginacion" class="sia.utilerias.generador.hpaginacion" scope="page"/>
<jsp:useBean id="fechaAfectacion" class="sia.rf.contabilidad.registroContable.servicios.FechaAfectacion" scope="page"/>
<jsp:useBean id="controlRegistro" class="sia.rf.contabilidad.registroContable.acciones.ControlRegistro" scope="session"/>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
        <title>c750Resultado</title>
        <link rel="stylesheet" href="../../../../Librerias/Estilos/siastyle.css" type='text/css'>
        <script language="JavaScript" src="../../../../Librerias/Javascript/Capturador/funciones.js" type="text/javascript"></script>
        <script language="JavaScript" src="../../../../Librerias/Javascript/Capturador/fcgencValida2.js" type="text/javascript"></script>
        <script language="JavaScript" src="../../../../Librerias/Javascript/Capturador/calendar.js" type="text/javascript"></script>

        <script languaje="JavaScript" type='text/JavaScript'>

            function revisa(){ // Valida que al menos un check box está seleccionado y si es así, confirmar su Revisión
                checksOK = false;
                checksOK = verificaChecksSinSubmit(document.resultado, 'AMBITOS', 'Debe seleccionar al menos un cheque'); 
                return checksOK;
            }//function

            function revisarForm(){
                return revisa() ;
                    //&& validaRadio() && validaFecha();
            }

            function validaFecha(){
                pasa = false;
                fechaCan = document.getElementById('txtFechaCancelacion').value;
                tipoFecha = valorRadio();
  
                if(tipoFecha == "seleccion"){
                    if(fechaCan==""){
                        alert('AVISO: Favor de seleccionar una fecha de Cancelación.');
                        pasa = false;
                    }else{
                        if(tipoFecha == "seleccion"){
       
                            pasa= confirm('¿Está seguro de cancelar los cheques seleccionados con fecha de cancelación '+fechaCan+' ?');
                        }
                        else{
                            pasa= confirm('¿Está seguro de cancelar el(los) cheque(s) seleccionado(s)?');
                        }
                    }
                }else{ pasa = true;}
                if(pasa)
                    resultado.btnAceptar.disabled=true;
                return pasa;
            }

            function valorRadio(){
                var valor="";
                elementos = document.getElementById('resultado').elements;
                longitud = elementos.length;
                for (var i = 0; i < longitud; i++){
                    if(elementos[i].name == "selFecha" && elementos[i].type == "radio" && elementos[i].checked == true){
                        valor = elementos[i].value;
                    }
                }
                return valor;
            }

            function validaRadio(){
                var avanzar = true;
                if(valorRadio() == ""){
                    alert('AVISO: Selecione que fecha requiere para la cancelación.\n');
                    avanzar = false;
                }
                return avanzar;
            }

            function activar(parametro){
                if(parametro == 0)
                    document.getElementById('fechaCancelacion').style.display  = "none";
                else
                    document.getElementById('fechaCancelacion').style.display  = "";
            }

            function ocultar(){
                alert('entra funcion ocultar');
                document.getElementById('fechaCancelacion').style.display  = "none";
            }
        </script>

    </head>
    <body >
        <%
            String opcion = controlRegistro.getPagina();
            if (opcion.equals("reexpedir")) {
        %>
        <jsp:scriptlet>util.tituloPagina(out, "Contabilidad", " ", "Resultado de cheques a reexpedir", "Reexpedir", true);</jsp:scriptlet>    
        <%} else {%>  
        <jsp:scriptlet>util.tituloPagina(out, "Contabilidad", " ", "Resultado de cheques a cancelar", "Cancelar", true);</jsp:scriptlet>    
        <%}%>

        <!---<br><br>
              <b>Fecha Actual</b> [<%=request.getParameter("txtfechaActual")%>]
        <br><br>--->

        <H2>Informaci&oacute;n del Cheque: Unidad ejecutora=<%=controlRegistro.getUnidad()%>, Entidad=<%=controlRegistro.getEntidad()%>, Ambito=<%=controlRegistro.getAmbito()%> y Ejercicio=<%=controlRegistro.getEjercicio()%>  </H2>
        <br>
        <FORM name="resultado" id="resultado" Method="post" action="c750Control.jsp" onsubmit='return revisarForm();' >

            <!-- Encabezado de la tabla con resultado del filtro -->
            <table width="15%" align="left" class="general">
                <tr>
                    <th height="21" colspan="3" align="left" class="general">Resultado del filtro</th>
                </tr>
            </table>

            <br><br><br>
            <%
                Connection conexion = null;
                //String lsOpcion= controlRegistro.getPagina();
                //String lsIdCatalogo=String.valueOf(controlRegistro.getIdCatalogoCuenta());
                //String lsUnidad= controlRegistro.getUnidad();
                //String lsAmbito= String.valueOf(controlRegistro.getAmbito());
                //String lsEntidad= String.valueOf(controlRegistro.getEntidad());
                //String lsEjercicio= String.valueOf(controlRegistro.getEjercicio());
                //String lsFechaActual=controlRegistro.getFechaEstablecida();
                String lsMes = request.getParameter("lstMes");
                String lsFecha01 = request.getParameter("txtFecha01");
                String lsFecha02 = request.getParameter("txtFecha02");
                String lsPoliza01 = request.getParameter("txtPoliza01");
                String lsPoliza02 = request.getParameter("txtPoliza02");
                String lsCuentaBancaria = request.getParameter("lstCuentaBancaria");
            %>

            <!-- Inicia tabla para desplegar resultado del filtro utilizando el bean xPaginacion -->
            <table width="100%" align="center" class="general" name="tAmbitos" id="tAmbitos">
                <!-- Mensaje "Marcar todos" -->

                <tr>
                    <td width="100%" bgcolor="#B6D0F1"> <input type="radio" name="ambitoRegional" value="0AMBITOS" onClick="seleccionarOpcion(document.resultado, this);">
                        Marcar todos &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <input name="ambitoRegional" type="radio" value="1AMBITOS" onClick="seleccionarOpcion(document.resultado, this);" checked>
                        Des-marcar todos </td>
                </tr>

                <tr>
                    <td>
                        <!-- (Inicia codigo JAVA) -->
                        <%
                            String Query = "";
                            String lsCondicion = "";
                            if (opcion.equals("reexpedir")) {
                                lsCondicion = " and a.origen in('NA','NB','NC','CB','CS','29') ";
                            }

                            String lsAnio = "";
                            String lsCondModificar = "";

                            try {
                                conexion = DaoFactory.getContabilidad();
                                if (!lsMes.equals("")) {
                                    lsCondicion = lsCondicion + " and to_date(to_char(cheque.FECHACHEQUE,'MM'),'MM')=to_date('" + lsMes + "','MM')";
                                }
                                if (!String.valueOf(controlRegistro.getEjercicio()).equals("")) {
                                    lsAnio = " and to_date(to_char(cheque.FECHACHEQUE,'yyyy'),'yyyy')=to_date('" + controlRegistro.getEjercicio() + "','yyyy') ";
                                }

                                if (!lsFecha01.equals("")) {
                                    lsCondicion = lsCondicion + " and to_date(to_char(cheque.FECHACHEQUE,'dd/MM/yyyy'),'dd/MM/yyyy')>=to_date('" + lsFecha01 + "','dd/MM/yyyy') and to_date(to_char(cheque.FECHACHEQUE,'dd/MM/yyyy'),'dd/MM/yyyy')<=to_date('" + lsFecha02 + "','dd/MM/yyyy') ";
                                }
                                if (!lsPoliza01.equals("")) {
                                    lsCondicion = lsCondicion + " and upper(b.abreviatura)='E' and cheque.consecutivo >=" + lsPoliza01 + " and cheque.consecutivo<=" + lsPoliza02;
                                }

                                lsCondicion = lsCondicion
                                        + " and a.poliza_id not in "
                                        + " (select distinct p.poliza_referencia from rf_tr_polizas p where p.id_catalogo_cuenta='" + controlRegistro.getIdCatalogoCuenta()
                                        + "' and p.unidad_ejecutora='" + controlRegistro.getUnidad() + "' and p.ambito=" + controlRegistro.getAmbito()
                                        + " and p.entidad=" + controlRegistro.getEntidad() + " and p.poliza_referencia is not null ) ";

                                Query = "select ROWNUM CONSFILA, sqlresul.* from ("
                                        + "SELECT distinct '' polizaid,''||'-'||cheque.cheque_id ids,cheque.consecutivo as chequeCons, '' consecutivo,to_char(cheque.fechacheque,'dd/mm/yyyy') fecha,cheque.beneficiario, cheque.referencia concepto,'CS' origen, 'Cheque' as poliza "
                                        + "FROM RF_TR_CHEQUES cheque, RF_TC_CUENTAS_CHEQUES CC "
                                        + "where 1 = 1 "
                                        + lsAnio
                                        + " and cheque.cuenta_cheques_id=cc.cuenta_cheques_id  and cc.id_cuenta=" + lsCuentaBancaria
                                        + " and cheque.estatus not in (3)"
//                                        + " and to_number(to_char(a.fecha,'mm')) not in ( select distinct  t.mes from rf_tr_cierres_mensuales t where t.unidad_ejecutora='" + controlRegistro.getUnidad()
  //                                      + "' and t.ambito='" + controlRegistro.getAmbito() + "' and t.entidad='" + controlRegistro.getEntidad()
    //                                    + "' and t.ejercicio='" + controlRegistro.getEjercicio() + "' and t.id_catalogo_cuenta='" + controlRegistro.getIdCatalogoCuenta()
      //                                  + "' and t.programa=substr(c.cuenta_contable,6,4) and t.estatus_cierre_id='2') "
                                        + ") sqlresul ";

                                System.out.println("Query c750Resultado: " + Query);      
                                String[] DefQuery = {Query, "100%", "LEFT", "2", ""};
                                String[] DefAlias = {"CHEQUECONS-,FECHA-,BENEFICIARIO-,CONCEPTO-,", "9%,9%,9%,30%,30%", "Consecutivo Cheque,Fecha,Beneficiario,Concepto", "4,4,4,4,4"};
                                String[] DefInput = {"IDS", "CHECKBOX", "ambitos", "LEFT"};
                                xPaginacion.SeleccionarHorizontalQuery(conexion, DefQuery, DefAlias, DefInput, out, 0, "", "", "", true);
                            } catch (Exception E) {
                                System.out.println("Error en " + E.getMessage());
                                System.out.println("Query Cancela/Reexpide CXL : " + Query);
                        %>
                        <p>Ha ocurrido un error al accesar la Base de Datos,</p>
                        <p>favor de reportarlo al Administrador del Sistema.</p>
                        <p>Gracias.</p>
                        <%
                            } finally {
                                if (conexion != null) {
                                    conexion.close();
                                    conexion = null;
                                }
                            }
                        %>
                    </td>
                </tr>
            </table>
            <br><br><br>
<!--            <table width="30%" class="azulCla">
                <tr > <td>Selección de fecha de cancelación</td></tr>
            </table>-->
<!--            <table border="0" width="30%" class='sianoborder'>
                <tr><td >
                        <input type="radio" name="selFecha" value="original" onclick="return activar(0);" checked="TRUE"/> Fecha original del cheque
                    </td></tr>
                <tr><td >
                        <input type="radio" name="selFecha" value="seleccion" onclick="return activar(1);"/> Seleccionar fecha de cancelación
                    </td></tr>
            </table>-->
<!--            <div id="fechaCancelacion" style="display:none">
                </br>
                <font color="red"><b>**NOTA:</b>  La fecha que se seleccione es la que aplicará contablemente para la cancelacion de todos los cheques seleccionados.</font>
                </br>
                <table>
                    </br>
                    <tr><td class="negrita" align="right">Fecha cancelacion: </td>
                        <td><input type='text' name='txtFechaCancelacion' id="txtFechaCancelacion" size='10' maxlength='10' class='cajaTexto'><a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" onClick="open_calendar('../../../../Librerias/Javascript/Capturador/calendar.html','document.forms[0].txtFechaCancelacion')"><img src="../../../../Librerias/Imagenes/calendar.gif" name="imgCalendar" width="34" height="21" border="0" alt=""></a> dd/mm/aaaa</td>
                        value="<%=sia.libs.formato.Fecha.formatear(sia.libs.formato.Fecha.FECHA_CORTA, controlRegistro.getFechaAfectacion())%>"
                    </tr>
                </table>
            </div>-->

            <table width='100%'>
                <tr><td width='73%'>&nbsp;</td>
                    <td width='10%'>
                        <!--<INPUT type="hidden" name="txtfechaActual" value=''>
                         <INPUT type="hidden" name="txtOperacion" value=''>-->
                        <input type='hidden' name='opcion' value=<%=controlRegistro.getPagina()%>>
                        <input type='submit' name='btnAceptar' value='Aceptar' class='boton'>
                    </td>
                    <td width='80%'>
                        <input type='button' name='btnRegresar' value='Regresar' class='boton' onClick="javascript:LlamaPagina('c750Filtro.jsp?opcion=<%=opcion%>','');" >
                    </td></tr>
            </table>

        </FORM>
    </body>
</html>
