package sia.scriptlets;

import java.io.PrintWriter;
import java.io.Writer;

import net.sf.jasperreports.engine.JRDefaultScriptlet;
import net.sf.jasperreports.engine.JRScriptletException;

import java.sql.Connection;
import java.sql.Date;
import java.sql.Statement;
import java.sql.ResultSet;

import java.sql.Timestamp;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import sia.libs.progreso.ProgressMonitor;
import sia.libs.formato.Error;
import sia.libs.formato.Fecha;
import sia.libs.formato.Numero;
import sia.libs.formato.Cadena;

public class BarraProgreso extends JRDefaultScriptlet {

  // Estas variables son para determina el progreso del reporte a nivel de registro
  private int currentRecord = 0;
  private int recordCount = 1;
  private String form = null;
  private boolean showPercent = false;
  private boolean htmlPercent = false;
  private boolean debug = false;
  private Connection connection = null;


  // Estas variables son para colocarle la barra de progreso al reporte
  private Collection coleccion = null;
  private int porcentaje = 0;
  private int avancePorcetaje = 0;
  private PrintWriter out = null;

  protected Object checkParameter(String parameter, Object defaultValue) {
    try {
      return this.getParameterValue(parameter);
    } 
    catch (JRScriptletException e) {
      System.err.println(Error.getMensaje(this, "SIAFM", "checkParameter", "No existe el parametro ".concat(parameter)));
      return defaultValue;
    } // try
  } // checkParameter

  protected Object checkParameter(String parameter) {
    return checkParameter(parameter, null);
  }

  protected Object checkVariable(String variable, Object defaultValue) {
    try {
      return this.getVariableValue(variable);
    } 
    catch (JRScriptletException e) {
      System.err.println(Error.getMensaje(this, "SIAFM", "checkVariable", "No existe la variable ".concat(variable)));
      return defaultValue;
    } // try
  } // checkParameter

  protected Object checkVariable(String variable) {
    return checkVariable(variable, null);
  }

  protected Object checkField(String field, Object defaultValue) {
    try {
      return this.getFieldValue(field);
    } 
    catch (JRScriptletException e) {
      System.err.println(Error.getMensaje(this, "SIAFM", "checkField", "No existe el campo ".concat(field)));
      return defaultValue;
    } // try
  } // checkField

  protected Object checkField(String parameter) {
    return checkField(parameter, null);
  }

  public void beforeReportInit() throws JRScriptletException {
    recordCount = 0;
    porcentaje = 0;
    avancePorcetaje = 0;
    String queryReport = null;
    try {
      connection = (Connection)this.getParameterValue("REPORT_CONNECTION");
      if (checkParameter("SIA_QUERY_REPORT") != null)
        queryReport = (String)checkParameter("SIA_QUERY_REPORT");
      if (checkParameter("SIA_RECORD_COUNT") != null)
        recordCount = ((Integer)this.getParameterValue("SIA_RECORD_COUNT")).intValue();
      if (recordCount <= 0 && connection != null && queryReport != null) {
        Statement stm = connection.createStatement();
        ResultSet rst = stm.executeQuery(queryReport);
        while (rst.next()) {
          recordCount++;
        }
        ; // while
        stm.close();
        stm = null;
        rst = null;
        if (debug)
          System.out.println("total de registro: " + recordCount);
      }
      ; // if
    } catch (Exception e) {
      System.err.println(Error.getMensaje(this, "SIAFM", "beforeReportInit", e));
    }
    ; // try
    if (checkParameter("SIA_OUT_REPORT") != null)
      out = new PrintWriter((Writer)checkParameter("SIA_OUT_REPORT"));
    if (checkParameter("SIA_FORM_REPORT") != null)
      form = (String)checkParameter("SIA_FORM_REPORT");
    showPercent = recordCount > 0;
    htmlPercent = out != null && form != null;
    if (showPercent && htmlPercent)
      agregarBarrasProgreso();
  }

  public void afterReportInit() throws JRScriptletException {
  }

  public void beforePageInit() throws JRScriptletException {
  }

  public void afterPageInit() throws JRScriptletException {
  }

  public void beforeColumnInit() throws JRScriptletException {
  }

  public void afterColumnInit() throws JRScriptletException {
  }

  public void beforeGroupInit(String groupName) throws JRScriptletException {
  }

  public void afterGroupInit(String groupName) throws JRScriptletException {

  }

  public void beforeDetailEval() throws JRScriptletException {
  }

  public void afterDetailEval() throws JRScriptletException {
    if (showPercent)
      progress();
  }

  public void progress() throws JRScriptletException {
    currentRecord = ((Integer)this.getVariableValue("REPORT_COUNT")).intValue();
    String porcentaje = (currentRecord * 100 / recordCount) + "%";
    if (debug)
      System.out.println("Registro: " + this.getVariableValue("REPORT_COUNT") + " de " + recordCount +
                         " porcentaje: " + porcentaje + " avance: " + avancePorcetaje);
    if (htmlPercent)
      setRegistroActual();
  } // progress

  private void setRegistroActual() throws JRScriptletException {
    if (debug)
      System.out.println("setRegistroActual");
    // Determinar los avances de las barras de progreso
    avancePorcetaje = (int)(currentRecord * 100 / recordCount);
    if (porcentaje != avancePorcetaje) {
      Iterator iterator = coleccion.iterator();
      while (iterator.hasNext()) {
        ProgressMonitor progressmonitor = (ProgressMonitor)iterator.next();
        progressmonitor.setMessage(" Registro [" + currentRecord + " de " + recordCount + "]");
        progressmonitor.setProgress(currentRecord);
        progressmonitor.printProgress();
      }
      ; // while
      porcentaje = avancePorcetaje;
      out.flush();
    }
    ; // if
    if (avancePorcetaje >= 100) {
      //out.println("<br><b><center>Espere por favor, exportando archivo. !</center></b><br>");
      out.flush();
    }
    ; // if
  } // setRegistroActual

  private void insertarBarras() {
    if (debug)
      System.out.println("insertarBarras =>" + form);
    coleccion = new Vector();
    // Prepare the progress monitors...
    ProgressMonitor statusMonitor = new ProgressMonitor();
    statusMonitor.setPrinter(out);
    statusMonitor.setFormat("<script> window.status = \"( {1}% ) Procesando : {0} ... \";actualizarProgreso({1});</script>");
    addProgressMonitor(statusMonitor);
  } // insertarBarras

  private void actualizarBarras() {
    if (debug)
      System.out.println("actualizarBarras");
    Iterator iterator = coleccion.iterator();
    int contador = 0;
    while (iterator.hasNext()) {
      ProgressMonitor progressmonitor = (ProgressMonitor)iterator.next();
      progressmonitor.setBarra();
      progressmonitor.setMin(0);
      progressmonitor.setMax(recordCount);
      switch (contador) {
      case 0: // To present the bar gif.
        progressmonitor.setFormat("<script> window.status = \"( {1}% ) Procesando : {0} ... \";actualizarProgreso({1});</script>");
        break;
      }
      ; // switch
      contador++;
    }
    ; // while
  } // actualizarBarras

  private void agregarBarrasProgreso() {
    if (debug)
      System.out.println("agregarBarrasProgreso");
    // Crear arreglo de barras de progreso
    if (coleccion != null)
      actualizarBarras();
    else
      insertarBarras();
  }

  private void addProgressMonitor(ProgressMonitor progressmonitor) {
    if (debug)
      System.out.println("addProgressMonitor");
    progressmonitor.setMin(0);
    progressmonitor.setMax(recordCount);
    coleccion.add(progressmonitor);
  }

  public String hello() throws JRScriptletException {
    return "Hola! I'm the report's scriptlet object.";
  } // hello

  public void setDebug(boolean debug) {
    this.debug = debug;
  } // setDebug

  public String getFecha(int formato, Timestamp registro) {
    java.util.Date date = new java.util.Date(registro.getTime());
    return Fecha.formatear(formato, date);
  }

  public String getFecha(Timestamp registro) {
    return getFecha(Fecha.FECHA_LARGA, registro);
  }

  public String getFecha(int formato, Date registro) {
    return Fecha.formatear(formato, registro);
  }

  public String getFecha(Date registro) {
    return getFecha(Fecha.FECHA_LARGA, registro);
  }

  public String getFecha(int formato, java.util.Date registro) {
    return Fecha.formatear(formato, registro);
  }

  public String getFecha(java.util.Date registro) {
    return getFecha(Fecha.FECHA_LARGA, registro);
  }

  public String getFecha() {
    return getFecha(Fecha.FECHA_LARGA, new java.util.Date());
  }
  
  public String getNumeroRedondea(double valor){
    return Numero.formatear(Numero.NUMERO_MONEDA,Double.parseDouble(Numero.redondear(Double.parseDouble(Numero.redondear(valor)))));
  }
  
  public String getLetraCapital(String cadena){
    return Cadena.letraCapital(cadena);
  }
  
  public String getNombrePersona(String cadena){
    return Cadena.nombrePersona(cadena);
  }

  private void setConnection(Connection connection) {
    this.connection = connection;
  }

  public Connection getConnection() {
    return connection;
  }
  

} // BarraProgreso
