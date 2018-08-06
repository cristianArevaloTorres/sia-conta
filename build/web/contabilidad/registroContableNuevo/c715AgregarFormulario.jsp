<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page  import="java.util.*,java.sql.*"%>
<%@ page  import="sia.db.dao.*"%>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>   
<jsp:useBean id="controlRegistro" class="sia.rf.contabilidad.registroContable.acciones.ControlRegistro" scope="session" />

<%
  String lsUniEje=controlRegistro.getUnidad();
  String lsPais=String.valueOf(controlRegistro.getPais());   
  String lsEntidad=String.valueOf(controlRegistro.getEntidad());
  String lsAmbito=String.valueOf(controlRegistro.getAmbito());
  String lsEjercicio=String.valueOf(controlRegistro.getEjercicio());
try{

%>
<jsp:useBean id="abReportes" class="sun.jdbc.rowset.CachedRowSet" scope="page"/>
<%
    
  abReportes.setTableName("rf_tc_reportes");
  String SQL = "select t.id_documento,t.descripcion from rf_tc_reportes t";
  abReportes.setCommand(SQL);
  Connection con=null;
  try{
     con = DaoFactory.getContabilidad();
     abReportes.execute(con);
   }
     catch(Exception e){
       System.out.println("Ocurrio un error al accesar el metodo abReportes "+e.getMessage());
     } //Fin catch
     finally{
       if (con!=null){
         con.close();
         con=null;
       }
     } //Fin finally
%>
<jsp:useBean id="abFirmasAutorizadas" class="sun.jdbc.rowset.CachedRowSet" scope="page"/>
<%
    
  abFirmasAutorizadas.setTableName("rf_tc_firmas_autorizadas");
   SQL = "select t.num_empleado,t.nombres||' '||t.apellido_pat||' '||t.apellido_mat as nombre from rf_tc_firmas_autorizadas t order by t.nombres ";
  abFirmasAutorizadas.setCommand(SQL);
   con=null;
  try{
     con = DaoFactory.getContabilidad();
     abFirmasAutorizadas.execute(con);
   }
     catch(Exception e){
       System.out.println("Ocurrio un error al accesar el metodo abFirmasAutorizadas "+e.getMessage());
     } //Fin catch
     finally{
       if (con!=null){
         con.close();
         con=null;
       }
     } //Fin finally
%>


<html>
<head>
<title>Agregar Firmas</title>
<link rel="stylesheet" href="../../Librerias/Estilos/siastyle.css" type='text/css'>
<script language="JavaScript" src="../../Librerias/Javascript/Capturador/funciones.js" type="text/javascript"></script>
<script language="JavaScript" src="../../Librerias/Javascript/Capturador/fcgencValida2.js" type="text/javascript"></script>
<script language="JavaScript">
function revisaFormulario() {
   var valida01=true;
   var cadenaError='';
   
  if ((document.formulario.lstMes.value=="")){
      valida01=false;
       cadenaError=cadenaError+"Es necesario seleccionar el Mes\n";
  }
  
  if ((document.formulario.lstReporte.value=="")){
      valida01=false;
       cadenaError=cadenaError+"Es necesario seleccionar el Reporte\n";
   }
  if ((document.formulario.lstElaboro.value=="")){
      valida01=false;
       cadenaError=cadenaError+"Es necesario seleccionar persona que Elabora<\n";
   }

  if ((document.formulario.lstReviso.value=="")){
      valida01=false;
       cadenaError=cadenaError+"Es necesario seleccionar persona que Revisa<\n";
   }
  if ((document.formulario.lstAutorizo.value=="")){
      valida01=false;
       cadenaError=cadenaError+"Es necesario seleccionar persona que Autoriza<\n";
   }
        
   if(!(valida01))
       alert(cadenaError);
   else        
       document.formulario.submit();
}
</script>


</head>

<body>
<jsp:scriptlet>util.tituloPagina(out, "Contabilidad", " ", "Agregar Firmas", "Agregar", true);</jsp:scriptlet>    

<FORM Method="post" action="..\..\contabilidad\registroContableNuevo\c715Control.jsp" name="formulario" >
<H2>Informaci&oacute;n: Unidad ejecutora=<%=lsUniEje%>, Entidad=<%=lsEntidad%>, Ambito=<%=lsAmbito%> y  Ejercicio=<%=lsEjercicio%></H2>
<br><br>
<hr noshade size='3'>
<br><br>

<table>
<tr><td  class="negrita" align="right">Mes para firmas: </td>
     <td ><select name="lstMes" class=cajaTexto>
            <option SELECTED Value="">- Seleccione -</option>
            <option Value="1">Enero</option>
            <option Value="2">Febrero</option>
            <option Value="3">Marzo</option>
            <option Value="4">Abril</option>
            <option Value="5">Mayo</option>
            <option Value="6">Junio</option>
            <option Value="7">Julio</option>
            <option Value="8">Agosto</option>
            <option Value="9">Septiembre</option>
            <option Value="10">Octubre</option>
            <option Value="11">Noviembre</option>
            <option Value="12">Diciembre</option>
  </select>         
  </td>      
</tr>      
<tr>
    <td class="negrita" align="left">Reporte</td>
    <td>
         <select name='lstReporte' class= 'cajaTexto' >
         <option SELECTED Value="">- Seleccione -</option>
<%
     abReportes.beforeFirst();
     while (abReportes.next()) {       
%>        
            <option  value='<%=abReportes.getString("id_documento")%>'><%=abReportes.getString("descripcion")%></option>
 <%    }%>   
      </select>    
    </td>
</tr>
<tr>
    <td class="negrita" align="left">Elaboró</td>
    <td>
       <select name='lstElaboro' class= 'cajaTexto' >
       <option SELECTED Value="">- Seleccione -</option>
<%
         abFirmasAutorizadas.beforeFirst();
         while (abFirmasAutorizadas.next()) {       
%>        
            <option  value='<%=abFirmasAutorizadas.getString("num_empleado")%>'><%=abFirmasAutorizadas.getString("nombre")%></option>
 <%      }%>   
      </select>
    </td>
  </tr> 

<tr>
    <td class="negrita" align="left">Revisó</td>
    <td>
       <select name='lstReviso' class= 'cajaTexto' >
       <option SELECTED Value="">- Seleccione -</option>
<%
         abFirmasAutorizadas.beforeFirst();
         while (abFirmasAutorizadas.next()) {       
%>        
            <option  value='<%=abFirmasAutorizadas.getString("num_empleado")%>'><%=abFirmasAutorizadas.getString("nombre")%></option>
 <%      }%>   
      </select>
    </td>
  </tr> 


<tr>
    <td class="negrita" align="left">Autorizó</td>
    <td>
       <select name='lstAutorizo' class= 'cajaTexto' >
       <option SELECTED Value="">- Seleccione -</option>
<%
         abFirmasAutorizadas.beforeFirst();
         while (abFirmasAutorizadas.next()) {       
%>        
            <option  value='<%=abFirmasAutorizadas.getString("num_empleado")%>'><%=abFirmasAutorizadas.getString("nombre")%></option>
 <%      }%>   
      </select>
    </td>
  </tr> 
</table>

<table width="100%">
  <tr>
    <td  width="48%" valign="middle"></td>
    <td width="18%" align="right">
       <INPUT type='button' name='btnAceptar' value="Aceptar" class='boton' onClick="javascript:revisaFormulario();"></td>
    <td width="14%" align="right">
       <INPUT type='button' name='btnCancelar' VALUE="Regresar" class='boton' onClick="javascript:LlamaPagina('c715AgregarFormulario','');" ></td>
  </tr>
</table>
  <input name="txtUniEje" type="hidden" value=<%=lsUniEje%> >
  <input name="txtPais" type="hidden" value=<%=lsPais%> >
  <input name="txtEntidad" type="hidden" value=<%=lsEntidad%> >
  <input name="txtAmbito" type="hidden" value=<%=lsAmbito%> >
  <input name="txtEjercicio" type="hidden" value=<%=lsEjercicio%> >
</form>
<%
}
catch(Exception e){
       System.out.println("Ocurrio un error al accesar  "+e.getMessage());
} //Fin catch
%>
</body>
</html>