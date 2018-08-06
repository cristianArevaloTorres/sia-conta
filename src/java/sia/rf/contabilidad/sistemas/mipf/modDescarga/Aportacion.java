package sia.rf.contabilidad.sistemas.mipf.modDescarga;

import java.sql.*;

import sia.db.dao.DaoFactory;

import sun.jdbc.rowset.CachedRowSet;

public class Aportacion{ 
   public Aportacion(){ 
   } 
   private String aportacion_id; 
   private String descripcion; 
   private String cuenta; 
   private String nivel5; 
   private String nivel6; 
   private String regla_contable; 
   private String estatus; 
   private String fecha_inicio; 
   private String fecha_fin; 
  
   /** 
   * aportacion_id 
   * @return aportacion_id 
   */ 
   public String getAportacion_id() { 
      return aportacion_id; 
   } 
  
   /** 
   * aportacion_id 
   * @param aportacion_id 
   */ 
   public void setAportacion_id( String aportacion_id ) { 
      this.aportacion_id=aportacion_id; 
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
   * regla_contable 
   * @return regla_contable 
   */ 
   public String getRegla_contable() { 
      return regla_contable; 
   } 
  
   /** 
   * regla_contable 
   * @param regla_contable 
   */ 
   public void setRegla_contable( String regla_contable ) { 
      this.regla_contable=regla_contable; 
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
    * Metodo que lee la informacion de rf_tc_aportaciones 
    * Fecha de creacion: 
    * Autor: 
    * Fecha de modificacion: 
    * Modificado por: 
    */ 
    public CachedRowSet select_rf_tc_aportaciones_todas()throws SQLException, Exception{ 
       Connection con2=null;
       CachedRowSet rsQuery=null;
        StringBuffer SQL=new StringBuffer("");
       try{ 
        con2=DaoFactory.getContabilidad();
        rsQuery = new CachedRowSet();
        SQL.append("SELECT a.aportacion_id,a.descripcion,a.cuenta,a.nivel5,a.nivel6,a.regla_contable,a.estatus,a.fecha_inicio,a.fecha_fin");  
        SQL.append(" FROM rf_tc_aportaciones a where a.estatus=1"); 

        rsQuery.setCommand(SQL.toString());
        rsQuery.execute(con2);

      } //Fin try 
      catch(Exception e){ 
        System.out.println("Ocurrio un error al accesar al metodo select_rf_tc_aportaciones_todas"+e.getMessage()); 
        System.out.println("Query : "+SQL.toString()); 
        throw e; 
      } //Fin catch 
      finally{ 
        if(con2!=null){
           con2.close();
           con2 = null;
        } 
      } //Fin finally 
      return rsQuery;
    } //Fin metodo select_rf_tc_aportaciones 

   /** 
   * Metodo que lee la informacion de rf_tc_aportaciones 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void select_rf_tc_aportaciones(Connection con, String clave)throws SQLException, Exception{ 
      Statement stQuery=null; 
      ResultSet rsQuery=null; 
      StringBuffer SQL=new StringBuffer("");
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       SQL.append("SELECT a.aportacion_id,a.descripcion,a.cuenta,a.nivel5,a.nivel6,a.regla_contable,a.estatus,a.fecha_inicio,a.fecha_fin");  
       SQL.append(" FROM rf_tc_aportaciones a "); 
       SQL.append(" WHERE a.LLAVE='").append(clave).append("'"); 
       
       rsQuery=stQuery.executeQuery(SQL.toString()); 
       while (rsQuery.next()){ 
          aportacion_id=(rsQuery.getString("aportacion_id")==null) ? "" : rsQuery.getString("aportacion_id"); 
          descripcion=(rsQuery.getString("descripcion")==null) ? "" : rsQuery.getString("descripcion"); 
          cuenta=(rsQuery.getString("cuenta")==null) ? "" : rsQuery.getString("cuenta"); 
          nivel5=(rsQuery.getString("nivel5")==null) ? "" : rsQuery.getString("nivel5"); 
          nivel6=(rsQuery.getString("nivel6")==null) ? "" : rsQuery.getString("nivel6"); 
          regla_contable=(rsQuery.getString("regla_contable")==null) ? "" : rsQuery.getString("regla_contable"); 
          estatus=(rsQuery.getString("estatus")==null) ? "" : rsQuery.getString("estatus"); 
          fecha_inicio=(rsQuery.getString("fecha_inicio")==null) ? "" : rsQuery.getString("fecha_inicio"); 
          fecha_fin=(rsQuery.getString("fecha_fin")==null) ? "" : rsQuery.getString("fecha_fin"); 
       } // Fin while 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo select_rf_tc_aportaciones "+e.getMessage()); 
       System.out.println("Query: "+SQL.toString()); 
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
   } //Fin metodo select_rf_tc_aportaciones 
  
   /** 
   * Metodo que inserta la informacion de rf_tc_aportaciones 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void insert_rf_tc_aportaciones(Connection con)throws SQLException, Exception{ 
      Statement stQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("INSERT INTO rf_tc_aportaciones( aportacion_id,descripcion,cuenta,nivel5,nivel6,regla_contable,estatus,fecha_inicio,fecha_fin) ");  
       SQL.append("VALUES("); 
       SQL.append(aportacion_id).append(","); 
       SQL.append("'").append(descripcion).append("',"); 
       SQL.append("'").append(cuenta).append("',"); 
       SQL.append("'").append(nivel5).append("',"); 
       SQL.append("'").append(nivel6).append("',"); 
       SQL.append("'").append(regla_contable).append("',"); 
       SQL.append(estatus).append(","); 
       SQL.append("'").append(fecha_inicio).append("',"); 
       SQL.append("'").append(fecha_fin).append("')"); 
       System.out.println(SQL.toString()); 
       int rs=-1; 
       rs=stQuery.executeUpdate(SQL.toString()); 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo insert_rf_tc_aportaciones "+e.getMessage()); 
       throw e; 
     } //Fin catch 
     finally{ 
       if (stQuery!=null){ 
         stQuery.close(); 
         stQuery=null; 
       } 
     } //Fin finally 
   } //Fin metodo insert_rf_tc_aportaciones 
  
   /** 
   * Metodo que modifica la informacion de rf_tc_aportaciones 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void update_rf_tc_aportaciones(Connection con, String clave)throws SQLException, Exception{ 
      Statement stQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("UPDATE rf_tc_aportaciones"); 
       SQL.append(" SET aportacion_id=").append(aportacion_id).append(","); 
       SQL.append("descripcion=").append("'").append(descripcion).append("',"); 
       SQL.append("cuenta=").append("'").append(cuenta).append("',"); 
       SQL.append("nivel5=").append("'").append(nivel5).append("',"); 
       SQL.append("nivel6=").append("'").append(nivel6).append("',"); 
       SQL.append("regla_contable=").append("'").append(regla_contable).append("',"); 
       SQL.append("estatus=").append(estatus).append(","); 
       SQL.append("fecha_inicio=").append("'").append(fecha_inicio).append("',"); 
       SQL.append("fecha_fin=").append("'").append(fecha_fin); 
       SQL.append(" WHERE LLAVE='").append(clave).append("'"); 
       System.out.println(SQL.toString()); 
       int rs=-1; 
       rs=stQuery.executeUpdate(SQL.toString()); 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo update_rf_tc_aportaciones "+e.getMessage()); 
       throw e; 
     } //Fin catch 
     finally{ 
       if (stQuery!=null){ 
         stQuery.close(); 
         stQuery=null; 
       } 
     } //Fin finally 
   } //Fin metodo update_rf_tc_aportaciones 
  
   /** 
   * Metodo que borra la informacion de rf_tc_aportaciones 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void delete_rf_tc_aportaciones(Connection con,  String clave)throws SQLException, Exception{ 
      Statement stQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("DELETE FROM rf_tc_aportaciones a "); 
       SQL.append("WHERE a.LLAVE='").append(clave).append("'"); 
       System.out.println(SQL.toString()); 
       int rs=-1; 
       rs=stQuery.executeUpdate(SQL.toString()); 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo delete_rf_tc_aportaciones "+e.getMessage()); 
       throw e; 
     } //Fin catch 
     finally{ 
       if (stQuery!=null){ 
         stQuery.close(); 
         stQuery=null; 
       } 
     } //Fin finally 
   } //Fin metodo delete_rf_tc_aportaciones 
} //Fin clase Aportacion 
    
