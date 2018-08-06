package sia.rf.tesoreria.registro.repInversiones;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import sia.configuracion.BeanBase;

public class bcRfTrFondoReserva extends BeanBase {
		
        private String fecha;
	private String monto;
	private String porcentaje;
  
	public bcRfTrFondoReserva() {
		limpia();
	}
	
        public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getFecha() {
		return this.fecha;
	}

	public void setMonto(String monto) {
		this.monto = monto;
	}

	public String getMonto() {
		return this.monto;
	}

	public void setPorcentaje(String porcentaje) {
		this.porcentaje = porcentaje;
	}

	public String getPorcentaje() {
		return this.porcentaje;
	}
  
  private void limpia(){
		fecha = "";
		monto = "";
		porcentaje = "";

  }
  
  private void adecuaCampos() {
      fecha = ((fecha==null)||(fecha.equals("")))? "null" : "to_date('" + fecha + "','dd/mm/yyyy')";
      monto = ((monto==null)||(monto.equals("")))? "null" : monto;
      porcentaje = ((porcentaje==null)||(porcentaje.equals("")))? "null" : porcentaje;
  }
  
  private void getNext(Connection con) throws  Exception {
    Statement stQuery=null;
    StringBuffer SQL = null;
    ResultSet rsQuerySeq = null;
    try {
      stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      SQL = new StringBuffer("select 1 valoracutal from dual");
      rsQuerySeq = stQuery.executeQuery(SQL.toString());
      while (rsQuerySeq.next()){
      } //del while
      if(fecha == null)
        fecha = "1";

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
			SQL.append("  to_char(FECHA,'dd/mm/yyyy') FECHA, \n");
			SQL.append("  MONTO, \n");
			SQL.append("  PORCENTAJE \n");
			SQL.append("from \n"); 
			SQL.append("  RF_TR_FONDO_RESERVA\n");
			SQL.append("where \n");
                        SQL.append(" trunc(FECHA) = to_date('" + fecha + "','dd/mm/yyyy') ");

    Statement stQuery = null;
    ResultSet rsQuery = null;
    int resultado = 0;
    try {
      stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      rsQuery = stQuery.executeQuery(SQL.toString());
      
      rsQuery.beforeFirst();
      if (rsQuery.next() )  {
        
        limpia();
        
              fecha = rsQuery.getString("FECHA")==null?"":rsQuery.getString("FECHA");
              monto = rsQuery.getString("MONTO")==null?"":rsQuery.getString("MONTO");
              porcentaje = rsQuery.getString("PORCENTAJE")==null?"":rsQuery.getString("PORCENTAJE");

      }
    }
    catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo select");
      System.out.println("bcRfTrFondoReserva.select: " + SQL.toString());
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
      adecuaCampos();   
      SQL = new StringBuffer();
      SQL.setLength(0);
      SQL.append("insert into RF_TR_FONDO_RESERVA ( ");
      SQL.append("FECHA, ");
      SQL.append("MONTO, ");
      SQL.append("PORCENTAJE ");
      SQL.append("\n) values (");
      SQL.append(fecha+", ");
      SQL.append(monto+", ");
      SQL.append(porcentaje+")  ");
//3
      stQuery2=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      stQuery2.executeUpdate(SQL.toString());
    }
    catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo insert");
      System.out.println("bcRfTrFondoReserva.insert: " + SQL);
      fecha = "-1";

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
       adecuaCampos();
       StringBuffer SQL = new StringBuffer();
       SQL.append("update RF_TR_FONDO_RESERVA set \n");
       SQL.append("  FECHA = " + fecha+", \n");
       SQL.append("  MONTO = " + monto+", \n");
       SQL.append("  PORCENTAJE = " + porcentaje+" \n");
       SQL.append("where \n");
       SQL.append("  FECHA = "+ fecha );
//10
       int regsAfectados =-1;
       Statement stQuery=null;
       try {
          stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
          regsAfectados=stQuery.executeUpdate(SQL.toString());
       }
       catch (Exception e) {
          System.out.println("Ha ocurrido un error en el metodo update");
          System.out.println("bcRfTrFondoReserva.update: "+SQL);
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
        SQL = "delete from RF_TR_FONDO_RESERVA where FECHA = " + fecha + " " ;
        rs=stQuery.executeUpdate(SQL);
      }
     catch (Exception e) {
        System.out.println("Ha occurrido un error en el metodo delete");
        System.out.println("bcRfTrFondoReserva.delete: "+SQL);         
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
		sb.append(getFecha());
		sb.append(",");
		sb.append(getPorcentaje());
		sb.append(",");
		sb.append(getMonto());
    return sb.toString();
  }
  
}
