package sia.rf.contabilidad.registroContableEvento;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import sia.db.dao.DaoFactory;
import sia.libs.correo.Envio;
import sia.rf.contabilidad.registroContable.formas.Areas;
import sia.rf.contabilidad.registroContableNuevo.bcCuentaContable;
import sia.rf.contabilidad.registroContableNuevo.bcProcesosCierre;
import sia.rf.contabilidad.sistemas.cierreAnual.SiafmPolizasTraspaso;
import sia.ws.publicar.contabilidad.Registro;
import sun.jdbc.rowset.CachedRowSet;

/**
 *
 * @author luz.lopez
 */
public class EventoTraspaso {

    private EventoContable eventoContable = null;
    private FormaContable formaContable = null;
    private Modulo modulo = null;
    private Sistema sistema = null;
    private Registro registro = null;
    private BitacoraLocal bitacoraLocal = null;
    private CachedRowSet crs = null;
    private bcProcesosCierre pbProcesos = null;
    private int banAre = 0;
    private String origen = "";
    private Boolean Positivo6100 = false;
    private String Nombreforma = "";
    String tipoCierre = "";

    public EventoTraspaso() {
        eventoContable = new EventoContable();
        formaContable = new FormaContable();
        modulo = new Modulo();
        sistema = new Sistema();
        registro = new Registro();
        bitacoraLocal = new BitacoraLocal();
        pbProcesos = new bcProcesosCierre();
    }

    private void enviaNotitificacionCuentaPub(String asunto, String mensaje) {
        StringBuilder sb = new StringBuilder();
        String lsDestinatarios = "siacontabilidad@senado.gob.mx, minerva.vidales@senado.gob.mx, erika.martinez@senado.gob.mx,vicente.delcarmen@senado.gob.mx";
        sb.append("<html><title><head></head></title><body>");
        sb.append("<br><strong>");
        sb.append(mensaje);
        sb.append("</strong><br>");
        sb.append("<br>");
        Envio.asuntoMensaje("siacontabilidad@senado.gob.mx", lsDestinatarios, null, asunto, sb.toString(), null, true);

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
            origen = eventoContable.getOrigen();
        } catch (Exception e) {
            System.out.println("Ocurrio un errror al accesar el metodo obtenerNombreDatasource " + e.getMessage());
            throw e;
        } finally {
            if (conexion != null) {
                conexion.close();
                conexion = null;
            }
        }
        return resultado;
    }

    private Connection obtenerConexionModulo(String nombreDS) throws SQLException, Exception {
        Connection conexion = null;
        try {
            conexion = DaoFactory.getConnection(nombreDS);
        } catch (Exception e) {
            System.out.println("Ocurrio un errror al accesar el metodo obtenerConexionModulo " + e.getMessage());
            throw e;
        }
        return conexion;
    }

    private void preparaRegistro(String cadena, String claveSeguridadWS, String fechaEnvio, String forma, String unidadEjecutora, String entidad, String ambito, String referencia, String concepto, String catalogoCuenta, String idEvento, String ejercicio) throws SQLException, Exception {
        registro.setForma(forma);
        registro.setUnidadeje(unidadEjecutora);
        registro.setEntidad(entidad);
        registro.setAmbito(ambito);
        registro.setReferencia(referencia);
        registro.setConcepto(concepto);
        registro.setVariables(cadena);
        registro.setNoempleado(2222);
        registro.setClave(claveSeguridadWS);
        registro.setFecenvio(fechaEnvio);
        registro.setCatalogocuenta(catalogoCuenta);
        registro.setIdEvento(idEvento);
        registro.setEjercicio(ejercicio);
    }

    public String procesarEvento(int eventoContable, int eventoProceso, String parametros, String claveSeguridadWS) throws SQLException, Exception {
        String nombreDS = null;
        Connection conexionSistema = null;
        Connection conexionContabilidad = null;
        String cadena = null;
        String resultado = null;
        String ref = null;
        String[] arregloParametros = parametros.split("&");
        HashMap hashMap = new HashMap();


        bcCuentaContable cuentaContable = null;

        SiafmPolizasTraspaso siafmPolizasTraspaso = null;
        int liPoliza = 0;
        String lsPolizas = "|";

        String ejercicio = null;
        try {
            nombreDS = obtenerNombreDatasource(Integer.toString(eventoContable));
            conexionSistema = obtenerConexionModulo(nombreDS);
            conexionSistema.setAutoCommit(false);
            conexionContabilidad = DaoFactory.getContabilidad();
            conexionContabilidad.setAutoCommit(false);
            ejercicio = arregloParametros[0].substring(6);
            int forma = 0;
            while (crs.next()) {
                System.out.println("Procesando Evento: " + eventoContable);
                Nombreforma = crs.getString("forma");
                switch (eventoContable) {
                    case 301:  //Evento 301 de Polizas Traspaso Unidades
                        tipoCierre = "Evento 301 - Cierre de Unidades";
                        siafmPolizasTraspaso = new SiafmPolizasTraspaso();
                        cadena = "";
                        forma = Integer.parseInt(crs.getString("idforma"));
                        switch (Integer.parseInt(crs.getString("idforma"))) {
                            case 650:
                                cadena = siafmPolizasTraspaso.formaOATraspaso8140_a_8120(conexionContabilidad, "1", arregloParametros[1], "_pub");
                                break;
                            case 651:
                                cadena = siafmPolizasTraspaso.formaOBTraspaso8130_a_8120(conexionContabilidad, "1", arregloParametros[1], "_pub");
                                break;
                            case 652:
                                cadena = siafmPolizasTraspaso.formaOCTraspaso8120_a_8130(conexionContabilidad, "1", arregloParametros[1], "_pub");
                                break;
                            case 653:
                                cadena = siafmPolizasTraspaso.formaODTraspaso8120_a_8110(conexionContabilidad, "1", arregloParametros[1], "_pub");
                                break;
                            case 654:
                                cadena = siafmPolizasTraspaso.formaOFTraspaso8240_a_8220(conexionContabilidad, "1", arregloParametros[1], "_pub");
                                break;
                            case 655:
                                cadena = siafmPolizasTraspaso.formaOGTraspasoCargos8230_a_8220(conexionContabilidad, "1", arregloParametros[1], "_pub");
                                break;
                            case 656:
                                cadena = siafmPolizasTraspaso.formaOHAbonosTraspasos8230_a_8220(conexionContabilidad, "1", arregloParametros[1], "_pub");
                                break;
                            case 657:
                                cadena = siafmPolizasTraspaso.formaOETraspaso8220_a_8210(conexionContabilidad, "1", arregloParametros[1], "_pub");
                                break;
                            case 658:
                                cadena = siafmPolizasTraspaso.formaOITraspasos8250_8260_a_9300(conexionContabilidad, "1", arregloParametros[1], "_pub");
                                break;
                            case 659:
                                cadena = siafmPolizasTraspaso.formaOJTraspasos8150_8270_9300_a_9100(conexionContabilidad, "1", arregloParametros[1], "_pub");
                                break;
                            case 660:
                                cadena = siafmPolizasTraspaso.formaOKTraspasos8110_8210_a_9100(conexionContabilidad, "1", arregloParametros[1], "_pub");
                                break;
                            case 661:
                                cadena = siafmPolizasTraspaso.formaOLTraspasos4221_4399_4319_a_6100(conexionContabilidad, "1", arregloParametros[1], "_pub");
                                break;
                            case 662:
                                cadena = siafmPolizasTraspaso.formaOMTraspaso4322_a_6100(conexionContabilidad, "1", arregloParametros[1], "_pub");
                                break;
                            case 663:
                                cadena = siafmPolizasTraspaso.formaONTraspasos4321_4326_a_6100(conexionContabilidad, "1", arregloParametros[1], "_pub");
                                break;
                            case 664:
                                cadena = siafmPolizasTraspaso.formaOOTraspasos51_55_a_6100(conexionContabilidad, "1", arregloParametros[1], "_pub");
                                break;
                            case 665:
                                cadena = siafmPolizasTraspaso.formaOPTraspaso5532_5531_5536_5592_a_6100(conexionContabilidad, "1", arregloParametros[1], "_pub");
                                break;
                            case 666:
                                Positivo6100 = (siafmPolizasTraspaso.VerificaSaldo6100(conexionContabilidad, "1", arregloParametros[1], "_pub"));
                                if (Positivo6100) {
                                    cadena = siafmPolizasTraspaso.formaOQTraspaso6100_a_6200(conexionContabilidad, "1", arregloParametros[1], "_pub");
                                } else {
                                    cadena = "";
                                }
                                break;
                            case 667:
                                if (Positivo6100) {
                                    cadena = siafmPolizasTraspaso.formaORTraspaso6100_a_6200(conexionContabilidad, "1", arregloParametros[1], "_pub");
                                } else {
                                    cadena = "";
                                }
                                break;
                            case 668:
                                if (!Positivo6100) {
                                    cadena = siafmPolizasTraspaso.formaOSTraspaso6100_a_6300(conexionContabilidad, "1", arregloParametros[1], "_pub");
                                } else {
                                    cadena = "";
                                }
                                break;
                            case 669:
                                if (!Positivo6100) {
                                    cadena = siafmPolizasTraspaso.formaOTTraspaso6100_a_6300(conexionContabilidad, "1", arregloParametros[1], "_pub");
                                } else {
                                    cadena = "";
                                }
                                break;
                            case 670:
                                if (Positivo6100) {
                                    cadena = siafmPolizasTraspaso.formaOUTraspaso6200_a_3210(conexionContabilidad, "1", arregloParametros[1], "_pub");
                                } else {
                                    cadena = "";
                                }
                                break;
                            case 671:
                                if (Positivo6100) {
                                    cadena = siafmPolizasTraspaso.formaOVTraspaso6200_a_3210(conexionContabilidad, "1", arregloParametros[1], "_pub");
                                } else {
                                    cadena = "";
                                }
                                break;

                            case 672:
                                if (!Positivo6100) {
                                    cadena = siafmPolizasTraspaso.formaOWTraspaso6300_a_3210(conexionContabilidad, "1", arregloParametros[1], "_pub");
                                } else {
                                    cadena = "";
                                }
                                break;
                            case 673:
                                if (!Positivo6100) {
                                    cadena = siafmPolizasTraspaso.formaOXTraspaso6300_a_3210(conexionContabilidad, "1", arregloParametros[1], "_pub");
                                } else {
                                    cadena = "";
                                }
                                break;
                        }
                        if (!cadena.isEmpty()) {
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), crs.getString("unidadejecutora"), crs.getString("entidad"), crs.getString("ambito"), siafmPolizasTraspaso.getReferenciaGral(), crs.getString("concepto"), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            if (forma == 659 || forma == 660) {
                                resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF_NO_ACUMULADO);
                            } else {
                                resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.CONPROVE);
                            }
                            liPoliza = resultado.indexOf("***") + 3;
                            lsPolizas = lsPolizas + resultado.substring(liPoliza, liPoliza + 7) + "|";
                            hashMap.clear();
                        } else {
                            lsPolizas = lsPolizas + "SIN POLIZA|";
                            resultado = "1,Sin poliza por hashMap vacio";
                        }
                        break;
                    case 302:  //Evento 302 de Polizas Traspaso CENTRAL
                        tipoCierre = "Evento 302 - Cierre de Central";
                        siafmPolizasTraspaso = new SiafmPolizasTraspaso();
                        cadena = "";
                        forma = Integer.parseInt(crs.getString("idforma"));
                        switch (Integer.parseInt(crs.getString("idforma"))) {
                            case 680:
                                cadena = siafmPolizasTraspaso.formaOATraspaso8140_a_8120(conexionContabilidad, "1", arregloParametros[1], "_eli_pub");
                                break;
                            case 681:
                                cadena = siafmPolizasTraspaso.formaOBTraspaso8130_a_8120(conexionContabilidad, "1", arregloParametros[1], "_eli_pub");
                                break;
                            case 682:
                                cadena = siafmPolizasTraspaso.formaOCTraspaso8120_a_8130(conexionContabilidad, "1", arregloParametros[1], "_eli_pub");
                                break;
                            case 683:
                                cadena = siafmPolizasTraspaso.formaODTraspaso8120_a_8110(conexionContabilidad, "1", arregloParametros[1], "_eli_pub");
                                break;
                            case 684:
                                cadena = siafmPolizasTraspaso.formaOFTraspaso8240_a_8220(conexionContabilidad, "1", arregloParametros[1], "_eli_pub");
                                break;
                            case 685:
                                cadena = siafmPolizasTraspaso.formaOGTraspasoCargos8230_a_8220(conexionContabilidad, "1", arregloParametros[1], "_eli_pub");
                                break;
                            case 686:
                                cadena = siafmPolizasTraspaso.formaOHAbonosTraspasos8230_a_8220(conexionContabilidad, "1", arregloParametros[1], "_eli_pub");
                                break;
                            case 687:
                                cadena = siafmPolizasTraspaso.formaOETraspaso8220_a_8210(conexionContabilidad, "1", arregloParametros[1], "_eli_pub");
                                break;
                            case 688:
                                cadena = siafmPolizasTraspaso.formaOITraspasos8250_8260_a_9300(conexionContabilidad, "1", arregloParametros[1], "_eli_pub");
                                break;
                            case 689:
                                cadena = siafmPolizasTraspaso.formaOJTraspasos8150_8270_9300_a_9100(conexionContabilidad, "1", arregloParametros[1], "_eli_pub");
                                break;
                            case 690:
                                cadena = siafmPolizasTraspaso.formaOKTraspasos8110_8210_a_9100(conexionContabilidad, "1", arregloParametros[1], "_eli_pub");
                                break;
                            case 691:
                                cadena = siafmPolizasTraspaso.formaOLTraspasos4221_4399_4319_a_6100(conexionContabilidad, "1", arregloParametros[1], "_eli_pub");
                                break;
                            case 692:
                                cadena = siafmPolizasTraspaso.formaOMTraspaso4322_a_6100(conexionContabilidad, "1", arregloParametros[1], "_eli_pub");
                                break;
                            case 693:
                                cadena = siafmPolizasTraspaso.formaONTraspasos4321_4326_a_6100(conexionContabilidad, "1", arregloParametros[1], "_eli_pub");
                                break;
                            case 694:
                                cadena = siafmPolizasTraspaso.formaOOTraspasos51_55_a_6100(conexionContabilidad, "1", arregloParametros[1], "_eli_pub");
                                break;
                            case 695:
                                cadena = siafmPolizasTraspaso.formaOPTraspaso5532_5531_5536_5592_a_6100(conexionContabilidad, "1", arregloParametros[1], "_eli_pub");
                                break;
                            case 696:
                                Positivo6100 = (siafmPolizasTraspaso.VerificaSaldo6100(conexionContabilidad, "1", arregloParametros[1], "_eli_pub"));
                                if (Positivo6100) {
                                    cadena = siafmPolizasTraspaso.formaOQTraspaso6100_a_6200(conexionContabilidad, "1", arregloParametros[1], "_eli_pub");
                                } else {
                                    cadena = "";
                                }
                                break;
                            case 697:
                                if (Positivo6100) {
                                    cadena = siafmPolizasTraspaso.formaORTraspaso6100_a_6200(conexionContabilidad, "1", arregloParametros[1], "_eli_pub");
                                } else {
                                    cadena = "";
                                }
                                break;
                            case 698:
                                if (!Positivo6100) {
                                    cadena = siafmPolizasTraspaso.formaOSTraspaso6100_a_6300(conexionContabilidad, "1", arregloParametros[1], "_eli_pub");
                                } else {
                                    cadena = "";
                                }
                                break;
                            case 699:
                                if (!Positivo6100) {
                                    cadena = siafmPolizasTraspaso.formaOTTraspaso6100_a_6300(conexionContabilidad, "1", arregloParametros[1], "_eli_pub");
                                } else {
                                    cadena = "";
                                }
                                break;
                            case 700:
                                if (Positivo6100) {
                                    cadena = siafmPolizasTraspaso.formaOUTraspaso6200_a_3210(conexionContabilidad, "1", arregloParametros[1], "_eli_pub");
                                } else {
                                    cadena = "";
                                }
                                break;
                            case 701:
                                if (Positivo6100) {
                                    cadena = siafmPolizasTraspaso.formaOVTraspaso6200_a_3210(conexionContabilidad, "1", arregloParametros[1], "_eli_pub");
                                } else {
                                    cadena = "";
                                }
                                break;

                            case 702:
                                if (!Positivo6100) {
                                    cadena = siafmPolizasTraspaso.formaOWTraspaso6300_a_3210(conexionContabilidad, "1", arregloParametros[1], "_eli_pub");
                                } else {
                                    cadena = "";
                                }
                                break;
                            case 703:
                                if (!Positivo6100) {
                                    cadena = siafmPolizasTraspaso.formaOXTraspaso6300_a_3210(conexionContabilidad, "1", arregloParametros[1], "_eli_pub");
                                } else {
                                    cadena = "";
                                }
                                break;
                        }
                        if (!cadena.isEmpty()) {
                            preparaRegistro(cadena, claveSeguridadWS, arregloParametros[0], crs.getString("forma"), crs.getString("unidadejecutora"), crs.getString("entidad"), crs.getString("ambito"), siafmPolizasTraspaso.getReferenciaGral(), crs.getString("concepto"), crs.getString("catalogocuenta"), String.valueOf(eventoContable), ejercicio);
                            if (forma == 689 || forma == 690) {
                                resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.MIPF_NO_ACUMULADO);
                            } else {
                                resultado = registro.aplicaRegistroContable(conexionContabilidad, crs.getString("catalogocuenta"), Areas.CONPROVE);
                            }
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
                resultado = "1,El evento contable " + eventoContable + " con numero de proceso " + eventoProceso + ", fue procesado correctamente y genero las siguientes polizas [" + lsPolizas + "]";
                if (eventoContable == 300) {
                    cuentaContable.update_rf_tr_cuentas_contables_acumulados_eli(conexionContabilidad, ejercicio, "1", arregloParametros[0].substring(3, 5));
                }
                if (eventoContable == 301) {
                    pbProcesos.update_rf_tr_procesos_cierre_estatus(conexionContabilidad, ejercicio, arregloParametros[0].substring(3, 5), "6", "1");
                }
                if (eventoContable == 302) {
                    pbProcesos.update_rf_tr_procesos_cierre_estatus(conexionContabilidad, ejercicio, arregloParametros[0].substring(3, 5), "7", "1");
                }
                conexionContabilidad.commit();
                conexionSistema.commit();
                enviaNotitificacionCuentaPub("Estatus del Proceso de Cierre Anual de Cuenta Publica del ejercicio" + arregloParametros[1], "El proceso Cierre Anual de Cuenta Publica" + tipoCierre + " ha concluido satisfactoriamente, favor de revisar las polizas en el sistema.");

            } else {
                throw new Exception(resultado);
            }
            System.out.println(resultado);
            return resultado;
        } catch (Exception e) {
            conexionContabilidad.rollback();
            conexionSistema.rollback();
            enviaNotitificacionCuentaPub("Error - Cierre Anual de Cuenta Publica del ejercicio" + arregloParametros[1], "Ha ocurrido un error en el proceso Cierre Anual " + tipoCierre + ": " + "Forma: " + Nombreforma + " " + e.getMessage() + ".  Favor de comunicarse con el administrador del sistema.");
            System.out.println("Ocurrio un error al accesar al metodo aplicaRegistroContableTraspaso " + e.getMessage());
            throw e;
        } finally {
            if (crs != null) {
                crs.close();
                crs = null;
            }
            if (conexionSistema != null) {
                conexionSistema.close();
                conexionSistema = null;
            }
            if (conexionContabilidad != null) {
                conexionContabilidad.close();
                conexionContabilidad = null;
            }
        }

    }
}
