package sia.ij.bd;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class IjTiSecuencia {
    public IjTiSecuencia() {
    }
    
    private String idSecuencia;
    private String anio;
    private String siguiente;
    private String idInstJurid;
    
    public void setIdSecuencia(String idSecuencia) {
        this.idSecuencia = idSecuencia;
    }

    public String getIdSecuencia() {
        return idSecuencia;
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

    public void setIdInstJurid(String idInstJurid) {
        this.idInstJurid = idInstJurid;
    }

    public String getIdInstJurid() {
        return idInstJurid;
    }
    
    public void insertIjTiSecuencia(Connection con) throws  Exception {
        ResultSet rsQuery  = null;
        Statement statement  = null;
        StringBuffer cadenaSql   = null;

      try  {
           getNext(con);
          cadenaSql = new StringBuffer();
          cadenaSql.append("insert into ij_ti_Secuencia ijTiSecuencia (Idsecuencia, Anio, Siguiente, Idinstjurid) \n");
          cadenaSql.append(" values ("+this.idSecuencia+","+this.anio+","+this.siguiente+","+this.idInstJurid+")");
          
          statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
          statement.executeUpdate(cadenaSql.toString());
          
      } 
      catch (Exception ex)  {
        System.out.println("query "+cadenaSql.toString());
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
    
    
    private void getNext(Connection con) throws  Exception {
      Statement stQuery=null;
      StringBuffer SQL = null;
      ResultSet rsQuerySeq = null;
      
      try {
          stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        
          SQL = new StringBuffer("select MAX(secuencia.idsecuencia) + 1 as idsecuencia from ij_ti_Secuencia secuencia ");
          
          rsQuerySeq = stQuery.executeQuery(SQL.toString());
          
          if (rsQuerySeq.next()){
              idSecuencia = rsQuerySeq.getString("idsecuencia");
          } 
          else
           idSecuencia = "1";
          
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
