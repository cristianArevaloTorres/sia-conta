package sia.rf.contabilidad.registroContableNuevo.formas;

import java.util.ArrayList;
import java.util.List;

public class bcRfTcSecuenciaFormaPojo {
  
  public bcRfTcSecuenciaFormaPojo() {
  }
  
  private String secuenciaFormaId;
  private String operacionContableId;
  private String formaContableId;
  private String crearCuentaContable;
  private String acumularImporte;
  private String movible;
  private List<bcRfTcConfiguraFormaPojo> detConf;
  
  public void setSecuenciaFormaId(String secuenciaFormaId) {
    this.secuenciaFormaId = secuenciaFormaId;
  }

  public String getSecuenciaFormaId() {
    return this.secuenciaFormaId;
  }

  public void setOperacionContableId(String operacionContableId) {
    this.operacionContableId = operacionContableId;
  }

  public String getOperacionContableId() {
    return this.operacionContableId;
  }

  public void setFormaContableId(String formaContableId) {
    this.formaContableId = formaContableId;
  }

  public String getFormaContableId() {
    return this.formaContableId;
  }

  public void setCrearCuentaContable(String crearCuentaContable) {
    this.crearCuentaContable = crearCuentaContable;
  }

  public String getCrearCuentaContable() {
    return this.crearCuentaContable;
  }

  public void setAcumularImporte(String acumularImporte) {
    this.acumularImporte = acumularImporte;
  }

  public String getAcumularImporte() {
    return this.acumularImporte;
  }

  public void setMovible(String movible) {
    this.movible = movible;
  }

  public String getMovible() {
    return this.movible;
  }

  public void setDetConf(List<bcRfTcConfiguraFormaPojo> detConf) {
    this.detConf = detConf;
  }

  public List<bcRfTcConfiguraFormaPojo> getDetConf() {
    if(detConf == null)
      detConf = new ArrayList();
    return detConf;
  }
}
