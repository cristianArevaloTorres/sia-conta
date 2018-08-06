package sia.rf.tesoreria.registro.detalleCompra;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import sia.configuracion.BeanBase;

public class bcRfTrDetalleCompra extends BeanBase {
		
  private String idCompraInversion;
	private String idDetalleCompra;
	private String idOperacion;
	private String idTipoValores;
	private String titulosAcciones;
	private String valor;
	private String importe;
	private String valorAccionVenta;
	private String rendimiento;
	private String idOperacionRend;
	private String idOperacionRecupera;
  
	public bcRfTrDetalleCompra() {
		limpia();
	}
	
  public void setIdCompraInversion(String idCompraInversion) {
		this.idCompraInversion = idCompraInversion;
	}

	public String getIdCompraInversion() {
		return this.idCompraInversion;
	}

	public void setIdDetalleCompra(String idDetalleCompra) {
		this.idDetalleCompra = idDetalleCompra;
	}

	public String getIdDetalleCompra() {
		return this.idDetalleCompra;
	}

	public void setIdOperacion(String idOperacion) {
		this.idOperacion = idOperacion;
	}

	public String getIdOperacion() {
		return this.idOperacion;
	}

	public void setIdTipoValores(String idTipoValores) {
		this.idTipoValores = idTipoValores;
	}

	public String getIdTipoValores() {
		return this.idTipoValores;
	}

	public void setTitulosAcciones(String titulosAcciones) {
		this.titulosAcciones = quitarComas(titulosAcciones);
	}

	public String getTitulosAcciones() {
		return this.titulosAcciones;
	}

	public void setValor(String valor) {
		this.valor = quitarComas(valor);
	}

	public String getValor() {
		return this.valor;
	}

	public void setImporte(String importe) {
		this.importe = quitarComas(importe);
	}

	public String getImporte() {
		return this.importe;
	}

	public void setValorAccionVenta(String valorAccionVenta) {
		this.valorAccionVenta = quitarComas(valorAccionVenta);
	}

	public String getValorAccionVenta() {
		return this.valorAccionVenta;
	}

	public void setRendimiento(String rendimiento) {
		this.rendimiento = quitarComas(rendimiento);
	}

	public String getRendimiento() {
		return this.rendimiento;
	}

	public void setIdOperacionRend(String idOperacionRend) {
		this.idOperacionRend = idOperacionRend;
	}

	public String getIdOperacionRend() {
		return this.idOperacionRend;
	}

	public void setIdOperacionRecupera(String idOperacionRecupera) {
		this.idOperacionRecupera = idOperacionRecupera;
	}

	public String getIdOperacionRecupera() {
		return this.idOperacionRecupera;
	}
  
  private void limpia(){
		idCompraInversion = "";
		idDetalleCompra = "";
		idOperacion = "";
		idTipoValores = "";
		titulosAcciones = "";
		valor = "";
		importe = "";
		valorAccionVenta = "";
		rendimiento = "";
		idOperacionRend = "";
		idOperacionRecupera = "";

  }
  
  private void adecuaCampos() {
		idCompraInversion = ((idCompraInversion==null)||(idCompraInversion.equals("")))? "null" : idCompraInversion;
		idDetalleCompra = ((idDetalleCompra==null)||(idDetalleCompra.equals("")))? "null" : idDetalleCompra;
		idOperacion = ((idOperacion==null)||(idOperacion.equals("")))? "null" : idOperacion;
		idTipoValores = ((idTipoValores==null)||(idTipoValores.equals("")))? "null" : idTipoValores;
		titulosAcciones = ((titulosAcciones==null)||(titulosAcciones.equals("")))? "null" : titulosAcciones;
		valor = ((valor==null)||(valor.equals("")))? "null" : valor;
		importe = ((importe==null)||(importe.equals("")))? "null" : importe;
		valorAccionVenta = ((valorAccionVenta==null)||(valorAccionVenta.equals("")))? "null" : valorAccionVenta;
		rendimiento = ((rendimiento==null)||(rendimiento.equals("")))? "null" : rendimiento;
		idOperacionRend = ((idOperacionRend==null)||(idOperacionRend.equals("")))? "null" : idOperacionRend;
		idOperacionRecupera = ((idOperacionRecupera==null)||(idOperacionRecupera.equals("")))? "null" : idOperacionRecupera;

  }
  
  private void getNext(Connection con) throws  Exception {
    Statement stQuery=null;
    StringBuffer SQL = null;
    ResultSet rsQuerySeq = null;
    try {
      stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      SQL = new StringBuffer("select SEQ_TR_DETALLE_COMPRA.nextval valoractual from dual");
      rsQuerySeq = stQuery.executeQuery(SQL.toString());
      while (rsQuerySeq.next()){
        idDetalleCompra = rsQuerySeq.getString("valoractual");
      } //del while
      if(idDetalleCompra == null)
        idDetalleCompra = "1";

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
			SQL.append("  ID_COMPRA_INVERSION, \n");
			SQL.append("  ID_DETALLE_COMPRA, \n");
			SQL.append("  ID_OPERACION, \n");
			SQL.append("  ID_TIPO_VALORES, \n");
			SQL.append("  TITULOS_ACCIONES, \n");
			SQL.append("  VALOR, \n");
			SQL.append("  IMPORTE, \n");
			SQL.append("  VALOR_ACCION_VENTA, \n");
			SQL.append("  RENDIMIENTO, \n");
			SQL.append("  ID_OPERACION_REND, \n");
			SQL.append("  ID_OPERACION_RECUPERA \n");
			SQL.append("from \n"); 
			SQL.append("  rf_tr_detalle_compra\n");
			SQL.append("where \n");
			SQL.append("  ID_DETALLE_COMPRA = " + idDetalleCompra + "  ");

    Statement stQuery = null;
    ResultSet rsQuery = null;
    int resultado = 0;
    try {
      stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      rsQuery = stQuery.executeQuery(SQL.toString());
      
      rsQuery.beforeFirst();
      if (rsQuery.next() )  {
        
        limpia();
        
              idCompraInversion = rsQuery.getString("ID_COMPRA_INVERSION")==null?"":rsQuery.getString("ID_COMPRA_INVERSION");
              idDetalleCompra = rsQuery.getString("ID_DETALLE_COMPRA")==null?"":rsQuery.getString("ID_DETALLE_COMPRA");
              idOperacion = rsQuery.getString("ID_OPERACION")==null?"":rsQuery.getString("ID_OPERACION");
              idTipoValores = rsQuery.getString("ID_TIPO_VALORES")==null?"":rsQuery.getString("ID_TIPO_VALORES");
              titulosAcciones = rsQuery.getString("TITULOS_ACCIONES")==null?"":rsQuery.getString("TITULOS_ACCIONES");
              valor = rsQuery.getString("VALOR")==null?"":rsQuery.getString("VALOR");
              importe = rsQuery.getString("IMPORTE")==null?"":rsQuery.getString("IMPORTE");
              valorAccionVenta = rsQuery.getString("VALOR_ACCION_VENTA")==null?"":rsQuery.getString("VALOR_ACCION_VENTA");
              rendimiento = rsQuery.getString("RENDIMIENTO")==null?"":rsQuery.getString("RENDIMIENTO");
              idOperacionRend = rsQuery.getString("ID_OPERACION_REND")==null?"":rsQuery.getString("ID_OPERACION_REND");
              idOperacionRecupera = rsQuery.getString("ID_OPERACION_RECUPERA")==null?"":rsQuery.getString("ID_OPERACION_RECUPERA");

      }
    }
    catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo select");
      System.out.println("bcRfTrDetalleCompra.select: " + SQL.toString());
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
      SQL.append("insert into rf_tr_detalle_compra ( ");
      SQL.append("ID_COMPRA_INVERSION, ");
      SQL.append("ID_DETALLE_COMPRA, ");
      SQL.append("ID_OPERACION, ");
      SQL.append("ID_TIPO_VALORES, ");
      SQL.append("TITULOS_ACCIONES, ");
      SQL.append("VALOR, ");
      SQL.append("IMPORTE, ");
      SQL.append("VALOR_ACCION_VENTA, ");
      SQL.append("RENDIMIENTO, ");
      SQL.append("ID_OPERACION_REND, ");
      SQL.append("ID_OPERACION_RECUPERA ");
      SQL.append("\n) values (");
      SQL.append(idCompraInversion + ", ");
      SQL.append(idDetalleCompra + ", ");
      SQL.append(idOperacion + ", ");
      SQL.append(idTipoValores + ", ");
      SQL.append(titulosAcciones + ", ");
      SQL.append(valor + ", ");
      SQL.append(importe + ", ");
      SQL.append(valorAccionVenta + ", ");
      SQL.append(rendimiento + ", ");
      SQL.append(idOperacionRend + ", ");
      SQL.append(idOperacionRecupera + ")  ");
//3
      stQuery2=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      stQuery2.executeUpdate(SQL.toString());
    }
    catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo insert");
      System.out.println("bcRfTrDetalleCompra.insert: " + SQL);
      idDetalleCompra = "-1";

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
       SQL.append("update rf_tr_detalle_compra set \n");
       SQL.append("  ID_COMPRA_INVERSION = " + idCompraInversion + " , \n");
       SQL.append("  ID_DETALLE_COMPRA = " + idDetalleCompra + " , \n");
       SQL.append("  ID_OPERACION = " + idOperacion + " , \n");
       SQL.append("  ID_TIPO_VALORES = " + idTipoValores + " , \n");
       SQL.append("  TITULOS_ACCIONES = " + titulosAcciones + " , \n");
       SQL.append("  VALOR = " + valor + " , \n");
       SQL.append("  IMPORTE = " + importe + " , \n");
       SQL.append("  VALOR_ACCION_VENTA = " + valorAccionVenta + " , \n");
       SQL.append("  RENDIMIENTO = " + rendimiento + " , \n");
       SQL.append("  ID_OPERACION_REND = " + idOperacionRend + " , \n");
       SQL.append("  ID_OPERACION_RECUPERA = " + idOperacionRecupera + "  \n");
       SQL.append("where \n");
       SQL.append("  ID_DETALLE_COMPRA = " + idDetalleCompra + "   " );
//10
       int regsAfectados =-1;
       Statement stQuery=null;
       try {
          stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
          regsAfectados=stQuery.executeUpdate(SQL.toString());
       }
       catch (Exception e) {
          System.out.println("Ha ocurrido un error en el metodo update");
          System.out.println("bcRfTrDetalleCompra.update: "+SQL);
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
        SQL = "delete from rf_tr_detalle_compra where ID_DETALLE_COMPRA = " + idDetalleCompra + " " ;
        rs=stQuery.executeUpdate(SQL);
      }
     catch (Exception e) {
        System.out.println("Ha occurrido un error en el metodo delete");
        System.out.println("bcRfTrDetalleCompra.delete: "+SQL);         
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
		sb.append(getTitulosAcciones());
		sb.append(",");
		sb.append(getIdOperacion());
		sb.append(",");
		sb.append(getIdDetalleCompra());
		sb.append(",");
		sb.append(getValorAccionVenta());
		sb.append(",");
		sb.append(getRendimiento());
		sb.append(",");
		sb.append(getImporte());
		sb.append(",");
		sb.append(getIdCompraInversion());
		sb.append(",");
		sb.append(getIdOperacionRend());
		sb.append(",");
		sb.append(getIdOperacionRecupera());
		sb.append(",");
		sb.append(getValor());
		sb.append(",");
		sb.append(getIdTipoValores());
    return sb.toString();
  }
  
}
