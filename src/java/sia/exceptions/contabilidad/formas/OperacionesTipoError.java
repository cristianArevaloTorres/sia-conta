package sia.exceptions.contabilidad.formas;

import sia.exceptions.Except;

public class OperacionesTipoError extends Except {

    public OperacionesTipoError(Throwable ex) {
        super(ex);
        setTo("jorgeluis.perez@senado.gob.mx,luz.lopez@senado.gob.mx,claudia.macariot@senado.gob.mx,amartinez.montiel@senado.gob.mx");
        //setTo("amartinez.montiel@senado.gob.mx");
        setSubject("OPERACIONES_TIPO - ".concat(ex.getMessage() != null ? ex.getMessage() : "NME"));
        addParam("error", getSubject());
        enviarMensaje();
    }
}
