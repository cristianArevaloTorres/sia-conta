package sia.ws.publicar.contabilidad.definicion;

import java.util.List;

import sia.rf.contabilidad.registroContable.formas.servicios.CuentasForma;

public class FormaLista {
  private int id;
  private List<CuentasForma> cuentas;
  
  public FormaLista(int id, List<CuentasForma> cuentas) {
    setId(id);
    setCuentas(cuentas);
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }

  public void setCuentas(List<CuentasForma> cuentas) {
    this.cuentas = cuentas;
  }

  public List<CuentasForma> getCuentas() {
    return cuentas;
  }
}
