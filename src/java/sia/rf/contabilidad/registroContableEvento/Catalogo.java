package sia.rf.contabilidad.registroContableEvento;

import java.sql.SQLException;

import java.util.Date;
import sia.rf.contabilidad.registroContableNuevo.bcCheque;
import java.sql.*;
import oracle.jdbc.OracleDriver;

import sun.jdbc.rowset.CachedRowSet;


public class Catalogo {
    private String estatus;
    private String descripcion;
    private String estadoNuevo;
    
    public Catalogo() {
    estatus=null;
    descripcion=null;
    estadoNuevo=null;
    }
    
    public void select_estado_actual(Connection con)throws SQLException, Exception{ 
    Statement stQuery=null; 
    ResultSet rsQuery=null;
    try{ 
         StringBuffer SQL=new StringBuffer("");  
         stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
         SQL.append("select c.estatus, e.descripcion\n");
         SQL.append("from rf_tc_estado_catalogo c,rf_tc_estatus_catalogo e \n" );
         SQL.append("where e.estatus=c.estatus");
         rsQuery=stQuery.executeQuery(SQL.toString());
         while(rsQuery.next()){
            estatus=rsQuery.getString("estatus");          
            descripcion=rsQuery.getString("descripcion");
         }     
     } //Fin try
     catch(Exception e){
       System.out.println("Ocurrio un error al accesar al metodo select_select_BITACORALOCAL_enCache "+e.getMessage()); 
       throw e; 
     } //Fin catch
     finally{
         if(stQuery!=null){stQuery.close();stQuery=null;}
         if(rsQuery!=null){rsQuery.close();rsQuery=null;}
     } //Fin finally
}

public CachedRowSet select_estados(Connection con)throws SQLException, Exception{ 
    CachedRowSet rsQuery=null;
    rsQuery = new CachedRowSet();
    try{ 
       StringBuffer SQL=new StringBuffer("");  
       SQL.append("select e.estatus, e.descripcion\n");
       SQL.append("from rf_tc_estatus_catalogo e\n" );
       rsQuery.setCommand(SQL.toString());
       rsQuery.execute(con);
    } //Fin try
    catch(Exception e){
     System.out.println("Ocurrio un error al accesar al metodo select_estados "+e.getMessage()); 
     throw e; 
    } //Fin catch
    finally{
    } //Fin finally
return rsQuery;
}

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
    public void cambiaEstado(Connection con)throws SQLException, Exception{ 
    Statement stQuery=null; 
        try{ 
           stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
           StringBuffer SQL=new StringBuffer("");  
           SQL.append("update rf_tc_estado_catalogo e\n").append("set e.estatus=" ).append(estadoNuevo).append(" where 1=1" );
            int rs=-1; 
            rs=stQuery.executeUpdate(SQL.toString()); 
        } //Fin try
        catch(Exception e){
         System.out.println("Ocurrio un error al accesar al metodo cambiaEstado. "+e.getMessage()); 
         throw e; 
        } //Fin catch
        finally{
            if (stQuery!=null){ 
              stQuery.close(); 
              stQuery=null; 
            } 
        } //Fin finally
}

    public void setEstadoNuevo(String estadoNuevo) {
        this.estadoNuevo = estadoNuevo;
    }

    public String getEstadoNuevo() {
        return estadoNuevo;
    }
}
