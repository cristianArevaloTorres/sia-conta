<%@ page contentType="text/html;charset=WINDOWS-1252" import="java.util.*,java.sql.*,java.io.*"%>
<%@page import="sun.jdbc.rowset.CachedRowSet"%>
<%@ page  import="sia.db.dao.*"%>
<jsp:useBean id="pbPoliza" class = "sia.rf.contabilidad.registroContableNuevo.bcPoliza" scope="page" />
<jsp:useBean id="pbMaestroOperaciones" class = "sia.rf.contabilidad.registroContableNuevo.bcMaestroOperaciones" scope="page" />




<BODY OnLoad ="parent.loadSourceFinish('capaOperacion', parent.bufferFrame9);leeDetalle();">

<%

  String catCuentaId=request.getParameter("catCuentaId");
  String uniEje=request.getParameter("uniEje").length()==1?("00").concat(request.getParameter("uniEje")):request.getParameter("uniEje");
  String entidad=request.getParameter("entidad");
  String ambito=request.getParameter("ambito");
  String ejercicio=request.getParameter("ejercicio");
  String lsOperacionTipoTem=request.getParameter("operacionTipo");
  int liPos=lsOperacionTipoTem.lastIndexOf("-");
  String lsMaestroId=lsOperacionTipoTem.substring(0,liPos);
  String lsOperacionTipo=lsOperacionTipoTem.substring(liPos+1);

  CachedRowSet crsDetalleOperaciones=null;  
  String  lsTemCuentPold9="";
  String  lsTemCuentPold8="";
  String  lsTemCuentPold7="";
  String  lsTemCuentPold6="";
  String  lsTemCuentPold5="";
  String  lsTemCuentPold4="";
  String  lsTemCuentPold3="";
  String  lsTemCuentPold2="";
  String  lsTemCuentPold1="";

  

 
 try{
  //  sbAutentifica.setPagina(request.getRequestURI());
  pbMaestroOperaciones.setUnidad_ejecutora(uniEje);
  pbMaestroOperaciones.setAmbito(ambito);
  pbMaestroOperaciones.setEntidad(entidad);
  pbMaestroOperaciones.setId_catalogo_cuenta(catCuentaId);
  pbMaestroOperaciones.setConsecutivo(lsOperacionTipo);
  crsDetalleOperaciones=pbMaestroOperaciones.select_rf_tr_detalle_operaciones(ejercicio);
  
  crsDetalleOperaciones.beforeFirst();
  //System.out.println("inicio capa operacion");
  out.println("<script language='JavaScript'>");
  out.println("function leeDetalle(){");
  //out.println("alert('hola')");
  String lsCadena="";
  while (crsDetalleOperaciones.next()){  
    lsTemCuentPold1=crsDetalleOperaciones.getString("cuenta_contable").substring(0,4); 
    lsTemCuentPold2=crsDetalleOperaciones.getString("cuenta_contable").substring(4,5);
    lsTemCuentPold3=crsDetalleOperaciones.getString("cuenta_contable").substring(5,9);
    lsTemCuentPold4=crsDetalleOperaciones.getString("cuenta_contable").substring(9,13);
    lsTemCuentPold5=crsDetalleOperaciones.getString("cuenta_contable").substring(13,17);
    lsTemCuentPold8=crsDetalleOperaciones.getString("cuenta_contable").substring(25,29);
    lsTemCuentPold9=crsDetalleOperaciones.getString("cuenta_contable").substring(29,33);
    lsTemCuentPold7=crsDetalleOperaciones.getString("cuenta_contable").substring(21,25);
    lsTemCuentPold6=crsDetalleOperaciones.getString("cuenta_contable").substring(17,21);
    // if (catCuentaId.equals("3"))
    //  lsTemCuentPold9="'"+crsDetalleOperaciones.getString("cuenta_contable").substring(29,33)+"',"; todos quedan de nueve
    String lsNivel=crsDetalleOperaciones.getString("nivel"); 
    lsCadena="  parent.cargaDetalleOperacion('"+lsTemCuentPold1+"','"+lsTemCuentPold2+"','"+lsTemCuentPold3+"','"+lsTemCuentPold4+"','"+lsTemCuentPold5+"','"+lsTemCuentPold6+"','"+lsTemCuentPold7+"','"+lsTemCuentPold8+"','"+lsTemCuentPold9;
    lsCadena=lsCadena+"','"+crsDetalleOperaciones.getString("descCuentaCon")+"','"+crsDetalleOperaciones.getString("operacion_contable_id")+"','0.0','0.0','',"+lsNivel+");";
    out.println(lsCadena);
    //System.out.println(lsCadena);
    //out.println("  parent.cargaDetalleOperacion('11203','0001','0128','0011','0000','0000','0000','descripcion','1','214','543','es una preuba');");
  }
  out.println("}");
  out.println("</script>");
  //System.out.println("termino capa operacion");
      
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
}
   %>
</body>

