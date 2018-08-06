package sia.ws.publicar.contabilidad;

import javax.servlet.http.HttpServletRequest;
import org.apache.axis.MessageContext;
import org.apache.axis.transport.http.HTTPConstants;

import sia.libs.correo.Envio;
import sia.libs.formato.Encriptar;
import sia.libs.formato.Error;
import sia.libs.recurso.Contabilidad;

public class ValidaWs {
  
  private String clave;
  
  public ValidaWs(String clave) {
    this.setClave(clave);
  }

  public boolean verificaHost(){
    String clave         = null;
    String usuariosWs    = null;
    boolean claveOk      = false;
    try{
      Encriptar encriptado = new Encriptar();
      clave=encriptado.desencriptar(getClave(), getIP());
      usuariosWs = Contabilidad.getPropiedad("ws.usuarios.consumir");
    //  if(usuariosWs.indexOf(clave)!=-1)
       claveOk=true;
       System.out.println("Se invoco verificaHost... ");
     }
     catch(Exception e){
       System.out.println(e.getMessage());
       e.printStackTrace();
       Error.mensaje(e,"SIAFM");
     }
    return claveOk; 
  }

  private void setClave(String clave) {
    this.clave = clave;
  }

  public String getClave() {
    return clave;
  }
  
  private String getIP(){
   return getRequest().getRemoteAddr();
  }
  
  private HttpServletRequest getRequest() {
    MessageContext context = MessageContext.getCurrentContext();
    HttpServletRequest req = (HttpServletRequest) context.getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST);    
    return req;
  }
  
  public void enviaNotitificacion(String mensaje){
      StringBuffer sb= new StringBuffer();
      sb.append("<html><title><head></head></title><body>");
      sb.append("<br><strong>");
      sb.append("Notificacion webservice contabilidad  ");
      sb.append("</strong><br>");
      sb.append("<br><strong>");
      sb.append(mensaje);
      sb.append("</strong><br>");
      sb.append("<br>");
      Envio.asuntoMensaje( Contabilidad.getPropiedad("notificacion.webService.emisor") ,  Contabilidad.getPropiedad("notificacion.webService.destinatarios")  ,null  ,"Sistema Contabilidad - Armonizado",sb.toString(),null,true);
  }
  
  
  
  
}
  
  

