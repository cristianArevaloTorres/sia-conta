package sia.ij.bd;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class bcIjTrExternos {
    public bcIjTrExternos() {
    }
    
    private String idJuridico;
    private String idExternos;
    private String orden;


    public void setIdJuridico(String idJuridico) {
        this.idJuridico = idJuridico;
    }

    public String getIdJuridico() {
        return idJuridico;
    }

    public void setIdExternos(String idExternos) {
        this.idExternos = idExternos;
    }

    public String getIdExternos() {
        return idExternos;
    }

    public void setOrden(String orden) {
        this.orden = orden;
    }

    public String getOrden() {
        return orden;
    }
    
    public void insert(Connection con) throws  Exception {
        ResultSet rsQuery  = null;
        Statement statement  = null;
        StringBuffer cadenaSql   = null;

      try  {
          cadenaSql = new StringBuffer();
          cadenaSql.append("INSERT into ij_tr_externos (orden, idJuridico, idExternos) values(");
          cadenaSql.append(this.orden+","+this.idJuridico+","+this.idExternos+")");
          System.out.println("ij_tr_externos sql = "+cadenaSql.toString());
          statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
          statement.executeUpdate(cadenaSql.toString());
          
      } 
      catch (Exception ex)  {
         System.out.println("Error en bcIjTrFirmantesJ.Insert "+cadenaSql.toString());
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
    
    public void deleteIjTrExternos(Connection con) throws  Exception {
             int rs=-1;
             Statement stQuery=null;
             String SQL = "";
             try {
               stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
               SQL = "delete from ij_tr_externos where ij_tr_externos.idjuridico =  " + this.idJuridico ;
               rs=stQuery.executeUpdate(SQL);
             }
            catch (Exception e) {
               System.out.println("Ha occurrido un error en el metodo bcIjTrExternos.deleteIjTrExternos "+SQL.toString());
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
