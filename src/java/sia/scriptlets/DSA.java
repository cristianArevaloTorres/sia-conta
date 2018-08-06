/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sia.scriptlets;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
//import net.sf.jasperreports.engine.export.JRXhtmlExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
//import net.sf.jasperreports.engine.export.oasis.JROdsExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import java.sql.Connection;
//import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
//import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;
//import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;

import net.sf.jasperreports.engine.util.JRLoader;

import sia.db.dao.DaoFactory;

import sia.rf.contabilidad.reportes.estadosFinancieros.LDPODS;

/**
 *
 * @author EST.JUAN.GALINDO
 */
public class DSA {

    private String fuente;
    private String destino;
    
    private String fechaAfectacionIzq;
    private String fechaAfectacionDer;
    
    private LDPODS libroDiarioPorOperacion;
    
    public String getFechaAfectacionIzq() {
        return fechaAfectacionIzq;
    }

    public void setFechaAfectacionIzq(String fechaAfectacionIzq) {
        this.fechaAfectacionIzq = fechaAfectacionIzq;
    }

    public String getFechaAfectacionDer() {
        return fechaAfectacionDer;
    }

    public void setFechaAfectacionDer(String fechaAfectacionDer) {
        this.fechaAfectacionDer = fechaAfectacionDer;
    }

    public String getFuente() {
        return fuente;
    }

    public void setFuente(String fuente) {
        this.fuente = fuente;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public void fill(Map parameters) throws JRException {
        long start = System.currentTimeMillis();

        parameters.put("ReportTitle", "Address Report");
        parameters.put("DataFile", "LDPODS.java");
        parameters.put("PAGINA_INICIO", new Integer(1));
        parameters.put("PAGINAS_TOTALES", new Integer(999));
        //parameters.put("SIA_RECORD_COUNT", 100000);
        
        Connection conexion = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            
            //connection = DriverManager.getConnection("jdbc:oracle:thin:@10.1.8.207:1521:sia_tra", "rf_conta_proc", "rf_conta_proc");
            //connection = DriverManager.getConnection("jdbc:oracle:thin:@172.16.1.70:1521:ora10g", "rf_conta_proc", "Kw35rn8f4A");
            conexion = sia.db.dao.DaoFactory.getConnection(DaoFactory.CONEXION_CONTABILIDAD);
            
            //JasperFillManager.fillReportToFile("D:/Proyecto - Procudaria Agraria/Check-out-TOMCATVersion20062013/ContaProcu/web/contabilidad/registroContableNuevo/estadosFinancieros/reportes/libros/libroDiarioPorOperacionLineasSinFirmas.jasper", parameters, new LDPODS(diarioPorOperacion().toString(), connection));
            //JasperFillManager.fillReportToFile(getFuente() + ".jasper", parameters, new LDPODS(diarioPorOperacion().toString(), connection));
            
            libroDiarioPorOperacion = new LDPODS(diarioPorOperacion().toString(), conexion);
            JasperFillManager.fillReportToFile(getFuente() + ".jasper", parameters, libroDiarioPorOperacion);
            
            //JasperFillManager.
            libroDiarioPorOperacion = null;
        
        } // try                                                                                                                                                                                         
        catch (Exception e) {
            e.printStackTrace();
        } // catch
        finally {
            try {
                if (conexion != null) {
                    conexion.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } // catch
        }
        System.err.println("Filling time : " + (System.currentTimeMillis() - start));
    }

    public void pdf() throws JRException {
        long start = System.currentTimeMillis();
        //JasperExportManager.exportReportToPdfFile("D:/Proyecto - Procudaria Agraria/Check-out-TOMCATVersion20062013/ContaProcu/web/contabilidad/registroContableNuevo/estadosFinancieros/reportes/libros/libroDiarioPorOperacionLineasSinFirmas.jrprint");
        JasperExportManager.exportReportToPdfFile(getFuente() + ".jrprint", getDestino() + ".pdf");
        System.err.println("PDF creation time : " + (System.currentTimeMillis() - start));
    }

    public StringBuffer diarioPorOperacion() {
        StringBuffer sentencia = new StringBuffer();
        sentencia.append("select dp.fecha_afectacion,p.poliza_id, p.origen num_evento, p.unidad_ejecutora||'-'||LPAD(p.entidad,2,'0')||p.ambito||'-'||tp.abreviatura||'-'||LPAD(p.consecutivo,5,'0') num_asiento, ");
        sentencia.append("decode(p.referencia,null,'VARIOS',p.referencia) documento, cla.cuenta_mayor, decode(substr(cla.cuenta_mayor,1,1),'8',1,decode(substr(cla.cuenta_mayor,1,1),'9',1,0) ) presupuestal, ");
        sentencia.append("cla.descripcion descripcion_cuenta, p.concepto descripcion, sum(decode(dp.operacion_contable_id,'0',dp.importe,0)) Debe, sum(decode(dp.operacion_contable_id,'1',dp.importe,0)) Haber ");
        sentencia.append("from rf_tr_detalle_poliza dp, Rf_Tr_Polizas p, rf_tr_cuentas_contables cc, rf_tc_clasificador_cuentas cla, rf_tc_tipos_polizas tp ");
        sentencia.append("where (p.idevento not in(301) or p.idevento is null ) and tp.tipo_poliza_id=p.tipo_poliza_id and dp.cuenta_contable_id=cc.cuenta_contable_id and cc.cuenta_mayor_id=cla.cuenta_mayor_id and cc.id_catalogo_cuenta=1 ");
        //sentencia.append("and dp.fecha_afectacion>='16/09/2013' AND  dp.fecha_afectacion<='30/09/2013' and to_char(p.fecha, 'dd/MM/yyyy')=to_char(dp.fecha_afectacion,'dd/MM/yyyy')and p.id_catalogo_cuenta=1");
        sentencia.append("and dp.fecha_afectacion>='" + getFechaAfectacionIzq() + "' AND  dp.fecha_afectacion <= '"+ getFechaAfectacionDer() +"' and to_char(p.fecha, 'dd/MM/yyyy')=to_char(dp.fecha_afectacion,'dd/MM/yyyy')and p.id_catalogo_cuenta=1");
        sentencia.append("and p.id_catalogo_cuenta=cc.id_catalogo_cuenta and p.poliza_id=dp.poliza_id and dp.cuenta_contable_id=cc.cuenta_contable_id ");
        sentencia.append("group by dp.fecha_afectacion, p.poliza_id,p.origen, p.unidad_ejecutora||'-'||LPAD(p.entidad,2,'0')||p.ambito||'-'||tp.abreviatura||'-'||LPAD(p.consecutivo,5,'0'), ");
        sentencia.append("p.referencia, cla.cuenta_mayor, decode(substr(cla.cuenta_mayor,1,1),'8',1,decode(substr(cla.cuenta_mayor,1,1),'9',1,0) ), cla.descripcion, ");
        sentencia.append("p.concepto, decode(sign(dp.importe),0, -1,sign(dp.importe)) order by dp.fecha_afectacion, p.poliza_id");
        return sentencia;

    }
}
