package sia.rf.contabilidad.registroContableNuevo;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;

import sia.rf.contabilidad.registroContable.procesos.polizas.servicios.Debe;
import sia.rf.contabilidad.registroContable.procesos.polizas.servicios.DetallePoliza;
import sia.rf.contabilidad.registroContable.procesos.polizas.servicios.Haber;

public class bcDetallePoliza{
   public bcDetallePoliza(){
   }
   private String operacion_contable_id;
   private String referencia;
   private String fecha_afectacion;
   private String id_detalle;
   private String importe;
   private String poliza_id;
   private String cuenta_contable_id;
 
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
   * fecha_afectacion
   * @return fecha_afectacion
   */
   public String getFecha_afectacion() {
      return fecha_afectacion;
   }
 
   /**
   * fecha_afectacion
   * @param fecha_afectacion
   */
   public void setFecha_afectacion( String fecha_afectacion ) {
      this.fecha_afectacion=fecha_afectacion;
   }
 
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
   * cuenta_contable_id
   * @return cuenta_contable_id
   */
   public String getCuenta_contable_id() {
      return cuenta_contable_id;
   }
 
   /**
   * cuenta_contable_id
   * @param cuenta_contable_id
   */
   public void setCuenta_contable_id( String cuenta_contable_id ) {
      this.cuenta_contable_id=cuenta_contable_id;
   }
   
    public String select_SEQ_rf_tr_detalle_polizas(Connection con)throws SQLException, Exception{
       Statement stQuery=null;
       ResultSet rsQuery= null;
       String SidPoliza = "";
       try{
         String SQL2 = "select seq_rf_tr_detalle_poliza.nextval valoractual from dual";     
         stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
         rsQuery = stQuery.executeQuery(SQL2);
         //  secuencia para poliza_id
         while (rsQuery.next()){
            SidPoliza = rsQuery.getString("valoractual");
         } //del while
       } //Fin try
       catch(Exception e){
         System.out.println("Ocurrio un error al accesar al metodo select_SEQ_rf_tr_detalle_polizas "+e.getMessage());
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
   * Metodo que lee la informacion de rf_tr_detalle_poliza
   * Fecha de creacion:
   * Autor:
   * Fecha de modificacion:
   * Modificado por:
   */
   public void select_rf_tr_detalle_poliza(Connection con, String pPolizaId)throws SQLException, Exception{
      Statement stQuery=null;
      ResultSet rsQuery=null;
      try{
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
       StringBuffer SQL=new StringBuffer("SELECT a.operacion_contable_id,a.referencia,a.fecha_afectacion,a.id_detalle,a.importe,a.poliza_id,a.cuenta_contable_id"); 
       SQL.append(" FROM rf_tr_detalle_poliza a ");
       SQL.append(" WHERE a.poliza_id=").append(pPolizaId).append(" ");
       System.out.println(SQL.toString());
       rsQuery=stQuery.executeQuery(SQL.toString());
       while (rsQuery.next()){
          operacion_contable_id=(rsQuery.getString("operacion_contable_id")==null) ? "" : rsQuery.getString("operacion_contable_id");
          referencia=(rsQuery.getString("referencia")==null) ? "" : rsQuery.getString("referencia");
          fecha_afectacion=(rsQuery.getString("fecha_afectacion")==null) ? "" : rsQuery.getString("fecha_afectacion");
          id_detalle=(rsQuery.getString("id_detalle")==null) ? "" : rsQuery.getString("id_detalle");
          importe=(rsQuery.getString("importe")==null) ? "" : rsQuery.getString("importe");
          poliza_id=(rsQuery.getString("poliza_id")==null) ? "" : rsQuery.getString("poliza_id");
          cuenta_contable_id=(rsQuery.getString("cuenta_contable_id")==null) ? "" : rsQuery.getString("cuenta_contable_id");
       } // Fin while
     } //Fin try
     catch(Exception e){
       System.out.println("Ocurrio un error al accesar al metodo select_rf_tr_detalle_poliza "+e.getMessage());
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
   } //Fin metodo select_rf_tr_detalle_poliza


    /**
    * Metodo que lee la informacion de rf_tr_detalle_poliza
    * Fecha de creacion:
    * Autor:
    * Fecha de modificacion:
    * Modificado por:
    */
    public List select_rf_tr_detalle_poliza_res(Connection con, String pPolizaId)throws SQLException, Exception{
       Statement stQuery=null;
       ResultSet rsQuery=null;
       List  lsTabla= new ArrayList();
       String lsRenglon="";
       StringBuffer SQL=new StringBuffer("SELECT a.operacion_contable_id,a.referencia,to_char(a.fecha_afectacion,'dd/mm/yyyy') fecha_afectacion,a.id_detalle,a.importe,a.poliza_id,a.cuenta_contable_id"); 
       SQL.append(" FROM rf_tr_detalle_poliza a ");
       SQL.append(" WHERE a.poliza_id=").append(pPolizaId).append(" order by id_detalle ");
        // System.out.println(SQL.toString());       
       try{
        stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        
        /*StringBuffer SQL=new StringBuffer("SELECT a.operacion_contable_id,a.referencia,to_char(b.fecha,'dd/mm/yyyy') fecha_afectacion,a.id_detalle,a.importe,a.poliza_id,a.cuenta_contable_id"); 
        SQL.append(" FROM rf_tr_detalle_poliza a, rf_tr_polizas b ");
        SQL.append(" WHERE a.poliza_id=b.poliza_id and a.poliza_id=").append(pPolizaId).append(" ");
        
        CLHL comentado por si se usa mas adelante
        */
        
        rsQuery=stQuery.executeQuery(SQL.toString());
        while (rsQuery.next()){
           operacion_contable_id=(rsQuery.getString("operacion_contable_id")==null) ? "" : rsQuery.getString("operacion_contable_id");
           referencia=(rsQuery.getString("referencia")==null) ? "" : rsQuery.getString("referencia");
           fecha_afectacion=(rsQuery.getString("fecha_afectacion")==null) ? "" : rsQuery.getString("fecha_afectacion");
           id_detalle=(rsQuery.getString("id_detalle")==null) ? "" : rsQuery.getString("id_detalle");
           importe=(rsQuery.getString("importe")==null) ? "" : rsQuery.getString("importe");
           poliza_id=(rsQuery.getString("poliza_id")==null) ? "" : rsQuery.getString("poliza_id");
           cuenta_contable_id=(rsQuery.getString("cuenta_contable_id")==null) ? "" : rsQuery.getString("cuenta_contable_id");
           lsRenglon=operacion_contable_id+"&"+referencia+"&"+fecha_afectacion+"&"+id_detalle+"&"+importe+"&"+poliza_id+"&"+cuenta_contable_id+"&1&2&3";
           lsTabla.add(lsRenglon);
           lsRenglon="";
        } // Fin while
      } //Fin try
      catch(Exception e){
        System.out.println("Ocurrio un error al accesar al metodo select_rf_tr_detalle_poliza_res "+e.getMessage());
        System.out.println(SQL.toString());
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
      }
      return lsTabla;//Fin finally
    } //Fin metodo select_rf_tr_detalle_poliza


     /**
     * Metodo que lee la informacion de rf_tr_detalle_poliza
     * Fecha de creacion:
     * Autor:
     * Fecha de modificacion:
     * Modificado por:
     */
     public List select_rf_tr_detalle_poliza_respaldo(Connection con, String pPolizaId)throws SQLException, Exception{
        Statement stQuery=null;
        ResultSet rsQuery=null;
        List  lsTabla= new ArrayList();
        StringBuffer lsRenglon=new StringBuffer("");
        try{
         stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
         StringBuffer SQL=new StringBuffer("SELECT a.operacion_contable_id,a.referencia,a.fecha_afectacion,a.id_detalle,a.importe,a.poliza_id,a.cuenta_contable_id"); 
         SQL.append(" FROM rf_tr_detalle_poliza a ");
         SQL.append(" WHERE a.poliza_id=").append(pPolizaId).append(" ");
         System.out.println(SQL.toString());
         rsQuery=stQuery.executeQuery(SQL.toString());
         while (rsQuery.next()){
            operacion_contable_id=(rsQuery.getString("operacion_contable_id")==null) ? "" : rsQuery.getString("operacion_contable_id");
            referencia=(rsQuery.getString("referencia")==null) ? "" : rsQuery.getString("referencia");
            fecha_afectacion=(rsQuery.getString("fecha_afectacion")==null) ? "" : rsQuery.getString("fecha_afectacion");
            id_detalle=(rsQuery.getString("id_detalle")==null) ? "" : rsQuery.getString("id_detalle");
            importe=(rsQuery.getString("importe")==null) ? "" : rsQuery.getString("importe");
            poliza_id=(rsQuery.getString("poliza_id")==null) ? "" : rsQuery.getString("poliza_id");
            cuenta_contable_id=(rsQuery.getString("cuenta_contable_id")==null) ? "" : rsQuery.getString("cuenta_contable_id");
            lsRenglon.append(operacion_contable_id).append("&").append(referencia).append("&").append(fecha_afectacion).append("&");
            lsRenglon.append(id_detalle).append("&").append(importe).append("&").append(poliza_id).append("&").append(cuenta_contable_id);
            lsTabla.add(lsRenglon);
            lsRenglon.setLength(0);
         } // Fin while
       } //Fin try
       catch(Exception e){
         System.out.println("Ocurrio un error al accesar al metodo select_rf_tr_detalle_poliza_res "+e.getMessage());
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
       }
       return lsTabla;//Fin finally
     } //Fin metodo select_rf_tr_detalle_poliza
     
      public List<DetallePoliza> select_rf_tr_detalle_poliza(String pIdPoliza, Connection con) throws SQLException, Exception{
         Statement stQuery=null;
         ResultSet rsQuery=null;
         List<DetallePoliza> detallePoliza = null;
         try{
          detallePoliza = new ArrayList<DetallePoliza>();
          stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
          StringBuffer SQL=new StringBuffer("select poliza_id, cuenta_contable_id, importe, operacion_contable_id, ");
          SQL.append("nvl(referencia,' ') as referencia, to_char(fecha_afectacion,'yyyyMMdd') fecha_afectacion ");
          SQL.append("from rf_tr_detalle_poliza where poliza_id=").append(pIdPoliza);
          System.out.println(SQL.toString());
          rsQuery=stQuery.executeQuery(SQL.toString());
          while (rsQuery.next()){
            if (rsQuery.getString("operacion_contable_id").equals("0")) {
              detallePoliza.add(new DetallePoliza(rsQuery.getString("referencia"), rsQuery.getString("fecha_afectacion"), new Debe(Double.valueOf(rsQuery.getString("importe"))), Integer.valueOf(rsQuery.getString("cuenta_contable_id"))));
            } //end if(vista.getCurrentRow().getA...
            else {
              detallePoliza.add(new DetallePoliza(rsQuery.getString("referencia"), rsQuery.getString("fecha_afectacion"), new Haber(Double.valueOf(rsQuery.getString("importe"))), Integer.valueOf(rsQuery.getString("cuenta_contable_id"))));
            } //end else
          } // Fin while
        } //Fin try
        catch(Exception e){
          System.out.println("Ocurrio un error al accesar al metodo select_rf_tr_detalle_poliza "+e.getMessage());
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
        return detallePoliza;
      } //Fin metodo select_rf_tr_polizas



   /**
   * Metodo que inserta la informacion de rf_tr_detalle_poliza_contable
   * Fecha de creacion:
   * Autor:
   * Fecha de modificacion:
   * Modificado por:
   */
   public void insert_rf_tr_detalle_poliza(Connection con)throws SQLException, Exception{
      Statement stQuery=null;
      StringBuffer SQL=new StringBuffer("INSERT INTO rf_tr_detalle_poliza( operacion_contable_id,referencia,fecha_afectacion,id_detalle,importe,poliza_id,cuenta_contable_id) "); 
      try{
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
       SQL.append("VALUES(");
       SQL.append(operacion_contable_id).append(",");
       SQL.append("'").append(referencia).append("',");
       SQL.append("to_date('").append(fecha_afectacion).append("','dd/mm/yyyy'),");
       SQL.append(id_detalle).append(",");
       SQL.append(importe).append(",");
       SQL.append(poliza_id).append(",");
       SQL.append(cuenta_contable_id).append(")");
       // System.out.println(SQL.toString());
       int rs=-1;
       rs=stQuery.executeUpdate(SQL.toString());
     } //Fin try
     catch(Exception e){
       System.out.println("Ocurrio un error al accesar al metodo insert_rf_tr_detalle_poliza "+e.getMessage());
       System.out.println(SQL.toString());
       throw e;
     } //Fin catch
     finally{
       if (stQuery!=null){
         stQuery.close();
         stQuery=null;
       }
     } //Fin finally
   } //Fin metodo insert_rf_tr_detalle_poliza
 
   /**
   * Metodo que modifica la informacion de rf_tr_detalle_poliza
   * Fecha de creacion:
   * Autor:
   * Fecha de modificacion:
   * Modificado por:
   */
   public void update_rf_tr_detalle_poliza(Connection con, String clave)throws SQLException, Exception{
      Statement stQuery=null;
      try{
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
       StringBuffer SQL=new StringBuffer("UPDATE rf_tr_detalle_poliza");
       SQL.append(" SET operacion_contable_id=").append(operacion_contable_id).append(",");
       SQL.append("referencia=").append("'").append(referencia).append("',");
       SQL.append("fecha_afectacion=").append("to_date('").append(fecha_afectacion).append("','dd/mm/yyyy'),");
       SQL.append("id_detalle=").append(id_detalle).append(",");
       SQL.append("importe=").append(importe).append(",");
       SQL.append("poliza_id=").append(poliza_id).append(",");
       SQL.append("cuenta_contable_id=").append(cuenta_contable_id);
       SQL.append(" WHERE LLAVE='").append(clave).append("'");
       System.out.println(SQL.toString());
       int rs=-1;
       rs=stQuery.executeUpdate(SQL.toString());
     } //Fin try
     catch(Exception e){
       System.out.println("Ocurrio un error al accesar al metodo update_rf_tr_detalle_poliza "+e.getMessage());
       throw e;
     } //Fin catch
     finally{
       if (stQuery!=null){
         stQuery.close();
         stQuery=null;
       }
     } //Fin finally
   } //Fin metodo update_rf_tr_detalle_poliza
 
   /**
   * Metodo que borra la informacion de rf_tr_detalle_poliza
   * Fecha de creacion:
   * Autor:
   * Fecha de modificacion:
   * Modificado por:
   */
   public void delete_rf_tr_detalle_poliza(Connection con, String pPolizaId)throws SQLException, Exception{
      Statement stQuery=null;
      stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      StringBuffer SQL=new StringBuffer("DELETE FROM rf_tr_detalle_poliza a ");
      SQL.append("WHERE a.poliza_id=").append(pPolizaId).append(" ");      
      try{
       // System.out.println(SQL.toString());
       int rs=-1;
       rs=stQuery.executeUpdate(SQL.toString());
     } //Fin try
     catch(Exception e){
       System.out.println("Ocurrio un error al accesar al metodo delete_rf_tr_detalle_poliza "+e.getMessage());
       System.out.println(SQL.toString());
       throw e;
     } //Fin catch
     finally{
       if (stQuery!=null){
         stQuery.close();
         stQuery=null;
       }
     } //Fin finally
   } //Fin metodo delete_rf_tr_detalle_poliza
   
   
    /**
     * Descripcion: Verifica si existen polizas para un valor de cuenta_contable_id especifico
     * <p>Fecha de creacion: 16/Diciembre/2010</p>
     * <p>Autor: Elida Pozos Vazquez</p>
     */     
     public int selectTotalPol(Connection con, String ctaContId) throws SQLException, Exception  {
         Statement stQuery=null;
         ResultSet rsQuery= null;
         StringBuffer SQL=null;
         int tempo=0;
         try{
             stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
             SQL=new StringBuffer("SELECT count(cuenta_contable_id) as total FROM rf_tr_detalle_poliza ");
             SQL.append("WHERE cuenta_contable_id="+ctaContId);
             rsQuery = stQuery.executeQuery(SQL.toString());
             while (rsQuery.next()){
                tempo=rsQuery.getInt("total");
             }          
          }catch (Exception e){
                  System.out.println("Ocurrio un error al accesar el metodo bcDetallePoliza.selectTotalPol() "+ e.getMessage());  
                  System.out.println(SQL);
                  throw e;
          }finally{
              if (rsQuery != null){
                  rsQuery.close();
                  rsQuery=null;
              }

              if (stQuery != null){
                  stQuery.close();
                  stQuery=null;
              }
          }//finally 
          return tempo;
       }  
} //Fin clase bcDetallePoliza
 