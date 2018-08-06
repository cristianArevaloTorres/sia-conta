package sia.rf.contabilidad.registroContable.procesos.polizas.servicios;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import sia.db.dao.DaoFactory;
import sia.db.sql.Sentencias;
import sia.libs.formato.Error;
import sia.libs.formato.Fecha;
import sia.libs.formato.Numero;
import sia.libs.recurso.Propiedades;
import sia.rf.contabilidad.registroContable.servicios.Cuenta;
import sia.rf.contabilidad.registroContableEvento.bcEstadoCatalogo;
import sia.rf.contabilidad.registroContableNuevo.bcCierresMensuales;
import sia.rf.contabilidad.registroContableNuevo.bcCuentaContable;
import sia.rf.contabilidad.registroContableNuevo.bcDetallePoliza;
import sia.ws.publicar.contabilidad.excepciones.PolizaException;

public class Polizas extends ArrayList<DetallePoliza> {
    public static final String AMDEF = 
        "sia.rf.contabilidad.registroContable.procesos.polizas.servicios.ModuloPolizas";
    public static final String CONFIG = "ModuloPolizasLocal";
    public static final int POLIZA_DIARIO = 1;
    public static final int POLIZA_CHEQUE = 2;
    public static final int POLIZA_EGRESO = 3;
    public static final int POLIZA_INGRESO = 4;
    public static final int POLIZA_ELIMINACION = 5;
    public static final int REGISTRAR_POLIZA = 1;
    public static final int ACTUALIZAR_POLIZA = 2;
    public static final int POLIZA_ABIERTA = 1;
    public static final int POLIZA_CANCELACEDA = 2;
    public static final int POLIZA_DE_CANCELACION = 3;
    public static final int EXISTE_POLIZA = 0;
    public static final int CATALOGO_INSTITUCIONAL = 1;
    public static final int CATALOGO_INGRESOS_POR_VENTA = 2;
    public static final int CATALOGO_COSTOS = 3;
    private String unidadEjecutora;
    private String ambito;
    private String pais;
    private int entidad;
    private int consecutivo;
    private int tipoPolizaId;
    private int maestroOperacionId;
    private String fecha;
    private String concepto;
    private String referencia;
    private String fechaAfectacion;
    private int clasificacionPolizaId;
    private int polizaRererencia;
    private int numEmpleado;
    private int global;
    private String ejercicio;
    private int polizaId;
    private int mes;
    private String origen;
    private int catalogoCuenta;
    private boolean amCreado;
    private String idEvento;

    public Polizas(int catalogoCuenta, String ejercicio) { 
        this("", "", "", 0, 0, 0, 0, 0, "", "", "", "", ejercicio, 0, "", catalogoCuenta,null);
    }

    public Polizas() {
        this("0", "0", "0", 0, 0, 0, 0, 0, "", "", "", "", "", 0, "", 0,null);
    }

    public Polizas(String unidadEjecutora, String ambito, String pais, int entidad, int tipoPolizaId, int maestroOperacionId, int clasificacionPolizaId, int numEmpleado, String fecha, String fechaAfectacion, String referencia, String concepto, String ejercicio, int mes, String origen, int catalogoCuenta, String idEvento) {
        this(unidadEjecutora, ambito, pais, entidad, tipoPolizaId, maestroOperacionId, clasificacionPolizaId, numEmpleado, fecha, fechaAfectacion, referencia, concepto, ejercicio, mes, 0, origen, catalogoCuenta, idEvento);
    }

    public Polizas(String unidadEjecutora, String ambito, String pais, int entidad, int tipoPolizaId, int maestroOperacionId, int clasificacionPolizaId, int numEmpleado, String fecha, String fechaAfectacion, String referencia, String concepto, String ejercicio, int mes, int polizaReferencia, String origen, int catalogoCuenta, String idEvento) {
        this.clear();
        this.setUnidadEjecutora(unidadEjecutora);
        this.setAmbito(ambito);
        this.setPais(pais);
        this.setEntidad(entidad);
        this.setTipoPolizaId(tipoPolizaId);
        this.setMaestroOperacionId(maestroOperacionId);
        this.setClasificacionPolizaId(clasificacionPolizaId);
        this.setNumEmpleado(numEmpleado);
        this.setFecha(fecha);
        this.setFechaAfectacion(fechaAfectacion);
        this.setReferencia(referencia);
        this.setConcepto(concepto);
        this.setConsecutivo(0);
        this.setGlobal(0);
        this.setEjercicio(ejercicio);
        this.setMes(mes);
        this.setPolizaRererencia(polizaReferencia);
        this.setOrigen(origen);
        this.setCatalogoCuenta(catalogoCuenta);
        this.amCreado = false;
        this.setIdEvento(idEvento);
    }

    public void setUnidadEjecutora(String unidadEjecutora) {
        this.unidadEjecutora = unidadEjecutora;
    }

    public String getUnidadEjecutora() {
        return unidadEjecutora;
    }

    public void setAmbito(String ambito) {
        this.ambito = ambito;
    }

    public String getAmbito() {
        return ambito;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getPais() {
        return pais;
    }

    public void setEntidad(int entidad) {
        this.entidad = entidad;
    }

    public int getEntidad() {
        return entidad;
    }

    public void setConsecutivo(int consecutivo) {
        this.consecutivo = consecutivo;
    }

    public int getConsecutivo() {
        return consecutivo;
    }

    public void setTipoPolizaId(int tipopolizaId) {
        this.tipoPolizaId = tipopolizaId;
    }

    public int getTipoPolizaId() {
        return tipoPolizaId;
    }

    public void setMaestroOperacionId(int maestroOperacionId) {
        this.maestroOperacionId = maestroOperacionId;
    }

    public int getMaestroOperacionId() {
        return maestroOperacionId;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getFecha() {
        return fecha;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setFechaAfectacion(String fechaAfectacion) {
        this.fechaAfectacion = fechaAfectacion;
    }

    public String getFechaAfectacion() {
        return fechaAfectacion;
    }

    public void setClasificacionPolizaId(int clasificacionPolizaId) {
        this.clasificacionPolizaId = clasificacionPolizaId;
    }

    public int getClasificacionPolizaId() {
        return clasificacionPolizaId;
    }

    public void setPolizaRererencia(int polizaRererencia) {
        this.polizaRererencia = polizaRererencia;
    }

    public int getPolizaRererencia() {
        return polizaRererencia;
    }

    public void setNumEmpleado(int numEmpleado) {
        this.numEmpleado = numEmpleado;
    }

    public int getNumEmpleado() {
        return numEmpleado;
    }

    private void setGlobal(int global) {
        this.global = global;
    }

    public int getGlobal() {
        return global;
    }


    public void setEjercicio(String ejercicio) {
        this.ejercicio = ejercicio;
    }

    public String getEjercicio() {
        return ejercicio;
    }

    private void setPolizaId(int polizaId) {
        this.polizaId = polizaId;
    }

    public int getPolizaId() {
        return polizaId;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getMes() {
        return mes;
    }

    public void setAmCreado(boolean amCreado) {
        this.amCreado = amCreado;
    }

    public boolean isAmCreado() {
        return amCreado;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getOrigen() {
        return origen;
    }

    public void setCatalogoCuenta(int catalogoCuenta) {
        this.catalogoCuenta = catalogoCuenta;
    }

    public int getCatalogoCuenta() {
        return catalogoCuenta;
    }

  public void setIdEvento(String idEvento) {
    this.idEvento = idEvento;
  }

  public String getIdEvento() {
    return idEvento;
  }
    
    
    
    public int obtenerConsecutivo(Connection con,String unidadEjecutora, String ambito, int entidad, int tipoPolizaId, int mes, String ejercicio) {
        int consecutivo = 0;
        Sentencias sentencia;
        Map parametros = new HashMap();
        try {
            sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD, Sentencias.XML);
            parametros.put("unidadEjecutora", unidadEjecutora);
            parametros.put("ambito", ambito);
            parametros.put("entidad", entidad);
            parametros.put("tipoPolizaId", tipoPolizaId);
            parametros.put("mes", mes);
            parametros.put("ejercicio", ejercicio);
            parametros.put("idCatalogoCuenta", getCatalogoCuenta());
            consecutivo = Integer.valueOf(sentencia.consultar(con,"webService.select.obtenerConsecutivo", parametros).toString());
        } catch (Exception e) {
            System.out.println("Error al obtener consecutivo " + e.getMessage());
            System.err.println(Error.getMensaje(this, "SIAFM", "obtenerConsecutivo", e.getMessage()));
        }
        return consecutivo;
    }


/*    public int obtenerConsecutivo(ApplicationModule am, String unidadEjecutora, String ambito, int entidad, int tipoPolizaId, int mes, String ejercicio) {
        int consecutivo = 0;
        Formatos formatos = null;
        ViewObject VistaConsecutivoAfectacion = null;
        try {
            formatos = new Formatos(Contabilidad.getInstance().getPropiedad("procesos.polizas.obtenerConsecutivo"), unidadEjecutora, ambito, entidad, tipoPolizaId, mes, ejercicio, getCatalogoCuenta());
            VistaConsecutivoAfectacion = am.findViewObject("VistaConsecutivoAfectacion");
            if (VistaConsecutivoAfectacion != null) {
                VistaConsecutivoAfectacion.remove();
            }
            VistaConsecutivoAfectacion = am.createViewObjectFromQueryStmt("VistaConsecutivoAfectacion", formatos.getSentencia());
            VistaConsecutivoAfectacion.executeQuery();
            consecutivo = ((Number)VistaConsecutivoAfectacion.first().getAttribute("CONSECUTIVO")).intValue();
            VistaConsecutivoAfectacion = null;
        } catch (Exception e) {
            System.err.println(Error.getMensaje(this, "SIAFM", "obtenerConsecutivo", e.getMessage()));
        } finally {
            if (VistaConsecutivoAfectacion != null)
                VistaConsecutivoAfectacion.remove();
            formatos = null;
        }
        return consecutivo;
    }
*/

/*    public boolean cuentaTienePolizasAsociadas(ApplicationModule am, String ejercicio, String mes, int cuentaContableId) {
        ViewObject vistaPolizas = null;
        try {
            vistaPolizas = am.findViewObject("VistaRfTrPolizas1");
            vistaPolizas.setNamedWhereClauseParam("pcuenta", cuentaContableId);
            vistaPolizas.setNamedWhereClauseParam("pejercicio", ejercicio);
            vistaPolizas.setNamedWhereClauseParam("pmes", mes);
            vistaPolizas.executeQuery();
        } catch (Exception e) {
            Error.mensaje(e, "SIAFM");
        }
        return vistaPolizas.getRowCount() > 0 ? true : false;
    }*/

    public int obtenerConsecutivoGlobal(Connection con) throws Exception {
        int consGlobal = 0;
        Sentencias sentencia;
        try {
            sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD, Sentencias.XML);
            consGlobal = Integer.valueOf(sentencia.consultar(con,"webService.select.obtenerConsecutivoGlobal", "ejercicio=".concat(this.getEjercicio())).toString());
        } catch (Exception e) {
            System.err.println(Error.getMensaje(this, "SIAFM", "obtenerConsecutivoGlobal", e.getMessage()));
            e.printStackTrace();
            throw new Exception("Error al obtener el consecutivo global");
        }
        return consGlobal;
    }


 /*   public int obtenerConsecutivoGlobal(ApplicationModule am) throws Exception {
        ViewObject VistaConsecutivoGlobal = null;
        Formatos formatos = null;
        int consGlobal = 0;
        try {
            formatos = new Formatos(Contabilidad.getInstance().getPropiedad("procesos.polizas.obtenerConsecutivoGlobal"), this.getEjercicio());
            VistaConsecutivoGlobal = am.findViewObject("VistaConsecutivoGlobal");
            if (VistaConsecutivoGlobal != null) {
                VistaConsecutivoGlobal.remove();
            }
            VistaConsecutivoGlobal = am.createViewObjectFromQueryStmt("VistaConsecutivoGlobal", formatos.getSentencia());
            VistaConsecutivoGlobal.executeQuery();
            consGlobal = ((Number)VistaConsecutivoGlobal.first().getAttribute("CONSGLOBAL")).intValue();
            VistaConsecutivoGlobal = null;
        } catch (Exception e) {
            System.err.println(Error.getMensaje(this, "SIAFM", "obtenerConsecutivoGlobal", e.getMessage()));
            e.printStackTrace();
            throw new Exception("Error al obtener el consecutivo global");
        } finally {
            if (VistaConsecutivoGlobal != null)
                VistaConsecutivoGlobal.remove();
            formatos = null;
        }
        return consGlobal;
    }
*/
    private int insertarRegistrosWs(Connection connection, int polizaId) throws Exception {
        Sentencias sentencia;
        Map parametros = new HashMap();
        String fecha;
        int consecutivoPoliza;
        StringBuffer query = new StringBuffer();
        try {
            consecutivoPoliza = this.obtenerConsecutivo(connection,getUnidadEjecutora(), getAmbito(), getEntidad(), getTipoPolizaId(), getMes(), getEjercicio());
            this.setConsecutivo(consecutivoPoliza);
            fecha = getFecha().substring(0, 4).concat("-").concat(getFecha().substring(4, 6)).concat("-").concat(getFecha().substring(6, 8)).concat(" ").concat(getFecha().substring(8, 10).concat(":").concat(getFecha().substring(10, 12)).concat(":").concat(getFecha().substring(12, 14)));
            sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD, Sentencias.XML);
            parametros.put("polizaId", polizaId);
            parametros.put("unidadEjecutora", getUnidadEjecutora());
            parametros.put("ambito", getAmbito());
            parametros.put("pais", getPais());
            parametros.put("entidad", getEntidad());
            parametros.put("consecutivo", consecutivoPoliza);
            parametros.put("global", this.obtenerConsecutivoGlobal(connection));
            parametros.put("ejercicio", getEjercicio());
            parametros.put("tipoPolizaId", getTipoPolizaId());
            parametros.put("maestroOperacionId", getMaestroOperacionId());
            //Ver que hace en la fecha
            parametros.put("fecha", fecha);
            parametros.put("concepto", getConcepto());
            parametros.put("referencia", getReferencia());
            parametros.put("fechaAfectacion", getFechaAfectacion().substring(0, 4) + "-" + getFechaAfectacion().substring(4, 6) + "-" + getFechaAfectacion().substring(6, 8));
            parametros.put("clasificacionPolizaId", getClasificacionPolizaId());
            parametros.put("polizaReferencia", getPolizaRererencia());
            parametros.put("numEmpleado", getNumEmpleado());
            parametros.put("mes", getMes());
            parametros.put("origen", getOrigen());
            parametros.put("idCatalogoCuenta", getCatalogoCuenta());
            parametros.put("idEvento", getIdEvento().equals("-1")?null:getIdEvento());
            parametros.put("ban_cheque", getReferencia().contains("CHEQUE NO.")?1:0);
            System.out.println("getReferencia: " + getReferencia() + getReferencia().contains("CHEQUE NO."));
            query.append(sentencia.getComando("webService.insert.insertarPoliza",parametros));
            polizaId = sentencia.ejecutar(connection, "webService.insert.insertarPoliza", parametros);
            //System.out.println(query);
        } catch (Exception e) {
            System.out.println(query);
            System.out.println("Error en metodo: insertarRegistrosWs. " + e.getMessage());            
            System.err.println(Error.getMensaje(this, "SIAFM", "insertarRegistros", e.getMessage()));
            connection.rollback();
            // e.printStackTrace(); SE COMENTO JLPN
            throw new Exception("Error al insertar la póliza");
        }
        return polizaId;
    }

 /*   private void insertarRegistros(ApplicationModule am, ViewObject vistaRfTrPolizas) throws Exception {
        Row regPoliza = null;
        String fecha = getFecha().substring(0, 4).concat("-").concat(getFecha().substring(4, 6)).concat("-").concat(getFecha().substring(6, 8)).concat(" ").concat(getFecha().substring(8, 10).concat(":").concat(getFecha().substring(10, 12)).concat(":").concat(getFecha().substring(12, 14)));
        regPoliza = vistaRfTrPolizas.createRow();
        regPoliza.setAttribute("UnidadEjecutora", getUnidadEjecutora());
        regPoliza.setAttribute("Ambito", getAmbito());
        regPoliza.setAttribute("Pais", getPais());
        regPoliza.setAttribute("Entidad", new Number(getEntidad()));
        regPoliza.setAttribute("Consecutivo", new Number(this.obtenerConsecutivo(am, getUnidadEjecutora(), getAmbito(), getEntidad(), getTipoPolizaId(), getMes(), getEjercicio())));
        this.setConsecutivo(((Number)regPoliza.getAttribute("Consecutivo")).intValue());
        regPoliza.setAttribute("Global", new Number(this.obtenerConsecutivoGlobal(am)));
        regPoliza.setAttribute("Ejercicio", new Number(getEjercicio()));
        regPoliza.setAttribute("TipoPolizaId", new Number(getTipoPolizaId()));
        regPoliza.setAttribute("MaestroOperacionId", new Number(getMaestroOperacionId()));
        regPoliza.setAttribute("Fecha", new Timestamp(fecha));
        regPoliza.setAttribute("Concepto", getConcepto());
        regPoliza.setAttribute("Referencia", getReferencia());
        regPoliza.setAttribute("FechaAfectacion", getFechaAfectacion().substring(0, 4) + "-" + getFechaAfectacion().substring(4, 6) + "-" + getFechaAfectacion().substring(6, 8));
        regPoliza.setAttribute("ClasificacionPolizaId", new Number(getClasificacionPolizaId()));
        regPoliza.setAttribute("PolizaReferencia", getPolizaRererencia());
        regPoliza.setAttribute("NumEmpleado", new Number(getNumEmpleado()));
        regPoliza.setAttribute("Mes", new Number(getMes()));
        regPoliza.setAttribute("Origen", getOrigen());
        regPoliza.setAttribute("IdCatalogoCuenta", getCatalogoCuenta());
        vistaRfTrPolizas.insertRow(regPoliza);
    }
*/
 /*   public void actualizaRegistro(ViewObject vistaRfTrPolizas) {
        Row regPoliza = null;
        String fecha = getFecha().substring(0, 4).concat("-").concat(getFecha().substring(4, 6)).concat("-").concat(getFecha().substring(6, 8)).concat(" ").concat(getFecha().substring(8,  10).concat(":").concat(getFecha().substring(10, 12)).concat(":").concat(getFecha().substring(12, 14)));
        regPoliza = vistaRfTrPolizas.first();
        regPoliza.setAttribute("Concepto", getConcepto());
        regPoliza.setAttribute("Referencia", getReferencia());
        regPoliza.setAttribute("NumEmpleado", new Number(getNumEmpleado()));
        regPoliza.setAttribute("MaestroOperacionId", new Number(getMaestroOperacionId()));
        regPoliza.setAttribute("Origen", getOrigen());
        regPoliza.setAttribute("FechaAfectacion", getFechaAfectacion().substring(0, 4) + "-" + getFechaAfectacion().substring(4, 6) + "-" + getFechaAfectacion().substring(6, 8));
        regPoliza.setAttribute("Fecha", new Timestamp(fecha));
        this.setConsecutivo(((Number)regPoliza.getAttribute("Consecutivo")).intValue());
    }
*/
    private int obtenerIntentos() {
        return Integer.parseInt(Propiedades.getInstance().getPropiedad("sistama.sia.intentos"));
    }

 /*   private ApplicationModule crearModulo() {
        this.setAmCreado(true);
        return Configuration.createRootApplicationModule(AMDEF, CONFIG);
    }*/

 /*   public boolean registrarPolizaAfectacion(ApplicationModule modulo, int tipoActualizacion) throws Exception {
        return registrarPolizaAfectacion(modulo, tipoActualizacion, -1);
    }*/

/*    public boolean registrarPolizaAfectacion(int tipoActualizacion) throws Exception {
        ApplicationModule modulo = null;
        boolean registro = false;
        try {
            modulo = this.crearModulo();
            registro = registrarPolizaAfectacion(modulo, tipoActualizacion, -1);
            modulo.getTransaction().commit();
        } catch (Exception e) {
            Error.mensaje(e, "CONTABILIDAD");
            throw e;
        } finally {
            if (isAmCreado()) {
                Configuration.releaseRootApplicationModule(modulo, false);
                modulo = null;
            }
        }
        return registro;

    }*/

    public boolean registrarPolizaAfectacionAutomaticas(Connection connection, int tipoActualizacion) throws SQLException, Exception {
        //ApplicationModule modulo=null;
        boolean registro = false;
        //Connection connection = null; Aqui la pase como parametro jlpn
        try {
            // connection = DaoFactory.getContabilidad(); //SE ELIMINO JLPN
            //System.out.println(connection + " abrir propagado");
            //modulo=this.crearModulo();
           // connection.setAutoCommit(false); ME LO LLEVE AL PRINCIPAL JLPN
            registro = registrarPolizaAfectacionWs(connection, tipoActualizacion, -1);
           // connection.commit(); JLPN
            //modulo.getTransaction().commit();
        } catch (Exception e) {
            Error.mensaje(e, "CONTABILIDAD");
            System.out.println("JLPN. Error en metodo registrarPolizaAfectacionAutomaticas. " + e.getMessage());
           // connection.rollback(); JLPN
            throw e;
        } finally {
            /*if(isAmCreado()){
        Configuration.releaseRootApplicationModule(modulo, false);
        modulo = null;
      }  */
           // if (connection != null) {
              //  System.out.println(connection + " cerrar propagado"); JLPN
             //   connection.close();
          //  }
          //  connection = null;
        }
        return registro;

    }

    // metodo para ser usado en el web service

    public boolean registrarPolizaAfectacionWs(Connection connection, int tipoActualizacion, int cuentaChequesId) throws Exception {
        Sentencias sentencia = null;
        Map parametros = new HashMap();
        boolean transaccion = true;
        int polizaId;
        bcDetallePoliza detallePoliza = null;
        try {
            sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD, Sentencias.XML);
            int intentos = obtenerIntentos();
            int contador = 0;
            detallePoliza = new bcDetallePoliza();
            String exception = "JBO-26048";
            while ((contador < intentos) && (exception.equals("JBO-26048"))) {
                try {
                    if (tipoActualizacion == REGISTRAR_POLIZA) { //Opcion Agregar
                        parametros.clear();
                        polizaId = Integer.valueOf(sentencia.consultar("webService.select.sencuenciaPoliza", parametros).toString());
                        setPolizaId(polizaId);
                        this.insertarRegistrosWs(connection, getPolizaId());
                        this.desglosarDetallePolizaWs(connection, this, "+");
                    } else {
                        this.desglosarDetallePolizaWs(connection,detallePoliza.select_rf_tr_detalle_poliza(String.valueOf(getPolizaId()),connection),"-");
                        detallePoliza.delete_rf_tr_detalle_poliza(connection, String.valueOf(getPolizaId()));
                        this.desglosarDetallePolizaWs(connection,this,"+");          
                    }
                    Double acumImporte = 0.00;
                    for (int i = 0; i < size(); i++) {
                        DetallePoliza detalle = get(i);
                        parametros.put("cuentaContableId", detalle.getCuentaContableId());
                        parametros.put("importe", Numero.redondea(detalle.getOperacionContable().getImporte(), 2));
                        parametros.put("operacionContableId", detalle.getOperacionContable().getId());
                        acumImporte = detalle.getOperacionContable().getId() == 1 ? detalle.getOperacionContable().getImporte() + acumImporte : acumImporte;
                        parametros.put("referencia", detalle.getReferencia());
                        parametros.put("fechaAfectacion", getFecha().substring(0, 4) + "-" + getFecha().substring(4, 6) + "-" + getFecha().substring(6, 8));
                        parametros.put("polizaId", getPolizaId());
                        sentencia.ejecutar(connection, "webService.select.insertarDetallePoliza", parametros);
                    } //for
                   break;
                } catch (Exception ex) {
                if(ex.getMessage().substring(21,22).equals("&")){
                    throw new Exception(ex.getMessage().substring(21,ex.getMessage().length()));
                }
                else{
                    ex.getMessage();
                    exception = (ex.getMessage().split(":"))[0];
                    contador++;
                    //vistaRfTrPolizas.clearCache();
                    ex.printStackTrace();
                }
                }
            } //while

            if ((contador >= intentos) || (!exception.equals("JBO-26048"))) {
                connection.rollback();
                //am.getTransaction().rollback();        
                transaccion = false;
                throw new Exception("Error se terminaron los intentos de registro");
            }
        } catch (Exception e) {
            System.err.println(Error.getMensaje(this, e, "SIAFM", "registrarPolizaAfectacion", e.getMessage()));
            //e.printStackTrace();
            connection.rollback();
            //am.getTransaction().rollback();   
            transaccion = false;
            throw new Exception(e);

        } //catch
        return transaccion;
    }


    // metodo para ser usado en normalmente

 /*   public boolean registrarPolizaAfectacion(ApplicationModule am, int tipoActualizacion, int cuentaChequesId) throws Exception {
        Row regDetalle = null;
        ViewObject vistaRfTrPolizas = null;
        ViewObject vistaDetalle = null;
        boolean transaccion = true;
        Cheque cheque = null;
        try {
            //System.out.println((AM.getDBTransaction().createStatement(0).getConnection()).getMetaData().getURL());      
            vistaRfTrPolizas = am.findViewObject("VistaRfTrPolizas1");
            int intentos = obtenerIntentos();
            int contador = 0;
            String exception = "JBO-26048";
            while ((contador < intentos) && (exception.equals("JBO-26048"))) {
                try {
                    if (tipoActualizacion == REGISTRAR_POLIZA) { //Opcion Agregar
                        this.insertarRegistros(am, vistaRfTrPolizas);
                        this.desglosarDetallePoliza(am, this, "+");
                    } else {
                        this.obtenerRegistroActualPoliza(vistaRfTrPolizas);
                        this.actualizaRegistro(vistaRfTrPolizas);
                        this.desglosarDetallePoliza(am, this.obtenerDetallePoliza(am, getPolizaId()), "-");
                        this.eliminarDetallePoliza(am, this.getPolizaId());
                        this.desglosarDetallePoliza(am, this, "+");
                    }
                    vistaDetalle = am.findViewObject("VistaRfTrDetallePoliza1");
                    Double acumImporte = 0.00;
                    for (int i = 0; i < size(); i++) {
                        regDetalle = vistaDetalle.createRow();
                        DetallePoliza detalle = get(i);
                        regDetalle.setAttribute("CuentaContableId", detalle.getCuentaContableId());
                        regDetalle.setAttribute("Importe", Numero.redondea(detalle.getOperacionContable().getImporte(), 2));
                        regDetalle.setAttribute("OperacionContableId", detalle.getOperacionContable().getId());
                        acumImporte = detalle.getOperacionContable().getId() == 1 ? detalle.getOperacionContable().getImporte() +  acumImporte : acumImporte;
                        regDetalle.setAttribute("Referencia", detalle.getReferencia());
                        regDetalle.setAttribute("FechaAfectacion", new Date(getFecha().substring(0, 4) + "-" + getFecha().substring(4, 6) + "-" + getFecha().substring(6, 8)));
                        vistaDetalle.insertRow(regDetalle);
                    } //for
                    if (tipoActualizacion == REGISTRAR_POLIZA) { //Opcion Agregar
                        if (cuentaChequesId != -1) {
                            cheque = new Cheque();
                            cheque.setAM((ApplicationModuleImpl)am);
                            am.getTransaction().postChanges();
                            cheque.verificarGuardarSobrePoliza(fechaAfectacion, acumImporte, cuentaChequesId);
                        }
                    }
                    am.getTransaction().postChanges();
                    this.setPolizaId(((DBSequence)vistaRfTrPolizas.getCurrentRow().getAttribute("PolizaId")).getSequenceNumber().intValue());
                    vistaRfTrPolizas.clearCache();
                    break;
                } catch (Exception ex) {
                    ex.getMessage();
                    exception = (ex.getMessage().split(":"))[0];
                    contador++;
                    vistaRfTrPolizas.clearCache();
                    ex.printStackTrace();
                }
            } //while

            if ((contador >= intentos) || (!exception.equals("JBO-26048"))) {
                am.getTransaction().rollback();
                transaccion = false;
                throw new Exception("Error se terminaron los intentos de registro");
            }
        } catch (Exception e) {
            System.err.println(Error.getMensaje(this, e, "SIAFM", "registrarPolizaAfectacion", e.getMessage()));
            e.printStackTrace();
            am.getTransaction().rollback();
            transaccion = false;
            throw new Exception(e);

        } //catch
        finally {
            if (amCreado) {
                //AM.remove();
                //Configuration.releaseRootApplicationModule(Am, false);
                am = null;
            }
            regDetalle = null;
            vistaRfTrPolizas = null;
        }
        return transaccion;
    }
*/
    public void addDetalle(List<DetallePoliza> detallePoliza) {
        addAll(detallePoliza);
    }

/*    public ViewObject agregarCuentasOperacion99(ModuloPolizasImpl moduloPolizas, int cuentaContableId, int operacionContable, int maestroOperacionId) throws Exception {
        Row registro = null;
        VistaDetalleOperacionesImpl vistaDetalleOperaciones = null;
        ;
        int consecutivo = 0;
        try {
            vistaDetalleOperaciones = moduloPolizas.getVistaDetalleOperaciones();
            consecutivo = vistaDetalleOperaciones.getRowCount();
            registro = vistaDetalleOperaciones.createRow();
            registro.setAttribute("CUENTA_CONTABLE_ID", cuentaContableId);
            registro.setAttribute("OPERACION_CONTABLE_ID", operacionContable);
            registro.setAttribute("MAESTRO_OPERACION_ID", maestroOperacionId);
            registro.setAttribute("CONS_SELECCION", consecutivo + 1);
            vistaDetalleOperaciones.insertRow(registro);
            registro = null;
            return vistaDetalleOperaciones;
        } catch (Exception e) {
            System.err.print(Error.getMensaje(this, "SIAFM", "agregarCuentasOperacion99", e.getMessage()));
            return null;
        } finally {
            //Configuration.releaseRootApplicationModule(moduloPolizas, true);
        }
    }*/
/*
    public int obtenerMaestroOperacion(String unidadEjecutora, String entidad, String ambito, String consecutivo) throws Exception {
        ViewObject voMaestroOperacion = null;
        int maestroOperacionID = 0;
        UtileriasComun utileria = null;
        Formatos formatos = null;
        try {
            utileria = new UtileriasComun("sia.rf.contabilidad.registroContable.procesos.polizas.servicios", "ModuloPolizas");
            formatos = new Formatos(Contabilidad.getInstance().getPropiedad("procesos.polizas.obtenerMaestroOperacion"), unidadEjecutora, entidad, ambito, consecutivo, this.getCatalogoCuenta(), this.getEjercicio());
            utileria.removerVistas("Nombrex");
            voMaestroOperacion = utileria.createViewObjectFromSentencia(formatos.getSentencia());
            maestroOperacionID = voMaestroOperacion.first() == null ? 0 : ((Number)voMaestroOperacion.first().getAttribute("MAESTRO_OPERACION_ID")).intValue();
        } //try
        catch (Exception e) {
            Error.mensaje(e, "SIAFM");
            throw new Exception("Fallo al obtener el maestro operacion para la poliza");
        } //catch
        finally {
            if (voMaestroOperacion != null) {
                voMaestroOperacion.remove();
                voMaestroOperacion = null;
            }
            utileria.removerVistas("Nombrex");
            utileria = null;
            formatos = null;
        } //finally
        return maestroOperacionID;
    }
*/
  public void posicionarPoliza(int polizaId) {
    this.setPolizaId(polizaId);
  }
/*
  private void obtenerRegistroActualPoliza(ViewObject vistaRfTrPoliza) {
    try {
      vistaRfTrPoliza.setWhereClause("Poliza_Id=" + this.getPolizaId());
      vistaRfTrPoliza.executeQuery();
    } //try
    catch (Exception e) {
      System.err.print(Error.getMensaje(this, "SIAFM", "obtenerRegistroActualPoliza", e.getMessage()));
      e.printStackTrace();
    } //catch
  }
  */
/*
  // metodo usado para proceso normal
  public void desglosarDetallePoliza(ApplicationModule moduloPolizas, List<DetallePoliza> detallePoliza, String signo) throws Exception {
    try {
      for (int i = 0; i < detallePoliza.size(); i++) {
        DetallePoliza detalle = detallePoliza.get(i);
        OperacionContable operacion = detalle.getOperacionContable();
       // this.registrarImporteCuenta(moduloPolizas, detalle.getCuentaContableId(), operacion.getImporte(), operacion.getCampo(), Fecha.formatear(Fecha.FECHA_ESTANDAR, getFecha()), signo);
      } //end (int i = 0; i < detallePoliza.size(); i++)
    } //end try
    catch (Exception e) {
      System.err.println(Error.getMensaje(this, "SIAFM", "desglosarDetallePoliza", e.getMessage()));
      throw new Exception(e);
    } //end catch(Exception e)    
  } //end public void desglosarDetallePoliza(List<DetallePoliza> detallePoliza){
*/
  // metodo para el web service
  public void desglosarDetallePolizaWs(Connection connection, List<DetallePoliza> detallePoliza, String signo) throws Exception {
      bcCierresMensuales cierreMensual;
      String unidadEjecutora = null;
      String programa = null;
      String entidad = null;
      List<String> registros;
      boolean actualizar = false;
      bcEstadoCatalogo bcEstadoCat;
      bcCuentaContable  bcCtaContable;
      String cuentaContable;
      boolean abierto = false;
      String cadenaEstatus = "";
      int posicionError = 0;
      try {
        cierreMensual = new   bcCierresMensuales();
        bcEstadoCat = new bcEstadoCatalogo();
        bcCtaContable = new bcCuentaContable();
        for (int i = 0; i < detallePoliza.size(); i++) {
          DetallePoliza detalle = detallePoliza.get(i);
          OperacionContable operacion = detalle.getOperacionContable();
          //System.out.println("Polizas.desglosarDetallePolizaWs.idEvento "+getIdEvento());
            if(!(getIdEvento().equals("300") || getIdEvento().equals("301") || getIdEvento().equals("302"))){
             bcCtaContable.select_rf_tr_cuentas_contables(connection,String.valueOf(detalle.getCuentaContableId()));
              cuentaContable = bcCtaContable.getCuenta_contable();
              //System.out.println("Cuenta contable: "+cuentaContable);
              unidadEjecutora = cuentaContable.substring(10,13);
              programa = cuentaContable.substring(5,9);
              entidad = cuentaContable.substring(14,16);
              ambito = cuentaContable.substring(16,17);
              //System.out.println("Verfica si el mes est� abierto o ya esta cerrado definitivamente");
              //System.out.println("Unidad Ejecutora: "+unidadEjecutora+" Programa: "+programa+" Entidad: "+entidad+" Ambito: "+ambito+" Fecha: "+getFecha());
               registros = cierreMensual.select_cierre_mensual(connection,unidadEjecutora,ambito,entidad,getEjercicio(),String.valueOf(getCatalogoCuenta()),getFecha().substring(4,6), programa);
               for(String x: registros){
                   cadenaEstatus = cadenaEstatus + x + "|";         
               }
               
               if(cadenaEstatus.indexOf("0")!=-1){
                  if(cadenaEstatus.indexOf("2")!=-1) {
                       actualizar = true;
                   }
               }
               else{
                 actualizar = true;
               }
            }    
           // System.out.println("Polizas.desglosarDetallePolizaWs.actualizar "+actualizar);
          posicionError++;
            if(actualizar){
                 throw new Exception("&, No se encuentra el mes " + getFecha().substring(4,6) + " UE "+ unidadEjecutora +" Entidad " +entidad+ " Ambito " +ambito+" y Programa "+ programa +" disponible para aferctar o ya se encuentra cerrado definitivamente");            
               }
               else{
                   
                   bcEstadoCat.select_rf_tc_estado_catalogo(connection);
          posicionError++;
                   if(bcEstadoCat.getEstatus().equals("1") && !(getIdEvento().equals("300")||getIdEvento().equals("301")||getIdEvento().equals("302"))){
          posicionError++;
                         this.registrarImporteCuentaWs(connection, detalle.getCuentaContableId(), operacion.getImporte(), operacion.getCampo(), Fecha.formatear(Fecha.FECHA_ESTANDAR, getFecha()), signo);
          posicionError++;
                   }
                   else{
                     if(!bcEstadoCat.getEstatus().equals("1") && (getIdEvento().equals("300")||getIdEvento().equals("301")|| getIdEvento().equals("302"))){
                       this.registrarImporteCierres(connection, detalle.getCuentaContableId(), operacion.getImporte(), operacion.getCampo(), Fecha.formatear(Fecha.FECHA_ESTANDAR, getFecha()), signo,getIdEvento());
          posicionError++;
                     }
                     else {
                           throw new Exception("&, Acaba de empezar un proceso de cierre o mantenimiento al catalogo de cuentas, por lo tanto no es posible continuar...");
                       }            
                   }
               }
        
        } //end (int i = 0; i < detallePoliza.size(); i++)
      } //end try
      catch (Exception e) {
          System.out.println("Polizas.desglosarDetallePolisaWs "+posicionError+" "+e.getClass().getName()+" - "+e.getMessage());
        System.out.println("Error en metodo: desglosarDetallePolizaWs");
        System.err.println(Error.getMensaje(this, "SIAFM", "desglosarDetallePoliza", e.getMessage()));
        throw new Exception(e);
      } //end catch(Exception e)    
    } //end public void desglosarDetallePoliza(List<DetallePoliza> detallePoliza){

  
  

   /* public void eliminarDetallePoliza(ApplicationModule modulo, int polizaId) throws Exception {
      int control = 0;
      Formatos formatos = null;
      try {
        formatos = new Formatos(Contabilidad.getInstance().getPropiedad("procesos.polizas.eliminarDetallePoliza"), String.valueOf(polizaId));
        control = modulo.getTransaction().executeCommand(formatos.getSentencia());
        if (control == 0) {
            throw new Exception("No se elimino el registro del detalle de la poliza, " + polizaId);
        }
      } //end try
      catch (Exception e) {
        Error.mensaje(this, e, "SIAFM", "eliminarDetallePoliza", e.getMessage());
        modulo.getTransaction().rollback();
        throw new Exception(e);
      } //end catch(Exception e)
      finally {
        modulo = null;
        formatos = null;
      }
    } //end public void eliminarDetallePoliza(int polizaId)
*/
    //metodo usa en operacion normal
/*  public void registrarImporteCuenta(ApplicationModule modulo, int cuentaContableId, double importe, String tipo, String fechaAfectacion, String signo) throws Exception {
    int control = 0;
    ModuloRegistroContableImpl am = null;
    Formatos formatos = null;
    try {
      am =  (ModuloRegistroContableImpl)Configuration.createRootApplicationModule("sia.rf.contabilidad.registroContable.servicios.ModuloRegistroContable",  "ModuloRegistroContableLocal");
      Cuenta cuenta = new Cuenta(cuentaContableId, am);
      ArrayList listaCuenta = cuenta.getPadreId(cuenta.getNivel());
      Campo campo = new Campo(tipo, fechaAfectacion);
      for (int i = 0; i < listaCuenta.size(); i++) {
        if (listaCuenta.get(i) != null) {
          formatos = new Formatos(Contabilidad.getInstance().getPropiedad("procesos.polizas.modificarImporteCuentaContable"), String.valueOf(campo.construirMes()), String.valueOf(signo), String.valueOf(importe), String.valueOf(listaCuenta.get(i)));
          control =  modulo.getTransaction().executeCommand(formatos.getSentencia());
          // System.out.println(formatos.getSentencia());
          if (control == 0) {
            throw new Exception("No se pudo modificar los importes de la cuenta contable, " + listaCuenta.get(i));
          }
        } else {
            System.out.println("no existe elemento ".concat(cuenta.getCuenta()));
        }
      }
      //modulo.getTransaction().commit();
    } //end try
    catch (Exception e) {
      Error.mensaje(this, e, "SIAFM", "registrarImporteCuenta", e.getMessage());
      modulo.getTransaction().rollback();
      throw new Exception(e);
    } //end catch(Exception e)
    finally {
      modulo = null;
      if (am != null) {
        Configuration.releaseRootApplicationModule(am, false);
      }
      am = null;
      formatos = null;
    }
  } //end public void registrarImporteCuenta(int cuentaContableId, double importe.....

    //metodo usado para el ws
  */  
     public void registrarImporteCierres(Connection connection, int cuentaContableId, double importe, String tipo, String fechaAfectacion, String signo, String idEvento) throws Exception {
            int control;
            Sentencias sentencia;
            Map parametros;
            Campo campo = null;
            try {
                parametros = new HashMap();
                sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD, Sentencias.XML);
                Cuenta cuenta = new Cuenta(cuentaContableId,connection);
                ArrayList listaCuenta = cuenta.getPadreId(cuenta.getNivel());
                if(idEvento.equals("300")) {
                    campo = new Campo(tipo+"_eli", fechaAfectacion);
                }
                else if(idEvento.equals("301")) {
                    campo = new Campo(tipo+"_pub", fechaAfectacion);
                }
                else if(idEvento.equals("302")) {
                    campo = new Campo(tipo+"_eli_pub", fechaAfectacion);
                }                  
                  
                for (int i = 0; i < listaCuenta.size(); i++) {
                    if (listaCuenta.get(i) != null) {
                        parametros.put("mes", String.valueOf(campo.construirMes()));
                        parametros.put("signo", String.valueOf(signo));
                        parametros.put("cuentaContableId", String.valueOf(listaCuenta.get(i)));
                        parametros.put("importe", String.valueOf(importe));
                        control = sentencia.ejecutar(connection, "webService.select.modificarImporteCuentaContable", parametros);
                        // System.out.println(formatos.getSentencia());
                        if (control == 0) {
                            throw new Exception("No se pudo modificar los importes de la cuenta contable, " + listaCuenta.get(i));
                        }
                    } 
                    else {
                        System.out.println("no existe elemento ".concat(cuenta.getCuenta()));
                    }
                }
                //modulo.getTransaction().commit();
            } //end try
            catch (Exception e) {
                Error.mensaje(this, e, "SIAFM", "registrarImporteCuenta", e.getMessage());
                connection.rollback();
                throw new Exception(e);
            } //end catch(Exception e)
        } //end public void registrarImporteCuenta(int cuentaContableId, double importe.....

    public void registrarImporteCuentaWs(Connection connection, int cuentaContableId, double importe, String tipo, String fechaAfectacion, String signo) throws Exception {
        int control;
        //ModuloRegistroContableImpl am = null;
        //Formatos formatos = null;
        Sentencias sentencia;
        Map parametros;
        int posicionError = 0;
        //System.out.println("Polizas.registrarImporteCuentaWs.tipo "+tipo);
        try {
            if(tipo.equals("0") || tipo.equals("1")){
              tipo = tipo.equals("0") ? "CARGO" : "ABONO";
              fechaAfectacion =  fechaAfectacion.substring(6, 10).concat(fechaAfectacion.substring(3, 5)).concat(fechaAfectacion.substring(0, 2));
            }
            posicionError++;
            parametros = new HashMap();
            sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD, Sentencias.XML);
            posicionError++;
            //am = (ModuloRegistroContableImpl)Configuration.createRootApplicationModule("sia.rf.contabilidad.registroContable.servicios.ModuloRegistroContable", 
            // "ModuloRegistroContableLocal");      
            Cuenta cuenta = new Cuenta(cuentaContableId,connection);
            posicionError++;
            //cuenta.setEjercicio(Integer.valueOf(getEjercicio()));
            ArrayList listaCuenta = cuenta.getPadreId(cuenta.getNivel());
            posicionError++;
            Campo campo = new Campo(tipo, fechaAfectacion);
       //     System.out.println("Polizas.registrarImporteCuentaWs listaCuenta "+ listaCuenta);
            if(cuenta.getNivel()<=listaCuenta.size()){
                for (int i = 0; i < listaCuenta.size(); i++) {
                    if (listaCuenta.get(i) != null) {
                        if(!listaCuenta.get(i).toString().trim().equals("-1"))
                        {
                            parametros.put("mes", String.valueOf(campo.construirMes()));
                            parametros.put("signo", String.valueOf(signo));
                            parametros.put("cuentaContableId", String.valueOf(listaCuenta.get(i)));
                            parametros.put("importe", String.valueOf(importe));
                            //System.out.println(importe);
                            control = sentencia.ejecutar(connection, "webService.select.modificarImporteCuentaContable", parametros);
                            // System.out.println(formatos.getSentencia());
                            if (control == 0) {
                                throw new Exception("No se pudo modificar los importes de la cuenta contable, " + listaCuenta.get(i));
                            }
                        }
                    } 
                    else {
                        System.out.println("no existe elemento ".concat(cuenta.getCuenta()));
                    }
                }
            }else{
                throw new Exception(" La cuenta contable " + cuenta.getCuenta() + " con nivel "+ cuenta.getNivel() +", que se quiere afectar en la poliza no corresponde con el nivel configurado en la cuenta de mayor , favor de verificarla. ");
            }
            //modulo.getTransaction().commit();
        } //end try
        catch (Exception e) {
            Error.mensaje(this, e, "SIAFM", "registrarImporteCuenta", e.getMessage());
            connection.rollback();
            //modulo.getTransaction().rollback();
            throw new Exception(e);
        } //end catch(Exception e)
    } //end public void registrarImporteCuenta(int cuentaContableId, double importe.....

    public void actualizarImporteCuenta(Connection connection, int cuentaContableId, double importe, String tipo, String fechaAfectacion, String signo) throws Exception {
        int control = 0;
        Sentencias sentencia;
        Map parametros;
        //String fecha = null;
        try {
            tipo = tipo.equals("0") ? "CARGO" : "ABONO";
            //fecha = sia.libs.formato.Fecha.formatear(sia.libs.formato.Fecha.FECHA_ESTANDAR,fechaAfectacion);
            parametros = new HashMap();
            sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD, Sentencias.XML);
            Cuenta cuenta = new Cuenta(cuentaContableId, connection);
            ArrayList listaCuenta = cuenta.getPadreId(cuenta.getNivel());
            ArrayList listaCuentasAgrupadas= new ArrayList();
            Campo campo = new Campo(tipo, fechaAfectacion.substring(6, 10).concat(fechaAfectacion.substring(3, 5)).concat(fechaAfectacion.substring(0, 2)));
            for (int i = 0; i < listaCuenta.size(); i++) {
                if (listaCuenta.get(i) != null) {
                    parametros.put("mes", String.valueOf(campo.construirMes()));
                    parametros.put("signo", String.valueOf(signo));
                    parametros.put("cuentaContableId", String.valueOf(listaCuenta.get(i)));
                    parametros.put("importe", String.valueOf(importe));
                    //control = sentencia.ejecutar(connection, "webService.select.modificarImporteCuentaContable", parametros);
                    // System.out.println("igual "+sentencia.getCommand("webService.select.modificarImporteCuentaContable", parametros)); //meterlo a una lista y ejecutarlos todos de golpe en un solo metodo
                    listaCuentasAgrupadas.add(sentencia.getCommand("webService.select.modificarImporteCuentaContable", parametros));
                    //if (control == 0) {
                    //    throw new Exception("No se pudo modificar los importes de la cuenta contable, " + listaCuenta.get(i));
                    //}
                } else {
                    System.out.println("no existe elemento ".concat(cuenta.getCuenta()));
                }
            }
            control=sentencia.ejecutarAgrupadas(connection,listaCuentasAgrupadas);
        } //end try
        catch (Exception e) {
            Error.mensaje(this, e, "SIAFM", "registrarImporteCuenta", e.getMessage());
            connection.rollback();
            throw new Exception(e);
        } //end catch(Exception e)
    } //end public void registrarImporteCuenta(int cuentaContableId, double importe.....
/*
    public List<DetallePoliza> obtenerDetallePoliza(ApplicationModule modulo, int polizaId) throws Exception {
        ViewObject vista = null;
        try {
            List detallePoliza = new ArrayList<DetallePoliza>();
            vista = modulo.createViewObjectFromQueryStmt("VistaDetallePoliza", "select poliza_id, cuenta_contable_id, importe, operacion_contable_id, " + "nvl(referencia,' ') as referencia, to_char(fecha_afectacion,'yyyyMMdd') fecha_afectacion from rf_tr_detalle_poliza where poliza_id=" + polizaId);
            vista.executeQuery();
            vista.first();
            for (int i = 0; i < vista.getRowCount(); i++) {
                if (vista.getCurrentRow().getAttribute(3).equals("0")) {
                    detallePoliza.add(new DetallePoliza(vista.getCurrentRow().getAttribute(4).toString(), vista.getCurrentRow().getAttribute(5).toString(), new Debe(((Number)vista.getCurrentRow().getAttribute(2)).doubleValue()), ((Number)vista.getCurrentRow().getAttribute(1)).intValue()));
                } //end if(vista.getCurrentRow().getA...
                else {
                    detallePoliza.add(new DetallePoliza(vista.getCurrentRow().getAttribute(4).toString(), vista.getCurrentRow().getAttribute(5).toString(), new Haber(((Number)vista.getCurrentRow().getAttribute(2)).doubleValue()), ((Number)vista.getCurrentRow().getAttribute(1)).intValue()));

                } //end else
                vista.next();
            } //end for (int i = 0; i < vista.getRowCount(); i++)
            return detallePoliza;
        } //end try
        catch (Exception e) {
            Error.mensaje(this, e, "SIAFM", "obtenerDetallePoliza", e.getMessage());
            throw new Exception(e);
        } //end catch(Exception e)
        finally {
            vista.remove();
            modulo = null;
        }
    } //end public List<DetallePoliza> eliminarDetallePoliza(int polizaId)
*/
    public void verificarImportes() throws Exception {
        double sumaHaber = 0.0;
        double sumaDebe = 0.0;
        int existeDebe = 0;
        int existeHaber = 0;
        int cuentaCeros = -1;
        DetallePoliza detallePoliza;
        Iterator iteradorDetalle = this.iterator();
        //System.out.println("Total de detalles = "+this.size());
        String cuentasContablesId="in (";
        int posicionError=0;
        try {
            while (iteradorDetalle.hasNext() && cuentaCeros != 0) {
                posicionError = 0;
                detallePoliza = (DetallePoliza)iteradorDetalle.next();
                posicionError++;
                cuentasContablesId=cuentasContablesId+detallePoliza.getCuentaContableId()+",";
                posicionError++;
                if (detallePoliza.getOperacionContable().getImporte() == 0) {
                   // cuentaCeros++;
                } //if
                posicionError++;
                if (detallePoliza.getOperacionContable().getId() == 0) {
                    sumaDebe = Double.parseDouble(Numero.redondea(sumaDebe + Double.parseDouble(Numero.formatear(Numero.NUMERO_DECIMALES, detallePoliza.getOperacionContable().getImporte())), 2));
                    //sumaDebe= sumaDebe+Double.parseDouble(Numero.formatear(Numero.NUMERO_DECIMALES, detallePoliza.getOperacionContable().getImporte()));                  
                    //System.out.println("cuenta contable id " +detallePoliza.getCuentaContableId()+ " debe " + detallePoliza.getOperacionContable().getImporte());
                    existeDebe++;
                } 
                else {
                    sumaHaber = Double.parseDouble(Numero.redondea(sumaHaber + Double.parseDouble(Numero.formatear(Numero.NUMERO_DECIMALES, detallePoliza.getOperacionContable().getImporte())), 2));
                    //System.out.println("cuenta contable id " +detallePoliza.getCuentaContableId()+ " haber " + detallePoliza.getOperacionContable().getImporte());
                    existeHaber++;
                } //if
                posicionError++;
            } //while
            posicionError++;
            if (cuentaCeros == 0) {
                throw new PolizaException(PolizaException.IMPORTES_CON_CEROS);
            } //if
            else {
                if (sumaDebe != sumaHaber) {
                    throw new PolizaException(PolizaException.IMPORTE_INCONSISTENTE);
                } //if
                //no debe verificar los importes del debe
                /*else{
            if(existeDebe==0){
                tipoValidacion=IMPORTE_DEBE;
            }//if
            if(existeHaber==0){
               tipoValidacion=IMPORTE_HABER;
            }//if
          }//else        */
            } //else          
        } //try
        catch (Exception e) {
            System.out.println(posicionError+" Polizas.verificarImporte() "+e.getClass().getName()+": "+e.getMessage());
            System.out.println("Debe= "+sumaDebe+" Haber="+sumaHaber+"  ultimaCuentaContableId Procesada= "+cuentasContablesId);
            Error.mensaje(e, "Contabilidad");
            throw e;
        } //catch
    } //verifivar importes
/*
    public int existPoliza(ApplicationModule moduloPolizas, String referencia, String ejercicio) throws Exception {
        Formatos formatos = null;
        int polizaId = 0;
        ViewObject vistaExistePoliza = null;
        try {
            vistaExistePoliza = moduloPolizas.findViewObject("vistaExistePoliza");
            if (vistaExistePoliza != null)
                vistaExistePoliza.remove();
            formatos = new Formatos(Contabilidad.getInstance().getPropiedad("procesoPorLotes.query.existePoliza"), referencia, ejercicio, getCatalogoCuenta());
            vistaExistePoliza = moduloPolizas.createViewObjectFromQueryStmt("vistaExistePoliza", formatos.getSentencia());
            vistaExistePoliza.executeQuery();
            if (vistaExistePoliza.getRowCount() != 0)
                polizaId = ((Number)vistaExistePoliza.first().getAttribute("POLIZA_ID")).intValue();
        } 
        catch (Exception ex) {
            Error.mensaje(ex, "Contabilidad");
            throw ex;
        } 
        finally {
            if (vistaExistePoliza != null)
                vistaExistePoliza.remove();
            vistaExistePoliza = null;
            formatos = null;
        }
        return polizaId;
    }
*/
    /*public static void main(String[] args) {
    try{
      Polizas pol = new Polizas("", "", "", 1, 1, 1, 1, 1, "", "", "", "","");
      pol.registrarImporteCuenta(moduloPolizas,10269, 10.0, "cargo","20071015", "+");
      /*List detallePoliza = pol.obtenerDetallePoliza(2);
      for (int i = 0; i < detallePoliza.size(); i++)  {
        DetallePoliza detalle = (DetallePoliza)detallePoliza.get(i);
        System.out.println(detalle.getCuentaContableId());
        System.out.println(detalle.getFechaAfectacion());
        OperacionContable operacion = detalle.getOperacionContable();
        System.out.println(operacion.getCampo());
        System.out.println(operacion.getImporte());
      }

    }
    catch(Exception e){
      System.out.println("Transaction Exception");
    }
  }
*/

    public String getAbreviaturaPoliza() {
        String tipoPoliza;
        switch (getTipoPolizaId()) {
        case POLIZA_DIARIO:
            tipoPoliza = "D";
            break;
        case POLIZA_CHEQUE:
            tipoPoliza = "C";
            break;
        case POLIZA_EGRESO:
            tipoPoliza = "E";
            break;
        case POLIZA_INGRESO:
            tipoPoliza = "I";
            break;
        case POLIZA_ELIMINACION:
            tipoPoliza = "L";
            break;    
        default:
            tipoPoliza = "INDEFINIDO";
            break;
        }
        return tipoPoliza;
    }
/*
    public void actualizaDatosProcesados(ApplicationModule moduloPolizas, DocumentoAutomatico documentoAutomatico) throws Exception {
        String query = null;
        ResourceBundle propiedades = null;
        Formatos formatos = null;
        Sentencias sentencia = null;
        try {
            propiedades = ListResourceBundle.getBundle("contabilidad");
            formatos = new Formatos(propiedades.getString("presupuesto.polizaAutomatica.actualizarDatosProcesada"), documentoAutomatico.getParametros());
            query = formatos.getSentencia();
            moduloPolizas.getTransaction().executeCommand(query);
        }catch (Exception e) {
            Error.mensaje(e, "CONTABILIDAD");
            throw e;
        }
        finally {
            query = null;
            propiedades = null;
            formatos = null;
            sentencia = null;
        }

    }
*/
}
