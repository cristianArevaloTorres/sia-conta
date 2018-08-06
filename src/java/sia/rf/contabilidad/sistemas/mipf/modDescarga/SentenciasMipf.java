package sia.rf.contabilidad.sistemas.mipf.modDescarga;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import sun.jdbc.rowset.CachedRowSet;

public class SentenciasMipf{ 
   public SentenciasMipf(){ 
   } 
   
   private String tipo; 
   private String sentencia_id; 
   private String descripcion; 
   private String sentencia; 
   private String estatus; 
   private String fecha_inicio; 
   private String fecha_fin; 
  
   /** 
   * tipo 
   * @return tipo 
   */ 
   public String getTipo() { 
      return tipo; 
   } 
  
   /** 
   * tipo 
   * @param tipo 
   */ 
   public void setTipo( String tipo ) { 
      this.tipo=tipo; 
   } 
  
   /** 
   * sentencia_id 
   * @return sentencia_id 
   */ 
   public String getSentencia_id() { 
      return sentencia_id; 
   } 
  
   /** 
   * sentencia_id 
   * @param sentencia_id 
   */ 
   public void setSentencia_id( String sentencia_id ) { 
      this.sentencia_id=sentencia_id; 
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
   * sentencia 
   * @return sentencia 
   */ 
   public String getSentencia() { 
      return sentencia; 
   } 
  
   /** 
   * sentencia 
   * @param sentencia 
   */ 
   public void setSentencia( String sentencia ) { 
      this.sentencia=sentencia; 
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
    * Metodo que lee la informacion de rf_tc_sentencias 
    * Fecha de creacion: 
    * Autor: 
    * Fecha de modificacion: 
    * Modificado por: 
    */ 
    public CachedRowSet select_rf_tc_sentencias_evento(Connection con, String clave)throws SQLException, Exception{ 
       CachedRowSet rsQuery=null;
        StringBuffer SQL=new StringBuffer("");
       try{ 
        rsQuery = new CachedRowSet();
        SQL.append("SELECT a.tipo,a.sentencia_id,a.descripcion,a.sentencia as sentencia,a.sentencia2 as sentencia2, a.estatus,a.fecha_inicio,a.fecha_fin,a.sentencia3 as sentencia3,a.sentencia4 as sentencia4,a.sentencia5 as sentencia5 ");  
        SQL.append(" FROM rf_tc_sentencias a, rf_tr_eventos_sentencias e "); 
        SQL.append(" WHERE a.sentencia_id = e.sentencia_id and e.idevento=").append(clave).append(" "); 
        rsQuery.setCommand(SQL.toString());
        rsQuery.execute(con);
        //System.out.println("SentenciasMipf.select_rf_tc_sentencias_evento("+clave+").sql "+SQL);
        //while (rsQuery.next()){ 
        //System.out.println("Query ejecutado MIPF: "+rsQuery.getString("descripcion")); 
        //}
      } //Fin try 
      catch(Exception e){ 
        System.out.println(SQL.toString()); 
        System.out.println("Ocurrio un error al accesar al metodo select_rf_tc_sentencias_evento "+e.getMessage()); 
        throw e; 
      } //Fin catch 
      finally{ 
      } //Fin finally 
      return rsQuery;
    } //Fin metodo select_rf_tc_sentencias_evento 

  
   /** 
   * Metodo que lee la informacion de rf_tc_sentencias 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void select_rf_tc_sentencias(Connection con, String clave)throws SQLException, Exception{ 
      Statement stQuery=null; 
      ResultSet rsQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("SELECT a.tipo,a.sentencia_id,a.descripcion,a.sentencia,a.estatus,a.fecha_inicio,a.fecha_fin");  
       SQL.append(" FROM rf_tc_sentencias a "); 
       SQL.append(" WHERE a.LLAVE='").append(clave).append("'"); 
       System.out.println(SQL.toString()); 
       rsQuery=stQuery.executeQuery(SQL.toString()); 
       while (rsQuery.next()){ 
          tipo=(rsQuery.getString("tipo")==null) ? "" : rsQuery.getString("tipo"); 
          sentencia_id=(rsQuery.getString("sentencia_id")==null) ? "" : rsQuery.getString("sentencia_id"); 
          descripcion=(rsQuery.getString("descripcion")==null) ? "" : rsQuery.getString("descripcion"); 
          sentencia=(rsQuery.getString("sentencia")==null) ? "" : rsQuery.getString("sentencia"); 
          estatus=(rsQuery.getString("estatus")==null) ? "" : rsQuery.getString("estatus"); 
          fecha_inicio=(rsQuery.getString("fecha_inicio")==null) ? "" : rsQuery.getString("fecha_inicio"); 
          fecha_fin=(rsQuery.getString("fecha_fin")==null) ? "" : rsQuery.getString("fecha_fin"); 
       } // Fin while 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo select_rf_tc_sentencias "+e.getMessage()); 
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
   } //Fin metodo select_rf_tc_sentencias 
  
   /** 
   * Metodo que inserta la informacion de rf_tc_sentencias 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void insert_rf_tc_sentencias(Connection con)throws SQLException, Exception{ 
      Statement stQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("INSERT INTO rf_tc_sentencias( tipo,sentencia_id,descripcion,sentencia,estatus,fecha_inicio,fecha_fin) ");  
       SQL.append("VALUES("); 
       SQL.append(tipo).append(","); 
       SQL.append(sentencia_id).append(","); 
       SQL.append("'").append(descripcion).append("',"); 
       SQL.append("'").append(sentencia).append("',"); 
       SQL.append(estatus).append(","); 
       SQL.append("'").append(fecha_inicio).append("',"); 
       SQL.append("'").append(fecha_fin).append("')"); 
       System.out.println(SQL.toString()); 
       int rs=-1; 
       rs=stQuery.executeUpdate(SQL.toString()); 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo insert_rf_tc_sentencias "+e.getMessage()); 
       throw e; 
     } //Fin catch 
     finally{ 
       if (stQuery!=null){ 
         stQuery.close(); 
       } 
     } //Fin finally 
   } //Fin metodo insert_rf_tc_sentencias 
  
   /** 
   * Metodo que modifica la informacion de rf_tc_sentencias 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void update_rf_tc_sentencias(Connection con, String clave)throws SQLException, Exception{ 
      Statement stQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("UPDATE rf_tc_sentencias"); 
       SQL.append(" SET tipo=").append(tipo).append(","); 
       SQL.append("sentencia_id=").append(sentencia_id).append(","); 
       SQL.append("descripcion=").append("'").append(descripcion).append("',"); 
       SQL.append("sentencia=").append("'").append(sentencia).append("',"); 
       SQL.append("estatus=").append(estatus).append(","); 
       SQL.append("fecha_inicio=").append("'").append(fecha_inicio).append("',"); 
       SQL.append("fecha_fin=").append("'").append(fecha_fin); 
       SQL.append(" WHERE LLAVE='").append(clave).append("'"); 
       System.out.println(SQL.toString()); 
       int rs=-1; 
       rs=rs=stQuery.executeUpdate(SQL.toString());
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo update_rf_tc_sentencias "+e.getMessage()); 
       throw e; 
     } //Fin catch 
     finally{ 
       if (stQuery!=null){ 
         stQuery.close(); 
       } 
     } //Fin finally 
   } //Fin metodo update_rf_tc_sentencias 
  
   /** 
   * Metodo que borra la informacion de rf_tc_sentencias 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void delete_rf_tc_sentencias(Connection con, String clave)throws SQLException, Exception{ 
      Statement stQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("DELETE FROM rf_tc_sentencias a "); 
       SQL.append("WHERE a.LLAVE='").append(clave).append("'"); 
       System.out.println(SQL.toString()); 
       int rs=-1; 
       rs=stQuery.executeUpdate(SQL.toString());
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo delete_rf_tc_sentencias "+e.getMessage()); 
       throw e; 
     } //Fin catch 
     finally{ 
       if (stQuery!=null){ 
         stQuery.close(); 
       } 
     } //Fin finally 
   } //Fin metodo delete_rf_tc_sentencias 
} //Fin clase SentenciasMipf 
    