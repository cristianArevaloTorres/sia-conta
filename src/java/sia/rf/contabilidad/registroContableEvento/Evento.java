package sia.rf.contabilidad.registroContableEvento;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import sia.db.dao.DaoFactory;
import sia.libs.correo.Envio;
import sia.libs.recurso.Contabilidad;
import sia.rf.contabilidad.registroContable.formas.Areas;
import sia.rf.contabilidad.registroContableNuevo.bcCuentaContable;
import sia.rf.contabilidad.registroContableNuevo.bcCuentasBancarias;
import sia.rf.contabilidad.registroContableNuevo.bcProcesosCierre;
import sia.rf.contabilidad.registroContableNuevo.polizasEliminacion.ComparacionCuentas;
import sia.rf.contabilidad.sistemas.cierreAnual.PagoPasivos;
import sia.rf.contabilidad.sistemas.eliminacion.SiafmEliminacion;
import sia.rf.contabilidad.sistemas.inversiones.SiafmInversiones;
import sia.rf.contabilidad.sistemas.mipf.modPrueba.Adecuacion;
import sia.rf.contabilidad.sistemas.mipf.modPrueba.Compromiso;
import sia.rf.contabilidad.sistemas.mipf.modPrueba.OperacionFondoRotatorio;
import sia.rf.contabilidad.sistemas.mipf.transformacion.Totales;
import sia.rf.contabilidad.sistemas.mipf.transformacion.TotalesSenado;
import sia.rf.contabilidad.sistemas.tesoreria.SiafmTesoreria;
import sia.ws.publicar.contabilidad.Registro;
import sun.jdbc.rowset.CachedRowSet;

/**
 *
 * @author jorgeluis.perez
 */
public class Evento {

    private EventoContable eventoContable = null;
    private FormaContable formaContable = null;
    private Modulo modulo = null;
    private Sistema sistema = null;
    private Registro registro = null;
    private BitacoraLocal bitacoraLocal = null;
    private CachedRowSet crs = null;
    private int factor = 0;
    private int banAre = 0;
    private int banAre_ant = 0;
    private String origen = "";
    private int banPasivo = 1;
    private boolean banDevengado = false;
    private boolean banCorreo = true;

    public Evento() {
        eventoContable = new EventoContable();
        formaContable = new FormaContable();
        modulo = new Modulo();
        sistema = new Sistema();
        registro = new Registro();
        bitacoraLocal = new BitacoraLocal();
    }

    private void enviaNotitificacion(String asunto, String mensaje) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><title><head></head></title><body>");
        sb.append("<br><strong>");
        sb.append(asunto);
        sb.append("</strong><br>");
        sb.append("<br><strong>");
        sb.append(mensaje);
        sb.append("</strong><br>");
        sb.append("<br>");
        Envio.asuntoMensaje(Contabilidad.getPropiedad("notificacion.webService.emisor"), Contabilidad.getPropiedad("notificacion.webService.destinatarios.aviso"), null, "webService Contabilidad ", sb.toString(), null, true);
    }

    private void enviaNotitificacionEliminacion(String asunto, String mensaje) {
        StringBuilder sb = new StringBuilder();
        String lsDestinatariosEli = "Salvador.Reyes@senado.gob.mx, Minerva.Vidales@senado.gob.mx, carlos.bragdon@senado.gob.mx, patricia.casas@senado.gob.mx, LUZ.LOPEZ@senado.gob.mx, SUSANA.MEDINA@senado.gob.mx, JORGELUIS.PEREZ@senado.gob.mx,Yadhira.Ramos@senado.gob.mx, Bertha.Uribe@senado.gob.mx, claudia.macarioT@senado.gob.mx,"
                + "erika.martinez@senado.gob.mx,graciela.cervantes@senado.gob.mx,eduardo.peralta@senado.gob.mx,humberto.ortiz@senado.gob.mx";
        sb.append("<html><title><head></head></title><body>");
        sb.append("<br><strong>");
        sb.append(mensaje);
        sb.append("</strong><br>");
        sb.append("<br>");
        Envio.asuntoMensaje("siacontabilidad@senado.gob.mx", lsDestinatariosEli, null, asunto, sb.toString(), null, true);

    }

    private String obtenerNombreDatasource(String pEventoContable) throws SQLException, Exception {
        Connection conexion = null;
        String resultado = null;

        try {
            conexion = DaoFactory.getContabilidad();
            eventoContable.select_rf_tr_eventoContable(conexion, pEventoContable);
            modulo.select_rf_tr_modulo(conexion, eventoContable.getIdmodulo());
            sistema.select_rf_tr_sistema(conexion, modulo.getIdsistema());
            crs = formaContable.select_rf_tr_formaContable(conexion, pEventoContable);
            resultado = sistema.getCadconexion();
            banAre = eventoContable.getBanAre();
            banAre_ant = eventoContable.getBanAre_ant();
            origen = eventoContable.getOrigen();
        } catch (Exception e) {
            System.out.println("Ocurrio un errror al accesar el metodo obtenerNombreDatasource " + e.getMessage());
            throw e;
        } finally {
            if (conexion != null) {
                conexion.close();
            }
        }
        return resultado;
    }

    private Connection obtenerConexionModulo(String nombreDS) throws SQLException, Exception {
        Connection conexion = null;
        try {
            conexion = DaoFactory.getConnection(nombreDS);
        } catch (Exception e) {
            System.out.println("Ocurrio un errror al accesar el metodo obtenerConexionModulo " + nombreDS + " " + e.getMessage());
            throw e;
        }
        return conexion;
    }

    private void preparaRegistro(String cadena, String claveSeguridadWS, String fechaEnvio, String forma, String unidadEjecutora, String entidad, String ambito, String referencia, String concepto, String catalogoCuenta, String idEvento, String ejercicio) throws SQLException, Exception {
        registro.setForma(forma);
        registro.setUnidadeje("001");//unidadEjecutora);
        registro.setEntidad(entidad);
        registro.setAmbito(ambito);
        registro.setReferencia(referencia);
        registro.setConcepto(concepto);
        registro.setVariables(cadena);
        registro.setNoempleado(5137);
        registro.setClave(claveSeguridadWS);
        registro.setFecenvio(fechaEnvio);
        registro.setCatalogocuenta(catalogoCuenta);
        registro.setIdEvento(idEvento);
        registro.setEjercicio(ejercicio);
    }

    private String obtieneAmbitoCuenta(Connection conexionContabilidad, String cuenta, bcCuentasBancarias bcCuenta) throws SQLException, Exception {
        bcCuenta.select_rf_tesoreria_rf_tr_cuentas_bancarias_tipo(conexionContabilidad, cuenta, "9"); //verifica si es central
        return bcCuenta.getAmbito_cuenta();
    }

    private String obtieneTipoCuenta(String ambitoCuenta, bcCuentasBancarias bcCuenta) throws SQLException, Exception {
        if (ambitoCuenta.equals("2")) {
            if (bcCuenta.getId_tipo_cta().equals("4")) // Pagadora central de gasto corriente
            {
                return "0102";
            } else if (bcCuenta.getId_tipo_cta().equals("5")) // Pagadora central de servicio personal
            {
                return "0101";
            } else {
                return "SC00";
            }
        }
        if (ambitoCuenta.equals("5")) {//Cuenta de Bancomer
            return "1003";
        } else {
            return "SC00";
        }
    }

    private boolean verificaForma(String forma, String ambitoCuenta) throws SQLException, Exception {
        if (forma.equals("YA") || forma.equals("YB") || forma.equals("YC") || forma.equals("YH")
                || forma.equals("YK") || forma.equals("YL") || forma.equals("YJ") || forma.equals("YR")) {//formas para cancelacion
            if ((forma.equals("YB") || forma.equals("YH") || forma.equals("YK") || forma.equals("YJ")) && ambitoCuenta.equals("2")) //Usa forma 2 de MIPF FF
            {
                return true;
            } else if ((forma.equals("YC") || forma.equals("YL")) && (ambitoCuenta.equals("3") || (banAre == 1))) //Usa forma 3 de MIPF FF
            {
                return true;
            } else if ((forma.equals("YA")) && ambitoCuenta.equals("2")) //Usa forma 1 de MIPF FF
            {
                return true;
            } else if ((forma.equals("YA") || forma.equals("YH")) && ambitoCuenta.equals("5")) //Bancomer
            {
                return true;
            } else if ((forma.equals("YR")) && ambitoCuenta.equals("4")) //solo para chequera de las unidades
            {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    private boolean verificaFormaTotBruto(String forma, double totBruto) throws SQLException, Exception {
        if (forma.equals("ZL") || forma.equals("ZZ")) {
            if (totBruto == 0.0) {
                return true;
            } else {
                return false;
            }
        } else if (forma.equals("ZF") || forma.equals("ZP")) {
            if (totBruto != 0) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public String procesarEvento(int eventoContable, int eventoProceso, String parametros, String claveSeguridadWS) throws SQLException, Exception {
        String nombreDS;
        Connection conexionSistema = null;
        Connection conexionContabilidad = null;
        String cadena = null;
        String resultado = null;
        String ref = null;
        String[] arregloParametros = parametros.split("&");
        HashMap hashMap = new HashMap();

        SiafmEliminacion siafmEliminacion;
        bcCuentaContable cuentaContable;
        SiafmTesoreria siafmTesoreria;
        SiafmInversiones siafmInversiones;
        bcProcesosCierre procesosCierre = null;
        Compromiso compromiso = null;
        Adecuacion adecuacion;
        OperacionFondoRotatorio operacionFR;
        bcCuentasBancarias bcCuenta;
        Totales totales = null;
        PagoPasivos pagoPasi;
        int regs = 0;
        int liPoliza;
        String lsPolizas = "|";
        String unidad = null;
        String entidad = null;
        String ambito = null;
        String anticipo = null;
        String capitulo = null;
        String tipoCuenta = null;
        String ambitoCuenta = null;
        String tipoForma;
        String ejercicio;
        boolean polizaPresupuestal = true;
        boolean polizaFinanciera = true;
        boolean polizaAreInicioEjer = false;
        boolean OREsAnteriores = false;
        String fechaOriginal = "";
        String param5 = " ";
        String auxConcepto;
        int posicionError = 0;
        try {
            nombreDS = obtenerNombreDatasource(Integer.toString(eventoContable));
            conexionSistema = obtenerConexionModulo(nombreDS);
            conexionSistema.setAutoCommit(false);
            conexionContabilidad = DaoFactory.getContabilidad();
            conexionContabilidad.setAutoCommit(false);
            ejercicio = arregloParametros[0].substring(6);
            while (crs.next()) {
                System.out.println("Procesando Evento: " + eventoContable);
                switch (eventoContable) {
                    case 29: //Evento 29 de MIPF parametros(fecha de envio, ejercicio y numero de documento)     
                    {
                        posicionError = 0;
                        if (Integer.parseInt(arregloParametros[0].substring(6)) > Integer.parseInt(arregloParametros[1])) {
//                            String fec = "31/12/" + arregloParametros[1];
//                            arregloParametros[0] = fec;
                            ejercicio = arregloParametros[0].substring(6);
                        }
                        posicionError++;
                        TotalesSenado totalesS = new TotalesSenado();
                        totalesS.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[2] + "&" + origen + "&" + param5); //estatus, ejercicio , numero de documento, tipoCXL, origen, condicion de pago de pasivos 
                        posicionError++;
                        regs = totalesS.asignaPropiedadesCtaXLiq();
                        posicionError++;
                        if (regs == 0) {//No trajo registros el query de ctaxliq
                            cadena = "AQUI FALTA ALGO";
                        }
                        cadena = totalesS.calculaPresupComprometido(conexionContabilidad, crs.getString("forma"));
                        cadena = cadena + totalesS.calculaPresupPorEjercer(conexionContabilidad, crs.getString("forma"));
                        posicionError++;
                        if (!cadena.isEmpty()) {
                            //System.out.println("Evento.procesarEvento.cadena "+cadena);
                            totalesS.asignaPropiedadesCtaXLiq();
                            posicionError++;
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), "101", "9", "1", arregloParametros[2], totalesS.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            posicionError++;
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                            posicionError++;
                            liPoliza = resultado.indexOf("***") + 3;
                            posicionError++;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                            posicionError++;
                            hashMap.clear();
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    }
                    case 30: //Evento 30 de MIPF parametros(fecha de envio, ejercicio y numero de documento) 
                    {
                        posicionError = 0;
                        if (Integer.parseInt(arregloParametros[0].substring(6)) > Integer.parseInt(arregloParametros[1])) {
//                            String fec = "31/12/" + arregloParametros[1];
//                            arregloParametros[0] = fec;
                            ejercicio = arregloParametros[0].substring(6);
                        }
                        posicionError++;
                        //compromiso = new Compromiso();
                        //regs = compromiso.leeCompromiso(conexionSistema, arregloParametros[1], arregloParametros[2]);
                        //cadena = compromiso.registroContableCompromiso(arregloParametros[0], conexionContabilidad, registro, crs.getString("catalogocuenta"), Integer.parseInt(crs.getString("idforma")));
                        TotalesSenado totalesS = new TotalesSenado();
                        totalesS.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[2] + "&" + origen + "&" + param5); //estatus, ejercicio , numero de documento, tipoCXL, origen, condicion de pago de pasivos 
                        posicionError++;
                        regs = totalesS.asignaPropiedadesCtaXLiq();
                        posicionError++;
                        if (regs == 0) {//No trajo registros el query de ctaxliq
                            cadena = "AQUI FALTA ALGO";
                        }
                        cadena = totalesS.calculaPresupComprometido(conexionContabilidad, crs.getString("forma"));
                        cadena = cadena + totalesS.calculaPresupPorEjercer(conexionContabilidad, crs.getString("forma"));
                        posicionError++;
                        if (!cadena.isEmpty()) {
                            //System.out.println("Evento.procesarEvento.cadena "+cadena);
                            totalesS.asignaPropiedadesCtaXLiq();
                            posicionError++;
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), "101", "9", "1", arregloParametros[2], totalesS.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            posicionError++;
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                            posicionError++;
                            liPoliza = resultado.indexOf("***") + 3;
                            posicionError++;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                            posicionError++;
                            hashMap.clear();
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    }
                    case 31: //Evento 31 de MIPF adecuaciones presupuestarias(fecha de envio, ejercicio y numero de oficio
                    {
                        posicionError = 0;
                        if (Integer.parseInt(arregloParametros[0].substring(6)) > Integer.parseInt(arregloParametros[1])) {
//                            String fec = "31/12/" + arregloParametros[1];
//                            arregloParametros[0] = fec;
                            ejercicio = arregloParametros[0].substring(6);
                        }
                        posicionError++;
                        //adecuacion = new Adecuacion();
                        //cadena = adecuacion.registroContable(conexionSistema, arregloParametros[0], arregloParametros[1], arregloParametros[2]);
                        TotalesSenado totalesS = new TotalesSenado();
                        totalesS.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[2] + "&" + origen + "&" + param5); //estatus, ejercicio , numero de documento, tipoCXL, origen, condicion de pago de pasivos 
                        posicionError++;
                        regs = totalesS.asignaPropiedadesCtaXLiq();
                        posicionError++;
                        if (regs == 0) {//No trajo registros el query de ctaxliq
                            cadena = "AQUI FALTA ALGO";
                        }
                        cadena = totalesS.calculaPresupPorEjercer(conexionContabilidad, crs.getString("forma"));
                        posicionError++;
                        if (!cadena.isEmpty()) {
                            //System.out.println("Evento.procesarEvento.cadena "+cadena);
                            totalesS.asignaPropiedadesCtaXLiq();
                            posicionError++;
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), "101", "9", "1", arregloParametros[2], totalesS.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            posicionError++;
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                            posicionError++;
                            liPoliza = resultado.indexOf("***") + 3;
                            posicionError++;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                            posicionError++;
                            hashMap.clear();
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    }
                    case 101: //Evento 101 de MIPF parametros(fecha de envio,estatus,ejercicio,documento,tipo_cxl)  ejemplo: 24/04/2010&4&2010&105011CXL00022&A                  
                    {
                        if (Integer.parseInt(arregloParametros[0].substring(6)) > Integer.parseInt(arregloParametros[2])) {
//                            String fec = "31/12/" + arregloParametros[2];
//                            arregloParametros[0] = fec;
                            ejercicio = arregloParametros[0].substring(6);
                        }
                        TotalesSenado totalesS = new TotalesSenado();
                        String cxp = "";
                        totalesS.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[3] + "&" + arregloParametros[4] + "&" + origen + "&" + param5); //estatus, ejercicio , numero de documemnto, tipoCXL, origen, condicion de pago de pasivos 
                        if (totalesS.asignaPropiedadesCtaXLiq() == 0) {//No trajo registros el query de ctaxliq
                            cadena = "";
                        } else {
                            System.out.println("Evento.aqui " + totalesS.asignaPropiedadesCtaXLiq());
                            anticipo = totalesS.getAnticipo();
                            //capitulo = totalesS.getCapitulo();
                            //totalesS.setFactor(crs.getInt("factor"));
                            //totalesS.setEjercicio(arregloParametros[2]);
                            if (anticipo.equals("0")) {  // NO ES FACTURA
                                //totalesS.calculaRetencionesHistorico(); //Calcula todas las retenciones por unidad y ambito
                                //totalesS.calculaAportaciones(); //Calcula todas las retenciones por unidad y ambito
                                //totalesS.verificaProveedor(conexionContabilidad, registro, arregloParametros[0], crs.getString("catalogocuenta"), "2112", "5");
                                cadena = totalesS.calculaGasto(conexionContabilidad, crs.getString("forma"));
                                cxp = totalesS.calculaCxpCp(conexionContabilidad);
                                cadena = cadena + cxp;
                                cadena = cadena + totalesS.calculaRetenciones(conexionContabilidad, crs.getString("forma"), eventoContable);
                                cadena = cadena + totalesS.calculaPresupDevengado(conexionContabilidad, crs.getString("forma"));
                                cadena = cadena + totalesS.calculaPresupComprometido(conexionContabilidad, crs.getString("forma"));
                                cadena = cadena + totalesS.calculaPresupComprometido2(conexionContabilidad);
                                cadena = cadena + totalesS.calculaPresupPorEjercer(conexionContabilidad, crs.getString("forma"));
                                cadena = cadena + totalesS.calculaPresupPagado(conexionContabilidad, crs.getString("forma"));
                                cadena = cadena + totalesS.calculaPresupEjercido(conexionContabilidad, crs.getString("forma"));
                            }
                        }
                        System.out.println("*******************\n" + crs.getString("forma") + "\n***************");
                        if (crs.getString("forma").equals("CC") && !cxp.isEmpty()) {
                            cadena = "";
                        } else if (crs.getString("forma").equals("CA") && cxp.isEmpty()) {
                            cadena = "";
                        }
                        if (!cadena.isEmpty()) {
                            System.out.println("Evento.procesarEvento.cadena " + cadena);
                            totalesS.asignaPropiedadesCtaXLiq();
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), totalesS.getUnidad().substring(1, 4), totalesS.getEntidad(), totalesS.getAmbito().substring(3, 4), arregloParametros[3], totalesS.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            //aqui se genera la póliza
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                            liPoliza = resultado.indexOf("***") + 3;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                            hashMap.clear();
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    }
                    case 102:  //Evento 102 de MIPF parametros(fecha de envio,estatus,ejercicio,documento,tipo_cxl)  ejemplo: 24/04/2010&4&2010&105011CXL00022&A                  
                    {
                        pagoPasi = new PagoPasivos();
                        TotalesSenado totalesS = new TotalesSenado();
                        totalesS.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[3] + "&" + arregloParametros[4] + "&" + origen + "&" + param5); //estatus, ejercicio , numero de documento, tipoCXL, origen, condicion de pago de pasivos 
                        regs = totalesS.asignaPropiedadesCtaXLiq();
//                        System.out.println("aqui " + regs);
                        if (regs == 0) {//No trajo registros el query de ctaxliq
                            cadena = "";
                        } else {
                            cadena = totalesS.calculaActivos(conexionContabilidad);
                            cadena = cadena + totalesS.calculaGasto(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaRetenciones(conexionContabilidad, crs.getString("forma"), eventoContable);
                            cadena = cadena + totalesS.calculaPresupDevengado(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaPresupEjercido(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaPresupPagado(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaPresupComprometido(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaPresupPorEjercer(conexionContabilidad, crs.getString("forma"));
                        }
                        if (!cadena.isEmpty()) {
                            //System.out.println("Evento.procesarEvento.cadena "+cadena);
                            totalesS.asignaPropiedadesCtaXLiq();
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), totalesS.getUnidad().substring(1, 4), totalesS.getEntidad(), totalesS.getAmbito().substring(3, 4), arregloParametros[3], totalesS.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                            liPoliza = resultado.indexOf("***") + 3;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                            hashMap.clear();
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    }
                    case 170:  //Evento 170 de MIPF en Estatus Cancelado parametros(fecha de envio,estatus,ejercicio,documento,tipo_cxl)  ejemplo: 24/04/2010&4&2010&105011CXL00022&A                  
                        pagoPasi = new PagoPasivos();
                        if (Integer.parseInt(arregloParametros[0].substring(6)) > Integer.parseInt(arregloParametros[2])) {
                            pagoPasi.select_rf_tc_fecha_lim_pasivo(conexionContabilidad, arregloParametros[2]);
                            banPasivo = pagoPasi.select_F_COMPARA_FECHA(conexionContabilidad, arregloParametros[0], pagoPasi.getFecha_limite());
                            fechaOriginal = arregloParametros[0];
//                            String fec = "31/12/" + arregloParametros[2];
//                            arregloParametros[0] = fec;
                            ejercicio = arregloParametros[0].substring(6);
                            polizaPresupuestal = true;
                            polizaFinanciera = false;
                            polizaAreInicioEjer = true;

                        }
                        if (banPasivo == 1) {
                            if (totales == null) {
                                totales = new Totales();
                                totales.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[3] + "&" + arregloParametros[4] + "&" + origen + "&" + param5); //estatus, ejercicio , numero de documemnto, tipoCXL, origen, condicion de pago de pasivos 
                                regs = totales.asignaPropiedadesCtaXLiq();
                                if (regs == 0) {//No trajo registros el query de ctaxliq
                                    cadena = "";
                                } else {
                                    anticipo = totales.getAnticipo();
                                    capitulo = totales.getCapitulo();
                                    totales.setEjercicio(arregloParametros[2]);
                                    totales.setBanAre(banAre);
                                    bcCuenta = new bcCuentasBancarias();
                                    ambitoCuenta = obtieneAmbitoCuenta(conexionContabilidad, totales.getCuenta(), bcCuenta);
                                    tipoCuenta = obtieneTipoCuenta(ambitoCuenta, bcCuenta);
                                    totales.calculaRetencionesHistorico(); //Calcula todas las retenciones por unidad y ambito
                                    totales.calculaAportaciones(); //Calcula todas las retenciones por unidad y ambito
                                }
                            }
                            tipoForma = crs.getString("tipo");
                            if ((tipoForma.equals("P") && polizaPresupuestal && (!(crs.getString("forma").equals("ZU") || crs.getString("forma").equals("ZV") || crs.getString("forma").equals("ZW")))) || (polizaAreInicioEjer && (crs.getString("forma").equals("ZU") || crs.getString("forma").equals("ZV") || crs.getString("forma").equals("ZW")))) {
                                if (regs != 0) { //No trajo registros el query de ctaxliq
                                    if (crs.getString("forma").equals("ZW")) {
                                        arregloParametros[0] = fechaOriginal;
                                        ejercicio = arregloParametros[0].substring(6);
                                    }
                                    if (anticipo.equals("0")) {  // NO ES FACTURA    
                                        totales.setFactor(crs.getInt("factor"));
                                        totales.setFactorNetoNeg(crs.getInt("FactorNetoNeg"));
                                        cadena = totales.calculaTotalBrutoHisEjeCapitulo(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "C");
                                        cadena = cadena + totales.calculaTotalBrutoHisEje(conexionContabilidad, Integer.parseInt(crs.getString("idforma")));  //Recorre histeje para calcular total bruto, total neto y cas 
                                        cadena = cadena + totales.calculaTotalBrutoHisEjePartidaGen(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "P");
                                        cadena = cadena + totales.recorreTotalRetenciones();  // Recorre todas las retenciones para la forma               
                                        cadena = cadena + totales.recorreTotalAportaciones();  // Recorre todas las retenciones para la forma               
                                        if (totales.isTotalNetoCero() == false && (crs.getString("forma").equals("ZU") || crs.getString("forma").equals("ZV") || crs.getString("forma").equals("ZW"))) {
                                            cadena = "";
                                        }
                                    }
                                }
                            } else if (tipoForma.equals("F") && ((polizaFinanciera && (!(crs.getString("forma").equals("YI") || crs.getString("forma").equals("YQ")))) || (polizaAreInicioEjer && (crs.getString("forma").equals("YI") || crs.getString("forma").equals("YQ"))))) {//Procesa formas financieras                 
                                cadena = "";
                                if (crs.getString("forma").equals("YQ")) {
                                    arregloParametros[0] = fechaOriginal;
                                    ejercicio = arregloParametros[0].substring(6);
                                }
                                if (verificaForma(crs.getString("forma"), ambitoCuenta)) {
                                    totales.setFactor(crs.getInt("factor"));
                                    totales.setFactorNetoNeg(crs.getInt("FactorNetoNeg"));
                                    totales.setEjercicio(arregloParametros[2]);
                                    totales.setTotalNetoCero(false);
                                    cadena = cadena + totales.generaFormaFinaciera(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), ambitoCuenta, tipoCuenta);  //Recorre histeje para calcular total bruto, total neto y cas 
                                    cadena = cadena + totales.calculaTotalBrutoHisEjeCapitulo(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "C");
                                    if (totales.isTotalNetoCero() == false) {
                                        cadena = "";
                                    }
                                }
                            } else {
                                cadena = "";
                            }
                        } else {
                            cadena = "";
                        }
                        if (!cadena.isEmpty()) {
                            //System.out.println("Evento.procesarEvento.cadena "+cadena);
                            totales.asignaPropiedadesCtaXLiq();
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), totales.getUnidad().substring(1, 4), totales.getEntidad(), totales.getAmbito().substring(3, 4), arregloParametros[3], totales.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                            liPoliza = resultado.indexOf("***") + 3;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                            hashMap.clear();
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    case 103:  //Evento 103 de MIPF parametros(fecha de envio,estatus,ejercicio,documento,tipo_cxl)  ejemplo: 24/04/2010&4&2010&105011CXL00022&A                  
                    case 104:  //Evento 104 de MIPF parametros(fecha de envio,estatus,ejercicio,documento,tipo_cxl)  ejemplo: 24/04/2010&4&2010&105011CXL00022&A                  
                    {
                        if (Integer.parseInt(arregloParametros[0].substring(6)) > Integer.parseInt(arregloParametros[2])) {
//                            String fec = "31/12/" + arregloParametros[2];
//                            arregloParametros[0] = fec;
                            ejercicio = arregloParametros[0].substring(6);
                        }
                        TotalesSenado totalesS = new TotalesSenado();
                        totalesS.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[3] + "&" + arregloParametros[4] + "&" + origen + "&" + param5); //estatus, ejercicio , numero de documento, tipoCXL, origen, condicion de pago de pasivos 
                        regs = totalesS.asignaPropiedadesCtaXLiq();
                        if (regs == 0) {//No trajo registros el query de ctaxliq
                            cadena = "";
                        } else {
                            cadena = totalesS.calculaGasto(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaRetenciones(conexionContabilidad, crs.getString("forma"), eventoContable);
                            cadena = cadena + totalesS.calculaPresupPorEjercer(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaPresupComprometido(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaPresupDevengado(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaPresupEjercido(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaPresupPagado(conexionContabilidad, crs.getString("forma"));
//                                cadena = cadena + totalesS.calculaDivisas(conexionContabilidad);
                        }
                        if (!cadena.isEmpty()) {
                            //System.out.println("Evento.procesarEvento.cadena "+cadena);
                            totalesS.asignaPropiedadesCtaXLiq();
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), totalesS.getUnidad().substring(1, 4), totalesS.getEntidad(), totalesS.getAmbito().substring(3, 4), arregloParametros[3], totalesS.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                            liPoliza = resultado.indexOf("***") + 3;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                            hashMap.clear();
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    }
                    case 105: //Evento 105 de MIPF parametros(fecha de envio,estatus,ejercicio,documento,tipo_cxl)  ejemplo: 24/04/2010&4&2010&105011CXL00022&A                  
                    {
                        if (Integer.parseInt(arregloParametros[0].substring(6)) > Integer.parseInt(arregloParametros[2])) {
//                            String fec = "31/12/" + arregloParametros[2];
//                            arregloParametros[0] = fec;
                            ejercicio = arregloParametros[0].substring(6);
                        }
                        TotalesSenado totalesS = new TotalesSenado();
                        totalesS.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[3] + "&" + arregloParametros[4] + "&" + origen + "&" + param5); //estatus, ejercicio , numero de documento, tipoCXL, origen, condicion de pago de pasivos 
                        regs = totalesS.asignaPropiedadesCtaXLiq();
                        if (regs == 0) {//No trajo registros el query de ctaxliq
                            cadena = "";
                        } else {
                            cadena = totalesS.calculaRetenciones(conexionContabilidad, crs.getString("forma"), eventoContable);
                            cadena = cadena + totalesS.calculaAportaciones(conexionContabilidad);
                            cadena = cadena + totalesS.calculaGasto(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaCxpCp(conexionContabilidad);
                            cadena = cadena + totalesS.calculaPresupPorEjercer(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaPresupComprometido2(conexionContabilidad);
                            cadena = cadena + totalesS.calculaPresupComprometido(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaPresupDevengado(conexionContabilidad, crs.getString("forma"));
                        }
                        if (!cadena.isEmpty()) {
                            //System.out.println("Evento.procesarEvento.cadena "+cadena);
                            totalesS.asignaPropiedadesCtaXLiq();
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), totalesS.getUnidad().substring(1, 4), totalesS.getEntidad(), totalesS.getAmbito().substring(3, 4), arregloParametros[3], totalesS.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                            liPoliza = resultado.indexOf("***") + 3;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                            hashMap.clear();
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    }
                    case 106:  //Evento 106 de MIPF parametros(fecha de envio,estatus,ejercicio,documento,tipo_cxl)  ejemplo: 24/04/2010&4&2010&105011CXL00022&A                  
                        pagoPasi = new PagoPasivos();
                        if (Integer.parseInt(arregloParametros[0].substring(6)) > Integer.parseInt(arregloParametros[2])) {
                            pagoPasi.select_rf_tc_fecha_lim_pasivo(conexionContabilidad, arregloParametros[2]);
                            banPasivo = pagoPasi.select_F_COMPARA_FECHA(conexionContabilidad, arregloParametros[0], pagoPasi.getFecha_limite());
                            fechaOriginal = arregloParametros[0];
//                            String fec = "31/12/" + arregloParametros[2];
//                            arregloParametros[0] = fec;
                            ejercicio = arregloParametros[0].substring(6);
                            polizaPresupuestal = true;
                            polizaFinanciera = false;
                            polizaAreInicioEjer = true;
                        }
                        if (banPasivo == 1) {
                            if (totales == null) {
                                totales = new Totales();
                                totales.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[3] + "&" + arregloParametros[4] + "&" + origen + "&" + param5); //estatus, ejercicio , numero de documemnto, tipoCXL, origen, condicion de pago de pasivos 
                                regs = totales.asignaPropiedadesCtaXLiq();
                                if (regs == 0) {//No trajo registros el query de ctaxliq
                                    cadena = "";
                                } else {
                                    anticipo = totales.getAnticipo();
                                    capitulo = totales.getCapitulo();
                                    totales.setEjercicio(arregloParametros[2]);
                                    totales.setBanAre(banAre);
                                    bcCuenta = new bcCuentasBancarias();
                                    ambitoCuenta = obtieneAmbitoCuenta(conexionContabilidad, totales.getCuenta(), bcCuenta);
                                    tipoCuenta = obtieneTipoCuenta(ambitoCuenta, bcCuenta);
                                    totales.calculaRetencionesHistorico(); //Calcula todas las retenciones por unidad y ambito
                                    totales.calculaAportaciones(); //Calcula todas las retenciones por unidad y ambito                
                                }
                            }
                            tipoForma = crs.getString("tipo");
                            if ((tipoForma.equals("P") && polizaPresupuestal && (!(crs.getString("forma").equals("ZU") || crs.getString("forma").equals("ZV") || crs.getString("forma").equals("ZW")))) || (polizaAreInicioEjer && (crs.getString("forma").equals("ZU") || crs.getString("forma").equals("ZV") || crs.getString("forma").equals("ZW")))) {
                                if (regs != 0) { //No trajo registros el query de ctaxliq
                                    if (crs.getString("forma").equals("ZW")) {
                                        arregloParametros[0] = fechaOriginal;
                                        ejercicio = arregloParametros[0].substring(6);
                                    }
                                    if (anticipo.equals("0")) {  // NO ES FACTURA                     
                                        totales.setFactor(crs.getInt("factor"));
                                        totales.setFactorNetoNeg(crs.getInt("FactorNetoNeg"));
                                        cadena = totales.calculaTotalBrutoHisEjeCapitulo(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "C");
                                        totales.setTotalBrutoCero(false);
                                        cadena = cadena + totales.calculaTotalBrutoHisEje(conexionContabilidad, Integer.parseInt(crs.getString("idforma")));  //Recorre histeje para calcular total bruto, total neto y cas 
                                        cadena = cadena + totales.calculaTotalBrutoHisEjePartidaGen(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "P");
                                        cadena = cadena + totales.recorreTotalRetenciones();  // Recorre todas las retenciones para la forma               
                                        cadena = cadena + totales.recorreTotalAportaciones();  // Recorre todas las retenciones para la forma                                     
                   /*  if (totales.isTotalBrutoCero()){  
                                         enviaNotitificacion("Aviso del Sistema de Contabilidad Armonizado se invoco el evento "+eventoContable+" para su revisi�n, no se genero poliza presupuestal, por tener importe bruto diferente de 0. ","El aviso de reintegro es: "+arregloParametros[3]);                  
                                         cadena="";                         
                                         }*/ //Se quita esta condicion en sep 2013 a petici�n del usuario.
                                    }
                                }
                            } else if (tipoForma.equals("F") && ((polizaFinanciera && (!(crs.getString("forma").equals("YI") || crs.getString("forma").equals("YQ")))) || (polizaAreInicioEjer && (crs.getString("forma").equals("YI") || crs.getString("forma").equals("YQ"))))) {//Procesa formas financieras   
                                cadena = "";
                                if (crs.getString("forma").equals("YQ")) {
                                    arregloParametros[0] = fechaOriginal;
                                    ejercicio = arregloParametros[0].substring(6);
                                }
                                totales.setFactor(crs.getInt("factor"));
                                totales.setFactorNetoNeg(crs.getInt("FactorNetoNeg"));
                                totales.setTotalNetoCero(false);
                                cadena = cadena + totales.generaFormaFinaciera(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), ambitoCuenta, tipoCuenta);  //Recorre histeje para calcular total bruto, total neto y cas 
                                cadena = cadena + totales.calculaTotalBrutoHisEjeCapitulo(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "C");
                                if (totales.isTotalNetoCero() == false) {
                                    cadena = "";
                                }
                            } else {
                                cadena = "";
                            }
                        } else {
                            cadena = "";
                        }
                        if (!cadena.isEmpty()) {
                            //System.out.println("Evento.procesarEvento.cadena "+cadena);
                            totales.asignaPropiedadesCtaXLiq();
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), totales.getUnidad().substring(1, 4), totales.getEntidad(), totales.getAmbito().substring(3, 4), arregloParametros[3], totales.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                            liPoliza = resultado.indexOf("***") + 3;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                            hashMap.clear();
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    case 107:  //Evento 107 de MIPF parametros(fecha de envio,estatus,ejercicio,documento,tipo_cxl)  ejemplo: 24/04/2010&4&2010&105011CXL00022&A                  
                    case 108:  //Evento 108 de MIPF parametros(fecha de envio,estatus,ejercicio,documento,tipo_cxl)  ejemplo: 24/04/2010&4&2010&105011CXL00022&A                  
                        pagoPasi = new PagoPasivos();
                        if (Integer.parseInt(arregloParametros[0].substring(6)) > Integer.parseInt(arregloParametros[2])) {
                            pagoPasi.select_rf_tc_fecha_lim_pasivo(conexionContabilidad, arregloParametros[2]);
                            banPasivo = pagoPasi.select_F_COMPARA_FECHA(conexionContabilidad, arregloParametros[0], pagoPasi.getFecha_limite());
                            fechaOriginal = arregloParametros[0];
//                            String fec = "31/12/" + arregloParametros[2];
//                            arregloParametros[0] = fec;
                            ejercicio = arregloParametros[0].substring(6);
                            if (Integer.parseInt(arregloParametros[2]) <= 2013) {
                                arregloParametros[0] = fechaOriginal;
                                ejercicio = arregloParametros[0].substring(6);
                                if (crs.getString("forma").equals("ZZ")) {
                                    banPasivo = 1;
                                    OREsAnteriores = true;
                                    param5 = "--";
                                }
                            }
                        }
                        if (banPasivo == 1) {
                            if (totales == null) {
                                totales = new Totales();
                                totales.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[3] + "&" + arregloParametros[4] + "&" + origen + "&" + param5); //estatus, ejercicio , numero de documemnto, tipoCXL, origen, condicion de pago de pasivos 
                                regs = totales.asignaPropiedadesCtaXLiq();
                                if (regs != 0) {//trajo registros el query de ctaxliq
                                    anticipo = totales.getAnticipo();
                                    capitulo = totales.getCapitulo();
                                    totales.setEjercicio(arregloParametros[2]);
                                    bcCuenta = new bcCuentasBancarias();
                                    ambitoCuenta = obtieneAmbitoCuenta(conexionContabilidad, totales.getCuenta(), bcCuenta);
                                    tipoCuenta = obtieneTipoCuenta(ambitoCuenta, bcCuenta);
                                    totales.calculaRetencionesHistorico(); //Calcula todas las retenciones por unidad y ambito
                                    totales.calculaAportaciones(); //Calcula todas las retenciones por unidad y ambito
                                }
                            }
                            if (crs.getString("tipo").equals("P") && (regs != 0)) {
                                totales.setFactor(crs.getInt("factor"));
                                totales.setFactorNetoNeg(crs.getInt("FactorNetoNeg"));
                                cadena = totales.calculaTotalBrutoHisEjeCapitulo(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "C");
                                cadena = cadena + totales.calculaTotalBrutoHisEje(conexionContabilidad, Integer.parseInt(crs.getString("idforma")));  //Recorre histeje para calcular total bruto, total neto y cas 
                                cadena = cadena + totales.calculaTotalBrutoHisEjeConPartida(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "O");
                                cadena = cadena + totales.calculaTotalBrutoHisEjePartidaGen(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "P");
                                cadena = cadena + totales.recorreTotalRetenciones();  // Recorre todas las retenciones para la forma               
                                cadena = cadena + totales.recorreTotalAportaciones();  // Recorre todas las retenciones para la forma                                    
                                if (!verificaFormaTotBruto(crs.getString("forma"), totales.getTotalBruto())) {
                                    cadena = "";
                                }
                                if (crs.getString("forma").equals("ZZ") && (OREsAnteriores == false)) {
                                    cadena = "";
                                }
                            } else if (crs.getString("tipo").equals("F") && (regs != 0)) {
                                cadena = "";
                                if (verificaForma(crs.getString("forma"), ambitoCuenta)) {
                                    totales.setFactor(crs.getInt("factor"));
                                    totales.setFactorNetoNeg(crs.getInt("FactorNetoNeg"));
                                    totales.setTotalNetoCero(false);
                                    cadena = cadena + totales.generaFormaFinaciera(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), ambitoCuenta, tipoCuenta);  //Recorre histeje para calcular total bruto, total neto y cas 
                                    cadena = cadena + totales.calculaTotalBrutoHisEjeConPartida(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "O");
                                    if ((totales.getAnticipo().equals("1")) && (totales.getCapitulo().equals("5000"))) {
                                        totales.setBanProv(1);
                                    }
                                    totales.verificaProveedor(conexionContabilidad, registro, arregloParametros[0], crs.getString("catalogocuenta"), "2112", "5");
                                    cadena = cadena + totales.calculaTotalBrutoHisEjeCapitulo(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "C");
                                    if (totales.isTotalNetoCero() == false) {
                                        cadena = "";
                                    }
                                }
                            } else {
                                cadena = "";
                            }
                        } else {
                            cadena = "";
                            if (Integer.parseInt(arregloParametros[2]) > 2013) {
                                enviaNotitificacion("Aviso del Sistema de Contabilidad Armonizado se invoco el evento " + eventoContable + " para su revisi�n, no se genero poliza presupuestal, debido a que la fecha de afectaci�n es mayor a la fecha l�mite del ejercicio actual.", "La Oficio de Rectificaci�n es: " + arregloParametros[3]);
                            }
                        }
                        if (!cadena.isEmpty()) {
                            //System.out.println("Evento.procesarEvento.cadena "+cadena);
                            totales.asignaPropiedadesCtaXLiq();
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), totales.getUnidad().substring(1, 4), totales.getEntidad(), totales.getAmbito().substring(3, 4), arregloParametros[3], totales.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                            liPoliza = resultado.indexOf("***") + 3;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                            hashMap.clear();
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    case 109: //Evento 109 de MIPF parametros(fecha de envio,estatus,ejercicio,documento,tipo_cxl)  ejemplo: 24/04/2010&4&2010&105011CXL00022&A                  
                    {
                        if (Integer.parseInt(arregloParametros[0].substring(6)) > Integer.parseInt(arregloParametros[2])) {
//                            String fec = "31/12/" + arregloParametros[2];
//                            arregloParametros[0] = fec;
                            ejercicio = arregloParametros[0].substring(6);
                        }
                        TotalesSenado totalesS = new TotalesSenado();
                        String cxp = "";
                        posicionError++;
                        totalesS.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[3] + "&" + arregloParametros[4] + "&" + origen + "&" + param5); //estatus, ejercicio , numero de documento, tipoCXL, origen, condicion de pago de pasivos 
                        posicionError++;
                        if (totalesS.asignaPropiedadesCtaXLiq() == 0) {//No trajo registros el query de ctaxliq
                            cadena = "";
                        } else {
                        posicionError++;
                            anticipo = totalesS.getAnticipo();
                        posicionError++;
//                            capitulo = totales.getCapitulo();
                            totalesS.setFactor(crs.getInt("factor"));
                        posicionError++;
//                            totales.setEjercicio(arregloParametros[2]);
                            System.out.println("Evento.procesarEvento.109.anticipo " + anticipo);
                        posicionError++;
                            cadena = totalesS.calculaGasto(conexionContabilidad, crs.getString("forma"));
                        posicionError++;
                            cxp = totalesS.calculaCxpCp(conexionContabilidad);
                        posicionError++;
                            cadena = cadena + cxp;
                        posicionError++;
                            cadena = cadena + totalesS.calculaRetenciones(conexionContabilidad, crs.getString("forma"), eventoContable);
                        posicionError++;
                            cadena = cadena + totalesS.calculaPresupDevengado(conexionContabilidad, crs.getString("forma"));
                        posicionError++;
                            cadena = cadena + totalesS.calculaPresupComprometido(conexionContabilidad, crs.getString("forma"));
                        posicionError++;
                            cadena = cadena + totalesS.calculaPresupComprometido2(conexionContabilidad);
                        posicionError++;
                            cadena = cadena + totalesS.calculaPresupPorEjercer(conexionContabilidad, crs.getString("forma"));
                        posicionError++;
                            cadena = cadena + totalesS.calculaPresupPagado(conexionContabilidad, crs.getString("forma"));
                        posicionError++;
                            cadena = cadena + totalesS.calculaPresupEjercido(conexionContabilidad, crs.getString("forma"));
                        posicionError++;
                        }
                        //System.out.println("*******************\n" + crs.getString("forma") + "\n***************");
                        if (crs.getString("forma").equals("CC") && !cxp.isEmpty()) {
                            cadena = "";
                        } else if (crs.getString("forma").equals("CA") && cxp.isEmpty()) {
                            cadena = "";
                        }
                        if (!cadena.isEmpty()) {
                            //System.out.println("Evento.procesarEvento.cadena "+cadena);
                            totalesS.asignaPropiedadesCtaXLiq();
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), totalesS.getUnidad().substring(1, 4), totalesS.getEntidad(), totalesS.getAmbito().substring(3, 4), arregloParametros[3], totalesS.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                            liPoliza = resultado.indexOf("***") + 3;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                            hashMap.clear();
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    }
                    case 110: //Evento 110 de MIPF parametros(fecha de envio, estatus,ejercicio, documento, tipo_ar) ejemplo: 24/10/2004&4&2010&10912109011ARE00351&D                                               
                    {
                        pagoPasi = new PagoPasivos();
                        TotalesSenado totalesS = new TotalesSenado();
                        totalesS.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[3] + "&" + arregloParametros[4] + "&" + origen + "&" + param5); //estatus, ejercicio , numero de documento, tipoCXL, origen, condicion de pago de pasivos 
                        regs = totalesS.asignaPropiedadesCtaXLiq();
//                        System.out.println("aqui " + regs);
                        if (regs == 0) {//No trajo registros el query de ctaxliq
                            cadena = "";
                        } else {
                            cadena = totalesS.calculaActivos(conexionContabilidad);
                            cadena = cadena + totalesS.calculaGasto(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaRetenciones(conexionContabilidad, crs.getString("forma"), eventoContable);
                            cadena = cadena + totalesS.calculaPresupDevengado(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaPresupEjercido(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaPresupPagado(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaPresupComprometido(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaPresupPorEjercer(conexionContabilidad, crs.getString("forma"));
                        }
                        if (!cadena.isEmpty()) {
                            //System.out.println("Evento.procesarEvento.cadena "+cadena);
                            totalesS.asignaPropiedadesCtaXLiq();
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), totalesS.getUnidad().substring(1, 4), totalesS.getEntidad(), totalesS.getAmbito().substring(3, 4), arregloParametros[3], totalesS.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                            liPoliza = resultado.indexOf("***") + 3;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                            hashMap.clear();
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    }
                    case 111:  //Evento 111 de MIPF parametros(fecha de envio, estatus,ejercicio, documento, tipo_ar) ejemplo: 24/10/2004&4&2010&10912109011ARE00351&D                   
                    case 112:  //Evento 112 de MIPF parametros(fecha de envio, estatus,ejercicio, documento, tipo_ar) ejemplo: 24/10/2004&4&2010&10912109011ARE00351&D                              
                    {
                        if (Integer.parseInt(arregloParametros[0].substring(6)) > Integer.parseInt(arregloParametros[2])) {
//                            String fec = "31/12/" + arregloParametros[2];
//                            arregloParametros[0] = fec;
                            ejercicio = arregloParametros[0].substring(6);
                        }
                        TotalesSenado totalesS = new TotalesSenado();
                        totalesS.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[3] + "&" + arregloParametros[4] + "&" + origen + "&" + param5); //estatus, ejercicio , numero de documento, tipoCXL, origen, condicion de pago de pasivos 
                        regs = totalesS.asignaPropiedadesCtaXLiq();
                        if (regs == 0) {//No trajo registros el query de ctaxliq
                            cadena = "";
                        } else {
                            cadena = totalesS.calculaGasto(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaRetenciones(conexionContabilidad, crs.getString("forma"), eventoContable);
                            cadena = cadena + totalesS.calculaPresupPorEjercer(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaPresupComprometido(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaPresupDevengado(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaPresupEjercido(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaPresupPagado(conexionContabilidad, crs.getString("forma"));
//                                cadena = cadena + totalesS.calculaDivisas(conexionContabilidad);
                        }
                        if (!cadena.isEmpty()) {
                            //System.out.println("Evento.procesarEvento.cadena "+cadena);
                            totalesS.asignaPropiedadesCtaXLiq();
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), totalesS.getUnidad().substring(1, 4), totalesS.getEntidad(), totalesS.getAmbito().substring(3, 4), arregloParametros[3], totalesS.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                            liPoliza = resultado.indexOf("***") + 3;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                            hashMap.clear();
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    }
                    case 113: //Evento 113 de MIPF parametros(fecha de envio, estatus,ejercicio, documento, tipo_ar) ejemplo: 24/10/2004&4&2010&10912109011ARE00351&D                   
                    {
                        if (Integer.parseInt(arregloParametros[0].substring(6)) > Integer.parseInt(arregloParametros[2])) {
//                            String fec = "31/12/" + arregloParametros[2];
//                            arregloParametros[0] = fec;
                            ejercicio = arregloParametros[0].substring(6);
                        }
                        cadena = "";
                        TotalesSenado totalesS = new TotalesSenado();
                        totalesS.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[3] + "&" + arregloParametros[4] + "&" + origen + "&" + param5); //estatus, ejercicio , numero de documento, tipoCXL, origen, condicion de pago de pasivos 
                        if (totalesS.asignaPropiedadesCtaXLiq() > 0) {//trajo registros el query de ctaxliq
//                            anticipo = totalesS.getAnticipo();
//                            capitulo = totales.getCapitulo();
//                            totalesS.setFactor(crs.getInt("factor"));
//                            totales.setEjercicio(arregloParametros[2]);
                            System.out.println("Evento.procesarEvento.113.anticipo " + anticipo);
                            cadena = totalesS.calculaCxpCp(conexionContabilidad);
                            cadena = cadena + totalesS.calculaDeudores(conexionContabilidad);
                            cadena = cadena + totalesS.calculaPresupDevengado(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaPresupComprometido(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaPresupPorEjercer(conexionContabilidad, crs.getString("forma"));
                        }
                        if (!cadena.isEmpty()) {
                            System.out.println("Evento.procesarEvento.cadena " + cadena);
                            totalesS.asignaPropiedadesCtaXLiq();
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), totalesS.getUnidad().substring(1, 4), totalesS.getEntidad(), totalesS.getAmbito().substring(3, 4), arregloParametros[3], totalesS.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                            liPoliza = resultado.indexOf("***") + 3;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                            hashMap.clear();
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    }
                    case 114: //Evento 114 de MIPF parametros(fecha de envio, estatus,ejercicio, documento, tipo_ar) ejemplo: 24/10/2004&4&2010&10912109011ARE00351&D                               
                    {
                        pagoPasi = new PagoPasivos();
                        if (Integer.parseInt(arregloParametros[0].substring(6)) > Integer.parseInt(arregloParametros[2])) {
                            pagoPasi.select_rf_tc_fecha_lim_pasivo(conexionContabilidad, arregloParametros[2]);
                            banPasivo = pagoPasi.select_F_COMPARA_FECHA(conexionContabilidad, arregloParametros[0], pagoPasi.getFecha_limite());
//                            String fec = "31/12/" + arregloParametros[2];
//                            arregloParametros[0] = fec;
                            ejercicio = arregloParametros[0].substring(6);
                        }
                        cadena = "";
                        TotalesSenado totalesS = new TotalesSenado();
                        totalesS.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[3] + "&" + arregloParametros[4] + "&" + origen + "&" + param5); //estatus, ejercicio , numero de documento, tipoCXL, origen, condicion de pago de pasivos 
                        if (totalesS.asignaPropiedadesCtaXLiq() > 0) {//trajo registros el query de ctaxliq
//                            anticipo = totalesS.getAnticipo();
//                            capitulo = totales.getCapitulo();
//                            totalesS.setFactor(crs.getInt("factor"));
//                            totales.setEjercicio(arregloParametros[2]);
                            System.out.println("Evento.procesarEvento.114 ");
                            cadena = totalesS.calculaGasto(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaDeudores(conexionContabilidad);
                            cadena = cadena + totalesS.calculaRetenciones(conexionContabilidad, crs.getString("forma"), eventoContable);
                            cadena = cadena + totalesS.calculaPresupPagado(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaPresupDevengado(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaPresupEjercido(conexionContabilidad, crs.getString("forma"));
                        }
                        if (!cadena.isEmpty()) {
                            //System.out.println("Evento.procesarEvento.cadena "+cadena);
                            totalesS.asignaPropiedadesCtaXLiq();
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), totalesS.getUnidad().substring(1, 4), totalesS.getEntidad(), totalesS.getAmbito().substring(3, 4), arregloParametros[3], totalesS.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                            liPoliza = resultado.indexOf("***") + 3;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                            hashMap.clear();
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    }
                    case 115: //Evento 115 de MIPF parametros(fecha de envio, estatus,ejercicio, documento, tipo_ar) ejemplo: 24/10/2004&4&2010&10912109011ARE00351&D                   
                    {
                        pagoPasi = new PagoPasivos();
                        if (Integer.parseInt(arregloParametros[0].substring(6)) > Integer.parseInt(arregloParametros[2])) {
                            pagoPasi.select_rf_tc_fecha_lim_pasivo(conexionContabilidad, arregloParametros[2]);
                            banPasivo = pagoPasi.select_F_COMPARA_FECHA(conexionContabilidad, arregloParametros[0], pagoPasi.getFecha_limite());
                            fechaOriginal = arregloParametros[0];
//                            String fec = "31/12/" + arregloParametros[2];
//                            arregloParametros[0] = fec;
                            ejercicio = arregloParametros[0].substring(6);
                            polizaPresupuestal = true;
                            polizaFinanciera = false;
                            polizaAreInicioEjer = true;
                        }
                        TotalesSenado totalesS = new TotalesSenado();
                        totalesS.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[3] + "&" + arregloParametros[4] + "&" + origen + "&" + param5); //estatus, ejercicio , numero de documento, tipoCXL, origen, condicion de pago de pasivos 
                        regs = totalesS.asignaPropiedadesCtaXLiq();
                        if (regs == 0) {//No trajo registros el query de ctaxliq
                            cadena = "";
                        } else {
                            cadena = totalesS.calculaActivos(conexionContabilidad);
                            cadena = cadena + totalesS.calculaPresupDevengado(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaPresupComprometido(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaDeudores(conexionContabilidad);
                            cadena = cadena + totalesS.calculaPresupPorEjercer(conexionContabilidad, crs.getString("forma"));
                        }
                        if (!cadena.isEmpty()) {
                            //System.out.println("Evento.procesarEvento.cadena "+cadena);
                            totalesS.asignaPropiedadesCtaXLiq();
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), totalesS.getUnidad().substring(1, 4), totalesS.getEntidad(), totalesS.getAmbito().substring(3, 4), arregloParametros[3], totalesS.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                            liPoliza = resultado.indexOf("***") + 3;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                            hashMap.clear();
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    }
                   case 116:  //Evento 116 de MIPF parametros(fecha de envio,estatus,ejercicio,documento,tipo_cxl)  ejemplo: 24/04/2010&4&2010&105011CXL00022&A                  
                    {
                        pagoPasi = new PagoPasivos();
                        TotalesSenado totalesS = new TotalesSenado();
                        totalesS.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[3] + "&" + arregloParametros[4] + "&" + origen + "&" + param5); //estatus, ejercicio , numero de documento, tipoCXL, origen, condicion de pago de pasivos 
                        regs = totalesS.asignaPropiedadesCtaXLiq();
//                        System.out.println("aqui " + regs);
                        if (regs == 0) {//No trajo registros el query de ctaxliq
                            cadena = "";
                        } else {
                            cadena = totalesS.calculaActivos(conexionContabilidad);
                            cadena = cadena + totalesS.calculaGasto(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaRetenciones(conexionContabilidad, crs.getString("forma"), eventoContable);
                            cadena = cadena + totalesS.calculaPresupDevengado(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaPresupEjercido(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaPresupPagado(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaPresupComprometido(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaPresupPorEjercer(conexionContabilidad, crs.getString("forma"));
                        }
                        if (!cadena.isEmpty()) {
                            //System.out.println("Evento.procesarEvento.cadena "+cadena);
                            totalesS.asignaPropiedadesCtaXLiq();
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), totalesS.getUnidad().substring(1, 4), totalesS.getEntidad(), totalesS.getAmbito().substring(3, 4), arregloParametros[3], totalesS.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                            liPoliza = resultado.indexOf("***") + 3;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                            hashMap.clear();
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    }
                    case 117: //Evento 117 de MIPF parametros(fecha de envio, estatus,ejercicio, documento, tipo_ar) ejemplo: 24/10/2004&4&2010&10912109011ARE00351&D                   
                    {
                        if (Integer.parseInt(arregloParametros[0].substring(6)) > Integer.parseInt(arregloParametros[2])) {
//                            String fec = "31/12/" + arregloParametros[2];
//                            arregloParametros[0] = fec;
                            ejercicio = arregloParametros[0].substring(6);
                        }
                        TotalesSenado totalesS = new TotalesSenado();
                        totalesS.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[3] + "&" + arregloParametros[4] + "&" + origen + "&" + param5); //estatus, ejercicio , numero de documento, tipoCXL, origen, condicion de pago de pasivos 
                        regs = totalesS.asignaPropiedadesCtaXLiq();
                        if (regs == 0) {//No trajo registros el query de ctaxliq
                            cadena = "";
                        } else {
                            cadena = totalesS.calculaGasto(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaRetenciones(conexionContabilidad, crs.getString("forma"), eventoContable);
                            cadena = cadena + totalesS.calculaPresupPorEjercer(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaPresupComprometido(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaPresupDevengado(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaPresupEjercido(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaPresupPagado(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaDivisas(conexionContabilidad);
                        }
                        if (!cadena.isEmpty()) {
                            //System.out.println("Evento.procesarEvento.cadena "+cadena);
                            totalesS.asignaPropiedadesCtaXLiq();
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), totalesS.getUnidad().substring(1, 4), totalesS.getEntidad(), totalesS.getAmbito().substring(3, 4), arregloParametros[3], totalesS.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                            liPoliza = resultado.indexOf("***") + 3;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                            hashMap.clear();
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    }
                    case 118: //Evento 118 de MIPF parametros(fecha de envio, estatus,ejercicio, documento, tipo_ar) ejemplo: 24/10/2004&4&2010&10912109011ARE00351&D                   
                    {
                        if (Integer.parseInt(arregloParametros[0].substring(6)) > Integer.parseInt(arregloParametros[2])) {
//                            String fec = "31/12/" + arregloParametros[2];
//                            arregloParametros[0] = fec;
                            ejercicio = arregloParametros[0].substring(6);
                        }
                        TotalesSenado totalesS = new TotalesSenado();
                        totalesS.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[3] + "&" + arregloParametros[4] + "&" + origen + "&" + param5); //estatus, ejercicio , numero de documento, tipoCXL, origen, condicion de pago de pasivos 
                        regs = totalesS.asignaPropiedadesCtaXLiq();
                        if (regs == 0) {//No trajo registros el query de ctaxliq
                            cadena = "";
                        } else {
                            cadena = totalesS.calculaGasto(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaRetenciones(conexionContabilidad, crs.getString("forma"), eventoContable);
                            cadena = cadena + totalesS.calculaPresupPorEjercer(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaPresupComprometido(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaPresupDevengado(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaPresupEjercido(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaPresupPagado(conexionContabilidad, crs.getString("forma"));
//                                cadena = cadena + totalesS.calculaDivisas(conexionContabilidad);
                        }
                        if (!cadena.isEmpty()) {
                            //System.out.println("Evento.procesarEvento.cadena "+cadena);
                            totalesS.asignaPropiedadesCtaXLiq();
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), totalesS.getUnidad().substring(1, 4), totalesS.getEntidad(), totalesS.getAmbito().substring(3, 4), arregloParametros[3], totalesS.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                            liPoliza = resultado.indexOf("***") + 3;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                            hashMap.clear();
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    }
                    case 119:  //Evento 119 de MIPF parametros(fecha de envio, estatus,ejercicio, documento, tipo_ar) ejemplo: 24/10/2004&4&2010&10912109011ARE00351&D                   
                    case 120:  //Evento 120 de MIPF parametros(fecha de envio, estatus,ejercicio, documento, tipo_ar) ejemplo: 24/10/2004&4&2010&10912109011ARE00351&D                              
                        pagoPasi = new PagoPasivos();
                        if (Integer.parseInt(arregloParametros[0].substring(6)) > Integer.parseInt(arregloParametros[2])) {
                            pagoPasi.select_rf_tc_fecha_lim_pasivo(conexionContabilidad, arregloParametros[2]);
                            banPasivo = pagoPasi.select_F_COMPARA_FECHA(conexionContabilidad, arregloParametros[0], pagoPasi.getFecha_limite());
//                            String fec = "31/12/" + arregloParametros[2];
//                            arregloParametros[0] = fec;
                            ejercicio = arregloParametros[0].substring(6);
                        }
                        if (banPasivo == 1) {
                            if (totales == null) {
                                totales = new Totales();
                                totales.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[3] + "&" + arregloParametros[4] + "&" + origen + "&" + param5); //estatus, ejercicio , numero de documento, tipoCXL, origen, condicion de pago de pasivos 
                                regs = totales.asignaPropiedadesCtaXLiq();
                                if (regs != 0) {//trajo registros el query de ctaxliq
                                    capitulo = totales.getCapitulo();
                                    totales.setFactor(crs.getInt("factor"));
                                    totales.setEjercicio(arregloParametros[2]);
                                    totales.calculaRetencionesHistorico(); //Calcula todas las retenciones por unidad y ambito
                                    totales.calculaAportaciones(); //Calcula todas las retenciones por unidad y ambito
                                }
                            }
                            if (crs.getString("tipo").equals("P") && (regs != 0)) {
                                cadena = "";
                                cadena = cadena + totales.calculaTotalBrutoHisEjePartidaGen(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "P");
                                totales.verificaProveedor(conexionContabilidad, registro, arregloParametros[0], crs.getString("catalogocuenta"), "2112", "5");
                                cadena = cadena + totales.calculaTotalBrutoHisEjeCapitulo(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "C");
                                cadena = cadena + totales.calculaTotalBrutoHisEje(conexionContabilidad, Integer.parseInt(crs.getString("idforma")));  //Recorre histeje para calcular total bruto, total neto y cas                     
                                cadena = cadena + totales.calculaTotalBrutoHisEjeConPartida(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "O");
                                cadena = cadena + totales.recorreTotalRetenciones();  // Recorre todas las retenciones para la forma               
                                cadena = cadena + totales.recorreTotalAportaciones();  // Recorre todas las retenciones para la forma               
                            } else if (crs.getString("tipo").equals("F") && (regs != 0)) {
                                cadena = "";
                                if (verificaForma(crs.getString("forma"), ambitoCuenta)) {
                                    totales.setFactor(crs.getInt("factor"));
                                    totales.setFactorNetoNeg(crs.getInt("FactorNetoNeg"));
                                    totales.setTotalNetoCero(false);
                                    cadena = cadena + totales.generaFormaFinaciera(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), ambitoCuenta, tipoCuenta);  //Recorre histeje para calcular total bruto, total neto y cas 
                                    cadena = cadena + totales.calculaTotalBrutoHisEjeConPartida(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "O");
                                    if ((totales.getAnticipo().equals("1")) && (totales.getCapitulo().equals("5000"))) {
                                        totales.setBanProv(1);
                                    }
                                    totales.verificaProveedor(conexionContabilidad, registro, arregloParametros[0], crs.getString("catalogocuenta"), "2112", "5");
                                    cadena = cadena + totales.calculaTotalBrutoHisEjeCapitulo(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "C");
                                    if (totales.isTotalNetoCero() == false) {
                                        cadena = "";
                                    }
                                }
                            } else {
                                cadena = "";
                            }
                        } else {
                            cadena = "";
                            enviaNotitificacion("Aviso del Sistema de Contabilidad Armonizado se invoco el evento " + eventoContable + " para su revisi�n, no se genero poliza presupuestal, debido a que la fecha de afectaci�n es mayor a la fecha l�mite del ejercicio actual.", "La Oficio de Rectificaci�n es: " + arregloParametros[3]);
                        }
                        if (!cadena.isEmpty()) {
                            //System.out.println("Evento.procesarEvento.cadena "+cadena);
                            totales.asignaPropiedadesCtaXLiq();
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), totales.getUnidad().substring(1, 4), totales.getEntidad(), totales.getAmbito().substring(3, 4), arregloParametros[3], totales.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                            liPoliza = resultado.indexOf("***") + 3;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                            hashMap.clear();
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    case 121: //Evento 121 de MIPF parametros(fecha de envio,estatus,ejercicio,documento,tipo_cxl)  ejemplo: 24/04/2010&4&2010&105011CXL00022&A                               
                    {
                        if (Integer.parseInt(arregloParametros[0].substring(6)) > Integer.parseInt(arregloParametros[2])) {
//                            String fec = "31/12/" + arregloParametros[2];
//                            arregloParametros[0] = fec;
                            ejercicio = arregloParametros[0].substring(6);
                        }
                        TotalesSenado totalesS = new TotalesSenado();
                        totalesS.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[3] + "&" + arregloParametros[4] + "&" + origen + "&" + param5); //estatus, ejercicio , numero de documento, tipoCXL, origen, condicion de pago de pasivos 
                        regs = totalesS.asignaPropiedadesCtaXLiq();
                        if (regs == 0) {//No trajo registros el query de ctaxliq
                            cadena = "";
                        } else {
                            cadena = totalesS.calculaRetenciones(conexionContabilidad, crs.getString("forma"), eventoContable);
                            cadena = cadena + totalesS.calculaCxpCp(conexionContabilidad);
                        }
                        if (!cadena.isEmpty()) {
                            //System.out.println("Evento.procesarEvento.cadena "+cadena);
                            totalesS.asignaPropiedadesCtaXLiq();
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), totalesS.getUnidad().substring(1, 4), totalesS.getEntidad(), totalesS.getAmbito().substring(3, 4), arregloParametros[3], totalesS.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                            liPoliza = resultado.indexOf("***") + 3;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                            hashMap.clear();
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    }
                    case 122:  //Evento 122 de MIPF parametros(fecha de envio,estatus,ejercicio,documento,tipo_cxl)  ejemplo: 24/04/2010&4&2010&105011CXL00022&A                                                      
                    {
                        pagoPasi = new PagoPasivos();
                        if (Integer.parseInt(arregloParametros[0].substring(6)) > Integer.parseInt(arregloParametros[2])) {
                            pagoPasi.select_rf_tc_fecha_lim_pasivo(conexionContabilidad, arregloParametros[2]);
                            banPasivo = pagoPasi.select_F_COMPARA_FECHA(conexionContabilidad, arregloParametros[0], pagoPasi.getFecha_limite());
                            fechaOriginal = arregloParametros[0];
//                            String fec = "31/12/" + arregloParametros[2];
//                            arregloParametros[0] = fec;
                            ejercicio = arregloParametros[0].substring(6);
                            polizaPresupuestal = true;
                            polizaFinanciera = false;
                            polizaAreInicioEjer = true;
                        }
                        TotalesSenado totalesS = new TotalesSenado();
                        totalesS.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[3] + "&" + arregloParametros[4] + "&" + origen + "&" + param5); //estatus, ejercicio , numero de documento, tipoCXL, origen, condicion de pago de pasivos 
                        regs = totalesS.asignaPropiedadesCtaXLiq();
                        if (regs == 0) {//No trajo registros el query de ctaxliq
                            cadena = "";
                        } else {
                            cadena = totalesS.calculaActivos(conexionContabilidad);
                            cadena = cadena + totalesS.calculaPresupDevengado(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaPresupComprometido(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaDeudores(conexionContabilidad);
                            cadena = cadena + totalesS.calculaPresupPorEjercer(conexionContabilidad, crs.getString("forma"));
                        }
                        if (!cadena.isEmpty()) {
                            //System.out.println("Evento.procesarEvento.cadena "+cadena);
                            totalesS.asignaPropiedadesCtaXLiq();
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), totalesS.getUnidad().substring(1, 4), totalesS.getEntidad(), totalesS.getAmbito().substring(3, 4), arregloParametros[3], totalesS.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                            liPoliza = resultado.indexOf("***") + 3;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                            hashMap.clear();
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    }
                    case 175:  //Evento 175 de MIPF Estatus Cancelado parametros(fecha de envio,estatus,ejercicio,documento,tipo_cxl)  ejemplo: 24/04/2010&4&2010&105011CXL00022&A                                 
                        pagoPasi = new PagoPasivos();
                        if (Integer.parseInt(arregloParametros[0].substring(6)) > Integer.parseInt(arregloParametros[2])) {
                            pagoPasi.select_rf_tc_fecha_lim_pasivo(conexionContabilidad, arregloParametros[2]);
                            banPasivo = pagoPasi.select_F_COMPARA_FECHA(conexionContabilidad, arregloParametros[0], pagoPasi.getFecha_limite());
                            fechaOriginal = arregloParametros[0];
//                            String fec = "31/12/" + arregloParametros[2];
//                            arregloParametros[0] = fec;
                            ejercicio = arregloParametros[0].substring(6);
                            polizaPresupuestal = true;
                            polizaFinanciera = false;
                            polizaAreInicioEjer = true;
                        }
                        if (banPasivo == 1) {
                            if (totales == null) {
                                totales = new Totales();
                                totales.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[3] + "&" + arregloParametros[4] + "&" + origen + "&" + param5); //estatus, ejercicio , numero de documento, tipoCXL, origen, condicion de pago de pasivos 
                                regs = totales.asignaPropiedadesCtaXLiq();
                                if (regs == 0) {//No trajo registros el query de ctaxliq
                                    cadena = "";
                                } else {
                                    anticipo = totales.getAnticipo();
                                    capitulo = totales.getCapitulo();
                                    totales.setEjercicio(arregloParametros[2]);
                                    totales.setBanAre(banAre);
                                    bcCuenta = new bcCuentasBancarias();
                                    ambitoCuenta = obtieneAmbitoCuenta(conexionContabilidad, totales.getCuenta(), bcCuenta);
                                    tipoCuenta = obtieneTipoCuenta(ambitoCuenta, bcCuenta);
                                    totales.calculaRetencionesHistorico(); //Calcula todas las retenciones por unidad y ambito
                                    totales.calculaAportaciones(); //Calcula todas las retenciones por unidad y ambito
                                }
                            }
                            tipoForma = crs.getString("tipo");
                            if ((tipoForma.equals("P") && polizaPresupuestal && (!(crs.getString("forma").equals("ZU") || crs.getString("forma").equals("ZV") || crs.getString("forma").equals("ZW")))) || (polizaAreInicioEjer && (crs.getString("forma").equals("ZU") || crs.getString("forma").equals("ZV") || crs.getString("forma").equals("ZW")))) {
                                if (regs != 0) {//No trajo registros el query de ctaxliq
                                    if (crs.getString("forma").equals("ZW")) {
                                        arregloParametros[0] = fechaOriginal;
                                        ejercicio = arregloParametros[0].substring(6);
                                    }
                                    if (anticipo.equals("0")) {  // NO ES FACTURA
                                        totales.setFactor(crs.getInt("factor"));
                                        totales.setFactorNetoNeg(crs.getInt("FactorNetoNeg"));
                                        cadena = "";
                                        cadena = cadena + totales.calculaTotalBrutoHisEjeConPartida(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "O");
                                        totales.verificaProveedor(conexionContabilidad, registro, arregloParametros[0], crs.getString("catalogocuenta"), "2112", "5");
                                        totales.setTotalBrutoCero(false);
                                        cadena = cadena + totales.calculaTotalBrutoHisEjeCapitulo(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "C");
                                        if (!totales.isTotalBrutoCero()) { //No se genera poliza presupuestal  
                                            enviaNotitificacion("Aviso del Sistema de Contabilidad Armonizado se invoco el evento " + eventoContable + " para su revisi�n, no se genero poliza presupuestal, por tener importe bruto = 0. ", "El documento es: " + arregloParametros[3]);
                                        } else {
                                            cadena = cadena + totales.calculaTotalBrutoHisEje(conexionContabilidad, Integer.parseInt(crs.getString("idforma")));  //Recorre histeje para calcular total bruto, total neto y cas                      
                                            cadena = cadena + totales.recorreTotalRetenciones();  // Recorre todas las retenciones para la forma               
                                            cadena = cadena + totales.recorreTotalAportaciones();  // Recorre todas las retenciones para la forma               
                                        }
                                    }
                                    enviaNotitificacion("Aviso del Sistema de Contabilidad Armonizado se invoco el evento " + eventoContable + " para su revisi�n, no se genero poliza presupuestal.", "La cuenta por liquidar es: " + arregloParametros[3]);
                                    cadena = "";
                                }
                            } else if (tipoForma.equals("F") && ((polizaFinanciera && (!(crs.getString("forma").equals("YI") || crs.getString("forma").equals("YQ")))) || (polizaAreInicioEjer && (crs.getString("forma").equals("YI") || crs.getString("forma").equals("YQ"))))) {//Procesa formas financieras  
                                if (crs.getString("forma").equals("YQ")) {
                                    arregloParametros[0] = fechaOriginal;
                                    ejercicio = arregloParametros[0].substring(6);
                                }
                                totales.setFactor(crs.getInt("factor"));
                                totales.setFactorNetoNeg(crs.getInt("FactorNetoNeg"));
                                totales.setTotalNetoCero(false);
                                cadena = cadena + totales.generaFormaFinaciera(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), ambitoCuenta, tipoCuenta);  //Recorre histeje para calcular total bruto, total neto y cas 
                                cadena = cadena + totales.calculaTotalBrutoHisEjeCapitulo(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "C");
                                if (totales.isTotalNetoCero() == false) {
                                    cadena = "";
                                }
                            } else {
                                cadena = "";
                            }
                        } else {
                            cadena = "";
                        }
                        if (!cadena.isEmpty()) {
                            //System.out.println("Evento.procesarEvento.cadena "+cadena);
                            totales.asignaPropiedadesCtaXLiq();
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), totales.getUnidad().substring(1, 4), totales.getEntidad(), totales.getAmbito().substring(3, 4), arregloParametros[3], totales.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                            liPoliza = resultado.indexOf("***") + 3;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                            hashMap.clear();
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    case 123:  //Evento 123 de MIPF parametros(fecha de envio, estatus, ejercicio, numero de documento y tipo_cxl) ejemplo: 24/10/2010&4&2010&105011CXL00022&A                              
                        if (Integer.parseInt(arregloParametros[0].substring(6)) > Integer.parseInt(arregloParametros[2])) {
//                            String fec = "31/12/" + arregloParametros[2];
//                            arregloParametros[0] = fec;
                            ejercicio = arregloParametros[0].substring(6);
                        }
                        totales = new Totales();
                        totales.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[3] + "&" + arregloParametros[4] + "&" + " " + "&" + param5); //estatus, ejercicio , numero de documemnto, y tipoCXL 
                        totales.setTotalbruto(totales.calculaTotalBrutoPorCtaXLiq());
                        totales.setTotalNeto(totales.getTotalBruto());
                        totales.setEjercicio(arregloParametros[2]);
                        totales.asignaPropiedadesCtaXLiq();
                        hashMap = totales.creaHashMap(conexionContabilidad, Integer.parseInt(crs.getString("idforma")));
                        if ((hashMap != null) && (!hashMap.isEmpty())) {
                            cadena = Cadena.construyeCadena(hashMap);
                            //System.out.println("arregloParametros[3] "+arregloParametros[3]);
                            if (totales.getUnidad() == null) {
                                totales.setUnidad("0" + arregloParametros[3].substring(0, 3));
                            }
                            if (totales.getAmbito() == null) {
                                totales.setAmbito("000" + arregloParametros[3].substring(5, 6));
                            }
                            if (totales.getEntidad() == null) {
                                totales.setEntidad(arregloParametros[3].substring(4, 5));
                            }
                            System.out.println("totales.getUnidad() " + totales.getUnidad());
                            System.out.println("totales.getAmbito() " + totales.getAmbito());
                            System.out.println("totales.getEntidad() " + totales.getEntidad());
                            System.out.println("crs.getString(\"catalogocuenta\") " + crs.getString("catalogocuenta"));

                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), totales.getUnidad().substring(1, 4), totales.getEntidad(), totales.getAmbito().substring(3, 4), arregloParametros[3], totales.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            posicionError++;
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                            posicionError++;
                            liPoliza = resultado.indexOf("***") + 3;
                            posicionError++;
                            lsPolizas = (lsPolizas == null ? "" : lsPolizas) + resultado.substring(liPoliza, liPoliza + 7) + "|";
                            posicionError++;
                            hashMap.clear();
                            posicionError++;
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        posicionError++;
                        break;
                    case 124:  //Evento 124 de MIPF parametros(fecha de envio, estatus,  ejercicio, numero de documento y tipo_cxl) ejemplo: 24/04/21010&4&2010&105011CXL00022&A                    
                        pagoPasi = new PagoPasivos();
                        if (Integer.parseInt(arregloParametros[0].substring(6)) > Integer.parseInt(arregloParametros[2])) {
                            pagoPasi.select_rf_tc_fecha_lim_pasivo(conexionContabilidad, arregloParametros[2]);
                            banPasivo = pagoPasi.select_F_COMPARA_FECHA(conexionContabilidad, arregloParametros[0], pagoPasi.getFecha_limite());
//                            String fec = "31/12/" + arregloParametros[2];
//                            arregloParametros[0] = fec;
                            ejercicio = arregloParametros[0].substring(6);
                        }
                        if (banPasivo == 1) {
                            totales = new Totales();
                            totales.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[3] + "&" + arregloParametros[4]); //estatus, ejercicio , numero de documemnto, y tipoCXL                 
                            totales.setTotalbruto(totales.calculaTotalBrutoPorCtaXLiq());
                            totales.setTotalNeto(totales.getTotalBruto());
                            totales.setEjercicio(arregloParametros[2]);
                            totales.asignaPropiedadesCtaXLiq();
                            hashMap = totales.creaHashMap(conexionContabilidad, Integer.parseInt(crs.getString("idforma")));
                            if ((hashMap != null) && (!hashMap.isEmpty())) {
                                cadena = Cadena.construyeCadena(hashMap);
                                preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), totales.getUnidad().substring(1, 4), totales.getEntidad(), totales.getAmbito().substring(3, 4), arregloParametros[3], totales.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                                resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF_NO_ACUMULADO);
                                liPoliza = resultado.indexOf("***") + 3;
                                lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                                hashMap.clear();
                            } else {
                                lsPolizas = lsPolizas + "SIN POLIZA|";
                                resultado = "1,Sin poliza por hashMap vacio";
                            }
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    case 125:  //Evento 125 de MIPF parametros(fecha de envio, estatus,ejercicio, documento, tipo_cxl)  ejemplo: 24/04/2010&4&2010&105011CXL00022&A                  
                        if (Integer.parseInt(arregloParametros[0].substring(6)) > Integer.parseInt(arregloParametros[2])) {
//                            String fec = "31/12/" + arregloParametros[2];
//                            arregloParametros[0] = fec;
                            ejercicio = arregloParametros[0].substring(6);
                        }
                        totales = new Totales();
                        totales.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[3] + "&" + arregloParametros[4] + "&" + origen + "&" + param5); //estatus, ejercicio , numero de documento, tipoCXL, origen, condicion de pago de pasivos 
                        if (totales.asignaPropiedadesCtaXLiq() == 0) {//No trajo registros el query de ctaxliq
                            cadena = "";
                        } else {
                            anticipo = totales.getAnticipo();
                            capitulo = totales.getCapitulo();
                            totales.setFactor(crs.getInt("factor"));
                            totales.setEjercicio(arregloParametros[2]);
                            if (anticipo.equals("0")) {  // NO ES FACTURA
                                totales.calculaRetencionesHistorico(); //Calcula todas las retenciones por unidad y ambito
                                totales.calculaAportaciones(); //Calcula todas las retenciones por unidad y ambito
                                cadena = "";
                                cadena = cadena + totales.calculaTotalBrutoHisEjePartidaGen(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "P");
                                totales.verificaProveedor(conexionContabilidad, registro, arregloParametros[0], crs.getString("catalogocuenta"), "2112", "5");
                                cadena = cadena + totales.calculaTotalBrutoHisEjeCapitulo(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "C");
                                cadena = cadena + totales.calculaTotalBrutoHisEje(conexionContabilidad, Integer.parseInt(crs.getString("idforma")));  //Recorre histeje para calcular total bruto, total neto y cas                     
                                cadena = cadena + totales.calculaTotalBrutoHisEjeConPartida(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "O");
                                cadena = cadena + totales.recorreTotalRetenciones();  // Recorre todas las retenciones para la forma               
                                cadena = cadena + totales.recorreTotalAportaciones();  // Recorre todas las retenciones para la forma                                                  
                            } else {
                                cadena = "";
                                cadena = cadena + totales.calculaTotalFacturaCapitulo(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "F", registro, arregloParametros[0], crs.getString("catalogocuenta"));
                                cadena = cadena + totales.calculaTotalFacturaConPartida(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "N", registro, arregloParametros[0], crs.getString("catalogocuenta"));
                                cadena = cadena + totales.calculaTotalBrutoHisEjeCapitulo(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "C");
                                cadena = cadena + totales.calculaTotalBrutoHisEje(conexionContabilidad, Integer.parseInt(crs.getString("idforma")));  //Recorre histeje para calcular total bruto, total neto y cas                 
                            }
                        }
                        if (!cadena.isEmpty()) {
                            //System.out.println("Evento.procesarEvento.cadena "+cadena);
                            totales.asignaPropiedadesCtaXLiq();
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), totales.getUnidad().substring(1, 4), totales.getEntidad(), totales.getAmbito().substring(3, 4), arregloParametros[3], totales.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                            liPoliza = resultado.indexOf("***") + 3;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                            hashMap.clear();
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    case 126:  //Evento 126 de MIPF parametros(fecha de envio, estatus, ejercicio, documento, tipo_ar)  ejemplo: 24/04/2010&4&2010&10912109011ARE00351&D                  
                        pagoPasi = new PagoPasivos();
                        if (Integer.parseInt(arregloParametros[0].substring(6)) > Integer.parseInt(arregloParametros[2])) {
                            pagoPasi.select_rf_tc_fecha_lim_pasivo(conexionContabilidad, arregloParametros[2]);
                            banPasivo = pagoPasi.select_F_COMPARA_FECHA(conexionContabilidad, arregloParametros[0], pagoPasi.getFecha_limite());
                            fechaOriginal = arregloParametros[0];
//                            String fec = "31/12/" + arregloParametros[2];
//                            arregloParametros[0] = fec;
                            ejercicio = arregloParametros[0].substring(6);
                            polizaPresupuestal = true;
                            polizaFinanciera = false;
                            polizaAreInicioEjer = true;
                        }
                        if (banPasivo == 1) {
                            if (totales == null) {
                                totales = new Totales();
                                totales.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[3] + "&" + arregloParametros[4] + "&" + origen + "&" + param5); //estatus, ejercicio , numero de documento, tipoCXL, origen, condicion de pago de pasivos 
                                regs = totales.asignaPropiedadesCtaXLiq();
                                if (regs == 0) {//No trajo registros el query de ctaxliq
                                    cadena = "";
                                } else {
                                    anticipo = totales.getAnticipo();
                                    capitulo = totales.getCapitulo();
                                    totales.setEjercicio(arregloParametros[2]);
                                    totales.setBanAre(banAre);
                                    bcCuenta = new bcCuentasBancarias();
                                    ambitoCuenta = obtieneAmbitoCuenta(conexionContabilidad, totales.getCuenta(), bcCuenta);
                                    tipoCuenta = obtieneTipoCuenta(ambitoCuenta, bcCuenta);
                                    totales.calculaRetencionesHistorico(); //Calcula todas las retenciones por unidad y ambito
                                    totales.calculaAportaciones(); //Calcula todas las retenciones por unidad y ambito
                                }
                            }
                            tipoForma = crs.getString("tipo");
                            if ((tipoForma.equals("P") && polizaPresupuestal && (!(crs.getString("forma").equals("ZU") || crs.getString("forma").equals("ZV") || crs.getString("forma").equals("ZW")))) || (polizaAreInicioEjer && (crs.getString("forma").equals("ZU") || crs.getString("forma").equals("ZV") || crs.getString("forma").equals("ZW")))) {
                                if (regs != 0) { //No trajo registros el query de ctaxliq
                                    if (crs.getString("forma").equals("ZW")) {
                                        arregloParametros[0] = fechaOriginal;
                                        ejercicio = arregloParametros[0].substring(6);
                                    }
                                    totales.setFactor(crs.getInt("factor"));
                                    totales.setFactorNetoNeg(crs.getInt("FactorNetoNeg"));
                                    cadena = totales.calculaTotalBrutoHisEjeCapitulo(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "C");
                                    //cambio solicitado por salvador 09-08-2011
                                    if (totales.getBanProv() == 1) {
                                        enviaNotitificacion("Aviso del Sistema de Contabilidad  Armonizado se invoco el evento " + eventoContable + " (ARE)con cap�tulo = 5000 y anticipo=0. Para su revisi�n, no se genero poliza presupuestal.", "El aviso de reintegro es: " + arregloParametros[3]);
                                        cadena = "";
                                    } else {
                                        cadena = cadena + totales.calculaTotalBrutoHisEjePartidaGen(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "P");
                                        cadena = cadena + totales.calculaTotalBrutoHisEje(conexionContabilidad, Integer.parseInt(crs.getString("idforma")));  //Recorre histeje para calcular total bruto, total neto y cas 
                                        cadena = cadena + totales.recorreTotalRetenciones();  // Recorre todas las retenciones para la forma               
                                        cadena = cadena + totales.recorreTotalAportaciones();  // Recorre todas las retenciones para la forma               
                                    }
                                }
                            } else if (tipoForma.equals("F") && ((polizaFinanciera && (!(crs.getString("forma").equals("YI") || crs.getString("forma").equals("YQ")))) || (polizaAreInicioEjer && (crs.getString("forma").equals("YI") || crs.getString("forma").equals("YQ"))))) {//Procesa formas financieras                 
                                if (crs.getString("forma").equals("YQ")) {
                                    arregloParametros[0] = fechaOriginal;
                                    ejercicio = arregloParametros[0].substring(6);
                                }
                                totales.setFactor(crs.getInt("factor"));
                                totales.setFactorNetoNeg(crs.getInt("FactorNetoNeg"));
                                totales.setTotalNetoCero(false);
                                cadena = cadena + totales.generaFormaFinaciera(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), ambitoCuenta, tipoCuenta);  //Recorre histeje para calcular total bruto, total neto y cas 
                                cadena = cadena + totales.calculaTotalBrutoHisEjeCapitulo(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "C");
                                if (totales.isTotalNetoCero() == false) {
                                    cadena = "";
                                }
                            } else {
                                cadena = "";
                            }
                        } else {
                            cadena = "";
                        }
                        if (!cadena.isEmpty()) {
                            //System.out.println("Evento.procesarEvento.cadena "+cadena);
                            totales.asignaPropiedadesCtaXLiq();
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), totales.getUnidad().substring(1, 4), totales.getEntidad(), totales.getAmbito().substring(3, 4), arregloParametros[3], totales.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                            liPoliza = resultado.indexOf("***") + 3;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                            hashMap.clear();
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    case 127:  //Evento 127 de MIPF parametros(fecha de envio,estatus,ejercicio,documento,tipo_cxl)  ejemplo: 24/04/2010&4&2010&105011CXL00022&A                  
                    case 128:  //Evento 128 de MIPF parametros(fecha de envio,estatus,ejercicio,documento,tipo_cxl)  ejemplo: 24/04/2010&4&2010&105011CXL00022&A                  
                        pagoPasi = new PagoPasivos();
                        if (Integer.parseInt(arregloParametros[0].substring(6)) > Integer.parseInt(arregloParametros[2])) {
                            pagoPasi.select_rf_tc_fecha_lim_pasivo(conexionContabilidad, arregloParametros[2]);
                            banPasivo = pagoPasi.select_F_COMPARA_FECHA(conexionContabilidad, arregloParametros[0], pagoPasi.getFecha_limite());
//                            String fec = "31/12/" + arregloParametros[2];
//                            arregloParametros[0] = fec;
                            ejercicio = arregloParametros[0].substring(6);
                        }
                        if (banPasivo == 1) {
                            if (totales == null) {
                                totales = new Totales();
                                totales.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[3] + "&" + arregloParametros[4] + "&" + origen + "&" + param5); //estatus, ejercicio , numero de documento, tipoCXL, origen, condicion de pago de pasivos 
                                regs = totales.asignaPropiedadesCtaXLiq();
                                if (regs != 0) {//trajo registros el query de ctaxliq
                                    anticipo = totales.getAnticipo();
                                    capitulo = totales.getCapitulo();
                                    totales.setFactor(crs.getInt("factor"));
                                    totales.setEjercicio(arregloParametros[2]);
                                    bcCuenta = new bcCuentasBancarias();
                                    ambitoCuenta = obtieneAmbitoCuenta(conexionContabilidad, totales.getCuenta(), bcCuenta);
                                    tipoCuenta = obtieneTipoCuenta(ambitoCuenta, bcCuenta);
                                    totales.calculaRetencionesHistorico(); //Calcula todas las retenciones por unidad y ambito
                                    totales.calculaAportaciones(); //Calcula todas las retenciones por unidad y ambito
                                }
                            }
                            if (crs.getString("tipo").equals("P") && (regs != 0)) {
                                cadena = "";
                                cadena = cadena + totales.calculaTotalBrutoHisEjePartidaGen(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "P");
                                totales.verificaProveedor(conexionContabilidad, registro, arregloParametros[0], crs.getString("catalogocuenta"), "2112", "5");
                                cadena = cadena + totales.calculaTotalBrutoHisEjeCapitulo(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "C");
                                cadena = cadena + totales.calculaTotalBrutoHisEje(conexionContabilidad, Integer.parseInt(crs.getString("idforma")));  //Recorre histeje para calcular total bruto, total neto y cas                     
                                cadena = cadena + totales.calculaTotalBrutoHisEjeConPartida(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "O");
                                cadena = cadena + totales.recorreTotalRetenciones();  // Recorre todas las retenciones para la forma               
                                cadena = cadena + totales.recorreTotalAportaciones();  // Recorre todas las retenciones para la forma                                        
                            } else if (crs.getString("tipo").equals("F") && (regs != 0)) {
                                cadena = "";
                                if (verificaForma(crs.getString("forma"), ambitoCuenta)) {
                                    totales.setFactor(crs.getInt("factor"));
                                    totales.setFactorNetoNeg(crs.getInt("FactorNetoNeg"));
                                    totales.setTotalNetoCero(false);
                                    cadena = cadena + totales.generaFormaFinaciera(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), ambitoCuenta, tipoCuenta);  //Recorre histeje para calcular total bruto, total neto y cas 
                                    cadena = cadena + totales.calculaTotalBrutoHisEjeConPartida(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "O");
                                    if ((totales.getAnticipo().equals("1")) && (totales.getCapitulo().equals("5000"))) {
                                        totales.setBanProv(1);
                                    }
                                    totales.verificaProveedor(conexionContabilidad, registro, arregloParametros[0], crs.getString("catalogocuenta"), "2112", "5");
                                    cadena = cadena + totales.calculaTotalBrutoHisEjeCapitulo(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "C");
                                    if ((totales.isTotalNetoCero() == false) || (totales.verificaProgramaBancoMundial() && (crs.getString("forma").equals("YE") || crs.getString("forma").equals("YN")))) {
                                        cadena = "";
                                    }
                                }
                            } else {
                                cadena = "";
                            }
                        } else {
                            cadena = "";
                            enviaNotitificacion("Aviso del Sistema de Contabilidad Armonizado se invoco el evento " + eventoContable + " para su revisi�n, no se genero poliza presupuestal, debido a que la fecha de afectaci�n es mayor a la fecha l�mite del ejercicio actual.", "La Oficio de Rectificaci�n es: " + arregloParametros[3]);
                        }
                        if (!cadena.isEmpty()) {
                            //System.out.println("Evento.procesarEvento.cadena "+cadena);
                            totales.asignaPropiedadesCtaXLiq();
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), totales.getUnidad().substring(1, 4), totales.getEntidad(), totales.getAmbito().substring(3, 4), arregloParametros[3], totales.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                            liPoliza = resultado.indexOf("***") + 3;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                            hashMap.clear();
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    case 129:
                        operacionFR = new OperacionFondoRotatorio();
                        cadena = operacionFR.registroOFR(conexionSistema, arregloParametros[2], arregloParametros[3], arregloParametros[1]);
                        if (!cadena.isEmpty()) {
                            //System.out.println("Evento.procesarEvento.cadena "+cadena);
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), operacionFR.getUnidad(), operacionFR.getEntidad(), operacionFR.getAmbito().substring(3, 4), arregloParametros[3], operacionFR.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                            liPoliza = resultado.indexOf("***") + 3;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                            hashMap.clear();
                            cadena = "";
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    case 130:
                        operacionFR = new OperacionFondoRotatorio();
                        cadena = operacionFR.registroOFR_ARE(conexionSistema, arregloParametros[2], arregloParametros[3], arregloParametros[1], arregloParametros[4]);
                        if (!cadena.isEmpty()) {
                            //System.out.println("Evento.procesarEvento.cadena "+cadena);
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), operacionFR.getUnidad(), operacionFR.getEntidad(), operacionFR.getAmbito().substring(3, 4), arregloParametros[3], operacionFR.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                            liPoliza = resultado.indexOf("***") + 3;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                            hashMap.clear();
                            cadena = "";
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    case 137:  //Evento 137 de MIPF parametros(fecha de envio,estatus,ejercicio,documento,tipo_cxl)  ejemplo: 24/04/2010&4&2010&105011CXL00022&A                  
                        // Se cambia al código del evento 101 para generar una poliza de cancelación MMH 20/07/2017 
                        // ***Evento 101 de MIPF parametros(fecha de envio,estatus,ejercicio,documento,tipo_cxl)  ejemplo: 24/04/2010&4&2010&105011CXL00022&A***
                    {
                        if (Integer.parseInt(arregloParametros[0].substring(6)) > Integer.parseInt(arregloParametros[2])) {
//                            String fec = "31/12/" + arregloParametros[2];
//                            arregloParametros[0] = fec;
                            ejercicio = arregloParametros[0].substring(6);
                        }
                        TotalesSenado totalesS = new TotalesSenado();
                        String cxp = "";
                        totalesS.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[3] + "&" + arregloParametros[4] + "&" + origen + "&" + param5); //estatus, ejercicio , numero de documemnto, tipoCXL, origen, condicion de pago de pasivos 
                        if (totalesS.asignaPropiedadesCtaXLiq() == 0) {//No trajo registros el query de ctaxliq
                            cadena = "";
                        } else {
                            System.out.println("Evento.aqui " + totalesS.asignaPropiedadesCtaXLiq());
                            anticipo = totalesS.getAnticipo();
                            //capitulo = totalesS.getCapitulo();
                            // Se elimina el comentario donde se asigna el valor al factor MMM 20/07/2017
                            totalesS.setFactor(crs.getInt("factor"));
                            // ***************************************
                            //totalesS.setEjercicio(arregloParametros[2]);
                            if (anticipo.equals("0")) {  // NO ES FACTURA
                                //totalesS.calculaRetencionesHistorico(); //Calcula todas las retenciones por unidad y ambito
                                //totalesS.calculaAportaciones(); //Calcula todas las retenciones por unidad y ambito
                                //totalesS.verificaProveedor(conexionContabilidad, registro, arregloParametros[0], crs.getString("catalogocuenta"), "2112", "5");
                                cadena = totalesS.calculaGasto(conexionContabilidad, crs.getString("forma"));
                                cxp = totalesS.calculaCxpCp(conexionContabilidad);
                                cadena = cadena + cxp;
                                cadena = cadena + totalesS.calculaRetenciones(conexionContabilidad, crs.getString("forma"), eventoContable );
                                cadena = cadena + totalesS.calculaPresupDevengado(conexionContabilidad, crs.getString("forma"));
                                cadena = cadena + totalesS.calculaPresupComprometido(conexionContabilidad, crs.getString("forma"));
                                cadena = cadena + totalesS.calculaPresupComprometido2(conexionContabilidad);
                                cadena = cadena + totalesS.calculaPresupPorEjercer(conexionContabilidad, crs.getString("forma"));
                                cadena = cadena + totalesS.calculaPresupPagado(conexionContabilidad, crs.getString("forma"));
                                cadena = cadena + totalesS.calculaPresupEjercido(conexionContabilidad, crs.getString("forma"));
                            }
                        }
                        System.out.println("*******************\n" + crs.getString("forma") + "\n***************");
                        if (crs.getString("forma").equals("CC") && !cxp.isEmpty()) {
                            cadena = "";
                        } else if (crs.getString("forma").equals("CA") && cxp.isEmpty()) {
                            cadena = "";
                        }
                        if (!cadena.isEmpty()) {
                            System.out.println("Evento.procesarEvento.cadena " + cadena);
                            totalesS.asignaPropiedadesCtaXLiq();
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), totalesS.getUnidad().substring(1, 4), totalesS.getEntidad(), totalesS.getAmbito().substring(3, 4), arregloParametros[3], totalesS.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            //aqui se genera la póliza
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                            liPoliza = resultado.indexOf("***") + 3;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                            hashMap.clear();
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    }
                    case 138:  //Evento 138 de MIPF parametros(fecha de envio,estatus,ejercicio,documento,tipo_cxl)  ejemplo: 24/04/2010&4&2010&105011CXL00022&A                                  
                       {
                        if (Integer.parseInt(arregloParametros[0].substring(6)) > Integer.parseInt(arregloParametros[2])) {
//                            String fec = "31/12/" + arregloParametros[2];
//                            arregloParametros[0] = fec;
                            ejercicio = arregloParametros[0].substring(6);
                        }
                        TotalesSenado totalesS = new TotalesSenado();
                        totalesS.setFactor(crs.getInt("factor"));
                        totalesS.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[3] + "&" + arregloParametros[4] + "&" + origen + "&" + param5); //estatus, ejercicio , numero de documento, tipoCXL, origen, condicion de pago de pasivos 
                        regs = totalesS.asignaPropiedadesCtaXLiq();
                        if (regs == 0) {//No trajo registros el query de ctaxliq
                            cadena = "";
                        } else {
                            cadena = totalesS.calculaRetenciones(conexionContabilidad, crs.getString("forma"), eventoContable);
                            cadena = cadena + totalesS.calculaAportaciones(conexionContabilidad);
                            cadena = cadena + totalesS.calculaGasto(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaCxpCp(conexionContabilidad);
                            cadena = cadena + totalesS.calculaPresupPorEjercer(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaPresupComprometido2(conexionContabilidad);
                            cadena = cadena + totalesS.calculaPresupComprometido(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaPresupDevengado(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaPresupEjercido(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaActivos(conexionContabilidad);
                        }
                        if (!cadena.isEmpty()) {
                            //System.out.println("Evento.procesarEvento.cadena "+cadena);
                            totalesS.asignaPropiedadesCtaXLiq();
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), totalesS.getUnidad().substring(1, 4), totalesS.getEntidad(), totalesS.getAmbito().substring(3, 4), arregloParametros[3], totalesS.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                            liPoliza = resultado.indexOf("***") + 3;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                            hashMap.clear();
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    }
                    case 139:  //Evento 139 de MIPF parametros(fecha de envio,estatus,ejercicio,documento,tipo_cxl)  ejemplo: 24/04/2010&4&2010&105011CXL00022&A                  
                         {
                        if (Integer.parseInt(arregloParametros[0].substring(6)) > Integer.parseInt(arregloParametros[2])) {
//                            String fec = "31/12/" + arregloParametros[2];
//                            arregloParametros[0] = fec;
                            ejercicio = arregloParametros[0].substring(6);
                        }
                        TotalesSenado totalesS = new TotalesSenado();
                        String cxp = "";
                        posicionError++;
                        totalesS.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[3] + "&" + arregloParametros[4] + "&" + origen + "&" + param5); //estatus, ejercicio , numero de documento, tipoCXL, origen, condicion de pago de pasivos 
                        posicionError++;
                        if (totalesS.asignaPropiedadesCtaXLiq() == 0) {//No trajo registros el query de ctaxliq
                            cadena = "";
                        } else {
                        posicionError++;
                            anticipo = totalesS.getAnticipo();
                        posicionError++;
//                            capitulo = totales.getCapitulo();
                            totalesS.setFactor(crs.getInt("factor"));
                        posicionError++;
//                            totales.setEjercicio(arregloParametros[2]);
                            System.out.println("Evento.procesarEvento.139.anticipo " + anticipo);
                        posicionError++;
                            cadena = totalesS.calculaGasto(conexionContabilidad, crs.getString("forma"));
                        posicionError++;
                            cxp = totalesS.calculaCxpCp(conexionContabilidad);
                        posicionError++;
                            cadena = cadena + cxp;
                        posicionError++;
                            cadena = cadena + totalesS.calculaRetenciones(conexionContabilidad, crs.getString("forma"), eventoContable);
                        posicionError++;
                            cadena = cadena + totalesS.calculaPresupDevengado(conexionContabilidad, crs.getString("forma"));
                        posicionError++;
                            cadena = cadena + totalesS.calculaPresupComprometido(conexionContabilidad, crs.getString("forma"));
                        posicionError++;
                            cadena = cadena + totalesS.calculaPresupComprometido2(conexionContabilidad);
                        posicionError++;
                            cadena = cadena + totalesS.calculaPresupPorEjercer(conexionContabilidad, crs.getString("forma"));
                        posicionError++;
                            cadena = cadena + totalesS.calculaPresupPagado(conexionContabilidad, crs.getString("forma"));
                        posicionError++;
                            cadena = cadena + totalesS.calculaPresupEjercido(conexionContabilidad, crs.getString("forma"));
                        posicionError++;
                        }
                        //System.out.println("*******************\n" + crs.getString("forma") + "\n***************");
                        if (crs.getString("forma").equals("CC") && !cxp.isEmpty()) {
                            cadena = "";
                        } else if (crs.getString("forma").equals("CA") && cxp.isEmpty()) {
                            cadena = "";
                        }
                        if (!cadena.isEmpty()) {
                            //System.out.println("Evento.procesarEvento.cadena "+cadena);
                            totalesS.asignaPropiedadesCtaXLiq();
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), totalesS.getUnidad().substring(1, 4), totalesS.getEntidad(), totalesS.getAmbito().substring(3, 4), arregloParametros[3], totalesS.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                            liPoliza = resultado.indexOf("***") + 3;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                            hashMap.clear();
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    }
                    case 140:  //Evento 140 de MIPF parametros(fecha de envio, estatus,ejercicio, documento, tipo_ar) ejemplo: 24/10/2004&4&2010&10912109011ARE00351&D                   
                        {
                        if (Integer.parseInt(arregloParametros[0].substring(6)) > Integer.parseInt(arregloParametros[2])) {
//                            String fec = "31/12/" + arregloParametros[2];
//                            arregloParametros[0] = fec;
                            ejercicio = arregloParametros[0].substring(6);
                        }
                        cadena = "";
                        TotalesSenado totalesS = new TotalesSenado();
                        totalesS.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[3] + "&" + arregloParametros[4] + "&" + origen + "&" + param5); //estatus, ejercicio , numero de documento, tipoCXL, origen, condicion de pago de pasivos 
                        if (totalesS.asignaPropiedadesCtaXLiq() > 0) {//trajo registros el query de ctaxliq
//                            anticipo = totalesS.getAnticipo();
//                            capitulo = totales.getCapitulo();
                            totalesS.setFactor(crs.getInt("factor"));
//                            totales.setEjercicio(arregloParametros[2]);
                            System.out.println("Evento.procesarEvento.140.anticipo " + anticipo);
                            cadena = totalesS.calculaCxpCp(conexionContabilidad);
                            cadena = cadena + totalesS.calculaDeudores(conexionContabilidad);
                            cadena = cadena + totalesS.calculaPresupDevengado(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaPresupComprometido(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaPresupPorEjercer(conexionContabilidad, crs.getString("forma"));
                        }
                        if (!cadena.isEmpty()) {
                            System.out.println("Evento.procesarEvento.cadena " + cadena);
                            totalesS.asignaPropiedadesCtaXLiq();
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), totalesS.getUnidad().substring(1, 4), totalesS.getEntidad(), totalesS.getAmbito().substring(3, 4), arregloParametros[3], totalesS.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                            liPoliza = resultado.indexOf("***") + 3;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                            hashMap.clear();
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    }
                    case 141:  //Evento 141 de MIPF parametros(fecha de envio,estatus,ejercicio,documento,tipo_cxl)  ejemplo: 24/04/2010&4&2010&105011CXL00022&A                               
                        if (Integer.parseInt(arregloParametros[0].substring(6)) > Integer.parseInt(arregloParametros[2])) {
//                            String fec = "31/12/" + arregloParametros[2];
//                            arregloParametros[0] = fec;
                            ejercicio = arregloParametros[0].substring(6);
                        }

                        totales = new Totales();
                        totales.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[3] + "&" + arregloParametros[4] + "&" + origen + "&" + param5); //estatus, ejercicio , numero de documento, tipoCXL, origen, condicion de pago de pasivos
                        if (totales.asignaPropiedadesCtaXLiq() == 0) {//No trajo registros el query de ctaxliq
                            cadena = "";
                        } else {
                            anticipo = totales.getAnticipo();
                            capitulo = totales.getCapitulo();
                            totales.setFactor(crs.getInt("factor"));
                            totales.setEjercicio(arregloParametros[2]);
                            totales.calculaRetencionesHistorico(); //Calcula todas las retenciones por unidad y ambito
                            totales.calculaAportaciones(); //Calcula todas las retenciones por unidad y ambito
                            cadena = "";
                            cadena = cadena + totales.calculaTotalBrutoHisEjeConPartida(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "O");
                            totales.verificaProveedor(conexionContabilidad, registro, arregloParametros[0], crs.getString("catalogocuenta"), "2112", "5");
                            cadena = cadena + totales.calculaTotalBrutoHisEjeCapitulo(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "C");
                            cadena = cadena + totales.calculaTotalBrutoHisEje(conexionContabilidad, Integer.parseInt(crs.getString("idforma")));  //Recorre histeje para calcular total bruto, total neto y cas 
                            cadena = cadena + totales.recorreTotalRetenciones();  // Recorre todas las retenciones para la forma               
                            cadena = cadena + totales.recorreTotalAportaciones();  // Recorre todas las retenciones para la forma               
                        }
                        if (!cadena.isEmpty()) {
                            //System.out.println("Evento.procesarEvento.cadena "+cadena);
                            totales.asignaPropiedadesCtaXLiq();
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), totales.getUnidad().substring(1, 4), totales.getEntidad(), totales.getAmbito().substring(3, 4), arregloParametros[3], totales.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                            liPoliza = resultado.indexOf("***") + 3;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                            hashMap.clear();
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    case 142:  //Evento 142 de MIPF parametros(fecha de envio, estatus, ejercicio, numero de documento y tipo_cxl) ejemplo: 24/10/2010&4&2010&105011CXL00022&A                    
                        if (Integer.parseInt(arregloParametros[0].substring(6)) > Integer.parseInt(arregloParametros[2])) {
//                            String fec = "31/12/" + arregloParametros[2];
//                            arregloParametros[0] = fec;
                            ejercicio = arregloParametros[0].substring(6);
                        }
                        totales = new Totales();
                        totales.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[3] + "&" + arregloParametros[4] + "&" + " " + "&" + param5); //estatus, ejercicio , numero de documemnto, y tipoCXL 
                        totales.setFactor(crs.getInt("factor"));
                        totales.setEjercicio(arregloParametros[2]);
                        totales.setTotalbruto(totales.calculaTotalBrutoPorCtaXLiq());
                        totales.setTotalNeto(totales.getTotalBruto());
                        totales.asignaPropiedadesCtaXLiq();
                        hashMap = totales.creaHashMap(conexionContabilidad, Integer.parseInt(crs.getString("idforma")));
                        if ((hashMap != null) && (!hashMap.isEmpty())) {
                            cadena = Cadena.construyeCadena(hashMap);
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), totales.getUnidad().substring(1, 4), totales.getEntidad(), totales.getAmbito().substring(3, 4), arregloParametros[3], totales.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                            liPoliza = resultado.indexOf("***") + 3;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                            hashMap.clear();
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    case 143:  //Evento 143 de MIPF parametros(fecha de envio, estatus,ejercicio, documento, tipo_cxl)  ejemplo: 24/04/2010&4&2010&105011CXL00022&A                  
                        if (Integer.parseInt(arregloParametros[0].substring(6)) > Integer.parseInt(arregloParametros[2])) {
//                            String fec = "31/12/" + arregloParametros[2];
//                            arregloParametros[0] = fec;
                            ejercicio = arregloParametros[0].substring(6);
                        }
                        totales = new Totales();
                        totales.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[3] + "&" + arregloParametros[4] + "&" + origen + "&" + param5); //estatus, ejercicio , numero de documento, tipoCXL, origen, condicion de pago de pasivos
                        if (totales.asignaPropiedadesCtaXLiq() == 0) {//No trajo registros el query de ctaxliq
                            cadena = "";
                        } else {
                            anticipo = totales.getAnticipo();
                            capitulo = totales.getCapitulo();
                            totales.setFactor(crs.getInt("factor"));
                            totales.setEjercicio(arregloParametros[2]);
                            if (anticipo.equals("0")) {  // NO ES FACTURA
                                totales.calculaRetencionesHistorico(); //Calcula todas las retenciones por unidad y ambito
                                totales.calculaAportaciones(); //Calcula todas las retenciones por unidad y ambito
                                cadena = "";
                                cadena = cadena + totales.calculaTotalBrutoHisEjePartidaGen(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "P");
                                totales.verificaProveedor(conexionContabilidad, registro, arregloParametros[0], crs.getString("catalogocuenta"), "2112", "5");
                                cadena = cadena + totales.calculaTotalBrutoHisEjeCapitulo(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "C");
                                cadena = cadena + totales.calculaTotalBrutoHisEje(conexionContabilidad, Integer.parseInt(crs.getString("idforma")));  //Recorre histeje para calcular total bruto, total neto y cas                     
                                cadena = cadena + totales.calculaTotalBrutoHisEjeConPartida(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "O");
                                cadena = cadena + totales.recorreTotalRetenciones();  // Recorre todas las retenciones para la forma               
                                cadena = cadena + totales.recorreTotalAportaciones();  // Recorre todas las retenciones para la forma               
                            } else {
                                cadena = "";
                                cadena = cadena + totales.calculaTotalFacturaCapitulo(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "F", registro, arregloParametros[0], crs.getString("catalogocuenta"));
                                cadena = cadena + totales.calculaTotalFacturaConPartida(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "N", registro, arregloParametros[0], crs.getString("catalogocuenta"));
                                cadena = cadena + totales.calculaTotalBrutoHisEjeCapitulo(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "C");
                                cadena = cadena + totales.calculaTotalBrutoHisEje(conexionContabilidad, Integer.parseInt(crs.getString("idforma")));  //Recorre histeje para calcular total bruto, total neto y cas 
                            }
                        }
                        if (!cadena.isEmpty()) {
                            //System.out.println("Evento.procesarEvento.cadena "+cadena);
                            totales.asignaPropiedadesCtaXLiq();
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), totales.getUnidad().substring(1, 4), totales.getEntidad(), totales.getAmbito().substring(3, 4), arregloParametros[3], totales.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                            liPoliza = resultado.indexOf("***") + 3;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                            hashMap.clear();
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    case 144:  //Evento 144 de MIPF parametros(fecha de envio,estatus,ejercicio,documento,tipo_cxl)  ejemplo: 24/04/2010&4&2010&105011CXL00022&A                                     
                    {
                        if (Integer.parseInt(arregloParametros[0].substring(6)) > Integer.parseInt(arregloParametros[2])) {
//                            String fec = "31/12/" + arregloParametros[2];
//                            arregloParametros[0] = fec;
                            ejercicio = arregloParametros[0].substring(6);
                        }
                        TotalesSenado totalesS = new TotalesSenado();
                        totalesS.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[3] + "&" + arregloParametros[4] + "&" + origen + "&" + param5); //estatus, ejercicio , numero de documento, tipoCXL, origen, condicion de pago de pasivos 
                        regs = totalesS.asignaPropiedadesCtaXLiq();
                        if (regs == 0) {//No trajo registros el query de ctaxliq
                            cadena = "";
                        } else {
                            cadena = totalesS.calculaPresupPagado(conexionContabilidad,crs.getString("forma"));
                            cadena = cadena + totalesS.calculaPresupDevengado(conexionContabilidad,crs.getString("forma"));
                            cadena = cadena + totalesS.calculaPresupEjercido(conexionContabilidad,crs.getString("forma"));
                        }
                        if(cadena.contains("IMPORTE_PRESUP_PAGADO=0.0|~") && cadena.contains("IMPORTE_PRESUP_DEVENGADO=0.0|~") && cadena.contains("IMPORTE_PRESUP_EJERCIDO=0.0|~")) {
                            cadena = "";
                        }
                        if (!cadena.isEmpty()) {
                            //System.out.println("Evento.procesarEvento.cadena "+cadena);
                            totalesS.asignaPropiedadesCtaXLiq();
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), totalesS.getUnidad().substring(1, 4), totalesS.getEntidad(), totalesS.getAmbito().substring(3, 4), arregloParametros[3], totalesS.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                            liPoliza = resultado.indexOf("***") + 3;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                            hashMap.clear();
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    }
                    case 145:  //Evento 145 de MIPF parametros(fecha de envio,estatus,ejercicio,documento,tipo_cxl)  ejemplo: 24/04/2010&4&2010&105011CXL00022&A                                
                        if (Integer.parseInt(arregloParametros[0].substring(6)) > Integer.parseInt(arregloParametros[2])) {
                            polizaPresupuestal = false;
                            param5 = "--";
                        }
                        if (totales == null) {  //Procesa forma presupuestal                          
                            totales = new Totales();
                            totales.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[3] + "&" + arregloParametros[4] + "&" + origen + "&" + param5); //estatus, ejercicio , numero de documemnto, tipoCXL, origen, condicion de pago de pasivo 
                            regs = totales.asignaPropiedadesCtaXLiq();
                            if (regs != 0) {//trajo registros el query de ctaxliq
                                anticipo = totales.getAnticipo();
                                capitulo = totales.getCapitulo();
                                totales.setEjercicio(arregloParametros[2]);
                                totales.setBanDevengado(banDevengado);
                                bcCuenta = new bcCuentasBancarias();
                                ambitoCuenta = obtieneAmbitoCuenta(conexionContabilidad, totales.getCuenta(), bcCuenta);
                                tipoCuenta = obtieneTipoCuenta(ambitoCuenta, bcCuenta);
                                totales.calculaRetencionesHistorico(); //Calcula todas las retenciones por unidad y ambito
                                totales.calculaAportaciones(); //Calcula todas las retenciones por unidad y ambito
                            }
                        }
                        if ((Integer.parseInt(arregloParametros[0].substring(6)) > Integer.parseInt(arregloParametros[2])) && (totales.getTipo_afectacion().equals("C"))) {
                            banDevengado = true;
                        }
                        tipoForma = crs.getString("tipo");
                        cadena = "";
                        if (tipoForma.equals("P")) {
                            if (polizaPresupuestal) {
                                if (regs != 0) { //No trajo registros el query de ctaxliq
                                    totales.setFactor(crs.getInt("factor"));
                                    totales.setTotalBrutoCero(false);
                                    cadena = totales.calculaTotalBrutoHisEjeCapitulo(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "C");
                                    if (!totales.isTotalBrutoCero()) { //No se genera poliza presupuestal 
                                        cadena = "";
                                    } else {
                                        cadena = cadena + totales.calculaTotalBrutoHisEje(conexionContabilidad, Integer.parseInt(crs.getString("idforma")));  //Recorre histeje para calcular total bruto, total neto y cas 
                                    }
                                }
                            }
                        } else if ((banDevengado == false && !(crs.getString("forma").equals("YS"))) || (polizaPresupuestal == false && banDevengado && !(crs.getString("forma").equals("YE")))) {  //Procesa formas financieras                      
                            cadena = "";
                            if (regs != 0) { //No trajo registros el query de ctaxliq
                                if (verificaForma(crs.getString("forma"), ambitoCuenta)) {
                                    totales.setFactor(crs.getInt("factor"));
                                    totales.setFactorNetoNeg(crs.getInt("FactorNetoNeg"));
                                    totales.setTotalNetoCero(false);
                                    cadena = cadena + totales.generaFormaFinaciera(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), ambitoCuenta, tipoCuenta);  //Recorre histeje para calcular total bruto, total neto y cas 
                                    cadena = cadena + totales.calculaTotalBrutoHisEjeConPartida(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "O");
                                    cadena = cadena + totales.calculaTotalBrutoHisEjeCapitulo(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "C");
                                    if (crs.getString("forma").equals("YS")) {
                                        cadena = cadena + totales.recorreTotalRetenciones();  // Recorre todas las retenciones para la forma               
                                        cadena = cadena + totales.recorreTotalAportaciones();  // Recorre todas las retenciones para la forma               
                                    }
                                    if ((totales.isTotalNetoCero() == false) || (totales.verificaProgramaBancoMundial() && (crs.getString("forma").equals("YE")))) {
                                        cadena = "";
                                    }
                                }
                            }
                        } else {
                            cadena = "";
                        }
                        if (!cadena.isEmpty()) {
                            //System.out.println("Evento.procesarEvento.cadena "+cadena);
                            totales.asignaPropiedadesCtaXLiq();
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), totales.getUnidad().substring(1, 4), totales.getEntidad(), totales.getAmbito().substring(3, 4), arregloParametros[3], totales.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                            liPoliza = resultado.indexOf("***") + 3;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                            hashMap.clear();
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    case 146:  //Evento 146 de MIPF parametros(fecha de envio,estatus,ejercicio,documento,tipo_cxl)  ejemplo: 24/04/2010&4&2010&105011CXL00022&A                  
                    {
                        if (Integer.parseInt(arregloParametros[0].substring(6)) > Integer.parseInt(arregloParametros[2])) {
//                            String fec = "31/12/" + arregloParametros[2];
//                            arregloParametros[0] = fec;
                            ejercicio = arregloParametros[0].substring(6);
                        }
                        TotalesSenado totalesS = new TotalesSenado();
                        totalesS.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[3] + "&" + arregloParametros[4] + "&" + origen + "&" + param5); //estatus, ejercicio , numero de documento, tipoCXL, origen, condicion de pago de pasivos 
                        regs = totalesS.asignaPropiedadesCtaXLiq();
                        if (regs == 0) {//No trajo registros el query de ctaxliq
                            cadena = "";
                        } else {
                            cadena = totalesS.calculaPresupPagado(conexionContabilidad,crs.getString("forma"));
                            cadena = cadena + totalesS.calculaPresupDevengado(conexionContabilidad,crs.getString("forma"));
                            cadena = cadena + totalesS.calculaPresupEjercido(conexionContabilidad,crs.getString("forma"));
                        }
                        if (!cadena.isEmpty()) {
                            //System.out.println("Evento.procesarEvento.cadena "+cadena);
                            totalesS.asignaPropiedadesCtaXLiq();
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), totalesS.getUnidad().substring(1, 4), totalesS.getEntidad(), totalesS.getAmbito().substring(3, 4), arregloParametros[3], totalesS.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                            liPoliza = resultado.indexOf("***") + 3;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                            hashMap.clear();
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    }
                    case 147: //Evento 147 de MIPF parametros(fecha de envio,estatus,ejercicio,documento,tipo_cxl)  ejemplo: 24/04/2010&4&2010&105011CXL00022&A                                             
                    {
                        if (Integer.parseInt(arregloParametros[0].substring(6)) > Integer.parseInt(arregloParametros[2])) {
//                            String fec = "31/12/" + arregloParametros[2];
//                            arregloParametros[0] = fec;
                            ejercicio = arregloParametros[0].substring(6);
                        }
                        TotalesSenado totalesS = new TotalesSenado();
                        totalesS.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[3] + "&" + arregloParametros[4] + "&" + origen + "&" + param5); //estatus, ejercicio , numero de documento, tipoCXL, origen, condicion de pago de pasivos 
                        regs = totalesS.asignaPropiedadesCtaXLiq();
                        if (regs == 0) {//No trajo registros el query de ctaxliq
                            cadena = "";
                        } else {
                            cadena = totalesS.calculaCxpCp(conexionContabilidad);
                            cadena = cadena + totalesS.calculaActivos(conexionContabilidad);
                        }
                        if (!cadena.isEmpty()) {
                            //System.out.println("Evento.procesarEvento.cadena "+cadena);
                            totalesS.asignaPropiedadesCtaXLiq();
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), totalesS.getUnidad().substring(1, 4), totalesS.getEntidad(), totalesS.getAmbito().substring(3, 4), arregloParametros[3], totalesS.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                            liPoliza = resultado.indexOf("***") + 3;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                            hashMap.clear();
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    }
                    case 148:  //Evento 148 de MIPF parametros(fecha de envio, estatus,ejercicio, documento, tipo_cxl)  ejemplo: 24/04/2010&4&2010&105011CXL00022&A                  
                        if (Integer.parseInt(arregloParametros[0].substring(6)) > Integer.parseInt(arregloParametros[2])) {
                            polizaPresupuestal = false;
                            param5 = "--";
                        }
                        if (totales == null) {  //Procesa forma presupuestal                          
                            totales = new Totales();
                            totales.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[3] + "&" + arregloParametros[4] + "&" + origen + "&" + param5); //estatus, ejercicio , numero de documemnto, tipoCXL, origen, condicion de pago de pasivo 
                            regs = totales.asignaPropiedadesCtaXLiq();
                            if (regs != 0) {//trajo registros el query de ctaxliq
                                anticipo = totales.getAnticipo();
                                capitulo = totales.getCapitulo();
                                totales.setEjercicio(arregloParametros[2]);
                                totales.setBanDevengado(banDevengado);
                                bcCuenta = new bcCuentasBancarias();
                                ambitoCuenta = obtieneAmbitoCuenta(conexionContabilidad, totales.getCuenta(), bcCuenta);
                                tipoCuenta = obtieneTipoCuenta(ambitoCuenta, bcCuenta);
                                totales.calculaRetencionesHistorico(); //Calcula todas las retenciones por unidad y ambito
                                totales.calculaAportaciones(); //Calcula todas las retenciones por unidad y ambito
                            }
                        }
                        if ((Integer.parseInt(arregloParametros[0].substring(6)) > Integer.parseInt(arregloParametros[2])) && (totales.getTipo_afectacion().equals("C"))) {
                            banDevengado = true;
                        }
                        tipoForma = crs.getString("tipo");
                        cadena = "";
                        if (tipoForma.equals("P")) {
                            if (polizaPresupuestal) {
                                if (regs != 0) { //No trajo registros el query de ctaxliq
                                    totales.setFactor(crs.getInt("factor"));
                                    totales.setTotalBrutoCero(false);
                                    cadena = totales.calculaTotalBrutoHisEjeCapitulo(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "C");
                                    if (!totales.isTotalBrutoCero()) { //No se genera poliza presupuestal
                                        cadena = "";
                                    } else {
                                        cadena = cadena + totales.calculaTotalBrutoHisEje(conexionContabilidad, Integer.parseInt(crs.getString("idforma")));  //Recorre histeje para calcular total bruto, total neto y cas 
                                    }
                                }
                            }
                        } else if ((banDevengado == false && !(crs.getString("forma").equals("YP"))) || (polizaPresupuestal == false && banDevengado && !(crs.getString("forma").equals("YE")))) {  //Procesa formas financieras    
                            cadena = "";
                            if (regs != 0) { //No trajo registros el query de ctaxliq
                                if (verificaForma(crs.getString("forma"), ambitoCuenta)) {
                                    totales.setFactor(crs.getInt("factor"));
                                    totales.setFactorNetoNeg(crs.getInt("FactorNetoNeg"));
                                    totales.setTotalNetoCero(false);
                                    cadena = cadena + totales.generaFormaFinaciera(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), ambitoCuenta, tipoCuenta);  //Recorre histeje para calcular total bruto, total neto y cas                      
                                    cadena = cadena + totales.calculaTotalBrutoHisEjeConPartida(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "O");
                                    cadena = cadena + totales.calculaTotalBrutoHisEjeCapitulo(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "C");
                                    if (crs.getString("forma").equals("YP")) {
                                        cadena = cadena + totales.recorreTotalRetenciones();  // Recorre todas las retenciones para la forma               
                                    }
                                    if ((totales.isTotalNetoCero() == false) || (totales.verificaProgramaBancoMundial() && (crs.getString("forma").equals("YE")))) {
                                        cadena = "";
                                    }
                                }
                            }
                        } else {
                            cadena = "";
                        }
                        if (!cadena.isEmpty()) {
                            //System.out.println("Evento.procesarEvento.cadena "+cadena);
                            totales.asignaPropiedadesCtaXLiq();
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), totales.getUnidad().substring(1, 4), totales.getEntidad(), totales.getAmbito().substring(3, 4), arregloParametros[3], totales.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                            liPoliza = resultado.indexOf("***") + 3;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                            hashMap.clear();
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    case 149: //Evento 149 de MIPF parametros(fecha de envio,estatus,ejercicio,documento,tipo_cxl)  ejemplo: 24/04/2010&4&2010&105011CXL00022&A                  
                    {
                        if (Integer.parseInt(arregloParametros[0].substring(6)) > Integer.parseInt(arregloParametros[2])) {
                            polizaPresupuestal = false;
                            param5 = "--";
                        }
                        TotalesSenado totalesS = new TotalesSenado();
                        totalesS.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[3] + "&" + arregloParametros[4] + "&" + origen + "&" + param5); //estatus, ejercicio , numero de documemnto, tipoCXL, origen, condicion de pago de pasivo 
                        regs = totalesS.asignaPropiedadesCtaXLiq();
                        cadena = "";
                        if (regs != 0 && arregloParametros[1].equals("13")) {//No trajo registros el query de ctaxliq
                            cadena = totalesS.calculaPresupDevengado(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaPresupEjercido(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaCxpCp(conexionContabilidad);
                            cadena = cadena + totalesS.calculaActivos(conexionContabilidad);
                            cadena = cadena + totalesS.calculaPresupPagado(conexionContabilidad, crs.getString("forma"));
                        }
                        if (!cadena.isEmpty()) {
                            try {
                                totalesS.asignaPropiedadesCtaXLiq();
                                preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), totalesS.getUnidad().substring(1, 4), totalesS.getEntidad(), totalesS.getAmbito().substring(3, 4), arregloParametros[3], totalesS.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                                resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                                liPoliza = resultado.indexOf("***") + 3;
                                lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                                hashMap.clear();
                            } catch (Exception e) {
                            }
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    }
                    case 150: //Evento 150 de MIPF parametros(fecha de envio,estatus,ejercicio,documento,tipo_cxl)  ejemplo: 24/04/2010&4&2010&105011CXL00022&A                  
                    {
                        if (Integer.parseInt(arregloParametros[0].substring(6)) > Integer.parseInt(arregloParametros[2])) {
//                            String fec = "31/12/" + arregloParametros[2];
//                            arregloParametros[0] = fec;
                            ejercicio = arregloParametros[0].substring(6);
                        }
                        TotalesSenado totalesS = new TotalesSenado();
                        totalesS.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[3] + "&" + arregloParametros[4] + "&" + origen + "&" + param5); //estatus, ejercicio , numero de documento, tipoCXL, origen, condicion de pago de pasivos 
                        regs = totalesS.asignaPropiedadesCtaXLiq();
                        cadena = "";
                        if (regs > 0) {//Trajo registros el query de ctaxliq
                            if (arregloParametros[1].equals("13")) {
                                cadena = totalesS.calculaPresupEjercido(conexionContabilidad, crs.getString("forma"));
                                cadena = cadena + totalesS.calculaPresupDevengado(conexionContabilidad, crs.getString("forma"));
                                cadena = cadena + totalesS.calculaCxpCp(conexionContabilidad);
                                cadena = cadena + totalesS.calculaActivos(conexionContabilidad);
//                            } else {
                                cadena = cadena + totalesS.calculaPresupPagado(conexionContabilidad, crs.getString("forma"));
//                                cadena = cadena + totalesS.calculaPresupEjercido(conexionContabilidad, crs.getString("forma"));
                                if(crs.getString("forma").equals("CL") && cadena.contains("IMPORTE_PRESUP_PAGADO=0.0|") && cadena.contains("IMPORTE_PRESUP_EJERCIDO=0.0|")) {
                                    cadena = "";
                                }
                            }
                        }
                        if (!cadena.isEmpty()) {
                            //System.out.println("Evento.procesarEvento.cadena "+cadena);
                            try {
                                totalesS.asignaPropiedadesCtaXLiq();
                                preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), totalesS.getUnidad().substring(1, 4), totalesS.getEntidad(), totalesS.getAmbito().substring(3, 4), arregloParametros[3], totalesS.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                                resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                                liPoliza = resultado.indexOf("***") + 3;
                                lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                                hashMap.clear();
                            } catch (Exception e) {
                            }
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    }
                    case 151: //Evento 151 de MIPF parametros(fecha de envio,estatus,ejercicio,documento,tipo_cxl)  ejemplo: 24/04/2010&4&2010&105011CXL00022&A                  
                    {
                        if (Integer.parseInt(arregloParametros[0].substring(6)) > Integer.parseInt(arregloParametros[2])) {
                            polizaPresupuestal = false;
                            param5 = "--";
                        }
                        TotalesSenado totalesS = new TotalesSenado();
                        totalesS.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[3] + "&" + arregloParametros[4] + "&" + origen + "&" + param5); //estatus, ejercicio , numero de documemnto, tipoCXL, origen, condicion de pago de pasivo 
                        regs = totalesS.asignaPropiedadesCtaXLiq();
                        cadena = "";
                        if (regs != 0 && arregloParametros[1].equals("13")) {//No trajo registros el query de ctaxliq
                            cadena = totalesS.calculaPresupDevengado(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaPresupEjercido(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaCxpCp(conexionContabilidad);
                            cadena = cadena + totalesS.calculaActivos(conexionContabilidad);
                            cadena = cadena + totalesS.calculaPresupPagado(conexionContabilidad, crs.getString("forma"));
                        }
                        if (!cadena.isEmpty()) {
                            try {
                                totalesS.asignaPropiedadesCtaXLiq();
                                preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), totalesS.getUnidad().substring(1, 4), totalesS.getEntidad(), totalesS.getAmbito().substring(3, 4), arregloParametros[3], totalesS.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                                resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                                liPoliza = resultado.indexOf("***") + 3;
                                lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                                hashMap.clear();
                            } catch (Exception e) {
                            }
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    }
                    case 152: //Evento 152 de MIPF parametros(fecha de envio, estatus,ejercicio, documento, tipo_ar) ejemplo: 24/10/2004&4&2010&10912109011ARE00351&D
                    {
                        if (Integer.parseInt(arregloParametros[0].substring(6)) > Integer.parseInt(arregloParametros[2])) {
                            polizaPresupuestal = false;
                            param5 = "--";
                            banDevengado = true;
                        }
                        cadena = "";
                        TotalesSenado totalesS = new TotalesSenado();
                        totalesS.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[3] + "&" + arregloParametros[4] + "&" + origen + "&" + param5); //estatus, ejercicio , numero de documento, tipoCXL, origen, condicion de pago de pasivos 
                        if (totalesS.asignaPropiedadesCtaXLiq() > 0) {//trajo registros el query de ctaxliq
//                            anticipo = totalesS.getAnticipo();
//                            capitulo = totales.getCapitulo();
//                            totalesS.setFactor(crs.getInt("factor"));
//                            totales.setEjercicio(arregloParametros[2]);
                            System.out.println("Evento.procesarEvento.152 ");
                            cadena = totalesS.calculaCxpCp(conexionContabilidad);
                            cadena = cadena + totalesS.calculaActivos(conexionContabilidad);
                        }
                        if (!cadena.isEmpty()) {
                            //System.out.println("Evento.procesarEvento.cadena "+cadena);
                            totalesS.asignaPropiedadesCtaXLiq();
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), totalesS.getUnidad().substring(1, 4), totalesS.getEntidad(), totalesS.getAmbito().substring(3, 4), arregloParametros[3], totalesS.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                            liPoliza = resultado.indexOf("***") + 3;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                            hashMap.clear();
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                }
                    case 153:  //Evento 153 de MIPF parametros(fecha de envio,estatus,ejercicio,documento,tipo_cxl)  ejemplo: 24/04/2010&4&2010&105011CXL00022&A                               
                    {
                        if (Integer.parseInt(arregloParametros[0].substring(6)) > Integer.parseInt(arregloParametros[2])) {
//                            String fec = "31/12/" + arregloParametros[2];
//                            arregloParametros[0] = fec;
                            ejercicio = arregloParametros[0].substring(6);
                        }
                        TotalesSenado totalesS = new TotalesSenado();
                        totalesS.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[3] + "&" + arregloParametros[4] + "&" + origen + "&" + param5); //estatus, ejercicio , numero de documento, tipoCXL, origen, condicion de pago de pasivos 
                        regs = totalesS.asignaPropiedadesCtaXLiq();
                        if (regs == 0) {//No trajo registros el query de ctaxliq
                            cadena = "";
                        } else {
                            cadena = totalesS.calculaActivos(conexionContabilidad);
                            cadena = cadena + totalesS.calculaCxpCp(conexionContabilidad);
                            cadena = cadena + totalesS.calculaDivisas(conexionContabilidad);
                        }
                        if (!cadena.isEmpty()) {
                            //System.out.println("Evento.procesarEvento.cadena "+cadena);
                            totalesS.asignaPropiedadesCtaXLiq();
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), totalesS.getUnidad().substring(1, 4), totalesS.getEntidad(), totalesS.getAmbito().substring(3, 4), arregloParametros[3], totalesS.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                            liPoliza = resultado.indexOf("***") + 3;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                            hashMap.clear();
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    }
                    case 154:  //Evento 154 de MIPF parametros(fecha de envio, estatus,ejercicio, documento, tipo_cxl)  ejemplo: 24/04/2010&4&2010&105011CXL00022&A                  
                        if (Integer.parseInt(arregloParametros[0].substring(6)) > Integer.parseInt(arregloParametros[2])) {
                            polizaPresupuestal = false;
                            param5 = "--";
                        }
                        if (totales == null) {  //Procesa forma presupuestal                          
                            totales = new Totales();
                            totales.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[3] + "&" + arregloParametros[4] + "&" + origen + "&" + param5); //estatus, ejercicio , numero de documemnto, tipoCXL, origen, condicion de pago de pasivo 
                            regs = totales.asignaPropiedadesCtaXLiq();
                            if (regs != 0) {//No trajo registros el query de ctaxliq
                                anticipo = totales.getAnticipo();
                                capitulo = totales.getCapitulo();
                                totales.setEjercicio(arregloParametros[2]);
                                totales.setBanDevengado(banDevengado);
                                bcCuenta = new bcCuentasBancarias();
                                ambitoCuenta = obtieneAmbitoCuenta(conexionContabilidad, totales.getCuenta(), bcCuenta);
                                tipoCuenta = obtieneTipoCuenta(ambitoCuenta, bcCuenta);
                                totales.calculaRetencionesHistorico(); //Calcula todas las retenciones por unidad y ambito
                                totales.calculaAportaciones(); //Calcula todas las retenciones por unidad y ambito
                            }
                        }
                        if ((Integer.parseInt(arregloParametros[0].substring(6)) > Integer.parseInt(arregloParametros[2])) && (totales.getTipo_afectacion().equals("C"))) {
                            banDevengado = true;
                        }
                        tipoForma = crs.getString("tipo");
                        cadena = "";
                        if (tipoForma.equals("P")) {
                            if (polizaPresupuestal) {
                                if (regs != 0) { //No trajo registros el query de ctaxliq
                                    totales.setFactor(crs.getInt("factor"));
                                    totales.setTotalBrutoCero(false);
                                    cadena = totales.calculaTotalBrutoHisEjeCapitulo(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "C");
                                    if (!totales.isTotalBrutoCero()) { //No se genera poliza presupuestal 
                                        cadena = "";
                                    } else {
                                        cadena = cadena + totales.calculaTotalBrutoHisEje(conexionContabilidad, Integer.parseInt(crs.getString("idforma")));  //Recorre histeje para calcular total bruto, total neto y cas 
                                    }
                                }
                            }
                        } else if ((banDevengado == false && !(crs.getString("forma").equals("YP"))) || (polizaPresupuestal == false && banDevengado && !(crs.getString("forma").equals("YE")))) {  //Procesa formas financieras    
                            cadena = "";
                            if (regs != 0) { //No trajo registros el query de ctaxliq
                                if (verificaForma(crs.getString("forma"), ambitoCuenta)) {
                                    totales.setFactor(crs.getInt("factor"));
                                    totales.setFactorNetoNeg(crs.getInt("FactorNetoNeg"));
                                    totales.setTotalNetoCero(false);
                                    cadena = cadena + totales.generaFormaFinaciera(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), ambitoCuenta, tipoCuenta);  //Recorre histeje para calcular total bruto, total neto y cas 
                                    cadena = cadena + totales.calculaTotalBrutoHisEjeConPartida(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "O");
                                    cadena = cadena + totales.calculaTotalBrutoHisEjeCapitulo(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "C");
                                    if (crs.getString("forma").equals("YP")) {
                                        cadena = cadena + totales.recorreTotalRetenciones();  // Recorre todas las retenciones para la forma               
                                    }
                                    if ((totales.isTotalNetoCero() == false) || (totales.verificaProgramaBancoMundial() && (crs.getString("forma").equals("YE")))) {
                                        cadena = "";
                                    }
                                }
                            }
                        } else {
                            cadena = "";
                        }
                        if (!cadena.isEmpty()) {
                            //System.out.println("Evento.procesarEvento.cadena "+cadena);
                            totales.asignaPropiedadesCtaXLiq();
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), totales.getUnidad().substring(1, 4), totales.getEntidad(), totales.getAmbito().substring(3, 4), arregloParametros[3], totales.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                            liPoliza = resultado.indexOf("***") + 3;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                            hashMap.clear();
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    case 155:  //Evento 155 de MIPF parametros(fecha de envio,estatus,ejercicio,documento,tipo_cxl)  ejemplo: 24/04/2010&4&2010&105011CXL00022&A                  
                        if (Integer.parseInt(arregloParametros[0].substring(6)) > Integer.parseInt(arregloParametros[2])) {
//                            String fec = "31/12/" + arregloParametros[2];
//                            arregloParametros[0] = fec;
                            ejercicio = arregloParametros[0].substring(6);
                        }
                        if (totales == null) {  //Procesa forma presupuestal                          
                            totales = new Totales();
                            totales.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[3] + "&" + arregloParametros[4] + "&" + origen + "&" + param5); //estatus, ejercicio , numero de documento, tipoCXL, origen, condicion de pago de pasivos
                            regs = totales.asignaPropiedadesCtaXLiq();
                            if (regs == 0) {//No trajo registros el query de ctaxliq
                                cadena = "";
                            } else {
                                anticipo = totales.getAnticipo();
                                capitulo = totales.getCapitulo();
                                totales.setEjercicio(arregloParametros[2]);
                                bcCuenta = new bcCuentasBancarias();
                                ambitoCuenta = obtieneAmbitoCuenta(conexionContabilidad, totales.getCuenta(), bcCuenta);
                                tipoCuenta = obtieneTipoCuenta(ambitoCuenta, bcCuenta);
                                totales.calculaRetencionesHistorico(); //Calcula todas las retenciones por unidad y ambito
                                totales.calculaAportaciones(); //Calcula todas las retenciones por unidad y ambito
                            }
                        }
                        tipoForma = crs.getString("tipo");
                        if (tipoForma.equals("P")) {
                            if (regs != 0) { //No trajo registros el query de ctaxliq
                                totales.setFactor(crs.getInt("factor"));
                                totales.verificaProveedor(conexionContabilidad, registro, arregloParametros[0], crs.getString("catalogocuenta"), "2112", "5");
                                totales.setTotalBrutoCero(false);
                                cadena = totales.calculaTotalBrutoHisEjeCapitulo(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "C");
                                if (!totales.isTotalBrutoCero()) { //No se genera poliza presupuestal 
                                    cadena = "";
                                } else {
                                    cadena = cadena + totales.calculaTotalBrutoHisEje(conexionContabilidad, Integer.parseInt(crs.getString("idforma")));  //Recorre histeje para calcular total bruto, total neto y cas 
                                    cadena = cadena + totales.calculaTotalBrutoHisEjeConPartida(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "O");
                                    cadena = cadena + totales.calculaTotalBrutoHisEjePartidaGen(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "P");
                                    cadena = cadena + totales.recorreTotalRetenciones();  // Recorre todas las retenciones para la forma               
                                    cadena = cadena + totales.recorreTotalAportaciones();  // Recorre todas las retenciones para la forma                                     
                                }
                            }
                        } else {  //Procesa formas financieras
                            cadena = "";
                            if (regs != 0) { //No trajo registros el query de ctaxliq
                                if (verificaForma(crs.getString("forma"), ambitoCuenta)) {
                                    totales.setFactor(crs.getInt("factor"));
                                    totales.setTotalNetoCero(false);
                                    cadena = cadena + totales.generaFormaFinaciera(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), ambitoCuenta, tipoCuenta);  //Recorre histeje para calcular total bruto, total neto y cas 
                                    cadena = cadena + totales.calculaTotalBrutoHisEjeConPartida(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "O");
                                    cadena = cadena + totales.calculaTotalBrutoHisEjeCapitulo(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "C");
                                    if ((totales.isTotalNetoCero() == false) || (totales.verificaProgramaBancoMundial() && (crs.getString("forma").equals("YN")))) {
                                        cadena = "";
                                    }
                                }
                            }
                        }
                        if (!cadena.isEmpty()) {
                            //System.out.println("Evento.procesarEvento.cadena "+cadena);
                            totales.asignaPropiedadesCtaXLiq();
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), totales.getUnidad().substring(1, 4), totales.getEntidad(), totales.getAmbito().substring(3, 4), arregloParametros[3], totales.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                            liPoliza = resultado.indexOf("***") + 3;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                            hashMap.clear();
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    case 156:  //Evento 156 de MIPF parametros(fecha de envio,estatus,ejercicio,documento,tipo_cxl)  ejemplo: 24/04/2010&4&2010&105011CXL00022&A                  
                    {
                        if (Integer.parseInt(arregloParametros[0].substring(6)) > Integer.parseInt(arregloParametros[2])) {
//                            String fec = "31/12/" + arregloParametros[2];
//                            arregloParametros[0] = fec;
                            ejercicio = arregloParametros[0].substring(6);
                        }
                        TotalesSenado totalesS = new TotalesSenado();
                        totalesS.setFactor(crs.getInt("factor"));
                        totalesS.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[3] + "&" + arregloParametros[4] + "&" + origen + "&" + param5); //estatus, ejercicio , numero de documento, tipoCXL, origen, condicion de pago de pasivos 
                        regs = totalesS.asignaPropiedadesCtaXLiq();
                        if (regs == 0) {//No trajo registros el query de ctaxliq
                            cadena = "";
                        } else {
                            cadena = totalesS.calculaRetenciones(conexionContabilidad, crs.getString("forma"), eventoContable);
                            cadena = cadena + totalesS.calculaAportaciones(conexionContabilidad);
                            cadena = cadena + totalesS.calculaGasto(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaCxpCp(conexionContabilidad);
                            cadena = cadena + totalesS.calculaPresupPorEjercer(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaPresupComprometido2(conexionContabilidad);
                            cadena = cadena + totalesS.calculaPresupComprometido(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaPresupDevengado(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaPresupEjercido(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaActivos(conexionContabilidad);
                        }
                        if (!cadena.isEmpty()) {
                            //System.out.println("Evento.procesarEvento.cadena "+cadena);
                            totalesS.asignaPropiedadesCtaXLiq();
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), totalesS.getUnidad().substring(1, 4), totalesS.getEntidad(), totalesS.getAmbito().substring(3, 4), arregloParametros[3], totalesS.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                            liPoliza = resultado.indexOf("***") + 3;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                            hashMap.clear();
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    }
                    case 157:  //Evento 157 de MIPF parametros(fecha de envio,estatus,ejercicio,documento,tipo_cxl)  ejemplo: 24/04/2010&4&2010&105011CXL00022&A                  
                    {
                        if (Integer.parseInt(arregloParametros[0].substring(6)) > Integer.parseInt(arregloParametros[2])) {
//                            String fec = "31/12/" + arregloParametros[2];
//                            arregloParametros[0] = fec;
                            ejercicio = arregloParametros[0].substring(6);
                        }
                        TotalesSenado totalesS = new TotalesSenado();
                        String cxp = "";
                        totalesS.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[3] + "&" + arregloParametros[4] + "&" + origen + "&" + param5); //estatus, ejercicio , numero de documento, tipoCXL, origen, condicion de pago de pasivos 
                        if (totalesS.asignaPropiedadesCtaXLiq() == 0) {//No trajo registros el query de ctaxliq
                            cadena = "";
                        } else {
                            anticipo = totalesS.getAnticipo();
//                            capitulo = totales.getCapitulo();
                            totalesS.setFactor(crs.getInt("factor"));
//                            totales.setEjercicio(arregloParametros[2]);
                            System.out.println("Evento.procesarEvento.157.anticipo " + anticipo);
                            cadena = totalesS.calculaGasto(conexionContabilidad, crs.getString("forma"));
                            cxp = totalesS.calculaCxpCp(conexionContabilidad);
                            cadena = cadena + cxp;
                            cadena = cadena + totalesS.calculaRetenciones(conexionContabilidad, crs.getString("forma"), eventoContable);
                            cadena = cadena + totalesS.calculaPresupDevengado(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaPresupComprometido(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaPresupComprometido2(conexionContabilidad);
                            cadena = cadena + totalesS.calculaPresupPorEjercer(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaPresupEjercido(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaActivos(conexionContabilidad);
                            cadena = cadena + totalesS.calculaPresupPagado(conexionContabilidad, crs.getString("forma"));
                        }
                        //System.out.println("*******************\n" + crs.getString("forma") + "\n***************");
                        if (crs.getString("forma").equals("CC") && !cxp.isEmpty()) {
                            cadena = "";
                        } else if (crs.getString("forma").equals("CA") && cxp.isEmpty()) {
                            cadena = "";
                        }
                        if (!cadena.isEmpty()) {
                            //System.out.println("Evento.procesarEvento.cadena "+cadena);
                            totalesS.asignaPropiedadesCtaXLiq();
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), totalesS.getUnidad().substring(1, 4), totalesS.getEntidad(), totalesS.getAmbito().substring(3, 4), arregloParametros[3], totalesS.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                            liPoliza = resultado.indexOf("***") + 3;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                            hashMap.clear();
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    }
                    case 158:  //Evento 158 de MIPF parametros(fecha de envio, estatus,ejercicio, documento, tipo_ar) ejemplo: 24/10/2004&4&2010&10912109011ARE00351&D                   
                    {
                        if (Integer.parseInt(arregloParametros[0].substring(6)) > Integer.parseInt(arregloParametros[2])) {
//                            String fec = "31/12/" + arregloParametros[2];
//                            arregloParametros[0] = fec;
                            ejercicio = arregloParametros[0].substring(6);
                        }
                        cadena = "";
                        TotalesSenado totalesS = new TotalesSenado();
                        totalesS.setFactor(crs.getInt("factor"));
                        totalesS.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[3] + "&" + arregloParametros[4] + "&" + origen + "&" + param5); //estatus, ejercicio , numero de documento, tipoCXL, origen, condicion de pago de pasivos 
                        if (totalesS.asignaPropiedadesCtaXLiq() > 0) {//trajo registros el query de ctaxliq
                            System.out.println("Evento.procesarEvento.158.anticipo " + anticipo);
                            cadena = totalesS.calculaCxpCp(conexionContabilidad);
                            cadena = cadena + totalesS.calculaDeudores(conexionContabilidad);
                            cadena = cadena + totalesS.calculaPresupDevengado(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaPresupComprometido(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaPresupPorEjercer(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaActivos(conexionContabilidad);
                       }
                        if (!cadena.isEmpty()) {
                            System.out.println("Evento.procesarEvento.cadena " + cadena);
                            totalesS.asignaPropiedadesCtaXLiq();
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), totalesS.getUnidad().substring(1, 4), totalesS.getEntidad(), totalesS.getAmbito().substring(3, 4), arregloParametros[3], totalesS.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                            liPoliza = resultado.indexOf("***") + 3;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                            hashMap.clear();
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    }
                    case 159:  //Evento 159 de MIPF parametros(fecha de envio,estatus,ejercicio,documento,tipo_cxl)  ejemplo: 24/04/2010&4&2010&105011CXL00022&A                               
                        if (Integer.parseInt(arregloParametros[0].substring(6)) > Integer.parseInt(arregloParametros[2])) {
//                            String fec = "31/12/" + arregloParametros[2];
//                            arregloParametros[0] = fec;
                            ejercicio = arregloParametros[0].substring(6);
                        }
                        if (totales == null) {
                            totales = new Totales();
                            totales.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[3] + "&" + arregloParametros[4] + "&" + origen + "&" + param5); //estatus, ejercicio , numero de documento, tipoCXL, origen, condicion de pago de pasivos
                            regs = totales.asignaPropiedadesCtaXLiq();
                            if (regs == 0) {//No trajo registros el query de ctaxliq
                                cadena = "";
                            } else {
                                anticipo = totales.getAnticipo();
                                capitulo = totales.getCapitulo();
                                totales.setEjercicio(arregloParametros[2]);
                                bcCuenta = new bcCuentasBancarias();
                                ambitoCuenta = obtieneAmbitoCuenta(conexionContabilidad, totales.getCuenta(), bcCuenta);
                                tipoCuenta = obtieneTipoCuenta(ambitoCuenta, bcCuenta);
                                totales.calculaRetencionesHistorico(); //Calcula todas las retenciones por unidad y ambito
                                totales.calculaAportaciones(); //Calcula todas las retenciones por unidad y ambito                
                            }
                        }
                        tipoForma = crs.getString("tipo");
                        if (tipoForma.equals("P")) {
                            if (regs != 0) {//No trajo registros el query de ctaxliq
                                totales.setFactor(crs.getInt("factor"));
                                cadena = "";
                                cadena = cadena + totales.calculaTotalBrutoHisEjeConPartida(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "O");
                                totales.verificaProveedor(conexionContabilidad, registro, arregloParametros[0], crs.getString("catalogocuenta"), "2112", "5");
                                totales.setTotalBrutoCero(false);
                                cadena = cadena + totales.calculaTotalBrutoHisEjeCapitulo(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "C");
                                if (!totales.isTotalBrutoCero()) { //No se genera poliza presupuestal 
                                    cadena = "";
                                } else {
                                    cadena = cadena + totales.calculaTotalBrutoHisEje(conexionContabilidad, Integer.parseInt(crs.getString("idforma")));  //Recorre histeje para calcular total bruto, total neto y cas 
                                    cadena = cadena + totales.recorreTotalRetenciones();  // Recorre todas las retenciones para la forma               
                                    cadena = cadena + totales.recorreTotalAportaciones();  // Recorre todas las retenciones para la forma               
                                }
                            }
                        } else {  //Procesa formas financieras
                            cadena = "";
                            if (regs != 0) { //No trajo registros el query de ctaxliq
                                if (verificaForma(crs.getString("forma"), ambitoCuenta)) {
                                    totales.setFactor(crs.getInt("factor"));
                                    cadena = cadena + totales.generaFormaFinaciera(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), ambitoCuenta, tipoCuenta);  //Recorre histeje para calcular total bruto, total neto y cas 
                                    cadena = cadena + totales.calculaTotalBrutoHisEjeConPartida(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "O");
                                    cadena = cadena + totales.calculaTotalBrutoHisEjeCapitulo(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "C");
                                    if ((totales.verificaProgramaBancoMundial() && (crs.getString("forma").equals("YN")))) {
                                        cadena = "";
                                    }
                                }
                            }
                        }
                        if (!cadena.isEmpty()) {
                            //System.out.println("Evento.procesarEvento.cadena "+cadena);
                            totales.asignaPropiedadesCtaXLiq();
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), totales.getUnidad().substring(1, 4), totales.getEntidad(), totales.getAmbito().substring(3, 4), arregloParametros[3], totales.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                            liPoliza = resultado.indexOf("***") + 3;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                            hashMap.clear();
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    case 161:  //Evento 161 de MIPF parametros(fecha de envio, estatus,ejercicio, documento, tipo_cxl)  ejemplo: 24/04/2010&4&2010&105011CXL00022&A                  
                        if (Integer.parseInt(arregloParametros[0].substring(6)) > Integer.parseInt(arregloParametros[2])) {
//                            String fec = "31/12/" + arregloParametros[2];
//                            arregloParametros[0] = fec;
                            ejercicio = arregloParametros[0].substring(6);
                        }
                        if (totales == null) {
                            totales = new Totales();
                            totales.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[3] + "&" + arregloParametros[4] + "&" + origen + "&" + param5); //estatus, ejercicio , numero de documento, tipoCXL, origen, condicion de pago de pasivos
                            regs = totales.asignaPropiedadesCtaXLiq();
                            if (regs == 0) {//No trajo registros el query de ctaxliq
                                cadena = "";
                            } else {
                                anticipo = totales.getAnticipo();
                                capitulo = totales.getCapitulo();
                                totales.setEjercicio(arregloParametros[2]);
                                bcCuenta = new bcCuentasBancarias();
                                ambitoCuenta = obtieneAmbitoCuenta(conexionContabilidad, totales.getCuenta(), bcCuenta);
                                tipoCuenta = obtieneTipoCuenta(ambitoCuenta, bcCuenta);
                                totales.calculaRetencionesHistorico(); //Calcula todas las retenciones por unidad y ambito
                                totales.calculaAportaciones(); //Calcula todas las retenciones por unidad y ambito
                            }
                        }
                        tipoForma = crs.getString("tipo");
                        if (tipoForma.equals("P")) {
                            if (regs != 0) { //No trajo registros el query de ctaxliq
                                totales.setFactor(crs.getInt("factor"));
                                totales.setEjercicio(arregloParametros[2]);
                                if (anticipo.equals("0")) {  // NO ES F ACTURA
                                    cadena = "";
                                    cadena = cadena + totales.calculaTotalBrutoHisEjePartidaGen(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "P");
                                    totales.verificaProveedor(conexionContabilidad, registro, arregloParametros[0], crs.getString("catalogocuenta"), "2112", "5");
                                    totales.setTotalBrutoCero(false);
                                    cadena = cadena + totales.calculaTotalBrutoHisEjeCapitulo(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "C");
                                    if (!totales.isTotalBrutoCero()) { //No se genera poliza presupuestal 
                                        cadena = "";
                                    } else {
                                        cadena = cadena + totales.calculaTotalBrutoHisEje(conexionContabilidad, Integer.parseInt(crs.getString("idforma")));  //Recorre histeje para calcular total bruto, total neto y cas                     
                                        cadena = cadena + totales.calculaTotalBrutoHisEjeConPartida(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "O");
                                        cadena = cadena + totales.recorreTotalRetenciones();  // Recorre todas las retenciones para la forma               
                                        cadena = cadena + totales.recorreTotalAportaciones();  // Recorre todas las retenciones para la forma                                    
                                    }
                                } else {
                                    cadena = "";
                                    cadena = cadena + totales.calculaTotalFacturaCapitulo(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "F", registro, arregloParametros[0], crs.getString("catalogocuenta"));
                                    cadena = cadena + totales.calculaTotalFacturaConPartida(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "N", registro, arregloParametros[0], crs.getString("catalogocuenta"));
                                    cadena = cadena + totales.calculaTotalBrutoHisEjeCapitulo(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "C");
                                    cadena = cadena + totales.calculaTotalBrutoHisEje(conexionContabilidad, Integer.parseInt(crs.getString("idforma")));  //Recorre histeje para calcular total bruto, total neto y cas 
                                }
                            }
                        } else {  //Procesa formas financieras
                            cadena = "";
                            if (regs != 0) { //No trajo registros el query de ctaxliq
                                if (verificaForma(crs.getString("forma"), ambitoCuenta)) {
                                    totales.setFactor(crs.getInt("factor"));
                                    cadena = cadena + totales.generaFormaFinaciera(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), ambitoCuenta, tipoCuenta);  //Recorre histeje para calcular total bruto, total neto y cas 
                                    cadena = cadena + totales.calculaTotalBrutoHisEjeConPartida(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "O");
                                    cadena = cadena + totales.calculaTotalBrutoHisEjeCapitulo(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "C");
                                    if ((totales.verificaProgramaBancoMundial() && (crs.getString("forma").equals("YN")))) {
                                        cadena = "";
                                    }
                                }
                            }
                        }
                        if (!cadena.isEmpty()) {
                            //System.out.println("Evento.procesarEvento.cadena "+cadena);
                            totales.asignaPropiedadesCtaXLiq();
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), totales.getUnidad().substring(1, 4), totales.getEntidad(), totales.getAmbito().substring(3, 4), arregloParametros[3], totales.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                            liPoliza = resultado.indexOf("***") + 3;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                            hashMap.clear();
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    case 162:  //Evento 162 de MIPF parametros(fecha de envio, estatus,ejercicio, documento, tipo_ar) ejemplo: 24/10/2004&4&2010&10912109011ARE00351&D                                 
                    {
                        if (Integer.parseInt(arregloParametros[0].substring(6)) > Integer.parseInt(arregloParametros[2])) {
//                            String fec = "31/12/" + arregloParametros[2];
//                            arregloParametros[0] = fec;
                            ejercicio = arregloParametros[0].substring(6);
                        }
                        TotalesSenado totalesS = new TotalesSenado();
                        totalesS.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[3] + "&" + arregloParametros[4] + "&" + origen + "&" + param5); //estatus, ejercicio , numero de documento, tipoCXL, origen, condicion de pago de pasivos 
                        regs = totalesS.asignaPropiedadesCtaXLiq();
                        if (regs == 0) {//No trajo registros el query de ctaxliq
                            cadena = "";
                        } else {
                            cadena = totalesS.calculaPresupPagado(conexionContabilidad,crs.getString("forma"));
                            cadena = cadena + totalesS.calculaPresupDevengado(conexionContabilidad,crs.getString("forma"));
                            cadena = cadena + totalesS.calculaPresupEjercido(conexionContabilidad,crs.getString("forma"));
                        }
                        if (!cadena.isEmpty()) {
                            //System.out.println("Evento.procesarEvento.cadena "+cadena);
                            totalesS.asignaPropiedadesCtaXLiq();
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), totalesS.getUnidad().substring(1, 4), totalesS.getEntidad(), totalesS.getAmbito().substring(3, 4), arregloParametros[3], totalesS.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                            liPoliza = resultado.indexOf("***") + 3;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                            hashMap.clear();
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    }
                    case 163:  //Evento 163 de MIPF parametros(fecha de envio,estatus,ejercicio,documento,tipo_cxl)  ejemplo: 24/04/2010&4&2010&105011CXL00022&A                                  
                    case 177:  //Evento 177 de MIPF parametros(fecha de envio,estatus,ejercicio,documento,tipo_cxl)  ejemplo: 24/04/2010&4&2010&105011CXL00022&A                                  
                        if (totales == null) {
                            totales = new Totales();
                            totales.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[3] + "&" + arregloParametros[4] + "&" + origen + "&" + param5); //estatus, ejercicio , numero de documento, tipoCXL, origen, condicion de pago de pasivos
                            regs = totales.asignaPropiedadesCtaXLiq();
                            if (regs != 0) {//No trajo registros el query de ctaxliq
                                anticipo = totales.getAnticipo();
                                capitulo = totales.getCapitulo();
                                totales.setBanAre(banAre);
                                totales.setEjercicio(arregloParametros[2]);
                                bcCuenta = new bcCuentasBancarias();
                                ambitoCuenta = obtieneAmbitoCuenta(conexionContabilidad, totales.getCuenta(), bcCuenta);
                                totales.setEntidadAmbitoCuentaBancaria(bcCuenta.getEntidadAmbito());
                                totales.setUnidadCuentaBancaria(bcCuenta.getUnidad_ejecutora());
                                tipoCuenta = obtieneTipoCuenta(ambitoCuenta, bcCuenta);
                                totales.calculaRetencionesHistorico(); //Calcula todas las retenciones por unidad y ambito
                                totales.calculaAportaciones(); //Calcula todas las retenciones por unidad y ambito
                            }
                        }
                        cadena = "";
                        tipoForma = crs.getString("tipo");
                        if (tipoForma.equals("P")) {
                            if (regs != 0) { //No trajo registros el query de ctaxliq
                                totales.setBanAre(banAre);
                                totales.setBanAre_ant(banAre_ant);
                                totales.setFactor(crs.getInt("factor"));
                                totales.setFactorNetoNeg(crs.getInt("FactorNetoNeg"));
                                if (anticipo.equals("0")) {  // NO ES FACTURA
                                    totales.setTotalBrutoCero(false);
                                    cadena = totales.calculaTotalBrutoHisEjeCapitulo(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "C");
                                    if (!totales.isTotalBrutoCero()) { //No se genera poliza presupuestal 
                                        cadena = "";
                                        enviaNotitificacion("Aviso del Sistema de Contabilidad Armonizado se invoco el evento " + eventoContable + " (ARE de A�os Anteriores)con importe bruto = 0, para su revisi�n, no se genero poliza.", "El aviso de reintegro es: " + arregloParametros[3]);
                                    } else {
                                        cadena = cadena + totales.calculaTotalBrutoHisEje(conexionContabilidad, Integer.parseInt(crs.getString("idforma")));  //Recorre histeje para calcular total bruto, total neto y cas 
                                        cadena = cadena + totales.recorreTotalRetenciones();  // Recorre todas las retenciones para la forma               
                                        cadena = cadena + totales.recorreTotalAportaciones();  // Recorre todas las retenciones para la forma               
                                        if (totales.isBantotTodasRetenciones() == false && crs.getString("forma").equals("ZQ")) {
                                            cadena = "";
                                        }
                                    }
                                    if (totales.isTotalNetoCero() == false && crs.getString("forma").equals("ZX")) {
                                        cadena = "";
                                    }
                                }
                            }
                        } else if (tipoForma.equals("F")) {
                            if (verificaForma(crs.getString("forma"), ambitoCuenta)) {
                                totales.setFactor(crs.getInt("factor"));
                                totales.setFactorNetoNeg(crs.getInt("FactorNetoNeg"));
                                totales.setEjercicio(arregloParametros[2]);
                                totales.setTotalNetoCero(false);
                                cadena = cadena + totales.generaFormaFinaciera(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), ambitoCuenta, tipoCuenta);  //Recorre histeje para calcular total bruto, total neto y cas 
                                cadena = cadena + totales.calculaTotalBrutoHisEjeCapitulo(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "C");
                                if (totales.isTotalNetoCero() == false) {
                                    cadena = "";
                                }
                            }
                        }
                        if (!cadena.isEmpty()) {
                            //System.out.println("Evento.procesarEvento.cadena "+cadena);
                            totales.asignaPropiedadesCtaXLiq();
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), totales.getUnidad().substring(1, 4), totales.getEntidad(), totales.getAmbito().substring(3, 4), arregloParametros[3], totales.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                            liPoliza = resultado.indexOf("***") + 3;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                            hashMap.clear();
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    case 164:  //Evento 164 de MIPF parametros(fecha de envio,estatus,ejercicio,documento,tipo_cxl)  ejemplo: 24/04/2010&4&2010&105011CXL00022&A                  
                    case 178:  //Evento 178 de MIPF parametros(fecha de envio,estatus,ejercicio,documento,tipo_cxl)  ejemplo: 24/04/2010&4&2010&105011CXL00022&A                               
                        if (totales == null) {
                            totales = new Totales();
                            totales.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[3] + "&" + arregloParametros[4] + "&" + origen + "&" + param5); //estatus, ejercicio , numero de documento, tipoCXL, origen, condicion de pago de pasivos
                            regs = totales.asignaPropiedadesCtaXLiq();
                            if (regs != 0) {//trajo registros el query de ctaxliq
                                anticipo = totales.getAnticipo();
                                capitulo = totales.getCapitulo();
                                totales.setBanAre(banAre);
                                totales.setEjercicio(arregloParametros[2]);
                                bcCuenta = new bcCuentasBancarias();
                                ambitoCuenta = obtieneAmbitoCuenta(conexionContabilidad, totales.getCuenta(), bcCuenta);
                                totales.setEntidadAmbitoCuentaBancaria(bcCuenta.getEntidadAmbito());
                                totales.setUnidadCuentaBancaria(bcCuenta.getUnidad_ejecutora());
                                tipoCuenta = obtieneTipoCuenta(ambitoCuenta, bcCuenta);
                                totales.calculaRetencionesHistorico(); //Calcula todas las retenciones por unidad y ambito
                                totales.calculaAportaciones(); //Calcula todas las retenciones por unidad y ambito
                            }
                        }
                        cadena = "";
                        tipoForma = crs.getString("tipo");
                        if (tipoForma.equals("P")) {
                            if (regs != 0) { //No trajo registros el query de ctaxliq
                                totales.setBanAre(banAre);
                                totales.setBanAre_ant(banAre_ant);
                                totales.setFactor(crs.getInt("factor"));
                                totales.setFactorNetoNeg(crs.getInt("FactorNetoNeg"));
                                if (anticipo.equals("0")) {  // NO ES FACTURA
                                    cadena = totales.calculaTotalBrutoHisEjeCapitulo(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "C");
                                    cadena = cadena + totales.calculaTotalBrutoHisEje(conexionContabilidad, Integer.parseInt(crs.getString("idforma")));  //Recorre histeje para calcular total bruto, total neto y cas 
                                    cadena = cadena + totales.recorreTotalRetenciones();  // Recorre todas las retenciones para la forma               
                                    cadena = cadena + totales.recorreTotalAportaciones();  // Recorre todas las retenciones para la forma               
                                    if (totales.isBantotTodasRetenciones() == false && crs.getString("forma").equals("ZQ")) {
                                        cadena = "";
                                    }

                                }
                                if (totales.isTotalNetoCero() == false && crs.getString("forma").equals("ZX")) {
                                    cadena = "";
                                }
                            }
                        } else if (tipoForma.equals("F")) {
                            if (verificaForma(crs.getString("forma"), ambitoCuenta)) {
                                totales.setFactor(crs.getInt("factor"));
                                totales.setFactorNetoNeg(crs.getInt("FactorNetoNeg"));
                                totales.setEjercicio(arregloParametros[2]);
                                totales.setTotalNetoCero(false);
                                cadena = cadena + totales.generaFormaFinaciera(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), ambitoCuenta, tipoCuenta);  //Recorre histeje para calcular total bruto, total neto y cas 
                                cadena = cadena + totales.calculaTotalBrutoHisEjeCapitulo(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "C");
                                if (totales.isTotalNetoCero() == false) {
                                    cadena = "";
                                }
                            }
                        }
                        if (!cadena.isEmpty()) {
                            //System.out.println("Evento.procesarEvento.cadena "+cadena);
                            totales.asignaPropiedadesCtaXLiq();
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), totales.getUnidad().substring(1, 4), totales.getEntidad(), totales.getAmbito().substring(3, 4), arregloParametros[3], totales.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                            liPoliza = resultado.indexOf("***") + 3;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                            hashMap.clear();
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    case 165:  //Evento 165 de MIPF parametros(fecha de envio,estatus,ejercicio,documento,tipo_cxl)  ejemplo: 24/04/2010&4&2010&105011CXL00022&A                  
                    case 166:  //Evento 166 de MIPF parametros(fecha de envio,estatus,ejercicio,documento,tipo_cxl)  ejemplo: 24/04/2010&4&2010&105011CXL00022&A                  
                    case 167:  //Evento 167 de MIPF parametros(fecha de envio,estatus,ejercicio,documento,tipo_cxl)  ejemplo: 24/04/2010&4&2010&105011CXL00022&A                  
                    case 169:  //Evento 169 de MIPF parametros(fecha de envio,estatus,ejercicio,documento,tipo_cxl)  ejemplo: 24/04/2010&4&2010&105011CXL00022&A                  
                    case 179:  //Evento 179 de MIPF parametros(fecha de envio,estatus,ejercicio,documento,tipo_cxl)  ejemplo: 24/04/2010&4&2010&105011CXL00022&A                  
                    case 180:  //Evento 180 de MIPF parametros(fecha de envio,estatus,ejercicio,documento,tipo_cxl)  ejemplo: 24/04/2010&4&2010&105011CXL00022&A                               
                    case 181:  //Evento 181 de MIPF parametros(fecha de envio,estatus,ejercicio,documento,tipo_cxl)  ejemplo: 24/04/2010&4&2010&105011CXL00022&A                               
                        if (totales == null) {
                            totales = new Totales();
                            totales.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[3] + "&" + arregloParametros[4] + "&" + origen + "&" + param5); //estatus, ejercicio , numero de documento, tipoCXL, origen, condicion de pago de pasivos
                            regs = totales.asignaPropiedadesCtaXLiq();
                            if (regs != 0) {//trajo registros el query de ctaxliq
                                anticipo = totales.getAnticipo();
                                capitulo = totales.getCapitulo();
                                totales.setBanAre(banAre);
                                totales.setBanAre_ant(banAre_ant);
                                totales.setFactor(crs.getInt("factor"));
                                totales.setEjercicio(arregloParametros[2]);
                                bcCuenta = new bcCuentasBancarias();
                                ambitoCuenta = obtieneAmbitoCuenta(conexionContabilidad, totales.getCuenta(), bcCuenta);
                                totales.setEntidadAmbitoCuentaBancaria(bcCuenta.getEntidadAmbito());
                                totales.setUnidadCuentaBancaria(bcCuenta.getUnidad_ejecutora());
                                tipoCuenta = obtieneTipoCuenta(ambitoCuenta, bcCuenta);
                                totales.calculaRetencionesHistorico(); //Calcula todas las retenciones por unidad y ambito
                                totales.calculaAportaciones(); //Calcula todas las retenciones por unidad y ambito
                            }
                        }
                        cadena = "";
                        tipoForma = crs.getString("tipo");
                        if (tipoForma.equals("P")) {
                            if (regs != 0) { //No trajo registros el query de ctaxliq
                                totales.setBanAre(banAre);
                                totales.setBanAre_ant(banAre_ant);
                                totales.setFactor(crs.getInt("factor"));
                                totales.setFactorNetoNeg(crs.getInt("FactorNetoNeg"));
                                cadena = totales.calculaTotalBrutoHisEjeCapitulo(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "C");
                                cadena = cadena + totales.calculaTotalBrutoHisEje(conexionContabilidad, Integer.parseInt(crs.getString("idforma")));  //Recorre histeje para calcular total bruto, total neto y cas 
                                cadena = cadena + totales.recorreTotalRetenciones();  // Recorre todas las retenciones para la forma               
                                cadena = cadena + totales.recorreTotalAportaciones();  // Recorre todas las retenciones para la forma                          
                                if (totales.isTotalNetoCero() == false && crs.getString("forma").equals("ZX")) {
                                    cadena = "";
                                }
                            }
                        } else if (tipoForma.equals("F")) {
                            if (verificaForma(crs.getString("forma"), ambitoCuenta)) {
                                totales.setFactor(crs.getInt("factor"));
                                totales.setFactorNetoNeg(crs.getInt("FactorNetoNeg"));
                                totales.setEjercicio(arregloParametros[2]);
                                totales.setTotalNetoCero(false);
                                cadena = cadena + totales.generaFormaFinaciera(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), ambitoCuenta, tipoCuenta);  //Recorre histeje para calcular total bruto, total neto y cas 
                                cadena = cadena + totales.calculaTotalBrutoHisEjeCapitulo(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "C");
                                if (totales.isTotalNetoCero() == false) {
                                    cadena = "";
                                }
                            }
                        }
                        if (!cadena.isEmpty()) {
                            //System.out.println("Evento.procesarEvento.cadena "+cadena);
                            totales.asignaPropiedadesCtaXLiq();
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), totales.getUnidad().substring(1, 4), totales.getEntidad(), totales.getAmbito().substring(3, 4), arregloParametros[3], totales.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                            liPoliza = resultado.indexOf("***") + 3;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                            hashMap.clear();
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    case 168:  //Evento 168 de MIPF parametros(fecha de envio,estatus,ejercicio,documento,tipo_cxl)  ejemplo: 24/04/2010&4&2010&105011CXL00022&A                  
                        if (totales == null) {
                            totales = new Totales();
                            totales.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[3] + "&" + arregloParametros[4] + "&" + origen + "&" + param5); //estatus, ejercicio , numero de documento, tipoCXL, origen, condicion de pago de pasivos
                            regs = totales.asignaPropiedadesCtaXLiq();
                            if (regs != 0) {//trajo registros el query de ctaxliq
                                anticipo = totales.getAnticipo();
                                capitulo = totales.getCapitulo();
                                totales.setBanAre(banAre);
                                totales.setBanAre_ant(banAre_ant);
                                totales.setEjercicio(arregloParametros[2]);
                                bcCuenta = new bcCuentasBancarias();
                                ambitoCuenta = obtieneAmbitoCuenta(conexionContabilidad, totales.getCuenta(), bcCuenta);
                                totales.setEntidadAmbitoCuentaBancaria(bcCuenta.getEntidadAmbito());
                                totales.setUnidadCuentaBancaria(bcCuenta.getUnidad_ejecutora());
                                tipoCuenta = obtieneTipoCuenta(ambitoCuenta, bcCuenta);
                                totales.calculaRetencionesHistorico(); //Calcula todas las retenciones por unidad y ambito
                                totales.calculaAportaciones(); //Calcula todas las retenciones por unidad y ambito
                            }
                        }
                        cadena = "";
                        tipoForma = crs.getString("tipo");
                        if (tipoForma.equals("P")) {
                            if (regs != 0) { //No trajo registros el query de ctaxliq
                                cadena = totales.calculaTotalBrutoHisEjeCapitulo(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "C");
                                if (totales.getTotalBruto() == 0.0) { //No se genera poliza presupuestal                   
                                    enviaNotitificacion("Aviso del Sistema de Contabilidad  Armonizado se invoco el evento " + eventoContable + " para su revisi�n, no se genero poliza presupuestal.", "La cuenta por liquidar es: " + arregloParametros[3]);
                                    cadena = "";
                                } else {
                                    cadena = cadena + totales.calculaTotalBrutoHisEje(conexionContabilidad, Integer.parseInt(crs.getString("idforma")));  //Recorre histeje para calcular total bruto, total neto y cas 
                                    cadena = cadena + totales.recorreTotalRetenciones();  // Recorre todas las retenciones para la forma               
                                    cadena = cadena + totales.recorreTotalAportaciones();  // Recorre todas las retenciones para la forma               
                                }
                                if (totales.isTotalNetoCero() == false && crs.getString("forma").equals("ZX")) {
                                    cadena = "";
                                }
                            }

                        } else if (tipoForma.equals("F")) {
                            if (verificaForma(crs.getString("forma"), ambitoCuenta)) {
                                totales.setFactor(crs.getInt("factor"));
                                totales.setFactorNetoNeg(crs.getInt("FactorNetoNeg"));
                                totales.setEjercicio(arregloParametros[2]);
                                totales.setTotalNetoCero(false);
                                cadena = cadena + totales.generaFormaFinaciera(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), ambitoCuenta, tipoCuenta);  //Recorre histeje para calcular total bruto, total neto y cas 
                                cadena = cadena + totales.calculaTotalBrutoHisEjeCapitulo(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "C");
                                if (totales.isTotalNetoCero() == false) {
                                    cadena = "";
                                }
                            }
                        }
                        if (!cadena.isEmpty()) {
                            //System.out.println("Evento.procesarEvento.cadena "+cadena);
                            totales.asignaPropiedadesCtaXLiq();
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), totales.getUnidad().substring(1, 4), totales.getEntidad(), totales.getAmbito().substring(3, 4), arregloParametros[3], totales.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                            liPoliza = resultado.indexOf("***") + 3;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                            hashMap.clear();
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    case 171:  //Evento 171 de MIPF parametros(fecha de envio,estatus,ejercicio,documento,tipo_cxl)  ejemplo: 24/04/2010&4&2010&105011CXL00022&A                  
                        pagoPasi = new PagoPasivos();
                        if (Integer.parseInt(arregloParametros[0].substring(6)) > Integer.parseInt(arregloParametros[2])) {
                            pagoPasi.select_rf_tc_fecha_lim_pasivo(conexionContabilidad, arregloParametros[2]);
                            banPasivo = pagoPasi.select_F_COMPARA_FECHA(conexionContabilidad, arregloParametros[0], pagoPasi.getFecha_limite());
                            fechaOriginal = arregloParametros[0];
//                            String fec = "31/12/" + arregloParametros[2];
//                            arregloParametros[0] = fec;
                            ejercicio = arregloParametros[0].substring(6);
                            polizaPresupuestal = true;
                            polizaFinanciera = false;
                            polizaAreInicioEjer = true;
                        }
                        if (banPasivo == 1) {
                            if (totales == null) {
                                totales = new Totales();
                                totales.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[3] + "&" + arregloParametros[4] + "&" + origen + "&" + param5); //estatus, ejercicio , numero de documemnto, tipoCXL, origen, condicion de pago de pasivos 
                                regs = totales.asignaPropiedadesCtaXLiq();
                                if (regs == 0) {//No trajo registros el query de ctaxliq
                                    cadena = "";
                                } else {
                                    anticipo = totales.getAnticipo();
                                    capitulo = totales.getCapitulo();
                                    totales.setEjercicio(arregloParametros[2]);
                                    totales.setBanAre(banAre);
                                    bcCuenta = new bcCuentasBancarias();
                                    ambitoCuenta = obtieneAmbitoCuenta(conexionContabilidad, totales.getCuenta(), bcCuenta);
                                    tipoCuenta = obtieneTipoCuenta(ambitoCuenta, bcCuenta);
                                    totales.calculaRetencionesHistorico(); //Calcula todas las retenciones por unidad y ambito
                                    totales.calculaAportaciones(); //Calcula todas las retenciones por unidad y ambito                
                                }
                            }
                            tipoForma = crs.getString("tipo");
                            if ((tipoForma.equals("P") && polizaPresupuestal && (!(crs.getString("forma").equals("ZU") || crs.getString("forma").equals("ZV") || crs.getString("forma").equals("ZW")))) || (polizaAreInicioEjer && (crs.getString("forma").equals("ZU") || crs.getString("forma").equals("ZV") || crs.getString("forma").equals("ZW")))) {
                                if (regs != 0) { //No trajo registros el query de ctaxliq
                                    if (crs.getString("forma").equals("ZW")) {
                                        arregloParametros[0] = fechaOriginal;
                                        ejercicio = arregloParametros[0].substring(6);
                                    }
                                    if (anticipo.equals("0")) {  // NO ES FACTURA                     
                                        totales.setFactor(crs.getInt("factor"));
                                        totales.setFactorNetoNeg(crs.getInt("FactorNetoNeg"));
                                        cadena = totales.calculaTotalBrutoHisEjeCapitulo(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "C");
                                        totales.setTotalBrutoCero(false);
                                        cadena = cadena + totales.calculaTotalBrutoHisEje(conexionContabilidad, Integer.parseInt(crs.getString("idforma")));  //Recorre histeje para calcular total bruto, total neto y cas 
                                        cadena = cadena + totales.calculaTotalBrutoHisEjePartidaGen(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "P");
                                        cadena = cadena + totales.recorreTotalRetenciones();  // Recorre todas las retenciones para la forma               
                                        cadena = cadena + totales.recorreTotalAportaciones();  // Recorre todas las retenciones para la forma                                     
                   /*  if (totales.isTotalBrutoCero()){  
                                         enviaNotitificacion("Aviso del Sistema de Contabilidad Armonizado se invoco el evento "+eventoContable+" para su revisi�n, no se genero poliza presupuestal, por tener importe bruto diferente de 0. ","El aviso de reintegro es: "+arregloParametros[3]);                  
                                         cadena="";                         
                                         }*/ //Se quita esta condicion en sep 2013 a petici�n del usuario.
                                    }
                                }
                            } else if (tipoForma.equals("F") && ((polizaFinanciera && (!(crs.getString("forma").equals("YI") || crs.getString("forma").equals("YQ")))) || (polizaAreInicioEjer && (crs.getString("forma").equals("YI") || crs.getString("forma").equals("YQ"))))) {//Procesa formas financieras 
                                if (crs.getString("forma").equals("YQ")) {
                                    arregloParametros[0] = fechaOriginal;
                                    ejercicio = arregloParametros[0].substring(6);
                                }
                                totales.setFactor(crs.getInt("factor"));
                                totales.setFactorNetoNeg(crs.getInt("FactorNetoNeg"));
                                totales.setTotalNetoCero(false);
                                cadena = cadena + totales.generaFormaFinaciera(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), ambitoCuenta, tipoCuenta);  //Recorre histeje para calcular total bruto, total neto y cas 
                                cadena = cadena + totales.calculaTotalBrutoHisEjeCapitulo(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "C");
                                if (totales.isTotalNetoCero() == false) {
                                    cadena = "";
                                }
                            } else {
                                cadena = "";
                            }
                        } else {
                            cadena = "";
                        }
                        if (!cadena.isEmpty()) {
                            //System.out.println("Evento.procesarEvento.cadena "+cadena);
                            totales.asignaPropiedadesCtaXLiq();
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), totales.getUnidad().substring(1, 4), totales.getEntidad(), totales.getAmbito().substring(3, 4), arregloParametros[3], totales.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                            liPoliza = resultado.indexOf("***") + 3;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                            hashMap.clear();
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    case 172:  //Evento 172 de MIPF parametros(fecha de envio, estatus,ejercicio, documento, tipo_ar) ejemplo: 24/10/2004&4&2010&10912109011ARE00351&D                                 
                        pagoPasi = new PagoPasivos();
                        if (Integer.parseInt(arregloParametros[0].substring(6)) > Integer.parseInt(arregloParametros[2])) {
                            pagoPasi.select_rf_tc_fecha_lim_pasivo(conexionContabilidad, arregloParametros[2]);
                            banPasivo = pagoPasi.select_F_COMPARA_FECHA(conexionContabilidad, arregloParametros[0], pagoPasi.getFecha_limite());
                            fechaOriginal = arregloParametros[0];
//                            String fec = "31/12/" + arregloParametros[2];
//                            arregloParametros[0] = fec;
                            ejercicio = arregloParametros[0].substring(6);
                            polizaPresupuestal = true;
                            polizaFinanciera = false;
                            polizaAreInicioEjer = true;
                        }
                        if (banPasivo == 1) {
                            if (totales == null) {
                                totales = new Totales();
                                totales.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[3] + "&" + arregloParametros[4] + "&" + origen + "&" + param5); //estatus, ejercicio , numero de documento, tipoCXL, origen, condicion de pago de pasivos 
                                regs = totales.asignaPropiedadesCtaXLiq();
                                if (regs == 0) {//No trajo registros el query de ctaxliq
                                    cadena = "";
                                } else {
                                    anticipo = totales.getAnticipo();
                                    capitulo = totales.getCapitulo();
                                    totales.setEjercicio(arregloParametros[2]);
                                    totales.setBanAre(banAre);
                                    bcCuenta = new bcCuentasBancarias();
                                    ambitoCuenta = obtieneAmbitoCuenta(conexionContabilidad, totales.getCuenta(), bcCuenta);
                                    tipoCuenta = obtieneTipoCuenta(ambitoCuenta, bcCuenta);
                                    totales.calculaRetencionesHistorico(); //Calcula todas las retenciones por unidad y ambito
                                    totales.calculaAportaciones(); //Calcula todas las retenciones por unidad y ambito
                                }
                            }
                            tipoForma = crs.getString("tipo");
                            if ((tipoForma.equals("P") && polizaPresupuestal && (!(crs.getString("forma").equals("ZU") || crs.getString("forma").equals("ZV") || crs.getString("forma").equals("ZW")))) || (polizaAreInicioEjer && (crs.getString("forma").equals("ZU") || crs.getString("forma").equals("ZV") || crs.getString("forma").equals("ZW")))) {
                                if (regs != 0) { //No trajo registros el query de ctaxliq
                                    if (crs.getString("forma").equals("ZW")) {
                                        arregloParametros[0] = fechaOriginal;
                                        ejercicio = arregloParametros[0].substring(6);
                                    }
                                    totales.setFactor(crs.getInt("factor"));
                                    totales.setFactorNetoNeg(crs.getInt("FactorNetoNeg"));
                                    cadena = totales.calculaTotalBrutoHisEjeCapitulo(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "C");
                                    //cambio solicitado por salvador 09-08-2011
                                    if (totales.getBanProv() == 1) {
                                        enviaNotitificacion("Aviso del Sistema de Contabilidad Armonizado se invoco el evento " + eventoContable + " (ARE)con cap�tulo = 5000 y anticipo=0. Para su revisi�n, no se genero poliza presupuestal.", "El aviso de reintegro es: " + arregloParametros[3]);
                                        cadena = "";
                                    } else {
                                        cadena = cadena + totales.calculaTotalBrutoHisEjePartidaGen(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "P");
                                        cadena = cadena + totales.calculaTotalBrutoHisEje(conexionContabilidad, Integer.parseInt(crs.getString("idforma")));  //Recorre histeje para calcular total bruto, total neto y cas 
                                        cadena = cadena + totales.recorreTotalRetenciones();  // Recorre todas las retenciones para la forma               
                                        cadena = cadena + totales.recorreTotalAportaciones();  // Recorre todas las retenciones para la forma               
                                    }
                                }
                            } else if (tipoForma.equals("F") && ((polizaFinanciera && (!(crs.getString("forma").equals("YI") || crs.getString("forma").equals("YQ")))) || (polizaAreInicioEjer && (crs.getString("forma").equals("YI") || crs.getString("forma").equals("YQ"))))) {//Procesa formas financieras   
                                if (crs.getString("forma").equals("YQ")) {
                                    arregloParametros[0] = fechaOriginal;
                                    ejercicio = arregloParametros[0].substring(6);
                                }
                                totales.setFactor(crs.getInt("factor"));
                                totales.setFactorNetoNeg(crs.getInt("FactorNetoNeg"));
                                totales.setTotalNetoCero(false);
                                cadena = cadena + totales.generaFormaFinaciera(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), ambitoCuenta, tipoCuenta);  //Recorre histeje para calcular total bruto, total neto y cas 
                                cadena = cadena + totales.calculaTotalBrutoHisEjeCapitulo(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "C");
                                if (totales.isTotalNetoCero() == false) {
                                    cadena = "";
                                }
                            } else {
                                cadena = "";
                            }
                        } else {
                            cadena = "";
                        }
                        if (!cadena.isEmpty()) {
                            //System.out.println("Evento.procesarEvento.cadena "+cadena);
                            totales.asignaPropiedadesCtaXLiq();
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), totales.getUnidad().substring(1, 4), totales.getEntidad(), totales.getAmbito().substring(3, 4), arregloParametros[3], totales.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                            liPoliza = resultado.indexOf("***") + 3;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                            hashMap.clear();
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    case 173:  //Evento 173 de MIPF parametros(fecha de envio, estatus,ejercicio, documento, tipo_ar) ejemplo: 24/10/2004&4&2010&10912109011ARE00351&D                                
                        pagoPasi = new PagoPasivos();
                        if (Integer.parseInt(arregloParametros[0].substring(6)) > Integer.parseInt(arregloParametros[2])) {
                            pagoPasi.select_rf_tc_fecha_lim_pasivo(conexionContabilidad, arregloParametros[2]);
                            banPasivo = pagoPasi.select_F_COMPARA_FECHA(conexionContabilidad, arregloParametros[0], pagoPasi.getFecha_limite());
                            fechaOriginal = arregloParametros[0];
//                            String fec = "31/12/" + arregloParametros[2];
//                            arregloParametros[0] = fec;
                            ejercicio = arregloParametros[0].substring(6);
                            polizaPresupuestal = true;
                            polizaFinanciera = false;
                            polizaAreInicioEjer = true;
                        }
                        if (banPasivo == 1) {
                            if (totales == null) {
                                totales = new Totales();
                                totales.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[3] + "&" + arregloParametros[4] + "&" + origen + "&" + param5); //estatus, ejercicio , numero de documento, tipoCXL, origen, condicion de pago de pasivos 
                                regs = totales.asignaPropiedadesCtaXLiq();
                                if (regs == 0) {//No trajo registros el query de ctaxliq
                                    cadena = "";
                                } else {
                                    anticipo = totales.getAnticipo();
                                    capitulo = totales.getCapitulo();
                                    totales.setEjercicio(arregloParametros[2]);
                                    totales.setBanAre(banAre);
                                    bcCuenta = new bcCuentasBancarias();
                                    ambitoCuenta = obtieneAmbitoCuenta(conexionContabilidad, totales.getCuenta(), bcCuenta);
                                    tipoCuenta = obtieneTipoCuenta(ambitoCuenta, bcCuenta);
                                }
                            }
                            tipoForma = crs.getString("tipo");
                            if ((tipoForma.equals("P") && polizaPresupuestal && (!(crs.getString("forma").equals("ZU") || crs.getString("forma").equals("ZV") || crs.getString("forma").equals("ZW")))) || (polizaAreInicioEjer && (crs.getString("forma").equals("ZU") || crs.getString("forma").equals("ZV") || crs.getString("forma").equals("ZW")))) {
                                if (regs != 0) { //No trajo registros el query de ctaxliq
                                    if (crs.getString("forma").equals("ZW")) {
                                        arregloParametros[0] = fechaOriginal;
                                        ejercicio = arregloParametros[0].substring(6);
                                    }
                                    if (anticipo.equals("0")) {  // NO ES FACTURA
                                        totales.setFactor(crs.getInt("factor"));
                                        totales.setFactorNetoNeg(crs.getInt("FactorNetoNeg"));
                                        cadena = totales.calculaTotalBrutoHisEjeCapitulo(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "C");
                                        cadena = cadena + totales.calculaTotalBrutoHisEje(conexionContabilidad, Integer.parseInt(crs.getString("idforma")));  //Recorre histeje para calcular total bruto, total neto y cas 
                                    }
                                }
                            } else if (tipoForma.equals("F") && ((polizaFinanciera && (!(crs.getString("forma").equals("YI") || crs.getString("forma").equals("YQ")))) || (polizaAreInicioEjer && (crs.getString("forma").equals("YI") || crs.getString("forma").equals("YQ"))))) {//Procesa formas financieras                 
                                if (crs.getString("forma").equals("YQ")) {
                                    arregloParametros[0] = fechaOriginal;
                                    ejercicio = arregloParametros[0].substring(6);
                                }
                                totales.setFactor(crs.getInt("factor"));
                                totales.setFactorNetoNeg(crs.getInt("FactorNetoNeg"));
                                totales.setTotalNetoCero(false);
                                cadena = cadena + totales.generaFormaFinaciera(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), ambitoCuenta, tipoCuenta);  //Recorre histeje para calcular total bruto, total neto y cas 
                                if (totales.isTotalNetoCero() == false) {
                                    cadena = "";
                                }
                            } else {
                                cadena = "";
                            }
                        } else {
                            cadena = "";
                        }
                        if (!cadena.isEmpty()) {
                            //System.out.println("Evento.procesarEvento.cadena "+cadena);
                            totales.asignaPropiedadesCtaXLiq();
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), totales.getUnidad().substring(1, 4), totales.getEntidad(), totales.getAmbito().substring(3, 4), arregloParametros[3], totales.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                            liPoliza = resultado.indexOf("***") + 3;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                            hashMap.clear();
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    case 174:  //Evento 174 de MIPF parametros(fecha de envio, estatus,ejercicio, documento, tipo_ar) ejemplo: 24/10/2004&4&2010&10912109011ARE00351&D                               
                        pagoPasi = new PagoPasivos();
                        if (Integer.parseInt(arregloParametros[0].substring(6)) > Integer.parseInt(arregloParametros[2])) {
                            pagoPasi.select_rf_tc_fecha_lim_pasivo(conexionContabilidad, arregloParametros[2]);
                            banPasivo = pagoPasi.select_F_COMPARA_FECHA(conexionContabilidad, arregloParametros[0], pagoPasi.getFecha_limite());
                            fechaOriginal = arregloParametros[0];
//                            String fec = "31/12/" + arregloParametros[2];
//                            arregloParametros[0] = fec;
                            ejercicio = arregloParametros[0].substring(6);
                            polizaPresupuestal = true;
                            polizaFinanciera = false;
                            polizaAreInicioEjer = true;
                        }
                        if (banPasivo == 1) {
                            if (totales == null) {
                                totales = new Totales();
                                totales.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[3] + "&" + arregloParametros[4] + "&" + origen + "&" + param5); //estatus, ejercicio , numero de documento, tipoCXL, origen, condicion de pago de pasivos 
                                regs = totales.asignaPropiedadesCtaXLiq();
                                if (regs == 0) {//No trajo registros el query de ctaxliq
                                    cadena = "";
                                } else {
                                    anticipo = totales.getAnticipo();
                                    capitulo = totales.getCapitulo();
                                    totales.setEjercicio(arregloParametros[2]);
                                    totales.setBanAre(banAre);
                                    bcCuenta = new bcCuentasBancarias();
                                    ambitoCuenta = obtieneAmbitoCuenta(conexionContabilidad, totales.getCuenta(), bcCuenta);
                                    tipoCuenta = obtieneTipoCuenta(ambitoCuenta, bcCuenta);
                                    totales.calculaRetencionesHistorico(); //Calcula todas las retenciones por unidad y ambito
                                    totales.calculaAportaciones(); //Calcula todas las retenciones por unidad y ambito
                                }
                            }
                            tipoForma = crs.getString("tipo");
                            if ((tipoForma.equals("P") && polizaPresupuestal && (!(crs.getString("forma").equals("ZU") || crs.getString("forma").equals("ZV") || crs.getString("forma").equals("ZW")))) || (polizaAreInicioEjer && (crs.getString("forma").equals("ZU") || crs.getString("forma").equals("ZV") || crs.getString("forma").equals("ZW")))) {
                                if (regs != 0) { //No trajo registros el query de ctaxliq
                                    if (crs.getString("forma").equals("ZW")) {
                                        arregloParametros[0] = fechaOriginal;
                                        ejercicio = arregloParametros[0].substring(6);
                                    }
                                    if (anticipo.equals("0")) {  // NO ES FACTURA                       
                                        totales.setFactor(crs.getInt("factor"));
                                        totales.setFactorNetoNeg(crs.getInt("FactorNetoNeg"));
                                        cadena = totales.calculaTotalBrutoHisEjeCapitulo(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "C");
                                        cadena = cadena + totales.calculaTotalBrutoHisEje(conexionContabilidad, Integer.parseInt(crs.getString("idforma")));  //Recorre histeje para calcular total bruto, total neto y cas 
                                        cadena = cadena + totales.calculaTotalBrutoHisEjePartidaGen(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "P");
                                        cadena = cadena + totales.recorreTotalRetenciones();  // Recorre todas las retenciones para la forma               
                                        cadena = cadena + totales.recorreTotalAportaciones();  // Recorre todas las retenciones para la forma               
                                    }
                                }
                            } else if (tipoForma.equals("F") && ((polizaFinanciera && (!(crs.getString("forma").equals("YI") || crs.getString("forma").equals("YQ")))) || (polizaAreInicioEjer && (crs.getString("forma").equals("YI") || crs.getString("forma").equals("YQ"))))) {//Procesa formas financieras                     
                                if (crs.getString("forma").equals("YQ")) {
                                    arregloParametros[0] = fechaOriginal;
                                    ejercicio = arregloParametros[0].substring(6);
                                }
                                totales.setFactor(crs.getInt("factor"));
                                totales.setFactorNetoNeg(crs.getInt("FactorNetoNeg"));
                                totales.setTotalNetoCero(false);
                                cadena = cadena + totales.generaFormaFinaciera(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), ambitoCuenta, tipoCuenta);  //Recorre histeje para calcular total bruto, total neto y cas 
                                cadena = cadena + totales.calculaTotalBrutoHisEjeCapitulo(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "C");
                                if (totales.isTotalNetoCero() == false) {
                                    cadena = "";
                                }
                            } else {
                                cadena = "";
                            }
                        } else {
                            cadena = "";
                        }
                        if (!cadena.isEmpty()) {
                            //System.out.println("Evento.procesarEvento.cadena "+cadena);
                            totales.asignaPropiedadesCtaXLiq();
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), totales.getUnidad().substring(1, 4), totales.getEntidad(), totales.getAmbito().substring(3, 4), arregloParametros[3], totales.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                            liPoliza = resultado.indexOf("***") + 3;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                            hashMap.clear();
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    case 176:  //Evento 176 de MIPF parametros(fecha de envio, estatus, ejercicio, documento, tipo_ar)  ejemplo: 24/04/2010&4&2010&10912109011ARE00351&D                  
                        pagoPasi = new PagoPasivos();
                        if (Integer.parseInt(arregloParametros[0].substring(6)) > Integer.parseInt(arregloParametros[2])) {
                            pagoPasi.select_rf_tc_fecha_lim_pasivo(conexionContabilidad, arregloParametros[2]);
                            banPasivo = pagoPasi.select_F_COMPARA_FECHA(conexionContabilidad, arregloParametros[0], pagoPasi.getFecha_limite());
                            fechaOriginal = arregloParametros[0];
//                            String fec = "31/12/" + arregloParametros[2];
//                            arregloParametros[0] = fec;
                            ejercicio = arregloParametros[0].substring(6);
                            polizaPresupuestal = true;
                            polizaFinanciera = false;
                            polizaAreInicioEjer = true;
                        }
                        if (banPasivo == 1) {
                            if (totales == null) {
                                totales = new Totales();
                                totales.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[3] + "&" + arregloParametros[4] + "&" + origen + "&" + param5); //estatus, ejercicio , numero de documento, tipoCXL, origen, condicion de pago de pasivos 
                                regs = totales.asignaPropiedadesCtaXLiq();
                                if (regs == 0) {//No trajo registros el query de ctaxliq
                                    cadena = "";
                                } else {
                                    anticipo = totales.getAnticipo();
                                    capitulo = totales.getCapitulo();
                                    totales.setEjercicio(arregloParametros[2]);
                                    totales.setBanAre(banAre);
                                    bcCuenta = new bcCuentasBancarias();
                                    ambitoCuenta = obtieneAmbitoCuenta(conexionContabilidad, totales.getCuenta(), bcCuenta);
                                    tipoCuenta = obtieneTipoCuenta(ambitoCuenta, bcCuenta);
                                    totales.calculaRetencionesHistorico(); //Calcula todas las retenciones por unidad y ambito
                                    totales.calculaAportaciones(); //Calcula todas las retenciones por unidad y ambito
                                }
                            }
                            tipoForma = crs.getString("tipo");
                            if ((tipoForma.equals("P") && polizaPresupuestal && (!(crs.getString("forma").equals("ZU") || crs.getString("forma").equals("ZV") || crs.getString("forma").equals("ZW")))) || (polizaAreInicioEjer && (crs.getString("forma").equals("ZU") || crs.getString("forma").equals("ZV") || crs.getString("forma").equals("ZW")))) {
                                if (regs != 0) { //No trajo registros el query de ctaxliq
                                    if (crs.getString("forma").equals("ZW")) {
                                        arregloParametros[0] = fechaOriginal;
                                        ejercicio = arregloParametros[0].substring(6);
                                    }
                                    totales.setFactor(crs.getInt("factor"));
                                    totales.setFactorNetoNeg(crs.getInt("FactorNetoNeg"));
                                    cadena = totales.calculaTotalBrutoHisEjeCapitulo(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "C");
                                    //cambio solicitado por salvador 09-08-2011
                                    if (totales.getBanProv() == 1) {
                                        enviaNotitificacion("Aviso del Sistema de Contabilidad  Armonizado se invoco el evento " + eventoContable + " (ARE)con cap�tulo = 5000 y anticipo=0. Para su revisi�n, no se genero poliza presupuestal.", "El aviso de reintegro es: " + arregloParametros[3]);
                                        cadena = "";
                                    } else {
                                        cadena = cadena + totales.calculaTotalBrutoHisEjePartidaGen(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "P");
                                        cadena = cadena + totales.calculaTotalBrutoHisEje(conexionContabilidad, Integer.parseInt(crs.getString("idforma")));  //Recorre histeje para calcular total bruto, total neto y cas 
                                        cadena = cadena + totales.recorreTotalRetenciones();  // Recorre todas las retenciones para la forma               
                                        cadena = cadena + totales.recorreTotalAportaciones();  // Recorre todas las retenciones para la forma               
                                    }
                                }
                            } else if (tipoForma.equals("F") && ((polizaFinanciera && (!(crs.getString("forma").equals("YI") || crs.getString("forma").equals("YQ")))) || (polizaAreInicioEjer && (crs.getString("forma").equals("YI") || crs.getString("forma").equals("YQ"))))) {//Procesa formas financieras 
                                if (crs.getString("forma").equals("YQ")) {
                                    arregloParametros[0] = fechaOriginal;
                                    ejercicio = arregloParametros[0].substring(6);
                                }
                                totales.setFactor(crs.getInt("factor"));
                                totales.setFactorNetoNeg(crs.getInt("FactorNetoNeg"));
                                totales.setTotalNetoCero(false);
                                cadena = cadena + totales.generaFormaFinaciera(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), ambitoCuenta, tipoCuenta);  //Recorre histeje para calcular total bruto, total neto y cas 
                                cadena = cadena + totales.calculaTotalBrutoHisEjeCapitulo(conexionContabilidad, Integer.parseInt(crs.getString("idforma")), "C");
                                if (totales.isTotalNetoCero() == false) {
                                    cadena = "";
                                }
                            } else {
                                cadena = "";
                            }
                        } else {
                            cadena = "";
                        }
                        if (!cadena.isEmpty()) {
                            //System.out.println("Evento.procesarEvento.cadena "+cadena);
                            totales.asignaPropiedadesCtaXLiq();
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), totales.getUnidad().substring(1, 4), totales.getEntidad(), totales.getAmbito().substring(3, 4), arregloParametros[3], totales.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                            liPoliza = resultado.indexOf("***") + 3;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                            hashMap.clear();
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    //******* Tesoreria        
                    case 250:  //Evento Tesoreria para cuentas BANAMEX parametros(fecha inicio, fechaFin)    
                        System.out.println("Evento.procesarEvento.movimiento = " + crs.getString("movimiento") + "**");
                        siafmTesoreria = new SiafmTesoreria();
                        if (crs.getString("movimiento").equals("11")) {
                            cadena = siafmTesoreria.registrosContablesBmx29076(crs.getString("forma"), arregloParametros[0], arregloParametros[1], crs.getString("movimiento"));
                        } else {
                            cadena = siafmTesoreria.registrosContablesBanamex(crs.getString("forma"), arregloParametros[0], arregloParametros[1], crs.getString("movimiento"));
                        }
                        System.out.println("Evento.procesarEvento.cadena = " + cadena + "**");
                        //cadena="";
                        if (!cadena.isEmpty()) {
                            //System.out.println("Evento.procesarEvento.cadena "+cadena);
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), "001", "9", "1", "BMX. ".concat(arregloParametros[0]), crs.getString("referencia"), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF_NO_ACUMULADO);
                            liPoliza = resultado.indexOf("***") + 5;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 5) + "|";
                            hashMap.clear();
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    case 251:  //Evento Tesoreria para cuentas HSBC parametros(fecha inicio, fechaFin)     
                        siafmTesoreria = new SiafmTesoreria();
                        cadena = siafmTesoreria.registrosContablesHSBC(crs.getString("forma"), arregloParametros[0], arregloParametros[1], crs.getString("movimiento"));
                        if (!cadena.isEmpty()) {
                            //System.out.println("Evento.procesarEvento.cadena "+cadena);
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), "001", "9", "1", "HSBC".concat(crs.getString("forma")).concat(arregloParametros[0]), crs.getString("referencia"), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF_NO_ACUMULADO);
                            liPoliza = resultado.indexOf("***") + 5;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 5) + "|";
                            hashMap.clear();
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    case 280:  //Evento Inversiones BANAMEX parametros(fecha inicio, fechaFin)    
                        siafmInversiones = new SiafmInversiones();
                        cadena = siafmInversiones.registrosContablesBMX(crs.getString("forma"), arregloParametros[0], arregloParametros[1], crs.getString("movimiento"));
                        if (!cadena.isEmpty()) {
                            //System.out.println("Evento.procesarEvento.cadena "+cadena);
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), "001", "9", "1", "INVERSIONES BMX. ".concat(arregloParametros[0]), crs.getString("referencia"), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF_NO_ACUMULADO);
                            liPoliza = resultado.indexOf("***") + 5;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 5) + "|";
                            hashMap.clear();
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    case 281:  //Evento Inversiones HSBC parametros(fecha inicio, fechaFin)    
                        siafmInversiones = new SiafmInversiones();
                        cadena = siafmInversiones.registrosContablesHSBC(crs.getString("forma"), arregloParametros[0], arregloParametros[1], crs.getString("movimiento"));
                        if (!cadena.isEmpty()) {
                            //System.out.println("Evento.procesarEvento.cadena "+cadena);
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), "001", "9", "1", "INVERSIONES HSBC. ".concat(arregloParametros[0]), crs.getString("referencia"), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF_NO_ACUMULADO);
                            liPoliza = resultado.indexOf("***") + 5;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 5) + "|";
                            hashMap.clear();
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    case 298: //Evento 151 de MIPF parametros(fecha de envio,estatus,ejercicio,documento,tipo_cxl)  ejemplo: 24/04/2010&4&2010&105011CXL00022&A                  
                    {
                        if (Integer.parseInt(arregloParametros[0].substring(6)) > Integer.parseInt(arregloParametros[2])) {
                            polizaPresupuestal = false;
                            param5 = "--";
                        }
                        TotalesSenado totalesS = new TotalesSenado();
                        totalesS.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[3] + "&" + arregloParametros[4] + "&" + origen + "&" + param5); //estatus, ejercicio , numero de documemnto, tipoCXL, origen, condicion de pago de pasivo 
                        regs = totalesS.asignaPropiedadesCtaXLiq();
                        cadena = "";
                        if (regs != 0 && arregloParametros[1].equals("13")) {//No trajo registros el query de ctaxliq
                            cadena = cadena + totalesS.calculaCxpCp(conexionContabilidad);
                            cadena = cadena + totalesS.calculaActivos(conexionContabilidad);
                        }
                        if (!cadena.isEmpty()) {
                            try {
                                totalesS.asignaPropiedadesCtaXLiq();
                                preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), totalesS.getUnidad().substring(1, 4), totalesS.getEntidad(), totalesS.getAmbito().substring(3, 4), arregloParametros[3], totalesS.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                                resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                                liPoliza = resultado.indexOf("***") + 3;
                                lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                                hashMap.clear();
                            } catch (Exception e) {
                            }
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    }
                    case 299: //Evento 109 de MIPF parametros(fecha de envio,estatus,ejercicio,documento,tipo_cxl)  ejemplo: 24/04/2010&4&2010&105011CXL00022&A                  
                    {
                        if (Integer.parseInt(arregloParametros[0].substring(6)) > Integer.parseInt(arregloParametros[2])) {
//                            String fec = "31/12/" + arregloParametros[2];
//                            arregloParametros[0] = fec;
                            ejercicio = arregloParametros[0].substring(6);
                        }
                        TotalesSenado totalesS = new TotalesSenado();
                        String cxp = "";
                        posicionError++;
                        totalesS.leeModeloMipf(conexionContabilidad, conexionSistema, Integer.toString(eventoContable), arregloParametros[1] + "&" + arregloParametros[2] + "&" + arregloParametros[3] + "&" + arregloParametros[4] + "&" + origen + "&" + param5); //estatus, ejercicio , numero de documento, tipoCXL, origen, condicion de pago de pasivos 
                        posicionError++;
                        if (totalesS.asignaPropiedadesCtaXLiq() == 0) {//No trajo registros el query de ctaxliq
                            cadena = "";
                        } else {
                        posicionError++;
                            anticipo = totalesS.getAnticipo();
                        posicionError++;
//                            capitulo = totales.getCapitulo();
                            totalesS.setFactor(crs.getInt("factor"));
                        posicionError++;
//                            totales.setEjercicio(arregloParametros[2]);
                            System.out.println("Evento.procesarEvento.299.anticipo " + anticipo);
                        posicionError++;
                            cadena = totalesS.calculaGasto(conexionContabilidad, crs.getString("forma"));
                            cxp = totalesS.calculaCxpCp(conexionContabilidad);
                            cadena = cadena + cxp;
                            cadena = cadena + totalesS.calculaRetenciones(conexionContabilidad, crs.getString("forma"), eventoContable);
                            cadena = cadena + totalesS.calculaPresupDevengado(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaPresupComprometido(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaPresupComprometido2(conexionContabilidad);
                            cadena = cadena + totalesS.calculaPresupPorEjercer(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaPresupPagado(conexionContabilidad, crs.getString("forma"));
                            cadena = cadena + totalesS.calculaPresupEjercido(conexionContabilidad, crs.getString("forma"));
                        }
                        //System.out.println("*******************\n" + crs.getString("forma") + "\n***************");
                        if (crs.getString("forma").equals("CC") && !cxp.isEmpty()) {
                            cadena = "";
                        } else if (crs.getString("forma").equals("CT") && cxp.isEmpty()) {
                            cadena = "";
                        }
                        if (!cadena.isEmpty()) {
                            //System.out.println("Evento.procesarEvento.cadena "+cadena);
                            totalesS.asignaPropiedadesCtaXLiq();
                            System.out.println("Concepto: "+totalesS.getConcepto());
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), totalesS.getUnidad().substring(1, 4), totalesS.getEntidad(), totalesS.getAmbito().substring(3, 4), arregloParametros[3], totalesS.getConcepto(), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF);
                            liPoliza = resultado.indexOf("***") + 3;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                            hashMap.clear();
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    }
                    case 300:  //Evento 300 de Polizas de eliminacion  
                        siafmEliminacion = new SiafmEliminacion();
                        procesosCierre = new bcProcesosCierre();
                        cadena = "";
                        switch (Integer.parseInt(crs.getString("idforma"))) { //arregloParametros[1],arregloParametros[2],arregloParametros[3],
                            case 500:
                                if (arregloParametros[6].equals("1")) {
                                    cadena = siafmEliminacion.formaAmpliacionReduccion(conexionSistema, arregloParametros[1], arregloParametros[2], arregloParametros[3], arregloParametros[4], arregloParametros[5]);
                                }
                                break;
                            case 501:
                                if (arregloParametros[7].equals("1")) {
                                    cadena = siafmEliminacion.formaEliminacionTipo2(conexionSistema, arregloParametros[1], arregloParametros[2], arregloParametros[3], arregloParametros[4]);
                                }
                                break;
                            case 502:
                                if (arregloParametros[8].equals("1")) {
                                    cadena = siafmEliminacion.formaEliminacionActivoFijo(conexionSistema, arregloParametros[1], arregloParametros[2], arregloParametros[3], arregloParametros[4]);
                                }
                                break;
                            case 503:
                                if (arregloParametros[9].equals("1")) {
                                    cadena = siafmEliminacion.formaEliminacionPagosPresupuestarios(conexionSistema, arregloParametros[1], arregloParametros[2], arregloParametros[3], arregloParametros[4]);
                                }
                                break;
                            case 504:
                                if (arregloParametros[10].equals("1")) {
                                    cadena = siafmEliminacion.formaCancMinistracionesAUE(conexionSistema, arregloParametros[1], arregloParametros[2], arregloParametros[3], arregloParametros[4], arregloParametros[5]);
                                }
                                break;
                            case 505:
                                if (arregloParametros[11].equals("1")) {
                                    cadena = siafmEliminacion.elimacionRemesas(conexionSistema, arregloParametros[1], arregloParametros[2], arregloParametros[3]);
                                }
                                break;
                            case 506:
                                if (arregloParametros[12].equals("1")) {
                                    cadena = siafmEliminacion.entradasSalidasActivos(conexionSistema, arregloParametros[1], arregloParametros[2], arregloParametros[3]);
                                }
                                break;
                            case 507:
                                if (arregloParametros[13].equals("1")) {
                                    cadena = siafmEliminacion.elimacionEntradasAlmacen(conexionSistema, arregloParametros[1], arregloParametros[2], arregloParametros[3], arregloParametros[4]);
                                }
                                break;
                        }
                        if (!cadena.isEmpty()) {
                            //System.out.println("Evento.procesarEvento.cadena "+cadena);
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), crs.getString("unidadejecutora"), crs.getString("entidad"), crs.getString("ambito"), crs.getString("referencia"), crs.getString("concepto"), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.CONPROVE);
                            liPoliza = resultado.indexOf("***") + 3;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                            hashMap.clear();

                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    default:
                        return "0, No existe el evento contable: " + eventoContable + " para ser procesado.";
                }//FIN WHILE   
            }//TRY
            if (resultado.substring(0, 1).equals("1")) {
                cuentaContable = new bcCuentaContable();
                bitacoraLocal.update_BITACORALOCAL_estatus(conexionSistema, eventoContable, eventoProceso, "1", lsPolizas);
                resultado = "1,El evento contable " + eventoContable + " con n�mero de proceso " + eventoProceso + ", fue procesado correctamente y gener� las siguientes polizas [" + lsPolizas + "]";
                if (eventoContable == 300) {
                    //agregar actualizaci�n de tabla de cierres
                    procesosCierre.update_rf_tr_procesos_cierre_estatus(conexionContabilidad, arregloParametros[2], arregloParametros[3], "5", "1");
                    //trasladar saldos con eliminacion
                    cuentaContable.update_rf_tr_cuentas_contables_acumulados_eli(conexionContabilidad, ejercicio, "1", arregloParametros[0].substring(3, 5));
                }
                conexionContabilidad.commit();
                conexionSistema.commit();
                if (eventoContable == 300) {
                    StringBuilder cadenaHtml = new StringBuilder();
                    ComparacionCuentas comparacion = new ComparacionCuentas();
                    cadenaHtml.append("<p><b>El proceso de Aplicaci�n de P�lizas de Eliminaci�n ha concluido satisfactoriamente.</b></p>");
                    cadenaHtml.append("<br>");
                    //Como parametro se le manda la fecha en formato ejercicio, mes, dia (se manda siempre el primero ya que �ste no interesa mucho)
                    cadenaHtml.append(comparacion.codigoHtml(conexionContabilidad, arregloParametros[2] + arregloParametros[3] + "01"));
                    enviaNotitificacionEliminacion("Estatus del Proceso de Eliminaci�n del mes " + arregloParametros[3] + " para el ejercicio " + arregloParametros[2], cadenaHtml.toString());
                }
            } else {
                throw new Exception(resultado);
            }
            System.out.println(resultado);
            return resultado;
        } catch (Exception e) {
            conexionContabilidad.rollback();
            conexionSistema.rollback();
            if (eventoContable == 300) {
                enviaNotitificacionEliminacion("Error - Estatus del Proceso de Eliminaci�n del mes " + arregloParametros[3] + " para el ejercicio" + arregloParametros[2], "Ha ocurrido un error en el proceso de Aplicaci�n de P�lizas de Eliminaci�n: " + e.getMessage() + ".  Favor de comunicarse con el administrador del sistema.");
            }
            System.out.println(posicionError + " Ocurrio un error en Evento.procesarEvento " + e.getMessage());
            throw e;
        } finally {
            if (crs != null) {
                crs.close();
                crs = null;
            }
            if (conexionSistema != null) {
                conexionSistema.close();
            }
            if (conexionContabilidad != null) {
                conexionContabilidad.close();
            }
        }
    }
}
