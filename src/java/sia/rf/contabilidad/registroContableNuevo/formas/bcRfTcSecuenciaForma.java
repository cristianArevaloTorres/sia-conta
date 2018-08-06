package sia.rf.contabilidad.registroContableNuevo.formas;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import sia.configuracion.BeanBase;

import sia.db.dao.DaoFactory;

// Clase que reliza las operaciones basicas(Altas,Bajas y Cambios) sobre la tabla
// RF_TC_SECUENCIA_FORMA
public class bcRfTcSecuenciaForma extends BeanBase {
	
  private String secuenciaFormaId;
  private String operacionContableId;
  private String formaContableId;
  private String crearCuentaContable;
  private String acumularImporte;
  private String movible;
  
  public bcRfTcSecuenciaForma() {
    limpia();
  }
	
  public void setSecuenciaFormaId(String secuenciaFormaId) {
    this.secuenciaFormaId = secuenciaFormaId;
  }

  public String getSecuenciaFormaId() {
    return this.secuenciaFormaId;
  }

  public void setOperacionContableId(String operacionContableId) {
    this.operacionContableId = operacionContableId;
  }

  public String getOperacionContableId() {
    return this.operacionContableId;
  }

  public void setFormaContableId(String formaContableId) {
    this.formaContableId = formaContableId;
  }

  public String getFormaContableId() {
    return this.formaContableId;
  }

  public void setCrearCuentaContable(String crearCuentaContable) {
    this.crearCuentaContable = crearCuentaContable;
  }

  public String getCrearCuentaContable() {
    return this.crearCuentaContable;
  }

  public void setAcumularImporte(String acumularImporte) {
    this.acumularImporte = acumularImporte;
  }

  public String getAcumularImporte() {
    return this.acumularImporte;
  }

  public void setMovible(String movible) {
    this.movible = movible;
  }

  public String getMovible() {
    return this.movible;
  }
  
  private void limpia(){
    secuenciaFormaId    = "";
    operacionContableId = "";
    formaContableId     = "";
    crearCuentaContable = "";
    acumularImporte     = "";
    movible             = "";
  }
  
  private void adecuaCampos() {
    secuenciaFormaId = ((secuenciaFormaId==null)||(secuenciaFormaId.equals("")))? "null" : secuenciaFormaId;
    operacionContableId = ((operacionContableId==null)||(operacionContableId.equals("")))? "null" : operacionContableId;
    formaContableId = ((formaContableId==null)||(formaContableId.equals("")))? "null" : formaContableId;
    crearCuentaContable = ((crearCuentaContable==null)||(crearCuentaContable.equals("")))? "null" : crearCuentaContable;
    acumularImporte = ((acumularImporte==null)||(acumularImporte.equals("")))? "null" : acumularImporte;
    movible = ((movible==null)||(movible.equals("")))? "null" : movible;
  }
  
  //Metodo que hace una consulta para saber el número de secuencia(consecutivo) con el cual se insertara 
  //un id de registro de la cuenta contable para la forma
  private void getNext(Connection con) throws  Exception {
    Statement stQuery=null;
    StringBuffer SQL = null;
    ResultSet rsQuerySeq = null;
    try {
      stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      SQL = new StringBuffer("select SEQ_RF_TC_SECUENCIA_FORMA.nextval valoractual from dual");
      rsQuerySeq = stQuery.executeQuery(SQL.toString());
      while (rsQuerySeq.next()){
        secuenciaFormaId = rsQuerySeq.getString("valoractual");
      } //del while
      if(secuenciaFormaId == null)
        secuenciaFormaId = "1";

    } catch(Exception e) {
      throw e;
    } finally {
      SQL.setLength(0);
      if(rsQuerySeq != null)
        rsQuerySeq.close();
      rsQuerySeq = null;
      if(stQuery != null)
        stQuery.close();
      stQuery = null;
    }
  }
  
  //Selecciona un registro de la tabla RF_TC_SECUENCIA_FORMA en base al atributo
  //SECUENCIA_FORMA_ID
  public int select(Connection con) throws Exception {
    StringBuffer SQL;
    SQL = new StringBuffer();
          SQL.append("select \n");
          SQL.append("  SECUENCIA_FORMA_ID, \n");
          SQL.append("  OPERACION_CONTABLE_ID, \n");
	  SQL.append("  FORMA_CONTABLE_ID, \n");
          SQL.append("  CREAR_CUENTA_CONTABLE, \n");
          SQL.append("  ACUMULAR_IMPORTE, \n");
          SQL.append("  MOVIBLE \n");
	  SQL.append("from \n"); 
	  SQL.append("  RF_TC_SECUENCIA_FORMA\n");
	  SQL.append("where \n");
	  SQL.append("  SECUENCIA_FORMA_ID = " + secuenciaFormaId + "  ");
    Statement stQuery = null;
    ResultSet rsQuery = null;
    int resultado = 0;
    try {
      stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      rsQuery = stQuery.executeQuery(SQL.toString());
      rsQuery.beforeFirst();
      if (rsQuery.next() )  {
          limpia();
          secuenciaFormaId = rsQuery.getString("SECUENCIA_FORMA_ID")==null?"":rsQuery.getString("SECUENCIA_FORMA_ID");
          operacionContableId = rsQuery.getString("OPERACION_CONTABLE_ID")==null?"":rsQuery.getString("OPERACION_CONTABLE_ID");
          formaContableId = rsQuery.getString("FORMA_CONTABLE_ID")==null?"":rsQuery.getString("FORMA_CONTABLE_ID");
          crearCuentaContable = rsQuery.getString("CREAR_CUENTA_CONTABLE")==null?"":rsQuery.getString("CREAR_CUENTA_CONTABLE");
          acumularImporte = rsQuery.getString("ACUMULAR_IMPORTE")==null?"":rsQuery.getString("ACUMULAR_IMPORTE");
          movible = rsQuery.getString("MOVIBLE")==null?"":rsQuery.getString("MOVIBLE");
      }
    } catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo select");
      System.out.println("bcRfTcSecuenciaForma.select: " + SQL.toString());
      throw e;
    } finally {
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
  
  //Inserta un nuevo registro en la tabla SEQ_RF_TC_SECUENCIA_FORMA
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
      SQL.append("insert into RF_TC_SECUENCIA_FORMA ( ");
      SQL.append("SECUENCIA_FORMA_ID, ");
      SQL.append("OPERACION_CONTABLE_ID, ");
      SQL.append("FORMA_CONTABLE_ID, ");
      SQL.append("CREAR_CUENTA_CONTABLE, ");
      SQL.append("ACUMULAR_IMPORTE, ");
      SQL.append("MOVIBLE ");
      SQL.append("\n) values (");
      SQL.append(secuenciaFormaId + ", ");
      SQL.append(operacionContableId + ", ");
      SQL.append(formaContableId + ", ");
      SQL.append(crearCuentaContable + ", ");
      SQL.append(acumularImporte + ", ");
      SQL.append(movible + ")  ");
//3
      stQuery2=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      stQuery2.executeUpdate(SQL.toString());
    }
    catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo insert");
      System.out.println("bcRfTcSecuenciaForma.insert: " + SQL);
      secuenciaFormaId = "-1";

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
  
  //Actualiza un registro de la tabla SEQ_RF_TC_SECUENCIA_FORMA
  public void update(Connection con) throws  Exception {
       adecuaCampos();
       StringBuffer SQL = new StringBuffer();
       SQL.append("update RF_TC_SECUENCIA_FORMA set \n");
       SQL.append("  SECUENCIA_FORMA_ID = " + secuenciaFormaId + " , \n");
       SQL.append("  OPERACION_CONTABLE_ID = " + operacionContableId + " , \n");
       SQL.append("  FORMA_CONTABLE_ID = " + formaContableId + " , \n");
       SQL.append("  CREAR_CUENTA_CONTABLE = " + crearCuentaContable + " , \n");
       SQL.append("  ACUMULAR_IMPORTE = " + acumularImporte + " , \n");
       SQL.append("  MOVIBLE = " + movible + "  \n");
       SQL.append("where \n");
       SQL.append("  SECUENCIA_FORMA_ID = " + secuenciaFormaId + "   " );
       System.out.println(SQL.toString());
       int regsAfectados =-1;
       Statement stQuery=null;
       try {
          stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
          regsAfectados=stQuery.executeUpdate(SQL.toString());
       }
       catch (Exception e) {
          System.out.println("Ha ocurrido un error en el metodo update");
          System.out.println("bcRfTcSecuenciaForma.update: "+SQL);
          throw e;
       }
       finally {
          if (stQuery != null) {
             stQuery.close();
             stQuery=null;
          }           
       }
  }
  
  //Elimina un registro de la tabla SEQ_RF_TC_SECUENCIA_FORMA
  public void delete(Connection con) throws  Exception {
      int rs=-1;
      Statement stQuery=null;
      String SQL = "";
      try {
        stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        SQL = "delete from RF_TC_SECUENCIA_FORMA where SECUENCIA_FORMA_ID = " + secuenciaFormaId + " " ;
        rs=stQuery.executeUpdate(SQL);
      }
     catch (Exception e) {
        System.out.println("Ha occurrido un error en el metodo delete");
        System.out.println("bcRfTcSecuenciaForma.delete: "+SQL);         
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
		sb.append(getCrearCuentaContable());
		sb.append(",");
		sb.append(getOperacionContableId());
		sb.append(",");
		sb.append(getMovible());
		sb.append(",");
		sb.append(getAcumularImporte());
		sb.append(",");
		sb.append(getSecuenciaFormaId());
		sb.append(",");
		sb.append(getFormaContableId());
    return sb.toString();
  }
  
  public void borraEnCascada(Connection con) {
    if(formaContableId != null)
      try  {
        if(getSecuenciaFormaId()!=null && !getSecuenciaFormaId().trim().equals(""))
        addParametro("secuencia",getSecuenciaFormaId());
        ejecuta(con,"formasContables.delete.configuraDeSecuencia",DaoFactory.CONEXION_CONTABILIDAD);
        delete(con);  
      } catch (Exception ex)  {
        ex.printStackTrace();
      } finally  {
      }
  }
  
}