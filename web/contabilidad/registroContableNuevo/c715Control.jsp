<%@ page contentType="text/html; charset=ISO-8859-1" %>
<%@ page  import="java.util.*,java.sql.*"%>
<%@ page  import="sia.db.dao.*"%>
<%@ page  import="sia.beans.seguridad.*"%>
<%@ page  import="sia.rf.contabilidad.registroContableNuevo.bcRfTrDocumentosFirmas"%>
<%@ page  import="sia.rf.contabilidad.registroContableNuevo.bcRfTcFirmasAutorizadas"%>
<%@ page  import="sia.beans.seguridad.*"%>
<%@ page  import="sia.rf.contabilidad.registroContable.acciones.ControlRegistro" %>
<html>
<head>
<meta http-equiv="Content-Language" content="es-mx">
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<title>Cierre Definitivo</title>
<link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type='text/css'>
</head>

<jsp:useBean id="Autentifica" class = "sia.beans.seguridad.Autentifica" scope="session" />
<jsp:useBean id="pbDocumentoContable" class = "sia.rf.contabilidad.registroContableNuevo.bcDocumentoContable" scope="page"/>
<jsp:useBean id="pbDocumentosFirmas" class = "sia.rf.contabilidad.registroContableNuevo.bcDocumentosFirmas" scope="page"/>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>  
<jsp:useBean id="controlRegistro" class="sia.rf.contabilidad.registroContable.acciones.ControlRegistro" scope="session" />

<body topmargin=1 leftmargin=10>
<jsp:scriptlet>util.tituloPagina(out, "Contabilidad", " ", "Firmas", "Control", true);</jsp:scriptlet>


  

      


<br>
<FORM Method="post" action="c715Resultado.jsp">


<%!//Función que graba manda valores a las propiedades del bean bcDocumentoContable
  private void eliminarFirmas(HttpServletRequest request, Autentifica aut, Connection con) throws Exception {
    String[] eliminados = request.getParameter("eliminados").split(",");
    bcRfTrDocumentosFirmas doctosFirmas = new bcRfTrDocumentosFirmas();
    try  {
      for(int x=0; x<eliminados.length; x++) {
        if(!eliminados[x].equals("")) {
          doctosFirmas.setDocumentoFirmaId(eliminados[x]);
          doctosFirmas.delete(con);
        }
      }  
    } catch (Exception ex)  {
      throw (ex);
    } finally  {
    }
  }
  
  private void modificarFirmasAutorizadas(HttpServletRequest request, Autentifica aut, Connection con, int x) throws Exception {
    bcRfTcFirmasAutorizadas firmasAutorizadas = new bcRfTcFirmasAutorizadas();
    ControlRegistro controlReg = null;
    int numReg=0;
    try  {
      controlReg = (ControlRegistro)request.getSession().getAttribute("controlRegistro");
      
      
        firmasAutorizadas.setNumEmpleado(request.getParameterValues("numEmpleado")[x]);
        firmasAutorizadas.setEjercicio(String.valueOf(controlReg.getEjercicio()));
        firmasAutorizadas.setMes((String)request.getSession().getAttribute("mes"));
        numReg = firmasAutorizadas.select(con);
        firmasAutorizadas.setPrefijo(request.getParameterValues("prefijo")[x]);
        firmasAutorizadas.setNombres(request.getParameterValues("nombres")[x]);
        firmasAutorizadas.setApellidoPat(request.getParameterValues("apellidoPat")[x]);
        firmasAutorizadas.setApellidoMat(request.getParameterValues("apellidoMat")[x]);
        firmasAutorizadas.setPuestoFirma(request.getParameterValues("puestoFirma")[x]);
        
        if(numReg > 0)
          firmasAutorizadas.update(con,true);
        else {
          firmasAutorizadas.insert(con);
      }
    } catch (Exception ex)  {
      throw (ex);
    } finally  {
    }
    
  }
  
  private void insertarDocumentosFirmas(HttpServletRequest request, Autentifica aut, Connection con, int x) throws Exception {
    bcRfTrDocumentosFirmas doctosFirmas = null;
    try  {
      doctosFirmas = new bcRfTrDocumentosFirmas();
      doctosFirmas.setFirma(request.getParameterValues("tipoFirma")[x]);
      doctosFirmas.setNumEmpleado(request.getParameterValues("numEmpleado")[x]);
      doctosFirmas.setDocumentoContableId(request.getParameter("documentoContableId"));
      doctosFirmas.insert(con);    
    } catch (Exception ex)  {
      throw (ex);
    } finally  {
    }
    
  }
  
  private void modificarDocumentosFirmas(HttpServletRequest request, Autentifica aut, Connection con, int x) throws Exception {
    bcRfTrDocumentosFirmas doctosFirmas = null;
    try  {
      doctosFirmas = new bcRfTrDocumentosFirmas();
      doctosFirmas.setDocumentoFirmaId(request.getParameterValues("documentoFirmaId")[x]);
      doctosFirmas.select(con);
      doctosFirmas.setFirma(request.getParameterValues("tipoFirma")[x]);
      doctosFirmas.update(con);
    } catch (Exception ex)  {
      throw (ex);
    } finally  {
    }
    
  }
  
  private void modificarAgregarFirmas(HttpServletRequest request, Autentifica aut, Connection con)  throws Exception {
    String[] documentoFirmaId = request.getParameterValues("documentoFirmaId");
       
    if (documentoFirmaId == null) {
        return;
    }
    
    for(int x=0; x<documentoFirmaId.length; x++) {
      if(documentoFirmaId[x]==null || documentoFirmaId[x].equals("")) {
        insertarDocumentosFirmas(request,aut,con,x);
        modificarFirmasAutorizadas(request,aut,con,x);
      } else {
        modificarDocumentosFirmas(request,aut,con,x);
        modificarFirmasAutorizadas(request,aut,con,x);
      }
    }
  }

  private void modificar(HttpServletRequest request, Autentifica aut, Connection con)  throws Exception {
    eliminarFirmas(request,aut,con);
    modificarAgregarFirmas(request,aut,con);
  }
 %>




<%
 
 Connection conexion=null;
 
 Autentifica.setPagina("c715Control");
 try{
    conexion=DaoFactory.getContabilidad();
    conexion.setAutoCommit(false);
    
    modificar(request,Autentifica,conexion);
  
      
   //}             
 
 conexion.commit();  
 
%>
<br><br>
<table align='center' class='general'>
  <thead>
    <tr>
      <th class='azulObs'><b>      </b></th>
      <!--<th class='azulObs'><b>Fecha Actual</b> [< % =controlRegistro.getFechaAfectacion() % >]</th>-->
    </tr>
  </thead>
  <tbody>
    <tr>
      <td align="center" >
     <p> <font color='#003399'>Las firmas del documento <b><%=request.getParameter("documento")%></b> han sido modificadas satisfactoriamente</font></p>
     
<%

}
catch(Exception E){
    conexion.rollback(); 
     System.out.println("Error en pagina en "+E.getMessage()); 
%>
     <p>Ha ocurrido un error al accesar la Base de Datos,</p>
     <p>favor de reportarlo al Administrador del Sistema.</p>
     <p>Gracias.</p>
<%
}

finally{
   if (conexion != null){
       conexion.close();
       conexion=null;
   }  
}
%>
      </td>
    </tr>
  </tbody>
</table>
<br>
  <hr class="piePagina">
<br>
<input type="hidden" name=idCatalogo value=<%=(String) request.getAttribute("idCatalogoCuenta")%> >
<input type="hidden" name="mes" id="mes" value="<%=session.getAttribute("mes")%>">
<table width="100%" align="center">
 <tr>
     <td width="100%" align="center">
     <INPUT TYPE="submit" NAME="btnAceptar" VALUE="Aceptar" class="boton" >
    </td>
  </tr>
</table>
</FORM>
</body>
</html>