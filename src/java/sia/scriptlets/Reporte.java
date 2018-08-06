package sia.scriptlets;

import java.io.File;
import java.sql.Connection;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.commons.io.FilenameUtils;

public class Reporte {

    private static final String TASK_COMPILE = "compile";
    private static final String TASK_FILL = "fill";
    private static final String TASK_PRINT = "print";
    private static final String TASK_PDF = "pdf";
    private static final String TASK_RTF = "rtf";
    private static final String TASK_XML = "xml";
    private static final String TASK_XML_EMBED = "xmlEmbed";
    private static final String TASK_HTML = "html";
    private static final String TASK_XLS = "xls";
    private static final String TASK_CSV = "csv";
    private static final String TASK_RUN = "run";    
    private Map parameters = null;
    private String source = null;
    private String target = null;
    private String extension = null;
    private Connection connection = null;
    private boolean compile = false;
    private boolean correcto = false;
    private boolean debug = false;
    private String _vtipoExtencion = null;

    public Reporte() {
        this(null, "", null, null);
    }

    public Reporte(Connection conexion, String fuente, String destino, Map parametros) {
        setConnection(conexion);
        setSource(fuente);
        setTarget(destino);
        setParameter(parametros);
    }

    public void setParameter(Map parameters) {
        this.parameters = parameters;
    } // setparameters

    public void setSource(String source) {
        String tmp = source.replace("\\", "/");
        this.source = tmp;
    } // setSource

    public void setTarget(String target) {
        this.target = target;
    } // setTarget

    public String getTarget() {
        return getTarget();
    } // getTarget

    public void setConnection(Connection connection) {
        this.connection = connection;
    } // setConnection

    public void setCompile(boolean compile) {
        this.compile = compile;
    } // setCompile

    public boolean procesar(String typeExport, boolean compile) {
        setExtension(typeExport);
        if (compile) {
            setCompile(compile);
        }
          System.out.println("reporte.procesar-1--");
        return procesar(typeExport);
    } // procesar

    public byte[] procesarMemoria(boolean compile) throws JRException {
        if (compile) {
            proceso("compile");
        }
        return JasperExportManager.exportReportToPdf(JasperFillManager.fillReport(source + ".jasper", parameters,
                connection));
    } // procesarMemoria

    public boolean procesar(String typeExport) {
        setExtension(typeExport);
        if (compile) {
            proceso("compile");
        }
        proceso("fill");
//    System.out.println("reporte.procesar--2--");
        return proceso(typeExport);
    } // setProcesar

    private boolean proceso(String taskName) {
//System.out.println("Entra a reporte.java -proceso: " + taskName);
        correcto = false;
        _vtipoExtencion = null;
        if (target == null || target.length() == 0) {
            target = source;
        }

        //Si no existe la carpeta donde se va a generar el archivo, la crea
        File rutaArchivo = new File(target.substring(0, source.lastIndexOf("/")));
        if (!(rutaArchivo.exists())) {
            rutaArchivo.mkdirs();
        }
        System.out.println("taskName: " + taskName);
        try 
        {
            _vtipoExtencion = FilenameUtils.getExtension(source); 
            
            long start = System.currentTimeMillis();
            
            
            if (TASK_COMPILE.equals(taskName)) 
            {
//          System.out.println("source de compile: " + source);
                JasperCompileManager.compileReportToFile(source + ".jrxml"); //era .xml
                if (debug) 
                {
                    System.out.println("Compile-tiempo: " + (System.currentTimeMillis() - start));
                }
            } else if (TASK_FILL.equals(taskName)) {
                System.out.println("source: " + source);
                System.out.println("Parámetros para hacer el fill: " + parameters);
                try {
                 //   JasperCompileManager.compileReportToFile(source + ".jrxml"); //era .xml
                    //JasperCompileManager.compileReportToFile("C:/Pro/sia-conta/build/web/contabilidad/registroContableNuevo/reportes/chequeUnidades.jrxml"); //era .xml
                    //JasperCompileManager.compileReportToFile("C:/Pro/sia-conta/web/contabilidad/registroContableNuevo/reportes/cheque_bancomer.jrxml"); //era .xml
                    JasperFillManager.fillReportToFile(source + ".jasper", source + ".jrprint", parameters, connection);
                } catch (Throwable e) {
                    System.out.println("Error en TASK_FILL reporte.java@140: " + e.getClass().getName() + ": " + e.getMessage());
                }
                if (debug) {
                    System.out.println("Filling-tiempo: " + (System.currentTimeMillis() - start));
                }
            } else if (TASK_PRINT.equals(taskName)) {
                JasperPrintManager.printReport(source + ".jrprint", true);
                if (debug) {
                    System.out.println("Printing -tiempo: " + (System.currentTimeMillis() - start));
                }
            } else if (TASK_PDF.equals(taskName)) {
                setSource(source);
                JasperExportManager.exportReportToPdfFile(source + ".jrprint", target + ".pdf");
                if (debug) {
                    System.out.println("PDF-Tiempo de creacion: " + (System.currentTimeMillis() - start));
                }
            } else if (TASK_RTF.equals(taskName)) {
                File sourceFile = new File(source + ".jrprint");
                JasperPrint jasperPrint = (JasperPrint) JRLoader.loadObject(sourceFile);
                File destFile = new File(sourceFile.getParent(), target + ".rtf");
                JRRtfExporter exporter = new JRRtfExporter();
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, target + ".rtf");
                exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
                exporter.exportReport();
                if (debug) {
                    System.out.println("RTF creation time : " + (System.currentTimeMillis() - start));
                }
            } else if (TASK_XML.equals(taskName)) {
                JasperExportManager.exportReportToXmlFile(source + ".jrprint", target + ".xml", false);
                if (debug) {
                    System.out.println("XML-Tiempo de creacion: " + (System.currentTimeMillis() - start));
                }
            } else if (TASK_XML_EMBED.equals(taskName)) {
                JasperExportManager.exportReportToXmlFile(source + ".jrprint", target + ".xmlEmbed", true);
                if (debug) {
                    System.out.println("XML-Tiempo de creacion: " + (System.currentTimeMillis() - start));
                }
            } else if (TASK_HTML.equals(taskName)) {
                JasperExportManager.exportReportToHtmlFile(source + ".jrprint", target + ".html");
                if (debug) {
                    System.out.println("HTML-Tiempo de creacion: " + (System.currentTimeMillis() - start));
                }
            } else if (TASK_XLS.equals(taskName)) {
                File sourceFile = new File(source + ".jrprint");
                JasperPrint jasperPrint = (JasperPrint) JRLoader.loadObject(sourceFile);
                File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".xls");
                JRXlsExporter exporter = new JRXlsExporter();
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, target + ".xls");
                exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
                exporter.exportReport();
                if (debug) {
                    System.out.println("XLS-Tiempo de creacion: " + (System.currentTimeMillis() - start));
                }
            } else if (TASK_CSV.equals(taskName)) {
                File sourceFile = new File(source + ".jrprint");
                JasperPrint jasperPrint = (JasperPrint) JRLoader.loadObject(sourceFile);
                File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".csv");
                JRCsvExporter exporter = new JRCsvExporter();
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, target + ".csv");
                exporter.exportReport();
                if (debug) {
                    System.out.println("CSV-Tiempo de creacion: " + (System.currentTimeMillis() - start));
                }
            } else if (TASK_RUN.equals(taskName)) {
                //Preparing parameters
                JasperRunManager.runReportToPdfFile(source + ".jrprint", parameters, connection);
                if (debug) {
                    System.out.println("PDF running-tiempo: " + (System.currentTimeMillis() - start));
                }
            } // if
            correcto = true;
        } catch (Exception e) {
            //Error.mensaje(this, e, "SIAFM", "proceso", e.getMessage());
            e.printStackTrace(System.err);
        }
        return correcto;
    } // proceso

    private static void usage() {
        System.out.println("scriptletBean uso:");
        System.out.println("\tjava scriptletBean -Ttask -Ffile");
        System.out.println("\tTarea: compile | fill | print | pdf | rtf | xml | xmlEmbed | html | xls | csv | run");
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    } // setDebug

    public java.lang.String getExtension() {
        return "." + extension;
    } // getExtension

    public void setExtension(java.lang.String extension) {
        this.extension = extension;
    } // setExtension

    public static void main(String[] args) {
        String source = null;
        String taskName = null;
        if (args.length == 0) {
            usage();
            return;
        }
        int k = 0;
        while (args.length > k) {
            if (args[k].startsWith("-T")) {
                taskName = args[k].substring(2);
            }
            if (args[k].startsWith("-F")) {
                source = args[k].substring(2);
            }
            k++;
        }
    }
} // Reporte
