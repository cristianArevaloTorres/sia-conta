package sia.rf.contabilidad.reportes;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.sf.jasperreports.engine.JRScriptletException;

import sia.db.dao.DaoFactory;

import sia.libs.formato.Cadena;
import sia.libs.formato.Error;
import sia.libs.formato.Formatos;

import sia.scriptlets.BarraProgreso;
import sia.scriptlets.Firmante;

public class Firmas extends BarraProgreso {

  public Firmante getFirma(String firma, String unidadEjecutora, String entidad, String ambito, String ejercicio, String mes, String documento, String numEmpleado) {
    Firmante regresar     = null;
    StringBuffer query= new StringBuffer();
    Connection conexion = null;
    /*sentencia.append("select ");
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
    sentencia.append("fd.documento= '{0}' and fa.num_empleado=".concat(String.valueOf(numEmpleado)));*/
    
    
    query.append("select  ");
    query.append("  upper(fa.prefijo||' '||fa.nombres||' '||fa.apellido_pat||' '||fa.apellido_mat) nombre, upper(fa.puesto_firma) puesto ");
    query.append("from ");
    query.append("  rf_tr_documentos_firmas df ");
    query.append("inner join  rf_tr_documentos_contables doc on   df.documento_contable_id=doc.documento_contable_id     ");
    query.append("inner join   rf_tc_firmas_autorizadas fa on   df.num_empleado = fa.num_empleado ");
    query.append("where df.firma='"+firma+"' and doc.unidad_ejecutora='"+unidadEjecutora+"' and doc.entidad="+entidad+" and doc.ambito="+ambito+" ");
    query.append("and doc.mes="+mes+" and fa.mes=doc.mes "+" and doc.ejercicio="+ejercicio+" and fa.ejercicio=doc.ejercicio "+" and doc.documento='"+documento+"' ");
    query.append("and fa.num_empleado="+numEmpleado);
    
    /*
     * select t.unidad_ejecutora,t.ambito,t.entidad,t.mes,t.ejercicio,t.documento, count from rf_tr_documentos_contables t
group by t.unidad_ejecutora,t.ambito,t.entidad,t.mes,t.ejercicio,t.documento

     * 
     * 
     * 
     * 
     * sentencia.append("SELECT a.documento,a.descripcionAut,a.nombreAut,a.puestoAut,b.descripcionRev,b.nombreRev,b.puestoRev,c.descripcionEla,c.nombreEla,c.puestoEla ");
    sentencia.append("FROM( ");
    sentencia.append("select  ");
    sentencia.append("  doc.documento,decode(df.firma,'AUT','AUTORIZO')  descripcionAut, ");
    sentencia.append("  upper(fa.nombre||' '||fa.apellido_pat||' '||fa.apellido_mat) nombreAut, upper(fa.puesto_esp) puestoAut, ");
    sentencia.append("  df.num_empleado,doc.unidad_ejecutora,doc.entidad,doc.ambito,doc.mes,doc.ejercicio ");
    sentencia.append("from ");
    sentencia.append("  rf_tr_documentos_firmas df ");
    sentencia.append("inner join  rf_tr_documentos_contables doc on   df.documento_contable_id=doc.documento_contable_id     ");
    sentencia.append("inner join   sia_admin.rh_tc_firmas_autorizadas fa on   df.num_empleado = fa.num_empleado ");
    sentencia.append("where df.firma='AUT' and doc.unidad_ejecutora='109' and doc.entidad=1 and doc.ambito=1 and doc.mes=9 and doc.ejercicio=2010  ");
    sentencia.append(") A, ");
    sentencia.append("(select  ");
    sentencia.append("  doc.documento,decode(df.firma,'REV','REVISO')  descripcionRev, ");
    sentencia.append("  upper(fa.nombre||' '||fa.apellido_pat||' '||fa.apellido_mat) nombreRev, upper(fa.puesto_esp) puestoRev, ");
    sentencia.append("  df.num_empleado,doc.unidad_ejecutora,doc.entidad,doc.ambito,doc.mes,doc.ejercicio ");
    sentencia.append("from ");
    sentencia.append("  rf_tr_documentos_firmas df ");
    sentencia.append("inner join  rf_tr_documentos_contables doc on   df.documento_contable_id=doc.documento_contable_id     ");
    sentencia.append("inner join   sia_admin.rh_tc_firmas_autorizadas fa on   df.num_empleado = fa.num_empleado ");
    sentencia.append("where df.firma='REV' and doc.unidad_ejecutora='109' and doc.entidad=1 and doc.ambito=1 and doc.mes=9 and doc.ejercicio=2010  ");
    sentencia.append(") B, ");
    sentencia.append("(select  ");
    sentencia.append("  doc.documento,decode(df.firma,'ELB','ELABORO')  descripcionEla, ");
    sentencia.append("  upper(fa.nombre||' '||fa.apellido_pat||' '||fa.apellido_mat) nombreEla, upper(fa.puesto_esp) puestoEla, ");
    sentencia.append("  df.num_empleado,doc.unidad_ejecutora,doc.entidad,doc.ambito,doc.mes,doc.ejercicio ");
    sentencia.append("from ");
    sentencia.append("  rf_tr_documentos_firmas df ");
    sentencia.append("inner join  rf_tr_documentos_contables doc on   df.documento_contable_id=doc.documento_contable_id     ");
    sentencia.append("inner join   sia_admin.rh_tc_firmas_autorizadas fa on   df.num_empleado = fa.num_empleado ");
    sentencia.append("where df.firma='ELB' and doc.unidad_ejecutora='109' and doc.entidad=1 and doc.ambito=1 and doc.mes=9 and doc.ejercicio=2010  ");
    sentencia.append(") C ");
    sentencia.append("where a.documento=b.documento and a.documento=c.documento and a.documento='BCO' ");*/

    Statement st= null;
    ResultSet rs= null;
    Formatos formatos = null;
    try {
      conexion = DaoFactory.getContabilidad();
      formatos = new Formatos(query.toString());
      st= conexion.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
      rs= st.executeQuery(formatos.getSentencia());
      if(rs.next())
        regresar= new Firmante(rs.getString("NOMBRE"),rs.getString("PUESTO"));
    } //try
    catch (Exception e) {
      Error.mensaje(e, "SIAFM");
    } //catch
    finally {
      try {
        if (rs != null)
            rs.close();
        if (st != null)
          st.close();
        if (conexion != null)
            conexion.close();
           
      } //try
      catch (Exception e) {
        Error.mensaje(e, "SIAFM");
      } //catch
    } //finally
    return regresar;
  }
  
  public String getFechaFirma(int iEntidad, int ambito, int unidadEjecutora){
    Entidad entidad = null;
    String regresa = null;
    try{
      entidad = new Entidad();
      regresa = entidad.getEntidad(iEntidad,ambito,unidadEjecutora);
      if(regresa==null)
        regresa = "";
      else
        regresa = entidad.getEntidad(iEntidad,ambito,unidadEjecutora).concat(" a ").concat(getFecha());
    }
    catch(Exception e){
      e.printStackTrace();
    }
    return regresa;
  }
  
    public void afterDetailEval() throws JRScriptletException{
      super.afterDetailEval();
    }
}
