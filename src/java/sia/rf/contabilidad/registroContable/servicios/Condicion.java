package sia.rf.contabilidad.registroContable.servicios;

import sia.libs.formato.Cadena;
import sia.libs.formato.Rangos;


public class Condicion extends Rangos {

  public Condicion() {
    this("D1.D9,I15.I19,E12");
  }

  public Condicion(String criterios) {
    super(criterios);
  }

  public String getSentencia(String tipo, String consecutivo) {
    StringBuffer sb= new StringBuffer();
    sb.append("(");
    for(Rango rango: getElementos()) {
      sb.append("(");
      if(rango.getTipo()!= null && rango.getTipo().length()> 0) {
        sb.append(tipo);
        sb.append("=");
        sb.append(Cadena.comillas(rango.getTipo()));
        sb.append(" and ");
      } // if
      
      sb.append("(");
      sb.append(consecutivo);
      if(rango.isRango()) {
        sb.append(">= ");
        sb.append((int)rango.getMin());
        sb.append(" and ");
        sb.append(consecutivo);
        sb.append("<= ");
        sb.append((int)rango.getMax());
      }
      else {
        sb.append("= ");
        sb.append(rango.getMin());
      } // if 
      sb.append(")");
      sb.append(")");
      sb.append(" or ");
    } // for
    sb.delete(sb.length()- 4, sb.length());
    sb.append(")");
    return sb.toString();
  }

  public static void main(String[] args) {
    Condicion condicion= new Condicion("1.9,15.19,E12");
    System.out.println(condicion.getSentencia("a.abreviatura", "b.consecutivo"));
  }

}
