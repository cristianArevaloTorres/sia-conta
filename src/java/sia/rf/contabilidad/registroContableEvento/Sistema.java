package sia.rf.contabilidad.registroContableEvento;

import java.sql.*;
import sia.db.dao.DaoFactory;
import sun.jdbc.rowset.CachedRowSet;

public class Sistema{ 
   public Sistema(){ 
   } 
   private String idsistema; 
   private String descripcion; 
   private String fechaalta; 
   private String cadconexion; 
   private String usuario; 
   private String contrasena; 
  
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
   * cadconexion 
   * @return cadconexion 
   */ 
   public String getCadconexion() { 
      return cadconexion; 
   } 
  
   /** 
   * cadconexion 
   * @param cadconexion 
   */ 
   public void setCadconexion( String cadconexion ) { 
      this.cadconexion=cadconexion; 
   } 
  
   /** 
   * usuario 
   * @return usuario 
   */ 
   public String getUsuario() { 
      return usuario; 
   } 
  
   /** 
   * usuario 
   * @param usuario 
   */ 
   public void setUsuario( String usuario ) { 
      this.usuario=usuario; 
   } 
  
   /** 
   * contrasena 
   * @return contrasena 
   */ 
   public String getContrasena() { 
      return contrasena; 
   } 
  
   /** 
   * contrasena 
   * @param contrasena 
   */ 
   public void setContrasena( String contrasena ) { 
      this.contrasena=contrasena; 
   } 

    /** 
    * Metodo que lee la informacion de select_rf_tr_sistema_enCache 
    * Fecha de creacion: 
    * Autor: 
    * Fecha de modificacion: 
    * Modificado por: 
    */    
    public CachedRowSet select_rf_tr_sistema_enCache()throws SQLException, Exception{ 
       Connection con2=null;
       CachedRowSet rsQuery=null;
       StringBuffer SQL=new StringBuffer("");
       try{ 
        con2=DaoFactory.getContabilidad();
        rsQuery = new CachedRowSet();
        SQL.append("SELECT a.idsistema,a.descripcion,a.fechaalta,a.cadconexion,a.usuario,a.contrasena, a.estatus");  
        SQL.append(" FROM rf_tr_sistema a WHERE a.estatus=1 "); 
     
        rsQuery.setCommand(SQL.toString());
        rsQuery.execute(con2);
      } //Fin try 
      catch(Exception e){ 
        System.out.println("Ocurrio un error al accesar al metodo select_rf_tr_sistema_enCache "+e.getMessage()); 
        System.out.println(SQL.toString()); 
        throw e; 
      } //Fin catch 
      finally{ 
         if(con2!=null){
            con2.close();
            con2 = null;
         }
      } //Fin finally 
      return rsQuery;
    } //Fin metodo select_rf_tr_sistema_enCache 
     /** 
     * Metodo que lee la informacion de select_rf_tr_sistema_enCache sin WHERE
     * Fecha de creacion: 
     * Autor: 
     * Fecha de modificacion: 
     * Modificado por: 
     */    
     public CachedRowSet select_rf_tr_sistema_enCache2(Connection con)throws SQLException, Exception{ 
        CachedRowSet rsQuery=null;
        try{ 
         rsQuery = new CachedRowSet();
         StringBuffer SQL=new StringBuffer("SELECT a.idsistema,a.descripcion,a.fechaalta,a.cadconexion,a.usuario,a.contrasena, a.estatus");  
         SQL.append(" FROM rf_tr_sistema a  order by a.idsistema "); 
         System.out.println(SQL.toString()); 
         rsQuery.setCommand(SQL.toString());
         rsQuery.execute(con);
       } //Fin try 
       catch(Exception e){ 
         System.out.println("Ocurrio un error al accesar al metodo select_rf_tr_sistema_enCache "+e.getMessage()); 
         throw e; 
       } //Fin catch 
       finally{ 
       } //Fin finally 
       return rsQuery;
     } //Fin metodo select_rf_tr_sistema_enCache 
   
  
   /** 
   * Metodo que lee la informacion de rf_tr_sistema 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void select_rf_tr_sistema(Connection con, String clave)throws SQLException, Exception{ 
      Statement stQuery=null; 
      ResultSet rsQuery=null; 
      StringBuffer SQL=new StringBuffer("");  
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       SQL.append("SELECT a.idsistema,a.descripcion,a.fechaalta,a.cadconexion,a.usuario,a.contrasena");  
       SQL.append(" FROM rf_tr_sistema a "); 
       SQL.append(" WHERE a.idSistema=").append(clave).append("  ");        

       rsQuery=stQuery.executeQuery(SQL.toString()); 
       while (rsQuery.next()){ 
          idsistema=(rsQuery.getString("idsistema")==null) ? "" : rsQuery.getString("idsistema"); 
          descripcion=(rsQuery.getString("descripcion")==null) ? "" : rsQuery.getString("descripcion"); 
          fechaalta=(rsQuery.getString("fechaalta")==null) ? "" : rsQuery.getString("fechaalta"); 
          cadconexion=(rsQuery.getString("cadconexion")==null) ? "" : rsQuery.getString("cadconexion"); 
          usuario=(rsQuery.getString("usuario")==null) ? "" : rsQuery.getString("usuario"); 
          contrasena=(rsQuery.getString("contrasena")==null) ? "" : rsQuery.getString("contrasena"); 
       } // Fin while 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo select_rf_tr_sistema "+e.getMessage()); 
       System.out.println("Query del metodo select_rf_tr_sistema: "+SQL.toString()); 
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
   } //Fin metodo select_rf_tr_sistema 
   
    
   /** 
   * Metodo que inserta la informacion de rf_tr_sistema 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void insert_rf_tr_sistema(Connection con)throws SQLException, Exception{ 
      Statement stQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("INSERT INTO rf_tr_sistema( idsistema,descripcion,fechaalta,cadconexion,usuario,contrasena) ");  
       SQL.append("VALUES("); 
       SQL.append(idsistema).append(","); 
       SQL.append("'").append(descripcion).append("',"); 
       SQL.append("'").append(fechaalta).append("',"); 
       SQL.append("'").append(cadconexion).append("',"); 
       SQL.append("'").append(usuario).append("',"); 
       SQL.append("'").append(contrasena).append("')"); 
       System.out.println(SQL.toString()); 
       int rs=-1; 
       rs=stQuery.executeUpdate(SQL.toString()); 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo insert_rf_tr_sistema "+e.getMessage()); 
       throw e; 
     } //Fin catch 
     finally{ 
       if (stQuery!=null){ 
         stQuery.close(); 
         stQuery=null; 
       } 
     } //Fin finally 
   } //Fin metodo insert_rf_tr_sistema 
  
   /** 
   * Metodo que modifica la informacion de rf_tr_sistema 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void update_rf_tr_sistema(Connection con, String clave)throws SQLException, Exception{ 
      Statement stQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("UPDATE rf_tr_sistema"); 
       SQL.append(" SET idsistema=").append(idsistema).append(","); 
       SQL.append("descripcion=").append("'").append(descripcion).append("',"); 
       SQL.append("fechaalta=").append("'").append(fechaalta).append("',"); 
       SQL.append("cadconexion=").append("'").append(cadconexion).append("',"); 
       SQL.append("usuario=").append("'").append(usuario).append("',"); 
       SQL.append("contrasena=").append("'").append(contrasena); 
       SQL.append(" WHERE idsistema=").append(clave).append(" "); 
       System.out.println(SQL.toString()); 
       int rs=-1; 
       rs=stQuery.executeUpdate(SQL.toString()); 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo update_rf_tr_sistema "+e.getMessage()); 
       throw e; 
     } //Fin catch 
     finally{ 
       if (stQuery!=null){ 
         stQuery.close(); 
         stQuery=null; 
       } 
     } //Fin finally 
   } //Fin metodo update_rf_tr_sistema 
  
   /** 
   * Metodo que borra la informacion de rf_tr_sistema 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void delete_rf_tr_sistema(Connection con, String clave)throws SQLException, Exception{ 
      Statement stQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("DELETE FROM rf_tr_sistema a "); 
       SQL.append("WHERE a.idsistema=").append(clave).append(" "); 
       System.out.println(SQL.toString()); 
       int rs=-1; 
       rs=stQuery.executeUpdate(SQL.toString()); 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo delete_rf_tr_sistema "+e.getMessage()); 
       throw e; 
     } //Fin catch 
     finally{ 
       if (stQuery!=null){ 
         stQuery.close(); 
         stQuery=null; 
       } 
     } //Fin finally 
   } //Fin metodo delete_rf_tr_sistema 
} //Fin clase bcRf_tr_sistema 
    