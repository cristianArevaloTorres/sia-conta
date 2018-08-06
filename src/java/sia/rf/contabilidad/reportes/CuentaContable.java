package sia.rf.contabilidad.reportes;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRScriptletException;

import sia.db.dao.DaoFactory;
import sia.db.sql.Sentencias;

import sia.libs.formato.Error;

import sia.libs.periodo.Fecha;

import sia.scriptlets.BarraProgreso;

import sia.rf.contabilidad.registroContable.servicios.Cuenta;

public class CuentaContable extends BarraProgreso {
  private Cuenta cuenta = null;
  private ArrayList alNiveles = new ArrayList();

  public CuentaContable() {
  }
  
  public void afterReportInit() throws JRScriptletException {
    super.afterReportInit();
    try {
      if (checkParameter("FECHA_CONSOLIDACION") != null) {
        Fecha fechaPeriodo = new Fecha(getParameterValue("FECHA_CONSOLIDACION").toString(), "/");
        fechaPeriodo.getDiasEnElMes();
        String fecha = sia.libs.formato.Fecha.formatear(6, fechaPeriodo.getTerminoMes().toString()).toUpperCase();
        fechaPeriodo.addMeses(-1);
        setVariableValue("FECHA_MAY_CONSOLIDACION", fecha);
      } //if
    } //try
    catch (JRScriptletException e) {
      Error.mensaje(e, "SIAFM");
    } //catch
  } //afterReportInit

  public void beforeGroupInit(String groupName) throws JRScriptletException {
    super.beforeGroupInit(groupName);
    //ModuloRegistroContableImpl am = null;    
    try  {
      //am = (ModuloRegistroContableImpl)Configuration.createRootApplicationModule("sia.rf.contabilidad.registroContable.servicios.ModuloRegistroContable", 
        //"ModuloRegistroContableLocal");      
      if (groupName.equals("CUENTA_MAYOR")) {
       // System.out.println("CUENTA_CONTABLE_ID="+this.getFieldValue("CUENTA_CONTABLE_ID").toString());
        int iCuentaContableId = Integer.parseInt(this.getFieldValue("CUENTA_CONTABLE_ID").toString());
        //System.out.println("Antes de entrar a clase de cuenta");
        cuenta = new Cuenta(iCuentaContableId, getConnection());
        //System.out.println("Sale de la clase de cuenta");
        alNiveles = cuenta.getLongitudNivel();
      } // if  
      if (groupName.equals("CUENTA_CONTABLE1"));      
    } 
    catch (Exception ex)  {
      ex.printStackTrace();
    } 
   /* finally  {
      if(am !=null){
        Configuration.releaseRootApplicationModule(am, true);
      }
      am = null;       
    }*/
  } // beforeGroupInit

  public void afterDetailEval() throws JRScriptletException {
    super.afterDetailEval();
    String strCuentaContable = this.getFieldValue("CUENTA_CONTABLE").toString();
    //String reporte           = getParameterValue("REPORTE").toString();
    String naturaleza        = null;
    String mesAnt            = null;
    String mesAct            = null;
    Double mesAntCargoAcum   = 0.0;
    Double mesAntAbonoAcum   = 0.0;
    Double cargoPeriodo      = 0.0;
    Double abonoPeriodo      = 0.0;
    int idCatalogoCuenta     = 0;
    int ejercicio            = 0; 
    int indice               = 0;
    if (checkVariable("nivel0",1) == null ) {
      for (int i = 0; i < alNiveles.size(); i++) {
        setVariableValue("nivel" + i, 
                         obtenerNivel(strCuentaContable, indice, i)==" "?null:obtenerNivel(strCuentaContable, indice, i));
        indice = indice + Integer.parseInt(alNiveles.get(i).toString());
      } // for i 
    } //if
    else {
      String cuentaContable = "";
      for (int i = 0; i < alNiveles.size(); i++) {
       // System.out.println("FOR  +i");
        strCuentaContable += obtenerNivel(strCuentaContable, indice, i)+" ";
        indice = indice + Integer.parseInt(alNiveles.get(i).toString());
      } //for
      setVariableValue("cuenta_contable", cuentaContable);
    } //else
    /*if(reporte.equals("CuentasContablesSaldo")){
      mesAct           = getParameterValue("MES_ACTUAL").toString();
      if(mesAct.equals("ENE")){
        naturaleza       = getFieldValue("NATURALEZA").toString();
        mesAnt           = getParameterValue("MES_ANTERIOR").toString();
        idCatalogoCuenta = Integer.parseInt(getParameterValue("ID_CATALOGO_CUENTA").toString());
        ejercicio        = Integer.parseInt(getParameterValue("EJERCICIO").toString());
        cargoPeriodo     = Double.parseDouble(getFieldValue("CARGOS_PER").toString());
        abonoPeriodo     = Double.parseDouble(getFieldValue("ABONOS_PER").toString());
        mesAntCargoAcum = obtenerValor(idCatalogoCuenta, ejercicio, strCuentaContable, mesAnt, "D");
        mesAntAbonoAcum = obtenerValor(idCatalogoCuenta, ejercicio, strCuentaContable, mesAnt, "A");
        setVariableValue("MES_ANT_CARGO_ACUM",mesAntCargoAcum);
        setVariableValue("MES_ANT_ABONO_ACUM",mesAntAbonoAcum);
        setVariableValue("ABONO_ACUM", obtenerAcumulados(mesAntCargoAcum, mesAntAbonoAcum, naturaleza, cargoPeriodo, abonoPeriodo, "abono"));
        setVariableValue("CARGO_ACUM", obtenerAcumulados(mesAntCargoAcum, mesAntAbonoAcum, naturaleza, cargoPeriodo, abonoPeriodo, "cargo"));
      }
    }*/
  } // afterDetailEval

  private boolean validaNiveles(String strCadena) {
    boolean bBandera = false;
    for (int i = 0; i < strCadena.length() && !bBandera; i++) {
      if (!strCadena.substring(i, i + 1).equals("0"))
        bBandera = true;
    } // for i
    return bBandera;
  } // validaNiveles

  public String obtenerNivel(String strCuentaContable, int indice, 
                              int i) throws JRScriptletException {
    if (validaNiveles(strCuentaContable.substring(indice, 
                                                  indice + Integer.parseInt(alNiveles.get(i).toString()))))
      return strCuentaContable.substring(indice, 
                                         indice + Integer.parseInt(alNiveles.get(i).toString()));
    return " ";
  } //obtenerNivel
  
  
  public Double obtenerValor(int idCatalogocuenta, int ejercicio, String cuentaContable, String mes, String naturaleza){
    Map parametros = new HashMap();
    Double regresar = 0.0;
    Sentencias sentencia = null;
    try{
      sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD, Sentencias.XML);
      if (naturaleza.equals("D"))
        parametros.put("mes", mes.concat("_cargo_acum"));
      else
        parametros.put("mes", mes.concat("_abono_acum"));
      parametros.put("cuentaContable", cuentaContable);
      parametros.put("idCatalogoCuenta", idCatalogocuenta);
      parametros.put("ejercicio", ejercicio - 1);
      regresar =Double.parseDouble(sentencia.consultar("registroContable.select.obtenerSaldoMesAnterior",parametros).toString());
    }
    catch(Exception e) {
      Error.mensaje(e, "SIAFM");
    }
    finally{
      parametros = null;
      sentencia = null;
    }
    return regresar;
  }
  
  public Double obtenerAcumulados(Double mesAntCargoAcum, Double mesAntAbonoAcum, String naturaleza, Double cargoPeriodo, Double abonoPeriodo, String operacion){
    Double regresar = 0.0;
    if(naturaleza.equals("D")){
      regresar = operacion.equals("cargo")?mesAntCargoAcum - mesAntAbonoAcum + cargoPeriodo:abonoPeriodo;
    }
    else{
      if(naturaleza.equals("A"))
        regresar = operacion.equals("abono")?mesAntAbonoAcum - mesAntCargoAcum + abonoPeriodo:cargoPeriodo;
    }
    return regresar;
  }
}