package sia.rf.contabilidad.registroContableEvento;

import java.sql.Connection;
import java.sql.SQLException;
import sia.db.dao.DaoFactory;
import sun.jdbc.rowset.CachedRowSet;


/**
 *
 * @author jorgeluis.perez
 */
public class RecuperaEvento {

  private Sistema sistema=null;
  private BitacoraLocal bitacoraLocal=null;
  private bcEstadoCatalogo bcEstadoCat=null;
  private CachedRowSet crsBitacoras=null;
  private CachedRowSet crsSistemas=null;
    
  public RecuperaEvento() {
    sistema = new Sistema();
    bitacoraLocal = new BitacoraLocal();
    bcEstadoCat = new bcEstadoCatalogo();
  }
     
  private void obtenerSistemas() throws SQLException, Exception {   
    try{      
      crsSistemas=sistema.select_rf_tr_sistema_enCache();
    }catch(Exception e) {
       System.out.println("Ocurrio un errror al accesar el metodo obtenerSistemas "+e.getMessage());   
       throw e;
    }
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
  
  public void procesarEvento(String claveSeguridadWS) throws SQLException, Exception{
    String nombreDS;
    Connection conexionSistema=null;
    Connection conexionContabilidad=null;
    String resultado=null;
    String lsPolizas;
    String estatus;
    String descripcion;
//    System.out.println("sia.rf.contabilidad.registroContableEvento.RecuperaEvento.procesarEvento.inicia ");
    try{ 
      obtenerSistemas();
      conexionContabilidad=DaoFactory.getContabilidad();
      
      while (crsSistemas.next()){
        nombreDS=crsSistemas.getString("cadConexion");
//        System.out.println("sia.rf.contabilidad.registroContableEvento.RecuperaEvento.procesarEvento.nombreDS "+nombreDS);
        // System.out.println("USR:"+ crsSistemas.getString("usuario")+"- Pwd:"+crsSistemas.getString("contrasena"));
        conexionSistema=obtenerConexionModulo(nombreDS);
        crsBitacoras=bitacoraLocal.select_BITACORALOCAL_enCache(conexionSistema);        
        int posicionError = 0;
         while (crsBitacoras.next()){
           try{
             System.out.println("sia.rf.contabilidad.registroContableEvento.RecuperaEvento.idEvento "+crsBitacoras.getString("idevento"));
             bcEstadoCat.select_rf_tc_estado_catalogo(conexionContabilidad);
             posicionError++;
             estatus=bcEstadoCat.getEstatus();
             posicionError++;
             descripcion=bcEstadoCat.getDescripcion();
             posicionError++;
             if (estatus.equals("1") || (crsBitacoras.getString("idevento").equals("300"))||(crsBitacoras.getString("idevento").equals("301"))||(crsBitacoras.getString("idevento").equals("302"))){
             System.out.println("aqui1.9"+crsBitacoras.getString("idevento"));
               if (Integer.parseInt(crsBitacoras.getString("idevento"))>=6 && Integer.parseInt(crsBitacoras.getString("idevento"))<=23){                 
             posicionError++;
                  EventoInventario eventoInventario = new EventoInventario();      
                  lsPolizas=eventoInventario.procesarEvento(Integer.parseInt(crsBitacoras.getString("idevento")),Integer.parseInt(crsBitacoras.getString("idproceso")),crsBitacoras.getString("parametros"),claveSeguridadWS);
                  resultado="1,El evento contable " + crsBitacoras.getString("idevento") + " con numero de proceso " + crsBitacoras.getString("idproceso") + " del sistema " +crsSistemas.getString("descripcion")+ " se lanzo desde el demonio, fue procesado correctamente y genero las siguientes polizas [" + lsPolizas+"]" ;              
               /*}  else  if ((Integer.parseInt(crsBitacoras.getString("idevento"))>=1 && Integer.parseInt(crsBitacoras.getString("idevento"))<=4)||(Integer.parseInt(crsBitacoras.getString("idevento"))>=40 && Integer.parseInt(crsBitacoras.getString("idevento"))<=70)){  
                     EventoConprove eventoConprove = new EventoConprove();
                     lsPolizas=eventoConprove.procesarEvento(Integer.parseInt(crsBitacoras.getString("idevento")),Integer.parseInt(crsBitacoras.getString("idproceso")),crsBitacoras.getString("parametros"),claveSeguridadWS);
                     resultado="1,El evento contable " + crsBitacoras.getString("idevento") + " con numero de proceso " + crsBitacoras.getString("idproceso") + " del sistema " +crsSistemas.getString("descripcion")+ " se lanzo desde el demonio, fue procesado correctamente y genero las siguientes polizas [" + lsPolizas+"]" ;              */
               }               
               else if  (Integer.parseInt(crsBitacoras.getString("idevento"))>=81 && Integer.parseInt(crsBitacoras.getString("idevento"))<=92){
             posicionError++;
                     EventoSiga eventoSiga = new EventoSiga();
                     lsPolizas=eventoSiga.procesarEvento(Integer.parseInt(crsBitacoras.getString("idevento")),Integer.parseInt(crsBitacoras.getString("idproceso")),crsBitacoras.getString("parametros"),claveSeguridadWS);
                     resultado="1,El evento contable " + crsBitacoras.getString("idevento") + " con numero de proceso " + crsBitacoras.getString("idproceso") + " del sistema " +crsSistemas.getString("descripcion")+ " se lanzo desde el demonio, fue procesado correctamente y genero las siguientes polizas [" + lsPolizas+"]" ;              
               }  else if ((Integer.parseInt(crsBitacoras.getString("idevento"))>=29 && Integer.parseInt(crsBitacoras.getString("idevento"))<=31)||(Integer.parseInt(crsBitacoras.getString("idevento"))>=100 && Integer.parseInt(crsBitacoras.getString("idevento"))<=300)){                 
             System.out.println("*aqui1.9"+crsBitacoras.getString("idevento"));
             posicionError++;
                       Evento evento = new Evento();
             System.out.println("*"+crsBitacoras.getString("idevento"));
                       lsPolizas=evento.procesarEvento(Integer.parseInt(crsBitacoras.getString("idevento")),Integer.parseInt(crsBitacoras.getString("idproceso")),crsBitacoras.getString("parametros"),claveSeguridadWS);
             System.out.println("*"+crsBitacoras.getString("idevento"));
                       resultado="1,El evento contable " + crsBitacoras.getString("idevento") + " con numero de proceso " + crsBitacoras.getString("idproceso") + " del sistema " +crsSistemas.getString("descripcion")+ " se lanzo desde el demonio, fue procesado correctamente y genero las siguientes polizas [" + lsPolizas+"]" ;                     
             System.out.println("*"+crsBitacoras.getString("idevento"));
               }  else if ((Integer.parseInt(crsBitacoras.getString("idevento"))>=301 && Integer.parseInt(crsBitacoras.getString("idevento"))<=302)){                 
             posicionError++;
                       EventoTraspaso eventoTraspaso = new EventoTraspaso();      
                       lsPolizas=eventoTraspaso.procesarEvento(Integer.parseInt(crsBitacoras.getString("idevento")),Integer.parseInt(crsBitacoras.getString("idproceso")),crsBitacoras.getString("parametros"),claveSeguridadWS);
                       resultado="1,El evento contable " + crsBitacoras.getString("idevento") + " con numero de proceso " + crsBitacoras.getString("idproceso") + " del sistema " +crsSistemas.getString("descripcion")+ " se lanzo desde el demonio, fue procesado correctamente y genero las siguientes polizas [" + lsPolizas+"]" ;                     
               } else if (Integer.parseInt(crsBitacoras.getString("idevento"))>=350  && Integer.parseInt(crsBitacoras.getString("idevento"))<=356){                 
             posicionError++;
                       EventoSici eventoSici = new EventoSici();      
                       lsPolizas=eventoSici.procesarEvento(Integer.parseInt(crsBitacoras.getString("idevento")),Integer.parseInt(crsBitacoras.getString("idproceso")),crsBitacoras.getString("parametros"),claveSeguridadWS);
                       resultado="1,El evento contable " + crsBitacoras.getString("idevento") + " con numero de proceso " + crsBitacoras.getString("idproceso") + " del sistema " +crsSistemas.getString("descripcion")+ " se lanzo desde el demonio, fue procesado correctamente y genero las siguientes polizas [" + lsPolizas+"]" ;                     
               }   
               else {
                     throw new Exception("0, No existe el evento contable: " + crsBitacoras.getString("idevento") + " para ser procesado.");
                 }
             }       
              else{
                  throw new Exception("0, El evento contable con numero de proceso " + crsBitacoras.getString("idproceso") + " no puede ser procesado en estos momentos porque hay un proceso de "+ descripcion+".");
              }    
             System.out.println(resultado);             
           }catch(Exception e){
               System.out.println(posicionError+" Ocurrio un error al accesar al metodo aplicaRegistroContable desde el demonio, evento "+crsBitacoras.getString("idevento")+" : "+e.getMessage()); 
           }
         }//Fin while bitacoras
//        System.out.println("sia.rf.contabilidad.registroContableEvento.RecuperaEvento fin de while ");
         if (crsBitacoras!=null){
           crsBitacoras.close();
           crsBitacoras=null;
         }  
         if (conexionSistema!=null){
           conexionSistema.close();
           conexionSistema=null;
         }  
      } //Fin while sistemas
    }catch(Exception e){
        System.out.println("Ocurrio un error grave al accesar al metodo aplicaRegistroContable desde el demonio "+e.getMessage()); 
        throw e;
    }
    finally{
      if (crsSistemas!=null){
        crsSistemas.close();
        crsSistemas=null;
      }    
      if (crsBitacoras!=null){
        crsBitacoras.close();
        crsBitacoras=null;
      }            
      if(conexionSistema!=null){
        conexionSistema.close();
      }
      if(conexionContabilidad!=null){
        conexionContabilidad.close();
      }      
    }
  }
/*  public void imprime(){
      System.out.println("imprime otro");
  }*/
}