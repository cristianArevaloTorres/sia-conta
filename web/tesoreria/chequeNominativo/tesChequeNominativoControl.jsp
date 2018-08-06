<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="java.util.*"%>
<%@ page import="sia.libs.formato.Fecha"%>
<%@ page import="sia.db.dao.DaoFactory"%>
<%@ page import="sia.rf.tesoreria.libs.csv.*"%>
<% response.setHeader("Pragma","no-cache");%>
<% response.setHeader("Cache-Control","no-store");%>
<% response.setDateHeader("Expires",-1);%> 
<jsp:useBean id="crsQueryReporte" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="crsQueryRepResumen" class="sia.db.sql.SentenciasCRS" scope="page"/>
<%
 Map parametros = new HashMap();
 ParametrosCsv paramcsv = new ParametrosCsv();
 String cuentas[] = null;
 String idEstatus[] = null;
 String filtroEstatus = "";
 String idCuentasSelecciondas = ""; 
 String query = null;
 String queryResumen = null;
 String tipoReporte = "";
 String numReporte = "";
 String[] encabezados = null;
 
 tipoReporte = request.getParameter("tipoReporte");
 numReporte = request.getParameter("numReporte");
 idEstatus = request.getParameterValues("estatus");
 
 cuentas = request.getParameterValues("idCuenta");
 for (int i = 0; i < cuentas.length; i++){
    idCuentasSelecciondas = idCuentasSelecciondas + cuentas[i].concat(",");
 }
 idCuentasSelecciondas = idCuentasSelecciondas.substring(0,idCuentasSelecciondas.length()-1);
 
 if (request.getParameterValues("estatus")!= null && numReporte.equals("1")) {
 idEstatus = request.getParameterValues("estatus");
 for (int i = 0; i < idEstatus.length; i++){
    filtroEstatus = filtroEstatus + idEstatus[i].concat(",");
 }
 filtroEstatus = filtroEstatus.substring(0,filtroEstatus.length()-1);
 crsQueryReporte.addParamVal("estatus","and  decode(m.id_movimiento, null, decode(q.estatus, 3, 3, 5, 4, 2), 1) in (:param)",filtroEstatus);
 }
 
 request.getSession().setAttribute("formatoSalida",tipoReporte.toLowerCase());
 request.getSession().setAttribute("conexion",DaoFactory.CONEXION_TESORERIA);

 parametros.put("IMG_DIR",request.getSession().getServletContext().getRealPath("/Librerias/Imagenes/"));
 parametros.put("SUBREPORT_DIR","/tesoreria/chequeNominativo/Reportes/");
 parametros.put("CUENTAS",idCuentasSelecciondas);
 sia.libs.periodo.Fecha fechaInicioFormato = new sia.libs.periodo.Fecha(request.getParameter("fechaInicio"), "/");
 sia.libs.periodo.Fecha fechaFinalFormato = new sia.libs.periodo.Fecha(request.getParameter("fechaFinal"), "/");
 parametros.put("FECHA_PERIODO","del ".concat(sia.libs.formato.Fecha.formatear(sia.libs.formato.Fecha.FECHA_LARGA,fechaInicioFormato.toString()).concat(" al ").concat(sia.libs.formato.Fecha.formatear(sia.libs.formato.Fecha.FECHA_LARGA,fechaFinalFormato.toString()))));
 parametros.put("FECHAORIGEN",sia.libs.formato.Fecha.getFechaBD(sia.libs.formato.Fecha.getRegistroDateSql()));
 parametros.put("FECHA_INICIAL",request.getParameter("fechaInicio"));
 parametros.put("FECHA_FINAL",request.getParameter("fechaFinal"));
 parametros.put("RESUMEN",request.getParameter("resumen"));
 
 
 
 if (numReporte.equals("1")){
   request.getSession().setAttribute("rutaArchivo","/tesoreria/chequeNominativo/Reportes/chequeNominativoDetalle");
   request.getSession().setAttribute("nombreArchivo","chequeNominativoDetalle");
 }
 else{  //// generar archivo para reporte de chq sin emision
   request.getSession().setAttribute("rutaArchivo","/tesoreria/chequeNominativo/Reportes/chequeSinReferenciaCont");
   request.getSession().setAttribute("nombreArchivo","cobroChqSinEmision");
 }
 
 if ( request.getParameter("beneficiario")!= null && !request.getParameter("beneficiario").equals("") && numReporte.equals("1") ){
      crsQueryReporte.addParamVal("beneficiario","and '%'||replace(replace(replace(replace(replace(replace(upper(q.beneficiario),' ','%'),'Á','A'),'É','E'),'Í','I'),'Ó','O'),'Ú','U')||'%' like '%'||replace(replace(replace(replace(replace(replace(upper(':param'),' ','%'),'Á','A'),'É','E'),'Í','I'),'Ó','O'),'Ú','U')||'%'",request.getParameter("beneficiario"));
 }
 
 if(!request.getParameter("inicioRango").equals("") && !request.getParameter("finRango").equals("")){
   if(numReporte.equals("1"))
     crsQueryReporte.addParamVal("rangoChq","and q.consecutivo >= :param0 and q.consecutivo <= :param1",request.getParameter("inicioRango"),request.getParameter("finRango"));
   else 
     crsQueryReporte.addParamVal("rangoChq","and (TO_NUMBER(REPLACE(m.DESCRIPCION, 'COBRO DE CHEQUE NUMERO ', '')) >= :param0 and TO_NUMBER(REPLACE(m.DESCRIPCION, 'COBRO DE CHEQUE NUMERO ', ''))<=:param1 )",request.getParameter("inicioRango"),request.getParameter("finRango"));
 }
 
 crsQueryReporte.addParam("idCuenta",idCuentasSelecciondas);
 crsQueryReporte.addParam("fechaInicial",request.getParameter("fechaInicio"));
 crsQueryReporte.addParam("fechaFinal",request.getParameter("fechaFinal"));

 if (tipoReporte.equals("PDF")){
   if(numReporte.equals("1"))
     query = crsQueryReporte.getComando(DaoFactory.CONEXION_TESORERIA,"reportes.select.ChqNominativoDetalle.chqNominativo",crsQueryReporte.getParametros());
   else
     query = crsQueryReporte.getComando(DaoFactory.CONEXION_TESORERIA,"reportes.select.ChqSinReferenciaCont.chqNominativo",crsQueryReporte.getParametros());
 }
 else{
   if(numReporte.equals("1"))
     query = crsQueryReporte.getComando(DaoFactory.CONEXION_TESORERIA,"reportes.select.ChqNominativoDetallexls.chqNominativo",crsQueryReporte.getParametros());
   else
     query = crsQueryReporte.getComando(DaoFactory.CONEXION_TESORERIA,"reportes.select.ChqSinReferenciaCont.chqNominativo",crsQueryReporte.getParametros());
 }
 
 request.getSession().setAttribute("query",new StringBuffer(query));
 request.getSession().setAttribute("parametros",parametros);
 
 
 // hacer la diferencia para cada uno de los reportes cuando su formato es de excel 
 if (tipoReporte.equals("XLS")){
    crsQueryRepResumen.addParam("idCuenta",idCuentasSelecciondas);   
    paramcsv.setTotalRegistros(0);
    paramcsv.setConn(DaoFactory.CONEXION_TESORERIA);
    if(numReporte.equals("1")){
        encabezados = new String[] {"NUM_CHEQUE", "FECHA_EMISION", "ESTATUS", "FECHA_ESTATUS", "ANTIGUEDAD", "IMPORTE_CHEQUE", "BENEFICIARIO", "CONCEPTO", "ORIGEN", "OPERACION_PAGO", "DIFERENCIA"};        
        if (request.getParameter("resumen").equals("1")){
            queryResumen = crsQueryRepResumen.getComando(DaoFactory.CONEXION_TESORERIA,"reportes.select.resumenCheques.chqNominativo",crsQueryRepResumen.getParametros());
            paramcsv.setQuery(query.concat("~").concat(queryResumen));
        }
        else
            paramcsv.setQuery(query.concat("~").concat("2"));       
        paramcsv.setTituloReporte("Reporte Cheque Nominativo (Detalle)");
        paramcsv.setNombreReporte("chequeNominativoDetalle"); 
        paramcsv.setTipoReporte(97);
    }
    else{
        encabezados = new String[] {"FECHA_HORA", "NUM_CHEQUE", "DESCRIPCION", "CARGO", "ABONO", "REFERENCIA"};        
        paramcsv.setQuery(query);
        paramcsv.setTituloReporte("Cobro de cheques sin referencia de emisión");
        paramcsv.setNombreReporte("cobroChqSinEmision"); 
        paramcsv.setTipoReporte(96);
 
    }
    paramcsv.setEncabezados(encabezados); 
      //fecha origen del perido -> programa
    paramcsv.setPrograma("Fecha solicitud del reporte ".concat(sia.libs.formato.Fecha.getFechaBD(sia.libs.formato.Fecha.getRegistroDateSql())));
    paramcsv.setPeriodo("del ".concat(sia.libs.formato.Fecha.formatear(sia.libs.formato.Fecha.FECHA_LARGA,fechaInicioFormato.toString()).concat(" al ").concat(sia.libs.formato.Fecha.formatear(sia.libs.formato.Fecha.FECHA_LARGA,fechaFinalFormato.toString()))));
    request.getSession().setAttribute("ruta","/tesoreria/chequeNominativo/Reportes");
    request.getSession().setAttribute("parametros",paramcsv);
  }

%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesChequeNominativoControl</title>
      <script language="javascript" type="text/javascript">
        
        function ir(){
            if (document.getElementById('idReporte').value=='PDF'){
                document.getElementById('form1').action='../../Librerias/reportes/generaReporte.jsp';
                document.getElementById('form1').submit();
            }
            else{
                document.getElementById('form1').action='../../tesoreria/generaCsvGral.jsp';
                document.getElementById('form1').submit();
            }
        }
    </script>
  </head>
  <body onload="ir()">
  <form id="form1" name="form1" >
   <input type="hidden" value="<%=request.getParameter("tipoReporte")%>" id="idReporte" name="idReporte">
  </form>
  </body>
</html>