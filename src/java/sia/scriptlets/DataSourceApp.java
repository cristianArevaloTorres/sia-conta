/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of JasperReports.
 *
 * JasperReports is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JasperReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JasperReports. If not, see <http://www.gnu.org/licenses/>.
 */
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


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: DataSourceApp.java 4595 2011-09-08 15:55:10Z teodord $
 */
public class DataSourceApp 
{

    	public static void main(String[] args) throws JRException
	{
		// main(new DataSourceApp(), args);
		DataSourceApp app= new DataSourceApp();
		app.fill();
		app.pdf();
	}
	
	
	/**
	 *
	 */
	public void test() throws JRException
	{
		fill();
		pdf();
		xmlEmbed();
		xml();
		html();
		rtf();
		xls();
		jxl();
		csv();
		odt();
		//ods();
		//docx();
		//xlsx();
		//pptx();
		//xhtml();
	}


	/**
	 *
	 */
	public void fill() throws JRException
	{
	  long start = System.currentTimeMillis();
	  //Preparing parameters
	  Map parameters = new HashMap();
	  parameters.put("ReportTitle", "Address Report");
	  parameters.put("DataFile", "LibroDiarioPorOperacionDataSource.java");
	  parameters.put("PAGINA_INICIO",new Integer(156352));
	  parameters.put("PAGINAS_TOTALES",new Integer(169862));
	  Connection connection = null;
          try {//cambiar lineas de la 110 a la 112
            Class.forName("oracle.jdbc.driver.OracleDriver");
            //connection = DriverManager.getConnection("jdbc:oracle:thin:@10.1.8.207:1521:sia_pru","rf_contabilidad","c4nt1b3l3d1d");  
            connection = DriverManager.getConnection("jdbc:oracle:thin:@bdsiapro.senado.gob.mx:1521:sia","rf_contabilidad_arm","Quetzalcoatl2012");  
            //connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", ""); 			
            //JasperFillManager.fillReportToFile("D:/Plataforma/Netbeans/Ireport/JRDataSource/JRJava/src/reports/libroDiarioPorOperacionLineas.jasper", parameters, new LibroDiarioPorOperacionDataSource("select * from polizas", connection));
          //Compilar este jasper AVargas 20161202
            //  JasperFillManager.fillReportToFile("D:/Proyectos/Armonizado/siafmArm19Oct2012/SIAFM/public_html/contabilidad/estadosFinancieros/reportes/libros/libroDiarioPorOperacionLineas.jasper", parameters, new LibroDiarioPorOperacionDataSource(diarioPorOperacion().toString(), connection));
       	  } // try                                                                                                                                                                                         
	  catch(Exception e) {
            e.printStackTrace();
          } // catch
	  finally {
	    try {
	       if(connection!= null)
                connection.close();
	    }	
	    catch(Exception ex) {
              ex.printStackTrace();
            } // catch
	  }
	  System.err.println("Filling time : " + (System.currentTimeMillis() - start));
	}
	
	/**
	 *
	 */
	public void print() throws JRException
	{
		long start = System.currentTimeMillis();
		JasperPrintManager.printReport("D:/Proyectos/Armonizado/siafmArm19Oct2012/SIAFM/public_html/contabilidad/estadosFinancieros/reportes/libros/libroDiarioPorOperacionLineas.jrprint", true);
		System.err.println("Printing time : " + (System.currentTimeMillis() - start));
	}
	
	/**
	 *
	 */
	public void pdf() throws JRException
	{
		long start = System.currentTimeMillis();
		JasperExportManager.exportReportToPdfFile("D:/Proyectos/Armonizado/siafmArm19Oct2012/SIAFM/public_html/contabilidad/estadosFinancieros/reportes/libros/libroDiarioPorOperacionLineas.jrprint");
		System.err.println("PDF creation time : " + (System.currentTimeMillis() - start));
	}
	
	
	/**
	 *
	 */
	public void xml() throws JRException
	{
		long start = System.currentTimeMillis();
		JasperExportManager.exportReportToXmlFile("D:/ProyectoSIA/Libros/siafmArm19Oct2012/SIAFM/public_html/contabilidad/estadosFinancieros/reportes/libros/libroDiarioPorOperacionLineasSinFirmas.jrprint", false);
		System.err.println("XML creation time : " + (System.currentTimeMillis() - start));
	}
	
	
	/**
	 *
	 */
	public void xmlEmbed() throws JRException
	{
		long start = System.currentTimeMillis();
		JasperExportManager.exportReportToXmlFile("D:/ProyectoSIA/Libros/siafmArm19Oct2012/SIAFM/public_html/contabilidad/estadosFinancieros/reportes/libros/libroDiarioPorOperacionLineasSinFirmas.jrprint", true);
		System.err.println("XML creation time : " + (System.currentTimeMillis() - start));
	}
	
	
	/**
	 *
	 */
	public void html() throws JRException
	{
		long start = System.currentTimeMillis();
		JasperExportManager.exportReportToHtmlFile("D:/ProyectoSIA/Libros/siafmArm19Oct2012/SIAFM/public_html/contabilidad/estadosFinancieros/reportes/libros/libroDiarioPorOperacionLineasSinFirmas.jrprint");
		System.err.println("HTML creation time : " + (System.currentTimeMillis() - start));
	}
	
	
	/**
	 *
	 */
	public void rtf() throws JRException
	{
		long start = System.currentTimeMillis();
		File sourceFile = new File("D:/ProyectoSIA/Libros/siafmArm19Oct2012/SIAFM/public_html/contabilidad/estadosFinancieros/reportes/libros/libroDiarioPorOperacionLineasSinFirmas.jrprint");
		JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);
		File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".rtf");
		JRRtfExporter exporter = new JRRtfExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
		exporter.exportReport();
		System.err.println("RTF creation time : " + (System.currentTimeMillis() - start));
	}
	
	
	/**
	 *
	 */
	public void xls() throws JRException
	{
		long start = System.currentTimeMillis();
		File sourceFile = new File("D:/ProyectoSIA/Libros/siafmArm19Oct2012/SIAFM/public_html/contabilidad/estadosFinancieros/reportes/libros/libroDiarioPorOperacionLineasSinFirmas.jrprint");
		JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);
		File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".xls");
		JRXlsExporter exporter = new JRXlsExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
		exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
		exporter.exportReport();
		System.err.println("XLS creation time : " + (System.currentTimeMillis() - start));
	}
	
	
	/**
	 *
	 */
	public void jxl() throws JRException
	{
		long start = System.currentTimeMillis();
		File sourceFile = new File("D:/ProyectoSIA/Libros/siafmArm19Oct2012/SIAFM/public_html/contabilidad/estadosFinancieros/reportes/libros/libroDiarioPorOperacionLineasSinFirmas.jrprint");
		JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);
		File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".jxl.xls");
		JExcelApiExporter exporter = new JExcelApiExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
		exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
		exporter.exportReport();
		System.err.println("XLS creation time : " + (System.currentTimeMillis() - start));
	}
	
	
	/**
	 *
	 */
	public void csv() throws JRException
	{
		long start = System.currentTimeMillis();
		File sourceFile = new File("D:/ProyectoSIA/Libros/siafmArm19Oct2012/SIAFM/public_html/contabilidad/estadosFinancieros/reportes/libros/libroDiarioPorOperacionLineasSinFirmas.jrprint");
		JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);
		File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".csv");
		JRCsvExporter exporter = new JRCsvExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
		exporter.exportReport();
		System.err.println("CSV creation time : " + (System.currentTimeMillis() - start));
	}
	
	
	/**
	 *
	 */
	public void odt() throws JRException
	{
		long start = System.currentTimeMillis();
		File sourceFile = new File("D:/ProyectoSIA/Libros/siafmArm19Oct2012/SIAFM/public_html/contabilidad/estadosFinancieros/reportes/libros/libroDiarioPorOperacionLineasSinFirmas.jrprint");
		JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);
		File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".odt");
		JROdtExporter exporter = new JROdtExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
		exporter.exportReport();
		System.err.println("ODT creation time : " + (System.currentTimeMillis() - start));
	}
	
	
	
	
	 
	/**public void ods() throws JRException
	{
		long start = System.currentTimeMillis();
		File sourceFile = new File("D:/Proyectos/Armonizado/siafmArm19Oct2012/SIAFM/public_html/contabilidad/estadosFinancieros/reportes/libros/libroDiarioPorOperacionLineasSinFirmas.jrprint");
		JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);
		File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".ods");
		JROdsExporter exporter = new JROdsExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
		exporter.exportReport();
		System.err.println("ODS creation time : " + (System.currentTimeMillis() - start));
	}*/
	
	
	
	
	
	/**public void docx() throws JRException
	{
		long start = System.currentTimeMillis();
		File sourceFile = new File("D:/Proyectos/Armonizado/siafmArm19Oct2012/SIAFM/public_html/contabilidad/estadosFinancieros/reportes/libros/libroDiarioPorOperacionLineasSinFirmas.jrprint");
		JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);
		File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".docx");
		JRDocxExporter exporter = new JRDocxExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
		exporter.exportReport();
		System.err.println("DOCX creation time : " + (System.currentTimeMillis() - start));
	} */
	
	
	
	
	
	/**public void xlsx() throws JRException
	{
		long start = System.currentTimeMillis();
		File sourceFile = new File("D:/Proyectos/Armonizado/siafmArm19Oct2012/SIAFM/public_html/contabilidad/estadosFinancieros/reportes/libros/libroDiarioPorOperacionLineasSinFirmas.jrprint");
		JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);
		File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".xlsx");
		JRXlsxExporter exporter = new JRXlsxExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
		exporter.exportReport();
		System.err.println("XLSX creation time : " + (System.currentTimeMillis() - start));
	} */
	
	
	
	 
	/**public void pptx() throws JRException
	{
		long start = System.currentTimeMillis();
		File sourceFile = new File("D:/Proyectos/Armonizado/siafmArm19Oct2012/SIAFM/public_html/contabilidad/estadosFinancieros/reportes/libros/libroDiarioPorOperacionLineasSinFirmas.jrprint");
		JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);
		File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".pptx");
		JRPptxExporter exporter = new JRPptxExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
		exporter.exportReport();
		System.err.println("PPTX creation time : " + (System.currentTimeMillis() - start));
	} */
	
	
	
	
	
	/**public void xhtml() throws JRException
	{
		long start = System.currentTimeMillis();
		File sourceFile = new File("D:/Proyectos/Armonizado/siafmArm19Oct2012/SIAFM/public_html/contabilidad/estadosFinancieros/reportes/libros/libroDiarioPorOperacionLineasSinFirmas.jrprint");
		JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);
		File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".x.html");
		JRXhtmlExporter exporter = new JRXhtmlExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
		exporter.exportReport();
		System.err.println("XHTML creation time : " + (System.currentTimeMillis() - start));
	} */
	
	 public StringBuffer diarioPorOperacion() {
             StringBuffer sentencia = new StringBuffer();
             sentencia.append("select dp.fecha_afectacion,p.poliza_id, p.origen num_evento, p.unidad_ejecutora||'-'||LPAD(p.entidad,2,'0')||p.ambito||'-'||tp.abreviatura||'-'||LPAD(p.consecutivo,5,'0') num_asiento, "); 
             sentencia.append("decode(p.referencia,null,'VARIOS',p.referencia) documento, cla.cuenta_mayor, decode(substr(cla.cuenta_mayor,1,1),'8',1,decode(substr(cla.cuenta_mayor,1,1),'9',1,0) ) presupuestal, ");
             sentencia.append("cla.descripcion descripcion_cuenta, p.concepto descripcion, sum(decode(dp.operacion_contable_id,'0',dp.importe,0)) Debe, sum(decode(dp.operacion_contable_id,'1',dp.importe,0)) Haber ");
             sentencia.append("from rf_tr_detalle_poliza dp, Rf_Tr_Polizas p, rf_tr_cuentas_contables cc, rf_tc_clasificador_cuentas cla, rf_tc_tipos_polizas tp " );
             sentencia.append("where (p.idevento not in(301) or p.idevento is null ) and tp.tipo_poliza_id=p.tipo_poliza_id and dp.cuenta_contable_id=cc.cuenta_contable_id and cc.cuenta_mayor_id=cla.cuenta_mayor_id and cc.id_catalogo_cuenta=1 ");
             sentencia.append("and dp.fecha_afectacion>='16/09/2013' AND  dp.fecha_afectacion<='30/09/2013' and to_char(p.fecha, 'dd/MM/yyyy')=to_char(dp.fecha_afectacion,'dd/MM/yyyy')and p.id_catalogo_cuenta=1");
             sentencia.append("and p.id_catalogo_cuenta=cc.id_catalogo_cuenta and p.poliza_id=dp.poliza_id and dp.cuenta_contable_id=cc.cuenta_contable_id ");
             sentencia.append("group by dp.fecha_afectacion, p.poliza_id,p.origen, p.unidad_ejecutora||'-'||LPAD(p.entidad,2,'0')||p.ambito||'-'||tp.abreviatura||'-'||LPAD(p.consecutivo,5,'0'), ");
             sentencia.append("p.referencia, cla.cuenta_mayor, decode(substr(cla.cuenta_mayor,1,1),'8',1,decode(substr(cla.cuenta_mayor,1,1),'9',1,0) ), cla.descripcion, ");
             sentencia.append("p.concepto, decode(sign(dp.importe),0, -1,sign(dp.importe)) order by dp.fecha_afectacion, p.poliza_id");
             return sentencia;    
         
         }
	
}
