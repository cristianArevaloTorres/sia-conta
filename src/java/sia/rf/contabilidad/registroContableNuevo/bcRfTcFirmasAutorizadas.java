package sia.rf.contabilidad.registroContableNuevo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.jsp.jstl.sql.Result;

import javax.servlet.jsp.jstl.sql.ResultSupport;

import sia.configuracion.BeanBase;

public class bcRfTcFirmasAutorizadas extends BeanBase {
		
  private String numEmpleado;
	private String prefijo;
	private String nombres;
	private String apellidoPat;
	private String apellidoMat;
	private String puestoFirma;
	private String mes;
	private String ejercicio;
  
	public bcRfTcFirmasAutorizadas() {
		limpia();
	}
	
  public void setNumEmpleado(String numEmpleado) {
		this.numEmpleado = numEmpleado;
	}

	public String getNumEmpleado() {
		return this.numEmpleado;
	}

	public void setPrefijo(String prefijo) {
		this.prefijo = prefijo;
	}

	public String getPrefijo() {
		return this.prefijo;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getNombres() {
		return this.nombres;
	}

	public void setApellidoPat(String apellidoPat) {
		this.apellidoPat = apellidoPat;
	}

	public String getApellidoPat() {
		return this.apellidoPat;
	}

	public void setApellidoMat(String apellidoMat) {
		this.apellidoMat = apellidoMat;
	}

	public String getApellidoMat() {
		return this.apellidoMat;
	}

	public void setPuestoFirma(String puestoFirma) {
		this.puestoFirma = puestoFirma;
	}

	public String getPuestoFirma() {
		return this.puestoFirma;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public String getMes() {
		return this.mes;
	}

	public void setEjercicio(String ejercicio) {
		this.ejercicio = ejercicio;
	}

	public String getEjercicio() {
		return this.ejercicio;
	}
  
  private void limpia(){
		numEmpleado = "";
		prefijo = "";
		nombres = "";
		apellidoPat = "";
		apellidoMat = "";
		puestoFirma = "";
		mes = "";
		ejercicio = "";

  }
  
  private void adecuaCampos() {
		numEmpleado = ((numEmpleado==null)||(numEmpleado.equals("")))? "null" : numEmpleado;
		prefijo = ((prefijo==null)||(prefijo.equals("")))? "null" : "'" +prefijo+ "'";
		nombres = ((nombres==null)||(nombres.equals("")))? "null" : "'" +nombres+ "'";
		apellidoPat = ((apellidoPat==null)||(apellidoPat.equals("")))? "null" : "'" +apellidoPat+ "'";
		apellidoMat = ((apellidoMat==null)||(apellidoMat.equals("")))? "null" : "'" +apellidoMat+ "'";
		puestoFirma = ((puestoFirma==null)||(puestoFirma.equals("")))? "null" : "'" +puestoFirma+ "'";
		mes = ((mes==null)||(mes.equals("")))? "null" : mes;
		ejercicio = ((ejercicio==null)||(ejercicio.equals("")))? "null" : ejercicio;

  }
  
  private void getNext(Connection con) throws  Exception {
    Statement stQuery=null;
    StringBuffer SQL = null;
    ResultSet rsQuerySeq = null;
    try {
      stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      SQL = new StringBuffer("select 1 valoracutal from dual");
      rsQuerySeq = stQuery.executeQuery(SQL.toString());
//      while (rsQuerySeq.next()){
//        TODO:// Asignar llave compuesta
//      } //del while

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
    Result result;
    SQL = new StringBuffer();
			SQL.append("select \n");
			SQL.append("  NUM_EMPLEADO, \n");
			SQL.append("  PREFIJO, \n");
			SQL.append("  NOMBRES, \n");
			SQL.append("  APELLIDO_PAT, \n");
			SQL.append("  APELLIDO_MAT, \n");
			SQL.append("  PUESTO_FIRMA, \n");
			SQL.append("  MES, \n");
			SQL.append("  EJERCICIO \n");
			SQL.append("from \n"); 
			SQL.append("  rf_tc_firmas_autorizadas\n");
			SQL.append("where \n");
			SQL.append("  EJERCICIO = " + ejercicio + " and ");
			SQL.append("  MES = " + mes + " and ");
			SQL.append("  NUM_EMPLEADO = " + numEmpleado + "  ");

    Statement stQuery = null;
    ResultSet rsQuery = null;
    int resultado = 0;
    try {
      stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      rsQuery = stQuery.executeQuery(SQL.toString());
      result = ResultSupport.toResult(rsQuery);
      resultado = result.getRowCount();
      rsQuery.beforeFirst();
      if (rsQuery.next() )  {
        
        limpia();
        
              numEmpleado = rsQuery.getString("NUM_EMPLEADO")==null?"":rsQuery.getString("NUM_EMPLEADO");
              prefijo = rsQuery.getString("PREFIJO")==null?"":rsQuery.getString("PREFIJO");
              nombres = rsQuery.getString("NOMBRES")==null?"":rsQuery.getString("NOMBRES");
              apellidoPat = rsQuery.getString("APELLIDO_PAT")==null?"":rsQuery.getString("APELLIDO_PAT");
              apellidoMat = rsQuery.getString("APELLIDO_MAT")==null?"":rsQuery.getString("APELLIDO_MAT");
              puestoFirma = rsQuery.getString("PUESTO_FIRMA")==null?"":rsQuery.getString("PUESTO_FIRMA");
              mes = rsQuery.getString("MES")==null?"":rsQuery.getString("MES");
              ejercicio = rsQuery.getString("EJERCICIO")==null?"":rsQuery.getString("EJERCICIO");

      }
    }
    catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo select");
      System.out.println("bcRfTcFirmasAutorizadas.select: " + SQL.toString());
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
      SQL.append("insert into rf_tc_firmas_autorizadas ( ");
      SQL.append("NUM_EMPLEADO, ");
      SQL.append("PREFIJO, ");
      SQL.append("NOMBRES, ");
      SQL.append("APELLIDO_PAT, ");
      SQL.append("APELLIDO_MAT, ");
      SQL.append("PUESTO_FIRMA, ");
      SQL.append("MES, ");
      SQL.append("EJERCICIO ");
      SQL.append("\n) values (");
      SQL.append(numEmpleado + ", ");
      SQL.append(prefijo + ", ");
      SQL.append(nombres + ", ");
      SQL.append(apellidoPat + ", ");
      SQL.append(apellidoMat + ", ");
      SQL.append(puestoFirma + ", ");
      SQL.append(mes + ", ");
      SQL.append(ejercicio + ")  ");
//3
      stQuery2=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      stQuery2.executeUpdate(SQL.toString());
    }
    catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo insert");
      System.out.println("bcRfTcFirmasAutorizadas.insert: " + SQL);
      ejercicio = "-1";
      mes = "-1";
      numEmpleado = "-1";

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
    update(con,false);
  }
  
  public void update(Connection con, boolean mesEnAdelante) throws  Exception {
       String simbolo = mesEnAdelante?">":"";
       adecuaCampos();
       StringBuffer SQL = new StringBuffer();
       SQL.append("update rf_tc_firmas_autorizadas set \n");
       SQL.append("  NUM_EMPLEADO = " + numEmpleado + " , \n");
       SQL.append("  PREFIJO = " + prefijo + " , \n");
       SQL.append("  NOMBRES = " + nombres + " , \n");
       SQL.append("  APELLIDO_PAT = " + apellidoPat + " , \n");
       SQL.append("  APELLIDO_MAT = " + apellidoMat + " , \n");
       SQL.append("  PUESTO_FIRMA = " + puestoFirma + " , \n");
       if(!mesEnAdelante)
         SQL.append("  MES = " + mes + " , \n");
       SQL.append("  EJERCICIO = " + ejercicio + "  \n");
       SQL.append("where \n");
       SQL.append("  EJERCICIO = " + ejercicio + "  and " );
       SQL.append("  MES "+simbolo+"= " + mes + "  and " );
       SQL.append("  NUM_EMPLEADO = " + numEmpleado + "   " );
//10
       int regsAfectados =-1;
       Statement stQuery=null;
       try {
          stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
          regsAfectados=stQuery.executeUpdate(SQL.toString());
       }
       catch (Exception e) {
          System.out.println("Ha ocurrido un error en el metodo update");
          System.out.println("bcRfTcFirmasAutorizadas.update: "+SQL);
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
        SQL = "delete from rf_tc_firmas_autorizadas where EJERCICIO = " + ejercicio + " and MES = " + mes + " and NUM_EMPLEADO = " + numEmpleado + " " ;
        rs=stQuery.executeUpdate(SQL);
      }
     catch (Exception e) {
        System.out.println("Ha occurrido un error en el metodo delete");
        System.out.println("bcRfTcFirmasAutorizadas.delete: "+SQL);         
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
		sb.append(getMes());
		sb.append(",");
		sb.append(getNumEmpleado());
		sb.append(",");
		sb.append(getPrefijo());
		sb.append(",");
		sb.append(getApellidoMat());
		sb.append(",");
		sb.append(getApellidoPat());
		sb.append(",");
		sb.append(getNombres());
		sb.append(",");
		sb.append(getPuestoFirma());
		sb.append(",");
		sb.append(getEjercicio());
    return sb.toString();
  }
  
}
