<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/> 
<jsp:useBean id="pbCuenta" class ="sia.rf.contabilidad.registroContableNuevo.bcCuentaContable" scope="page"/>
<%@ page import="java.util.*,sia.rf.contabilidad.registroContable.acciones.ControlRegistro, sia.libs.formato.Error"%>
<%@ page import="sia.db.sql.Vista,sia.rf.contabilidad.reportes.DatosReportes, sia.rf.contabilidad.reportes.catalogoCuentas.ConsultasCatalogoCuentas"%>
<%@ page import="sia.libs.formato.Cadena,sia.libs.periodo.Fecha, sia.db.dao.DaoFactory,java.sql.*"%>
<%@ page import="sia.rf.contabilidad.registroContable.servicios.Cuenta"%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1252"/>
    <title>c729ImprimirReporte</title>
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
<%  Connection conexion=null;
    String pagina = null;
    StringBuffer sentencia = new StringBuffer();
    Map mapParams = new HashMap();
    //Reporte repClase = new Reporte();  
    List<Vista> registros = new ArrayList<Vista>();
    DatosReportes datosReportes = new DatosReportes();
    boolean genera = false;
    ControlRegistro controlReg = (ControlRegistro)request.getSession().getAttribute("controlRegistro");
    
    String fechaReporte = null;
    String reporte = null;
    ConsultasCatalogoCuentas consultas = null;
    String cuentaContable = request.getParameter("cuentaContable");
    String mes = request.getParameter("lstMes");
    String formato = request.getParameter("formato");
    String diaMes = Cadena.rellenar(String.valueOf(sia.libs.formato.Fecha.getDiasEnElMes(controlReg.getEjercicio(),Integer.valueOf(mes)-1)),2,'0',true);
    String strMesActual = null;
    String strMesAnterior = null;
    int cuentaContableId= -1;
    Cuenta cuenta = null;
    String strCondicionDos = "";
    List alPadres = new ArrayList();
    String patronUnidadEjec = "0000";
    String patronEntAmb = "0000";
    String tipoPolizas = request.getParameter("tipoPolizas").equals("1")?"central":"unidades";
    //controlReg.setUniEjecFormateada(tipoPolizas.equals("1")?"0100":"0000");
    try  {
    tipoPolizas = controlReg.getTipoUsuario()==1?tipoPolizas:"unidades";
    if(controlReg.getTipoUsuario()==1){
      if(controlReg.getUnidad().equals("100") && tipoPolizas.equals("central"))
        controlReg.setUniEjecFormateada("0100");
      else
       controlReg.setUniEjecFormateada("0000");
    }
    conexion=DaoFactory.getContabilidad();
    cuentaContableId= Integer.valueOf(pbCuenta.selectCuentaContableId(conexion,cuentaContable,String.valueOf(controlReg.getIdCatalogoCuenta()),String.valueOf(controlReg.getEjercicio()))); 
    cuenta = new Cuenta(cuentaContableId,conexion);
     
    if(formato.equals("0"))
      formato = "pdf";
    else
      formato = "xls";

    consultas = new ConsultasCatalogoCuentas(request.getParameter("cierre"), controlReg, tipoPolizas);
    fechaReporte = String.valueOf(controlReg.getEjercicio()).concat(mes).concat(diaMes);
    Fecha fechaPeriodo = new Fecha(fechaReporte, "/");
    strMesActual = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodo.getMes() - 1);
    fechaPeriodo.addMeses(-1);
    strMesAnterior = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodo.getMes() - 1);
    String fechaLarga = sia.libs.formato.Fecha.formatear(sia.libs.formato.Fecha.FECHA_LARGA, fechaReporte);
    
    switch(Integer.valueOf(request.getParameter("btrReporte"))){
      case 1:
        alPadres = cuenta.getPadre(cuenta.getNivel().intValue());
        for (int i = 0; i < alPadres.size(); i++) {
          strCondicionDos = strCondicionDos + "cuenta_contable='"+alPadres.get(i)+"' or ";
        }
        if(alPadres.size()!=0){
          strCondicionDos = " or " + strCondicionDos;
          strCondicionDos = strCondicionDos.substring(0,strCondicionDos.length()-3);            
        }
        if(cuentaContableId == -1)
          strCondicionDos = "CUENTA_CONTABLE LIKE '%'".concat(strCondicionDos);
        patronUnidadEjec = cuenta.patronNivel(3);
        patronEntAmb = cuenta.patronNivel(4);
        reporte = "cuentasContablesSaldo";
        sentencia.append(consultas.sentenciaSaldos(formato, reporte, strMesActual, strMesAnterior, strCondicionDos, patronUnidadEjec, patronEntAmb, controlReg, cuentaContableId));
        //System.out.println("Query\\n"+sentencia);
        reporte = "cuentasContablesConSaldo";
        break;
      case 2:
      case 3:
        StringBuffer querySubReporte = new StringBuffer();
        String strFechaInicio = controlReg.getEjercicio()+mes+"01";
        String strFechaFinal = controlReg.getEjercicio()+mes+diaMes;
        String condicion = null;
        if(cuentaContableId==-1){
          condicion = "%";
        }
        else{
          //cuenta = new Cuenta(cuentaContableId);
          condicion = cuenta.getCuentaReporte(cuenta.getNivel().intValue());
        }
        reporte = "estadoCuentaPoliza";
        if(request.getParameter("btrReporte").equals("3")){
          reporte = "ValidacionDeCaptura";
          fechaPeriodo = new Fecha(fechaReporte, "/");
        }
        mapParams.put("FECHA_INI",sia.libs.formato.Fecha.formatear(2, fechaPeriodo.getInicioMes().toString()));
        mapParams.put("FECHA_FIN",sia.libs.formato.Fecha.formatear(2, fechaPeriodo.getTerminoMes().toString()));
        mapParams.put("FECHA",fechaLarga);
        mapParams.put("SUBREPORT_DIR",request.getSession().getServletContext().getRealPath("/contabilidad/registroContable/reportes/").toString());
        querySubReporte.append(consultas.sentenciaEstadoCuenta(cuentaContableId, formato, strMesActual, strMesAnterior, strFechaInicio, strFechaFinal, "subReporte", condicion));
        //System.out.println("Query subreporte: "+querySubReporte);
        mapParams.put("QUERY_SUBREPORTE",querySubReporte.toString());
        sentencia.append(consultas.sentenciaEstadoCuenta(cuentaContableId, formato, strMesActual, strMesAnterior, strFechaInicio, strFechaFinal, "reporte", condicion));
        //System.out.println("Query: "+ sentencia);
        break;
    }
      
      if(formato.equals("pdf")){ 
        pagina = "generaReporte.jsp";
        registros = datosReportes.obtenerTitulos(controlReg.getUnidad(),String.valueOf(controlReg.getEntidad()),String.valueOf(controlReg.getAmbito()));
        //System.out.println(sentencia);
        mapParams.put("FECHA",fechaLarga);
        mapParams.put("EJERCICIO",controlReg.getEjercicio());
        mapParams.put("ID_CATALOGO_CUENTA", controlReg.getIdCatalogoCuenta());
        mapParams.put("MES_ANTERIOR", strMesAnterior);
        mapParams.put("MES_ACTUAL", strMesActual);
        mapParams.put("PROGRAMA", request.getParameter("txtTitulo"));
        mapParams.put("TITULO1",registros==null?"T1":registros.get(0).getField("TITULO1"));
        mapParams.put("TITULO2",registros==null?"T2":registros.get(0).getField("TITULO2"));
        mapParams.put("TITULO3",registros==null?"T3":registros.get(0).getField("TITULO3"));
        mapParams.put("FECHA_ACTUAL", sia.libs.formato.Fecha.formatear(sia.libs.formato.Fecha.FECHA_LARGA,controlReg.getFechaAfectacion()));
        mapParams.put("IMG_DIR",request.getSession().getServletContext().getRealPath("/Librerias/Imagenes/").toString());
        /*repClase.imprimir(request, sentencia, mapParams, reporte, "/contabilidad/registroContable/reportes/"+reporte, 
        DaoFactory.CONEXION_CONTABILIDAD, "xls", "Generacion de reporte de cuentas contables", "Contabilidad");*/
      }
      else{
          formato="";
          reporte = "cuentasContablesExcel";
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
      cuenta = null;
      if (conexion!=null){
         conexion.close();
         conexion=null;
       }
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