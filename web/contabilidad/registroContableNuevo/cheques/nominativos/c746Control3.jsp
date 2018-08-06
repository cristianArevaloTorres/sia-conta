<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page  import="sia.db.dao.*,java.util.Map, java.util.HashMap"%>
<%@ page import="sia.db.sql.Sentencias,sia.db.dao.DaoFactory,sia.db.sql.Vista"%>
<%@ page  import="java.util.*,java.sql.*"%>
<%@ page  import="sia.rf.contabilidad.registroContableNuevo.chequesNominativos.ProcesarRegistroContable"%>
<%@ page import="sia.rf.contabilidad.registroContable.acciones.ControlRegistro"%>
<%@ page import="sia.beans.seguridad.Autentifica"%>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="pbCheque" class="sia.rf.contabilidad.registroContableNuevo.bcCheque" scope="page"/>  
<jsp:useBean id="xScriptlet" class="sia.scriptlets.Reporte" scope="page"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <link rel="stylesheet" href="../../../../Librerias/Estilos/siastyle.css" type='text/css'>
    <title>c746Control</title>
    <script language="JavaScript" src="../../../../Librerias/Javascript/Capturador/funciones.js" type="text/javascript"></script>
    <script language="JavaScript" type="text/javascript">
      function abrirReporte(){
        archivo = getElementById('nombreArchivo').value;
        window.open("'"+archivo+"'",'_blank');
      }
    </script>
  </head>
  <body>
  <jsp:scriptlet>util.tituloPagina(out, "Contabilidad", " ", "Cheques nominativos", "Generacion", true);</jsp:scriptlet>  
  <form action="c746Filtro.jsp">
<%   
 //String listaSeleccionada[] = request.getParameterValues("cheque");
  try{
   //System.out.println("** PROBANDO PROBANDO PROBANDO CEHQUES NOMINATIVOS ......  **** 1.... ");
   }catch(Exception e){
   //System.out.println("** ENTRO AL CATCH ......  **** CATCH.... ");
     e.printStackTrace();
   }
 String idOperacion = request.getParameter("idOperacion");
 String idCheque = request.getParameter("idCheque");
 Autentifica sbAutentifica = (Autentifica)request.getSession().getAttribute("Autentifica");
 String ordernarQuery = "order by ";
 String orden= request.getParameter("ordenamiento");
 String tipoFormato = request.getParameter("tipoFormato");
 String pocicion = request.getParameter("pocicion");
 String condicionINE = request.getParameter("hcondicionINE");
String condicionPR = request.getParameter("hcondicionPR");
String condicionSP = request.getParameter("hcondicionSP");
String condicionOFR = request.getParameter("hcondicionOFR");
 //List<Vista> registros = request.getParameter("registrosCheques");
 
 /*********** Construir el Reporte: ******/
    String ruta=null;
    String rutaJasper=null;
    String formatoSalida = "pdf";
    String reporte = null;
    String rutaSinArchivo = null;
    String rutaSinArchivoSinReportes = null;
    String nom_archivo = null;
    Connection conexion = null;
    Sentencias sentencia = null;
    List<Vista> registros = null;
    Map parametros = null;
    StringBuffer resultado = null;
    String chequesFaltantes = null;
    //String  = null;
    String campos = null;
    String camposGrupo = null;
    //sbAutentifica = (Autentifica)request.getSession().getAttribute("Autentifica");
    String strNombreAbrir = null; //Para el reporte
    String strNombreAbrirSinFormato = null; //Para el reporte
    String idCheques = null;
    ProcesarRegistroContable regContable = null;
    String realPath = application.getRealPath("")+"/";
 try{
   //System.out.println("** PROBANDO PROBANDO PROBANDO CEHQUES NOMINATIVOS ......  **** 2... ");
   switch(Integer.parseInt(request.getParameter("ordenamiento"))){
      case 1: ordernarQuery = ordernarQuery.concat("beneficiario");break;
      case 2: ordernarQuery = ordernarQuery.concat("fecha_pago");break;
      case 3: ordernarQuery = ordernarQuery.concat("operacion_pago");break;
      case 4: ordernarQuery = ordernarQuery.concat("origen_operacion");break;
      //default: ordernarQuery = ordernarQuery.concat("beneficiario");break;
    }
    resultado = new StringBuffer();
    ControlRegistro controlReg = (ControlRegistro)request.getSession().getAttribute("controlRegistro"); 
    conexion = DaoFactory.getContabilidad();
    conexion.setAutoCommit(false);
    sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD,Sentencias.XML);
    parametros = new HashMap();
    registros = new ArrayList<Vista>();
    //condicion = " and oc.id_operacion in ("+idOperacion+") and cn.id_cheque_nominativo in ("+idCheque+")";
    campos = "beneficiario, fecha_pago, cuenta_bancaria, id_cuenta, tipo_registro, ejercicio,"+
             "rtrim(xmlagg(xmlelement(e, id_cheque_nominativo || ',')).extract('//text()'),',') id_cheque_nominativo, "+
             "rtrim(xmlagg(xmlelement(e, id_operacion || ',')).extract('//text()'),',') id_operacion ";


    camposGrupo = "beneficiario, fecha_pago, cuenta_bancaria, id_cuenta, tipo_registro, ejercicio";


    //parametros.put("condicion",condicion);
    parametros.put("condicionINE",condicionINE);
    parametros.put("condicionPR",condicionPR);
    parametros.put("condicionSP",condicionSP);
    parametros.put("condicionOFR",condicionOFR);
    parametros.put("campos",campos);
    parametros.put("camposGrupo",camposGrupo);
    parametros.put("ordenar",ordernarQuery);   
    //System.out.println(sentencia.getComando("chequesNominativos.select.verificaCheques",parametros));
    //cheques = unificaCheque.obtenerChequesUnificados(idOperacion);
    registros = sentencia.registros("chequesNominativos.select.verificaCheques",parametros);
    // Generar el registro contable de cada cheque
    regContable = new ProcesarRegistroContable(controlReg.getUnidad(), String.valueOf(controlReg.getEntidad()),String.valueOf(controlReg.getAmbito()), controlReg.getEjercicio(), sbAutentifica.getNumeroEmpleado());
    resultado.append(regContable.procesar(conexion,registros));
    parametros.clear();
    conexion.commit();
    // Generacion del reporte
    //reporte = "/contabilidad/registroContableNuevo/reportes/chequePreview.jasper";
    //rutaJasper=("/contabilidad/registroContableNuevo/reportes/cheque.jasper");
    rutaJasper=("/contabilidad/registroContableNuevo/reportes/chequePreview.jasper");
    parametros.put("SUBREPORT_DIR",application.getRealPath("").concat("/contabilidad/registroContableNuevo/reportes/"));
    //parameters.put("PID_META", idMeta);                            
    //parameters.put("PID_EJERCICIO", ejercicio);              
    //parameters.put("SIA_QUERY_REPORT", strQuery); 
    parametros.put("RUTA_IMAGEN", application.getRealPath("").concat("/contabilidad/registroContableNuevo/reportes/")); 
    parametros.put("REPORT_LOCALE",new java.util.Locale("es","MX"));
    parametros.put("SIA_OUT_REPORT", out);
    parametros.put("SIA_FORM_REPORT", "forma");
    parametros.put("DEBUG", new Boolean(false));
    //System.out.println("Id cheque: " + regContable.getIdCheques().substring(0,regContable.getIdCheques().length()-1)); 
    parametros.put("LSTCHEQUES_ID", regContable.getIdCheques().substring(0,regContable.getIdCheques().length()-1));// recuperar los id de cheques que se van a imprimir
    parametros.put("EJERCICIO",String.valueOf(controlReg.getEjercicio()));
    //System.out.println("tipoFormato: " +tipoFormato);
    if(tipoFormato.equals("central")){
     //Para generar tanto el reporte con formato y sin formato
     for(int i=0; i<2; i++){
       if(i==0)
         rutaJasper=("/contabilidad/registroContableNuevo/reportes/cheque.jasper");
      else
         rutaJasper=("/contabilidad/registroContableNuevo/reportes/chequePreview.jasper");
        String nombreArchivo = rutaJasper.substring(rutaJasper.lastIndexOf("/")+1,rutaJasper.length()); 
        
        nombreArchivo = nombreArchivo.substring(0,nombreArchivo.lastIndexOf("."));//rutaArchivo.split("\\/")[rutaArchivo.split("\\/").length-1].split("\\.")[0];
        rutaSinArchivo = rutaJasper.substring(0,rutaJasper.lastIndexOf("/"));
        rutaSinArchivoSinReportes = rutaSinArchivo.substring(0,rutaSinArchivo.lastIndexOf("/"));
        
        
        if(i==0){
          nom_archivo= "xSia"+ util.formatearFecha(new java.util.Date(), "yyyyMMddhhmmss")+ "_"+nombreArchivo + "_NominativoSinFormato";
          strNombreAbrir = nom_archivo+"."+formatoSalida;
        }else{
          nom_archivo= "xSia"+ util.formatearFecha(new java.util.Date(), "yyyyMMddhhmmss")+ "_"+nombreArchivo + "_NominativoConFormato";
          strNombreAbrirSinFormato = nom_archivo+"."+formatoSalida;
        }
        util.eliminarTemporalesDias(application.getRealPath("").concat("/Reportes").concat(rutaSinArchivoSinReportes), 1);
        xScriptlet.setSource(application.getRealPath("").concat(rutaJasper.split("\\.")[0]));
        xScriptlet.setTarget(realPath.concat("Reportes").concat(rutaSinArchivoSinReportes).concat("/").concat(nom_archivo));
        //System.out.println("c746Control.jsp ** "+realPath.concat("Reportes").concat(rutaSinArchivoSinReportes).concat("/").concat(nom_archivo));
        //System.out.println("** PROBANDO PROBANDO PROBANDO CEHQUES NOMINATIVOS ......  **** ");
        //out.println("<p>"+realPath.concat("Reportes").concat(rutaSinArchivoSinReportes).concat("/").concat(nom_archivo)+"</p>");
        xScriptlet.setConnection(conexion);  
        xScriptlet.setParameter(parametros);
    
        if (xScriptlet.procesar(formatoSalida, false)) {        
          if(i==0)
            strNombreAbrir = "../../../../Reportes"+rutaSinArchivoSinReportes+"/"+nom_archivo+"."+formatoSalida;
          else
            strNombreAbrirSinFormato = "../../../../Reportes"+rutaSinArchivoSinReportes+"/"+nom_archivo+"."+formatoSalida;
        }// if XScriptlet  
      }
      }else{
      if(pocicion.equals("2")){
        chequesFaltantes = "select -1 consecutivo, 'x' fecha, 'x' beneficiario, 0 importe, 'x' letra from dual".
        concat(" union");
      }
      if(pocicion.equals("3")){
        chequesFaltantes = "\nselect -1 consecutivo, 'x' fecha, 'x' beneficiario, 0 importe, 'x' letra from dual".
        concat(" union").
        concat("\nselect -1 consecutivo, 'xx' fecha, 'xx' beneficiario, 0 importe, 'xx' letra from dual").
        concat(" union");
      }
        parametros.put("chequesFaltantes", chequesFaltantes == null?"":chequesFaltantes);
        parametros.put("chequesId", regContable.getIdCheques().substring(0,regContable.getIdCheques().length()-1));
       // System.out.println(sentencia.getComando("reportes.select.chequesUnidades",parametros));
        parametros.put("SIA_QUERY_REPORT", sentencia.getComando("reportes.select.chequesUnidades",parametros));
       // System.out.println("pocicion: "+pocicion);
        parametros.put("POCICION",pocicion);
        rutaJasper=("/contabilidad/registroContableNuevo/reportes/chequeUnidades.jasper");
        String nombreArchivo = rutaJasper.substring(rutaJasper.lastIndexOf("/")+1,rutaJasper.length()); 
        nombreArchivo = nombreArchivo.substring(0,nombreArchivo.lastIndexOf("."));//rutaArchivo.split("\\/")[rutaArchivo.split("\\/").length-1].split("\\.")[0];
        rutaSinArchivo = rutaJasper.substring(0,rutaJasper.lastIndexOf("/"));
        rutaSinArchivoSinReportes = rutaSinArchivo.substring(0,rutaSinArchivo.lastIndexOf("/"));
        nom_archivo= "xSia"+ util.formatearFecha(new java.util.Date(), "yyyyMMddhhmmss")+ "_"+nombreArchivo + "_NominativoSinFormato";
        strNombreAbrir = nom_archivo+"."+formatoSalida;
        util.eliminarTemporalesDias(application.getRealPath("").concat("/Reportes").concat(rutaSinArchivoSinReportes), 1);
       // System.out.println("Split: "+application.getRealPath("").concat(rutaJasper.split("\\.")[0]));
        xScriptlet.setSource(application.getRealPath("").concat(rutaJasper.split("\\.")[0]));
        xScriptlet.setTarget(realPath.concat("Reportes").concat(rutaSinArchivoSinReportes).concat("/").concat(nom_archivo));
        xScriptlet.setConnection(conexion);  
        xScriptlet.setParameter(parametros);
        if (xScriptlet.procesar(formatoSalida, false)) 
          strNombreAbrir = "../../../../Reportes"+rutaSinArchivoSinReportes+"/"+nom_archivo+"."+formatoSalida;
      }
%>

<table width="60%" class="resultado" border="1" align="center">
<tr>
<th class="general" width="30%">Operación pago</th>
<th class="general" align="center">Poliza</th>
<th class="general" align="center">Fecha Alta</th>
<th class="general" align="center">Fecha Poliza</th>
<th class="general" align="center">Consecutivo cheque</th>
</tr>
  <%=resultado%>
</table>
</br>
</br>
</br>
<table width="50%" align="center">
<thead>
<tr>
<td><hr noshade="noshade" width="80%" align="center" style="border-width: 1px;border-style:solid"></td>
</tr>
</thead>
</table>
</br>
<input type="hidden" name="nombreArchivo" id="nombreArchivo" value="<%=strNombreAbrir%>">
<table align="center">
    <thead></thead>
    <tbody>
 <tr>
 <%
if(tipoFormato.equals("central")){
 // System.out.println("Reporte Con Formato: "+strNombreAbrir);
  //System.out.println("Reporte Sin Formato: "+strNombreAbrirSinFormato);
  out.println("<td><INPUT type='button' name='reporte' value='Reporte Sin formato' class='boton' onclick=window.open('"+strNombreAbrir+"','_blank')></td>");
  out.println("<td><INPUT type='button' name='reporteSinFormato' value='Reporte Con Formato' class='boton' onclick=window.open('"+strNombreAbrirSinFormato+"','_blank')></td>");
}else{
  System.out.println("Reporte Con Formato: "+strNombreAbrir);
  out.println("<td><INPUT type='button' name='reporte' value='Reporte' class='boton' onclick=window.open('"+strNombreAbrir+"','_blank')></td>");
}
 %>
     
     <td><input type='submit' name='btnRegresar' value='Regresar' class='boton'></td>
  </tr>
  </tbody>
 </table>
<%
  }
  catch(Exception E) {
   conexion.rollback();
   System.out.println("Error en "+E.getMessage()); 
%>
     <p><b><%=E.getMessage()%></b></p>
    
     <p>Ha ocurrido un error al accesar la Base de Datos,</p>
     <p>favor de reportarlo al Administrador del Sistema.</p>
     <p>Gracias.</p>
<%

   }
   finally{ 
     if (conexion!=null){
         conexion.close();
         conexion=null;
     }

   }
%>
<!-- (Termina codigo JAVA) -->
</form>
  </body>
</html>