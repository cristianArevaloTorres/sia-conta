<%@ page contentType="text/html;charset=WINDOWS-1252" import="java.util.*,java.sql.*"%>
<%@ page import="sun.jdbc.rowset.CachedRowSet"%>
<%@ page  import="sia.db.dao.*"%>

<jsp:useBean id="Autentifica" class="sia.beans.seguridad.Autentifica" scope="session"/>  
<jsp:useBean id="pbRegCont" class="sia.rf.contabilidad.registroContableNuevo.bcCuentaContable" scope="request"/>
<%
// System.out.println(request.getParameter("nivRep"));
int nr = Integer.parseInt(request.getParameter("nivRep"));
String n1 = request.getParameter("n1");
String n2 = request.getParameter("n2");
String n3 = request.getParameter("n3");
String n4 = request.getParameter("n4");
String n5 = request.getParameter("n5");
String n6 = request.getParameter("n6");
String n7 = request.getParameter("n7");
String n8 = request.getParameter("n8");
String n9 = request.getParameter("n9");
String catCuentaId=request.getParameter("catCuentaId");
String uniEje=request.getParameter("uniEje").length()==1?("00").concat(request.getParameter("uniEje")):request.getParameter("uniEje");
String entidad=request.getParameter("entidad");
String ambito=request.getParameter("ambito");
String ejercicio=request.getParameter("ejercicio");
boolean entro=false;
Connection con = DaoFactory.getContabilidad();
ResultSet crs=null;
try{
crs = pbRegCont.cargaSubcuentas(con,nr,n1,n2,n3,n4,n5,n6,n7,n8,n9,catCuentaId,uniEje,entidad,ambito,ejercicio,String.valueOf(Autentifica.getNumeroEmpleado()));
%>
<BODY OnLoad ="parent.loadSourceFinish('capaNivel2', parent.bufferFrame);parent.regresa();">

<script language='javascript'>
var nr =<%=nr%>;
// alert('paso entro capa');
  if (nr == 2){
      for (j = parent.formulario.lstNivel2.options.length; j>0;j--) {parent.formulario.lstNivel2.options[j-1]=null;}
      for (j = parent.formulario.lstNivel3.options.length; j>0;j--) {parent.formulario.lstNivel3.options[j-1]=null;}
      for (j = parent.formulario.lstNivel4.options.length; j>0;j--) {parent.formulario.lstNivel4.options[j-1]=null;}
      for (j = parent.formulario.lstNivel5.options.length; j>0;j--) {parent.formulario.lstNivel5.options[j-1] =null;}
      for (j = parent.formulario.lstNivel6.options.length; j>0;j--) {parent.formulario.lstNivel6.options[j-1] =null;}
      for (j = parent.formulario.lstNivel7.options.length; j>0;j--) {parent.formulario.lstNivel7.options[j-1] =null;}
      for (j = parent.formulario.lstNivel8.options.length; j>0;j--) {parent.formulario.lstNivel8.options[j-1] =null;}
      for (j = parent.formulario.lstNivel9.options.length; j>0;j--) {parent.formulario.lstNivel9.options[j-1] =null;}      
      parent.trlstNivel2.style.display='none';
      parent.trlstNivel3.style.display='none';
      parent.trlstNivel4.style.display='none';
      parent.trlstNivel5.style.display='none';
      parent.trlstNivel6.style.display='none';
      parent.trlstNivel7.style.display='none';
      parent.trlstNivel8.style.display='none';
      parent.trlstNivel9.style.display='none';
      for (j=9;j>=nr;j--)
          eval("parent.formulario.txtCtaNivel"+j).value = '';
  } else if (nr == 3) {
      for (j = parent.formulario.lstNivel3.options.length; j>0;j--) {parent.formulario.lstNivel3.options[j-1]=null;}
      for (j = parent.formulario.lstNivel4.options.length; j>0;j--) {parent.formulario.lstNivel4.options[j-1]=null;}
      for (j = parent.formulario.lstNivel5.options.length; j>0;j--) {parent.formulario.lstNivel5.options[j-1] =null;}
      for (j = parent.formulario.lstNivel6.options.length; j>0;j--) {parent.formulario.lstNivel6.options[j-1] =null;}
      for (j = parent.formulario.lstNivel7.options.length; j>0;j--) {parent.formulario.lstNivel7.options[j-1] =null;}
      for (j = parent.formulario.lstNivel8.options.length; j>0;j--) {parent.formulario.lstNivel8.options[j-1] =null;}
      for (j = parent.formulario.lstNivel9.options.length; j>0;j--) {parent.formulario.lstNivel9.options[j-1] =null;}
      parent.trlstNivel3.style.display='none';
      parent.trlstNivel4.style.display='none';
      parent.trlstNivel5.style.display='none';
      parent.trlstNivel6.style.display='none';
      parent.trlstNivel7.style.display='none';
      parent.trlstNivel8.style.display='none';
      parent.trlstNivel9.style.display='none';
      for (j=9;j>=nr;j--)
          eval("parent.formulario.txtCtaNivel"+j).value = '';
  } else if (nr == 4) {
      for (j = parent.formulario.lstNivel4.options.length; j>0;j--) {parent.formulario.lstNivel4.options[j-1] =null;}
      for (j = parent.formulario.lstNivel5.options.length; j>0;j--) {parent.formulario.lstNivel5.options[j-1] =null;}
      for (j = parent.formulario.lstNivel6.options.length; j>0;j--) {parent.formulario.lstNivel6.options[j-1] =null;}
      for (j = parent.formulario.lstNivel7.options.length; j>0;j--) {parent.formulario.lstNivel7.options[j-1] =null;}
      for (j = parent.formulario.lstNivel8.options.length; j>0;j--) {parent.formulario.lstNivel8.options[j-1] =null;}     
      for (j = parent.formulario.lstNivel9.options.length; j>0;j--) {parent.formulario.lstNivel9.options[j-1] =null;}
      parent.trlstNivel4.style.display='none';
      parent.trlstNivel5.style.display='none';
      parent.trlstNivel6.style.display='none';
      parent.trlstNivel7.style.display='none';
      parent.trlstNivel8.style.display='none';   
      parent.trlstNivel9.style.display='none';
      for (j=9;j>=nr;j--)
          eval("parent.formulario.txtCtaNivel"+j).value = '';
  } else if (nr == 5) {
      for (j = parent.formulario.lstNivel5.options.length; j>0;j--) {parent.formulario.lstNivel5.options[j-1] =null;}
      for (j = parent.formulario.lstNivel6.options.length; j>0;j--) {parent.formulario.lstNivel6.options[j-1] =null;}
      for (j = parent.formulario.lstNivel7.options.length; j>0;j--) {parent.formulario.lstNivel7.options[j-1] =null;}
      for (j = parent.formulario.lstNivel8.options.length; j>0;j--) {parent.formulario.lstNivel8.options[j-1] =null;}   
      for (j = parent.formulario.lstNivel9.options.length; j>0;j--) {parent.formulario.lstNivel9.options[j-1] =null;}
      parent.trlstNivel5.style.display='none';
      parent.trlstNivel6.style.display='none';
      parent.trlstNivel7.style.display='none'
      parent.trlstNivel8.style.display='none'; 
      parent.trlstNivel9.style.display='none';  
      for (j=9;j>=nr;j--)
          eval("parent.formulario.txtCtaNivel"+j).value = '';
  } else if (nr == 6) {
      for (j = parent.formulario.lstNivel6.options.length; j>0;j--) {parent.formulario.lstNivel6.options[j-1] =null;}
      for (j = parent.formulario.lstNivel7.options.length; j>0;j--) {parent.formulario.lstNivel7.options[j-1] =null;}
      for (j = parent.formulario.lstNivel8.options.length; j>0;j--) {parent.formulario.lstNivel8.options[j-1] =null;}     
      for (j = parent.formulario.lstNivel9.options.length; j>0;j--) {parent.formulario.lstNivel9.options[j-1] =null;}
      parent.trlstNivel6.style.display='none';
      parent.trlstNivel7.style.display='none';
      parent.trlstNivel8.style.display='none';      
      parent.trlstNivel9.style.display='none';  
      for (j=9;j>=nr;j--)
          eval("parent.formulario.txtCtaNivel"+j).value = '';
  } else if (nr == 7) {
      for (j = parent.formulario.lstNivel7.options.length; j>0;j--) {parent.formulario.lstNivel7.options[j-1] =null;}
      for (j = parent.formulario.lstNivel8.options.length; j>0;j--) {parent.formulario.lstNivel8.options[j-1] =null;}      
      for (j = parent.formulario.lstNivel9.options.length; j>0;j--) {parent.formulario.lstNivel9.options[j-1] =null;}
      parent.trlstNivel7.style.display='none';
      parent.trlstNivel8.style.display='none';      
      parent.trlstNivel9.style.display='none';  
      for (j=9;j>=nr;j--)
          eval("parent.formulario.txtCtaNivel"+j).value = '';
  } else if (nr == 8) {
      for (j = parent.formulario.lstNivel8.options.length; j>0;j--) {parent.formulario.lstNivel8.options[j-1] =null;}  
      for (j = parent.formulario.lstNivel9.options.length; j>0;j--) {parent.formulario.lstNivel9.options[j-1] =null;}
      parent.trlstNivel8.style.display='none';
      parent.trlstNivel9.style.display='none';
      for (j=9;j>=nr;j--)
          eval("parent.formulario.txtCtaNivel"+j).value = '';
   //       alert('paso salio capa');
 }
<%
// System.out.println("entro");
crs.beforeFirst();
entro =false;
while (crs.next()){
entro = true;
// System.out.println("descripcion "+crs.getString("descripcion")+"cuenta "+crs.getString("cuenta"));
%>
 parent.formulario.lstNivel<%=nr%>.options[parent.formulario.lstNivel<%=nr%>.options.length] = new Option('<%=crs.getString("cuenta")+"-"+crs.getString("descripcion")%>','<%=crs.getString("cuenta")%>');
<%
}
}
     catch(Exception e){
       System.out.println("Ocurrio un error al accesar la capa c700CapaPoliza9.jsp "+e.getMessage());
%>
<p>Ha ocurrido un error al accesar la Base de Datos,</p>

<p>favor de reportarlo al Administrador del Sistema.</p>

<p>Gracias.</p>
<%
     } //Fin catch
     finally{
       if (crs!=null){
         crs.close();
         crs=null;
       }
       if (con!=null){
         con.close();
         con=null;
       }
     } //Fin finally

if (entro){
%>
parent.trlstNivel<%=nr%>.style.display='';
parent.formulario.txtCtaNivel<%=nr%>.focus();
parent.divImporte.style.display='none';
parent.formulario.txtImporte.value ="0";
parent.acepta.style.display ='none';
<%} else{%>
parent.divImporte.style.display='';
parent.acepta.style.display ='';
parent.formulario.txtImporte.focus();
<%}%>
</script>
</BODY>
