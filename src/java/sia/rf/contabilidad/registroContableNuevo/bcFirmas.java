package sia.rf.contabilidad.registroContableNuevo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import sia.configuracion.BeanBase;

import sun.jdbc.rowset.CachedRowSet;

public class bcFirmas extends BeanBase {
		
  private String documentoFirmaId;
	private String firma;
	private String numEmpleado;
	private String documentoContableId;
        
    private String lsDoc;
    private String lsTipoDoc;
    private String lsUnidad;
    private String lsEntidad;
    private String lsAmbito;
    private String lsMes;
    private String lsEjer;
  
	public bcFirmas() {
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
  

  
  public CachedRowSet selectFirmas(Connection con) throws Exception {
     CachedRowSet rsQuery=null; 
     StringBuffer SQL=new StringBuffer("");

      SQL.append(" select ");
      SQL.append(" fa.num_empleado, upper(fa.prefijo||' '||fa.nombres||' '||fa.apellido_pat||' '||fa.apellido_mat) nombre ");
      SQL.append(" from rf_tr_documentos_firmas df ");
      SQL.append(" inner join  rf_tr_documentos_contables doc on   df.documento_contable_id=doc.documento_contable_id ");
      SQL.append(" inner join   rf_tc_firmas_autorizadas fa on   df.num_empleado = fa.num_empleado  ");
      SQL.append(" where df.firma='"+lsTipoDoc+"' and doc.unidad_ejecutora='"+lsUnidad+"' and doc.entidad="+lsEntidad+" and doc.ambito='"+lsAmbito+"' ");
      SQL.append(" and doc.mes='"+lsMes+"' and fa.mes=doc.mes and doc.ejercicio='"+lsEjer+"' and fa.ejercicio=doc.ejercicio and doc.documento='"+lsDoc+"' ");
      System.out.println(SQL.toString());
      try{ 
       rsQuery = new CachedRowSet();       
       rsQuery.setCommand(SQL.toString());
       rsQuery.execute(con);
  } //Fin try 
  catch(Exception e){        
    System.out.println("Ocurrio un error al accesar al metodo "+e.getMessage()); 
    System.out.println(SQL.toString()); 
    throw e; 
  } //Fin catch 
  finally{ 
  } //Fin finally 
  return rsQuery;
}
  

  
    public String get_documentoFirmaId() {
        return documentoFirmaId;
    }

    public String get_firma() {
        return firma;
    }

    public String get_numEmpleado() {
        return numEmpleado;
    }

    public String get_documentoContableId() {
        return documentoContableId;
    }

    public void setLsDoc(String lsDoc) {
        this.lsDoc = lsDoc;
    }

    public String getLsDoc() {
        return lsDoc;
    }

    public void setLsTipoDoc(String lsTipoDoc) {
        this.lsTipoDoc = lsTipoDoc;
    }

    public String getLsTipoDoc() {
        return lsTipoDoc;
    }

    public void setLsUnidad(String lsUnidad) {
        this.lsUnidad = lsUnidad;
    }

    public String getLsUnidad() {
        return lsUnidad;
    }

    public void setLsEntidad(String lsEntidad) {
        this.lsEntidad = lsEntidad;
    }

    public String getLsEntidad() {
        return lsEntidad;
    }

    public void setLsAmbito(String lsAmbito) {
        this.lsAmbito = lsAmbito;
    }

    public String getLsAmbito() {
        return lsAmbito;
    }

    public void setLsMes(String lsMes) {
        this.lsMes = lsMes;
    }

    public String getLsMes() {
        return lsMes;
    }

    public void setLsEjer(String lsEjer) {
        this.lsEjer = lsEjer;
    }

    public String getLsEjer() {
        return lsEjer;
    }
}
