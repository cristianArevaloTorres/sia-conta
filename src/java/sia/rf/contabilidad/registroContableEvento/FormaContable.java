package sia.rf.contabilidad.registroContableEvento;

import java.sql.*;
import sun.jdbc.rowset.CachedRowSet;

public class FormaContable{ 
   public FormaContable(){ 
   } 
   private String forma; 
   private String bean; 
   private String metodo; 
   private String idforma; 
   private String idevento; 
   private String unidadejecutora; 
   private String entidad; 
   private String referencia; 
   private String variables; 
   private String numempleadoautorizado; 
   private String clavesw; 
   private String fechaafectacion; 
   private String precondiciones; 
   private String descripcion; 
   private String parametros; 
   private String observaciones; 
   private String ambito;
   private String catalogocuenta;
   private int factor;
   private int factorNetoNeg;
  
   /** 
   * forma 
   * @return forma 
   */ 
   public String getForma() { 
      return forma; 
   } 
  
   /** 
   * forma 
   * @param forma 
   */ 
   public void setForma( String forma ) { 
      this.forma=forma; 
   } 
  
   /** 
   * bean 
   * @return bean 
   */ 
   public String getBean() { 
      return bean; 
   } 
  
   /** 
   * bean 
   * @param bean 
   */ 
   public void setBean( String bean ) { 
      this.bean=bean; 
   } 
  
   /** 
   * metodo 
   * @return metodo 
   */ 
   public String getMetodo() { 
      return metodo; 
   } 
  
   /** 
   * metodo 
   * @param metodo 
   */ 
   public void setMetodo( String metodo ) { 
      this.metodo=metodo; 
   } 
  
   /** 
   * idforma 
   * @return idforma 
   */ 
   public String getIdforma() { 
      return idforma; 
   } 
  
   /** 
   * idforma 
   * @param idforma 
   */ 
   public void setIdforma( String idforma ) { 
      this.idforma=idforma; 
   } 
  
   /** 
   * idevento 
   * @return idevento 
   */ 
   public String getIdevento() { 
      return idevento; 
   } 
  
   /** 
   * idevento 
   * @param idevento 
   */ 
   public void setIdevento( String idevento ) { 
      this.idevento=idevento; 
   } 
  
   /** 
   * unidadejecutora 
   * @return unidadejecutora 
   */ 
   public String getUnidadejecutora() { 
      return unidadejecutora; 
   } 
  
   /** 
   * unidadejecutora 
   * @param unidadejecutora 
   */ 
   public void setUnidadejecutora( String unidadejecutora ) { 
      this.unidadejecutora=unidadejecutora; 
   } 
  
   /** 
   * entidad 
   * @return entidad 
   */ 
   public String getEntidad() { 
      return entidad; 
   } 
  
   /** 
   * entidad 
   * @param entidad 
   */ 
   public void setEntidad( String entidad ) { 
      this.entidad=entidad; 
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
   * variables 
   * @return variables 
   */ 
   public String getVariables() { 
      return variables; 
   } 
  
   /** 
   * variables 
   * @param variables 
   */ 
   public void setVariables( String variables ) { 
      this.variables=variables; 
   } 
  
   /** 
   * numempleadoautorizado 
   * @return numempleadoautorizado 
   */ 
   public String getNumempleadoautorizado() { 
      return numempleadoautorizado; 
   } 
  
   /** 
   * numempleadoautorizado 
   * @param numempleadoautorizado 
   */ 
   public void setNumempleadoautorizado( String numempleadoautorizado ) { 
      this.numempleadoautorizado=numempleadoautorizado; 
   } 
  
   /** 
   * clavesw 
   * @return clavesw 
   */ 
   public String getClavesw() { 
      return clavesw; 
   } 
  
   /** 
   * clavesw 
   * @param clavesw 
   */ 
   public void setClavesw( String clavesw ) { 
      this.clavesw=clavesw; 
   } 
  
   /** 
   * fechaafectacion 
   * @return fechaafectacion 
   */ 
   public String getFechaafectacion() { 
      return fechaafectacion; 
   } 
  
   /** 
   * fechaafectacion 
   * @param fechaafectacion 
   */ 
   public void setFechaafectacion( String fechaafectacion ) { 
      this.fechaafectacion=fechaafectacion; 
   } 
  
   /** 
   * precondiciones 
   * @return precondiciones 
   */ 
   public String getPrecondiciones() { 
      return precondiciones; 
   } 
  
   /** 
   * precondiciones 
   * @param precondiciones 
   */ 
   public void setPrecondiciones( String precondiciones ) { 
      this.precondiciones=precondiciones; 
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
   * parametros 
   * @return parametros 
   */ 
   public String getParametros() { 
      return parametros; 
   } 
  
   /** 
   * parametros 
   * @param parametros 
   */ 
   public void setParametros( String parametros ) { 
      this.parametros=parametros; 
   } 
  
   /** 
   * observaciones 
   * @return observaciones 
   */ 
   public String getObservaciones() { 
      return observaciones; 
   } 
  
   /** 
   * observaciones 
   * @param observaciones 
   */ 
   public void setObservaciones( String observaciones ) { 
      this.observaciones=observaciones; 
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
    * catalogoCuenta 
    * @return catalogoCuenta 
    */ 
    public String getCatalogocuenta() { 
       return catalogocuenta; 
    } 
    
    /** 
    * catalogoCuenta 
    * @param catalogoCuenta 
    */ 
    public void setCatalogoCuenta( String catalogouenta ) { 
       this.catalogocuenta=catalogouenta; 
    } 
    
    public int getFactor() {
        return factor;
    }

    public void setFactor(int factor) {
        this.factor = factor;
    }

    public void setFactorNetoNeg(int factorNetoNeg) {
        this.factorNetoNeg = factorNetoNeg;
    }

    public int getFactorNetoNeg() {
        return factorNetoNeg;
    }  
   /** 
   * Metodo que lee la informacion de rf_tr_formaContable 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
    public CachedRowSet select_rf_tr_formaContable(Connection con, String pEvento)throws SQLException, Exception{ 
           CachedRowSet rsQuery=null;
           StringBuffer SQL=new StringBuffer("");
          try{ 
           rsQuery = new CachedRowSet();
           SQL.append("SELECT a.forma,a.bean,a.metodo,a.idforma,a.idevento,");
           SQL.append("a.unidadejecutora,a.entidad,a.referencia,a.concepto,a.variables,a.numempleadoautorizado,");
           SQL.append("a.clavesw,a.fechaafectacion,a.precondiciones,a.descripcion,a.parametros,a.observaciones, ");
           SQL.append("a.ambito, a.catalogocuenta, replace(substr(descripcion,0,2),' ','') movimiento,a.factor, a.tipo, a.factorNetoNeg  ");  

           SQL.append(" FROM rf_tr_formaContable a "); 
           SQL.append(" WHERE a.idevento=").append(pEvento).append(" order by a.idforma"); 
           //SQL.append(" ORDER BY ");
           
           rsQuery.setCommand(SQL.toString());
           rsQuery.execute(con);
           /**
           while (rsQuery.next()){ 
              forma=(rsQuery.getString("forma")==null) ? "" : rsQuery.getString("forma"); 
              bean=(rsQuery.getString("bean")==null) ? "" : rsQuery.getString("bean"); 
              metodo=(rsQuery.getString("metodo")==null) ? "" : rsQuery.getString("metodo"); 
              idforma=(rsQuery.getString("idforma")==null) ? "" : rsQuery.getString("idforma"); 
              idevento=(rsQuery.getString("idevento")==null) ? "" : rsQuery.getString("idevento"); 
              unidadejecutora=(rsQuery.getString("unidadejecutora")==null) ? "" : rsQuery.getString("unidadejecutora"); 
              entidad=(rsQuery.getString("entidad")==null) ? "" : rsQuery.getString("entidad"); 
              referencia=(rsQuery.getString("referencia")==null) ? "" : rsQuery.getString("referencia"); 
              variables=(rsQuery.getString("variables")==null) ? "" : rsQuery.getString("variables"); 
              numempleadoautorizado=(rsQuery.getString("numempleadoautorizado")==null) ? "" : rsQuery.getString("numempleadoautorizado"); 
              clavesw=(rsQuery.getString("clavesw")==null) ? "" : rsQuery.getString("clavesw"); 
              fechaafectacion=(rsQuery.getString("fechaafectacion")==null) ? "" : rsQuery.getString("fechaafectacion"); 
              precondiciones=(rsQuery.getString("precondiciones")==null) ? "" : rsQuery.getString("precondiciones"); 
              descripcion=(rsQuery.getString("descripcion")==null) ? "" : rsQuery.getString("descripcion"); 
              parametros=(rsQuery.getString("parametros")==null) ? "" : rsQuery.getString("parametros"); 
              observaciones=(rsQuery.getString("observaciones")==null) ? "" : rsQuery.getString("observaciones"); 
              ambito=(rsQuery.getString("ambito")==null) ? "" : rsQuery.getString("ambito"); 
              catalogocuenta=(rsQuery.getString("catalogocuenta")==null) ? "" : rsQuery.getString("catalogocuenta"); 
           } // Fin while **/
         } //Fin try 
         catch(Exception e){ 
           System.out.println("Ocurrio un error al accesar al metodo select_rf_tr_formaContable "+e.getMessage()); 
           System.out.println("Query en método select_rf_tr_formaContable: "+SQL.toString()); 
           throw e; 
         } //Fin catch 
         finally{ 
        /**   if (rsQuery!=null){ 
             rsQuery.close(); 
             rsQuery=null; 
           } **/
         } //Fin finally 
         return rsQuery;
       } //Fin metodo select_rf_tr_formaContable 
  
   /** 
   * Metodo que inserta la informacion de rf_tr_formaContable 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void insert_rf_tr_formaContable(Connection con)throws SQLException, Exception{ 
      Statement stQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("INSERT INTO rf_tr_formaContable( forma,bean,metodo,idforma,idevento,unidadejecutora,entidad,referencia,variables,numempleadoautorizado,clavesw,fechaafectacion,precondiciones,descripcion,parametros,observaciones) ");  
       SQL.append("VALUES("); 
       SQL.append("'").append(forma).append("',"); 
       SQL.append("'").append(bean).append("',"); 
       SQL.append("'").append(metodo).append("',"); 
       SQL.append(idforma).append(","); 
       SQL.append(idevento).append(","); 
       SQL.append("'").append(unidadejecutora).append("',"); 
       SQL.append(entidad).append(","); 
       SQL.append("'").append(referencia).append("',"); 
       SQL.append("'").append(variables).append("',"); 
       SQL.append(numempleadoautorizado).append(","); 
       SQL.append("'").append(clavesw).append("',"); 
       SQL.append("'").append(fechaafectacion).append("',"); 
       SQL.append("'").append(precondiciones).append("',"); 
       SQL.append("'").append(descripcion).append("',"); 
       SQL.append("'").append(parametros).append("',"); 
       SQL.append("'").append(observaciones).append("')"); 
       System.out.println(SQL.toString()); 
       int rs=-1; 
       rs=stQuery.executeUpdate(SQL.toString()); 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo insert_rf_tr_formaContable "+e.getMessage()); 
       throw e; 
     } //Fin catch 
     finally{ 
       if (stQuery!=null){ 
         stQuery.close();
       } 
     } //Fin finally 
   } //Fin metodo insert_rf_tr_formaContable 
  
   /** 
   * Metodo que modifica la informacion de rf_tr_formaContable 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void update_rf_tr_formaContable(Connection con,  String clave)throws SQLException, Exception{ 
      Statement stQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("UPDATE rf_tr_formaContable"); 
       SQL.append(" SET forma=").append("'").append(forma).append("',"); 
       SQL.append("bean=").append("'").append(bean).append("',"); 
       SQL.append("metodo=").append("'").append(metodo).append("',"); 
       SQL.append("idforma=").append(idforma).append(","); 
       SQL.append("idevento=").append(idevento).append(","); 
       SQL.append("unidadejecutora=").append("'").append(unidadejecutora).append("',"); 
       SQL.append("entidad=").append(entidad).append(","); 
       SQL.append("referencia=").append("'").append(referencia).append("',"); 
       SQL.append("variables=").append("'").append(variables).append("',"); 
       SQL.append("numempleadoautorizado=").append(numempleadoautorizado).append(","); 
       SQL.append("clavesw=").append("'").append(clavesw).append("',"); 
       SQL.append("fechaafectacion=").append("'").append(fechaafectacion).append("',"); 
       SQL.append("precondiciones=").append("'").append(precondiciones).append("',"); 
       SQL.append("descripcion=").append("'").append(descripcion).append("',"); 
       SQL.append("parametros=").append("'").append(parametros).append("',"); 
       SQL.append("observaciones=").append("'").append(observaciones); 
       SQL.append(" WHERE idforma=").append(clave).append(" "); 
       System.out.println(SQL.toString()); 
       int rs=-1; 
       rs=stQuery.executeUpdate(SQL.toString()); 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo update_rf_tr_formaContable "+e.getMessage()); 
       throw e; 
     } //Fin catch 
     finally{ 
       if (stQuery!=null){ 
         stQuery.close();
       } 
     } //Fin finally 
   } //Fin metodo update_rf_tr_formaContable 
  
   /** 
   * Metodo que borra la informacion de rf_tr_formaContable 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void delete_rf_tr_formaContable(Connection con, String clave)throws SQLException, Exception{ 
      Statement stQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("DELETE FROM rf_tr_formaContable a "); 
       SQL.append("WHERE a.idforma=").append(clave).append(" "); 
       System.out.println(SQL.toString()); 
       int rs=-1; 
       rs=stQuery.executeUpdate(SQL.toString()); 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo delete_rf_tr_formaContable "+e.getMessage()); 
       throw e; 
     } //Fin catch 
     finally{ 
       if (stQuery!=null){ 
         stQuery.close(); 
       } 
     } //Fin finally 
   } //Fin metodo delete_rf_tr_formaContable 

 
} //Fin clase bcRf_tr_formacontable 