/*
 * Clase: Error.java
 *
 * Creado: 21 de mayo de 2007, 12:16 AM
 *
 * Write by: alejandro.jimenez
 */
package sia.libs.formato;

import java.text.MessageFormat;

public final class Error {
  
  private Error() {
  }
  
  public static String getMsg(Throwable ex) {
    String regresa = "ninguno";
    if(ex.getMessage()!=null)
      regresa = ex.getMessage();
    return regresa;
  }

  public static String getPaquete(Object objeto)  {
    return objeto.getClass().getPackage().getName();
  }

  public static String getDominio(Object objeto)  {
    return objeto.getClass().getSimpleName();
  }

  public static String getNombre(Object objeto)  {
    return objeto.getClass().getName();
  }

  public static String getMensaje(Object objeto)  {
    return "[{0}:".concat(getNombre(objeto)).concat(".{1}] Error: {2}");
  }

  public static String getMensaje(Object objeto, Object ... valores)  {
    return MessageFormat.format(getMensaje(objeto), valores);
  }
  
  public static void mensaje(Object objeto, Throwable exception, Object ... valores)  {
    System.err.println(MessageFormat.format(getMensaje(objeto), valores));
    exception.printStackTrace();
  }

  public static String getMensaje() {
    return "[{0}:{1}.{2}] Error: {3}";
  }

  public static void mensaje(Throwable exception, String proyecto, String propio)  {
    StackTraceElement[] stackTraceElements= exception.getStackTrace();
    boolean existe = exception.getMessage()!=null;
    if(propio!= null)
      propio= exception.getMessage()!=null?exception.getMessage().concat("[").concat(propio).concat("]"):propio;
    else
      propio= exception.getMessage()!=null?exception.getMessage():"Ningun mensaje en el error";
    Object[] valores= new Object[] {proyecto, stackTraceElements[0].getClassName(), stackTraceElements[0].getMethodName(), propio};
    if(existe)
      System.err.println(MessageFormat.format(getMensaje(), valores));
    //exception.printStackTrace();
  }

  public static void mensaje(Throwable exception, String proyecto)  {
    mensaje(exception, proyecto, null);
  } 
  
  public static void main(String[] args) {
     Error.mensaje(new Exception("Hola"), "SIAFM"); 
     Error.mensaje(new Exception("Hola"), "SIAFM", "MENSAJE PROPIO"); 
  }
  
}