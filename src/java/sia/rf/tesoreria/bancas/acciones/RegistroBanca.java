package sia.rf.tesoreria.bancas.acciones;

import java.util.Iterator;
import java.util.ListResourceBundle;
import java.util.ResourceBundle;
import java.util.Vector;

public class RegistroBanca {

    private Vector nombresColumnas;
    private Vector longitudesColumnas;
    private Vector tipoDatos;
    private Vector valores;
    private Vector validacion;
    private String nombre;
    private String seccion;
    private String separador;
    private int columnas;
    private boolean correcto;
    private final String NOMBRE_DEFAULT = "loyoutTesoreria";
    private final String SECCION_DEFAULT = "movBanamex";

    public RegistroBanca(String nombre, String seccion) {
        nombresColumnas = new Vector();
        longitudesColumnas = new Vector();
        tipoDatos = new Vector();
        valores = new Vector();
        validacion = new Vector();
        setNombre(nombre);
        setSeccion(seccion);
        setSeparador(separador);
        setColumnas(0);
        setCorrecto(true);
        procesar();
    }

    /* public RegistroBanca() {
     this(NOMBRE_DEFAULT, SECCION_DEFAULT);
     }
     */
    private int getEntero(String valor) {
        int entero = 0;
        try {
            entero = Integer.parseInt(valor);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entero;
    } // getEntero

    private void procesar() {
        try {
            ResourceBundle propiedades = ListResourceBundle.getBundle(getNombre());
            setColumnas(getEntero(propiedades.getString(getSeccion() + ".columnas")));
            setSeparador(propiedades.getString(getSeccion() + ".separador"));
            for (int x = 0; x < getColumnas(); x++) {
                nombresColumnas.add(propiedades.getString(getSeccion() + ".nombre." + x));
                longitudesColumnas.add(new Integer(getEntero(propiedades.getString(getSeccion() + ".longitud." + x))));
                tipoDatos.add((propiedades.getString(getSeccion() + ".formato." + x)));
                validacion.add((propiedades.getString(getSeccion() + ".criterio." + x)));
            } // for x
        } catch (Exception e) {
            e.printStackTrace();
        } // try
    } // procesar

    public boolean tokens(String tokens) {
        if (getSeparador().equals("0")) {
            movimientos(tokens);
        }
        return isCorrecto();
    } // tokens

    public boolean formatoValido(String t, String formato) {
        boolean regresa = true;
        if (getSeccion().equals("movimientos")) {
            t = t.replace("-", "0");
        }
        if (formato.equals("numerico") || formato.equals("fecha")) {
            for (int i = 0; i < t.length() && regresa; i++) {
                regresa = regresa && (t.charAt(i) >= '0' && t.charAt(i) <= '9');
            }
        }
        return regresa;
    }

    public void movimientos(String tokens) {
        int contador = 0;
        valores.clear();
        Iterator iterator = getLongitudesColumnas();
        Iterator iteratorValidacion = getValidacion();
        Iterator itFormato = getTipoDatos();
        try {
            while (iterator.hasNext() && isCorrecto()) {
                int longitud = ((Integer) iterator.next()).intValue();
                String valida = ((String) iteratorValidacion.next());
                String formato = (String) itFormato.next();
                String s = tokens.substring(contador, contador + longitud);
                if (s.length() == longitud && formatoValido(s.trim(), formato)) {
                    if (getSeccion().equals("movSantander")) {
                        if (valida.equals("1")) {
                            s = validaNumPunto2(s);
                        }
                    }
                    if (getSeccion().equals("movimientos")) {
                        if (valida.equals("1")) {
                            s = s.replace("-", "0");
                            s = validaNumPunto2(s);
                        }
                    }
                    valores.add(s.trim());
                } else {
                    setCorrecto(false);
                }
                contador += longitud;
            } // while iterator  
        } catch (Exception e) {
            e.printStackTrace();
            setCorrecto(false);
        }// try
    }

    public String validaNumPunto2(String tokens) {
        StringBuffer res = new StringBuffer();
        res.append(tokens.substring(0, tokens.length() - 2));
        res.append(".");
        res.append(tokens.substring(tokens.length() - 2));
        return res.toString();
    } // valida

    public void finalized() {
        nombresColumnas.clear();
        nombresColumnas = null;
        longitudesColumnas.clear();
        longitudesColumnas = null;
        tipoDatos.clear();
        tipoDatos = null;
        valores.clear();
        valores = null;
        validacion.clear();
        validacion = null;
    }

    public boolean movimientosBBVA(String tokens) {
        valores.clear();
        Double monto = 0D;
        String[] datos = null;
        datos = tokens.split("\t");
        try {
            valores.add("'".concat(datos[0].trim()).concat("'"));
            nombresColumnas.add("num_cuenta");
            valores.add("to_date('".concat(datos[1].trim()).concat("','dd/mm/yyyy')"));
            nombresColumnas.add("fecha_hora");
            valores.add("'".concat(datos[2].trim()).concat("'"));
            nombresColumnas.add("clave_trans");
            valores.add("'".concat(datos[3].trim()).concat("'"));
            nombresColumnas.add("descripcion");
            valores.add("'".concat(datos[4].trim()).concat("'"));
            nombresColumnas.add("referencia");
            if (!datos[5].equals(null) && !datos[5].equals("")) {
                monto = Double.parseDouble(datos[5].trim()) * -1;
                valores.add(monto.toString());
                nombresColumnas.add("monto");
                valores.add("'DR'");
                nombresColumnas.add("id_tipo_trans");
            } else if (!datos[6].equals(null) && !datos[6].equals("")) {
                valores.add(datos[6].trim());
                nombresColumnas.add("monto");
                valores.add("'CR'");
                nombresColumnas.add("id_tipo_trans");
            }
            valores.add(datos[7].trim());
            nombresColumnas.add("saldo");
            valores.add("'CR'");
            nombresColumnas.add("tipo_saldo");
            setCorrecto(true);
        } catch (Exception e) {
            e.printStackTrace();
            setCorrecto(false);
        }
        return isCorrecto();
    }

    public boolean movimientosMultiva(String tokens) {
        valores.clear();
        String[] datos = null;
        datos = tokens.split("\t");
        try {
            valores.add("'".concat(datos[0].trim()).concat("'"));
            nombresColumnas.add("num_cuenta");
            valores.add("to_date('".concat(datos[1].trim().substring(0, 19)).concat("','dd/mm/yyyy HH24:mi:ss')"));
            nombresColumnas.add("fecha_hora");
            valores.add("'".concat(datos[4].trim()).concat("'"));
            nombresColumnas.add("clave_trans");
            valores.add("'".concat(datos[5].trim()).concat("'"));
            nombresColumnas.add("descripcion");
            valores.add("'".concat(datos[3].trim()).concat("'"));
            nombresColumnas.add("referencia");
            //      valores.add("'".concat(datos[2].trim()).concat("'"));
            //    nombresColumnas.add("numero_folio");
            if (!datos[6].equals(null) && !datos[6].equals("")) {
                valores.add(datos[6].trim());
                nombresColumnas.add("monto");
                valores.add("'DR'");
                nombresColumnas.add("id_tipo_trans");
            } else if (!datos[7].equals(null) && !datos[7].equals("")) {
                valores.add(datos[7].trim());
                nombresColumnas.add("monto");
                valores.add("'CR'");
                nombresColumnas.add("id_tipo_trans");
            }
            valores.add(datos[8].trim());
            nombresColumnas.add("saldo");
            if (datos[8].contains("-")) {
                valores.add("'DR'");
            } else {
                valores.add("'CR'");
            }
            nombresColumnas.add("tipo_saldo");
            setCorrecto(true);
        } catch (Exception e) {
            e.printStackTrace();
            setCorrecto(false);
        }
        return isCorrecto();
    }

    public boolean movimientosBajio(String tokens) {
        valores.clear();
        nombresColumnas.clear();
        Double monto = 0D;
        Double saldoCalculado;
        String[] datos;
        datos = tokens.split("\t");
        try {
            if (!datos[5].trim().toString().equals("0")) {
                valores.add("'".concat(datos[0].trim()).concat("'"));
                nombresColumnas.add("num_cuenta");
                valores.add("to_date('".concat(datos[1].trim().substring(6, 8)).concat("/").concat(datos[1].trim().substring(4, 6)).concat("/").concat(datos[1].trim().substring(0, 4)).concat("','dd/mm/yyyy')"));
                nombresColumnas.add("fecha_hora");
                valores.add("'1521'");
                nombresColumnas.add("clave_trans");
                valores.add("'COMISION'");
                nombresColumnas.add("descripcion");
                if (datos[5] != null && !datos[5].equals("")) {
                    if (datos[5].contains("-")) {
                        monto = Double.parseDouble(datos[5].trim()) * -1;
                        valores.add("'DR'");
                    } else {
                        valores.add("'CR'");
                    }
                    nombresColumnas.add("id_tipo_trans");
                    valores.add(monto.toString());
                    nombresColumnas.add("monto");
                    saldoCalculado = Double.parseDouble(datos[8].trim()) - Double.parseDouble(datos[7].trim()) - Double.parseDouble(datos[6].trim());
                    nombresColumnas.add("tipo_saldo");
                    if (saldoCalculado < 0) {
                        valores.add("'DR'");
                    } else {
                        valores.add("'CR'");
                    }
                    valores.add(saldoCalculado.toString());
                    nombresColumnas.add("saldo");
                }
                valores.add("'".concat(datos[3].trim()).concat("'"));
                nombresColumnas.add("referencia");
                valores.add("2DOREGISTROBAJIO");
                nombresColumnas.add("2DOREGISTROBAJIO");
                valores.add("'".concat(datos[0].trim()).concat("'"));
                nombresColumnas.add("num_cuenta");
                valores.add("to_date('".concat(datos[1].trim().substring(6, 8)).concat("/").concat(datos[1].trim().substring(4, 6)).concat("/").concat(datos[1].trim().substring(0, 4)).concat("','dd/mm/yyyy')"));
                nombresColumnas.add("fecha_hora");
                valores.add("'1501'");
                nombresColumnas.add("clave_trans");
                valores.add("'IVA'");
                nombresColumnas.add("descripcion");
                if (datos[6] != null && !datos[6].equals("")) {
                    if (datos[6].contains("-")) {
                        monto = Double.parseDouble(datos[6].trim()) * -1;
                        valores.add("'DR'");
                    } else {
                        valores.add("'CR'");
                        monto = Double.parseDouble(datos[6].trim());
                    }
                    nombresColumnas.add("id_tipo_trans");
                    valores.add(monto.toString());
                    nombresColumnas.add("monto");
                    saldoCalculado = Double.parseDouble(datos[8].trim()) - Double.parseDouble(datos[7].trim());
                    nombresColumnas.add("tipo_saldo");
                    if (saldoCalculado < 0) {
                        valores.add("'DR'");
                    } else {
                        valores.add("'CR'");
                    }
                    valores.add(saldoCalculado.toString());
                    nombresColumnas.add("saldo");
                }
                valores.add("'".concat(datos[3].trim()).concat("'"));
                nombresColumnas.add("referencia");
                if (!datos[7].trim().toString().equals("0")) {
                    valores.add("3ERREGISTROBAJIO");
                    nombresColumnas.add("3ERREGISTROBAJIO");
                    valores.add("'".concat(datos[0].trim()).concat("'"));
                    nombresColumnas.add("num_cuenta");
                    valores.add("to_date('".concat(datos[1].trim().substring(6, 8)).concat("/").concat(datos[1].trim().substring(4, 6)).concat("/").concat(datos[1].trim().substring(0, 4)).concat("','dd/mm/yyyy')"));
                    nombresColumnas.add("fecha_hora");
                    valores.add("'".concat(datos[2]).concat("'"));
                    nombresColumnas.add("clave_trans");
                    valores.add("'".concat(datos[4]).concat("'"));
                    nombresColumnas.add("descripcion");
                    if (datos[7] != null && !datos[7].equals("")) {
                        if (datos[7].contains("-")) {
                            monto = Double.parseDouble(datos[7].trim()) * -1;
                            valores.add("'DR'");
                        } else {
                            valores.add("'CR'");
                            monto = Double.parseDouble(datos[7].trim());
                        }
                        nombresColumnas.add("id_tipo_trans");
                        valores.add(monto.toString());
                        nombresColumnas.add("monto");
                        if (datos[8].contains("-")) {
                            saldoCalculado = Double.parseDouble(datos[8].trim()) * -1;
                            valores.add("'DR'");
                        } else {
                            valores.add("'CR'");
                            saldoCalculado = Double.parseDouble(datos[8].trim());
                        }
                        nombresColumnas.add("tipo_saldo");
                        valores.add(saldoCalculado.toString());
                        nombresColumnas.add("saldo");
                    }
                    valores.add("'".concat(datos[3].trim()).concat("'"));
                    nombresColumnas.add("referencia");
                }
                setCorrecto(true);
            } else {
                valores.add("'".concat(datos[0].trim()).concat("'"));
                nombresColumnas.add("num_cuenta");
                valores.add("to_date('".concat(datos[1].trim().substring(6, 8)).concat("/").concat(datos[1].trim().substring(4, 6)).concat("/").concat(datos[1].trim().substring(0, 4)).concat("','dd/mm/yyyy')"));
                nombresColumnas.add("fecha_hora");
                valores.add("'".concat(datos[2].trim()).concat("'"));
                nombresColumnas.add("clave_trans");
                valores.add("'".concat(datos[4].trim()).concat("'"));
                nombresColumnas.add("descripcion");
                valores.add("'".concat(datos[3].trim()).concat("'"));
                nombresColumnas.add("referencia");
                if (datos[7] != null && !datos[7].equals("")) {
                    if (Double.parseDouble(datos[7].trim()) < 0) {
                        monto = Double.parseDouble(datos[7].trim()) * -1;
                        valores.add(monto.toString());
                        nombresColumnas.add("monto");
                        valores.add("'DR'");
                        nombresColumnas.add("id_tipo_trans");
                    } else {
                        valores.add(datos[7].trim());
                        nombresColumnas.add("monto");
                        valores.add("'CR'");
                        nombresColumnas.add("id_tipo_trans");
                    }
                }
                if (datos[8] != null && !datos[8].equals("")) {
                    if (Double.parseDouble(datos[8].trim()) < 0) {
                        monto = Double.parseDouble(datos[8].trim()) * -1;
                        valores.add(monto.toString());
                        nombresColumnas.add("saldo");
                        valores.add("'DR'");
                        nombresColumnas.add("tipo_saldo");
                    } else {
                        valores.add(datos[8].trim());
                        nombresColumnas.add("saldo");
                        valores.add("'CR'");
                        nombresColumnas.add("tipo_saldo");
                    }
                }
                setCorrecto(true);
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
            setCorrecto(false);
        }
        return isCorrecto();
    }

    /*   public boolean movimientosBajio(String tokens){
     valores.clear();
     nombresColumnas.clear();
     Double monto= 0D;
     Double saldoCalculado = 0D;
     String[] datos = null;
     datos  = tokens.split("\t");
     try  {
     if (getTipoTransaccion(datos[2]).equals("C")){
     for (int i = 0; i <= 1; i++)  {
     saldoCalculado = 0D;
     if (i==0) {
     valores.add("'".concat(datos[0].trim()).concat("'"));
     nombresColumnas.add("num_cuenta");
     valores.add("to_date('".concat(datos[1].trim().substring(6,8)).concat("/").concat(datos[1].trim().substring(4,6)).concat("/").concat(datos[1].trim().substring(0,4)).concat("','dd/mm/yyyy')") );
     nombresColumnas.add("fecha_hora");
     valores.add("'".concat(datos[2].trim()).concat("'"));
     nombresColumnas.add("clave_trans");
     valores.add("'".concat(datos[4].trim()).concat("'"));
     nombresColumnas.add("descripcion");
     if(!datos[5].equals(null) && !datos[5].equals("")){
     if(datos[5].contains("-")){
     monto = Double.parseDouble(datos[5].trim()) * -1;
     valores.add("'DR'");
     }
     else
     valores.add("'CR'");
     nombresColumnas.add("id_tipo_trans");
     valores.add(monto.toString());
     nombresColumnas.add("monto");
     saldoCalculado = Double.parseDouble(datos[8].trim()) - Double.parseDouble(datos[6].trim());
     nombresColumnas.add("tipo_saldo");
     if(saldoCalculado<0)
     valores.add("'DR'");
     else
     valores.add("'CR'");
     valores.add(saldoCalculado.toString());
     nombresColumnas.add("saldo");
     }
     valores.add("'".concat(datos[3].trim()).concat("'"));
     nombresColumnas.add("referencia");
     valores.add("2DOREGISTROBAJIO");
     nombresColumnas.add("2DOREGISTROBAJIO");
     }
     else{
     valores.add( "'".concat(datos[0].trim()).concat("'") );
     nombresColumnas.add("num_cuenta");
     valores.add( "to_date('".concat(datos[1].trim().substring(6,8)).concat("/").concat(datos[1].trim().substring(4,6)).concat("/").concat(datos[1].trim().substring(0,4)).concat("','dd/mm/yyyy')") );
     nombresColumnas.add("fecha_hora");
     valores.add("'1501'");
     nombresColumnas.add("clave_trans");
     valores.add("'IVA'");
     nombresColumnas.add("descripcion");
     if(!datos[6].equals(null) && !datos[6].equals("")){
     if(datos[6].contains("-")){
     monto = Double.parseDouble(datos[6].trim()) * -1;
     valores.add("'DR'");    
     }
     else
     valores.add("'CR'");
     nombresColumnas.add("id_tipo_trans");
     valores.add(monto.toString());
     nombresColumnas.add("monto");
     //saldoCalculado = (Double.parseDouble(datos[8].trim()) - Double.parseDouble(datos[6].trim())) + Double.parseDouble(datos[6].trim());
     saldoCalculado = Double.parseDouble(datos[8].trim());
     nombresColumnas.add("tipo_saldo");
     if(saldoCalculado<0)
     valores.add("'DR'");
     else
     valores.add("'CR'");
     valores.add(saldoCalculado.toString());
     nombresColumnas.add("saldo");
     }
     valores.add("'".concat(datos[3].trim()).concat("'"));
     nombresColumnas.add("referencia");
     }
     }
     setCorrecto(true); 
     }
     else{
     valores.add( "'".concat(datos[0].trim()).concat("'") );
     nombresColumnas.add("num_cuenta");
     valores.add( "to_date('".concat(datos[1].trim().substring(6,8)).concat("/").concat(datos[1].trim().substring(4,6)).concat("/").concat(datos[1].trim().substring(0,4)).concat("','dd/mm/yyyy')") );
     nombresColumnas.add("fecha_hora");
     valores.add( "'".concat(datos[2].trim()).concat("'") );
     nombresColumnas.add("clave_trans");
     valores.add("'".concat(datos[4].trim()).concat("'"));
     nombresColumnas.add("descripcion");
     valores.add("'".concat(datos[3].trim()).concat("'"));
     nombresColumnas.add("referencia");
     if(!datos[7].equals(null) && !datos[7].equals("")){
     if (Double.parseDouble(datos[7].trim())<0){
     monto = Double.parseDouble(datos[7].trim()) * -1;
     valores.add(monto.toString());
     nombresColumnas.add("monto");
     valores.add("'DR'");
     nombresColumnas.add("id_tipo_trans");
     }else{
     valores.add(datos[7].trim());
     nombresColumnas.add("monto");
     valores.add("'CR'");
     nombresColumnas.add("id_tipo_trans");
     }
     } 
     if(!datos[8].equals(null) && !datos[8].equals("")){
     if (Double.parseDouble(datos[8].trim())<0){
     monto = Double.parseDouble(datos[8].trim()) * -1;
     valores.add(monto.toString());
     nombresColumnas.add("saldo");
     valores.add("'DR'");
     nombresColumnas.add("tipo_saldo");
     }else{
     valores.add(datos[8].trim());
     nombresColumnas.add("saldo");
     valores.add("'CR'");
     nombresColumnas.add("tipo_saldo");
     }
     } 
     setCorrecto(true);
     }
     } catch (Exception e)  {
     e.printStackTrace();
     setCorrecto(false);
     }
     return isCorrecto();
     }
     */
    public Iterator getNombresColumnas() {
        return nombresColumnas.iterator();
    }

    public Iterator getTipoDatos() {
        return tipoDatos.iterator();
    }

    public Iterator getValidacion() {
        return validacion.iterator();
    }

    public Iterator getLongitudesColumnas() {
        return longitudesColumnas.iterator();
    }

    public Iterator getValores() {
        return valores.iterator();
    }

    private void setNombre(String nombre) {
        this.nombre = nombre;
    }

    private String getNombre() {
        return nombre;
    }

    private void setSeccion(String seccion) {
        this.seccion = seccion;
    }

    private String getSeccion() {
        return seccion;
    }

    public void setColumnas(int columnas) {
        this.columnas = columnas;
    }

    public int getColumnas() {
        return columnas;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator iterator = getValores();
        sb.append('[');
        while (iterator.hasNext()) {
            sb.append((String) iterator.next());
            if (iterator.hasNext()) {
                sb.append(',');
            }
        } // while iterator
        sb.append(']');
        return sb.toString();
    } // toString

    public String toNombres() {
        StringBuilder sb = new StringBuilder();
        Iterator iterator = getNombresColumnas();
        sb.append('[');
        while (iterator.hasNext()) {
            sb.append((String) iterator.next());
            if (iterator.hasNext()) {
                sb.append(',');
            }
        } // while iterator
        sb.append(']');
        return sb.toString();
    } // toString

    public int getLongitudColumna(String columna) {
        int posicion = nombresColumnas.indexOf(columna);
        if (posicion >= 0) {
            posicion = ((Integer) longitudesColumnas.elementAt(posicion)).intValue();
        }
        return posicion;
    } // getLongitudColumna

    public void setSeparador(String separador) {
        this.separador = separador;
    }

    public String getSeparador() {
        return separador;
    }

    public void setCorrecto(boolean correcto) {
        this.correcto = correcto;
    }

    public boolean isCorrecto() {
        return correcto;
    }
}
