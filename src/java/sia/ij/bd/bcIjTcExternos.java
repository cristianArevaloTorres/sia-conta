package sia.ij.bd;

import sia.configuracion.BeanBase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;


public class bcIjTcExternos extends BeanBase {
		
  private String idexternos;
	private String nombres;
	private String apellidopat;
	private String apellidomat;
	private String cargo;
	//private String entidad;
	private String fecactualiza;
	private String estatus;
	private String idtitulo;
	private String idorganismo;
  
	public bcIjTcExternos() {
		limpia();
	}
	
  public void setIdexternos(String idexternos) {
		this.idexternos = idexternos;
	}

	public String getIdexternos() {
		return this.idexternos;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getNombres() {
		return this.nombres;
	}

	public void setApellidopat(String apellidopat) {
		this.apellidopat = apellidopat;
	}

	public String getApellidopat() {
		return this.apellidopat;
	}

	public void setApellidomat(String apellidomat) {
		this.apellidomat = apellidomat;
	}

	public String getApellidomat() {
		return this.apellidomat;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	public String getCargo() {
		return this.cargo;
	}

	public void setFecactualiza(String fecactualiza) {
		this.fecactualiza = fecactualiza;
	}

	public String getFecactualiza() {
		return this.fecactualiza;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public String getEstatus() {
		return this.estatus;
	}

	public void setIdtitulo(String idtitulo) {
		this.idtitulo = idtitulo;
	}

	public String getIdtitulo() {
		return this.idtitulo;
	}

	public void setIdorganismo(String idorganismo) {
		this.idorganismo = idorganismo;
	}

	public String getIdorganismo() {
		return this.idorganismo;
	}
  
  private void limpia(){
		idexternos = "";
		nombres = "";
		apellidopat = "";
		apellidomat = "";
		cargo = "";
		fecactualiza = "";
		estatus = "";
		idtitulo = "";
		idorganismo = "";

  }
  
  /*private void adecuaCampos() {

  }*/
  
  private void getNext(Connection con) throws  Exception {
    Statement stQuery=null;
    StringBuffer SQL = null;
    ResultSet rsQuerySeq = null;
    try {
      stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      SQL = new StringBuffer("select max(IDEXTERNOS) + 1 valoractual from IJ_TC_EXTERNOS");
      rsQuerySeq = stQuery.executeQuery(SQL.toString());
      while (rsQuerySeq.next()){
        idexternos = rsQuerySeq.getString("valoractual");
      } //del while
      if(idexternos == null)
        idexternos = "1";

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
			SQL.append("  IDEXTERNOS, \n");
			SQL.append("  NOMBRES, \n");
			SQL.append("  APELLIDOPAT, \n");
			SQL.append("  APELLIDOMAT, \n");
			SQL.append("  CARGO, \n");
			SQL.append("  to_char(FECACTUALIZA,'dd/mm/yyyy') FECACTUALIZA, \n");
			SQL.append("  ESTATUS, \n");
			SQL.append("  IDTITULO, \n");
			SQL.append("  IDORGANISMO \n");
			SQL.append("from \n"); 
			SQL.append("  IJ_TC_EXTERNOS\n");
			SQL.append("where \n");
			SQL.append("  IDEXTERNOS = " + idexternos + "  ");

    Statement stQuery = null;
    ResultSet rsQuery = null;
    int resultado = 0;
    try {
      stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      rsQuery = stQuery.executeQuery(SQL.toString());
      
      rsQuery.beforeFirst();
      if (rsQuery.next() )  {
        
        limpia();
        
              idexternos = rsQuery.getString("IDEXTERNOS")==null?"":rsQuery.getString("IDEXTERNOS");
              nombres = rsQuery.getString("NOMBRES")==null?"":rsQuery.getString("NOMBRES");
              apellidopat = rsQuery.getString("APELLIDOPAT")==null?"":rsQuery.getString("APELLIDOPAT");
              apellidomat = rsQuery.getString("APELLIDOMAT")==null?"":rsQuery.getString("APELLIDOMAT");
              cargo = rsQuery.getString("CARGO")==null?"":rsQuery.getString("CARGO");
              fecactualiza = rsQuery.getString("FECACTUALIZA")==null?"":rsQuery.getString("FECACTUALIZA");
              estatus = rsQuery.getString("ESTATUS")==null?"":rsQuery.getString("ESTATUS");
              idtitulo = rsQuery.getString("IDTITULO")==null?"":rsQuery.getString("IDTITULO");
              idorganismo = rsQuery.getString("IDORGANISMO")==null?"":rsQuery.getString("IDORGANISMO");

      }
    }
    catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo select");
      System.out.println("bcIjTcExternos.select: " + SQL.toString());
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
      SQL.append("insert into IJ_TC_EXTERNOS ( ");
      SQL.append("IDEXTERNOS, ");
      SQL.append("NOMBRES, ");
      SQL.append("APELLIDOPAT, ");
      SQL.append("APELLIDOMAT, ");
      SQL.append("CARGO, ");
      SQL.append("FECACTUALIZA, ");
      SQL.append("ESTATUS, ");
      SQL.append("IDTITULO, ");
      SQL.append("IDORGANISMO ");
      SQL.append("\n) values (");
      SQL.append(getCampo(idexternos)+", ");
      SQL.append(getString(nombres)+", ");
      SQL.append(getString(apellidopat)+", ");
      SQL.append(getString(apellidomat)+", ");
      SQL.append(getString(cargo)+", ");
      SQL.append(getDate(fecactualiza)+", ");
      SQL.append(getString(estatus)+", ");
      SQL.append(getCampo(idtitulo)+", ");
      SQL.append(getCampo(idorganismo)+")  ");
//3
      stQuery2=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      stQuery2.executeUpdate(SQL.toString());
    }
    catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo insert");
      System.out.println("bcIjTcExternos.insert: " + SQL);
      idexternos = "-1";

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
       SQL.append("update IJ_TC_EXTERNOS set \n");
       SQL.append("  IDEXTERNOS = " + getCampo(idexternos)+", \n");
       SQL.append("  NOMBRES = " + getString(nombres)+", \n");
       SQL.append("  APELLIDOPAT = " + getString(apellidopat)+", \n");
       SQL.append("  APELLIDOMAT = " + getString(apellidomat)+", \n");
       SQL.append("  CARGO = " + getString(cargo)+", \n");
       SQL.append("  FECACTUALIZA = to_char(sysdate,'dd/mm/yyyy'), \n");
       SQL.append("  ESTATUS = " + getString(estatus)+", \n");
       SQL.append("  IDTITULO = " + getCampo(idtitulo)+", \n");
       SQL.append("  IDORGANISMO = " + getCampo(idorganismo)+" \n");
       SQL.append("where \n");
       SQL.append("  IDEXTERNOS = " + idexternos + "   " );
//10
       int regsAfectados =-1;
       Statement stQuery=null;
       try {
          stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
          regsAfectados=stQuery.executeUpdate(SQL.toString());
       }
       catch (Exception e) {
          System.out.println("Ha ocurrido un error en el metodo update");
          System.out.println("bcIjTcExternos.update: "+SQL);
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
        SQL = "delete from IJ_TC_EXTERNOS where IDEXTERNOS = " + idexternos + " " ;
        rs=stQuery.executeUpdate(SQL);
      }
     catch (Exception e) {
        System.out.println("Ha occurrido un error en el metodo delete");
        System.out.println("bcIjTcExternos.delete: "+SQL);         
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
		sb.append(getApellidomat());
		sb.append(",");
		sb.append(getFecactualiza());
		sb.append(",");
		sb.append(getEstatus());
		sb.append(",");
		sb.append(getIdtitulo());
		sb.append(",");
		sb.append(getApellidopat());
		sb.append(",");
		sb.append(getNombres());
		sb.append(",");
		sb.append(getIdorganismo());
		sb.append(",");
		sb.append(getCargo());
		sb.append(",");
		sb.append(getIdexternos());
    return sb.toString();
  }
  
}
