package sia.utilerias.mail;

import java.util.Properties;

import javax.mail.Transport;

public class AvisosTesoreria extends Avisos {
  
  public AvisosTesoreria() {
  }
  
  public void enviarAviso(String mensaje) {
    setMensaje(mensaje);
    enviaCorreos();
  }
  
  
}
