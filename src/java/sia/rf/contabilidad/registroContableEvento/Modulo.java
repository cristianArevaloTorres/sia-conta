package sia.rf.contabilidad.registroContableEvento;

import java.sql.*;

import sun.jdbc.rowset.CachedRowSet;

public class Modulo{ 
   public Modulo(){ 
   } 
   private String idmodulo; 
   private String idsistema; 
   private String descripcion; 
   private String fechaalta; 
   private String fechabaja; 
   private String observacion; 
   private String estatus; 
  
   /** 
   * idmodulo 
   * @return idmodulo 
   */ 
   public String getIdmodulo() { 
      return idmodulo; 
   } 
  
   /** 
   * idmodulo 
   * @param idmodulo 
   */ 
   public void setIdmodulo( String idmodulo ) { 
      this.idmodulo=idmodulo; 
   } 
  
   /** 
   * idsistema 
   * @return idsistema 
   */ 
   public String getIdsistema() { 
      return idsistema; 
   } 
  
   /** 
   * idsistema 
   * @param idsistema 
   */ 
   public void setIdsistema( String idsistema ) { 
      this.idsistema=idsistema; 
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
   * fechaalta 
   * @return fechaalta 
   */ 
   public String getFechaalta() { 
      return fechaalta; 
   } 
  
   /** 
   * fechaalta 
   * @param fechaalta 
   */ 
   public void setFechaalta( String fechaalta ) { 
      this.fechaalta=fechaalta; 
   } 
  
   /** 
   * fechabaja 
   * @return fechabaja 
   */ 
   public String getFechabaja() { 
      return fechabaja; 
   } 
  
   /** 
   * fechabaja 
   * @param fechabaja 
   */ 
   public void setFechabaja( String fechabaja ) { 
      this.fechabaja=fechabaja; 
   } 
  
   /** 
   * observacion 
   * @return observacion 
   */ 
   public String getObservacion() { 
      return observacion; 
   } 
  
   /** 
   * observacion 
   * @param observacion 
   */ 
   public void setObservacion( String observacion ) { 
      this.observacion=observacion; 
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
   * Metodo que lee la informacion de rf_tr_modulo 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void select_rf_tr_modulo(Connection con, String clave)throws SQLException, Exception{ 
      Statement stQuery=null; 
      ResultSet rsQuery=null; 
      StringBuffer SQL=new StringBuffer("");  
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       SQL.append("SELECT a.idmodulo,a.idsistema,a.descripcion,a.fechaalta,a.fechabaja,a.observacion,a.estatus");  
       SQL.append(" FROM rf_tr_modulo a "); 
       SQL.append(" WHERE a.idmodulo=").append(clave).append(" ");        

       rsQuery=stQuery.executeQuery(SQL.toString()); 
       while (rsQuery.next()){ 
          idmodulo=(rsQuery.getString("idmodulo")==null) ? "" : rsQuery.getString("idmodulo"); 
          idsistema=(rsQuery.getString("idsistema")==null) ? "" : rsQuery.getString("idsistema"); 
          descripcion=(rsQuery.getString("descripcion")==null) ? "" : rsQuery.getString("descripcion"); 
          fechaalta=(rsQuery.getString("fechaalta")==null) ? "" : rsQuery.getString("fechaalta"); 
          fechabaja=(rsQuery.getString("fechabaja")==null) ? "" : rsQuery.getString("fechabaja"); 
          observacion=(rsQuery.getString("observacion")==null) ? "" : rsQuery.getString("observacion"); 
          estatus=(rsQuery.getString("estatus")==null) ? "" : rsQuery.getString("estatus"); 
       } // Fin while 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo select_rf_tr_modulo "+e.getMessage()); 
       System.out.println("Query método select_rf_tr_modulo: "+SQL.toString()); 
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
   } //Fin metodo select_rf_tr_modulo 
  
  
    /** 
    * Descripcion: Lista los registros de rf_tr_modulo para mostrar en  los combos
    * Fecha de creacion: 20/01/2011
    * Autor: 
    * Fecha de modificacion: 
    * Modificado por: 
    */ 
    public CachedRowSet select_rf_tr_modulo_cacheRowSet(Connection con,String sistema)throws SQLException, Exception{ 
      CachedRowSet crs = null;     
      StringBuffer SQL=null;
       try{ 
        crs = new CachedRowSet();
        SQL=new StringBuffer("SELECT a.idmodulo,a.idsistema,a.descripcion,a.fechaalta,a.fechabaja,a.observacion,a.estatus");  
        SQL.append(" FROM rf_tr_modulo a "); 
        SQL.append(" WHERE idsistema= ").append(sistema); 
        SQL.append(" ORDER BY a.idsistema,a.idmodulo");
        System.out.println(SQL.toString()); 
        crs.setCommand(SQL.toString());
        crs.execute(con);        
      } //Fin try 
      catch(Exception e){ 
        System.out.println("Ocurrio un error al accesar al metodo select_rf_tr_modulo filtrado por idsistema"+e.getMessage()); 
        crs.close();
        crs = null;
        throw e; 
      } //Fin catch 
      finally{ 
        SQL.setLength(0);
        SQL = null;
      } //Fin finally 
       return crs;
    } //Fin metodo select_rf_tr_modulo 
    
   /** 
   * Metodo que inserta la informacion de rf_tr_modulo 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void insert_rf_tr_modulo(Connection con)throws SQLException, Exception{ 
      Statement stQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("INSERT INTO rf_tr_modulo( idmodulo,idsistema,descripcion,fechaalta,fechabaja,observacion,estatus) ");  
       SQL.append("VALUES("); 
       SQL.append(idmodulo).append(","); 
       SQL.append(idsistema).append(","); 
       SQL.append("'").append(descripcion).append("',"); 
       SQL.append("'").append(fechaalta).append("',"); 
       SQL.append("'").append(fechabaja).append("',"); 
       SQL.append("'").append(observacion).append("',"); 
       SQL.append("'").append(estatus).append("')"); 
       System.out.println(SQL.toString()); 
       int rs=-1; 
       rs=stQuery.executeUpdate(SQL.toString()); 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo insert_rf_tr_modulo "+e.getMessage()); 
       throw e; 
     } //Fin catch 
     finally{ 
       if (stQuery!=null){ 
         stQuery.close(); 
         stQuery=null; 
       } 
     } //Fin finally 
   } //Fin metodo insert_rf_tr_modulo 
  
   /** 
   * Metodo que modifica la informacion de rf_tr_modulo 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void update_rf_tr_modulo(Connection con, String clave)throws SQLException, Exception{ 
      Statement stQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("UPDATE rf_tr_modulo"); 
       SQL.append(" SET idmodulo=").append(idmodulo).append(","); 
       SQL.append("idsistema=").append(idsistema).append(","); 
       SQL.append("descripcion=").append("'").append(descripcion).append("',"); 
       SQL.append("fechaalta=").append("'").append(fechaalta).append("',"); 
       SQL.append("fechabaja=").append("'").append(fechabaja).append("',"); 
       SQL.append("observacion=").append("'").append(observacion).append("',"); 
       SQL.append("estatus=").append("'").append(estatus); 
       SQL.append(" WHERE idmodulo=").append(clave).append(" "); 
       System.out.println(SQL.toString()); 
       int rs=-1; 
       rs=stQuery.executeUpdate(SQL.toString()); 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo update_rf_tr_modulo "+e.getMessage()); 
       throw e; 
     } //Fin catch 
     finally{ 
       if (stQuery!=null){ 
         stQuery.close(); 
         stQuery=null; 
       } 
     } //Fin finally 
   } //Fin metodo update_rf_tr_modulo 
  
   /** 
   * Metodo que borra la informacion de rf_tr_modulo 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void delete_rf_tr_modulo(Connection con, String clave)throws SQLException, Exception{ 
      Statement stQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("DELETE FROM rf_tr_modulo a "); 
       SQL.append("WHERE a.idmodulo=").append(clave).append(" "); 
       System.out.println(SQL.toString()); 
       int rs=-1; 
       rs=stQuery.executeUpdate(SQL.toString()); 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo delete_rf_tr_modulo "+e.getMessage()); 
       throw e; 
     } //Fin catch 
     finally{ 
       if (stQuery!=null){ 
         stQuery.close(); 
         stQuery=null; 
       } 
     } //Fin finally 
   } //Fin metodo delete_rf_tr_modulo 
} //Fin clase bcRf_tr_modulo 
    