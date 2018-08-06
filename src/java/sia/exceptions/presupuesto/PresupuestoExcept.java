package sia.exceptions.presupuesto;

import javax.servlet.http.HttpServletRequest;
import sia.exceptions.Except;

public class PresupuestoExcept extends Except {

    public PresupuestoExcept(Throwable ex) {
        this(ex, null);
    }

    public PresupuestoExcept(Throwable ex, HttpServletRequest request) {
        super(ex, request);
        setTo("salvador.munoz@senado.gob.mx,jesus.urraca@senado.gob.mx,amartinez.montiel@senado.gob.mx");
        setSubject("Presupuesto - ".concat(ex.getMessage() != null ? ex.getMessage() : "NME"));
        enviarMensaje();
    }
}
