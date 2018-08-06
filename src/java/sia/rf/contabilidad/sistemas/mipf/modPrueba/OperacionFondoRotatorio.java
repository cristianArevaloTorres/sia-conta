package sia.rf.contabilidad.sistemas.mipf.modPrueba;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.HashMap;

import sia.rf.contabilidad.registroContableEvento.Cadena;
import sun.jdbc.rowset.CachedRowSet;


public class OperacionFondoRotatorio {
  public OperacionFondoRotatorio() {
  }
  private String unidad;
  private String entidad;
  private String ambito;
  private String consecutivo;
  private String ejercicio;
  private String id_status;
  private String id_instrumento_pago;
  private String importe;
  private String concepto;


  public void setUnidad(String unidad) {
    this.unidad = unidad;
  }

  public String getUnidad() {
    return unidad;
  }

  public void setEntidad(String entidad) {
    this.entidad = entidad;
  }

  public String getEntidad() {
    return entidad;
  }

  public void setAmbito(String ambito) {
    this.ambito = ambito;
  }

  public String getAmbito() {
    return ambito;
  }

  public void setConsecutivo(String consecutivo) {
    this.consecutivo = consecutivo;
  }

  public String getConsecutivo() {
    return consecutivo;
  }

  public void setEjercicio(String ejercicio) {
    this.ejercicio = ejercicio;
  }

  public String getEjercicio() {
    return ejercicio;
  }

  public void setId_status(String id_status) {
    this.id_status = id_status;
  }

  public String getId_status() {
    return id_status;
  }

  public void setId_instrumento_pago(String id_instrumento_pago) {
    this.id_instrumento_pago = id_instrumento_pago;
  }

  public String getId_instrumento_pago() {
    return id_instrumento_pago;
  }

  public void setImporte(String importe) {
    this.importe = importe;
  }

  public String getImporte() {
    return importe;
  }

  public void setConcepto(String concepto) {
    this.concepto = concepto;
  }

  public String getConcepto() {
    return concepto;
  }

  public String registroOFR (Connection con, String ejercicio, String numeroDocumento, String estatus) throws SQLException, Exception {
    HashMap hm = new HashMap();
    CachedRowSet crs = null;
    boolean bandera=false;
    StringBuffer cadenaTem= new StringBuffer("");
    StringBuffer cadena= new StringBuffer("");
    StringBuffer SQL = new StringBuffer("SELECT ")
    .append(" '0001' as programa,")
    .append(" substr(t.consecutivo,1,3) as unidad,")
    .append(" to_number(substr(t.consecutivo,4,2)) entidad,")
    .append(" lpad(substr(t.consecutivo,4,2),3,'0') || substr(t.consecutivo,6,1) as ambito,")
    .append(" t.consecutivo,")
    .append(" t.ejercicio,")
    .append(" t.id_status,")
    .append(" decode(t.id_instrumento_pago,9,'0001',3,'0002',10,'0003',8,'0005') as id_instrumento_pago,")
    .append(" t.importe,")
    .append(" replace(replace(t.concepto,chr(39),''),chr(13),'') as concepto ")
    .append(" from sapfin_pa.rf_tr_ofr t")
    .append(" where t.id_status=").append(estatus)
    .append(" and t.id_instrumento_pago<>3 ") // se agrego condicion por solicitud de usuario Ago-2013
    .append(" and t.ejercicio=").append(ejercicio)
    .append(" and t.consecutivo='").append(numeroDocumento).append("'");

    try {
    crs = new CachedRowSet();
    crs.setCommand(SQL.toString());      
    crs.execute(con);
    crs.beforeFirst();
    while (crs.next()){
          bandera=true; 
          hm.put ("PROGRAMA",crs.getString("programa"));//imprimeHM(hm);
          hm.put ("UNIDAD",crs.getString("unidad"));//imprimeHM(hm);          
          hm.put ("AMBITO",crs.getString("ambito"));//imprimeHM(hm);
          hm.put ("TIPO",crs.getString("id_instrumento_pago"));//imprimeHM(hm);
          hm.put ("REFERENCIA",crs.getString("consecutivo"));//imprimeHM(hm);
          hm.put ("IMPORTE",crs.getString("importe"));//imprimeHM(hm);
          unidad=crs.getString("unidad");
          entidad=crs.getString("entidad");
          ambito=crs.getString("ambito");
          concepto=crs.getString("concepto");
          cadenaTem.append(Cadena.construyeCadena(hm));
          cadenaTem.append("~");
          hm.clear();
        }        
        if (bandera==true){
            cadena.append(cadenaTem.toString()); 
        }  
    
    }catch (Exception e){
       System.out.println ("Ha ocurrido un error en el metodo registroOFR");
       System.out.println ("registroOFR:" + SQL.toString());
       crs.close();
       crs=null;
       throw e;
    } finally {
      SQL.setLength(0);
      SQL=null;
      crs=null;
    }
    return cadena.toString();
  }    
  public String registroOFR_ARE (Connection con, String ejercicio, String numeroDocumento, String estatus, String tipo_ar) throws SQLException, Exception {
    HashMap hm = new HashMap();
    CachedRowSet crs = null;
    boolean bandera=false;
    StringBuffer cadenaTem= new StringBuffer("");
    StringBuffer cadena= new StringBuffer("");
    StringBuffer SQL = new StringBuffer("SELECT ")    
    .append(" '0001' as programa, ")
    .append(" substr(t.consecutivo,1,3) as unidad,")
    .append(" to_number(substr(t.consecutivo,4,2)) entidad,")
    .append(" lpad(substr(t.consecutivo,4,2),3,'0') || substr(t.consecutivo,6,1) as ambito,")
    .append(" t.consecutivo,")
    .append(" t.ejercicio,")
    .append(" t.id_status,")
    .append(" decode(f.id_instrumento_pago,9,'0001',3,'0002',10,'0003',8,'0005') as id_instrumento_pago,")
    .append(" t.importe_bruto as importe,")
    .append(" replace(replace(t.concepto,chr(39),''),chr(13),'') as concepto")
    .append(" from sapfin_pa.rf_tr_ar t, sapfin_pa.rf_tr_ofr f")
    .append(" where t.id_documento=f.id_ofr ")
    .append(" and t.tipo_ar='").append(tipo_ar).append("' ")
    .append(" and t.tipo_docto='OFR'")    
    .append(" and t.id_status=").append(estatus)
    .append(" and t.ejercicio=").append(ejercicio)
    .append(" and t.consecutivo='").append(numeroDocumento).append("'");

    try {
    crs = new CachedRowSet();
    crs.setCommand(SQL.toString());      
    crs.execute(con);
    crs.beforeFirst();
    while (crs.next()){
          bandera=true; 
          hm.put ("PROGRAMA",crs.getString("programa"));//imprimeHM(hm);
          hm.put ("UNIDAD",crs.getString("unidad"));//imprimeHM(hm);          
          hm.put ("AMBITO",crs.getString("ambito"));//imprimeHM(hm);
          hm.put ("TIPO",crs.getString("id_instrumento_pago"));//imprimeHM(hm);
          hm.put ("REFERENCIA",crs.getString("consecutivo"));//imprimeHM(hm);
          hm.put ("IMPORTE",crs.getString("importe"));//imprimeHM(hm);
          unidad=crs.getString("unidad");
          entidad=crs.getString("entidad");
          ambito=crs.getString("ambito");
          concepto=crs.getString("concepto");
          cadenaTem.append(Cadena.construyeCadena(hm));
          cadenaTem.append("~");
          hm.clear();
        }        
        if (bandera==true){
            cadena.append(cadenaTem.toString()); 
        }  
    
    }catch (Exception e){
       System.out.println ("Ha ocurrido un error en el metodo registroOFR_ARE");
       System.out.println ("registroOFR_ARE:" + SQL.toString());
       crs.close();
       crs=null;
       throw e;
    } finally {
      SQL.setLength(0);
      SQL=null;
      crs=null;
    }
    return cadena.toString();
  }    

}
