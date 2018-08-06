package sia.utilerias.mail;

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

    private String from; // direccion de la persona que manda el correo
    private String to; // direcciones de correo electronico separados por coma
    private String cc; // direcciones de correo electronico separados por coma
    private String mensaje; // mensaje del correo
    private String subject; // titulo del correo
    private boolean html;
    private Map parametros;

    public Avisos() {
        setHtml(true);
        parametros = new HashMap();
        setFrom("siafm@senado.gob.mx");
        setSubject("Titulo no especificado");
        setMensaje("Mensaje no establecido");
    }

    public void addParam(String k, Object v) {
        if (parametros == null) {
            parametros = new HashMap();
        }
        if (k != null && v != null) {
            parametros.put(k, v);
        }
    }

    public void setParametros(Map parametros) {
        this.parametros = parametros;
    }

    public Map getParametros() {
        return parametros;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTo() {
        return to;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getCc() {
        return cc;
    }

    /*private bcAvisos getAviso(Connection con, String evento) throws Exception {
     bcAvisos aviso = null;
     try  {
     aviso = new bcAvisos();
     aviso.setEvento(evento);
     aviso.select(con);
     } catch (Exception ex)  {
     throw new Exception("error al obtener el mensaje");
     } finally  {
     }
     return aviso;
     }*/
    protected String formatearMensaje(String mensaje) throws Exception {
        if (mensaje == null || mensaje.isEmpty()) {
            throw new Exception("Mensaje vacio");
        }
        if (parametros != null) {
            Iterator keys = getParametros().keySet().iterator();
            String key = null;
            while (keys.hasNext()) {
                key = (String) keys.next();
                mensaje = mensaje.replaceAll(":".concat(key), parametros.get(key).toString());
            }
        }
        return mensaje;
    }

    protected void agregarDestinatarios(MimeMessage mimemessage, String cadenaDestinatarios, Message.RecipientType tipo) throws MessagingException {
        InternetAddress internetaddress1 = new InternetAddress();
        if (cadenaDestinatarios != null && !cadenaDestinatarios.isEmpty()) {
            String[] ctas = cadenaDestinatarios.split(",");
            for (int i = 0; i < ctas.length; i++) {
                internetaddress1.setAddress(cadenaDestinatarios);
                mimemessage.addRecipient(tipo, internetaddress1);
            }
        }
    }

    protected MimeMultipart colocarTexto(String cuerpo, boolean html) throws MessagingException {
        MimeMultipart multipart = new MimeMultipart();
        MimeBodyPart mbp = new MimeBodyPart();
        if (html) {
            mbp.setContent(cuerpo, "text/html");
        } else {
            mbp.setText(cuerpo);
        }
        multipart.addBodyPart(mbp);
        return multipart;
    }

    protected void enviaCorreos() {
        try {
            Properties properties = new Properties();
            //fin de uso el 2005/04/20 properties.put("mail.smtp.host", "10.1.8.121");
            properties.put("mail.smtp.host", "w-appintrasmtp.senado.gob.mx");
            Session session = Session.getInstance(properties, null);
            MimeMessage mimemessage = new MimeMessage(session);
            InternetAddress internetaddress = new InternetAddress(getFrom());
            mimemessage.setFrom(internetaddress);
            agregarDestinatarios(mimemessage, getTo(), Message.RecipientType.TO);
            agregarDestinatarios(mimemessage, getCc(), Message.RecipientType.CC);
            mimemessage.setSubject(getSubject());
            mimemessage.setContent(colocarTexto(formatearMensaje(getMensaje()), isHtml()));
            //mimemessage.setText(aviso.getMachote());
            Transport.send(mimemessage);
        } catch (AddressException addressexception) {
            System.out.println("Error en la dirección de correo");
            System.out.println(addressexception.toString());
            addressexception.printStackTrace();
        } catch (Exception exception) {
            System.out.println("Error en el envío del error por correo");
            System.out.println(exception.toString());
            exception.printStackTrace();
        }
    }

    /*public void envia(Connection con, String evento) throws Exception {
     bcAvisos aviso = getAviso(con,evento);
     aviso.setMachote(formatearMensaje(aviso.getMachote()));
     aviso.setTitulo(formatearMensaje(aviso.getTitulo()));
     enviaCorreos(aviso);
     }*/
    public void setFrom(String from) {
        this.from = from;
    }

    public String getFrom() {
        return from;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    public void setHtml(boolean html) {
        this.html = html;
    }

    public boolean isHtml() {
        return html;
    }
}
