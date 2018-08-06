<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="java.util.*,java.sql.*,java.io.*" %>
<%@ page import="sia.db.dao.*"%>
<%@ page import="sun.jdbc.rowset.CachedRowSet" %>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>   
<jsp:useBean id="controlRegistro" class="sia.rf.contabilidad.registroContable.acciones.ControlRegistro" scope="session" />

<jsp:useBean id="pbCuenta" class = "sia.rf.contabilidad.registroContableNuevo.bcClasificadorCuentas" scope="page"/>
<%@include file="../../../Librerias/Funciones/utilscpv.jsp"%>
<BODY OnLoad ="parent.loadSourceFinish('capaMuestra', parent.bufferFrame1);parent.SeleccionaCuenta();parent.regresa();">

<%
   Connection conexion=null;
 try{
    conexion=DaoFactory.getContabilidad(); 
    
    String lsGenero = request.getParameter("cveGenero");
    String lsGpo = request.getParameter("cveGpo");  
    String lsClase = request.getParameter("cveClase");  
                 
    CachedRowSet crsConsecutivo = null;
    crsConsecutivo =pbCuenta.selectConsecutivo(conexion,lsGenero,lsGpo,lsClase);   
    
%>
<table width=100% border=0 >
   <tr><td width="36%"><div align="right">Cuenta:</div></td>
       <td><div align="left"><SELECT NAME="lstCta"  class='cajaTexto' OnChange="javascript:Cuenta(Filtro.lstCta[selectedIndex].value);"> 
          <option selected value='' >- Seleccione -
<%      
        if (crsConsecutivo.size()==0){ %>
             <option selected value='' >- Seleccione -
<%      }
        else{
        while (crsConsecutivo.next()){            
 %>             
                <OPTION VALUE="<%=crsConsecutivo.getString(1)%>"><%=crsConsecutivo.getString(1)%></OPTION>                
<%
        } //while 
        crsConsecutivo.close();
        crsConsecutivo = null;
        }//else
%>
  </select></div></td>
  </tr>
  <tr>
      <td ><div align="right">N&uacute;mero de la cuenta:</div></td>
      <td><div align="left"><INPUT TYPE="text" Class="cajaTexto" NAME="txtCve" SIZE=3 maxlength=4 >
  </div></td>
  </tr>
</table>
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
%>
</BODY>