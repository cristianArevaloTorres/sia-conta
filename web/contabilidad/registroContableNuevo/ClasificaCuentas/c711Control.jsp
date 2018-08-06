<%@ page contentType="text/html; charset=ISO-8859-1" %>
<%@ page  import="java.util.*,java.sql.*"%>
<%@ page  import="sia.db.dao.*"%>
<%@ page  import="sia.beans.seguridad.*"%>

<html>
    <head>
        <meta http-equiv="Content-Language" content="es-mx">
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
        <title>c711Control</title>
        <link rel="stylesheet" href="../../../Librerias/Estilos/siaexplorer.css" type='text/css'>
    </head>
    <script language="JavaScript" src="../../../Librerias/Javascript/Capturador/funciones.js" type="text/javascript"></script>
    <script language="JavaScript" src="../../../Librerias/Javascript/Capturador/fcgencValida2.js" type="text/javascript"></script>
    
    <jsp:useBean id="Autentifica"  class="sia.beans.seguridad.Autentifica" scope="session"/>  
    <jsp:useBean id="pbClasificaCuentas" class="sia.rf.contabilidad.registroContableNuevo.bcClasificadorCuentas" scope="page"/>
    <jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>  
    <jsp:useBean id="controlRegistro" class="sia.rf.contabilidad.registroContable.acciones.ControlRegistro" scope="session" />
    <jsp:useBean id="fechaAfectacion" class="sia.rf.contabilidad.registroContable.servicios.FechaAfectacion" scope="page"/>

    <body  topmargin=1 leftmargin=10>
        <jsp:scriptlet>util.tituloPagina(out, "Contabilidad", "Clasificador de Cuentas", "[Actualizar]","Control", true);</jsp:scriptlet>

        <table  width="100%" border="0">
            <tr> 
                <td width="390" height="32"> <div align="left"><strong>Fecha actual:</strong> 
                        [ <%=sia.libs.formato.Fecha.formatear(sia.libs.formato.Fecha.FECHA_CORTA,controlRegistro.getFechaAfectacion())%> ]</div></td>
                <td width="625">&nbsp; </td>
            </tr>
        </table>
        
        <FORM Method="post">


<%!//Función que graba manda valores a las propiedades del bean bcClasificadorCuentas para RF_TC_CONFIGURA_CLAVES
 public void grabaConfig_Cves(sia.rf.contabilidad.registroContableNuevo.bcClasificadorCuentas pbClasCtas,String pDes,String pFVI,String pFVF,String pLong,String pAju,String pCar,String pNivO) {   
          pbClasCtas.setDescripcion(pDes);
          pbClasCtas.setFecha_vig_ini(pFVI);
          pbClasCtas.setFecha_vig_fin(pFVF);
          pbClasCtas.setLongitud(pLong);
          pbClasCtas.setAjuste(pAju);
          pbClasCtas.setCaracter(pCar);
          pbClasCtas.setNivel_operacion(pNivO);
 }
 
 //Función que graba manda valores a las propiedades del bean bcClasificadorCuentas para RF_TR_DETALLE_CONF_CVE
 public void grabaDetalle_Cfg_Cve(sia.rf.contabilidad.registroContableNuevo.bcClasificadorCuentas pbClasCtas,String pOrd,String pICC,String pTam,String pPos,String pAgr,String pNCI,String pCod) {   
          pbClasCtas.setOrden(pOrd);
          pbClasCtas.setId_conf_cve(pICC);
          pbClasCtas.setTamanio(pTam);
          pbClasCtas.setPosicion(pPos);
          pbClasCtas.setAgrupar(pAgr);
          pbClasCtas.setNivel_cta_id(pNCI);
          pbClasCtas.setCodigo(pCod);
 } %>

<%

  Connection conexion=null;
  // Para obtener el Max de cve
  ResultSet rsQuery=null;
  Statement statementAux =null;
  StringBuffer SQL=new StringBuffer("");
  
  ResultSet rsQuery1=null;
  Statement statementAux1 =null;
  StringBuffer SQL1=new StringBuffer("");
  
  String opcion = "";
  String lsIdConfCve="";
  String lsMensaje="";

try{
    conexion=DaoFactory.getContabilidad();
    conexion.setAutoCommit(false);

    opcion = request.getParameter("txtOpcion");
    String lsNCta       = request.getParameter("txtCtaMayor");
    String lsDescrip   = request.getParameter("txtDescripcion"); 
    String lsFecVigI   = request.getParameter("txtFecIni");
    String lsFecVigF = request.getParameter("txtFecFin");  
    String lsLong       = request.getParameter("txtLong");   
    String lsNiv          = request.getParameter("txtNivel");

    String[] lsTamanio ={""};
    String[] lsCodigos  ={""};

    lsTamanio  = request.getParameterValues("txtTamanio");
/*    System.out.println("lsTamanio = "+lsTamanio.length);
    
    for(int i=0; i  < lsTamanio.length; i++)
          System.out.println("lsTamanio("+i+ ") = "+lsTamanio[i]);
*/

    lsCodigos = request.getParameterValues("txtCodigo");
    if(opcion.equals("A")){  //if de Agregar
        //Obtener el valor de ID_CONF_CVE que se acaba de generar para grabar el detalle en RF_TR_DETALLE_CONF_CVE
        grabaConfig_Cves(pbClasificaCuentas,lsDescrip,lsFecVigI,lsFecVigF,lsLong,"1","0",lsNiv);
        pbClasificaCuentas.insert_RF_TR_CONFIGURA_CLAVES(conexion);
        lsIdConfCve = pbClasificaCuentas.getId_conf_cve();
        
        //grabar detalle de Configuracion de claves en RF_TR_DETALLE_CONF_CVE
        if(lsTamanio != null){
            int Pos=1;
            // graba cuenta de Mayor
            String lsPos= String.valueOf(Pos);
            grabaDetalle_Cfg_Cve(pbClasificaCuentas,"1",lsIdConfCve, "4", lsPos,"0","1",lsNCta );
            pbClasificaCuentas.insert_RF_TR_DETALLE_CONF_CVE(conexion);
            Pos=Pos+4;

            for(int i=0; i < lsTamanio.length; i++ ){
                  lsPos= String.valueOf(Pos);
                  grabaDetalle_Cfg_Cve(pbClasificaCuentas, Integer.toString(i+2), lsIdConfCve, lsTamanio[i],lsPos,"0","2", lsCodigos[i]);
                  Pos=Pos + Integer.parseInt(lsTamanio[i]);
                  pbClasificaCuentas.insert_RF_TR_DETALLE_CONF_CVE(conexion);
            } //for
        }  //if(lsTamanio)
         pbClasificaCuentas.update_Clasificador_ctas(conexion,lsNCta,lsIdConfCve);
        lsMensaje="Se grabo la cuenta "+lsNCta+" "+lsDescrip+" correctamente <br> ";
    } //if(A)
    
    if(opcion.equals("M")) { //if de Actualizar    
       //Obtener el Id_Conf_Cve correspondiente a la cuenta de mayor seleccionado
        pbClasificaCuentas.select_RF_TC_CLASIFICADOR_CUENTAS(conexion,lsNCta);
        lsIdConfCve = pbClasificaCuentas.getConf_cve_cta_cont_id();
        //posicionarse en el registro de select_RF_TR_CONFIGURA_CLAVES
        pbClasificaCuentas.select_RF_TR_CONFIGURA_CLAVES_CfgCve(conexion,lsIdConfCve);
        //modificar los datos a actualizar
        pbClasificaCuentas.setLongitud(lsLong);
        pbClasificaCuentas.setNivel_operacion(lsNiv);
        pbClasificaCuentas.setAjuste("1");
        pbClasificaCuentas.setCaracter("0");

        pbClasificaCuentas.update_RF_TR_CONFIGURA_CLAVES(conexion,lsIdConfCve);
        //Elimina los registros de detalle de RF_TR_DETALLE_CONF_CVE
        pbClasificaCuentas.delete_RF_TR_DETALLE_CONF_CVE(conexion,lsIdConfCve);
        //grabar detalle de Configuracion de claves en RF_TR_DETALLE_CONF_CVE
        if(lsTamanio != null){
            int Pos=1;
            // graba cuenta de Mayor
            String lsPos= String.valueOf(Pos);
            grabaDetalle_Cfg_Cve(pbClasificaCuentas,"1",lsIdConfCve, "4", lsPos,"0","1",lsNCta );
            pbClasificaCuentas.insert_RF_TR_DETALLE_CONF_CVE(conexion);
            Pos=Pos+4;
            for(int i=0; i < lsTamanio.length; i++ ){
                  lsPos= String.valueOf(Pos);
                  grabaDetalle_Cfg_Cve(pbClasificaCuentas, Integer.toString(i+2), lsIdConfCve, lsTamanio[i],lsPos,"0","2", lsCodigos[i]);
                  Pos=Pos + Integer.parseInt(lsTamanio[i]);
                  pbClasificaCuentas.insert_RF_TR_DETALLE_CONF_CVE(conexion);
            } //for
       }  //if(lsTamanio)
       lsMensaje="Se modifico la cuenta "+lsNCta+" "+lsDescrip+" correctamente <br> ";
    }//if(M)
    
    if(opcion.equals("E")) { //if de eliminar
       String[] lsIdCong_Cve=request.getParameterValues("AMBITOS");
       String lsCveC="";
       String lsDes="";
       
               for(int x= 0; x< lsIdCong_Cve.length; x++) {        
                   lsCveC=lsIdCong_Cve[x];
                   lsCveC=lsIdCong_Cve[x].substring(0,lsIdCong_Cve[x].length()-1);
                  //Obtener el Id_Conf_Cve correspondiente a la cuenta de mayor seleccionado
                   pbClasificaCuentas.select_RF_TC_CLASIFICADOR_CUENTAS(conexion,lsCveC);
                   lsIdConfCve = pbClasificaCuentas.getConf_cve_cta_cont_id();
                   lsDes=pbClasificaCuentas.getDescripcion();
                   //elimina el registro de la tabla RF_TR_CONFIGURA_CLAVES  y todos los registros de las tabla RF_TR_CONF_CVE
                   pbClasificaCuentas.delete_RF_TR_DETALLE_CONF_CVE(conexion,lsIdConfCve);
                   pbClasificaCuentas.update_Clasificador_ctas(conexion,lsCveC,"0");
                   pbClasificaCuentas.delete_RF_TR_CONFIGURA_CLAVE(conexion,lsIdConfCve);
                }//for 
                lsMensaje="Se elimino la clasificación de la cuenta "+lsCveC+" "+lsDes+" correctamente  y así sucesivamente con todas las cuentas que se hayan seleccionado para eliminar <br> ";
    } //termina if de eliminar

%>
     <p> <font color='#003399'><%=lsMensaje%></font></p>
     <br><br><br>
<%
          conexion.commit();   
}
catch(Exception E){
    conexion.rollback(); 
    System.out.println("Error en pagina en "+E.getMessage()); 
%>
     <p>Ha ocurrido un error al accesar la Base de Datos,</p>
     <p>favor de reportarlo al Administrador del Sistema.</p>
     <p>Gracias.</p>
<%
}
finally{
   if (conexion != null){
       conexion.close();
       conexion=null;
   }  
   if (rsQuery != null){
      rsQuery.close();
      rsQuery=null;
   }
   if (statementAux != null){
       statementAux.close();
       statementAux=null;
   }           
}
         if(opcion.equals("A")){
%>
            <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
                <thead>           
                    <tr>
                        <td>
                            <div align="center">
                                <input type="button" name="btnAceptar " value=" Aceptar" class='boton' onClick="javascript:LlamaPagina('c711AgregarFiltro.jsp','');">
                            </div>
                        </td>
                        <td width="5">&nbsp;</td>
                   	<td>&nbsp;</td>
                    </tr>
                </thead>           
            </table>
<%
        }
        if(opcion.equals("M")){
%>
            <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
                <thead>           
                    <tr>
                        <td>
                            <div align="center">
                                <input type="button" name="btnAceptar " value=" Aceptar" class='boton' onClick="javascript:LlamaPagina('c711ModificarFiltro.jsp','');">
                            </div>
                        </td>
                        <td width="5">&nbsp;</td>
                   	<td>&nbsp;</td>
                    </tr>
                </thead>           
            </table>
<%
        }
        if(opcion.equals("E")){
%>
            <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
                <thead>           
                    <tr>
                        <td>
                            <div align="center">
                                <input type="button" name="btnAceptar " value=" Aceptar" class='boton' onClick="javascript:LlamaPagina('c711EliminarFiltro.jsp','');">
                            </div>
                        </td>
                        <td width="5">&nbsp;</td>
                   	<td>&nbsp;</td>
                    </tr>
                </thead>           
            </table>
<%
          }
%>
</FORM>
</body>
</html>