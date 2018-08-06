package sia.ws.publicar.contabilidad.definicion;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import sia.db.dao.DaoFactory;
import sia.db.sql.Sentencias;
import sia.db.sql.Vista;
import sia.libs.formato.Error;
import sia.libs.formato.Fecha;
import sia.libs.formato.Formatos;
import sia.libs.recurso.Contabilidad;
import sia.rf.contabilidad.registroContable.acciones.ControlRegistro;
import sia.rf.contabilidad.registroContable.formas.servicios.CuentasForma;
import sia.rf.contabilidad.registroContable.formas.servicios.Formas;
import sia.rf.contabilidad.registroContable.procesos.polizas.servicios.Polizas;
import sia.ws.publicar.contabilidad.excepciones.FormaException;


public class PrepararForma {
    private static final String AMDEF_FORMAS          = "sia.rf.contabilidad.registroContable.formas.servicios.ModuloFormas";
    private static final String CONFIG_FORMAS         = "ModuloFormasLocal";              
    private static final String MOPER_ABIERTO         = "99";
    private static final int    REGISTRAR_POLIZA      = 1;    
    private String forma;
    private String origen;
    private String unidadEjecutora;
    private String entidad;
    private String ambito;
    private String pais;
    private String variables;
    private String referencia;    
    private int numEmpleado;  
    private int idCatalogoCuenta;
    private String fechaPoliza;
    private int formaContableId;
    private List cuentasNoexiten;
    private ControlRegistro controlReg;
    
    public PrepararForma(String unidadEjecutora, String entidad, String ambito,String referencia,int numEmpleado, int idCatalogoCuenta) {
      this("",unidadEjecutora,entidad,ambito,"",referencia,numEmpleado, idCatalogoCuenta);
    }
    
    public PrepararForma(String forma,String unidadEjecutora, String entidad, String ambito,String variables, String referencia,int numEmpleado, int idCatalogoCuenta,String fechaPoliza){
        this.setForma(forma);
        this.setUnidadEjecutora(unidadEjecutora);
        this.setEntidad(entidad);
        this.setAmbito(ambito);
        this.setVariables(variables);
        this.setReferencia(referencia);
        this.setNumEmpleado(numEmpleado);      
        this.setIdCatalogoCuenta(idCatalogoCuenta);
        this.setFechaPoliza(fechaPoliza);
        setControlReg(new ControlRegistro());
        getControlReg().setEjercicio(Integer.valueOf(fechaPoliza.substring(0,4)));
        getControlReg().inicializa(idCatalogoCuenta);
        
    }
    
    public PrepararForma(String forma,String unidadEjecutora,String entidad,String ambito,String variables,String referencia,int numEmpleado, int idCatalogoCuenta) {
      this.setForma(forma);
      this.setUnidadEjecutora(unidadEjecutora);
      this.setEntidad(entidad);
      this.setAmbito(ambito);
      this.setVariables(variables);
      this.setReferencia(referencia);
      this.setNumEmpleado(numEmpleado);      
      this.setIdCatalogoCuenta(idCatalogoCuenta);
    }   
    
    
    public PrepararForma(int formaContableId, String forma,String unidadEjecutora,String entidad,String ambito,String variables,String referencia,int numEmpleado, int idCatalogoCuenta) {
      this.setForma(forma);
      this.setUnidadEjecutora(unidadEjecutora);
      this.setEntidad(entidad);
      this.setAmbito(ambito);
      this.setVariables(variables);
      this.setReferencia(referencia);
      this.setNumEmpleado(numEmpleado);      
      this.setIdCatalogoCuenta(idCatalogoCuenta);
      this.setFormaContableId(formaContableId);
    }
    
  public FormaLista getFormaPoliza(String unidadCentral, String ambitoCentral, String entidadCentral, int area, String ejercicio, Connection conexion) throws  Exception {
      Formas              frmContable    = null;    
    List<CuentasForma>  cuentasForma         = null;
    int posicionError = 0;
    try{
      if(this.getFormaContableId()==-1 || this.getFormaContableId()==0){
/*      System.out.println("PrepararForma.java: ");
      System.out.println("if getFormaPoliza");
      System.out.println("unidad central: " + unidadCentral);
      System.out.println("ambito central: " + ambitoCentral);
      System.out.println("area: " + area);
      System.out.println("entidad Central: " + entidadCentral);
      System.out.println("ejercicio: " + ejercicio);
      System.out.println("forma: " + getForma());
      System.out.println("idcatalogocuenta: " + this.getIdCatalogoCuenta());
      System.out.println("fechapoliza: " + getFechaPoliza());
      System.out.println("controlreg: " + getControlReg());*/
        frmContable  = new Formas(getForma(), unidadCentral, entidadCentral, ambitoCentral, area, ejercicio, this.getIdCatalogoCuenta(), getFechaPoliza(), getControlReg());
      }
      else{
        frmContable  = new Formas(String.valueOf(this.getFormaContableId()), area, String.valueOf(Fecha.getAnioActual()), this.getIdCatalogoCuenta(),null);        
      }  
      posicionError++;
      cuentasForma = getFormas(frmContable, conexion);
//      System.out.println("pasa GetFormas");
      posicionError++;
      //getCuentasNoexiten().addAll(frmContable.getCuentasNoExisten());
  //    System.out.println("frmContable" + frmContable);
      //System.out.println("frmContable.getCuentasNoExisten(): " + frmContable.getCuentasNoExisten());
      isExisteCuenta(frmContable.getCuentasNoExisten());
//      System.out.println("paso isExisteCuenta");
      posicionError++;
    }//try
    catch(Exception e){
        System.out.println(posicionError+" - PrepararForma.getFormaPoliza: "+e.getClass().getName()+e.getMessage());
      throw e;
      //Error.mensaje(e, "SIAFM");   
      
    }//catch 
    //System.out.println("frmContable.getFormaContableId(): " + frmContable.getFormaContableId());
    //System.out.println("cuentasForma: " + cuentasForma);
    return new FormaLista(Integer.parseInt(frmContable.getFormaContableId()), cuentasForma);               
  }
  
  public List<CuentasForma> getFormas(Formas frmContable, Connection conexion) throws  Exception {
    List <CuentasForma> cuentasForma   = null;
//    System.out.println("1. Variables de getFormas = "+getVariables());
//    System.out.println("frmContable "+frmContable.getFormaContableId());
    try{
      cuentasForma = new ArrayList(frmContable.getCuentas(getVariables(),conexion));   
  //    System.out.println("tamaño cuentasForma: " + cuentasForma.size());
    }
    catch(Exception e){
        System.out.println("PrepararForma.getFormas: "+e.getClass().getName()+" - "+e.getMessage());
      Error.mensaje(e, "SIAFM");   
      throw e;
    }//catch   
    return cuentasForma;
  }
  
  private void isExisteCuenta(List cuentasABuscar) throws  Exception {
    String cuenta;
    List cuentasTemp  = new ArrayList();
    try{
        //System.out.println("Tamaño cuentas a buscar: " + cuentasABuscar.size());
    if(getCuentasNoexiten() == null){ setCuentasNoexiten(new ArrayList());}
      if(getCuentasNoexiten().isEmpty()){
        //System.out.println("no hay cuentas inexistentes");
        getCuentasNoexiten().addAll(cuentasABuscar); 
      }
      else{
          //System.out.println("si hay cuentas inexistentes");
        for(int pocicion=0; pocicion < cuentasABuscar.size();pocicion++){
          Iterator iteratorLista = getCuentasNoexiten().iterator();
          int agrego = 0;
          while (iteratorLista.hasNext()) {
            cuenta = (String)iteratorLista.next();
            if (cuentasABuscar.get(pocicion).equals(cuenta)) {
                agrego++;
            }
          }
          if(agrego==0){
            cuentasTemp.add(cuentasABuscar.get(pocicion));
          }
        }
        getCuentasNoexiten().addAll(cuentasTemp);
      }
    }
    catch(Exception e){System.out.println("catch de isexistecuenta");
      Error.mensaje(e, "SIAFM");
      throw e;  
    }
    //return cuentas;
  }
  
  public Polizas getFormas(int formaContableId, List <CuentasForma> cuentasForma, String concepto, String idEvento, Connection conexion) throws  Exception {
    Sentencias sentencia;
    List<Vista> registroForma;
    PrepararPoliza prepararPoliza;  
    Polizas poliza = null;
    int posicionError = 0;
    try{
      sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD, Sentencias.XML);
      posicionError++;
      registroForma = sentencia.registros("webService.select.obtenerForma","formaContableId=".concat(String.valueOf(formaContableId)).concat("|"));
      posicionError++;
      if(registroForma.isEmpty()){
         // System.out.println("registroForma está vacío");         
        throw new FormaException(FormaException.FORMA_CONTABLE);
      }//if ...
      else{
          //System.out.println("registroFroma no está vacío");
        prepararPoliza = new PrepararPoliza(MOPER_ABIERTO, getUnidadEjecutora(), getEntidad(), getAmbito(), Integer.valueOf(registroForma.get(0).getField("TIPO_POLIZA_ID")), getReferencia(), concepto, getNumEmpleado() , registroForma.get(0).getField("FORMA"),getIdCatalogoCuenta(),false,idEvento);
      posicionError++;
//      System.out.println("getFormas - cuentasForma.size(): " + cuentasForma.size());
  //    System.out.println("cuentasForma.get(0)" + cuentasForma.get(0));
        poliza=prepararPoliza.getPoliza(this.getFechaPoliza(),cuentasForma, conexion);                                         
     // System.out.println("getFormas (ya paso getPoliza)- poliza=prepararPoliza.getPoliza(");
        posicionError++;
      }
      posicionError++;
    }//try
    catch(Exception e){
        System.out.println(posicionError+" PreparaForma.getFormas "+e.getClass().getName()+": "+e.getMessage());
      Error.mensaje(e, "SIAFM");
      throw e;      
    }//catch
    return poliza;
  }
    
    /*
    public Polizas getFormaPoliza(Connection conexion) throws  Exception {
      ModuloFormasImpl    amFormas       = null;  
      Polizas             poliza         = null;
      ViewObject          voFormas       = null;    
      Row                 regForma       = null;
      Formas              frmContable    = null;    
      List <CuentasForma> cuentasForma   = null;  
      PrepararPoliza      prepararPoliza = null;     
      try{
        amFormas=(ModuloFormasImpl)Configuration.createRootApplicationModule(AMDEF_FORMAS, CONFIG_FORMAS);
        if(this.getFormaContableId()==-1 || this.getFormaContableId()==0){
          frmContable  = new Formas(getForma(), getUnidadEjecutora(), getEntidad(), getAmbito(),Areas.MANUAL,String.valueOf(Fecha.getAnioActual()), this.getIdCatalogoCuenta(),getFechaPoliza(),getControlReg());
        }
        else{
          frmContable  = new Formas(String.valueOf(this.getFormaContableId()), Areas.MANUAL, String.valueOf(Fecha.getAnioActual()), this.getIdCatalogoCuenta(),null);        
        }
        cuentasForma = new ArrayList(frmContable.getCuentas(variables, conexion));        
        voFormas=amFormas.findViewObject("VistaRfTcFormasContables1");        
        voFormas.setWhereClause("forma_contable_id=".concat(String.valueOf(frmContable.getFormaContableId())));
        voFormas.executeQuery();
        if(voFormas.getRowCount()==0){         
          throw new FormaException(FormaException.FORMA_CONTABLE);
        }//if ...
        else{
          regForma=voFormas.first();
          prepararPoliza = new PrepararPoliza(MOPER_ABIERTO, getUnidadEjecutora(), getEntidad(), getAmbito(), ((Number)regForma.getAttribute("TipoPolizaId")).intValue()  , getReferencia(), regForma.getAttribute("Concepto").toString(), getNumEmpleado() ,regForma.getAttribute("Forma").toString(),getIdCatalogoCuenta(),false,null);
          poliza=prepararPoliza.getPoliza(this.getFechaPoliza(),cuentasForma, conexion);                        
        }             
      }//try
      catch(Exception e){
        Error.mensaje(e, "SIAFM");
        throw e;      
      }//catch            
      finally{
        if(amFormas!= null)
          Configuration.releaseRootApplicationModule(amFormas,false);    
        amFormas        = null;
        voFormas        = null;
        regForma        = null;      
        prepararPoliza  = null;        
        cuentasForma    = null;
      }
      return poliza;               
    }
    */
    public Polizas getFormaPoliza(String fechaPoliza,String tipoMovimiento,List variables, String areaFormasId, Map valores,Connection conexion) throws Exception {
      Formas frmContable; 
      PrepararPoliza prepararPoliza;
      Polizas  poliza = null;
      List <CuentasForma>cuentasForma;      
      List <Vista> detalle = null;
      Vista vista;
      String concepto; 
      int tipoPoliza;
      int formaId = 0;     
      try{
        detalle         = getDetalleForma(tipoMovimiento, areaFormasId);   
        if(detalle.isEmpty()){
          throw new Exception("No Hay formas para el tipo de movimiento ".concat(tipoMovimiento));
        }
        vista           = detalle.get(0);    
        concepto        = vista.getField("CONCEPTO");
        tipoPoliza      = Integer.valueOf(vista.getField("TIPO_POLIZA_ID"));
        formaId         = Integer.valueOf(vista.getField("FORMA_CONTABLE_ID") );     
        frmContable     = new Formas(vista.getField("FORMA_CONTABLE_ID"),Integer.valueOf(areaFormasId),fechaPoliza.substring(0,4), this.getIdCatalogoCuenta(), fechaPoliza); 
        cuentasForma    = new ArrayList(frmContable.getCuentas(variables,conexion));                  
        prepararPoliza  = new PrepararPoliza(MOPER_ABIERTO, getUnidadEjecutora(), getEntidad(), getAmbito(), tipoPoliza , getReferencia(), reemplazarValores(valores,concepto), getNumEmpleado(),vista.getField("FORMA"),getIdCatalogoCuenta(),false,"-1");
        poliza          = prepararPoliza.getPoliza(fechaPoliza,cuentasForma, conexion);           
    }catch(Exception e){
      Error.mensaje(e,"SAIFM");
      throw e;
    }finally{
      detalle.clear();
    }
    return poliza;  
  }
  
  public Polizas getFormaPoliza(String fechaPoliza,String tipoMovimiento,List variables, String areaFormasId, Connection conexion) throws Exception {
     return getFormaPoliza(fechaPoliza,tipoMovimiento,variables,areaFormasId,new HashMap(),conexion);
  }
     
  private List<Vista> getDetalleForma(String tipoMovimiento, String areaFormasId) throws Exception {
    ResourceBundle propiedades      = null;
    StringBuffer   sbConsulta       = null;    
    Sentencias     sentencia;     
    List<Vista>    consulta         = null;
    Formatos       formatos;
    try{
      sbConsulta  = new StringBuffer();
      sentencia=new Sentencias(DaoFactory.CONEXION_CONTABILIDAD);
      formatos = new Formatos(Contabilidad.getPropiedad("procesoPorLotes.query.getDetalleForma"),tipoMovimiento, areaFormasId);
      consulta=sentencia.registros(formatos.getSentencia());      
    } catch (Exception ex)  {
      Error.mensaje(ex,"SIAFM");
      throw new Exception("Error al obtener el detalle forma");
    } 
    finally{
      sbConsulta   = null;
      propiedades  = null;
    }
    return consulta;
  }
    
  
  private String reemplazarValores(Map variables,String cadenaOrigen){    
    Formatos formatos;
    formatos=new Formatos(cadenaOrigen,variables);
    return formatos.getSentencia();
  }


  /*
    public String registrarPoliza(Connection conexion) throws Exception {
      String msjNotificacion = null;      
      Polizas poliza         = null;
      int consecutivo = 0;
      try {    
        poliza = getFormaPoliza(conexion);
        if (poliza.registrarPolizaAfectacion(REGISTRAR_POLIZA)) {
          consecutivo = poliza.getConsecutivo();          
          msjNotificacion ="Se realizo con exito el registro de la poliza del tipo "
                            .concat(poliza.getAbreviaturaPoliza()).concat("-").concat(Cadena.rellenar( String.valueOf(poliza.getConsecutivo()), 5, '0',true))
                            //.concat(" Fecha poliza " + Fecha.formatear(Fecha.FECHA_CORTA,poliza.getFechaAfectacion()));
                             .concat(" Fecha poliza " + this.getFechaPoliza());
        }// end if...
      } // end try...
      catch (Exception e) {
        Error.mensaje(e, "SIAFM");
        msjNotificacion ="Error,"+e.getMessage();
        throw e;
      } //catch
      finally {
        poliza = null;
      } //finally
        return  msjNotificacion;
    }//registrarPoliza
   */
 
    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setNumEmpleado(int numEmpleado) {
        this.numEmpleado = numEmpleado;
    }

    public int getNumEmpleado() {
        return numEmpleado;
    }

    public void setForma(String forma) {
        this.forma = forma;
    }

    public String getForma() {
        return forma;
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

    public void setVariables(String variables) {
        this.variables = variables;
    }

    public String getVariables() {
        return variables;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getPais() {
        return pais;
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

  public void setFechaPoliza(String fechaPoliza) {
    this.fechaPoliza = fechaPoliza;
  }

  public String getFechaPoliza() {
    return fechaPoliza;
  }

  public void setFormaContableId(int formaContableId) {
    this.formaContableId = formaContableId;
  }

  public int getFormaContableId() {
    return formaContableId;
  }

  public void setCuentasNoexiten(List cuentasNoexiten) {
    this.cuentasNoexiten = cuentasNoexiten;
  }

  public List getCuentasNoexiten() {
    return cuentasNoexiten;
  }

  public void setControlReg(ControlRegistro controlReg) {
    this.controlReg = controlReg;
  }

  public ControlRegistro getControlReg() {
    return controlReg;
  }
}
