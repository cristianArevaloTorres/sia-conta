package sia.libs.periodo;

import java.util.Calendar;
import java.util.GregorianCalendar;

import java.sql.Date;


public class Fecha {
  
  
  private int dia;
  private int mes;
  private int anio;
  private String separador= null;
  
  /** Creates a new instance of Fecha */
  public Fecha() {
    setSeparador("/");
    this.dia = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    this.mes = Calendar.getInstance().get(Calendar.MONTH)+ 1;
    this.anio= Calendar.getInstance().get(Calendar.YEAR);
  }
  
  public Fecha(Date fecha) {
    String fec = fecha.toString();
    this.dia = Integer.parseInt(fec.substring(8,10));
    this.mes = Integer.parseInt(fec.substring(5,7));
    this.anio= Integer.parseInt(fec.substring(0,4));
  }
  
  public Fecha(int dia, int mes, int anio) {
    this.setSeparador("/");
    this.dia = dia;
    this.mes = mes;
    this.anio= anio;
  }

  public Fecha(String valor, String separador) {
    this.setSeparador(separador);
    valor       = getFormatoValido(valor);
    this.dia    = getIntDia(valor.substring(6, 8));
    this.mes    = getIntMes(valor.substring(4, 6));
    this.anio   = getIntAnio(valor.substring(0, 4));
  }

  public Fecha(String valor) {
    this(valor, "/");
  }

  public Fecha(Fecha valor) {
    this(valor.toString(), valor.getSeparador());
  }

  private static int getIntDia(String dia) {
    try {
      int valor= Integer.parseInt(dia);
      return valor> 31 || valor< 0? Calendar.getInstance().get(Calendar.DAY_OF_MONTH): valor;
    }
    catch(Exception e) {
      return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }
  }

  private static int getIntMes(String mes) {
    try {
      int valor= Integer.parseInt(mes);
      return valor> 12 || valor< 0? Calendar.getInstance().get(Calendar.MONTH)+ 1: valor;
    }
    catch(Exception e) {
      return Calendar.getInstance().get(Calendar.MONTH)+ 1;
    }
  }

  private static int getIntAnio(String anio) {
    try {
      int valor= Integer.parseInt(anio);
      return valor> 9999 || valor< 1900? Calendar.getInstance().get(Calendar.YEAR): valor;
    }
    catch(Exception e) {
      return Calendar.getInstance().get(Calendar.YEAR);
    }
  }
  
  public int getDia() {
    return this.dia;
  }

  public void setDia(int dia) {
    this.dia= dia> 0 && dia<= getDiasEnElMes()? dia: getDia();
  }
  
  public int getMes() {
    return this.mes;
  }
  
  public void setMes(int mes) {
	this.mes = mes;
  }

  public int getAnio() {
    return this.anio;
  }
  
  public void setAnio(int anio) {
	this.anio = anio;
  }

  private String getDosDigitos(int valor) {
    return valor< 10? "0"+ valor: String.valueOf(valor);
  }
  
  public String toString() {
    return anio+ getDosDigitos(mes)+ getDosDigitos(dia);
  }

  public String getSeparador() {
    return separador;
  }

  public void setSeparador(String separador) {
    this.separador = separador;
  }

  public String toFecha() {
    return getDosDigitos(dia)+ getSeparador()+ getDosDigitos(mes)+ getSeparador()+ anio;
  }

  private String getFormatoValido(String valor) {
    if (valor== null)
      return Calendar.getInstance().get(Calendar.YEAR)+ getDosDigitos(Calendar.getInstance().get(Calendar.MONTH)+ 1)+ getDosDigitos(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
    else
      if (valor.indexOf(getSeparador())>= 0)
        return valor.substring(6, 10)+ valor.substring(3, 5)+ valor.substring(0, 2);
      else
        return valor;
  }

  public int getDiasEnElMes() {
    GregorianCalendar calendario= new GregorianCalendar(getAnio(), getMes()- 1, getDia());
    return calendario.getActualMaximum(Calendar.DAY_OF_MONTH);
  }

  public String toProbar() {
    return "dias: ["+ getDiasEnElMes()+ "] fecha: "+ toFecha();
  }
  
  private Fecha getDiaComodin(int corrimiento) {
    GregorianCalendar calendario= new GregorianCalendar(getAnio(), getMes()- 1, getDia());
    calendario.add(calendario.DAY_OF_MONTH, corrimiento);
    return new Fecha(calendario.get(Calendar.DAY_OF_MONTH), calendario.get(Calendar.MONTH)+ 1, calendario.get(Calendar.YEAR));
  }
  
  public Fecha getDiaSiguiente() {
    return getDiaComodin(1);
  }  
  
  public Fecha getDiaAnterior() {
    return getDiaComodin(-1);
  }  

  public boolean isQuincenaImpar() {
    return getDia()< 16;
  }
  
  public Fecha getInicioQuincena() {
    return new Fecha(isQuincenaImpar()? 1: 16, getMes(), getAnio());
  }
  
  public Fecha getTerminoQuincena() {
    return new Fecha(isQuincenaImpar()? 15: getDiasEnElMes(), getMes(), getAnio());
  }
  
  public int getQuincena() {
    return (getMes()* 2- 1)+ (isQuincenaImpar()? 0: 1);
  }

  public Fecha getInicioMes() {
    return new Fecha(1, getMes(), getAnio());
  }
  
  public Fecha getTerminoMes() {
    return new Fecha(getDiasEnElMes(), getMes(), getAnio());
  }
  
  public Fecha getInicioAnio() {
    return new Fecha(1, 1, getAnio());
  }
  
  public Fecha getTerminoAnio() {
    return new Fecha(31, 12, getAnio());
  }
  
  public Fecha getInicioSemestre() {
    return new Fecha(1, getMes()< 7? 1: 7, getAnio());
  }
  
  public Fecha getTerminoSemestre() {
    return new Fecha(getMes()< 7? 30: 31, getMes()< 7? 6: 12, getAnio());
  }
  
  private int getDiAnual() {
    GregorianCalendar calendario= new GregorianCalendar(getAnio(), getMes()- 1, getDia());
    return calendario.get(calendario.DAY_OF_YEAR);
  }
  
  public void addMeses(int meses) {
    GregorianCalendar calendario= new GregorianCalendar(getAnio(), getMes()- 1, getDia());
    calendario.add(GregorianCalendar.MONTH, meses);
    this.anio= calendario.get(GregorianCalendar.YEAR);
    this.mes = calendario.get(GregorianCalendar.MONTH)+ 1;
    this.dia = calendario.get(GregorianCalendar.DATE);
  }
  
  private String sumarRestarDias(int dias) {
    GregorianCalendar calendario= new GregorianCalendar(getAnio(), getMes()- 1, getDia());
      calendario.add(GregorianCalendar.DATE, dias);
    this.anio= calendario.get(GregorianCalendar.YEAR);
    this.mes = calendario.get(GregorianCalendar.MONTH)+ 1;
    this.dia = calendario.get(GregorianCalendar.DATE);
    return String.valueOf(dia).concat("/").
    concat(String.valueOf(mes)).concat("/").
    concat(String.valueOf(anio));
  }
  
  public String addDias(int dias) {
    return sumarRestarDias(dias);
  }
  
  public Date addDiasDate(int dias) {
    addDias(dias);
    StringBuffer sb = new StringBuffer();
    sb.append(anio).append("-");
    sb.append(mes).append("-");
    sb.append(dia);
    return Date.valueOf(sb.toString());
  }
  
  public String restarDias(int dias) {
    return sumarRestarDias((-1*dias));
  }
  
  public Date restarDiasDate(int dias) {
    restarDias(dias);
    StringBuffer sb = new StringBuffer();
    sb.append(anio).append("-");
    sb.append(mes).append("-");
    sb.append(dia);
    return Date.valueOf(sb.toString());
  }
  

  public Fecha getFechaEspecial(int meses) {
    GregorianCalendar calendario= new GregorianCalendar(getAnio(), getMes()- 1, getDia());
    calendario.add(GregorianCalendar.MONTH, meses);
    return new Fecha(calendario.get(GregorianCalendar.DATE), calendario.get(GregorianCalendar.MONTH)+ 1, calendario.get(GregorianCalendar.YEAR));
  }

  public boolean equals(String fecha) {
    return toString().equals(fecha);
  }
  
  public boolean equals(Fecha fecha) {
    return equals(fecha.toString());
  }
  
  
  /*
   *  las fechas se reciben en el sig formato yyyyMMdd
   * */
  public  boolean isDentro(String fechaInicial, String fechaFinal, String fechaComparar) {
    fechaInicial = sia.libs.formato.Fecha.formatear(sia.libs.formato.Fecha.FECHA_ESTANDAR,fechaInicial);
    fechaFinal = sia.libs.formato.Fecha.formatear(sia.libs.formato.Fecha.FECHA_ESTANDAR,fechaFinal);
    fechaComparar = sia.libs.formato.Fecha.formatear(sia.libs.formato.Fecha.FECHA_ESTANDAR,fechaComparar);
    return Long.valueOf(fechaInicial) <= Long.valueOf(fechaComparar) && Long.valueOf(fechaFinal) >= Long.valueOf(fechaComparar);
  }

}
