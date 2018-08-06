package sia.rf.contabilidad.sistemas.cierreAnual;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PagoPasivos {
    public PagoPasivos() {
    }
    private String fecha_limite;
    private String ejercicio;

    public void setFecha_limite(String fecha_limite) {
        this.fecha_limite = fecha_limite;
    }

    public String getFecha_limite() {
        return fecha_limite;
    }

    public void setEjercicio(String ejercicio) {
        this.ejercicio = ejercicio;
    }

    public String getEjercicio() {
        return ejercicio;
    }
    
    public void  select_rf_tc_fecha_lim_pasivo(Connection con,String pejercicio)throws SQLException, Exception{
       Statement stQuery=null;
       ResultSet rsQuery= null;
       String SQL="";
       try{
         SQL = "select t.ejercicio,to_char(t.fecha_limite,'dd/mm/yyyy') as fecha_limite  from rf_tc_fecha_lim_pasivo t where t.ejercicio="+pejercicio;
         stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
         rsQuery = stQuery.executeQuery(SQL);         
         while (rsQuery.next()){
            ejercicio = rsQuery.getString("ejercicio");
            fecha_limite = rsQuery.getString("fecha_limite");
         } //del while
       } //Fin try
       catch(Exception e){
         System.out.println("Ocurrio un error al accesar al metodo select_rf_tc_fecha_lim_pasivo "+e.getMessage());
         System.out.println("SQL: "+SQL);
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
    } //fin Select 

     public int  select_F_COMPARA_FECHA(Connection con,String pFechaAfecta,String pFechaLimite)throws SQLException, Exception{
        Statement stQuery=null;
        ResultSet rsQuery= null;
        String SQL="";
        int bandera=0;
        try{
          if (pFechaLimite!=null){ // Si no hay fecha limite se debe aplicar el registro
           SQL = "select F_COMPARA_FECHA(to_date('"+pFechaAfecta+"','dd/mm/yyyy'),to_date('"+pFechaLimite+"','dd/mm/yyyy')) as bandera from DUAL t";
           stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
           rsQuery = stQuery.executeQuery(SQL);          
           while (rsQuery.next()){
             bandera = Integer.parseInt(rsQuery.getString("bandera"));             
            } //del while
          }
          else{
            bandera=1;   
          }
        } //Fin try       
        catch(Exception e){
          System.out.println("Ocurrio un error al accesar al metodo select_F_COMPARA_FECHA"+e.getMessage());
          System.out.println("SQL: "+SQL);
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
}
