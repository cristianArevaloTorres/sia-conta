package sia.rf.contabilidad.registroContableEvento;

import sia.db.dao.DaoFactory;
import java.sql.Connection;
import java.sql.SQLException;   
import java.util.HashMap;

import sia.rf.contabilidad.registroContable.formas.Areas;


import sia.ws.publicar.contabilidad.Registro;
import sun.jdbc.rowset.CachedRowSet;


import sia.rf.contabilidad.sistemas.inventarios.SiafmInventarios;
import sia.rf.contabilidad.sistemas.inventarios.SiafmInventariosCOG;

import sia.rf.contabilidad.registroContableNuevo.bcCuentaContable;

/**
 *
 * @author jorgeluis.perez
 */
public class EventoInventario {
    
  private EventoContable eventoContable=null;
  private FormaContable formaContable=null;
  private Modulo modulo = null;
  private Sistema sistema=null;
  private Registro registro=null;
  private BitacoraLocal bitacoraLocal=null;
  private CachedRowSet crs=null;
  private int banAre=0;
  private String origen="";

  
  public EventoInventario() {
    eventoContable = new EventoContable();
    formaContable = new FormaContable();
    modulo = new Modulo();
    sistema = new Sistema();
    registro = new Registro();
    bitacoraLocal = new BitacoraLocal();
  }
     
  private String obtenerNombreDatasource(String  pEventoContable) throws SQLException, Exception {
    Connection conexion=null;
    String resultado=null;

    try{
      conexion=DaoFactory.getContabilidad();
      eventoContable.select_rf_tr_eventoContable(conexion,pEventoContable);
      modulo.select_rf_tr_modulo(conexion,eventoContable.getIdmodulo());
      sistema.select_rf_tr_sistema(conexion,modulo.getIdsistema());
      crs=formaContable.select_rf_tr_formaContable(conexion,pEventoContable);
      resultado=sistema.getCadconexion(); 
      banAre=eventoContable.getBanAre();
      origen=eventoContable.getOrigen();
    }catch(Exception e) {
       System.out.println("Ocurrio un errror al accesar el metodo obtenerNombreDatasource "+e.getMessage());   
       throw e;
    }
    finally{
      if(conexion!=null){
        conexion.close();
        conexion = null;
      }
    }
      return resultado;
  }
  
  private Connection obtenerConexionModulo(String nombreDS)throws SQLException, Exception{
    Connection conexion=null;
    try{
      conexion=DaoFactory.getConnection(nombreDS);
    }catch(Exception e){
       System.out.println("Ocurrio un errror al accesar el metodo obtenerConexionModulo "+e.getMessage()); 
       throw e;
    }
      return conexion;
  }

  private void preparaRegistro(String cadena, String claveSeguridadWS, String fechaEnvio,String forma, String unidadEjecutora,String entidad,String ambito, String referencia,String concepto, String catalogoCuenta, String idEvento, String ejercicio) throws SQLException, Exception {
    registro.setForma(forma);           
    registro.setUnidadeje(unidadEjecutora);
    registro.setEntidad(entidad);
    registro.setAmbito(ambito);
    registro.setReferencia(referencia);
    registro.setConcepto(concepto);
    registro.setVariables(cadena);
    registro.setNoempleado(2222);
    registro.setClave(claveSeguridadWS);
    registro.setFecenvio(fechaEnvio);
    registro.setCatalogocuenta(catalogoCuenta);
    registro.setIdEvento(idEvento);
    registro.setEjercicio(ejercicio);
  }
    
  public String procesarEvento(int eventoContable, int eventoProceso, String parametros, String claveSeguridadWS) throws SQLException, Exception{  
    String nombreDS=null;
    Connection conexionSistema=null;
    Connection conexionContabilidad=null;
    String cadena=null;
    String resultado=null;
    String [] arregloParametros = parametros.split("&");
    HashMap hashMap = new HashMap(); 
    
    // SiafmInventarios siafmInventarios = null;
    SiafmInventariosCOG siafmInventariosCOG = null;
    bcCuentaContable cuentaContable = null;
    int liPoliza=0;
    String lsPolizas="|";
    String [] meses = {"Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre" };
    int liMes=0;
    String ejercicio = null;
    try{ 
      nombreDS=obtenerNombreDatasource(Integer.toString(eventoContable)); 
      conexionSistema=obtenerConexionModulo(nombreDS);
      conexionSistema.setAutoCommit(false);
      conexionContabilidad=DaoFactory.getContabilidad();
      conexionContabilidad.setAutoCommit(false);
      ejercicio = arregloParametros[0].substring(6);    
      while (crs.next()){     
         System.out.println(eventoContable);
         switch (eventoContable){
           case 6:  //Evento 6 de Inventarios parametros(fecha de envio, unidad ejecutora, entidad, ambito, referencia, numero, factura y rfc)    
              //siafmInventarios = new SiafmInventarios();
              siafmInventariosCOG = new SiafmInventariosCOG();
              //if ((arregloParametros[0].substring(6).equals("2010"))||(arregloParametros[0].substring(6).equals("2011")&&arregloParametros[0].substring(3,5).equals("01"))){
               //     cadena=siafmInventarios.Forma7(conexionSistema,arregloParametros[0],arregloParametros[4],arregloParametros[1],arregloParametros[2],arregloParametros[3], conexionContabilidad,crs.getString("catalogocuenta"), registro,arregloParametros[5],arregloParametros[6]);
               // }else{
                    cadena=siafmInventariosCOG.Forma7(conexionSistema,arregloParametros[0],arregloParametros[4],arregloParametros[1],arregloParametros[2],arregloParametros[3], conexionContabilidad,crs.getString("catalogocuenta"), registro,arregloParametros[5],arregloParametros[6]);
                //}     
                  if (!cadena.equals("")){
                  preparaRegistro(cadena,claveSeguridadWS,arregloParametros[0],crs.getString("forma"),arregloParametros[1],arregloParametros[2],arregloParametros[3], arregloParametros[4],crs.getString("concepto"),crs.getString("catalogocuenta"),String.valueOf(eventoContable),ejercicio);
                  resultado=registro.aplicaRegistroContable(conexionContabilidad,crs.getString("catalogocuenta"),Areas.INVENTARIO);
                  liPoliza=resultado.indexOf("***")+3;
                  lsPolizas=lsPolizas+resultado.substring(liPoliza,liPoliza+7)+"|";
                  System.out.println(lsPolizas);
                  hashMap.clear();
                }
                else{
                  lsPolizas=lsPolizas+"SIN POLIZA|";                
                  resultado="1,Sin poliza por hashMap vacio";
                }                              
              break;               
             case 7:  //Evento 7 de Inventarios parametros(fecha de envio, unidad ejecutora, entidad, ambito, referencia, numero, factura y rfc) 
               //siafmInventarios = new SiafmInventarios();
               siafmInventariosCOG = new SiafmInventariosCOG();
               cadena="";
               switch (Integer.parseInt(crs.getString("idforma"))){
                 case 21: { 
                           //if ((arregloParametros[0].substring(6).equals("2010"))||(arregloParametros[0].substring(6).equals("2011")&&arregloParametros[0].substring(3,5).equals("01")))
                           //    cadena=siafmInventarios.Forma12(conexionSistema,arregloParametros[0],arregloParametros[4],arregloParametros[1],arregloParametros[2],arregloParametros[3],crs.getString("catalogocuenta"),registro,arregloParametros[5],arregloParametros[6]); //Forma MI
                           //else
                               cadena=siafmInventariosCOG.Forma12(conexionSistema,arregloParametros[0],arregloParametros[4],arregloParametros[1],arregloParametros[2],arregloParametros[3],crs.getString("catalogocuenta"),registro,arregloParametros[5],arregloParametros[6]); //Forma MI                                                          }
                          } 
                          break;                 
               /*  case 26: {
                     if ((arregloParametros[0].substring(6).equals("2010"))||(arregloParametros[0].substring(6).equals("2011")&&arregloParametros[0].substring(3,5).equals("01")))
                         cadena=siafmInventarios.Forma13(conexionSistema,arregloParametros[0],arregloParametros[4],arregloParametros[1],arregloParametros[2],arregloParametros[3],arregloParametros[5],arregloParametros[6]);
                     else
                         cadena=siafmInventariosCOG.Forma13(conexionSistema,arregloParametros[0],arregloParametros[4],arregloParametros[1],arregloParametros[2],arregloParametros[3],arregloParametros[5],arregloParametros[6]);
                 }
                 break; //Forma FM */
               }
               if (!cadena.equals("")){
                 //cadena=Cadena.construyeCadena(hashMap);
                 preparaRegistro(cadena,claveSeguridadWS,arregloParametros[0],crs.getString("forma"),arregloParametros[1],arregloParametros[2],arregloParametros[3], arregloParametros[4],crs.getString("concepto"),crs.getString("catalogocuenta"),String.valueOf(eventoContable),ejercicio);
                 resultado=registro.aplicaRegistroContable(conexionContabilidad,crs.getString("catalogocuenta"),Areas.INVENTARIO);
                 liPoliza=resultado.indexOf("***")+3;
                 lsPolizas=lsPolizas+resultado.substring(liPoliza,liPoliza+7)+"|";
                 System.out.println(lsPolizas);
                 hashMap.clear();
               } 
               else{
                 lsPolizas=lsPolizas+"SIN POLIZA|";                 
                 resultado="1,Sin poliza por hashMap vacio";
               }                     
               break;     
             case 8:  //Evento 8 de Inventarios parametros(fecha de envio, unidad ejecutora, entidad, ambito, referencia, numero, referencia)     
               //siafmInventarios = new SiafmInventarios();
               siafmInventariosCOG = new SiafmInventariosCOG();
               //if ((arregloParametros[0].substring(6).equals("2010"))||(arregloParametros[0].substring(6).equals("2011")&&arregloParametros[0].substring(3,5).equals("01")))               
                 //  cadena=siafmInventarios.Forma16(conexionSistema,arregloParametros[0],arregloParametros[4],arregloParametros[1],arregloParametros[2],arregloParametros[3], crs.getString("catalogocuenta"), registro, arregloParametros[5],arregloParametros[6]);
               //else
                   cadena=siafmInventariosCOG.Forma16(conexionSistema,arregloParametros[0],arregloParametros[4],arregloParametros[1],arregloParametros[2],arregloParametros[3], crs.getString("catalogocuenta"), registro, arregloParametros[5],arregloParametros[6]);
               if (!cadena.equals("")){ 
                 //cadena=Cadena.construyeCadena(hashMap);
                 preparaRegistro(cadena,claveSeguridadWS,arregloParametros[0],crs.getString("forma"),arregloParametros[1],arregloParametros[2],arregloParametros[3], arregloParametros[5]+" Baja",crs.getString("concepto"),crs.getString("catalogocuenta"),String.valueOf(eventoContable),ejercicio);
                 resultado=registro.aplicaRegistroContable(conexionContabilidad,crs.getString("catalogocuenta"),Areas.INVENTARIO);
                 liPoliza=resultado.indexOf("***")+3;
                 lsPolizas=lsPolizas+resultado.substring(liPoliza,liPoliza+7)+"|";
                 System.out.println(lsPolizas);
                 hashMap.clear();
               } 
               else{
                 lsPolizas=lsPolizas+"SIN POLIZA|";                 
                 resultado="1,Sin poliza por hashMap vacio";
               }                     
               break;                              
             case 9:  //Evento 9 de Inventarios parametros(fecha de envio, unidad ejecutora, entidad, ambito, referencia, numero, referencia)     
              // siafmInventarios = new SiafmInventarios();
               siafmInventariosCOG = new SiafmInventariosCOG();
               //if ((arregloParametros[0].substring(6).equals("2010"))||(arregloParametros[0].substring(6).equals("2011")&&arregloParametros[0].substring(3,5).equals("01")))               
                 // cadena=siafmInventarios.Forma18(conexionSistema,arregloParametros[0],arregloParametros[4],arregloParametros[1],arregloParametros[2],arregloParametros[3],crs.getString("catalogocuenta"), registro,arregloParametros[5],arregloParametros[6]);
               // else   
                  cadena=siafmInventariosCOG.Forma18(conexionSistema,arregloParametros[0],arregloParametros[4],arregloParametros[1],arregloParametros[2],arregloParametros[3],crs.getString("catalogocuenta"), registro,arregloParametros[5],arregloParametros[6]);
               if (!cadena.equals("")){
                 //cadena=Cadena.construyeCadena(hashMap);
                 preparaRegistro(cadena,claveSeguridadWS,arregloParametros[0],crs.getString("forma"),arregloParametros[1],arregloParametros[2],arregloParametros[3], arregloParametros[5]+" Alta",crs.getString("concepto"),crs.getString("catalogocuenta"),String.valueOf(eventoContable),ejercicio);
                 resultado=registro.aplicaRegistroContable(conexionContabilidad,crs.getString("catalogocuenta"),Areas.INVENTARIO);
                 liPoliza=resultado.indexOf("***")+3;
                 lsPolizas=lsPolizas+resultado.substring(liPoliza,liPoliza+7)+"|";
                 System.out.println(lsPolizas);
                 hashMap.clear();
               } 
               else{
                 lsPolizas=lsPolizas+"SIN POLIZA|";                 
                 resultado="1,Sin poliza por hashMap vacio";
               }                     
               break;  
              
             case 10:  //Evento 10 de Inventarios parametros(fecha de envio, unidad ejecutora, entidad, ambito, referencia, numero, factura y rfc)     
               //siafmInventarios = new SiafmInventarios();
               siafmInventariosCOG = new SiafmInventariosCOG();
               //if ((arregloParametros[0].substring(6).equals("2010"))||(arregloParametros[0].substring(6).equals("2011")&&arregloParametros[0].substring(3,5).equals("01")))                              
               //   cadena=siafmInventarios.Forma19(conexionSistema,arregloParametros[0],arregloParametros[4],arregloParametros[1],arregloParametros[2],arregloParametros[3],arregloParametros[5],arregloParametros[6]);
               //else   
                  cadena=siafmInventariosCOG.Forma19(conexionSistema,arregloParametros[0],arregloParametros[4],arregloParametros[1],arregloParametros[2],arregloParametros[3],crs.getString("catalogocuenta"), registro,arregloParametros[5],arregloParametros[6]);               
               if (!cadena.equals("")){
                 //cadena=Cadena.construyeCadena(hashMap);
                 preparaRegistro(cadena,claveSeguridadWS,arregloParametros[0],crs.getString("forma"),arregloParametros[1],arregloParametros[2],arregloParametros[3], arregloParametros[4]+" "+arregloParametros[5]+arregloParametros[6],siafmInventariosCOG.getConcepto(),crs.getString("catalogocuenta"),String.valueOf(eventoContable),ejercicio);
               /* if ((arregloParametros[0].substring(6).equals("2010"))||(arregloParametros[0].substring(6).equals("2011")&&arregloParametros[0].substring(3,5).equals("01"))){                              
                 liMes=Integer.parseInt(siafmInventarios.getMes())-1;
                 resultado=registro.AgregaCuentaContable("12201","0001",siafmInventarios.getUnidad(),siafmInventarios.getAmbito(),siafmInventarios.getConcepto(),siafmInventarios.getEjercicio(),siafmInventarios.getMes(),arregloParametros[0],meses[liMes],"7",crs.getString("catalogocuenta"));
                 resultado=registro.AgregaCuentaContable("12202","0001",siafmInventarios.getUnidad(),siafmInventarios.getAmbito(),siafmInventarios.getConcepto(),siafmInventarios.getEjercicio(),siafmInventarios.getMes(),arregloParametros[0],meses[liMes],"7",crs.getString("catalogocuenta"));
                 resultado=registro.AgregaCuentaContable("12203","0001",siafmInventarios.getUnidad(),siafmInventarios.getAmbito(),siafmInventarios.getConcepto(),siafmInventarios.getEjercicio(),siafmInventarios.getMes(),arregloParametros[0],meses[liMes],"7",crs.getString("catalogocuenta"));                 
                 resultado=registro.AgregaCuentaContable("51201","0001",siafmInventarios.getUnidad(),siafmInventarios.getAmbito(),siafmInventarios.getConcepto(),siafmInventarios.getTipo(),siafmInventarios.getEjercicio(),arregloParametros[0],"Ejercicio "+siafmInventarios.getEjercicio(),"7",crs.getString("catalogocuenta"));
                }  */          
                 resultado=registro.aplicaRegistroContable(conexionContabilidad,crs.getString("catalogocuenta"),Areas.INVENTARIO);
                 liPoliza=resultado.indexOf("***")+3;
                 lsPolizas=lsPolizas+resultado.substring(liPoliza,liPoliza+7)+"|";
                 System.out.println(lsPolizas);
                 hashMap.clear();
               } 
               else{
                 lsPolizas=lsPolizas+"SIN POLIZA|";                 
                 resultado="1,Sin poliza por hashMap vacio";
               }                     
               break;                                               
             case 11:  //Evento 11 de Inventarios parametros(fecha de envio, unidad ejecutora, entidad, ambito, referencia, numero, factura y rfc)     
               //siafmInventarios = new SiafmInventarios();
               siafmInventariosCOG = new SiafmInventariosCOG();
               // if ((arregloParametros[0].substring(6).equals("2010"))||(arregloParametros[0].substring(6).equals("2011")&&arregloParametros[0].substring(3,5).equals("01")))                              
                  // cadena=siafmInventarios.Forma24(conexionSistema,arregloParametros[0],arregloParametros[4],arregloParametros[1],arregloParametros[2],arregloParametros[3],crs.getString("catalogocuenta"), registro,arregloParametros[5],arregloParametros[6]);
               //else
                  cadena=siafmInventariosCOG.Forma24(conexionSistema,arregloParametros[0],arregloParametros[4],arregloParametros[1],arregloParametros[2],arregloParametros[3],crs.getString("catalogocuenta"), registro,arregloParametros[5],arregloParametros[6]);               
               if (!cadena.equals("")){
                 //cadena=Cadena.construyeCadena(hashMap);
                 preparaRegistro(cadena,claveSeguridadWS,arregloParametros[0],crs.getString("forma"),arregloParametros[1],arregloParametros[2],arregloParametros[3], arregloParametros[4],crs.getString("concepto"),crs.getString("catalogocuenta"),String.valueOf(eventoContable),ejercicio);
               //  liMes=Integer.parseInt(siafmInventarios.getMes())-1;
                 resultado=registro.aplicaRegistroContable(conexionContabilidad,crs.getString("catalogocuenta"),Areas.INVENTARIO);  
                 liPoliza=resultado.indexOf("***")+3;
                 lsPolizas=lsPolizas+resultado.substring(liPoliza,liPoliza+7)+"|";
                 System.out.println(lsPolizas);
                 hashMap.clear();
               } 
               else{
                 lsPolizas=lsPolizas+"SIN POLIZA|";                 
                 resultado="1,Sin poliza por hashMap vacio";
               }                     
               break;      
               
             case 13:  //Evento 13 de Inventarios parametros(fecha de envio, unidad ejecutora, entidad, ambito, n               
               //siafmInventarios = new SiafmInventarios();
               siafmInventariosCOG = new SiafmInventariosCOG();
               //if ((arregloParametros[0].substring(6).equals("2010"))||(arregloParametros[0].substring(6).equals("2011")&&arregloParametros[0].substring(3,5).equals("01")))                              
                   //cadena=siafmInventarios.Forma22A(conexionSistema,arregloParametros[0],arregloParametros[4],arregloParametros[1],arregloParametros[2],arregloParametros[3],crs.getString("catalogocuenta"),registro);
               //else
                   cadena=siafmInventariosCOG.Forma22A(conexionSistema,arregloParametros[0],arregloParametros[4],arregloParametros[1],arregloParametros[2],arregloParametros[3],crs.getString("catalogocuenta"),registro,arregloParametros[6]);
                 if (!cadena.equals("")){ 
                 //cadena=Cadena.construyeCadena(hashMap);
                 preparaRegistro(cadena,claveSeguridadWS,arregloParametros[0],crs.getString("forma"),arregloParametros[1],arregloParametros[2],arregloParametros[3], "BAJA SINIESTROS",arregloParametros[4],crs.getString("catalogocuenta"),String.valueOf(eventoContable),ejercicio);
                 resultado=registro.aplicaRegistroContable(conexionContabilidad,crs.getString("catalogocuenta"),Areas.INVENTARIO); 
                 liPoliza=resultado.indexOf("***")+3;
                 lsPolizas=lsPolizas+resultado.substring(liPoliza,liPoliza+7)+"|";
                 System.out.println(lsPolizas);
                 hashMap.clear();
               } 
               else{
                 lsPolizas=lsPolizas+"SIN POLIZA|";                 
                 resultado="1,Sin poliza por hashMap vacio";
               }                     
               break;                                                              
             case 14:  //Evento 14 de Inventarios parametros(fecha de envio, unidad ejecutora, entidad, ambito, nombre
               //siafmInventarios = new SiafmInventarios();
               siafmInventariosCOG = new SiafmInventariosCOG();
               //if ((arregloParametros[0].substring(6).equals("2010"))||(arregloParametros[0].substring(6).equals("2011")&&arregloParametros[0].substring(3,5).equals("01")))                                             
                  //cadena=siafmInventarios.Forma22B(conexionSistema,arregloParametros[0],arregloParametros[4],arregloParametros[1],arregloParametros[2],arregloParametros[3],crs.getString("catalogocuenta"),registro);
               //else   
                  cadena=siafmInventariosCOG.Forma22B(conexionSistema,arregloParametros[0],arregloParametros[4],arregloParametros[1],arregloParametros[2],arregloParametros[3],crs.getString("catalogocuenta"),registro);
               if (!cadena.equals("")){ 
                 //cadena=Cadena.construyeCadena(hashMap);
                 preparaRegistro(cadena,claveSeguridadWS,arregloParametros[0],crs.getString("forma"),arregloParametros[1],arregloParametros[2],arregloParametros[3], "BAJA PAGO NUMERARIO",arregloParametros[4],crs.getString("catalogocuenta"),String.valueOf(eventoContable),ejercicio);
                 resultado=registro.aplicaRegistroContable(conexionContabilidad,crs.getString("catalogocuenta"),Areas.INVENTARIO); 
                 liPoliza=resultado.indexOf("***")+3;
                 lsPolizas=lsPolizas+resultado.substring(liPoliza,liPoliza+7)+"|";
                 System.out.println(lsPolizas);
                 hashMap.clear();
               } 
               else{
                 lsPolizas=lsPolizas+"SIN POLIZA|";                 
                 resultado="1,Sin poliza por hashMap vacio";
               }                     
               break;                                                              
           case 15:  //Evento 15 de Inventarios parametros(fecha de envio, unidad ejecutora, entidad, ambito, nombre
              //siafmInventarios = new SiafmInventarios();
              siafmInventariosCOG = new SiafmInventariosCOG();
              //if ((arregloParametros[0].substring(6).equals("2010"))||(arregloParametros[0].substring(6).equals("2011")&&arregloParametros[0].substring(3,5).equals("01")))                                             
                 //cadena=siafmInventarios.Forma22(conexionSistema,arregloParametros[0],arregloParametros[4],arregloParametros[1],arregloParametros[2],arregloParametros[3], crs.getString("catalogocuenta"), registro);
              //else
                 cadena=siafmInventariosCOG.Forma22(conexionSistema,arregloParametros[0],arregloParametros[4],arregloParametros[1],arregloParametros[2],arregloParametros[3], crs.getString("catalogocuenta"), registro,arregloParametros[6]);
              if (!cadena.equals("")){ 
                //cadena=Cadena.construyeCadena(hashMap);
                preparaRegistro(cadena,claveSeguridadWS,arregloParametros[0],crs.getString("forma"),arregloParametros[1],arregloParametros[2],arregloParametros[3], siafmInventariosCOG.getRefGral(),arregloParametros[4],crs.getString("catalogocuenta"),String.valueOf(eventoContable),ejercicio);
                resultado=registro.aplicaRegistroContable(conexionContabilidad,crs.getString("catalogocuenta"),Areas.INVENTARIO); 
                liPoliza=resultado.indexOf("***")+3;
                lsPolizas=lsPolizas+resultado.substring(liPoliza,liPoliza+7)+"|";
                System.out.println(lsPolizas);
                hashMap.clear();
              } 
              else{
                lsPolizas=lsPolizas+"SIN POLIZA|";                 
                resultado="1,Sin poliza por hashMap vacio";
              }                     
             break;   
             case 16:  //Evento 16 de Inventarios parametros(fecha de envio, unidad ejecutora, entidad, ambito, referencia, numero, factura y rfc)     
               //siafmInventarios = new SiafmInventarios();
               siafmInventariosCOG = new SiafmInventariosCOG();
               cadena="";
               switch (Integer.parseInt(crs.getString("idforma"))){
                 case 30: { 
                              //if ((arregloParametros[0].substring(6).equals("2010"))||(arregloParametros[0].substring(6).equals("2011")&&arregloParametros[0].substring(3,5).equals("01")))                                             
                                //cadena=siafmInventarios.Forma11(conexionSistema,arregloParametros[0],arregloParametros[4],arregloParametros[1],arregloParametros[2],arregloParametros[3],crs.getString("catalogocuenta"),registro); //Forma RI
                              //else  
                                cadena=siafmInventariosCOG.Forma11(conexionSistema,arregloParametros[0],arregloParametros[4],arregloParametros[1],arregloParametros[2],arregloParametros[3],crs.getString("catalogocuenta"),registro,arregloParametros[6]); //Forma RI
                          } 
                 break;
                /* case 31:{
                     if ((arregloParametros[0].substring(6).equals("2010"))||(arregloParametros[0].substring(6).equals("2011")&&arregloParametros[0].substring(3,5).equals("01")))                                             
                        cadena=siafmInventarios.Forma14(conexionSistema,arregloParametros[0],arregloParametros[4],arregloParametros[1],arregloParametros[2],arregloParametros[3]);
                     else
                         cadena=siafmInventariosCOG.Forma14(conexionSistema,arregloParametros[0],arregloParametros[4],arregloParametros[1],arregloParametros[2],arregloParametros[3]);
                 }
                   break; //Forma FB 
                */   
               }
               if (!cadena.equals("")){ 
                 //cadena=Cadena.construyeCadena(hashMap);
                 preparaRegistro(cadena,claveSeguridadWS,arregloParametros[0],crs.getString("forma"),arregloParametros[1],arregloParametros[2],arregloParametros[3], "BAJA TRANSFERENCIA",arregloParametros[4],crs.getString("catalogocuenta"),String.valueOf(eventoContable),ejercicio);
                 resultado=registro.aplicaRegistroContable(conexionContabilidad,crs.getString("catalogocuenta"),Areas.INVENTARIO); 
                 liPoliza=resultado.indexOf("***")+3;
                 lsPolizas=lsPolizas+resultado.substring(liPoliza,liPoliza+7)+"|";
                 System.out.println(lsPolizas);
                 hashMap.clear();
               } 
               else{
                 lsPolizas=lsPolizas+"SIN POLIZA|";                 
                 resultado="1,Sin poliza por hashMap vacio";
               }                     
               break;                                  
             case 17:  //Evento 17 de Inventarios parametros(fecha de envio, unidad ejecutora, entidad, ambito, nombre
               //siafmInventarios = new SiafmInventarios();
               siafmInventariosCOG = new SiafmInventariosCOG();
               //if ((arregloParametros[0].substring(6).equals("2010"))||(arregloParametros[0].substring(6).equals("2011")&&arregloParametros[0].substring(3,5).equals("01")))                                             
                  // cadena=siafmInventarios.Forma23(conexionSistema,arregloParametros[0],arregloParametros[4],arregloParametros[1],arregloParametros[2],arregloParametros[3],crs.getString("catalogocuenta"),registro);
               //else   
                   cadena=siafmInventariosCOG.Forma23(conexionSistema,arregloParametros[0],arregloParametros[4],arregloParametros[1],arregloParametros[2],arregloParametros[3],crs.getString("catalogocuenta"),registro,arregloParametros[6]);
               if (!cadena.equals("")){
                 //cadena=Cadena.construyeCadena(hashMap);
                 preparaRegistro(cadena,claveSeguridadWS,arregloParametros[0],crs.getString("forma"),arregloParametros[1],arregloParametros[2],arregloParametros[3],"BAJA COMODATO",arregloParametros[4],crs.getString("catalogocuenta"),String.valueOf(eventoContable),ejercicio);
                 resultado=registro.aplicaRegistroContable(conexionContabilidad,crs.getString("catalogocuenta"),Areas.INVENTARIO); 
                 liPoliza=resultado.indexOf("***")+3;
                 lsPolizas=lsPolizas+resultado.substring(liPoliza,liPoliza+7)+"|";
                 System.out.println(lsPolizas);
                 hashMap.clear();
               } 
               else{
                 lsPolizas=lsPolizas+"SIN POLIZA|";                 
                 resultado="1,Sin poliza por hashMap vacio";
               }                     
               break;                                                                             
             case 18:  //Evento 18 de Inventarios parametros(fecha de envio, unidad ejecutora, entidad, ambito, nombre
               //siafmInventarios = new SiafmInventarios();
               siafmInventariosCOG = new SiafmInventariosCOG();
               //if ((arregloParametros[0].substring(6).equals("2010"))||(arregloParametros[0].substring(6).equals("2011")&&arregloParametros[0].substring(3,5).equals("01")))                                             
                  //cadena=siafmInventarios.Forma17(conexionSistema,arregloParametros[0],arregloParametros[4],arregloParametros[1],arregloParametros[2],arregloParametros[3],crs.getString("catalogocuenta"),registro);
              //else
                  cadena=siafmInventariosCOG.Forma17(conexionSistema,arregloParametros[0],arregloParametros[4],arregloParametros[1],arregloParametros[2],arregloParametros[3],crs.getString("catalogocuenta"),registro,arregloParametros[6]);
               if (!cadena.equals("")){
                 //cadena=Cadena.construyeCadena(hashMap);
                 preparaRegistro(cadena,claveSeguridadWS,arregloParametros[0],crs.getString("forma"),arregloParametros[1],arregloParametros[2],arregloParametros[3],"BAJA DONACION",arregloParametros[4],crs.getString("catalogocuenta"),String.valueOf(eventoContable),ejercicio);
                // liMes=Integer.parseInt(siafmInventarios.getMes())-1;
                 resultado=registro.aplicaRegistroContable(conexionContabilidad,crs.getString("catalogocuenta"),Areas.INVENTARIO);
                 liPoliza=resultado.indexOf("***")+3;
                 lsPolizas=lsPolizas+resultado.substring(liPoliza,liPoliza+7)+"|";
                 System.out.println(lsPolizas);
                 hashMap.clear();
               } 
               else{
                 lsPolizas=lsPolizas+"SIN POLIZA|";                 
                 resultado="1,Sin poliza por hashMap vacio";
               }                     
               break;                                
              
             case 19:  //Evento 19 de Inventarios parametros(fecha de envio, unidad ejecutora, entidad, ambito, referencia, numero, factura y rfc)     
               //siafmInventarios = new SiafmInventarios();
               siafmInventariosCOG = new SiafmInventariosCOG();
               String refGeneral19="";
               switch (Integer.parseInt(crs.getString("idforma"))){
                 case 34: {
                              //if ((arregloParametros[0].substring(6).equals("2010"))||(arregloParametros[0].substring(6).equals("2011")&&arregloParametros[0].substring(3,5).equals("01")))                                                             
                                  //cadena=siafmInventarios.Forma10(conexionSistema,arregloParametros[0],arregloParametros[4],arregloParametros[5],arregloParametros[6],arregloParametros[1],arregloParametros[2],arregloParametros[3],crs.getString("catalogocuenta"),registro); //Forma MI
                              //else
                                  // System.out.println(" aaaForma10 ");
                                  cadena=siafmInventariosCOG.Forma10(conexionSistema,arregloParametros[0],arregloParametros[4],arregloParametros[5],arregloParametros[6],arregloParametros[1],arregloParametros[2],arregloParametros[3],crs.getString("catalogocuenta"),registro); //Forma MI
                                  // System.out.println(" dddForma10 "+cadena);
                            refGeneral19 = "A";
                          } 
                 break;
                 case 35: {
                              //if ((arregloParametros[0].substring(6).equals("2010"))||(arregloParametros[0].substring(6).equals("2011")&&arregloParametros[0].substring(3,5).equals("01")))                                                                              
                              //   cadena=siafmInventarios.Forma8(conexionSistema,arregloParametros[0],arregloParametros[4],arregloParametros[5],arregloParametros[6],arregloParametros[1],arregloParametros[2],arregloParametros[3],crs.getString("catalogocuenta"),registro); //Forma RI
                              //else   
                                 // System.out.println(" aaaForma18 ");
                                 cadena=siafmInventariosCOG.Forma8(conexionSistema,arregloParametros[0],arregloParametros[4],arregloParametros[5],arregloParametros[6],arregloParametros[1],arregloParametros[2],arregloParametros[3],crs.getString("catalogocuenta"),registro); //Forma RI
                                 // System.out.println(" dddForma18 "+cadena);
                            refGeneral19 = "B";
                          } 
                 break;
                 case 36: { 
                             // if ((arregloParametros[0].substring(6).equals("2010"))||(arregloParametros[0].substring(6).equals("2011")&&arregloParametros[0].substring(3,5).equals("01")))                                                                                               
                               //  cadena=siafmInventarios.Forma9(conexionSistema,arregloParametros[0],arregloParametros[4],arregloParametros[5],arregloParametros[6],arregloParametros[1],arregloParametros[2],arregloParametros[3],crs.getString("catalogocuenta"),registro); //Forma CR
                              //else   
                                 // System.out.println(" aaaForma9 ");
                                 cadena=siafmInventariosCOG.Forma9(conexionSistema,arregloParametros[0],arregloParametros[4],arregloParametros[5],arregloParametros[6],arregloParametros[1],arregloParametros[2],arregloParametros[3],crs.getString("catalogocuenta"),registro); //Forma CR
                                 // System.out.println(" dddForma9 "+cadena);  
                          } 
                   break;
                 
               }
               if (!cadena.equals("")){
                 //cadena=Cadena.construyeCadena(hashMa
                 // System.out.println(" aaapreparaRegistro ");
                 if(crs.getString("idforma").equals("35")){
                     //if ((arregloParametros[0].substring(6).equals("2010"))||(arregloParametros[0].substring(6).equals("2011")&&arregloParametros[0].substring(3,5).equals("01")))                                                                                               
                       //  preparaRegistro(cadena,claveSeguridadWS,arregloParametros[0],crs.getString("forma"),siafmInventarios.getUnidad() ,siafmInventarios.getEntidad(),siafmInventarios.getAmbito(), arregloParametros[4] + " " + refGeneral19,crs.getString("concepto"),crs.getString("catalogocuenta"),String.valueOf(eventoContable),ejercicio);
                     //else
                         preparaRegistro(cadena,claveSeguridadWS,arregloParametros[0],crs.getString("forma"),siafmInventariosCOG.getUnidad() ,siafmInventariosCOG.getEntidad(),siafmInventariosCOG.getAmbito(), arregloParametros[4] + " " + refGeneral19,crs.getString("concepto"),crs.getString("catalogocuenta"),String.valueOf(eventoContable),ejercicio);
                 }
                 else{
                   if(crs.getString("idforma").equals("36")){
                      // if ((arregloParametros[0].substring(6).equals("2010"))||(arregloParametros[0].substring(6).equals("2011")&&arregloParametros[0].substring(3,5).equals("01")))                                                                                               
                      //    preparaRegistro(cadena,claveSeguridadWS,arregloParametros[0],crs.getString("forma"),siafmInventarios.getUnidad() ,siafmInventarios.getEntidad(),siafmInventarios.getAmbito(), arregloParametros[4] + " " + refGeneral19,crs.getString("concepto"),crs.getString("catalogocuenta"),String.valueOf(eventoContable),ejercicio);
                       //else   
                          preparaRegistro(cadena,claveSeguridadWS,arregloParametros[0],crs.getString("forma"),siafmInventariosCOG.getUnidad() ,siafmInventariosCOG.getEntidad(),siafmInventariosCOG.getAmbito(), arregloParametros[4] + " " + refGeneral19,crs.getString("concepto"),crs.getString("catalogocuenta"),String.valueOf(eventoContable),ejercicio);
                   } 
                   else{                      
                         preparaRegistro(cadena,claveSeguridadWS,arregloParametros[0],crs.getString("forma"),arregloParametros[1],arregloParametros[2],arregloParametros[3], arregloParametros[4] + " " + refGeneral19,crs.getString("concepto"),crs.getString("catalogocuenta"),String.valueOf(eventoContable),ejercicio);
                   }  
                 }
                   // System.out.println(" dddpreparaRegistro ");
                   // System.out.println(" aaaAplicaRegistro ");
                 resultado=registro.aplicaRegistroContable(conexionContabilidad,crs.getString("catalogocuenta"),Areas.INVENTARIO); 
                   // System.out.println(" dddAplicaRegistro ");
                 liPoliza=resultado.indexOf("***")+3;
                 lsPolizas=lsPolizas+resultado.substring(liPoliza,liPoliza+7)+"|";
                 System.out.println(lsPolizas);
                 hashMap.clear();
               } 
               else{
                 lsPolizas=lsPolizas+"SIN POLIZA|";                 
                 resultado="1,Sin poliza por hashMap vacio";
               }                     
               break; 
           case 20:  //Evento 20 de Inventarios parametros(fecha, unidad, entidad, ambito, factura, numero, factura);
             //siafmInventarios = new SiafmInventarios();
             siafmInventariosCOG = new SiafmInventariosCOG();
             //if ((arregloParametros[0].substring(6).equals("2010"))||(arregloParametros[0].substring(6).equals("2011")&&arregloParametros[0].substring(3,5).equals("01")))                                                                                               
                //cadena=siafmInventarios.Forma25(conexionSistema,arregloParametros[0],arregloParametros[4],arregloParametros[1],arregloParametros[2],arregloParametros[3],crs.getString("catalogocuenta"),registro,arregloParametros[5],arregloParametros[6]);
             //else   
                cadena=siafmInventariosCOG.Forma25(conexionSistema,arregloParametros[0],arregloParametros[4],arregloParametros[1],arregloParametros[2],arregloParametros[3],crs.getString("catalogocuenta"),registro,arregloParametros[5],arregloParametros[6]);
             if (!cadena.equals("")){
               //cadena=Cadena.construyeCadena(hashMap);
               preparaRegistro(cadena,claveSeguridadWS,arregloParametros[0],crs.getString("forma"),arregloParametros[1],arregloParametros[2],arregloParametros[3],"RECLASIFICACION VALOR",crs.getString("concepto"),crs.getString("catalogocuenta"),String.valueOf(eventoContable),ejercicio);
               resultado=registro.aplicaRegistroContable(conexionContabilidad,crs.getString("catalogocuenta"),Areas.INVENTARIO);
               liPoliza=resultado.indexOf("***")+3;
               lsPolizas=lsPolizas+resultado.substring(liPoliza,liPoliza+7)+"|";
               System.out.println(lsPolizas);
               hashMap.clear();
             } 
             else{
               lsPolizas=lsPolizas+"SIN POLIZA|";                 
               resultado="1,Sin poliza por hashMap vacio";
             }                     
             break;  
           case 21:  //Evento 21 de Inventarios parametros(fecha, unidad, entidad, ambito, factura, numero, tipo ajuste);
             //siafmInventarios = new SiafmInventarios();
             siafmInventariosCOG = new SiafmInventariosCOG();
             //if ((arregloParametros[0].substring(6).equals("2010"))||(arregloParametros[0].substring(6).equals("2011")&&arregloParametros[0].substring(3,5).equals("01")))                                                                                               
               // cadena=siafmInventarios.Forma26(conexionSistema,arregloParametros[0],arregloParametros[4],arregloParametros[1],arregloParametros[2],arregloParametros[3],crs.getString("catalogocuenta"),registro,arregloParametros[5],arregloParametros[6]);
            //else
                cadena=siafmInventariosCOG.Forma26(conexionSistema,arregloParametros[0],arregloParametros[4],arregloParametros[1],arregloParametros[2],arregloParametros[3],crs.getString("catalogocuenta"),registro,arregloParametros[5],arregloParametros[6]);
             if (!cadena.equals("")){
               //cadena=Cadena.construyeCadena(hashMap);
               preparaRegistro(cadena,claveSeguridadWS,arregloParametros[0],crs.getString("forma"),arregloParametros[1],arregloParametros[2],arregloParametros[3], "RECLASIFICACION VALOR", crs.getString("concepto"),crs.getString("catalogocuenta"),String.valueOf(eventoContable),ejercicio); 
               resultado=registro.aplicaRegistroContable(conexionContabilidad,crs.getString("catalogocuenta"),Areas.INVENTARIO);
               liPoliza=resultado.indexOf("***")+3;
               lsPolizas=lsPolizas+resultado.substring(liPoliza,liPoliza+7)+"|";
               System.out.println(lsPolizas);
               hashMap.clear();
             } 
             else{
               lsPolizas=lsPolizas+"SIN POLIZA|";                 
               resultado="1,Sin poliza por hashMap vacio";
             }                     
             break;  
           case 22:  //Evento 22 de Inventarios parametros(fecha, unidad, entidad, ambito, factura, numero, factura);
             //siafmInventarios = new SiafmInventarios();
             siafmInventariosCOG = new SiafmInventariosCOG();
             String referencia22 =" RECLASIFICACION ";
             if (arregloParametros[7].equals("2"))
               referencia22=referencia22+" AÑO - "+arregloParametros[4]+" "+arregloParametros[5]+arregloParametros[6];
             else    
             if (arregloParametros[7].equals("3"))
               referencia22=referencia22+" PARTIDA - "+arregloParametros[4]+" "+arregloParametros[5]+arregloParametros[6];
             else
             if (arregloParametros[7].equals("4"))
               referencia22=referencia22+" VALOR - "+arregloParametros[4]+" "+arregloParametros[5]+arregloParametros[6];
             //if ((arregloParametros[0].substring(6).equals("2010"))||(arregloParametros[0].substring(6).equals("2011")&&arregloParametros[0].substring(3,5).equals("01")))                                                                                               
             //   cadena=siafmInventarios.Forma27(conexionSistema,arregloParametros[0],arregloParametros[4],arregloParametros[1],arregloParametros[2],arregloParametros[3],crs.getString("catalogocuenta"),registro,arregloParametros[5],arregloParametros[6]);
            //else
                cadena=siafmInventariosCOG.Forma27(conexionSistema,arregloParametros[0],arregloParametros[4],arregloParametros[1],arregloParametros[2],arregloParametros[3],crs.getString("catalogocuenta"),registro,arregloParametros[5],arregloParametros[6],arregloParametros[7]);
             if (!cadena.equals("")){   
               preparaRegistro(cadena,claveSeguridadWS,arregloParametros[0],crs.getString("forma"),arregloParametros[1],arregloParametros[2],arregloParametros[3],referencia22,crs.getString("concepto"),crs.getString("catalogocuenta"),String.valueOf(eventoContable),ejercicio);             
             //    liMes=Integer.parseInt(siafmInventarios.getMes())-1;
               resultado=registro.aplicaRegistroContable(conexionContabilidad,crs.getString("catalogocuenta"),Areas.MIPF_NO_ACUMULADO);
               liPoliza=resultado.indexOf("***")+3;
               lsPolizas=lsPolizas+resultado.substring(liPoliza,liPoliza+7)+"|";
               System.out.println(lsPolizas);
               hashMap.clear();
             } 
             else{
               lsPolizas=lsPolizas+"SIN POLIZA|";                 
               resultado="1,Sin poliza por hashMap vacio";
             }                     
             break;  
             case 23:  //Evento 23 de Inventarios parametros(fecha de envio, unidad ejecutora, entidad, ambito, nombre
               // siafmInventarios = new SiafmInventarios();
                siafmInventariosCOG = new SiafmInventariosCOG();
                //if ((arregloParametros[0].substring(6).equals("2010"))||(arregloParametros[0].substring(6).equals("2011")&&arregloParametros[0].substring(3,5).equals("01")))                                                                                               
                //   cadena=siafmInventarios.Forma22C(conexionSistema,arregloParametros[0],arregloParametros[4],arregloParametros[1],arregloParametros[2],arregloParametros[3], crs.getString("catalogocuenta"), registro);
                //else   
                   cadena=siafmInventariosCOG.Forma22C(conexionSistema,arregloParametros[0],arregloParametros[4],arregloParametros[1],arregloParametros[2],arregloParametros[3], crs.getString("catalogocuenta"), registro);
                if (!cadena.equals("")){ 
                  //cadena=Cadena.construyeCadena(hashMap);
                  preparaRegistro(cadena,claveSeguridadWS,arregloParametros[0],crs.getString("forma"),arregloParametros[1],arregloParametros[2],arregloParametros[3], "BAJA POR REPOSICION",arregloParametros[4],crs.getString("catalogocuenta"),String.valueOf(eventoContable),ejercicio);
                  resultado=registro.aplicaRegistroContable(conexionContabilidad,crs.getString("catalogocuenta"),Areas.INVENTARIO); 
                  liPoliza=resultado.indexOf("***")+3;
                  lsPolizas=lsPolizas+resultado.substring(liPoliza,liPoliza+7)+"|";
                  System.out.println(lsPolizas);
                  hashMap.clear();
                } 
                else{
                  lsPolizas=lsPolizas+"SIN POLIZA|";                 
                  resultado="1,Sin poliza por hashMap vacio";
                }                     
               break;                                                                                          
           default:
              return "0, No existe el evento contable: " + eventoContable + " para ser procesado.";
         }//FIN WHILE   
      }//TRY
      
      if (resultado.substring(0,1).equals("1")){
        cuentaContable = new bcCuentaContable();
        bitacoraLocal.update_BITACORALOCAL_estatus(conexionSistema,eventoContable,eventoProceso,"1",lsPolizas);                       
        resultado="1,El evento contable " + eventoContable+ " con numero de proceso " + eventoProceso + ", fue procesado correctamente y genero las siguientes polizas [" + lsPolizas+"]" ;
        if(eventoContable==300)
          cuentaContable.update_rf_tr_cuentas_contables_acumulados_eli(conexionContabilidad,ejercicio,"1","11");
        conexionContabilidad.commit();
        conexionSistema.commit();
        
      }
      else{
        throw new Exception(resultado);  
      }
      System.out.println(resultado);  
      return resultado;
    }catch(Exception e){
        conexionContabilidad.rollback();
        conexionSistema.rollback();
        System.out.println("Ocurrio un error al accesar al metodo aplicaRegistroContableInventario "+e.getMessage()); 
        throw e;
    }
    finally{
        if (crs!=null){
          crs.close();
          crs=null;
        }    
        if(conexionSistema!=null){
          conexionSistema.close();
          conexionSistema = null;
        }
        if(conexionContabilidad!=null){
          conexionContabilidad.close();
          conexionContabilidad = null;
        }
    }

  }
} 