package sia.libs.correo;

import java.util.*;
import javax.activation.*;
import javax.mail.*;

import javax.mail.internet.*;
import java.io.*;
import sia.libs.formato.Error;



public class Envio {
  
  public static void asuntoMensaje(String de, String para) {
    envia(de, para, null, "asunto", "contenido", null, false, "application/pdf");
  }
  
  public static void asuntoMensaje(String de, String para, String asunto, String contenido) {
    envia(de, para, null, asunto, contenido, null, false, "application/pdf");
  }
  
  public static void asuntoMensaje(String de, String para, String asunto, String contenido, boolean textoHTML) {
    envia(de, para, null, asunto, contenido, null, textoHTML, "application/pdf");
  }
  
  public static void asuntoMensaje(String de, String para, ByteArrayOutputStream anexo) {
    envia(de, para, anexo, "asunto", "contenido", "nombre_anexo", false, "application/pdf");
  }
  
  public static void asuntoMensaje(String de, String para, ByteArrayOutputStream anexo, String asunto) {
    envia(de, para, anexo, asunto, "contenido", "nombre_anexo", false, "application/pdf");
  }
  
  public static void asuntoMensaje(String de, String para, ByteArrayOutputStream anexo, String asunto, String contenido) {
    envia(de, para, anexo, asunto, contenido, "nombre_anexo", false, "application/pdf");
  }
  
  public static void asuntoMensaje(String de, String para, ByteArrayOutputStream anexo, String asunto, String contenido, String adjunto) {
    envia(de, para, anexo, asunto, contenido, adjunto, false, "application/pdf");
  }
  
  public static void asuntoMensaje(String de, String para, ByteArrayOutputStream anexo, String asunto, String contenido, String adjunto, boolean textoHTML) {
    envia(de, para, anexo, asunto, contenido, adjunto, textoHTML, "application/pdf");
  }

  public static void asuntoMensaje(String de, String para, ByteArrayOutputStream anexo, String asunto, String contenido, String adjunto, boolean textoHTML, String formato) {
    envia(de, para, anexo, asunto, contenido, adjunto, textoHTML, formato);
  }
  
  private static void envia(String de, String para, ByteArrayOutputStream anexo, String asunto, String contenido, String adjunto, boolean textoHTML, String formato) {
    try {
      Properties properties = new Properties();
      properties.put("mail.smtp.host", "10.1.8.102");
      Session session = Session.getInstance(properties, null);
      MimeMessage mimemessage = new MimeMessage(session);
      InternetAddress internetaddressDe  = new InternetAddress(de);
      mimemessage.setFrom(internetaddressDe);
      // SI SON VARIOS CORREOS TIENEN QUE ESTAR SEPARADOS POR COMAS Y SIN ESPACIOS EN BLANCO
      StringTokenizer st= new StringTokenizer(para, ",");
      Address[] internetaddressPara= new InternetAddress[st.countTokens()];
      int contador= -1;
      while(st.hasMoreTokens()) {
        internetaddressPara[++contador]= new InternetAddress(st.nextToken());
      } // while
      mimemessage.addRecipients(javax.mail.Message.RecipientType.TO, internetaddressPara);
      mimemessage.setSubject(asunto);
      MimeBodyPart mbp2= new MimeBodyPart();
      MimeBodyPart mbp1= new MimeBodyPart();
      Multipart multipart = new MimeMultipart();        
      DataSource ds =  null;
      if (anexo== null){
        if (textoHTML) {
          mimemessage.setContent(contenido, "text/html");
          mbp1.setContent(contenido, "text/html");
        }
        else{ 
          mimemessage.setText(contenido);
          mbp1.setText(contenido);  
        }
        if (adjunto!=null)
          ds= new FileDataSource(adjunto);         
        multipart.addBodyPart(mbp1);   
           
      }     
      else {
         if (textoHTML) 
           mbp1.setContent(contenido, "text/html");
         else 
           mbp1.setText(contenido);
        // agrega archivo anexo al correo electronico
        ds= new ByteArrayDataSource(anexo.toByteArray(), formato, adjunto);
        multipart.addBodyPart(mbp1);     
        mbp2.setDataHandler(new DataHandler(ds));
        mbp2.setFileName(ds.getName());
        multipart.addBodyPart(mbp2);      
      }      
      mimemessage.setContent(multipart);      
      Transport.send(mimemessage);
    }
    catch (Exception e) {
      System.err.println(Error.getMensaje(Envio.class, "SIA-CONTA","sia.libs.correo.Envio.envia",e));
      //e.printStackTrace();
    }
  }

}
