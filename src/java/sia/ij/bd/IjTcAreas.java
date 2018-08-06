package sia.ij.bd;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class IjTcAreas {
    public IjTcAreas() {
    }
    
    private String idAreas;
    private String descripcion;
    private String estatus;

    public void setIdAreas(String idAreas) {
        this.idAreas = idAreas;
    }

    public String getIdAreas() {
        return idAreas;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getEstatus() {
        return estatus;
    }
    
    public void insertIjTcAreas(Connection con) throws  Exception {
        ResultSet rsQuery  = null;
        Statement statement  = null;
        StringBuffer cadenaSql   = null;

      try  {
          cadenaSql.append("Insert into Ij_tc_Areas (idAreas, descripcion, estatus) values(");
          cadenaSql.append(this.idAreas+","+this.descripcion+","+this.estatus+")");
          
          statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
          statement.executeUpdate(cadenaSql.toString());
          
      } 
      catch (Exception ex)  {
          throw new Exception(cadenaSql.toString(),ex);
      } 
      finally  {
          cadenaSql.setLength(0);
          
          if(rsQuery != null){
              rsQuery.close();
              rsQuery = null;
          }
          
          if(statement != null){
              statement.close();
              statement = null;
          }
      }
    }    
}
