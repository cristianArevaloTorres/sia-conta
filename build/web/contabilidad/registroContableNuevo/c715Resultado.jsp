
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ page import="sia.libs.fun.tabla.*" %>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="controlRegistro" class="sia.rf.contabilidad.registroContable.acciones.ControlRegistro" scope="session"/>

<jsp:useBean id="pbDocumentos" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="pbCierresMen" class="sia.rf.contabilidad.registroContableNuevo.bcCierresMensuales" scope="page"/>
<jsp:useBean id="pag" class="sia.libs.fun.tabla.PaginacionSE" scope="page"/>
<%
  String contexto = request.getContextPath();
  session.setAttribute("mes",request.getParameter("mes"));
%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>c715Resultado</title>
  <link rel="stylesheet" href="<%=contexto%>/Librerias/Estilos/siaexplorer.css" type='text/css'>
    <script language="javascript" type="text/javascript">
      function llamamisma(parametro) {
        
        forma.param.value = parametro
        
        
        forma.action="<%=contexto%>/contabilidad/registroContableNuevo/c715Resultado.jsp?mes=<%=session.getAttribute("mes")%>";
        forma.submit();
      }
      
      function validaEliminar() {
        if(confirm('Realmente quiere eliminar la forma en cuestion?'))
          return true;
        else
          return false;
      }
    </script>
  </head>
  <body>
<%
  try  {        
    pbDocumentos.addParam("ejercicio",controlRegistro.getEjercicio());
    pbDocumentos.addParamVal("mes","and mes=:param",session.getAttribute("mes"));
    pbDocumentos.addParamVal("unidad","and unidad_ejecutora=':param'",controlRegistro.getUnidad());
    pbDocumentos.addParamVal("entidad","and entidad=:param",controlRegistro.getEntidad());
    pbDocumentos.addParamVal("ambito","and ambito=:param",controlRegistro.getAmbito());
    pbDocumentos.addParamVal("pais","and pais=:param","147");
   
    
    pbDocumentos.registrosMap(DaoFactory.CONEXION_CONTABILIDAD,"firmasAutorizadas.select.RfTrDocumentosContables-Resultado");
    
    pag.addCampo(new Campo("documento","Documento",Campo.AL_IZQ,null));
    pag.addImg(new Img("c715Modificar.jsp",Img.IMG_MODIFICAR, "", "documento_contable_id", false, "Modificar firmas autorizadas"));
    pag.addImg(new Img("c715Consultar.jsp",Img.IMG_CONSULTAR, "", "documento_contable_id", false, "Consultar firmas autorizadas"));
    
    
    int param = request.getParameter("param") != null ? Integer.parseInt(request.getParameter("param")) : 0;   
    
    
    
%>

    
    <%util.tituloPagina(out,"Contabilidad","Resultado de firmas autorizadas","Resultado",true);%>
    <form action="<%=contexto%>/contabilidad/registroContableNuevo/filtroGeneral.jsp?opcion=irFirmasAutorizadas" method="POST" id="forma" name="forma">
      <input type="hidden" name="param" id="param" value="<%=param%>">
      <input type="hidden" id="Pagina" name="Pagina" value="irFirmasAutorizadas">
      <br>
      <table align='center' class='general' width="40%" cellpadding="0" cellspacing="0">
        <thead></thead>
        <tbody>
          <tr>
            <td>
              <table align='center' class='general' width="100%">
                <thead>
                  <tr>
                    <th class='azulObs'>Unidad</th>
                    <th class='azulObs'>Entidad</th>
                    <th class='azulObs'>Ambito</th>
                  </tr>
                  
                </thead>
                <tbody>
                  <tr>
                    <td align="center"><%=controlRegistro.getUnidad()%></td>
                    <td align="center"><%=controlRegistro.getEntidad()%></td>
                    <td align="center"><%=controlRegistro.getAmbito()%></td>
                    
                  </tr>
                </tbody>
              </table>
            </td>
          </tr>
          <tr>
            <td>
              <table align='center' class='general' width="100%">
                <thead>
                  <tr>
                    <th class='azulObs'>Ejercicio</th>
                    <th class='azulObs'>Mes</th>
                  </tr>
                  
                </thead>
                <tbody>
                  <tr>
                    <td align="center"><%=controlRegistro.getEjercicio()%></td>
                    <td align="center"><%=session.getAttribute("mes")%></td>
                  </tr>
                </tbody>
              </table>
            </td>
          </tr>
        </tbody>
      </table>
      
      
      <br/>
      <br/>
      <br/>
      <%
      ///aqui si hay resultado>0, construir pagina, sino, construir botón recarga firmas
        pag.seleccionarPagina(pbDocumentos, 
                               out, 10, 
                               param, contexto+"/", 
                               "documento_contable_id",null,50,"Documentos que ya cuentan con firmas autorizadas");  
      } catch (Exception ex)  {
        ex.printStackTrace();
      } finally  {
      }
      
      
      %>
      <br>
      <hr class="piePagina">
      <br>
      <table align='center'>
        <thead></thead>
        <tbody>
          <tr>
            <td><input type="submit" class="boton" value="Regresar"></td>
          </tr>
        </tbody>
      </table>
    </form>
  </body>
</html>