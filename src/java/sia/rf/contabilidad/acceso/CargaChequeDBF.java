package sia.rf.contabilidad.acceso;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import sia.rf.contabilidad.registroContableNuevo.bcCargaCheque;

public class CargaChequeDBF {

    private bcCargaCheque pbCargaCheque;
    public final int COLUMNA_FECHA_CHEQUE = 0;
    public final int COLUMNA_CUENTA_BANCARIA = 1;
    public final int COLUMNA_OPERACION_TIPO = 2;
    public final int COLUMNA_IMPORTE = 3;
    public final int COLUMNA_CONCEPTO = 4;
    public final int COLUMNA_BENEFICIARIO = 5;
    public final int COLUMNA_OPERACION_PAGO = 6;
    public final int COLUMNA_OPERACION_PAGO_SUP = 7;

    public CargaChequeDBF() {
        pbCargaCheque = new bcCargaCheque();
    }

    public String procesa(Connection conexion, String numEmpleado, String lsUnidad, String lsAmbito, String lsRuta, int ejercicio) throws Exception, SQLException {
        String dbUnidad = lsUnidad;
        String dbAmbito = lsAmbito;
        String dbFechaCheque;
        String dbCuentaBancaria;
        String dbOperacionTipo;
        String dbImporte;
        String dbConcepto;
        String dbBeneficiario;
        String dbOperacionPago;
        String dbOperacionPagoSup;
        String dbOrigenOperacion;

        String dbNumEmpleado = numEmpleado;
        int totalCheques = 0;
        String lsChequeId;
        String resultado = "";
        String error = "";

        File archivo;
        String nombre;
        long tamanio = 0;
        String archivoXls = lsRuta;   // "c:/datos/dbf/cheques.xls";
        Workbook woorkBook;
        Sheet sheet;
        int totalFilas;
        int totalColumnas;
        Cell[] filax;
        int fila = 1;

        try {
            archivo = new File(archivoXls);
            if (!archivo.exists()) {
                throw new Exception("-El archivo " + archivoXls + " no existe.");
            }
            nombre = archivo.getName();
            tamanio = archivo.length();
            woorkBook = Workbook.getWorkbook(archivo);
            sheet = woorkBook.getSheet(0);
            totalFilas = sheet.getRows();
            totalColumnas = sheet.getColumns();
            if (totalFilas > 0) {
                System.out.println("El archivo " + nombre + "contiene " + totalFilas + "registros.");
            }
            while (fila < totalFilas) {
                filax = sheet.getRow(fila);
                dbFechaCheque = filax[COLUMNA_FECHA_CHEQUE].getContents();
                if (dbFechaCheque.toUpperCase().equals("FIN")) {
                    break;
                }
                dbCuentaBancaria = filax[COLUMNA_CUENTA_BANCARIA].getContents();
                dbOperacionTipo = filax[COLUMNA_OPERACION_TIPO].getContents();
                dbImporte = filax[COLUMNA_IMPORTE].getContents();
                dbConcepto = filax[COLUMNA_CONCEPTO].getContents();
                dbBeneficiario = filax[COLUMNA_BENEFICIARIO].getContents();
                dbOperacionPago = filax[COLUMNA_OPERACION_PAGO].getContents().trim().toUpperCase();
                dbOperacionPago = dbOperacionPago.startsWith("SIN") ? "" : dbOperacionPago;
                dbOperacionPagoSup = filax[COLUMNA_OPERACION_PAGO_SUP].getContents().trim().toUpperCase();
                dbOperacionPagoSup = dbOperacionPagoSup.startsWith("SIN") ? "" : dbOperacionPagoSup;

                if (dbOperacionPago.length() == 14 && dbOperacionPagoSup.length() == 14) {
                    if (Integer.parseInt(dbOperacionPago.substring(9, 14)) > Integer.parseInt(dbOperacionPagoSup.substring(9, 14))) {
                        error = error.concat("<tr><td>El valor de la columna <b>OPERACION_PAGO</b> no debe ser mayor a la columna <b>OPERACION_PAGO_SUP</b></tr></td>");
                    }
                }
                lsChequeId = pbCargaCheque.select_SEQ_rf_tr_CargaCheque(conexion);
                pbCargaCheque.setConsecutivocheques(lsChequeId);
                pbCargaCheque.setUnidad(dbUnidad);
                pbCargaCheque.setAmbito(dbAmbito);
                if ((dbFechaCheque.replaceAll(" ", "")).length() < 10) {
                    error = error.concat("<tr><td>El formato de la columna <b>FECHA_CHEQUE</b> es incorrecto <b>[" + dbFechaCheque.replaceAll(" ", "") + "]</b>, el formato correcto debe ser <b>[01/01/2016]</b>.\n</tr></td>");
                } else {
                    pbCargaCheque.setFechacheque(dbFechaCheque);
                }
                    pbCargaCheque.setCuentabancaria(dbCuentaBancaria);
/*                if ((dbCuentaBancaria.replaceAll(" ", "")).length() != 11) {
                    error = error.concat("<tr><td>La <b>CUENTA_BANCARIA</b> debe contener 11 d�gitos.\n</tr></td>");
                } else {
                    pbCargaCheque.setCuentabancaria(dbCuentaBancaria);
                }*/
                if ((dbOperacionTipo.replaceAll(" ", "")).length() != 2) {
                    error = error.concat("<tr><td>La columna de <b>OPERACION_TIPO</b> debe contener 2 d�gitos");
                } else {
                    if (dbOperacionTipo.equals("99") || dbOperacionTipo.equals("00")) {
                        error = error.concat("<tr><td>La columna de <b>OPERACION_TIPO</b> debe ser diferente de <b>00</b> y <b>99</b> ya que para la carga de cheques no esta permitida, la operaci�n <b>99</b> esta reservada para p�lizas manuales.</tr></td>");
                    } else {
                        pbCargaCheque.setOperaciontipo(dbOperacionTipo);
                    }
                }
                /*if(dbOperacionTipo.equals("99"))  
                 //throw new Exception(" \nOPERACION_TIPO 99 en carga de cheques no es permitida, dicha operaci�n esta reservada para polizas manuales.\n" );  
                 error=error.concat("En la columna OPERACION_TIPO el valor 99 en carga de cheques no es permitida, dicha operaci�n esta reservada para polizas manuales.\n");
                 else  
                 pbCargaCheque.setOperaciontipo(dbOperacionTipo);    */
                if ((dbImporte.replaceAll(" ", "")).length() == 0) { //&& Double.parseDouble(dbImporte)==0.00)  
                    //throw new Exception(" \nIMPORTE con formato incorrecto o con valor 0.00.\n" );  
                    error = error.concat("<tr><td>La columna <b>IMPORTE</b> el valor debe de ser diferente de nulo.\n</tr></td>");
                } else {
                    if (Double.parseDouble(dbImporte) == 0.00) {
                        error = error.concat("<tr><td>La columna <b>IMPORTE</b> el valor debe de ser mayor que <b>0.00</b> \n</tr></td>");
                    } else {
                        pbCargaCheque.setImporte(dbImporte);
                    }
                }
                if ((dbConcepto.replaceAll(" ", "")).length() == 0) {
                    error = error.concat("<tr><td>En la columna <b>CONCEPTO</b> el valor debe de ser diferente de nulo.\n</tr></td>");
                } else {
                    pbCargaCheque.setConcepto(dbConcepto);
                }
                if ((dbBeneficiario.replaceAll(" ", "")).length() == 0) {
                    error = error.concat("<tr><td>En la columna de <b>BENEFICIARIO</b> el valor debe de ser diferente de nulo.\n</tr></td>");
                } else {
                    pbCargaCheque.setBeneficiario(dbBeneficiario);
                }
                if (!error.isEmpty()) {
                    throw new Exception("<br><table>" + error + "</table>");
                }
                pbCargaCheque.setEstatus("0");
                pbCargaCheque.setNumempleado(dbNumEmpleado);
                pbCargaCheque.setMensaje("C");
                pbCargaCheque.setOperacion_pago(dbOperacionPago);
                pbCargaCheque.setOperacion_pago_sup(dbOperacionPagoSup);
                if (dbOperacionPago.isEmpty()) {
                    dbOrigenOperacion = "0";
                } else {
                    dbOrigenOperacion = "0";
                 //   dbOrigenOperacion = pbCargaCheque.select_origen_rf_presupuesto_s2_rf_tr_operaciones_cheques_CargaCheque(conexion, dbOperacionPago.substring(0, 9), dbOperacionPago, dbOperacionPagoSup, dbUnidad, ejercicio);
                   }
                pbCargaCheque.setOrigen_operacion(dbOrigenOperacion);
                pbCargaCheque.insert_RF_TR_CHEQUES_CARGA(conexion);

                fila++;
                totalCheques++;
            }
            resultado = "Total de cheques cargados: " + totalCheques;
            System.out.println("Total de cheques cargado: " + totalCheques);
        } catch (Exception e) {
            resultado = "Total de cheques cargados: " + 0;
            //resultado = "\n Favor de verificar el ERROR que se ha detectado en el archivo de carga en la FILA "+ (++fila)  +". \n".concat(error);
            System.out.println("Error en metodo procesa de carga de cheques. " + e.getMessage());
            throw new Exception("Verificar el ERROR que se ha detectado en el archivo de carga en la FILA " + (++fila) + ". \n" + e.getMessage());
            //throw e;
        } finally {
            System.out.println("Termino proceso de carga.");
        }
        return resultado;
    }

    public void setPbCargaCheque(bcCargaCheque pbCargaCheque) {
        this.pbCargaCheque = pbCargaCheque;
    }

    public bcCargaCheque getPbCargaCheque() {
        return pbCargaCheque;
    }
}
