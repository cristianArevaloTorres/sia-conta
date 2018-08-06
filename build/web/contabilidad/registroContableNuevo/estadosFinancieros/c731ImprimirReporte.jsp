<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="java.util.*,sia.rf.contabilidad.registroContable.acciones.ControlRegistro"%>
<%@ page import="sia.db.sql.Vista,sia.rf.contabilidad.reportes.DatosReportes"%>
<%@ page import="sia.libs.formato.Cadena,sia.libs.periodo.Fecha, sia.db.dao.DaoFactory,java.sql.Connection,sia.db.sql.Vista,sia.db.sql.Sentencias,java.util.List"%>
<%@ page import="sia.rf.contabilidad.reportes.estadosFinancieros.ConsultasEdosFinancieros"%>
<%@ page import="sia.rf.contabilidad.reportes.estadosFinancieros.SituacionFinancieraComparativo"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>   
<jsp:useBean id="fechaAfectacion" class="sia.rf.contabilidad.registroContable.servicios.FechaAfectacion" scope="page"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>c731ImprimirReporte</title>
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
    ConsultasEdosFinancieros consultas = null;
    String pagina = null;
    boolean genera = false;
    String reporte = null;
    String formatoReporte = null;
    StringBuffer query = new StringBuffer();
    Map mapParams = new HashMap();
    //Reporte repClase = new Reporte();    
    String unidad = null;
    //String entidad = null;
    String ambito = null;
    String programa = request.getParameter("programa");
    String activaEtiqueta = request.getParameter("activaEtiqueta");
    String textoPreliminar = request.getParameter("textoPreliminar");
    String nivel = request.getParameter("criterio");
    String cierre  = request.getParameter("cierre");
    String cierreCentral  = request.getParameter("central");
    String mes = request.getParameter("lstMes");
    String ejercicioCompara = request.getParameter("ejercicioCompara");
    String mesCompara = request.getParameter("lstMesCompara").equals("")?"1":request.getParameter("lstMesCompara");
    //String cuentasBancos = request.getParameter("lstCuentasBanco");
    List<Vista> registros = new ArrayList<Vista>();
    DatosReportes datosReportes = new DatosReportes();
    ControlRegistro controlReg = (ControlRegistro)request.getSession().getAttribute("controlRegistro");
    //String carpeta = "estadosFinancieros";
    Connection conexion = null;
    try{
    //conexion =  DaoFactory.getContabilidad();
    String diaMes = Cadena.rellenar(String.valueOf(sia.libs.formato.Fecha.getDiasEnElMes(controlReg.getEjercicio(),Integer.valueOf(mes)-1)),2,'0',true);
    String diaMesCompara = Cadena.rellenar(String.valueOf(sia.libs.formato.Fecha.getDiasEnElMes(Integer.valueOf(ejercicioCompara),Integer.valueOf(mesCompara)-1)),2,'0',true);
    String fecha = null;
    fecha = String.valueOf(controlReg.getEjercicio()) + request.getParameter("lstMes") + diaMes;
    Fecha fechaPeriodo = new Fecha(fecha, "/");
    if ( controlReg.getFechaAfectacion()==null)
         fechaAfectacion.establecerFechaAfectacion(request,fechaAfectacion.obtenerFechaAfectacion());
    String fechaTerminoMes = sia.libs.formato.Fecha.formatear(6, fechaPeriodo.getTerminoMes().toString()).toUpperCase();
    pagina = "generaReporte.jsp";
    if(controlReg.getUnidad().equals("100") || controlReg.getUnidad().equals("000") ){
      if(cierreCentral.equals("2"))
        controlReg.setUnidad("000");
      else
        controlReg.setUnidad("100");
    }
      switch (Integer.valueOf(nivel)) {
        case 1:
        case 3:{
          unidad = "0000";
          ambito = "0000";
          break;
        }
        case 4:{
          unidad = controlReg.getUniEjecFormateada();
          ambito = "0000";
          break;   
        }
        case 5:{
          unidad = controlReg.getUniEjecFormateada();
          ambito = controlReg.getAmbEntFormateada();
          break;
        }
      }
      consultas = new ConsultasEdosFinancieros(cierre,programa,nivel,unidad, ambito,request);  
     
      //Construye la consulta del reporte seleccionado
      switch (Integer.valueOf(request.getParameter("reporte"))) {
       //Balanza de Comprobación
       case 0:{
          reporte = "BCO";
          query.append(consultas.balanzaComprobacion(fecha));
          break;
       }
       //Estado de Situación Financiera 
       case 1:{
          conexion =  DaoFactory.getContabilidad();
          reporte = "EFC";
          mapParams.put("FECHA_MES_EJERCICIO_COMPARA", ejercicioCompara+mesCompara+"01");
          mapParams.put("EJERCICIO_COMPARA",Integer.valueOf(ejercicioCompara));
          query.append(consultas.situacionFinanciera( fecha, mes, mesCompara, ejercicioCompara));
          String strRutaArchivo = "/contabilidad/registroContableNuevo/estadosFinancieros/reportes/" + reporte;
          request.getSession().setAttribute("abrirReporte", "abrirEFC");
          request.getSession().setAttribute("nombreArchivo",reporte);
          request.getSession().setAttribute("rutaArchivo", strRutaArchivo);
          request.getSession().setAttribute("parametros", mapParams);
          request.getSession().setAttribute("modulo", "Contabilidad");
          request.getSession().setAttribute("conexion", DaoFactory.CONEXION_CONTABILIDAD);
          SituacionFinancieraComparativo sfc = new SituacionFinancieraComparativo();
          sfc.vaciarInformacion(conexion, consultas, fecha, mes, mesCompara, ejercicioCompara,1);
          request.getSession().setAttribute("edoFinancieroCom", sfc);
          request.getSession().setAttribute("accionPagina","Generacion de estado de situacion financiera comparativo");
          pagina="generaReporteDataSource.jsp";
          break;
        }               
        //Estado de Situacion Financiera detallado
        case 2:{
          conexion =  DaoFactory.getContabilidad();
          reporte = "EFD";
          mapParams.put("FECHA_MES_EJERCICIO_COMPARA", ejercicioCompara+mesCompara+"01");
          mapParams.put("EJERCICIO_COMPARA",Integer.valueOf(ejercicioCompara));
          query.append(consultas.situacionFinancieraDetallado( fecha, mes, mesCompara, ejercicioCompara));
          String strRutaArchivo = "/contabilidad/registroContableNuevo/estadosFinancieros/reportes/" + reporte;
          request.getSession().setAttribute("abrirReporte", "abrirEFC");
          request.getSession().setAttribute("nombreArchivo",reporte);
          request.getSession().setAttribute("rutaArchivo", strRutaArchivo);
          request.getSession().setAttribute("parametros", mapParams);
          request.getSession().setAttribute("modulo", "Contabilidad");
          request.getSession().setAttribute("conexion", DaoFactory.CONEXION_CONTABILIDAD);
          SituacionFinancieraComparativo sfc = new SituacionFinancieraComparativo();
          sfc.vaciarInformacion(conexion, consultas, fecha, mes, mesCompara, ejercicioCompara,2);
          request.getSession().setAttribute("edoFinancieroCom", sfc);
          request.getSession().setAttribute("accionPagina","Generacion de estado de situacion financiera comparativo");
          pagina="generaReporteDataSource.jsp";
          break;
        }                            
        //Estado Analitico del Activo
        case 13 :{
          reporte = "EAA";
          // METODO PARA EL QUERY DEL REPORTE
          //String variable = fecha.substring(8);
          //out.println("FECHA "+variable);
          //out.println("FECHA "+fecha.substring(0,4));
          query.append(consultas.analiticoActivo(fecha));
          break;
        }
        //Estado Analitico del Pasivo
        case 14:{
          reporte = "EAP";
          query.append(consultas.estadoAnaliticoPasivo(fecha));
          break;
        }
        //Estado de Actividades
        case 15:{
          reporte = "EAC";
          mapParams.put("FECHA_MES_EJERCICIO_COMPARA", ejercicioCompara+mesCompara+"01");
          query.append(consultas.estadoActividades( fecha, mes, mesCompara, ejercicioCompara));
          break;
        }
        //Estado de Variaciones en la Hacienda Pública / Patrimonio
        case 16: {
            reporte = "EVH";
            //mapParams.put("FECHA_MES_EJERCICIO_COMPARA", ejercicioCompara+mesCompara+"01");
            query.append(consultas.estadoVariacionHaciendaPublicaPatrimonio(fecha, mes, mesCompara, ejercicioCompara));

            Sentencias sentencia = null;
            List<Vista> registrosQuery = null;
            sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD, Sentencias.XML);
            registrosQuery = new ArrayList<Vista>();
            registrosQuery = sentencia.registros(query.toString());
            if (registrosQuery != null) {
                for (Vista registro : registrosQuery) {
                    if (registro.getField("DESCRIPCION") != null) {
                        if (registro.getField("DESCRIPCION").indexOf("Var") < 0) {
                            String nmonico = registro.getField("DESCRIPCION").substring(0, 1).toString();

                            mapParams.put("IMPORTE_".concat(nmonico.equals("H") ? "0_0" : nmonico.concat("_0")), registro.getField("IMPORTE_0") != null ? registro.getDouble("IMPORTE_0") : 0);
                            mapParams.put("IMPORTE_".concat(nmonico.equals("H") ? "0_1" : nmonico.concat("_1")), registro.getField("IMPORTE_1") != null ? registro.getDouble("IMPORTE_1") : 0);
                            mapParams.put("IMPORTE_".concat(nmonico.equals("H") ? "0_2" : nmonico.concat("_2")), registro.getField("IMPORTE_2") != null ? registro.getDouble("IMPORTE_2") : 0);
                            //mapParams.put("importe_".concat(registro.getField("DESCRIPCION").substring(0, 1).concat("_0")), registro.getDouble("IMPORTE_0"));
                            //mapParams.put("importe_".concat(registro.getField("DESCRIPCION").substring(0, 1).concat("_1")), registro.getDouble("IMPORTE_1"));
                            //mapParams.put("importe_".concat(registro.getField("DESCRIPCION").substring(0, 1).concat("_2")), registro.getDouble("IMPORTE_2"));
                        }
                    }
                }
            }
            break;
         }        
        // Estado de Flujo de Efectivo
        case 17:{
          reporte = "EFE";
          mapParams.put("FECHA_MES_EJERCICIO_COMPARA", ejercicioCompara+mesCompara+"01");
          query.append(consultas.estadoFlujoEfectivo( fecha, mes, mesCompara, ejercicioCompara));
          break;
        }
        // Estado de Actividades DETALLADO
        case 20:{
          reporte = "EAD";
          mapParams.put("FECHA_MES_EJERCICIO_COMPARA", ejercicioCompara+mesCompara+"01");
          query.append(consultas.estadoActividadesDetallado( fecha, mes, mesCompara, ejercicioCompara));
          break;
        }
        // Estado de Analítco de la Deuda y Otros Pasivos    
        case 22:{
          reporte = "EDP";
          mapParams.put("FECHA_MES_EJERCICIO_COMPARA", ejercicioCompara+mesCompara+"01");
          query.append(consultas.estadoAnaliticoDelaDeudayOtrosPasivos(fecha, mes, mesCompara, ejercicioCompara));
          break;
          /*query.append("select * from dual");
          break;*/
        }
        // Estado de Cambios en la Situación Financiera
        case 23:{
          conexion =  DaoFactory.getContabilidad();
          reporte = "ECS";
          mapParams.put("FECHA_MES_EJERCICIO_COMPARA", ejercicioCompara+mesCompara+"01");
          mapParams.put("EJERCICIO_COMPARA",Integer.valueOf(ejercicioCompara));
          query.append(consultas.cambiosSituacionFinanciera( fecha, mes, mesCompara, ejercicioCompara));
          String strRutaArchivo = "/contabilidad/registroContableNuevo/estadosFinancieros/reportes/" + reporte;
          request.getSession().setAttribute("abrirReporte", "abrirEFC");
          request.getSession().setAttribute("nombreArchivo",reporte);
          request.getSession().setAttribute("rutaArchivo", strRutaArchivo);
          request.getSession().setAttribute("parametros", mapParams);
          request.getSession().setAttribute("modulo", "Contabilidad");
          request.getSession().setAttribute("conexion", DaoFactory.CONEXION_CONTABILIDAD);
          SituacionFinancieraComparativo sfc = new SituacionFinancieraComparativo();
          sfc.vaciarInformacion(conexion, consultas, fecha, mes, mesCompara, ejercicioCompara,3);
          request.getSession().setAttribute("edoFinancieroCom", sfc);
          request.getSession().setAttribute("accionPagina","Generacion de estado de situacion financiera comparativo");
          pagina="generaReporteDataSource.jsp";
          break;
        }
        //Estado Analitico de Ingresos
        case 24:{
          reporte = "EAI";
          break;
        }
        //Estado Analitico del ejercicio
        case 25:{
          reporte = "EAE";
          break;
        }
                
      } //Fin de Switch para los Reportes Financieros  
  
      String criterio = request.getParameter("criterio").trim();
      switch(criterio.charAt(0)){
            case '1'://Consolidado General
                if(reporte.equals( "BCO")){
                    mapParams.put("DESC_PROGRAMA","CONSOLIDADA GENERAL AL");
                }
                else mapParams.put("DESC_PROGRAMA","CONSOLIDADO GENERAL AL");
            break;
            case '3'://Consolidado por Programa
                if(reporte.equals( "BCO")){
                    mapParams.put("DESC_PROGRAMA","CONSOLIDADA POR PROGRAMA AL ");
                }
                else mapParams.put("DESC_PROGRAMA","CONSOLIDADO POR PROGRAMA AL ");
            break;
            case '4'://Consolidado por programa y unidad
                if(reporte.equals( "BCO")){
                    mapParams.put("DESC_PROGRAMA","CONSOLIDADA POR PROGRAMA Y UNIDAD AL ");
                }
                else mapParams.put("DESC_PROGRAMA","CONSOLIDADO POR PROGRAMA Y UNIDAD AL");
            break;
            case '5'://Consolidado por programa, unidad y estado
                if(reporte.equals( "BCO")){
                    mapParams.put("DESC_PROGRAMA","CONSOLIDADA POR PROGRAMA, UNIDAD Y ESTADO AL");
                }
                else mapParams.put("DESC_PROGRAMA","CONSOLIDADO POR PROGRAMA, UNIDAD Y ESTADO AL");
            break;
      }           
      //Inserta los parametros que recibira el reporte, como por ejemplo, la Unidad, Entidad, Ejercicio, quien REVISO, ELABORO y AUTORIZO el reoporte etc.
      mapParams.put("FECHA_CONSOLIDACION", fecha);
      mapParams.put("FECHA_TERMINO_MES", fechaTerminoMes);
      mapParams.put("FECHA_ACTUAL", sia.libs.formato.Fecha.formatear(sia.libs.formato.Fecha.FECHA_LARGA,controlReg.getFechaAfectacion()));
      mapParams.put("EJERCICIO",controlReg.getEjercicio());
      mapParams.put("UNIDAD_EJECUTORA",controlReg.getUnidad().equals("000")?"100":controlReg.getUnidad());
      mapParams.put("ENTIDAD",String.valueOf(controlReg.getEntidad()));
      mapParams.put("AMBITO",String.valueOf(controlReg.getAmbito()));
      registros = datosReportes.obtenerTitulos(String.valueOf(controlReg.getUnidad().equals("000")?"100":controlReg.getUnidad()),String.valueOf(controlReg.getEntidad()),String.valueOf(controlReg.getAmbito()));
      mapParams.put("TITULO1",registros.get(0).getField("TITULO1"));
      mapParams.put("TITULO2",registros.get(0).getField("TITULO2"));
      mapParams.put("TITULO3",registros.get(0).getField("TITULO3"));
      //mapParams.put("DESC_PROGRAMA",request.getParameter("txtTitulo").equals("")?" ":request.getParameter("txtTitulo"));
      mapParams.put("PROGRAMA",request.getParameter("txtTitulo").equals("x")?" ":request.getParameter("txtTitulo"));
      mapParams.put("DOCUMENTO",reporte.equals("EFCV")?"EFC":reporte);
      mapParams.put("MES",request.getParameter("lstMes"));
      //mapParams.put("MES",sia.libs.formato.Fecha.getNombreMes(Integer.parseInt(mes)-1).toUpperCase().concat(" DE"));
      //mapParams.put("MES_COMPARA",sia.libs.formato.Fecha.getNombreMes(Integer.parseInt(mesCompara)-1).toUpperCase().concat(" DE"));
      mapParams.put("ELABORO",request.getParameter("elaboro"));
      mapParams.put("REVISO",request.getParameter("reviso"));
      mapParams.put("TIPO_CIERRE",cierre.equals("2")?"Precierre":cierre.equals("3")?"Cuenta Pública":cierre.equals("1") && !controlReg.getUnidad().equals("100") && request.getParameter("lstMes").equals("12")?"Precierre":"");
      mapParams.put("AUTORIZO",request.getParameter("autorizo"));
      mapParams.put("ACTIVA_ETIQUETA",activaEtiqueta);
      mapParams.put("TEXTO_PRELIMINAR",textoPreliminar);
      mapParams.put("IMG_DIR",request.getSession().getServletContext().getRealPath("/Librerias/Imagenes/").toString());
      mapParams.put("SUBREPORT_DIR", request.getSession().getServletContext().getRealPath("/contabilidad/registroContableNuevo/estadosFinancieros/reportes"));
      formatoReporte = "pdf";

      /*repClase.imprimir(query, mapParams, reporte, 
        "/contabilidad/"+carpeta+"/reportes/"+reporte, 
        DaoFactory.CONEXION_CONTABILIDAD, formatoReporte, "Generacion de reporte de estados financieros", "Contabilidad");     */
    if(Integer.valueOf(request.getParameter("reporte"))!=30){
      request.getSession().setAttribute("abrirReporte", "abrir");
      request.getSession().setAttribute("nombreArchivo", reporte);
      //Manda llamar al reporte seleccionado pasandole la ruta fisica 
      request.getSession().setAttribute("rutaArchivo", "/contabilidad/registroContableNuevo/estadosFinancieros/reportes/"+reporte);
      //Establece todos los parametros del reporte
      request.getSession().setAttribute("parametros", mapParams);
      request.getSession().setAttribute("conexion", DaoFactory.CONEXION_CONTABILIDAD);
      request.getSession().setAttribute("query", query);
      //Se espcifica el formato del reporte que en este caso en un archivo PDF
      request.getSession().setAttribute("formatoSalida", formatoReporte);
      request.getSession().setAttribute("accionPagina", "Generacion de reporte");
      request.getSession().setAttribute("modulo", "Contabilidad");
    }
    genera = true;
    }//end try
    catch(Exception e){
      e.printStackTrace();
      System.err.println("Error al generar el reporte");
    }     
    finally{    
      mapParams = null;
      //repClase = null;
      registros = null;
      datosReportes = null;
      consultas = null;
      //query.delete(0, query.length());
      //query = null;
    //String carpeta = "estadosFinancieros";
      if(conexion!=null)
        conexion.close();
        conexion = null;
      //consultasReportes = null;
    }
  %>
  <form id="forma" name="forma" action="../../../Librerias/reportes/<%=pagina%>">
    <br><br><br><br><br>
<table border="0" width="100%" class='sianoborder' align="center">
<%if(genera){%>
<tr align="center"><td>Generando el reporte, espere un momento...</td></tr>
<%}else{%>
<tr align="center"><td>Hubo un error al momento de generar el reporte, notifíquelo al administrador....</td></tr>
<%}%>
</table>
<BR><hr noshade="noshade" width="40%" align="center" style="border-width: 1px;border-style:solid">
<input type="hidden" name="genera" id="genera" value="<%=genera%>">
   </form>
  </body>
</html>