package sia.exceptions.acceso;

import sia.exceptions.Except;

public class AccesoError extends Except {

    public AccesoError(Throwable ex) {
        this(ex, "", "", "");
    }

    public AccesoError(Throwable ex, String usuario, String sistema, String ip) {
        super(ex);
        mensaje = "<p style='color: rgb(255, 0, 0);'><font color='#ff0000' size='3'><strong>OCURRIO UN ERROR EN EL SISTEMA</strong></font></p>\n"
                + "<font size='2'><strong>usuario:</strong> :usuario<br /></font>\n"
                + "<font size='2'><strong>sistema:</strong> :sistema<br /></font>\n"
                + "<font size='2'><strong>ip:</strong>:ip</font><br />\n"
                + "<p style='font-weight: bold;'><font color='#000000' size='2'>A continuacion se muestran los detalles&nbsp; .....</font></p>\n"
                + "      <blockquote dir='' ltr='' style='' margin-right:='' 0px?=''> \n"
                + "<p><font size='2'>:detalle</font></p>\n"
                + " \n"
                + "   </blockquote>   \n"
                + "<p><font size='2'>Favor de verificar su programaci&oacute;n</font></p>\n"
                + " \n"
                + "<p><font size='2'>atte:</font> <br /><font 2='' size=''><strong>Administrador del sistema</strong></font></p>";
        //   super.mensaje = mensaje;
        setTo("luz.lopez@senado.gob.mx,jorgeluis.perez@senado.gob.mx");
        setSubject("Error de acceso al SIAFM - ".concat(ex.getMessage() != null ? ex.getMessage() : "NME"));
        addParam("usuario", usuario);
        addParam("sistema", sistema);
        addParam("ip", ip);
        //enviarMensaje();
    }
}
