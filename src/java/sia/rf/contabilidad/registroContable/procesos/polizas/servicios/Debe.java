package sia.rf.contabilidad.registroContable.procesos.polizas.servicios;

public class Debe  extends OperacionContable {

  public Debe(double importe) {
    super(0, importe, "cargo", "frmRegistro:txtDebe","frmRegistro:txtReferenciaD");
  }

}

