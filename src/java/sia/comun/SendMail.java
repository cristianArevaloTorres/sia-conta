/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sia.comun;

import java.io.*;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class SendMail {

    public SendMail() {
    }

    public void dePara(String de, String para) {
        this.de = de;
        this.para = para;
    }

    public void asuntoMensaje(String asunto, Exception exception) {
        envia(asunto, exception);
    }

    public void asuntoMensaje(Exception exception) {
        envia("Error en la aplicacion", exception);
    }

    public void asuntoMensaje(String asunto) {
        envia(asunto, "Error no capturado apropiadamente");
    }

    public void asuntoMensaje(String asunto, String mensaje) {
        envia(asunto, mensaje);
    }

    private void envia(String asunto, String mensaje) {
        try {
            Properties properties = new Properties();
            properties.put("mail.smtp.host", "smtp.office365.com");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.port", "587");
            Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
                String username = "contactosia@senado.gob.mx";
                String password = "Temporal9";

                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });
            de = "contactosia@senado.gob.mx";
            MimeMessage mimemessage = new MimeMessage(session);
            InternetAddress internetaddress = new InternetAddress(de);
            mimemessage.setFrom(internetaddress);
            InternetAddress  internetaddress1 = new InternetAddress(para);
            mimemessage.addRecipient(javax.mail.Message.RecipientType.TO, internetaddress1);
            mimemessage.setSubject(asunto);
            mimemessage.setText(mensaje);
            Transport.send(mimemessage);
        } catch (AddressException addressException) {
            System.out.println("Error en la direccion de correo");
            System.out.println(addressException.toString());
            //addressException.printStackTrace(System.err);
        } catch (Throwable exception) {
            System.out.println("Error en el envio del error por correo");
            System.out.println(exception.toString());
            //exception.printStackTrace(System.err);
        }
    }

    private void envia(String asunto, Exception exception) {
        StringWriter stringwriter = new StringWriter();
        PrintWriter printwriter = new PrintWriter(stringwriter);
        exception.printStackTrace(printwriter);
        envia(asunto, stringwriter.toString());
    }
    protected String de;
    protected String para;
    protected String para2;
}