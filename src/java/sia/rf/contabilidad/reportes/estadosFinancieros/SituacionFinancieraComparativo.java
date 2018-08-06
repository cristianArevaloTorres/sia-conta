/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sia.rf.contabilidad.reportes.estadosFinancieros;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import sia.libs.pagina.Funciones;

/**
 *
 * @author claudia.macariot
 */
public class SituacionFinancieraComparativo {

    private int count;
    private List listaFinal;

    public List getListaFinal() {
        return listaFinal;
    }

    public void setListaFinal(List listaFinal) {
        this.listaFinal = listaFinal;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
//String cierre, String programa,String nivel, String unidad, String ambito, HttpServletRequest request

    public SituacionFinancieraComparativo() {
        this.count = 0;
        this.listaFinal = new ArrayList();
    }

    public void vaciarInformacion(Connection conexion, ConsultasEdosFinancieros consulta, String fecha, String mes, String mesCompara, String ejercicioCompara, int tipo) throws SQLException {
        recuperar(conexion, consulta, fecha, mes, mesCompara, ejercicioCompara, tipo);
    }

    private void recuperar(Connection conexion, ConsultasEdosFinancieros consulta, String fecha, String mes, String mesCompara, String ejercicioCompara, int tipo) throws SQLException {
        StringBuffer query;
        String descripcion;

        Funciones util;
        Statement stm = null;
        ResultSet rst = null;

        int ajuste;
        int emision = 0;

        boolean bExisteS2 = false;
        boolean bExisteCuentasOrden = false;
        boolean bExiste3220 = false;
        boolean tituloActivoNoCir = false;
        boolean tituloPasivoNoCir = false;
        boolean tituloHacienda = false;
        boolean tituloHaciendaGen = false;

        double totalCuentaOrdenA[][] = new double[3][5];
        double totalCuentaOrdenB[][] = new double[3][5];
        double totalS1A[] = new double[3];
        double totalS1B[] = new double[3];
        double ahorroDesahorro[] = new double[4];

        List listaRegistrosA;
        List listaRegistrosB;
        List listaRegistrosFinal;

        RegistroReporteV2 registroFinal;
        RegistroEdoFinV2 registroE = null;

        String cuentas[][] = new String[][]{
            //Todas las cuentas que inician con 3 digitos y una N corresponden al rubro almacenado en la tabla rf_tc_clase_clasif_cta 
            //Si cambia un rubro o se agrega uno, se deberá agregar en esta sección a fin de que no aparezcan las descripciones
            //XDescripcion SubTotales
            {"111N", "111 Efectivo y Equivalentes"},
            {"112N", "112 Derechos a Recibir Efectivo o Equivalentes"},
            {"113N", "113 Derechos a Recibir Bienes o Servicios"},
            {"114N", "114 Inventarios"},
            {"115N", "115 Almacenes"},
            {"116N", "116 Estimación por Pérdida o Deterioro de Activos Circulantes"},
            {"119N", "119 Otros Activos Circulantes"},
            {"121N", "121 Inversiones Financieras a Largo Plazo"},
            {"122N", "122 Derechos a Recibir Efectivo o Equivalentes a Largo Plazo"},
            {"123N", "123 Bienes Inmuebles, Infraestructura y Construcciones en Proceso"},
            {"124N", "124 Bienes Muebles"},
            {"125N", "125 Activos Intangibles"},
            {"126N", "126 Depreciación, Deterioro y Amortización Acumulada de Bienes"},
            {"127N", "127 Activos Diferidos"},
            {"128N", "128 Estimación por Pérdida o Deterioro de Activos no Circulantes"},
            {"129N", "129 Otros Activos no Circulantes"},
            {"211N", "211 Cuentas por Pagar a Corto Plazo"},
            {"213N", "213 Porción a Corto Plazo de la Deuda Pública a Largo Plazo"},
            {"215N", "215 Pasivos Diferidos a Corto Plazo"},
            {"217N", "217 Provisiones a Corto Plazo"},
            {"219N", "219 Otros Pasivos a Corto Plazo"},
            {"221N", "221 Cuentas por Pagar a Largo Plazo"},
            {"222N", "222 Documentos por Pagar a Largo Plazo"},
            {"223N", "223 Deuda Pública a Largo Plazo"},
            {"226N", "226 Provisiones a Largo Plazo"},
            {"311N", "311 Aportaciones"},
            {"312N", "312 Donaciones de Capital"},
            {"313N", "313 Actualización de la Hacienda Pública/Patrimonio"},
            {"314N", "314 Donaciones de Bienes Inmuebles"},
            {"321N", "321 Resultados del Ejercicio (Ahorro/Desahorro)"},
            {"322N", "322 Resultados de Ejercicios anteriores"},
            {"323N", "323 Revalúos"},
            {"324N", "324 Reservas"},
            {"325N", "325 Rectificaciones de resultados de ejercicios anteriores"},
            {"330N", "330 Exceso o Insuficiencia en la Actualización del Patrimonio"},
            {"331N", "331 Resultado por Posición Monetaria"},
            {"332N", "332 Resultados por Tenencia de Activos no Monetarios"},
            {"333N", "333 Exceso o Insuficiencia en la Actualización de la Hacienda Pública/Patrimonio"},
            {"1199", "Total de Activo Circulante"},
            {"1299", "Total de Activo No Circulante"},
            {"2199", "Total de Pasivo Circulante"},
            {"2299", "Total de Pasivo No Circulante"},
            {"3NNN", "3 Hacienda Pública/Patrimonio"}
        };
        try {
            util = new Funciones();
            stm = conexion.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            listaRegistrosA = new ArrayList();
            listaRegistrosB = new ArrayList();
            listaRegistrosFinal = new ArrayList();
            query = new StringBuffer();

            //consulta = new ConsultasEdosFinancieros(cierre, programa, nivel, unidad, ambito, request);
            for (int co = 0; co < 3; co++) {
                for (int cos = 0; cos < 5; cos++) {
                    totalCuentaOrdenA[co][cos] = 0;
                    totalCuentaOrdenB[co][cos] = 0;
                }
            }

            for (int co = 0; co < 3; co++) {
                totalS1A[co] = 0;
                totalS1B[co] = 0;
            }
            // Linea donde se trae el query de ahorro desahorro
            /*query.append(consulta.situacionFinancieraAhorro(fecha, mes, mesCompara, ejercicioCompara));
             rst   = stm.executeQuery(query.toString());
             if (rst.next()){
             ahorroDesahorro[0]= rst.getDouble("AHORRO_DESAHORRO_MA");
             ahorroDesahorro[1]= rst.getDouble("AHORRO_DESAHORRO_MB");
             ahorroDesahorro[2]= rst.getDouble("VARIACION");
             ahorroDesahorro[3]= rst.getDouble("PORCENTAJE");
             }
             rst.close();*/

            // Lineas donde se trae el query para el reporte simple(1) y (2) detallado 
            query = new StringBuffer();
            if (tipo == 1) {
                query.append(consulta.situacionFinanciera(fecha, mes, mesCompara, ejercicioCompara));
            } else if (tipo == 2) {
                query.append(consulta.situacionFinancieraDetallado(fecha, mes, mesCompara, ejercicioCompara));
            } else if (tipo == 3) {
                query.append(consulta.cambiosSituacionFinanciera(fecha, mes, mesCompara, ejercicioCompara));
            }

            rst = stm.executeQuery(query.toString());
            //Navega entre los resultados del Query Escogido (Simple y Detallado)    
            while (rst.next()) {
                if (rst.getString("N1").equals("3220") && !bExiste3220) {
                    bExiste3220 = true;
                }
                if (rst.getString("N1").equals("3NNN")) {
                    registroE = new RegistroEdoFinV2(null, null, null, null, null, false, false);
                    listaRegistrosB.add(registroE);
                    registroE = new RegistroEdoFinV2(
                            "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">TOTAL PASIVO</style>",
                            "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(util.formatearNumero("#,##0.00", totalS1B[0])).concat("</style>"),
                            "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(util.formatearNumero("#,##0.00", totalS1B[1])).concat("</style>"),
                            "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(util.formatearNumero("#,##0.00", totalS1B[2])).concat("</style>"),
                            "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(util.formatearNumero("0.00", ((totalS1B[1] - totalS1B[0]) != 0 ? (totalS1B[1] != 0 ? (((totalS1B[1] - totalS1B[0]) / totalS1B[1]) * 100) : ((totalS1B[1] - totalS1B[0]) > 0 ? 100.00 : -100.00)) : 0) * -1)).concat("</style>"),
                            false, true);
                    listaRegistrosB.add(registroE);
                }
                /*
                 if ( rst.getString("N1").equals("3220") || (rst.getString("N1").equals("3999") && !bExiste3220)){
                 registroE = new RegistroEdoFinV2(
                 "   Resultado del Ejercicio (Ahorro/Desahorro)", 
                 util.formatearNumero("#,##0.00",ahorroDesahorro[0]), 
                 util.formatearNumero("#,##0.00",ahorroDesahorro[1]), 
                 util.formatearNumero("#,##0.00",ahorroDesahorro[2]), 
                 util.formatearNumero("0.00", ((ahorroDesahorro[1]- ahorroDesahorro[0])!= 0? ( ahorroDesahorro[1] != 0 ? ( ((ahorroDesahorro[1]- ahorroDesahorro[0])/ ahorroDesahorro[1])*100): ( (ahorroDesahorro[1]- ahorroDesahorro[0]) > 0? 100.00: -100.00) ): 0 )*-1),
                 false, false               );
                 listaRegistrosB.add(registroE);
                 totalS1B[0]+= ahorroDesahorro[0];
                 totalS1B[1]+= ahorroDesahorro[1];
                 totalS1B[2]+= ahorroDesahorro[2];
                 }
                 */
                /*HACE LA SUMA DE LOS TOATALES*/
                if (rst.getString("SECCION").equals("S1") && rst.getString("N1").indexOf("N") < 0) {
                    if (((rst.getString("N1").equals("1199") || 
                          rst.getString("N1").equals("1299") || 
                          rst.getString("N1").equals("2199") || 
                          rst.getString("N1").equals("2299") || 
                          rst.getString("N1").equals("3999")) && 
                          !rst.getString("DESCRIPCION").equals("Otros Pasivos Circulantes"))) {
                        if (rst.getString("REPORTE").equals("LA")) {
                            totalS1A[0] += rst.getDouble("SALDO_ACTUAL");
                            totalS1A[1] += rst.getDouble("SALDO_ANTERIOR");
                            totalS1A[2] += rst.getDouble("VARIACION");
                        } else {
                            totalS1B[0] += rst.getDouble("SALDO_ACTUAL");
                            totalS1B[1] += rst.getDouble("SALDO_ANTERIOR");
                            totalS1B[2] += rst.getDouble("VARIACION");
                        }
                    }//Fin if Segundo
                }//Fin if Primero

                if (!bExisteCuentasOrden && !rst.getString("SECCION").equals("S1")) {
                    registroE = new RegistroEdoFinV2(null, null, null, null, null, false, false);
                    if (listaRegistrosA.size() > listaRegistrosB.size()) {
                        for (ajuste = listaRegistrosB.size(); ajuste < listaRegistrosA.size(); ajuste++) {
                            listaRegistrosB.add(registroE);
                        }
                    } else {
                        for (ajuste = listaRegistrosA.size(); ajuste < listaRegistrosB.size(); ajuste++) {
                            listaRegistrosA.add(registroE);
                        }
                    }
                    listaRegistrosA.add(registroE);
                    listaRegistrosB.add(registroE);
                    registroE = new RegistroEdoFinV2(
                            "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">TOTAL ACTIVO</style>",
                            "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(util.formatearNumero("#,##0.00", totalS1A[0])).concat("</style>"),
                            "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(util.formatearNumero("#,##0.00", totalS1A[1])).concat("</style>"),
                            "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(util.formatearNumero("#,##0.00", totalS1A[2])).concat("</style>"),
                            "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(util.formatearNumero("0.00", ((totalS1A[1] - totalS1A[0]) != 0 ? (totalS1A[1] != 0 ? (((totalS1A[1] - totalS1A[0]) / totalS1A[1]) * 100) : ((totalS1A[1] - totalS1A[0]) > 0 ? 100.00 : -100.00)) : 0) * -1)).concat("</style>"),
                            false, true);
                    listaRegistrosA.add(registroE);
                    registroE = new RegistroEdoFinV2(
                            "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">TOTAL DE PASIVO Y HACIENDA PÚBLICA / PATRIMONIO</style>",
                            "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(util.formatearNumero("#,##0.00", totalS1B[0])).concat("</style>"),
                            "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(util.formatearNumero("#,##0.00", totalS1B[1])).concat("</style>"),
                            "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(util.formatearNumero("#,##0.00", totalS1B[2])).concat("</style>"),
                            "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(util.formatearNumero("0.00", ((totalS1B[1] - totalS1B[0]) != 0 ? (totalS1B[1] != 0 ? (((totalS1B[1] - totalS1B[0]) / totalS1B[1]) * 100) : ((totalS1B[1] - totalS1B[0]) > 0 ? 100.00 : -100.00)) : 0) * -1)).concat("</style>"),
                            false, true);
                    listaRegistrosB.add(registroE);
                    registroE = new RegistroEdoFinV2(null, null, null, null, null, false, false);
                    listaRegistrosA.add(registroE);
                    listaRegistrosB.add(registroE);
                    bExisteCuentasOrden = true;
                }

                if (rst.getString("N1").equals("7999") || 
                    rst.getString("N1").equals("8199") || 
                    rst.getString("N1").equals("8299")) {
                    if (rst.getString("REPORTE").equals("LA")) {
                        totalCuentaOrdenA[(rst.getString("N1").equals("7999") ? 0 : (rst.getString("N1").equals("8199") ? 1 : 2))][0] = 1;
                        totalCuentaOrdenA[(rst.getString("N1").equals("7999") ? 0 : (rst.getString("N1").equals("8199") ? 1 : 2))][1] += rst.getDouble("SALDO_ACTUAL");
                        totalCuentaOrdenA[(rst.getString("N1").equals("7999") ? 0 : (rst.getString("N1").equals("8199") ? 1 : 2))][2] += rst.getDouble("SALDO_ANTERIOR");
                        totalCuentaOrdenA[(rst.getString("N1").equals("7999") ? 0 : (rst.getString("N1").equals("8199") ? 1 : 2))][3] += rst.getDouble("VARIACION");
                        totalCuentaOrdenA[(rst.getString("N1").equals("7999") ? 0 : (rst.getString("N1").equals("8199") ? 1 : 2))][4] += rst.getDouble("PORCENTAJE");
                    } else {
                        totalCuentaOrdenB[(rst.getString("N1").equals("7999") ? 0 : (rst.getString("N1").equals("8199") ? 1 : 2))][0] = 1;
                        totalCuentaOrdenB[(rst.getString("N1").equals("7999") ? 0 : (rst.getString("N1").equals("8199") ? 1 : 2))][1] += rst.getDouble("SALDO_ACTUAL");
                        totalCuentaOrdenB[(rst.getString("N1").equals("7999") ? 0 : (rst.getString("N1").equals("8199") ? 1 : 2))][2] += rst.getDouble("SALDO_ANTERIOR");
                        totalCuentaOrdenB[(rst.getString("N1").equals("7999") ? 0 : (rst.getString("N1").equals("8199") ? 1 : 2))][3] += rst.getDouble("VARIACION");
                        totalCuentaOrdenB[(rst.getString("N1").equals("7999") ? 0 : (rst.getString("N1").equals("8199") ? 1 : 2))][4] += rst.getDouble("PORCENTAJE");
                    }
                }

                if (rst.getString("N1").equals("700N") && rst.getString("REPORTE").equals("LA")) {
                    registroE = new RegistroEdoFinV2(null, null, null, null, null, false, false);
                    listaRegistrosA.add(registroE);
                    listaRegistrosB.add(registroE);
                    listaRegistrosA.add(registroE);
                    registroE = new RegistroEdoFinV2("<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">CUENTAS DE ORDEN CONTABLE</style>", null, null, null, null, false, false);
                    listaRegistrosB.add(registroE);
                }

                if (rst.getString("N1").equals("81NN") && rst.getString("REPORTE").equals("LA")) {
                    registroE = new RegistroEdoFinV2(null, null, null, null, null, false, false);

                    if (listaRegistrosA.size() > listaRegistrosB.size()) {
                        for (ajuste = listaRegistrosB.size(); ajuste < listaRegistrosA.size(); ajuste++) {
                            listaRegistrosB.add(registroE);
                        }
                    } else {
                        for (ajuste = listaRegistrosA.size(); ajuste < listaRegistrosB.size(); ajuste++) {
                            listaRegistrosA.add(registroE);
                        }
                    }

                    if (totalCuentaOrdenA[0][0] == 1) {
                        listaRegistrosA.add(registroE);
                        listaRegistrosB.add(registroE);
                        registroE = new RegistroEdoFinV2(
                                "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">Total Cuentas de Orden Contable</style>",
                                "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(util.formatearNumero("#,##0.00", totalCuentaOrdenA[0][1])).concat("</style>"),
                                "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(util.formatearNumero("#,##0.00", totalCuentaOrdenA[0][2])).concat("</style>"),
                                "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(util.formatearNumero("#,##0.00", totalCuentaOrdenA[0][3])).concat("</style>"),
                                "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(util.formatearNumero("0.00", ((totalCuentaOrdenA[0][2] - totalCuentaOrdenA[0][1]) != 0 ? (totalCuentaOrdenA[0][2] != 0 ? (((totalCuentaOrdenA[0][2] - totalCuentaOrdenA[0][1]) / totalCuentaOrdenA[0][2]) * 100) : ((totalCuentaOrdenA[0][2] - totalCuentaOrdenA[0][1]) > 0 ? 100.00 : -100.00)) : 0) * -1)).concat("</style>"),
                                false, true);
                        listaRegistrosA.add(registroE);
                        registroE = new RegistroEdoFinV2(
                                "",
                                "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(util.formatearNumero("#,##0.00", totalCuentaOrdenB[0][1])).concat("</style>"),
                                "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(util.formatearNumero("#,##0.00", totalCuentaOrdenB[0][2])).concat("</style>"),
                                "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(util.formatearNumero("#,##0.00", totalCuentaOrdenB[0][3])).concat("</style>"),
                                "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(util.formatearNumero("0.00", ((totalCuentaOrdenB[0][2] - totalCuentaOrdenB[0][1]) != 0 ? (totalCuentaOrdenB[0][2] != 0 ? (((totalCuentaOrdenB[0][2] - totalCuentaOrdenB[0][1]) / totalCuentaOrdenB[0][2]) * 100) : ((totalCuentaOrdenB[0][2] - totalCuentaOrdenB[0][1]) > 0 ? 100.00 : -100.00)) : 0) * -1)).concat("</style>"),
                                false, true);
                        listaRegistrosB.add(registroE);
                    }
                    registroE = new RegistroEdoFinV2(null, null, null, null, null, false, false);
                    listaRegistrosA.add(registroE);
                    listaRegistrosB.add(registroE);
                    registroE = new RegistroEdoFinV2(null, null, null, null, null, false, false);
                    listaRegistrosA.add(registroE);
                    registroE = new RegistroEdoFinV2("<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">CUENTAS DE ORDEN PRESUPUESTAL</style>", null, null, null, null, false, false);
                    listaRegistrosB.add(registroE);
                    registroE = new RegistroEdoFinV2(null, null, null, null, null, false, false);
                    listaRegistrosA.add(registroE);
                    listaRegistrosB.add(registroE);
                    registroE = new RegistroEdoFinV2(null, null, null, null, null, false, false);
                    listaRegistrosA.add(registroE);
                    registroE = new RegistroEdoFinV2("<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">LEY DE INGRESOS</style>", null, null, null, null, false, false);
                    listaRegistrosB.add(registroE);
                }

                if (rst.getString("N1").equals("82NN") && rst.getString("REPORTE").equals("LA")) {
                    registroE = new RegistroEdoFinV2(null, null, null, null, null, false, false);
                    if (listaRegistrosA.size() > listaRegistrosB.size()) {
                        for (ajuste = listaRegistrosB.size(); ajuste < listaRegistrosA.size(); ajuste++) {
                            listaRegistrosB.add(registroE);
                        }
                    } else {
                        for (ajuste = listaRegistrosA.size(); ajuste < listaRegistrosB.size(); ajuste++) {
                            listaRegistrosA.add(registroE);
                        }
                    }
                    if (totalCuentaOrdenA[1][0] == 1) {
                        listaRegistrosA.add(registroE);
                        listaRegistrosB.add(registroE);
                        registroE = new RegistroEdoFinV2(
                                "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">Total Ley de Ingresos</style>",
                                "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(util.formatearNumero("#,##0.00", totalCuentaOrdenA[1][1])).concat("</style>"),
                                "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(util.formatearNumero("#,##0.00", totalCuentaOrdenA[1][2])).concat("</style>"),
                                "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(util.formatearNumero("#,##0.00", totalCuentaOrdenA[1][3])).concat("</style>"),
                                "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(util.formatearNumero("0.00", ((totalCuentaOrdenA[1][2] - totalCuentaOrdenA[1][1]) != 0 ? (totalCuentaOrdenA[1][2] != 0 ? (((totalCuentaOrdenA[1][2] - totalCuentaOrdenA[1][1]) / totalCuentaOrdenA[1][2]) * 100) : ((totalCuentaOrdenA[1][2] - totalCuentaOrdenA[1][1]) > 0 ? 100.00 : -100.00)) : 0) * -1)).concat("</style>"),
                                false, true);
                        listaRegistrosA.add(registroE);
                        registroE = new RegistroEdoFinV2(
                                "",
                                "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(util.formatearNumero("#,##0.00", totalCuentaOrdenB[1][1])).concat("</style>"),
                                "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(util.formatearNumero("#,##0.00", totalCuentaOrdenB[1][2])).concat("</style>"),
                                "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(util.formatearNumero("#,##0.00", totalCuentaOrdenB[1][3])).concat("</style>"),
                                "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(util.formatearNumero("0.00", ((totalCuentaOrdenB[1][2] - totalCuentaOrdenB[1][1]) != 0 ? (totalCuentaOrdenB[1][2] != 0 ? (((totalCuentaOrdenB[1][2] - totalCuentaOrdenB[1][1]) / totalCuentaOrdenB[1][2]) * 100) : ((totalCuentaOrdenB[1][2] - totalCuentaOrdenB[1][1]) > 0 ? 100.00 : -100.00)) : 0) * -1)).concat("</style>"),
                                false, true);
                        listaRegistrosB.add(registroE);
                    }
                    registroE = new RegistroEdoFinV2(null, null, null, null, null, false, false);
                    listaRegistrosA.add(registroE);
                    listaRegistrosB.add(registroE);
                    registroE = new RegistroEdoFinV2(null, null, null, null, null, false, false);
                    listaRegistrosA.add(registroE);
                    registroE = new RegistroEdoFinV2("<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">PRESUPUESTO DE EGRESOS</style>", null, null, null, null, false, false);
                    listaRegistrosB.add(registroE);
                }

                if (rst.getString("N1").indexOf("N") >= 0 || rst.getString("N1").equals("3999")) {
                    if (rst.getString("N1").equals("111N")) {
                        registroE = new RegistroEdoFinV2("<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">ACTIVO</style>", null, null, null, null, false, false);
                        listaRegistrosA.add(registroE);
                        registroE = new RegistroEdoFinV2(null, null, null, null, null, false, false);
                        listaRegistrosA.add(registroE);
                        registroE = new RegistroEdoFinV2("<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">ACTIVO CIRCULANTE</style>", null, null, null, null, false, false);
                        listaRegistrosA.add(registroE);
                    }
                    if (rst.getString("N1").equals("211N")) {
                        registroE = new RegistroEdoFinV2("<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">PASIVO</style>", null, null, null, null, false, false);
                        listaRegistrosB.add(registroE);
                        registroE = new RegistroEdoFinV2(null, null, null, null, null, false, false);
                        listaRegistrosB.add(registroE);
                        registroE = new RegistroEdoFinV2("<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">PASIVO CIRCULANTE</style>", null, null, null, null, false, false);
                        listaRegistrosB.add(registroE);
                    }
                    if ((rst.getString("N1").equals("121N") || rst.getString("N1").equals("122N") || rst.getString("N1").equals("123N") || rst.getString("N1").equals("124N")) && (tituloActivoNoCir == false)) {
                        registroE = new RegistroEdoFinV2(null, null, null, null, null, false, false);
                        listaRegistrosA.add(registroE);
                        registroE = new RegistroEdoFinV2("<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">ACTIVO NO CIRCULANTE</style>", null, null, null, null, false, false);
                        listaRegistrosA.add(registroE);
                        tituloActivoNoCir = true;
                    }
                    if (((rst.getString("N1").equals("221N") || rst.getString("N1").equals("222N")) || rst.getString("N1").equals("223N") || rst.getString("N1").equals("224N")) && tituloPasivoNoCir == false) {
                        registroE = new RegistroEdoFinV2(null, null, null, null, null, false, false);
                        listaRegistrosB.add(registroE);
                        registroE = new RegistroEdoFinV2("<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">PASIVO NO CIRCULANTE</style>", null, null, null, null, false, false);
                        listaRegistrosB.add(registroE);
                        tituloPasivoNoCir = true;
                    }
                    //   if (rst.getString("N1").equals("3NNN")){
                    if ((rst.getString("N1").equals("311N") || rst.getString("N1").equals("312N") || rst.getString("N1").equals("313N") || rst.getString("N1").equals("314N")) && (tituloHacienda == false)) {
                        registroE = new RegistroEdoFinV2(null, null, null, null, null, false, false);
                        listaRegistrosB.add(registroE);
                        registroE = new RegistroEdoFinV2("<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">HACIENDA PÚBLICA / PATRIMONIO</style>", null, null, null, null, false, false);
                        listaRegistrosB.add(registroE);
//                        registroE = new RegistroEdoFinV2(null, null, null, null, null, false, false);
//                        listaRegistrosB.add(registroE);
//                        registroE = new RegistroEdoFinV2("<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">HACIENDA PÚBLICA/PATRIMONIO CONSTITUIDO</style>", null, null, null, null, false, false);
//                        listaRegistrosB.add(registroE);
                        tituloHacienda = true;
                    }
//                    if ((rst.getString("N1").equals("321N") || rst.getString("N1").equals("322N") || rst.getString("N1").equals("323N")) && (tituloHaciendaGen == false)) {
//                        registroE = new RegistroEdoFinV2(null, null, null, null, null, false, false);
//                        listaRegistrosB.add(registroE);
//                        registroE = new RegistroEdoFinV2("<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">32 HACIENDA PÚBLICA/PATRIMONIO GENERADO</style>", null, null, null, null, false, false);
//                        listaRegistrosB.add(registroE);
//                        tituloHaciendaGen = true;
//                    }
                    if (rst.getString("N1").equals("333N")) {
                        registroE = new RegistroEdoFinV2(null, null, null, null, null, false, false);
                        listaRegistrosB.add(registroE);
                        registroE = new RegistroEdoFinV2("<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">EXCESO O INSUFICIENCIA EN LA ACTUALIZACIÓN DE LA HACIENDA PÚBLICA/PATRIMONIO</style>", null, null, null, null, false, false);
                        listaRegistrosB.add(registroE);
                        tituloHaciendaGen = true;
                    }
                    if (rst.getString("N1").equals("3999")) {
                        registroE = new RegistroEdoFinV2(null, null, null, null, null, false, false);
                        listaRegistrosB.add(registroE);
                        ahorroDesahorro[0] += rst.getDouble("SALDO_ACTUAL");
                        ahorroDesahorro[1] += rst.getDouble("SALDO_ANTERIOR");
                        ahorroDesahorro[2] += rst.getDouble("VARIACION");
                        registroE = new RegistroEdoFinV2(
                                "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(sia.libs.formato.Cadena.getFormatoOracion(rst.getString("DESCRIPCION"))).concat("</style>"),
                                "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(util.formatearNumero("#,##0.00", ahorroDesahorro[0])).concat("</style>"),
                                "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(util.formatearNumero("#,##0.00", ahorroDesahorro[1])).concat("</style>"),
                                "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(util.formatearNumero("#,##0.00", ahorroDesahorro[2])).concat("</style>"),
                                "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(util.formatearNumero("0.00", ((ahorroDesahorro[1] - ahorroDesahorro[0]) != 0 ? (ahorroDesahorro[1] != 0 ? (((ahorroDesahorro[1] - ahorroDesahorro[0]) / ahorroDesahorro[1]) * 100) : ((ahorroDesahorro[1] - ahorroDesahorro[0]) > 0 ? 100.00 : -100.00)) : 0) * -1)).concat("</style>"),
                                false, true);
                        listaRegistrosB.add(registroE);
                    }
                }

                if ((rst.getString("N1").equals("1199") || rst.getString("N1").equals("1299") || rst.getString("N1").equals("2199") || rst.getString("N1").equals("3999") || rst.getString("N1").equals("2299")) && !rst.getString("DESCRIPCION").equals("Otros Pasivos Circulantes")) {
                    registroE = new RegistroEdoFinV2(null, null, null, null, null, false, false);
                    if (rst.getString("REPORTE").equals("LA")) {
                        listaRegistrosA.add(registroE);
                    } else {
                        listaRegistrosB.add(registroE);
                    }
                }

                //descripcion = sia.libs.formato.Cadena.getFormatoOracion(rst.getString("DESCRIPCION"));
                descripcion = rst.getString("DESCRIPCION");
                if (rst.getString("DESCRIPCION").indexOf("XDescripcion ") >= 0 || rst.getString("DESCRIPCION").indexOf("Xdescripcion ") >= 0 || rst.getString("N1").equals("1199") || rst.getString("N1").equals("1299")) {
                    for (int t = 0; t < cuentas.length; t++) {
                        if (cuentas[t][0].equals(rst.getString("N1"))) {
                            if (rst.getString("DESCRIPCION").indexOf("XDescripcion SubTotales") >= 0 || rst.getString("DESCRIPCION").indexOf("Xdescripcion Subtotales") >= 0) {
                                registroE = new RegistroEdoFinV2(null, null, null, null, null, false, false);
                                if (rst.getString("REPORTE").equals("LA")) {
                                    listaRegistrosA.add(registroE);
                                } else {
                                    listaRegistrosB.add(registroE);
                                }
                            }
                            descripcion = "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(cuentas[t][1]).concat("</style>");
                            t = cuentas.length;
                        }
                    }
                }

                if (!(rst.getString("N1").equals("3999") || rst.getString("N1").equals("1199") || rst.getString("N1").equals("1299") || rst.getString("N1").equals("2199") || rst.getString("N1").equals("2299")) || rst.getString("DESCRIPCION").equals("Otros Pasivos Circulantes")) {
                    descripcion = "   ".concat(descripcion);
                }
                if (rst.getString("N1").equals("1131")) {
                    descripcion = "   Anticipo a Proveedores por Adquisición de Bienes y Prestación de ";
                }
                descripcion = descripcion.indexOf("<style") != -1 ? descripcion : "   " + sia.libs.formato.Cadena.getFormatoOracion(descripcion);
                if (!(rst.getString("N1").equals("700N") || rst.getString("N1").equals("81NN") || rst.getString("N1").equals("82NN") || rst.getString("N1").equals("7999") || rst.getString("N1").equals("8199") || rst.getString("N1").equals("8299") || rst.getString("N1").equals("3NNN") || rst.getString("N1").equals("3999"))) {
                    registroE = new RegistroEdoFinV2(
                            //sia.libs.formato.Cadena.getFormatoOracion(descripcion), 
                            descripcion,
                            (descripcion.indexOf("<style") >= 0 ? "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">" : "").concat(util.formatearNumero("#,##0.00", rst.getDouble("SALDO_ACTUAL"))).concat(descripcion.indexOf("<style") >= 0 ? "</style>" : ""),
                            (descripcion.indexOf("<style") >= 0 ? "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">" : "").concat(util.formatearNumero("#,##0.00", rst.getDouble("SALDO_ANTERIOR"))).concat(descripcion.indexOf("<style") >= 0 ? "</style>" : ""),
                            (descripcion.indexOf("<style") >= 0 ? "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">" : "").concat(util.formatearNumero("#,##0.00", rst.getDouble("VARIACION"))).concat(descripcion.indexOf("<style") >= 0 ? "</style>" : ""),
                            (descripcion.indexOf("<style") >= 0 ? "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">" : "").concat(util.formatearNumero("0.00", rst.getDouble("PORCENTAJE"))).concat(descripcion.indexOf("<style") >= 0 ? "</style>" : ""),
                            false, ((rst.getString("N1").equals("1199") || rst.getString("N1").equals("1299") || rst.getString("N1").equals("2199") || rst.getString("N1").equals("2299") || rst.getString("N1").equals("3999")) && !rst.getString("DESCRIPCION").equals("Otros Pasivos Circulantes")));
                    if (rst.getString("REPORTE").equals("LA")) {
                        listaRegistrosA.add(registroE);
                    } else {
                        listaRegistrosB.add(registroE);
                    }
                }
                if (rst.getString("N1").equals("1131")) {
                    registroE = new RegistroEdoFinV2("   Servicios a Corto Plazo", null, null, null, null, false, false);
                    listaRegistrosA.add(registroE);
                }
            }//Fin de While

            rst.close();
            if (!bExisteCuentasOrden) {
                if (listaRegistrosA.size() > listaRegistrosB.size()) {
                    for (ajuste = listaRegistrosB.size(); ajuste < listaRegistrosA.size(); ajuste++) {
                        registroE = new RegistroEdoFinV2(null, null, null, null, null, false, false);
                        listaRegistrosB.add(registroE);
                    }
                } else {
                    for (ajuste = listaRegistrosA.size(); ajuste < listaRegistrosB.size(); ajuste++) {
                        registroE = new RegistroEdoFinV2(null, null, null, null, null, false, false);
                        listaRegistrosA.add(registroE);
                    }
                }
                registroE = new RegistroEdoFinV2(
                        "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">TOTAL ACTIVO</style>",
                        "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(util.formatearNumero("#,##0.00", totalS1A[0])).concat("</style>"),
                        "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(util.formatearNumero("#,##0.00", totalS1A[1])).concat("</style>"),
                        "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(util.formatearNumero("#,##0.00", totalS1A[2])).concat("</style>"),
                        "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(util.formatearNumero("0.00", ((totalS1A[1] - totalS1A[0]) != 0 ? (totalS1A[1] != 0 ? (((totalS1A[1] - totalS1A[0]) / totalS1A[1]) * 100) : ((totalS1A[1] - totalS1A[0]) > 0 ? 100.00 : -100.00)) : 0) * -1)).concat("</style>"),
                        false, true);
                listaRegistrosA.add(registroE);
                registroE = new RegistroEdoFinV2(
                        "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">TOTAL DE PASIVO Y HACIENDA PÚBLICA / PATRIMONIO</style>",
                        "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(util.formatearNumero("#,##0.00", totalS1B[0])).concat("</style>"),
                        "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(util.formatearNumero("#,##0.00", totalS1B[1])).concat("</style>"),
                        "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(util.formatearNumero("#,##0.00", totalS1B[2])).concat("</style>"),
                        "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(util.formatearNumero("0.00", ((totalS1B[1] - totalS1B[0]) != 0 ? (totalS1B[1] != 0 ? (((totalS1B[1] - totalS1B[0]) / totalS1B[1]) * 100) : ((totalS1B[1] - totalS1B[0]) > 0 ? 100.00 : -100.00)) : 0) * -1)).concat("</style>"),
                        false, true);
                listaRegistrosB.add(registroE);
            }

            if (totalCuentaOrdenA[2][0] == 1) {
                if (listaRegistrosA.size() > listaRegistrosB.size()) {
                    for (ajuste = listaRegistrosB.size(); ajuste < listaRegistrosA.size(); ajuste++) {
                        registroE = new RegistroEdoFinV2(null, null, null, null, null, false, false);
                        listaRegistrosB.add(registroE);
                    }
                } else {
                    for (ajuste = listaRegistrosA.size(); ajuste < listaRegistrosB.size(); ajuste++) {
                        registroE = new RegistroEdoFinV2(null, null, null, null, null, false, false);
                        listaRegistrosA.add(registroE);
                    }
                }
                listaRegistrosA.add(registroE);
                listaRegistrosB.add(registroE);
                registroE = new RegistroEdoFinV2(
                        "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">Total Presupuesto de Egresos</style>",
                        "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(util.formatearNumero("#,##0.00", totalCuentaOrdenA[2][1])).concat("</style>"),
                        "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(util.formatearNumero("#,##0.00", totalCuentaOrdenA[2][2])).concat("</style>"),
                        "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(util.formatearNumero("#,##0.00", totalCuentaOrdenA[2][3])).concat("</style>"),
                        "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(util.formatearNumero("0.00", ((totalCuentaOrdenA[2][2] - totalCuentaOrdenA[2][1]) != 0 ? (totalCuentaOrdenA[2][2] != 0 ? (((totalCuentaOrdenA[2][2] - totalCuentaOrdenA[2][1]) / totalCuentaOrdenA[2][2]) * 100) : ((totalCuentaOrdenA[2][2] - totalCuentaOrdenA[2][1]) > 0 ? 100.00 : -100.00)) : 0) * -1)).concat("</style>"),
                        false, true);
                listaRegistrosA.add(registroE);
                registroE = new RegistroEdoFinV2(
                        "",
                        "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(util.formatearNumero("#,##0.00", totalCuentaOrdenB[2][1])).concat("</style>"),
                        "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(util.formatearNumero("#,##0.00", totalCuentaOrdenB[2][2])).concat("</style>"),
                        "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(util.formatearNumero("#,##0.00", totalCuentaOrdenB[2][3])).concat("</style>"),
                        "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(util.formatearNumero("0.00", ((totalCuentaOrdenB[2][2] - totalCuentaOrdenB[2][1]) != 0 ? (totalCuentaOrdenB[2][2] != 0 ? (((totalCuentaOrdenB[2][2] - totalCuentaOrdenB[2][1]) / totalCuentaOrdenB[2][2]) * 100) : ((totalCuentaOrdenB[2][2] - totalCuentaOrdenB[2][1]) > 0 ? 100.00 : -100.00)) : 0) * -1)).concat("</style>"),
                        false, true);
                listaRegistrosB.add(registroE);
            }

            if (totalCuentaOrdenA[1][0] == 1 || totalCuentaOrdenA[2][0] == 1) {
                registroE = new RegistroEdoFinV2(null, null, null, null, null, false, false);
                listaRegistrosA.add(registroE);
                listaRegistrosB.add(registroE);
                registroE = new RegistroEdoFinV2(
                        "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">Total Cuentas de Orden Presupuestal</style>",
                        "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(util.formatearNumero("#,##0.00", (totalCuentaOrdenA[1][1] + totalCuentaOrdenA[2][1]))).concat("</style>"),
                        "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(util.formatearNumero("#,##0.00", (totalCuentaOrdenA[1][2] + totalCuentaOrdenA[2][2]))).concat("</style>"),
                        "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(util.formatearNumero("#,##0.00", (totalCuentaOrdenA[1][3] + totalCuentaOrdenA[2][3]))).concat("</style>"),
                        "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(
                        util.formatearNumero("0.00", (((totalCuentaOrdenA[1][2] + totalCuentaOrdenA[2][2]) - (totalCuentaOrdenA[1][1] + totalCuentaOrdenA[2][1])) != 0 ? ((totalCuentaOrdenA[1][2] + totalCuentaOrdenA[2][2]) != 0 ? ((((totalCuentaOrdenA[1][2] + totalCuentaOrdenA[2][2]) - (totalCuentaOrdenA[1][1] + totalCuentaOrdenA[2][1])) / (totalCuentaOrdenA[1][2] + totalCuentaOrdenA[2][2])) * 100) : (((totalCuentaOrdenA[1][2] + totalCuentaOrdenA[2][2]) - (totalCuentaOrdenA[1][1] + totalCuentaOrdenA[2][1])) > 0 ? 100.00 : -100.00)) : 0) * -1)).concat("</style>"),
                        false, true);
                listaRegistrosA.add(registroE);
                registroE = new RegistroEdoFinV2(
                        "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">Total Cuentas de Orden Presupuestal</style>",
                        "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(util.formatearNumero("#,##0.00", (totalCuentaOrdenB[1][1] + totalCuentaOrdenB[2][1]))).concat("</style>"),
                        "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(util.formatearNumero("#,##0.00", (totalCuentaOrdenB[1][2] + totalCuentaOrdenB[2][2]))).concat("</style>"),
                        "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(util.formatearNumero("#,##0.00", (totalCuentaOrdenB[1][3] + totalCuentaOrdenB[2][3]))).concat("</style>"),
                        "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(
                        util.formatearNumero("0.00", (((totalCuentaOrdenB[1][2] + totalCuentaOrdenB[2][2]) - (totalCuentaOrdenB[1][1] + totalCuentaOrdenB[2][1])) != 0 ? ((totalCuentaOrdenB[1][2] + totalCuentaOrdenB[2][2]) != 0 ? ((((totalCuentaOrdenB[1][2] + totalCuentaOrdenB[2][2]) - (totalCuentaOrdenB[1][1] + totalCuentaOrdenB[2][1])) / (totalCuentaOrdenB[1][2] + totalCuentaOrdenB[2][2])) * 100) : (((totalCuentaOrdenB[1][2] + totalCuentaOrdenB[2][2]) - (totalCuentaOrdenB[1][1] + totalCuentaOrdenB[2][1])) > 0 ? 100.00 : -100.00)) : 0) * -1)).concat("</style>"),
                        false, true);
                listaRegistrosB.add(registroE);
            }

            if (totalCuentaOrdenA[0][0] == 1 || totalCuentaOrdenA[1][0] == 1 || totalCuentaOrdenA[2][0] == 1) {
                registroE = new RegistroEdoFinV2(null, null, null, null, null, false, false);
                listaRegistrosA.add(registroE);
                listaRegistrosB.add(registroE);
                listaRegistrosA.add(registroE);
                listaRegistrosB.add(registroE);
                totalCuentaOrdenA[0][1] += totalCuentaOrdenA[1][1] + totalCuentaOrdenA[2][1];
                totalCuentaOrdenA[0][2] += totalCuentaOrdenA[1][2] + totalCuentaOrdenA[2][2];
                totalCuentaOrdenA[0][3] += totalCuentaOrdenA[1][3] + totalCuentaOrdenA[2][3];
                registroE = new RegistroEdoFinV2(
                        "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">TOTAL CUENTAS DE ORDEN</style>",
                        "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(util.formatearNumero("#,##0.00", totalCuentaOrdenA[0][1])).concat("</style>"),
                        "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(util.formatearNumero("#,##0.00", totalCuentaOrdenA[0][2])).concat("</style>"),
                        "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(util.formatearNumero("#,##0.00", totalCuentaOrdenA[0][3])).concat("</style>"),
                        "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(
                        util.formatearNumero("0.00", ((totalCuentaOrdenA[0][2] - totalCuentaOrdenA[0][1]) != 0 ? (totalCuentaOrdenA[0][2] != 0 ? (((totalCuentaOrdenA[0][2] - totalCuentaOrdenA[0][1]) / totalCuentaOrdenA[0][2]) * 100) : ((totalCuentaOrdenA[0][2] - totalCuentaOrdenA[0][1]) > 0 ? 100.00 : -100.00)) : 0) * -1)).concat("</style>"),
                        false, true);
                listaRegistrosA.add(registroE);
                totalCuentaOrdenB[0][1] += totalCuentaOrdenB[1][1] + totalCuentaOrdenB[2][1];
                totalCuentaOrdenB[0][2] += totalCuentaOrdenB[1][2] + totalCuentaOrdenB[2][2];
                totalCuentaOrdenB[0][3] += totalCuentaOrdenB[1][3] + totalCuentaOrdenB[2][3];
                registroE = new RegistroEdoFinV2(
                        "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">TOTAL CUENTAS DE ORDEN</style>",
                        "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(util.formatearNumero("#,##0.00", totalCuentaOrdenB[0][1])).concat("</style>"),
                        "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(util.formatearNumero("#,##0.00", totalCuentaOrdenB[0][2])).concat("</style>"),
                        "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(util.formatearNumero("#,##0.00", totalCuentaOrdenB[0][3])).concat("</style>"),
                        "<style pdfFontName=\"Helvetica-Bold\" isBold=\"true\">".concat(
                        util.formatearNumero("0.00", ((totalCuentaOrdenB[0][2] - totalCuentaOrdenB[0][1]) != 0 ? (totalCuentaOrdenB[0][2] != 0 ? (((totalCuentaOrdenB[0][2] - totalCuentaOrdenB[0][1]) / totalCuentaOrdenB[0][2]) * 100) : ((totalCuentaOrdenB[0][2] - totalCuentaOrdenB[0][1]) > 0 ? 100.00 : -100.00)) : 0) * -1)).concat("</style>"),
                        false, true);
                listaRegistrosB.add(registroE);
            }

            for (ajuste = 0; ajuste < listaRegistrosA.size(); ajuste++) {
                registroFinal = new RegistroReporteV2(
                        ((RegistroEdoFinV2) listaRegistrosA.get(ajuste)).getConcepto(), ((RegistroEdoFinV2) listaRegistrosA.get(ajuste)).getSaldoActual(), ((RegistroEdoFinV2) listaRegistrosA.get(ajuste)).getSaldoAnterior(), ((RegistroEdoFinV2) listaRegistrosA.get(ajuste)).getVariacion(), ((RegistroEdoFinV2) listaRegistrosA.get(ajuste)).getPorcentaje(), ((RegistroEdoFinV2) listaRegistrosA.get(ajuste)).isLineaSaldo(), ((RegistroEdoFinV2) listaRegistrosA.get(ajuste)).isLineaRenglon(),
                        ((RegistroEdoFinV2) listaRegistrosB.get(ajuste)).getConcepto(), ((RegistroEdoFinV2) listaRegistrosB.get(ajuste)).getSaldoActual(), ((RegistroEdoFinV2) listaRegistrosB.get(ajuste)).getSaldoAnterior(), ((RegistroEdoFinV2) listaRegistrosB.get(ajuste)).getVariacion(), ((RegistroEdoFinV2) listaRegistrosB.get(ajuste)).getPorcentaje(), ((RegistroEdoFinV2) listaRegistrosB.get(ajuste)).isLineaSaldo(), ((RegistroEdoFinV2) listaRegistrosB.get(ajuste)).isLineaRenglon(),
                        (emision == 1));
                listaRegistrosFinal.add(registroFinal);
            }

            getListaFinal().addAll(listaRegistrosFinal);
            setCount(listaRegistrosFinal.size());
            System.out.println(getListaFinal());

        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            if (stm != null) {
                stm.close();
            }
            if (rst != null) {
                rst.close();
            }
        }
    }
}