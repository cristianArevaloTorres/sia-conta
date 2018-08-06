package sia.rf.contabilidad.registroContableNuevo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import sia.configuracion.BeanBase;

public class bcRfTrDocumentosFirmas extends BeanBase {
		
  private String documentoFirmaId;
	private String firma;
	private String numEmpleado;
	private String documentoContableId;
  
	public bcRfTrDocumentosFirmas() {
		limpia();
	}
	
  public void setDocumentoFirmaId(String documentoFirmaId) {
		this.documentoFirmaId = documentoFirmaId;
	}

	public String getDocumentoFirmaId() {
		return this.documentoFirmaId;
	}

	public void setFirma(String firma) {
		this.firma = firma;
	}

	public String getFirma() {
		return this.firma;
	}

	public void setNumEmpleado(String numEmpleado) {
		this.numEmpleado = numEmpleado;
	}

	public String getNumEmpleado() {
		return this.numEmpleado;
	}

	public void setDocumentoContableId(String documentoContableId) {
		this.documentoContableId = documentoContableId;
	}

	public String getDocumentoContableId() {
		return this.documentoContableId;
	}
  
  private void limpia(){
		documentoFirmaId = "";
		firma = "";
		numEmpleado = "";
		documentoContableId = "";

  }
  
  private void adecuaCampos() {
		documentoFirmaId = ((documentoFirmaId==null)||(documentoFirmaId.equals("")))? "null" : documentoFirmaId;
		firma = ((firma==null)||(firma.equals("")))? "null" : "'" +firma+ "'";
		numEmpleado = ((numEmpleado==null)||(numEmpleado.equals("")))? "null" : numEmpleado;
		documentoContableId = ((documentoContableId==null)||(documentoContableId.equals("")))? "null" : documentoContableId;

  }
  
  private void getNext(Connection con) throws  Exception {
    Statement stQuery=null;
    StringBuffer SQL = null;
    ResultSet rsQuerySeq = null;
    try {
      stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      SQL = new StringBuffer("select SEQ_RF_TR_DOCUMENTOS_FIRMAS.nextval valoractual from dual");
      rsQuerySeq = stQuery.executeQuery(SQL.toString());
      while (rsQuerySeq.next()){
        documentoFirmaId = rsQuerySeq.getString("valoractual");
      } //del while
      if(documentoFirmaId == null)
        documentoFirmaId = "1";

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
			SQL.append("  DOCUMENTO_FIRMA_ID, \n");
			SQL.append("  FIRMA, \n");
			SQL.append("  NUM_EMPLEADO, \n");
			SQL.append("  DOCUMENTO_CONTABLE_ID \n");
			SQL.append("from \n"); 
			SQL.append("  rf_tr_documentos_firmas\n");
			SQL.append("where \n");
			SQL.append("  DOCUMENTO_FIRMA_ID = " + documentoFirmaId + "  ");

    Statement stQuery = null;
    ResultSet rsQuery = null;
    int resultado = 0;
    try {
      stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      rsQuery = stQuery.executeQuery(SQL.toString());
      
      rsQuery.beforeFirst();
      if (rsQuery.next() )  {
        
        limpia();
        
              documentoFirmaId = rsQuery.getString("DOCUMENTO_FIRMA_ID")==null?"":rsQuery.getString("DOCUMENTO_FIRMA_ID");
              firma = rsQuery.getString("FIRMA")==null?"":rsQuery.getString("FIRMA");
              numEmpleado = rsQuery.getString("NUM_EMPLEADO")==null?"":rsQuery.getString("NUM_EMPLEADO");
              documentoContableId = rsQuery.getString("DOCUMENTO_CONTABLE_ID")==null?"":rsQuery.getString("DOCUMENTO_CONTABLE_ID");

      }
    }
    catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo select");
      System.out.println("bcRfTrDocumentosFirmas.select: " + SQL.toString());
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
      SQL.append("insert into rf_tr_documentos_firmas ( ");
      SQL.append("DOCUMENTO_FIRMA_ID, ");
      SQL.append("FIRMA, ");
      SQL.append("NUM_EMPLEADO, ");
      SQL.append("DOCUMENTO_CONTABLE_ID ");
      SQL.append("\n) values (");
      SQL.append(documentoFirmaId + ", ");
      SQL.append(firma + ", ");
      SQL.append(numEmpleado + ", ");
      SQL.append(documentoContableId + ")  ");
//3
      stQuery2=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      stQuery2.executeUpdate(SQL.toString());
    }
    catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo insert");
      System.out.println("bcRfTrDocumentosFirmas.insert: " + SQL);
      documentoFirmaId = "-1";

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
       SQL.append("update rf_tr_documentos_firmas set \n");
       SQL.append("  DOCUMENTO_FIRMA_ID = " + documentoFirmaId + " , \n");
       SQL.append("  FIRMA = " + firma + " , \n");
       SQL.append("  NUM_EMPLEADO = " + numEmpleado + " , \n");
       SQL.append("  DOCUMENTO_CONTABLE_ID = " + documentoContableId + "  \n");
       SQL.append("where \n");
       SQL.append("  DOCUMENTO_FIRMA_ID = " + documentoFirmaId + "   " );
//10
       int regsAfectados =-1;
       Statement stQuery=null;
       try {
          stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
          regsAfectados=stQuery.executeUpdate(SQL.toString());
       }
       catch (Exception e) {
          System.out.println("Ha ocurrido un error en el metodo update");
          System.out.println("bcRfTrDocumentosFirmas.update: "+SQL);
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
        SQL = "delete from rf_tr_documentos_firmas where DOCUMENTO_FIRMA_ID = " + documentoFirmaId + " " ;
        rs=stQuery.executeUpdate(SQL);
      }
     catch (Exception e) {
        System.out.println("Ha occurrido un error en el metodo delete");
        System.out.println("bcRfTrDocumentosFirmas.delete: "+SQL);         
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
		sb.append(getNumEmpleado());
		sb.append(",");
		sb.append(getDocumentoContableId());
		sb.append(",");
		sb.append(getFirma());
		sb.append(",");
		sb.append(getDocumentoFirmaId());
    return sb.toString();
  }
  
}
