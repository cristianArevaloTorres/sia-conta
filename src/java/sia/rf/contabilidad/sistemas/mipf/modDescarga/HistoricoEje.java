package sia.rf.contabilidad.sistemas.mipf.modDescarga;

import java.sql.*;

import sun.jdbc.rowset.CachedRowSet;

public class HistoricoEje{ 
   public HistoricoEje(){ 
   } 
   
   private String actividad; 
   private String prioridad; 
   private String proceso; 
   private String subproceso; 
   private String macro; 
   private String unidad; 
   private String estado; 
   private String partida; 
   private String mes; 
   private String tipoeje; 
   private String cxlc; 
   private String docoreco; 
   private String rfc; 
   private String factura; 
   private String importe; 
   private String concepto; 
   private String tipoadq; 
   private String fechaaut; 
   private String fechacap; 
   private String ident; 
   private String poliza; 
   private String idhisteje; 
  
   /** 
   * actividad 
   * @return actividad 
   */ 
   public String getActividad() { 
      return actividad; 
   } 
  
   /** 
   * actividad 
   * @param actividad 
   */ 
   public void setActividad( String actividad ) { 
      this.actividad=actividad; 
   } 
  
   /** 
   * prioridad 
   * @return prioridad 
   */ 
   public String getPrioridad() { 
      return prioridad; 
   } 
  
   /** 
   * prioridad 
   * @param prioridad 
   */ 
   public void setPrioridad( String prioridad ) { 
      this.prioridad=prioridad; 
   } 
  
   /** 
   * proceso 
   * @return proceso 
   */ 
   public String getProceso() { 
      return proceso; 
   } 
  
   /** 
   * proceso 
   * @param proceso 
   */ 
   public void setProceso( String proceso ) { 
      this.proceso=proceso; 
   } 
  
   /** 
   * subproceso 
   * @return subproceso 
   */ 
   public String getSubproceso() { 
      return subproceso; 
   } 
  
   /** 
   * subproceso 
   * @param subproceso 
   */ 
   public void setSubproceso( String subproceso ) { 
      this.subproceso=subproceso; 
   } 
  
   /** 
   * macro 
   * @return macro 
   */ 
   public String getMacro() { 
      return macro; 
   } 
  
   /** 
   * macro 
   * @param macro 
   */ 
   public void setMacro( String macro ) { 
      this.macro=macro; 
   } 
  
   /** 
   * unidad 
   * @return unidad 
   */ 
   public String getUnidad() { 
      return unidad; 
   } 
  
   /** 
   * unidad 
   * @param unidad 
   */ 
   public void setUnidad( String unidad ) { 
      this.unidad=unidad; 
   } 
  
   /** 
   * estado 
   * @return estado 
   */ 
   public String getEstado() { 
      return estado; 
   } 
  
   /** 
   * estado 
   * @param estado 
   */ 
   public void setEstado( String estado ) { 
      this.estado=estado; 
   } 
  
   /** 
   * partida 
   * @return partida 
   */ 
   public String getPartida() { 
      return partida; 
   } 
  
   /** 
   * partida 
   * @param partida 
   */ 
   public void setPartida( String partida ) { 
      this.partida=partida; 
   } 
  
   /** 
   * mes 
   * @return mes 
   */ 
   public String getMes() { 
      return mes; 
   } 
  
   /** 
   * mes 
   * @param mes 
   */ 
   public void setMes( String mes ) { 
      this.mes=mes; 
   } 
  
   /** 
   * tipoeje 
   * @return tipoeje 
   */ 
   public String getTipoeje() { 
      return tipoeje; 
   } 
  
   /** 
   * tipoeje 
   * @param tipoeje 
   */ 
   public void setTipoeje( String tipoeje ) { 
      this.tipoeje=tipoeje; 
   } 
  
   /** 
   * cxlc 
   * @return cxlc 
   */ 
   public String getCxlc() { 
      return cxlc; 
   } 
  
   /** 
   * cxlc 
   * @param cxlc 
   */ 
   public void setCxlc( String cxlc ) { 
      this.cxlc=cxlc; 
   } 
  
   /** 
   * docoreco 
   * @return docoreco 
   */ 
   public String getDocoreco() { 
      return docoreco; 
   } 
  
   /** 
   * docoreco 
   * @param docoreco 
   */ 
   public void setDocoreco( String docoreco ) { 
      this.docoreco=docoreco; 
   } 
  
   /** 
   * rfc 
   * @return rfc 
   */ 
   public String getRfc() { 
      return rfc; 
   } 
  
   /** 
   * rfc 
   * @param rfc 
   */ 
   public void setRfc( String rfc ) { 
      this.rfc=rfc; 
   } 
  
   /** 
   * factura 
   * @return factura 
   */ 
   public String getFactura() { 
      return factura; 
   } 
  
   /** 
   * factura 
   * @param factura 
   */ 
   public void setFactura( String factura ) { 
      this.factura=factura; 
   } 
  
   /** 
   * importe 
   * @return importe 
   */ 
   public String getImporte() { 
      return importe; 
   } 
  
   /** 
   * importe 
   * @param importe 
   */ 
   public void setImporte( String importe ) { 
      this.importe=importe; 
   } 
  
   /** 
   * concepto 
   * @return concepto 
   */ 
   public String getConcepto() { 
      return concepto; 
   } 
  
   /** 
   * concepto 
   * @param concepto 
   */ 
   public void setConcepto( String concepto ) { 
      this.concepto=concepto; 
   } 
  
   /** 
   * tipoadq 
   * @return tipoadq 
   */ 
   public String getTipoadq() { 
      return tipoadq; 
   } 
  
   /** 
   * tipoadq 
   * @param tipoadq 
   */ 
   public void setTipoadq( String tipoadq ) { 
      this.tipoadq=tipoadq; 
   } 
  
   /** 
   * fechaaut 
   * @return fechaaut 
   */ 
   public String getFechaaut() { 
      return fechaaut; 
   } 
  
   /** 
   * fechaaut 
   * @param fechaaut 
   */ 
   public void setFechaaut( String fechaaut ) { 
      this.fechaaut=fechaaut; 
   } 
  
   /** 
   * fechacap 
   * @return fechacap 
   */ 
   public String getFechacap() { 
      return fechacap; 
   } 
  
   /** 
   * fechacap 
   * @param fechacap 
   */ 
   public void setFechacap( String fechacap ) { 
      this.fechacap=fechacap; 
   } 
  
   /** 
   * ident 
   * @return ident 
   */ 
   public String getIdent() { 
      return ident; 
   } 
  
   /** 
   * ident 
   * @param ident 
   */ 
   public void setIdent( String ident ) { 
      this.ident=ident; 
   } 
  
   /** 
   * poliza 
   * @return poliza 
   */ 
   public String getPoliza() { 
      return poliza; 
   } 
  
   /** 
   * poliza 
   * @param poliza 
   */ 
   public void setPoliza( String poliza ) { 
      this.poliza=poliza; 
   } 
  
   /** 
   * idhisteje 
   * @return idhisteje 
   */ 
   public String getIdhisteje() { 
      return idhisteje; 
   } 
  
   /** 
   * idhisteje 
   * @param idhisteje 
   */ 
   public void setIdhisteje( String idhisteje ) { 
      this.idhisteje=idhisteje; 
   } 

    /** 
    * Metodo que lee la informacion de rf_tr_histeje, el query viene como parametro
    * Fecha de creacion: 
    * Autor: 
    * Fecha de modificacion: 
    * Modificado por: 
    */ 
    public CachedRowSet select_rf_tr_histeje_bd(Connection con, String query)throws SQLException, Exception{ 
       CachedRowSet rsQuery=null; 
       StringBuffer SQL=new StringBuffer("");  
       try{ 
        rsQuery = new CachedRowSet();
        SQL.append(query);         
        rsQuery.setCommand(SQL.toString());
        rsQuery.execute(con);       
      } //Fin try 
      catch(Exception e){ 
        System.out.println("Ocurrio un error al accesar al metodo select_rf_tr_histeje_bd "+e.getMessage()); 
        System.out.println(SQL.toString()); 
        throw e; 
      } //Fin catch 
      finally{ 
      } //Fin finally 
      return rsQuery;
    } //Fin metodo select_rf_tr_histeje_bd 
  
   /** 
   * Metodo que lee la informacion de rf_tr_histeje 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void select_rf_tr_histeje(Connection con, String clave)throws SQLException, Exception{ 
      Statement stQuery=null; 
      ResultSet rsQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("SELECT a.actividad,a.prioridad,a.proceso,a.subproceso,a.macro,a.unidad,a.estado,a.partida,a.mes,a.tipoeje,a.cxlc,a.docoreco,a.rfc,a.factura,a.importe,a.concepto,a.tipoadq,a.fechaaut,a.fechacap,a.ident,a.poliza,a.idhisteje");  
       SQL.append(" FROM rf_tr_histeje a "); 
       SQL.append(" WHERE a.idhisteje=").append(clave).append(" "); 
       System.out.println(SQL.toString()); 
       rsQuery=stQuery.executeQuery(SQL.toString()); 
       while (rsQuery.next()){ 
          actividad=(rsQuery.getString("actividad")==null) ? "" : rsQuery.getString("actividad"); 
          prioridad=(rsQuery.getString("prioridad")==null) ? "" : rsQuery.getString("prioridad"); 
          proceso=(rsQuery.getString("proceso")==null) ? "" : rsQuery.getString("proceso"); 
          subproceso=(rsQuery.getString("subproceso")==null) ? "" : rsQuery.getString("subproceso"); 
          macro=(rsQuery.getString("macro")==null) ? "" : rsQuery.getString("macro"); 
          unidad=(rsQuery.getString("unidad")==null) ? "" : rsQuery.getString("unidad"); 
          estado=(rsQuery.getString("estado")==null) ? "" : rsQuery.getString("estado"); 
          partida=(rsQuery.getString("partida")==null) ? "" : rsQuery.getString("partida"); 
          mes=(rsQuery.getString("mes")==null) ? "" : rsQuery.getString("mes"); 
          tipoeje=(rsQuery.getString("tipoeje")==null) ? "" : rsQuery.getString("tipoeje"); 
          cxlc=(rsQuery.getString("cxlc")==null) ? "" : rsQuery.getString("cxlc"); 
          docoreco=(rsQuery.getString("docoreco")==null) ? "" : rsQuery.getString("docoreco"); 
          rfc=(rsQuery.getString("rfc")==null) ? "" : rsQuery.getString("rfc"); 
          factura=(rsQuery.getString("factura")==null) ? "" : rsQuery.getString("factura"); 
          importe=(rsQuery.getString("importe")==null) ? "" : rsQuery.getString("importe"); 
          concepto=(rsQuery.getString("concepto")==null) ? "" : rsQuery.getString("concepto"); 
          tipoadq=(rsQuery.getString("tipoadq")==null) ? "" : rsQuery.getString("tipoadq"); 
          fechaaut=(rsQuery.getString("fechaaut")==null) ? "" : rsQuery.getString("fechaaut"); 
          fechacap=(rsQuery.getString("fechacap")==null) ? "" : rsQuery.getString("fechacap"); 
          ident=(rsQuery.getString("ident")==null) ? "" : rsQuery.getString("ident"); 
          poliza=(rsQuery.getString("poliza")==null) ? "" : rsQuery.getString("poliza"); 
          idhisteje=(rsQuery.getString("idhisteje")==null) ? "" : rsQuery.getString("idhisteje"); 
       } // Fin while 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo select_rf_tr_histeje "+e.getMessage()); 
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
   } //Fin metodo select_rf_tr_histeje 
  
   /** 
   * Metodo que inserta la informacion de rf_tr_histeje 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void insert_rf_tr_histeje(Connection con)throws SQLException, Exception{ 
      Statement stQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("INSERT INTO rf_tr_histeje( actividad,prioridad,proceso,subproceso,macro,unidad,estado,partida,mes,tipoeje,cxlc,docoreco,rfc,factura,importe,concepto,tipoadq,fechaaut,fechacap,ident,poliza,idhisteje) ");  
       SQL.append("VALUES("); 
       SQL.append("'").append(actividad).append("',"); 
       SQL.append("'").append(prioridad).append("',"); 
       SQL.append("'").append(proceso).append("',"); 
       SQL.append("'").append(subproceso).append("',"); 
       SQL.append("'").append(macro).append("',"); 
       SQL.append("'").append(unidad).append("',"); 
       SQL.append("'").append(estado).append("',"); 
       SQL.append("'").append(partida).append("',"); 
       SQL.append(mes).append(","); 
       SQL.append(tipoeje).append(","); 
       SQL.append("'").append(cxlc).append("',"); 
       SQL.append("'").append(docoreco).append("',"); 
       SQL.append("'").append(rfc).append("',"); 
       SQL.append("'").append(factura).append("',"); 
       SQL.append(importe).append(","); 
       SQL.append("'").append(concepto).append("',"); 
       SQL.append("'").append(tipoadq).append("',"); 
       SQL.append("'").append(fechaaut).append("',"); 
       SQL.append("'").append(fechacap).append("',"); 
       SQL.append("'").append(ident).append("',"); 
       SQL.append("'").append(poliza).append("',"); 
       SQL.append(idhisteje).append(")"); 
       System.out.println(SQL.toString()); 
       int rs=-1; 
       rs=stQuery.executeUpdate(SQL.toString()); 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo insert_rf_tr_histeje "+e.getMessage()); 
       throw e; 
     } //Fin catch 
     finally{ 
       if (stQuery!=null){ 
         stQuery.close(); 
         stQuery=null; 
       } 
     } //Fin finally 
   } //Fin metodo insert_rf_tr_histeje 
  
   /** 
   * Metodo que modifica la informacion de rf_tr_histeje 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void update_rf_tr_histeje(Connection con, String clave)throws SQLException, Exception{ 
      Statement stQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("UPDATE rf_tr_histeje"); 
       SQL.append(" SET actividad=").append("'").append(actividad).append("',"); 
       SQL.append("prioridad=").append("'").append(prioridad).append("',"); 
       SQL.append("proceso=").append("'").append(proceso).append("',"); 
       SQL.append("subproceso=").append("'").append(subproceso).append("',"); 
       SQL.append("macro=").append("'").append(macro).append("',"); 
       SQL.append("unidad=").append("'").append(unidad).append("',"); 
       SQL.append("estado=").append("'").append(estado).append("',"); 
       SQL.append("partida=").append("'").append(partida).append("',"); 
       SQL.append("mes=").append(mes).append(","); 
       SQL.append("tipoeje=").append(tipoeje).append(","); 
       SQL.append("cxlc=").append("'").append(cxlc).append("',"); 
       SQL.append("docoreco=").append("'").append(docoreco).append("',"); 
       SQL.append("rfc=").append("'").append(rfc).append("',"); 
       SQL.append("factura=").append("'").append(factura).append("',"); 
       SQL.append("importe=").append(importe).append(","); 
       SQL.append("concepto=").append("'").append(concepto).append("',"); 
       SQL.append("tipoadq=").append("'").append(tipoadq).append("',"); 
       SQL.append("fechaaut=").append("'").append(fechaaut).append("',"); 
       SQL.append("fechacap=").append("'").append(fechacap).append("',"); 
       SQL.append("ident=").append("'").append(ident).append("',"); 
       SQL.append("poliza=").append("'").append(poliza).append("',"); 
       SQL.append("idhisteje=").append(idhisteje); 
       SQL.append(" WHERE idhisteje=").append(clave).append(" "); 
       System.out.println(SQL.toString()); 
       int rs=-1; 
       rs=stQuery.executeUpdate(SQL.toString()); 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo update_rf_tr_histeje "+e.getMessage()); 
       throw e; 
     } //Fin catch 
     finally{ 
       if (stQuery!=null){ 
         stQuery.close(); 
         stQuery=null; 
       } 
     } //Fin finally 
   } //Fin metodo update_rf_tr_histeje 
  
   /** 
   * Metodo que borra la informacion de rf_tr_histeje 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void delete_rf_tr_histeje(Connection con, String clave)throws SQLException, Exception{ 
      Statement stQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("DELETE FROM rf_tr_histeje a "); 
       SQL.append("WHERE a.idhisteje=").append(clave).append(" "); 
       System.out.println(SQL.toString()); 
       int rs=-1; 
       rs=stQuery.executeUpdate(SQL.toString()); 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo delete_rf_tr_histeje "+e.getMessage()); 
       throw e; 
     } //Fin catch 
     finally{ 
       if (stQuery!=null){ 
         stQuery.close(); 
         stQuery=null; 
       } 
     } //Fin finally 
   } //Fin metodo delete_rf_tr_histeje 
} //Fin clase HistoricoEje 
    
