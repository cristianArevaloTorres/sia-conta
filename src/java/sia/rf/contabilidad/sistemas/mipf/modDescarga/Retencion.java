package sia.rf.contabilidad.sistemas.mipf.modDescarga;

import java.sql.*;

import sia.db.dao.DaoFactory;

import sun.jdbc.rowset.CachedRowSet;

public class Retencion{ 
   public Retencion(){ 
   } 
   private String retencion_id; 
   private String descripcion; 
   private String clave_ret_ded; 
   private String tipo_gasto; 
   private String par_impuesto_id; 
   private String cuenta; 
   private String nivel5; 
   private String nivel6; 
   private String estatus; 
   private String fecha_inicio; 
   private String fecha_fin; 
  
   /** 
   * retencion_id 
   * @return retencion_id 
   */ 
   public String getRetencion_id() { 
      return retencion_id; 
   } 
  
   /** 
   * retencion_id 
   * @param retencion_id 
   */ 
   public void setRetencion_id( String retencion_id ) { 
      this.retencion_id=retencion_id; 
   } 
  
   /** 
   * descripcion 
   * @return descripcion 
   */ 
   public String getDescripcion() { 
      return descripcion; 
   } 
  
   /** 
   * descripcion 
   * @param descripcion 
   */ 
   public void setDescripcion( String descripcion ) { 
      this.descripcion=descripcion; 
   } 
  
   /** 
   * clave_ret_ded 
   * @return clave_ret_ded 
   */ 
   public String getClave_ret_ded() { 
      return clave_ret_ded; 
   } 
  
   /** 
   * clave_ret_ded 
   * @param clave_ret_ded 
   */ 
   public void setClave_ret_ded( String clave_ret_ded ) { 
      this.clave_ret_ded=clave_ret_ded; 
   } 
  
   /** 
   * tipo_gasto 
   * @return tipo_gasto 
   */ 
   public String getTipo_gasto() { 
      return tipo_gasto; 
   } 
  
   /** 
   * tipo_gasto 
   * @param tipo_gasto 
   */ 
   public void setTipo_gasto( String tipo_gasto ) { 
      this.tipo_gasto=tipo_gasto; 
   } 
  
   /** 
   * par_impuesto_id 
   * @return par_impuesto_id 
   */ 
   public String getPar_impuesto_id() { 
      return par_impuesto_id; 
   } 
  
   /** 
   * par_impuesto_id 
   * @param par_impuesto_id 
   */ 
   public void setPar_impuesto_id( String par_impuesto_id ) { 
      this.par_impuesto_id=par_impuesto_id; 
   } 
  
   /** 
   * cuenta 
   * @return cuenta 
   */ 
   public String getCuenta() { 
      return cuenta; 
   } 
  
   /** 
   * cuenta 
   * @param cuenta 
   */ 
   public void setCuenta( String cuenta ) { 
      this.cuenta=cuenta; 
   } 
  
   /** 
   * nivel5 
   * @return nivel5 
   */ 
   public String getNivel5() { 
      return nivel5; 
   } 
  
   /** 
   * nivel5 
   * @param nivel5 
   */ 
   public void setNivel5( String nivel5 ) { 
      this.nivel5=nivel5; 
   } 
  
   /** 
   * nivel6 
   * @return nivel6 
   */ 
   public String getNivel6() { 
      return nivel6; 
   } 
  
   /** 
   * nivel6 
   * @param nivel6 
   */ 
   public void setNivel6( String nivel6 ) { 
      this.nivel6=nivel6; 
   } 
  
   /** 
   * estatus 
   * @return estatus 
   */ 
   public String getEstatus() { 
      return estatus; 
   } 
  
   /** 
   * estatus 
   * @param estatus 
   */ 
   public void setEstatus( String estatus ) { 
      this.estatus=estatus; 
   } 
  
   /** 
   * fecha_inicio 
   * @return fecha_inicio 
   */ 
   public String getFecha_inicio() { 
      return fecha_inicio; 
   } 
  
   /** 
   * fecha_inicio 
   * @param fecha_inicio 
   */ 
   public void setFecha_inicio( String fecha_inicio ) { 
      this.fecha_inicio=fecha_inicio; 
   } 
  
   /** 
   * fecha_fin 
   * @return fecha_fin 
   */ 
   public String getFecha_fin() { 
      return fecha_fin; 
   } 
  
   /** 
   * fecha_fin 
   * @param fecha_fin 
   */ 
   public void setFecha_fin( String fecha_fin ) { 
      this.fecha_fin=fecha_fin; 
   } 
  
   /** 
   * Metodo que lee la informacion de rf_tc_retenciones 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public CachedRowSet select_rf_tc_retenciones_todas()throws SQLException, Exception{ 
       Connection con2=null;       
       CachedRowSet rsQuery=null;
       StringBuffer SQL=new StringBuffer("");
      try{ 
       con2=DaoFactory.getContabilidad();
       rsQuery = new CachedRowSet();
       SQL.append("SELECT a.retencion_id,a.descripcion,a.clave_ret_ded,a.tipo_gasto,a.par_impuesto_id,a.cuenta,a.nivel5,a.nivel6,a.estatus,a.fecha_inicio,a.fecha_fin,a.tipo_ret");  
       SQL.append(" FROM rf_tc_retenciones a where a.estatus=1");        
       
       rsQuery.setCommand(SQL.toString());
       rsQuery.execute(con2);       

     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo select_rf_tc_retenciones_todas "+e.getMessage()); 
       System.out.println("Query: "+SQL.toString()); 
       throw e; 
     } //Fin catch 
     finally{ 
         if(con2!=null){
            con2.close();
            con2 = null;
         } 
     } //Fin finally 
     return rsQuery;
   } //Fin metodo select_rf_tc_retenciones 
  
    /** 
    * Metodo que lee la informacion de rf_tc_retenciones 
    * Fecha de creacion: 
    * Autor: 
    * Fecha de modificacion: 
    * Modificado por: 
    */ 
    public void select_rf_tc_retenciones(Connection con, String clave)throws SQLException, Exception{ 
       Statement stQuery=null; 
       ResultSet rsQuery=null; 
       try{ 
        stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
        StringBuffer SQL=new StringBuffer("SELECT a.retencion_id,a.descripcion,a.clave_ret_ded,a.tipo_gasto,a.par_impuesto_id,a.cuenta,a.nivel5,a.nivel6,a.estatus,a.fecha_inicio,a.fecha_fin");  
        SQL.append(" FROM rf_tc_retenciones a "); 
        SQL.append(" WHERE a.LLAVE='").append(clave).append("'"); 
        System.out.println(SQL.toString()); 
        rsQuery=stQuery.executeQuery(SQL.toString()); 
        while (rsQuery.next()){ 
           retencion_id=(rsQuery.getString("retencion_id")==null) ? "" : rsQuery.getString("retencion_id"); 
           descripcion=(rsQuery.getString("descripcion")==null) ? "" : rsQuery.getString("descripcion"); 
           clave_ret_ded=(rsQuery.getString("clave_ret_ded")==null) ? "" : rsQuery.getString("clave_ret_ded"); 
           tipo_gasto=(rsQuery.getString("tipo_gasto")==null) ? "" : rsQuery.getString("tipo_gasto"); 
           par_impuesto_id=(rsQuery.getString("par_impuesto_id")==null) ? "" : rsQuery.getString("par_impuesto_id"); 
           cuenta=(rsQuery.getString("cuenta")==null) ? "" : rsQuery.getString("cuenta"); 
           nivel5=(rsQuery.getString("nivel5")==null) ? "" : rsQuery.getString("nivel5"); 
           nivel6=(rsQuery.getString("nivel6")==null) ? "" : rsQuery.getString("nivel6"); 
           estatus=(rsQuery.getString("estatus")==null) ? "" : rsQuery.getString("estatus"); 
           fecha_inicio=(rsQuery.getString("fecha_inicio")==null) ? "" : rsQuery.getString("fecha_inicio"); 
           fecha_fin=(rsQuery.getString("fecha_fin")==null) ? "" : rsQuery.getString("fecha_fin"); 
        } // Fin while 
      } //Fin try 
      catch(Exception e){ 
        System.out.println("Ocurrio un error al accesar al metodo select_rf_tc_retenciones "+e.getMessage()); 
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
    } //Fin metodo select_rf_tc_retenciones 
  
   /** 
   * Metodo que inserta la informacion de rf_tc_retenciones 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void insert_rf_tc_retenciones(Connection con)throws SQLException, Exception{ 
      Statement stQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("INSERT INTO rf_tc_retenciones( retencion_id,descripcion,clave_ret_ded,tipo_gasto,par_impuesto_id,cuenta,nivel5,nivel6,estatus,fecha_inicio,fecha_fin) ");  
       SQL.append("VALUES("); 
       SQL.append(retencion_id).append(","); 
       SQL.append("'").append(descripcion).append("',"); 
       SQL.append("'").append(clave_ret_ded).append("',"); 
       SQL.append(tipo_gasto).append(","); 
       SQL.append(par_impuesto_id).append(","); 
       SQL.append("'").append(cuenta).append("',"); 
       SQL.append("'").append(nivel5).append("',"); 
       SQL.append("'").append(nivel6).append("',"); 
       SQL.append(estatus).append(","); 
       SQL.append("'").append(fecha_inicio).append("',"); 
       SQL.append("'").append(fecha_fin).append("')"); 
       System.out.println(SQL.toString()); 
       int rs=-1; 
       rs=stQuery.executeUpdate(SQL.toString()); 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo insert_rf_tc_retenciones "+e.getMessage()); 
       throw e; 
     } //Fin catch 
     finally{ 
       if (stQuery!=null){ 
         stQuery.close(); 
         stQuery=null; 
       } 
     } //Fin finally 
   } //Fin metodo insert_rf_tc_retenciones 
  
   /** 
   * Metodo que modifica la informacion de rf_tc_retenciones 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void update_rf_tc_retenciones(Connection con, String clave)throws SQLException, Exception{ 
      Statement stQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("UPDATE rf_tc_retenciones"); 
       SQL.append(" SET retencion_id=").append(retencion_id).append(","); 
       SQL.append("descripcion=").append("'").append(descripcion).append("',"); 
       SQL.append("clave_ret_ded=").append("'").append(clave_ret_ded).append("',"); 
       SQL.append("tipo_gasto=").append(tipo_gasto).append(","); 
       SQL.append("par_impuesto_id=").append(par_impuesto_id).append(","); 
       SQL.append("cuenta=").append("'").append(cuenta).append("',"); 
       SQL.append("nivel5=").append("'").append(nivel5).append("',"); 
       SQL.append("nivel6=").append("'").append(nivel6).append("',"); 
       SQL.append("estatus=").append(estatus).append(","); 
       SQL.append("fecha_inicio=").append("'").append(fecha_inicio).append("',"); 
       SQL.append("fecha_fin=").append("'").append(fecha_fin); 
       SQL.append(" WHERE LLAVE='").append(clave).append("'"); 
       System.out.println(SQL.toString()); 
       int rs=-1; 
       rs=stQuery.executeUpdate(SQL.toString()); 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo update_rf_tc_retenciones "+e.getMessage()); 
       throw e; 
     } //Fin catch 
     finally{ 
       if (stQuery!=null){ 
         stQuery.close(); 
         stQuery=null; 
       } 
     } //Fin finally 
   } //Fin metodo update_rf_tc_retenciones 
  
   /** 
   * Metodo que borra la informacion de rf_tc_retenciones 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void delete_rf_tc_retenciones(Connection con, String clave)throws SQLException, Exception{ 
      Statement stQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("DELETE FROM rf_tc_retenciones a "); 
       SQL.append("WHERE a.LLAVE='").append(clave).append("'"); 
       System.out.println(SQL.toString()); 
       int rs=-1; 
       rs=stQuery.executeUpdate(SQL.toString()); 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo delete_rf_tc_retenciones "+e.getMessage()); 
       throw e; 
     } //Fin catch 
     finally{ 
       if (stQuery!=null){ 
         stQuery.close(); 
         stQuery=null; 
       } 
     } //Fin finally 
   } //Fin metodo delete_rf_tc_retenciones 
} //Fin clase Retencion 
    
