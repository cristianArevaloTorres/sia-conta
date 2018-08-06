package sia.ws.publicar.contabilidad.test;

import java.sql.*; 
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

public class ConsumeEventoWS {
    public ConsumeEventoWS() {
    }
    
   private int idevento;  
   private int idproceso; 
   private String parametros; 
   private String claveWS; 
  
   /** 
   * idproceso 
   * @return idproceso 
   */ 
   public int getIdproceso() { 
      return idproceso; 
   } 
  
   /** 
   * idproceso 
   * @param idproceso 
   */ 
   public void setIdproceso( int idproceso ) { 
      this.idproceso=idproceso; 
   } 
  
   /** 
   * idevento 
   * @return idevento 
   */ 
   public int getIdevento() { 
      return idevento; 
   } 
  
   /** 
   * idevento 
   * @param idevento 
   */ 
   public void setIdevento( int idevento ) { 
      this.idevento=idevento; 
   } 
  
   /** 
   * parametros 
   * @return parametros 
   */ 
   public String getParametros() { 
      return parametros; 
   } 
  
   /** 
   * parametros 
   * @param parametros 
   */ 
   public void setParametros( String parametros ) { 
      this.parametros=parametros; 
   } 

    /** 
    * claveWS 
    * @return claveWS 
    */ 
    public String getClaveWS() { 
       return claveWS; 
    } 
    
    /** 
    * claveWS
    * @param claveWS 
    */ 
    public void setClaveWS( String claveWS ) { 
       this.claveWS=claveWS; 
    } 
     
    /** 
    * Metodo que consume el metodo procesarEvento del servicio WEB Contabilidad 
    * Fecha de creacion: 06 de Noviembre del 2009
    * Autor: Jorge Luis P�rez Navarro
    * Fecha de modificacion: 
    * Modificado por: 
    */ 
    public String aplicaRegistroContable()throws SQLException, Exception{ 
      Service service;
      String resultado="";
      try {
        //String endpoint = "http://10.26.3.116:8990/siafm/services/Contabilidad"; //Ip del servidor de servicios WEB
        //String endpoint = "http://10.192.12.250:8989/siafm/services/Contabilidad"; //Ip del servidor de servicios WEB  
        //String endpoint = "http://10.26.3.113:8989/siafm/services/Contabilidad"; //Ip del servidor de servicios WEB  
        //String endpoint = "http://10.40.40.211:8989/siafm/services/Contabilidad"; //Ip del servidor de servicios WEB  
        //String endpoint = "http://10.26.4.16:8988/siafm/services/Contabilidad"; //Ip del servidor de servicios WEB  
        //String endpoint = "http://uasipro5.senado.gob.mx/siafm_inv/services/Contabilidad"; //Ip del servidor de servicios WEB  
        String endpoint = "http://algo.senado.gob.mx/siafm_inv/services/Contabilidad"; //Ip del servidor de servicios WEB  
        service= new Service();
        Call     call   = (Call) service.createCall();
        call.setTargetEndpointAddress(endpoint);
        call.setOperationName("procesarEvento");
        resultado= (String) call.invoke(new Object[] {idevento,idproceso,parametros,claveWS});
        System.out.println(resultado);
      }  
      catch (Exception e) {
        System.out.println("Ocurrio un error al accesar al metodo ConsumeEventoWS "+e.getMessage()); 
        throw e; 
      }
      return resultado;
    } //Fin metodo aplicaRegistroContable    
} //Fin ConsumeEventoWS
