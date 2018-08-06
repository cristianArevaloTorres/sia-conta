/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sia.rf.contabilidad.registroContableNuevo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class bcAperturaEjercicio {

   public bcAperturaEjercicio(){ 
   } 

   private String proceso;

    public String getProceso() {
        return proceso;
   }

   public void setProceso(String proceso) {
       this.proceso = proceso;
   }

    public String select_ultimo_ejercicio(Connection con) throws SQLException, Exception{
        Statement stQuery=null; 
        ResultSet rsQuery=null; 
        StringBuffer SQL=new StringBuffer("select max(extract(year from t.fecha_vig_ini))+1 as ejercicio from rf_tr_cuentas_contables t");  
        proceso="-1"; 
        try{
         stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
         rsQuery=stQuery.executeQuery(SQL.toString()); 
         if (rsQuery.next()){ 
            proceso=(rsQuery.getString("ejercicio")==null) ? "" : rsQuery.getString("ejercicio"); 
         } // Fin while 
        } //Fin try
        catch(Exception e){
           System.out.println("Ocurrio un error al accesar al metodo select_ultimo_ejercicio "+e.getMessage()); 
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
      return proceso;
    }
    
    public String select_ejercicio(Connection con,int ejercicioActual) throws SQLException, Exception{
        Statement stQuery=null; 
        ResultSet rsQuery=null; 
        StringBuffer SQL=new StringBuffer("select t.ejercicio from rf_tc_ejercicios t where t.ejercicio=").append(ejercicioActual);  
        proceso="-1"; 
        try{
         stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
         rsQuery=stQuery.executeQuery(SQL.toString()); 
         if (rsQuery.next()){ 
            proceso=(rsQuery.getString("ejercicio")==null) ? "" : rsQuery.getString("ejercicio"); 
         } // Fin while 
        } //Fin try
        catch(Exception e){
           System.out.println("Ocurrio un error al accesar al metodo select_ejercicio "+e.getMessage()); 
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
      return proceso;
    }
    
     public String insert_ejercicio(Connection con,int ejercicioActual) throws SQLException, Exception{ 
        Statement stQuery=null; 
        StringBuffer SQL=new StringBuffer("");
        int rs=-1; 
        try{ 
         stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
         SQL.append("INSERT INTO rf_tc_ejercicios(ejercicio,abierto) ");  
         SQL.append("VALUES("); 
         SQL.append(ejercicioActual).append(","); 
         SQL.append(0).append(")"); 
         rs=stQuery.executeUpdate(SQL.toString()); 
       } //Fin try 
       catch(Exception e){ 
         System.out.println(SQL.toString()); 
         System.out.println("Ocurrio un error al accesar al metodo insert_ejercicio "+e.getMessage()); 
         throw e; 
       } //Fin catch 
       finally{ 
         if (stQuery!=null){ 
           stQuery.close(); 
           stQuery=null; 
         } 
         SQL.replace(0,SQL.length(),"");
         SQL = null;
       } //Fin finally 
        return Integer.toString(rs);
     } //Fin metodo insert_ejercicio 
   
   
   
   
     
    /** 
     * Metodo que duplica el catalogo de cuentas en base al ejercicio anterior
     */ 
   
     public String select_duplicar_catalogo(Connection con, int ejercicioAnterior, int ejercicioActual )throws SQLException, Exception{ 
        Statement stQuery=null; 
        ResultSet rsQuery=null; 
        
        StringBuffer SQL=new StringBuffer("SELECT F_DUPLICAR_CATALOGO("+ejercicioAnterior+","+ ejercicioActual);
        SQL.append(",TO_DATE('01/01/"+ejercicioActual+"','dd/mm/yyyy') ) proceso FROM dual");
        proceso="-1"; 
       try{ 
         stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
         rsQuery=stQuery.executeQuery(SQL.toString()); 
         if (rsQuery.next()){ 
            proceso=(rsQuery.getString("proceso")==null) ? "" : rsQuery.getString("proceso"); 
         } // Fin while 
       } //Fin try 
       catch(Exception e){ 
         System.out.println("Ocurrio un error al accesar al metodo select_duplicar_catalogo "+e.getMessage()); 
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
       return proceso;
     } //Fin metodo select_duplicar_catalogo
 
      /** 
       * Metodo que duplica las operaciones contables  base al ejercicio anterior
       */       
      public String select_duplicar_maestro_operacion(Connection con, int ejercicioAnterior, int ejercicioActual)throws SQLException, Exception{ 
         Statement stQuery=null; 
         ResultSet rsQuery=null; 
         
         StringBuffer SQL=new StringBuffer("SELECT F_DUPLICAR_MAESTRO_OPERACION("+ejercicioAnterior+","+ejercicioActual+") proceso  FROM dual");  
         proceso="-1"; 
        try{ 
          stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
          rsQuery=stQuery.executeQuery(SQL.toString()); 
          if (rsQuery.next()){ 
             proceso=(rsQuery.getString("proceso")==null) ? "" : rsQuery.getString("proceso"); 
          } // Fin while 
        } //Fin try 
        catch(Exception e){ 
          System.out.println("Ocurrio un error al accesar al metodo select_duplicar_maestro_operacion "+e.getMessage()); 
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
        return proceso;
      } //Fin metodo select_duplicar_maestro_operacion    


       public String select_registrar_mes_programa(Connection con, int ejercicioActual) throws SQLException, Exception {
           Statement stQuery = null;
           Statement stQueryFuncion = null;
           ResultSet rsQuery = null;
           ResultSet rsQueryFuncion = null;
           String programa = "";
           String idCatCuentas = "";

           try {
               stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
               StringBuffer SQL = new StringBuffer("select distinct substr(t.cuenta_contable,6,4) as programa,t.id_catalogo_cuenta from rf_tr_cuentas_contables t ");
               SQL.append("where to_char(t.fecha_vig_ini,'yyyy')=").append(ejercicioActual-1).append(" and t.nivel=3 "); 
               SQL.append("order by t.id_catalogo_cuenta,substr(t.cuenta_contable,6,4) ");
            

               rsQuery = stQuery.executeQuery(SQL.toString());
               while (rsQuery.next()) {

                   programa = (rsQuery.getString("programa") == null) ? "" : rsQuery.getString("programa");
                   idCatCuentas = (rsQuery.getString("id_catalogo_cuenta") == null) ? "" : rsQuery.getString("id_catalogo_cuenta");
                  
                   StringBuffer funcionSQL = new StringBuffer("SELECT F_REGISTRAR_MES_PROGRAMA(");
                   funcionSQL.append(ejercicioActual).append(",1,").append(idCatCuentas).append(",'").append(programa).append("') as proceso FROM dual ");
        
                   stQueryFuncion = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                   rsQueryFuncion = stQueryFuncion.executeQuery(funcionSQL.toString());
                   if (rsQueryFuncion.next()){ 
                      proceso=(rsQueryFuncion.getString("proceso")==null) ? "" : rsQueryFuncion.getString("proceso"); 
                   } // Fin while 
           
                  if  (stQueryFuncion!=null){
                    stQueryFuncion.close();
                    stQueryFuncion=null;
                  }
                   
               } // Fin while 

           } //Fin try 
           catch (Exception e) {
               System.out.println("Ocurrio un error al accesar al metodo select_registrar_mes_programa " + e.getMessage());
               throw e;
           } //Fin catch 
           finally {
               if (rsQuery != null) {
                   rsQuery.close();
                   rsQuery = null;
               }
                  if (rsQueryFuncion != null) {
                   rsQueryFuncion.close();
                   rsQueryFuncion = null;
               }
               if (stQuery != null) {
                   stQuery.close();
                   stQuery = null;
               }
                  if (stQueryFuncion != null) {
                   stQueryFuncion.close();
                   stQueryFuncion = null;
               }
           } //Fin finally 
           return proceso;
       } //Fin metodo select_registrar_mes_programa




      /** 
     * Metodo que duplica la configuracion del catalogo de cuentas en la BD 
     * Fecha de creacion: 18/12/2014
     */ 

     public String select_duplicar_configuracion(Connection con, int ejercicioAnterior, int ejercicioActual)throws SQLException, Exception{ 
        Statement stQuery=null; 
        ResultSet rsQuery=null; 
        
        StringBuffer SQL=new StringBuffer("SELECT F_DUPLICA_CONFIGURACION("+ejercicioAnterior+","+ ejercicioActual);
        SQL.append(") proceso  FROM dual");
        proceso="-1"; 
       try{ 
         stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
         rsQuery=stQuery.executeQuery(SQL.toString()); 
         while (rsQuery.next()){ 
            proceso=(rsQuery.getString("proceso")==null) ? "" : rsQuery.getString("proceso"); 
         } // Fin while 
       } //Fin try 
       catch(Exception e){ 
         System.out.println("Ocurrio un error al accesar al metodo select_duplicar_configuracion "+e.getMessage()); 
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
       return proceso;
     } //Fin metodo select_duplicar_configuracion      

 
     
 
}