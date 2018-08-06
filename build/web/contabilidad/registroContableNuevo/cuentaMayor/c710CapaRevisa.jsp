<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="java.util.*,java.sql.*,java.io.*"%>
<%@ page import="sia.db.dao.*"%>
<%@ page import="sun.jdbc.rowset.CachedRowSet"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="controlRegistro"  class="sia.rf.contabilidad.registroContable.acciones.ControlRegistro" scope="session"/>
<jsp:useBean id="pbCuenta" class="sia.rf.contabilidad.registroContableNuevo.bcClasificadorCuentas" scope="page"/>
<%@ include file="../../../Librerias/Funciones/utilscpv.jsp"%>
<body onload="parent.loadSourceFinish('capaRevisa', parent.bufferFrame2);parent.ValidaCapaRevisa();parent.regresa();">
<%
   Connection conexion=null;
 try{
    conexion=DaoFactory.getContabilidad();
    
    String lsCveCta = request.getParameter("cveCta");
    
    int tamanio=0;
    
    CachedRowSet crsCuenta = null;   
    crsCuenta=pbCuenta.selectVerificaCuenta(conexion,lsCveCta);     
    tamanio=crsCuenta.size();  
%>  
   
<input type="hidden" name="txtExiste" maxlength="100" size="60"  value="<%=tamanio%>" class="cajaTexto">
<%
}
     catch(Exception e){
       System.out.println("Ocurrio un error al accesar  "+e.getMessage());
    } //Fin catch
    finally{
       if (conexion!=null){
         conexion.close();
         conexion=null;
       }
     } //Fin finally
%></body>