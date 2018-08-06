package sia.rf.contabilidad.registroContableEvento;

import sia.db.dao.DaoFactory;
import java.sql.Connection;
import java.sql.SQLException;   
import java.util.HashMap;

import sia.libs.correo.Envio;

import sia.libs.recurso.Contabilidad;

import sia.rf.contabilidad.registroContable.formas.Areas;

import sia.rf.contabilidad.registroContableNuevo.bcCuentasBancarias;

import sia.ws.publicar.contabilidad.Registro;
import sun.jdbc.rowset.CachedRowSet;

import sia.rf.contabilidad.sistemas.siga.SiafmSiga;

import sia.rf.contabilidad.registroContableNuevo.bcCuentaContable;


/**
 *
 * @author luz.lopez
 */
public class EventoSiga {

  private EventoContable eventoContable=null;
  private FormaContable formaContable=null;
  private Modulo modulo = null;
  private Sistema sistema=null;
  private Registro registro=null;
  private BitacoraLocal bitacoraLocal=null;
  private CachedRowSet crs=null;
  private int banAre=0;
  private String origen="";
  
  
  
  public EventoSiga() {
    eventoContable = new EventoContable();
    formaContable = new FormaContable();
    modulo = new Modulo();
    sistema = new Sistema();
    registro = new Registro();
    bitacoraLocal = new BitacoraLocal();
  }

  private void enviaNotitificacion(String asunto,String mensaje){
    StringBuffer sb= new StringBuffer();
    sb.append("<html><title><head></head></title><body>");
    sb.append("<br><strong>");
    sb.append(asunto);
    sb.append("</strong><br>");
    sb.append("<br><strong>");
    sb.append(mensaje);
    sb.append("</strong><br>");
    sb.append("<br>");
    Envio.asuntoMensaje( Contabilidad.getPropiedad("notificacion.webService.emisor") ,  Contabilidad.getPropiedad("notificacion.webService.destinatarios.aviso")  ,null  ,"webService Contabilidad ",sb.toString(),null,true);
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
  
  private String obtieneAmbitoCuenta(Connection conexionContabilidad,String cuenta, bcCuentasBancarias bcCuenta) throws SQLException, Exception {
      bcCuenta.select_rf_tesoreria_rf_tr_cuentas_bancarias_tipo(conexionContabilidad,cuenta,"2"); //verifica si es central
      return bcCuenta.getAmbito_cuenta();
  }
   
  public String procesarEvento(int eventoContable, int eventoProceso, String parametros, String claveSeguridadWS) throws SQLException, Exception{  
    String nombreDS=null;
    Connection conexionSistema=null;
    Connection conexionContabilidad=null;
    String cadena=null;
    String resultado=null;
    String [] arregloParametros = parametros.split("&");
    HashMap hashMap = new HashMap(); 
    
    bcCuentaContable cuentaContable = null;
    SiafmSiga siafmSiga = null;
    int liPoliza=0;
    String lsPolizas="|";
    String ejercicio = null;
    try{ 
      nombreDS=obtenerNombreDatasource(Integer.toString(eventoContable)); 
      conexionSistema=obtenerConexionModulo(nombreDS);
      conexionSistema.setAutoCommit(false);
      conexionContabilidad=DaoFactory.getContabilidad();
      conexionContabilidad.setAutoCommit(false);
      ejercicio = arregloParametros[0].substring(6);    
      while (crs.next()){     
        System.out.println("Procesando Evento SIGA: "+eventoContable+" con forma: "+crs.getString("forma"));
         switch (eventoContable){
         
/*Por numero de folio y movimiento contable 2013 Para subirse a produccion*/
     case 81:  //Evento 81 de Siga parametros(fecha de envio, unidad ejecutora, entidad, ambito)    
     if(  crs.getString("movimiento").equals(arregloParametros[7])){
         
           siafmSiga = new SiafmSiga();
           String fecha = arregloParametros[0].substring(6,8).concat("/").concat(arregloParametros[0].substring(4,6)).concat("/").concat(arregloParametros[0].substring(0,4));
           cadena=siafmSiga.general2013(conexionSistema,arregloParametros[1],arregloParametros[2],arregloParametros[7],arregloParametros[3], arregloParametros[4],arregloParametros[5],arregloParametros[6]);
           //cadena="";
           // System.out.println("Resultado de la cadena: "+cadena); 
            if (!cadena.equals("")){                                 //arregloParametros[0].substring(6,8)                   //unidad-ejecut         //entidad               //ambito
               preparaRegistro(cadena,claveSeguridadWS,arregloParametros[0],crs.getString("forma"),arregloParametros[3].substring(0,3),arregloParametros[4], arregloParametros[5],"SIGA. Forma "+crs.getString("forma")+" "+arregloParametros[0]+" "+arregloParametros[4]+" "+ arregloParametros[5]+" Folio Siga: "+arregloParametros[6]+" "+crs.getString("referencia"),crs.getString("concepto"),crs.getString("catalogocuenta"),String.valueOf(eventoContable),ejercicio);
               resultado=registro.aplicaRegistroContable(conexionContabilidad,crs.getString("catalogocuenta"),Areas.MANUAL);
               liPoliza=resultado.indexOf("***")+3;
               lsPolizas=lsPolizas+resultado.substring(liPoliza,liPoliza+7)+"|";
               System.out.println(lsPolizas);
               hashMap.clear();
             }
             else{
                 lsPolizas=lsPolizas+"SIN POLIZA|";      
                 resultado="1,Sin poliza por hashMap vacio";
             }
         
         }
         break;
         
/*por unidad ejecutora-entidad-ambito, todos los eventos Sin folio Siga*/            
/*        case 81:  //Evento 81 de Siga parametros(fecha de envio, unidad ejecutora, entidad, ambito)
      //if(   crs.getString("forma").equals("XH")||crs.getString("forma").equals("XI")||crs.getString("forma").equals("XJ")||crs.getString("forma").equals("XK")|crs.getString("forma").equals("XL")){
             siafmSiga = new SiafmSiga();
             String fecha = arregloParametros[0].substring(6,8).concat("/").concat(arregloParametros[0].substring(4,6)).concat("/").concat(arregloParametros[0].substring(0,4));
             cadena=siafmSiga.generalAnt(conexionSistema,arregloParametros[1],arregloParametros[2],crs.getString("movimiento"),arregloParametros[3], arregloParametros[4],arregloParametros[5]);
             
             //cadena="";
              if (!cadena.equals("")){                                 //arregloParametros[0].substring(6,8)                   //unidad-ejecut         //entidad               //ambito
                 preparaRegistro(cadena,claveSeguridadWS,arregloParametros[0],crs.getString("forma"),arregloParametros[3].substring(0,3),arregloParametros[4], arregloParametros[5],"SIGA. Forma "+crs.getString("forma")+" "+arregloParametros[0]+" "+arregloParametros[4]+ arregloParametros[5]+arregloParametros[6]+crs.getString("referencia"),crs.getString("concepto"),crs.getString("catalogocuenta"),String.valueOf(eventoContable),ejercicio);
                 resultado=registro.aplicaRegistroContable(conexionContabilidad,crs.getString("catalogocuenta"),Areas.MANUAL);
                 liPoliza=resultado.indexOf("***")+3;
                 lsPolizas=lsPolizas+resultado.substring(liPoliza,liPoliza+7)+"|";
                 System.out.println(lsPolizas);
                 hashMap.clear();
               }
               else{
                   lsPolizas=lsPolizas+"SIN POLIZA|";      
                 resultado="1,Sin poliza por hashMap vacio";
               }
       //      } 

             break; 
*/               
             /*volumen*/
            /*      case 81:  //Evento 81 de Siga parametros(fecha de envio, unidad ejecutora, entidad, ambito)    
                        siafmSiga = new SiafmSiga();
                        CachedRowSet crsRegistros = null;
                        String fecha = arregloParametros[0].substring(6,8).concat("/").concat(arregloParametros[0].substring(4,6)).concat("/").concat(arregloParametros[0].substring(0,4));
                        crsRegistros = siafmSiga.getUnidadEntidadAmbito(conexionSistema,arregloParametros[1],arregloParametros[2]);
                        while(crsRegistros.next()){
                         cadena=siafmSiga.general(conexionSistema,arregloParametros[1],arregloParametros[2],crs.getString("movimiento"),crsRegistros.getString("UNIDAD"), crsRegistros.getString("ENTIDAD"),crsRegistros.getString("AMBITO"));
                         //cadena="";
                         if (!cadena.equals("")){                                 //arregloParametros[0].substring(6,8)                   //unidad-ejecut         //entidad               //ambito
                            preparaRegistro(cadena,claveSeguridadWS,arregloParametros[0],crs.getString("forma"),crsRegistros.getString("UNIDAD_EJECUTORA"),crsRegistros.getString("ENTIDAD"), crsRegistros.getString("AMBITO"),"SIGA. Forma "+crs.getString("forma")+" "+arregloParametros[0]+" "+arregloParametros[4]+ arregloParametros[5]+arregloParametros[6]+crs.getString("referencia"),crs.getString("concepto"),crs.getString("catalogocuenta"),String.valueOf(eventoContable),ejercicio);
                            resultado=registro.aplicaRegistroContable(conexionContabilidad,crs.getString("catalogocuenta"),Areas.MANUAL);
                            liPoliza=resultado.indexOf("***")+3;
                            lsPolizas=lsPolizas+resultado.substring(liPoliza,liPoliza+7)+"|";
                            System.out.println(lsPolizas);
                            hashMap.clear();
                          }
                          else{
                            //lsPolizas=lsPolizas+"SIN POLIZA|";                
                            resultado="1,Sin poliza por hashMap vacio";
                          }
                        }
                        break;*/
        /*case 81:  //Evento 81 de Siga parametros(fecha de envio, unidad ejecutora, entidad, ambito)    
              siafmSiga = new SiafmSiga();
                cadena=siafmSiga.general(conexionSistema,arregloParametros[1],arregloParametros[2],crs.getString("movimiento"),arregloParametros[3],arregloParametros[4], arregloParametros[5]);
                if (!cadena.equals("")){                                 //arregloParametros[0].substring(6,8)                   //unidad-ejecut         //entidad               //ambito
                  preparaRegistro(cadena,claveSeguridadWS,arregloParametros[0],crs.getString("forma"),arregloParametros[3].substring(0,3),arregloParametros[4], arregloParametros[5],"SIGA. Forma "+crs.getString("forma")+" "+arregloParametros[0]+" "+arregloParametros[4]+ arregloParametros[5]+arregloParametros[6]+crs.getString("referencia"),crs.getString("concepto"),crs.getString("catalogocuenta"),String.valueOf(eventoContable),ejercicio);
                  resultado=registro.aplicaRegistroContable(conexionContabilidad,crs.getString("catalogocuenta"),Areas.MANUAL);
                  liPoliza=resultado.indexOf("***")+3;
                  lsPolizas=lsPolizas+resultado.substring(liPoliza,liPoliza+7)+"|";
                  System.out.println(lsPolizas);
                  hashMap.clear();
                }
                else{
                  //lsPolizas=lsPolizas+"SIN POLIZA|";                
                  resultado="1,Sin poliza por hashMap vacio";
                }      
              break;*/ 
              
             
             case 82:  //Evento 82 de Siga parametros(fecha de envio, unidad ejecutora, entidad, ambito)     
               siafmSiga = new SiafmSiga();
               hashMap=siafmSiga.Forma2(conexionSistema,arregloParametros[0],arregloParametros[1],arregloParametros[2],arregloParametros[3]);
               if ((hashMap != null) && (!hashMap.isEmpty())){ 
                 cadena=Cadena.construyeCadena(hashMap);
             //                 String cadena, String claveSeguridadWS, String fechaEnvio,String forma, String unidadEjecutora,               String entidad,   String ambito,    String referencia,    String concepto, String catalogoCuenta
                 preparaRegistro(cadena,claveSeguridadWS,arregloParametros[0],crs.getString("forma"),arregloParametros[1].substring(0,3),siafmSiga.getEntidad(),siafmSiga.getAmbito().substring(3,4),crs.getString("referencia"),crs.getString("concepto"),crs.getString("catalogocuenta"),String.valueOf(eventoContable),ejercicio);
                 //liMes=Integer.parseInt(siafmInventarios.getMes())-1;
                 resultado=registro.aplicaRegistroContable(conexionContabilidad,crs.getString("catalogocuenta"),Areas.MANUAL);
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
           case 83:  //Evento 83 de Siga parametros(fecha de envio, unidad ejecutora, entidad, ambito)     
             siafmSiga = new SiafmSiga();
             hashMap=siafmSiga.Forma3(conexionSistema,arregloParametros[0],arregloParametros[1],arregloParametros[2],arregloParametros[3]);
             if ((hashMap != null) && (!hashMap.isEmpty())){ 
               cadena=Cadena.construyeCadena(hashMap);
//                 String cadena, String claveSeguridadWS, String fechaEnvio,String forma, String unidadEjecutora,               String entidad,   String ambito,    String referencia,    String concepto, String catalogoCuenta
               preparaRegistro(cadena,claveSeguridadWS,arregloParametros[0],crs.getString("forma"),arregloParametros[1].substring(0,3),siafmSiga.getEntidad(),siafmSiga.getAmbito().substring(3,4),crs.getString("referencia"),crs.getString("concepto"),crs.getString("catalogocuenta"),String.valueOf(eventoContable),ejercicio);
               //liMes=Integer.parseInt(siafmInventarios.getMes())-1;
               resultado=registro.aplicaRegistroContable(conexionContabilidad,crs.getString("catalogocuenta"),Areas.MANUAL);
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
             case 85:  //Evento 85 de Siga parametros(fecha de envio, unidad ejecutora, entidad, ambito)     
             siafmSiga = new SiafmSiga();
             hashMap=siafmSiga.Forma5(conexionSistema,arregloParametros[0],arregloParametros[1],arregloParametros[2],arregloParametros[3]);
               if ((hashMap != null) && (!hashMap.isEmpty())){ 
                 cadena=Cadena.construyeCadena(hashMap);
             //                 String cadena, String claveSeguridadWS, String fechaEnvio,String forma, String unidadEjecutora,               String entidad,   String ambito,    String referencia,    String concepto, String catalogoCuenta
                 preparaRegistro(cadena,claveSeguridadWS,arregloParametros[0],crs.getString("forma"),arregloParametros[1].substring(0,3),siafmSiga.getEntidad(),siafmSiga.getAmbito().substring(3,4),crs.getString("referencia"),crs.getString("concepto"),crs.getString("catalogocuenta"),String.valueOf(eventoContable),ejercicio);
                 //liMes=Integer.parseInt(siafmInventarios.getMes())-1;
                 resultado=registro.aplicaRegistroContable(conexionContabilidad,crs.getString("catalogocuenta"),Areas.MANUAL);
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
             case 88:  //Evento 88 de Siga parametros(fecha de envio, unidad ejecutora, entidad, ambito)     
               siafmSiga = new SiafmSiga();
               hashMap=siafmSiga.Forma8(conexionSistema,arregloParametros[0],arregloParametros[1],arregloParametros[2],arregloParametros[3]);
               if ((hashMap != null) && (!hashMap.isEmpty())){ 
                 cadena=Cadena.construyeCadena(hashMap);
                 preparaRegistro(cadena,claveSeguridadWS,arregloParametros[0],crs.getString("forma"),arregloParametros[1].substring(0,3),siafmSiga.getEntidad(),siafmSiga.getAmbito().substring(3,4),crs.getString("referencia"),crs.getString("concepto"),crs.getString("catalogocuenta"),String.valueOf(eventoContable),ejercicio);
                 resultado=registro.aplicaRegistroContable(conexionContabilidad,crs.getString("catalogocuenta"),Areas.MANUAL);
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
             case 89:  //Evento 89 de Siga parametros(fecha de envio, unidad ejecutora, entidad, ambito)     
               siafmSiga = new SiafmSiga();
               hashMap=siafmSiga.Forma9(conexionSistema,arregloParametros[0],arregloParametros[1],arregloParametros[2],arregloParametros[3]);
               if ((hashMap != null) && (!hashMap.isEmpty())){ 
                 cadena=Cadena.construyeCadena(hashMap);
                 preparaRegistro(cadena,claveSeguridadWS,arregloParametros[0],crs.getString("forma"),arregloParametros[1].substring(0,3),siafmSiga.getEntidad(),siafmSiga.getAmbito().substring(3,4),crs.getString("referencia"),crs.getString("concepto"),crs.getString("catalogocuenta"),String.valueOf(eventoContable),ejercicio);
                 resultado=registro.aplicaRegistroContable(conexionContabilidad,crs.getString("catalogocuenta"),Areas.MANUAL);
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
             case 90:  //Evento 90 de Siga parametros(fecha de envio, unidad ejecutora, entidad, ambito)     
               siafmSiga = new SiafmSiga();
               hashMap.clear();
               hashMap=siafmSiga.Forma10(conexionSistema,arregloParametros[0],arregloParametros[1],arregloParametros[2],arregloParametros[3]);
               if ((hashMap != null) && (!hashMap.isEmpty())){ 
                 cadena=Cadena.construyeCadena(hashMap);
                 preparaRegistro(cadena,claveSeguridadWS,arregloParametros[0],crs.getString("forma"),arregloParametros[1].substring(0,3),siafmSiga.getEntidad(),siafmSiga.getAmbito().substring(3,4),crs.getString("referencia"),crs.getString("concepto"),crs.getString("catalogocuenta"),String.valueOf(eventoContable),ejercicio);
                 resultado=registro.aplicaRegistroContable(conexionContabilidad,crs.getString("catalogocuenta"),Areas.MANUAL);
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
          cuentaContable.update_rf_tr_cuentas_contables_acumulados_eli(conexionContabilidad,ejercicio,"1",arregloParametros[0].substring(3,5));
        conexionContabilidad.commit();
        conexionSistema.commit();
        
      }
      else{
        System.out.println("Error.  Numero de proceso " + eventoProceso);
        throw new Exception(resultado);  
      }
      System.out.println(resultado);  
      return resultado;
    }catch(Exception e){
        conexionContabilidad.rollback();
        conexionSistema.rollback();
        System.out.println("Ocurrio un error al accesar al metodo aplicaRegistroContableSiga "+e.getMessage()); 
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