package sia.ij.bd;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class bcIjTiActividad {
    public bcIjTiActividad() {
    }

    private String idActividad;
    private String actividad;
    private String porcentaje;
    private String fecAlta;
    private String idPt;
    private String idTrimestre;


    public void setIdActividad(String idActividad) {
        this.idActividad = idActividad;
    }

    public String getIdActividad() {
        return idActividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public String getActividad() {
        return actividad;
    }

    public void setPorcentaje(String porcentaje) {
        this.porcentaje = porcentaje;
    }

    public String getPorcentaje() {
        return porcentaje;
    }

    public void setFecAlta(String fecAlta) {
        this.fecAlta = fecAlta;
    }

    public String getFecAlta() {
        return fecAlta;
    }

    public void setIdPt(String idPt) {
        this.idPt = idPt;
    }

    public String getIdPt() {
        return idPt;
    }

    public void setIdTrimestre(String idTrimestre) {
        this.idTrimestre = idTrimestre;
    }

    public String getIdTrimestre() {
        return idTrimestre;
    }
    
    public void insert(Connection con) throws  Exception {
      ResultSet rsQuerySeq= null;
      Statement stQuery=null;
      Statement stQuery2=null;  
      StringBuffer SQL = null;
      try {
        getNext(con);
    
        SQL = new StringBuffer();
        SQL.setLength(0);
        SQL.append("INSERT into ij_ti_actividad t (t.idactividad, t.actividad, t.porcentaje, t.fecalta, t.idpt, t.idtrimestre)");
        SQL.append(" values (");
        SQL.append(this.idActividad +",'"+this.actividad+"',"+this.porcentaje+",sysdate,"+this.idPt+","+this.idTrimestre);
        SQL.append(")  ");
    
        stQuery2=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        stQuery2.executeUpdate(SQL.toString());
      }
      catch (Exception e) {
        System.out.println("Ha ocurrido un error en el metodo bcIjTiActividad.insert: " + SQL);

        throw new Exception(SQL.toString(),e);
      }
      finally {
        if (rsQuerySeq != null) {
          rsQuerySeq.close();
          rsQuerySeq=null;
        }
        if (stQuery != null) {
          stQuery.close();
          stQuery=null;
        }
        if (stQuery2 != null) {
          stQuery2.close();
          stQuery2=null;
        }
        if (SQL != null) {
          SQL.setLength(0);
          SQL = null;
        }
      }         
   }
   
    private void getNext(Connection con) throws  Exception {
      Statement stQuery=null;
      StringBuffer SQL = null;
      ResultSet rsQuerySeq = null;
      try {
        stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        SQL = new StringBuffer("select Max(t.idactividad)+1 valorActual from ij_ti_actividad t");
        rsQuerySeq = stQuery.executeQuery(SQL.toString());
        while (rsQuerySeq.next()){
          this.idActividad = rsQuerySeq.getString("valoractual");
        } //del while
        if(this.idActividad == null)
          this.idActividad = "1";

      }
      catch(Exception e) {
          throw new Exception(SQL.toString(),e);
      }
      finally {
        SQL.setLength(0);
        if(rsQuerySeq != null)
          rsQuerySeq.close();
        rsQuerySeq = null;
        if(stQuery != null)
          stQuery.close();
        stQuery = null;
        
      }
    }   
    
    public void update(Connection con)throws SQLException, Exception{
       Statement stQuery=null;
       try{
        stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        StringBuffer SQL = new StringBuffer();
        SQL.append(" UPDATE IJ_TI_ACTIVIDAD IJTIACTIVIDAD SET IJTIACTIVIDAD.ACTIVIDAD = '").append(this.getActividad()).append("', \n");
        SQL.append(" IJTIACTIVIDAD.PORCENTAJE = "+this.porcentaje+" \n");
        SQL.append(" where IJTIACTIVIDAD.Idactividad = "+this.idActividad+" \n");
        
        int rs = stQuery.executeUpdate(SQL.toString());
      } //Fin try
      catch(Exception e){
        System.out.println("Ocurrio un error al accesar al metodo bcIjTiPt.update "+e.getMessage());
        throw e;
      } //Fin catch
      finally{
        if (stQuery!=null){
          stQuery.close();
          stQuery=null;
        }
      } //Fin finally
    } //Fin metodo       
}
