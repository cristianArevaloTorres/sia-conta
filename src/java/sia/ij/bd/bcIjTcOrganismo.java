package sia.ij.bd;

import sia.configuracion.BeanBase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;


public class bcIjTcOrganismo extends BeanBase {
		
  private String idorganismo;
	private String organismo;
	private String siglas;
	private String sector;
	private String estatus;
    private String paisd;
    private String entidadd;
	private String idpais;
	private String identidad;
  
	public bcIjTcOrganismo() {
		limpia();
	}
	
  public void setIdorganismo(String idorganismo) {
		this.idorganismo = idorganismo;
	}

	public String getIdorganismo() {
		return this.idorganismo;
	}

	public void setOrganismo(String organismo) {
		this.organismo = organismo;
	}

	public String getOrganismo() {
		return this.organismo;
	}

	public void setSiglas(String siglas) {
		this.siglas = siglas;
	}

	public String getSiglas() {
		return this.siglas;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}

	public String getSector() {
		return this.sector;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public String getEstatus() {
		return this.estatus;
	}

	public void setPaisd(String paisd) {
		this.paisd = paisd;
	}

	public String getPaisd() {
		return this.paisd;
	}

	public void setEntidadd(String entidadd) {
		this.entidadd = entidadd;
	}

	public String getEntidadd() {
		return this.entidadd;
	}

	public void setIdpais(String idpais) {
		this.idpais = idpais;
	}

	public String getIdpais() {
		return this.idpais;
	}

	public void setIdentidad(String identidad) {
		this.identidad = identidad;
	}

	public String getIdentidad() {
		return this.identidad;
	}

  private void limpia(){
		idorganismo = "";
		organismo = "";
		siglas = "";
		sector = "";
		estatus = "";
        paisd = "";
        entidadd = "";
		idpais = "";
		identidad = "";
 
  }
  
  /*private void adecuaCampos() {

  }*/
  
  private void getNext(Connection con) throws  Exception {
    Statement stQuery=null;
    StringBuffer SQL = null;
    ResultSet rsQuerySeq = null;
    try {
      stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      SQL = new StringBuffer("select max(IDORGANISMO) + 1 valoractual from IJ_TC_ORGANISMO");
      rsQuerySeq = stQuery.executeQuery(SQL.toString());
      while (rsQuerySeq.next()){
        idorganismo = rsQuerySeq.getString("valoractual");
      } //del while
      if(idorganismo == null)
        idorganismo = "1";

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
  
  public int select(Connection con) throws Exception {
    StringBuffer SQL;
    SQL = new StringBuffer();
			SQL.append("select \n");
			SQL.append("  IDORGANISMO, \n");
			SQL.append("  ORGANISMO, \n");
			SQL.append("  SIGLAS, \n");
			SQL.append("  SECTOR, \n");
			SQL.append("  ESTATUS, \n");
            SQL.append("  PAISD, \n");
			SQL.append("  ENTIDADD, \n");
			SQL.append("  IDPAIS, \n");
			SQL.append("  IDENTIDAD \n");
			SQL.append("from \n"); 
			SQL.append("  IJ_TC_ORGANISMO\n");
			SQL.append("where \n");
			SQL.append("  IDORGANISMO = " + idorganismo + "  ");

    Statement stQuery = null;
    ResultSet rsQuery = null;
    int resultado = 0;
    try {
      stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      rsQuery = stQuery.executeQuery(SQL.toString());
      
      rsQuery.beforeFirst();
      if (rsQuery.next() )  {
        
        limpia();
        
              idorganismo = rsQuery.getString("IDORGANISMO")==null?"":rsQuery.getString("IDORGANISMO");
              organismo = rsQuery.getString("ORGANISMO")==null?"":rsQuery.getString("ORGANISMO");
              siglas = rsQuery.getString("SIGLAS")==null?"":rsQuery.getString("SIGLAS");
              sector = rsQuery.getString("SECTOR")==null?"":rsQuery.getString("SECTOR");
              estatus = rsQuery.getString("ESTATUS")==null?"":rsQuery.getString("ESTATUS");
              paisd = rsQuery.getString("PAISD")==null?"":rsQuery.getString("PAISD");
              entidadd = rsQuery.getString("ENTIDADD")==null?"":rsQuery.getString("ENTIDADD");
              idpais = rsQuery.getString("IDPAIS")==null?"":rsQuery.getString("IDPAIS");
              identidad = rsQuery.getString("IDENTIDAD")==null?"":rsQuery.getString("IDENTIDAD");

      }
    }
    catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo select");
      System.out.println("bcIjTcOrganismo.select: " + SQL.toString());
      throw new Exception(SQL.toString(),e);
    }
    finally {
      if (rsQuery != null) 
        rsQuery.close(); 
      rsQuery=null;
      if (stQuery != null)
        stQuery.close();
      stQuery = null;
      SQL.setLength(0);
      SQL = null;
    }         
    return resultado;
  }
  
  public void insert(Connection con) throws  Exception {
    ResultSet rsQuerySeq= null;
    Statement stQuery=null;
    Statement stQuery2=null;  
    StringBuffer SQL = null;
    try {
      getNext(con);
      //adecuaCampos();   
      SQL = new StringBuffer();
      SQL.setLength(0);
      SQL.append("insert into IJ_TC_ORGANISMO ( ");
      SQL.append("IDORGANISMO, ");
      SQL.append("ORGANISMO, ");
      SQL.append("SIGLAS, ");
      SQL.append("SECTOR, ");
      SQL.append("ESTATUS, ");
      SQL.append("PAISD, ");
      SQL.append("ENTIDADD, ");
      SQL.append("IDPAIS, ");
      SQL.append("IDENTIDAD ");
      SQL.append("\n) values (");
      SQL.append(getCampo(idorganismo)+", ");
      SQL.append(getString(organismo)+", ");
      SQL.append(getString(siglas)+", ");
      SQL.append(getString(sector)+", ");
      SQL.append(getString(estatus)+", ");
      SQL.append(getString(paisd)+", ");
      SQL.append(getString(entidadd)+", ");
      SQL.append(getString(idpais)+", ");
      SQL.append(getCampo(identidad)+")  ");
//3
      stQuery2=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      stQuery2.executeUpdate(SQL.toString());
    }
    catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo insert");
      System.out.println("bcIjTcOrganismo.insert: " + SQL);
      idorganismo = "-1";

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
    //return (Integer.parseInt(idMetaIndicador));
  }
  
  public void update(Connection con) throws  Exception {
       //adecuaCampos();
       StringBuffer SQL = new StringBuffer();
       SQL.append("update IJ_TC_ORGANISMO set \n");
       SQL.append("  IDORGANISMO = " + getCampo(idorganismo)+", \n");
       SQL.append("  ORGANISMO = " + getString(organismo)+", \n");
       SQL.append("  SIGLAS = " + getString(siglas)+", \n");
       SQL.append("  SECTOR = " + getString(sector)+", \n");
       SQL.append("  ESTATUS = " + getString(estatus)+", \n");
       SQL.append("  PAISD = " + getString(paisd)+", \n");
       SQL.append("  ENTIDADD = " + getString(entidadd)+", \n");
       SQL.append("  IDPAIS = " + getString(idpais)+", \n");
       SQL.append("  IDENTIDAD = " + getCampo(identidad)+" \n");
       SQL.append("where \n");
       SQL.append("  IDORGANISMO = " + idorganismo + "   " );
//10
       int regsAfectados =-1;
       Statement stQuery=null;
       try {
          stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
          regsAfectados=stQuery.executeUpdate(SQL.toString());
       }
       catch (Exception e) {
          System.out.println("Ha ocurrido un error en el metodo update");
          System.out.println("bcIjTcOrganismo.update: "+SQL);
          throw new Exception(SQL.toString(),e);
       }
       finally {
          if (stQuery != null) {
             stQuery.close();
             stQuery=null;
          }           
       }
  }
  
  public void delete(Connection con) throws  Exception {
      int rs=-1;
      Statement stQuery=null;
      String SQL = "";
      try {
        stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        SQL = "delete from IJ_TC_ORGANISMO where IDORGANISMO = " + idorganismo + " " ;
        rs=stQuery.executeUpdate(SQL);
      }
     catch (Exception e) {
        System.out.println("Ha occurrido un error en el metodo delete");
        System.out.println("bcIjTcOrganismo.delete: "+SQL);         
        throw new Exception(SQL.toString(),e);
      }
      finally {                  
        if (stQuery != null) {
          stQuery.close();
          stQuery=null;
        }                         
      }
  }
  
  public String toString() { 
    StringBuffer sb = new StringBuffer();
		sb.append(getEntidadd());
		sb.append(",");
		sb.append(getSiglas());
		sb.append(",");
		sb.append(getIdpais());
		sb.append(",");
		sb.append(getOrganismo());
		sb.append(",");
		sb.append(getSector());
		sb.append(",");
		sb.append(getEstatus());
		sb.append(",");
		sb.append(getIdentidad());
		sb.append(",");
		sb.append(getIdorganismo());
		sb.append(",");
		sb.append(getPaisd());
    return sb.toString();
  }
  
}
