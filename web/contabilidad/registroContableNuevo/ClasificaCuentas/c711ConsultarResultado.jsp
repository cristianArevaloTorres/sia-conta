<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page  import="java.util.*,java.sql.*"%>
<%@ page  import="sia.db.dao.*"%>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>  
<jsp:useBean id="xPaginacion" class="sia.utilerias.generador.hpaginacion" scope="page"/>
<jsp:useBean id="controlRegistro" class ="sia.rf.contabilidad.registroContable.acciones.ControlRegistro" scope="session" />
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
    <title>c711ConsultarResultado</title>
    <link rel="stylesheet" href="../../../Librerias/Estilos/siastyle.css" type='text/css'>
    <script language="JavaScript" src="../../../Librerias/Javascript/Capturador/funciones.js" type="text/javascript"></script>
  </head>
  <body>

<jsp:scriptlet>util.tituloPagina(out, "Contabilidad", "Clasificador de Cuentas", "[Consultar]","Resultado", true);</jsp:scriptlet>

<!-- Encabezado de la tabla con resultado del filtro -->
 <table  width="100%" border="0">
    <tr> 
      <td width="390" height="16"> <div align="left"><strong>Fecha actual:</strong> 
      [ <%=sia.libs.formato.Fecha.formatear(sia.libs.formato.Fecha.FECHA_CORTA,controlRegistro.getFechaAfectacion())%> ]</div></td>
      <td width="625">&nbsp; </td>
    </tr>
 </table>
 
<FORM name="resultado" id="resultado" Method="post" action="">
<!-- (Inicia codigo JAVA) -->
<%
 // Connection conexion=null;
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
        lsNat="Deudora";
    else
        if (lsNaturaleza.equals("02"))
            lsNat="Acreedora";
        else
            lsNaturaleza="";
%>

<!-- Inicia tabla para desplegar resultado del filtro utilizando el bean xPaginacion -->
<table width="100%" align="center" class="general">
<tr>
<td>

<!-- (Inicia codigo JAVA) -->
<%  

 //String Query="";
 //try{
   // conexion=DaoFactory.getContabilidad();

    // conforma la condición de la consulta de acuerdo a los parametros que escogio en filtro
    if(!lsCtaMayorId.equals("null"))
        lsCondicion=" AND Cuenta_Mayor_Id = "+lsCtaMayorId;

    if(!lsNoCuenta.equals(""))
       lsCondicion = lsCondicion+" AND Cuenta_Mayor = '"+lsNoCuenta+"'";
						
    if(!lsDescrip.equals(""))
           lsCondicion = lsCondicion+" AND Descripcion LIKE '%"+lsDescrip+"%' ";
					
    if(!lsNaturaleza.equals(""))
           lsCondicion = lsCondicion+" AND Naturaleza = '"+lsNat+"'";
        
    // Listar las cuentas de RF_TC_CLASIFICADOR_CUENTAS que ya estan configuradas.
    Query = "SELECT * FROM (SELECT RF_TC_CLASIFICADOR_CUENTAS.CUENTA_MAYOR_ID, RF_TC_CLASIFICADOR_CUENTAS.CUENTA_MAYOR, "+
            " RF_TR_CONFIGURA_CLAVES.ID_CONF_CVE, RF_TC_CLASIFICADOR_CUENTAS.DESCRIPCION, "+
            " to_char(RF_TC_CLASIFICADOR_CUENTAS.fecha_vig_ini,'DD/MM/YYYY') as Fecha_Inicial,to_char(RF_TC_CLASIFICADOR_CUENTAS.fecha_vig_fin,'DD/MM/YYYY') as Fecha_Final, "+
            " decode(naturaleza,'A','Acreedora','D','Deudora') as naturaleza "+
            " FROM RF_TC_CLASIFICADOR_CUENTAS LEFT OUTER JOIN "+
            " RF_TR_CONFIGURA_CLAVES ON RF_TC_CLASIFICADOR_CUENTAS.CONF_CVE_CTA_CONT_ID = RF_TR_CONFIGURA_CLAVES.ID_CONF_CVE) a "+
            " WHERE (ID_CONF_CVE <> 0) "+lsCondicion+" ORDER BY CUENTA_MAYOR ";
/*        
/*          Query = " SELECT RF_TC_CLASIFICADOR_CUENTAS.CUENTA_MAYOR_ID, RF_TC_CLASIFICADOR_CUENTAS.CUENTA_MAYOR, "+
                  " RF_TC_CLASIFICADOR_CUENTAS.DESCRIPCION " +
                  " RF_TC_CLASIFICADOR_CUENTAS.fecha_vig_ini as Fecha_Inicial,RF_TC_CLASIFICADOR_CUENTAS.fecha_vig_fin as Fecha_Final, "+
                  " decode(naturaleza,'A','Acreedora','D','Deudora') as naturaleza "+                  
                  " FROM RF_TC_CLASIFICADOR_CUENTAS " +
                  " WHERE conf_cve_cta_cont_id <> -1 OR conf_cve_cta_cont_id <> NULL " +lsCondicion+
                  " ORDER BY CUENTA_MAYOR ";
*/    
//        Query = "SELECT cuenta_mayor,descripcion,fecha_vig_ini AS Fecha_Inicial,fecha_vig_fin AS Fecha_Final,decode(naturaleza,'A','Acreedora','D','Deudora') as naturaleza"+ 
//                " FROM RF_TC_CLASIFICADOR_CUENTAS "+lsCondicion;

//System.out.println("Query : "+Query);
   
      String [] DefQuery={Query,"100%","LEFT","2",""};
      String [] DefAlias={"CUENTA_MAYOR-,DESCRIPCION-,NATURALEZA-,FECHA_INICIAL-,FECHA_FINAL-","10%,50%,10%,15%,15%","Número de Cuenta,descripción,Naturaleza,Fecha inicial,Fecha Final","4,4,4,4,4"};
      String [] DefInput={"CUENTA_MAYOR","CHECKBOX","ambitos","LEFT"};
      
      xPaginacion.SeleccionarHorizontalQuery(conexion, DefQuery, DefAlias, DefInput, out, 1, "<a href=c711ConsultarFormulario.jsp","?NCta=", ">", false);

}
catch(Exception E)
   {
   System.out.println("Query: "+Query);
   System.out.println("Error en "+E.getMessage()); 
 
%>
     <p>Ha ocurrido un error al accesar la Base de Datos,</p>
     <p>favor de reportarlo al Administrador del Sistema.</p>
     <p>Gracias.</p>
<%
   }
   finally{ 
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

    <table width="100%">
        <tr> 
            <td width="51%" align=right>&nbsp; </td>
            <td width="49%" align=right>&nbsp;</td>
        </tr>
        <tr> 
            <td colspan="2" align=right>
                <div align="center"> 
                    <input type="button" name="btnRegresar" value="Regresar" class="boton" onClick="javascript:LlamaPagina('c711ConsultarFiltro.jsp','');">
                </div>
            </td>
        </tr>
    </table>
  </FORM>
  </body>
</html>