package sia.rf.contabilidad.registroContableNuevo;

import java.sql.*; 
public class bcCheque{ 
   public bcCheque(){ 
   } 
   private String cheque_id; 
   private String cuenta_cheques_id; 
   private String consecutivo; 
   private String abreviatura; 
   private String fecha; 
   private String importe; 
   private String referencia; 
   private String poliza_id; 
   private String fecha_impresion;
   private String estatus;
   private String fecha_ult_react;
   private String beneficiario;
   private String numEmpleado;
   private String digitoVerificador;
   private String fechaCheque;
   private String origenDocto;
   private String cxl;
   private String cxlSup;
   private String operacionPago;
   
  
   public String  getOperacion_Pago()
   { 
       return operacionPago;
   }
   
   
   public void setOperacion_Pago (String operacionPago)
   {
       this.operacionPago = operacionPago;
   }
   
   /** 
   * cheque_id 
   * @return cheque_id 
   */ 
   public String getCheque_id() { 
      return cheque_id; 
   } 
  
   /** 
   * cheque_id 
   * @param cheque_id 
   */ 
   public void setCheque_id( String cheque_id ) { 
      this.cheque_id=cheque_id; 
   } 
  
   /** 
   * cuenta_cheques_id 
   * @return cuenta_cheques_id 
   */ 
   public String getCuenta_cheques_id() { 
      return cuenta_cheques_id; 
   } 
  
   /** 
   * cuenta_cheques_id 
   * @param cuenta_cheques_id 
   */ 
   public void setCuenta_cheques_id( String cuenta_cheques_id ) { 
      this.cuenta_cheques_id=cuenta_cheques_id; 
   } 
  
   /** 
   * consecutivo 
   * @return consecutivo 
   */ 
   public String getConsecutivo() { 
      return consecutivo; 
   } 
  
   /** 
   * consecutivo 
   * @param consecutivo 
   */ 
   public void setConsecutivo( String consecutivo ) { 
      this.consecutivo=consecutivo; 
   } 
  
   /** 
   * abreviatura 
   * @return abreviatura 
   */ 
   public String getAbreviatura() { 
      return abreviatura; 
   } 
  
   /** 
   * abreviatura 
   * @param abreviatura 
   */ 
   public void setAbreviatura( String abreviatura ) { 
      this.abreviatura=abreviatura; 
   } 
  
   /** 
   * fecha 
   * @return fecha 
   */ 
   public String getFecha() { 
      return fecha; 
   } 
  
   /** 
   * fecha 
   * @param fecha 
   */ 
   public void setFecha( String fecha ) { 
      this.fecha=fecha; 
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
  
    public String getFecha_impresion() {
        return fecha_impresion;
    }

    public void setFecha_impresion(String fecha_impresion) {
        this.fecha_impresion = fecha_impresion;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getFecha_ult_react() {
        return fecha_ult_react;
    }

    public void setFecha_ult_react(String fecha_ult_react) {
        this.fecha_ult_react = fecha_ult_react;
    }

    public String getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(String beneficiario) {
        this.beneficiario = beneficiario;
    }

    public String getNumEmpleado() {
        return numEmpleado;
    }

    public void setNumEmpleado(String numEmpleado) {
        this.numEmpleado = numEmpleado;
    }
    
    public String getDigitoVerificador() {
        return digitoVerificador;
    }

    public void setDigitoVerificador(String digitoVerificador) {
        this.digitoVerificador = digitoVerificador;
    }
    
    public String getFechaCheque() {
        return fechaCheque;
    }

    public void setFechaCheque(String fechaCheque) {
        this.fechaCheque = fechaCheque;
    }
    
    public String getOrigenDocto() {
        return origenDocto;
    }

    public void setOrigenDocto(String origenDocto) {
        this.origenDocto = origenDocto;
    }
    
    public String getCxl() {
        return cxl;
    }

    public void setCxl(String cxl) {
        this.cxl = cxl;
    }

    public String getCxlSup() {
        return cxlSup;
    }

    public void setCxlSup(String cxlSup) {
        this.cxlSup = cxlSup;
    }
  
    /**
    * Metodo que lee la sequencia para el cheque que se va a generar
    * Fecha de creacion:08/06/2009
    * Autor:Jorge Luis Perez N.
    * Fecha de modificacion:
    * Modificado por:
    */
    
    public String select_SEQ_rf_tr_cheques(Connection con)throws SQLException, Exception{
       Statement stQuery=null;
       ResultSet rsQuery= null;
       String SidCheque = "";
       try{
         String SQL2 = "select seq_rf_tr_cheques.nextval valoractual from dual";     
         stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
         rsQuery = stQuery.executeQuery(SQL2);
         //  secuencia para poliza_id
         while (rsQuery.next()){
            SidCheque = rsQuery.getString("valoractual");
         } //del while
       } //Fin try
       catch(Exception e){
         System.out.println("Ocurrio un error al accesar al metodo select_SEQ_rf_tr_cheques "+e.getMessage());
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
       return SidCheque;
    } //fin Select 
    
    /**
    * Fecha de creacion: 24/02/2017
    * Autor: Alvaro Vargas.
    * Fecha de modificacion:
    * Modificado por:
    */
    
    public String select_consecutivo_m(Connection con, String lsIdChequeConta)throws SQLException, Exception{
       Statement stQuery=null;
       ResultSet rsQuery= null;
       String SidCheque = "";
       try{
         String SQL2 = "select consecutivo from rf_tr_chequesm where cheque_id = " + lsIdChequeConta;     
         stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
         rsQuery = stQuery.executeQuery(SQL2);
         //  secuencia para poliza_id
         while (rsQuery.next()){
            SidCheque = rsQuery.getString("consecutivo");
         } //del while
       } //Fin try
       catch(Exception e){
         System.out.println("Ocurrio un error al accesar al metodo select_consecutivo_m "+e.getMessage());
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
       return SidCheque;
    } //fin Select 
    
    
    public String select_consecutivo_ARE(Connection con, String lsIdChequeConta)throws SQLException, Exception{
       Statement stQuery=null;
       ResultSet rsQuery= null;
       String SidCheque = "";
       try{
         String SQL2 = "select consecutivo from rf_tr_cheques where cheque_id = " + lsIdChequeConta;     
         stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
         rsQuery = stQuery.executeQuery(SQL2);
         //  secuencia para poliza_id
         while (rsQuery.next()){
            SidCheque = rsQuery.getString("consecutivo");
         } //del while
       } //Fin try
       catch(Exception e){
         System.out.println("Ocurrio un error al accesar al metodo select_consecutivo_m "+e.getMessage());
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
       return SidCheque;
    } //fin Select 

     /**
     * Metodo que lee la sequencia para el cheque que se va a generar
     * Fecha de creacion:08/06/2009
     * Autor:Jorge Luis Perez N.
     * Fecha de modificacion:
     * Modificado por:
     */
     
     public String select_digito_verificador(Connection con, String pDigitoVer)throws SQLException, Exception{
        Statement stQuery=null;
        ResultSet rsQuery= null;
        String resDigito = "-1";
        try{
          String SQL2 = "select f_digito_ver(lpad('"+pDigitoVer+"',6,'0')) valorActual from dual";     
          stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
          rsQuery = stQuery.executeQuery(SQL2);
          //  secuencia para poliza_id
          while (rsQuery.next()){
             resDigito = rsQuery.getString("valoractual");
          } //del while
        } //Fin try
        catch(Exception e){
          System.out.println("Ocurrio un error al accesar al metodo select_digito_verificador "+e.getMessage());
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
        return resDigito;
     } //fin Select 

  
   /** 
   * Metodo que lee la informacion de rf_tr_cheques 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void select_rf_tr_cheques(Connection con, String clave)throws SQLException, Exception
   { 
      Statement stQuery=null; 
      ResultSet rsQuery=null; 
      StringBuffer SQL = new StringBuffer();
      try
      { 
          
        SQL=new StringBuffer("SELECT a.cheque_id,a.cuenta_cheques_id,a.consecutivo,a.abreviatura,to_char(a.fecha,'dd/mm/yyyy hh24:mi:ss') fecha,"
                + "a.importe,a.referencia,a.poliza_id, to_char(a.fecha_impresion,'dd/mm/yyyy hh24:mi:ss') fecha_impresion, estatus,"
                + " to_char(a.fecha_ult_react,'dd/mm/yyyy hh24:mi:ss') fecha_ult_react, beneficiario, numEmpleado, "
                + "digitoVerificador ,to_char(a.fechaCheque,'dd/mm/yyyy') fechaCheque, a.operacion_pago");  
        SQL.append(" FROM rf_tr_cheques a "); 
        SQL.append(" WHERE a.cheque_id='").append(clave).append("'"); 
         System.out.println(SQL.toString());       

        stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
        rsQuery=stQuery.executeQuery(SQL.toString()); 
        
        while (rsQuery.next())
        { 
           cheque_id=(rsQuery.getString("cheque_id")==null) ? "" : rsQuery.getString("cheque_id"); 
           cuenta_cheques_id=(rsQuery.getString("cuenta_cheques_id")==null) ? "" : rsQuery.getString("cuenta_cheques_id"); 
           consecutivo=(rsQuery.getString("consecutivo")==null) ? "" : rsQuery.getString("consecutivo"); 
           abreviatura=(rsQuery.getString("abreviatura")==null) ? "" : rsQuery.getString("abreviatura"); 
           fecha=(rsQuery.getString("fecha")==null) ? "" : rsQuery.getString("fecha"); 
           importe=(rsQuery.getString("importe")==null) ? "" : rsQuery.getString("importe"); 
           referencia=(rsQuery.getString("referencia")==null) ? "" : rsQuery.getString("referencia"); 
           poliza_id=(rsQuery.getString("poliza_id")==null) ? "" : rsQuery.getString("poliza_id"); 
           fecha_impresion=(rsQuery.getString("fecha_impresion")==null) ? "" : rsQuery.getString("fecha_impresion");           
           estatus=(rsQuery.getString("estatus")==null) ? "" : rsQuery.getString("estatus"); 
           fecha_ult_react=(rsQuery.getString("fecha_ult_react")==null) ? "" : rsQuery.getString("fecha_ult_react"); 
           beneficiario=(rsQuery.getString("beneficiario")==null) ? "" : rsQuery.getString("beneficiario"); 
           numEmpleado=(rsQuery.getString("numEmpleado")==null) ? "" : rsQuery.getString("numEmpleado");            
           digitoVerificador=(rsQuery.getString("digitoVerificador")==null) ? "" : rsQuery.getString("digitoVerificador");            
           fechaCheque=(rsQuery.getString("fechaCheque")==null) ? "" : rsQuery.getString("fechaCheque");                      
           operacionPago =(rsQuery.getString("operacion_pago")==null) ? "" : rsQuery.getString("operacion_pago");                
           System.out.println("entré!"); 
        } // Fin while 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo select_rf_tr_cheques "+e.getMessage()); 
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
     } //Fin finally 
   } //Fin metodo select_rf_tr_cheques 
   
    /** 
    * Metodo que lee la informacion de rf_tr_cheques 
    * Fecha de creacion: 
    * Autor: 
    * Fecha de modificacion: 
    * Modificado por: 
    */ 
    public void select_rf_tr_cheques_poliza(Connection con, String pPolizaId)throws SQLException, Exception{ 
       Statement stQuery=null; 
       ResultSet rsQuery=null; 
       try{ 
        stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
        StringBuffer SQL=new StringBuffer("SELECT a.cheque_id,a.cuenta_cheques_id,a.consecutivo,a.abreviatura,to_char(a.fecha,'dd/mm/yyyy hh24:mi:ss') fecha,a.importe,a.referencia,a.poliza_id,to_char(a.fecha_impresion,'dd/mm/yyyy hh24:mi:ss') fecha_impresion, estatus, to_char(a.fecha_ult_react,'dd/mm/yyyy hh24:mi:ss') fecha_ult_react, beneficiario, numEmpleado, digitoVerificador, to_char(a.fechaCheque,'dd/mm/yyyy') fechaCheque, origen_operacion, operacion_pago, operacion_pago_sup "); 
        SQL.append(" FROM rf_tr_cheques a "); 
        SQL.append(" WHERE a.poliza_id=").append(pPolizaId).append(" "); 
        System.out.println(SQL.toString()); 
        rsQuery=stQuery.executeQuery(SQL.toString()); 
        while (rsQuery.next()){ 
           cheque_id=(rsQuery.getString("cheque_id")==null) ? "" : rsQuery.getString("cheque_id"); 
           cuenta_cheques_id=(rsQuery.getString("cuenta_cheques_id")==null) ? "" : rsQuery.getString("cuenta_cheques_id"); 
           consecutivo=(rsQuery.getString("consecutivo")==null) ? "" : rsQuery.getString("consecutivo"); 
           abreviatura=(rsQuery.getString("abreviatura")==null) ? "" : rsQuery.getString("abreviatura"); 
           fecha=(rsQuery.getString("fecha")==null) ? "" : rsQuery.getString("fecha"); 
           importe=(rsQuery.getString("importe")==null) ? "" : rsQuery.getString("importe"); 
           referencia=(rsQuery.getString("referencia")==null) ? "" : rsQuery.getString("referencia"); 
           poliza_id=(rsQuery.getString("poliza_id")==null) ? "" : rsQuery.getString("poliza_id"); 
           fecha_impresion=(rsQuery.getString("fecha_impresion")==null) ? "" : rsQuery.getString("fecha_impresion");           
           estatus=(rsQuery.getString("estatus")==null) ? "" : rsQuery.getString("estatus"); 
           fecha_ult_react=(rsQuery.getString("fecha_ult_react")==null) ? "" : rsQuery.getString("fecha_ult_react");            
           beneficiario=(rsQuery.getString("beneficiario")==null) ? "" : rsQuery.getString("beneficiario"); 
           numEmpleado=(rsQuery.getString("numEmpleado")==null) ? "" : rsQuery.getString("numEmpleado");                       
           digitoVerificador=(rsQuery.getString("digitoVerificador")==null) ? "" : rsQuery.getString("digitoVerificador");                      
           fechaCheque=(rsQuery.getString("fechaCheque")==null) ? "" : rsQuery.getString("fechaCheque");                       
           origenDocto=(rsQuery.getString("origen_operacion")==null) ? "" : rsQuery.getString("origen_operacion");                       
           cxl=(rsQuery.getString("operacion_pago")==null) ? "" : rsQuery.getString("operacion_pago");   
           cxlSup = (rsQuery.getString("operacion_pago_sup")==null) ? "" : rsQuery.getString("operacion_pago_sup");   
        } // Fin while 
      } //Fin try 
      catch(Exception e){ 
        System.out.println("Ocurrio un error al accesar al metodo select_rf_tr_cheques_poliza "+e.getMessage()); 
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
    } //Fin metodo select_rf_tr_cheques_poliza 
       
  
   /** 
   * Metodo que inserta la informacion de rf_tr_cheques 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void insert_rf_tr_cheques(Connection con) throws SQLException, Exception{ 
      Statement stQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("INSERT INTO rf_tr_cheques( cheque_id,cuenta_cheques_id,consecutivo,abreviatura,fecha,importe,referencia,poliza_id, fecha_impresion, estatus, fecha_ult_react, beneficiario, numEmpleado, digitoVerificador, fechaCheque, origen_operacion, operacion_pago, operacion_pago_sup) ");  
       SQL.append("VALUES("); 
       SQL.append(cheque_id).append(","); 
       SQL.append(cuenta_cheques_id).append(","); 
       SQL.append(consecutivo).append(","); 
       SQL.append("'").append(abreviatura).append("',"); 
       SQL.append(" sysdate,"); 
       SQL.append(importe).append(","); 
       SQL.append("'").append(referencia).append("',"); 
       SQL.append(poliza_id).append(","); 
       SQL.append(" null,");        
       SQL.append("0,"); 
       SQL.append(" null,");        
       SQL.append("'").append(beneficiario).append("',");       
       SQL.append(numEmpleado).append(",");        
       SQL.append(digitoVerificador).append(",");
       SQL.append("to_date('").append(fechaCheque).append("','dd/mm/yyyy'),"); 
       SQL.append("null,");
       //SQL.append(getOrigenDocto()).append(",");
       SQL.append("'").append(getCxl()).append("',");
       SQL.append("'").append(getCxlSup()).append("')");
       System.out.println(SQL.toString()); 
       int rs=-1; 
       System.out.println("origendocto: " + origenDocto);
       System.out.println("SQL insert_rf_tr_cheques: " + SQL);
       rs=stQuery.executeUpdate(SQL.toString()); 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo insert_rf_tr_cheques "+e.getMessage()); 
       throw e; 
     } //Fin catch 
     finally{ 
       if (stQuery!=null){ 
         stQuery.close(); 
         stQuery=null; 
       } 
     } //Fin finally 
   } //Fin metodo insert_rf_tr_cheques 
  
   /** 
   * Metodo que modifica la informacion de rf_tr_cheques 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void update_rf_tr_cheques(Connection con, String clave)throws SQLException, Exception{ 
      Statement stQuery=null; 
      StringBuffer SQL=new StringBuffer("UPDATE rf_tr_cheques"); 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       SQL.append(" SET cheque_id=").append(cheque_id).append(","); 
       SQL.append("cuenta_cheques_id=").append(cuenta_cheques_id).append(","); 
       SQL.append("consecutivo=").append(consecutivo).append(","); 
       SQL.append("abreviatura=").append("'").append(abreviatura).append("',"); 
       SQL.append("fecha=to_date('").append(fecha).append("','dd/mm/yyyy hh24:mi:ss'),"); 
       SQL.append("importe=").append(importe).append(","); 
       SQL.append("referencia=").append("'").append(referencia).append("',"); 
       SQL.append("poliza_id=").append(poliza_id).append(","); 
       SQL.append("fecha_impresion=to_date('").append(fecha_impresion).append("','dd/mm/yyyy hh24:mi:ss'),"); 
       SQL.append("estatus=").append(estatus).append(","); 
       SQL.append("fecha_ult_react=to_date('").append(fecha_ult_react).append("','dd/mm/yyyy hh24:mi:ss'),");        
       SQL.append("beneficiario=").append("'").append(beneficiario).append("',"); 
       SQL.append("numEmpleado=").append(numEmpleado).append(",");        
       SQL.append("digitoVerificador=").append(digitoVerificador).append(",");        
       SQL.append("fechaCheque=to_date('").append(fechaCheque).append("','dd/mm/yyyy')");        
       SQL.append(" WHERE cheque_id='").append(clave).append("'"); 
       // System.out.println(SQL.toString());  
       int rs=-1; 
       rs=stQuery.executeUpdate(SQL.toString()); 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo update_rf_tr_cheques "+e.getMessage()); 
       System.out.println(SQL.toString());  
       throw e; 
     } //Fin catch 
     finally{ 
       if (stQuery!=null){ 
         stQuery.close(); 
         stQuery=null; 
       } 
     } //Fin finally 
   } //Fin metodo update_rf_tr_cheques 
  
    /**
     * Metodo que verifica que el consecutivo no este duplicado
     * Fecha de creacion:15/10/2013
     * Autor: Claudia Torres Macario
     * Fecha de modificacion:
     * Modificado por:
     */
     
     public String select_consecutivo(Connection con, String cuentaChequesId, String consecutivo, String ejercicio)throws SQLException, Exception{
        Statement stQuery=null;
        ResultSet rsQuery= null;
        String regresa = "-1";
        try{
          String SQL2 = "select c.consecutivo from rf_tr_cheques c where c.cuenta_cheques_id="+cuentaChequesId+" and c.consecutivo ="+consecutivo+" and extract(year from c.fecha)="+ejercicio ;     
          stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
          rsQuery = stQuery.executeQuery(SQL2);
          while (rsQuery.next()){
             regresa = rsQuery.getString("consecutivo");
          } //del while
        } //Fin try
        catch(Exception e){
          System.out.println("Ocurrio un error al accesar al metodo select_consecutivo "+e.getMessage());
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
        return regresa;
     } //fin Select 
   
           /** 
      * Metodo que modifica la informacion de rf_tr_cheques 
      * Fecha de creacion: 30/Enero/2014
      * Autor: Yadhira
      * Fecha de modificacion: 
      * Modificado por: 
      */ 
      public void update_rf_tr_cheques_reexpedir(Connection con, String pPolizaId)throws SQLException, Exception{ 
         Statement stQuery=null; 
         StringBuffer SQL=new StringBuffer("UPDATE rf_tr_cheques"); 
         SQL.append(" SET reexpedir=1"); 
         SQL.append(" WHERE poliza_id=").append(pPolizaId).append(" ");        
         try{ 
          stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
          int rs=-1; 
          rs=stQuery.executeUpdate(SQL.toString()); 
        } //Fin try 
        catch(Exception e){ 
          System.out.println("Ocurrio un error al accesar al metodo update_rf_tr_cheques_reexpedir "+e.getMessage()); 
          System.out.println(SQL.toString());  
          throw e; 
        } //Fin catch 
        finally{ 
          if (stQuery!=null){ 
            stQuery.close(); 
            stQuery=null; 
          } 
        } //Fin finally 
      } //Fin metodo uupdate_rf_tr_cheques_reexpedir

             /** 
       * Metodo que modifica la informacion de rf_tr_cheques 
       * Fecha de creacion: 30/Enero/2014
       * Autor: Yadhira
       * Fecha de modificacion: 
       * Modificado por: 
       */ 
       public void update_rf_presupuesto_s2_rf_tr_cheque_nominativo(Connection con, String idChequeConta)throws SQLException, Exception{ 
          Statement stQuery=null; 
          StringBuffer SQL=new StringBuffer("update sapfin_pa.rf_tr_cheque_nominativo cn"); 
          SQL.append(" set cn.num_cheque = null, cn.id_cheque_conta=null"); 
          SQL.append(" where cn.id_cheque_conta in (").append(idChequeConta).append(")");        
          try{ 
           stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
           int rs=-1; 
           System.out.println(SQL.toString());
           rs=stQuery.executeUpdate(SQL.toString()); 
         } //Fin try 
         catch(Exception e){ 
           System.out.println("Ocurrio un error al accesar al metodo update_rf_tr_cheques_reexpedir "+e.getMessage()); 
           System.out.println(SQL.toString());  
           throw e; 
         } //Fin catch 
         finally{ 
           if (stQuery!=null){ 
             stQuery.close(); 
             stQuery=null; 
           } 
         } //Fin finally 
       } //Fin metodo uupdate_rf_tr_cheques_reexpedir

          /** 
    * Metodo que modifica la informacion de rf_tr_cheques 
    * Fecha de creacion: 30/Enero/2014
    * Autor: Yadhira
    * Fecha de modificacion: 
    * Modificado por: 
    */ 
    public void update_rf_tr_operaciones_cheques(Connection con, String idChequeConta)throws SQLException, Exception{ 
       Statement stQuery = null; 
       StringBuffer SQL=new StringBuffer("update sapfin_pa.rf_tr_operaciones_cheques cn"); 
       SQL.append(" set cn.num_cheque = null, cn.id_cheque_conta=null"); 
       SQL.append(" where cn.id_cheque_conta in (").append(idChequeConta).append(")");        
       try{ 
        stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
        int rs=-1; 
        rs=stQuery.executeUpdate(SQL.toString()); 
      } //Fin try 
      catch(Exception e){ 
        System.out.println("Ocurrio un error al accesar al metodo update_rf_tr_cheques_reexpedir "+e.getMessage()); 
        System.out.println(SQL.toString());   
        throw e; 
      } //Fin catch 
      finally{ 
        if (stQuery!=null){ 
          stQuery.close(); 
          stQuery=null; 
        } 
      } //Fin finally 
    } //Fin metodo uupdate_rf_tr_cheques_reexpedir

      
   /** 
   * Metodo que borra la informacion de rf_tr_cheques 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void delete_rf_tr_cheques(Connection con,  String pPolizaId)throws SQLException, Exception{ 
      Statement stQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("DELETE FROM rf_tr_cheques a "); 
       SQL.append("WHERE a.poliza_id='").append(pPolizaId).append("'"); 
       System.out.println(SQL.toString()); 
       int rs=-1; 
       rs=stQuery.executeUpdate(SQL.toString()); 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo delete_rf_tr_cheques "+e.getMessage()); 
       throw e; 
     } //Fin catch 
     finally{ 
       if (stQuery!=null){ 
         stQuery.close(); 
         stQuery=null; 
       } 
     } //Fin finally 
   } //Fin metodo delete_rf_tr_cheques 

    /** 
    * Metodo que modifica la informacion de rf_tr_cheques 
    * Fecha de creacion: 
    * Autor: 
    * Fecha de modificacion: 
    * Modificado por: 
    */ 
    public void update_rf_tr_cheques_estatus(Connection con, String pPolizaId, String pEstatus)throws SQLException, Exception{ 
       Statement stQuery=null; 
       StringBuffer SQL=new StringBuffer("UPDATE rf_tr_cheques"); 
       SQL.append(" SET estatus=").append(pEstatus).append(" "); 
       SQL.append(" WHERE poliza_id=").append(pPolizaId).append(" ");        
       try{ 
        stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 

        // System.out.println(SQL.toString());  
        int rs=-1; 
        rs=stQuery.executeUpdate(SQL.toString()); 
      } //Fin try 
      catch(Exception e){ 
        System.out.println("Ocurrio un error al accesar al metodo update_rf_tr_cheques_estatus "+e.getMessage()); 
        System.out.println(SQL.toString());  
        throw e; 
      } //Fin catch 
      finally{ 
        if (stQuery!=null){ 
          stQuery.close(); 
          stQuery=null; 
        } 
      } //Fin finally 
    } //Fin metodo update_rf_tr_cheques_estatus 

    /** 
    * Metodo que modifica la informacion de rf_tr_chequesm 
    * Fecha de creacion: 
    * Autor: Alvaro Vargas
    * Fecha de modificacion: 
    * Modificado por: 
    */ 
    public void update_rf_tr_chequesm_estatus(Connection con, String pChequeId, String pEstatus)throws SQLException, Exception{ 
       Statement stQuery=null; 
       StringBuffer SQL=new StringBuffer("UPDATE rf_tr_chequesm"); 
       SQL.append(" SET estatus=").append(pEstatus).append(" "); 
       SQL.append(" WHERE cheque_id=").append(pChequeId).append(" ");        
       try{ 
        stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 

//         System.out.println(SQL.toString());  
        int rs=-1; 
        rs=stQuery.executeUpdate(SQL.toString()); 
      } //Fin try 
      catch(Exception e){ 
        System.out.println("Ocurrio un error al accesar al metodo update_rf_tr_cheques_estatus "+e.getMessage()); 
        System.out.println(SQL.toString());  
        throw e; 
      } //Fin catch 
      finally{ 
        if (stQuery!=null){ 
          stQuery.close(); 
          stQuery=null; 
        } 
      } //Fin finally 
    } //Fin metodo update_rf_tr_cheques_estatus 
    
    
    /** 
    * Metodo que modifica la informacion de rf_tr_cheques 
    * Fecha de creacion: 
    * Autor: 
    * Fecha de modificacion: 
    * Modificado por: 
    */ 
    public void update_rf_tr_chequesARE_estatus(Connection con, String pPolizaId, String pEstatus)throws SQLException, Exception{ 
       Statement stQuery=null; 
       StringBuffer SQL=new StringBuffer("UPDATE rf_tr_cheques"); 
       SQL.append(" SET estatus=").append(pEstatus).append(" , cancelacion_are=1 "); 
       SQL.append(" WHERE cheque_id=").append(pPolizaId).append(" ");        
       try{ 
        stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 

         System.out.println(SQL.toString());  
        int rs=-1; 
        rs=stQuery.executeUpdate(SQL.toString()); 
      } //Fin try 
      catch(Exception e){ 
        System.out.println("Ocurrio un error al accesar al metodo update_rf_tr_chequesARE_estatus "+e.getMessage()); 
        System.out.println(SQL.toString());  
        throw e; 
      } //Fin catch 
      finally{ 
        if (stQuery!=null){ 
          stQuery.close(); 
          stQuery=null; 
        } 
      } //Fin finally 
    } //Fin metodo update_rf_tr_cheques_estatus 

    
    
 
} //Fin clase bcRf_tr_cheques 
