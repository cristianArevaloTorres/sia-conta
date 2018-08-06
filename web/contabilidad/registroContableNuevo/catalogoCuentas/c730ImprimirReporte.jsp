<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>  
<%@ page import="java.util.*,sia.rf.contabilidad.registroContable.acciones.ControlRegistro, sia.libs.formato.Error"%>
<%@ page import="sia.db.sql.Vista,sia.rf.contabilidad.reportes.DatosReportes, sia.rf.contabilidad.reportes.catalogoCuentas.ConsultasCatalogoCuentas"%>
<%@ page import="sia.libs.formato.Cadena,sia.libs.periodo.Fecha, sia.db.dao.DaoFactory"%>
<jsp:useBean id="fechaAfectacion" class="sia.rf.contabilidad.registroContable.servicios.FechaAfectacion" scope="page"/>
<jsp:useBean id="Autentifica" class="sia.beans.seguridad.Autentifica" scope="session"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>c730ImprimirReporte</title>
    <link rel="stylesheet" href="../../../Librerias/Estilos/siaexplorer.css" type="text/css">
    <script type="text/javascript" language="javascript">
      function envia() {
        if(document.getElementById("genera").value=='true')
          document.getElementById("forma").submit();
      }
    </script>
  </head>
  <body onload="envia()">
  <jsp:scriptlet>util.tituloPagina(out, "Contabilidad", " ", "Resultado", "Generación de reporte", true);</jsp:scriptlet> 
<%
    String pagina = null;
    StringBuffer sentencia = new StringBuffer();
    Map mapParams = new HashMap();
    //Reporte repClase = new Reporte();  
    List<Vista> registros = new ArrayList<Vista>();
    List<String> rangos = new ArrayList<String>();
    DatosReportes datosReportes = new DatosReportes();
    boolean genera = false;
    ControlRegistro controlReg = (ControlRegistro)request.getSession().getAttribute("controlRegistro");
    
    String fechaReporte = null;
    String reporte = null;
    ConsultasCatalogoCuentas consultas = null;
    String formato = request.getParameter("rbtFormato");
    String rangoUnidad = request.getParameter("rangoUnidad");
    String[] lsCtas=request.getParameterValues("txtRango");
    boolean saldosCero = Boolean.valueOf(request.getParameter("saldosCero"));
    String mes = request.getParameter("cboMes");
    String diaMes = null;
    String strMesActual = null;
    String strMesAnterior = null;
    try  {
      controlReg.setEjercicio(Integer.valueOf(request.getParameter("lstEjercicio")));
      diaMes = Cadena.rellenar(String.valueOf(sia.libs.formato.Fecha.getDiasEnElMes(controlReg.getEjercicio(),Integer.valueOf(mes)-1)),2,'0',true);
    if(formato.equals("0"))
      formato = "pdf";
    else
      formato = "xls";
    for(int i=0; i<lsCtas.length; i++){
      String rango1 = (i==2 && lsCtas[i].equals(""))?"0":(i!=2 && lsCtas[i].equals(""))?"0000":lsCtas[i];
      i++;
      String rango2 = (i==3 && lsCtas[i].equals(""))?"0":(i!=3 && lsCtas[i].equals(""))?"0000":lsCtas[i];
      rangos.add(rango1+","+rango2);
    }

        if(controlReg.getTipoUsuario() == 1 ){
          if(!lsCtas[6].equals("") && !lsCtas[7].equals("") && lsCtas[6].equals(lsCtas[7])){
            controlReg.setUnidad(lsCtas[6].substring(1,4));
            controlReg.setUniEjecFormateada(lsCtas[6]);
            if(rangoUnidad.equals("rangoTodos")){
              if(lsCtas[6].equals("0002") && lsCtas[7].equals("0002"))
                rangos.set(3, "0000,9999");
            }
          }
          else{
            controlReg.setUnidad("0002");
            controlReg.setUniEjecFormateada(lsCtas[6]);
          }
          if(!(lsCtas[8].equals("") && lsCtas[9].equals("")) && !(lsCtas[8].equals("0000") && lsCtas[9].equals("0000")) && lsCtas[8].equals(lsCtas[9])){
            controlReg.setEntidad(Integer.valueOf(lsCtas[8].substring(1,3)));
            controlReg.setAmbito(Integer.valueOf(lsCtas[8].substring(3,4)));
          }
          else{
            //lsCtas[8].equals("") && lsCtas[9].equals("") && 
            if(!(lsCtas[6].equals("") && lsCtas[7].equals("")) && lsCtas[6].equals(lsCtas[7]) && !(lsCtas[6].equals("0000") && lsCtas[7].equals("9999")) && !(Integer.valueOf(lsCtas[6]) >= 100 && Integer.valueOf(lsCtas[6]) <= 120)){
              controlReg.setEntidad(1);
              controlReg.setAmbito(2);
            }
            else{
              controlReg.setEntidad(1);
              controlReg.setAmbito(1);
            }
          }
          if(lsCtas[6].equals("0000") && lsCtas[7].equals("0000") && lsCtas[8].equals("0000") && lsCtas[9].equals("0000")){
            controlReg.setUnidad("0002");
            controlReg.setEntidad(9);
            controlReg.setAmbito(1);
          }
        }else{
            if(controlReg.getTipoUsuario() == 2){
              controlReg.setUniEjecFormateada(lsCtas[6]);
              controlReg.setUnidad(lsCtas[6].substring(1,4));
              //controlReg.setEntidad(Integer.valueOf(lsCtas[8].substring(1,3)));
              if(Autentifica.getAmbito().equals("1"))
                controlReg.setAmbito(1);
              else
                controlReg.setAmbito(2);
            }
            else{
              controlReg.setUniEjecFormateada(lsCtas[6]);
              controlReg.setUnidad(lsCtas[6].substring(1,4));
              controlReg.setEntidad(Integer.valueOf(lsCtas[8].substring(1,3)));
              controlReg.setAmbito(Integer.valueOf(lsCtas[8].substring(3,4)));
            }
            
        }


   /*   controlReg.setEntidad(1);
    if(controlReg.getAmbito()==0)
      controlReg.setAmbito(1);
    if(controlReg.getUnidad().equals("000"))
      controlReg.setUnidad("100");*/
    consultas = new ConsultasCatalogoCuentas(request.getParameter("cierre"), controlReg);
    if(request.getParameter("rbtRepte").equals("0"))
      reporte = "cuentasContablesConSaldo";
    else 
      reporte = "cuentasColectivas";
      fechaReporte = String.valueOf(controlReg.getEjercicio()).concat(mes).concat(diaMes);
      Fecha fechaPeriodo = new Fecha(fechaReporte, "/");
      strMesActual = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodo.getMes() - 1);
      fechaPeriodo.addMeses(-1);
      strMesAnterior = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodo.getMes() - 1);
      if ( controlReg.getFechaAfectacion()==null)
         fechaAfectacion.establecerFechaAfectacion(request,fechaAfectacion.obtenerFechaAfectacion());
      if(formato.equals("pdf")){ 
        pagina = "generaReporte.jsp";
        registros = datosReportes.obtenerTitulos(controlReg.getUnidad(),String.valueOf(controlReg.getEntidad()),String.valueOf(controlReg.getAmbito()));
        sentencia.append(consultas.reporteSaldosRangos(fechaReporte, formato, reporte, rangos, saldosCero,strMesActual,strMesAnterior));
        //System.out.println(sentencia);
        mapParams.put("EJERCICIO",controlReg.getEjercicio());
        mapParams.put("FECHA","" + sia.libs.formato.Fecha.formatear(sia.libs.formato.Fecha.FECHA_LARGA, fechaReporte));
        mapParams.put("ID_CATALOGO_CUENTA", controlReg.getIdCatalogoCuenta());
        mapParams.put("MES_ANTERIOR", strMesAnterior);
        mapParams.put("MES_ACTUAL", strMesActual);
        mapParams.put("REPORTE", reporte);
        mapParams.put("PROGRAMA", request.getParameter("txtTitulo"));
        mapParams.put("TITULO1",registros.get(0).getField("TITULO1"));
        mapParams.put("TITULO2",registros.get(0).getField("TITULO2"));
        mapParams.put("TITULO3",registros.get(0).getField("TITULO3"));
        mapParams.put("FECHA_ACTUAL", sia.libs.formato.Fecha.formatear(sia.libs.formato.Fecha.FECHA_LARGA,controlReg.getFechaAfectacion()));
        mapParams.put("IMG_DIR",request.getSession().getServletContext().getRealPath("/Librerias/Imagenes/").toString());
        /*repClase.imprimir(request, sentencia, mapParams, reporte, "/contabilidad/registroContable/reportes/".concat(reporte), 
        DaoFactory.CONEXION_CONTABILIDAD, "pdf", "Generacion de reporte de cuentas contables", "Contabilidad");*/
      }
      else{
          reporte = "cuentasContablesConSaldoExcel";
          sentencia.append(consultas.reporteSaldosRangos(fechaReporte, formato, reporte, rangos, saldosCero,strMesActual,strMesAnterior));
          pagina = "generaCsv.jsp";
          mapParams = null;
          /*repClase.imprimir(request, sentencia, mapParams,
            "ArchivoXls", "/contabilidad/registroContable/reportes/", DaoFactory.CONEXION_CONTABILIDAD,
            "", "Generar archivo XLS", "Contabilidad");    */
      }
    request.getSession().setAttribute("abrirReporte", "abrir");
    request.getSession().setAttribute("nombreArchivo", reporte);
    request.getSession().setAttribute("rutaArchivo", "/contabilidad/registroContable/reportes/"+reporte);
    request.getSession().setAttribute("parametros", mapParams);
    request.getSession().setAttribute("conexion", DaoFactory.CONEXION_CONTABILIDAD);
    request.getSession().setAttribute("query", sentencia);
    request.getSession().setAttribute("formatoSalida", formato);
    request.getSession().setAttribute("accionPagina", "Generacion de reporte");
    request.getSession().setAttribute("modulo", "Contabilidad");
    genera = true;
    
    }//end try
    catch (Exception e){
      e.printStackTrace();
      Error.mensaje(e, "CONTABILIDAD");
    }//end catch
    finally{    
      mapParams = null;
      //repClase = null;
      registros = null;
      datosReportes = null;
      //consultasReportes = null;
    }
%>
 <form id="forma" name="forma" action="../../../Librerias/reportes/<%=pagina%>">
    <br><br><br><br><br>
<table border="0" width="100%" class='sianoborder' align="center">
<%
if(genera){
%>
<tr align="center"><td>Generando el reporte, espere un momento...</td></tr>
<%
}else{
%><tr align="center"><td>Hubo un error al momento de generar el reporte, notifíquelo al administrador....</td></tr>
<%}%>
</table>
<BR><hr noshade="noshade" width="40%" align="center" style="border-width: 1px;border-style:solid">
<input type="hidden" name="genera" id="genera" value="<%=genera%>">
   </form>
  </body>
</html>