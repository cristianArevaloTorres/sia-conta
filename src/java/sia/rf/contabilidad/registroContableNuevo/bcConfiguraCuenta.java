package sia.rf.contabilidad.registroContableNuevo;

import java.sql.*; 
public class bcConfiguraCuenta{ 
   public bcConfiguraCuenta(){ 
   } 
   private String idConfiguraCuenta; 
   private String longitud; 
   private String fechaVigenciaIni; 
   private String fechaVigenciaFin;  

    public String getIdConfiguraCuenta() {
        return idConfiguraCuenta;
    }

    public void setIdConfiguraCuenta(String idConfiguraCuenta) {
        this.idConfiguraCuenta = idConfiguraCuenta;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getFechaVigenciaIni() {
        return fechaVigenciaIni;
    }

    public void setFechaVigenciaIni(String fechaVigenciaIni) {
        this.fechaVigenciaIni = fechaVigenciaIni;
    }

    public String getFechaVigenciaFin() {
        return fechaVigenciaFin;
    }

    public void setFechaVigenciaFin(String fechaVigenciaFin) {
        this.fechaVigenciaFin = fechaVigenciaFin;
    }  
    
/** 
* Metodo que lee la informacion de rf_tc_configura_cuenta 
* Fecha de creacion: 31/05/2012
* Fecha de modificacion: 10/07/2012
* Autor: Elida Pozos Vazquez
* Descripcion: se corrige el nombre de campo id_configura_cuenta
*/ 
public void select_rf_tc_configura_cuenta(Connection con, String pEjercicio)throws SQLException, Exception{ 
Statement stQuery=null; 
ResultSet rsQuery=null; 
StringBuffer SQL=new StringBuffer("SELECT a.id_configura_cuenta,a.longitud,to_char(a.fecha_vig_ini,'dd/mm/yyyy') fecha_vig_ini, to_char(a.fecha_vig_fin,'dd/mm/yyyy') fecha_vig_fin ");  
SQL.append(" FROM rf_tc_configura_cuenta a "); 
SQL.append(" WHERE to_char(a.fecha_vig_ini,'yyyy')=").append(pEjercicio);
// System.out.println(SQL.toString());       
try{ 
 stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
 rsQuery=stQuery.executeQuery(SQL.toString()); 
 while (rsQuery.next()){ 
  idConfiguraCuenta=(rsQuery.getString("id_Configura_Cuenta")==null) ? "" : rsQuery.getString("id_Configura_Cuenta"); 
  longitud=(rsQuery.getString("longitud")==null) ? "" : rsQuery.getString("longitud"); 
  fechaVigenciaIni=(rsQuery.getString("fecha_vig_ini")==null) ? "" : rsQuery.getString("fecha_vig_ini"); 
  fechaVigenciaFin=(rsQuery.getString("fecha_vig_fin")==null) ? "" : rsQuery.getString("fecha_vig_fin"); 
 } // Fin while 
} //Fin try 
catch(Exception e){ 
 System.out.println("Ocurrio un error al accesar al metodo select_rf_tc_configura_cuenta "+e.getMessage()); 
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
} //Fin metodo select_rf_tc_configura_cuenta 
 
   /** 
   * Metodo que inserta la informacion de rf_tc_configura_cuenta 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void insert_rf_tc_configura_cuenta(Connection con) throws SQLException, Exception{ 
      Statement stQuery=null; 
      StringBuffer SQL=new StringBuffer("INSERT INTO rf_tc_configura_cuenta( id_Configura_Cuenta,longitud,fecha_vig_ini,fecha_vig_fin) ");
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       SQL.append("VALUES("); 
       SQL.append(idConfiguraCuenta).append(","); 
       SQL.append(longitud).append(","); 
       SQL.append("to_date('").append(fechaVigenciaIni).append("','dd/mm/yyyy hh24:mi:ss'),");
       SQL.append("to_date('").append(fechaVigenciaFin).append("','dd/mm/yyyy hh24:mi:ss'),");     
       // System.out.println(SQL.toString()); 
       int rs=-1; 
       rs=stQuery.executeUpdate(SQL.toString()); 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo insert_rf_tc_configura_cuenta "+e.getMessage()); 
       System.out.println(SQL.toString()); 
       throw e; 
     } //Fin catch 
     finally{ 
       if (stQuery!=null){ 
         stQuery.close(); 
         stQuery=null; 
       } 
     } //Fin finally 
   } //Fin metodo insert_rf_tc_configura_cuenta 
  
   /** 
   * Metodo que modifica la informacion de rf_tc_configura_cuenta 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void update_rf_tc_configura_cuenta(Connection con, String pEjercicio)throws SQLException, Exception{ 
      Statement stQuery=null; 
      StringBuffer SQL=new StringBuffer("UPDATE rf_tc_configura_cuenta"); 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       SQL.append(" SET id_Configura_Cuenta=").append(idConfiguraCuenta).append(", "); 
       SQL.append("longitud=").append(longitud).append(", "); 
       SQL.append("fecha_vig_ini=to_date('").append(fechaVigenciaIni).append("','dd/mm/yyyy hh24:mi:ss'), ");    
       SQL.append("fecha_vig_fin=to_date('").append(fechaVigenciaFin).append("','dd/mm/yyyy hh24:mi:ss'), ");           
       SQL.append(" WHERE to_char(fecha_vig_ini,'yyyy')=").append(pEjercicio); 
       // System.out.println(SQL.toString());  
       int rs=-1; 
       rs=stQuery.executeUpdate(SQL.toString()); 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo update_rf_tc_configura_cuenta "+e.getMessage()); 
       System.out.println(SQL.toString());  
       throw e; 
     } //Fin catch 
     finally{ 
       if (stQuery!=null){ 
         stQuery.close(); 
         stQuery=null; 
       } 
     } //Fin finally 
   } //Fin metodo update_rf_tc_configura_cuenta 
  
   /** 
   * Metodo que borra la informacion de rf_tc_configura_cuenta 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void delete_rf_tc_configura_cuenta(Connection con,  String pEjercicio)throws SQLException, Exception{ 
      Statement stQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("DELETE FROM rf_tc_configura_cuenta a "); 
       SQL.append(" WHERE to_char(fecha_vig_ini,'yyyy')=").append(pEjercicio); 
       System.out.println(SQL.toString()); 
       int rs=-1; 
       rs=stQuery.executeUpdate(SQL.toString()); 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo delete_rf_tc_configura_cuenta "+e.getMessage()); 
       throw e; 
     } //Fin catch 
     finally{ 
       if (stQuery!=null){ 
         stQuery.close(); 
         stQuery=null; 
       } 
     } //Fin finally 
   } //Fin metodo delete_rf_tc_configura_cuenta 

     /** 
     * Metodo que lee la informacion de rf_tc_configura_cuenta
     * Fecha de creacion: 
     * Fecha de modificacion: 
     * Modificado por: 
     */ 
     public String select_numero_niveles(Connection con, String pEjercicio)throws SQLException, Exception{ 
        Statement stQuery=null; 
        ResultSet rsQuery=null; 
        String resultado="0";
        StringBuffer SQL=new StringBuffer("SELECT max(nivel) maximoNivel");  
        SQL.append(" FROM rf_tc_configura_cuenta c, rf_tc_detalle_conf_cve d ");  
        SQL.append(" WHERE c.id_Configura_Cuenta=d.id_Configura_Cuenta and ");
        SQL.append(" to_char(fecha_vig_ini,'yyyy')=").append(pEjercicio);  
        try{ 
         stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
         rsQuery=stQuery.executeQuery(SQL.toString()); 
         while (rsQuery.next()){
            resultado=(rsQuery.getString("maximoNivel")==null) ? "" : rsQuery.getString("maximoNivel"); 
         } // Fin while 
       } //Fin try 
       catch(Exception e){ 
         System.out.println("Ocurrio un error al accesar al metodo select_numero_niveles "+e.getMessage()); 
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
       return resultado;
     } //Fin metodo select_numero_niveles        
     
      /** 
      * Metodo que lee la informacion de rf_tc_configura_cuenta
      * Fecha de creacion: 
      * Fecha de modificacion: 
      * Modificado por: 
      */ 
      public String select_tamano_niveles(Connection con, String pEjercicio)throws SQLException, Exception{ 
         Statement stQuery=null; 
         ResultSet rsQuery=null; 
         StringBuffer cadenaTamanos = new StringBuffer("");
         StringBuffer SQL=new StringBuffer("SELECT d.id_configura_cuenta,d.tamanio ");  
         SQL.append(" FROM rf_tc_configura_cuenta c, rf_tc_detalle_conf_cve d ");  
         SQL.append(" WHERE c.id_Configura_Cuenta=d.id_Configura_Cuenta and ");
         SQL.append(" to_char(fecha_vig_ini,'yyyy')=").append(pEjercicio);                    
          // System.out.println(SQL.toString()); 
         try{ 
          stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
          rsQuery=stQuery.executeQuery(SQL.toString()); 
          while (rsQuery.next()){ 
             cadenaTamanos.append(rsQuery.getString("tamanio")).append(","); 
          } // Fin while 
        } //Fin try 
        catch(Exception e){ 
          System.out.println("Ocurrio un error al accesar al metodo select_tamano_niveles "+e.getMessage()); 
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
        return cadenaTamanos.toString().substring(0,cadenaTamanos.toString().length()-1);
      } //Fin metodo select_tamano_niveles        

} //Fin clase bcConfiguraCuenta 

