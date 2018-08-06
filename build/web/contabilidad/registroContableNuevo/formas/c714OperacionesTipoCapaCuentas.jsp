<%@ page contentType="text/html;charset=WINDOWS-1252" import="java.util.*,java.sql.*"%>
<%@ page import="sun.jdbc.rowset.CachedRowSet"%>
<%@ page import="sia.rf.contabilidad.registroContableNuevo.bcCuentaContableArmonizadaV2"%>
<%@ page  import="sia.db.dao.*"%>


<jsp:useBean id="pbRegCont" class = "sia.rf.contabilidad.registroContableNuevo.bcCuentaContableArmonizadaV2" scope="request"/>
<%!
  private int[] obt(String configura, int nivel) {
    String[] valores = configura.split(",");
    int[] regresa={0,0};
    int inicio = 0;
    int i=0;
    for(i=0; i < nivel-1; i++) { 
      inicio = inicio + Integer.parseInt(valores[i]);
    }
    regresa[0]= inicio;
    regresa[1]= inicio + Integer.parseInt(valores[i]);
    return regresa;
  }
%>
<%
//System.out.println(request.getParameter("nivRep"));
String ctas       = request.getParameter("ctas");
String configura  = request.getParameter("configura");
String catCuentaId=request.getParameter("catCuentaId");
String uniEje=request.getParameter("uniEje");
String entidad=request.getParameter("entidad");
String ambito=request.getParameter("ambito");
String ejercicio=request.getParameter("ejercicio");
int nr = Integer.parseInt(request.getParameter("nivRep"));

boolean entro=false;
Connection con = DaoFactory.getContabilidad();
ResultSet crs=null;
try{
String idCuenta = pbRegCont.selectCuentaContableId(ctas,catCuentaId,ejercicio,String.valueOf(nr-1));
if(!catCuentaId.equals("3") || Integer.parseInt(ejercicio)<=2010)
  //cargaSubcuentas(Connection con, int nivRep, String pCatCuentaId,String pUniEje, String pEntidad, 
    //                  String pAmbito, String pEjercicio,String pnumEmpleado, String ... ctas)
  crs = pbRegCont.cargaSubcuentasArm(con,catCuentaId,uniEje,entidad,ambito,ejercicio,"",nr,configura,ctas);
idCuenta = idCuenta == null ? "" : idCuenta;
int maxNivel = configura.split(",").length;
%>
<BODY OnLoad ="parent.termino(<%=idCuenta%>); parent.loadSourceFinish('capaNivel2');">

<script language='javascript'>
var nr =<%=nr%>;
var maxNivel =<%=maxNivel%>; 
  for(x = nr; x <= maxNivel; x++) {
    for (j = parent.document.getElementById("lstNivel"+x).options.length; j>0;j--) {parent.document.getElementById("lstNivel"+x).options[j-1]=null;}
    parent.document.getElementById("trlstNivel"+x).style.display='none';
    parent.document.getElementById("txtCtaNivel"+x).value = '';
  }
  

<%
crs.beforeFirst();
entro =false;
//int[] valores = obt(configura,nr);
//String cta = null;
while (crs.next()){
entro = true;
//cta = crs.getString("cuenta").substring(valores[0],valores[1]);
%>
 parent.document.getElementById('lstNivel<%=nr%>').options[parent.document.getElementById('lstNivel<%=nr%>').options.length] = new Option('<%=crs.getString("cuenta")%>'+'-'+'<%=crs.getString("descripcion")%>','<%=crs.getString("cuenta")%>');
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
if (entro) {
%>
parent.document.getElementById('trlstNivel<%=nr%>').style.display='';
parent.document.getElementById('txtCtaNivel<%=nr%>').focus();
<%
}
if (entro && nr <= bcCuentaContableArmonizadaV2.NIVEL_OPERACIONES){
%>
parent.document.getElementById('trlstNivel<%=nr%>').style.display='';
parent.document.getElementById('txtCtaNivel<%=nr%>').focus();
parent.document.getElementById('divTipoOper').style.display='none';
parent.document.getElementById('divAgregar').style.display='none';
<!--parent.acepta.style.display ='none';-->
<%} else{%>
parent.document.getElementById('divTipoOper').style.display='';
parent.document.getElementById('divAgregar').style.display='';
<%}%>
</script>
</BODY>