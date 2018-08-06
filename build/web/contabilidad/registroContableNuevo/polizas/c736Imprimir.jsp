<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="java.util.*"%>
<%//@ page import="sia.comun.Reporte"%>
<%@ page import="sia.scriptlets.Reporte"%>
<%@ page import="sia.libs.recurso.Contabilidad"%>
<%@ page import="sia.db.dao.DaoFactory"%>
<%@ page import="sia.db.sql.Sentencias"%>
<%@ page import="sia.rf.contabilidad.reportes.DatosReportes"%>
<%@ page import="sia.db.sql.Vista"%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
     <title>c736Imprimir</title>
    <script type="text/javascript" language="javascript">
      function envia() {
        if(document.getElementById("genera").value=='true')
          document.forma.submit();
      }
    </script>
  </head>
  
  <body onload="envia()">
    <form id="forma" name="forma" action="../../../Librerias/reportes/generaReporte.jsp">
  <%
      boolean genera = false;
      List<Vista> registros          = null; 
      try{
          DatosReportes datosReportes = new DatosReportes();
          String Ejercicio = request.getParameter("ejercicio");
          String Unidad = request.getParameter("unidad");
          String Entidad = request.getParameter("entidad");
          String Ambito = request.getParameter("ambito");
          registros = new ArrayList<Vista>();
          StringBuffer query = new StringBuffer();
          Map mapParams = new HashMap();
          //Reporte repClase = new Reporte();    
          registros = datosReportes.obtenerTitulos(Unidad,Entidad,Ambito);
          query.append("select ROWNUM CONSFILA, sqlresul.* from (select t.mes, m.descripcion, t.unidad_ejecutora, t.entidad||t.ambito ambito,sum(decode(t.tipo_poliza_id,1,1,0)) Diario, sum(decode(t.tipo_poliza_id,2,1,0)) Cheque,");
          query.append(" sum(decode(t.tipo_poliza_id,3,1,0)) Egreso, sum(decode(t.tipo_poliza_id,4,1,0)) Ingreso from rf_tr_polizas t, tc_meses m  where t.id_catalogo_cuenta=1 and t.ejercicio=").append(Ejercicio);
          query.append(" and t.unidad_ejecutora=").append(Unidad).append(" and t.tipo_poliza_id in(1,2,3,4) and t.entidad=").append(Entidad).append(" and t.ambito=").append(Ambito);
          query.append(" and t.mes=m.id_mes group by  t.mes, m.descripcion,t.unidad_ejecutora, t.entidad||t.ambito order by t.unidad_ejecutora, t.entidad||t.ambito, t.mes) sqlresul order by CONSFILA");
          //System.out.println(query);
          mapParams.put("IMG_DIR",request.getSession().getServletContext().getRealPath("/Librerias/Imagenes/"));
          mapParams.put("TITULO1",registros==null?"T1":registros.get(0).getField("TITULO1")); 
          mapParams.put("TITULO2",registros==null?"T2":registros.get(0).getField("TITULO2")); 
          mapParams.put("TITULO3",registros==null?"T3":registros.get(0).getField("TITULO3")); 
          request.getSession().setAttribute("abrirReporte", "abrir");
          request.getSession().setAttribute("nombreArchivo","listadoPolizas");
          request.getSession().setAttribute("rutaArchivo", "/contabilidad/registroContableNuevo/polizas/formato/listadoPolizas");
          request.getSession().setAttribute("parametros", mapParams);
          request.getSession().setAttribute("conexion", DaoFactory.CONEXION_CONTABILIDAD);
          request.getSession().setAttribute("query", query);
          request.getSession().setAttribute("formatoSalida", "pdf");
          request.getSession().setAttribute("accionPagina", "Generacion de reporte del listado de pólizas");
          request.getSession().setAttribute("modulo", "Contabilidad");
          /**repClase.imprimir(query, mapParams, "Polizas", 
              "/contabilidad/registroContableNuevo/polizas/formato/listadoPolizas", 
          DaoFactory.CONEXION_CONTABILIDAD, "pdf", "Generacion del reporte de pólizas", "Contabilidad"); */             
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




   
