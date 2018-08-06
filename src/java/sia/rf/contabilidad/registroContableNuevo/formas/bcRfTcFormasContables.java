package sia.rf.contabilidad.registroContableNuevo.formas;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

import sia.configuracion.BeanBase;

import sia.db.dao.DaoFactory;
import sia.db.sql.Vista;

import sun.jdbc.rowset.CachedRowSet;

public class bcRfTcFormasContables extends BeanBase {
		
  private String formaContableId;
	private String forma;
	private String unidadEjecutora;
	private String entidad;
	private String ambito;
	private String concepto;
	private String documentoFuente;
	private String registro;
	private String tipoPolizaId;
	private String esmanual;
	private String idCatalogoCuenta;
  
  private List<bcRfTcSecuenciaFormaPojo> detSec;
  
	public bcRfTcFormasContables() {
		limpia();
	}
	
  public void setFormaContableId(String formaContableId) {
		this.formaContableId = formaContableId;
	}

	public String getFormaContableId() {
		return this.formaContableId;
	}

	public void setForma(String forma) {
		this.forma = forma;
	}

	public String getForma() {
		return this.forma;
	}

	public void setUnidadEjecutora(String unidadEjecutora) {
		this.unidadEjecutora = unidadEjecutora;
	}

	public String getUnidadEjecutora() {
		return this.unidadEjecutora;
	}

	public void setEntidad(String entidad) {
		this.entidad = entidad;
	}

	public String getEntidad() {
		return this.entidad;
	}

	public void setAmbito(String ambito) {
		this.ambito = ambito;
	}

	public String getAmbito() {
		return this.ambito;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	public String getConcepto() {
		return this.concepto;
	}

	public void setDocumentoFuente(String documentoFuente) {
		this.documentoFuente = documentoFuente;
	}

	public String getDocumentoFuente() {
		return this.documentoFuente;
	}

	public void setRegistro(String registro) {
		this.registro = registro;
	}

	public String getRegistro() {
		return this.registro;
	}

	public void setTipoPolizaId(String tipoPolizaId) {
		this.tipoPolizaId = tipoPolizaId;
	}

	public String getTipoPolizaId() {
		return this.tipoPolizaId;
	}

	public void setEsmanual(String esmanual) {
		this.esmanual = esmanual;
	}

	public String getEsmanual() {
		return this.esmanual;
	}

	public void setIdCatalogoCuenta(String idCatalogoCuenta) {
		this.idCatalogoCuenta = idCatalogoCuenta;
	}

	public String getIdCatalogoCuenta() {
		return this.idCatalogoCuenta;
	}
  
  private void limpia(){
		formaContableId = "";
		forma = "";
		unidadEjecutora = "";
		entidad = "";
		ambito = "";
		concepto = "";
		documentoFuente = "";
		registro = "";
		tipoPolizaId = "";
		esmanual = "";
		idCatalogoCuenta = "";

  }
  
  private void adecuaCampos() {
		formaContableId = ((formaContableId==null)||(formaContableId.equals("")))? "null" : formaContableId;
		forma = ((forma==null)||(forma.equals("")))? "null" : "'" +forma.toUpperCase()+ "'";
		unidadEjecutora = ((unidadEjecutora==null)||(unidadEjecutora.equals("")))? "null" : "'" +unidadEjecutora+ "'";
		entidad = ((entidad==null)||(entidad.equals("")))? "null" : entidad;
		ambito = ((ambito==null)||(ambito.equals("")))? "null" : "'" +ambito+ "'";
		concepto = ((concepto==null)||(concepto.equals("")))? "null" : "'" +concepto+ "'";
		documentoFuente = ((documentoFuente==null)||(documentoFuente.equals("")))? "null" : "'" +documentoFuente+ "'";
		registro = ((registro==null)||(registro.equals("")))? "null" : "to_date('" + registro + "','dd/mm/yyyy')";
		tipoPolizaId = ((tipoPolizaId==null)||(tipoPolizaId.equals("")))? "null" : tipoPolizaId;
		esmanual = ((esmanual==null)||(esmanual.equals("")))? "null" : esmanual;
		idCatalogoCuenta = ((idCatalogoCuenta==null)||(idCatalogoCuenta.equals("")))? "null" : idCatalogoCuenta;

  }
  
  /*Metodo que trae el siguente numero de secuencia(consecutivo) con la cual se 
    insertara el registro en la tabla RF_TC_FORMAS_CONTABLES.*/
  private void getNext(Connection con) throws  Exception {
    Statement stQuery=null;
    StringBuffer SQL = null;
    ResultSet rsQuerySeq = null;
    try {
      stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      SQL = new StringBuffer("select SEQ_RF_TC_FORMAS_CONTABLES.nextval valoractual from dual");
      rsQuerySeq = stQuery.executeQuery(SQL.toString());
      while (rsQuerySeq.next()){
        formaContableId = rsQuerySeq.getString("valoractual");
      } //del while
      if(formaContableId == null)
        formaContableId = "1";

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

  /* Metodo que selecciona los atributos los atributos de una forma contable en 
   * base a un id de la forma cotable          
   */  
  public int select(Connection con) throws Exception {
    StringBuffer SQL;
    SQL = new StringBuffer();
			SQL.append("select \n");
			SQL.append("  FORMA_CONTABLE_ID, \n");
			SQL.append("  FORMA, \n");
			SQL.append("  UNIDAD_EJECUTORA, \n");
			SQL.append("  ENTIDAD, \n");
			SQL.append("  AMBITO, \n");
			SQL.append("  CONCEPTO, \n");
			SQL.append("  DOCUMENTO_FUENTE, \n");
			SQL.append("  to_char(REGISTRO,'dd/mm/yyyy') REGISTRO, \n");
			SQL.append("  TIPO_POLIZA_ID, \n");
			SQL.append("  ESMANUAL, \n");
			SQL.append("  ID_CATALOGO_CUENTA \n");
			SQL.append("from \n"); 
			SQL.append("  RF_TC_FORMAS_CONTABLES\n");
			SQL.append("where \n");
			SQL.append("  FORMA_CONTABLE_ID = " + formaContableId + "  ");

    Statement stQuery = null;
    ResultSet rsQuery = null;
    int resultado = 0;
    try {
      stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      rsQuery = stQuery.executeQuery(SQL.toString());
      rsQuery.beforeFirst();
      if (rsQuery.next() )  {
        
        limpia();
        
              formaContableId = rsQuery.getString("FORMA_CONTABLE_ID")==null?"":rsQuery.getString("FORMA_CONTABLE_ID");
              forma = rsQuery.getString("FORMA")==null?"":rsQuery.getString("FORMA");
              unidadEjecutora = rsQuery.getString("UNIDAD_EJECUTORA")==null?"":rsQuery.getString("UNIDAD_EJECUTORA");
              entidad = rsQuery.getString("ENTIDAD")==null?"":rsQuery.getString("ENTIDAD");
              ambito = rsQuery.getString("AMBITO")==null?"":rsQuery.getString("AMBITO");
              concepto = rsQuery.getString("CONCEPTO")==null?"":rsQuery.getString("CONCEPTO");
              documentoFuente = rsQuery.getString("DOCUMENTO_FUENTE")==null?"":rsQuery.getString("DOCUMENTO_FUENTE");
              registro = rsQuery.getString("REGISTRO")==null?"":rsQuery.getString("REGISTRO");
              tipoPolizaId = rsQuery.getString("TIPO_POLIZA_ID")==null?"":rsQuery.getString("TIPO_POLIZA_ID");
              esmanual = rsQuery.getString("ESMANUAL")==null?"":rsQuery.getString("ESMANUAL");
              idCatalogoCuenta = rsQuery.getString("ID_CATALOGO_CUENTA")==null?"":rsQuery.getString("ID_CATALOGO_CUENTA");

      }
    }
    catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo select");
      System.out.println("bcRfTcFormasContables.select: " + SQL.toString());
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
  
  //Inserta un nuevo registro en la tabla RF_TC_FORMAS_CONTABLES
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
      SQL.append("insert into RF_TC_FORMAS_CONTABLES ( ");
      SQL.append("FORMA_CONTABLE_ID, ");
      SQL.append("FORMA, ");
      SQL.append("UNIDAD_EJECUTORA, ");
      SQL.append("ENTIDAD, ");
      SQL.append("AMBITO, ");
      SQL.append("CONCEPTO, ");
      SQL.append("DOCUMENTO_FUENTE, ");
      SQL.append("REGISTRO, ");
      SQL.append("TIPO_POLIZA_ID, ");
      SQL.append("ESMANUAL, ");
      SQL.append("ID_CATALOGO_CUENTA ");
      SQL.append("\n) values (");
      SQL.append(formaContableId + ", ");
      SQL.append(forma + ", ");
      SQL.append(unidadEjecutora + ", ");
      SQL.append(entidad + ", ");
      SQL.append(ambito + ", ");
      SQL.append(concepto + ", ");
      SQL.append(documentoFuente + ", ");
      SQL.append(registro + ", ");
      SQL.append(tipoPolizaId + ", ");
      SQL.append(esmanual + ", ");
      SQL.append(idCatalogoCuenta + ")  ");
//3
      stQuery2=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      stQuery2.executeUpdate(SQL.toString());
    }
    catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo insert");
      System.out.println("bcRfTcFormasContables.insert: " + SQL);
      formaContableId = "-1";

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
  
  //Actualiza los datos de un registro en la tabla RF_TC_FORMAS_CONTABLES
  public void update(Connection con) throws  Exception {
       adecuaCampos();
       StringBuffer SQL = new StringBuffer();
       SQL.append("update RF_TC_FORMAS_CONTABLES set \n");
       SQL.append("  FORMA_CONTABLE_ID = " + formaContableId + " , \n");
       SQL.append("  FORMA = " + forma + " , \n");
       SQL.append("  UNIDAD_EJECUTORA = " + unidadEjecutora + " , \n");
       SQL.append("  ENTIDAD = " + entidad + " , \n");
       SQL.append("  AMBITO = " + ambito + " , \n");
       SQL.append("  CONCEPTO = " + concepto + " , \n");
       SQL.append("  DOCUMENTO_FUENTE = " + documentoFuente + " , \n");
       SQL.append("  REGISTRO = " + registro + " , \n");
       SQL.append("  TIPO_POLIZA_ID = " + tipoPolizaId + " , \n");
       SQL.append("  ESMANUAL = " + esmanual + " , \n");
       SQL.append("  ID_CATALOGO_CUENTA = " + idCatalogoCuenta + "  \n");
       SQL.append("where \n");
       SQL.append("  FORMA_CONTABLE_ID = " + formaContableId + "   " );
//10
       int regsAfectados =-1;
       Statement stQuery=null;
       try {
          stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
          regsAfectados=stQuery.executeUpdate(SQL.toString());
       }
       catch (Exception e) {
          System.out.println("Ha ocurrido un error en el metodo update");
          System.out.println("bcRfTcFormasContables.update: "+SQL);
          throw e;
       }
       finally {
          if (stQuery != null) {
             stQuery.close();
             stQuery=null;
          }           
       }
  }

  //Elimina un registro de la tabla RF_TC_FORMAS_CONTABLES  
  public void delete(Connection con) throws  Exception {
      int rs=-1;
      Statement stQuery=null;
      String SQL = "";
      try {
        stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        SQL = "delete from RF_TC_FORMAS_CONTABLES where FORMA_CONTABLE_ID = " + formaContableId + " " ;
        rs=stQuery.executeUpdate(SQL);
      }
     catch (Exception e) {
        System.out.println("Ha occurrido un error en el metodo delete");
        System.out.println("bcRfTcFormasContables.delete: "+SQL);         
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
		sb.append(getConcepto());
		sb.append(",");
		sb.append(getForma());
		sb.append(",");
		sb.append(getAmbito());
		sb.append(",");
		sb.append(getIdCatalogoCuenta());
		sb.append(",");
		sb.append(getEntidad());
		sb.append(",");
		sb.append(getDocumentoFuente());
		sb.append(",");
		sb.append(getUnidadEjecutora());
		sb.append(",");
		sb.append(getTipoPolizaId());
		sb.append(",");
		sb.append(getRegistro());
		sb.append(",");
		sb.append(getEsmanual());
		sb.append(",");
		sb.append(getFormaContableId());
    return sb.toString();
  }

  public void setDetSec(List<bcRfTcSecuenciaFormaPojo> detSec) {
    this.detSec = detSec;
  }

  public List<bcRfTcSecuenciaFormaPojo> getDetSec() {
    if(detSec == null) {
      detSec = new ArrayList();
      getDetSecuencia();
    }
    return detSec;
  }
  
  public void getDetSecuencia() {
    if(getFormaContableId() != null && !getFormaContableId().equals("")) {
      List<Vista> registros         = null;
      bcRfTcSecuenciaFormaPojo sec  = null;
      bcRfTcConfiguraFormaPojo cfg  = null;
      try{
        addParametro("forma",getFormaContableId());
        registros = consultar(DaoFactory.CONEXION_CONTABILIDAD,"formasContables.select.detalleFormaContable");
        String secuencia = "0";
        for(Vista reg : registros) {
          if(!reg.getField("secuencia_forma_id").equals(secuencia)) {
            secuencia = reg.getField("secuencia_forma_id");
            sec = new bcRfTcSecuenciaFormaPojo();
            sec.setFormaContableId(reg.getField("forma_contable_id"));
            sec.setSecuenciaFormaId(reg.getField("secuencia_forma_id"));
            sec.setOperacionContableId(reg.getField("operacion_contable_id"));
            detSec.add(sec);
          }
          
          cfg = new bcRfTcConfiguraFormaPojo();
          sec.getDetConf().add(cfg);
          
          cfg.setCodigo(reg.getField("codigo"));
          cfg.setEsvariable(reg.getField("esvariable"));
          cfg.setNivel(reg.getField("nivel"));
          cfg.setSecuenciaFormaId(sec.getSecuenciaFormaId());
          cfg.setSigno(reg.getField("signo"));
          cfg.setConfiguraFormaId(reg.getField("configura_forma_id"));
        }//Fin de For
      } catch (Exception ex)  {
        System.err.println("Ocurrio un error al obtener el detalle de la secuencia de las formas contables");
        System.err.println("bcRfTcFormasContables.getDetSecuencia: ");
      } finally  {
        registros = null;
        liberaParametros();
      }
    
    }
  
  }
  
  
  public void borraEnCascada(Connection con) {
    if(formaContableId != null)
      try  {
      
        addParametro("forma",getFormaContableId());
        ejecuta(con,"formasContables.delete.configuraDeForma",DaoFactory.CONEXION_CONTABILIDAD);
        ejecuta(con,"formasContables.delete.secuenciaDeForma",DaoFactory.CONEXION_CONTABILIDAD);
        delete(con);  
      } catch (Exception ex)  {
        ex.printStackTrace();
      } finally  {
      }
  }
  
 /** 
  * Descripcion: Lista de campos que son varialbes de las formas manuales
  * Fecha de creacion: 18/02/2011
  */ 
  public CachedRowSet select_formaContable_secuenciaForma_configuraForma(Connection con,String formaContableId)throws SQLException, Exception{ 
    CachedRowSet crs = null;     
    StringBuffer SQL=null;
     try{ 
      crs = new CachedRowSet();
      SQL=new StringBuffer("SELECT fc.concepto,cf.codigo,fc.forma,tp.descripcion,cf.nivel,cf.signo ");  
      SQL.append(" FROM rf_tc_formas_contables fc,rf_tc_secuencia_forma sf,rf_tc_configura_forma cf, rf_tc_tipos_polizas tp"); 
      SQL.append(" WHERE fc.forma_contable_id=").append(formaContableId);         
      SQL.append(" AND tp.tipo_poliza_id=fc.tipo_poliza_id");       
      SQL.append(" AND fc.forma_contable_id=sf.forma_contable_id");       
      SQL.append(" AND sf.secuencia_forma_id=cf.secuencia_forma_id");       
      SQL.append(" AND cf.esvariable=1");
      SQL.append(" GROUP BY concepto, codigo,forma,descripcion,nivel,signo");
      System.out.println(SQL.toString()); 
      crs.setCommand(SQL.toString());
      crs.execute(con);                
    } //Fin try 
    catch(Exception e){ 
      System.out.println("Ocurrio un error al accesar al metodo select_rf_tr_eventoContable_cacheRowSet "+e.getMessage()); 
      crs.close();
      crs = null;
      throw e; 
    } //Fin catch 
    finally{ 
      SQL.setLength(0);
      SQL = null;
    } //Fin finally 
     return crs;
  } //Fin metodo select_formaContable_secuenciaForma_configuraForma
  
  //Método agregado para la modificación de pólizas a través de formas
   public CachedRowSet select_formaContable_secuenciaForma_configuraFormaPoliza(Connection con,String formaContableId)throws SQLException, Exception{ 
     CachedRowSet crs = null;     
     StringBuffer SQL=null;
      try{ 
       crs = new CachedRowSet();
       SQL=new StringBuffer("SELECT fc.concepto,cf.codigo,fc.forma,tp.descripcion,cf.nivel, p.referencia as refgeneral, fc.forma_contable_id, dp.referencia, cf.signo ");  
       SQL.append(" FROM rf_tc_formas_contables fc,rf_tc_secuencia_forma sf,rf_tc_configura_forma cf, rf_tc_tipos_polizas tp, rf_tr_polizas p, rf_tr_detalle_poliza dp ");
       SQL.append(" WHERE p.poliza_id="+formaContableId+" and p.origen=fc.forma and p.unidad_ejecutora=fc.unidad_ejecutora and p.entidad=fc.entidad and p.ambito= fc.ambito");
       SQL.append(" AND tp.tipo_poliza_id=fc.tipo_poliza_id AND fc.forma_contable_id=sf.forma_contable_id AND sf.secuencia_forma_id=cf.secuencia_forma_id ");
       SQL.append(" AND cf.esvariable=1 and dp.poliza_id=p.poliza_id GROUP BY fc.concepto, codigo,forma,descripcion,nivel, p.referencia, fc.forma_contable_id, dp.referencia,cf.signo ");
       System.out.println(SQL.toString()); 
       crs.setCommand(SQL.toString());
       crs.execute(con);                
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo select_formaContable_secuenciaForma_configuraFormaPoliza "+e.getMessage()); 
       crs.close();
       crs = null;
       throw e; 
     } //Fin catch 
     finally{ 
       SQL.setLength(0);
       SQL = null;
     } //Fin finally 
      return crs;
   } //Fin metodo select_formaContable_secuenciaForma_configuraForma

  //Valida que el nombre de la forma manual que vamos a insertar sea unico y que no este repetido 
  public void validaUniqueForma(String forma, String unidad, String entidad, String ambito, String idCatalogoCuenta) throws Exception {
    addParametro("forma",forma.toUpperCase());
    addParametro("unidad",unidad);
    addParametro("entidad",entidad);
    addParametro("ambito",ambito);
    addParametro("idCatalogo",idCatalogoCuenta);
    List<Vista> registros =  consultar(DaoFactory.CONEXION_CONTABILIDAD,"formasContables.select.validarUniqueFormas");
    liberaParametros();
    if(registros!= null && registros.size() > 0)
      throw new Exception("Ya existe otro registro con el nombre de forma asignado");
  }
  
  //Metodo para validar que no existan polizas a una forma manual
  public void validaNoPolizasAsociadas(String forma, String unidad, int entidad, int ambito, int idCatalogoCuenta, int esManual) throws Exception {
    addParamVal("forma","and t.origen=':param'",forma.toUpperCase());
    addParamVal("idCatalogo","and t.id_catalogo_cuenta=:param",idCatalogoCuenta);
    if(esManual==0)
      addParamVal("referencia","and t.poliza_referencia is not null",1); //automatica
    else {
      addParamVal("referencia","and t.poliza_referencia is null",1); //manual
      addParamVal("unidad","and t.unidad_ejecutora=':param'",unidad);
      addParamVal("entidad","and t.entidad=:param",entidad);
      addParamVal("ambito","and t.ambito=:param",ambito);
    }
    List<Vista> registros =  consultar(DaoFactory.CONEXION_CONTABILIDAD,"formasContables.select.validarExistenciaDePolizas");
    liberaParametros();
    if(registros!= null && registros.size() > 0)
      throw new Exception("No se puede eliminar el registro debido a que existen polizas asociadas a él");
  }
  
  public Integer nivelMax(Connection con,String formaContableId)throws SQLException, Exception{ 
     Statement stQuery=null;
     StringBuffer SQL = null;
     ResultSet rs = null;
     Integer maximo=null;
     try {
       SQL = new StringBuffer();
       stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
       SQL.append("select max(cf.nivel) maximo from rf_tc_formas_contables t, rf_tc_secuencia_forma sf, rf_tc_configura_forma cf ");
       SQL.append("where t.esmanual=1  and t.forma_contable_id=").append(formaContableId); 
       SQL.append(" and t.forma_contable_id=sf.forma_contable_id ");
       SQL.append("and sf.secuencia_forma_id=cf.secuencia_forma_id and cf.nivel not in(98,99) order by cf.nivel ");
       rs = stQuery.executeQuery(SQL.toString());       
       while (rs.next()){
         maximo = rs.getInt("maximo");
       } //del while
       
     }
     catch(Exception e) {
       throw e;
     }
     finally {
       SQL.setLength(0);
       if(rs != null)
         rs.close();
       rs = null;
       if(stQuery != null)
         stQuery.close();
       stQuery = null;       
     }
     return maximo;
   } //Fin metodo nivelMax(Connection con,String formaContableId)
    
}