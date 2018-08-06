<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page  import="java.util.*,java.sql.*"%>
<%@ page  import="sia.db.dao.*"%>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>  
<jsp:useBean id="xPaginacion" class="sia.utilerias.generador.hpaginacion" scope="page"/>
<jsp:useBean id="controlRegistro" class="sia.rf.contabilidad.registroContable.acciones.ControlRegistro" scope="session" />
<jsp:useBean id="fechaAfectacion" class="sia.rf.contabilidad.registroContable.servicios.FechaAfectacion" scope="page"/>
<%
    
    if ( controlRegistro.getFechaAfectacion()==null){
       fechaAfectacion.establecerFechaAfectacion(request,fechaAfectacion.obtenerFechaAfectacion());           
    }

    String Query="";
    Connection conexion=null;
    try{
           conexion=DaoFactory.getContabilidad();      
%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>c711EliminarResultado</title>
    <link rel="stylesheet" href="../../../Librerias/Estilos/siastyle.css" type='text/css'>
    <script language="JavaScript" src="../../../Librerias/Javascript/Capturador/funciones.js" type="text/javascript"></script>
    
    <script type='text/JavaScript' language='javascript'>
        function revisa() { // Valida que al menos un check box está seleccionado y si es así, confirmar su Eliminacón
            var avanza = true;
            var checksOK = false;
            
            checksOK = verificaChecksSinSubmit(document.resultado, 'AMBITOS', 'Debe seleccionar al menos una cuenta');
            if (checksOK) {  //Al menos un check box esta seleccionado
                var borrar= confirm('¿Está seguro de eliminar la configuración de las cuentas ?');
                if (borrar == false){
                    avanza = false;
                }
            } else {        //Ningún check box esta seleccionado
                    avanza=false;
            }
            if (avanza) resultado.btnAceptar.disabled=true;
                return avanza;
        }//function
   </script>
  </head>
  <body>
        <jsp:scriptlet>util.tituloPagina(out, "Contabilidad", "Clasificador de Cuentas", "[Eliminar]","Resultado", true);</jsp:scriptlet>

<!-- Encabezado de la tabla con resultado del filtro -->
        <table  width="100%" border="0">
            <tr> 
                <td width="390" height="32"> <div align="left"><strong>Fecha actual:</strong> 
                        [ <%=sia.libs.formato.Fecha.formatear(sia.libs.formato.Fecha.FECHA_CORTA,controlRegistro.getFechaAfectacion())%> ]</div></td>
                <td width="625">&nbsp; </td>
            </tr>
        </table>

        <FORM name="resultado" id="resultado" Method="post" action="c711Control.jsp" onsubmit='return revisa();'>
            <table width='100%' align='center' class='general'  id='tAmbitos' name='tAmbitos'>
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
//        Connection conexion=null;
        String lsCtaMayorId="";
        String lsNoCuenta="";
        String lsDescrip="";  
        String lsNaturaleza="";
        String lsNat ="";
        String lsCondicion="";

        // obtención de los valores de la página de filtro.
        lsCtaMayorId=request.getParameter("lstCtasMayor");
        lsNoCuenta= request.getParameter("txtNumero");
        lsDescrip=request.getParameter("txtNombre");
        lsNaturaleza=request.getParameter("lstNaturaleza");

        //determinar naturaleza de la cuenta para la condición de busqueda.
        if (lsNaturaleza.equals("01"))
            lsNat="D";
        else
            if (lsNaturaleza.equals("02"))
                lsNat="A";
            else
                lsNaturaleza="";
%>

<!-- Inicia tabla para desplegar resultado del filtro utilizando el bean xPaginacion -->
        <table width="100%" align="center" class="general">
            <tr>
                <td>
<!-- (Inicia codigo JAVA) -->
<%  
//                 String Query="";
//                 try{
//                      conexion=DaoFactory.getContabilidad();

    // conforma la condición de la consulta de acuerdo a los parametros que escogio en filtro
                     if(!lsCtaMayorId.equals("null"))
                        lsCondicion=" AND RF_TC_CLASIFICADOR_CUENTAS.Cuenta_Mayor_Id = "+lsCtaMayorId;

                     if(!lsNoCuenta.equals(""))
                        lsCondicion = lsCondicion+" AND RF_TC_CLASIFICADOR_CUENTAS.Cuenta_Mayor = '"+lsNoCuenta+"'";
						
                     if(!lsDescrip.equals(""))
                        lsCondicion = lsCondicion+" AND RF_TC_CLASIFICADOR_CUENTAS.Descripcion LIKE '%"+lsDescrip+"%' ";
					
                     if(!lsNaturaleza.equals(""))
                        lsCondicion = lsCondicion+" AND RF_TC_CLASIFICADOR_CUENTAS.Naturaleza = '"+lsNat+"'";
        
    // Listar las cuentas de RF_TC_CLASIFICADOR_CUENTAS que ya estan configuradas.
                     Query = "SELECT DISTINCT RF_TC_CLASIFICADOR_CUENTAS.CUENTA_MAYOR_ID, RF_TC_CLASIFICADOR_CUENTAS.CUENTA_MAYOR,RF_TR_CONFIGURA_CLAVES.ID_CONF_CVE, "+
                             " RF_TC_CLASIFICADOR_CUENTAS.DESCRIPCION, DECODE(RF_TC_CLASIFICADOR_CUENTAS.NATURALEZA,'A','Acreedora','D','Deudora') as naturaleza, "+
                             " to_char(RF_TC_CLASIFICADOR_CUENTAS.FECHA_VIG_INI, 'DD/MM/YYYY') as Fecha_Inicial, "+
                             " to_char(RF_TC_CLASIFICADOR_CUENTAS.FECHA_VIG_FIN, 'DD/MM/YYYY' )as Fecha_Final "+
                             " FROM RF_TR_CONFIGURA_CLAVES INNER JOIN "+
                             " RF_TC_CLASIFICADOR_CUENTAS ON RF_TR_CONFIGURA_CLAVES.ID_CONF_CVE = RF_TC_CLASIFICADOR_CUENTAS.CONF_CVE_CTA_CONT_ID LEFT OUTER JOIN "+
                             " RF_TR_CUENTAS_CONTABLES ON RF_TC_CLASIFICADOR_CUENTAS.CUENTA_MAYOR_ID = RF_TR_CUENTAS_CONTABLES.CUENTA_MAYOR_ID "+
                             " WHERE (RF_TR_CUENTAS_CONTABLES.CUENTA_MAYOR_ID IS NULL and RF_TC_CLASIFICADOR_CUENTAS.CONF_CVE_CTA_CONT_ID <>  0 ) "+lsCondicion+" ORDER BY CUENTA_MAYOR ";

//System.out.println("Query : "+Query);
   
                     String [] DefQuery={Query,"100%","LEFT","2",""};
                     String [] DefAlias={"CUENTA_MAYOR-,DESCRIPCION-,NATURALEZA-,FECHA_INICIAL-,FECHA_FINAL-","10%,50%,10%,15%,15%","Número de Cuenta,descripción,Naturaleza,Fecha inicial,Fecha Final","4,4,4,4,4"};
                     String [] DefInput={"CUENTA_MAYOR","CHECKBOX","ambitos","LEFT"};
      
                     xPaginacion.SeleccionarHorizontalQuery(conexion, DefQuery, DefAlias, DefInput, out, 1, "<a href=c711ConsultarFormulario.jsp","?NCta=", ">", true);

                 } catch(Exception E) {
                         System.out.println("Query: "+Query);
                         System.out.println("Error en "+E.getMessage()); 
    
%>
                         <p>Ha ocurrido un error al accesar la Base de Datos,</p>
                         <p>favor de reportarlo al Administrador del Sistema.</p>
                         <p>Gracias.</p>
<%
                 } finally { 
                         if (conexion!=null){
                             conexion.close();
                             conexion=null;
                         }
                }
%>
<!-- (Termina codigo JAVA) -->
                </td>
            </tr>
        </table>
<!-- Termina tabla para desplegar resultado del filtro utilizando el bean xPaginacion -->

        <table width="100%" border="0">
            <tr> 
                <td width="49%" align=right>
                    <input type="submit" name="btnAceptar" value="Aceptar" class="boton" ></td>
                <td width="52%" align=left>
                    <input type="button" name="btnRegresar" value="Regresar" class="boton" onClick="javascript:LlamaPagina('c711EliminarFiltro.jsp','');">
                </td>
            </tr>
        </table>
        <input type="hidden" name="txtOpcion" value="E"  id="txtOpcion">
    </FORM>
  </body>
</html>