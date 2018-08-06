package sia.exceptions.contabilidad.formas;

import sia.exceptions.Except;

public class FormasError extends Except {

  public FormasError(Throwable ex) {
    super(ex);
    setTo("jorgeluis.perez@senado.gob.mx,luz.lopez@senado.gob.mx,claudia.macariot@senado.gob.mx,amartinez.montiel@senado.gob.mx");
    setSubject("FORMAS_CONTABLES - ".concat(ex.getMessage()!=null ? ex.getMessage() : "NME"));
    addParam("error",getSubject());
    enviarMensaje();
  }
}
