<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.libs.formato.Formatos"%>
<%@ page import="java.util.*"%>
<%@ page import="sia.libs.recurso.Contabilidad"%>
<%@ page import="sia.db.dao.DaoFactory"%>
<%@ page import="sia.xml.Dml"%>
<%@ page import="sia.rf.contabilidad.registroContable.acciones.ControlRegistro"%>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>c710RepCuentasMayor</title>
    <script type="text/javascript" language="javascript">
      function envia() {
        if(document.getElementById("genera").value=='true')
          forma.submit();
      }
    </script>
  </head>
  
  <body onload="envia()">
    <form id="forma" name="forma" action="../../../Librerias/reportes/generaReporte.jsp">
  <%    
    ControlRegistro controlReg = (ControlRegistro)request.getSession().getAttribute("controlRegistro"); 
    Map mapParams = new HashMap();
    Dml dml = null;
    //Reporte repClase = new Reporte();
    
    sia.libs.formato.Fecha fecha = null;
    StringBuffer sentencia = new StringBuffer();
    int reporte = 0;
    boolean genera = false;
    
    try{  
       String condicion=" where ";    
       String lsGenero = request.getParameter("lstGenero");
       String lsGrupo = request.getParameter("lstGrupo");
       String lsClase = request.getParameter("lstClase");
       String lsFecha1 = request.getParameter("txtFecha01");
       String lsFecha2 = request.getParameter("txtFecha02");
       
       String ban="0";
     
       if (!lsGenero.equals("null")){
          condicion = condicion + " id_genero = '" + lsGenero+"' ";
          ban="1";}
                
       if (!lsGrupo.equals("null"))
          condicion = condicion + " and id_grupo = '" + lsGrupo+"' ";
       
       if (!lsClase.equals("null"))
          condicion = condicion + " and id_clase = '" + lsClase+"' ";
          
       if(!lsFecha1.equals("")){
          if (ban.equals("1")){
               condicion=condicion+" and (fecha_vig_ini >='"+lsFecha1+"' and fecha_vig_fin<='"+lsFecha2+"') "; 
          }else{
               condicion=condicion+" (fecha_vig_ini >=TO_DATE('"+lsFecha1+"')) and (fecha_vig_fin<=TO_DATE('"+lsFecha2+"')) "; 
       }    }
       
       if (condicion.equals(" where ")){
       condicion = condicion + " 0=0 ";
       }  
sentencia.append(" SELECT CUENTA_MAYOR,DESCRIPCION,NATURALEZA, FECHA_VIG_INI, ");
sentencia.append(" DECODE(FECHA_VIG_FIN,'01/01/9999','VIGENTE',FECHA_VIG_FIN) AS FECHA_VIGENTE,FECHA_VIG_FIN ");
sentencia.append(" FROM( ");
sentencia.append(" SELECT CUENTA_MAYOR, ");
sentencia.append(" DESCRIPCION, ");
sentencia.append(" CASE NATURALEZA WHEN 'D'  ");
sentencia.append(" THEN 'DEUDORA'  ");
sentencia.append(" WHEN 'A'  ");
sentencia.append(" THEN 'ACREEDORA'  ");
sentencia.append(" ELSE 'OTRA' END  ");
sentencia.append(" NATURALEZA,TO_CHAR(FECHA_VIG_INI, 'dd/mm/yyyy') AS FECHA_VIG_INI, ");
sentencia.append(" TO_CHAR(FECHA_VIG_FIN, 'dd/mm/yyyy') FECHA_VIG_FIN, ID_GRUPO, ID_GENERO, ID_CLASE ");
sentencia.append(" FROM RF_TC_CLASIFICADOR_CUENTAS) ").append(condicion);
sentencia.append(" ORDER BY CUENTA_MAYOR ");
           
      mapParams.clear();
      mapParams.put("CONDICION",condicion);
      mapParams.put("IMG_DIR",request.getSession().getServletContext().getRealPath("/Librerias/Imagenes/").toString());
      /*repClase.imprimir(request, sentencia, mapParams, "RepCuentasMayor", "/contabilidad/registroContableNuevo/cuentaMayor/RepCuentasMayor", 
      DaoFactory.CONEXION_CONTABILIDAD, "pdf", "Generacion de reporte de listado de Cuentas", "Contabilidad");*/
    request.getSession().setAttribute("abrirReporte", "abrir");
    request.getSession().setAttribute("nombreArchivo", "RepCuentasMayor");
    request.getSession().setAttribute("rutaArchivo", "/contabilidad/registroContableNuevo/cuentaMayor/RepCuentasMayor");
    request.getSession().setAttribute("parametros", mapParams);
    request.getSession().setAttribute("conexion", DaoFactory.CONEXION_CONTABILIDAD);
    request.getSession().setAttribute("query", sentencia);
    request.getSession().setAttribute("formatoSalida", "pdf");
    request.getSession().setAttribute("accionPagina", "Generacion de reporte");
    request.getSession().setAttribute("modulo", "Contabilidad");
      genera = true;
    }
    catch(Exception e){
      System.err.println("Error al generar el reporte de cuentas de mayor");
    }
      
  %>
    <input type="hidden" name="genera" id="genera" value="<%=genera%>">
    </form>
  </body>
</html>