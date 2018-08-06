package sia.rf.contabilidad.registroContableNuevo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import sia.rf.contabilidad.registroContableNuevo.bloqueo.BloquearUnidades;

public class bcUnidadesEjec {
  public bcUnidadesEjec() {
  }
  
  private String unidadEjecutora;
  private String descripUnidad;
  private String entidad;
  private String descripEntidad;
  private String ambito;  
  private String descripAmbito;  
  private String sede;
  private String pais;
  private String tipo;   
  private String programa;
  
  
  private Vector vUnidad;
  private Vector vEntidad;
  private Vector vAmbito;
  private Vector vPrograma;
  private List<BloquearUnidades> listUnidades; 


  public void setUnidadEjecutora(String unidadEjecutora) {
    this.unidadEjecutora = unidadEjecutora;
  }

  public String getUnidadEjecutora() {
    return unidadEjecutora;
  }

  public void setDescripUnidad(String descripUnidad) {
    this.descripUnidad = descripUnidad;
  }

  public String getDescripUnidad() {
    return descripUnidad;
  }

  public void setEntidad(String entidad) {
    this.entidad = entidad;
  }

  public String getEntidad() {
    return entidad;
  }

  public void setDescripEntidad(String descripEntidad) {
    this.descripEntidad = descripEntidad;
  }

  public String getDescripEntidad() {
    return descripEntidad;
  }

  public void setAmbito(String ambito) {
    this.ambito = ambito;
  }

  public String getAmbito() {
    return ambito;
  }

  public void setDescripAmbito(String descripAmbito) {
    this.descripAmbito = descripAmbito;
  }

  public String getDescripAmbito() {
    return descripAmbito;
  }

  public void setSede(String sede) {
    this.sede = sede;
  }

  public String getSede() {
    return sede;
  }

  public void setPais(String pais) {
    this.pais = pais;
  }

  public String getPais() {
    return pais;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public String getTipo() {
    return tipo;
  }
  
  public void setPrograma(String programa) {
      this.programa = programa;
  }

  public String getPrograma() {
      return programa;
  }   
  
  public void setVUnidad(Vector vUnidad) {
    this.vUnidad = vUnidad;
  }

  public Vector getVUnidad() {
    return vUnidad;
  }
  
  public void setVEntidad(Vector vEntidad) {
    this.vEntidad = vEntidad;
  }

  public Vector getVEntidad() {
    return vEntidad;
  }

  public void setVAmbito(Vector vAmbito) {
    this.vAmbito = vAmbito;
  }

  public Vector getVAmbito() {
    return vAmbito;
  }  
  
  public void setVPrograma(Vector vPrograma) {
    this.vPrograma = vPrograma;
  }

  public Vector getVPrograma() {
    return vPrograma;
  }
  
  public void setListUnidades(List<BloquearUnidades> listUnidades) {
    this.listUnidades = listUnidades;
  }

  public List<BloquearUnidades> getListUnidades() {
    return listUnidades;
  }


  /** 
  * Metodo que lee la informacion de TC_UNI_EJECUTORAS 
  * Fecha de creacion: 12/11/2009
  * Autor: Claudia Luz Hernandez Lopez
  * Fecha de modificacion: 
  * Modificado por: 
  */ 
  public void select_UnidadesEjecporUnidad(Connection con, String pUnidad_ejecutora,String pEjecicio,String pIdCatCuenta,String pPrograma)throws SQLException, Exception{ 
     Statement stQuery=null; 
     ResultSet rsQuery=null; 
     StringBuffer SQL=new StringBuffer("");
     try{ 
        stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
        SQL.append("  select lpad(unidad,3,0) unidadEjecutora, desunidad as descripUnidad, y.entidad, descripcion descripEntidad, y.ambito, desc_ambito as descripAmbito,sede,tipo   "); 
        SQL.append(" from ( ");
        SQL.append(" select u.unidad_ejecutora, u.descripcion desunidad, e.entidad, e.descripcion, 2 ambito, 'Regional' desc_ambito,ue.sede, 1 tipo ");
        SQL.append(" from ");
        SQL.append(" rh_tc_reg_entidad ue ");
        SQL.append(" inner join rh_tc_entidades e on e.entidad=ue.entidad ");
        SQL.append(" inner join rh_tc_entidades u on u.unidad_ejecutora=ue.unidad_ejecutora ");         
        SQL.append(" and u.ambito=2 and ue.sede=1 ");
        SQL.append(" union ");
        SQL.append(" select u.unidad_ejecutora, u.descripcion desunidad, e.entidad, e.descripcion, decode(u.ambito,2,decode(ue.sede,0,3,1,3),1) ambito, decode(u.ambito,2,decode(ue.sede,0,'Estatal',1,'Estatal'),'Central') desc_ambito,ue.sede,1 tipo ");
        SQL.append(" from ");
        SQL.append(" rh_tc_reg_entidad ue ");
        SQL.append(" inner join rh_tc_entidades e on e.entidad=ue.entidad ");
        SQL.append(" inner join rh_tc_entidades u on u.unidad_ejecutora=ue.unidad_ejecutora ");         
        SQL.append(" ) x, ");
        SQL.append("   (select distinct to_char(to_number(substr(t.cuenta_contable,10,4))) unidad,to_char(to_number(substr(t.cuenta_contable,14,3))) entidad,to_char(to_number(substr(t.cuenta_contable,17,1))) ambito,substr(t.cuenta_contable,6,4) programa from rf_tr_cuentas_contables t ");
        SQL.append("    where  substr(t.cuenta_contable,10,4)<>'0000' and to_number(substr(t.cuenta_contable,14,3))<>'0' and to_number(substr(t.cuenta_contable,17,1))<>0  ");
        SQL.append("    and t.id_catalogo_cuenta='").append(pIdCatCuenta).append("' and extract(year from t.fecha_vig_ini) = '").append(pEjecicio).append("' and substr(t.cuenta_contable,6,4)='").append(pPrograma).append("' ");
        SQL.append("    ) y ");
        SQL.append("  where  x.unidad_ejecutora(+)=y.unidad and x.entidad(+)=y.entidad  and x.ambito(+)=y.ambito and y.unidad='").append(pUnidad_ejecutora).append("' ");
        SQL.append(" order by ambito desc ");         
        rsQuery=stQuery.executeQuery(SQL.toString()); 
        
        vEntidad = new Vector();
        vAmbito = new Vector();
        setListUnidades(new ArrayList<BloquearUnidades>());
        
        while (rsQuery.next()){ 
          unidadEjecutora=(rsQuery.getString("unidadEjecutora")==null) ? "" : rsQuery.getString("unidadEjecutora"); 
          descripUnidad=(rsQuery.getString("descripUnidad")==null) ? "" : rsQuery.getString("descripUnidad"); 
          entidad=(rsQuery.getString("entidad")==null) ? "" : rsQuery.getString("entidad"); 
          descripEntidad=(rsQuery.getString("descripEntidad")==null) ? "" : rsQuery.getString("descripEntidad"); 
          ambito=(rsQuery.getString("ambito")==null) ? "" : rsQuery.getString("ambito"); 
          descripAmbito=(rsQuery.getString("descripAmbito")==null) ? "" : rsQuery.getString("descripAmbito"); 
          sede=(rsQuery.getString("sede")==null) ? "" : rsQuery.getString("sede");         
          tipo=(rsQuery.getString("tipo")==null) ? "" : rsQuery.getString("tipo"); 
         
          vEntidad.addElement(entidad);
          vAmbito.addElement(ambito);
          getListUnidades().add(new BloquearUnidades(pUnidad_ejecutora,ambito,entidad,pPrograma));
        } // Fin while 
    } //Fin try 
    catch(Exception e){ 
      System.out.println(SQL.toString()); 
      System.out.println("Ocurrio un error al accesar al metodo select_UnidadesEjec "+e.getMessage()); 
      throw e; 
    } //Fin catch 
    finally{ 
      if (rsQuery!=null){ 
        rsQuery.close(); 
        rsQuery=null; 
      } 
      if (stQuery!=null){ 
        stQuery.close(); 
        stQuery=null; 
      } 
    } //Fin finally 
  } //Fin metodo select_UnidadesEjecporUnidad 
  
  

   /** 
   * Metodo que lee la informacion de TC_UNI_EJECUTORAS 
   * Fecha de creacion: 20/11/2009
   * Autor: Claudia Luz Hernandez Lopez
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void select_UnidadesEjecTodas(Connection con,String pEjecicio,String pIdCatCuenta,String pPrograma)throws SQLException, Exception{ 
      Statement stQuery=null; 
      ResultSet rsQuery=null; 
      StringBuffer SQL=new StringBuffer("");
      try{ 
         stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
    
  
         SQL.append(" select programa,lpad(unidad,3,0) unidadEjecutora, desunidad as descripUnidad, y.entidad, x.descripcion descripEntidad, y.ambito, desc_ambito as descripAmbito,sede, tipo ");
         SQL.append("   from ( ");
         SQL.append("   select u.unidad_ejecutora, u.descripcion desunidad, e.entidad, e.descripcion, 2 ambito, 'Regional' desc_ambito,ue.sede, 1 tipo ");
         SQL.append("   from ");
         SQL.append("   rh_tc_reg_entidad ue ");
         SQL.append("   inner join rh_tc_entidades e on e.entidad=ue.entidad ");
         SQL.append("   inner join rh_tc_uni_ejecutoras u on u.unidad_ejecutora=ue.unidad_ejecutora ");
         SQL.append("   and u.ambito=2 and ue.sede=1 ");
         SQL.append("   union ");
         SQL.append("   select u.unidad_ejecutora, u.descripcion desunidad, e.entidad, e.descripcion, decode(u.ambito,2,decode(ue.sede,0,3,1,3),1) ambito, decode(u.ambito,2,decode(ue.sede,0,'Estatal',1,'Estatal'),'Central') desc_ambito,ue.sede, 1 tipo ");
         SQL.append("   from ");
         SQL.append("   rh_tc_reg_entidad ue ");
         SQL.append("   inner join rh_tc_entidades e on e.entidad=ue.entidad ");
         SQL.append("   inner join rh_tc_uni_ejecutoras u on u.unidad_ejecutora=ue.unidad_ejecutora ");         
         SQL.append("   ) x, ");
         SQL.append("   (select distinct to_char(to_number(substr(t.cuenta_contable,10,4))) unidad,to_char(to_number(substr(t.cuenta_contable,14,3))) entidad,to_char(to_number(substr(t.cuenta_contable,17,1))) ambito,substr(t.cuenta_contable,6,4) programa from rf_tr_cuentas_contables t ");
         SQL.append("    where  substr(t.cuenta_contable,10,4)<>'0000' and to_number(substr(t.cuenta_contable,14,3))<>'0' and to_number(substr(t.cuenta_contable,17,1))<>0  ");
         SQL.append("    and t.id_catalogo_cuenta='").append(pIdCatCuenta).append("' and extract(year from t.fecha_vig_ini) = '").append(pEjecicio).append("' and substr(t.cuenta_contable,6,4)='").append(pPrograma).append("' ");
         SQL.append("    ) y ");
         SQL.append("   where x.unidad_ejecutora(+)=y.unidad and x.entidad(+)=y.entidad  and x.ambito(+)=y.ambito ");
         SQL.append("   order by unidad_ejecutora,entidad,ambito,programa ");     
         System.out.println("Todas las unidades: "+SQL.toString()); 
  
         rsQuery=stQuery.executeQuery(SQL.toString()); 
      
         vUnidad = new Vector();   
         vEntidad = new Vector();
         vAmbito = new Vector();
         vPrograma = new Vector();
         while (rsQuery.next()){ 
           unidadEjecutora=(rsQuery.getString("unidadEjecutora")==null) ? "" : rsQuery.getString("unidadEjecutora"); 
           descripUnidad=(rsQuery.getString("descripUnidad")==null) ? "" : rsQuery.getString("descripUnidad"); 
           entidad=(rsQuery.getString("entidad")==null) ? "" : rsQuery.getString("entidad"); 
           descripEntidad=(rsQuery.getString("descripEntidad")==null) ? "" : rsQuery.getString("descripEntidad"); 
           ambito=(rsQuery.getString("ambito")==null) ? "" : rsQuery.getString("ambito"); 
           descripAmbito=(rsQuery.getString("descripAmbito")==null) ? "" : rsQuery.getString("descripAmbito"); 
           sede=(rsQuery.getString("sede")==null) ? "" : rsQuery.getString("sede");             
           tipo=(rsQuery.getString("tipo")==null) ? "" : rsQuery.getString("tipo"); 
           programa=(rsQuery.getString("programa")==null) ? "" : rsQuery.getString("programa"); 
          
           vUnidad.addElement(unidadEjecutora);            
           vEntidad.addElement(entidad);
           vAmbito.addElement(ambito);
           vPrograma.addElement(programa);
         } // Fin while 
     } //Fin try 
     catch(Exception e){ 
       System.out.println(SQL.toString()); 
       System.out.println("Ocurrio un error al accesar al metodo select_UnidadesEjecTodas "+e.getMessage()); 
       throw e; 
     } //Fin catch 
     finally{ 
       if (rsQuery!=null){ 
         rsQuery.close(); 
         rsQuery=null; 
       } 
       if (stQuery!=null){ 
         stQuery.close(); 
         stQuery=null; 
       } 
     } //Fin finally 
   } //Fin metodo select_UnidadesEjecTodas 
}
