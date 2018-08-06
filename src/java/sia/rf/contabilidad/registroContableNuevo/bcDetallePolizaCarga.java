package sia.rf.contabilidad.registroContableNuevo;

import java.sql.*;

public class bcDetallePolizaCarga{
   public bcDetallePolizaCarga(){
   }
   
   private String id_detalle;
   private String unidad;
   private String ambito;
   private String numPold;
   private String cuentPold;   
   private String debeHaber;
   private String impoPold;
   private String referencia;
   private String poliza_id;
 
    /**
    * id_detalle
    * @return id_detalle
    */
    public String getId_detalle() {
       return id_detalle;
    }
    
    /**
    * id_detalle
    * @param id_detalle
    */
    public void setId_detalle( String id_detalle ) {
       this.id_detalle=id_detalle;
    }

    /**
    * unidad
    * @return unidad
    */
    public String getUnidad() {
       return unidad;
    }
    
    /**
    * unidad
    * @param unidad
    */
    public void setUnidad( String unidad ) {
       this.unidad=unidad;
    }


    /**
    * ambito
    * @return ambito
    */
    public String getAmbito() {
       return ambito;
    }
    
    /**
    * ambito
    * @param ambito
    */
    public void setAmbito( String ambito ) {
       this.ambito=ambito;
    }   
       
    /**
     * numPold
     * @return numPold
     */
    public String getNumPold() {
      return numPold;
    }
    
    /**
     * numPold
     * @param numPold
     */
    public void setNumPold( String numPold ) {
      this.numPold=numPold;
    }  

    /**
    * cuentPold
    * @return cuentPold
    */
    public String getCuentPold() {
       return cuentPold;
    }
    
    /**
    * cuentPold
    * @param cuentPold
    */
    public void setCuentPold( String cuentPold ) {
       this.cuentPold=cuentPold;
    }
         
   /**
   * debeHaber
   * @return debeHaber
   */
   public String getDebeHaber() {
      return debeHaber;
   }
 
   /**
   * debeHaber
   * @param debeHaber
   */
   public void setDebeHaber( String debeHaber ) {
      this.debeHaber=debeHaber;
   }

    /**
    * impoPold
    * @return impoPold
    */
    public String getImpoPold() {
       return impoPold;
    }
    
    /**
    * impoPold
    * @param impoPold
    */
    public void setImpoPold( String impoPold ) {
       this.impoPold=impoPold;
    }
     
   /**
   * referencia
   * @return referencia
   */
   public String getReferencia() {
      return referencia;
   }
 
   /**
   * referencia
   * @param referencia
   */
   public void setReferencia( String referencia ) {
      this.referencia=referencia;
   }
   
   /**
   * poliza_id
   * @return poliza_id
   */
   public String getPoliza_id() {
      return poliza_id;
   }
 
   /**
   * poliza_id
   * @param poliza_id
   */
   public void setPoliza_id( String poliza_id ) {
      this.poliza_id=poliza_id;
   }
 
   
    public String select_SEQ_rf_tr_detalle_polizas_carga(Connection con)throws SQLException, Exception{
       Statement stQuery=null;
       ResultSet rsQuery= null;
       String SidPoliza = "";
       try{
         String SQL2 = "select seq_rf_tr_detalle_poliza_carga.nextval valoractual from dual";     
         stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
         rsQuery = stQuery.executeQuery(SQL2);
         //  secuencia para poliza_id
         while (rsQuery.next()){
            SidPoliza = rsQuery.getString("valoractual");
         } //del while
       } //Fin try
       catch(Exception e){
         System.out.println("Ocurrio un error al accesar al metodo select_SEQ_rf_tr_detalle_polizas_carga "+e.getMessage());
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
       return SidPoliza;
    } //fin Select 
   

   /**
   * Metodo que lee la informacion de rf_tr_detalle_poliza_carga
   * Fecha de creacion:
   * Autor:
   * Fecha de modificacion:
   * Modificado por:
   */
   public void select_rf_tr_detalle_poliza_carga(Connection con, String pPolizaId)throws SQLException, Exception{
      Statement stQuery=null;
      ResultSet rsQuery=null;
      try{
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
       StringBuffer SQL=new StringBuffer("SELECT a.id_detalle, a.unidad, a.ambito, a.numPold, a.cuentPold, a.debeHaber, a.impoPold, a.referencia,a.poliza_id"); 
       SQL.append(" FROM rf_tr_detalle_poliza_carga a ");
       SQL.append(" WHERE a.poliza_id=").append(pPolizaId).append(" ");
       System.out.println(SQL.toString());
       rsQuery=stQuery.executeQuery(SQL.toString());
       while (rsQuery.next()){
          id_detalle=(rsQuery.getString("id_detalle")==null) ? "" : rsQuery.getString("id_detalle");
          unidad=(rsQuery.getString("unidad")==null) ? "" : rsQuery.getString("unidad");
          ambito=(rsQuery.getString("ambito")==null) ? "" : rsQuery.getString("ambito");          
          numPold=(rsQuery.getString("numPold")==null) ? "" : rsQuery.getString("numPold");          
          cuentPold=(rsQuery.getString("cuentPold")==null) ? "" : rsQuery.getString("cuentPold");
          debeHaber=(rsQuery.getString("debeHaber")==null) ? "" : rsQuery.getString("debeHaber");
          impoPold=(rsQuery.getString("impoPold")==null) ? "" : rsQuery.getString("impoPold");
          referencia=(rsQuery.getString("referencia")==null) ? "" : rsQuery.getString("referencia");
          poliza_id=(rsQuery.getString("poliza_id")==null) ? "" : rsQuery.getString("poliza_id");
       } // Fin while
     } //Fin try
     catch(Exception e){
       System.out.println("Ocurrio un error al accesar al metodo select_rf_tr_detalle_poliza_carga "+e.getMessage());
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
   } //Fin metodo select_rf_tr_detalle_poliza_carga

   /**
   * Metodo que inserta la informacion de rf_tr_detalle_poliza_carga
   * Fecha de creacion:
   * Autor:
   * Fecha de modificacion:
   * Modificado por:
   */
   public void insert_rf_tr_detalle_poliza_carga(Connection con)throws SQLException, Exception{
      Statement stQuery=null;
      try{
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
       StringBuffer SQL=new StringBuffer("INSERT INTO rf_tr_detalle_poliza_carga( id_detalle, unidad, ambito, numPold, cuentPold, debeHaber, impoPold, referencia,poliza_id) "); 
       SQL.append("VALUES(");
       SQL.append(id_detalle).append(",");
       SQL.append("'").append(unidad).append("',");
       SQL.append("'").append(ambito).append("',");
       SQL.append("'").append(numPold).append("',");
       SQL.append("'").append(cuentPold).append("',");       
       SQL.append("'").append(debeHaber).append("',");       
       SQL.append(impoPold).append(",");       
       SQL.append("'").append(referencia).append("',");
       SQL.append(poliza_id).append(") ");
       System.out.println(SQL.toString());
       int rs=-1;
       rs=stQuery.executeUpdate(SQL.toString());
     } //Fin try
     catch(Exception e){
       System.out.println("Ocurrio un error al accesar al metodo insert_rf_tr_detalle_poliza_carga "+e.getMessage());
       throw e;
     } //Fin catch
     finally{
       if (stQuery!=null){
         stQuery.close();
         stQuery=null;
       }
     } //Fin finally
   } //Fin metodo insert_rf_tr_detalle_poliza_carga
 
   /**
   * Metodo que modifica la informacion de rf_tr_detalle_poliza
   * Fecha de creacion:
   * Autor:
   * Fecha de modificacion:
   * Modificado por:
   */
   public void update_rf_tr_detalle_poliza_carga(Connection con, String clave)throws SQLException, Exception{
      Statement stQuery=null;
      try{
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
       StringBuffer SQL=new StringBuffer("UPDATE rf_tr_detalle_poliza_carga");
       SQL.append("SET id_detalle=").append(id_detalle).append(",");
       SQL.append("unidad=").append("'").append(unidad).append("',");
       SQL.append("ambito=").append("'").append(ambito).append("',");
       SQL.append("numPold=").append("'").append(numPold).append("',");       
       SQL.append("cuentPold=").append("'").append(cuentPold).append("',");
       SQL.append("debeHaber=").append("'").append(debeHaber).append("',");
       SQL.append("impoPold=").append(impoPold).append(",");
       SQL.append("referencia=").append("'").append(referencia).append("',");
       SQL.append("poliza_id=").append(poliza_id).append(" ");
       SQL.append(" WHERE LLAVE='").append(clave).append("'");
       System.out.println(SQL.toString());
       int rs=-1;
       rs=stQuery.executeUpdate(SQL.toString());
     } //Fin try
     catch(Exception e){
       System.out.println("Ocurrio un error al accesar al metodo update_rf_tr_detalle_poliza_carga "+e.getMessage());
       throw e;
     } //Fin catch
     finally{
       if (stQuery!=null){
         stQuery.close();
         stQuery=null;
       }
     } //Fin finally
   } //Fin metodo update_rf_tr_detalle_poliza_carga
 
   /**
   * Metodo que borra la informacion de rf_tr_detalle_poliza_carga
   * Fecha de creacion:
   * Autor:
   * Fecha de modificacion:
   * Modificado por:
   */
   public void delete_rf_tr_detalle_poliza_carga(Connection con, String pPolizaId)throws SQLException, Exception{
      Statement stQuery=null;
      try{
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
       StringBuffer SQL=new StringBuffer("DELETE FROM rf_tr_detalle_poliza_carga a ");
       SQL.append("WHERE a.poliza_id=").append(pPolizaId).append(" ");
       System.out.println(SQL.toString());
       int rs=-1;
       rs=stQuery.executeUpdate(SQL.toString());
     } //Fin try
     catch(Exception e){
       System.out.println("Ocurrio un error al accesar al metodo delete_rf_tr_detalle_poliza_carga "+e.getMessage());
       throw e;
     } //Fin catch
     finally{
       if (stQuery!=null){
         stQuery.close();
         stQuery=null;
       }
     } //Fin finally
   } //Fin metodo delete_rf_tr_detalle_poliza_carga
} //Fin clase bcDetallePolizaCarga
 