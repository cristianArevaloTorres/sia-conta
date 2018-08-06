/*
 * Clase: Cadena.java
 *
 * Creado: 21 de mayo de 2007, 12:16 AM
 *
 * Write by: alejandro.jimenez
 */
package sia.libs.formato;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import sia.libs.recurso.Configuracion;

public final class Fecha {

  public static final int FECHA_NOMBRE_MES  = 1;
  public static final int FECHA_CORTA       = 2;
  public static final int FECHA_CORTA_GUION = 12;
  public static final int FECHA_NOMBRE_DIA  = 3;
  public static final int FECHA_EXTEDIDA    = 4;
  public static final int FECHA_MINIMA      = 5;
  public static final int FECHA_LARGA       = 6;
  public static final int FECHA_HORA        = 7;
  public static final int HORA_CORTA        = 8;
  public static final int HORA_LARGA        = 9;
  public static final int FECHA_HORA_EXTENDIDA= 10;
  public static final int FECHA_ESTANDAR      = 11;
  public static final int FECHA_MYSQL          = 14;
  public static final int HORA_MYSQL           = 15;
  public static final int FECHA_HORA_MYSQL     = 16;
  public static final int FECHA_ORACLE         = 17;
  public static final int HORA_ORACLE          = 18;
  public static final int FECHA_HORA_ORACLE    = 19;

  private Fecha() {
  }
  
  public static Date getRegistroDate(){
    Calendar fecha = Calendar.getInstance();
    return fecha.getTime();
  }
  
  public static java.sql.Date getRegistroDateSql() {
    return new java.sql.Date(getRegistroDate().getTime());
  }

  public static String getRegistro() {
    return formatear("yyyyMMddHHmmss",getRegistroDate());
  } // getPatron

  public static String getNombreDiaCorto(int dia) {
    String nombreDia[] = { "DO", "LU", "MA", "MI", "JU", "VI", "SA" };
    return nombreDia[dia - 1];
  } // getNombreDiaCorto

  public static String getNombreMes(int mes) {
    String[] nombreMes =
    { "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre",
      "Diciembre" };
    return nombreMes[mes];
  } // getNombreMes

  public static String getNombreMesCorto(int mes) {
    String[] nombreMes =
    { "ENE", "FEB", "MAR", "ABR", "MAY", "JUN", "JUL", "AGO", "SEP", "OCT", "NOV", "DIC" };
    return nombreMes[mes];
  } // getNombreMes
  
  public static String inversa(String fecha){
    String  mes ="";
    String  dia = "";
    String  anio = "";
    anio = fecha.substring(fecha.length()-4,fecha.length());
    dia =fecha.substring(0,fecha.indexOf(" "));
    if (fecha.length() == 11 || fecha.length() == 10){
      for (int i= 0; i< 12;i++){
        if (getNombreMesCorto(i).equals(fecha.substring(fecha.length()-8,fecha.length()-5)))
          mes = String.valueOf(i+1);
      }//for    
      if (mes.length()==1)
        mes="0".concat(mes);
      if (dia.length()==1)
        dia="0".concat(dia);
      fecha = anio.concat(mes).concat(dia);  
    }//if
       
    return fecha;
  }

  public static String getNombreDia(int dia) {
    String nombreDia[] = { "Domingo", "Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado" };
    return nombreDia[dia - 1];
  } // getNombreDia
 
  /*param fecha yyyyMMdd
   * */
  public static String formatear(int patron, String fecha) {
    if (fecha != null && fecha.length() > 0) {
      int anio = Integer.parseInt(fecha.substring(0, 4));
      int mes = Integer.parseInt(fecha.substring(4, 6)) - 1;
      int dia = Integer.parseInt(fecha.substring(6, 8));
      GregorianCalendar calendario = new GregorianCalendar(anio, mes, dia);
      switch (patron) {
      case FECHA_NOMBRE_MES: // Fecha en dd/mes/yyyy   26/Noviembre/2003
        fecha = calendario.get(calendario.DATE) + "/" + getNombreMes(calendario.get(calendario.MONTH)) + "/" + fecha.substring(0,4);
        break;
      case FECHA_CORTA: // Fecha en dd/mm/yyyy    26/11/2003
        fecha = fecha.substring(6, 8) + "/" + fecha.substring(4, 6) + "/" + fecha.substring(0, 4);
        break;
      case FECHA_CORTA_GUION: // Fecha en yyyy-mm-dd    26/11/2003
        fecha = fecha.substring(0, 4) + "-" + fecha.substring(4, 6) + "-" + fecha.substring(6, 8);
        break;
      case FECHA_NOMBRE_DIA: // Fecha en:  nombre del dia, dd/mm/yyyy    Miercoles, 26/11/2003
        fecha = getNombreDia(calendario.get(calendario.DAY_OF_WEEK)) + ", " + fecha.substring(6, 8) + "/" + fecha.substring(4,6) +"/" + fecha.substring(0, 4);
        break;
      case FECHA_EXTEDIDA: // Fecha en:  nombre del dia, dia mes a�o   Miercoles, 26 de Noviembre del 2003
        fecha = getNombreDia(calendario.get(calendario.DAY_OF_WEEK)) + ", " + calendario.get(calendario.DATE) + " de " +
            getNombreMes(calendario.get(calendario.MONTH)) + " de " + calendario.get(calendario.YEAR);
        break;
      case FECHA_MINIMA: // Fecha en dd/mm/yy   26/11/03
        fecha = fecha.substring(6, 8).concat("/").concat(fecha.substring(4, 6)).concat("/").concat(fecha.substring(2, 4));
        break;
      case FECHA_LARGA: // Fecha en:  dia mes a�o  26 de Noviembre del 2003
        fecha = calendario.get(calendario.DATE) + " de " + getNombreMes(calendario.get(calendario.MONTH)) + " de " + calendario.get(calendario.YEAR);
        break;
      case FECHA_HORA: // Fecha en dd/mmm/yyyy hh:mm:ss  03/12/2007 12:26:00
        fecha = fecha.substring(6, 8).concat("/").concat(fecha.substring(4, 6)).concat("/").concat(
            fecha.substring(0, 4)).concat(" ").concat(fecha.substring(8,10)).concat(":").concat(fecha.substring(10,12)).concat(":").concat(fecha.substring(12,14));
        break;
      case HORA_CORTA: // Fecha en hh:mm:ss  12:26
        fecha = fecha.substring(8, 10).concat(":").concat(fecha.substring(10, 12));
        break;
      case HORA_LARGA: // Fecha en hh:mm:ss  12:26:00
        fecha = fecha.substring(8, 10).concat(":").concat(fecha.substring(10, 12)).concat(":").concat(fecha.substring(12,14));
        break;
      case FECHA_HORA_EXTENDIDA: // Fecha en yyyyMMddhhmmss 20071203241600
        fecha = fecha.substring(0, 14);
        break;
      case FECHA_ESTANDAR: // Fecha en yyyyMMdd 20071203
        fecha = fecha.substring(0, 8);
        break;
      }// switch
    }
    else
      fecha = "";
    return fecha;
  } // formatear

  /**
   * @param fecha yyyy/mm/dd o yyyy-mm-dd
   * @return yyyymmdd
   */
  public static String getFormatoValidoFromIzq(String fecha){
    return fecha.substring(0,4).concat(fecha.substring(5,7).concat(fecha.substring(8,10)));
  }

  /**
   * @param fecha dd/mm/yyyy o dd-mm-yyyy
   * @return yyymmdd
   */
  public static String getFormatoValidoFromDer(String fecha){
    return fecha.substring(6,10).concat(fecha.substring(3,5).concat(fecha.substring(0,2)));
  }

  public static String formatear(String patron, Date fecha) {
    SimpleDateFormat formato = new SimpleDateFormat(patron);
    return formato.format(fecha);
  } // formatear

  public static String formatear(int patron, Date fecha) {
    return formatear(patron, formatear("yyyyMMddHHmmss", fecha));
  } // formatear

  public static String formatear(int patron, Calendar fecha) {
    return formatear(patron, formatear("yyyyMMddHHmmss", fecha.getTime()));
  } // formatear

  public static String formatear(int patron) {
    return formatear(patron, formatear("yyyyMMddHHmmss", Calendar.getInstance().getTime()));
  } // formatear

  public static String getHoy() {
    return formatear(FECHA_CORTA);
  } // getHoy

  public static String getHoyEstandar() {
    return formatear(FECHA_ESTANDAR);
  } // getHoy

  public static String getHoyExtendido() {
    return formatear(FECHA_HORA);
  } // getHoy

  public static int getDiasEnElMes(int anio, int mes) {
    int dias[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
    if ((anio % 4 == 0 && !(anio % 100 == 0)) || anio % 400 == 0)
      dias[1] = 29;
    return dias[mes];
  } // getDiasEnElMes

  public static int diasSiguienteMes(int anio, int mes) {
    if (mes + 1 > 11) {
      anio += 1;
      mes = 0;
    }
    else
      mes++;
    return getDiasEnElMes(anio, mes);
  } // siguienteMes

  public static String reversa(String fecha) {
    return fecha.substring(6, 8).concat("/").concat(fecha.substring(4, 6)).concat("/").concat(fecha.substring(0, 4));
  }

  public static String getFecha(String fecha) {
    return fecha.substring(6, 10).concat(fecha.substring(3, 5)).concat(fecha.substring(0, 2));
  }

  public static int getDiaActual() {
    return Calendar.getInstance().get(Calendar.DATE);
  }

  public static int getMesActual() {
    return Calendar.getInstance().get(Calendar.MONTH) + 1;
  }

  public static int getAnioActual() {
    return Calendar.getInstance().get(Calendar.YEAR);
  }

  public static int diferenciasDias(String ayer, String hoy) {
    int diasDiferiencia = 0;
    if (ayer != null && (ayer.length() != 8 || ayer.length() != 14)) {
      if (hoy != null && (hoy.length() != 8 || hoy.length() != 14)) {
        if (ayer.length() == 14)
          ayer = ayer.substring(0, 8);
        if (hoy.length() == 14)
          hoy = hoy.substring(0, 8);
        int anioAyer = Integer.parseInt(ayer.substring(0, 4));
        int anioHoy = Integer.parseInt(hoy.substring(0, 4));
        // fecha anterior
        java.util.Calendar fechaAyer = java.util.Calendar.getInstance();
        fechaAyer.set(anioAyer, Integer.parseInt(ayer.substring(4, 6)) - 1, Integer.parseInt(ayer.substring(6, 8)));

        // fecha posterior
        java.util.Calendar fechaHoy = java.util.Calendar.getInstance();
        fechaHoy.set(anioHoy, Integer.parseInt(hoy.substring(4, 6)) - 1, Integer.parseInt(hoy.substring(6, 8)));

        // fecha pivote
        java.util.Calendar pivote = java.util.Calendar.getInstance();
        if (anioAyer < anioHoy) {
          for (int x = anioAyer; x < anioHoy; x++) {
            pivote.set(x, 11, 31); // 31 de Diciembre de los a�os anteriores
            diasDiferiencia += pivote.get(pivote.DAY_OF_YEAR);
          }// for
          diasDiferiencia += fechaHoy.get(fechaHoy.DAY_OF_YEAR);
          diasDiferiencia -= fechaAyer.get(fechaAyer.DAY_OF_YEAR);
        }
        else if (anioHoy < anioAyer) {
          for (int x = anioHoy; x < anioAyer; x++) {
            pivote.set(x, 11, 31); // 31 de Diciembre de los a�os anteriores
            diasDiferiencia += pivote.get(pivote.DAY_OF_YEAR);
          }// for
          diasDiferiencia += fechaAyer.get(fechaAyer.DAY_OF_YEAR);
          diasDiferiencia -= fechaHoy.get(fechaHoy.DAY_OF_YEAR);
        }
        else {
          diasDiferiencia = fechaHoy.get(fechaHoy.DAY_OF_YEAR) - fechaAyer.get(fechaAyer.DAY_OF_YEAR);
        } // if
        fechaAyer = null;
        fechaHoy = null;
        pivote = null;
      } // if hoy
    } // if ayer
    return Math.abs(diasDiferiencia);
  } // restar

  public static long diferenciaMinutos(long antes, long despues) {
    long regresar = 0;
    long diferencia = despues - antes;
    regresar = diferencia / 1000 / 60;
    return regresar;
  } // minutos

  public static long diferenciaHoras(long antes, long despues) {
    long regresar = 0;
    long diferencia = despues - antes;
    regresar = diferencia / 1000 / 60 / 60;
    return regresar;
  } // horas

  public static long milisegundos(String hora) {
    Calendar calendario = Calendar.getInstance();
    // formatos permitidos
    // hora= 6  => HHMMSS
    // hora= 8  => HH:MM:SS
    // hora= 14 => YYYYMMDDHHMMSS
    if (hora != null && (hora.length() != 6 || hora.length() != 8 || hora.length() != 14)) {
      if (hora.length() == 14)
        hora = hora.substring(8);
      if (hora.length() == 8)
        hora = hora.substring(0, 2).concat(hora.substring(3, 5)).concat(hora.substring(6, 8));
      calendario.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hora.substring(0, 2)));
      calendario.set(Calendar.MINUTE, Integer.parseInt(hora.substring(2, 4)));
      calendario.set(Calendar.SECOND, Integer.parseInt(hora.substring(4, 6)));
    } // if
    return calendario.getTimeInMillis();
  } // milesegundos

  public static long diferenciaHoras(String antes, String despues) {
    return diferenciaHoras(milisegundos(antes), milisegundos(despues));
  } // diferenciaHoras

  public static long diferenciaMinutos(String antes, String despues) {
    return diferenciaMinutos(milisegundos(antes), milisegundos(despues));
  } // diferenciaMinutos

  public static long horas(int dias) {
    return dias * 24;
  } // diferenciaMinutos
  
  public static String[] meses(int iMes){
    String[] meses = new String[12];
    String[] nombreMes =
    { "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre",
      "Diciembre" };
    System.arraycopy(nombreMes,0,meses,0,iMes);
    return meses;
  }
  
    public static Calendar getFechaCalendar(String fecha) {
        // DD/MM/YYYY ó DD-MM-YYYY
      return getFechaHora(fecha.concat(" 00:00:00"));
    }

    public static Calendar getHora(String hora) {
        // HH:MM:SS
      return getFechaHora("01/01/9999 ".concat(hora).concat(hora.length()== 5? ":00": ""));
    }

    public static String getFechaHoraBD(Date date) {
      String regresar = null;
      String db = Configuracion.getInstance().getPropiedad("sistema.bd.type");
      if (db.equalsIgnoreCase("mysql"))
        regresar = formatear(FECHA_HORA_MYSQL, date);
      else 
        if (db.equalsIgnoreCase("oracle"))
          regresar = formatear(FECHA_HORA_ORACLE, date);
        else
          regresar = getHoyExtendido();
      return regresar;
    } // getFechaHoraBD
    
     public static Calendar getFechaHora(String fecha) {
         // DD/MM/YYYY HH:MM:SS
         // 0123456789012345678
         // YYYYMMDDHHmmSS
         // 01234567890123
         // YYYY-MM-DD HH:mm:ss.S
         // 012345678901234567012
         if(fecha.length()== 6) // YYYYMMDD
           fecha= reversa(fecha);
         else  
           if(fecha.length()== 8) // DD/MM/YY
             fecha= fecha.substring(0,6).concat("19").concat(fecha.substring(6)).concat(" 00:00:00");
           else  
             if(fecha.length()== 10) // DD/MM/YYYY
               fecha= getFormatoEspaniol(fecha).concat(" 00:00:00");
             else
               if(fecha.length()== 12) // YYYYMMDDHHmm
                 fecha= reversa(fecha.substring(0, 8)).concat(" ").concat(fecha.substring(8, 10)).concat(":").concat(fecha.substring(10, 12)).concat(":");
               else  
                 if(fecha.length()== 14) // YYYYMMDDHHmmSS
                   fecha= reversa(fecha.substring(0, 8)).concat(" ").concat(fecha.substring(8, 10)).concat(":").concat(fecha.substring(10, 12)).concat(":").concat(fecha.substring(12, 14));
                 else  
                   if(fecha.length()>= 20) // YYYY-MM-DD HH:mm:ss.S
                     fecha= getFormatoEspaniol(fecha.substring(0,10)).concat(fecha.substring(10));
         Calendar calendar= Calendar.getInstance();
         calendar.set(Calendar.DATE, Numero.getInteger(fecha.substring(0, 2)));
         calendar.set(Calendar.MONTH, Numero.getInteger(fecha.substring(3, 5))- 1);
         calendar.set(Calendar.YEAR, Numero.getInteger(fecha.substring(6, 10)));
         calendar.set(Calendar.HOUR, Numero.getInteger(fecha.substring(11, 13)));
         calendar.set(Calendar.MINUTE, Numero.getInteger(fecha.substring(14, 16)));
         calendar.set(Calendar.SECOND, Numero.getInteger(fecha.substring(17, 19)));
         return calendar;
       }
       
    private static String getFormatoEspaniol(String fecha) {
      if(fecha.indexOf("/")== 4 || fecha.indexOf("-")== 4)
        return fecha.substring(8, 10).concat("/").concat(fecha.substring(5, 7)).concat("/").concat(fecha.substring(0, 4));
      else
        return fecha; 
    }
    
    public static String getFechaBD(Date date) {
      String regresar = null;
      String db = Configuracion.getInstance().getPropiedad("sistema.bd.type");
      if (db.equalsIgnoreCase("mysql"))
        regresar = formatear(FECHA_MYSQL, date);
      else if (db.equalsIgnoreCase("oracle"))
        regresar = formatear(FECHA_ORACLE, date);
      else
        regresar = getHoy();
      return regresar;
    } // getFechaBD

    public static String getHoraBD(Date date) {
      String regresar = null;
      String db = Configuracion.getInstance().getPropiedad("sistema.bd.type");
      if (db.equalsIgnoreCase("mysql"))
        regresar = formatear(HORA_CORTA, date);
      else 
        if (db.equalsIgnoreCase("oracle"))
          regresar = formatear(HORA_ORACLE, date);
        else
          regresar = formatear(HORA_CORTA);
      return regresar;
    } // getHoraBD
    
    public static void main(String[] args) {
      java.sql.Date fec = java.sql.Date.valueOf("2011-01-01");
      System.out.println(Fecha.formatear(Fecha.FECHA_CORTA,fec));
      System.out.println(Fecha.formatear(Fecha.FECHA_CORTA,Fecha.getRegistro()));
    }
    
    
    
} // Fecha
