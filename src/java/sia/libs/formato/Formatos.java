package sia.libs.formato;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import sia.libs.recurso.StringBean;

public class Formatos {

  private String codigos;
  private Map variables;

  public Formatos(String codigos, StringBean object) {
    setCodigos(codigos);
    setVariables(toHashMap(object));
  }

  /**
   * @param codigos -- sentencia o variables a remplazar
   * @param variables -- lista de valores que reemplazaran a las variables del query o variables
   */
  public Formatos(String codigos, Map variables) {
    setCodigos(codigos);
    setVariables(variables);
  }
  
  // "|var=valor|"
  // "valor";
  // {valor1,valor2};
  // valor1,valor2, ..... valorn
  
  public Formatos(String codigos, Object ... variables) {
    setCodigos(codigos);
    Variables variable=null;
    try  {
      variable=new Variables(variables);
      setVariables(variable.getMap());
    } 
    catch (Exception ex)  {
      Error.mensaje(ex, "SIAFM");
    } 
    finally  {
      variable=null;
    }
  }

  /*public String getSentencia() {
    StringTokenizer resultado = new StringTokenizer(getCodigos(), "{}", true);
    String token = null;
    Object valor = null;
    String sentencia = "";
    while (resultado.hasMoreTokens()) {
      token = resultado.nextToken();
      if (token.equals("{")){
        token = resultado.nextToken();
        valor = getVariables().get(token);
        if (valor!=null)
          sentencia = sentencia.concat(valor.toString());
        else
         sentencia = sentencia.concat("{").concat(token).concat("}");
        token = resultado.nextToken();  
      }
      else
        sentencia = sentencia.concat(token);
    }
    return sentencia;
  }*/


   public String getSentencia() {
     StringTokenizer resultado = new StringTokenizer(getCodigos(), "{}", true);
       String token = null;
       Object valor = null;
       String sentencia = "";
       while (resultado.hasMoreTokens()) {
         token = resultado.nextToken();
         if (token.equals("{")){
           token = resultado.nextToken();
           valor = getVariables().get(token);
           if (valor!=null)
             sentencia = sentencia.concat(valor.toString());
           else
            sentencia = sentencia.concat("{").concat(token).concat("}");
           token = resultado.nextToken();  
         }
         else
           sentencia = sentencia.concat(token);
       }
       return sentencia;
     }

  public String totalParsing(String texto) {
      System.out.println("Entrando");
      for (Object key: getVariables().keySet()) {
          texto = texto.replaceAll("{"+key.toString() +"}", getVariables().get(key).toString());
      }
      return texto;
  }

  private Map toHashMap(StringBean o) {
    Map regresar = new HashMap();
    Field[] atributos = o.getClass().getDeclaredFields();
    String metodo = null;
    for (Field atributo: atributos) {
      Object resultado = null;
      try {
        metodo = atributo.getName();
        metodo = "get".concat(metodo.substring(0, 1).toUpperCase().concat(metodo.substring(1)));
        Method method = o.getClass().getMethod(metodo, new Class[] { });
        resultado = method.invoke(o, new Object[] { });
      }
      catch (Exception e) {
        Error.mensaje(e, "SIAFM");
      }
      regresar.put(atributo.getName(), resultado);
    }
    return regresar;
  }
  
    public String getFormateada() {
        String sentencia = getCodigos();
        Iterator iterador= getVariables().keySet().iterator();
        String id = null;
        while (iterador.hasNext()) {
          id = iterador.next().toString();
          sentencia= sentencia.replaceAll(":".concat(id), String.valueOf(getVariables().get(id)));
        } // while    
        return sentencia;
      }

  public void setCodigos(String codigos) {
    this.codigos = codigos==null?"":codigos;
  }

  public String getCodigos() {
    return codigos;
  }

  public void setVariables(Map variables) {
    this.variables = variables;
  }

  public Map getVariables() {
    return variables;
  }

  public void finalize() {
    getVariables().clear();
    setVariables(null);
    setCodigos(null);
  }

}
