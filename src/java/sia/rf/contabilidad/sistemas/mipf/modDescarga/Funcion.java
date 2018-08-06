package sia.rf.contabilidad.sistemas.mipf.modDescarga;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import sia.db.dao.DaoFactory;
import sun.jdbc.rowset.CachedRowSet;

public class Funcion{ 

   public Funcion(){ 
   } 

   private String idfuncion; 
   private String idvariable; 
   private String nombrevariable; 
   private String regla_contable; 
  
   /** 
   * idfuncion 
   * @return idfuncion 
   */ 
   public String getIdfuncion() { 
      return idfuncion; 
   } 
  
   /** 
   * idfuncion 
   * @param idfuncion 
   */ 
   public void setIdfuncion( String idfuncion ) { 
      this.idfuncion=idfuncion; 
   } 
  
   /** 
   * idvariable 
   * @return idvariable 
   */ 
   public String getIdvariable() { 
      return idvariable; 
   } 
  
   /** 
   * idvariable 
   * @param idvariable 
   */ 
   public void setIdvariable( String idvariable ) { 
      this.idvariable=idvariable; 
   } 
  
   /** 
   * nombrevariable 
   * @return nombrevariable 
   */ 
   public String getNombrevariable() { 
      return nombrevariable; 
   } 
  
   /** 
   * nombrevariable 
   * @param nombrevariable 
   */ 
   public void setNombrevariable( String nombrevariable ) { 
      this.nombrevariable=nombrevariable; 
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
   * Metodo que lee la informacion de rf_tc_funciones 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public CachedRowSet select_rf_tc_FuncionesPorForma( int numForma)throws SQLException, Exception{ 
      Connection con=null;
      CachedRowSet rsQuery=null;
      StringBuffer SQL=new StringBuffer("");
      try{ 
       con=DaoFactory.getContabilidad();
       rsQuery = new CachedRowSet();
       SQL.append("SELECT a.idfuncion,a.idvariable,a.nombrevariable,a.regla_contable");  
       SQL.append(" FROM rf_tc_funciones a, rf_tr_formas_funciones f "); 
       SQL.append(" WHERE a.idFuncion=f.idFuncion and f.forma_contable_id=").append(numForma).append(" and a.tipo is null "); 
       rsQuery.setCommand(SQL.toString());
       rsQuery.execute(con);               

     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo select_rf_tc_funcionesPorForma "+e.getMessage()); 
       System.out.println("Query: "+SQL.toString());        
       throw e; 
     } //Fin catch 
     finally{  
       if(con!=null){
         con.close();
       }
     } //Fin finally 
     return rsQuery;
   } //Fin metodo select_rf_tc_funciones 

    /** 
    * Metodo que lee la informacion de rf_tc_funciones 
    * Fecha de creacion: 
    * Autor: 
    * Fecha de modificacion: 
    * Modificado por: 
    */ 
    public CachedRowSet select_rf_tc_FuncionesPorFormaVariable(int numForma, String pTipo)throws SQLException, Exception{ 
       Connection con=null;
       CachedRowSet rsQuery=null;
       StringBuffer SQL=new StringBuffer("");
       try{ 
        con=DaoFactory.getContabilidad();
        rsQuery = new CachedRowSet();
        SQL.append("SELECT a.idfuncion,a.idvariable,a.nombrevariable,a.regla_contable, a.tipo");  
        SQL.append(" FROM rf_tc_funciones a, rf_tr_formas_funciones f "); 
        SQL.append(" WHERE a.idFuncion=f.idFuncion and f.forma_contable_id=").append(numForma).append(" and a.tipo='").append(pTipo).append("' ");
        //System.out.println("Funcion.select_rf_tc_FuncionesPorFormaVariable.SQL "+SQL);
        rsQuery.setCommand(SQL.toString());
        rsQuery.execute(con);               
      } //Fin try 
      catch(Exception e){ 
        System.out.println("Ocurrio un error al accesar al metodo select_rf_tc_funcionesPorForma "+e.getMessage()); 
        System.out.println("Query: "+SQL.toString()); 
        throw e; 
      } //Fin catch 
      finally{  
         if(con!=null){
           con.close();
         }     
      } //Fin finally 
      return rsQuery;
    } //Fin metodo select_rf_tc_funciones 

    /** 
    * Metodo que lee la informacion de rf_tc_funciones 
    * Fecha de creacion: 
    * Autor: 
    * Fecha de modificacion: 
    * Modificado por: 
    */ 
    public void select_rf_tc_funciones(Connection con, String clave)throws SQLException, Exception{ 
       Statement stQuery=null; 
       ResultSet rsQuery=null; 
       try{ 
        stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
        StringBuffer SQL=new StringBuffer("SELECT a.idfuncion,a.idvariable,a.nombrevariable,a.regla_contable");  
        SQL.append(" FROM rf_tc_funciones a "); 
        SQL.append(" WHERE a.LLAVE='").append(clave).append("'"); 
        System.out.println(SQL.toString()); 
        rsQuery=stQuery.executeQuery(SQL.toString()); 
        while (rsQuery.next()){ 
           idfuncion=(rsQuery.getString("idfuncion")==null) ? "" : rsQuery.getString("idfuncion"); 
           idvariable=(rsQuery.getString("idvariable")==null) ? "" : rsQuery.getString("idvariable"); 
           nombrevariable=(rsQuery.getString("nombrevariable")==null) ? "" : rsQuery.getString("nombrevariable"); 
           regla_contable=(rsQuery.getString("regla_contable")==null) ? "" : rsQuery.getString("regla_contable"); 
        } // Fin while 
      } //Fin try 
      catch(Exception e){ 
        System.out.println("Ocurrio un error al accesar al metodo select_rf_tc_funciones "+e.getMessage()); 
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
    } //Fin metodo select_rf_tc_funciones 
  
   /** 
   * Metodo que inserta la informacion de rf_tc_funciones 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void insert_rf_tc_funciones(Connection con)throws SQLException, Exception{ 
      Statement stQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("INSERT INTO rf_tc_funciones( idfuncion,idvariable,nombrevariable,regla_contable) ");  
       SQL.append("VALUES("); 
       SQL.append(idfuncion).append(","); 
       SQL.append(idvariable).append(","); 
       SQL.append("'").append(nombrevariable).append("',"); 
       SQL.append("'").append(regla_contable).append("')"); 
       System.out.println(SQL.toString()); 
       int rs=-1; 
       rs=stQuery.executeUpdate(SQL.toString()); 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo insert_rf_tc_funciones "+e.getMessage()); 
       throw e; 
     } //Fin catch 
     finally{ 
       if (stQuery!=null){ 
         stQuery.close();
       } 
     } //Fin finally 
   } //Fin metodo insert_rf_tc_funciones 
  
   /** 
   * Metodo que modifica la informacion de rf_tc_funciones 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void update_rf_tc_funciones(Connection con, String clave)throws SQLException, Exception{ 
      Statement stQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("UPDATE rf_tc_funciones"); 
       SQL.append(" SET idfuncion=").append(idfuncion).append(","); 
       SQL.append("idvariable=").append(idvariable).append(","); 
       SQL.append("nombrevariable=").append("'").append(nombrevariable).append("',"); 
       SQL.append("regla_contable=").append("'").append(regla_contable); 
       SQL.append(" WHERE LLAVE='").append(clave).append("'"); 
       System.out.println(SQL.toString()); 
       int rs=-1; 
       rs=stQuery.executeUpdate(SQL.toString()); 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo update_rf_tc_funciones "+e.getMessage()); 
       throw e; 
     } //Fin catch 
     finally{ 
       if (stQuery!=null){ 
         stQuery.close();
       } 
     } //Fin finally 
   } //Fin metodo update_rf_tc_funciones 
  
   /** 
   * Metodo que borra la informacion de rf_tc_funciones 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void delete_rf_tc_funciones(Connection con, String clave)throws SQLException, Exception{ 
      Statement stQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("DELETE FROM rf_tc_funciones a "); 
       SQL.append("WHERE a.LLAVE='").append(clave).append("'"); 
       System.out.println(SQL.toString()); 
       int rs=-1; 
       rs=stQuery.executeUpdate(SQL.toString()); 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo delete_rf_tc_funciones "+e.getMessage()); 
       throw e; 
     } //Fin catch 
     finally{ 
       if (stQuery!=null){ 
         stQuery.close(); 
       } 
     } //Fin finally 
   } //Fin metodo delete_rf_tc_funciones 
} //Fin clase Funcion 
    
