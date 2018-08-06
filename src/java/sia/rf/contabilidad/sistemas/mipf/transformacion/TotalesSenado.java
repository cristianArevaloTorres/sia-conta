package sia.rf.contabilidad.sistemas.mipf.transformacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import sia.db.dao.DaoFactory;
import sia.rf.contabilidad.registroContableEvento.Cadena;
import sia.rf.contabilidad.registroContableNuevo.bcProveedores;
import sia.rf.contabilidad.sistemas.mipf.modDescarga.Aportacion;
import sia.rf.contabilidad.sistemas.mipf.modDescarga.Funcion;
import sia.rf.contabilidad.sistemas.mipf.modDescarga.Impuesto;
import sia.rf.contabilidad.sistemas.mipf.modDescarga.Retencion;
import sia.rf.contabilidad.sistemas.mipf.modDescarga.SentenciasMipf;
import sia.ws.publicar.contabilidad.Registro;
import sun.jdbc.rowset.CachedRowSet;

public class TotalesSenado extends Totales {

    private CachedRowSet crsFormasActivos = null;
    private CachedRowSet crsFormasCxpCp = null;
    private CachedRowSet crsFormasDeducciones = null;
    private CachedRowSet crsFormasGastos = null;
    private CachedRowSet crsFormasPresupuestales = null;
    private CachedRowSet crsGasto = null;
    private CachedRowSet crsCxpCp = null;
    private CachedRowSet crsRetenciones = null;
    private CachedRowSet crsDivisas = null;
    private CachedRowSet crsAportaciones = null;
    private CachedRowSet crsPresupDevengado = null;
    private CachedRowSet crsPresupComprometido = null;
    private CachedRowSet crsPresupComprometido2 = null;
    private CachedRowSet crsPresupPorEjercer = null;
    private CachedRowSet crsPresupEjercido = null;
    private CachedRowSet crsPresupPagado = null;
    private CachedRowSet crsActivos = null;
    private CachedRowSet crsDeudores = null;
    private CachedRowSet crsClientes = null;

    public TotalesSenado() {
        super();
    }

    @Override
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
                    //System.out.println("Totales.leeModeloMipf.sentenciaSustituida("+crsSentencia.getString("descripcion") +")2 "+sentenciaSustituida);
                    crsFactura = factura.select_rf_tr_factura_bd(conexionSistema, sentenciaSustituida);
                } else if (crsSentencia.getString("tipo").equals("3")) { //Historico Eje
                    //System.out.println("Totales.leeModeloMipf.sentenciaSustituida("+crsSentencia.getString("descripcion") +")3 "+sentenciaSustituida);
                    crsHistoricoEje = historicoEje.select_rf_tr_histeje_bd(conexionSistema, sentenciaSustituida);
                } else if (crsSentencia.getString("tipo").equals("4")) { // Historico Ret
                    //System.out.println("Totales.leeModeloMipf.sentenciaSustituida("+crsSentencia.getString("descripcion") +")4 "+sentenciaSustituida);
                    crsHistoricoRet = historicoRet.select_rf_tr_histret_bd(conexionSistema, sentenciaSustituida);
                } else if (crsSentencia.getString("tipo").equals("5")) { // Gasto
                    //System.out.println("Totales.leeModeloMipf.sentenciaSustituida(" + crsSentencia.getString("descripcion") + ")5 " + sentenciaSustituida);
                    crsGasto = historicoRet.select_rf_tr_histret_bd(conexionSistema, sentenciaSustituida);
                } else if (crsSentencia.getString("tipo").equals("6")) { // CxpCp
                    //System.out.println("Totales.leeModeloMipf.sentenciaSustituida(" + crsSentencia.getString("descripcion") + ")6 " + sentenciaSustituida);
                    crsCxpCp = historicoRet.select_rf_tr_histret_bd(conexionSistema, sentenciaSustituida);
                } else if (crsSentencia.getString("tipo").equals("7")) { // Retenciones
                    //System.out.println("Totales.leeModeloMipf.sentenciaSustituida(" + crsSentencia.getString("descripcion") + ")7 " + sentenciaSustituida);
                    crsRetenciones = historicoRet.select_rf_tr_histret_bd(conexionSistema, sentenciaSustituida);
                } else if (crsSentencia.getString("tipo").equals("8")) { // Presup. Devengado
                    //System.out.println("Totales.leeModeloMipf.sentenciaSustituida(" + crsSentencia.getString("descripcion") + ")8 " + sentenciaSustituida);
                    crsPresupDevengado = historicoRet.select_rf_tr_histret_bd(conexionSistema, sentenciaSustituida);
                } else if (crsSentencia.getString("tipo").equals("9")) { // Presup. Comprometido
                    //System.out.println("Totales.leeModeloMipf.sentenciaSustituida(" + crsSentencia.getString("descripcion") + ")9 " + sentenciaSustituida);
                    crsPresupComprometido = historicoRet.select_rf_tr_histret_bd(conexionSistema, sentenciaSustituida);
                } else if (crsSentencia.getString("tipo").equals("10")) { // Presup. Comprometido 2
                    //System.out.println("Totales.leeModeloMipf.sentenciaSustituida(" + crsSentencia.getString("descripcion") + ")10 " + sentenciaSustituida);
                    crsPresupComprometido2 = historicoRet.select_rf_tr_histret_bd(conexionSistema, sentenciaSustituida);
                } else if (crsSentencia.getString("tipo").equals("11")) { // Presup. Por Ejercer
                    //System.out.println("Totales.leeModeloMipf.sentenciaSustituida(" + crsSentencia.getString("descripcion") + ")11 " + sentenciaSustituida);
                    crsPresupPorEjercer = historicoRet.select_rf_tr_histret_bd(conexionSistema, sentenciaSustituida);
                } else if (crsSentencia.getString("tipo").equals("12")) { // Presup. ejercido
                    //System.out.println("Totales.leeModeloMipf.sentenciaSustituida(" + crsSentencia.getString("descripcion") + ")12 " + sentenciaSustituida);
                    crsPresupEjercido = historicoRet.select_rf_tr_histret_bd(conexionSistema, sentenciaSustituida);
                } else if (crsSentencia.getString("tipo").equals("13")) { // Activos
                    //System.out.println("Totales.leeModeloMipf.sentenciaSustituida(" + crsSentencia.getString("descripcion") + ")13 " + sentenciaSustituida);
                    crsActivos = historicoRet.select_rf_tr_histret_bd(conexionSistema, sentenciaSustituida);
                } else if (crsSentencia.getString("tipo").equals("14")) { // Presup. pagado
                    //System.out.println("Totales.leeModeloMipf.sentenciaSustituida(" + crsSentencia.getString("descripcion") + ")14 " + sentenciaSustituida);
                    crsPresupPagado = historicoRet.select_rf_tr_histret_bd(conexionSistema, sentenciaSustituida);
                } else if (crsSentencia.getString("tipo").equals("15")) { // Clientes
                    //System.out.println("Totales.leeModeloMipf.sentenciaSustituida("+crsSentencia.getString("descripcion") +")15 "+sentenciaSustituida);
                    crsClientes = historicoRet.select_rf_tr_histret_bd(conexionSistema, sentenciaSustituida);
                } else if (crsSentencia.getString("tipo").equals("16")) { // Deudores
                    //System.out.println("Totales.leeModeloMipf.sentenciaSustituida(" + crsSentencia.getString("descripcion") + ")16 " + sentenciaSustituida);
                    crsDeudores = historicoRet.select_rf_tr_histret_bd(conexionSistema, sentenciaSustituida);
                } else if (crsSentencia.getString("tipo").equals("17")) { // Aportaciones
                    //System.out.println("Totales.leeModeloMipf.sentenciaSustituida(" + crsSentencia.getString("descripcion") + ")17 " + sentenciaSustituida);
                    crsAportaciones = historicoRet.select_rf_tr_histret_bd(conexionSistema, sentenciaSustituida);
                } else if (crsSentencia.getString("tipo").equals("18")) { // Divisas
                    //System.out.println("Totales.leeModeloMipf.sentenciaSustituida(" + crsSentencia.getString("descripcion") + ")18 " + sentenciaSustituida);
                    crsDivisas = historicoRet.select_rf_tr_histret_bd(conexionSistema, sentenciaSustituida);
                }
            }
        } catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo leeModelo " + e.getMessage());
            throw e;
        } //Fin catch 
        finally {
            if (crsSentencia != null) {
                crsSentencia.close();
                crsSentencia = null;
            }
        }
    }

    @Override
    public void verificaProveedor(Connection conexionContabilidad, Registro registro, String fecha, String pCatCuenta, String pCtaMayor, String pSubCtaMayor) throws SQLException, Exception {
        if (banProv == 1 || tipo_afectacion.equals("C")) {
            crsFactura.beforeFirst();
            while (crsFactura.next()) {
                unidad = crsFactura.getString("unidad");
                ambito = crsFactura.getString("ambito");
                programa = crsFactura.getString("programa");

                rfc = crsFactura.getString("cveprov");
                descripcionRFC = crsFactura.getString("nomprov");
                claveConcepto = crsFactura.getString("claveConcepto");
                String resultado = "";
                bcProveedores bcProveedor = new bcProveedores();
                claveProveedor = bcProveedor.select_rf_tc_proveedores_rfc(conexionContabilidad, rfc);
                if (claveProveedor.equals("0")) {
                    claveProveedor = bcProveedor.select_SEQ_rf_tc_proveedores(conexionContabilidad);
                    bcProveedor.setId_contable(claveProveedor);
                    bcProveedor.setRfc(rfc);
                    bcProveedor.setNombre_razon_social(descripcionRFC);
                    bcProveedor.insert_rf_tc_proveedores(conexionContabilidad);
                }
                if (banProv == 1) {
                    resultado = registro.AgregaCuentaContable(pCtaMayor, pSubCtaMayor, programa, unidad, ambito, claveProveedor, "0000", "0000", "0000", fecha, descripcionRFC, "6", pCatCuenta);
                }
            }
        }
    }

    @Override
    public String calculaTotalBrutoHisEjeCapitulo(Connection conexionContabilidad, int numForma, String pTipo) throws SQLException, Exception {
        HashMap hashMap;
        HashMap hashMapHisEjeCap = new HashMap();
        StringBuilder cadena = new StringBuilder("");
        Set sKey;
        String st;
        Iterator i;
        String lsPrograma;
        String lsUnidad;
        String lsAmbito;
        String lsCapitulo;
        String lsTiga;

        crsHistoricoEje.beforeFirst();
        while (crsHistoricoEje.next()) {
            try {
                cveret = crsHistoricoEje.getString("cveret");
                cveret = cveret == null ? " " : cveret;
            } catch (Exception e) {
                cveret = " ";
            }
            acumulaAportacion(crsHistoricoEje.getString("programa") + crsHistoricoEje.getString("unidad") + crsHistoricoEje.getString("ambito") + crsHistoricoEje.getString("capitulo") + crsHistoricoEje.getString("tiga") + cveret, Double.parseDouble((crsHistoricoEje.getString("importe") == null) ? "0.00" : crsHistoricoEje.getString("importe")), hashMapHisEjeCap);
            tipoDocto = crsHistoricoEje.getString("Tipo_docto");
        }
        sKey = hashMapHisEjeCap.keySet();
        i = sKey.iterator();
        while (i.hasNext()) {
            st = (String) i.next();
            lsPrograma = st.substring(0, 4);
            lsUnidad = st.substring(4, 8);
            lsAmbito = st.substring(8, 12);
            lsCapitulo = st.substring(12, 16);
            lsTiga = st.substring(16, 20);
            cveret = st.substring(20);
//        System.out.println("Totales.calculaTotalBrutoHisEjeCapitulo lsCapitulo = "+lsCapitulo);
//        System.out.println("Totales.calculaTotalBrutoHisEjeCapitulo cveret = "+cveret);
            totalBruto = (Double) hashMapHisEjeCap.get(st);
//        System.out.println("Totales.calculaTotalBrutoHisEjeCapitulo totalBruto = "+totalBruto);
            calculaTotalRetencionesCapitulo(lsPrograma, lsUnidad, lsAmbito, lsCapitulo, importeReteCapitulo);
            programa = lsPrograma;
            unidad = lsUnidad;
            ambito = lsAmbito;
            capitulo = lsCapitulo;
            tiga = lsTiga;
            calculaTotalNeto();
            // se quita el capitulo 6000, ya que este no necesita ahora ir a buscar el dato del proveedor, salvador  09-08-2011
            // if ((anticipo.equals("0")) &&(capitulo.equals("5000") || capitulo.equals("6000")))
            if ((anticipo.equals("0")) && (capitulo.equals("5000"))) {
                setBanProv(1);
            }
            hashMap = creaHashMapVariable(conexionContabilidad, numForma, pTipo);
            cadena.append(Cadena.construyeCadena(hashMap));
            cadena.append("~");
            hashMap.clear();
        }
//      System.out.println("Totales.calculaTotalBrutoHisEjeCapitulo.cadena "+cadena);
        return cadena.toString();
    }

    @Override
    public String calculaTotalBrutoHisEjeConPartida(Connection conexionContabilidad, int numForma, String pTipo) throws SQLException, Exception {
        HashMap hashMap;
        HashMap hashMapHisEjeConPartida = new HashMap();
        StringBuilder cadena = new StringBuilder("");
        Set sKey;
        String st;
        Iterator i;
        String lsPrograma;
        String lsUnidad;
        String lsAmbito;
        String lsConPartida;

        crsHistoricoEje.beforeFirst();
        while (crsHistoricoEje.next()) {
            try {
                cveret = crsHistoricoEje.getString("cveret");
                cveret = cveret == null ? "* " : "*" + cveret;
            } catch (Exception e) {
                cveret = "* ";
            }
            acumulaAportacion(crsHistoricoEje.getString("programa") + crsHistoricoEje.getString("unidad") + crsHistoricoEje.getString("ambito") + crsHistoricoEje.getString("partida") + cveret, Double.parseDouble((crsHistoricoEje.getString("importe") == null) ? "0.00" : crsHistoricoEje.getString("importe")), hashMapHisEjeConPartida);
            tipoDocto = crsHistoricoEje.getString("Tipo_docto");
        }
        sKey = hashMapHisEjeConPartida.keySet();
        i = sKey.iterator();
        while (i.hasNext()) {
            st = (String) i.next();
            lsPrograma = st.substring(0, 4);
            lsUnidad = st.substring(4, 8);
            lsAmbito = st.substring(8, 12);
            partida = st.substring(12, st.indexOf("*"));
            lsConPartida = partida.substring(0, 2);
            cveret = st.substring(st.indexOf("*") + 1);
//        System.out.println("Totales.calculaTotalBrutoHisEjeConPartida lsConPartida = "+lsConPartida);
//        System.out.println("Totales.calculaTotalBrutoHisEjeConPartida cveret = "+cveret);
//        System.out.println("Totales.calculaTotalBrutoHisEjeConPartida pTipo = "+pTipo);
            totalBruto = (Double) hashMapHisEjeConPartida.get(st);
//        System.out.println("Totales.calculaTotalBrutoHisEjeConPartida totalBruto = "+totalBruto);
            calculaTotalRetencionesConPartida(lsPrograma, lsUnidad, lsAmbito, lsConPartida, importeReteConPartida);
            calculaTotalNeto();
            programa = lsPrograma;
            unidad = lsUnidad;
            ambito = lsAmbito;
            conPartida = lsConPartida;
            partidaGen = lsConPartida;
            if ((anticipo.equals("0")) && (conPartida.substring(0, 1).equals("5"))) {
                setBanProv(1);
            }
            hashMap = creaHashMapVariable(conexionContabilidad, numForma, pTipo);
            cadena.append(Cadena.construyeCadena(hashMap));
            cadena.append("~");
            hashMap.clear();
        }
//      System.out.println("Totales.calculaTotalBrutoHisEjeConPartida.cadena "+cadena);
        return cadena.toString();
    }

    @Override
    public String calculaTotalBrutoHisEjePartidaGen(Connection conexionContabilidad, int numForma, String pTipo) throws SQLException, Exception {
        HashMap hashMap;
        HashMap hashMapHisEjePartidaGen = new HashMap();
        StringBuilder cadena = new StringBuilder("");
        Set sKey;
        String st;
        Iterator i;
        String lsPrograma;
        String lsUnidad;
        String lsAmbito;
        String lsPartidaGen;
        crsHistoricoEje.beforeFirst();
        while (crsHistoricoEje.next()) {
            try {
                cveret = crsHistoricoEje.getString("cveret");
                cveret = cveret == null ? " " : cveret;
            } catch (Exception e) {
                cveret = " ";
            }
            acumulaAportacion(crsHistoricoEje.getString("programa") + crsHistoricoEje.getString("unidad") + crsHistoricoEje.getString("ambito") + crsHistoricoEje.getString("partida").substring(0, 3) + cveret, Double.parseDouble((crsHistoricoEje.getString("importe") == null) ? "0.00" : crsHistoricoEje.getString("importe")), hashMapHisEjePartidaGen);
            tipoDocto = crsHistoricoEje.getString("Tipo_docto");

        }
        sKey = hashMapHisEjePartidaGen.keySet();
        i = sKey.iterator();
        while (i.hasNext()) {
            st = (String) i.next();
            lsPrograma = st.substring(0, 4);
            lsUnidad = st.substring(4, 8);
            lsAmbito = st.substring(8, 12);
            lsPartidaGen = st.substring(12, 15);
            cveret = st.substring(15);
//        System.out.println("Totales.calculaTotalBrutoHisEjePartidaGen lsPartidaGen = "+lsPartidaGen);
//        System.out.println("Totales.calculaTotalBrutoHisEjePartidaGen cveret = "+cveret);
//        System.out.println("Totales.calculaTotalBrutoHisEjePartidaGen pTipo = "+pTipo);
            totalBruto = (Double) hashMapHisEjePartidaGen.get(st);
//        System.out.println("Totales.calculaTotalBrutoHisEjePartidaGen totalBruto = "+totalBruto);
            calculaTotalRetencionesPartidaGen(lsPrograma, lsUnidad, lsAmbito, lsPartidaGen, importeRetePartidaGen);
            calculaTotalNeto();
            programa = lsPrograma;
            unidad = lsUnidad;
            ambito = lsAmbito;
            partidaGen = lsPartidaGen;
            if ((anticipo.equals("0")) && (partidaGen.substring(0, 1).equals("5"))) {
                setBanProv(1);
            }
            hashMap = creaHashMapVariable(conexionContabilidad, numForma, pTipo);
            cveret = " ";
            cadena.append(Cadena.construyeCadena(hashMap));
            cadena.append("~");
            hashMap.clear();
        }
//      System.out.println("Totales.calculaTotalBrutoHisEjePartidaGen.cadena "+cadena);
        return cadena.toString();
    }

    public String calculaGasto(Connection conexionContabilidad, String forma) throws SQLException, Exception {
        if (crsGasto == null) {
            return "";
        }
        HashMap hashMapGasto = new HashMap();
        StringBuilder cadena = new StringBuilder("");
        Set sKey;
        String st;
        Iterator i;
        String cc;
        totalNeto = 0;
        crsGasto.beforeFirst();//aqui  
        while (crsGasto.next()) {
            double importe = (factor==0?1:factor)*Double.parseDouble((crsGasto.getString("importe_bruto") == null) ? "0.00" : crsGasto.getString("importe_bruto"));
            String ref = forma.equals("CI") ? (importe < 0 ? " DICE" : " DEBE DECIR") : "";
            acumulaAportacion(crsGasto.getString(1) + "*" + crsGasto.getString("cuenta_contable_gastos") + crsGasto.getString("consecutivo") + ref, importe, hashMapGasto);
            totalNeto += crsGasto.getString("importe_neto") == null ? 0 : (factor==0?1:factor)*Double.parseDouble(crsGasto.getString("importe_neto"));
        }
        totalNetoCero = totalNeto == 0.0;
        sKey = hashMapGasto.keySet();
        i = sKey.iterator();
        while (i.hasNext()) {
            st = (String) i.next();
            String st2 = st.substring(st.indexOf("*") + 1);
            cc = st2.substring(0, 33);
            String ref = st2.substring(33);
            totalBruto = (Double) hashMapGasto.get(st);
            cadena.append("CC_GASTO=").append(cc).append("|");
            cadena.append("REFERENCIA=").append(ref).append("|");
            cadena.append("IMPORTE_GASTO=").append(totalBruto).append("|");
            cadena.append("~");
        }
        System.out.println("calculaGasto.Cadena " + cadena);
        return cadena.toString();
    }

    public String calculaCxpCp(Connection conexionContabilidad) throws SQLException, Exception {
        if (crsCxpCp == null) {
            return "";
        }
        HashMap hashMapCxpCp = new HashMap();
        StringBuilder cadena = new StringBuilder("");
        Set sKey;
        String st;
        Iterator i;
        String cc;
        crsCxpCp.beforeFirst();//aqui  
        while (crsCxpCp.next()) {
            acumulaAportacion(crsCxpCp.getString(1) + "*" + crsCxpCp.getString("cuenta_contable_Cxp") + crsCxpCp.getString("consecutivo"), (factor==0?1:factor)*Double.parseDouble((crsCxpCp.getString("importe_neto") == null) ? "0.00" : crsCxpCp.getString("importe_neto")), hashMapCxpCp);
        }
        sKey = hashMapCxpCp.keySet();
        i = sKey.iterator();
        while (i.hasNext()) {
            st = (String) i.next();
            String st2 = st.substring(st.indexOf("*") + 1);
            cc = st2.substring(0, 33);
            String ref = st2.substring(33);
            totalNeto = (Double) hashMapCxpCp.get(st);
            if(totalNeto!=0) {
                cadena.append("CC_CXP_CP=").append(cc).append("|");
                cadena.append("REFERENCIA=").append(ref).append("|");
                cadena.append("IMPORTE_CXP_CP=").append(totalNeto).append("|");
                cadena.append("~");
            }
        }
        //System.out.println("calculaCxp.Cadena " + cadena);
        return cadena.toString();
    }
// Se agrega el parametro de evento contable MMH 24/07/2017
    public String calculaRetenciones(Connection conexionContabilidad, String forma, int eventoContable) throws SQLException, Exception {
        if (crsRetenciones == null) {
            return "";
        }
        HashMap hashMapRetenciones = new HashMap();
        StringBuilder cadena = new StringBuilder("");
        Set sKey;
        String st;
        Iterator i;
        String cc;
        crsRetenciones.beforeFirst();//aqui  
        while (crsRetenciones.next()) {
            double importe = (factor==0?1:factor)*Double.parseDouble((crsRetenciones.getString("importe_retenciones") == null) ? "0.00" : crsRetenciones.getString("importe_retenciones"));
            String ref = forma.equals("CI") ? (importe < 0 ? " DICE" : " DEBE DECIR") : "";
            acumulaAportacion(crsRetenciones.getString(1) + "*" + crsRetenciones.getString("cuenta_contable_retenciones") + crsRetenciones.getString("consecutivo") + ref, importe, hashMapRetenciones);
        }
        sKey = hashMapRetenciones.keySet();
        i = sKey.iterator();
        while (i.hasNext()) {
            st = (String) i.next();
            String st2 = st.substring(st.indexOf("*") + 1);
            cc = st2.substring(0, 33);
            String ref = st2.substring(33);
            Double det = (Double) hashMapRetenciones.get(st);
            //Se exceptua el evento 137 que corresponde a una cancelación MMH 24/07/2017            
            if (det < 0 && (forma.equals("CA") || forma.equals("CT")) && eventoContable != 137) {
                cadena.append("CC_RETENCIONES_NEG=").append(cc).append("|");
                cadena.append("REFERENCIA=").append(ref).append("|");
                cadena.append("IMPORTE_RETENCIONES_NEG=").append(-det).append("|");
            } else {
                cadena.append("CC_RETENCIONES=").append(cc).append("|");
                cadena.append("REFERENCIA=").append(ref).append("|");
                cadena.append("IMPORTE_RETENCIONES=").append(det).append("|");
            }
            cadena.append("~");
        }
        System.out.println("calculaRetenciones.Cadena " + cadena);
        return cadena.toString();
    }

    public String calculaDivisas(Connection conexionContabilidad) throws SQLException, Exception {
        if (crsDivisas == null) {
            return "";
        }
        HashMap hashMapDivisas = new HashMap();
        StringBuilder cadena = new StringBuilder("");
        Set sKey;
        String st;
        Iterator i;
        String cc;
        crsDivisas.beforeFirst();//aqui  
        while (crsDivisas.next()) {
            acumulaAportacion(crsDivisas.getString(1) + "*" + crsDivisas.getString("cuenta_contable_divisas") + crsDivisas.getString("consecutivo"), Double.parseDouble((crsDivisas.getString("importe_divisas") == null) ? "0.00" : crsDivisas.getString("importe_divisas")), hashMapDivisas);
        }
        sKey = hashMapDivisas.keySet();
        i = sKey.iterator();
        while (i.hasNext()) {
            st = (String) i.next();
            String st2 = st.substring(st.indexOf("*") + 1);
            cc = st2.substring(0, 33);
            String ref = st2.substring(33);
            Double det = (Double) hashMapDivisas.get(st);
            cadena.append("CC_FONDO_FIJO=111110001000100910004000000000000").append(cc).append("|");
            cadena.append("REFERENCIA=").append(ref).append("|");
            cadena.append("IMPORTE_FONDO_FIJO=").append(det).append("|");
            cadena.append("~");
            cadena.append("CC_DIVISAS=").append(cc).append("|");
            cadena.append("REFERENCIA=").append(ref).append("|");
            cadena.append("IMPORTE_DIVISAS=").append(det).append("|");
            cadena.append("~");
        }
        //System.out.println("calculaDivisas.Cadena " + cadena);
        return cadena.toString();
    }

    public String calculaAportaciones(Connection conexionContabilidad) throws SQLException, Exception {
        if (crsAportaciones == null) {
            return "";
        }
        HashMap hashMapAportaciones = new HashMap();
        StringBuilder cadena = new StringBuilder("");
        Set sKey;
        String st;
        Iterator i;
        String cc;
        crsAportaciones.beforeFirst();//aqui  
        while (crsAportaciones.next()) {
            acumulaAportacion(crsAportaciones.getString(1) + "*" + crsAportaciones.getString("cuenta_contable_aportaciones") + crsAportaciones.getString("consecutivo"), (factor==0?1:factor)*Double.parseDouble((crsAportaciones.getString("importe_aportaciones") == null) ? "0.00" : crsAportaciones.getString("importe_aportaciones")), hashMapAportaciones);
        }
        sKey = hashMapAportaciones.keySet();
        i = sKey.iterator();
        while (i.hasNext()) {
            st = (String) i.next();
            String st2 = st.substring(st.indexOf("*") + 1);
            cc = st2.substring(0, 33);
            String ref = st2.substring(33);
            Double det = (Double) hashMapAportaciones.get(st);
            cadena.append("CC_APORTACIONES=").append(cc).append("|");
            cadena.append("REFERENCIA=").append(ref).append("|");
            cadena.append("IMPORTE_APORTACIONES=").append(det).append("|");
            cadena.append("~");
        }
        //System.out.println("calculaAportaciones.Cadena " + cadena);
        return cadena.toString();
    }

    public String calculaPresupDevengado(Connection conexionContabilidad, String forma) throws SQLException, Exception {
        if (crsPresupDevengado == null) {
            return "";
        }
        HashMap hashMapPresupDevengado = new HashMap();
        StringBuilder cadena = new StringBuilder("");
        Set sKey;
        String st;
        Iterator i;
        String cc;
        crsPresupDevengado.beforeFirst();//aqui  
        while (crsPresupDevengado.next()) {
            double importe = (factor==0?1:factor)*Double.parseDouble((crsPresupDevengado.getString("importe_bruto") == null) ? "0.00" : crsPresupDevengado.getString("importe_bruto"));
            String ref = forma.equals("CI") ? (importe < 0 ? " DICE" : " DEBE DECIR") : "";
            acumulaAportacion(crsPresupDevengado.getString(1) + "*" + crsPresupDevengado.getString("cuenta_contable_presup_deveng") + crsPresupDevengado.getString("consecutivo") + ref, importe, hashMapPresupDevengado);
        }
        sKey = hashMapPresupDevengado.keySet();
        i = sKey.iterator();
        while (i.hasNext()) {
            st = (String) i.next();
            String st2 = st.substring(st.indexOf("*") + 1);
            cc = st2.substring(0, 33);
            String ref = st2.substring(33);
            Double det = (Double) hashMapPresupDevengado.get(st);
            cadena.append("CC_PRESUP_DEVENGADO=").append(cc).append("|");
            cadena.append("REFERENCIA=").append(ref).append("|");
            cadena.append("IMPORTE_PRESUP_DEVENGADO=").append(det).append("|");
            cadena.append("~");
        }
        System.out.println("calculaPresupDevengado.Cadena " + cadena);
        return cadena.toString();
    }

    public String calculaPresupComprometido(Connection conexionContabilidad, String forma) throws SQLException, Exception {
        if (crsPresupComprometido == null) {
            return "";
        }
        HashMap hashMapPresupComprometido = new HashMap();
        StringBuilder cadena = new StringBuilder("");
        Set sKey;
        String st;
        Iterator i;
        String cc;
        crsPresupComprometido.beforeFirst();//aqui  
        while (crsPresupComprometido.next()) {
            double importe = (factor==0?1:factor)*Double.parseDouble((crsPresupComprometido.getString("importe_bruto") == null) ? "0.00" : crsPresupComprometido.getString("importe_bruto"));
            String ref = forma.equals("CI") ? (importe < 0 ? " DICE" : " DEBE DECIR") : "";
            acumulaAportacion(crsPresupComprometido.getString(1) + "*" + crsPresupComprometido.getString("cuenta_contable_presup_compr") + crsPresupComprometido.getString("consecutivo") + ref, importe, hashMapPresupComprometido);
        }
        sKey = hashMapPresupComprometido.keySet();
        i = sKey.iterator();
        while (i.hasNext()) {
            st = (String) i.next();
            String st2 = st.substring(st.indexOf("*") + 1);
            cc = st2.substring(0, 33);
            String ref = st2.substring(33);
            Double det = (Double) hashMapPresupComprometido.get(st);
            if (det < 0 && forma.equals("CQ")) {
                cadena.append("CC_PRESUP_COMPROMETIDO_DEC=").append(cc).append("|");
                cadena.append("REFERENCIA=").append(ref).append("|");
                cadena.append("IMPORTE_PRESUP_COMPROMETIDO_DEC=").append(-det).append("|");
            } else {
                cadena.append("CC_PRESUP_COMPROMETIDO=").append(cc).append("|");
                cadena.append("REFERENCIA=").append(ref).append("|");
                cadena.append("IMPORTE_PRESUP_COMPROMETIDO=").append(det).append("|");
            }
            cadena.append("~");
        }
        System.out.println("calculaPresupComprometido.Cadena " + cadena);
        return cadena.toString();
    }

    public String calculaPresupComprometido2(Connection conexionContabilidad) throws SQLException, Exception {
        if (crsPresupComprometido2 == null) {
            return "";
        }
        HashMap hashMapPresupComprometido2 = new HashMap();
        StringBuilder cadena = new StringBuilder("");
        Set sKey;
        String st;
        Iterator i;
        String cc;
        crsPresupComprometido2.beforeFirst();//aqui  
        while (crsPresupComprometido2.next()) {
            acumulaAportacion(crsPresupComprometido2.getString(1) + "*" + crsPresupComprometido2.getString("cuenta_contable_presup_comp2") + crsPresupComprometido2.getString("consecutivo"), (factor==0?1:factor)*Double.parseDouble((crsPresupComprometido2.getString("importe_bruto") == null) ? "0.00" : crsPresupComprometido2.getString("importe_bruto")), hashMapPresupComprometido2);
        }
        sKey = hashMapPresupComprometido2.keySet();
        i = sKey.iterator();
        while (i.hasNext()) {
            st = (String) i.next();
            String st2 = st.substring(st.indexOf("*") + 1);
            cc = st2.substring(0, 33);
            String ref = st2.substring(33);
            Double det = (Double) hashMapPresupComprometido2.get(st);
            cadena.append("CC_PRESUP_COMPROMETIDO_2=").append(cc).append("|");
            cadena.append("REFERENCIA=").append(ref).append("|");
            cadena.append("IMPORTE_PRESUP_COMPROMETIDO_2=").append(det).append("|");
            cadena.append("~");
        }
        //System.out.println("calculaPresupComprometido2.Cadena " + cadena);
        return cadena.toString();
    }

    public String calculaPresupPorEjercer(Connection conexionContabilidad, String forma) throws SQLException, Exception {
        if (crsPresupPorEjercer == null) {
            return "";
        }
        HashMap hashMapPresupPorEjercer = new HashMap();
        StringBuilder cadena = new StringBuilder("");
        Set sKey;
        String st;
        Iterator i;
        String cc;
        crsPresupPorEjercer.beforeFirst();//aqui  
        while (crsPresupPorEjercer.next()) {
            double importe = (factor==0?1:factor)*Double.parseDouble((crsPresupPorEjercer.getString("importe_bruto") == null) ? "0.00" : crsPresupPorEjercer.getString("importe_bruto"));
            String ref = forma.equals("CI") ? (importe < 0 ? " DICE" : " DEBE DECIR") : "";
            acumulaAportacion(crsPresupPorEjercer.getString(1) + "*" + crsPresupPorEjercer.getString("cuenta_contable_presup_ejercer") + crsPresupPorEjercer.getString("consecutivo") + ref, importe, hashMapPresupPorEjercer);
        }
        sKey = hashMapPresupPorEjercer.keySet();
        i = sKey.iterator();
        while (i.hasNext()) {
            st = (String) i.next();
            String st2 = st.substring(st.indexOf("*") + 1);
            cc = st2.substring(0, 33);
            String ref = st2.substring(33);
            Double det = (Double) hashMapPresupPorEjercer.get(st);
            if (forma.equals("CO")) {
                if (det < 0) {
                    cadena.append("CC_PRESUP_POR_EJERCER_DEC=").append(cc).append("|");
                    cadena.append("IMPORTE_PRESUP_POR_EJERCER_DEC=").append(-det).append("|");
                } else {
                    cadena.append("CC_PRESUP_POR_EJERCER_INC=").append(cc).append("|");
                    cadena.append("IMPORTE_PRESUP_POR_EJERCER_INC=").append(det).append("|");
                }
            } else if (det < 0 && forma.equals("CQ")) {
                cadena.append("CC_PRESUP_POR_EJERCER_DEC=").append(cc).append("|");
                cadena.append("IMPORTE_PRESUP_POR_EJERCER_DEC=").append(-det).append("|");
            } else {
                cadena.append("CC_PRESUP_POR_EJERCER=").append(cc).append("|");
                cadena.append("IMPORTE_PRESUP_POR_EJERCER=").append(det).append("|");
            }
            cadena.append("REFERENCIA=").append(ref).append("|");
            cadena.append("~");
        }
        if (forma.equals("CO") && cadena.indexOf("_DEC")<0) {
            String cad = cadena.toString().replace("INC", "DEC").replace("8221", "8231");
            cadena.append(cad);
        }
        else if (forma.equals("CO") && cadena.indexOf("_INC")<0) {
            String cad = cadena.toString().replace("DEC", "INC").replace("8231", "8221");
            cadena.append(cad);
        }
        System.out.println("calculaPresupPorEjercer.Cadena " + cadena);
        return cadena.toString();
    }

    public String calculaPresupEjercido(Connection conexionContabilidad, String forma) throws SQLException, Exception {
        if (crsPresupEjercido == null) {
            return "";
        }
        HashMap hashMapPresupEjercido = new HashMap();
        StringBuilder cadena = new StringBuilder("");
        Set sKey;
        String st;
        Iterator i;
        String cc;
        crsPresupEjercido.beforeFirst();//aqui  
        while (crsPresupEjercido.next()) {
            if (!crsPresupEjercido.getString("cuenta_contable_pres_ejercido").equals("0")) {
                double importe = (factor==0?1:factor)*Double.parseDouble((crsPresupEjercido.getString("importe_bruto") == null) ? "0.00" : crsPresupEjercido.getString("importe_bruto"));
                String ref = forma.equals("CI") ? (importe < 0 ? " DICE" : " DEBE DECIR") : "";
                acumulaAportacion(crsPresupEjercido.getString(1) + "*" + crsPresupEjercido.getString("cuenta_contable_pres_ejercido") + crsPresupEjercido.getString("consecutivo") + ref, importe, hashMapPresupEjercido);
            }
        }
        sKey = hashMapPresupEjercido.keySet();
        i = sKey.iterator();
        while (i.hasNext()) {
            st = (String) i.next();
            String st2 = st.substring(st.indexOf("*") + 1);
            cc = st2.substring(0, 33);
            String ref = st2.substring(33);
            Double det = (Double) hashMapPresupEjercido.get(st);
            cadena.append("CC_PRESUP_EJERCIDO=").append(cc).append("|");
            cadena.append("REFERENCIA=").append(ref).append("|");
            cadena.append("IMPORTE_PRESUP_EJERCIDO=").append(det).append("|");
            cadena.append("~");
        }
        System.out.println("calculaPresupEjercido.Cadena " + cadena);
        return cadena.toString();
    }

    public String calculaPresupPagado(Connection conexionContabilidad, String forma) throws SQLException, Exception {
        if (crsPresupPagado == null) {
            return "";
        }
        HashMap hashMapPresupPagado = new HashMap();
        StringBuilder cadena = new StringBuilder("");
        Set sKey;
        String st;
        Iterator i;
        String cc;
        crsPresupPagado.beforeFirst();//aqui  
        while (crsPresupPagado.next()) {
            double importe = (factor==0?1:factor)*Double.parseDouble((crsPresupPagado.getString("importe_bruto") == null) ? "0.00" : crsPresupPagado.getString("importe_bruto"));
            String ref = forma.equals("CI") ? (importe < 0 ? " DICE" : " DEBE DECIR") : "";
            acumulaAportacion(crsPresupPagado.getString(1) + "*" + crsPresupPagado.getString("cuenta_contable_presup_pagado") + crsPresupPagado.getString("consecutivo") + ref, importe, hashMapPresupPagado);
        }
        sKey = hashMapPresupPagado.keySet();
        i = sKey.iterator();
        while (i.hasNext()) {
            st = (String) i.next();
            String st2 = st.substring(st.indexOf("*") + 1);
            cc = st2.substring(0, 33);
            String ref = st2.substring(33);
            Double det = (Double) hashMapPresupPagado.get(st);
            cadena.append("CC_PRESUP_PAGADO=").append(cc).append("|");
            cadena.append("REFERENCIA=").append(ref).append("|");
            cadena.append("IMPORTE_PRESUP_PAGADO=").append(det).append("|");
            cadena.append("~");
        }
        System.out.println("calculaPresupPagado.Cadena " + cadena);
        return cadena.toString();
    }

    public String calculaActivos(Connection conexionContabilidad) throws SQLException, Exception {
        if (crsActivos == null) {
            return "";
        }
        HashMap hashMapActivos = new HashMap();
        StringBuilder cadena = new StringBuilder("");
        Set sKey;
        String st;
        Iterator i;
        String cc;
        crsActivos.beforeFirst();//aqui  
        while (crsActivos.next()) {
            acumulaAportacion(crsActivos.getString(1) + "*" + crsActivos.getString(3) + crsActivos.getString("CONSECUTIVO"), (factor==0?1:factor)*Double.parseDouble((crsActivos.getString("IMPORTE_NETO") == null) ? "0.00" : crsActivos.getString("IMPORTE_NETO")), hashMapActivos);
        }
        sKey = hashMapActivos.keySet();
        i = sKey.iterator();
        while (i.hasNext()) {
            st = (String) i.next();
            String st2 = st.substring(st.indexOf("*") + 1);
            cc = st2.substring(0, 33);
            String ref = st2.substring(33);
            Double det = (Double) hashMapActivos.get(st);
            cadena.append("CC_ACTIVO=").append(cc).append("|");
            cadena.append("REFERENCIA=").append(ref).append("|");
            cadena.append("IMPORTE_ACTIVO=").append(det).append("|");
            cadena.append("~");
        }
        System.out.println("calculaActivos.Cadena " + cadena);
        return cadena.toString();
    }

    public String calculaDeudores(Connection conexionContabilidad) throws SQLException, Exception {
        if (crsDeudores == null) {
            return "";
        }
        HashMap hashMapDeudores = new HashMap();
        StringBuilder cadena = new StringBuilder("");
        Set sKey;
        String st;
        Iterator i;
        String cc;
        crsDeudores.beforeFirst();//aqui  
        while (crsDeudores.next()) {
            acumulaAportacion(crsDeudores.getString(1) + "*" + crsDeudores.getString("cuenta_contable_deudores") + crsDeudores.getString("consecutivo"), (factor==0?1:factor)*Double.parseDouble((crsDeudores.getString("importe_bruto") == null) ? "0.00" : crsDeudores.getString("importe_bruto")), hashMapDeudores);
        }
        sKey = hashMapDeudores.keySet();
        i = sKey.iterator();
        while (i.hasNext()) {
            st = (String) i.next();
            String st2 = st.substring(st.indexOf("*") + 1);
            cc = st2.substring(0, 33);
            String ref = st2.substring(33);
            Double det = (Double) hashMapDeudores.get(st);
            cadena.append("CC_DEUDORES=").append(cc).append("|");
            cadena.append("REFERENCIA=").append(ref).append("|");
            cadena.append("IMPORTE_DEUDORES=").append(det).append("|");
            cadena.append("~");
        }
        //System.out.println("calculaDeudores.Cadena " + cadena);
        return cadena.toString();
    }

    public String calculaClientes(Connection conexionContabilidad) throws SQLException, Exception {
        if (crsClientes == null) {
            return "";
        }
        HashMap hashMapClientes = new HashMap();
        StringBuilder cadena = new StringBuilder("");
        Set sKey;
        String st;
        Iterator i;
        String cc;
        crsClientes.beforeFirst();//aqui  
        while (crsClientes.next()) {
            acumulaAportacion(crsClientes.getString(1) + "*" + crsClientes.getString("cc_clientes") + crsClientes.getString("consecutivo"), Double.parseDouble((crsClientes.getString("importe_bruto") == null) ? "0.00" : crsClientes.getString("importe_bruto")), hashMapClientes);
        }
        sKey = hashMapClientes.keySet();
        i = sKey.iterator();
        while (i.hasNext()) {
            st = (String) i.next();
            String st2 = st.substring(st.indexOf("*") + 1);
            cc = st2.substring(0, 33);
            String ref = st2.substring(33);
            Double det = (Double) hashMapClientes.get(st);
            cadena.append("CC_CLIENTES=").append(cc).append("|");
            cadena.append("REFERENCIA=").append(ref).append("|");
            cadena.append("IMPORTE_CLIENTES=").append(det).append("|");
            cadena.append("~");
        }
        //System.out.println("calculaClientes.Cadena " + cadena);
        return cadena.toString();
    }

    @Override
    public String calculaTotalFacturaCapitulo(Connection conexionContabilidad, int numForma, String pTipo, Registro registro, String fecha, String pCatCuenta) throws SQLException, Exception {
        HashMap hashMap;
        HashMap hashMapFactura = new HashMap();
        HashMap hashMapIva = new HashMap();
        StringBuilder cadena = new StringBuilder("");
        Set sKey;
        String st;
        Iterator i;
        String lsPrograma;
        String lsUnidad;
        String lsAmbito;
        String lsCapitulo;
        String resultado;

        crsFactura.beforeFirst();
        while (crsFactura.next()) {
            acumulaAportacion(crsFactura.getString("programa") + crsFactura.getString("unidad") + crsFactura.getString("ambito") + crsFactura.getString("capitulo"), Double.parseDouble((crsFactura.getString("importe") == null) ? "0.00" : crsFactura.getString("importe")), hashMapFactura);
            acumulaAportacion(crsFactura.getString("programa") + crsFactura.getString("unidad") + crsFactura.getString("ambito") + crsFactura.getString("capitulo"), Double.parseDouble((crsFactura.getString("iva") == null) ? "0.00" : crsFactura.getString("iva")), hashMapIva);
            rfc = crsFactura.getString("cveprov");
            descripcionRFC = crsFactura.getString("nomprov");
            tiga = crsFactura.getString("tiga");
            capitulo = crsFactura.getString("capitulo");
            tigaCapitulo = crsFactura.getString("tigaCapitulo");
            claveConcepto = crsFactura.getString("claveConcepto");
            unidad = crsFactura.getString("unidad");
            ambito = crsFactura.getString("ambito");
            programa = crsFactura.getString("programa");
            bcProveedores bcProveedor = new bcProveedores();
            claveProveedor = bcProveedor.select_rf_tc_proveedores_rfc(conexionContabilidad, rfc);
            if (claveProveedor.equals("0")) {
                claveProveedor = bcProveedor.select_SEQ_rf_tc_proveedores(conexionContabilidad);
                bcProveedor.setId_contable(claveProveedor);
                bcProveedor.setRfc(rfc);
                bcProveedor.setNombre_razon_social(descripcionRFC);
                bcProveedor.insert_rf_tc_proveedores(conexionContabilidad);
            }

            if (capitulo.equals("2000") || capitulo.equals("3000")) {//anticipo a prov
                resultado = registro.AgregaCuentaContable("1131", capitulo.substring(0, 1), programa, unidad, ambito, claveProveedor, "0000", "0000", "0000", fecha, descripcionRFC, "6", pCatCuenta);
            } else if (capitulo.equals("6000")) {//anticipo a contratista
                resultado = registro.AgregaCuentaContable("1134", "1", programa, unidad, ambito, claveProveedor, "0000", "0000", "0000", fecha, descripcionRFC, "6", pCatCuenta);
                resultado = registro.AgregaCuentaContable("1134", "1", programa, unidad, ambito, claveProveedor, "0001", "0000", "0000", fecha, "Obra Pública", "7", pCatCuenta);
                resultado = registro.AgregaCuentaContable("1134", "1", programa, unidad, ambito, claveProveedor, "0002", "0000", "0000", fecha, "Iva de Obra Pública", "7", pCatCuenta);
            }
        }
        sKey = hashMapFactura.keySet();
        i = sKey.iterator();
        while (i.hasNext()) {
            st = (String) i.next();
            lsPrograma = st.substring(0, 4);
            lsUnidad = st.substring(4, 8);
            lsAmbito = st.substring(8, 12);
            lsCapitulo = st.substring(12);
            setTotalAntesIVA((Double) hashMapFactura.get(st));
            setTotalIVA(buscaIVA(lsPrograma, lsUnidad, lsAmbito, lsCapitulo, hashMapIva));
            totalBruto = totalAntesIVA + totalIVA;
            programa = lsPrograma;
            unidad = lsUnidad;
            ambito = lsAmbito;
            capitulo = lsCapitulo;
            hashMap = creaHashMapVariable(conexionContabilidad, numForma, pTipo);
            cadena.append(Cadena.construyeCadena(hashMap));
            cadena.append("~");
            hashMap.clear();
        }
        return cadena.toString();
    }

    @Override
    public String calculaTotalFacturaConPartida(Connection conexionContabilidad, int numForma, String pTipo, Registro registro, String fecha, String pCatCuenta) throws SQLException, Exception {
        HashMap hashMap;
        HashMap hashMapFacturaConPartida = new HashMap();
        HashMap hashMapIvaConPartida = new HashMap();
        StringBuilder cadena = new StringBuilder("");
        Set sKey;
        String st;
        Iterator i;
        String lsPrograma;
        String lsUnidad;
        String lsAmbito;
        String lsConPartida;
        String resultado = "";

        bcProveedores bcProveedor = new bcProveedores();
        crsFactura.beforeFirst();
        while (crsFactura.next()) {
            acumulaAportacion(crsFactura.getString("programa") + crsFactura.getString("unidad") + crsFactura.getString("ambito") + crsFactura.getString("partida"), Double.parseDouble((crsFactura.getString("importe") == null) ? "0.00" : crsFactura.getString("importe")), hashMapFacturaConPartida);
            acumulaAportacion(crsFactura.getString("programa") + crsFactura.getString("unidad") + crsFactura.getString("ambito") + crsFactura.getString("partida"), Double.parseDouble((crsFactura.getString("iva") == null) ? "0.00" : crsFactura.getString("iva")), hashMapIvaConPartida);
            lsConPartida = crsFactura.getString("partida").substring(0, 2);
            rfc = crsFactura.getString("cveprov");
            descripcionRFC = crsFactura.getString("nomprov");
            tiga = crsFactura.getString("tiga");
            capitulo = crsFactura.getString("capitulo");
            tigaCapitulo = crsFactura.getString("tigaCapitulo");
            claveConcepto = crsFactura.getString("claveConcepto");
            unidad = crsFactura.getString("unidad");
            ambito = crsFactura.getString("ambito");
            programa = crsFactura.getString("programa");
            claveProveedor = bcProveedor.select_rf_tc_proveedores_rfc(conexionContabilidad, rfc);
            if (claveProveedor.equals("0")) {
                claveProveedor = bcProveedor.select_SEQ_rf_tc_proveedores(conexionContabilidad);
                bcProveedor.setId_contable(claveProveedor);
                bcProveedor.setRfc(rfc);
                bcProveedor.setNombre_razon_social(descripcionRFC);
                bcProveedor.insert_rf_tc_proveedores(conexionContabilidad);
            }
            if (lsConPartida.equals("59")) {
                resultado = registro.AgregaCuentaContable("1133", "1", programa, unidad, ambito, claveProveedor, "0000", "0000", "0000", fecha, descripcionRFC, "6", pCatCuenta);
                resultado = registro.AgregaCuentaContable("1133", "1", programa, unidad, ambito, claveProveedor, "0001", "0000", "0000", fecha, "Bienes inventariables", "7", pCatCuenta);
                resultado = registro.AgregaCuentaContable("1133", "1", programa, unidad, ambito, claveProveedor, "0002", "0000", "0000", fecha, "Iva de bienes inventariables", "7", pCatCuenta);
            } else if (lsConPartida.equals("50") || lsConPartida.equals("51") || lsConPartida.equals("53") || lsConPartida.equals("54") || lsConPartida.equals("55") || lsConPartida.equals("56") || lsConPartida.equals("57") || lsConPartida.equals("58")) {
                resultado = registro.AgregaCuentaContable("1132", "1", programa, unidad, ambito, claveProveedor, "0000", "0000", "0000", fecha, descripcionRFC, "6", pCatCuenta);
                resultado = registro.AgregaCuentaContable("1132", "1", programa, unidad, ambito, claveProveedor, "0001", "0000", "0000", fecha, "Bienes inventariables", "7", pCatCuenta);
                resultado = registro.AgregaCuentaContable("1132", "1", programa, unidad, ambito, claveProveedor, "0002", "0000", "0000", fecha, "Iva de bienes inventariables", "7", pCatCuenta);
            }
        }
        sKey = hashMapFacturaConPartida.keySet();
        i = sKey.iterator();
        while (i.hasNext()) {
            st = (String) i.next();
            lsPrograma = st.substring(0, 4);
            lsUnidad = st.substring(4, 8);
            lsAmbito = st.substring(8, 12);
            partida = st.substring(12);
            lsConPartida = partida.substring(0, 2);
            setTotalAntesIVA((Double) hashMapFacturaConPartida.get(st));
            setTotalIVA(buscaIVA(lsPrograma, lsUnidad, lsAmbito, lsConPartida, hashMapIvaConPartida));
            totalBruto = totalAntesIVA + totalIVA;
            programa = lsPrograma;
            unidad = lsUnidad;
            ambito = lsAmbito;
            conPartida = lsConPartida;
            hashMap = creaHashMapVariable(conexionContabilidad, numForma, pTipo);
            cadena.append(Cadena.construyeCadena(hashMap));
            cadena.append("~");
            hashMap.clear();
        }
        return cadena.toString();
    }

    @Override
    public String calculaTotalBrutoHisEje(Connection conexionContabilidad, int numForma) throws SQLException, Exception {
        HashMap hashMap;
        HashMap hashMapHisEjeTIGA = new HashMap();
        StringBuilder cadena = new StringBuilder("");
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
        while (crsHistoricoEje.next()) {
            try {
                cveret = crsHistoricoEje.getString("cveret");
                cveret = cveret == null ? " " : cveret;
            } catch (Exception e) {
                cveret = " ";
            }
            try {
                lsPartida = crsHistoricoEje.getString("partida");
                lsPartida = lsPartida == null ? " " : lsPartida;
            } catch (Exception e) {
                lsPartida = " ";
            }
            acumulaAportacion(crsHistoricoEje.getString("programa") + crsHistoricoEje.getString("unidad") + crsHistoricoEje.getString("ambito") + crsHistoricoEje.getString("tiga") + crsHistoricoEje.getString("tigaCapitulo") + cveret + "*" + lsPartida, Double.parseDouble((crsHistoricoEje.getString("importe") == null) ? "0.00" : crsHistoricoEje.getString("importe")), hashMapHisEjeTIGA);
            tipoDocto = crsHistoricoEje.getString("Tipo_docto");
        }
        sKey = hashMapHisEjeTIGA.keySet();
        i = sKey.iterator();
        while (i.hasNext()) {
            st = (String) i.next();
            //System.out.println("Eje");
            lsPrograma = st.substring(0, 4);
            lsUnidad = st.substring(4, 8);
            lsAmbito = st.substring(8, 12);
            lsTiga = st.substring(12, 16);
            lsTigaCapitulo = st.substring(16, 20);
            cveret = st.substring(20, st.indexOf("*"));
            partida = st.substring(st.indexOf("*") + 1);
            lsPartida = partida.substring(0, 3);
//        System.out.println("Totales.calculaTotalBrutoHisEje cveret = "+cveret);
//        System.out.println("Totales.calculaTotalBrutoHisEje lsPartida = "+lsPartida);
            partidaGen = lsPartida;
            conPartida = lsPartida.substring(0, 2);
            totalBruto = (Double) hashMapHisEjeTIGA.get(st);
            //System.out.println("Totales.calculaTotalBrutoHisEje totalBruto = "+totalBruto);
            calculaTotalRetencionesTIGA(lsPrograma, lsUnidad, lsAmbito, lsTiga, importeReteTIGA);
            programa = lsPrograma;
            unidad = lsUnidad;
            ambito = lsAmbito;
            tiga = lsTiga;
            tigaCapitulo = lsTigaCapitulo;
            calculaTotalNeto();
            hashMap = creaHashMap(conexionContabilidad, numForma);
            cadena.append(Cadena.construyeCadena(hashMap));
            cadena.append("~");
            hashMap.clear();
        }
//      System.out.println("Totales.calculaTotalBrutoHisEje.cadena "+cadena);
        return cadena.toString();
    }

    @Override
    public String generaFormaFinaciera(Connection conexionContabilidad, int numForma, String ambitoCuenta, String tipoCuenta) throws SQLException, Exception {
        HashMap hashMap;
        HashMap hashMapHisEjeTIGA = new HashMap();
        StringBuilder cadena = new StringBuilder("");
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
        while (crsHistoricoEje.next()) {
            acumulaAportacion(crsHistoricoEje.getString("programa") + crsHistoricoEje.getString("unidad") + crsHistoricoEje.getString("ambito") + crsHistoricoEje.getString("tiga") + crsHistoricoEje.getString("tigaCapitulo"), Double.parseDouble((crsHistoricoEje.getString("importe") == null) ? "0.00" : crsHistoricoEje.getString("importe")), hashMapHisEjeTIGA);
            tipoDocto = crsHistoricoEje.getString("Tipo_docto");
        }
        sKey = hashMapHisEjeTIGA.keySet();
        i = sKey.iterator();
        while (i.hasNext()) {
            st = (String) i.next();
            lsPrograma = st.substring(0, 4);
            lsUnidad = st.substring(4, 8);
            lsAmbito = st.substring(8, 12);
            lsTiga = st.substring(12, 16);
            lsTigaCapitulo = st.substring(16);
            totalBruto = (Double) hashMapHisEjeTIGA.get(st);
            calculaTotalRetencionesTIGA(lsPrograma, lsUnidad, lsAmbito, lsTiga, importeReteTIGA);
            //System.out.println("Programa: "+lsPrograma+"Unidad:"+lsUnidad+"Ambito:"+lsAmbito);
            programa = lsPrograma;
            unidad = lsUnidad;
            ambito = lsAmbito;
            tiga = lsTiga;
            tigaCapitulo = lsTigaCapitulo;
            calculaTotalNeto();
            hashMap = creaHashMap(conexionContabilidad, numForma);
            cadena.append(Cadena.construyeCadena(hashMap));
            cadena.append("~");
            hashMap.clear();
        }
        return cadena.toString();
    }

    @Override
    public String recorreTotalRetenciones() throws SQLException, Exception {
        Set sKey;
        String st;
        sKey = importeRete.keySet();
        Iterator i = sKey.iterator();
        String lsPrograma;
        String lsUnidad;
        String lsAmbito;
        String lsEjercicio;
        String lsRetencion = "";
        String lsId;
        String lsTipo;
        StringBuilder cadena = new StringBuilder("");
        HashMap hashMap = new HashMap();
        Double ldImporte;
        while (i.hasNext()) {
            st = (String) i.next();
            lsPrograma = st.substring(0, 4);
            lsUnidad = st.substring(4, 8);
            lsAmbito = st.substring(8, 12);
            lsEjercicio = st.substring(12, 16);
            if (st.substring(16, 20).equals("D077") || st.substring(16, 20).equals("D214") || st.substring(16, 20).equals("D002") || st.substring(16, 20).equals("D004")) {
                lsRetencion = st.substring(16, 21);
                lsId = st.substring(21);
            } else {
                lsRetencion = st.substring(16, 20);
                lsId = st.substring(20);

            }
            crsSentenciaRetencion.beforeFirst();
            while (crsSentenciaRetencion.next()) {
                if (crsSentenciaRetencion.getString("retencion_id").equals(lsId)) {
                    hashMap.put("PROGRAMA", lsPrograma);
                    hashMap.put("UNIDAD", lsUnidad);
                    hashMap.put("AMBITO", lsAmbito);
                    hashMap.put("IMP1", crsSentenciaRetencion.getString("nivel5"));
                    hashMap.put("EJERCICIO", lsEjercicio);
                    hashMap.put("EJERCICIO_ANT", "00" + lsEjercicio.substring(2));
                    hashMap.put("IMP2", crsSentenciaRetencion.getString("nivel6"));
                    lsTipo = crsSentenciaRetencion.getString("tipo_ret");
                    ldImporte = (Double) importeRete.get(st);
                    if (factor == -1) {
                        ldImporte = ldImporte * factor;
                    }
                    if (lsTipo.equals("C")) {
                        ldImporte = ldImporte * -1.0D;  //Multiplica la retencion de CAS por -1 siempre
                        hashMap.put("IMPORTE_CAS", ldImporte);
                    } else {
                        //Se agrego el Tipo A para Anticipos retencion D032 11206
                        if (lsTipo.equals("A")) {
                            ldImporte = ldImporte * -1.0D;  //Multiplica la retencion de Anticipo a Sueldos (11206) por -1 siempre
                            hashMap.put("IMPORTE_ANTICIPOS", ldImporte);
                        } else {
                            if (lsTipo.equals("I")) {
                                hashMap.put("IMPORTE_ISR", ldImporte);
                            } else if (lsTipo.equals("P")) {
                                hashMap.put("IMPORTE_APORTA_P076", ldImporte);
                            } else {
                                hashMap.put("IMPORTE_RETENCIONES", ldImporte);
                            }
                        }
                    }
                    //ASIENTO DE RECLASIFICACION PARA ANTICIPO A SUELDOS

                    if (crsSentenciaRetencion.getString("retencion_id").equals("10")) { //Retencion id=10 correspondiente a D032 21204
                        hashMap.put("IMPORTE_RECLASIFICA_ANTICIPO", ldImporte);
                    }
                    hashMap.put("REFERENCIA", referencia);
                    cadena.append(Cadena.construyeCadena(hashMap));
                    cadena.append("~");
                    hashMap.clear();
                    break;
                }
            }
        }
        return cadena.toString();
    }

    @Override
    public String recorreTotalAportaciones() throws SQLException, Exception {
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
        StringBuilder cadena = new StringBuilder("");
        HashMap hashMap = new HashMap();
        Double ldImporte;
        if (banDeduccionDA == false) {
            while (i.hasNext()) {
                st = (String) i.next();
                lsPrograma = st.substring(0, 4);
                lsUnidad = st.substring(4, 8);
                lsAmbito = st.substring(8, 12);
                lsEjercicio = st.substring(12, 16);
                lsCapitulo = st.substring(16, 20);
                lsId = st.substring(20);
                crsSentenciaAportacion.beforeFirst();
                while (crsSentenciaAportacion.next()) {
                    if (crsSentenciaAportacion.getString("aportacion_id").equals(lsId)) {
                        ldImporte = (Double) importeAport.get(st);
                        if (ldImporte != 0.0) {
                            hashMap.put("PROGRAMA", lsPrograma);
                            hashMap.put("UNIDAD", lsUnidad);
                            hashMap.put("AMBITO", lsAmbito);
                            hashMap.put("IMP1", crsSentenciaAportacion.getString("nivel5"));
                            hashMap.put("EJERCICIO", lsEjercicio);
                            hashMap.put("CAPITULO", lsCapitulo);
                            hashMap.put("IMP2", crsSentenciaAportacion.getString("nivel6"));
                            if (factor == -1) {
                                ldImporte = ldImporte * factor;
                            }
                            hashMap.put("IMPORTE_APORTA", ldImporte);
                            hashMap.put("REFERENCIA", referencia);
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

    /*   
     private double recorreAportaciones(HashMap arreglo){
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
     private double recorreRetenciones(HashMap arreglo){
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
    @Override
    public void imprimeRetenciones() {
        Set sKey;
        String st = "";
        sKey = importeRete.keySet();
        Iterator i = sKey.iterator();
        while (i.hasNext()) {
            st = (String) i.next();
            //System.out.println("V: " + st + "="+importeRete.get(st));
        }
    }

    @Override
    public void calculaAportaciones() throws SQLException, Exception {
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
        try {
            crsSentenciaAportacion = aportacion.select_rf_tc_aportaciones_todas();
            crsHistoricoEje.beforeFirst();
            while (crsHistoricoEje.next()) {
                crsSentenciaAportacion.beforeFirst();
                llave = "0";
                while (crsSentenciaAportacion.next()) {
                    partida = crsHistoricoEje.getString("partida");
                    unidad = crsHistoricoEje.getString("unidad");
                    ambito = crsHistoricoEje.getString("ambito");
                    conceptoNomina = String.valueOf(conceptoNomina());
                    tipoEje = crsHistoricoEje.getString("tipoEje");
                    tipoDoc = crsHistoricoEje.getString("tipo_Docto");
                    ejercicio = crsHistoricoEje.getString("ejercicio");
                    capitulo = crsHistoricoEje.getString("capitulo");

                    ScriptableObject.putProperty(scope, "partida", Context.javaToJS(partida, scope));
                    ScriptableObject.putProperty(scope, "unidad", Context.javaToJS(unidad, scope));
                    ScriptableObject.putProperty(scope, "ambito", Context.javaToJS(ambito, scope));
                    ScriptableObject.putProperty(scope, "conceptoNomina", Context.javaToJS(conceptoNomina, scope));
                    ScriptableObject.putProperty(scope, "tipoEje", Context.javaToJS(tipoEje, scope));
                    ScriptableObject.putProperty(scope, "tipoDoc", Context.javaToJS(tipoDoc, scope));
                    ScriptableObject.putProperty(scope, "ejercicio", Context.javaToJS(ejercicio, scope));
                    compiled = context.compileString(crsSentenciaAportacion.getString("regla_contable"), "defLoaf", 0, null);
                    compiled.exec(context, scope);
                    resultado = ScriptableObject.getProperty(scope, "resultado");
//  modificado para debug MMH 24/07/2017 se quitaron comentarios                    
                System.out.println("Totales.calculaAportaciones."+crsSentenciaAportacion.getString("descripcion")+": "+crsSentenciaAportacion.getString("regla_contable"));
                System.out.println("Totales.calculaAportaciones.resultado "+resultado);
                    if (resultado.equals("1")) {
                        llave = crsSentenciaAportacion.getString("aportacion_id");
                        break;
                    }
                }
                if (!llave.equals("0")) {
                    acumulaAportacion(crsHistoricoEje.getString("programa") + crsHistoricoEje.getString("unidad") + crsHistoricoEje.getString("ambito") + crsHistoricoEje.getString("ejercicio") + crsHistoricoEje.getString("capitulo") + llave, Double.parseDouble((crsHistoricoEje.getString("importe") == null) ? "0.00" : crsHistoricoEje.getString("importe")), importeAport);
                }
            }
        } catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo calculaAportaciones " + e.getMessage());
            throw e;
        } //Fin catch 
        finally {
            if (crsSentencia != null) {
                crsSentencia.close();
                crsSentencia = null;
            }
        }
    }

    @Override
    public void calculaRetencionesHistorico() throws SQLException, Exception {
        if(crsHistoricoEje==null || crsHistoricoRet==null) {
            return;
        }
        Context context = Context.enter();
        Scriptable scope = context.initStandardObjects();
        context.getWrapFactory().setJavaPrimitiveWrap(false);
        org.mozilla.javascript.Script compiled;

        String partida;
        String tipoEje;
        Object resultado;
        String llave;
        boolean banRompe;

        Retencion retencion = new Retencion();
        Impuesto impuesto = new Impuesto();
        try {
            /* ** Obtiene Total Bruto por Unidad y AmbitoEstado */
            HashMap hashMapHisEjeBruto = new HashMap();

            Set sKey;
            String st;
            Iterator i;
            String ejercicio;
            crsHistoricoEje.beforeFirst();
            while (crsHistoricoEje.next()) {
                acumulaAportacion(crsHistoricoEje.getString("programa") + crsHistoricoEje.getString("unidad") + crsHistoricoEje.getString("ambito"), Double.parseDouble((crsHistoricoEje.getString("importe") == null) ? "0.00" : crsHistoricoEje.getString("importe")), hashMapHisEjeBruto);
            }
            sKey = hashMapHisEjeBruto.keySet();
            i = sKey.iterator();
            while (i.hasNext()) {
                st = (String) i.next();
                totalBruto = (Double) hashMapHisEjeBruto.get(st);
            }
            //aqui
            crsFormasActivos = obtenFormasActivos();
            crsFormasCxpCp = obtenFormasCxpCp();
            crsFormasDeducciones = obtenFormasDeducciones();
            crsFormasGastos = obtenFormasGastos();
            crsFormasPresupuestales = obtenFormasPresupuestales();
            crsSentenciaRetencion = retencion.select_rf_tc_retenciones_todas();
            crsSentenciaImpuesto = impuesto.select_rf_tc_partidas_impuestos_todas();
            crsHistoricoRet.beforeFirst();
            while (crsHistoricoRet.next()) {
                if (crsHistoricoRet.getString("CVERET").substring(0, 2).equals("DA")) {
                    banDeduccionDA = true;
                }
                crsSentenciaRetencion.beforeFirst();
                llave = "0";
                while (crsSentenciaRetencion.next()) {
                    ejercicio = crsHistoricoRet.getString("ejercicio");
                    if (crsSentenciaRetencion.getString("par_impuesto_id") == null) {
                        if (crsHistoricoRet.getString("cveRet").equals(crsSentenciaRetencion.getString("clave_ret_ded"))) {
                            llave = crsSentenciaRetencion.getString("clave_ret_ded") + crsSentenciaRetencion.getString("retencion_id");
                            break;
                        } else {
                            continue;
                        }
                    } else {
                        if (crsHistoricoRet.getString("cveRet").equals(crsSentenciaRetencion.getString("clave_ret_ded"))) {
                            crsSentenciaImpuesto.beforeFirst();
                            banRompe = false;
                            while (crsSentenciaImpuesto.next()) {
                                if (crsSentenciaImpuesto.getString("clave_ret").equals(crsSentenciaRetencion.getString("retencion_id"))) {
                                    partida = crsHistoricoRet.getString("partida");
                                    tipoEje = crsHistoricoRet.getString("tipoEje");
                                    ScriptableObject.putProperty(scope, "partida", Context.javaToJS(partida, scope));
                                    ScriptableObject.putProperty(scope, "tipoeje", Context.javaToJS(tipoEje, scope));
                                    ScriptableObject.putProperty(scope, "totalBruto", Context.javaToJS(totalBruto, scope));
                                    ScriptableObject.putProperty(scope, "tipoDoc", Context.javaToJS(crsHistoricoRet.getString("cxlc").substring(6, 9), scope));
                                    ScriptableObject.putProperty(scope, "ejercicio", Context.javaToJS(ejercicio, scope));
                                    compiled = context.compileString(crsSentenciaImpuesto.getString("regla_contable"), "defLoaf", 0, null);
                                    compiled.exec(context, scope);
                                    resultado = ScriptableObject.getProperty(scope, "resultado");
                                    //                System.out.println("Totales.calculaRetencionesHistorico."+crsSentenciaImpuesto.getString("nombrevariable")+": "+crsSentenciaImpuesto.getString("regla_contable"));
                                    //                System.out.println("Totales.calculaRetencionesHistorico.resultado "+resultado);
                                    if (resultado.equals("1")) {
                                        //System.out.println("Totales.calculaRetencionesHistorico."+crsSentenciaImpuesto.getString("regla_contable"));
                                        llave = crsSentenciaRetencion.getString("clave_ret_ded") + crsSentenciaRetencion.getString("retencion_id");
                                        banRompe = true;
                                        break;
                                    }
                                }
                            }
                            if (banRompe == true) {
                                break;
                            }
                        }
                    } //fin else 
                }
                if (!llave.equals("0")) {
                    //   if (crsHistoricoRet.getString("unidad").equals("0124") && crsHistoricoRet.getString("ambito").equals("0092")){
                    //if (crsHistoricoRet.getString("unidad").equals("0124")){
                    acumulaAportacion(crsHistoricoRet.getString("programa") + crsHistoricoRet.getString("unidad") + crsHistoricoRet.getString("ambito") + crsHistoricoRet.getString("ejercicio") + llave, Double.parseDouble((crsHistoricoRet.getString("importe") == null) ? "0.00" : crsHistoricoRet.getString("importe")), importeRete);
                    acumulaAportacion(crsHistoricoRet.getString("programa") + crsHistoricoRet.getString("unidad") + crsHistoricoRet.getString("ambito") + crsHistoricoRet.getString("tiga") + llave, Double.parseDouble((crsHistoricoRet.getString("importe") == null) ? "0.00" : crsHistoricoRet.getString("importe")), importeReteTIGA);
                    acumulaAportacion(crsHistoricoRet.getString("programa") + crsHistoricoRet.getString("unidad") + crsHistoricoRet.getString("ambito") + crsHistoricoRet.getString("partida").substring(0, 2) + llave, Double.parseDouble((crsHistoricoRet.getString("importe") == null) ? "0.00" : crsHistoricoRet.getString("importe")), importeReteConPartida);
                    acumulaAportacion(crsHistoricoRet.getString("programa") + crsHistoricoRet.getString("unidad") + crsHistoricoRet.getString("ambito") + crsHistoricoRet.getString("capitulo") + llave, Double.parseDouble((crsHistoricoRet.getString("importe") == null) ? "0.00" : crsHistoricoRet.getString("importe")), importeReteCapitulo);
                    acumulaAportacion(crsHistoricoRet.getString("programa") + crsHistoricoRet.getString("unidad") + crsHistoricoRet.getString("ambito") + crsHistoricoRet.getString("partida").substring(0, 3) + llave, Double.parseDouble((crsHistoricoRet.getString("importe") == null) ? "0.00" : crsHistoricoRet.getString("importe")), importeRetePartidaGen);
                    // }
                }
            }
        } catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo calculaRetencionesHistorico " + e.getMessage());
            throw e;
        } //Fin catch 
        finally {
            if (crsSentencia != null) {
                crsSentencia.close();
                crsSentencia = null;
            }
        }
    }

    @Override
    public void calcutaTotales(Connection conexionContabilidad, Connection conexionSistema, String evento, String parametros) throws SQLException, Exception {
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

    @Override
    public HashMap creaHashMap(Connection conexionContabilidad, int numForma) throws SQLException, Exception {
        Context context = Context.enter();
        Scriptable scope = context.initStandardObjects();
        context.getWrapFactory().setJavaPrimitiveWrap(false);
        org.mozilla.javascript.Script compiled;
        Object r = null;

        Object resultado;
        String llave;
        String valor;
        boolean bandera;

        HashMap variables = null;
        variables = new HashMap();
        Funcion funcion = new Funcion();

        try {
            crsSentenciaFuncion = funcion.select_rf_tc_FuncionesPorForma(numForma);
            crsSentenciaFuncion.beforeFirst();
            factoriza();
            while (crsSentenciaFuncion.next()) {
                bandera = false;
                if (crsSentenciaFuncion.getString("regla_contable").equals("SIN")) {
                    bandera = true;
                } else {
                    ScriptableObject.putProperty(scope, "anticipo", Context.javaToJS(anticipo, scope));
                    ScriptableObject.putProperty(scope, "capitulo", Context.javaToJS(capitulo, scope));
                    ScriptableObject.putProperty(scope, "ambitoCuenta", Context.javaToJS(ambitoCuenta, scope));
                    ScriptableObject.putProperty(scope, "tiga", Context.javaToJS(tiga, scope));
                    ScriptableObject.putProperty(scope, "totalNeto", Context.javaToJS(totalNeto, scope));
                    ScriptableObject.putProperty(scope, "programa", Context.javaToJS(programa, scope));
                    ScriptableObject.putProperty(scope, "tipo_afectacion", Context.javaToJS(tipo_afectacion, scope));
                    ScriptableObject.putProperty(scope, "devengado", Context.javaToJS(banDevengado, scope));
                    ScriptableObject.putProperty(scope, "id_tipo_bene", Context.javaToJS(id_tipo_beneficiario, scope));
                    ScriptableObject.putProperty(scope, "cveret", Context.javaToJS(cveret, scope));
                    ScriptableObject.putProperty(scope, "partidaGen", Context.javaToJS(partidaGen, scope));
                    ScriptableObject.putProperty(scope, "conPartida", Context.javaToJS(conPartida, scope));
                    ScriptableObject.putProperty(scope, "partida", Context.javaToJS(partida, scope));
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
                     */ try {
//                    System.out.println("Totales.creaHashMap "+crsSentenciaFuncion.getString("nombrevariable")+" "+crsSentenciaFuncion.getString("regla_contable"));
                        compiled = context.compileString(crsSentenciaFuncion.getString("regla_contable"), "defLoaf", 0, null);
                        compiled.exec(context, scope);
                        resultado = ScriptableObject.getProperty(scope, "resultado");
//                    System.out.println("Resultado = "+resultado);
                    } catch (Exception e) {
                        System.out.println("Totales.creaHashMap().crsSentenciaFuncion.getString(\"regla_contable\") " + crsSentenciaFuncion.getString("regla_contable"));
                        System.out.println("Ocurrio un error al accesar al metodo creaHashMap " + e.getMessage());
                        resultado = "0";
                    }
//                System.out.println("Totales.creaHashMap "+crsSentenciaFuncion.getString("nombrevariable")+": "+crsSentenciaFuncion.getString("regla_contable"));
//                System.out.println("Resultado : "+resultado);
                    if (resultado.equals("1")) {
                        bandera = true;
                    }
                }
//              System.out.println("-Totales.creaHashMap "+crsSentenciaFuncion.getString("nombrevariable"));
                if (bandera == true) {
//                System.out.println("Totales.creaHashMap "+crsSentenciaFuncion.getString("nombrevariable"));
                    llave = crsSentenciaFuncion.getString("nombreVariable");
                    valor = "0.0";
                    String mayor51 = partida == null || partida.length() < 5 ? "0" : "51" + partida.substring(1, 2);
                    String mayor52 = partida == null || partida.length() < 5 ? "0" : "52" + partida.substring(1, 2);
                    String nivel2 = partida == null || partida.length() < 5 ? "0" : partida.substring(3, 1);
                    String nivel6 = partida == null || partida.length() < 5 ? "0" : partida.equals("11301") ? "0003" : "00" + partida.substring(4, 2);
                    String nivel7 = partida == null || partida.length() < 5 ? "0" : partida.equals("11301") ? "0001" : "0000";
                    switch (crsSentenciaFuncion.getInt("idVariable")) {
                        case 1:
                            valor = Double.toString(aplicaReglaARE_ant(getTotalBruto()));
                            break;
                        case 3:
                            valor = Double.toString(totalTodasRetenciones);
                            break;
                        case 4:
                            valor = Double.toString(aplicaFactorNetoNeg(aplicaReglaARE(getTotalNeto())));
                            break;
                        case 5:
                            valor = Double.toString(getTotalAntesIVA());
                            break;
                        case 6:
                            valor = Double.toString(getTotalIVA());
                            break;
                        case 9:
                            valor = getUnidad();
                            break;
                        case 10:
                            valor = getEntidad();
                            break;
                        case 11:
                            valor = getAmbito();
                            break;
                        case 12:
                            valor = tiga;
                            break;
                        case 13:
                            valor = referencia;
                            break;
                        case 14:
                            valor = getConcepto();
                            break;
                        case 15:
                            valor = getClaveProveedor();
                            break;
                        case 16:
                            valor = getCapitulo();
                            break;
                        case 18:
                            valor = Double.toString(getTotalCAS());
                            break;
                        case 19:
                            valor = null;
                            break;
                        case 21:
                            valor = getClaveConcepto();
                            break;
                        case 22:
                            valor = tigaCapitulo;
                            break;
                        case 23:
                            valor = tipoCuenta;
                            break;
                        case 24:
                            valor = ejercicio;
                            break;
                        case 25:
                            valor = programa;
                            break;
                        case 26:
                            valor = conPartida == null ? null : conPartida.substring(1);
                            break;
                        case 27:
                            valor = partidaGen == null ? null : partidaGen.substring(2);
                            break;
                        case 28:
                            valor = getCapitulo() == null ? null : getCapitulo().substring(0, 1);
                            break;
                        case 29:
                            valor = entidadAmbitoCuentaBancaria;
                            break;
                        case 30:
                            valor = unidadCuentaBancaria;
                            break;
                        case 31:
                            valor = cveret;
                            break;
                        case 32:
                            valor = mayor51;
                            break;
                        case 33:
                            valor = mayor52;
                            break;
                        case 34:
                            valor = nivel2;
                            break;
                        case 35:
                            valor = nivel6;
                            break;
                        case 36:
                            valor = nivel7;
                            break;
                    }
                    variables.put(llave, valor);
                    //System.out.println("Totales.creaHashMap "+llave+" = "+valor);
                }
            }
        } catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo creaHashMap " + e.getMessage());
            throw e;
        } //Fin catch 
        finally {
        }
        return variables;
    }

    @Override
    public HashMap creaHashMapVariable(Connection conexionContabilidad, int numForma, String pTipo) throws SQLException, Exception {
        Context context = Context.enter();
        Scriptable scope = context.initStandardObjects();
        context.getWrapFactory().setJavaPrimitiveWrap(false);
        org.mozilla.javascript.Script compiled;
        Object r = null;

        Object resultado;
        String llave;
        String valor = null;
        boolean bandera;

        HashMap variables = null;
        variables = new HashMap();
        Funcion funcion = new Funcion();

        try {
            //System.out.println("Totales.creaHashMapVariable.pTipo "+pTipo);
            crsSentenciaFuncion = funcion.select_rf_tc_FuncionesPorFormaVariable(numForma, pTipo);
            crsSentenciaFuncion.beforeFirst();
            factoriza();
            while (crsSentenciaFuncion.next()) {
                bandera = false;
                if (crsSentenciaFuncion.getString("regla_contable").equals("SIN")) {
                    bandera = true;
                } else {
                    ScriptableObject.putProperty(scope, "programa", Context.javaToJS(programa, scope));
                    ScriptableObject.putProperty(scope, "anticipo", Context.javaToJS(anticipo, scope));
                    ScriptableObject.putProperty(scope, "totalNeto", Context.javaToJS(totalNeto, scope));
                    ScriptableObject.putProperty(scope, "devengado", Context.javaToJS(banDevengado, scope));
                    ScriptableObject.putProperty(scope, "id_tipo_bene", Context.javaToJS(id_tipo_beneficiario, scope));
                    ScriptableObject.putProperty(scope, "cveret", Context.javaToJS(cveret, scope));
                    /*              System.out.println("Totales.creaHashMapVariable.programa: "+programa);
                     System.out.println("Totales.creaHashMapVariable.anticipo: "+anticipo);
                     System.out.println("Totales.creaHashMapVariable.totalNeto: "+totalNeto);
                     System.out.println("Totales.creaHashMapVariable.devengado: "+banDevengado);
                     System.out.println("Totales.creaHashMapVariable.id_tipo_bene: "+id_tipo_beneficiario);
                     System.out.println("Totales.creaHashMapVariable.cveret: "+cveret);
                     */ if (pTipo.equals("O") || pTipo.equals("N")) {
                        ScriptableObject.putProperty(scope, "conPartida", Context.javaToJS(conPartida, scope));
//              System.out.println("Totales.creaHashMapVariable.conPartida: "+conPartida);
                    } else if (pTipo.equals("P")) {
                        ScriptableObject.putProperty(scope, "partidaGen", Context.javaToJS(partidaGen, scope));
//              System.out.println("Totales.creaHashMapVariable.partidaGen: "+partidaGen);
                    } else {
                        ScriptableObject.putProperty(scope, "capitulo", Context.javaToJS(capitulo, scope));
//              System.out.println("Totales.creaHashMapVariable.capitulo: "+capitulo);
                    }
                    compiled = context.compileString(crsSentenciaFuncion.getString("regla_contable"), "defLoaf", 0, null);
                    compiled.exec(context, scope);
                    resultado = ScriptableObject.getProperty(scope, "resultado");
//              System.out.println("Totales.creaHashMapVariable."+crsSentenciaFuncion.getString("nombrevariable")+": "+crsSentenciaFuncion.getString("regla_contable"));
//              System.out.println("Resultado = "+resultado);
                    if (resultado.equals("1")) {
                        bandera = true;
                    }
                }
//            System.out.println("--Totales.creaHashMapVariable."+crsSentenciaFuncion.getString("nombrevariable"));
                if (bandera == true) {
                    //System.out.println("Totales.creaHashMapVariable."+crsSentenciaFuncion.getString("nombrevariable"));
                    llave = crsSentenciaFuncion.getString("nombreVariable");
                    switch (crsSentenciaFuncion.getInt("idVariable")) {
                        case 1:
                            valor = Double.toString(aplicaReglaARE_ant(getTotalBruto()));
                            break;
                        case 3:
                            valor = Double.toString(totalTodasRetenciones * -1.00);
                            break;
                        case 4:
                            valor = Double.toString(aplicaFactorNetoNeg(aplicaReglaARE(getTotalNeto())));
                            break;
                        case 5:
                            valor = Double.toString(getTotalAntesIVA());
                            break;
                        case 6:
                            valor = Double.toString(getTotalIVA());
                            break;
                        case 9:
                            valor = getUnidad();
                            break;
                        case 10:
                            valor = getEntidad();
                            break;
                        case 11:
                            valor = getAmbito();
                            break;
                        case 12:
                            valor = tiga;
                            break;
                        case 13:
                            valor = referencia;
                            break;
                        case 14:
                            valor = getConcepto();
                            break;
                        case 15:
                            valor = getClaveProveedor();
                            break;
                        case 16:
                            valor = getCapitulo();
                            break;
                        case 18:
                            valor = Double.toString(getTotalCAS());
                            break;
                        case 19:
                            valor = null;
                            break;
                        case 21:
                            valor = getClaveConcepto();
                            break;
                        case 22:
                            valor = tigaCapitulo;
                            break;
                        case 23:
                            valor = tipoCuenta;
                            break;
                        case 24:
                            valor = ejercicio;
                            break;
                        case 25:
                            valor = programa;
                            break;
                        case 26:
                            valor = conPartida.substring(1);
                            break;
                        case 27:
                            valor = partidaGen.substring(2);
                            break;
                        case 28:
                            valor = getCapitulo().substring(0, 1);
                            break;
                        case 29:
                            valor = entidadAmbitoCuentaBancaria;
                            break;
                        case 30:
                            valor = unidadCuentaBancaria;
                            break;
                        case 31:
                            valor = cveret;
                            break;
                    }
                    variables.put(llave, valor);
                    //System.out.println("Totales.creaHashMapVariable "+llave+" = "+valor);
                }
            }
        } catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo creaHashMapVariable " + e.getMessage());
            throw e;
        } //Fin catch 
        finally {
        }
        return variables;
    }

    @Override
    public boolean verificaProgramaBancoMundial() throws SQLException, Exception {
        boolean bancoMundial = true;
        try {
            crsHistoricoEje.beforeFirst();
            while (crsHistoricoEje.next()) {
                if (crsHistoricoEje.getString("programa").equals("0001")) {
                    bancoMundial = false;
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo verificaProgramaBancoMundial " + e.getMessage());
            throw e;
        } //Fin catch 
        finally {
        }
        return bancoMundial;
    }

    public CachedRowSet obtenFormasActivos() throws SQLException, Exception {
        Connection con2 = null;
        CachedRowSet rsQuery = null;
        StringBuilder SQL = new StringBuilder("SELECT R.ID_FORMA_ACTIVO, R.ID_BANCO, R.CUENTA, R.CUENTA_CONTABLE FROM RF_TC_FORMAS_ACTIVOS R");
        try {
            con2 = DaoFactory.getContabilidad();
            rsQuery = new CachedRowSet();

            rsQuery.setCommand(SQL.toString());
            rsQuery.execute(con2);

        } //Fin try 
        catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo TotalesSenado.obtenFormasActivos " + e.getMessage());
            System.out.println("Query: " + SQL.toString());
            throw e;
        } //Fin catch 
        finally {
            if (con2 != null) {
                con2.close();
            }
        } //Fin finally 
        return rsQuery;
    } //Fin metodo select_rf_tc_retenciones 

    public CachedRowSet obtenFormasCxpCp() throws SQLException, Exception {
        Connection con2 = null;
        CachedRowSet rsQuery = null;
        StringBuilder SQL = new StringBuilder("SELECT R.ID_FORMAS_CXP_CP, R.DESCRIPCION, R.CAPITULOS, R.TIPO_PROVEEDOR, R.CUENTA_CONTABLE FROM RF_TC_FORMAS_CXP_CP R;");
        try {
            con2 = DaoFactory.getContabilidad();
            rsQuery = new CachedRowSet();

            rsQuery.setCommand(SQL.toString());
            rsQuery.execute(con2);

        } //Fin try 
        catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo TotalesSenado.obtenFormasCxpCp " + e.getMessage());
            System.out.println("Query: " + SQL.toString());
            throw e;
        } //Fin catch 
        finally {
            if (con2 != null) {
                con2.close();
            }
        } //Fin finally 
        return rsQuery;
    } //Fin metodo select_rf_tc_retenciones 

    public CachedRowSet obtenFormasDeducciones() throws SQLException, Exception {
        Connection con2 = null;
        CachedRowSet rsQuery = null;
        StringBuilder SQL = new StringBuilder("SELECT R.ID_FORMAS_DEDUCCIONES, R.CONCEPTO, R.DESCRIPCION, R.CUENTA_CONTABLE FROM RF_RC_FORMAS_DEDUCCIONES R;");
        try {
            con2 = DaoFactory.getContabilidad();
            rsQuery = new CachedRowSet();

            rsQuery.setCommand(SQL.toString());
            rsQuery.execute(con2);

        } //Fin try 
        catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo TotalesSenado.obtenFormasDeducciones " + e.getMessage());
            System.out.println("Query: " + SQL.toString());
            throw e;
        } //Fin catch 
        finally {
            if (con2 != null) {
                con2.close();
            }
        } //Fin finally 
        return rsQuery;
    } //Fin metodo select_rf_tc_retenciones 

    public CachedRowSet obtenFormasGastos() throws SQLException, Exception {
        Connection con2 = null;
        CachedRowSet rsQuery = null;
        StringBuilder SQL = new StringBuilder("SELECT R.ID_FORMAS_GASTOS, R.PARTIDA, R.NOMBRE, R.CUENTA_CONTABLE FROM RF_TC_FORMAS_GASTOS R;");
        try {
            con2 = DaoFactory.getContabilidad();
            rsQuery = new CachedRowSet();

            rsQuery.setCommand(SQL.toString());
            rsQuery.execute(con2);

        } //Fin try 
        catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo TotalesSenado.obtenFormasGastos " + e.getMessage());
            System.out.println("Query: " + SQL.toString());
            throw e;
        } //Fin catch 
        finally {
            if (con2 != null) {
                con2.close();
            }
        } //Fin finally 
        return rsQuery;
    } //Fin metodo select_rf_tc_retenciones 

    public CachedRowSet obtenFormasPresupuestales() throws SQLException, Exception {
        Connection con2 = null;
        CachedRowSet rsQuery = null;
        StringBuilder SQL = new StringBuilder("SELECT R.ID_FORMAS_PRESUPUESTALES, R.DESCRIPCION, R.CUENTA_CONTABLE FROM RF_TC_FORMAS_PRESUPUESTALES R;");
        try {
            con2 = DaoFactory.getContabilidad();
            rsQuery = new CachedRowSet();

            rsQuery.setCommand(SQL.toString());
            rsQuery.execute(con2);

        } //Fin try 
        catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo TotalesSenado.obtenFormasPresupuestales " + e.getMessage());
            System.out.println("Query: " + SQL.toString());
            throw e;
        } //Fin catch 
        finally {
            if (con2 != null) {
                con2.close();
            }
        } //Fin finally 
        return rsQuery;
    } //Fin metodo select_rf_tc_retenciones 
}
