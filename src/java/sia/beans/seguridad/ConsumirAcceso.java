package sia.beans.seguridad;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import sia.libs.recurso.Propiedades;

public class ConsumirAcceso {
  
  private static final String SEARCH_ACCESO="searchAcceso";
  private static final String ADD_IP="addIp";
  private static final String DELETE_ACCESO="deleteAcceso"; 
  public  static final int    ERROR_COMUNICACION=-2; 
  public  static final int ACCESO_ELIMINADO=1;
  private String ip;
  private String nombreAplicacion;
  private String sesion;
  private int numeroEmpleado;
     
  public ConsumirAcceso(String ip, String nombreAplicacion,String sesion){
    this(ip,nombreAplicacion,sesion,0);
  }
 
  public ConsumirAcceso(String ip, String nombreAplicacion,String sesion,int numeroEmpleado) {
    this.setIp(ip);
    this.setNombreAplicacion(nombreAplicacion);
    this.setSesion(sesion);
    this.setNumeroEmpleado(numeroEmpleado);    
  } 
  
  private String consumir(Object[] parametros, String metodo) throws Exception {
    String regresa="0";
    Service service=null;
    
    try {
      service= new Service();
      Call     call   = (Call) service.createCall();
      call.setTargetEndpointAddress(Propiedades.getInstance().getPropiedad("sistema.sia.url"));
      call.setOperationName(metodo);          
      regresa=(String) call.invoke(parametros);                                                           
      System.out.println("Invocar WsAutenticacion: " + regresa);
    }
    
    catch (Exception e) {      
      throw new Exception("Error de comunicacion con WsAutenticacion" );      
    }finally{
      service=null;
    }
    return regresa;
  }
  
  public int getAccesoEmpleado()  {
    int valor=0; // 
    try{
      valor=Integer.parseInt(consumir(new Object[]{getIp(), getSesion(), getNombreAplicacion()},SEARCH_ACCESO));     
    
    }catch(Exception e){
      valor=ERROR_COMUNICACION;
    }    
    return valor;
  }
  
  public int addAccesoEmpleado() throws Exception {
    int valor=0; // 
    try{
      valor=Integer.valueOf(consumir(new Object[]{getIp(), getSesion(), getNombreAplicacion(), getNumeroEmpleado()}, ADD_IP));
    }
    catch(Exception e){
      valor=ERROR_COMUNICACION;
    }    
    return valor;
  }
  
  public int deleteAccesoEmpleado(boolean cerrarSesion) throws Exception {
     String mensaje=null;
     try{
       mensaje= consumir(new Object[]{getIp(),getSesion(), getNombreAplicacion(), getNumeroEmpleado(),cerrarSesion}, DELETE_ACCESO );
     }catch(Exception e){
       mensaje=String. valueOf(ERROR_COMUNICACION).concat(",").concat(e.getMessage());
     }     
     return  Integer.valueOf(mensaje.substring(0,mensaje.lastIndexOf(",")) );
  }
  
  

  private void setIp(String ip) {
    this.ip = ip;
  }

  public String getIp() {
    return ip;
  }

  private void setNombreAplicacion(String nombreAplicacion) {
    this.nombreAplicacion = nombreAplicacion;
  }

  public String getNombreAplicacion() {
    return nombreAplicacion;
  }

  private void setSesion(String sesion) {
    this.sesion = sesion;
  }

  public String getSesion() {
    return sesion;
  }


  private void setNumeroEmpleado(int numeroEmpleado) {
    this.numeroEmpleado = numeroEmpleado;
  }

  public int getNumeroEmpleado() {
    return numeroEmpleado;
  }
}
