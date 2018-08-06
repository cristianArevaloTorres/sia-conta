<%-- 
    Document   : c761AgregarFormulario
    Created on : 12/08/2013, 03:13:28 PM
    Author     : EST.JOSE.FLORES
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.logging.Logger"%>
<%@page import="java.util.logging.Level"%>
<%@page import="java.util.*,java.sql.*"%>
<%@page import="sia.db.dao.*"%>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="maximoPerfil" class="sun.jdbc.rowset.CachedRowSet" scope="page">
  <%
   maximoPerfil.setTableName("sg_tc_perfil");
   String SQL2 = " SELECT MAX(p.cve_perfil) as maximo from sg_tc_perfil p ";
   maximoPerfil.setCommand(SQL2);
   Connection con2=null;
   try{
     con2 = DaoFactory.getContabilidad();
     maximoPerfil.execute(con2);
   } catch(Exception e){
        System.out.println("Ocurrio un error al accesar el metodo consultaGenero "+e.getMessage());
   } finally{
      if (con2!=null){
          con2.close();
          con2=null;
      }
   } //Fin finally
 %>
</jsp:useBean>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>c761AgregarFormulario</title>
      <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type="text/css">
      <script language="JavaScript" src="../../Librerias/Javascript/Capturador/funciones.js" type="text/javascript"></script>
            <script language="JavaScript">
                function regresar(){ 
                    window.location.href="../perfil/c761Resultado.jsp?clave=<%=session.getAttribute("claveGrupo")%>";
                } 
                function validar(){
                    mensaje= '------------------ ALERTA ------------------\n';  
                    var aprobado = true;
                    var valorCampo = document.forms.formaAgregarPerfil.txtNombrePerfil.value;
                    if(valorCampo=='' || valorCampo==' ' || valorCampo=='  ' || valorCampo=='   ' ){
                       mensaje+='Por favor escriba en el campo Nombre';
                       alert(mensaje);
                       mensaje='';
                       aprobado = false;
                    }
                    return aprobado;
                }
            </script>
    </head>
    <body onLoad="">
    <%util.tituloPagina(out, "Seguridad", "Agregar Perfil","", "Formulario", true);%>
    <br></br> 
        <form id="formaAgregarPerfil" method="post" action="c761Control.jsp" >    
            <table align='center' class='general'  cellpadding="3" >
                <thead></thead>
                <tbody>
                    <tr>
                        <th class="azulObs" align="right">
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            Clave:
                        </th>
                        <td>
                   <%
                       maximoPerfil.beforeFirst(); 
                       while (maximoPerfil.next()){
                   %>   
                          <INPUT TYPE="text" NAME="clavePerfil" SIZE="50"   class="cajaTexto" value="<%=Integer.parseInt(maximoPerfil.getString("maximo"))+1%>">
                   <%  }   %>                            
                        </td>
                    </tr>
                    <tr>
                        <th class="azulObs" align="right">
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            Nombre:
                        </th>
                        <td>    
                                <INPUT TYPE="text" NAME="txtNombrePerfil" SIZE="50"   class="cajaTexto" value="">
                        </td>
                    </tr> 
                </tbody>
            </table>
            <br></br>
            <table align='center' cellpadding="3">
                <thead></thead>
                <tbody>
                <tr>
                    <td>
                        <input type="submit" class="boton" value="Aceptar" onClick="javaScript: if( validar() == false) return false; "/>
                        <input type="hidden" name="operacion" value="Agregar" />
                    </td>
                    <td>
                        <input type="button" class="boton" value="Regresar" onclick="regresar();" >
                    </td>
                </tr>
                </tbody>
            </table>                        
       </form>
    </body>
</html>