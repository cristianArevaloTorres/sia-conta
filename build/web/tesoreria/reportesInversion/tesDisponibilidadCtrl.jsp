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

<jsp:useBean id="rfTrFondoReserva" class="sia.rf.tesoreria.registro.repInversiones.bcRfTrFondoReserva" scope="page"/>

<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesDisponibilidadCtrl</title>
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
            sentencia.addParam("fechaInicial",Fecha.formatear(Fecha.FECHA_CORTA_GUION,estableceFormato(request.getParameter("fechaInicio")).toString())); 
            regresa = sentencia.getCommand("consultas.select.disponibilidadFinanciera.reportesInversiones");
        } catch (Exception e)  {
           e.printStackTrace();
        }  finally  {
              sentencia = null;
        }
        return regresa; 
    }

  
     private String getFolio(){
       SentenciasCRS sen = null;
       String regresa = null;
       try  {
            sen = new SentenciasCRS();
            sen.addParam("idReporte",3);      
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
  
        private List<Vista> getSaldoCtas(HttpServletRequest request){
            SentennciasSE sentencia = new SentennciasSE(DaoFactory.CONEXION_TESORERIA);
            List<Vista> registros = null;
            try  {   
                sentencia.addParam("fechaInicial",Fecha.formatear(Fecha.FECHA_CORTA_GUION,estableceFormato(request.getParameter("fechaInicio")).toString())); 
                registros =  sentencia.registros(DaoFactory.getTesoreria(),"consultas.select.disponibilidadFinanciera.reportesInversiones");
            } catch (Exception e)  {
                e.printStackTrace();
            }  finally  {
                sentencia = null;
            }
            return registros;
        }
  
      private String getDisponibilidad(HttpServletRequest request, String fechaHist){
       SentenciasCRS sen = null;
       String regresa = null;
       String nomQuery = null;
       SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
       try  {
        sen = new SentenciasCRS();
        if(request.getParameter("procedencia")!=null){
          if ( sdf.parse(fechaHist).after(sdf.parse("23/05/2012")))
              nomQuery = "totalDisponbilidadFinancieraSV";
          else
              nomQuery = "totalDisponbilidadFinanciera";
        }
        else
            nomQuery = "totalDisponbilidadFinancieraSV";
        sen.addParam("fechaInicial",Fecha.formatear(Fecha.FECHA_CORTA_GUION,estableceFormato(request.getParameter("fechaInicio")).toString())); 
        sen.registrosMap(DaoFactory.CONEXION_TESORERIA,"consultas.select.".concat(nomQuery).concat(".reportesInversiones"));
        if(sen.next())
          regresa = sen.getString("TOTALDISPONIBILIDAD");
        } catch (Exception e)  {
           e.printStackTrace();
        }  finally  {
            sen = null;
        }
        return regresa;
      }
    
  %>
  <%
      Connection conn = null;
      Double montoInv = 0D;
      Double comPagos = 0D;
      Double compEgre = 0D;
      Double saldoAR = 0D;
      Double saldoVencimiento = 0D;
      Double invierte = 0D;
      Double retira  = 0D;
      Double fondoReservaCE = 0D;
      List <Vista> registros = null;
      Double porcentajeFR = 0D;
      String fechaHist = null;
      String datosHistorico[] = {"-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1"};
      Map parametros = new HashMap();
      String resultado  = null;
      String destino = null;
      
      try  {   
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        conn = DaoFactory.getTesoreria();
        conn.setAutoCommit(false);
        registros = getSaldoCtas(request); 
        
        compEgre = !request.getParameter("conEgresosCP").equals("")?  Double.parseDouble(request.getParameter("conEgresosCP")) : 0.00;
        montoInv = !request.getParameter("conIngresoInv").equals("")?  Double.parseDouble(request.getParameter("conIngresoInv")) : 0.00;
        comPagos = !request.getParameter("vencimientoCP").equals("")?  Double.parseDouble(request.getParameter("vencimientoCP")) : 0.00;
        invierte = !request.getParameter("conEgresosInv").equals("")?  Double.parseDouble(request.getParameter("conEgresosInv")) : 0.00;
        retira = !request.getParameter("vencimientoInv").equals("")?  Double.parseDouble(request.getParameter("vencimientoInv")) : 0.00;
        /// 1 = nafinsa     2 = otros bancos 
        if (request.getParameter("banco").equals("1")){
            saldoAR = Double.parseDouble(registros.get(0).getField("CONEGRESOS")) - compEgre;
            saldoVencimiento = Double.parseDouble(registros.get(0).getField("VENCIMIENTODIA")) - comPagos;
            fondoReservaCE = (saldoAR - invierte ) + (saldoVencimiento - retira);
        }
        else{
            saldoAR = Double.parseDouble(registros.get(0).getField("CONEGRESOS")) - compEgre;
            fondoReservaCE = saldoAR - invierte;
        }
        
        if (request.getParameter("procedencia")!=null)
            fechaHist=request.getParameter("fecha");
        
        
        porcentajeFR = (fondoReservaCE/Double.parseDouble(getDisponibilidad(request, fechaHist)))*100.00;
        DecimalFormat twoDForm = new DecimalFormat("#.##"); 
        
        if (request.getParameter("tipoReporte").equals("pdf")){
            if(request.getParameter("procedencia")!=null){
                if(request.getParameter("actualizar").equals("true")){
                    rfTrFondoReserva.setFecha(fechaHist); 
                    //rfTrFondoReserva.setMonto(fondoReservaCE.toString()); 
                    rfTrFondoReserva.setMonto((Double.valueOf(twoDForm.format(fondoReservaCE))).toString()); 
                    rfTrFondoReserva.setPorcentaje((Double.valueOf(twoDForm.format(porcentajeFR))).toString()); 
                    rfTrFondoReserva.update(conn);
                }
            }
            else{
              rfTrFondoReserva.setFecha(Fecha.formatear(Fecha.FECHA_CORTA,estableceFormato(request.getParameter("fechaInicio")).toString())); 
              //rfTrFondoReserva.setMonto(fondoReservaCE.toString()); 
              rfTrFondoReserva.setMonto((Double.valueOf(twoDForm.format(fondoReservaCE))).toString()); 
              rfTrFondoReserva.setPorcentaje((Double.valueOf(twoDForm.format(porcentajeFR))).toString()); 
              rfTrFondoReserva.insert(conn);
            }
        }
        
        parametros.put("IMG_DIR",request.getSession().getServletContext().getRealPath("/Librerias/Imagenes/"));
        parametros.put("FECHA",Fecha.formatear(Fecha.FECHA_LARGA,estableceFormato(request.getParameter("fechaInicio")).toString()));
        datosHistorico[0] = request.getParameter("fechaInicio").concat("~");
  
        dtosFirmas.addParamVal("numEmpleado"," and NUM_EMPLEADO=:param ",request.getParameter("autoriza"));
        dtosFirmas.addParam("docto","DFIN");
        dtosFirmas.addParam("tipo","AUT");
        dtosFirmas.registrosMap(DaoFactory.CONEXION_TESORERIA,"transferenciaFondos.select.RhTrFirmasDocsTesoreriaTransferencia");
        if(dtosFirmas.next()){
           parametros.put("FIRMAVOBO",dtosFirmas.getString("NOMBRE"));
           parametros.put("PUESTOVOBO",dtosFirmas.getString("PUESTO_ESP"));
           datosHistorico[3] = request.getParameter("autoriza").concat("~");
        }
        dtosFirmas.liberaParametros();
        
        dtosFirmas.addParamVal("numEmpleado"," and NUM_EMPLEADO=:param ",request.getParameter("reviso"));
        dtosFirmas.addParam("docto","DFIN");
        dtosFirmas.addParam("tipo","REV");
        dtosFirmas.registrosMap(DaoFactory.CONEXION_TESORERIA,"transferenciaFondos.select.RhTrFirmasDocsTesoreriaTransferencia");
        if(dtosFirmas.next()){
            parametros.put("FIRMAREVISO",dtosFirmas.getString("NOMBRE"));
            parametros.put("PUESTOREVISO",dtosFirmas.getString("PUESTO_ESP"));
            datosHistorico[4] = request.getParameter("reviso").concat("~");
        }
        dtosFirmas.liberaParametros();
        
        dtosFirmas.addParamVal("numEmpleado"," and NUM_EMPLEADO=:param ",request.getParameter("elaboro"));
        dtosFirmas.addParam("docto","DFIN");
        dtosFirmas.addParam("tipo","ELB");
        dtosFirmas.registrosMap(DaoFactory.CONEXION_TESORERIA,"transferenciaFondos.select.RhTrFirmasDocsTesoreriaTransferencia");
        if(dtosFirmas.next()){
            parametros.put("FIRMAELABORO",dtosFirmas.getString("NOMBRE"));
            parametros.put("PUESTOELABORO",dtosFirmas.getString("PUESTO_ESP"));
            datosHistorico[5] = request.getParameter("elaboro").concat("~");
        }
        dtosFirmas.liberaParametros();       
  
        if (!request.getParameter("observaciones").equals("")){
            parametros.put("OBSERVACIONES",request.getParameter("observaciones"));
            datosHistorico[2] = request.getParameter("observaciones").concat("~");
        }
        else{
             parametros.put("OBSERVACIONES"," ");
            datosHistorico[2] =  " ".concat("~");
        }
        
        if (request.getParameter("banco").equals("1")){
            if (!request.getParameter("vencimientoCP").equals("")){
                parametros.put("COMPPAGOS", Double.parseDouble(request.getParameter("vencimientoCP"))); 
                datosHistorico[7] = request.getParameter("vencimientoCP").concat("~");
            }
            else{
                parametros.put("COMPPAGOS", 0.00);
                datosHistorico[7] = "0.00".concat("~");
            }
        }
        else{
            parametros.put("COMPPAGOS", 0.00);
            datosHistorico[7] = "0.00".concat("~");
        }

        if (!request.getParameter("conEgresosCP").equals("")){
            parametros.put("COMPPAGOSEGRESOS", Double.parseDouble(request.getParameter("conEgresosCP"))); 
            datosHistorico[11] = request.getParameter("conEgresosCP");
        }
        else{
            parametros.put("COMPPAGOSEGRESOS", 0.00);
            datosHistorico[11] = "0.00";
        }
  
        datosHistorico[6] = "0.00".concat("~");   
        
        if (!request.getParameter("conIngresoInv").equals("")){
            parametros.put("MONTOINV", Double.parseDouble(request.getParameter("conIngresoInv")));
            datosHistorico[8] = request.getParameter("conIngresoInv").concat("~");
        }
        else{
            parametros.put("MONTOINV", 0.00);
            datosHistorico[8] = "0.00".concat("~");
        }
        
        if (!request.getParameter("conEgresosInv").equals("")){
            parametros.put("INVIERTECE", Double.parseDouble(request.getParameter("conEgresosInv")));
            datosHistorico[9]= request.getParameter("conEgresosInv").concat("~");
        }
        else{
            parametros.put("INVIERTECE", 0.00);
            datosHistorico[9]= "0.00".concat("~");
        }
        
        if (!request.getParameter("vencimientoInv").equals("")){
            parametros.put("RETIROVD", Double.parseDouble(request.getParameter("vencimientoInv")));
            datosHistorico[10] = request.getParameter("vencimientoInv").concat("~");
        }
        else{
            parametros.put("RETIROVD", 0.00);
            datosHistorico[10] = "0.00".concat("~");
        }
        
        parametros.put("PTOTALDISP",Double.parseDouble(getDisponibilidad(request, fechaHist)));
          
        parametros.put("BANCO",request.getParameter("banco"));
        datosHistorico[1] = request.getParameter("banco").concat("~");
        
        request.getSession().setAttribute("formatoSalida",request.getParameter("tipoReporte"));
        request.getSession().setAttribute("conexion",DaoFactory.CONEXION_TESORERIA);
        
        if(request.getParameter("procedencia")!=null){
            if (sdf.parse(request.getParameter("fecha")).after(sdf.parse("23/05/2012")))
                request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesInversion/Reportes/disponibilidadFinancieraNLSV");
            else
                request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesInversion/Reportes/disponibilidadFinancieraNL");
        }
        else 
            request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesInversion/Reportes/disponibilidadFinancieraNLSV");
       
        resultado = getQueryReporte(request);
        request.getSession().setAttribute("nombreArchivo","dispFinanciera");
        request.getSession().setAttribute("query",new StringBuffer(resultado)); 
        request.getSession().setAttribute("parametros",parametros);
        
        if (request.getParameter("tipoReporte").equals("pdf")){
           rfTrHistoricoReportes.setConsecutivo(Integer.toString(Fecha.getAnioActual()).concat(getFolio()));
           rfTrHistoricoReportes.setReporte("3");
           rfTrHistoricoReportes.setInformacion(datosHistorico[0].concat(datosHistorico[1]).concat(datosHistorico[2]).concat(datosHistorico[3]).concat(datosHistorico[4]).concat(datosHistorico[5]).concat(datosHistorico[6]).
           concat(datosHistorico[7]).concat(datosHistorico[8]).concat(datosHistorico[9]).concat(datosHistorico[10]).concat(datosHistorico[11]));
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
              rfTrHistoricoReportes.setFecha(request.getParameter("fechaInicio")); 
              rfTrHistoricoReportes.insert(conn);
              conn.commit();
           }
        }       

      
  } catch (Exception ex)  {
      ex.printStackTrace();
  } finally  {
  }
  
    
  %>
   <body onload="ir()">
   <form id="form1" name="form1">
   </form>
  </body>
</html>