package sia.ij.bd;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class IjTrFirmantesP {
    public IjTrFirmantesP() {
    }
    
    private String idPropuesta;
    private String idFirmante;
    private String orden;

    public void setIdPropuesta(String idPropuesta) {
        this.idPropuesta = idPropuesta;
    }

    public String getIdPropuesta() {
        return idPropuesta;
    }

    public void setIdFirmante(String idFirmante) {
        this.idFirmante = idFirmante;
    }

    public String getIdFirmante() {
        return idFirmante;
    }
    
    public void setOrden(String orden) {
        this.orden = orden;
    }

    public String getOrden() {
        return orden;
    }
    
    public void insertIjTrFirmantesP(Connection con) throws  Exception {
        ResultSet rsQuery  = null;
        Statement statement  = null;
        StringBuffer cadenaSql   = null;

      try  {
          cadenaSql = new StringBuffer();
          cadenaSql.append("Insert into Ij_tr_FirmantesP (idPropuesta, idFirmante, orden) values(");
          cadenaSql.append(this.idPropuesta+","+this.idFirmante+","+this.orden+")");
          System.out.println("Insert into insertIjTrFirmantesP  "+cadenaSql.toString());
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
    }//insertIjTrFirmantesP
    
   public void deleteIjTrFirmantesP(Connection con) throws  Exception {
            int rs=-1;
            Statement stQuery=null;
            String SQL = "";
            try {
              stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
              SQL = "delete from Ij_Tr_Firmantesp where IDPROPUESTA = " + this.idPropuesta + " " ;
              rs=stQuery.executeUpdate(SQL);
            }
           catch (Exception e) {
              System.out.println("Ha occurrido un error en el metodo delete");
              System.out.println("IjTrFirmantesP.deleteIjTrFirmantesP: "+SQL.toString());         
              throw e;
            }
            finally {                  
              if (stQuery != null) {
                stQuery.close();
                stQuery=null;
              }                         
            }
  }//deleteIjTrFirmantesP
}
