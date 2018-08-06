package sia.rf.contabilidad.reportes.conciliacion;

import sia.libs.formato.Fecha;
//import sia.libs.periodo.Fecha;

public class ConsultasReportes {
  private String idCatalogoCuenta;
  private String ejercicio;
    private String unidadEjecutora;
    private String ambito;
    private String programa;

    public ConsultasReportes() {
      this("","","","","");
    }

    public ConsultasReportes(String idCatalogoCuenta, String ejercicio, String unidadEjecutora, String ambito, String programa) {
      setIdCatalogoCuenta(idCatalogoCuenta);
      setEjercicio(ejercicio);
      setUnidadEjecutora(unidadEjecutora);
      setAmbito(ambito);
      setPrograma(programa);
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
    
    public StringBuffer contabilidadMipf(String mes, String tipoRedondeo, String tipoCierre){
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
      sentencia.append("\nselect Initcap(des.descripcion) descripcion,");
      sentencia.append("\ndecode(mipf.criterio,'0001','1','0002','2',mipf.criterio) criterio, mipf.unidad, mipf.estado,  ");

      sentencia.append("\nround(decode(sia.pres_egre_apro,'',0,sia.pres_egre_apro),"+tipoRedondeo+") pres_egre_apro, round(decode(mipf.pre_aut_ori_anu,'',0,mipf.pre_aut_ori_anu),"+tipoRedondeo+") pre_aut_ori_anu, round((decode(sia.pres_egre_apro,'',0,sia.pres_egre_apro) - decode(mipf.pre_aut_ori_anu,'',0,mipf.pre_aut_ori_anu)),"+tipoRedondeo+") variacion1,");
      sentencia.append("\nround(decode(sia.mod_pres_egre_apro,'',0,sia.mod_pres_egre_apro),"+tipoRedondeo+") mod_pres_egre_apro, round((decode(mipf.Amp,'',0,mipf.Amp) - decode(mipf.Red,'',0,mipf.Red)),"+tipoRedondeo+") amp_red, round((decode(sia.mod_pres_egre_apro,'',0,sia.mod_pres_egre_apro) - (decode(mipf.Amp,'',0,mipf.Amp) - decode(mipf.Red,'',0,mipf.Red))),"+tipoRedondeo+") variacion2,");
      sentencia.append("\nround(decode(sia.pres_egre_com,'',0,sia.pres_egre_com),"+tipoRedondeo+") pres_egre_com,  round((decode((mipf.pre_com_acum),'',0,mipf.pre_com_acum)+ decode((mipf.gas_x_com),'',0,mipf.gas_x_com)),"+tipoRedondeo+") pre_com_acum, round((decode(sia.pres_egre_com,'',0,sia.pres_egre_com) - (decode(mipf.pre_com_acum,'',0,mipf.pre_com_acum)+decode((mipf.gas_x_com),'',0,mipf.gas_x_com))),"+tipoRedondeo+") variacion3,");
      sentencia.append("\nround(decode(sia.pres_egre_dev,'',0,sia.pres_egre_dev),"+tipoRedondeo+") pres_egre_dev, round(decode(mipf.pre_ejer_acum,'',0,mipf.pre_ejer_acum),"+tipoRedondeo+") pre_ejer_acum, round((decode(sia.pres_egre_dev,'',0,sia.pres_egre_dev) - decode(mipf.pre_ejer_acum,'',0,mipf.pre_ejer_acum)),"+tipoRedondeo+") variacion4,");
      sentencia.append("\nround(decode(sia.pres_egre_ejer,'',0,sia.pres_egre_ejer),"+tipoRedondeo+") pres_egre_ejer, round(decode(mipf.pre_ejer_acum,'',0,mipf.pre_ejer_acum),"+tipoRedondeo+") pre_ejer_acum, round((decode(sia.pres_egre_ejer,'',0,sia.pres_egre_ejer) - decode(mipf.pre_ejer_acum,'',0,mipf.pre_ejer_acum)),"+tipoRedondeo+") variacion5");
      sentencia.append("\nfrom   ");
      sentencia.append("\n(select criterio, unidad, estado, sum(pres_egre_dev) pres_egre_dev, sum(pres_egre_ejer) pres_egre_ejer, sum(pres_egre_com) pres_egre_com, sum(pres_egre_apro) pres_egre_apro, sum(mod_pres_egre_apro) mod_pres_egre_apro");
      sentencia.append("\nfrom (   ");
      sentencia.append("\nselect saldo_cuenta.*,   ");
      sentencia.append("\ncase when subStr(cuenta_contable,1,5) ='82501' then cargo_acum  else 0 end pres_egre_dev, ");
      sentencia.append("\ncase when subStr(cuenta_contable,1,5) ='82601' then cargo_acum  else 0 end pres_egre_ejer,");
      sentencia.append("\ncase when subStr(cuenta_contable,1,5) ='82401' then saldo  else 0 end pres_egre_com,");
      sentencia.append("\ncase when subStr(cuenta_contable,1,5) ='82101' then abono_acum  else 0 end pres_egre_apro,");
      sentencia.append("\ncase when subStr(cuenta_contable,1,5) ='82301' then saldo  else 0 end mod_pres_egre_apro");
      sentencia.append("\nfrom(   ");
      sentencia.append("\nselect substr(t.cuenta_contable,f_posicion_nivel('6',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('6',t.cuenta_mayor_id,"+getEjercicio()+")) criterio,   ");
      sentencia.append("\nsubstr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+")) unidad,   ");
      sentencia.append("\nsubstr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+")) estado, tc.naturaleza,   ");
      sentencia.append("\nt.cuenta_contable,   ");
      sentencia.append("\n("+mesCargoAcum+" + "+mesAct+"_cargo) cargo_acum, 0 abono_acum, 0 saldo ");
      sentencia.append("\nfrom rf_tr_cuentas_contables t,rf_tc_clasificador_cuentas  tc   ");
      sentencia.append("\nwhere t.cuenta_mayor_id = tc.cuenta_mayor_id   ");
      sentencia.append("\nand t.id_catalogo_cuenta="+getIdCatalogoCuenta());
      sentencia.append("\nand extract(year from t.fecha_vig_ini)="+getEjercicio());
      sentencia.append("\nand (t.cuenta_contable  like '82501____________1000%' and t.nivel=6 or  ");
      sentencia.append("\nt.cuenta_contable  like '82501____________2000%' and t.nivel=6 or ");
      sentencia.append("\nt.cuenta_contable  like '82501____________3000%' and t.nivel=6 or  ");
      sentencia.append("\nt.cuenta_contable  like '82501____________4000%' and t.nivel=6 or ");
      sentencia.append("\nt.cuenta_contable  like '82501____________5000%' and t.nivel=6 or  ");
      sentencia.append("\nt.cuenta_contable  like '82501____________6000%' and t.nivel=6 or ");
      sentencia.append("\nt.cuenta_contable  like '82501____________7000%' and t.nivel=6 or");
      sentencia.append("\nt.cuenta_contable  like '82601____________1000%' and t.nivel=6 or  ");
      sentencia.append("\nt.cuenta_contable  like '82601____________2000%' and t.nivel=6 or ");
      sentencia.append("\nt.cuenta_contable  like '82601____________3000%' and t.nivel=6 or  ");
      sentencia.append("\nt.cuenta_contable  like '82601____________4000%' and t.nivel=6 or ");
      sentencia.append("\nt.cuenta_contable  like '82601____________5000%' and t.nivel=6 or  ");
      sentencia.append("\nt.cuenta_contable  like '82601____________6000%' and t.nivel=6 or ");
      sentencia.append("\nt.cuenta_contable  like '82601____________7000%' and t.nivel=6)");

      if(!getPrograma().equals("'____'"))
        sentencia.append("\nand substr(t.cuenta_contable,f_posicion_nivel('programa',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('programa',t.cuenta_mayor_id,"+getEjercicio()+")) in ("+getPrograma()+")");
      if(!getUnidadEjecutora().equals("____"))
        sentencia.append("\nand substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
      if(!getAmbito().equals("____")) 
        sentencia.append("\nand substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());
      sentencia.append("\nunion all");
      sentencia.append("\nselect substr(t.cuenta_contable,f_posicion_nivel('6',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('6',t.cuenta_mayor_id,"+getEjercicio()+")) criterio,  ");
      sentencia.append("\nsubstr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+")) unidad,  ");
      sentencia.append("\nsubstr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+")) estado, tc.naturaleza,  ");
      sentencia.append("\nt.cuenta_contable,  ");
      sentencia.append("\n0 cargo_acum, ("+mesAbonoAcum+" + "+mesAct+"_abono) abono_acum, 0 saldo");
      sentencia.append("\nfrom rf_tr_cuentas_contables t,rf_tc_clasificador_cuentas  tc  ");
      sentencia.append("\nwhere t.cuenta_mayor_id = tc.cuenta_mayor_id  ");
      sentencia.append("\nand t.id_catalogo_cuenta="+getIdCatalogoCuenta());
      sentencia.append("\nand extract(year from t.fecha_vig_ini)="+getEjercicio());
      sentencia.append("\nand (t.cuenta_contable  like '82101____________0001%' and t.nivel=6 or ");
      sentencia.append("\nt.cuenta_contable  like '82101____________0002%' and t.nivel=6 )   ");
      if(!getPrograma().equals("'____'"))
        sentencia.append("\nand substr(t.cuenta_contable,f_posicion_nivel('programa',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('programa',t.cuenta_mayor_id,"+getEjercicio()+")) in ("+getPrograma()+")");
      if(!getUnidadEjecutora().equals("____"))
        sentencia.append("\nand substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
      if(!getAmbito().equals("____")) 
        sentencia.append("\nand substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());
      sentencia.append("\nunion all");
      sentencia.append("\nselect substr(t.cuenta_contable,f_posicion_nivel('6',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('6',t.cuenta_mayor_id,"+getEjercicio()+")) criterio,  ");
      sentencia.append("\nsubstr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+")) unidad,  ");
      sentencia.append("\nsubstr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+")) estado, tc.naturaleza,  ");
      sentencia.append("\nt.cuenta_contable,");
      sentencia.append("\n0 cargo_acum, 0 abono_acum, case tc.naturaleza when 'D' then (("+mesCargoAcum+" + "+mesAct+"_cargo) - ("+mesAbonoAcum+" + "+mesAct+"_abono))  ");
      sentencia.append("\nelse (("+mesAbonoAcum+" + "+mesAct+"_abono) - ("+mesCargoAcum+" + "+mesAct+"_cargo)) end saldo ");
      sentencia.append("\nfrom rf_tr_cuentas_contables t,rf_tc_clasificador_cuentas  tc  ");
      sentencia.append("\nwhere t.cuenta_mayor_id = tc.cuenta_mayor_id  ");
      sentencia.append("\nand t.id_catalogo_cuenta="+getIdCatalogoCuenta());
      sentencia.append("\nand extract(year from t.fecha_vig_ini)="+getEjercicio());
      sentencia.append("\nand (t.cuenta_contable  like '82301____________0001%' and t.nivel=6 or ");
      sentencia.append("\nt.cuenta_contable  like '82301____________0002%' and t.nivel=6 or ");
      sentencia.append("\nt.cuenta_contable  like '82401____________0001%' and t.nivel=6 or  ");
      sentencia.append("\nt.cuenta_contable  like '82401____________0002%' and t.nivel=6 or ");
      sentencia.append("\nt.cuenta_contable  like '82401____________5000%' and t.nivel=6 or "); 
      sentencia.append("\nt.cuenta_contable  like '82401____________6000%' and t.nivel=6)");
      if(!getPrograma().equals("'____'"))
        sentencia.append("\nand substr(t.cuenta_contable,f_posicion_nivel('programa',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('programa',t.cuenta_mayor_id,"+getEjercicio()+")) in ("+getPrograma()+")");
      if(!getUnidadEjecutora().equals("____"))
        sentencia.append("\nand substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
      if(!getAmbito().equals("____")) 
        sentencia.append("\nand substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());
      sentencia.append("\n)saldo_cuenta )x   ");
      sentencia.append("\ngroup by criterio, unidad, estado) sia,   ");
      sentencia.append("\n(SELECT criterio, unidad, estado, ");
      sentencia.append("\nsum(pre_aut_ori_anu) pre_aut_ori_anu,");
      sentencia.append("\nsum(Amp) Amp,");
      sentencia.append("\nsum(Red) Red,");
      sentencia.append("\nsum(pre_com_acum) pre_com_acum,");
      sentencia.append("\nsum(pre_ejer_acum) pre_ejer_acum,");
      sentencia.append("\nsum(GXCac)  gas_x_com");
      sentencia.append("\nFROM  (   ");
      sentencia.append("\nSELECT   ");
      sentencia.append("\nsum(decode(id_tipo_presup,1, enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + diciembre + 0,0)) as pre_aut_ori_anu, ");
      sentencia.append("\nsum(decode(id_tipo_presup,8, enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + diciembre + 0,0)) as Amp,  ");
      sentencia.append("\nsum(decode(id_tipo_presup,9, enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + diciembre + 0,0)) as Red,  ");
      sentencia.append("\n0 as pre_com_acum, ");
      sentencia.append("\n0 pre_ejer_acum,");
      sentencia.append("\n0 GXCac,");
      sentencia.append("\ncase when to_number(substr(pp.partida,0,1))<= 1 then '0001' else '0002' end as criterio, lpad(ruc.unidad_ejecutora,4,'0') as unidad,  ");
      sentencia.append("\nlpad(decode(ruc.unidad_ejecutora,'100','1',ruc.estado)||ruc.ambito,4,'0') estado");
      sentencia.append("\nFROM    ");
      sentencia.append("\nrf_tv_cierre_presupuesto@dblsia_sic p   ");
      sentencia.append("\nleft join rf_tc_claves_presupuestarias@dblsia_sic cp on p.clave_presupuestaria_id = cp.clave_presupuestaria_id   ");
      sentencia.append("\ninner join rf_tr_rel_uea_cveprog@dblsia_sic ruc on cp.id_rel_uea_cve_prog = ruc.id_rel_uea_cve_prog   ");
      sentencia.append("\ninner join rf_tr_claves_programaticas@dblsia_sic cpg on ruc.id_cve_prog=cpg.id_cve_prog and cpg.eliminado = 0   ");
      sentencia.append("\ninner join rf_tc_partidas_presupuestales@dblsia_sic pp on cp.id_partida=pp.id_partida   ");
      sentencia.append("\nWHERE  p.ejercicio ="+ getEjercicio());
      sentencia.append("\nand p.mes ="+mes);
      sentencia.append("\nand p.corte = (   ");
      sentencia.append("\nselect max(corte)  ");
      sentencia.append("\nfrom rf_tv_control_meses@dblsia_sic   ");
      sentencia.append("\nwhere ejercicio="+getEjercicio()+" and mes="+mes+" and cierre="+tipoCierre+")");
      if(getPrograma().contains("0008"))
        sentencia.append("   and (cpg.clave_programatica like '01P02280401%' or cpg.clave_programatica like '01P02280501%' or cpg.clave_programatica like '01P02280601%') ");
      if(programa.contains("0001"))
        sentencia.append("   and not (cpg.clave_programatica like '01P02280401%' or cpg.clave_programatica like '01P02280501%' or cpg.clave_programatica like '01P02280601%') ");
      sentencia.append("\nGROUP BY   ");
      sentencia.append("\ncase when to_number(substr(pp.partida,0,1))<= 1 then '0001' else '0002' end, p.mes, lpad(ruc.unidad_ejecutora,4,'0'),  ");
      sentencia.append("\nlpad(decode(ruc.unidad_ejecutora,'100','1',ruc.estado)||ruc.ambito,4,'0')  ");
      sentencia.append("\nunion all ");
      sentencia.append("\nSELECT  ");
      sentencia.append("\n0 pre_aut_ori_anu,  ");
      sentencia.append("\n0 Amp,   ");
      sentencia.append("\n0 Red,   ");
      sentencia.append("\nsum(decode(id_tipo_presup,4, enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + diciembre + 0,0)) as pre_com_acum, ");
      sentencia.append("\n0 pre_ejer_acum, ");
      sentencia.append("\n0 GXCac, ");
      sentencia.append("\ncase when to_number(substr(pp.partida,0,1))<= 1 then '0001' else '0002' end as criterio, lpad(ruc.unidad_ejecutora,4,'0') as unidad,   ");
      sentencia.append("\nlpad(decode(ruc.unidad_ejecutora,'100','1',ruc.estado)||ruc.ambito,4,'0') estado ");
      sentencia.append("\nFROM     ");
      sentencia.append("\nrf_tv_cierre_presupuesto@dblsia_sic p    ");
      sentencia.append("\nleft join rf_tc_claves_presupuestarias@dblsia_sic cp on p.clave_presupuestaria_id = cp.clave_presupuestaria_id    ");
      sentencia.append("\ninner join rf_tr_rel_uea_cveprog@dblsia_sic ruc on cp.id_rel_uea_cve_prog = ruc.id_rel_uea_cve_prog    ");
      sentencia.append("\ninner join rf_tr_claves_programaticas@dblsia_sic cpg on ruc.id_cve_prog=cpg.id_cve_prog and cpg.eliminado = 0    ");
      sentencia.append("\ninner join rf_tc_partidas_presupuestales@dblsia_sic pp on cp.id_partida=pp.id_partida    ");
      sentencia.append("\nWHERE  p.ejercicio ="+getEjercicio());
      sentencia.append("\nand p.mes ="+mes);
      sentencia.append("\nand substr(pp.partida,0,1) not in ('5','6')  ");
      sentencia.append("\nand p.corte = (    ");
      sentencia.append("\nselect max(corte)   ");
      sentencia.append("\nfrom rf_tv_control_meses@dblsia_sic    ");
      sentencia.append("\nwhere ejercicio="+getEjercicio()+" and mes="+mes+" and cierre="+tipoCierre+") ");
      if(getPrograma().contains("0008"))
        sentencia.append("   and (cpg.clave_programatica like '01P02280401%' or cpg.clave_programatica like '01P02280501%' or cpg.clave_programatica like '01P02280601%') ");
      if(programa.contains("0001"))
        sentencia.append("   and not (cpg.clave_programatica like '01P02280401%' or cpg.clave_programatica like '01P02280501%' or cpg.clave_programatica like '01P02280601%') ");
      sentencia.append("\nGROUP BY ");
      sentencia.append("\ncase when to_number(substr(pp.partida,0,1))<= 1 then '0001' else '0002' end, p.mes, lpad(ruc.unidad_ejecutora,4,'0'),   ");
      sentencia.append("\nlpad(decode(ruc.unidad_ejecutora,'100','1',ruc.estado)||ruc.ambito,4,'0')   ");
      sentencia.append("\nunion all");
      sentencia.append("\nselect");
      sentencia.append("\n0 pre_aut_ori_anu,");
      sentencia.append("\n0 Amp,");
      sentencia.append("\n0 Red,");
      sentencia.append("\n0 pre_com_acum,");
      sentencia.append("\n0 pre_ejer_acum,   ");
      sentencia.append("\n(sum( (ministrado1 - comprobado1) + (ministrado2 - comprobado2) + (ministrado3 - comprobado3) + (ministrado4 - comprobado4) + (ministrado5 - comprobado5) + (ministrado6 - comprobado6) + (ministrado7 - comprobado7) + (ministrado8 - comprobado8) + (ministrado9 - comprobado9) + (ministrado10 - comprobado10) + (ministrado11 - comprobado11) + (ministrado12 - comprobado12) )) as GXCac, ");
      sentencia.append("\ncase when to_number(substr(pp.partida,0,1))<= 1 then '0001' else '0002' end as criterio, lpad(ruc.unidad_ejecutora,4,'0') as unidad,  ");
      sentencia.append("\nlpad(decode(ruc.unidad_ejecutora,'100','1',ruc.estado)||ruc.ambito,4,'0') estado");
      sentencia.append("\nFROM   ");
      sentencia.append("\nrf_tv_cierre_financiero@dblsia_sic cf, rf_tc_claves_presupuestarias@dblsia_sic cp,");
      sentencia.append("\nrf_tc_partidas_presupuestales@dblsia_sic pp, rf_tr_rel_uea_cveprog@dblsia_sic ruc,");
      sentencia.append("\nrf_tr_claves_programaticas@dblsia_sic cpg");
      sentencia.append("\nwhere ");
      sentencia.append("\ncp.clave_presupuestaria_id = cf.clave_presupuestaria_id  ");
      sentencia.append("\nand cp.id_partida=pp.id_partida ");
      sentencia.append("\nand cp.id_rel_uea_cve_prog = ruc.id_rel_uea_cve_prog ");
      sentencia.append("\nand ruc.id_cve_prog=cpg.id_cve_prog and cpg.eliminado = 0 ");
      sentencia.append("\nand cf.ejercicio = "+getEjercicio()+" and cf.mes ="+mes);
      sentencia.append("\nand cf.corte = (  ");
      sentencia.append("\nselect max(corte)  ");
      sentencia.append("\nfrom rf_tv_control_meses@dblsia_sic ");
      sentencia.append("\nwhere ejercicio = "+getEjercicio()+"  and mes = "+mes+" and cierre="+tipoCierre+") ");
      if(getPrograma().contains("0008"))
        sentencia.append("   and (cpg.clave_programatica like '01P02280401%' or cpg.clave_programatica like '01P02280501%' or cpg.clave_programatica like '01P02280601%') ");
      if(programa.contains("0001"))
        sentencia.append("   and not (cpg.clave_programatica like '01P02280401%' or cpg.clave_programatica like '01P02280501%' or cpg.clave_programatica like '01P02280601%') ");
      sentencia.append("\nGROUP BY ");
      sentencia.append("\ncase when to_number(substr(pp.partida,0,1))<= 1 then '0001' else '0002' end, cf.mes, lpad(ruc.unidad_ejecutora,4,'0'),  ");
      sentencia.append("\nlpad(decode(ruc.unidad_ejecutora,'100','1',ruc.estado)||ruc.ambito,4,'0')");
      
      sentencia.append("\nunion all");
      sentencia.append("\nSELECT  ");
      sentencia.append("\n0 pre_aut_ori_anu,");
      sentencia.append("\n0 Amp,");
      sentencia.append("\n0 Red,");
      sentencia.append("\n0 pre_com_acum,");
      sentencia.append("\nsum(decode(id_tipo_presup,5, enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + diciembre + 0,6, enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + diciembre + 0,7, enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + diciembre + 0,0)) as pre_ejer_acum,  ");
      sentencia.append("\n0 GXCac,");
      sentencia.append("\nsubstr(pp.partida,0,1)||'000' as criterio, lpad(ruc.unidad_ejecutora,4,'0') as unidad,  ");
      sentencia.append("\nlpad(decode(ruc.unidad_ejecutora,'100','1',ruc.estado)||ruc.ambito,4,'0') estado ");
      sentencia.append("\nFROM   ");
      sentencia.append("\nrf_tv_cierre_presupuesto@dblsia_sic p   ");
      sentencia.append("\nleft join rf_tc_claves_presupuestarias@dblsia_sic cp on p.clave_presupuestaria_id = cp.clave_presupuestaria_id   ");
      sentencia.append("\ninner join rf_tr_rel_uea_cveprog@dblsia_sic ruc on cp.id_rel_uea_cve_prog = ruc.id_rel_uea_cve_prog  ");
      sentencia.append("\ninner join rf_tr_claves_programaticas@dblsia_sic cpg on ruc.id_cve_prog=cpg.id_cve_prog and cpg.eliminado = 0   ");
      sentencia.append("\ninner join rf_tc_partidas_presupuestales@dblsia_sic pp on cp.id_partida=pp.id_partida   ");
      sentencia.append("\nWHERE  p.ejercicio ="+ getEjercicio());
      sentencia.append("\nand p.mes ="+mes);
      sentencia.append("\nand p.corte = (   ");
      sentencia.append("\nselect max(corte)  ");
      sentencia.append("\nfrom rf_tv_control_meses@dblsia_sic   ");
      sentencia.append("\nwhere ejercicio="+getEjercicio()+" and mes="+mes+" and cierre="+tipoCierre+")");
      if(getPrograma().contains("0008"))
        sentencia.append("   and (cpg.clave_programatica like '01P02280401%' or cpg.clave_programatica like '01P02280501%' or cpg.clave_programatica like '01P02280601%') ");
      if(programa.contains("0001"))
        sentencia.append("   and not (cpg.clave_programatica like '01P02280401%' or cpg.clave_programatica like '01P02280501%' or cpg.clave_programatica like '01P02280601%') ");
      sentencia.append("\n GROUP BY substr(pp.partida,0,1)||'000', p.mes, lpad(ruc.unidad_ejecutora,4,'0'),  ");
      sentencia.append("\nlpad(decode(ruc.unidad_ejecutora,'100','1',ruc.estado)||ruc.ambito,4,'0') ");
      sentencia.append("\nunion all");
      sentencia.append("\nSELECT  ");
      sentencia.append("\n0 pre_aut_ori_anu,");
      sentencia.append("\n0 Amp,");
      sentencia.append("\n0 Red,");
      sentencia.append("\nsum(decode(id_tipo_presup,4, enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + diciembre + 0,0)) as pre_com_acum, ");
      sentencia.append("\n0 pre_ejer_acum,");
      sentencia.append("\n0 GXCac,");
      sentencia.append("\nsubstr(pp.partida,0,1)||'000' as criterio, lpad(ruc.unidad_ejecutora,4,'0') as unidad,  ");
      sentencia.append("\nlpad(decode(ruc.unidad_ejecutora,'100','1',ruc.estado)||ruc.ambito,4,'0') estado");
      sentencia.append("\nFROM ");
      sentencia.append("\nrf_tv_cierre_presupuesto@dblsia_sic p   ");
      sentencia.append("\nleft join rf_tc_claves_presupuestarias@dblsia_sic cp on p.clave_presupuestaria_id = cp.clave_presupuestaria_id   ");
      sentencia.append("\ninner join rf_tr_rel_uea_cveprog@dblsia_sic ruc on cp.id_rel_uea_cve_prog = ruc.id_rel_uea_cve_prog  ");
      sentencia.append("\ninner join rf_tr_claves_programaticas@dblsia_sic cpg on ruc.id_cve_prog=cpg.id_cve_prog and cpg.eliminado = 0   ");
      sentencia.append("\ninner join rf_tc_partidas_presupuestales@dblsia_sic pp on cp.id_partida=pp.id_partida   ");
      sentencia.append("\nWHERE  p.ejercicio ="+ getEjercicio());
      sentencia.append("\nand substr(pp.partida,0,1) in ('5','6') ");
      sentencia.append("\nand p.mes ="+mes);
      sentencia.append("\nand p.corte = (   ");
      sentencia.append("\nselect max(corte)  ");
      sentencia.append("\nfrom rf_tv_control_meses@dblsia_sic   ");
      sentencia.append("\nwhere ejercicio="+getEjercicio()+" and mes="+mes+" and cierre="+tipoCierre+")");
      if(getPrograma().contains("0008"))
        sentencia.append("   and (cpg.clave_programatica like '01P02280401%' or cpg.clave_programatica like '01P02280501%' or cpg.clave_programatica like '01P02280601%') ");
      if(programa.contains("0001"))
        sentencia.append("   and not (cpg.clave_programatica like '01P02280401%' or cpg.clave_programatica like '01P02280501%' or cpg.clave_programatica like '01P02280601%') ");
      sentencia.append("\nGROUP BY  ");
      sentencia.append("\nsubstr(pp.partida,0,1)||'000', p.mes, lpad(ruc.unidad_ejecutora,4,'0'),  ");
      sentencia.append("\nlpad(decode(ruc.unidad_ejecutora,'100','1',ruc.estado)||ruc.ambito,4,'0')");
      sentencia.append("\nunion all");
      sentencia.append("\nselect");
      sentencia.append("\n0 pre_aut_ori_anu,");
      sentencia.append("\n0 Amp,");
      sentencia.append("\n0 Red,");
      sentencia.append("\n0 pre_com_acum,");
      sentencia.append("\n0 pre_ejer_acum,   ");
      sentencia.append("\n(sum( (ministrado1 - comprobado1) + (ministrado2 - comprobado2) + (ministrado3 - comprobado3) + (ministrado4 - comprobado4) + (ministrado5 - comprobado5) + (ministrado6 - comprobado6) + (ministrado7 - comprobado7) + (ministrado8 - comprobado8) + (ministrado9 - comprobado9) + (ministrado10 - comprobado10) + (ministrado11 - comprobado11) + (ministrado12 - comprobado12) )) as GXCac, ");
      sentencia.append("\ncase when to_number(substr(pp.partida,0,1))<= 1 then '0001' else '0002' end as criterio, lpad(ruc.unidad_ejecutora,4,'0') as unidad,  ");
      sentencia.append("\nlpad(decode(ruc.unidad_ejecutora,'100','1',ruc.estado)||ruc.ambito,4,'0') estado");
      sentencia.append("\nFROM   ");
      sentencia.append("\nrf_tv_cierre_financiero@dblsia_sic cf, rf_tc_claves_presupuestarias@dblsia_sic cp,");
      sentencia.append("\nrf_tc_partidas_presupuestales@dblsia_sic pp, rf_tr_rel_uea_cveprog@dblsia_sic ruc,");
      sentencia.append("\nrf_tr_claves_programaticas@dblsia_sic cpg");
      sentencia.append("\nwhere ");
      sentencia.append("\ncp.clave_presupuestaria_id = cf.clave_presupuestaria_id  ");
      sentencia.append("\nand cp.id_partida=pp.id_partida ");
      sentencia.append("\nand cp.id_rel_uea_cve_prog = ruc.id_rel_uea_cve_prog ");
      sentencia.append("\nand ruc.id_cve_prog=cpg.id_cve_prog and cpg.eliminado = 0 ");
      sentencia.append("\nand cf.ejercicio = "+ getEjercicio());
      sentencia.append("\nand substr(pp.partida,0,1) in ('5','6') ");
      sentencia.append("\nand cf.mes ="+mes);
      sentencia.append("\nand cf.corte = (");  
      sentencia.append("\nselect max(corte)  ");
      sentencia.append("\nfrom rf_tv_control_meses@dblsia_sic ");
      sentencia.append("\nwhere ejercicio = "+getEjercicio()+"  and mes = "+mes+" and cierre="+tipoCierre+") ");
      sentencia.append("\nGROUP BY ");
      sentencia.append("\ncase when to_number(substr(pp.partida,0,1))<= 1 then '0001' else '0002' end, cf.mes, lpad(ruc.unidad_ejecutora,4,'0'),  ");
      sentencia.append("\nlpad(decode(ruc.unidad_ejecutora,'100','1',ruc.estado)||ruc.ambito,4,'0')");
      sentencia.append("\n) resultado ");
      if(!getUnidadEjecutora().equals("____"))
        sentencia.append("where unidad='"+getUnidadEjecutora()+"' \n");
      if(!getAmbito().equals("____"))
        sentencia.append("and estado='"+getAmbito()+"' \n");
      sentencia.append("\ngroup by criterio, unidad, estado ");
      sentencia.append("\n) mipf,");
      sentencia.append("\n(select clave,descripcion from rf_tc_capitulos ) des  ");
      sentencia.append("\nwhere des.clave=mipf.criterio");
      sentencia.append("\nand sia.unidad(+)=mipf.unidad and sia.estado(+)=mipf.estado and sia.criterio(+)=mipf.criterio");
      sentencia.append("\norder by mipf.criterio, mipf.unidad, mipf.estado  ");
      //System.out.println(sentencia);
      return sentencia;
    }
    
  public StringBuffer subAmpliacionesReducciones (String mes, String tipoRedondeo, String tipoCierre){
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
    sentencia.append("select decode(mipf.criterio,'0001','Servicios Personales','Gasto Corriente') descripcion, \n");
    sentencia.append("substr(mipf.criterio,4,1) criterio, mipf.unidad, mipf.estado, \n");
    sentencia.append("round(decode(sia.mpea,'',0,sia.mpea),"+tipoRedondeo+") mpea, round((decode(mipf.amp,'',0,mipf.amp) - decode(mipf.red,'',0,mipf.red)),"+tipoRedondeo+") amp_menos_red, round((decode(sia.mpea,'',0,sia.mpea) - (decode(mipf.amp,'',0,mipf.amp) - decode(mipf.red,'',0,mipf.red))),"+tipoRedondeo+") variacion \n");
    sentencia.append("from  \n");
    sentencia.append("(select criterio, unidad, estado, decode(sum(mpea),'',0,sum(mpea)) mpea \n");
    sentencia.append("from (  \n");
    sentencia.append("select saldo_cuenta.*,  \n");
    sentencia.append("case when subStr(cuenta_contable,1,5) ='82301' then saldo  else 0 end mpea  \n");
    sentencia.append("from(  \n");
    sentencia.append("select substr(t.cuenta_contable,f_posicion_nivel('6',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('6',t.cuenta_mayor_id,"+getEjercicio()+")) criterio,  \n");
    sentencia.append("substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+")) unidad,  \n");
    sentencia.append("substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+")) estado, tc.naturaleza,  \n");
    sentencia.append("t.cuenta_contable,  \n");
    sentencia.append("case tc.naturaleza when 'D' then (("+mesCargoAcum+" + "+mesAct+"_cargo) - ("+mesAbonoAcum+" + "+mesAct+"_abono))  \n");
    sentencia.append("else (("+mesAbonoAcum+" + "+mesAct+"_abono) - ("+mesCargoAcum+" + "+mesAct+"_cargo)) end saldo \n");
    sentencia.append("from rf_tr_cuentas_contables t,rf_tc_clasificador_cuentas  tc  \n");
    sentencia.append("where t.cuenta_mayor_id = tc.cuenta_mayor_id  \n");
    sentencia.append("and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" \n");
    sentencia.append("and extract(year from t.fecha_vig_ini)="+getEjercicio()+" \n");
    sentencia.append("and (t.cuenta_contable  like '82301____________0001%' and t.nivel=6 or \n");
    sentencia.append("t.cuenta_contable  like '82301____________0002%' and t.nivel=6 ) \n");
    if(!getPrograma().equals("'____'"))
      sentencia.append("\nand substr(t.cuenta_contable,f_posicion_nivel('programa',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('programa',t.cuenta_mayor_id,"+getEjercicio()+")) in ("+getPrograma()+")");
    if(!getUnidadEjecutora().equals("____"))
      sentencia.append("\nand substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
    if(!getAmbito().equals("____")) 
      sentencia.append("\nand substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());
    sentencia.append(")saldo_cuenta  \n");
    sentencia.append(")x  \n");
    sentencia.append("group by criterio, unidad, estado) sia,  \n");
    sentencia.append("(  \n");
    sentencia.append("SELECT   \n");
    sentencia.append("criterio,  \n");
    sentencia.append("unidad,  \n");
    sentencia.append("estado,  \n");
    sentencia.append("case when criterio in ('0001','0002') then sum(Amp) else 0 end amp,  \n");
    sentencia.append("case when criterio in ('0001','0002') then sum(Red) else 0 end red \n");
    sentencia.append("FROM  \n");
    sentencia.append("(  \n");
    sentencia.append("SELECT  \n");
    sentencia.append("\nsum(decode(id_tipo_presup,8, enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + diciembre + 0,0)) as Amp,  ");
    sentencia.append("\nsum(decode(id_tipo_presup,9, enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + diciembre + 0,0)) as Red,  ");
    sentencia.append("case when to_number(substr(pp.partida,0,1))<= 1 then '0001' else '0002' end as criterio, lpad(ruc.unidad_ejecutora,4,'0') as unidad, \n");
    sentencia.append("lpad(decode(ruc.unidad_ejecutora,'100','1',ruc.estado)||ruc.ambito,4,'0') estado \n");
    sentencia.append("FROM   \n");
    sentencia.append("rf_tv_cierre_presupuesto@dblsia_sic p  \n");
    sentencia.append("left join rf_tc_claves_presupuestarias@dblsia_sic cp on p.clave_presupuestaria_id = cp.clave_presupuestaria_id  \n");
    sentencia.append("inner join rf_tr_rel_uea_cveprog@dblsia_sic ruc on cp.id_rel_uea_cve_prog = ruc.id_rel_uea_cve_prog  \n");
    sentencia.append("inner join rf_tr_claves_programaticas@dblsia_sic cpg on ruc.id_cve_prog=cpg.id_cve_prog and cpg.eliminado = 0  \n");
    sentencia.append("inner join rf_tc_partidas_presupuestales@dblsia_sic pp on cp.id_partida=pp.id_partida  \n");
    sentencia.append("WHERE   \n");
    sentencia.append("p.ejercicio = "+getEjercicio()+" \n");
    sentencia.append("and p.mes = "+mes+" \n");
    sentencia.append("and p.corte = (  \n");
    sentencia.append("select max(corte) \n");
    sentencia.append("from rf_tv_control_meses@dblsia_sic  \n");
    sentencia.append("where  \n");
    sentencia.append("ejercicio="+getEjercicio()+" \n");
    sentencia.append("and mes="+mes+" \n");
    sentencia.append(" and cierre = "+tipoCierre+")  \n");
    if(getPrograma().contains("0008"))
      sentencia.append("   and (cpg.clave_programatica like '01P02280401%' or cpg.clave_programatica like '01P02280501%' or cpg.clave_programatica like '01P02280601%') ");
    if(programa.contains("0001"))
      sentencia.append("   and not (cpg.clave_programatica like '01P02280401%' or cpg.clave_programatica like '01P02280501%' or cpg.clave_programatica like '01P02280601%') ");
    sentencia.append("GROUP BY  \n");
    sentencia.append("case when to_number(substr(pp.partida,0,1))<= 1 then '0001' else '0002' end, p.mes, lpad(ruc.unidad_ejecutora,4,'0'), \n");
    sentencia.append("lpad(decode(ruc.unidad_ejecutora,'100','1',ruc.estado)||ruc.ambito,4,'0') \n");
    sentencia.append(") resultado  \n");
    if(!getUnidadEjecutora().equals("____"))
      sentencia.append("where unidad='"+getUnidadEjecutora()+"' \n");
    if(!getAmbito().equals("____"))
      sentencia.append("and estado='"+getAmbito()+"' \n");
    sentencia.append("group by criterio, unidad, estado \n");
    sentencia.append(") mipf  \n");
    sentencia.append("where sia.unidad(+)=mipf.unidad and sia.estado(+)=mipf.estado and sia.criterio(+)=mipf.criterio   \n");
    sentencia.append("order by mipf.criterio, mipf.unidad, mipf.estado \n");
    return sentencia;
  }
    
  public StringBuffer subAutorizadoOriginalAnual(String mes, String tipoRedondeo, String tipoCierre){
    StringBuffer sentencia = new StringBuffer();
    Fecha fecha = null;
    String mesAct = fecha.getNombreMesCorto(Integer.valueOf(mes)-1);
    String mesAnt = "";
    String mesAbonoAcum = "";
    tipoRedondeo = tipoRedondeo.equals("centavos")?"2":"0";
    if(mesAct.equals("ENE")){
      mesAnt = fecha.getNombreMesCorto(Integer.valueOf(mes)-1).concat("");
      mesAbonoAcum = mesAnt.concat("_abono_ini");
    }
    else{
      mesAnt = fecha.getNombreMesCorto(Integer.valueOf(mes)-2);
      mesAbonoAcum = mesAnt.concat("_abono_acum");
    }
    sentencia.append("select decode(mipf.criterio,'0001','Servicios Personales','Gasto Corriente') descripcion, \n");
    sentencia.append("substr(mipf.criterio,4,1) criterio,mipf.unidad, mipf.estado, \n");
    sentencia.append("round(decode(sia.pea,'',0,sia.pea),"+tipoRedondeo+") pea, round(mipf.paoa,"+tipoRedondeo+") paoa, round((decode(sia.pea,'',0,sia.pea)-mipf.paoa),"+tipoRedondeo+") variacion \n");
    sentencia.append("from  \n");
    sentencia.append("(select criterio, unidad, estado, decode(sum(pea),'',0,sum(pea)) pea \n");
    sentencia.append("from (  \n");
    sentencia.append("select saldo_cuenta.*,  \n");
    sentencia.append("case when subStr(cuenta_contable,1,5) ='82101' then saldo_acum  else 0 end pea  \n");
    sentencia.append("from( \n");
    sentencia.append("select substr(t.cuenta_contable,f_posicion_nivel('6',t.cuenta_mayor_id,2012),f_longitud_nivel('6',t.cuenta_mayor_id,2012)) criterio,  \n");
    sentencia.append("substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,2012),f_longitud_nivel('unidad',t.cuenta_mayor_id,2012)) unidad,  \n");
    sentencia.append("substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,2012),f_longitud_nivel('ambito',t.cuenta_mayor_id,2012)) estado, tc.naturaleza,  \n");
    sentencia.append("t.cuenta_contable,  \n");
    sentencia.append("("+mesAbonoAcum+" + "+mesAct+"_abono) saldo_acum \n");
    sentencia.append("from rf_tr_cuentas_contables t,rf_tc_clasificador_cuentas  tc  \n");
    sentencia.append("where t.cuenta_mayor_id = tc.cuenta_mayor_id  \n");
    sentencia.append("and t.id_catalogo_cuenta= "+getIdCatalogoCuenta()+" \n");
    sentencia.append("and extract(year from t.fecha_vig_ini)="+getEjercicio()+" \n");
    sentencia.append("and (t.cuenta_contable  like '82101____________0001%' and t.nivel=6 or \n");
    sentencia.append("t.cuenta_contable  like '82101____________0002%' and t.nivel=6 ) \n");
    if(!getPrograma().equals("'____'"))
      sentencia.append("\nand substr(t.cuenta_contable,f_posicion_nivel('programa',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('programa',t.cuenta_mayor_id,"+getEjercicio()+")) in ("+getPrograma()+")");
    if(!getUnidadEjecutora().equals("____"))
      sentencia.append("\nand substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
    if(!getAmbito().equals("____")) 
      sentencia.append("\nand substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());
    sentencia.append(")saldo_cuenta  \n");
    sentencia.append(")x  \n");
    sentencia.append("group by criterio, unidad, estado) sia,  \n");
    sentencia.append("(  \n");
    sentencia.append("SELECT criterio, unidad, estado, case when criterio in ('0001','0002') then sum(paoa) else 0 end paoa\n");
    sentencia.append("FROM ( \n");
    sentencia.append("SELECT  \n");
    sentencia.append("sum(decode(id_tipo_presup,1, enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + diciembre + 0,0)) as paoa, \n");
    sentencia.append("case when to_number(substr(pp.partida,0,1))<= 1 then '0001' else '0002' end as criterio,  \n");
    sentencia.append("lpad(ruc.unidad_ejecutora,4,'0') as unidad,  \n");
    sentencia.append("lpad(decode(ruc.unidad_ejecutora,'100','1',ruc.estado)||ruc.ambito,4,'0') estado \n");
    sentencia.append("FROM   \n");
    sentencia.append("rf_tv_cierre_presupuesto@dblsia_sic p  \n");
    sentencia.append("left join rf_tc_claves_presupuestarias@dblsia_sic cp on p.clave_presupuestaria_id = cp.clave_presupuestaria_id  \n");
    sentencia.append("inner join rf_tr_rel_uea_cveprog@dblsia_sic ruc on cp.id_rel_uea_cve_prog = ruc.id_rel_uea_cve_prog  \n");
    sentencia.append("inner join rf_tr_claves_programaticas@dblsia_sic cpg on ruc.id_cve_prog=cpg.id_cve_prog and cpg.eliminado = 0  \n");
    sentencia.append("inner join rf_tc_partidas_presupuestales@dblsia_sic pp on cp.id_partida=pp.id_partida  \n");
    sentencia.append("WHERE p.ejercicio = "+getEjercicio()+" and p.mes = "+mes+" \n");
    sentencia.append("and p.corte = (  \n");
    sentencia.append("select max(corte) \n");
    sentencia.append("from rf_tv_control_meses@dblsia_sic  \n");
    sentencia.append("where ejercicio="+getEjercicio()+" and mes="+mes+" and cierre = "+tipoCierre+" )\n");
    if(getPrograma().contains("0008"))
      sentencia.append("   and (cpg.clave_programatica like '01P02280401%' or cpg.clave_programatica like '01P02280501%' or cpg.clave_programatica like '01P02280601%') ");
    if(programa.contains("0001"))
      sentencia.append("   and not (cpg.clave_programatica like '01P02280401%' or cpg.clave_programatica like '01P02280501%' or cpg.clave_programatica like '01P02280601%') ");
    sentencia.append("GROUP BY  \n");
    sentencia.append("case when to_number(substr(pp.partida,0,1))<= 1 then '0001' else '0002' end, p.mes, lpad(ruc.unidad_ejecutora,4,'0'), \n");
    sentencia.append("lpad(decode(ruc.unidad_ejecutora,'100','1',ruc.estado)||ruc.ambito,4,'0') \n");
    sentencia.append(") resultado  \n");
    if(!getUnidadEjecutora().equals("____"))
      sentencia.append("where unidad='"+getUnidadEjecutora()+"' \n");
    if(!getAmbito().equals("____"))
      sentencia.append("and estado='"+getAmbito()+"' \n");
    sentencia.append("group by criterio, unidad, estado  \n");
    sentencia.append(") mipf  \n");
    sentencia.append("where sia.unidad(+)=mipf.unidad and sia.estado(+)=mipf.estado and sia.criterio(+)=mipf.criterio   \n");
    sentencia.append("order by mipf.criterio, mipf.unidad, mipf.estado \n");
    return sentencia;
  }
  
  public StringBuffer subPresupuestoEjercidoAcumulado(String mes, String cuenta, String tipoRedondeo,String tipoCierre){
    StringBuffer sentencia = new StringBuffer();
    Fecha fecha = null;
    String mesAct = fecha.getNombreMesCorto(Integer.valueOf(mes)-1);
    String mesAnt = "";
    String mesCargoAcum = "";
    tipoRedondeo = tipoRedondeo.equals("centavos")?"2":"0";
    if(mesAct.equals("ENE")){
      mesAnt = fecha.getNombreMesCorto(Integer.valueOf(mes)-1).concat("");
      mesCargoAcum = mesAnt.concat("_cargo_ini");
    }
    else{
      mesAnt = fecha.getNombreMesCorto(Integer.valueOf(mes)-2);
      mesCargoAcum = mesAnt.concat("_cargo_acum");
    }
  
    sentencia.append("select Initcap(mipf.descripcion) descripcion, \n");
    sentencia.append("decode(mipf.criterio,'0001','1','0002','2',mipf.criterio) criterio, mipf.unidad, mipf.estado,  \n");
    sentencia.append("round(decode(sia.presejer,'',0,sia.presejer),"+tipoRedondeo+") presejer, round(mipf.PEAc,"+tipoRedondeo+") PEAc, round((decode(sia.presejer,'',0,sia.presejer) - mipf.PEAc),"+tipoRedondeo+") variacion  \n");
    sentencia.append("from   \n");
    sentencia.append("(select criterio, unidad, estado, decode(sum(presejer),'',0,sum(presejer)) presejer  \n");
    sentencia.append("from (   \n");
    sentencia.append("select saldo_cuenta.*,   \n");
    sentencia.append("case when subStr(cuenta_contable,1,5) ='"+cuenta+"' then saldo_acum  else 0 end presejer   \n");
    sentencia.append("from(   \n");
    sentencia.append("select substr(t.cuenta_contable,f_posicion_nivel('6',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('6',t.cuenta_mayor_id,"+getEjercicio()+")) criterio,   \n");
    sentencia.append("substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+")) unidad,   \n");
    sentencia.append("substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+")) estado, tc.naturaleza,   \n");
    sentencia.append("t.cuenta_contable,   \n");
    sentencia.append("("+mesCargoAcum+" + "+mesAct+"_cargo) saldo_acum \n");
    sentencia.append("from rf_tr_cuentas_contables t,rf_tc_clasificador_cuentas  tc   \n");
    sentencia.append("where t.cuenta_mayor_id = tc.cuenta_mayor_id   \n");
    sentencia.append("and t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" \n");
    sentencia.append("and extract(year from t.fecha_vig_ini)="+getEjercicio()+" \n");
    sentencia.append("and (t.cuenta_contable  like '"+cuenta+"____________1000%' and t.nivel=6 or  \n");
    sentencia.append("t.cuenta_contable  like '"+cuenta+"____________2000%' and t.nivel=6 or \n");
    sentencia.append("t.cuenta_contable  like '"+cuenta+"____________3000%' and t.nivel=6 or  \n");
    sentencia.append("t.cuenta_contable  like '"+cuenta+"____________4000%' and t.nivel=6 or \n");
    sentencia.append("t.cuenta_contable  like '"+cuenta+"____________5000%' and t.nivel=6 or  \n");
    sentencia.append("t.cuenta_contable  like '"+cuenta+"____________6000%' and t.nivel=6 or \n");
    sentencia.append("t.cuenta_contable  like '"+cuenta+"____________7000%' and t.nivel=6)  \n");
    if(!getPrograma().equals("'____'"))
      sentencia.append("\nand substr(t.cuenta_contable,f_posicion_nivel('programa',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('programa',t.cuenta_mayor_id,"+getEjercicio()+")) in ("+getPrograma()+")");
    if(!getUnidadEjecutora().equals("____"))
      sentencia.append("\nand substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
    if(!getAmbito().equals("____")) 
      sentencia.append("\nand substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());
    sentencia.append(")saldo_cuenta )x   \n");
    sentencia.append("group by criterio, unidad, estado) sia,   \n");
    sentencia.append("(SELECT criterio, unidad, estado, descripcion, sum(PEAc) PEAc  \n");
    sentencia.append("FROM  (   \n");
    sentencia.append(" SELECT  \n");
    sentencia.append("sum(decode(id_tipo_presup,5, enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + diciembre + 0,6, enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + diciembre + 0,7, enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + diciembre + 0,0)) as PEAc,  \n");
    sentencia.append("substr(pp.partida,0,1)||'000' as criterio, lpad(ruc.unidad_ejecutora,4,'0') as unidad,  \n");
    sentencia.append("lpad(decode(ruc.unidad_ejecutora,'100','1',ruc.estado)||ruc.ambito,4,'0') estado, des.descripcion \n");
    sentencia.append("FROM   \n");
    sentencia.append("rf_tv_cierre_presupuesto@dblsia_sic p   \n");
    sentencia.append("left join rf_tc_claves_presupuestarias@dblsia_sic cp on p.clave_presupuestaria_id = cp.clave_presupuestaria_id   \n");
    sentencia.append("inner join rf_tr_rel_uea_cveprog@dblsia_sic ruc on cp.id_rel_uea_cve_prog = ruc.id_rel_uea_cve_prog  \n");
    sentencia.append("inner join rf_tr_claves_programaticas@dblsia_sic cpg on ruc.id_cve_prog=cpg.id_cve_prog and cpg.eliminado = 0   \n");
    sentencia.append("inner join rf_tc_partidas_presupuestales@dblsia_sic pp on cp.id_partida=pp.id_partida   \n");
    sentencia.append("inner join (select clave,descripcion from rf_tc_capitulos ) des on des.clave=substr(pp.partida,0,1)||'000' \n");
    sentencia.append("WHERE   \n");
    sentencia.append("p.ejercicio="+getEjercicio()+" \n");
    sentencia.append("and p.mes = "+mes+" \n");
    sentencia.append("and p.corte = (  \n");
    sentencia.append("select max(corte)  \n");
    sentencia.append("from rf_tv_control_meses@dblsia_sic  \n");
    sentencia.append("where ejercicio="+getEjercicio()+" and mes="+mes+" and cierre = "+tipoCierre+") \n");
    if(getPrograma().contains("0008"))
      sentencia.append("   and (cpg.clave_programatica like '01P02280401%' or cpg.clave_programatica like '01P02280501%' or cpg.clave_programatica like '01P02280601%') ");
    if(programa.contains("0001"))
      sentencia.append("   and not (cpg.clave_programatica like '01P02280401%' or cpg.clave_programatica like '01P02280501%' or cpg.clave_programatica like '01P02280601%') ");
    sentencia.append(" GROUP BY substr(pp.partida,0,1)||'000', p.mes, lpad(ruc.unidad_ejecutora,4,'0'),  \n");
    sentencia.append("lpad(decode(ruc.unidad_ejecutora,'100','1',ruc.estado)||ruc.ambito,4,'0'), des.descripcion \n");
    sentencia.append(") resultado   \n");
    if(!getUnidadEjecutora().equals("____"))
      sentencia.append("where unidad='"+getUnidadEjecutora()+"' \n");
    if(!getAmbito().equals("____"))
      sentencia.append("and estado='"+getAmbito()+"' \n");
    sentencia.append("group by criterio, unidad, estado, descripcion \n");
    sentencia.append(") mipf   \n");
    sentencia.append("where sia.unidad(+)=mipf.unidad and sia.estado(+)=mipf.estado and sia.criterio(+)=mipf.criterio    \n");
    sentencia.append("order by mipf.criterio, mipf.unidad, mipf.estado  \n");

  return sentencia;
  }
  
  public StringBuffer subPresupuestoComprometidoAcum(String mes, String tipoRedondeo, String tipoCierre){
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
    sentencia.append("select \n");
    sentencia.append("decode(mipf.criterio,'0001','Servicios Personales','0002','Gasto Corriente','5000','Bienes Muebles, Inmuebles e Intangibles','6000','Inversión Pública','Falta') descripcion,  \n");
    sentencia.append("decode(mipf.criterio,'0001','1','0002','2',mipf.criterio) criterio, mipf.unidad, mipf.estado,  \n");
    sentencia.append("round(decode(sia.pec,'',0,sia.pec),"+tipoRedondeo+") pec, round((mipf.PCAc + mipf.GXCac),"+tipoRedondeo+") PCAc, round((decode(sia.pec,'',0,sia.pec) - (mipf.PCAc + mipf.GXCac)),"+tipoRedondeo+") variacion \n");
    sentencia.append("from   \n");
    sentencia.append("(select criterio, unidad, estado, decode(sum(pec),'',0,sum(pec)) pec  \n");
    sentencia.append("from (select saldo_cuenta.*,   \n");
    sentencia.append("case when subStr(cuenta_contable,1,5) ='82401' then saldo  else 0 end pec   \n");
    sentencia.append("from(   \n");
    sentencia.append("select substr(t.cuenta_contable,f_posicion_nivel('6',t.cuenta_mayor_id,2012),f_longitud_nivel('6',t.cuenta_mayor_id,2012)) criterio,   \n");
    sentencia.append("substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,2012),f_longitud_nivel('unidad',t.cuenta_mayor_id,2012)) unidad,   \n");
    sentencia.append("substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,2012),f_longitud_nivel('ambito',t.cuenta_mayor_id,2012)) estado, tc.naturaleza,   \n");
    sentencia.append("t.cuenta_contable,   \n");
    sentencia.append("case tc.naturaleza when 'D' then (("+mesCargoAcum+" + "+mesAct+"_cargo) - ("+mesAbonoAcum+" + "+mesAct+"_abono))\n");
    sentencia.append("else (("+mesAbonoAcum+" + "+mesAct+"_abono) - ("+mesCargoAcum+" + "+mesAct+"_cargo)) end saldo \n");
    //sentencia.append("("+mesCargoAcum+" + "+mesAct+"_cargo) saldo_acum \n");
    sentencia.append("from rf_tr_cuentas_contables t,rf_tc_clasificador_cuentas  tc   \n");
    sentencia.append("where t.cuenta_mayor_id = tc.cuenta_mayor_id   \n");
    sentencia.append("and t.id_catalogo_cuenta=1  \n");
    sentencia.append("and extract(year from t.fecha_vig_ini)= "+getEjercicio()+"  \n");
    sentencia.append("and (t.cuenta_contable  like '82401____________0001%' and t.nivel=6 or  \n");
    sentencia.append("t.cuenta_contable  like '82401____________0002%' and t.nivel=6 or \n");
    sentencia.append("t.cuenta_contable  like '82401____________5000%' and t.nivel=6 or  \n");
    sentencia.append("t.cuenta_contable  like '82401____________6000%' and t.nivel=6 )  \n");
    if(!getPrograma().equals("'____'"))
      sentencia.append("\nand substr(t.cuenta_contable,f_posicion_nivel('programa',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('programa',t.cuenta_mayor_id,"+getEjercicio()+")) in ("+getPrograma()+")");
    if(!getUnidadEjecutora().equals("____"))
      sentencia.append("\nand substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
    if(!getAmbito().equals("____")) 
      sentencia.append("\nand substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());
    sentencia.append(")saldo_cuenta  )x   \n");
    sentencia.append("group by criterio, unidad, estado) sia,   \n");
    sentencia.append("( SELECT  criterio, unidad,  estado,  sum(PCAc) PCAc, sum(GXCac) GXCac  \n");
    sentencia.append("FROM (   \n");
    sentencia.append("SELECT   \n");
    sentencia.append("\nsum(decode(id_tipo_presup,4, enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + diciembre + 0,0)) as PCAc, ");
    sentencia.append("\n0 GXCac,");
    sentencia.append("    case when to_number(substr(pp.partida,0,1))<= 1 then '0001' else '0002' end as criterio, lpad(ruc.unidad_ejecutora,4,'0') as unidad,  \n");
    sentencia.append("    lpad(decode(ruc.unidad_ejecutora,'100','1',ruc.estado)||ruc.ambito,4,'0') estado  \n");
    sentencia.append("FROM    \n");
    sentencia.append("rf_tv_cierre_presupuesto@dblsia_sic p   \n");
    sentencia.append("left join rf_tc_claves_presupuestarias@dblsia_sic cp on p.clave_presupuestaria_id = cp.clave_presupuestaria_id   \n");
    sentencia.append("inner join rf_tr_rel_uea_cveprog@dblsia_sic ruc on cp.id_rel_uea_cve_prog = ruc.id_rel_uea_cve_prog   \n");
    sentencia.append("inner join rf_tr_claves_programaticas@dblsia_sic cpg on ruc.id_cve_prog=cpg.id_cve_prog and cpg.eliminado = 0   \n");
    sentencia.append("inner join rf_tc_partidas_presupuestales@dblsia_sic pp on cp.id_partida=pp.id_partida   \n");
    sentencia.append("and substr(pp.partida,0,1) not in ('5','6') \n");
    sentencia.append("WHERE  p.ejercicio = "+getEjercicio()+"  \n");
    sentencia.append("and p.mes = "+mes+"  \n");
    sentencia.append("and p.corte = (   \n");
    sentencia.append("select max(corte)  \n");
    sentencia.append("from rf_tv_control_meses@dblsia_sic   \n");
    sentencia.append("where ejercicio="+getEjercicio()+" and mes="+mes+" and cierre = "+tipoCierre+")   \n");
    if(getPrograma().contains("0008"))
      sentencia.append("   and (cpg.clave_programatica like '01P02280401%' or cpg.clave_programatica like '01P02280501%' or cpg.clave_programatica like '01P02280601%') ");
    if(programa.contains("0001"))
    sentencia.append("   and not (cpg.clave_programatica like '01P02280401%' or cpg.clave_programatica like '01P02280501%' or cpg.clave_programatica like '01P02280601%') ");
    sentencia.append("GROUP BY   \n");
    sentencia.append("case when to_number(substr(pp.partida,0,1))<= 1 then '0001' else '0002' end, p.mes, lpad(ruc.unidad_ejecutora,4,'0'),  \n");
    sentencia.append("lpad(decode(ruc.unidad_ejecutora,'100','1',ruc.estado)||ruc.ambito,4,'0')  \n");
    sentencia.append("UNION ALL  \n");
    sentencia.append("\nselect");
    sentencia.append("\n0 PCAc,  ");
    sentencia.append("\n(sum( (ministrado1 - comprobado1) + (ministrado2 - comprobado2) + (ministrado3 - comprobado3) + (ministrado4 - comprobado4) + (ministrado5 - comprobado5) + (ministrado6 - comprobado6) + (ministrado7 - comprobado7) + (ministrado8 - comprobado8) + (ministrado9 - comprobado9) + (ministrado10 - comprobado10) + (ministrado11 - comprobado11) + (ministrado12 - comprobado12) )) as GXCac, ");
    sentencia.append("\ncase when to_number(substr(pp.partida,0,1))<= 1 then '0001' else '0002' end as criterio, lpad(ruc.unidad_ejecutora,4,'0') as unidad,  ");
    sentencia.append("\nlpad(decode(ruc.unidad_ejecutora,'100','1',ruc.estado)||ruc.ambito,4,'0') estado");
    sentencia.append("\nFROM   ");
    sentencia.append("\nrf_tv_cierre_financiero@dblsia_sic cf, rf_tc_claves_presupuestarias@dblsia_sic cp,");
    sentencia.append("\nrf_tc_partidas_presupuestales@dblsia_sic pp, rf_tr_rel_uea_cveprog@dblsia_sic ruc,");
    sentencia.append("\n(select decode(substr(clave_programatica,1,5),'01E02','0002','01E03','0003','01E01','0004',decode(substr(clave_programatica,1,11),'01P02280401','0008','01P02280501','0008','01P02280601','0008','0001')) programa , cp.* ");
    sentencia.append("\nfrom rf_tr_claves_programaticas@dblsia_sic cp) cpg");
    sentencia.append("\nwhere ");
    sentencia.append("\ncp.clave_presupuestaria_id = cf.clave_presupuestaria_id  ");
    sentencia.append("\nand cp.id_partida=pp.id_partida ");
    sentencia.append("\nand cp.id_rel_uea_cve_prog = ruc.id_rel_uea_cve_prog ");
    sentencia.append("\nand ruc.id_cve_prog=cpg.id_cve_prog and cpg.eliminado = 0 ");
    sentencia.append("and substr(pp.partida,0,1) not in ('5','6') \n");
    sentencia.append("and cf.ejercicio = "+getEjercicio()+"  \n");
    sentencia.append("and cf.mes = "+mes+"  \n");
    sentencia.append("and cf.corte = (   \n");
    sentencia.append("select max(corte)  \n");
    sentencia.append("from rf_tv_control_meses@dblsia_sic   \n");
    sentencia.append("where ejercicio="+getEjercicio()+" and mes="+mes+" and cierre = "+tipoCierre+")   \n");
    if(getPrograma().contains("0008"))
      sentencia.append("   and (cpg.clave_programatica like '01P02280401%' or cpg.clave_programatica like '01P02280501%' or cpg.clave_programatica like '01P02280601%') ");
    if(programa.contains("0001"))
      sentencia.append("   and not (cpg.clave_programatica like '01P02280401%' or cpg.clave_programatica like '01P02280501%' or cpg.clave_programatica like '01P02280601%') ");
    sentencia.append("\nGROUP BY ");
    sentencia.append("\ncase when to_number(substr(pp.partida,0,1))<= 1 then '0001' else '0002' end, cf.mes, lpad(ruc.unidad_ejecutora,4,'0'),  ");
    sentencia.append("\nlpad(decode(ruc.unidad_ejecutora,'100','1',ruc.estado)||ruc.ambito,4,'0')");
    sentencia.append("\nUNION ALL ");
    sentencia.append("SELECT  \n");
    sentencia.append("\nsum(decode(id_tipo_presup,4, enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + diciembre + 0,0)) as PCAc, ");
    sentencia.append("\n0 GXCac,");
    sentencia.append("substr(pp.partida,0,1)||'000' as criterio, lpad(ruc.unidad_ejecutora,4,'0') as unidad,  \n");
    sentencia.append("lpad(decode(ruc.unidad_ejecutora,'100','1',ruc.estado)||ruc.ambito,4,'0') estado  \n");
    sentencia.append("FROM   \n");
    sentencia.append("rf_tv_cierre_presupuesto@dblsia_sic p   \n");
    sentencia.append("left join rf_tc_claves_presupuestarias@dblsia_sic cp on p.clave_presupuestaria_id = cp.clave_presupuestaria_id   \n");
    sentencia.append("inner join rf_tr_rel_uea_cveprog@dblsia_sic ruc on cp.id_rel_uea_cve_prog = ruc.id_rel_uea_cve_prog  \n");
    sentencia.append("inner join rf_tr_claves_programaticas@dblsia_sic cpg on ruc.id_cve_prog=cpg.id_cve_prog and cpg.eliminado = 0   \n");
    sentencia.append("inner join rf_tc_partidas_presupuestales@dblsia_sic pp on cp.id_partida=pp.id_partida   \n");
    sentencia.append("WHERE   \n");
    sentencia.append("p.ejercicio="+getEjercicio()+" \n");
    sentencia.append("and substr(pp.partida,0,1) in ('5','6') \n");
    sentencia.append("and p.mes = "+mes+" \n");
    sentencia.append("and p.corte = (  \n");
    sentencia.append("select max(corte)  \n");
    sentencia.append("from rf_tv_control_meses@dblsia_sic  \n");
    sentencia.append("where ejercicio="+getEjercicio()+" and mes="+mes+" and cierre = "+tipoCierre+")  \n");
    if(getPrograma().contains("0008"))
      sentencia.append("   and (cpg.clave_programatica like '01P02280401%' or cpg.clave_programatica like '01P02280501%' or cpg.clave_programatica like '01P02280601%') ");
    if(programa.contains("0001"))
      sentencia.append("   and not (cpg.clave_programatica like '01P02280401%' or cpg.clave_programatica like '01P02280501%' or cpg.clave_programatica like '01P02280601%') ");
    sentencia.append("GROUP BY  \n");
    sentencia.append("substr(pp.partida,0,1)||'000', p.mes, lpad(ruc.unidad_ejecutora,4,'0'),  \n");
    sentencia.append("lpad(decode(ruc.unidad_ejecutora,'100','1',ruc.estado)||ruc.ambito,4,'0')  \n");
    sentencia.append("\nUNION ALL");
    sentencia.append("\nselect");
    sentencia.append("\n0 PCAc,  ");
    sentencia.append("\n(sum( (ministrado1 - comprobado1) + (ministrado2 - comprobado2) + (ministrado3 - comprobado3) + (ministrado4 - comprobado4) + (ministrado5 - comprobado5) + (ministrado6 - comprobado6) + (ministrado7 - comprobado7) + (ministrado8 - comprobado8) + (ministrado9 - comprobado9) + (ministrado10 - comprobado10) + (ministrado11 - comprobado11) + (ministrado12 - comprobado12) )) as GXCac, ");
    sentencia.append("\nsubstr(pp.partida,0,1)||'000' as criterio, lpad(ruc.unidad_ejecutora,4,'0') as unidad,  ");
    sentencia.append("\nlpad(decode(ruc.unidad_ejecutora,'100','1',ruc.estado)||ruc.ambito,4,'0') estado");
    sentencia.append("\nFROM   ");
    sentencia.append("\nrf_tv_cierre_financiero@dblsia_sic cf, rf_tc_claves_presupuestarias@dblsia_sic cp,");
    sentencia.append("\nrf_tc_partidas_presupuestales@dblsia_sic pp, rf_tr_rel_uea_cveprog@dblsia_sic ruc,");
    sentencia.append("\n(select decode(substr(clave_programatica,1,5),'01E02','0002','01E03','0003','01E01','0004',decode(substr(clave_programatica,1,11),'01P02280401','0008','01P02280501','0008','01P02280601','0008','0001')) programa , cp.* ");
    sentencia.append("\nfrom rf_tr_claves_programaticas@dblsia_sic cp) cpg");
    sentencia.append("\nwhere ");
    sentencia.append("\ncp.clave_presupuestaria_id = cf.clave_presupuestaria_id  ");
    sentencia.append("\nand cp.id_partida=pp.id_partida ");
    sentencia.append("\nand cp.id_rel_uea_cve_prog = ruc.id_rel_uea_cve_prog ");
    sentencia.append("\nand ruc.id_cve_prog=cpg.id_cve_prog and cpg.eliminado = 0 ");
    sentencia.append("and cf.ejercicio = "+getEjercicio()+"  \n");
    sentencia.append("and substr(pp.partida,0,1) in ('5','6') \n");
    sentencia.append("and cf.mes = "+mes+"  \n");
    sentencia.append("and cf.corte = (   \n");
    sentencia.append("select max(corte)  \n");
    sentencia.append("from rf_tv_control_meses@dblsia_sic   \n");
    sentencia.append("where ejercicio="+getEjercicio()+" and mes="+mes+" and cierre = "+tipoCierre+")   \n");
    if(getPrograma().contains("0008"))
      sentencia.append("   and (cpg.clave_programatica like '01P02280401%' or cpg.clave_programatica like '01P02280501%' or cpg.clave_programatica like '01P02280601%') ");
    if(programa.contains("0001"))
      sentencia.append("   and not (cpg.clave_programatica like '01P02280401%' or cpg.clave_programatica like '01P02280501%' or cpg.clave_programatica like '01P02280601%') ");
     sentencia.append("\nGROUP BY ");
    sentencia.append("\nsubstr(pp.partida,0,1)||'000', cf.mes, lpad(ruc.unidad_ejecutora,4,'0'),  ");
    sentencia.append("\nlpad(decode(ruc.unidad_ejecutora,'100','1',ruc.estado)||ruc.ambito,4,'0')");
    sentencia.append(") resultado   \n");
    if(!getUnidadEjecutora().equals("____"))
      sentencia.append("where unidad='"+getUnidadEjecutora()+"' \n");
    if(!getAmbito().equals("____"))
      sentencia.append("and estado='"+getAmbito()+"' \n");
    sentencia.append("group by criterio, unidad, estado  \n");
    sentencia.append(") mipf   \n");
    sentencia.append("where sia.unidad(+)=mipf.unidad and sia.estado(+)=mipf.estado and sia.criterio(+)=mipf.criterio    \n");
    sentencia.append("order by mipf.criterio, mipf.unidad, mipf.estado  \n");
    //System.out.println(sentencia);
    return sentencia;
  } 
  
  public StringBuffer contabilidadMipfCostos(String mes, String tipoRedondeo, String tipoCierre){
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
    

    sentencia.append("\nselect mipf.criterio, des.descripcion, mipf.unidad, mipf.estado, ");
    sentencia.append("\nround(decode(sia.saldo,'',0,sia.saldo),"+tipoRedondeo+") saldo_contabilidad, ");
    sentencia.append("\nround(decode(mipf.PEAc,'',0,mipf.PEAc),"+tipoRedondeo+") saldo_presupuestal, ");
    sentencia.append("\nround((decode(sia.saldo,'',0,sia.saldo) - decode(mipf.PEAc,'',0,mipf.PEAc)),"+tipoRedondeo+") saldo_variacion ");
    sentencia.append("\nfrom ");
    sentencia.append("\n(select criterio, unidad, estado, sum(saldo) saldo ");
    sentencia.append("\nfrom(");

    sentencia.append("\nselect ");
    sentencia.append("\npg.partida_presupuestal criterio, ");
    sentencia.append("\nsubstr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+")) unidad, ");
    sentencia.append("\nsubstr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+")) estado, tc.naturaleza, ");
    sentencia.append("\nt.cuenta_contable, ");
    sentencia.append("\ncase tc.naturaleza when 'D' then (("+mesCargoAcum+" + "+mesAct+"_cargo) - ("+mesAbonoAcum+" + "+mesAct+"_abono)) ");
    sentencia.append("\nelse (("+mesAbonoAcum+" + "+mesAct+"_abono) - ("+mesCargoAcum+" + "+mesAct+"_cargo)) end saldo ");
    sentencia.append("\nfrom rf_tr_cuentas_contables t,rf_tc_clasificador_cuentas  tc, rf_tc_partidas_genericas pg ");
    sentencia.append("\nwhere t.cuenta_mayor_id = tc.cuenta_mayor_id ");
    sentencia.append("\nand pg.subcuenta_contable=substr(t.cuenta_contable,f_posicion_nivel('1',t.cuenta_mayor_id,2012),f_longitud_nivel('1',t.cuenta_mayor_id,2012))||substr(t.cuenta_contable,f_posicion_nivel('2',t.cuenta_mayor_id,2012),f_longitud_nivel('2',t.cuenta_mayor_id,2012)) ");
    sentencia.append("\nand t.id_catalogo_cuenta=1 ");
    sentencia.append("\nand extract(year from t.fecha_vig_ini)="+getEjercicio());
    sentencia.append("\nand substr(t.cuenta_contable,f_posicion_nivel('1',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('1',t.cuenta_mayor_id,"+getEjercicio()+"))||substr(t.cuenta_contable,f_posicion_nivel('2',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('2',t.cuenta_mayor_id,"+getEjercicio()+")) in (select p.subcuenta_contable from rf_tc_partidas_genericas p) ");
    sentencia.append("\nand substr(t.cuenta_contable,f_posicion_nivel('6',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('6',t.cuenta_mayor_id,"+getEjercicio()+"))='0001' ");
    if(!getPrograma().equals("'____'"))
      sentencia.append("\nand substr(t.cuenta_contable,f_posicion_nivel('programa',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('programa',t.cuenta_mayor_id,"+getEjercicio()+")) in ("+getPrograma()+") ");
    if(!getUnidadEjecutora().equals("____"))
      sentencia.append("\nand substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"))="+getUnidadEjecutora());
    if(!getAmbito().equals("____")) 
      sentencia.append("\nand substr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"))="+getAmbito());
    //sentencia.append("\norder by substr(t.cuenta_contable,f_posicion_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('unidad',t.cuenta_mayor_id,"+getEjercicio()+")), \n");
    //sentencia.append("\nsubstr(t.cuenta_contable,f_posicion_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+"),f_longitud_nivel('ambito',t.cuenta_mayor_id,"+getEjercicio()+")) \n");
    sentencia.append("\n)x ");
    sentencia.append("\ngroup by criterio, unidad, estado) sia, ");
    sentencia.append("\n(SELECT criterio, unidad, estado, sum(PEAc) PEAc ");
    sentencia.append("\nFROM  ( ");

    sentencia.append("\nSELECT ");
    sentencia.append("\nsum(decode(id_tipo_presup,5, enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + diciembre + 0,6, enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + diciembre + 0,7, enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + diciembre + 0,0)) as PEAc, ");
    sentencia.append("\nsubstr(pp.partida,0,4) as criterio, lpad(ruc.unidad_ejecutora,4,'0') as unidad, ");
    sentencia.append("\nlpad(decode(ruc.unidad_ejecutora,'100','1',ruc.estado)||ruc.ambito,4,'0') estado ");
    sentencia.append("\nFROM ");
    sentencia.append("\nrf_tv_cierre_presupuesto@dblsia_sic p ");
    sentencia.append("\nleft join rf_tc_claves_presupuestarias@dblsia_sic cp on p.clave_presupuestaria_id = cp.clave_presupuestaria_id ");
    sentencia.append("\ninner join rf_tr_rel_uea_cveprog@dblsia_sic ruc on cp.id_rel_uea_cve_prog = ruc.id_rel_uea_cve_prog ");
    sentencia.append("\ninner join rf_tr_claves_programaticas@dblsia_sic cpg on ruc.id_cve_prog=cpg.id_cve_prog and cpg.eliminado = 0 ");
    sentencia.append("\ninner join rf_tc_partidas_presupuestales@dblsia_sic pp on cp.id_partida=pp.id_partida  ");
    sentencia.append("\nWHERE  p.ejercicio ="+getEjercicio());
    sentencia.append("\nand substr(pp.partida,0,4) in (select p.partida_presupuestal from rf_tc_partidas_genericas p) ");
    sentencia.append("\nand p.mes ="+mes);
    sentencia.append("\nand p.corte = ( ");
    sentencia.append("\nselect max(corte) ");
    sentencia.append("\nfrom rf_tv_control_meses@dblsia_sic ");
    sentencia.append("\nwhere ejercicio="+getEjercicio()+" and mes="+mes+" and cierre="+tipoCierre+") ");
    if(getPrograma().contains("0008"))
      sentencia.append("   and (cpg.clave_programatica like '01P02280401%' or cpg.clave_programatica like '01P02280501%' or cpg.clave_programatica like '01P02280601%') ");
    if(programa.contains("0001"))
      sentencia.append("   and not (cpg.clave_programatica like '01P02280401%' or cpg.clave_programatica like '01P02280501%' or cpg.clave_programatica like '01P02280601%') ");
    sentencia.append("\nGROUP BY ");
    sentencia.append("\nsubstr(pp.partida,0,4), p.mes, lpad(ruc.unidad_ejecutora,4,'0'), ");
    sentencia.append("\nlpad(decode(ruc.unidad_ejecutora,'100','1',ruc.estado)||ruc.ambito,4,'0') ");

    sentencia.append("\n) resultado ");
    if(!getUnidadEjecutora().equals("____"))
      sentencia.append("where unidad='"+getUnidadEjecutora()+"' ");
    if(!getAmbito().equals("____"))
      sentencia.append("and estado='"+getAmbito()+"' ");
    sentencia.append("\ngroup by criterio, unidad, estado ");
    sentencia.append("\n) mipf, ");
    sentencia.append("\n(select p.partida_presupuestal, p.descripcion from rf_tc_partidas_genericas p) des ");
    sentencia.append("\nwhere des.partida_presupuestal=mipf.criterio ");
    sentencia.append("\nand sia.unidad(+)=mipf.unidad and sia.estado(+)=mipf.estado and sia.criterio(+)= mipf.criterio ");
    sentencia.append("\norder by mipf.criterio, mipf.unidad, mipf.estado ");

    return sentencia;
  }
    
    public StringBuffer contabilidadTesoreriaCheques(String fechaInicio, String fechaFin, String fechaReporte,String tipoRedondeo){
        StringBuffer sentencia = new StringBuffer();
          Fecha fecha = null;
          String mesAct = fecha.getNombreMesCorto(Integer.valueOf(fechaReporte.substring(3,5))-1);
          String mesAnt = fecha.getNombreMesCorto(Integer.valueOf(fechaReporte.substring(3,5))-2);
          //if(tipoRedondeo.equals("centavos"))tipoRedondeo="2"; else tipoRedondeo="0";
          tipoRedondeo = tipoRedondeo.equals("centavos")?"2":"0";
          
          sentencia.append("select origen.unidad_ejecutora,cat_ue.descripcion,origen.entidad,origen.ambito,origen.num_cuenta, \n");
          sentencia.append("       origen.chequera,origen.nombre_corto,origen.descripcion, \n");
          sentencia.append("       round(origen.saldo_actual,"+tipoRedondeo+") o_saldo_actual,");
          sentencia.append("to_char(origen.fecha_hora,'dd/MM/yyyy') o_fecha_hora, \n");
          sentencia.append("       round(destino.saldo_actual,"+tipoRedondeo+") d_saldo_actual, ");
          sentencia.append("to_char(destino.fecha_hora,'dd/MM/yyyy') d_fecha_hora, \n");
          sentencia.append("       (round(origen.saldo_actual,"+tipoRedondeo+")-round(destino.saldo_actual,"+tipoRedondeo+")) dif_saldos, \n");
          sentencia.append("       to_date(origen.fecha_hora,'dd/MM/yyyy')-(to_date(destino.fecha_hora,'dd/MM/yyyy'))  \n");
          sentencia.append("       dif_fechas, \n");
          sentencia.append("       destino.cuenta_contable \n");
          sentencia.append(" from ( \n");
          sentencia.append("      select lpad(cb.unidad_ejecutora,4,0) unidad_ejecutora,  \n");
          sentencia.append("      lpad(cb.entidad,3,0) entidad, \n");
          sentencia.append("      cb.ambito, cb.id_banco,tc.id_tipo_gasto,cb.num_cuenta,tc.id_tipo_cta, \n");
          sentencia.append("      max(to_date(to_char(m.fecha_hora, 'dd/MM/yyyy'), 'dd/MM/yyyy'))fecha_hora, \n");
          sentencia.append("      cb.alias_chequera1,cb.alias_chequera2,tc.id_tipo_cta,tc.nombre_corto,tc.descripcion, \n");
          sentencia.append("      decode(tc.id_tipo_gasto,1,'2001','2002') chequera \n");
          sentencia.append("      , sum(m.monto) saldo_actual \n");
          sentencia.append("      from rf_tesoreria.RF_TR_CUENTAS_BANCARIAS  cb,  \n");
          sentencia.append("            rf_tesoreria.RF_TR_MOVIMIENTOS_CUENTA m,  \n");
          sentencia.append("            rf_tesoreria.rf_tc_tipo_cuenta tc \n");
          sentencia.append("      where cb.id_cuenta = m.id_cuenta \n");
          sentencia.append("        and cb.id_tipo_cta = tc.id_tipo_cta \n");
          sentencia.append("        and cb.id_tipo_cta in (1,2)  \n");
          sentencia.append("        and tc.id_tipo_gasto in (1,2) \n");
          sentencia.append("        and (to_date(to_char(m.fecha_hora, 'dd/MM/yyyy'), 'dd/MM/yyyy') \n");
        sentencia.append("                                <= to_date('"+fechaFin+"', 'dd/MM/yyyy')) \n");
          /*sentencia.append("                                >= to_date('"+fechaInicio+"', 'dd/MM/yyyy') and \n"); 
          sentencia.append("            to_date(to_char(m.fecha_hora, 'dd/MM/yyyy'), 'dd/MM/yyyy') \n"); 
          sentencia.append("                                <= to_date('"+fechaFin+"', 'dd/MM/yyyy')) \n");*/                  
          sentencia.append("        and cb.id_banco=2 \n");
          if(!getUnidadEjecutora().equals("____"))
            sentencia.append("      and cb.unidad_ejecutora="+getUnidadEjecutora()+" \n");
          if(!getAmbito().equals("____"))
            sentencia.append("      and cb.entidad="+getAmbito().substring(1,3)+" and cb.ambito="+getAmbito().substring(3)+" \n");
          sentencia.append("      group by cb.unidad_ejecutora, cb.entidad,cb.ambito,cb.id_banco,tc.id_tipo_gasto,cb.num_cuenta,tc.id_tipo_cta \n");
          sentencia.append("      , cb.alias_chequera1,cb.alias_chequera2,tc.nombre_corto,tc.descripcion \n");
          sentencia.append("      order by cb.unidad_ejecutora, cb.entidad,cb.ambito,cb.id_banco,tc.id_tipo_gasto,cb.num_cuenta,tc.id_tipo_cta \n");
          sentencia.append(")origen, \n");
          
          sentencia.append("( \n");     
          sentencia.append("      select  d.cuenta_contable_id, cc.cuenta_contable,substr(cc.cuenta_contable,18,4) chequera, \n");
          sentencia.append("              sum(nvl(decode(d.operacion_contable_id,1,d.importe*-1,D.IMPORTE),0))saldo,  \n");
          sentencia.append("              substr(cc.cuenta_contable,10,4) unidad_ejecutora, \n");
          sentencia.append("              substr(cc.cuenta_contable,14,3) entidad, \n");
          sentencia.append("              substr(cc.cuenta_contable,17,1) ambito,    \n ");  
          sentencia.append("              max(to_date(to_char(d.fecha_afectacion, 'dd/MM/yyyy'), 'dd/MM/yyyy')) fecha_hora, \n");
          sentencia.append("              saldos.saldo_actual \n");
                        
          sentencia.append("      from rf_tr_detalle_poliza d, rf_tr_cuentas_contables cc, \n");
          sentencia.append("              (select t.cuenta_contable_id, \n");
          sentencia.append("                  case c.naturaleza when 'D'  \n");
          sentencia.append("                  then ((t."+mesAnt+"_cargo_acum + "+mesAct+"_cargo) - (t."+mesAnt+"_abono_acum +"+mesAct+"_abono))  \n");
          sentencia.append("                  else (("+mesAnt+"_abono_acum + "+mesAct+"_abono) - ("+mesAnt+"_cargo_acum + "+mesAct+"_cargo))  \n");
          sentencia.append("                  end saldo_actual \n");
          sentencia.append("                    from rf_tr_cuentas_contables t, rf_tc_clasificador_cuentas c  \n");
          sentencia.append("                   where t.id_catalogo_cuenta ="+getIdCatalogoCuenta()+" \n");
          sentencia.append("                     and t.cuenta_mayor_id=c.cuenta_mayor_id \n");
          sentencia.append("                     and extract(year from t.fecha_vig_ini) = "+getEjercicio()+" \n");
          sentencia.append("                     and (t.cuenta_contable like '11122____________2002%') \n");
          //sentencia.append("                          t.cuenta_contable like '11122____________2002%') \n");
          sentencia.append("                     and t.nivel = 6)saldos \n");
                               
          sentencia.append("      where d.cuenta_contable_id in  \n");
          sentencia.append("              (select t.cuenta_contable_id \n");
          sentencia.append("               from rf_tr_cuentas_contables t \n"); 
          sentencia.append("               where t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+" \n");
          sentencia.append("                    and (t.cuenta_contable like '11122____________2002%') \n");
          //sentencia.append("                         t.cuenta_contable like '11203____________2002%') \n");
          sentencia.append("                    and t.nivel=6 \n");
          sentencia.append("                    ) \n");
          sentencia.append("          and d.cuenta_contable_id=saldos.cuenta_contable_id \n");
          sentencia.append("          and (to_date(to_char(d.fecha_afectacion, 'dd/MM/yyyy'), 'dd/MM/yyyy') \n");
        sentencia.append("                                <= to_date('"+fechaFin+"', 'dd/MM/yyyy')) \n");
          /*sentencia.append("                                >= to_date('"+fechaInicio+"', 'dd/MM/yyyy') and \n"); 
          sentencia.append("            to_date(to_char(d.fecha_afectacion, 'dd/MM/yyyy'), 'dd/MM/yyyy') \n"); 
          sentencia.append("                                <= to_date('"+fechaFin+"', 'dd/MM/yyyy')) \n");*/ 
          sentencia.append("          and d.cuenta_contable_id=cc.cuenta_contable_id \n");
          //sentencia.append("          --and SUBSTR(cc.cuenta_contable,10,8)='01090011'");
          sentencia.append("      group by d.cuenta_contable_id, cc.cuenta_contable,saldos.saldo_actual \n");
          sentencia.append("      order by substr(cc.cuenta_contable,10,4), \n");
          sentencia.append("              substr(cc.cuenta_contable,14,3), \n");
          sentencia.append("              substr(cc.cuenta_contable,17,1), \n");
          sentencia.append("              substr(cc.cuenta_contable,18,4)  \n");
          sentencia.append(")destino, \n");
          sentencia.append("SIA_CATALOGOS.TC_UNI_EJECUTORAS cat_ue \n");
        
          sentencia.append(" where origen.unidad_ejecutora=destino.unidad_ejecutora and \n");
          sentencia.append("      origen.entidad=destino.entidad(+) and \n");
          sentencia.append("      origen.ambito=destino.ambito (+) and \n");
          sentencia.append("      origen.chequera=destino.chequera(+) and \n"); 
          sentencia.append("      origen.unidad_ejecutora=lpad(cat_ue.unidad_ejecutora,4,0) \n");
          
          sentencia.append("order by origen.unidad_ejecutora,origen.entidad,origen.ambito,origen.num_cuenta \n");
          return sentencia;
      
      
      
      /*StringBuffer sentencia = new StringBuffer();
        Fecha fecha = null;
        String mesAct = fecha.getNombreMesCorto(Integer.valueOf(fechaReporte.substring(3,5))-1);
        String mesAnt = fecha.getNombreMesCorto(Integer.valueOf(fechaReporte.substring(3,5))-2);
        sentencia.append("select origen.unidad_ejecutora,cat_ue.descripcion,origen.entidad,origen.ambito,origen.num_cuenta, \n");
        sentencia.append("       origen.chequera,origen.nombre_corto,origen.descripcion, \n");
        sentencia.append("       origen.saldo_actual o_saldo_actual, origen.fecha_hora o_fecha_hora, \n");
        sentencia.append("       destino.saldo_actual d_saldo_actual,destino.fecha_hora d_fecha_hora, \n");
        sentencia.append("       (origen.saldo_actual-destino.saldo_actual) dif_saldos, \n");
        sentencia.append("       to_date(origen.fecha_hora,'dd/MM/yyyy')-(to_date(destino.fecha_hora,'dd/MM/yyyy'))  \n");
        sentencia.append("       dif_fechas, \n");
        sentencia.append("       destino.cuenta_contable \n");
        sentencia.append(" from ( \n");
        sentencia.append("      select lpad(cb.unidad_ejecutora,4,0) unidad_ejecutora,  \n");
        sentencia.append("      lpad(cb.entidad,3,0) entidad, \n");
        sentencia.append("      cb.ambito, cb.id_banco,tc.id_tipo_gasto,cb.num_cuenta,tc.id_tipo_cta, \n");
        sentencia.append("      max(to_date(to_char(m.fecha_hora, 'dd/MM/yyyy'), 'dd/MM/yyyy'))fecha_hora, \n");
        sentencia.append("      cb.alias_chequera1,cb.alias_chequera2,tc.id_tipo_cta,tc.nombre_corto,tc.descripcion, \n");
        sentencia.append("      decode(tc.id_tipo_gasto,1,'2001','2002') chequera \n");
        sentencia.append("      , sum(m.monto) saldo_actual \n");
        sentencia.append("      from rf_tesoreria.RF_TR_CUENTAS_BANCARIAS  cb,  \n");
        sentencia.append("            rf_tesoreria.RF_TR_MOVIMIENTOS_CUENTA m,  \n");
        sentencia.append("            rf_tesoreria.rf_tc_tipo_cuenta tc \n");
        sentencia.append("      where cb.id_cuenta = m.id_cuenta \n");
        sentencia.append("        and cb.id_tipo_cta = tc.id_tipo_cta \n");
        sentencia.append("        and cb.id_tipo_cta in (1,2)  \n");
        sentencia.append("        and tc.id_tipo_gasto in (1,2) \n");
        sentencia.append("        and (to_date(to_char(m.fecha_hora, 'dd/MM/yyyy'), 'dd/MM/yyyy') <= \n");
               //--     between to_date('01/07/2011', 'dd/MM/yyyy') and ");
        sentencia.append("             to_date('"+fechaReporte+"', 'dd/MM/yyyy')) \n");
        sentencia.append("        and cb.id_banco=2 \n");
        if(!getUnidadEjecutora().equals("____"))
          sentencia.append("      and cb.unidad_ejecutora="+getUnidadEjecutora()+" \n");
        if(!getAmbito().equals("____"))
          sentencia.append("      and cb.entidad="+getAmbito().substring(1,3)+" and cb.ambito="+getAmbito().substring(3)+" \n");
        sentencia.append("      group by cb.unidad_ejecutora, cb.entidad,cb.ambito,cb.id_banco,tc.id_tipo_gasto,cb.num_cuenta,tc.id_tipo_cta \n");
        sentencia.append("      , cb.alias_chequera1,cb.alias_chequera2,tc.nombre_corto,tc.descripcion \n");
        sentencia.append("      order by cb.unidad_ejecutora, cb.entidad,cb.ambito,cb.id_banco,tc.id_tipo_gasto,cb.num_cuenta,tc.id_tipo_cta \n");
        sentencia.append(")origen, \n");
        
        sentencia.append("( \n");     
        sentencia.append("      select  d.cuenta_contable_id, cc.cuenta_contable,substr(cc.cuenta_contable,18,4) chequera, \n");
        sentencia.append("              sum(nvl(decode(d.operacion_contable_id,1,d.importe*-1,D.IMPORTE),0))saldo,  \n");
        sentencia.append("              substr(cc.cuenta_contable,10,4) unidad_ejecutora, \n");
        sentencia.append("              substr(cc.cuenta_contable,14,3) entidad, \n");
        sentencia.append("              substr(cc.cuenta_contable,17,1) ambito,    \n ");  
        sentencia.append("              max(to_date(to_char(d.fecha_afectacion, 'dd/MM/yyyy'), 'dd/MM/yyyy')) fecha_hora, \n");
        sentencia.append("              saldos.saldo_actual \n");
                      
        sentencia.append("      from rf_tr_detalle_poliza d, rf_tr_cuentas_contables cc, \n");
        sentencia.append("              (select t.cuenta_contable_id, \n");
        sentencia.append("                  case c.naturaleza when 'D'  \n");
        sentencia.append("                  then ((t."+mesAnt+"_cargo_acum + "+mesAct+"_cargo) - (t."+mesAnt+"_abono_acum +"+mesAct+"_abono))  \n");
        sentencia.append("                  else (("+mesAnt+"_abono_acum + "+mesAct+"_abono) - ("+mesAnt+"_cargo_acum + "+mesAct+"_cargo))  \n");
        sentencia.append("                  end saldo_actual \n");
        sentencia.append("                    from rf_tr_cuentas_contables t, rf_tc_clasificador_cuentas c  \n");
        sentencia.append("                   where t.id_catalogo_cuenta ="+getIdCatalogoCuenta()+" \n");
        sentencia.append("                     and t.cuenta_mayor_id=c.cuenta_mayor_id \n");
        sentencia.append("                     and extract(year from t.fecha_vig_ini) = "+getEjercicio()+" \n");
        sentencia.append("                     and (t.cuenta_contable like '11203____________200__000%') \n");
         //                    and (t.cuenta_contable like '11203____________2001%' or  \n"); //t.cuenta_contable like '11203____________2001%' or  \n");
        //sentencia.append("                         t.cuenta_contable like '11203____________2002%') \n");
        sentencia.append("                     and t.nivel = 5)saldos \n");
                             
        sentencia.append("      where d.cuenta_contable_id in  \n");
        sentencia.append("              (select t.cuenta_contable_id \n");
        sentencia.append("               from rf_tr_cuentas_contables t \n"); 
        sentencia.append("               where t.id_catalogo_cuenta="+getIdCatalogoCuenta()+" and extract(year from t.fecha_vig_ini)="+getEjercicio()+" \n");
        sentencia.append("                    and (t.cuenta_contable like '11203____________200__000%') \n");
        //sentencia.append("                       or  t.cuenta_contable like '11203____________2002%') \n");
        sentencia.append("                    and t.nivel=5 \n");
        sentencia.append("                    ) \n");
        sentencia.append(" and d.cuenta_contable_id=saldos.cuenta_contable_id \n");
        sentencia.append("          and (to_date(to_char(d.fecha_afectacion, 'dd/MM/yyyy'), 'dd/MM/yyyy') <= \n");
        //sentencia.append("              --BETWEEN to_date('01/07/2011', 'dd/MM/yyyy') and ");
        sentencia.append("              to_date('"+fechaReporte+"', 'dd/MM/yyyy')) \n");
        sentencia.append("          and d.cuenta_contable_id=cc.cuenta_contable_id \n");
        //sentencia.append("          --and SUBSTR(cc.cuenta_contable,10,8)='01090011'");
        sentencia.append("      group by d.cuenta_contable_id, cc.cuenta_contable,saldos.saldo_actual \n");
        sentencia.append("      order by substr(cc.cuenta_contable,10,4), \n");
        sentencia.append("              substr(cc.cuenta_contable,14,3), \n");
        sentencia.append("              substr(cc.cuenta_contable,17,1), \n");
        sentencia.append("              substr(cc.cuenta_contable,18,4)  \n");
        sentencia.append(")destino, \n");
        sentencia.append("SIA_CATALOGOS.TC_UNI_EJECUTORAS cat_ue \n");
     
        sentencia.append(" where origen.unidad_ejecutora=destino.unidad_ejecutora and \n");
        sentencia.append("      origen.entidad=destino.entidad(+) and \n");
        sentencia.append("      origen.ambito=destino.ambito (+) and \n");
        sentencia.append("      origen.chequera=destino.chequera(+) and \n"); 
        sentencia.append("      origen.unidad_ejecutora=lpad(cat_ue.unidad_ejecutora,4,0) \n");
        
        sentencia.append("order by origen.unidad_ejecutora,origen.entidad,origen.ambito,origen.num_cuenta \n");
        return sentencia;*/
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

    public void setPrograma(String programa) {
      this.programa="";
      String prog[]= programa.split(",");
      for(int i=0; i < prog.length; i++){
        this.programa = this.programa+"'"+prog[i]+"',";
      }
      this.programa =  this.programa.substring(0,this.programa.length()-1);
    }

    public String getPrograma() {
        return programa;
    }
    

    public StringBuffer contabilidadTesoreriaConcOrigen(String fechaInicio, String fechaFin, String fechaReporte, String tipoRedondeo ){
      StringBuffer sentencia = new StringBuffer();
        Fecha fecha = null;
        String mesAct = fecha.getNombreMesCorto(Integer.valueOf(fechaReporte.substring(3,5))-1);
        String mesAnt = fecha.getNombreMesCorto(Integer.valueOf(fechaReporte.substring(3,5))-2);
        tipoRedondeo = tipoRedondeo.equals("centavos")?"2":"0";
        sentencia.append("select cb.num_cuenta, "); 
        sentencia.append("      to_char(m.fecha_hora,'dd/MM/yyyy') fecha, "); 
        sentencia.append("      m.id_clave_trans||' '||catal2.descripcion descripcion_clave_trans, "); 
        sentencia.append("      case when cb.num_cuenta = '43710149845'  "); 
        sentencia.append("           then round( (nvl(decode(m.id_tipo_trans,'C', m.monto,0),0)*-1),"+tipoRedondeo+")"); 
        sentencia.append("           else round( (nvl(decode(m.id_tipo_trans,'DR',m.monto,0),0)   ),"+tipoRedondeo+")"); 
        sentencia.append("             "); 
        //sentencia.append("      end cargo, "); 
        sentencia.append("      end abono, ");//Eridany solicitaron cambio de nombre de columna
        sentencia.append("      case when cb.num_cuenta = '43710149845'  "); 
        sentencia.append("           then round( (nvl(decode(m.id_tipo_trans,'D',m.monto,0),0)*-1),"+tipoRedondeo+")"); 
        sentencia.append("           else round( (nvl(decode(m.id_tipo_trans,'CR',m.monto,0),0)  ),"+tipoRedondeo+")"); 
        //sentencia.append("      end abono, "); 
        sentencia.append("      end cargo, "); //Eridany solicitaron cambio de nombre de columna
        sentencia.append("      decode(m.referencia,'','',m.referencia) referencia "); 
        sentencia.append("      from rf_tesoreria.RF_TR_CUENTAS_BANCARIAS  cb,  "); 
        sentencia.append("            rf_tesoreria.RF_TR_MOVIMIENTOS_CUENTA m,  "); 
        sentencia.append("            rf_tesoreria.rf_tc_tipo_cuenta tc, "); 
        sentencia.append("            rf_tesoreria.rf_tc_tipo_transaccion catal, "); 
        sentencia.append("            rf_tesoreria.rf_tc_claves_transaccion catal2 "); 
        sentencia.append("      where cb.id_cuenta = m.id_cuenta "); 
        sentencia.append("        and cb.id_tipo_cta = tc.id_tipo_cta "); 
        sentencia.append("        and catal.id_tipo_trans=m.id_tipo_trans "); 
        sentencia.append("      and catal2.id_clave_trans=m.id_clave_trans "); 
        sentencia.append(" "); 
        sentencia.append("        and cb.num_cuenta in ('43710149845','4042547752')   "); 
        sentencia.append("        and ( "); 
        sentencia.append("            to_date(to_char(m.fecha_hora, 'dd/MM/yyyy'), 'dd/MM/yyyy')  "); 
        sentencia.append("                                >= to_date('"+fechaInicio+"', 'dd/MM/yyyy') and  "); 
        sentencia.append("            to_date(to_char(m.fecha_hora, 'dd/MM/yyyy'), 'dd/MM/yyyy') "); 
        sentencia.append("                                <= to_date('"+fechaFin+"', 'dd/MM/yyyy')) "); 
        sentencia.append("        and (cb.id_banco=1 or cb.id_banco=2) "); 
        sentencia.append("        and abs(m.monto)>0 "); 
        sentencia.append("      order by cb.num_cuenta desc,m.fecha_hora, cargo, abono");
        
        /*//ireport
        "select cb.num_cuenta,cb.nombre_cta,m.fecha_hora, "); 
        sentencia.append("  "); 
        sentencia.append(" m.id_clave_trans||' '||catal2.descripcion descripcion_clave_trans, "); 
        sentencia.append("       case when cb.num_cuenta = '43710149845'  "); 
        sentencia.append("then nvl(decode(m.id_tipo_trans,'C',monto,0),0)*-1  "); 
        sentencia.append("else nvl(decode(m.id_tipo_trans,'D',monto,0),0)  "); 
        sentencia.append("end cargo, "); 
        sentencia.append("case when cb.num_cuenta = '43710149845'  "); 
        sentencia.append("     then nvl(decode(m.id_tipo_trans,'D',monto,0),0)*-1  "); 
        sentencia.append("     else nvl(decode(m.id_tipo_trans,'C',monto,0),0)  "); 
        sentencia.append("end abono, "); 
        sentencia.append("decode(m.referencia,'','',m.referencia) referencia, "); 
        sentencia.append(" cb.alias_chequera1,cb.alias_chequera2,tc.nombre_corto, "); 
        sentencia.append(" decode(tc.id_tipo_cta,3,'3','4') tipo_cuenta, "); 
        sentencia.append("case "); 
        sentencia.append("  when   (cb.num_cuenta='43710149845') then '0103' "); 
        sentencia.append("  when   (cb.num_cuenta='4042547752')  then '0003' "); 
        sentencia.append("end ctaCtble, "); 
        sentencia.append("m.id_movimiento, "); 

        sentencia.append("catal.descripcion tipo_trans, "); 
        sentencia.append("decode(m.id_tipo_trans,'C','I',decode(m.id_tipo_trans,'DR','I',decode(m.id_tipo_trans,'D','E',decode(m.id_tipo_trans,'DR','E')) ))tipoOperac, "); 
        sentencia.append("m.monto, "); 
        sentencia.append("m.id_tipo_trans id_tipo_trans, "); 
        sentencia.append("m.id_clave_trans, "); 
        sentencia.append("tc.id_tipo_cta,tc.descripcion descripcion_tipo_cuenta, "); 
        sentencia.append("lpad(cb.unidad_ejecutora,4,0) unidad_ejecutora,  "); 
        sentencia.append("lpad(cb.entidad,3,0) entidad, "); 
        sentencia.append("cb.ambito, cb.id_banco   "); 
        sentencia.append("from rf_tesoreria.RF_TR_CUENTAS_BANCARIAS  cb,  "); 
        sentencia.append("      rf_tesoreria.RF_TR_MOVIMIENTOS_CUENTA m,  "); 
        sentencia.append("      rf_tesoreria.rf_tc_tipo_cuenta tc, "); 
        sentencia.append("      rf_tesoreria.rf_tc_tipo_transaccion catal, "); 
        sentencia.append("      rf_tesoreria.rf_tc_claves_transaccion catal2 "); 
        sentencia.append("where cb.id_cuenta = m.id_cuenta "); 
        sentencia.append("  and cb.id_tipo_cta = tc.id_tipo_cta "); 
        sentencia.append("  and catal.id_tipo_trans=m.id_tipo_trans "); 
        sentencia.append("and catal2.id_clave_trans=m.id_clave_trans "); 

        sentencia.append("  and cb.num_cuenta in ('43710149845','4042547752')   "); 
        sentencia.append("  and ( "); 
        sentencia.append("      to_date(to_char(m.fecha_hora, 'dd/MM/yyyy'), 'dd/MM/yyyy') >= to_date('"+fechaInicio+"', 'dd/MM/yyyy') and  "); 
        sentencia.append("      to_date(to_char(m.fecha_hora, 'dd/MM/yyyy'), 'dd/MM/yyyy') <= to_date('"+fechaFin+"', 'dd/MM/yyyy')) "); 
        sentencia.append("  and (cb.id_banco=1 or cb.id_banco=2) "); 
        sentencia.append("  and abs(m.monto)>0 "); 
        sentencia.append("order by m.fecha_hora, cargo, abono,cb.num_cuenta desc");*/
    return sentencia;
    }
    
public StringBuffer contabilidadTesoreriaConcDestino(String fechaInicio, String fechaFin, String fechaReporte, String tipoRedondeo){
StringBuffer sentencia = new StringBuffer();
        Fecha fecha = null;
        String mesAct = fecha.getNombreMesCorto(Integer.valueOf(fechaReporte.substring(3,5))-1);
        String mesAnt = fecha.getNombreMesCorto(Integer.valueOf(fechaReporte.substring(3,5))-2);  
        tipoRedondeo = tipoRedondeo.equals("centavos")?"2":"0";
        sentencia.append("select cc.cuenta_contable, "); 
        sentencia.append("	  pp.concepto,	 "); 
        sentencia.append("        d.referencia, "); 
        sentencia.append("        to_char(d.fecha_afectacion,'dd/MM/yyyy') fecha_afectacion, "); 
        sentencia.append("        decode(d.operacion_contable_id,'0','I'||lpad(pp.consecutivo,5,0),'E'||lpad(pp.consecutivo,5,0))num_poliza, "); 
        sentencia.append("        round( nvl(decode(d.operacion_contable_id,0,D.IMPORTE,0),0),"+tipoRedondeo+")debe, "); 
        sentencia.append("        round( nvl(decode(d.operacion_contable_id,0,0,D.IMPORTE),0),"+tipoRedondeo+")haber  "); 
        sentencia.append("      from rf_tr_detalle_poliza d, rf_tr_cuentas_contables cc, rf_tr_polizas pp "); 
        sentencia.append("      where d.cuenta_contable_id in  "); 
        sentencia.append("              (select t.cuenta_contable_id "); 
        sentencia.append("               from rf_tr_cuentas_contables t  "); 
        sentencia.append("               where t.id_catalogo_cuenta=1 and extract(year from t.fecha_vig_ini)=2012 "); 
        sentencia.append("                    and (t.cuenta_contable like '111220001010000110103%'"); 
        sentencia.append("                         or  "); 
        sentencia.append("                         t.cuenta_contable like '111220001010000110003%' "); 
        sentencia.append("                        ) "); 
        sentencia.append("                    and t.nivel=6 "); 
        sentencia.append("                    ) "); 
        sentencia.append("          and ( "); 
        sentencia.append("            to_date(to_char(d.fecha_afectacion, 'dd/MM/yyyy'), 'dd/MM/yyyy')  "); 
        sentencia.append("                                >= to_date('"+fechaInicio+"', 'dd/MM/yyyy') and  "); 
        sentencia.append("            to_date(to_char(d.fecha_afectacion, 'dd/MM/yyyy'), 'dd/MM/yyyy') "); 
        sentencia.append("                                <= to_date('"+fechaFin+"', 'dd/MM/yyyy')) "); 
        sentencia.append("          and d.cuenta_contable_id=cc.cuenta_contable_id "); 
        sentencia.append("          and d.poliza_id=pp.poliza_id "); 
        sentencia.append("          and d.referencia like '%TESORERIA ID_MOV:%' "); 
        sentencia.append("      order by  "); 
        sentencia.append("               substr(cc.cuenta_contable,10,4), "); 
        sentencia.append("               substr(cc.cuenta_contable,14,3), "); 
        sentencia.append("               substr(cc.cuenta_contable,17,1), "); 
        sentencia.append("               substr(cc.cuenta_contable,18,4) desc, "); 
        sentencia.append("               d.fecha_afectacion,haber,debe");
        
        /*//ireport
         * sentencia.append("select  d.cuenta_contable_id, cc.cuenta_contable,cc.descripcion, "); 
        sentencia.append("substr(cc.cuenta_contable,18,4) ctaCble, "); 
        sentencia.append("pp.poliza_id, "); 
        sentencia.append("pp.referencia referencia_gral,	 "); 
        sentencia.append("d.referencia, "); 
        sentencia.append("trim(substr(d.referencia,length(d.referencia)-7,length(d.referencia)))id_movimiento, "); 
        sentencia.append("d.fecha_afectacion, "); 
        sentencia.append("d.operacion_contable_id, "); 
        sentencia.append("decode(d.operacion_contable_id,'0','I','E')tipoOoperac, "); 
        sentencia.append("decode(d.operacion_contable_id,'0','I'||lpad(pp.consecutivo,5,0),'E'||lpad(pp.consecutivo,5,0))num_poliza, "); 
        sentencia.append("d.importe, "); 
        sentencia.append("nvl(decode(d.operacion_contable_id,0,D.IMPORTE,0),0)debe, "); 
        sentencia.append("nvl(decode(d.operacion_contable_id,0,0,D.IMPORTE),0)haber,  "); 
        //"              --sum(nvl(decode(d.operacion_contable_id,1,d.importe*-1,D.IMPORTE),0))saldo, "); 
        sentencia.append("substr(cc.cuenta_contable,10,4) unidad_ejecutora, "); 
        sentencia.append("substr(cc.cuenta_contable,14,3) entidad, "); 
        sentencia.append("substr(cc.cuenta_contable,14,3)||substr(cc.cuenta_contable,17,1) ambito,       "); 
        sentencia.append("to_date(to_char(d.fecha_afectacion, 'dd/MM/yyyy'), 'dd/MM/yyyy') fecha_hora "); 
        sentencia.append(" "); 
        sentencia.append("from rf_tr_detalle_poliza d, rf_tr_cuentas_contables cc, rf_tr_polizas pp "); 
        sentencia.append("where d.cuenta_contable_id in  "); 
        sentencia.append("(select t.cuenta_contable_id "); 
        sentencia.append("from rf_tr_cuentas_contables t  "); 
        sentencia.append("where t.id_catalogo_cuenta=1 and extract(year from t.fecha_vig_ini)=2011 "); 
        sentencia.append("      and (t.cuenta_contable like '112030001010000110103%' or  "); 
        sentencia.append("         t.cuenta_contable like '112030001010000110003%' ) "); 
        sentencia.append("   and t.nivel=5 "); 
        sentencia.append("   ) "); 
        sentencia.append("and ( "); 
        sentencia.append(" to_date(to_char(d.fecha_afectacion, 'dd/MM/yyyy'), 'dd/MM/yyyy') >= to_date('"+fechaInicio+"', 'dd/MM/yyyy') and  "); 
        sentencia.append(" to_date(to_char(d.fecha_afectacion, 'dd/MM/yyyy'), 'dd/MM/yyyy')\n  <= to_date('"+fechaFin+"', 'dd/MM/yyyy')) "); 
        sentencia.append("and d.cuenta_contable_id=cc.cuenta_contable_id "); 
        sentencia.append("and d.poliza_id=pp.poliza_id "); 
        sentencia.append("and d.referencia like '%TESORERIA ID_MOV:%' "); 
        sentencia.append("order by d.fecha_afectacion,haber,debe, "); 
        sentencia.append("substr(cc.cuenta_contable,10,4), "); 
        sentencia.append("substr(cc.cuenta_contable,14,3), "); 
        sentencia.append("substr(cc.cuenta_contable,17,1), "); 
        sentencia.append("substr(cc.cuenta_contable,18,4)");*/
        return sentencia;
    }

    public StringBuffer contabilidadTesoreriaConcTotales(String fechaInicio, String fechaFin, String fechaReporte,String tipoRedondeo){
      StringBuffer sentencia = new StringBuffer();
        Fecha fecha = null;
        String mesAct = fecha.getNombreMesCorto(Integer.valueOf(fechaReporte.substring(3,5))-1);
        String mesAnt = fecha.getNombreMesCorto(Integer.valueOf(fechaReporte.substring(3,5))-2);
        tipoRedondeo = tipoRedondeo.equals("centavos")?"2":"0";
        sentencia.append("select to_char(destino.fecha_afectacion, 'dd/MM/yyyy') dFecha_afectacion,");
        sentencia.append("destino.ctaCtble,destino.cuenta_contable,");
        sentencia.append("round( destino.debe,"+tipoRedondeo+")destinoDebe,");
        sentencia.append("round(destino.haber,"+tipoRedondeo+")destinoHaber,");
        sentencia.append("       to_char(destino.fecha_afectacion, 'dd/MM/yyyy') dFecha_afectacion,");
        sentencia.append("origen.ctaCtble,origen.num_cuenta,");
        //Eridany solicitó el cambio de nombre de comunas
        //sentencia.append("round( origen.cargo,"+tipoRedondeo+")origenCargo,");
        //sentencia.append("round(origen.abono,"+tipoRedondeo+")origenAbono,");
         //sentencia.append("round( destino.debe,"+tipoRedondeo+") + round( origen.cargo,"+tipoRedondeo+") diferenciaCargos,");
         //sentencia.append("round(destino.haber,"+tipoRedondeo+") - round(origen.abono,"+tipoRedondeo+") diferenciaAbonos ");
        sentencia.append("round( origen.cargo,"+tipoRedondeo+")origenAbono,");
        sentencia.append("round(origen.abono,"+tipoRedondeo+")origenCargo,");
        //ssentencia.append("           then round( (nvl(decode(m.id_tipo_trans,'D',m.monto,0),0)*-1),"+tipoRedondeo+")"); 
        sentencia.append("round( destino.debe,"+tipoRedondeo+") + round( origen.cargo,"+tipoRedondeo+") diferenciaAbonos,");
        sentencia.append("round(destino.haber,"+tipoRedondeo+") - round(origen.abono,"+tipoRedondeo+") diferenciaCargos ");
        sentencia.append("from ");
        sentencia.append("(select  d.fecha_afectacion,");
        sentencia.append("        d.cuenta_contable_id, ");
        sentencia.append("        cc.cuenta_contable,");
        sentencia.append("        substr(cc.cuenta_contable,18,4) ctaCtble, ");
        sentencia.append("              sum(nvl(decode(d.operacion_contable_id,0,D.IMPORTE,0),0))debe,");
        sentencia.append("              sum(nvl(decode(d.operacion_contable_id,0,0,D.IMPORTE),0))haber ");
           
        sentencia.append("      from rf_tr_detalle_poliza d, rf_tr_cuentas_contables cc, rf_tr_polizas pp");
        sentencia.append("      where d.cuenta_contable_id in ");
        sentencia.append("              (select t.cuenta_contable_id");
        sentencia.append("               from rf_tr_cuentas_contables t"); 
        sentencia.append("               where t.id_catalogo_cuenta=1 and extract(year from t.fecha_vig_ini)=2012");
        sentencia.append("                    and (t.cuenta_contable like '111220001010000110103%' or ");
        sentencia.append("                         t.cuenta_contable like '111220001010000110003%' ");
        sentencia.append("                         )");
        sentencia.append("                    and t.nivel=6 ");
        sentencia.append("                    ) ");
        sentencia.append("          and ( ");
        sentencia.append("            to_date(to_char(d.fecha_afectacion, 'dd/MM/yyyy'), 'dd/MM/yyyy')  ");
        sentencia.append("                                >= to_date('"+fechaInicio+"', 'dd/MM/yyyy') and ");
        sentencia.append("            to_date(to_char(d.fecha_afectacion, 'dd/MM/yyyy'), 'dd/MM/yyyy')  ");
        sentencia.append("                                <= to_date('"+fechaFin+"', 'dd/MM/yyyy')) ");
        sentencia.append("          and d.cuenta_contable_id=cc.cuenta_contable_id ");
        sentencia.append("          and d.poliza_id=pp.poliza_id ");
        sentencia.append("      group by d.fecha_afectacion,d.cuenta_contable_id,cc.cuenta_contable ");
        sentencia.append("      order by d.fecha_afectacion,d.cuenta_contable_id,cc.cuenta_contable ");
        sentencia.append(")destino, ");
        sentencia.append("(select m.fecha_hora,cb.num_cuenta, ");
        sentencia.append("case ");
        sentencia.append("        when   (cb.num_cuenta='43710149845') then '0103' ");
        sentencia.append("        when   (cb.num_cuenta='4042547752')  then '0003' ");
        sentencia.append("      end ctaCtble, ");
        sentencia.append("      sum(case when cb.num_cuenta = '43710149845'  ");
        sentencia.append("          then nvl(decode(m.id_tipo_trans,'C',monto,0),0)*-1  ");
        sentencia.append("          else nvl(decode(m.id_tipo_trans,'DR',monto,0),0)  ");
        sentencia.append("          end ");
        sentencia.append("      )cargo, ");
        sentencia.append("      sum(case when cb.num_cuenta = '43710149845'  ");
        sentencia.append("           then nvl(decode(m.id_tipo_trans,'D',monto,0),0)*-1  ");
        sentencia.append("           else nvl(decode(m.id_tipo_trans,'CR',monto,0),0)  ");
        sentencia.append("           end  ");
        sentencia.append("      )abono ");
        sentencia.append("      from rf_tesoreria.RF_TR_CUENTAS_BANCARIAS  cb,  ");
        sentencia.append("            rf_tesoreria.RF_TR_MOVIMIENTOS_CUENTA m,  ");
        sentencia.append("            rf_tesoreria.rf_tc_tipo_cuenta tc ");
        sentencia.append("      where cb.id_cuenta = m.id_cuenta ");
        sentencia.append("        and cb.id_tipo_cta = tc.id_tipo_cta ");

        sentencia.append("        and cb.num_cuenta in ('43710149845','4042547752')   ");
        sentencia.append("        and ( ");
        sentencia.append("            to_date(to_char(m.fecha_hora, 'dd/MM/yyyy'), 'dd/MM/yyyy')  ");
        sentencia.append("                                >= to_date('"+fechaInicio+"', 'dd/MM/yyyy') and  ");
        sentencia.append("            to_date(to_char(m.fecha_hora, 'dd/MM/yyyy'), 'dd/MM/yyyy') ");
        sentencia.append("                                <= to_date('"+fechaFin+"', 'dd/MM/yyyy')) ");
        sentencia.append("        and (cb.id_banco=1 or cb.id_banco=2) ");
        sentencia.append("        and abs(m.monto)>0 ");
        sentencia.append("group by m.fecha_hora,cb.num_cuenta) ");
        sentencia.append("origen ");
        sentencia.append("where destino.ctaCtble=origen.ctaCtble and  ");
        sentencia.append("      destino.fecha_afectacion=origen.fecha_hora ");
        sentencia.append("order by destino.fecha_afectacion");
        return sentencia;
    }
 
    public StringBuffer contabilidadTesoreriaDispOrigen(String fechaInicio, String fechaFin, String fechaReporte,String tipoRedondeo){
      //mipf
      StringBuffer sentencia = new StringBuffer();
        Fecha fecha = null;
        String mesAct = fecha.getNombreMesCorto(Integer.valueOf(fechaReporte.substring(3,5))-1);
        String mesAnt = fecha.getNombreMesCorto(Integer.valueOf(fechaReporte.substring(3,5))-2);
        tipoRedondeo = tipoRedondeo.equals("centavos")?"2":"0";
        sentencia.append("select   "); 
        sentencia.append("  bc.cuenta,  "); 
        sentencia.append("  estados.entidad||ambitos.ambito||'-'||cxl.consecutivo as num_operacion, "); 
        sentencia.append("  round( decode(cxl.importe_bruto, 0, cxl.importe_ret_ded, (cxl.importe_bruto- cxl.importe_ret_ded)),"+tipoRedondeo+") as importe_neto,  "); 
        sentencia.append("  ref_bancaria,  "); 
        sentencia.append("  to_char(status.fecha,'dd/MM/yyyy') as fecha "); 
        sentencia.append("from  "); 
        sentencia.append("  rf_tr_cxl@dblsia_sic cxl  "); 
        sentencia.append("  inner join rf_tc_beneficiario_cuenta@dblsia_sic bc on cxl.id_cuenta_inegi = bc.id_cuenta_bene  "); 
        sentencia.append("  inner join rf_th_status_doctos@dblsia_sic status on cxl.id_cxl = status.id_documento   "); 
        sentencia.append("    and status.tipo_docto = 'CXL'   "); 
        sentencia.append("    and (status.id_status = 5 or status.id_status = 13)  "); 
        sentencia.append("    and status.fecha= (  "); 
        sentencia.append("      select max(fecha) from rf_th_status_doctos@dblsia_sic   "); 
        sentencia.append("      where id_documento= status.id_documento   "); 
        sentencia.append("        and tipo_docto = 'CXL'   "); 
        sentencia.append("        and (id_status = 5 or id_status = 13)  "); 
        sentencia.append("    )  "); 
        sentencia.append("  inner join rf_tc_unidades_estados@dblsia_sic unient on unient.id_unidad_estado = cxl.id_unidad_estado "); 
        sentencia.append("  inner join rh_tc_uni_ejecutoras@dblsia_sic unidades on unidades.unidad_ejecutora = unient.unidad_ejecutora "); 
        sentencia.append("  inner join rh_tc_entidades@dblsia_sic estados on estados.entidad = unient.estado and estados.pais = 147 "); 
        sentencia.append("  inner join rh_tc_ambitos@dblsia_sic ambitos on ambitos.ambito = unient.ambito "); 
        sentencia.append("WHERE  "); 
        sentencia.append("  cxl.ref_bancaria is not null  "); 
        sentencia.append("  and to_number(to_char(cxl.fecha_pago, 'yyyy')) <= 2012  "); 
        sentencia.append("  and trunc(status.fecha) >= to_date('"+fechaInicio+"', 'dd/MM/yyyy') and  "); 
        sentencia.append("      trunc(status.fecha) <=to_date('"+fechaFin+"', 'dd/MM/yyyy')   "); 
        sentencia.append("  and ( "); 
        sentencia.append("    bc.cuenta='08400029076' "); 
        sentencia.append("    or  "); 
        sentencia.append("    bc.cuenta ='08400029084' "); 
        sentencia.append("  ) "); 
        sentencia.append("order by  "); 
        sentencia.append("  bc.cuenta asc, "); 
        sentencia.append("  fecha,  "); 
        sentencia.append("  importe_neto");
        
        /*//ireport
        sentencia.append("select  "); 
        sentencia.append("  bc.cuenta, "); 
        sentencia.append("  estados.entidad||ambitos.ambito||'-'||cxl.consecutivo as num_operacion,"); 
        sentencia.append("  decode(cxl.importe_bruto, 0, cxl.importe_ret_ded, (cxl.importe_bruto- cxl.importe_ret_ded)) as importe_neto, "); 
        sentencia.append("  ref_bancaria, "); 
        sentencia.append("  trunc(status.fecha) as fecha"); 
        sentencia.append("from "); 
        sentencia.append("  rf_tr_cxl cxl "); 
        sentencia.append("  inner join rf_tc_beneficiario_cuenta bc on cxl.id_cuenta_inegi = bc.id_cuenta_bene "); 
        sentencia.append("  inner join rf_th_status_doctos status on cxl.id_cxl = status.id_documento  "); 
        sentencia.append("    and status.tipo_docto = 'CXL'  "); 
        sentencia.append("    and (status.id_status = 5 or status.id_status = 13) "); 
        sentencia.append("    and status.fecha= ( "); 
        sentencia.append("      select max(fecha) from rf_th_status_doctos  "); 
        sentencia.append("      where id_documento= status.id_documento  "); 
        sentencia.append("        and tipo_docto = 'CXL'  "); 
        sentencia.append("        and (id_status = 5 or id_status = 13) "); 
        sentencia.append("    ) "); 
        sentencia.append("  inner join rf_presupuesto_s2.rf_tc_unidades_estados unient on unient.id_unidad_estado = cxl.id_unidad_estado"); 
        sentencia.append("  inner join rh_tc_uni_ejecutoras unidades on unidades.unidad_ejecutora = unient.unidad_ejecutora"); 
        sentencia.append("  inner join rh_tc_entidades estados on estados.entidad = unient.estado and estados.pais = 147"); 
        sentencia.append("  inner join rh_tc_ambitos ambitos on ambitos.ambito = unient.ambito"); 
        sentencia.append("WHERE "); 
        sentencia.append("  cxl.ref_bancaria is not null "); 
        sentencia.append("  and to_number(to_char(cxl.fecha_pago, 'yyyy')) <= 2011 "); 
        sentencia.append("  and trunc(status.fecha) >= to_date('"+fechaInicio+"', 'dd/MM/yyyy') and "); 
        sentencia.append("      trunc(status.fecha) <=to_date('"+fechaFin+"', 'dd/MM/yyyy')  "); 
        sentencia.append("  and ("); 
        sentencia.append("    bc.cuenta = '08400029076' "); //--dispersora de gasto corriente
        sentencia.append("    or "); 
        sentencia.append("    bc.cuenta = '08400029084'"); //--dispersora de servicios personales"); 
        sentencia.append("  )"); 
        sentencia.append("order by "); 
        sentencia.append("  fecha, "); 
        sentencia.append("  importe_neto");*/
   
    return sentencia;
    }
    public StringBuffer contabilidadTesoreriaDispDestino(String fechaInicio, String fechaFin, String fechaReporte,String tipoRedondeo){
      StringBuffer sentencia = new StringBuffer();
        Fecha fecha = null;
        String mesAct = fecha.getNombreMesCorto(Integer.valueOf(fechaReporte.substring(3,5))-1);
        String mesAnt = fecha.getNombreMesCorto(Integer.valueOf(fechaReporte.substring(3,5))-2);
        tipoRedondeo = tipoRedondeo.equals("centavos")?"2":"0";
        sentencia.append("select  cc.cuenta_contable, ");
        sentencia.append("pp.unidad_ejecutora,"); 
        sentencia.append("pp.ambito,");
        //sentencia.append("	      pp.referencia referencia_gral,	 "); 
        sentencia.append("        d.referencia, "); 
        sentencia.append("        to_char(d.fecha_afectacion, 'dd/MM/yyyy') fecha_afectacion, "); 
        sentencia.append("        decode(d.operacion_contable_id,'0','I'||lpad(pp.consecutivo,5,0),'E'||lpad(pp.consecutivo,5,0))num_poliza, "); 
        sentencia.append("        round( nvl(decode(d.operacion_contable_id,0,D.IMPORTE,0),0),"+tipoRedondeo+")debe, "); 
        sentencia.append("        round( nvl(decode(d.operacion_contable_id,0,0,D.IMPORTE),0),"+tipoRedondeo+")haber  "); 
        sentencia.append("from rf_tr_detalle_poliza d, rf_tr_cuentas_contables cc, rf_tr_polizas pp "); 
        sentencia.append("where d.cuenta_contable_id in  "); 
        sentencia.append("              (select t.cuenta_contable_id "); 
        sentencia.append("               from rf_tr_cuentas_contables t  "); 
        sentencia.append("               where t.id_catalogo_cuenta=1 and extract(year from t.fecha_vig_ini)=2012 "); 
        sentencia.append("                    and (t.cuenta_contable like '111220001010000110101%' or  "); 
        sentencia.append("                         t.cuenta_contable like '111220001010000110102%' ) "); 
        sentencia.append("                    and t.nivel=6 "); 
        sentencia.append("                    ) "); 
        sentencia.append("          and ( "); 
        sentencia.append("            to_date(to_char(d.fecha_afectacion, 'dd/MM/yyyy'), 'dd/MM/yyyy')  "); 
        sentencia.append("                                >= to_date('"+fechaInicio+"', 'dd/MM/yyyy') and  "); 
        sentencia.append("            to_date(to_char(d.fecha_afectacion, 'dd/MM/yyyy'), 'dd/MM/yyyy') "); 
        sentencia.append("                                <= to_date('"+fechaFin+"', 'dd/MM/yyyy')) "); 
        sentencia.append("          and d.cuenta_contable_id=cc.cuenta_contable_id "); 
        sentencia.append("          and d.poliza_id=pp.poliza_id "); 
        sentencia.append(" "); 
        sentencia.append("order by       substr(cc.cuenta_contable,10,4), "); 
        sentencia.append("               substr(cc.cuenta_contable,14,3), "); 
        sentencia.append("               substr(cc.cuenta_contable,17,1), "); 
        sentencia.append("               substr(cc.cuenta_contable,18,4) desc, "); 
        sentencia.append("               d.fecha_afectacion,haber,debe");
        /*//ireport
         * 
         * select  cc.cuenta_contable,"); 
        sentencia.append("	      pp.referencia referencia_gral,	"); 
        sentencia.append("        d.referencia,"); 
        sentencia.append("        d.fecha_afectacion,"); 
        sentencia.append("        decode(d.operacion_contable_id,'0','I'||lpad(pp.consecutivo,5,0),'E'||lpad(pp.consecutivo,5,0))num_poliza,"); 
        sentencia.append("        nvl(decode(d.operacion_contable_id,0,D.IMPORTE,0),0)debe,"); 
        sentencia.append("        nvl(decode(d.operacion_contable_id,0,0,D.IMPORTE),0)haber "); 
        sentencia.append("from rf_tr_detalle_poliza d, rf_tr_cuentas_contables cc, rf_tr_polizas pp"); 
        sentencia.append("where d.cuenta_contable_id in "); 
        sentencia.append("              (select t.cuenta_contable_id"); 
        sentencia.append("               from rf_tr_cuentas_contables t "); 
        sentencia.append("               where t.id_catalogo_cuenta=1 and extract(year from t.fecha_vig_ini)=2011"); 
        sentencia.append("                    and (t.cuenta_contable like '112030001010000110101%' or "); 
        sentencia.append("                         t.cuenta_contable like '112030001010000110102%' )"); 
        sentencia.append("                    and t.nivel=5"); 
        sentencia.append("                    )"); 
        sentencia.append("          and ("); 
        sentencia.append("            to_date(to_char(d.fecha_afectacion, 'dd/MM/yyyy'), 'dd/MM/yyyy') "); 
        sentencia.append("                                >= to_date('"+fechaInicio+"', 'dd/MM/yyyy') and "); 
        sentencia.append("            to_date(to_char(d.fecha_afectacion, 'dd/MM/yyyy'), 'dd/MM/yyyy')"); 
        sentencia.append("                                <= to_date('"+fechaFin+"', 'dd/MM/yyyy'))"); 
        sentencia.append("          and d.cuenta_contable_id=cc.cuenta_contable_id"); 
        sentencia.append("          and d.poliza_id=pp.poliza_id"); 
        sentencia.append(""); 
        sentencia.append("order by d.fecha_afectacion,haber,debe,"); 
        sentencia.append("               substr(cc.cuenta_contable,10,4),"); 
        sentencia.append("               substr(cc.cuenta_contable,14,3),"); 
        sentencia.append("               substr(cc.cuenta_contable,17,1),"); 
        sentencia.append("               substr(cc.cuenta_contable,18,4)")  ;*/
    
    return sentencia;
    }     
    
    public StringBuffer contabilidadTesoreriaDispTotales(String fechaInicio, String fechaFin, String fechaReporte,String tipoRedondeo){
      StringBuffer sentencia = new StringBuffer();
        Fecha fecha = null;
        String mesAct = fecha.getNombreMesCorto(Integer.valueOf(fechaReporte.substring(3,5))-1);
        String mesAnt = fecha.getNombreMesCorto(Integer.valueOf(fechaReporte.substring(3,5))-2);
        tipoRedondeo = tipoRedondeo.equals("centavos")?"2":"0";   
        sentencia.append("select to_char(destino.fecha_afectacion,'dd/MM/yyyy') fecha_afectacion, "); 
        sentencia.append("       destino.ctaCtble,destino.cuenta_contable,");
        sentencia.append(" round(destino.debe,"+tipoRedondeo+")dDebe,");
        sentencia.append(" round(destino.haber,"+tipoRedondeo+")dHaber,"); 
        sentencia.append("       to_char(origen.fecha,'dd/MM/yyyy') fecha, "); 
        sentencia.append("       origen.ctaCtble,origen.num_cuenta,");
        sentencia.append(" round(origen.importe_neto,"+tipoRedondeo+")oImporte_neto, "); 
       //sentencia.append("round( destino.debe,"+tipoRedondeo+") + round( origen.cargo,"+tipoRedondeo+") dif1,");
        sentencia.append(" round( destino.debe,"+tipoRedondeo+") - round(origen.importe_neto,"+tipoRedondeo+") difAbono,");
        sentencia.append(" round( destino.haber,"+tipoRedondeo+")- round(origen.importe_neto,"+tipoRedondeo+") difCargo "); 
        sentencia.append("from "); 
        sentencia.append(" "); 
        sentencia.append("(select to_date(to_char(trunc(d.fecha_afectacion),'dd/MM/yyyy'),'dd/MM/yyyy') fecha_afectacion, "); 
        sentencia.append("        d.cuenta_contable_id,  "); 
        sentencia.append("        cc.cuenta_contable, "); 
        sentencia.append("        substr(cc.cuenta_contable,18,4) ctaCtble,  "); 
        sentencia.append("              sum(nvl(decode(d.operacion_contable_id,0,D.IMPORTE,0),0))debe, "); 
        sentencia.append("              sum(nvl(decode(d.operacion_contable_id,0,0,D.IMPORTE),0))haber  "); 
        sentencia.append("      from rf_tr_detalle_poliza d, rf_tr_cuentas_contables cc, rf_tr_polizas pp "); 
        sentencia.append("      where d.cuenta_contable_id in  "); 
        sentencia.append("              (select t.cuenta_contable_id "); 
        sentencia.append("               from rf_tr_cuentas_contables t  "); 
        sentencia.append("               where t.id_catalogo_cuenta=1 and extract(year from t.fecha_vig_ini)=2012 "); 
        sentencia.append("                    and (t.cuenta_contable like '111220001010000110101%' or  "); 
        sentencia.append("                         t.cuenta_contable like '111220001010000110102%'  "); 
        sentencia.append("                         ) "); 
        sentencia.append("                    and t.nivel=6 "); 
        sentencia.append("                    ) "); 
        sentencia.append("          and ( "); 
        sentencia.append("            to_date(to_char(d.fecha_afectacion, 'dd/MM/yyyy'), 'dd/MM/yyyy')  "); 
        sentencia.append("                                >= to_date('"+fechaInicio+"', 'dd/MM/yyyy') and  "); 
        sentencia.append("            to_date(to_char(d.fecha_afectacion, 'dd/MM/yyyy'), 'dd/MM/yyyy') "); 
        sentencia.append("                                <= to_date('"+fechaFin+"', 'dd/MM/yyyy')) "); 
        sentencia.append("          and d.cuenta_contable_id=cc.cuenta_contable_id "); 
        sentencia.append("          and d.poliza_id=pp.poliza_id "); 
        sentencia.append("      group by d.fecha_afectacion,d.cuenta_contable_id,cc.cuenta_contable "); 
        sentencia.append("      order by d.fecha_afectacion,d.cuenta_contable_id,cc.cuenta_contable "); 
        sentencia.append(")destino, "); 
        sentencia.append("(select "); 
        sentencia.append("  bc.cuenta num_cuenta, to_date(to_char(trunc(status.fecha),'dd/MM/yyyy'),'dd/MM/yyyy') fecha, "); 
        sentencia.append("  case "); 
        sentencia.append("        when   (bc.cuenta='08400029084') then '0101' "); 
        sentencia.append("        when   (bc.cuenta='08400029076') then '0102' "); 
        sentencia.append("      end ctaCtble, "); 
        sentencia.append("  sum(decode(cxl.importe_bruto, 0, cxl.importe_ret_ded, (cxl.importe_bruto- cxl.importe_ret_ded))) as importe_neto "); 
        sentencia.append(" "); 
        sentencia.append("from  "); 
        sentencia.append("  rf_tr_cxl@dblsia_sic cxl,  "); 
        sentencia.append("  rf_tc_beneficiario_cuenta@dblsia_sic bc  , "); 
        sentencia.append("  rf_th_status_doctos@dblsia_sic status  "); 
        sentencia.append(" "); 
        sentencia.append("WHERE  "); 
        sentencia.append("    (bc.cuenta='08400029076' "); 
        sentencia.append("    or  "); 
        sentencia.append("    bc.cuenta ='08400029084' "); 
        sentencia.append("    ) "); 
        sentencia.append(" and "); 
        sentencia.append(" cxl.id_cuenta_inegi = bc.id_cuenta_bene and "); 
        sentencia.append(" cxl.ref_bancaria is not null and  "); 
        sentencia.append("( cxl.id_cxl = status.id_documento  and "); 
        sentencia.append("              status.tipo_docto = 'CXL'  and "); 
        sentencia.append("             (status.id_status = 5 or status.id_status = 13)) "); 
        sentencia.append("  "); 
        sentencia.append("    "); 
        sentencia.append("  and to_number(to_char(cxl.fecha_pago, 'yyyy')) <= 2012  "); 
        sentencia.append("  and (to_date(to_char(trunc(status.fecha),'dd/MM/yyyy'),'dd/MM/yyyy') >= to_date('"+fechaInicio+"', 'dd/MM/yyyy') and  "); 
        sentencia.append("       to_date(to_char(trunc(status.fecha),'dd/MM/yyyy'),'dd/MM/yyyy') <= to_date('"+fechaFin+"', 'dd/MM/yyyy'))  "); 
        sentencia.append("group by bc.cuenta, to_date(to_char(trunc(status.fecha),'dd/MM/yyyy'),'dd/MM/yyyy')    "); 
        sentencia.append("order by  "); 
        sentencia.append("  bc.cuenta asc, "); 
        sentencia.append("  to_date(to_char(trunc(status.fecha),'dd/MM/yyyy'),'dd/MM/yyyy'), "); 
        sentencia.append("  importe_neto "); 
        sentencia.append(") "); 
        sentencia.append("origen "); 
        sentencia.append("where destino.ctaCtble=origen.ctaCtble and  "); 
        sentencia.append("      to_date(to_char(trunc(destino.fecha_afectacion),'dd/MM/yyyy'),'dd/MM/yyyy')=to_date(to_char(trunc(origen.fecha),'dd/MM/yyyy'),'dd/MM/yyyy') "); 
        sentencia.append("order by destino.ctaCtble,destino.fecha_afectacion");
    return sentencia;
    }   
    
  public StringBuffer contabilidadAlmacenes(String fechaReporte, String campos){
    StringBuffer sentencia = new StringBuffer();
    sia.libs.periodo.Fecha fechaPeriodo = new sia.libs.periodo.Fecha(fechaReporte, "/");
    String fechaConprove = sia.libs.formato.Fecha.formatear(2, fechaReporte);
    String strMesActual = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodo.getMes() - 1);
    fechaPeriodo.addMeses(-1);
    String strMesAnterior = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodo.getMes() - 1);
    String mesCargoAcum = "";
    String mesAbonoAcum = "";
    
    
    if(strMesActual.equals("ENE")){      
      mesCargoAcum = "ene_cargo_ini";
      mesAbonoAcum = "ene_abono_ini";
    }
    else{
      //mesAnt = fecha.getNombreMesCorto(Integer.valueOf(mes)-2);
      mesCargoAcum = strMesAnterior.concat("_cargo_acum");
      mesAbonoAcum = strMesAnterior.concat("_abono_acum");
    }    
    if(!getUnidadEjecutora().equals("0100")){
    sentencia.append("select \n");
    /*sentencia.append("select Initcap(sia.desProd) desProd, sia.comer, sia.unidad, sia.entidad, sia.ambito, sia.unidad||sia.entidad||sia.ambito uni_ent_amb,\n");
    sentencia.append("       sia.almacen, sia.prod, conprove.almacen nom_almacen,sia.saldo_actual, conprove.costototal,\n");
    sentencia.append("       (sia.saldo_actual - conprove.costototal) diferencia\n");*/
    sentencia.append(campos).append("\n");
    sentencia.append("from(\n");
    sentencia.append("  select  substr(t.cuenta_contable,5,1) comer, to_number(substr(t.cuenta_contable,10,4)) unidad,to_number(substr(t.cuenta_contable,15,2)) entidad,\n");
    sentencia.append("          to_number(substr(t.cuenta_contable,17,1)) ambito, substr(t.cuenta_contable,18,4) almacen, \n");
    sentencia.append("          to_number(substr(t.cuenta_contable,22,4)) prod, upper(t.descripcion) desProd,\n");
    sentencia.append("          case cla.naturaleza when 'D' then ((").append(mesCargoAcum).append(" + ").append(strMesActual).append("_cargo) - (").append(mesAbonoAcum).append(" + ").append(strMesActual).append("_abono))\n");
    sentencia.append(" else ((").append(mesAbonoAcum).append(" + ").append(strMesActual).append("_abono) - (").append(mesCargoAcum).append(" + ").append(strMesActual).append("_cargo)) end saldo_actual\n");
    sentencia.append("  from rf_tr_cuentas_contables t, rf_tc_clasificador_cuentas cla\n");
    sentencia.append("  where t.cuenta_mayor_id=cla.cuenta_mayor_id \n");
    sentencia.append("  and extract(year from t.fecha_vig_ini)=").append(getEjercicio()).append("\n");
    sentencia.append("  and t.id_catalogo_cuenta=").append(getIdCatalogoCuenta()).append("\n");
    sentencia.append("  and substr(t.cuenta_contable,1,4) in ('1141','1142') \n");
    sentencia.append("  and substr(t.cuenta_contable,10,4) ='").append(getUnidadEjecutora()).append("'").append("\n");
    sentencia.append("  and substr(t.cuenta_contable,14,4) ='").append(getAmbito()).append("'").append("\n") ;
    sentencia.append("  and substr(t.cuenta_contable,22,4) <>'0000' \n");
    sentencia.append("  order by to_number(substr(t.cuenta_contable,10,4)),to_number(substr(t.cuenta_contable,15,2)),to_number(substr(t.cuenta_contable,17,1)),substr(t.cuenta_contable,18,4),\n");
    sentencia.append("  to_number(substr(t.cuenta_contable,22,4)))sia,\n");
    sentencia.append("(select DECODE(v.comer,1,1,0,2) as comercial,c.uniejecutora,c.entidad,c.ambito,c.cvealmcontabilidad,\n");
    sentencia.append("decode(v.comer,1,decode(substr(V.CLASIFICACION,2,1),7,4,substr(V.CLASIFICACION,2,1)),0,decode(substr(V.CLASIFICACION,2,1),1,4,2,5,3,6,7,7,4,1,5,2,6,3)) AS CVETIPPRO,\n");
    sentencia.append("c.nombre as almacen, sub.cvealmacen,sum(nvl(sub.costototal,0)) as costototal  \n");
    sentencia.append("from(  \n");
    sentencia.append("select a.cvealmacen,a.upc,b.cantexist as existencia,b.costoprom,  b.saldototal as costototal  \n");
    sentencia.append("       from (\n");
    sentencia.append("SELECT a.cvealmacen,a.upc, max(a.idtarjeta) as idtarjeta \n");
    sentencia.append("FROM scpv.atitarjetaalm a  \n");
    sentencia.append("WHERE  to_date(to_char(a.fechamov,'dd/mm/yyyy'),'dd/mm/yyyy')<=to_date('").append(fechaConprove).append("','dd/mm/yyyy') \n");
    sentencia.append("GROUP BY a.cvealmacen,a.upc) a, \n");
    sentencia.append("scpv.atitarjetaalm b \n");
    sentencia.append("where a.idtarjeta=b.idtarjeta and not (b.cantexist=0 and b.saldototal<>0)   \n");
    sentencia.append(") sub,scpv.avititulo v, scpv.atcalmacenes c  \n");
    sentencia.append("where sub.upc=v.UPC  and sub.cvealmacen=c.cvealmacen and sub.existencia>=0\n");
    sentencia.append("and c.uniejecutora=").append(getUnidadEjecutora().substring(1)).append("\n");
    sentencia.append("and c.entidad=").append(Integer.valueOf(getAmbito().substring(1,3))).append("\n");
    sentencia.append("and c.ambito=").append(getAmbito().substring(3)).append("\n");
     sentencia.append("  and c.cvealmacen not in ('KA') ");
    sentencia.append("group by c.nombre, sub.cvealmacen,c.uniejecutora,c.entidad,c.ambito,c.cvealmcontabilidad,\n");
    sentencia.append("decode(v.comer,1,decode(substr(V.CLASIFICACION,2,1),7,4,substr(V.CLASIFICACION,2,1)),0,decode(substr(V.CLASIFICACION,2,1),1,4,2,5,3,6,7,7,4,1,5,2,6,3)),DECODE(v.comer,1,1,0,2)\n");
    sentencia.append("order by  sub.cvealmacen, DECODE(v.comer,1,1,0,2), decode(v.comer,1,decode(substr(V.CLASIFICACION,2,1),7,4,substr(V.CLASIFICACION,2,1)),0,decode(substr(V.CLASIFICACION,2,1),1,4,2,5,3,6,7,7,4,1,5,2,6,3)),DECODE(v.comer,1,1,0,2)\n");
    sentencia.append("    )conprove\n");
    sentencia.append("where sia.comer(+)=conprove.comercial \n");
    sentencia.append("and sia.unidad(+)=conprove.uniejecutora \n");
    sentencia.append("and sia.entidad(+)=conprove.entidad and sia.ambito(+)=conprove.ambito\n");
    sentencia.append("and sia.almacen(+)=conprove.cvealmcontabilidad and sia.prod(+)=conprove.CVETIPPRO\n");
    //sentencia.append("and sia.unidad=130 and sia.entidad=9 and sia.ambito=3\n");
    sentencia.append("order by sia.unidad, sia.entidad, sia.ambito, sia.almacen, sia.comer, sia.prod\n");
    }//if(!getUnidadEjecutora().equals("100"))
    else{
        sentencia.append("select primeramay(sia.desProd) desProd, decode(sia.comer, '1','PRODUCTOS COMERCIALIZABLES','PRODUCTOS NO COMERCIALIZABLES') tipo_producto,\n");
        sentencia.append("sia.prod, sia.saldo_actual, conprove.costototal,(sia.saldo_actual - conprove.costototal) diferencia\n");
        sentencia.append("from(\n");
        sentencia.append("select  substr(t.cuenta_contable,5,1) comer,\n");
        sentencia.append("to_number(substr(t.cuenta_contable,22,4)) prod, upper(t.descripcion) desProd,\n");
        sentencia.append("          case cla.naturaleza when 'D' then sum((").append(mesCargoAcum).append(" + ").append(strMesActual).append("_cargo) - (").append(mesAbonoAcum).append(" + ").append(strMesActual).append("_abono))\n");
        sentencia.append(" else sum((").append(mesAbonoAcum).append(" + ").append(strMesActual).append("_abono) - (").append(mesCargoAcum).append(" + ").append(strMesActual).append("_cargo)) end saldo_actual\n");
        sentencia.append("from rf_tr_cuentas_contables t, rf_tc_clasificador_cuentas cla\n");
        sentencia.append("where t.cuenta_mayor_id=cla.cuenta_mayor_id\n"); 
        sentencia.append("and extract(year from t.fecha_vig_ini)=").append(getEjercicio()).append("\n");
        sentencia.append("and t.id_catalogo_cuenta=1\n");
        sentencia.append("and substr(t.cuenta_contable,1,4) in ('1141','1142')\n"); 
        sentencia.append("and substr(t.cuenta_contable,22,4) <>'0000'\n"); 
        sentencia.append("group by  cla.naturaleza,substr(t.cuenta_contable,5,1),to_number(substr(t.cuenta_contable,22,4)), upper(t.descripcion)\n");
        sentencia.append("order by  to_number(substr(t.cuenta_contable,5,1)),to_number(substr(t.cuenta_contable,22,4))\n");
        sentencia.append(")sia,\n");
        sentencia.append("(select DECODE(v.comer,1,1,0,2) as comercial,\n");
        sentencia.append("decode(v.comer,1,decode(substr(V.CLASIFICACION,2,1),7,4,substr(V.CLASIFICACION,2,1)),0,decode(substr(V.CLASIFICACION,2,1),1,4,2,5,3,6,7,7,4,1,5,2,6,3)) AS CVETIPPRO,\n");
        sentencia.append("sum(nvl(sub.costototal,0)) as costototal\n");  
        sentencia.append("from(\n");  
        sentencia.append("select a.cvealmacen,a.upc,b.cantexist as existencia,b.costoprom,  b.saldototal as costototal\n");  
        sentencia.append("from (\n");
        sentencia.append("SELECT a.cvealmacen,a.upc, max(a.idtarjeta) as idtarjeta\n"); 
        sentencia.append("FROM scpv.atitarjetaalm a\n");   
        sentencia.append("WHERE  to_date(to_char(a.fechamov,'dd/mm/yyyy'),'dd/mm/yyyy')<=to_date('").append(fechaConprove).append("','dd/mm/yyyy') \n");
        sentencia.append("GROUP BY a.cvealmacen,a.upc) a,\n");  
        sentencia.append("scpv.atitarjetaalm b\n");  
        sentencia.append("where a.idtarjeta=b.idtarjeta and not (b.cantexist=0 and b.saldototal<>0)\n");    
        sentencia.append(") sub,scpv.avititulo v, scpv.atcalmacenes c\n");   
        sentencia.append("where sub.upc=v.UPC  and sub.cvealmacen=c.cvealmacen and sub.existencia>=0\n"); 
        sentencia.append("and c.cvealmacen not in ('KA')\n");  
        sentencia.append("group by decode(v.comer,1,decode(substr(V.CLASIFICACION,2,1),7,4,substr(V.CLASIFICACION,2,1)),0,decode(substr(V.CLASIFICACION,2,1),1,4,2,5,3,6,7,7,4,1,5,2,6,3)),DECODE(v.comer,1,1,0,2)\n"); 
        sentencia.append("order by   DECODE(v.comer,1,1,0,2), decode(v.comer,1,decode(substr(V.CLASIFICACION,2,1),7,4,substr(V.CLASIFICACION,2,1)),0,decode(substr(V.CLASIFICACION,2,1),1,4,2,5,3,6,7,7,4,1,5,2,6,3))\n"); 
        sentencia.append(" )conprove\n"); 
        sentencia.append("where sia.comer(+)=conprove.comercial\n");  
        sentencia.append("and sia.prod(+)=conprove.CVETIPPRO\n"); 
        sentencia.append("order by  sia.comer, sia.prod\n"); 
    }//ELSE if(!getUnidadEjecutora().equals("100"))
    
    
    return sentencia;
  }   
}
