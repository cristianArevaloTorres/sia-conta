package sia.rf.contabilidad.registroContableNuevo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class bcDocumentosFirmas {
    public bcDocumentosFirmas() {
    }
    
    private String documento_firma_id; 
    private String firma; 
    private String num_empleado;
    private String documento_contable_id;


    public void setDocumento_firma_id(String documento_firma_id) {
        this.documento_firma_id = documento_firma_id;
    }

    public String getDocumento_firma_id() {
        return documento_firma_id;
    }

    public void setFirma(String firma) {
        this.firma = firma;
    }

    public String getFirma() {
        return firma;
    }

    public void setNum_empleado(String num_empleado) {
        this.num_empleado = num_empleado;
    }

    public String getNum_empleado() {
        return num_empleado;
    }

    public void setDocumento_contable_id(String documento_contable_id) {
        this.documento_contable_id = documento_contable_id;
    }

    public String getDocumento_contable_id() {
        return documento_contable_id;
    }

    /** 
    * Metodo que inserta la informacion de rf_tr_documentos_firmas
    * Fecha de creacion: 13/01/2010
    * Autor: Claudia Luz Hernandez Lopez
    * Fecha de modificacion: 
    * Modificado por: 
    */ 
    public void insert_documentos_firmas(Connection con) throws SQLException, Exception{ 
       Statement stQuery=null; 
       try{ 
        stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
        StringBuffer SQL=new StringBuffer("INSERT INTO  rf_tr_documentos_firmas(firma,num_empleado,documento_contable_id) ");  
        SQL.append("VALUES('");         
        SQL.append(firma).append("',"); 
        SQL.append(num_empleado).append(","); 
        SQL.append(documento_contable_id).append(")"); 
        
        System.out.println(SQL.toString()); 
        int rs=-1; 
        rs=stQuery.executeUpdate(SQL.toString()); 
      } //Fin try 
      catch(Exception e){ 
        System.out.println("Ocurrio un error al accesar al metodo insert_documentos_firmas "+e.getMessage()); 
        throw e; 
      } //Fin catch 
      finally{ 
        if (stQuery!=null){ 
          stQuery.close(); 
          stQuery=null; 
        } 
      } //Fin finally 
    } //Fin metodo insert_documentos_firmas





}
