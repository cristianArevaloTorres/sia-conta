package sia.rf.contabilidad.registroContableNuevo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Vector;

public class bcConsultaPerfiles {
    public bcConsultaPerfiles() {
    }
    
    private String numEmpleado;
    private String eventos;

    public void setNumEmpleado(String numEmpleado) {
        this.numEmpleado = numEmpleado;
    }

    public String getNumEmpleado() {
        return numEmpleado;
    }

    public void setEventos(String eventos) {
        this.eventos = eventos;
    }

    public String getEventos() {
        return eventos;
    }

    /** 
    * Metodo que lee la informacion de rf_tc_pers_modifica_polizas en el cual indica si un empleado tiene privilegios para modificar pólizas automáticas y que eventos
    * Fecha de creacion: 20/02/2011
    * Autor: Claudia Luz Hernandez Lopez
    * Fecha de modificacion: 
    * Modificado por: 
    */ 
    public void select_rf_tc_pers_modifica_polizas(Connection con, String pNumEmpleado)throws SQLException, Exception{ 
       Statement stQuery=null; 
       ResultSet rsQuery=null; 
       StringBuffer SQL=new StringBuffer("");
       try{ 
          stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
          SQL.append("  select empleado,eventos   "); 
          SQL.append(" from rf_tc_pers_modifica_polizas ");
          SQL.append("  where  empleado='").append(pNumEmpleado).append("' ");
          rsQuery=stQuery.executeQuery(SQL.toString()); 
          
          if (rsQuery.next()){ 
            numEmpleado=(rsQuery.getString("empleado")==null) ? "" : rsQuery.getString("empleado"); 
            eventos=(rsQuery.getString("eventos")==null) ? "" : rsQuery.getString("eventos"); 
          } // Fin while 
          else{
            numEmpleado=""; 
            eventos="";
          }
      } //Fin try 
      catch(Exception e){ 
        System.out.println(SQL.toString()); 
        System.out.println("Ocurrio un error al accesar al metodo select_rf_tc_pers_modifica_polizas "+e.getMessage()); 
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
    } //Fin metodo select_rf_tc_pers_modifica_polizas     
    

     /** 
     * Metodo que lee la informacion de rf_tc_pers_modifica_polizas y devuelve los numEmpleado que puede ver varios ambitos
     * Fecha de creacion: 17/05/2011
     * Autor: Claudia Luz Hernandez Lopez
     * Fecha de modificacion: 
     * Modificado por: 
     */ 
     public void select_pers_varios_ambitos(Connection con)throws SQLException, Exception{ 
        Statement stQuery=null; 
        ResultSet rsQuery=null; 
        StringBuffer SQL=new StringBuffer("");
        try{ 
           stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
           SQL.append(" select empleado   "); 
           SQL.append(" from rf_tc_pers_modifica_polizas ");
           SQL.append(" where variosambitos=1  ");
           rsQuery=stQuery.executeQuery(SQL.toString()); 
           
           numEmpleado="|";
           while (rsQuery.next()){ 
             numEmpleado=numEmpleado+rsQuery.getString("empleado")+"|"; 
           } // Fin while            
       } //Fin try 
       catch(Exception e){ 
         System.out.println(SQL.toString()); 
         System.out.println("Ocurrio un error al accesar al metodo select_pers_varios_ambitos"+e.getMessage()); 
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
     } //Fin metodo select_pers_varios_ambitos        

    
    
    
}
