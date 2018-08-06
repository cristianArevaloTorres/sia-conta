package sia.db.sql;

import java.sql.Clob;

import java.util.HashMap;
import java.util.Map;






public class Vista extends HashMap {

  public String getField(String name) {
    String regresar= null;
    if(containsKey(name.toUpperCase()) && get(name.toUpperCase())!= null)
      regresar= get(name.toUpperCase()).toString();
    return regresar;  
  }

  public Object getObject(String name) {
    Object regresar= null;
    if(containsKey(name.toUpperCase()))
      regresar= get(name.toUpperCase());
    return regresar;  
  }

  public String getField(int index) {
    String regresar= null;
    Object[] datos = entrySet().toArray();
    int tope= datos.length;
    if(index>= 0 && index< tope)
      regresar= datos[index].toString().substring(datos[index].toString().indexOf("=")+ 1);
    return regresar;  
  }

  public Object getObject(int index) {
    Object regresar= null;
    Object[] datos = entrySet().toArray();
    int tope= datos.length;
    if(index>= 0 && index< tope) {
      regresar= datos[index].toString().substring(0, datos[index].toString().indexOf("="));
      regresar= get(regresar);
    } // if  
    return regresar;  
  }
  
  public Object[] getValues() {
    return entrySet().toArray();
  }

  public String[] getNames() {
    Object[] objects = keySet().toArray();
    String[] regresar= new String[objects.length];
    for (int x= 0; x< objects.length; x++)
      regresar[x]= objects[x].toString();
    return regresar;
  }
  
  public String getName(int index) {
    String regresar = null;
    Object[] nombres= keySet().toArray();
    int tope= nombres.length;
    if(index>= 0 && index< tope)
      regresar= nombres[index].toString();
    return regresar;  
  }
  
  public Double getDouble(String columna) {
    return Double.parseDouble(getField(columna));
  }
  
  public int getInt(String columna) {
    return Integer.parseInt(getField(columna));
  }
  
  public String getClob(String name) {
    String regresar= null;
    try  {
      if(containsKey(name.toUpperCase()) && get(name.toUpperCase())!= null)
        regresar= ((Clob)get(name.toUpperCase())).getSubString(1,(int)((Clob)get(name.toUpperCase())).length());  
    } catch (Exception ex)  {
      ex.printStackTrace();
    } finally  {
    }
    
    
    return regresar;
  }

}
