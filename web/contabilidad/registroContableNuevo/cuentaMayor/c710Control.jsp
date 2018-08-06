<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="java.util.*,java.sql.*,java.io.*" %>
<%@ page import="sia.db.dao.*"%>
<%@ page import="sun.jdbc.rowset.CachedRowSet" %>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>   
<jsp:useBean id="controlRegistro" class="sia.rf.contabilidad.registroContable.acciones.ControlRegistro" scope="session" />
<jsp:useBean id="fechaAfectacion" class="sia.rf.contabilidad.registroContable.servicios.FechaAfectacion" scope="page"/>
<%
    if ( controlRegistro.getFechaAfectacion()==null){
       fechaAfectacion.establecerFechaAfectacion(request,fechaAfectacion.obtenerFechaAfectacion());           
    }        
%>
<jsp:useBean id="pbCuentas" class = "sia.rf.contabilidad.registroContableNuevo.bcClasificadorCuentas" scope="page"/>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
<title>Contabilidad - Cuentas de Mayor - Modificar</title>
<link rel="stylesheet" href="../../../Librerias/Estilos/siastyle.css" type='text/css'>

<script language="JavaScript" src="../../../Librerias/Javascript/Capturador/funciones.js" type="text/javascript"></script>
<script language="JavaScript" src="../../../Librerias/Javascript/Capturador/fcgencValida2.js" type="text/javascript"></script>
<script language="JavaScript" src="../../../Librerias/Javascript/Capturador/calendar.js" type="text/javascript"></script>
<script src="../../../Librerias/Javascript/Capturador/dynapi/src/dynapi.js" type="text/javascript"></script>
<script src="../../../Librerias/Javascript/Capturador/almacen.js" type="text/javascript"></script>
<script type="text/javascript" src="../../../Librerias/Javascript/jquery.js"></script>
<script type="text/javascript" src="../../../Librerias/Javascript/jquery.meio.mask.js" charset="utf-8"></script>
<script type="text/javascript" language="Javascript"> 

</script> 
</head>  
  
<body>
<jsp:scriptlet>util.tituloPagina(out, "Contabilidad", "", "Cuentas de Mayor", "Control", true);</jsp:scriptlet>
<FORM Method="post" name="Filtro" action="" >
<%
   Connection conexion=null;
   boolean bandArch=false;
   
   String lsId,lsGenero,lsGrupo,lsClase,lsNombre,lsNatura,lsFecha01,lsFecha02,lsFechaReg;
   String lsConfId,lsConfCont,lsConfCve,lsConfCant,lsFecha,lsIdentifica;   
   String lsOpcion ="";
   String cveCta="";   
   String lsCuenta="";
   String lsDescripcion="";
  
   Vector temporal;   //para mostrar los registros que se eliminan
   temporal=new Vector();
   
   lsFecha=sia.libs.formato.Fecha.formatear(sia.libs.formato.Fecha.FECHA_CORTA,controlRegistro.getFechaAfectacion());
   
   try {
       conexion=DaoFactory.getContabilidad();    
       conexion.setAutoCommit(false);        
       CachedRowSet crsCuentas = null;
       lsOpcion=request.getParameter("hOpcion");              
      
        if (lsOpcion.equals("A")){
            lsConfCve="0"; 
            lsConfCant="0";            
            lsCuenta=request.getParameter("txtCve"); 
            lsNombre=request.getParameter("txtNombre");
            lsNatura=request.getParameter("btrNatura");
            lsFecha01=request.getParameter("txtFecha01");
            lsFecha02=request.getParameter("txtFecha02");
            lsClase=request.getParameter("lstClase");
            lsGrupo=request.getParameter("lstGrupo"); 
            lsGenero=request.getParameter("lstGenero");    
             
            pbCuentas.setCuenta_mayor(lsCuenta);
            pbCuentas.setConf_cve_mayor_id(lsConfCve);
            pbCuentas.setConf_cve_cta_cont_id(lsConfCant);
            pbCuentas.setDescripcion(lsNombre);
            pbCuentas.setNaturaleza(lsNatura);
            pbCuentas.setFecha_vig_ini(lsFecha01);
            pbCuentas.setFecha_vig_fin(lsFecha02);
            pbCuentas.setId_clase(lsClase);
            pbCuentas.setId_grupo(lsGrupo);
            pbCuentas.setId_genero(lsGenero);      
            
            crsCuentas=pbCuentas.selectVerificaCuenta(conexion,lsCuenta);  
            if (crsCuentas.size() == 0){      
             pbCuentas.insert_RF_TC_CLASIFICADOR_CUENTAS(conexion);       
              %>
                <br><br><br>
                <font class='texto03'>La Cuenta de Mayor <strong> <%=lsCuenta%>  <%=lsNombre%> </strong> fue registrada correctamente.</font>
              <%
            }else{                 
               %>       <br><br><br>
                        <font class='texto03'>La cuenta de mayor <strong><%=lsCuenta%>  <%=lsNombre%> </strong>  ya existe .</font>
                       <%                                     
                }//if
        } //opción A insert_RF_TC_CLASIFICADOR_CUENTAS
        

        if (lsOpcion.equals("E")){ 
   
            String[] cveCuenta=request.getParameterValues("AMBITOS");         
            
            for(int x= 0; x< cveCuenta.length; x++) {            
            cveCta=cveCuenta[x].substring(0,cveCuenta[x].length()-1); 
            pbCuentas.selectCuenta(conexion,cveCta);      
            lsDescripcion= pbCuentas.getDescripcion();    
            
            crsCuentas=pbCuentas.selectVerifica(conexion,cveCta);  
                if (crsCuentas.size() == 0){
                   pbCuentas.delete_RF_TC_CLASIFICADOR_CUENTAS(conexion,cveCta);  //Elimina el registro
                    %>
                     <p class='texto01'>La cuenta de Mayor <strong><%=cveCta%>  <%=lsDescripcion%></strong> fue eliminada correctamente </p>
                    <% 
                }else{
                       //System.out.println("YA EXISTE LA CUENTA CONTABLE");                  
                       %>
                        <br><br><br>
                        <font class='texto03'>No se puede eliminar esta cuenta <strong><%=cveCta%>  <%=lsDescripcion%></strong> porque ya está registrada en el catálogo de cuentas.</font>
                       <%                                     
                }//if
            } //for  
       }//opcion de E
       
     if (lsOpcion.equals("M")){
        
            lsCuenta=request.getParameter("txtCve"); 
            lsNombre=request.getParameter("txtNombre");
            lsNatura=request.getParameter("btrNatura");
            lsFecha01=request.getParameter("txtFecha01");
            lsFecha02=request.getParameter("txtFecha02");            
            pbCuentas.setDescripcion(lsNombre);
            pbCuentas.setNaturaleza(lsNatura);
            pbCuentas.setFecha_vig_ini(lsFecha01);
            pbCuentas.setFecha_vig_fin(lsFecha02);              
            pbCuentas.update_RF_TC_CLASIFICADOR_CUENTAS(conexion,lsCuenta);  
%>
            <br><br><br>
            <font class='texto03'>La Cuenta de Mayor <strong><%=lsCuenta%>  <%=lsNombre%> </strong> fue modificada correctamente </font>
<%
       } //opción de M    
              conexion.commit();
%>
<br>
<br><br>

<table width="100%">
 <tr><td width="73%">&nbsp;</td>
     <td width="10%">&nbsp;</td>
      <td width="80%">                                                                                                      
        <INPUT TYPE="button" NAME="btnAceptar" VALUE="Regresar" class=boton onClick="if ('<%=lsOpcion%>'=='A') javascript:LlamaPagina('c710AgregarFormulario.jsp',''); else {if ('<%=lsOpcion%>'=='E')  LlamaPagina('c710EliminarFiltro.jsp','');else {if ('<%=lsOpcion%>'=='M') LlamaPagina('c710ModificarFiltro.jsp','');else {window.close();}}}">
     </td>
  </tr>
  
</table> 
<%}
    
     catch(Exception e){
       conexion.rollback();
       System.out.println("Ocurrio un error al accesar  "+e.getMessage());
    } //Fin catch
    finally{
       if (conexion!=null){
         conexion.close();
         conexion=null;
       }
     } //Fin finally

%>
</form>
</body>
</html>
