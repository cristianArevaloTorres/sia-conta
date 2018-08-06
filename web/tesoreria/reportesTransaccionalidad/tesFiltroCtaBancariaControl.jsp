<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="java.util.*"%>
<%@ page import="sia.libs.formato.Fecha"%>
<%@ page import="sia.db.dao.DaoFactory"%>
<%@ page import="sia.db.sql.Vista"%>
<%@ page import="sia.rf.tesoreria.administrarComisiones.servicios.DiaAnteriorHabil"%>
<%@ page import="sia.db.sql.SentennciasSE"%>
<% response.setHeader("Pragma","no-cache");%>
<% response.setHeader("Cache-Control","no-store");%>
<% response.setDateHeader("Expires",-1);%> 
<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesFiltroCtaBancariaControl</title>
    <script language="javascript" type="text/javascript"> 
     
        function ir(){
            
                document.getElementById('form1').action='../../Librerias/reportes/generaReporte.jsp';
                document.getElementById('form1').submit();
        }
    
    </script>
  </head>
  <%!
    private String getFechaGeneracion(){
        String regresa = null;
        SentennciasSE sentencia = null;
        List<Vista> registros = null;
        try  {
          sentencia = new SentennciasSE(DaoFactory.CONEXION_TESORERIA);
          registros = sentencia.registros("select to_char(sysdate,'dd/mm/yy HH24:mm:ss') fechaOrigen from dual");
          if (registros!=null){
            regresa = registros.get(0).getField("FECHAORIGEN");
          }
        } catch (Exception ex)  {
           ex.printStackTrace();
        } finally  {
          sentencia = null;
          registros = null;
        }
        return regresa;
    }
  
  
    private String getCuentas(HttpServletRequest request){
    String idCuentasSelecciondas = ""; 
    String cuentas[] = null; 
    String combos[] = {"smDispGC","smDispSP","smChqGC","smChqSP","smEnlace","smIngXV","smDonativo"};
    for (int j = 0; j < combos.length; j++)  {
        cuentas = request.getParameterValues(combos[j]);
        if (request.getParameterValues(combos[j])!=null){
            for (int i = 0; i < cuentas.length; i++)
                idCuentasSelecciondas = idCuentasSelecciondas + cuentas[i].concat(",");
        }
    }
    idCuentasSelecciondas = idCuentasSelecciondas.substring(0,idCuentasSelecciondas.length()-1);  
    return idCuentasSelecciondas;
    }
 
    private  sia.libs.periodo.Fecha estableceFormato(String fecha){
        sia.libs.periodo.Fecha regresa = null;
        try  {
          regresa = new sia.libs.periodo.Fecha(fecha, "/");
        } catch (Exception ex)  {
           ex.printStackTrace();
        }
        return regresa;
    }
  
    private String getQueryReporte(String idQuery, HttpServletRequest request, int numReporte){
    String regresa = null;
    String modulo = null;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    SentennciasSE sentencia = new SentennciasSE(DaoFactory.CONEXION_TESORERIA);
    try  {
        if ( sdf.parse(request.getParameter("fechaFinal")).after(sdf.parse("31/01/2012")) && numReporte==17 &&  request.getParameter("idProgramaS").equals("7") )
            sentencia.addParam("idCuentas","524,1051,1055,1060,".concat(getCuentas(request)));
        else 
            sentencia.addParam("idCuentas",getCuentas(request));
        if (numReporte!=17)
           sentencia.addParam("fechaInicial",Fecha.formatear(Fecha.FECHA_CORTA_GUION,estableceFormato(request.getParameter("fechaInicio")).toString()));
        else{
            DiaAnteriorHabil diaAnterior = new DiaAnteriorHabil();
            sentencia.addParam("fechaInicial",diaAnterior.obtenerDiaAnteriorHabil("'".concat(request.getParameter("fechaFinal")).concat("'"),"diaAnteriorHabilP"));
            diaAnterior = null;
        }
        sentencia.addParam("fechaFinal",Fecha.formatear(Fecha.FECHA_CORTA_GUION,estableceFormato(request.getParameter("fechaFinal")).toString()));
        sentencia.addParam("idTipoPrograma",request.getParameter("idProgramaS"));
        if (idQuery.equals("RendimientoGlobalGral") && numReporte==2 ){
            if (request.getParameter("idProgramaS").equals("8")){
                sentencia.addParam("clave"," AND mvc.id_clave_trans_recla = 1162 ");
            } else if (request.getParameter("idProgramaS").equals("10")){
                       sentencia.addParam("clave"," AND (mvc.id_clave_trans_recla = 47 OR mvc.id_clave_trans_recla = 69 OR mvc.id_clave_trans_recla = 177) AND mvc.descripcion LIKE '%INT%' ");
                   }
                   else if (request.getParameter("idProgramaS").equals("12")){
                            sentencia.addParam("clave"," AND (mvc.id_clave_trans_recla = 1201) ");
                        }
                        else {
                            sentencia.addParam("clave"," AND (mvc.id_clave_trans_recla = 47) ");
                        }
            sentencia.addParam("programa",request.getParameter("idProgramaS"));
        }
        if (idQuery.equals("RendimientosTodosProgramasGral") && numReporte==3 ){
          if (request.getParameter("idProgramaS").equals("10")){
                sentencia.addParam("clave"," AND (id_clave_trans_recla = 47 OR id_clave_trans_recla = 69 OR id_clave_trans_recla = 177) AND descripcion LIKE '%INT%'  ");
           }else if (request.getParameter("idProgramaS").equals("12")) {
                    sentencia.addParam("clave"," AND (id_clave_trans_recla = 1201) ");
                 } 
                 else {
                     sentencia.addParam("clave"," AND (id_clave_trans_recla = 47) ");
                 }
           sentencia.addParam("programa",request.getParameter("idProgramaS"));
        }
      regresa = sentencia.getCommand("reportes.select.".concat(idQuery).concat(".reportesMovimientos"));
    } catch (Exception e)  {
       e.printStackTrace();
    } 
    return regresa; 
  }
  
  %>
  <%
    StringBuffer queryReporte = new StringBuffer();
    String resultado  = null;
    String[] encabezados = null;
    int numReporte = 0;
    Map parametros = new HashMap();
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    numReporte = Integer.parseInt(request.getParameter("numReporte"));
    try {
        parametros.put("IMG_DIR",request.getSession().getServletContext().getRealPath("/Librerias/Imagenes/"));
        parametros.put("SUBREPORT_DIR","/tesoreria/reportesTransaccionalidad/Reportes/");
        parametros.put("PROGRAMA",request.getParameter("idProgramaS"));
        parametros.put("DESCRIPCION_PROGRAMA", request.getParameter("nomProg"));
        parametros.put("CUENTAS",getCuentas(request));
        parametros.put("FECHAORIGEN",getFechaGeneracion());
        if (numReporte!=17){
          parametros.put("FECHA_PERIODO", "del ".concat( Fecha.formatear(Fecha.FECHA_LARGA,estableceFormato(request.getParameter("fechaInicio")).toString())).concat(" al ").concat( Fecha.formatear(Fecha.FECHA_LARGA,estableceFormato(request.getParameter("fechaFinal")).toString())));
          parametros.put("FECHA_INICIAL",request.getParameter("fechaInicio"));
          parametros.put("FECHA_FINAL",request.getParameter("fechaFinal"));
        }
        request.getSession().setAttribute("formatoSalida",request.getParameter("tipoReporte"));
        request.getSession().setAttribute("conexion",DaoFactory.CONEXION_TESORERIA);
        switch (numReporte){
            case 1:
                int dias = 0;
                dias = new sia.libs.periodo.Fecha(estableceFormato(request.getParameter("fechaInicio"))).getDiasEnElMes();
                parametros.put("DIAS_MES", dias); 
                if (request.getParameter("tipoReporte").equals("pdf"))
                    request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesTransaccionalidad/Reportes/chequesCobradosBuenCobro");
                else
                    request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesTransaccionalidad/Reportes/chequesCobradosBuenCobroXls");
                request.getSession().setAttribute("nombreArchivo","chqDepositadoSBC");
                resultado = getQueryReporte("ChqsSalvoBuenCobro", request, numReporte);
                request.getSession().setAttribute("query",new StringBuffer(resultado)); 
                break;
            case 2:
                parametros.put("DIAS_MES", 0); 
                if (request.getParameter("tipoReporte").equals("pdf"))
                    request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesTransaccionalidad/Reportes/rendimientosGlobal");
                else
                    request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesTransaccionalidad/Reportes/rendimientosGlobalXls");
                request.getSession().setAttribute("nombreArchivo","rendimientosGral");
                if (request.getParameter("idProgramaS").equals("7"))
                    resultado = getQueryReporte("RendimientosGlobalBMX", request, numReporte);
                else if (request.getParameter("idProgramaS").equals("9"))
                        resultado = getQueryReporte("RendimientosGlobalEsp", request, numReporte);
                     else if (request.getParameter("idProgramaS").equals("8") || request.getParameter("idProgramaS").equals("10") || request.getParameter("idProgramaS").equals("11") || request.getParameter("idProgramaS").equals("12") ){
                            resultado = getQueryReporte("RendimientoGlobalGral", request, numReporte);
                          }
                          else
                            resultado = getQueryReporte("RendimientosGlobal", request, numReporte);
                request.getSession().setAttribute("query",new StringBuffer(resultado));     
            break;
            case 3:
                if (request.getParameter("idProgramaS").equals("1") || request.getParameter("idProgramaS").equals("6"))
                    resultado = getQueryReporte("RendimientosTodosProgramas",request, numReporte);
                else if (request.getParameter("idProgramaS").equals("7"))
                        resultado = getQueryReporte("RendimientosTodosProgramasBMX",request, numReporte);
                     else if (request.getParameter("idProgramaS").equals("9"))
                         resultado = getQueryReporte("RendimientosTodosProgramasEsp",request, numReporte);
                          else if (request.getParameter("idProgramaS").equals("8"))
                               resultado = getQueryReporte("RendimientosTodosProgramasBBVA",request, numReporte);
                               else if (request.getParameter("idProgramaS").equals("11") || request.getParameter("idProgramaS").equals("10")  || request.getParameter("idProgramaS").equals("12")  )
                                    resultado = getQueryReporte("RendimientosTodosProgramasGral",request, numReporte);
                parametros.put("DIAS_MES", 0); 
                if (request.getParameter("tipoReporte").equals("pdf"))
                    request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesTransaccionalidad/Reportes/rendimientosTodosProgramas");
                else
                    request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesTransaccionalidad/Reportes/rendimientosTodosProgramasXls");
                request.getSession().setAttribute("nombreArchivo","rendimientosDetalle");
                request.getSession().setAttribute("query",new StringBuffer(resultado)); 
            break; 
            case 4:
                resultado = getQueryReporte("ComIndebidasChq", request, numReporte); 
                parametros.put("DIAS_MES", 0); 
                if (request.getParameter("tipoReporte").equals("pdf"))
                    request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesTransaccionalidad/Reportes/comIndebidasChq");
                else
                    request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesTransaccionalidad/Reportes/comIndebidasChqXls");
                request.getSession().setAttribute("nombreArchivo","comIndebidaCheques");
                request.getSession().setAttribute("query",new StringBuffer(resultado)); 
            break;
            case 5:
                if (request.getParameter("idProgramaS").equals("7"))
                    resultado = getQueryReporte("TerminalPuntoVtaBMX", request, numReporte); 
                else
                    resultado = getQueryReporte("TerminalPuntoVta", request, numReporte); 
                if (request.getParameter("tipoReporte").equals("pdf"))
                    request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesTransaccionalidad/Reportes/terminalPuntoVenta");
                else
                    request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesTransaccionalidad/Reportes/terminalptovtaXls");
                parametros.put("DIAS_MES", 0); 
                request.getSession().setAttribute("nombreArchivo","TPV");
                request.getSession().setAttribute("query",new StringBuffer(resultado)); 
            break;
            case 6:
                if (request.getParameter("idProgramaS").equals("7") || request.getParameter("idProgramaS").equals("9")){
                    resultado = getQueryReporte("MovimientosCtasDetalleBMX", request, numReporte); 
                     if (request.getParameter("tipoReporte").equals("pdf"))
                        request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesTransaccionalidad/Reportes/MovimientosCuentasDetalleBMX");
                    else
                        request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesTransaccionalidad/Reportes/MovimientosCuentasDetalleBMXXls");
                }
                else if (request.getParameter("idProgramaS").equals("8")){
                        resultado = getQueryReporte("MovimientosCtasDetalleBBVA", request, numReporte); 
                        if (request.getParameter("tipoReporte").equals("pdf"))
                          request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesTransaccionalidad/Reportes/MovimientosCuentasDetalleBBVA");
                        else
                           request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesTransaccionalidad/Reportes/MovimientosCtasDetalleBBVAXls");
                     }
                     else {
                            resultado = getQueryReporte("MovimientosCtasDetalle", request, numReporte); 
                            if (request.getParameter("tipoReporte").equals("pdf"))
                              request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesTransaccionalidad/Reportes/MovimientosCuentasDetalle");
                            else
                              request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesTransaccionalidad/Reportes/MovimientosCuentasDetalleXls");
                     }
                parametros.put("DIAS_MES", 0); 
                request.getSession().setAttribute("nombreArchivo","movimientosDetalle");
                request.getSession().setAttribute("query",new StringBuffer(resultado));
            break;
            case 7:
                resultado = getQueryReporte("saldosDiarios", request, numReporte); 
                if (request.getParameter("fechaInicio").equals(request.getParameter("fechaFinal"))){
                    if (request.getParameter("tipoReporte").equals("pdf"))
                        request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesTransaccionalidad/Reportes/saldosDiariosV");
                    else
                        request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesTransaccionalidad/Reportes/saldosDiariosVXLS");
                    }
                else{
                    if (request.getParameter("tipoReporte").equals("pdf"))
                        request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesTransaccionalidad/Reportes/saldosDiarios");
                    else
                        request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesTransaccionalidad/Reportes/saldosDiariosXls");
                    }
                request.getSession().setAttribute("nombreArchivo","saldosDiarios");
                parametros.put("SIA_SUBDATA_QUERY",resultado);
                parametros.put("DIAS_MES", 0);  
                request.getSession().setAttribute("query",new StringBuffer("select * from dual"));        
            break;
            case 8:
                resultado = getQueryReporte("MovimientosCtasTotalDia", request, numReporte); 
                parametros.put("DIAS_MES", 0); 
                if (request.getParameter("tipoReporte").equals("pdf"))
                    request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesTransaccionalidad/Reportes/movCtasTotalesDia");
                else
                    request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesTransaccionalidad/Reportes/movCtasTotalesDiaXls");
                request.getSession().setAttribute("nombreArchivo","movimientosTotales");
                request.getSession().setAttribute("query",new StringBuffer(resultado)); 
            break;
            case 9:
                resultado = getQueryReporte("ChqsPagados", request, numReporte); 
                parametros.put("DIAS_MES", 0); 
                if (request.getParameter("tipoReporte").equals("pdf"))
                    request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesTransaccionalidad/Reportes/chequesPagadosGC");
                else
                    request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesTransaccionalidad/Reportes/chequesPagadosGCXls");
                request.getSession().setAttribute("nombreArchivo","chqsPagados");
                request.getSession().setAttribute("query",new StringBuffer(resultado)); 
            break;
            case 10:
                resultado = getQueryReporte("ReversoComisiones", request, numReporte); 
                parametros.put("DIAS_MES", 0); 
                if (request.getParameter("tipoReporte").equals("pdf"))
                    request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesTransaccionalidad/Reportes/reversoComisionesBanco");
                else
                    request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesTransaccionalidad/Reportes/reversoComisionesBancoXls");
                request.getSession().setAttribute("nombreArchivo","revComisiones");
                request.getSession().setAttribute("query",new StringBuffer(resultado)); 
            break;
            case 11:
                resultado = getQueryReporte("TotalReversoCom", request, numReporte); 
                parametros.put("DIAS_MES", 0); 
                if (request.getParameter("tipoReporte").equals("pdf"))
                    request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesTransaccionalidad/Reportes/totalReversoComisiones");
                else
                    request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesTransaccionalidad/Reportes/totalReversoComisionesXls");
                request.getSession().setAttribute("nombreArchivo","revComisionesTotal");
                request.getSession().setAttribute("query",new StringBuffer(resultado)); 
            break;
            case 12:
                resultado = getQueryReporte("TotalComisionesCobradas", request, numReporte); 
                parametros.put("DIAS_MES", 0); 
                if (request.getParameter("tipoReporte").equals("pdf"))
                    request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesTransaccionalidad/Reportes/totalComCobradasBco");
                else
                    request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesTransaccionalidad/Reportes/totalComCobradasBcoXls");
                request.getSession().setAttribute("nombreArchivo","totalComisiones");
                request.getSession().setAttribute("query",new StringBuffer(resultado)); 
            break;
            case 13:
                resultado = getQueryReporte("TransaccionalidadCuentas", request, numReporte); 
                if (request.getParameter("tipoReporte").equals("pdf"))
                    request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesTransaccionalidad/Reportes/transaccionalidadCuentas");
                else
                    request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesTransaccionalidad/Reportes/transaccionalidadCuentasXls");
                request.getSession().setAttribute("nombreArchivo","transaccionalidad");
                parametros.put("SIA_SUBDATA_QUERY",resultado);
                parametros.put("DIAS_MES", 0);  
                request.getSession().setAttribute("query",new StringBuffer("select * from dual"));  
            break;
            case 14:
                resultado = getQueryReporte("DetalleComisionesCobradas", request, numReporte); 
                parametros.put("DIAS_MES", 0); 
                if (request.getParameter("tipoReporte").equals("pdf"))
                    request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesTransaccionalidad/Reportes/comCobradasBanco");
                else
                    request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesTransaccionalidad/Reportes/comCobradasBancoXls");
                request.getSession().setAttribute("nombreArchivo","comCobradasDetalle");
                request.getSession().setAttribute("query",new StringBuffer(resultado)); 
            break;
            case 15:
                resultado = getQueryReporte("MovimientosCuentasPropias", request, numReporte); 
                parametros.put("DIAS_MES", 0); 
                if (request.getParameter("tipoReporte").equals("pdf"))
                    request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesTransaccionalidad/Reportes/movimientosCuentasPropias");
                else
                    request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesTransaccionalidad/Reportes/movimientosCuentasPropiasXls");
                request.getSession().setAttribute("nombreArchivo","movCtasPropias");
                request.getSession().setAttribute("query",new StringBuffer(resultado)); 
            break;
            case 16:
                resultado = getQueryReporte("ReferenciasIdentificadasReintegros", request, numReporte); 
                parametros.put("DIAS_MES", 0); 
                if (request.getParameter("tipoReporte").equals("pdf"))
                    request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesTransaccionalidad/Reportes/referenciasIdentificadas");
                else
                    request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesTransaccionalidad/Reportes/referenciasIdentificadasXls");
                request.getSession().setAttribute("nombreArchivo","reintegros");
                request.getSession().setAttribute("query",new StringBuffer(resultado));   
            break;
            case 17:
                String fecha = null;
                if (request.getParameter("idProgramaS").equals("7")){
                    if (sdf.parse(request.getParameter("fechaFinal")).after(sdf.parse("31/01/2012"))){
                        resultado = getQueryReporte("consolidacionSaldosNL", request, numReporte); 
                        if (request.getParameter("tipoReporte").equals("pdf"))
                           request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesTransaccionalidad/Reportes/consSaldosBancariosNL");
                        else
                            request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesTransaccionalidad/Reportes/consSaldosBanNLXls");
                        request.getSession().setAttribute("nombreArchivo","consolidacionSaldosNL");
                    }
                    else {
                        resultado = getQueryReporte("consolidacionSaldosBMX", request, numReporte); 
                        if (request.getParameter("tipoReporte").equals("pdf"))
                           request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesTransaccionalidad/Reportes/consSaldosBancariosBMX");
                        else
                            request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesTransaccionalidad/Reportes/consSaldosBancariosBMXXls");
                        request.getSession().setAttribute("nombreArchivo","consolidacionSaldosBMX");
                    }
                }
                else{
                    resultado = getQueryReporte("consolidacionSaldos", request, numReporte);  
                    if (request.getParameter("tipoReporte").equals("pdf"))
                        request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesTransaccionalidad/Reportes/consSaldosBancarios");
                    else
                        request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesTransaccionalidad/Reportes/consSaldosBancariosXls");
                    request.getSession().setAttribute("nombreArchivo","consolidacionSaldos");
                }
                parametros.put("FECHA_FINAL","'".concat(request.getParameter("fechaFinal")).concat("'"));
                parametros.put("FECHA_PERIODO", Fecha.formatear(Fecha.FECHA_LARGA,estableceFormato(request.getParameter("fechaFinal")).toString()));
                DiaAnteriorHabil diaAnterior = new DiaAnteriorHabil();
                fecha = diaAnterior.obtenerDiaAnteriorHabil("'".concat(request.getParameter("fechaFinal")).concat("'"),"diaAnteriorHabilP");
                parametros.put("FECHA_INICIAL","'".concat(fecha.substring(8,10).concat("/").concat(fecha.substring(5,7)).concat("/").concat(fecha.substring(0,4))).concat("'"));
                diaAnterior = null;
                request.getSession().setAttribute("query",new StringBuffer(resultado));
            break;
            case 18:
                resultado = getQueryReporte("comisionesTPV", request, numReporte); 
                parametros.put("DIAS_MES", 0); 
                if (request.getParameter("tipoReporte").equals("pdf"))
                    request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesTransaccionalidad/Reportes/comisionesTPV");
                else
                    request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesTransaccionalidad/Reportes/comisionesTPVXls");
                request.getSession().setAttribute("nombreArchivo","comisionesTPV");
                request.getSession().setAttribute("query",new StringBuffer(resultado));   
            break;
        }
        request.getSession().setAttribute("parametros",parametros);
    } catch (Exception ex)  {
        ex.printStackTrace();
    } 
    
%>
  <body onload="ir()">
    <form id="form1" name="form1">
      <input type="hidden" id="nomProg" name="nomProg" value="<%=request.getParameter("nomProg")%>">
      <input type="hidden" id="opcion" name="opcion" value="<%=request.getParameter("opcion")%>">
      <input type="hidden" id="idProgramaS" name="idProgramaS" value="<%=request.getParameter("idProgramaS")%>">
      <input type="hidden" id="proceso" name="proceso" value="<%=request.getParameter("proceso")%>">
      <input type="hidden" id="nomReporte" name="nomReporte" value="<%=request.getParameter("nomReporte")%>">
      <input type="hidden" id="numReporte" name="numReporte" value="<%=request.getParameter("numReporte")%>">
      <input type="hidden" id="tipoReporte" name="tipoReporte" value="<%=request.getParameter("tipoReporte")%>">
    </form>
  </body>
</html>