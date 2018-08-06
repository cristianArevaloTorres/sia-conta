package sia.ij.bd;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class bcIjTcEntidades {
    public bcIjTcEntidades() {
    }
    
  private String identidad;
  private String descripcion;
  private String abreviatura;
  private String entidad_curp;
  private String validado;
  private String nombre_corto;
  private String pais;

    public void setIdentidad(String identidad) {
        this.identidad = identidad;
    }

    public String getIdentidad() {
        return identidad;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setEntidad_curp(String entidad_curp) {
      if(entidad_curp == null || entidad_curp.equals(""))
           this.entidad_curp = "null";
       else 
           this.entidad_curp = "'" +entidad_curp + "'";
    }

    public String getEntidad_curp() {
        return entidad_curp;
    }

    public void setValidado(String validado) {
      if(validado == null && validado.equals(""))
          this.validado = "null";
      else
          this.validado = validado;
    }

    public String getValidado() {
        return validado;
    }

    public void setNombre_corto(String nombre_corto) {
        this.nombre_corto = nombre_corto;
    }

    public String getNombre_corto() {
        return nombre_corto;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getPais() {
        return pais;
    }
    
    public void insert(Connection con) throws  Exception {
      ResultSet rsQuerySeq= null;
      Statement stQuery=null;
      StringBuffer SQL = null;
      try {
        getNext(con);

        SQL = new StringBuffer();
        SQL.setLength(0);
        SQL.append("insert into IJ_TC_ENTIDADES ( identidad,descripcion,abreviatura,entidad_curp,validado,nombre_corto, idPais) values (");
        SQL.append(this.identidad+", ");
        SQL.append("'"+this.descripcion+"',");
        SQL.append("'"+this.abreviatura+"',");
        SQL.append(this.entidad_curp +",");
        SQL.append(this.validado+",");
        SQL.append("'"+this.nombre_corto+"',");
        SQL.append("'"+this.pais+"')");
   //3
        stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        stQuery.executeUpdate(SQL.toString());
      }
      catch (Exception e) {
        System.out.println("Ha ocurrido un error en el metodo insert");
        System.out.println("bcIjTcEntidades.insert: " + SQL);
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
        SQL = new StringBuffer("Select Max(entidades.identidad) + 1 valoractual from Ij_Tc_Entidades entidades");
        rsQuerySeq = stQuery.executeQuery(SQL.toString());
        while (rsQuerySeq.next()){
          this.identidad = rsQuerySeq.getString("valoractual");
        } //del while
        if(this.identidad == null)
          this.identidad = "1";

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
