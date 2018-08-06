package sia.rf.contabilidad.sistemas.mipf.modPrueba;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Statement;

import sun.jdbc.rowset.CachedRowSet;

public class Devengado {
    public Devengado() {
    }
  
    private CachedRowSet crsDevengado=null;  
    private String parametros;
    private String ejercicio;
    private String polizas;
    private String estatus;
    private String fecha_registro;

    public void setParametros(String parametros) {
        this.parametros = parametros;
    }

    public String getParametros() {
        return parametros;
    }

    public void setEjercicio(String ejercicio) {
        this.ejercicio = ejercicio;
    }

    public String getEjercicio() {
        return ejercicio;
    }

    public void setPolizas(String polizas) {
        this.polizas = polizas;
    }

    public String getPolizas() {
        return polizas;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getEstatus() {
        return estatus;
    }
    public void setFecha_registro(String fecha_registro) {
        this.fecha_registro = fecha_registro;
    }

    public String getFecha_registro() {
        return fecha_registro;
    }
public CachedRowSet leeDevengadoPorEjercicio(Connection con,  String ejercicio) throws SQLException, Exception {
    
    StringBuffer SQL = new StringBuffer("");    
    
    SQL.append("Select '31/12/'||ci.ejercicio||'&'||ci.ejercicio||'&'||com.CONSECUTIVO as PARAMETROS  ")
              .append(" from rf_th_cierre_comp_pago  ci, ")
              .append("      rf_tv_control_meses m, ")
              .append("      RF_TR_COMP_PAGO com ")
              .append(" where ci.ejercicio=m.ejercicio and ci.mes=m.mes and ci.corte=m.corte ")
              .append(" and ci.id_comp_pago=com.id_comp_pago ")
              .append(" and ci.saldo<>0 ")
              .append(" and ci.mes=12 and ci.ejercicio=").append(ejercicio)
              .append(" and m.corte = (select max(n.corte) from rf_tv_control_meses n where n.mes=12 and n.ejercicio=").append(ejercicio).append(") ")
              .append(" group by '31/12/'||ci.ejercicio||'&'||ci.ejercicio||'&'||com.CONSECUTIVO  ")
              .append(" order by '31/12/'||ci.ejercicio||'&'||ci.ejercicio||'&'||com.CONSECUTIVO ");
    try {
        crsDevengado = new CachedRowSet();
        crsDevengado.setCommand(SQL.toString());         
        crsDevengado.execute(con);
    }catch (Exception e){
        System.out.println ("Ha ocurrido un error en el metodo leeDevengadoPorEjercicio");
        System.out.println ("leeDevengadoPorEjercicio:" + SQL.toString());
        crsDevengado.close();
        crsDevengado=null;
        throw e;
    } finally {
        SQL.setLength(0);
        SQL=null;
     }
     return crsDevengado;
}

    /** 
    * Metodo que inserta la informacion de rf_tr_comp_devengados 
    * Fecha de creacion: 16/12/2013
    * Autor: Claudia Luz Hernandez
    */ 
    public int insert_rf_tr_comp_devengados(Connection con )throws SQLException, Exception{ 
       Statement stQuery=null;       
       StringBuffer SQL=new StringBuffer("");
       int rs=-1;
       
       try{ 

        stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
        SQL.append("INSERT INTO RF_TR_COMP_DEVEGADOS (parametros,ejercicio,polizas,estatus,fecharegistro) ");  
        SQL.append("VALUES("); 
        SQL.append("'").append(parametros).append("',"); 
        SQL.append(ejercicio).append(","); 
        SQL.append("'").append(polizas).append("',"); 
        SQL.append("'").append(estatus).append("',");     
        SQL.append("sysdate)"); 
        rs=stQuery.executeUpdate(SQL.toString()); 
      } //Fin try 
      catch(Exception e){ 
        System.out.println("Ocurrio un error al accesar al metodo insert_rf_tr_comp_devengados "+e.getMessage()); 
        System.out.println(SQL.toString()); 
        throw e; 
      } //Fin catch 
      finally{ 
        if (stQuery!=null){ 
          stQuery.close(); 
          stQuery=null; 
        } 
      } //Fin finally 
      return rs;
    } //Fin metodo insert_rf_tr_comp_devengados

     /** 
     * Metodo que realiza un select de la tabla rf_tr_comp_devengados para ver si ya se procesaron registros de un ejercicio
     * Fecha de creacion: 17/12/2013
     * Autor: Claudia Luz Hernandez
     */ 
     public int select_rf_tr_comp_devengados(Connection con, String ejercicio )throws SQLException, Exception{ 
        Statement stQuery=null;  
        ResultSet rsQuery=null;
        StringBuffer SQL=new StringBuffer("");
        int rs=-1;
        
        try{ 

         stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
         SQL.append("SELECT COUNT(*) cuantos FROM RF_TR_COMP_DEVEGADOS T WHERE T.EJERCICIO='").append(ejercicio).append("'");  
         
         rsQuery=stQuery.executeQuery(SQL.toString()); 
         while (rsQuery.next()){
              rs = rsQuery.getInt("cuantos");
           } //del while
       } //Fin try 
       catch(Exception e){ 
         System.out.println("Ocurrio un error al accesar al metodo select_rf_tr_comp_devengados "+e.getMessage()); 
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
       return rs;
     } //Fin metodo select_rf_tr_comp_devengados




}
