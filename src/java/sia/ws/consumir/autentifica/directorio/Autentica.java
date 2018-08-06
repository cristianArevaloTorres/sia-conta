package sia.ws.consumir.autentifica.directorio;

import java.lang.*;

/**
 * Clase que permite la autenticación de un usuario, así como la obtención de su CURP y/o Número de empleado. 
 * <br>Para la autenticación se hace uso del Servicio Web <a href="http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion/ldap.asmx?WSDL" target="_blank">http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion/ldap.asmx?WSDL</a>
 * <br><br><b>Ejemplo:</b>
 *    <p>
 *     <br>String curp = "";
 *     <br>Autentica autentica = new Autentica();
 *     <br>autentica.setUsuario("UsuarioAAutenticar");
 *     <br>autentica.setContrasena("passwordDelUsuario");
 *     <br>
 *     <br>if(autentica.ObtenerCURP())
 *     <br>{
 *     <br>&nbsp;&nbsp;&nbsp;curp = autentica.getCURP();
 *     <br>}
 *     <br>
 *     <br>System.out.println(curp);
 *     </p>
 */
public class Autentica 
{

  //Propiedades  
  private String usuario;
  private String contrasena;
  private String CURP;
  private String NumeroEmpleado;  
  private LDAPSoapProxy obj;  
  
  private String usrCuenta = null;
  private String usrCorreo = null;
  
  //Admin
  private String usrCrear = null;
  private AdminSoapProxy objAdmin;

  private final String CVE_APLICACION = "DGITI_JAVA_SIA";
  //private final String CVE_MOV = "60";
  
  /**
   * Constructor por default. 
   * Establece el usuario y su contraseña a vacios.
   */
  public Autentica()
  {
    obj = new LDAPSoapProxy();

    this.setUsuario("");
    this.setContrasena("");
    this.setCURP("");
    this.setNumeroEmpleado("");
    
    this.setUsrCuenta("");
    this.setUsrCorreo("");
    
    //Admin
    objAdmin = new AdminSoapProxy();
    this.setUsrCrear("");

  }


  /**
   * Constructor que establece el usuario y contraseña de la persona a autenticar. 
   */
  public Autentica(String usuario, String contrasena)
  {
    obj = new LDAPSoapProxy();

    this.setUsuario(usuario);
    this.setContrasena(contrasena);
    this.setCURP("");
    this.setNumeroEmpleado("");
    
    this.setUsrCuenta("");
    this.setUsrCorreo("");
    
    //Admin
    objAdmin = new AdminSoapProxy();
    this.setUsrCrear("");    
  }


  /**
   * <b>Método para obtener la CURP de un usuario.</b> Si la autenticación se realiza con éxito se establece la CURP del usuario, en caso contrario la CURP se establece a una cadena vacía. Para acceder a la CURP del usuario es necesario hacer uso del método getCURP()
   * 
   * @return Si la autenticación se realizo con éxito regresa true, en cualquier otro caso retorna false. 
   */
  public boolean ObtenerCURP() {
    boolean success = false;    
    
    try {
      this.setCURP(obj.obtenerCURP(this.usuario, this.contrasena));
      
      if(!this.getCURP().equals("")){
        success = true;
      }
    }
    catch(Exception ex) {
      this.setCURP("");
      success = false;
    }
    
    return success;
  }


  /**
   * <b>Método para obtener el Número de Empleado de un usuario.</b> Si la autenticación se realiza con éxito se establece el Número de Empleado del usuario, en caso contrario el Número de Empleado se establece a una cadena vacía. Para acceder al Numero de Empleado del usuario es necesario hacer uso del método getNumeroEmpleado()
   * 
   * @return Si la autenticación se realizo con éxito regresa true, en cualquier otro caso retorna false. 
   */
  public boolean ObtenerNumeroEmpleado() {
    boolean success = false;
    
    try {
      this.setNumeroEmpleado(obj.obtenerNumEmpleado(this.usuario, this.contrasena));
      
      if(!this.getNumeroEmpleado().equals("")) {
        success = true;
      }
    }
    catch(Exception ex) {
      this.setNumeroEmpleado("");
      success = false;
    }
    
    return success;
  }

  
  /**
   * Método que verifica si el usuario y contraseñaa son correctos. Se hace uso del método ObtenerNumEmpleado();
   * 
   * @return 
   */
  public boolean Autenticar() {
    return this.ObtenerNumeroEmpleado();
  }


  /**
   * Método que establece el usuario a autenticar.
   * 
   * @param usuario Usuario a autenticar.
   */
  public void setUsuario(String usuario) {
    this.usuario = usuario;
  }


  /**
   * Método que obtiene el usuario a autenticar.
   * 
   * @return Usuario a autenticar.
   */
  public String getUsuario() {
    return usuario;
  }


  /**
   * Método que establece la contrasena del usuario a autenticar.
   * 
   * @param contrasena Constraseña o Password del usuario a autenticar.
   */
  public void setContrasena(String contrasena) {
    this.contrasena = contrasena;
  }


  /**
   * Método que obtiene la contrasena del usuario a autenticar.
   * 
   * @return Contraseña del usuario a autenticar.
   */
  public String getContrasena() {
    return contrasena;
  }


  /**
   * Método que establece la CURP del usuario autenticado.
   * 
   * @param CURP Valor de la CURP del usuario autenticado.
   */
  private void setCURP(String CURP) {
    this.CURP = CURP;
  }


  /**
   * Método que obtiene la CURP del usuario autenticado. Es necesario hacer uso del método ObtenerCURP() antes de utilizar este método, de lo contrario el valor regresado sera una cadena vacía.
   * 
   * @return CURP del usuario autenticado.
   */
  public String getCURP() {
    return CURP.trim();
  }


  /**
   * Metodo que establece el Numero de Empleado del usuario autenticado.
   * 
   * @param NumeroEmpleado Numero de Empleado del usuario autenticado.
   */
  private void setNumeroEmpleado(String NumeroEmpleado) {
    this.NumeroEmpleado = NumeroEmpleado;
  }


  /**
   * Metodo que obtiene el Numero de Empleado del usuario autenticado. Es necesario hacer uso del metodo ObtenerNumeroEmpleado() antes de utilizar este metodo, de lo contrario el valor regresado sera una cadena vacia.
   * 
   * @return Numero del empleado del usuario autenticado.
   */
  public String getNumeroEmpleado() {
    return NumeroEmpleado.trim();
  }

  
  //wakko...
  
  //Obtiene la cuenta(login) del usuario a partir del numEmpleado
  public boolean ObtenerUsrCuenta(String numEmpleado) {
    boolean success = false;    
    
    try {
      this.setUsrCuenta(obj.obtenerUsrCuenta(numEmpleado, "DGITI_JAVA_SIA"));
      
      if(!this.getUsrCuenta().equals("")){
        success = true;
      }
    }
    catch(Exception ex) {
      this.setUsrCuenta("");
      success = false;
    }
    
    return success;
  }  
  
  public java.lang.String getUsrCuenta() {
    return usrCuenta;
  }
  
  public void setUsrCuenta(java.lang.String usrCuenta) {
    this.usrCuenta = usrCuenta;
  }
  
  //Obtiene el atributo correo(mail) del usuario a partir del numEmpleado, Mediante el ws ObtenerUsrAtributo
  public boolean ObtenerUsrCorreo(String numEmpleado) {
    boolean success = false;
    try {      
      this.setUsrCorreo(obj.obtenerUsrAtributo("buscar", "loginUsr", "passUsr", "correo", "numempleado", numEmpleado, "DGITI_JAVA_SIA"));      
      if(!this.getUsrCorreo().equals(""))
        success = true;
    }
    catch(Exception ex) {
      this.setUsrCorreo("");
      success = false;
    }
    return success;    
  }
  
  public java.lang.String getUsrCorreo() {
    return usrCorreo;
  }

  public void setUsrCorreo(java.lang.String usrCorreo) {
    this.usrCorreo = usrCorreo;
  }
  
  //Crea un usaruio en el DA, Mediante el ws CrearUsuario
  public boolean CrearUsuario(String cveMov, String nombre, String apellido, String curp, String cargo, String clavePuesto, String nivelPuesto, String puesto, String numEmpleado, String objPuesto) {
    boolean success = false;
    try {
      //this.setUsrCrear(objAdmin.crearUsuario(nombre, apellido, curp, cargo, clavePuesto, nivelPuesto, puesto, numEmpleado, objPuesto, CVE_APLICACION));
      this.setUsrCrear(objAdmin.activeDirectory(cveMov, nombre, apellido, curp, cargo, clavePuesto, nivelPuesto, puesto, numEmpleado, objPuesto, CVE_APLICACION));
      if(!this.getUsrCrear().equals(""))
        success = true;
    }
    catch(Exception ex) {
      System.out.println("Error[crearUsuario]: " +ex.toString());
      this.setUsrCrear("");
      success = false;
    }
    return success;    
  }
  
  public java.lang.String getUsrCrear() {
    return usrCrear;
  }

  public void setUsrCrear(java.lang.String usrCrear) {
    this.usrCrear = usrCrear;
  }
  
}