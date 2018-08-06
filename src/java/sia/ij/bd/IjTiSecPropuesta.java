package sia.ij.bd;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class IjTiSecPropuesta {
    public IjTiSecPropuesta() {
    }
    
    private String idSecPropuesta;
    private String anio;
    private String siguiente;
    
    public void setIdSecPropuesta(String idSecPropuesta) {
        this.idSecPropuesta = idSecPropuesta;
    }

    public String getIdSecPropuesta() {
        return idSecPropuesta;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public String getAnio() {
        return anio;
    }

    public void setSiguiente(String siguiente) {
        this.siguiente = siguiente;
    }

    public String getSiguiente() {
        return siguiente;
    }    
    
    public void insertIjTiSecPropuesta(Connection con) throws  Exception {
        ResultSet rsQuery  = null;
        Statement statement  = null;
        StringBuffer cadenaSql   = null;

      try  {
          getIdSecPropuesta(con);
          cadenaSql = new StringBuffer();
          cadenaSql.append("insert into ij_Ti_SecPropuesta (idSecPropuesta, anio, Siguiente) \n");
          cadenaSql.append(" values ("+this.idSecPropuesta+","+this.anio+","+this.siguiente+")");
          System.out.println("insertIjTiSecPropuesta =    "+cadenaSql.toString());
          
          statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
          statement.executeUpdate(cadenaSql.toString());
          
      } 
      catch (Exception ex)  {
          System.out.println("error en ijTiSecPropuesta.insertIjTiSecPropuesta "+ex.getMessage());
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

    private void getIdSecPropuesta(Connection con) throws  Exception {
      Statement stQuery=null;
      StringBuffer SQL = null;
      ResultSet rsQuerySeq = null;
      
      try {
          stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        
          SQL = new StringBuffer("select MAX(secpropuesta.idsecpropuesta) + 1 as secpropuesta from ij_ti_secpropuesta secpropuesta ");
          
          rsQuerySeq = stQuery.executeQuery(SQL.toString());
          
          if (rsQuerySeq.next()){
              this.idSecPropuesta = rsQuerySeq.getString("secpropuesta");
          } 
          else
           this.idSecPropuesta  = "1";
          
      }
      catch(Exception e) {
          throw new Exception(SQL.toString(),e);
      }
      finally {
        SQL.setLength(0);
        if(rsQuerySeq != null){
          rsQuerySeq.close();
          rsQuerySeq = null;
        }
        if(stQuery != null){
          stQuery.close();
         stQuery = null;
        }
      }
    }

    public String getNumPropuesta(Connection con) throws  Exception {
      Statement stQuery=null;
      StringBuffer SQL = null;
      ResultSet rsQuerySeq = null;
      String regresa = "";
      try {
          stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        
          SQL = new StringBuffer("select nvl((max(i.siguiente) + 1),'"+this.anio+"'||'0001') as numPropuesta from ij_ti_secpropuesta I where i.anio = '"+this.anio+"'");
          
          rsQuerySeq = stQuery.executeQuery(SQL.toString());
          
          if (rsQuerySeq.next()){
            regresa = rsQuerySeq.getString("numPropuesta");
          } //if
      return regresa;
      }
      catch(Exception e) {
          throw new Exception(SQL.toString(),e);
      }
      finally {
        SQL.setLength(0);
        if(rsQuerySeq != null){
          rsQuerySeq.close();
          rsQuerySeq = null;
        }
        if(stQuery != null){
          stQuery.close();
         stQuery = null;
        }
      }
    }
}
