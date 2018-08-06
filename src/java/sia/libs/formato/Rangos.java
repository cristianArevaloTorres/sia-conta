package sia.libs.formato;

import sia.libs.formato.Error;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import sia.rf.contabilidad.registroContable.servicios.Rango;

public class Rangos {
  
  private List <Rango> elementos;
  private String criterios;
  
  public Rangos() {this("12,13.15,18.20");
  }

public Rangos(String criterios) {
    setElementos(new ArrayList <Rango> ());
    setCriterios(criterios);
  }

  private void setElementos(List elementos) {
    this.elementos = elementos;
  }

  public List <Rango> getElementos() {
    return elementos;
  }

  public void setCriterios(String criterios) {
    if(criterios!= null) {
      this.criterios = criterios;
      proceso();
    }  
  }

  private boolean isCodigo(String valor) {
    return Character.isLetter(valor.charAt(0));
  }
  
  private double getDouble(String valor) {
    double regresar= 0;
    try  {
      regresar= Double.parseDouble(valor);
    } 
    catch (Exception e)  {
      Error.mensaje(this, e, "Contabilidad", "getDouble", e.getMessage());
    }
    return regresar;
  }
  
  private String getCriterios() {
    return criterios;
  }
  
  private void periodo(String ... valores) {
    getElementos().add(sencillo(valores[0], valores[1]));
  }
  
  private Rango sencillo(String valor) {
    if(isCodigo(valor)) 
      return new sia.rf.contabilidad.registroContable.servicios.Rango(valor.substring(0, 1), getDouble(valor.substring(1)));
    else
      return new Rango(getDouble(valor));
  }

  private Rango sencillo(String inicio, String fin) {
    if(isCodigo(inicio)) 
      return new sia.rf.contabilidad.registroContable.servicios.Rango(inicio.substring(0, 1), getDouble(inicio.substring(1)), getDouble(fin.substring(1)));
    else
      return new Rango(getDouble(inicio), getDouble(fin));
  }
  
  private void proceso() {
    getElementos().clear();
    StringTokenizer st= new StringTokenizer(getCriterios(), ",");
    while (st.hasMoreTokens())  {
      String elemento = st.nextToken();
      String[] valores= elemento.split("[.]");
      if(valores.length> 1)
        periodo(valores);
      else
        getElementos().add(sencillo(valores[0]));
    } // while
  }
  
  public boolean isDentro(double valor) {
    for(Rango rango: getElementos()) {
      if(rango.isDentro(valor))
        return true;
    } // for
    return false;
  }

  public static void main(String[] args) {
    Rangos rangos= new Rangos("D1.D9,I15.I19,E12");
    System.out.println(rangos.isDentro(11));
    System.out.println(rangos.isDentro(14));
  }
  
}
