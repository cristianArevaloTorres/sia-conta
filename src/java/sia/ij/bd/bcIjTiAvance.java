package sia.ij.bd;

import sia.configuracion.BeanBase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;


public class bcIjTiAvance extends BeanBase {
		
  private String idavance;
	private String fecnotifica;
	private String avance;
	private String porcentaje;
	private String fecalta;
	private String feclibera;
	private String estatus;
	private String idactividad;
	private String iddregional;
  
	public bcIjTiAvance() {
		limpia();
	}
	
  public void setIdavance(String idavance) {
		this.idavance = idavance;
	}

	public String getIdavance() {
		return this.idavance;
	}

	public void setFecnotifica(String fecnotifica) {
		this.fecnotifica = fecnotifica;
	}

	public String getFecnotifica() {
		return this.fecnotifica;
	}

	public void setAvance(String avance) {
		this.avance = avance;
	}

	public String getAvance() {
		return this.avance;
	}

	public void setPorcentaje(String porcentaje) {
		this.porcentaje = porcentaje;
	}

	public String getPorcentaje() {
		return this.porcentaje;
	}

	public void setFecalta(String fecalta) {
		this.fecalta = fecalta;
	}

	public String getFecalta() {
		return this.fecalta;
	}

	public void setFeclibera(String feclibera) {
		this.feclibera = feclibera;
	}

	public String getFeclibera() {
		return this.feclibera;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public String getEstatus() {
		return this.estatus;
	}

	public void setIdactividad(String idactividad) {
		this.idactividad = idactividad;
	}

	public String getIdactividad() {
		return this.idactividad;
	}

	public void setIddregional(String iddregional) {
		this.iddregional = iddregional;
	}

	public String getIddregional() {
		return this.iddregional;
	}
  
  private void limpia(){
		idavance = "";
		fecnotifica = "";
		avance = "";
		porcentaje = "";
		fecalta = "";
		feclibera = "";
		estatus = "";
		idactividad = "";
		iddregional = "";

  }
  
  /*private void adecuaCampos() {

  }*/
  
  private void getNext(Connection con) throws  Exception {
    Statement stQuery=null;
    StringBuffer SQL = null;
    ResultSet rsQuerySeq = null;
    try {
      stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      SQL = new StringBuffer("select max(IDAVANCE) + 1 valoractual from IJ_TI_AVANCE");
      rsQuerySeq = stQuery.executeQuery(SQL.toString());
      while (rsQuerySeq.next()){
        idavance = rsQuerySeq.getString("valoractual");
      } //del while
      if(idavance == null)
        idavance = "1";

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
			SQL.append("  IDAVANCE, \n");
			SQL.append("  to_char(FECNOTIFICA,'dd/mm/yyyy') FECNOTIFICA, \n");
			SQL.append("  AVANCE, \n");
			SQL.append("  PORCENTAJE, \n");
			SQL.append("  to_char(FECALTA,'dd/mm/yyyy') FECALTA, \n");
			SQL.append("  to_char(FECLIBERA,'dd/mm/yyyy') FECLIBERA, \n");
			SQL.append("  ESTATUS, \n");
			SQL.append("  IDACTIVIDAD, \n");
			SQL.append("  IDDREGIONAL \n");
			SQL.append("from \n"); 
			SQL.append("  IJ_TI_AVANCE\n");
			SQL.append("where \n");
			SQL.append("  IDAVANCE = " + idavance + "  ");

    Statement stQuery = null;
    ResultSet rsQuery = null;
    int resultado = 0;
    try {
      stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      rsQuery = stQuery.executeQuery(SQL.toString());
      
      rsQuery.beforeFirst();
      if (rsQuery.next() )  {
        
        limpia();
        
              idavance = rsQuery.getString("IDAVANCE")==null?"":rsQuery.getString("IDAVANCE");
              fecnotifica = rsQuery.getString("FECNOTIFICA")==null?"":rsQuery.getString("FECNOTIFICA");
              avance = rsQuery.getString("AVANCE")==null?"":rsQuery.getString("AVANCE");
              porcentaje = rsQuery.getString("PORCENTAJE")==null?"":rsQuery.getString("PORCENTAJE");
              fecalta = rsQuery.getString("FECALTA")==null?"":rsQuery.getString("FECALTA");
              feclibera = rsQuery.getString("FECLIBERA")==null?"":rsQuery.getString("FECLIBERA");
              estatus = rsQuery.getString("ESTATUS")==null?"":rsQuery.getString("ESTATUS");
              idactividad = rsQuery.getString("IDACTIVIDAD")==null?"":rsQuery.getString("IDACTIVIDAD");
              iddregional = rsQuery.getString("IDDREGIONAL")==null?"":rsQuery.getString("IDDREGIONAL");

      }
    }
    catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo select");
      System.out.println("bcIjTiAvance.select: " + SQL.toString());
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
      SQL.append("insert into IJ_TI_AVANCE ( ");
      SQL.append("IDAVANCE, ");
      SQL.append("FECNOTIFICA, ");
      SQL.append("AVANCE, ");
      SQL.append("PORCENTAJE, ");
      SQL.append("FECALTA, ");
      SQL.append("FECLIBERA, ");
      SQL.append("ESTATUS, ");
      SQL.append("IDACTIVIDAD, ");
      SQL.append("IDDREGIONAL ");
      SQL.append("\n) values (");
      SQL.append(getCampo(idavance)+", ");
      SQL.append(getDate(fecnotifica)+", ");
      SQL.append(getString(avance)+", ");
      SQL.append(getCampo(porcentaje)+", ");
      SQL.append(getDate(fecalta)+", ");
      SQL.append(getDate(feclibera)+", ");
      SQL.append(getString(estatus)+", ");
      SQL.append(getCampo(idactividad)+", ");
      SQL.append(getCampo(iddregional)+")  ");
//3
      stQuery2=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      stQuery2.executeUpdate(SQL.toString());
    }
    catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo insert");
      System.out.println("bcIjTiAvance.insert: " + SQL);
      idavance = "-1";

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
       SQL.append("update IJ_TI_AVANCE set \n");
       SQL.append("  IDAVANCE = " + getCampo(idavance)+", \n");
       SQL.append("  FECNOTIFICA = " + getDate(fecnotifica)+", \n");
       SQL.append("  AVANCE = " + getString(avance)+", \n");
       SQL.append("  PORCENTAJE = " + getCampo(porcentaje)+", \n");
       SQL.append("  FECALTA = " + getDate(fecalta)+", \n");
       SQL.append("  FECLIBERA = " + getDate(feclibera)+", \n");
       SQL.append("  ESTATUS = " + getString(estatus)+", \n");
       SQL.append("  IDACTIVIDAD = " + getCampo(idactividad)+", \n");
       SQL.append("  IDDREGIONAL = " + getCampo(iddregional)+" \n");
       SQL.append("where \n");
       SQL.append("  IDAVANCE = " + idavance + "   " );
//10
       int regsAfectados =-1;
       Statement stQuery=null;
       try {
          stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
          regsAfectados=stQuery.executeUpdate(SQL.toString());
       }
       catch (Exception e) {
          System.out.println("Ha ocurrido un error en el metodo update");
          System.out.println("bcIjTiAvance.update: "+SQL);
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
        SQL = "delete from IJ_TI_AVANCE where IDAVANCE = " + idavance + " " ;
        rs=stQuery.executeUpdate(SQL);
      }
     catch (Exception e) {
        System.out.println("Ha occurrido un error en el metodo delete");
        System.out.println("bcIjTiAvance.delete: "+SQL);         
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
		sb.append(getIddregional());
		sb.append(",");
		sb.append(getIdactividad());
		sb.append(",");
		sb.append(getPorcentaje());
		sb.append(",");
		sb.append(getFecnotifica());
		sb.append(",");
		sb.append(getEstatus());
		sb.append(",");
		sb.append(getFeclibera());
		sb.append(",");
		sb.append(getFecalta());
		sb.append(",");
		sb.append(getIdavance());
		sb.append(",");
		sb.append(getAvance());
    return sb.toString();
  }
  
}
