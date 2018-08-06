/*
 * Clase: Numero.java
 *
 * Creado: 21 de mayo de 2007, 12:16 AM
 *
 * Write by: alejandro.jimenez
 */

package sia.libs.formato;

import java.lang.reflect.Constructor;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public final class Numero {
  
  public static final int NUMERO_MONEDA              = 1;
  public static final int NUMERO_MILES_CON_DECIMALES = 2;
  public static final int NUMERO_MILES_SIN_DECIMALES = 3;
  public static final int NUMERO_MONEDA_SIN_DECIMALES= 4;
  public static final int NUMERO_DECIMALES           = 5;

  private Numero() {
  }

  public static String formatear(String patron, double valor) {
    NumberFormat formateo= NumberFormat.getCurrencyInstance(Locale.US);
    if (formateo instanceof DecimalFormat) {
      ((DecimalFormat) formateo).setDecimalSeparatorAlwaysShown(true);
      ((DecimalFormat) formateo).applyPattern(patron);
    }; // if
    return formateo.format(valor);
  }; // formatear
  
  public static String formatear(int patron, double valor) {
    String regresar= null;
    switch(patron) {
      case NUMERO_MONEDA: // formato moneda
        regresar= formatear("$ ###,##0.00", valor);
        break;
      case NUMERO_MILES_CON_DECIMALES: // separacion de miles
        regresar= formatear("###,###,###,###,##0.00", valor);
        break;
      case NUMERO_MILES_SIN_DECIMALES: // separacion de miles sin decimales
        regresar= formatear("###,##0", valor);
        break;
      case NUMERO_MONEDA_SIN_DECIMALES: // formato moneda
        regresar= formatear("$ ###,##0", valor);
        break;
      case NUMERO_DECIMALES: // formato moneda
        regresar= formatear("###############0.00", valor);
        break;
    }; // switch
    return regresar;
  }; // formatear
      
  public static String redondea(double valor, int decimales) {
    NumberFormat numberFormatUS = NumberFormat.getNumberInstance(Locale.US);
    numberFormatUS.setGroupingUsed(false);
    numberFormatUS.setMaximumFractionDigits(decimales);
    String numberString = numberFormatUS.format(valor);
    return numberString;
  }; // redondea
  
  public static String redondear(double valor){
    int operador= valor< 0? -1: 1;
    valor= operador* (Math.floor(Math.abs(valor)*100+ 0.5001)/100.0);
    return String.valueOf(valor);
  };// redondear

  private static Number getNumber(Class objeto, String value) {
    Number regresar= null;
    try  {
      Constructor constructor= objeto.getConstructor(String.class);
      regresar= (Number)constructor.newInstance(value);
    } 
    catch (Exception e) {
      try  {
        regresar= (Number)objeto.newInstance();
      }
      catch (Exception x) {
        regresar= null;
      } // try
    } // try
    return regresar;
  }
   
  public static Double getDouble(String value) {
    Double regresar= (Double)getNumber(Double.class, value);
    return regresar!= null? regresar: new Double(0);
  }

  public static Integer getInteger(String value) {
    Integer regresar= (Integer)getNumber(Integer.class, value);
    return regresar!= null? regresar: new Integer(0);
  }

  public static Long getLong(String value) {
    Long regresar= (Long)getNumber(Long.class, value);
    return regresar!= null? regresar: new Long(0);
  }

  public Short getLShort(String value) {
    Short regresar= (Short)getNumber(Short.class, value);
    short number  = 0;
    return regresar!= null? regresar: new Short(number);
  }

  public static Byte getByte(String value) {
    Byte regresar= (Byte)getNumber(Byte.class, value);
    byte number  = 0;
    return regresar!= null? regresar: new Byte(number);
  }
  
}; // Numero