package sia.rf.contabilidad.registroContableNuevo.formas;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import sia.configuracion.BeanBase;

public class bcRfTcConfiguraForma extends BeanBase {
		
  private String configuraFormaId;
	private String codigo;
	private String nivel;
	private String esvariable;
	private String secuenciaFormaId;
	private String signo;
  
	public bcRfTcConfiguraForma(String configuraFormaId, String codigo, String nivel, String esvariable,  
                              String secuenciaFormaId, String signo) {
    setConfiguraFormaId(configuraFormaId);
    setCodigo(codigo);
    setNivel(nivel);
    setEsvariable(esvariable);
    setSecuenciaFormaId(secuenciaFormaId);
    setSigno(signo);
  }
  
  public bcRfTcConfiguraForma() {
		limpia();
	}
	
  public void setConfiguraFormaId(String configuraFormaId) {
		this.configuraFormaId = configuraFormaId;
	}

	public String getConfiguraFormaId() {
		return this.configuraFormaId;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getCodigo() {
		return this.codigo;
	}

	public void setNivel(String nivel) {
		this.nivel = nivel;
	}

	public String getNivel() {
		return this.nivel;
	}

	public void setEsvariable(String esvariable) {
		this.esvariable = esvariable;
	}

	public String getEsvariable() {
		return this.esvariable;
	}

	public void setSecuenciaFormaId(String secuenciaFormaId) {
		this.secuenciaFormaId = secuenciaFormaId;
	}

	public String getSecuenciaFormaId() {
		return this.secuenciaFormaId;
	}

	public void setSigno(String signo) {
		this.signo = signo;
	}

	public String getSigno() {
		return this.signo;
	}
  
  private void limpia(){
		configuraFormaId = "";
		codigo = "";
		nivel = "";
		esvariable = "";
		secuenciaFormaId = "";
		signo = "";

  }
  
  private void adecuaCampos() {
		configuraFormaId = ((configuraFormaId==null)||(configuraFormaId.equals("")))? "null" : configuraFormaId;
		codigo = ((codigo==null)||(codigo.equals("")))? "null" : "'" +codigo+ "'";
		nivel = ((nivel==null)||(nivel.equals("")))? "null" : nivel;
		esvariable = ((esvariable==null)||(esvariable.equals("")))? "null" : esvariable;
		secuenciaFormaId = ((secuenciaFormaId==null)||(secuenciaFormaId.equals("")))? "null" : secuenciaFormaId;
		signo = ((signo==null)||(signo.equals("")))? "null" : signo;

  }
  
  private void getNext(Connection con) throws  Exception {
    Statement stQuery=null;
    StringBuffer SQL = null;
    ResultSet rsQuerySeq = null;
    try {
      stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      SQL = new StringBuffer("select SEQ_RF_TC_CONFIGURA_FORMA.nextval valoractual from dual");
      rsQuerySeq = stQuery.executeQuery(SQL.toString());
      while (rsQuerySeq.next()){
        configuraFormaId = rsQuerySeq.getString("valoractual");
      } //del while
      if(configuraFormaId == null)
        configuraFormaId = "1";

    }
        catch(Exception e) {
      throw e;
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
			SQL.append("  CONFIGURA_FORMA_ID, \n");
			SQL.append("  CODIGO, \n");
			SQL.append("  NIVEL, \n");
			SQL.append("  ESVARIABLE, \n");
			SQL.append("  SECUENCIA_FORMA_ID, \n");
			SQL.append("  SIGNO \n");
			SQL.append("from \n"); 
			SQL.append("  RF_TC_CONFIGURA_FORMA\n");
			SQL.append("where \n");
			SQL.append("  CONFIGURA_FORMA_ID = " + configuraFormaId + "  ");

    Statement stQuery = null;
    ResultSet rsQuery = null;
    int resultado = 0;
    try {
      stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      rsQuery = stQuery.executeQuery(SQL.toString());
      
      rsQuery.beforeFirst();
      if (rsQuery.next() )  {
        
        limpia();
        
              configuraFormaId = rsQuery.getString("CONFIGURA_FORMA_ID")==null?"":rsQuery.getString("CONFIGURA_FORMA_ID");
              codigo = rsQuery.getString("CODIGO")==null?"":rsQuery.getString("CODIGO");
              nivel = rsQuery.getString("NIVEL")==null?"":rsQuery.getString("NIVEL");
              esvariable = rsQuery.getString("ESVARIABLE")==null?"":rsQuery.getString("ESVARIABLE");
              secuenciaFormaId = rsQuery.getString("SECUENCIA_FORMA_ID")==null?"":rsQuery.getString("SECUENCIA_FORMA_ID");
              signo = rsQuery.getString("SIGNO")==null?"":rsQuery.getString("SIGNO");

      }
    }
    catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo select");
      System.out.println("bcRfTcConfiguraForma.select: " + SQL.toString());
      throw e;
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
      adecuaCampos();   
      SQL = new StringBuffer();
      SQL.setLength(0);
      SQL.append("insert into RF_TC_CONFIGURA_FORMA ( ");
      SQL.append("CONFIGURA_FORMA_ID, ");
      SQL.append("CODIGO, ");
      SQL.append("NIVEL, ");
      SQL.append("ESVARIABLE, ");
      SQL.append("SECUENCIA_FORMA_ID, ");
      SQL.append("SIGNO ");
      SQL.append("\n) values (");
      SQL.append(configuraFormaId + ", ");
      SQL.append(codigo + ", ");
      SQL.append(nivel + ", ");
      SQL.append(esvariable + ", ");
      SQL.append(secuenciaFormaId + ", ");
      SQL.append(signo + ")  ");
//3
      stQuery2=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      stQuery2.executeUpdate(SQL.toString());
    }
    catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo insert");
      System.out.println("bcRfTcConfiguraForma.insert: " + SQL);
      configuraFormaId = "-1";

      throw e;
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
       adecuaCampos();
       StringBuffer SQL = new StringBuffer();
       SQL.append("update RF_TC_CONFIGURA_FORMA set \n");
       SQL.append("  CONFIGURA_FORMA_ID = " + configuraFormaId + " , \n");
       SQL.append("  CODIGO = " + codigo + " , \n");
       SQL.append("  NIVEL = " + nivel + " , \n");
       SQL.append("  ESVARIABLE = " + esvariable + " , \n");
       SQL.append("  SECUENCIA_FORMA_ID = " + secuenciaFormaId + " , \n");
       SQL.append("  SIGNO = " + signo + "  \n");
       SQL.append("where \n");
       SQL.append("  CONFIGURA_FORMA_ID = " + configuraFormaId + "   " );
//10
       int regsAfectados =-1;
       Statement stQuery=null;
       try {
          stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
          regsAfectados=stQuery.executeUpdate(SQL.toString());
       }
       catch (Exception e) {
          System.out.println("Ha ocurrido un error en el metodo update");
          System.out.println("bcRfTcConfiguraForma.update: "+SQL);
          throw e;
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
        SQL = "delete from RF_TC_CONFIGURA_FORMA where CONFIGURA_FORMA_ID = " + configuraFormaId + " " ;
        rs=stQuery.executeUpdate(SQL);
      }
     catch (Exception e) {
        System.out.println("Ha occurrido un error en el metodo delete");
        System.out.println("bcRfTcConfiguraForma.delete: "+SQL);         
        throw e;
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
		sb.append(getSigno());
		sb.append(",");
		sb.append(getCodigo());
		sb.append(",");
		sb.append(getNivel());
		sb.append(",");
		sb.append(getEsvariable());
		sb.append(",");
		sb.append(getConfiguraFormaId());
		sb.append(",");
		sb.append(getSecuenciaFormaId());
    return sb.toString();
  }
  
}
