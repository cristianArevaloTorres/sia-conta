package sia.scriptlets;

import java.sql.ResultSet;
import java.sql.Statement;

import sia.libs.formato.Cadena;
import sia.libs.formato.Error;
import sia.libs.formato.Formatos;


public class ScriptletFirmas extends CantidadLetras {

  private Firmante getFirma(int numEmpleado, String parametros, Object ... objetos) {
    Firmante regresar     = null;
    StringBuffer sentencia= new StringBuffer();
    sentencia.append("select ");
    sentencia.append("fd.tipo_firma,fd.num_empleado,nombre ||' ' ||apellido_pat || ' '|| apellido_mat nombre, ");
    sentencia.append("puesto_esp adscripcion ");
    sentencia.append("from ");
    sentencia.append("sia_admin.rh_tr_firmas_docs fd ");
    sentencia.append("inner join ");
    sentencia.append("sia_admin.rh_tc_firmas_autorizadas fa ");
    sentencia.append("on ");
    sentencia.append("fd.num_empleado = fa.num_empleado ");
    sentencia.append("inner join ");
    sentencia.append("sia_admin.rh_tc_doctos_autoriza da ");
    sentencia.append("on da.documento = fd.documento ");
    sentencia.append("where ");
    sentencia.append("fd.documento= '{0}' and fa.num_empleado=".concat(String.valueOf(numEmpleado)));
    if(parametros!= null) 
      sentencia.append(parametros);
    Statement st= null;
    ResultSet rs= null;
    Formatos formatos = null;
    try {
      formatos = new Formatos(sentencia.toString(),objetos);
      st= getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
      rs= st.executeQuery(formatos.getSentencia());
      if(rs.next())
        regresar= new Firmante(Cadena.nombrePersona(rs.getString("NOMBRE")),Cadena.nombrePersona(rs.getString("ADSCRIPCION")));
    } //try
    catch (Exception e) {
      Error.mensaje(e, "SIAFM");
    } //catch
    finally {
      try {
        if (st != null)
          st.close();
        if (rs != null)
          rs.close();
      } //try
      catch (Exception e) {
        Error.mensaje(e, "SIAFM");
      } //catch
    } //finally
    return regresar;
  }
  
  public Firmante getFirmante(int numEmpleado) {
    Object[] objetos= new Object[] {checkParameter("CLAVE_REPORTE"),String.valueOf(numEmpleado), checkParameter("UNIDAD_EJECUTORA"), checkParameter("AMBITO"), checkParameter("ENTIDAD")};
    return getFirma(numEmpleado," and unidad_ejecutora= {2} and ambito= {3} and entidad= {4}", objetos);
  } // getFirmante
  
  public Firmante getFirmante(int numEmpleado, String documento, String tipoFirma) {
    Object[] objetos= new Object[] {documento, String.valueOf(numEmpleado), tipoFirma, checkParameter("UNIDAD_EJECUTORA"), checkParameter("AMBITO"), checkParameter("ENTIDAD")};
    return getFirma(numEmpleado," and fd.tipo_firma= '{2}' and unidad_ejecutora= {3} and ambito= {4} and entidad= {5}", objetos);
  } // getFirmante

  public Firmante getFirmante(int numEmpleado, String documento, String tipoFirma, String unidadEjectura, String ambito, String entidad, String pais) {
    Object[] objetos= new Object[] {documento, String.valueOf(numEmpleado), tipoFirma, unidadEjectura, ambito, entidad, pais};
    return getFirma(numEmpleado, " and fd.tipo_firma= '{2}' and unidad_ejecutora= {3} and ambito= {4} and entidad= {5} and pais= {6}", objetos);
  } // getFirmante
  
  public Firmante getFirmanteDocumento(int numEmpleado, String documento, String tipoFirma) {
    Object[] objetos= new Object[] {documento, tipoFirma, String.valueOf(numEmpleado)};
    return getFirma(numEmpleado, " and fd.tipo_firma= '{1}'", objetos);
  } // getFirmanteDocumento0
  
}
