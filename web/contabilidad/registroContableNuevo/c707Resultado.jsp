<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page  import="java.util.*,java.sql.*"%>
<%@ page  import="sia.db.dao.*"%>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="Autentifica" class="sia.beans.seguridad.Autentifica" scope="session"/>  
<jsp:useBean id="xPaginacion" class="sia.utilerias.generador.hpaginacion" scope="page"/>

<!-- (Inicia codigo JAVA) -->
<%
    Connection conexion = null;

    String lsUnidad = request.getParameter("txtUnidad");
    String lsAmbito = request.getParameter("txtAmbito");
    String lsEntidad = request.getParameter("txtEntidad");
    String lsEjercicio = request.getParameter("txtEjercicio");
    String lsFechaActual = request.getParameter("txtfechaActual");

    String lsCuentaBancaria = request.getParameter("lstCuentaBancaria");
    String lsNoCheque = request.getParameter("txtNoCheque");
    String lsFecha01 = request.getParameter("txtFecha01");
    String lsFecha02 = request.getParameter("txtFecha02");
    String lsImporte = request.getParameter("txtImporte");
    String lsConcepto = request.getParameter("txtConcepto");
    String lsPolizaAsoc = request.getParameter("txtPolizaAsoc");
    String lsBeneficiario = request.getParameter("txtBeneficiario");
    String lsEstatus = request.getParameter("lstEstatus");
%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
        <meta http-equiv="Cache-Control" content ="no-cache">
        <meta http-equiv="Pragma" content="no-cache"> 
        <meta http-equiv="expires" content="0" >

        <link rel="stylesheet" href="../../Librerias/Estilos/siastyle.css" type='text/css'>

        <script language="JavaScript" src="../../Librerias/Javascript/Capturador/funciones.js" type="text/javascript"></script>
        <script language="JavaScript" type='text/JavaScript'>

            function revisa()
            { // Valida que al menos un check box está seleccionado y si es así, confirmar su Revisión
                var avanza = true;
                var checksOK = false;
                var tipoChek = typeof(document.resultado.AMBITOS)
 
                checksOK = verificaChecksSinSubmit(document.resultado, 'AMBITOS', 'Debe seleccionar al menos un cheque');
                if (checksOK)  //Al menos un check box esta seleccionado
                {
                    if(  tipoChek === 'undefined' ) {
                        avanza = false;
                    }
                }
                else        //Ningún check box esta seleccionado
                {
                    avanza=false;
                }
                if (avanza) resultado.btnAceptar.disabled=true;
                return avanza;
            }//function
        </script>
        <title>c707Resultado</title>
    </head>
    <body>

        <jsp:scriptlet>util.tituloPagina(out, "Contabilidad", " ", "Filtro de cheques", "Reactivación", true);</jsp:scriptlet>
            <H2>Informaci&oacute;n de la P&oacute;liza: Unidad ejecutora=<%=lsUnidad%>, Entidad=<%=lsEntidad%>, Ambito=<%=lsAmbito%> y Ejercicio=<%=lsEjercicio%> </H2>
        <br>

        <FORM name="resultado" id="resultado" method="post"  action="c707Control.jsp" onsubmit='return revisa();'>
            <!-- Encabezado de la tabla con resultado del filtro -->
            <table width="15%" align="left" class="general">
                <tr>
                    <th height="21" colspan="3" align="left" class="general">Resultado del filtro</th>
                </tr>
            </table>
            <br><br><br>

            <!-- Inicia tabla para desplegar resultado del filtro utilizando el bean xPaginacion -->
            <table width="100%" align="center" class="general" name="tAmbitos" id="tAmbitos">
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
                            try {
                                conexion = DaoFactory.getContabilidad();
                                String lsCondicion = "";

                                if (!lsCuentaBancaria.equals("")) {
                                    lsCondicion = lsCondicion + " and cb.id_cuenta = " + lsCuentaBancaria + " ";
                                }
                                if (!lsNoCheque.equals("")) {
                                    lsCondicion = lsCondicion + " and ch.consecutivo=" + lsNoCheque + " ";
                                }
                                /*
                                 if (!lsEjercicio.equals("")) {
                                 lsCondicion = lsCondicion + " and to_date(to_char(c.FECHA,'yyyy'),'yyyy')=to_date('"+lsEjercicio+"','yyyy')";
                                 }  
                                 */
                                if (!lsFecha01.equals("")) {
                                    lsCondicion = lsCondicion + " and to_date(to_char(ch.fecha,'dd/MM/yyyy'),'dd/MM/yyyy')>=to_date('" + lsFecha01 + "','dd/MM/yyyy') and to_date(to_char(ch.fecha,'dd/MM/yyyy'),'dd/MM/yyyy')<=to_date('" + lsFecha02 + "','dd/MM/yyyy') ";
                                }
                                if (!lsImporte.equals("")) {
                                    lsCondicion = lsCondicion + " and ch.importe =" + lsImporte + " ";
                                }
                                if (!lsConcepto.equals("")) {
                                    lsCondicion = lsCondicion + " and upper(ch.referencia) like  upper('%" + lsConcepto + "%') ";
                                }
                                if (!lsPolizaAsoc.equals("")) {
                                    lsCondicion = lsCondicion + " and 'C' || p.consecutivo =upper('" + lsPolizaAsoc + "') ";
                                }
                                if (!lsBeneficiario.equals("")) {
                                    lsCondicion = lsCondicion + " and upper(ch.beneficiario) like  upper('%" + lsBeneficiario + "%') ";
                                }

                                if (!lsEstatus.equals("")) {
                                    lsCondicion = lsCondicion + " and ch.estatus = " + lsEstatus + " ";
                                }

                                /* Pendiente
                                 if(!Autentifica.getPerfilAcceso().equals("1051")){
                                 lsCondicion=lsCondicion+ " and a.poliza_referencia is null " +
                                 " and a.poliza_id not in " +
                                 " (select distinct p.poliza_referencia from rf_tr_polizas p where p.id_catalogo_cuenta='"+lsIdCatalogo+"' and p.unidad_ejecutora='"+lsUnidad+"' and p.ambito="+lsAmbito+" and p.entidad="+lsEntidad+" and p.poliza_referencia is not null ) ";
                                 }*/

                                Query = "select ROWNUM CONSFILA, sqlresul.* from "
                                        + "(select ch.cheque_id, cb.num_cuenta ||'   '||cb.nombre_cta nombre_cta, ch.consecutivo, to_char(ch.fechaCheque, 'dd/mm/yyyy') fechaCheque, "
                                        + "ch.importe, ch.poliza_id, ch.referencia, ch.beneficiario, decode(ch.estatus,0,'GENERADO',1,'IMPRESO',2,'REACTIVADO') estatus, tp.abreviatura || p.consecutivo consPoliza "
                                        + "from rf_tr_cheques ch, rf_tesoreria.rf_tr_cuentas_bancarias cb, rf_tc_cuentas_cheques cc, rf_tr_polizas p, rf_tc_tipos_polizas tp "
                                        + "where ch.cuenta_cheques_id = cc.cuenta_cheques_id and tp.tipo_poliza_id = p.tipo_poliza_id and cb.id_cuenta = cc.id_cuenta and ch.estatus in(0,1) and ch.poliza_id=p.poliza_id "
                                        + " and cc.unidad_ejecutora=" + lsUnidad + " and cc.entidad=" + lsEntidad + " and cc.ambito=" + lsAmbito + " and p.ejercicio=" + lsEjercicio
                                        + lsCondicion + " ) sqlresul ";
                                //out.println("query "+Query);     

                                String[] DefQuery = {Query, "100%", "LEFT", "2", ""};
                                String[] DefAlias = {"CONSFILA-,NOMBRE_CTA-,CONSECUTIVO-,CONSPOLIZA-,FECHACHEQUE-,IMPORTE-,REFERENCIA-,BENEFICIARIO-,ESTATUS-", "5%,17%,10%,9%,10%,10%,17%,17%,10%", "No.,Nombre de Cuenta,Cheque,Poliza Asociada,Fecha,Importe,Concepto,Beneficiario,Estatus", "4,4,4,4,4"};
                                String[] DefInput = {"CHEQUE_ID", "CHECKBOX", "ambitos", "LEFT"};

                                xPaginacion.SeleccionarHorizontalQuery(conexion, DefQuery, DefAlias, DefInput, out, 0, "", "", "", true);

                            } catch (Exception E) {
                                System.out.println("Error en " + E.getMessage());
                                System.out.println("Query: " + Query);
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
                        <!-- (Termina codigo JAVA) -->
                    </td>
                </tr>
            </table>
            <!-- Termina tabla para desplegar resultado del filtro utilizando el bean xPaginacion -->

            <table width='100%'>
                <tr><td width='73%'>&nbsp;</td>
                    <td width='10%'>
                        <INPUT type="hidden" name="txtfechaActual" value=<%=lsFechaActual%>>
                        <input type='submit' name='btnAceptar' value='Aceptar' class='boton'>
                    </td>
                    <td width='80%'>
                        <input type='button' name='btnRegresar' value='Regresar' class='boton' onClick="javascript:LlamaPagina('c707Filtro.jsp?','');">
                    </td></tr>
            </table>
        </FORM>
    </body>
</html>