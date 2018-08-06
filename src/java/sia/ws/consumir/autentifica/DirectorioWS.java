/*
 * Directorio.java
 *
 * Created on 26 de julio de 2006, 09:33 AM
 *
 * elena.herrera@senado.gob.mx
 */
package sia.ws.consumir.autentifica;

import sia.ws.consumir.autentifica.directorio.Autentica;

/**Nueva clase de autenticacion del sia*/
public class DirectorioWS {
  private String login;
  private String password;
  private Autentica autenticador;
  
  private final String OBJ_PRUEBA = "false";
  
  /**
   * Constructores para la clase directorio
   */
  public DirectorioWS() {
    this.autenticador = new Autentica();
  }
  
  public DirectorioWS(String login, String password){
    this.login = login;
    this.password = password;
    this.autenticador = new Autentica(login, password);
  }
  
  /**habiendose authenticado el usuario obtiene la informacion y la iguala*/
  private void getInfo(){
    /**obtener la curp*/
    this.autenticador.ObtenerCURP();
    /**obtener el numero de empleado*/
    this.autenticador.ObtenerNumeroEmpleado();
  }
  
  public boolean Authenticate(){
    boolean blResultado = false;
    if((this.login!=null)&&(this.password!=null)){
      this.autenticador = new Autentica(this.login, this.password);
      if(autenticador.Autenticar()){
        System.out.println("[sia.webservice.acceso.Directorio.Authenticate:42] usuario autenticado");
        blResultado = true;
        this.getInfo();
      }
    }
    return blResultado;
  }
  
  public boolean Authenticate(String login, String password){
    boolean blResultado = false;
    this.autenticador = new Autentica(login, password);
    if(this.autenticador.Autenticar()){
      blResultado = true;
      this.getInfo();
    }
    return blResultado;
  }
  
  /**
   * getters y setters para los attributos de la clase
   */
  
  public void  setLogin(String login){
    this.login = login;
  }
  
  public String getLogin(){
    return this.login;
  }
  
  public void  setPassword(String password){
    this.password = password;
  }
  
  public String getPassword(){
    return this.password;
  }
  
  public String getNumeroEmpleado(){
    if(!this.autenticador.getNumeroEmpleado().trim().equals("autenticado"))
      return this.autenticador.getNumeroEmpleado();
    else
      return null;
  }
  
  public String getCURP(){
    return this.autenticador.getCURP();
  }
  
  //wakko...
  public String getUsrCuenta(String numEmpleado){
    if (this.autenticador.ObtenerUsrCuenta(numEmpleado))
      return this.autenticador.getUsrCuenta();
    else
      return null;
  }
  
  public String getUsrCorreo(String numEmpleado){
    if (this.autenticador.ObtenerUsrCorreo(numEmpleado))
      return this.autenticador.getUsrCorreo();
    else
      return null;
  }
  
  //AdminSoap. crea un usuario en DA
  public String getUsrCrear(String cveMov, String nombre, String apellido, String curp, String cargo, String clavePuesto, String nivelPuesto, String puesto, String numEmpleado, String objPrueba){    
    if (this.autenticador.CrearUsuario(cveMov, nombre, apellido, curp, cargo, clavePuesto, nivelPuesto, puesto, numEmpleado, objPrueba))
      return this.autenticador.getUsrCrear();
    else
      return null;
  }
  
  //AdminSoap. crea un usuario en DA, solo con los valores requeridos
  public String getCrearUsuario(String cveMov, String nombre, String apellido, String curp, String cargo, String clavePuesto, String nivelPuesto, String puesto, String numEmpleado){
    if (this.autenticador.CrearUsuario(cveMov, nombre, apellido, curp, cargo, clavePuesto, nivelPuesto, puesto, numEmpleado, OBJ_PRUEBA))
      return this.autenticador.getUsrCrear();
    else
      return null;
  }
}
