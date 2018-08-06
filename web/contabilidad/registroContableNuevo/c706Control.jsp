<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page  import="java.util.*,java.sql.*"%>
<%@ page import="sia.db.sql.Sentencias"%>
<%@ page  import="sia.db.dao.*,java.util.Map, java.util.HashMap"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="Autentifica" class="sia.beans.seguridad.Autentifica" scope="session"/>  
<jsp:useBean id="pbCheque" class="sia.rf.contabilidad.registroContableNuevo.bcCheque" scope="page"/>  
<jsp:useBean id="pbFechaHoy" class="sun.jdbc.rowset.CachedRowSet" scope="page">
   <% pbFechaHoy.setCommand("select to_Char(sysdate,'dd/mm/yyyy hh24:mi:ss') fechaActual from dual");%>
</jsp:useBean> 
<jsp:useBean id="xScriptlet" class="sia.scriptlets.Reporte" scope="page"/>
<!-- (Inicia codigo JAVA) -->
<%
 Connection conexion=null;
 System.out.println("1");
 String lsUnidad= request.getParameter("txtUnidad");
 String lsAmbito= request.getParameter("txtAmbito");
 String lsEntidad= request.getParameter("txtEntidad");
 String lsEjercicio= request.getParameter("txtEjercicio");
 String lsFechaActual=request.getParameter("txtfechaActual");
 String lsOperacion = request.getParameter("txtOperacion"); //0 = Vista Preliminar y 1 = Imprimir
  String tipoFormato = request.getParameter("hTipoFormato");
 String pocicion = request.getParameter("hPocicion");
 String lsCuentaBancaria = request.getParameter("lsCuentaBancaria");
 String lsChequeId = "";
 String lsLstCheques = "";
 String lsMensaje = "";
 String strNombreAbrir = null; //Para el reporte
 Sentencias sentencia = null;
 String chequesFaltantes = null;
%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <link rel="stylesheet" href="../../Librerias/Estilos/siastyle.css" type='text/css'>
    <script language="JavaScript" src="../../Librerias/Javascript/Capturador/funciones.js" type="text/javascript"></script>
<script language="JavaScript" type='text/JavaScript'>
    function validaReportePreview(reporte, nombreArchivo){
      alert(nombreArchivo);
      if(reporte == 0)
        window.open("'"+nombreArchivo+"'",'_blank');
      else 
       window.open("'"+nombreArchivo+"'",'_blank');
    }
</script>
    <title>c706Resultado</title>
  </head>
  <body>
     
    <br><br>
      <b>Fecha Actual</b> [<%=lsFechaActual%>]
    <br><br>
    <br>
    <FORM name="control" method="post"  action="c706Filtro.jsp">
    <jsp:scriptlet>util.tituloPagina(out, "Contabilidad", " ", "Filtro de cheques", "Impresión", true);</jsp:scriptlet>   
 <br><br><br>
<!-- Inicia tabla para desplegar resultado del filtro utilizando el bean xPaginacion -->
<table width="100%" align="center" class="general" name="tAmbitos" id="tAmbitos">
<tr>
<td align="center">

<!-- (Inicia codigo JAVA) -->
<%  
    try
    {

        String _voperacionPago = "";
        sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD,Sentencias.XML);
        conexion=DaoFactory.getContabilidad();
        String[] lstCheques= request.getParameterValues("AMBITOS");
        
        if (lstCheques == null) 
        {
            return; //se sale.
        }
        //out.println("Generando Repporte....");
        conexion.setAutoCommit(false);
        //lstCheques.
        for(int i=0; i< lstCheques.length; i++ )
        {
           lsChequeId = lstCheques[i].substring(0,lstCheques[i].length()-1);             
           pbCheque.select_rf_tr_cheques(conexion, lsChequeId);
           _voperacionPago =  pbCheque.getOperacion_Pago();
           

           if (pbCheque.getEstatus().equals("1"))
           { //1 = Impreso
                lsMensaje = lsMensaje + "<br>El cheque " + pbCheque.getConsecutivo() + " fue impreso el " + pbCheque.getFecha_impresion(); 
           }
           else 
           {
               lsLstCheques = lsLstCheques + lsChequeId + ",";
                  //out.println(lsChequeId);

                  if (lsOperacion.equals("1")) 
                  {//imprimir
                         pbCheque.setEstatus("1");
                         pbFechaHoy.execute(conexion);
                         pbFechaHoy.first();
                         pbCheque.setFecha_impresion(pbFechaHoy.getString("fechaActual"));
                         pbCheque.update_rf_tr_cheques(conexion,lsChequeId);
                  } //fin de imprimir

           } //if    
        }

                lsLstCheques = lsLstCheques.substring(0,lsLstCheques.length()-1);
                out.println(lsMensaje);
                conexion.commit();
                /*********** Construir el Reporte: ******/
                Map parameters = new HashMap();
                String ruta=null;
                String rutaJasper=null;
                String formatoSalida = "pdf";
                String reporte = null;
                String rutaSinArchivo = null;
                String rutaSinArchivoSinReportes = null;
                String nom_archivo = null;
                String realPath = application.getRealPath("")+"/";
                short recorrido = 2;
                
                parameters.put("RUTA_IMAGEN", application.getRealPath("").replace("\\","/").concat("/contabilidad/registroContableNuevo/reportes/"));                 
                ///parameters.put("RUTA_IMAGEN", "../../contabilidad/registroContableNuevo/reportes/");
                
                parameters.put("REPORT_LOCALE",new java.util.Locale("es","MX"));
                parameters.put("SIA_OUT_REPORT", out);
                parameters.put("SIA_FORM_REPORT", "forma");
                parameters.put("DEBUG", new Boolean(true));
                parameters.put("LSTCHEQUES_ID", lsLstCheques);               
                parameters.put("EJERCICIO",lsEjercicio);
                
                ///parameters.put("SUBREPORT_DIR","../../contabilidad/registroContableNuevo/reportes");                
                parameters.put("SUBREPORT_DIR",application.getRealPath("").replace("\\","/").concat("/contabilidad/registroContableNuevo/reportes/"));
                
                //String strNombreAbrir = null;

                if (lsOperacion.equals("1")) 
                {//imprimir
                  recorrido = 1;
                  if(tipoFormato.equals("central"))
                  {
                        if (lsCuentaBancaria.equals("2"))
                        {
                        rutaJasper=("/contabilidad/registroContableNuevo/reportes/cheque.jasper");
                        }
                        else if (lsCuentaBancaria.equals("1"))
                        {
                        rutaJasper=("/contabilidad/registroContableNuevo/reportes/chequebanorte.jasper");
                        }
                        else 
                        { //Queda el idcuenta 6, banorte de contabilidad
                                rutaJasper=("/contabilidad/registroContableNuevo/reportes/chequebanorte105.jasper");
                        }
                  }
                  else
                  {
                        if(pocicion.equals("2"))
                        {
                          chequesFaltantes = "select -1 consecutivo, 'x' fecha, 'x' beneficiario, 0 importe, 'x' letra from dual".
                          concat(" union");
                        } 
                        else 
                        {
                          if (pocicion.equals("3"))
                          {
                                chequesFaltantes = "\nselect -1 consecutivo, 'x' fecha, 'x' beneficiario, 0 importe, 'x' letra from dual".
                                concat(" union").
                                concat("\nselect -1 consecutivo, 'xx' fecha, 'xx' beneficiario, 0 importe, 'xx' letra from dual").
                                concat(" union");
                          }
                        }

                        parameters.put("chequesFaltantes", chequesFaltantes == null?"":chequesFaltantes);
                        ///parameters.put("chequesId", lsLstCheques);
                        parameters.put("chequesId", _voperacionPago);
                        //System.out.println("Sentencia Cheques: "+sentencia.getComando("reportes.select.chequesUnidades",parameters));
                        parameters.put("SIA_QUERY_REPORT", sentencia.getComando("reportes.select.chequesUnidades",parameters));
                        parameters.put("POCICION",pocicion);
                        rutaJasper=("/contabilidad/registroContableNuevo/reportes/chequeUnidades.jasper");
                  }
                }

        if(tipoFormato.equals("unidad"))
          recorrido = 1;

        //parameters.put("PID_META", idMeta);                            
        //parameters.put("PID_EJERCICIO", ejercicio);              
        //parameters.put("SIA_QUERY_REPORT", strQuery); 

        //Este for a para verificar si va a imprimir el reporte normal o el preview y si es el preview cual de los dos si con o sin datos del cheque
        for(int i=0; i<recorrido; i++)
        {
           if(!lsOperacion.equals("1"))
           {
            if(tipoFormato.equals("central"))
            {
                if(i==0)
                {
                        reporte = "/contabilidad/registroContableNuevo/reportes/chequePreview.jasper";
                }
                else
                {
                        reporte = "/contabilidad/registroContableNuevo/reportes/chequePreviewSinDatos.jasper";
                }
                rutaJasper = reporte;
            }
            else
            {

                  if(pocicion.equals("2"))
                  {
                        chequesFaltantes = "select -1 consecutivo, 'x' fecha, 'x' beneficiario, 0 importe, 'x' letra from dual".
                        concat(" union");
                  } 
                  else 
                  {
                    if (pocicion.equals("3"))
                    {
                      chequesFaltantes = "\nselect -1 consecutivo, 'x' fecha, 'x' beneficiario, 0 importe, 'x' letra from dual".
                      concat(" union").
                      concat("\nselect -1 consecutivo, 'xx' fecha, 'xx' beneficiario, 0 importe, 'xx' letra from dual").
                      concat(" union");
                    }
                  }

                  parameters.put("chequesFaltantes", chequesFaltantes == null?"":chequesFaltantes);
                  parameters.put("chequesId", lsLstCheques);
                  ///parameters.put("chequesId", _voperacionPago);
                  //System.out.println("Sentencia Cheques: "+sentencia.getComando("reportes.select.chequesUnidades",parameters));
                  parameters.put("SIA_QUERY_REPORT", sentencia.getComando("reportes.select.chequesUnidades",parameters));
                  parameters.put("POCICION",pocicion);
                  rutaJasper=("/contabilidad/registroContableNuevo/reportes/chequeUnidades.jasper");
                  rutaJasper=("/contabilidad/registroContableNuevo/reportes/chequeUnidades.jasper");

                }
            }

                  String nombreArchivo = rutaJasper.substring(rutaJasper.lastIndexOf("/")+1,rutaJasper.length()); 
                  nombreArchivo = nombreArchivo.substring(0,nombreArchivo.lastIndexOf("."));//rutaArchivo.split("\\/")[rutaArchivo.split("\\/").length-1].split("\\.")[0];
                  rutaSinArchivo = rutaJasper.substring(0,rutaJasper.lastIndexOf("/"));
                  rutaSinArchivoSinReportes = rutaSinArchivo.substring(0,rutaSinArchivo.lastIndexOf("/"));
                  nom_archivo= "xSia"+ util.formatearFecha(new java.util.Date(), "yyyyMMddhhmmss")+ "_"+nombreArchivo;
                  strNombreAbrir = nom_archivo+"."+formatoSalida;
                  util.eliminarTemporalesDias(application.getRealPath("").concat("/Reportes").concat(rutaSinArchivoSinReportes), 1);
                  xScriptlet.setSource(application.getRealPath("").concat(rutaJasper.split("\\.")[0]));
                  xScriptlet.setTarget(realPath.concat("Reportes").concat(rutaSinArchivoSinReportes).concat("/").concat(nom_archivo));
                  xScriptlet.setConnection(conexion);  
                  xScriptlet.setParameter(parameters);

                        //xScriptlet.setSource(application.getRealPath("").concat(rutaArchivo.split("\\.")[0]));
                //xScriptlet.setTarget(application.getRealPath("").concat("/Reportes").concat(rutaSinArchivoSinReportes).concat("/").concat(nom_archivo));

                if (xScriptlet.procesar(formatoSalida, true)) 
                {     
                        /*File destino = new File(rutaDescarga);
                        if (!(destino.exists())) {
                                destino.mkdirs();
                        }*/
                        strNombreAbrir = "../../Reportes"+rutaSinArchivoSinReportes+"/"+nom_archivo+"."+formatoSalida;
                }// if XScriptlet 

                //System.out.println("Archivo Creado:" + strNombreAbrir);
                if(!lsOperacion.equals("1"))
                {
                        if(tipoFormato.equals("central"))
                        {
                          if(i==0)
                          {
                                out.println("<INPUT type='button' name='txtMostrar' value='Mostrar Reporte Con Datos del Cheque' class='boton' onclick= window.open('"+strNombreAbrir+"','_blank')>");
                          }
                          else
                          {
                                out.println("<INPUT type='button' name='txtMostrarSinDatos' value='Mostrar Reporte Sin Datos del Cheque' class='boton' onclick=window.open('"+strNombreAbrir+"','_blank')>");
                          }
                        } 
                        else 
                        {
                           out.println("<INPUT type='button' name='txtMostrar' value='Mostrar Reporte' class='boton' onclick=window.open('"+strNombreAbrir+"','_blank')>");
                        }
                }
                else
                {
                        out.println("<INPUT type='button' name='txtMostrar' value='Abrir PDF' class='boton' onclick=window.open('"+strNombreAbrir+"','_blank')>");
                }
        }
    //System.out.println("Archivo Creado:" + strNombreAbrir);
    //out.println("<INPUT type='button' name='txtMostrar' value='Mostrar Reporte' class='boton' onclick=validaReportePreview(0,)  window.open('"+strNombreAbrir+"','_blank')>");
}
catch(Exception E)
   {
   conexion.rollback();
   System.out.println("Error en "+E.getMessage()); 
   
%>
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
</td>
</tr>
</table>
<!-- Termina tabla para desplegar resultado del filtro utilizando el bean xPaginacion -->
<table width='100%'>
 <tr><td width='73%'>&nbsp;</td>
     <td width='10%'>
       <INPUT type="hidden" name="txtfechaActual" value=<%=lsFechaActual%>>
       <input type='submit' name='btnAceptar' value='Aceptar' class='boton'>
    </td>
     <td width='80%'>
     <input type='submit' name='btnRegresar' value='Regresar' class='boton' onClick="javascript:history.back();" >
     </td></tr>
 </table>
 </FORM>
  </body>
</html>