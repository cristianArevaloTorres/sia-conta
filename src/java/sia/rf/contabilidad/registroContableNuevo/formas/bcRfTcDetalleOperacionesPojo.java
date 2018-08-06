package sia.rf.contabilidad.registroContableNuevo.formas;

import java.util.ArrayList;
import java.util.List;

import sia.rf.contabilidad.registroContableNuevo.bcConfiguracionRenglon;

public class bcRfTcDetalleOperacionesPojo {
  
  private String maestroOperacionId;
  private String cuentaContableId;
  private String operacionContableId;
  private String detalleOperacionId;
  
  // CALCULADOS NO NECESARIOS
  private String cuentaContable;
  private int nivel;
  private List<String> detalleCuenta;
  
  public bcRfTcDetalleOperacionesPojo() {
  }

  public void setMaestroOperacionId(String maestroOperacionId) {
    this.maestroOperacionId = maestroOperacionId;
  }

  public String getMaestroOperacionId() {
    return maestroOperacionId;
  }

  public void setCuentaContableId(String cuentaContableId) {
    this.cuentaContableId = cuentaContableId;
  }

  public String getCuentaContableId() {
    return cuentaContableId;
  }

  public void setOperacionContableId(String operacionContableId) {
    this.operacionContableId = operacionContableId;
  }

  public String getOperacionContableId() {
    return operacionContableId;
  }

  public void setDetalleOperacionId(String detalleOperacionId) {
    this.detalleOperacionId = detalleOperacionId;
  }

  public String getDetalleOperacionId() {
    return detalleOperacionId;
  }

  public void setCuentaContable(String cuentaContable) {
    this.cuentaContable = cuentaContable;
  }

  public String getCuentaContable() {
    return cuentaContable;
  }

  public void setNivel(int nivel) {
    this.nivel = nivel;
  }

  public int getNivel() {
    return nivel;
  }

  public void setDetalleCuenta(List<String> detalleCuenta) {
    this.detalleCuenta = detalleCuenta;
  }

  public List<String> getDetalleCuenta(List<bcConfiguracionRenglon> configuracion) {
    if( (cuentaContable != null && !cuentaContable.equals("")) && (nivel!=0) && detalleCuenta != null) {
      detalleCuenta = new ArrayList();
      for(int x =0; x < getNivel(); x++) {
        detalleCuenta.add(cuentaContable.substring(configuracion.get(x).getPosicion(),configuracion.get(x).getPosicion()+configuracion.get(x).getLongitud()));
      }
    }
    return detalleCuenta;
  }
}
