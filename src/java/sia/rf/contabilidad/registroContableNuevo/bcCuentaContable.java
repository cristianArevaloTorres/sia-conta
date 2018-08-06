package sia.rf.contabilidad.registroContableNuevo;

import java.sql.*;


import sia.db.dao.DaoFactory;
import sia.db.sql.Sentencias;
import sia.db.sql.SentenciasCRS;
import sia.rf.contabilidad.registroContableNuevo.bcConsultaPerfiles;

public class bcCuentaContable{

   public bcCuentaContable(){
   }
   
   private String fecha_vig_ini;
   private String fecha_vig_fin;
   private String ene_cargo;
   private String feb_cargo;
   private String mar_cargo;
   private String abr_cargo;
   private String may_cargo;
   private String jun_cargo;
   private String jul_cargo;
   private String ago_cargo;
   private String sep_cargo;
   private String oct_cargo;
   private String nov_cargo;
   private String dic_cargo;
   private String ene_abono;
   private String feb_abono;
   private String mar_abono;
   private String abr_abono;
   private String may_abono;
   private String jun_abono;
   private String jul_abono;
   private String ago_abono;
   private String sep_abono;
   private String oct_abono;
   private String nov_abono;
   private String dic_abono;
   private String ene_cargo_acum;
   private String feb_cargo_acum;
   private String mar_cargo_acum;
   private String abr_cargo_acum;
   private String may_cargo_acum;
   private String jun_cargo_acum;
   private String jul_cargo_acum;
   private String ago_cargo_acum;
   private String sep_cargo_acum;
   private String oct_cargo_acum;
   private String nov_cargo_acum;
   private String dic_cargo_acum;
   private String ene_abono_acum;
   private String feb_abono_acum;
   private String mar_abono_acum;
   private String abr_abono_acum;
   private String may_abono_acum;
   private String jun_abono_acum;
   private String jul_abono_acum;
   private String ago_abono_acum;
   private String sep_abono_acum;
   private String oct_abono_acum;
   private String nov_abono_acum;
   private String dic_abono_acum;
   private String codigo_registro;
   private String registro;
   private String id_catalogo_cuenta;
   private String cuenta_contable_id;
   private String cuenta_mayor_id;
   private String cuenta_contable;
   private String nivel;
   private String descripcion;

   /**
   * fecha_vig_ini
   * @return fecha_vig_ini
   */
   public String getFecha_vig_ini() {
      return fecha_vig_ini;
   }
 
   /**
   * fecha_vig_ini
   * @param fecha_vig_ini
   */
   public void setFecha_vig_ini( String fecha_vig_ini ) {
      this.fecha_vig_ini=fecha_vig_ini;
   }
 
   /**
   * fecha_vig_fin
   * @return fecha_vig_fin
   */
   public String getFecha_vig_fin() {
      return fecha_vig_fin;
   }
 
   /**
   * fecha_vig_fin
   * @param fecha_vig_fin
   */
   public void setFecha_vig_fin( String fecha_vig_fin ) {
      this.fecha_vig_fin=fecha_vig_fin;
   }
 
   /**
   * ene_cargo
   * @return ene_cargo
   */
   public String getEne_cargo() {
      return ene_cargo;
   }
 
   /**
   * ene_cargo
   * @param ene_cargo
   */
   public void setEne_cargo( String ene_cargo ) {
      this.ene_cargo=ene_cargo;
   }
 
   /**
   * feb_cargo
   * @return feb_cargo
   */
   public String getFeb_cargo() {
      return feb_cargo;
   }
 
   /**
   * feb_cargo
   * @param feb_cargo
   */
   public void setFeb_cargo( String feb_cargo ) {
      this.feb_cargo=feb_cargo;
   }
 
   /**
   * mar_cargo
   * @return mar_cargo
   */
   public String getMar_cargo() {
      return mar_cargo;
   }
 
   /**
   * mar_cargo
   * @param mar_cargo
   */
   public void setMar_cargo( String mar_cargo ) {
      this.mar_cargo=mar_cargo;
   }
 
   /**
   * abr_cargo
   * @return abr_cargo
   */
   public String getAbr_cargo() {
      return abr_cargo;
   }
 
   /**
   * abr_cargo
   * @param abr_cargo
   */
   public void setAbr_cargo( String abr_cargo ) {
      this.abr_cargo=abr_cargo;
   }
 
   /**
   * may_cargo
   * @return may_cargo
   */
   public String getMay_cargo() {
      return may_cargo;
   }
 
   /**
   * may_cargo
   * @param may_cargo
   */
   public void setMay_cargo( String may_cargo ) {
      this.may_cargo=may_cargo;
   }
 
   /**
   * jun_cargo
   * @return jun_cargo
   */
   public String getJun_cargo() {
      return jun_cargo;
   }
 
   /**
   * jun_cargo
   * @param jun_cargo
   */
   public void setJun_cargo( String jun_cargo ) {
      this.jun_cargo=jun_cargo;
   }
 
   /**
   * jul_cargo
   * @return jul_cargo
   */
   public String getJul_cargo() {
      return jul_cargo;
   }
 
   /**
   * jul_cargo
   * @param jul_cargo
   */
   public void setJul_cargo( String jul_cargo ) {
      this.jul_cargo=jul_cargo;
   }
 
   /**
   * ago_cargo
   * @return ago_cargo
   */
   public String getAgo_cargo() {
      return ago_cargo;
   }
 
   /**
   * ago_cargo
   * @param ago_cargo
   */
   public void setAgo_cargo( String ago_cargo ) {
      this.ago_cargo=ago_cargo;
   }
 
   /**
   * sep_cargo
   * @return sep_cargo
   */
   public String getSep_cargo() {
      return sep_cargo;
   }
 
   /**
   * sep_cargo
   * @param sep_cargo
   */
   public void setSep_cargo( String sep_cargo ) {
      this.sep_cargo=sep_cargo;
   }
 
   /**
   * oct_cargo
   * @return oct_cargo
   */
   public String getOct_cargo() {
      return oct_cargo;
   }
 
   /**
   * oct_cargo
   * @param oct_cargo
   */
   public void setOct_cargo( String oct_cargo ) {
      this.oct_cargo=oct_cargo;
   }
 
   /**
   * nov_cargo
   * @return nov_cargo
   */
   public String getNov_cargo() {
      return nov_cargo;
   }
 
   /**
   * nov_cargo
   * @param nov_cargo
   */
   public void setNov_cargo( String nov_cargo ) {
      this.nov_cargo=nov_cargo;
   }
 
   /**
   * dic_cargo
   * @return dic_cargo
   */
   public String getDic_cargo() {
      return dic_cargo;
   }
 
   /**
   * dic_cargo
   * @param dic_cargo
   */
   public void setDic_cargo( String dic_cargo ) {
      this.dic_cargo=dic_cargo;
   }
 
   /**
   * ene_abono
   * @return ene_abono
   */
   public String getEne_abono() {
      return ene_abono;
   }
 
   /**
   * ene_abono
   * @param ene_abono
   */
   public void setEne_abono( String ene_abono ) {
      this.ene_abono=ene_abono;
   }
 
   /**
   * feb_abono
   * @return feb_abono
   */
   public String getFeb_abono() {
      return feb_abono;
   }
 
   /**
   * feb_abono
   * @param feb_abono
   */
   public void setFeb_abono( String feb_abono ) {
      this.feb_abono=feb_abono;
   }
 
   /**
   * mar_abono
   * @return mar_abono
   */
   public String getMar_abono() {
      return mar_abono;
   }
 
   /**
   * mar_abono
   * @param mar_abono
   */
   public void setMar_abono( String mar_abono ) {
      this.mar_abono=mar_abono;
   }
 
   /**
   * abr_abono
   * @return abr_abono
   */
   public String getAbr_abono() {
      return abr_abono;
   }
 
   /**
   * abr_abono
   * @param abr_abono
   */
   public void setAbr_abono( String abr_abono ) {
      this.abr_abono=abr_abono;
   }
 
   /**
   * may_abono
   * @return may_abono
   */
   public String getMay_abono() {
      return may_abono;
   }
 
   /**
   * may_abono
   * @param may_abono
   */
   public void setMay_abono( String may_abono ) {
      this.may_abono=may_abono;
   }
 
   /**
   * jun_abono
   * @return jun_abono
   */
   public String getJun_abono() {
      return jun_abono;
   }
 
   /**
   * jun_abono
   * @param jun_abono
   */
   public void setJun_abono( String jun_abono ) {
      this.jun_abono=jun_abono;
   }
 
   /**
   * jul_abono
   * @return jul_abono
   */
   public String getJul_abono() {
      return jul_abono;
   }
 
   /**
   * jul_abono
   * @param jul_abono
   */
   public void setJul_abono( String jul_abono ) {
      this.jul_abono=jul_abono;
   }
 
   /**
   * ago_abono
   * @return ago_abono
   */
   public String getAgo_abono() {
      return ago_abono;
   }
 
   /**
   * ago_abono
   * @param ago_abono
   */
   public void setAgo_abono( String ago_abono ) {
      this.ago_abono=ago_abono;
   }
 
   /**
   * sep_abono
   * @return sep_abono
   */
   public String getSep_abono() {
      return sep_abono;
   }
 
   /**
   * sep_abono
   * @param sep_abono
   */
   public void setSep_abono( String sep_abono ) {
      this.sep_abono=sep_abono;
   }
 
   /**
   * oct_abono
   * @return oct_abono
   */
   public String getOct_abono() {
      return oct_abono;
   }
 
   /**
   * oct_abono
   * @param oct_abono
   */
   public void setOct_abono( String oct_abono ) {
      this.oct_abono=oct_abono;
   }
 
   /**
   * nov_abono
   * @return nov_abono
   */
   public String getNov_abono() {
      return nov_abono;
   }
 
   /**
   * nov_abono
   * @param nov_abono
   */
   public void setNov_abono( String nov_abono ) {
      this.nov_abono=nov_abono;
   }
 
   /**
   * dic_abono
   * @return dic_abono
   */
   public String getDic_abono() {
      return dic_abono;
   }
 
   /**
   * dic_abono
   * @param dic_abono
   */
   public void setDic_abono( String dic_abono ) {
      this.dic_abono=dic_abono;
   }
 
   /**
   * ene_cargo_acum
   * @return ene_cargo_acum
   */
   public String getEne_cargo_acum() {
      return ene_cargo_acum;
   }
 
   /**
   * ene_cargo_acum
   * @param ene_cargo_acum
   */
   public void setEne_cargo_acum( String ene_cargo_acum ) {
      this.ene_cargo_acum=ene_cargo_acum;
   }
 
   /**
   * feb_cargo_acum
   * @return feb_cargo_acum
   */
   public String getFeb_cargo_acum() {
      return feb_cargo_acum;
   }
 
   /**
   * feb_cargo_acum
   * @param feb_cargo_acum
   */
   public void setFeb_cargo_acum( String feb_cargo_acum ) {
      this.feb_cargo_acum=feb_cargo_acum;
   }
 
   /**
   * mar_cargo_acum
   * @return mar_cargo_acum
   */
   public String getMar_cargo_acum() {
      return mar_cargo_acum;
   }
 
   /**
   * mar_cargo_acum
   * @param mar_cargo_acum
   */
   public void setMar_cargo_acum( String mar_cargo_acum ) {
      this.mar_cargo_acum=mar_cargo_acum;
   }
 
   /**
   * abr_cargo_acum
   * @return abr_cargo_acum
   */
   public String getAbr_cargo_acum() {
      return abr_cargo_acum;
   }
 
   /**
   * abr_cargo_acum
   * @param abr_cargo_acum
   */
   public void setAbr_cargo_acum( String abr_cargo_acum ) {
      this.abr_cargo_acum=abr_cargo_acum;
   }
 
   /**
   * may_cargo_acum
   * @return may_cargo_acum
   */
   public String getMay_cargo_acum() {
      return may_cargo_acum;
   }
 
   /**
   * may_cargo_acum
   * @param may_cargo_acum
   */
   public void setMay_cargo_acum( String may_cargo_acum ) {
      this.may_cargo_acum=may_cargo_acum;
   }
 
   /**
   * jun_cargo_acum
   * @return jun_cargo_acum
   */
   public String getJun_cargo_acum() {
      return jun_cargo_acum;
   }
 
   /**
   * jun_cargo_acum
   * @param jun_cargo_acum
   */
   public void setJun_cargo_acum( String jun_cargo_acum ) {
      this.jun_cargo_acum=jun_cargo_acum;
   }
 
   /**
   * jul_cargo_acum
   * @return jul_cargo_acum
   */
   public String getJul_cargo_acum() {
      return jul_cargo_acum;
   }
 
   /**
   * jul_cargo_acum
   * @param jul_cargo_acum
   */
   public void setJul_cargo_acum( String jul_cargo_acum ) {
      this.jul_cargo_acum=jul_cargo_acum;
   }
 
   /**
   * ago_cargo_acum
   * @return ago_cargo_acum
   */
   public String getAgo_cargo_acum() {
      return ago_cargo_acum;
   }
 
   /**
   * ago_cargo_acum
   * @param ago_cargo_acum
   */
   public void setAgo_cargo_acum( String ago_cargo_acum ) {
      this.ago_cargo_acum=ago_cargo_acum;
   }
 
   /**
   * sep_cargo_acum
   * @return sep_cargo_acum
   */
   public String getSep_cargo_acum() {
      return sep_cargo_acum;
   }
 
   /**
   * sep_cargo_acum
   * @param sep_cargo_acum
   */
   public void setSep_cargo_acum( String sep_cargo_acum ) {
      this.sep_cargo_acum=sep_cargo_acum;
   }
 
   /**
   * oct_cargo_acum
   * @return oct_cargo_acum
   */
   public String getOct_cargo_acum() {
      return oct_cargo_acum;
   }
 
   /**
   * oct_cargo_acum
   * @param oct_cargo_acum
   */
   public void setOct_cargo_acum( String oct_cargo_acum ) {
      this.oct_cargo_acum=oct_cargo_acum;
   }
 
   /**
   * nov_cargo_acum
   * @return nov_cargo_acum
   */
   public String getNov_cargo_acum() {
      return nov_cargo_acum;
   }
 
   /**
   * nov_cargo_acum
   * @param nov_cargo_acum
   */
   public void setNov_cargo_acum( String nov_cargo_acum ) {
      this.nov_cargo_acum=nov_cargo_acum;
   }
 
   /**
   * dic_cargo_acum
   * @return dic_cargo_acum
   */
   public String getDic_cargo_acum() {
      return dic_cargo_acum;
   }
 
   /**
   * dic_cargo_acum
   * @param dic_cargo_acum
   */
   public void setDic_cargo_acum( String dic_cargo_acum ) {
      this.dic_cargo_acum=dic_cargo_acum;
   }
 
   /**
   * ene_abono_acum
   * @return ene_abono_acum
   */
   public String getEne_abono_acum() {
      return ene_abono_acum;
   }
 
   /**
   * ene_abono_acum
   * @param ene_abono_acum
   */
   public void setEne_abono_acum( String ene_abono_acum ) {
      this.ene_abono_acum=ene_abono_acum;
   }
 
   /**
   * feb_abono_acum
   * @return feb_abono_acum
   */
   public String getFeb_abono_acum() {
      return feb_abono_acum;
   }
 
   /**
   * feb_abono_acum
   * @param feb_abono_acum
   */
   public void setFeb_abono_acum( String feb_abono_acum ) {
      this.feb_abono_acum=feb_abono_acum;
   }
 
   /**
   * mar_abono_acum
   * @return mar_abono_acum
   */
   public String getMar_abono_acum() {
      return mar_abono_acum;
   }
 
   /**
   * mar_abono_acum
   * @param mar_abono_acum
   */
   public void setMar_abono_acum( String mar_abono_acum ) {
      this.mar_abono_acum=mar_abono_acum;
   }
 
   /**
   * abr_abono_acum
   * @return abr_abono_acum
   */
   public String getAbr_abono_acum() {
      return abr_abono_acum;
   }
 
   /**
   * abr_abono_acum
   * @param abr_abono_acum
   */
   public void setAbr_abono_acum( String abr_abono_acum ) {
      this.abr_abono_acum=abr_abono_acum;
   }
 
   /**
   * may_abono_acum
   * @return may_abono_acum
   */
   public String getMay_abono_acum() {
      return may_abono_acum;
   }
 
   /**
   * may_abono_acum
   * @param may_abono_acum
   */
   public void setMay_abono_acum( String may_abono_acum ) {
      this.may_abono_acum=may_abono_acum;
   }
 
   /**
   * jun_abono_acum
   * @return jun_abono_acum
   */
   public String getJun_abono_acum() {
      return jun_abono_acum;
   }
 
   /**
   * jun_abono_acum
   * @param jun_abono_acum
   */
   public void setJun_abono_acum( String jun_abono_acum ) {
      this.jun_abono_acum=jun_abono_acum;
   }
 
   /**
   * jul_abono_acum
   * @return jul_abono_acum
   */
   public String getJul_abono_acum() {
      return jul_abono_acum;
   }
 
   /**
   * jul_abono_acum
   * @param jul_abono_acum
   */
   public void setJul_abono_acum( String jul_abono_acum ) {
      this.jul_abono_acum=jul_abono_acum;
   }
 
   /**
   * ago_abono_acum
   * @return ago_abono_acum
   */
   public String getAgo_abono_acum() {
      return ago_abono_acum;
   }
 
   /**
   * ago_abono_acum
   * @param ago_abono_acum
   */
   public void setAgo_abono_acum( String ago_abono_acum ) {
      this.ago_abono_acum=ago_abono_acum;
   }
 
   /**
   * sep_abono_acum
   * @return sep_abono_acum
   */
   public String getSep_abono_acum() {
      return sep_abono_acum;
   }
 
   /**
   * sep_abono_acum
   * @param sep_abono_acum
   */
   public void setSep_abono_acum( String sep_abono_acum ) {
      this.sep_abono_acum=sep_abono_acum;
   }
 
   /**
   * oct_abono_acum
   * @return oct_abono_acum
   */
   public String getOct_abono_acum() {
      return oct_abono_acum;
   }
 
   /**
   * oct_abono_acum
   * @param oct_abono_acum
   */
   public void setOct_abono_acum( String oct_abono_acum ) {
      this.oct_abono_acum=oct_abono_acum;
   }
 
   /**
   * nov_abono_acum
   * @return nov_abono_acum
   */
   public String getNov_abono_acum() {
      return nov_abono_acum;
   }
 
   /**
   * nov_abono_acum
   * @param nov_abono_acum
   */
   public void setNov_abono_acum( String nov_abono_acum ) {
      this.nov_abono_acum=nov_abono_acum;
   }
 
   /**
   * dic_abono_acum
   * @return dic_abono_acum
   */
   public String getDic_abono_acum() {
      return dic_abono_acum;
   }
 
   /**
   * dic_abono_acum
   * @param dic_abono_acum
   */
   public void setDic_abono_acum( String dic_abono_acum ) {
      this.dic_abono_acum=dic_abono_acum;
   }
 
   /**
   * codigo_registro
   * @return codigo_registro
   */
   public String getCodigo_registro() {
      return codigo_registro;
   }
 
   /**
   * codigo_registro
   * @param codigo_registro
   */
   public void setCodigo_registro( String codigo_registro ) {
      this.codigo_registro=codigo_registro;
   }
 
   /**
   * registro
   * @return registro
   */
   public String getRegistro() {
      return registro;
   }
 
   /**
   * registro
   * @param registro
   */
   public void setRegistro( String registro ) {
      this.registro=registro;
   }
 
   /**
   * id_catalogo_cuenta
   * @return id_catalogo_cuenta
   */
   public String getId_catalogo_cuenta() {
      return id_catalogo_cuenta;
   }
 
   /**
   * id_catalogo_cuenta
   * @param id_catalogo_cuenta
   */
   public void setId_catalogo_cuenta( String id_catalogo_cuenta ) {
      this.id_catalogo_cuenta=id_catalogo_cuenta;
   }
 
   /**
   * cuenta_contable_id
   * @return cuenta_contable_id
   */
   public String getCuenta_contable_id() {
      return cuenta_contable_id;
   }
 
   /**
   * cuenta_contable_id
   * @param cuenta_contable_id
   */
   public void setCuenta_contable_id( String cuenta_contable_id ) {
      this.cuenta_contable_id=cuenta_contable_id;
   }
 
   /**
   * cuenta_mayor_id
   * @return cuenta_mayor_id
   */
   public String getCuenta_mayor_id() {
      return cuenta_mayor_id;
   }
 
   /**
   * cuenta_mayor_id
   * @param cuenta_mayor_id
   */
   public void setCuenta_mayor_id( String cuenta_mayor_id ) {
      this.cuenta_mayor_id=cuenta_mayor_id;
   }
 
   /**
   * cuenta_contable
   * @return cuenta_contable
   */
   public String getCuenta_contable() {
      return cuenta_contable;
   }
 
   /**
   * cuenta_contable
   * @param cuenta_contable
   */
   public void setCuenta_contable( String cuenta_contable ) {
      this.cuenta_contable=cuenta_contable;
   }
 
   /**
   * nivel
   * @return nivel
   */
   public String getNivel() {
      return nivel;
   }
 
   /**
   * nivel
   * @param nivel
   */
   public void setNivel( String nivel ) {
      this.nivel=nivel;
   }
 
   /**
   * descripcion
   * @return descripcion
   */
   public String getDescripcion() {
      return descripcion;
   }
 
   /**
   * descripcion
   * @param descripcion
   */
   public void setDescripcion( String descripcion ) {
      this.descripcion=descripcion;
   }
   
   
   
    public ResultSet cargaSubcuentas(Connection con, int nivRep, String niv1, String niv2, String niv3, String niv4, String niv5, String niv6, String niv7, String niv8, String niv9, String pCatCuentaId,String pUniEje, String pEntidad, String pAmbito, String pEjercicio,String pnumEmpleado) throws SQLException, Exception  {
      bcConsultaPerfiles consultaPerfiles = null;
      consultaPerfiles = new bcConsultaPerfiles();
      Statement stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      ResultSet rsQuery = null;
        String x="";
        StringBuffer SQL = new StringBuffer("");
        String condicion="";
        String  condicionUniEje="";
        String temAmbito="";
        int nipRepTem=nivRep-1;
        
        if (pEntidad.length()==1)
          temAmbito="00"+pEntidad+pAmbito;
        else  
           temAmbito="0"+pEntidad+pAmbito;
        if (pUniEje.length()==3) 
          pUniEje="0"+pUniEje;
        
        //Cambio agregado por CLHL 05/08/2009 para que para el catálogo de cuentas 2 (Ingresos x ventas - banquito)
        //Le muestre todas las unidades ejecutoras, ya que ellos entrar con la 100 pero pueden agregarle a cualquier unidad.
         consultaPerfiles.select_pers_varios_ambitos(con);  
         //Cambio agregado por CHL 17/05/2011 para que solo los empleados indicados en la tabla 
         //rf_tc_pers_modifica_polizas.variosambitos=1 puedar tanto agregar como modificar polizas con varios ambitos
        if   ((!pCatCuentaId.equals("2"))&&(!(consultaPerfiles.getNumEmpleado().contains(pnumEmpleado)))){
            condicionUniEje=" and substr(cuenta_contable,10,4) = '" + pUniEje +"'";             
            //Este cambio se agrego el 11-mar-2011 a petición de Minerva Vidales
            if ((pUniEje.equals("0130")&&temAmbito.equals("0092"))||(pUniEje.equals("0101")&&temAmbito.equals("0011"))){
               condicion= " and substr(cuenta_contable,10,8) like '" + pUniEje +"%'";
            }
            else{
               condicion= " and substr(cuenta_contable,10,8) = '" + pUniEje + temAmbito + "'";   
            }
        }   
        // StringBuffer SQL = new StringBuffer("select nivel"+nivRep+" cuenta,descrip from rtcCuentas where nivel1='"+niv1);
        if (nivRep == 1){
          x ="select substr(cuenta_contable,1,4) cuenta, descripcion from RF_TR_CUENTAS_CONTABLES r where nivel = "+ nivRep+ " and id_catalogo_cuenta = '"+pCatCuentaId+"'"+" and extract(year from fecha_vig_ini)="+pEjercicio+" order by cuenta";        
        }
        else if (nivRep == 2){
            x ="select substr(cuenta_contable,5,1) cuenta, descripcion from RF_TR_CUENTAS_CONTABLES r where substr(cuenta_contable,1,4) = '"+niv1+"' and nivel = "+ nivRep+ " and id_catalogo_cuenta = '"+pCatCuentaId+"'"+" and extract(year from fecha_vig_ini)="+pEjercicio+" order by cuenta";        
        }
        else if (nivRep == 3){
            x ="select substr(cuenta_contable,6,4) cuenta, descripcion from RF_TR_CUENTAS_CONTABLES r where substr(cuenta_contable,1,5) = '"+niv1+niv2+"' and nivel = "+ nivRep+ " and id_catalogo_cuenta = '"+pCatCuentaId+"'"+" and extract(year from fecha_vig_ini)="+pEjercicio+" order by cuenta";        
        } else if (nivRep ==4){
            x ="select substr(cuenta_contable,10,4) cuenta, descripcion from RF_TR_CUENTAS_CONTABLES r where substr(cuenta_contable,1,9) = '"+niv1+niv2+niv3+"' and nivel = "+ nivRep+ " and id_catalogo_cuenta = '"+pCatCuentaId+"'"+ condicionUniEje+" and extract(year from fecha_vig_ini)="+pEjercicio+" order by cuenta";                
        } else if (nivRep ==5){
            x ="select substr(cuenta_contable,14,4) cuenta, descripcion from RF_TR_CUENTAS_CONTABLES r where substr(cuenta_contable,1,13) = '"+niv1+niv2+niv3+niv4+"' and nivel = "+ nivRep+ " and id_catalogo_cuenta = '"+pCatCuentaId+"'"+condicion+" and extract(year from fecha_vig_ini)="+pEjercicio+" order by cuenta";                
        } else if (nivRep ==6){
            x ="select substr(cuenta_contable,18,4) cuenta, descripcion from RF_TR_CUENTAS_CONTABLES r where substr(cuenta_contable,1,17) = '"+niv1+niv2+niv3+niv4+niv5+"' and nivel = "+ nivRep+ " and id_catalogo_cuenta = '"+pCatCuentaId+"'"+condicion+" and extract(year from fecha_vig_ini)="+pEjercicio+" order by cuenta";                
        } else if (nivRep ==7){
            x ="select substr(cuenta_contable,22,4) cuenta, descripcion from RF_TR_CUENTAS_CONTABLES r where substr(cuenta_contable,1,21) = '"+niv1+niv2+niv3+niv4+niv5+niv6+"' and nivel = "+ nivRep+ " and id_catalogo_cuenta = '"+pCatCuentaId+"'"+condicion+" and extract(year from fecha_vig_ini)="+pEjercicio+" order by cuenta";                    
        } else if (nivRep ==8){
            x ="select substr(cuenta_contable,26,4) cuenta, descripcion from RF_TR_CUENTAS_CONTABLES r where substr(cuenta_contable,1,25) = '"+niv1+niv2+niv3+niv4+niv5+niv6+niv7+"' and nivel = "+ nivRep+ " and id_catalogo_cuenta = '"+pCatCuentaId+"'"+condicion+" and extract(year from fecha_vig_ini)="+pEjercicio+" order by cuenta";                        
        } else if (nivRep ==9){
            x ="select substr(cuenta_contable,30,4) cuenta, descripcion from RF_TR_CUENTAS_CONTABLES r where substr(cuenta_contable,1,29) = '"+niv1+niv2+niv3+niv4+niv5+niv6+niv7+niv8+"' and nivel = "+ nivRep+ " and id_catalogo_cuenta = '"+pCatCuentaId+"'"+condicion+" and extract(year from fecha_vig_ini)="+pEjercicio+" order by cuenta";                        
        }        
        SQL.append(x);
        // System.out.println("va "+SQL.toString());
        rsQuery = stQuery.executeQuery(SQL.toString());
        return rsQuery;
    }
    
    public ResultSet cargaSubcuentas(Connection con, String pCatCuentaId,String pUniEje, String pEntidad, 
                      String pAmbito, String pEjercicio,String pnumEmpleado, int nivRep, String ... ctas) throws SQLException, Exception  {
    
      Statement stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      ResultSet rsQuery = null;
        String x="";
        StringBuffer SQL = new StringBuffer("");
        String condicion="";
        String  condicionUniEje="";
        String temAmbito="";
        
        if (pEntidad.length()==1)
          temAmbito="00"+pEntidad+pAmbito;
        else  
           temAmbito="0"+pEntidad+pAmbito;
        if (pUniEje.length()==3) 
          pUniEje="0"+pUniEje;
        
        //Cambio agregado por CLHL 05/08/2009 para que para el catálogo de cuentas 2 (Ingresos x ventas - banquito)
        //Le muestre todas las unidades ejecutoras, ya que ellos entrar con la 100 pero pueden agregarle a cualquier unidad.
        if(!pUniEje.equals("0100")) {
          if   ((!pCatCuentaId.equals("2"))&&(!(pnumEmpleado.equals("39547")||pnumEmpleado.equals("16107")||pnumEmpleado.equals("46168")||pnumEmpleado.equals("78258")||pnumEmpleado.equals("46657")||pnumEmpleado.equals("21068")||pnumEmpleado.equals("3509")))){
            if(nivRep == 3)  
              condicion=" and substr(cuenta_contable,10,4) = '" + pUniEje +"'"; 
            if(nivRep >= 4)
              condicion= " and substr(cuenta_contable,10,8) = '" + pUniEje + temAmbito + "'"; 
          }   
        }
        // StringBuffer SQL = new StringBuffer("select nivel"+nivRep+" cuenta,descrip from rtcCuentas where nivel1='"+niv1);
        if (nivRep == 1){
          x ="select F_EXTRAER_NIVEL(cuenta_contable,"+nivRep+","+pEjercicio+","+pCatCuentaId+",0) cuenta, descripcion from RF_TR_CUENTAS_CONTABLES r where nivel = "+ nivRep+ " and id_catalogo_cuenta = '"+pCatCuentaId+"'"+" and extract(year from fecha_vig_ini)="+pEjercicio+" order by cuenta";        
        }
        else {
            x ="select F_EXTRAER_NIVEL(cuenta_contable,"+nivRep+","+pEjercicio+","+pCatCuentaId+",0) cuenta, descripcion from RF_TR_CUENTAS_CONTABLES r where F_EXTRAER_NIVEL(cuenta_contable,"+(nivRep-1)+","+pEjercicio+","+pCatCuentaId+",1) = '"+concatenarCuenta(nivRep,ctas)+"' and nivel = "+ nivRep+ " and id_catalogo_cuenta = '"+pCatCuentaId+"'"+ condicion + " and extract(year from fecha_vig_ini)="+pEjercicio+" order by cuenta";        
        }
        SQL.append(x);
        //System.out.println("va "+SQL.toString());
        rsQuery = stQuery.executeQuery(SQL.toString());
        return rsQuery;
    }
    
    public String concatenarCuenta(int nivel, String ... cuentas) {
      StringBuffer regresa = new StringBuffer();
      for (int i = 0; i < nivel && i < cuentas.length; i++)  {
        regresa.append(cuentas[i]);
      }
      return regresa.toString();
    }
    
    public String selectCuentaContableId(String pCuenta, String pCatCuentaId, String ejercicio, String nivel)throws SQLException, Exception{
      SentenciasCRS sentencia = null;
      String regresa = null;
      try  {
        sentencia = new SentenciasCRS();
        sentencia.addParam("cuenta",pCuenta);
        sentencia.addParam("idCatalogoCuenta",pCatCuentaId);
        sentencia.addParam("ejercicio",ejercicio);
        sentencia.addParam("nivel",nivel);
        sentencia.registrosMap(DaoFactory.CONEXION_CONTABILIDAD,"clasificadorCuenta.select.obtenerCuentaContable");
        if(sentencia.next())
          regresa = sentencia.getString("cuenta_contable_id");
      } catch (Exception ex)  {
        ex.printStackTrace();
      } finally  {
        sentencia.liberaParametros();
        sentencia = null;
      }
      return regresa;
    }

    public String selectCuentaContableId(Connection con, String pCuenta, String pCatCuentaId, String ejercicio)throws SQLException, Exception{
       Statement stQuery=null;
       ResultSet rsQuery= null;
       String SidPoliza = "";
        String SQL=null;
       try{
         SQL = "select cuenta_contable_id from rf_tr_cuentas_contables where cuenta_contable = '"+pCuenta+"'"+ " and id_catalogo_cuenta = '"+pCatCuentaId+"' and extract(year from fecha_vig_ini) = " + ejercicio;     
         stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
         rsQuery = stQuery.executeQuery(SQL);
         //  secuencia para poliza_id
         while (rsQuery.next()){
            SidPoliza = rsQuery.getString("cuenta_contable_id");
         } //del while
       } //Fin try
       catch(Exception e){
         System.out.println("Ocurrio un error al accesar al metodo selectCuentaContableId "+e.getMessage()+" "+SQL);
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
       return SidPoliza;
    } //fin Select 
   
 
   /**
   * Metodo que lee la informacion de rf_tr_cuentas_contables
   * Fecha de creacion:
   * Autor:
   * Fecha de modificacion:
   * Modificado por:
   */
   public void select_rf_tr_cuentas_contables(Connection con, String pCuentaConId)throws SQLException, Exception{
      Statement stQuery=null;
      ResultSet rsQuery=null;
      try{
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
       StringBuffer SQL=new StringBuffer("SELECT a.fecha_vig_ini,a.fecha_vig_fin,a.ene_cargo,a.feb_cargo,a.mar_cargo,a.abr_cargo,a.may_cargo,a.jun_cargo,a.jul_cargo,a.ago_cargo,a.sep_cargo,a.oct_cargo,a.nov_cargo,a.dic_cargo,a.ene_abono,a.feb_abono,a.mar_abono,a.abr_abono,a.may_abono,a.jun_abono,a.jul_abono,a.ago_abono,a.sep_abono,a.oct_abono,a.nov_abono,a.dic_abono,a.ene_cargo_acum,a.feb_cargo_acum,a.mar_cargo_acum,a.abr_cargo_acum,a.may_cargo_acum,a.jun_cargo_acum,a.jul_cargo_acum,a.ago_cargo_acum,a.sep_cargo_acum,a.oct_cargo_acum,a.nov_cargo_acum,a.dic_cargo_acum,a.ene_abono_acum,a.feb_abono_acum,a.mar_abono_acum,a.abr_abono_acum,a.may_abono_acum,a.jun_abono_acum,a.jul_abono_acum,a.ago_abono_acum,a.sep_abono_acum,a.oct_abono_acum,a.nov_abono_acum,a.dic_abono_acum,a.codigo_registro,a.registro,a.id_catalogo_cuenta,a.cuenta_contable_id,a.cuenta_mayor_id,a.cuenta_contable,a.nivel,a.descripcion"); 
       SQL.append(" FROM rf_tr_cuentas_contables a ");
       SQL.append(" WHERE a.cuenta_contable_id=").append(pCuentaConId).append(" ");
       // System.out.println(SQL.toString());
       rsQuery=stQuery.executeQuery(SQL.toString()); 
       while (rsQuery.next()){
          fecha_vig_ini=(rsQuery.getString("fecha_vig_ini")==null) ? "" : rsQuery.getString("fecha_vig_ini");
          fecha_vig_fin=(rsQuery.getString("fecha_vig_fin")==null) ? "" : rsQuery.getString("fecha_vig_fin");
          ene_cargo=(rsQuery.getString("ene_cargo")==null) ? "" : rsQuery.getString("ene_cargo");
          feb_cargo=(rsQuery.getString("feb_cargo")==null) ? "" : rsQuery.getString("feb_cargo");
          mar_cargo=(rsQuery.getString("mar_cargo")==null) ? "" : rsQuery.getString("mar_cargo");
          abr_cargo=(rsQuery.getString("abr_cargo")==null) ? "" : rsQuery.getString("abr_cargo");
          may_cargo=(rsQuery.getString("may_cargo")==null) ? "" : rsQuery.getString("may_cargo");
          jun_cargo=(rsQuery.getString("jun_cargo")==null) ? "" : rsQuery.getString("jun_cargo");
          jul_cargo=(rsQuery.getString("jul_cargo")==null) ? "" : rsQuery.getString("jul_cargo");
          ago_cargo=(rsQuery.getString("ago_cargo")==null) ? "" : rsQuery.getString("ago_cargo");
          sep_cargo=(rsQuery.getString("sep_cargo")==null) ? "" : rsQuery.getString("sep_cargo");
          oct_cargo=(rsQuery.getString("oct_cargo")==null) ? "" : rsQuery.getString("oct_cargo");
          nov_cargo=(rsQuery.getString("nov_cargo")==null) ? "" : rsQuery.getString("nov_cargo");
          dic_cargo=(rsQuery.getString("dic_cargo")==null) ? "" : rsQuery.getString("dic_cargo");
          ene_abono=(rsQuery.getString("ene_abono")==null) ? "" : rsQuery.getString("ene_abono");
          feb_abono=(rsQuery.getString("feb_abono")==null) ? "" : rsQuery.getString("feb_abono");
          mar_abono=(rsQuery.getString("mar_abono")==null) ? "" : rsQuery.getString("mar_abono");
          abr_abono=(rsQuery.getString("abr_abono")==null) ? "" : rsQuery.getString("abr_abono");
          may_abono=(rsQuery.getString("may_abono")==null) ? "" : rsQuery.getString("may_abono");
          jun_abono=(rsQuery.getString("jun_abono")==null) ? "" : rsQuery.getString("jun_abono");
          jul_abono=(rsQuery.getString("jul_abono")==null) ? "" : rsQuery.getString("jul_abono");
          ago_abono=(rsQuery.getString("ago_abono")==null) ? "" : rsQuery.getString("ago_abono");
          sep_abono=(rsQuery.getString("sep_abono")==null) ? "" : rsQuery.getString("sep_abono");
          oct_abono=(rsQuery.getString("oct_abono")==null) ? "" : rsQuery.getString("oct_abono");
          nov_abono=(rsQuery.getString("nov_abono")==null) ? "" : rsQuery.getString("nov_abono");
          dic_abono=(rsQuery.getString("dic_abono")==null) ? "" : rsQuery.getString("dic_abono");
          ene_cargo_acum=(rsQuery.getString("ene_cargo_acum")==null) ? "" : rsQuery.getString("ene_cargo_acum");
          feb_cargo_acum=(rsQuery.getString("feb_cargo_acum")==null) ? "" : rsQuery.getString("feb_cargo_acum");
          mar_cargo_acum=(rsQuery.getString("mar_cargo_acum")==null) ? "" : rsQuery.getString("mar_cargo_acum");
          abr_cargo_acum=(rsQuery.getString("abr_cargo_acum")==null) ? "" : rsQuery.getString("abr_cargo_acum");
          may_cargo_acum=(rsQuery.getString("may_cargo_acum")==null) ? "" : rsQuery.getString("may_cargo_acum");
          jun_cargo_acum=(rsQuery.getString("jun_cargo_acum")==null) ? "" : rsQuery.getString("jun_cargo_acum");
          jul_cargo_acum=(rsQuery.getString("jul_cargo_acum")==null) ? "" : rsQuery.getString("jul_cargo_acum");
          ago_cargo_acum=(rsQuery.getString("ago_cargo_acum")==null) ? "" : rsQuery.getString("ago_cargo_acum");
          sep_cargo_acum=(rsQuery.getString("sep_cargo_acum")==null) ? "" : rsQuery.getString("sep_cargo_acum");
          oct_cargo_acum=(rsQuery.getString("oct_cargo_acum")==null) ? "" : rsQuery.getString("oct_cargo_acum");
          nov_cargo_acum=(rsQuery.getString("nov_cargo_acum")==null) ? "" : rsQuery.getString("nov_cargo_acum");
          dic_cargo_acum=(rsQuery.getString("dic_cargo_acum")==null) ? "" : rsQuery.getString("dic_cargo_acum");
          ene_abono_acum=(rsQuery.getString("ene_abono_acum")==null) ? "" : rsQuery.getString("ene_abono_acum");
          feb_abono_acum=(rsQuery.getString("feb_abono_acum")==null) ? "" : rsQuery.getString("feb_abono_acum");
          mar_abono_acum=(rsQuery.getString("mar_abono_acum")==null) ? "" : rsQuery.getString("mar_abono_acum");
          abr_abono_acum=(rsQuery.getString("abr_abono_acum")==null) ? "" : rsQuery.getString("abr_abono_acum");
          may_abono_acum=(rsQuery.getString("may_abono_acum")==null) ? "" : rsQuery.getString("may_abono_acum");
          jun_abono_acum=(rsQuery.getString("jun_abono_acum")==null) ? "" : rsQuery.getString("jun_abono_acum");
          jul_abono_acum=(rsQuery.getString("jul_abono_acum")==null) ? "" : rsQuery.getString("jul_abono_acum");
          ago_abono_acum=(rsQuery.getString("ago_abono_acum")==null) ? "" : rsQuery.getString("ago_abono_acum");
          sep_abono_acum=(rsQuery.getString("sep_abono_acum")==null) ? "" : rsQuery.getString("sep_abono_acum");
          oct_abono_acum=(rsQuery.getString("oct_abono_acum")==null) ? "" : rsQuery.getString("oct_abono_acum");
          nov_abono_acum=(rsQuery.getString("nov_abono_acum")==null) ? "" : rsQuery.getString("nov_abono_acum");
          dic_abono_acum=(rsQuery.getString("dic_abono_acum")==null) ? "" : rsQuery.getString("dic_abono_acum");
          codigo_registro=(rsQuery.getString("codigo_registro")==null) ? "" : rsQuery.getString("codigo_registro");
          registro=(rsQuery.getString("registro")==null) ? "" : rsQuery.getString("registro");
          id_catalogo_cuenta=(rsQuery.getString("id_catalogo_cuenta")==null) ? "" : rsQuery.getString("id_catalogo_cuenta");
          cuenta_contable_id=(rsQuery.getString("cuenta_contable_id")==null) ? "" : rsQuery.getString("cuenta_contable_id");
          cuenta_mayor_id=(rsQuery.getString("cuenta_mayor_id")==null) ? "" : rsQuery.getString("cuenta_mayor_id");
          cuenta_contable=(rsQuery.getString("cuenta_contable")==null) ? "" : rsQuery.getString("cuenta_contable");
          nivel=(rsQuery.getString("nivel")==null) ? "" : rsQuery.getString("nivel");
          descripcion=(rsQuery.getString("descripcion")==null) ? "" : rsQuery.getString("descripcion");
       } // Fin while
     } //Fin try
     catch(Exception e){
       System.out.println("Ocurrio un error al accesar al metodo select_rf_tr_cuentas_contables "+e.getMessage());
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
   } //Fin metodo select_rf_tr_cuentas_contables
 
 
    /**
    * Metodo que lee la informacion de rf_tr_cuentas_contables para verificar si existe un cliente
    * Fecha de creacion:
    * Autor:
    * Fecha de modificacion:
    * Modificado por:
    */
    public String select_rf_tr_cuentas_contables_existe(Connection con, String pCuentaCon, String pFecha)throws SQLException, Exception{
       Statement stQuery=null;
       ResultSet rsQuery=null;
       String resul="";
       StringBuffer SQL=new StringBuffer("");
       try{
        stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        SQL.append("SELECT a.fecha_vig_ini,a.fecha_vig_fin,a.codigo_registro,a.registro,a.id_catalogo_cuenta,a.cuenta_contable_id,a.cuenta_mayor_id,a.cuenta_contable,a.nivel,a.descripcion"); 
        SQL.append(" FROM rf_tr_cuentas_contables a ");
        SQL.append(" WHERE a.cuenta_contable='").append(pCuentaCon).append("' and ").append(" a.id_catalogo_cuenta=1 and ");
        //SQL.append("to_date('").append(pFecha).append("','dd/mm/yyyy') between to_date(to_char(a.fecha_vig_ini,'dd/mm/yyyy'),'dd/mm/yyyy') and  to_date(to_char(a.fecha_vig_fin,'dd/mm/yyyy'),'dd/mm/yyyy') ");
        //Solo tomar el ejercicio de la fecha inicio
        SQL.append("  EXTRACT (YEAR FROM to_date('").append(pFecha).append("','dd/mm/yyyy')) = EXTRACT (YEAR FROM a.fecha_vig_ini) ");
        rsQuery=stQuery.executeQuery(SQL.toString());  
        while (rsQuery.next()){
           fecha_vig_ini=(rsQuery.getString("fecha_vig_ini")==null) ? "" : rsQuery.getString("fecha_vig_ini");
           fecha_vig_fin=(rsQuery.getString("fecha_vig_fin")==null) ? "" : rsQuery.getString("fecha_vig_fin");
           codigo_registro=(rsQuery.getString("codigo_registro")==null) ? "" : rsQuery.getString("codigo_registro");
           registro=(rsQuery.getString("registro")==null) ? "" : rsQuery.getString("registro");
           id_catalogo_cuenta=(rsQuery.getString("id_catalogo_cuenta")==null) ? "" : rsQuery.getString("id_catalogo_cuenta");
           cuenta_contable_id=(rsQuery.getString("cuenta_contable_id")==null) ? "" : rsQuery.getString("cuenta_contable_id");
           cuenta_mayor_id=(rsQuery.getString("cuenta_mayor_id")==null) ? "" : rsQuery.getString("cuenta_mayor_id");
           cuenta_contable=(rsQuery.getString("cuenta_contable")==null) ? "" : rsQuery.getString("cuenta_contable");
           nivel=(rsQuery.getString("nivel")==null) ? "" : rsQuery.getString("nivel");
           descripcion=(rsQuery.getString("descripcion")==null) ? "" : rsQuery.getString("descripcion");
           resul=pCuentaCon;
        } // Fin while
      } //Fin try
      catch(Exception e){        
        System.out.println("Ocurrio un error al accesar al metodo select_rf_tr_cuentas_contables_existe "+e.getMessage());
        System.out.println(SQL.toString());
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
      return resul;
    } //Fin metodo select_rf_tr_cuentas_contables_existe
 
 
     /**
     * Metodo que lee la informacion de rf_tr_cuentas_contables para obtener la siguiente clave del promotor 
     * Fecha de creacion:
     * Autor:
     * Fecha de modificacion:
     * Modificado por:
     */
     public String select_rf_tr_cuentas_contables_promotor(Connection con, String pCuentaCon, String pAno)throws SQLException, Exception{
        Statement stQuery=null;
        ResultSet rsQuery=null;
        String resul="";
        try{
         stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
         StringBuffer SQL=new StringBuffer("select lpad(to_number(max(substr(r.cuenta_contable,26,4)))+1,4,'0') clave "); 
         SQL.append("  from rf_tr_cuentas_contables r ");
         SQL.append(" where r.cuenta_contable like '").append(pCuentaCon).append("%' and ").append(" r.id_catalogo_cuenta=1 and ");
         SQL.append(" to_date(to_char(r.fecha_vig_ini,'yyyy'),'yyyy')=to_date('").append(pAno).append("','yyyy')");
         // System.out.println(SQL.toString());
         rsQuery=stQuery.executeQuery(SQL.toString());
         while (rsQuery.next()){
            resul=(rsQuery.getString("clave")==null) ? "" : rsQuery.getString("clave");
         } // Fin while
       } //Fin try
       catch(Exception e){
         System.out.println("Ocurrio un error al accesar al metodo select_rf_tr_cuentas_contables_promotor "+e.getMessage());
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
       return resul;
     } //Fin metodo select_rf_tr_cuentas_contables_promotor
 
   /**
   * Metodo que inserta la informacion de rf_tr_cuentas_contables
   * Fecha de creacion:
   * Autor:
   * Fecha de modificacion:
   * Modificado por:
   */
   public void insert_rf_tr_cuentas_contables(Connection con)throws SQLException, Exception{
      Statement stQuery=null;
      try{
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
       StringBuffer SQL=new StringBuffer("INSERT INTO rf_tr_cuentas_contables( fecha_vig_ini,fecha_vig_fin,ene_cargo,feb_cargo,mar_cargo,abr_cargo,may_cargo,jun_cargo,jul_cargo,ago_cargo,sep_cargo,oct_cargo,nov_cargo,dic_cargo,ene_abono,feb_abono,mar_abono,abr_abono,may_abono,jun_abono,jul_abono,ago_abono,sep_abono,oct_abono,nov_abono,dic_abono,ene_cargo_acum,feb_cargo_acum,mar_cargo_acum,abr_cargo_acum,may_cargo_acum,jun_cargo_acum,jul_cargo_acum,ago_cargo_acum,sep_cargo_acum,oct_cargo_acum,nov_cargo_acum,dic_cargo_acum,ene_abono_acum,feb_abono_acum,mar_abono_acum,abr_abono_acum,may_abono_acum,jun_abono_acum,jul_abono_acum,ago_abono_acum,sep_abono_acum,oct_abono_acum,nov_abono_acum,dic_abono_acum,codigo_registro,registro,id_catalogo_cuenta,cuenta_contable_id,cuenta_mayor_id,cuenta_contable,nivel,descripcion) "); 
       SQL.append("VALUES(");
       SQL.append("to_date('").append(fecha_vig_ini).append("','dd/mm/yyyy'),");
       SQL.append("to_date('").append(fecha_vig_fin).append("','dd/mm/yyyy'),");
       SQL.append(ene_cargo).append(",");
       SQL.append(feb_cargo).append(",");
       SQL.append(mar_cargo).append(",");
       SQL.append(abr_cargo).append(",");
       SQL.append(may_cargo).append(",");
       SQL.append(jun_cargo).append(",");
       SQL.append(jul_cargo).append(",");
       SQL.append(ago_cargo).append(",");
       SQL.append(sep_cargo).append(",");
       SQL.append(oct_cargo).append(",");
       SQL.append(nov_cargo).append(",");
       SQL.append(dic_cargo).append(",");
       SQL.append(ene_abono).append(",");
       SQL.append(feb_abono).append(",");
       SQL.append(mar_abono).append(",");
       SQL.append(abr_abono).append(",");
       SQL.append(may_abono).append(",");
       SQL.append(jun_abono).append(",");
       SQL.append(jul_abono).append(",");
       SQL.append(ago_abono).append(",");
       SQL.append(sep_abono).append(",");
       SQL.append(oct_abono).append(",");
       SQL.append(nov_abono).append(",");
       SQL.append(dic_abono).append(",");
       SQL.append(ene_cargo_acum).append(",");
       SQL.append(feb_cargo_acum).append(",");
       SQL.append(mar_cargo_acum).append(",");
       SQL.append(abr_cargo_acum).append(",");
       SQL.append(may_cargo_acum).append(",");
       SQL.append(jun_cargo_acum).append(",");
       SQL.append(jul_cargo_acum).append(",");
       SQL.append(ago_cargo_acum).append(",");
       SQL.append(sep_cargo_acum).append(",");
       SQL.append(oct_cargo_acum).append(",");
       SQL.append(nov_cargo_acum).append(",");
       SQL.append(dic_cargo_acum).append(",");
       SQL.append(ene_abono_acum).append(",");
       SQL.append(feb_abono_acum).append(",");
       SQL.append(mar_abono_acum).append(",");
       SQL.append(abr_abono_acum).append(",");
       SQL.append(may_abono_acum).append(",");
       SQL.append(jun_abono_acum).append(",");
       SQL.append(jul_abono_acum).append(",");
       SQL.append(ago_abono_acum).append(",");
       SQL.append(sep_abono_acum).append(",");
       SQL.append(oct_abono_acum).append(",");
       SQL.append(nov_abono_acum).append(",");
       SQL.append(dic_abono_acum).append(",");
       SQL.append(codigo_registro).append(",");
       SQL.append("'").append(registro).append("',");
       SQL.append(id_catalogo_cuenta).append(",");
       SQL.append(cuenta_contable_id).append(",");
       SQL.append(cuenta_mayor_id).append(",");
       SQL.append("'").append(cuenta_contable).append("',");
       SQL.append(nivel).append(",");
       SQL.append("'").append(descripcion).append("')");
       // System.out.println(SQL.toString());
       int rs=-1;
       rs=stQuery.executeUpdate(SQL.toString());
     } //Fin try
     catch(Exception e){
       System.out.println("Ocurrio un error al accesar al metodo insert_rf_tr_cuentas_contables "+e.getMessage());
       throw e;
     } //Fin catch
     finally{
       if (stQuery!=null){
         stQuery.close();
         stQuery=null;
       }
     } //Fin finally
   } //Fin metodo insert_rf_tr_cuentas_contables
   
    /**
    * Metodo que inserta la informacion de rf_tr_cuentas_contables
    * Fecha de creacion:
    * Autor:
    * Fecha de modificacion:
    * Modificado por:
    */
    public void insert_rf_tr_cuentas_contables_general(Connection con)throws SQLException, Exception{
       Statement stQuery=null;
       try{
        stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        StringBuffer SQL=new StringBuffer("INSERT INTO rf_tr_cuentas_contables( fecha_vig_ini,fecha_vig_fin,ene_cargo,feb_cargo,mar_cargo,abr_cargo,may_cargo,jun_cargo,jul_cargo,ago_cargo,sep_cargo,oct_cargo,nov_cargo,dic_cargo,ene_abono,feb_abono,mar_abono,abr_abono,may_abono,jun_abono,jul_abono,ago_abono,sep_abono,oct_abono,nov_abono,dic_abono,ene_cargo_acum,feb_cargo_acum,mar_cargo_acum,abr_cargo_acum,may_cargo_acum,jun_cargo_acum,jul_cargo_acum,ago_cargo_acum,sep_cargo_acum,oct_cargo_acum,nov_cargo_acum,dic_cargo_acum,ene_abono_acum,feb_abono_acum,mar_abono_acum,abr_abono_acum,may_abono_acum,jun_abono_acum,jul_abono_acum,ago_abono_acum,sep_abono_acum,oct_abono_acum,nov_abono_acum,dic_abono_acum,codigo_registro,registro,id_catalogo_cuenta,cuenta_contable_id,cuenta_mayor_id,cuenta_contable,nivel,descripcion) "); 
        SQL.append("VALUES(");
        SQL.append("to_date('").append(fecha_vig_ini).append("','dd/mm/yyyy'),");
        SQL.append("to_date('").append(fecha_vig_fin).append("','dd/mm/yyyy'),");
        SQL.append(0).append(",");
        SQL.append(0).append(",");
        SQL.append(0).append(",");
        SQL.append(0).append(",");
        SQL.append(0).append(",");
        SQL.append(0).append(",");
        SQL.append(0).append(",");
        SQL.append(0).append(",");
        SQL.append(0).append(",");
        SQL.append(0).append(",");
        SQL.append(0).append(",");
        SQL.append(0).append(",");
        SQL.append(0).append(",");
        SQL.append(0).append(",");
        SQL.append(0).append(",");
        SQL.append(0).append(",");
        SQL.append(0).append(",");
        SQL.append(0).append(",");
        SQL.append(0).append(",");
        SQL.append(0).append(",");
        SQL.append(0).append(",");
        SQL.append(0).append(",");
        SQL.append(0).append(",");
        SQL.append(0).append(",");
        SQL.append(0).append(",");
        SQL.append(0).append(",");
        SQL.append(0).append(",");
        SQL.append(0).append(",");
        SQL.append(0).append(",");
        SQL.append(0).append(",");
        SQL.append(0).append(",");
        SQL.append(0).append(",");
        SQL.append(0).append(",");
        SQL.append(0).append(",");
        SQL.append(0).append(",");
        SQL.append(0).append(",");
        SQL.append(0).append(",");
        SQL.append(0).append(",");
        SQL.append(0).append(",");
        SQL.append(0).append(",");
        SQL.append(0).append(",");
        SQL.append(0).append(",");
        SQL.append(0).append(",");
        SQL.append(0).append(",");
        SQL.append(0).append(",");
        SQL.append(0).append(",");
        SQL.append(0).append(",");
        SQL.append(0).append(",");
        SQL.append(codigo_registro).append(","); //Numero de empleado que registro 
         SQL.append("to_date('").append(registro).append("','dd/mm/yyyy'),"); //Fecha de registro
        SQL.append(id_catalogo_cuenta).append(","); 
        SQL.append("null").append(",");//Lo inserta  un triger con un MAX
        SQL.append(cuenta_mayor_id).append(",");
        SQL.append("'").append(cuenta_contable).append("',");
        SQL.append(nivel).append(","); 
        SQL.append("'").append(descripcion).append("')");
       //System.out.println(SQL.toString());
        int rs=-1;
        rs=stQuery.executeUpdate(SQL.toString());
      } //Fin try
      catch(Exception e){
        System.out.println("Ocurrio un error al accesar al metodo insert_rf_tr_cuentas_contables_general "+e.getMessage());
        throw e;
      } //Fin catch
      finally{
        if (stQuery!=null){
          stQuery.close();
          stQuery=null;
        }
      } //Fin finally
    } //Fin metodo insert_rf_tr_cuentas_contables_cliente   
    
    /**
    * Metodo que inserta la informacion de rf_tr_cuentas_contables (para poder insertar caracteres especiales)
    * Fecha de creacion: 18/02/2011
    * Autor: Elida Pozos Vazquez
    */
    public void insert_cuentas_contables2(Connection con)throws SQLException, Exception{
        PreparedStatement ps=null;    
        try{
            StringBuffer SQL=new StringBuffer("INSERT INTO rf_tr_cuentas_contables(fecha_vig_ini,fecha_vig_fin,codigo_registro,registro,id_catalogo_cuenta,cuenta_mayor_id,cuenta_contable,nivel,descripcion) "); 
            SQL.append("VALUES(to_date(?,'dd/mm/yyyy'),to_date(?,'dd/mm/yyyy'),?,to_date(?,'dd/mm/yyyy'),?,?,?,?,?)");
            ps=con.prepareStatement(SQL.toString());
            ps.setString(1,fecha_vig_ini);
            ps.setString(2,fecha_vig_fin);
            ps.setInt(3,Integer.parseInt(codigo_registro));//Numero de empleado que registro 
            ps.setString(4,registro);//Fecha de registro
            ps.setInt(5,Integer.parseInt(id_catalogo_cuenta));
            ps.setInt(6,Integer.parseInt(cuenta_mayor_id));
            ps.setString(7,cuenta_contable);
            ps.setInt(8,Integer.parseInt(nivel));
            ps.setString(9,descripcion);
            ps.executeUpdate();
        } //Fin try
        catch(Exception e){
            System.out.println("Ocurrio un error al accesar al metodo insert_cuentas_contables2 "+e.getMessage());
            throw e;
        } //Fin catch
        finally{   
            if (ps != null) {
                ps.close();
            }
            ps = null;
        } //Fin finally
    } //Fin metodo insert_rf_tr_cuentas_contables_cliente   

     /**
     * Metodo que inserta la informacion de rf_tr_cuentas_contables
     * Fecha de creacion:
     * Autor:
     * Fecha de modificacion:
     * Modificado por:
     */
     public void insert_rf_tr_cuentas_contables_cliente(Connection con, String nivel)throws SQLException, Exception{
        Statement stQuery=null;
        try{
         stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
         StringBuffer SQL=new StringBuffer("INSERT INTO rf_tr_cuentas_contables( fecha_vig_ini,fecha_vig_fin,ene_cargo,feb_cargo,mar_cargo,abr_cargo,may_cargo,jun_cargo,jul_cargo,ago_cargo,sep_cargo,oct_cargo,nov_cargo,dic_cargo,ene_abono,feb_abono,mar_abono,abr_abono,may_abono,jun_abono,jul_abono,ago_abono,sep_abono,oct_abono,nov_abono,dic_abono,ene_cargo_acum,feb_cargo_acum,mar_cargo_acum,abr_cargo_acum,may_cargo_acum,jun_cargo_acum,jul_cargo_acum,ago_cargo_acum,sep_cargo_acum,oct_cargo_acum,nov_cargo_acum,dic_cargo_acum,ene_abono_acum,feb_abono_acum,mar_abono_acum,abr_abono_acum,may_abono_acum,jun_abono_acum,jul_abono_acum,ago_abono_acum,sep_abono_acum,oct_abono_acum,nov_abono_acum,dic_abono_acum,codigo_registro,registro,id_catalogo_cuenta,cuenta_contable_id,cuenta_mayor_id,cuenta_contable,nivel,descripcion) "); 
         SQL.append("VALUES(");
         SQL.append("to_date('").append(fecha_vig_ini).append("','dd/mm/yyyy'),");
         SQL.append("to_date('").append(fecha_vig_fin).append("','dd/mm/yyyy'),");
         SQL.append(0).append(",");
         SQL.append(0).append(",");
         SQL.append(0).append(",");
         SQL.append(0).append(",");
         SQL.append(0).append(",");
         SQL.append(0).append(",");
         SQL.append(0).append(",");
         SQL.append(0).append(",");
         SQL.append(0).append(",");
         SQL.append(0).append(",");
         SQL.append(0).append(",");
         SQL.append(0).append(",");
         SQL.append(0).append(",");
         SQL.append(0).append(",");
         SQL.append(0).append(",");
         SQL.append(0).append(",");
         SQL.append(0).append(",");
         SQL.append(0).append(",");
         SQL.append(0).append(",");
         SQL.append(0).append(",");
         SQL.append(0).append(",");
         SQL.append(0).append(",");
         SQL.append(0).append(",");
         SQL.append(0).append(",");
         SQL.append(0).append(",");
         SQL.append(0).append(",");
         SQL.append(0).append(",");
         SQL.append(0).append(",");
         SQL.append(0).append(",");
         SQL.append(0).append(",");
         SQL.append(0).append(",");
         SQL.append(0).append(",");
         SQL.append(0).append(",");
         SQL.append(0).append(",");
         SQL.append(0).append(",");
         SQL.append(0).append(",");
         SQL.append(0).append(",");
         SQL.append(0).append(",");
         SQL.append(0).append(",");
         SQL.append(0).append(",");
         SQL.append(0).append(",");
         SQL.append(0).append(",");
         SQL.append(0).append(",");
         SQL.append(0).append(",");
         SQL.append(0).append(",");
         SQL.append(0).append(",");
         SQL.append(0).append(",");
         SQL.append(0).append(",");
         SQL.append(999999).append(","); //Numero de empleado que registro 
         SQL.append("sysdate,"); //Fecha de registro
         SQL.append(1).append(","); //Catalogo de cuenta 1 Institucional
         SQL.append("null").append(",");//Lo inserta  un triger con un MAX
         SQL.append(cuenta_mayor_id).append(",");
         SQL.append("'").append(cuenta_contable).append("',");
         SQL.append(nivel).append(","); // Nivel 7 para el cleinte 8 Proveedor 07032012
         SQL.append("'").append(descripcion).append("')");
         // System.out.println(SQL.toString());
         int rs=-1;
         rs=stQuery.executeUpdate(SQL.toString());
       } //Fin try
       catch(Exception e){
         System.out.println("Ocurrio un error al accesar al metodo insert_rf_tr_cuentas_contables_cliente "+e.getMessage());
         throw e;
       } //Fin catch
       finally{
         if (stQuery!=null){
           stQuery.close();
           stQuery=null;
         }
       } //Fin finally
     } //Fin metodo insert_rf_tr_cuentas_contables_cliente   
 
   /**
   * Metodo que modifica la informacion de rf_tr_cuentas_contables
   * Fecha de creacion:
   * Autor:
   * Fecha de modificacion:
   * Modificado por:
   */
   public void update_rf_tr_cuentas_contables(Connection con, String pCuentaConId, String pCatCuenta, String pMes, String pOperacionConId)throws SQLException, Exception{
      Statement stQuery=null;
      try{
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
       StringBuffer SQL=new StringBuffer("UPDATE rf_tr_cuentas_contables");
       if (pMes.equals("01"))
         if (pOperacionConId=="0")
           SQL.append(" SET ene_cargo=").append("ene_cargo+").append(ene_cargo);
         else  
           SQL.append(" SET ene_abono=").append("ene_abono+").append(ene_abono);

       if (pMes.equals("02"))
         if (pOperacionConId=="0")
           SQL.append(" SET feb_cargo=").append("feb_cargo+").append(feb_cargo);
         else  
           SQL.append(" SET fec_abono=").append("feb_abono+").append(feb_abono);
           
         if (pMes.equals("03"))
           if (pOperacionConId=="0")
             SQL.append(" SET mar_cargo=").append("mar_cargo+").append(mar_cargo);
           else  
             SQL.append(" SET mar_abono=").append("mar_abono+").append(mar_abono);

         if (pMes.equals("04"))
           if (pOperacionConId=="0")
             SQL.append(" SET abr_cargo=").append("abr_cargo+").append(abr_cargo);
           else  
             SQL.append(" SET abr_abono=").append("abr_abono+").append(abr_abono);
             
         if (pMes.equals("05"))
           if (pOperacionConId=="0")
             SQL.append(" SET may_cargo=").append("may_cargo+").append(may_cargo);
           else  
             SQL.append(" SET may_abono=").append("may_abono+").append(may_abono);
             
         if (pMes.equals("06"))
           if (pOperacionConId=="0")
             SQL.append(" SET jun_cargo=").append("jun_cargo+").append(jun_cargo);
           else  
             SQL.append(" SET jun_abono=").append("jun_abono+").append(jun_abono);
             
         if (pMes.equals("07"))
           if (pOperacionConId=="0")
             SQL.append(" SET jul_cargo=").append("jul_cargo+").append(jul_cargo);
           else  
             SQL.append(" SET jul_abono=").append("jul_abono+").append(jul_abono);
             
         if (pMes.equals("08"))
           if (pOperacionConId=="0")
             SQL.append(" SET ago_cargo=").append("ago_cargo+").append(ago_cargo);
           else  
             SQL.append(" SET ago_abono=").append("ago_abono+").append(ago_abono);

         if (pMes.equals("09"))
           if (pOperacionConId=="0")
             SQL.append(" SET sep_cargo=").append("sep_cargo+").append(sep_cargo);
           else  
             SQL.append(" SET sep_abono=").append("sep_abono+").append(sep_abono);
             
         if (pMes.equals("10"))
           if (pOperacionConId=="0")
             SQL.append(" SET oct_cargo=").append("oct_cargo+").append(oct_cargo);
           else  
             SQL.append(" SET oct_abono=").append("oct_abono+").append(oct_abono);
             
         if (pMes.equals("11"))
           if (pOperacionConId=="0")
             SQL.append(" SET nov_cargo=").append("nov_cargo+").append(nov_cargo);
           else  
             SQL.append(" SET nov_abono=").append("nov_abono+").append(nov_abono);
             
         if (pMes.equals("12"))
           if (pOperacionConId=="0")
             SQL.append(" SET dic_cargo=").append("dic_cargo+").append(dic_cargo);
           else  
             SQL.append(" SET dic_abono=").append("dic_abono+").append(dic_abono);
             
       SQL.append(" WHERE cuenta_contable_id=").append(pCuentaConId).append(" and id_catalogo_cuenta = ").append(pCatCuenta);
      //  System.out.println(SQL.toString());
       int rs=-1;
       rs=stQuery.executeUpdate(SQL.toString());
     } //Fin try
     catch(Exception e){
       System.out.println("Ocurrio un error al accesar al metodo update_rf_tr_cuentas_contables "+e.getMessage());
       throw e;
     } //Fin catch
     finally{
       if (stQuery!=null){
         stQuery.close();
         stQuery=null;
       }
     } //Fin finally
   } //Fin metodo update_rf_tr_cuentas_contables
 
   /**
   * Metodo que borra la informacion de rf_tr_cuentas_contables
   * Fecha de creacion: 
   * Autor:
   * Fecha de modificacion: 16/Diciembre/2010
   * Modificado por: Elida Pozos Vazquez
   * Descripcion: se cambio en el where a.llave por cuenta_contable_id
   */
   public void delete_rf_tr_cuentas_contables(Connection con, String clave)throws SQLException, Exception{
      Statement stQuery=null;
      try{
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
       StringBuffer SQL=new StringBuffer("DELETE FROM rf_tr_cuentas_contables a ");
      // SQL.append("WHERE a.LLAVE='").append(clave).append("'");
       SQL.append("WHERE cuenta_contable_id=").append(clave).append(" ");
     //   System.out.println(SQL.toString());
       int rs=-1;
       rs=stQuery.executeUpdate(SQL.toString());
     } //Fin try
     catch(Exception e){
       System.out.println("Ocurrio un error al accesar al metodo bcCuentaContable.delete_rf_tr_cuentas_contables() "+e.getMessage());
       throw e;
     } //Fin catch
     finally{
       if (stQuery!=null){
         stQuery.close();
         stQuery=null;
       }
     } //Fin finally
   } //Fin metodo delete_rf_tr_cuentas_contables

 /**
 * Metodo que modifica la informacion de rf_tr_cuentas_contables
 * Fecha de creacion:
 * Autor: Claudia Luz Hernandez
 * Fecha de modificacion:
 * Modificado por:
 */
 public void update_rf_tr_cuentas_contables_acumulados(Connection con, String pUnidad_ejecutora,String pAmbito,String pEntidad,String pEjecicio, String pPrograma, String pCatCuenta, String pMes)throws SQLException, Exception{
    Statement stQuery=null;    
    StringBuffer SQL=new StringBuffer("");
    try{      
     stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
     SQL.append("UPDATE rf_tr_cuentas_contables t");
     if (pMes.equals("01")) {     
       SQL.append(" SET t.ene_cargo_acum=");
       SQL.append("(CASE (select c.naturaleza from  rf_tc_clasificador_cuentas  c where c.cuenta_mayor_id=t.cuenta_mayor_id) WHEN 'D' THEN ");
       SQL.append("(t.ene_cargo_ini - t.ene_abono_ini + t.ene_cargo) else (t.ene_cargo) end),");
       SQL.append("t.ene_abono_acum=");    
       SQL.append("(CASE (select c.naturaleza from  rf_tc_clasificador_cuentas  c where c.cuenta_mayor_id=t.cuenta_mayor_id) WHEN 'A' THEN ");
       SQL.append("(t.ene_abono_ini - t.ene_cargo_ini + t.ene_abono) else (t.ene_abono) end),t.ene_cargo_eli=t.ene_cargo,t.ene_abono_eli=t.ene_abono  "); 
     }
     if (pMes.equals("02"))
         SQL.append(" SET t.feb_cargo_acum=t.feb_cargo+t.ene_cargo_acum, t.feb_abono_acum=t.feb_abono+t.ene_abono_acum,t.feb_cargo_eli=t.feb_cargo, t.feb_abono_eli=t.feb_abono ");         
     
     if (pMes.equals("03"))
         SQL.append(" SET t.mar_cargo_acum=t.mar_cargo+t.feb_cargo_acum, t.mar_abono_acum=t.mar_abono+t.feb_abono_acum,t.mar_cargo_eli=t.mar_cargo, t.mar_abono_eli=t.mar_abono  ");         
     
     if (pMes.equals("04"))      
         SQL.append(" SET t.abr_cargo_acum=t.abr_cargo+t.mar_cargo_acum, t.abr_abono_acum=t.abr_abono+t.mar_abono_acum,t.abr_cargo_eli=t.abr_cargo, t.abr_abono_eli=t.abr_abono ");         
     
     if (pMes.equals("05"))
         SQL.append(" SET t.may_cargo_acum=t.may_cargo+t.abr_cargo_acum, t.may_abono_acum=t.may_abono+t.abr_abono_acum,t.may_cargo_eli=t.may_cargo, t.may_abono_eli=t.may_abono ");         
     
     if (pMes.equals("06"))
         SQL.append(" SET t.jun_cargo_acum=t.jun_cargo+t.may_cargo_acum, t.jun_abono_acum=t.jun_abono+t.may_abono_acum,t.jun_cargo_eli=t.jun_cargo, t.jun_abono_eli=t.jun_abono  ");         
     
     if (pMes.equals("07"))
         SQL.append(" SET t.jul_cargo_acum=t.jul_cargo+t.jun_cargo_acum, t.jul_abono_acum=t.jul_abono+t.jun_abono_acum,t.jul_cargo_eli=t.jul_cargo, t.jul_abono_eli=t.jul_abono ");         
     
     if (pMes.equals("08"))
         SQL.append(" SET t.ago_cargo_acum=t.ago_cargo+t.jul_cargo_acum, t.ago_abono_acum=t.ago_abono+t.jul_abono_acum,t.ago_cargo_eli=t.ago_cargo, t.ago_abono_eli=t.ago_abono ");         
     
     if (pMes.equals("09"))
        SQL.append(" SET t.sep_cargo_acum=t.sep_cargo+t.ago_cargo_acum, t.sep_abono_acum=t.sep_abono+t.ago_abono_acum,t.sep_cargo_eli=t.sep_cargo, t.sep_abono_eli=t.sep_abono  ");         
        
     if (pMes.equals("10"))
         SQL.append(" SET t.oct_cargo_acum=t.oct_cargo+t.sep_cargo_acum, t.oct_abono_acum=t.oct_abono+t.sep_abono_acum,t.oct_cargo_eli=t.oct_cargo, t.oct_abono_eli=t.oct_abono ");                    
         
     if (pMes.equals("11"))
        SQL.append(" SET t.nov_cargo_acum=t.nov_cargo+t.oct_cargo_acum, t.nov_abono_acum=t.nov_abono+t.oct_abono_acum,t.nov_cargo_eli=t.nov_cargo, t.nov_abono_eli=t.nov_abono  ");         

     if (pMes.equals("12"))
         SQL.append(" SET t.dic_cargo_acum=t.dic_cargo+t.nov_cargo_acum, t.dic_abono_acum=t.dic_abono+t.nov_abono_acum,t.dic_cargo_eli=t.dic_cargo, t.dic_abono_eli=t.dic_abono ");         
     
     SQL.append(" WHERE  to_number(substr(t.cuenta_contable,10,4))=").append(pUnidad_ejecutora).append(" and to_number(substr(t.cuenta_contable,14,3))=").append(pEntidad);
     SQL.append(" and to_number(substr(t.cuenta_contable,17,1))=").append(pAmbito).append(" and to_number(substr(t.cuenta_contable,6,4))='").append(pPrograma).append("' and id_catalogo_cuenta = ").append(pCatCuenta);
     SQL.append(" and extract(year from t.fecha_vig_ini) = ").append(pEjecicio);
         
     
     //System.out.println("Query update_rf_tr_cuentas_contables_acumulados: "+SQL.toString());
     int rs=-1;
     rs=stQuery.executeUpdate(SQL.toString());
   } //Fin try
   catch(Exception e){
     System.out.println("Query update_rf_tr_cuentas_contables_acumulados: "+SQL.toString());
     System.out.println("Ocurrio un error al accesar al metodo update_rf_tr_cuentas_contables_acumulados "+e.getMessage());
     throw e;
   } //Fin catch
   finally{
     if (stQuery!=null){
       stQuery.close();
       stQuery=null;
     }
   } //Fin finally
 } //Fin metodo update_rf_tr_cuentas_contables_acumulados



  /**
  * Metodo que modifica la informacion de rf_tr_cuentas_contables
  * Fecha de creacion:
  * Autor: Claudia Luz Hernandez
  * Fecha de modificacion:
  * Modificado por:
  */
  public void update_rf_tr_cuentas_contables_acumulados_todos(Connection con,String pPrograma,String pEjecicio, String pCatCuenta, String pMes)throws SQLException, Exception{
     Statement stQuery=null;    
     StringBuffer SQL=new StringBuffer("");
     try{      
      stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      SQL.append("UPDATE rf_tr_cuentas_contables t");
      if (pMes.equals("01")) {     
        SQL.append(" SET t.ene_cargo_acum=");
        SQL.append("(CASE (select c.naturaleza from  rf_tc_clasificador_cuentas  c where c.cuenta_mayor_id=t.cuenta_mayor_id) WHEN 'D' THEN ");
        SQL.append("(t.ene_cargo_ini - t.ene_abono_ini + t.ene_cargo) else (t.ene_cargo) end),");
        SQL.append("t.ene_abono_acum=");    
        SQL.append("(CASE (select c.naturaleza from  rf_tc_clasificador_cuentas  c where c.cuenta_mayor_id=t.cuenta_mayor_id) WHEN 'A' THEN ");
        SQL.append("(t.ene_abono_ini - t.ene_cargo_ini + t.ene_abono) else (t.ene_abono) end),t.ene_cargo_eli=t.ene_cargo,t.ene_abono_eli=t.ene_abono  "); 
      }
      if (pMes.equals("02"))
          SQL.append(" SET t.feb_cargo_acum=t.feb_cargo+t.ene_cargo_acum, t.feb_abono_acum=t.feb_abono+t.ene_abono_acum,t.feb_cargo_eli=t.feb_cargo, t.feb_abono_eli=t.feb_abono ");         
      
      if (pMes.equals("03"))
          SQL.append(" SET t.mar_cargo_acum=t.mar_cargo+t.feb_cargo_acum, t.mar_abono_acum=t.mar_abono+t.feb_abono_acum,t.mar_cargo_eli=t.mar_cargo, t.mar_abono_eli=t.mar_abono  ");         
      
      if (pMes.equals("04"))      
          SQL.append(" SET t.abr_cargo_acum=t.abr_cargo+t.mar_cargo_acum, t.abr_abono_acum=t.abr_abono+t.mar_abono_acum,t.abr_cargo_eli=t.abr_cargo, t.abr_abono_eli=t.abr_abono ");         
      
      if (pMes.equals("05"))
          SQL.append(" SET t.may_cargo_acum=t.may_cargo+t.abr_cargo_acum, t.may_abono_acum=t.may_abono+t.abr_abono_acum,t.may_cargo_eli=t.may_cargo, t.may_abono_eli=t.may_abono ");         
      
      if (pMes.equals("06"))
          SQL.append(" SET t.jun_cargo_acum=t.jun_cargo+t.may_cargo_acum, t.jun_abono_acum=t.jun_abono+t.may_abono_acum,t.jun_cargo_eli=t.jun_cargo, t.jun_abono_eli=t.jun_abono  ");         
      
      if (pMes.equals("07"))
          SQL.append(" SET t.jul_cargo_acum=t.jul_cargo+t.jun_cargo_acum, t.jul_abono_acum=t.jul_abono+t.jun_abono_acum,t.jul_cargo_eli=t.jul_cargo, t.jul_abono_eli=t.jul_abono ");         
      
      if (pMes.equals("08"))
          SQL.append(" SET t.ago_cargo_acum=t.ago_cargo+t.jul_cargo_acum, t.ago_abono_acum=t.ago_abono+t.jul_abono_acum,t.ago_cargo_eli=t.ago_cargo, t.ago_abono_eli=t.ago_abono ");         
      
      if (pMes.equals("09"))
         SQL.append(" SET t.sep_cargo_acum=t.sep_cargo+t.ago_cargo_acum, t.sep_abono_acum=t.sep_abono+t.ago_abono_acum,t.sep_cargo_eli=t.sep_cargo, t.sep_abono_eli=t.sep_abono  ");         
         
      if (pMes.equals("10"))
          SQL.append(" SET t.oct_cargo_acum=t.oct_cargo+t.sep_cargo_acum, t.oct_abono_acum=t.oct_abono+t.sep_abono_acum,t.oct_cargo_eli=t.oct_cargo, t.oct_abono_eli=t.oct_abono ");                    
          
      if (pMes.equals("11"))
         SQL.append(" SET t.nov_cargo_acum=t.nov_cargo+t.oct_cargo_acum, t.nov_abono_acum=t.nov_abono+t.oct_abono_acum,t.nov_cargo_eli=t.nov_cargo, t.nov_abono_eli=t.nov_abono  ");         

      if (pMes.equals("12"))
          SQL.append(" SET t.dic_cargo_acum=t.dic_cargo+t.nov_cargo_acum, t.dic_abono_acum=t.dic_abono+t.nov_abono_acum,t.dic_cargo_eli=t.dic_cargo, t.dic_abono_eli=t.dic_abono ");         
      
      SQL.append(" WHERE id_catalogo_cuenta = ").append(pCatCuenta);
      SQL.append(" and extract(year from t.fecha_vig_ini) = ").append(pEjecicio);
      if (!pPrograma.equals("0")){
          SQL.append(" and substr(t.cuenta_contable,6,4)='").append(pPrograma).append("'");  
      }
      //System.out.println("Query update_rf_tr_cuentas_contables_acumulados_todos: "+SQL.toString());
      int rs=-1;
      rs=stQuery.executeUpdate(SQL.toString());
    } //Fin try
    catch(Exception e){
      System.out.println("Query update_rf_tr_cuentas_contables_acumulados_todos: "+SQL.toString());
      System.out.println("Ocurrio un error al accesar al metodo update_rf_tr_cuentas_contables_acumulados_todos "+e.getMessage());
      throw e;
    } //Fin catch
    finally{
      if (stQuery!=null){
        stQuery.close();
        stQuery=null;
      }
    } //Fin finally
  } //Fin metodo update_rf_tr_cuentas_contables_acumulados_todos




  /**
  * Metodo que modifica la informacion de rf_tr_cuentas_contables de los campos acumulados eliminacion
  * Fecha de creacion:26/01/2011
  * Autor: Claudia Luz Hernandez
  * Fecha de modificacion:
  * Modificado por:
  */
  public void update_rf_tr_cuentas_contables_acumulados_eli(Connection con, String pEjecicio, String pCatCuenta, String pMes)throws SQLException, Exception{
     Statement stQuery=null;
      StringBuffer SQL=new StringBuffer("");
     try{
      stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      SQL.append("UPDATE rf_tr_cuentas_contables t");
      if (pMes.equals("01")) {     
        SQL.append(" SET t.ene_cargo_acum_eli=");
        SQL.append("(CASE (select c.naturaleza from  rf_tc_clasificador_cuentas  c where c.cuenta_mayor_id=t.cuenta_mayor_id) WHEN 'D' THEN ");
        SQL.append("(t.ene_cargo_ini_eli - t.ene_abono_ini_eli + t.ene_cargo_eli) else (t.ene_cargo_eli) end),");
        SQL.append("t.ene_abono_acum_eli=");    
        SQL.append("(CASE (select c.naturaleza from  rf_tc_clasificador_cuentas  c where c.cuenta_mayor_id=t.cuenta_mayor_id) WHEN 'A' THEN ");
        SQL.append("(t.ene_abono_ini_eli - t.ene_cargo_ini_eli + t.ene_abono_eli) else (t.ene_abono_eli) end)  "); 
      }
      if (pMes.equals("02"))
          SQL.append(" SET t.feb_cargo_acum_eli=t.feb_cargo_eli+t.ene_cargo_acum_eli, t.feb_abono_acum_eli=t.feb_abono_eli+t.ene_abono_acum_eli ");         
      
      if (pMes.equals("03"))
          SQL.append(" SET t.mar_cargo_acum_eli=t.mar_cargo_eli+t.feb_cargo_acum_eli, t.mar_abono_acum_eli=t.mar_abono_eli+t.feb_abono_acum_eli ");         
      
      if (pMes.equals("04"))      
          SQL.append(" SET t.abr_cargo_acum_eli=t.abr_cargo_eli+t.mar_cargo_acum_eli, t.abr_abono_acum_eli=t.abr_abono_eli+t.mar_abono_acum_eli ");         
      
      if (pMes.equals("05"))
          SQL.append(" SET t.may_cargo_acum_eli=t.may_cargo_eli+t.abr_cargo_acum_eli, t.may_abono_acum_eli=t.may_abono_eli+t.abr_abono_acum_eli ");         
      
      if (pMes.equals("06"))
          SQL.append(" SET t.jun_cargo_acum_eli=t.jun_cargo_eli+t.may_cargo_acum_eli, t.jun_abono_acum_eli=t.jun_abono_eli+t.may_abono_acum_eli ");         
      
      if (pMes.equals("07"))
          SQL.append(" SET t.jul_cargo_acum_eli=t.jul_cargo_eli+t.jun_cargo_acum_eli, t.jul_abono_acum_eli=t.jul_abono_eli+t.jun_abono_acum_eli ");         
      
      if (pMes.equals("08"))
          SQL.append(" SET t.ago_cargo_acum_eli=t.ago_cargo_eli+t.jul_cargo_acum_eli, t.ago_abono_acum_eli=t.ago_abono_eli+t.jul_abono_acum_eli ");         
      
      if (pMes.equals("09"))
         SQL.append(" SET t.sep_cargo_acum_eli=t.sep_cargo_eli+t.ago_cargo_acum_eli, t.sep_abono_acum_eli=t.sep_abono_eli+t.ago_abono_acum_eli  ");         
         
      if (pMes.equals("10"))
          SQL.append(" SET t.oct_cargo_acum_eli=t.oct_cargo_eli+t.sep_cargo_acum_eli, t.oct_abono_acum_eli=t.oct_abono_eli+t.sep_abono_acum_eli ");                    
          
      if (pMes.equals("11"))
         SQL.append(" SET t.nov_cargo_acum_eli=t.nov_cargo_eli+t.oct_cargo_acum_eli, t.nov_abono_acum_eli=t.nov_abono_eli+t.oct_abono_acum_eli  ");         

      if (pMes.equals("12"))          
          SQL.append(" SET t.dic_cargo_acum_eli=t.dic_cargo_eli+t.nov_cargo_acum_eli, t.dic_abono_acum_eli=t.dic_abono_eli+t.nov_abono_acum_eli,t.dic_cargo_pub=t.dic_cargo,t.dic_abono_pub=t.dic_abono,t.dic_cargo_eli_pub=t.dic_cargo_eli,t.dic_abono_eli_pub=t.dic_abono_eli ");         
      
      SQL.append(" WHERE  id_catalogo_cuenta = ").append(pCatCuenta);
      SQL.append(" and extract(year from t.fecha_vig_ini) = ").append(pEjecicio);
      
      int rs=-1;
      rs=stQuery.executeUpdate(SQL.toString());
    } //Fin try
    catch(Exception e){
      System.out.println("Query update_rf_tr_cuentas_contables_acumulados_eli:"+SQL.toString());
      System.out.println("Ocurrio un error al accesar al metodo update_rf_tr_cuentas_contables_acumulados_eli "+e.getMessage());
      throw e;
    } //Fin catch
    finally{
      if (stQuery!=null){
        stQuery.close();
        stQuery=null;
      }
    } //Fin finally
  } //Fin metodo update_rf_tr_cuentas_contables_acumulados
   /**
   * Metodo que modifica la informacion de rf_tr_cuentas_contables de los campos de cuenta pública
   * Fecha de creacion:05/03/2013
   * Autor: Claudia Luz Hernandez
   */
   public void update_rf_tr_cuentas_contables_limpia_traspaso(Connection con, String pEjecicio, String pCatCuenta)throws SQLException, Exception{
      Statement stQuery=null;
      StringBuffer SQL=new StringBuffer("");
      try{
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
       SQL.append(" UPDATE rf_tr_cuentas_contables t");
       SQL.append(" SET t.dic_cargo_pub=0.0,t.dic_abono_pub=0.0,t.dic_cargo_eli_pub=0.0,t.dic_abono_eli_pub=0.0 ");               
       SQL.append(" WHERE  id_catalogo_cuenta = ").append(pCatCuenta);
       SQL.append(" and extract(year from t.fecha_vig_ini) = ").append(pEjecicio);       
       int rs=-1;
       rs=stQuery.executeUpdate(SQL.toString());
     } //Fin try
     catch(Exception e){
       System.out.println("Query update_rf_tr_cuentas_contables_limpia_traspaso:"+SQL.toString());
       System.out.println("Ocurrio un error al accesar al metodo update_rf_tr_cuentas_contables_limpia_traspaso "+e.getMessage());
       throw e;
     } //Fin catch
     finally{
       if (stQuery!=null){
         stQuery.close();
         stQuery=null;
       }
     } //Fin finally
   } //Fin metodo update_rf_tr_cuentas_contables_limpia_traspaso
  /**
  * Metodo que lee la informacion de select_saldos_acum para verificar si ya se acumularon los saldos de a los ultimos niveles
  * Fecha de creacion:
  * Autor: Claudia Luz Hernandez
  * Fecha de modificacion:
  * Modificado por:
  */
  public void select_saldos_acum(Connection con, String pEjecicio, String pPrograma, String pCatCuenta, String pMes)throws SQLException, Exception{
    Statement stQuery=null;
    ResultSet rsQuery=null;    
    StringBuffer SQL=new StringBuffer("select t.* ");     
    try{
     stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      SQL.append("  from rf_tr_cuentas_contables t ");
      if (pMes.equals("01")){      
          SQL.append("where (t.ene_cargo_acum<>(CASE (select c.naturaleza from  rf_tc_clasificador_cuentas  c where c.cuenta_mayor_id=t.cuenta_mayor_id) WHEN 'D' THEN ");
          SQL.append("      (t.ene_cargo_ini - t.ene_abono_ini + t.ene_cargo) else (t.ene_cargo) end) ");
          SQL.append(       "or t.ene_abono_acum<>(CASE (select c.naturaleza from  rf_tc_clasificador_cuentas  c where c.cuenta_mayor_id=t.cuenta_mayor_id) WHEN 'A' THEN ");
          SQL.append("      (t.ene_abono_ini - t.ene_cargo_ini + t.ene_abono) else (t.ene_abono) end) )");
      }
      if (pMes.equals("02"))
          SQL.append(" where (t.feb_cargo_acum<>(t.feb_cargo+t.ene_cargo_acum) or  t.feb_abono_acum<>(t.feb_abono+t.ene_abono_acum)) ");         
      
      if (pMes.equals("03"))
          SQL.append("where (t.mar_cargo_acum<>(t.mar_cargo+t.feb_cargo_acum) or t.mar_abono_acum<>(t.mar_abono+t.feb_abono_acum)) ");         
      
      if (pMes.equals("04"))      
          SQL.append("where (t.abr_cargo_acum<>(t.abr_cargo+t.mar_cargo_acum) or t.abr_abono_acum<>(t.abr_abono+t.mar_abono_acum))");         
      
      if (pMes.equals("05"))
          SQL.append("where (t.may_cargo_acum<>(t.may_cargo+t.abr_cargo_acum) or t.may_abono_acum<>(t.may_abono+t.abr_abono_acum))");         
      
      if (pMes.equals("06"))
          SQL.append("where (t.jun_cargo_acum<>(t.jun_cargo+t.may_cargo_acum) or t.jun_abono_acum<>(t.jun_abono+t.may_abono_acum)) ");         
      
      if (pMes.equals("07"))
          SQL.append("where (t.jul_cargo_acum<>(t.jul_cargo+t.jun_cargo_acum) or t.jul_abono_acum<>(t.jul_abono+t.jun_abono_acum)) ");         
      
      if (pMes.equals("08"))
          SQL.append("where (t.ago_cargo_acum<>(t.ago_cargo+t.jul_cargo_acum) or  t.ago_abono_acum<>(t.ago_abono+t.jul_abono_acum))");         
      
      if (pMes.equals("09"))
         SQL.append("where (t.sep_cargo_acum<>(t.sep_cargo+t.ago_cargo_acum) or t.sep_abono_acum<>(t.sep_abono+t.ago_abono_acum))");         
         
      if (pMes.equals("10"))
          SQL.append("where (t.oct_cargo_acum<>(t.oct_cargo+t.sep_cargo_acum) or t.oct_abono_acum<>(t.oct_abono+t.sep_abono_acum))");                    
          
      if (pMes.equals("11"))
         SQL.append("where (t.nov_cargo_acum<>(t.nov_cargo+t.oct_cargo_acum) or t.nov_abono_acum<>(t.nov_abono+t.oct_abono_acum))");         

      if (pMes.equals("12"))
          SQL.append(" where ( t.dic_cargo_acum<>(t.dic_cargo+t.nov_cargo_acum) or t.dic_abono_acum<>(t.dic_abono+t.nov_abono_acum))");         
      
      SQL.append(" and to_number(substr(t.cuenta_contable,6,4))='").append(pPrograma).append("' and id_catalogo_cuenta = ").append(pCatCuenta);
      SQL.append(" and extract(year from t.fecha_vig_ini) = ").append(pEjecicio);
      
     System.out.println(SQL.toString());
     rsQuery=stQuery.executeQuery(SQL.toString());
     if (rsQuery.next()){
        cuenta_contable_id=(rsQuery.getString("cuenta_contable_id")==null) ? "" : rsQuery.getString("cuenta_contable_id");
     } // Fin if
     else{
        cuenta_contable_id=""; 
     }
    } //Fin try
    catch(Exception e){
     System.out.println(SQL.toString());
     System.out.println("Ocurrio un error al accesar al metodo select_saldos_acum "+e.getMessage());
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
  } //Fin metodo select_saldos_acum

  
  
   /** 
   * Metodo que muestra datos de RF_TC_CLASIFICADOR_CUENTAS 
   * Fecha de creacion: 09/12/2010
   * Autor: Elida Pozos Vazquez
   */ 
   public ResultSet selectCargaCuentas(Connection con,int nivRep,String cveCta,String pCatCuentaId,String pUniEje, String pEntidad, String pAmbito, String pEjercicio,String lsLon) throws SQLException, Exception  {
     Statement stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
     ResultSet rsQuery = null;
       String x="";
       StringBuffer SQL = new StringBuffer("");
       StringBuffer SQLA =new StringBuffer("");
       String condicion="";
       String  condicionUniEje="";
       String temAmbito="";
       String tempo="";
       if (pEntidad.length()==1)
         temAmbito="00"+pEntidad+pAmbito;
       else  
          temAmbito="0"+pEntidad+pAmbito;
       if (pUniEje.length()==3) 
         pUniEje="0"+pUniEje;
       
       //Cambio agregado por CLHL 05/08/2009 para que para el catálogo de cuentas 2 (Ingresos x ventas - banquito)
       //Le muestre todas las unidades ejecutoras, ya que ellos entrar con la 100 pero pueden agregarle a cualquier unidad.
       if   (pCatCuentaId.equals("2") || pCatCuentaId.equals("3")){
           condicionUniEje=" and substr(cuenta_contable,10,4) = '" + pUniEje +"'";             
           condicion= " and substr(cuenta_contable,10,8) = '" + pUniEje + temAmbito + "'"; 
       }
         
      int y=cveCta.length();
      for(int i=0; i<(Integer.parseInt(lsLon)-y); i++){
          tempo="0"+tempo;
      }
      //muestra los datos del registro seleccionado
       x ="select descripcion,TO_CHAR(fecha_vig_ini,'dd/mm/yyyy') as fecha1,TO_CHAR(fecha_vig_fin,'dd/mm/yyyy') as fecha2,cuenta_contable_id,cuenta_mayor_id,TO_CHAR(registro,'dd/mm/yyyy') as fechaReg from RF_TR_CUENTAS_CONTABLES r where substr(cuenta_contable,1,"+y+") = '"+cveCta+"' ";
       if(!tempo.equals("")){
            x=x+" AND SUBSTR(CUENTA_CONTABLE,"+(y+1)+")='"+tempo+"' ";
       }
       x=x+"and nivel = "+ (nivRep-1)+ " and id_catalogo_cuenta = '"+pCatCuentaId+"'"+condicion+" and extract(year from fecha_vig_ini)="+pEjercicio+" ";
       SQL.append(x);
       rsQuery = stQuery.executeQuery(SQL.toString());
    //   System.out.println("SQL.toString() "+SQL.toString());
       rsQuery.last();
       int j=rsQuery.getRow();
       if(j==0 && nivRep-1==1){//PARA CUANDO NO ESTA DADA DE ALTA LA CUENTA DE MAYOR SE OBTIENEN LOS DATOS DEL CLASIFICADOR DE CUENTAS
            rsQuery=null;
            SQLA=null;
            SQLA=new StringBuffer("SELECT A.descripcion,A.cuenta_mayor_id,TO_CHAR(A.fecha_vig_ini,'dd/mm/yyyy') as fecha1,TO_CHAR(A.fecha_vig_fin,'dd/mm/yyyy') as fecha2, '' as cuenta_contable_id,TO_CHAR(A.fecha_registro,'dd/mm/yyyy') as fechaReg  FROM RF_TC_CLASIFICADOR_CUENTAS A where cuenta_mayor="+cveCta);
       //     System.out.println("SQLA "+SQLA.toString());
            rsQuery = stQuery.executeQuery(SQLA.toString());
       }
       return rsQuery;
   }      

 
      /**
      * Metodo que modifica la informacion de rf_tr_cuentas_contables
      * Fecha de creacion: 15/Diciembre/2010
      * Autor:Elida Pozos Vazquez
      */
      public void update_cuentas_contables(Connection con, String pCuentaConId, String pCatCuenta)throws SQLException, Exception{
         Statement stQuery=null;
         try{
          stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
          StringBuffer SQL=new StringBuffer("UPDATE rf_tr_cuentas_contables");
          SQL.append(" set descripcion='"+descripcion+"', ");
          SQL.append("fecha_vig_ini=to_date('"+fecha_vig_ini+"','dd/mm/yyyy'), ");
          SQL.append("fecha_vig_fin=to_date('"+fecha_vig_fin+"','dd/mm/yyyy'), ");
          SQL.append("registro=to_date('"+fecha_vig_ini+"','dd/mm/yyyy'), ");  
          SQL.append("codigo_registro= "+codigo_registro+" ");  
          SQL.append(" WHERE cuenta_contable_id=").append(pCuentaConId).append(" and id_catalogo_cuenta = ").append(pCatCuenta);
         //  System.out.println(SQL.toString());
          int rs=-1;
          rs=stQuery.executeUpdate(SQL.toString());
        } //Fin try
        catch(Exception e){
          System.out.println("Ocurrio un error al accesar al metodo bcCuentaContable.update_rf_tr_cuentas_contables() "+e.getMessage());
          throw e;
        } //Fin catch
        finally{
          if (stQuery!=null){
            stQuery.close();
            stQuery=null;
          }
        } //Fin finally
      } //Fin metodo update_rf_tr_cuentas_contables 
  

       /**
       * Metodo que selecciona de rf_tr_cuentas_contables con una cuenta contable específica
       * Fecha de creacion: 20/Enero/2011
       * Autor:Elida Pozos Vazquez
       */
       public String selectCuentaContable(Connection con, String pCuentas, String pCatCuentaId, String ejercicio)throws SQLException, Exception{
          Statement stQuery=null;
          ResultSet rsQuery= null;
          String SQL=null;
          String resultado="";
          try{
            SQL = "SELECT descripcion FROM rf_tr_cuentas_contables WHERE cuenta_contable in('"+pCuentas+"')"+ " and id_catalogo_cuenta = '"+pCatCuentaId+"' and SUBSTR(TO_CHAR(fecha_vig_ini,'dd/mm/yyyy'),7,10) = " + ejercicio;     
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsQuery = stQuery.executeQuery(SQL);
            rsQuery.beforeFirst();
            while (rsQuery.next()){
                resultado="1";                    
            } //del while
          } //Fin try
          catch(Exception e){
            System.out.println("Ocurrio un error al accesar al metodo selectCuentaContable "+e.getMessage()+" "+SQL);
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
          return resultado;
       } //fin metodo


        /**
        * Metodo que selecciona de rf_tr_cuentas_contables con una cuenta contable específica
        * Fecha de creacion: 3/Marzo/2011
        * Autor:Elida Pozos Vazquez
        */
        public String selectCuentaContables(Connection con, String pCuentas, String pCatCuentaId, String ejercicio)throws SQLException, Exception{
           Statement stQuery=null;
           ResultSet rsQuery= null;
           StringBuffer SQL=null;
           String resultado="";
           int conta=0;
           int x=pCuentas.length();
           try{
             SQL = new StringBuffer("SELECT descripcion FROM rf_tr_cuentas_contables WHERE ");
             SQL.append("id_catalogo_cuenta = '"+pCatCuentaId+"' and SUBSTR(TO_CHAR(fecha_vig_ini,'dd/mm/yyyy'),7,10) = '" + ejercicio+"' AND " );     
             SQL.append("substr(cuenta_contable,1,"+x+") ='"+pCuentas+"' " );       
          //   System.out.println("SQL "+SQL);
             stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
             rsQuery = stQuery.executeQuery(SQL.toString());
             rsQuery.beforeFirst();
             while (rsQuery.next()){
                 conta=conta+1;                    
             } //del while
             resultado=String.valueOf(conta);
           } //Fin try
           catch(Exception e){
                
             System.out.println("Ocurrio un error al accesar al metodo selectCuentaContables "+e.getMessage()+" "+SQL);
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
           return resultado;
        } //fin metodo
        

       public String selectCuentaContableNoExisten(Connection con, String pCuentas, String pCatCuentaId, String ejercicio)throws SQLException, Exception{
          Statement stQuery=null;
          ResultSet rsQuery= null;
          String SQL=null;
          String[] lsCuentas=pCuentas.split(","); 
          String lsCuentasTem="";
          String resultado="";
          boolean bandera=false;
          try{
            SQL = "select cuenta_contable from rf_tr_cuentas_contables where cuenta_contable in("+pCuentas+")"+ " and id_catalogo_cuenta = '"+pCatCuentaId+"' and extract(year from fecha_vig_ini) = " + ejercicio;     
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsQuery = stQuery.executeQuery(SQL);
            for (int x=0;x<lsCuentas.length;x++){
              bandera=false;
              rsQuery.beforeFirst();
              while (rsQuery.next()){
                 if (lsCuentas[x].equals("'"+rsQuery.getString("cuenta_contable")+"'")){
                    bandera=true;
                    break;
                  }
               }
               if (bandera==false)
                 resultado=resultado+lsCuentas[x]+", ";
            } //del while
          } //Fin try
          catch(Exception e){
            System.out.println("Ocurrio un error al accesar al metodo selectCuentaContableNoExisten "+e.getMessage()+" "+SQL);
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
          return resultado;
       } //fin Select 
       
        public int selectCountCuenta(Connection con, String sentencia)throws SQLException, Exception{
           Statement stQuery=null;
           ResultSet rsQuery= null;
           int SidPoliza = 0;
            String SQL=null;
           try{
             SQL = sentencia;
             stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
             rsQuery = stQuery.executeQuery(SQL);
             //  secuencia para poliza_id
             while (rsQuery.next()){
                SidPoliza = rsQuery.getInt("registros");
             } //del while
           } //Fin try
           catch(Exception e){
             System.out.println("Ocurrio un error al accesar al metodo selectCountCuenta "+e.getMessage()+" "+SQL);
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
           return SidPoliza;
        } //fin Select 
       
  } //Fin clase bcCuentaContable