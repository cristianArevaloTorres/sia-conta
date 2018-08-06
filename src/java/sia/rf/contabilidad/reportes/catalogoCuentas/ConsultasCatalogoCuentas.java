package sia.rf.contabilidad.reportes.catalogoCuentas;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import sia.db.dao.DaoFactory;
import sia.db.sql.Sentencias;

import sia.libs.formato.Error;

import sia.libs.formato.Formatos;
import sia.libs.recurso.Contabilidad;

import sia.rf.contabilidad.registroContable.acciones.ControlRegistro;

import sia.xml.Dml;

public class ConsultasCatalogoCuentas {
  private String cierre;
  private ControlRegistro controlReg;
  private String tipoPolizas;
  
  public ConsultasCatalogoCuentas(String cierre, ControlRegistro controlReg) {
    this(cierre,controlReg,"0");
    //setCierre(cierre);
    //setControlReg(controlReg);
  }
  
    public ConsultasCatalogoCuentas(String cierre, ControlRegistro controlReg, String tipoPolizas) {
      setCierre(cierre);
      setControlReg(controlReg); 
      setTipoPolizas(tipoPolizas);
    }
  
  public StringBuffer reporteSaldosRangos(String fechaReporte, String formato, String reporte, List lista, boolean saldosCero,String strMesActual, String strMesAnterior){
  //probando cuenta
    Map mapParams = new HashMap();
    Dml dml                       = null;
    sia.libs.formato.Fecha fecha  = null;
    StringBuffer sentencia        = null;
    StringBuffer sentencia2        = null;
    String desdeHasta []          = null;
    StringBuffer cargos_acum      = null;
    StringBuffer abonos_acum      = null;
    String mesActCargo = null;
    String mesActAbono = null;
    String mesAntCargoAcum = null;
    String mesAntAbonoAcum = null;
    try  {
      dml = new Dml(Dml.CONTABILIDAD);
      sentencia = new StringBuffer();
      sentencia2 = new StringBuffer();
      cargos_acum = new StringBuffer();
      abonos_acum = new StringBuffer();
      for (int i = 0; i < lista.size(); i++) {
        desdeHasta = String.valueOf(lista.get(i)).split(",");
        if(desdeHasta.length==2){
          mapParams.put("desde".concat(String.valueOf(i)), desdeHasta[0]);
          mapParams.put("hasta".concat(String.valueOf(i)), desdeHasta[1]);
        }
        else {
          if(i==1){
            mapParams.put("desde".concat(String.valueOf(i)), "0");
            mapParams.put("hasta".concat(String.valueOf(i)), "0"); 
          }
          else{
            mapParams.put("desde".concat(String.valueOf(i)), "0000");
            mapParams.put("hasta".concat(String.valueOf(i)), "0000");
          }

        }
      }
      if (lista.get(3)!= null)    
        mapParams.put("hasta4",mapParams.get("hasta4").toString().equals("0000")?"9999":mapParams.get("hasta4").toString());
      String hasta4 = mapParams.get("hasta4").toString();
      //if(getCierre()==3 && mapParams.get("hasta3").toString().equals("0000") && mapParams.get("hasta3").toString().equals("9999") )
        //getControlReg().setUniEjecFormateada("0100");
     
      mesActCargo = construirMes(strMesActual,"cargo");
      mesActAbono = construirMes(strMesActual,"abono");
      if(strMesActual.equals("ENE") || strMesActual.equals("ene")){
        mesAntCargoAcum = construirMes(strMesActual,"cargo_ini");
        mesAntAbonoAcum = construirMes(strMesActual,"abono_ini");
        if(formato.equals("xls")){
          cargos_acum.append(" (case cla.naturaleza when 'D' then (suma_campo(cc.nivel,cc.cuenta_mayor_id,'"+mesAntCargoAcum+"','"+mapParams.get("desde1").toString()+"','"+mapParams.get("desde2").toString()+"','"+mapParams.get("desde3").toString()+"','"+mapParams.get("desde4").toString()+"','"+mapParams.get("hasta1").toString()+"','"+mapParams.get("hasta2").toString()+"','"+mapParams.get("hasta3").toString()+"','"+hasta4+"',"+controlReg.getIdCatalogoCuenta()+","+controlReg.getEjercicio()+",cc."+mesAntCargoAcum+",substr(cc.cuenta_contable,5,1),substr(cc.cuenta_contable,6,4),substr(cc.cuenta_contable,10,4),substr(cc.cuenta_contable,14,4))- ");
          cargos_acum.append("  suma_campo(cc.nivel,cc.cuenta_mayor_id,'"+mesAntAbonoAcum+"','"+mapParams.get("desde1").toString()+"','"+mapParams.get("desde2").toString()+"','"+mapParams.get("desde3").toString()+"','"+mapParams.get("desde4").toString()+"','"+mapParams.get("hasta1").toString()+"','"+mapParams.get("hasta2").toString()+"','"+mapParams.get("hasta3").toString()+"','"+hasta4+"',"+controlReg.getIdCatalogoCuenta()+","+controlReg.getEjercicio()+",cc."+mesAntAbonoAcum+",substr(cc.cuenta_contable,5,1),substr(cc.cuenta_contable,6,4),substr(cc.cuenta_contable,10,4),substr(cc.cuenta_contable,14,4))+ ");
          cargos_acum.append("  suma_campo(cc.nivel,cc.cuenta_mayor_id,'"+mesActCargo+"','"+mapParams.get("desde1").toString()+"','"+mapParams.get("desde2").toString()+"','"+mapParams.get("desde3").toString()+"','"+mapParams.get("desde4").toString()+"','"+mapParams.get("hasta1").toString()+"','"+mapParams.get("hasta2").toString()+"','"+mapParams.get("hasta3").toString()+"','"+hasta4+"',"+controlReg.getIdCatalogoCuenta()+","+controlReg.getEjercicio()+",cc."+mesActCargo+",substr(cc.cuenta_contable,5,1),substr(cc.cuenta_contable,6,4),substr(cc.cuenta_contable,10,4),substr(cc.cuenta_contable,14,4))) ");
          cargos_acum.append("  else  ");
          cargos_acum.append("  suma_campo(cc.nivel,cc.cuenta_mayor_id,'"+mesActCargo+"','"+mapParams.get("desde1").toString()+"','"+mapParams.get("desde2").toString()+"','"+mapParams.get("desde3").toString()+"','"+mapParams.get("desde4").toString()+"','"+mapParams.get("hasta1").toString()+"','"+mapParams.get("hasta2").toString()+"','"+mapParams.get("hasta3").toString()+"','"+hasta4+"',"+controlReg.getIdCatalogoCuenta()+","+controlReg.getEjercicio()+",cc."+mesActCargo+",substr(cc.cuenta_contable,5,1),substr(cc.cuenta_contable,6,4),substr(cc.cuenta_contable,10,4),substr(cc.cuenta_contable,14,4)) end) cargos_acum, ");
        
          abonos_acum.append("  (case cla.naturaleza when 'A' then (suma_campo(cc.nivel,cc.cuenta_mayor_id,'"+mesAntAbonoAcum+"','"+mapParams.get("desde1").toString()+"','"+mapParams.get("desde2").toString()+"','"+mapParams.get("desde3").toString()+"','"+mapParams.get("desde4").toString()+"','"+mapParams.get("hasta1").toString()+"','"+mapParams.get("hasta2").toString()+"','"+mapParams.get("hasta3").toString()+"','"+hasta4+"',"+controlReg.getIdCatalogoCuenta()+","+controlReg.getEjercicio()+",cc."+mesAntAbonoAcum+",substr(cc.cuenta_contable,5,1),substr(cc.cuenta_contable,6,4),substr(cc.cuenta_contable,10,4),substr(cc.cuenta_contable,14,4))- ");
          abonos_acum.append("  suma_campo(cc.nivel,cc.cuenta_mayor_id,'"+mesAntCargoAcum+"','"+mapParams.get("desde1").toString()+"','"+mapParams.get("desde2").toString()+"','"+mapParams.get("desde3").toString()+"','"+mapParams.get("desde4").toString()+"','"+mapParams.get("hasta1").toString()+"','"+mapParams.get("hasta2").toString()+"','"+mapParams.get("hasta3").toString()+"','"+hasta4+"',"+controlReg.getIdCatalogoCuenta()+","+controlReg.getEjercicio()+",cc."+mesAntCargoAcum+",substr(cc.cuenta_contable,5,1),substr(cc.cuenta_contable,6,4),substr(cc.cuenta_contable,10,4),substr(cc.cuenta_contable,14,4))+ ");
          abonos_acum.append("  suma_campo(cc.nivel,cc.cuenta_mayor_id,'"+mesActAbono+"','"+mapParams.get("desde1").toString()+"','"+mapParams.get("desde2").toString()+"','"+mapParams.get("desde3").toString()+"','"+mapParams.get("desde4").toString()+"','"+mapParams.get("hasta1").toString()+"','"+mapParams.get("hasta2").toString()+"','"+mapParams.get("hasta3").toString()+"','"+hasta4+"',"+controlReg.getIdCatalogoCuenta()+","+controlReg.getEjercicio()+",cc."+mesActAbono+",substr(cc.cuenta_contable,5,1),substr(cc.cuenta_contable,6,4),substr(cc.cuenta_contable,10,4),substr(cc.cuenta_contable,14,4))) ");
          abonos_acum.append("  else ");
          abonos_acum.append("  suma_campo(cc.nivel,cc.cuenta_mayor_id,'"+mesActAbono+"','"+mapParams.get("desde1").toString()+"','"+mapParams.get("desde2").toString()+"','"+mapParams.get("desde3").toString()+"','"+mapParams.get("desde4").toString()+"','"+mapParams.get("hasta1").toString()+"','"+mapParams.get("hasta2").toString()+"','"+mapParams.get("hasta3").toString()+"','"+hasta4+"',"+controlReg.getIdCatalogoCuenta()+","+controlReg.getEjercicio()+",cc."+mesActAbono+",substr(cc.cuenta_contable,5,1),substr(cc.cuenta_contable,6,4),substr(cc.cuenta_contable,10,4),substr(cc.cuenta_contable,14,4)) end) abonos_acum, ");
          mapParams.put("cargosAcum", "");
          mapParams.put("abonosAcum", "");
          mapParams.put("deudor",cargos_acum);
          mapParams.put("acreedor",abonos_acum);
        }
      }
      else{
        mesAntCargoAcum = construirMes(strMesAnterior,"cargo_acum");
        mesAntAbonoAcum = construirMes(strMesAnterior,"abono_acum");
        if(formato.equals("xls")){
          cargos_acum.append(" (suma_campo(cc.nivel,cc.cuenta_mayor_id,'"+mesAntCargoAcum+"','"+mapParams.get("desde1").toString()+"','"+mapParams.get("desde2").toString()+"','"+mapParams.get("desde3").toString()+"','"+mapParams.get("desde4").toString()+"','"+mapParams.get("hasta1").toString()+"','"+mapParams.get("hasta2").toString()+"','"+mapParams.get("hasta3").toString()+"','"+hasta4+"',"+controlReg.getIdCatalogoCuenta()+","+controlReg.getEjercicio()+",cc."+mesAntCargoAcum+",substr(cc.cuenta_contable,5,1),substr(cc.cuenta_contable,6,4),substr(cc.cuenta_contable,10,4),substr(cc.cuenta_contable,14,4))+ ");
          cargos_acum.append(" suma_campo(cc.nivel,cc.cuenta_mayor_id,'"+mesActCargo+"','"+mapParams.get("desde1").toString()+"','"+mapParams.get("desde2").toString()+"','"+mapParams.get("desde3").toString()+"','"+mapParams.get("desde4").toString()+"','"+mapParams.get("hasta1").toString()+"','"+mapParams.get("hasta2").toString()+"','"+mapParams.get("hasta3").toString()+"','"+hasta4+"',"+controlReg.getIdCatalogoCuenta()+","+controlReg.getEjercicio()+",cc."+mesActCargo+",substr(cc.cuenta_contable,5,1),substr(cc.cuenta_contable,6,4),substr(cc.cuenta_contable,10,4),substr(cc.cuenta_contable,14,4))) cargos_acum, ");   
          abonos_acum.append(" (suma_campo(cc.nivel,cc.cuenta_mayor_id,'"+mesAntAbonoAcum+"','"+mapParams.get("desde1").toString()+"','"+mapParams.get("desde2").toString()+"','"+mapParams.get("desde3").toString()+"','"+mapParams.get("desde4").toString()+"','"+mapParams.get("hasta1").toString()+"','"+mapParams.get("hasta2").toString()+"','"+mapParams.get("hasta3").toString()+"','"+hasta4+"',"+controlReg.getIdCatalogoCuenta()+","+controlReg.getEjercicio()+",cc."+mesAntAbonoAcum+",substr(cc.cuenta_contable,5,1),substr(cc.cuenta_contable,6,4),substr(cc.cuenta_contable,10,4),substr(cc.cuenta_contable,14,4))+ ");
          abonos_acum.append(" suma_campo(cc.nivel,cc.cuenta_mayor_id,'"+mesActAbono+"','"+mapParams.get("desde1").toString()+"','"+mapParams.get("desde2").toString()+"','"+mapParams.get("desde3").toString()+"','"+mapParams.get("desde4").toString()+"','"+mapParams.get("hasta1").toString()+"','"+mapParams.get("hasta2").toString()+"','"+mapParams.get("hasta3").toString()+"','"+hasta4+"',"+controlReg.getIdCatalogoCuenta()+","+controlReg.getEjercicio()+",cc."+mesActAbono+",substr(cc.cuenta_contable,5,1),substr(cc.cuenta_contable,6,4),substr(cc.cuenta_contable,10,4),substr(cc.cuenta_contable,14,4))) abonos_acum, "); 
          mapParams.put("deudor", "");
          mapParams.put("acreedor", "");
          mapParams.put("cargosAcum",cargos_acum);
          mapParams.put("abonosAcum",abonos_acum);
        }
      }
      mapParams.put("mesAnt_abono_acum", mesAntAbonoAcum);
      mapParams.put("mesAnt_cargo_acum", mesAntCargoAcum);
      mapParams.put("idCatalogoCuenta", controlReg.getIdCatalogoCuenta());
      mapParams.put("ejercicio", controlReg.getEjercicio());
      mapParams.put("mesActual_cargo", mesActCargo);
      mapParams.put("mesActual_abono", mesActAbono);
      mapParams.put("mesActual_cargo_acum", mesAntCargoAcum);
      mapParams.put("mesActual_abono_acum", mesAntAbonoAcum);  
      mapParams.put("mes",fechaReporte.substring(4,6));
      if(formato.equals("xls")){
        if(saldosCero)
          mapParams.put("saldosCero","having (sum(cargos_per) <> 0 or sum(abonos_per) <> 0 or sum(cargos_acum) <> 0 or sum(abonos_acum) <> 0 " +
           "or sum(saldo_anterior) <> 0 or sum(saldo_actual) <> 0)");
        else
          mapParams.put("saldosCero","");
        if(reporte.equals("cuentasColectivas")){
          mapParams.put("campos","ltrim(cuenta,0) cuenta, ltrim(s_cuenta,0) s_cuenta, ltrim(ss_cuenta,0) ss_cuenta, ltrim(sss_cuenta,0) sss_cuenta, \n" + 
          "       ltrim(ssss_cuenta,0) ssss_cuenta, ltrim(sssss_cuenta,0) sssss_cuenta, \n" + 
          "       ltrim(ssssss_cuenta,0) ssssss_cuenta, ltrim(sssssss_cuenta,0) sssssss_cuenta, ltrim(ssssssss_cuenta,0) ssssssss_cuenta,");
          mapParams.put("grupo","cuenta, s_cuenta,ss_cuenta, sss_cuenta, ssss_cuenta, sssss_cuenta, ssssss_cuenta, sssssss_cuenta, ssssssss_cuenta, ");
          mapParams.put("ordena"," nvl(cuenta,0) asc, \n" + 
          "                   nvl(s_cuenta,0) asc, \n" + 
          "                   nvl(lpad(ss_cuenta,4,0),0) asc, \n" + 
          "                   nvl(lpad(sss_cuenta,4,0),0) asc,\n" + 
          "                   nvl(lpad(ssss_cuenta,4,0),0) asc, \n" + 
          "                   nvl(lpad(sssss_cuenta,4,0),0) asc, \n" + 
          "                   nvl(lpad(ssssss_cuenta,4,0),0) asc, \n" + 
          "                   nvl(lpad(sssssss_cuenta,4,0),0) asc, \n" + 
          "                   nvl(lpad(ssssssss_cuenta,4,0),0) asc");

        }
        sentencia.append(dml.getSelect("registroContable",reporte,mapParams));
        if(reporte.equals("cuentasColectivas")){
          sentencia2.append("select ").append("cuenta||' '||lpad(s_cuenta,1,0)||' '||lpad(ss_cuenta,4,0)||' '||lpad(sss_cuenta,4,0)||' '||lpad(ssss_cuenta,4,0)||' '||lpad(sssss_cuenta,4,0)||' '||lpad(ssssss_cuenta,4,0)||' '||lpad(sssssss_cuenta,4,0)||' '||lpad(ssssssss_cuenta,4,0) cuenta,");
          sentencia2.append("decode(nivel,1,descripcion,lpad(descripcion,length(descripcion)+nivel-1,' ')) concepto,");
          // sentencia2.append("decode(nivel,1,ltrim(to_char(saldo_actual,'9,999,999,999,999,999.99')),rpad(ltrim(to_char(saldo_actual,'9,999,999,999,999,999.99')),length(ltrim(to_char(saldo_actual,'9,999,999,999,999,999.99')))+nivel-1,' ')) saldo");      
          sentencia2.append("decode(nivel,1,' ',ltrim(to_char(saldo_actual,'9999999999999999.99'))) importe, ");
          sentencia2.append("decode(nivel,1,ltrim(to_char(saldo_actual,'9,999,999,999,999,999.99')),' ') saldo ");
          sentencia2.append(" from ( ");    
          sentencia2.append(sentencia.toString());
          sentencia2.append(" ) final");       
          sentencia.replace(0,sentencia.length(),"");
          sentencia.append(sentencia2.toString());
        }  
        //System.out.println(sentencia);
      }
      else{
        if(saldosCero)
           mapParams.put("saldosCero","having (sum(cargos_per) <> 0 or sum(abonos_per) <> 0 or sum(cargos_acum) <> 0 or sum(abonos_acum) <> 0 or sum(cargos_acumm) <> 0" +
           "or sum(abonos_acumm) <> 0 or sum(saldo_anterior) <> 0 or sum(saldo_actual) <> 0)");
        else
          mapParams.put("saldosCero","");
        mapParams.put("mesAnt_cargo_acumm", mesAntCargoAcum);
        mapParams.put("mesAnt_abono_acumm", mesAntAbonoAcum);
        if(reporte.equals("cuentasColectivas")){
          mapParams.put("campos","cuenta_contable_id, cuenta_mayor_id, cuenta_contable,");
          mapParams.put("grupo","cuenta_mayor_id, cuenta_contable_id, cuenta_contable,");
          mapParams.put("ordena","ctas.cuenta_contable");

        }
        sentencia.append(dml.getSelect("registroContable", reporte, mapParams));
        //System.out.println(sentencia);
      }

    }//end try
    catch (Exception e){
      e.printStackTrace();
      Error.mensaje(e, "CONTABILIDAD");
    }//end catch
    finally{
      mapParams = null;
      dml                       = null;
      fecha  = null;
      desdeHasta         = null;
      strMesActual           = null;
      strMesAnterior         = null;
      cargos_acum      = null;
      abonos_acum      = null;
      lista = null;
      fechaReporte = null;
      controlReg = null;
      formato = null;
      reporte = null;
      lista = null;
      
    }
    return sentencia;
  }
  
  private String construirMes(String mes, String movimiento){
    String regresa = null;
    switch (Integer.valueOf(getCierre())){
        case 2:
          regresa = mes + "_" + movimiento + "_eli";
          break;
        case 3: 
          if(movimiento.contains("acum")){
            if(getControlReg().getUniEjecFormateada().equals("0100"))
              regresa = mes + "_" + movimiento + "_eli";
            else
              regresa = mes + "_" + movimiento;
          }
          else{
            if(getControlReg().getUniEjecFormateada().equals("0100"))
              regresa = mes + "_" + movimiento + "_eli_pub";
            else
              regresa = mes + "_" + movimiento + "_pub";
          }
          break;
        default:
          regresa = mes + "_" + movimiento;
    }
    return regresa;
  }
  
  public StringBuffer sentenciaSaldos(String strFormato, String strReporte, String strMesActual, String strMesAnterior, String condicion, String patronUni, String patronEdoAmb, ControlRegistro controlReg, int cuentaContableId)
  {
    String mesActCargo = null;
    String mesActAbono = null;
    String mesAntCargoAcum = null;
    String mesAntAbonoAcum = null;
    StringBuffer sentencia = new StringBuffer();
    try{
      Map mapParams = new HashMap();
      Dml dml = null;
      String strCondicionUno = "";
      /*if(iCuentaContableId==-1){
        strCondicionUno = "%";
      }
      else{  
      }*/
      strCondicionUno = "cuenta_contable like '"+strCondicionUno+"'"+condicion;  
      dml = new Dml(Dml.CONTABILIDAD);
      mesActCargo = construirMes(strMesActual,"cargo");
      mesActAbono = construirMes(strMesActual,"abono");
      if(strMesActual.equals("ENE") || strMesActual.equals("ene")){
        mesAntCargoAcum = construirMes(strMesActual,"cargo_ini");
        mesAntAbonoAcum = construirMes(strMesActual,"abono_ini");
        if(strFormato.equals("xls")){
        mapParams.put("cargosAcum", "");
          mapParams.put("abonosAcum", "");
          mapParams.put("deudor", "case cla.naturaleza when 'D' then (".concat(mesAntCargoAcum).concat(" - ").concat(mesAntAbonoAcum).concat(" + ").concat(mesActCargo).concat(") else (").concat(mesActCargo).concat(") end cargos_acum,"));
          mapParams.put("acreedor", "case cla.naturaleza when 'A' then (".concat(mesAntAbonoAcum).concat(" - ").concat(mesAntCargoAcum).concat(" + ").concat(mesActAbono).concat(") else (").concat(mesActAbono).concat(")end abonos_acum,"));
        }
      }
      else{
        mesAntCargoAcum = construirMes(strMesAnterior,"cargo_acum");
        mesAntAbonoAcum = construirMes(strMesAnterior,"abono_acum");
        if(strFormato.equals("xls")){
          mapParams.put("deudor", "");
          mapParams.put("acreedor", "");
          mapParams.put("cargosAcum", "(".concat(mesAntCargoAcum).concat(" + ").concat(mesActCargo).concat(") cargos_acum,"));
          mapParams.put("abonosAcum", "(".concat(mesAntAbonoAcum).concat(" + ").concat(mesActAbono).concat(") abonos_acum,"));
        }
      }
      mapParams.put("mesActual_cargo", mesActCargo);
      mapParams.put("mesActual_abono", mesActAbono);
      mapParams.put("mesAnt_cargo_acum", mesAntCargoAcum);
      mapParams.put("mesAnt_abono_acum", mesAntAbonoAcum);
      mapParams.put("ejercicio", controlReg.getEjercicio());
      mapParams.put("condicion", strCondicionUno);
      mapParams.put("idCatalogo", controlReg.getIdCatalogoCuenta());
      mapParams.put("cuentaContableId", cuentaContableId);
      if(!controlReg.getUniEjecFormateada().equals("0100")){
        mapParams.put("validacion", this.getCondicion(String.valueOf(controlReg.getIndInicial()), String.valueOf(controlReg.getIndFinal()), 
          patronUni+patronEdoAmb, controlReg.getUniEjecFormateada()+patronEdoAmb, controlReg.getUEAFormateada()));          
      }// end if unidad 100
      else{
        mapParams.put("validacion", "");      
      }//end else unidad 100
      if(strFormato.equals("pdf")){
        mapParams.put("mesAnt_cargo_acumm", mesAntCargoAcum);
        mapParams.put("mesAnt_abono_acumm", mesAntAbonoAcum);
        sentencia.append(dml.getSelect("registroContable",strReporte,mapParams));
      }//end if formato reporte PDF
      else{  /// TODO: SI EL FORMATO ES EXCEL
         mapParams.put("mesAnt_cargo_acum", mesAntCargoAcum);
         mapParams.put("mesAnt_abono_acum", mesAntAbonoAcum);
         sentencia.append(dml.getSelect("registroContable", "cuentasContablesExcel", mapParams));   
      }// end else formato reporte EXCEL
     //System.out.println();
    }//end try
    catch(Exception e){
      System.out.println("[sia.rf.contabilidad.reportes.consolidacion." +
        "CatalogoCuentas.imprimir] Error: " + e);
        e.printStackTrace();
    }//end catch(Exception e)     
    return sentencia;
  }
  
  public String getCondicion(String indiceInicial, String indiceFinal, String unidad, String unidadFormateada, String UEAFormateada){
    StringBuffer sb = new StringBuffer();
    sb.append("and (substr(cuenta_contable,".concat("10").concat(", ").concat(indiceFinal).concat(")='").concat(unidad).concat("' "));
    sb.append("or substr(cuenta_contable,".concat("10").concat(", ").concat(indiceFinal).concat(")='").concat(unidadFormateada).concat("' "));
    sb.append("or substr(cuenta_contable,".concat("10").concat(", ").concat(indiceFinal).concat(")='").concat(UEAFormateada).concat("') "));
    return sb.toString();
  }
  
  public StringBuffer sentenciaEstadoCuenta(int cuentaContableId,String formato, String mesActual, String mesAnterior, String fechaInicio, String fechaFinal, String tipoQuery, String condicion) {
    Map mapParams = new HashMap();
    StringBuffer sbQuery = new StringBuffer();
    Formatos formatos = null;
    String mesAntCargoAcum=null;
    String mesAntAbonoAcum=null;
    String mesAntCargoAcumm=null;
    String mesAntAbonoAcumm=null;
    Sentencias sentencia = null;
    String condicionTipoPoliza = null;
    String mesActCargo = null;
    String mesActAbono = null;
    try{
      sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD, Sentencias.XML);
      
      //if(fechaReporte.equals("null"))
      //  fechaReporte = sia.libs.formato.Fecha.formatear(sia.libs.formato.Fecha.FECHA_CORTA,controlReg.getFechaAfectacion());            
      //String strFechaActual = fechaReporte;
      //Fecha fechaPeriodo = new Fecha(strFechaActual, "/");
      //String fecha = sia.libs.formato.Fecha.formatear(6, fechaPeriodo.getTerminoMes().toString()).toUpperCase();
      //String strFechaInicio = strFechaActual.substring(6,10).concat(strFechaActual.substring(3,5)).concat("01");
      //String strFechaFinal = strFechaActual.substring(6,10).concat(strFechaActual.substring(3,5)).concat(fecha.substring(0,2));
      //String strMesActual = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodo.getMes() - 1);
      //fechaPeriodo.addMeses(-1);
      //String strMesAnterior = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodo.getMes() - 1);
      /*if(getCierre().equals("1"))
        condicionTipoPoliza = " and pl.tipo_poliza_id in (1,2,3,4) ";
      else
        condicionTipoPoliza = " and pl.tipo_poliza_id = 5 ";*/
        //Reporte con polizas normales
        if(getCierre().equals("1"))
          condicionTipoPoliza = " and pl.tipo_poliza_id in (1,2,3,4) ";
        else
          //Reporte con polizas normales y con eliminacion
          if(getCierre().equals("2"))
            condicionTipoPoliza = " and pl.tipo_poliza_id in (1,2,3,4,5) ";
          else{
           /* if(getCierre().equals("3")){
                if(controlReg.getTipoUsuario()==1){
                    controlReg.setUnidad("100");
                    controlReg.setUniEjecFormateada("0000");
                    if(getTipoPolizas().equals("central"))
                      condicionTipoPoliza = " and pl.tipo_poliza_id in (1,2,3,4,5,7) and pl.idevento = 302 ";
                    else
                      condicionTipoPoliza = " and pl.tipo_poliza_id in (1,2,3,4,7) and pl.idevento = 301 ";
                }
                else{
                    condicionTipoPoliza = " and pl.tipo_poliza_id in (1,2,3,4,7) and pl.idevento = 301  ";
                }
                
            }*/
              
            //Reporte con polizas normales, con eliminacion y cierre anual de la central
            if(getTipoPolizas().equals("central")){
              //controlReg.setUniEjecFormateada("0100");
              condicionTipoPoliza = " and (pl.tipo_poliza_id in (1,2,3,4,5,7) or pl.idevento = 302) ";
            }
            else{
              if(controlReg.getTipoUsuario()==1){
                controlReg.setUnidad("100");
                //controlReg.setUniEjecFormateada("0000");
              }
              //Reporte con polizas normales, con eliminacion y cierre anual de las unidades
              condicionTipoPoliza = " and (pl.tipo_poliza_id in (1,2,3,4,7) or pl.idevento = 301)  ";
              
            }
          }
      
      //String strFechaInicio = strFechaActual.substring(0,4).concat(strFechaActual.substring(4,6)).concat("01");
      //String strFechaFinal = strFechaActual.substring(0,4).concat(strFechaActual.substring(4,6)).concat(fecha.substring(0,2));
      
      /*if(iCuentaContableId==-1){
        condicion = "%";
      }
      else{
        cuenta = new Cuenta(iCuentaContableId);
        condicion = cuenta.getCuentaReporte(cuenta.getNivel().intValue());
      }
      */
       if(mesActual.equals("ENE")){
        mesAntCargoAcum = construirMes(mesActual,"cargo_ini");
        mesAntAbonoAcum = construirMes(mesActual,"abono_ini");
       }
       else{
        mesAntCargoAcum = construirMes(mesAnterior,"cargo_acum");
        mesAntAbonoAcum = construirMes(mesAnterior,"abono_acum");
       }
       mesActCargo = construirMes(mesActual,"cargo");
       mesActAbono = construirMes(mesActual,"abono");
       mesAntCargoAcumm = mesAntCargoAcum;
       mesAntAbonoAcumm = mesAntAbonoAcum;

      if(formato.equals("pdf")){

      
      
        /*if(mesActual.equals("ENE")){
           mesAntCargoAcum = mesActual.concat("_cargo_ini");
           mesAntAbonoAcum = mesActual.concat("_abono_ini");
           mesAntCargoAcumm = mesActual.concat("_cargo_acum");
           mesAntAbonoAcumm = mesActual.concat("_abono_acum");
        }
        else{
          mesAntCargoAcum = mesAnterior.concat("_cargo_acum");
          mesAntAbonoAcum = mesAnterior.concat("_abono_acum");
          mesAntCargoAcumm = mesAnterior.concat("_cargo_acum");
          mesAntAbonoAcumm = mesAnterior.concat("_abono_acum");
        }*/
        

        if(tipoQuery.equals("subReporte")){
            formatos = new Formatos(Contabilidad.getInstance().getPropiedad("registroContable.query.consultarEstadoCuentaDiferencias"),
                                      fechaInicio, fechaFinal, cuentaContableId,controlReg.getIdCatalogoCuenta(),
                                      condicion,controlReg.getEjercicio(),condicionTipoPoliza);
            sbQuery.append(formatos.getSentencia());
            
            //System.out.println("QUERY SUB \\N");
            //System.out.println(sbQuery);
        }
       if(tipoQuery.equals("reporte")){
         formatos = new Formatos(Contabilidad.getInstance().getPropiedad("registroContable.query.estadoCuenta"),
                               mesActCargo,mesActAbono,String.valueOf(cuentaContableId),mesAntCargoAcum,mesAntAbonoAcum,
                               mesAntCargoAcumm,mesAntAbonoAcumm);
       sbQuery.append(formatos.getSentencia());
       //System.out.println(sbQuery);
       }
        //System.out.println(formatos.getSentencia());
        /*registro = datosReportes.obtenerTitulos(controlReg.getUnidad(),String.valueOf(controlReg.getEntidad()),String.valueOf(controlReg.getAmbito()));
        mapParams.put("FECHA_INI",strFechaInicio);
        mapParams.put("FECHA_FIN",strFechaFinal);
        mapParams.put("FECHA",fecha);
        mapParams.put("NUM_EMPLEADO",numEmpleado);
        mapParams.put("EJERCICIO", controlReg.getEjercicio());
        mapParams.put("ID_CATALOGO_CUENTA", controlReg.getIdCatalogoCuenta());
        mapParams.put("MES_ANTERIOR", strMesAnterior);
        mapParams.put("MES_ACTUAL", strMesActual);
        mapParams.put("TITULO1",registro.get(0).getField("TITULO1"));
        mapParams.put("TITULO2",registro.get(0).getField("TITULO2"));
        mapParams.put("TITULO3",registro.get(0).getField("TITULO3"));
        mapParams.put("FECHA_ACTUAL", sia.libs.formato.Fecha.formatear(sia.libs.formato.Fecha.FECHA_LARGA,controlReg.getFechaAfectacion()));
        mapParams.put("SUBREPORT_DIR",request.getSession().getServletContext().getRealPath("/contabilidad/registroContable/reportes/").toString());
        mapParams.put("IMG_DIR",request.getSession().getServletContext().getRealPath("/Librerias/Imagenes/").toString());
        mapParams.put("QUERY_SUBREPORTE",subQuery.toString());
        repClase.imprimir(sbQuery, mapParams, "CuentasContables", "/contabilidad/registroContable/reportes/estadoCuentaPoliza", 
  
        DaoFactory.CONEXION_CONTABILIDAD, "pdf", "Generacion de reporte del estado de cuenta", "Contabilidad");
          //System.out.println(sbQuery);*/
      }
      else{
        mapParams.put("idCuentaContable",cuentaContableId);
        mapParams.put("ejercicio",controlReg.getEjercicio());
        mapParams.put("condicion",condicion);
        mapParams.put("idCatalogoCuenta",controlReg.getIdCatalogoCuenta());
        mapParams.put("fechaIni",fechaInicio);
        mapParams.put("fechaFin",fechaFinal);
        mapParams.put("condicionTipoPoliza",condicionTipoPoliza);
        if(tipoQuery.equals("reporte")){
          formatos = new Formatos(Contabilidad.getInstance().getPropiedad("registroContable.query.estadoCuenta"),
                                mesActCargo,mesActAbono,String.valueOf(cuentaContableId),mesAntCargoAcum,mesAntAbonoAcum,
                                mesAntCargoAcumm,mesAntAbonoAcumm);
        sbQuery.append(formatos.getSentencia());
        //System.out.println(sbQuery);
        }else
        sbQuery.append(sentencia.getComando("registroContable.select.estadoCuenta",mapParams));
        //System.out.println(sbQuery);
        /*repClase.imprimir(sbQuery, mapParams,
          "EstadoCuenta", "/contabilidad/registroContable/reportes/", DaoFactory.CONEXION_CONTABILIDAD,
          "", "Generar archivo XLS", "Contabilidad", false); */
      }

    } 
    catch (Exception e){
      System.out.println("[sia.rf.contabilidad.registroContable.acciones." +
        "AccionFiltrarCuentasContables.reporteCuentaPoliza] Error: " + e);
        e.printStackTrace();
    }
    finally{
      mapParams = null;
      formatos = null;
      mesAntCargoAcum=null;
      mesAntAbonoAcum=null;
      mesAntCargoAcumm=null;
      mesAntAbonoAcumm=null;
    }
    return sbQuery;
  }

  public void setCierre(String cierre) {
    this.cierre = cierre;
  }

  public String getCierre() {
    return cierre;
  }

  public void setControlReg(ControlRegistro controlReg) {
    this.controlReg = controlReg;
  }

  public ControlRegistro getControlReg() {
    return controlReg;
  }

    public void setTipoPolizas(String tipoPolizas) {
        //this.tipoPolizas = tipoPolizas.equals("1")?"central":tipoPolizas.equals("2")?"unidades":null;
         this.tipoPolizas = tipoPolizas;
    }

    public String getTipoPolizas() {
        return tipoPolizas;
    }
}
