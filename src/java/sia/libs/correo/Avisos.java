package sia.libs.correo;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class Avisos {
  private String form;
  private String to;
  private String cc;
  private String cco;
  private String titulo;
  private String machote;
  private String html;
  private Map parametros;

  public Avisos() { parametros = new HashMap(); }
  public void setParametros(Map parametros) { this.parametros = parametros; }
  public Map getParametros() { return parametros; }
  public void setTo(String to) { this.to = to; }
  public String getTo() { return to; }
  public void setCc(String cc) { this.cc = cc; }
  public String getCc() { return cc; }
  public void setForm(String form) { this.form = form; }
  public String getForm() { return form; }
  public void setCco(String cco) { this.cco = cco; }
  public String getCco() { return cco; }
  public void setTitulo(String titulo) { this.titulo = titulo; }
  public String getTitulo() { return titulo; }
  public void setMachote(String machote) { this.machote = machote; }
  public String getMachote() { return machote; }
  public void setHtml(String html) { this.html = html; }
  public String getHtml() { return html; }

private bcAvisos getAviso(Connection con, String evento) throws Exception {
 bcAvisos aviso = null;
 try {
  aviso = new bcAvisos();
  aviso.setEvento(evento);
  aviso.select(con);
 }
 catch (Exception ex) { throw new Exception("error al obtener el mensaje"); }
 finally { }
 return aviso;
}

private String formatearMensaje(String mensaje) throws Exception {
 if (mensaje == null || mensaje.equals(""))
 throw new Exception("Mensaje vacio");
 if (parametros != null) {
  Iterator keys = getParametros().keySet().iterator();
  String key = null;
  while (keys.hasNext()) {
   key = (String)keys.next();
   mensaje =
   mensaje.replaceAll(":".concat(key), parametros.get(key).toString());
  }
 }
 return mensaje;
}

private void agregarDestinatarios(MimeMessage mimemessage,String cadenaDestinatarios,Message.RecipientType tipo) throws MessagingException {
 InternetAddress internetaddress1 = new InternetAddress();
 if (cadenaDestinatarios != null && !cadenaDestinatarios.equals("")) {
  String[] ctas = cadenaDestinatarios.split(",");
  for (int i = 0; i < ctas.length; i++) {
   internetaddress1.setAddress(ctas[i]);
   mimemessage.addRecipient(tipo, internetaddress1);
  }
 }
}

private MimeMultipart colocarTexto(String cuerpo,boolean html) throws MessagingException {
 MimeMultipart multipart = new MimeMultipart();
 MimeBodyPart mbp = new MimeBodyPart();
 if (html) mbp.setContent(cuerpo, "text/html");
 else mbp.setText(cuerpo);
 multipart.addBodyPart(mbp);
 return multipart;
}

public void enviaCorreos() {
 try {
  Properties properties = new Properties();
  //fin de uso 31/10/2012 properties.put("mail.smtp.host", "10.1.32.15");
  properties.put("mail.smtp.host", "w-appintrasmtp.senado.gob.mx");
  Session session = Session.getInstance(properties, null);
  MimeMessage mimemessage = new MimeMessage(session);
  InternetAddress internetaddress = new InternetAddress(getForm());
  mimemessage.setFrom(internetaddress);
  agregarDestinatarios(mimemessage, getTo(), Message.RecipientType.TO);
  agregarDestinatarios(mimemessage, getCc(), Message.RecipientType.CC);
  agregarDestinatarios(mimemessage, getCco(), Message.RecipientType.BCC);
  mimemessage.setSubject(getTitulo());
  mimemessage.setContent(colocarTexto(getMachote(),
  getHtml().equals("1")));
  System.out.println("Titulo:"+ getTitulo() + "\nMENSAJE:\n"+ getMachote());
  Transport.send(mimemessage);
 }
 catch (AddressException addressexception) {
  System.out.println("Error en la direccion de correo");
  System.out.println(addressexception.toString());
  //addressexception.printStackTrace();
 }
 catch (Exception exception) {
  System.out.println("Error en el envio del error por correo");
  System.out.println(exception.toString());
  //exception.printStackTrace();
 }
}

public void envia(Connection con, String evento) throws Exception {
 bcAvisos aviso = getAviso(con, evento);
 aviso.setMachote(formatearMensaje(aviso.getMachote()));
 aviso.setTitulo(formatearMensaje(aviso.getTitulo()));
 setTitulo(aviso.getTitulo());
 setMachote(aviso.getMachote());
 setHtml(aviso.getHtml());
 setForm(aviso.getCtaorigen());
 setTo(aviso.getListadestinatarios());
 setCc(aviso.getCopiapara());
 setCco(aviso.getCopiaOculta());
}
  
public void addTo(String destinatario) {
 if(to==null) to = destinatario.concat(",");
 else to = to.concat(destinatario).concat(",");
}

public void addCc(String destinatario) {
 if(cc==null) cc = destinatario.concat(",");
 else cc = cc.concat(destinatario).concat(",");
}
 
}
