package sia.ws.publicar.contabilidad.definicion;

import java.sql.Connection ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*import oracle.jbo.ViewObject;
import oracle.jbo.client.Configuration;
import oracle.jbo.domain.Number;
*/
import sia.libs.formato.Cadena;
import sia.libs.formato.Error;
import sia.libs.formato.Formatos;
import sia.libs.periodo.Fecha;

import sia.libs.recurso.Contabilidad;

import sia.rf.contabilidad.registroContable.formas.servicios.CuentasForma;
import sia.rf.contabilidad.registroContable.procesos.polizas.servicios.Debe;
import sia.rf.contabilidad.registroContable.procesos.polizas.servicios.DetallePoliza;
import sia.rf.contabilidad.registroContable.procesos.polizas.servicios.Haber;

//import sia.rf.contabilidad.registroContable.procesos.polizas.servicios.ModuloPolizasImpl;

import sia.rf.contabilidad.registroContable.procesos.polizas.servicios.OperacionContable;
import sia.rf.contabilidad.registroContable.procesos.polizas.servicios.Polizas;
//import sia.rf.contabilidad.registroContable.procesos.polizas.vistas.VistaDetalleOperacionesImpl;
import sia.rf.contabilidad.registroContable.servicios.Cuenta;
import sia.rf.contabilidad.registroContable.servicios.FechaAfectacion;
//import sia.rf.contabilidad.registroContable.servicios.ModuloRegistroContableImpl;
//import sia.configuracion.UtileriasComun;

import sia.db.dao.DaoFactory;
import sia.db.sql.Sentencias;

import sia.db.sql.Vista;

import sia.ws.publicar.contabilidad.excepciones.PolizaException;


public class PrepararPoliza {
  
  private static final String AMDEF                 = "sia.rf.contabilidad.registroContable.procesos.polizas.servicios.ModuloPolizas";
  private static final String CONFIG                = "ModuloPolizasLocal";    
  private static final String AMDEF_REG_CONTABLE    = "sia.rf.contabilidad.registroContable.servicios.ModuloRegistroContable";
  private static final String CONFIG_REG_CONTABLE   = "ModuloRegistroContableLocal";        
  private static final String UNIDAD_EJECUTORA      = "114";
  private static final String ENTIDAD               = "1";
  private static final String AMBITO                = "1";      
  private String origen;
  private String maestroOperacion; 
  private String programa;
  private String unidadEjecutora;
  private String entidad; 
  private String ambito;
  private String referencia;
  private String concepto; 
  private String importes; 
  private int numeroEmpleado;
  private int tipoPoliza;  
  private int maestroOperacionID;
  private int idCatalogoCuenta;
  private boolean dinamico;
  private String idEvento;
  
  public PrepararPoliza(String maestroOperacion, String unidadEjecutora, String entidad, String ambito, int tipoPoliza,String referencia, String concepto, 
     int numeroEmpleado, String origen,int idCatalogoCuenta , boolean dinamico, String idEvento){     
     this(maestroOperacion, "", unidadEjecutora, entidad, ambito, tipoPoliza , referencia, concepto , "0",numeroEmpleado,origen,idCatalogoCuenta ,dinamico,idEvento);    
  }
  
  public PrepararPoliza(String maestroOperacion, String programa,String unidadEjecutora, String entidad, String ambito, int tipoPoliza,String referencia, String concepto, 
    String importes, int numeroEmpleado, String origen,int idCatalogoCuenta,boolean dinamico, String idEvento) {
    setMaestroOperacion(maestroOperacion);
    setPrograma(programa);
    setUnidadEjecutora(unidadEjecutora);
    setEntidad(entidad);
    setAmbito(ambito);
    setReferencia(referencia);
    setConcepto(concepto);
    setImportes(importes);
    setNumeroEmpleado(numeroEmpleado);
    setDinamico(dinamico);
    setTipoPoliza(tipoPoliza);
    setOrigen(origen);
    setIdCatalogoCuenta(idCatalogoCuenta);
    setIdEvento(idEvento);
    //setFechaPoliza("");
  } 
  /*
 
   private List<DetallePoliza> getDetalle(String fechaAfectacion,String unidad,String entidad,String ambito,String ejercicio, Connection conexion) throws PolizaException {
    ModuloPolizasImpl moduloPolizas=null;    
    VistaDetalleOperacionesImpl voDetOper=null;
    List detallePoliza = new ArrayList<DetallePoliza>();
    int tipoOperacion=0;    
    int cuentaContableId=0;    
    try{
      moduloPolizas = (ModuloPolizasImpl)Configuration.createRootApplicationModule(AMDEF, CONFIG);
      voDetOper=(VistaDetalleOperacionesImpl)moduloPolizas.findViewObject("VistaDetalleOperaciones");
      voDetOper.setUnidad(unidad);
      voDetOper.setEnt(entidad);
      voDetOper.setAmb(ambito);
      voDetOper.setOperacion(getMaestroOperacion().equals("99")? "-1": String.valueOf(getMaestroOperacionId(unidad,entidad,ambito,getMaestroOperacion(),getIdCatalogoCuenta(), ejercicio, conexion)));
      voDetOper.setTipo("-1");
      voDetOper.setConsPoliza("-1");
      voDetOper.setMes("-1");
      voDetOper.setEjercicio(ejercicio);
      voDetOper.executeQuery();
      voDetOper.first();      
      if(voDetOper.getRowCount()>= 2){
        for(int i=0; i<voDetOper.getRowCount(); i++) {
          tipoOperacion=(Integer)voDetOper.getCurrentRow().getAttribute("OPERACION_CONTABLE_ID");
            OperacionContable operacionContable= null;
            if(tipoOperacion==0) {
              operacionContable= new Debe(0);
            }//if
            else{
              operacionContable= new Haber(0);
            }//else        
            
          cuentaContableId=isDinamico()?this.obtenerCuentaContableRegistrar((Integer)voDetOper.getCurrentRow().getAttribute("CUENTA_CONTABLE_ID"), voDetOper.getCurrentRow().getAttribute("CUENTA_CONTABLE").toString().trim()):(Integer)voDetOper.getCurrentRow().getAttribute("CUENTA_CONTABLE_ID");  
          detallePoliza.add(new DetallePoliza(getReferencia(), fechaAfectacion, operacionContable, cuentaContableId));
          voDetOper.next();  
        }//for 
        incorparImportes(detallePoliza);
      }//if
      else {
        throw new PolizaException(PolizaException.PREPARAR_POLIZA);
      }
    }
    catch(Exception e){
      Error.mensaje(e, "SIAFM");
      throw new PolizaException(PolizaException.PREPARAR_POLIZA);
    }
    finally{
      if(moduloPolizas!=null)
        Configuration.releaseRootApplicationModule(moduloPolizas, true);
      moduloPolizas=null;
      voDetOper=null;      
    }   
    return detallePoliza;
  }  
  */
  private List<DetallePoliza> getDetalle(String fechaAfectacion,List<CuentasForma> detalleForma) throws PolizaException{
    List<DetallePoliza> detallePoliza=new ArrayList();
    OperacionContable operacionContable;
    CuentasForma detalle;
    try{         
      if(detalleForma.size()>=2){
        for (int i = 0; i < detalleForma.size() ; i++){
          detalle=detalleForma.get(i);
          if(detalle.getTipoOperacion().equals("0")){
            operacionContable= new Debe(Double.parseDouble(detalle.getImporte()));      
          }//if
          else{
            operacionContable= new Haber(Double.parseDouble(detalle.getImporte()));
          }//else
          detallePoliza.add(new DetallePoliza(detalle.getReferencia(),fechaAfectacion,operacionContable,detalle.getCuentaContableId()));
        }//for...        
      }else{          
        throw new PolizaException(PolizaException.PREPARAR_POLIZA);
      }  
    }//try
    catch(Exception e){
      Error.mensaje(e, "SIAFM");
      System.out.println("Excepción en getDetalle");
      throw new PolizaException(PolizaException.PREPARAR_POLIZA);    
    }
    finally{
      operacionContable=null;
      detalle=null;
    }
    return detallePoliza;
  } 
  /*
  private List<DetallePoliza> getDetalle(String fechaAfectacion, String ejercicio, Connection conexion) throws PolizaException {    
    return getDetalle(fechaAfectacion, isDinamico()?UNIDAD_EJECUTORA:getUnidadEjecutora(), isDinamico()?ENTIDAD:getEntidad(), isDinamico()?AMBITO:getAmbito(), ejercicio, conexion);
  }
  
  */
 private Polizas getPoliza(String unidadEjecutora,String ambito,String pais,int entidad,int tipoPoliza, int maestroOperacionID, int clasificacionPoliza, int numeroEmpleado,String fecha, String fechaAfectacion,
                       String referencia,String concepto, String anio, int mes, String idEvento){
    Polizas poliza=null;
    try{
     poliza =new Polizas( unidadEjecutora, ambito, pais,  entidad,  tipoPoliza, maestroOperacionID, clasificacionPoliza, 
                          numeroEmpleado, fecha, fechaAfectacion, 
                          referencia, concepto, anio, mes,getOrigen(),getIdCatalogoCuenta(),idEvento);
    }catch(Exception e){
      Error.mensaje(e,"SIAFM");
    }
    return poliza;                          
  
  }
  
  private Polizas inicializarPoliza(String ejercicio,String fechaPoliza,String fechaAfectacion,int mes, Connection conexion) throws PolizaException{
    Polizas poliza=null;    
    try {    
      setMaestroOperacionID(getMaestroOperacionId(getUnidadEjecutora(), getEntidad(),getAmbito(),isDinamico()?"99":getMaestroOperacion(),getIdCatalogoCuenta(),ejercicio, conexion));
      if(getMaestroOperacionID()== 0) {
        if(insertarOperacion(ejercicio, fechaAfectacion, conexion) != 0)
          setMaestroOperacionID(getMaestroOperacionId(getUnidadEjecutora(), getEntidad(),getAmbito(),isDinamico()?"99":getMaestroOperacion(),getIdCatalogoCuenta(),ejercicio, conexion));
      }
      if(getMaestroOperacionID()!= 0) { 

        poliza = getPoliza(getUnidadEjecutora(), getAmbito(), "147", Integer.valueOf(getEntidad()),  getTipoPoliza(), maestroOperacionID, Polizas.POLIZA_ABIERTA, 
                                      getNumeroEmpleado(), fechaPoliza, fechaAfectacion, 
                                      getReferencia(), getConcepto(), ejercicio, mes, getIdEvento());        
      }
      else{
        throw new PolizaException("No es posible registrar la poliza, debido a que maestro operacion 99" +
            " no existe para la Unidad Ejecutora: "+ getUnidadEjecutora()+ ", Entidad: "+ getEntidad()+ " y Ambito: "+ getAmbito() + ",\n favor de comunicarselo al administrador.");
      }
    }//try  
    catch(Exception e) {
      Error.mensaje(e,"SIAFM");
      throw new PolizaException(e.getMessage()) ;
    }   
    return poliza;         
  } 
  
  public Polizas getPoliza(List<CuentasForma> cuentasForma, String fechaAfec, Connection conexion)throws PolizaException {
    Polizas poliza = null;
    sia.libs.periodo.Fecha fecha = null;    
    String fechaFormateada = null;
    try {      
      fechaFormateada = "".concat(fechaAfec.substring(6,10)).concat(fechaAfec.substring(3,5)).concat(fechaAfec.substring(0,2)).concat("000000");
      fecha= new sia.libs.periodo.Fecha(fechaFormateada);    
      poliza=this.inicializarPoliza(String.valueOf(fecha.getAnio()), fechaFormateada, fecha.toString(),fecha.getMes(), conexion);      
      poliza.addDetalle(this.getDetalle(fecha.toString(),cuentasForma));
      poliza.verificarImportes();
    }//try
    catch(Exception e) {
      Error.mensaje(e,"SIAFM");
      throw new PolizaException(e.getMessage()) ;
    }
    finally{
     fecha=null;
    }
    return poliza;
  }
  
  public Polizas getPoliza(List <CuentasForma> cuentasForma, Connection conexion) throws PolizaException {
    Polizas poliza               = null;      
    sia.libs.periodo.Fecha fecha = null;
    fecha=this.obtenerFechaAfectacion();          
    try {      
      poliza=this.inicializarPoliza(String.valueOf(fecha.getAnio()), this.obtenerFechaPoliza(), fecha.toString(),fecha.getMes(),conexion);      
      poliza.addDetalle(this.getDetalle(fecha.toString(),cuentasForma));
      poliza.verificarImportes();
    }//try
    catch(Exception e) {
      Error.mensaje(e,"SIAFM");
      throw new PolizaException(e.getMessage()) ;
    }
    finally{
     fecha=null;
    }//finalyy    
      return poliza;    
  }//getPoliza ...
  
   public Polizas getPoliza(String fechaPoliza,List <CuentasForma> cuentasForma,Connection conexion) throws PolizaException {
     Polizas poliza               = null;      
     sia.libs.periodo.Fecha fecha;
     int posicionError = 0;
     try {
        posicionError++;
        fecha=this.obtenerFechaAfectacion();
        posicionError++;
        Fecha fechaPolizas = new Fecha(fechaPoliza); 
        posicionError++;
        //System.out.println("antes de inicializarpoliza en getpoliza");
        poliza=this.inicializarPoliza(String.valueOf(fechaPolizas.getAnio()) , fechaPoliza, fecha.toString(),fechaPolizas.getMes(), conexion);      
        //System.out.println("después de inicializarpoliza en getpoliza");
        posicionError++;
        //System.out.println("antes de addDetalle");
        //System.out.println(fecha.toString() + "*" + cuentasForma + "*");
        poliza.addDetalle(this.getDetalle(fecha.toString(),cuentasForma));
        //System.out.println("después de addDetalle");
        posicionError++;
        poliza.verificarImportes();       
        //System.out.println("después de verificar importes");
        posicionError++;
     }//try
     catch(Exception e) {
         System.out.println(posicionError+" PrepararPoliza.getPoliza "+e.getClass().getName()+": "+e.getMessage());
       Error.mensaje(e,"SIAFM");
       throw new PolizaException(e.getMessage()) ;
     }
       return poliza;    
   }//getPoliza ...   
   
  private int insertarOperacion(String ejercicio, String fechaAfectacion, Connection conexion) throws PolizaException {
    int registro = 0;     
    Sentencias sentencia = null;
    Map parametros = null;
    try {
      sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD,Sentencias.XML);
      parametros = new HashMap();
      parametros.put("unidadEjecutora",getUnidadEjecutora());
      parametros.put("ambito",getAmbito());
      parametros.put("entidad", getEntidad());
      parametros.put("idCatalogoCuenta",getIdCatalogoCuenta());
      parametros.put("ejercicio",ejercicio);
      parametros.put("fechaRegistro",fechaAfectacion.substring(6,8).concat("/").concat(fechaAfectacion.substring(4,6)).concat("/").concat(fechaAfectacion.substring(0,4)));
      registro = sentencia.ejecutar(conexion,"webService.insert.insertarMaestroOperacion",parametros);     
    }//try
    catch(Exception e) {
      Error.mensaje(e,"SIAFM");
      throw new PolizaException(e.getMessage()) ;
    }
    finally{
      sentencia = null;
      parametros.clear();
      parametros = null;
    }//finally    
      return registro;    
  }
  /*
  private  int  obtenerCuentaContableRegistrar(int cuentaContableId,String cuentaContable) throws PolizaException{
    ModuloPolizasImpl moduloPolizas            = null;            
    ViewObject vistaObtenerCuenta              = null;
    ModuloRegistroContableImpl  moduloRegistro = null;
    Cuenta cuenta                              = null;
    int ctaContableid=0;    
    ArrayList nivelesCuenta                    = null;
    ArrayList cuentaCorta                      = null;
    StringBuffer ctaContableRemplazar          = null;
    Formatos formatos                          = null;
    char relleno='\u0000';
    String cuentaRemplazo=cuentaContable;    
    int longitudRecorte=0;
    //boolean rellena=false;
    try{
      nivelesCuenta          = new ArrayList();
      cuentaCorta            = new ArrayList();
      ctaContableRemplazar   = new StringBuffer();
      moduloPolizas = (ModuloPolizasImpl)Configuration.createRootApplicationModule(AMDEF, CONFIG);      
      moduloRegistro=(ModuloRegistroContableImpl)Configuration.createRootApplicationModule(AMDEF_REG_CONTABLE,CONFIG_REG_CONTABLE);      
      cuenta=new Cuenta(cuentaContableId,moduloRegistro);
      relleno='0';//cuenta.regresaCaracter().toString().charAt(0);
      nivelesCuenta   = cuenta.getLongitudNivel();
      cuentaCorta     = cuenta.getCuentaCorta();              
      longitudRecorte =(Integer)nivelesCuenta.get(1)+(Integer)nivelesCuenta.get(2)+(Integer)nivelesCuenta.get(3);
      ctaContableRemplazar.append(Cadena.rellenar(getPrograma(),(Integer)nivelesCuenta.get(1),relleno,true));
      ctaContableRemplazar.append(Cadena.rellenar(getUnidadEjecutora(),(Integer)nivelesCuenta.get(2),relleno,true));
      ctaContableRemplazar.append(Cadena.rellenar(getEntidad().concat(getAmbito()),(Integer)nivelesCuenta.get(3),relleno,true));                   
      cuentaRemplazo     = cuentaRemplazo.replace(cuentaRemplazo.substring((Integer)nivelesCuenta.get(0),(Integer)nivelesCuenta.get(0)+longitudRecorte) ,ctaContableRemplazar.toString());            
      vistaObtenerCuenta = moduloPolizas.findViewObject("vistaCuentaMayor");
      if (vistaObtenerCuenta != null) {
        vistaObtenerCuenta.remove();
      }
      formatos = new Formatos(Contabilidad.getInstance().getPropiedad("procesos.polizas.obtenerCuentaContable"),cuentaRemplazo, String.valueOf(sia.libs.formato.Fecha.getAnioActual()));
      vistaObtenerCuenta =  moduloPolizas.createViewObjectFromQueryStmt("vistaObtenerCuenta", formatos.getSentencia());
      vistaObtenerCuenta.executeQuery();
      ctaContableid    = vistaObtenerCuenta.first()==null?0:((Number)vistaObtenerCuenta.first().getAttribute("CUENTA_CONTABLE_ID")).intValue();         
      if(ctaContableid==0){
        throw new PolizaException(PolizaException.CUENTA_CONTABLE);   
      }
    }
    catch(Exception  e){
      Error.mensaje(e,"SIAFM");
      throw new PolizaException(PolizaException.CUENTA_CONTABLE);  
    }
    finally{
      if(moduloPolizas!=null)
        Configuration.releaseRootApplicationModule(moduloPolizas, true);
      if(moduloRegistro!=null)  
        Configuration.releaseRootApplicationModule(moduloRegistro, true);
      moduloRegistro     = null;  
      moduloPolizas      = null;            
      formatos           = null;
      if(vistaObtenerCuenta!=null){
         vistaObtenerCuenta.remove();         
      }
      vistaObtenerCuenta = null;  
      cuenta             = null;
      nivelesCuenta.clear();
      nivelesCuenta      = null;
    }
    return ctaContableid;
  }
  */
  
  private void incorparImportes(List<DetallePoliza> detallePoliza) throws PolizaException {
    String[] importePoliza= getImportes().split(",");    
    try{
      DetallePoliza detPoliza=null;
      for(int i=0; i<detallePoliza.size(); i++){
        detPoliza=detallePoliza.get(i);
        detPoliza.getOperacionContable().setImporte(Double.parseDouble(importePoliza[i]));
      }//for        
    }
    catch(Exception e) {
       Error.mensaje(e, "SIAFM");
       throw new PolizaException(PolizaException.INCORPORAR_IMPORTES);
    }
    finally{
      importePoliza=null;    
    }    
  }//incorporarImportes
  
    
  private sia.libs.periodo.Fecha obtenerFechaAfectacion() throws PolizaException{
    FechaAfectacion fechaAfectacion = null;
    sia.libs.periodo.Fecha fecha    = null;
    try{
      fechaAfectacion = new FechaAfectacion();
      fecha= new sia.libs.periodo.Fecha(fechaAfectacion.obtenerFechaAfectacion(), "/");      
    }
    catch(Exception e){
      throw new PolizaException(e.getMessage()) ;
    }
    finally{
      fechaAfectacion = null;
    }
    return fecha;       
  }
 
 private String obtenerFechaPoliza(){
   FechaAfectacion fechaAfectacion = null;   
   fechaAfectacion = new FechaAfectacion();     
   return fechaAfectacion.obtenerFechaAfectacion();    
 }
 /*
  public Polizas getPoliza(Connection conexion) throws PolizaException {
    Polizas poliza=null;    
    sia.libs.periodo.Fecha fecha = null;
    fecha=obtenerFechaAfectacion();
    try {         
      poliza=this.inicializarPoliza(String.valueOf(fecha.getAnio()), obtenerFechaPoliza(), fecha.toString(), fecha.getMes(),conexion);      
      poliza.addDetalle(getDetalle(fecha.toString(),String.valueOf(fecha.getAnio()), conexion));
      poliza.verificarImportes();       
    }
    catch(Exception e) {
      Error.mensaje(e,"SIAFM");
      throw new PolizaException(e.getMessage()) ;
    }
    finally{
      fecha= null;
    }    
    return poliza;    
 }*/
 
  private int getMaestroOperacionId(String unidadEjecutora,String entidad,String ambito,String moper, int idCatalogoCuenta,String ejercicio, Connection conexion) throws PolizaException {
    Sentencias sentencia = null;
    Map parametros = null;
    //ViewObject voMaestroOperacion = null;
    int maestroOperacion   = 0;
    //UtileriasComun utileria       = null;
    //Formatos formatos             = null;
    List<Vista> registros = null;
    try {
      registros = new ArrayList<Vista>();
      sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD,Sentencias.XML);
      parametros = new HashMap();
      parametros.put("uniEjecutora",unidadEjecutora);
      parametros.put("ambito",ambito);
      parametros.put("entidad", entidad);
      parametros.put("consecutivo",moper);
      parametros.put("idCatalogoCuenta",idCatalogoCuenta);
      parametros.put("ejercicio",ejercicio);
      //utileria   = new UtileriasComun("sia.rf.contabilidad.registroContable.procesos.polizas.servicios","ModuloPolizas");
      //utileria.removerVistas("Nombrex");
      //formatos = new Formatos(Contabilidad.getInstance().getPropiedad("procesos.polizas.obtenerMaestroOperacion"),unidadEjecutora,ambito,entidad,moper,idCatalogoCuenta,ejercicio);
      //voMaestroOperacion= utileria.createViewObjectFromSentencia(formatos.getSentencia());
      //maestroOperacion  = voMaestroOperacion.first()==null?0:((Number)voMaestroOperacion.first().getAttribute("MAESTRO_OPERACION_ID")).intValue();
      registros = sentencia.registros(sentencia.getComando("webService.select.obtenerMaestroOperacion",parametros),conexion);
      if(registros!=null)
        maestroOperacion = Integer.valueOf(registros.get(0).getField("MAESTRO_OPERACION_ID"));
      //maestroOperacion = maestroOperacion==null?0:maestroOperacion;
    }//try
    catch (Exception e) {
      Error.mensaje(e, "SIAFM");
      throw new PolizaException();
    }//catch
    finally{
      /*if( voMaestroOperacion!=null){
         voMaestroOperacion.remove();
         voMaestroOperacion=null;        
      }
      utileria.removerVistas("Nombrex");
      utileria = null;*/
      sentencia = null;
      parametros = null;
      
    }//finally
    return maestroOperacion;
  } 
  
  public void setMaestroOperacion(String maestroOperacion) {
    this.maestroOperacion = maestroOperacion;
  }

  public String getMaestroOperacion() {
    return maestroOperacion;
  }

  public void setUnidadEjecutora(String unidadEjecutora) {
    this.unidadEjecutora = unidadEjecutora;
  }

  public String getUnidadEjecutora() {
    return unidadEjecutora;
  }

  public void setEntidad(String entidad) {
    this.entidad = entidad;
  }

  public String getEntidad() {
    return entidad;
  }

  public void setAmbito(String ambito) {
    this.ambito = ambito;
  }

  public String getAmbito() {
    return ambito;
  }

  public void setReferencia(String referencia) {
    this.referencia = referencia;
  }

  public String getReferencia() {
    return referencia;
  }

  public void setConcepto(String concepto) {
    this.concepto = concepto;
  }

  public String getConcepto() {
    return concepto;
  }

  public void setImportes(String importes) {
    this.importes = importes;
  }

  public String getImportes() {
    return importes;
  }

  public void setNumeroEmpleado(int numeroEmpleado) {
    this.numeroEmpleado = numeroEmpleado;
  }

  public int getNumeroEmpleado() {
    return numeroEmpleado;
  }

    public void setPrograma(String programa) {
        this.programa = programa;
    }

    public String getPrograma() {
        return programa;
    }

    public void setDinamico(boolean tipoAfectacion) {
        this.dinamico = tipoAfectacion;
    }

    public boolean isDinamico() {
        return dinamico;
    }

    public void setMaestroOperacionID(int maestroOperacionID) {
        this.maestroOperacionID = maestroOperacionID;
    }

    public int getMaestroOperacionID() {
        return maestroOperacionID;
    }

    public void setTipoPoliza(int tipoPoliza) {
        this.tipoPoliza = tipoPoliza;
    }

    public int getTipoPoliza() {
        return tipoPoliza;
    }

  public void setOrigen(String origen) {
    this.origen = origen;
  }

  public String getOrigen() {
    return origen;
  }

    public void setIdCatalogoCuenta(int idCatalogoCuenta) {
        this.idCatalogoCuenta = idCatalogoCuenta;
    }

    public int getIdCatalogoCuenta() {
        return idCatalogoCuenta;
    }

  public void setIdEvento(String idEvento) {
    this.idEvento = idEvento;
  }

  public String getIdEvento() {
    return idEvento;
  }
}