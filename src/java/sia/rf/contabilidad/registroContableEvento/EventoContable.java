package sia.rf.contabilidad.registroContableEvento;

import java.sql.*;

import sun.jdbc.rowset.CachedRowSet;

public class EventoContable{ 
   public EventoContable(){ 
   } 
   private String idevento; 
   private String idmodulo; 
   private String descripcion; 
   private String fechaalta; 
   private String fechabaja; 
   private String estatus; 
   private int banAre;
   private int banAre_ant;
   private String origen;

   /** 
   * idevento 
   * @return idevento 
   */ 
   public String getIdevento() { 
      return idevento; 
   } 
  
   /** 
   * idevento 
   * @param idevento 
   */ 
   public void setIdevento( String idevento ) { 
      this.idevento=idevento; 
   } 
  
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

    public int getBanAre() {
        return banAre;
    }

    public void setBanAre(int banAre) {
        this.banAre = banAre;
    }
    public void setBanAre_ant(int banAre_ant) {
        this.banAre_ant = banAre_ant;
    }

    public int getBanAre_ant() {
        return banAre_ant;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }
  
   /** 
   * Metodo que lee la informacion de rf_tr_eventoContable 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void select_rf_tr_eventoContable(Connection con, String clave)throws SQLException, Exception{ 
      Statement stQuery=null; 
      ResultSet rsQuery=null; 
      StringBuffer SQL=new StringBuffer("");  
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       SQL.append("SELECT a.idevento,a.idmodulo,a.descripcion,a.fechaalta,a.fechabaja,a.estatus, a.banAre, a.origen,a.banAre_ant ");  
       SQL.append(" FROM rf_tr_eventoContable a "); 
       SQL.append(" WHERE a.idevento=").append(clave).append(" ");        

       rsQuery=stQuery.executeQuery(SQL.toString()); 
       while (rsQuery.next()){ 
          idevento=(rsQuery.getString("idevento")==null) ? "" : rsQuery.getString("idevento"); 
          idmodulo=(rsQuery.getString("idmodulo")==null) ? "" : rsQuery.getString("idmodulo"); 
          descripcion=(rsQuery.getString("descripcion")==null) ? "" : rsQuery.getString("descripcion"); 
          fechaalta=(rsQuery.getString("fechaalta")==null) ? "" : rsQuery.getString("fechaalta"); 
          fechabaja=(rsQuery.getString("fechabaja")==null) ? "" : rsQuery.getString("fechabaja"); 
          estatus=(rsQuery.getString("estatus")==null) ? "" : rsQuery.getString("estatus"); 
          banAre=(rsQuery.getInt("banAre")==0) ? 0 : rsQuery.getInt("banAre"); 
          origen=(rsQuery.getString("origen")==null) ? "" : rsQuery.getString("origen"); 
          banAre_ant=(rsQuery.getInt("banAre_ant")==0) ? 0 : rsQuery.getInt("banAre_ant"); 
       } // Fin while 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo select_rf_tr_eventoContable "+e.getMessage()); 
       System.out.println("Query método select_rf_tr_eventoContable: "+SQL.toString()); 
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
   } //Fin metodo select_rf_tr_eventoContable 
   
    /** 
    * Descripcion: Lista los registros de rf_tr_eventoContable para mostrar en  los combos
    * Fecha de creacion: 20/01/2011
    * Autor: 
    * Fecha de modificacion: 
    * Modificado por: 
    */ 
    public CachedRowSet select_rf_tr_eventoContable_cacheRowSet(Connection con,String modulo)throws SQLException, Exception{ 
      CachedRowSet crs = null;     
      StringBuffer SQL=null;
       try{ 
        crs = new CachedRowSet();
        SQL=new StringBuffer("SELECT a.idevento,a.idmodulo,a.descripcion,a.fechaalta,a.fechabaja,a.estatus, a.banAre, a.origen,a.banAre_ant ");  
        SQL.append(" FROM rf_tr_eventoContable a "); 
        SQL.append(" WHERE idmodulo= ").append(modulo);         
        SQL.append(" ORDER BY a.idevento");
        System.out.println(SQL.toString()); 
        crs.setCommand(SQL.toString());
        crs.execute(con);                
      } //Fin try 
      catch(Exception e){ 
        System.out.println("Ocurrio un error al accesar al metodo select_rf_tr_eventoContable_cacheRowSet "+e.getMessage()); 
        crs.close();
        crs = null;
        throw e; 
      } //Fin catch 
      finally{ 
        SQL.setLength(0);
        SQL = null;
      } //Fin finally 
       return crs;
    } //Fin metodo select_rf_tr_eventoContable 
  
   /** 
   * Metodo que inserta la informacion de rf_tr_eventoContable 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void insert_rf_tr_eventoContable(Connection con)throws SQLException, Exception{ 
      Statement stQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("INSERT INTO rf_tr_eventoContable( idevento,idmodulo,descripcion,fechaalta,fechabaja,estatus) ");  
       SQL.append("VALUES("); 
       SQL.append(idevento).append(","); 
       SQL.append(idmodulo).append(","); 
       SQL.append("'").append(descripcion).append("',"); 
       SQL.append("'").append(fechaalta).append("',"); 
       SQL.append("'").append(fechabaja).append("',"); 
       SQL.append("'").append(estatus).append("')"); 
       System.out.println(SQL.toString()); 
       int rs=-1; 
       rs=stQuery.executeUpdate(SQL.toString()); 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo insert_rf_tr_eventoContable "+e.getMessage()); 
       throw e; 
     } //Fin catch 
     finally{ 
       if (stQuery!=null){ 
         stQuery.close(); 
         stQuery=null; 
       } 
     } //Fin finally 
   } //Fin metodo insert_rf_tr_eventoContable 
  
   /** 
   * Metodo que modifica la informacion de rf_tr_eventoContable 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void update_rf_tr_eventoContable(Connection con, String clave)throws SQLException, Exception{ 
      Statement stQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("UPDATE rf_tr_eventoContable"); 
       SQL.append(" SET idevento=").append(idevento).append(","); 
       SQL.append("idmodulo=").append(idmodulo).append(","); 
       SQL.append("descripcion=").append("'").append(descripcion).append("',"); 
       SQL.append("fechaalta=").append("'").append(fechaalta).append("',"); 
       SQL.append("fechabaja=").append("'").append(fechabaja).append("',"); 
       SQL.append("estatus=").append("'").append(estatus); 
       SQL.append(" WHERE idevento=").append(clave).append(" "); 
       System.out.println(SQL.toString()); 
       int rs=-1; 
       rs=stQuery.executeUpdate(SQL.toString()); 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo update_rf_tr_eventoContable "+e.getMessage()); 
       throw e; 
     } //Fin catch 
     finally{ 
       if (stQuery!=null){ 
         stQuery.close(); 
         stQuery=null; 
       } 
     } //Fin finally 
   } //Fin metodo update_rf_tr_eventoContable 
  
   /** 
   * Metodo que borra la informacion de rf_tr_eventoContable 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void delete_rf_tr_eventoContable(Connection con, String clave)throws SQLException, Exception{ 
      Statement stQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("DELETE FROM rf_tr_eventoContable a "); 
       SQL.append("WHERE a.idevento=").append(clave).append(""); 
       System.out.println(SQL.toString()); 
       int rs=-1; 
       rs=stQuery.executeUpdate(SQL.toString()); 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo delete_rf_tr_eventoContable "+e.getMessage()); 
       throw e; 
     } //Fin catch 
     finally{ 
       if (stQuery!=null){ 
         stQuery.close(); 
         stQuery=null; 
       } 
     } //Fin finally 
   } //Fin metodo delete_rf_tr_eventoContable 

 
} //Fin clase bcRf_tr_eventocontable 
