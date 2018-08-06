<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page  import="java.util.*,java.sql.*"%>
<%@ page  import="sia.db.dao.*"%>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ page import="sia.rf.contabilidad.registroContableNuevo.formas.bcRfTrFormasFunciones" %>


<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/> 
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>c728EventosFormasControl</title>
    <link rel="stylesheet" href="../../../../Librerias/Estilos/siaexplorer.css" type="text/css">
  </head>
  <%!
    public void insertarEnGrupo(Connection con, HttpServletRequest request) throws Exception {
      String[] ids = request.getParameterValues("idFunBase");
      bcRfTrFormasFunciones funciones = new bcRfTrFormasFunciones();
      for (int i = 1; i < ids.length; i++)  {
        funciones.setIdfuncion(ids[i]);
        funciones.insertInGroup(con,request.getParameter("forma"));
      }
      if(ids.length <= 1)
        throw new Exception("No hay error, pero selecciono ninguna funcion, por tanto no se hizo registro ninguno");
    }
    
    private void eliminarRegistros(Connection con, HttpServletRequest request) throws Exception {
      String[] idsFunciones = request.getParameter("eliminados").split(",");
      bcRfTrFormasFunciones funciones = new bcRfTrFormasFunciones();
      String idForma = request.getParameter("idForma");
      for (int i = 0; i < idsFunciones.length; i++)  {
        if(!idsFunciones[i].equals("")) {
          funciones.setFormaContableId(idForma);
          funciones.setIdfuncion(idsFunciones[i]);
          funciones.delete(con);
        }
      }
    }
    
    private void modificarRegistros(Connection con, HttpServletRequest request) throws Exception {
      String[] ids = request.getParameterValues("idFunBase");
      bcRfTrFormasFunciones funciones = new bcRfTrFormasFunciones();
      for (int i = 1; i < ids.length; i++)  {
        if(request.getParameterValues("ihnuevo")[i].equals("1")) {
          funciones.setIdfuncion(ids[i]);
          funciones.setFormaContableId(request.getParameter("idForma"));
          funciones.insert(con);
        }
      }
    }
  %>
  <%
    Connection con  = null;
    String mensaje  = null;
    String pagina   = null;
    String evento = request.getParameter("fevento") != null ? request.getParameter("fevento") : "";
    String forma = request.getParameter("fforma") != null ? request.getParameter("fforma") : "";
    String nombreVariable = request.getParameter("fnombreVariable") != null ? request.getParameter("fnombreVariable") : "";
    String tipo = request.getParameter("ftipo") != null ? request.getParameter("ftipo") : "";
    try  {
      Integer accion = Integer.parseInt(request.getParameter("accion"));
      con = DaoFactory.getContabilidad();
      con.setAutoCommit(false);
      switch(accion) {
        case 1:
          pagina = "c728EventosFormasAgregar.jsp";
          insertarEnGrupo(con,request);
          mensaje= "Se insertaron las funciones para todas las formas " + request.getParameter("forma");
          break;
        case 2:
          pagina = "c728EventosFormasResultado.jsp";
          eliminarRegistros(con,request);
          modificarRegistros(con,request);
          mensaje= "Se modificó con éxito la forma " + request.getParameter("forma");
          break;
      }
      con.commit();
    } catch (Exception ex)  {
      mensaje = ex.getMessage()==null ? "Posiblemente un null (inconsistencia de informacion)" : ex.getMessage();
      ex.printStackTrace();
      con.rollback();
    } finally  {
      DaoFactory.closeConnection(con);
    }
    
    
    
  %>
  <body>
    <form name="form1" id="form1" action="<%=pagina%>" method="post">
      <%util.tituloPagina(out,"Contabilidad","Funciones de formas contables","Control",true);%>
      <input type="hidden" name="fevento" id="fevento" value="<%=evento%>">
      <input type="hidden" name="fforma" id="fforma" value="<%=forma%>">
      <input type="hidden" name="fnombreVariable" id="fnombreVariable" value="<%=nombreVariable%>">
      <input type="hidden" name="ftipo" id="ftipo" value="<%=tipo%>">
      <br>
      <br>
      <br>
      <table align="center" class="azulClaCen" width="70%">
        <thead></thead>
        <tbody>
          <tr>
            <td><%=mensaje%></td>
          </tr>
        </tbody>
      </table>
      <br>
      <hr class="piePagina">
      <br>
      <table align="center">
        <thead></thead>
        <tbody>
          <tr>
            <td><input type="submit" class="boton" value="Aceptar"></td>
          </tr>
        </tbody>
      </table>
    </form>
  </body>
</html>