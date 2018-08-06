package sia.rf.contabilidad.registroContable.servicios;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import sia.db.dao.DaoFactory;
import sia.db.sql.Sentencias;
import sia.db.sql.Vista;
import sia.libs.formato.Error;
import sia.libs.formato.Formatos;
import sia.libs.recurso.Contabilidad;
//import sia.configuracion.UtileriasComun;

public class Cuenta{

  private Integer id;
  private String descripcion;
  private Integer nivel;
  private String cuenta;
  private List<SubCuenta> subCuentas; 
  private List<SubCuenta> subCuentasReales; 
  private List<OperacionContable> operacion;
  private int cuentaContableId; 
  private int cuentaMayorId; 
  private String cuentaContable;
  private String fechaVigIni; 
  private String fechaVigFin;
  //private ModuloRegistroContableImpl am;  
  private final String AMDEF    = "sia.rf.contabilidad.registroContable.servicios";
  private final String MODULO   = "ModuloRegistroContable";
  private int ejercicio;
  private int idCatalogoCuenta;  
  
/*  public Cuenta(ModuloRegistroContableImpl am, int idCatalogoCuenta) {
    this(0, am);
    this.setAm(am);
    setEjercicio(((ControlRegistro)((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest()).getSession().getAttribute("controlRegistro")).getEjercicio());
    setIdCatalogoCuenta(idCatalogoCuenta);
  }  */
  
 /* public Cuenta(ModuloRegistroContableImpl am, int cuentaMayorId, int idCatalogoCuenta){
    setCuentaMayorId(cuentaMayorId);
    setId(0);
    setSubcuentas(new ArrayList());
    setSubCuentasReales(new ArrayList());    
    setAm(am);
    setIdCatalogoCuenta(idCatalogoCuenta);
    proceso();
  }*/
  
  public Cuenta(Integer id, Connection conexion) {
    setId(id);
    setSubcuentas(new ArrayList());
    //setSubCuentasReales(new ArrayList());
    //this.setAm(am);
    procesoWs(conexion);
  }
  
 /* public Cuenta(Integer id, ModuloRegistroContableImpl am) {
    setId(id);
    setSubcuentas(new ArrayList());
    setSubCuentasReales(new ArrayList());
    this.setAm(am);
    proceso();
  }*/
  
 /* public Cuenta(int secCuentaContable,int cuentaMayorId, String claveContable, int nivel, String descripcion, String fechaIni, String fechaFin, ModuloRegistroContableImpl am, int ejercicio, int idCatalogoCuenta)
  {
    this.cuentaContableId=secCuentaContable;
    this.cuentaMayorId=cuentaMayorId;
    this.cuentaContable=claveContable;
    this.nivel=nivel;
    this.descripcion=descripcion;
    this.fechaVigIni=fechaIni;
    this.fechaVigFin=fechaFin;
    this.operacion=new ArrayList();
    this.setAm(am);
    setEjercicio(ejercicio);
    setIdCatalogoCuenta(idCatalogoCuenta);
  }*/

  public void setCuentaMayorId(int cuentaMayorId) {
	this.cuentaMayorId = cuentaMayorId;
  }

  public int getCuentaMayorId() {
	return cuentaMayorId;
  }

  public void setFechaVigIni(String fechaVigIni) {
	this.fechaVigIni = fechaVigIni;
  }

  public String getFechaVigIni() {
	return fechaVigIni;
  }

  public void setFechaVigFin(String fechaVigFin) {
	this.fechaVigFin = fechaVigFin;
  }

  public String getFechaVigFin() {
	return fechaVigFin;
  }

  public void setSubCuentasReales(List<SubCuenta> subCuentasReales) {
    this.subCuentasReales = subCuentasReales;
  }

  public List<SubCuenta> getSubCuentasReales() {
    return subCuentasReales;
  }

  public void setEjercicio(int ejercicio) {
    this.ejercicio = ejercicio;
  }

  public int getEjercicio() {
    return ejercicio;
  }

  public void setIdCatalogoCuenta(int idCatalogoCuenta) {
    this.idCatalogoCuenta = idCatalogoCuenta;
  }

  public int getIdCatalogoCuenta() {
    return idCatalogoCuenta;
  }

  private void setSubcuentas(List subCuentas) {
    this.subCuentas = subCuentas;
  }

  public List getSubcuentas() {
    return subCuentas;
  }
  
  public void addSubcuenta(SubCuenta subcuenta){
    getSubcuentas().add(subcuenta);  
  }//addDetallePropuesta
  
  public void setDescripcion(String descripcion) {
    this.descripcion= descripcion;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setCuenta(String cuenta) {
   this.cuenta= cuenta;
  }
  
  public String getCuenta() {
    return cuenta;
  }
         
  public void setNivel(Integer nivel) {
    this.nivel = nivel;
  }

  public Integer getNivel() {
    return nivel;
  }

  private void setInformacion(Integer id, Connection conexion){
    //UtileriasComun util=null;
    Sentencias sentencia=null;
    List<Vista> registros=null;
    Formatos formatos = null;
    try  {
      //util=new UtileriasComun();
      sentencia=new Sentencias(DaoFactory.CONEXION_CONTABILIDAD);
      formatos = new Formatos(Contabilidad.getPropiedad("cuenta.query.obtenerCuentaContable"),String.valueOf(id));
      registros=sentencia.registros(formatos.getSentencia(),conexion);
      if(registros!=null){
        sia.libs.periodo.Fecha fecha=new sia.libs.periodo.Fecha((registros.get(0)).getField("FECHA_VIG_INI").toString(),"/");
        setEjercicio(fecha.getAnio());  
      }
    } catch (Exception ex)  {
      ex.printStackTrace();
      setEjercicio(0);
    } finally  {
     // util=null;
      sentencia=null;
      registros=null;
      formatos = null;
    } 
  }
  
  public void setId(Integer id) {
    this.id = id;
  }
  
  public Integer getId() {
    return id;
  }  
  public void colocarImportePorMes(Double mesCargo,Double mesAbono,Double acumCargo,Double acumAbono,int mes)
  {
    operacion.clear();
    for(int x=1;x<=12;x++)
    {
      if(x==mes)
        operacion.add(new OperacionContable(new PeriodoOper(mesCargo,mesAbono),new AcumuladoOper(acumCargo,acumAbono),mes));
      else
        operacion.add(new OperacionContable(new PeriodoOper(0.00,0.00),new AcumuladoOper(0.00,0.00),mes));
    }
  }
  
  
  
/*  public void guardar()
  {
    ApplicationModule am = Modulo.obtenerModulo(AMDEF,MODULO);
    ViewObject cuentasContables=am.findViewObject("VistaRfTrCuentasContables1");
    ViewRowImpl cuenta=(ViewRowImpl)cuentasContables.createRow();
    cuenta.setAttribute(4,descripcion);
    cuenta.setAttribute(6,fechaVigFin);
    cuenta.setAttribute("CuentaContableId",cuentaContableId);
    cuenta.setAttribute("CuentaMayorId",cuentaMayorId);
    cuenta.setAttribute("CuentaContable",cuentaContable);
    cuenta.setAttribute("Nivel",nivel);
    cuenta.setAttribute("Descripcion",descripcion);
    cuenta.setAttribute("FechaVigIni",fechaVigIni);
    cuenta.setAttribute("FechaVigFin",fechaVigFin);
    for (int i = 0; i < 12; i++)  
    {
      colocarImportes(
      cuenta,i,operacion.get(i).getPeriodoOper().getDebe(),
      operacion.get(i).getPeriodoOper().getHaber(),
      operacion.get(i).getAcumuladoOper().getDebe(),
      operacion.get(i).getAcumuladoOper().getHaber());
    }
  }
  */
  /*private void colocarImportes(Row cuenta,int mes,Double mesCargo,Double mesAbono,Double acumCargo,Double acumAbono)
  {
    cuenta.setAttribute(Cadena.letraCapital(Fecha.getNombreMesCorto(mes))+"Cargo",mesCargo);
    cuenta.setAttribute(Cadena.letraCapital(Fecha.getNombreMesCorto(mes))+"Abono",mesAbono);
    cuenta.setAttribute(Cadena.letraCapital(Fecha.getNombreMesCorto(mes))+"CargoAcum",acumCargo);
    cuenta.setAttribute(Cadena.letraCapital(Fecha.getNombreMesCorto(mes))+"AbonoAcum",acumAbono);
  }*/
  
/*  public void acualizarCifrasDelMes(int cuentaContableId, int mes, Double mesCargo,
                  Double mesAbono,Double acumCargo,Double acumAbono) throws Exception
  {
    ApplicationModule am = Modulo.obtenerModulo(AMDEF,MODULO);
    try
    {
      ViewObject cuentas=am.findViewObject("VistaTrCuentasContables1");
      cuentas.setNamedWhereClauseParam("id_cuenta_contable",cuentaContableId);
      cuentas.executeQuery(); 
      colocarImportes(cuentas.first(),mes-1,mesCargo,mesAbono,acumCargo,acumAbono);
    }
    catch(Exception e)
    {
      System.err.println(Error.getMensaje(this,"Contabilidad","acualizarCifrasDelMes",e.getMessage()));    
      e.printStackTrace();
      am.getTransaction().rollback();
      throw new Exception("Mensaje error");
    }
  }
*/


  
  
  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append("Cuenta[");
    sb.append(getId());
    sb.append(",");
    sb.append(getDescripcion());
    sb.append(",");
    sb.append(getNivel());
    sb.append(",");
    sb.append(getCuenta());
    sb.append(",");
    for (int i=0; i< getSubcuentas().size(); i++){
      sb.append(getSubcuentas().get(i).toString());
      if(i!= getSubcuentas().size()- 1)
        sb.append(",");
    } // for
    sb.append("]");
    return sb.toString();
  }
  
 /* private String getClave() {
    return null;
  }*/   
   
  public void finalize(){
    subCuentas.clear();
    subCuentas = null;
  }

  
  /*private void proceso() {
    ViewObject voCuentaMayor  = null;
    ViewObject voConfiguracion= null;
    ViewObject voDetalle      = null;
    Formatos formatos         = null;
    int iCuentaMayor  = 0;
    int iConfiguracion= 0;
    try {
      setInformacion(getId(),DaoFactory.getContabilidad());
      if (getId()> 0) {    
        formatos = new Formatos(Contabilidad.getInstance().getPropiedad("cuenta.query.registroContable"),id.toString());
        voCuentaMayor= am.createViewObjectFromQueryStmt("VistaRegistroContable", formatos.getSentencia());
        voCuentaMayor.executeQuery();
        if(voCuentaMayor.getRowCount()> 0) {
          setDescripcion(voCuentaMayor.first().getAttribute(0).toString().toUpperCase());
          setCuenta(voCuentaMayor.first().getAttribute(1).toString());
          setNivel(new Integer(voCuentaMayor.first().getAttribute(2).toString()));
          iCuentaMayor= Integer.parseInt(voCuentaMayor.first().getAttribute(3).toString());
          setIdCatalogoCuenta(Integer.parseInt(voCuentaMayor.first().getAttribute(4).toString()));
        } //if voCuentaMayor
      } // if getId
      else{
        setDescripcion("RAIZ");
        CuentaMayor cuentaMayor = new CuentaMayor(am, getEjercicio());
        String strCeros = cuentaMayor.rellenaRaiz();
        setCuenta(strCeros);
        setNivel(2);
        iCuentaMayor= this.getCuentaMayorId();
       } // else
       formatos=new Formatos(Contabilidad.getInstance().getPropiedad("cuenta.query.configuracion"),iCuentaMayor);
       voConfiguracion= am.createViewObjectFromQueryStmt("VistaCuentaMayor", formatos.getSentencia());
       voConfiguracion.executeQuery();
       voConfiguracion.first();
       if(voConfiguracion.getRowCount()> 0)  
         iConfiguracion= Integer.parseInt(voConfiguracion.getCurrentRow().getAttribute(0).toString());
       else 
         iConfiguracion = 0;
       formatos = new Formatos(Contabilidad.getInstance().getPropiedad("cuenta.query.detalleConfiguracion"),iConfiguracion);
       voDetalle= am.createViewObjectFromQueryStmt("VistaDetalle", formatos.getSentencia());
       voDetalle.executeQuery();
       voDetalle.first();
       StringBuffer cadena= new StringBuffer();
       int posicion= 1;
       int indice  = 0;
       int tamanio = 0;
       int agrupar = 0;
       String tabla="";
       String campoCompara = "";
       String caracter= "";
       for (int i= 0; i < voDetalle.getRowCount();i++) {
         SubCuenta subCuenta= null;
         tamanio = Integer.parseInt(voDetalle.getCurrentRow().getAttribute(0).toString());
         agrupar = Integer.parseInt(voDetalle.getCurrentRow().getAttribute(1).toString());
         caracter= voDetalle.getCurrentRow().getAttribute(2).toString();
         tabla= voDetalle.getCurrentRow().getAttribute(3).toString();
         campoCompara= voDetalle.getCurrentRow().getAttribute(4).toString();
         cadena.append(getCuenta().substring(indice,tamanio+indice));
         for(int x= indice+tamanio;  x< getCuenta().length(); x++) 
           cadena.append(caracter);
         if(posicion<= getNivel().intValue())  {
           subCuenta= new SubCuenta(cadena.substring(indice,  indice + tamanio), cadena.toString(), tamanio, agrupar, caracter,tabla,campoCompara, getEjercicio(), this.getIdCatalogoCuenta(),am.getCurrentConnection());
         } // if posicion
         else 
          subCuenta= new SubCuenta(null, null, tamanio, agrupar, caracter,tabla,campoCompara, getEjercicio(), this.getIdCatalogoCuenta(), am.getCurrentConnection());
          getSubcuentas().add(subCuenta);
         if(subCuenta.getCuentaPadre().getCuentaContableId()!=null)
           if(subCuenta.getCuentaPadre().getCuentaContableId()!=0)
             getSubCuentasReales().add(subCuenta);
         cadena.delete(indice+tamanio, cadena.length());
         indice+= (tamanio);
         voDetalle.next();
         posicion++;
       } // for detalle
       if (getId()==0)
        setNivel(0);
    }   // try
    catch (Exception e) {
      System.err.println("[sia.rf.contabilidad.registroContable.servicios.Cuenta.proceso] Error: "+ e);
      e.printStackTrace();
    }  // catch
    finally {
      //Configuration.releaseRootApplicationModule(am, false);
      if(voDetalle!=null)
        voDetalle.remove();
      if(voConfiguracion!=null)
        voConfiguracion.remove();
      //if (getId()>0)
        //voCuentaMayor.remove();
      if(voCuentaMayor!=null)
        voCuentaMayor.remove();
      if(am.findViewObject("VistaRegistroContable")!=null)
        am.findViewObject("VistaRegistroContable").remove();
      if(am.findViewObject("VistaCuentaMayor")!=null)
        am.findViewObject("VistaCuentaMayor").remove();
      if(am.findViewObject("VistaDetalle")!=null)
        am.findViewObject("VistaDetalle").remove();
      formatos=null;
    } // finally
  }*/
   
   // metodo para el constructor del ws
  private void procesoWs(Connection conexion) {
   Sentencias sentencia; 
   List<Vista> registrosCuentas;
   List<Vista> registroCuentaMayor;
   List<Vista> registroConfCuenta;
   int iCuentaMayor  = 0;
   int iConfiguracion= 0;
   try {
     registrosCuentas = new ArrayList<Vista>();
     registroCuentaMayor = new ArrayList<Vista>();
     registroConfCuenta = new ArrayList<Vista>();
     sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD,Sentencias.XML);
     setInformacion(getId(),conexion);
     if (getId()> 0) { 
       registrosCuentas = sentencia.registros(sentencia.getComando("webService.select.registroContable","cuentaContableId=".concat(String.valueOf(id)).concat("|")),conexion);//se requiere pasar la conexion
       if(registrosCuentas.size()> 0) {
         setDescripcion(registrosCuentas.get(0).getField("DESCRIPCION").toUpperCase());
         setCuenta(registrosCuentas.get(0).getField("CUENTA_CONTABLE"));
         setNivel(Integer.parseInt(registrosCuentas.get(0).getField("NIVEL")));
         iCuentaMayor= Integer.parseInt(registrosCuentas.get(0).getField("CUENTA_MAYOR_ID"));
         setIdCatalogoCuenta(Integer.parseInt(registrosCuentas.get(0).getField("ID_CATALOGO_CUENTA")));
       } //if voCuentaMayor
     } // if getId
     else{
       setDescripcion("RAIZ");
       CuentaMayor cuentaMayor = new CuentaMayor(getEjercicio(),conexion);//dentro de esta clase se requiere pasar la conexion
       String strCeros = cuentaMayor.rellenaRaiz();
       setCuenta(strCeros);
       setNivel(2);
       iCuentaMayor= this.getCuentaMayorId();
      } // else
      registroCuentaMayor = sentencia.registros(sentencia.getComando("webService.select.configuracion","cuentaMayorId=".concat(String.valueOf(iCuentaMayor)).concat("|")),conexion);//se requiere pasar la conexion
      //if(registroCuentaMayor.size()> 0)  
      if(registroCuentaMayor!=null) {
           iConfiguracion= Integer.valueOf(registroCuentaMayor.get(0).getField("CONF_CVE_CTA_CONT_ID"));
       }
      else {
           iConfiguracion = 0;
       }
      registroConfCuenta = sentencia.registros(sentencia.getComando("webService.select.detalleConfiguracion","idConfCve=".concat(String.valueOf(iConfiguracion)).concat("|")),conexion);// se requiere pasar la conexion
      if(registroConfCuenta == null) {
           registroConfCuenta = new ArrayList<Vista>();
       }
      StringBuffer cadena= new StringBuffer("");
      int posicion= 1;
      int indice  = 0;
      int tamanio = 0;
      int agrupar = 0;
      String tabla="";
      String campoCompara = "";
      String caracter= "";
      for (int i= 0; i < registroConfCuenta.size();i++) {
        SubCuenta subCuenta= null;
        tamanio = Integer.parseInt(registroConfCuenta.get(i).getField("TAMANIO"));
        agrupar = Integer.parseInt(registroConfCuenta.get(i).getField("AGRUPAR"));
        caracter= registroConfCuenta.get(i).getField("CARACTER");
        tabla= registroConfCuenta.get(i).getField("DESCRIPCION");
        campoCompara= registroConfCuenta.get(i).getField("CAMPO_COMPARA");
        cadena.append(getCuenta().substring(indice,tamanio+indice));
        for(int x= indice+tamanio;  x< getCuenta().length(); x++) 
          cadena.append(caracter);
        if(posicion<= getNivel().intValue())  {
          subCuenta= new SubCuenta(cadena.substring(indice,  indice + tamanio), cadena.toString(), tamanio, agrupar, caracter,tabla,campoCompara, getEjercicio(), this.getIdCatalogoCuenta(), conexion);
        } // if posicion
        else 
         subCuenta= new SubCuenta(null, null, tamanio, agrupar, caracter,tabla,campoCompara, getEjercicio(), this.getIdCatalogoCuenta(), conexion);
         getSubcuentas().add(subCuenta);
        if(subCuenta.getCuentaPadre().getCuentaContableId()!=null)
          if(subCuenta.getCuentaPadre().getCuentaContableId()!=0)
            //getSubCuentasReales().add(subCuenta);
        cadena.delete(indice+tamanio, cadena.length());
        indice+= (tamanio);
        //voDetalle.next();
        posicion++;
      } // for detalle
      if (getId()==0)
       setNivel(0);
   }   // try
   catch (Exception e) {
     System.err.println("[sia.rf.contabilidad.registroContable.servicios.Cuenta.proceso] Error: "+ e);
     e.printStackTrace();
   }  // catch
   finally {
      registrosCuentas = null;
      registroCuentaMayor = null;
      registroConfCuenta = null;
      sentencia = null; 
   } // finally
  }

 /* public String getCuentaHijos(int iNivel){
    String strCadena = "";
    int iNiveles     = 0;
    try{
      if (iNivel !=0){ 
        if (iNivel != 1)
          iNiveles = iNivel--;
        else
          iNiveles = iNivel;
        for (int x = 0; x< iNiveles; x++ )
         strCadena += subCuentas.get(x).getCuentaCorta();
        strCadena  = strCadena.concat("%");
        for (int x = iNiveles+1;x< subCuentas.size();x++)
          strCadena += subCuentas.get(x).getCuentaNula();
      } // iNivel !=0
      else{
       CuentaMayor cuentaMayor = new CuentaMayor(am, getEjercicio());
       strCadena= cuentaMayor.regresaCondicionRaiz()+'%'; 
      }  
    } // try
    catch (Exception e){
      System.err.println("[sia.rf.contabilidad.registroContable.servicios.Cuenta.getCuentaHijos] Error: "+ e);
      e.printStackTrace();
    } //catch
    return strCadena;
    
  }*/
  
  public String getCuentaReporte(int iNivel){
    String strCadena = "";
    try{
      for (int x = 0; x< iNivel; x++ )
        strCadena += subCuentas.get(x).getCuentaCorta();
      strCadena  = strCadena.concat("%");
    } // try
    catch (Exception e){
      System.err.println("[sia.rf.contabilidad.registroContable.servicios.Cuenta.getCuentaReporte] Error: "+ e);
      e.printStackTrace();
    } //catch
    return strCadena;
    
  }
  
  public ArrayList getPadre(int iNivel){
    ArrayList alPadre = new ArrayList();
    try{
      for (int x = 0; x < iNivel; x++)
        alPadre.add(subCuentas.get(x).getCuentaContable());  
    } // try
    catch (Exception e){
      System.err.println("[sia.rf.contabilidad.registroContable.servicios.Cuenta.getPadre] Error: "+ e);
      e.printStackTrace();
    } //catch
    return alPadre;
  }
  
  public ArrayList getPadreId(int iNivel){
    ArrayList alPadre = new ArrayList();
    try{
      for (int x = 0; x < iNivel; x++)
      {
        alPadre.add(subCuentas.get(x).getCuentaPadre().getCuentaContableId()); 
      }
    } // try
    catch (Exception e){
      System.err.println("[sia.rf.contabilidad.registroContable.servicios.Cuenta.getPadre] Error: "+ e);
      e.printStackTrace();
    } //catch
    return alPadre;
  }
  
  public ArrayList getPadreIdReal(int iNivel){
    ArrayList alPadre = new ArrayList();
    try{
      for (int x = 0; x < iNivel; x++)
        alPadre.add(subCuentasReales.get(x).getCuentaPadre().getCuentaContableId());  
    } // try
    catch (Exception e){
      System.err.println("[sia.rf.contabilidad.registroContable.servicios.Cuenta.getPadre] Error: "+ e);
      e.printStackTrace();
    } //catch
    return alPadre;
  }
  
  public String getCuentaContable(int iNivel, String strClave){
    String strCadena = "";
    try{
      for (int x=0; x < iNivel;x++)
        strCadena+= subCuentas.get(x).getCuentaCorta();
      strCadena = strCadena.concat(strClave);   
      if (iNivel!= subCuentas.size()) {
            for (int y = iNivel+1; y < subCuentas.size(); y++) {
              strCadena += subCuentas.get(y).getCuentaNula();
          }
        }
    } // try
    catch (Exception e){
      System.err.println("[sia.rf.contabilidad.registroContable.servicios.Cuenta.getCuentaContable] Error: "+ e);
      e.printStackTrace();
    } //catch
    return strCadena;
  }
  
  public String getConsultaValida(int iNivel,String strClave){
    String strCadena      = "";
    String strTabla       = "";
    String strCampoCompara= "";
    int iIndice           = 0;
    Formatos formatos     = null;
    String strCondicion          = "";
    try{
      strTabla = subCuentas.get(iNivel-1).getTabla();
      strCampoCompara = subCuentas.get(iNivel-1).getCampoCompara();
      
      formatos = new Formatos(Contabilidad.getInstance().getPropiedad("cuenta.query.consultaValida"),strTabla,strCampoCompara);
      strCadena = formatos.getSentencia();
      iIndice = strCadena.indexOf("?");
      strCondicion = strCadena.substring(0,iIndice).concat(strClave);
      strCadena = strCondicion + strCadena.substring(iIndice+1);
    }
    catch (Exception e){
      System.err.println("[sia.rf.contabilidad.registroContable.servicios.Cuenta.getConsultaValida] Error: "+ e);
      e.printStackTrace();
    } //catch
    return strCadena;
  }
  
  public String getCuentaHijosPadre(int iNivel){
    String strCadena = "";
    int iNiveles     = 0;
    try{
      
        if (iNivel != 1)
          iNiveles = iNivel--;
        else
          iNiveles = iNivel;
        for (int x = 0; x< iNiveles; x++ )
         strCadena += subCuentas.get(x).getCuentaCorta();
      
      strCadena  = strCadena.concat("%");
      
    } // try
    catch (Exception e){
      System.err.println("[sia.rf.contabilidad.registroContable.servicios.Cuenta.getCuentaHijos] Error: "+ e);
      e.printStackTrace();
    } //catch
    return strCadena;
    
  }
  public String getCondicionBusqueda(int iNivel,String strCondicion){
    StringBuffer strCadena = new StringBuffer();
    try{
      if (iNivel!=0){
        strCadena.append("cuenta_contable like '");
        strCadena.append(getCuentaHijosPadre(iNivel));
        strCadena.append("' and ");
      }
      strCadena.append("upper(descripcion) like  upper('%" );
      strCadena.append(strCondicion);
      strCadena.append("%')");
    } //try
    catch (Exception e){
      System.err.println("[sia.rf.contabilidad.registroContable.servicios.Cuenta.getConsultaValida] Error: "+ e);
      e.printStackTrace();
     } //catch
     return strCadena.toString(); 
  }
  
  public boolean getCompara(int iNivel){
    
    if (subCuentas.get(iNivel).getTabla().equals("CONSECUTIVO") || subCuentas.get(iNivel).getTabla().equals("CODIGOS") ) {
          return false;
      }
    else {
          return true;
      }
  }
  
  public int getNivelMaximo(){
    return subCuentas.size();
  }
  
  public String rellenaCeros(int iNivel,String strClave){
    String strCadena = "";
    if(subCuentas==null || subCuentas.isEmpty()) {
          return "";
      }
    int longitud = subCuentas.get(iNivel).getLongitud();
   
    try{
      if (strClave.length() < longitud){
        for (int x = 0; x< subCuentas.get(iNivel).getLongitud()-strClave.length(); x++ )
          strCadena += '0';
       } //if
       strCadena+=strClave;
    } // try
    
    catch (Exception e){
      System.err.println("[sia.rf.contabilidad.registroContable.servicios.Cuenta.rellenaCeros] Error: "+ e);
      e.printStackTrace();
    } //catch
    return strCadena;
  }
  
  public boolean validaLongitud(int iNivel,String strClave){
    boolean bandera = true;
    try{
      if (strClave.length() > subCuentas.get(iNivel).getLongitud()) {
            bandera = false;
        }
    } // try
    catch (Exception e){
      System.err.println("[sia.rf.contabilidad.registroContable.servicios.Cuenta.validaLongitud] Error: "+ e);
      e.printStackTrace();
    } //catch
    return bandera;
  }
  
  public ArrayList longitudNiveles(){
    ArrayList arNiveles = new ArrayList();
    try{
      for (int i=0; i <=subCuentas.size()-1;i++)
        arNiveles.add(subCuentas.get(i).getLongitud());
    }
    catch (Exception e ){
      Error.mensaje(this,e,"longitudNiveles","error al regresar la longitud de mayor");
    }
    return arNiveles;
  }
  
 /* public ArrayList regresaLongitudMayor(String strClave){
    ArrayList  arNiveles = new ArrayList();
    ViewObject voMayor = null;
    Formatos formatos = null;
    try{
      if (getId()> 0) {    
        formatos = new Formatos(Contabilidad.getInstance().getPropiedad("clasificadorCuenta.query.longitudCaracter"),strClave, String.valueOf(getEjercicio()), String.valueOf(this.getIdCatalogoCuenta()));
        voMayor = am.createViewObjectFromQueryStmt("VistaMayor", formatos.getSentencia());
        voMayor.executeQuery();
        if (voMayor.getRowCount() > 0){
          Cuenta cuentaMayor = new Cuenta(Integer.parseInt(voMayor.first().getAttribute(0).toString()), am);
          arNiveles = cuentaMayor.longitudNiveles();
        }  
      }   
    }
    catch (Exception e){
      Error.mensaje(this,e,"regresaLongitudMayor","error al regresar la longitud de mayor");
    }
    finally{
      if(voMayor!=null)
        voMayor.remove();
      if(am.findViewObject("VistaMayor")!=null)
        am.findViewObject("VistaMayor").remove();
      formatos = null;
    }
    return arNiveles;
  }*/
  
  public String regresaCaracter(){
    return subCuentas.get(0).getCaracter();
  }
  
  
/*  public int getNivelMaximoTotal(){
    int iNivelTotal = 0;
    ViewObject voNivelMaximoTotal = null;
    Formatos formatos = null;
    try{
      formatos = new Formatos(Contabilidad.getInstance().getPropiedad("cuenta.query.nivelMaximo"),String.valueOf(this.getIdCatalogoCuenta()));
      voNivelMaximoTotal= am.createViewObjectFromQueryStmt("VistaNivelTotal", formatos.getSentencia());
      voNivelMaximoTotal.executeQuery();
      voNivelMaximoTotal.first();
      if(voNivelMaximoTotal.getRowCount()> 0)  
        iNivelTotal= Integer.parseInt(voNivelMaximoTotal.getCurrentRow().getAttribute(0).toString());
      else 
        iNivelTotal = 0;
   }
    catch (Exception e){
      System.err.println("[sia.rf.contabilidad.registroContable.servicios.Cuenta.getNivelMaximoTotal] Error: "+ e);
      e.printStackTrace();
    } //catch
    finally{
        if(voNivelMaximoTotal!=null)
            voNivelMaximoTotal.remove();
        if(am.findViewObject("VistaNivelTotal")!=null)
            am.findViewObject("VistaNivelTotal").remove();
    }
    return iNivelTotal;
  }*/
  /*public boolean isBorrar(){
    ViewObject voBorrar = null;
    boolean bandera = false;
    try{
      if (getId()> 0) {
        String strCondicion = getCuentaHijos(getNivel());
        voBorrar = am.findViewObject("VistaMostrarHijosCuentaContable1");
        voBorrar.setWhereClause("cuenta_contable like '" + strCondicion +"' and cuenta_contable <> '" + getCuenta() + "'");
        voBorrar.executeQuery();
        if (voBorrar.getRowCount() ==0)
          bandera= true;
      }   
      
    }
    catch(Exception e){
      bandera = false;
      System.err.println("[sia.rf.contabilidad.registroContable.servicios.Cuenta.isBorrar] Error: "+ e);
      e.printStackTrace();
    }// catch
    finally{
      //voBorrar.remove();
      //Configuration.releaseRootApplicationModule(modulo, false);
   
      return bandera;
 //   }// finally
  }*/
  
/*  public boolean borrar(){
    Formatos formatos = null;
    boolean bandera = false;
    try{
      formatos = new Formatos(Contabilidad.getInstance().getPropiedad("cuenta.query.borrar"),id.toString());
      am.getTransaction().executeCommand(formatos.getSentencia());
      am.getTransaction().commit();
      bandera = true;
    }//try  
    catch(Exception e){
      System.err.println("[sia.rf.contabilidad.registroContable.servicios.Cuenta.borrar] Error: "+ e);
      e.printStackTrace();
    }// catch
    finally{
      formatos = null;  
    }
    return bandera;
  }*/
  
 public ArrayList getLongitudNivel(){
    ArrayList alNiveles = new ArrayList();
    try{
      for(int i= 0; i < subCuentas.size(); i++) {
            alNiveles.add(subCuentas.get(i).getLongitud());
        }
     }
    catch (Exception e) {
      System.err.println("[sia.rf.contabilidad.registroContable.servicios.Cuenta.getLongitudNivel] Error: "+ e);
      e.printStackTrace();
    }
    finally{
      return alNiveles;
    }
  }
  
  public ArrayList getCuentaCorta(){
    ArrayList alCuentaCorta = new ArrayList();
    try{
      for(int i= 0; i < subCuentas.size(); i++)
        alCuentaCorta.add(subCuentas.get(i).getCuentaCorta());
     }
    catch (Exception e) {
      System.err.println("[sia.rf.contabilidad.registroContable.servicios.Cuenta.getLongitudNivel] Error: "+ e);
      e.printStackTrace();
    }
    finally{
      return alCuentaCorta;
    }
  }
  
  public ArrayList getCuentaCortaDesc(){
    ArrayList alCuentaCorta = new ArrayList();
    try{
      for(int i= 0; i < subCuentas.size(); i++)
        alCuentaCorta.add(subCuentas.get(i).getCuentaPadre().getCuentaContable());
     }
    catch (Exception e) {
      System.err.println("[sia.rf.contabilidad.registroContable.servicios.Cuenta.getLongitudNivel] Error: "+ e);
      e.printStackTrace();
    }
    finally{
      return alCuentaCorta;
    }
  }
  
/*  public int verificarCuenta(String strCuenta,String strFechaAfectacion,int intParametro){
    ViewObject voCuenta = null;
    Formatos formatos = null;
    int cuentaContableId = 0;
    try{
      formatos = new Formatos(Contabilidad.getInstance().getPropiedad("procesos.polizas.obtenerCuentaContable"),String.valueOf(getEjercicio()),strCuenta, String.valueOf(this.getIdCatalogoCuenta()));
      voCuenta = am.createViewObjectFromQueryStmt("VistaCtaContable",formatos.getSentencia());
      voCuenta.executeQuery();
      if(voCuenta.getRowCount()!=0){
        cuentaContableId = ((oracle.jbo.domain.Number)voCuenta.first().getAttribute(0)).intValue();
        Cuenta cuenta = new Cuenta(cuentaContableId, am);
        if (intParametro==0)
          if(cuenta.getNivel().intValue()<cuenta.getNivelMaximo())
            cuentaContableId=-1;
      }       
      else
        cuentaContableId=0;
    }//try  
    catch(Exception e){
      System.err.println("[sia.rf.contabilidad.registroContable.servicios.Cuenta.borrar] Error: "+ e);
      e.printStackTrace();
    }// catch
    finally{
      if(voCuenta!=null)
        voCuenta.remove();
      if(am.findViewObject("VistaCtaContable")!=null)
        am.findViewObject("VistaCtaContable").remove();
      formatos = null;
    }
    return cuentaContableId;
  }
  */
/*  public boolean isSheet(ModuloRegistroContableImpl modulo, int ejercicio){
    ViewObject vista = null;
    Formatos formatos = null;
    boolean control = false;
    try{
      String condicion = this.getCuentaHijos(this.getNivel());
      formatos = new Formatos(Contabilidad.getInstance().getPropiedad("registroContable.query.isSheet"),condicion, String.valueOf(ejercicio), String.valueOf(this.getIdCatalogoCuenta()));
      vista = modulo.createViewObjectFromQueryStmt("VistaCuentasContables",formatos.getSentencia());
      vista.setOrderByClause("cuenta_contable");
      vista.executeQuery();
      if(vista.getRowCount()<=1){
        control = true;
      }
    }
    catch(Exception e){
      Error.mensaje(e, "SIAFM");
    }
    finally{
      if(vista!=null)
        vista.remove();
      if(am.findViewObject("VistaCuentasContables")!=null)
        am.findViewObject("VistaCuentasContables").remove();
      formatos = null;
    }
    return control;
  }*/
  
  /*public int maximoSecuencia() {
	int iMaxSecuencia=0;
	ViewObject voMaxBit =null;
  try {
		String strQuery = "select max(cuenta_contable_id) cuenta_contable_id from RF_TR_CUENTAS_CONTABLES";
		voMaxBit = am.createViewObjectFromQueryStmt("voMB",strQuery);
		voMaxBit.executeQuery();
		String strSecuencia = "";
		if (voMaxBit.getRowCount()!=0){
		  if(voMaxBit.first().getAttribute(0)!=null){
			strSecuencia = voMaxBit.first().getAttribute(0).toString();
			iMaxSecuencia = Integer.parseInt(strSecuencia);
		  }//if
		}//if
	  }//try
	  catch (Exception e) {
		System.out.print("[sia.rf.contabilidad.registroContable.acciones." +
		  "AccionAgregarCuentaContable.maximoSecuencia] Error: "+e);
		e.printStackTrace();
		iMaxSecuencia = 0;
	  }//catch
	  finally{
		voMaxBit.remove();
	  }//finally
	  return iMaxSecuencia;
  }//fin maximoSecuencia  
  */
  public String patronNivel(int nivel){
    String patron = "";
    try{
      for (int i = 0; i < (Integer)this.getLongitudNivel().get(nivel-1); i++)  {
        patron = patron + this.regresaCaracter();
      }//end for
    }//end try
    catch(Exception e){
      Error.mensaje(e, "SIAFM");
    }
    return patron;
  }
  
 /* public int crear(String cuenta)throws Exception{
	int secuenciaCuenta = 0;
  Formatos formatos = null;
	ArrayList lista = null;
	String cuentaCorta = null;
	try  {
	  secuenciaCuenta = this.maximoSecuencia();
	  if(secuenciaCuenta != 0){
	    secuenciaCuenta++;
	    CuentaMayor cuentaMayor = new CuentaMayor(am, getEjercicio());
		cuentaCorta = cuenta.substring(0,5);
	    lista = cuentaMayor.getCuentaMayor(cuentaCorta, Fecha.getHoy());
      formatos = new Formatos(Contabilidad.getInstance().getPropiedad("registroContable.query.registrarCuentaContable"),String.valueOf(secuenciaCuenta), 
                      String.valueOf(lista.get(1)), "'"+cuenta+"'", String.valueOf(lista.get(4)), "'Cuenta agregada automaticamente'", 
                      "'"+Fecha.formatear(Fecha.FECHA_ESTANDAR,Fecha.getRegistro())+"'", "'99991231'", String.valueOf(0.00), String.valueOf(47372), String.valueOf(this.getIdCatalogoCuenta()));
	    if(am.getTransaction().executeCommand(formatos.getSentencia())!=1){
		  secuenciaCuenta = 0;
		}
	  }
	}//end try 
	catch(Exception e)  {
	  Error.mensaje(e, "CONTABILIDAD");
	  e.printStackTrace();
	  throw new Exception();
	}//end catch 
	finally{
	  return secuenciaCuenta;
	}//end finally
  }//public int crear()
  */
 /* public static void main(String[] args) {
	String AMDEF = "sia.rf.contabilidad.registroContable.servicios.ModuloRegistroContable";
	String CONFIG= "ModuloRegistroContableLocal";
	ModuloRegistroContableImpl am = null;
	try  {
	  am = (ModuloRegistroContableImpl)Configuration.createRootApplicationModule(AMDEF, CONFIG);
	  Cuenta cuenta = new Cuenta(am, 1);
	  cuenta.crear("111120001011400110003");
	  am.getTransaction().commit();
	}//end try 
	catch(Exception e)  {
	  Error.mensaje(e, "CONTABILIDAD");
	  e.printStackTrace();
	  am.getTransaction().rollback();
	}//end catch 
	finally{
	  Configuration.releaseRootApplicationModule(am, false);
	}//end finally  */
   /* ArrayList alPadre = new ArrayList();
    alPadre =cuenta.regresaLongitudMayor("11203");
    for (int x=0; x< alPadre.size();x++)
    
//    Cuenta cuenta = new Cuenta(1,new ArrayList());
//    
 //ArrayList alPadre = new ArrayList();
// ArrayList alCuenta = new ArrayList();
 //  alPadre = cuenta.getCuentaCortaDesc(); 
//    alCuenta = cuenta.getCuentaCorta();
//System.out.println("claudio");
 // for (int x=0; x< alPadre.size();x++)
  //   System.out.println(alPadre.get(x).toString());
//  
    
//   }   
//   System.out.println("cuentacorta");   
//    for (int x=0; x< alCuenta.size();x++){
//       
//      System.out.println(alCuenta.get(x));
//     
//    }   
//    int indice = 0;
//    String strCuenta = "";
//    System.out.println(alPadre.size());
//    for (int i = 0; i < alPadre.size(); i++){
//      strCuenta+=cuenta.getCuenta().substring(indice,indice + Integer.parseInt(alPadre.get(i).toString()));
//      System.out.println(strCuenta);
//      indice = Integer.parseInt(alPadre.get(i).toString());
//    }
  
   
//    Cuenta cuenta1 = new Cuenta();
//    alPadre = new ArrayList();
//    alPadre = cuenta1.getPadre(0); 
//    for (int x=0; x< alPadre.size();x++)
//      System.out.println(alPadre.get(x).toString());
      
  */
 // }


 /*   public void setAm(ModuloRegistroContableImpl am) {
        this.am = am;
    }

    public ModuloRegistroContableImpl getAm() {
        return am;
    }
*/
}
