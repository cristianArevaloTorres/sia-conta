<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page  import="java.util.*,java.sql.*"%>
<%@ page  import="sia.db.dao.*"%>
<%@ page  import="sia.beans.seguridad.*"%>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="Autentifica" class="sia.beans.seguridad.Autentifica" scope="session"/>  
<jsp:useBean id="xPaginacion" class="sia.utilerias.generador.hpaginacion" scope="page"/>
<jsp:useBean id="sbAutentifica" class="sia.beans.seguridad.Autentifica" scope="session" />  

<!-- (Inicia codigo JAVA) -->
<%

    Connection conexion = null;
    
    // Recuperacion de los parametros pasando mediante POST: 
    //         Unidad Ejecutora, Ambito, Entidad, Ejercicio, Fecha Actual
    //         y Catalogo de Cuentas
    String lsUnidad = request.getParameter("txtUnidad");
    String lsAmbito = request.getParameter("txtAmbito");
    String lsEntidad = request.getParameter("txtEntidad");
    String lsEjercicio = request.getParameter("txtEjercicio");
    String lsFechaActual = request.getParameter("txtfechaActual");
    String lsCatCuenta = request.getParameter("txtCatCuenta");

    String lsFecha01 = request.getParameter("txtFecha01");
    String lsFecha02 = request.getParameter("txtFecha02");
    String lsFecha03 = request.getParameter("txtFecha03");
    String lsFecha04 = request.getParameter("txtFecha04");
    //Numero de la poliza
    String lsNumPoli = request.getParameter("txtNumPoli");
    //Operacion de la poliza
    String lsOperacion = request.getParameter("txtOperacion");
    //Concepto de la poliza
    String lsConcepto = request.getParameter("txtConcepto");
    //Referencia general de la poliza
    String lsRefGral = request.getParameter("txtRefGral");

    sbAutentifica = (Autentifica) request.getSession().getAttribute("Autentifica");
    String numEmpleado = Integer.toString(sbAutentifica.getNumeroEmpleado());

%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
        <meta http-equiv="Cache-Control" content ="no-cache">
        <meta http-equiv="Pragma" content="no-cache"> 
        <meta http-equiv="expires" content="0" >

        <link rel="stylesheet" href="../../../Librerias/Estilos/siastyle.css" type='text/css'>

        <script language="JavaScript" src="../../../Librerias/Javascript/Capturador/funciones.js" type="text/javascript"></script>
        <script language="JavaScript" type='text/JavaScript'>
            function imprimir(banImprimir){
                resultado.txtOperacion.value = banImprimir; //0 = vista preliminar y 1 = Imprimir
                if (revisa())
                    resultado.submit();
            }
            
            // Valida que al menos un check box está seleccionado y si es así, confirmar su Revisión
            function revisa()
            { 
                var avanza = true;
                var checksOK = false;
                var tipoChek = typeof(document.resultado.AMBITOS)
                checksOK = verificaChecksSinSubmit(document.resultado, 'AMBITOS', 'Debe seleccionar al menos una poliza');
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
        <title>c725Resultado</title>
    </head>
    <body>

        <jsp:scriptlet>util.tituloPagina(out, "Contabilidad", " ", "Resultado de Polizas", "Eliminar", true);</jsp:scriptlet>    
            <br><br>
            <b>Fecha Actual</b> [<%=lsFechaActual%>]

        <br><br>
        <H2>Informaci&oacute;n de la P&oacute;liza: Unidad ejecutora=<%=lsUnidad%>, Entidad=<%=lsEntidad%>, Ambito=<%=lsAmbito%> y Ejercicio=<%=lsEjercicio%> </H2>
        <br>

        <FORM name="resultado" method="post"  action="c725Control.jsp">
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
                                //Conexion de a la base de datos de Contabilidad
                                conexion = DaoFactory.getContabilidad();
                                String lsCondicion = "";

                                //Construccion de la sentencia SQL - Numero de poliza
                                if (!lsNumPoli.equals("")) {
                                    lsCondicion = lsCondicion + " and t.numPoli = '" + lsNumPoli + "' ";
                                }
                                
                                //Construccion de la sentencia SQL - Tipo de operacion
                                if (!lsOperacion.equals("")) {
                                    lsCondicion = lsCondicion + " and t.numOper = '" + lsOperacion + "' ";
                                }

                                //Construccion de la sentencia SQL - Concepto
                                if (!lsConcepto.equals("")) {
                                    lsCondicion = lsCondicion + " and upper(t.conceppoli) like  upper('%" + lsConcepto + "%') ";
                                }

                                //Construccion de la sentencia SQL - Referencia general
                                if (!lsRefGral.equals("")) {
                                    lsCondicion = lsCondicion + " and upper(t.refgral) like  upper('%" + lsRefGral + "%') ";
                                }

                                //Construccion de la sentencia SQL - Fecha de poliza
                                if (!lsFecha01.equals("")) {
                                    lsCondicion = lsCondicion + " and to_date(to_char(t.fechPoli,'dd/MM/yyyy'),'dd/MM/yyyy')>=to_date('" + lsFecha01 + "','dd/MM/yyyy') and to_date(to_char(t.fechPoli,'dd/MM/yyyy'),'dd/MM/yyyy')<=to_date('" + lsFecha02 + "','dd/MM/yyyy') ";
                                }

                                if (!lsFecha03.equals("")) {
                                    lsCondicion = lsCondicion + " and to_date(to_char(t.fechAlta,'dd/MM/yyyy'),'dd/MM/yyyy')>=to_date('" + lsFecha03 + "','dd/MM/yyyy') and to_date(to_char(t.fechAlta,'dd/MM/yyyy'),'dd/MM/yyyy')<=to_date('" + lsFecha04 + "','dd/MM/yyyy') ";
                                }

                                //Construccion de la SQL final
                                Query = "select ROWNUM CONSFILA, sqlresul.* from "
                                    + "(select t.poliza_id, t.unidad, t.ambito, t.numpoli, t.numoper, to_char(t.fechpoli,'dd/mm/yyyy') fechpoli, t.conceppoli, to_char(t.fechalta,'dd/mm/yyyy') fechalta, t.refgral, t.fechadia "
                                    + "from rf_tr_polizas_carga t "
                                    + "where t.unidad=" + lsUnidad + " and t.ambito=" + lsEntidad + lsAmbito + " and t.ejercicio=" + lsEjercicio + " and t.estatus=0 and t.numEmpleado=" + numEmpleado
                                    + lsCondicion + " ) sqlresul order by 1";

                                System.out.println("Query: " + Query);

                                //System.out.println("Qry: " + Query);
                                String[] DefQuery = {Query, "100%", "LEFT", "2", ""};
                                String[] DefAlias = {"CONSFILA-,UNIDAD-,AMBITO-,NUMPOLI-,NUMOPER-,CONCEPPOLI-,REFGRAL-,FECHPOLI-,FECHALTA-", "5%,5%,5%,5%,5%,25%,25%,12%,13%", "No.,Unidad,Ambito,NumPoli,Operacion,Concepto,Referencia general,Fecha de aplicación Contable,Fecha de alta", "4,4,4,4,4"};
                                String[] DefInput = {"POLIZA_ID", "CHECKBOX", "ambitos", "LEFT"};


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
                                    // Cierra la conexion con la base de datos
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

                <tr><td width='60%'>&nbsp;</td>
                    <td width='30%'>
                        <INPUT type="hidden" name="txtfechaActual" value=<%=lsFechaActual%>>
                        <INPUT type="hidden" name="txtEjercicio" value=<%=lsEjercicio%>>
                        <INPUT type="hidden" name="txtOperacion" value="E">
                        <input type='button' name='btnAceptar' value='Eliminar poliza' class='boton' onclick="imprimir(1);">
                        <INPUT type="hidden" name="txtCatCuenta" value=<%=lsCatCuenta%>>
                        <INPUT type="hidden" name="txtUnidad" value=<%=lsUnidad%>>
                        <INPUT type="hidden" name="txtEntidad" value=<%=lsEntidad%>>
                        <INPUT type="hidden" name="txtAmbito" value=<%=lsAmbito%>>
                        <INPUT type="hidden" name="txtEjercicio" value=<%=lsEjercicio%>>       
                    </td>
                    <td width='10%'>
                        <input type='button' name='btnRegresar' value='Regresar' class='boton' onClick="window.open('../filtroGeneral.jsp?opcion=eliminarCargaExcel&idCatalogoCuenta=1','_self');" >
                    </td></tr>
            </table>
        </FORM>
    </body>
</html>