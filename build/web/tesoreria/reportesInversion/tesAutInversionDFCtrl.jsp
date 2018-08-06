<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.libs.formato.Fecha" %>
<%@ page import="sia.db.dao.DaoFactory"%>
<%@ page import="sia.db.sql.*"%>
<%@ page import="sia.db.sql.Vista"%>
<%@ page import="sia.libs.formato.Cadena"%>
<%@ page import="sia.libs.formato.Letras"%>
<%@ page import="sia.rf.tesoreria.inversiones.acciones.NumeroLetra"%>
<jsp:useBean id="dtosFirmas" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="rfTrHistoricoReportes" class="sia.rf.tesoreria.registro.repHistInv.bcRfTrHistoricoReportes" scope="page"/>
<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesAutInversionDFCtrl</title>
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
    
    private String getQueryReporte(HttpServletRequest request){
        String regresa = null;
        SentennciasSE sentencia = new SentennciasSE(DaoFactory.CONEXION_TESORERIA);
        try  {      
            sentencia.addParam("idCompraInversion",request.getParameter("idCompra"));
            regresa = sentencia.getCommand("consultas.select.autorizaInversionDF.reportesInversiones");
        } catch (Exception e)  {
           e.printStackTrace();
        }  finally  {
              sentencia = null;
        }
        return regresa; 
    }

  
    private List<Vista> getDatosPrincipales(HttpServletRequest request){
        SentennciasSE sentencia = new SentennciasSE(DaoFactory.CONEXION_TESORERIA);
        List<Vista> registros = null;
        try  {   
            sentencia.addParam("idCompraInversion",request.getParameter("idCompra"));      
            registros =  sentencia.registros(DaoFactory.getTesoreria(),"consultas.select.autorizaInversionDF.reportesInversiones");
        } catch (Exception e)  {
           e.printStackTrace();
        }  finally  {
              sentencia = null;
        }
        return registros;
    }
    
     private String getFolio(){
       SentenciasCRS sen = null;
       String regresa = null;
       try  {
            sen = new SentenciasCRS();
            sen.addParam("idReporte",4);      
            sen.addParam("anio",Fecha.getAnioActual());      
            sen.registrosMap(DaoFactory.CONEXION_TESORERIA,"consultas.select.obtenConsecutivo.reportesInversiones");
            if(sen.next())
              regresa = sen.getString("CONSIG");
        } catch (Exception e)  {
           e.printStackTrace();
        }  finally  {
            sen = null;
        }
        return regresa;
      }
    
  %>
  <%
    StringBuffer queryReporte = new StringBuffer();
    String resultado  = null;
    String bancoCuenta = null;
    String leyenda[] = null;
    String valorArticulo = "";
    String verLeyendaAut = "0";
    String verLeyendaVobo = "0";
    String datosHistorico[] = {"-1","-1","-1","-1","-1","-1","-1"};
    Map parametros = new HashMap();
    Connection conn = null;
    String destino = null;
    List<Vista> sen = null;
    String cantLetras = null;
    String letras = null;
    Letras importeALetras = new Letras();
    String importe = null;
    Double efecImporte = 0D;
    Number cantidad = 0.00;
    NumeroLetra numeroLetra = new NumeroLetra();
    
    try  { 
        conn = DaoFactory.getTesoreria();
        conn.setAutoCommit(false);
        
        parametros.put("IMG_DIR",request.getSession().getServletContext().getRealPath("/Librerias/Imagenes/"));
        dtosFirmas.addParamVal("numEmpleado"," and NUM_EMPLEADO=:param ",request.getParameter("autoriza"));
        dtosFirmas.addParam("docto","DRIC");
        dtosFirmas.addParam("tipo","AUT");
        dtosFirmas.registrosMap(DaoFactory.CONEXION_TESORERIA,"transferenciaFondos.select.RhTrFirmasDocsTesoreriaTransferencia");
        if(dtosFirmas.next()){
           parametros.put("AUTORIZA",dtosFirmas.getString("NOMBRE"));
           parametros.put("FIRMAA",dtosFirmas.getString("PUESTO_ESP"));
           datosHistorico[3] = request.getParameter("autoriza").concat("~");
        }
        dtosFirmas.liberaParametros();
        
        dtosFirmas.addParamVal("numEmpleado"," and NUM_EMPLEADO=:param ",request.getParameter("reviso"));
        dtosFirmas.addParam("docto","DRIC");
        dtosFirmas.addParam("tipo","VB");
        dtosFirmas.registrosMap(DaoFactory.CONEXION_TESORERIA,"transferenciaFondos.select.RhTrFirmasDocsTesoreriaTransferencia");
        if(dtosFirmas.next()){
            parametros.put("REVISO",dtosFirmas.getString("NOMBRE"));
            parametros.put("FIRMAR",dtosFirmas.getString("PUESTO_ESP"));
            datosHistorico[4] = request.getParameter("reviso").concat("~");
        }
        dtosFirmas.liberaParametros();
        
        dtosFirmas.addParamVal("numEmpleado"," and NUM_EMPLEADO=:param ",request.getParameter("elaboro"));
        dtosFirmas.addParam("docto","DRIC");
        dtosFirmas.addParam("tipo","ELB");
        dtosFirmas.registrosMap(DaoFactory.CONEXION_TESORERIA,"transferenciaFondos.select.RhTrFirmasDocsTesoreriaTransferencia");
        if(dtosFirmas.next()){
            parametros.put("ELABORO",dtosFirmas.getString("NOMBRE"));
            parametros.put("FIRMAE",dtosFirmas.getString("PUESTO_ESP"));
            datosHistorico[5] = request.getParameter("elaboro").concat("~");
        }
        dtosFirmas.liberaParametros(); 
        
        if (request.getParameter("observaciones") != null){
            parametros.put("OBSERVACIONES",request.getParameter("observaciones"));
            datosHistorico[2] = request.getParameter("observaciones").concat("~");
        }
        else{
             parametros.put("OBSERVACIONES",request.getParameter("observaciones"));
            datosHistorico[2] =  " ".concat("~");
        }
        
        leyenda = request.getParameterValues("leyenda");
        if (request.getParameterValues("leyenda")!=null){
            for (int i = 0; i < leyenda.length; i++){
                valorArticulo = valorArticulo.concat(leyenda[i]).concat(",");
                if (leyenda[i].equals("1")){
                    if (request.getParameter("autoriza").equals("99282"))
                            verLeyendaAut="1";
                    else {
                        if (request.getParameter("reviso").equals("101716"))
                            verLeyendaAut="2";
                    }
                } 
                else {
                    if (request.getParameter("reviso").equals("99282"))
                        verLeyendaVobo="1";
                    else {
                        if (request.getParameter("reviso").equals("101716"))
                        verLeyendaVobo="2";
                    }
                }
            } // for 
        } 
        else {
            verLeyendaAut="0";
            verLeyendaVobo="0";
            valorArticulo = " ";
        }
        parametros.put("LEYENDAAUT",verLeyendaAut);
        parametros.put("LEYENDAVOBO",verLeyendaVobo);
        if (!valorArticulo.equals(" "))
            valorArticulo = valorArticulo.substring(0,valorArticulo.length()-1);
        datosHistorico[6]= valorArticulo;
        
        parametros.put("SUBREPORT_DIR","/tesoreria/reportesInversion/Reportes/");
        
        sen = getDatosPrincipales(request);
        
        parametros.put("FECHA_COMPRA","'".concat(Fecha.formatear(2,sen.get(0).getField("FECHA").replaceAll("-",""))).concat("'"));
        parametros.put("FECHA", Fecha.formatear(Fecha.FECHA_LARGA,sen.get(0).getField("FECHA").replaceAll("-","")));
        if (sen.get(0).getField("PLAZO").equals("1") || sen.get(0).getField("PLAZO").equals("2") || 
            sen.get(0).getField("PLAZO").equals("3") || sen.get(0).getField("PLAZO").equals("4") || 
            sen.get(0).getField("PLAZO").equals("5") || sen.get(0).getField("PLAZO").equals("6") )
            parametros.put("PLAZO","1,2,3,4,5,6");
        else if(sen.get(0).getField("PLAZO").equals("7") || sen.get(0).getField("PLAZO").equals("8") || 
                sen.get(0).getField("PLAZO").equals("9") || sen.get(0).getField("PLAZO").equals("10") ||
                sen.get(0).getField("PLAZO").equals("11")|| sen.get(0).getField("PLAZO").equals("12")  )
                   parametros.put("PLAZO","7,8,9,10,11,12");
             else if (sen.get(0).getField("PLAZO").equals("13") || sen.get(0).getField("PLAZO").equals("14") || 
                      sen.get(0).getField("PLAZO").equals("15") || sen.get(0).getField("PLAZO").equals("16") || 
                      sen.get(0).getField("PLAZO").equals("17") || sen.get(0).getField("PLAZO").equals("18") ||
                      sen.get(0).getField("PLAZO").equals("19") || sen.get(0).getField("PLAZO").equals("20") )
                          parametros.put("PLAZO","13,14,15,16,17,18,19,20");
              else if (sen.get(0).getField("PLAZO").equals("21") || sen.get(0).getField("PLAZO").equals("22") || 
                       sen.get(0).getField("PLAZO").equals("23") || sen.get(0).getField("PLAZO").equals("24") ||
                       sen.get(0).getField("PLAZO").equals("25") || sen.get(0).getField("PLAZO").equals("26") ||
                       sen.get(0).getField("PLAZO").equals("27") )
                           parametros.put("PLAZO","21,22,23,24,25,26,27");
                   else 
                      parametros.put("PLAZO",sen.get(0).getField("PLAZO"));        
    
        efecImporte = Double.parseDouble(sen.get(0).getField("MONTO")) + Double.parseDouble(sen.get(0).getField("IMPORTE")==null?"0.00":sen.get(0).getField("IMPORTE"));
        importe  = efecImporte.toString();
        letras = importeALetras.getMoneda(importe);
        cantidad = Double.parseDouble(importe);
        cantLetras = numeroLetra.getCantidadLetra(cantidad,DaoFactory.getTesoreria());
        parametros.put("CANTIDADLETRA", "(".concat(Cadena.letraCapital(cantLetras).concat(")")));
        parametros.put("MONTOTALINV", efecImporte);
    
        datosHistorico[1]= request.getParameter("idCompra").concat("~");
        datosHistorico[0]= request.getParameter("idCuenta").concat("~");
    
    
        request.getSession().setAttribute("formatoSalida",request.getParameter("tipoReporte"));
        request.getSession().setAttribute("conexion",DaoFactory.CONEXION_TESORERIA);
        if (request.getParameter("tipoReporte").equals("pdf"))
            request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesInversion/Reportes/autorizanInvDF");
        else
            request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesInversion/Reportes/autorizaInvDFXls");
        request.getSession().setAttribute("nombreArchivo","autorizaInversion");
        resultado = getQueryReporte(request);
        request.getSession().setAttribute("query",new StringBuffer(resultado)); 
        request.getSession().setAttribute("parametros",parametros);
        
        if (request.getParameter("tipoReporte").equals("pdf")){
           rfTrHistoricoReportes.setConsecutivo(Integer.toString(Fecha.getAnioActual()).concat(getFolio()));
           rfTrHistoricoReportes.setReporte("4");
           rfTrHistoricoReportes.setInformacion(datosHistorico[0].concat(datosHistorico[1]).concat(datosHistorico[2]).concat(datosHistorico[3]).concat(datosHistorico[4]).concat(datosHistorico[5]).concat(datosHistorico[6]));
           destino = request.getParameter("procedencia")!=null?request.getParameter("procedencia"):"";
           if (destino.equals("historicos")){
              if (request.getParameter("actualizar").equals("true")){
                rfTrHistoricoReportes.setFecha(request.getParameter("fecha")); 
                rfTrHistoricoReportes.setReporte(request.getParameter("numReporte")); 
                rfTrHistoricoReportes.setConsecutivo(request.getParameter("consecutivo")); 
                rfTrHistoricoReportes.update(conn);
                conn.commit();
              }
           }
           else{
              rfTrHistoricoReportes.setFecha(Fecha.getHoy()); 
              rfTrHistoricoReportes.insert(conn);
              conn.commit();
           }
        }       
    } catch (Exception ex)  {
        ex.printStackTrace();
    } finally  {
         sen = null;
    }
    
  %>
   <body onload="ir()">
   <form id="form1" name="form1">
   </form>
  </body>
</html>