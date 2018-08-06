package sia.rf.contabilidad.registroContableNuevo;

import java.sql.*;

import sia.db.dao.DaoFactory;

import sun.jdbc.rowset.CachedRowSet;

public class bcMaestroOperaciones {
    public bcMaestroOperaciones() {
    }

   private String maestro_operacion_id; 
   private String unidad_ejecutora; 
   private String ambito; 
   private String pais; 
   private String entidad; 
   private String consecutivo; 
   private String descripcion; 
   private String aplicacion; 
   private String registro; 
   private String fecha_vig_ini; 
   private String fecha_vig_fin; 
   private String id_catalogo_cuenta; 
  
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
   * aplicacion 
   * @return aplicacion 
   */ 
   public String getAplicacion() { 
      return aplicacion; 
   } 
  
   /** 
   * aplicacion 
   * @param aplicacion 
   */ 
   public void setAplicacion( String aplicacion ) { 
      this.aplicacion=aplicacion; 
   } 
  
   /** 
   * registro 
   * @return registro 
   */ 
   public String getRegistro() { 
      return registro; 
   } 
  
   /** 
   * registro 
   * @param registro 
   */ 
   public void setRegistro( String registro ) { 
      this.registro=registro; 
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
    * Metodo que lee la informacion de rf_tc_maestro_operaciones 
    * Fecha de creacion: 
    * Autor: 
    * Fecha de modificacion: 
    * Modificado por: 
    */ 
    public void select_rf_tc_maestro_operaciones_carga(Connection con, String lsUniEje, String lsAmbito, String lsEntidad, String lsCatCuenta, String lsEjercicio)throws SQLException, Exception{ 
       Statement stQuery=null; 
       ResultSet rsQuery=null; 
       try{ 
        stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
        StringBuffer SQL=new StringBuffer("SELECT a.maestro_operacion_id,a.unidad_ejecutora,a.ambito,a.pais,a.entidad,a.consecutivo,a.descripcion,a.aplicacion,a.registro,a.fecha_vig_ini,a.fecha_vig_fin,a.id_catalogo_cuenta ");  
        SQL.append(" FROM rf_tc_maestro_operaciones a WHERE unidad_ejecutora='").append(lsUniEje).append("' and ambito='").append(lsAmbito).append("' and entidad=").append(lsEntidad).append(" and pais=147 and id_catalogo_cuenta="); 
        SQL.append(lsCatCuenta).append(" and ").append(lsEjercicio).append(" between extract(year from fecha_vig_ini) and extract(year from fecha_vig_fin) and consecutivo=99 ");
        System.out.println(SQL.toString()); 
        rsQuery=stQuery.executeQuery(SQL.toString()); 
        while (rsQuery.next()){ 
           maestro_operacion_id=(rsQuery.getString("maestro_operacion_id")==null) ? "" : rsQuery.getString("maestro_operacion_id"); 
           unidad_ejecutora=(rsQuery.getString("unidad_ejecutora")==null) ? "" : rsQuery.getString("unidad_ejecutora"); 
           ambito=(rsQuery.getString("ambito")==null) ? "" : rsQuery.getString("ambito"); 
           pais=(rsQuery.getString("pais")==null) ? "" : rsQuery.getString("pais"); 
           entidad=(rsQuery.getString("entidad")==null) ? "" : rsQuery.getString("entidad"); 
           consecutivo=(rsQuery.getString("consecutivo")==null) ? "" : rsQuery.getString("consecutivo"); 
           descripcion=(rsQuery.getString("descripcion")==null) ? "" : rsQuery.getString("descripcion"); 
           aplicacion=(rsQuery.getString("aplicacion")==null) ? "" : rsQuery.getString("aplicacion"); 
           registro=(rsQuery.getString("registro")==null) ? "" : rsQuery.getString("registro"); 
           fecha_vig_ini=(rsQuery.getString("fecha_vig_ini")==null) ? "" : rsQuery.getString("fecha_vig_ini"); 
           fecha_vig_fin=(rsQuery.getString("fecha_vig_fin")==null) ? "" : rsQuery.getString("fecha_vig_fin"); 
           id_catalogo_cuenta=(rsQuery.getString("id_catalogo_cuenta")==null) ? "" : rsQuery.getString("id_catalogo_cuenta"); 
        } // Fin while 
      } //Fin try 
      catch(Exception e){ 
        System.out.println("Ocurrio un error al accesar al metodo select_rf_tc_maestro_operaciones_carga "+e.getMessage()); 
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
    } //Fin metodo select_rf_tc_maestro_operaciones 

     /** 
     * Metodo que lee la informacion de rf_tc_maestro_operaciones 
     * Fecha de creacion: 
     * Autor: 
     * Fecha de modificacion: 
     * Modificado por: 
     */ 
     public void select_rf_tc_maestro_operaciones_carga_var(Connection con, String lsUniEje, String lsAmbito, String lsEntidad, String lsCatCuenta, String lsEjercicio, String consecutivo)throws SQLException, Exception{ 
        Statement stQuery=null; 
        ResultSet rsQuery=null; 
        try{ 
         stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
         StringBuffer SQL=new StringBuffer("SELECT a.maestro_operacion_id,a.unidad_ejecutora,a.ambito,a.pais,a.entidad,a.consecutivo,a.descripcion,a.aplicacion,a.registro,a.fecha_vig_ini,a.fecha_vig_fin,a.id_catalogo_cuenta ");  
         SQL.append(" FROM rf_tc_maestro_operaciones a WHERE unidad_ejecutora='").append(lsUniEje).append("' and ambito='").append(lsAmbito).append("' and entidad=").append(lsEntidad).append(" and pais=147 and id_catalogo_cuenta="); 
         SQL.append(lsCatCuenta).append(" and ").append(lsEjercicio).append(" between extract(year from fecha_vig_ini) and extract(year from fecha_vig_fin) and consecutivo='").append(consecutivo).append("' ");
         System.out.println(SQL.toString()); 
         rsQuery=stQuery.executeQuery(SQL.toString()); 
         while (rsQuery.next()){ 
            maestro_operacion_id=(rsQuery.getString("maestro_operacion_id")==null) ? "" : rsQuery.getString("maestro_operacion_id"); 
            unidad_ejecutora=(rsQuery.getString("unidad_ejecutora")==null) ? "" : rsQuery.getString("unidad_ejecutora"); 
            ambito=(rsQuery.getString("ambito")==null) ? "" : rsQuery.getString("ambito"); 
            pais=(rsQuery.getString("pais")==null) ? "" : rsQuery.getString("pais"); 
            entidad=(rsQuery.getString("entidad")==null) ? "" : rsQuery.getString("entidad"); 
            consecutivo=(rsQuery.getString("consecutivo")==null) ? "" : rsQuery.getString("consecutivo"); 
            descripcion=(rsQuery.getString("descripcion")==null) ? "" : rsQuery.getString("descripcion"); 
            aplicacion=(rsQuery.getString("aplicacion")==null) ? "" : rsQuery.getString("aplicacion"); 
            registro=(rsQuery.getString("registro")==null) ? "" : rsQuery.getString("registro"); 
            fecha_vig_ini=(rsQuery.getString("fecha_vig_ini")==null) ? "" : rsQuery.getString("fecha_vig_ini"); 
            fecha_vig_fin=(rsQuery.getString("fecha_vig_fin")==null) ? "" : rsQuery.getString("fecha_vig_fin"); 
            id_catalogo_cuenta=(rsQuery.getString("id_catalogo_cuenta")==null) ? "" : rsQuery.getString("id_catalogo_cuenta"); 
         } // Fin while 
       } //Fin try 
       catch(Exception e){ 
         System.out.println("Ocurrio un error al accesar al metodo select_rf_tc_maestro_operaciones_carga "+e.getMessage()); 
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
     } //Fin metodo select_rf_tc_maestro_operaciones 

   /** 
   * Metodo que lee la informacion de rf_tc_maestro_operaciones 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void select_rf_tc_maestro_operaciones(Connection con, String clave)throws SQLException, Exception{ 
      Statement stQuery=null; 
      ResultSet rsQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("SELECT a.maestro_operacion_id,a.unidad_ejecutora,a.ambito,a.pais,a.entidad,a.consecutivo,a.descripcion,a.aplicacion,a.registro,a.fecha_vig_ini,a.fecha_vig_fin,a.id_catalogo_cuenta");  
       SQL.append(" FROM rf_tc_maestro_operaciones a "); 
       SQL.append(" WHERE a.LLAVE='").append(clave).append("'"); 
       System.out.println(SQL.toString()); 
       rsQuery=stQuery.executeQuery(SQL.toString()); 
       while (rsQuery.next()){ 
          maestro_operacion_id=(rsQuery.getString("maestro_operacion_id")==null) ? "" : rsQuery.getString("maestro_operacion_id"); 
          unidad_ejecutora=(rsQuery.getString("unidad_ejecutora")==null) ? "" : rsQuery.getString("unidad_ejecutora"); 
          ambito=(rsQuery.getString("ambito")==null) ? "" : rsQuery.getString("ambito"); 
          pais=(rsQuery.getString("pais")==null) ? "" : rsQuery.getString("pais"); 
          entidad=(rsQuery.getString("entidad")==null) ? "" : rsQuery.getString("entidad"); 
          consecutivo=(rsQuery.getString("consecutivo")==null) ? "" : rsQuery.getString("consecutivo"); 
          descripcion=(rsQuery.getString("descripcion")==null) ? "" : rsQuery.getString("descripcion"); 
          aplicacion=(rsQuery.getString("aplicacion")==null) ? "" : rsQuery.getString("aplicacion"); 
          registro=(rsQuery.getString("registro")==null) ? "" : rsQuery.getString("registro"); 
          fecha_vig_ini=(rsQuery.getString("fecha_vig_ini")==null) ? "" : rsQuery.getString("fecha_vig_ini"); 
          fecha_vig_fin=(rsQuery.getString("fecha_vig_fin")==null) ? "" : rsQuery.getString("fecha_vig_fin"); 
          id_catalogo_cuenta=(rsQuery.getString("id_catalogo_cuenta")==null) ? "" : rsQuery.getString("id_catalogo_cuenta"); 
       } // Fin while 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo select_rf_tc_maestro_operaciones "+e.getMessage()); 
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
   } //Fin metodo select_rf_tc_maestro_operaciones 
  
   /** 
   * Metodo que inserta la informacion de rf_tc_maestro_operaciones select_rf_tr_detalle_operaciones
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void insert_rf_tc_maestro_operaciones(Connection con)throws SQLException, Exception{ 
      Statement stQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("INSERT INTO rf_tc_maestro_operaciones( maestro_operacion_id,unidad_ejecutora,ambito,pais,entidad,consecutivo,descripcion,aplicacion,registro,fecha_vig_ini,fecha_vig_fin,id_catalogo_cuenta) ");  
       SQL.append("VALUES("); 
       SQL.append(maestro_operacion_id).append(","); 
       SQL.append("'").append(unidad_ejecutora).append("',"); 
       SQL.append("'").append(ambito).append("',"); 
       SQL.append("'").append(pais).append("',"); 
       SQL.append(entidad).append(","); 
       SQL.append("'").append(consecutivo).append("',"); 
       SQL.append("'").append(descripcion).append("',"); 
       SQL.append("'").append(aplicacion).append("',"); 
       SQL.append("'").append(registro).append("',"); 
       SQL.append("'").append(fecha_vig_ini).append("',"); 
       SQL.append("'").append(fecha_vig_fin).append("',"); 
       SQL.append(id_catalogo_cuenta).append(")"); 
       System.out.println(SQL.toString()); 
       int rs=-1; 
       rs=stQuery.executeUpdate(SQL.toString()); 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo insert_rf_tc_maestro_operaciones "+e.getMessage()); 
       throw e; 
     } //Fin catch 
     finally{ 
       if (stQuery!=null){ 
         stQuery.close(); 
         stQuery=null; 
       } 
     } //Fin finally 
   } //Fin metodo insert_rf_tc_maestro_operaciones 
  
   /** 
   * Metodo que modifica la informacion de rf_tc_maestro_operaciones 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void update_rf_tc_maestro_operaciones(Connection con,  String clave)throws SQLException, Exception{ 
      Statement stQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("UPDATE rf_tc_maestro_operaciones"); 
       SQL.append(" SET maestro_operacion_id=").append(maestro_operacion_id).append(","); 
       SQL.append("unidad_ejecutora=").append("'").append(unidad_ejecutora).append("',"); 
       SQL.append("ambito=").append("'").append(ambito).append("',"); 
       SQL.append("pais=").append("'").append(pais).append("',"); 
       SQL.append("entidad=").append(entidad).append(","); 
       SQL.append("consecutivo=").append("'").append(consecutivo).append("',"); 
       SQL.append("descripcion=").append("'").append(descripcion).append("',"); 
       SQL.append("aplicacion=").append("'").append(aplicacion).append("',"); 
       SQL.append("registro=").append("'").append(registro).append("',"); 
       SQL.append("fecha_vig_ini=").append("'").append(fecha_vig_ini).append("',"); 
       SQL.append("fecha_vig_fin=").append("'").append(fecha_vig_fin).append("',"); 
       SQL.append("id_catalogo_cuenta=").append(id_catalogo_cuenta); 
       SQL.append(" WHERE LLAVE='").append(clave).append("'"); 
       System.out.println(SQL.toString()); 
       int rs=-1; 
       rs=stQuery.executeUpdate(SQL.toString()); 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo update_rf_tc_maestro_operaciones "+e.getMessage()); 
       throw e; 
     } //Fin catch 
     finally{ 
       if (stQuery!=null){ 
         stQuery.close(); 
         stQuery=null; 
       } 
     } //Fin finally 
   } //Fin metodo update_rf_tc_maestro_operaciones 
  
   /** 
   * Metodo que borra la informacion de rf_tc_maestro_operaciones 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void delete_rf_tc_maestro_operaciones(Connection con, String clave)throws SQLException, Exception{ 
      Statement stQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("DELETE FROM rf_tc_maestro_operaciones a "); 
       SQL.append("WHERE a.LLAVE='").append(clave).append("'"); 
       System.out.println(SQL.toString()); 
       int rs=-1; 
       rs=stQuery.executeUpdate(SQL.toString()); 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo delete_rf_tc_maestro_operaciones "+e.getMessage()); 
       throw e; 
     } //Fin catch 
     finally{ 
       if (stQuery!=null){ 
         stQuery.close(); 
         stQuery=null; 
       } 
     } //Fin finally 
   } //Fin metodo delete_rf_tc_maestro_operaciones 
   
   
       /** 
    * Metodo que lee la informacion de rf_tr_ctaxliq, el query viene como parametro
    * Fecha de creacion: 
    * Autor: 
    * Fecha de modificacion: 
    * Modificado por: 
    */ 
    
    public CachedRowSet select_rf_tr_detalle_operaciones(String ejercicio)throws SQLException, Exception{ 
       Connection conexion=null;
       conexion=DaoFactory.getContabilidad();
       // Statement stQuery=null; 
       CachedRowSet rsQuery=null; 
       try{ 
        rsQuery = new CachedRowSet();
        
          String SQL = "select m.maestro_operacion_id, m.consecutivo, m.descripcion, d.operacion_contable_id, d.cuenta_contable_id, c.cuenta_contable, c.nivel, c.descripcion descCuentaCon "
          +"from rf_tc_maestro_operaciones m, rf_tc_detalle_operaciones d, rf_tr_cuentas_contables c "
          +" where m.MAESTRO_OPERACION_ID = d.MAESTRO_OPERACION_ID "
          +" and  d.cuenta_contable_id = c.cuenta_contable_id "
          +" and  m.consecutivo = '"+consecutivo+"' "
          +" and  m.unidad_ejecutora='" + unidad_ejecutora + "' and m.ambito='"+ambito +
                     "' and m.entidad=" + entidad + " and m.pais=147 " + " and m.id_catalogo_cuenta=" + id_catalogo_cuenta + " and "
          + ejercicio + " between extract(year from m.fecha_vig_ini) and extract(year from m.fecha_vig_fin)"
          + " and "+ejercicio + " between extract(year from c.fecha_vig_ini) and extract(year from c.fecha_vig_fin)"
          + " ORDER BY d.DETALLE_OPERACION_ID";
          System.out.println(" SQL "+SQL);

        // stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
        
        System.out.println(SQL.toString()); 
        rsQuery.setCommand(SQL.toString());
        rsQuery.execute(conexion);       
        // rsQuery=stQuery.executeQuery(SQL.toString()); 
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
        if (conexion!=null){ 
          conexion.close(); 
          conexion=null; 
        } 
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

   
} //Fin clase bcRf_tc_maestro_operaciones 
    
