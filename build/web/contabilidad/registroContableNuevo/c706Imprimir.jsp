<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="java.util.*"%>
<%@ page import="sia.comun.Reporte"%>
<%@ page import="sia.libs.recurso.Contabilidad"%>
<%@ page import="sia.db.dao.DaoFactory"%>
<%@ page import="sia.db.sql.Sentencias"%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>c706Imprimir</title>
    <script type="text/javascript" language="javascript">
      function envia() {
        if(document.getElementById("genera").value=='true')
          document.forma.submit();
      }
    </script>
  </head>
  
  <body onload="envia()">
    <form id="forma" name="forma" action="../../Librerias/reportes/generaReporte.jsp">
  <%
      boolean genera = false;
      try{
          String cuentaBancaria = request.getParameter("cuentaBancaria");
          String consecutivoChueque = request.getParameter("noCheque");
          String fechaDesde = request.getParameter("fechaDesde");
          String fechaHasta = request.getParameter("fechaHasta");
          String importe = request.getParameter("importe");
          String concepto = request.getParameter("concepto");
          String beneficiario = request.getParameter("beneficiario");
          String consecutivoPoliza = request.getParameter("poliza");
          String estatus = request.getParameter("estatus");
          String lsEjercicio= request.getParameter("ejercicio");
          Sentencias sentencias = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD,Sentencias.XML);    
          StringBuffer query = new StringBuffer();
          Map mapParams = new HashMap();
          Reporte repClase = new Reporte();    
          String condicion = "";
            condicion = condicion + "and p.ejercicio="+lsEjercicio + "\n";
          if(!cuentaBancaria.equals(""))
            condicion = condicion + "and cb.id_cuenta=" + cuentaBancaria + "\n";
          if(!consecutivoChueque.equals(""))
            condicion = condicion + " and c.consecutivo=" + consecutivoChueque + "\n";
          if(!fechaDesde.equals(""))
            condicion=condicion + " and to_date(to_char(c.fechacheque,'dd/MM/yyyy'),'dd/MM/yyyy')>=to_date('"+fechaDesde+"','dd/MM/yyyy') and to_date(to_char(c.fechacheque,'dd/MM/yyyy'),'dd/MM/yyyy')<=to_date('"+fechaHasta+"','dd/MM/yyyy') \n";
          if(!importe.equals(""))
            condicion = condicion + " and c.importe=" + importe + "\n";
          if(!concepto.equals(""))
            condicion = condicion + " and c.referencia='" + concepto +"' \n";
          if(!beneficiario.equals(""))
            condicion = condicion + " and c.beneficiario='" + beneficiario + "' \n";
          if(!consecutivoPoliza.equals(""))
            condicion = condicion + " and p.consecutivo=" + consecutivoPoliza.substring(1) + " \n and tp.abreviatura='" + consecutivoPoliza.substring(0,1) + "' \n";
          if(!estatus.equals(""))
            condicion = condicion + " and c.estatus = " + estatus;
        
          query.append(sentencias. getComando("reportes.select.cheques", "condicion=".concat(condicion)));
         // System.out.println(query);
          mapParams.put("FECHA_DESDE",fechaDesde.equals("")?"01/01/"+sia.libs.formato.Fecha.getAnioActual():fechaDesde);
          mapParams.put("FECHA_HASTA",fechaHasta.equals("")?sia.libs.formato.Fecha.getHoy():fechaHasta);
          mapParams.put("IMG_DIR",session.getServletContext().getRealPath("").concat("/Librerias/Imagenes/"));
          //mapParams.put("IMG_DIR",request.getSession().getServletContext().getRealPath("/Librerias/Imagenes/").toString());
          repClase.imprimir(request,query, mapParams, "Cheques", 
              "/contabilidad/registroContableNuevo/cheques/reportes/cheques", 
          DaoFactory.CONEXION_CONTABILIDAD, "pdf", "Generacion del reporte de cheques", "Contabilidad");              
          genera = true;
      }//end try
      catch(Exception e){
          System.err.println("Error al generar el reporte");
       }                   
  %>
    <input type="hidden" name="genera" id="genera" value="<%=genera%>">
    </form>
  </body>
</html>