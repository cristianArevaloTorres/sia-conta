package sia.ij.bd;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class bcIjTcPaises {
    public bcIjTcPaises() {
    }
    
  private String idPais;
  private String descripcion;
  private String nacionalidad;
  private String claveAlfabetica;
  private String claveShcp;
  private String claveTelefono;

        public void setIdPais(String idPais) {
            this.idPais = idPais;
        }

        public String getIdPais() {
            return idPais;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public void setNacionalidad(String nacionalidad) {
            this.nacionalidad = nacionalidad;
        }

        public String getNacionalidad() {
            return nacionalidad;
        }

        public void setClaveAlfabetica(String claveAlfabetica) {
            this.claveAlfabetica = claveAlfabetica;
        }

        public String getClaveAlfabetica() {
            return claveAlfabetica;
        }

        public void setClaveShcp(String claveShcp) {
          if(claveShcp == null || claveShcp.equals(""))
              this.claveShcp = "null";
          else
            this.claveShcp = claveShcp;
        }

        public String getClaveShcp() {
            return claveShcp;
        }

        public void setClaveTelefono(String claveTelefono) {
            this.claveTelefono = claveTelefono;
        }

        public String getClaveTelefono() {
            return claveTelefono;
        }  
  
    public void insert(Connection con) throws  Exception {
      ResultSet rsQuerySeq= null;
      Statement stQuery=null;
      StringBuffer SQL = null;
      try {
        getNext(con);

        SQL = new StringBuffer();
        SQL.setLength(0);
        SQL.append("insert into IJ_TC_PAISES ( ");
        SQL.append("idpais, ");
        SQL.append("DESCRIPCION, ");
        SQL.append("nacionalidad,");
        SQL.append("clave_alfabetica,");        
        SQL.append("clave_shcp,");        
        SQL.append("clave_telefono");        
        SQL.append("\n) values (");
        SQL.append(this.idPais+", ");
        SQL.append("'"+this.descripcion+"',");
        SQL.append("'"+this.nacionalidad+"',");
        SQL.append("'"+this.claveAlfabetica+"',");
        SQL.append(this.claveShcp+",");
        SQL.append("'"+this.claveTelefono+"')");
    //3
        stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        stQuery.executeUpdate(SQL.toString());
      }
      catch (Exception e) {
        System.out.println("Ha ocurrido un error en el metodo insert");
        System.out.println("bcIjTcPaises.insert: " + SQL);
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
        SQL = new StringBuffer("select MAX(paises.idpais)+1 as valoractual From Ij_Tc_Paises paises");
        rsQuerySeq = stQuery.executeQuery(SQL.toString());
        while (rsQuerySeq.next()){
          this.idPais = rsQuerySeq.getString("valoractual");
        } //del while
        if(this.idPais == null)
          this.idPais = "1";

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
