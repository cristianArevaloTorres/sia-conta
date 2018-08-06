package sia.rf.contabilidad.registroContableEvento;

import sia.db.dao.DaoFactory;
import java.sql.Connection;
import java.sql.SQLException;   
import java.util.HashMap;

import sia.libs.correo.Envio;

import sia.libs.recurso.Contabilidad;

import sia.rf.contabilidad.registroContable.formas.Areas;

import sia.ws.publicar.contabilidad.Registro;
import sun.jdbc.rowset.CachedRowSet;


import sia.rf.contabilidad.registroContableNuevo.bcCuentaContable;

import sia.rf.contabilidad.sistemas.sici.SiafmSICI;


/**
 *
 * @author luz.lopez
 */
public class EventoSici {

  private EventoContable eventoContable=null;
  private FormaContable formaContable=null;
  private Modulo modulo = null;
  private Sistema sistema=null;
  private Registro registro=null;
  private BitacoraLocal bitacoraLocal=null;
  private CachedRowSet crs=null;
  private int banAre=0;
  private String origen="";
  
  
  public EventoSici() {
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
  public String procesarEvento(int eventoContable, int eventoProceso, String parametros, String claveSeguridadWS) throws SQLException, Exception{  
    String nombreDS=null;
    Connection conexionSistema=null;
    Connection conexionContabilidad=null;
    String cadena=null;
    String resultado=null;
    String ref=null;
    String [] arregloParametros = parametros.split("&");
    HashMap hashMap = new HashMap(); 
    
    
    bcCuentaContable cuentaContable = null;    
    
    SiafmSICI siafmSICI=null;    
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
         System.out.println("Procesando Evento: "+eventoContable);
         switch (eventoContable){
             case 350:  //Evento SICI parametros(Conexion, idIngresoAuto)     
                siafmSICI  = new SiafmSICI();
                cadena = siafmSICI.evento350(conexionSistema,arregloParametros[1]);
                if (!cadena.equals("")){    
                  preparaRegistro(cadena,claveSeguridadWS,arregloParametros[0],crs.getString("forma"),crs.getString("unidadEjecutora"),crs.getString("entidad"),crs.getString("ambito"),siafmSICI.getReferencia(),crs.getString("concepto"),crs.getString("catalogocuenta"),String.valueOf(eventoContable),ejercicio);
                  resultado=registro.aplicaRegistroContable(conexionContabilidad,crs.getString("catalogocuenta"),Areas.MIPF_NO_ACUMULADO);  
                  liPoliza=resultado.indexOf("***")+3;
                  lsPolizas=lsPolizas+resultado.substring(liPoliza,liPoliza+7)+"|";                 
                  hashMap.clear();
                }
                else{
                  lsPolizas=lsPolizas+"SIN POLIZA|";
                  resultado="1,Sin poliza por hashMap vacio";
                }
                break;                  
           case 351:  ///Evento SICI parametros(Conexion, idIngresoAuto)   
              siafmSICI  = new SiafmSICI();
              cadena = siafmSICI.evento351(conexionSistema,arregloParametros[1]);
              if (!cadena.equals("")){    
                preparaRegistro(cadena,claveSeguridadWS,arregloParametros[0],crs.getString("forma"),crs.getString("unidadEjecutora"),crs.getString("entidad"),crs.getString("ambito"),siafmSICI.getReferencia(),crs.getString("concepto"),crs.getString("catalogocuenta"),String.valueOf(eventoContable),ejercicio);
                resultado=registro.aplicaRegistroContable(conexionContabilidad,crs.getString("catalogocuenta"),Areas.MIPF_NO_ACUMULADO);  
                liPoliza=resultado.indexOf("***")+3;
                lsPolizas=lsPolizas+resultado.substring(liPoliza,liPoliza+7)+"|";                 
                hashMap.clear();
              }
              else{
                lsPolizas=lsPolizas+"SIN POLIZA|";
                resultado="1,Sin poliza por hashMap vacio";
              }
              break;
           case 352:  ///Evento SICI parametros(Conexion, idIngresoAuto)   
              siafmSICI  = new SiafmSICI();
              cadena = siafmSICI.evento352(conexionSistema,arregloParametros[1]);
              if (!cadena.equals("")){    
                preparaRegistro(cadena,claveSeguridadWS,arregloParametros[0],crs.getString("forma"),crs.getString("unidadEjecutora"),crs.getString("entidad"),crs.getString("ambito"),siafmSICI.getReferencia(),crs.getString("concepto"),crs.getString("catalogocuenta"),String.valueOf(eventoContable),ejercicio);
                resultado=registro.aplicaRegistroContable(conexionContabilidad,crs.getString("catalogocuenta"),Areas.MIPF_NO_ACUMULADO);  
                liPoliza=resultado.indexOf("***")+3;
                lsPolizas=lsPolizas+resultado.substring(liPoliza,liPoliza+7)+"|";                 
                hashMap.clear();
              }
              else{
                lsPolizas=lsPolizas+"SIN POLIZA|";
                resultado="1,Sin poliza por hashMap vacio";
              }
              break;
           case 353:  ///Evento SICI parametros(Conexion, idIngresoAuto)   
              siafmSICI  = new SiafmSICI();
              cadena = "";
              switch (Integer.parseInt(crs.getString("idforma"))){
                case 553: cadena=siafmSICI.evento353A(conexionSistema,arregloParametros[1]);break;
                case 554: cadena=siafmSICI.evento353B(conexionSistema,arregloParametros[1]);break;
              }
              if (!cadena.equals("")){    
                preparaRegistro(cadena,claveSeguridadWS,arregloParametros[0],crs.getString("forma"),crs.getString("unidadEjecutora"),crs.getString("entidad"),crs.getString("ambito"),siafmSICI.getReferencia(),crs.getString("concepto"),crs.getString("catalogocuenta"),String.valueOf(eventoContable),ejercicio);
                resultado=registro.aplicaRegistroContable(conexionContabilidad,crs.getString("catalogocuenta"),Areas.MIPF_NO_ACUMULADO);  
                liPoliza=resultado.indexOf("***")+3;
                lsPolizas=lsPolizas+resultado.substring(liPoliza,liPoliza+7)+"|";                 
                hashMap.clear();
              }
              else{
                lsPolizas=lsPolizas+"SIN POLIZA|";
                resultado="1,Sin poliza por hashMap vacio";
              }
              break;
           case 354:  ///Evento SICI parametros(Conexion, idIngresoAuto)   
              siafmSICI  = new SiafmSICI();
              cadena = siafmSICI.evento354(conexionSistema,arregloParametros[1]);
              if (!cadena.equals("")){    
                preparaRegistro(cadena,claveSeguridadWS,arregloParametros[0],crs.getString("forma"),crs.getString("unidadEjecutora"),crs.getString("entidad"),crs.getString("ambito"),siafmSICI.getReferencia(),crs.getString("concepto"),crs.getString("catalogocuenta"),String.valueOf(eventoContable),ejercicio);
                resultado=registro.aplicaRegistroContable(conexionContabilidad,crs.getString("catalogocuenta"),Areas.MIPF_NO_ACUMULADO);  
                liPoliza=resultado.indexOf("***")+3;
                lsPolizas=lsPolizas+resultado.substring(liPoliza,liPoliza+7)+"|";                 
                hashMap.clear();
              }
              else{
                lsPolizas=lsPolizas+"SIN POLIZA|";
                resultado="1,Sin poliza por hashMap vacio";
              }
              break;
             case 355:  ///Evento SICI parametros(Conexion, mes)  
                siafmSICI  = new SiafmSICI();
               cadena = siafmSICI.evento355(conexionSistema,arregloParametros[0].substring(3,10));
                if (!cadena.equals("")){    
                  preparaRegistro(cadena,claveSeguridadWS,arregloParametros[0],crs.getString("forma"),crs.getString("unidadEjecutora"),crs.getString("entidad"),crs.getString("ambito"),siafmSICI.getReferencia(),crs.getString("concepto")+"_"+arregloParametros[0].substring(3,5)+"_"+ejercicio,crs.getString("catalogocuenta"),String.valueOf(eventoContable),ejercicio);
                  resultado=registro.aplicaRegistroContable(conexionContabilidad,crs.getString("catalogocuenta"),Areas.MIPF_NO_ACUMULADO);  
                  liPoliza=resultado.indexOf("***")+3;
                  lsPolizas=lsPolizas+resultado.substring(liPoliza,liPoliza+7)+"|";                 
                  hashMap.clear();
                }
                else{
                  lsPolizas=lsPolizas+"SIN POLIZA|";
                  resultado="1,Sin poliza por hashMap vacio";
                }
                break;
           case 356:  ///Evento SICI parametros(Conexion, mes) Eliminacion por remesas
              siafmSICI  = new SiafmSICI();
             cadena = siafmSICI.evento356(conexionSistema,arregloParametros[0].substring(3,10));
              if (!cadena.equals("")){    
                preparaRegistro(cadena,claveSeguridadWS,arregloParametros[0],crs.getString("forma"),crs.getString("unidadEjecutora"),crs.getString("entidad"),crs.getString("ambito"),"ELIMINACION_REMESAS_"+arregloParametros[0].substring(3,5)+"_"+ejercicio,crs.getString("concepto")+" CORRESPONDIENTE A "+ejercicio,crs.getString("catalogocuenta"),String.valueOf(eventoContable),ejercicio);
                resultado=registro.aplicaRegistroContable(conexionContabilidad,crs.getString("catalogocuenta"),Areas.MIPF_NO_ACUMULADO);  
                liPoliza=resultado.indexOf("***")+3;
                lsPolizas=lsPolizas+resultado.substring(liPoliza,liPoliza+7)+"|";                 
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
        throw new Exception(resultado);  
      }
      System.out.println(resultado);  
      return resultado;
    }catch(Exception e){
        conexionContabilidad.rollback();
        conexionSistema.rollback();
        System.out.println("Ocurrio un error al accesar al metodo aplicaRegistroContableSici "+e.getMessage()); 
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