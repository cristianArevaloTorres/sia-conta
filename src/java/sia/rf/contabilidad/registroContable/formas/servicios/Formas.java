package sia.rf.contabilidad.registroContable.formas.servicios;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import sia.db.dao.DaoFactory;
import sia.db.sql.Sentencias;
import sia.db.sql.Vista;
import sia.libs.formato.Cadena;
import sia.libs.formato.Error;
import sia.libs.formato.Formatos;
import sia.libs.formato.Numero;
import sia.libs.formato.Variables;
import sia.libs.recurso.Contabilidad;
import sia.rf.contabilidad.registroContable.acciones.ControlRegistro;
import sia.rf.contabilidad.registroContable.formas.Areas;
import sia.rf.contabilidad.registroContable.servicios.Cuenta;
import sia.rf.contabilidad.registroContable.servicios.FechaAfectacion;
import sia.rf.contabilidad.registroContable.servicios.SubCuenta;
import sia.ws.publicar.contabilidad.excepciones.PolizaException;

public class Formas {

    private String formaContableId;
    private String ejercicio;
    private List listaConfiguracion;
    private List listaCuentasContables;
    private List cuentasNoExisten;
    private int tipoProceso;
    private int idCatalogoCuenta;
    private String fecha;
    private ControlRegistro controlReg;
    public static final String UNIDAD_EJECUTORA = "001";
    public static final String ENTIDAD = "9";
    public static final String AMBITO = "1";

    public Formas(String formaContableId, int idCatalogoCuenta) {
        setFormaContableId(formaContableId);
        setListaConfiguracion(new ArrayList());
        setListaCuentasContables(new ArrayList());
        setIdCatalogoCuenta(idCatalogoCuenta);
        setCuentasNoExisten(new ArrayList());
    }

    public Formas(String formaContableId, int tipoProceso, String ejercicio, int idCatalogoCuenta, String fecha) {
        setFormaContableId(formaContableId);
        setTipoProceso(tipoProceso);
        setListaConfiguracion(new ArrayList());
        setListaCuentasContables(new ArrayList());
        setEjercicio(ejercicio);
        setIdCatalogoCuenta(idCatalogoCuenta);
        setFecha(fecha);
        setCuentasNoExisten(new ArrayList());
    }

    public Formas(String forma, String unidadEjecutora, String entidad, String ambito, String ejercicio, int idCatalogoCuenta, String fecha) {
        this(forma, unidadEjecutora, entidad, ambito, Areas.MANUAL, ejercicio, idCatalogoCuenta, fecha, null);
    }

    public Formas(String forma, String unidadEjecutora, String entidad, String ambito, int tipoProceso, String ejercicio, int idCatalogoCuenta, String fecha, ControlRegistro controlReg) {
        setListaConfiguracion(new ArrayList());
        setListaCuentasContables(new ArrayList());
        setTipoProceso(tipoProceso);
        setEjercicio(ejercicio);
        setIdCatalogoCuenta(idCatalogoCuenta);
        setFormaContableId(obtenerFormaContableId(forma, unidadEjecutora, entidad, ambito));
        setCuentasNoExisten(new ArrayList());
        setFecha(fecha);
        setControlReg(controlReg);
    }

    public String obtenerFormaContableId(String forma, String unidadEjecutora, String entidad, String ambito) {
        int formaContableId = -1;
        Formatos formatos;
        Sentencias sentencias;
        List<Vista> vista;
        Vista reg;
        try {
            //System.out.println("forma: "+forma);
            //System.out.println("unidad: "+unidadEjecutora);
            //System.out.println("entidad: "+entidad);
            //System.out.println("ambito: "+ambito);
            formatos = new Formatos(Contabilidad.getPropiedad("formasContables.query.obtenerFormaContableId"), forma, unidadEjecutora, entidad, ambito, getIdCatalogoCuenta());
            sentencias = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD);
            vista = sentencias.registros(formatos.getSentencia());
            if (vista.size() > 0) {
                reg = vista.get(0);
                formaContableId = Integer.parseInt(reg.getField("FORMA_CONTABLE_ID"));
            }
        } catch (Exception e) {
            Error.mensaje(e, "CONTABILIDAD");
        }
        return String.valueOf(formaContableId);
    }

    public List getCuentas(String cadenaVariables, Connection conexion) throws Exception {
        StringBuilder cadena;
        String codigo;
        int esVariable;
        int secuenciaFormaId;
        int nivel;
        String signo;
        String crearCuentaContable;
        String operacionContableId;
        String importe = null;
        String referencia = null;
        String movible;
        List listaDetalleFormaContable;
        Formatos formatos;
        List<Vista> vista;
        Sentencias sentencias;
        Vista reg;
        int posicionError = 0;
        try {
            listaDetalleFormaContable = new ArrayList();
            cadena = new StringBuilder();
            formatos = new Formatos(Contabilidad.getPropiedad("formasContables.query.obtenerDetalleConfiguraFormas"), getFormaContableId());
            sentencias = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD);
            vista = sentencias.registros(formatos.getSentencia());
            //System.out.println("Formas.getCuentas().formatos.getSentencias() "+formatos.getSentencia());
            Iterator it = vista.iterator();
            reg = (Vista) it.next();
            while (it.hasNext()) {
                posicionError = 1;
                secuenciaFormaId = Integer.parseInt(reg.getField("SECUENCIA_FORMA_ID"));
                crearCuentaContable = reg.getField("CREAR_CUENTA_CONTABLE");
                movible = reg.getField("MOVIBLE");
                operacionContableId = reg.getField("OPERACION_CONTABLE_ID");
                do {
                    codigo = reg.getField("CODIGO");
                    esVariable = Integer.parseInt(reg.getField("ESVARIABLE"));
                    nivel = Integer.parseInt(reg.getField("NIVEL"));
                    signo = reg.getField("SIGNO");
                    switch (nivel) {
                        case 98:
                            if ((signo != null) && (signo.equals("1"))) {
                                importe = "-".concat(formateaCadena(esVariable, codigo));
                            } else {
                                importe = formateaCadena(esVariable, codigo);
                            }
                            break;
                        case 99:
                            referencia = formateaCadena(esVariable, codigo);
                            break;
                        default:
                            cadena.append(formateaCadena(esVariable, codigo));
                            listaDetalleFormaContable.add(new ElementoFormaContable(formateaCadena(esVariable, codigo), nivel));
                            //System.out.println("Formas.getCuentas agrega código "+codigo);
                            break;
                    } //switch
                    reg = (Vista) it.next();
                } while (it.hasNext() && Integer.parseInt(reg.getField("SECUENCIA_FORMA_ID")) == secuenciaFormaId);
                posicionError++;
                //System.out.println("Formas.getCuentas().codigo = "+codigo);
                //System.out.println("Formas.getCuentas().secuenciaFormaId = "+secuenciaFormaId);
                if (cadena.length() > 0) {
                    agregarCuentasFormas(cadenaVariables, listaDetalleFormaContable, operacionContableId, importe, referencia,
                            crearCuentaContable, movible, String.valueOf(secuenciaFormaId), conexion);
                    posicionError++;
                    listaDetalleFormaContable.clear();
                    posicionError++;
                    //cadena.delete(0, cadena.length());
                    cadena = new StringBuilder();
                    posicionError++;
                } //if
                posicionError++;
            }
        } // try
        catch (Exception e) {
            System.out.println(posicionError + " - Formas.getCuentas " + e.getClass().getName() + ": " + e.getMessage());
            Error.mensaje(e, "CONTABILIDAD");
            throw new Exception(" * " + e.getMessage() + " * ");
        } finally {
        } //finally
        this.validarCuentas();
        return getListaCuentasContables();
    }

    private String formateaCadena(int esVariable, String cadena) {
        if (esVariable == 1) {
            return "{".concat(cadena).concat("}");
        }
        return cadena;
    }

    public List getCuentas(List cadenaVariables, Connection conexion) throws Exception {
        for (int i = 0; i < cadenaVariables.size(); i++) {
            getCuentas((String) cadenaVariables.get(i), conexion);
        }
        validarCuentas();
        return getListaCuentasContables();
    }

    private void validarCuentas() {
        CuentasForma cuentaForma;
        List listaTempCuentasContables = new ArrayList();
        for (int i = 0; i < getListaCuentasContables().size(); i++) {
            cuentaForma = (CuentasForma) getListaCuentasContables().get(i);
            if (!(Double.parseDouble(cuentaForma.getImporte()) == 0 && cuentaForma.getMovible().equals("1"))) {
                listaTempCuentasContables.add(cuentaForma);
            }
        }
        setListaCuentasContables(listaTempCuentasContables);
    }

    private boolean agregarCuentasFormas(String cadenaVariables, List listaDetalleFormaContable, String operacionContableId, String importe,
            String referencia, String crearCuentaContable, String movible, String secuenciaFormaId, Connection conexion) throws PolizaException, Exception {
        boolean isAgrego = false;
        int posicionError = 0;
        try {
            CuentasForma cuentaForma = generarCuentasFormas(cadenaVariables, listaDetalleFormaContable, operacionContableId, importe, referencia, movible, secuenciaFormaId, conexion);
            posicionError++;
            switch (getTipoProceso()) {
                case Areas.MANUAL:
                case Areas.MIPF:
                    crearCuentaContable = "1";
                    if (cuentaForma.getCuentaContableId() != -1) {
                        if (!isExisteCuenta(cuentaForma)) {
                            if (cuentaForma.getCuentaContableId() == -1 && crearCuentaContable != null && crearCuentaContable.equals("1")) {
                                insertarCuenta(cuentaForma, conexion);
                                //cuentaForma.setCuentaContableId(obtenerCuentacontableId(cuentaForma.getCuentaContableFormateada()));
                            } else if (cuentaForma.getCuentaContableId() == -1 && (crearCuentaContable == null || crearCuentaContable.equals("0"))) {
                                System.out.println("No agrego elemento a la forma");
                            }
                            getListaCuentasContables().add(cuentaForma);
                            //getListaCuentasContables().add(cuentaForma);
                            isAgrego = true;
                        } // if            
                    }
                    break;
                case Areas.ALMACEN:
                case Areas.NOMINA:
                case Areas.VIATICOS:
                case Areas.PRESUPUESTO:
                    if (!isExisteCuenta(cuentaForma)) {
                        if (cuentaForma.getCuentaContableId() == -1 && crearCuentaContable != null && crearCuentaContable.equals("1")) {
                            insertarCuenta(cuentaForma, conexion);
                            //cuentaForma.setCuentaContableId(obtenerCuentacontableId(cuentaForma.getCuentaContableFormateada()));
                        } else if (cuentaForma.getCuentaContableId() == -1 && (crearCuentaContable == null || crearCuentaContable.equals("0"))) {
                            System.out.println("No agrego elemento a la forma");
                        }
                        getListaCuentasContables().add(cuentaForma);
                        isAgrego = true;
                    } // if
                    break;
                case Areas.TESORERIA:
                    if (cuentaForma.getCuentaContableId() != -1) {
                        if (!isExisteCuenta(cuentaForma)) {
                            getListaCuentasContables().add(cuentaForma);
                            isAgrego = true;
                        } // if            
                    } else {
                        // insetar en tabla de movimietos 
                        System.out.println("movimiento no identificado");
                        System.out.println(cuentaForma.getCuentaContableFormateada());
                        System.out.println(getFormaContableId());
                        System.out.println(cadenaVariables);
                    }
                    break;
                case Areas.MIPF_NO_ACUMULADO:
                    if (cuentaForma.getCuentaContableId() != -1) {
                        getListaCuentasContables().add(cuentaForma);
                        isAgrego = true;
                    }
                    break;
                case Areas.VENTAS:
                    if (cuentaForma.getCuentaContableId() != -1) {
                        getListaCuentasContables().add(cuentaForma);
                        isAgrego = true;
                    }
                    break;
                case Areas.INGRESOS_POR_VENTA:
                    if (cuentaForma.getCuentaContableId() != -1) {
                        getListaCuentasContables().add(cuentaForma);
                        isAgrego = true;
                    } // if      
                    else {
                        System.out.println(cuentaForma.getCuentaContableFormateada());
                    }
            } // switch
        } catch (Exception e) {
            System.out.println(posicionError + "Ocurrio un error al accesar al metodo agregarCuentasFormas " + e.getClass().getName() + ": " + e.getMessage());
            throw e;
        }
        return isAgrego;
    }

    private boolean insertarCuenta(CuentasForma cuenta, Connection conexion) {
        Formatos formatos = null;
        String cuentaMayor;
        String partida = null;
        String idCuentaMayor;
        String descPartida = null;
        boolean insertoCorrectamente = false;
        ElementoConfiguracionCuenta elemento;
        Map parametros;
        Sentencias sentencias;
        try {
            sentencias = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD, Sentencias.XML);
            parametros = new HashMap();
            //if (cuenta.getCuentaContableFormateada().length()!=getTamanioCuenta())
            //throw new PolizaException(PolizaException.REGISTRO_CUENTA_CONTABLE);
            elemento = (ElementoConfiguracionCuenta) getListaConfiguracion().get(0);
            cuentaMayor = cuenta.getCuentaContableFormateada().substring(0, elemento.getTamanio());
            idCuentaMayor = getIdCuentaMayor(cuentaMayor, conexion);
            elemento = (ElementoConfiguracionCuenta) getListaConfiguracion().get(getListaConfiguracion().size() - 1);
            //int inicio =cuenta.getCuentaContableFormateada().length()-elemento.getTamanio();
            //partida = cuenta.getCuentaContableFormateada().substring(inicio);            
            //descPartida=getDescripcionPartida(partida,Fecha.getHoyEstandar());      
            if (idCuentaMayor.equals("-1")) {
                throw new PolizaException(PolizaException.REGISTRO_CUENTA_CONTABLE);
            }
            parametros.put("idCuentaMayor", idCuentaMayor);
            parametros.put("cuentaContable", cuenta.getCuentaContableFormateada());
            parametros.put("nivel", listaConfiguracion.size());
            parametros.put("descripcion", "Pendiente");
            parametros.put("fechaIni", "01/01/2012");
            parametros.put("fechaFin", "31/12/2099");
            parametros.put("numEmpleado", 76454);
            parametros.put("fechaAlta", "24/04/2012");
            parametros.put("idCatalogoCuenta", cuenta.getIdCatalogoCuenta());
            //sentencias.ejecutar(conexion,"validaCC.insert.insertarCuentaContable",parametros);
            //formatos = new Formatos(Contabilidad.getInstance().getPropiedad("registroContable.query.registrarCuentaContable"),null, idCuentaMayor, cuenta.getCuentaContableFormateada(),
            //                                                                  listaConfiguracion.size(), "Pendiente",
            //                                                                  Fecha.formatear(Fecha.FECHA_ESTANDAR,Fecha.getRegistro()),"20991231","0", String.valueOf(47372), String.valueOf(this.getIdCatalogoCuenta()));
            //Cuenta cuenta = new Cuenta(cuentaContableId);
            //ArrayList listaCuenta = cuenta.getPadreId(cuenta.getNivel());
            //Sentencias sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD);
            //System.out.println(sentencias.getComando("validaCuentaContable.insert.insertarCuentaContable",parametros));
            if (sentencias.ejecutar(conexion, "validaCuentaContable.insert.insertarCuentaContable", parametros) == -1) {
                throw new PolizaException(PolizaException.REGISTRO_CUENTA_CONTABLE);
            } else {
                insertoCorrectamente = true;
            }
        } catch (Exception e) {
            Error.mensaje(e, "CONTABILIDAD");
        }
        return insertoCorrectamente;
    }

    private boolean insertarCuenta(Connection conexion, String cuentaMayor, String cuentaContable, int nivel, String fechaIni, String fechaFin, String fechaAlta, int idCatalogoCuenta) {
        Map parametros;
        Sentencias sentencias;
        String idCuentaMayor;
        boolean inserto = false;
        try {
            sentencias = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD, Sentencias.XML);
            parametros = new HashMap();
            idCuentaMayor = getIdCuentaMayor(cuentaMayor, conexion);
            if (idCuentaMayor.equals("-1")) {
                throw new PolizaException(PolizaException.REGISTRO_CUENTA_CONTABLE);
            }
            parametros.put("idCuentaMayor", idCuentaMayor);
            parametros.put("cuentaContable", cuentaContable);
            parametros.put("nivel", nivel);
            parametros.put("descripcion", "Pendiente");
            parametros.put("fechaIni", fechaIni);
            parametros.put("fechaFin", fechaFin);
            parametros.put("numEmpleado", 27704);
            parametros.put("fechaAlta", fechaAlta);
            parametros.put("idCatalogoCuenta", idCatalogoCuenta);
            if (sentencias.ejecutar(conexion, "validaCuentaContable.insert.insertarCuentaContable", parametros) == -1) {
                throw new PolizaException(PolizaException.REGISTRO_CUENTA_CONTABLE);
            } else {
                inserto = true;
            }
        } catch (Exception e) {
            Error.mensaje(e, "CONTABILIDAD");
        }
        return inserto;
    }

    private String obtenerNivel(String cuentaContable) throws Exception {
        String regresa = null;
        try {
            String nivel1 = cuentaContable.substring(0, 4);
            String nivel2 = cuentaContable.substring(4, 5);
            String nivel3 = cuentaContable.substring(5, 9);
            String nivel4 = cuentaContable.substring(9, 13);
            String nivel5 = cuentaContable.substring(13, 17);
            String nivel6 = cuentaContable.substring(17, 21);
            String nivel7 = cuentaContable.substring(21, 25);
            String nivel8 = cuentaContable.substring(25, 29);
            String nivel9 = cuentaContable.substring(29, cuentaContable.length() > 33 ? 33 : cuentaContable.length());
            //System.out.println(nivel1+" "+nivel2+" "+nivel3+" "+nivel4+" "+nivel5+" "+nivel6+" "+nivel7+" "+nivel8+" "+nivel9);

            if (!nivel1.equals("0000") && nivel2.equals("0") && nivel3.equals("0000") && nivel4.equals("0000") && nivel5.equals("0000") && nivel6.equals("0000") && nivel7.equals("0000") && nivel8.equals("0000") && nivel9.equals("0000")) {
                regresa = "1";
            } else if (!nivel1.equals("0000") && !nivel2.equals("0") && nivel3.equals("0000") && nivel4.equals("0000") && nivel5.equals("0000") && nivel6.equals("0000") && nivel7.equals("0000") && nivel8.equals("0000") && nivel9.equals("0000")) {
                regresa = "2";
            } else if (!nivel1.equals("0000") && !nivel2.equals("0") && !nivel3.equals("0000") && nivel4.equals("0000") && nivel5.equals("0000") && nivel6.equals("0000") && nivel7.equals("0000") && nivel8.equals("0000") && nivel9.equals("0000")) {
                regresa = "3";
            } else if (!nivel1.equals("0000") && !nivel2.equals("0") && !nivel3.equals("0000") && !nivel4.equals("0000") && nivel5.equals("0000") && nivel6.equals("0000") && nivel7.equals("0000") && nivel8.equals("0000") && nivel9.equals("0000")) {
                regresa = "4";
            } else if (!nivel1.equals("0000") && !nivel2.equals("0") && !nivel3.equals("0000") && !nivel4.equals("0000") && !nivel5.equals("0000") && nivel6.equals("0000") && nivel7.equals("0000") && nivel8.equals("0000") && nivel9.equals("0000")) {
                regresa = "5";
            } else if (!nivel1.equals("0000") && !nivel2.equals("0") && !nivel3.equals("0000") && !nivel4.equals("0000") && !nivel5.equals("0000") && !nivel6.equals("0000") && nivel7.equals("0000") && nivel8.equals("0000") && nivel9.equals("0000")) {
                regresa = "6";
            } else if (!nivel1.equals("0000") && !nivel2.equals("0") && !nivel3.equals("0000") && !nivel4.equals("0000") && !nivel5.equals("0000") && !nivel6.equals("0000") && !nivel7.equals("0000") && nivel8.equals("0000") && nivel9.equals("0000")) {
                regresa = "7";
            } else if (!nivel1.equals("0000") && !nivel2.equals("0") && !nivel3.equals("0000") && !nivel4.equals("0000") && !nivel5.equals("0000") && !nivel6.equals("0000") && !nivel7.equals("0000") && !nivel8.equals("0000") && nivel9.equals("0000")) {
                regresa = "8";
            } else if (!nivel1.equals("0000") && !nivel2.equals("0") && !nivel3.equals("0000") && !nivel4.equals("0000") && !nivel5.equals("0000") && !nivel6.equals("0000") && !nivel7.equals("0000") && !nivel8.equals("0000") && !nivel9.equals("0000")) {
                regresa = "9";
            } else {
                throw new Exception("La cuenta que se requiere insertar es incorrecta " + cuentaContable);
            }
            //System.out.println("La cuenta es incorrecta por lo tanto no puede darse de alta");
        } catch (Exception e) {
            System.out.println("*** Excepción en Formas.obtenerNivel.cuentaContable " + cuentaContable);
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
            Error.mensaje(e, "CONTABILIDAD");
            throw e;
        }
        return regresa;
    }

    private String getIdCuentaMayor(String cuentaMayor, Connection conexion) {

        Object id = null;
        Formatos formatos = null;
        Sentencias sentencias;
        Map parametros;
        try {
            parametros = new HashMap();
            sentencias = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD, Sentencias.XML);
            parametros.put("cuentaMayor", cuentaMayor);
            id = sentencias.consultar(conexion, "validaCuentaContable.select.cuentaMayor", parametros);

            //id=sentencias.consultar(formatos.getSentencia());      
        } catch (Exception e) {
            Error.mensaje(e, "CONTABILIDAD");
        }
        if (id != null) {
            return String.valueOf(id);
        }
        return "-1";
    }

    private String getDescripcionPartida(String partida, String fecha) {
        String desc = null;
        Map variables;
        VariableForma variable;
        try {
            variables = new HashMap();
            variables.put("nombre", "BIEN");
            variables.put("areas_formas_id", 1);
            variables.put("partida", partida);
            variables.put("fecha", fecha);
            variable = new VariableForma(variables);
            desc = variable.getCodigo();
        } catch (Exception e) {
            Error.mensaje(e, "CONTABILIDAD");
        }
        if (desc != null && !desc.isEmpty()) {
            return desc;
        }
        return "Partida no existe";
    }

    private boolean isExisteCuenta(CuentasForma cuentaABuscar) {
        Iterator iteratorLista = getListaCuentasContables().iterator();
        CuentasForma cuentaForma;
        boolean existe = false;
        while (iteratorLista.hasNext() && !existe) {
            cuentaForma = (CuentasForma) iteratorLista.next();
            switch (getTipoProceso()) {
                case Areas.MANUAL:
                case Areas.MIPF:
                    if (cuentaABuscar.getCuentaContableId() == cuentaForma.getCuentaContableId()
                            && cuentaABuscar.getTipoOperacion().equals(cuentaForma.getTipoOperacion())
                            && cuentaABuscar.getReferencia().equals(cuentaForma.getReferencia())) {
                        cuentaForma.setImporte(sumarImporte(cuentaForma.getImporte(), cuentaABuscar.getImporte()));
                        existe = true;
                    }
                    break;
                case Areas.ALMACEN:
                case Areas.NOMINA:
                case Areas.VIATICOS:
                case Areas.PRESUPUESTO:
                    if (cuentaABuscar.getCuentaContableId() == cuentaForma.getCuentaContableId()
                            && cuentaABuscar.getTipoOperacion().equals(cuentaForma.getTipoOperacion())) {
                        cuentaForma.setImporte(sumarImporte(cuentaForma.getImporte(), cuentaABuscar.getImporte()));
                        existe = true;
                    }
                    break;
                case Areas.TESORERIA:
                    if (cuentaABuscar.getCuentaContableId() == cuentaForma.getCuentaContableId()
                            && cuentaABuscar.getTipoOperacion().equals(cuentaForma.getTipoOperacion())
                            && cuentaABuscar.getReferencia().equals(cuentaForma.getReferencia())) {
                        cuentaForma.setImporte(sumarImporte(cuentaForma.getImporte(), cuentaABuscar.getImporte()));
                        existe = true;
                    }
                    break;
            }
        }
        return existe;
    }

    private String sumarImporte(String importe1, String importe2) {
        return String.valueOf(Numero.formatear(Numero.NUMERO_DECIMALES, (Double.valueOf(importe1) + Double.valueOf(importe2))));
    }

    private CuentasForma generarCuentasFormas(String cadenaVariables, List listaDetalleFormaContable, String operacionContableId, String importe, String referencia, String movible, String secuenciaFormaId, Connection conexion) throws Exception {
        String importeFormateado = null;
        String referenciaFormateado = null;
        String cuentaContableOriginal = null;
        ElementoFormaContable elemento;
        ElementoConfiguracionCuenta elementoConfiguracion;
        String cuentaContableFormateada = null;
        CuentasForma cuentaForma = null;
        int cuentaContableId = -1;
        int nivel;
        //List<String> cuentas = null;
        String cuentaMayor;
        SubCuenta subCuenta;
        //boolean existe = false;
        //bcCierresMensuales cierreMensual = null;
        //Connection conexion = null;
        int posicionError = 0;
        try {
            //cierreMensual = new bcCierresMensuales();
            //conexion = DaoFactory.getContabilidad();
            if (validarVariables(secuenciaFormaId, cadenaVariables)) {
                //System.out.println("Formas.generarCuentasFormas validarVariables OK");
                //System.out.println(cadenaVariables);
                //cuentaContableId = 0;
                //cuentas = new ArrayList<String>();
                //System.out.println("Formas.generarCuentasFormas importe "+importe);
                if (importe.substring(0, 1).equals("-")) {
                    importeFormateado = reemplazarValores(cadenaVariables, importe);
                    if (importeFormateado.indexOf("{") != -1 || importeFormateado.indexOf("}") != -1) {
                        importeFormateado = "0.0";
                    } else if (importeFormateado.lastIndexOf("--") == 0) {
                        importeFormateado = importeFormateado.substring(2);
                    } else if (importeFormateado.lastIndexOf("-") == 1) {
                        importeFormateado = importeFormateado.substring(importeFormateado.lastIndexOf("-"));
                    }
                } else {
                    //System.out.println("Formas.generarCuentasFormas else inicial ");
                    importeFormateado = reemplazarValores(cadenaVariables, importe);
                    if (importeFormateado.indexOf("{") != -1 || importeFormateado.indexOf("}") != -1) {
                        importeFormateado = "0.0";
                    }
                }
                //System.out.println("Formas.generarCuentasFormas previo a reeemplazarValores ");
                referenciaFormateado = reemplazarValores(cadenaVariables, referencia);
                posicionError++;
                cuentaContableOriginal = obtenerCadena(listaDetalleFormaContable, false, null);
                posicionError++;
                elemento = (ElementoFormaContable) listaDetalleFormaContable.get(0);
                posicionError++;
                //System.out.println("Formas.generarCuentasFormas().elemento.getValor() " + elemento.getValor());
                cargarConfiguracion(elemento.getValor());
                posicionError++;
                //System.out.println("Formas.generarCuentasFormas.cadenaVariables " + cadenaVariables);
                //System.out.println(referenciaFormateado);
                //System.out.println(importeFormateado);
                //System.out.println("Formas.generarCuentasFormas.listaDetalleFormaContable.nivel "+((ElementoFormaContable)listaDetalleFormaContable.get(1)).getNivel());
                //System.out.println("Formas.generarCuentasFormas.listaDetalleFormaContable.valor "+((ElementoFormaContable)listaDetalleFormaContable.get(1)).getValor());
                cuentaContableFormateada = obtenerCadena(listaDetalleFormaContable, true, cadenaVariables);
                posicionError++;
                String[] vars = cadenaVariables.split(Pattern.quote("|"));
                //System.out.println("Formas.generarCuentasFormas.cuentaContableFormateada "+cuentaContableFormateada);
                for (String var : vars) {
                    //System.out.println("Formas.generarCuentasFormas. var "+var);
                    if (var.contains("TIGACAPITULO")) {
                        String capitulo = var.split("=")[1];
                        cuentaContableOriginal = cuentaContableOriginal.replace("TIGACAPITULO", capitulo);
                        cuentaContableFormateada = cuentaContableFormateada.replace("TIGACAPITULO", capitulo);
                        //System.out.println("Formas.generarCuentasFormas reemplazo de TIGACAPITULO por "+capitulo);
                        //System.out.println("Formas.generarCuentasFormas.cuentaContableFormateada "+cuentaContableFormateada);
                    } else if (var.contains("TIGA") && !((cuentaContableOriginal != null && cuentaContableOriginal.contains("TIGACAPITULO")) || (cuentaContableFormateada != null && cuentaContableFormateada.contains("TIGACAPITULO")))) {
                        String tiga = var.split("=")[1];
                        cuentaContableOriginal = cuentaContableOriginal.replace("TIGA", tiga);
                        cuentaContableFormateada = cuentaContableFormateada.replace("TIGA", tiga);
                        //System.out.println("Formas.generarCuentasFormas.cuentaContableFormateada "+cuentaContableFormateada);
                    }
                    if (var.contains("PARTIDA")) {
                        String partida = var.split("=")[1];
                        cuentaContableOriginal = cuentaContableOriginal.replace("PARTIDA", partida);
                        cuentaContableFormateada = cuentaContableFormateada.replace("PARTIDA", partida);
                        //System.out.println("Formas.generarCuentasFormas.cuentaContableFormateada " + cuentaContableFormateada);
                    }
                    if (var.contains("IMP1")) {
                        String imp1 = var.split("=")[1];
                        cuentaContableOriginal = cuentaContableOriginal.replace("IMP1", imp1);
                        cuentaContableFormateada = cuentaContableFormateada.replace("IMP1", imp1);
                        //System.out.println("Formas.generarCuentasFormas.cuentaContableFormateada "+cuentaContableFormateada);
                    }
                    if (var.contains("IMP2")) {
                        String imp2 = var.split("=")[1];
                        cuentaContableOriginal = cuentaContableOriginal.replace("IMP2", imp2);
                        cuentaContableFormateada = cuentaContableFormateada.replace("IMP2", imp2);
                        //System.out.println("Formas.generarCuentasFormas.cuentaContableFormateada "+cuentaContableFormateada);
                    }
                }
                if (cuentaContableFormateada.length() > 32) {
                    cuentaContableFormateada = cuentaContableFormateada.substring(0, 33);
                }
                posicionError++;
                cuentaContableId = obtenerCuentacontableId(cuentaContableFormateada, conexion);
                posicionError++;
                cuentaForma = new CuentasForma(cuentaContableOriginal, operacionContableId, importeFormateado, referenciaFormateado, cuentaContableFormateada, cuentaContableId, movible, this.getIdCatalogoCuenta());
                //System.out.println("Formas.generarCuentasFormas.cuentaContableOriginal " + cuentaContableOriginal);
                //System.out.println("Formas.generarCuentasFormas.cuentaContableFormateada " + cuentaContableFormateada);
                posicionError++;//AQUI --- RegistroContable.getCuentas para obtener los elementos de configuracion
                if (this.getIdCatalogoCuenta() == 1) {
                    if (getListaConfiguracion() == null || getListaConfiguracion().isEmpty()) {
                        cuentaMayor = cuentaForma.getCuentaContableFormateada().substring(0, 4);
                    } else {
                        elementoConfiguracion = (ElementoConfiguracionCuenta) getListaConfiguracion().get(0);
                        cuentaMayor = cuentaForma.getCuentaContableFormateada().substring(0, elementoConfiguracion.getTamanio());
                    }
                    posicionError++;
                    //System.out.println("Formas.generarCuentasFormas.cuentaForma.cuentaContableId "+cuentaForma.getCuentaContableId());
                    if (cuentaForma.getCuentaContableId() == -1) {
                        if (getControlReg().getFechaAfectacion() == null) {
                            FechaAfectacion fechaAfectacion = new FechaAfectacion();
                            getControlReg().setFechaAfectacion(fechaAfectacion.obtenerFechaAfectacion());
                        }
                        posicionError++;
                        //System.out.println("No existe la cuenta: " + cuentaContableFormateada);
              /* for(int i=0; i < listaDetalleFormaContable.size(); i++){
                         // Verificar por nivel para verificar si el ultimo trae puro 0000 u otro valoy determinar el nivel
                         }*/
                        nivel = Integer.valueOf(obtenerNivel(cuentaContableFormateada));
                        posicionError++;
                        System.out.println("Fecha alta " + sia.libs.formato.Fecha.formatear(sia.libs.formato.Fecha.FECHA_CORTA, getControlReg().getFechaAfectacion()) + "de la cuenta contable " + cuentaContableFormateada);
                        if (insertarCuenta(conexion, cuentaMayor, cuentaForma.getCuentaContableFormateada(), nivel, "01/" + getFecha().substring(4, 6) + "/" + getFecha().substring(0, 4), "31/12/" + getFecha().substring(0, 4), sia.libs.formato.Fecha.formatear(sia.libs.formato.Fecha.FECHA_CORTA, getControlReg().getFechaAfectacion()), cuentaForma.getIdCatalogoCuenta())) {
                            System.out.println("Se inserto la cuenta: " + cuentaContableFormateada + " nivel " + nivel);
                            cuentaContableId = obtenerCuentacontableId(cuentaContableFormateada, conexion);
                            cuentaForma.setCuentaContableId(cuentaContableId);

                        }

                    }
                    posicionError++;
                    Cuenta cuenta = new Cuenta(cuentaContableId, conexion);
                    //actualizarNivel(conexion,cuentaContableId,cuenta.getNivel(),cuentaForma.getIdCatalogoCuenta());
                    //Empezar i=1 en el siguiente for
                    for (int i = 1; i < cuenta.getPadreId(cuenta.getNivel()).size(); i++) {
                        nivel = i + 1;
                        subCuenta = (SubCuenta) cuenta.getSubcuentas().get(i);
                        cuentaContableId = obtenerCuentacontableId(subCuenta.getCuentaContable(), conexion);
                        if (cuentaContableId == -1) {// && subCuenta.getCuentaPadre().getCuentaContableId() == -1){
                            getFecha();
                            getControlReg().getFechaAfectacion();
                            System.out.println("Fecha alta " + sia.libs.formato.Fecha.formatear(sia.libs.formato.Fecha.FECHA_CORTA, getControlReg().getFechaAfectacion()) + "de la cuenta contable " + subCuenta.getCuentaContable());
                            if (insertarCuenta(conexion, cuentaMayor, subCuenta.getCuentaContable(), nivel, "01/" + getFecha().substring(4, 6) + "/" + getFecha().substring(0, 4), "31/12/" + getFecha().substring(0, 4), sia.libs.formato.Fecha.formatear(sia.libs.formato.Fecha.FECHA_CORTA, getControlReg().getFechaAfectacion()), cuentaForma.getIdCatalogoCuenta())) {
                                System.out.println("Inserto cuentas padres: " + subCuenta.getCuentaContable() + " nivel " + nivel);
                            }
                        }
                    }
                    posicionError++;

                    /* for (int i = 0; i < listaCuenta.size(); i++) {
                     if (listaCuenta.get(i) != null) {
                     }
                     }*/
                    /* if(getCuentasNoExisten()==null){
                     getCuentasNoExisten().add(cuentaContableFormateada);
                     }
                     else{
                     for(int i=0; i<getCuentasNoExisten().size();i++){
                     if(cuentaContableFormateada.equals(getCuentasNoExisten().get(i)))
                     existe = true;
                     }
                     if(!existe)
                     getCuentasNoExisten().add(cuentaContableFormateada);
                     }*/

                    //throw new Exception("No existe la cuenta "+ cuentaContableFormateada);

                } else if (cuentaForma.getCuentaContableId() == -1) {
                    throw new Exception("No se encuentra la cuenta contable " + cuentaContableFormateada);
                }
                /* else{
                 if(getIdCatalogoCuenta()!=2 || !getFormaContableId().equals("1547") || !getFormaContableId().equals("1548") || !getFormaContableId().equals("1549") || !getFormaContableId().equals("1550")){
                 System.out.println("Cuenta contable: "+cuentaContableFormateada);
                 unidadEjecutora = cuentaContableFormateada.substring(10,13);
                 programa = cuentaContableFormateada.substring(5,9);
                 entidad = cuentaContableFormateada.substring(14,16);
                 ambito = cuentaContableFormateada.substring(16,17);
                 System.out.println("Verfica si el mes este abierto o ya esta cerrado definitivamente");
                 System.out.println("Unidad Ejecutora: "+unidadEjecutora+" Programa: "+programa+" Entidad: "+entidad+" Ambito: "+ambito+" Fecha: "+getFecha());
                 cierreMensual.select_cierre_mensual(conexion,unidadEjecutora,ambito,entidad,getEjercicio(),String.valueOf(getIdCatalogoCuenta()),getFecha().substring(4,6), programa,"0");
                 if(cierreMensual.getEstatus_cierre_id().equals("0")){
                 cierreMensual.select_cierre_mensual(conexion,unidadEjecutora,ambito,entidad,getEjercicio(),String.valueOf(getIdCatalogoCuenta()),getFecha().substring(4,6), programa,"2");
                 if(cierreMensual.getEstatus_cierre_id().equals("2"))
                 throw new Exception("El mes " + getFecha().substring(4,6) + " UE "+ unidadEjecutora +" Entindad " +entidad+ " Ambito " +ambito+" y Programa "+ programa +" ya se encuentra cerrado definitivamente");    
                 }
                 else{
                 throw new Exception("No se encuentra el mes " + getFecha().substring(4,6) + " UE "+ unidadEjecutora +" Entidad " +entidad+ " Ambito " +ambito+" y Programa "+ programa +" abierto para aferctar");            
                 }
                 }
                 }*/
            } else {
                //System.out.println("Formas.generarCuentasFormas validarVariables NO OK");
                //System.out.println("Formas.generarCuentasFormas.cuentaContableOriginal "+cuentaContableOriginal);
                cuentaForma = new CuentasForma(cuentaContableOriginal, operacionContableId, importeFormateado, referenciaFormateado, cuentaContableFormateada, cuentaContableId, movible, this.getIdCatalogoCuenta());
            }
        } catch (Exception e) {
            System.out.println(posicionError + " Ocurrio un error al accesar al metodo generarCuentasFormas " + e.getClass().getName() + ": " + e.getMessage());
            throw e;
        } finally {
            /* if(conexion!=null)
             conexion.close();
             conexion = null;*/
        }
        return cuentaForma;
    }

    private boolean validarVariables(String secuenciaFormaId, String variables) throws Exception {
        boolean existe = false;
        Sentencias sentencia;
        String codigo;
        //System.out.println("Formas.validarVariables.secuenciaFormaId "+secuenciaFormaId);
        //System.out.println("Formas.validarVariables.variables "+variables);
        String variable[] = variables.split("\\|");
        List<String> variableImporte;

        try {
            variableImporte = new ArrayList<String>();
            for (int i = 0; i < variable.length; i++) {
                int contador = 0;
                //System.out.println("Formas.validarVariables.variable["+i+"] "+variable[i]);        
                if (variable[i].startsWith("IMPORTE") || variable[i].startsWith("IVA")) {
                    if (variable[i].split("=")[1].equals("0") && contador == 0) {
                        contador++;
                    } else {
                        if (variable[i].split("=")[1].equals("0.0") && contador == 0) {
                            contador++;
                        } else {
                            if (variable[i].split("=")[1].equals("0.00") && contador == 0) {
                                contador++;
                            } else {
                                if (variable[i].split("=")[1].equals("-0.0") && contador == 0) {
                                    contador++;
                                }
                            }
                        }
                    }
                    if (contador == 0) {
                        variableImporte.add(variable[i].split("=")[0]);
                    }
                }
            }
            sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD, Sentencias.XML);
            codigo = sentencia.consultar("formasContables.select.variablesForma", "formaId=".concat(getFormaContableId().concat("|secuenciaId=").concat(secuenciaFormaId).concat("|"))).toString();
            if (codigo != null) {
                for (int x = 0; x < variableImporte.size(); x++) {
                    if (codigo.equals(variableImporte.get(x))) {
                        existe = true;
                        //System.out.println("Formas.validarVariables.codigo(" + x + ") " + codigo);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo validarVariables " + e.getMessage());
            throw e;
        }
        return existe;
    }

    private String obtenerCadena(List listaValores, boolean reemplazar, String cadenaVariables) throws Exception {
        Iterator iteratorLista = listaValores.iterator();
        StringBuilder cadena = new StringBuilder();
        ElementoFormaContable elemento;
        ElementoConfiguracionCuenta elementoConfiguracion;
        HttpServletRequest request = null;
        ControlRegistro controlRegistro;
        int nivel = 0;
        int posicionError = 0;
        //int totalNiveles = obtenerMaximoNivel();
        try {
            if (getControlReg() == null) {
                //  request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();  
                controlRegistro = (ControlRegistro) request.getSession().getAttribute("controlRegistro");
                setControlReg(controlRegistro);
            }
            posicionError++;
            while (iteratorLista.hasNext()) {
                posicionError = 1;
                elemento = (ElementoFormaContable) iteratorLista.next();
                posicionError++;
                nivel = elemento.getNivel();
                posicionError++;
                if (reemplazar) {
                    elementoConfiguracion = obtenerElementoConfiguracion(nivel);
                    posicionError++;
                    //System.out.println("Formas.obtenerCadena.obtenerElementoConfiguracion("+nivel+") "+elementoConfiguracion);
                    posicionError++;
                    if (elementoConfiguracion == null) {
                        if (elemento.getValor().contains("{")) {
                            for (String segmento : cadenaVariables.split(Pattern.quote("|"))) {
                                if (elemento.getValor().contains(segmento.split(Pattern.quote("="))[0])) {
                                    cadena.append(segmento.split(Pattern.quote("="))[1]);
                                }
                            }
                        } else {
                            cadena.append(elemento.getValor());
                        }
                    } else {
                        cadena.append(Cadena.rellenar(reemplazarValores(cadenaVariables, elemento.getValor()), elementoConfiguracion.getTamanio(), elementoConfiguracion.getCaracter().charAt(0), true).trim());
                    }
                } else {
                    cadena.append(elemento.getValor());
                }
                posicionError++;
            }
            posicionError++;
            for (int i = nivel; i < getControlReg().getNivelMaximo(); i++) {
                cadena.append("0000");
            }
            posicionError++;

            return cadena.toString();
        } catch (Exception e) {
            throw new Exception("Formas.obtenerCadena(" + posicionError + ") " + e.getClass().getName() + ": " + e.getMessage());
        }
    }

    /*public int obtenerMaximoNivel(){
     Sentencias sentencias      = null;
     Map parametros = null;
     int maximoNivel = -1;
     try {
     parametros = new HashMap();
     sentencias = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD);
     parametros.put("idCatalogoCuenta", getIdCatalogoCuenta());
     parametros.put("ejercicio",getEjercicio());
     maximoNivel = Integer.valueOf(sentencias.consultar("clasificadorCuenta.select.obtenerMaxNivel",parametros).toString()); 
     }
     catch (Exception e) {
     Error.mensaje(e, "CONTABILIDAD");
     }
     finally { 
     parametros = null;
     sentencias = null;
     }
     return maximoNivel;
     }*/
    private ElementoConfiguracionCuenta obtenerElementoConfiguracion(int nivel) {
        Iterator iteratorLista = getListaConfiguracion().iterator();
        ElementoConfiguracionCuenta elemento = null;
        boolean existe = false;
        while (iteratorLista.hasNext() && !existe) {
            ElementoConfiguracionCuenta elementoAux = (ElementoConfiguracionCuenta) iteratorLista.next();
            //System.out.println("Formas.obtenerElementoConfiguracion.elemento "+elementoAux.getCaracter());
            //System.out.println("Formas.obtenerElementoConfiguracion.elemento.orden "+elementoAux.getOrden());
            if (elementoAux.getOrden() == nivel) {
                existe = true;
                elemento = elementoAux;
            }
        }
        return elemento;
    }

    private int getTamanioCuenta() {
        Iterator iteratorLista = getListaConfiguracion().iterator();
        ElementoConfiguracionCuenta elemento;
        int tamanio = 0;
        while (iteratorLista.hasNext()) {
            elemento = (ElementoConfiguracionCuenta) iteratorLista.next();
            tamanio = tamanio + elemento.getTamanio();
        }
        return tamanio;
    }

    private void cargarConfiguracion(String cuentaMayor) {
        Formatos formatos;
        Sentencias sentencias;
        List<Vista> vista;
        int orden;
        int tamanio;
        String caracter;
        getListaConfiguracion().clear();
        try {
            try {
                Integer.parseInt(cuentaMayor);
                formatos = new Formatos(Contabilidad.getPropiedad("configuracionClaves.query.obtenerDetalleConfiguracionCuentaMayor"), cuentaMayor);
                sentencias = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD);
                System.out.println("Formas.cargarConfiguracion().formatos.getSentencia() "+formatos.getSentencia());
                vista = sentencias.registros(formatos.getSentencia());
                for (Vista reg : vista) {
                    orden = Integer.parseInt(reg.getField("ORDEN"));
                    tamanio = Integer.parseInt(reg.getField("TAMANIO"));
                    caracter = reg.getField("CARACTER");
                    //System.out.println("Formas.cargarConfiguracion.orden "+orden);
                    getListaConfiguracion().add(new ElementoConfiguracionCuenta(orden, tamanio, caracter));
                }
            } catch(Exception e) {
                getListaConfiguracion().add(new ElementoConfiguracionCuenta(1, 4, "0"));
            }
        } catch (Exception e) {
            Error.mensaje(e, "CONTABILIDAD");
        }
    }

    private String reemplazarValores(String cadenaVariables, String cadenaOrigen) {
        Variables variables;
        Formatos formatos;
        variables = new Variables(cadenaVariables);
        formatos = new Formatos(cadenaOrigen, variables.getMap());
        return formatos.getSentencia();
    }

    public int obtenerCuentacontableId(String cuentaContable, Connection conexion) {
        Formatos formatos;
        Sentencias sentencias;
        List<Vista> vista;
        int cuentaContableId = -1;
        try {
            formatos = new Formatos(Contabilidad.getPropiedad("formasContables.query.obtenerCuentaContableId"), cuentaContable, this.getEjercicio(), String.valueOf(this.getIdCatalogoCuenta()));
            sentencias = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD);
            vista = sentencias.registros(formatos.getSentencia(), conexion);
            if (vista != null) {
                for (Vista reg : vista) {
                    cuentaContableId = Integer.parseInt(reg.getField("CUENTA_CONTABLE_ID"));
                }
            }
        } catch (Exception e) {
            Error.mensaje(e, "CONTABILIDAD");
        } finally {
        }
        return cuentaContableId;
    }

    private void setFormaContableId(String formaContableId) {
        this.formaContableId = formaContableId;
    }

    public String getFormaContableId() {
        return formaContableId;
    }

    private void setListaConfiguracion(List listaConfiguracion) {
        this.listaConfiguracion = listaConfiguracion;
    }

    private List getListaConfiguracion() {
        return listaConfiguracion;
    }

    public void setTipoProceso(int tipoProceso) {
        if (tipoProceso <= 7) {
            this.tipoProceso = Areas.MANUAL;
        } else {
            if ((tipoProceso == Areas.AR) || (tipoProceso == Areas.INGRESOS_POR_VENTA) || (tipoProceso == Areas.MIPF) || (tipoProceso == Areas.VENTAS) || (tipoProceso == Areas.MIPF_NO_ACUMULADO)) {
                this.tipoProceso = tipoProceso;
            }
        }
        /*if ((tipoProceso<0 || tipoProceso>7) | (tipoProceso!=Areas.INGRESOS_POR_VENTA )) 
         this.tipoProceso = Areas.MANUAL;
         else 
         this.tipoProceso = tipoProceso;*/

    }

    public int getTipoProceso() {
        return tipoProceso;
    }

    private void setListaCuentasContables(List listaCuentas) {
        this.listaCuentasContables = listaCuentas;
    }

    private List getListaCuentasContables() {
        return listaCuentasContables;
    }

    public void setEjercicio(String ejercicio) {
        this.ejercicio = ejercicio;
    }

    public String getEjercicio() {
        return ejercicio;
    }

    public void setIdCatalogoCuenta(int idCatalogoCuenta) {
        this.idCatalogoCuenta = idCatalogoCuenta;
    }

    public int getIdCatalogoCuenta() {
        return idCatalogoCuenta;
    }

    public void setCuentasNoExisten(List cuentasNoExisten) {
        this.cuentasNoExisten = cuentasNoExisten;
    }

    public List getCuentasNoExisten() {
        return cuentasNoExisten;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getFecha() {
        return fecha;
    }

    public void setControlReg(ControlRegistro controlReg) {
        this.controlReg = controlReg;
    }

    public ControlRegistro getControlReg() {
        return controlReg;
    }
}
