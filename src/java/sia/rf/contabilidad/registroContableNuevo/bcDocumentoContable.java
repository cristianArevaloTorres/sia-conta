package sia.rf.contabilidad.registroContableNuevo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class bcDocumentoContable {
    public bcDocumentoContable() {
    }
    private String documento_contable_id; 
    private String unidad_ejecutora; 
    private String ambito; 
    private String pais; 
    private String entidad; 
    private String mes; 
    private String num_empleado;
    private String registro;
    private String estatus_cierre_id; 
    private String documento;
    private String ejercicio;


    public void setDocumento_contable_id(String documento_contable_id) {
        this.documento_contable_id = documento_contable_id;
    }

    public String getDocumento_contable_id() {
        return documento_contable_id;
    }

    public void setUnidad_ejecutora(String unidad_ejecutora) {
        this.unidad_ejecutora = unidad_ejecutora;
    }

    public String getUnidad_ejecutora() {
        return unidad_ejecutora;
    }

    public void setAmbito(String ambito) {
        this.ambito = ambito;
    }

    public String getAmbito() {
        return ambito;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getPais() {
        return pais;
    }

    public void setEntidad(String entidad) {
        this.entidad = entidad;
    }

    public String getEntidad() {
        return entidad;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getMes() {
        return mes;
    }

    public void setNum_empleado(String num_empleado) {
        this.num_empleado = num_empleado;
    }

    public String getNum_empleado() {
        return num_empleado;
    }

    public void setRegistro(String registro) {
        this.registro = registro;
    }

    public String getRegistro() {
        return registro;
    }

    public void setEstatus_cierre_id(String estatus_cierre_id) {
        this.estatus_cierre_id = estatus_cierre_id;
    }

    public String getEstatus_cierre_id() {
        return estatus_cierre_id;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getDocumento() {
        return documento;
    }

    public void setEjercicio(String ejercicio) {
        this.ejercicio = ejercicio;
    }

    public String getEjercicio() {
        return ejercicio;
    }
    /** 
    * Metodo que inserta la informacion de RF_TR_DOCUMENTOS_CONTABLES
    * Fecha de creacion: 13/01/2010
    * Autor: Claudia Luz Hernandez Lopez
    * Fecha de modificacion: 
    * Modificado por: 
    */ 
    public void insert_documento_contable(Connection con) throws SQLException, Exception{ 
       Statement stQuery=null; 
       try{ 
        stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
        StringBuffer SQL=new StringBuffer("INSERT INTO RF_TR_DOCUMENTOS_CONTABLES(documento_contable_id,unidad_ejecutora,ambito,pais,entidad,mes,num_empleado,registro,estatus_cierre_id,documento,ejercicio) ");  
        SQL.append("VALUES("); 
        SQL.append(documento_contable_id).append(","); 
        SQL.append(unidad_ejecutora).append(","); 
        SQL.append(ambito).append(","); 
        SQL.append(pais).append(","); 
        SQL.append(entidad).append(","); 
        SQL.append(mes).append(",");          
        SQL.append(num_empleado).append(","); 
        SQL.append(" sysdate,"); 
        SQL.append(estatus_cierre_id).append(",'"); 
        SQL.append(documento).append("',");         
        SQL.append(ejercicio).append(")"); 
        
        System.out.println(SQL.toString()); 
        int rs=-1; 
        rs=stQuery.executeUpdate(SQL.toString()); 
      } //Fin try 
      catch(Exception e){ 
        System.out.println("Ocurrio un error al accesar al metodo insert_documento_contable "+e.getMessage()); 
        throw e; 
      } //Fin catch 
      finally{ 
        if (stQuery!=null){ 
          stQuery.close(); 
          stQuery=null; 
        } 
      } //Fin finally 
    } //Fin metodo insert_documento_contable

     /**
     * Metodo que lee la sequencia para documento_contable_id
     * Fecha de creacion:13/01/2010
     * Autor:Claudia Luz Hernandez Lopez
     * Fecha de modificacion:
     * Modificado por:
     */
     
     public String select_SEQ_rf_tr_documentos_contables(Connection con)throws SQLException, Exception{
        Statement stQuery=null;
        ResultSet rsQuery= null;
        String idDocto = "";
        try{
          String SQL2 = "select seq_rf_tr_documentos_contables.nextval valoractual from dual";     
          stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
          rsQuery = stQuery.executeQuery(SQL2);
          //  secuencia para documento_contable_id
          while (rsQuery.next()){
             idDocto = rsQuery.getString("valoractual");
          } //del while
        } //Fin try
        catch(Exception e){
          System.out.println("Ocurrio un error al accesar al metodo SEQ_rf_tr_documentos_contabless "+e.getMessage());
          throw e;
        } //Fin catch
        finally{
          if (rsQuery!=null){
            rsQuery.close();
            rsQuery=null;
          }
          if (stQuery!=null){
            stQuery.close();
            stQuery=null;
          }
        } //Fin finally
        return idDocto;
     } //fin Select 
     



}
