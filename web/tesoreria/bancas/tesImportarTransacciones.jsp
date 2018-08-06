<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ page import="sia.libs.fun.tabla.*" %>
<%@ page import="sia.db.sql.Vista" %>
<%@ page import="sia.db.sql.SentennciasSE" %>
<%@ page import="sia.rf.tesoreria.bancas.acciones.Criterios"%>
<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/> 
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesImportarTransacciones</title>
    <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type="text/css">
    <script language="javascript" type="text/javascript">
    
       function regresa(){
          document.getElementById('form1').action='seleccionarArchivo.jsp';
          document.getElementById('form1').submit();
       }
       
       
      function ir(pagina) {
        if(pagina!='' && pagina!='null') {
          document.getElementById('form1').action = pagina;
          document.getElementById('form1').submit();
        }
      }
    
    </script>
  </head>
  <%!
   
    private String obtieneRegistro(Map parametro, String seccionXML, String campo){
        String regresa = null;
        List <Vista> registros= null;
        SentennciasSE sentenciasSE = new SentennciasSE(DaoFactory.CONEXION_TESORERIA);
        try  {
          registros = sentenciasSE.registros(seccionXML,parametro);
          if (registros != null){
            regresa = registros.get(0).getField(campo);
          }
          else 
            regresa = null;
        } catch (Exception e)  {
          e.printStackTrace();
          regresa = null; 
        } finally  {
          sentenciasSE = null;
        }
        return regresa;
     }
  
  %>
  <%
    Connection con    = null;
    String pagina     = "tesArchivoIncorrecto.jsp";
    String fechaBD    = null;
    List <Vista> ctasSinSaldoInicial = null;
    String fechaCarga = null;
    String mensaje = null;
    Map parametros = new HashMap(); 
    Criterios criterios = null;
    boolean mostrar = false;
    try  {
      mostrar = request.getParameter("idProgramaS").equals("7") || request.getParameter("idProgramaS").equals("6")?false:true;
      con = DaoFactory.getTesoreria();
      con.setAutoCommit(false);
      criterios = new Criterios();
      parametros.put("prog",request.getParameter("idProgramaS"));
      fechaBD = obtieneRegistro(parametros,"catalogos.select.RfTrBitacoraActualizacion.bancas","fecha");
      fechaCarga = obtieneRegistro(null,"criterios.select.unicaFecha.bancas","fecha");
      ctasSinSaldoInicial = criterios.getCuentasSinSaldoInicialArrastra(null,"criterios.select.ctaSinSaldo.bancas");
      if (ctasSinSaldoInicial != null){
        criterios.arrastaSaldosInicial(Integer.parseInt(request.getParameter("idProgramaS").toString()),fechaCarga,con, request);
        con.commit();
      }
      if (request.getParameter("idProgramaS").toString().equals("10") || request.getParameter("idProgramaS").toString().equals("6")){
          if (criterios.registrosDuplicados()!=0){
            pagina = "tesListaRegistrosDuplicados.jsp";
          }
          else{
            pagina = "tesImportarTransControl.jsp";
          }
      }
      else {
        pagina = "tesImportarTransControl.jsp";
      }
    } catch (Exception ex)  {
          ex.printStackTrace();
          con.rollback();
          mensaje = "Error al registrar las transacciones bancarias";
          
    } finally  {
          DaoFactory.closeConnection(con);
          con = null;
    }
    
  %>
  <body>
   <form id="form1" name="form1" action="seleccionarArchivo.jsp">
    <%util.tituloPagina(out,"Tesorería","Transaccionalidad bancaria","Listado de cuentas bancarias inexistentes",true);%>  
    <input type="hidden" id="nomProg" name="nomProg" value="<%=request.getParameter("nomProg")%>">
    <input type="hidden" id="opcion" name="opcion" value="<%=request.getParameter("opcion")%>">
    <input type="hidden" id="idProgramaS" name="idProgramaS" value="<%=request.getParameter("idProgramaS")%>">
    <input type="hidden" id="proceso" name="proceso" value="<%=request.getParameter("proceso")%>">
    <input type="hidden" id="fechaCarga" name="fechaCarga" value="<%=fechaCarga%>">
    <input type="hidden" id="mensaje" name="mensaje" value="<%=mensaje%>">
    <table>
        <thead></thead>
        <tbody>
           <tr align="left">
              <td style="color:rgb(0,99,148); font-style:italic; font-weight:bold;"><%=request.getParameter("opcion")%> >> <%=request.getParameter("nomProg")%></td>
           </tr>
        </tbody>
   </table>
   <br>
   <br>
   <table width="50%" align="center" class="general" cellpadding="5">
   <thead></thead>
        <tbody>
           <tr class="azulCla">
             <td colspan="4" >Importante</td>
           </tr>
           <tr>
             <td width="6%"></td>
             <td width="30%">Los últimos movimientos en la BD son del día:</td>
             <td width="3%"></td>
             <td width="11%"><%=fechaBD%></td>
           </tr>
           <tr>
             <td width="6%"></td>
             <td width="30%">Los movimientos que está importando son del día:</td>
             <td width="3%"></td>
             <td width="11%"><%=fechaCarga%></td>  
           </tr>
        </tbody>
   </table> 
   <table width="50%" align="center" >
   <thead></thead>
        <tbody>
              <tr <%=mostrar?"style='display:none'":""%>>
                <td align="right">
                  <input type="checkbox" name="recarga" id="recarga" value="1"> Recarga<br>
                </td>
              </tr>
        </tbody>
   </table> 
   <br>
   <br>
   <hr class="piePagina">
    <br>
    <table align="center">
      <thead></thead>
      <tbody>
        <tr>
          <td><input type="button" class="boton" value="Continuar" onclick="ir('<%=pagina%>')"></td>
          <td><input type="button" class="boton" value="Seleccionar otro archivo" onclick="regresa()"></td>
        </tr>
      </tbody>
    </table>
  </form>
  </body>
</html>