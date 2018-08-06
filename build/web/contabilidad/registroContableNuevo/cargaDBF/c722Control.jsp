<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page  import="java.sql.*"%>
<%@ page  import="sia.db.dao.*"%>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="pbEnvia" class="sia.rf.contabilidad.registroContableNuevo.bcEnviaPolizaExc" scope="page"/> 
<jsp:useBean id="pbPolizaCarga" class="sia.rf.contabilidad.registroContableNuevo.bcPolizaCarga" scope="page"/>  
<jsp:useBean id="pbFechaHoy" class="sun.jdbc.rowset.CachedRowSet" scope="page">
    <% pbFechaHoy.setCommand("select to_Char(sysdate,'dd/mm/yyyy hh24:mi:ss') fechaActual from dual");%>
</jsp:useBean> 
<jsp:useBean id="pbEstadoCat" class = "sia.rf.contabilidad.registroContableEvento.bcEstadoCatalogo" scope="page"/>

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
    String lsPolizaId = "";
%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
        <link rel="stylesheet" href="../../../Librerias/Estilos/siastyle.css" type='text/css'>

        <script language="JavaScript" src="../../../Librerias/Javascript/Capturador/funciones.js" type="text/javascript"></script>

        <title>c706Resultado</title>
    </head>
    <body>

        <jsp:scriptlet>util.tituloPagina(out, "Contabilidad", " ", "Control de polizas", "Aplicar", true);</jsp:scriptlet>    

            <br><br>
            <b>Fecha Actual</b> [<%=lsFechaActual%>]

        <br><br>

        <H2>Informaci&oacute;n de la P&oacute;liza: Unidad ejecutora=<%=lsUnidad%>, Entidad=<%=lsEntidad%>, Ambito=<%=lsAmbito%> y Ejercicio=<%=lsEjercicio%> </H2>

        <br>

        <FORM name="control" method="post"  action="">
            <br><br><br>


            <!-- Inicia tabla para desplegar resultado del filtro utilizando el bean xPaginacion -->
            <table width="100%" align="center" class="general" name="tAmbitos" id="tAmbitos">
                <tr>
                    <td align="center">

                        <!-- (Inicia codigo JAVA) -->
                        <%


                            try {

                                // Conexion a la bd de Contabilidad
                                conexion = DaoFactory.getContabilidad();

                                // Recuperacion del arreglo de ambitos seleccionados
                                String[] lstPolizas = request.getParameterValues("AMBITOS");
                                String resultado = "";
                                conexion.setAutoCommit(false);
                                //conexion=DaoFactory.getContabilidad();
                                String estatus = "";
                                String descripcion = "";
                                String mensajeError = "";

                                // Recorrido por cada unos de los elementos del arreglo de ambitos
                                for (int i = 0; i < lstPolizas.length; i++) {
                                    // Extraccion del ID de la poliza
                                    lsPolizaId = lstPolizas[i].substring(0, lstPolizas[i].length() - 1);
                                    pbPolizaCarga.select_rf_tr_polizas_carga(conexion, lsPolizaId);
                                    try {
                                        mensajeError = "";
                                        // Verifica si no hay cierre definitivo justamente antes de hacer el commit a la poliza
                                        pbEstadoCat.select_rf_tc_estado_catalogo(conexion);
                                        //Obtiene el estado actual del Catalogo
                                        estatus = pbEstadoCat.getEstatus();
                                        //Obtiene el catalogo de descripcion
                                        descripcion = pbEstadoCat.getDescripcion();
                                        if (estatus.equals("1")) {
                                            resultado = pbEnvia.procesa(conexion, lsPolizaId, lsCatCuenta, "0001");
                                            conexion.commit();
                                        } else {
                                            resultado = "C";
                                            conexion.commit();  // Para terminar la transaccion en falso
                                        }
                                    } catch (Exception E) {
                                        resultado = "";
                                        conexion.rollback();
                                        //mensajeError =  E.getMessage().substring(E.getMessage().lastIndexOf(":"), E.getMessage().length() ) + " y ";
                                        mensajeError = E.getMessage();
                                    }

                                    if (resultado.equals("C")) {
                        %>       
                        <p>La poliza <b><%=pbPolizaCarga.getNumPoli()%></b> con fecha de aplicacion contable del <b><%=pbPolizaCarga.getFechPoli()%></b>,  NO fue aplicada, ya que hay un proceso de <b><%=descripcion%></b> en SIA CONTABILIDAD</p>       
                        <%
                        } else if (!resultado.equals("")) {

                        %>
                        <p>La poliza <b><%=pbPolizaCarga.getNumPoli()%></b> con fecha de aplicacion contable del <b><%=pbPolizaCarga.getFechPoli()%></b>,  fue aplicada correctamente y se genero la poliza <b><%=resultado%></b> en SIA CONTABILIDAD</p>
                        <%
                        } else {
                        %>
                        <p>La poliza <b><%=pbPolizaCarga.getNumPoli()%></b> de la carga <b>NO fue aplicada</b> por el siguiente motivo <b><%=mensajeError%></b> vuelva a intentarlo, si el problema persiste comuniquese con el administrador.</p>
                        <%
                                }
                            }
                        } catch (Exception E) {
 
                            conexion.rollback();
                            System.out.println("Error en " + E.getMessage());

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
                    </td>
                    <td width='80%'>
                        <input type='botton' name='btnRegresar' value='Regresar' class='boton' onClick="javascript:history.go(-2);" >
                    </td></tr>
            </table>
        </FORM>
    </body>
</html>