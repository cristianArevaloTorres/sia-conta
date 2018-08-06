package sia.libs.archivo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import sia.db.sql.Sentencias;
import sia.db.sql.Vista;
import sia.libs.formato.Error;

public class Csv extends BarraProgreso {

    public int conexion = 0;
    private String nombreReporte = null;
    private String nombreZip = null;
    private String ruta = null;
    private String tituloReporte = null;
    private Writer out;
    private int numeroEncabezado = 0;
    private List<String> rsmd = null;
    private String rutaCompleta;
    private String nombreArchivoCsv;

    public Csv() {
    }

    private String getNombreArchivoCSV() {
        if (nombreArchivoCsv == null) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddhhmmss");
            java.util.Date currentTime = new java.util.Date();
            this.setNombreZip(this.getNombreReporte() + "_" + formatter.format(currentTime));
            nombreArchivoCsv = (this.getNombreReporte() + "_" + formatter.format(currentTime) + ".csv");
        }
        return nombreArchivoCsv;
    }

    private void setNombreZip(String nombreZip) {
        this.nombreZip = nombreZip;
    }

    public String getNombreZip() {
        return nombreZip;
    }

    public String getRuta() {
        return ruta;
    }

    private void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getNombreReporte() {
        return nombreReporte;
    }

    private void setNombreReporte(String nombreReporte) {
        this.nombreReporte = nombreReporte;
    }

    public String getTituloReporte() {
        return tituloReporte;
    }

    public void setTituloReporte(String tituloReporte) {
        this.tituloReporte = tituloReporte;
    }

    public void setOut(Writer out) {
        this.out = new PrintWriter(out);
    }

    public void setConexion(int conexion) {
        this.conexion = conexion;
    }

    public int getConexion() {
        return conexion;
    }

    public void setNumeroEncabezado(int numeroEncabezado) {
        this.numeroEncabezado = numeroEncabezado;
    }

    public int getNumeroEncabezado() {
        return numeroEncabezado;
    }

    private List<Vista> getRegistros(String query, int conn) {
        Sentencias sentencias;
        List<Vista> regresa = null;
        try {
            sentencias = new Sentencias(conn);
            regresa = sentencias.registros(query);
            rsmd = sentencias.getResmd();
        } catch (Exception ex) {
            Error.mensaje(ex, "Tesoreria");
        }
        return regresa;
    }

    private void imprimirEncabezado(FileOutputStream archivo) throws SQLException,
            IOException {
        int numCols = rsmd.size();
        for (int i = 0; i < numCols; i++) {
            archivo.write(rsmd.get(i).concat(",").getBytes("ISO-8859-1"));
        }
        archivo.write("\r\n".getBytes("ISO-8859-1"));
    }

    private void imprimirRenglones(FileOutputStream archivo, List<Vista> registros, Writer out) throws SQLException,
            IOException {
        int numCols = rsmd.size();

        monitoreo(registros.size(), out);       // inicializar en 0 la barra progreso 
        String valor;
        for (int x = 0; x < registros.size(); x++) {
            for (int i = 0; i < numCols; i++) {
                valor = registros.get(x).getField(rsmd.get(i));
                valor = valor != null && valor.indexOf(",") > -1 ? "\"".concat(valor).concat("\"") : valor;
                archivo.write((valor == null ? "" : valor).concat(",").getBytes("ISO-8859-1"));
            }
            archivo.write("\r\n".getBytes("ISO-8859-1"));

            out.flush();                        // barra progreso
            this.progreso(x + 1);                   // barra progreso
        }
    }

    public boolean procesar(String ruta, String query, String nombreReporte,
            int conn, HttpServletRequest request, Writer out) throws Exception {
        setOut(out);       //barra progreso
        boolean regresa = false;
        List<Vista> registros;
        Zip zipFile = null;
        try {
            this.setNombreReporte(nombreReporte);
            this.setRuta(ruta);
            registros = getRegistros(query, conn);
            File archivo;
            FileOutputStream archivoOut;
            setRutaCompleta(request.getContextPath().concat("/").concat(this.getRuta() + "/" + getNombreArchivoCSV()));
            archivo = new File(request.getSession().getServletContext().getRealPath(this.getRuta() + "/" + getNombreArchivoCSV()));
            if (!(archivo.exists())) {
                if (archivo.isDirectory()) {
                    archivo.mkdirs();
                }
            }
            //archivo = new File(request.getSession().getServletContext().getRealPath(this.getRuta() + "/" + getNombreArchivoCSV()));
            archivoOut = new FileOutputStream(archivo);
            imprimirEncabezado(archivoOut);
            imprimirRenglones(archivoOut, registros, out);
            archivoOut.write("\r\n".getBytes("ISO-8859-1"));
            archivoOut.close();
            //if (getRecordCount() > 0) {
            // zipFile = new Zip();
            // zipFile.setDebug(false);
            // zipFile.setEliminar(true);
            // zipFile.compactar(request.getSession().getServletContext().getRealPath(this.getRuta()) + "/" + this.getNombreZip().concat(".zip"), request.getSession().getServletContext().getRealPath(this.getRuta()), this.getNombreZip().concat(".csv"));
            regresa = true;
            //}
            zipFile = null;
        } catch (Exception ex) {
            Error.mensaje(ex, "Tesoreria");
        } finally {
        }
        return regresa;
    }

    public void setRutaCompleta(String rutaCompleta) {
        this.rutaCompleta = rutaCompleta;
    }

    public String getRutaCompleta() {
        return rutaCompleta;
    }
}
