package sia.ij.bd;

import sia.configuracion.BeanBase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;


public class bcIjTcFirmantes extends BeanBase {
		
  private String idfirmante;
	private String nombre;
	private String apellidopat;
	private String apellidomat;
	private String cargo;
	private String fecactualiza;
	private String estatus;
	private String idtitulo;
	private String idareas;
  
	public bcIjTcFirmantes() {
		limpia();
	}
	
  public void setIdfirmante(String idfirmante) {
		this.idfirmante = idfirmante;
	}

	public String getIdfirmante() {
		return this.idfirmante;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre() {
		return this.nombre;
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

	public void setIdareas(String idareas) {
		this.idareas = idareas;
	}

	public String getIdareas() {
		return this.idareas;
	}
  
  private void limpia(){
		idfirmante = "";
		nombre = "";
		apellidopat = "";
		apellidomat = "";
		cargo = "";
		fecactualiza = "";
		estatus = "";
		idtitulo = "";
		idareas = "";

  }
  
  /*private void adecuaCampos() {

  }*/
  
  private void getNext(Connection con) throws  Exception {
    Statement stQuery=null;
    StringBuffer SQL = null;
    ResultSet rsQuerySeq = null;
    try {
      stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      SQL = new StringBuffer("select max(IDFIRMANTE) + 1 valoractual from IJ_TC_FIRMANTES");
      rsQuerySeq = stQuery.executeQuery(SQL.toString());
      while (rsQuerySeq.next()){
        idfirmante = rsQuerySeq.getString("valoractual");
      } //del while
      if(idfirmante == null)
        idfirmante = "1";

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
			SQL.append("  IDFIRMANTE, \n");
			SQL.append("  NOMBRE, \n");
			SQL.append("  APELLIDOPAT, \n");
			SQL.append("  APELLIDOMAT, \n");
			SQL.append("  CARGO, \n");
			SQL.append("  to_char(FECACTUALIZA,'dd/mm/yyyy') FECACTUALIZA, \n");
			SQL.append("  ESTATUS, \n");
			SQL.append("  IDTITULO, \n");
			SQL.append("  IDAREAS \n");
			SQL.append("from \n"); 
			SQL.append("  IJ_TC_FIRMANTES\n");
			SQL.append("where \n");
			SQL.append("  IDFIRMANTE = " + idfirmante + "  ");

    Statement stQuery = null;
    ResultSet rsQuery = null;
    int resultado = 0;
    try {
      stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      rsQuery = stQuery.executeQuery(SQL.toString());
      
      rsQuery.beforeFirst();
      if (rsQuery.next() )  {
        
        limpia();
        
              idfirmante = rsQuery.getString("IDFIRMANTE")==null?"":rsQuery.getString("IDFIRMANTE");
              nombre = rsQuery.getString("NOMBRE")==null?"":rsQuery.getString("NOMBRE");
              apellidopat = rsQuery.getString("APELLIDOPAT")==null?"":rsQuery.getString("APELLIDOPAT");
              apellidomat = rsQuery.getString("APELLIDOMAT")==null?"":rsQuery.getString("APELLIDOMAT");
              cargo = rsQuery.getString("CARGO")==null?"":rsQuery.getString("CARGO");
              fecactualiza = rsQuery.getString("FECACTUALIZA")==null?"":rsQuery.getString("FECACTUALIZA");
              estatus = rsQuery.getString("ESTATUS")==null?"":rsQuery.getString("ESTATUS");
              idtitulo = rsQuery.getString("IDTITULO")==null?"":rsQuery.getString("IDTITULO");
              idareas = rsQuery.getString("IDAREAS")==null?"":rsQuery.getString("IDAREAS");

      }
    }
    catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo select");
      System.out.println("bcIjTcFirmantes.select: " + SQL.toString());
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
      SQL.append("insert into IJ_TC_FIRMANTES ( ");
      SQL.append("IDFIRMANTE, ");
      SQL.append("NOMBRE, ");
      SQL.append("APELLIDOPAT, ");
      SQL.append("APELLIDOMAT, ");
      SQL.append("CARGO, ");
      SQL.append("FECACTUALIZA, ");
      SQL.append("ESTATUS, ");
      SQL.append("IDTITULO, ");
      SQL.append("IDAREAS ");
      SQL.append("\n) values (");
      SQL.append(getCampo(idfirmante)+", ");
      SQL.append(getString(nombre)+", ");
      SQL.append(getString(apellidopat)+", ");
      SQL.append(getString(apellidomat)+", ");
      SQL.append(getString(cargo)+", ");
      SQL.append(getDate(fecactualiza)+", ");
      SQL.append(getString(estatus)+", ");
      SQL.append(getCampo(idtitulo)+", ");
      SQL.append(getCampo(idareas)+")  ");
System.out.println("insert into IJ_TC_FIRMANTES query "+SQL.toString());
      stQuery2=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      stQuery2.executeUpdate(SQL.toString());
    }
    catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo insert");
      System.out.println("bcIjTcFirmantes.insert: " + SQL);
      idfirmante = "-1";

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
       SQL.append("update IJ_TC_FIRMANTES set \n");
       SQL.append("  IDFIRMANTE = " + getCampo(idfirmante)+", \n");
       SQL.append("  NOMBRE = " + getString(nombre)+", \n");
       SQL.append("  APELLIDOPAT = " + getString(apellidopat)+", \n");
       SQL.append("  APELLIDOMAT = " + getString(apellidomat)+", \n");
       SQL.append("  CARGO = " + getString(cargo)+", \n");
       SQL.append("  FECACTUALIZA = to_char(sysdate,'dd/mm/yyyy'), \n");
       SQL.append("  ESTATUS = " + getString(estatus)+", \n");
       SQL.append("  IDTITULO = " + getCampo(idtitulo)+", \n");
       SQL.append("  IDAREAS = " + getCampo(idareas)+" \n");
       SQL.append("where \n");
       SQL.append("  IDFIRMANTE = " + idfirmante + "   " );
//10
       int regsAfectados =-1;
       Statement stQuery=null;
       try {
          stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
          regsAfectados=stQuery.executeUpdate(SQL.toString());
       }
       catch (Exception e) {
          System.out.println("Ha ocurrido un error en el metodo update");
          System.out.println("bcIjTcFirmantes.update: "+SQL);
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
        SQL = "delete from IJ_TC_FIRMANTES where IDFIRMANTE = " + idfirmante + " " ;
        rs=stQuery.executeUpdate(SQL);
      }
     catch (Exception e) {
        System.out.println("Ha occurrido un error en el metodo delete");
        System.out.println("bcIjTcFirmantes.delete: "+SQL);         
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
		sb.append(getNombre());
		sb.append(",");
		sb.append(getFecactualiza());
		sb.append(",");
		sb.append(getEstatus());
		sb.append(",");
		sb.append(getIdfirmante());
		sb.append(",");
		sb.append(getIdtitulo());
		sb.append(",");
		sb.append(getApellidopat());
		sb.append(",");
		sb.append(getCargo());
		sb.append(",");
		sb.append(getIdareas());
    return sb.toString();
  }
  
}
