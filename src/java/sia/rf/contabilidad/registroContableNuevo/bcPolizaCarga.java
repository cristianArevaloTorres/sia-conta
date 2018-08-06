package sia.rf.contabilidad.registroContableNuevo;
 
import java.sql.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class bcPolizaCarga{

   public bcPolizaCarga(){
   }
   
   private String poliza_id;
   private String unidad;
   private String ambito;
   private String numPoli;
   private String numOper;
   private String fechPoli;
   private String concepPoli;
   private String fechAlta;
   private String refGral;
   private String ejercicio;
   private String mes;
   private String estatus;
   private String numEmpleado;
   private String fechaDia;
   private String fechaCarga;
   private String mensaje;
 
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
   * numpoli
   * @return numPoli
   */
   public String getNumPoli() {
      return numPoli;
   }
 
   /**
   * numPoli
   * @param numPoli
   */
   public void setNumPoli( String numPoli ) {
      this.numPoli=numPoli;
   }
 
   /**
   * numOper
   * @return numOper
   */
   public String getNumOper() {
      return numOper;
   }
 
   /**
   * numOper
   * @param numOper
   */
   public void setNumOper( String numOper ) {
      this.numOper=numOper;
   }
  
    /**
    * fechPoli
    * @return fechPoli
    */
    public String getFechPoli() {
       return fechPoli;
    }
    
    /**
    * fechPoli
    * @param fechPoli
    */
    public void setFechPoli( String fechPoli ) {
       this.fechPoli=fechPoli;
    }
     
   /**
   * concepPoli
   * @return concepPoli
   */
   public String getConcepPoli() {
      return concepPoli;
   }
 
   /**
   * concepPoli
   * @param concepPoli
   */
   public void setConcepPoli( String concepPoli ) {
      this.concepPoli=concepPoli;
   }
    /**
    * fechAlta
    * @return fechAlta
    */
    public String getFechAlta() {
       return fechAlta;
    }
    
    /**
    * fechAlta
    * @param fechAlta
    */
    public void setFechAlta( String fechAlta ) {
       this.fechAlta=fechAlta;
    }

   /**
   * refGral
   * @return refGral
   */
   public String getRefGral() {
      return refGral;
   }
 
   /**
   * refGral
   * @param refGral
   */
   public void setRefGral( String refGral ) {
      this.refGral=refGral;
   }
   
 
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


    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getNumEmpleado() {
        return numEmpleado;
    }

    public void setNumEmpleado(String numEmpleado) {
        this.numEmpleado = numEmpleado;
    }

    public String getFechaDia() {
        return fechaDia;
    }

    public void setFechaDia(String fechaDia) {
        this.fechaDia = fechaDia;
    }

    public String getFechaCarga() {
        return fechaCarga;
    }

    public void setFechaCarga(String fechaCarga) {
        this.fechaCarga = fechaCarga;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    
    /**
    * Metodo que lee la sequencia para la poliza que se va a generar
    * fechAlta de creacion:08/06/2009
    * Autor:Jorge Luis Perez N.
    * fechAlta de modificacion:
    * Modificado por:
    */
 
    public String select_SEQ_rf_tr_polizas_carga(Connection con)throws SQLException, Exception{
       Statement stQuery=null;
       ResultSet rsQuery= null;
       String SidPoliza = "";
       try{
         String SQL2 = "select seq_rf_tr_polizas_carga.nextval valoractual from dual";     
         stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
         rsQuery = stQuery.executeQuery(SQL2);
         //  secuencia para poliza_id
         while (rsQuery.next()){
            SidPoliza = rsQuery.getString("valoractual");
         } //del while
       } //Fin try
       catch(Exception e){
         System.out.println("Ocurrio un error al accesar al metodo select_SEQ_rf_tr_polizas_carga "+e.getMessage());
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
   * Metodo que lee la informacion de bcPolizaCarga
   * fechAlta de creacion:08/06/2009
   * Autor:Genera Bean
   * fechAlta de modificacion:
   * Modificado por:
   */
   public void select_rf_tr_polizas_carga(Connection con, String pPolizaId)throws SQLException, Exception{
      Statement stQuery=null;
      ResultSet rsQuery=null;
      try{
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
       StringBuffer SQL=new StringBuffer("SELECT a.poliza_id,a.unidad,a.ambito,a.numPoli,a.numOper,to_char(a.fechPoli,'dd/mm/yyyy') fechPoli,a.concepPoli,to_char(a.fechAlta,'dd/mm/yyyy') fechAlta,a.refGral,a.ejercicio,a.mes, a.estatus, a.numEmpleado, a.fechaDia, a.fechaCarga, a.mensaje "); 
       SQL.append(" FROM rf_tr_polizas_carga a ");
       SQL.append(" WHERE a.poliza_id = ").append(pPolizaId).append(" ");
       System.out.println(SQL.toString());
       rsQuery=stQuery.executeQuery(SQL.toString());
       while (rsQuery.next()){
          poliza_id=(rsQuery.getString("poliza_id")==null) ? "" : rsQuery.getString("poliza_id");
          unidad=(rsQuery.getString("unidad")==null) ? "" : rsQuery.getString("unidad");
          ambito=(rsQuery.getString("ambito")==null) ? "" : rsQuery.getString("ambito");
          numPoli=(rsQuery.getString("numPoli")==null) ? "" : rsQuery.getString("numPoli");
          numOper=(rsQuery.getString("numOper")==null) ? "" : rsQuery.getString("numOper");
          fechPoli=(rsQuery.getString("fechPoli")==null) ? "" : rsQuery.getString("fechPoli");
          concepPoli=(rsQuery.getString("concepPoli")==null) ? "" : rsQuery.getString("concepPoli");
          fechAlta=(rsQuery.getString("fechAlta")==null) ? "" : rsQuery.getString("fechAlta");          
          refGral=(rsQuery.getString("refGral")==null) ? "" : rsQuery.getString("refGral");
          ejercicio=(rsQuery.getString("ejercicio")==null) ? "" : rsQuery.getString("ejercicio");
          mes=(rsQuery.getString("mes")==null) ? "" : rsQuery.getString("mes");
          estatus=(rsQuery.getString("estatus")==null) ? "" : rsQuery.getString("estatus");
          numEmpleado=(rsQuery.getString("numEmpleado")==null) ? "" : rsQuery.getString("numEmpleado");
          fechaDia=(rsQuery.getString("fechaDia")==null) ? "" : rsQuery.getString("fechaDia");          
          fechaCarga=(rsQuery.getString("fechaCarga")==null) ? "" : rsQuery.getString("fechaCarga");          
          mensaje=(rsQuery.getString("mensaje")==null) ? "" : rsQuery.getString("mensaje");                    
       } // Fin while
     } //Fin try
     catch(Exception e){
       System.out.println("Ocurrio un error al accesar al metodo select_rf_tr_polizas_carga "+e.getMessage());
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
   } //Fin metodo select_rf_tr_polizas_carga

 
   /**
   * Metodo que inserta la informacion de rf_tr_polizas_carga
   * fechAlta de creacion:
   * Autor:
   * fechAlta de modificacion:
   * Modificado por:
   */
   public void insert_rf_tr_polizas_carga(Connection con)throws SQLException, Exception{
      Statement stQuery=null;
      ResultSet rsQuery= null;
      try{      
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
       StringBuffer SQL=new StringBuffer("INSERT INTO rf_tr_polizas_carga( poliza_id,unidad,ambito,numPoli,numOper,fechPoli,concepPoli,fechAlta,refGral,ejercicio,mes,estatus,numEmpleado,fechaDia,fechaCarga,mensaje) "); 
       SQL.append("VALUES(");
       SQL.append(poliza_id).append(",");
       SQL.append("'").append(unidad).append("',");
       SQL.append("'").append(ambito).append("',");
       SQL.append("'").append(numPoli).append("',");
       SQL.append("'").append(numOper).append("',");
       SQL.append("to_date('").append(fechPoli).append("','dd/mm/yyyy'),");       
       SQL.append("'").append(concepPoli).append("',");
       SQL.append("to_date('").append(fechAlta).append("','dd/mm/yyyy'),");              
       SQL.append("'").append(refGral).append("',");
       SQL.append(ejercicio).append(",");
       SQL.append(mes).append(",");
       SQL.append(estatus).append(",");
       SQL.append(numEmpleado).append(", ");
       SQL.append("sysdate,");                     
       SQL.append("null,null)");                     
       System.out.println(SQL.toString());
       int rs=-1;
       rs=stQuery.executeUpdate(SQL.toString());
     } //Fin try
     catch(Exception e){
       System.out.println("Ocurrio un error al accesar al metodo insert_rf_tr_polizas_carga "+e.getMessage());
       throw e;
     } //Fin catch
     finally{
       if (rsQuery != null){
         rsQuery.close();
         rsQuery=null;
       }     
       if (stQuery!=null){
         stQuery.close();
         stQuery=null;
       }
     } //Fin finally
   } //Fin metodo insert_rf_tr_polizas_carga
 
   /**
   * Metodo que modifica la informacion de rf_tr_polizas_carga
   * fechAlta de creacion:
   * Autor:
   * fechAlta de modificacion:
   * Modificado por:
   */
   public void update_rf_tr_polizas_carga(Connection con, String clave)throws SQLException, Exception{
      Statement stQuery=null;
      try{
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
       StringBuffer SQL=new StringBuffer("UPDATE rf_tr_polizas_carga");
       SQL.append(" SET poliza_id=").append(poliza_id).append(",");
       SQL.append("unidad=").append("'").append(unidad).append("',");
       SQL.append("ambito=").append("'").append(ambito).append("',");
       SQL.append("numPoli=").append("'").append(numPoli).append("',");
       SQL.append("numOper=").append("'").append(numOper).append(",");
       SQL.append("fechPoli=").append("to_date('").append(fechPoli).append("','dd/mm/yyyy'),");    
       SQL.append("concepPoli=").append("'").append(concepPoli).append("',");
       SQL.append("fechAlta=").append("to_date('").append(fechAlta).append("','dd/mm/yyyy'),");       
       SQL.append("refGral=").append("'").append(refGral).append("',");
       SQL.append("ejercicio=").append(ejercicio).append(",");
       SQL.append("mes=").append(mes).append(",");
       SQL.append("estatus=").append(estatus).append(",");
       SQL.append("numEmpleado=").append(numEmpleado).append(", ");       
       SQL.append("fechaDia=").append("'").append(fechaDia).append("', ");
       SQL.append("fechaCarga=").append("'").append(fechaCarga).append("', ");
       SQL.append("mensaje=").append("'").append(mensaje).append("' ");       
       SQL.append(" WHERE LLAVE='").append(clave).append("'");
       System.out.println(SQL.toString());
       int rs=-1;
       rs=stQuery.executeUpdate(SQL.toString());
     } //Fin try
     catch(Exception e){
       System.out.println("Ocurrio un error al accesar al metodo update_rf_tr_polizas_carga "+e.getMessage());
       throw e;
     } //Fin catch
     finally{
       if (stQuery!=null){
         stQuery.close();
         stQuery=null;
       }
     } //Fin finally
   } //Fin metodo update_rf_tr_polizas_carga

    /**
    * Metodo que modifica la informacion de rf_tr_polizas_carga
    * fechAlta de creacion:
    * Autor:
    * fechAlta de modificacion:
    * Modificado por:
    */
    public void update_rf_tr_polizas_carga_estatus(Connection con, String pPolizaId, String valorStatus, String mensaje)throws SQLException, Exception{
       Statement stQuery=null;
       try{
        stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        StringBuffer SQL=new StringBuffer("UPDATE rf_tr_polizas_carga");
        SQL.append(" SET estatus=").append(valorStatus).append(", ");
        SQL.append(" mensaje='").append(mensaje).append("', ");
        SQL.append(" fechaCarga=sysdate").append(" ");
        SQL.append(" WHERE poliza_id=").append(pPolizaId).append("");
        System.out.println(SQL.toString());
        int rs=-1;
        rs=stQuery.executeUpdate(SQL.toString());
      } //Fin try
      catch(Exception e){
        System.out.println("Ocurrio un error al accesar al metodo update_rf_tr_polizas_carga_estatus "+e.getMessage());
        throw e;
      } //Fin catch
      finally{
        if (stQuery!=null){
          stQuery.close();
          stQuery=null;
        }
      } //Fin finally
    } //Fin metodo update_rf_tr_polizas_carga_estatus


     /**
     * Metodo que modifica la informacion de rf_tr_polizas_carga
     * fechAlta de creacion:
     * Autor:
     * fechAlta de modificacion:
     * Modificado por:
     */
     public void update_rf_tr_polizas_carga_estatus_can(Connection con, String pPolizaId, String valorStatus, String mensaje)throws SQLException, Exception{
        Statement stQuery=null;
        try{
         stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
         StringBuffer SQL=new StringBuffer("UPDATE rf_tr_polizas_carga");
         SQL.append(" SET estatus=").append(valorStatus).append(", ");
         SQL.append(" mensaje='").append(mensaje).append("' || to_char(sysdate,'dd/mm/yyyy hh24:mi:ss') ");
         SQL.append(" WHERE poliza_id=").append(pPolizaId).append("");
         System.out.println(SQL.toString());
         int rs=-1;
         rs=stQuery.executeUpdate(SQL.toString());
       } //Fin try
       catch(Exception e){
         System.out.println("Ocurrio un error al accesar al metodo update_rf_tr_polizas_carga_estatus_can "+e.getMessage());
         throw e;
       } //Fin catch
       finally{
         if (stQuery!=null){
           stQuery.close();
           stQuery=null;
         }
       } //Fin finally
     } //Fin metodo update_rf_tr_polizas_carga_estatus
 
   /**
   * Metodo que borra la informacion de rf_tr_polizas_carga
   * fechAlta de creacion:
   * Autor:
   * fechAlta de modificacion:
   * Modificado por:
   */
   public void delete_rf_tr_polizas_carga(Connection con, String pPolizaId)throws SQLException, Exception{
      Statement stQuery=null;
      try{
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
       StringBuffer SQL=new StringBuffer("DELETE FROM rf_tr_polizas_carga a ");
       SQL.append("WHERE a.poliza_id=").append(pPolizaId).append(" ");
       System.out.println(SQL.toString());
       int rs=-1;
       rs=stQuery.executeUpdate(SQL.toString());
     } //Fin try
     catch(Exception e){
       System.out.println("Ocurrio un error al accesar al metodo delete_rf_tr_polizas_carga "+e.getMessage());
       throw e;
     } //Fin catch
     finally{
       if (stQuery!=null){
         stQuery.close();
         stQuery=null;
       }
     } //Fin finally
   } //Fin metodo delete_rf_tr_polizas_carga
   
   
    /**
    * Metodo que lee la sequencia para la poliza que se va a generar
    * fechAlta de creacion:08/06/2009
    * Autor:Jorge Luis Perez N.
    * fechAlta de modificacion:
    * Modificado por:
    */
    
    public String select_SEQ_rf_tc_modifica_ambitos(Connection con, String numEmpleado)throws SQLException, Exception{
       Statement stQuery=null;
       ResultSet rsQuery= null;
       String estatus = "0";
       try{
         String SQL2 = "select estatus from rf_tc_pers_modifica_ambitos where numEmpleado= "+numEmpleado;     
         stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
         rsQuery = stQuery.executeQuery(SQL2);
         //  secuencia para poliza_id
         while (rsQuery.next()){
            estatus = rsQuery.getString("estatus");
         } //del while
       } //Fin try
       catch(Exception e){
         System.out.println("Ocurrio un error al accesar al metodo select_SEQ_rf_tc_modifica_ambitos "+e.getMessage());
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
       return estatus;
    } //fin Select 
        
   
} //Fin clase bcrf_tr_polizas_carga

