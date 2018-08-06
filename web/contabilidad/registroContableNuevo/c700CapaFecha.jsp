<%@ page contentType="text/html;charset=WINDOWS-1252" import="java.util.*,java.sql.*,java.io.*"%>
<%@page import="sun.jdbc.rowset.CachedRowSet"%>
<%@ page  import="sia.db.dao.*"%>
<jsp:useBean id="pbPoliza" class = "sia.rf.contabilidad.registroContableNuevo.bcPoliza" scope="page" />



<BODY OnLoad ="parent.loadSourceFinish('capaFecha', parent.bufferFrame8);parent.validaFechaCapa();">

<%
 ResultSet rsQuery= null;
 Statement stQuery=null;
 String param1="1";
 boolean banderaFecha=false;
 String catCuentaId=request.getParameter("catCuentaId");
 String uniEje=request.getParameter("uniEje").length()==1?("00").concat(request.getParameter("uniEje")):request.getParameter("uniEje");
 String entidad=request.getParameter("entidad");
 String ambito=request.getParameter("ambito");
 int  ejercicio=Integer.parseInt(request.getParameter("ejercicio"));
 String lsFecha = request.getParameter("fecha");
 int lsMes=Integer.parseInt(lsFecha.substring(3,5));
 int lsFechaEjercicio= Integer.parseInt(lsFecha.substring(6,10));
 // System.out.println("ejer "+ejercicio);
 int liRetorno=0;
 
 
 String programa=request.getParameter("programa"); 
 
 Connection conexion=null;
 try{
  conexion=DaoFactory.getContabilidad();
  //  sbAutentifica.setPagina(request.getRequestURI());
      
  liRetorno= pbPoliza.selectValidaFechaFuturo(conexion,lsFecha);
  if ( liRetorno < 0)
    param1="2"; //Fecha en el futuro
  else
  if (lsFechaEjercicio!=ejercicio)
    param1="3"; //Ejercicio diferente al seleccionado
  else{    
    String SQL = "select ejercicio, mes, estatus_cierre_id from rf_tr_cierres_mensuales where unidad_ejecutora='" + uniEje + "' and ambito='"+ambito +
    "' and entidad=" + entidad + " and pais=147 and ejercicio=" + ejercicio + " and mes="+ lsMes +" and id_catalogo_cuenta="+catCuentaId +" and estatus_cierre_id<>2 and programa='"+programa+"'"; 
    // System.out.println(SQL);
    stQuery=conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
    rsQuery = stQuery.executeQuery(SQL);
    
    while (rsQuery.next() == true) {
      banderaFecha=true;
    }
    if (banderaFecha==false)
      param1="4";  // No existe un mes en preliminar o no se abierto el mes actual
    else{
       banderaFecha=false;
       SQL = "select ejercicio, mes, estatus_cierre_id from rf_tr_cierres_mensuales where unidad_ejecutora='" + uniEje + "' and ambito='"+ambito +
       "' and entidad=" + entidad + " and pais=147 and ejercicio=" + ejercicio + " and mes="+ lsMes +" and id_catalogo_cuenta="+catCuentaId +" and estatus_cierre_id=2 and programa='"+programa+"'"; //aqui si
       // System.out.println(SQL);
       stQuery=conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
       rsQuery = stQuery.executeQuery(SQL);
    
       while (rsQuery.next() == true) {
         banderaFecha=true;
       }
       if (banderaFecha)
         param1="5";  // El mes ya se encuentra cerrado definitivamente
    }

  }
   // System.out.println("param xxxx "+param1);
}
catch(Exception E)
{
    System.out.println("Error en pagina en "+" "+E.getMessage()); 
%>
     <p>Ha ocurrido un error al accesar la Base de Datos,</p>
     <p>favor de reportarlo al Administrador del Sistema.</p>
     <p>Gracias.</p>
<%
}
finally{
  //sbAutentifica.cerrarConexion(conexion);  
  if (rsQuery != null){
    rsQuery.close();
    rsQuery=null;
  }    
  if (stQuery != null){
    stQuery.close();
    stQuery=null;
  }   
  if (conexion != null){
    conexion.close();
    conexion=null;
  }   
  
}
   %>
<input name="txtValFecha" type="hidden" value="<%=param1%>">
</body>

