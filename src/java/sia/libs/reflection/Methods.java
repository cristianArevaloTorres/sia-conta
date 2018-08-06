package sia.libs.reflection;

import java.lang.reflect.Method;


public final class Methods {

  private Methods() {
  }

  public static Object getValue(Object component,
								String name) throws Exception {
	Object regresar = null;
	name =
name.substring(0, 1).toUpperCase().concat(name.substring(1, name.length()));
	Method method =
	  getMethodSearching(component.getClass(), "get".concat(name), new Class[] { });
	if (method != null)
	  regresar = method.invoke(component, new Object[] { });
	return regresar;
  }

  public static void setValue(Object component, String name, Object valor) {
	setValue(component, name, new Object[] { valor });
  }
  
  public static void callMethod(Object component, String name, Object[] values) throws Exception {
  Method method =
    getMethodSearching(component.getClass(), name, getParams(values));
  if (method != null)
    try {
    method.invoke(component, values);
    } catch (Exception ex) {
    throw ex;
    }
  }

  public static void setValue(Object component, String name, Object[] values) {
	name =
name.substring(0, 1).toUpperCase().concat(name.substring(1, name.length()));
	Method method =
	  getMethodSearching(component.getClass(), "set".concat(name), getParams(values));
	if (method != null)
	  try {
		method.invoke(component, values);
	  } catch (Exception ex) {
		ex.printStackTrace();
	  }
  }

    public static void setValue(Object component, String name, Class[] params, Object[] values) {
      try {
        Method method = component.getClass().getDeclaredMethod(name, params);
        if (method != null) {
          method.invoke(component, values);

        } // if 
      }
      catch (Exception e) {
        e.printStackTrace();
        //        searchInterface(component, name, params, values, e);
      } // try
    }
  private static Class[] getParams(Object[] values) {
	Class[] clases = new Class[values.length];
	for (int i = 0; i < values.length; i++) {
	  clases[i] = values[i].getClass();
	}
	return clases;
  }


  private static Method getMethodSearching(Class clase, String name,
										   Class[] params) {
	Method method = null;
	if (clase.toString().indexOf("java.lang.Object") == -1) {
	  try {
		method = clase.getDeclaredMethod(name, params);
	  } catch (Exception e) {
		method = getMethodSearching(clase.getSuperclass(), name, params);
		new Exception(e);
	  }
	}
	return method;
  }
  /*
  private static void searchInterface(Object component, String name, Class[] params, Object[] values, Exception e_) {
    boolean finded = false;
    try {
      Class[] interfaces = component.getClass().getInterfaces();
      for (Class base: interfaces) {
        try {
          Method method = base.getDeclaredMethod(name, params);
          finded=method != null;
          if (finded) {
            Object object = method.invoke(component, values);
          } // if
        }
        catch (Exception _e) {
          _e=null;
        } // try
      } // for
      if (!finded)
        throw new Exception("No se encontrol el metodo en las interfaces. !");
    }
    catch (Exception e) {
      //Error.mensaje(e);
	  e.printStackTrace();
    }
  }
  */

}
