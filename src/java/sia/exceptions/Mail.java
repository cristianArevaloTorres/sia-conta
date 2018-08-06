package sia.exceptions;

import java.io.*;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class Mail {

  protected String de;
  protected String para;

  public void dePara(String s, String s1) {
    de = s;
    para = s1;
  }

  public void error(String s, Exception exception) {
    envia(s, exception);
  }

  public void error(Exception exception) {
    envia("Error en la aplicacion", exception);
  }

  public void error(String s) {
    envia(s, "Error no capturado apropiadamente");
  }

  public void error(String s, String s1) {
    envia(s, s1);
  }

  private void envia(String s, String s1) {
    try {
      Properties properties = new Properties();
      properties.put("mail.smtp.host", "10.1.8.103");
      Session session = Session.getInstance(properties, null);
      MimeMessage mimemessage = new MimeMessage(session);
      InternetAddress internetaddress = new InternetAddress(de);
      mimemessage.setFrom(internetaddress);
      InternetAddress internetaddress1 = new InternetAddress(para);
      mimemessage.addRecipient(javax.mail.Message.RecipientType.TO, internetaddress1);
      mimemessage.setSubject(s);
      mimemessage.setText(s1);
      Transport.send(mimemessage);
    }
    catch (AddressException addressexception) {
      System.out.println("Error en la direcci\363n de correo");
    }
    catch (Exception exception) {
      System.out.println("Error en el env\355o del error por correo");
    }
  }

  private void envia(String s, Exception exception) {
    StringWriter stringwriter = new StringWriter();
    PrintWriter printwriter = new PrintWriter(stringwriter);
    exception.printStackTrace(printwriter);
    envia(s, stringwriter.toString());
  }

}
