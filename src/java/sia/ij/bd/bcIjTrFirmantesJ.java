package sia.ij.bd;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class bcIjTrFirmantesJ {
    public bcIjTrFirmantesJ() {
    }
    
    private String orden;
    private String idJuridico;
    private String idFirmante;


    public void setOrden(String orden) {
        this.orden = orden;
    }

    public String getOrden() {
        return orden;
    }

    public void setIdJuridico(String idJuridico) {
        this.idJuridico = idJuridico;
    }

    public String getIdJuridico() {
        return idJuridico;
    }

    public void setIdFirmante(String idFirmante) {
        this.idFirmante = idFirmante;
    }

    public String getIdFirmante() {
        return idFirmante;
    }
    
    public void insert(Connection con) throws  Exception {
        ResultSet rsQuery  = null;
        Statement statement  = null;
        StringBuffer cadenaSql   = null;

      try  {
          cadenaSql = new StringBuffer();
          cadenaSql.append("Insert into Ij_Tr_Firmantesj firmantesJ (orden, idJuridico, idFirmante) values(");
          cadenaSql.append(this.orden+","+this.idJuridico+","+this.idFirmante+")");
          System.out.println("Ij_Tr_Firmantesj cadenaSql =  "+cadenaSql.toString());
          statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
          statement.executeUpdate(cadenaSql.toString());
          
      } 
      catch (Exception ex)  {
         System.out.println("Error en bcIjTrFirmantesJ.Insert");
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
    
    public void delete(Connection con) throws  Exception {
             int rs=-1;
             Statement stQuery=null;
             String SQL = "";
             try {
               stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
               SQL = "delete from ij_tr_firmantesj where ij_tr_firmantesj.idjuridico = " + this.idJuridico ;
               rs=stQuery.executeUpdate(SQL);
             }
            catch (Exception e) {
               System.out.println("Ha occurrido un error en el metodo bcIjTrFirmantesJ.delete "+SQL.toString());
               throw e;
             }
             finally {                  
               if (stQuery != null) {
                 stQuery.close();
                 stQuery=null;
               }                         
             }
    }    
}
