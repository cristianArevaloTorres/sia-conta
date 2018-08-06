package sia.beans.seguridad;

import java.util.Date;

import java.util.Iterator;

import sia.libs.formato.Fecha;

public class UsuariosSitioStack {
  
  private String pagina;
  private Date fechaHora;
  
  public UsuariosSitioStack(String pagina) {
    setPagina(pagina);
    setFechaHora(Fecha.getRegistroDate());
  }

  public void setPagina(String pagina) {
    this.pagina = pagina;
  }

  public String getPagina() {
    return pagina;
  }

  public void setFechaHora(Date fechaHora) {
    this.fechaHora = fechaHora;
  }

  public Date getFechaHora() {
    return fechaHora;
  }
  
  public String getFechaHoraFormateada() {
    return Fecha.formatear(Fecha.FECHA_HORA,getFechaHora());
  }
}
