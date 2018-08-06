package sia.rf.contabilidad.sistemas.mipf.modDescarga;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import sia.db.dao.DaoFactory;
import sun.jdbc.rowset.CachedRowSet;

public class Impuesto{ 
   public Impuesto(){ 
   } 
   private String par_impuesto_id; 
   private String descripcion; 
   private String regla_contable; 
   private String estatus; 
   private String fecha_inicio; 
   private String fecha_fin; 
  
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
    * Metodo que lee la informacion de rf_tc_partidas_impuestos 
    * Fecha de creacion: 
    * Autor: 
    * Fecha de modificacion: 
    * Modificado por: 
    */ 
    public CachedRowSet select_rf_tc_partidas_impuestos_todas()throws SQLException, Exception{ 
       Connection con2=null;       
       CachedRowSet rsQuery=null;
       StringBuffer SQL=new StringBuffer("");
       try{ 
        con2=DaoFactory.getContabilidad();
        rsQuery = new CachedRowSet();
        SQL.append("SELECT a.par_impuesto_id,a.descripcion,a.regla_contable,a.estatus,a.fecha_inicio,a.fecha_fin, a.clave_ret");  
        SQL.append(" FROM rf_tc_partidas_impuestos a "); 

        rsQuery.setCommand(SQL.toString());
        rsQuery.execute(con2);     
 
      } //Fin try 
      catch(Exception e){ 
        System.out.println("Ocurrio un error al accesar al metodo select_rf_tc_partidas_impuestos_todas "+e.getMessage()); 
        System.out.println("Query: "+SQL.toString()); 
        throw e; 
      } //Fin catch 
      finally{ 
          if(con2!=null){
             con2.close();
          } 
      } //Fin finally 
      return rsQuery;
    } //Fin metodo select_rf_tc_partidas_impuestos 
  
   /** 
   * Metodo que lee la informacion de rf_tc_partidas_impuestos 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void select_rf_tc_partidas_impuestos(Connection con, String clave)throws SQLException, Exception{ 
      Statement stQuery=null; 
      ResultSet rsQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("SELECT a.par_impuesto_id,a.descripcion,a.regla_contable,a.estatus,a.fecha_inicio,a.fecha_fin");  
       SQL.append(" FROM rf_tc_partidas_impuestos a "); 
       SQL.append(" WHERE a.LLAVE='").append(clave).append("'"); 
       System.out.println(SQL.toString()); 
       rsQuery=stQuery.executeQuery(SQL.toString()); 
       while (rsQuery.next()){ 
          par_impuesto_id=(rsQuery.getString("par_impuesto_id")==null) ? "" : rsQuery.getString("par_impuesto_id"); 
          descripcion=(rsQuery.getString("descripcion")==null) ? "" : rsQuery.getString("descripcion"); 
          regla_contable=(rsQuery.getString("regla_contable")==null) ? "" : rsQuery.getString("regla_contable"); 
          estatus=(rsQuery.getString("estatus")==null) ? "" : rsQuery.getString("estatus"); 
          fecha_inicio=(rsQuery.getString("fecha_inicio")==null) ? "" : rsQuery.getString("fecha_inicio"); 
          fecha_fin=(rsQuery.getString("fecha_fin")==null) ? "" : rsQuery.getString("fecha_fin"); 
       } // Fin while 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo select_rf_tc_partidas_impuestos "+e.getMessage()); 
       throw e; 
     } //Fin catch 
     finally{ 
       if (rsQuery!=null){ 
         rsQuery.close();
       } 
       if (stQuery!=null){ 
         stQuery.close();
       } 
     } //Fin finally 
   } //Fin metodo select_rf_tc_partidas_impuestos 
  
   /** 
   * Metodo que inserta la informacion de rf_tc_partidas_impuestos 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void insert_rf_tc_partidas_impuestos(Connection con)throws SQLException, Exception{ 
      Statement stQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("INSERT INTO rf_tc_partidas_impuestos( par_impuesto_id,descripcion,regla_contable,estatus,fecha_inicio,fecha_fin) ");  
       SQL.append("VALUES("); 
       SQL.append(par_impuesto_id).append(","); 
       SQL.append("'").append(descripcion).append("',"); 
       SQL.append("'").append(regla_contable).append("',"); 
       SQL.append(estatus).append(","); 
       SQL.append("'").append(fecha_inicio).append("',"); 
       SQL.append("'").append(fecha_fin).append("')"); 
       System.out.println(SQL.toString()); 
       int rs=-1; 
       rs=stQuery.executeUpdate(SQL.toString()); 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo insert_rf_tc_partidas_impuestos "+e.getMessage()); 
       throw e; 
     } //Fin catch 
     finally{ 
       if (stQuery!=null){ 
         stQuery.close();
       } 
     } //Fin finally 
   } //Fin metodo insert_rf_tc_partidas_impuestos 
  
   /** 
   * Metodo que modifica la informacion de rf_tc_partidas_impuestos 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void update_rf_tc_partidas_impuestos(Connection con, String clave)throws SQLException, Exception{ 
      Statement stQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("UPDATE rf_tc_partidas_impuestos"); 
       SQL.append(" SET par_impuesto_id=").append(par_impuesto_id).append(","); 
       SQL.append("descripcion=").append("'").append(descripcion).append("',"); 
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
       System.out.println("Ocurrio un error al accesar al metodo update_rf_tc_partidas_impuestos "+e.getMessage()); 
       throw e; 
     } //Fin catch 
     finally{ 
       if (stQuery!=null){ 
         stQuery.close();
       } 
     } //Fin finally 
   } //Fin metodo update_rf_tc_partidas_impuestos 
  
   /** 
   * Metodo que borra la informacion de rf_tc_partidas_impuestos 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void delete_rf_tc_partidas_impuestos(Connection con, String clave)throws SQLException, Exception{ 
      Statement stQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("DELETE FROM rf_tc_partidas_impuestos a "); 
       SQL.append("WHERE a.LLAVE='").append(clave).append("'"); 
       System.out.println(SQL.toString()); 
       int rs=-1; 
       rs=stQuery.executeUpdate(SQL.toString()); 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo delete_rf_tc_partidas_impuestos "+e.getMessage()); 
       throw e; 
     } //Fin catch 
     finally{ 
       if (stQuery!=null){ 
         stQuery.close();
       } 
     } //Fin finally 
   } //Fin metodo delete_rf_tc_partidas_impuestos 
} //Fin clase Impuesto 
    
