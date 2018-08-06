package sia.rf.contabilidad.registroContable.procesos.polizas.servicios;

public class Haber  extends OperacionContable {

  public Haber(double importe) {
    super(1, importe, "abono", "frmRegistro:txtHaber","frmRegistro:txtReferenciaH");
  }
}
