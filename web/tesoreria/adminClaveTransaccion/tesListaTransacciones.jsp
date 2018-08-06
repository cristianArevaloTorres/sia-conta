<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ page import="sia.libs.fun.tabla.*" %>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="pbFormas" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="pag" class="sia.libs.fun.tabla.PaginacionSE" scope="page"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesListaTransacciones</title>
      <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type="text/css">
    <script language="javascript" type="text/javascript">
    
       function llamamisma(pag){
        document.getElementById('pagina').value=pag;
        document.form1.submit();
       }
       
          function regresar(){
           document.getElementById('form1').action='tesListaCtasBanca.jsp';
           document.getElementById('form1').target='';
           document.getElementById('form1').submit();
       } 
    
    </script>
  </head>
 
   <body >
   <form id="form1" name="form1" action="tesListaTransacciones.jsp" method="POST">
    <%util.tituloPagina(out,"Tesorería","Listado de movimientos bancarios","Modificar clave transacción ",true);%>
    <input type="hidden" id="nomProg" name="nomProg" value="<%=request.getParameter("nomProg")%>">
    <input type="hidden" id="opcion" name="opcion" value="<%=request.getParameter("opcion")%>">
    <input type="hidden" id="idProgramaS" name="idProgramaS" value="<%=request.getParameter("idProgramaS")%>">
    <input type="hidden" id="proceso" name="proceso" value="<%=request.getParameter("proceso")%>">
    <br>
    <br>
    <%
      String nomOpcion = request.getParameter("opcion");
      String nomPrograma = request.getParameter("nomProg");
      String idProg = request.getParameter("idProgramaS");
      String nomProceso = request.getParameter("proceso");
      String fechaI = request.getParameter("fechaInicio");
      String fechaF = request.getParameter("fechaFinal");
      String ctas = request.getParameter("ctaSelec");
      int param = request.getParameter("pagina") != null ? Integer.parseInt(request.getParameter("pagina")) : 0;
      pbFormas.addParam("pCuentas",request.getParameter("ctaSelec"));
      pbFormas.addParam("pFechaIni",request.getParameter("fechaInicio"));
      pbFormas.addParam("pFechaFin",request.getParameter("fechaFinal"));
      pbFormas.registrosMap(DaoFactory.CONEXION_TESORERIA,"criterios.select.listaClavesTrans.modClaveTrans");
      pag.addCampo(new Campo("num_cuenta","No. cuenta",Campo.AL_CEN,null));  
      pag.addCampo(new Campo("fechaHoraFormato","Fecha",Campo.AL_CEN,null));  
      pag.addCampo(new Campo("monto","Monto",Campo.AL_DER,Campo.FORMATO_DOS_DEC));  
      pag.addCampo(new Campo("clave_trans","Clave trans.",Campo.AL_DER,null));  
      pag.addCampo(new Campo("numero_cheque","Cheque",Campo.AL_DER,null));  
      pag.addCampo(new Campo("descripcion","Descripción",Campo.AL_IZQ,null));  
      pag.addCampo(new Campo("referencia","Referencia",Campo.AL_IZQ,null));  
      pag.addCampo(new Campo("clave_trans_recla","Trans. reclasificada",Campo.AL_DER,null)); 
      pag.addImg(new Img("tesListaTransCtrl.jsp",Img.IMG_MODIFICAR,"opcion="+nomOpcion+"&nomProg="+nomPrograma+"&idProgramaS="+idProg+"&proceso="+nomProceso+"&fechaInicio="+fechaI+"&fechaFinal="+fechaF+"&ctaSelec="+ctas,"id_movimiento,id_cuenta",false,"Modificar"));
      pag.seleccionarPagina(pbFormas, out, 10, param, "../../", "id_movimiento,id_cuenta",null,"Listado de transacciones bancarias");
    %>
    <input type="hidden" name="pagina" id="pagina" value="<%=param%>"/>
    <input type="hidden" name="fechaInicio" id="fechaInicio" value="<%=request.getParameter("fechaInicio")%>"/>
    <input type="hidden" name="fechaFinal" id="fechaFinal" value="<%=request.getParameter("fechaFinal")%>"/>
    <input type="hidden" id="ctaSelec" name="ctaSelec" value="<%=request.getParameter("ctaSelec")%>"/>
    <br>
    <hr class="piePagina">
    <br>
      <table align="center">
      <thead></thead>
      <tbody>
        <tr>
           <td><input type="button" class="boton" value="Regresar" onclick="regresar()"></td>
        </tr>
      </tbody>
    </table>
    </form>
  </body>
</html>