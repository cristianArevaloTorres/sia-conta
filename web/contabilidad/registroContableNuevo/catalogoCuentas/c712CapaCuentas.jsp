<%@ page contentType="text/html;charset=WINDOWS-1252" import="java.util.*,java.sql.*"%>
<%@ page import="sun.jdbc.rowset.CachedRowSet,sia.rf.contabilidad.registroContable.acciones.ControlRegistro"%>
<%@ page  import="sia.db.dao.*"%>


<jsp:useBean id="pbRegCont" class="sia.rf.contabilidad.registroContableNuevo.bcClasificadorCuentas" scope="request"/>
<%
//System.out.println(request.getParameter("nivRep"));
int nr = Integer.parseInt(request.getParameter("nivRep"));
String catCuentaId=request.getParameter("catCuentaId");
String uniEje=request.getParameter("uniEje").length()==1?("00").concat(request.getParameter("uniEje")):request.getParameter("uniEje");
String entidad=request.getParameter("entidad");
String ambito=request.getParameter("ambito");
String ejercicio=request.getParameter("ejercicio");
String lsTipoUsr = request.getParameter("tipoUsr");
String totNivel = request.getParameter("totN");
boolean entro=false;

String n1 = request.getParameter("n1");
String n2 = request.getParameter("n2");
String n3 = request.getParameter("n3");
String n4 = request.getParameter("n4");
String n5 = request.getParameter("n5");
String n6 = request.getParameter("n6");
String n7 = request.getParameter("n7");
String n8 = request.getParameter("n8");
String tipoUsuario = null;


Connection con = DaoFactory.getContabilidad();
ResultSet crs=null;
try{
    ControlRegistro controlReg = (ControlRegistro)request.getSession().getAttribute("controlRegistro"); 
    tipoUsuario = String.valueOf(controlReg.getTipoUsuario());
    //esta informacion se utilizar para llenar las listas
    if (nr > Integer.parseInt(totNivel)){
        nr=9;    
        ejercicio="1111";    
    }
    
    crs = pbRegCont.cargaCuentasMayor(con,nr,n1,n2,n3,n4,n5,n6,n7,n8,catCuentaId,uniEje,entidad,ambito,ejercicio,tipoUsuario);
%>
<BODY OnLoad ="parent.loadSourceFinish('capaNivel2', parent.bufferFrame);parent.Fin2();parent.regresa();">

<script language='javascript'>
var nr =<%=nr%>;
 //alert('paso entro capa');
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
      for (j = parent.formulario.lstNivel4.options.length; j>0;j--) {parent.formulario.lstNivel4.options[j-1] =null;}
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
      parent.trlstNivel7.style.display='none';

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
 }else if (nr == 7) {
      for (j = parent.formulario.lstNivel7.options.length; j>0;j--) {parent.formulario.lstNivel7.options[j-1] =null;}
      for (j = parent.formulario.lstNivel8.options.length; j>0;j--) {parent.formulario.lstNivel8.options[j-1] =null;}
      for (j = parent.formulario.lstNivel9.options.length; j>0;j--) {parent.formulario.lstNivel9.options[j-1] =null;}

      
      parent.trlstNivel7.style.display='none';
      parent.trlstNivel8.style.display='none';
      parent.trlstNivel9.style.display='none';    
      
      for (j=9;j>=nr;j--)
          eval("parent.formulario.txtCtaNivel"+j).value = '';
          
 }else if (nr == 8){
 /*   
      for (j = parent.formulario.lstNivel7.options.length; j>0;j--) {parent.formulario.lstNivel7.options[j-1] =null;}
      for (j = parent.formulario.lstNivel8.options.length; j>0;j--) {parent.formulario.lstNivel8.options[j-1] =null;}
      parent.trlstNivel7.style.display='none';
      parent.trlstNivel8.style.display='none';
    }
*/
  for (j = parent.formulario.lstNivel8.options.length; j>0;j--) {parent.formulario.lstNivel8.options[j-1] =null;}
  for (j = parent.formulario.lstNivel9.options.length; j>0;j--) {parent.formulario.lstNivel9.options[j-1] =null;}
  parent.trlstNivel8.style.display='none';
  parent.trlstNivel9.style.display='none';    
      
      for (j=9;j>=nr;j--)
          eval("parent.formulario.txtCtaNivel"+j).value = '';


 }else if (nr == 9){
    for (j = parent.formulario.lstNivel9.options.length; j>0;j--) 
      parent.formulario.lstNivel9.options[j-1] =null;
    }
    parent.trlstNivel9.style.display='none';    
    
    for (j=9;j>=nr;j--)
      eval("parent.formulario.txtCtaNivel"+j).value = '';
<%
crs.beforeFirst();
entro =false;
while (crs.next()){
//System.out.println("while ");
entro = true;
    if (nr==1){
%>
        parent.formulario.lstNivel<%=nr%>.options[parent.formulario.lstNivel<%=nr%>.options.length] = new Option('<%=crs.getString("descripcion")%>','<%=crs.getString("cuenta_mayor")%>');
        parent.formulario.txtCtaNivel<%=nr%>.focus();
<%
    }else{
  //  System.out.println("elsee "+nr);
%>    
        parent.formulario.lstNivel<%=nr%>.options[parent.formulario.lstNivel<%=nr%>.options.length] = new Option('<%=crs.getString("descripcion")%>','<%=crs.getString("cuenta")%>');
 
<%    }
}//while
%>
<%
}
     catch(Exception e){
       System.out.println("Ocurrio un error al accesar la capa c712CapaCuentas.jsp "+e.getMessage());
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
//System.out.println("if "+nr);
%>
    parent.trlstNivel<%=nr%>.style.display='';
    parent.formulario.txtCtaNivel<%=nr%>.focus();
<%} else{
//System.out.println("else "+nr);
//DEBO VERIFCAR ESTO SI ES DINAMICO O CON ESTO ES SUFICIENTE
    if (nr < Integer.parseInt(totNivel)){
%>
    parent.trlstNivel<%=nr%>.style.display='';
    parent.formulario.txtCtaNivel<%=nr%>.focus();
<%  }
}%>
</script>
</BODY>
