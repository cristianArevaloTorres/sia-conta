package sia.rf.contabilidad.registroContable.servicios;

import java.sql.Connection ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.Map;

//import oracle.jbo.ViewObject;

import sia.db.dao.DaoFactory;
import sia.db.sql.Sentencias;

import sia.db.sql.Vista;

import sia.libs.formato.Formatos;
import sia.libs.recurso.Contabilidad;



public class CuentaPadre{
  private Integer cuentaContableId;
  private String cuentaContable;
 // private ModuloRegistroContableImpl am;  
  private int ejercicio;
  private int idCatalogoCuenta;

  public CuentaPadre(String cuentaContanble, int ejercicio, int idCatalogoCuenta, Connection conexion) {// reuqiere parametro de conexion
   //setAm(am);
   setEjercicio(ejercicio);
   setIdCatalogoCuenta(idCatalogoCuenta);
   proceso(cuentaContanble, conexion); 
  }
  
  public CuentaPadre(int ejercicio, int idCatalogoCuenta) {
    this(0, "", ejercicio, idCatalogoCuenta);
  }
  
  public CuentaPadre(Integer cuentaContableId,String cuentaContable, int ejercicio, int idCatalogoCuenta) {
    setCuentaContableId(cuentaContableId);
    setCuentaContable(cuentaContable);
    setEjercicio(ejercicio);
    setIdCatalogoCuenta(idCatalogoCuenta);
  }

  public void setCuentaContableId(Integer cuentaContableId) {
    this.cuentaContableId = cuentaContableId;
  }

  public Integer getCuentaContableId() {
    return cuentaContableId;
  }

  public void setCuentaContable(String cuentaContable) {
    this.cuentaContable = cuentaContable;
  }

  public String getCuentaContable() {
    return cuentaContable;
  }
  
  private void proceso(String cuentaContanble, Connection conexion) {// se le acaba de agregar parametro de conexion 
    //ViewObject voCuentaContable= null;    
    //Formatos formatos = null;
    Sentencias sentencia = null;
    List<Vista> cuentaContable = null;
    Map parametros = new HashMap();
    try {
      sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD,Sentencias.XML);
      cuentaContable = new ArrayList<Vista>();
      parametros.put("cuentaContable",cuentaContanble);
      parametros.put("ejercicio",getEjercicio());
      parametros.put("idCatalogoCuenta",getIdCatalogoCuenta());
      cuentaContable = sentencia.registros(sentencia.getComando("webService.select.procesoCuentaPadre",parametros),conexion);// requiere conexion
      //formatos = new Formatos(Contabilidad.getInstance().getPropiedad("cuentaPadre.query.procesoCuentaPadre"),cuentaContanble,String.valueOf(getEjercicio()), String.valueOf(this.getIdCatalogoCuenta()));
      //voCuentaContable= am.createViewObjectFromQueryStmt("VistaCuentaContable",formatos.getSentencia());
      //voCuentaContable.executeQuery();
      //voCuentaContable.first();
      if(cuentaContable != null && cuentaContable.size()> 0) {
        setCuentaContableId(Integer.valueOf(cuentaContable.get(0).getField("CUENTA_CONTABLE_ID")));
        setCuentaContable(cuentaContable.get(0).getField("DESCRIPCION"));
      } // if
      else{
        setCuentaContableId(Integer.valueOf(-1));
        setCuentaContable(null);
      }
    }  
    catch (Exception e) {
      System.err.println("[sia.rf.contabilidad.registroContable.servicios.CuentaPadre.proceso] Error: "+ e);
      e.printStackTrace();
    }  // catch
    finally {
      //voCuentaContable.remove();
      //if(am.findViewObject("VistaCuentaContable")!=null){
      //  am.findViewObject("VistaCuentaContable").remove();
      //}  
      //formatos=null;
      //voCuentaContable=null;
      sentencia = null;
      parametros = null;
      cuentaContable = null;
    } // finally
  }
  
   public String toString(){
     StringBuffer sb = new StringBuffer();
     sb.append("CuentaPadre[");
     sb.append(getCuentaContableId());
     sb.append(",");
     sb.append(getCuentaContable());
     sb.append("]");
     return sb.toString();
   }

  /*  public void setAm(ModuloRegistroContableImpl am) {
        this.am = am;
    }

    public ModuloRegistroContableImpl getAm() {
        return am;
    }*/

  public void setEjercicio(int ejercicio) {
    this.ejercicio = ejercicio;
  }

  public int getEjercicio() {
    return ejercicio;
  }

  public void setIdCatalogoCuenta(int idCatalogoCuenta) {
    this.idCatalogoCuenta = idCatalogoCuenta;
  }

  public int getIdCatalogoCuenta() {
    return idCatalogoCuenta;
  }
}
