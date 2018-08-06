package sia.ij.bd;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class bcIjTiPt {
    public bcIjTiPt() {
    }
    
    private String idPt;
    private String anio;
    private String fecCrea;
    private String fecNotifica;
    private String fecLibera;
    private String estatus;
    private String idJuridico;
    private String horaCrea;    
    private String horaNotifica;
    private String horaLibera;

    public void setIdPt(String idPt) {
        this.idPt = idPt;
    }

    public String getIdPt() {
        return idPt;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public String getAnio() {
        return anio;
    }

    public void setFecCrea(String fecCrea) {
        this.fecCrea = fecCrea;
    }

    public String getFecCrea() {
        return fecCrea;
    }

    public void setFecNotifica(String fecNotifica) {
        this.fecNotifica = fecNotifica;
    }

    public String getFecNotifica() {
        return fecNotifica;
    }

    public void setFecLibera(String fecLibera) {
        this.fecLibera = fecLibera;
    }

    public String getFecLibera() {
        return fecLibera;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setIdJuridico(String idJuridico) {
        this.idJuridico = idJuridico;
    }

    public String getIdJuridico() {
        return idJuridico;
    }
    
    public void setHoraNotifica(String horaNotifica) {
        this.horaNotifica = horaNotifica;
    }

    public String getHoraNotifica() {
        return horaNotifica;
    }

    public void setHoraLibera(String horaLibera) {
        this.horaLibera = horaLibera;
    }

    public String getHoraLibera() {
        return horaLibera;
    }
    
    
    public void insert(Connection con) throws  Exception {
        ResultSet rsQuery  = null;
        Statement statement  = null;
        StringBuffer cadenaSql   = null;

      try  {
          getNext(con);
          cadenaSql = new StringBuffer();
          //cadenaSql.append("Insert into ij_Ti_Pt (Idpt, Anio, Feccrea, Fecnotifica, Feclibera, Estatus, Idjuridico)  \n");
           cadenaSql.append("Insert into ij_Ti_Pt (Idpt, Anio, Feccrea, Estatus, Idjuridico)  \n");
          cadenaSql.append(" values ( \n");
          cadenaSql.append(this.idPt+",to_char(sysdate,'yyyy'),sysdate,'"+this.estatus+"',"+this.idJuridico);
         cadenaSql.append(")");
          
          statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
          statement.executeUpdate(cadenaSql.toString());

      } 
      catch (Exception ex)  {
          System.out.println("Error en el metodo bcIjTiPt.insert"+ex.getMessage());
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
        SQL = new StringBuffer("select MAX(ij_Ti_Pt.Idpt)+1 valoractual from  ij_Ti_Pt");
        rsQuerySeq = stQuery.executeQuery(SQL.toString());
        while (rsQuerySeq.next()){
          this.idPt = rsQuerySeq.getString("valoractual");
        } //del while
        if(this.idPt == null)
          this.idPt = "1";

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
       String sFNotifica = "";
       String sFLibera = "";
       try{
        stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        StringBuffer SQL = new StringBuffer();
        SQL.append(" UPDATE IJ_TI_PT IJTIPT SET  \n");
        
        if((this.fecNotifica != null) && (!this.fecNotifica.equals("")) )
          sFNotifica = " to_date('" + this.fecNotifica + " " + this.horaNotifica + "','dd/MM/yyyy HH24:MI:SS')";
        else 
          sFNotifica = "null";
           
        if((this.fecLibera != null) && (!this.fecLibera.equals("")) )
          sFLibera = " to_date('"+this.fecLibera+" "+this.horaLibera+"','dd/MM/yyyy HH24:MI:SS')";
        else   
          sFLibera =  "null";
          
        SQL.append(" IJTIPT.FECNOTIFICA = " +sFNotifica + ", \n ");
        SQL.append(" IJTIPT.FECLIBERA = "+ sFLibera +",  \n ");
        SQL.append(" IJTIPT.ESTATUS = '"+this.estatus+"' \n ");
        SQL.append(" WHERE IJTIPT.IDPT = "+this.idPt+"     \n ");

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
