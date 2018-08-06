package sia.rf.contabilidad.registroContable.acciones;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.Map;

import sia.beans.seguridad.Autentifica;

import sia.db.dao.DaoFactory;
import sia.db.sql.Sentencias;

import sia.db.sql.Vista;

import sia.libs.formato.Error;
import sia.libs.formato.Fecha;
import sia.rf.contabilidad.registroContableNuevo.bcConfiguracionRenglon;
import sia.rf.contabilidad.registroContableNuevo.bcConfiguracionGeneral;

public class ControlRegistro {

  private String unidad;
  private int ambito;
  private int entidad;
  private int tipoUsuario;
  private int pais;
  private int indInicial;
  private String ambEntFormateada;
  private String uniEjecFormateada;
  private String programaFormateado;
  private String fechaAfectacion;
  private int ejercicio;
  private String fechaEstablecida;
  private int idCatalogoCuenta;
  private int nivelMaximo;
  private int idCatCuenta;
  private List<bcConfiguracionRenglon> configuracion;
  private String pagina = null;
  
  public static final int USUARIO_ADMIN=1;
  public static final int USUARIO_INTERMEDIO=2;
  public static final int USUARIO_BAJO=3;
  

  public ControlRegistro() {
    unidad=null;
    ambito=-1;
    entidad=-1;
    tipoUsuario=-1;
    pais=-1;
    indInicial=-1;
    ambEntFormateada=null;
    uniEjecFormateada=null;
    programaFormateado = null;
    fechaAfectacion=null;
    ejercicio = -1;
    nivelMaximo=0;
  }
  
  public void inicializa(int idCatalogoCuenta){
    setIdCatalogoCuenta(idCatalogoCuenta);
    setNivelMaximo(obtenerMaximoNivel());
  }
  
  public void inicializa(Autentifica autentifica, int idCatalogoCuenta)
  {
    unidad=autentifica==null?null:autentifica.getUnidadEjecutora();
    ambito=autentifica==null?-1:Integer.parseInt(autentifica.getAmbito());
    entidad=autentifica==null?-1:Integer.parseInt(autentifica.getEntidad());
    pais=autentifica==null?-1:Integer.parseInt(autentifica.getPais());
    obtenerTipoUsuario(unidad,ambito);
    ambEntFormateada=null;
    uniEjecFormateada=null;
    setIdCatalogoCuenta(idCatalogoCuenta);
    //setNivelMaximo(obtenerMaximoNivel());
  }
  
  public boolean isUsuarioAdmin(){
    return obtenerTipoUsuario(getUnidad(),getAmbito())==USUARIO_ADMIN;
  }
  
  public boolean isUsuarioIntermedio(){
    return obtenerTipoUsuario(getUnidad(),getAmbito())==USUARIO_INTERMEDIO;
  }
  
  public boolean isUsuarioBajo(){
    return obtenerTipoUsuario(getUnidad(),getAmbito())==USUARIO_BAJO;
  }
  
  private int obtenerTipoUsuario(String Unidad, int ambito){
    if(tipoUsuario==-1){
        if(ambito==3)
          tipoUsuario=USUARIO_BAJO;
        else
          tipoUsuario=USUARIO_ADMIN;
    }
    //System.out.println("Tipo usuario: "+ getTipoUsuario());
    return tipoUsuario;
  }
  
  public int obtenerMaximoNivel(){
    Sentencias sentencias      = null;
    Map parametros = null;
    int maximoNivel = -1;
    List<Vista> registros = null;
    try {
      registros = new ArrayList<Vista>();
      parametros = new HashMap();
      sentencias = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD);
      parametros.put("idCatalogoCuenta", getIdCatalogoCuenta());
      parametros.put("ejercicio",getEjercicio());
      registros = sentencias.registros("clasificadorCuenta.select.obtenerMaxNivel",parametros); 
      if(registros.get(0).getField("NIVEL")!=null){
        maximoNivel = registros.get(0).getInt("NIVEL");
      }
      else{
        registros = sentencias.registros("clasificadorCuenta.select.obtenerMaxNivelConf",parametros); 
        if(registros.get(0).getField("NIVEL")!=null)
          maximoNivel = registros.get(0).getInt("NIVEL");
      }
    }
    catch (Exception e) {
      Error.mensaje(e, "CONTABILIDAD");
    }
    finally { 
      parametros = null;
      sentencias = null;
    }
    return maximoNivel;
  }
  
  public int obtenerTipoUsuario(String unidad){
      return tipoUsuario = USUARIO_ADMIN;
  }

  public void setUnidad(String unidad) {
    this.unidad = unidad;
  }

  public String getUnidad() {
    return unidad;
  }

  public void setAmbito(int ambito) {
    this.ambito = ambito;
  }

  public int getAmbito() {
    return ambito;
  }

  public void setEntidad(int entidad) {
    this.entidad = entidad;
  }

  public int getEntidad() {
    return entidad;
  }

  public int getTipoUsuario() {
    return tipoUsuario;
  }

  public void setPais(int pais) {
    this.pais = pais;
  }

  public int getPais() {
    return pais;
  }

  public void setIndInicial(int indInicial) {
    this.indInicial = indInicial;
  }

  public int getIndInicial() {
    return indInicial;
  }
  
  public String getUEAFormateada()
  {
    return uniEjecFormateada!=null && ambEntFormateada!=null?uniEjecFormateada+ambEntFormateada:null;
  }
  
  public int getIndFinal()
  {
    return indInicial+getUEAFormateada()==null?0:getUEAFormateada().length();
  }

  public void setAmbEntFormateada(String ambEntFormateada) {
    this.ambEntFormateada = ambEntFormateada;
  }

  public String getAmbEntFormateada() {
    return ambEntFormateada;
  }

  public void setUniEjecFormateada(String uniEjecFormateada) {
    this.uniEjecFormateada = uniEjecFormateada;
  }

  public String getUniEjecFormateada() {
    return uniEjecFormateada;
  }

  public void setProgramaFormateado(String programaFormateado) {
    this.programaFormateado = programaFormateado;
  }

  public String getProgramaFormateado() {
    return programaFormateado;
  }

  public void setFechaAfectacion(String fechaAfectacion) {
    this.fechaAfectacion = fechaAfectacion;
  }

  public String getFechaAfectacion() {
    return fechaAfectacion;
  }

  public void setEjercicio(int ejercicio) {
    this.ejercicio = ejercicio;
    this.setFechaEstablecida(String.valueOf(ejercicio));
  }

  public int getEjercicio() {
    return ejercicio;
  }

  public String getFechaEstablecida() {
    return fechaEstablecida;
  }

  private void setFechaEstablecida(String ejercicio) {
    String fecha=Fecha.formatear(Fecha.FECHA_CORTA,Fecha.getRegistro());
    this.fechaEstablecida=fecha.substring(0,fecha.lastIndexOf("/")).concat("/").concat(ejercicio);
  }

  public void setIdCatalogoCuenta(int idCatalogoCuenta) {
    this.idCatalogoCuenta = idCatalogoCuenta;
  }

  public int getIdCatalogoCuenta() {
    return idCatalogoCuenta;
  }

  public void setConfiguracion(List<bcConfiguracionRenglon> configuracion) {
    this.configuracion = configuracion;
  }

  public List<bcConfiguracionRenglon> getConfiguracion() {
    if(idCatCuenta != idCatalogoCuenta || configuracion == null) {
      idCatCuenta = idCatalogoCuenta;
      bcConfiguracionGeneral conf = new bcConfiguracionGeneral(getIdCatalogoCuenta(),getEjercicio());
      configuracion = conf.getConfiguracion();
      conf = null;
    }
    return configuracion;
  }

  public void setPagina(String pagina) {
    this.pagina = pagina;
  }

  public String getPagina() {
    return pagina;
  }

  private void setNivelMaximo(int nivelMaximo) {
    this.nivelMaximo = nivelMaximo;
  }

  public int getNivelMaximo() {
    return nivelMaximo;
  }
}
