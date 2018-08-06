package sia.scriptlets;

import sia.libs.formato.Error;

import net.sf.jasperreports.engine.JRScriptletException;

import sia.libs.formato.Letras;

public class CantidadLetras extends BarraProgreso {

  public String getLetras(String cantidad) throws JRScriptletException {
    String regresar= "";
    try {
      Letras letras= new Letras();
      regresar= letras.getMoneda(cantidad, false);
      letras= null;
    }
    catch(Exception e) {
      System.err.println(Error.getMensaje(this, "SIAFM", "getLetras", e.getMessage()));
    }; // try
    return regresar;
  }

  public String getPorcentaje(String cantidad) throws JRScriptletException {
    String regresar= "";
    try {
      Letras letras= new Letras();
      regresar= letras.getPorciento(cantidad);
      letras= null;
    }
    catch(Exception e) {
      System.err.println(Error.getMensaje(this, "SIAFM", "getPorciento ", e.getMessage()));
    }; // try
    return regresar;
  }

}
