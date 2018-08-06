package sia.rf.contabilidad.registroContableNuevo;
 
import java.sql.*;

import sun.jdbc.rowset.CachedRowSet;

public class bcPoliza{

   public bcPoliza(){
   }
   
   private String poliza_id;
   private String unidad_ejecutora;
   private String ambito;
   private String pais;
   private String entidad;
   private String consecutivo;
   private String tipo_poliza_id;
   private String ban_cheque;
   private String maestro_operacion_id;
   private String fecha;
   private String concepto;
   private String referencia;
   private String fecha_afectacion;
   private String clasificacion_poliza_id;
   private String poliza_referencia;
   private String num_empleado;
   private String global;
   private String ejercicio;
   private String mes;
   private String origen;
   private String id_catalogo_cuenta;
 
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
   * tipo_poliza_id
   * @return tipo_poliza_id
   */
   public String getTipo_poliza_id() {
      return tipo_poliza_id;
   }
 
   /**
   * tipo_poliza_id
   * @param tipo_poliza_id
   */
   public void setTipo_poliza_id( String tipo_poliza_id ) {
      this.tipo_poliza_id=tipo_poliza_id;
   }
   
    public String getBan_cheque() {
        return ban_cheque;
    }

    public void setBan_cheque(String ban_cheque) {
        this.ban_cheque = ban_cheque;
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
   * concepto
   * @return concepto
   */
   public String getConcepto() {
      return concepto;
   }
 
   /**
   * concepto
   * @param concepto
   */
   public void setConcepto( String concepto ) {
      this.concepto=concepto;
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
   * clasificacion_poliza_id
   * @return clasificacion_poliza_id
   */
   public String getClasificacion_poliza_id() {
      return clasificacion_poliza_id;
   }
 
   /**
   * clasificacion_poliza_id
   * @param clasificacion_poliza_id
   */
   public void setClasificacion_poliza_id( String clasificacion_poliza_id ) {
      this.clasificacion_poliza_id=clasificacion_poliza_id;
   }
 
   /**
   * poliza_referencia
   * @return poliza_referencia
   */
   public String getPoliza_referencia() {
      return poliza_referencia;
   }
 
   /**
   * poliza_referencia
   * @param poliza_referencia
   */
   public void setPoliza_referencia( String poliza_referencia ) {
      this.poliza_referencia=poliza_referencia;
   }
 
   /**
   * num_empleado
   * @return num_empleado
   */
   public String getNum_empleado() {
      return num_empleado;
   }
 
   /**
   * num_empleado
   * @param num_empleado
   */
   public void setNum_empleado( String num_empleado ) {
      this.num_empleado=num_empleado;
   }
 
   /**
   * global
   * @return global
   */
   public String getGlobal() {
      return global;
   }
 
   /**
   * global
   * @param global
   */
   public void setGlobal( String global ) {
      this.global=global;
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
 
   /**
   * origen
   * @return origen
   */
   public String getOrigen() {
      return origen;
   }
 
   /**
   * origen
   * @param origen
   */
   public void setOrigen( String origen ) {
      this.origen=origen;
   }
 
   /**
   * id_catalogo_cuenta
   * @return id_catalogo_cuenta
   */
   public String getId_catalogo_cuenta() {
      return id_catalogo_cuenta;
   }
 
   /**
   * id_catalogo_cuenta
   * @param id_catalogo_cuenta
   */
   public void setId_catalogo_cuenta( String id_catalogo_cuenta ) {
      this.id_catalogo_cuenta=id_catalogo_cuenta;
   }


    /**
    * Metodo que obtiene si es una fecha futura
    * Fecha de creacion:18/06/2009
    * Autor:Jorge Luis Perez N.
    * Fecha de modificacion:
    * Modificado por:
    */
    
    public int selectValidaFechaFuturo(Connection con, String pFecha)throws SQLException, Exception{
       Statement stQuery=null;
       ResultSet rsQuery= null;
       int SidConsecutivo = -1;
       try{
         String SQL2 = "select to_date(to_char(sysdate,'dd/mm/yyyy'),'dd/mm/yyyy')-to_date('"+pFecha+"','dd/mm/yyyy') consecutivo from dual" ;

         stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
         rsQuery = stQuery.executeQuery(SQL2);
         //  secuencia para poliza_id
         System.out.println(SQL2);
         while (rsQuery.next()){
            SidConsecutivo = rsQuery.getInt("consecutivo");
         } //del while
       } //Fin try
       catch(Exception e){
         System.out.println("Ocurrio un error al accesar al metodo selectValidaFechaFuturo "+e.getMessage());
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
       return SidConsecutivo;
    } //fin Select 



     /**
     * Metodo que obtiene si es una fecha futura
     * Fecha de creacion:18/06/2009
     * Autor:Jorge Luis Perez N.
     * Fecha de modificacion:
     * Modificado por:
     */
     
     public float selectValidaFechaPasado(Connection con, String pFecha)throws SQLException, Exception{
        Statement stQuery=null;
        ResultSet rsQuery= null;
        float SidConsecutivo = -1;
        try{
          String SQL2 = "select months_between(to_date('"+pFecha+"','dd/mm/yyyy'),last_day(sysdate)) consecutivo from dual";

          stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
          rsQuery = stQuery.executeQuery(SQL2);
          //  secuencia para poliza_id
          while (rsQuery.next()){
             SidConsecutivo = rsQuery.getFloat("consecutivo");
          } //del while
        } //Fin try
        catch(Exception e){
          System.out.println("Ocurrio un error al accesar al metodo selectValidaFechaPasado "+e.getMessage());
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
        return SidConsecutivo;
     } //fin Select 



    /**
    * Metodo que lee la sequencia para la poliza que se va a generar
    * Fecha de creacion:08/06/2009
    * Autor:Jorge Luis Perez N.
    * Fecha de modificacion:
    * Modificado por:
    */
 
    public String select_SEQ_rf_tr_polizas(Connection con)throws SQLException, Exception{
       Statement stQuery=null;
       ResultSet rsQuery= null;
       String SidPoliza = "";
       try{
         String SQL2 = "select seq_rf_tr_polizas.nextval valoractual from dual";     
         stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
         rsQuery = stQuery.executeQuery(SQL2);
         //  secuencia para poliza_id
         while (rsQuery.next()){
            SidPoliza = rsQuery.getString("valoractual");
         } //del while
       } //Fin try
       catch(Exception e){
         System.out.println("Ocurrio un error al accesar al metodo select_SEQ_rf_tr_polizas "+e.getMessage());
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
     * Metodo que obtiene el consecutivo de la poliza segun ambito
     * Fecha de creacion:18/06/2009
     * Autor:Jorge Luis Perez N.
     * Fecha de modificacion:
     * Modificado por:
     */
     
     public String selectConsecutivoPolizas(Connection con)throws SQLException, Exception{
        Statement stQuery=null;
        ResultSet rsQuery= null;
        String SidConsecutivo = "";
        try{
          String SQL2 = "select nvl(max(consecutivo),0)+1 as consecutivo from rf_tr_polizas where unidad_ejecutora='" + unidad_ejecutora + "' and ambito='"+ambito +
          "' and entidad=" + entidad + " and tipo_poliza_id=" + tipo_poliza_id + " and mes=" + mes + " and pais=147 and ejercicio=" + ejercicio + " and id_catalogo_cuenta="+id_catalogo_cuenta;

          stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
          rsQuery = stQuery.executeQuery(SQL2);
          //  secuencia para poliza_id
          while (rsQuery.next()){
             SidConsecutivo = rsQuery.getString("consecutivo");
          } //del while
        } //Fin try
        catch(Exception e){
          System.out.println("Ocurrio un error al accesar al metodo selectConsecutivoPolizas "+e.getMessage());
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
        return SidConsecutivo;
     } //fin Select 
        
        
        /**
        * Metodo que obtiene el consecutivo global de la poliza por ejercicio
        * Fecha de creacion:18/06/2009
        * Autor:Jorge Luis Perez N.
        * Fecha de modificacion:
        * Modificado por:
        */
        
        public String selectConsecutivoGlobalPolizas(Connection con)throws SQLException, Exception{
           Statement stQuery=null;
           ResultSet rsQuery= null;
           String SidConsecutivo = "";
           try{
             String SQL2 = "select nvl(max(global),0)+1 as consecutivo from rf_tr_polizas where ejercicio=" + ejercicio;

             stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
             rsQuery = stQuery.executeQuery(SQL2);
             //  secuencia para poliza_id
             while (rsQuery.next()){
                SidConsecutivo = rsQuery.getString("consecutivo");
             } //del while
           } //Fin try
           catch(Exception e){
             System.out.println("Ocurrio un error al accesar al metodo selectConsecutivoGlobalPolizas "+e.getMessage());
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
           return SidConsecutivo;
        } //fin Select 
                 

   /**
   * Metodo que lee la informacion de bcPoliza
   * Fecha de creacion:08/06/2009
   * Autor:Genera Bean
   * Fecha de modificacion:
   * Modificado por:
   */
   public void select_rf_tr_polizas(Connection con, String pPolizaId)throws SQLException, Exception{
      Statement stQuery=null;
      ResultSet rsQuery=null;
      try{
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
       StringBuffer SQL=new StringBuffer("SELECT a.poliza_id,a.unidad_ejecutora,a.ambito,a.pais,a.entidad,a.consecutivo,a.tipo_poliza_id,a.maestro_operacion_id,to_char(a.fecha,'dd/mm/yyyy') fecha,a.concepto,a.referencia,to_char(a.fecha_afectacion,'') fecha_afectacion,a.clasificacion_poliza_id,a.poliza_referencia,a.num_empleado,a.global,a.ejercicio,a.mes,a.origen,a.id_catalogo_cuenta, a.ban_cheque"); 
       SQL.append(" FROM rf_tr_polizas a ");
       SQL.append(" WHERE a.poliza_id = ").append(pPolizaId).append(" ");
       System.out.println(SQL.toString());
       rsQuery=stQuery.executeQuery(SQL.toString());
       while (rsQuery.next()){
          poliza_id=(rsQuery.getString("poliza_id")==null) ? "" : rsQuery.getString("poliza_id");
          unidad_ejecutora=(rsQuery.getString("unidad_ejecutora")==null) ? "" : rsQuery.getString("unidad_ejecutora");
          ambito=(rsQuery.getString("ambito")==null) ? "" : rsQuery.getString("ambito");
          pais=(rsQuery.getString("pais")==null) ? "" : rsQuery.getString("pais");
          entidad=(rsQuery.getString("entidad")==null) ? "" : rsQuery.getString("entidad");
          consecutivo=(rsQuery.getString("consecutivo")==null) ? "" : rsQuery.getString("consecutivo");
          tipo_poliza_id=(rsQuery.getString("tipo_poliza_id")==null) ? "" : rsQuery.getString("tipo_poliza_id");
          maestro_operacion_id=(rsQuery.getString("maestro_operacion_id")==null) ? "" : rsQuery.getString("maestro_operacion_id");
          fecha=(rsQuery.getString("fecha")==null) ? "" : rsQuery.getString("fecha");
          concepto=(rsQuery.getString("concepto")==null) ? "" : rsQuery.getString("concepto");
          referencia=(rsQuery.getString("referencia")==null) ? "" : rsQuery.getString("referencia");
          fecha_afectacion=(rsQuery.getString("fecha_afectacion")==null) ? "" : rsQuery.getString("fecha_afectacion");
          clasificacion_poliza_id=(rsQuery.getString("clasificacion_poliza_id")==null) ? "" : rsQuery.getString("clasificacion_poliza_id");
          poliza_referencia=(rsQuery.getString("poliza_referencia")==null) ? "" : rsQuery.getString("poliza_referencia");
          num_empleado=(rsQuery.getString("num_empleado")==null) ? "" : rsQuery.getString("num_empleado");
          global=(rsQuery.getString("global")==null) ? "" : rsQuery.getString("global");
          ejercicio=(rsQuery.getString("ejercicio")==null) ? "" : rsQuery.getString("ejercicio");
          mes=(rsQuery.getString("mes")==null) ? "" : rsQuery.getString("mes");
          origen=(rsQuery.getString("origen")==null) ? "" : rsQuery.getString("origen");
          id_catalogo_cuenta=(rsQuery.getString("id_catalogo_cuenta")==null) ? "" : rsQuery.getString("id_catalogo_cuenta");
          ban_cheque=(rsQuery.getString("ban_cheque")==null) ? "0" : rsQuery.getString("ban_cheque");
       } // Fin while
     } //Fin try
     catch(Exception e){
       System.out.println("Ocurrio un error al accesar al metodo select_rf_tr_polizas "+e.getMessage());
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
   } //Fin metodo select_rf_tr_polizas
   
    public String select_rf_tr_polizas(Connection con, String pForma, String pReferencia, String pIdEvento,String ejercicio,String mes,String unidadEjecutora,String entidad,String ambito) throws SQLException, Exception{
       Statement stQuery=null;
       ResultSet rsQuery=null;
       String poliza=null;
       try{
        stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        StringBuffer SQL=new StringBuffer("select a.poliza_id, a.consecutivo "); 
        SQL.append(" FROM rf_tr_polizas a ");
        SQL.append(" WHERE a.origen='").append(pForma).append("' and referencia='").append(pReferencia).append("' and  idevento=").append(pIdEvento);
        SQL.append(" and a.ejercicio=").append(ejercicio).append(" and a.mes=").append(mes);
        SQL.append(" and a.unidad_ejecutora=").append(unidadEjecutora).append(" and a.entidad=").append(entidad).append(" and a.ambito=").append(ambito);
        System.out.println(SQL.toString());
        rsQuery=stQuery.executeQuery(SQL.toString());
        while (rsQuery.next()){
           poliza  =(rsQuery.getString("poliza_id")==null) ? "0" : rsQuery.getString("poliza_id");
           poliza = poliza.concat(",");
           poliza = poliza.concat((rsQuery.getString("consecutivo")==null) ? "00000" : rsQuery.getString("consecutivo"));
        } // Fin while
        poliza =(poliza==null?"0,00000":poliza); //agregado ctorres y susy 11mar2011
      } //Fin try
      catch(Exception e){
        System.out.println("Ocurrio un error al accesar al metodo select_rf_tr_polizas "+e.getMessage());
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
      return poliza;
    } //Fin metodo select_rf_tr_polizas
    
     public String select_rf_tr_polizas(Connection con, String pForma, String pReferencia, String pIdEvento,String ejercicio,String mes) throws SQLException, Exception{
        Statement stQuery=null;
        ResultSet rsQuery=null;
        String poliza=null;
        try{
         stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
         StringBuffer SQL=new StringBuffer("select a.poliza_id, a.consecutivo "); 
         SQL.append(" FROM rf_tr_polizas a ");
         SQL.append(" WHERE a.origen='").append(pForma).append("' and referencia='").append(pReferencia).append("' and  idevento=").append(pIdEvento);
         SQL.append(" and a.ejercicio=").append(ejercicio).append(" and a.mes=").append(mes);
         rsQuery=stQuery.executeQuery(SQL.toString());
         while (rsQuery.next()){
            poliza  =(rsQuery.getString("poliza_id")==null) ? "0" : rsQuery.getString("poliza_id");
            poliza = poliza.concat(",");
            poliza = poliza.concat((rsQuery.getString("consecutivo")==null) ? "00000" : rsQuery.getString("consecutivo"));
         } // Fin while
         poliza =(poliza==null?"0,00000":poliza); //agregado ctorres y susy 11mar2011
       } //Fin try
       catch(Exception e){
         System.out.println("Ocurrio un error al accesar al metodo select_rf_tr_polizas "+e.getMessage());
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
       return poliza;
     } //Fin metodo select_rf_tr_polizas
    
     public CachedRowSet select_rf_tr_formasManualesPolizas(Connection con, String ue, int ambito, int entidad) throws SQLException, Exception{
        CachedRowSet rsQuery=null;
        try{
         rsQuery = new CachedRowSet();         
         StringBuffer SQL=new StringBuffer("select pol.origen "); 
         SQL.append(" from rf_tr_polizas pol, rf_tc_formas_contables fc ");
         SQL.append(" where pol.origen=fc.forma and fc.esmanual=1 ");
         SQL.append(" and pol.unidad_ejecutora='").append(ue).append("' and pol.ambito=").append(ambito);
         SQL.append(" and pol.pais=147 ").append(" and pol.entidad=").append(entidad);
         SQL.append(" group by pol.origen");
         System.out.println(SQL.toString());
         rsQuery.setCommand(SQL.toString());
         rsQuery.execute(con);
       } //Fin try
       catch(Exception e){
         System.out.println("Ocurrio un error al accesar al metodo select_rf_tr_formasManualesPolizas "+e.getMessage());
         throw e;
       } //Fin catch
       return rsQuery;
   } //Fin metodo select_rf_tr_polizasFormasManuales
    
     
 
   /**
   * Metodo que inserta la informacion de rf_tr_polizas
   * Fecha de creacion:
   * Autor:
   * Fecha de modificacion:
   * Modificado por:
   */
   public void insert_rf_tr_polizas(Connection con)throws SQLException, Exception{
      Statement stQuery=null;
      ResultSet rsQuery= null;
      try{      
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
       StringBuffer SQL=new StringBuffer("INSERT INTO rf_tr_polizas( poliza_id,unidad_ejecutora,ambito,pais,entidad,consecutivo,tipo_poliza_id,maestro_operacion_id,fecha,concepto,referencia,fecha_afectacion,clasificacion_poliza_id,poliza_referencia,num_empleado,global,ejercicio,mes,origen,id_catalogo_cuenta,ban_cheque ) "); 
       SQL.append("VALUES(");
       SQL.append(poliza_id).append(",");
       SQL.append("'").append(unidad_ejecutora).append("',");
       SQL.append("'").append(ambito).append("',");
       SQL.append("'").append(pais).append("',");
       SQL.append(entidad).append(",");
       SQL.append(consecutivo).append(",");
       SQL.append(tipo_poliza_id).append(",");
       SQL.append(maestro_operacion_id).append(",");
              SQL.append("to_date('").append(fecha).append("','dd/mm/yyyy'),");       
       SQL.append("'").append(concepto).append("',");
       SQL.append("'").append(referencia).append("',");
       SQL.append("sysdate").append(",");
       SQL.append(clasificacion_poliza_id).append(",");
       SQL.append(poliza_referencia).append(",");
       SQL.append(num_empleado).append(",");
       SQL.append(global).append(",");
       SQL.append(ejercicio).append(",");
       SQL.append(mes).append(",");
       SQL.append("'").append(origen).append("',");
       SQL.append(id_catalogo_cuenta).append(",");       
       SQL.append(ban_cheque).append(")");
       System.out.println(SQL.toString());
       int rs=-1;
       rs=stQuery.executeUpdate(SQL.toString());
     } //Fin try
     catch(Exception e){
       System.out.println("Ocurrio un error al accesar al metodo insert_rf_tr_polizas "+e.getMessage());
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
   } //Fin metodo insert_rf_tr_polizas
 
   /**
   * Metodo que modifica la informacion de rf_tr_polizas
   * Fecha de creacion:
   * Autor:
   * Fecha de modificacion:
   * Modificado por:
   */
   public void update_rf_tr_polizas(Connection con, String clave)throws SQLException, Exception{
      Statement stQuery=null;
      try{
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
       StringBuffer SQL=new StringBuffer("UPDATE rf_tr_polizas");
       SQL.append(" SET poliza_id=").append(poliza_id).append(",");
       SQL.append("unidad_ejecutora=").append("'").append(unidad_ejecutora).append("',");
       SQL.append("ambito=").append("'").append(ambito).append("',");
       SQL.append("pais=").append("'").append(pais).append("',");
       SQL.append("entidad=").append(entidad).append(",");
       SQL.append("consecutivo=").append(consecutivo).append(",");
       SQL.append("tipo_poliza_id=").append(tipo_poliza_id).append(",");
       SQL.append("maestro_operacion_id=").append(maestro_operacion_id).append(",");
       SQL.append("fecha=").append("to_date('").append(fecha).append("','dd/mm/yyyy'),");
       SQL.append("concepto=").append("'").append(concepto).append("',");
       SQL.append("referencia=").append("'").append(referencia).append("',");
       SQL.append("fecha_afectacion=").append("to_date('").append(fecha_afectacion).append("','dd/mm/yyyy'),");
       SQL.append("clasificacion_poliza_id=").append(clasificacion_poliza_id).append(",");
       SQL.append("poliza_referencia=").append(poliza_referencia).append(",");
       SQL.append("num_empleado=").append(num_empleado).append(",");
       SQL.append("global=").append(global).append(",");
       SQL.append("ejercicio=").append(ejercicio).append(",");
       SQL.append("mes=").append(mes).append(",");
       SQL.append("origen=").append("'").append(origen).append("',");
       SQL.append("id_catalogo_cuenta=").append(id_catalogo_cuenta);
       SQL.append(" WHERE LLAVE='").append(clave).append("'");
       System.out.println(SQL.toString());
       int rs=-1;
       rs=stQuery.executeUpdate(SQL.toString());
     } //Fin try
     catch(Exception e){
       System.out.println("Ocurrio un error al accesar al metodo update_rf_tr_polizas "+e.getMessage());
       throw e;
     } //Fin catch
     finally{
       if (stQuery!=null){
         stQuery.close();
         stQuery=null;
       }
     } //Fin finally
   } //Fin metodo update_rf_tr_polizas

    /**
    * Metodo que modifica la informacion de rf_tr_polizas
    * Fecha de creacion:
    * Autor:
    * Fecha de modificacion:
    * Modificado por:
    */
    public void update_rf_tr_polizas_estatus(Connection con, String pPolizaId, String valorStatus)throws SQLException, Exception{
       Statement stQuery=null;
       try{
        stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        StringBuffer SQL=new StringBuffer("UPDATE rf_tr_polizas");
        SQL.append(" SET clasificacion_poliza_id=").append(valorStatus).append(" ");
        SQL.append(" WHERE poliza_id=").append(pPolizaId).append("");
        System.out.println(SQL.toString());
        int rs=-1;
        rs=stQuery.executeUpdate(SQL.toString());
      } //Fin try
      catch(Exception e){
        System.out.println("Ocurrio un error al accesar al metodo update_rf_tr_polizas_estatus "+e.getMessage());
        throw e;
      } //Fin catch
      finally{
        if (stQuery!=null){
          stQuery.close();
          stQuery=null;
        }
      } //Fin finally
    } //Fin metodo update_rf_tr_polizas


     /**
     * Metodo que modifica la informacion de rf_tr_polizas en los campos: set t.origen='99',t.poliza_referencia=null, t.idevento=null
     * Fecha de creacion:03/05/2012
     * Autor:Yadhira
     * Fecha de modificacion:
     * Modificado por:
     */
     public void update_rf_tr_polizas_automaticas(Connection con, String pPolizaId, String maestroOperacionId, int numEmpleado)throws SQLException, Exception{
        Statement stQuery=null;
        try{
         stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
         StringBuffer SQL=new StringBuffer("UPDATE rf_tr_polizas t");
         SQL.append(" SET t.origen='99', t.poliza_referencia=null, t.idevento=null");
         SQL.append(", t.maestro_operacion_id = " + maestroOperacionId);
         SQL.append(", t.num_empleado = " + numEmpleado);
         SQL.append(" WHERE t.poliza_id =").append(pPolizaId).append("");
         System.out.println(SQL.toString());
         int rs=-1;
         rs=stQuery.executeUpdate(SQL.toString());
       } //Fin try
       catch(Exception e){
         System.out.println("Ocurrio un error al accesar al metodo update_rf_tr_polizas_automaticas "+e.getMessage());
         throw e;
       } //Fin catch
       finally{
         if (stQuery!=null){
           stQuery.close();
           stQuery=null;
         }
       } //Fin finally
     } //Fin metodo update_rf_tr_polizas
     
          /**
     * Metodo que modifica la informacion de rf_tr_polizas en los campos: set t.origen='99',t.poliza_referencia=null, t.idevento=null
     * Fecha de creacion:03/05/2012
     * Autor:Yadhira
     * Fecha de modificacion:
     * Modificado por:
     */
     public void update_rf_tr_polizas_automaticas(Connection con, String pPolizaId, String numEmpleado)throws SQLException, Exception{
        Statement stQuery=null;
        try{
         stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
         StringBuffer SQL=new StringBuffer("UPDATE rf_tr_polizas t");
         //SQL.append(" SET t.origen='99', t.poliza_referencia=null, t.idevento=null, t.num_empleado=").append(numEmpleado);
         SQL.append(" SET t.poliza_referencia=null");
         SQL.append(" WHERE poliza_id =").append(pPolizaId).append("");
         System.out.println(SQL.toString());
         int rs=-1;
         rs=stQuery.executeUpdate(SQL.toString());
       } //Fin try
       catch(Exception e){
         System.out.println("Ocurrio un error al accesar al metodo update_rf_tr_polizas_automaticas "+e.getMessage());
         throw e;
       } //Fin catch
       finally{
         if (stQuery!=null){
           stQuery.close();
           stQuery=null;
         }
       } //Fin finally
     } //Fin metodo update_rf_tr_polizas
 
           /**
      * Metodo que modifica la referencia agregando el consecutivo del cheque que est� relacionado
      * Fecha de creacion:07/03/2014
      * Autor:Yadhira
      * Fecha de modificacion:
      * Modificado por:
      */
      public void update_referencia_consecutivoCheque(Connection con, String polizaId, String consecutivo)throws SQLException, Exception{
         Statement stQuery=null;
         try{
          stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
          StringBuffer SQL=new StringBuffer("UPDATE rf_tr_polizas t");
          SQL.append(" SET t.referencia='CHEQUE NO. " + consecutivo+"'");
          SQL.append(" , t.ban_cheque= 1");
          SQL.append(" WHERE poliza_id =").append(polizaId).append("");
          //System.out.println(SQL.toString());
          int rs=-1;
          rs=stQuery.executeUpdate(SQL.toString());
        } //Fin try
        catch(Exception e){
          System.out.println("Ocurrio un error al accesar al metodo update_referencia_consecutivoCheque "+e.getMessage());
          throw e;
        } //Fin catch
        finally{
          if (stQuery!=null){
            stQuery.close();
            stQuery=null;
          }
        } //Fin finally
      } //Fin metodo update_rf_tr_polizas
     
   /**
   * Metodo que borra la informacion de rf_tr_polizas
   * Fecha de creacion:
   * Autor:
   * Fecha de modificacion:
   * Modificado por:
   */
   public void delete_rf_tr_polizas(Connection con, String pPolizaId)throws SQLException, Exception{
      Statement stQuery=null;
      try{
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
       StringBuffer SQL=new StringBuffer("DELETE FROM rf_tr_polizas a ");
       SQL.append("WHERE a.poliza_id=").append(pPolizaId).append(" ");
       System.out.println(SQL.toString());
       int rs=-1;
       rs=stQuery.executeUpdate(SQL.toString());
     } //Fin try
     catch(Exception e){
       System.out.println("Ocurrio un error al accesar al metodo delete_rf_tr_polizas "+e.getMessage());
       throw e;
     } //Fin catch
     finally{
       if (stQuery!=null){
         stQuery.close();
         stQuery=null;
       }
     } //Fin finally
   } //Fin metodo delete_rf_tr_polizas
   
    /**
    * Metodo que obtiene el consecutivo de la poliza segun ambito
    * Fecha de creacion:18/06/2009
    * Autor:Jorge Luis Perez N.
    * Fecha de modificacion:
    * Modificado por:
    */
    
    public boolean selectVerificaEstatusMes(Connection con, String condicion, String programa)throws SQLException, Exception{
       Statement stQuery=null;
       ResultSet rsQuery= null;
       boolean bandera=false;
       try{
         String SQL = "select ejercicio, mes, estatus_cierre_id from rf_tr_cierres_mensuales where unidad_ejecutora='" + unidad_ejecutora + "' and ambito='"+ambito +
         "' and entidad=" + entidad + " and pais=147 and ejercicio=" + ejercicio + " and mes="+ mes +" and id_catalogo_cuenta="+id_catalogo_cuenta + " and programa='" +programa +"' "+ condicion;
         System.out.println(SQL);

         stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
         rsQuery = stQuery.executeQuery(SQL);
         //  secuencia para poliza_id
         while (rsQuery.next()){
            bandera = true;
         } //del while
       } //Fin try
       catch(Exception e){
         System.out.println("Ocurrio un error al accesar al metodo selectVerificaEstatusMes "+e.getMessage());
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
       return bandera;
    } //fin Select 

     /**
     * Metodo que borra la informacion de rf_tr_polizas de las polizas de eliminacion
     * Fecha de creacion:
     * Autor:
     * Fecha de modificacion:
     * Modificado por:
     */
     public void delete_rf_tr_polizas_eliminacion(Connection con, String pEjercicio, String pMes, String pCatCuenta, String pForma)throws SQLException, Exception{
        Statement stQuery=null;
        try{
         stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
         StringBuffer SQL=new StringBuffer("DELETE FROM rf_tr_polizas a WHERE ejercicio=").append(pEjercicio).append(" and mes=").append(pMes).append(" and tipo_poliza_id=5").append(" and id_catalogo_cuenta=").append(pCatCuenta).append(" and origen='").append(pForma).append("'");
         System.out.println(SQL.toString());
         int rs=-1;
         rs=stQuery.executeUpdate(SQL.toString());
       } //Fin try
       catch(Exception e){
         System.out.println("Ocurrio un error al accesar al metodo delete_rf_tr_polizas_eliminacion "+e.getMessage());
         throw e;
       } //Fin catch
       finally{
         if (stQuery!=null){
           stQuery.close();
           stQuery=null;
         }
       } //Fin finally
     } //Fin metodo delete_rf_tr_polizas

      /**
      * Metodo que borra la informacion de rf_tr_polizas de las polizas de eliminacion
      * Fecha de creacion:
      * Autor:
      * Fecha de modificacion:
      * Modificado por:
      */
      public void delete_rf_tr_polizas_detalle_Eli(Connection con, String pEjercicio, String pMes, String pCatCuenta, String pForma)throws SQLException, Exception{
         Statement stQuery=null;
         try{
          stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
          StringBuffer SQL=new StringBuffer("DELETE FROM rf_tr_detalle_poliza d WHERE d.poliza_id in (select  p.poliza_id from rf_tr_polizas p, rf_tr_detalle_poliza d where p.poliza_id=d.poliza_id and ejercicio=").append(pEjercicio).append(" and mes=").append(pMes).append(" and tipo_poliza_id=5").append(" and id_catalogo_cuenta=").append(pCatCuenta).append(" and origen='").append(pForma).append("')");
          System.out.println(SQL.toString());
          int rs=-1;
          rs=stQuery.executeUpdate(SQL.toString());
        } //Fin try
        catch(Exception e){
          System.out.println("Ocurrio un error al accesar al metodo delete_rf_tr_polizas_detalle_Eli "+e.getMessage());
          throw e;
        } //Fin catch
        finally{
          if (stQuery!=null){
            stQuery.close();
            stQuery=null;
          }
        } //Fin finally
      } //Fin metodo delete_rf_tr_polizas
      


       /**
       * Metodo que borra la informacion de rf_tr_polizas de las polizas de traspaso
       * Fecha de creacion: 05-03-2013
       * Autor: CLHL
       */
       public void delete_rf_tr_polizas_traspaso(Connection con, String pEjercicio, String pMes, String pCatCuenta)throws SQLException, Exception{
         Statement stQuery=null;
         StringBuffer SQL=new StringBuffer("");
          try{
           stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
           SQL.append("DELETE FROM rf_tr_polizas a WHERE ejercicio=").append(pEjercicio).append(" and mes=").append(pMes).append(" and tipo_poliza_id=7").append(" and id_catalogo_cuenta=").append(pCatCuenta);
           int rs=-1;
           rs=stQuery.executeUpdate(SQL.toString());
         } //Fin try
         catch(Exception e){
           System.out.println("Ocurrio un error al accesar al metodo delete_rf_tr_polizas_traspaso "+e.getMessage());
           System.out.println("Query en delete_rf_tr_polizas_traspaso: "+SQL.toString());
           throw e;
         } //Fin catch
         finally{
           if (stQuery!=null){
             stQuery.close();
             stQuery=null;
           }
         } //Fin finally
       } //Fin metodo delete_rf_tr_polizas_traspaso

         /**
         * Metodo que borra la informacion de rf_tr_detalle_poliza de las polizas de traspaso
         * Fecha de creacion: 05-03-2013
         * Autor: CLHL
         */        
        public void delete_rf_tr_polizas_detalle_traspaso(Connection con, String pEjercicio, String pMes, String pCatCuenta)throws SQLException, Exception{
           Statement stQuery=null;
          StringBuffer SQL=new StringBuffer("");
           try{
            stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            SQL.append("DELETE FROM rf_tr_detalle_poliza d WHERE d.poliza_id in (select  p.poliza_id from rf_tr_polizas p where  ejercicio=").append(pEjercicio).append(" and mes=").append(pMes).append(" and tipo_poliza_id=7").append(" and id_catalogo_cuenta=").append(pCatCuenta).append(")");
            int rs=-1;
            rs=stQuery.executeUpdate(SQL.toString());
          } //Fin try
          catch(Exception e){
            System.out.println("Ocurrio un error al accesar al metodo delete_rf_tr_polizas_detalle_traspaso "+e.getMessage());
            System.out.println("Query en delete_rf_tr_polizas_traspaso: "+SQL.toString());
            throw e;
          } //Fin catch
          finally{
            if (stQuery!=null){
              stQuery.close();
              stQuery=null;
            }
          } //Fin finally
        } //Fin metodo delete_rf_tr_polizas_detalle_traspaso
        
       /**
       * Metodo que obtiene consecutivo de una póliza
       * Fecha de creacion:12/08/2011
       * Autor:Yadhira
       * Fecha de modificacion:
       * Modificado por:
       */
       
       public String selectConsecutivo(Connection con, String idPoliza)throws SQLException, Exception{
          Statement stQuery=null;
          ResultSet rsQuery= null;
          String consecutivo= null;
          try{
            String SQL2 = "select tp.abreviatura||t.consecutivo numpoliza from rf_tr_polizas t, rf_tc_tipos_polizas tp where t.poliza_id="+
                          idPoliza+" and t.tipo_poliza_id=tp.tipo_poliza_id";        
      
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsQuery = stQuery.executeQuery(SQL2);
            rsQuery.first();
            //  secuencia para poliza_id
            System.out.println(SQL2);
            
               consecutivo = rsQuery.getString("numpoliza");
            
          } //Fin try
          catch(Exception e){
            System.out.println("Ocurrio un error al accesar al metodo selectValidaFechaFuturo "+e.getMessage());
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
          return consecutivo;
       } //fin Select 
} //Fin clase bcRf_tr_polizas

