package sia.ij.bd;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class bcIjTrJuridico {
    public bcIjTrJuridico() {
    }
    
    private String idRelacionado;
    private String relacionado;
    private String idJuridico;
    private String idJuridicoRel;


    public void setIdRelacionado(String idRelacionado) {
        this.idRelacionado = idRelacionado;
    }

    public String getIdRelacionado() {
        return idRelacionado;
    }

    public void setRelacionado(String relacionado) {
        this.relacionado = relacionado;
    }

    public String getRelacionado() {
        return relacionado;
    }

    public void setIdJuridico(String idJuridico) {
        this.idJuridico = idJuridico;
    }

    public String getIdJuridico() {
        return idJuridico;
    }

    public void setIdJuridicoRel(String idJuridicoRel) {
        this.idJuridicoRel = idJuridicoRel;
    }

    public String getIdJuridicoRel() {
        return idJuridicoRel;
    }
    
    public void insert(Connection con) throws  Exception {
        ResultSet rsQuery  = null;
        Statement statement  = null;
        StringBuffer cadenaSql   = null;

      try  {
          getNext(con);
          cadenaSql = new StringBuffer();
          cadenaSql.append("INSERT INTO ij_tr_juridico ijtrjuridico (idRelacionado, relacionado,idJuridico, idJuridicoRel) values(");
          cadenaSql.append(this.idRelacionado+",'"+this.relacionado+"',"+this.idJuridico+","+this.idJuridicoRel+")");
          
          statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
          statement.executeUpdate(cadenaSql.toString());
          
      } 
      catch (Exception ex)  {
         System.out.println("Error en bcIjTrJuridico.Insert query ="+cadenaSql.toString());
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

    public void deleteIjTrJuridico(Connection con) throws  Exception {
             int rs=-1;
             Statement stQuery=null;
             String SQL = "";
             try {
               stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
               SQL = "delete from ij_tr_juridico where ij_tr_juridico.idjuridico = " + this.idJuridico;
               rs=stQuery.executeUpdate(SQL);
             }
            catch (Exception e) {
               System.out.println("Ha occurrido un error en el metodo bcIjTrJuridico.deleteIjTrJuridico: "+SQL.toString());         
               throw e;
             }
             finally {                  
               if (stQuery != null) {
                 stQuery.close();
                 stQuery=null;
               }                         
             }
    }
    
    private void getNext(Connection con) throws  Exception {
      Statement stQuery=null;
      StringBuffer SQL = null;
      ResultSet rsQuerySeq = null;
      try {
        stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        SQL = new StringBuffer("select MAX(juridico.idrelacionado) +1 valoractual from Ij_Tr_Juridico juridico");
        rsQuerySeq = stQuery.executeQuery(SQL.toString());
        while (rsQuerySeq.next()){
          this.idRelacionado = rsQuerySeq.getString("valoractual");
        } //del while
        if(this.idRelacionado == null)
          this.idRelacionado = "1";

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
}
