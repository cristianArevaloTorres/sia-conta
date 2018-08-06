package sia.rf.contabilidad.sistemas.mipf.transformacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import sia.rf.contabilidad.registroContableEvento.Cadena;
import sia.rf.contabilidad.registroContableNuevo.bcProveedores;
import sia.rf.contabilidad.sistemas.conprove.costos.SiafmCostosProduccion;
import sia.rf.contabilidad.sistemas.mipf.modDescarga.Aportacion;
import sia.rf.contabilidad.sistemas.mipf.modDescarga.CuentaPorLiquidar;
import sia.rf.contabilidad.sistemas.mipf.modDescarga.Factura;
import sia.rf.contabilidad.sistemas.mipf.modDescarga.Funcion;
import sia.rf.contabilidad.sistemas.mipf.modDescarga.HistoricoEje;
import sia.rf.contabilidad.sistemas.mipf.modDescarga.HistoricoRet;
import sia.rf.contabilidad.sistemas.mipf.modDescarga.Impuesto;
import sia.rf.contabilidad.sistemas.mipf.modDescarga.Retencion;
import sia.rf.contabilidad.sistemas.mipf.modDescarga.SentenciasMipf;
import sia.ws.publicar.contabilidad.Registro;
import sun.jdbc.rowset.CachedRowSet;

public class Totales {

    protected double totalBruto;
    protected double totalAportaciones;
    protected double totalRetenciones;
    protected double totalTodasRetenciones;
    protected double totalCAS;
    protected double totalISR;
    protected double totalNeto;
    protected double totalAntesIVA;
    protected double totalIVA;    
    
    protected boolean totalNetoCero;
    protected boolean totalBrutoCero;
    
    protected HashMap importeAport=null; 
    protected HashMap importeRete=null;
    protected HashMap importe=null; 
    protected HashMap importeIVA=null;
    protected HashMap importeReteTIGA=null;
    protected HashMap importeReteConPartida=null;
    protected HashMap importeReteCapitulo=null;
    protected HashMap importeRetePartidaGen=null;
    
    protected String programa;
    protected String unidad;
    protected String entidad;
    protected String ambito;
    protected String tiga;
    protected String tigaCapitulo;
    protected String referencia;
    protected String concepto;
    protected String anticipo;
    protected String capitulo;
    protected String conPartida;
    protected String partidaGen;
    protected String rfc;
    protected String descripcionRFC;
    protected String claveProveedor;
    protected String claveConcepto;
    protected String tipoDocto;
    protected String cuenta;
    protected String tipoCuenta;
    protected String ambitoCuenta;
    protected String ejercicio;
    protected String tipo_afectacion;
    protected String entidadAmbitoCuentaBancaria;
    protected String unidadCuentaBancaria;
    protected String id_tipo_beneficiario;
    
    protected CachedRowSet crsSentencia=null;
    protected CachedRowSet crsSentenciaAportacion=null;
    protected CachedRowSet crsSentenciaRetencion=null;
    protected CachedRowSet crsSentenciaImpuesto=null;
    protected CachedRowSet crsSentenciaFuncion=null;

    protected CuentaPorLiquidar cuentaPorLiquidar=null;
    protected Factura factura=null;
    protected HistoricoEje historicoEje=null;
    protected HistoricoRet historicoRet=null;
    
    protected CachedRowSet crsCuentaPorLiquidar=null;
    protected CachedRowSet crsFactura=null;
    protected CachedRowSet crsHistoricoEje=null;    
    protected CachedRowSet crsHistoricoRet=null;

    protected int factor=0;
    protected int banAre=0;
    protected int banAre_ant=0;
    protected int banProv=0;
    protected int factorNetoNeg=0;
    protected boolean banDeduccionDA=false;
    protected boolean bantotTodasRetenciones=false;
    protected boolean banDevengado;
    protected String partida;
    protected String cveret;
    
    public Totales() {
      cuentaPorLiquidar = new CuentaPorLiquidar();
      factura = new Factura();
      historicoEje = new HistoricoEje();
      historicoRet = new HistoricoRet();
            
      importeAport = new HashMap();
      importeRete = new HashMap();   
      importeReteTIGA = new HashMap();
      importeReteConPartida = new HashMap();
      importeReteCapitulo = new HashMap();
      importeRetePartidaGen = new HashMap();
      importe = new HashMap();
      importeIVA = new HashMap();
    }
    
    
    
    
    public void setTotalbruto(double totalBruto) {
        this.totalBruto = totalBruto;
    }

    public double getTotalBruto() {
        return totalBruto;
    }
    
    public double getTotalAportaciones() {
        return totalAportaciones;
    }

    public void setTotalAportaciones(double totalAportaciones) {
        this.totalAportaciones = totalAportaciones;
    }
    
    public double getTotalRetenciones() {
        return totalRetenciones;
    }

    public void setTotalRetenciones(double totalRetenciones) {
        this.totalRetenciones = totalRetenciones;
    }

    public double getTotalCAS() {
        return totalCAS;
    }

    public void setTotalCAS(double totalCAS) {
        this.totalCAS = totalCAS;
    }

    public double getTotalISR() {
        return totalISR;
    }

    public void setTotalISR(double totalISR) {
        this.totalISR = totalISR;
    }
    
    public void setTotalNeto(double totalNeto) {
        this.totalNeto = totalNeto;
    }

    public double getTotalNeto() {
        return totalNeto;
    }

    public double getTotalAntesIVA() {
        return totalAntesIVA;
    }

    public void setTotalAntesIVA(double totalAntesIVA) {
        this.totalAntesIVA = totalAntesIVA;
    }

    public double getTotalIVA() {
        return totalIVA;
    }

    public void setTotalIVA(double totalIVA) {
        this.totalIVA = totalIVA;
    }
    public void setTotalNetoCero(boolean totalNetoCero) {
        this.totalNetoCero = totalNetoCero;
    }

    public boolean isTotalNetoCero() {
        return totalNetoCero;
    }
    public void setTotalBrutoCero(boolean totalBrutoCero) {
        this.totalBrutoCero = totalBrutoCero;
    }

    public boolean isTotalBrutoCero() {
        return totalBrutoCero;
    }
   
    public void setImporteAport(HashMap importeAport) {
        this.importeAport = importeAport;
    }

    public HashMap getImporteAport() {
        return importeAport;
    }
    
    public void setPrograma(String programa) {
        this.programa = programa;
    }

    public String getPrograma() {
        return programa;
    }    
    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public String getEntidad() {
        return entidad;
    }

    public void setEntidad(String entidad) {
        this.entidad = entidad;
    }

    public String getAmbito() {
        return ambito;
    }

    public void setAmbito(String ambito) {
        this.ambito = ambito;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public String getAnticipo() {
        return anticipo;
    }

    public void setAnticipo(String anticipo) {
        this.anticipo = anticipo;
    }

    public String getCapitulo() {
        return capitulo;
    }

    public void setCapitulo(String capitulo) {
        this.capitulo = capitulo;
    }

    public void setClaveProveedor(String claveProveedor) {
        this.claveProveedor = claveProveedor;
    }

    public String getClaveProveedor() {
        return claveProveedor;
    }

    public void setClaveConcepto(String claveConcepto) {
        this.claveConcepto = claveConcepto;
    }

    public String getClaveConcepto() {
        return claveConcepto;
    }

    public int getFactor() {
        return factor;
    }

    public void setFactor(int factor) {
        this.factor = factor;
    }

    public int getBanAre() {
        return banAre;
    }

    public void setBanAre(int banAre) {
        this.banAre = banAre;
    }

    public void setBanAre_ant(int banAre_ant) {
        this.banAre_ant = banAre_ant;
    }

    public int getBanAre_ant() {
        return banAre_ant;
    }

    public String getCuenta() {
        return cuenta;
    }

    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }

    public String getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public String getAmbitoCuenta() {
        return ambitoCuenta;
    }

    public void setAmbitoCuenta(String ambitoCuenta) {
        this.ambitoCuenta = ambitoCuenta;
    }
    
    public int getBanProv() {
        return banProv;
    }

    public void setBanProv(int banProv) {
        this.banProv = banProv;
    }
    public void setFactorNetoNeg(int factorNetoNeg) {
        this.factorNetoNeg = factorNetoNeg;
    }

    public int getFactorNetoNeg() {
        return factorNetoNeg;
    }

    public void setEjercicio(String ejercicio) {
        this.ejercicio = ejercicio;
    }

    public String getEjercicio() {
        return ejercicio;
    }
    
    public String getConPartida() {
        return conPartida;
    }

    public void setConPartida(String conPartida) {
        this.conPartida = conPartida;
    }   
    public void setPartidaGen(String partidaGen) {
        this.partidaGen = partidaGen;
    }

    public String getPartidaGen() {
        return partidaGen;
    }    
    public void setTotalTodasRetenciones(double totalTodasRetenciones) {
        this.totalTodasRetenciones = totalTodasRetenciones;
    }

    public double getTotalTodasRetenciones() {
        return totalTodasRetenciones;
    }        
    public void setTipo_afectacion(String tipo_afectacion) {
        this.tipo_afectacion = tipo_afectacion;
    }

    public String getTipo_afectacion() {
        return tipo_afectacion;
    }
  public void setBantotTodasRetenciones(boolean bantotTodasRetenciones) {
    this.bantotTodasRetenciones = bantotTodasRetenciones;
  }

  public boolean isBantotTodasRetenciones() {
    return bantotTodasRetenciones;
  }

  public void setBanDevengado(boolean banDevengado) {
    this.banDevengado = banDevengado;
  }

  public boolean isBanDevengado() {
    return banDevengado;
  }
  
  public void setEntidadAmbitoCuentaBancaria(String entidadAmbitoCuentaBancaria) {
     this.entidadAmbitoCuentaBancaria = entidadAmbitoCuentaBancaria;
  }

  public String getEntidadAmbitoCuentaBancaria() {
     return entidadAmbitoCuentaBancaria;
  }
    public void setUnidadCuentaBancaria(String unidadCuentaBancaria) {
        this.unidadCuentaBancaria = unidadCuentaBancaria;
    }

    public String getUnidadCuentaBancaria() {
        return unidadCuentaBancaria;
    }

    public void leeModeloMipf(Connection conexionContabilidad, Connection conexionSistema, String evento, String parametros) throws SQLException, Exception {
        SentenciasMipf sentenciasMipf = new SentenciasMipf();
        String sentenciaSustituida;
        try {
            crsSentencia = sentenciasMipf.select_rf_tc_sentencias_evento(conexionContabilidad, evento);
            System.out.println("Parametros en querys MIPF:  " + parametros);
            crsSentencia.beforeFirst();
            while (crsSentencia.next()) {
                sentenciaSustituida = Cadena.sustituyeCadena(crsSentencia.getString("sentencia") + crsSentencia.getString("sentencia2") + crsSentencia.getString("sentencia3") + crsSentencia.getString("sentencia4") + crsSentencia.getString("sentencia5"), parametros, "&");
                if (crsSentencia.getString("tipo").equals("1")) { //Cuenta por liquidar
                    //System.out.println("Totales.leeModeloMipf.sentenciaSustituida(" + crsSentencia.getString("descripcion") + ")1 " + sentenciaSustituida);
                    crsCuentaPorLiquidar = cuentaPorLiquidar.select_rf_tr_ctaxliq_bd(conexionSistema, sentenciaSustituida);
                } else if (crsSentencia.getString("tipo").equals("2")) { //factura     
                    //System.out.println("Totales.leeModeloMipf.sentenciaSustituida(" + crsSentencia.getString("descripcion") + ")2 " + sentenciaSustituida);
                    crsFactura = factura.select_rf_tr_factura_bd(conexionSistema, sentenciaSustituida);
                } else if (crsSentencia.getString("tipo").equals("3")) { //Historico Eje
                    //System.out.println("Totales.leeModeloMipf.sentenciaSustituida(" + crsSentencia.getString("descripcion") + ")3 " + sentenciaSustituida);
                    crsHistoricoEje = historicoEje.select_rf_tr_histeje_bd(conexionSistema, sentenciaSustituida);
                } else { // Historico Ret
                    //System.out.println("Totales.leeModeloMipf.sentenciaSustituida(" + crsSentencia.getString("descripcion") + ")4 " + sentenciaSustituida);
                    crsHistoricoRet = historicoRet.select_rf_tr_histret_bd(conexionSistema, sentenciaSustituida);
                }
            }
        } catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo leeModelo " + e.getMessage());
            throw e;
        } //Fin catch 
        finally{ 
          if (crsSentencia!=null){
          crsSentencia.close();
          crsSentencia=null;
          }    
        }
    }
    
    public void verificaProveedor(Connection conexionContabilidad,Registro registro, String fecha, String pCatCuenta, String pCtaMayor,String pSubCtaMayor)throws SQLException, Exception{
      if (banProv==1||tipo_afectacion.equals("C")){
          crsFactura.beforeFirst();      
          while (crsFactura.next()){
            unidad=crsFactura.getString("unidad");            
            ambito=crsFactura.getString("ambito");
            programa=crsFactura.getString("programa");
            
            rfc=crsFactura.getString("cveprov");
            descripcionRFC=crsFactura.getString("nomprov");
            claveConcepto=crsFactura.getString("claveConcepto");
            String resultado="";
            bcProveedores bcProveedor = new bcProveedores();
            claveProveedor=bcProveedor.select_rf_tc_proveedores_rfc(conexionContabilidad,rfc);
            if (claveProveedor.equals("0")){
               claveProveedor=bcProveedor.select_SEQ_rf_tc_proveedores(conexionContabilidad);
               bcProveedor.setId_contable(claveProveedor);
               bcProveedor.setRfc(rfc);
               bcProveedor.setNombre_razon_social(descripcionRFC);
               bcProveedor.insert_rf_tc_proveedores(conexionContabilidad);
            }    
            if (banProv==1){
               resultado=registro.AgregaCuentaContable(pCtaMayor,pSubCtaMayor,programa,unidad,ambito,claveProveedor,"0000","0000","0000",fecha,descripcionRFC,"6",pCatCuenta);
            }
      }
      } 
    }  

    
    public String calculaTotalBrutoHisEjeCapitulo(Connection conexionContabilidad, int numForma, String pTipo) throws SQLException, Exception{ 
      HashMap hashMap;       
      HashMap hashMapHisEjeCap = new HashMap();  
      StringBuilder cadena= new StringBuilder("");
      Set sKey;
      String st;
      Iterator i;
      String lsPrograma;
      String lsUnidad;
      String lsAmbito;
      String lsCapitulo;
      String lsTiga;
      
      crsHistoricoEje.beforeFirst();      
      while (crsHistoricoEje.next()){
          try{
              cveret = crsHistoricoEje.getString("cveret");cveret=cveret==null?" ":cveret;
          }catch(Exception e){
              cveret = " ";
          }
        acumulaAportacion(crsHistoricoEje.getString("programa")+crsHistoricoEje.getString("unidad")+crsHistoricoEje.getString("ambito")+crsHistoricoEje.getString("capitulo")+crsHistoricoEje.getString("tiga")+cveret,Double.parseDouble((crsHistoricoEje.getString("importe")== null)? "0.00":crsHistoricoEje.getString("importe")),hashMapHisEjeCap); 
        tipoDocto=crsHistoricoEje.getString("Tipo_docto");
      }  
      sKey = hashMapHisEjeCap.keySet();  
      i = sKey.iterator();
      while(i.hasNext()){
        st = (String) i.next();
        lsPrograma=st.substring(0,4);
        lsUnidad=st.substring(4,8);
        lsAmbito=st.substring(8,12);        
        lsCapitulo=st.substring(12,16);
        lsTiga=st.substring(16,20);
        cveret=st.substring(20);
//        System.out.println("Totales.calculaTotalBrutoHisEjeCapitulo lsCapitulo = "+lsCapitulo);
//        System.out.println("Totales.calculaTotalBrutoHisEjeCapitulo cveret = "+cveret);
        totalBruto=(Double) hashMapHisEjeCap.get(st);
//        System.out.println("Totales.calculaTotalBrutoHisEjeCapitulo totalBruto = "+totalBruto);
        calculaTotalRetencionesCapitulo(lsPrograma,lsUnidad, lsAmbito, lsCapitulo, importeReteCapitulo);
        programa=lsPrograma;
        unidad=lsUnidad;
        ambito=lsAmbito;
        capitulo=lsCapitulo;
        tiga=lsTiga;
        calculaTotalNeto();
       // se quita el capitulo 6000, ya que este no necesita ahora ir a buscar el dato del proveedor, salvador  09-08-2011
      // if ((anticipo.equals("0")) &&(capitulo.equals("5000") || capitulo.equals("6000")))
       if ((anticipo.equals("0")) &&(capitulo.equals("5000"))) {
              setBanProv(1);
          }
        hashMap=creaHashMapVariable(conexionContabilidad,numForma,pTipo);
        cadena.append(Cadena.construyeCadena(hashMap));
        cadena.append("~");
        hashMap.clear();            
      }
//      System.out.println("Totales.calculaTotalBrutoHisEjeCapitulo.cadena "+cadena);
      return cadena.toString();
    }  
    
    public String calculaTotalBrutoHisEjeConPartida(Connection conexionContabilidad, int numForma, String pTipo) throws SQLException, Exception{ 
      HashMap hashMap;       
      HashMap hashMapHisEjeConPartida = new HashMap();  
      StringBuilder cadena= new StringBuilder("");
      Set sKey;
      String st;
      Iterator i;
      String lsPrograma;
      String lsUnidad;
      String lsAmbito;
      String lsConPartida;
      
      crsHistoricoEje.beforeFirst();      
      while (crsHistoricoEje.next()){
          try{
              cveret = crsHistoricoEje.getString("cveret");cveret=cveret==null?"* ":"*"+cveret;
          }catch(Exception e){
              cveret = "* ";
          }
        acumulaAportacion(crsHistoricoEje.getString("programa")+crsHistoricoEje.getString("unidad")+crsHistoricoEje.getString("ambito")+crsHistoricoEje.getString("partida")+cveret,Double.parseDouble((crsHistoricoEje.getString("importe")== null)? "0.00":crsHistoricoEje.getString("importe")),hashMapHisEjeConPartida); 
        tipoDocto=crsHistoricoEje.getString("Tipo_docto");
      }  
      sKey = hashMapHisEjeConPartida.keySet();  
      i = sKey.iterator();
      while(i.hasNext()){
        st = (String) i.next();
        lsPrograma=st.substring(0,4);
        lsUnidad=st.substring(4,8);
        lsAmbito=st.substring(8,12);
        partida=st.substring(12,st.indexOf("*"));
        lsConPartida = partida.substring(0,2);
        cveret=st.substring(st.indexOf("*")+1);
//        System.out.println("Totales.calculaTotalBrutoHisEjeConPartida lsConPartida = "+lsConPartida);
//        System.out.println("Totales.calculaTotalBrutoHisEjeConPartida cveret = "+cveret);
//        System.out.println("Totales.calculaTotalBrutoHisEjeConPartida pTipo = "+pTipo);
        totalBruto=(Double) hashMapHisEjeConPartida.get(st);
//        System.out.println("Totales.calculaTotalBrutoHisEjeConPartida totalBruto = "+totalBruto);
        calculaTotalRetencionesConPartida(lsPrograma,lsUnidad, lsAmbito, lsConPartida, importeReteConPartida);
        calculaTotalNeto();
        programa=lsPrograma;
        unidad=lsUnidad;
        ambito=lsAmbito;
        conPartida=lsConPartida;
        partidaGen=lsConPartida;
        if ((anticipo.equals("0")) &&(conPartida.substring(0,1).equals("5"))) {
              setBanProv(1);
          }
        hashMap=creaHashMapVariable(conexionContabilidad,numForma,pTipo);
        cadena.append(Cadena.construyeCadena(hashMap));
        cadena.append("~");
        hashMap.clear();            
      }
//      System.out.println("Totales.calculaTotalBrutoHisEjeConPartida.cadena "+cadena);
      return cadena.toString();
    }  

    public String calculaTotalBrutoHisEjePartidaGen(Connection conexionContabilidad, int numForma, String pTipo) throws SQLException, Exception{ 
      HashMap hashMap;       
      HashMap hashMapHisEjePartidaGen = new HashMap();  
      StringBuilder cadena= new StringBuilder("");
      Set sKey;
      String st;
      Iterator i;
      String lsPrograma;
      String lsUnidad;
      String lsAmbito;
      String lsPartidaGen;
      crsHistoricoEje.beforeFirst();      
      while (crsHistoricoEje.next()){
          try{
              cveret = crsHistoricoEje.getString("cveret");cveret=cveret==null?" ":cveret;
          }catch(Exception e){
              cveret = " ";
          }
        acumulaAportacion(crsHistoricoEje.getString("programa")+crsHistoricoEje.getString("unidad")+crsHistoricoEje.getString("ambito")+crsHistoricoEje.getString("partida").substring(0,3)+cveret,Double.parseDouble((crsHistoricoEje.getString("importe")== null)? "0.00":crsHistoricoEje.getString("importe")),hashMapHisEjePartidaGen); 
        tipoDocto=crsHistoricoEje.getString("Tipo_docto");

      }
      sKey = hashMapHisEjePartidaGen.keySet();  
      i = sKey.iterator();
      while(i.hasNext()){
        st = (String) i.next();
        lsPrograma=st.substring(0,4);
        lsUnidad=st.substring(4,8);
        lsAmbito=st.substring(8,12);
        lsPartidaGen=st.substring(12,15);        
        cveret=st.substring(15);
//        System.out.println("Totales.calculaTotalBrutoHisEjePartidaGen lsPartidaGen = "+lsPartidaGen);
//        System.out.println("Totales.calculaTotalBrutoHisEjePartidaGen cveret = "+cveret);
//        System.out.println("Totales.calculaTotalBrutoHisEjePartidaGen pTipo = "+pTipo);
        totalBruto=(Double) hashMapHisEjePartidaGen.get(st);
//        System.out.println("Totales.calculaTotalBrutoHisEjePartidaGen totalBruto = "+totalBruto);
        calculaTotalRetencionesPartidaGen(lsPrograma,lsUnidad, lsAmbito, lsPartidaGen, importeRetePartidaGen);
        calculaTotalNeto();
        programa=lsPrograma;
        unidad=lsUnidad;
        ambito=lsAmbito;
        partidaGen=lsPartidaGen;
        if ((anticipo.equals("0")) &&(partidaGen.substring(0,1).equals("5"))) {
              setBanProv(1);
          }
        hashMap=creaHashMapVariable(conexionContabilidad,numForma,pTipo);
        cveret=" ";
        cadena.append(Cadena.construyeCadena(hashMap));
        cadena.append("~");
        hashMap.clear();
      }
//      System.out.println("Totales.calculaTotalBrutoHisEjePartidaGen.cadena "+cadena);
      return cadena.toString();
    }

    protected Double buscaIVA(String pPrograma,String pUnidad, String pAmbito, String pCapitulo, HashMap hashMapIva ){
        // Agrega la parte de factura solo el iva a la forma 
        Set sKey;
        String st;
        Iterator i;    
        String lsPrograma;
        String lsUnidad;
        String lsAmbito;
        String lsCapitulo;
        Double IVA=0.0D;

        sKey = hashMapIva.keySet();  
        i = sKey.iterator();
        while(i.hasNext()){
          st = (String) i.next();
          lsPrograma=st.substring(0,4);
          lsUnidad=st.substring(4,8);
          lsAmbito=st.substring(8,12);
          lsCapitulo=st.substring(12);
          setTotalIVA((Double) hashMapIva.get(st));
          programa=lsPrograma;
          unidad=lsUnidad;
          ambito=lsAmbito;
          capitulo=lsCapitulo;
          if (lsPrograma.equals(pPrograma) && lsUnidad.equals(pUnidad) && lsAmbito.equals(pAmbito) && lsCapitulo.equals(pCapitulo)){
            IVA=(Double) hashMapIva.get(st);
            break;
          }  
        }
        return IVA;
    }
    
    public String calculaTotalFacturaCapitulo(Connection conexionContabilidad, int numForma, String pTipo, Registro registro, String fecha, String pCatCuenta) throws SQLException, Exception{ 
      HashMap hashMap;       
      HashMap hashMapFactura = new HashMap();  
      HashMap hashMapIva = new HashMap();       
      StringBuilder cadena= new StringBuilder("");
      Set sKey;
      String st;
      Iterator i;    
      String lsPrograma;
      String lsUnidad;
      String lsAmbito;
      String lsCapitulo;
      String resultado;
      
      crsFactura.beforeFirst();      
      while (crsFactura.next()){
        acumulaAportacion(crsFactura.getString("programa")+crsFactura.getString("unidad")+crsFactura.getString("ambito")+crsFactura.getString("capitulo"),Double.parseDouble((crsFactura.getString("importe")== null)? "0.00":crsFactura.getString("importe")),hashMapFactura); 
        acumulaAportacion(crsFactura.getString("programa")+crsFactura.getString("unidad")+crsFactura.getString("ambito")+crsFactura.getString("capitulo"),Double.parseDouble((crsFactura.getString("iva")== null)? "0.00":crsFactura.getString("iva")),hashMapIva); 
        rfc=crsFactura.getString("cveprov");
        descripcionRFC=crsFactura.getString("nomprov");
        tiga=crsFactura.getString("tiga");
        capitulo=crsFactura.getString("capitulo");
        tigaCapitulo=crsFactura.getString("tigaCapitulo");
        claveConcepto=crsFactura.getString("claveConcepto");
        unidad=crsFactura.getString("unidad");            
        ambito=crsFactura.getString("ambito");
        programa=crsFactura.getString("programa");        
        bcProveedores bcProveedor = new bcProveedores();
        claveProveedor=bcProveedor.select_rf_tc_proveedores_rfc(conexionContabilidad,rfc);
        if (claveProveedor.equals("0")){
          claveProveedor=bcProveedor.select_SEQ_rf_tc_proveedores(conexionContabilidad);
          bcProveedor.setId_contable(claveProveedor);
          bcProveedor.setRfc(rfc);
          bcProveedor.setNombre_razon_social(descripcionRFC);
          bcProveedor.insert_rf_tc_proveedores(conexionContabilidad);
        } 
    
        if (capitulo.equals("2000")||capitulo.equals("3000")){//anticipo a prov
           resultado=registro.AgregaCuentaContable("1131",capitulo.substring(0,1),programa,unidad,ambito,claveProveedor,"0000","0000","0000",fecha,descripcionRFC,"6",pCatCuenta);           
        } else if (capitulo.equals("6000")){//anticipo a contratista
           resultado=registro.AgregaCuentaContable("1134","1",programa,unidad,ambito,claveProveedor,"0000","0000","0000",fecha,descripcionRFC,"6",pCatCuenta);
           resultado=registro.AgregaCuentaContable("1134","1",programa,unidad,ambito,claveProveedor,"0001","0000","0000",fecha,"Obra Pública","7",pCatCuenta);
           resultado=registro.AgregaCuentaContable("1134","1",programa,unidad,ambito,claveProveedor,"0002","0000","0000",fecha,"Iva de Obra Pública","7",pCatCuenta);        
        }
    }
      sKey = hashMapFactura.keySet();  
      i = sKey.iterator();
      while(i.hasNext()){
        st = (String) i.next();
        lsPrograma=st.substring(0,4);
        lsUnidad=st.substring(4,8);
        lsAmbito=st.substring(8,12);
        lsCapitulo=st.substring(12);
        setTotalAntesIVA((Double) hashMapFactura.get(st));
        setTotalIVA(buscaIVA(lsPrograma,lsUnidad, lsAmbito, lsCapitulo, hashMapIva));
        totalBruto=totalAntesIVA+totalIVA;
        programa=lsPrograma;
        unidad=lsUnidad;
        ambito=lsAmbito;
        capitulo=lsCapitulo;
        hashMap=creaHashMapVariable(conexionContabilidad,numForma,pTipo);
        cadena.append(Cadena.construyeCadena(hashMap));
        cadena.append("~");
        hashMap.clear();
      }      
      return cadena.toString();
    }  

    public String calculaTotalFacturaConPartida(Connection conexionContabilidad, int numForma, String pTipo, Registro registro, String fecha, String pCatCuenta) throws SQLException, Exception{ 
      HashMap hashMap;       
      HashMap hashMapFacturaConPartida = new HashMap();  
      HashMap hashMapIvaConPartida = new HashMap();       
      StringBuilder cadena= new StringBuilder("");
      Set sKey;
      String st;
      Iterator i;    
      String lsPrograma;
      String lsUnidad;
      String lsAmbito;
      String lsConPartida;
      String resultado="";
      
      bcProveedores bcProveedor = new bcProveedores();
      crsFactura.beforeFirst();      
      while (crsFactura.next()){
        acumulaAportacion(crsFactura.getString("programa")+crsFactura.getString("unidad")+crsFactura.getString("ambito")+crsFactura.getString("partida"),Double.parseDouble((crsFactura.getString("importe")== null)? "0.00":crsFactura.getString("importe")),hashMapFacturaConPartida); 
        acumulaAportacion(crsFactura.getString("programa")+crsFactura.getString("unidad")+crsFactura.getString("ambito")+crsFactura.getString("partida"),Double.parseDouble((crsFactura.getString("iva")== null)? "0.00":crsFactura.getString("iva")),hashMapIvaConPartida); 
        lsConPartida=crsFactura.getString("partida").substring(0,2);
        rfc=crsFactura.getString("cveprov");
        descripcionRFC=crsFactura.getString("nomprov");
        tiga=crsFactura.getString("tiga");
        capitulo=crsFactura.getString("capitulo");
        tigaCapitulo=crsFactura.getString("tigaCapitulo");
        claveConcepto=crsFactura.getString("claveConcepto");
        unidad=crsFactura.getString("unidad");            
        ambito=crsFactura.getString("ambito");
        programa=crsFactura.getString("programa"); 
        claveProveedor=bcProveedor.select_rf_tc_proveedores_rfc(conexionContabilidad,rfc);
        if (claveProveedor.equals("0")){
            claveProveedor=bcProveedor.select_SEQ_rf_tc_proveedores(conexionContabilidad);
            bcProveedor.setId_contable(claveProveedor);
            bcProveedor.setRfc(rfc);
            bcProveedor.setNombre_razon_social(descripcionRFC);
            bcProveedor.insert_rf_tc_proveedores(conexionContabilidad);
       } 
          if (lsConPartida.equals("59")){
             resultado=registro.AgregaCuentaContable("1133","1",programa,unidad,ambito,claveProveedor,"0000","0000","0000",fecha,descripcionRFC,"6",pCatCuenta);
             resultado=registro.AgregaCuentaContable("1133","1",programa,unidad,ambito,claveProveedor,"0001","0000","0000",fecha,"Bienes inventariables","7",pCatCuenta);
             resultado=registro.AgregaCuentaContable("1133","1",programa,unidad,ambito,claveProveedor,"0002","0000","0000",fecha,"Iva de bienes inventariables","7",pCatCuenta);
          } else if (lsConPartida.equals("50")||lsConPartida.equals("51")||lsConPartida.equals("53")||lsConPartida.equals("54")||lsConPartida.equals("55")||lsConPartida.equals("56")||lsConPartida.equals("57")||lsConPartida.equals("58")){
             resultado=registro.AgregaCuentaContable("1132","1",programa,unidad,ambito,claveProveedor,"0000","0000","0000",fecha,descripcionRFC,"6",pCatCuenta);
             resultado=registro.AgregaCuentaContable("1132","1",programa,unidad,ambito,claveProveedor,"0001","0000","0000",fecha,"Bienes inventariables","7",pCatCuenta);
             resultado=registro.AgregaCuentaContable("1132","1",programa,unidad,ambito,claveProveedor,"0002","0000","0000",fecha,"Iva de bienes inventariables","7",pCatCuenta);
          }
      }  
      sKey = hashMapFacturaConPartida.keySet();  
      i = sKey.iterator();
      while(i.hasNext()){
        st = (String) i.next();
        lsPrograma=st.substring(0,4);
        lsUnidad=st.substring(4,8);
        lsAmbito=st.substring(8,12);
        partida=st.substring(12);
        lsConPartida=partida.substring(0,2);
        setTotalAntesIVA((Double) hashMapFacturaConPartida.get(st));
        setTotalIVA(buscaIVA(lsPrograma,lsUnidad, lsAmbito, lsConPartida, hashMapIvaConPartida));
        totalBruto=totalAntesIVA+totalIVA;
        programa=lsPrograma;
        unidad=lsUnidad;
        ambito=lsAmbito;
        conPartida=lsConPartida;
        hashMap=creaHashMapVariable(conexionContabilidad,numForma,pTipo);
        cadena.append(Cadena.construyeCadena(hashMap));
        cadena.append("~");
        hashMap.clear();
      }      
      return cadena.toString();
    }  



    public String calculaTotalBrutoHisEje(Connection conexionContabilidad, int numForma) throws SQLException, Exception{ 
      HashMap hashMap;       
      HashMap hashMapHisEjeTIGA = new HashMap();  
      StringBuilder cadena= new StringBuilder("");
      Set sKey;
      String st;
      Iterator i;
      String lsPrograma;
      String lsUnidad;
      String lsAmbito;
      String lsTiga;
      String lsTigaCapitulo;
      String lsPartida;

      crsHistoricoEje.beforeFirst();      
      while (crsHistoricoEje.next()){
          try{
              cveret = crsHistoricoEje.getString("cveret");cveret=cveret==null?" ":cveret;
          }catch(Exception e){
              cveret = " ";
          }
          try{
              lsPartida = crsHistoricoEje.getString("partida");lsPartida=lsPartida==null?" ":lsPartida;
          }catch(Exception e){
              lsPartida = " ";
          }
        acumulaAportacion(crsHistoricoEje.getString("programa")+crsHistoricoEje.getString("unidad")+crsHistoricoEje.getString("ambito")+crsHistoricoEje.getString("tiga")+crsHistoricoEje.getString("tigaCapitulo")+cveret+"*"+lsPartida,Double.parseDouble((crsHistoricoEje.getString("importe")== null)? "0.00":crsHistoricoEje.getString("importe")),hashMapHisEjeTIGA); 
        tipoDocto=crsHistoricoEje.getString("Tipo_docto");
      }  
      sKey = hashMapHisEjeTIGA.keySet();  
      i = sKey.iterator();
      while(i.hasNext()){
        st = (String) i.next();
        //System.out.println("Eje");
        lsPrograma=st.substring(0,4);
        lsUnidad=st.substring(4,8);
        lsAmbito=st.substring(8,12);
        lsTiga=st.substring(12,16);
        lsTigaCapitulo=st.substring(16,20);
        cveret=st.substring(20,st.indexOf("*"));
        partida=st.substring(st.indexOf("*")+1);
        lsPartida=partida.substring(0,3);
//        System.out.println("Totales.calculaTotalBrutoHisEje cveret = "+cveret);
//        System.out.println("Totales.calculaTotalBrutoHisEje lsPartida = "+lsPartida);
        partidaGen = lsPartida;
        conPartida = lsPartida.substring(0,2);
        totalBruto=(Double) hashMapHisEjeTIGA.get(st);
        //System.out.println("Totales.calculaTotalBrutoHisEje totalBruto = "+totalBruto);
        calculaTotalRetencionesTIGA(lsPrograma,lsUnidad, lsAmbito, lsTiga, importeReteTIGA);
        programa=lsPrograma;
        unidad=lsUnidad;
        ambito=lsAmbito;
        tiga=lsTiga;
        tigaCapitulo=lsTigaCapitulo;
        calculaTotalNeto();
        hashMap=creaHashMap(conexionContabilidad,numForma);
        cadena.append(Cadena.construyeCadena(hashMap));
        cadena.append("~");
        hashMap.clear();    
      }
//      System.out.println("Totales.calculaTotalBrutoHisEje.cadena "+cadena);
      return cadena.toString();
    }  

    public String generaFormaFinaciera(Connection conexionContabilidad, int numForma, String ambitoCuenta, String tipoCuenta) throws SQLException, Exception{ 
      HashMap hashMap;       
      HashMap hashMapHisEjeTIGA = new HashMap();  
      StringBuilder cadena= new StringBuilder("");
      Set sKey;
      String st;
      Iterator i;
      String lsPrograma;
      String lsUnidad;
      String lsAmbito;
      String lsTiga;
      String lsTigaCapitulo;

      setAmbitoCuenta(ambitoCuenta);
      setTipoCuenta(tipoCuenta);
      
      crsHistoricoEje.beforeFirst();      
      while (crsHistoricoEje.next()){
        acumulaAportacion(crsHistoricoEje.getString("programa")+crsHistoricoEje.getString("unidad")+crsHistoricoEje.getString("ambito")+crsHistoricoEje.getString("tiga")+crsHistoricoEje.getString("tigaCapitulo"),Double.parseDouble((crsHistoricoEje.getString("importe")== null)? "0.00":crsHistoricoEje.getString("importe")),hashMapHisEjeTIGA); 
        tipoDocto=crsHistoricoEje.getString("Tipo_docto");
      }  
      sKey = hashMapHisEjeTIGA.keySet();  
      i = sKey.iterator();
      while(i.hasNext()){
        st = (String) i.next();
        lsPrograma=st.substring(0,4);
        lsUnidad=st.substring(4,8);
        lsAmbito=st.substring(8,12);
        lsTiga=st.substring(12,16);
        lsTigaCapitulo=st.substring(16);
        totalBruto=(Double) hashMapHisEjeTIGA.get(st);
        calculaTotalRetencionesTIGA(lsPrograma,lsUnidad, lsAmbito, lsTiga, importeReteTIGA);
        //System.out.println("Programa: "+lsPrograma+"Unidad:"+lsUnidad+"Ambito:"+lsAmbito);
        programa=lsPrograma;
        unidad=lsUnidad;
        ambito=lsAmbito;
        tiga=lsTiga;
        tigaCapitulo=lsTigaCapitulo;
        calculaTotalNeto();
        hashMap=creaHashMap(conexionContabilidad,numForma);
        cadena.append(Cadena.construyeCadena(hashMap));
        cadena.append("~");
        hashMap.clear();    
      }
      return cadena.toString();
    }  

 protected void calculaTotalRetencionesConPartida(String programa,String unidad, String ambito, String ConPartida, HashMap arreglo) throws SQLException, Exception{
   Double ldTotalRet=0D;  
   Double ldTotalCreSal=0D;
   Double ldTotalISR=0D;
   Double ldTotalP076=0D;
   Set sKey;
   String st;
   sKey = arreglo.keySet();
   Iterator i = sKey.iterator();
   String lsPrograma;
   String lsUnidad;
   String lsAmbito;
   String lsConPartida;
   String lsRetencion="";
   String lsId;
   String lsTipo;
   while(i.hasNext()){
     st = (String) i.next();
     lsPrograma=st.substring(0,4);
     lsUnidad=st.substring(4,8);
     lsAmbito=st.substring(8,12);
     lsConPartida=st.substring(12,14);
     lsRetencion=st.substring(14,18);
     lsId=st.substring(18);
     if ((lsPrograma.equals(programa)) && (lsUnidad.equals(unidad)) && (lsAmbito.equals(ambito)) && (lsConPartida.equals(ConPartida))) {
       lsTipo=buscaRetencionesTipo(lsId); 
       if (lsTipo.equals("C")) {
             ldTotalCreSal+=(Double) arreglo.get(st);
         }
       else
         if (lsTipo.equals("I")) {
             ldTotalISR+=(Double) arreglo.get(st);
         }
         else
           if (lsTipo.equals("P")) {
             ldTotalP076+=(Double) arreglo.get(st);
         }
         else {
             ldTotalRet+=(Double) arreglo.get(st);
         }
     }      
   }
   totalCAS=ldTotalCreSal;
   totalISR=ldTotalISR;
   totalRetenciones=ldTotalRet;
 }
    protected void calculaTotalRetencionesPartidaGen(String programa,String unidad, String ambito, String PartidaGen, HashMap arreglo) throws SQLException, Exception{
      Double ldTotalRet=0D;  
      Double ldTotalCreSal=0D;
      Double ldTotalISR=0D;
      Double ldTotalP076=0D;
      Set sKey;
      String st;
      sKey = arreglo.keySet();
      Iterator i = sKey.iterator();
      String lsPrograma;
      String lsUnidad;
      String lsAmbito;
      String lsPartidaGen;
      String lsRetencion;
      String lsId;
      String lsTipo;
      while(i.hasNext()){
        st = (String) i.next();
        lsPrograma=st.substring(0,4);
        lsUnidad=st.substring(4,8);
        lsAmbito=st.substring(8,12);
        lsPartidaGen=st.substring(12,15);
        lsRetencion=st.substring(15,19);
        lsId=st.substring(19);
        if ((lsPrograma.equals(programa)) && (lsUnidad.equals(unidad)) && (lsAmbito.equals(ambito)) && (lsPartidaGen.equals(PartidaGen))) {
          lsTipo=buscaRetencionesTipo(lsId); 
          if (lsTipo.equals("C")) {
                ldTotalCreSal+=(Double) arreglo.get(st);
            }
          else
            if (lsTipo.equals("I")) {
                ldTotalISR+=(Double) arreglo.get(st);
            }
          else
            if (lsTipo.equals("P")) {
                ldTotalP076+=(Double) arreglo.get(st);
            }
            else {
                ldTotalRet+=(Double) arreglo.get(st);
            }
        }      
      }
      totalCAS=ldTotalCreSal;
      totalISR=ldTotalISR;
      totalRetenciones=ldTotalRet;
    }



    protected void calculaTotalRetencionesTIGA(String programa,String unidad, String ambito, String tiga, HashMap arreglo) throws SQLException, Exception{
      Double ldTotalRet=0D;  
      Double ldTotalCreSal=0D;
      Double ldTotalISR=0D;
      Double ldTotalP076=0D;
      Set sKey;
      String st;
      sKey = arreglo.keySet();
      Iterator i = sKey.iterator();
      String lsPrograma;
      String lsUnidad;
      String lsAmbito;
      String lsTiga;
      String lsRetencion;
      String lsId;
      String lsTipo;
      while(i.hasNext()){
        st = (String) i.next();
        lsPrograma=st.substring(0,4);
        lsUnidad=st.substring(4,8);
        lsAmbito=st.substring(8,12);
        lsTiga=st.substring(12,16);
        lsRetencion=st.substring(16,20);
        lsId=st.substring(20);
        if ((lsPrograma.equals(programa)) && (lsUnidad.equals(unidad)) && (lsAmbito.equals(ambito)) && (lsTiga.equals(tiga))) {
          lsTipo=buscaRetencionesTipo(lsId); 
          if (lsTipo.equals("C")) {
                ldTotalCreSal+=(Double) arreglo.get(st);
            }
          else
            if (lsTipo.equals("I")) {
                ldTotalISR+=(Double) arreglo.get(st);
            }
            else 
               if (lsTipo.equals("P")) {
                ldTotalP076+=(Double) arreglo.get(st);
            }
            else {
                ldTotalRet+=(Double) arreglo.get(st);
            }
        }      
      }
      totalCAS=ldTotalCreSal;
      totalISR=ldTotalISR;
      totalRetenciones=ldTotalRet;
      
    }

    protected void calculaTotalRetencionesCapitulo(String programa,String unidad, String ambito, String capitulo, HashMap arreglo) throws SQLException, Exception{
      Double ldTotalRet=0D;  
      Double ldTotalCreSal=0D;
      Double ldTotalISR=0D;
      Double ldTotalP076=0D;
      Set sKey;
      String st;
      sKey = arreglo.keySet();
      Iterator i = sKey.iterator();
      String lsPrograma;
      String lsUnidad;
      String lsAmbito;
      String lsCapitulo;
      String lsRetencion="";
      String lsId;
      String lsTipo;
      while(i.hasNext()){
        st = (String) i.next();
        lsPrograma=st.substring(0,4);
        lsUnidad=st.substring(4,8);
        lsAmbito=st.substring(8,12);
        lsCapitulo=st.substring(12,16);
        lsRetencion=st.substring(16,20);
        lsId=st.substring(20);
        if ((lsPrograma.equals(programa)) && (lsUnidad.equals(unidad)) && (lsAmbito.equals(ambito)) && (lsCapitulo.equals(capitulo))) {
          lsTipo=buscaRetencionesTipo(lsId); 
          if (lsTipo.equals("C")) {
                ldTotalCreSal+=(Double) arreglo.get(st);
            }
          else
            if (lsTipo.equals("I")) {
                ldTotalISR+=(Double) arreglo.get(st);
            }
          else 
             if (lsTipo.equals("P")) {
                ldTotalP076+=(Double) arreglo.get(st);
            }
            else {
                ldTotalRet+=(Double) arreglo.get(st);
            }
        }      
      }
      totalCAS=ldTotalCreSal;
      totalISR=ldTotalISR;
      totalRetenciones=ldTotalRet;      
    }

    protected String buscaRetencionesTipo(String lsId) throws SQLException, Exception{ 
      String lsTipo="";
      crsSentenciaRetencion.beforeFirst();
      while (crsSentenciaRetencion.next()){
        if  (crsSentenciaRetencion.getString("retencion_id").equals(lsId)){       
          lsTipo=crsSentenciaRetencion.getString("tipo_ret");
          break;
        }  
      }
      return lsTipo;
    }
    
    //no se esta usando
    protected double calculaTotalAportaciones(String unidad, String ambito, HashMap arreglo){
      Double total=0D;  
      Set sKey;
      String st;
      sKey = arreglo.keySet();
      Iterator i = sKey.iterator();
      String lsUnidad;
      String lsAmbito;
      String lsId="";
      while(i.hasNext()){
        st = (String) i.next();
        lsUnidad=st.substring(0,4);
        lsAmbito=st.substring(4,8);
        lsId=st.substring(8);
        if ((lsUnidad.equals(unidad)) && (lsAmbito.equals(ambito))) {
              total=total+ (Double) arreglo.get(st);
          }
      }
      return total;
    }

    public String recorreTotalRetenciones() throws SQLException, Exception{ 
      Set sKey;
      String st;
      sKey = importeRete.keySet();
      Iterator i = sKey.iterator();
      String lsPrograma;
      String lsUnidad;
      String lsAmbito;
      String lsEjercicio;
      String lsRetencion="";
      String lsId;
      String lsTipo;
      StringBuilder cadena= new StringBuilder("");
      HashMap hashMap = new HashMap();       
      Double ldImporte;
      while(i.hasNext()){      
        st = (String) i.next();
        lsPrograma=st.substring(0,4);
        lsUnidad=st.substring(4,8);
        lsAmbito=st.substring(8,12);
        lsEjercicio=st.substring(12,16);
        if (st.substring(16,20).equals("D077")||st.substring(16,20).equals("D214")||st.substring(16,20).equals("D002")||st.substring(16,20).equals("D004")) {
           lsRetencion=st.substring(16,21);
           lsId=st.substring(21);
        }
        else   {
           lsRetencion=st.substring(16,20);
           lsId=st.substring(20);
            
        }
        crsSentenciaRetencion.beforeFirst();
        while (crsSentenciaRetencion.next()){
          if  (crsSentenciaRetencion.getString("retencion_id").equals(lsId)){       
            hashMap.put("PROGRAMA",lsPrograma); 
            hashMap.put("UNIDAD",lsUnidad);
            hashMap.put("AMBITO",lsAmbito);
            hashMap.put("IMP1",crsSentenciaRetencion.getString("nivel5"));
            hashMap.put("EJERCICIO",lsEjercicio);
            hashMap.put("EJERCICIO_ANT","00"+lsEjercicio.substring(2));
            hashMap.put("IMP2",crsSentenciaRetencion.getString("nivel6")); 
            lsTipo=crsSentenciaRetencion.getString("tipo_ret"); 
            ldImporte= (Double) importeRete.get(st);
            if (factor==-1) {
                  ldImporte=ldImporte*factor;
              }
            if (lsTipo.equals("C")){
              ldImporte=ldImporte*-1.0D;  //Multiplica la retencion de CAS por -1 siempre
              hashMap.put("IMPORTE_CAS",ldImporte); 
            }  
            else {
               //Se agrego el Tipo A para Anticipos retencion D032 11206
               if (lsTipo.equals("A")){
                   ldImporte=ldImporte*-1.0D;  //Multiplica la retencion de Anticipo a Sueldos (11206) por -1 siempre
                   hashMap.put("IMPORTE_ANTICIPOS",ldImporte); 
               }
               else{
                   if (lsTipo.equals("I")) {
                       hashMap.put("IMPORTE_ISR",ldImporte); 
                   } 
                   else
                     if (lsTipo.equals("P")) {
                       hashMap.put("IMPORTE_APORTA_P076",ldImporte); 
                   } 
                   
                   else {
                       hashMap.put("IMPORTE_RETENCIONES",ldImporte);
                   }         
               }
            }
            //ASIENTO DE RECLASIFICACION PARA ANTICIPO A SUELDOS
            
            if  (crsSentenciaRetencion.getString("retencion_id").equals("10")){ //Retencion id=10 correspondiente a D032 21204
                 hashMap.put("IMPORTE_RECLASIFICA_ANTICIPO",ldImporte);  
            }            
            hashMap.put("REFERENCIA",referencia);
            cadena.append(Cadena.construyeCadena(hashMap));
            cadena.append("~");
            hashMap.clear();       
            break;
          }  
        }
      }
      return cadena.toString();
    }
    public String recorreTotalAportaciones() throws SQLException, Exception{ 
      Set sKey;
      String st;
      sKey = importeAport.keySet();
      Iterator i = sKey.iterator();
      String lsPrograma;
      String lsUnidad;
      String lsAmbito;
      String lsEjercicio;
      String lsCapitulo;
      String lsId;      
      StringBuffer cadena= new StringBuffer("");
      HashMap hashMap = new HashMap();       
      Double ldImporte;  
     if (banDeduccionDA==false){
       while(i.hasNext()){
         st = (String) i.next();
         lsPrograma=st.substring(0,4);
         lsUnidad=st.substring(4,8);
         lsAmbito=st.substring(8,12);
         lsEjercicio=st.substring(12,16);
         lsCapitulo=st.substring(16,20);
         lsId=st.substring(20);
         crsSentenciaAportacion.beforeFirst();
         while (crsSentenciaAportacion.next()){
           if  (crsSentenciaAportacion.getString("aportacion_id").equals(lsId)){       
             ldImporte=(Double)importeAport.get(st);
             if (ldImporte!=0.0){
                hashMap.put("PROGRAMA",lsPrograma);
                hashMap.put("UNIDAD",lsUnidad);
                hashMap.put("AMBITO",lsAmbito);
                hashMap.put("IMP1",crsSentenciaAportacion.getString("nivel5"));
                hashMap.put("EJERCICIO",lsEjercicio);
                hashMap.put("CAPITULO",lsCapitulo);
                hashMap.put("IMP2",crsSentenciaAportacion.getString("nivel6")); 
                if (factor==-1) {
                     ldImporte=ldImporte*factor;
                 }
                hashMap.put("IMPORTE_APORTA",ldImporte); 
                hashMap.put("REFERENCIA",referencia);
                cadena.append(Cadena.construyeCadena(hashMap));
                cadena.append("~");
                hashMap.clear();       
             }
             break;
          }  
        }
      }
     }
      return cadena.toString();
    }    
    
    public Double calculaTotalBrutoPorCtaXLiq() throws SQLException, Exception{
      Double totalBruto=0.00; 
      crsCuentaPorLiquidar.beforeFirst();
      while (crsCuentaPorLiquidar.next()){ 
        totalBruto+=Double.parseDouble((crsCuentaPorLiquidar.getString("totBruto")== null)? "0.00":crsCuentaPorLiquidar.getString("totBruto"));
        programa=crsCuentaPorLiquidar.getString("programa");
      } 
      return totalBruto;         
    }  
    
    public int asignaPropiedadesCtaXLiq() throws SQLException, Exception{
        if(crsCuentaPorLiquidar==null) {
            return 0;
        }
      int regs=crsCuentaPorLiquidar.size();      
      crsCuentaPorLiquidar.beforeFirst();
      int posicionError = 0;
      /*for(int i=0;i< crsCuentaPorLiquidar.getMetaData().getColumnCount();i++) {
            System.out.println("crsCuentaPorLiquidar("+i+") "+crsCuentaPorLiquidar.getMetaData().getColumnName(i+1));
        }*/
      try{
      while (crsCuentaPorLiquidar.next()){
        posicionError = 0;
        unidad=crsCuentaPorLiquidar.getString("unidad");
        posicionError++;
        entidad=crsCuentaPorLiquidar.getString("entidad");
        posicionError++;
        ambito=crsCuentaPorLiquidar.getString("ambito");
        posicionError++;
        referencia=crsCuentaPorLiquidar.getString("CXLC");
        posicionError++;
        setConcepto(crsCuentaPorLiquidar.getString("concepto"));
        posicionError++;
        anticipo=crsCuentaPorLiquidar.getString("anticipo");
        posicionError++;
        cuenta=crsCuentaPorLiquidar.getString("ctaDisp")==null?"0":crsCuentaPorLiquidar.getString("ctaDisp");       
        posicionError++;
        //propiedad agregada para relevos
        tipo_afectacion=((crsCuentaPorLiquidar.getString("tipo_afectacion") == null)||("".equals(crsCuentaPorLiquidar.getString("tipo_afectacion")))) ? "" : crsCuentaPorLiquidar.getString("tipo_afectacion");
        posicionError++;
        //propiedad agregada para el caso de los relevos de beneficiario inegi
        try{
        id_tipo_beneficiario=((crsCuentaPorLiquidar.getString("id_tipo_beneficiario") == null)||("".equals(crsCuentaPorLiquidar.getString("id_tipo_beneficiario")))) ? "" : crsCuentaPorLiquidar.getString("id_tipo_beneficiario");
        }catch(Exception e)
        {
            id_tipo_beneficiario="";
        }
        posicionError++;
      }
      }
      catch(Exception e)
      {
          System.out.println(posicionError+" Totales.asignaPropiedadesCtaXLiq "+e.getClass().getName()+": "+e.getMessage());
      }
      return regs;
    }    
    
    public void asignaPropiedadesHisEje() throws SQLException, Exception{
      crsHistoricoEje.beforeFirst();
      while (crsHistoricoEje.next()){ 
        programa=crsHistoricoEje.getString("programa");
        unidad=crsHistoricoEje.getString("unidad");
        entidad=crsHistoricoEje.getString("entidad");
        ambito=crsHistoricoEje.getString("ambito");
      } 
    }    

 /*   
    protected double recorreAportaciones(HashMap arreglo){
      Double total=0D;  
      Set sKey = null;
      String st = "";
      sKey = arreglo.keySet();
      Iterator i = sKey.iterator();
      while(i.hasNext()){
        st = (String) i.next();
        total=total+ (Double) arreglo.get(st);
       // System.out.println("V: " + st + "="+arreglo.get(st));
      }
      return total;
    }

*/

/*
    protected double recorreRetenciones(HashMap arreglo){
      Double total=0D;  
      Double totalCreSal=0D;
      Set sKey = null;
      String st = "";
      sKey = arreglo.keySet();
      Iterator i = sKey.iterator();
      while(i.hasNext()){
        st = (String) i.next();
        
        if (st.equals("D192") || st.equals("D195"))
          totalCreSal=(Double) arreglo.get(st);
        else
          total=total+ (Double) arreglo.get(st);
       // System.out.println("V: " + st + "="+arreglo.get(st));
      }
      totalCAS=totalCreSal;
      return total;
    }
    */
    
    public void imprimeRetenciones(){
      Set sKey;
      String st = "";
      sKey = importeRete.keySet();
      Iterator i = sKey.iterator();
      while(i.hasNext()){
        st = (String) i.next();       
        //System.out.println("V: " + st + "="+importeRete.get(st));
      }
    }    

    protected void acumulaAportacion(String llave,Double importe, HashMap importeRec) throws SQLException, Exception{
      Double importeTem;
      Double importeAcum;
      if ((importeRec!=null) && (importeRec.containsKey(llave))){
        importeTem= (Double) importeRec.get(llave);
        importeAcum=importe+importeTem;
        importeRec.put(llave,importeAcum); 
      }
      else{
        importeRec.put(llave,importe); 
      }
    }
    
    public void calculaAportaciones() throws SQLException, Exception{
       Context context = Context.enter();
       Scriptable scope = context.initStandardObjects();
       context.getWrapFactory().setJavaPrimitiveWrap(false);
       org.mozilla.javascript.Script compiled;
       Object r = null;
       
       String partida;
       String tipoEje;
       String unidad;
       String ambito;
       String tipoDoc;
       String conceptoNomina;
       String ejercicio;
       Object resultado;
       String llave;
       
       Aportacion aportacion = new Aportacion();
       try{
         crsSentenciaAportacion=aportacion.select_rf_tc_aportaciones_todas(); 
         crsHistoricoEje.beforeFirst();
         while (crsHistoricoEje.next()){ 
           crsSentenciaAportacion.beforeFirst();
           llave="0";
           while (crsSentenciaAportacion.next()){
             partida=crsHistoricoEje.getString("partida");
             unidad=crsHistoricoEje.getString("unidad");             
             ambito=crsHistoricoEje.getString("ambito");
             conceptoNomina=String.valueOf(conceptoNomina());
             tipoEje=crsHistoricoEje.getString("tipoEje");
             tipoDoc=crsHistoricoEje.getString("tipo_Docto");
             ejercicio=crsHistoricoEje.getString("ejercicio");
             capitulo=crsHistoricoEje.getString("capitulo");

             ScriptableObject.putProperty(scope,"partida",Context.javaToJS(partida,scope));
             ScriptableObject.putProperty(scope,"unidad",Context.javaToJS(unidad,scope));
             ScriptableObject.putProperty(scope,"ambito",Context.javaToJS(ambito,scope));
             ScriptableObject.putProperty(scope,"conceptoNomina",Context.javaToJS(conceptoNomina,scope));             
             ScriptableObject.putProperty(scope,"tipoEje",Context.javaToJS(tipoEje,scope));
             ScriptableObject.putProperty(scope,"tipoDoc",Context.javaToJS(tipoDoc,scope));
             ScriptableObject.putProperty(scope,"ejercicio",Context.javaToJS(ejercicio,scope));
             compiled = context.compileString(crsSentenciaAportacion.getString("regla_contable"),"defLoaf",0,null);             
             compiled.exec(context,scope);
             resultado=ScriptableObject.getProperty(scope,"resultado"); 
//                System.out.println("Totales.calculaAportaciones."+crsSentenciaAportacion.getString("descripcion")+": "+crsSentenciaAportacion.getString("regla_contable"));
//                System.out.println("Totales.calculaAportaciones.resultado "+resultado);
             if (resultado.equals("1")){
               llave=crsSentenciaAportacion.getString("aportacion_id");
               break;
             }  
           }
           if (!llave.equals("0")){
             acumulaAportacion(crsHistoricoEje.getString("programa")+crsHistoricoEje.getString("unidad")+crsHistoricoEje.getString("ambito")+crsHistoricoEje.getString("ejercicio")+crsHistoricoEje.getString("capitulo")+llave,Double.parseDouble((crsHistoricoEje.getString("importe")== null)? "0.00":crsHistoricoEje.getString("importe")),importeAport);
           } 
         }  
       } 
       catch(Exception e){ 
          System.out.println("Ocurrio un error al accesar al metodo calculaAportaciones "+e.getMessage()); 
          throw e; 
        } //Fin catch 
        finally{ 
          if (crsSentencia!=null){
          crsSentencia.close();
          crsSentencia=null;
          }    
        }
    }
    
    public void calculaRetencionesHistorico() throws SQLException, Exception{
        if(crsHistoricoEje==null || crsHistoricoRet==null) {
            return;
        }
       Context context = Context.enter();
       Scriptable scope = context.initStandardObjects();
       context.getWrapFactory().setJavaPrimitiveWrap(false);
       org.mozilla.javascript.Script compiled;
       Object r = null;
       
       String partida;
       String tipoEje;
       String tipoDoc;
       Object resultado;
       String llave;
       boolean banRompe;
              
       Retencion retencion = new Retencion();
       Impuesto impuesto = new Impuesto();
       try{
         /* ** Obtiene Total Bruto por Unidad y AmbitoEstado */

          HashMap hashMapHisEjeBruto = new HashMap();  

          Set sKey;
          String st;
          Iterator i;
          String lsPrograma="";
          String lsUnidad="";
          String lsAmbito="";      
          String ejercicio;
          crsHistoricoEje.beforeFirst();      
          while (crsHistoricoEje.next()){
            acumulaAportacion(crsHistoricoEje.getString("programa")+crsHistoricoEje.getString("unidad")+crsHistoricoEje.getString("ambito"),Double.parseDouble((crsHistoricoEje.getString("importe")== null)? "0.00":crsHistoricoEje.getString("importe")),hashMapHisEjeBruto);         
          }  
          sKey = hashMapHisEjeBruto.keySet();  
          i = sKey.iterator();
          while(i.hasNext()){
            st = (String) i.next();
            lsPrograma=st.substring(0,4);
            lsUnidad=st.substring(4,8);
            lsAmbito=st.substring(8);
            totalBruto=(Double) hashMapHisEjeBruto.get(st);
          }
         crsSentenciaRetencion=retencion.select_rf_tc_retenciones_todas(); 
         crsSentenciaImpuesto=impuesto.select_rf_tc_partidas_impuestos_todas();
         crsHistoricoRet.beforeFirst();
         while (crsHistoricoRet.next()){ 
           if (crsHistoricoRet.getString("CVERET").substring(0,2).equals("DA")){
             banDeduccionDA=true;
           }
           crsSentenciaRetencion.beforeFirst();
           llave="0";         
           while (crsSentenciaRetencion.next()){
            ejercicio=crsHistoricoRet.getString("ejercicio");
             if (crsSentenciaRetencion.getString("par_impuesto_id")==null){
               if (crsHistoricoRet.getString("cveRet").equals(crsSentenciaRetencion.getString("clave_ret_ded"))){
                  llave=crsSentenciaRetencion.getString("clave_ret_ded")+crsSentenciaRetencion.getString("retencion_id");
                  break;
               }   
               else{
                continue;   
               }
             }
             else{
               if (crsHistoricoRet.getString("cveRet").equals(crsSentenciaRetencion.getString("clave_ret_ded"))){            
               crsSentenciaImpuesto.beforeFirst();
               banRompe=false;
               while (crsSentenciaImpuesto.next()){
                 if (crsSentenciaImpuesto.getString("clave_ret").equals(crsSentenciaRetencion.getString("retencion_id"))){
                   partida=crsHistoricoRet.getString("partida");
                   tipoEje=crsHistoricoRet.getString("tipoEje");
                   ScriptableObject.putProperty(scope,"partida",Context.javaToJS(partida,scope)); 
                   ScriptableObject.putProperty(scope,"tipoeje",Context.javaToJS(tipoEje,scope)); 
                   ScriptableObject.putProperty(scope,"totalBruto",Context.javaToJS(totalBruto,scope));
                   ScriptableObject.putProperty(scope,"tipoDoc",Context.javaToJS(crsHistoricoRet.getString("cxlc").substring(6,9),scope));
                   ScriptableObject.putProperty(scope,"ejercicio",Context.javaToJS(ejercicio,scope));
                   compiled = context.compileString(crsSentenciaImpuesto.getString("regla_contable"),"defLoaf",0,null);             
                   compiled.exec(context,scope);
                   resultado=ScriptableObject.getProperty(scope,"resultado");
   //                System.out.println("Totales.calculaRetencionesHistorico."+crsSentenciaImpuesto.getString("nombrevariable")+": "+crsSentenciaImpuesto.getString("regla_contable"));
   //                System.out.println("Totales.calculaRetencionesHistorico.resultado "+resultado);
                   if (resultado.equals("1")){
                    //System.out.println("Totales.calculaRetencionesHistorico."+crsSentenciaImpuesto.getString("regla_contable"));
                    llave=crsSentenciaRetencion.getString("clave_ret_ded")+crsSentenciaRetencion.getString("retencion_id");
                     banRompe=true;
                   break;
                   }  
                 }
               }
               if (banRompe==true){
                break;   
               }
             }  
             } //fin else 
           }
           if (!llave.equals("0")){
          //   if (crsHistoricoRet.getString("unidad").equals("0124") && crsHistoricoRet.getString("ambito").equals("0092")){
              //if (crsHistoricoRet.getString("unidad").equals("0124")){
             acumulaAportacion(crsHistoricoRet.getString("programa")+crsHistoricoRet.getString("unidad")+crsHistoricoRet.getString("ambito")+crsHistoricoRet.getString("ejercicio")+llave,Double.parseDouble((crsHistoricoRet.getString("importe")== null)? "0.00":crsHistoricoRet.getString("importe")),importeRete);
             acumulaAportacion(crsHistoricoRet.getString("programa")+crsHistoricoRet.getString("unidad")+crsHistoricoRet.getString("ambito")+crsHistoricoRet.getString("tiga")+llave,Double.parseDouble((crsHistoricoRet.getString("importe")== null)? "0.00":crsHistoricoRet.getString("importe")),importeReteTIGA);
             acumulaAportacion(crsHistoricoRet.getString("programa")+crsHistoricoRet.getString("unidad")+crsHistoricoRet.getString("ambito")+crsHistoricoRet.getString("partida").substring(0,2)+llave,Double.parseDouble((crsHistoricoRet.getString("importe")== null)? "0.00":crsHistoricoRet.getString("importe")),importeReteConPartida);
             acumulaAportacion(crsHistoricoRet.getString("programa")+crsHistoricoRet.getString("unidad")+crsHistoricoRet.getString("ambito")+crsHistoricoRet.getString("capitulo")+llave,Double.parseDouble((crsHistoricoRet.getString("importe")== null)? "0.00":crsHistoricoRet.getString("importe")),importeReteCapitulo);  
             acumulaAportacion(crsHistoricoRet.getString("programa")+crsHistoricoRet.getString("unidad")+crsHistoricoRet.getString("ambito")+crsHistoricoRet.getString("partida").substring(0,3)+llave,Double.parseDouble((crsHistoricoRet.getString("importe")== null)? "0.00":crsHistoricoRet.getString("importe")),importeRetePartidaGen);
            // }
           } 
         }  
       } 
       catch(Exception e){ 
          System.out.println("Ocurrio un error al accesar al metodo calculaRetencionesHistorico "+e.getMessage()); 
          throw e; 
        } //Fin catch 
        finally{ 
          if (crsSentencia!=null){
          crsSentencia.close();
          crsSentencia=null;
          }    
        }
    }
  
    protected int conceptoNomina() throws SQLException, Exception{       
       int conceptoNomina=0;
       try{
         crsCuentaPorLiquidar.beforeFirst();
         while (crsCuentaPorLiquidar.next()){ 
             if  (crsCuentaPorLiquidar.getString("concepto").toString().indexOf("NOMINA")!=-1||crsCuentaPorLiquidar.getString("concepto").toString().indexOf("NÓMINA")!=-1 ) {
                  conceptoNomina=1;
             }
         }
       }  
       catch(Exception e){ 
          System.out.println("Ocurrio un error al accesar al metodo conceptoNomina "+e.getMessage()); 
          throw e; 
        } //Fin catch 
        finally{ 
        }
        return conceptoNomina;
    }

    protected void calculaRetencionesFactura(Connection conexionContabilidad) throws SQLException, Exception{
       String llave;         
       try{
         crsFactura.beforeFirst();
         while (crsFactura.next()){ 
           llave=crsFactura.getString("factura");
           acumulaAportacion(llave,Double.parseDouble((crsFactura.getString("importe")== null)? "0.00":crsFactura.getString("importe")),importe);
           acumulaAportacion(llave,Double.parseDouble((crsFactura.getString("iva")== null)? "0.00":crsFactura.getString("iva")),importeIVA);           
         } 
       }  
       catch(Exception e){ 
          System.out.println("Ocurrio un error al accesar al metodo calculaRetencionesFactura "+e.getMessage()); 
          throw e; 
        } //Fin catch 
        finally{ 
          if (crsSentencia!=null){
          crsSentencia.close();
          crsSentencia=null;
          }    
        }
    }

    protected void calculaTotalNeto()throws SQLException, Exception{
        crsCuentaPorLiquidar.beforeFirst();
        crsCuentaPorLiquidar.next();
        if (crsCuentaPorLiquidar.getString("tipoeje").equals("13") || crsCuentaPorLiquidar.getString("tipoeje").equals("26") || crsCuentaPorLiquidar.getString("tipoeje").equals("14") || crsCuentaPorLiquidar.getString("tipoeje").equals("27") || crsCuentaPorLiquidar.getString("tipoeje").equals("28")) {
//          totalNeto= totalBruto + totalCAS - ((totalRetenciones + totalISR) - totalAportaciones);
            totalNeto= totalBruto + totalCAS + ((totalRetenciones + totalISR));
        }
        else  
            if ((crsCuentaPorLiquidar.getString("tipoeje").equals("25") || crsCuentaPorLiquidar.getString("tipoeje").equals("26")) && tiga.equals("0001")) {
            totalNeto= totalBruto + totalCAS + ((totalRetenciones + totalISR));
        }
            else {
              //  totalNeto= totalBruto - totalCAS - ((totalRetenciones + totalISR) - totalAportaciones);
            totalNeto= totalBruto - totalCAS - ((totalRetenciones + totalISR));
        }
        totalNeto=SiafmCostosProduccion.decimales(totalNeto);
        totalTodasRetenciones=totalCAS+totalISR+totalRetenciones;
        //System.out.println("bruto: " +totalBruto+", Retenciones: " + totalRetenciones+", aportaciones: "+totalAportaciones+", totalCAS: "+totalCAS+", totalNeto: "+totalNeto + ", totalISR: "+totalISR+", Total todas las Retenciones:"+totalTodasRetenciones);
        if (crsCuentaPorLiquidar.getString("tipoeje").equals("22") && tipoDocto.equals("OCG") && totalTodasRetenciones == totalAportaciones) {
            totalTodasRetenciones=0.0D; //Para que no pinte el complemento de Impuestos al OCG
        }
        if (totalTodasRetenciones!=0.0) {
            bantotTodasRetenciones=true;
        }    
        if  (totalNeto!=0){
            totalNetoCero=true;
        }       
        if  (totalBruto!=0){
            totalBrutoCero=true;
        }
    }
   
    public void calcutaTotales(Connection conexionContabilidad, Connection conexionSistema, String evento, String parametros) throws SQLException, Exception{
  /*    leeModeloMipf(conexionContabilidad, conexionSistema, evento, parametros);
      totalBruto=calculaTotalBrutoHisEje();
      if (evento.equals("100")){                         //Sirve para determinar cuando procesar aportaciones y retenciones. Por ejemplo fondo rotatorio no aplica ninguna al parecer.
       calculaAportaciones(conexionContabilidad);
       totalAportaciones=recorreAportaciones(importeAport);
      }
      calculaRetencionesHistorico(conexionContabilidad);
      calculaRetencionesFactura(conexionContabilidad);
      totalRetenciones=recorreRetenciones(importeRete);
      calculaTotalNeto();
      recorreRetenciones(importeRete);
*/
    }
    
     protected void factoriza(){
        if (factor==-1){
           totalBruto=totalBruto*factor;
           totalISR=totalISR*factor;
           totalCAS=totalCAS*factor;
           totalNeto=totalNeto*factor;
           totalTodasRetenciones=totalTodasRetenciones*factor;
        }
     }

    protected double aplicaReglaARE(double importeNeto){
      double totalNetoTem;
      totalNetoTem=totalNeto;
      if (banAre==1) {
    //    if (totalNeto<0.0) // se quito esta condicion por especificacion de Salvador Reyes, para los casos de los importes netos que al momento del calculo dan negativo
            totalNetoTem=totalNeto*-1.0;
        }
       return totalNetoTem;    
      }

    protected double aplicaReglaARE_ant(double importeBruto){
      double totalBrutoTem;
      totalBrutoTem=totalBruto;
      if (banAre_ant==1) {
            totalBrutoTem=totalBruto*-1.0;
        }
       return totalBrutoTem;    
      }      
    protected double aplicaFactorNetoNeg(double importeNeto){
      double totalNetoTem;
      totalNetoTem=importeNeto;
      if (factorNetoNeg==1) {
            if (importeNeto<0.0) {
              totalNetoTem=importeNeto*-1.0;
          }
        }
       return totalNetoTem;    
      }      
     
      public HashMap creaHashMap(Connection conexionContabilidad, int numForma) throws SQLException, Exception{
          Context context = Context.enter();
          Scriptable scope = context.initStandardObjects();
          context.getWrapFactory().setJavaPrimitiveWrap(false);
          org.mozilla.javascript.Script compiled;
          Object r = null;

          Object resultado;
          String llave;
          String valor;
          boolean bandera;
          
          HashMap variables=null; 
          variables = new HashMap();
          Funcion funcion = new Funcion();
          
          try{
            crsSentenciaFuncion=funcion.select_rf_tc_FuncionesPorForma(numForma); 
            crsSentenciaFuncion.beforeFirst();
            factoriza();
            while (crsSentenciaFuncion.next()){
              bandera=false;
              if (crsSentenciaFuncion.getString("regla_contable").equals("SIN")){ 
                bandera=true;
              }
              else{
                ScriptableObject.putProperty(scope,"anticipo",Context.javaToJS(anticipo,scope));
                ScriptableObject.putProperty(scope,"capitulo",Context.javaToJS(capitulo,scope));
                ScriptableObject.putProperty(scope,"ambitoCuenta",Context.javaToJS(ambitoCuenta,scope));
                ScriptableObject.putProperty(scope,"tiga",Context.javaToJS(tiga,scope));          
                ScriptableObject.putProperty(scope,"totalNeto",Context.javaToJS(totalNeto,scope));               
                ScriptableObject.putProperty(scope,"programa",Context.javaToJS(programa,scope));               
                ScriptableObject.putProperty(scope,"tipo_afectacion",Context.javaToJS(tipo_afectacion,scope));  
                ScriptableObject.putProperty(scope,"devengado",Context.javaToJS(banDevengado,scope));                    
                ScriptableObject.putProperty(scope,"id_tipo_bene",Context.javaToJS(id_tipo_beneficiario,scope));  
                ScriptableObject.putProperty(scope,"cveret",Context.javaToJS(cveret,scope)); 
                ScriptableObject.putProperty(scope,"partidaGen",Context.javaToJS(partidaGen,scope)); 
                ScriptableObject.putProperty(scope,"conPartida",Context.javaToJS(conPartida,scope));
                ScriptableObject.putProperty(scope,"partida",Context.javaToJS(partida,scope));
/*              System.out.println("Totales.creaHashMap.anticipo: "+anticipo);
              System.out.println("Totales.creaHashMap.capitulo: "+capitulo);
              System.out.println("Totales.creaHashMap.ambitoCuenta: "+ambitoCuenta);
              System.out.println("Totales.creaHashMap.tiga: "+tiga);
              System.out.println("Totales.creaHashMap.totalNeto: "+totalNeto);
              System.out.println("Totales.creaHashMap.programa: "+programa);
              System.out.println("Totales.creaHashMap.tipo_afectacion: "+tipo_afectacion);
              System.out.println("Totales.creaHashMap.devengado: "+banDevengado);
              System.out.println("Totales.creaHashMap.id_tipo_bene: "+id_tipo_beneficiario);
              System.out.println("Totales.creaHashMap.cveret: "+cveret);
              System.out.println("Totales.creaHashMap.partidaGen: "+partidaGen);
              System.out.println("Totales.creaHashMap.conPartida: "+conPartida);
*/                try 
                {
//                    System.out.println("Totales.creaHashMap "+crsSentenciaFuncion.getString("nombrevariable")+" "+crsSentenciaFuncion.getString("regla_contable"));
                    compiled = context.compileString(crsSentenciaFuncion.getString("regla_contable"),"defLoaf",0,null);
                    compiled.exec(context,scope);
                    resultado=ScriptableObject.getProperty(scope,"resultado"); 
//                    System.out.println("Resultado = "+resultado);
                }catch(Exception e)
                {
                    System.out.println("Totales.creaHashMap().crsSentenciaFuncion.getString(\"regla_contable\") "+crsSentenciaFuncion.getString("regla_contable"));
                    System.out.println("Ocurrio un error al accesar al metodo creaHashMap "+e.getMessage()); 
                    resultado = "0";
                }
//                System.out.println("Totales.creaHashMap "+crsSentenciaFuncion.getString("nombrevariable")+": "+crsSentenciaFuncion.getString("regla_contable"));
//                System.out.println("Resultado : "+resultado);
                if (resultado.equals("1")){
                  bandera=true;
                } 
              }
//              System.out.println("-Totales.creaHashMap "+crsSentenciaFuncion.getString("nombrevariable"));
              if (bandera==true){
//                System.out.println("Totales.creaHashMap "+crsSentenciaFuncion.getString("nombrevariable"));
                llave=crsSentenciaFuncion.getString("nombreVariable");
                valor="0.0";
                String mayor51 = partida==null||partida.length()<5?"0":"51"+partida.substring(1,2);
                String mayor52 = partida==null||partida.length()<5?"0":"52"+partida.substring(1,2);
                String nivel2 = partida==null||partida.length()<5?"0":partida.substring(3,1);
                String nivel6 = partida==null||partida.length()<5?"0":partida.equals("11301")?"0003":"00"+partida.substring(4,2);
                String nivel7 = partida==null||partida.length()<5?"0":partida.equals("11301")?"0001":"0000";
                switch (crsSentenciaFuncion.getInt("idVariable")){
                  case 1: valor=Double.toString(aplicaReglaARE_ant(getTotalBruto())); break;
                  case 3: valor=Double.toString(totalTodasRetenciones); break;
                  case 4: valor=Double.toString(aplicaFactorNetoNeg(aplicaReglaARE(getTotalNeto()))); break;
                  case 5: valor=Double.toString(getTotalAntesIVA()); break;
                  case 6: valor=Double.toString(getTotalIVA()); break;
                  case 9: valor=getUnidad(); break;
                  case 10: valor=getEntidad(); break;
                  case 11: valor=getAmbito(); break;
                  case 12: valor=tiga; break;
                  case 13: valor=referencia;break;
                  case 14: valor = getConcepto();break;
                  case 15: valor=getClaveProveedor();break;
                  case 16: valor = getCapitulo();break;
                  case 18: valor = Double.toString(getTotalCAS());break;
                  case 19: valor = null;break;
                  case 21: valor = getClaveConcepto();break;                  
                  case 22: valor = tigaCapitulo;break;      
                  case 23: valor = tipoCuenta;break;  
                  case 24: valor = ejercicio;break;  
                  case 25: valor = programa;break;  
                  case 26: valor = conPartida==null?null:conPartida.substring(1);break;
                  case 27: valor = partidaGen==null?null:partidaGen.substring(2);break;
                  case 28: valor = getCapitulo()==null?null:getCapitulo().substring(0,1);break; 
                  case 29: valor = entidadAmbitoCuentaBancaria;break;  
                  case 30: valor = unidadCuentaBancaria;break;       
                  case 31: valor = cveret;break;
                  case 32: valor = mayor51;break;
                  case 33: valor = mayor52;break;
                  case 34: valor = nivel2;break;
                  case 35: valor = nivel6;break;
                  case 36: valor = nivel7;break;
                }
                variables.put(llave,valor);  
              //System.out.println("Totales.creaHashMap "+llave+" = "+valor);
              }
            }  
          } 
          catch(Exception e){ 
             System.out.println("Ocurrio un error al accesar al metodo creaHashMap "+e.getMessage()); 
             throw e; 
           } //Fin catch 
           finally{    
           }    
        return variables;
      }

    public HashMap creaHashMapVariable(Connection conexionContabilidad, int numForma, String pTipo) throws SQLException, Exception{
        Context context = Context.enter();
        Scriptable scope = context.initStandardObjects();
        context.getWrapFactory().setJavaPrimitiveWrap(false);
        org.mozilla.javascript.Script compiled;
        Object r = null;

        Object resultado;
        String llave;
        String valor=null;
        boolean bandera;
        
        HashMap variables=null; 
        variables = new HashMap();
        Funcion funcion = new Funcion();
        
        try{
          //System.out.println("Totales.creaHashMapVariable.pTipo "+pTipo);
          crsSentenciaFuncion=funcion.select_rf_tc_FuncionesPorFormaVariable(numForma, pTipo); 
          crsSentenciaFuncion.beforeFirst();
          factoriza();
          while (crsSentenciaFuncion.next()){
            bandera=false;
            if (crsSentenciaFuncion.getString("regla_contable").equals("SIN")){ 
              bandera=true;
            }
            else{          
              ScriptableObject.putProperty(scope,"programa",Context.javaToJS(programa,scope));                                
              ScriptableObject.putProperty(scope,"anticipo",Context.javaToJS(anticipo,scope));
              ScriptableObject.putProperty(scope,"totalNeto",Context.javaToJS(totalNeto,scope));               
              ScriptableObject.putProperty(scope,"devengado",Context.javaToJS(banDevengado,scope));  
              ScriptableObject.putProperty(scope,"id_tipo_bene",Context.javaToJS(id_tipo_beneficiario,scope));    
              ScriptableObject.putProperty(scope,"cveret",Context.javaToJS(cveret,scope));  
/*              System.out.println("Totales.creaHashMapVariable.programa: "+programa);
              System.out.println("Totales.creaHashMapVariable.anticipo: "+anticipo);
              System.out.println("Totales.creaHashMapVariable.totalNeto: "+totalNeto);
              System.out.println("Totales.creaHashMapVariable.devengado: "+banDevengado);
              System.out.println("Totales.creaHashMapVariable.id_tipo_bene: "+id_tipo_beneficiario);
              System.out.println("Totales.creaHashMapVariable.cveret: "+cveret);
*/              if (pTipo.equals("O")||pTipo.equals("N")) {
                    ScriptableObject.putProperty(scope,"conPartida",Context.javaToJS(conPartida,scope));
//              System.out.println("Totales.creaHashMapVariable.conPartida: "+conPartida);
                }
              else  if (pTipo.equals("P")) {
                    ScriptableObject.putProperty(scope,"partidaGen",Context.javaToJS(partidaGen,scope));
//              System.out.println("Totales.creaHashMapVariable.partidaGen: "+partidaGen);
                }
              else {
                    ScriptableObject.putProperty(scope,"capitulo",Context.javaToJS(capitulo,scope));
//              System.out.println("Totales.creaHashMapVariable.capitulo: "+capitulo);
                }                          
              compiled = context.compileString(crsSentenciaFuncion.getString("regla_contable"),"defLoaf",0,null);             
              compiled.exec(context,scope);
              resultado=ScriptableObject.getProperty(scope,"resultado"); 
//              System.out.println("Totales.creaHashMapVariable."+crsSentenciaFuncion.getString("nombrevariable")+": "+crsSentenciaFuncion.getString("regla_contable"));
//              System.out.println("Resultado = "+resultado);
              if (resultado.equals("1")){
                bandera=true;
              }  
            }
//            System.out.println("--Totales.creaHashMapVariable."+crsSentenciaFuncion.getString("nombrevariable"));
            if (bandera==true){
              //System.out.println("Totales.creaHashMapVariable."+crsSentenciaFuncion.getString("nombrevariable"));
              llave=crsSentenciaFuncion.getString("nombreVariable");
              switch (crsSentenciaFuncion.getInt("idVariable")){
                case 1: valor=Double.toString(aplicaReglaARE_ant(getTotalBruto())); break;
                case 3: valor=Double.toString(totalTodasRetenciones*-1.00); break;
                case 4: valor=Double.toString(aplicaFactorNetoNeg(aplicaReglaARE(getTotalNeto()))); break;
                case 5: valor=Double.toString(getTotalAntesIVA()); break;
                case 6: valor=Double.toString(getTotalIVA()); break;                
                case 9: valor=getUnidad(); break;
                case 10: valor=getEntidad(); break;
                case 11: valor=getAmbito(); break;
                case 12: valor=tiga; break;
                case 13: valor=referencia;break;
                case 14: valor = getConcepto();break;
                case 15: valor=getClaveProveedor();break;
                case 16: valor = getCapitulo();break;
                case 18: valor = Double.toString(getTotalCAS());break;
                case 19: valor = null;break;
                case 21: valor = getClaveConcepto();break;
                case 22: valor = tigaCapitulo;break;                                                    
                case 23: valor = tipoCuenta;break;   
                case 24: valor = ejercicio;break;  
                case 25: valor = programa;break; 
                case 26: valor = conPartida.substring(1);break;
                case 27: valor = partidaGen.substring(2);break;
                case 28: valor = getCapitulo().substring(0,1);break; 
                case 29: valor = entidadAmbitoCuentaBancaria;break; 
                case 30: valor = unidadCuentaBancaria;break;                      
                case 31: valor = cveret;break;
              }  
              variables.put(llave,valor);  
              //System.out.println("Totales.creaHashMapVariable "+llave+" = "+valor);
            }
          }  
        } 
        catch(Exception e){ 
           System.out.println("Ocurrio un error al accesar al metodo creaHashMapVariable "+e.getMessage()); 
           throw e; 
         } //Fin catch 
         finally{    
         }    
      return variables;
    }


    public boolean verificaProgramaBancoMundial() throws SQLException, Exception{ 
        boolean bancoMundial=true;
        try{
            crsHistoricoEje.beforeFirst();      
            while (crsHistoricoEje.next()){
              if (crsHistoricoEje.getString("programa").equals("0001")){
                  bancoMundial=false;
                  break;
              }
            }              
        } 
        catch(Exception e){ 
           System.out.println("Ocurrio un error al accesar al metodo verificaProgramaBancoMundial "+e.getMessage()); 
           throw e; 
         } //Fin catch 
         finally{    
         }    
        return bancoMundial;
    }
}
