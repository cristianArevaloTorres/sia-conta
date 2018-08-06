package sia.rf.tesoreria.libs.csv;

public class ParametrosCsv {

  private String query  = null;
  private String[] encabezados = null;
  private int totalRegistros     = 0;
  private int conn = 0;
  private String tituloReporte = null;
  private String nombreReporte = null;
  private String programa = null;
  private String periodo = null; 
  private int tipoReporte = 0;
  
  
  public ParametrosCsv() {
  }

  public void setQuery(String query) {
    this.query = query;
  }

  public String getQuery() {
    return query;
  }

  public void setEncabezados(String[] encabezados) {
    this.encabezados = encabezados;
  }

  public String[] getEncabezados() {
    return encabezados;
  }

  public void setTotalRegistros(int totalRegistros) {
    this.totalRegistros = totalRegistros;
  }

  public int getTotalRegistros() {
    return totalRegistros;
  }

  public void setConn(int conn) {
    this.conn = conn;
  }

  public int getConn() {
    return conn;
  }

  public void setTituloReporte(String tituloReporte) {
    this.tituloReporte = tituloReporte;
  }

  public String getTituloReporte() {
    return tituloReporte;
  }

  public void setNombreReporte(String nombreReporte) {
    this.nombreReporte = nombreReporte;
  }

  public String getNombreReporte() {
    return nombreReporte;
  }

  public void setPrograma(String programa) {
    this.programa = programa;
  }

  public String getPrograma() {
    return programa;
  }

  public void setPeriodo(String periodo) {
    this.periodo = periodo;
  }

  public String getPeriodo() {
    return periodo;
  }

  public void setTipoReporte(int tipoReporte) {
    this.tipoReporte = tipoReporte;
  }

  public int getTipoReporte() {
    return tipoReporte;
  }
}
