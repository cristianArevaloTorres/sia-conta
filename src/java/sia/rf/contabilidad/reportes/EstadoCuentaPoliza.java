package sia.rf.contabilidad.reportes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRScriptletException;

import sia.db.dao.DaoFactory;
import sia.db.sql.Sentencias;

import sia.rf.contabilidad.registroContable.servicios.Cuenta;

import sia.scriptlets.BarraProgreso;

public class EstadoCuentaPoliza extends BarraProgreso{

 
  public void afterDetailEval() throws JRScriptletException {
    super.afterDetailEval();
    String cuentaContable    = null;
    String naturaleza        = null;
    String mesAnt            = null;
    String mesAct            = null;
    Double mesAntCargoAcum   = 0.0;
    Double mesAntAbonoAcum   = 0.0;
    Double cargoPeriodo      = 0.0;
    Double abonoPeriodo      = 0.0;
    int idCatalogoCuenta     = 0;
    int ejercicio            = 0; 
    //ModuloRegistroContableImpl am = null;    
    try  {
      //am = (ModuloRegistroContableImpl)Configuration.createRootApplicationModule("sia.rf.contabilidad.registroContable.servicios.ModuloRegistroContable", 
       // "ModuloRegistroContableLocal");      
      ArrayList cuentaCorta     = new ArrayList();
      ArrayList descCuentaCorta = new ArrayList();
      int iCuentaContableId     = Integer.parseInt(this.getFieldValue("CUENTA_CONTABLE_ID").toString());
      //int tipoPolizaId = Integer.valueOf(getFieldValue("CLASIFICACION_POLIZA_ID").toString());
      Cuenta cuenta = new Cuenta(iCuentaContableId, getConnection());
      //DatosReportes datosReportes = new DatosReportes();
      cuentaCorta = cuenta.getCuentaCorta();
      descCuentaCorta = cuenta.getCuentaCortaDesc();
        /*if(tipoPolizaId == 1){
          this.setVariableValue("CLASIFICACION_POLIZA","");
        }
        else{
          if(checkField("POLIZA_REFERENCIA")!= null){
            poliza = datosReportes.getPolizaCancelada(getFieldValue("POLIZA_REFERENCIA").toString());
            this.setVariableValue("CLASIFICACION_POLIZA","De Cancelacion a la póliza "+poliza);
          }
          else{
            poliza="";
            this.setVariableValue("CLASIFICACION_POLIZA","Póliza Cancelada");
          }
        }*/
      for (int i = 0; i < cuentaCorta.size(); i++)  {
        setVariableValue("nivel" + i, cuentaCorta.get(i));
        setVariableValue("descripcion" + i, descCuentaCorta.get(i));
      }  
      mesAct           = getParameterValue("MES_ACTUAL").toString();
      if(mesAct.equals("ENE")){
        cuentaContable   = getFieldValue("CUENTA_CONTABLE") .toString();
        idCatalogoCuenta = Integer.parseInt(getParameterValue("ID_CATALOGO_CUENTA").toString());
        naturaleza       = getFieldValue("NATURALEZA").toString();
        mesAnt           = getParameterValue("MES_ANTERIOR").toString();
        ejercicio        = Integer.parseInt(getParameterValue("EJERCICIO").toString());
        cargoPeriodo     = Double.parseDouble(getFieldValue("CARGOS_PER").toString());
        abonoPeriodo     = Double.parseDouble(getFieldValue("ABONOS_PER").toString());
        //mesAntCargoAcum = obtenerValor(idCatalogoCuenta, ejercicio, cuentaContable, mesAnt, "D");
        //mesAntAbonoAcum = obtenerValor(idCatalogoCuenta, ejercicio, cuentaContable, mesAnt, "A");
        //setVariableValue("MES_ANT_CARGO_ACUM",mesAntCargoAcum);
        //setVariableValue("MES_ANT_ABONO_ACUM",mesAntAbonoAcum);
        //setVariableValue("ABONO_ACUM", obtenerAcumulados(mesAntCargoAcum, mesAntAbonoAcum, naturaleza, cargoPeriodo, abonoPeriodo, "abono"));
        //setVariableValue("CARGO_ACUM", obtenerAcumulados(mesAntCargoAcum, mesAntAbonoAcum, naturaleza, cargoPeriodo, abonoPeriodo, "cargo"));
      }
    } 
    catch (Exception ex)  {
      ex.printStackTrace();
    }
    /*finally  {
      if(am !=null){
        Configuration.releaseRootApplicationModule(am, true);
      }
      am = null;            
    }*/
  }
  
  public Double obtenerValor(int idCatalogocuenta, int ejercicio, String cuentaContable, String mes, String naturaleza){
    Map parametros = new HashMap();
    Double regresar = 0.0;
    Sentencias sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD, Sentencias.XML);
    if (naturaleza.equals("D"))
      parametros.put("mes", mes.concat("_cargo_acum"));
    else
      parametros.put("mes", mes.concat("_abono_acum"));
    parametros.put("cuentaContable", cuentaContable);
    parametros.put("idCatalogoCuenta", idCatalogocuenta);
    parametros.put("ejercicio", ejercicio - 1);
    regresar =Double.parseDouble(sentencia.consultar("registroContable.select.obtenerSaldoMesAnterior",parametros).toString());
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
