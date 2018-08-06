package sia.rf.contabilidad.registroContableNuevo;

import java.sql.*;

import sia.db.dao.DaoFactory;

import sun.jdbc.rowset.CachedRowSet;

public class bcCuentasCheques{ 
   public bcCuentasCheques(){ 
   } 
   private String cuenta_cheques_id; 
   private String id_cuenta; 
   private String consecutivo; 
   private String abreviatura; 
   private String maestro_operacion_id; 
   private String fecha_vig_ini; 
   private String fecha_vig_fin; 
   private String estatus_cheque_id; 
   private String unidad_ejecutora; 
   private String ambito; 
   private String pais; 
   private String entidad; 
  
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
   * id_cuenta 
   * @return id_cuenta 
   */ 
   public String getId_cuenta() { 
      return id_cuenta; 
   } 
  
   /** 
   * id_cuenta 
   * @param id_cuenta 
   */ 
   public void setId_cuenta( String id_cuenta ) { 
      this.id_cuenta=id_cuenta; 
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
   * maestro_operacion_id 
   * @return maestro_operacion_id 
   */ 
   public String getMaestro_operacion_id() { 
      return maestro_operacion_id; 
   } 
  
   /** 
   * maestro_operacion_id 
   * @param maestro_operacion_id 
   */ 
   public void setMaestro_operacion_id( String maestro_operacion_id ) { 
      this.maestro_operacion_id=maestro_operacion_id; 
   } 
  
   /** 
   * fecha_vig_ini 
   * @return fecha_vig_ini 
   */ 
   public String getFecha_vig_ini() { 
      return fecha_vig_ini; 
   } 
  
   /** 
   * fecha_vig_ini 
   * @param fecha_vig_ini 
   */ 
   public void setFecha_vig_ini( String fecha_vig_ini ) { 
      this.fecha_vig_ini=fecha_vig_ini; 
   } 
  
   /** 
   * fecha_vig_fin 
   * @return fecha_vig_fin 
   */ 
   public String getFecha_vig_fin() { 
      return fecha_vig_fin; 
   } 
  
   /** 
   * fecha_vig_fin 
   * @param fecha_vig_fin 
   */ 
   public void setFecha_vig_fin( String fecha_vig_fin ) { 
      this.fecha_vig_fin=fecha_vig_fin; 
   } 
  
   /** 
   * estatus_cheque_id 
   * @return estatus_cheque_id 
   */ 
   public String getEstatus_cheque_id() { 
      return estatus_cheque_id; 
   } 
  
   /** 
   * estatus_cheque_id 
   * @param estatus_cheque_id 
   */ 
   public void setEstatus_cheque_id( String estatus_cheque_id ) { 
      this.estatus_cheque_id=estatus_cheque_id; 
   } 
  
   /** 
   * unidad_ejecutora 
   * @return unidad_ejecutora 
   */ 
   public String getUnidad_ejecutora() { 
      return unidad_ejecutora; 
   } 
  
   /** 
   * unidad_ejecutora 
   * @param unidad_ejecutora 
   */ 
   public void setUnidad_ejecutora( String unidad_ejecutora ) { 
      this.unidad_ejecutora=unidad_ejecutora; 
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
   * pais 
   * @return pais 
   */ 
   public String getPais() { 
      return pais; 
   } 
  
   /** 
   * pais 
   * @param pais 
   */ 
   public void setPais( String pais ) { 
      this.pais=pais; 
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
   * Metodo que lee la informacion de rf_tc_cuentas_cheques 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void select_rf_tc_cuentas_cheques(Connection con, String pCuentaChequesId)throws SQLException, Exception{ 
      Statement stQuery=null; 
      ResultSet rsQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("SELECT a.cuenta_cheques_id,a.id_cuenta,a.consecutivo,a.abreviatura,a.maestro_operacion_id,a.fecha_vig_ini,a.fecha_vig_fin,a.estatus_cheque_id,a.unidad_ejecutora,a.ambito,a.pais,a.entidad");  
       SQL.append(" FROM rf_tc_cuentas_cheques a "); 
       SQL.append(" WHERE a.cuenta_cheques_id=").append(pCuentaChequesId).append(" "); 
       System.out.println(SQL.toString()); 
       rsQuery=stQuery.executeQuery(SQL.toString()); 
       while (rsQuery.next()){ 
          cuenta_cheques_id=(rsQuery.getString("cuenta_cheques_id")==null) ? "" : rsQuery.getString("cuenta_cheques_id"); 
          id_cuenta=(rsQuery.getString("id_cuenta")==null) ? "" : rsQuery.getString("id_cuenta"); 
          consecutivo=(rsQuery.getString("consecutivo")==null) ? "" : rsQuery.getString("consecutivo"); 
          abreviatura=(rsQuery.getString("abreviatura")==null) ? "" : rsQuery.getString("abreviatura"); 
          maestro_operacion_id=(rsQuery.getString("maestro_operacion_id")==null) ? "" : rsQuery.getString("maestro_operacion_id"); 
          fecha_vig_ini=(rsQuery.getString("fecha_vig_ini")==null) ? "" : rsQuery.getString("fecha_vig_ini"); 
          fecha_vig_fin=(rsQuery.getString("fecha_vig_fin")==null) ? "" : rsQuery.getString("fecha_vig_fin"); 
          estatus_cheque_id=(rsQuery.getString("estatus_cheque_id")==null) ? "" : rsQuery.getString("estatus_cheque_id"); 
          unidad_ejecutora=(rsQuery.getString("unidad_ejecutora")==null) ? "" : rsQuery.getString("unidad_ejecutora"); 
          ambito=(rsQuery.getString("ambito")==null) ? "" : rsQuery.getString("ambito"); 
          pais=(rsQuery.getString("pais")==null) ? "" : rsQuery.getString("pais"); 
          entidad=(rsQuery.getString("entidad")==null) ? "" : rsQuery.getString("entidad"); 
       } // Fin while 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo select_rf_tc_cuentas_cheques "+e.getMessage()); 
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
   } //Fin metodo select_rf_tc_cuentas_cheques 
  
   /** 
   * Metodo que inserta la informacion de rf_tc_cuentas_cheques 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void insert_rf_tc_cuentas_cheques(Connection con)throws SQLException, Exception{ 
      Statement stQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("INSERT INTO rf_tc_cuentas_cheques( cuenta_cheques_id,id_cuenta,consecutivo,abreviatura,maestro_operacion_id,fecha_vig_ini,fecha_vig_fin,estatus_cheque_id,unidad_ejecutora,ambito,pais,entidad) ");  
       SQL.append("VALUES("); 
       SQL.append(cuenta_cheques_id).append(","); 
       SQL.append(id_cuenta).append(","); 
       SQL.append(consecutivo).append(","); 
       SQL.append("'").append(abreviatura).append("',"); 
       SQL.append(maestro_operacion_id).append(","); 
       SQL.append("to_date('").append(fecha_vig_ini).append("','dd/mm/yyyy'),"); 
       SQL.append("to_date('").append(fecha_vig_fin).append("','dd/mm/yyyy'),"); 
       SQL.append(estatus_cheque_id).append(","); 
       SQL.append("'").append(unidad_ejecutora).append("',"); 
       SQL.append("'").append(ambito).append("',"); 
       SQL.append("'").append(pais).append("',"); 
       SQL.append(entidad).append(")"); 
       System.out.println(SQL.toString()); 
       int rs=-1; 
       rs=stQuery.executeUpdate(SQL.toString()); 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo insert_rf_tc_cuentas_cheques "+e.getMessage()); 
       throw e; 
     } //Fin catch 
     finally{ 
       if (stQuery!=null){ 
         stQuery.close(); 
         stQuery=null; 
       } 
     } //Fin finally 
   } //Fin metodo insert_rf_tc_cuentas_cheques 
  
   /** 
   * Metodo que modifica la informacion de rf_tc_cuentas_cheques 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void update_rf_tc_cuentas_cheques(Connection con,  String clave)throws SQLException, Exception{ 
      Statement stQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("UPDATE rf_tc_cuentas_cheques"); 
       SQL.append(" SET cuenta_cheques_id=").append(cuenta_cheques_id).append(","); 
       SQL.append("id_cuenta=").append(id_cuenta).append(","); 
       SQL.append("consecutivo=").append(consecutivo).append(","); 
       SQL.append("abreviatura=").append("'").append(abreviatura).append("',"); 
       SQL.append("maestro_operacion_id=").append(maestro_operacion_id).append(","); 
       SQL.append("fecha_vig_ini=to_date('").append(fecha_vig_ini).append("','dd/mm/yyyy'),"); 
       SQL.append("fecha_vig_fin=to_date('").append(fecha_vig_fin).append("','dd/mm/yyyy'),"); 
       SQL.append("estatus_cheque_id=").append(estatus_cheque_id).append(","); 
       SQL.append("unidad_ejecutora=").append("'").append(unidad_ejecutora).append("',"); 
       SQL.append("ambito=").append("'").append(ambito).append("',"); 
       SQL.append("pais=").append("'").append(pais).append("',"); 
       SQL.append("entidad=").append(entidad); 
       SQL.append(" WHERE LLAVE='").append(clave).append("'"); 
       System.out.println(SQL.toString()); 
       int rs=-1; 
       rs=stQuery.executeUpdate(SQL.toString()); 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo update_rf_tc_cuentas_cheques "+e.getMessage()); 
       throw e; 
     } //Fin catch 
     finally{ 
       if (stQuery!=null){ 
         stQuery.close(); 
         stQuery=null; 
       } 
     } //Fin finally 
   } //Fin metodo update_rf_tc_cuentas_cheques 
  
   /** 
   * Metodo que borra la informacion de rf_tc_cuentas_cheques 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void delete_rf_tc_cuentas_cheques(Connection con, String clave)throws SQLException, Exception{ 
      Statement stQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("DELETE FROM rf_tc_cuentas_cheques a "); 
       SQL.append("WHERE a.LLAVE='").append(clave).append("'"); 
       System.out.println(SQL.toString()); 
       int rs=-1; 
       rs=stQuery.executeUpdate(SQL.toString()); 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo delete_rf_tc_cuentas_cheques "+e.getMessage()); 
       throw e; 
     } //Fin catch 
     finally{ 
       if (stQuery!=null){ 
         stQuery.close(); 
         stQuery=null; 
       } 
     } //Fin finally 
   } //Fin metodo delete_rf_tc_cuentas_cheques 
    /** 
    * Metodo que modifica la informacion de rf_tc_cuentas_cheques 
    * Fecha de creacion: 
    * Autor: 
    * Fecha de modificacion: 
    * Modificado por: 
    */ 
    public void update_rf_tc_cuentas_consecutivo(Connection con,  String pCuentaChequeId)throws SQLException, Exception{ 
       Statement stQuery=null; 
       try{ 
        stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
        StringBuffer SQL=new StringBuffer("UPDATE rf_tc_cuentas_cheques"); 
        SQL.append(" SET consecutivo=").append(consecutivo).append(" "); 
        SQL.append(" WHERE cuenta_cheques_id=").append(pCuentaChequeId).append(" "); 
        System.out.println(SQL.toString()); 
        int rs=-1; 
        rs=stQuery.executeUpdate(SQL.toString()); 
      } //Fin try 
      catch(Exception e){ 
        System.out.println("Ocurrio un error al accesar al metodo update_rf_tc_cuentas_consecutivo "+e.getMessage()); 
        throw e; 
      } //Fin catch 
      finally{ 
        if (stQuery!=null){ 
          stQuery.close(); 
          stQuery=null; 
        } 
      } //Fin finally 
    } //Fin metodo update_rf_tc_cuentas_cheques    
    
     /** 
     * Metodo que lee la informacion de rf_tr_ctaxliq, el query viene como parametro
     * Fecha de creacion:
     * Autor:
     * Fecha de modificacion:
     * Modificado por:
     */
     
     public ResultSet select_rf_tr_detalle_Cuentas_Cheques(Connection conexion,String ejercicio, String numCuenta)throws SQLException, Exception{
     //Connection conexion=null;
     //conexion=DaoFactory.getContabilidad();
     Statement stQuery;
     ResultSet rsQuery=null; 
     try{ 
      //rsQuery = new CachedRowSet();
      
      String SQL="SELECT * FROM (SELECT cc.consecutivo,cc.abreviatura,to_char(cc.fecha_vig_ini,'dd/mm/yyyy') fecha_vig_ini,"+
                "to_char(cc.fecha_vig_fin,'dd/mm/yyyy') fecha_vig_fin,"+
                "cb.num_cuenta,"+
                "primeramay(cb.nombre_cta) desc_cuenta,"+
                "primeramay(ec.descripcion) desc_estatus,"+
                "cc.id_cuenta,"+
                "cc.maestro_operacion_id,"+
                "cc.cuenta_cheques_id,"+
                "moper.consecutivo consec_moper,"+
                "(cc.consecutivo + 1) ultimo_consecutivo,"+
                "cc.estatus_cheque_id"+
                " FROM rf_tc_cuentas_cheques cc"+
                " INNER JOIN rf_tc_maestro_operaciones moper ON moper.maestro_operacion_id = cc.maestro_operacion_id"+
                " AND cc.unidad_ejecutora ="+unidad_ejecutora+
                " AND cc.ambito ="+ambito+
                " AND cc.entidad ="+entidad+
                " AND cc.pais =147"+
                " AND "+ejercicio + " between extract(year from cc.fecha_vig_ini) and extract(year from cc.fecha_vig_fin)"+
                //" AND to_char(cc.fecha_vig_ini,'yyyymmdd') <= "+"20090720"+ //CAMBIAR POR LA FECHA ACTUAL
                //" AND to_char(cc.fecha_vig_fin,'yyyymmdd') >= "+"20090720"+
                " INNER JOIN rf_tesoreria.rf_tr_cuentas_bancarias cb ON cc.id_cuenta = cb.id_cuenta"+
               // " AND cc.unidad_ejecutora = cb.unidad_ejecutora"+
               // " AND cc.ambito = cb.ambito"+
               //  " AND cc.entidad = cb.entidad"+
                " AND cb.num_cuenta LIKE '%"+numCuenta+"'"+
                " INNER JOIN rf_tc_estatus_chequeras ec ON cc.estatus_cheque_id = ec.estatus_cheque_id) qrslt WHERE(estatus_cheque_id = 1)";
                System.out.println("chequera "+SQL);
      
       stQuery=conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
      
      System.out.println(SQL.toString()); 
      //rsQuery.setCommand(SQL.toString());
      // rsQuery.execute(conexion);       
       rsQuery=stQuery.executeQuery(SQL.toString()); 
       rsQuery.next();
      /*while (rsQuery.next()){ 
         unidad=(rsQuery.getString("unidad")==null) ? "" : rsQuery.getString("unidad"); 
         mes=(rsQuery.getString("mes")==null) ? "" : rsQuery.getString("mes"); 
         tipoeje=(rsQuery.getString("tipoeje")==null) ? "" : rsQuery.getString("tipoeje"); 
         cxlc=(rsQuery.getString("cxlc")==null) ? "" : rsQuery.getString("cxlc"); 
         anticipo=(rsQuery.getString("anticipo")==null) ? "" : rsQuery.getString("anticipo"); 
         totbruto=(rsQuery.getString("totbruto")==null) ? "" : rsQuery.getString("totbruto"); 
         totneto=(rsQuery.getString("totneto")==null) ? "" : rsQuery.getString("totneto"); 
         concepto=(rsQuery.getString("concepto")==null) ? "" : rsQuery.getString("concepto"); 
         fechaaut=(rsQuery.getString("fechaaut")==null) ? "" : rsQuery.getString("fechaaut"); 
         fechacap=(rsQuery.getString("fechacap")==null) ? "" : rsQuery.getString("fechacap"); 
         ident=(rsQuery.getString("ident")==null) ? "" : rsQuery.getString("ident"); 
         poliza=(rsQuery.getString("poliza")==null) ? "" : rsQuery.getString("poliza"); 
         pagocen=(rsQuery.getString("pagocen")==null) ? "" : rsQuery.getString("pagocen"); 
         ctadisp=(rsQuery.getString("ctadisp")==null) ? "" : rsQuery.getString("ctadisp"); 
         cajachi=(rsQuery.getString("cajachi")==null) ? "" : rsQuery.getString("cajachi"); 
         oridocto=(rsQuery.getString("oridocto")==null) ? "" : rsQuery.getString("oridocto"); 
         ejercicio=(rsQuery.getString("ejercicio")==null) ? "" : rsQuery.getString("ejercicio"); 
         referencia=(rsQuery.getString("referencia")==null) ? "" : rsQuery.getString("referencia"); 
         idctaxliq=(rsQuery.getString("idctaxliq")==null) ? "" : rsQuery.getString("idctaxliq"); 
      }*/ // Fin while 
     } //Fin try
     catch(Exception e){
      System.out.println("Ocurrio un error al accesar al metodo select_rf_tr_detalle_operaciones "+e.getMessage()); 
      throw e; 
     } //Fin catch
     finally{
     //if (conexion!=null){
     //  conexion.close(); 
     //  conexion=null; 
     //} 

     /*  if (rsQuery!=null){
        rsQuery.close(); 
        rsQuery=null; 
      } 
      if (stQuery!=null){
        stQuery.close(); 
        stQuery=null; 
      }
     */
      
     } //Fin finally
     return rsQuery;
     } //Fin metodo select_rf_tr_detalle_operaciones
     

     /*Prueba de overrride*/
          public ResultSet select_rf_tr_detalle_Cuentas_Cheques(Connection conexion,String ejercicio, String numCuenta, String origen)throws SQLException, Exception{
     //Connection conexion=null;
     //conexion=DaoFactory.getContabilidad();
     Statement stQuery;
     ResultSet rsQuery=null; 
     try{ 
      //rsQuery = new CachedRowSet();
      
      String SQL="SELECT * FROM (SELECT cc.consecutivo,cc.abreviatura,to_char(cc.fecha_vig_ini,'dd/mm/yyyy') fecha_vig_ini,"+
                "to_char(cc.fecha_vig_fin,'dd/mm/yyyy') fecha_vig_fin,"+
                "cb.num_cuenta,"+
                "primeramay(cb.nombre_cta) desc_cuenta,"+
                "primeramay(ec.descripcion) desc_estatus,"+
                "cc.id_cuenta,"+
                "cc.maestro_operacion_id,"+
                "cc.cuenta_cheques_id,"+
                "moper.consecutivo consec_moper,"+
                "(cc.consecutivo + 1) ultimo_consecutivo,"+
                "cc.estatus_cheque_id"+
                " FROM rf_tc_cuentas_cheques cc"+
                " INNER JOIN rf_tc_maestro_operaciones moper ON moper.maestro_operacion_id = cc.maestro_operacion_id"+
                " AND cc.unidad_ejecutora ="+unidad_ejecutora+
                " AND cc.ambito ="+ambito+
                " AND cc.entidad ="+entidad+
                " AND cc.pais =147"+
                " AND "+ejercicio + " between extract(year from cc.fecha_vig_ini) and extract(year from cc.fecha_vig_fin)"+
                //" AND to_char(cc.fecha_vig_ini,'yyyymmdd') <= "+"20090720"+ //CAMBIAR POR LA FECHA ACTUAL
                //" AND to_char(cc.fecha_vig_fin,'yyyymmdd') >= "+"20090720"+
                " INNER JOIN rf_tesoreria.rf_tr_cuentas_bancarias cb ON cc.id_cuenta = cb.id_cuenta"+
               // " AND cc.unidad_ejecutora = cb.unidad_ejecutora"+
               // " AND cc.ambito = cb.ambito"+
               //  " AND cc.entidad = cb.entidad"+
                " AND cb.num_cuenta LIKE '%"+numCuenta+"'"+
                " and cc.cuenta_cheques_id=   (SELECT CUENTA_CHEQUES_ID FROM rf_tc_cuentas_cheques "
              + " WHERE TIPO_FORMATO = 'U' AND " + ejercicio +
           " between extract(year from fecha_vig_ini) and extract(year from fecha_vig_fin) AND id_cuenta = cc.id_cuenta )  "+              
                " INNER JOIN rf_tc_estatus_chequeras ec ON cc.estatus_cheque_id = ec.estatus_cheque_id) qrslt WHERE(estatus_cheque_id = 1)";
                System.out.println("chequera "+SQL);
      
       stQuery=conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
      
      System.out.println(SQL.toString()); 
      //rsQuery.setCommand(SQL.toString());
      // rsQuery.execute(conexion);       
       rsQuery=stQuery.executeQuery(SQL.toString()); 
       rsQuery.next();
      /*while (rsQuery.next()){ 
         unidad=(rsQuery.getString("unidad")==null) ? "" : rsQuery.getString("unidad"); 
         mes=(rsQuery.getString("mes")==null) ? "" : rsQuery.getString("mes"); 
         tipoeje=(rsQuery.getString("tipoeje")==null) ? "" : rsQuery.getString("tipoeje"); 
         cxlc=(rsQuery.getString("cxlc")==null) ? "" : rsQuery.getString("cxlc"); 
         anticipo=(rsQuery.getString("anticipo")==null) ? "" : rsQuery.getString("anticipo"); 
         totbruto=(rsQuery.getString("totbruto")==null) ? "" : rsQuery.getString("totbruto"); 
         totneto=(rsQuery.getString("totneto")==null) ? "" : rsQuery.getString("totneto"); 
         concepto=(rsQuery.getString("concepto")==null) ? "" : rsQuery.getString("concepto"); 
         fechaaut=(rsQuery.getString("fechaaut")==null) ? "" : rsQuery.getString("fechaaut"); 
         fechacap=(rsQuery.getString("fechacap")==null) ? "" : rsQuery.getString("fechacap"); 
         ident=(rsQuery.getString("ident")==null) ? "" : rsQuery.getString("ident"); 
         poliza=(rsQuery.getString("poliza")==null) ? "" : rsQuery.getString("poliza"); 
         pagocen=(rsQuery.getString("pagocen")==null) ? "" : rsQuery.getString("pagocen"); 
         ctadisp=(rsQuery.getString("ctadisp")==null) ? "" : rsQuery.getString("ctadisp"); 
         cajachi=(rsQuery.getString("cajachi")==null) ? "" : rsQuery.getString("cajachi"); 
         oridocto=(rsQuery.getString("oridocto")==null) ? "" : rsQuery.getString("oridocto"); 
         ejercicio=(rsQuery.getString("ejercicio")==null) ? "" : rsQuery.getString("ejercicio"); 
         referencia=(rsQuery.getString("referencia")==null) ? "" : rsQuery.getString("referencia"); 
         idctaxliq=(rsQuery.getString("idctaxliq")==null) ? "" : rsQuery.getString("idctaxliq"); 
      }*/ // Fin while 
     } //Fin try
     catch(Exception e){
      System.out.println("Ocurrio un error al accesar al metodo select_rf_tr_detalle_operaciones "+e.getMessage()); 
      throw e; 
     } //Fin catch
     finally{
     //if (conexion!=null){
     //  conexion.close(); 
     //  conexion=null; 
     //} 

     /*  if (rsQuery!=null){
        rsQuery.close(); 
        rsQuery=null; 
      } 
      if (stQuery!=null){
        stQuery.close(); 
        stQuery=null; 
      }
     */
      
     } //Fin finally
     return rsQuery;
     } //Fin metodo select_rf_tr_detalle_operaciones
     

     
     
      /** 
      * Metodo que modifica la informacion de rf_tc_cuentas_cheques 
      * Fecha de creacion: 
      * Autor: 
      * Fecha de modificacion: 
      * Modificado por: 
      */ 
      public void update_rf_tc_cuentas_folioFinal(Connection con,  String pCuentaChequeId, String pFolioFinal)throws SQLException, Exception{ 
         Statement stQuery=null; 
         try{ 
          stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
          StringBuffer SQL=new StringBuffer("UPDATE rf_tc_cuentas_cheques"); 
          SQL.append(" SET folio_final=").append(pFolioFinal).append(" "); 
          SQL.append(" WHERE cuenta_cheques_id=").append(pCuentaChequeId).append(" "); 
          System.out.println(SQL.toString()); 
          int rs=-1; 
          rs=stQuery.executeUpdate(SQL.toString()); 
        } //Fin try 
        catch(Exception e){ 
          System.out.println("Ocurrio un error al accesar al metodoupdate_rf_tc_cuentas_folioFinal "+e.getMessage()); 
          throw e; 
        } //Fin catch 
        finally{ 
          if (stQuery!=null){ 
            stQuery.close(); 
            stQuery=null; 
          } 
        } //Fin finally 
      } //Fin metodo update_rf_tc_cuentas_cheques    

} //Fin clase bcRf_tc_cuentas_cheques 
    
