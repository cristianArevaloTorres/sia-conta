/*
 * Clase: Eliminar.java
 *
 * Creado: 4 de mayo de 2007, 08:20 PM
 *
 * Write by: alejandro.jimenez
 */
package sia.libs.archivo;

import java.io.File;
import sia.libs.formato.Error;

public class Eliminar {

    private boolean debug = false;

    /**
     * Creates a new instance of fileDirectory
     */
    public Eliminar() {
        setDebug(false);
    } // Eliminar

    private String remplazar(String cadena, String buscar, String sustituir) {
        String regresar = cadena;
        int posicion = 0;
        try {
            if (!sustituir.equalsIgnoreCase(buscar)) {
                do {
                    posicion = cadena.indexOf(buscar);
                    if (posicion >= 0) {
                        regresar =
                            cadena.substring(0, posicion) + sustituir + cadena.substring(posicion + buscar.length(), cadena.length());
                        cadena = regresar;
                    }// if
                } while (posicion >= 0);
            }
        } catch (Exception e) {
            System.err.println(Error.getMensaje(this, "SIAFM", "remplazar", e.getMessage()));
        }// try
        return regresar;
    }

    private boolean acceptFile(String patron, String name, boolean igual) {
        boolean comodin = patron.indexOf("?") >= 0;
        String empieza = null;
        String termina = null;
        if (comodin) {
            if (patron.indexOf("?") > 0) {
                empieza = patron.substring(0, patron.indexOf("?"));
            }
            if (patron.indexOf("?") < patron.length() - 1) {
                termina = patron.substring(patron.indexOf("?") + 1, patron.length());
            }
            comodin =
                (empieza != null ? name.startsWith(empieza) : true) && (termina != null ? name.endsWith(termina) : true);
        }// if comodin
        return igual ? name.equals(patron) || comodin : name.startsWith("xFile") && name.compareTo(patron) < 0;
    } // accept

    public void eliminar(String ruta, String patron) {
        File directorio = new File(ruta);
        String[] list = directorio.list();
        if (list != null && list.length > 0) {
            StringBuffer output = new StringBuffer();
            for (int i = 0; i < list.length; i++) {
                if (acceptFile(patron, list[i], false)) {
                    String commando = null;
                    try {
                        File eliminar = new File(remplazar(ruta, "\\", "/") + "/" + list[i]);
                        if (eliminar.exists()) {
                            eliminar.delete();
                        }
                        eliminar = null;
                        System.err.println(Error.getMensaje(this, "SIAFM", "eliminar", "Eliminado: " + list[i] + " os: " + System.getProperty("os.name")));
                    } catch (Exception e) {
                        System.err.println(Error.getMensaje(this, "SIAFM", "eliminar", e, "archivo: " + list[i]));
                    }// try
                    if (isDebug()) {
                        output.append("=> eliminado: true");
                    }
                    output.append("\n");
                }// if
            }// for
            list = null;
            directorio = null;
            if (isDebug()) {
                System.err.println(output.toString());
            }
        } else if (isDebug()) {
            System.err.println(Error.getMensaje(this, "SIAFM", "eliminar", "No existen archivos con ese patron: " + patron));
        }
    } // eliminar

    private boolean tieneNmonico(String patron, String name) {
        //boolean comodin = name.indexOf(patron) >= 0;
        //return comodin;
        return name.indexOf(patron) >= 0;
    } // accept

    public void eliminarNmonico(String ruta, String patron) {
        File directorio = new File(ruta);
        String[] list = directorio.list();
        if (list != null && list.length > 0) {
            StringBuffer output = new StringBuffer();
            for (int i = 0; i < list.length; i++) {
                if (tieneNmonico(patron, list[i])) {
                    String commando = null;
                    try {
                        File eliminar = new File(remplazar(ruta, "\\", "/") + "/" + list[i]);
                        if (eliminar.exists()) {
                            eliminar.delete();
                        }
                        eliminar = null;
                        System.err.println(Error.getMensaje(this, "SIAFM", "eliminar", "Eliminado: " + list[i] + " os: " + System.getProperty("os.name")));
                    } catch (Exception e) {
                        System.err.println(Error.getMensaje(this, "SIAFM", "eliminar", e, "archivo: " + list[i]));
                    }// try
                    if (isDebug()) {
                        output.append("=> eliminado: true");
                    }
                    output.append("\n");
                }// if
            }// for
            list = null;
            directorio = null;
            if (isDebug()) {
                System.err.println(output.toString());
            }
        } else if (isDebug()) {
            System.err.println(Error.getMensaje(this, "SIAFM", "eliminar", "No existen archivos con ese patron: " + patron));
        }
    } // eliminar

    public void eliminar(String ruta, String patron, boolean debug) {
        setDebug(debug);
        eliminar(ruta, patron);
    } // eliminar

    public String[] listaArchivosSeleccionados(String ruta, String patron) {
        return Patron.listaArchivosSeleccionados(ruta, patron);
    } // listaArchivosSeleccionados

    public void erase(String ruta, String patron) {
        File directorio = new File(ruta);
        String[] list = directorio.list(new FileNameFiltro(patron));
        File file = null;
        if (list != null && list.length > 0) {
            StringBuffer output = new StringBuffer();
            for (int i = 0; i < list.length; i++) {
                file = new File(ruta + list[i]);
                output.append(file.getName());
                output.append(" => es directorio: ");
                output.append(file.isDirectory());
                output.append(" => es archivo: ");
                output.append(file.isFile());
                try {
                    if (file.delete()) {
                        System.err.println(Error.getMensaje(this, "SIAFM", "erase", "Archivo eliminado: " + file.getName()));
                    }
                } catch (Exception e) {
                    System.err.println(Error.getMensaje(this, "SIAFM", "erase", "Error: " + e + " archivo: " + file.getName()));
                }// try
                if (isDebug()) {
                    output.append("=> eliminado: true");
                }
                output.append("\n");
            }// for
            list = null;
            directorio = null;
            if (isDebug()) {
                System.err.println(output.toString());
            }
        } else if (isDebug()) {
            System.err.println(Error.getMensaje(this, "SIAFM", "erase", "No existen archivos con ese patron: " + patron));
        }
    } // erase

    public void erase(String ruta, String patron, boolean debug) {
        setDebug(debug);
        erase(ruta, patron);
    } // erase

    public void depurar(String ruta, String patron) {
        File directorio = new File(ruta);
        File[] list = directorio.listFiles(new FileFiltro(patron));
        if (list != null) {
            StringBuffer output = new StringBuffer();
            for (int i = 0; i < list.length; i++) {
                output.append(list[i].getName());
                output.append(" => es directorio: ");
                output.append(list[i].isDirectory());
                output.append(" => es archivo: ");
                output.append(list[i].isFile());
                try {
                    if (list[i].delete()) {
                        System.out.println("Archivo eliminado: " + list[i].getName());
                    }
                } catch (Exception e) {
                    System.err.println(Error.getMensaje(this, "SIAFM", "depurar", "Error: " + e + " archivo: " + list[i].getName()));
                }// try
                if (isDebug()) {
                    output.append("=> eliminado: true");
                }
                output.append("\n");
            }// for
            list = null;
            directorio = null;
            if (isDebug()) {
                System.err.println(output.toString());
            }
        } else if (isDebug()) {
            System.err.println(Error.getMensaje(this, "SIAFM", "depurar", "No existen archivos con ese patron: " + patron));
        }
    } // depurar

    public void depurar(String ruta, String patron, boolean debug) {
        setDebug(debug);
        depurar(ruta, patron);
    } // depurar

    public void setDebug(boolean debug) {
        this.debug = debug;
    } // setDebug

    public static void eliminarTemporales(String ruta, String patron) {
        Eliminar eliminar = new Eliminar();
        eliminar.eliminar(ruta, patron, false);
    } //eliminarTemporales

    public static void main(String[] args) {
        Eliminar miClase = new Eliminar();
        miClase.eliminar("Humanos/Nomina/PagosXY/Dbfs/", "xSia20040513", true);
        /*
         String a= "x20040501121045_hola";
         String b= "x20040430121045_como";
         String c= "x20040502121045_estas";
         String d= "x20040502";
         System.out.println(a+ " "+ d+ " = "+ a.compareToIgnoreCase(d));
         System.out.println(b+ " "+ d+ " = "+ b.compareToIgnoreCase(d));
         System.out.println(c+ " "+ d+ " = "+ c.compareToIgnoreCase(d));
         System.out.println(d+ " "+ d+ " = "+ d.compareToIgnoreCase(d));
         */
    }

    public boolean isDebug() {
        return debug;
    }

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // The directory is now empty so delete it
        return dir.delete();
    }
}
