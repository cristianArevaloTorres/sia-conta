<%@ page contentType="text/html;charset=WINDOWS-1252" import="java.util.*,java.sql.*"%>
<%@ page import="sun.jdbc.rowset.CachedRowSet"%>
<%@ page  import="sia.db.dao.*"%>


<jsp:useBean id="pbRegCont" class = "sia.rf.contabilidad.registroContableNuevo.bcCuentaContableArmonizadaV2" scope="request"/>
<%
//System.out.println(request.getParameter("nivRep"));
String catCuentaId=request.getParameter("catCuentaId");
String uniEje=request.getParameter("uniEje").length()==1?("00").concat(request.getParameter("uniEje")):request.getParameter("uniEje");
String entidad=request.getParameter("entidad");
String ambito=request.getParameter("ambito");
String ejercicio=request.getParameter("ejercicio");

int nr = Integer.parseInt(request.getParameter("nivRep"));
String ctas       = request.getParameter("ctas");
String configura  = request.getParameter("configura");
boolean entro=false;
Connection con = DaoFactory.getContabilidad();
ResultSet crs=null;
try{
int maxNivel = configura.split(",").length;
if(!catCuentaId.equals("3") || Integer.parseInt(ejercicio)<=2010)
  //cargaSubcuentas(Connection con, int nivRep, String pCatCuentaId,String pUniEje, String pEntidad, 
    //                  String pAmbito, String pEjercicio,String pnumEmpleado, String ... ctas)
  crs = pbRegCont.cargaSubcuentasArm(con,catCuentaId,uniEje,entidad,ambito,ejercicio,"",nr,configura,ctas);

//crs = pbRegCont.cargaSubcuentas(con,nr,n1,n2,n3,n4,n5,n6,n7,"",catCuentaId,uniEje,entidad,ambito,ejercicio,"");
%>
<BODY OnLoad ="parent.loadSourceFinish('capaNivel2', parent.bufferFrame);parent.asignaValorRegreso(<%=nr%>);">

<script language='javascript'>
var nr =<%=nr%>;

var maxNivel =<%=maxNivel%>; 
  for(x = nr; x <= maxNivel; x++) {
    for (j = parent.document.getElementById("lstNivel"+x).options.length; j>0;j--) {
    parent.document.getElementById("lstNivel"+x).options[j-1]=null;}
    parent.document.getElementById("trlstNivel"+x).style.display='none';
    parent.document.getElementById("txtCtaNivel"+x).value = '';
  }

<%
// System.out.println("entro");
crs.beforeFirst();
entro =false;
while (crs.next()){
entro = true;
%>
 parent.formulario.lstNivel<%=nr%>.options[parent.formulario.lstNivel<%=nr%>.options.length] = new Option('<%=crs.getString("cuenta")%>'+'-'+'<%=crs.getString("descripcion")%>','<%=crs.getString("cuenta")%>');
<%
}
}
     catch(Exception e){
       System.out.println("Ocurrio un error al accesar la capa c700CapaPoliza.jsp "+e.getMessage());
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
parent.divTipoOper.style.display='none';
parent.divAgregar.style.display='none';
<!--parent.acepta.style.display ='none';-->
<%} else{%>
parent.divTipoOper.style.display='';
parent.divImporte.style.display='';
parent.divAgregar.style.display='';
<%}%>
</script>
</BODY>