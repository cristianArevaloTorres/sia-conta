package sia.rf.contabilidad.reportes.validacion;

import sia.libs.formato.Fecha;

public class ConsultasReportesValida {
    private String idCatalogoCuenta;
    private String ejercicio;
    private String unidadEjecutora;
    private String ambito;    

      public ConsultasReportesValida() {
        this("","","","");
      }

      public ConsultasReportesValida(String idCatalogoCuenta, String ejercicio, String unidadEjecutora, String ambito) {
        setIdCatalogoCuenta(idCatalogoCuenta);
        setEjercicio(ejercicio);
        setUnidadEjecutora(unidadEjecutora);
        setAmbito(ambito);        
      }


    public void setIdCatalogoCuenta(String idCatalogoCuenta) {
        this.idCatalogoCuenta = idCatalogoCuenta;
    }

    public String getIdCatalogoCuenta() {
        return idCatalogoCuenta;
    }

    public void setEjercicio(String ejercicio) {
        this.ejercicio = ejercicio;
    }

    public String getEjercicio() {
        return ejercicio;
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
    public StringBuffer consultaISE_Entradas(String mes, String tipoRedondeo){
        StringBuffer sentencia = new StringBuffer();
        Fecha fecha = null;
        String mesAct = fecha.getNombreMesCorto(Integer.valueOf(mes)-1);
        String mesAnt = "";
        String mesCargoAcum = "";
        String mesAbonoAcum = "";
        tipoRedondeo = tipoRedondeo.equals("centavos")?"2":"0";
      if(mesAct.equals("ENE")){
        mesAnt = fecha.getNombreMesCorto(Integer.valueOf(mes)-1).concat("");
        mesCargoAcum = mesAnt.concat("_cargo_ini");
        mesAbonoAcum = mesAnt.concat("_abono_ini");
      }
      else{
        mesAnt = fecha.getNombreMesCorto(Integer.valueOf(mes)-2);
        mesCargoAcum = mesAnt.concat("_cargo_acum");
        mesAbonoAcum = mesAnt.concat("_abono_acum");
      } 
      
      if (Integer.parseInt(getEjercicio())<=2012){      
        sentencia.append("\n select 1 as orden,'' as etiqueta, 'ADQUISICIÓN DE BIENES' as entradas,round(nvl(sum(t."+mesAct+"_cargo)*-1,0),"+tipoRedondeo+") as movimientos, 0 as RegAlmacen, 0 as movimientos2  ");
        sentencia.append("\n from rf_tr_cuentas_contables t  ");
        sentencia.append("\n where substr(t.cuenta_contable,1,4) >= 5121 and substr(t.cuenta_contable,1,4) <=5129   ");
        sentencia.append("\n and t.nivel=6  ");
        sentencia.append("\n and substr(t.cuenta_contable,18,4)='0007'  ");
        if(!getUnidadEjecutora().equals("____"))
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
        if(!getAmbito().equals("____")) 
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());         
        sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta() );
        sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio() );
        sentencia.append("\n union  ");      
        sentencia.append("\n select 2 as orden,'MENOS: ' as etiqueta, 'IVA DE ADQUISICIONES' as entradas, round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+")  as movimientos, 0 as RegAlmacen,0 as movimientos2  ");
        sentencia.append("\n from rf_tr_cuentas_contables t   ");
        sentencia.append("\n where substr(t.cuenta_contable,1,4) >= 5121 and substr(t.cuenta_contable,1,4) <=5129 and t.nivel=6  ");
        sentencia.append("\n and substr(t.cuenta_contable,18,4)='0006'  ");
        if(!getUnidadEjecutora().equals("____"))
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
        if(!getAmbito().equals("____")) 
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
        sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta() );
        sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio() );
        sentencia.append("\n union  ");
        sentencia.append("\n select 3 as orden,'' as etiqueta, 'ADQUISICIONES NETAS' as entradas, (select round(nvl(sum(t."+mesAct+"_cargo)*-1,0),"+tipoRedondeo+") as movimientos  ");
        sentencia.append("\n from rf_tr_cuentas_contables t  ");
        sentencia.append("\n where substr(t.cuenta_contable,1,4) >= 5121 and substr(t.cuenta_contable,1,4) <=5129   ");
        sentencia.append("\n and t.nivel=6 and substr(t.cuenta_contable,18,4)='0007'  ");
        if(!getUnidadEjecutora().equals("____"))
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
        if(!getAmbito().equals("____")) 
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());         
        sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") ");
        sentencia.append("\n -  ");
        sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+")  as movimientos from rf_tr_cuentas_contables t   ");
        sentencia.append("\n where substr(t.cuenta_contable,1,4) >= 5121 and substr(t.cuenta_contable,1,4) <=5129 and t.nivel=6  ");
        if(!getUnidadEjecutora().equals("____"))
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
        if(!getAmbito().equals("____")) 
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());         
        sentencia.append("\n and substr(t.cuenta_contable,18,4)='0006' and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+"  and extract(year from t.fecha_vig_ini)="+getEjercicio()+")   ");
        sentencia.append("\n  as movimientos,  ");
         sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+") as movimientos  ");
        sentencia.append("\n from rf_tr_cuentas_contables t  ");
        sentencia.append("\n where substr(t.cuenta_contable,1,4)=1151 and t.nivel=5  ");
        if(!getUnidadEjecutora().equals("____"))
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
        if(!getAmbito().equals("____")) 
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());         
        sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta() );
        sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio()+") as RegAlmacen,  ");
        sentencia.append("\n (((select round(nvl(sum(t."+mesAct+"_cargo)*-1,0),"+tipoRedondeo+") as movimientos  ");
        sentencia.append("\n from rf_tr_cuentas_contables t  ");
        sentencia.append("\n where substr(t.cuenta_contable,1,4) >= 5121 and substr(t.cuenta_contable,1,4) <=5129   ");
        sentencia.append("\n and t.nivel=6 and substr(t.cuenta_contable,18,4)='0007'  ");
        if(!getUnidadEjecutora().equals("____"))
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
        if(!getAmbito().equals("____")) 
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());         
        sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") ");
        sentencia.append("\n -  ");
        sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+")  as movimientos from rf_tr_cuentas_contables t   ");
        sentencia.append("\n where substr(t.cuenta_contable,1,4) >= 5121 and substr(t.cuenta_contable,1,4) <=5129 and t.nivel=6  ");
        if(!getUnidadEjecutora().equals("____"))
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
        if(!getAmbito().equals("____")) 
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());         
        sentencia.append("\n and substr(t.cuenta_contable,18,4)='0006' and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+"  and extract(year from t.fecha_vig_ini)="+getEjercicio()+"))   ");
        sentencia.append("\n +  ");
        sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_abono),0),"+tipoRedondeo+")  as movimientos  ");
        sentencia.append("\n from rf_tr_cuentas_contables t  ");
        sentencia.append("\n where substr(t.cuenta_contable,1,5)=43992 and t.nivel=7  ");
        sentencia.append("\n and substr(t.cuenta_contable,18,8)='00050003'  ");
        if(!getUnidadEjecutora().equals("____"))
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
        if(!getAmbito().equals("____")) 
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());         
        sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta() );
        sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio()+")");
        sentencia.append("\n +  ");
        sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_abono),0),"+tipoRedondeo+") as movimientos  ");
        sentencia.append("\n from rf_tr_cuentas_contables t  ");
        sentencia.append("\n where substr(t.cuenta_contable,1,5)=43993 and t.nivel=5  ");
        if(!getUnidadEjecutora().equals("____"))
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
        if(!getAmbito().equals("____")) 
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());         
        sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta() );
        sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio()+")");
        sentencia.append("\n  ) -  ");
        sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+") as movimientos  ");
        sentencia.append("\n from rf_tr_cuentas_contables t  ");
        sentencia.append("\n where substr(t.cuenta_contable,1,4)=1151 and t.nivel=5  ");
        if(!getUnidadEjecutora().equals("____"))
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
        if(!getAmbito().equals("____")) 
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());         
        sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta() );
        sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio()+") as movimientos2  ");        
        sentencia.append("\n from dual  ");
        sentencia.append("\n union  ");
        sentencia.append("\n select 4 as orden,'' as etiqueta, 'DIFERENCIAS POR REDONDEO DE INV. INICIAL' as entradas,round(nvl(sum(t."+mesAct+"_abono),0),"+tipoRedondeo+")  as movimientos, 0 as RegAlmacen,0 as movimientos2  ");
        sentencia.append("\n from rf_tr_cuentas_contables t  ");
        sentencia.append("\n where substr(t.cuenta_contable,1,5)=43992 and t.nivel=7  ");
        sentencia.append("\n and substr(t.cuenta_contable,18,8)='00050003'  ");
        if(!getUnidadEjecutora().equals("____"))
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
        if(!getAmbito().equals("____")) 
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());         
        sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta() );
        sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio() );
        sentencia.append("\n union  ");
        sentencia.append("\n select 5 as orden,'MAS: ' as etiqueta, 'ENTRADAS POR REMESAS' as entradas,round(nvl(sum(t."+mesAct+"_abono),0),"+tipoRedondeo+") as movimientos, 0 as RegAlmacen,0  as movimientos2  ");
        sentencia.append("\n from rf_tr_cuentas_contables t  ");
        sentencia.append("\n where substr(t.cuenta_contable,1,5)=43993 and t.nivel=5  ");
        if(!getUnidadEjecutora().equals("____"))
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
        if(!getAmbito().equals("____")) 
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());         
        sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta() );
        sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio());
        sentencia.append("\n order by orden  ");
      }else if ((Integer.parseInt(getEjercicio())==2013) && (Integer.parseInt(mes)<=5)){ //cambios a partir ejercicio 2013 y hasta mayor

        sentencia.append("\n select 1 as orden,'' as etiqueta, 'ADQUISICIÓN DE BIENES' as entradas,round(nvl(sum(t."+mesAct+"_abono),0),"+tipoRedondeo+") as movimientos, 0 as RegAlmacen, 0 as movimientos2  ");
        sentencia.append("\n from rf_tr_cuentas_contables t  ");
        sentencia.append("\n where substr(t.cuenta_contable,1,5)='21122'  ");
        sentencia.append("\n and t.nivel=7  ");
        sentencia.append("\n and substr(t.cuenta_contable,18,4)='9996' and substr(t.cuenta_contable,22,4)='0001'  ");
        if(!getUnidadEjecutora().equals("____"))
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
        if(!getAmbito().equals("____")) 
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());         
        sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta() );
        sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio() );
        sentencia.append("\n union  ");
        sentencia.append("\n select 2 as orden,'' as etiqueta, 'DIFERENCIAS POR REDONDEO DE INV. INICIAL' as entradas,round(nvl(sum(t."+mesAct+"_abono),0),"+tipoRedondeo+")  as movimientos, 0 as RegAlmacen,0 as movimientos2  ");
        sentencia.append("\n from rf_tr_cuentas_contables t  ");
        sentencia.append("\n where substr(t.cuenta_contable,1,5)=43992 and t.nivel=7  ");
        sentencia.append("\n and substr(t.cuenta_contable,18,8)='00050003'  ");
        if(!getUnidadEjecutora().equals("____"))
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
        if(!getAmbito().equals("____")) 
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());         
        sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta() );
        sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio() );
        sentencia.append("\n union  ");

        sentencia.append("\n select 3 as orden,'' as etiqueta, 'ADQUISICIONES NETAS' as entradas, (select round(nvl(sum(t."+mesAct+"_abono),0),"+tipoRedondeo+") as movimientos  ");
        sentencia.append("\n from rf_tr_cuentas_contables t  ");
        sentencia.append("\n where substr(t.cuenta_contable,1,5)='21122'   ");
        sentencia.append("\n and t.nivel=7  and substr(t.cuenta_contable,18,4)='9996' and substr(t.cuenta_contable,22,4)='0001'  ");
        if(!getUnidadEjecutora().equals("____"))
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
        if(!getAmbito().equals("____")) 
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());         
        sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") ");
        sentencia.append("\n +  ");
        sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_abono),0),"+tipoRedondeo+")  as movimientos from rf_tr_cuentas_contables t   ");
        sentencia.append("\n where substr(t.cuenta_contable,1,5)=43992 and t.nivel=7 and substr(t.cuenta_contable,18,8)='00050003'  ");
        if(!getUnidadEjecutora().equals("____"))
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
        if(!getAmbito().equals("____")) 
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());         
        sentencia.append("\n  and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+"  and extract(year from t.fecha_vig_ini)="+getEjercicio()+")   ");
        sentencia.append("\n  as movimientos,  ");
         sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+") as movimientos  ");
        sentencia.append("\n from rf_tr_cuentas_contables t  ");
        sentencia.append("\n where substr(t.cuenta_contable,1,4)=1151 and t.nivel=5  ");
        if(!getUnidadEjecutora().equals("____"))
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
        if(!getAmbito().equals("____")) 
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());         
        sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta() );
        sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio()+") as RegAlmacen,  ");
        sentencia.append("\n (((select round(nvl(sum(t."+mesAct+"_abono),0),"+tipoRedondeo+") as movimientos  ");
        sentencia.append("\n from rf_tr_cuentas_contables t  ");
        sentencia.append("\n where substr(t.cuenta_contable,1,5)='21122'   ");
        sentencia.append("\n and t.nivel=7 and substr(t.cuenta_contable,18,4)='9996' and substr(t.cuenta_contable,22,4)='0001'  ");
        if(!getUnidadEjecutora().equals("____"))
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
        if(!getAmbito().equals("____")) 
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());         
        sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") ");
        sentencia.append("\n +  ");
        sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_abono),0),"+tipoRedondeo+")  as movimientos  ");
        sentencia.append("\n from rf_tr_cuentas_contables t  ");
        sentencia.append("\n where substr(t.cuenta_contable,1,5)=43992 and t.nivel=7  ");
        sentencia.append("\n and substr(t.cuenta_contable,18,8)='00050003'  ");
        if(!getUnidadEjecutora().equals("____"))
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
        if(!getAmbito().equals("____")) 
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());         
        sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta() );
        sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio()+")");
        sentencia.append("\n +  ");
        sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_abono),0),"+tipoRedondeo+") as movimientos  ");
        sentencia.append("\n from rf_tr_cuentas_contables t  ");
        sentencia.append("\n where substr(t.cuenta_contable,1,5)=43993 and t.nivel=5  ");
        if(!getUnidadEjecutora().equals("____"))
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
        if(!getAmbito().equals("____")) 
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());         
        sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta() );
        sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio()+")");
        sentencia.append("\n  ) -  ");
        sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+") as movimientos  ");
        sentencia.append("\n from rf_tr_cuentas_contables t  ");
        sentencia.append("\n where substr(t.cuenta_contable,1,4)=1151 and t.nivel=5  ");
        if(!getUnidadEjecutora().equals("____"))
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
        if(!getAmbito().equals("____")) 
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());         
        sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta() );
        sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio()+")) as movimientos2  ");        
        sentencia.append("\n from dual  ");
        sentencia.append("\n union  ");
        sentencia.append("\n select 5 as orden,'MAS: ' as etiqueta, 'ENTRADAS POR REMESAS' as entradas,round(nvl(sum(t."+mesAct+"_abono),0),"+tipoRedondeo+") as movimientos, 0 as RegAlmacen,0  as movimientos2  ");
        sentencia.append("\n from rf_tr_cuentas_contables t  ");
        sentencia.append("\n where substr(t.cuenta_contable,1,5)=43993 and t.nivel=5  ");
        if(!getUnidadEjecutora().equals("____"))
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
        if(!getAmbito().equals("____")) 
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());         
        sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta() );
        sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio());
        sentencia.append("\n order by orden  ");
    }else if (((Integer.parseInt(getEjercicio())==2013) && (Integer.parseInt(mes)>5)) || (Integer.parseInt(getEjercicio())>2013)) { //cambios a partir ejercicio 2013 despues de junio
             sentencia.append("\n select 1 as orden,'' as etiqueta, 'ADQUISICIÓN DE BIENES' as entradas,round(nvl(sum(t."+mesAct+"_abono),0),"+tipoRedondeo+") as movimientos, 0 as RegAlmacen, 0 as movimientos2  ");
             sentencia.append("\n from rf_tr_cuentas_contables t  ");
             sentencia.append("\n where substr(t.cuenta_contable,1,5)='21122'  ");
             sentencia.append("\n and t.nivel=7  ");
             sentencia.append("\n and substr(t.cuenta_contable,18,4)='9996' and substr(t.cuenta_contable,22,4)='0001'  ");
             if(!getUnidadEjecutora().equals("____"))
               sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
             if(!getAmbito().equals("____")) 
               sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());         
             sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta() );
             sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio() );
             sentencia.append("\n union  ");
             sentencia.append("\n select 2 as orden,'' as etiqueta, 'DIFERENCIAS POR REDONDEO DE INV. INICIAL' as entradas,round(nvl(sum(t."+mesAct+"_abono),0),"+tipoRedondeo+")  as movimientos, 0 as RegAlmacen,0 as movimientos2  ");
             sentencia.append("\n from rf_tr_cuentas_contables t  ");
             sentencia.append("\n where substr(t.cuenta_contable,1,5)=43992 and t.nivel=7  ");
             sentencia.append("\n and substr(t.cuenta_contable,18,8)='00050003'  ");
             if(!getUnidadEjecutora().equals("____"))
               sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
             if(!getAmbito().equals("____")) 
               sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());         
             sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta() );
             sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio() );
             sentencia.append("\n union  ");
             sentencia.append("\n select 3 as orden,'' as etiqueta, 'PENALIZACIÓN BIENES DE CONSUMO' as entradas,round(nvl(sum(t."+mesAct+"_abono),0),"+tipoRedondeo+")  as movimientos, 0 as RegAlmacen,0 as movimientos2  ");
             sentencia.append("\n from rf_tr_cuentas_contables t  ");
             sentencia.append("\n where substr(t.cuenta_contable,1,5)=43992 and t.nivel=7  ");
             sentencia.append("\n and substr(t.cuenta_contable,18,8)='00050005'  ");
             if(!getUnidadEjecutora().equals("____"))
               sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
             if(!getAmbito().equals("____")) 
               sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());         
             sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta() );
             sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio() );
             sentencia.append("\n union  ");
             sentencia.append("\n select 4 as orden,'' as etiqueta, 'CORRECCIONES EN SIGA' as entradas,round(nvl(sum(t."+mesAct+"_abono),0),"+tipoRedondeo+")  as movimientos, 0 as RegAlmacen,0 as movimientos2  ");
             sentencia.append("\n from rf_tr_cuentas_contables t  ");
             sentencia.append("\n where substr(t.cuenta_contable,1,5)=43992 and t.nivel=7  ");
             sentencia.append("\n and substr(t.cuenta_contable,18,8)='00050006'  ");
             if(!getUnidadEjecutora().equals("____"))
               sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
             if(!getAmbito().equals("____")) 
               sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());         
             sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta() );
             sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio() );
             sentencia.append("\n union  ");
             sentencia.append("\n select 5 as orden,'' as etiqueta, 'DIFERENCIAS POR REDONDEO EN SIGA' as entradas,round(nvl(sum(t."+mesAct+"_abono),0),"+tipoRedondeo+")  as movimientos, 0 as RegAlmacen,0 as movimientos2  ");
             sentencia.append("\n from rf_tr_cuentas_contables t  ");
             sentencia.append("\n where substr(t.cuenta_contable,1,5)=43992 and t.nivel=7  ");
             sentencia.append("\n and substr(t.cuenta_contable,18,8)='00050007'  ");
             if(!getUnidadEjecutora().equals("____"))
               sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
             if(!getAmbito().equals("____")) 
               sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());         
             sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta() );
             sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio() );

             sentencia.append("\n union  ");
             sentencia.append("\n select 6 as orden,'' as etiqueta, 'ANTICIPO A PROVEEDORES POR ADQUISICIÓN DE BIENES DE CONSUMO' as entradas,round(nvl(sum(t."+mesAct+"_cargo)*-1,0),"+tipoRedondeo+")  as movimientos, 0 as RegAlmacen,0 as movimientos2  ");
             sentencia.append("\n from rf_tr_cuentas_contables t  ");
             sentencia.append("\n where substr(t.cuenta_contable,1,5)=11312 and t.nivel=6  ");
             if(!getUnidadEjecutora().equals("____"))
               sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
             if(!getAmbito().equals("____")) 
               sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());         
             sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta() );
             sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio() );
             sentencia.append("\n union  ");
             sentencia.append("\n select 7 as orden,'' as etiqueta, 'ENTRADAS A  ALMACÉN DE EJERCICIOS ANTERIORES' as entradas,round(nvl(sum(t."+mesAct+"_abono),0),"+tipoRedondeo+")  as movimientos, 0 as RegAlmacen,0 as movimientos2  ");
             sentencia.append("\n from rf_tr_cuentas_contables t  ");
             sentencia.append("\n where substr(t.cuenta_contable,1,5)=43992 and t.nivel=7  ");
             sentencia.append("\n and substr(t.cuenta_contable,18,8)='00050008'  ");
             if(!getUnidadEjecutora().equals("____"))
              sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
             if(!getAmbito().equals("____")) 
              sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());         
             sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta() );
             sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio() );
             sentencia.append("\n union  ");
             sentencia.append("\n select 8 as orden,'' as etiqueta, 'POR LOS DESCUENTOS EN LAS ADQUISICIONES NO CONSIDERADAS POR EL SIGA' as entradas,round(nvl(sum(t."+mesAct+"_abono),0),"+tipoRedondeo+")  as movimientos, 0 as RegAlmacen,0 as movimientos2  ");
             sentencia.append("\n from rf_tr_cuentas_contables t  ");
             sentencia.append("\n where substr(t.cuenta_contable,1,5)=43992 and t.nivel=7  ");
             sentencia.append("\n and substr(t.cuenta_contable,18,8)='00050009'  ");
             if(!getUnidadEjecutora().equals("____"))
              sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
             if(!getAmbito().equals("____")) 
              sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());         
             sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta() );
             sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio() );                   
             sentencia.append("\n union  ");
             sentencia.append("\n select 9 as orden,'' as etiqueta, 'ADQUISICIONES NETAS' as entradas, (select round(nvl(sum(t."+mesAct+"_abono),0),"+tipoRedondeo+") as movimientos  ");
             sentencia.append("\n from rf_tr_cuentas_contables t  ");
             sentencia.append("\n where substr(t.cuenta_contable,1,5)='21122'   ");
             sentencia.append("\n and t.nivel=7  and substr(t.cuenta_contable,18,4)='9996' and substr(t.cuenta_contable,22,4)='0001'  ");
             if(!getUnidadEjecutora().equals("____"))
               sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
             if(!getAmbito().equals("____")) 
               sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());         
             sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") ");
             sentencia.append("\n +  ");
             sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_abono),0),"+tipoRedondeo+")  as movimientos from rf_tr_cuentas_contables t   ");
             sentencia.append("\n where substr(t.cuenta_contable,1,5)=43992 and t.nivel=7 and substr(t.cuenta_contable,18,4)='0005' and substr(t.cuenta_contable,22,4) in ('0003','0005','0006','0007','0008','0009')   ");
             if(!getUnidadEjecutora().equals("____"))
               sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
             if(!getAmbito().equals("____")) 
               sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());         
             sentencia.append("\n  and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+"  and extract(year from t.fecha_vig_ini)="+getEjercicio()+")   ");
             sentencia.append("\n +  ");
             sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_cargo)*-1,0),"+tipoRedondeo+")  as movimientos from rf_tr_cuentas_contables t   ");
             sentencia.append("\n where substr(t.cuenta_contable,1,5)=11312 and t.nivel=6   ");
             if(!getUnidadEjecutora().equals("____"))
              sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
             if(!getAmbito().equals("____")) 
              sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());         
             sentencia.append("\n  and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+"  and extract(year from t.fecha_vig_ini)="+getEjercicio()+")   ");                                     
             sentencia.append("\n  as movimientos,  ");
             sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+") as movimientos  ");
             sentencia.append("\n from rf_tr_cuentas_contables t  ");
             sentencia.append("\n where substr(t.cuenta_contable,1,4)=1151 and t.nivel=5  ");
             if(!getUnidadEjecutora().equals("____"))
               sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
             if(!getAmbito().equals("____")) 
               sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());         
             sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta() );
             sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio()+") as RegAlmacen,  ");
             sentencia.append("\n (((select round(nvl(sum(t."+mesAct+"_abono),0),"+tipoRedondeo+") as movimientos  ");
             sentencia.append("\n from rf_tr_cuentas_contables t  ");
             sentencia.append("\n where substr(t.cuenta_contable,1,5)='21122'   ");
             sentencia.append("\n and t.nivel=7 and substr(t.cuenta_contable,18,4)='9996' and substr(t.cuenta_contable,22,4)='0001'  ");
             if(!getUnidadEjecutora().equals("____"))
               sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
             if(!getAmbito().equals("____")) 
               sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());         
             sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") ");
             sentencia.append("\n +  ");
             sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_abono),0),"+tipoRedondeo+")  as movimientos  ");
             sentencia.append("\n from rf_tr_cuentas_contables t  ");
             sentencia.append("\n where substr(t.cuenta_contable,1,5)=43992 and t.nivel=7  ");
             sentencia.append("\n and substr(t.cuenta_contable,18,4)='0005' and substr(t.cuenta_contable,22,4) in ('0003','0005','0006','0007','0008','0009')   ");
             if(!getUnidadEjecutora().equals("____"))
               sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
             if(!getAmbito().equals("____")) 
               sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());         
             sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta() );
             sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio()+")");
             sentencia.append("\n +  ");
             sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_cargo)*-1,0),"+tipoRedondeo+")  as movimientos from rf_tr_cuentas_contables t   ");
             sentencia.append("\n where substr(t.cuenta_contable,1,5)=11312 and t.nivel=6   ");
             if(!getUnidadEjecutora().equals("____"))
              sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
             if(!getAmbito().equals("____")) 
               sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());         
             sentencia.append("\n  and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+"  and extract(year from t.fecha_vig_ini)="+getEjercicio()+")   ");                                                  
             sentencia.append("\n +  ");
             sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_abono),0),"+tipoRedondeo+") as movimientos  ");
             sentencia.append("\n from rf_tr_cuentas_contables t  ");
             sentencia.append("\n where substr(t.cuenta_contable,1,5)=43993 and t.nivel=5  ");
             if(!getUnidadEjecutora().equals("____"))
               sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
             if(!getAmbito().equals("____")) 
               sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());         
             sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta() );
             sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio()+")");
             sentencia.append("\n  ) -  ");
             sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+") as movimientos  ");
             sentencia.append("\n from rf_tr_cuentas_contables t  ");
             sentencia.append("\n where substr(t.cuenta_contable,1,4)=1151 and t.nivel=5  ");
             if(!getUnidadEjecutora().equals("____"))
               sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
             if(!getAmbito().equals("____")) 
               sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());         
             sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta() );
             sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio()+")) as movimientos2  ");        
             sentencia.append("\n from dual  ");
             sentencia.append("\n union  ");
             sentencia.append("\n select 10 as orden,'MAS: ' as etiqueta, 'ENTRADAS POR REMESAS' as entradas,round(nvl(sum(t."+mesAct+"_abono),0),"+tipoRedondeo+") as movimientos, 0 as RegAlmacen,0  as movimientos2  ");
             sentencia.append("\n from rf_tr_cuentas_contables t  ");
             sentencia.append("\n where substr(t.cuenta_contable,1,5)=43993 and t.nivel=5  ");
             if(!getUnidadEjecutora().equals("____"))
               sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
             if(!getAmbito().equals("____")) 
               sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());         
             sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta() );
             sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio());
             sentencia.append("\n order by orden  ");

    } 
      return sentencia;
      }

    public StringBuffer consultaISE_Salidas(String mes, String tipoRedondeo){
        StringBuffer sentencia = new StringBuffer();
        Fecha fecha = null;
        String mesAct = fecha.getNombreMesCorto(Integer.valueOf(mes)-1);
        String mesAnt = "";
        String mesCargoAcum = "";
        String mesAbonoAcum = "";
        tipoRedondeo = tipoRedondeo.equals("centavos")?"2":"0";
      if(mesAct.equals("ENE")){
        mesAnt = fecha.getNombreMesCorto(Integer.valueOf(mes)-1).concat("");
        mesCargoAcum = mesAnt.concat("_cargo_ini");
        mesAbonoAcum = mesAnt.concat("_abono_ini");
      }
      else{
        mesAnt = fecha.getNombreMesCorto(Integer.valueOf(mes)-2);
        mesCargoAcum = mesAnt.concat("_cargo_acum");
        mesAbonoAcum = mesAnt.concat("_abono_acum");
      } 
      
      if (((Integer.parseInt(getEjercicio())==2013) && (Integer.parseInt(mes)<=5)) || (Integer.parseInt(getEjercicio())<2013)) { //ejercicios anteriores y 2013 antes de mayo
        sentencia.append("\n select 1 as orden,'' as etiqueta, 'VALES PAPELERÍA' as salidas,round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+") as movimientos, 0 as RegAlmacen, 0 as movimientos2 ");
        sentencia.append("\n from rf_tr_cuentas_contables t ");
        sentencia.append("\n where substr(t.cuenta_contable,1,4) >= 5121 and substr(t.cuenta_contable,1,4) <=5129  ");
        sentencia.append("\n and t.nivel=6 and substr(t.cuenta_contable,18,4)='0005' ");
        if(!getUnidadEjecutora().equals("____"))
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
        if(!getAmbito().equals("____")) 
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());         
        sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta()); 
        sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio());
        sentencia.append("\n union ");
        sentencia.append("\n select 2 as orden,'MAS: ' as etiqueta, 'REMESAS CONFIRMADAS' as salidas,round(nvl((sum(case cla.naturaleza when 'D' then (t."+mesAnt+"_cargo_acum - t."+mesAnt+"_abono_acum) else (t."+mesAnt+"_abono_acum - t."+mesAnt+"_cargo_acum) end))*-1,0),"+tipoRedondeo+") as movimientos, 0 as RegAlmacen, 0 as movimientos2 ");
        sentencia.append("\n from rf_tr_cuentas_contables t INNER JOIN rf_tc_clasificador_cuentas cla  ");
        sentencia.append("\n      on t.cuenta_mayor_id = cla.cuenta_mayor_id  ");
        sentencia.append("\n where substr(t.cuenta_contable,1,5) = 11235 and t.nivel=5 ");
        if(!getUnidadEjecutora().equals("____"))
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
        if(!getAmbito().equals("____")) 
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
        sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta());
        sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio());
        sentencia.append("\n union ");
        sentencia.append("\n select 3 as orden,'' as etiqueta, 'ÓRDENES DE TRASPASO PENDIENTES DE CONFIRMAR ' as salidas,round(nvl(sum( case cla.naturaleza when 'D' then ((t."+mesAnt+"_cargo_acum + t."+mesAct+"_cargo) - (t."+mesAnt+"_abono_acum + t."+mesAct+"_abono)) else ((t."+mesAnt+"_abono_acum +  t."+mesAct+"_abono) - (t."+mesAnt+"_cargo_acum + t."+mesAct+"_cargo)) end),0),"+tipoRedondeo+") as movimientos, 0 as RegAlmacen, 0 as movimientos2 ");
        sentencia.append("\n from rf_tr_cuentas_contables t INNER JOIN rf_tc_clasificador_cuentas cla  ");
        sentencia.append("\n      on t.cuenta_mayor_id = cla.cuenta_mayor_id  ");
        sentencia.append("\n where substr(t.cuenta_contable,1,5) = 11235 and t.nivel=5 ");
        if(!getUnidadEjecutora().equals("____"))
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
        if(!getAmbito().equals("____")) 
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
        sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta());
        sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio());
        sentencia.append("\n union ");
        sentencia.append("\n select 4 as orden,'' as etiqueta, 'DEVOLUCIÓN DE EJERCICIOS ANTERIORES' as salidas,round(nvl(sum(t."+mesAct+"_abono),0),"+tipoRedondeo+")  as movimientos, 0 as RegAlmacen,0 as movimientos2 ");
        sentencia.append("\n from rf_tr_cuentas_contables t ");
        sentencia.append("\n where substr(t.cuenta_contable,1,5)=43992  ");
        sentencia.append("\n and t.nivel=7 and substr(t.cuenta_contable,18,8)='00050001' ");
        if(!getUnidadEjecutora().equals("____"))
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
        if(!getAmbito().equals("____")) 
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
        sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta());
        sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio());
        sentencia.append("\n union ");
        sentencia.append("\n select 5 as orden,'' as etiqueta, 'ENAJENACIÓN BIENES DE CONSUMO' as salidas,round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+") as movimientos, 0 as RegAlmacen, 0 as movimientos2 ");
        sentencia.append("\n from rf_tr_cuentas_contables t ");
        sentencia.append("\n where substr(t.cuenta_contable,1,5)= 55351 ");
        sentencia.append("\n and t.nivel=7 and (substr(t.cuenta_contable,18,4) >= '2100' and substr(t.cuenta_contable,18,4) <= '2900') and substr(t.cuenta_contable,22,4)= '0001' ");
        if(!getUnidadEjecutora().equals("____"))
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
        if(!getAmbito().equals("____")) 
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
        sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta());
        sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio());
        sentencia.append("\n union ");
        sentencia.append("\n select 6 as orden,'' as etiqueta, 'DIFERENCIA POR REDONDEO DE INV. INICIAL' as salidas,round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+") as movimientos, 0 as RegAlmacen, 0 as movimientos2 ");
        sentencia.append("\n from rf_tr_cuentas_contables t ");
        sentencia.append("\n where substr(t.cuenta_contable,1,5)= 55351 ");
        sentencia.append("\n and t.nivel=7 and (substr(t.cuenta_contable,18,4) >= 2100 and substr(t.cuenta_contable,18,4) <= 2900) and substr(t.cuenta_contable,22,4)= '0002' ");
        if(!getUnidadEjecutora().equals("____"))
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
        if(!getAmbito().equals("____")) 
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
        sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta());
        sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio());
        sentencia.append("\n union ");
        sentencia.append("\n select 7 as orden,'' as etiqueta, 'VENTA DE DESECHO DE BIENES DE CONSUMO' as salidas, round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+") as movimientos, 0 as RegAlmacen, 0 as movimientos2 ");
        sentencia.append("\n from rf_tr_cuentas_contables t ");
        sentencia.append("\n where substr(t.cuenta_contable,1,5)= 55351 ");
        sentencia.append("\n and t.nivel=7 and (substr(t.cuenta_contable,18,4) >= 2100 and substr(t.cuenta_contable,18,4) <= 2900) and substr(t.cuenta_contable,22,4)= '0003' ");
        if(!getUnidadEjecutora().equals("____"))
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
        if(!getAmbito().equals("____")) 
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
        sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta());
        sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio());
        sentencia.append("\n union ");
        sentencia.append("\n select 8 as orden,'' as etiqueta, 'SUBTOTAL' as salidas, ");
        sentencia.append("\n ( ");
        sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+") as mov from rf_tr_cuentas_contables t ");
        sentencia.append("\n where substr(t.cuenta_contable,1,4) >= 5121 and substr(t.cuenta_contable,1,4) <=5129 and t.nivel=6 and substr(t.cuenta_contable,18,4)='0005' ");
        if(!getUnidadEjecutora().equals("____"))
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
        if(!getAmbito().equals("____")) 
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
        sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") + ");
        sentencia.append("\n (select round(nvl((sum(case cla.naturaleza when 'D' then (t."+mesAnt+"_cargo_acum - t."+mesAnt+"_abono_acum) else (t."+mesAnt+"_abono_acum - t."+mesAnt+"_cargo_acum) end)*-1),0),"+tipoRedondeo+") as mov from rf_tr_cuentas_contables t INNER JOIN rf_tc_clasificador_cuentas cla  ");
        sentencia.append("\n  on t.cuenta_mayor_id = cla.cuenta_mayor_id where substr(t.cuenta_contable,1,5) = 11235 and t.nivel=5 ");
        if(!getUnidadEjecutora().equals("____"))
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
        if(!getAmbito().equals("____")) 
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
        sentencia.append("\n  and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") + ");        
        sentencia.append("\n (select round(nvl(sum( case cla.naturaleza when 'D' then ((t."+mesAnt+"_cargo_acum + t."+mesAct+"_cargo) - (t."+mesAnt+"_abono_acum + t."+mesAct+"_abono)) else ((t."+mesAnt+"_abono_acum +  t."+mesAct+"_abono) - (t."+mesAnt+"_cargo_acum + t."+mesAct+"_cargo)) end),0),"+tipoRedondeo+")  ");
        sentencia.append("\n from rf_tr_cuentas_contables t INNER JOIN rf_tc_clasificador_cuentas cla  on t.cuenta_mayor_id = cla.cuenta_mayor_id  ");
        sentencia.append("\n where substr(t.cuenta_contable,1,5) = 11235 and t.nivel=5");
        if(!getUnidadEjecutora().equals("____"))
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
        if(!getAmbito().equals("____")) 
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
        sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") + ");
        sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_abono),0),"+tipoRedondeo+") as mov from rf_tr_cuentas_contables t ");
        sentencia.append("\n where substr(t.cuenta_contable,1,5)=43992 and t.nivel=7 and substr(t.cuenta_contable,18,8)='00050001' ");
        if(!getUnidadEjecutora().equals("____"))
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
        if(!getAmbito().equals("____")) 
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
        sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") + ");
        sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+") as mov from rf_tr_cuentas_contables t ");
        sentencia.append("\n where substr(t.cuenta_contable,1,5)= 55351 and t.nivel=7 and (substr(t.cuenta_contable,18,4) >= 2100 and substr(t.cuenta_contable,18,4) <= 2900) and substr(t.cuenta_contable,22,4)= '0001' ");
        if(!getUnidadEjecutora().equals("____"))
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
        if(!getAmbito().equals("____")) 
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
        sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") + ");
        sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+") as mov from rf_tr_cuentas_contables t ");
        sentencia.append("\n where substr(t.cuenta_contable,1,5)= 55351 and t.nivel=7 and (substr(t.cuenta_contable,18,4) >= 2100 and substr(t.cuenta_contable,18,4) <= 2900) and substr(t.cuenta_contable,22,4)= '0002' ");
        if(!getUnidadEjecutora().equals("____"))
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
        if(!getAmbito().equals("____")) 
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
        sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") + ");
        sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+") as mov from rf_tr_cuentas_contables t ");
        sentencia.append("\n where substr(t.cuenta_contable,1,5)= 55351 and t.nivel=7 and (substr(t.cuenta_contable,18,4) >= 2100 and substr(t.cuenta_contable,18,4) <= 2900) and substr(t.cuenta_contable,22,4)= '0003' ");
        if(!getUnidadEjecutora().equals("____"))
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
        if(!getAmbito().equals("____")) 
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
        sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") ");
        sentencia.append("\n )  as movimientos,  ");
        sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_abono),0),"+tipoRedondeo+") as movimientos ");
        sentencia.append("\n from rf_tr_cuentas_contables t ");
        sentencia.append("\n where substr(t.cuenta_contable,1,4)=1151 and t.nivel=5 ");
        if(!getUnidadEjecutora().equals("____"))
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
        if(!getAmbito().equals("____")) 
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
        sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta());
        sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio()+") as RegAlmacen, ");
        sentencia.append("\n ( "); 
        sentencia.append("\n select ");
        sentencia.append("\n ((select round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+") as mov from rf_tr_cuentas_contables t ");
        sentencia.append("\n where substr(t.cuenta_contable,1,4) >= 5121 and substr(t.cuenta_contable,1,4) <=5129 and t.nivel=6 and substr(t.cuenta_contable,18,4)='0005' ");
        if(!getUnidadEjecutora().equals("____"))
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
        if(!getAmbito().equals("____")) 
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
        sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") + ");
        sentencia.append("\n (select round(nvl((sum(case cla.naturaleza when 'D' then (t."+mesAnt+"_cargo_acum - t."+mesAnt+"_abono_acum) else (t."+mesAnt+"_abono_acum - t."+mesAnt+"_cargo_acum) end)*-1),0),"+tipoRedondeo+") as mov from rf_tr_cuentas_contables t INNER JOIN rf_tc_clasificador_cuentas cla  ");
        sentencia.append("\n  on t.cuenta_mayor_id = cla.cuenta_mayor_id where substr(t.cuenta_contable,1,5) = 11235 and t.nivel=5 ");
        if(!getUnidadEjecutora().equals("____"))
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
        if(!getAmbito().equals("____")) 
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
        sentencia.append("\n  and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") + ");        
        sentencia.append("\n (select round(nvl(sum( case cla.naturaleza when 'D' then ((t."+mesAnt+"_cargo_acum + t."+mesAct+"_cargo) - (t."+mesAnt+"_abono_acum + t."+mesAct+"_abono)) else ((t."+mesAnt+"_abono_acum +  t."+mesAct+"_abono) - (t."+mesAnt+"_cargo_acum + t."+mesAct+"_cargo)) end),0),"+tipoRedondeo+")  ");
        sentencia.append("\n from rf_tr_cuentas_contables t INNER JOIN rf_tc_clasificador_cuentas cla  on t.cuenta_mayor_id = cla.cuenta_mayor_id  ");
        sentencia.append("\n where substr(t.cuenta_contable,1,5) = 11235 and t.nivel=5");
        if(!getUnidadEjecutora().equals("____"))
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
        if(!getAmbito().equals("____")) 
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
        sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") + ");
        sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_abono),0),"+tipoRedondeo+") as mov from rf_tr_cuentas_contables t ");
        sentencia.append("\n where substr(t.cuenta_contable,1,5)=43992 and t.nivel=7 and substr(t.cuenta_contable,18,8)='00050001' ");
        if(!getUnidadEjecutora().equals("____"))
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
        if(!getAmbito().equals("____")) 
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
        sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") + ");
        sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+") as mov from rf_tr_cuentas_contables t ");
        sentencia.append("\n where substr(t.cuenta_contable,1,5)= 55351 and t.nivel=7 and (substr(t.cuenta_contable,18,4) >= 2100 and substr(t.cuenta_contable,18,4) <= 2900) and substr(t.cuenta_contable,22,4)= '0001' ");
        if(!getUnidadEjecutora().equals("____"))
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
        if(!getAmbito().equals("____")) 
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
        sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") + ");
        sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+") as mov from rf_tr_cuentas_contables t ");
        sentencia.append("\n where substr(t.cuenta_contable,1,5)= 55351 and t.nivel=7 and (substr(t.cuenta_contable,18,4) >= 2100 and substr(t.cuenta_contable,18,4) <= 2900) and substr(t.cuenta_contable,22,4)= '0002' ");
        if(!getUnidadEjecutora().equals("____"))
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
        if(!getAmbito().equals("____")) 
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
        sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") + ");
        sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+") as mov from rf_tr_cuentas_contables t ");
        sentencia.append("\n where substr(t.cuenta_contable,1,5)= 55351 and t.nivel=7 and (substr(t.cuenta_contable,18,4) >= 2100 and substr(t.cuenta_contable,18,4) <= 2900) and substr(t.cuenta_contable,22,4)= '0003' ");
        if(!getUnidadEjecutora().equals("____"))
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
        if(!getAmbito().equals("____")) 
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
        sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") ");
        sentencia.append("\n + (select round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+") as movimientos ");
        sentencia.append("\n from rf_tr_cuentas_contables t  ");
        sentencia.append("\n where substr(t.cuenta_contable,1,5) = 11235 and t.nivel=5 ");
        if(!getUnidadEjecutora().equals("____"))
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
        if(!getAmbito().equals("____")) 
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
        sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta());
        sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio()+"))");        
        sentencia.append("\n - (select round(nvl(sum(t."+mesAct+"_abono),0),"+tipoRedondeo+") as movimientos ");
        sentencia.append("\n from rf_tr_cuentas_contables t ");
        sentencia.append("\n where substr(t.cuenta_contable,1,4)=1151 and t.nivel=5 ");
        if(!getUnidadEjecutora().equals("____"))
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
        if(!getAmbito().equals("____")) 
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
        sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta());
        sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio()+")"); 
        sentencia.append("\n from dual ) as movimientos2  ");
        sentencia.append("\n from dual ");
        sentencia.append("\n union all ");
        sentencia.append("\n select 9 as orden,'' as etiqueta, 'SALIDA POR REMESAS' as salidas,round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+") as movimientos, 0 as RegAlmacen, 0 as movimientos2 ");
        sentencia.append("\n from rf_tr_cuentas_contables t  ");
        sentencia.append("\n where substr(t.cuenta_contable,1,5)=11235 and t.nivel=5 ");
        if(!getUnidadEjecutora().equals("____"))
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
        if(!getAmbito().equals("____")) 
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
        sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta());
        sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio());
        sentencia.append("\n Order by orden "); 
        }else if (((Integer.parseInt(getEjercicio())==2013) && (Integer.parseInt(mes)>5)) || (Integer.parseInt(getEjercicio())>2013)) { //cambios a partir ejercicio 2013 despues de junio

         sentencia.append("\n select 1 as orden,'' as etiqueta, 'VALES PAPELERÍA' as salidas,round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+") as movimientos, 0 as RegAlmacen, 0 as movimientos2 ");
         sentencia.append("\n from rf_tr_cuentas_contables t ");
         sentencia.append("\n where substr(t.cuenta_contable,1,4) >= 5121 and substr(t.cuenta_contable,1,4) <=5129  ");
         sentencia.append("\n and t.nivel=6 and substr(t.cuenta_contable,18,4)='0005' ");
         if(!getUnidadEjecutora().equals("____"))
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
         if(!getAmbito().equals("____")) 
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());         
         sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta()); 
         sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio());
         sentencia.append("\n union ");
         sentencia.append("\n select 2 as orden,'MAS: ' as etiqueta, 'REMESAS CONFIRMADAS' as salidas,round(nvl((sum(case cla.naturaleza when 'D' then (t."+mesAnt+"_cargo_acum - t."+mesAnt+"_abono_acum) else (t."+mesAnt+"_abono_acum - t."+mesAnt+"_cargo_acum) end))*-1,0),"+tipoRedondeo+") as movimientos, 0 as RegAlmacen, 0 as movimientos2 ");
         sentencia.append("\n from rf_tr_cuentas_contables t INNER JOIN rf_tc_clasificador_cuentas cla  ");
         sentencia.append("\n      on t.cuenta_mayor_id = cla.cuenta_mayor_id  ");
         sentencia.append("\n where substr(t.cuenta_contable,1,5) = 11235 and t.nivel=5 ");
         if(!getUnidadEjecutora().equals("____"))
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
         if(!getAmbito().equals("____")) 
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
         sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta());
         sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio());
         sentencia.append("\n union ");
         sentencia.append("\n select 3 as orden,'' as etiqueta, 'ÓRDENES DE TRASPASO PENDIENTES DE CONFIRMAR ' as salidas,round(nvl(sum( case cla.naturaleza when 'D' then ((t."+mesAnt+"_cargo_acum + t."+mesAct+"_cargo) - (t."+mesAnt+"_abono_acum + t."+mesAct+"_abono)) else ((t."+mesAnt+"_abono_acum +  t."+mesAct+"_abono) - (t."+mesAnt+"_cargo_acum + t."+mesAct+"_cargo)) end),0),"+tipoRedondeo+") as movimientos, 0 as RegAlmacen, 0 as movimientos2 ");
         sentencia.append("\n from rf_tr_cuentas_contables t INNER JOIN rf_tc_clasificador_cuentas cla  ");
         sentencia.append("\n      on t.cuenta_mayor_id = cla.cuenta_mayor_id  ");
         sentencia.append("\n where substr(t.cuenta_contable,1,5) = 11235 and t.nivel=5 ");
         if(!getUnidadEjecutora().equals("____"))
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
         if(!getAmbito().equals("____")) 
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
         sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta());
         sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio());
         sentencia.append("\n union ");
         sentencia.append("\n select 4 as orden,'' as etiqueta, 'DEVOLUCIÓN DE EJERCICIOS ANTERIORES' as salidas,round(nvl(sum(t."+mesAct+"_abono)*-1,0),"+tipoRedondeo+")  as movimientos, 0 as RegAlmacen,0 as movimientos2 ");
         sentencia.append("\n from rf_tr_cuentas_contables t ");
         sentencia.append("\n where substr(t.cuenta_contable,1,5)=43992  ");
         sentencia.append("\n and t.nivel=7 and substr(t.cuenta_contable,18,8)='00050001' ");
         if(!getUnidadEjecutora().equals("____"))
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
         if(!getAmbito().equals("____")) 
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
         sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta());
         sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio());
         sentencia.append("\n union ");
         sentencia.append("\n select 5 as orden,'' as etiqueta, 'ENAJENACIÓN BIENES DE CONSUMO' as salidas,round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+") as movimientos, 0 as RegAlmacen, 0 as movimientos2 ");
         sentencia.append("\n from rf_tr_cuentas_contables t ");
         sentencia.append("\n where substr(t.cuenta_contable,1,5)= 55351 ");
         sentencia.append("\n and t.nivel=7 and (substr(t.cuenta_contable,18,4) >= 2100 and substr(t.cuenta_contable,18,4) <= 2900) and substr(t.cuenta_contable,22,4)= '0001' ");
         if(!getUnidadEjecutora().equals("____"))
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
         if(!getAmbito().equals("____")) 
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
         sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta());
         sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio());
         sentencia.append("\n union ");
         sentencia.append("\n select 6 as orden,'' as etiqueta, 'DIFERENCIA POR REDONDEO DE INV. INICIAL' as salidas,round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+") as movimientos, 0 as RegAlmacen, 0 as movimientos2 ");
         sentencia.append("\n from rf_tr_cuentas_contables t ");
         sentencia.append("\n where substr(t.cuenta_contable,1,5)= 55351 ");
         sentencia.append("\n and t.nivel=7 and (substr(t.cuenta_contable,18,4) >= 2100 and substr(t.cuenta_contable,18,4) <= 2900) and substr(t.cuenta_contable,22,4)= '0002' ");
         if(!getUnidadEjecutora().equals("____"))
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
         if(!getAmbito().equals("____")) 
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
         sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta());
         sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio());
         sentencia.append("\n union ");
         sentencia.append("\n select 7 as orden,'' as etiqueta, 'VENTA DE DESECHO DE BIENES DE CONSUMO' as salidas, round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+") as movimientos, 0 as RegAlmacen, 0 as movimientos2 ");
         sentencia.append("\n from rf_tr_cuentas_contables t ");
         sentencia.append("\n where substr(t.cuenta_contable,1,5)= 55351 ");
         sentencia.append("\n and t.nivel=7 and (substr(t.cuenta_contable,18,4) >= 2100 and substr(t.cuenta_contable,18,4) <= 2900) and substr(t.cuenta_contable,22,4)= '0003' ");
         if(!getUnidadEjecutora().equals("____"))
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
         if(!getAmbito().equals("____")) 
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
         sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta());
         sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio());
         sentencia.append("\n union ");
         sentencia.append("\n select 8 as orden,'' as etiqueta, 'CORRECCIONES EN SIGA' as salidas, round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+") as movimientos, 0 as RegAlmacen, 0 as movimientos2 ");
         sentencia.append("\n from rf_tr_cuentas_contables t ");
         sentencia.append("\n where substr(t.cuenta_contable,1,5)=  55994 ");
         sentencia.append("\n and t.nivel=6  and substr(t.cuenta_contable,18,4)= '0001' ");
         if(!getUnidadEjecutora().equals("____"))
         sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
         if(!getAmbito().equals("____"))
         sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
         sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta());
         sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio());
         sentencia.append("\n union ");
         sentencia.append("\n select 9 as orden,'' as etiqueta, 'DIFERENCIAS POR REDONDEO' as salidas, round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+") as movimientos, 0 as RegAlmacen, 0 as movimientos2 ");
         sentencia.append("\n from rf_tr_cuentas_contables t ");
         sentencia.append("\n where substr(t.cuenta_contable,1,5)=  55994 ");
         sentencia.append("\n and t.nivel=6  and substr(t.cuenta_contable,18,4)= '0002' ");
         if(!getUnidadEjecutora().equals("____"))
         sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
         if(!getAmbito().equals("____"))
         sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
         sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta());
         sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio());
         
         sentencia.append("\n union ");
         sentencia.append("\n select 10 as orden,'' as etiqueta, 'SUBTOTAL' as salidas, ");
         sentencia.append("\n ( ");
         sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+") as mov from rf_tr_cuentas_contables t ");
         sentencia.append("\n where substr(t.cuenta_contable,1,4) >= 5121 and substr(t.cuenta_contable,1,4) <=5129 and t.nivel=6 and substr(t.cuenta_contable,18,4)='0005' ");
         if(!getUnidadEjecutora().equals("____"))
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
         if(!getAmbito().equals("____")) 
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
         sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") + ");
         sentencia.append("\n (select round(nvl((sum(case cla.naturaleza when 'D' then (t."+mesAnt+"_cargo_acum - t."+mesAnt+"_abono_acum) else (t."+mesAnt+"_abono_acum - t."+mesAnt+"_cargo_acum) end)*-1),0),"+tipoRedondeo+") as mov from rf_tr_cuentas_contables t INNER JOIN rf_tc_clasificador_cuentas cla  ");
         sentencia.append("\n  on t.cuenta_mayor_id = cla.cuenta_mayor_id where substr(t.cuenta_contable,1,5) = 11235 and t.nivel=5 ");
         if(!getUnidadEjecutora().equals("____"))
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
         if(!getAmbito().equals("____")) 
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
         sentencia.append("\n  and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") + ");        
         sentencia.append("\n (select round(nvl(sum( case cla.naturaleza when 'D' then ((t."+mesAnt+"_cargo_acum + t."+mesAct+"_cargo) - (t."+mesAnt+"_abono_acum + t."+mesAct+"_abono)) else ((t."+mesAnt+"_abono_acum +  t."+mesAct+"_abono) - (t."+mesAnt+"_cargo_acum + t."+mesAct+"_cargo)) end),0),"+tipoRedondeo+")  ");
         sentencia.append("\n from rf_tr_cuentas_contables t INNER JOIN rf_tc_clasificador_cuentas cla  on t.cuenta_mayor_id = cla.cuenta_mayor_id  ");
         sentencia.append("\n where substr(t.cuenta_contable,1,5) = 11235 and t.nivel=5");
         if(!getUnidadEjecutora().equals("____"))
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
         if(!getAmbito().equals("____")) 
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
         sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") + ");
         sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_abono),0),"+tipoRedondeo+") as mov from rf_tr_cuentas_contables t ");
         sentencia.append("\n where substr(t.cuenta_contable,1,5)=43992 and t.nivel=7 and substr(t.cuenta_contable,18,8)='00050001' ");
         if(!getUnidadEjecutora().equals("____"))
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
         if(!getAmbito().equals("____")) 
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
         sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") + ");
         sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+") as mov from rf_tr_cuentas_contables t ");
         sentencia.append("\n where substr(t.cuenta_contable,1,5)= 55351 and t.nivel=7 and (substr(t.cuenta_contable,18,4) >= 2100 and substr(t.cuenta_contable,18,4) <= 2900) and substr(t.cuenta_contable,22,4)= '0001' ");
         if(!getUnidadEjecutora().equals("____"))
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
         if(!getAmbito().equals("____")) 
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
         sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") + ");
         sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+") as mov from rf_tr_cuentas_contables t ");
         sentencia.append("\n where substr(t.cuenta_contable,1,5)= 55351 and t.nivel=7 and (substr(t.cuenta_contable,18,4) >= 2100 and substr(t.cuenta_contable,18,4) <= 2900) and substr(t.cuenta_contable,22,4)= '0002' ");
         if(!getUnidadEjecutora().equals("____"))
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
         if(!getAmbito().equals("____")) 
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
         sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") + ");
         sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+") as mov from rf_tr_cuentas_contables t ");
         sentencia.append("\n where substr(t.cuenta_contable,1,5)= 55351 and t.nivel=7 and (substr(t.cuenta_contable,18,4) >= 2100 and substr(t.cuenta_contable,18,4) <= 2900) and substr(t.cuenta_contable,22,4)= '0003' ");
         if(!getUnidadEjecutora().equals("____"))
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
         if(!getAmbito().equals("____")) 
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
         sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") + ");
         
         sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+") as mov from rf_tr_cuentas_contables t ");
         sentencia.append("\n where substr(t.cuenta_contable,1,5)= 55994 and t.nivel=6  and substr(t.cuenta_contable,18,4) IN ('0001','0002') ");
         if(!getUnidadEjecutora().equals("____"))
         sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
         if(!getAmbito().equals("____"))
         sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
         sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") ");
         
         
         sentencia.append("\n )  as movimientos,  ");
         sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_abono),0),"+tipoRedondeo+") as movimientos ");
         sentencia.append("\n from rf_tr_cuentas_contables t ");
         sentencia.append("\n where substr(t.cuenta_contable,1,4)=1151 and t.nivel=5 ");
         if(!getUnidadEjecutora().equals("____"))
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
         if(!getAmbito().equals("____")) 
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
         sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta());
         sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio()+") as RegAlmacen, ");
         sentencia.append("\n ( "); 
         sentencia.append("\n select ");
         sentencia.append("\n ((select round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+") as mov from rf_tr_cuentas_contables t ");
         sentencia.append("\n where substr(t.cuenta_contable,1,4) >= 5121 and substr(t.cuenta_contable,1,4) <=5129 and t.nivel=6 and substr(t.cuenta_contable,18,4)='0005' ");
         if(!getUnidadEjecutora().equals("____"))
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
         if(!getAmbito().equals("____")) 
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
         sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") + ");
         sentencia.append("\n (select round(nvl((sum(case cla.naturaleza when 'D' then (t."+mesAnt+"_cargo_acum - t."+mesAnt+"_abono_acum) else (t."+mesAnt+"_abono_acum - t."+mesAnt+"_cargo_acum) end)*-1),0),"+tipoRedondeo+") as mov from rf_tr_cuentas_contables t INNER JOIN rf_tc_clasificador_cuentas cla  ");
         sentencia.append("\n  on t.cuenta_mayor_id = cla.cuenta_mayor_id where substr(t.cuenta_contable,1,5) = 11235 and t.nivel=5 ");
         if(!getUnidadEjecutora().equals("____"))
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
         if(!getAmbito().equals("____")) 
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
         sentencia.append("\n  and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") + ");        
         sentencia.append("\n (select round(nvl(sum( case cla.naturaleza when 'D' then ((t."+mesAnt+"_cargo_acum + t."+mesAct+"_cargo) - (t."+mesAnt+"_abono_acum + t."+mesAct+"_abono)) else ((t."+mesAnt+"_abono_acum +  t."+mesAct+"_abono) - (t."+mesAnt+"_cargo_acum + t."+mesAct+"_cargo)) end),0),"+tipoRedondeo+")  ");
         sentencia.append("\n from rf_tr_cuentas_contables t INNER JOIN rf_tc_clasificador_cuentas cla  on t.cuenta_mayor_id = cla.cuenta_mayor_id  ");
         sentencia.append("\n where substr(t.cuenta_contable,1,5) = 11235 and t.nivel=5");
         if(!getUnidadEjecutora().equals("____"))
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
         if(!getAmbito().equals("____")) 
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
         sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") + ");
         sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_abono)*-1,0),"+tipoRedondeo+") as mov from rf_tr_cuentas_contables t ");
         sentencia.append("\n where substr(t.cuenta_contable,1,5)=43992 and t.nivel=7 and substr(t.cuenta_contable,18,8)='00050001' ");
         if(!getUnidadEjecutora().equals("____"))
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
         if(!getAmbito().equals("____")) 
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
         sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") + ");
         sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+") as mov from rf_tr_cuentas_contables t ");
         sentencia.append("\n where substr(t.cuenta_contable,1,5)= 55351 and t.nivel=7 and (substr(t.cuenta_contable,18,4) >= 2100 and substr(t.cuenta_contable,18,4) <= 2900) and substr(t.cuenta_contable,22,4)= '0001' ");
         if(!getUnidadEjecutora().equals("____"))
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
         if(!getAmbito().equals("____")) 
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
         sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") + ");
         sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+") as mov from rf_tr_cuentas_contables t ");
         sentencia.append("\n where substr(t.cuenta_contable,1,5)= 55351 and t.nivel=7 and (substr(t.cuenta_contable,18,4) >= 2100 and substr(t.cuenta_contable,18,4) <= 2900) and substr(t.cuenta_contable,22,4)= '0002' ");
         if(!getUnidadEjecutora().equals("____"))
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
         if(!getAmbito().equals("____")) 
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
         sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") + ");
         sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+") as mov from rf_tr_cuentas_contables t ");
         sentencia.append("\n where substr(t.cuenta_contable,1,5)= 55351 and t.nivel=7 and (substr(t.cuenta_contable,18,4) >= 2100 and substr(t.cuenta_contable,18,4) <= 2900) and substr(t.cuenta_contable,22,4)= '0003' ");
         if(!getUnidadEjecutora().equals("____"))
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
         if(!getAmbito().equals("____")) 
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
         sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") + ");
         
         sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+") as mov from rf_tr_cuentas_contables t ");
         sentencia.append("\n where substr(t.cuenta_contable,1,5)= 55994 and t.nivel=6  and substr(t.cuenta_contable,18,4) IN ('0001','0002') ");
         if(!getUnidadEjecutora().equals("____"))
         sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
         if(!getAmbito().equals("____"))
         sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
         sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") ");
         
         sentencia.append("\n + (select round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+") as movimientos ");
         sentencia.append("\n from rf_tr_cuentas_contables t  ");
         sentencia.append("\n where substr(t.cuenta_contable,1,5)  in ('11235','11455') and t.nivel=5 ");
         if(!getUnidadEjecutora().equals("____"))
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
         if(!getAmbito().equals("____")) 
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
         sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta());
         sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio()+"))");        
         sentencia.append("\n - (select round(nvl(sum(t."+mesAct+"_abono),0),"+tipoRedondeo+") as movimientos ");
         sentencia.append("\n from rf_tr_cuentas_contables t ");
         sentencia.append("\n where substr(t.cuenta_contable,1,4)=1151 and t.nivel=5 ");
         if(!getUnidadEjecutora().equals("____"))
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
         if(!getAmbito().equals("____")) 
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
         sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta());
         sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio()+")"); 
         sentencia.append("\n from dual ) as movimientos2  ");
         sentencia.append("\n from dual ");
         sentencia.append("\n union all ");
         sentencia.append("\n select 11 as orden,'' as etiqueta, 'SALIDA POR REMESAS' as salidas,round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+") as movimientos, 0 as RegAlmacen, 0 as movimientos2 ");
         sentencia.append("\n from rf_tr_cuentas_contables t  ");
         sentencia.append("\n where substr(t.cuenta_contable,1,5) in ('11235','11455') and t.nivel=5 ");
         if(!getUnidadEjecutora().equals("____"))
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
         if(!getAmbito().equals("____")) 
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
         sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta());
         sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio());
         sentencia.append("\n Order by orden "); 
        
        }    
        return sentencia;
    }

       public StringBuffer consultaISE_Variacion(String mes, String tipoRedondeo){
           StringBuffer sentencia = new StringBuffer();
           Fecha fecha = null;
           String mesAct = fecha.getNombreMesCorto(Integer.valueOf(mes)-1);
           String mesAnt = "";
           String mesCargoAcum = "";
           String mesAbonoAcum = "";
           tipoRedondeo = tipoRedondeo.equals("centavos")?"2":"0";
         if(mesAct.equals("ENE")){
           mesAnt = fecha.getNombreMesCorto(Integer.valueOf(mes)-1).concat("");
           mesCargoAcum = mesAnt.concat("_cargo_ini");
           mesAbonoAcum = mesAnt.concat("_abono_ini");
         }
         else{
           mesAnt = fecha.getNombreMesCorto(Integer.valueOf(mes)-2);
           mesCargoAcum = mesAnt.concat("_cargo_acum");
           mesAbonoAcum = mesAnt.concat("_abono_acum");
         } 
         
         
         if (Integer.parseInt(getEjercicio())<=2012){      
            sentencia.append("\n select 1 as orden,'' as etiqueta, 'VARIACIÓN EN CUENTA DE ALMACENES' as entradas, (select round(nvl(sum(t."+mesAct+"_cargo)*-1,0),"+tipoRedondeo+") as movimientos  ");
            sentencia.append("\n from rf_tr_cuentas_contables t  ");
            sentencia.append("\n where substr(t.cuenta_contable,1,4) >= 5121 and substr(t.cuenta_contable,1,4) <=5129   ");
            sentencia.append("\n and t.nivel=6 and substr(t.cuenta_contable,18,4)='0007'  ");
            if(!getUnidadEjecutora().equals("____"))
              sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
            if(!getAmbito().equals("____")) 
              sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
            sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") ");
            sentencia.append("\n -  ");
            sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+")  as movimientos from rf_tr_cuentas_contables t   ");
            sentencia.append("\n where substr(t.cuenta_contable,1,4) >= 5121 and substr(t.cuenta_contable,1,4) <=5129 and t.nivel=6  ");
            if(!getUnidadEjecutora().equals("____"))
             sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
            if(!getAmbito().equals("____")) 
             sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
            sentencia.append("\n and substr(t.cuenta_contable,18,4)='0006' and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+"  and extract(year from t.fecha_vig_ini)="+getEjercicio()+")   ");
            sentencia.append("\n +  ");
            sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_abono),0),"+tipoRedondeo+")  as movimientos from rf_tr_cuentas_contables t  ");
            sentencia.append("\n where substr(t.cuenta_contable,1,5)=43992 and t.nivel=7  ");
            sentencia.append("\n and substr(t.cuenta_contable,18,8)='00050003' and t.id_catalogo_cuenta="+getIdCatalogoCuenta() );
            if(!getUnidadEjecutora().equals("____"))
             sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
            if(!getAmbito().equals("____")) 
             sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
            sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio()+")");
            sentencia.append("\n +  ");            
            sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_abono),0),"+tipoRedondeo+") as movimientos  ");
            sentencia.append("\n from rf_tr_cuentas_contables t  ");
            sentencia.append("\n where substr(t.cuenta_contable,1,5)=43993 and t.nivel=5  ");
            if(!getUnidadEjecutora().equals("____"))
              sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
            if(!getAmbito().equals("____")) 
              sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
            sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta() );
            sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio()+")");
            sentencia.append("\n -  ");
            sentencia.append("\n ((select round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+") as mov from rf_tr_cuentas_contables t ");
            sentencia.append("\n where substr(t.cuenta_contable,1,4) >= 5121 and substr(t.cuenta_contable,1,4) <=5129 and t.nivel=6 and substr(t.cuenta_contable,18,4)='0005' ");
            if(!getUnidadEjecutora().equals("____"))
              sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
            if(!getAmbito().equals("____")) 
              sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
            sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") + ");
            sentencia.append("\n (select round(nvl((sum(case cla.naturaleza when 'D' then (t."+mesAnt+"_cargo_acum - t."+mesAnt+"_abono_acum) else (t."+mesAnt+"_abono_acum - t."+mesAnt+"_cargo_acum) end))*-1,0),"+tipoRedondeo+") as mov from rf_tr_cuentas_contables t INNER JOIN rf_tc_clasificador_cuentas cla  ");
            sentencia.append("\n  on t.cuenta_mayor_id = cla.cuenta_mayor_id where substr(t.cuenta_contable,1,5) = 11235 and t.nivel=5 ");
            if(!getUnidadEjecutora().equals("____"))
             sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
            if(!getAmbito().equals("____")) 
             sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
            sentencia.append("\n  and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") + ");        
            sentencia.append("\n (select round(nvl(sum( case cla.naturaleza when 'D' then ((t."+mesAnt+"_cargo_acum + t."+mesAct+"_cargo) - (t."+mesAnt+"_abono_acum + t."+mesAct+"_abono)) else ((t."+mesAnt+"_abono_acum +  t."+mesAct+"_abono) - (t."+mesAnt+"_cargo_acum + t."+mesAct+"_cargo)) end),0),"+tipoRedondeo+")  ");
            sentencia.append("\n from rf_tr_cuentas_contables t INNER JOIN rf_tc_clasificador_cuentas cla  on t.cuenta_mayor_id = cla.cuenta_mayor_id  ");
            sentencia.append("\n where substr(t.cuenta_contable,1,5) = 11235 and t.nivel=5 ");
            if(!getUnidadEjecutora().equals("____"))
              sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
            if(!getAmbito().equals("____")) 
              sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
           sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") + ");
            sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_abono),0),"+tipoRedondeo+") as mov from rf_tr_cuentas_contables t ");
            sentencia.append("\n where substr(t.cuenta_contable,1,5)=43992 and t.nivel=7 and substr(t.cuenta_contable,18,8)='00050001' ");
            if(!getUnidadEjecutora().equals("____"))
              sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
            if(!getAmbito().equals("____")) 
              sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
            sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") + ");
            sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+") as mov from rf_tr_cuentas_contables t ");
            sentencia.append("\n where substr(t.cuenta_contable,1,5)= 55351 and t.nivel=7 and (substr(t.cuenta_contable,18,4) >= 2100 and substr(t.cuenta_contable,18,4) <= 2900) and substr(t.cuenta_contable,22,4)= '0001' ");
            if(!getUnidadEjecutora().equals("____"))
              sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
            if(!getAmbito().equals("____")) 
              sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
            sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") + ");
            sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+") as mov from rf_tr_cuentas_contables t ");
            sentencia.append("\n where substr(t.cuenta_contable,1,5)= 55351 and t.nivel=7 and (substr(t.cuenta_contable,18,4) >= 2100 and substr(t.cuenta_contable,18,4) <= 2900) and substr(t.cuenta_contable,22,4)= '0002' ");
            if(!getUnidadEjecutora().equals("____"))
              sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
            if(!getAmbito().equals("____")) 
              sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
            sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") + ");
            sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+") as mov from rf_tr_cuentas_contables t ");
            sentencia.append("\n where substr(t.cuenta_contable,1,5)= 55351 and t.nivel=7 and (substr(t.cuenta_contable,18,4) >= 2100 and substr(t.cuenta_contable,18,4) <= 2900) and substr(t.cuenta_contable,22,4)= '0003' ");
            if(!getUnidadEjecutora().equals("____"))
              sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
            if(!getAmbito().equals("____")) 
              sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
            sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") + ");           
            sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+") as mov from rf_tr_cuentas_contables t  ");
            sentencia.append("\n where substr(t.cuenta_contable,1,5) = 11235 and t.nivel=5 ");
            if(!getUnidadEjecutora().equals("____"))
              sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
            if(!getAmbito().equals("____")) 
              sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
            sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") ");           
            sentencia.append("\n ) as movimientos,  ");
            sentencia.append("\n ((select round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+") as movimientos  ");
            sentencia.append("\n from rf_tr_cuentas_contables t  ");
            sentencia.append("\n where substr(t.cuenta_contable,1,4)=1151 and t.nivel=5  ");
            if(!getUnidadEjecutora().equals("____"))
              sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
            if(!getAmbito().equals("____")) 
              sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
            sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta() );
            sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio()+") ");
            sentencia.append("\n - ");
            sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_abono),0),"+tipoRedondeo+") as movimientos ");
            sentencia.append("\n from rf_tr_cuentas_contables t ");
            sentencia.append("\n where substr(t.cuenta_contable,1,4)=1151 and t.nivel=5 ");
            if(!getUnidadEjecutora().equals("____"))
              sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
            if(!getAmbito().equals("____")) 
              sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
            sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta());
            sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio()+")) as RegAlmacen,  ");
            sentencia.append("\n ((select round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+") as movimientos  ");
            sentencia.append("\n from rf_tr_cuentas_contables t  ");
            sentencia.append("\n where substr(t.cuenta_contable,1,4)=1151 and t.nivel=5  ");
            if(!getUnidadEjecutora().equals("____"))
              sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
            if(!getAmbito().equals("____")) 
              sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
            sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta() );
            sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio()+")- " );
            sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_abono),0),"+tipoRedondeo+") as movimientos  ");
            sentencia.append("\n from rf_tr_cuentas_contables t  ");
            sentencia.append("\n where substr(t.cuenta_contable,1,4)=1151 and t.nivel=5  ");
            sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta());
            if(!getUnidadEjecutora().equals("____"))
              sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
            if(!getAmbito().equals("____")) 
              sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
            sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio()+")) as movimientos2  ");
            sentencia.append("\n from dual  ");
        
         }else if ((Integer.parseInt(getEjercicio())==2013) && (Integer.parseInt(mes)<=5)){ //cambios a partir ejercicio 2013 y hasta mayor
           sentencia.append("\n select 1 as orden,'' as etiqueta, 'VARIACIÓN EN CUENTA DE ALMACENES' as entradas, (select round(nvl(sum(t."+mesAct+"_abono),0),"+tipoRedondeo+") as movimientos  ");
           sentencia.append("\n from rf_tr_cuentas_contables t  ");
           sentencia.append("\n where substr(t.cuenta_contable,1,5)='21122'   ");
           sentencia.append("\n and t.nivel=7  and substr(t.cuenta_contable,18,4)='9996' and substr(t.cuenta_contable,22,4)='0001'  ");
           if(!getUnidadEjecutora().equals("____"))
             sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
           if(!getAmbito().equals("____")) 
             sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
           sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") ");
           sentencia.append("\n +  ");
           sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_abono),0),"+tipoRedondeo+")  as movimientos from rf_tr_cuentas_contables t  ");
           sentencia.append("\n where substr(t.cuenta_contable,1,5)=43992 and t.nivel=7  ");
           sentencia.append("\n and substr(t.cuenta_contable,18,8)='00050003' and t.id_catalogo_cuenta="+getIdCatalogoCuenta() );
           if(!getUnidadEjecutora().equals("____"))
            sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
           if(!getAmbito().equals("____")) 
            sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
           sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio()+")");
           sentencia.append("\n +  ");            
           sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_abono),0),"+tipoRedondeo+") as movimientos  ");
           sentencia.append("\n from rf_tr_cuentas_contables t  ");
           sentencia.append("\n where substr(t.cuenta_contable,1,5)=43993 and t.nivel=5  ");
           if(!getUnidadEjecutora().equals("____"))
             sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
           if(!getAmbito().equals("____")) 
             sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
           sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta() );
           sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio()+")");
           sentencia.append("\n -  ");
           sentencia.append("\n ((select round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+") as mov from rf_tr_cuentas_contables t ");
           sentencia.append("\n where substr(t.cuenta_contable,1,4) >= 5121 and substr(t.cuenta_contable,1,4) <=5129 and t.nivel=6 and substr(t.cuenta_contable,18,4)='0005' ");
           if(!getUnidadEjecutora().equals("____"))
             sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
           if(!getAmbito().equals("____")) 
             sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
           sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") + ");
           sentencia.append("\n (select round(nvl((sum(case cla.naturaleza when 'D' then (t."+mesAnt+"_cargo_acum - t."+mesAnt+"_abono_acum) else (t."+mesAnt+"_abono_acum - t."+mesAnt+"_cargo_acum) end))*-1,0),"+tipoRedondeo+") as mov from rf_tr_cuentas_contables t INNER JOIN rf_tc_clasificador_cuentas cla  ");
           sentencia.append("\n  on t.cuenta_mayor_id = cla.cuenta_mayor_id where substr(t.cuenta_contable,1,5) = 11235 and t.nivel=5 ");
           if(!getUnidadEjecutora().equals("____"))
            sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
           if(!getAmbito().equals("____")) 
            sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
           sentencia.append("\n  and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") + ");        
           sentencia.append("\n (select round(nvl(sum( case cla.naturaleza when 'D' then ((t."+mesAnt+"_cargo_acum + t."+mesAct+"_cargo) - (t."+mesAnt+"_abono_acum + t."+mesAct+"_abono)) else ((t."+mesAnt+"_abono_acum +  t."+mesAct+"_abono) - (t."+mesAnt+"_cargo_acum + t."+mesAct+"_cargo)) end),0),"+tipoRedondeo+")  ");
           sentencia.append("\n from rf_tr_cuentas_contables t INNER JOIN rf_tc_clasificador_cuentas cla  on t.cuenta_mayor_id = cla.cuenta_mayor_id  ");
           sentencia.append("\n where substr(t.cuenta_contable,1,5) = 11235 and t.nivel=5 ");
           if(!getUnidadEjecutora().equals("____"))
             sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
           if(!getAmbito().equals("____")) 
             sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
           sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") + ");
           sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_abono),0),"+tipoRedondeo+") as mov from rf_tr_cuentas_contables t ");
           sentencia.append("\n where substr(t.cuenta_contable,1,5)=43992 and t.nivel=7 and substr(t.cuenta_contable,18,8)='00050001' ");
           if(!getUnidadEjecutora().equals("____"))
             sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
           if(!getAmbito().equals("____")) 
             sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
           sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") + ");
           sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+") as mov from rf_tr_cuentas_contables t ");
           sentencia.append("\n where substr(t.cuenta_contable,1,5)= 55351 and t.nivel=7 and (substr(t.cuenta_contable,18,4) >= 2100 and substr(t.cuenta_contable,18,4) <= 2900) and substr(t.cuenta_contable,22,4)= '0001' ");
           if(!getUnidadEjecutora().equals("____"))
             sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
           if(!getAmbito().equals("____")) 
             sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
           sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") + ");
           sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+") as mov from rf_tr_cuentas_contables t ");
           sentencia.append("\n where substr(t.cuenta_contable,1,5)= 55351 and t.nivel=7 and (substr(t.cuenta_contable,18,4) >= 2100 and substr(t.cuenta_contable,18,4) <= 2900) and substr(t.cuenta_contable,22,4)= '0002' ");
           if(!getUnidadEjecutora().equals("____"))
             sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
           if(!getAmbito().equals("____")) 
             sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
           sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") + ");
           sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+") as mov from rf_tr_cuentas_contables t ");
           sentencia.append("\n where substr(t.cuenta_contable,1,5)= 55351 and t.nivel=7 and (substr(t.cuenta_contable,18,4) >= 2100 and substr(t.cuenta_contable,18,4) <= 2900) and substr(t.cuenta_contable,22,4)= '0003' ");
           if(!getUnidadEjecutora().equals("____"))
             sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
           if(!getAmbito().equals("____")) 
             sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
           sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") + ");           
           sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+") as mov from rf_tr_cuentas_contables t  ");
           sentencia.append("\n where substr(t.cuenta_contable,1,5) in ('11235','11455') and t.nivel=5 ");
           if(!getUnidadEjecutora().equals("____"))
             sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
           if(!getAmbito().equals("____")) 
             sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
           sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") ");           
           sentencia.append("\n ) as movimientos,  ");
           sentencia.append("\n ((select round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+") as movimientos  ");
           sentencia.append("\n from rf_tr_cuentas_contables t  ");
           sentencia.append("\n where substr(t.cuenta_contable,1,4)=1151 and t.nivel=5  ");
           if(!getUnidadEjecutora().equals("____"))
             sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
           if(!getAmbito().equals("____")) 
             sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
           sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta() );
           sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio()+") ");
           sentencia.append("\n - ");
           sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_abono),0),"+tipoRedondeo+") as movimientos ");
           sentencia.append("\n from rf_tr_cuentas_contables t ");
           sentencia.append("\n where substr(t.cuenta_contable,1,4)=1151 and t.nivel=5 ");
           if(!getUnidadEjecutora().equals("____"))
             sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
           if(!getAmbito().equals("____")) 
             sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
           sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta());
           sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio()+")) as RegAlmacen,  ");
           sentencia.append("\n ((select round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+") as movimientos  ");
           sentencia.append("\n from rf_tr_cuentas_contables t  ");
           sentencia.append("\n where substr(t.cuenta_contable,1,4)=1151 and t.nivel=5  ");
           if(!getUnidadEjecutora().equals("____"))
             sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
           if(!getAmbito().equals("____")) 
             sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
           sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta() );
           sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio()+")- " );
           sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_abono),0),"+tipoRedondeo+") as movimientos  ");
           sentencia.append("\n from rf_tr_cuentas_contables t  ");
           sentencia.append("\n where substr(t.cuenta_contable,1,4)=1151 and t.nivel=5  ");
           sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta());
           if(!getUnidadEjecutora().equals("____"))
             sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
           if(!getAmbito().equals("____")) 
             sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
           sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio()+")) as movimientos2  ");
           sentencia.append("\n from dual  ");
        }else if (((Integer.parseInt(getEjercicio())==2013) && (Integer.parseInt(mes)>5)) || (Integer.parseInt(getEjercicio())>2013)) { //cambios a partir ejercicio 2013 despues de junio

         sentencia.append("\n select 1 as orden,'' as etiqueta, 'VARIACIÓN EN CUENTA DE ALMACENES' as entradas, (select round(nvl(sum(t."+mesAct+"_abono),0),"+tipoRedondeo+") as movimientos  ");
         sentencia.append("\n from rf_tr_cuentas_contables t  ");
         sentencia.append("\n where substr(t.cuenta_contable,1,5)='21122'   ");
         sentencia.append("\n and t.nivel=7  and substr(t.cuenta_contable,18,4)='9996' and substr(t.cuenta_contable,22,4)='0001'  ");
         if(!getUnidadEjecutora().equals("____"))
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
         if(!getAmbito().equals("____")) 
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
         sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") ");
         sentencia.append("\n +  ");
         sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_abono),0),"+tipoRedondeo+")  as movimientos from rf_tr_cuentas_contables t  ");
         sentencia.append("\n where substr(t.cuenta_contable,1,5)=43992 and t.nivel=7  ");
         sentencia.append("\n and substr(t.cuenta_contable,18,4)='0005' and substr(t.cuenta_contable,22,4) in ('0003','0005','0006','0007','0008','0009') and t.id_catalogo_cuenta="+getIdCatalogoCuenta() );
         if(!getUnidadEjecutora().equals("____"))
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
         if(!getAmbito().equals("____")) 
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
         sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio()+")");
          sentencia.append("\n +  ");
          sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_cargo)*-1,0),"+tipoRedondeo+")  as movimientos from rf_tr_cuentas_contables t  ");
          sentencia.append("\n where substr(t.cuenta_contable,1,5)=11312 and t.nivel=6  ");
          sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta() );
          if(!getUnidadEjecutora().equals("____"))
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
          if(!getAmbito().equals("____")) 
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
          sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio()+")");
         sentencia.append("\n +  ");            
         sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_abono),0),"+tipoRedondeo+") as movimientos  ");
         sentencia.append("\n from rf_tr_cuentas_contables t  ");
         sentencia.append("\n where substr(t.cuenta_contable,1,5)=43993 and t.nivel=5  ");
         if(!getUnidadEjecutora().equals("____"))
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
         if(!getAmbito().equals("____")) 
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
         sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta() );
         sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio()+")");
         sentencia.append("\n -  ");
         sentencia.append("\n ((select round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+") as mov from rf_tr_cuentas_contables t ");
         sentencia.append("\n where substr(t.cuenta_contable,1,4) >= 5121 and substr(t.cuenta_contable,1,4) <=5129 and t.nivel=6 and substr(t.cuenta_contable,18,4)='0005' ");
         if(!getUnidadEjecutora().equals("____"))
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
         if(!getAmbito().equals("____")) 
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
         sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") + ");
         sentencia.append("\n (select round(nvl((sum(case cla.naturaleza when 'D' then (t."+mesAnt+"_cargo_acum - t."+mesAnt+"_abono_acum) else (t."+mesAnt+"_abono_acum - t."+mesAnt+"_cargo_acum) end))*-1,0),"+tipoRedondeo+") as mov from rf_tr_cuentas_contables t INNER JOIN rf_tc_clasificador_cuentas cla  ");
         sentencia.append("\n  on t.cuenta_mayor_id = cla.cuenta_mayor_id where substr(t.cuenta_contable,1,5) = 11235 and t.nivel=5 ");
         if(!getUnidadEjecutora().equals("____"))
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
         if(!getAmbito().equals("____")) 
          sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
         sentencia.append("\n  and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") + ");        
         sentencia.append("\n (select round(nvl(sum( case cla.naturaleza when 'D' then ((t."+mesAnt+"_cargo_acum + t."+mesAct+"_cargo) - (t."+mesAnt+"_abono_acum + t."+mesAct+"_abono)) else ((t."+mesAnt+"_abono_acum +  t."+mesAct+"_abono) - (t."+mesAnt+"_cargo_acum + t."+mesAct+"_cargo)) end),0),"+tipoRedondeo+")  ");
         sentencia.append("\n from rf_tr_cuentas_contables t INNER JOIN rf_tc_clasificador_cuentas cla  on t.cuenta_mayor_id = cla.cuenta_mayor_id  ");
         sentencia.append("\n where substr(t.cuenta_contable,1,5) = 11235 and t.nivel=5 ");
         if(!getUnidadEjecutora().equals("____"))
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
         if(!getAmbito().equals("____")) 
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
         sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") + ");
         sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_abono),0),"+tipoRedondeo+") as mov from rf_tr_cuentas_contables t ");
         sentencia.append("\n where substr(t.cuenta_contable,1,5)=43992 and t.nivel=7 and substr(t.cuenta_contable,18,8)='00050001' ");
         if(!getUnidadEjecutora().equals("____"))
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
         if(!getAmbito().equals("____")) 
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
         sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") + ");
         sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+") as mov from rf_tr_cuentas_contables t ");
         sentencia.append("\n where substr(t.cuenta_contable,1,5)= 55351 and t.nivel=7 and (substr(t.cuenta_contable,18,4) >= 2100 and substr(t.cuenta_contable,18,4) <= 2900) and substr(t.cuenta_contable,22,4)= '0001' ");
         if(!getUnidadEjecutora().equals("____"))
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
         if(!getAmbito().equals("____")) 
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
         sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") + ");
         sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+") as mov from rf_tr_cuentas_contables t ");
         sentencia.append("\n where substr(t.cuenta_contable,1,5)= 55351 and t.nivel=7 and (substr(t.cuenta_contable,18,4) >= 2100 and substr(t.cuenta_contable,18,4) <= 2900) and substr(t.cuenta_contable,22,4)= '0002' ");
         if(!getUnidadEjecutora().equals("____"))
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
         if(!getAmbito().equals("____")) 
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
         sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") + ");
         
         sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+") as mov from rf_tr_cuentas_contables t ");
         sentencia.append("\n where substr(t.cuenta_contable,1,5)= 55994 and t.nivel=6  and substr(t.cuenta_contable,18,4) IN ('0001','0002') ");
         if(!getUnidadEjecutora().equals("____"))
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
         if(!getAmbito().equals("____")) 
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
         sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") + ");
         
         
         
         sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+") as mov from rf_tr_cuentas_contables t ");
         sentencia.append("\n where substr(t.cuenta_contable,1,5)= 55351 and t.nivel=7 and (substr(t.cuenta_contable,18,4) >= 2100 and substr(t.cuenta_contable,18,4) <= 2900) and substr(t.cuenta_contable,22,4)= '0003' ");
         if(!getUnidadEjecutora().equals("____"))
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
         if(!getAmbito().equals("____")) 
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
         sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") + ");           
         sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+") as mov from rf_tr_cuentas_contables t  ");
         sentencia.append("\n where substr(t.cuenta_contable,1,5)  in ('11235','11455') and t.nivel=5 ");
         if(!getUnidadEjecutora().equals("____"))
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
         if(!getAmbito().equals("____")) 
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
         sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+") ");           
         sentencia.append("\n ) as movimientos,  ");
         sentencia.append("\n ((select round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+") as movimientos  ");
         sentencia.append("\n from rf_tr_cuentas_contables t  ");
         sentencia.append("\n where substr(t.cuenta_contable,1,4)=1151 and t.nivel=5  ");
         if(!getUnidadEjecutora().equals("____"))
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
         if(!getAmbito().equals("____")) 
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
         sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta() );
         sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio()+") ");
         sentencia.append("\n - ");
         sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_abono),0),"+tipoRedondeo+") as movimientos ");
         sentencia.append("\n from rf_tr_cuentas_contables t ");
         sentencia.append("\n where substr(t.cuenta_contable,1,4)=1151 and t.nivel=5 ");
         if(!getUnidadEjecutora().equals("____"))
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
         if(!getAmbito().equals("____")) 
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
         sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta());
         sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio()+")) as RegAlmacen,  ");
         sentencia.append("\n ((select round(nvl(sum(t."+mesAct+"_cargo),0),"+tipoRedondeo+") as movimientos  ");
         sentencia.append("\n from rf_tr_cuentas_contables t  ");
         sentencia.append("\n where substr(t.cuenta_contable,1,4)=1151 and t.nivel=5  ");
         if(!getUnidadEjecutora().equals("____"))
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
         if(!getAmbito().equals("____")) 
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
         sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta() );
         sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio()+")- " );
         sentencia.append("\n (select round(nvl(sum(t."+mesAct+"_abono),0),"+tipoRedondeo+") as movimientos  ");
         sentencia.append("\n from rf_tr_cuentas_contables t  ");
         sentencia.append("\n where substr(t.cuenta_contable,1,4)=1151 and t.nivel=5  ");
         sentencia.append("\n and t.id_catalogo_cuenta="+getIdCatalogoCuenta());
         if(!getUnidadEjecutora().equals("____"))
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
         if(!getAmbito().equals("____")) 
           sentencia.append("\n and substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());                 
         sentencia.append("\n and extract(year from t.fecha_vig_ini)="+getEjercicio()+")) as movimientos2  ");
         sentencia.append("\n from dual  ");
         
        }
          return sentencia;
       }
}
