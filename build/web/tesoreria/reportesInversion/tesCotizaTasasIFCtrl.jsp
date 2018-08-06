<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.libs.formato.Fecha" %>
<%@ page import="sia.db.dao.DaoFactory"%>
<%@ page import="sia.db.sql.*"%>
<%@ page import="sia.db.sql.Vista"%>
<%@ page import="sia.rf.tesoreria.reportes.CotizacionTasasDatasource"%>
<%@ page import="sia.rf.tesoreria.reportes.CotizacionTasas"%>
<jsp:useBean id="dtosFirmas" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="rfTrHistoricoReportes" class="sia.rf.tesoreria.registro.repHistInv.bcRfTrHistoricoReportes" scope="page"/>
<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesCotizaTasasIFCtrl</title>
  <script language="JavaScript"  type="text/javascript">
     
       function ir(){
            document.getElementById('form1').action='../../Librerias/reportes/reporte.jsp';
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
    
  
    private List<Vista> getVencimientoDisponiblidad(HttpServletRequest request){
        SentennciasSE sentencia = new SentennciasSE(DaoFactory.CONEXION_TESORERIA);
        List<Vista> registros = null;
        try  {   
            sentencia.addParam("fechaInicial",request.getParameter("fechaInicio"));      
            registros =  sentencia.registros(DaoFactory.getTesoreria(),"consultas.select.venDispCotTasas.reportesInversiones");
        } catch (Exception e)  {
            e.printStackTrace();
        }  finally  {
              sentencia = null;
        }
        return registros;
    }
  
    private List<Vista> getTasaBanxico(HttpServletRequest request){
        SentennciasSE sentencia = new SentennciasSE(DaoFactory.CONEXION_TESORERIA);
        List<Vista> registros = null;
        try  {   
            sentencia.addParam("fechaInicial",request.getParameter("fechaInicio"));      
            registros =  sentencia.registros(DaoFactory.getTesoreria(),"consultas.select.tasaBanxico.reportesInversiones");
        } catch (Exception e)  {
            e.printStackTrace();
        }  finally  {
              sentencia = null;
        }
        return registros;
    }
    
     private List<Vista> getIdBancos(HttpServletRequest request){
        SentennciasSE sentencia = new SentennciasSE(DaoFactory.CONEXION_TESORERIA);
        List<Vista> registros = null;
        try  {   
            sentencia.addParam("dia",request.getParameter("fechaInicio"));      
            registros =  sentencia.registros(DaoFactory.getTesoreria(),"catalogos.select.RfTrTasasRendimientoBancosCotizacion.reportesInversiones");
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
    Connection conn = null;
    String datosHistorico[] ={"-1","-1","-1","-1","-1","-1","-1","-1","-1"};
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    Map parametros = new HashMap();
    String destino = null;
    List <Vista> tasasReg= null;
    List <Vista> idBcoTasas= null;
    List <Vista> registros= null;
    boolean nafinsa = false, hsbc = false, banamex = false, concentradora = false;
    Double salHSBC = 0D, salBMX = 0D, salCon = 0D;
    List<Vista> sen = null;
    String[] renTasa = null;
    StringBuffer tTasasRendimiento = new StringBuffer();
    
    try  { 
        conn = DaoFactory.getTesoreria();
        conn.setAutoCommit(false);
        
        destino = request.getParameter("procedencia")!=null?request.getParameter("procedencia"):"";
        request.getSession().setAttribute("imgDir",request.getSession().getServletContext().getRealPath("/Librerias/Imagenes/"));
        request.getSession().setAttribute("subreportDir","/tesoreria/reportesInversion/Reportes/");
        
        if (sdf.parse(request.getParameter("fechaInicio")).after(sdf.parse("08/05/2012")))
            request.getSession().setAttribute("etiqueta","2");
        else
            request.getSession().setAttribute("etiqueta","1");
       
        if (sdf.parse(request.getParameter("fechaInicio")).after(sdf.parse("23/05/2012")))
            request.getSession().setAttribute("mostrar","0");
        else
            request.getSession().setAttribute("mostrar","1");
    
        if (!request.getParameter("observaciones").equals("")){
            request.getSession().setAttribute("observaciones",request.getParameter("observaciones"));
            datosHistorico[2] = request.getParameter("observaciones").concat("~");
        }
        else{
            request.getSession().setAttribute("observaciones"," ");
            datosHistorico[2] =  " ".concat("~");
        }        

        if (!request.getParameter("inversionRec").equals("")){
            request.getSession().setAttribute("inversion",Double.parseDouble(request.getParameter("inversionRec")));
            datosHistorico[8] = request.getParameter("inversionRec");
        }
        else{
            request.getSession().setAttribute("inversion",0.00);
            datosHistorico[8] = "0.00";
        }
        
        if (!request.getParameter("retiro").equals("")){
            request.getSession().setAttribute("retiro",Double.parseDouble(request.getParameter("retiro")));
            datosHistorico[6] = request.getParameter("retiro").concat("~");
        }
        else{
            request.getSession().setAttribute("retiro",0.00);
            datosHistorico[6] = "0.00".concat("~");
        }
        
        if (!request.getParameter("fondoReserva").equals("")){
            request.getSession().setAttribute("fondoReserva",Double.parseDouble(request.getParameter("fondoReserva")));
            datosHistorico[7] = request.getParameter("fondoReserva").concat("~");
        }
        else{
            request.getSession().setAttribute("fondoReserva",0.00);
            datosHistorico[7] = "0.00".concat("~");
        }
      
        tasasReg = getTasaBanxico(request);
        if (tasasReg.size()==2){
            request.getSession().setAttribute("tasa",tasasReg.get(0).getField("TASA"));
            request.getSession().setAttribute("tasaT",tasasReg.get(1).getField("TASA"));
        }
        else if (tasasReg.size()==1){
                request.getSession().setAttribute("tasa",tasasReg.get(0).getField("TASA"));
                request.getSession().setAttribute("tasaT","0.0");
             }
             else{
                request.getSession().setAttribute("tasa","0.0");
                request.getSession().setAttribute("tasaT","0.0");
            }

        request.getSession().setAttribute("fecha","'".concat(Fecha.formatear(Fecha.FECHA_CORTA_GUION,estableceFormato(request.getParameter("fechaInicio")).toString())).concat("'"));
        request.getSession().setAttribute("fechaCotizada",Fecha.formatear(Fecha.FECHA_CORTA,estableceFormato(request.getParameter("fechaInicio")).toString()));
  
        datosHistorico[0] = request.getParameter("fechaInicio").concat("~");
        datosHistorico[5] = " ~";

        request.getSession().setAttribute("periodo","ene - ".concat(Fecha.getNombreMesCorto(Integer.parseInt(Fecha.formatear(Fecha.FECHA_CORTA,estableceFormato(request.getParameter("fechaInicio")).toString()).substring(3,5))-1).toLowerCase()));
  
        registros = getVencimientoDisponiblidad(request);
        for (int i = 0; i < registros.size(); i++)  {
            if (registros.get(i).getField("ORDEN").toString().equals("1")){
                request.getSession().setAttribute("ven_nafinsa",Double.parseDouble(registros.get(i).getField("TOTAL").toString()));
                nafinsa = true;
            }
            if (registros.get(i).getField("ORDEN").toString().equals("2")){
                request.getSession().setAttribute("ven_hsbc",Double.parseDouble(registros.get(i).getField("TOTAL").toString()));  
                salHSBC = Double.parseDouble(registros.get(i).getField("TOTAL").toString());
                hsbc = true;
            }
            if (registros.get(i).getField("ORDEN").toString().equals("3")){
                request.getSession().setAttribute("ven_banamex",Double.parseDouble(registros.get(i).getField("TOTAL").toString()));  
                salBMX = Double.parseDouble(registros.get(i).getField("TOTAL").toString());
                banamex = true;
            }
            if (registros.get(i).getField("ORDEN").toString().equals("4")){
                salCon = Double.parseDouble(registros.get(i).getField("TOTAL").toString()) - salHSBC - salBMX;
                request.getSession().setAttribute("saldo_concentradora",salCon);    
                concentradora = true;
            }
        }
    
        if (!nafinsa)
            request.getSession().setAttribute("ven_nafinsa",0.00);  
        if (!hsbc)
            request.getSession().setAttribute("ven_hsbc",0.00);  
        if (!banamex)
            request.getSession().setAttribute("ven_banamex",0.00);  
        if (!concentradora)
            request.getSession().setAttribute("saldo_concentradora",0.00);    

        dtosFirmas.addParamVal("numEmpleado"," and NUM_EMPLEADO=:param ",request.getParameter("elaboro"));
        dtosFirmas.addParam("docto","STFO");
        dtosFirmas.addParam("tipo","ELB");
        dtosFirmas.registrosMap(DaoFactory.CONEXION_TESORERIA,"transferenciaFondos.select.RhTrFirmasDocsTesoreriaTransferencia");
        if(dtosFirmas.next()){
           request.getSession().setAttribute("nom_cotizo",dtosFirmas.getString("NOMBRE"));
           request.getSession().setAttribute("puesto_cotizo",dtosFirmas.getString("PUESTO_ESP"));
           datosHistorico[3] = request.getParameter("elaboro").concat("~");
        }
        dtosFirmas.liberaParametros();
        
        dtosFirmas.addParamVal("numEmpleado"," and NUM_EMPLEADO=:param ",request.getParameter("reviso"));
        dtosFirmas.addParam("docto","STFO");
        dtosFirmas.addParam("tipo","REV");
        dtosFirmas.registrosMap(DaoFactory.CONEXION_TESORERIA,"transferenciaFondos.select.RhTrFirmasDocsTesoreriaTransferencia");
        if(dtosFirmas.next()){
            request.getSession().setAttribute("nom_vobo",dtosFirmas.getString("NOMBRE"));
            request.getSession().setAttribute("puesto_vobo",dtosFirmas.getString("PUESTO_ESP"));
            datosHistorico[4] = request.getParameter("reviso").concat("~");
        }
        dtosFirmas.liberaParametros();

        CotizacionTasasDatasource datasource = new CotizacionTasasDatasource();        
        idBcoTasas = getIdBancos(request);
        for (int i = 0; i < idBcoTasas.size(); i++)  {
            renTasa= request.getParameterValues("valor".concat(idBcoTasas.get(i).getField("ID_BANCO")));
            CotizacionTasas cotiza = new CotizacionTasas(idBcoTasas.get(i).getField("INSTITUCION_FINANCIERA"),Double.parseDouble(idBcoTasas.get(i).getField("DIA_1")),Double.parseDouble(idBcoTasas.get(i).getField("DIAS_7")),Double.parseDouble(idBcoTasas.get(i).getField("DIAS_14")),Double.parseDouble(idBcoTasas.get(i).getField("DIAS_21")),Double.parseDouble(idBcoTasas.get(i).getField("DIAS_28")),renTasa[0].equals("")?"N/D":renTasa[0],renTasa[1].equals("")?"N/D":renTasa[1],renTasa[2].equals("")?"N/D":renTasa[2],renTasa[3].equals("")?"N/D":renTasa[3]);
            datasource.addCotizacionTasas(cotiza);
            tTasasRendimiento.append(idBcoTasas.get(i).getField("ID_BANCO"));
            tTasasRendimiento.append(",").append(renTasa[0]);
            tTasasRendimiento.append(",").append(renTasa[1]);
            tTasasRendimiento.append(",").append(renTasa[2]);
            tTasasRendimiento.append(",").append(renTasa[3]);
            tTasasRendimiento.append("|");
        }
        
        datosHistorico[1] = tTasasRendimiento.toString().substring(0,tTasasRendimiento.toString().length()-1).concat("~");
        request.getSession().setAttribute("listado",datasource);
        request.getSession().setAttribute("registros",idBcoTasas.size());
        request.getSession().setAttribute("con",DaoFactory.CONEXION_TESORERIA);
    
        request.getSession().setAttribute("formatoSalida",request.getParameter("tipoReporte"));
        request.getSession().setAttribute("conexion",DaoFactory.CONEXION_TESORERIA);
        if (request.getParameter("tipoReporte").equals("pdf"))
            request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesInversion/Reportes/cotizacionTasas");
        else
            request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesInversion/Reportes/cotizacionTasasXls");
        request.getSession().setAttribute("nombreArchivo","cotizacionTasas");
        request.getSession().setAttribute("parametros",parametros);
        
        if (request.getParameter("tipoReporte").equals("pdf")){
           rfTrHistoricoReportes.setConsecutivo(Integer.toString(Fecha.getAnioActual()).concat(getFolio()));
           rfTrHistoricoReportes.setReporte("1");
           rfTrHistoricoReportes.setInformacion(datosHistorico[0].concat(datosHistorico[1]).concat(datosHistorico[2]).concat(datosHistorico[3]).concat(datosHistorico[4]).concat(datosHistorico[5]).concat(datosHistorico[6]).concat(datosHistorico[7]).concat(datosHistorico[8]));
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