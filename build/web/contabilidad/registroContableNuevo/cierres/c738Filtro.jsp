<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page  import="sia.db.dao.*,sia.db.sql.Sentencias"%>
<%@ page  import="java.util.*,java.sql.*"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<!-- <jsp : useBean id="pbLocalizaPolizas" class = "sia.rf.contabilidad.registroContableNuevo.polizas" scope="session"/> -->
<%
  Sentencias sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD,Sentencias.XML);
  Map parametros = new HashMap();
%>
<jsp:useBean id="abEjercicio" class="sun.jdbc.rowset.CachedRowSet" scope="page">
   <%
      Connection conexion=null;
      conexion=DaoFactory.getContabilidad();
      abEjercicio.setTableName("RF_TC_EJERCICIOS");
      abEjercicio.setCommand(sentencia.getComando("filtros.select.ejercicios", parametros));
      abEjercicio.execute(conexion);
      conexion.close();
      conexion=null;
   %>
</jsp:useBean> 
    

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>c716Filtro</title>
    <link rel="stylesheet" href="../../../Librerias/Estilos/siastyle.css" type='text/css'>
    <script language="JavaScript" src="../../../Librerias/Javascript/Capturador/funciones.js" type="text/javascript"></script>
    <script language="JavaScript" src="../../../Librerias/Javascript/Capturador/fcgencValida2.js" type="text/javascript"></script>
    <script language="JavaScript" src="../../../Librerias/Javascript/Capturador/calendar.js" type="text/javascript"></script>
    
<script language="JavaScript">

function preguntaConfirmacion(proceso, mes, ejercicio) {
   mensaje = 'Se lanzara el proceso '+ proceso +' del mes de '+mes+' para el ejercicio '+ejercicio+', ¿Está seguro?';
   if (confirm(mensaje)) {
     document.filtro.btnAceptar.disabled=true;
     document.filtro.submit();
     return true;
   } 
   else {
     return false;
   }   
}

function revisaFormulario() {
var valida01=true;
var cadenaError='';
with(document.filtro){   
  if (lstEjercicio.value=="") {
       valida01=false;
       cadenaError=cadenaError+"Es necesario seleccionar el Ejercicio\n";
  }

  
  if (lstMes.value=="") {
       valida01=false;
       cadenaError=cadenaError+"Es necesario seleccionar el mes\n";
   }
 
  if(!(valida01))
       {alert(cadenaError);}
  else{        
    valida01=true;
       //document.filtro.btnAceptar.disabled=true;
       //document.filtro.submit();
       }
 }
 return valida01;
}
function validaMesCerrado(vEjercicio,vMes){
//proceso busca mes y valida cierre definitivo
res=false;
return true;
}

function validaEjercicio(){
//proceso busca ejercicio es correcto
return true;
}

function validaMesContienePolizasElim(){
res=false;
    /*proceso localizaPolizas de eliminacion del mes
    if(localizaPE>0){
            res=true;
        else res=false;
    else res=true;*/
return res;
}

</script>
</head>
<%
 String lsProceso=request.getParameter("proceso");
%>
<body>
    <jsp:scriptlet>util.tituloPagina(out, "Contabilidad", "Procesos de cierre.","Filtrar", true);</jsp:scriptlet>    
    <jsp:directive.include file="../../registroContableNuevo/encabezadoFechaActual.jspf"/>

    <br><br>
 <%   
   String pagina="";
   String proceso="";
   if (lsProceso.equals("1")){    
     pagina="c738Control.jsp";
     proceso="verificacion";
%>
    <H2>Proceso de verificación. </H2>
 <% } else   
    if (lsProceso.equals("2")){    
      pagina="c739Control.jsp";
      proceso="reafectacion";
%>
    <H2>Proceso de re afectación. </H2>
 <% } else  
    if (lsProceso.equals("3")){
      pagina="c740Control.jsp";
      proceso="congruencia";
%>
    <H2>Proceso de congruencia. </H2>
<%  }%>     
    <br>

    <FORM name="filtro" method="post"  action=<%=pagina%>>
    <table align="center">
       <tr><td  class="negrita" align="right">Ejercicio: </td>
          <td><SELECT  NAME="lstEjercicio" class=cajaTexto>
         <%

          try{
          abEjercicio.beforeFirst(); 
          while (abEjercicio.next()){
          %>
            <OPTION VALUE="<%=abEjercicio.getString("ejercicio")%>"><%=abEjercicio.getString("ejercicio")%></OPTION>
          <%
         } //Fin while
         }catch (Exception e) {
           System.out.println("Error en lista de abEjercicio: "+ e.getMessage());
         }         
         
         %>
               
          
          
          
          </td>
      </tr>
      <tr><td  class="negrita" align="right">Mes de Afectación: </td>
          <td ><select name="lstMes" class=cajaTexto>
                <option SELECTED Value="">- Seleccione -</option>
                <option Value="01-ENE">Enero</option>
                <option Value="02-FEB">Febrero</option>
                <option Value="03-MAR">Marzo</option>
                <option Value="04-ABR">Abril</option>
                <option Value="05-MAY">Mayo</option>
                <option Value="06-JUN">Junio</option>
                <option Value="07-JUL">Julio</option>
                <option Value="08-AGO">Agosto</option>
                <option Value="09-SEP">Septiembre</option>
                <option Value="10-OCT">Octubre</option>
                <option Value="11-NOV">Noviembre</option>
                <option Value="12-DIC">Diciembre</option>
             </select>
          </td>      
      </tr>      
 
      <tr><td><br></td>
          <td>
              <input type='hidden' name='txtUnidad' value=<%=request.getAttribute("unidadEjecutora")%>>
              <input type='hidden' name='txtAmbito' value=<%=request.getAttribute("ambito")%>>
              <input type='hidden' name='txtEntidad' value=<%=request.getAttribute("entidad")%>>
              <input type='hidden' name='txtFechaActual' value=<%=controlRegistro.getFechaAfectacion()%>>
          </td>
      </tr>
      <tr>
          <td  align="right"><input type='button' name='btnAceptar' value='Aceptar' class='boton' onClick="javascript:if (revisaFormulario()){preguntaConfirmacion('<%=proceso%>',lstMes.options[lstMes.selectedIndex].text,lstEjercicio.options[lstEjercicio.selectedIndex].text);}"></td>
          <td><input type='button' name='btnCancelar' value='Cancelar' class='boton' onClick="javascript:"javascript:history.go(-1);"></td>
      </tr>
      </table>            
  </FORM>
  </body>
</html>