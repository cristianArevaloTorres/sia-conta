package sia.rf.contabilidad.registroContable.formas.servicios.docs;

import java.util.HashMap;
import java.util.ListResourceBundle;
import java.util.Map;
import java.util.ResourceBundle;
import sia.db.dao.DaoFactory;
import sia.db.sql.Sentencias;
import sia.libs.formato.Error;
import sia.libs.formato.Formatos;

public class DocumentoAutomatico {
  private String polizaAutomaticaId;
  private String idDocumento;
  private String tipoDocumento;
  private String atendido;
  private String polizaId;
  private String empAtendio;
  private String tramite;

  public DocumentoAutomatico(String polizaAutomaticaId, String idDocumento, String tipoDocumento, String tramite) {
    this(polizaAutomaticaId, idDocumento, tipoDocumento, "","-1","", tramite);
  }

  public DocumentoAutomatico(String polizaAutomaticaId, String idDocumento, String tipoDocumento, String atendido,
                             String polizaId, String empAtendio, String tramite) {
    setPolizaAutomaticaId(polizaAutomaticaId);
    setIdDocumento(idDocumento);
    setTipoDocumento(tipoDocumento);
    setAtendido(atendido);
    setPolizaId(polizaId);
    setEmpAtendio(empAtendio);
    setTramite(tramite);
  }

  public void setIdDocumento(String idDocumento) {
    this.idDocumento = idDocumento;
  }

  public String getIdDocumento() {
    return idDocumento;
  }

  public void setTipoDocumento(String tipoDocumento) {
    this.tipoDocumento = tipoDocumento;
  }

  public String getTipoDocumento() {
    return tipoDocumento;
  }

  public void setPolizaAutomaticaId(String polizaAutomaticaId) {
    this.polizaAutomaticaId = polizaAutomaticaId;
  }

  public String getPolizaAutomaticaId() {
    return polizaAutomaticaId;
  }
  
  public Map getParametros(){
    Map parametros = new HashMap();
    parametros.put("poliza_automatica_id",getPolizaAutomaticaId());
    parametros.put("tipo_documento",getTipoDocumento());
    parametros.put("atendido",getAtendido());
    parametros.put("emp_atendio",getEmpAtendio());
    parametros.put("poliza_id",getPolizaId());
    parametros.put("tramite", getTramite());
    return parametros;
  }
  
  public boolean actualizaEstatusProcesando() {
    String query                         = null;
    ResourceBundle propiedades           = null;
    boolean insertoCorrectamente         = false;
    Formatos formatos                    = null;
    Sentencias sentencia                 = null;
    try {      
      propiedades = ListResourceBundle.getBundle("contabilidad");      
      formatos = new Formatos(propiedades.getString("presupuesto.polizaAutomatica.actualizarEstatus"), getParametros());
      query = formatos.getSentencia();
      sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD);
      if (sentencia.ejecutar(query) == -1)
        insertoCorrectamente = false;
      else
        insertoCorrectamente=true;
    } catch (Exception e) {
      Error.mensaje(e, "CONTABILIDAD");
    }
    finally{
      query = null;
      propiedades = null;
      formatos = null;
      sentencia = null;      
    }
    return insertoCorrectamente;
  }
  
  
  public void setAtendido(String atendido) {
    this.atendido = atendido;
  }

  public String getAtendido() {
    return atendido;
  }

  public void setPolizaId(String polizaId) {
    this.polizaId = polizaId;
  }

  public String getPolizaId() {
    return polizaId;
  }

  public void setEmpAtendio(String empAtendio) {
    this.empAtendio = empAtendio;
  }

  public String getEmpAtendio() {
    return empAtendio;
  }

  public void setTramite(String tramite) {
    this.tramite = tramite;
  }

  public String getTramite() {
    return tramite;
  }
}
