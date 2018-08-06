package sia.rf.contabilidad.registroContableNuevo;

import java.sql.*;

import sia.db.dao.DaoFactory;

public class bcProveedores{ 
   public bcProveedores(){ 
   } 
   private String id_contable; 
   private String rfc; 
   private String nombre_razon_social; 
   private String fecha_alta; 
  
   /** 
   * id_contable 
   * @return id_contable 
   */ 
   public String getId_contable() { 
      return id_contable; 
   } 
  
   /** 
   * id_contable 
   * @param id_contable 
   */ 
   public void setId_contable( String id_contable ) { 
      this.id_contable=id_contable; 
   } 
  
   /** 
   * rfc 
   * @return rfc 
   */ 
   public String getRfc() { 
      return rfc; 
   } 
  
   /** 
   * rfc 
   * @param rfc 
   */ 
   public void setRfc( String rfc ) { 
      this.rfc=rfc; 
   } 
  
   /** 
   * nombre_razon_social 
   * @return nombre_razon_social 
   */ 
   public String getNombre_razon_social() { 
      return nombre_razon_social; 
   } 
  
   /** 
   * nombre_razon_social 
   * @param nombre_razon_social 
   */ 
   public void setNombre_razon_social( String nombre_razon_social ) { 
      this.nombre_razon_social=nombre_razon_social; 
   } 
  
   /** 
   * fecha_alta 
   * @return fecha_alta 
   */ 
   public String getFecha_alta() { 
      return fecha_alta; 
   } 
  
   /** 
   * fecha_alta 
   * @param fecha_alta 
   */ 
   public void setFecha_alta( String fecha_alta ) { 
      this.fecha_alta=fecha_alta; 
   } 
  
   /** 
   * Metodo que lee la informacion de rf_tc_proveedores 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void select_rf_tc_proveedores(Connection con, String clave)throws SQLException, Exception{ 
      Statement stQuery=null; 
      ResultSet rsQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("SELECT a.id_contable,a.rfc,a.nombre_razon_social,a.fecha_alta");  
       SQL.append(" FROM rf_tc_proveedores a "); 
       SQL.append(" WHERE a.id_contable=").append(clave).append(" "); 
       System.out.println(SQL.toString()); 
       rsQuery=stQuery.executeQuery(SQL.toString()); 
       while (rsQuery.next()){ 
          id_contable=(rsQuery.getString("id_contable")==null) ? "" : rsQuery.getString("id_contable"); 
          rfc=(rsQuery.getString("rfc")==null) ? "" : rsQuery.getString("rfc"); 
          nombre_razon_social=(rsQuery.getString("nombre_razon_social")==null) ? "" : rsQuery.getString("nombre_razon_social"); 
          fecha_alta=(rsQuery.getString("fecha_alta")==null) ? "" : rsQuery.getString("fecha_alta"); 
       } // Fin while 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo select_rf_tc_proveedores "+e.getMessage()); 
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
   } //Fin metodo select_rf_tc_proveedores 

    /** 
    * Metodo que lee y verifica si el proveedor ya exiate en el catalogo 
    * Fecha de creacion: 
    * Autor: 
    * Fecha de modificacion: 
    * Modificado por: 
    */ 
    public String select_rf_tc_proveedores_rfc(Connection con, String clave)throws SQLException, Exception{ 
       Statement stQuery=null; 
       ResultSet rsQuery=null; 
       String resultado="0";
       try{ 
        stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
        StringBuffer SQL=new StringBuffer("SELECT lpad(a.id_contable,4,'0') id_contable,a.rfc,a.nombre_razon_social,a.fecha_alta");  
        SQL.append(" FROM rf_tc_proveedores a "); 
        SQL.append(" WHERE a.rfc='").append(clave).append("'"); 
        System.out.println(SQL.toString()); 
        rsQuery=stQuery.executeQuery(SQL.toString()); 
        while (rsQuery.next()){ 
           resultado=(rsQuery.getString("id_contable")==null) ? "" : rsQuery.getString("id_contable"); 
        } // Fin while 
      } //Fin try 
      catch(Exception e){ 
        System.out.println("Ocurrio un error al accesar al metodo select_rf_tc_proveedores_rfc "+e.getMessage()); 
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
      return resultado;
    } //Fin metodo select_rf_tc_proveedores 

     /**
     * Metodo que lee la sequencia para el cheque que se va a generar
     * Fecha de creacion:08/06/2009
     * Autor:Jorge Luis Perez N.
     * Fecha de modificacion:
     * Modificado por:
     */
     
     public String select_SEQ_rf_tc_proveedores(Connection con)throws SQLException, Exception{
        Statement stQuery=null;
        ResultSet rsQuery= null;
        String SidProveedor = "";
        try{
          String SQL2 = "select lpad(seq_rf_tc_proveedores.nextval,4,0) valoractual from dual";     
          stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
          rsQuery = stQuery.executeQuery(SQL2);
          //  secuencia para poliza_id
          while (rsQuery.next()){
             SidProveedor = rsQuery.getString("valoractual");
          } //del while
        } //Fin try
        catch(Exception e){
          System.out.println("Ocurrio un error al accesar al metodo select_SEQ_rf_tc_proveedores "+e.getMessage());
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
        return SidProveedor;
     } //fin Select 

  
   /** 
   * Metodo que inserta la informacion de rf_tc_proveedores 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void insert_rf_tc_proveedores(Connection con)throws SQLException, Exception{ 
      Statement stQuery=null; 
      Connection conexion=null;
      try{ 
       conexion=DaoFactory.getContabilidad();
       conexion.setAutoCommit(false);
       stQuery=conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("INSERT INTO rf_tc_proveedores( id_contable,rfc,nombre_razon_social,fecha_alta) ");  
       SQL.append("VALUES("); 
       SQL.append(id_contable).append(","); 
       SQL.append("'").append(rfc).append("',"); 
       SQL.append("'").append(nombre_razon_social).append("',"); 
       SQL.append("sysdate)"); 
       System.out.println(SQL.toString()); 
       int rs=-1; 
       rs=stQuery.executeUpdate(SQL.toString()); 
       conexion.commit();
     } //Fin try 
     catch(Exception e){ 
       conexion.rollback();
       System.out.println("Ocurrio un error al accesar al metodo insert_rf_tc_proveedores "+e.getMessage()); 
       throw e; 
     } //Fin catch 
     finally{ 
       if (stQuery!=null){ 
         stQuery.close(); 
         stQuery=null; 
       } 
       if (conexion !=null){
          conexion.close();
          conexion=null;
       }       
     } //Fin finally 
   } //Fin metodo insert_rf_tc_proveedores 
  
   /** 
   * Metodo que modifica la informacion de rf_tc_proveedores 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void update_rf_tc_proveedores(Connection con, String clave)throws SQLException, Exception{ 
      Statement stQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("UPDATE rf_tc_proveedores"); 
       SQL.append(" SET id_contable=").append(id_contable).append(","); 
       SQL.append("rfc=").append("'").append(rfc).append("',"); 
       SQL.append("nombre_razon_social=").append("'").append(nombre_razon_social).append("',"); 
       SQL.append("fecha_alta=").append("'").append(fecha_alta); 
       SQL.append(" WHERE rf_tc_proveedores.id_contable=").append(clave).append(" "); 
       System.out.println(SQL.toString()); 
       int rs=-1; 
       rs=stQuery.executeUpdate(SQL.toString()); 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo update_rf_tc_proveedores "+e.getMessage()); 
       throw e; 
     } //Fin catch 
     finally{ 
       if (stQuery!=null){ 
         stQuery.close(); 
         stQuery=null; 
       } 
     } //Fin finally 
   } //Fin metodo update_rf_tc_proveedores 
  
   /** 
   * Metodo que borra la informacion de rf_tc_proveedores 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void delete_rf_tc_proveedores(Connection con, String clave)throws SQLException, Exception{ 
      Statement stQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("DELETE FROM rf_tc_proveedores a "); 
       SQL.append("WHERE a.id_contable=").append(clave).append(" "); 
       System.out.println(SQL.toString()); 
       int rs=-1; 
       rs=stQuery.executeUpdate(SQL.toString()); 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo delete_rf_tc_proveedores "+e.getMessage()); 
       throw e; 
     } //Fin catch 
     finally{ 
       if (stQuery!=null){ 
         stQuery.close(); 
         stQuery=null; 
       } 
     } //Fin finally 
   } //Fin metodo delete_rf_tc_proveedores 
} //Fin clase bcProveedores 
  
