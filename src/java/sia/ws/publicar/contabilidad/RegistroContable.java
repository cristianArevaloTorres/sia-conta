 /*
  * RegistroContable.java
  *
  * Created on 9 de mayo de 2007, 11:45 AM
  *
  * Write by alejandro.jimenez
  * alejandro.jimenez@senado.gob.mx
  */

 package sia.ws.publicar.contabilidad;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import sia.db.dao.DaoFactory;
import sia.libs.formato.Cadena;
import sia.libs.formato.Error;
import sia.libs.formato.Fecha;
import sia.libs.formato.Numero;
import sia.rf.contabilidad.registroContable.formas.Areas;
import sia.rf.contabilidad.registroContable.formas.servicios.CuentasForma;
import sia.rf.contabilidad.registroContable.procesos.polizas.servicios.Polizas;
import sia.rf.contabilidad.registroContable.servicios.FechaAfectacion;
import sia.rf.contabilidad.registroContableNuevo.bcClasificadorCuentas;
import sia.rf.contabilidad.registroContableNuevo.bcCuentaContable;
import sia.rf.contabilidad.registroContableNuevo.bcPoliza;
import sia.ws.publicar.contabilidad.definicion.FormaLista;
import sia.ws.publicar.contabilidad.definicion.PrepararForma;
//import sia.ws.publicar.contabilidad.definicion.TransaccionWs;
//import sia.rf.contabilidad.sistemas.conprove.ventas.bcCliente;


/**
  *
  * @author alejandro.jimenez
  */
 public class RegistroContable {
   
/*
   
   public String afectacion(String maestroOperacion, String programa, String unidadEjecutora, String entidad, String ambito,
                            String referencia, String concepto, String importes, int numeroEmpleado, String clave) {
     PrepararPoliza prepararPoliza = null;
     String msjNotificacion        = null;
     Polizas poliza                = null;    
     ValidaWs validaWs             = null;     
     try {
       validaWs = new  ValidaWs(clave);
       if(validaWs.verificaHost()){
         prepararPoliza = new PrepararPoliza(maestroOperacion, programa, unidadEjecutora, entidad, ambito, 1, referencia, concepto, importes, numeroEmpleado, maestroOperacion, Polizas.CATALOGO_INSTITUCIONAL,false,null);
         //poliza = prepararPoliza.getPoliza();
         if (poliza.registrarPolizaAfectacion(Polizas.REGISTRAR_POLIZA)) {
           int consecutivo = 0;
           consecutivo = poliza.getConsecutivo();
           msjNotificacion =
               "1, Se realizo con exito el registro de la poliza del tipo D"
                .concat("-").concat(Cadena.rellenar(String.valueOf(poliza.getConsecutivo()),5, '0', true))
                .concat(" Fecha poliza "+Fecha.formatear(2,poliza.getFechaAfectacion()));
         }
       }//end validaWs.verificaHost() 
       else
         msjNotificacion="0,La clave es incorrecta, verifiquela por favor";
     }
     catch (Exception e) {
       Error.mensaje(e, "SIAFM");
       msjNotificacion = e.getMessage();
     }
     finally {
       prepararPoliza = null;
       poliza = null;
     }
     return "WS-Contabilidad [afetacion]" + msjNotificacion;
   }
*/
     /*
   public String dinamica(String maestroOperacion, String programa, String unidadEjecutora, String entidad, String ambito,
                          String referencia, String concepto, String importes, int numeroEmpleado, String clave) {
     PrepararPoliza prepararPoliza = null;
     String msjNotificacion        = null;
     Polizas poliza                = null;
     ValidaWs validaWs             = null;     
     StringBuffer error            = null;
     try {
       error    = new StringBuffer();    
       validaWs = new  ValidaWs(clave);
       if(validaWs.verificaHost()){
         prepararPoliza = new PrepararPoliza(maestroOperacion, programa, unidadEjecutora, entidad, ambito, 1, referencia, concepto, importes,
                                numeroEmpleado, maestroOperacion ,Polizas.CATALOGO_INSTITUCIONAL,true,null);
         //poliza = prepararPoliza.getPoliza();
         if (poliza.registrarPolizaAfectacion(Polizas.REGISTRAR_POLIZA)) {
           int consecutivo = 0;
           consecutivo = poliza.getConsecutivo();
           msjNotificacion =
               "1,Se realizo con exito el registro de la poliza del tipo D".concat("-").concat(Cadena.rellenar(String.valueOf(poliza.getConsecutivo()),
                                                                                                             5, '0',
                                                                                                             true)).concat(" Fecha poliza " +
                                                                                                                           Fecha.formatear(2,
                                                                                                                                           poliza.getFechaAfectacion()));
         }
       }//end validaWs.verificaHost() 
       else
         msjNotificacion="0,La clave es incorrecta, verifiquela por favor";
       
     }
     catch (Exception e) {
       Error.mensaje(e, "SIAFM");
       msjNotificacion = e.getMessage();  
       error.append("Parametros metodo[dinamica]: ");
       error.append(" <br>");
       error.append("MaestroOperacion-->"+maestroOperacion);
       error.append(" <br>");
       error.append("UniEjec|entidad|ambito--> "+ unidadEjecutora+entidad+ambito);
       error.append(" <br>");
       error.append("referencia-->"+referencia);
       error.append(" <br>");
       error.append("concepto-->"+concepto);
       error.append(" <br>");
       error.append("importes-->"+importes);     
       error.append(" <br>");
       error.append("Error--> "+ msjNotificacion);
       validaWs.enviaNotitificacion(error.toString());
       
     }
     finally {
       prepararPoliza = null;
       poliza         = null;
       validaWs       = null;
     }
     
     
     return "WS-Contabilidad [dinamica]" + msjNotificacion;
   }
   */
   public String formasCostos(String forma, String unidadEjecutora, String entidad, String ambito, String referencia, String concepto,
     String variables, int numeroEmpleado, String clave, String fechaPoliza, Connection conexionContabilidad, String idEvento, String ejercicio) throws SQLException, Exception {
     return formas(forma, unidadEjecutora, entidad, ambito, referencia, concepto, variables, numeroEmpleado, clave, Polizas.CATALOGO_COSTOS, fechaPoliza, Areas.MANUAL,conexionContabilidad,idEvento,ejercicio);
   }
   
   public String formasMipf(String forma, String unidadEjecutora, String entidad, String ambito, String referencia, String concepto,
                        String variables, int numeroEmpleado, String clave, String fechaAfectacion, Connection conexionContabilidad,String idEvento, String ejercicio) throws SQLException, Exception{
     return formas(forma, unidadEjecutora, entidad, ambito, referencia, concepto, variables, numeroEmpleado, clave, Polizas.CATALOGO_INSTITUCIONAL, fechaAfectacion, Areas.MIPF,conexionContabilidad,idEvento,ejercicio);
   }
   
   public String formasNoAcumulado(String forma, String unidadEjecutora, String entidad, String ambito, String referencia, String concepto,
                          String variables, int numeroEmpleado, String clave, String fechaAfectacion, Connection conexionContabilidad, String idEvento, String ejercicio) throws SQLException, Exception{
     return formas(forma, unidadEjecutora, entidad, ambito, referencia, concepto, variables, numeroEmpleado, clave, Polizas.CATALOGO_INSTITUCIONAL, fechaAfectacion, Areas.MIPF_NO_ACUMULADO,conexionContabilidad,idEvento,ejercicio);
   }
   
   public String formasVentas(String forma, String unidadEjecutora, String entidad, String ambito, String referencia, String concepto,
                        String variables, int numeroEmpleado, String clave, String fechaAfectacion, Connection conexionContabilidad, String idEvento, String ejercicio) throws SQLException, Exception{
     return formas(forma, unidadEjecutora, entidad, ambito, referencia, concepto, variables, numeroEmpleado, clave, Polizas.CATALOGO_INSTITUCIONAL, fechaAfectacion, Areas.VENTAS, conexionContabilidad,idEvento,ejercicio);
   }
   
   public String formas(String forma, String unidadEjecutora, String entidad, String ambito, String referencia, String concepto,
                        String variables, int numeroEmpleado, String clave, String fechaAfectacion, Connection conexionContabilidad, String idEvento, String ejercicio) throws SQLException, Exception{
     return formas(forma, unidadEjecutora, entidad, ambito, referencia, concepto, variables, numeroEmpleado, clave, Polizas.CATALOGO_INSTITUCIONAL, fechaAfectacion, Areas.MANUAL, conexionContabilidad,idEvento,ejercicio);
   }


   private String formas(String forma, String unidadEjecutora, String entidad, String ambito, String referencia, String concepto, String variables, int numeroEmpleado, String clave, int idCatalogoContable, 
                         String fechaPoliza, int area, Connection conexionContabilidad, String idEvento, String ejercicio) throws SQLException, Exception {
     String msjNotificacion;
     PrepararForma prepararForma;
     Polizas poliza;    
     ValidaWs validaWs           = null;   
     StringBuffer error          = null;
     String [] detalle;
     List<CuentasForma> cuentas  = new ArrayList<CuentasForma>();
     FormaLista formaLista       = null;
     FechaAfectacion fechaAfectacion = null;
     String mes                  = null;
     int consecutivo             = 0;
     //boolean mesCerrado          = false;
     String polizaId                = null;
     bcPoliza bcPoliza = new bcPoliza();
       System.out.println("Registro contable 22222");
     int posicionError = 0;
     try {       
      // conexionContabilidad.setAutoCommit(false);
       fechaAfectacion = new FechaAfectacion();
       error    = new StringBuffer();     
       validaWs = new  ValidaWs(clave);     
       String continua="SI";
       // if(validaWs.verificaHost() || continua.equals("SI")){
       if(continua.equals("SI")){
         mes = fechaPoliza.substring(4,6);

         // LA VALIDACION DE QUE SI EL MES SE ENCUENTRA CERRADO SE HIZO EN LA CLASE DE FORMAS DONDE VERIFICA SI LA CUENTA EXISTE
         /*if(mes.startsWith("0"))
                 mes = fechaPoliza.substring(5,6);
                 if (idEvento.equals("300"))
                 mesCerrado = true;
                 else
                 mesCerrado = fechaAfectacion.obtenerEstatusMes(mes,fechaPoliza.substring(0,4),unidadEjecutora,entidad,ambito,idCatalogoContable);
                 if(mesCerrado){*/
                detalle = variables.split("~");
                System.out.println("detalle = " + detalle.length);
                System.out.println("forma = " + forma);
                if (detalle.length > 0) {
                    prepararForma = new PrepararForma(forma, unidadEjecutora, entidad, ambito, variables, referencia, Integer.valueOf(numeroEmpleado), idCatalogoContable, fechaPoliza.concat(((Fecha.formatear(Fecha.HORA_LARGA, Fecha.getRegistro())).replace(":", "")).trim()));
                    prepararForma.setCuentasNoexiten(new ArrayList());
                    posicionError++;
                    for (int i = 0; i < detalle.length; i++) {
                        prepararForma.setVariables(detalle[i]);
                        posicionError++;
                        formaLista = prepararForma.getFormaPoliza("001", "1", "9", area, ejercicio, conexionContabilidad);
                        posicionError++;
                        //System.out.println("RegistroContable.detalle.length "+detalle.length);
                        //System.out.println("RegistroContable.detalle.area "+area);
                        //System.out.println("RegistroContable.detalle.referencia "+referencia);
                        //System.out.println("RegistroContable.detalle.formaLista.getCuentas() "+formaLista.getCuentas().toString());
                        if (detalle.length == 1 || area == Areas.MIPF_NO_ACUMULADO) {
                            cuentas.addAll(formaLista.getCuentas());
                        } else if (referencia.indexOf("SIGA") >= 0) {
                            isExisteCuentaSIGA(cuentas, formaLista.getCuentas());
                        } else {
                            cuentas = isExisteCuenta(cuentas, formaLista.getCuentas());
                        }
                    }
                    if (prepararForma.getCuentasNoexiten().size() > 0) {
                        throw new Exception("No existe la cuenta " + prepararForma.getCuentasNoexiten());
                    }
                    posicionError++;
                    //System.out.println("RegistroContable.formas");
                    poliza = prepararForma.getFormas(formaLista.getId(), cuentas, concepto, idEvento, conexionContabilidad);
                    //System.out.println("RegistroContable.formas*");
                    posicionError++;
                } else {
                    posicionError++;
                    prepararForma = new PrepararForma(forma, unidadEjecutora, entidad, ambito, variables, referencia, Integer.valueOf(numeroEmpleado), idCatalogoContable, fechaPoliza.concat(((Fecha.formatear(Fecha.HORA_LARGA, Fecha.getRegistro())).replace(":", "")).trim()));
                    formaLista = prepararForma.getFormaPoliza("001", "1", "9", area, ejercicio, conexionContabilidad);
                    cuentas.addAll(formaLista.getCuentas());
                    poliza = prepararForma.getFormas(formaLista.getId(), cuentas, concepto, idEvento, conexionContabilidad);
                    posicionError++;
                }

                /*
                 // EL SIGUIENTE CODIGO COMENTADO ES PARA ACTUALIZAR POLIZAS QUE YA HAN SIDO CREADAS SOLO MODIFICA EL DETALLE DE LA P�LIZA
                 polizaId = bcPoliza.select_rf_tr_polizas(conexionContabilidad, forma, referencia, String.valueOf(idEvento),ejercicio,mes, unidadEjecutora, entidad, ambito);
         
                 if(Integer.valueOf(polizaId.split(",")[0]) == Polizas.EXISTE_POLIZA){  
           
                 if (poliza.registrarPolizaAfectacionAutomaticas(conexionContabilidad,Polizas.REGISTRAR_POLIZA)) {
                 consecutivo = poliza.getConsecutivo();    
                 msjNotificacion ="1,".concat(String.valueOf(poliza.getPolizaId()).concat(",Se realizo con exito el registro de la poliza del tipo ***").
                 concat(poliza.getAbreviaturaPoliza()).concat("-").concat(
                 Cadena.rellenar(String.valueOf(poliza.getConsecutivo()), 5, '0', true)).concat("*** Fecha poliza "
                 +Fecha.formatear(2,poliza.getFechaAfectacion())
                 ));
                 }
                 else {
                 msjNotificacion="0,Error en el registro de la poliza";   
                 throw new Exception("Error en el registro de la poliza");
                 }  
                 }
                 else{
                   cuentas = isExisteCuenta(cuentas, formaLista.getCuentas());
                 }
          }
           if(prepararForma.getCuentasNoexiten().size()>0) {
                 throw new Exception("No existe la cuenta "+ prepararForma.getCuentasNoexiten());
             }
       posicionError++;
       System.out.println("RegistroContable.formas");
         poliza= prepararForma.getFormas(formaLista.getId(), cuentas, concepto,idEvento, conexionContabilidad);
       System.out.println("RegistroContable.formas*");
       posicionError++;
         }
         else {
       posicionError++;
           prepararForma = new PrepararForma(forma, unidadEjecutora, entidad, ambito, variables, referencia, Integer.valueOf(numeroEmpleado), idCatalogoContable,fechaPoliza.concat(((Fecha.formatear(Fecha.HORA_LARGA,Fecha.getRegistro())).replace(":","")).trim()));
           formaLista= prepararForma.getFormaPoliza("001","1","9", area,ejercicio,conexionContabilidad);   
           cuentas.addAll(formaLista.getCuentas());                    
           poliza= prepararForma.getFormas(formaLista.getId(), cuentas, concepto,idEvento, conexionContabilidad);
       posicionError++;
         }

/*
         // EL SIGUIENTE CODIGO COMENTADO ES PARA ACTUALIZAR POLIZAS QUE YA HAN SIDO CREADAS SOLO MODIFICA EL DETALLE DE LA P�LIZA
        polizaId = bcPoliza.select_rf_tr_polizas(conexionContabilidad, forma, referencia, String.valueOf(idEvento),ejercicio,mes, unidadEjecutora, entidad, ambito);
         
         if(Integer.valueOf(polizaId.split(",")[0]) == Polizas.EXISTE_POLIZA){  
           
          if (poliza.registrarPolizaAfectacionAutomaticas(conexionContabilidad,Polizas.REGISTRAR_POLIZA)) {
             consecutivo = poliza.getConsecutivo();    
             msjNotificacion ="1,".concat(String.valueOf(poliza.getPolizaId()).concat(",Se realizo con exito el registro de la poliza del tipo ***").
                              concat(poliza.getAbreviaturaPoliza()).concat("-").concat(
                                                                           Cadena.rellenar(String.valueOf(poliza.getConsecutivo()), 5, '0', true)).concat("*** Fecha poliza "
                                                                           +Fecha.formatear(2,poliza.getFechaAfectacion())
                                                                         ));
           }
           else {
              msjNotificacion="0,Error en el registro de la poliza";   
              throw new Exception("Error en el registro de la poliza");
           }  
         }
         else{
           poliza.posicionarPoliza(Integer.valueOf(polizaId.split(",")[0]));  
           poliza.setConsecutivo(Integer.valueOf(polizaId.split(",")[1]));
           if(poliza.registrarPolizaAfectacionAutomaticas(conexionContabilidad,Polizas.ACTUALIZAR_POLIZA)){
             // System.out.println("Poliza :".concat(poliza.getAbreviaturaPoliza().concat("-").concat(Cadena.rellenar(String.valueOf(poliza.getConsecutivo()),5,'0',true))));         
             msjNotificacion ="1,".concat(String.valueOf(poliza.getPolizaId()).concat(",Se realizo con exito la actualizacion de la poliza del tipo ***").
                              concat(poliza.getAbreviaturaPoliza()).concat("-").concat(
                              Cadena.rellenar(String.valueOf(poliza.getConsecutivo()), 5, '0', true)).concat("*** Fecha poliza "
                              +Fecha.formatear(2,poliza.getFechaAfectacion())));
           }
           //System.out.println("La p�liza ya ha sido registrada");
         }// AQUI TERMINA LA ACTUALIZACION DE UNA POLIZA

       */
        
    
          // PROCESO NORMAL PARA LA CREACION DE LA POLIZA
       posicionError++;
      if (poliza.registrarPolizaAfectacionAutomaticas(conexionContabilidad,Polizas.REGISTRAR_POLIZA)) {
       posicionError++;
           consecutivo = poliza.getConsecutivo();    
           msjNotificacion ="1,".concat(String.valueOf(poliza.getPolizaId()).concat(",Se realizo con exito el registro de la poliza del tipo ***").
                            concat(poliza.getAbreviaturaPoliza()).concat("-").concat(
                                                                         Cadena.rellenar(String.valueOf(poliza.getConsecutivo()), 5, '0', true)).concat("*** Fecha poliza "
                                                                         +Fecha.formatear(2,poliza.getFechaAfectacion())
                                                                       ));
       posicionError++;
        }
        else {
            msjNotificacion="0,Error en el registro de la poliza";   
            throw new Exception("Error en el registro de la poliza");
        }  // FIN PROCESO NORMAL 

       /*}      
       else{
         msjNotificacion="0,No se encuentra el mes "+ mes +" disponible para afectar o ya se encuentra cerrado definitivamente"; 
         throw new Exception("No se encuentra el mes " + mes+ " UE "+ unidadEjecutora +" Entindad " +entidad+ " Ambito " +ambito+" disponible para afectar o ya se encuentra cerrado definitivamente");
       }*/
       }//end validaWs.verificaHost() 
       else{
         msjNotificacion = "0,La clave es incorrecta, verifiquela por favor"; 
         throw new Exception("La clave es incorrecta, verifiquela por favor");
       }
     } //try
     catch (Exception e) {
         System.out.println("posicionError = "+posicionError);
       System.out.println("JLPN. Error en metodo: formas."+e.getMessage());
       Error.mensaje(e, "SIAFM");
       msjNotificacion = "0, " + e.getMessage();
       error.append("Parametros metodo[formas]: ");
       error.append(" <br>");
         error.append("forma-->").append(forma);
       error.append(" <br> ");
         error.append("UniEjec|entidad|ambito--> ").append(unidadEjecutora).append(entidad).append(ambito);
       error.append(" <br> ");
         error.append("referencia-->").append(referencia);
       error.append(" <br> ");
         error.append("variables-->").append(variables);        
       error.append(" <br> ");
         error.append("Error-->  ").append(msjNotificacion);
       validaWs.enviaNotitificacion(error.toString());
       throw e;
         
     } //catch
 //    return "WS-Contabilidad [formas]" + msjNotificacion; JLPN
       return msjNotificacion;
   } // formas

  private String sumarImporte(String importe1, String importe2){
    return String.valueOf (Numero.formatear(Numero.NUMERO_DECIMALES,(Double.valueOf(importe1)+Double.valueOf(importe2))));
  }
    
    
  private List<CuentasForma> isExisteCuenta(List<CuentasForma> cuentas, List<CuentasForma> cuentasABuscar) throws  Exception {
    CuentasForma cuentaForma;
    List<CuentasForma> cuentasAcumuladas  = new ArrayList<CuentasForma>();
    try{
      if(cuentas.isEmpty()){
        cuentas.addAll(cuentasABuscar); 
      }
      else{
        for(int pocicion=0; pocicion < cuentasABuscar.size();pocicion++){
          Iterator iteratorLista = cuentas.iterator();
          int agrego = 0;
          while (iteratorLista.hasNext()) {
            cuentaForma = (CuentasForma)iteratorLista.next();
            if (cuentasABuscar.get(pocicion).getCuentaContableId() == cuentaForma.getCuentaContableId() &&
              cuentasABuscar.get(pocicion).getTipoOperacion().equals(cuentaForma.getTipoOperacion()) && 
              cuentasABuscar.get(pocicion).getReferencia().equals(cuentaForma.getReferencia())) {
                cuentaForma.setImporte(sumarImporte(cuentaForma.getImporte(), cuentasABuscar.get(pocicion).getImporte()));
                agrego++;
            }
          }
          if(agrego==0){
            cuentasAcumuladas.add(cuentasABuscar.get(pocicion));
          }
        }
        cuentas.addAll(cuentasAcumuladas);
      }
    }
    catch(Exception e){
      Error.mensaje(e, "SIAFM");
      throw e;  
    }
    return cuentas;
  }
    
 /*  public String cancelarPoliza(int polizaId,int numeroEmpleado,String clave) {
     TransaccionWs  transaccionWs = null;    
     String msjNotificacion       = null;
     ValidaWs validaWs            = null;   
     
     try{
       validaWs = new  ValidaWs(clave);
       if(validaWs.verificaHost()){
         transaccionWs = new  TransaccionWs();
         msjNotificacion=transaccionWs.cancelarPoliza(polizaId,numeroEmpleado);
       }//end validaWs.verificaHost() 
       else
         msjNotificacion="0,La clave es incorrecta, verifiquela por favor";
     }
     catch(Exception e){
       Error.mensaje(e,"SIAFM");
       msjNotificacion.concat("0,").concat(e.getMessage());
       
     }
     finally{
       transaccionWs = null;
       validaWs      = null;
     }
     return msjNotificacion;    
   } */
   /*
   public String polizasAutomaticas(String polizaAutomaticaId, int numeroEmpleado,String clave){
     ProcesarTotal              procesarTotal       = null;
     List                       documentoAutomatico = null;    
     CicloDocumento             cicloDocumento      = null;  
     Map                        mapParametros       = null;
     StringBuffer               msjNotificacion     = null;
     Formatos                   formatos            = null;
     Sentencias                 sentencia           = null;
     String                     query               = null;
     List <Vista>               detalle             = null;
     Vista                      vista               = null;
     ValidaWs validaWs                              = null;   
     StringBuffer  error                            = new StringBuffer();    
     try{
       validaWs = new  ValidaWs(clave);
       if(validaWs.verificaHost()){ 
         msjNotificacion     = new StringBuffer();
         documentoAutomatico = new ArrayList();
         mapParametros       = new HashMap();
         mapParametros.put("poliza_automatica_id", polizaAutomaticaId);
         formatos = new Formatos( Contabilidad.getPropiedad("presupuesto.polizaAutomatica.getPolizaAutomatica"),mapParametros);
         query = formatos.getSentencia();
         sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD);
         detalle=sentencia.registros(query);
         if(detalle.size()==0){
           msjNotificacion.append("0,No se encontro la poliza automatica con el id"+polizaAutomaticaId);
           validaWs.enviaNotitificacion(msjNotificacion.toString()); 
         }  
         else{
           vista=detalle.get(0);           
           documentoAutomatico.add(new DocumentoAutomatico(polizaAutomaticaId,vista.getField("ID_DOCUMENTO"),vista.getField("TIPO_DOCUMENTO"), vista.getField("TRAMITE") ));
           cicloDocumento = new CicloDocumento(documentoAutomatico);
           procesarTotal  = new ProcesarTotal(cicloDocumento.recorrer(), numeroEmpleado, 1);      
           procesarTotal.procesarWS();
           //mapParametros.put("poliza_automatica_id", polizaAutomaticaId);
           formatos = new Formatos( Contabilidad.getPropiedad("presupuesto.polizaAutomatica.getPolizaId"),mapParametros);
           query = formatos.getSentencia();
           sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD);
           detalle=sentencia.registros(query);
           if(detalle.size()==0)
             msjNotificacion.append("0,No se encontro una poliza asociada");
             
           else{
             vista=detalle.get(0); 
             msjNotificacion.append("1,Se realizo con exito el registro de la poliza: ");
             msjNotificacion.append(vista.getField("TIPO_POLIZA"));
             msjNotificacion.append(" - ");
             msjNotificacion.append(Cadena.rellenar(vista.getField("CONSECUTIVO"),5,'0',true));
             msjNotificacion.append("fecha p?liza:" );
             msjNotificacion.append(vista.getField("FECHA_AFECTACION"));                
           } 
         }  
       }//end validaWs.verificaHost() 
       else
         msjNotificacion.append("0,La clave es incorrecta, verifiquela por favor");
         
     }
     catch(Exception e){
       Error.mensaje(e,"SIAFM");
       error.append("Parametros metodo[polizasAutomaticas] ");
       error.append(" <br>");
       error.append("poliza AutomaticaId-->"+polizaAutomaticaId);
       error.append(polizaAutomaticaId);
       error.append(" <br>");
       error.append("Error--<"+ e.getMessage());
       validaWs.enviaNotitificacion(error.toString());        
       //msjNotificacion.append("0,Ocurrio un error al generar la poliza");
     }
     finally{
       procesarTotal       = null;
       mapParametros.clear();
       mapParametros      = null;
       documentoAutomatico.clear();
       documentoAutomatico = null;
       validaWs            = null;
     }
    return msjNotificacion.toString();
   }
*/
   /*
     public String AgregaClienteCuentaContable(String mayor, String subMayor, String programa, String unidadEjecutora, String entidadAmbito,String claAlmContabilidad, String rfcCliente, String nombreCliente, String pFecha, String clave) throws SQLException, Exception{
       //PrepararPoliza prepararPoliza = null;
       String msjNotificacion        = null;
       //Polizas poliza                = null;    
       ValidaWs validaWs             = null;
       bcCuentaContable bcCuenta = new bcCuentaContable();
       bcClasificadorCuentas bcClasificador = new bcClasificadorCuentas();
       bcCliente bcCli = new bcCliente();
       String lsResul=null;
       String lsMaxCliente="";
       // String lsCuentaContable="112060005"+unidadEjecutora+entidadAmbito+claAlmContabilidad+"0005"+claCliente+"0000";

       String lsFecha=pFecha;
       //String lsFecha="28/07/2010";
       String lsAno=lsFecha.substring(6);
      Connection conexion=null;
       try {
         conexion=DaoFactory.getContabilidad();
         conexion.setAutoCommit(false);
         bcCli.select_rf_tc_clentes(conexion,rfcCliente);
         if (bcCli.getId_contable()==null){ 
           lsMaxCliente=bcCli.select_rf_tc_clientes_max(conexion);
           bcCli.setId_contable(lsMaxCliente);
           bcCli.setRfc(rfcCliente);
           bcCli.setNombre(nombreCliente);
           bcCli.insert_rf_tc_clientes(conexion);
         }  
         String lsCuentaContable=mayor+subMayor+programa+unidadEjecutora+entidadAmbito+claAlmContabilidad+bcCli.getId_contable()+"00000000";
           
         validaWs = new  ValidaWs(clave);
       //  if(validaWs.verificaHost()){
            lsResul=bcCuenta.select_rf_tr_cuentas_contables_existe(conexion,lsCuentaContable, lsFecha);
            if (!(lsResul.equals("")))
              msjNotificacion ="1, El cliente ya existe.";
            else{
              lsResul=bcClasificador.select_RF_TC_CLASIFICADOR_CUENTAS_mayor(conexion,mayor); 
              if (lsResul.equals(""))
                  msjNotificacion ="0, La cuenta de mayor 1122 no existe.";
              else{
                  bcCuenta.setFecha_vig_ini("01/01/"+lsAno);
                  bcCuenta.setFecha_vig_fin("31/12/"+lsAno);
                  bcCuenta.setCuenta_mayor_id(bcClasificador.getCuenta_mayor_id());
                  bcCuenta.setCuenta_contable(lsCuentaContable);
                  bcCuenta.setDescripcion(nombreCliente);
                  bcCuenta.insert_rf_tr_cuentas_contables_cliente(conexion,"7");
                  msjNotificacion ="1, El cliente fue dado de alta al catalogo de cuentas contables: "+lsCuentaContable;
              }
            }
            conexion.commit();
        // }//end validaWs.verificaHost() 
       //  else
         //  msjNotificacion="0,La clave es incorrecta, verifiquela por favor";
       }
       catch (Exception e) {
         conexion.rollback();
         Error.mensaje(e, "SIAFM");
         msjNotificacion = "0,"+e.getMessage();
           throw e; 
       }
       finally {
           if (conexion !=null){
               conexion.close();
               conexion=null;
           }      

       }
       return bcCli.getId_contable();
     }
*/
     public String AgregaPromotorCuentaContable(String unidadEjecutora, String entidadAmbito,String claAlmContabilidad, String claPromotor, String nombrePromotor, String pFecha, String clave) throws SQLException, Exception{
       //PrepararPoliza prepararPoliza = null;
       String msjNotificacion        = null;
       //Polizas poliza                = null;    
       ValidaWs validaWs             = null;     
       bcCuentaContable bcCuenta = new bcCuentaContable();
       bcClasificadorCuentas bcClasificador = new bcClasificadorCuentas();
       String lsResulContable;
       String lsResul;
       String lsFecha=pFecha;
       String lsAno=lsFecha.substring(6);
      // String nivel8="";
      // if (lsAno.equals("2011"))
      //     nivel8="0000";
        
       //String lsCuentaContable="113040005"+unidadEjecutora+entidadAmbito+claAlmContabilidad+"0004"; //Sin clave promotor/vendedor
        String lsCuentaContable="112310005"+unidadEjecutora+entidadAmbito+claAlmContabilidad+"0001"; //Sin clave promotor/vendedor
      Connection conexion=null;
       try {
         conexion=DaoFactory.getContabilidad();
         conexion.setAutoCommit(false);
         validaWs = new  ValidaWs(clave);
   //      if(validaWs.verificaHost()){
            lsResulContable=bcCuenta.select_rf_tr_cuentas_contables_promotor(conexion,lsCuentaContable, lsAno); //obtiene siguiente clave contable
            if (lsResulContable.equals("")) {
               msjNotificacion ="0, No se pudo obtener la clave del promotor/vendedor.";
           }
            else{
              lsResul=bcClasificador.select_RF_TC_CLASIFICADOR_CUENTAS_mayor(conexion,"1123"); 
              if (lsResul.equals("")) {
                    msjNotificacion ="0, La cuenta de mayor 1123 no existe.";
                }
              else{
                  bcCuenta.setFecha_vig_ini("01/01/"+lsAno);
                  bcCuenta.setFecha_vig_fin("31/12/2099");
                  bcCuenta.setCuenta_mayor_id(bcClasificador.getCuenta_mayor_id());
                  lsCuentaContable+=lsResulContable+"0000"; //completa la cuenta contable con el consecutivo de promotor/vendedor.
                  bcCuenta.setCuenta_contable(lsCuentaContable);
                  bcCuenta.setDescripcion(nombrePromotor);
                  bcCuenta.insert_rf_tr_cuentas_contables_cliente(conexion,"8");
                  msjNotificacion ="1: El Promotor/vendedor fue dado de alta al catalogo de cuentas contables: "+lsCuentaContable + " con el consecutivo:"+lsResulContable;
              }
            }  
            conexion.commit();
    //     }//end validaWs.verificaHost() 
      //   else
        //   msjNotificacion="0,La clave es incorrecta, verifiquela por favor";
       }
       catch (Exception e) {
         conexion.rollback();
         Error.mensaje(e, "SIAFM");
         msjNotificacion = "0,"+e.getMessage();
           throw e; 
       }
       finally {
           if (conexion !=null){
               conexion.close();
           }      

       }
       return "WS-Contabilidad [Alta cliente]" + msjNotificacion;
     }
    
 private List<CuentasForma> isExisteCuentaSIGA(List<CuentasForma> cuentas, List<CuentasForma> cuentasABuscar) throws  Exception {
    CuentasForma cuentaForma;
    List<CuentasForma> cuentasAcumuladas  = new ArrayList<CuentasForma>();
    try{
      if(cuentas.isEmpty()){
        cuentas.addAll(cuentasABuscar); 
      }
      else{
        for(int pocicion=0; pocicion < cuentasABuscar.size();pocicion++){
          Iterator iteratorLista = cuentas.iterator();
          int agrego = 0;
          while (iteratorLista.hasNext()) {
            cuentaForma = (CuentasForma)iteratorLista.next();
            if (cuentasABuscar.get(pocicion).getCuentaContableId() == cuentaForma.getCuentaContableId() &&
              cuentasABuscar.get(pocicion).getTipoOperacion().equals(cuentaForma.getTipoOperacion())
              //&& cuentasABuscar.get(pocicion).getReferencia().equals(cuentaForma.getReferencia())
              ) {
                cuentaForma.setImporte(sumarImporte(cuentaForma.getImporte(), cuentasABuscar.get(pocicion).getImporte()));
                agrego++;
            }
          }
          if(agrego==0){
            cuentasAcumuladas.add(cuentasABuscar.get(pocicion));
          }
        }
        cuentas.addAll(cuentasAcumuladas);
      }
    }
    catch(Exception e){
      Error.mensaje(e, "SIAFM");
      throw e;  
    }
    return cuentas;
  }

 }
