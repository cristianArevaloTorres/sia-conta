<%@ page contentType="text/html;charset=UTF-8"%>
<% response.setHeader("Pragma","no-cache");%>
<% response.setHeader("Cache-Control","no-store");%>
<% response.setDateHeader("Expires",-1);%>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ page import="sia.libs.fun.tabla.*" %>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="crsListadoMov" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="pag" class="sia.libs.fun.tabla.PaginacionSE" scope="page"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>tesListadoMovReclaReferencia</title>
    <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type="text/css">
    <script language="javascript" type="text/javascript">
    
       function llamamisma(pag){
        document.getElementById('pagina').value=pag;
        document.form1.submit();
       }
       
       function asignaPagina(){
           document.form1.action='tesFiltroCtaReclaReferencia.jsp';
           document.form1.submit();
       }
    
    </script>
  </head>
  <body>
  <form id="form1" name="form1" action="tesListadoMovReclaReferencia.jsp" method="POST">
    <%util.tituloPagina(out,"Tesorería","Reclasificación de referencia bancaria ","Listado de movimientos bancarios",true);%>
    <br>
    <br>
    <%
      String cuentas[] = null;
      String idCuentasSelecciondas = ""; 
      int param = request.getParameter("pagina") != null ? Integer.parseInt(request.getParameter("pagina")) : 0;
      
      cuentas = request.getParameterValues("cuentas");
      for (int i = 0; i < cuentas.length; i++){
        idCuentasSelecciondas = idCuentasSelecciondas + cuentas[i].concat(",");
      }
      idCuentasSelecciondas = idCuentasSelecciondas.substring(0,idCuentasSelecciondas.length()-1);
      
      crsListadoMov.addParam("idCuenta",idCuentasSelecciondas);
      crsListadoMov.addParam("fechaInicio",request.getParameter("fechaInicio"));
      crsListadoMov.addParam("fechaFinal",request.getParameter("fechaFinal"));
      crsListadoMov.registrosMap(DaoFactory.CONEXION_TESORERIA,"movimientos.select.listadoMovimientos.reclaReferencia");
      pag.addCampo(new Campo("num_cuenta","Num. cuenta",Campo.AL_CEN,null));  
      pag.addCampo(new Campo("FECHA_HORA","Fecha",Campo.AL_CEN,null));  
      pag.addCampo(new Campo("MONTO","Monto",Campo.AL_DER,Campo.FORMATO_DOS_DEC));  
      pag.addCampo(new Campo("CLAVE_TRANS_RECLA","Cve. trans",Campo.AL_CEN,null));  
    //  pag.addCampo(new Campo("NUMERO_CHEQUE","Cheque",Campo.AL_DER,null));  
      pag.addCampo(new Campo("DESCRIPCION","Descripción",Campo.AL_IZQ,null));
      pag.addCampo(new Campo("REFERENCIA","Referencia",Campo.AL_IZQ,null));
      pag.addCampo(new Campo("REFERENCIA_ANT","Referencia original banco",Campo.AL_IZQ,null));
      pag.addImg(new Img("tesModificaReclaReferencia.jsp",Img.IMG_MODIFICAR,"pagina="+param+"&cuentas="+idCuentasSelecciondas+"&fechaInicio="+request.getParameter("fechaInicio")+"&fechaFinal="+request.getParameter("fechaFinal"),"ID_CUENTA,ID_MOVIMIENTO,num_cuenta,",false,"Modificar"));
      pag.seleccionarPagina(crsListadoMov, out, 10, param, "../../", "ID_CUENTA,ID_MOVIMIENTO,num_cuenta",null,"Listado de movimientos bancarios");
    %>
      <input type="hidden" name="pagina" id="pagina" value="<%=param%>"/>
      <input type="hidden" name="cuentas" id="cuentas" value="<%=idCuentasSelecciondas%>"/>
      <input type="hidden" name="fechaInicio" id="fechaInicio" value="<%=request.getParameter("fechaInicio")%>"/>
      <input type="hidden" name="fechaFinal" id="fechaFinal" value="<%=request.getParameter("fechaFinal")%>"/>
      <br>
      <hr class="piePagina">
      <br>
      <table align="center">
        <thead></thead>
        <tbody>
          <tr>
            <td><input type="button" class="boton" value="Regresar" onclick="asignaPagina()"></td>
          </tr>
        </tbody>
      </table>
    </form>
  </body>
</html>