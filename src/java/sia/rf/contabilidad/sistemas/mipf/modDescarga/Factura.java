package sia.rf.contabilidad.sistemas.mipf.modDescarga;

import java.sql.*;

import sun.jdbc.rowset.CachedRowSet;

public class Factura{ 
   public Factura(){ 
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
   private String tasaiva; 
   private String iva; 
   private String penaliza; 
   private String otros; 
   private String cveret; 
   private String fechaaut; 
   private String fechacap; 
   private String ident; 
   private String poliza; 
   private String id_conta; 
   private String idfactura; 
  
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
   * tasaiva 
   * @return tasaiva 
   */ 
   public String getTasaiva() { 
      return tasaiva; 
   } 
  
   /** 
   * tasaiva 
   * @param tasaiva 
   */ 
   public void setTasaiva( String tasaiva ) { 
      this.tasaiva=tasaiva; 
   } 
  
   /** 
   * iva 
   * @return iva 
   */ 
   public String getIva() { 
      return iva; 
   } 
  
   /** 
   * iva 
   * @param iva 
   */ 
   public void setIva( String iva ) { 
      this.iva=iva; 
   } 
  
   /** 
   * penaliza 
   * @return penaliza 
   */ 
   public String getPenaliza() { 
      return penaliza; 
   } 
  
   /** 
   * penaliza 
   * @param penaliza 
   */ 
   public void setPenaliza( String penaliza ) { 
      this.penaliza=penaliza; 
   } 
  
   /** 
   * otros 
   * @return otros 
   */ 
   public String getOtros() { 
      return otros; 
   } 
  
   /** 
   * otros 
   * @param otros 
   */ 
   public void setOtros( String otros ) { 
      this.otros=otros; 
   } 
  
   /** 
   * cveret 
   * @return cveret 
   */ 
   public String getCveret() { 
      return cveret; 
   } 
  
   /** 
   * cveret 
   * @param cveret 
   */ 
   public void setCveret( String cveret ) { 
      this.cveret=cveret; 
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
   * id_conta 
   * @return id_conta 
   */ 
   public String getId_conta() { 
      return id_conta; 
   } 
  
   /** 
   * id_conta 
   * @param id_conta 
   */ 
   public void setId_conta( String id_conta ) { 
      this.id_conta=id_conta; 
   } 
  
   /** 
   * idfactura 
   * @return idfactura 
   */ 
   public String getIdfactura() { 
      return idfactura; 
   } 
  
   /** 
   * idfactura 
   * @param idfactura 
   */ 
   public void setIdfactura( String idfactura ) { 
      this.idfactura=idfactura; 
   } 

    /** 
    * Metodo que lee la informacion de rf_tr_factura, el query viene como parametro
    * Fecha de creacion: 
    * Autor: 
    * Fecha de modificacion: 
    * Modificado por: 
    */ 
    public CachedRowSet select_rf_tr_factura_bd(Connection con, String query)throws SQLException, Exception{ 
       CachedRowSet rsQuery=null; 
        StringBuffer SQL=new StringBuffer("");  
       try{ 
        rsQuery = new CachedRowSet();
        SQL.append(query);   

        rsQuery.setCommand(SQL.toString());
        rsQuery.execute(con);       
      } //Fin try 
      catch(Exception e){ 
        System.out.println("Ocurrio un error al accesar al metodo select_rf_tr_factura_bd "+e.getMessage()); 
        System.out.println(SQL.toString()); 
        throw e; 
      } //Fin catch 
      finally{ 
      } //Fin finally 
      return rsQuery;
    } //Fin metodo select_rf_tr_factura_bd 
  
   /** 
   * Metodo que lee la informacion de rf_tr_factura 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void select_rf_tr_factura(Connection con, String clave)throws SQLException, Exception{ 
      Statement stQuery=null; 
      ResultSet rsQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("SELECT a.actividad,a.prioridad,a.proceso,a.subproceso,a.macro,a.unidad,a.estado,a.partida,a.mes,a.tipoeje,a.cxlc,a.docoreco,a.rfc,a.factura,a.importe,a.tasaiva,a.iva,a.penaliza,a.otros,a.cveret,a.fechaaut,a.fechacap,a.ident,a.poliza,a.id_conta,a.idfactura");  
       SQL.append(" FROM rf_tr_factura a "); 
       SQL.append(" WHERE a.idfactura=").append(clave).append(" "); 
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
          tasaiva=(rsQuery.getString("tasaiva")==null) ? "" : rsQuery.getString("tasaiva"); 
          iva=(rsQuery.getString("iva")==null) ? "" : rsQuery.getString("iva"); 
          penaliza=(rsQuery.getString("penaliza")==null) ? "" : rsQuery.getString("penaliza"); 
          otros=(rsQuery.getString("otros")==null) ? "" : rsQuery.getString("otros"); 
          cveret=(rsQuery.getString("cveret")==null) ? "" : rsQuery.getString("cveret"); 
          fechaaut=(rsQuery.getString("fechaaut")==null) ? "" : rsQuery.getString("fechaaut"); 
          fechacap=(rsQuery.getString("fechacap")==null) ? "" : rsQuery.getString("fechacap"); 
          ident=(rsQuery.getString("ident")==null) ? "" : rsQuery.getString("ident"); 
          poliza=(rsQuery.getString("poliza")==null) ? "" : rsQuery.getString("poliza"); 
          id_conta=(rsQuery.getString("id_conta")==null) ? "" : rsQuery.getString("id_conta"); 
          idfactura=(rsQuery.getString("idfactura")==null) ? "" : rsQuery.getString("idfactura"); 
       } // Fin while 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo select_rf_tr_factura "+e.getMessage()); 
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
   } //Fin metodo select_rf_tr_factura 
  
   /** 
   * Metodo que inserta la informacion de rf_tr_factura 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void insert_rf_tr_factura(Connection con)throws SQLException, Exception{ 
      Statement stQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("INSERT INTO rf_tr_factura( actividad,prioridad,proceso,subproceso,macro,unidad,estado,partida,mes,tipoeje,cxlc,docoreco,rfc,factura,importe,tasaiva,iva,penaliza,otros,cveret,fechaaut,fechacap,ident,poliza,id_conta,idfactura) ");  
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
       SQL.append(tasaiva).append(","); 
       SQL.append(iva).append(","); 
       SQL.append(penaliza).append(","); 
       SQL.append(otros).append(","); 
       SQL.append("'").append(cveret).append("',"); 
       SQL.append("'").append(fechaaut).append("',"); 
       SQL.append("'").append(fechacap).append("',"); 
       SQL.append("'").append(ident).append("',"); 
       SQL.append("'").append(poliza).append("',"); 
       SQL.append("'").append(id_conta).append("',"); 
       SQL.append(idfactura).append(")"); 
       System.out.println(SQL.toString()); 
       int rs=-1; 
       rs=stQuery.executeUpdate(SQL.toString()); 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo insert_rf_tr_factura "+e.getMessage()); 
       throw e; 
     } //Fin catch 
     finally{ 
       if (stQuery!=null){ 
         stQuery.close(); 
         stQuery=null; 
       } 
     } //Fin finally 
   } //Fin metodo insert_rf_tr_factura 
  
   /** 
   * Metodo que modifica la informacion de rf_tr_factura 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void update_rf_tr_factura(Connection con, String clave)throws SQLException, Exception{ 
      Statement stQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("UPDATE rf_tr_factura"); 
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
       SQL.append("tasaiva=").append(tasaiva).append(","); 
       SQL.append("iva=").append(iva).append(","); 
       SQL.append("penaliza=").append(penaliza).append(","); 
       SQL.append("otros=").append(otros).append(","); 
       SQL.append("cveret=").append("'").append(cveret).append("',"); 
       SQL.append("fechaaut=").append("'").append(fechaaut).append("',"); 
       SQL.append("fechacap=").append("'").append(fechacap).append("',"); 
       SQL.append("ident=").append("'").append(ident).append("',"); 
       SQL.append("poliza=").append("'").append(poliza).append("',"); 
       SQL.append("id_conta=").append("'").append(id_conta).append("',"); 
       SQL.append("idfactura=").append(idfactura); 
       SQL.append(" WHERE idfactura= ").append(clave).append(" "); 
       System.out.println(SQL.toString()); 
       int rs=-1; 
       rs=stQuery.executeUpdate(SQL.toString()); 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo update_rf_tr_factura "+e.getMessage()); 
       throw e; 
     } //Fin catch 
     finally{ 
       if (stQuery!=null){ 
         stQuery.close(); 
         stQuery=null; 
       } 
     } //Fin finally 
   } //Fin metodo update_rf_tr_factura 
  
   /** 
   * Metodo que borra la informacion de rf_tr_factura 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void delete_rf_tr_factura(Connection con, String clave)throws SQLException, Exception{ 
      Statement stQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("DELETE FROM rf_tr_factura a "); 
       SQL.append("WHERE a.idfactura= ").append(clave).append(" "); 
       System.out.println(SQL.toString()); 
       int rs=-1; 
       rs=stQuery.executeUpdate(SQL.toString()); 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo delete_rf_tr_factura "+e.getMessage()); 
       throw e; 
     } //Fin catch 
     finally{ 
       if (stQuery!=null){ 
         stQuery.close(); 
         stQuery=null; 
       } 
     } //Fin finally 
   } //Fin metodo delete_rf_tr_factura 
} //Fin clase Factura 
    
