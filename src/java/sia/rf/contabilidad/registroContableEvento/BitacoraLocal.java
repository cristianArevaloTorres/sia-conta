package sia.rf.contabilidad.registroContableEvento;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import sun.jdbc.rowset.CachedRowSet;

public class BitacoraLocal{ 
   public BitacoraLocal(){ 
   } 
   private String idproceso; 
   private String idevento; 
   private String parametros; 
   private String numempleado; 
   private String fechasolicitud;
   private String fecharegistro; 
   private String estatus; 
   private String polizas; 

  
   /** 
   * idproceso 
   * @return idproceso 
   */ 
   public String getIdproceso() { 
      return idproceso; 
   } 
  
   /** 
   * idproceso 
   * @param idproceso 
   */ 
   public void setIdproceso( String idproceso ) { 
      this.idproceso=idproceso; 
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
   * numempleado 
   * @return numempleado 
   */ 
   public String getNumempleado() { 
      return numempleado; 
   } 
  
   /** 
   * numempleado 
   * @param numempleado 
   */ 
   public void setNumempleado( String numempleado ) { 
      this.numempleado=numempleado; 
   } 
  
   /** 
   * fechasolicitud 
   * @return fechasolicitud 
   */ 
   public String getFechasolicitud() { 
      return fechasolicitud; 
   } 
  
   /** 
   * fechasolicitud 
   * @param fechasolicitud 
   */ 
   public void setFechasolicitud( String fechasolicitud ) { 
      this.fechasolicitud=fechasolicitud; 
   } 
  
   /** 
   * fecharegistro 
   * @return fecharegistro 
   */ 
   public String getFecharegistro() { 
      return fecharegistro; 
   } 
  
   /** 
   * fecharegistro 
   * @param fecharegistro 
   */ 
   public void setFecharegistro( String fecharegistro ) { 
      this.fecharegistro=fecharegistro; 
   } 
  
   /** 
   * estatus 
   * @return estatus 
   */ 
   public String getEstatus() { 
      return estatus; 
   } 
  
   /** 
   * estatus 
   * @param estatus 
   */ 
   public void setEstatus( String estatus ) { 
      this.estatus=estatus; 
   } 
  
   /** 
   * polizas 
   * @return polizas 
   */ 
   public String getPolizas() { 
      return polizas; 
   } 
  
   /** 
   * polizas 
   * @param polizas 
   */ 
   public void setPolizas( String polizas ) { 
      this.polizas=polizas; 
   } 
   
    /** 
    * Metodo que lee la informacion de select_rf_tr_sistema_enCache 
    * Fecha de creacion: 
    * Autor: 
    * Fecha de modificacion: 
    * Modificado por: 
    */    
    public CachedRowSet select_BITACORALOCAL_enCache(Connection con)throws SQLException, Exception{ 
       CachedRowSet rsQuery=null;
       StringBuilder SQL=new StringBuilder(""); 
       try{ 
        rsQuery = new CachedRowSet();
        SQL.append("SELECT t.*\n");  
        SQL.append("FROM(\n"); 
        SQL.append("SELECT a.idproceso,a.idevento,replace(TO_CHAR(FECHASOLICITUD,'DD/MM/YYYY')||SUBSTR(PARAMETROS,11,100),'&'||TO_CHAR(sysdate,'YYYY')||'&','&'||TO_CHAR(FECHASOLICITUD,'YYYY')||'&') AS parametros,a.numempleado,a.fechasolicitud,a.fecharegistro,a.estatus_arm,a.polizas_arm\n");  
        SQL.append(" FROM BITACORALOCAL a \n");     
        SQL.append("   WHERE ((a.estatus_arm in (0,3) and a.idevento  not in ('145','147')) ");                   
        SQL.append("    or  ");   
        SQL.append("   (a.estatus_arm in (0,3) and a.idevento  in ('145','147')  and  ");   
        SQL.append("   ( (to_date(to_char(a.fechasolicitud,'dd/mm/yyyy'),'dd/mm/yyyy')<to_date(to_char(sysdate,'dd/mm/yyyy'),'dd/mm/yyyy'))  or (to_date(to_char(a.fechasolicitud,'dd/mm/yyyy'),'dd/mm/yyyy')=to_date(to_char(sysdate,'dd/mm/yyyy'),'dd/mm/yyyy') and to_number(to_char(a.fechasolicitud,'HH24'))<to_number(to_char(sysdate,'HH24'))-7)) ");   
        SQL.append("   )) and (a.parametros like '%&'||to_char(sysdate,'yyyy')||'&%' or a.estatus_arm = 3) ");   
        SQL.append(" ORDER BY a.idproceso\n"); 
        SQL.append(") t\n"); 
        SQL.append("WHERE rownum <=10");       
        //System.out.println("BitacoraLocal.select_BITACORALOCAL_enCache.SQL "+SQL);
        rsQuery.setCommand(SQL.toString());
        rsQuery.execute(con);
      } //Fin try 
      catch(Exception e){ 
        System.out.println("Query select_BITACORALOCAL_enCache:"+SQL.toString()); 
        System.out.println("Ocurrio un error al accesar al metodo select_BITACORALOCAL_enCache "+e.getMessage()); 
        throw e; 
      } //Fin catch 
      finally{ 
      } //Fin finally 
      return rsQuery;
    } //Fin metodo select_select_BITACORALOCAL_enCache 
   
   
     /** 
     * Metodo que lee la informacion de select_rf_tr_sistema_enCache 
     * Fecha de creacion: 26/01/2011
     * Autor: 
     * Fecha de modificacion: 
     * Modificado por: 
     */    
         
      public String select_BITACORALOCAL_Query(String evento,String fecha01,String fecha02,String fecha03, String fecha04,String estatus,String referencia, String sinPoliza) throws SQLException, Exception{ 
        int condiciones=0;      
        StringBuilder SQL=new StringBuilder("SELECT rownum numRegistro,a.idevento,to_char(a.fechasolicitud, 'dd/mm/yyyy hh:mm:ss') fechasolicitud, to_char(a.fecharegistro, 'dd/mm/yyyy hh:mm:ss') fecharegistro,a.parametros,a.estatus_arm,a.polizas_arm");  
        SQL.append(" FROM BITACORALOCAL a "); 
        if(!evento.equals("") || !estatus.equals("") || !fecha01.equals("") || !fecha02.equals("") || !fecha04.equals("") || !referencia.equals("")) {
              SQL.append(" WHERE ");
          }
          if(!evento.equals("")){
            SQL.append(" a.idevento=").append(evento);
            condiciones++;
          }
          if(!estatus.equals("")){
            if(condiciones!=0) {
                  SQL.append(" and ");
              }
            SQL.append(" a.estatus_arm=").append(estatus);
            condiciones++;
          }
          if(!fecha01.equals("")){
            if(condiciones!=0) {
                  SQL.append(" and ");
              }
            SQL.append(" (to_date(to_char(a.fechasolicitud,'dd/mm/yyyy'),'dd/mm/yyyy')>= to_date('").append(fecha01).append("','dd/mm/yyyy'))");
            condiciones++;
          }
          if(!fecha02.equals("")){
            if(condiciones!=0) {
                  SQL.append(" and ");
              }
            SQL.append(" (to_date(to_char(a.fechasolicitud,'dd/mm/yyyy'),'dd/mm/yyyy')<= to_date('").append(fecha02).append("','dd/mm/yyyy'))");
            condiciones++;
          }
          if(!fecha03.equals("")){
            if(condiciones!=0) {
                  SQL.append(" and ");
              }
            SQL.append(" (to_date(to_char(a.fecharegistro,'dd/mm/yyyy'),'dd/mm/yyyy')>= to_date('").append(fecha03).append("','dd/mm/yyyy'))");
            condiciones++;
          }
          if(!fecha04.equals("")){
            if(condiciones!=0) {
                  SQL.append(" and ");
              }
            SQL.append(" (to_date(to_char(a.fecharegistro,'dd/mm/yyyy'),'dd/mm/yyyy')<= to_date('").append(fecha04).append("','dd/mm/yyyy'))");
            condiciones++;
          }
          if(!referencia.equals("")){
            if(condiciones!=0) {
                  SQL.append(" and ");
              }
            SQL.append(" parametros like('%").append(referencia).append("%')");
            condiciones++;
          }
        if(sinPoliza.equals("1")){
          if(condiciones!=0) {
                SQL.append(" and ");
            }
          SQL.append(" (a.polizas_arm like '%SIN POLIZA%' and not a.polizas_arm like '%0%') ");
          condiciones++;
        }
          SQL.append(" order by  numRegistro,a.idevento"); 
        return SQL.toString();
      } //Fin metodo select_select_BITACORALOCAL_enCache
     
     
  
   /** 
   * Metodo que lee la informacion de BITACORALOCAL 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void select_BITACORALOCAL(Connection con, String pEvento, String pProceso)throws SQLException, Exception{ 
      Statement stQuery=null; 
      ResultSet rsQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuilder SQL=new StringBuilder("SELECT a.idproceso,a.idevento,a.parametros,a.numempleado,a.fechasolicitud,a.fecharegistro,a.estatus,a.polizas");  
       SQL.append(" FROM BITACORALOCAL a "); 
       SQL.append(" WHERE a.idEvento=").append(pEvento).append(" and a.idProceso=").append(pProceso); 
       System.out.println(SQL.toString()); 
       rsQuery=stQuery.executeQuery(SQL.toString()); 
       while (rsQuery.next()){ 
          idproceso=(rsQuery.getString("idproceso")==null) ? "" : rsQuery.getString("idproceso"); 
          idevento=(rsQuery.getString("idevento")==null) ? "" : rsQuery.getString("idevento"); 
          parametros=(rsQuery.getString("parametros")==null) ? "" : rsQuery.getString("parametros"); 
          numempleado=(rsQuery.getString("numempleado")==null) ? "" : rsQuery.getString("numempleado"); 
          fechasolicitud=(rsQuery.getString("fechasolicitud")==null) ? "" : rsQuery.getString("fechasolicitud"); 
          fecharegistro=(rsQuery.getString("fecharegistro")==null) ? "" : rsQuery.getString("fecharegistro"); 
          estatus=(rsQuery.getString("estatus")==null) ? "" : rsQuery.getString("estatus"); 
          polizas=(rsQuery.getString("polizas")==null) ? "" : rsQuery.getString("polizas"); 
       } // Fin while 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo select_BITACORALOCAL "+e.getMessage()); 
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
   } //Fin metodo select_BITACORALOCAL 
  

        private String select_BITACORALOCAL_Seq(Connection con) throws SQLException, Exception {
            Statement stQuery=null;
            ResultSet rsQuery= null;
            String numConsec=null;
           StringBuilder SQL =new StringBuilder("SELECT seqBitacoraLocal.nextval actual from dual");
            try{
                stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                rsQuery = stQuery.executeQuery(SQL.toString());
                  while (rsQuery.next()){
                   numConsec = rsQuery.getString("actual");
                  }
            }
              catch (Exception e){
                    System.out.println(SQL);
                    System.out.println("Ocurrio un error al accesar el metodo select_BITACORALOCAL_Seq "+ e.getMessage());  
                    throw e;
            }
            
            finally{
                if (rsQuery != null){
                    rsQuery.close();
                }

                if (stQuery != null){
                    stQuery.close();
                }
            }//finally 
                return numConsec;

          }     

    
   /** 
   * Metodo que inserta la informacion de BITACORALOCAL 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public String insert_BITACORALOCAL(Connection con )throws SQLException, Exception{ 
      Statement stQuery=null; 
      String lsProceso =  "";
      try{ 
       lsProceso=select_BITACORALOCAL_Seq(con);
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuilder SQL=new StringBuilder("INSERT INTO BITACORALOCAL( idproceso,idevento,parametros,numempleado,fechasolicitud,fecharegistro,estatus,polizas) ");  
       SQL.append("VALUES("); 
       SQL.append(lsProceso).append(","); 
       SQL.append(idevento).append(","); 
       SQL.append("'").append(parametros).append("',"); 
       SQL.append(numempleado).append(","); 
       SQL.append("to_date('").append(fechasolicitud).append("','dd/mm/yyyy hh24:mi:ss'),"); 
       SQL.append("'").append(fecharegistro).append("',"); 
       SQL.append("'").append(estatus).append("',"); 
       SQL.append("'").append(polizas).append("')"); 
       System.out.println(SQL.toString()); 
       int rs=-1; 
       rs=stQuery.executeUpdate(SQL.toString()); 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo insert_BITACORALOCAL "+e.getMessage()); 
       throw e; 
     } //Fin catch 
     finally{ 
       if (stQuery!=null){ 
         stQuery.close();
       } 
     } //Fin finally 
     return lsProceso;
   } //Fin metodo insert_BITACORALOCAL 
  
   /** 
   * Metodo que modifica la informacion de BITACORALOCAL 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void update_BITACORALOCAL(Connection con, int pEvento, int pProceso)throws SQLException, Exception{ 
      Statement stQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuilder SQL=new StringBuilder("UPDATE BITACORALOCAL"); 
       SQL.append(" SET idproceso=").append(idproceso).append(","); 
       SQL.append("idevento=").append(idevento).append(","); 
       SQL.append("parametros=").append("'").append(parametros).append("',"); 
       SQL.append("numempleado=").append(numempleado).append(","); 
       SQL.append("fechasolicitud=").append("'").append(fechasolicitud).append("',"); 
       SQL.append("fecharegistro=").append("'").append(fecharegistro).append("',"); 
       SQL.append("estatus=").append("'").append(estatus).append("',"); 
       SQL.append("polizas=").append("'").append(polizas); 
       SQL.append(" WHERE a.idEvento=").append(pEvento).append(" and a.idProceso=").append(pProceso);        
       System.out.println(SQL.toString()); 
       int rs=-1; 
       rs=stQuery.executeUpdate(SQL.toString()); 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo update_BITACORALOCAL "+e.getMessage()); 
       throw e; 
     } //Fin catch 
     finally{ 
       if (stQuery!=null){ 
         stQuery.close();
       } 
     } //Fin finally 
   } //Fin metodo update_BITACORALOCAL 

    /** 
    * Metodo que modifica la informacion de BITACORALOCAL 
    * Fecha de creacion: 
    * Autor: 
    * Fecha de modificacion: 
    * Modificado por: 
    */ 
    public void update_BITACORALOCAL_estatus(Connection con, int pEvento, int pProceso, String pEstatus, String pPolizas)throws SQLException, Exception{ 
       Statement stQuery=null; 
       StringBuilder SQL=new StringBuilder(""); 
       try{ 
        stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
        SQL.append("UPDATE BITACORALOCAL a"); 
        SQL.append(" set estatus_arm=").append("'").append(pEstatus).append("', ");
        SQL.append("     fechaRegistro=").append("sysdate").append(", ");
        SQL.append("     polizas_arm=").append("'").append(pPolizas).append("' "); 
        SQL.append(" WHERE a.idEvento=").append(pEvento).append(" and a.idProceso=").append(pProceso);                
        int rs=-1; 
        rs=stQuery.executeUpdate(SQL.toString()); 
      } //Fin try 
      catch(Exception e){ 
        System.out.println("Query update_BITACORALOCAL_estatus: "+SQL.toString()); 
        System.out.println("Ocurrio un error al accesar al metodo update_BITACORALOCAL_estatus "+e.getMessage()); 
        throw e; 
      } //Fin catch 
      finally{ 
        if (stQuery!=null){ 
          stQuery.close();
        } 
      } //Fin finally 
    } //Fin metodo update_BITACORALOCAL 

  
   /** 
   * Metodo que borra la informacion de BITACORALOCAL 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void delete_BITACORALOCAL(Connection con, String pEvento, String pProceso)throws SQLException, Exception{ 
      Statement stQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuilder SQL=new StringBuilder("DELETE FROM BITACORALOCAL a "); 
       SQL.append(" WHERE a.idEvento=").append(pEvento).append(" and a.idProceso=").append(pProceso); 
       System.out.println(SQL.toString()); 
       int rs=-1; 
       rs=stQuery.executeUpdate(SQL.toString()); 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo delete_BITACORALOCAL "+e.getMessage()); 
       throw e; 
     } //Fin catch 
     finally{ 
       if (stQuery!=null){ 
         stQuery.close();
       } 
     } //Fin finally 
   } //Fin metodo delete_BITACORALOCAL 
} //Fin clase bcBitacoralocal 
    
