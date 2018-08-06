<%-- 
    Document   : c761Resultado
    Created on : 12/08/2013, 03:18:15 PM
    Author     : EST.JOSE.FLORES
--%>

<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ page import="sia.libs.fun.tabla.*" %>
<%@page import="java.util.*,java.sql.*"%>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="controlRegistro" class="sia.rf.contabilidad.registroContable.acciones.ControlRegistro" scope="session"/>

<jsp:useBean id="pbDocumentos" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="pag" class="sia.libs.fun.tabla.PaginacionSE" scope="page"/>
<jsp:useBean id="descripcionGrupo" class="sun.jdbc.rowset.CachedRowSet" scope="page">
  <%
   descripcionGrupo.setTableName("sg_tc_grupo");
   String SQL = " SELECT descripcion from sg_tc_grupo where cve_grupo ="+request.getParameter("clave");
   descripcionGrupo.setCommand(SQL);
   Connection con=null;
   try{
     con = DaoFactory.getContabilidad();
     descripcionGrupo.execute(con);
   } catch(Exception e){
        System.out.println("Ocurrio un error al accesar el metodo consultaGenero "+e.getMessage());
   } finally{
      if (con!=null){
          con.close();
          con=null;
      }
   } //Fin finally
 %>
</jsp:useBean>
<%
  String contexto = request.getContextPath();
  session.setAttribute("claveGrupo",request.getParameter("clave"));
%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>c715Resultado</title>
  <link rel="stylesheet" href="<%=contexto%>/Librerias/Estilos/siaexplorer.css" type='text/css'>
    <script language="javascript" type="text/javascript">
      function llamamisma(parametro) {
        forma.param.value = parametro
        forma.action="../perfil/c761Resultado.jsp?clave=<%=session.getAttribute("claveGrupo")%>";
        forma.submit();
      }
      function regresar(){ 
        window.location.href="../grupos/c760Resultado.jsp";
      }
    </script>
  </head>
  <body>
<%
  try  {        
    pbDocumentos.addParam("idGrupo",Integer.parseInt(request.getParameter("clave")));
    
    pbDocumentos.registrosMap(DaoFactory.CONEXION_CONTABILIDAD,"Seguridad.select.Perfiles-Resultado");
    
    pag.addCampo(new Campo("clave","Clave",Campo.AL_IZQ,null));
    pag.addCampo(new Campo("descripcion","Descripcion",Campo.AL_IZQ,null));
    pag.addCampo(new Campo("usuarios","Usuarios",Campo.AL_IZQ,null));
    
    pag.addImg(new Img("c761ModificarFormulario.jsp",Img.IMG_MODIFICAR,"","documento_contable_id", false, "Modificar perfil"));
    pag.addImg(new Img("c761ConsultarFormulario.jsp",Img.IMG_ELIMINAR,"","documento_contable_id", false, "Eliminar perfil"));
    pag.addImg(new Img("../arbol/c764Formulario.jsp",Img.IMG_SEG_ARBOL_MENU,"", "", false, "Actualizar árbol Menú"));
    pag.addImg(new Img("../usuarios/c762Resultado.jsp",Img.IMG_SEG_USUARIOS,"", "documento_contable_id", false, "Ir a usuarios"));
    
    int param = request.getParameter("param") != null ? Integer.parseInt(request.getParameter("param")) : 0;   
%>
    <%util.tituloPagina(out,"Contabilidad","Resultado de Perfiles","Resultado",true);%>
    <form action="" method="POST" id="forma" name="forma">
      <input type="hidden" name="param" id="param" value="<%=param%>">
      <input type="hidden" id="Pagina" name="Pagina" value="irFirmasAutorizadas">
        <table align="center" cellPadding="3">
            <tr class="resultado"> 
                <td> <div align="center">
                        <img src="../../Librerias/Imagenes/botonAgregar2.gif" width="16" height="16">
                        <a href="c761AgregarFormulario.jsp">Agregar</a> 
                    </div>
                </td>
            </tr>     
        </table>
<%
    String grupo="";
    descripcionGrupo.beforeFirst(); 
    while(descripcionGrupo.next()){
          grupo = descripcionGrupo.getString("descripcion");
    }
    pag.seleccionarPagina(pbDocumentos,out,10,param,contexto+"/","clave",null,50,"Perfiles del grupo ".concat("[ ").concat(grupo).concat(" ]"));  
  } catch (Exception ex)  {
        ex.printStackTrace();
  } finally  { }
%>
      <br><br><br>
      <hr class="piePagina">
      <br>
      <table align='center'>
        <thead></thead>
        <tbody>
          <tr>
            <td><input type="button" class="boton" value="Regresar a grupos" onclick="regresar();"></td>
          </tr>
        </tbody>
      </table>
    </form>
  </body>
</html>