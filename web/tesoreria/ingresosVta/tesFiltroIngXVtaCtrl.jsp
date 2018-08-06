<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.libs.formato.Fecha" %>
<%@ page import="sia.libs.formato.Cadena" %>
<%@ page import="sia.db.dao.DaoFactory"%>
<%@ page import="sia.db.sql.SentennciasSE"%>
<%@ page import="sia.db.sql.Vista"%>
<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<jsp:useBean id="Autentifica" class="sia.beans.seguridad.Autentifica" scope="session"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesFiltroIngXVtaCtrl</title>
 <script language="JavaScript"  type="text/javascript">
     
       function ir(){
            document.getElementById('form1').action='../../Librerias/reportes/generaReporte.jsp';
            document.getElementById('form1').submit();
       }

    </script>
  </head>
     <%!
    private  sia.libs.periodo.Fecha estableceFormato(String fecha){
        sia.libs.periodo.Fecha regresa = null;
        try  {
          regresa = new sia.libs.periodo.Fecha(fecha, "/");
        } catch (Exception ex)  {
           ex.printStackTrace();
        }
        return regresa;
    }
    
    private String getCuentas(HttpServletRequest request){
        String idCuentasSelecciondas = ""; 
        String cuentas[] = null; 
        cuentas = request.getParameterValues("ingXVta");
        if (request.getParameterValues("ingXVta")!=null){
            for (int i = 0; i < cuentas.length; i++)
                idCuentasSelecciondas = idCuentasSelecciondas + cuentas[i].concat(",");
        }
        idCuentasSelecciondas = idCuentasSelecciondas.substring(0,idCuentasSelecciondas.length()-1);  
        return idCuentasSelecciondas;
    }
    
    private String getQueryReporte(HttpServletRequest request, String nomQuery){
        String regresa = null;
        SentennciasSE sentencia = new SentennciasSE(DaoFactory.CONEXION_TESORERIA);
        try  {      
            sentencia.addParam("fechaInicial",Fecha.formatear(Fecha.FECHA_CORTA_GUION,estableceFormato(request.getParameter("fechaInicio")).toString()));
            sentencia.addParam("fechaFinal",Fecha.formatear(Fecha.FECHA_CORTA_GUION,estableceFormato(request.getParameter("fechaFinal")).toString()));
            sentencia.addParam("idCuentas",getCuentas(request));
            regresa = sentencia.getCommand("reportes.select.".concat(nomQuery).concat(".ingresosVtas"));
        } catch (Exception e)  {
           e.printStackTrace();
        }  finally  {
              sentencia = null;
        }
        return regresa; 
    }
    
    private String getFechaGeneracion(){
      String regresa = null;
      SentennciasSE sentencias = null;
      List<Vista> registros = null;
      try  {
        sentencias = new SentennciasSE(DaoFactory.CONEXION_TESORERIA);
        registros = sentencias.registros("select to_char(sysdate,'dd/mm/yy HH24:mm:ss') fechaOrigen from dual");
        if (registros!=null){
          regresa = registros.get(0).getField("FECHAORIGEN");
        }
      } catch (Exception ex)  {
            ex.printStackTrace();
      } finally  {
        sentencias = null;
        registros = null;
      }
      return regresa;
    }
 
    
  %>
  <%
    String sTipoReporte = null;
    String tituloReporte = null;
    String resultado  = null;
    String entidad = null;
    StringBuffer queryReporte = new StringBuffer();
    Map parametros = new HashMap();
    
    if (request.getParameter("referenciaRep").equals("1")){
        sTipoReporte = "ingresosVentaAdministrativos";
        tituloReporte = "Listado de movimientos por referencia bancaria";
    }
    else{
        sTipoReporte = "referenciasInvalidas";
        tituloReporte = "Listado de movimientos con referencias inválidas";
    }
    
    if (request.getParameter("tesAdm").equals("1")){
        resultado = getQueryReporte(request,sTipoReporte).concat(" order by  mov.id_cuenta, mov.id_movimiento");
    }
    else{
         resultado = getQueryReporte(request,sTipoReporte);
         if (request.getParameter("referenciaRep").equals("2")){
            resultado = resultado.concat(" order by mov.id_cuenta, mov.id_movimiento");
         }
         else{
            if (Autentifica.getAmbito().equals("2") || Autentifica.getAmbito().equals("1")){
                resultado = resultado.concat(" and substr(mov.referencia,1,3) = '").concat(Autentifica.getUnidadEjecutora()).concat("' order by mov.id_cuenta, mov.id_movimiento");
            }
            else{
                if (Autentifica.getEntidad().length() < 2){
                    entidad = Cadena.rellenar(Autentifica.getEntidad(),2,'0',true);
                }
                else
                    entidad = Autentifica.getEntidad();
                  resultado = resultado.concat(" and substr(mov.referencia,1,3) = '").concat(Autentifica.getUnidadEjecutora()).concat("' and substr(mov.referencia,4,2) = '").concat(entidad).concat("' order by mov.id_cuenta, mov.id_movimiento");
            }
        }
    }
    
        parametros.put("IMG_DIR",request.getSession().getServletContext().getRealPath("/Librerias/Imagenes/"));
        parametros.put("FECHAORIGEN",getFechaGeneracion());
        parametros.put("FECHA_PERIODO", "del ".concat( Fecha.formatear(Fecha.FECHA_LARGA,estableceFormato(request.getParameter("fechaInicio")).toString())).concat(" al ").concat( Fecha.formatear(Fecha.FECHA_LARGA,estableceFormato(request.getParameter("fechaFinal")).toString())));
        parametros.put("FECHA_INICIAL",request.getParameter("fechaInicio"));
        parametros.put("FECHA_FINAL",request.getParameter("fechaFinal"));
        if (request.getParameter("referenciaRep").equals("1"))
            parametros.put("TITULO","Listado de movimientos por referencia bancaria");
        else
            parametros.put("TITULO","Listado de movimientos con referencias inválidas");
        request.getSession().setAttribute("formatoSalida",request.getParameter("tipoReporte"));
        request.getSession().setAttribute("conexion",DaoFactory.CONEXION_TESORERIA);
        if (request.getParameter("tipoReporte").equals("pdf"))
            request.getSession().setAttribute("rutaArchivo","/tesoreria/ingresosVta/Reportes/ingresosXVta");
        else
            request.getSession().setAttribute("rutaArchivo","/tesoreria/ingresosVta/Reportes/ingresosXVtaXls");
        request.getSession().setAttribute("nombreArchivo","ingresosXVta");
        request.getSession().setAttribute("query",new StringBuffer(resultado)); 
        request.getSession().setAttribute("parametros",parametros);
    
  %>
   <body onload="ir()">
   <form id="form1" name="form1">
   </form>
  </body>
</html>