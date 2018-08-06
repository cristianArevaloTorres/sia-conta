package sia.rf.contabilidad.registroContableNuevo;
import java.sql.*; 

public class bcCuentasBancarias{ 
   public bcCuentasBancarias(){ 
   } 
  
   private String id_cuenta; 
   private String num_cuenta; 
   private String entidad; 
   private String id_banco; 
   private String nombre_cta; 
   private String unidad_ejecutora; 
   private String id_tipo_cta; 
   private String id_tipo_programa; 
   private String operacion_central; 
   private String id_estatus_cta_prog; 
   private String ambito; 
   private String entidadAmbito;
   private String procesar; 
   private String fecha_operacion; 
   private String fecha_fin_operacion; 
   private String cuenta_encr; 
   private String ambito_cuenta;
  
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
   * num_cuenta 
   * @return num_cuenta 
   */ 
   public String getNum_cuenta() { 
      return num_cuenta; 
   } 
  
   /** 
   * num_cuenta 
   * @param num_cuenta 
   */ 
   public void setNum_cuenta( String num_cuenta ) { 
      this.num_cuenta=num_cuenta; 
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
   * id_banco 
   * @return id_banco 
   */ 
   public String getId_banco() { 
      return id_banco; 
   } 
  
   /** 
   * id_banco 
   * @param id_banco 
   */ 
   public void setId_banco( String id_banco ) { 
      this.id_banco=id_banco; 
   } 
  
   /** 
   * nombre_cta 
   * @return nombre_cta 
   */ 
   public String getNombre_cta() { 
      return nombre_cta; 
   } 
  
   /** 
   * nombre_cta 
   * @param nombre_cta 
   */ 
   public void setNombre_cta( String nombre_cta ) { 
      this.nombre_cta=nombre_cta; 
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
   * id_tipo_cta 
   * @return id_tipo_cta 
   */ 
   public String getId_tipo_cta() { 
      return id_tipo_cta; 
   } 
  
   /** 
   * id_tipo_cta 
   * @param id_tipo_cta 
   */ 
   public void setId_tipo_cta( String id_tipo_cta ) { 
      this.id_tipo_cta=id_tipo_cta; 
   } 
  
   /** 
   * id_tipo_programa 
   * @return id_tipo_programa 
   */ 
   public String getId_tipo_programa() { 
      return id_tipo_programa; 
   } 
  
   /** 
   * id_tipo_programa 
   * @param id_tipo_programa 
   */ 
   public void setId_tipo_programa( String id_tipo_programa ) { 
      this.id_tipo_programa=id_tipo_programa; 
   } 
  
   /** 
   * operacion_central 
   * @return operacion_central 
   */ 
   public String getOperacion_central() { 
      return operacion_central; 
   } 
  
   /** 
   * operacion_central 
   * @param operacion_central 
   */ 
   public void setOperacion_central( String operacion_central ) { 
      this.operacion_central=operacion_central; 
   } 
  
   /** 
   * id_estatus_cta_prog 
   * @return id_estatus_cta_prog 
   */ 
   public String getId_estatus_cta_prog() { 
      return id_estatus_cta_prog; 
   } 
  
   /** 
   * id_estatus_cta_prog 
   * @param id_estatus_cta_prog 
   */ 
   public void setId_estatus_cta_prog( String id_estatus_cta_prog ) { 
      this.id_estatus_cta_prog=id_estatus_cta_prog; 
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
   * procesar 
   * @return procesar 
   */ 
   public String getProcesar() { 
      return procesar; 
   } 
  
   /** 
   * procesar 
   * @param procesar 
   */ 
   public void setProcesar( String procesar ) { 
      this.procesar=procesar; 
   } 
  
   /** 
   * fecha_operacion 
   * @return fecha_operacion 
   */ 
   public String getFecha_operacion() { 
      return fecha_operacion; 
   } 
  
   /** 
   * fecha_operacion 
   * @param fecha_operacion 
   */ 
   public void setFecha_operacion( String fecha_operacion ) { 
      this.fecha_operacion=fecha_operacion; 
   } 
  
   /** 
   * fecha_fin_operacion 
   * @return fecha_fin_operacion 
   */ 
   public String getFecha_fin_operacion() { 
      return fecha_fin_operacion; 
   } 
  
   /** 
   * fecha_fin_operacion 
   * @param fecha_fin_operacion 
   */ 
   public void setFecha_fin_operacion( String fecha_fin_operacion ) { 
      this.fecha_fin_operacion=fecha_fin_operacion; 
   } 
  
   /** 
   * cuenta_encr 
   * @return cuenta_encr 
   */ 
   public String getCuenta_encr() { 
      return cuenta_encr; 
   } 
  
   /** 
   * cuenta_encr 
   * @param cuenta_encr 
   */ 
   public void setCuenta_encr( String cuenta_encr ) { 
      this.cuenta_encr=cuenta_encr; 
   } 

    public String getAmbito_cuenta() {
        return ambito_cuenta;
    }

    public void setAmbito_cuenta(String ambito_cuenta) {
        this.ambito_cuenta = ambito_cuenta;
    }

     public void setEntidadAmbito(String entidadAmbito) {
         this.entidadAmbito = entidadAmbito;
    }

    public String getEntidadAmbito() {
         return entidadAmbito;
    } 
   /** 
   * Metodo que lee la informacion de rf_tesoreria_rf_tr_cuentas_bancarias 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void select_rf_tesoreria_rf_tr_cuentas_bancarias(Connection con, String pCuentaId)throws SQLException, Exception{ 
      Statement stQuery=null; 
      ResultSet rsQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("SELECT a.id_cuenta,a.num_cuenta,a.entidad,a.id_banco,a.nombre_cta,a.unidad_ejecutora,a.id_tipo_cta,a.id_tipo_programa,a.operacion_central,a.id_estatus_cta_prog,a.ambito,a.procesar,a.fecha_operacion,a.fecha_fin_operacion,a.cuenta_encr,lpad(a.entidad,3,'0')||a.ambito as entidadAmbito ");  
       SQL.append(" FROM rf_tesoreria.rf_tr_cuentas_bancarias a "); 
       SQL.append(" WHERE a.id_cuenta='").append(pCuentaId).append("' "); 
       //System.out.println(SQL.toString()); 
       rsQuery=stQuery.executeQuery(SQL.toString()); 
       while (rsQuery.next()){ 
          id_cuenta=(rsQuery.getString("id_cuenta")==null) ? "" : rsQuery.getString("id_cuenta"); 
          num_cuenta=(rsQuery.getString("num_cuenta")==null) ? "" : rsQuery.getString("num_cuenta"); 
          entidad=(rsQuery.getString("entidad")==null) ? "" : rsQuery.getString("entidad"); 
          id_banco=(rsQuery.getString("id_banco")==null) ? "" : rsQuery.getString("id_banco"); 
          nombre_cta=(rsQuery.getString("nombre_cta")==null) ? "" : rsQuery.getString("nombre_cta"); 
          unidad_ejecutora=(rsQuery.getString("unidad_ejecutora")==null) ? "" : rsQuery.getString("unidad_ejecutora"); 
          id_tipo_cta=(rsQuery.getString("id_tipo_cta")==null) ? "" : rsQuery.getString("id_tipo_cta"); 
          id_tipo_programa=(rsQuery.getString("id_tipo_programa")==null) ? "" : rsQuery.getString("id_tipo_programa"); 
          operacion_central=(rsQuery.getString("operacion_central")==null) ? "" : rsQuery.getString("operacion_central"); 
          id_estatus_cta_prog=(rsQuery.getString("id_estatus_cta_prog")==null) ? "" : rsQuery.getString("id_estatus_cta_prog"); 
          ambito=(rsQuery.getString("ambito")==null) ? "" : rsQuery.getString("ambito"); 
          procesar=(rsQuery.getString("procesar")==null) ? "" : rsQuery.getString("procesar"); 
          fecha_operacion=(rsQuery.getString("fecha_operacion")==null) ? "" : rsQuery.getString("fecha_operacion"); 
          fecha_fin_operacion=(rsQuery.getString("fecha_fin_operacion")==null) ? "" : rsQuery.getString("fecha_fin_operacion"); 
          cuenta_encr=(rsQuery.getString("cuenta_encr")==null) ? "" : rsQuery.getString("cuenta_encr"); 
          entidadAmbito=(rsQuery.getString("entidadAmbito")==null) ? "" : rsQuery.getString("entidadAmbito"); 
       } // Fin while 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo select_rf_tesoreria_rf_tr_cuentas_bancarias "+e.getMessage()); 
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
   } //Fin metodo select_rf_tesoreria_rf_tr_cuentas_bancarias 

    /** 
    * Metodo que lee la informacion de rf_tesoreria_rf_tr_cuentas_bancarias 
    * Fecha de creacion: 
    * Autor: 
    * Fecha de modificacion: 
    * Modificado por: 
    */ 
    public void select_rf_tesoreria_rf_tr_cuentas_bancarias_tipo(Connection con, String pCuenta, String pBanco)throws SQLException, Exception{ 
       Statement stQuery=null; 
       ResultSet rsQuery=null; 
        StringBuffer SQL=new StringBuffer("");
       try{ 
        stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
        
        if (pCuenta.equals("0161763024")||pCuenta.equals("00161763024")){
            SQL.append(" SELECT a.id_cuenta,a.num_cuenta,a.entidad,a.id_banco,a.nombre_cta,a.unidad_ejecutora,a.id_tipo_cta,a.id_tipo_programa,a.operacion_central,a.id_estatus_cta_prog,a.ambito,a.procesar,a.fecha_operacion,a.fecha_fin_operacion,a.cuenta_encr,lpad(a.entidad,3,'0')||a.ambito as entidadAmbito ");  
            SQL.append(" FROM rf_tesoreria.rf_tr_cuentas_bancarias a "); 
            SQL.append(" WHERE a.num_cuenta='").append(pCuenta).append("' and a.id_banco=3");   
        }else{    
            SQL.append("SELECT a.id_cuenta,a.num_cuenta,a.entidad,a.id_banco,a.nombre_cta,a.unidad_ejecutora,a.id_tipo_cta,a.id_tipo_programa,a.operacion_central,a.id_estatus_cta_prog,a.ambito,a.procesar,a.fecha_operacion,a.fecha_fin_operacion,a.cuenta_encr,lpad(a.entidad,3,'0')||a.ambito as entidadAmbito ");  
            SQL.append(" FROM rf_tesoreria.rf_tr_cuentas_bancarias a "); 
            SQL.append(" WHERE a.num_cuenta='").append(pCuenta.trim()).append("'");// and a.id_banco=").append(pBanco);                         
        }
        //System.out.println("bcCuentasBancarias.select_rf_tesoreria_rf_tr_cuentas_bancarias_tipo.sql "+SQL);
        rsQuery=stQuery.executeQuery(SQL.toString()); 
        while (rsQuery.next()){ 
           id_cuenta=(rsQuery.getString("id_cuenta")==null) ? "" : rsQuery.getString("id_cuenta"); 
           num_cuenta=(rsQuery.getString("num_cuenta")==null) ? "" : rsQuery.getString("num_cuenta"); 
           entidad=(rsQuery.getString("entidad")==null) ? "" : rsQuery.getString("entidad"); 
           id_banco=(rsQuery.getString("id_banco")==null) ? "" : rsQuery.getString("id_banco"); 
           nombre_cta=(rsQuery.getString("nombre_cta")==null) ? "" : rsQuery.getString("nombre_cta"); 
           unidad_ejecutora=(rsQuery.getString("unidad_ejecutora")==null) ? "" : rsQuery.getString("unidad_ejecutora"); 
           id_tipo_cta=(rsQuery.getString("id_tipo_cta")==null) ? "" : rsQuery.getString("id_tipo_cta"); 
           id_tipo_programa=(rsQuery.getString("id_tipo_programa")==null) ? "" : rsQuery.getString("id_tipo_programa"); 
           operacion_central=(rsQuery.getString("operacion_central")==null) ? "" : rsQuery.getString("operacion_central"); 
           id_estatus_cta_prog=(rsQuery.getString("id_estatus_cta_prog")==null) ? "" : rsQuery.getString("id_estatus_cta_prog"); 
           ambito=(rsQuery.getString("ambito")==null) ? "" : rsQuery.getString("ambito"); 
           procesar=(rsQuery.getString("procesar")==null) ? "" : rsQuery.getString("procesar"); 
           fecha_operacion=(rsQuery.getString("fecha_operacion")==null) ? "" : rsQuery.getString("fecha_operacion"); 
           fecha_fin_operacion=(rsQuery.getString("fecha_fin_operacion")==null) ? "" : rsQuery.getString("fecha_fin_operacion"); 
           cuenta_encr=(rsQuery.getString("cuenta_encr")==null) ? "" : rsQuery.getString("cuenta_encr"); 
           entidadAmbito=(rsQuery.getString("entidadAmbito")==null) ? "" : rsQuery.getString("entidadAmbito"); 
        } // Fin while 
       if (id_banco.equals("9")|| pCuenta.equals("0161763024")||pCuenta.equals("00161763024")){ //Si es la cuenta de Bancomer
           ambito_cuenta="5";
       } else{
        if (num_cuenta!=null){ // Si existe la cuenta bancaria           
           if (operacion_central.equals("1")){ // Si es una cuenta centralizada
              if (id_tipo_cta.equals("4")||id_tipo_cta.equals("5")){
                ambito_cuenta="2";  // Si es una pagadora central                
              }
              else
                if (id_tipo_cta.equals("2")) {
                   ambito_cuenta="3";
               }
                else
                if (id_tipo_cta.equals("1")) {
                   ambito_cuenta="4";
               }
                else {
                   ambito_cuenta="0";
               }  //Error no es ninguna cuenta valida
          }else{  // Si es una chequera de las unidades
             if (id_tipo_cta.equals("1")||id_tipo_cta.equals("2")||id_tipo_cta.equals("10")) {
                  ambito_cuenta="4";
              } // Si es la chequera de una de las unidades de gasto corriente o servicios personales
             else if (id_tipo_cta.equals("3")&&(pCuenta.equals("43710149845"))){ 
                 ambito_cuenta="6"; //Concentradora de Egresos             
             }else {
                  ambito_cuenta="0";
              } //Error no es ninguna cuenta valida
              }   
          }else {
               ambito_cuenta="0";
           } //Error no es ninguna cuenta valida
       } 
      } //Fin try 
      catch(Exception e){ 
        System.out.println("Ocurrio un error al accesar al metodo select_rf_tesoreria_rf_tr_cuentas_bancarias "+e.getMessage()); 
        System.out.println("Query:"+SQL.toString()); 
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
    } //Fin metodo select_rf_tesoreria_rf_tr_cuentas_bancarias 
  
   /** 
   * Metodo que inserta la informacion de rf_tesoreria_rf_tr_cuentas_bancarias 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void insert_rf_tesoreria_rf_tr_cuentas_bancarias(Connection con)throws SQLException, Exception{ 
      Statement stQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("INSERT INTO rf_tesoreria.rf_tr_cuentas_bancarias( id_cuenta,num_cuenta,entidad,id_banco,nombre_cta,unidad_ejecutora,id_tipo_cta,id_tipo_programa,operacion_central,id_estatus_cta_prog,ambito,procesar,fecha_operacion,fecha_fin_operacion,cuenta_encr) ");  
       SQL.append("VALUES("); 
       SQL.append(id_cuenta).append(","); 
       SQL.append("'").append(num_cuenta).append("',"); 
       SQL.append(entidad).append(","); 
       SQL.append(id_banco).append(","); 
       SQL.append("'").append(nombre_cta).append("',"); 
       SQL.append("'").append(unidad_ejecutora).append("',"); 
       SQL.append(id_tipo_cta).append(","); 
       SQL.append(id_tipo_programa).append(","); 
       SQL.append(operacion_central).append(","); 
       SQL.append(id_estatus_cta_prog).append(","); 
       SQL.append("'").append(ambito).append("',"); 
       SQL.append(procesar).append(","); 
       SQL.append("to_date('").append(fecha_operacion).append("','dd/mm/yyyy'),"); 
       SQL.append("to_date('").append(fecha_fin_operacion).append("','dd/mm/yyyy'),"); 
       SQL.append("'").append(cuenta_encr).append("')"); 
       System.out.println(SQL.toString()); 
       int rs=-1; 
       rs=stQuery.executeUpdate(SQL.toString()); 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo insert_rf_tesoreria_rf_tr_cuentas_bancarias "+e.getMessage()); 
       throw e; 
     } //Fin catch 
     finally{ 
       if (stQuery!=null){ 
         stQuery.close();
       } 
     } //Fin finally 
   } //Fin metodo insert_rf_tesoreria_rf_tr_cuentas_bancarias 
  
   /** 
   * Metodo que modifica la informacion de rf_tesoreria_rf_tr_cuentas_bancarias 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void update_rf_tesoreria_rf_tr_cuentas_bancarias(Connection con, String clave)throws SQLException, Exception{ 
      Statement stQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("UPDATE rf_tesoreria.rf_tr_cuentas_bancarias"); 
       SQL.append(" SET id_cuenta=").append(id_cuenta).append(","); 
       SQL.append("num_cuenta=").append("'").append(num_cuenta).append("',"); 
       SQL.append("entidad=").append(entidad).append(","); 
       SQL.append("id_banco=").append(id_banco).append(","); 
       SQL.append("nombre_cta=").append("'").append(nombre_cta).append("',"); 
       SQL.append("unidad_ejecutora=").append("'").append(unidad_ejecutora).append("',"); 
       SQL.append("id_tipo_cta=").append(id_tipo_cta).append(","); 
       SQL.append("id_tipo_programa=").append(id_tipo_programa).append(","); 
       SQL.append("operacion_central=").append(operacion_central).append(","); 
       SQL.append("id_estatus_cta_prog=").append(id_estatus_cta_prog).append(","); 
       SQL.append("ambito=").append("'").append(ambito).append("',"); 
       SQL.append("procesar=").append(procesar).append(","); 
       SQL.append("fecha_operacion=to_date('").append(fecha_operacion).append("','dd/mm/yyyy'),"); 
       SQL.append("fecha_fin_operacion=to_date('").append(fecha_fin_operacion).append("','dd/mm/yyyy'),"); 
       SQL.append("cuenta_encr=").append("'").append(cuenta_encr); 
       SQL.append(" WHERE LLAVE='").append(clave).append("'"); 
       System.out.println(SQL.toString()); 
       int rs=-1; 
       rs=stQuery.executeUpdate(SQL.toString()); 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo update_rf_tesoreria_rf_tr_cuentas_bancarias "+e.getMessage()); 
       throw e; 
     } //Fin catch 
     finally{ 
       if (stQuery!=null){ 
         stQuery.close();
       } 
     } //Fin finally 
   } //Fin metodo update_rf_tesoreria_rf_tr_cuentas_bancarias 
  
   /** 
   * Metodo que borra la informacion de rf_tesoreria_rf_tr_cuentas_bancarias 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void delete_rf_tesoreria_rf_tr_cuentas_bancarias(Connection con, String clave)throws SQLException, Exception{ 
      Statement stQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("DELETE FROM rf_tesoreria.rf_tr_cuentas_bancarias a "); 
       SQL.append("WHERE a.LLAVE='").append(clave).append("'"); 
       System.out.println(SQL.toString()); 
       int rs=-1; 
       rs=stQuery.executeUpdate(SQL.toString()); 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo delete_rf_tesoreria_rf_tr_cuentas_bancarias "+e.getMessage()); 
       throw e; 
     } //Fin catch 
     finally{ 
       if (stQuery!=null){ 
         stQuery.close();
       } 
     } //Fin finally 
   } //Fin metodo delete_rf_tesoreria_rf_tr_cuentas_bancarias 

} //Fin clase bcrf_tesoreria_rf_tr_cuentas_bancarias 
    
