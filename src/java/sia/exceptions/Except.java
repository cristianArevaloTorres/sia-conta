package sia.exceptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import javax.servlet.http.HttpServletRequest;
import sia.beans.seguridad.Autentifica;
import sia.db.dao.DaoFactory;
import sia.db.sql.SentennciasSE;
import sia.utilerias.mail.AvisosTesoreria;

public class Except {

    private Throwable ex;
    private String to;
    private String subject;
    private Map parametros;
    protected String mensaje;/*="<p>Ocurrio el siguiente error en el sistema:<br> <font color='#ff0000' size='3'><strong>:error</strong></font><br>causa:<font color='#ff0000' size='3'><strong>:causa</strong></font></p>\n " + 
     "    <p><font color='#000000' size='2'>A continuacion se muestran los detalles&nbsp; .....</font></p>\n" + 
     "    <blockquote dir='ltr' style='MARGIN-RIGHT: 0px'><p><font size='2'>:detalle</font></p> </blockquote>\n" + 
     "    <p><font size='2'>Favor de verificar su programaci&oacute;n</font></p>\n" + 
     "    <p><font size='2'>atte:</font> <br /><font size='2'><strong>Administrador del sistema</strong></font></p>";*/


    public Except(String mensaje, Throwable e) {
        this(e, null);
        this.ex = e;
        setTo("amartinez.montiel@senado.gob.mx");
        this.mensaje = mensaje;

    }

  public Except(Throwable e) {
    this(e,null);
  }
  
  private String getCause(Throwable e) {
    String reg="<br><br> - ";
    if(e.getCause()!=null) {
      //System.out.println(e.getCause());
      reg = reg.concat(Matcher.quoteReplacement(e.getCause().toString()));
      reg = reg.concat(Matcher.quoteReplacement(getCause(e.getCause())));
    } else {reg="";}
    return ponerAcentos(reg,true);
    
  }
  
  private String ponerAcentos(String cadena, boolean acentos) {
    if(acentos) {
      cadena = cadena.replaceAll("á","&aacute;");
      cadena = cadena.replaceAll("é","&eacute;");
      cadena = cadena.replaceAll("í","&iacute;");
      cadena = cadena.replaceAll("ó","&oacute;");
      cadena = cadena.replaceAll("ú","&uacute;");
      cadena = cadena.replaceAll("Á","&Aacute;");
      cadena = cadena.replaceAll("É","&Eacute;");
      cadena = cadena.replaceAll("Í","&Iacute;");
      cadena = cadena.replaceAll("Ó","&Oacute;");
      cadena = cadena.replaceAll("Ú","&Uacute;");
      cadena = cadena.replaceAll("ñ","&ntilde;");
      cadena = cadena.replaceAll("N","&Ntilde;");
    }
    return cadena;
  }
  
  public Except(Throwable e, HttpServletRequest request) {
    this.ex=e;
    String causa=null;
    addParam("error",ex.getMessage()!=null ? Matcher.quoteReplacement(ex.getMessage()) : "NME");
    causa = getCause(e);
    System.out.println(causa);
    addParam("causa",causa != null ? Matcher.quoteReplacement(causa) : "NME");
    //addParam("causa",ex.getCause()!=null ? ex.getCause().toString() : "NME");
    Autentifica aut = null;
    if(request!=null) {
      aut = (Autentifica)request.getSession().getAttribute("Autentifica");
      addParam("usuario",aut.getNombre());
      addParam("servidor",request.getServerName()+" : "+request.getContextPath());
    }
    
  }
  
  public void validarSubject() {
    if(subject.toUpperCase().indexOf("INSERT") < 0 || subject.toUpperCase().indexOf("DELETE") < 0 || subject.toUpperCase().indexOf("UPDATE") < 0) 
      subject = "Asunto erroneo - IDU";
  }
  
  public void addParam(String k, Object v) {
    if(parametros == null)
      parametros = new HashMap();
    if(k != null && v != null)
      parametros.put(k,v);
  }
  
  public void liberaParametros() {
    Iterator it=parametros.keySet().iterator();
    List keys = new ArrayList();
    while(it.hasNext()) {
      keys.add(it.next());
    }
    for(Object key:keys) 
      parametros.remove(key);
    parametros = null;
  }
  
  
  
  private void reemplazarParametros() {
    if(parametros!=null) {
      Iterator it = parametros.keySet().iterator();
      String llave;
      while(it.hasNext()) {
        llave = (String)it.next();
        mensaje = mensaje.replaceAll(":"+llave,(String)parametros.get(llave));
      }
    }
  }
  
  private void getMensaje() throws Exception {
    if(mensaje == null) {
      SentennciasSE sen = null;
      try  {
        sen = new SentennciasSE(DaoFactory.CONEXION_SEGURIDAD);
        mensaje = sen.getComando("exceptions.select.mensajeErrorMorado",parametros);
      } catch (Exception ex)  {
        throw new Exception("Error al obtener el mensaje de error para el envio de correo");
      } finally  {
        sen = null;
      }
    }
    
  }
  
  protected void enviarMensaje() {
    try  {
      validarSubject();
      String compuesto = null;
      AvisosTesoreria aviso = new AvisosTesoreria();
      aviso.setTo(getTo());
      aviso.setSubject(getSubject());
      StackTraceElement[] stes = ex.getStackTrace();
      StringBuffer sb = new StringBuffer();
      for(StackTraceElement ste : stes) {
        //sb.append("linea ").append(ste.getLineNumber()).append(ste.getClassName()).append(ste.getMethodName());
         sb.append(ste.getClassName()).append("&nbsp;").append("linea ").append(ste.getLineNumber()).append("&nbsp;") .append(ste.getMethodName()).append("<br>");
      }
      compuesto = Matcher.quoteReplacement(sb.toString());
      addParam("detalle",compuesto);
      getMensaje();
      //mensaje = mensaje.replace(":detalle",compuesto);
      //reemplazarParametros();
      aviso.setHtml(true);
      aviso.enviarAviso(mensaje);
    } catch (Exception ex)  {
      System.out.println("Error al enviar el correo -".concat(getSubject()));
    } finally  {
    }
  }
  
  public static String getLocalAdrr(HttpServletRequest request) {
    return request.getLocalAddr().equals("127.0.1.1")?"10.26.4.15":request.getLocalAddr();
  }
  
  public static String getRuta(HttpServletRequest request) {
    return "http://".concat(request.getLocalAddr()).concat(":").concat(String.valueOf(request.getLocalPort())).concat(request.getContextPath());
    //return "http://".concat("10.26.4.15").concat(":").concat(String.valueOf(request.getLocalPort())).concat(request.getContextPath());
  }
  
  

  public void setTo(String to) {
    this.to = to;
  }

  public String getTo() {
    return to;
  }

  public void setSubject(String subject) {
    this.subject = subject.length() > 101 ? subject.substring(0,100) : subject;
  }

  public String getSubject() {
    return subject;
  }

  public void setEx(Throwable ex) {
    this.ex = ex;
  }

  public Throwable getEx() {
    return ex;
  }
}
