package sia.ij.bd;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class bcIjTrFichaTecnica {
    public bcIjTrFichaTecnica() {
    }
    
    private String idFichaTecnica;
    private String upc;
    private String idJuridico;


    public void setIdFichaTecnica(String idFichaTecnica) {
        this.idFichaTecnica = idFichaTecnica;
    }

    public String getIdFichaTecnica() {
        return idFichaTecnica;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public String getUpc() {
        return upc;
    }

    public void setIdJuridico(String idJuridico) {
        this.idJuridico = idJuridico;
    }

    public String getIdJuridico() {
        return idJuridico;
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
        SQL.append("Insert into ij_tr_fichatecnica (Idfichatecnica, upc, id_juridico) values( \n");
        SQL.append(this.idFichaTecnica + ",'"+this.upc + "',"+this.idJuridico);
        SQL.append(")");
   
        stQuery2=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        stQuery2.executeUpdate(SQL.toString());
      }
      catch (Exception e) {
        System.out.println("Ha ocurrido un error en el metodo bcIjTrFichaTecnica.insert: " + SQL.toString());

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
    
    public void delete(Connection con) throws  Exception {
        int rs=-1;
        Statement stQuery=null;
        String SQL = "";
        try {
          stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
          SQL = "delete from ij_tr_fichatecnica where id_Juridico =  " + this.idJuridico + " " ;
          rs=stQuery.executeUpdate(SQL);
        }
       catch (Exception e) {
          System.out.println("Ha occurrido un error en el metodo delete");
          System.out.println("bcIjTrFichaTecnica.delete: "+SQL);         
          throw new Exception(SQL.toString(),e);
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
        SQL = new StringBuffer("select MAX(idfichatecnica)+1 valorActual from ij_tr_fichatecnica ");
        rsQuerySeq = stQuery.executeQuery(SQL.toString());
        while (rsQuerySeq.next()){
          this.idFichaTecnica = rsQuerySeq.getString("valoractual");
        } 
        if(this.idFichaTecnica == null)
          this.idFichaTecnica = "1";

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
