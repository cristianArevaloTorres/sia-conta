package sia.rf.contabilidad.sistemas.mipf.modDescarga;

import java.sql.*;

import sun.jdbc.rowset.CachedRowSet;

public class CuentaPorLiquidar{ 
   public CuentaPorLiquidar(){ 
   } 
   
   private String unidad; 
   private String mes; 
   private String tipoeje; 
   private String cxlc; 
   private String anticipo; 
   private String totbruto; 
   private String totneto; 
   private String concepto; 
   private String fechaaut; 
   private String fechacap; 
   private String ident; 
   private String poliza; 
   private String pagocen; 
   private String ctadisp; 
   private String cajachi; 
   private String oridocto; 
   private String ejercicio; 
   private String referencia; 
   private String idctaxliq; 
  
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
   * anticipo 
   * @return anticipo 
   */ 
   public String getAnticipo() { 
      return anticipo; 
   } 
  
   /** 
   * anticipo 
   * @param anticipo 
   */ 
   public void setAnticipo( String anticipo ) { 
      this.anticipo=anticipo; 
   } 
  
   /** 
   * totbruto 
   * @return totbruto 
   */ 
   public String getTotbruto() { 
      return totbruto; 
   } 
  
   /** 
   * totbruto 
   * @param totbruto 
   */ 
   public void setTotbruto( String totbruto ) { 
      this.totbruto=totbruto; 
   } 
  
   /** 
   * totneto 
   * @return totneto 
   */ 
   public String getTotneto() { 
      return totneto; 
   } 
  
   /** 
   * totneto 
   * @param totneto 
   */ 
   public void setTotneto( String totneto ) { 
      this.totneto=totneto; 
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
   * pagocen 
   * @return pagocen 
   */ 
   public String getPagocen() { 
      return pagocen; 
   } 
  
   /** 
   * pagocen 
   * @param pagocen 
   */ 
   public void setPagocen( String pagocen ) { 
      this.pagocen=pagocen; 
   } 
  
   /** 
   * ctadisp 
   * @return ctadisp 
   */ 
   public String getCtadisp() { 
      return ctadisp; 
   } 
  
   /** 
   * ctadisp 
   * @param ctadisp 
   */ 
   public void setCtadisp( String ctadisp ) { 
      this.ctadisp=ctadisp; 
   } 
  
   /** 
   * cajachi 
   * @return cajachi 
   */ 
   public String getCajachi() { 
      return cajachi; 
   } 
  
   /** 
   * cajachi 
   * @param cajachi 
   */ 
   public void setCajachi( String cajachi ) { 
      this.cajachi=cajachi; 
   } 
  
   /** 
   * oridocto 
   * @return oridocto 
   */ 
   public String getOridocto() { 
      return oridocto; 
   } 
  
   /** 
   * oridocto 
   * @param oridocto 
   */ 
   public void setOridocto( String oridocto ) { 
      this.oridocto=oridocto; 
   } 
  
   /** 
   * ejercicio 
   * @return ejercicio 
   */ 
   public String getEjercicio() { 
      return ejercicio; 
   } 
  
   /** 
   * ejercicio 
   * @param ejercicio 
   */ 
   public void setEjercicio( String ejercicio ) { 
      this.ejercicio=ejercicio; 
   } 
  
   /** 
   * referencia 
   * @return referencia 
   */ 
   public String getReferencia() { 
      return referencia; 
   } 
  
   /** 
   * referencia 
   * @param referencia 
   */ 
   public void setReferencia( String referencia ) { 
      this.referencia=referencia; 
   } 
  
   /** 
   * idctaxliq 
   * @return idctaxliq 
   */ 
   public String getIdctaxliq() { 
      return idctaxliq; 
   } 
  
   /** 
   * idctaxliq 
   * @param idctaxliq 
   */ 
   public void setIdctaxliq( String idctaxliq ) { 
      this.idctaxliq=idctaxliq; 
   } 
  
   /** 
   * Metodo que lee la informacion de rf_tr_ctaxliq, el query viene como parametro
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public CachedRowSet select_rf_tr_ctaxliq_bd(Connection con, String query)throws SQLException, Exception{       
      CachedRowSet rsQuery=null; 
      StringBuffer SQL=new StringBuffer("");
      try{ 
       rsQuery = new CachedRowSet();       
       SQL.append(query);         
       rsQuery.setCommand(SQL.toString());
       rsQuery.execute(con);       
     } //Fin try 
     catch(Exception e){        
       System.out.println("Ocurrio un error al accesar al metodo select_rf_tr_ctaxliq_bd "+e.getMessage()); 
       System.out.println(SQL.toString()); 
       throw e; 
     } //Fin catch 
     finally{ 
     } //Fin finally 
     return rsQuery;
   } //Fin metodo select_rf_tr_ctaxliq_bd 

    /** 
    * Metodo que lee la informacion de rf_tr_ctaxliq 
    * Fecha de creacion: 
    * Autor: 
    * Fecha de modificacion: 
    * Modificado por: 
    */ 
    public void select_rf_tr_ctaxliq(Connection con, String clave)throws SQLException, Exception{ 
       Statement stQuery=null; 
       ResultSet rsQuery=null; 
       try{ 
        stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
        StringBuffer SQL=new StringBuffer("SELECT a.unidad,a.mes,a.tipoeje,a.cxlc,a.anticipo,a.totbruto,a.totneto,a.concepto,a.fechaaut,a.fechacap,a.ident,a.poliza,a.pagocen,a.ctadisp,a.cajachi,a.oridocto,a.ejercicio,a.referencia,a.idctaxliq");  
        SQL.append(" FROM rf_tr_ctaxliq a "); 
        SQL.append(" WHERE a.idctaxliq=").append(clave).append(" "); 
        System.out.println(SQL.toString()); 
        rsQuery=stQuery.executeQuery(SQL.toString()); 
        while (rsQuery.next()){ 
           unidad=(rsQuery.getString("unidad")==null) ? "" : rsQuery.getString("unidad"); 
           mes=(rsQuery.getString("mes")==null) ? "" : rsQuery.getString("mes"); 
           tipoeje=(rsQuery.getString("tipoeje")==null) ? "" : rsQuery.getString("tipoeje"); 
           cxlc=(rsQuery.getString("cxlc")==null) ? "" : rsQuery.getString("cxlc"); 
           anticipo=(rsQuery.getString("anticipo")==null) ? "" : rsQuery.getString("anticipo"); 
           totbruto=(rsQuery.getString("totbruto")==null) ? "" : rsQuery.getString("totbruto"); 
           totneto=(rsQuery.getString("totneto")==null) ? "" : rsQuery.getString("totneto"); 
           concepto=(rsQuery.getString("concepto")==null) ? "" : rsQuery.getString("concepto"); 
           fechaaut=(rsQuery.getString("fechaaut")==null) ? "" : rsQuery.getString("fechaaut"); 
           fechacap=(rsQuery.getString("fechacap")==null) ? "" : rsQuery.getString("fechacap"); 
           ident=(rsQuery.getString("ident")==null) ? "" : rsQuery.getString("ident"); 
           poliza=(rsQuery.getString("poliza")==null) ? "" : rsQuery.getString("poliza"); 
           pagocen=(rsQuery.getString("pagocen")==null) ? "" : rsQuery.getString("pagocen"); 
           ctadisp=(rsQuery.getString("ctadisp")==null) ? "" : rsQuery.getString("ctadisp"); 
           cajachi=(rsQuery.getString("cajachi")==null) ? "" : rsQuery.getString("cajachi"); 
           oridocto=(rsQuery.getString("oridocto")==null) ? "" : rsQuery.getString("oridocto"); 
           ejercicio=(rsQuery.getString("ejercicio")==null) ? "" : rsQuery.getString("ejercicio"); 
           referencia=(rsQuery.getString("referencia")==null) ? "" : rsQuery.getString("referencia"); 
           idctaxliq=(rsQuery.getString("idctaxliq")==null) ? "" : rsQuery.getString("idctaxliq"); 
        } // Fin while 
      } //Fin try 
      catch(Exception e){ 
        System.out.println("Ocurrio un error al accesar al metodo select_rf_tr_ctaxliq "+e.getMessage()); 
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
    } //Fin metodo select_rf_tr_ctaxliq 
  
   /** 
   * Metodo que inserta la informacion de rf_tr_ctaxliq 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void insert_rf_tr_ctaxliq(Connection con)throws SQLException, Exception{ 
      Statement stQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("INSERT INTO rf_tr_ctaxliq( unidad,mes,tipoeje,cxlc,anticipo,totbruto,totneto,concepto,fechaaut,fechacap,ident,poliza,pagocen,ctadisp,cajachi,oridocto,ejercicio,referencia,idctaxliq) ");  
       SQL.append("VALUES("); 
       SQL.append("'").append(unidad).append("',"); 
       SQL.append(mes).append(","); 
       SQL.append(tipoeje).append(","); 
       SQL.append("'").append(cxlc).append("',"); 
       SQL.append(anticipo).append(","); 
       SQL.append(totbruto).append(","); 
       SQL.append(totneto).append(","); 
       SQL.append("'").append(concepto).append("',"); 
       SQL.append("'").append(fechaaut).append("',"); 
       SQL.append("'").append(fechacap).append("',"); 
       SQL.append("'").append(ident).append("',"); 
       SQL.append("'").append(poliza).append("',"); 
       SQL.append(pagocen).append(","); 
       SQL.append("'").append(ctadisp).append("',"); 
       SQL.append(cajachi).append(","); 
       SQL.append("'").append(oridocto).append("',"); 
       SQL.append(ejercicio).append(","); 
       SQL.append("'").append(referencia).append("',"); 
       SQL.append(idctaxliq).append(")"); 
       System.out.println(SQL.toString()); 
       int rs=-1; 
       rs=stQuery.executeUpdate(SQL.toString()); 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo insert_rf_tr_ctaxliq "+e.getMessage()); 
       throw e; 
     } //Fin catch 
     finally{ 
       if (stQuery!=null){ 
         stQuery.close(); 
         stQuery=null; 
       } 
     } //Fin finally 
   } //Fin metodo insert_rf_tr_ctaxliq 
  
   /** 
   * Metodo que modifica la informacion de rf_tr_ctaxliq 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void update_rf_tr_ctaxliq(Connection con, String clave)throws SQLException, Exception{ 
      Statement stQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("UPDATE rf_tr_ctaxliq"); 
       SQL.append(" SET unidad=").append("'").append(unidad).append("',"); 
       SQL.append("mes=").append(mes).append(","); 
       SQL.append("tipoeje=").append(tipoeje).append(","); 
       SQL.append("cxlc=").append("'").append(cxlc).append("',"); 
       SQL.append("anticipo=").append(anticipo).append(","); 
       SQL.append("totbruto=").append(totbruto).append(","); 
       SQL.append("totneto=").append(totneto).append(","); 
       SQL.append("concepto=").append("'").append(concepto).append("',"); 
       SQL.append("fechaaut=").append("'").append(fechaaut).append("',"); 
       SQL.append("fechacap=").append("'").append(fechacap).append("',"); 
       SQL.append("ident=").append("'").append(ident).append("',"); 
       SQL.append("poliza=").append("'").append(poliza).append("',"); 
       SQL.append("pagocen=").append(pagocen).append(","); 
       SQL.append("ctadisp=").append("'").append(ctadisp).append("',"); 
       SQL.append("cajachi=").append(cajachi).append(","); 
       SQL.append("oridocto=").append("'").append(oridocto).append("',"); 
       SQL.append("ejercicio=").append(ejercicio).append(","); 
       SQL.append("referencia=").append("'").append(referencia).append("',"); 
       SQL.append("idctaxliq=").append(idctaxliq); 
       SQL.append(" WHERE idctaxliq=").append(clave).append(" "); 
       System.out.println(SQL.toString()); 
       int rs=-1; 
       rs=stQuery.executeUpdate(SQL.toString()); 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo update_rf_tr_ctaxliq "+e.getMessage()); 
       throw e; 
     } //Fin catch 
     finally{ 
       if (stQuery!=null){ 
         stQuery.close(); 
         stQuery=null; 
       } 
     } //Fin finally 
   } //Fin metodo update_rf_tr_ctaxliq 
  
   /** 
   * Metodo que borra la informacion de rf_tr_ctaxliq 
   * Fecha de creacion: 
   * Autor: 
   * Fecha de modificacion: 
   * Modificado por: 
   */ 
   public void delete_rf_tr_ctaxliq(Connection con, String clave)throws SQLException, Exception{ 
      Statement stQuery=null; 
      try{ 
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       StringBuffer SQL=new StringBuffer("DELETE FROM rf_tr_ctaxliq a "); 
       SQL.append("WHERE a.idctaxliq=").append(clave).append(" "); 
       System.out.println(SQL.toString()); 
       int rs=-1; 
       rs=stQuery.executeUpdate(SQL.toString()); 
     } //Fin try 
     catch(Exception e){ 
       System.out.println("Ocurrio un error al accesar al metodo delete_rf_tr_ctaxliq "+e.getMessage()); 
       throw e; 
     } //Fin catch 
     finally{ 
       if (stQuery!=null){ 
         stQuery.close(); 
         stQuery=null; 
       } 
     } //Fin finally 
   } //Fin metodo delete_rf_tr_ctaxliq 
} //Fin clase CuentaPorLiquidar 
    
