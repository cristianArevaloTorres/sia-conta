<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.db.dao.DaoFactory"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="sia.libs.formato.*"%>
<%@ page import="sia.rf.tesoreria.inversiones.acciones.NumeroLetra"%>
<%@ page import="sia.rf.tesoreria.reportes.TransferenciaFondos"%>
<%@ page import="sia.rf.tesoreria.reportes.TransferenciaFondosDatasource"%>
<jsp:useBean id="rfTrHistoricoReportes" class="sia.rf.tesoreria.registro.reportesHistorico.bcRfTrHistoricoReportes" scope="page"/>
<jsp:useBean id="dtosDirigido" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="dtosFirmas" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="consecutivo" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="cuentasBancarias" class="sia.db.sql.SentenciasCRS" scope="page"/>
<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<%
    String datosHistorico[] = {"-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1","-1"};
    Map mapParams = new HashMap();
    Connection conn = null;
    String importe = null;
    Letras importeALetras = new Letras();
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    String decimales = null;
    String letras = null;
    String cantLetras = null;
    Number cantidad = 0.00;
    NumeroLetra numeroLetra = new NumeroLetra();
    String sigConsecutivo = "";
    String folioFormado = null;
    String infTabla = "";
    String destino = null;
    String paramFolio = "";
    
    request.getSession().setAttribute("nombre","transferenciaFondos");
    request.getSession().setAttribute("rutaArchivo","/tesoreria/transferencias/Reportes/transferenciaFondos");
    request.getSession().setAttribute("imgDir",request.getSession().getServletContext().getRealPath("/Librerias/Imagenes/"));
    request.getSession().setAttribute("FECHA",Fecha.formatear(4,Fecha.getFormatoValidoFromDer(request.getParameter("fechaGenera"))));
    datosHistorico[0] = request.getParameter("fechaGenera").concat("~");
    
    datosHistorico[11] = request.getParameter("valida");
    
    if (request.getParameter("concepto") != null){
        request.getSession().setAttribute("CONCEPTO",request.getParameter("concepto"));
        datosHistorico[4] = request.getParameter("concepto").concat("~");
    }
    else{
        request.getSession().setAttribute("CONCEPTO"," ");
        datosHistorico[4] = "    ".concat("~");
    }
    
    if (request.getParameter("observaciones") != null){
        request.getSession().setAttribute("OBSERVACIONES",request.getParameter("observaciones"));
        datosHistorico[6] = request.getParameter("observaciones").concat("~");
    }
    else{
        request.getSession().setAttribute("OBSERVACIONES"," ");
        datosHistorico[6] =  "    ".concat("~");
    }
    
    importe = request.getParameter("importe");
    datosHistorico[1] = request.getParameter("importe").concat("~");;
    letras = importeALetras.getMoneda(importe);
    request.getSession().setAttribute("IMPORTE",letras.substring(0,letras.indexOf("(")-1));
    
    cantidad = Double.parseDouble(importe);
    boolean correcto = false;
    try  {
        conn = DaoFactory.getTesoreria();
        conn.setAutoCommit(false);
        
        cantLetras = numeroLetra.getCantidadLetra(cantidad,conn);
        request.getSession().setAttribute("CANTIDADLETRA","(".concat(Cadena.letraCapital(cantLetras).concat(")")));
        
        if(!request.getParameter("dirigidoA").equals("")) {
            datosHistorico[3] = request.getParameter("dirigidoA").concat("~");;
            dtosDirigido.addParamVal("idDestino"," and id_destinatario=:param",request.getParameter("dirigidoA"));
            dtosDirigido.registrosMap(DaoFactory.CONEXION_TESORERIA,"transferenciaFondos.select.destinatariosTF"); 
            if(dtosDirigido.next())
                request.getSession().setAttribute("DIRIGIDO",dtosDirigido.getString("PUESTO"));
        } 
        else {
            request.getSession().setAttribute("DIRIGIDO"," ");
            datosHistorico[3] = "-1".concat("~");;
        }
        
        datosHistorico[7] = request.getParameter("autoriza").concat("~");
        dtosFirmas.addParamVal("numEmpleado"," and NUM_EMPLEADO=:param ",request.getParameter("autoriza"));
        dtosFirmas.addParam("docto","STFO");
        dtosFirmas.addParam("tipo","AUT");                                      
        dtosFirmas.registrosMap(DaoFactory.CONEXION_TESORERIA,"transferenciaFondos.select.RhTrFirmasDocsTesoreriaTransferencia");
        if(dtosFirmas.next()){
            request.getSession().setAttribute("AUTORIZA",dtosFirmas.getString("NOMBRE"));
            request.getSession().setAttribute("FIRMAA",dtosFirmas.getString("PUESTO_ESP"));
        }
        dtosFirmas.liberaParametros();
        
        datosHistorico[8] = request.getParameter("reviso").concat("~");;
        dtosFirmas.addParamVal("numEmpleado"," and NUM_EMPLEADO=:param ",request.getParameter("reviso"));
        dtosFirmas.addParam("docto","STFO");
        dtosFirmas.addParam("tipo","REV");
        dtosFirmas.registrosMap(DaoFactory.CONEXION_TESORERIA,"transferenciaFondos.select.RhTrFirmasDocsTesoreriaTransferencia");
        if(dtosFirmas.next()){
            request.getSession().setAttribute("REVISO",dtosFirmas.getString("NOMBRE"));
            request.getSession().setAttribute("FIRMAR",dtosFirmas.getString("PUESTO_ESP"));
        }
        dtosFirmas.liberaParametros();
        
        datosHistorico[9] = request.getParameter("elaboro").concat("~");;
        dtosFirmas.addParamVal("numEmpleado"," and NUM_EMPLEADO=:param ",request.getParameter("elaboro"));
        dtosFirmas.addParam("docto","STFO");
        dtosFirmas.addParam("tipo","ELB");
        dtosFirmas.registrosMap(DaoFactory.CONEXION_TESORERIA,"transferenciaFondos.select.RhTrFirmasDocsTesoreriaTransferencia");
        if(dtosFirmas.next()){
            request.getSession().setAttribute("ELABORO",dtosFirmas.getString("NOMBRE"));
            request.getSession().setAttribute("FIRMAE",dtosFirmas.getString("PUESTO_ESP"));
        }
        dtosFirmas.liberaParametros();  
        destino = request.getParameter("procedencia")!=null?request.getParameter("procedencia"):"";
        if (destino.equals("historicos")){
            folioFormado = request.getParameter("folio");
            request.getSession().setAttribute("FOLIO",folioFormado);
            datosHistorico[10] = folioFormado.concat("~");
            if (!request.getParameter("existeFolio").equals("true") )
              datosHistorico[2] = request.getParameter("tipo").concat("~");
            else
              datosHistorico[2] ="-1".concat("~");
        }
        else {
          /// folio
          if (request.getParameter("area").equals("1")){
            if (sdf.parse(request.getParameter("fechaGenera")).after(sdf.parse("31/03/2013"))){
                if (request.getParameter("tipo").equals("REA")){
                    paramFolio = "DCIB";
                }
                else{
                    paramFolio = "DIIF";
                }
            }
            else 
                paramFolio = request.getParameter("tipo");
          }
          else
            paramFolio = "SEG";
          consecutivo.addParam("tipoFolio",paramFolio);
          consecutivo.addParam("anio",Fecha.getAnioActual());
          if (request.getParameter("area").equals("1")){
              if (sdf.parse(request.getParameter("fechaGenera")).after(sdf.parse("31/03/2013")))
                consecutivo.registrosMap(DaoFactory.CONEXION_TESORERIA,"transferenciaFondos.select.folioTransferenciaNR");
              else
                consecutivo.registrosMap(DaoFactory.CONEXION_TESORERIA,"transferenciaFondos.select.folioTransferencia");
          }
          else
            consecutivo.registrosMap(DaoFactory.CONEXION_TESORERIA,"transferenciaFondos.select.folioTransferencia");
          if(consecutivo.next()){
            sigConsecutivo = consecutivo.getString("conSig");
          }
        
          if(request.getParameter("area").equals("1")){
            
            if (sdf.parse(request.getParameter("fechaGenera")).after(sdf.parse("31/03/2013"))){
                if (request.getParameter("tipo").equals("REA")){
                    paramFolio = "DCIB";
                }
                else{
                    paramFolio = "DIIF";
                }
            }
            else 
                paramFolio = request.getParameter("tipo");
          
          
              folioFormado = Integer.toString(Fecha.getAnioActual()).concat(paramFolio).concat(sigConsecutivo);
              //folioFormado = Integer.toString(Fecha.getAnioActual()).concat(request.getParameter("tipo")).concat(sigConsecutivo);
              request.getSession().setAttribute("FOLIO",folioFormado);
              
              datosHistorico[10] = folioFormado.concat("~");
              datosHistorico[2] = request.getParameter("tipo").concat("~");
          }
          else{  /// ismael
              folioFormado = Integer.toString(Fecha.getAnioActual()).concat("SEG").concat(sigConsecutivo);
              request.getSession().setAttribute("FOLIO",folioFormado);
              datosHistorico[10] = folioFormado.concat("~");
              datosHistorico[2] ="-1".concat("~");
          } 
        }
        // recuperacion de tabla dinamica 
        int numeroTransaccion = 0;
        int col = 0;
        int posRef = -1;
        int registros = 0;
        String referenciaSinComa = "";
        TransferenciaFondosDatasource transferenciaFondosDatasource = new TransferenciaFondosDatasource();
        String[] transacciones = request.getParameterValues("hdNumTransaccion");
        String[] afectacion = request.getParameterValues("hdCA");
        String[] cuentas = request.getParameterValues("hdCuentaCA");
        String[] montos = request.getParameterValues("hdImporteCA");
        String[] referencias = request.getParameterValues("referencia");
        String[] tipoTransferencia = request.getParameterValues("hdTipoTransferencia");
        for (int i = 0; i < transacciones.length; i++)  {
            if(Integer.parseInt(transacciones[i])==numeroTransaccion){
                TransferenciaFondos transferenciaFondos = new TransferenciaFondos();
                if(afectacion[col].equals("Cargo")){
                    transferenciaFondos.setMontoCargo(Double.parseDouble(montos[col]));
                    transferenciaFondos.setMontoAbono(0.00);
                }
                else{
                    transferenciaFondos.setMontoAbono(Double.parseDouble(montos[col]));
                    transferenciaFondos.setMontoCargo(0.00);
                }
                cuentasBancarias.addParamVal("idCuenta"," and idCuenta=:param ",cuentas[col]);
                cuentasBancarias.addParamVal("tipo"," and tipo=:param ",tipoTransferencia[col]);
             //   cuentasBancarias.registrosMap(DaoFactory.CONEXION_TESORERIA,"transferenciaFondos.select.obtenDatosCuentas");
                cuentasBancarias.registrosMap(DaoFactory.CONEXION_TESORERIA,"transferenciaFondos.select.afectacion");
                if(cuentasBancarias.next()){
                    /*transferenciaFondos.setNumBanCuenta(cuentasBancarias.getString("nombre_corto").concat(" ").concat(cuentasBancarias.getString("NUM_CUENTA")));
                    transferenciaFondos.setNomCuenta(cuentasBancarias.getString("NOMBRE_CTA"));                */
               //     transferenciaFondos.setNumBanCuenta(cuentasBancarias.getString("banco").concat(" ").concat(cuentasBancarias.getString("cuenta")));
                    transferenciaFondos.setBanco(cuentasBancarias.getString("banco"));
                    transferenciaFondos.setNumCuenta(cuentasBancarias.getString("cuenta"));
                    transferenciaFondos.setNomCuenta(cuentasBancarias.getString("beneficiario"));                
                }
                transferenciaFondos.setGrupo(transacciones[i]);
          
                referenciaSinComa = referencias[posRef];
                referenciaSinComa = referenciaSinComa.replaceAll(","," - ");
                //transferenciaFondos.setReferencia(referencias[posRef]);
                transferenciaFondos.setReferencia(referenciaSinComa);
                
                //infTabla = infTabla +  transacciones[i].concat(",").concat(afectacion[col]).concat(",").concat(cuentas[col]).concat(",").concat(montos[col]).concat(",").concat(referencias[posRef]).concat(",").concat(tipoTransferencia[col]).concat("|");
                infTabla = infTabla +  transacciones[i].concat(",").concat(afectacion[col]).concat(",").concat(cuentas[col]).concat(",").concat(montos[col]).concat(",").concat(referenciaSinComa).concat(",").concat(tipoTransferencia[col]).concat("|");
                col++;
                transferenciaFondosDatasource.addTransferencia(transferenciaFondos);
                registros++;
            }
            else{
                posRef++;
            }
            numeroTransaccion=Integer.parseInt(transacciones[i]);
        }// for
        datosHistorico[5] = infTabla.substring(0,infTabla.length()-1).concat("~");
        
        request.getSession().setAttribute("listado",transferenciaFondosDatasource);
        request.getSession().setAttribute("registros",registros);
        request.getSession().setAttribute("con",DaoFactory.CONEXION_TESORERIA);
         
       // almacenar datos
       rfTrHistoricoReportes.setConsecutivo(folioFormado);
       rfTrHistoricoReportes.setReporte("2");
       rfTrHistoricoReportes.setInformacion(datosHistorico[0].concat(datosHistorico[1]).concat(datosHistorico[2]).concat(datosHistorico[3]).concat(datosHistorico[4]).concat(datosHistorico[5]).concat(datosHistorico[6]).concat(datosHistorico[7]).
       concat(datosHistorico[8]).concat(datosHistorico[9]).concat(datosHistorico[10]).concat(datosHistorico[11]));
       
       if (destino.equals("historicos")){
          if (request.getParameter("actualizar").equals("true")){
            rfTrHistoricoReportes.setFecha(request.getParameter("fecha")); 
            rfTrHistoricoReportes.update(conn);
            conn.commit();
          }
       }
       else{
          rfTrHistoricoReportes.setFecha(Fecha.getHoy()); 
          rfTrHistoricoReportes.insert(conn);
          conn.commit();
       }
       ///conn.commit();
       
       correcto = true;
    } catch (Exception ex)  {
        ex.printStackTrace();
        
    } finally {
      DaoFactory.closeConnection(conn);
    }
%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesTransferenciaFondosControl</title>
    <script language="javascript" type="text/javascript">
        
        function ir(){
            document.form1.submit();
        }
    </script>
  </head>
  <body onload="<%=correcto?"ir()":"" %>">
  <form id="form1" name="form1" action="../../Librerias/reportes/reporteTransferencia.jsp">
  </form>
  </body>
</html>