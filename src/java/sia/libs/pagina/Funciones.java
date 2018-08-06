package sia.libs.pagina;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.StringTokenizer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;
import sia.libs.progreso.PrintWriter;

import sia.libs.archivo.Eliminar;
import sia.libs.archivo.Patron;
import sia.libs.formato.Cadena;
import sia.libs.formato.Encriptar;
import sia.libs.formato.Error;
import sia.libs.formato.Fecha;
import sia.libs.formato.Numero;

public class Funciones {

/*  public String buscarEmpleado(String alias, String cadena){ //wakko
      StringBuffer like= null;
      if (!cadena.trim().equals("")){
        like= new StringBuffer();
        String[] busqueda = cadena.split(" ");
        int contador= 0;
        StringBuffer elemento= new StringBuffer();
        for(int i=0; i< busqueda.length; i++) {
          elemento.append(busqueda[i]);
          contador= i;
          while(contador+ 1< busqueda.length && busqueda[i].equals(busqueda[contador+ 1])) {
            contador++;
            elemento.append(" ").append(busqueda[contador]);
          } // while contador
          i= contador;
          if (i== busqueda.length- 1)
            like.append("  (").append(alias).append(".apellido_pat||' '||").append(alias).append(".apellido_mat||' '||").append(alias).append(".nombres) like '%").append(elemento.toString()).append("%' ");
          else
            like.append("  (").append(alias).append(".apellido_pat||' '||").append(alias).append(".apellido_mat||' '||").append(alias).append(".nombres) like '%").append(elemento.toString()).append("%' and ");
          elemento.delete(0, elemento.length());
        } // for i
        elemento= null;
        busqueda= null;
      } //if cadena
      return like.toString();
    } //buscarEmpleado
*/
  
  public static String getContexto(HttpServletRequest rqt) {
    return rqt.getContextPath().equals("/") ? "" : rqt.getContextPath();
  } // getContexto

  public void getHojaEstilo(Writer writer, String contexto) {
    try {
      PrintWriter out = new PrintWriter(writer);
      out.println("<base target=\"_self\">");
      out.println("<script language=\"javascript\">");
      out.println("  if (navigator.appName==\"Netscape\" && parseInt(navigator.appVersion) >= 4)");
      out.println("    document.write('<link rel=\"stylesheet\" type=\"text/css\" href=\"" + contexto +
                  "/Librerias/Estilos/sianetscape.css\">');");
      out.println("  else");
      out.println("    document.write('<link rel=\"stylesheet\" type=\"text/css\" href=\"" + contexto +
                  "/Librerias/Estilos/siaexplorer.css\">');");
      out.println("</script>");
    }
    catch (Exception e) {
      System.err.println(Error.getMensaje(this, "SIAFM", "getHojaEstilo", e));
    }
  }

  public void getHojaEstilo(Writer out, HttpServletRequest rqt) {
    getHojaEstilo(out, rqt.getContextPath().equals("/") ? "" : rqt.getContextPath());
  } //  InsertarHojaEstilo

  public void tituloPagina(Writer writer, String Titulo, String subTitulo, String Texto, boolean Linea) {
    tituloPagina(writer,Titulo,"",subTitulo,Texto,Linea);
  }
  
  public void tituloPagina ( Writer writer , String Modulo , String SubModulo , String CU , String accion ,
                           boolean Linea ) {
  try {
    PrintWriter out = new PrintWriter ( writer ) ;
    out.println ( "<table width='100%' border='0' class='sianoborder'>" ) ;
    //if ( SubModulo.length () != 0 ) {
      out.println ( "  <tr>" ) ;
      out.println ( "    <td  width='70%' class='sianoborder'><div align='left'><b>" + Cadena.getCadenaConAcute(SubModulo) +
                    "</b></div></td>" ) ;
      out.println ( "    <td width='30%' class='sianoborder'><div align='right'><b><font color='#003399'>" + Cadena.getCadenaConAcute(Modulo) +
                    "</font></b></div></td>" ) ;
      out.println ( "  </tr>" ) ;
    //}    
    out.println ( "  <tr>" ) ;
    out.println ( "    <td class='sianoborder'><b>" + Cadena.getCadenaConAcute(CU) + "</b></td>" ) ;
    if ( accion.length () != 0 ) {
      out.println ( "    <td class='sianoborder'><div align='right'><strong><font color='#00CCFF'>[" + Cadena.getCadenaConAcute(accion) +
                    "]</font></strong></div></td>" ) ;
    }    
    out.println ( "  </tr>" ) ;
    out.println ( "</table>" ) ;
    out.println ( ( Linea ? "<hr class='menu'>" : "" ) ) ;
  }
  catch ( Exception e ) {
    System.err.println ( Error.getMensaje ( this , "SIAFM" , "tituloPagina " , e ) ) ;
  }
}

public void msgError(JspWriter out, String msgError) {
    try {
      if (msgError == null)
        msgError = "La pagina tiene un error consulte al desarrollador. !";
      out.println("<script>");
      out.println("  var Msg= 'Se produjo una excepci�n:';");
      out.println("  Msg+='" + msgError.replace('\n', ' ').replace('"', '~').replace('\'', '~') + "';");
      out.println("  alert(Msg);");
      out.println("</script>");
    }
    catch (Exception e) {
      System.err.println(Error.getMensaje(this, "SIAFM", "msgError", e));
    } // try
  }

  public String regresarCampos(ResultSet mst, String campos, char caracter, String token) {
    String regresar = "";
    try {
      String valor = "";
      StringTokenizer st = new StringTokenizer(campos, token);
      while (st.hasMoreTokens()) {
        valor = st.nextToken();
        regresar += (mst.getString(valor) == null ? "" : mst.getString(valor)) + caracter;
      } // while
      if (regresar.length() == 0)
        regresar += caracter;
    }
    catch (SQLException eSQL) {
      System.err.println(Error.getMensaje(this, "SIAFM", "regresarCampos", eSQL.getMessage()));
    }
    catch (Exception e) {
      System.err.println(Error.getMensaje(this, "SIAFM", "regresarCampos", e.getMessage()));
    } // try

    return regresar.substring(0, regresar.length() - 1);
  } // regresarCampos

  public String regresarCampos(ResultSet mst, String campos, char caracter) {
    return regresarCampos(mst, campos, caracter, ",");
  } //usado por comboQuery

  public String comboQuery(Connection conection, JspWriter out, String sentencia, String oculto, String despliega,
                           String comparacion, String valor, char separador) throws Exception {
    return comboQuery(conection, out, sentencia, oculto, despliega, comparacion, valor, separador, false);
  } // comboQuery

  public String comboQuery(Connection conection, JspWriter out, String sentencia, String oculto, String despliega,
                           String comparacion, String valor, char separador, boolean sinMensaje) throws Exception {
    String regresar = valor;
    Statement estatuto = null;
    ResultSet crsTabla = null;
    try {
      estatuto = conection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      //      System.out.println("Combo especial=>"+ sentencia);
      crsTabla = estatuto.executeQuery(sentencia);
      crsTabla.last();
      if (crsTabla.getRow() > 0) {
        crsTabla.beforeFirst();
        while (crsTabla.next() && !crsTabla.getString(comparacion).equals(valor))
          if (!crsTabla.isAfterLast() && crsTabla.getString(comparacion).equals(valor)) {
            out.println("<option selected value=\"" + regresarCampos(crsTabla, oculto, ',') + "\">" +
                        regresarCampos(crsTabla, despliega, separador) + "</option>");
            regresar = valor;
          }
          else {
            crsTabla.beforeFirst();
            if (crsTabla.next())
              regresar = crsTabla.getString(comparacion);
          }
        crsTabla.beforeFirst();
        while (crsTabla.next()) {
          if (!crsTabla.getString(comparacion).equals(valor))
            out.println("<option value=\"" + regresarCampos(crsTabla, oculto, ',') + "\">" +
                        regresarCampos(crsTabla, despliega, separador) + "</option>");
        } // while
      }

      else {
        if (sinMensaje)
          out.println("<option value=''>Sin informaci�n</option>");
      }
      ; // if getRow
    }

    catch (SQLException eSQL) {
      System.err.println(Error.getMensaje(this, "SIAFM", "comboQuery", eSQL.getMessage()));
    }

    catch (Exception e) {
      System.err.println(Error.getMensaje(this, "SIAFM", "comboQuery", e.getMessage()));
    }

    finally {
      if (crsTabla != null)
        crsTabla.close();
      crsTabla = null;
      if (estatuto != null)
        estatuto.close();
      estatuto = null;
    }

    return regresar;
  }

  public String buscarDescripcion(Connection conection, String formato, String valor) throws Exception {
    return buscarDescripcion(conection, formato, valor, "");
  }

  public String buscarDescripcion(Connection conection, String formato, String valor,
                                  String tipoDato) throws Exception {
    String regresar = "";
    Statement estatuto = null;
    ResultSet crsTabla = null;
    try {
      String[] elementos = { "", "", "" };
      StringTokenizer st = new StringTokenizer(formato, "~");
      int contador = 0;
      while (st.hasMoreTokens()) {
        elementos[contador] = st.nextToken();
        //       System.out.println(elementos[contador]);
        contador++;
      }
      regresar = "=" + valor;
      if (tipoDato.equals("S")) {
        regresar = "='" + valor + "'";
      }
      else {
        try {
          int i = Integer.parseInt(valor);
          i = 0;
        }
        catch (NumberFormatException e) {
          regresar = "='" + valor + "'";
        } // try
      }
      estatuto = conection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
      //      System.out.println("[buscarDescripcion] Select * From "+ elementos[0] + " Where "+ elementos[1]+ regresar);
      crsTabla = estatuto.executeQuery("select * from " + elementos[0] + " where " + elementos[1] + regresar);
      crsTabla.beforeFirst();
      if (crsTabla.next())
        regresar = regresarCampos(crsTabla, elementos[2], ' ', "^");
      else
        regresar = valor;
    }
    catch (SQLException eSQL) {
      System.err.println(Error.getMensaje(this, "SIAFM", "buscarDescripcion", eSQL.getMessage()));
    }
    catch (Exception e) {
      System.err.println(Error.getMensaje(this, "SIAFM", "buscarDescripcion", e.getMessage()));
    }
    finally {
      try {
        if (crsTabla != null)
          crsTabla.close();
        crsTabla = null;
        if (estatuto != null)
          estatuto.close();
        estatuto = null;
      }
      catch (Exception _e) {
        System.err.println(Error.getMensaje(this, "SIAFM", "buscarDescripcion", _e.getMessage()));
      }
    }
    return regresar;
  }

  public String formatearNumero(String formato,double valor) {
    return Numero.formatear(formato, valor);
  }

  public String letraCapital(String nombreCampo) {
      return Cadena.letraCapital(nombreCampo);
  }

  public String nombrePersona(String nombreCampo) {
    return Cadena.nombrePersona(nombreCampo);
  } // nombrePersona

  public String getNombreMes(int mes) {
    return Fecha.getNombreMes(mes);
  } // getNombreMes

  public String getNombreDia(int dia) {
    return Fecha.getNombreDiaCorto(dia);
  } //

  public String formatearValor(Connection conexion, String celda, String formato) {
    try {
      if (formato.indexOf("~") >= 0) {
        if (formato.indexOf("*") >= 0) {
          celda = buscarDescripcion(conexion, formato.substring(0, formato.indexOf("*")), celda);
          formato = formato.substring(formato.indexOf("*") + 1);
        }
        else {
          celda = buscarDescripcion(conexion, formato, celda);
          formato = "4";
        } // if del *
      } // if recuperar campo
      int codigo = Integer.parseInt(formato);
      double valor = 0;
      switch (codigo) {
      case 0: // no hacer ninguna validacion
        break;
      case 1: // formato moneda
        valor = Double.valueOf(celda).doubleValue();
        celda = formatearNumero("$ ###,##0.0#",valor);
        break;
      case 2: // separacion de miles
        valor = Double.valueOf(celda).doubleValue();
        celda = formatearNumero("###,##0.0#", valor);
        break;
      case 3: // separacion de miles sin decimales
        valor = Double.valueOf(celda).doubleValue();
        celda = formatearNumero("###,##0", valor);
        break;
      case 4: // Letra capital la primer letra
        celda = letraCapital(celda);
        break;
      case 5: // Letra capital por cada palabra en la cadena
        celda = nombrePersona(celda);
        break;
      case 6: // Letra en mayusculas
        celda = celda.toUpperCase();
        break;
      case 7: // Letra en minusculas
        celda = celda.toLowerCase();
        break;
      case 14: // desencriptar palabra
        Encriptar desencriptar = new Encriptar();
        celda = desencriptar.desencriptar(celda, desencriptar.SIA_CLAVE);
        desencriptar = null;
        break;
      case 15: // encriptar palabra
        Encriptar encriptar = new Encriptar();
        celda = encriptar.encriptar(celda, encriptar.SIA_CLAVE);
        encriptar = null;
        break;
      case 16: // wakko
        valor = Double.valueOf(celda).doubleValue();
        celda = formatearNumero("#.00", valor);
        break;
      } // switch
      if (codigo > 7 && codigo < 14) {
        GregorianCalendar calendario = new GregorianCalendar();
        int xAnio = Integer.parseInt(celda.substring(0, 4));
        int xMes = Integer.parseInt(celda.substring(4, 6)) - 1;
        int xDia = Integer.parseInt(celda.substring(6, 8));
        calendario.set(xAnio, xMes, xDia);
        switch (codigo) {
        case 8: // Fecha en dd/mes/yyyy   26/Noviembre/2003
          celda =
              calendario.get(calendario.DATE) + "/" + getNombreMes(calendario.get(calendario.MONTH)) + "/" + celda.substring(0,
                                                                                                                             4);
          break;
        case 9: // Fecha en dd/mm/yyyy    26/11/2003
          celda = celda.substring(6, 8) + "/" + celda.substring(4, 6) + "/" + celda.substring(0, 4);
          break;
        case 10: // Fecha en:  nombre del dia, dd/mm/yyyy    Miercoles, 26/11/2003
          celda =
              getNombreDia(calendario.get(calendario.DAY_OF_WEEK)) + ", " + celda.substring(6, 8) + "/" + celda.substring(4,
                                                                                                                          6) +
              "/" + celda.substring(0, 4);
          break;
        case 11: // Fecha en:  nombre del dia, dia mes a�o   Miercoles, 26 de Noviembre del 2003
          celda =
              getNombreDia(calendario.get(calendario.DAY_OF_WEEK)) + ", " + calendario.get(calendario.DATE) + " de " +
              getNombreMes(calendario.get(calendario.MONTH)) + " de " + calendario.get(calendario.YEAR);
          break;
        case 12: // Fecha en dd/mm/yy   26/11/03
          celda = celda.substring(6, 8) + "/" + celda.substring(4, 6) + "/" + celda.substring(2, 4);
          break;
        case 13: // Fecha en:  dia mes a�o  26 de Noviembre del 2003
          celda =
              calendario.get(calendario.DATE) + " de " + getNombreMes(calendario.get(calendario.MONTH)) + " de " + calendario.get(calendario.YEAR);
          break;
        } // switch
      } // if
    }
    catch (Exception e) {
      System.err.println(Error.getMensaje(this, "SIAFM", "formatearValor", e));
    }
    return celda;
  } // formatearValor

  private void eliminarTemporales(String ruta, String patron) {
   Eliminar.eliminarTemporales(ruta, patron);
  } //eliminarTemporales

  public String formatearFecha(Date fecha, String patron) {
    return Fecha.formatear(patron, fecha);
  } // formatearFecha

  private String getPatronDias(int dias) {
    return Patron.getPatronDias(dias);
  } //getPatronDias

  public void eliminarTemporalesDias(String ruta, int dias) {
    Patron.eliminarTemporalesDias(ruta, dias);
  } //eliminarTemporalesDias

  public void eliminarTemporalesDias(String ruta) {
    eliminarTemporales(ruta, getPatronDias(0));
  } //

  public String despliegaFecha(String Fecha) {
    String Anio = "";
    int Mes = 0;
    String Dia = "";
    String Conversion = "Fecha invalida.";
    if (Fecha.length() == 6 || Fecha.length() == 8) {
      if (Fecha.length() == 6)
        Anio = Fecha.substring(0, 2);
      else
        Anio = Fecha.substring(0, 4);
      Mes = Integer.parseInt(Fecha.substring(Fecha.length() - 4, Fecha.length() - 2)) - 1;
      Dia = Fecha.substring(Fecha.length() - 2, Fecha.length());
      if (Mes >= 0 && Mes < 12)
        Conversion = Integer.parseInt(Dia) + " de " + getNombreMes(Mes) + " del " + Anio;
    }
    return Conversion;
  } //despliegaFecha

  public String fechaHoy() {
    return Fecha.getHoy();
  } //fechaHoy

  public String regresarDescripcion(Connection conection, String sentencia) {
    String regresar = null;
    Statement estatuto = null;
    ResultSet crsTabla = null;
    try {
      estatuto = conection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
      crsTabla = estatuto.executeQuery(sentencia);
      if (crsTabla.first())
        regresar = crsTabla.getString(1);
    }
    catch (Exception e) {
      System.err.println(Error.getMensaje(this, "SIAFM", "regresarDescripcion", e.getMessage()));
    }
    finally {
      try {
        if (crsTabla != null)
          crsTabla.close();
        crsTabla = null;
        if (estatuto != null)
          estatuto.close();
        estatuto = null;
      }
      catch (Exception _e) {
        System.err.println(Error.getMensaje(this, "SIAFM", "regresarDescripcion", _e.getMessage()));
      }
    }
    return regresar;
  } //regresarDescripcion

  public String buscarCurpEmpleado(Connection connection, String num_empleado) {
    return regresarDescripcion(connection,
                               "select curp from rh_tr_empleados where num_empleado= '" + num_empleado + "'");
  } // buscarNumeroEmpleado
  
   public String buscarEmpleado(String alias, String cadena){ //wakko
     StringBuffer like= null;
     if (!cadena.equals("")){
       like= new StringBuffer();
       String[] busqueda = cadena.split(" ");
       int contador= 0;
       StringBuffer elemento= new StringBuffer();
       for(int i=0; i< busqueda.length; i++) {
         elemento.append(busqueda[i]);
         contador= i;
         while(contador+ 1< busqueda.length && busqueda[i].equals(busqueda[contador+ 1])) {
           contador++;
           elemento.append(" ").append(busqueda[contador]);
         }; // while contador
         i= contador;
         if (i== busqueda.length- 1)
           like.append("  (").append(alias).append("apellido_pat||' '||").append(alias).append("apellido_mat||' '||").append(alias).append("nombres) like '%").append(elemento.toString()).append("%' ");
         else
           like.append("  (").append(alias).append("apellido_pat||' '||").append(alias).append("apellido_mat||' '||").append(alias).append("nombres) like '%").append(elemento.toString()).append("%' and ");
         elemento.delete(0, elemento.length());
       }; // for i
       elemento= null;
       busqueda= null;
     }; //if cadena
     return like.toString();
   }; //buscarEmpleado

  public String getInstanciaBD() {
    Properties p = null;
    ClassLoader loader = getClass().getClassLoader();
    InputStream is = loader.getResourceAsStream("db.properties");
    if (is == null) {
      System.out.println(" No se puede leer el archivo de propiedades db.properties");
      return "";
    }
    p = new Properties();
    try {
      p.load(is);
      String url = p.getProperty("total.url");
      if (url.indexOf("@") >= 0)
        return url.substring(url.indexOf("@") + 1, url.length());
      else
        return url.substring(url.indexOf("//") + 1, url.length());
    }
    catch (IOException ex) {
      System.out.println("Error al leer el archivo de propiedades");
    }
    return "";
  } // instanciaBD
  
  public String getRequest(HttpServletRequest request, String parametro, String valDefault) {
    return request.getParameter(parametro)!=null && !request.getParameter(parametro).equals("") && !request.getParameter(parametro).equals("null") ? request.getParameter(parametro) : valDefault;
  }

}
