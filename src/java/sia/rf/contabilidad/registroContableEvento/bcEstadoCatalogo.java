package sia.rf.contabilidad.registroContableEvento;

import java.sql.*; 
public class bcEstadoCatalogo{ 
   public bcEstadoCatalogo(){ 
   } 
   private String estatus; 
   private String descripcion; 

   
  
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
   * Metodo que lee la informacion de rf_tc_estatus_catalogo para verificar su disponibilidad del catalogo de cuentas 
   * Fecha de creacion: 27/07/2011
   * Fecha de modificacion: 27/07/2011
   * Modificado por: 
   */ 
   public void select_rf_tc_estado_catalogo(Connection con)throws SQLException, Exception{ 
      Statement stQuery=null; 
      ResultSet rsQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("select a.estatus, a.descripcion ");  
       SQL.append(" FROM rf_tc_estatus_catalogo a, rf_tc_estado_catalogo c ");
       SQL.append(" WHERE a.estatus=c.estatus "); 
       //System.out.println(SQL.toString()); 
       rsQuery=stQuery.executeQuery(SQL.toString()); 
       while (rsQuery.next()){ 
          estatus=(rsQuery.getString("estatus")==null) ? "" : rsQuery.getString("estatus"); 
          descripcion=(rsQuery.getString("descripcion")==null) ? "" : rsQuery.getString("descripcion"); 
                    
       } // Fin while 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo select_rf_tc_estado_catalogo "+e.getMessage()); 
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
   } //Fin metodo select_rf_tr_cheques 

} //Fin clase bcRf_tr_cheques 

