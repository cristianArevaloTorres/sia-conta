package sia.ws.consumir;
 
import javax.naming.NamingException;
import javax.naming.NamingEnumeration;
import javax.naming.Context;
import javax.naming.directory.Attributes;
import javax.naming.directory.Attribute;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;
import javax.naming.ldap.InitialLdapContext;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
 
import sia.libs.formato.Error;
 
public class Directorio implements Serializable {

  private final static String NOMBRES_DOMINIOS = "| |norte|sur|";
  
  private LdapContext ctx;
  private String cuenta;
  private String contrasenia;
  private String atributos;
  
  public Directorio() {
    setAtributos("DC=inegi,DC=gob,DC=mx");
  }
  
  public void getAttributes() {
    try {      
      ctx.close();
    } 
    catch (NamingException e) {
      Error.mensaje(e, "SIAFM");
    }
  }
 
  public String getAtributes(String atributo) {
    StringBuffer filter = new StringBuffer("(sAMAccountName=").append(getCuenta()).append(")");
    try {
      String returnedAtts[]= {atributo};
      SearchControls ctls  = new SearchControls();
      ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
      ctls.setReturningAttributes(returnedAtts);
      NamingEnumeration answer = ctx.search(getAtributos(), filter.toString(), ctls);
      Map record = new HashMap();
      while (answer.hasMoreElements()) {
        record= new HashMap();
        SearchResult sr= (SearchResult)answer.next();
        record= formatAttributes(sr.getAttributes());
        return (String)record.get(atributo);
      }
    } 
    catch (Exception e) {
      if(getCuenta()!= null && getCuenta().indexOf(".")> 0)
        Error.mensaje(e, "SIAFM", e.getMessage().concat(" [").concat(getCuenta()).concat("]-[").concat(atributo).concat("]"));
      return null;
    }
    return null;
  }
  
  
  public String getEmail(String numeroEmpleado) {
    return getAtributo(numeroEmpleado,"mail");
  }    
  
  
  public String getLogin(String numeroEmpleado) {
    return getAtributo(numeroEmpleado,"sAMAccountName");
  }    
  
  public String getAtributo(String numeroEmpleado, String atributo) {
    StringBuffer filter = new StringBuffer("(mobile=").append(numeroEmpleado).append(")");
    try {
      String returnedAtts[]= {atributo};
      SearchControls ctls  = new SearchControls();
      ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
      ctls.setReturningAttributes(returnedAtts);
      NamingEnumeration answer = ctx.search(getAtributos(), filter.toString(), ctls);
      Map record = new HashMap();
      while (answer.hasMoreElements()) {
        record= new HashMap();
        SearchResult sr= (SearchResult)answer.next();
        record= formatAttributes(sr.getAttributes());
        return (String)record.get(atributo);
      }
    } 
    catch (Exception e) {
      if(getCuenta()!= null && getCuenta().indexOf(".")> 0)
        Error.mensaje(e, "SECO", e.getMessage().concat(" [").concat(getCuenta()).concat("]-[").concat(numeroEmpleado).concat("]"));
      return null;
    }
    return null;
  }
 
  public String getCURP() {
    return getAtributo("description");
  }
  
  public String getCorreo() {
    return getAtributo("mail");
  }
 
  public String getNumeroEmpleado() {
    return getAtributo("mobile");
  }
 
  public String getNombreCompleto() {
    return getAtributo("name");
  }
 
  public String getNombreCorto() {
    return getAtributo("sAMAccountName");
  }
  
  private boolean checkDominio(String dominio) {
    Hashtable env = new Hashtable();
    env.put(Context.REFERRAL, "follow" );
    env.put(Context.SECURITY_AUTHENTICATION,"simple");
    env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
    env.put(Context.SECURITY_PRINCIPAL, getCuenta().concat("@").concat(dominio).concat("senado.gob.mx"));
    env.put(Context.PROVIDER_URL, "ldap://w-appintradns01.senado.gob.mx:389");
    //env.put(Context.PROVIDER_URL, "http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion");
    //env.put(Context.PROVIDER_URL, "http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion/ldap.asmx");
    env.put(Context.SECURITY_CREDENTIALS, getContrasenia());
    try {
      ctx= new InitialLdapContext(env, null);
      return true;
    } 
    catch (NamingException e) {
      //System.out.println("[sia.webservice] Error: "+ e);
      //e.printStackTrace();
      return false;
    }
  }

  public String getAtributo(String atributo) {
    String regresar= null;
    if(ctx== null) {
      StringTokenizer dominios= new StringTokenizer(NOMBRES_DOMINIOS, "|");
      while(dominios.hasMoreTokens() && regresar== null) {
        String dominio= dominios.nextToken().trim();
        checkDominio(dominio.concat(dominio.length()== 0? "": "."));
        regresar= getAtributes(atributo);
      } // while dominios
    }
    else
      regresar= getAtributes(atributo);
    return regresar;
  }
  
  public boolean Authenticate(String cuenta, String contrasenia) {
    setCuenta(cuenta);
    setContrasenia(contrasenia);
    boolean regresar= false;
    StringTokenizer dominios= new StringTokenizer(NOMBRES_DOMINIOS, "|");
    while(dominios.hasMoreTokens() && !regresar) {
      String dominio= dominios.nextToken().trim();
      regresar= checkDominio(dominio.concat(dominio.length()== 0? "": "."));
    } // while dominios
    return regresar;
  }
  
  public Map formatAttributes(Attributes attrs) throws Exception {
    Map columns = new Hashtable(20);
    String value= null;
    if (attrs== null) 
      Error.mensaje(new Exception("Este resultado no tiene atributos"), "SIAFM");
    else {
      try {
        for (NamingEnumeration enumeration = attrs.getAll(); enumeration.hasMore();) {
          Attribute attrib = (Attribute)enumeration.next();
          for (NamingEnumeration e= attrib.getAll(); e.hasMore();){
            value = e.next().toString();
            columns.put(attrib.getID(),value);
          }
        }
      } 
      catch (NamingException e) {
        Error.mensaje(e, "SIAFM");
        return null;
      }
    }
    return columns;
  }
  
  public java.lang.String getContrasenia() {
    return contrasenia;
  }
  
  public void setContrasenia(java.lang.String contrasenia) {
    this.contrasenia = contrasenia;
  }
  
  public java.lang.String getCuenta() {
    return cuenta;
  }
  
  public void setCuenta(java.lang.String cuenta) {
    this.cuenta = cuenta;
  }
  
  public java.lang.String getAtributos() {
    return atributos;
  }
  
  public void setAtributos(java.lang.String atributos) {
    this.atributos = atributos;
  }
  
  public void finalize() {
    getAttributes();
    ctx= null;
  }
  
}