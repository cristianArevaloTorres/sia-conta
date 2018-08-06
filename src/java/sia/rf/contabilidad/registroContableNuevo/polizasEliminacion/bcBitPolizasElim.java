package sia.rf.contabilidad.registroContableNuevo.polizasEliminacion;

import java.sql.*; 
public class bcBitPolizasElim{ 
   public bcBitPolizasElim(){ 
   } 
   private String ejercicio; 
   private String mes; 
   private String version; 
   private String cuenta; 
   private String descr; 
   private String importe; 
   private String operacion_contable_id; 
   private String fecha_aplicacion; 
  private String tipo_forma; 
  
   /** 
   * ejercicio 
   * @return ejercicio 
   */ 
   public String getEjercicio() { 
      return ejercicio; 
   } 
  
   /** 
   * ejercicio 
   * @param ejercicio 
   */ 
   public void setEjercicio( String ejercicio ) { 
      this.ejercicio=ejercicio; 
   } 
  
   /** 
   * mes 
   * @return mes 
   */ 
   public String getMes() { 
      return mes; 
   } 
  
   /** 
   * mes 
   * @param mes 
   */ 
   public void setMes( String mes ) { 
      this.mes=mes; 
   } 
  
   /** 
   * version 
   * @return version 
   */ 
   public String getVersion() { 
      return version; 
   } 
  
   /** 
   * version 
   * @param version 
   */ 
   public void setVersion( String version ) { 
      this.version=version; 
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
   * descr 
   * @return descr 
   */ 
   public String getDescr() { 
      return descr; 
   } 
  
   /** 
   * descr 
   * @param descr 
   */ 
   public void setDescr( String descr ) { 
      this.descr=descr; 
   } 
  
   /** 
   * importe 
   * @return importe 
   */ 
   public String getImporte() { 
      return importe; 
   } 
  
   /** 
   * importe 
   * @param importe 
   */ 
   public void setImporte( String importe ) { 
      this.importe=importe; 
   } 
  
   /** 
   * operacion_contable_id 
   * @return operacion_contable_id 
   */ 
   public String getOperacion_contable_id() { 
      return operacion_contable_id; 
   } 
  
   /** 
   * operacion_contable_id 
   * @param operacion_contable_id 
   */ 
   public void setOperacion_contable_id( String operacion_contable_id ) { 
      this.operacion_contable_id=operacion_contable_id; 
   } 
  
   /** 
   * fecha_aplicacion 
   * @return fecha_aplicacion 
   */ 
   public String getfecha_aplicacion() { 
      return fecha_aplicacion; 
   } 
  
  public void setTipo_forma(String tipo_forma) {
    this.tipo_forma = tipo_forma;
  }

  public String getTipo_forma() {
    return tipo_forma;
  }  
  
  
    /**
    * Metodo que lee la sequencia para la poliza que se va a generar
    * Fecha de creacion:08/06/2009
    * Autor:Jorge Luis Perez N.
    * Fecha de modificacion:
    * Modificado por:
    */
    
    public String select_SEQ_rf_tr_bit_polizas_elim(Connection con)throws SQLException, Exception{
       Statement stQuery=null;
       ResultSet rsQuery= null;
       String SidPoliza = "";
       try{
         String SQL2 = "select seq_rf_tr_bit_polizas_elim.nextval valoractual from dual";     
         stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
         rsQuery = stQuery.executeQuery(SQL2);
         //  secuencia para poliza_id
         while (rsQuery.next()){
            SidPoliza = rsQuery.getString("valoractual");
         } //del while
       } //Fin try
       catch(Exception e){
         System.out.println("Ocurrio un error al accesar al metodo select_SEQ_rf_tr_bit_polizas_elim "+e.getMessage());
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
   * fecha_aplicacion 
   * @param fecha_aplicacion 
   */ 
   public void setfecha_aplicacion( String fecha_aplicacion ) { 
      this.fecha_aplicacion=fecha_aplicacion; 
   } 
  
   /** 
   * Metodo que lee la informacion de rf_tr_bit_polizas_elim 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void select_rf_tr_bit_polizas_elim(Connection con, String pEjercicio, String pMes, String pVersion)throws SQLException, Exception{ 
      Statement stQuery=null; 
      ResultSet rsQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("SELECT a.ejercicio,a.mes,a.version,a.cuenta,a.descr,a.importe,a.operacion_contable_id,a.fecha_aplicacion");  
       SQL.append(" FROM rf_tr_bit_polizas_elim a "); 
       SQL.append(" WHERE a.ejercicio=").append(pEjercicio).append(" and a.mes=").append(pMes).append(" and a.version=").append(pVersion); 
       System.out.println(SQL.toString()); 
       rsQuery=stQuery.executeQuery(SQL.toString()); 
       while (rsQuery.next()){ 
          ejercicio=(rsQuery.getString("ejercicio")==null) ? "" : rsQuery.getString("ejercicio"); 
          mes=(rsQuery.getString("mes")==null) ? "" : rsQuery.getString("mes"); 
          version=(rsQuery.getString("version")==null) ? "" : rsQuery.getString("version"); 
          cuenta=(rsQuery.getString("cuenta")==null) ? "" : rsQuery.getString("cuenta"); 
          descr=(rsQuery.getString("descr")==null) ? "" : rsQuery.getString("descr"); 
          importe=(rsQuery.getString("importe")==null) ? "" : rsQuery.getString("importe"); 
          operacion_contable_id=(rsQuery.getString("operacion_contable_id")==null) ? "" : rsQuery.getString("operacion_contable_id"); 
          fecha_aplicacion=(rsQuery.getString("fecha_aplicacion")==null) ? "" : rsQuery.getString("fecha_aplicacion"); 
       } // Fin while 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo select_rf_tr_bit_polizas_elim "+e.getMessage()); 
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
   } //Fin metodo select_rf_tr_bit_polizas_elim 
  
   /** 
   * Metodo que inserta la informacion de rf_tr_bit_polizas_elim 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void insert_rf_tr_bit_polizas_elim(Connection con)throws SQLException, Exception{ 
      Statement stQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("INSERT INTO rf_tr_bit_polizas_elim( ejercicio,mes,version,cuenta,descr,importe,operacion_contable_id,fecha_aplicacion,tipo_forma) ");  
       SQL.append("VALUES("); 
       SQL.append(ejercicio).append(","); 
       SQL.append(mes).append(","); 
       SQL.append(version).append(","); 
       SQL.append("'").append(cuenta).append("',"); 
       SQL.append("'").append(descr).append("',"); 
       SQL.append(importe).append(","); 
       SQL.append(operacion_contable_id).append(","); 
       SQL.append(" sysdate,'");
       SQL.append(tipo_forma).append("')"); 
       System.out.println(SQL.toString()); 
       int rs=-1; 
       rs=stQuery.executeUpdate(SQL.toString()); 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo insert_rf_tr_bit_polizas_elim "+e.getMessage()); 
       throw e; 
     } //Fin catch 
     finally{ 
       if (stQuery!=null){ 
         stQuery.close(); 
         stQuery=null; 
       } 
     } //Fin finally 
   } //Fin metodo insert_rf_tr_bit_polizas_elim 
  
   /** 
   * Metodo que modifica la informacion de rf_tr_bit_polizas_elim 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void update_rf_tr_bit_polizas_elim(Connection con, String clave)throws SQLException, Exception{ 
      Statement stQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("UPDATE rf_tr_bit_polizas_elim"); 
       SQL.append(" SET ejercicio=").append(ejercicio).append(","); 
       SQL.append("mes=").append(mes).append(","); 
       SQL.append("version=").append(version).append(","); 
       SQL.append("cuenta=").append("'").append(cuenta).append("',"); 
       SQL.append("descr=").append("'").append(descr).append("',"); 
       SQL.append("importe=").append(importe).append(","); 
       SQL.append("operacion_contable_id=").append(operacion_contable_id).append(","); 
       SQL.append("fecha_aplicacion=").append("'").append(fecha_aplicacion); 
       SQL.append(" WHERE LLAVE='").append(clave).append("'"); 
       System.out.println(SQL.toString()); 
       int rs=-1; 
       rs=stQuery.executeUpdate(SQL.toString()); 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo update_rf_tr_bit_polizas_elim "+e.getMessage()); 
       throw e; 
     } //Fin catch 
     finally{ 
       if (stQuery!=null){ 
         stQuery.close(); 
         stQuery=null; 
       } 
     } //Fin finally 
   } //Fin metodo update_rf_tr_bit_polizas_elim 
  
   /** 
   * Metodo que borra la informacion de rf_tr_bit_polizas_elim 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void delete_rf_tr_bit_polizas_elim(Connection con,  String clave)throws SQLException, Exception{ 
      Statement stQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("DELETE FROM rf_tr_bit_polizas_elim a "); 
       SQL.append("WHERE a.LLAVE='").append(clave).append("'"); 
       System.out.println(SQL.toString()); 
       int rs=-1; 
       rs=stQuery.executeUpdate(SQL.toString()); 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo delete_rf_tr_bit_polizas_elim "+e.getMessage()); 
       throw e; 
     } //Fin catch 
     finally{ 
       if (stQuery!=null){ 
         stQuery.close(); 
         stQuery=null; 
       } 
     } //Fin finally 
   } //Fin metodo delete_rf_tr_bit_polizas_elim 


} //Fin clase bcBitPolizasElim 