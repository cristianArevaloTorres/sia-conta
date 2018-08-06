package sia.rf.contabilidad.reportes.estadosFinancieros;

//import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import sia.libs.formato.Formatos;
import sia.libs.periodo.Fecha;
import sia.rf.contabilidad.registroContable.acciones.ControlRegistro;

public class ConsultasEdosFinancieros {

    private String cierre;
    private String programa;
    private String nivel;
    private String unidad;
    private String ambito;
    private ControlRegistro controlReg;
    public static final int CARGO = 0;
    public static final int ABONO = 1;

    public ConsultasEdosFinancieros(String cierre, String programa, String nivel, String unidad, String ambito, HttpServletRequest request) {
        setCierre(cierre);
        setPrograma(programa);
        setNivel(nivel);
        setUnidad(unidad);
        setAmbito(ambito);
        //HttpServletRequest reuqest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        setControlReg((ControlRegistro) request.getSession().getAttribute("controlRegistro"));
    }

    public void setCierre(String cierre) {
        this.cierre = cierre;
    }

    public String getCierre() {
        return cierre;
    }

    public void setPrograma(String programa) {
        this.programa = programa;
        /*String programas[] = programa.split(",");
         for(int i=0; i < programas.length; i++){
         this.programa = this.programa+"'"+programas[i]+"',";
         //programa = programa+"'"+programas[i]+"',";
         }
         this.programa = this.programa.substring(0,getPrograma().length()-1);*/
    }

    public String getPrograma() {
        return programa;
    }

    public StringBuffer balanzaComprobacion(String fecha_consolidacion) {
        Fecha fechaPeriodo = new Fecha(fecha_consolidacion, "/");
        String mes_act = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodo.getMes() - 1);
        fechaPeriodo.addMeses(-1);
        String mes_ant = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodo.getMes() - 1);
        StringBuffer sentencia = new StringBuffer();
        String mesAntCargo;
        String mesAntAbono;
        String mesActCargo;
        String mesActAbono;
        int ejercicio = getControlReg().getEjercicio();
        setNivel(!getPrograma().equals("'0000'") && getUnidad().equals("0000") && getAmbito().equals("0000") ? "3" : getNivel());
        if (mes_ant.equals("DIC")) {
            mesAntCargo = construirMes(mes_act, "cargo_ini");
            mesAntAbono = construirMes(mes_act, "abono_ini");
        } else {
            mesAntCargo = construirMes(mes_ant, "cargo_acum");
            mesAntAbono = construirMes(mes_ant, "abono_acum");
        }
        mesActCargo = construirMes(mes_act, "cargo");
        mesActAbono = construirMes(mes_act, "abono");
        sentencia.append("select ");
        sentencia.append("\na.ctable as cuenta_contable, a.descripcion as concepto, a.anterior_acreedor, ");
        sentencia.append("\na.anterior_deudor, a.abono, a.cargo, a.actual_acreedor, a.actual_deudor ");
        sentencia.append("\nfrom ");
        sentencia.append("\n(select ");
        sentencia.append("\nsubstr(tc.cuenta_contable,f_posicion_nivel('mayor',tc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('mayor',tc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) as ctable, ");
        sentencia.append("\ntc.nivel, des.descripcion, sum(tc.").append(mesActCargo).append(") as cargo, ");
        sentencia.append("\nsum(tc.").append(mesActAbono).append(") as abono, ");
        //sentencia.append("sum(tc.{0}_cargo_acum) as anterior_deudor, sum(tc.{0}_abono_acum) as anterior_acreedor, ");
        sentencia.append("\n(case when cc.naturaleza = 'D' then(sum(tc.").append(mesAntCargo).append(" + tc.").append(mesActCargo).append(") -sum(tc.").append(mesAntAbono).append(" + tc.").append(mesActAbono).append(")) end) actual_deudor,   ");
        sentencia.append("\n(case when cc.naturaleza = 'A' then(sum(tc.").append(mesAntAbono).append(" + tc.").append(mesActAbono).append(") -sum(tc.").append(mesAntCargo).append(" + tc.").append(mesActCargo).append(")) end) actual_acreedor, ");
        sentencia.append("\n(case when cc.naturaleza = 'D' then(sum(tc.").append(mesAntCargo).append(") -sum(tc.").append(mesAntAbono).append(")) end) anterior_deudor, ");
        sentencia.append("\n(case when cc.naturaleza = 'A' then(sum(tc.").append(mesAntAbono).append(") -sum(tc.").append(mesAntCargo).append(")) end) anterior_acreedor, ");
        sentencia.append("\ncc.naturaleza ");
        sentencia.append("\nfrom ");
        sentencia.append("\nrf_tr_cuentas_contables tc, rf_tc_clasificador_cuentas cc, ");
        sentencia.append("\n(select ");
        sentencia.append("\ndescripcion, nivel, cuenta_mayor_id ");
        sentencia.append("\nfrom ");
        sentencia.append("\nrf_tr_cuentas_contables ");
        sentencia.append("\nwhere ");
        sentencia.append("\nnivel = 1 and extract(year from fecha_vig_ini) = ").append(getControlReg().getEjercicio());
        sentencia.append("\nand id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta());

        sentencia.append("\n and  length(cuenta_contable)=(select distinct con.longitud from rf_tc_configura_cuenta con where con.id_catalogo_cuenta=1 and to_char(con.fecha_vig_ini,'yyyy')=");
        sentencia.append(ejercicio).append(")").append(" )des ");

        sentencia.append("\nwhere ");
        sentencia.append("\ntc.nivel = ").append(getNivel());
        sentencia.append("\nand tc.cuenta_mayor_id = cc.cuenta_mayor_id ");
        sentencia.append("\nand tc.cuenta_mayor_id = des.cuenta_mayor_id ");
        //if(!(programas.length == 1 && programas[0].equals("0000"))){
        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\nand substr(tc.cuenta_contable,f_posicion_nivel('mayor',tc.cuenta_mayor_id,");
            sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('mayor',tc.cuenta_mayor_id,");
            sentencia.append(getControlReg().getEjercicio()).append(")) ");
            sentencia.append("\nin (select distinct substr(tc.cuenta_contable,f_posicion_nivel('mayor',tc.cuenta_mayor_id,");
            sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('mayor',tc.cuenta_mayor_id,");
            sentencia.append(getControlReg().getEjercicio()).append(")) ");
            sentencia.append("\nfrom RF_TR_CUENTAS_CONTABLES  where substr(tc.cuenta_contable,f_posicion_nivel('programa',tc.cuenta_mayor_id,");
            sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',tc.cuenta_mayor_id,");
            sentencia.append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(") ");
            sentencia.append("\nand extract(year from tc.fecha_vig_ini) =").append(getControlReg().getEjercicio());
            sentencia.append("\n)");
            if (Integer.valueOf(getNivel()) != 1) {
                sentencia.append("\nand substr(tc.cuenta_contable,f_posicion_nivel('programa',tc.cuenta_mayor_id,");
                sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',tc.cuenta_mayor_id,");
                sentencia.append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
            }
        }
        sentencia.append("\nand substr(tc.cuenta_contable,f_posicion_nivel('unidad',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\nand substr(tc.cuenta_contable,f_posicion_nivel('ambito',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('ambito',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append(")) = '").append(getAmbito()).append("'");
        sentencia.append("\nand extract(year from tc.fecha_vig_ini) = ").append(getControlReg().getEjercicio());
        sentencia.append("\nand id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta());

        sentencia.append("\n and  length(cuenta_contable)=(select distinct con.longitud from rf_tc_configura_cuenta con where con.id_catalogo_cuenta=1 and to_char(con.fecha_vig_ini,'yyyy')=").append(ejercicio).append(")");

        sentencia.append("\ngroup by substr(tc.cuenta_contable,f_posicion_nivel('mayor',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('mayor',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append(")), ");
        sentencia.append("\ntc.nivel, cc.naturaleza, des.descripcion) a ");
        sentencia.append("\nwhere ");
        sentencia.append("\n(a.cargo <> 0 or a.abono <> 0 or a.anterior_acreedor <> 0 or a.anterior_deudor <> 0) ");
        sentencia.append("\norder by a.ctable ");
        //System.out.println("BALANZA DE COMPROBACION"+sentencia.toString());
        return sentencia;
    }

    public StringBuffer analiticoActivo(String fecha_consolidacion) {
        Fecha fechaPeriodo = new Fecha(fecha_consolidacion, "/");
        String mes_act = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodo.getMes() - 1);
        fechaPeriodo.addMeses(-1);
        String mes_ant = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodo.getMes() - 1);
        StringBuffer sentencia = new StringBuffer();

        String mesAntCargo;
        String mesAntAbono;
        String mesActCargo;
        String mesActAbono;

        setNivel(!getPrograma().equals("'0000'") && getUnidad().equals("0000") && getAmbito().equals("0000") ? "3" : getNivel());
        if (mes_ant.equals("DIC")) {
            mesAntCargo = construirMes(mes_act, "cargo_ini");
            mesAntAbono = construirMes(mes_act, "abono_ini");
        } else {
            mesAntCargo = construirMes(mes_ant, "cargo_acum");
            mesAntAbono = construirMes(mes_ant, "abono_acum");
        }
        mesActCargo = construirMes(mes_act, "cargo");
        mesActAbono = construirMes(mes_act, "abono");

        /*query nuevo*/
        sentencia.append("\n SELECT ");
        sentencia.append("\n sub.n1, sub.descripcion, decode(length(sub.n1),3, saldo_anterior,'') as  saldo_anterior, ");
        sentencia.append("\n decode(length(sub.n1),3, cargo_periodo,'') as  cargo_periodo, ");
        sentencia.append("\n  decode(length(sub.n1),3, abono_periodo,'') as  abono_periodo, ");
        sentencia.append("\n   decode(length(sub.n1),3, saldo_actual,'') as  saldo_actual, ");
        sentencia.append("\n    decode(length(sub.n1),3, flujo_periodo,'') as  flujo_periodo ");
        sentencia.append("\n FROM ( ");
        sentencia.append("\n  --nivel 3 ");
        sentencia.append("\n SELECT n1, ");
        sentencia.append("\n   REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(initcap(descripcion), 'De ','de '), 'Por ','por '), 'Y ','y '), 'A ','a '), 'En ','en '), 'Del ','del '), 'Al ','al '), 'Para ','para '), 'O ','o '), 'No ','no ') as descripcion, ");
        sentencia.append("\n   sum(saldo_anterior) as saldo_anterior, ");
        sentencia.append("\n   sum(cargo_periodo) as cargo_periodo, ");
        sentencia.append("\n   sum(abono_periodo) as abono_periodo, ");
        sentencia.append("\n   sum(saldo_actual) as saldo_actual, ");
        sentencia.append("\n   sum(flujo_periodo) as   flujo_periodo ");
        sentencia.append("\n FROM( ");
        sentencia.append("\n SELECT  substr(n1,1,3)  n1, ");
        sentencia.append("\n  ( SELECT descripcion AS descripcion  ");
        sentencia.append("\n      FROM  rf_tc_clase_clasif_cta  ");
        sentencia.append("\n    WHERE   id_genero||id_grupo||id_clase = substr(n1,1,3) ");
        sentencia.append("\n  ) AS  descripcion, ");
        sentencia.append("\n        saldo_anterior, ");
        sentencia.append("\n         cargo_periodo, ");
        sentencia.append("\n        abono_periodo, ");
        sentencia.append("\n          saldo_actual, ");
        sentencia.append("\n        saldo_actual - saldo_anterior AS flujo_periodo  ");
        sentencia.append("\n FROM ");
        sentencia.append("\n  ( ");
        sentencia.append("\n  SELECT SUBSTR(cc.cuenta_contable, 1, 4) AS n1, ");
        sentencia.append("\n CASE MAX(cl.naturaleza) ");
        sentencia.append("\n      WHEN 'D' ");
        sentencia.append("\n      THEN (SUM(").append(mesAntCargo).append(") - SUM(").append(mesAntAbono).append(")) ");
        sentencia.append("\n      ELSE (SUM(").append(mesAntAbono).append(") - SUM(").append(mesAntCargo).append(")) ");
        sentencia.append("\n    END saldo_anterior, ");
        sentencia.append("\n    SUM(cc.").append(mesActCargo).append(") cargo_periodo, ");
        sentencia.append("\n    SUM(cc.").append(mesActAbono).append(") abono_periodo, ");
        sentencia.append("\n    CASE MAX(cl.naturaleza) ");
        sentencia.append("\n      WHEN 'D' ");
        sentencia.append("\n      THEN ((SUM(").append(mesAntCargo).append(" ) + SUM(").append(mesActCargo);
        sentencia.append(") ) - ( SUM(").append(mesAntAbono).append(" ) + SUM(").append(mesActAbono).append("))) ");
        sentencia.append("\n      ELSE ((SUM(").append(mesAntAbono).append(" ) + SUM(").append(mesActAbono);
        sentencia.append(") ) - (SUM(").append(mesAntCargo).append(" ) + SUM(").append(mesActCargo).append("))) ");
        sentencia.append("\n    END saldo_actual ");
        sentencia.append("\n    FROM  rf_tr_cuentas_contables cc, ");
        sentencia.append("\n          rf_tc_clasificador_cuentas cl ");
        sentencia.append("\n    WHERE    cc.cuenta_mayor_id             = cl.cuenta_mayor_id ");
        sentencia.append("\n  AND cc.nivel                          = ").append(getNivel());
        sentencia.append("\n            AND        cc.id_catalogo_cuenta        = ");
        sentencia.append(getControlReg().getIdCatalogoCuenta());
        sentencia.append("\n            AND TO_CHAR(cc.fecha_vig_ini,'yyyy') = ").append(getControlReg().getEjercicio());
        sentencia.append("\n            AND     cc.cuenta_contable LIKE '1_%' ");

        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n and substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n  AND substr(cc.cuenta_contable,f_posicion_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad',cc.cuenta_mayor_id, ");
        sentencia.append(getControlReg().getEjercicio()).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\n  AND substr(cc.cuenta_contable,f_posicion_nivel('ambito',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('ambito',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append(")) = '").append(getAmbito()).append("'");
        sentencia.append("\n  GROUP BY SUBSTR(cc.cuenta_contable, 1, 4)  ");
        sentencia.append("\n ) ");
        sentencia.append("\n ) ");
        sentencia.append("\n GROUP BY  n1,descripcion ");

        sentencia.append("\n --nivel2 ");
        sentencia.append("\n UNION ALL ");
        sentencia.append("\n SELECT distinct n1, ");
        sentencia.append("\n  (SELECT descripcion AS descripcion ");
        sentencia.append("\n    FROM  rf_tc_grupo_clasf_cta ");
        sentencia.append("\n    WHERE   id_genero ");
        sentencia.append("\n            ||id_grupo = n1 ");
        sentencia.append("\n  ) AS  descripcion, ");
        sentencia.append("\n      0  saldo_anterior, ");
        sentencia.append("\n      0  cargo_periodo, ");
        sentencia.append("\n      0  abono_periodo, ");
        sentencia.append("\n      0  saldo_actual, ");
        sentencia.append("\n      0  flujo_periodo ");
        sentencia.append("\n FROM ");
        sentencia.append("\n  (SELECT SUBSTR(cc.cuenta_contable, 1, 2) AS n1 ");
        sentencia.append("\n  FROM  rf_tr_cuentas_contables cc, ");
        sentencia.append("\n     rf_tc_clasificador_cuentas cl ");
        sentencia.append("\n  WHERE cc.cuenta_mayor_id             = cl.cuenta_mayor_id ");
        sentencia.append("\n  AND cc.nivel                          = ").append(getNivel());
        sentencia.append("\n  AND        cc.id_catalogo_cuenta        = ").append(getControlReg().getIdCatalogoCuenta());
        sentencia.append("\n  AND TO_CHAR(cc.fecha_vig_ini,'yyyy') = ").append(getControlReg().getEjercicio());
        sentencia.append("\n  AND cc.cuenta_contable LIKE '1_%' ");

        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n and substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n  AND substr(cc.cuenta_contable,f_posicion_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad',cc.cuenta_mayor_id, ");
        sentencia.append(getControlReg().getEjercicio()).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\n  AND substr(cc.cuenta_contable,f_posicion_nivel('ambito',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('ambito',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append(")) = '").append(getAmbito()).append("'");

        sentencia.append("\n  )  ");
        sentencia.append("\n UNION ALL ");
        sentencia.append("\n --nivel 1 ");
        sentencia.append("\n SELECT distinct n1, ");
        sentencia.append("\n  (SELECT descripcion AS descripcion ");
        sentencia.append("\n  FROM  rf_tc_genero_clasf_cta ");
        sentencia.append("\n  WHERE id_genero = n1 ");
        sentencia.append("\n  ) AS descripcion, ");
        sentencia.append("\n  0 saldo_anterior, ");
        sentencia.append("\n  0 cargo_periodo, ");
        sentencia.append("\n  0 abono_periodo, ");
        sentencia.append("\n  0 saldo_actual, ");
        sentencia.append("\n  0 flujo_periodo ");
        sentencia.append("\n FROM ");
        sentencia.append("\n  (SELECT SUBSTR(cc.cuenta_contable, 1, 1) AS n1 ");
        sentencia.append("\n  FROM  rf_tr_cuentas_contables cc, ");
        sentencia.append("\n     rf_tc_clasificador_cuentas cl ");
        sentencia.append("\n  WHERE cc.cuenta_mayor_id             = cl.cuenta_mayor_id ");
        sentencia.append("\n  AND cc.nivel                          = ").append(getNivel());
        sentencia.append("\n  AND        cc.id_catalogo_cuenta        = ").append(getControlReg().getIdCatalogoCuenta());
        sentencia.append("\n  AND TO_CHAR(cc.fecha_vig_ini,'yyyy') = ").append(getControlReg().getEjercicio());
        sentencia.append("\n  AND cc.cuenta_contable LIKE '1_%' ");

        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n and substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,");
            sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,");
            sentencia.append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n  AND substr(cc.cuenta_contable,f_posicion_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad',cc.cuenta_mayor_id, ");
        sentencia.append(getControlReg().getEjercicio()).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\n  AND substr(cc.cuenta_contable,f_posicion_nivel('ambito',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('ambito',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append(")) = '").append(getAmbito()).append("'");

        sentencia.append("\n  ) ");
        sentencia.append("\n )  sub ");
        sentencia.append("\n ORDER BY sub.n1 ");

        //System.out.println("ANALITICO ACTIVO NUEVO"+sentencia.toString());
        return sentencia;
    }

    public StringBuffer estadoAnaliticoPasivo(String fecha_consolidacion) {
        Fecha fechaPeriodo = new Fecha(fecha_consolidacion, "/");
        String mes_act = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodo.getMes() - 1);
        fechaPeriodo.addMeses(-1);
        String mes_ant = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodo.getMes() - 1);
        StringBuffer sentencia = new StringBuffer();
        String mesAntCargo;
        String mesAntAbono;
        String mesActCargo;
        String mesActAbono;
        setNivel(!getPrograma().equals("'0000'") && getUnidad().equals("0000") && getAmbito().equals("0000") ? "3" : getNivel());
        if (mes_ant.equals("DIC")) {
            mesAntCargo = construirMes(mes_act, "cargo_ini");
            mesAntAbono = construirMes(mes_act, "abono_ini");
        } else {
            mesAntCargo = construirMes(mes_ant, "cargo_acum");
            mesAntAbono = construirMes(mes_ant, "abono_acum");
        }
        mesActCargo = construirMes(mes_act, "cargo");
        mesActAbono = construirMes(mes_act, "abono");

        sentencia.append("select n1, \n");
        sentencia.append("decode(n1, '21', 'PASIVO A CORTO PLAZO', '22', 'PASIVO A LARGO PLAZO', (select initcap(descripcion) from rf_tc_clasificador_cuentas tc where tc.cuenta_mayor = n1 ) ) as descripcion,  \n");
        sentencia.append("decode(n1, '21', '', '22', '',saldo_anterior) saldo_anterior,  \n");
        sentencia.append("decode(n1, '21', '', '22', '',(saldo_actual - saldo_anterior))  AS movimienos_periodo, \n");
        sentencia.append("decode(n1, '21', '', '22', '','0')  AS depuracion, \n");
        sentencia.append("decode(n1, '21', '', '22', '',saldo_actual)  AS saldo_actual \n");
        sentencia.append("from \n");
        sentencia.append("( \n");
        sentencia.append("select '21' as n1,  0 saldo_anterior,0 cargo_periodo,0 abono_periodo,0 saldo_actual \n");
        sentencia.append("FROM dual \n");
        sentencia.append("union \n");
        sentencia.append("select '22' as n1,  0 saldo_anterior,0 cargo_periodo,0 abono_periodo,0 saldo_actual \n");
        sentencia.append("FROM dual \n");
        sentencia.append("union \n");
        sentencia.append("select substr(cc.cuenta_contable, 1, 4) AS n1, \n");
        sentencia.append("        case max(cl.naturaleza) \n");
        sentencia.append("        when 'D' \n");
        ////sentencia.append("        then (sum(" + mesAntCargo + ") - sum(" + mesAntAbono + ")) \n");
        sentencia.append("        then (sum(").append(mesAntCargo).append(") - sum(").append(mesAntAbono).append(")) \n");
        ////sentencia.append("        else (sum(" + mesAntAbono + ") - sum(" + mesAntCargo + ")) \n");
        sentencia.append("        else (sum(").append(mesAntAbono).append(") - sum(").append(mesAntCargo).append(")) \n");
        sentencia.append("        end saldo_anterior, \n");
        ////sentencia.append("        sum(cc." + mesActCargo + ") cargo_periodo, \n");
        sentencia.append("        sum(cc.").append(mesActCargo).append(") cargo_periodo, \n");
        ////sentencia.append("        sum(cc." + mesActAbono + ") abono_periodo, \n");
        sentencia.append("        sum(cc.").append(mesActAbono).append(") abono_periodo, \n");
        sentencia.append("        case max(cl.naturaleza) \n");
        sentencia.append("        when 'D' \n");
        sentencia.append("\nthen ((sum(").append(mesAntCargo).append(") + sum(").append(mesActCargo);
        sentencia.append(")) - (sum(").append(mesAntAbono).append(") + sum(").append(mesActAbono).append("))) ");
        ////sentencia.append("\nelse ((sum(" + mesAntAbono + ") + sum(" + mesActAbono + ")) - (sum(" + mesAntCargo + ") + sum(" + mesActCargo + "))) ");
        sentencia.append("\nelse ((sum(").append(mesAntAbono).append(") + sum(").append(mesActAbono);
        sentencia.append(")) - (sum(").append(mesAntCargo).append(") + sum(").append(mesActCargo).append("))) ");
        sentencia.append("        end saldo_actual \n");
        sentencia.append("from rf_tr_cuentas_contables cc, \n");
        sentencia.append("rf_tc_clasificador_cuentas cl \n");
        sentencia.append("where cc.cuenta_mayor_id = cl.cuenta_mayor_id \n");
        ////sentencia.append("      and cc.id_catalogo_cuenta = " + getControlReg().getIdCatalogoCuenta() + " \n");
        sentencia.append("      and cc.id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta()).append(" \n");
        ////sentencia.append("      and to_char(cc.fecha_vig_ini, 'yyyy') = " + getControlReg().getEjercicio() + " \n");
        sentencia.append("      and to_char(cc.fecha_vig_ini, 'yyyy') = ").append(getControlReg().getEjercicio()).append(" \n");
        sentencia.append("      and ( cc.cuenta_contable LIKE '21%'  \n");
        sentencia.append("      or cc.cuenta_contable LIKE '22%' ) \n");

        if (!getPrograma().equals("'0000'")) {
            //sentencia.append("\nand substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id," + getControlReg().getEjercicio() + "),f_longitud_nivel('programa',cc.cuenta_mayor_id," + getControlReg().getEjercicio() + ")) in (" + getPrograma() + ")");
            sentencia.append("\nand substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,");
            sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,");
            sentencia.append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }

        sentencia.append("      and substr(cc.cuenta_contable,f_posicion_nivel('unidad',cc.cuenta_mayor_id,2013),f_longitud_nivel('unidad',cc.cuenta_mayor_id, 2013)) = '0000'  \n");
        sentencia.append("      and substr(cc.cuenta_contable,f_posicion_nivel('ambito',cc.cuenta_mayor_id,2013),f_longitud_nivel('ambito',cc.cuenta_mayor_id,2013)) = '0000' \n");
        sentencia.append(" and cc.nivel = ").append(getNivel()).append(" \n");
        sentencia.append("group by substr(cc.cuenta_contable, 1, 4)  \n");
        sentencia.append(") \n");
        sentencia.append("order by n1 \n");

        //  System.out.println("PASIVO"+sentencia.toString());
        return sentencia;
    }

    public StringBuffer estadoAnaliticoDelaDeudayOtrosPasivos(String fecha_consolidacion, String mes1, String mes2, String ejercicioCompara) {
        Fecha fechaPeriodo = new Fecha(fecha_consolidacion, "/");
        String mes_act = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodo.getMes() - 1);
        fechaPeriodo.addMeses(-1);
        String mes_ant = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodo.getMes() - 1);
        StringBuffer sentencia = new StringBuffer();
        String mesAntCargo;
        String mesAntAbono;
        String mesActCargo;
        String mesActAbono;

        setNivel(!getPrograma().equals("'0000'") && getUnidad().equals("0000") && getAmbito().equals("0000") ? "3" : getNivel());

        if (mes_ant.equals("DIC")) {
            mesAntCargo = construirMes(mes_act, "cargo_ini");
            mesAntAbono = construirMes(mes_act, "abono_ini");
        } else {
            mesAntCargo = construirMes(mes_ant, "cargo_acum");
            mesAntAbono = construirMes(mes_ant, "abono_acum");
        }
        mesActCargo = construirMes(mes_act, "cargo");
        mesActAbono = construirMes(mes_act, "abono");

        sentencia.append("select p.descripcion_id,  \n");
        sentencia.append("case when length(p.descripcion_id)=2 then '            '||p.descripcion_rep else \n");
        sentencia.append("(case when length(p.descripcion_id)=4 then '      '||p.descripcion_rep else \n");
        sentencia.append("p.descripcion_rep end) end as Descripcion,0 saldo_inicial,0 saldo_final,decode(substr(p.descripcion_id,1,2),'21','CP','22','LP','') as grupo \n");
        sentencia.append("from rf_tc_rep_deuda_publica p \n");
        sentencia.append("where p.estatus=1 and p.descripcion_id not in ('2') \n");
        sentencia.append("union \n");
        sentencia.append("select \n");
        sentencia.append("'23' as descripcion_id, \n");
        sentencia.append("'Otros Pasivos' as Descripcion, \n");
        sentencia.append("CASE MAX(cl.naturaleza) \n");
        sentencia.append("      WHEN 'D' \n");
        sentencia.append("      THEN ( SUM(ENE_cargo_ini) -  SUM(ENE_abono_ini)) \n");
        sentencia.append("      ELSE ( SUM(ENE_abono_ini) -  SUM(ENE_cargo_ini)) \n");
        sentencia.append("     END AS saldo_inicial, \n");
        sentencia.append("   CASE MAX(cl.naturaleza) \n");
        sentencia.append("      WHEN 'D' \n");
        sentencia.append("      THEN (( SUM(").append(mesAntCargo).append(") + SUM(").append(mesActCargo);
        sentencia.append(")) - (SUM(").append(mesAntAbono).append(") + SUM(").append(mesActAbono).append(") )) \n");
        sentencia.append("      ELSE (( SUM(").append(mesAntAbono).append(") + SUM(").append(mesActAbono);
        sentencia.append(")) - (SUM(").append(mesAntCargo).append(") + SUM(").append(mesActCargo).append(") )) \n");
        sentencia.append("     END AS saldo_final,'OP' as grupo \n");
        sentencia.append("from RF_TR_CUENTAS_CONTABLES cc, \n");
        sentencia.append("rf_tc_clasificador_cuentas cl \n");
        sentencia.append("where cc.cuenta_mayor_id = cl.cuenta_mayor_id \n");
        sentencia.append("      and cc.id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta());
        sentencia.append("      and to_char(cc.fecha_vig_ini, 'yyyy') = ").append(getControlReg().getEjercicio());
        sentencia.append("      and cc.cuenta_contable like '2111%' \n");

        if (!getPrograma().equals("'0000'")) {
            //sentencia.append("\nand substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id," + getControlReg().getEjercicio() + "),f_longitud_nivel('programa',cc.cuenta_mayor_id," + getControlReg().getEjercicio() + ")) in (" + getPrograma() + ")");
            sentencia.append(" and substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,");
            sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,");
            sentencia.append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }

        sentencia.append("      and substr(cc.cuenta_contable,f_posicion_nivel('unidad',cc.cuenta_mayor_id,2014),f_longitud_nivel('unidad',cc.cuenta_mayor_id, 2014)) = '0000'  \n");
        sentencia.append("      and substr(cc.cuenta_contable,f_posicion_nivel('ambito',cc.cuenta_mayor_id,2014),f_longitud_nivel('ambito',cc.cuenta_mayor_id,2014)) = '0000' \n");
        sentencia.append(" and cc.nivel = ").append(getNivel()).append(" \n");
        sentencia.append("order by descripcion_id \n");

        // System.out.println("ANALITICO DE LA DEUDA"+sentencia.toString());
        return sentencia;
    }

    public StringBuffer estadoVariacionHaciendaPublicaPatrimonio(String fecha_consolidacion, String mes1, String mes2, String ejercicioCompara) {

        Fecha fechaPeriodo = new Fecha(fecha_consolidacion, "/");

        String mes_act1 = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodo.getMes() - 1);
        fechaPeriodo.addMeses(-1);

        String mes_ant1 = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodo.getMes() - 1);

        ///////////////////////////////////////////////////////////////////////////////////////////////////

        int diasMes = sia.libs.formato.Fecha.getDiasEnElMes(Integer.valueOf(ejercicioCompara), Integer.valueOf(mes2) - 1);
        Fecha fechaPeriodoAnt = new Fecha(ejercicioCompara + mes2 + String.valueOf(diasMes), "/");

        String mes_act2;
        fechaPeriodoAnt.addMeses(-1);

        String mes_ant2;
        StringBuffer sentencia = new StringBuffer();

        ///////////////////////////////////////////////////////////////////////////////////////////////////

        setNivel(!getPrograma().equals("'0000'") && getUnidad().equals("0000") && getAmbito().equals("0000") ? "3" : getNivel());

        String mesAntCargo1;
        String mesAntAbono1;
        String mesActCargo1;
        String mesActAbono1;
        String mesAntCargo2;
        String mesAntAbono2;
        String mesActCargo2;
        String mesActAbono2;

        String mesEne;
        String mesEneCargoIni;
        String mesEneAbonoIni;
        String mesEneAbono;
        String mesEneCargo;

        //mes_act1 = "ABR";
        //mes_ant1 = "MAR";
        //mes_act1 = "MAY";
        //mes_ant1 = "ABR";
        mes_act2 = "DIC";
        mes_ant2 = "NOV";
        mesEne = "ENE";

        //ejercicioCompara = "2012";

        mesAntCargo1 = construirMes(mes_ant1, "cargo_acum");
        mesAntAbono1 = construirMes(mes_ant1, "abono_acum");

        mesActCargo1 = construirMes(mes_act1, "cargo");
        mesActAbono1 = construirMes(mes_act1, "abono");

        mesAntCargo2 = construirMes(mes_ant2, "cargo_acum");
        mesAntAbono2 = construirMes(mes_ant2, "abono_acum");

        mesActCargo2 = construirMes(mes_act2, "cargo");
        mesActAbono2 = construirMes(mes_act2, "abono");

        mesEneAbono = construirMes(mesEne, "abono");
        mesEneCargo = construirMes(mesEne, "cargo");
        mesEneCargoIni = construirMes(mesEne, "cargo_ini");
        mesEneAbonoIni = construirMes(mesEne, "abono_ini");

        sentencia.append("SELECT 'Hacienda Pública / Patrimonio al Final del Ejercicio 2013' AS descripcion,");
        sentencia.append("\n  NULL                                                             AS importe_0,");
        sentencia.append("\n  NVL(");
        sentencia.append("\n  (SELECT ");
        sentencia.append("\n    CASE cl.naturaleza");
        sentencia.append("\n      WHEN 'D'");
        ////sentencia.append("\n      THEN SUM("+ mesEneCargoIni + ")  - SUM("+ mesEneAbonoIni + ")");
        sentencia.append("\n      THEN SUM(").append(mesEneCargoIni).append(")  - SUM(").append(mesEneAbonoIni).append(")");
        ////sentencia.append("\n      ELSE (SUM("+ mesEneAbonoIni + ")- SUM("+ mesEneCargoIni + "))");
        sentencia.append("\n      ELSE (SUM(").append(mesEneAbonoIni).append(")- SUM(").append(mesEneCargoIni).append("))");
        sentencia.append("\n    END saldo_anterior");
        sentencia.append("\n  FROM rf_tr_cuentas_contables cc,");
        sentencia.append("\n    rf_tc_clasificador_cuentas cl");
        sentencia.append("\n  WHERE cc.cuenta_mayor_id             = cl.cuenta_mayor_id");
        ////sentencia.append("\n  AND cc.id_catalogo_cuenta            = " + getControlReg().getIdCatalogoCuenta());
        sentencia.append("\n  AND cc.id_catalogo_cuenta            = ").append(getControlReg().getIdCatalogoCuenta());
        ////sentencia.append("\n  AND cc.id_catalogo_cuenta            = " + getControlReg().getIdCatalogoCuenta());
        sentencia.append("\n  AND cc.id_catalogo_cuenta            = ").append(getControlReg().getIdCatalogoCuenta());
        ////sentencia.append("\n  AND TO_CHAR(cc.fecha_vig_ini,'yyyy') = " + ejercicioCompara );        
        sentencia.append("\n  AND TO_CHAR(cc.fecha_vig_ini,'yyyy') = ").append(ejercicioCompara);
        sentencia.append("\n  AND cc.cuenta_contable LIKE '322_%'");

        if (!getPrograma().equals("'0000'")) {
            ////sentencia.append("\n  AND SUBSTR(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,"+getControlReg().getEjercicio()+"),f_longitud_nivel('programa',cc.cuenta_mayor_id,"+getControlReg().getEjercicio()+")) in ("+getPrograma()+")");
            sentencia.append("\n  AND SUBSTR(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }

        ////sentencia.append("\n  AND SUBSTR(cc.cuenta_contable,f_posicion_nivel('unidad',cc.cuenta_mayor_id,"+getControlReg().getEjercicio()+"),f_longitud_nivel('unidad',cc.cuenta_mayor_id,"+getControlReg().getEjercicio()+")) = '"+getUnidad()+"' "); 
        sentencia.append("\n  AND SUBSTR(cc.cuenta_contable,f_posicion_nivel('unidad',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getUnidad()).append("' ");
        ////sentencia.append("\n  AND SUBSTR(cc.cuenta_contable,f_posicion_nivel('ambito',cc.cuenta_mayor_id,"+getControlReg().getEjercicio()+"),f_longitud_nivel('ambito',cc.cuenta_mayor_id,"+getControlReg().getEjercicio()+")) = '"+getAmbito()+"'");
        sentencia.append("\n  AND SUBSTR(cc.cuenta_contable,f_posicion_nivel('ambito',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('ambito',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getAmbito()).append("'");
        ////sentencia.append("\n  AND cc.nivel =  " + getNivel() );
        sentencia.append("\n  AND cc.nivel =  ").append(getNivel());

        sentencia.append("\n  GROUP BY cl.naturaleza");
        sentencia.append("\n  ), 0) AS importe_1,");
        sentencia.append("\n  NVL(");
        sentencia.append("\n  (SELECT");
        sentencia.append("\n    CASE cl.naturaleza");
        sentencia.append("\n      WHEN 'D'");
        ////sentencia.append("\n      THEN SUM("+ mesEneCargoIni + ")  - SUM("+ mesEneAbonoIni + ")");
        sentencia.append("\n      THEN SUM(").append(mesEneCargoIni).append(")  - SUM(").append(mesEneAbonoIni).append(")");
        ////sentencia.append("\n      ELSE (SUM("+ mesEneAbonoIni + ")- SUM("+ mesEneCargoIni + "))");
        sentencia.append("\n      ELSE (SUM(").append(mesEneAbonoIni).append(")- SUM(").append(mesEneCargoIni).append("))");
        sentencia.append("\n    END saldo_anterior");
        sentencia.append("\n  FROM rf_tr_cuentas_contables cc,");
        sentencia.append("\n    rf_tc_clasificador_cuentas cl");
        sentencia.append("\n  WHERE cc.cuenta_mayor_id             = cl.cuenta_mayor_id");
        ////sentencia.append("\n  AND cc.id_catalogo_cuenta            = " + getControlReg().getIdCatalogoCuenta());
        sentencia.append("\n  AND cc.id_catalogo_cuenta            = ").append(getControlReg().getIdCatalogoCuenta());
        ////sentencia.append("\n  AND TO_CHAR(cc.fecha_vig_ini,'yyyy') = " + ejercicioCompara );
        sentencia.append("\n  AND TO_CHAR(cc.fecha_vig_ini,'yyyy') = ").append(ejercicioCompara);
        sentencia.append("\n  AND cc.cuenta_contable LIKE '321_%'");

        if (!getPrograma().equals("'0000'")) {
            ////sentencia.append("\n    AND SUBSTR(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,"+getControlReg().getEjercicio()+"),f_longitud_nivel('programa',cc.cuenta_mayor_id,"+getControlReg().getEjercicio()+")) in ("+getPrograma()+")");
            sentencia.append("\n    AND SUBSTR(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }

        ////sentencia.append("\n  AND SUBSTR(cc.cuenta_contable,f_posicion_nivel('unidad',cc.cuenta_mayor_id,"+getControlReg().getEjercicio()+"),f_longitud_nivel('unidad',cc.cuenta_mayor_id,"+getControlReg().getEjercicio()+")) = '"+getUnidad()+"' "); 
        sentencia.append("\n  AND SUBSTR(cc.cuenta_contable,f_posicion_nivel('unidad',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getUnidad()).append("' ");
        ////sentencia.append("\n  AND SUBSTR(cc.cuenta_contable,f_posicion_nivel('ambito',cc.cuenta_mayor_id,"+getControlReg().getEjercicio()+"),f_longitud_nivel('ambito',cc.cuenta_mayor_id,"+getControlReg().getEjercicio()+")) = '"+getAmbito()+"'");
        sentencia.append("\n  AND SUBSTR(cc.cuenta_contable,f_posicion_nivel('ambito',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('ambito',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getAmbito()).append("'");
        ////sentencia.append("\n  AND cc.nivel =  " + getNivel() );
        sentencia.append("\n  AND cc.nivel =  ").append(getNivel());

        sentencia.append("\n  GROUP BY cl.naturaleza");
        sentencia.append("\n  ), 0) AS importe_2");
        sentencia.append("\nFROM dual");
        sentencia.append("\nUNION ALL");
        sentencia.append("\nSELECT 'Variaciones de la Hacienda Pública / Patrimonio Neto del Ejercicio:' AS descripcion,");
        sentencia.append("\n  NULL                                                                       AS importe_0,");
        sentencia.append("\n  NULL                                                                       AS importe_1,");
        sentencia.append("\n  NULL                                                                       AS importe_2");
        sentencia.append("\nFROM dual");
        sentencia.append("\nUNION ALL");
        sentencia.append("\nSELECT DECODE( NVL(");
        sentencia.append("\n  CASE MAX(cl.naturaleza)");
        sentencia.append("\n    WHEN 'D'");
        ////sentencia.append("\n    THEN ((SUM(" + mesAntCargo2 + ") + SUM(" + mesActCargo2 + ")) - (SUM(" + mesAntAbono2 + ") + SUM(" + mesActAbono2 + ")))");
        sentencia.append("\n    THEN ((SUM(").append(mesAntCargo2).append(") + SUM(").append(mesActCargo2).append(")) - (SUM(").append(mesAntAbono2).append(") + SUM(").append(mesActAbono2).append(")))");
        ////sentencia.append("\n    ELSE ((SUM(" + mesAntAbono2 + ") + SUM(" + mesActAbono2 + ")) - (SUM(" + mesAntCargo2 + ") + SUM(" + mesActCargo2 + ")))");
        sentencia.append("\n    ELSE ((SUM(").append(mesAntAbono2).append(") + SUM(").append(mesActAbono2).append(")) - (SUM(").append(mesAntCargo2).append(") + SUM(").append(mesActCargo2).append(")))");
        sentencia.append("\n  END, 0), 0, NULL, '1     Aportaciones' ) AS descripcion,");
        sentencia.append("\n  DECODE( NVL(");
        sentencia.append("\n  CASE MAX(cl.naturaleza)");
        sentencia.append("\n    WHEN 'D'");
        ////sentencia.append("\n    THEN ((SUM(" + mesAntCargo2 + ") + SUM(" + mesActCargo2 + ")) - (SUM(" + mesAntAbono2 + ") + SUM(" + mesActAbono2 + ")))");
        sentencia.append("\n    THEN ((SUM(").append(mesAntCargo2).append(") + SUM(").append(mesActCargo2).append(")) - (SUM(").append(mesAntAbono2).append(") + SUM(").append(mesActAbono2).append(")))");
        ////sentencia.append("\n    ELSE ((SUM(" + mesAntAbono2 + ") + SUM(" + mesActAbono2 + ")) - (SUM(" + mesAntCargo2 + ") + SUM(" + mesActCargo2 + ")))");
        sentencia.append("\n    ELSE ((SUM(").append(mesAntAbono2).append(") + SUM(").append(mesActAbono2).append(")) - (SUM(").append(mesAntCargo2).append(") + SUM(").append(mesActCargo2).append(")))");
        sentencia.append("\n  END, 0), 0, NULL,");
        sentencia.append("\n  CASE MAX(cl.naturaleza)");
        sentencia.append("\n    WHEN 'D'");
        ////sentencia.append("\n    THEN ((SUM(" + mesAntCargo2 + ") + SUM(" + mesActCargo2 + ")) - (SUM(" + mesAntAbono2 + ") + SUM(" + mesActAbono2 + ")))");
        sentencia.append("\n    THEN ((SUM(").append(mesAntCargo2).append(") + SUM(").append(mesActCargo2).append(")) - (SUM(").append(mesAntAbono2).append(") + SUM(").append(mesActAbono2).append(")))");
        ////sentencia.append("\n    ELSE ((SUM(" + mesAntAbono2 + ") + SUM( " + mesActAbono2 + ")) - (SUM(" + mesAntCargo2 + ") + SUM(" + mesActCargo2 + ")))");
        sentencia.append("\n    ELSE ((SUM(").append(mesAntAbono2).append(") + SUM( ").append(mesActAbono2).append(")) - (SUM(").append(mesAntCargo2).append(") + SUM(").append(mesActCargo2).append(")))");
        sentencia.append("\n  END ) AS importe_0,");
        sentencia.append("\n  to_number( DECODE( NVL(");
        sentencia.append("\n  CASE MAX(cl.naturaleza)");
        sentencia.append("\n    WHEN 'D'");
        ////sentencia.append("\n    THEN ((SUM(" + mesAntCargo2 + ") + SUM(" + mesActCargo2 + ")) - (SUM(" + mesAntAbono2 + ") + SUM(" + mesActAbono2 + ")))");
        sentencia.append("\n    THEN ((SUM(").append(mesAntCargo2).append(") + SUM(").append(mesActCargo2).append(")) - (SUM(").append(mesAntAbono2).append(") + SUM(").append(mesActAbono2).append(")))");
        ////sentencia.append("\n    ELSE ((SUM(" + mesAntAbono2 + ") + SUM(" + mesActAbono2 + ")) - (SUM(" + mesAntCargo2 + ") + SUM(" + mesActCargo2 + ")))");
        sentencia.append("\n    ELSE ((SUM(").append(mesAntAbono2).append(") + SUM(").append(mesActAbono2).append(")) - (SUM(").append(mesAntCargo2).append(") + SUM(").append(mesActCargo2).append(")))");
        sentencia.append("\n  END, 0), 0, NULL, (");
        sentencia.append("\n  CASE MAX(cl.naturaleza)");
        sentencia.append("\n    WHEN 'D'");
        ////sentencia.append("\n    THEN ((SUM(" + mesAntCargo2 + ") + SUM(" + mesActCargo2 + ")) - (SUM(" + mesAntAbono2 + ") + SUM(" + mesActAbono2 + ")))");
        sentencia.append("\n    THEN ((SUM(").append(mesAntCargo2).append(") + SUM(").append(mesActCargo2).append(")) - (SUM(").append(mesAntAbono2).append(") + SUM(").append(mesActAbono2).append(")))");
        ////sentencia.append("\n    ELSE ((SUM(" + mesAntAbono2 + ") + SUM(" + mesActAbono2 + ")) - (SUM(" + mesAntCargo2 + ") + SUM(" + mesActCargo2 + ")))"); 
        sentencia.append("\n    ELSE ((SUM(").append(mesAntAbono2).append(") + SUM(").append(mesActAbono2).append(")) - (SUM(").append(mesAntCargo2).append(") + SUM(").append(mesActCargo2).append(")))");
        sentencia.append("\n  END)                             * -1 ) ) AS importe_1,");
        sentencia.append("\n  NULL                                      AS importe_2");
        sentencia.append("\nFROM rf_tr_cuentas_contables cc,");
        sentencia.append("\n  rf_tc_clasificador_cuentas cl");
        sentencia.append("\nWHERE cc.cuenta_mayor_id            = cl.cuenta_mayor_id");
        ////sentencia.append("\nAND cc.id_catalogo_cuenta           = " + getControlReg().getIdCatalogoCuenta());
        sentencia.append("\nAND cc.id_catalogo_cuenta           = ").append(getControlReg().getIdCatalogoCuenta());
        ////sentencia.append("\nAND TO_CHAR(cc.fecha_vig_ini,'yyyy')= " + ejercicioCompara );
        sentencia.append("\nAND TO_CHAR(cc.fecha_vig_ini,'yyyy')= ").append(ejercicioCompara);
        sentencia.append("\nAND cc.cuenta_contable LIKE '311_%'");

        if (!getPrograma().equals("'0000'")) {
            ////sentencia.append("\n  AND SUBSTR(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,"+getControlReg().getEjercicio()+"),f_longitud_nivel('programa',cc.cuenta_mayor_id,"+getControlReg().getEjercicio()+")) in ("+getPrograma()+")");
            sentencia.append("\n  AND SUBSTR(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }

        ////sentencia.append("\n  AND SUBSTR(cc.cuenta_contable,f_posicion_nivel('unidad',cc.cuenta_mayor_id,"+getControlReg().getEjercicio()+"),f_longitud_nivel('unidad',cc.cuenta_mayor_id,"+getControlReg().getEjercicio()+")) = '"+getUnidad()+"' "); 
        sentencia.append("\n  AND SUBSTR(cc.cuenta_contable,f_posicion_nivel('unidad',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getUnidad()).append("' ");
        ////sentencia.append("\n  AND SUBSTR(cc.cuenta_contable,f_posicion_nivel('ambito',cc.cuenta_mayor_id,"+getControlReg().getEjercicio()+"),f_longitud_nivel('ambito',cc.cuenta_mayor_id,"+getControlReg().getEjercicio()+")) = '"+getAmbito()+"'");
        sentencia.append("\n  AND SUBSTR(cc.cuenta_contable,f_posicion_nivel('ambito',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('ambito',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getAmbito()).append("'");
        ////sentencia.append("\n  AND cc.nivel =  " + getNivel() );
        sentencia.append("\n  AND cc.nivel =  ").append(getNivel());

        sentencia.append("\nUNION ALL");
        sentencia.append("\nSELECT DECODE( NVL(");
        sentencia.append("\n  CASE MAX(cl.naturaleza)");
        sentencia.append("\n    WHEN 'D'");
        ////sentencia.append("\n    THEN ((SUM(" + mesAntCargo2 + ") + SUM(" + mesActCargo2 + ")) - (SUM(" + mesAntAbono2 + ") + SUM(" + mesActAbono2 + ")))");
        sentencia.append("\n    THEN ((SUM(").append(mesAntCargo2).append(") + SUM(").append(mesActCargo2).append(")) - (SUM(").append(mesAntAbono2).append(") + SUM(").append(mesActAbono2).append(")))");
        ////sentencia.append("\n    ELSE ((SUM(" + mesAntAbono2 + ") + SUM(" + mesActAbono2 + ")) - (SUM(" + mesAntCargo2 + ") + SUM(" + mesActCargo2 + ")))");
        sentencia.append("\n    ELSE ((SUM(").append(mesAntAbono2).append(") + SUM(").append(mesActAbono2).append(")) - (SUM(").append(mesAntCargo2).append(") + SUM(").append(mesActCargo2).append(")))");
        sentencia.append("\n  END, 0), 0, NULL, '2     Donaciones de Capital' ) AS descripcion,");
        sentencia.append("\n  DECODE( NVL(");
        sentencia.append("\n  CASE MAX(cl.naturaleza)");
        sentencia.append("\n    WHEN 'D'");
        ////sentencia.append("\n    THEN ((SUM(" + mesAntCargo2 + ") + SUM(" + mesActCargo2 + ")) - (SUM(" + mesAntAbono2 + ") + SUM(" + mesActAbono2 + ")))");
        sentencia.append("\n    THEN ((SUM(").append(mesAntCargo2).append(") + SUM(").append(mesActCargo2).append(")) - (SUM(").append(mesAntAbono2).append(") + SUM(").append(mesActAbono2).append(")))");
        ////sentencia.append("\n    ELSE ((SUM(" + mesAntAbono2 + ") + SUM(" + mesActAbono2 + ")) - (SUM(" + mesAntCargo2 + ") + SUM(" + mesActCargo2 + ")))");
        sentencia.append("\n    ELSE ((SUM(").append(mesAntAbono2).append(") + SUM(").append(mesActAbono2).append(")) - (SUM(").append(mesAntCargo2).append(") + SUM(").append(mesActCargo2).append(")))");
        sentencia.append("\n  END, 0), 0, NULL,");
        sentencia.append("\n  CASE MAX(cl.naturaleza)");
        sentencia.append("\n    WHEN 'D'");
        ////sentencia.append("\n    THEN ((SUM(" + mesAntCargo2 + ") + SUM(" + mesActCargo2 + ")) - (SUM(" + mesAntAbono2 + ") + SUM( " + mesActAbono2 + ")))");
        sentencia.append("\n    THEN ((SUM(").append(mesAntCargo2).append(") + SUM(").append(mesActCargo2).append(")) - (SUM(").append(mesAntAbono2).append(") + SUM( ").append(mesActAbono2).append(")))");
        ////sentencia.append("\n    ELSE ((SUM(" + mesAntAbono2 + ") + SUM(" + mesActAbono2 + ")) - (SUM(" + mesAntCargo2 + ") + SUM(" + mesActCargo2 + ")))");
        sentencia.append("\n    ELSE ((SUM(").append(mesAntAbono2).append(") + SUM(").append(mesActAbono2).append(")) - (SUM(").append(mesAntCargo2).append(") + SUM(").append(mesActCargo2).append(")))");
        sentencia.append("\n  END )                                                                      AS importe_0,");
        sentencia.append("\n  to_number( DECODE( NVL(SUM(sep_abono), 0), 0, NULL, SUM(sep_abono)* -1 ) ) AS importe_1,");
        sentencia.append("\n  NULL                                                                       AS importe_2");
        sentencia.append("\nFROM rf_tr_cuentas_contables cc,");
        sentencia.append("\n  rf_tc_clasificador_cuentas cl");
        sentencia.append("\nWHERE cc.cuenta_mayor_id            = cl.cuenta_mayor_id");
        ////sentencia.append("\nAND cc.id_catalogo_cuenta           = " + getControlReg().getIdCatalogoCuenta());
        sentencia.append("\nAND cc.id_catalogo_cuenta           = ").append(getControlReg().getIdCatalogoCuenta());
        sentencia.append("\nAND TO_CHAR(cc.fecha_vig_ini,'yyyy')= ").append(ejercicioCompara);
        sentencia.append("\nAND cc.cuenta_contable LIKE '312_%'");

        if (!getPrograma().equals("'0000'")) {
            ////sentencia.append("\n  AND SUBSTR(cc.cuenta_contable, f_posicion_nivel('programa',cc.cuenta_mayor_id,"+getControlReg().getEjercicio()+"), f_longitud_nivel('programa', cc.cuenta_mayor_id,"+getControlReg().getEjercicio()+")) in ("+getPrograma()+")");
            sentencia.append("\n  AND SUBSTR(cc.cuenta_contable, f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("), f_longitud_nivel('programa', cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }

        ////sentencia.append("\n  AND SUBSTR(cc.cuenta_contable, f_posicion_nivel('unidad',cc.cuenta_mayor_id,"+getControlReg().getEjercicio()+"),f_longitud_nivel('unidad',cc.cuenta_mayor_id,"+getControlReg().getEjercicio()+")) = '"+getUnidad()+"' "); 
        sentencia.append("\n  AND SUBSTR(cc.cuenta_contable, f_posicion_nivel('ambito',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('ambito',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getAmbito()).append("'");
        ////sentencia.append("\n  AND cc.nivel =  "+getNivel() );
        sentencia.append("\n  AND cc.nivel =  ").append(getNivel());

        sentencia.append("\nUNION ALL");
        sentencia.append("\nSELECT '3     Resultados del Ejercicio : Ahorro / Desahorro' AS descripcion,");
        sentencia.append("\n  NULL                                                       AS importe_0,");
        sentencia.append("\n  SUM(saldo_anterior)                                        AS importe_1,");
        sentencia.append("\n  ( SUM(importe_calculo)- SUM(saldo_anterior) )              AS ahorro_desahorro");
        sentencia.append("\nFROM");
        sentencia.append("\n  (SELECT");
        sentencia.append("\n    CASE");
        sentencia.append("\n      WHEN SUBSTR(cc.cuenta_contable, 1, 1)= '4'");
        sentencia.append("\n      THEN");
        sentencia.append("\n        CASE cl.naturaleza");
        sentencia.append("\n          WHEN 'D'");
        ////sentencia.append("\n THEN ((" + mesAntCargo2 + " + " + mesActCargo2 + ") - (" + mesAntAbono2 + " +  " + mesActAbono2 + "))");
        sentencia.append("\n THEN ((").append(mesAntCargo2).append(" + ").append(mesActCargo2).append(") - (").append(mesAntAbono2).append(" +  ").append(mesActAbono2).append("))");
        ////sentencia.append("\n          ELSE ((" + mesAntAbono2 + " +  " + mesActAbono2 + ") - (" + mesAntCargo2 + " + " + mesActCargo2 + "))");
        sentencia.append("\n          ELSE ((").append(mesAntAbono2).append(" +  ").append(mesActAbono2).append(") - (").append(mesAntCargo2).append(" + ").append(mesActCargo2).append("))");
        sentencia.append("\n        END");
        //sentencia.append("\n      ELSE (cc." + mesAntCargo2 + "+ cc." + mesActCargo2 + ")*-1");
        sentencia.append("\n      ELSE (cc.").append(mesAntCargo2).append("+ cc.").append(mesActCargo2).append(")*-1");
        sentencia.append("\n    END AS importe_calculo,");
        sentencia.append("\n    0   AS saldo_anterior");
        sentencia.append("\n  FROM rf_tr_cuentas_contables cc,");
        sentencia.append("\n    rf_tc_clasificador_cuentas cl");
        sentencia.append("\n  WHERE cc.cuenta_mayor_id             = cl.cuenta_mayor_id");
        //sentencia.append("\n  AND cc.id_catalogo_cuenta            = " + getControlReg().getIdCatalogoCuenta());
        sentencia.append("\n  AND cc.id_catalogo_cuenta            = ").append(getControlReg().getIdCatalogoCuenta());
        //sentencia.append("\n  AND TO_CHAR(cc.fecha_vig_ini,'yyyy') = " + ejercicioCompara );      
        sentencia.append("\n  AND TO_CHAR(cc.fecha_vig_ini,'yyyy') = ").append(ejercicioCompara);
        sentencia.append("\n  AND ( cc.cuenta_contable LIKE '42__%'");
        sentencia.append("\n  OR cc.cuenta_contable LIKE '431_%'");
        sentencia.append("\n  OR cc.cuenta_contable LIKE '439_%'");
        sentencia.append("\n  OR cc.cuenta_contable LIKE '511_%'");
        sentencia.append("\n  OR cc.cuenta_contable LIKE '512_%'");
        sentencia.append("\n  OR cc.cuenta_contable LIKE '513_%'");
        sentencia.append("\n  OR cc.cuenta_contable LIKE '524_%'");
        sentencia.append("\n  OR cc.cuenta_contable LIKE '528_%'");
        sentencia.append("\n  OR cc.cuenta_contable LIKE '529_%'");
        sentencia.append("\n  OR cc.cuenta_contable LIKE '553_%'");
        sentencia.append("\n  OR cc.cuenta_contable LIKE '559_%'");
        sentencia.append("\n  OR cc.cuenta_contable LIKE '432_%'");
        sentencia.append("\n  OR cc.cuenta_contable LIKE '553_%' )");

        if (!getPrograma().equals("'0000'")) {
            ////sentencia.append("\n  AND SUBSTR(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,"+getControlReg().getEjercicio()+"),f_longitud_nivel('programa',cc.cuenta_mayor_id,"+getControlReg().getEjercicio()+")) in ("+getPrograma()+")");
            sentencia.append("\n  AND SUBSTR(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }

        //sentencia.append("\n  AND SUBSTR(cc.cuenta_contable,f_posicion_nivel('unidad',cc.cuenta_mayor_id,"+getControlReg().getEjercicio()+"),f_longitud_nivel('unidad',cc.cuenta_mayor_id,"+getControlReg().getEjercicio()+")) = '"+getUnidad()+"' "); 
        sentencia.append("\n  AND SUBSTR(cc.cuenta_contable,f_posicion_nivel('unidad',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getUnidad()).append("' ");
        //sentencia.append("\n  AND SUBSTR(cc.cuenta_contable,f_posicion_nivel('ambito',cc.cuenta_mayor_id,"+getControlReg().getEjercicio()+"),f_longitud_nivel('ambito',cc.cuenta_mayor_id,"+getControlReg().getEjercicio()+")) = '"+getAmbito()+"'");
        sentencia.append("\n  AND SUBSTR(cc.cuenta_contable,f_posicion_nivel('ambito',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('ambito',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append(")) = '").append(getAmbito()).append("'");
        //sentencia.append("\n  AND cc.nivel =  " + getNivel() );
        sentencia.append("\n  AND cc.nivel =  ").append(getNivel());

        sentencia.append("\n  UNION ALL");
        sentencia.append("\n  SELECT 0 AS importe_calculo,");
        sentencia.append("\n    CASE cl.naturaleza");
        sentencia.append("\n      WHEN 'D'");
        //sentencia.append("\n      THEN "+ mesEneCargoIni + " - "+ mesEneAbonoIni + "");
        sentencia.append("\n      THEN ").append(mesEneCargoIni).append(" - ").append(mesEneAbonoIni).append("");
        //sentencia.append("\n      ELSE (ene_abono_ini_eli- "+ mesEneCargoIni + ")");
        sentencia.append("\n      ELSE (ene_abono_ini_eli- ").append(mesEneCargoIni).append(")");
        sentencia.append("\n    END saldo_anterior");
        sentencia.append("\n  FROM rf_tr_cuentas_contables cc,");
        sentencia.append("\n    rf_tc_clasificador_cuentas cl");
        sentencia.append("\n  WHERE cc.cuenta_mayor_id            = cl.cuenta_mayor_id");
        ////sentencia.append("\n  AND cc.id_catalogo_cuenta           = " + getControlReg().getIdCatalogoCuenta());
        sentencia.append("\n  AND cc.id_catalogo_cuenta           = ").append(getControlReg().getIdCatalogoCuenta());
        ////sentencia.append("\n  AND TO_CHAR(cc.fecha_vig_ini,'yyyy')= " + ejercicioCompara );
        sentencia.append("\n  AND TO_CHAR(cc.fecha_vig_ini,'yyyy')= ").append(ejercicioCompara);
        sentencia.append("\n  AND cc.cuenta_contable LIKE '321_%'");

        if (!getPrograma().equals("'0000'")) {
            ////sentencia.append("\n  AND SUBSTR(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,"+getControlReg().getEjercicio()+"),f_longitud_nivel('programa',cc.cuenta_mayor_id,"+getControlReg().getEjercicio()+")) in ("+getPrograma()+")");
            sentencia.append("\n  AND SUBSTR(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }

        ////sentencia.append("\n  AND SUBSTR(cc.cuenta_contable,f_posicion_nivel('unidad',cc.cuenta_mayor_id,"+getControlReg().getEjercicio()+"),f_longitud_nivel('unidad',cc.cuenta_mayor_id,"+getControlReg().getEjercicio()+")) = '"+getUnidad()+"' "); 
        sentencia.append("\n  AND SUBSTR(cc.cuenta_contable,f_posicion_nivel('unidad',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getUnidad()).append("' ");
        ////sentencia.append("\n  AND SUBSTR(cc.cuenta_contable,f_posicion_nivel('ambito',cc.cuenta_mayor_id,"+getControlReg().getEjercicio()+"),f_longitud_nivel('ambito',cc.cuenta_mayor_id,"+getControlReg().getEjercicio()+")) = '"+getAmbito()+"'");
        sentencia.append("\n  AND SUBSTR(cc.cuenta_contable,f_posicion_nivel('ambito',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('ambito',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getAmbito()).append("'");
        ////sentencia.append("\n  AND cc.nivel =  "+getNivel());
        sentencia.append("\n  AND cc.nivel =  ").append(getNivel());

        sentencia.append("\n  )");
        sentencia.append("\nUNION ALL");
        sentencia.append("\nSELECT 'Variaciones de la Hacienda Pública / Patrimonio Neto del Ejercicio:' AS descripcion,");
        sentencia.append("\n  NULL                                                                       AS importe_0,");
        sentencia.append("\n  NULL                                                                       AS importe_1,");
        sentencia.append("\n  NULL                                                                       AS importe_2");
        sentencia.append("\nFROM dual");
        sentencia.append("\nUNION ALL");
        ////sentencia.append("\nSELECT DECODE( NVL(SUM(cc."+ mesEneAbono + "), 0), 0, NULL, '4     Aportaciones' )                AS descripcion,");
        sentencia.append("\nSELECT DECODE( NVL(SUM(cc.").append(mesEneAbono).append("), 0), 0, NULL, '4     Aportaciones' )                AS descripcion,");
        ////sentencia.append("\n  DECODE( NVL(SUM(cc."+ mesEneAbono + "), 0), 0, NULL, SUM(cc."+ mesEneAbono + ") )                    AS importe_0,");
        sentencia.append("\n  DECODE( NVL(SUM(cc.").append(mesEneAbono).append("), 0), 0, NULL, SUM(cc.").append(mesEneAbono).append(") )                    AS importe_0,");
        sentencia.append("\n  NULL                                                                                       AS importe_1,");
        //sentencia.append("\n  to_number( DECODE( NVL(SUM(cc."+ mesEneAbono + "), 0), 0, NULL, (SUM(cc."+ mesEneAbono + "))* -1 ) ) AS importe_2");
        sentencia.append("\n  to_number( DECODE( NVL(SUM(cc.").append(mesEneAbono).append("), 0), 0, NULL, (SUM(cc.").append(mesEneAbono).append("))* -1 ) ) AS importe_2");
        sentencia.append("\nFROM rf_tr_cuentas_contables cc,");
        sentencia.append("\n  rf_tc_clasificador_cuentas cl");
        sentencia.append("\nWHERE cc.cuenta_mayor_id            = cl.cuenta_mayor_id");
        ////sentencia.append("\nAND cc.id_catalogo_cuenta           = " + getControlReg().getIdCatalogoCuenta());
        sentencia.append("\nAND cc.id_catalogo_cuenta           = ").append(getControlReg().getIdCatalogoCuenta());
        ////sentencia.append("\nAND TO_CHAR(cc.fecha_vig_ini,'yyyy')= " + getControlReg().getEjercicio());
        sentencia.append("\nAND TO_CHAR(cc.fecha_vig_ini,'yyyy')= ").append(getControlReg().getEjercicio());
        sentencia.append("\nAND cc.cuenta_contable LIKE '311_%'");

        if (!getPrograma().equals("'0000'")) {
            ////sentencia.append("\n  AND SUBSTR(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,"+getControlReg().getEjercicio()+"),f_longitud_nivel('programa',cc.cuenta_mayor_id,"+getControlReg().getEjercicio()+")) in ("+getPrograma()+")");
            sentencia.append("\n  AND SUBSTR(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }

        ////sentencia.append("\n  AND SUBSTR(cc.cuenta_contable,f_posicion_nivel('unidad',cc.cuenta_mayor_id,"+getControlReg().getEjercicio()+"),f_longitud_nivel('unidad',cc.cuenta_mayor_id,"+getControlReg().getEjercicio()+")) = '"+getUnidad()+"' "); 
        sentencia.append("\n  AND SUBSTR(cc.cuenta_contable,f_posicion_nivel('unidad',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getUnidad()).append("' ");
        //sentencia.append("\n  AND SUBSTR(cc.cuenta_contable,f_posicion_nivel('ambito',cc.cuenta_mayor_id,"+getControlReg().getEjercicio()+"),f_longitud_nivel('ambito',cc.cuenta_mayor_id,"+getControlReg().getEjercicio()+")) = '"+getAmbito()+"'");
        sentencia.append("\n  AND SUBSTR(cc.cuenta_contable,f_posicion_nivel('ambito',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('ambito',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getAmbito()).append("'");
        //sentencia.append("\n  AND cc.nivel =  "+getNivel() );
        sentencia.append("\n  AND cc.nivel =  ").append(getNivel());

        sentencia.append("\nUNION ALL");
        ////sentencia.append("\nSELECT DECODE( NVL(SUM(cc."+ mesEneAbono + "), 0), 0, NULL, '5     Donaciones de Capital' )       AS descripcion,");
        sentencia.append("\nSELECT DECODE( NVL(SUM(cc.").append(mesEneAbono).append("), 0), 0, NULL, '5     Donaciones de Capital' )       AS descripcion,");
        ////sentencia.append("\n  DECODE( NVL(SUM(cc."+ mesEneAbono + "), 0), 0, NULL, SUM(cc."+ mesEneAbono + ") )                    AS importe_0,");
        sentencia.append("\n  DECODE( NVL(SUM(cc.").append(mesEneAbono).append("), 0), 0, NULL, SUM(cc.").append(mesEneAbono).append(") )                    AS importe_0,");
        sentencia.append("\n  NULL                                                                                       AS importe_1,");
        ////sentencia.append("\n  to_number( DECODE( NVL(SUM(cc."+ mesEneAbono + "), 0), 0, NULL, (SUM(cc."+ mesEneAbono + "))* -1 ) ) AS importe_2");
        sentencia.append("\n  to_number( DECODE( NVL(SUM(cc.").append(mesEneAbono).append("), 0), 0, NULL, (SUM(cc.").append(mesEneAbono).append("))* -1 ) ) AS importe_2");
        sentencia.append("\nFROM rf_tr_cuentas_contables cc,");
        sentencia.append("\n  rf_tc_clasificador_cuentas cl");
        sentencia.append("\nWHERE cc.cuenta_mayor_id            = cl.cuenta_mayor_id");
        ////sentencia.append("\nAND cc.id_catalogo_cuenta           = " + getControlReg().getIdCatalogoCuenta());
        sentencia.append("\nAND cc.id_catalogo_cuenta           = ").append(getControlReg().getIdCatalogoCuenta());
        ////sentencia.append("\nAND TO_CHAR(cc.fecha_vig_ini,'yyyy')= " + getControlReg().getEjercicio());
        sentencia.append("\nAND TO_CHAR(cc.fecha_vig_ini,'yyyy')= ").append(getControlReg().getEjercicio());
        sentencia.append("\nAND cc.cuenta_contable LIKE '312_%'");

        if (!getPrograma().equals("'0000'")) {
            //sentencia.append("\n  AND SUBSTR(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,"+getControlReg().getEjercicio()+"),f_longitud_nivel('programa',cc.cuenta_mayor_id,"+getControlReg().getEjercicio()+")) in ("+getPrograma()+")");
            sentencia.append("\n  AND SUBSTR(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }

        ////sentencia.append("\n  AND SUBSTR(cc.cuenta_contable,f_posicion_nivel('unidad',cc.cuenta_mayor_id,"+getControlReg().getEjercicio()+"),f_longitud_nivel('unidad',cc.cuenta_mayor_id,"+getControlReg().getEjercicio()+")) = '"+getUnidad()+"' "); 
        sentencia.append("\n  AND SUBSTR(cc.cuenta_contable,f_posicion_nivel('unidad',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getUnidad()).append("' ");
        ////sentencia.append("\n  AND SUBSTR(cc.cuenta_contable,f_posicion_nivel('ambito',cc.cuenta_mayor_id,"+getControlReg().getEjercicio()+"),f_longitud_nivel('ambito',cc.cuenta_mayor_id,"+getControlReg().getEjercicio()+")) = '"+getAmbito()+"'");
        sentencia.append("\n  AND SUBSTR(cc.cuenta_contable,f_posicion_nivel('ambito',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('ambito',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getAmbito()).append("'");
        ////sentencia.append("\n  AND cc.nivel =  " + getNivel());
        sentencia.append("\n  AND cc.nivel =  ").append(getNivel());

        sentencia.append("\nUNION ALL");
        sentencia.append("\nSELECT '6     Resultados del Ejercicio : Ahorro / Desahorro' AS descripcion,");
        sentencia.append("\n  NULL                                                       AS importe_0,");
        sentencia.append("\n  SUM(saldo_anterior)                                        AS importe_1,");
        sentencia.append("\n  ( SUM(importe_calculo)- SUM(saldo_anterior) )              AS ahorro_desahorro");
        sentencia.append("\nFROM");
        sentencia.append("\n  (SELECT");
        sentencia.append("\n    CASE");
        sentencia.append("\n      WHEN SUBSTR(cc.cuenta_contable, 1, 1)= '4'");
        sentencia.append("\n      THEN");
        sentencia.append("\n        CASE cl.naturaleza");
        sentencia.append("\n          WHEN 'D'");
        //sentencia.append("\n          THEN ((" + mesAntCargo1 + " + " + mesActCargo1 + ") - (" + mesAntAbono1 + " + " + mesActAbono1 + "))");
        //sentencia.append("\n          ELSE ((" + mesAntAbono1 + " + " + mesActAbono1 + ") - (" + mesAntCargo1 + " + " + mesActCargo1 + "))");
        //sentencia.append("\n        END");
        //sentencia.append("\n      ELSE (cc." + mesAntCargo1 + "+ cc." + mesActCargo1 + ")*-1");
        if (mes_act1.equals("ENE")) {
            ////sentencia.append("\n          THEN ((" + mesEneCargoIni + " + " +  mesEneCargo+  ") - (" + mesEneAbonoIni + " +  " + mesEneAbono + "))");
            sentencia.append("\n          THEN ((").append(mesEneCargoIni).append(" + ").append(mesEneCargo).append(") - (").append(mesEneAbonoIni).append(" +  ").append(mesEneAbono).append("))");
            ////sentencia.append("\n          ELSE ((" + mesEneAbonoIni + " + " + mesEneAbono + ") - (" + mesEneCargoIni + " + " + mesEneCargo + "))");
            sentencia.append("\n          ELSE ((").append(mesEneAbonoIni).append(" + ").append(mesEneAbono).append(") - (").append(mesEneCargoIni).append(" + ").append(mesEneCargo).append("))");
        } else {
            ////sentencia.append("\n          THEN ((" + mesAntCargo1 + " + " + mesActCargo1 + ") - (" + mesAntAbono1 + " + " + mesActAbono1 + "))");
            sentencia.append("\n          THEN ((").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(") - (").append(mesAntAbono1).append(" + ").append(mesActAbono1).append("))");
            ////sentencia.append("\n          ELSE ((" + mesAntAbono1 + " + " + mesActAbono1 + ") - (" + mesAntCargo1 + " + " + mesActCargo1 + "))");
            sentencia.append("\n          ELSE ((").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(") - (").append(mesAntCargo1).append(" + ").append(mesActCargo1).append("))");
        }

        sentencia.append("\n        END");
        sentencia.append("\n      ELSE");

        if (mes_act1.equals("ENE")) {
            //sentencia.append("\n      (CASE cl.naturaleza WHEN 'D' THEN ((" + mesEneCargoIni + " - " + mesEneAbonoIni + ") + " + mesEneCargo + ") ELSE " + mesEneCargo + " END)*-1 \n");
            sentencia.append("\n        (CASE cl.naturaleza");
            sentencia.append("\n            WHEN 'D'");
            ////sentencia.append("\n                THEN ((" + mesEneCargoIni + " - " + mesEneAbonoIni + ") + " + mesEneCargo + ")");
            sentencia.append("\n                THEN ((").append(mesEneCargoIni).append(" - ").append(mesEneAbonoIni).append(") + ").append(mesEneCargo).append(")");
            ////sentencia.append("\n                ELSE " + mesEneCargo);
            sentencia.append("\n                ELSE ").append(mesEneCargo);
            sentencia.append("\n            END");
            sentencia.append("\n         )*-1 ");
        } else {
            ////sentencia.append("\n      (cc." + mesAntCargo1 + "+ cc." + mesActCargo1 + ")*-1");
            sentencia.append("\n      (cc.").append(mesAntCargo1).append("+ cc.").append(mesActCargo1).append(")*-1");
        }

        sentencia.append("\n    END AS importe_calculo,");
        sentencia.append("\n    0   AS saldo_anterior");
        sentencia.append("\n  FROM rf_tr_cuentas_contables cc,");
        sentencia.append("\n    rf_tc_clasificador_cuentas cl");
        sentencia.append("\n  WHERE cc.cuenta_mayor_id             = cl.cuenta_mayor_id");
        ////sentencia.append("\n  AND cc.id_catalogo_cuenta            = " + getControlReg().getIdCatalogoCuenta());
        sentencia.append("\n  AND cc.id_catalogo_cuenta            = ").append(getControlReg().getIdCatalogoCuenta());
        ////sentencia.append("\n  AND TO_CHAR(cc.fecha_vig_ini,'yyyy') = " + getControlReg().getEjercicio());
        sentencia.append("\n  AND TO_CHAR(cc.fecha_vig_ini,'yyyy') = ").append(getControlReg().getEjercicio());
        sentencia.append("\n  AND ( cc.cuenta_contable LIKE '42__%'");

//        if (controlReg.getEjercicio()== 2012 && Integer.valueOf(mes1)< 11)
//          sentencia.append("   or   cc.cuenta_contable like '4400%'  \n");
//        else
//          sentencia.append("   or   cc.cuenta_contable like '4319%'  \n");

        sentencia.append("\n  OR cc.cuenta_contable LIKE '431_%'");
        sentencia.append("\n  OR cc.cuenta_contable LIKE '439_%'");
        sentencia.append("\n  OR cc.cuenta_contable LIKE '511_%'");
        sentencia.append("\n  OR cc.cuenta_contable LIKE '512_%'");
        sentencia.append("\n  OR cc.cuenta_contable LIKE '513_%'");
        sentencia.append("\n  OR cc.cuenta_contable LIKE '524_%'");
        sentencia.append("\n  OR cc.cuenta_contable LIKE '528_%'");
        sentencia.append("\n  OR cc.cuenta_contable LIKE '529_%'");
        sentencia.append("\n  OR cc.cuenta_contable LIKE '553_%'");
        sentencia.append("\n  OR cc.cuenta_contable LIKE '559_%'");
        sentencia.append("\n  OR cc.cuenta_contable LIKE '432_%'");
        sentencia.append("\n  OR cc.cuenta_contable LIKE '553_%' )");

        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n  AND SUBSTR(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
            ////sentencia.append("\n  AND SUBSTR(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,"+getControlReg().getEjercicio()+"),f_longitud_nivel('programa',cc.cuenta_mayor_id,"+getControlReg().getEjercicio()+")) in ("+getPrograma()+")");
        }

        ////sentencia.append("\n  AND SUBSTR(cc.cuenta_contable,f_posicion_nivel('unidad',cc.cuenta_mayor_id,"+getControlReg().getEjercicio()+"),f_longitud_nivel('unidad',cc.cuenta_mayor_id,"+getControlReg().getEjercicio()+")) = '"+getUnidad()+"' "); 
        sentencia.append("\n  AND SUBSTR(cc.cuenta_contable,f_posicion_nivel('unidad',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getUnidad()).append("' ");
        ////sentencia.append("\n  AND SUBSTR(cc.cuenta_contable,f_posicion_nivel('ambito',cc.cuenta_mayor_id,"+getControlReg().getEjercicio()+"),f_longitud_nivel('ambito',cc.cuenta_mayor_id,"+getControlReg().getEjercicio()+")) = '"+getAmbito()+"'");
        sentencia.append("\n  AND SUBSTR(cc.cuenta_contable,f_posicion_nivel('ambito',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('ambito',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getAmbito()).append("'");
        ////sentencia.append("\n  AND cc.nivel =  " + getNivel() );
        sentencia.append("\n  AND cc.nivel =  ").append(getNivel());

        sentencia.append("\n  UNION ALL");
        sentencia.append("\n  SELECT 0        AS importe_calculo,");
        ////sentencia.append("\n    "+ mesEneAbono + " AS saldo_anterior");
        sentencia.append("\n    ").append(mesEneAbono).append(" AS saldo_anterior");
        sentencia.append("\n  FROM rf_tr_cuentas_contables cc,");
        sentencia.append("\n    rf_tc_clasificador_cuentas cl");
        sentencia.append("\n  WHERE cc.cuenta_mayor_id            = cl.cuenta_mayor_id");
        ////sentencia.append("\n  AND cc.id_catalogo_cuenta           = " + getControlReg().getIdCatalogoCuenta());
        sentencia.append("\n  AND cc.id_catalogo_cuenta           = ").append(getControlReg().getIdCatalogoCuenta());
        sentencia.append("\n  AND TO_CHAR(cc.fecha_vig_ini,'yyyy')= ").append(getControlReg().getEjercicio());
        sentencia.append("\n  AND cc.cuenta_contable LIKE '322_%'");

        if (!getPrograma().equals("'0000'")) {
            ////sentencia.append("\n  AND SUBSTR(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,"+getControlReg().getEjercicio()+"),f_longitud_nivel('programa',cc.cuenta_mayor_id,"+getControlReg().getEjercicio()+")) in ("+getPrograma()+")");
            sentencia.append("\n  AND SUBSTR(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }

        ////sentencia.append("\n  AND SUBSTR(cc.cuenta_contable,f_posicion_nivel('unidad',cc.cuenta_mayor_id,"+getControlReg().getEjercicio()+"),f_longitud_nivel('unidad',cc.cuenta_mayor_id,"+getControlReg().getEjercicio()+")) = '"+getUnidad()+"' "); 
        sentencia.append("\n  AND SUBSTR(cc.cuenta_contable,f_posicion_nivel('unidad',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getUnidad()).append("' ");
        ////sentencia.append("\n  AND SUBSTR(cc.cuenta_contable,f_posicion_nivel('ambito',cc.cuenta_mayor_id,"+getControlReg().getEjercicio()+"),f_longitud_nivel('ambito',cc.cuenta_mayor_id,"+getControlReg().getEjercicio()+")) = '"+getAmbito()+"'");
        sentencia.append("\n  AND SUBSTR(cc.cuenta_contable,f_posicion_nivel('ambito',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('ambito',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getAmbito()).append("'");
        ////sentencia.append("\n  AND cc.nivel =  " + getNivel() );
        sentencia.append("\n  AND cc.nivel =  ").append(getNivel());
        sentencia.append("\n  )");

         return sentencia;
    }

    public StringBuffer estadoActividades(String fecha_consolidacion, String mes1, String mes2, String ejercicioCompara) {
        Fecha fechaPeriodo = new Fecha(fecha_consolidacion, "/");
        String mes_act1 = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodo.getMes() - 1);
        fechaPeriodo.addMeses(-1);
        String mes_ant1 = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodo.getMes() - 1);
        int diasMes = sia.libs.formato.Fecha.getDiasEnElMes(Integer.valueOf(ejercicioCompara), Integer.valueOf(mes2) - 1);
        Fecha fechaPeriodoAnt = new Fecha(ejercicioCompara + mes2 + String.valueOf(diasMes), "/");
        String mes_act2 = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodoAnt.getMes() - 1);
        fechaPeriodoAnt.addMeses(-1);
        String mes_ant2 = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodoAnt.getMes() - 1);
        StringBuffer sentencia = new StringBuffer();
        String mesAntCargo1;
        String mesAntAbono1;
        String mesActCargo1;
        String mesActAbono1;
        String mesAntCargo2;
        String mesAntAbono2;
        String mesActCargo2 = null;
        String mesActAbono2 = null;
        setNivel(!getPrograma().equals("'0000'") && getUnidad().equals("0000") && getAmbito().equals("0000") ? "3" : getNivel());


        if (mes_ant1.equals("DIC")) {
            mesAntCargo2 = construirMes(mes_act1, "cargo_ini");
            mesAntAbono2 = construirMes(mes_act1, "abono_ini");
            mesAntCargo1 = construirMes(mes_act1, "cargo_ini");
            mesAntAbono1 = construirMes(mes_act1, "abono_ini");
        } else {
            if (mes_act2.equals("DIC")) {
                mesAntCargo2 = "ene_cargo_ini";
                mesAntAbono2 = "ene_abono_ini";
            } else {
                mesAntCargo2 = construirMes(mes_act2, "cargo_acum");
                mesAntAbono2 = construirMes(mes_act2, "abono_acum");
            }
            mesAntCargo1 = construirMes(mes_ant1, "cargo_acum");
            mesAntAbono1 = construirMes(mes_ant1, "abono_acum");
        }
        mesActCargo1 = construirMes(mes_act1, "cargo");
        mesActAbono1 = construirMes(mes_act1, "abono");

        sentencia.append("\n SELECT CASE WHEN LENGTH(N1)>2 THEN N1 ELSE '' END AS N1, DESCRIPCION, SALDO_ANTERIOR, SALDO_ACTUAL, VARIACION, PORCENTAJE FROM (");
        sentencia.append("\n SELECT n1, descripcion,  saldo_anterior,  saldo_actual,");
        sentencia.append("\n       (saldo_actual-saldo_anterior) AS variacion,");
        sentencia.append("\n       (CASE WHEN (saldo_anterior- saldo_actual)!= 0");
        sentencia.append("\n           THEN CASE WHEN saldo_anterior != 0 ");
        sentencia.append("\n                 THEN ROUND( ( ((saldo_anterior- saldo_actual) / saldo_anterior)* 100), 2) ");
        sentencia.append("\n                 ELSE CASE WHEN (saldo_anterior- saldo_actual)>0 THEN 100 ELSE -100 ");
        sentencia.append("\n               END");
        sentencia.append("\n             END ");
        sentencia.append("\n         ELSE 0 ");
        sentencia.append("\n       END )*-1 AS porcentaje ");
        sentencia.append("\n FROM ( ");
        sentencia.append("\n SELECT tb2.n1, tb2.descripcion, sum(tb2.saldo_anterior) saldo_anterior,  sum(tb2.saldo_actual) saldo_actual FROM (  ");
        sentencia.append("\n	SELECT tb.n1, ");
        sentencia.append("\n            ( SELECT initcap(descripcion) AS descripcion ");
        sentencia.append("\n                FROM rf_tc_clase_clasif_cta ");
        sentencia.append("\n              WHERE id_genero||id_grupo||id_clase = tb.n1 ) AS descripcion, ");
        sentencia.append("\n					(tb.saldo_anterior    ) AS saldo_anterior, ");
        sentencia.append("\n					(tb.saldo_actual      ) AS saldo_actual ");
        sentencia.append("\n	FROM  ");
        sentencia.append("\n	(	--BLOQUE1-- ");
        sentencia.append("\n  SELECT    SUBSTR(cc.cuenta_contable, 1, 3) n1, ");
        sentencia.append("\n            CASE cl.naturaleza  WHEN 'C'  ");
        sentencia.append("\n                 THEN (( ").append(mesAntCargo2).append(" ) - ( ").append(mesAntAbono2).append("  )) ");
        sentencia.append("\n                 ELSE (( ").append(mesAntAbono2).append(" ) - ( ").append(mesAntCargo2).append("  )) ");
        sentencia.append("\n                 END ");
        sentencia.append("\n             AS saldo_anterior, ");
        sentencia.append("\n             CASE cl.naturaleza WHEN 'C' ");
        sentencia.append("\n                 THEN (( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ) - ( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" )) ");
        sentencia.append("\n                 ELSE (( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ) - ( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" )) ");
        sentencia.append("\n                            END ");
        sentencia.append("\n             AS saldo_actual ");
        sentencia.append("\n		FROM 	rf_tr_cuentas_contables     cc, ");
        sentencia.append("\n          rf_tc_clasificador_cuentas  cl ");
        sentencia.append("\n		WHERE cc.cuenta_mayor_id  = cl.cuenta_mayor_id ");
        sentencia.append("\n		AND cc.id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta()).append(" ");
        sentencia.append("\n		AND extract(year from cc.fecha_vig_ini) = ").append(controlReg.getEjercicio()).append("  ");
        sentencia.append("\n		AND (    cc.cuenta_contable LIKE '311_%' ");
        sentencia.append("\n                    OR cc.cuenta_contable LIKE '431_%'  ");
        sentencia.append("\n                    OR cc.cuenta_contable LIKE '439_%') ");

        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getAmbito()).append("' ");
        sentencia.append("\n     AND cc.nivel = ").append(getNivel());
        
        sentencia.append("\n--ANEXO");
        sentencia.append("\n     UNION ALL");
        sentencia.append("\n  SELECT    SUBSTR(cc.cuenta_contable, 1, 3) n1, ");
        sentencia.append("\n            CASE cl.naturaleza  WHEN 'D'  ");
        sentencia.append("\n                 THEN (( ").append(mesAntCargo2).append(" ) - ( ").append(mesAntAbono2).append("  )) ");
        sentencia.append("\n                 ELSE (( ").append(mesAntAbono2).append(" ) - ( ").append(mesAntCargo2).append("  )) ");
        sentencia.append("\n                 END ");
        sentencia.append("\n             AS saldo_anterior, ");
        sentencia.append("\n             CASE cl.naturaleza WHEN 'D' ");
        sentencia.append("\n                 THEN (( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ) - ( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" )) ");
        sentencia.append("\n                 ELSE (( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ) - ( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" )) ");
        sentencia.append("\n                            END ");
        sentencia.append("\n             AS saldo_actual ");
        sentencia.append("\n		FROM 	rf_tr_cuentas_contables     cc, ");
        sentencia.append("\n          rf_tc_clasificador_cuentas  cl ");
        sentencia.append("\n		WHERE cc.cuenta_mayor_id  = cl.cuenta_mayor_id ");
        sentencia.append("\n		AND cc.id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta()).append(" ");
        sentencia.append("\n		AND extract(year from cc.fecha_vig_ini) = ").append(controlReg.getEjercicio()).append("  ");
        sentencia.append("\n		AND (      cc.cuenta_contable LIKE '511_%'  ");
        sentencia.append("\n                    OR cc.cuenta_contable LIKE '512_%'  ");
        sentencia.append("\n                    OR cc.cuenta_contable LIKE '513_%'  ");
        sentencia.append("\n                    OR cc.cuenta_contable LIKE '524_%'  ");
        sentencia.append("\n                    OR cc.cuenta_contable LIKE '528_%'  ");
        sentencia.append("\n                    OR cc.cuenta_contable LIKE '529_%'  ");
        sentencia.append("\n                    OR cc.cuenta_contable LIKE '551_%') ");

        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getAmbito()).append("' ");
        sentencia.append("\n     AND cc.nivel = ").append(getNivel());
        
        sentencia.append("\n     UNION ALL");
        sentencia.append("\n     SELECT '511' n1,");
        sentencia.append("\n     CASE cl.naturaleza");
        sentencia.append("\n        WHEN 'D'");
        sentencia.append("\n                 THEN (( ").append(mesAntCargo2).append(" ) - ( ").append(mesAntAbono2).append("  )) ");
        sentencia.append("\n                 ELSE (( ").append(mesAntAbono2).append(" ) - ( ").append(mesAntCargo2).append("  )) ");
        sentencia.append("\n     END");
        sentencia.append("\n        AS saldo_anterior,");
        sentencia.append("\n     CASE cl.naturaleza");
        sentencia.append("\n        WHEN 'D'");
        sentencia.append("\n                 THEN (( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ) - ( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" )) ");
        sentencia.append("\n                 ELSE (( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ) - ( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" )) ");
        sentencia.append("\n     END");
        sentencia.append("\n        AS saldo_actual");
        sentencia.append("\n     FROM rf_tr_cuentas_contables cc,");
        sentencia.append("\n     rf_tc_clasificador_cuentas cl");
        sentencia.append("\n     WHERE cc.cuenta_mayor_id = cl.cuenta_mayor_id");
        sentencia.append("\n     AND cc.id_catalogo_cuenta = 1");
        sentencia.append("\n		AND extract(year from cc.fecha_vig_ini) = ").append(controlReg.getEjercicio()).append("  ");
        sentencia.append("\n	 AND ( cc.cuenta_contable LIKE '599_%')");
        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getAmbito()).append("' ");
        sentencia.append("\n     AND cc.nivel = ").append(getNivel());
        sentencia.append("\n     UNION ALL");
        sentencia.append("\n     SELECT '513' n1,");
        sentencia.append("\n     CASE cl.naturaleza");
        sentencia.append("\n        WHEN 'D'");
        sentencia.append("\n                 THEN (( ").append(mesAntCargo2).append(" ) - ( ").append(mesAntAbono2).append("  )) ");
        sentencia.append("\n                 ELSE (( ").append(mesAntAbono2).append(" ) - ( ").append(mesAntCargo2).append("  )) ");
        sentencia.append("\n     END");
        sentencia.append("\n        AS saldo_anterior,");
        sentencia.append("\n     CASE cl.naturaleza");
        sentencia.append("\n        WHEN 'D'");
        sentencia.append("\n                 THEN (( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ) - ( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" )) ");
        sentencia.append("\n                 ELSE (( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ) - ( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" )) ");
        sentencia.append("\n     END");
        sentencia.append("\n        AS saldo_actual");
        sentencia.append("\n     FROM rf_tr_cuentas_contables cc,");
        sentencia.append("\n     rf_tc_clasificador_cuentas cl");
        sentencia.append("\n     WHERE cc.cuenta_mayor_id = cl.cuenta_mayor_id");
        sentencia.append("\n     AND cc.id_catalogo_cuenta = 1");
        sentencia.append("\n		AND extract(year from cc.fecha_vig_ini) = ").append(controlReg.getEjercicio()).append("  ");
        sentencia.append("\n	 AND ( cc.cuenta_contable LIKE '518_%')");
        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getAmbito()).append("' ");
        sentencia.append("\n     AND cc.nivel = ").append(getNivel());
        sentencia.append("\n     --FIN DEL ANEXO");
        sentencia.append("\n  ) tb ) tb2 ");
        sentencia.append("\nGROUP BY    tb2.n1, tb2.descripcion ) ");
        sentencia.append("\nUNION ALL ");

        //-----------------------------------------------------------
        //--- INICIO TOTAL DE INGRESOS
        //-----------------------------------------------------------

        sentencia.append("\nSELECT '4999' AS n1,  ");
        sentencia.append("\n'TOTAL INGRESOS'    AS descripcion,  ");
        sentencia.append("\nSUM(saldo_anterior) AS saldo_anterior,  ");
        sentencia.append("\nSUM(saldo_actual)   AS saldo_actual,  ");
        sentencia.append("\n NULL AS variacion, ");
        sentencia.append("\n NULL AS porcentaje ");
        sentencia.append("\nFROM  ");
        sentencia.append("\n ( SELECT CASE cl.naturaleza  ");
        sentencia.append("\n			WHEN 'C'  ");
        sentencia.append("\n		     THEN (( ").append(mesAntCargo2).append(" ) - ( ").append(mesAntAbono2).append(" ))  ");
        sentencia.append("\n		     ELSE (( ").append(mesAntAbono2).append(" ) - ( ").append(mesAntCargo2).append(" ))  ");
        sentencia.append("\n		END AS saldo_anterior,  ");
        sentencia.append("\n		CASE cl.naturaleza  ");
        sentencia.append("\n			WHEN 'C'  ");
        sentencia.append("\n				THEN (( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ) - ( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ))  ");
        sentencia.append("\n				ELSE (( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ) - ( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ))  ");
        sentencia.append("\n		 END AS saldo_actual  ");
        sentencia.append("\n	FROM 	rf_tr_cuentas_contables 	cc,  ");
        sentencia.append("\n			rf_tc_clasificador_cuentas 	cl  ");
        sentencia.append("\n	WHERE 	cc.cuenta_mayor_id	  = cl.cuenta_mayor_id  ");
        sentencia.append("\n		AND cc.id_catalogo_cuenta = 1  ");
        sentencia.append("\n		AND extract(year from cc.fecha_vig_ini) = ").append(controlReg.getEjercicio()).append("  ");
        sentencia.append("\n		AND (      cc.cuenta_contable LIKE '311_%'  ");
        sentencia.append("\n                    OR cc.cuenta_contable LIKE '431_%'  ");
        sentencia.append("\n                    OR cc.cuenta_contable LIKE '439_%') ");
        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getAmbito()).append("' ");
        sentencia.append("\n     AND cc.nivel = ").append(getNivel());
        sentencia.append("\n	) ");
        sentencia.append("\nUNION ALL  ");

        //-----------------------------------------------------------
        //--- INICIO TOTAL DE GASTOS U OTRAS PERDIDAD
        //-----------------------------------------------------------

        sentencia.append("\nSELECT 	'5399' 								AS n1,  ");
        sentencia.append("\n		'TOTAL DE GASTOS'  AS descripcion,  ");
        sentencia.append("\n		SUM(saldo_anterior)  				AS saldo_anterior,  ");
        sentencia.append("\n		SUM(saldo_actual) 					AS saldo_actual,  ");
        sentencia.append("\n        NULL AS variacion, ");
        sentencia.append("\n        NULL AS porcentaje ");
        sentencia.append("\nFROM  ");
        sentencia.append("\n ( SELECT	CASE cl.naturaleza  ");
        sentencia.append("\n		WHEN 'D'  ");
        sentencia.append("\n		     THEN (( ").append(mesAntCargo2).append(" ) - ( ").append(mesAntAbono2).append(" ))  ");
        sentencia.append("\n		     ELSE (( ").append(mesAntAbono2).append(" ) - ( ").append(mesAntCargo2).append(" ))  ");
        sentencia.append("\n	         END AS saldo_anterior,  ");
        sentencia.append("\n	CASE cl.naturaleza  WHEN 'D'  ");
        sentencia.append("\n			THEN (( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ) - ( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ))  ");
        sentencia.append("\n			ELSE (( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ) - ( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ))  ");
        sentencia.append("\n		END AS saldo_actual  ");
        sentencia.append("\n	FROM rf_tr_cuentas_contables cc,  ");
        sentencia.append("\n		 rf_tc_clasificador_cuentas cl  ");
        sentencia.append("\n	WHERE cc.cuenta_mayor_id = cl.cuenta_mayor_id  ");
        sentencia.append("\n		AND cc.id_catalogo_cuenta = 1  ");
        sentencia.append("\n		AND extract(year from cc.fecha_vig_ini) = ").append(controlReg.getEjercicio()).append("  ");
        sentencia.append("\n		AND (      cc.cuenta_contable LIKE '511_%'   ");
        sentencia.append("\n                    OR cc.cuenta_contable LIKE '512_%'  ");
        sentencia.append("\n                    OR cc.cuenta_contable LIKE '513_%'  ");
        sentencia.append("\n                    OR cc.cuenta_contable LIKE '518_%'  ");
        sentencia.append("\n                    OR cc.cuenta_contable LIKE '524_%'  ");
        sentencia.append("\n                    OR cc.cuenta_contable LIKE '528_%'  ");
        sentencia.append("\n                    OR cc.cuenta_contable LIKE '529_%'  ");
        sentencia.append("\n                    OR cc.cuenta_contable LIKE '599_%') ");
        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getAmbito()).append("' ");
        sentencia.append("\n     AND cc.nivel = ").append(getNivel());
        sentencia.append("\n	) ");
        sentencia.append("\nUNION ALL  ");


        //-----------------------------------------------------------
        //--- INICIO AHORRO/DESHARRO DEL EJERCICIO
        //-----------------------------------------------------------

        sentencia.append("\nSELECT '5499' AS n1, ");
        sentencia.append("\n'AHORRO / (DESAHORRO) ANTES DE DEPRECIACIÓN' AS descripcion, ");
        sentencia.append("\n SUM(saldo_anterior)                    AS saldo_anterior, ");
        sentencia.append("\n SUM(saldo_actual)                      AS saldo_actual, ");
        sentencia.append("\n NULL AS variacion, ");
        sentencia.append("\n NULL AS porcentaje ");
        sentencia.append("\nFROM ");
        sentencia.append("\n ( ");
        sentencia.append("\n     SELECT CASE cl.naturaleza");
        sentencia.append("\n         WHEN 'C' ");
        sentencia.append("\n		     THEN (( ").append(mesAntCargo2).append(" ) - ( ").append(mesAntAbono2).append(" ))  ");
        sentencia.append("\n		     ELSE (( ").append(mesAntAbono2).append(" ) - ( ").append(mesAntCargo2).append(" ))  ");
        sentencia.append("\n         END");
        sentencia.append("\n          AS saldo_anterior,");
        sentencia.append("\n       CASE cl.naturaleza");
        sentencia.append("\n         WHEN 'C'");
        sentencia.append("\n			THEN (( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ) - ( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ))  ");
        sentencia.append("\n			ELSE (( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ) - ( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ))  ");
        sentencia.append("\n		END AS saldo_actual ");
        sentencia.append("\n	FROM rf_tr_cuentas_contables cc, ");
        sentencia.append("\n		 rf_tc_clasificador_cuentas cl ");
        sentencia.append("\n	WHERE cc.cuenta_mayor_id = cl.cuenta_mayor_id ");
        sentencia.append("\n		AND cc.id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta()).append(" ");
        sentencia.append("\n		AND extract(year from cc.fecha_vig_ini) = ").append(controlReg.getEjercicio()).append(" ");
        sentencia.append("\n	   AND (   cc.cuenta_contable LIKE '311_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '431_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '439_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '511_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '512_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '513_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '518_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '524_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '528_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '529_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '599_%')");
        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getAmbito()).append("' ");
        sentencia.append("\n     AND cc.nivel = ").append(getNivel());
        sentencia.append("\n	) ");
        //-----------------------------------------------------------
        //--- INICIO AHORRO/DESHARRO DEL EJERCICIO
        //-----------------------------------------------------------

        sentencia.append("\nUNION ALL ");
        sentencia.append("\nSELECT '9999' AS n1, ");
        sentencia.append("\n'AHORRO / (DESAHORRO) DEL EJERCICIO' AS descripcion, ");
        sentencia.append("\n SUM(saldo_anterior)                    AS saldo_anterior, ");
        sentencia.append("\n SUM(saldo_actual)                      AS saldo_actual, ");
        sentencia.append("\n NULL AS variacion, ");
        sentencia.append("\n NULL AS porcentaje ");
        sentencia.append("\nFROM ");
        sentencia.append("\n ( ");
        sentencia.append("\n     SELECT CASE cl.naturaleza");
        sentencia.append("\n         WHEN 'C' ");
        sentencia.append("\n		     THEN (( ").append(mesAntCargo2).append(" ) - ( ").append(mesAntAbono2).append(" ))  ");
        sentencia.append("\n		     ELSE (( ").append(mesAntAbono2).append(" ) - ( ").append(mesAntCargo2).append(" ))  ");
        sentencia.append("\n         END");
        sentencia.append("\n          AS saldo_anterior,");
        sentencia.append("\n       CASE cl.naturaleza");
        sentencia.append("\n         WHEN 'C'");
        sentencia.append("\n			THEN (( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ) - ( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ))  ");
        sentencia.append("\n			ELSE (( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ) - ( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ))  ");
        sentencia.append("\n		END AS saldo_actual ");
        sentencia.append("\n	FROM rf_tr_cuentas_contables cc, ");
        sentencia.append("\n		 rf_tc_clasificador_cuentas cl ");
        sentencia.append("\n	WHERE cc.cuenta_mayor_id = cl.cuenta_mayor_id ");
        sentencia.append("\n		AND cc.id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta()).append(" ");
        sentencia.append("\n		AND extract(year from cc.fecha_vig_ini) = ").append(controlReg.getEjercicio()).append(" ");
        sentencia.append("\n	   AND (   cc.cuenta_contable LIKE '311_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '431_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '439_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '511_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '512_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '513_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '518_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '524_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '528_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '529_%'");
        sentencia.append("\n            OR cc.cuenta_contable LIKE '551_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '599_%')");
        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getAmbito()).append("' ");
        sentencia.append("\n     AND cc.nivel = ").append(getNivel());
        sentencia.append("\n )--CIERRE FROM "); 
        sentencia.append("\nWHERE (saldo_actual!= 0  ");
        sentencia.append("\nOR saldo_anterior  != 0) ");
        sentencia.append("\nUNION ALL  ");
        sentencia.append("\nSELECT DISTINCT t.n1,  ");
        sentencia.append("\nt.descripcion, ");
        sentencia.append("\nNULL AS saldo_anterior, ");
        sentencia.append("\nNULL AS saldo_actual, ");
        sentencia.append("\nNULL AS variacion, ");
        sentencia.append("\nNULL AS porcentaje ");
        sentencia.append("\nFROM  ");
        sentencia.append("\n(	SELECT  SUBSTR(cc.cuenta_contable, 1, 1) n1, ");
        sentencia.append("\n            SUBSTR(cc.cuenta_contable, 1, 2) n2, ");
        sentencia.append("\n		CASE MAX(cl.naturaleza) ");
        sentencia.append("\n		WHEN 'C' ");
        sentencia.append("\n		     THEN (SUM( ").append(mesAntCargo2).append(" ) - SUM( ").append(mesAntAbono2).append(" ))  ");
        sentencia.append("\n		     ELSE (SUM( ").append(mesAntAbono2).append(" ) - SUM( ").append(mesAntCargo2).append(" ))  ");
        sentencia.append("\n		END AS saldo_anterior,  ");
        sentencia.append("\n		CASE MAX(cl.naturaleza) ");
        sentencia.append("\n		WHEN 'C' ");
        sentencia.append("\n			THEN (( SUM(").append(mesAntCargo1).append(" ) + SUM(").append(mesActCargo1).append(") ) - ( SUM(").append(mesAntAbono1).append(" ) + SUM(").append(mesActAbono1).append(")))");
        sentencia.append("\n			ELSE (( SUM(").append(mesAntAbono1).append(" ) + SUM(").append(mesActAbono1).append(") ) - ( SUM(").append(mesAntCargo1).append(" ) + SUM(").append(mesActCargo1).append(")))");
        sentencia.append("\n		END AS saldo_actual ");
        sentencia.append("\n	FROM    rf_tr_cuentas_contables cc, ");
        sentencia.append("\n		rf_tc_clasificador_cuentas cl ");
        sentencia.append("\n	WHERE cc.cuenta_mayor_id  = cl.cuenta_mayor_id ");
        sentencia.append("\n	AND   extract(year from cc.fecha_vig_ini) = ").append(controlReg.getEjercicio()).append("  ");
        sentencia.append("\n		AND (    cc.cuenta_contable LIKE '311_%' ");
        sentencia.append("\n                    OR cc.cuenta_contable LIKE '51_%'  ");
        sentencia.append("\n                    OR cc.cuenta_contable LIKE '55_%'  ) ");

        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getAmbito()).append("' ");
        sentencia.append("\n     AND cc.nivel = ").append(getNivel());
        sentencia.append("\n	GROUP BY    SUBSTR(cc.cuenta_contable, 1, 1), ");
        sentencia.append("\n                SUBSTR(cc.cuenta_contable, 1, 2) ");
        sentencia.append("\n) d, ");
        sentencia.append("\n(	SELECT TO_CHAR(id_genero) AS n1, ");
        sentencia.append("\n			   CASE WHEN ID_GENERO = 3 THEN 'INGRESOS' ELSE descripcion END AS descripcion, ");
        sentencia.append("\n				 NULL  AS saldo_anterior, ");
        sentencia.append("\n			         NULL  AS saldo_actual ");
        sentencia.append("\n		FROM rf_tc_genero_clasf_cta ");
        sentencia.append("\n	WHERE id_genero IN (3, 5) ");
        sentencia.append("\nUNION  ");
        sentencia.append("\n	SELECT id_genero || id_grupo  AS n1, ");
        sentencia.append("\n	                   descripcion AS descripcion, ");
        sentencia.append("\n                              NULL AS saldo_anterior, ");
        sentencia.append("\n                              NULL AS saldo_actual ");
        sentencia.append("\n	FROM rf_tc_grupo_clasf_cta ");
        sentencia.append("\n	WHERE id_genero || id_grupo IN ('51', '55') ");
        sentencia.append("\n  ) t ");
        sentencia.append("\nWHERE (d.saldo_actual!= 0 ");
        sentencia.append("\nOR d.saldo_anterior  != 0)");
        sentencia.append("\nAND   (    d.n1 = t.n1   ");
        sentencia.append("\n        OR d.n2 = t.n1 ) ");
        sentencia.append("\nORDER BY n1, descripcion ) ");
        System.out.println("**ESTADO DE ACTIVIDADES\n"+sentencia.toString());
       mes_ant2 = null;
        return sentencia;
    }

    public StringBuffer estadoActividadesDetallado(String fecha_consolidacion, String mes1, String mes2, String ejercicioCompara) {
        Fecha fechaPeriodo = new Fecha(fecha_consolidacion, "/");
        String mes_act1 = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodo.getMes() - 1);
        fechaPeriodo.addMeses(-1);
        String mes_ant1 = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodo.getMes() - 1);
        int diasMes = sia.libs.formato.Fecha.getDiasEnElMes(Integer.valueOf(ejercicioCompara), Integer.valueOf(mes2) - 1);
        Fecha fechaPeriodoAnt = new Fecha(ejercicioCompara + mes2 + String.valueOf(diasMes), "/");
        String mes_act2 = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodoAnt.getMes() - 1);
        fechaPeriodoAnt.addMeses(-1);
        String mes_ant2 = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodoAnt.getMes() - 1);
        StringBuffer sentencia = new StringBuffer();
        String mesAntCargo1;
        String mesAntAbono1;
        String mesActCargo1;
        String mesActAbono1;
        String mesAntCargo2;
        String mesAntAbono2;
        String mesActCargo2 = null;
        String mesActAbono2 = null;

        setNivel(!getPrograma().equals("'0000'") && getUnidad().equals("0000") && getAmbito().equals("0000") ? "3" : getNivel());

        if (mes_ant1.equals("DIC")) {
            mesAntCargo2 = construirMes(mes_act1, "cargo_ini");
            mesAntAbono2 = construirMes(mes_act1, "abono_ini");
            mesAntCargo1 = construirMes(mes_act1, "cargo_ini");
            mesAntAbono1 = construirMes(mes_act1, "abono_ini");
        } else {
            if (mes_act2.equals("DIC")) {
                mesAntCargo2 = "ene_cargo_ini";
                mesAntAbono2 = "ene_abono_ini";
            } else {
                mesAntCargo2 = construirMes(mes_act2, "cargo_acum");
                mesAntAbono2 = construirMes(mes_act2, "abono_acum");
            }
            mesAntCargo1 = construirMes(mes_ant1, "cargo_acum");
            mesAntAbono1 = construirMes(mes_ant1, "abono_acum");
        }
        mesActCargo1 = construirMes(mes_act1, "cargo");
        mesActAbono1 = construirMes(mes_act1, "abono");

        sentencia.append("\n SELECT n1, descripcion,  saldo_anterior,  saldo_actual,");
        sentencia.append("\n       (saldo_actual-saldo_anterior) AS variacion,");
        sentencia.append("\n       (CASE WHEN (saldo_anterior- saldo_actual)!= 0");
        sentencia.append("\n           THEN CASE WHEN saldo_anterior != 0 ");
        sentencia.append("\n                 THEN ROUND( ( ((saldo_anterior- saldo_actual) / saldo_anterior)* 100), 2) ");
        sentencia.append("\n                 ELSE CASE WHEN (saldo_anterior- saldo_actual)>0 THEN 100 ELSE -100 ");
        sentencia.append("\n               END");
        sentencia.append("\n             END ");
        sentencia.append("\n         ELSE 0 ");
        sentencia.append("\n       END )*-1 AS porcentaje ");
        sentencia.append("\n FROM ( ");
        sentencia.append("\n SELECT tb2.n1, tb2.descripcion, sum(tb2.saldo_anterior) saldo_anterior,  sum(tb2.saldo_actual) saldo_actual FROM (  ");
        sentencia.append("\n	SELECT tb.n1, ");
        sentencia.append("\n            ( SELECT initcap(descripcion) AS descripcion ");
        sentencia.append("\n                FROM rf_tc_clasificador_cuentas tc  ");
        sentencia.append("\n              WHERE  tc.cuenta_mayor = tb.n1 ) AS descripcion, ");
        sentencia.append("\n					(tb.saldo_anterior    ) AS saldo_anterior, ");
        sentencia.append("\n					(tb.saldo_actual      ) AS saldo_actual ");
        sentencia.append("\n	FROM  ");
        sentencia.append("\n	(	--BLOQUE1-- ");
        sentencia.append("\n  SELECT    SUBSTR(cc.cuenta_contable, 1, 4) n1, ");
        sentencia.append("\n                            CASE cl.naturaleza  ");
        sentencia.append("\n                              WHEN 'D'  ");
        sentencia.append("\n                 THEN (( ").append(mesAntCargo2).append(" ) - ( ").append(mesAntAbono2).append("  )) ");
        sentencia.append("\n                 ELSE (( ").append(mesAntAbono2).append(" ) - ( ").append(mesAntCargo2).append("  )) ");
        sentencia.append("\n                 END ");
        sentencia.append("\n             AS saldo_anterior, ");
        sentencia.append("\n                            CASE cl.naturaleza ");
        sentencia.append("\n                              WHEN 'D' ");
        sentencia.append("\n                 THEN (( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ) - ( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" )) ");
        sentencia.append("\n                 ELSE (( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ) - ( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" )) ");
        sentencia.append("\n                            END ");
        sentencia.append("\n             AS saldo_actual ");
        sentencia.append("\n		FROM 	rf_tr_cuentas_contables     cc, ");
        sentencia.append("\n                    rf_tc_clasificador_cuentas  cl ");
        sentencia.append("\n		WHERE cc.cuenta_mayor_id  = cl.cuenta_mayor_id ");
        sentencia.append("\n		AND cc.id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta()).append(" ");
        sentencia.append("\n		AND extract(year from cc.fecha_vig_ini) = ").append(controlReg.getEjercicio()).append("  ");
        sentencia.append("\n		AND (    cc.cuenta_contable LIKE '4_%' ");
        sentencia.append("\n                    OR cc.cuenta_contable LIKE '41_%'  ");
        sentencia.append("\n                    OR cc.cuenta_contable LIKE '412_%' ");
        sentencia.append("\n                    OR cc.cuenta_contable LIKE '42__%' ");
        sentencia.append("\n                    OR cc.cuenta_contable LIKE '422_%' ");
        sentencia.append("\n                    OR cc.cuenta_contable LIKE '43_%'  ");
        sentencia.append("\n                    OR cc.cuenta_contable LIKE '431_%' ");
        sentencia.append("\n                    OR cc.cuenta_contable LIKE '5_%'   ");
        sentencia.append("\n                    OR cc.cuenta_contable LIKE '51_%'  ");
        sentencia.append("\n                    OR cc.cuenta_contable LIKE '511_%' ");
        sentencia.append("\n                    OR cc.cuenta_contable LIKE '512_%' ");
        sentencia.append("\n                    OR cc.cuenta_contable LIKE '513_%' ");
        sentencia.append("\n                    OR cc.cuenta_contable LIKE '55_%'  ");
        sentencia.append("\n                    OR cc.cuenta_contable LIKE '551_%'  ) ");
        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getAmbito()).append("' ");
        sentencia.append("\n     AND cc.nivel = ").append(getNivel());
        sentencia.append("\n  ) tb ) tb2 ");
        sentencia.append("\nGROUP BY    tb2.n1, tb2.descripcion ");
        sentencia.append("\nUNION ALL ");

        //-----------------------------------------------------------
        //--- INICIO AHORRO/DESHARRO DEL EJERCICIO
        //-----------------------------------------------------------
        sentencia.append("\nSELECT '9999' AS n1, ");
        sentencia.append("\n'AHORRO / (DESAHORRO) DEL EJERCICIO' AS descripcion,        ");
        sentencia.append("\n SUM(saldo_anterior)                    AS saldo_anterior,  ");
        sentencia.append("\n SUM(saldo_actual)                      AS saldo_actual     ");
        sentencia.append("\nFROM ");
        sentencia.append("\n(   SELECT  ");
        sentencia.append("\n		CASE cl.naturaleza ");
        sentencia.append("\n		WHEN 'D' ");
        sentencia.append("\n                THEN DECODE(SUBSTR(cc.cuenta_contable, 1, 1), '4', ((cc.").append(mesAntCargo2).append(") - ( cc.").append(mesAntAbono2).append(")), (( cc.").append(mesAntCargo2).append(") - (cc.").append(mesAntAbono2).append("))* -1)");
        sentencia.append("\n                ELSE DECODE(SUBSTR(cc.cuenta_contable, 1, 1), '4', ((cc.").append(mesAntAbono2).append(") - ( cc.").append(mesAntCargo2).append(" )), (( cc.").append(mesAntAbono2).append(") - (cc.").append(mesAntCargo2).append("))* -1)");
        sentencia.append("\n		END AS saldo_anterior, ");
        sentencia.append("\n		CASE cl.naturaleza ");
        sentencia.append("\n			WHEN 'D' ");
        sentencia.append("\n			THEN DECODE(SUBSTR(cc.cuenta_contable, 1, 1), '4', ((cc.").append(mesAntCargo1).append(" + cc.").append(mesActCargo1).append(") - ( cc.").append(mesAntAbono1).append(" + cc.").append(mesActAbono1).append(")), (( cc.").append(mesAntCargo1).append(" + cc.").append(mesActCargo1).append(") - (cc.").append(mesAntAbono1).append(" + cc.").append(mesActAbono1).append("))* -1)");
        sentencia.append("\n			ELSE DECODE(SUBSTR(cc.cuenta_contable, 1, 1), '4', ((cc.").append(mesAntAbono1).append(" + cc.").append(mesActAbono1).append(") - ( cc.").append(mesAntCargo1).append(" + cc.").append(mesActCargo1).append(")), (( cc.").append(mesAntAbono1).append(" + cc.").append(mesActAbono1).append(") - (cc.").append(mesAntCargo1).append(" + cc.").append(mesActCargo1).append("))* -1)");
        sentencia.append("\n		END AS saldo_actual ");
        sentencia.append("\n	FROM rf_tr_cuentas_contables cc, ");
        sentencia.append("\n		 rf_tc_clasificador_cuentas cl ");
        sentencia.append("\n	WHERE cc.cuenta_mayor_id = cl.cuenta_mayor_id ");
        sentencia.append("\n		AND cc.id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta()).append(" ");
        sentencia.append("\n		AND  extract(year from cc.fecha_vig_ini) = ").append(controlReg.getEjercicio()).append("  ");
        sentencia.append("\n		AND (    cc.cuenta_contable LIKE '4_%' ");
        sentencia.append("\n                    OR cc.cuenta_contable LIKE '41_%'  ");
        sentencia.append("\n                    OR cc.cuenta_contable LIKE '412_%' ");
        sentencia.append("\n                    OR cc.cuenta_contable LIKE '42__%' ");
        sentencia.append("\n                    OR cc.cuenta_contable LIKE '422_%' ");
        sentencia.append("\n                    OR cc.cuenta_contable LIKE '43_%'  ");
        sentencia.append("\n                    OR cc.cuenta_contable LIKE '431_%' ");
        sentencia.append("\n                    OR cc.cuenta_contable LIKE '5_%'   ");
        sentencia.append("\n                    OR cc.cuenta_contable LIKE '51_%'  ");
        sentencia.append("\n                    OR cc.cuenta_contable LIKE '511_%' ");
        sentencia.append("\n                    OR cc.cuenta_contable LIKE '512_%' ");
        sentencia.append("\n                    OR cc.cuenta_contable LIKE '513_%' ");
        sentencia.append("\n                    OR cc.cuenta_contable LIKE '55_%'  ");
        sentencia.append("\n                    OR cc.cuenta_contable LIKE '551_%'  ) ");
        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getAmbito()).append("' ");
        sentencia.append("\n     AND cc.nivel = ").append(getNivel());
        sentencia.append("\n ))--CIERRE FROM ");
        sentencia.append("\nWHERE (saldo_actual!= 0  ");
        sentencia.append("\nOR saldo_anterior  != 0) ");
        sentencia.append("\nUNION ALL  ");
        sentencia.append("\nSELECT DISTINCT t.n1,  ");
        sentencia.append("\nt.descripcion, ");
        sentencia.append("\nNULL AS saldo_anterior, ");
        sentencia.append("\nNULL AS saldo_actual, ");
        sentencia.append("\nNULL AS variacion, ");
        sentencia.append("\nNULL AS porcentaje ");
        sentencia.append("\nFROM  ");
        sentencia.append("\n(	SELECT  SUBSTR(cc.cuenta_contable, 1, 1) n1, ");
        sentencia.append("\n            SUBSTR(cc.cuenta_contable, 1, 2) n2, ");
        sentencia.append("\n            SUBSTR(cc.cuenta_contable, 1, 2) n3, ");
        sentencia.append("\n		CASE MAX(cl.naturaleza) ");
        sentencia.append("\n		WHEN 'D' ");
        sentencia.append("\n		     THEN (SUM( ").append(mesAntCargo2).append(" ) - SUM( ").append(mesAntAbono2).append(" ))  ");
        sentencia.append("\n		     ELSE (SUM( ").append(mesAntAbono2).append(" ) - SUM( ").append(mesAntCargo2).append(" ))  ");
        sentencia.append("\n		END AS saldo_anterior,  ");
        sentencia.append("\n		CASE MAX(cl.naturaleza) ");
        sentencia.append("\n		WHEN 'D' ");
        sentencia.append("\n			THEN (( SUM(").append(mesAntCargo1).append(" ) + SUM(").append(mesActCargo1).append(") ) - ( SUM(").append(mesAntAbono1).append(" ) + SUM(").append(mesActAbono1).append(")))");
        sentencia.append("\n			ELSE (( SUM(").append(mesAntAbono1).append(" ) + SUM(").append(mesActAbono1).append(") ) - ( SUM(").append(mesAntCargo1).append(" ) + SUM(").append(mesActCargo1).append(")))");
        sentencia.append("\n		END AS saldo_actual ");
        sentencia.append("\n	FROM    rf_tr_cuentas_contables cc, ");
        sentencia.append("\n		rf_tc_clasificador_cuentas cl ");
        sentencia.append("\n	WHERE cc.cuenta_mayor_id  = cl.cuenta_mayor_id ");
        sentencia.append("\n	AND  extract(year from cc.fecha_vig_ini) = ").append(controlReg.getEjercicio()).append("  ");
        sentencia.append("\n	AND (      cc.cuenta_contable LIKE '4_%' ");
        sentencia.append("\n          OR cc.cuenta_contable LIKE '41_%'  ");
        sentencia.append("\n          OR cc.cuenta_contable LIKE '412_%' ");
        sentencia.append("\n          OR cc.cuenta_contable LIKE '42__%' ");
        sentencia.append("\n          OR cc.cuenta_contable LIKE '422_%' ");
        sentencia.append("\n          OR cc.cuenta_contable LIKE '43_%'  ");
        sentencia.append("\n          OR cc.cuenta_contable LIKE '431_%' ");
        sentencia.append("\n          OR cc.cuenta_contable LIKE '5_%'   ");
        sentencia.append("\n          OR cc.cuenta_contable LIKE '51_%'  ");
        sentencia.append("\n          OR cc.cuenta_contable LIKE '511_%' ");
        sentencia.append("\n          OR cc.cuenta_contable LIKE '512_%' ");
        sentencia.append("\n          OR cc.cuenta_contable LIKE '513_%' ");
        sentencia.append("\n          OR cc.cuenta_contable LIKE '55_%'  ");
        sentencia.append("\n          OR cc.cuenta_contable LIKE '551_%'  ) ");
        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getAmbito()).append("' ");
        sentencia.append("\n     AND cc.nivel = ").append(getNivel());
        sentencia.append("\n	GROUP BY    SUBSTR(cc.cuenta_contable, 1, 1), ");
        sentencia.append("\n                SUBSTR(cc.cuenta_contable, 1, 2), ");
        sentencia.append("\n                SUBSTR(cc.cuenta_contable, 1, 3) ");
        sentencia.append("\n) d, ");
        sentencia.append("\n(	SELECT TO_CHAR(id_genero) AS n1, ");
        sentencia.append("\n			   descripcion AS descripcion, ");
        sentencia.append("\n				 NULL  AS saldo_anterior, ");
        sentencia.append("\n			         NULL  AS saldo_actual ");
        sentencia.append("\n		FROM rf_tc_genero_clasf_cta ");
        sentencia.append("\n	WHERE id_genero IN (4, 5) ");
        sentencia.append("\nUNION  ");
        sentencia.append("\n	SELECT id_genero || id_grupo  AS n1, ");
        sentencia.append("\n	                   descripcion AS descripcion, ");
        sentencia.append("\n                              NULL AS saldo_anterior, ");
        sentencia.append("\n                              NULL AS saldo_actual ");
        sentencia.append("\n	FROM rf_tc_grupo_clasf_cta ");
        sentencia.append("\n	WHERE id_genero || id_grupo IN ('41','42', '43', '51', '55') ");
        sentencia.append("\n  UNION ");
        sentencia.append("\n 	SELECT id_genero || id_grupo || id_clase  AS n1, ");
        sentencia.append("\n 			              descripcion AS descripcion, ");
        sentencia.append("\n 					     NULL AS saldo_anterior, ");
        sentencia.append("\n 					     NULL AS saldo_actual  ");
        sentencia.append("\n 		FROM rf_tc_clase_clasif_cta ");
        sentencia.append("\n 	WHERE id_genero || id_grupo || id_clase IN ('412', '422','431','511','512','513','551') ) t ");
        sentencia.append("\n WHERE (d.saldo_actual!= 0  ");
        sentencia.append("\n OR d.saldo_anterior  != 0) ");
        sentencia.append("\n AND   (    d.n1 = t.n1  ");
        sentencia.append("\n         OR d.n2 = t.n1 ");
        sentencia.append("\n         OR d.n3 = t.n1 ) ");
        sentencia.append("\n ORDER BY n1, descripcion ");
        mes_ant2 = null;
        //System.out.println("ESTADO DE ACTIVIDADES DETALLADO"+sentencia.toString());
        return sentencia;
    }

    public StringBuffer estadoFlujoEfectivo(String fecha_consolidacion, String mes1, String mes2, String ejercicioCompara) {
        Fecha fechaPeriodo = new Fecha(fecha_consolidacion, "/");
        String mes_act1 = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodo.getMes() - 1);
        fechaPeriodo.addMeses(-1);
        String mes_ant1 = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodo.getMes() - 1);
        int diasMes = sia.libs.formato.Fecha.getDiasEnElMes(Integer.valueOf(ejercicioCompara), Integer.valueOf(mes2) - 1);
        Fecha fechaPeriodoAnt = new Fecha(ejercicioCompara + mes2 + String.valueOf(diasMes), "/");
        String mes_act2 = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodoAnt.getMes() - 1);
        fechaPeriodoAnt.addMeses(-1);
        String mes_ant2 = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodoAnt.getMes() - 1);
        StringBuffer sentencia = new StringBuffer();
        String mesAntCargo1;
        String mesAntAbono1;
        String mesActCargo1;
        String mesActAbono1;
        String mesAntCargo2;
        String mesAntAbono2;
        String mesActCargo2;
        String mesActAbono2;
        setNivel(!getPrograma().equals("'0000'") && getUnidad().equals("0000") && getAmbito().equals("0000") ? "3" : getNivel());
        mesAntCargo1 = construirMes(mes_ant1, "cargo_acum");
        mesAntAbono1 = construirMes(mes_ant1, "abono_acum");
        mesActCargo1 = construirMes(mes_act1, "cargo");
        mesActAbono1 = construirMes(mes_act1, "abono");
        mesAntCargo2 = construirMes(mes_ant2, "cargo_acum");
        mesAntAbono2 = construirMes(mes_ant2, "abono_acum");
        mesActCargo2 = construirMes(mes_act2, "cargo");
        mesActAbono2 = construirMes(mes_act2, "abono");
        sentencia.append("\n SELECT  decode (tb2.n1,'511',1,'512',2,'513',3,'422',4,'4221',5,'4223',6) orden , ");
        sentencia.append("\n         tb2.n1, ");
        sentencia.append("\n         tb2.descripcion, ");
        sentencia.append("\n         sum(tb2.saldo_anterior) saldo_anterior, ");
        sentencia.append("\n         sum(tb2.saldo_actual  ) saldo_actual FROM (  ");
        sentencia.append("\n                          SELECT tb.n1, ");
        sentencia.append("\n                                 decode ( length(tb.n1),4,'      '||( SELECT initcap(descripcion) ");
        sentencia.append("\n                                                                         FROM rf_tc_clasificador_cuentas tc ");
        sentencia.append("\n                                                                      WHERE tc.cuenta_mayor = tb.n1 ), ");
        sentencia.append("\n                                               ( SELECT initcap(descripcion) AS descripcion ");
        sentencia.append("\n                                                   FROM rf_tc_clase_clasif_cta ");
        sentencia.append("\n                                                 WHERE id_genero||id_grupo||id_clase = tb.n1 ) ");
        sentencia.append("\n                                         )                                AS descripcion, ");
        sentencia.append("\n                                 decode(tb.n1,'422','',tb.saldo_anterior) AS saldo_anterior, ");
        sentencia.append("\n                                 decode(tb.n1,'422','',tb.saldo_actual  ) AS saldo_actual ");
        sentencia.append("\n                          FROM ");
        sentencia.append("\n                              (select '422' as n1, 0 saldo_anterior, 0 saldo_actual ");
        sentencia.append("\n                                       from dual ");
        sentencia.append("\n                               UNION ");
        sentencia.append("\n                               SELECT  decode(SUBSTR(cc.cuenta_contable, 1, 1),'4',SUBSTR(cc.cuenta_contable, 1, 4),SUBSTR(cc.cuenta_contable, 1, 3)) n1, ");
        sentencia.append("\n                                       DECODE(TO_CHAR(cc.fecha_vig_ini,'yyyy'), '").append(ejercicioCompara).append("', ");
        sentencia.append("\n                                                       CASE cl.naturaleza ");
        sentencia.append("\n                                                         WHEN 'D' ");
        sentencia.append("\n                                                           THEN (( ").append(mesAntCargo2).append(" + ").append(mesActCargo2).append(" ) - ( ").append(mesAntAbono2).append(" + ").append(mesActAbono2).append("  )) ");
        sentencia.append("\n                                                           ELSE (( ").append(mesAntAbono2).append(" + ").append(mesActAbono2).append(" ) - ( ").append(mesAntCargo2).append(" + ").append(mesActCargo2).append("  )) ");
        sentencia.append("\n                                                       END ");
        sentencia.append("\n                                       , 0) AS saldo_anterior, ");
        sentencia.append("\n                                       DECODE(TO_CHAR(cc.fecha_vig_ini,'yyyy'), '").append(controlReg.getEjercicio()).append("', ");
        sentencia.append("\n                                                       CASE cl.naturaleza ");
        sentencia.append("\n                                                         WHEN 'D' ");
        sentencia.append("\n                                                           THEN (( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ) - ( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" )) ");
        sentencia.append("\n                                                           ELSE (( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ) - ( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" )) ");
        sentencia.append("\n                                                       END ");
        sentencia.append("\n                                       , 0) AS saldo_actual ");
        sentencia.append("\n                                   FROM    rf_tr_cuentas_contables     cc, ");
        sentencia.append("\n                                           rf_tc_clasificador_cuentas  cl ");
        sentencia.append("\n                               WHERE      cc.cuenta_mayor_id  = cl.cuenta_mayor_id ");
        sentencia.append("\n                                      AND cc.id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta()).append(" ");
        sentencia.append("\n                                      AND (      extract(year from cc.fecha_vig_ini)  =").append(ejercicioCompara).append(" ");
        sentencia.append("\n                                             OR  extract(year from cc.fecha_vig_ini)  =").append(controlReg.getEjercicio()).append(" ) ");
        sentencia.append("\n                                      AND (      cc.cuenta_contable LIKE '4221%'  ");
        sentencia.append("\n                                             OR  cc.cuenta_contable LIKE '4223%'  ");
        sentencia.append("\n                                             OR  cc.cuenta_contable LIKE '511%'   ");
        sentencia.append("\n                                             OR  cc.cuenta_contable LIKE '512%'   ");
        sentencia.append("\n                                             OR  cc.cuenta_contable LIKE '513%' ) ");
        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getAmbito()).append("' ");
        sentencia.append("\n                                      AND cc.nivel = ").append(getNivel());
        sentencia.append("\n                             ) tb  ");
        sentencia.append("\n       ) tb2 ");
        sentencia.append("\n GROUP BY    tb2.n1, tb2.descripcion ");
        sentencia.append("\n order by    orden  ");
        return sentencia;
    }

    public StringBuffer cambiosSituacionFinanciera(String fecha_consolidacion, String mes, String mesCompara, String ejercicioCompara) {
        //Variables que se refieren al mes del Filtro
        String mesAntCargo1;
        String mesAntAbono1;
        String mesActCargo1;
        String mesActAbono1;
        //Variables que se refieren al mes a Comparar
        String mesAntCargo2 = null;
        String mesAntAbono2 = null;
        int ejercicio = getControlReg().getEjercicio();
        Fecha fechaPeriodo = new Fecha(fecha_consolidacion, "/");
        String mes_act1 = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodo.getMes() - 1);
        fechaPeriodo.addMeses(-1);
        String mes_ant1 = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodo.getMes() - 1);
        int diasMes = sia.libs.formato.Fecha.getDiasEnElMes(Integer.valueOf(ejercicioCompara), Integer.valueOf(mesCompara) - 1);
        Fecha fechaPeriodoAnt = new Fecha(ejercicioCompara + mesCompara + String.valueOf(diasMes), "/");
        String mes_act2 = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodoAnt.getMes() - 1);
        fechaPeriodoAnt.addMeses(-1);
        String mes_ant2 = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodoAnt.getMes() - 1);
        StringBuffer sentencia = new StringBuffer();
        setNivel(!getPrograma().equals("'0000'") && getUnidad().equals("0000") && getAmbito().equals("0000") ? "3" : getNivel());

        if (mes_ant1.equals("DIC")) {
            mesAntCargo1 = construirMes(mes_act1, "cargo_ini");
            mesAntAbono1 = construirMes(mes_act1, "abono_ini");
        } else {
            mesAntCargo1 = construirMes(mes_ant1, "cargo_acum");
            mesAntAbono1 = construirMes(mes_ant1, "abono_acum");
        }
        mesActCargo1 = construirMes(mes_act1, "cargo");
        mesActAbono1 = construirMes(mes_act1, "abono");

        sentencia.append("\n select * ");
        sentencia.append("\n FROM( ");
        sentencia.append("\n SELECT 'LA' AS reporte, ");
        sentencia.append("\n seccion, ");
        sentencia.append("\n n1, ");
        sentencia.append("\n Initcap(descripcion) AS descripcion, ");
        sentencia.append("\n (saldo_actual- saldo_anterior) AS variacion, ");
        sentencia.append("\n ( CASE ");
        sentencia.append("\n     WHEN (saldo_anterior- saldo_actual)!= 0 ");
        sentencia.append("\n     THEN ");
        sentencia.append("\n       CASE ");
        sentencia.append("\n         WHEN saldo_anterior != 0 ");
        sentencia.append("\n         THEN ROUND( ( ((saldo_anterior- saldo_actual)/ saldo_anterior)* 100), 2) ");
        sentencia.append("\n         ELSE ");
        sentencia.append("\n           CASE ");
        sentencia.append("\n             WHEN (saldo_anterior- saldo_actual)>0 ");
        sentencia.append("\n             THEN 100 ");
        sentencia.append("\n             ELSE -100 ");
        sentencia.append("\n           END ");
        sentencia.append("\n       END ");
        sentencia.append("\n     ELSE 0 ");
        sentencia.append("\n   END )*-1 AS porcentaje, ");
        sentencia.append("\n ( CASE ");
        sentencia.append("\n      WHEN ( saldo_actual - saldo_anterior ) != 0 ");
        sentencia.append("\n       THEN ");
        sentencia.append("\n        CASE ");
        sentencia.append("\n          WHEN ( saldo_actual - saldo_anterior ) < 0 ");
        sentencia.append("\n             THEN (saldo_actual - saldo_anterior ) ");
        sentencia.append("\n             ELSE 0 ");
        sentencia.append("\n        END ");
        sentencia.append("\n      ELSE 0 ");

        sentencia.append("\n    END ) AS SALDO_ACTUAL,"); //(SALDO_ACTUAL)Es el campo ORIGEN
        sentencia.append("\n  ( CASE ");
        sentencia.append("\n      WHEN ( saldo_actual - saldo_anterior ) != 0 ");
        sentencia.append("\n       THEN ");
        sentencia.append("\n        CASE ");
        sentencia.append("\n          WHEN (saldo_actual - saldo_anterior ) > 0 ");
        sentencia.append("\n             THEN saldo_actual - saldo_anterior ");
        sentencia.append("\n             ELSE 0 ");
        sentencia.append("\n        END ");
        sentencia.append("\n      ELSE 0 ");
        sentencia.append("\n    END ) AS SALDO_ANTERIOR "); //(SALDO_ANTERIOR)Es el campo APLICACION
        sentencia.append("\n FROM (");

        sentencia.append("\n  SELECT 'S1' AS seccion, ");
        sentencia.append("\n    SUBSTR(cc.cuenta_contable, 1, 3)||'N'   AS n1,  ");
        sentencia.append("\n    'XDescripcion SubTotales' AS descripcion, ");
        sentencia.append("\n    CASE MAX(cl.naturaleza) ");
        sentencia.append("\n      WHEN 'D' ");
        sentencia.append("\n      THEN SUM(").append(mesAntCargo1).append(") - SUM(").append(mesAntAbono1).append(")");
        sentencia.append("\n      ELSE (SUM(").append(mesAntAbono1).append(") - SUM(").append(mesAntCargo1).append("))");
        sentencia.append("\n    END saldo_anterior, ");
        sentencia.append("\n    CASE MAX(cl.naturaleza) ");
        sentencia.append("\n      WHEN 'D' ");
        sentencia.append("\n        THEN (( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") ) - ( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") )) ");
        sentencia.append("\n        ELSE (( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") ) - ( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") )) ");
        sentencia.append("\n    END AS saldo_actual ");
        sentencia.append("\n  FROM 	rf_tr_cuentas_contables cc, ");
        sentencia.append("\n		rf_tc_clasificador_cuentas cl ");
        sentencia.append("\n  WHERE cc.cuenta_mayor_id            = cl.cuenta_mayor_id ");
        sentencia.append("\n  AND cc.nivel                        = ").append(getNivel());
        sentencia.append("\n  AND cc.id_catalogo_cuenta           = 1 ");
        sentencia.append("\n  AND TO_CHAR(cc.fecha_vig_ini,'yyyy')= ").append(ejercicio);
        sentencia.append("\n  AND cc.cuenta_contable LIKE '1_%'   ");

        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(ejercicio).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('unidad'  ,cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('unidad'  ,cc.cuenta_mayor_id,").append(ejercicio).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('ambito'  ,cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('ambito'  ,cc.cuenta_mayor_id,").append(ejercicio).append(")) = '").append(getAmbito()).append("' ");
        sentencia.append("\n   GROUP BY 'S1', SUBSTR(cc.cuenta_contable, 1, 3)||'N' ");
        sentencia.append("\n  ) ");
        sentencia.append("\n WHERE (saldo_actual!= 0 ");
        sentencia.append("\n OR saldo_anterior  != 0) ");

        sentencia.append("\n  UNION ALL ");

        sentencia.append("\n select 'LA' AS reporte,  seccion,substr(n1,1,2)||99, DECODE(substr(n1,1,2)||'99', '1199', 'Total de Activo Circulante', '1299', 'Total de Activo No Circulante', Initcap(descripcion)) AS descripcion, ");
        sentencia.append("\n sum(variacion) as variacion, sum (porcentaje) porcentaje, sum(saldo_actual) saldo_actual, sum(saldo_anterior) saldo_anterior ");
        sentencia.append("\n from ");
        sentencia.append("\n   (  ");
        sentencia.append("\n    SELECT  ");
        sentencia.append("\n    seccion,  ");
        sentencia.append("\n    n1,  ");
        sentencia.append("\n    Initcap(descripcion) AS descripcion,  ");
        sentencia.append("\n    (saldo_actual - saldo_anterior) AS variacion,  ");
        sentencia.append("\n  ( CASE  ");
        sentencia.append("\n        WHEN (saldo_anterior- saldo_actual)!= 0  ");
        sentencia.append("\n          THEN  ");
        sentencia.append("\n            CASE  ");
        sentencia.append("\n              WHEN saldo_anterior != 0  ");
        sentencia.append("\n                THEN ROUND( ( ((saldo_anterior- saldo_actual)/ saldo_anterior)* 100), 2)  ");
        sentencia.append("\n                ELSE  ");
        sentencia.append("\n                  CASE  ");
        sentencia.append("\n                    WHEN (saldo_anterior- saldo_actual)>0  ");
        sentencia.append("\n                      THEN 100  ");
        sentencia.append("\n                      ELSE -100  ");
        sentencia.append("\n                  END  ");
        sentencia.append("\n            END  ");
        sentencia.append("\n          ELSE 0  ");
        sentencia.append("\n      END )*-1 AS porcentaje,  ");
        sentencia.append("\n  ( CASE  ");
        sentencia.append("\n        WHEN ( saldo_actual - saldo_anterior ) != 0  ");
        sentencia.append("\n          THEN  ");
        sentencia.append("\n            CASE  ");
        sentencia.append("\n              WHEN ( saldo_actual - saldo_anterior ) < 0  ");
        sentencia.append("\n                THEN (saldo_actual - saldo_anterior )  ");
        sentencia.append("\n                ELSE 0  ");
        sentencia.append("\n            END  ");
        sentencia.append("\n          ELSE 0  ");
        sentencia.append("\n    END ) AS SALDO_ACTUAL, ");
        sentencia.append("\n  ( CASE  ");
        sentencia.append("\n        WHEN ( saldo_actual - saldo_anterior ) != 0  ");
        sentencia.append("\n          THEN  ");
        sentencia.append("\n            CASE  ");
        sentencia.append("\n              WHEN (saldo_actual - saldo_anterior ) > 0  ");
        sentencia.append("\n                THEN saldo_actual - saldo_anterior  ");
        sentencia.append("\n                ELSE 0  ");
        sentencia.append("\n            END  ");
        sentencia.append("\n          ELSE 0  ");
        sentencia.append("\n    END ) AS SALDO_ANTERIOR  ");
        sentencia.append("\n FROM ( ");
        sentencia.append("\n  SELECT 'S1' AS seccion, ");
        sentencia.append("\n  SUBSTR(cc.cuenta_contable, 1, 3)  AS n1, ");
        sentencia.append("\n    'XDescripcion Totales' AS descripcion, ");
        sentencia.append("\n    CASE MAX(cl.naturaleza) ");
        sentencia.append("\n      WHEN 'D' ");
        sentencia.append("\n      THEN SUM(").append(mesAntCargo1).append(") - SUM(").append(mesAntAbono1).append(")");
        sentencia.append("\n      ELSE (SUM(").append(mesAntAbono1).append(") - SUM(").append(mesAntCargo1).append("))");
        sentencia.append("\n    END saldo_anterior, ");
        sentencia.append("\n    CASE MAX(cl.naturaleza) ");
        sentencia.append("\n      WHEN 'D' ");
        sentencia.append("\n        THEN (( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") ) - ( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") )) ");
        sentencia.append("\n        ELSE (( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") ) - ( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") )) ");
        sentencia.append("\n    END AS saldo_actual ");
        sentencia.append("\n  FROM 	rf_tr_cuentas_contables cc, ");
        sentencia.append("\n		rf_tc_clasificador_cuentas cl ");
        sentencia.append("\n  WHERE cc.cuenta_mayor_id            = cl.cuenta_mayor_id ");
        sentencia.append("\n  AND cc.nivel                        = ").append(getNivel());
        sentencia.append("\n  AND cc.id_catalogo_cuenta           = 1 ");
        sentencia.append("\n  AND TO_CHAR(cc.fecha_vig_ini,'yyyy')= ").append(ejercicio);
        sentencia.append("\n  AND         cc.cuenta_contable LIKE '1_%'   ");

        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(ejercicio).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('unidad'  ,cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('unidad'  ,cc.cuenta_mayor_id,").append(ejercicio).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('ambito'  ,cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('ambito'  ,cc.cuenta_mayor_id,").append(ejercicio).append(")) = '").append(getAmbito()).append("' ");
        sentencia.append("\n  GROUP BY 'S1', SUBSTR(cc.cuenta_contable, 1, 3) ");
        sentencia.append("\n ) ");
        sentencia.append("\n WHERE (saldo_actual!= 0  ");
        sentencia.append("\n OR saldo_anterior  != 0) ");

        sentencia.append("\n  ) group by seccion, substr(n1,1,2),descripcion ");
        /*TERMINA EL QUERY DEL ACTIVO*/
        sentencia.append("\n UNION ALL ");
        /*EMPIEZA EL QUERY DEL PASIVO*/
        sentencia.append("\n SELECT 'LB' AS reporte, ");
        sentencia.append("\n seccion, ");
        sentencia.append("\n n1, ");
        sentencia.append("\n Initcap(descripcion) AS descripcion, ");
        sentencia.append("\n (saldo_actual- saldo_anterior) AS variacion, ");
        sentencia.append("\n  (CASE ");
        sentencia.append("\n    WHEN (saldo_anterior- saldo_actual)!= 0 ");
        sentencia.append("\n    THEN ");
        sentencia.append("\n      CASE ");
        sentencia.append("\n        WHEN saldo_anterior != 0 ");
        sentencia.append("\n        THEN ROUND( ( ((saldo_anterior- saldo_actual)/ saldo_anterior)* 100), 2) ");
        sentencia.append("\n        ELSE ");
        sentencia.append("\n          CASE ");
        sentencia.append("\n           WHEN (saldo_anterior- saldo_actual)>0 ");
        sentencia.append("\n            THEN 100 ");
        sentencia.append("\n            ELSE -100 ");
        sentencia.append("\n          END ");
        sentencia.append("\n      END ");
        sentencia.append("\n    ELSE 0 ");
        sentencia.append("\n  END )*-1 AS porcentaje, ");
        sentencia.append("\n  ( CASE ");
        sentencia.append("\n      WHEN (saldo_actual - saldo_anterior) != 0 ");
        sentencia.append("\n       THEN ");
        sentencia.append("\n        CASE ");
        sentencia.append("\n          WHEN (saldo_actual - saldo_anterior) > 0 ");
        sentencia.append("\n             THEN (saldo_actual - saldo_anterior) ");
        sentencia.append("\n             ELSE 0 ");
        sentencia.append("\n        END ");
        sentencia.append("\n      ELSE 0 ");
        sentencia.append("\n    END ) AS SALDO_ACTUAL,");//(SALDO_ACTUAL)Es el campo ORIGEN
        sentencia.append("\n  ( CASE ");
        sentencia.append("\n      WHEN (saldo_actual - saldo_anterior) != 0 ");
        sentencia.append("\n       THEN ");
        sentencia.append("\n        CASE ");
        sentencia.append("\n          WHEN (saldo_actual - saldo_anterior) < 0 ");
        sentencia.append("\n             THEN (saldo_actual - saldo_anterior) ");
        sentencia.append("\n             ELSE 0 ");
        sentencia.append("\n        END ");
        sentencia.append("\n      ELSE 0 ");
        sentencia.append("\n    END ) SALDO_ANTERIOR ");//(SALDO_ANTERIOR)Es el campo APLICACION 
        sentencia.append("\nFROM ( ");

        sentencia.append("\n  SELECT 'S1' AS seccion, ");
        sentencia.append("\n    SUBSTR(cc.cuenta_contable, 1, 3)||'N'  AS n1, ");
        sentencia.append("\n    'XDescripcion SubTotales' AS descripcion, ");
        sentencia.append("\n    CASE MAX(cl.naturaleza) ");
        sentencia.append("\n      WHEN 'C' ");
        sentencia.append("\n      THEN SUM(").append(mesAntCargo1).append(") - SUM(").append(mesAntAbono1).append(")");
        sentencia.append("\n      ELSE (SUM(").append(mesAntAbono1).append(") - SUM(").append(mesAntCargo1).append("))");
        sentencia.append("\n    END saldo_anterior, ");
        sentencia.append("\n    CASE MAX(cl.naturaleza) ");
        sentencia.append("\n      WHEN 'C' ");
        sentencia.append("\n        THEN (( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") ) - ( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") )) ");
        sentencia.append("\n        ELSE (( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") ) - ( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") )) ");
        sentencia.append("\n    END AS saldo_actual ");
        sentencia.append("\n  FROM 	rf_tr_cuentas_contables    cc, ");
        sentencia.append("\n		rf_tc_clasificador_cuentas cl ");
        sentencia.append("\n  WHERE cc.cuenta_mayor_id             = cl.cuenta_mayor_id ");
        sentencia.append("\n  AND cc.nivel                         = ").append(getNivel());
        sentencia.append("\n  AND cc.id_catalogo_cuenta            = 1 ");
        sentencia.append("\n  AND TO_CHAR(cc.fecha_vig_ini,'yyyy') = ").append(ejercicio);
        sentencia.append("\n  AND cc.cuenta_contable LIKE '2_%' ");

        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(ejercicio).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('unidad'  ,cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('unidad'  ,cc.cuenta_mayor_id,").append(ejercicio).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('ambito'  ,cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('ambito'  ,cc.cuenta_mayor_id,").append(ejercicio).append(")) = '").append(getAmbito()).append("' ");
        sentencia.append("\n  GROUP BY 'S1', SUBSTR(cc.cuenta_contable, 1, 3) ");
        sentencia.append("\n ) ");
        sentencia.append("\n WHERE (saldo_actual!= 0  ");
        sentencia.append("\n OR saldo_anterior  != 0) ");

        sentencia.append("\n  UNION ALL ");

        sentencia.append("\n select 'LB' AS reporte,  seccion,substr(n1,1,2)||99, DECODE(substr(n1,1,2)||'99', '2199', 'Total de Pasivo Circulante', '2299', 'Total Pasivo No Circulante', Initcap(descripcion)) AS descripcion, ");
        sentencia.append("\n sum(variacion) as variacion, sum (porcentaje) porcentaje, sum(saldo_actual) saldo_actual, sum(saldo_anterior) saldo_anterior ");
        sentencia.append("\n from ");
        sentencia.append("\n   (  ");
        sentencia.append("\n    SELECT  ");
        sentencia.append("\n    seccion,  ");
        sentencia.append("\n    n1,  ");
        sentencia.append("\n    Initcap(descripcion) AS descripcion,  ");
        sentencia.append("\n    (saldo_actual - saldo_anterior) AS variacion,  ");
        sentencia.append("\n  (CASE ");
        sentencia.append("\n    WHEN (saldo_anterior- saldo_actual)!= 0 ");
        sentencia.append("\n    THEN ");
        sentencia.append("\n      CASE ");
        sentencia.append("\n        WHEN saldo_anterior != 0 ");
        sentencia.append("\n        THEN ROUND( ( ((saldo_anterior- saldo_actual)/ saldo_anterior)* 100), 2) ");
        sentencia.append("\n        ELSE ");
        sentencia.append("\n          CASE ");
        sentencia.append("\n           WHEN (saldo_anterior- saldo_actual)>0 ");
        sentencia.append("\n            THEN 100 ");
        sentencia.append("\n            ELSE -100 ");
        sentencia.append("\n          END ");
        sentencia.append("\n      END ");
        sentencia.append("\n    ELSE 0 ");
        sentencia.append("\n  END )*-1 AS porcentaje, ");
        sentencia.append("\n  ( CASE ");
        sentencia.append("\n      WHEN (saldo_actual - saldo_anterior) != 0 ");
        sentencia.append("\n       THEN ");
        sentencia.append("\n        CASE ");
        sentencia.append("\n          WHEN (saldo_actual - saldo_anterior) > 0 ");
        sentencia.append("\n             THEN (saldo_actual - saldo_anterior) ");
        sentencia.append("\n             ELSE 0 ");
        sentencia.append("\n        END ");
        sentencia.append("\n      ELSE 0 ");
        sentencia.append("\n    END ) AS SALDO_ACTUAL,");//(SALDO_ACTUAL)Es el campo ORIGEN
        sentencia.append("\n  ( CASE ");
        sentencia.append("\n      WHEN (saldo_actual - saldo_anterior) != 0 ");
        sentencia.append("\n       THEN ");
        sentencia.append("\n        CASE ");
        sentencia.append("\n          WHEN (saldo_actual - saldo_anterior) < 0 ");
        sentencia.append("\n             THEN (saldo_actual - saldo_anterior) ");
        sentencia.append("\n             ELSE 0 ");
        sentencia.append("\n        END ");
        sentencia.append("\n      ELSE 0 ");
        sentencia.append("\n    END ) SALDO_ANTERIOR ");//(SALDO_ANTERIOR)Es el campo APLICACION 
        sentencia.append("\n FROM ( ");
        sentencia.append("\n  SELECT 'S1' AS seccion, ");
        sentencia.append("\n     SUBSTR(cc.cuenta_contable, 1, 3)  AS n1,  ");
        sentencia.append("\n    'XDescripcion Totales' AS descripcion, ");
        sentencia.append("\n   CASE MAX(cl.naturaleza) ");
        sentencia.append("\n      WHEN 'C' ");
        sentencia.append("\n      THEN SUM(").append(mesAntCargo1).append(") - SUM(").append(mesAntAbono1).append(")");
        sentencia.append("\n      ELSE (SUM(").append(mesAntAbono1).append(") - SUM(").append(mesAntCargo1).append("))");
        sentencia.append("\n    END saldo_anterior, ");
        sentencia.append("\n    CASE MAX(cl.naturaleza) ");
        sentencia.append("\n      WHEN 'C' ");
        sentencia.append("\n        THEN (( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") ) - ( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") )) ");
        sentencia.append("\n        ELSE (( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") ) - ( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") )) ");
        sentencia.append("\n    END AS saldo_actual ");
        sentencia.append("\n  FROM  rf_tr_cuentas_contables    cc, ");
        sentencia.append("\n	    rf_tc_clasificador_cuentas cl ");
        sentencia.append("\n  WHERE cc.cuenta_mayor_id            = cl.cuenta_mayor_id ");
        sentencia.append("\n  AND cc.nivel                        = ").append(getNivel());
        sentencia.append("\n  AND cc.id_catalogo_cuenta           = 1 ");
        sentencia.append("\n  AND TO_CHAR(cc.fecha_vig_ini,'yyyy')= ").append(ejercicio);
        sentencia.append("\n  AND     cc.cuenta_contable LIKE '2_%' ");

        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(ejercicio).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('unidad'  ,cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('unidad'  ,cc.cuenta_mayor_id,").append(ejercicio).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('ambito'  ,cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('ambito'  ,cc.cuenta_mayor_id,").append(ejercicio).append(")) = '").append(getAmbito()).append("' ");
        sentencia.append("\n  GROUP BY 'S1', SUBSTR(cc.cuenta_contable, 1, 3) ");
        sentencia.append("\n ) ");
        sentencia.append("\n WHERE (saldo_actual!= 0  ");
        sentencia.append("\n OR saldo_anterior  != 0) ");

        sentencia.append("\n  ) group by seccion, substr(n1,1,2),descripcion ");
        /*TERMINA EL QUERY DEL PASIVO*/
        /*EMPIEZA HACIENDA*/
        sentencia.append(" UNION ALL");
        sentencia.append("\n  SELECT 'LB' AS reporte,  ");
        sentencia.append("\n  seccion, n1, descripcion AS descripcion, ");
        sentencia.append("\n  (saldo_actual- saldo_anterior) AS variacion,");
        sentencia.append("\n  ( CASE ");
        sentencia.append("\n    WHEN (saldo_anterior- saldo_actual)!= 0 ");
        sentencia.append("\n    THEN ");
        sentencia.append("\n      CASE ");
        sentencia.append("\n        WHEN saldo_anterior != 0 ");
        sentencia.append("\n        THEN ROUND( ( ((saldo_anterior- saldo_actual)/ saldo_anterior)* 100), 2) ");
        sentencia.append("\n        ELSE ");
        sentencia.append("\n          CASE ");
        sentencia.append("\n           WHEN (saldo_anterior- saldo_actual)>0 ");
        sentencia.append("\n            THEN 100 ");
        sentencia.append("\n            ELSE -100 ");
        sentencia.append("\n          END ");
        sentencia.append("\n      END ");
        sentencia.append("\n    ELSE 0 ");
        sentencia.append("\n  END )*-1 AS porcentaje, ");
        sentencia.append("\n  ( CASE ");
        sentencia.append("\n      WHEN (saldo_actual - saldo_anterior) != 0 ");
        sentencia.append("\n       THEN ");
        sentencia.append("\n        CASE ");
        sentencia.append("\n          WHEN (saldo_actual - saldo_anterior) > 0 ");
        sentencia.append("\n             THEN (saldo_actual - saldo_anterior) ");
        sentencia.append("\n             ELSE 0 ");
        sentencia.append("\n        END ");
        sentencia.append("\n      ELSE 0 ");
        sentencia.append("\n    END ) AS SALDO_ACTUAL,");//(SALDO_ACTUAL)Es el campo ORIGEN
        sentencia.append("\n  ( CASE ");
        sentencia.append("\n      WHEN (saldo_actual - saldo_anterior) != 0 ");
        sentencia.append("\n       THEN ");
        sentencia.append("\n        CASE ");
        sentencia.append("\n          WHEN (saldo_actual - saldo_anterior) < 0 ");
        sentencia.append("\n             THEN (saldo_actual - saldo_anterior) ");
        sentencia.append("\n             ELSE 0 ");
        sentencia.append("\n        END ");
        sentencia.append("\n      ELSE 0 ");
        sentencia.append("\n    END ) SALDO_ANTERIOR ");//(SALDO_ANTERIOR)Es el campo APLICACION 
        sentencia.append("\n FROM ( ");

        sentencia.append("\n  SELECT ");
        sentencia.append("\n  'S1' as seccion,");
        sentencia.append("\n  '321N' AS n1, ");
        sentencia.append("\n  'XDescripcion SubTotales' AS descripcion, ");
        sentencia.append("\n  SUM(saldo_anterior)                    AS saldo_anterior, ");
        sentencia.append("\n  SUM(saldo_actual)                      AS saldo_actual");
        sentencia.append("\n  FROM ");
        sentencia.append("\n  ( ");
        sentencia.append("\n     SELECT CASE cl.naturaleza");
        sentencia.append("\n         WHEN 'D' ");
        sentencia.append("\n		     THEN (( ").append(mesAntCargo2).append(" ) - ( ").append(mesAntAbono2).append(" ))  ");
        sentencia.append("\n		     ELSE (( ").append(mesAntAbono2).append(" ) - ( ").append(mesAntCargo2).append(" ))  ");
        sentencia.append("\n         END");
        sentencia.append("\n          AS saldo_anterior,");
        sentencia.append("\n       CASE cl.naturaleza");
        sentencia.append("\n         WHEN 'D'");
        sentencia.append("\n			THEN (( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ) - ( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ))  ");
        sentencia.append("\n			ELSE (( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ) - ( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ))  ");
        sentencia.append("\n		END AS saldo_actual ");
        sentencia.append("\n	FROM rf_tr_cuentas_contables cc, ");
        sentencia.append("\n		 rf_tc_clasificador_cuentas cl ");
        sentencia.append("\n	WHERE cc.cuenta_mayor_id = cl.cuenta_mayor_id ");
        sentencia.append("\n		AND cc.id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta()).append(" ");
        sentencia.append("\n		AND extract(year from cc.fecha_vig_ini) = ").append(controlReg.getEjercicio()).append(" ");
        sentencia.append("\n	   AND (   cc.cuenta_contable LIKE '4_%'");
        sentencia.append("\n	      OR cc.cuenta_contable LIKE '41_%'");
        sentencia.append("\n	      OR cc.cuenta_contable LIKE '42_%'");
        sentencia.append("\n	      OR cc.cuenta_contable LIKE '43_%')");
        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getAmbito()).append("' ");
        sentencia.append("\n     AND cc.nivel = ").append(getNivel());
         sentencia.append("\n	--ANEXO2");
         sentencia.append("\n	UNION ALL");
         sentencia.append("\n	SELECT CASE cl.naturaleza");
        sentencia.append("\n	     WHEN 'D' ");
         sentencia.append("\n		     THEN (( ").append(mesAntCargo2).append(" ) - ( ").append(mesAntAbono2).append(" ))  ");
        sentencia.append("\n		     ELSE (( ").append(mesAntAbono2).append(" ) - ( ").append(mesAntCargo2).append(" ))  ");
        sentencia.append("\n	    END");
        sentencia.append("\n	       AS saldo_anterior,");
        sentencia.append("\n	    CASE cl.naturaleza");
        sentencia.append("\n	    WHEN 'D'");
        sentencia.append("\n			THEN (( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ) - ( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ))  ");
        sentencia.append("\n			ELSE (( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ) - ( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ))  ");
        sentencia.append("\n		END AS saldo_actual ");
        sentencia.append("\n	FROM rf_tr_cuentas_contables cc, ");
        sentencia.append("\n		 rf_tc_clasificador_cuentas cl ");
        sentencia.append("\n	WHERE cc.cuenta_mayor_id = cl.cuenta_mayor_id ");
        sentencia.append("\n		AND cc.id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta()).append(" ");
        sentencia.append("\n		AND extract(year from cc.fecha_vig_ini) = ").append(controlReg.getEjercicio()).append(" ");
        sentencia.append("\n	  AND (   cc.cuenta_contable LIKE '311%')");
        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getAmbito()).append("' ");
        sentencia.append("\n     AND cc.nivel = ").append(getNivel());
         sentencia.append("\n	UNION ALL");
         sentencia.append("\n	SELECT CASE cl.naturaleza");
        sentencia.append("\n	     WHEN 'D' ");
        sentencia.append("\n		     THEN (( ").append(mesAntCargo2).append(" ) - ( ").append(mesAntAbono2).append(" ))  ");
        sentencia.append("\n		     ELSE (( ").append(mesAntAbono2).append(" ) - ( ").append(mesAntCargo2).append(" ))  ");
        sentencia.append("\n	   END");
        sentencia.append("\n	       AS saldo_anterior,");
        sentencia.append("\n	    CASE cl.naturaleza");
        sentencia.append("\n	      WHEN 'D'");
        sentencia.append("\n			THEN (( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ) - ( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ))  ");
        sentencia.append("\n			ELSE (( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ) - ( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ))  ");
        sentencia.append("\n		END AS saldo_actual ");
        sentencia.append("\n	FROM rf_tr_cuentas_contables cc, ");
        sentencia.append("\n		 rf_tc_clasificador_cuentas cl ");
        sentencia.append("\n	WHERE cc.cuenta_mayor_id = cl.cuenta_mayor_id ");
        sentencia.append("\n		AND cc.id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta()).append(" ");
        sentencia.append("\n		AND extract(year from cc.fecha_vig_ini) = ").append(controlReg.getEjercicio()).append(" ");
        sentencia.append("\n	    AND (   cc.cuenta_contable LIKE '325%')");
        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getAmbito()).append("' ");
        sentencia.append("\n     AND cc.nivel = ").append(getNivel());
         sentencia.append("\n	--FIN ANEXO2  ");
        sentencia.append("\n	 UNION ALL ");
        sentencia.append("\n	SELECT CASE cl.naturaleza");
        sentencia.append("\n	    WHEN 'D' ");
        sentencia.append("\n		     THEN (( ").append(mesAntCargo2).append(" ) - ( ").append(mesAntAbono2).append(" ))  ");
        sentencia.append("\n		     ELSE (( ").append(mesAntAbono2).append(" ) - ( ").append(mesAntCargo2).append(" ))  ");
        sentencia.append("\n	    END");
        sentencia.append("\n	       AS saldo_anterior,");
        sentencia.append("\n	    CASE cl.naturaleza");
        sentencia.append("\n	       WHEN 'D'");
        sentencia.append("\n			THEN (( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ) - ( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ))  ");
        sentencia.append("\n			ELSE (( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ) - ( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ))  ");
        sentencia.append("\n		END AS saldo_actual ");
        sentencia.append("\n	FROM rf_tr_cuentas_contables cc, ");
        sentencia.append("\n		 rf_tc_clasificador_cuentas cl ");
        sentencia.append("\n	WHERE cc.cuenta_mayor_id = cl.cuenta_mayor_id ");
        sentencia.append("\n		AND cc.id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta()).append(" ");
        sentencia.append("\n		AND extract(year from cc.fecha_vig_ini) = ").append(controlReg.getEjercicio()).append(" ");
        sentencia.append("\n	   AND ( --  cc.cuenta_contable LIKE '43_%'");
        sentencia.append("\n	       -- OR ");
        sentencia.append("\n	        cc.cuenta_contable LIKE '5_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '51_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '52_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '53_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '54_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '55_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '825_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '826_%')");
        sentencia.append("\n	     AND ( cc.cuenta_contable NOT LIKE '511_%'");
        sentencia.append("\n	       AND cc.cuenta_contable NOT LIKE '512_%'");
        sentencia.append("\n	       AND cc.cuenta_contable NOT LIKE '513_%'");
        sentencia.append("\n	       AND cc.cuenta_contable NOT LIKE '8255_%'");
        sentencia.append("\n	       AND cc.cuenta_contable NOT LIKE '8256_%'");
        sentencia.append("\n           AND cc.cuenta_contable NOT LIKE '8257_%'");
        sentencia.append("\n	       AND cc.cuenta_contable NOT LIKE '8265_%'");
        sentencia.append("\n	       AND cc.cuenta_contable NOT LIKE '8266_%'");
        sentencia.append("\n	       AND cc.cuenta_contable NOT LIKE '8267_%')");
        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getAmbito()).append("' ");
        sentencia.append("\n     AND cc.nivel = ").append(getNivel());
        sentencia.append("\n	--FIN ANEXO3");
        sentencia.append("\n     UNION ALL");
        sentencia.append("\n     SELECT ");
        sentencia.append("\n     CASE cl.naturaleza");
        sentencia.append("\n        WHEN 'C'");
        sentencia.append("\n                 THEN (( ").append(mesAntCargo2).append(" ) - ( ").append(mesAntAbono2).append("  )) ");
        sentencia.append("\n                 ELSE (( ").append(mesAntAbono2).append(" ) - ( ").append(mesAntCargo2).append("  )) ");
        sentencia.append("\n     END");
        sentencia.append("\n        AS saldo_anterior,");
        sentencia.append("\n     CASE cl.naturaleza");
        sentencia.append("\n        WHEN 'C'");
        sentencia.append("\n                 THEN (( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ) - ( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" )) ");
        sentencia.append("\n                 ELSE (( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ) - ( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" )) ");
        sentencia.append("\n     END");
        sentencia.append("\n        AS saldo_actual");
        sentencia.append("\n     FROM rf_tr_cuentas_contables cc,");
        sentencia.append("\n     rf_tc_clasificador_cuentas cl");
        sentencia.append("\n     WHERE cc.cuenta_mayor_id = cl.cuenta_mayor_id");
        sentencia.append("\n     AND cc.id_catalogo_cuenta = 1");
        sentencia.append("\n		AND extract(year from cc.fecha_vig_ini) = ").append(controlReg.getEjercicio()).append("  ");
        sentencia.append("\n	 AND ( cc.cuenta_contable LIKE '827110001000100910001%'");
        sentencia.append("\n	      or cc.cuenta_contable LIKE '827110001000100911000%')");
        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }

        sentencia.append("\n     UNION ALL");
        sentencia.append("\n     SELECT ");
        sentencia.append("\n     CASE cl.naturaleza");
        sentencia.append("\n        WHEN 'C'");
        sentencia.append("\n                 THEN (( ").append(mesAntCargo2).append(" ) - ( ").append(mesAntAbono2).append("  )) ");
        sentencia.append("\n                 ELSE (( ").append(mesAntAbono2).append(" ) - ( ").append(mesAntCargo2).append("  )) ");
        sentencia.append("\n     END");
        sentencia.append("\n        AS saldo_anterior,");
        sentencia.append("\n     CASE cl.naturaleza");
        sentencia.append("\n        WHEN 'C'");
        sentencia.append("\n                 THEN (( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ) - ( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" )) ");
        sentencia.append("\n                 ELSE (( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ) - ( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" )) ");
        sentencia.append("\n     END");
        sentencia.append("\n        AS saldo_actual");
        sentencia.append("\n     FROM rf_tr_cuentas_contables cc,");
        sentencia.append("\n     rf_tc_clasificador_cuentas cl");
        sentencia.append("\n     WHERE cc.cuenta_mayor_id = cl.cuenta_mayor_id");
        sentencia.append("\n     AND cc.id_catalogo_cuenta = 1");
        sentencia.append("\n		AND extract(year from cc.fecha_vig_ini) = ").append(controlReg.getEjercicio()).append("  ");
        sentencia.append("\n	 AND ( cc.cuenta_contable LIKE '827110001000100910002%'");
        sentencia.append("\n	      or cc.cuenta_contable LIKE '827110001000100912000%')");
        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }

        sentencia.append("\n     UNION ALL");
        sentencia.append("\n     SELECT ");
        sentencia.append("\n     CASE cl.naturaleza");
        sentencia.append("\n        WHEN 'C'");
        sentencia.append("\n                 THEN (( ").append(mesAntCargo2).append(" ) - ( ").append(mesAntAbono2).append("  )) ");
        sentencia.append("\n                 ELSE (( ").append(mesAntAbono2).append(" ) - ( ").append(mesAntCargo2).append("  )) ");
        sentencia.append("\n     END");
        sentencia.append("\n        AS saldo_anterior,");
        sentencia.append("\n     CASE cl.naturaleza");
        sentencia.append("\n        WHEN 'C'");
        sentencia.append("\n                 THEN (( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ) - ( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" )) ");
        sentencia.append("\n                 ELSE (( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ) - ( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" )) ");
        sentencia.append("\n     END");
        sentencia.append("\n        AS saldo_actual");
        sentencia.append("\n     FROM rf_tr_cuentas_contables cc,");
        sentencia.append("\n     rf_tc_clasificador_cuentas cl");
        sentencia.append("\n     WHERE cc.cuenta_mayor_id = cl.cuenta_mayor_id");
        sentencia.append("\n     AND cc.id_catalogo_cuenta = 1");
        sentencia.append("\n		AND extract(year from cc.fecha_vig_ini) = ").append(controlReg.getEjercicio()).append("  ");
        sentencia.append("\n	 AND ( cc.cuenta_contable LIKE '827110001000100910003%'");
        sentencia.append("\n	      or cc.cuenta_contable LIKE '827110001000100913000%')");
        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }
        
        sentencia.append("\n     UNION ALL");
        sentencia.append("\n     SELECT ");
        sentencia.append("\n     CASE cl.naturaleza");
        sentencia.append("\n        WHEN 'D'");
        sentencia.append("\n                 THEN (( ").append(mesAntCargo2).append(" ) - ( ").append(mesAntAbono2).append("  )) ");
        sentencia.append("\n                 ELSE (( ").append(mesAntAbono2).append(" ) - ( ").append(mesAntCargo2).append("  )) ");
        sentencia.append("\n     END");
        sentencia.append("\n        AS saldo_anterior,");
        sentencia.append("\n     CASE cl.naturaleza");
        sentencia.append("\n        WHEN 'D'");
        sentencia.append("\n                 THEN (( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ) - ( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" )) ");
        sentencia.append("\n                 ELSE (( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ) - ( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" )) ");
        sentencia.append("\n     END");
        sentencia.append("\n        AS saldo_actual");
        sentencia.append("\n     FROM rf_tr_cuentas_contables cc,");
        sentencia.append("\n     rf_tc_clasificador_cuentas cl");
        sentencia.append("\n     WHERE cc.cuenta_mayor_id = cl.cuenta_mayor_id");
        sentencia.append("\n     AND cc.id_catalogo_cuenta = 1");
        sentencia.append("\n		AND extract(year from cc.fecha_vig_ini) = ").append(controlReg.getEjercicio()).append("  ");
        sentencia.append("\n	 AND ( cc.cuenta_contable LIKE '826110001000100910004%'");
        sentencia.append("\n	      or cc.cuenta_contable LIKE '826110001000100914000%')");
        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n ) ");
        sentencia.append("\n   UNION ALL ");
        sentencia.append("\n   SELECT 'S1'                       AS seccion, ");
        sentencia.append("\n     '3NNN'                          AS n1, ");
        sentencia.append("\n     'HACIENDA PÚBLICA / PATRIMONIO' AS descripcion, ");
        sentencia.append("\n     0 saldo_anterior, ");
        sentencia.append("\n     0   AS saldo_actual ");
        sentencia.append("\n   FROM dual ");

        sentencia.append("\n   UNION ALL ");

        sentencia.append("\n SELECT  'S1' AS seccion, ");
        sentencia.append("\n SUBSTR(cc.cuenta_contable, 1, 3)||'N'   AS n1, ");
        sentencia.append("\n 'XDescripcion SubTotales' AS descripcion, ");
        sentencia.append("\n CASE WHEN SUBSTR(cc.cuenta_contable, 1, 3)||'N'='322N' THEN  ");
        sentencia.append("\n  (CASE MAX(cl.naturaleza) ");
        sentencia.append("\n   WHEN 'D' ");
        sentencia.append("\n      THEN SUM(").append(mesAntCargo1).append(") - SUM(").append(mesAntAbono1).append(")");
        sentencia.append("\n      ELSE (SUM(").append(mesAntAbono1).append(") - SUM(").append(mesAntCargo1).append("))");
        sentencia.append("\n END)*-1 ELSE ");
        sentencia.append("\n  (CASE MAX(cl.naturaleza) ");
        sentencia.append("\n   WHEN 'D' ");
        sentencia.append("\n      THEN SUM(").append(mesAntCargo1).append(") - SUM(").append(mesAntAbono1).append(")");
        sentencia.append("\n      ELSE (SUM(").append(mesAntAbono1).append(") - SUM(").append(mesAntCargo1).append("))");
        sentencia.append("\n END) END AS saldo_anterior, ");
        sentencia.append("\n CASE WHEN SUBSTR(cc.cuenta_contable, 1, 3)||'N'='322N' THEN  ");
        sentencia.append("\n (CASE MAX(cl.naturaleza) ");
        sentencia.append("\n   WHEN 'D' ");
        sentencia.append("\n        THEN (( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") ) - ( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") )) ");
        sentencia.append("\n        ELSE (( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") ) - ( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") )) ");
        sentencia.append("\n END)*-1 ELSE");
        sentencia.append("\n (CASE MAX(cl.naturaleza) ");
        sentencia.append("\n   WHEN 'D' ");
        sentencia.append("\n        THEN (( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") ) - ( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") )) ");
        sentencia.append("\n        ELSE (( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") ) - ( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") )) ");
        sentencia.append("\n END) END AS saldo_actual ");
        sentencia.append("\n  FROM 	rf_tr_cuentas_contables cc, ");
        sentencia.append("\n 	rf_tc_clasificador_cuentas cl ");
        sentencia.append("\n WHERE cc.cuenta_mayor_id             = cl.cuenta_mayor_id ");
        sentencia.append("\n AND cc.nivel                         = ").append(getNivel());
        sentencia.append("\n AND cc.id_catalogo_cuenta            = 1  ");
        sentencia.append("\n  AND TO_CHAR(cc.fecha_vig_ini,'yyyy')= ").append(ejercicio);
        sentencia.append("\n AND substr(cc.cuenta_contable,1,3) between 322 and 329 and substr(cc.cuenta_contable,1,3) != 325 ");

        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(ejercicio).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('unidad'  ,cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('unidad'  ,cc.cuenta_mayor_id,").append(ejercicio).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('ambito'  ,cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('ambito'  ,cc.cuenta_mayor_id,").append(ejercicio).append(")) = '").append(getAmbito()).append("' ");
        sentencia.append("\n GROUP BY 'S1', SUBSTR(cc.cuenta_contable, 1, 3)||'N'");
        sentencia.append("\n ) ");
        sentencia.append("\n WHERE ");
        sentencia.append("\n ( saldo_actual!= 0   ");
        sentencia.append("\n OR saldo_anterior  != 0)  ");

        sentencia.append("\n   UNION ALL ");
        sentencia.append("\n   SELECT reporte,seccion,n1, descripcion, ");
        sentencia.append("\n     SUM(variacion) AS variacion, ");
        sentencia.append("\n     SUM(porcentaje) AS porcentaje, ");
        sentencia.append("\n     SUM(saldo_actual) AS saldo_actual, ");
        sentencia.append("\n     SUM(saldo_anterior) AS saldo_anterior ");
        sentencia.append("\n FROM( ");
        sentencia.append("\n SELECT 'LB' AS reporte,'S1' seccion,");
        sentencia.append("\n SUBSTR(cuenta_contable, 1, 1)||'999' n1, ");
        sentencia.append("\n 'Total Hacienda Pública / Patrimonio' AS descripcion, ");
        sentencia.append("\n  (saldo_actual- saldo_anterior) AS variacion, ");
        sentencia.append("\n  (CASE ");
        sentencia.append("\n    WHEN (saldo_anterior- saldo_actual)!= 0 ");
        sentencia.append("\n    THEN ");
        sentencia.append("\n      CASE ");
        sentencia.append("\n        WHEN saldo_anterior != 0 ");
        sentencia.append("\n        THEN ROUND( ( ((saldo_anterior- saldo_actual)/ saldo_anterior)* 100), 2) ");
        sentencia.append("\n        ELSE ");
        sentencia.append("\n          CASE ");
        sentencia.append("\n           WHEN (saldo_anterior- saldo_actual)>0 ");
        sentencia.append("\n            THEN 100 ");
        sentencia.append("\n            ELSE -100 ");
        sentencia.append("\n          END ");
        sentencia.append("\n      END ");
        sentencia.append("\n    ELSE 0 ");
        sentencia.append("\n  END )*-1 AS porcentaje, ");
        sentencia.append("\n  ( CASE ");
        sentencia.append("\n      WHEN (saldo_actual - saldo_anterior) != 0 ");
        sentencia.append("\n       THEN ");
        sentencia.append("\n        CASE ");
        sentencia.append("\n          WHEN (saldo_actual - saldo_anterior) > 0 ");
        sentencia.append("\n             THEN (saldo_actual - saldo_anterior) ");
        sentencia.append("\n             ELSE 0 ");
        sentencia.append("\n        END ");
        sentencia.append("\n      ELSE 0 ");
        sentencia.append("\n    END ) AS SALDO_ACTUAL,");//(SALDO_ACTUAL)Es el campo ORIGEN
        sentencia.append("\n  ( CASE ");
        sentencia.append("\n      WHEN (saldo_actual - saldo_anterior) != 0 ");
        sentencia.append("\n       THEN ");
        sentencia.append("\n        CASE ");
        sentencia.append("\n          WHEN (saldo_actual - saldo_anterior) < 0 ");
        sentencia.append("\n             THEN (saldo_actual - saldo_anterior) ");
        sentencia.append("\n             ELSE 0 ");
        sentencia.append("\n        END ");
        sentencia.append("\n      ELSE 0 ");
        sentencia.append("\n    END ) SALDO_ANTERIOR ");//(SALDO_ANTERIOR)Es el campo APLICACION 
        sentencia.append("\n FROM ( ");


        sentencia.append("\n SELECT  SUBSTR(cc.cuenta_contable, 1, 3) AS cuenta_contable, ");
        sentencia.append("\n CASE WHEN SUBSTR(cc.cuenta_contable, 1, 3)='322' THEN  ");
        sentencia.append("\n  (CASE MAX(cl.naturaleza) ");
        sentencia.append("\n   WHEN 'D' ");
        sentencia.append("\n      THEN SUM(").append(mesAntCargo1).append(") - SUM(").append(mesAntAbono1).append(")");
        sentencia.append("\n      ELSE (SUM(").append(mesAntAbono1).append(") - SUM(").append(mesAntCargo1).append("))");
        sentencia.append("\n END)*-1 ELSE ");
        sentencia.append("\n  (CASE MAX(cl.naturaleza) ");
        sentencia.append("\n   WHEN 'D' ");
        sentencia.append("\n      THEN SUM(").append(mesAntCargo1).append(") - SUM(").append(mesAntAbono1).append(")");
        sentencia.append("\n      ELSE (SUM(").append(mesAntAbono1).append(") - SUM(").append(mesAntCargo1).append("))");
        sentencia.append("\n END) END AS saldo_anterior, ");
        sentencia.append("\n CASE WHEN SUBSTR(cc.cuenta_contable, 1, 3)='322' THEN  ");
        sentencia.append("\n (CASE MAX(cl.naturaleza) ");
        sentencia.append("\n   WHEN 'D' ");
        sentencia.append("\n        THEN (( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") ) - ( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") )) ");
        sentencia.append("\n        ELSE (( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") ) - ( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") )) ");
        sentencia.append("\n END)*-1 ELSE");
        sentencia.append("\n (CASE MAX(cl.naturaleza) ");
        sentencia.append("\n   WHEN 'D' ");
        sentencia.append("\n        THEN (( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") ) - ( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") )) ");
        sentencia.append("\n        ELSE (( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") ) - ( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") )) ");
        sentencia.append("\n END) END AS saldo_actual ");
        sentencia.append("\n  FROM 	rf_tr_cuentas_contables cc, ");
        sentencia.append("\n 	rf_tc_clasificador_cuentas cl ");
        sentencia.append("\n WHERE cc.cuenta_mayor_id             = cl.cuenta_mayor_id ");
        sentencia.append("\n AND cc.nivel                         = ").append(getNivel());
        sentencia.append("\n AND cc.id_catalogo_cuenta            = 1  ");
        sentencia.append("\n  AND TO_CHAR(cc.fecha_vig_ini,'yyyy')= ").append(ejercicio);
        sentencia.append("\n AND substr(cc.cuenta_contable,1,3) >= 322 and substr(cc.cuenta_contable,1,3) <= 329 and substr(cc.cuenta_contable,1,3) != 325 ");

        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(ejercicio).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('unidad'  ,cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('unidad'  ,cc.cuenta_mayor_id,").append(ejercicio).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('ambito'  ,cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('ambito'  ,cc.cuenta_mayor_id,").append(ejercicio).append(")) = '").append(getAmbito()).append("' ");
        sentencia.append("\n GROUP BY SUBSTR(cc.cuenta_contable, 1, 3) ");

//////// 330
        sentencia.append("\n   UNION ALL ");

        sentencia.append("\n SELECT  'S1' AS seccion, ");
        sentencia.append("\n '330N'   AS n1, ");
        sentencia.append("\n 'XDescripcion SubTotales' AS descripcion, ");
        sentencia.append("\n SUM (saldo_anterior) AS saldo_anterior, ");
        sentencia.append("\n SUM (saldo_actual) AS saldo_actual ");
        sentencia.append("\n FROM ( ");
        sentencia.append("\n SELECT");
        sentencia.append("\n CASE WHEN SUBSTR(cc.cuenta_contable, 1, 3)||'N'='322N' THEN  ");
        sentencia.append("\n  (CASE MAX(cl.naturaleza) ");
        sentencia.append("\n   WHEN 'D' ");
        sentencia.append("\n      THEN SUM(").append(mesAntCargo1).append(") - SUM(").append(mesAntAbono1).append(")");
        sentencia.append("\n      ELSE (SUM(").append(mesAntAbono1).append(") - SUM(").append(mesAntCargo1).append("))");
        sentencia.append("\n END)*-1 ELSE ");
        sentencia.append("\n  (CASE MAX(cl.naturaleza) ");
        sentencia.append("\n   WHEN 'D' ");
        sentencia.append("\n      THEN SUM(").append(mesAntCargo1).append(") - SUM(").append(mesAntAbono1).append(")");
        sentencia.append("\n      ELSE (SUM(").append(mesAntAbono1).append(") - SUM(").append(mesAntCargo1).append("))");
        sentencia.append("\n END) END AS saldo_anterior, ");
        sentencia.append("\n CASE WHEN SUBSTR(cc.cuenta_contable, 1, 3)||'N'='322N' THEN  ");
        sentencia.append("\n (CASE MAX(cl.naturaleza) ");
        sentencia.append("\n   WHEN 'D' ");
        sentencia.append("\n        THEN (( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") ) - ( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") )) ");
        sentencia.append("\n        ELSE (( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") ) - ( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") )) ");
        sentencia.append("\n END)*-1 ELSE");
        sentencia.append("\n (CASE MAX(cl.naturaleza) ");
        sentencia.append("\n   WHEN 'D' ");
        sentencia.append("\n        THEN (( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") ) - ( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") )) ");
        sentencia.append("\n        ELSE (( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") ) - ( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") )) ");
        sentencia.append("\n END) END AS saldo_actual ");
        sentencia.append("\n  FROM 	rf_tr_cuentas_contables cc, ");
        sentencia.append("\n 	rf_tc_clasificador_cuentas cl ");
        sentencia.append("\n WHERE cc.cuenta_mayor_id             = cl.cuenta_mayor_id ");
        sentencia.append("\n AND cc.nivel                         = ").append(getNivel());
        sentencia.append("\n AND cc.id_catalogo_cuenta            = 1  ");
        sentencia.append("\n  AND TO_CHAR(cc.fecha_vig_ini,'yyyy')= ").append(ejercicio);
        sentencia.append("\n AND substr(cc.cuenta_contable,1,3) between 322 and 329 and substr(cc.cuenta_contable,1,3) != 325 ");

        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(ejercicio).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('unidad'  ,cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('unidad'  ,cc.cuenta_mayor_id,").append(ejercicio).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('ambito'  ,cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('ambito'  ,cc.cuenta_mayor_id,").append(ejercicio).append(")) = '").append(getAmbito()).append("' ");
        sentencia.append("\n GROUP BY 'S1', SUBSTR(cc.cuenta_contable, 1, 3)||'N'");
        sentencia.append("\n ) ");
        sentencia.append("\n WHERE ");
        sentencia.append("\n ( saldo_actual!= 0   ");
        sentencia.append("\n OR saldo_anterior  != 0)  ");

        sentencia.append("\n   UNION ALL ");
        sentencia.append("\n   SELECT reporte,seccion,n1, descripcion, ");
        sentencia.append("\n     SUM(variacion) AS variacion, ");
        sentencia.append("\n     SUM(porcentaje) AS porcentaje, ");
        sentencia.append("\n     SUM(saldo_actual) AS saldo_actual, ");
        sentencia.append("\n     SUM(saldo_anterior) AS saldo_anterior ");
        sentencia.append("\n FROM( ");
        sentencia.append("\n SELECT 'LB' AS reporte,'S1' seccion,");
        sentencia.append("\n SUBSTR(cuenta_contable, 1, 1)||'999' n1, ");
        sentencia.append("\n 'Total Hacienda Pública / Patrimonio' AS descripcion, ");
        sentencia.append("\n  (saldo_actual- saldo_anterior) AS variacion, ");
        sentencia.append("\n  (CASE ");
        sentencia.append("\n    WHEN (saldo_anterior- saldo_actual)!= 0 ");
        sentencia.append("\n    THEN ");
        sentencia.append("\n      CASE ");
        sentencia.append("\n        WHEN saldo_anterior != 0 ");
        sentencia.append("\n        THEN ROUND( ( ((saldo_anterior- saldo_actual)/ saldo_anterior)* 100), 2) ");
        sentencia.append("\n        ELSE ");
        sentencia.append("\n          CASE ");
        sentencia.append("\n           WHEN (saldo_anterior- saldo_actual)>0 ");
        sentencia.append("\n            THEN 100 ");
        sentencia.append("\n            ELSE -100 ");
        sentencia.append("\n          END ");
        sentencia.append("\n      END ");
        sentencia.append("\n    ELSE 0 ");
        sentencia.append("\n  END )*-1 AS porcentaje, ");
        sentencia.append("\n  ( CASE ");
        sentencia.append("\n      WHEN (saldo_actual - saldo_anterior) != 0 ");
        sentencia.append("\n       THEN ");
        sentencia.append("\n        CASE ");
        sentencia.append("\n          WHEN (saldo_actual - saldo_anterior) > 0 ");
        sentencia.append("\n             THEN (saldo_actual - saldo_anterior) ");
        sentencia.append("\n             ELSE 0 ");
        sentencia.append("\n        END ");
        sentencia.append("\n      ELSE 0 ");
        sentencia.append("\n    END ) AS SALDO_ACTUAL,");//(SALDO_ACTUAL)Es el campo ORIGEN
        sentencia.append("\n  ( CASE ");
        sentencia.append("\n      WHEN (saldo_actual - saldo_anterior) != 0 ");
        sentencia.append("\n       THEN ");
        sentencia.append("\n        CASE ");
        sentencia.append("\n          WHEN (saldo_actual - saldo_anterior) < 0 ");
        sentencia.append("\n             THEN (saldo_actual - saldo_anterior) ");
        sentencia.append("\n             ELSE 0 ");
        sentencia.append("\n        END ");
        sentencia.append("\n      ELSE 0 ");
        sentencia.append("\n    END ) SALDO_ANTERIOR ");//(SALDO_ANTERIOR)Es el campo APLICACION 
        sentencia.append("\n FROM ( ");


        sentencia.append("\n SELECT  SUBSTR(cc.cuenta_contable, 1, 3) AS cuenta_contable, ");
        sentencia.append("\n CASE WHEN SUBSTR(cc.cuenta_contable, 1, 3)='322' THEN  ");
        sentencia.append("\n  (CASE MAX(cl.naturaleza) ");
        sentencia.append("\n   WHEN 'D' ");
        sentencia.append("\n      THEN SUM(").append(mesAntCargo1).append(") - SUM(").append(mesAntAbono1).append(")");
        sentencia.append("\n      ELSE (SUM(").append(mesAntAbono1).append(") - SUM(").append(mesAntCargo1).append("))");
        sentencia.append("\n END)*-1 ELSE ");
        sentencia.append("\n  (CASE MAX(cl.naturaleza) ");
        sentencia.append("\n   WHEN 'D' ");
        sentencia.append("\n      THEN SUM(").append(mesAntCargo1).append(") - SUM(").append(mesAntAbono1).append(")");
        sentencia.append("\n      ELSE (SUM(").append(mesAntAbono1).append(") - SUM(").append(mesAntCargo1).append("))");
        sentencia.append("\n END) END AS saldo_anterior, ");
        sentencia.append("\n CASE WHEN SUBSTR(cc.cuenta_contable, 1, 3)='322' THEN  ");
        sentencia.append("\n (CASE MAX(cl.naturaleza) ");
        sentencia.append("\n   WHEN 'D' ");
        sentencia.append("\n        THEN (( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") ) - ( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") )) ");
        sentencia.append("\n        ELSE (( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") ) - ( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") )) ");
        sentencia.append("\n END)*-1 ELSE");
        sentencia.append("\n (CASE MAX(cl.naturaleza) ");
        sentencia.append("\n   WHEN 'D' ");
        sentencia.append("\n        THEN (( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") ) - ( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") )) ");
        sentencia.append("\n        ELSE (( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") ) - ( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") )) ");
        sentencia.append("\n END) END AS saldo_actual ");
        sentencia.append("\n  FROM 	rf_tr_cuentas_contables cc, ");
        sentencia.append("\n 	rf_tc_clasificador_cuentas cl ");
        sentencia.append("\n WHERE cc.cuenta_mayor_id             = cl.cuenta_mayor_id ");
        sentencia.append("\n AND cc.nivel                         = ").append(getNivel());
        sentencia.append("\n AND cc.id_catalogo_cuenta            = 1  ");
        sentencia.append("\n  AND TO_CHAR(cc.fecha_vig_ini,'yyyy')= ").append(ejercicio);
        sentencia.append("\n AND substr(cc.cuenta_contable,1,3) >= 331 and substr(cc.cuenta_contable,1,3) <= 332    ");

        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(ejercicio).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('unidad'  ,cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('unidad'  ,cc.cuenta_mayor_id,").append(ejercicio).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('ambito'  ,cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('ambito'  ,cc.cuenta_mayor_id,").append(ejercicio).append(")) = '").append(getAmbito()).append("' ");
        sentencia.append("\n GROUP BY SUBSTR(cc.cuenta_contable, 1, 3)) ");
        
//////// fin de 330
        
        sentencia.append("\n UNION ALL ");
        sentencia.append("\n SELECT  '321' AS cuenta_contable, ");
        sentencia.append("\n  SUM(saldo_anterior)                    AS saldo_anterior, ");
        sentencia.append("\n  SUM(saldo_actual)                      AS saldo_actual");
        sentencia.append("\n  FROM ");
        sentencia.append("\n  (");
        sentencia.append("\n     SELECT CASE cl.naturaleza");
        sentencia.append("\n         WHEN 'D' ");
        sentencia.append("\n		     THEN (( ").append(mesAntCargo2).append(" ) - ( ").append(mesAntAbono2).append(" ))  ");
        sentencia.append("\n		     ELSE (( ").append(mesAntAbono2).append(" ) - ( ").append(mesAntCargo2).append(" ))  ");
        sentencia.append("\n         END");
        sentencia.append("\n          AS saldo_anterior,");
        sentencia.append("\n       CASE cl.naturaleza");
        sentencia.append("\n         WHEN 'D'");
        sentencia.append("\n			THEN (( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ) - ( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ))  ");
        sentencia.append("\n			ELSE (( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ) - ( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ))  ");
        sentencia.append("\n		END AS saldo_actual ");
        sentencia.append("\n	FROM rf_tr_cuentas_contables cc, ");
        sentencia.append("\n		 rf_tc_clasificador_cuentas cl ");
        sentencia.append("\n	WHERE cc.cuenta_mayor_id = cl.cuenta_mayor_id ");
        sentencia.append("\n		AND cc.id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta()).append(" ");
        sentencia.append("\n		AND extract(year from cc.fecha_vig_ini) = ").append(controlReg.getEjercicio()).append(" ");
        sentencia.append("\n	   AND (   cc.cuenta_contable LIKE '4_%'");
        sentencia.append("\n	      OR cc.cuenta_contable LIKE '41_%'");
        sentencia.append("\n	      OR cc.cuenta_contable LIKE '42_%'");
        sentencia.append("\n	      OR cc.cuenta_contable LIKE '43_%')");
        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getAmbito()).append("' ");
        sentencia.append("\n     AND cc.nivel = ").append(getNivel());
         sentencia.append("\n	--ANEXO2");
         sentencia.append("\n	UNION ALL");
         sentencia.append("\n	SELECT CASE cl.naturaleza");
        sentencia.append("\n	     WHEN 'D' ");
         sentencia.append("\n		     THEN (( ").append(mesAntCargo2).append(" ) - ( ").append(mesAntAbono2).append(" ))  ");
        sentencia.append("\n		     ELSE (( ").append(mesAntAbono2).append(" ) - ( ").append(mesAntCargo2).append(" ))  ");
        sentencia.append("\n	    END");
        sentencia.append("\n	       AS saldo_anterior,");
        sentencia.append("\n	    CASE cl.naturaleza");
        sentencia.append("\n	    WHEN 'D'");
        sentencia.append("\n			THEN (( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ) - ( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ))  ");
        sentencia.append("\n			ELSE (( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ) - ( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ))  ");
        sentencia.append("\n		END AS saldo_actual ");
        sentencia.append("\n	FROM rf_tr_cuentas_contables cc, ");
        sentencia.append("\n		 rf_tc_clasificador_cuentas cl ");
        sentencia.append("\n	WHERE cc.cuenta_mayor_id = cl.cuenta_mayor_id ");
        sentencia.append("\n		AND cc.id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta()).append(" ");
        sentencia.append("\n		AND extract(year from cc.fecha_vig_ini) = ").append(controlReg.getEjercicio()).append(" ");
        sentencia.append("\n	  AND (   cc.cuenta_contable LIKE '311%')");
        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getAmbito()).append("' ");
        sentencia.append("\n     AND cc.nivel = ").append(getNivel());
         sentencia.append("\n	UNION ALL");
         sentencia.append("\n	SELECT CASE cl.naturaleza");
        sentencia.append("\n	     WHEN 'D' ");
        sentencia.append("\n		     THEN (( ").append(mesAntCargo2).append(" ) - ( ").append(mesAntAbono2).append(" ))  ");
        sentencia.append("\n		     ELSE (( ").append(mesAntAbono2).append(" ) - ( ").append(mesAntCargo2).append(" ))  ");
        sentencia.append("\n	   END");
        sentencia.append("\n	       AS saldo_anterior,");
        sentencia.append("\n	    CASE cl.naturaleza");
        sentencia.append("\n	      WHEN 'D'");
        sentencia.append("\n			THEN (( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ) - ( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ))  ");
        sentencia.append("\n			ELSE (( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ) - ( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ))  ");
        sentencia.append("\n		END AS saldo_actual ");
        sentencia.append("\n	FROM rf_tr_cuentas_contables cc, ");
        sentencia.append("\n		 rf_tc_clasificador_cuentas cl ");
        sentencia.append("\n	WHERE cc.cuenta_mayor_id = cl.cuenta_mayor_id ");
        sentencia.append("\n		AND cc.id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta()).append(" ");
        sentencia.append("\n		AND extract(year from cc.fecha_vig_ini) = ").append(controlReg.getEjercicio()).append(" ");
        sentencia.append("\n	    AND (   cc.cuenta_contable LIKE '325%')");
        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getAmbito()).append("' ");
        sentencia.append("\n     AND cc.nivel = ").append(getNivel());
         sentencia.append("\n	--FIN ANEXO2  ");
        sentencia.append("\n	 UNION ALL ");
        sentencia.append("\n	SELECT CASE cl.naturaleza");
        sentencia.append("\n	    WHEN 'D' ");
        sentencia.append("\n		     THEN (( ").append(mesAntCargo2).append(" ) - ( ").append(mesAntAbono2).append(" ))  ");
        sentencia.append("\n		     ELSE (( ").append(mesAntAbono2).append(" ) - ( ").append(mesAntCargo2).append(" ))  ");
        sentencia.append("\n	    END");
        sentencia.append("\n	       AS saldo_anterior,");
        sentencia.append("\n	    CASE cl.naturaleza");
        sentencia.append("\n	       WHEN 'D'");
        sentencia.append("\n			THEN (( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ) - ( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ))  ");
        sentencia.append("\n			ELSE (( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ) - ( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ))  ");
        sentencia.append("\n		END AS saldo_actual ");
        sentencia.append("\n	FROM rf_tr_cuentas_contables cc, ");
        sentencia.append("\n		 rf_tc_clasificador_cuentas cl ");
        sentencia.append("\n	WHERE cc.cuenta_mayor_id = cl.cuenta_mayor_id ");
        sentencia.append("\n		AND cc.id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta()).append(" ");
        sentencia.append("\n		AND extract(year from cc.fecha_vig_ini) = ").append(controlReg.getEjercicio()).append(" ");
        sentencia.append("\n	   AND ( --  cc.cuenta_contable LIKE '43_%'");
        sentencia.append("\n	       -- OR ");
        sentencia.append("\n	        cc.cuenta_contable LIKE '5_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '51_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '52_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '53_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '54_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '55_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '825_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '826_%')");
        sentencia.append("\n	     AND ( cc.cuenta_contable NOT LIKE '511_%'");
        sentencia.append("\n	       AND cc.cuenta_contable NOT LIKE '512_%'");
        sentencia.append("\n	       AND cc.cuenta_contable NOT LIKE '513_%'");
        sentencia.append("\n	       AND cc.cuenta_contable NOT LIKE '8255_%'");
        sentencia.append("\n	       AND cc.cuenta_contable NOT LIKE '8256_%'");
        sentencia.append("\n	       AND cc.cuenta_contable NOT LIKE '8257_%'");
        sentencia.append("\n	       AND cc.cuenta_contable NOT LIKE '8265_%'");
        sentencia.append("\n	       AND cc.cuenta_contable NOT LIKE '8266_%'");
        sentencia.append("\n	       AND cc.cuenta_contable NOT LIKE '8267_%')");
        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getAmbito()).append("' ");
        sentencia.append("\n     AND cc.nivel = ").append(getNivel());
        sentencia.append("\n	--FIN ANEXO3");
        sentencia.append("\n     UNION ALL");
        sentencia.append("\n     SELECT ");
        sentencia.append("\n     CASE cl.naturaleza");
        sentencia.append("\n        WHEN 'C'");
        sentencia.append("\n                 THEN (( ").append(mesAntCargo2).append(" ) - ( ").append(mesAntAbono2).append("  )) ");
        sentencia.append("\n                 ELSE (( ").append(mesAntAbono2).append(" ) - ( ").append(mesAntCargo2).append("  )) ");
        sentencia.append("\n     END");
        sentencia.append("\n        AS saldo_anterior,");
        sentencia.append("\n     CASE cl.naturaleza");
        sentencia.append("\n        WHEN 'C'");
        sentencia.append("\n                 THEN (( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ) - ( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" )) ");
        sentencia.append("\n                 ELSE (( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ) - ( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" )) ");
        sentencia.append("\n     END");
        sentencia.append("\n        AS saldo_actual");
        sentencia.append("\n     FROM rf_tr_cuentas_contables cc,");
        sentencia.append("\n     rf_tc_clasificador_cuentas cl");
        sentencia.append("\n     WHERE cc.cuenta_mayor_id = cl.cuenta_mayor_id");
        sentencia.append("\n     AND cc.id_catalogo_cuenta = 1");
        sentencia.append("\n		AND extract(year from cc.fecha_vig_ini) = ").append(controlReg.getEjercicio()).append("  ");
        sentencia.append("\n	 AND ( cc.cuenta_contable LIKE '827110001000100910001%'");
        sentencia.append("\n	      or cc.cuenta_contable LIKE '827110001000100911000%')");
        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }

        sentencia.append("\n     UNION ALL");
        sentencia.append("\n     SELECT ");
        sentencia.append("\n     CASE cl.naturaleza");
        sentencia.append("\n        WHEN 'C'");
        sentencia.append("\n                 THEN (( ").append(mesAntCargo2).append(" ) - ( ").append(mesAntAbono2).append("  )) ");
        sentencia.append("\n                 ELSE (( ").append(mesAntAbono2).append(" ) - ( ").append(mesAntCargo2).append("  )) ");
        sentencia.append("\n     END");
        sentencia.append("\n        AS saldo_anterior,");
        sentencia.append("\n     CASE cl.naturaleza");
        sentencia.append("\n        WHEN 'C'");
        sentencia.append("\n                 THEN (( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ) - ( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" )) ");
        sentencia.append("\n                 ELSE (( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ) - ( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" )) ");
        sentencia.append("\n     END");
        sentencia.append("\n        AS saldo_actual");
        sentencia.append("\n     FROM rf_tr_cuentas_contables cc,");
        sentencia.append("\n     rf_tc_clasificador_cuentas cl");
        sentencia.append("\n     WHERE cc.cuenta_mayor_id = cl.cuenta_mayor_id");
        sentencia.append("\n     AND cc.id_catalogo_cuenta = 1");
        sentencia.append("\n		AND extract(year from cc.fecha_vig_ini) = ").append(controlReg.getEjercicio()).append("  ");
        sentencia.append("\n	 AND ( cc.cuenta_contable LIKE '827110001000100910002%'");
        sentencia.append("\n	      or cc.cuenta_contable LIKE '827110001000100912000%')");
        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }

        sentencia.append("\n     UNION ALL");
        sentencia.append("\n     SELECT ");
        sentencia.append("\n     CASE cl.naturaleza");
        sentencia.append("\n        WHEN 'C'");
        sentencia.append("\n                 THEN (( ").append(mesAntCargo2).append(" ) - ( ").append(mesAntAbono2).append("  )) ");
        sentencia.append("\n                 ELSE (( ").append(mesAntAbono2).append(" ) - ( ").append(mesAntCargo2).append("  )) ");
        sentencia.append("\n     END");
        sentencia.append("\n        AS saldo_anterior,");
        sentencia.append("\n     CASE cl.naturaleza");
        sentencia.append("\n        WHEN 'C'");
        sentencia.append("\n                 THEN (( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ) - ( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" )) ");
        sentencia.append("\n                 ELSE (( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ) - ( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" )) ");
        sentencia.append("\n     END");
        sentencia.append("\n        AS saldo_actual");
        sentencia.append("\n     FROM rf_tr_cuentas_contables cc,");
        sentencia.append("\n     rf_tc_clasificador_cuentas cl");
        sentencia.append("\n     WHERE cc.cuenta_mayor_id = cl.cuenta_mayor_id");
        sentencia.append("\n     AND cc.id_catalogo_cuenta = 1");
        sentencia.append("\n		AND extract(year from cc.fecha_vig_ini) = ").append(controlReg.getEjercicio()).append("  ");
        sentencia.append("\n	 AND ( cc.cuenta_contable LIKE '827110001000100910003%'");
        sentencia.append("\n	      or cc.cuenta_contable LIKE '827110001000100913000%')");
        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }
        
        sentencia.append("\n     UNION ALL");
        sentencia.append("\n     SELECT ");
        sentencia.append("\n     CASE cl.naturaleza");
        sentencia.append("\n        WHEN 'D'");
        sentencia.append("\n                 THEN (( ").append(mesAntCargo2).append(" ) - ( ").append(mesAntAbono2).append("  )) ");
        sentencia.append("\n                 ELSE (( ").append(mesAntAbono2).append(" ) - ( ").append(mesAntCargo2).append("  )) ");
        sentencia.append("\n     END");
        sentencia.append("\n        AS saldo_anterior,");
        sentencia.append("\n     CASE cl.naturaleza");
        sentencia.append("\n        WHEN 'D'");
        sentencia.append("\n                 THEN (( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ) - ( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" )) ");
        sentencia.append("\n                 ELSE (( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ) - ( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" )) ");
        sentencia.append("\n     END");
        sentencia.append("\n        AS saldo_actual");
        sentencia.append("\n     FROM rf_tr_cuentas_contables cc,");
        sentencia.append("\n     rf_tc_clasificador_cuentas cl");
        sentencia.append("\n     WHERE cc.cuenta_mayor_id = cl.cuenta_mayor_id");
        sentencia.append("\n     AND cc.id_catalogo_cuenta = 1");
        sentencia.append("\n		AND extract(year from cc.fecha_vig_ini) = ").append(controlReg.getEjercicio()).append("  ");
        sentencia.append("\n	 AND ( cc.cuenta_contable LIKE '826110001000100910004%'");
        sentencia.append("\n	      or cc.cuenta_contable LIKE '826110001000100914000%')");
        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n   )");
        sentencia.append("\n ) ");
        sentencia.append("\n ) ");

        sentencia.append("\n  GROUP BY reporte,seccion,n1, descripcion ");
        sentencia.append("\n ) com ");
        sentencia.append("\n  ORDER BY seccion, reporte,  n1, descripcion ");

        //System.out.println(" "+sentencia.toString());
        mes_ant2 = null;
        return sentencia;
    }

    public StringBuffer situacionFinanciera(String fecha_consolidacion, String mes, String mesCompara, String ejercicioCompara) {
        //Variables que se refieren al mes del Filtro
        String mesAntCargo1;
        String mesAntAbono1;
        String mesActCargo1;
        String mesActAbono1;

        //Variables que se refieren al mes a Comparar
        String mesAntCargo2;
        String mesAntAbono2;

        int ejercicio = getControlReg().getEjercicio();
        Fecha fechaPeriodo = new Fecha(fecha_consolidacion, "/");
        String mes_act1 = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodo.getMes() - 1);
        fechaPeriodo.addMeses(-1);
        String mes_ant1 = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodo.getMes() - 1);
        int diasMes = sia.libs.formato.Fecha.getDiasEnElMes(Integer.valueOf(ejercicioCompara), Integer.valueOf(mesCompara) - 1);
        Fecha fechaPeriodoAnt = new Fecha(ejercicioCompara + mesCompara + String.valueOf(diasMes), "/");
        String mes_act2 = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodoAnt.getMes() - 1);
        fechaPeriodoAnt.addMeses(-1);
        String mes_ant2 = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodoAnt.getMes() - 1);
        StringBuffer sentencia = new StringBuffer();
        setNivel(!getPrograma().equals("'0000'") && getUnidad().equals("0000") && getAmbito().equals("0000") ? "3" : getNivel());

        if (mes_ant1.equals("DIC")) {
            mesAntCargo2 = construirMes(mes_act1, "cargo_ini");
            mesAntAbono2 = construirMes(mes_act1, "abono_ini");
            mesAntCargo1 = construirMes(mes_act1, "cargo_ini");
            mesAntAbono1 = construirMes(mes_act1, "abono_ini");
        } else {
            if (mes_act2.equals("DIC")) {
                mesAntCargo2 = "ene_cargo_ini";
                mesAntAbono2 = "ene_abono_ini";
            } else {
                mesAntCargo2 = construirMes(mes_act2, "cargo_acum");
                mesAntAbono2 = construirMes(mes_act2, "abono_acum");
            }
            mesAntCargo1 = construirMes(mes_ant1, "cargo_acum");
            mesAntAbono1 = construirMes(mes_ant1, "abono_acum");
        }
        mesActCargo1 = construirMes(mes_act1, "cargo");
        mesActAbono1 = construirMes(mes_act1, "abono");

        sentencia.append("\n SELECT 'LA' AS reporte, ");
        sentencia.append("\n seccion, ");
        sentencia.append("\n n1, ");
        sentencia.append("\n DECODE(n1, '1199', 'Total de Activo Circulante', '1299', 'Total de Activo No Circulante', Initcap(descripcion)) AS descripcion, ");
        sentencia.append("\n saldo_anterior, ");
        sentencia.append("\n saldo_actual, ");
        sentencia.append("\n (saldo_actual- saldo_anterior) AS variacion, ");
        sentencia.append("\n ( CASE ");
        sentencia.append("\n     WHEN (saldo_anterior- saldo_actual)!= 0 ");
        sentencia.append("\n     THEN ");
        sentencia.append("\n       CASE ");
        sentencia.append("\n         WHEN saldo_anterior != 0 ");
        sentencia.append("\n         THEN ROUND( ( ((saldo_anterior- saldo_actual)/ saldo_anterior)* 100), 2) ");
        sentencia.append("\n         ELSE ");
        sentencia.append("\n           CASE ");
        sentencia.append("\n             WHEN (saldo_anterior- saldo_actual)>0 ");
        sentencia.append("\n             THEN 100 ");
        sentencia.append("\n             ELSE -100 ");
        sentencia.append("\n           END ");
        sentencia.append("\n       END ");
        sentencia.append("\n     ELSE 0 ");
        sentencia.append("\n   END )*-1 AS porcentaje ");
        sentencia.append("\n FROM (");
        sentencia.append("\n  SELECT 'S1' AS seccion, ");
        sentencia.append("\n    SUBSTR(cc.cuenta_contable, 1, 3)||'N'   AS n1,  ");
        sentencia.append("\n    'XDescripcion SubTotales' AS descripcion, ");
        sentencia.append("\n    CASE MAX(cl.naturaleza) ");
        sentencia.append("\n      WHEN 'D' ");
        sentencia.append("\n        THEN    SUM(").append(mesAntCargo2).append(") - SUM(").append(mesAntAbono2).append(")");
        sentencia.append("\n        ELSE (  SUM(").append(mesAntAbono2).append(") - SUM(").append(mesAntCargo2).append("))");
        sentencia.append("\n    END saldo_anterior, ");
        sentencia.append("\n    CASE MAX(cl.naturaleza) ");
        sentencia.append("\n      WHEN 'D' ");
        sentencia.append("\n        THEN (( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") ) - ( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") )) ");
        sentencia.append("\n        ELSE (( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") ) - ( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") )) ");
        sentencia.append("\n    END AS saldo_actual ");
        sentencia.append("\n  FROM 	rf_tr_cuentas_contables cc, ");
        sentencia.append("\n		rf_tc_clasificador_cuentas cl ");
        sentencia.append("\n  WHERE cc.cuenta_mayor_id            = cl.cuenta_mayor_id ");
        sentencia.append("\n  AND cc.nivel                        = ").append(getNivel());
        sentencia.append("\n  AND cc.id_catalogo_cuenta           = 1 ");
        sentencia.append("\n  AND TO_CHAR(cc.fecha_vig_ini,'yyyy')= ").append(ejercicio);
        sentencia.append("\n  AND cc.cuenta_contable LIKE '1_%'   ");

        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(ejercicio).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('unidad'  ,cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('unidad'  ,cc.cuenta_mayor_id,").append(ejercicio).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('ambito'  ,cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('ambito'  ,cc.cuenta_mayor_id,").append(ejercicio).append(")) = '").append(getAmbito()).append("' ");
        sentencia.append("\n   GROUP BY 'S1', SUBSTR(cc.cuenta_contable, 1, 3)||'N' ");
        sentencia.append("\n  UNION ALL ");
        sentencia.append("\n  SELECT 'S1' AS seccion, ");
        sentencia.append("\n  SUBSTR(cc.cuenta_contable, 1, 2)||'99'  AS n1, ");
        sentencia.append("\n    'XDescripcion Totales' AS descripcion, ");
        sentencia.append("\n    CASE MAX(cl.naturaleza) ");
        sentencia.append("\n      WHEN 'D' ");
        sentencia.append("\n        THEN    SUM(").append(mesAntCargo2).append(") - SUM(").append(mesAntAbono2).append(")");
        sentencia.append("\n        ELSE (  SUM(").append(mesAntAbono2).append(") - SUM(").append(mesAntCargo2).append("))");
        sentencia.append("\n    END saldo_anterior, ");
        sentencia.append("\n    CASE MAX(cl.naturaleza) ");
        sentencia.append("\n      WHEN 'D' ");
        sentencia.append("\n        THEN (( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") ) - ( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") )) ");
        sentencia.append("\n        ELSE (( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") ) - ( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") )) ");
        sentencia.append("\n    END AS saldo_actual ");
        sentencia.append("\n  FROM 	rf_tr_cuentas_contables cc, ");
        sentencia.append("\n		rf_tc_clasificador_cuentas cl ");
        sentencia.append("\n  WHERE cc.cuenta_mayor_id            = cl.cuenta_mayor_id ");
        sentencia.append("\n  AND cc.nivel                        = ").append(getNivel());
        sentencia.append("\n  AND cc.id_catalogo_cuenta           = 1 ");
        sentencia.append("\n  AND TO_CHAR(cc.fecha_vig_ini,'yyyy')= ").append(ejercicio);
        sentencia.append("\n  AND         cc.cuenta_contable LIKE '1_%'   ");

        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(ejercicio).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('unidad'  ,cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('unidad'  ,cc.cuenta_mayor_id,").append(ejercicio).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('ambito'  ,cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('ambito'  ,cc.cuenta_mayor_id,").append(ejercicio).append(")) = '").append(getAmbito()).append("' ");
        sentencia.append("\n  GROUP BY 'S1', SUBSTR(cc.cuenta_contable, 1, 2)||'99' ");
        sentencia.append("\n  ) ");
        //sentencia.append("\n WHERE (saldo_actual!= 0 ");
        //sentencia.append("\n OR saldo_anterior  != 0) ");
        /*TERMINA EL QUERY DEL ACTIVO*/
        sentencia.append("\n UNION ALL ");
        sentencia.append("\n SELECT 'LB' AS reporte, ");
        sentencia.append("\n seccion, ");
        sentencia.append("\n   n1, ");
        sentencia.append("\n  DECODE(n1, '21NN', 'Total de Pasivo Circulante', '22NN', 'Total Pasivo No Circulante', '70NN', 'TOTAL CUENTAS DE ORDEN', Initcap(descripcion)) AS descripcion, ");
        sentencia.append("\n  saldo_anterior, ");
        sentencia.append("\n  saldo_actual, ");
        sentencia.append("\n  (saldo_actual- saldo_anterior) AS variacion, ");
        sentencia.append("\n  ( ");
        sentencia.append("\n  CASE ");
        sentencia.append("\n    WHEN (saldo_anterior- saldo_actual)!= 0 ");
        sentencia.append("\n    THEN ");
        sentencia.append("\n      CASE ");
        sentencia.append("\n        WHEN saldo_anterior != 0 ");
        sentencia.append("\n        THEN ROUND( ( ((saldo_anterior- saldo_actual)/ saldo_anterior)* 100), 2) ");
        sentencia.append("\n        ELSE ");
        sentencia.append("\n          CASE ");
        sentencia.append("\n           WHEN (saldo_anterior- saldo_actual)>0 ");
        sentencia.append("\n            THEN 100 ");
        sentencia.append("\n            ELSE -100 ");
        sentencia.append("\n          END ");
        sentencia.append("\n      END ");
        sentencia.append("\n    ELSE 0 ");
        sentencia.append("\n  END )*-1 AS porcentaje ");
        sentencia.append("\nFROM ( ");
        sentencia.append("\n  SELECT 'S1' AS seccion, ");
        sentencia.append("\n    SUBSTR(cc.cuenta_contable, 1, 3)||'N'  AS n1, ");
        sentencia.append("\n    'XDescripcion SubTotales' AS descripcion, ");
        sentencia.append("\n    CASE MAX(cl.naturaleza) ");
        sentencia.append("\n      WHEN 'C' ");
        sentencia.append("\n        THEN    SUM(").append(mesAntCargo2).append(") - SUM(").append(mesAntAbono2).append(")");
        sentencia.append("\n        ELSE (  SUM(").append(mesAntAbono2).append(") - SUM(").append(mesAntCargo2).append("))");
        sentencia.append("\n    END saldo_anterior, ");
        sentencia.append("\n    CASE MAX(cl.naturaleza) ");
        sentencia.append("\n      WHEN 'C' ");
        sentencia.append("\n        THEN (( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(")) - (SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") )) ");
        sentencia.append("\n        ELSE (( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(")) - (SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") )) ");
        sentencia.append("\n    END AS saldo_actual ");
        sentencia.append("\n  FROM 	rf_tr_cuentas_contables    cc, ");
        sentencia.append("\n		rf_tc_clasificador_cuentas cl ");
        sentencia.append("\n  WHERE cc.cuenta_mayor_id             = cl.cuenta_mayor_id ");
        sentencia.append("\n  AND cc.nivel                         = ").append(getNivel());
        sentencia.append("\n  AND cc.id_catalogo_cuenta            = 1 ");
        sentencia.append("\n  AND TO_CHAR(cc.fecha_vig_ini,'yyyy') = ").append(ejercicio);
        sentencia.append("\n  AND cc.cuenta_contable LIKE '2_%' ");

        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(ejercicio).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('unidad'  ,cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('unidad'  ,cc.cuenta_mayor_id,").append(ejercicio).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('ambito'  ,cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('ambito'  ,cc.cuenta_mayor_id,").append(ejercicio).append(")) = '").append(getAmbito()).append("' ");
        sentencia.append("\n  GROUP BY 'S1', SUBSTR(cc.cuenta_contable, 1, 3)||'N' ");
        sentencia.append("\n  UNION ALL ");
        sentencia.append("\n  SELECT 'S1' AS seccion, ");
        sentencia.append("\n     SUBSTR(cc.cuenta_contable, 1, 2)||'99'  AS n1,  ");
        sentencia.append("\n    'XDescripcion Totales' AS descripcion, ");
        sentencia.append("\n   CASE MAX(cl.naturaleza) ");
        sentencia.append("\n      WHEN 'C' ");
        sentencia.append("\n        THEN    SUM(").append(mesAntCargo2).append(") - SUM(").append(mesAntAbono2).append(")");
        sentencia.append("\n        ELSE (  SUM(").append(mesAntAbono2).append(") - SUM(").append(mesAntCargo2).append("))");
        sentencia.append("\n    END saldo_anterior, ");
        sentencia.append("\n    CASE MAX(cl.naturaleza) ");
        sentencia.append("\n      WHEN 'C' ");
        sentencia.append("\n        THEN (( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") ) - ( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") )) ");
        sentencia.append("\n        ELSE (( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") ) - ( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") )) ");
        sentencia.append("\n    END AS saldo_actual ");
        sentencia.append("\n  FROM  rf_tr_cuentas_contables    cc, ");
        sentencia.append("\n	    rf_tc_clasificador_cuentas cl ");
        sentencia.append("\n  WHERE cc.cuenta_mayor_id            = cl.cuenta_mayor_id ");
        sentencia.append("\n  AND cc.nivel                        = ").append(getNivel());
        sentencia.append("\n  AND cc.id_catalogo_cuenta           = 1 ");
        sentencia.append("\n  AND TO_CHAR(cc.fecha_vig_ini,'yyyy')= ").append(ejercicio);
        sentencia.append("\n  AND     cc.cuenta_contable LIKE '2_%' ");

        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(ejercicio).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('unidad'  ,cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('unidad'  ,cc.cuenta_mayor_id,").append(ejercicio).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('ambito'  ,cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('ambito'  ,cc.cuenta_mayor_id,").append(ejercicio).append(")) = '").append(getAmbito()).append("' ");
        sentencia.append("\n  GROUP BY  'S1', SUBSTR(cc.cuenta_contable, 1, 2)||'99' ");
        sentencia.append("\n  ) ");
        //sentencia.append("\n WHERE (saldo_actual!= 0 ");
        //sentencia.append("\n OR saldo_anterior  != 0) ");
        /*TERMINA PASIVO*/
        /*EMPIEZA HACIENDA*/
        sentencia.append("\n UNION ALL ");
        sentencia.append("\n SELECT 'LB' AS reporte,  ");
        sentencia.append("\n  seccion, ");
        sentencia.append("\n   n1, ");
        sentencia.append("\n   DECODE(n1, '3999', 'Total Hacienda Pública / Patrimonio', descripcion) AS descripcion, ");
        sentencia.append("\n   saldo_anterior, ");
        sentencia.append("\n   saldo_actual, ");
        sentencia.append("\n   (saldo_actual- saldo_anterior) AS variacion, ");
        sentencia.append("\n   ( ");
        sentencia.append("\n   CASE ");
        sentencia.append("\n     WHEN (saldo_anterior- saldo_actual)!= 0 ");
        sentencia.append("\n     THEN ");
        sentencia.append("\n       CASE ");
        sentencia.append("\n         WHEN saldo_anterior != 0 ");
        sentencia.append("\n         THEN ROUND( ( ((saldo_anterior- saldo_actual)/ saldo_anterior)* 100), 2) ");
        sentencia.append("\n         ELSE ");
        sentencia.append("\n           CASE ");
        sentencia.append("\n             WHEN (saldo_anterior- saldo_actual)>0 ");
        sentencia.append("\n             THEN 100 ");
        sentencia.append("\n             ELSE -100 ");
        sentencia.append("\n           END ");
        sentencia.append("\n       END ");
        sentencia.append("\n     ELSE 0 ");
        sentencia.append("\n   END )*-1 AS porcentaje ");
        sentencia.append("\n FROM (");
        sentencia.append("\n  SELECT ");
        sentencia.append("\n  'S1' as seccion,");
        sentencia.append("\n  '312N' AS n1, ");
        sentencia.append("\n  'XDescripcion SubTotales' AS descripcion, ");
        sentencia.append("\n  SUM(saldo_anterior)                    AS saldo_anterior, ");
        sentencia.append("\n  SUM(saldo_actual)                      AS saldo_actual");
        sentencia.append("\n  FROM ");
        sentencia.append("\n  (");
        sentencia.append("\n     SELECT CASE cl.naturaleza");
        sentencia.append("\n         WHEN 'C' ");
        sentencia.append("\n		     THEN (( ").append(mesAntCargo2).append(" ) - ( ").append(mesAntAbono2).append(" ))  ");
        sentencia.append("\n		     ELSE (( ").append(mesAntAbono2).append(" ) - ( ").append(mesAntCargo2).append(" ))  ");
        sentencia.append("\n         END");
        sentencia.append("\n          AS saldo_anterior,");
        sentencia.append("\n       CASE cl.naturaleza");
        sentencia.append("\n         WHEN 'C'");
        sentencia.append("\n			THEN (( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ) - ( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ))  ");
        sentencia.append("\n			ELSE (( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ) - ( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ))  ");
        sentencia.append("\n		END AS saldo_actual ");
        sentencia.append("\n	FROM rf_tr_cuentas_contables cc, ");
        sentencia.append("\n		 rf_tc_clasificador_cuentas cl ");
        sentencia.append("\n	WHERE cc.cuenta_mayor_id = cl.cuenta_mayor_id ");
        sentencia.append("\n		AND cc.id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta()).append(" ");
        sentencia.append("\n		AND extract(year from cc.fecha_vig_ini) = ").append(controlReg.getEjercicio()).append(" ");
        sentencia.append("\n	   AND (   cc.cuenta_contable LIKE '312_%')");
        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getAmbito()).append("' ");
        sentencia.append("\n     AND cc.nivel = ").append(getNivel());
        sentencia.append("\n   )");
        sentencia.append("\n  UNION ALL");
        sentencia.append("\n  SELECT ");
        sentencia.append("\n  'S1' as seccion,");
        sentencia.append("\n  '313N' AS n1, ");
        sentencia.append("\n  'XDescripcion SubTotales' AS descripcion, ");
        sentencia.append("\n  SUM(saldo_anterior)                    AS saldo_anterior, ");
        sentencia.append("\n  SUM(saldo_actual)                      AS saldo_actual");
        sentencia.append("\n  FROM ");
        sentencia.append("\n  (");
        sentencia.append("\n     SELECT CASE cl.naturaleza");
        sentencia.append("\n         WHEN 'C' ");
        sentencia.append("\n		     THEN (( ").append(mesAntCargo2).append(" ) - ( ").append(mesAntAbono2).append(" ))  ");
        sentencia.append("\n		     ELSE (( ").append(mesAntAbono2).append(" ) - ( ").append(mesAntCargo2).append(" ))  ");
        sentencia.append("\n         END");
        sentencia.append("\n          AS saldo_anterior,");
        sentencia.append("\n       CASE cl.naturaleza");
        sentencia.append("\n         WHEN 'C'");
        sentencia.append("\n			THEN (( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ) - ( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ))  ");
        sentencia.append("\n			ELSE (( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ) - ( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ))  ");
        sentencia.append("\n		END AS saldo_actual ");
        sentencia.append("\n	FROM rf_tr_cuentas_contables cc, ");
        sentencia.append("\n		 rf_tc_clasificador_cuentas cl ");
        sentencia.append("\n	WHERE cc.cuenta_mayor_id = cl.cuenta_mayor_id ");
        sentencia.append("\n		AND cc.id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta()).append(" ");
        sentencia.append("\n		AND extract(year from cc.fecha_vig_ini) = ").append(controlReg.getEjercicio()).append(" ");
        sentencia.append("\n	   AND (   cc.cuenta_contable LIKE '313_%')");
        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getAmbito()).append("' ");
        sentencia.append("\n     AND cc.nivel = ").append(getNivel());
        sentencia.append("\n   )");
        sentencia.append("\n  UNION ALL");
        sentencia.append("\n  SELECT ");
        sentencia.append("\n  'S1' as seccion,");
        sentencia.append("\n  '321N' AS n1, ");
        sentencia.append("\n  'XDescripcion SubTotales' AS descripcion, ");
        sentencia.append("\n  SUM(saldo_anterior)                    AS saldo_anterior, ");
        sentencia.append("\n  SUM(saldo_actual)                      AS saldo_actual");
        sentencia.append("\n  FROM ");
        sentencia.append("\n  (");
        sentencia.append("\n     SELECT CASE cl.naturaleza");
        sentencia.append("\n         WHEN 'C' ");
        sentencia.append("\n		     THEN (( ").append(mesAntCargo2).append(" ) - ( ").append(mesAntAbono2).append(" ))  ");
        sentencia.append("\n		     ELSE (( ").append(mesAntAbono2).append(" ) - ( ").append(mesAntCargo2).append(" ))  ");
        sentencia.append("\n         END");
        sentencia.append("\n          AS saldo_anterior,");
        sentencia.append("\n       CASE cl.naturaleza");
        sentencia.append("\n         WHEN 'C'");
        sentencia.append("\n			THEN (( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ) - ( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ))  ");
        sentencia.append("\n			ELSE (( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ) - ( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ))  ");
        sentencia.append("\n		END AS saldo_actual ");
        sentencia.append("\n	FROM rf_tr_cuentas_contables cc, ");
        sentencia.append("\n		 rf_tc_clasificador_cuentas cl ");
        sentencia.append("\n	WHERE cc.cuenta_mayor_id = cl.cuenta_mayor_id ");
        sentencia.append("\n		AND cc.id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta()).append(" ");
        sentencia.append("\n		AND extract(year from cc.fecha_vig_ini) = ").append(controlReg.getEjercicio()).append(" ");
        sentencia.append("\n	   AND (   cc.cuenta_contable LIKE '311_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '431_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '439_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '511_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '512_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '513_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '518_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '524_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '528_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '529_%'");
        sentencia.append("\n            OR cc.cuenta_contable LIKE '551_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '599_%')");
        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getAmbito()).append("' ");
        sentencia.append("\n     AND cc.nivel = ").append(getNivel());
        sentencia.append("\n   )");
        sentencia.append("\n   UNION ALL ");
        sentencia.append("\n   SELECT 'S1'                       AS seccion, ");
        sentencia.append("\n     '3NNN'                          AS n1, ");
        sentencia.append("\n     'HACIENDA PÚBLICA / PATRIMONIO CONSTITUIDO' AS descripcion, ");
        sentencia.append("\n     0 saldo_anterior, ");
        sentencia.append("\n     0   AS saldo_actual ");
        sentencia.append("\n   FROM dual ");

        sentencia.append("\n   UNION ALL ");
        sentencia.append("\n SELECT  'S1' AS seccion, ");
        sentencia.append("\n '325N'   AS n1, ");
        sentencia.append("\n 'XDescripcion SubTotales' AS descripcion, ");
        sentencia.append("\n  SUM (saldo_anterior) AS saldo_anterior, ");
        sentencia.append("\n SUM (saldo_actual) AS saldo_actual ");
        sentencia.append("\n FROM ( ");
        sentencia.append("\n SELECT  ");
        sentencia.append("\n  (SUM(").append(mesAntCargo2).append(") - SUM(").append(mesAntAbono2).append(")");
        sentencia.append("\n )*-1  AS saldo_anterior, ");
        sentencia.append("\n ( (( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") ) - ( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") )) ");
        sentencia.append("\n )*-1 AS saldo_actual ");
        sentencia.append("\n  FROM 	rf_tr_cuentas_contables cc, ");
        sentencia.append("\n 	rf_tc_clasificador_cuentas cl ");
        sentencia.append("\n WHERE cc.cuenta_mayor_id             = cl.cuenta_mayor_id ");
        sentencia.append("\n AND cc.nivel                         = ").append(getNivel());
        sentencia.append("\n AND cc.id_catalogo_cuenta            = 1  ");
        sentencia.append("\n  AND TO_CHAR(cc.fecha_vig_ini,'yyyy')= ").append(ejercicio);
        sentencia.append("\n AND substr(cc.cuenta_contable,1,3) = 325    ");

        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(ejercicio).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('unidad'  ,cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('unidad'  ,cc.cuenta_mayor_id,").append(ejercicio).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('ambito'  ,cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('ambito'  ,cc.cuenta_mayor_id,").append(ejercicio).append(")) = '").append(getAmbito()).append("' ");
        sentencia.append("\n GROUP BY 'S1')");

//////// 330
        sentencia.append("\n   UNION ALL ");
        sentencia.append("\n SELECT  'S1' AS seccion, ");
        sentencia.append("\n '330N'   AS n1, ");
        sentencia.append("\n 'XDescripcion SubTotales' AS descripcion, ");
        sentencia.append("\n  SUM (saldo_anterior) AS saldo_anterior, ");
        sentencia.append("\n SUM (saldo_actual) AS saldo_actual ");
        sentencia.append("\n FROM ( ");
        sentencia.append("\n SELECT  ");
        sentencia.append("\n  (SUM(").append(mesAntCargo2).append(") - SUM(").append(mesAntAbono2).append(")");
        sentencia.append("\n )*-1  AS saldo_anterior, ");
        sentencia.append("\n ( (( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") ) - ( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") )) ");
        sentencia.append("\n )*-1 AS saldo_actual ");
        sentencia.append("\n  FROM 	rf_tr_cuentas_contables cc, ");
        sentencia.append("\n 	rf_tc_clasificador_cuentas cl ");
        sentencia.append("\n WHERE cc.cuenta_mayor_id             = cl.cuenta_mayor_id ");
        sentencia.append("\n AND cc.nivel                         = ").append(getNivel());
        sentencia.append("\n AND cc.id_catalogo_cuenta            = 1  ");
        sentencia.append("\n  AND TO_CHAR(cc.fecha_vig_ini,'yyyy')= ").append(ejercicio);
        sentencia.append("\n AND substr(cc.cuenta_contable,1,3) >= 331 and substr(cc.cuenta_contable,1,3) <= 332    ");

        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(ejercicio).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('unidad'  ,cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('unidad'  ,cc.cuenta_mayor_id,").append(ejercicio).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('ambito'  ,cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('ambito'  ,cc.cuenta_mayor_id,").append(ejercicio).append(")) = '").append(getAmbito()).append("' ");
        sentencia.append("\n GROUP BY 'S1')");
        
//////// fin de 330

        sentencia.append("\n   UNION ALL ");
        sentencia.append("\n SELECT  'S1' AS seccion, ");
        sentencia.append("\n SUBSTR(cc.cuenta_contable, 1, 3)||'N'   AS n1, ");
        sentencia.append("\n 'XDescripcion SubTotales' AS descripcion, ");
        sentencia.append("\n CASE WHEN SUBSTR(cc.cuenta_contable, 1, 3)||'N'='322N' THEN  ");
        sentencia.append("\n  (CASE MAX(cl.naturaleza) ");
        sentencia.append("\n   WHEN 'D' ");
        sentencia.append("\n      THEN    SUM(").append(mesAntCargo2).append(") - SUM(").append(mesAntAbono2).append(")");
        sentencia.append("\n      ELSE (  SUM(").append(mesAntAbono2).append(") - SUM(").append(mesAntCargo2).append("))");
        sentencia.append("\n END)*-1 ELSE ");
        sentencia.append("\n  (CASE MAX(cl.naturaleza) ");
        sentencia.append("\n   WHEN 'D' ");
        sentencia.append("\n      THEN    SUM(").append(mesAntCargo2).append(") - SUM(").append(mesAntAbono2).append(")");
        sentencia.append("\n      ELSE (  SUM(").append(mesAntAbono2).append(") - SUM(").append(mesAntCargo2).append("))");
        sentencia.append("\n END) END AS saldo_anterior, ");
        sentencia.append("\n CASE WHEN SUBSTR(cc.cuenta_contable, 1, 3)||'N'='322N' THEN  ");
        sentencia.append("\n (CASE MAX(cl.naturaleza) ");
        sentencia.append("\n   WHEN 'D' ");
        sentencia.append("\n      THEN (( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") ) - ( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") )) ");
        sentencia.append("\n      ELSE (( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") ) - ( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") )) ");
        sentencia.append("\n END)*-1 ELSE");
        sentencia.append("\n (CASE MAX(cl.naturaleza) ");
        sentencia.append("\n   WHEN 'D' ");
        sentencia.append("\n      THEN (( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") ) - ( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") )) ");
        sentencia.append("\n      ELSE (( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") ) - ( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") )) ");
        sentencia.append("\n END) END AS saldo_actual ");
        sentencia.append("\n  FROM 	rf_tr_cuentas_contables cc, ");
        sentencia.append("\n 	rf_tc_clasificador_cuentas cl ");
        sentencia.append("\n WHERE cc.cuenta_mayor_id             = cl.cuenta_mayor_id ");
        sentencia.append("\n AND cc.nivel                         = ").append(getNivel());
        sentencia.append("\n AND cc.id_catalogo_cuenta            = 1  ");
        sentencia.append("\n  AND TO_CHAR(cc.fecha_vig_ini,'yyyy')= ").append(ejercicio);
        sentencia.append("\n AND substr(cc.cuenta_contable,1,3) >= 322 and substr(cc.cuenta_contable,1,3) <= 329 and substr(cc.cuenta_contable,1,3) != 325    ");

        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(ejercicio).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('unidad'  ,cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('unidad'  ,cc.cuenta_mayor_id,").append(ejercicio).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('ambito'  ,cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('ambito'  ,cc.cuenta_mayor_id,").append(ejercicio).append(")) = '").append(getAmbito()).append("' ");
        sentencia.append("\n GROUP BY 'S1', SUBSTR(cc.cuenta_contable, 1, 3)||'N'");
        sentencia.append("\n   UNION ALL ");
        sentencia.append("\n   SELECT 'S1' AS seccion, ");
        sentencia.append("\n     SUBSTR(cuenta_contable, 1, 1) ");
        sentencia.append("\n     ||'999'                AS n1, ");
        sentencia.append("\n     'XDescripcion Totales' AS descripcion, ");
        sentencia.append("\n     SUM(saldo_anterior) AS saldo_anterior, ");
        sentencia.append("\n     SUM(saldo_actual) AS saldo_actual ");
        sentencia.append("\n  FROM( ");
        sentencia.append("\n SELECT  '321' AS cuenta_contable, "); 
        sentencia.append("\n  SUM(saldo_anterior)                    AS saldo_anterior, ");
        sentencia.append("\n  SUM(saldo_actual)                      AS saldo_actual");
        sentencia.append("\n  FROM ");
        sentencia.append("\n  (");
        sentencia.append("\n     SELECT CASE cl.naturaleza");
        sentencia.append("\n         WHEN 'C' ");
        sentencia.append("\n		     THEN (( ").append(mesAntCargo2).append(" ) - ( ").append(mesAntAbono2).append(" ))  ");
        sentencia.append("\n		     ELSE (( ").append(mesAntAbono2).append(" ) - ( ").append(mesAntCargo2).append(" ))  ");
        sentencia.append("\n         END");
        sentencia.append("\n          AS saldo_anterior,");
        sentencia.append("\n       CASE cl.naturaleza");
        sentencia.append("\n         WHEN 'C'");
        sentencia.append("\n			THEN (( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ) - ( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ))  ");
        sentencia.append("\n			ELSE (( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ) - ( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ))  ");
        sentencia.append("\n		END AS saldo_actual ");
        sentencia.append("\n	FROM rf_tr_cuentas_contables cc, ");
        sentencia.append("\n		 rf_tc_clasificador_cuentas cl ");
        sentencia.append("\n	WHERE cc.cuenta_mayor_id = cl.cuenta_mayor_id ");
        sentencia.append("\n		AND cc.id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta()).append(" ");
        sentencia.append("\n		AND extract(year from cc.fecha_vig_ini) = ").append(controlReg.getEjercicio()).append(" ");
        sentencia.append("\n	   AND (   cc.cuenta_contable LIKE '311_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '312_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '313_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '321_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '322_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '323_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '324_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '325_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '330_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '431_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '439_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '511_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '512_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '513_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '518_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '524_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '528_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '529_%'");
        sentencia.append("\n            OR cc.cuenta_contable LIKE '551_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '599_%')");
        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getAmbito()).append("' ");
        sentencia.append("\n     AND cc.nivel = ").append(getNivel());

        sentencia.append("\n   )");
        sentencia.append("\n ) ");
        sentencia.append("\n   GROUP BY 'S1', ");
        sentencia.append("\n     SUBSTR(cuenta_contable, 1, 1) ");
        sentencia.append("\n     ||'999' ");
        
        sentencia.append("\n   ) ");
        //sentencia.append("\n WHERE ");
        //sentencia.append("\n(saldo_actual!= 0 ");
        //sentencia.append("\n OR saldo_anterior  != 0) ");
        sentencia.append("\n ORDER BY seccion, ");
        sentencia.append("\n   reporte, ");
        sentencia.append("\n   n1, ");
        sentencia.append("\n   descripcion  ");
        mes_ant2 = null;
        System.out.println("ESTADO DE SITUACION FINANCIERA" + sentencia.toString());
        return sentencia;
    }

    public StringBuffer situacionFinancieraDetallado(String fecha_consolidacion, String mes, String mesCompara, String ejercicioCompara) {
        //Variables que se refieren al mes del Filtro
        String mesAntCargo1;
        String mesAntAbono1;
        String mesActCargo1;
        String mesActAbono1;
        //Variables que se refieren al mes a Comparar
        String mesAntCargo2;
        String mesAntAbono2;
        int ejercicio = getControlReg().getEjercicio();
        Fecha fechaPeriodo = new Fecha(fecha_consolidacion, "/");
        String mes_act1 = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodo.getMes() - 1);
        fechaPeriodo.addMeses(-1);
        String mes_ant1 = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodo.getMes() - 1);
        int diasMes = sia.libs.formato.Fecha.getDiasEnElMes(Integer.valueOf(ejercicioCompara), Integer.valueOf(mesCompara) - 1);
        Fecha fechaPeriodoAnt = new Fecha(ejercicioCompara + mesCompara + String.valueOf(diasMes), "/");
        String mes_act2 = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodoAnt.getMes() - 1);
        fechaPeriodoAnt.addMeses(-1);
        String mes_ant2 = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodoAnt.getMes() - 1);
        StringBuffer sentencia = new StringBuffer();
        setNivel(!getPrograma().equals("'0000'") && getUnidad().equals("0000") && getAmbito().equals("0000") ? "3" : getNivel());

        if (mes_ant1.equals("DIC")) {
            mesAntCargo2 = construirMes(mes_act1, "cargo_ini");
            mesAntAbono2 = construirMes(mes_act1, "abono_ini");
            mesAntCargo1 = construirMes(mes_act1, "cargo_ini");
            mesAntAbono1 = construirMes(mes_act1, "abono_ini");
        } else {
            if (mes_act2.equals("DIC")) {
                mesAntCargo2 = "ene_cargo_ini";
                mesAntAbono2 = "ene_abono_ini";
            } else {
                mesAntCargo2 = construirMes(mes_act2, "cargo_acum");
                mesAntAbono2 = construirMes(mes_act2, "abono_acum");
            }
            mesAntCargo1 = construirMes(mes_ant1, "cargo_acum");
            mesAntAbono1 = construirMes(mes_ant1, "abono_acum");
        }
        mesActCargo1 = construirMes(mes_act1, "cargo");
        mesActAbono1 = construirMes(mes_act1, "abono");

        sentencia.append("\n SELECT 'LA' AS reporte, ");
        sentencia.append("\n seccion, ");
        sentencia.append("\n n1, ");
        sentencia.append("\n DECODE(n1, '1199', 'Total de Activo Circulante', '1299', 'Total de Activo No Circulante', Initcap(descripcion)) AS descripcion, ");
        sentencia.append("\n saldo_anterior, ");
        sentencia.append("\n saldo_actual, ");
        sentencia.append("\n (saldo_actual- saldo_anterior) AS variacion, ");
        sentencia.append("\n ( CASE ");
        sentencia.append("\n     WHEN (saldo_anterior- saldo_actual)!= 0 ");
        sentencia.append("\n     THEN ");
        sentencia.append("\n       CASE ");
        sentencia.append("\n         WHEN saldo_anterior != 0 ");
        sentencia.append("\n         THEN ROUND( ( ((saldo_anterior- saldo_actual)/ saldo_anterior)* 100), 2) ");
        sentencia.append("\n         ELSE ");
        sentencia.append("\n           CASE ");
        sentencia.append("\n             WHEN (saldo_anterior- saldo_actual)>0 ");
        sentencia.append("\n             THEN 100 ");
        sentencia.append("\n             ELSE -100 ");
        sentencia.append("\n           END ");
        sentencia.append("\n       END ");
        sentencia.append("\n     ELSE 0 ");
        sentencia.append("\n   END )*-1 AS porcentaje ");
        sentencia.append("\n FROM ");
        sentencia.append("\n   (SELECT  'S1' AS seccion, ");
        sentencia.append("\n     SUBSTR(cc.cuenta_contable, 1, 4) AS n1, ");
        sentencia.append("\n     SUBSTR(cc.cuenta_contable, 1, 4)||' '||tc.descripcion   AS descripcion, ");
        sentencia.append("\n     CASE MAX(cl.naturaleza) ");
        sentencia.append("\n       WHEN 'D' ");
        sentencia.append("\n        THEN    SUM(").append(mesAntCargo2).append(") - SUM(").append(mesAntAbono2).append(")");
        sentencia.append("\n        ELSE (  SUM(").append(mesAntAbono2).append(") - SUM(").append(mesAntCargo2).append("))");
        sentencia.append("\n     END saldo_anterior, ");
        sentencia.append("\n     CASE MAX(cl.naturaleza) ");
        sentencia.append("\n       WHEN 'D' ");
        sentencia.append("\n        THEN (( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") ) - ( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") )) ");
        sentencia.append("\n        ELSE (( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") ) - ( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") )) ");
        sentencia.append("\n     END AS saldo_actual ");
        sentencia.append("\n   FROM 	rf_tr_cuentas_contables    cc, ");
        sentencia.append("\n 		rf_tc_clasificador_cuentas cl, ");
        sentencia.append("\n 		rf_tc_clasificador_cuentas tc ");
        sentencia.append("\n   WHERE cc.cuenta_mayor_id            = cl.cuenta_mayor_id ");
        sentencia.append("\n  AND cc.nivel                         = ").append(getNivel());
        sentencia.append("\n   AND tc.cuenta_mayor                 = SUBSTR(cc.cuenta_contable, 1, 4) ");
        sentencia.append("\n  AND cc.id_catalogo_cuenta            = 1 ");
        sentencia.append("\n  AND TO_CHAR(cc.fecha_vig_ini,'yyyy') = ").append(ejercicio);
        sentencia.append("\n  AND cc.cuenta_contable LIKE '1_%'   ");

        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(ejercicio).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('unidad'  ,cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('unidad'  ,cc.cuenta_mayor_id,").append(ejercicio).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('ambito'  ,cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('ambito'  ,cc.cuenta_mayor_id,").append(ejercicio).append(")) = '").append(getAmbito()).append("' ");
        sentencia.append("\n  GROUP BY 'S1', SUBSTR(cc.cuenta_contable, 1, 4), tc.descripcion ");
        sentencia.append("\n  UNION ALL ");
        sentencia.append("\n  SELECT 'S1' AS seccion, ");
        sentencia.append("\n    SUBSTR(cc.cuenta_contable, 1, 3)||'N'   AS n1,  ");
        sentencia.append("\n    'XDescripcion SubTotales' AS descripcion, ");
        sentencia.append("\n    CASE MAX(cl.naturaleza) ");
        sentencia.append("\n      WHEN 'D' ");
        sentencia.append("\n        THEN    SUM(").append(mesAntCargo2).append(") - SUM(").append(mesAntAbono2).append(")");
        sentencia.append("\n        ELSE (  SUM(").append(mesAntAbono2).append(") - SUM(").append(mesAntCargo2).append("))");
        sentencia.append("\n    END saldo_anterior, ");
        sentencia.append("\n    CASE MAX(cl.naturaleza) ");
        sentencia.append("\n      WHEN 'D' ");
        sentencia.append("\n        THEN (( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") ) - ( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") )) ");
        sentencia.append("\n        ELSE (( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") ) - ( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") )) ");
        sentencia.append("\n    END AS saldo_actual ");
        sentencia.append("\n  FROM 	rf_tr_cuentas_contables cc, ");
        sentencia.append("\n		rf_tc_clasificador_cuentas cl ");
        sentencia.append("\n  WHERE cc.cuenta_mayor_id            = cl.cuenta_mayor_id ");
        sentencia.append("\n  AND cc.nivel                        = ").append(getNivel());
        sentencia.append("\n  AND cc.id_catalogo_cuenta           = 1 ");
        sentencia.append("\n  AND TO_CHAR(cc.fecha_vig_ini,'yyyy')= ").append(ejercicio);
        sentencia.append("\n  AND cc.cuenta_contable LIKE '1_%'   ");

        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getAmbito()).append("' ");
        sentencia.append("\n   GROUP BY 'S1', SUBSTR(cc.cuenta_contable, 1, 3)||'N' ");
        sentencia.append("\n  UNION ALL ");
        sentencia.append("\n  SELECT 'S1' AS seccion, ");
        sentencia.append("\n  SUBSTR(cc.cuenta_contable, 1, 2)||'99'  AS n1, ");
        sentencia.append("\n    'XDescripcion Totales' AS descripcion, ");
        sentencia.append("\n    CASE MAX(cl.naturaleza) ");
        sentencia.append("\n      WHEN 'D' ");
        sentencia.append("\n        THEN    SUM(").append(mesAntCargo2).append(") - SUM(").append(mesAntAbono2).append(")");
        sentencia.append("\n        ELSE (  SUM(").append(mesAntAbono2).append(") - SUM(").append(mesAntCargo2).append("))");
        sentencia.append("\n    END saldo_anterior, ");
        sentencia.append("\n    CASE MAX(cl.naturaleza) ");
        sentencia.append("\n      WHEN 'D' ");
        sentencia.append("\n        THEN (( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") ) - ( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") )) ");
        sentencia.append("\n        ELSE (( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") ) - ( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") )) ");
        sentencia.append("\n    END AS saldo_actual ");
        sentencia.append("\n  FROM 	rf_tr_cuentas_contables cc, ");
        sentencia.append("\n		rf_tc_clasificador_cuentas cl ");
        sentencia.append("\n  WHERE cc.cuenta_mayor_id            = cl.cuenta_mayor_id ");
        sentencia.append("\n  AND cc.nivel                        = ").append(getNivel());
        sentencia.append("\n  AND cc.id_catalogo_cuenta           = 1 ");
        sentencia.append("\n  AND TO_CHAR(cc.fecha_vig_ini,'yyyy')= ").append(ejercicio);
        sentencia.append("\n  AND         cc.cuenta_contable LIKE '1_%'   ");

        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(ejercicio).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('unidad'  ,cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('unidad'  ,cc.cuenta_mayor_id,").append(ejercicio).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('ambito'  ,cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('ambito'  ,cc.cuenta_mayor_id,").append(ejercicio).append(")) = '").append(getAmbito()).append("' ");
        sentencia.append("\n  GROUP BY 'S1', SUBSTR(cc.cuenta_contable, 1, 2)||'99' ");
        sentencia.append("\n  ) ");
        sentencia.append("\n WHERE (saldo_actual!= 0 ");
        sentencia.append("\n OR saldo_anterior  != 0) ");
        /*TERMINA EL QUERY DEL ACTIVO*/
        sentencia.append("\n UNION ALL ");
        sentencia.append("\n SELECT 'LB' AS reporte, ");
        sentencia.append("\n seccion, ");
        sentencia.append("\n   n1, ");
        sentencia.append("\n  DECODE(n1, '21NN', 'Total de Pasivo Circulante', '22NN', 'Total Pasivo No Circulante', '70NN', 'TOTAL CUENTAS DE ORDEN', Initcap(descripcion)) AS descripcion, ");
        sentencia.append("\n  saldo_anterior, ");
        sentencia.append("\n  saldo_actual, ");
        sentencia.append("\n  (saldo_actual- saldo_anterior) AS variacion, ");
        sentencia.append("\n  ( ");
        sentencia.append("\n  CASE ");
        sentencia.append("\n    WHEN (saldo_anterior- saldo_actual)!= 0 ");
        sentencia.append("\n    THEN ");
        sentencia.append("\n      CASE ");
        sentencia.append("\n        WHEN saldo_anterior != 0 ");
        sentencia.append("\n        THEN ROUND( ( ((saldo_anterior- saldo_actual)/ saldo_anterior)* 100), 2) ");
        sentencia.append("\n        ELSE ");
        sentencia.append("\n          CASE ");
        sentencia.append("\n           WHEN (saldo_anterior- saldo_actual)>0 ");
        sentencia.append("\n            THEN 100 ");
        sentencia.append("\n            ELSE -100 ");
        sentencia.append("\n          END ");
        sentencia.append("\n      END ");
        sentencia.append("\n    ELSE 0 ");
        sentencia.append("\n  END )*-1 AS porcentaje ");
        sentencia.append("\nFROM ");
        sentencia.append("\n  (SELECT  'S1' AS seccion, ");
        sentencia.append("\n    SUBSTR(cc.cuenta_contable, 1, 4)  AS n1,");
        sentencia.append("\n    SUBSTR(cc.cuenta_contable, 1, 4)||' '||tc.descripcion AS descripcion, ");
        sentencia.append("\n    CASE MAX(cl.naturaleza) ");
        sentencia.append("\n      WHEN 'C' ");
        sentencia.append("\n        THEN    SUM(").append(mesAntCargo2).append(") - SUM(").append(mesAntAbono2).append(")");
        sentencia.append("\n        ELSE (  SUM(").append(mesAntAbono2).append(") - SUM(").append(mesAntCargo2).append("))");
        sentencia.append("\n    END saldo_anterior, ");
        sentencia.append("\n    CASE MAX(cl.naturaleza) ");
        sentencia.append("\n      WHEN 'C' ");
        sentencia.append("\n        THEN (( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") ) - ( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") )) ");
        sentencia.append("\n        ELSE (( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") ) - ( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") )) ");
        sentencia.append("\n    END AS saldo_actual ");
        sentencia.append("\n  FROM  rf_tr_cuentas_contables cc, ");
        sentencia.append("\n		rf_tc_clasificador_cuentas cl, ");
        sentencia.append("\n		rf_tc_clasificador_cuentas tc ");
        sentencia.append("\n  WHERE cc.cuenta_mayor_id            = cl.cuenta_mayor_id ");
        sentencia.append("\n  AND cc.nivel                        = ").append(getNivel());
        sentencia.append("\n  AND tc.cuenta_mayor                 = SUBSTR(cc.cuenta_contable, 1, 4) ");
        sentencia.append("\n  AND cc.id_catalogo_cuenta           = 1 ");
        sentencia.append("\n  AND TO_CHAR(cc.fecha_vig_ini,'yyyy')= ").append(ejercicio);
        sentencia.append("\n  AND     cc.cuenta_contable LIKE '2_%'");

        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(ejercicio).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('unidad'  ,cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('unidad'  ,cc.cuenta_mayor_id,").append(ejercicio).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('ambito'  ,cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('ambito'  ,cc.cuenta_mayor_id,").append(ejercicio).append(")) = '").append(getAmbito()).append("' ");
        sentencia.append("\n   GROUP BY  'S1', SUBSTR(cc.cuenta_contable, 1, 4), tc.descripcion  ");
        sentencia.append("\n  UNION ALL ");
        sentencia.append("\n  SELECT 'S1' AS seccion, ");
        sentencia.append("\n    SUBSTR(cc.cuenta_contable, 1, 3)||'N'  AS n1, ");
        sentencia.append("\n    'XDescripcion SubTotales' AS descripcion, ");
        sentencia.append("\n    CASE MAX(cl.naturaleza) ");
        sentencia.append("\n      WHEN 'C' ");
        sentencia.append("\n        THEN    SUM(").append(mesAntCargo2).append(") - SUM(").append(mesAntAbono2).append(")");
        sentencia.append("\n        ELSE (  SUM(").append(mesAntAbono2).append(") - SUM(").append(mesAntCargo2).append("))");
        sentencia.append("\n    END saldo_anterior, ");
        sentencia.append("\n    CASE MAX(cl.naturaleza) ");
        sentencia.append("\n      WHEN 'C' ");
        sentencia.append("\n        THEN (( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") ) - ( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") )) ");
        sentencia.append("\n        ELSE (( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") ) - ( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") )) ");
        sentencia.append("\n    END AS saldo_actual ");
        sentencia.append("\n  FROM 	rf_tr_cuentas_contables    cc, ");
        sentencia.append("\n		rf_tc_clasificador_cuentas cl ");
        sentencia.append("\n  WHERE cc.cuenta_mayor_id            = cl.cuenta_mayor_id ");
        sentencia.append("\n  AND cc.nivel                        = ").append(getNivel());
        sentencia.append("\n  AND cc.id_catalogo_cuenta           = 1 ");
        sentencia.append("\n  AND TO_CHAR(cc.fecha_vig_ini,'yyyy')= ").append(ejercicio);
        sentencia.append("\n   AND     cc.cuenta_contable LIKE '2_%' ");

        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(ejercicio).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('unidad'  ,cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('unidad'  ,cc.cuenta_mayor_id,").append(ejercicio).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('ambito'  ,cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('ambito'  ,cc.cuenta_mayor_id,").append(ejercicio).append(")) = '").append(getAmbito()).append("' ");
        sentencia.append("\n  GROUP BY 'S1', SUBSTR(cc.cuenta_contable, 1, 3)||'N' ");

        sentencia.append("\n  UNION ALL ");

        sentencia.append("\n  SELECT 'S1' AS seccion, ");
        sentencia.append("\n     SUBSTR(cc.cuenta_contable, 1, 2)||'99'  AS n1,  ");
        sentencia.append("\n    'XDescripcion Totales' AS descripcion, ");
        sentencia.append("\n   CASE MAX(cl.naturaleza) ");
        sentencia.append("\n      WHEN 'C' ");
        sentencia.append("\n        THEN    SUM(").append(mesAntCargo2).append(") - SUM(").append(mesAntAbono2).append(")");
        sentencia.append("\n        ELSE (  SUM(").append(mesAntAbono2).append(") - SUM(").append(mesAntCargo2).append("))");
        sentencia.append("\n    END saldo_anterior, ");
        sentencia.append("\n    CASE MAX(cl.naturaleza) ");
        sentencia.append("\n      WHEN 'C' ");
        sentencia.append("\n        THEN (( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") ) - ( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") )) ");
        sentencia.append("\n        ELSE (( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") ) - ( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") )) ");
        sentencia.append("\n    END AS saldo_actual ");
        sentencia.append("\n  FROM  rf_tr_cuentas_contables cc, ");
        sentencia.append("\n		rf_tc_clasificador_cuentas cl ");
        sentencia.append("\n  WHERE cc.cuenta_mayor_id            = cl.cuenta_mayor_id ");
        sentencia.append("\n  AND cc.nivel                        = ").append(getNivel());
        sentencia.append("\n  AND cc.id_catalogo_cuenta           = 1 ");
        sentencia.append("\n  AND TO_CHAR(cc.fecha_vig_ini,'yyyy')= ").append(ejercicio);
        sentencia.append("\n  AND     cc.cuenta_contable LIKE '2_%' ");

        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(ejercicio).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('unidad'  ,cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('unidad'  ,cc.cuenta_mayor_id,").append(ejercicio).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('ambito'  ,cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('ambito'  ,cc.cuenta_mayor_id,").append(ejercicio).append(")) = '").append(getAmbito()).append("' ");
        sentencia.append("\n  GROUP BY  'S1', SUBSTR(cc.cuenta_contable, 1, 2)||'99' ");
        sentencia.append("\n  ) ");
        sentencia.append("\n WHERE (saldo_actual!= 0 ");
        sentencia.append("\n OR saldo_anterior  != 0) ");
        /*TERMINA PASIVO*/
        /*EMPIEZA HACIENDA*/
        sentencia.append("\n UNION ALL ");
        sentencia.append("\n SELECT 'LB' AS reporte,  ");
        sentencia.append("\n  seccion, ");
        sentencia.append("\n   n1, ");
        sentencia.append("\n   DECODE(n1, '3999', 'Total Hacienda Pública / Patrimonio', descripcion) AS descripcion, ");
        sentencia.append("\n   saldo_anterior, ");
        sentencia.append("\n   saldo_actual, ");
        sentencia.append("\n   (saldo_actual- saldo_anterior) AS variacion, ");
        sentencia.append("\n   ( ");
        sentencia.append("\n   CASE ");
        sentencia.append("\n     WHEN (saldo_anterior- saldo_actual)!= 0 ");
        sentencia.append("\n     THEN ");
        sentencia.append("\n       CASE ");
        sentencia.append("\n         WHEN saldo_anterior != 0 ");
        sentencia.append("\n         THEN ROUND( ( ((saldo_anterior- saldo_actual)/ saldo_anterior)* 100), 2) ");
        sentencia.append("\n         ELSE ");
        sentencia.append("\n           CASE ");
        sentencia.append("\n             WHEN (saldo_anterior- saldo_actual)>0 ");
        sentencia.append("\n             THEN 100 ");
        sentencia.append("\n             ELSE -100 ");
        sentencia.append("\n           END ");
        sentencia.append("\n       END ");
        sentencia.append("\n     ELSE 0 ");
        sentencia.append("\n   END )*-1 AS porcentaje ");
        sentencia.append("\n FROM ");
        sentencia.append("\n   (SELECT 'S1'                       AS seccion, ");
        sentencia.append("\n     SUBSTR(cc.cuenta_contable, 1, 4) AS n1, ");
        sentencia.append("\n     SUBSTR(cc.cuenta_contable, 1, 4)||' '||tc.descripcion   AS descripcion, ");
        sentencia.append("\n     CASE MAX(cl.naturaleza) ");
        sentencia.append("\n       WHEN 'D' ");
        sentencia.append("\n        THEN    SUM(").append(mesAntCargo2).append(") - SUM(").append(mesAntAbono2).append(")");
        sentencia.append("\n        ELSE (  SUM(").append(mesAntAbono2).append(") - SUM(").append(mesAntCargo2).append("))");
        sentencia.append("\n     END saldo_anterior, ");
        sentencia.append("\n     CASE MAX(cl.naturaleza) ");
        sentencia.append("\n       WHEN 'D' ");
        sentencia.append("\n        THEN (( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") ) - ( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") )) ");
        sentencia.append("\n        ELSE (( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") ) - ( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") )) ");
        sentencia.append("\n     END AS saldo_actual ");
        sentencia.append("\n   FROM 	rf_tr_cuentas_contables    cc,  ");
        sentencia.append("\n 		rf_tc_clasificador_cuentas cl,  ");
        sentencia.append("\n 		rf_tc_clasificador_cuentas tc   ");
        sentencia.append("\n   WHERE cc.cuenta_mayor_id            = cl.cuenta_mayor_id ");
        sentencia.append("\n   AND cc.nivel                        = ").append(getNivel());
        sentencia.append("\n   AND tc.cuenta_mayor                 = SUBSTR(cc.cuenta_contable, 1, 4) ");
        sentencia.append("\n   AND cc.id_catalogo_cuenta           = 1 ");
        sentencia.append("\n  AND TO_CHAR(cc.fecha_vig_ini,'yyyy')= ").append(ejercicio);
        sentencia.append("\n   AND substr(cc.cuenta_contable,1,3) >= 322 and substr(cc.cuenta_contable,1,3) <= 329 and substr(cc.cuenta_contable,1,3) != 325 ");

        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(ejercicio).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('unidad'  ,cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('unidad'  ,cc.cuenta_mayor_id,").append(ejercicio).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('ambito'  ,cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('ambito'  ,cc.cuenta_mayor_id,").append(ejercicio).append(")) = '").append(getAmbito()).append("' ");
        sentencia.append("\n   GROUP BY 'S1', ");
        sentencia.append("\n     SUBSTR(cc.cuenta_contable, 1, 4), ");
        sentencia.append("\n     tc.descripcion, ");
        sentencia.append("\n     cl.naturaleza ");
        sentencia.append("\n   UNION ALL ");
        sentencia.append("\n   SELECT 'S1'                       AS seccion, ");
        sentencia.append("\n     '3NNN'                          AS n1, ");
        sentencia.append("\n     'HACIENDA PÚBLICA / PATRIMONIO' AS descripcion, ");
        sentencia.append("\n     0 saldo_anterior, ");
        sentencia.append("\n     0   AS saldo_actual ");
        sentencia.append("\n   FROM dual ");
        sentencia.append("\n   UNION ALL ");

        sentencia.append("\n  SELECT ");
        sentencia.append("\n  'S1' as seccion,");
        sentencia.append("\n  '321N' AS n1, ");
        sentencia.append("\n  'XDescripcion SubTotales' AS descripcion, ");
        sentencia.append("\n  SUM(saldo_anterior)                    AS saldo_anterior, ");
        sentencia.append("\n  SUM(saldo_actual)                      AS saldo_actual");
        sentencia.append("\n  FROM ");
        sentencia.append("\n  (");
        sentencia.append("\n     SELECT CASE cl.naturaleza");
        sentencia.append("\n         WHEN 'D' ");
        sentencia.append("\n		     THEN (( ").append(mesAntCargo2).append(" ) - ( ").append(mesAntAbono2).append(" ))  ");
        sentencia.append("\n		     ELSE (( ").append(mesAntAbono2).append(" ) - ( ").append(mesAntCargo2).append(" ))  ");
        sentencia.append("\n         END");
        sentencia.append("\n          AS saldo_anterior,");
        sentencia.append("\n       CASE cl.naturaleza");
        sentencia.append("\n         WHEN 'D'");
        sentencia.append("\n			THEN (( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ) - ( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ))  ");
        sentencia.append("\n			ELSE (( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ) - ( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ))  ");
        sentencia.append("\n		END AS saldo_actual ");
        sentencia.append("\n	FROM rf_tr_cuentas_contables cc, ");
        sentencia.append("\n		 rf_tc_clasificador_cuentas cl ");
        sentencia.append("\n	WHERE cc.cuenta_mayor_id = cl.cuenta_mayor_id ");
        sentencia.append("\n		AND cc.id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta()).append(" ");
        sentencia.append("\n		AND extract(year from cc.fecha_vig_ini) = ").append(controlReg.getEjercicio()).append(" ");
        sentencia.append("\n	   AND (   cc.cuenta_contable LIKE '4_%'");
        sentencia.append("\n	      OR cc.cuenta_contable LIKE '41_%'");
        sentencia.append("\n	      OR cc.cuenta_contable LIKE '42_%'");
        sentencia.append("\n	      OR cc.cuenta_contable LIKE '43_%')");
        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getAmbito()).append("' ");
        sentencia.append("\n     AND cc.nivel = ").append(getNivel());
         sentencia.append("\n	--ANEXO2");
         sentencia.append("\n	UNION ALL");
         sentencia.append("\n	SELECT CASE cl.naturaleza");
        sentencia.append("\n	     WHEN 'D' ");
         sentencia.append("\n		     THEN (( ").append(mesAntCargo2).append(" ) - ( ").append(mesAntAbono2).append(" ))  ");
        sentencia.append("\n		     ELSE (( ").append(mesAntAbono2).append(" ) - ( ").append(mesAntCargo2).append(" ))  ");
        sentencia.append("\n	    END");
        sentencia.append("\n	       AS saldo_anterior,");
        sentencia.append("\n	    CASE cl.naturaleza");
        sentencia.append("\n	    WHEN 'D'");
        sentencia.append("\n			THEN (( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ) - ( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ))  ");
        sentencia.append("\n			ELSE (( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ) - ( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ))  ");
        sentencia.append("\n		END AS saldo_actual ");
        sentencia.append("\n	FROM rf_tr_cuentas_contables cc, ");
        sentencia.append("\n		 rf_tc_clasificador_cuentas cl ");
        sentencia.append("\n	WHERE cc.cuenta_mayor_id = cl.cuenta_mayor_id ");
        sentencia.append("\n		AND cc.id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta()).append(" ");
        sentencia.append("\n		AND extract(year from cc.fecha_vig_ini) = ").append(controlReg.getEjercicio()).append(" ");
        sentencia.append("\n	  AND (   cc.cuenta_contable LIKE '311%')");
        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getAmbito()).append("' ");
        sentencia.append("\n     AND cc.nivel = ").append(getNivel());
         sentencia.append("\n	UNION ALL");
         sentencia.append("\n	SELECT CASE cl.naturaleza");
        sentencia.append("\n	     WHEN 'D' ");
        sentencia.append("\n		     THEN (( ").append(mesAntCargo2).append(" ) - ( ").append(mesAntAbono2).append(" ))  ");
        sentencia.append("\n		     ELSE (( ").append(mesAntAbono2).append(" ) - ( ").append(mesAntCargo2).append(" ))  ");
        sentencia.append("\n	   END");
        sentencia.append("\n	       AS saldo_anterior,");
        sentencia.append("\n	    CASE cl.naturaleza");
        sentencia.append("\n	      WHEN 'D'");
        sentencia.append("\n			THEN (( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ) - ( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ))  ");
        sentencia.append("\n			ELSE (( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ) - ( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ))  ");
        sentencia.append("\n		END AS saldo_actual ");
        sentencia.append("\n	FROM rf_tr_cuentas_contables cc, ");
        sentencia.append("\n		 rf_tc_clasificador_cuentas cl ");
        sentencia.append("\n	WHERE cc.cuenta_mayor_id = cl.cuenta_mayor_id ");
        sentencia.append("\n		AND cc.id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta()).append(" ");
        sentencia.append("\n		AND extract(year from cc.fecha_vig_ini) = ").append(controlReg.getEjercicio()).append(" ");
        sentencia.append("\n	    AND (   cc.cuenta_contable LIKE '325%')");
        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getAmbito()).append("' ");
        sentencia.append("\n     AND cc.nivel = ").append(getNivel());
         sentencia.append("\n	--FIN ANEXO2  ");
        sentencia.append("\n	 UNION ALL ");
        sentencia.append("\n	SELECT CASE cl.naturaleza");
        sentencia.append("\n	    WHEN 'D' ");
        sentencia.append("\n		     THEN (( ").append(mesAntCargo2).append(" ) - ( ").append(mesAntAbono2).append(" ))  ");
        sentencia.append("\n		     ELSE (( ").append(mesAntAbono2).append(" ) - ( ").append(mesAntCargo2).append(" ))  ");
        sentencia.append("\n	    END");
        sentencia.append("\n	       AS saldo_anterior,");
        sentencia.append("\n	    CASE cl.naturaleza");
        sentencia.append("\n	       WHEN 'D'");
        sentencia.append("\n			THEN (( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ) - ( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ))  ");
        sentencia.append("\n			ELSE (( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ) - ( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ))  ");
        sentencia.append("\n		END AS saldo_actual ");
        sentencia.append("\n	FROM rf_tr_cuentas_contables cc, ");
        sentencia.append("\n		 rf_tc_clasificador_cuentas cl ");
        sentencia.append("\n	WHERE cc.cuenta_mayor_id = cl.cuenta_mayor_id ");
        sentencia.append("\n		AND cc.id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta()).append(" ");
        sentencia.append("\n		AND extract(year from cc.fecha_vig_ini) = ").append(controlReg.getEjercicio()).append(" ");
        sentencia.append("\n	   AND ( --  cc.cuenta_contable LIKE '43_%'");
        sentencia.append("\n	       -- OR ");
        sentencia.append("\n	        cc.cuenta_contable LIKE '5_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '51_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '52_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '53_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '54_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '55_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '825_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '826_%')");
        sentencia.append("\n	     AND ( cc.cuenta_contable NOT LIKE '511_%'");
        sentencia.append("\n	       AND cc.cuenta_contable NOT LIKE '512_%'");
        sentencia.append("\n	       AND cc.cuenta_contable NOT LIKE '513_%'");
        sentencia.append("\n	       AND cc.cuenta_contable NOT LIKE '8255_%'");
        sentencia.append("\n	       AND cc.cuenta_contable NOT LIKE '8256_%'");
        sentencia.append("\n	       AND cc.cuenta_contable NOT LIKE '8257_%'");
        sentencia.append("\n	       AND cc.cuenta_contable NOT LIKE '8265_%'");
        sentencia.append("\n	       AND cc.cuenta_contable NOT LIKE '8266_%'");
        sentencia.append("\n	       AND cc.cuenta_contable NOT LIKE '8267_%')");
        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getAmbito()).append("' ");
        sentencia.append("\n     AND cc.nivel = ").append(getNivel());
        sentencia.append("\n	--FIN ANEXO3");
        sentencia.append("\n     UNION ALL");
        sentencia.append("\n     SELECT ");
        sentencia.append("\n     CASE cl.naturaleza");
        sentencia.append("\n        WHEN 'C'");
        sentencia.append("\n                 THEN (( ").append(mesAntCargo2).append(" ) - ( ").append(mesAntAbono2).append("  )) ");
        sentencia.append("\n                 ELSE (( ").append(mesAntAbono2).append(" ) - ( ").append(mesAntCargo2).append("  )) ");
        sentencia.append("\n     END");
        sentencia.append("\n        AS saldo_anterior,");
        sentencia.append("\n     CASE cl.naturaleza");
        sentencia.append("\n        WHEN 'C'");
        sentencia.append("\n                 THEN (( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ) - ( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" )) ");
        sentencia.append("\n                 ELSE (( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ) - ( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" )) ");
        sentencia.append("\n     END");
        sentencia.append("\n        AS saldo_actual");
        sentencia.append("\n     FROM rf_tr_cuentas_contables cc,");
        sentencia.append("\n     rf_tc_clasificador_cuentas cl");
        sentencia.append("\n     WHERE cc.cuenta_mayor_id = cl.cuenta_mayor_id");
        sentencia.append("\n     AND cc.id_catalogo_cuenta = 1");
        sentencia.append("\n		AND extract(year from cc.fecha_vig_ini) = ").append(controlReg.getEjercicio()).append("  ");
        sentencia.append("\n	 AND ( cc.cuenta_contable LIKE '827110001000100910001%'");
        sentencia.append("\n	      or cc.cuenta_contable LIKE '827110001000100911000%')");
        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }

        sentencia.append("\n     UNION ALL");
        sentencia.append("\n     SELECT ");
        sentencia.append("\n     CASE cl.naturaleza");
        sentencia.append("\n        WHEN 'C'");
        sentencia.append("\n                 THEN (( ").append(mesAntCargo2).append(" ) - ( ").append(mesAntAbono2).append("  )) ");
        sentencia.append("\n                 ELSE (( ").append(mesAntAbono2).append(" ) - ( ").append(mesAntCargo2).append("  )) ");
        sentencia.append("\n     END");
        sentencia.append("\n        AS saldo_anterior,");
        sentencia.append("\n     CASE cl.naturaleza");
        sentencia.append("\n        WHEN 'C'");
        sentencia.append("\n                 THEN (( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ) - ( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" )) ");
        sentencia.append("\n                 ELSE (( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ) - ( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" )) ");
        sentencia.append("\n     END");
        sentencia.append("\n        AS saldo_actual");
        sentencia.append("\n     FROM rf_tr_cuentas_contables cc,");
        sentencia.append("\n     rf_tc_clasificador_cuentas cl");
        sentencia.append("\n     WHERE cc.cuenta_mayor_id = cl.cuenta_mayor_id");
        sentencia.append("\n     AND cc.id_catalogo_cuenta = 1");
        sentencia.append("\n		AND extract(year from cc.fecha_vig_ini) = ").append(controlReg.getEjercicio()).append("  ");
        sentencia.append("\n	 AND ( cc.cuenta_contable LIKE '827110001000100910002%'");
        sentencia.append("\n	      or cc.cuenta_contable LIKE '827110001000100912000%')");
        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }

        sentencia.append("\n     UNION ALL");
        sentencia.append("\n     SELECT ");
        sentencia.append("\n     CASE cl.naturaleza");
        sentencia.append("\n        WHEN 'C'");
        sentencia.append("\n                 THEN (( ").append(mesAntCargo2).append(" ) - ( ").append(mesAntAbono2).append("  )) ");
        sentencia.append("\n                 ELSE (( ").append(mesAntAbono2).append(" ) - ( ").append(mesAntCargo2).append("  )) ");
        sentencia.append("\n     END");
        sentencia.append("\n        AS saldo_anterior,");
        sentencia.append("\n     CASE cl.naturaleza");
        sentencia.append("\n        WHEN 'C'");
        sentencia.append("\n                 THEN (( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ) - ( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" )) ");
        sentencia.append("\n                 ELSE (( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ) - ( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" )) ");
        sentencia.append("\n     END");
        sentencia.append("\n        AS saldo_actual");
        sentencia.append("\n     FROM rf_tr_cuentas_contables cc,");
        sentencia.append("\n     rf_tc_clasificador_cuentas cl");
        sentencia.append("\n     WHERE cc.cuenta_mayor_id = cl.cuenta_mayor_id");
        sentencia.append("\n     AND cc.id_catalogo_cuenta = 1");
        sentencia.append("\n		AND extract(year from cc.fecha_vig_ini) = ").append(controlReg.getEjercicio()).append("  ");
        sentencia.append("\n	 AND ( cc.cuenta_contable LIKE '827110001000100910003%'");
        sentencia.append("\n	      or cc.cuenta_contable LIKE '827110001000100913000%')");
        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }
        
        sentencia.append("\n     UNION ALL");
        sentencia.append("\n     SELECT ");
        sentencia.append("\n     CASE cl.naturaleza");
        sentencia.append("\n        WHEN 'D'");
        sentencia.append("\n                 THEN (( ").append(mesAntCargo2).append(" ) - ( ").append(mesAntAbono2).append("  )) ");
        sentencia.append("\n                 ELSE (( ").append(mesAntAbono2).append(" ) - ( ").append(mesAntCargo2).append("  )) ");
        sentencia.append("\n     END");
        sentencia.append("\n        AS saldo_anterior,");
        sentencia.append("\n     CASE cl.naturaleza");
        sentencia.append("\n        WHEN 'D'");
        sentencia.append("\n                 THEN (( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ) - ( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" )) ");
        sentencia.append("\n                 ELSE (( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ) - ( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" )) ");
        sentencia.append("\n     END");
        sentencia.append("\n        AS saldo_actual");
        sentencia.append("\n     FROM rf_tr_cuentas_contables cc,");
        sentencia.append("\n     rf_tc_clasificador_cuentas cl");
        sentencia.append("\n     WHERE cc.cuenta_mayor_id = cl.cuenta_mayor_id");
        sentencia.append("\n     AND cc.id_catalogo_cuenta = 1");
        sentencia.append("\n		AND extract(year from cc.fecha_vig_ini) = ").append(controlReg.getEjercicio()).append("  ");
        sentencia.append("\n	 AND ( cc.cuenta_contable LIKE '826110001000100910004%'");
        sentencia.append("\n	      or cc.cuenta_contable LIKE '826110001000100914000%')");
        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n   )");
        sentencia.append("\n   UNION ALL ");
        sentencia.append("\n   SELECT 'S1'                       AS seccion, ");
        sentencia.append("\n     '3NNN'                          AS n1, ");
        sentencia.append("\n     'HACIENDA PÚBLICA / PATRIMONIO' AS descripcion, ");
        sentencia.append("\n     0 saldo_anterior, ");
        sentencia.append("\n     0   AS saldo_actual ");
        sentencia.append("\n   FROM dual ");

//////// 330
        sentencia.append("\n   UNION ALL ");

        sentencia.append("\n SELECT  'S1' AS seccion, ");
        sentencia.append("\n '330N'   AS n1, ");
        sentencia.append("\n 'XDescripcion SubTotales' AS descripcion, ");
        sentencia.append("\n SUM (saldo_anterior) AS saldo_anterior, ");
        sentencia.append("\n SUM (saldo_actual) AS saldo_actual ");
        sentencia.append("\n FROM ( ");
        sentencia.append("\n SELECT");
        sentencia.append("\n CASE WHEN SUBSTR(cc.cuenta_contable, 1, 3)||'N'='322N' THEN  ");
        sentencia.append("\n  (CASE MAX(cl.naturaleza) ");
        sentencia.append("\n   WHEN 'D' ");
        sentencia.append("\n      THEN SUM(").append(mesAntCargo1).append(") - SUM(").append(mesAntAbono1).append(")");
        sentencia.append("\n      ELSE (SUM(").append(mesAntAbono1).append(") - SUM(").append(mesAntCargo1).append("))");
        sentencia.append("\n END)*-1 ELSE ");
        sentencia.append("\n  (CASE MAX(cl.naturaleza) ");
        sentencia.append("\n   WHEN 'D' ");
        sentencia.append("\n      THEN SUM(").append(mesAntCargo1).append(") - SUM(").append(mesAntAbono1).append(")");
        sentencia.append("\n      ELSE (SUM(").append(mesAntAbono1).append(") - SUM(").append(mesAntCargo1).append("))");
        sentencia.append("\n END) END AS saldo_anterior, ");
        sentencia.append("\n CASE WHEN SUBSTR(cc.cuenta_contable, 1, 3)||'N'='322N' THEN  ");
        sentencia.append("\n (CASE MAX(cl.naturaleza) ");
        sentencia.append("\n   WHEN 'D' ");
        sentencia.append("\n        THEN (( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") ) - ( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") )) ");
        sentencia.append("\n        ELSE (( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") ) - ( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") )) ");
        sentencia.append("\n END)*-1 ELSE");
        sentencia.append("\n (CASE MAX(cl.naturaleza) ");
        sentencia.append("\n   WHEN 'D' ");
        sentencia.append("\n        THEN (( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") ) - ( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") )) ");
        sentencia.append("\n        ELSE (( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") ) - ( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") )) ");
        sentencia.append("\n END) END AS saldo_actual ");
        sentencia.append("\n  FROM 	rf_tr_cuentas_contables cc, ");
        sentencia.append("\n 	rf_tc_clasificador_cuentas cl ");
        sentencia.append("\n WHERE cc.cuenta_mayor_id             = cl.cuenta_mayor_id ");
        sentencia.append("\n AND cc.nivel                         = ").append(getNivel());
        sentencia.append("\n AND cc.id_catalogo_cuenta            = 1  ");
        sentencia.append("\n  AND TO_CHAR(cc.fecha_vig_ini,'yyyy')= ").append(ejercicio);
        sentencia.append("\n AND substr(cc.cuenta_contable,1,3) between 322 and 329 and substr(cc.cuenta_contable,1,3) != 325 ");

        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(ejercicio).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('unidad'  ,cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('unidad'  ,cc.cuenta_mayor_id,").append(ejercicio).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('ambito'  ,cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('ambito'  ,cc.cuenta_mayor_id,").append(ejercicio).append(")) = '").append(getAmbito()).append("' ");
        sentencia.append("\n GROUP BY 'S1', SUBSTR(cc.cuenta_contable, 1, 3)||'N'");
        sentencia.append("\n ) ");
        sentencia.append("\n WHERE ");
        sentencia.append("\n ( saldo_actual!= 0   ");
        sentencia.append("\n OR saldo_anterior  != 0)  ");

        sentencia.append("\n   UNION ALL ");
        sentencia.append("\n   SELECT reporte,seccion,n1, descripcion, ");
        sentencia.append("\n     SUM(variacion) AS variacion, ");
        sentencia.append("\n     SUM(porcentaje) AS porcentaje, ");
        sentencia.append("\n     SUM(saldo_actual) AS saldo_actual, ");
        sentencia.append("\n     SUM(saldo_anterior) AS saldo_anterior ");
        sentencia.append("\n FROM( ");
        sentencia.append("\n SELECT 'LB' AS reporte,'S1' seccion,");
        sentencia.append("\n SUBSTR(cuenta_contable, 1, 1)||'999' n1, ");
        sentencia.append("\n 'Total Hacienda Pública / Patrimonio' AS descripcion, ");
        sentencia.append("\n  (saldo_actual- saldo_anterior) AS variacion, ");
        sentencia.append("\n  (CASE ");
        sentencia.append("\n    WHEN (saldo_anterior- saldo_actual)!= 0 ");
        sentencia.append("\n    THEN ");
        sentencia.append("\n      CASE ");
        sentencia.append("\n        WHEN saldo_anterior != 0 ");
        sentencia.append("\n        THEN ROUND( ( ((saldo_anterior- saldo_actual)/ saldo_anterior)* 100), 2) ");
        sentencia.append("\n        ELSE ");
        sentencia.append("\n          CASE ");
        sentencia.append("\n           WHEN (saldo_anterior- saldo_actual)>0 ");
        sentencia.append("\n            THEN 100 ");
        sentencia.append("\n            ELSE -100 ");
        sentencia.append("\n          END ");
        sentencia.append("\n      END ");
        sentencia.append("\n    ELSE 0 ");
        sentencia.append("\n  END )*-1 AS porcentaje, ");
        sentencia.append("\n  ( CASE ");
        sentencia.append("\n      WHEN (saldo_actual - saldo_anterior) != 0 ");
        sentencia.append("\n       THEN ");
        sentencia.append("\n        CASE ");
        sentencia.append("\n          WHEN (saldo_actual - saldo_anterior) > 0 ");
        sentencia.append("\n             THEN (saldo_actual - saldo_anterior) ");
        sentencia.append("\n             ELSE 0 ");
        sentencia.append("\n        END ");
        sentencia.append("\n      ELSE 0 ");
        sentencia.append("\n    END ) AS SALDO_ACTUAL,");//(SALDO_ACTUAL)Es el campo ORIGEN
        sentencia.append("\n  ( CASE ");
        sentencia.append("\n      WHEN (saldo_actual - saldo_anterior) != 0 ");
        sentencia.append("\n       THEN ");
        sentencia.append("\n        CASE ");
        sentencia.append("\n          WHEN (saldo_actual - saldo_anterior) < 0 ");
        sentencia.append("\n             THEN (saldo_actual - saldo_anterior) ");
        sentencia.append("\n             ELSE 0 ");
        sentencia.append("\n        END ");
        sentencia.append("\n      ELSE 0 ");
        sentencia.append("\n    END ) SALDO_ANTERIOR ");//(SALDO_ANTERIOR)Es el campo APLICACION 
        sentencia.append("\n FROM ( ");


        sentencia.append("\n SELECT  SUBSTR(cc.cuenta_contable, 1, 3) AS cuenta_contable, ");
        sentencia.append("\n CASE WHEN SUBSTR(cc.cuenta_contable, 1, 3) IN ('313','322') THEN  ");
        sentencia.append("\n  (CASE MAX(cl.naturaleza) ");
        sentencia.append("\n   WHEN 'D' ");
        sentencia.append("\n      THEN SUM(").append(mesAntCargo1).append(") - SUM(").append(mesAntAbono1).append(")");
        sentencia.append("\n      ELSE (SUM(").append(mesAntAbono1).append(") - SUM(").append(mesAntCargo1).append("))");
        sentencia.append("\n END)*-1 ELSE ");
        sentencia.append("\n  (CASE MAX(cl.naturaleza) ");
        sentencia.append("\n   WHEN 'D' ");
        sentencia.append("\n      THEN SUM(").append(mesAntCargo1).append(") - SUM(").append(mesAntAbono1).append(")");
        sentencia.append("\n      ELSE (SUM(").append(mesAntAbono1).append(") - SUM(").append(mesAntCargo1).append("))");
        sentencia.append("\n END) END AS saldo_anterior, ");
        sentencia.append("\n CASE WHEN SUBSTR(cc.cuenta_contable, 1, 3) IN ('313','322') THEN  ");
        sentencia.append("\n (CASE MAX(cl.naturaleza) ");
        sentencia.append("\n   WHEN 'D' ");
        sentencia.append("\n        THEN (( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") ) - ( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") )) ");
        sentencia.append("\n        ELSE (( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") ) - ( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") )) ");
        sentencia.append("\n END)*-1 ELSE");
        sentencia.append("\n (CASE MAX(cl.naturaleza) ");
        sentencia.append("\n   WHEN 'D' ");
        sentencia.append("\n        THEN (( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") ) - ( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") )) ");
        sentencia.append("\n        ELSE (( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") ) - ( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") )) ");
        sentencia.append("\n END) END AS saldo_actual ");
        sentencia.append("\n  FROM 	rf_tr_cuentas_contables cc, ");
        sentencia.append("\n 	rf_tc_clasificador_cuentas cl ");
        sentencia.append("\n WHERE cc.cuenta_mayor_id             = cl.cuenta_mayor_id ");
        sentencia.append("\n AND cc.nivel                         = ").append(getNivel());
        sentencia.append("\n AND cc.id_catalogo_cuenta            = 1  ");
        sentencia.append("\n  AND TO_CHAR(cc.fecha_vig_ini,'yyyy')= ").append(ejercicio);
        sentencia.append("\n AND ((substr(cc.cuenta_contable,1,3) >= 331 and substr(cc.cuenta_contable,1,3) <= 332)   ");
        sentencia.append("\n OR substr(cc.cuenta_contable,1,3) =313 )   ");

        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(ejercicio).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('unidad'  ,cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('unidad'  ,cc.cuenta_mayor_id,").append(ejercicio).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('ambito'  ,cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('ambito'  ,cc.cuenta_mayor_id,").append(ejercicio).append(")) = '").append(getAmbito()).append("' ");
        sentencia.append("\n GROUP BY SUBSTR(cc.cuenta_contable, 1, 3)) ");
        
//////// fin de 330
        
        
        sentencia.append("\n   UNION ALL ");
        sentencia.append("\n SELECT  'S1' AS seccion, ");
        sentencia.append("\n SUBSTR(cc.cuenta_contable, 1, 3)||'N'   AS n1, ");
        sentencia.append("\n 'XDescripcion SubTotales' AS descripcion, ");
        sentencia.append("\n CASE WHEN SUBSTR(cc.cuenta_contable, 1, 3)||'N'='322N' THEN  ");
        sentencia.append("\n  (CASE MAX(cl.naturaleza) ");
        sentencia.append("\n   WHEN 'D' ");
        sentencia.append("\n      THEN    SUM(").append(mesAntCargo2).append(") - SUM(").append(mesAntAbono2).append(")");
        sentencia.append("\n      ELSE (  SUM(").append(mesAntAbono2).append(") - SUM(").append(mesAntCargo2).append("))");
        sentencia.append("\n END)*-1 ELSE ");
        sentencia.append("\n  (CASE MAX(cl.naturaleza) ");
        sentencia.append("\n   WHEN 'D' ");
        sentencia.append("\n      THEN    SUM(").append(mesAntCargo2).append(") - SUM(").append(mesAntAbono2).append(")");
        sentencia.append("\n      ELSE (  SUM(").append(mesAntAbono2).append(") - SUM(").append(mesAntCargo2).append("))");
        sentencia.append("\n END) END AS saldo_anterior, ");
        sentencia.append("\n CASE WHEN SUBSTR(cc.cuenta_contable, 1, 3)||'N'='322N' THEN  ");
        sentencia.append("\n (CASE MAX(cl.naturaleza) ");
        sentencia.append("\n   WHEN 'D' ");
        sentencia.append("\n      THEN (( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") ) - ( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") )) ");
        sentencia.append("\n      ELSE (( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") ) - ( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") )) ");
        sentencia.append("\n END)*-1 ELSE");
        sentencia.append("\n (CASE MAX(cl.naturaleza) ");
        sentencia.append("\n   WHEN 'D' ");
        sentencia.append("\n      THEN (( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") ) - ( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") )) ");
        sentencia.append("\n      ELSE (( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") ) - ( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") )) ");
        sentencia.append("\n END) END AS saldo_actual ");
        sentencia.append("\n  FROM 	rf_tr_cuentas_contables cc, ");
        sentencia.append("\n 	rf_tc_clasificador_cuentas cl ");
        sentencia.append("\n WHERE cc.cuenta_mayor_id             = cl.cuenta_mayor_id ");
        sentencia.append("\n AND cc.nivel                         = ").append(getNivel());
        sentencia.append("\n AND cc.id_catalogo_cuenta            = 1  ");
        sentencia.append("\n  AND TO_CHAR(cc.fecha_vig_ini,'yyyy')= ").append(ejercicio);
        sentencia.append("\n AND substr(cc.cuenta_contable,1,3) >= 322 and substr(cc.cuenta_contable,1,3) <= 329 and substr(cc.cuenta_contable,1,3) != 325   ");

        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(ejercicio).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('unidad'  ,cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('unidad'  ,cc.cuenta_mayor_id,").append(ejercicio).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('ambito'  ,cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('ambito'  ,cc.cuenta_mayor_id,").append(ejercicio).append(")) = '").append(getAmbito()).append("' ");
        sentencia.append("\n GROUP BY 'S1', SUBSTR(cc.cuenta_contable, 1, 3)||'N'");
        sentencia.append("\n   UNION ALL ");
        sentencia.append("\n   SELECT 'S1' AS seccion, ");
        sentencia.append("\n     SUBSTR(cuenta_contable, 1, 1) ");
        sentencia.append("\n     ||'999'                AS n1, ");
        sentencia.append("\n     'XDescripcion Totales' AS descripcion, ");
        sentencia.append("\n     SUM(saldo_anterior) AS saldo_anterior, ");
        sentencia.append("\n     SUM(saldo_actual) AS saldo_actual ");
        sentencia.append("\n  FROM( ");
        sentencia.append("\n  SELECT  SUBSTR(cc.cuenta_contable, 1, 3) AS cuenta_contable, ");
        sentencia.append("\n CASE WHEN SUBSTR(cc.cuenta_contable, 1, 3)='322' THEN  ");
        sentencia.append("\n    (CASE MAX(cl.naturaleza) ");
        sentencia.append("\n       WHEN 'D' ");
        sentencia.append("\n        THEN    SUM(").append(mesAntCargo2).append(") - SUM(").append(mesAntAbono2).append(")");
        sentencia.append("\n        ELSE (  SUM(").append(mesAntAbono2).append(") - SUM(").append(mesAntCargo2).append("))");
        sentencia.append("\n    END)*-1 ELSE ");
        sentencia.append("\n     (CASE MAX(cl.naturaleza) ");
        sentencia.append("\n       WHEN 'D' ");
        sentencia.append("\n        THEN    SUM(").append(mesAntCargo2).append(") - SUM(").append(mesAntAbono2).append(")");
        sentencia.append("\n        ELSE (  SUM(").append(mesAntAbono2).append(") - SUM(").append(mesAntCargo2).append("))");
        sentencia.append("\n    END) END AS saldo_anterior, ");
        sentencia.append("\n CASE WHEN SUBSTR(cc.cuenta_contable, 1, 3)='322' THEN  ");
        sentencia.append("\n     (CASE MAX(cl.naturaleza) ");
        sentencia.append("\n       WHEN 'D' ");
        sentencia.append("\n        THEN (( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") ) - ( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") )) ");
        sentencia.append("\n        ELSE (( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") ) - ( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") )) ");
        sentencia.append("\n     END) *-1 ELSE");
        sentencia.append("\n     (CASE MAX(cl.naturaleza) ");
        sentencia.append("\n       WHEN 'D' ");
        sentencia.append("\n        THEN (( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") ) - ( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") )) ");
        sentencia.append("\n        ELSE (( SUM(").append(mesAntAbono1).append(") + SUM(").append(mesActAbono1).append(") ) - ( SUM(").append(mesAntCargo1).append(") + SUM(").append(mesActCargo1).append(") )) ");
        sentencia.append("\n     END) END AS saldo_actual ");
        sentencia.append("\n   FROM 	rf_tr_cuentas_contables cc, ");
        sentencia.append("\n 		rf_tc_clasificador_cuentas cl ");
        sentencia.append("\n   WHERE cc.cuenta_mayor_id            = cl.cuenta_mayor_id ");
        sentencia.append("\n   AND cc.id_catalogo_cuenta           = 1 ");
        sentencia.append("\n  AND cc.nivel                         = ").append(getNivel());
        sentencia.append("\n  AND TO_CHAR(cc.fecha_vig_ini,'yyyy') = ").append(ejercicio);
        sentencia.append("\n   AND substr(cc.cuenta_contable,1,3) >= 322 and substr(cc.cuenta_contable,1,3) <= 329 and substr(cc.cuenta_contable,1,3) != 325 ");

        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(ejercicio).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('unidad'  ,cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('unidad'  ,cc.cuenta_mayor_id,").append(ejercicio).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('ambito'  ,cc.cuenta_mayor_id,").append(ejercicio).append("),f_longitud_nivel('ambito'  ,cc.cuenta_mayor_id,").append(ejercicio).append(")) = '").append(getAmbito()).append("' ");
        sentencia.append("\n GROUP BY SUBSTR(cc.cuenta_contable, 1, 3) ");

        sentencia.append("\n   UNION ALL ");
        sentencia.append("\n SELECT  '321' AS cuenta_contable, ");
        sentencia.append("\n  SUM(saldo_anterior)                    AS saldo_anterior, ");
        sentencia.append("\n  SUM(saldo_actual)                      AS saldo_actual");
        sentencia.append("\n  FROM ");
        sentencia.append("\n  (");
        sentencia.append("\n     SELECT CASE cl.naturaleza");
        sentencia.append("\n         WHEN 'D' ");
        sentencia.append("\n		     THEN (( ").append(mesAntCargo2).append(" ) - ( ").append(mesAntAbono2).append(" ))  ");
        sentencia.append("\n		     ELSE (( ").append(mesAntAbono2).append(" ) - ( ").append(mesAntCargo2).append(" ))  ");
        sentencia.append("\n         END");
        sentencia.append("\n          AS saldo_anterior,");
        sentencia.append("\n       CASE cl.naturaleza");
        sentencia.append("\n         WHEN 'D'");
        sentencia.append("\n			THEN (( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ) - ( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ))  ");
        sentencia.append("\n			ELSE (( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ) - ( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ))  ");
        sentencia.append("\n		END AS saldo_actual ");
        sentencia.append("\n	FROM rf_tr_cuentas_contables cc, ");
        sentencia.append("\n		 rf_tc_clasificador_cuentas cl ");
        sentencia.append("\n	WHERE cc.cuenta_mayor_id = cl.cuenta_mayor_id ");
        sentencia.append("\n		AND cc.id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta()).append(" ");
        sentencia.append("\n		AND extract(year from cc.fecha_vig_ini) = ").append(controlReg.getEjercicio()).append(" ");
        sentencia.append("\n	   AND (   cc.cuenta_contable LIKE '4_%'");
        sentencia.append("\n	      OR cc.cuenta_contable LIKE '41_%'");
        sentencia.append("\n	      OR cc.cuenta_contable LIKE '42_%'");
        sentencia.append("\n	      OR cc.cuenta_contable LIKE '43_%')");
        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getAmbito()).append("' ");
        sentencia.append("\n     AND cc.nivel = ").append(getNivel());
         sentencia.append("\n	--ANEXO2");
         sentencia.append("\n	UNION ALL");
         sentencia.append("\n	SELECT CASE cl.naturaleza");
        sentencia.append("\n	     WHEN 'D' ");
         sentencia.append("\n		     THEN (( ").append(mesAntCargo2).append(" ) - ( ").append(mesAntAbono2).append(" ))  ");
        sentencia.append("\n		     ELSE (( ").append(mesAntAbono2).append(" ) - ( ").append(mesAntCargo2).append(" ))  ");
        sentencia.append("\n	    END");
        sentencia.append("\n	       AS saldo_anterior,");
        sentencia.append("\n	    CASE cl.naturaleza");
        sentencia.append("\n	    WHEN 'D'");
        sentencia.append("\n			THEN (( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ) - ( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ))  ");
        sentencia.append("\n			ELSE (( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ) - ( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ))  ");
        sentencia.append("\n		END AS saldo_actual ");
        sentencia.append("\n	FROM rf_tr_cuentas_contables cc, ");
        sentencia.append("\n		 rf_tc_clasificador_cuentas cl ");
        sentencia.append("\n	WHERE cc.cuenta_mayor_id = cl.cuenta_mayor_id ");
        sentencia.append("\n		AND cc.id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta()).append(" ");
        sentencia.append("\n		AND extract(year from cc.fecha_vig_ini) = ").append(controlReg.getEjercicio()).append(" ");
        sentencia.append("\n	  AND (   cc.cuenta_contable LIKE '311%')");
        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getAmbito()).append("' ");
        sentencia.append("\n     AND cc.nivel = ").append(getNivel());
         sentencia.append("\n	UNION ALL");
         sentencia.append("\n	SELECT CASE cl.naturaleza");
        sentencia.append("\n	     WHEN 'D' ");
        sentencia.append("\n		     THEN (( ").append(mesAntCargo2).append(" ) - ( ").append(mesAntAbono2).append(" ))  ");
        sentencia.append("\n		     ELSE (( ").append(mesAntAbono2).append(" ) - ( ").append(mesAntCargo2).append(" ))  ");
        sentencia.append("\n	   END");
        sentencia.append("\n	       AS saldo_anterior,");
        sentencia.append("\n	    CASE cl.naturaleza");
        sentencia.append("\n	      WHEN 'D'");
        sentencia.append("\n			THEN (( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ) - ( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ))  ");
        sentencia.append("\n			ELSE (( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ) - ( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ))  ");
        sentencia.append("\n		END AS saldo_actual ");
        sentencia.append("\n	FROM rf_tr_cuentas_contables cc, ");
        sentencia.append("\n		 rf_tc_clasificador_cuentas cl ");
        sentencia.append("\n	WHERE cc.cuenta_mayor_id = cl.cuenta_mayor_id ");
        sentencia.append("\n		AND cc.id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta()).append(" ");
        sentencia.append("\n		AND extract(year from cc.fecha_vig_ini) = ").append(controlReg.getEjercicio()).append(" ");
        sentencia.append("\n	    AND (   cc.cuenta_contable LIKE '325%')");
        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getAmbito()).append("' ");
        sentencia.append("\n     AND cc.nivel = ").append(getNivel());
         sentencia.append("\n	--FIN ANEXO2  ");
        sentencia.append("\n	 UNION ALL ");
        sentencia.append("\n	SELECT CASE cl.naturaleza");
        sentencia.append("\n	    WHEN 'D' ");
        sentencia.append("\n		     THEN (( ").append(mesAntCargo2).append(" ) - ( ").append(mesAntAbono2).append(" ))  ");
        sentencia.append("\n		     ELSE (( ").append(mesAntAbono2).append(" ) - ( ").append(mesAntCargo2).append(" ))  ");
        sentencia.append("\n	    END");
        sentencia.append("\n	       AS saldo_anterior,");
        sentencia.append("\n	    CASE cl.naturaleza");
        sentencia.append("\n	       WHEN 'D'");
        sentencia.append("\n			THEN (( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ) - ( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ))  ");
        sentencia.append("\n			ELSE (( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ) - ( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ))  ");
        sentencia.append("\n		END AS saldo_actual ");
        sentencia.append("\n	FROM rf_tr_cuentas_contables cc, ");
        sentencia.append("\n		 rf_tc_clasificador_cuentas cl ");
        sentencia.append("\n	WHERE cc.cuenta_mayor_id = cl.cuenta_mayor_id ");
        sentencia.append("\n		AND cc.id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta()).append(" ");
        sentencia.append("\n		AND extract(year from cc.fecha_vig_ini) = ").append(controlReg.getEjercicio()).append(" ");
        sentencia.append("\n	   AND ( --  cc.cuenta_contable LIKE '43_%'");
        sentencia.append("\n	       -- OR ");
        sentencia.append("\n	        cc.cuenta_contable LIKE '5_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '51_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '52_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '53_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '54_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '55_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '825_%'");
        sentencia.append("\n	        OR cc.cuenta_contable LIKE '826_%')");
        sentencia.append("\n	     AND ( cc.cuenta_contable NOT LIKE '511_%'");
        sentencia.append("\n	       AND cc.cuenta_contable NOT LIKE '512_%'");
        sentencia.append("\n	       AND cc.cuenta_contable NOT LIKE '513_%'");
        sentencia.append("\n	       AND cc.cuenta_contable NOT LIKE '8255_%'");
        sentencia.append("\n	       AND cc.cuenta_contable NOT LIKE '8256_%'");
        sentencia.append("\n	       AND cc.cuenta_contable NOT LIKE '8257_%'");
        sentencia.append("\n	       AND cc.cuenta_contable NOT LIKE '8265_%'");
        sentencia.append("\n	       AND cc.cuenta_contable NOT LIKE '8266_%'");
        sentencia.append("\n	       AND cc.cuenta_contable NOT LIKE '8267_%')");
        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\n     AND substr(cc.cuenta_contable,f_posicion_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('ambito'  ,cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) = '").append(getAmbito()).append("' ");
        sentencia.append("\n     AND cc.nivel = ").append(getNivel());
        sentencia.append("\n	--FIN ANEXO3");
        sentencia.append("\n     SELECT ");
        sentencia.append("\n     CASE cl.naturaleza");
        sentencia.append("\n        WHEN 'C'");
        sentencia.append("\n                 THEN (( ").append(mesAntCargo2).append(" ) - ( ").append(mesAntAbono2).append("  )) ");
        sentencia.append("\n                 ELSE (( ").append(mesAntAbono2).append(" ) - ( ").append(mesAntCargo2).append("  )) ");
        sentencia.append("\n     END");
        sentencia.append("\n        AS saldo_anterior,");
        sentencia.append("\n     CASE cl.naturaleza");
        sentencia.append("\n        WHEN 'C'");
        sentencia.append("\n                 THEN (( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ) - ( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" )) ");
        sentencia.append("\n                 ELSE (( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ) - ( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" )) ");
        sentencia.append("\n     END");
        sentencia.append("\n        AS saldo_actual");
        sentencia.append("\n     FROM rf_tr_cuentas_contables cc,");
        sentencia.append("\n     rf_tc_clasificador_cuentas cl");
        sentencia.append("\n     WHERE cc.cuenta_mayor_id = cl.cuenta_mayor_id");
        sentencia.append("\n     AND cc.id_catalogo_cuenta = 1");
        sentencia.append("\n		AND extract(year from cc.fecha_vig_ini) = ").append(controlReg.getEjercicio()).append("  ");
        sentencia.append("\n	 AND ( cc.cuenta_contable LIKE '827110001000100910001%'");
        sentencia.append("\n	      or cc.cuenta_contable LIKE '827110001000100911000%')");
        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }

        sentencia.append("\n     UNION ALL");
        sentencia.append("\n     SELECT ");
        sentencia.append("\n     CASE cl.naturaleza");
        sentencia.append("\n        WHEN 'C'");
        sentencia.append("\n                 THEN (( ").append(mesAntCargo2).append(" ) - ( ").append(mesAntAbono2).append("  )) ");
        sentencia.append("\n                 ELSE (( ").append(mesAntAbono2).append(" ) - ( ").append(mesAntCargo2).append("  )) ");
        sentencia.append("\n     END");
        sentencia.append("\n        AS saldo_anterior,");
        sentencia.append("\n     CASE cl.naturaleza");
        sentencia.append("\n        WHEN 'C'");
        sentencia.append("\n                 THEN (( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ) - ( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" )) ");
        sentencia.append("\n                 ELSE (( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ) - ( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" )) ");
        sentencia.append("\n     END");
        sentencia.append("\n        AS saldo_actual");
        sentencia.append("\n     FROM rf_tr_cuentas_contables cc,");
        sentencia.append("\n     rf_tc_clasificador_cuentas cl");
        sentencia.append("\n     WHERE cc.cuenta_mayor_id = cl.cuenta_mayor_id");
        sentencia.append("\n     AND cc.id_catalogo_cuenta = 1");
        sentencia.append("\n		AND extract(year from cc.fecha_vig_ini) = ").append(controlReg.getEjercicio()).append("  ");
        sentencia.append("\n	 AND ( cc.cuenta_contable LIKE '827110001000100910002%'");
        sentencia.append("\n	      or cc.cuenta_contable LIKE '827110001000100912000%')");
        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }

        sentencia.append("\n     UNION ALL");
        sentencia.append("\n     SELECT ");
        sentencia.append("\n     CASE cl.naturaleza");
        sentencia.append("\n        WHEN 'C'");
        sentencia.append("\n                 THEN (( ").append(mesAntCargo2).append(" ) - ( ").append(mesAntAbono2).append("  )) ");
        sentencia.append("\n                 ELSE (( ").append(mesAntAbono2).append(" ) - ( ").append(mesAntCargo2).append("  )) ");
        sentencia.append("\n     END");
        sentencia.append("\n        AS saldo_anterior,");
        sentencia.append("\n     CASE cl.naturaleza");
        sentencia.append("\n        WHEN 'C'");
        sentencia.append("\n                 THEN (( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ) - ( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" )) ");
        sentencia.append("\n                 ELSE (( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ) - ( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" )) ");
        sentencia.append("\n     END");
        sentencia.append("\n        AS saldo_actual");
        sentencia.append("\n     FROM rf_tr_cuentas_contables cc,");
        sentencia.append("\n     rf_tc_clasificador_cuentas cl");
        sentencia.append("\n     WHERE cc.cuenta_mayor_id = cl.cuenta_mayor_id");
        sentencia.append("\n     AND cc.id_catalogo_cuenta = 1");
        sentencia.append("\n		AND extract(year from cc.fecha_vig_ini) = ").append(controlReg.getEjercicio()).append("  ");
        sentencia.append("\n	 AND ( cc.cuenta_contable LIKE '827110001000100910003%'");
        sentencia.append("\n	      or cc.cuenta_contable LIKE '827110001000100913000%')");
        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }
        
        sentencia.append("\n     UNION ALL");
        sentencia.append("\n     SELECT ");
        sentencia.append("\n     CASE cl.naturaleza");
        sentencia.append("\n        WHEN 'D'");
        sentencia.append("\n                 THEN (( ").append(mesAntCargo2).append(" ) - ( ").append(mesAntAbono2).append("  )) ");
        sentencia.append("\n                 ELSE (( ").append(mesAntAbono2).append(" ) - ( ").append(mesAntCargo2).append("  )) ");
        sentencia.append("\n     END");
        sentencia.append("\n        AS saldo_anterior,");
        sentencia.append("\n     CASE cl.naturaleza");
        sentencia.append("\n        WHEN 'D'");
        sentencia.append("\n                 THEN (( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" ) - ( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" )) ");
        sentencia.append("\n                 ELSE (( ").append(mesAntAbono1).append(" + ").append(mesActAbono1).append(" ) - ( ").append(mesAntCargo1).append(" + ").append(mesActCargo1).append(" )) ");
        sentencia.append("\n     END");
        sentencia.append("\n        AS saldo_actual");
        sentencia.append("\n     FROM rf_tr_cuentas_contables cc,");
        sentencia.append("\n     rf_tc_clasificador_cuentas cl");
        sentencia.append("\n     WHERE cc.cuenta_mayor_id = cl.cuenta_mayor_id");
        sentencia.append("\n     AND cc.id_catalogo_cuenta = 1");
        sentencia.append("\n		AND extract(year from cc.fecha_vig_ini) = ").append(controlReg.getEjercicio()).append("  ");
        sentencia.append("\n	 AND ( cc.cuenta_contable LIKE '826110001000100910004%'");
        sentencia.append("\n	      or cc.cuenta_contable LIKE '826110001000100914000%')");
        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n AND substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n   )");
        sentencia.append("\n ) ");
        sentencia.append("\n   GROUP BY 'S1', ");
        sentencia.append("\n     SUBSTR(cuenta_contable, 1, 1) ");
        sentencia.append("\n     ||'999' ");
        sentencia.append("\n   ) ");
        sentencia.append("\n WHERE ");
        sentencia.append("\n(saldo_actual!= 0 ");
        sentencia.append("\n OR saldo_anterior  != 0) ");
        sentencia.append("\n ORDER BY seccion, ");
        sentencia.append("\n   reporte, ");
        sentencia.append("\n   n1, ");
        sentencia.append("\n   descripcion  ");

        mes_ant2 = null;
        System.out.println("SITUACION FINANCIERA DETALLADO " + sentencia.toString());
        return sentencia;
    }

    public StringBuffer situacionFinancieraAhorro(String fecha_consolidacion, String mes, String mesCompara, String ejercicioCompara) {
        Fecha fechaPeriodo = new Fecha(fecha_consolidacion, "/");
        String mes_act1 = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodo.getMes() - 1);
        fechaPeriodo.addMeses(-1);
        String mes_ant1 = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodo.getMes() - 1);
        int diasMes = sia.libs.formato.Fecha.getDiasEnElMes(Integer.valueOf(ejercicioCompara), Integer.valueOf(mesCompara) - 1);
        Fecha fechaPeriodoAnt = new Fecha(ejercicioCompara + mesCompara + String.valueOf(diasMes), "/");
        String mes_act2 = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodoAnt.getMes() - 1);
        fechaPeriodoAnt.addMeses(-1);
        String mes_ant2 = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodoAnt.getMes() - 1);
        StringBuffer sentencia = new StringBuffer();
        String mesAntCargo1;
        String mesAntAbono1;
        String mesActCargo1;
        String mesActAbono1;
        String mesAntCargo2;
        String mesAntAbono2;
        String mesActCargo2;
        String mesActAbono2;
        setNivel(!getPrograma().equals("'0000'") && getUnidad().equals("0000") && getAmbito().equals("0000") ? "3" : getNivel());
        mesAntCargo1 = construirMes(mes_ant1, "cargo_acum");
        mesAntAbono1 = construirMes(mes_ant1, "abono_acum");
        mesActCargo1 = construirMes(mes_act1, "cargo");
        mesActAbono1 = construirMes(mes_act1, "abono");

        mesAntCargo2 = construirMes(mes_ant2, "cargo_acum");
        mesAntAbono2 = construirMes(mes_ant2, "abono_acum");
        mesActCargo2 = construirMes(mes_act2, "cargo");
        mesActAbono2 = construirMes(mes_act2, "abono");

        sentencia.append("\n SELECT SUM(saldo_anterior)                 AS ahorro_desahorro_mb, ");
        sentencia.append("\n   SUM(saldo_actual)                        AS ahorro_desahorro_ma, ");
        sentencia.append("\n   (SUM(saldo_actual)- SUM(saldo_anterior)) AS variacion, ");
        sentencia.append("\n   ( ");
        sentencia.append("\n   CASE ");
        sentencia.append("\n     WHEN (SUM(saldo_anterior)- SUM(saldo_actual))!= 0 ");
        sentencia.append("\n     THEN ");
        sentencia.append("\n       CASE ");
        sentencia.append("\n         WHEN SUM(saldo_anterior) != 0 ");
        sentencia.append("\n         THEN ROUND( ( (( SUM(saldo_anterior)- SUM(saldo_actual))/ SUM(saldo_anterior))* 100), 2) ");
        sentencia.append("\n         ELSE ");
        sentencia.append("\n           CASE ");
        sentencia.append("\n             WHEN (SUM(saldo_anterior)- SUM(saldo_actual))>0 ");
        sentencia.append("\n             THEN 100 ");
        sentencia.append("\n             ELSE -100 ");
        sentencia.append("\n           END ");
        sentencia.append("\n       END ");
        sentencia.append("\n     ELSE 0 ");
        sentencia.append("\n   END )*-1 AS porcentaje ");
        sentencia.append("\n FROM ");
        sentencia.append("\n   (SELECT DECODE(TO_CHAR(cc.fecha_vig_ini,'yyyy'), '").append(ejercicioCompara).append("', ");
        sentencia.append("\n     CASE cl.naturaleza ");
        sentencia.append("\n       WHEN 'D' ");
        sentencia.append("\n       THEN DECODE(SUBSTR(cc.cuenta_contable, 1, 1), '4', (( cc.").append(mesAntCargo2);
        sentencia.append(" + cc.").append(mesActCargo2).append(" ) - ( ").append(mesAntAbono2).append(" + cc.");
        sentencia.append(mesActAbono2).append(" )), (( cc.").append(mesAntCargo2).append(" + cc.").append(mesActCargo2);
        sentencia.append(" ) - (").append(mesAntAbono2).append(" + cc.").append(mesActAbono2).append(" ))*- 1) ");
        sentencia.append("\n       ELSE DECODE(SUBSTR(cc.cuenta_contable, 1, 1), '4', (( ").append(mesAntAbono2);
        sentencia.append(" + cc.").append(mesActAbono2).append(" ) - ( cc.").append(mesAntCargo2).append(" + cc.");
        sentencia.append(mesActCargo2).append(" )), (( ").append(mesAntAbono2).append(" + cc.").append(mesActAbono2);
        sentencia.append(" ) - ( cc.").append(mesAntCargo2).append(" + cc.").append(mesActCargo2).append(" ))* -1) ");
        sentencia.append("\n     END, 0 ) AS saldo_anterior, ");
        sentencia.append("\n     DECODE(TO_CHAR(cc.fecha_vig_ini,'yyyy'), '").append(getControlReg().getEjercicio());
        sentencia.append("', ");
        sentencia.append("\n     CASE cl.naturaleza ");
        sentencia.append("\n       WHEN 'D' ");
        sentencia.append("\n     THEN DECODE(SUBSTR(cc.cuenta_contable, 1, 1), '4', (( cc.").append(mesAntCargo1);
        sentencia.append(" + cc.").append(mesActCargo1).append(" ) - ( cc.").append(mesAntAbono1).append(" + cc.");
        sentencia.append(mesActAbono1).append(" )), (( cc.").append(mesAntCargo1).append(" + cc.").append(mesActCargo1);
        sentencia.append(" ) - (cc.").append(mesAntAbono1).append(" + cc.").append(mesActAbono1).append(" ))*- 1) ");
        sentencia.append("\n     ELSE DECODE(SUBSTR(cc.cuenta_contable, 1, 1), '4', (( cc.").append(mesAntAbono1);
        sentencia.append(" + cc.").append(mesActAbono1).append(" ) - ( cc.").append(mesAntCargo1).append(" + cc.");
        sentencia.append(mesActCargo1).append(" )), (( cc.").append(mesAntAbono1).append(" + cc.").append(mesActAbono1);
        sentencia.append(" ) - (cc.").append(mesAntCargo1).append(" + cc.").append(mesActCargo1).append(" ))* -1) ");
        sentencia.append("\n    END, 0 ) AS saldo_actual ");
        sentencia.append("\n  FROM 	rf_tr_cuentas_contables cc, ");
        sentencia.append("\n		rf_tc_clasificador_cuentas cl ");
        sentencia.append("\n  WHERE cc.cuenta_mayor_id              = cl.cuenta_mayor_id ");
        sentencia.append("\n  AND cc.id_catalogo_cuenta             = 1 ");
        sentencia.append("\n  AND cc.nivel                         = ").append(getNivel());
        sentencia.append("\n  AND (TO_CHAR(cc.fecha_vig_ini,'yyyy') = ").append(ejercicioCompara);
        sentencia.append("\n   OR TO_CHAR(cc.fecha_vig_ini,'yyyy')   = ").append(getControlReg().getEjercicio()).append(" ) ");
        sentencia.append("\n   AND (       cc.cuenta_contable LIKE '42__%' ");
        sentencia.append("\n 		OR cc.cuenta_contable LIKE '4319_%' ");
        sentencia.append("\n 		OR cc.cuenta_contable LIKE '439_%' ");
        sentencia.append("\n 		OR cc.cuenta_contable LIKE '511_%' ");
        sentencia.append("\n 		OR cc.cuenta_contable LIKE '512_%' ");
        sentencia.append("\n 		OR cc.cuenta_contable LIKE '513_%' ");
        sentencia.append("\n 		OR cc.cuenta_contable LIKE '524_%' ");
        sentencia.append("\n 		OR cc.cuenta_contable LIKE '528_%' ");
        sentencia.append("\n 		OR cc.cuenta_contable LIKE '529_%' ");
        sentencia.append("\n 		OR cc.cuenta_contable LIKE '553_%' ");
        sentencia.append("\n 		OR cc.cuenta_contable LIKE '5599_%' ");
        sentencia.append("\n 		OR cc.cuenta_contable LIKE '432_%' ");
        sentencia.append("\n 		OR cc.cuenta_contable LIKE '553_%' ) ");

        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n and substr(cc.cuenta_contable,f_posicion_nivel('programa',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append(")) in (").append(getPrograma()).append(")");
        }
        sentencia.append("\n  AND substr(cc.cuenta_contable,f_posicion_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad',cc.cuenta_mayor_id, ");
        sentencia.append(getControlReg().getEjercicio()).append(")) = '").append(getUnidad()).append("' ");
        sentencia.append("\n  AND substr(cc.cuenta_contable,f_posicion_nivel('ambito',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('ambito',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append(")) = '").append(getAmbito()).append("'");
        sentencia.append("\n   ) ");

        return sentencia;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getNivel() {
        return nivel;
    }

    public void setControlReg(ControlRegistro controlReg) {
        this.controlReg = controlReg;
    }

    public ControlRegistro getControlReg() {
        return controlReg;
    }

    private String construirMes(String mes, String movimiento) {
        String regresa;
        switch (Integer.valueOf(getCierre())) {
            case 2:
                regresa = mes + "_" + movimiento + "_eli";
                break;
            case 3:
                if (movimiento.contains("acum")) {
                    if (getControlReg().getUnidad().equals("100")) {
                        regresa = mes + "_" + movimiento + "_eli";
                    } else {
                        regresa = mes + "_" + movimiento;
                    }
                } else {
                    if (getControlReg().getUnidad().equals("100")) {
                        if (movimiento.contains("ini")) {
                            regresa = mes + "_" + movimiento + "_eli";
                        } else {
                            regresa = mes + "_" + movimiento + "_eli_pub";
                        }
                    } else {
                        if (movimiento.contains("ini")) {
                            regresa = mes + "_" + movimiento;
                        } else {
                            regresa = mes + "_" + movimiento + "_pub";
                        }
                    }
                }
                break;
            default:
                regresa = mes + "_" + movimiento;
        }
        return regresa;
    }

    public StringBuffer resultadoEjercicio(String tipoSaldo, String mesActual, String mesAnterior) {
        StringBuffer sentencia = new StringBuffer();
        String cuentasERE = "31207:51201:53105:31105:52101:52202";
        String[] cuentas = new String[3];
        String cargoAcum;
        String abonoAcum;
        String cargo;
        String abono;
        String saldoCargo;
        String saldoAbono;
        setNivel(!getPrograma().equals("'0000'") && getUnidad().equals("0000") && getAmbito().equals("0000") ? "2" : getNivel());
        cuentas = cuentasERE.split(":");
        Fecha fechaPeriodo = new Fecha(getControlReg().getEjercicio() + mesActual + "01", "/");
        mesActual = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodo.getMes() - 1);
        mesAnterior = sia.libs.formato.Fecha.getNombreMesCorto(Integer.valueOf(mesAnterior) - 1);
        if (tipoSaldo.equals("ACTUAL")) {

            fechaPeriodo.addMeses(-1); //susy
            mesAnterior = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodo.getMes() - 1);//susy
        }
        if (mesAnterior.equals("DIC")) {
            abonoAcum = construirMes(mesActual, "abono_ini");
            cargoAcum = construirMes(mesActual, "cargo_ini");
        } else {
            abonoAcum = construirMes(mesAnterior, "abono_acum");
            cargoAcum = construirMes(mesAnterior, "cargo_acum");
        }

        if (tipoSaldo.equals("ACTUAL")) {
            abono = construirMes(mesActual, "abono");
            cargo = construirMes(mesActual, "cargo");
            saldoAbono = "sum(cc." + abonoAcum + " + cc." + abono + ")";
            saldoCargo = "sum(cc." + cargoAcum + " + cc." + cargo + ")";
        } else {
            saldoAbono = "sum(cc." + abonoAcum + ")";
            saldoCargo = "sum(cc." + cargoAcum + ")";
        }
        sentencia.append("select * from (");
        sentencia.append("select distinct ");
        if (!programa.equals("'0000'")) {
            sentencia.append(" substr(cc.cuenta_contable,6,4) programa,");
        }
        sentencia.append(" substr(cc.cuenta_contable,10,4) ua ,substr(cc.cuenta_contable,14,4) ambito,");
        sentencia.append("  cc.cuenta_mayor_id cuenta_mayor_id, SUBSTR(cc.cuenta_contable,0,5) cuentaContable,");
        sentencia.append("  upper(tc.descripcion) concepto, ");
        sentencia.append("  (case when  SUBSTR(cc.cuenta_contable,0,5) in (31207,51201,53105) then 'I' else 'E' end) natura, ");
        sentencia.append("  (case when SUBSTR(cc.cuenta_contable,0,5) in ('31207','51201','53105') then ");
        sentencia.append(saldoAbono);
        sentencia.append("        else (case when SUBSTR(cc.cuenta_contable, 0, 5) in ('31105','52101','52202') then ");
        sentencia.append(saldoCargo);
        sentencia.append(" end)");
        sentencia.append(" end) saldo ");
        sentencia.append(" from ");
        sentencia.append("  rf_tr_cuentas_contables cc inner join   rf_tc_clasificador_cuentas tc on ");
        sentencia.append("  cc.cuenta_mayor_id = tc.cuenta_mayor_id ");
        sentencia.append(" where ");
        sentencia.append("  cc.nivel= ").append(getNivel()).append(" and ");
        sentencia.append("  extract(year from cc.fecha_vig_ini) = ").append(getControlReg().getEjercicio());
        sentencia.append("  and cc.id_catalogo_cuenta =").append(getControlReg().getIdCatalogoCuenta());
        if (!programa.equals("'0000'")) {
            sentencia.append(" and substr(cuenta_contable, 6, 4) in (").append(getPrograma()).append(")");
        }
        sentencia.append("and substr(cuenta_contable,10,4) = '").append(getUnidad()).append("'");
        sentencia.append("and substr(cuenta_contable,14,4) = '").append(getAmbito()).append("'");
        sentencia.append("and  (substr(cc.cuenta_contable,0,5) = '31207' ");
        sentencia.append("or substr(cc.cuenta_contable,0,5) = '51201' ");
        sentencia.append("    or substr(cc.cuenta_contable,0,5) = '53105' ");
        sentencia.append("    or substr(cc.cuenta_contable,0,5) = '31105' ");
        sentencia.append("    or substr(cc.cuenta_contable,0,5) = '52101' ");
        sentencia.append("    or substr(cc.cuenta_contable,0,5) = '52202' )");
        sentencia.append(" group by  ");
        if (!programa.equals("'0000'")) {
            sentencia.append(" substr(cc.cuenta_contable,6,4), ");
        }
        sentencia.append(" substr(cc.cuenta_contable,10,4), ");
        sentencia.append(" substr(cc.cuenta_contable,14,4), ");
        sentencia.append("  cc.cuenta_mayor_id, SUBSTR(cc.cuenta_contable,0,5) , tc.descripcion, ");
        sentencia.append("  tc.naturaleza");
        sentencia.append(" order by ");
        if (!programa.equals("'0000'")) {
            sentencia.append(" substr(cc.cuenta_contable, 6, 4),");
        }
        sentencia.append(" natura desc,");
        sentencia.append(" substr(cc.cuenta_contable, 10, 4),");
        sentencia.append(" substr(cc.cuenta_contable, 14, 4),");
        sentencia.append(" SUBSTR(cc.cuenta_contable, 0, 5)");
        sentencia.append(") ctas where ctas.saldo<>0");
        //System.out.println("Sentencia ERR2: "+sentenciaImpl);
        return sentencia;
    } //formarSentenciaERE2  

    public StringBuffer rectificacionResultados(String tipoSaldo, String mesActual, String mesAnterior) {
        StringBuffer sentencia = new StringBuffer();
        String cargoAcum;
        String abonoAcum;
        String cargo;
        String abono;
        String cadenaSaldo;
        setNivel(!getPrograma().equals("'0000'") && getUnidad().equals("0000") && getAmbito().equals("0000") ? "2" : getNivel());
        Fecha fechaPeriodo = new Fecha(getControlReg().getEjercicio() + mesActual + "01", "/");
        mesActual = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodo.getMes() - 1);
        if (tipoSaldo.equals("ACTUAL")) {
            fechaPeriodo.addMeses(-1);
            mesAnterior = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodo.getMes() - 1);//susy
        } else {
            mesAnterior = sia.libs.formato.Fecha.getNombreMesCorto(Integer.valueOf(mesAnterior) - 1);//susy
        }
        if (mesAnterior.equals("DIC")) {
            abonoAcum = construirMes(mesActual, "abono_ini");
            cargoAcum = construirMes(mesActual, "cargo_ini");
        } else {
            abonoAcum = construirMes(mesAnterior, "abono_acum");
            cargoAcum = construirMes(mesAnterior, "cargo_acum");
        }

        if (tipoSaldo.equals("ACTUAL")) {
            abono = construirMes(mesActual, "abono");
            cargo = construirMes(mesActual, "cargo");
            cadenaSaldo = " (CASE tc.naturaleza WHEN 'D' THEN  SUM(cc." + cargoAcum + " + cc." + cargo + ") ELSE SUM(cc." + abonoAcum + " + cc." + abono + ") end) saldo ";
        } else {
            cadenaSaldo = " (CASE tc.naturaleza WHEN 'D' THEN  SUM(cc." + cargoAcum + ") ELSE SUM(cc." + abonoAcum + ") end) saldo";
        }
        sentencia.append("select distinct ");
        if (!programa.equals("'0000'")) {
            sentencia.append("  substr(cc.cuenta_contable,6,4) programa,");
        }
        sentencia.append(" substr(cc.cuenta_contable,10,4) ua ,substr(cc.cuenta_contable,14,4) ambito,");
        sentencia.append("  cc.cuenta_mayor_id cuenta_mayor_id, SUBSTR(cc.cuenta_contable,0,5) cuentaContable,");
        sentencia.append("  upper(tc.descripcion) concepto, ");
        sentencia.append("  (case when  SUBSTR(cc.cuenta_contable,0,5) in (31106) then 'I' else 'E' end) natura, ");
        sentencia.append(cadenaSaldo);
        sentencia.append(" from ");
        sentencia.append("  rf_tr_cuentas_contables cc inner join   rf_tc_clasificador_cuentas tc on ");
        sentencia.append("  cc.cuenta_mayor_id = tc.cuenta_mayor_id ");
        sentencia.append(" where ");
        sentencia.append("  cc.nivel= ").append(getNivel()).append(" and ");
        sentencia.append("  extract(year from cc.fecha_vig_ini)= ").append(getControlReg().getEjercicio()).append(" ");
        sentencia.append("  and cc.id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta()).append(" ");
        if (!programa.equals("'0000'")) {
            sentencia.append(" and substr(cuenta_contable, 6, 4) in (").append(getPrograma()).append(")");
        }
        sentencia.append("  and substr(cuenta_contable,10,4) = '").append(getUnidad()).append("' ");
        sentencia.append("  and substr(cuenta_contable,14,4) = '").append(getAmbito()).append("' ");
        sentencia.append("  and  (substr(cc.cuenta_contable,0,5) = '31106' ");
        sentencia.append("        or substr(cc.cuenta_contable,0,5) = '52102' ) ");

        sentencia.append(" group by ");
        if (!programa.equals("'0000'")) {
            sentencia.append(" substr(cc.cuenta_contable,6,4), ");
        }
        sentencia.append(" substr(cc.cuenta_contable,10,4), ");
        sentencia.append(" substr(cc.cuenta_contable,14,4), ");
        sentencia.append("  cc.cuenta_mayor_id, SUBSTR(cc.cuenta_contable,0,5) , tc.descripcion, ");
        sentencia.append("  tc.naturaleza,cc.").append(cargoAcum).append(",cc.").append(abonoAcum).append(" ");
        sentencia.append(" order by ");
        if (!programa.equals("'0000'")) {
            sentencia.append(" substr(cc.cuenta_contable,6,4), ");
        }
        sentencia.append("natura desc,");
        sentencia.append(" substr(cc.cuenta_contable, 10, 4),");
        sentencia.append(" substr(cc.cuenta_contable, 14, 4),");
        sentencia.append(" SUBSTR(cc.cuenta_contable, 0, 5)");
        return sentencia;
    } //formarSentenciaERR  

    public StringBuffer posicionFinancieraCheques(String fecha_consolidacion, String cuentasBancos) {
        Fecha fechaPeriodo = new Fecha(fecha_consolidacion, "/");
        String mes_act = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodo.getMes() - 1);
        fechaPeriodo.addMeses(-1); //susy
        String mes_ant = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodo.getMes() - 1);//susy
        StringBuffer sentencia = new StringBuffer();
        StringBuilder queryCuentasBancos = new StringBuilder();
        String cuentasMayorPFC = "11203:11206:21205:23101:23113:13110";
        String cargoAcum;
        String abonoAcum;
        String mesActCargo;
        String mesActAbono;
        String[] cuentasPFC = new String[5];
        cuentasPFC = cuentasMayorPFC.split(":");
        String cuentaBanco[] = cuentasBancos.split(",");
        cuentasBancos = "";
        for (int i = 0; i < cuentaBanco.length; i++) {
            cuentasBancos = cuentasBancos + "'" + cuentaBanco[i] + "',";
        }
        cuentasBancos = cuentasBancos.substring(0, cuentasBancos.length() - 1);
        if (mes_act.equals("ENE")) {
            cargoAcum = construirMes(mes_act, "cargo_ini");
            abonoAcum = construirMes(mes_act, "abono_ini");
        } else {
            cargoAcum = construirMes(mes_ant, "cargo_acum");
            abonoAcum = construirMes(mes_ant, "abono_acum");
        }
        mesActCargo = construirMes(mes_act, "cargo");
        mesActAbono = construirMes(mes_act, "abono");
        if (cuentasBancos.equals("''")) {
            queryCuentasBancos.append("SELECT distinct substr(tc.cuenta_contable,f_posicion_nivel('5',tc.cuenta_mayor_id,");
            queryCuentasBancos.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('5',tc.cuenta_mayor_id,");
            queryCuentasBancos.append(getControlReg().getEjercicio()).append(")) cuenta\n");
            queryCuentasBancos.append("FROM rf_tr_cuentas_contables tc\n");
            queryCuentasBancos.append("WHERE id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta()).append("\n");
            queryCuentasBancos.append("AND EXTRACT(YEAR FROM fecha_vig_ini) = ").append(getControlReg().getEjercicio()).append("\n");
            /*if(!programa.equals("'0000'"))
             queryCuentasBancos.append("and substr(cuenta_contable, 6,4) in ("+programa+")\n");
             queryCuentasBancos.append("and substr(cuenta_contable, 10,4) = '"+unidad+"'\n");
             queryCuentasBancos.append("and substr(cuenta_contable, 14,4) = '"+estado+"'\n");*/
            queryCuentasBancos.append("AND(cuenta_contable LIKE '11203%')\n");
            queryCuentasBancos.append("and nivel =5\n");
            cuentasBancos = queryCuentasBancos.toString();
        }

        sentencia.append("\nselect ");
        sentencia.append("\n    cc.cuenta_mayor_id cuenta_mayor_id,");
        sentencia.append("\n    substr(cc.cuenta_contable,f_posicion_nivel('mayor',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('mayor',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append(")) cuentaContable,");
        sentencia.append("\n    substr(cc.cuenta_contable,f_posicion_nivel('programa',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append(")) programa,");
        sentencia.append("\n    substr(cc.cuenta_contable,f_posicion_nivel('unidad',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append(")) ua,");
        sentencia.append("\n    substr(cc.cuenta_contable,f_posicion_nivel('ambito',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('ambito',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append(")) ambito,");
        sentencia.append("\n    substr(cc.cuenta_contable,f_posicion_nivel('5',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('5',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append(")) cuentaContableC, ");
        sentencia.append("\n    ltrim(substr(cc.cuenta_contable,f_posicion_nivel('5',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('5',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append(")),'0') cuenta,");
        sentencia.append("\n    upper(tc.descripcion) concepto,");
        sentencia.append("\n    tc.naturaleza,");
        sentencia.append("\n    cc.descripcion,");
        sentencia.append("\n    (case when  tc.naturaleza = 'D'");
        sentencia.append("\n     then (sum(cc.").append(cargoAcum).append(" + cc.").append(mesActCargo);
        sentencia.append(") - sum(cc.").append(abonoAcum).append(" + cc.").append(mesActAbono).append("))");
        sentencia.append("\n     else (sum(cc.").append(abonoAcum).append(" + cc.").append(mesActAbono);
        sentencia.append(") - sum(cc.").append(cargoAcum).append(" + cc.").append(mesActCargo).append("))");
        sentencia.append("\n     end) saldo");
        sentencia.append(" \nfrom rf_tr_cuentas_contables cc ");
        sentencia.append(" inner join rf_tc_clasificador_cuentas tc on cc.cuenta_mayor_id = tc.cuenta_mayor_id ");
        sentencia.append(",(select \n");
        sentencia.append("substr(cc.cuenta_contable,f_posicion_nivel('mayor',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('mayor',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append(")) cuentaContable,\n");
        sentencia.append("substr(cc.cuenta_contable,f_posicion_nivel('programa',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append(")) programa,\n");
        sentencia.append("substr(cc.cuenta_contable,f_posicion_nivel('5',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('5',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append(")) cuentaContableC \n");
        sentencia.append("from rf_tr_cuentas_contables cc  inner join rf_tc_clasificador_cuentas tc on cc.cuenta_mayor_id = tc.cuenta_mayor_id  \n");
        sentencia.append("where \n");
        sentencia.append("  extract(year from cc.fecha_vig_ini)=").append(getControlReg().getEjercicio()).append("\n");
        sentencia.append("  and cc.id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta()).append("\n");
        if (!programa.equals("'0000'")) {
            sentencia.append(" and substr(cc.cuenta_contable,f_posicion_nivel('programa',tc.cuenta_mayor_id,");
            sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',tc.cuenta_mayor_id,");
            sentencia.append(getControlReg().getEjercicio()).append(")) in (").append(programa).append(")\n");
        }
        sentencia.append(" and substr(cc.cuenta_contable,f_posicion_nivel('unidad',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append(")) = '").append(getControlReg().getUniEjecFormateada());
        sentencia.append("'\n");
        sentencia.append(" and substr(cc.cuenta_contable,f_posicion_nivel('ambito',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('ambito',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append(")) ='").append(getControlReg().getAmbEntFormateada());
        sentencia.append("'\n");
        sentencia.append(" and (substr(cc.cuenta_contable,f_posicion_nivel('mayor',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('mayor',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append(")) in ('11203','11206','21205','23101','23113','13110')) \n");
        sentencia.append(" and substr(cc.cuenta_contable,f_posicion_nivel('5',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('5',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append(")) in \n");
        sentencia.append("(").append(cuentasBancos).append(")\n");
        sentencia.append("and nivel =5\n");
        sentencia.append("group by \n");
        sentencia.append("          substr(cc.cuenta_contable,f_posicion_nivel('mayor',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('mayor',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append(")),\n");
        sentencia.append("          substr(cc.cuenta_contable,f_posicion_nivel('programa',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append(")) ,\n");
        sentencia.append("          substr(cc.cuenta_contable,f_posicion_nivel('5',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('5',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("))\n");
        sentencia.append("having    substr(cc.cuenta_contable,f_posicion_nivel('mayor',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('mayor',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append(")) = '11203'       \n");
        sentencia.append(" order by substr(cc.cuenta_contable,f_posicion_nivel('programa',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append(")),\n");
        sentencia.append("          substr(cc.cuenta_contable,f_posicion_nivel('mayor',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('mayor',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("))\n");
        sentencia.append(") x\n");
        sentencia.append(" \nwhere ");
        sentencia.append("\n  extract(year from cc.fecha_vig_ini)=").append(getControlReg().getEjercicio()).append("\n");
        sentencia.append("  and cc.id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta()).append("\n");
        if (!programa.equals("'0000'")) {
            sentencia.append("\n and substr(cc.cuenta_contable,f_posicion_nivel('programa',tc.cuenta_mayor_id,");
            sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',tc.cuenta_mayor_id,");
            sentencia.append(getControlReg().getEjercicio()).append(")) in (").append(programa).append(")");
        }
        sentencia.append("\n and substr(cc.cuenta_contable,f_posicion_nivel('unidad',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append(")) = '").append(getControlReg().getUniEjecFormateada());
        sentencia.append("' ");
        if (getNivel().equals("4")) {
            sentencia.append("\n and substr(cc.cuenta_contable,f_posicion_nivel('ambito',tc.cuenta_mayor_id,");
            sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('ambito',tc.cuenta_mayor_id,");
            sentencia.append(getControlReg().getEjercicio()).append(")) = '").append(getControlReg().getAmbEntFormateada());
            sentencia.append("' ");
        }
        sentencia.append("\n and (substr(cc.cuenta_contable,f_posicion_nivel('mayor',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('mayor',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append(")) in ('11203','11206','21205','23101','23113','13110')) ");
        sentencia.append("\n and substr(cc.cuenta_contable,f_posicion_nivel('5',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('5',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append(")) in ");
        sentencia.append("\n(").append(cuentasBancos).append(")");
        sentencia.append("\n and nivel =5");
        sentencia.append("\n and x.cuentaContableC =  substr(cc.cuenta_contable,f_posicion_nivel('5',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('5',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("))");
        sentencia.append("\n and x.programa = substr(cc.cuenta_contable,f_posicion_nivel('programa',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("))");
        sentencia.append("\n group by cc.cuenta_mayor_id,");
        sentencia.append("\n          substr(cc.cuenta_contable,f_posicion_nivel('mayor',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('mayor',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append(")),");
        sentencia.append("\n          substr(cc.cuenta_contable,f_posicion_nivel('programa',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append(")),");
        sentencia.append("\n          substr(cc.cuenta_contable,f_posicion_nivel('unidad',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append(")),");
        sentencia.append("\n          substr(cc.cuenta_contable,f_posicion_nivel('ambito',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('ambito',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append(")),");
        sentencia.append("\n          substr(cc.cuenta_contable,f_posicion_nivel('5',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('5',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append(")),");
        sentencia.append("\n          tc.descripcion,");
        sentencia.append("\n          tc.naturaleza,");
        sentencia.append("\n          cc.descripcion");
        sentencia.append("\n order by substr(cc.cuenta_contable,f_posicion_nivel('5',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('5',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append(")),");
        sentencia.append("\n          substr(cc.cuenta_contable,f_posicion_nivel('ambito',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('ambito',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append(")),");
        sentencia.append("\n          substr(cc.cuenta_contable,f_posicion_nivel('unidad',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append(")),");
        sentencia.append("\n          substr(cc.cuenta_contable,f_posicion_nivel('programa',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append(")),");
        sentencia.append("\n          substr(cc.cuenta_contable,f_posicion_nivel('mayor',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('mayor',tc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append(")),");
        sentencia.append("\n          cc.cuenta_mayor_id");
        return sentencia;
    } //formarSentenciaPFC posici�n financiera de cheques

    public StringBuffer sentenciaEOAR(String fecha_consolidacion) {
        StringBuffer sentencia = new StringBuffer();
        Fecha fechaPeriodo = new Fecha(fecha_consolidacion, "/");
        String mes_act_num = String.valueOf(fechaPeriodo.getMes());
        String mes_act = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodo.getMes() - 1);
        fechaPeriodo.addMeses(-1);
        String mes_ant = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodo.getMes() - 1);//susy      
        String cargoAcum;
        String abonoAcum;
        String mesActCargo;
        String mesActAbono;
        if (mes_act.equals("ENE")) {
            cargoAcum = construirMes(mes_act, "cargo_ini");
            abonoAcum = construirMes(mes_act, "abono_ini");
        } else {
            cargoAcum = construirMes(mes_ant, "cargo_acum");
            abonoAcum = construirMes(mes_ant, "abono_acum");
        }
        mesActCargo = construirMes(mes_act, "cargo");
        mesActAbono = construirMes(mes_act, "abono");

        sentencia.append("select * from ( \n");
        sentencia.append("select p.descripcion nomPrograma, eoarSIAFin.progorder, capitulo, c.descripcion nomCapitulo, \n");
        sentencia.append("DAcum, eAcum, fAcum,  \n");
        sentencia.append("(EAcum + FAcum) g, c61214 hMIPF, ((EAcum + FAcum) - c61214 - DAcum) iCalc,   \n");
        sentencia.append("decode(((EAcum + FAcum) - DAcum),0,0,(c61214/((EAcum + FAcum) - DAcum))) jPorcEjer,  \n ");
        //sentencia.append("(c61214/((EAcum + FAcum) - DAcum)) jPorcEjer,  \n ");
        sentencia.append("c61205 lComprom, (c61214 + c61205) mSuma, decode(((EAcum + FAcum) - DAcum),0,0,((c61214 + c61205)/((EAcum + FAcum) - DAcum)))  nPorc,  \n ");
        sentencia.append("((EAcum + FAcum) - (c61214 + c61205) - DAcum) oDispon  \n ");
        sentencia.append("from  rf_tc_programas p, rf_tc_capitulos c, (  \n");
        sentencia.append(" select eoarSIA.*, eoarMIPF.c61214, eoarMIPF.c61205  \n");
        sentencia.append(" from \n ");
        sentencia.append(" (select Programa, decode(programa,null,'0000',programa) progorder, capitulo, sum(E) DAcum, sum(F)EAcum, sum(G) FAcum \n ");
        sentencia.append(" from (  \n");
        sentencia.append("select p.clave Programa,c.clave Capitulo, 'A' naturaleza, '00000000' cuenta_Contable, \n ");
        sentencia.append("   0.0 saldo_acu, 0.0 E, 0.0 F, 0.0 G from rf_tc_programas p, rf_tc_capitulos c \n ");
        sentencia.append("where p.clave != '0000' \n ");
        sentencia.append("union all  \n");
        sentencia.append(" select saldo_cuenta.*,   ");
        sentencia.append(" case when subStr(cuenta_contable,1,21) ='311050001010000110103' then saldo_acu  \n ");
        sentencia.append(" else 0 \n ");
        sentencia.append(" end E, \n ");
        sentencia.append(" case when subStr(cuenta_contable,1,21) ='312070001010000110008' and programa = '0001' then saldo_acu  \n");
        sentencia.append(" when cuenta_contable like '312070008________0008____0000%' then saldo_acu\n ");
        sentencia.append(" else 0 \n ");
        sentencia.append(" end F,  \n ");
        sentencia.append(" case when subStr(cuenta_contable,1,21) ='312070001010000110003' then saldo_acu  \n ");
        sentencia.append(" else 0 \n ");
        sentencia.append(" end G \n ");
        sentencia.append(" from( \n ");
        sentencia.append("   select decode(substr(t.cuenta_contable,f_posicion_nivel('7',t.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('7',t.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append(")),'0000',\n");
        sentencia.append("decode(substr(t.cuenta_contable,f_posicion_nivel('programa',t.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('programa',t.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append(")),'0008','0008','0001'),\n");
        sentencia.append("substr(t.cuenta_contable,f_posicion_nivel('7',t.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('7',t.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("))) programa, \n");
        sentencia.append("substr(t.cuenta_contable,f_posicion_nivel('6',t.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('6',t.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append(")) capitulo, tc.naturaleza,\n");
        sentencia.append(" t.cuenta_contable, \n ");
        sentencia.append(" case when tc.naturaleza ='D'   ");
        sentencia.append(" then (").append(cargoAcum).append("+").append(mesActCargo).append(") - (");
        sentencia.append(abonoAcum).append("+").append(mesActAbono).append(") \n ");
        sentencia.append(" else (").append(abonoAcum).append("+").append(mesActAbono).append(") - (");
        sentencia.append(cargoAcum).append("+").append(mesActCargo).append(") \n ");
        sentencia.append(" end saldo_acu \n ");
        sentencia.append(" from rf_tr_cuentas_contables t,rf_tc_clasificador_cuentas  tc \n ");
        sentencia.append(" where t.cuenta_mayor_id = tc.cuenta_mayor_id  \n ");
        sentencia.append(" and t.id_catalogo_cuenta=").append(getControlReg().getIdCatalogoCuenta()).append("\n");
        sentencia.append(" and extract(year from t.fecha_vig_ini)=").append(getControlReg().getEjercicio()).append("\n ");
        sentencia.append(" and ((t.cuenta_contable like '311050001010000110103____0001%' and t.nivel=7) or \n ");
        sentencia.append("     (t.cuenta_contable  like '311050001010000110103____0002%' and t.nivel=7) or \n ");
        sentencia.append("     (t.cuenta_contable  like '311050001010000110103____0003%' and t.nivel=7) or \n ");
        sentencia.append("     (t.cuenta_contable  like '311050001010000110103____0004%' and t.nivel=7) or \n ");
        sentencia.append(" (t.cuenta_contable  like '312070001010000110003____0001%' and t.nivel=7) or \n ");
        sentencia.append("     (t.cuenta_contable  like '312070001010000110003____0002%' and t.nivel=7) or \n ");
        sentencia.append("     (t.cuenta_contable  like '312070001010000110003____0003%' and t.nivel=7) or \n ");
        sentencia.append("     (t.cuenta_contable  like '312070001010000110003____0004%' and t.nivel=7) or \n ");
        sentencia.append("     (t.cuenta_contable  like '312070001010000110008____0000%' and t.nivel=6) or \n ");
        sentencia.append("     (t.cuenta_contable  like '312070008________0008____0000%' and t.nivel=6))  \n ");
        sentencia.append(" order by  programa, capitulo \n ");
        sentencia.append(" )saldo_cuenta) \n ");
        sentencia.append(" where programa <>'0000'  \n ");
        sentencia.append(" group by CUBE(Programa, capitulo) \n ");
        sentencia.append(" having grouping(capitulo) = 0 \n ");
        sentencia.append(" order by progorder, capitulo \n ");
        sentencia.append(" ) eoarSIA, \n  ");
        sentencia.append(" (  \n ");
        sentencia.append("SELECT  programa,decode(programa, null,'0000',programa) progorder, capitulo, sum(c61214) c61214, sum(c61205) c61205 \n ");
        sentencia.append("from ( \n ");
        sentencia.append("select p.clave Programa,c.clave Capitulo, \n ");
        sentencia.append("   0.0 C61214, 0.0 c61205 from rf_tc_programas p, rf_tc_capitulos c \n ");
        sentencia.append("where p.clave != '0000' \n ");
        sentencia.append("union all \n ");
        sentencia.append("SELECT  \n ");
        sentencia.append("  programa, \n ");
        sentencia.append("  PARTIDA  capitulo, \n ");
        sentencia.append("  sum(PEAc) as C61214,  \n ");
        sentencia.append("  (sum(GXCac)+sum(PCAc)) as c61205   \n");
        sentencia.append("FROM  \n ");
        sentencia.append("( \n ");
        sentencia.append("select \n ");
        sentencia.append("    sum(decode(id_tipo_presup,1, enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + diciembre + 0,0)) as AutOrig, \n");
        sentencia.append("    sum(decode(id_tipo_presup,8, enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + diciembre + 0,0)) as Amp, \n  ");
        sentencia.append("    sum(decode(id_tipo_presup,9, enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + diciembre + 0,0)) as Red, \n");
        sentencia.append("    sum(decode(id_tipo_presup,1, enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + diciembre + 0,8, enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + diciembre + 0,9, -1 * ( enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + diciembre + 0),0)) as ModPres,  \n ");
        sentencia.append("    sum(decode(id_tipo_presup,1, enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + diciembre + 0,8,enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + diciembre + 0,9,-1 * enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + diciembre + 0,0)) as PAM,  \n ");
        sentencia.append("    sum(decode(id_tipo_presup,1, enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + diciembre + 0,8, enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + diciembre + 0,9, -1 * ( enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + diciembre + 0),0)) as PAAc, \n  ");
        sentencia.append("    sum(decode(id_tipo_presup,5, enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + diciembre + 0,6,enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + diciembre + 0,7,enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + diciembre + 0,0)) as PEM,  \n ");
        sentencia.append("    sum(decode(id_tipo_presup,5, enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + diciembre + 0,6, enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + diciembre + 0,7, enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + diciembre + 0,0)) as PEAc, \n");
        //sentencia.append("    case when cpg.programa = '0008' then 0 \n");
        //sentencia.append("    else sum(decode(id_tipo_presup,5, enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + diciembre + 0,6, enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + diciembre + 0,7, enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + diciembre + 0,0)) end PEAc,  \n");
        sentencia.append("    0 as GXCac, \n");
        sentencia.append("    decode(p.mes, 1, sum(decode(id_tipo_presup,4, enero + 0, 0)),   \n");
        sentencia.append("      decode(p.mes, 2, sum(decode(id_tipo_presup,4, enero + febrero + 0, 0)), \n ");
        sentencia.append("        decode(p.mes, 3, sum(decode(id_tipo_presup,4, enero + febrero + marzo + 0, 0)),  \n");
        sentencia.append("          decode(p.mes, 4, sum(decode(id_tipo_presup,4, enero + febrero + marzo + abril + 0, 0)),  \n");
        sentencia.append("            decode(p.mes, 5, sum(decode(id_tipo_presup,4, enero + febrero + marzo + abril + mayo + 0, 0)),  \n");
        sentencia.append("              decode(p.mes, 6, sum(decode(id_tipo_presup,4, enero + febrero + marzo + abril + mayo + junio + 0, 0)),  \n");
        sentencia.append("                decode(p.mes, 7, sum(decode(id_tipo_presup,4, enero + febrero + marzo + abril + mayo + junio + julio + 0, 0)),  \n");
        sentencia.append("                  decode(p.mes, 8, sum(decode(id_tipo_presup,4, enero + febrero + marzo + abril + mayo + junio + julio + agosto + 0, 0)), \n ");
        sentencia.append("                    decode(p.mes, 9, sum(decode(id_tipo_presup,4, enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + 0, 0)), \n ");
        sentencia.append("                      decode(p.mes, 10, sum(decode(id_tipo_presup,4, enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + 0, 0)),  \n");
        sentencia.append("                        decode(p.mes, 11, sum(decode(id_tipo_presup,4, enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + 0, 0)), sum(decode(id_tipo_presup,4, enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + diciembre + 0, 0)) ) \n ");
        sentencia.append("                      )  \n");
        sentencia.append("                    )  \n");
        sentencia.append("                  )  \n ");
        sentencia.append("                )   \n");
        sentencia.append("              )  \n ");
        sentencia.append("            )  \n ");
        sentencia.append("          )  \n");
        sentencia.append("        )  \n");
        sentencia.append("      )  \n");
        sentencia.append("    ) as PCAc, \n ");
        sentencia.append("    sum(decode(id_tipo_presup,4, enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + diciembre + 0,0)) as PCA,   \n");
        sentencia.append("    sum(decode(id_tipo_presup,1, enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + diciembre + 0,0) + decode(id_tipo_presup,8,enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + diciembre + 0,0) - decode(id_tipo_presup,9,enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + diciembre + 0,0) - decode(id_tipo_presup,5,enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + diciembre + 0,0) - decode(id_tipo_presup,6,enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + diciembre + 0,0) - decode(id_tipo_presup,7,enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + diciembre + 0,0) - decode(id_tipo_presup,4,enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + diciembre + 0,0)) as PDM,  \n ");
        sentencia.append("    sum(decode(id_tipo_presup,1, enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + diciembre + 0,8, enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + diciembre + 0,0) - decode(id_tipo_presup,9, enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + diciembre + 0,0) - decode(id_tipo_presup,5, enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + diciembre + 0,0) - decode(id_tipo_presup,6, enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + diciembre + 0,0) - decode(id_tipo_presup,7, enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + diciembre + 0,0) - decode(id_tipo_presup,4, enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + diciembre + 0,0)) as PDAc,   \n");
        sentencia.append("    sum(decode(id_tipo_presup,1, enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + diciembre + 0,8, enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + diciembre + 0,0) - decode(id_tipo_presup,9, enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + diciembre + 0,0)- decode(id_tipo_presup,5, enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + diciembre + 0,0) - decode(id_tipo_presup,6, enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + diciembre + 0,0) - decode(id_tipo_presup,7, enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + diciembre + 0,0) - decode(id_tipo_presup,4, enero + febrero + marzo + abril + mayo + junio + julio + agosto + septiembre + octubre + noviembre + diciembre + 0,0)) as PDA,   \n");
        sentencia.append("    cpg.programa, \n");
        sentencia.append("    substr(pp.partida,0,1)||'000' as partida  \n ");
        sentencia.append("from rf_th_cierre_presupuesto@dblsia_sic p, rf_tc_claves_presupuestarias@dblsia_sic cp, \n");
        sentencia.append("   rf_tc_partidas_presupuestales@dblsia_sic pp, rf_tr_rel_uea_cveprog@dblsia_sic ruc, \n");
        sentencia.append("   (select decode(substr(clave_programatica,1,5),'01E02','0002','01E03','0003','01E01','0004',decode(substr(clave_programatica,1,11),'01P02280401','0008','01P02280501','0008','01P02280601','0008','0001')) programa , cp.* \n");
        sentencia.append("   from rf_tr_claves_programaticas@dblsia_sic cp) cpg  \n");
        sentencia.append("where \n ");
        sentencia.append("    p.clave_presupuestaria_id = cp.clave_presupuestaria_id  \n ");
        sentencia.append("    and cp.id_partida=pp.id_partida \n");
        sentencia.append("    and cp.id_rel_uea_cve_prog = ruc.id_rel_uea_cve_prog \n ");
        sentencia.append("    and ruc.id_cve_prog=cpg.id_cve_prog and cpg.eliminado = 0 \n");
        sentencia.append("    and p.ejercicio = ").append(getControlReg().getEjercicio());
        sentencia.append(" and mes = ").append(mes_act_num).append(" \n ");
        sentencia.append("    and p.corte = (  \n ");
        sentencia.append("      select max(corte)  \n ");
        sentencia.append("      from rf_tr_control_meses@dblsia_sic  \n ");
        sentencia.append("      where  \n ");
        sentencia.append("        ejercicio = ").append(getControlReg().getEjercicio()).append(" \n");
        sentencia.append("        and mes = ").append(mes_act_num).append(" \n ");
        sentencia.append("      )  \n ");
        sentencia.append("group by cpg.programa, substr(pp.partida,0,1), p.mes \n ");
        sentencia.append("union all \n");
        sentencia.append("select \n ");
        sentencia.append("    0 as AutOrig,  \n");
        sentencia.append("    0 as Amp, \n ");
        sentencia.append("    0 as Red, \n ");
        sentencia.append("    0 as ModPres, \n ");
        sentencia.append("    0 as PAM, \n ");
        sentencia.append("    0 as PAAc, \n ");
        sentencia.append("    0 as PEM, \n ");
        sentencia.append("    0 as PEAc, \n  ");
        sentencia.append("    (sum( (ministrado1 - comprobado1) + (ministrado2 - comprobado2) + (ministrado3 - comprobado3) + (ministrado4 - comprobado4) + (ministrado5 - comprobado5) + (ministrado6 - comprobado6) + (ministrado7 - comprobado7) + (ministrado8 - comprobado8) + (ministrado9 - comprobado9) + (ministrado10 - comprobado10) + (ministrado11 - comprobado11) + (ministrado12 - comprobado12) )) as GXCac, \n  ");
        sentencia.append("    0 as PCAc, 0 as PCa,  \n ");
        sentencia.append("    -1 * (sum(ministrado1 - comprobado1)) as PDM,  \n ");
        sentencia.append("    -1 * (sum( (ministrado1 - comprobado1) + (ministrado2 - comprobado2) + (ministrado3 - comprobado3) + (ministrado4 - comprobado4) + (ministrado5 - comprobado5) + (ministrado6 - comprobado6) + (ministrado7 - comprobado7) + (ministrado8 - comprobado8) + (ministrado9 - comprobado9) + (ministrado10 - comprobado10) + (ministrado11 - comprobado11) + (ministrado12 - comprobado12) )) as PDAc, \n  ");
        sentencia.append("    -1 * (sum( (ministrado1 - comprobado1) + (ministrado2 - comprobado2) + (ministrado3 - comprobado3) + (ministrado4 - comprobado4) + (ministrado5 - comprobado5) + (ministrado6 - comprobado6) + (ministrado7 - comprobado7) + (ministrado8 - comprobado8) + (ministrado9 - comprobado9) + (ministrado10 - comprobado10) + (ministrado11 - comprobado11) + (ministrado12 - comprobado12) )) as PDA,  \n ");
        sentencia.append("    cpg.programa, \n");
        sentencia.append("    substr(pp.partida,0,1)||'000' as partida \n  ");
        sentencia.append("from rf_th_cierre_financiero@dblsia_sic cf, rf_tc_claves_presupuestarias@dblsia_sic cp,\n ");
        sentencia.append("   rf_tc_partidas_presupuestales@dblsia_sic pp, rf_tr_rel_uea_cveprog@dblsia_sic ruc,\n ");
        sentencia.append("   (select decode(substr(clave_programatica,1,5),'01E02','0002','01E03','0003','01E01','0004',decode(substr(clave_programatica,1,11),'01P02280401','0008','01P02280501','0008','01P02280601','0008','0001')) programa , cp.* \n");
        sentencia.append("   from rf_tr_claves_programaticas@dblsia_sic cp) cpg  \n");
        sentencia.append("where \n ");
        sentencia.append("    cp.clave_presupuestaria_id = cf.clave_presupuestaria_id  \n ");
        sentencia.append("    and cp.id_partida=pp.id_partida \n ");
        sentencia.append("    and cp.id_rel_uea_cve_prog = ruc.id_rel_uea_cve_prog \n ");
        sentencia.append("    and ruc.id_cve_prog=cpg.id_cve_prog and cpg.eliminado = 0 \n ");
        sentencia.append("    and cf.ejercicio = ").append(getControlReg().getEjercicio());
        sentencia.append(" and cf.mes = ").append(mes_act_num).append(" \n ");
        sentencia.append("    and cf.corte = (  \n ");
        sentencia.append("      select max(corte)  \n ");
        sentencia.append("      from rf_tr_control_meses@dblsia_sic \n  ");
        sentencia.append("      where \n  ");
        sentencia.append("        ejercicio = ").append(getControlReg().getEjercicio()).append(" \n ");
        sentencia.append("        and mes = ").append(mes_act_num).append("\n ");
        sentencia.append("      ) \n ");
        sentencia.append("group by cpg.programa, substr(pp.partida,0,1)\n ");
        sentencia.append(")\n ");
        sentencia.append("GROUP BY programa, partida \n");
        sentencia.append(") EOARMIFP  \n");
        sentencia.append("group by cube(programa, capitulo) \n ");
        sentencia.append("having grouping(capitulo)=0  \n ");
        sentencia.append("order by progorder, capitulo \n ");
        sentencia.append(")eoarMIPF \n ");
        sentencia.append("where eoarSIA.progorder=eoarMIPF.progorder and eoarSIA.capitulo=eoarMIPF.capitulo \n ");
        sentencia.append(") eoarSIAFin  \n");
        sentencia.append("where p.clave = eoarSIAFin.progorder and c.clave = eoarSIAFin.capitulo and p.estatus=1) gen \n ");
        sentencia.append("where gen.dacum<>0 or gen.eacum<>0 or gen.facum<>0 or gen.g<>0 or gen.hmipf<>0 or gen.icalc<>0 \n ");
        sentencia.append("and gen.jporcejer<>0 or gen.lcomprom<>0 or gen.msuma<>0 or gen.nporc<>0 or gen.odispon<>0 \n ");

        /*formatos = new Formatos(sentencia.toString(),String.valueOf(nivel), String.valueOf(getEjercicio()), 
         String.valueOf(longitud), String.valueOf(programa), 
         String.valueOf(unidad), String.valueOf(estadoUsa),
         " ", " ", 
         " ", String.valueOf(this.getIdCatalogoCuenta()),
         mesActual,mesAnterior,ejercicioCompara," ",mes_act_num);
         sentenciaImpl.append(formatos.getSentencia());
         System.out.println("EOAR "+sentenciaImpl.toString());*/
        return sentencia;
    }

    public StringBuffer formarSentenciaVAF(String fecha_consolidacion, String iva, String activos) {
        Fecha fechaPeriodo = new Fecha(fecha_consolidacion, "/");
        String mes_act = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodo.getMes() - 1);
        fechaPeriodo.addMeses(-1);
        String mes_ant = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodo.getMes() - 1);//susy      
        StringBuffer sentencia = new StringBuffer();
        String unidadAux = getNivel().equals("1") || getNivel().equals("2") ? "____" : getControlReg().getUniEjecFormateada();
        String estado = getNivel().equals("1") || getNivel().equals("2") || getNivel().equals("3") ? "____" : getControlReg().getAmbEntFormateada();
        String cargoAcum;
        String abonoAcum;
        String mesActCargo;
        String mesActAbono;
        if (mes_act.equals("ENE")) {
            cargoAcum = construirMes(mes_act, "cargo_ini");
            abonoAcum = construirMes(mes_act, "abono_ini");
        } else {
            cargoAcum = construirMes(mes_ant, "cargo_acum");
            abonoAcum = construirMes(mes_ant, "abono_acum");
        }
        mesActCargo = construirMes(mes_act, "cargo");
        mesActAbono = construirMes(mes_act, "abono");


        //--Obtiene las cuentas que son de los cargos mensuales
        sentencia.append("  select distinct (cc.cuenta_mayor_id) cuenta_mayor_id, sum(cc.").append(mesActCargo);
        sentencia.append(") mov_mensual,  \n");
        sentencia.append(" to_number(decode((case when  substr(cc.cuenta_contable,f_posicion_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append(")) in ('21203','11213','61214','12204','12701','52202','11206','11209','12206','52102','52101') then substr(cc.cuenta_contable,f_posicion_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append(")) else  \n");
        sentencia.append(" substr(cc.cuenta_contable,f_posicion_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("))||substr(cc.cuenta_contable,f_posicion_nivel('6',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('6',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append(")) end),'21203','1','11213','2', '61214','5','12204','11','12701','12',  \n");
        sentencia.append(" '412010003','29','52202','30','11206','31','11209','32','12206','34','52102','13','52101','14')) as numero,  \n");
        sentencia.append(" (decode((case when  substr(cc.cuenta_contable,f_posicion_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append(")) in ('21203','11213','61214','12204','12701','52202','11206','11209','12206','52102','52101') then substr(cc.cuenta_contable,f_posicion_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("),f_longitud_nivel('mayor',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio());
        sentencia.append(")) else  \n");
        sentencia.append(" substr(cc.cuenta_contable,f_posicion_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("),f_longitud_nivel('mayor',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio());
        sentencia.append("))||substr(cc.cuenta_contable,f_posicion_nivel('6',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('6',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append(")) end),'21203','21203 PROVEEDORES','11213','11213 ANTICIPO A PROVEEDORES (CAP. 5000 SIN IVA)','61214','61214 PRESUPUESTO EJERCIDO', \n");
        sentencia.append(" '12204','12204 COLECCIONES CIENTFICAS, ARcc. Y LITERARIAS','12701','12701 ACTIVOS EN CONTRATO DE COMODATO',  \n");
        sentencia.append(" '412010003','41201 DECREMENTOS AL PATRIMONIO (SUBCTA. 3 DONACIONES)','52202','52202 P�RDIDAS DIVERSAS', '11206','11206 DEUDORES DIVERSOS','11209','11209 CR�DITOS SUJETOS A RESOLUCI�N JUDICIAL','12206','12206 REMESAS DE BIENES MUEBLES', \n");
        sentencia.append("          '52102','52102 COSTO DE OPER. A�OS ANcc. (SUB. 5000 DIF. POR REDONDEO)','52101','52101 COSTO DE OPER. DE PROG. (SUB. 5000 DIF. POR REDONDEO)')) as desRep,  \n");
        sentencia.append(" (decode(substr(cc.cuenta_contable,f_posicion_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append(")),'21203','PAGOS','11213','PAGOS','61214','PAGOS', '12204','ALTAS','12701','ALTAS', \n");
        sentencia.append(" '41201','BAJAS','52202','BAJAS','11206','BAJAS','11209','BAJAS','12206','BAJAS','52102','ALTAS','52101','ALTAS')) as Tipo  \n");
        sentencia.append(" from rf_tr_cuentas_contables cc, rf_tc_clasificador_cuentas tc  \n");
        sentencia.append(" where cc.cuenta_mayor_id = tc.cuenta_mayor_id and  \n");
        sentencia.append(" ( (cc.cuenta_contable like '212030001").append(unidadAux).append(estado);
        sentencia.append("%' and cc.nivel = 5 and substr(cc.cuenta_contable,f_posicion_nivel('5',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('5',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("))<>'0005') or  \n");
        sentencia.append("  (cc.cuenta_contable like '112130001").append(unidadAux).append(estado).append("5000%' and cc.nivel = 5) or  \n");
        sentencia.append("  (cc.cuenta_contable like '612140001").append(unidadAux).append(estado).append("5000%' and cc.nivel = 5) or  \n");
        sentencia.append("  (cc.cuenta_contable like '122040001").append(unidadAux).append(estado).append("%' and cc.nivel = 4) or  \n");
        sentencia.append("  (cc.cuenta_contable like '127010001").append(unidadAux).append(estado).append("%' and cc.nivel = 4) or   \n");
        sentencia.append("  (cc.cuenta_contable like '412010001").append(unidadAux).append(estado).append("5___0003%' and cc.nivel = 6) or \n");
        sentencia.append("  (cc.cuenta_contable like '522020001").append(unidadAux).append(estado).append("5___%' and cc.nivel = 5) or  \n");
        sentencia.append("  (cc.cuenta_contable like '112060001").append(unidadAux).append(estado).append("5___%' and cc.nivel = 5) or  \n");
        sentencia.append("  (cc.cuenta_contable like '112090001").append(unidadAux).append(estado).append("5___%' and cc.nivel = 5) or  \n");
        sentencia.append("  (cc.cuenta_contable like '122060001").append(unidadAux).append(estado).append("%' and cc.nivel = 4)  or  \n");
        sentencia.append("  (cc.cuenta_contable like '521020001").append(unidadAux).append(estado).append("5000%' and cc.nivel = 5) or  \n");
        sentencia.append("  (cc.cuenta_contable like '521010001").append(unidadAux).append(estado).append("5000%' and cc.nivel = 5)  \n");
        sentencia.append("  )   \n");
        sentencia.append("  and cc.id_catalogo_cuenta=").append(getControlReg().getIdCatalogoCuenta()).append("\n");
        sentencia.append("  and extract(year from cc.fecha_vig_ini)=").append(getControlReg().getEjercicio()).append("\n");
        sentencia.append("  group by cc.cuenta_mayor_id,   \n");
        sentencia.append("  to_number(decode((case when  substr(cc.cuenta_contable,f_posicion_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append(")) in ('21203','11213','61214','12204','12701','52202','11206','11209','12206','52102','52101') then substr(cc.cuenta_contable,f_posicion_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append(")) else  \n");
        sentencia.append("  substr(cc.cuenta_contable,f_posicion_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("))||substr(cc.cuenta_contable,f_posicion_nivel('6',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('6',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append(")) end),'21203','1','11213','2', '61214','5','12204','11','12701','12',  \n");
        sentencia.append("  '412010003','29','52202','30','11206','31','11209','32','12206','34','52102','13','52101','14')), \n");
        sentencia.append("  (decode((case when  substr(cc.cuenta_contable,f_posicion_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append(")) in ('21203','11213','61214','12204','12701','52202','11206','11209','12206','52102','52101') then substr(cc.cuenta_contable,f_posicion_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append(")) else  \n");
        sentencia.append("  substr(cc.cuenta_contable,f_posicion_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("))||substr(cc.cuenta_contable,f_posicion_nivel('6',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('6',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append(")) end),'21203','21203 PROVEEDORES','11213','11213 ANTICIPO A PROVEEDORES (CAP. 5000 SIN IVA)','61214','61214 PRESUPUESTO EJERCIDO', \n");
        sentencia.append("  '12204','12204 COLECCIONES CIENTFICAS, ARcc. Y LITERARIAS','12701','12701 ACTIVOS EN CONTRATO DE COMODATO',  \n");
        sentencia.append("  '412010003','41201 DECREMENTOS AL PATRIMONIO (SUBCTA. 3 DONACIONES)','52202','52202 P�RDIDAS DIVERSAS', '11206','11206 DEUDORES DIVERSOS','11209','11209 CR�DITOS SUJETOS A RESOLUCI�N JUDICIAL','12206','12206 REMESAS DE BIENES MUEBLES', \n");
        sentencia.append("  '52102','52102 COSTO DE OPER. A�OS ANcc. (SUB. 5000 DIF. POR REDONDEO)','52101','52101 COSTO DE OPER. DE PROG. (SUB. 5000 DIF. POR REDONDEO)')),  \n");
        sentencia.append("  (decode(substr(cc.cuenta_contable,f_posicion_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append(")),'21203','PAGOS','11213','PAGOS','61214','PAGOS','12204','ALTAS','12701','ALTAS', \n");
        sentencia.append("  '41201','BAJAS','52202','BAJAS','11206','BAJAS','11209','BAJAS','12206','BAJAS','52102','ALTAS','52101','ALTAS'))  \n");
        sentencia.append("  UNION      \n");
        // --Obtiene las cuenta duplicada para la parte de bajas,cargos mensuales
        sentencia.append("  select distinct (cc.cuenta_mayor_id) cuenta_mayor_id, sum(cc.").append(mesActCargo).append(") mov_mensual,  \n");
        sentencia.append("  decode(substr(cc.cuenta_contable,f_posicion_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append(")),'12701',33,'52101',14) numero, decode(substr(cc.cuenta_contable,f_posicion_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append(")),'12701','12701 ACTIVOS EN CONTRATO DE COMODATO','52101','52101 COSTO DE OPER. DE PROG. (IVA CAP. 5000)') as desRep,  \n");
        sentencia.append("  decode(substr(cc.cuenta_contable,f_posicion_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append(")),'12701','BAJAS','52101','ALTAS') as Tipo \n");
        sentencia.append("  from rf_tr_cuentas_contables cc, rf_tc_clasificador_cuentas tc  \n");
        sentencia.append("  where cc.cuenta_mayor_id = tc.cuenta_mayor_id and  \n");
        sentencia.append("  ((cc.cuenta_contable like '127010001").append(unidadAux).append(estado).append("%' and cc.nivel = 4)  or \n");
        sentencia.append("   (cc.cuenta_contable like '521010001").append(unidadAux).append(estado).append("5___%' and cc.nivel = 5 and substr(cc.cuenta_contable,f_posicion_nivel('5',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('5',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("))<>'5000')) \n");
        sentencia.append("  and cc.id_catalogo_cuenta=").append(getControlReg().getIdCatalogoCuenta()).append("\n");
        sentencia.append("  and extract(year from cc.fecha_vig_ini)=").append(getControlReg().getEjercicio()).append("\n");
        sentencia.append("  group by substr(cc.cuenta_contable,f_posicion_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append(")),cc.cuenta_mayor_id\n");
        sentencia.append("  UNION  \n");
        //    --OBTIENE SALDO FINAL DE LA CUENTA 61206
        sentencia.append("  select distinct (cc.cuenta_mayor_id) cuenta_mayor_id, case tc.naturaleza when 'D' then sum((cc.");
        sentencia.append(cargoAcum).append(" + cc.").append(mesActCargo).append(") - (cc.").append(abonoAcum);
        sentencia.append(" + cc.").append(mesActAbono).append(")) else sum((cc.").append(abonoAcum).append(" + cc.");
        sentencia.append(mesActAbono).append(") - (cc.").append(cargoAcum).append(" + cc.").append(mesActCargo);
        sentencia.append(")) end mov_mensual, \n");
        sentencia.append("  6 numero, '61206 PRESUP. EJERC. DEVENG. PEND. DE PAGO' as desRep, 'PAGOS' as Tipo  \n");
        sentencia.append("  from rf_tr_cuentas_contables cc, rf_tc_clasificador_cuentas tc  \n");
        sentencia.append("  where cc.cuenta_mayor_id = tc.cuenta_mayor_id and  \n");
        sentencia.append("  (cc.cuenta_contable like '612060001").append(unidadAux).append(estado).append("5000%' and cc.nivel = 5)   \n");
        sentencia.append("  and cc.id_catalogo_cuenta=").append(getControlReg().getIdCatalogoCuenta()).append("\n");
        sentencia.append("  and extract(year from cc.fecha_vig_ini)=").append(getControlReg().getEjercicio()).append("  \n");
        sentencia.append("  group by cc.cuenta_mayor_id, tc.naturaleza  \n");
        sentencia.append("  UNION  \n");
        //   --Obtiene las cuentas que son de los abonos mensuales
        sentencia.append("  select distinct (cc.cuenta_mayor_id) cuenta_mayor_id, sum(cc.");
        sentencia.append(mesActAbono).append(") mov_mensual,  \n");
        sentencia.append("  to_number(decode((case when substr(cc.cuenta_contable,f_posicion_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append(")) in ('12701','51201','21203') then substr(cc.cuenta_contable,f_posicion_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append(")) else  \n");
        sentencia.append("  substr(cc.cuenta_contable,f_posicion_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("))||substr(cc.cuenta_contable,f_posicion_nivel('6',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('6',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append(")) end),'12701','27',\n");
        sentencia.append("  '412020003','16','412020004','17','51201','18','21203','19')) as numero,  \n");
        sentencia.append("  (decode((case when  substr(cc.cuenta_contable,f_posicion_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append(")) in ('12701','51201','21203') then substr(cc.cuenta_contable,f_posicion_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append(")) else  \n");
        sentencia.append("  substr(cc.cuenta_contable,f_posicion_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("))||substr(cc.cuenta_contable,f_posicion_nivel('6',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('6',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append(")) end),'12701','12701 ACTIVOS EN CONTRATO DE COMODATO',  \n");
        sentencia.append("  '412020003','41202 INCREMENTOS AL PATRIMONIO (SUBCTA. 3 DONACIONES)','412020004','41202 INCREMENTOS AL PATRIMONIO (SUBCTA. 4 ALTAS POR OT)','51201','51201 BENEFICIOS DIVERSOS','21203','21203 PROVEEDORES')) as desRep,  \n");
        sentencia.append("  (decode(substr(cc.cuenta_contable,f_posicion_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append(")),'12701','BAJAS','41202','ALTAS','51201','ALTAS','21203','ALTAS')) as Tipo  \n");
        sentencia.append("  from rf_tr_cuentas_contables cc, rf_tc_clasificador_cuentas tc  \n");
        sentencia.append("  where cc.cuenta_mayor_id = tc.cuenta_mayor_id and  \n");
        sentencia.append("  ( (cc.cuenta_contable like '127010001").append(unidadAux).append(estado).append("%' and cc.nivel = 4) or   \n");
        sentencia.append("  (cc.cuenta_contable like '412020001").append(unidadAux).append(estado).append("___0003%' and cc.nivel = 6) or   \n");
        sentencia.append("  (cc.cuenta_contable like '412020001").append(unidadAux).append(estado).append("___0004%' and cc.nivel = 6) or  \n");
        sentencia.append("  (cc.cuenta_contable like '212030001").append(unidadAux).append(estado).append("%' and cc.nivel = 5 and substr(cc.cuenta_contable,f_posicion_nivel('5',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('5',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("))<>'0005') or \n");
        sentencia.append("  (cc.cuenta_contable like '512010001").append(unidadAux).append(estado).append("%' and cc.nivel = 5)    \n");
        sentencia.append("   )   \n");
        sentencia.append("  and cc.id_catalogo_cuenta=").append(getControlReg().getIdCatalogoCuenta()).append(" \n");
        sentencia.append("  and extract(year from cc.fecha_vig_ini)=").append(getControlReg().getEjercicio()).append("\n");
        sentencia.append("  group by cc.cuenta_mayor_id,   \n");
        sentencia.append("  to_number(decode((case when  substr(cc.cuenta_contable,f_posicion_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append(")) in ('12701','51201','21203') then substr(cc.cuenta_contable,f_posicion_nivel('mayor',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('mayor',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append(")) else  \n");
        sentencia.append("  substr(cc.cuenta_contable,f_posicion_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("))||substr(cc.cuenta_contable,f_posicion_nivel('6',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('6',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append(")) end),'12701','27',  \n");
        sentencia.append("  '412020003','16','412020004','17','51201','18','21203','19')),  \n");
        sentencia.append("  (decode((case when  substr(cc.cuenta_contable,f_posicion_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append(")) in ('12701','51201','21203') then substr(cc.cuenta_contable,f_posicion_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append(")) else  \n");
        sentencia.append("  substr(cc.cuenta_contable,f_posicion_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("))||substr(cc.cuenta_contable,f_posicion_nivel('6',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('6',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append(")) end),'12701','12701 ACTIVOS EN CONTRATO DE COMODATO',  \n");
        sentencia.append("  '412020003','41202 INCREMENTOS AL PATRIMONIO (SUBCTA. 3 DONACIONES)','412020004','41202 INCREMENTOS AL PATRIMONIO (SUBCTA. 4 ALTAS POR OT)','51201','51201 BENEFICIOS DIVERSOS','21203','21203 PROVEEDORES')), \n");
        sentencia.append("  (decode(substr(cc.cuenta_contable,f_posicion_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append(")),'12701','BAJAS','41202','ALTAS','51201','ALTAS','21203','ALTAS'))   \n");
        sentencia.append("  UNION  \n");
        // --Suma Cargos de la 8+9+10+11+12+13+14
        sentencia.append("   select distinct 2 cuenta_mayor_id, nvl(A.mov_mensual,0)+nvl(B.mov_mensual,0)+nvl(C.mov_mensual,0) as   mov_mensual,  \n");
        sentencia.append("  15 as numero,  \n");
        sentencia.append("  'SUMA' as desRep,  \n");
        sentencia.append("  'ALTAS' as Tipo  \n");
        sentencia.append("  from ( \n");
        sentencia.append("  select distinct nvl(sum(cc.").append(mesActCargo).append("),0) mov_mensual   \n");
        sentencia.append("  from rf_tr_cuentas_contables cc, rf_tc_clasificador_cuentas tc  \n");
        sentencia.append("  where cc.cuenta_mayor_id = tc.cuenta_mayor_id and  \n");
        sentencia.append("  ((cc.cuenta_contable like '122040001").append(unidadAux).append(estado).append("%' and cc.nivel = 4) or  \n");
        sentencia.append("  (cc.cuenta_contable like '127010001").append(unidadAux).append(estado).append("%' and cc.nivel = 4) or  \n");
        sentencia.append("  (cc.cuenta_contable like '521020001").append(unidadAux).append(estado).append("5000%' and cc.nivel = 5) or  \n");
        sentencia.append("  (cc.cuenta_contable like '521010001").append(unidadAux).append(estado).append("5___%' and cc.nivel = 5)  \n");
        sentencia.append("  )   \n");
        sentencia.append("  and cc.id_catalogo_cuenta=").append(getControlReg().getIdCatalogoCuenta()).append(" \n");
        sentencia.append("  and extract(year from cc.fecha_vig_ini)=").append(getControlReg().getEjercicio()).append("\n");
        sentencia.append(") A,   \n");
        sentencia.append("(select  sum(cc.").append(mesActAbono).append(") as mov_mensual \n");
        sentencia.append(" from rf_tr_cuentas_contables cc, rf_tc_clasificador_cuentas tc  \n");
        sentencia.append(" where cc.cuenta_mayor_id = tc.cuenta_mayor_id and  \n");
        sentencia.append(" (  \n");
        sentencia.append("   (cc.cuenta_contable like '412020001").append(unidadAux).append(estado);
        sentencia.append("%' and substr(cc.cuenta_contable,f_posicion_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("))<>'0100'\n");
        sentencia.append("and substr(cc.cuenta_contable,f_posicion_nivel('5',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),2) in (51,52)\n");
        sentencia.append("and substr(cc.cuenta_contable,f_posicion_nivel('6',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('6',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("))='0004'  and cc.nivel = 6)  or \n");
        sentencia.append("   (cc.cuenta_contable like '412020001").append(unidadAux).append(estado);
        sentencia.append("%' and substr(cc.cuenta_contable,f_posicion_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("))<>'0100'\n");
        sentencia.append("and substr(cc.cuenta_contable,f_posicion_nivel('5',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),2) in (54)\n");
        sentencia.append("and  substr(cc.cuenta_contable,f_posicion_nivel('6',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('6',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("))='0004'  and cc.nivel = 6)   or \n");
        sentencia.append("   (cc.cuenta_contable like '412020001").append(unidadAux).append(estado);
        sentencia.append("%' and substr(cc.cuenta_contable,f_posicion_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("))<>'0100' and substr(cc.cuenta_contable,f_posicion_nivel('5',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("),2) in (53,55,56,57,58,59) and substr(cc.cuenta_contable,f_posicion_nivel('6',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('6',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("))='0004'  and cc.nivel = 6)\n");
        sentencia.append("  )   \n");
        sentencia.append("  and cc.id_catalogo_cuenta=").append(getControlReg().getIdCatalogoCuenta()).append("\n");
        sentencia.append("  and extract(year from cc.fecha_vig_ini)=").append(getControlReg().getEjercicio()).append("\n");
        sentencia.append(") B,   \n");
        sentencia.append("( \n");
        sentencia.append("select  nvl(A.mov_mensual,0)-nvl(B.mov_mensual,0) as mov_mensual \n");
        sentencia.append(" from  \n");
        sentencia.append(" (select  sum(cc.").append(mesActCargo).append(") as mov_mensual \n");
        sentencia.append("  from rf_tr_cuentas_contables cc, rf_tc_clasificador_cuentas tc  \n");
        sentencia.append("  where cc.cuenta_mayor_id = tc.cuenta_mayor_id and  \n");
        sentencia.append("  ((cc.cuenta_contable like '122010001").append(unidadAux).append(estado);
        sentencia.append("%' and cc.nivel = 5 and substr(cc.cuenta_contable,f_posicion_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("))<>'0100') or \n");
        sentencia.append("   (cc.cuenta_contable like '122020001").append(unidadAux).append(estado);
        sentencia.append("%' and cc.nivel = 5 and substr(cc.cuenta_contable,f_posicion_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("))<>'0100') or \n");
        sentencia.append("   (cc.cuenta_contable like '122030001").append(unidadAux).append(estado);
        sentencia.append("%' and cc.nivel = 5 and substr(cc.cuenta_contable,f_posicion_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("))<>'0100')  \n");
        sentencia.append("  ) \n");
        sentencia.append("  and cc.id_catalogo_cuenta=").append(getControlReg().getIdCatalogoCuenta()).append("\n");
        sentencia.append("  and extract(year from cc.fecha_vig_ini)=").append(getControlReg().getEjercicio()).append("\n");
        sentencia.append("  ) A,  \n");
        sentencia.append("(select  sum(cc.").append(mesActAbono).append(") as mov_mensual \n");
        sentencia.append("  from rf_tr_cuentas_contables cc, rf_tc_clasificador_cuentas tc  \n");
        sentencia.append("  where cc.cuenta_mayor_id = tc.cuenta_mayor_id and  \n");
        sentencia.append("  (  \n");
        sentencia.append("   (cc.cuenta_contable like '412020001").append(unidadAux).append(estado);
        sentencia.append("%' and substr(cc.cuenta_contable,f_posicion_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("))<>'0100' and substr(cc.cuenta_contable,f_posicion_nivel('5',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("),2) in (51,52) and  substr(cc.cuenta_contable,f_posicion_nivel('6',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('6',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("))='0004'  and cc.nivel = 6)  or \n");
        sentencia.append("   (cc.cuenta_contable like '412020001").append(unidadAux).append(estado);
        sentencia.append("%' and substr(cc.cuenta_contable,f_posicion_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("))<>'0100' and substr(cc.cuenta_contable,f_posicion_nivel('5',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("),2) in (54) and  substr(cc.cuenta_contable,f_posicion_nivel('6',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('6',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("))='0004'  and cc.nivel = 6)   or \n");
        sentencia.append("   (cc.cuenta_contable like '412020001").append(unidadAux).append(estado);
        sentencia.append("%' and substr(cc.cuenta_contable,f_posicion_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("))<>'0100' and substr(cc.cuenta_contable,f_posicion_nivel('5',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("),2) in (53,55,56,57,58,59) and substr(cc.cuenta_contable,f_posicion_nivel('6',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('6',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("))='0004'  and cc.nivel = 6)   \n");
        sentencia.append("  ) \n");
        sentencia.append("  and cc.id_catalogo_cuenta=").append(getControlReg().getIdCatalogoCuenta()).append("\n");
        sentencia.append("  and extract(year from cc.fecha_vig_ini)=").append(getControlReg().getEjercicio()).append("\n");
        sentencia.append("  ) B \n");
        sentencia.append(")C \n");
        sentencia.append("  UNION  \n");
        //  --Suma Abonos de la 24,25,26,27
        sentencia.append("   select distinct 2 cuenta_mayor_id, nvl(A.mov_mensual,0)+nvl(B.mov_mensual,0)+nvl(C.mov_mensual,0) as   mov_mensual,  \n");
        sentencia.append("  28 as numero,  \n");
        sentencia.append("  'SUMA' as desRep,  \n");
        sentencia.append("  'BAJAS' as Tipo  \n");
        sentencia.append("  from ( \n");
        sentencia.append("  select distinct nvl(sum(cc.").append(mesActAbono).append("),0) mov_mensual   \n");
        sentencia.append("  from rf_tr_cuentas_contables cc, rf_tc_clasificador_cuentas tc  \n");
        sentencia.append("  where cc.cuenta_mayor_id = tc.cuenta_mayor_id and  \n");
        sentencia.append("  (   (cc.cuenta_contable like '127010001").append(getControlReg().getUniEjecFormateada());
        sentencia.append("").append(getControlReg().getAmbEntFormateada()).append("%' and cc.nivel = 4)   \n");
        sentencia.append("  )   \n");
        sentencia.append("  and cc.id_catalogo_cuenta=").append(getControlReg().getIdCatalogoCuenta()).append("  \n");
        sentencia.append("  and extract(year from cc.fecha_vig_ini)=").append(getControlReg().getEjercicio()).append("\n");
        sentencia.append(") A,   \n");
        sentencia.append("(select  sum(cc.").append(mesActCargo).append(") as mov_mensual \n");
        sentencia.append(" from rf_tr_cuentas_contables cc, rf_tc_clasificador_cuentas tc  \n");
        sentencia.append(" where cc.cuenta_mayor_id = tc.cuenta_mayor_id and  \n");
        sentencia.append(" (  \n");
        sentencia.append("   (cc.cuenta_contable like '412010001").append(unidadAux).append(estado);
        sentencia.append("%' and substr(cc.cuenta_contable,f_posicion_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("))<>'0100' and substr(cc.cuenta_contable,f_posicion_nivel('5',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("),2) in (51,52) and  substr(cc.cuenta_contable,f_posicion_nivel('6',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('6',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("))='0004' and cc.nivel = 6)  or \n");
        sentencia.append("   (cc.cuenta_contable like '412010001").append(unidadAux).append(estado);
        sentencia.append("%' and substr(cc.cuenta_contable,f_posicion_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("))<>'0100' and substr(cc.cuenta_contable,f_posicion_nivel('5',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("),2) in (54) and  substr(cc.cuenta_contable,f_posicion_nivel('6',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('6',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("))='0004'  and cc.nivel = 6)   or \n");
        sentencia.append("   (cc.cuenta_contable like '412010001").append(unidadAux).append(estado);
        sentencia.append("%' and substr(cc.cuenta_contable,f_posicion_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("))<>'0100' and substr(cc.cuenta_contable,f_posicion_nivel('5',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("),2) in (53,55,56,57,58,59) and  substr(cc.cuenta_contable,f_posicion_nivel('6',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('6',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("))='0004'  and cc.nivel = 6)   \n");
        sentencia.append("  )   \n");
        sentencia.append("  and cc.id_catalogo_cuenta=").append(getControlReg().getIdCatalogoCuenta()).append("\n");
        sentencia.append("  and extract(year from cc.fecha_vig_ini)=").append(getControlReg().getEjercicio()).append("\n");
        sentencia.append(") B,   \n");
        sentencia.append("( \n");
        sentencia.append("select  nvl(A.mov_mensual,0)-nvl(B.mov_mensual,0) as mov_mensual \n");
        sentencia.append(" from  \n");
        sentencia.append(" (select  sum(cc.").append(mesActAbono).append(") as mov_mensual \n");
        sentencia.append("  from rf_tr_cuentas_contables cc, rf_tc_clasificador_cuentas tc  \n");
        sentencia.append("  where cc.cuenta_mayor_id = tc.cuenta_mayor_id and  \n");
        sentencia.append("  ((cc.cuenta_contable like '122010001").append(unidadAux).append(estado);
        sentencia.append("%' and cc.nivel = 5 and substr(cc.cuenta_contable,f_posicion_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("))<>'0100') or \n");
        sentencia.append("   (cc.cuenta_contable like '122020001").append(unidadAux).append(estado);
        sentencia.append("%' and cc.nivel = 5 and substr(cc.cuenta_contable,f_posicion_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("))<>'0100') or \n");
        sentencia.append("   (cc.cuenta_contable like '122030001").append(unidadAux).append(estado);
        sentencia.append("%' and cc.nivel = 5 and substr(cc.cuenta_contable,f_posicion_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("))<>'0100')  \n");
        sentencia.append("  ) \n");
        sentencia.append("  and cc.id_catalogo_cuenta=").append(getControlReg().getIdCatalogoCuenta()).append("\n");
        sentencia.append("  and extract(year from cc.fecha_vig_ini)=").append(getControlReg().getEjercicio()).append("\n");
        sentencia.append("  ) A,  \n");
        sentencia.append("(select  sum(cc.").append(mesActCargo).append(") as mov_mensual \n");
        sentencia.append("  from rf_tr_cuentas_contables cc, rf_tc_clasificador_cuentas tc  \n");
        sentencia.append("  where cc.cuenta_mayor_id = tc.cuenta_mayor_id and  \n");
        sentencia.append("  (  \n");
        sentencia.append("   (cc.cuenta_contable like '412010001").append(unidadAux).append(estado);
        sentencia.append("%' and substr(cc.cuenta_contable,f_posicion_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("))<>'0100' and substr(cc.cuenta_contable,f_posicion_nivel('5',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("),2) in (51,52) and  substr(cc.cuenta_contable,f_posicion_nivel('6',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('6',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("))='0004'  and cc.nivel = 6)  or \n");
        sentencia.append("   (cc.cuenta_contable like '412010001").append(unidadAux).append(estado);
        sentencia.append("%'and substr(cc.cuenta_contable,f_posicion_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("))<>'0100' and substr(cc.cuenta_contable,f_posicion_nivel('5',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("),2) in (54) and  substr(cc.cuenta_contable,f_posicion_nivel('6',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('6',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("))='0004'  and cc.nivel = 6)   or \n");
        sentencia.append("   (cc.cuenta_contable like '412010001").append(unidadAux).append(estado);
        sentencia.append("%' and substr(cc.cuenta_contable,f_posicion_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("))<>'0100' and substr(cc.cuenta_contable,f_posicion_nivel('5',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("),2) in (53,55,56,57,58,59) and substr(cc.cuenta_contable,f_posicion_nivel('6',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('6',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("))='0004'  and cc.nivel = 6)   \n");
        sentencia.append("  ) \n");
        sentencia.append("  and cc.id_catalogo_cuenta=").append(getControlReg().getIdCatalogoCuenta()).append("\n");
        sentencia.append("  and extract(year from cc.fecha_vig_ini)=").append(getControlReg().getEjercicio()).append("\n");
        sentencia.append("  ) B \n");
        sentencia.append(")C \n");
        sentencia.append("  UNION  \n");
        //  --Suma Cargos de la 29+30+31+32+33+34
        sentencia.append("   select distinct 2 cuenta_mayor_id, sum(cc.").append(mesActCargo).append(") mov_mensual,  \n");
        sentencia.append("  35 as numero,  \n");
        sentencia.append("  'SUMA' as desRep,  \n");
        sentencia.append("  'BAJAS' as Tipo  \n");
        sentencia.append("  from rf_tr_cuentas_contables cc, rf_tc_clasificador_cuentas tc  \n");
        sentencia.append("  where cc.cuenta_mayor_id = tc.cuenta_mayor_id and      \n");
        sentencia.append("  ((cc.cuenta_contable like '412010001").append(unidadAux).append(estado).append("5___0003%' and cc.nivel = 6) or  \n");
        sentencia.append("  (cc.cuenta_contable like '522020001").append(unidadAux).append(estado).append("5___%' and cc.nivel = 5) or  \n");
        sentencia.append("  (cc.cuenta_contable like '112060001").append(unidadAux).append(estado).append("5___%' and cc.nivel = 5) or  \n");
        sentencia.append("  (cc.cuenta_contable like '112090001").append(unidadAux).append(estado).append("5___%' and cc.nivel = 5) or  \n");
        sentencia.append("  (cc.cuenta_contable like '127010001").append(unidadAux).append(estado).append("%' and cc.nivel = 4) or    \n");
        sentencia.append("  (cc.cuenta_contable like '122060001").append(unidadAux).append(estado).append("%' and cc.nivel = 4)  \n");
        sentencia.append("  )      \n");
        sentencia.append("  and cc.id_catalogo_cuenta=").append(getControlReg().getIdCatalogoCuenta()).append("\n");
        sentencia.append("  and extract(year from cc.fecha_vig_ini)=").append(getControlReg().getEjercicio()).append("\n");
        sentencia.append("  UNION  \n");
        // --Captura de Pagos
        sentencia.append("  select distinct 1 cuenta_mayor_id,  to_number(").append(iva).append(") mov_mensual,  \n");
        sentencia.append("  3 as numero,  \n");
        sentencia.append("  '11213 ANTICIPO A PROVEEDORES (IVA CAP�TULO 5000)' as desRep,  \n");
        sentencia.append("  'PAGOS' as Tipo  \n");
        sentencia.append("  from dual  \n");
        sentencia.append("  UNION  \n");
        //  --Suma de Pagos 1+2+3
        sentencia.append("  select distinct 2 cuenta_mayor_id, sum(cc.").append(mesActCargo).append(")+   to_number(");
        sentencia.append(iva).append(") mov_mensual,  \n");
        sentencia.append("  4 as numero,  \n");
        sentencia.append("  'SUMA DE PAGOS' as desRep,  \n");
        sentencia.append("  'PAGOS' as Tipo  \n");
        sentencia.append("  from rf_tr_cuentas_contables cc, rf_tc_clasificador_cuentas tc  \n");
        sentencia.append("  where cc.cuenta_mayor_id = tc.cuenta_mayor_id and  \n");
        sentencia.append("  (  (cc.cuenta_contable like '212030001").append(unidadAux).append(estado);
        sentencia.append("%' and cc.nivel = 5 and substr(cc.cuenta_contable,f_posicion_nivel('5',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('5',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("))<>'0005') or  \n");
        sentencia.append("   (cc.cuenta_contable like '112130001").append(unidadAux).append(estado).append("5000%' and cc.nivel = 5)   \n");
        sentencia.append("   )   \n");
        sentencia.append("  and cc.id_catalogo_cuenta=").append(getControlReg().getIdCatalogoCuenta()).append("\n");
        sentencia.append("  and extract(year from cc.fecha_vig_ini)=").append(getControlReg().getEjercicio()).append("\n");
        sentencia.append("  UNION  \n");
        //  --Captura de Altas
        sentencia.append("  select distinct 3 cuenta_mayor_id,  to_number(").append(activos).append(")  mov_mensual,  \n");
        sentencia.append("  20 as numero,  \n");
        sentencia.append("  '**ACTIVOS EN CONTRATO DE COMODATO' as desRep,  \n");
        sentencia.append("  'ALTAS' as Tipo  \n");
        sentencia.append("  from dual  \n");
        sentencia.append("  UNION  \n");
        //   --Suma de Altas
        sentencia.append("  select distinct 2 cuenta_mayor_id, sum(cc.").append(mesActAbono).append(")+ to_number(");
        sentencia.append(activos).append(")   mov_mensual,  \n");
        sentencia.append("  22 as numero,  \n");
        sentencia.append("  'SUMA' as desRep,  \n");
        sentencia.append("  'ALTAS' as Tipo  \n");
        sentencia.append("  from rf_tr_cuentas_contables cc, rf_tc_clasificador_cuentas tc  \n");
        sentencia.append("  where cc.cuenta_mayor_id = tc.cuenta_mayor_id and  \n");
        sentencia.append("  ( (cc.cuenta_contable like '412020001").append(unidadAux).append(estado);
        sentencia.append("5___0003%' and cc.nivel = 6) or    \n");
        sentencia.append("    (cc.cuenta_contable like '412020001").append(unidadAux).append(estado);
        sentencia.append("5___0004%' and cc.nivel = 6) or  \n");
        sentencia.append("    (cc.cuenta_contable like '212030001").append(unidadAux).append(estado);
        sentencia.append("%' and cc.nivel = 5 and substr(cc.cuenta_contable,f_posicion_nivel('5',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('5',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("))<>'0005') or \n");
        sentencia.append("    (cc.cuenta_contable like '512010001").append(unidadAux).append(estado);
        sentencia.append("5%' and cc.nivel = 5)     \n");
        sentencia.append("   )    \n");
        sentencia.append("  and cc.id_catalogo_cuenta=").append(getControlReg().getIdCatalogoCuenta()).append("\n");
        sentencia.append("  and extract(year from cc.fecha_vig_ini)=").append(getControlReg().getEjercicio()).append("\n");
        sentencia.append("UNION \n");
        //-- C�lculo de la cuenta 12201,12202,12203  OT - Altas
        sentencia.append("select distinct (cc.cuenta_mayor_id) cuenta_mayor_id, sum(cc.").append(mesActAbono).append(") as mov_mensual, \n");
        sentencia.append(" (case when substr(cc.cuenta_contable,f_posicion_nivel('5',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("),2) in ('51','52') then 8 else (case when substr(cc.cuenta_contable,f_posicion_nivel('5',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),2) in ('54') then 9 else 10 end )end) as numero,  \n");
        sentencia.append(" case when substr(cc.cuenta_contable,f_posicion_nivel('5',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("),2) in ('51','52') then '12201 MOBILIARIO Y EQUIPO (ORDENES DE TRASPASO)' else (case when substr(cc.cuenta_contable,f_posicion_nivel('5',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("),2) in ('54') then '12202 VEHÍCULOS TERRESTRES MARÍTIMOS Y AÉREOS (ORDENES DE TRASPASO)' else '12203 MAQUINARIA, HERRAMIENTAS Y APARATOS (ORDENES DE TRASPASO)' end) end as desRep,  \n");
        sentencia.append(" 'ALTAS' as Tipo  \n");
        sentencia.append(" from rf_tr_cuentas_contables cc, rf_tc_clasificador_cuentas tc  \n");
        sentencia.append(" where cc.cuenta_mayor_id = tc.cuenta_mayor_id and  \n");
        sentencia.append(" (  \n");
        sentencia.append("   (cc.cuenta_contable like '412020001").append(unidadAux).append(estado);
        sentencia.append("%' and substr(cc.cuenta_contable,f_posicion_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("))<>'0100' and substr(cc.cuenta_contable,f_posicion_nivel('5',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("),2) in (51,52) and substr(cc.cuenta_contable,f_posicion_nivel('6',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('6',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("))='0004'  and cc.nivel = 6)  or \n");
        sentencia.append("   (cc.cuenta_contable like '412020001").append(unidadAux).append(estado);
        sentencia.append("%' and substr(cc.cuenta_contable,f_posicion_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("))<>'0100' and substr(cc.cuenta_contable,f_posicion_nivel('5',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("),2) in (54) and substr(cc.cuenta_contable,f_posicion_nivel('6',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('6',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("))='0004'  and cc.nivel = 6)   or \n");
        sentencia.append("   (cc.cuenta_contable like '412020001").append(unidadAux).append(estado);
        sentencia.append("%' and substr(cc.cuenta_contable,f_posicion_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("))<>'0100' and substr(cc.cuenta_contable,f_posicion_nivel('5',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("),2) in (53,55,56,57,58,59) and substr(cc.cuenta_contable,f_posicion_nivel('6',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('6',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("))='0004'  and cc.nivel = 6)   \n");
        sentencia.append("  )   \n");
        sentencia.append("  and cc.id_catalogo_cuenta=").append(getControlReg().getIdCatalogoCuenta()).append("\n");
        sentencia.append("  and extract(year from cc.fecha_vig_ini)=").append(getControlReg().getEjercicio()).append("\n");
        sentencia.append("  group by cc.cuenta_mayor_id,  (case when substr(cc.cuenta_contable,f_posicion_nivel('5',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("),2) in ('51','52') then 8 else (case when substr(cc.cuenta_contable,f_posicion_nivel('5',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),2) in ('54') then 9 else 10 end )end), \n");
        sentencia.append("  case when substr(cc.cuenta_contable,f_posicion_nivel('5',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("),2) in ('51','52') then '12201 MOBILIARIO Y EQUIPO (ORDENES DE TRASPASO)' else (case when substr(cc.cuenta_contable,f_posicion_nivel('5',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("),2) in ('54') then '12202 VEHÍCULOS TERRESTRES MARÍTIMOS Y AÉREOS (ORDENES DE TRASPASO)' else '12203 MAQUINARIA, HERRAMIENTAS Y APARATOS (ORDENES DE TRASPASO)' end) end \n");
        sentencia.append("UNION \n");
        //-- C�lculo de la cuenta 12201,12202,12203  Adquisiones - Altas
        sentencia.append("select a.cuenta_mayor_id, nvl(A.mov_mensual,0)-nvl(B.mov_mensual,0) as mov_mensual, \n");
        sentencia.append(" (case when a.cuenta='12201' then 8 else (case when a.cuenta='12202' then 9 else 10 end )end) as numero,  \n");
        sentencia.append(" (case when a.cuenta='12201' then '12201 MOBILIARIO Y EQUIPO (ADQUISICIONES)'  else (case when a.cuenta='12202' then '12202 VEH�CULOS TERRESTRES MAR�TIMOS Y A�REOS (ADQUISICIONES)'  else '12203 MAQUINARIA, HERRAMIENTAS Y APARATOS (ADQUISICIONES)' end )end) as  desRep,  \n");
        sentencia.append(" 'ALTAS' as Tipo  \n");
        sentencia.append(" from  \n");
        sentencia.append(" (select substr(cc.cuenta_contable,f_posicion_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append(")) cuenta,tc.cuenta_mayor_id, sum(cc.");
        sentencia.append(mesActCargo).append(") as mov_mensual \n");
        sentencia.append("  from rf_tr_cuentas_contables cc, rf_tc_clasificador_cuentas tc  \n");
        sentencia.append("  where cc.cuenta_mayor_id = tc.cuenta_mayor_id and  \n");
        sentencia.append("  ((cc.cuenta_contable like '122010001").append(unidadAux).append(estado);
        sentencia.append("%' and cc.nivel = 5 and substr(cc.cuenta_contable,f_posicion_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("))<>'0100') or \n");
        sentencia.append("   (cc.cuenta_contable like '122020001").append(unidadAux).append(estado);
        sentencia.append("%' and cc.nivel = 5 and substr(cc.cuenta_contable,f_posicion_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("))<>'0100') or \n");
        sentencia.append("   (cc.cuenta_contable like '122030001").append(unidadAux).append(estado);
        sentencia.append("%' and cc.nivel = 5 and substr(cc.cuenta_contable,f_posicion_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("))<>'0100')  \n");
        sentencia.append("  ) \n");
        sentencia.append("  and cc.id_catalogo_cuenta=").append(getControlReg().getIdCatalogoCuenta()).append("\n");
        sentencia.append("  and extract(year from cc.fecha_vig_ini)=").append(getControlReg().getEjercicio()).append("\n");
        sentencia.append("  group by substr(cc.cuenta_contable,f_posicion_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append(")),tc.cuenta_mayor_id \n");
        sentencia.append("  ) A,  \n");
        sentencia.append("(select  case when substr(cc.cuenta_contable,f_posicion_nivel('5',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("),2) in ('51','52') then '12201' else (case when substr(cc.cuenta_contable,f_posicion_nivel('5',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),2) in ('54') then '12202' else '12203' end) end cuenta,  \n");
        sentencia.append("  case when substr(cc.cuenta_contable,f_posicion_nivel('5',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("),2) in ('51','52') then '246' else (case when substr(cc.cuenta_contable,f_posicion_nivel('5',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),2) in ('54') then '247' else '248' end) end cuenta_mayor_id,  \n");
        sentencia.append("  sum(cc.").append(mesActAbono).append(") as mov_mensual \n");
        sentencia.append("  from rf_tr_cuentas_contables cc, rf_tc_clasificador_cuentas tc  \n");
        sentencia.append("  where cc.cuenta_mayor_id = tc.cuenta_mayor_id and  \n");
        sentencia.append("  (  \n");
        sentencia.append("   (cc.cuenta_contable like '412020001").append(unidadAux).append(estado);
        sentencia.append("%' and substr(cc.cuenta_contable,f_posicion_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("))<>'0100' " + "and substr(cc.cuenta_contable,f_posicion_nivel('5',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("),2) in (51,52) and  substr(cc.cuenta_contable,f_posicion_nivel('6',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("))='0004'  and cc.nivel = 6)  or \n");
        sentencia.append("   (cc.cuenta_contable like '412020001").append(unidadAux).append(estado);
        sentencia.append("%' and substr(cc.cuenta_contable,f_posicion_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("))<>'0100' and substr(cc.cuenta_contable,f_posicion_nivel('5',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("),2) in (54) and substr(cc.cuenta_contable,f_posicion_nivel('6',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('6',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("))='0004'  and cc.nivel = 6) or \n");
        sentencia.append("   (cc.cuenta_contable like '412020001").append(unidadAux).append(estado);
        sentencia.append("%' and substr(cc.cuenta_contable,f_posicion_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("))<>'0100' and substr(cc.cuenta_contable,f_posicion_nivel('5',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("),2) in (53,55,56,57,58,59) and substr(cc.cuenta_contable,f_posicion_nivel('6',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('6',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("))='0004'  and cc.nivel = 6)   \n");
        sentencia.append("  ) \n");
        sentencia.append("  and cc.id_catalogo_cuenta=").append(getControlReg().getIdCatalogoCuenta()).append("\n");
        sentencia.append("  and extract(year from cc.fecha_vig_ini)=").append(getControlReg().getEjercicio()).append("\n");
        sentencia.append("  group by case when substr(cc.cuenta_contable,f_posicion_nivel('5',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("),2) in ('51','52') then '12201' else (case when substr(cc.cuenta_contable,f_posicion_nivel('5',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),2) in ('54') then '12202' else '12203' end) end,  \n");
        sentencia.append("    case when substr(cc.cuenta_contable,f_posicion_nivel('5',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("),2) in ('51','52') then '246' else (case when substr(cc.cuenta_contable,f_posicion_nivel('5',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),2) in ('54') then '247' else '248' end) end  \n");
        sentencia.append("  ) B \n");
        sentencia.append("where A.cuenta=B.cuenta   \n");
        sentencia.append("UNION \n");
        //-- C�lculo de la cuenta 12201,12202,12203  OT - Bajas
        sentencia.append("select distinct (cc.cuenta_mayor_id) cuenta_mayor_id, sum(cc.");
        sentencia.append(mesActCargo).append(") as mov_mensual, \n");
        sentencia.append(" (case when substr(cc.cuenta_contable,f_posicion_nivel('5',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("),2) in ('51','52') then 24 else (case when substr(cc.cuenta_contable,f_posicion_nivel('5',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),2) in ('54') then 25 else 26 end )end) as numero,  \n");
        sentencia.append(" case when substr(cc.cuenta_contable,f_posicion_nivel('5',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("),2) in ('51','52') then '12201 MOBILIARIO Y EQUIPO (ORDENES DE TRASPASO)' else (case when substr(cc.cuenta_contable,f_posicion_nivel('5',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("),2) in ('54') then '12202 VEH�CULOS TERRESTRES MAR�TIMOS Y A�REOS (ORDENES DE TRASPASO)' else '12203 MAQUINARIA, HERRAMIENTAS Y APARATOS (ORDENES DE TRASPASO)' end) end as desRep,  \n");
        sentencia.append(" 'BAJAS' as Tipo  \n");
        sentencia.append(" from rf_tr_cuentas_contables cc, rf_tc_clasificador_cuentas tc  \n");
        sentencia.append(" where cc.cuenta_mayor_id = tc.cuenta_mayor_id and  \n");
        sentencia.append(" (  \n");
        sentencia.append("   (cc.cuenta_contable like '412010001").append(unidadAux).append(estado);
        sentencia.append("%' and substr(cc.cuenta_contable,f_posicion_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("))<>'0100' and substr(cc.cuenta_contable,f_posicion_nivel('5',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("),2) in (51,52) and  substr(cc.cuenta_contable,f_posicion_nivel('6',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('6',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("))='0004'  and cc.nivel = 6)  or \n");
        sentencia.append("   (cc.cuenta_contable like '412010001").append(unidadAux).append(estado);
        sentencia.append("%' and substr(cc.cuenta_contable,f_posicion_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("))<>'0100' and substr(cc.cuenta_contable,f_posicion_nivel('5',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("),2) in (54) and  substr(cc.cuenta_contable,f_posicion_nivel('6',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('6',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("))='0004'  and cc.nivel = 6)   or \n");
        sentencia.append("   (cc.cuenta_contable like '412010001").append(unidadAux).append(estado);
        sentencia.append("%' and substr(cc.cuenta_contable,f_posicion_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("))<>'0100' and substr(cc.cuenta_contable,f_posicion_nivel('5',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("),2) in (53,55,56,57,58,59) and substr(cc.cuenta_contable,f_posicion_nivel('6',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('6',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("))='0004'  and cc.nivel = 6)   \n");
        sentencia.append("  )   \n");
        sentencia.append("  and cc.id_catalogo_cuenta=").append(getControlReg().getIdCatalogoCuenta()).append("\n");
        sentencia.append("  and extract(year from cc.fecha_vig_ini)=").append(getControlReg().getEjercicio()).append("\n");
        sentencia.append("  group by cc.cuenta_mayor_id,  (case when substr(cc.cuenta_contable,f_posicion_nivel('5',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("),2) in ('51','52') then 24 else (case when substr(cc.cuenta_contable,f_posicion_nivel('5',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),2) in ('54') then 25 else 26 end )end), \n");
        sentencia.append("  case when substr(cc.cuenta_contable,f_posicion_nivel('5',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("),2) in ('51','52') then '12201 MOBILIARIO Y EQUIPO (ORDENES DE TRASPASO)' else (case when substr(cc.cuenta_contable,f_posicion_nivel('5',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("),2) in ('54') then '12202 VEH�CULOS TERRESTRES MAR�TIMOS Y A�REOS (ORDENES DE TRASPASO)' else '12203 MAQUINARIA, HERRAMIENTAS Y APARATOS (ORDENES DE TRASPASO)' end) end \n");
        sentencia.append("UNION \n");
        //-- C�lculo de la cuenta 12201,12202,12203  Adquisiones - Bajas
        sentencia.append("select a.cuenta_mayor_id, nvl(nvl(A.mov_mensual,0)-nvl(B.mov_mensual,0),0) as mov_mensual, \n");
        sentencia.append(" (case when a.cuenta='12201' then 24 else (case when a.cuenta='12202' then 25 else 26 end )end) as numero,  \n");
        sentencia.append(" (case when a.cuenta='12201' then '12201 MOBILIARIO Y EQUIPO'  else (case when a.cuenta='12202' then '12202 VEH�CULOS TERRESTRES MAR�TIMOS Y A�REOS'  else '12203 MAQUINARIA, HERRAMIENTAS Y APARATOS' end )end) as  desRep,  \n");
        sentencia.append(" 'BAJAS' as Tipo  \n");
        sentencia.append(" from  \n");
        sentencia.append(" (select substr(cc.cuenta_contable,f_posicion_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append(")) cuenta,tc.cuenta_mayor_id, sum(cc.");
        sentencia.append(mesActAbono).append(") as mov_mensual \n");
        sentencia.append("  from rf_tr_cuentas_contables cc, rf_tc_clasificador_cuentas tc  \n");
        sentencia.append("  where cc.cuenta_mayor_id = tc.cuenta_mayor_id and  \n");
        sentencia.append("  ((cc.cuenta_contable like '122010001").append(unidadAux).append(estado);
        sentencia.append("%' and cc.nivel = 5 and substr(cc.cuenta_contable,f_posicion_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("))<>'0100') or \n");
        sentencia.append("   (cc.cuenta_contable like '122020001").append(unidadAux).append(estado);
        sentencia.append("%' and cc.nivel = 5 and substr(cc.cuenta_contable,f_posicion_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("))<>'0100') or \n");
        sentencia.append("   (cc.cuenta_contable like '122030001").append(unidadAux).append(estado);
        sentencia.append("%' and cc.nivel = 5 and substr(cc.cuenta_contable,f_posicion_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("))<>'0100')  \n");
        sentencia.append("  ) \n");
        sentencia.append("  and cc.id_catalogo_cuenta=").append(getControlReg().getIdCatalogoCuenta()).append("\n");
        sentencia.append("  and extract(year from cc.fecha_vig_ini)=").append(getControlReg().getEjercicio()).append("\n");
        sentencia.append("  group by substr(cc.cuenta_contable,f_posicion_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('mayor',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append(")),tc.cuenta_mayor_id \n");
        sentencia.append("  ) A,  \n");
        sentencia.append("(select  case when substr(cc.cuenta_contable,f_posicion_nivel('5',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("),2) in ('51','52') then '12201' else (case when substr(cc.cuenta_contable,f_posicion_nivel('5',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),2) in ('54') then '12202' else '12203' end) end cuenta,  \n");
        sentencia.append("  case when substr(cc.cuenta_contable,f_posicion_nivel('5',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("),2) in ('51','52') then '246' else (case when substr(cc.cuenta_contable,f_posicion_nivel('5',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),2) in ('54') then '247' else '248' end) end cuenta_mayor_id,  \n");
        sentencia.append("  sum(cc.").append(mesActCargo).append(") as mov_mensual \n");
        sentencia.append("  from rf_tr_cuentas_contables cc, rf_tc_clasificador_cuentas tc  \n");
        sentencia.append("  where cc.cuenta_mayor_id = tc.cuenta_mayor_id and  \n");
        sentencia.append("  (  \n");
        sentencia.append("   (cc.cuenta_contable like '412010001").append(unidadAux).append(estado);
        sentencia.append("%' and substr(cc.cuenta_contable,f_posicion_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("))<>'0100' and substr(cc.cuenta_contable,f_posicion_nivel('5',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("),2) in (51,52) and substr(cc.cuenta_contable,f_posicion_nivel('6',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('6',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("))='0004'  and cc.nivel = 6)  or \n");
        sentencia.append("   (cc.cuenta_contable like '412010001").append(unidadAux).append(estado);
        sentencia.append("%' and substr(cc.cuenta_contable,f_posicion_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("))<>'0100' and substr(cc.cuenta_contable,f_posicion_nivel('5',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("),2) in (54) and substr(cc.cuenta_contable,f_posicion_nivel('6',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('6',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("))='0004'  and cc.nivel = 6) or \n");
        sentencia.append("   (cc.cuenta_contable like '412010001").append(unidadAux).append(estado);
        sentencia.append("%' and substr(cc.cuenta_contable,f_posicion_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio()).append("),f_longitud_nivel('unidad',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("))<>'0100' and substr(cc.cuenta_contable,f_posicion_nivel('5',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("),2) in (53,55,56,57,58,59) and substr(cc.cuenta_contable,f_posicion_nivel('6',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),f_longitud_nivel('6',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("))='0004'  and cc.nivel = 6)   \n");
        sentencia.append("  ) \n");
        sentencia.append("  and cc.id_catalogo_cuenta=").append(getControlReg().getIdCatalogoCuenta()).append("\n");
        sentencia.append("  and extract(year from cc.fecha_vig_ini)=").append(getControlReg().getEjercicio()).append("\n");
        sentencia.append("  group by case when substr(cc.cuenta_contable,f_posicion_nivel('5',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("),2) in ('51','52') then '12201' else (case when substr(cc.cuenta_contable,f_posicion_nivel('5',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),2) in ('54') then '12202' else '12203' end) end,  \n");
        sentencia.append("    case when substr(cc.cuenta_contable,f_posicion_nivel('5',cc.cuenta_mayor_id,");
        sentencia.append(getControlReg().getEjercicio());
        sentencia.append("),2) in ('51','52') then '246' else (case when substr(cc.cuenta_contable,f_posicion_nivel('5',cc.cuenta_mayor_id,").append(getControlReg().getEjercicio()).append("),2) in ('54') then '247' else '248' end) end  \n");
        sentencia.append("  ) B \n");
        sentencia.append("where A.cuenta=B.cuenta     \n");
        sentencia.append("order by numero \n");

        /*formatos = new Formatos(sentencia.toString(),String.valueOf(nivel), String.valueOf(getEjercicio()), String.valueOf(longitud), programa, 
         unidad, estado, " ", " ", " ", String.valueOf(this.getIdCatalogoCuenta()), mesActual,mesAnterior,ejercicioCompara," ");
         sentenciaImpl.append(formatos.getSentencia());*/
        //  System.oucc.println("VAF "+sentenciaImpl.toString());
        return sentencia;
    }

    public StringBuffer cuentasColectivas(String fecha_consolidacion) {
        StringBuffer sentencia = new StringBuffer();
        //StringBuffer sentenciaImpl = new StringBuffer();
        StringBuilder sentenciaNivel = new StringBuilder();
        //Formatos formatos = null;
        String cargoAcum;
        String abonoAcum;
        String cargo;
        String abono;
        String cadenaPrograma;
        Fecha fechaPeriodo = new Fecha(fecha_consolidacion, "/");
        String mesActual = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodo.getMes() - 1);
        fechaPeriodo.addMeses(-1);
        String mesAnterior = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodo.getMes() - 1);
        setNivel(!getPrograma().equals("'0000'") && getUnidad().equals("0000") && getAmbito().equals("0000") ? "2" : getNivel());
        abono = construirMes(mesActual, "abono");
        cargo = construirMes(mesActual, "cargo");
        if (mesAnterior.equals("DIC")) {
            abonoAcum = construirMes(mesActual, "abono_ini");
            cargoAcum = construirMes(mesActual, "cargo_ini");
        } else {
            abonoAcum = construirMes(mesAnterior, "abono_acum");
            cargoAcum = construirMes(mesAnterior, "cargo_acum");
        }
        /*if(mesActual.equals("ENE")){
         mesAntCargoAcum = mesActual.concat("_cargo_ini");
         mesAntAbonoAcum = mesActual.concat("_abono_ini");
         }
         else{
         mesAntCargoAcum = mes_ant.concat("_cargo_acum");
         mesAntAbonoAcum = mes_ant.concat("_abono_acum");
         }
         mesActCargo = mesActual.concat("_cargo");
         mesActAbono = mesActual.concat("_abono");*/

        if (getPrograma().equals("'0000'")) {
            cadenaPrograma = " and substr(cuenta_contable,6,4) between '0000' and '9999' \n ";
        } else {
            cadenaPrograma = " and substr(cuenta_contable,6,4) in (" + getPrograma() + ") \n ";
        }

        if (Integer.valueOf(getNivel()) == 1 || Integer.valueOf(getNivel()) == 2) {
            sentenciaNivel.append(cadenaPrograma);
            sentenciaNivel.append("and substr(cuenta_contable,10,4) between '0000' and '9999' \n");
            sentenciaNivel.append("and substr(cuenta_contable,14,4) between '0000' and '9999' \n");
        } else {
            if (Integer.valueOf(getNivel()) == 3) {
                sentenciaNivel.append(cadenaPrograma);
                sentenciaNivel.append("and substr(cuenta_contable,10,4) between '").append(getUnidad()).append("' and '").append(getUnidad()).append("' \n");
                sentenciaNivel.append("and substr(cuenta_contable,14,4) between '0000' and '9999'  \n ");
            } else {
                sentenciaNivel.append(cadenaPrograma);
                sentenciaNivel.append("and substr(cuenta_contable,10,4) between '").append(getUnidad()).append("' and '").append(getUnidad()).append("' \n");
                sentenciaNivel.append("and substr(cuenta_contable,14,4) between '").append(getAmbito()).append("' and '").append(getAmbito()).append("' \n ");
            }
        }
        sentencia.append("select * from ( \n ");
        sentencia.append("select \n ");
        sentencia.append("cuenta_contable_id,  \n");
        sentencia.append("cc.cuenta_mayor_id,  \n");
        sentencia.append("cuenta_contable,  \n");
        sentencia.append("cc.descripcion,  \n");
        //sentencia.append("case cla.naturaleza when 'D' then {0}_cargo_acum-{0}_abono_acum else {0}_abono_acum-{0}_cargo_acum end saldo_actual ");
        sentencia.append("case cla.naturaleza when 'D' then  ((  \n");


        sentencia.append("(case when nivel = 1 and '").append(getUnidad()).append("' = '0000' and '").append(getAmbito()).append("' = '0000' then \n");
        sentencia.append("  (select sum(").append(cargoAcum).append(")  from RF_TR_CUENTAS_CONTABLES x where   x.cuenta_mayor_id=cc.cuenta_mayor_id  \n");
        sentencia.append(cadenaPrograma).append(" and  nivel=2 \n");
        sentencia.append("   and  id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta());
        sentencia.append(" and extract(year from x.fecha_vig_ini)=").append(getControlReg().getEjercicio());
        sentencia.append(" group by x.cuenta_mayor_id)  \n");
        sentencia.append(" else      \n");
        sentencia.append("  (case when nivel = 1 and '").append(getAmbito()).append("' = '0000' then \n");
        sentencia.append("    (select sum(").append(cargoAcum).append(")  from RF_TR_CUENTAS_CONTABLES x where   x.cuenta_mayor_id=cc.cuenta_mayor_id  \n");
        sentencia.append(cadenaPrograma);
        sentencia.append("     and substr(cuenta_contable,10,4) between '").append(getUnidad()).append("' and '".concat(getUnidad().equals("0000") ? "9999" : getUnidad()).concat("' and  nivel=3    \n"));
        sentencia.append("     and  id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta());
        sentencia.append(" and extract(year from x.fecha_vig_ini)=").append(getControlReg().getEjercicio());
        sentencia.append(" group by x.cuenta_mayor_id)  \n");
        sentencia.append("   else \n");
        //Calcula cuenta de mayor cuando solo se captura un rango de programas,regional, entidad
        sentencia.append("     (case when nivel = 1 and '").append(getAmbito()).append("' <> '0000' then \n");
        sentencia.append("       (select sum(").append(cargoAcum).append(")  from RF_TR_CUENTAS_CONTABLES x where   x.cuenta_mayor_id=cc.cuenta_mayor_id  \n");
        sentencia.append(cadenaPrograma);
        sentencia.append("        and substr(cuenta_contable,10,4) between '").append(getUnidad()).append("' and '".concat(getUnidad().equals("0000") ? "9999" : getUnidad()).concat("'  \n"));
        sentencia.append("        and substr(cuenta_contable,14,4) between '").append(getAmbito()).append("' and '".concat(getAmbito().equals("0000") ? "9999" : getAmbito()).concat("' and nivel=4   \n"));
        sentencia.append("        and  id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta());
        sentencia.append(" and extract(year from x.fecha_vig_ini)=").append(getControlReg().getEjercicio());
        sentencia.append(" group by x.cuenta_mayor_id)  \n");
        sentencia.append("      else  \n");
        //Calcula cuenta de programa cuando se captura rango de programas y regional 
        sentencia.append("        (case when nivel = 2 and '").append(getAmbito()).append("' = '0000' then \n");
        sentencia.append("          (select sum(").append(cargoAcum).append(")  from RF_TR_CUENTAS_CONTABLES x where   x.cuenta_mayor_id=cc.cuenta_mayor_id  \n");
        sentencia.append("            and substr(cuenta_contable,6,4) in (substr(cc.cuenta_contable,6,4)) \n ");
        sentencia.append("            and substr(cuenta_contable,10,4) between '").append(getUnidad()).append("' and '".concat(getUnidad().equals("0000") ? "9999" : getUnidad()).concat("' and  nivel=3    \n"));
        sentencia.append("            and  id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta());
        sentencia.append(" and extract(year from x.fecha_vig_ini)=").append(getControlReg().getEjercicio());
        sentencia.append(" group by x.cuenta_mayor_id)  \n");
        sentencia.append("        else      \n");
        //Calcula cuenta de programa cuando solo se captura un rango de programas,regional, entidad
        sentencia.append("        (case when nivel = 2 and '").append(getAmbito()).append("' <> '0000' then \n");
        sentencia.append("          (select sum(").append(cargoAcum).append(")  from RF_TR_CUENTAS_CONTABLES x where   x.cuenta_mayor_id=cc.cuenta_mayor_id  \n");
        sentencia.append("           and substr(cuenta_contable,6,4) in (substr(cc.cuenta_contable,6,4)) \n ");
        sentencia.append("           and substr(cuenta_contable,10,4) between '").append(getUnidad()).append("' and '".concat(getUnidad().equals("0000") ? "9999" : getUnidad()).concat("'  \n"));
        sentencia.append("           and substr(cuenta_contable,14,4) between '").append(getAmbito()).append("' and '".concat(getAmbito().equals("0000") ? "9999" : getAmbito()).concat("' and nivel=4   \n"));
        sentencia.append("           and  id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta());
        sentencia.append(" and extract(year from x.fecha_vig_ini)=").append(getControlReg().getEjercicio());
        sentencia.append(" group by x.cuenta_mayor_id)  \n");
        sentencia.append("         else \n");
        //Calcula cuenta de regional cuando solo se captura un rango de programas,regional, entidad        
        sentencia.append("           (case when nivel = 3 and '").append(getAmbito()).append("' <> '0000' then \n");
        sentencia.append("             (select sum(").append(cargoAcum).append(")  from RF_TR_CUENTAS_CONTABLES x where   x.cuenta_mayor_id=cc.cuenta_mayor_id  \n");
        sentencia.append("              and substr(cuenta_contable,6,4) in (substr(cc.cuenta_contable,6,4)) \n ");
        sentencia.append("              and substr(cuenta_contable,10,4) between '").append(getUnidad()).append("' and '".concat(getUnidad().equals("0000") ? "9999" : getUnidad()).concat("'  \n"));
        sentencia.append("              and substr(cuenta_contable,14,4) between '").append(getAmbito()).append("' and '".concat(getAmbito().equals("0000") ? "9999" : getAmbito()).concat("' and nivel=4   \n"));
        sentencia.append("              and  id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta());
        sentencia.append(" and extract(year from x.fecha_vig_ini)=").append(getControlReg().getEjercicio());
        sentencia.append(" group by x.cuenta_mayor_id)  \n");
        sentencia.append("            else  ").append(cargoAcum).append(" end) \n");
        sentencia.append("         end) \n");
        sentencia.append("      end) \n");
        sentencia.append("    end) \n");
        sentencia.append("  end) \n");
        sentencia.append("end) \n");

        sentencia.append(" +  \n");

        sentencia.append("(case when nivel = 1 and '").append(getUnidad()).append("' = '0000' and '").append(getAmbito()).append("' = '0000' then \n");
        sentencia.append("  (select sum(").append(cargo).append(")  from RF_TR_CUENTAS_CONTABLES x where   x.cuenta_mayor_id=cc.cuenta_mayor_id  \n");
        sentencia.append(cadenaPrograma).append(" and  nivel=2 \n");
        sentencia.append("   and  id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta());
        sentencia.append(" and extract(year from x.fecha_vig_ini)=").append(getControlReg().getEjercicio());
        sentencia.append(" group by x.cuenta_mayor_id)  \n");
        sentencia.append(" else      \n");
        sentencia.append("  (case when nivel = 1 and '").append(getAmbito()).append("' = '0000' then \n");
        sentencia.append("    (select sum(").append(cargo).append(")  from RF_TR_CUENTAS_CONTABLES x where   x.cuenta_mayor_id=cc.cuenta_mayor_id  \n");
        sentencia.append(cadenaPrograma);
        sentencia.append("     and substr(cuenta_contable,10,4) between '").append(getUnidad()).append("' and '".concat(getUnidad().equals("0000") ? "9999" : getUnidad()).concat("' and  nivel=3    \n"));
        sentencia.append("     and  id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta());
        sentencia.append(" and extract(year from x.fecha_vig_ini)=").append(getControlReg().getEjercicio());
        sentencia.append(" group by x.cuenta_mayor_id)  \n");
        sentencia.append("   else \n");
        //Calcula cuenta de mayor cuando solo se captura un rango de programas,regional, entidad
        sentencia.append("     (case when nivel = 1 and '").append(getAmbito()).append("' <> '0000' then \n");
        sentencia.append("       (select sum(").append(cargo).append(")  from RF_TR_CUENTAS_CONTABLES x where   x.cuenta_mayor_id=cc.cuenta_mayor_id  \n");
        sentencia.append(cadenaPrograma);
        sentencia.append("        and substr(cuenta_contable,10,4) between '").append(getUnidad()).append("' and '".concat(getUnidad().equals("0000") ? "9999" : getUnidad()).concat("'  \n"));
        sentencia.append("        and substr(cuenta_contable,14,4) between '").append(getAmbito()).append("' and '".concat(getAmbito().equals("0000") ? "9999" : getAmbito()).concat("' and nivel=4   \n"));
        sentencia.append("        and  id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta());
        sentencia.append(" and extract(year from x.fecha_vig_ini)=").append(getControlReg().getEjercicio());
        sentencia.append(" group by x.cuenta_mayor_id)  \n");
        sentencia.append("      else  \n");
        //Calcula cuenta de programa cuando se captura rango de programas y regional 
        sentencia.append("        (case when nivel = 2 and '").append(getAmbito()).append("' = '0000' then \n");
        sentencia.append("          (select sum(").append(cargo).append(")  from RF_TR_CUENTAS_CONTABLES x where   x.cuenta_mayor_id=cc.cuenta_mayor_id  \n");
        sentencia.append("            and substr(cuenta_contable,6,4) in (substr(cc.cuenta_contable,6,4)) \n ");
        sentencia.append("            and substr(cuenta_contable,10,4) between '").append(getUnidad()).append("' and '".concat(getUnidad().equals("0000") ? "9999" : getUnidad()).concat("' and  nivel=3    \n"));
        sentencia.append("            and  id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta());
        sentencia.append(" and extract(year from x.fecha_vig_ini)=").append(getControlReg().getEjercicio());
        sentencia.append(" group by x.cuenta_mayor_id)  \n");
        sentencia.append("        else      \n");
        //Calcula cuenta de programa cuando solo se captura un rango de programas,regional, entidad
        sentencia.append("        (case when nivel = 2 and '").append(getAmbito()).append("' <> '0000' then \n");
        sentencia.append("          (select sum(").append(cargo).append(")  from RF_TR_CUENTAS_CONTABLES x where   x.cuenta_mayor_id=cc.cuenta_mayor_id  \n");
        sentencia.append("           and substr(cuenta_contable,6,4) in (substr(cc.cuenta_contable,6,4)) \n ");
        sentencia.append("           and substr(cuenta_contable,10,4) between '").append(getUnidad()).append("' and '".concat(getUnidad().equals("0000") ? "9999" : getUnidad()).concat("'  \n"));
        sentencia.append("           and substr(cuenta_contable,14,4) between '").append(getAmbito()).append("' and '".concat(getAmbito().equals("0000") ? "9999" : getAmbito()).concat("' and nivel=4   \n"));
        sentencia.append("           and  id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta());
        sentencia.append(" and extract(year from x.fecha_vig_ini)=").append(getControlReg().getEjercicio());
        sentencia.append(" group by x.cuenta_mayor_id)  \n");
        sentencia.append("         else \n");
        //Calcula cuenta de regional cuando solo se captura un rango de programas,regional, entidad        
        sentencia.append("           (case when nivel = 3 and '").append(getAmbito()).append("' <> '0000' then \n");
        sentencia.append("             (select sum(").append(cargo).append(")  from RF_TR_CUENTAS_CONTABLES x where   x.cuenta_mayor_id=cc.cuenta_mayor_id  \n");
        sentencia.append("              and substr(cuenta_contable,6,4) in (substr(cc.cuenta_contable,6,4)) \n ");
        sentencia.append("              and substr(cuenta_contable,10,4) between '").append(getUnidad()).append("' and '".concat(getUnidad().equals("0000") ? "9999" : getUnidad()).concat("'  \n"));
        sentencia.append("              and substr(cuenta_contable,14,4) between '").append(getAmbito()).append("' and '".concat(getAmbito().equals("0000") ? "9999" : getAmbito()).concat("' and nivel=4   \n"));
        sentencia.append("              and  id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta());
        sentencia.append(" and extract(year from x.fecha_vig_ini)=").append(getControlReg().getEjercicio());
        sentencia.append(" group by x.cuenta_mayor_id)  \n");
        sentencia.append("            else  ").append(cargo).append(" end) \n");
        sentencia.append("         end) \n");
        sentencia.append("      end) \n");
        sentencia.append("    end) \n");
        sentencia.append("  end) \n");
        sentencia.append("end) \n");

        sentencia.append(" ) - ( \n");

        sentencia.append("(case when nivel = 1 and '").append(getUnidad()).append("' = '0000' and '").append(getAmbito()).append("' = '0000' then \n");
        sentencia.append("  (select sum(").append(abonoAcum).append(")  from RF_TR_CUENTAS_CONTABLES x where   x.cuenta_mayor_id=cc.cuenta_mayor_id  \n");
        sentencia.append(cadenaPrograma).append(" and  nivel=2 \n");
        sentencia.append("   and  id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta());
        sentencia.append(" and extract(year from x.fecha_vig_ini)=").append(getControlReg().getEjercicio());
        sentencia.append(" group by x.cuenta_mayor_id)  \n");
        sentencia.append(" else      \n");
        sentencia.append("  (case when nivel = 1 and '").append(getAmbito()).append("' = '0000' then \n");
        sentencia.append("    (select sum(").append(abonoAcum).append(")  from RF_TR_CUENTAS_CONTABLES x where   x.cuenta_mayor_id=cc.cuenta_mayor_id  \n");
        sentencia.append(cadenaPrograma);
        sentencia.append("     and substr(cuenta_contable,10,4) between '").append(getUnidad()).append("' and '".concat(getUnidad().equals("0000") ? "9999" : getUnidad()).concat("' and  nivel=3    \n"));
        sentencia.append("     and  id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta());
        sentencia.append(" and extract(year from x.fecha_vig_ini)=").append(getControlReg().getEjercicio());
        sentencia.append(" group by x.cuenta_mayor_id)  \n");
        sentencia.append("   else \n");
        //Calcula cuenta de mayor cuando solo se captura un rango de programas,regional, entidad
        sentencia.append("     (case when nivel = 1 and '").append(getAmbito()).append("' <> '0000' then \n");
        sentencia.append("       (select sum(").append(abonoAcum).append(")  from RF_TR_CUENTAS_CONTABLES x where   x.cuenta_mayor_id=cc.cuenta_mayor_id  \n");
        sentencia.append(cadenaPrograma);
        sentencia.append("        and substr(cuenta_contable,10,4) between '").append(getUnidad()).append("' and '".concat(getUnidad().equals("0000") ? "9999" : getUnidad()).concat("'  \n"));
        sentencia.append("        and substr(cuenta_contable,14,4) between '").append(getAmbito()).append("' and '".concat(getAmbito().equals("0000") ? "9999" : getAmbito()).concat("' and nivel=4   \n"));
        sentencia.append("        and  id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta());
        sentencia.append(" and extract(year from x.fecha_vig_ini)=").append(getControlReg().getEjercicio());
        sentencia.append(" group by x.cuenta_mayor_id)  \n");
        sentencia.append("      else  \n");
        //Calcula cuenta de programa cuando se captura rango de programas y regional 
        sentencia.append("        (case when nivel = 2 and '").append(getAmbito()).append("' = '0000' then \n");
        sentencia.append("          (select sum(").append(abonoAcum).append(")  from RF_TR_CUENTAS_CONTABLES x where   x.cuenta_mayor_id=cc.cuenta_mayor_id  \n");
        sentencia.append("            and substr(cuenta_contable,6,4) in (substr(cc.cuenta_contable,6,4)) \n ");
        sentencia.append("            and substr(cuenta_contable,10,4) between '").append(getUnidad()).append("' and '".concat(getUnidad().equals("0000") ? "9999" : getUnidad()).concat("' and  nivel=3    \n"));
        sentencia.append("            and  id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta());
        sentencia.append(" and extract(year from x.fecha_vig_ini)=").append(getControlReg().getEjercicio());
        sentencia.append(" group by x.cuenta_mayor_id)  \n");
        sentencia.append("        else      \n");
        //Calcula cuenta de programa cuando solo se captura un rango de programas,regional, entidad
        sentencia.append("        (case when nivel = 2 and '").append(getAmbito()).append("' <> '0000' then \n");
        sentencia.append("          (select sum(").append(abonoAcum).append(")  from RF_TR_CUENTAS_CONTABLES x where   x.cuenta_mayor_id=cc.cuenta_mayor_id  \n");
        sentencia.append("           and substr(cuenta_contable,6,4) in (substr(cc.cuenta_contable,6,4)) \n ");
        sentencia.append("           and substr(cuenta_contable,10,4) between '").append(getUnidad()).append("' and '".concat(getUnidad().equals("0000") ? "9999" : getUnidad()).concat("'  \n"));
        sentencia.append("           and substr(cuenta_contable,14,4) between '").append(getAmbito()).append("' and '".concat(getAmbito().equals("0000") ? "9999" : getAmbito()).concat("' and nivel=4   \n"));
        sentencia.append("           and  id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta());
        sentencia.append(" and extract(year from x.fecha_vig_ini)=").append(getControlReg().getEjercicio());
        sentencia.append(" group by x.cuenta_mayor_id)  \n");
        sentencia.append("         else \n");
        //Calcula cuenta de regional cuando solo se captura un rango de programas,regional, entidad        
        sentencia.append("           (case when nivel = 3 and '").append(getAmbito()).append("' <> '0000' then \n");
        sentencia.append("             (select sum(").append(abonoAcum).append(")  from RF_TR_CUENTAS_CONTABLES x where   x.cuenta_mayor_id=cc.cuenta_mayor_id  \n");
        sentencia.append("              and substr(cuenta_contable,6,4) in (substr(cc.cuenta_contable,6,4)) \n ");
        sentencia.append("              and substr(cuenta_contable,10,4) between '").append(getUnidad()).append("' and '".concat(getUnidad().equals("0000") ? "9999" : getUnidad()).concat("'  \n"));
        sentencia.append("              and substr(cuenta_contable,14,4) between '").append(getAmbito()).append("' and '".concat(getAmbito().equals("0000") ? "9999" : getAmbito()).concat("' and nivel=4   \n"));
        sentencia.append("              and  id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta());
        sentencia.append(" and extract(year from x.fecha_vig_ini)=").append(getControlReg().getEjercicio());
        sentencia.append(" group by x.cuenta_mayor_id)  \n");
        sentencia.append("            else  ").append(abonoAcum).append(" end) \n");
        sentencia.append("         end) \n");
        sentencia.append("      end) \n");
        sentencia.append("    end) \n");
        sentencia.append("  end) \n");
        sentencia.append("end) \n");

        sentencia.append(" +  \n");

        sentencia.append("(case when nivel = 1 and '").append(getUnidad()).append("' = '0000' and '").append(getAmbito()).append("' = '0000' then \n");
        sentencia.append("  (select sum(").append(abono).append(")  from RF_TR_CUENTAS_CONTABLES x where   x.cuenta_mayor_id=cc.cuenta_mayor_id  \n");
        sentencia.append(cadenaPrograma).append(" and  nivel=2 \n");
        sentencia.append("   and  id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta());
        sentencia.append(" and extract(year from x.fecha_vig_ini)=").append(getControlReg().getEjercicio());
        sentencia.append(" group by x.cuenta_mayor_id)  \n");
        sentencia.append(" else      \n");
        sentencia.append("  (case when nivel = 1 and '").append(getAmbito()).append("' = '0000' then \n");
        sentencia.append("    (select sum(").append(abono).append(")  from RF_TR_CUENTAS_CONTABLES x where   x.cuenta_mayor_id=cc.cuenta_mayor_id  \n");
        sentencia.append(cadenaPrograma);
        sentencia.append("     and substr(cuenta_contable,10,4) between '").append(getUnidad()).append("' and '".concat(getUnidad().equals("0000") ? "9999" : getUnidad()).concat("' and  nivel=3    \n"));
        sentencia.append("     and  id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta());
        sentencia.append(" and extract(year from x.fecha_vig_ini)=").append(getControlReg().getEjercicio());
        sentencia.append(" group by x.cuenta_mayor_id)  \n");
        sentencia.append("   else \n");
        //Calcula cuenta de mayor cuando solo se captura un rango de programas,regional, entidad
        sentencia.append("     (case when nivel = 1 and '").append(getAmbito()).append("' <> '0000' then \n");
        sentencia.append("       (select sum(").append(abono).append(")  from RF_TR_CUENTAS_CONTABLES x where   x.cuenta_mayor_id=cc.cuenta_mayor_id  \n");
        sentencia.append(cadenaPrograma);
        sentencia.append("        and substr(cuenta_contable,10,4) between '").append(getUnidad()).append("' and '".concat(getUnidad().equals("0000") ? "9999" : getUnidad()).concat("'  \n"));
        sentencia.append("        and substr(cuenta_contable,14,4) between '").append(getAmbito()).append("' and '".concat(getAmbito().equals("0000") ? "9999" : getAmbito()).concat("' and nivel=4   \n"));
        sentencia.append("        and  id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta());
        sentencia.append(" and extract(year from x.fecha_vig_ini)=").append(getControlReg().getEjercicio());
        sentencia.append(" group by x.cuenta_mayor_id)  \n");
        sentencia.append("      else  \n");
        //Calcula cuenta de programa cuando se captura rango de programas y regional 
        sentencia.append("        (case when nivel = 2 and '").append(getAmbito()).append("' = '0000' then \n");
        sentencia.append("          (select sum(").append(abono).append(")  from RF_TR_CUENTAS_CONTABLES x where   x.cuenta_mayor_id=cc.cuenta_mayor_id  \n");
        sentencia.append("            and substr(cuenta_contable,6,4) in (substr(cc.cuenta_contable,6,4)) \n ");
        sentencia.append("            and substr(cuenta_contable,10,4) between '").append(getUnidad()).append("' and '".concat(getUnidad().equals("0000") ? "9999" : getUnidad()).concat("' and  nivel=3    \n"));
        sentencia.append("            and  id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta());
        sentencia.append(" and extract(year from x.fecha_vig_ini)=").append(getControlReg().getEjercicio());
        sentencia.append(" group by x.cuenta_mayor_id)  \n");
        sentencia.append("        else      \n");
        //Calcula cuenta de programa cuando solo se captura un rango de programas,regional, entidad
        sentencia.append("        (case when nivel = 2 and '").append(getAmbito()).append("' <> '0000' then \n");
        sentencia.append("          (select sum(").append(abono).append(")  from RF_TR_CUENTAS_CONTABLES x where   x.cuenta_mayor_id=cc.cuenta_mayor_id  \n");
        sentencia.append("           and substr(cuenta_contable,6,4) in (substr(cc.cuenta_contable,6,4)) \n ");
        sentencia.append("           and substr(cuenta_contable,10,4) between '").append(getUnidad()).append("' and '".concat(getUnidad().equals("0000") ? "9999" : getUnidad()).concat("'  \n"));
        sentencia.append("           and substr(cuenta_contable,14,4) between '").append(getAmbito()).append("' and '".concat(getAmbito().equals("0000") ? "9999" : getAmbito()).concat("' and nivel=4   \n"));
        sentencia.append("           and  id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta());
        sentencia.append(" and extract(year from x.fecha_vig_ini)=").append(getControlReg().getEjercicio());
        sentencia.append(" group by x.cuenta_mayor_id)  \n");
        sentencia.append("         else \n");
        //Calcula cuenta de regional cuando solo se captura un rango de programas,regional, entidad        
        sentencia.append("           (case when nivel = 3 and '").append(getAmbito()).append("' <> '0000' then \n");
        sentencia.append("             (select sum(").append(abono).append(")  from RF_TR_CUENTAS_CONTABLES x where   x.cuenta_mayor_id=cc.cuenta_mayor_id  \n");
        sentencia.append("              and substr(cuenta_contable,6,4) in (substr(cc.cuenta_contable,6,4)) \n ");
        sentencia.append("              and substr(cuenta_contable,10,4) between '").append(getUnidad()).append("' and '".concat(getUnidad().equals("0000") ? "9999" : getUnidad()).concat("'  \n"));
        sentencia.append("              and substr(cuenta_contable,14,4) between '").append(getAmbito()).append("' and '".concat(getAmbito().equals("0000") ? "9999" : getAmbito()).concat("' and nivel=4   \n"));
        sentencia.append("              and  id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta());
        sentencia.append(" and extract(year from x.fecha_vig_ini)=").append(getControlReg().getEjercicio());
        sentencia.append(" group by x.cuenta_mayor_id)  \n");
        sentencia.append("            else  ").append(abono).append(" end) \n");
        sentencia.append("         end) \n");
        sentencia.append("      end) \n");
        sentencia.append("    end) \n");
        sentencia.append("  end) \n");
        sentencia.append("end) \n");

        sentencia.append(" )) \n");
        sentencia.append(" else ((  \n");

        sentencia.append("(case when nivel = 1 and '").append(getUnidad()).append("' = '0000' and '").append(getAmbito()).append("' = '0000' then \n");
        sentencia.append("  (select sum(").append(abonoAcum).append(")  from RF_TR_CUENTAS_CONTABLES x where   x.cuenta_mayor_id=cc.cuenta_mayor_id  \n");
        sentencia.append(cadenaPrograma).append(" and  nivel=2 \n");
        sentencia.append("   and  id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta());
        sentencia.append(" and extract(year from x.fecha_vig_ini)=").append(getControlReg().getEjercicio());
        sentencia.append(" group by x.cuenta_mayor_id)  \n");
        sentencia.append(" else      \n");
        sentencia.append("  (case when nivel = 1 and '").append(getAmbito()).append("' = '0000' then \n");
        sentencia.append("    (select sum(").append(abonoAcum).append(")  from RF_TR_CUENTAS_CONTABLES x where   x.cuenta_mayor_id=cc.cuenta_mayor_id  \n");
        sentencia.append(cadenaPrograma);
        sentencia.append("     and substr(cuenta_contable,10,4) between '").append(getUnidad()).append("' and '".concat(getUnidad().equals("0000") ? "9999" : getUnidad()).concat("' and  nivel=3    \n"));
        sentencia.append("     and  id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta());
        sentencia.append(" and extract(year from x.fecha_vig_ini)=").append(getControlReg().getEjercicio());
        sentencia.append(" group by x.cuenta_mayor_id)  \n");
        sentencia.append("   else \n");
        //Calcula cuenta de mayor cuando solo se captura un rango de programas,regional, entidad
        sentencia.append("     (case when nivel = 1 and '").append(getAmbito()).append("' <> '0000' then \n");
        sentencia.append("       (select sum(").append(abonoAcum).append(")  from RF_TR_CUENTAS_CONTABLES x where   x.cuenta_mayor_id=cc.cuenta_mayor_id  \n");
        sentencia.append(cadenaPrograma);
        sentencia.append("        and substr(cuenta_contable,10,4) between '").append(getUnidad()).append("' and '".concat(getUnidad().equals("0000") ? "9999" : getUnidad()).concat("'  \n"));
        sentencia.append("        and substr(cuenta_contable,14,4) between '").append(getAmbito()).append("' and '".concat(getAmbito().equals("0000") ? "9999" : getAmbito()).concat("' and nivel=4   \n"));
        sentencia.append("        and  id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta());
        sentencia.append(" and extract(year from x.fecha_vig_ini)=").append(getControlReg().getEjercicio());
        sentencia.append(" group by x.cuenta_mayor_id)  \n");
        sentencia.append("      else  \n");
        //Calcula cuenta de programa cuando se captura rango de programas y regional 
        sentencia.append("        (case when nivel = 2 and '").append(getAmbito()).append("' = '0000' then \n");
        sentencia.append("          (select sum(").append(abonoAcum).append(")  from RF_TR_CUENTAS_CONTABLES x where   x.cuenta_mayor_id=cc.cuenta_mayor_id  \n");
        sentencia.append("            and substr(cuenta_contable,6,4) in (substr(cc.cuenta_contable,6,4)) \n ");
        sentencia.append("            and substr(cuenta_contable,10,4) between '").append(getUnidad()).append("' and '".concat(getUnidad().equals("0000") ? "9999" : getUnidad()).concat("' and  nivel=3    \n"));
        sentencia.append("            and  id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta());
        sentencia.append(" and extract(year from x.fecha_vig_ini)=").append(getControlReg().getEjercicio());
        sentencia.append(" group by x.cuenta_mayor_id)  \n");
        sentencia.append("        else      \n");
        //Calcula cuenta de programa cuando solo se captura un rango de programas,regional, entidad
        sentencia.append("        (case when nivel = 2 and '").append(getAmbito()).append("' <> '0000' then \n");
        sentencia.append("          (select sum(").append(abonoAcum).append(")  from RF_TR_CUENTAS_CONTABLES x where   x.cuenta_mayor_id=cc.cuenta_mayor_id  \n");
        sentencia.append("           and substr(cuenta_contable,6,4) in (substr(cc.cuenta_contable,6,4)) \n ");
        sentencia.append("           and substr(cuenta_contable,10,4) between '").append(getUnidad()).append("' and '".concat(getUnidad().equals("0000") ? "9999" : getUnidad()).concat("'  \n"));
        sentencia.append("           and substr(cuenta_contable,14,4) between '").append(getAmbito()).append("' and '".concat(getAmbito().equals("0000") ? "9999" : getAmbito()).concat("' and nivel=4   \n"));
        sentencia.append("           and  id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta());
        sentencia.append(" and extract(year from x.fecha_vig_ini)=").append(getControlReg().getEjercicio());
        sentencia.append(" group by x.cuenta_mayor_id)  \n");
        sentencia.append("         else \n");
        //Calcula cuenta de regional cuando solo se captura un rango de programas,regional, entidad        
        sentencia.append("           (case when nivel = 3 and '").append(getAmbito()).append("' <> '0000' then \n");
        sentencia.append("             (select sum(").append(abonoAcum).append(")  from RF_TR_CUENTAS_CONTABLES x where   x.cuenta_mayor_id=cc.cuenta_mayor_id  \n");
        sentencia.append("              and substr(cuenta_contable,6,4) in (substr(cc.cuenta_contable,6,4)) \n ");
        sentencia.append("              and substr(cuenta_contable,10,4) between '").append(getUnidad()).append("' and '".concat(getUnidad().equals("0000") ? "9999" : getUnidad()).concat("'  \n"));
        sentencia.append("              and substr(cuenta_contable,14,4) between '").append(getAmbito()).append("' and '".concat(getAmbito().equals("0000") ? "9999" : getAmbito()).concat("' and nivel=4   \n"));
        sentencia.append("              and  id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta());
        sentencia.append(" and extract(year from x.fecha_vig_ini)=").append(getControlReg().getEjercicio());
        sentencia.append(" group by x.cuenta_mayor_id)  \n");
        sentencia.append("            else  ").append(abonoAcum).append(" end) \n");
        sentencia.append("         end) \n");
        sentencia.append("      end) \n");
        sentencia.append("    end) \n");
        sentencia.append("  end) \n");
        sentencia.append("end) \n");

        sentencia.append(" +  \n");

        sentencia.append("(case when nivel = 1 and '").append(getUnidad()).append("' = '0000' and '").append(getAmbito()).append("' = '0000' then \n");
        sentencia.append("  (select sum(").append(abono).append(")  from RF_TR_CUENTAS_CONTABLES x where   x.cuenta_mayor_id=cc.cuenta_mayor_id  \n");
        sentencia.append(cadenaPrograma).append(" and  nivel=2 \n");
        sentencia.append("   and  id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta());
        sentencia.append(" and extract(year from x.fecha_vig_ini)=").append(getControlReg().getEjercicio());
        sentencia.append(" group by x.cuenta_mayor_id)  \n");
        sentencia.append(" else      \n");
        sentencia.append("  (case when nivel = 1 and '").append(getAmbito()).append("' = '0000' then \n");
        sentencia.append("    (select sum(").append(abono).append(")  from RF_TR_CUENTAS_CONTABLES x where   x.cuenta_mayor_id=cc.cuenta_mayor_id  \n");
        sentencia.append(cadenaPrograma);
        sentencia.append("     and substr(cuenta_contable,10,4) between '").append(getUnidad()).append("' and '".concat(getUnidad().equals("0000") ? "9999" : getUnidad()).concat("' and  nivel=3    \n"));
        sentencia.append("     and  id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta()).append(" and extract(year from x.fecha_vig_ini)=").append(getControlReg().getEjercicio()).append(" group by x.cuenta_mayor_id)  \n");
        sentencia.append("   else \n");
        //Calcula cuenta de mayor cuando solo se captura un rango de programas,regional, entidad
        sentencia.append("     (case when nivel = 1 and '").append(getAmbito()).append("' <> '0000' then \n");
        sentencia.append("       (select sum(").append(abono).append(")  from RF_TR_CUENTAS_CONTABLES x where   x.cuenta_mayor_id=cc.cuenta_mayor_id  \n");
        sentencia.append(cadenaPrograma);
        sentencia.append("        and substr(cuenta_contable,10,4) between '").append(getUnidad()).append("' and '".concat(getUnidad().equals("0000") ? "9999" : getUnidad()).concat("'  \n"));
        sentencia.append("        and substr(cuenta_contable,14,4) between '").append(getAmbito()).append("' and '".concat(getAmbito().equals("0000") ? "9999" : getAmbito()).concat("' and nivel=4   \n"));
        sentencia.append("        and  id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta());
        sentencia.append(" and extract(year from x.fecha_vig_ini)=").append(getControlReg().getEjercicio());
        sentencia.append(" group by x.cuenta_mayor_id)  \n");
        sentencia.append("      else  \n");
        //Calcula cuenta de programa cuando se captura rango de programas y regional 
        sentencia.append("        (case when nivel = 2 and '").append(getAmbito()).append("' = '0000' then \n");
        sentencia.append("          (select sum(").append(abono).append(")  from RF_TR_CUENTAS_CONTABLES x where   x.cuenta_mayor_id=cc.cuenta_mayor_id  \n");
        sentencia.append("            and substr(cuenta_contable,6,4) in (substr(cc.cuenta_contable,6,4)) \n ");
        sentencia.append("            and substr(cuenta_contable,10,4) between '").append(getUnidad()).append("' and '".concat(getUnidad().equals("0000") ? "9999" : getUnidad()).concat("' and  nivel=3    \n"));
        sentencia.append("            and  id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta()).append(" and extract(year from x.fecha_vig_ini)=").append(getControlReg().getEjercicio()).append(" group by x.cuenta_mayor_id)  \n");
        sentencia.append("        else      \n");
        //Calcula cuenta de programa cuando solo se captura un rango de programas,regional, entidad
        sentencia.append("        (case when nivel = 2 and '").append(getAmbito()).append("' <> '0000' then \n");
        sentencia.append("          (select sum(").append(abono).append(")  from RF_TR_CUENTAS_CONTABLES x where   x.cuenta_mayor_id=cc.cuenta_mayor_id  \n");
        sentencia.append("           and substr(cuenta_contable,6,4) in (substr(cc.cuenta_contable,6,4)) \n ");
        sentencia.append("           and substr(cuenta_contable,10,4) between '").append(getUnidad()).append("' and '".concat(getUnidad().equals("0000") ? "9999" : getUnidad()).concat("'  \n"));
        sentencia.append("           and substr(cuenta_contable,14,4) between '").append(getAmbito()).append("' and '".concat(getAmbito().equals("0000") ? "9999" : getAmbito()).concat("' and nivel=4   \n"));
        sentencia.append("           and  id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta());
        sentencia.append(" and extract(year from x.fecha_vig_ini)=").append(getControlReg().getEjercicio());
        sentencia.append(" group by x.cuenta_mayor_id)  \n");
        sentencia.append("         else \n");
        //Calcula cuenta de regional cuando solo se captura un rango de programas,regional, entidad        
        sentencia.append("           (case when nivel = 3 and '").append(getAmbito()).append("' <> '0000' then \n");
        sentencia.append("             (select sum(").append(abono).append(")  from RF_TR_CUENTAS_CONTABLES x where   x.cuenta_mayor_id=cc.cuenta_mayor_id  \n");
        sentencia.append("              and substr(cuenta_contable,6,4) in (substr(cc.cuenta_contable,6,4)) \n ");
        sentencia.append("              and substr(cuenta_contable,10,4) between '").append(getUnidad()).append("' and '".concat(getUnidad().equals("0000") ? "9999" : getUnidad()).concat("'  \n"));
        sentencia.append("              and substr(cuenta_contable,14,4) between '").append(getAmbito()).append("' and '".concat(getAmbito().equals("0000") ? "9999" : getAmbito()).concat("' and nivel=4   \n"));
        sentencia.append("              and  id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta());
        sentencia.append(" and extract(year from x.fecha_vig_ini)=").append(getControlReg().getEjercicio());
        sentencia.append(" group by x.cuenta_mayor_id)  \n");
        sentencia.append("            else  ").append(abono).append(" end) \n");
        sentencia.append("         end) \n");
        sentencia.append("      end) \n");
        sentencia.append("    end) \n");
        sentencia.append("  end) \n");
        sentencia.append("end) \n");

        sentencia.append(" ) - (  \n");

        sentencia.append("(case when nivel = 1 and '").append(getUnidad()).append("' = '0000' and '").append(getAmbito()).append("' = '0000' then \n");
        sentencia.append("  (select sum(").append(cargoAcum).append(")  from RF_TR_CUENTAS_CONTABLES x where   x.cuenta_mayor_id=cc.cuenta_mayor_id  \n");
        sentencia.append(cadenaPrograma).append(" and  nivel=2 \n");
        sentencia.append("   and  id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta());
        sentencia.append(" and extract(year from x.fecha_vig_ini)=").append(getControlReg().getEjercicio());
        sentencia.append(" group by x.cuenta_mayor_id)  \n");
        sentencia.append(" else      \n");
        sentencia.append("  (case when nivel = 1 and '").append(getAmbito()).append("' = '0000' then \n");
        sentencia.append("    (select sum(").append(cargoAcum).append(")  from RF_TR_CUENTAS_CONTABLES x where   x.cuenta_mayor_id=cc.cuenta_mayor_id  \n");
        sentencia.append(cadenaPrograma);
        sentencia.append("     and substr(cuenta_contable,10,4) between '").append(getUnidad()).append("' and '".concat(getUnidad().equals("0000") ? "9999" : getUnidad()).concat("' and  nivel=3    \n"));
        sentencia.append("     and  id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta());
        sentencia.append(" and extract(year from x.fecha_vig_ini)=").append(getControlReg().getEjercicio());
        sentencia.append(" group by x.cuenta_mayor_id)  \n");
        sentencia.append("   else \n");
        //Calcula cuenta de mayor cuando solo se captura un rango de programas,regional, entidad
        sentencia.append("     (case when nivel = 1 and '").append(getAmbito()).append("' <> '0000' then \n");
        sentencia.append("       (select sum(").append(cargoAcum).append(")  from RF_TR_CUENTAS_CONTABLES x where   x.cuenta_mayor_id=cc.cuenta_mayor_id  \n");
        sentencia.append(cadenaPrograma);
        sentencia.append("        and substr(cuenta_contable,10,4) between '").append(getUnidad()).append("' and '".concat(getUnidad().equals("0000") ? "9999" : getUnidad()).concat("'  \n"));
        sentencia.append("        and substr(cuenta_contable,14,4) between '").append(getAmbito()).append("' and '".concat(getAmbito().equals("0000") ? "9999" : getAmbito()).concat("' and nivel=4   \n"));
        sentencia.append("        and  id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta());
        sentencia.append(" and extract(year from x.fecha_vig_ini)=").append(getControlReg().getEjercicio());
        sentencia.append(" group by x.cuenta_mayor_id)  \n");
        sentencia.append("      else  \n");
        //Calcula cuenta de programa cuando se captura rango de programas y regional 
        sentencia.append("        (case when nivel = 2 and '").append(getAmbito()).append("' = '0000' then \n");
        sentencia.append("          (select sum(").append(cargoAcum).append(")  from RF_TR_CUENTAS_CONTABLES x where   x.cuenta_mayor_id=cc.cuenta_mayor_id  \n");
        sentencia.append("            and substr(cuenta_contable,6,4) in (substr(cc.cuenta_contable,6,4)) \n ");
        sentencia.append("            and substr(cuenta_contable,10,4) between '").append(getUnidad()).append("' and '".concat(getUnidad().equals("0000") ? "9999" : getUnidad()).concat("' and  nivel=3    \n"));
        sentencia.append("            and  id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta());
        sentencia.append(" and extract(year from x.fecha_vig_ini)=").append(getControlReg().getEjercicio());
        sentencia.append(" group by x.cuenta_mayor_id)  \n");
        sentencia.append("        else      \n");
        //Calcula cuenta de programa cuando solo se captura un rango de programas,regional, entidad
        sentencia.append("        (case when nivel = 2 and '").append(getAmbito()).append("' <> '0000' then \n");
        sentencia.append("          (select sum(").append(cargoAcum).append(")  from RF_TR_CUENTAS_CONTABLES x where   x.cuenta_mayor_id=cc.cuenta_mayor_id  \n");
        sentencia.append("           and substr(cuenta_contable,6,4) in (substr(cc.cuenta_contable,6,4)) \n ");
        sentencia.append("           and substr(cuenta_contable,10,4) between '").append(getUnidad()).append("' and '".concat(getUnidad().equals("0000") ? "9999" : getUnidad()).concat("'  \n"));
        sentencia.append("           and substr(cuenta_contable,14,4) between '").append(getAmbito()).append("' and '".concat(getAmbito().equals("0000") ? "9999" : getAmbito()).concat("' and nivel=4   \n"));
        sentencia.append("           and  id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta()).append(" and extract(year from x.fecha_vig_ini)=").append(getControlReg().getEjercicio()).append(" group by x.cuenta_mayor_id)  \n");
        sentencia.append("         else \n");
        //Calcula cuenta de regional cuando solo se captura un rango de programas,regional, entidad        
        sentencia.append("           (case when nivel = 3 and '").append(getAmbito()).append("' <> '0000' then \n");
        sentencia.append("             (select sum(").append(cargoAcum).append(")  from RF_TR_CUENTAS_CONTABLES x where   x.cuenta_mayor_id=cc.cuenta_mayor_id  \n");
        sentencia.append("              and substr(cuenta_contable,6,4) in (substr(cc.cuenta_contable,6,4)) \n ");
        sentencia.append("              and substr(cuenta_contable,10,4) between '").append(getUnidad()).append("' and '".concat(getUnidad().equals("0000") ? "9999" : getUnidad()).concat("'  \n"));
        sentencia.append("              and substr(cuenta_contable,14,4) between '").append(getAmbito()).append("' and '".concat(getAmbito().equals("0000") ? "9999" : getAmbito()).concat("' and nivel=4   \n"));
        sentencia.append("              and  id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta());
        sentencia.append(" and extract(year from x.fecha_vig_ini)=").append(getControlReg().getEjercicio());
        sentencia.append(" group by x.cuenta_mayor_id)  \n");
        sentencia.append("            else  ").append(cargoAcum).append(" end) \n");
        sentencia.append("         end) \n");
        sentencia.append("      end) \n");
        sentencia.append("    end) \n");
        sentencia.append("  end) \n");
        sentencia.append("end) \n");

        sentencia.append(" +  ");

        sentencia.append("(case when nivel = 1 and '").append(getUnidad()).append("' = '0000' and '").append(getAmbito()).append("' = '0000' then \n");
        sentencia.append("  (select sum(").append(cargo).append(")  from RF_TR_CUENTAS_CONTABLES x where   x.cuenta_mayor_id=cc.cuenta_mayor_id  \n");
        sentencia.append(cadenaPrograma).append(" and  nivel=2 \n");
        sentencia.append("   and  id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta());
        sentencia.append(" and extract(year from x.fecha_vig_ini)=").append(getControlReg().getEjercicio());
        sentencia.append(" group by x.cuenta_mayor_id)  \n");
        sentencia.append(" else      \n");
        sentencia.append("  (case when nivel = 1 and '").append(getAmbito()).append("' = '0000' then \n");
        sentencia.append("    (select sum(").append(cargo).append(")  from RF_TR_CUENTAS_CONTABLES x where   x.cuenta_mayor_id=cc.cuenta_mayor_id  \n");
        sentencia.append(cadenaPrograma);
        sentencia.append("     and substr(cuenta_contable,10,4) between '").append(getUnidad()).append("' and '".concat(getUnidad().equals("0000") ? "9999" : getUnidad()).concat("' and  nivel=3    \n"));
        sentencia.append("     and  id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta());
        sentencia.append(" and extract(year from x.fecha_vig_ini)=").append(getControlReg().getEjercicio());
        sentencia.append(" group by x.cuenta_mayor_id)  \n");
        sentencia.append("   else \n");
        //Calcula cuenta de mayor cuando solo se captura un rango de programas,regional, entidad
        sentencia.append("     (case when nivel = 1 and '").append(getAmbito()).append("' <> '0000' then \n");
        sentencia.append("       (select sum(").append(cargo).append(")  from RF_TR_CUENTAS_CONTABLES x where   x.cuenta_mayor_id=cc.cuenta_mayor_id  \n");
        sentencia.append(cadenaPrograma);
        sentencia.append("        and substr(cuenta_contable,10,4) between '").append(getUnidad()).append("' and '".concat(getUnidad().equals("0000") ? "9999" : getUnidad()).concat("'  \n"));
        sentencia.append("        and substr(cuenta_contable,14,4) between '").append(getAmbito()).append("' and '".concat(getAmbito().equals("0000") ? "9999" : getAmbito()).concat("' and nivel=4   \n"));
        sentencia.append("        and  id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta());
        sentencia.append(" and extract(year from x.fecha_vig_ini)=").append(getControlReg().getEjercicio());
        sentencia.append(" group by x.cuenta_mayor_id)  \n");
        sentencia.append("      else  \n");
        //Calcula cuenta de programa cuando se captura rango de programas y regional 
        sentencia.append("        (case when nivel = 2 and '").append(getAmbito()).append("' = '0000' then \n");
        sentencia.append("          (select sum(").append(cargo);
        sentencia.append(")  from RF_TR_CUENTAS_CONTABLES x where   x.cuenta_mayor_id=cc.cuenta_mayor_id  \n");
        sentencia.append("            and substr(cuenta_contable,6,4) in (substr(cc.cuenta_contable,6,4)) \n ");
        sentencia.append("            and substr(cuenta_contable,10,4) between '").append(getUnidad()).append("' and '".concat(getUnidad().equals("0000") ? "9999" : getUnidad()).concat("' and  nivel=3    \n"));
        sentencia.append("            and  id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta()).append(" and extract(year from x.fecha_vig_ini)=").append(getControlReg().getEjercicio()).append(" group by x.cuenta_mayor_id)  \n");
        sentencia.append("        else      \n");
        //Calcula cuenta de programa cuando solo se captura un rango de programas,regional, entidad
        sentencia.append("        (case when nivel = 2 and '").append(getAmbito()).append("' <> '0000' then \n");
        sentencia.append("          (select sum(").append(cargo).append(")  from RF_TR_CUENTAS_CONTABLES x where   x.cuenta_mayor_id=cc.cuenta_mayor_id  \n");
        sentencia.append("           and substr(cuenta_contable,6,4) in (substr(cc.cuenta_contable,6,4)) \n ");
        sentencia.append("           and substr(cuenta_contable,10,4) between '").append(getUnidad()).append("' and '".concat(getUnidad().equals("0000") ? "9999" : getUnidad()).concat("'  \n"));
        sentencia.append("           and substr(cuenta_contable,14,4) between '").append(getAmbito()).append("' and '".concat(getAmbito().equals("0000") ? "9999" : getAmbito()).concat("' and nivel=4   \n"));
        sentencia.append("           and  id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta()).append(" and extract(year from x.fecha_vig_ini)=").append(getControlReg().getEjercicio()).append(" group by x.cuenta_mayor_id)  \n");
        sentencia.append("         else \n");
        //Calcula cuenta de regional cuando solo se captura un rango de programas,regional, entidad        
        sentencia.append("           (case when nivel = 3 and '").append(getAmbito()).append("' <> '0000' then \n");
        sentencia.append("             (select sum(").append(cargo).append(")  from RF_TR_CUENTAS_CONTABLES x where   x.cuenta_mayor_id=cc.cuenta_mayor_id  \n");
        sentencia.append("              and substr(cuenta_contable,6,4) in (substr(cc.cuenta_contable,6,4)) \n ");
        sentencia.append("              and substr(cuenta_contable,10,4) between '").append(getUnidad()).append("' and '".concat(getUnidad().equals("0000") ? "9999" : getUnidad()).concat("'  \n"));
        sentencia.append("              and substr(cuenta_contable,14,4) between '").append(getAmbito()).append("' and '".concat(getAmbito().equals("0000") ? "9999" : getAmbito()).concat("' and nivel=4   \n"));
        sentencia.append("              and  id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta()).append(" and extract(year from x.fecha_vig_ini)=").append(getControlReg().getEjercicio()).append(" group by x.cuenta_mayor_id)  \n");
        sentencia.append("            else  ").append(cargo).append(" end) \n");
        sentencia.append("         end) \n");
        sentencia.append("      end) \n");
        sentencia.append("    end) \n");
        sentencia.append("  end) \n");
        sentencia.append("end) \n");
        sentencia.append(" )) end saldo_actual \n ");

        //sentencia.append("nivel ");
        sentencia.append("from \n ");
        sentencia.append("rf_tr_cuentas_contables cc  \n");
        sentencia.append("inner join rf_tc_clasificador_cuentas cla \n ");
        sentencia.append("on cc.cuenta_mayor_id = cla.cuenta_mayor_id \n ");
        sentencia.append("where  \n");
        //sentencia.append("nivel<={1} and ");
        sentencia.append("extract(year from cc.fecha_vig_ini) = ").append(getControlReg().getEjercicio()).append("\n ");
        sentencia.append("and id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta()).append(" \n ");

        if (Integer.valueOf(getNivel()) == 1 || Integer.valueOf(getNivel()) == 2) {
            sentencia.append("and (");
            sentencia.append("(substr(cuenta_contable,1,5) between '0000' and '9999' and nivel=1 \n");
            //sentencia.append("and substr(cuenta_contable,1,5) in (select distinct substr(cuenta_contable,1,5) from RF_TR_CUENTAS_CONTABLES  where substr(cuenta_contable,6,4) between '{6}' and '".concat(programaFormateado.equals("0000")?"9999":programaFormateado).concat("' and id_catalogo_cuenta = "+getControlReg().getIdCatalogoCuenta()+" ) "));
            sentencia.append("and substr(cuenta_contable,1,5) in (select distinct substr(cuenta_contable,1,5) from RF_TR_CUENTAS_CONTABLES \n");
            //sentencia.append("where substr(cuenta_contable,6,4) between '{6}' and '".concat(programaFormateado.equals("0000")?"9999":programaFormateado).concat("' \n"));
            sentencia.append("where id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta()).append(" ").append(cadenaPrograma);
            sentencia.append("and substr(cuenta_contable,10,4) between '0000' and '9999' \n");
            sentencia.append("and substr(cuenta_contable,14,4) between '0000' and '9999' \n");
            sentencia.append("and extract(year from fecha_vig_ini)=").append(getControlReg().getEjercicio()).append(" )\n");
            sentencia.append(") ");
            sentencia.append("or (substr(cuenta_contable,1,5) between '0000' and '9999'  \n");
            //sentencia.append("and substr(cuenta_contable,6,4) between '{6}' and '".concat(programaFormateado.equals("0000")?"9999":programaFormateado).concat("' and nivel=2  \n"));
            sentencia.append(cadenaPrograma).append("and nivel=2 \n");
            sentencia.append("and substr(cuenta_contable,1,5) in (select distinct substr(cuenta_contable,1,5) from RF_TR_CUENTAS_CONTABLES \n");
            //sentencia.append("where substr(cuenta_contable,6,4) between '{6}' and '".concat(programaFormateado.equals("0000")?"9999":programaFormateado).concat("' \n"));
            sentencia.append("where id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta()).append(" ").append(cadenaPrograma);
            sentencia.append("and substr(cuenta_contable,10,4) between '0000' and '9999'\n ");
            sentencia.append("and substr(cuenta_contable,14,4) between '0000' and '9999' \n");
            sentencia.append("and extract(year from fecha_vig_ini)=").append(getControlReg().getEjercicio()).append(" )\n");
            sentencia.append(")  \n");
            sentencia.append("or (substr(cuenta_contable,1,5) between '0000' and '9999'  \n");
            //sentencia.append("and substr(cuenta_contable,6,4) between '{6}' and '".concat(programaFormateado.equals("0000")?"9999":programaFormateado).concat("' \n"));
            sentencia.append(cadenaPrograma);
            sentencia.append("and substr(cuenta_contable,10,4) between '0000' and '9999' and nivel=3  \n");
            sentencia.append("and substr(cuenta_contable,1,5) in (select distinct substr(cuenta_contable,1,5) from RF_TR_CUENTAS_CONTABLES \n");
            //sentencia.append("where substr(cuenta_contable,6,4) between '{6}' and '".concat(programaFormateado.equals("0000")?"9999":programaFormateado).concat("' \n"));
            sentencia.append("where id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta()).append(" ").append(cadenaPrograma);
            sentencia.append("and substr(cuenta_contable,10,4) between '0000' and '9999' \n");
            sentencia.append("and substr(cuenta_contable,14,4) between '0000' and '9999' \n");
            sentencia.append("and extract(year from fecha_vig_ini)=").append(getControlReg().getEjercicio()).append(" )\n");
            sentencia.append(")  \n");
            sentencia.append("or (substr(cuenta_contable,1,5) between '0000' and '9999'  \n");
            //sentencia.append("and substr(cuenta_contable,6,4) between '{6}' and '".concat(programaFormateado.equals("0000")?"9999":programaFormateado).concat("' \n "));
            sentencia.append(cadenaPrograma);
            sentencia.append("and substr(cuenta_contable,10,4) between '0000' and '9999' \n");
            sentencia.append("and substr(cuenta_contable,14,4) between '0000' and '9999' and nivel=4  \n");
            sentencia.append(")  \n");
        } else {
            if (Integer.valueOf(getNivel()) == 3) {
                sentencia.append("and (\n");
                sentencia.append("(substr(cuenta_contable,1,5) between '0000' and '9999' and nivel=1 \n");
                sentencia.append("and substr(cuenta_contable,1,5) in (select distinct substr(cuenta_contable,1,5) from RF_TR_CUENTAS_CONTABLES \n");
                //sentencia.append("where substr(cuenta_contable,6,4) between '{6}' and '".concat(programaFormateado.equals("0000")?"9999":programaFormateado).concat("' \n"));
                sentencia.append("where id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta()).append(" ").append(cadenaPrograma);
                sentencia.append("and substr(cuenta_contable,10,4) between '").append(getUnidad()).append("' and '").append(getUnidad()).append("' \n");
                sentencia.append("and substr(cuenta_contable,14,4) between '").append(getAmbito()).append("' and '9999' \n");
                sentencia.append("and extract(year from fecha_vig_ini)=").append(getControlReg().getEjercicio()).append(" )\n");
                sentencia.append(") ");
                sentencia.append("or (substr(cuenta_contable,1,5) between '0000' and '9999'  \n");
                //sentencia.append("and substr(cuenta_contable,6,4) between '{6}' and '".concat(programaFormateado.equals("0000")?"9999":programaFormateado).concat("' and nivel=2  \n"));
                sentencia.append(cadenaPrograma).append("and nivel=2 \n");
                sentencia.append("and substr(cuenta_contable,1,5) in (select distinct substr(cuenta_contable,1,5) from RF_TR_CUENTAS_CONTABLES \n");
                //sentencia.append("where substr(cuenta_contable,6,4) between '{6}' and '".concat(programaFormateado.equals("0000")?"9999":programaFormateado).concat("' \n"));
                sentencia.append("where id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta()).append(" ").append(cadenaPrograma);
                sentencia.append("and substr(cuenta_contable,10,4) between '").append(getUnidad()).append("' and '").append(getUnidad()).append("' \n");
                sentencia.append("and substr(cuenta_contable,14,4) between '").append(getAmbito()).append("' and '9999' \n");
                sentencia.append("and extract(year from fecha_vig_ini)=").append(getControlReg().getEjercicio()).append(" )\n");
                sentencia.append(") \n ");
                sentencia.append("or (substr(cuenta_contable,1,5) between '0000' and '9999' \n ");
                //sentencia.append("and substr(cuenta_contable,6,4) between '{6}' and '".concat(programaFormateado.equals("0000")?"9999":programaFormateado).concat("' \n"));
                sentencia.append(cadenaPrograma);
                sentencia.append("and substr(cuenta_contable,10,4) between '").append(getUnidad()).append("' and '").append(getUnidad()).append("' and nivel=3  \n");
                sentencia.append("and substr(cuenta_contable,1,5) in (select distinct substr(cuenta_contable,1,5) from RF_TR_CUENTAS_CONTABLES \n");
                //sentencia.append("where substr(cuenta_contable,6,4) between '{6}' and '".concat(programaFormateado.equals("0000")?"9999":programaFormateado).concat("' \n"));
                sentencia.append("where id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta()).append(" ").append(cadenaPrograma);
                sentencia.append("and substr(cuenta_contable,10,4) between '").append(getUnidad()).append("' and '").append(getUnidad()).append("' \n");
                sentencia.append("and substr(cuenta_contable,14,4) between '").append(getAmbito()).append("' and '9999' \n");
                sentencia.append("and extract(year from fecha_vig_ini)=").append(getControlReg().getEjercicio()).append(" )\n");
                sentencia.append(")  \n");
                sentencia.append("or (substr(cuenta_contable,1,5) between '0000' and '9999'  \n");
                //sentencia.append("and substr(cuenta_contable,6,4) between '{6}' and '".concat(programaFormateado.equals("0000")?"9999":programaFormateado).concat("' \n"));
                sentencia.append(cadenaPrograma);
                sentencia.append("and substr(cuenta_contable,10,4) between '").append(getUnidad()).append("' and '").append(getUnidad()).append("' \n");
                sentencia.append("and substr(cuenta_contable,14,4) between '0000' and '9999' and nivel=4  \n");
                sentencia.append(")  \n");
            } else {
                sentencia.append("and (\n");
                sentencia.append("(substr(cuenta_contable,1,5) between '0000' and '9999' and nivel=1 \n");
                sentencia.append("and substr(cuenta_contable,1,5) in (select distinct substr(cuenta_contable,1,5) from RF_TR_CUENTAS_CONTABLES \n");
                //sentencia.append("where substr(cuenta_contable,6,4) between '{6}' and '".concat(programaFormateado.equals("0000")?"9999":programaFormateado).concat("' \n"));
                sentencia.append("where id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta()).append(" ").append(cadenaPrograma);
                sentencia.append("and substr(cuenta_contable,10,4) between '").append(getUnidad()).append("' and '").append(getUnidad()).append("' \n");
                sentencia.append("and substr(cuenta_contable,14,4) between '").append(getAmbito()).append("' and '").append(getAmbito()).append("' \n");
                sentencia.append("and extract(year from fecha_vig_ini)=").append(getControlReg().getEjercicio()).append(" )\n");
                sentencia.append(") ");
                sentencia.append("or (substr(cuenta_contable,1,5) between '0000' and '9999'  \n");
                //sentencia.append("and substr(cuenta_contable,6,4) between '{6}' and '".concat(programaFormateado.equals("0000")?"9999":programaFormateado).concat("' and nivel=2  \n"));
                sentencia.append(cadenaPrograma).append("and nivel=2 \n");
                sentencia.append("and substr(cuenta_contable,1,5) in (select distinct substr(cuenta_contable,1,5) from RF_TR_CUENTAS_CONTABLES \n");
                sentencia.append("where id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta()).append(" ").append(cadenaPrograma);
                sentencia.append("and substr(cuenta_contable,10,4) between '").append(getUnidad()).append("' and '").append(getUnidad()).append("'\n ");
                sentencia.append("and substr(cuenta_contable,14,4) between '").append(getAmbito()).append("' and '").append(getAmbito()).append("' \n");
                sentencia.append("and extract(year from fecha_vig_ini)=").append(getControlReg().getEjercicio()).append(" )\n");
                sentencia.append(")  \n");
                sentencia.append("or (substr(cuenta_contable,1,5) between '0000' and '9999'  \n");
                //sentencia.append("and substr(cuenta_contable,6,4) between '{6}' and '".concat(programaFormateado.equals("0000")?"9999":programaFormateado).concat("' \n"));
                sentencia.append(cadenaPrograma);
                sentencia.append("and substr(cuenta_contable,10,4) between '").append(getUnidad()).append("' and '").append(getUnidad()).append("' and nivel=3  \n");
                sentencia.append("and substr(cuenta_contable,1,5) in (select distinct substr(cuenta_contable,1,5) from RF_TR_CUENTAS_CONTABLES \n");
                sentencia.append("where id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta()).append(" ").append(cadenaPrograma);
                sentencia.append("and substr(cuenta_contable,10,4) between '").append(getUnidad()).append("' and '").append(getUnidad()).append("' \n");
                sentencia.append("and substr(cuenta_contable,14,4) between '").append(getAmbito()).append("' and '").append(getAmbito()).append("' \n");
                sentencia.append("and extract(year from fecha_vig_ini)=").append(getControlReg().getEjercicio()).append(" )\n");
                sentencia.append(")  ");
                sentencia.append("or (substr(cuenta_contable,1,5) between '0000' and '9999'  \n");
                //sentencia.append("and substr(cuenta_contable,6,4) between '{6}' and '".concat(programaFormateado.equals("0000")?"9999":programaFormateado).concat("' \n"));
                sentencia.append(cadenaPrograma);
                sentencia.append("and substr(cuenta_contable,10,4) between '").append(getUnidad()).append("' and '").append(getUnidad()).append("' \n");
                sentencia.append("and substr(cuenta_contable,14,4) between '").append(getAmbito()).append("' and '").append(getAmbito()).append("' and nivel=4  \n");
                sentencia.append(")  ");
            }
        }
        sentencia.append("or (substr(cuenta_contable,1,5) between '0000' and '9999'  \n".concat(sentenciaNivel.toString()));
        sentencia.append("and substr(cuenta_contable,18,4) between '0000' and '9999' and nivel=5  \n");
        sentencia.append(") \n ");
        sentencia.append("or (substr(cuenta_contable,1,5) between '0000' and '9999'  \n".concat(sentenciaNivel.toString()));
        sentencia.append("and substr(cuenta_contable,18,4) between '0000' and '9999' \n");
        sentencia.append("and substr(cuenta_contable,22,4) between '0000' and '9999' and nivel=6  \n");
        sentencia.append(") \n ");
        sentencia.append("or (substr(cuenta_contable,1,5) between '0000' and '9999'  \n".concat(sentenciaNivel.toString()));
        sentencia.append("and substr(cuenta_contable,18,4) between '0000' and '9999' \n");
        sentencia.append("and substr(cuenta_contable,22,4) between '0000' and '9999'  \n");
        sentencia.append("and substr(cuenta_contable,26,4) between '0000' and '9999' and nivel=7  \n");
        sentencia.append(")  \n");
        sentencia.append("or (substr(cuenta_contable,1,5) between '0000' and '9999'  \n".concat(sentenciaNivel.toString()));
        sentencia.append("and substr(cuenta_contable,18,4) between '0000' and '9999' \n");
        sentencia.append("and substr(cuenta_contable,22,4) between '0000' and '9999' \n ");
        sentencia.append("and substr(cuenta_contable,26,4) between '0000' and '9999'  \n");
        sentencia.append("and substr(cuenta_contable,30,4) between '0000' and '9999' and nivel=8 ");
        sentencia.append(")\n ");
        //sentencia.append(")\n ");  
        sentencia.append(") order by \n");
        sentencia.append("cuenta_contable\n");
        sentencia.append(") cta where cta.saldo_actual <> 0 \n");
        /*formatos = new Formatos(sentencia.toString(),mesActual, nivel,getEjercicio(), getIdCatalogoCuenta(),getUnidad(),getAmbito(),programaFormateado,mesAntCargoAcum,mesAntAbonoAcum,mesActCargo,mesActAbono);
         sentenciaImpl.append(formatos.getSentencia());*/
        //System.out.println(sentenciaImpl);
        return sentencia;
    }

    public StringBuffer situacionFinancieraComparativo(String genero, String naturaleza, Boolean cajaBanco, Boolean isExcluye, String mesActual, String mesAnterior, int ejercicioCompara) {
        StringBuffer sentencia = new StringBuffer();
        StringBuffer sentenciaSaldo;
        StringBuffer sentenciaSaldoAnt;
        //StringBuffer sentenciaClasifica = new StringBuffer();
        sentenciaSaldo = seleccionaSaldos(cajaBanco, false, mesActual, mesAnterior, ejercicioCompara, false, genero, naturaleza);
        sentenciaSaldoAnt = seleccionaSaldosAnt(cajaBanco, true, mesActual, mesAnterior, ejercicioCompara, false, genero, naturaleza);
        //sentenciaClasifica = //formarSentenciaClasificacion(genero,naturaleza);
        //sentencia.append(" select saldoAnt.cuenta_mayor, ltrim(saldoAnt.descripcion) descripcion,\n");
        //sentencia.append("        saldoAnt.gen_descripcion,\n");
        //sentencia.append("        saldoAnt.gru_descripcion,\n");
        //sentencia.append("        decode(saldo.saldo,null,0,saldo.saldo) saldo, saldo_ant\n");


        sentencia.append(" select ");
        sentencia.append(" decode(saldoAnt.cuenta_mayor,null, saldo.cuenta_mayor, saldoAnt.cuenta_mayor) cuenta_mayor, ");
        sentencia.append(" decode(ltrim(saldoAnt.descripcion),null, ltrim(saldo.descripcion), ltrim(saldoAnt.descripcion)) descripcion,");
        sentencia.append(" decode(saldoAnt.gen_descripcion, null, saldo.gen_descripcion, saldoAnt.gen_descripcion ) gen_descripcion,");
        sentencia.append(" decode(saldoAnt.gru_descripcion, null, saldo.gru_descripcion, saldoAnt.gru_descripcion) gru_descripcion,");
        sentencia.append(" decode(saldo.saldo,null,0,saldo.saldo) saldo, saldo_ant");
        sentencia.append(" from (--situacionFinancieraComparativo.sentenciaSaldo\n");
        sentencia.append(sentenciaSaldo);
        sentencia.append(") saldo \n");
        sentencia.append(" inner join \n");
        sentencia.append("(");
        if (getUnidad().equals("0117") && getPrograma().equals("'0000'")) {
            sentencia.append("select saldoAnt.cuenta_mayor,\n");
            sentencia.append(" upper(saldoAnt.descripcion) descripcion,\n");
            sentencia.append(" upper(saldoAnt.gen_descripcion) gen_descripcion,\n");
            sentencia.append(" upper(saldoAnt.gru_descripcion) gru_descripcion, \n");
            sentencia.append(" saldoAnt.nivel,saldoAnt.cuenta_mayor_id, sum(saldoAnt.saldo_ant) saldo_ant\n");
            sentencia.append(" from(");
        }
        sentencia.append("--situacionFinancieraComparativo.sentenciaSaldoAnt\n");
        sentencia.append(sentenciaSaldoAnt);
        sentencia.append(") saldoAnt \n");
        if (getUnidad().equals("0117") && getPrograma().equals("'0000'")) {

            sentencia.append("group by saldoAnt.cuenta_mayor,  \n");
            sentencia.append("         saldoAnt.descripcion, \n");
            sentencia.append("          saldoAnt.gen_descripcion, \n");
            sentencia.append("         saldoAnt.gru_descripcion, \n");
            sentencia.append("         saldoAnt.nivel, saldoAnt.cuenta_mayor_id  \n");
            sentencia.append(") saldoAnt  \n");
        }
        sentencia.append("   on saldo.cuenta_mayor_id = saldoAnt.cuenta_mayor_id(+)\n"
                + "                 and (saldo.saldo <> 0 or  saldoant.saldo_ant <> 0)");

        if (isExcluye) {
            sentencia.append(" where saldoAnt.cuenta_mayor not in(11209,11305,12501,12502,21103,23113)");
        }
        sentencia.append("  order by saldo.cuenta_mayor");
        return sentencia;
    }

    public StringBuffer situacionFinanciera(String genero, String naturaleza, Boolean cajaBanco, String mesActual, String mesAnterior, int ejercicioCompara) {
        StringBuffer sentencia = new StringBuffer();
        StringBuffer sentenciaSaldo;
        //StringBuffer sentenciaClasifica = new StringBuffer();
        sentenciaSaldo = seleccionaSaldos(cajaBanco, false, mesActual, mesAnterior, ejercicioCompara, false, genero, naturaleza);
        //sentenciaClasifica = formarSentenciaClasificacion(genero,naturaleza);


        sentencia.append("select ");
        sentencia.append("saldo.cuenta_mayor, ltrim(saldo.descripcion) descripcion, saldo.gen_descripcion,");
        sentencia.append("saldo.gru_descripcion, saldo.saldo");
        sentencia.append(" from ");
        sentencia.append("(");
        sentencia.append(sentenciaSaldo);
        sentencia.append(") saldo ");
        sentencia.append(" where saldo.saldo <> 0.00 ");
        sentencia.append(" order by ");
        sentencia.append(" saldo.cuenta_mayor");
        return sentencia;
    }

    public StringBuffer seleccionaSaldos(Boolean cajaBanco, Boolean isMesAnterior, String mesActual, String mesAnterior, int ejercicioCompara, boolean resultadoEjercicio, String genero, String naturaleza) {
        StringBuffer sentencia = new StringBuffer();
        String cargoAcum;
        String abonoAcum;
        String cargo;
        String abono;
        String banco = cajaBanco == null ? "null" : cajaBanco ? "1" : "0";
        setNivel(!getPrograma().equals("'0000'") && getUnidad().equals("0000") && getAmbito().equals("0000") ? "2" : getNivel());
        Fecha fechaPeriodo = new Fecha(getControlReg().getEjercicio() + mesActual + "01", "/");
        mesActual = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodo.getMes() - 1);
        if (isMesAnterior) {
            mesAnterior = sia.libs.formato.Fecha.getNombreMesCorto(Integer.valueOf(mesAnterior) - 1);
        } else {
            fechaPeriodo.addMeses(-1);
            mesAnterior = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodo.getMes() - 1);
        }
        abono = construirMes(mesActual, "abono");
        cargo = construirMes(mesActual, "cargo");
        if (mesAnterior.equals("DIC")) {
            abonoAcum = construirMes("ENE", "abono_ini");
            cargoAcum = construirMes("ENE", "cargo_ini");
        } else {
            abonoAcum = construirMes(mesAnterior, "abono_acum");
            cargoAcum = construirMes(mesAnterior, "cargo_acum");
        }
        sentencia.append("select ");
        sentencia.append("            clas.cuenta_mayor,\n"
                + "            upper(clas.descripcion) descripcion,\n"
                + "            upper(gen.descripcion) gen_descripcion,\n"
                + "            upper(gru.descripcion) gru_descripcion, \n"
                + "                   ctas.nivel,\n"
                + "                    ctas.cuenta_mayor_id,");


        if (cajaBanco != null ? cajaBanco : false) {
            sentencia.append("(case when clas.naturaleza ='D' ");
            sentencia.append("then sum(ctas.").append(cargoAcum);
            sentencia.append(" - ctas.").append(abonoAcum).append(") ");
            sentencia.append("else sum(ctas.").append(abonoAcum);
            sentencia.append(" - ctas.").append(cargoAcum).append(") end) saldo \n");
        } else {
            sentencia.append("(case when clas.naturaleza ='D' \n");
            sentencia.append("then sum((ctas.").append(cargoAcum);
            sentencia.append(" + ctas.").append(cargo).append(") - (ctas.").append(abonoAcum);
            sentencia.append(" + ctas.").append(abono).append(")) \n");
            sentencia.append("else sum((ctas.").append(abonoAcum);
            sentencia.append(" + ctas.").append(abono).append(") - (ctas.").append(cargoAcum);
            sentencia.append(" + ctas.").append(cargo).append(")) end) saldo \n");
        }
        sentencia.append(" from ");
        sentencia.append(" rf_tr_cuentas_contables ctas ");
        sentencia.append("inner join rf_tc_clasificador_cuentas clas \n");
        sentencia.append("on ctas.cuenta_mayor_id = clas.cuenta_mayor_id \n");
        sentencia.append("and nivel = ").append(getNivel());
        if (!getPrograma().equals("'0000'")) {
            sentencia.append("and substr(cuenta_contable,1,5) in ");
            sentencia.append("(select distinct substr(cuenta_contable,1,5) ");
            sentencia.append("from RF_TR_CUENTAS_CONTABLES  ");
            sentencia.append("where substr(cuenta_contable,6,4) in (").append(getPrograma());
            sentencia.append(") and id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta());
            sentencia.append("and extract(year from fecha_vig_ini) = ").append(getControlReg().getEjercicio()).append(" )");
            sentencia.append("and substr(cuenta_contable, 6, 4) in (").append(getPrograma()).append(")");
        }
        if (Integer.valueOf(getNivel()) >= 2) {
            sentencia.append("and substr(cuenta_contable, 10, 4) = '").append(getUnidad()).append("' ");
            sentencia.append("and substr(cuenta_contable, 14, 4) = '").append(getAmbito()).append("' ");
        }

        if (!mesAnterior.equals("DIC") && getControlReg().getEjercicio() != ejercicioCompara && cajaBanco != null ? cajaBanco : false) {
            sentencia.append("and extract(year from ctas.fecha_vig_ini)= ").append(ejercicioCompara);
        } else {
            sentencia.append("and extract(year from ctas.fecha_vig_ini)= ").append(getControlReg().getEjercicio());
        }
        sentencia.append("and id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta());
        sentencia.append("and (( ").append(banco).append(" is null) ");
        sentencia.append("or (").append(banco).append("=1 and (clas.cuenta_mayor = 11201 or clas.cuenta_mayor = 11203)) ");
        sentencia.append("or (").append(banco).append("=0 and clas.cuenta_mayor not in (11201,11203) ");
            sentencia.append("and clas.id_grupo in (1,3) )) ");

        sentencia.append("           inner join rf_tc_genero_clasf_cta gen on clas.id_genero = gen.id_genero");
        sentencia.append("\n         inner join rf_tc_grupo_clasf_cta gru on clas.id_grupo = gru.id_grupo");
        sentencia.append("\n                           and clas.id_genero = gru.id_genero");
        sentencia.append("\n                           and clas.id_genero in (").append(genero).append(")\n");

        if (naturaleza == null) {
            sentencia.append(" and clas.naturaleza is not null ");
        } else {
            sentencia.append(" and clas.naturaleza in ('").append(naturaleza).append("') ");
        }

        sentencia.append(" \ngroup by \n");
        sentencia.append("clas.cuenta_mayor,\n"
                + "            upper(clas.descripcion) ,\n"
                + "            upper(gen.descripcion) ,\n"
                + "            upper(gru.descripcion) , ");
        sentencia.append(" nivel, ctas.cuenta_mayor_id, clas.naturaleza ");
        if (resultadoEjercicio && (!getPrograma().contains("'0001'") && !getPrograma().contains("'0000'"))) {
            sentencia.append(" union");
            sentencia.append(" select 4 nivel, 264 cuenta_mayor_id, 0 saldo_ant from dual");
        }
        return sentencia;
    } //seleccionaSaldos

    public StringBuffer formarSentenciaClasificacion(String genero, String naturaleza) {
        //pendiente revisar
        String gen[] = genero.split(",");
        StringBuilder sentencia = new StringBuilder();
        StringBuffer sentenciaImpl = new StringBuffer();
        Formatos formatos;
        sentencia.append("select ");
        sentencia.append("ctas.cuenta_mayor_id,ctas.cuenta_mayor,");
        sentencia.append("upper(ctas.descripcion) descripcion, ctas.naturaleza,upper(gen.descripcion) gen_descripcion,");
        sentencia.append("upper(gru.descripcion) gru_descripcion");
        sentencia.append(" from ");
        sentencia.append("rf_tc_clasificador_cuentas ctas ");
        sentencia.append("inner join rf_tc_genero_clasf_cta gen ");
        sentencia.append("on ctas.id_genero= gen.id_genero ");
        sentencia.append("inner join rf_tc_grupo_clasf_cta gru ");
        sentencia.append("on ctas.id_grupo= gru.id_grupo ");
        sentencia.append("and ctas.id_genero=gru.id_genero ");
        sentencia.append("and ctas.id_genero in ({0}) ");
        if (gen.length == 3 && (gen[1].equals("41102") || gen[2].equals("41103"))) {
            sentencia.append("and ctas.cuenta_mayor not in (41102,41103) ");
        }
        if (gen.length == 4 && (gen[1].equals("41202") || gen[2].equals("41101") || gen[2].equals("41201"))) {
            sentencia.append("and ctas.cuenta_mayor not in (41202,41101,41201) ");
        }
        if (naturaleza == null) {
            sentencia.append("and ctas.naturaleza is not null ");
        } else {
            sentencia.append("and ctas.naturaleza in ('{1}') ");
        }
        sentencia.append(" order by ");
        sentencia.append("ctas.cuenta_mayor");
        if (gen.length == 2) {
            if (gen[1].equals("41202")) {
                formatos = new Formatos(sentencia.toString(), String.valueOf(gen[0]), String.valueOf(naturaleza));
            } else {
                formatos = new Formatos(sentencia.toString(), String.valueOf(gen[0].concat(",").concat(gen[1])), String.valueOf(naturaleza));
            }
        } else {
            formatos = new Formatos(sentencia.toString(), String.valueOf(gen[0]), String.valueOf(naturaleza));
        }
        sentenciaImpl.append(formatos.getSentencia());
        return sentenciaImpl;
    } //formarSentenciaClasificacion

    public StringBuffer cambiosSituacionFinanciera(String genero, String naturaleza, Boolean cajaBanco, Boolean variacion, Boolean fijo, Boolean isMesAnterior, String mesActual, String mesAnterior, int ejercicioCompara) {
        StringBuffer sentencia = new StringBuffer();
        StringBuffer sentenciaSaldo;
        StringBuffer sentenciaClasifica = new StringBuffer();
        StringBuffer sentenciaSaldosVariacionAnterior;
        sentenciaClasifica = formarSentenciaClasificacion(genero, naturaleza);
        if (variacion) {//adecuar para comparar con cualquier mes y ejercicio
            sentenciaSaldo = saldosVariacion(cajaBanco, fijo, mesActual, ejercicioCompara, genero, naturaleza);
            sentenciaSaldosVariacionAnterior = saldosVariacionAnterior(cajaBanco, fijo, mesAnterior, ejercicioCompara, genero, naturaleza);
            /*sentencia.append("select * from ( ");
             sentencia.append("select ");
        
             sentencia.append("ctas.cuenta_mayor, ctas.descripcion, ctas.gen_descripcion,");
             sentencia.append("ctas.gru_descripcion,(saldo.saldo - saldoant.saldo_ant) saldo");
        
             sentencia.append(" from ");*/

            sentencia.append(" select saldoAnt.cuenta_mayor, ltrim(saldoAnt.descripcion) descripcion,\n");
            sentencia.append("        saldoAnt.gen_descripcion,\n");
            sentencia.append("        saldoAnt.gru_descripcion,\n");
            sentencia.append("        (decode(saldo.saldo,null,0,saldo.saldo) - saldoant.saldo_ant) saldo");
            sentencia.append(" from (");
            //sentencia.append("(");
            sentencia.append(sentenciaSaldo);
            sentencia.append(") saldo ");
            sentencia.append(" inner join ");
            sentencia.append("(");
            if ((getUnidad().equals("0117") || getUnidad().equals("0118")) && getPrograma().equals("'0000'")) {
                sentencia.append("select saldoAnt.cuenta_mayor,\n");
                sentencia.append(" upper(saldoAnt.descripcion) descripcion,\n");
                sentencia.append(" upper(saldoAnt.gen_descripcion) gen_descripcion,\n");
                sentencia.append(" upper(saldoAnt.gru_descripcion) gru_descripcion, \n");
                sentencia.append(" saldoAnt.nivel,saldoAnt.cuenta_mayor_id, sum(saldoAnt.saldo_ant) saldo_ant\n");
                sentencia.append(" from(\n");
            }
            //sentencia.append(" from(\n");  
            sentencia.append(sentenciaSaldosVariacionAnterior);
            sentencia.append(") saldoant ");
            if ((getUnidad().equals("0117") || getUnidad().equals("0118")) && getPrograma().equals("'0000'")) {
                sentencia.append("group by saldoAnt.cuenta_mayor,  \n");
                sentencia.append("         saldoAnt.descripcion, \n");
                sentencia.append("          saldoAnt.gen_descripcion, \n");
                sentencia.append("         saldoAnt.gru_descripcion, \n");
                sentencia.append("         saldoAnt.nivel, saldoAnt.cuenta_mayor_id  \n");
                sentencia.append(") saldoAnt  \n");
            }
            sentencia.append("   on saldo.cuenta_mayor_id(+) = saldoAnt.cuenta_mayor_id\n"
                    + "                 and (saldo.saldo <> 0)");
            sentencia.append(" order by ");
            sentencia.append(" cuenta_mayor");
        } else {
            sentenciaSaldosVariacionAnterior = saldosVariacionAnterior(cajaBanco, fijo, mesAnterior, ejercicioCompara, genero, naturaleza);
            sentencia.append("select saldoAnt.cuenta_mayor,\n");
            sentencia.append(" upper(saldoAnt.descripcion) descripcion,\n");
            sentencia.append(" upper(saldoAnt.gen_descripcion) gen_descripcion,\n");
            sentencia.append(" upper(saldoAnt.gru_descripcion) gru_descripcion, \n");
            sentencia.append(" saldoAnt.nivel,saldoAnt.cuenta_mayor_id, sum(saldoAnt.saldo_ant) saldo\n");
            sentencia.append(" from(\n");
            sentencia.append(sentenciaSaldosVariacionAnterior);
            sentencia.append(") saldoant ");
            sentencia.append("group by saldoAnt.cuenta_mayor,  \n");
            sentencia.append("         saldoAnt.descripcion, \n");
            sentencia.append("          saldoAnt.gen_descripcion, \n");
            sentencia.append("         saldoAnt.gru_descripcion, \n");
            sentencia.append("         saldoAnt.nivel, saldoAnt.cuenta_mayor_id  \n");
            //torres--sentenciaSaldo = seleccionaSaldos(cajaBanco, isMesAnterior,mesActual,mesAnterior,ejercicioCompara,false,genero,naturaleza);
            //sentenciaClasifica = formarSentenciaClasificacion(genero,naturaleza);
            //torres--sentencia.append("select ");
            //torres--sentencia.append("ctas.cuenta_mayor, ctas.descripcion, ctas.gen_descripcion,");
            //torres--sentencia.append("ctas.gru_descripcion, saldo.saldo");
            //torres--sentencia.append(" from ");
            //torres--sentencia.append("(");
            //torres--sentencia.append(sentenciaSaldo);
            //torres--sentencia.append(") saldo ");
            //torres--sentencia.append(" inner join ");  
            //torres--sentencia.append("(");  
            //torres--sentencia.append(sentenciaClasifica);  
            //torres--sentencia.append(") ctas ");  
            //torres--sentencia.append(" on saldo.cuenta_mayor_id = ctas.cuenta_mayor_id ");  
            //torres--sentencia.append(" and saldo <> 0.00 ");  
            //torres--sentencia.append(" order by ");  
            //torres--sentencia.append(" ctas.cuenta_mayor"); 
        }
        return sentencia;
    }

    public StringBuffer variacionECSF(String fecha_consolidacion, String mesCompara, int ejercicioCompara) {
        StringBuffer sentencia = new StringBuffer();
        Fecha fechaPeriodo = new Fecha(fecha_consolidacion, "/");
        String mesActual = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodo.getMes() - 1);
        fechaPeriodo.addMeses(-1);
        String mesInmediatoAnterior = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodo.getMes() - 1);//mes que se usa para el saldo actual
        mesCompara = sia.libs.formato.Fecha.getNombreMesCorto(Integer.valueOf(mesCompara) - 1);//mes que se usa para el saldo actual
        String unidadAnt;
        if (mesCompara.equals("DIC") && ejercicioCompara < getControlReg().getEjercicio()) {
            unidadAnt = (getUnidad().equals("0117") ? "0104" : getUnidad().equals("0118") ? "0108" : getUnidad());
        } else {
            unidadAnt = getUnidad();
        }
        /*mesActual = sia.libs.formato.Fecha.getNombreMesCorto(Integer.valueOf(mesActual)-1);
         //mesAnterior = sia.libs.formato.Fecha.getNombreMesCorto(Integer.valueOf(mesAnterior)-1);
         fechaPeriodo.addMeses(-1);
         mesAnterior = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodo.getMes() - 1);*/
        String cargoAcum;
        String abonoAcum;
        String abono;
        String cargo;
        setNivel(!getPrograma().equals("'0000'") && getUnidad().equals("0000") && getAmbito().equals("0000") ? "2" : getNivel());
        if (mesInmediatoAnterior.equals("DIC")) {
            abonoAcum = construirMes("ene", "abono_ini");
            cargoAcum = construirMes("ene", "cargo_ini");
        } else {
            abonoAcum = construirMes(mesInmediatoAnterior, "abono_acum");
            cargoAcum = construirMes(mesInmediatoAnterior, "cargo_acum");
        }
        abono = construirMes(mesActual, "abono");
        cargo = construirMes(mesActual, "cargo");
        sentencia.append("\n ");
        sentencia.append("\nSELECT ");
        sentencia.append("\n saldo.cuenta, ");
        sentencia.append("\n decode(saldo.cuenta,'41102','RESULTADOS DEL EJERCICIO','41103','RECTIFICACIONES A RESULTADO',saldo.concepto) concepto, ");
        sentencia.append("\n SUM(saldo.saldo_actual) saldo, sum(saldoAnt.saldo_anterior) saldo_ant, ");
        //sentencia.append("\n (SUM(saldo)-(case when cuenta = '41103' then sum((saldo_ant)*-1) else sum(saldo_ant) end)) total  ");
        sentencia.append("\n (SUM(saldo.saldo_actual)-SUM(saldoAnt.saldo_anterior)) total ");
        sentencia.append("\n FROM (  ");
        sentencia.append("\n SELECT tc.cuenta_mayor_id,");
        sentencia.append("\n     tc.naturaleza,  ");
        sentencia.append("\n     decode(SUBSTR(cc.cuenta_contable,    0,    5),'51201','41102','52101','41102','52202','41102','53105','41102','31105','41102','31207','41102','31106','41103','52102','41103',SUBSTR(cc.cuenta_contable,    0,    5)) cuenta,  ");
        sentencia.append("\n     UPPER(tc.descripcion) concepto, ");
        sentencia.append("\n   (CASE WHEN(SUBSTR(cc.cuenta_contable,    0,    5) = '31207' OR SUBSTR(cc.cuenta_contable,    0,    5) = '51201' OR SUBSTR(cc.cuenta_contable,    0,    5) = '53105') THEN SUM(cc.").append(abonoAcum);
        sentencia.append(" + cc.").append(abono).append(") ");
        sentencia.append("\n     ELSE(CASE WHEN(SUBSTR(cc.cuenta_contable,    0,    5) = '31105' OR SUBSTR(cc.cuenta_contable,    0,    5) = '52101' OR SUBSTR(cc.cuenta_contable,    0,    5) = '52202'        OR SUBSTR(cc.cuenta_contable,   0,   5) = '31106' ");
        sentencia.append("\n     OR SUBSTR(cc.cuenta_contable,   0,   5) = '52102') THEN SUM((cc.").append(cargoAcum);
        sentencia.append(" + cc.").append(cargo).append(") *-1) END) END) saldo_actual ");
        sentencia.append("\n FROM rf_tr_cuentas_contables cc ");
        sentencia.append("\n INNER JOIN rf_tc_clasificador_cuentas tc ON cc.cuenta_mayor_id = tc.cuenta_mayor_id ");
        sentencia.append("\n WHERE cc.nivel =").append(getNivel());
        sentencia.append(" AND EXTRACT(YEAR FROM cc.fecha_vig_ini) = ").append(getControlReg().getEjercicio());
        sentencia.append(" AND cc.id_catalogo_cuenta =").append(getControlReg().getIdCatalogoCuenta());
        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n   AND SUBSTR(cuenta_contable,    6,    4) in (").append(getPrograma()).append(") ");
        }
        sentencia.append("\n   AND SUBSTR(cuenta_contable,    10,    4) = '").append(getUnidad()).append("' ");
        sentencia.append("\n   AND SUBSTR(cuenta_contable,    14,    4) = '").append(getAmbito()).append("' ");
        if (mesCompara.equals("DIC")) {
            sentencia.append("\n   AND SUBSTR(cc.cuenta_contable,    0,    5) in ('31207','51201','53105','31105','52101','52202','31106','52102','41102','41103') ");
        } else {
            sentencia.append("\n   AND SUBSTR(cc.cuenta_contable,    0,    5) in ('31207','51201','53105','31105','52101','52202','31106','52102') ");
        }
        sentencia.append("\n GROUP BY  tc.cuenta_mayor_id,tc.naturaleza,");
        sentencia.append("\n   SUBSTR(cc.cuenta_contable,    0,    5), ");
        sentencia.append("\n   tc.descripcion )saldo");
        sentencia.append("\n   inner join ");
        sentencia.append("\n   (SELECT tc.cuenta_mayor_id,");
        sentencia.append("\n   tc.naturaleza,  ");
        sentencia.append("\n   decode(SUBSTR(cc.cuenta_contable,    0,    5),'51201','41102','52101','41102','52202','41102','53105','41102','31105','41102','31207','41102','31106','41103','52102','41103',SUBSTR(cc.cuenta_contable,    0,    5)) cuenta,  ");
        sentencia.append("\n   UPPER(tc.descripcion) concepto, ");
        //mesCompara mes seleccionado en el filtro para calcular saldo anterior (mes a comparar)
        if (mesCompara.equals("DIC")) {
            abonoAcum = construirMes("ene", "abono_ini");
            cargoAcum = construirMes("ene", "cargo_ini");
            sentencia.append("\n(CASE WHEN SUBSTR(cc.cuenta_contable,    0,    5) = '41103' THEN SUM((cc.").append(cargoAcum);
            sentencia.append(" - cc.").append(abonoAcum).append(")* -1 ) ");
            sentencia.append("\n  else (CASE WHEN tc.naturaleza = 'D' THEN SUM(cc.").append(cargoAcum);
            sentencia.append(" - cc.").append(abonoAcum).append(")  ");
            sentencia.append("\n   ELSE SUM(cc.").append(abonoAcum);
            sentencia.append(" -cc.").append(cargoAcum).append(") end) END) saldo_anterior  ");
        } else {
            sentencia.append("\n (CASE WHEN(SUBSTR(cc.cuenta_contable,    0,    5) = '31207' OR SUBSTR(cc.cuenta_contable,    0,    5) = '51201' OR SUBSTR(cc.cuenta_contable,    0,    5) = '53105') THEN SUM(cc.").append(abonoAcum).append(") ");
            sentencia.append("\n    ELSE(CASE WHEN(SUBSTR(cc.cuenta_contable,    0,    5) = '31105' OR SUBSTR(cc.cuenta_contable,    0,    5) = '52101' OR SUBSTR(cc.cuenta_contable,    0,    5) = '52202'        OR SUBSTR(cc.cuenta_contable,   0,   5) = '31106' ");
            sentencia.append("\n    OR SUBSTR(cc.cuenta_contable,   0,   5) = '52102') THEN SUM((cc.").append(cargoAcum).append(") *-1) END) END) saldo_anterior ");
        }
        sentencia.append("\n   FROM rf_tr_cuentas_contables cc ");
        sentencia.append("\n   INNER JOIN rf_tc_clasificador_cuentas tc ON cc.cuenta_mayor_id = tc.cuenta_mayor_id ");
        sentencia.append("\n   WHERE cc.nivel =").append(getNivel());
        sentencia.append(" AND EXTRACT(YEAR FROM cc.fecha_vig_ini) = ").append(getControlReg().getEjercicio());
        sentencia.append(" AND cc.id_catalogo_cuenta =").append(getControlReg().getIdCatalogoCuenta());
        if (!getPrograma().equals("'0000'")) {
            sentencia.append("\n   AND SUBSTR(cuenta_contable,    6,    4) in (").append(getPrograma()).append(") ");
        }
        sentencia.append("\n   AND SUBSTR(cuenta_contable,    10,    4) = '").append(unidadAnt).append("' ");
        sentencia.append("\n   AND SUBSTR(cuenta_contable,    14,    4) = '").append(getAmbito()).append("' ");
        if (mesCompara.equals("DIC")) {
            sentencia.append("\n   AND SUBSTR(cc.cuenta_contable,    0,    5) in ('31207','51201','53105','31105','52101','52202','31106','52102','41102','41103') ");
        } else {
            sentencia.append("\n   AND SUBSTR(cc.cuenta_contable,    0,    5) in ('31207','51201','53105','31105','52101','52202','31106','52102') ");
        }
        sentencia.append("\n   GROUP BY  tc.cuenta_mayor_id,tc.naturaleza, ");
        sentencia.append("\n SUBSTR(cc.cuenta_contable,    0,    5), ");
        sentencia.append("\n tc.descripcion) saldoAnt on saldo.cuenta_mayor_id=saldoAnt.cuenta_mayor_id ");
        sentencia.append("\n group by ");
        sentencia.append("\n saldo.cuenta, ");
        sentencia.append("\n decode(saldo.cuenta,'41102','RESULTADOS DEL EJERCICIO','41103','RECTIFICACIONES A RESULTADO',saldo.concepto) ");
        sentencia.append("\n ORDER BY saldo.cuenta ");
        return sentencia;
    }

    public StringBuffer saldosVariacion(Boolean cajaBanco, Boolean fijo, String mesActual, int ejercicioCompara, String genero, String naturaleza) {
        StringBuffer sentencia = new StringBuffer();
        Fecha fechaPeriodo = new Fecha(getControlReg().getEjercicio() + mesActual + "01", "/");
        mesActual = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodo.getMes() - 1);
        fechaPeriodo.addMeses(-1);
        String mesAnterior = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodo.getMes() - 1);
        String cargoAcum;
        String abonoAcum;
        String cargo;
        String abono;
        String banco = cajaBanco == null ? "null" : cajaBanco ? "1" : "0";
        setNivel(!getPrograma().equals("'0000'") && getUnidad().equals("0000") && getAmbito().equals("0000") ? "2" : getNivel());
        abono = construirMes(mesActual, "abono");
        cargo = construirMes(mesActual, "cargo");
        if (mesAnterior.equals("DIC")) {
            abonoAcum = construirMes(mesActual, "abono_ini");
            cargoAcum = construirMes(mesActual, "cargo_ini");
        } else {
            abonoAcum = construirMes(mesAnterior, "abono_acum");
            cargoAcum = construirMes(mesAnterior, "cargo_acum");
        }
        sentencia.append("select ");
        // sentencia.append("ctas.nivel, ctas.cuenta_mayor_id, ");
        sentencia.append("            clas.cuenta_mayor,\n"
                + "            upper(clas.descripcion) descripcion,\n"
                + "            upper(gen.descripcion) gen_descripcion,\n"
                + "            upper(gru.descripcion) gru_descripcion, \n"
                + "                   ctas.nivel,\n"
                + "                    ctas.cuenta_mayor_id,");


        //if(mesActual.equals("ENE") && (mesAnterior.equals("DIC") && ejercicioCompara < getEjercicio())){
        sentencia.append("(case when clas.naturaleza ='D' ");
        sentencia.append("then sum((ctas.").append(cargoAcum).append(" + ctas.").append(cargo);
        sentencia.append(") - (ctas.").append(abonoAcum).append(" + ctas.").append(abono).append(")) ");
        sentencia.append("else sum((ctas.").append(abonoAcum).append(" + ctas.").append(abono).append(") - (ctas.").append(cargoAcum).append(" + ctas.").append(cargo).append(")) end) saldo ");
        /*}
         else{
         sentencia.append("(case when clas.naturaleza ='D' ");
         sentencia.append("then sum((ctas."+cargoAcum+"_cargo_acum + ctas."+cargo+"_cargo) - (ctas."+cargoAcum+"_abono_acum + ctas."+cargo+"_abono)) ");
         sentencia.append("else sum((ctas."+cargoAcum+"_abono_acum + ctas."+cargo+"_abono) - (ctas."+cargoAcum+"_cargo_acum + ctas."+cargo+"_cargo)) end) saldo ");
         }*/

        sentencia.append(" from ");
        sentencia.append(" rf_tr_cuentas_contables ctas ");
        sentencia.append("inner join rf_tc_clasificador_cuentas clas ");
        sentencia.append("on ctas.cuenta_mayor_id = clas.cuenta_mayor_id ");
        sentencia.append("and nivel = ").append(getNivel());

        if (!getPrograma().equals("'0000'")) {
            sentencia.append("and substr(cuenta_contable,1,5) in (select distinct substr(cuenta_contable,1,5) from RF_TR_CUENTAS_CONTABLES  where substr(cuenta_contable,6,4) in (").append(getPrograma()).append(") and id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta());
            sentencia.append("and extract(year from fecha_vig_ini)=").append(getControlReg().getEjercicio()).append(") ");
            sentencia.append("and substr(cuenta_contable, 6, 4) in (").append(getPrograma()).append(")");
        }
        if (Integer.valueOf(getNivel()) >= 2) {
            sentencia.append("and substr(cuenta_contable, 10, 4) = '").append(getUnidad()).append("' ");
            sentencia.append("and substr(cuenta_contable, 14, 4) = '").append(getAmbito()).append("' ");
        }
        sentencia.append("and extract(year from ctas.fecha_vig_ini)=").append(getControlReg().getEjercicio());
        sentencia.append("and ctas.id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta());
        sentencia.append("and (( ").append(banco).append(" is null) ");
        sentencia.append("or (").append(banco).append("=1 and (clas.cuenta_mayor = 11201 or clas.cuenta_mayor = 11203)) ");
        sentencia.append("or (").append(banco).append("=0 and clas.cuenta_mayor not in (11201,11203,41101) and clas.id_grupo in ");
        if (fijo) {
            sentencia.append("(2) )) ");
        } else {
            sentencia.append("(1,3) )) ");
        }
        sentencia.append("           inner join rf_tc_genero_clasf_cta gen on clas.id_genero = gen.id_genero");
        sentencia.append("\n           inner join rf_tc_grupo_clasf_cta gru on clas.id_grupo = gru.id_grupo");
        sentencia.append("\n                                                  and clas.id_genero = gru.id_genero");
        sentencia.append("\n                                                  and clas.id_genero in (");
        sentencia.append(genero).append(")\n");

        if (naturaleza == null) {
            sentencia.append(" and clas.naturaleza is not null ");
        } else {
            sentencia.append(" and clas.naturaleza in ('").append(naturaleza).append("') ");
        }

        sentencia.append(" \ngroup by \n");
        sentencia.append("clas.cuenta_mayor,\n"
                + "            upper(clas.descripcion) ,\n"
                + "            upper(gen.descripcion) ,\n"
                + "            upper(gru.descripcion) , ");
        sentencia.append(" nivel, ctas.cuenta_mayor_id, clas.naturaleza ");
        /*formatos=new Formatos(sentencia.toString(),mes_act, String.valueOf(nivel), String.valueOf(getEjercicio()), programa, unidad, ambito , cajaBanco==null?"null":cajaBanco?"1":"0",mes_ant, String.valueOf(this.getIdCatalogoCuenta()),
         mesActual,mesAnterior);
         sentenciaImpl.append(formatos.getSentencia());*/
        return sentencia;
    }

    public StringBuffer saldosVariacionAnterior(Boolean cajaBanco, Boolean fijo, String mesAnterior, int ejercicioCompara, String genero, String naturaleza) {
        StringBuffer sentencia = new StringBuffer();
        mesAnterior = sia.libs.formato.Fecha.getNombreMesCorto(Integer.valueOf(mesAnterior) - 1);
        String banco = cajaBanco == null ? "null" : cajaBanco ? "1" : "0";
        String cargoAcum;
        String abonoAcum;
        String unidadAnt;
        if (mesAnterior.equals("DIC") && ejercicioCompara < getControlReg().getEjercicio()) {
            unidadAnt = (getUnidad().equals("0117") ? "0104" : getUnidad().equals("0118") ? "0108" : getUnidad());
        } else {
            unidadAnt = getUnidad();
        }
        setNivel(!getPrograma().equals("'0000'") && getUnidad().equals("0000") && getAmbito().equals("0000") ? "2" : getNivel());
        if (mesAnterior.equals("DIC") && (ejercicioCompara < getControlReg().getEjercicio())) {
            abonoAcum = construirMes("ene", "abono_ini");
            cargoAcum = construirMes("ene", "cargo_ini");
        } else {
            abonoAcum = construirMes(mesAnterior, "abono_acum");
            cargoAcum = construirMes(mesAnterior, "cargo_acum");
        }
        sentencia.append("select ");
        sentencia.append("            clas.cuenta_mayor,\n"
                + "            upper(clas.descripcion) descripcion,\n"
                + "            upper(gen.descripcion) gen_descripcion,\n"
                + "            upper(gru.descripcion) gru_descripcion, \n"
                + "                   ctas.nivel,\n"
                + "                    ctas.cuenta_mayor_id,");
        sentencia.append("(case when clas.naturaleza ='D' ");
        sentencia.append("then sum(ctas.").append(cargoAcum).append(" - ctas.").append(abonoAcum).append(") ");
        sentencia.append("else sum(ctas.").append(abonoAcum).append(" - ctas.").append(cargoAcum).append(") end) saldo_ant ");
        sentencia.append(" from ");
        sentencia.append(" rf_tr_cuentas_contables ctas ");
        sentencia.append("inner join rf_tc_clasificador_cuentas clas ");
        sentencia.append("on ctas.cuenta_mayor_id = clas.cuenta_mayor_id ");
        sentencia.append("and nivel = ").append(getNivel());
        if (!getPrograma().equals("'0000'")) {
            sentencia.append("and substr(cuenta_contable,1,5) in (select distinct substr(cuenta_contable,1,5) from RF_TR_CUENTAS_CONTABLES  where substr(cuenta_contable,6,4) in (").append(getPrograma()).append(") and id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta());
            if (!programa.equals("'0005'") && getControlReg().getEjercicio() > 2010) {
                sentencia.append("and extract(year from fecha_vig_ini)=").append(getControlReg().getEjercicio()).append(") ");
            } else {
                sentencia.append("and extract(year from fecha_vig_ini)=").append(ejercicioCompara).append(") ");
            }
            sentencia.append("and substr(cuenta_contable, 6, 4) in (").append(getPrograma()).append(")");
        }
        if (getUnidad().equals("0117") && getPrograma().equals("'0000'")) {
            sentencia.append("and substr(cuenta_contable, 6, 4) <> '0005' ");
        }
        if (Integer.valueOf(getNivel()) >= 2) {
            sentencia.append("and substr(cuenta_contable, 10, 4) = '").append(unidadAnt).append("' ");
            sentencia.append("and substr(cuenta_contable, 14, 4) = '").append(getAmbito()).append("' ");
        }
        if (mesAnterior.equals("DIC") && (ejercicioCompara < getControlReg().getEjercicio())) {
            sentencia.append("and extract(year from ctas.fecha_vig_ini)=").append(getControlReg().getEjercicio()).append(" ");
        } else {
            sentencia.append("and extract(year from ctas.fecha_vig_ini)= ").append(ejercicioCompara);
        }
        sentencia.append("and ctas.id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta());
        sentencia.append("and (( ").append(banco).append(" is null) ");
        sentencia.append("or (").append(banco).append("=1 and (clas.cuenta_mayor = 11201 or clas.cuenta_mayor = 11203)) ");
        sentencia.append("or (").append(banco).append("=0 and clas.cuenta_mayor not in (11201,11203) and clas.id_grupo in ");
        if (fijo) {
            sentencia.append("(2) )) ");
        } else {
            sentencia.append("(1,3) )) ");
        }
        sentencia.append("           inner join rf_tc_genero_clasf_cta gen on clas.id_genero = gen.id_genero\n" + "           inner join rf_tc_grupo_clasf_cta gru on clas.id_grupo = gru.id_grupo\n" + "                                                  and clas.id_genero = gru.id_genero\n" + "                                                  and clas.id_genero in (").append(genero).append(")\n");
        if (naturaleza == null) {
            sentencia.append(" and clas.naturaleza is not null ");
        } else {
            sentencia.append(" and clas.naturaleza in ('").append(naturaleza).append("') ");
        }

        sentencia.append("\n group by \n");
        sentencia.append("clas.cuenta_mayor,\n"
                + "            upper(clas.descripcion) ,\n"
                + "            upper(gen.descripcion) ,\n"
                + "            upper(gru.descripcion) , ");
        sentencia.append(" nivel, ctas.cuenta_mayor_id, clas.naturaleza \n");

        // Este quere se integra cuando se compara dic ejercicio actual con dic ejercicio menor al actual y cuando sea programa 5 y unidad 117
        if (getUnidad().equals("0117") && getPrograma().equals("'0000'")) {
            sentencia.append("union \n ");
            sentencia.append("select ");
            sentencia.append("            clas.cuenta_mayor,\n"
                    + "            upper(clas.descripcion) descripcion,\n"
                    + "            upper(gen.descripcion) gen_descripcion,\n"
                    + "            upper(gru.descripcion) gru_descripcion, \n"
                    + "                   ctas.nivel,\n"
                    + "                    ctas.cuenta_mayor_id,");
            sentencia.append("(case when clas.naturaleza ='D' ");
            sentencia.append("then sum(ctas.").append(cargoAcum).append(" - ctas.").append(abonoAcum).append(") ");
            sentencia.append("else sum(ctas.").append(abonoAcum).append(" - ctas.").append(cargoAcum).append(") end) saldo_ant \n");
            sentencia.append(" from ");
            sentencia.append(" rf_tr_cuentas_contables ctas \n");
            sentencia.append("inner join rf_tc_clasificador_cuentas clas \n");
            sentencia.append("on ctas.cuenta_mayor_id = clas.cuenta_mayor_id \n");
            sentencia.append("and nivel = ").append(getNivel()).append("\n");
            if (!programa.equals("'0000'")) {
                sentencia.append("and substr(cuenta_contable,1,5) in (select distinct substr(cuenta_contable,1,5) from RF_TR_CUENTAS_CONTABLES  where substr(cuenta_contable,6,4) in (").append(getPrograma()).append(") and id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta());
                if (!programa.equals("0005") && getControlReg().getEjercicio() > 2010) {
                    sentencia.append("and extract(year from fecha_vig_ini)=").append(getControlReg().getEjercicio()).append(")\n");
                } else {
                    sentencia.append("and extract(year from fecha_vig_ini)=").append(ejercicioCompara).append(")\n ");
                }
                sentencia.append("and substr(cuenta_contable, 6, 4) in (").append(getPrograma()).append(")\n");
            } else {
                sentencia.append("and substr(cuenta_contable, 6, 4) = '0005'\n ");
            }
            if (Integer.valueOf(getNivel()) >= 2) {
                sentencia.append("and substr(cuenta_contable, 10, 4) = '0108'\n ");
                sentencia.append("and substr(cuenta_contable, 14, 4) = '").append(getAmbito()).append("'\n ");
            }
            sentencia.append("and extract(year from ctas.fecha_vig_ini)=").append(getControlReg().getEjercicio()).append("\n");
            sentencia.append("and id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta()).append("\n");
            sentencia.append("and ((").append(banco).append(" is null) ");
            sentencia.append("or (").append(banco).append("=1 and (clas.cuenta_mayor = 11201 or clas.cuenta_mayor = 11203)) ");
            sentencia.append("or (").append(banco).append("=0 and clas.cuenta_mayor not in (11201,11203) and clas.id_grupo in (1,3) )) ");
            sentencia.append("           inner join rf_tc_genero_clasf_cta gen on clas.id_genero = gen.id_genero");
            sentencia.append("\n           inner join rf_tc_grupo_clasf_cta gru on clas.id_grupo = gru.id_grupo");
            sentencia.append("\n               and clas.id_genero = gru.id_genero");
            sentencia.append("\n                     and clas.id_genero in (").append(genero).append(")\n");
            if (naturaleza == null) {
                sentencia.append(" and clas.naturaleza is not null ");
            } else {
                sentencia.append(" and clas.naturaleza in ('").append(naturaleza).append("') ");
            }

            sentencia.append("\n group by \n");
            sentencia.append("clas.cuenta_mayor,\n"
                    + "            upper(clas.descripcion) ,\n"
                    + "            upper(gen.descripcion) ,\n"
                    + "            upper(gru.descripcion) , ");
            sentencia.append(" nivel, ctas.cuenta_mayor_id, clas.naturaleza ");
        }




        /*formatos=new Formatos(sentencia.toString(),mes_act, String.valueOf(nivel), String.valueOf(getEjercicio()), programa, unidad, ambito , cajaBanco==null?"null":cajaBanco?"1":"0",mesAntCargo, mesAntAbono, String.valueOf(this.getIdCatalogoCuenta()), ejercicioCompara);
         sentenciaImpl.append(formatos.getSentencia());*/
        return sentencia;
    }

    public StringBuffer pasivoHacienda(String genero, String naturaleza, Boolean cajaBanco, Boolean isExcluye, String mesActual, String mesAnterior, int ejercicioCompara, boolean resultadoEjercicio, String reporte) {
        StringBuffer sentencia = new StringBuffer();
        StringBuffer sentenciaSaldo;
        StringBuffer sentenciaSaldoAnt;
        //StringBuffer sentenciaClasifica = new StringBuffer();
        setNivel(!getPrograma().equals("'0000'") && getUnidad().equals("0000") && getAmbito().equals("0000") ? "2" : getNivel());
        sentenciaSaldo = seleccionaSaldos(cajaBanco, false, mesActual, mesAnterior, ejercicioCompara, resultadoEjercicio, genero, naturaleza);
        sentenciaSaldoAnt = seleccionaSaldosAnt(cajaBanco, false, mesActual, mesAnterior, ejercicioCompara, resultadoEjercicio, genero, naturaleza);
        //sentenciaClasifica = formarSentenciaClasificacion(genero,naturaleza);
        sentencia.append("select ");
        sentencia.append("saldoAnt.cuenta_mayor, ltrim(saldoAnt.descripcion) descripcion, \n");
        sentencia.append("'PASIVO Y HACIENDA PUBLICA' gen_descripcion, ");
        sentencia.append("(case when upper(saldoAnt.gru_descripcion) = 'PATRIMONIALES' \n");
        sentencia.append("then 'HACIENDA PUBLICA' ");
        sentencia.append("else saldoAnt.gru_descripcion end) gru_descripcion, \n");
        sentencia.append("(case when saldoAnt.cuenta_mayor = 41201 \n");
        sentencia.append("then (saldo.saldo*(-1)) ");
        sentencia.append("else saldo.saldo end) saldo, \n");
        sentencia.append("(case when saldoAnt.cuenta_mayor = 41201 or saldoAnt.cuenta_mayor= 41103\n");
        sentencia.append("then (saldoAnt.saldo_ant*(-1)) \n");
        sentencia.append("else saldoAnt.saldo_ant end) saldo_ant \n");
        sentencia.append(" from ");
        sentencia.append("(--pasivoHacienda.sentenciaSaldo\n");
        sentencia.append(sentenciaSaldo);
        sentencia.append("\n) saldo ");
        sentencia.append(" inner join\n ");
        sentencia.append("(");
        if (getUnidad().equals("0117") && getPrograma().equals("'0000'")) {
            sentencia.append("select saldoAnt.cuenta_mayor,\n");
            sentencia.append(" upper(saldoAnt.descripcion) descripcion,\n");
            sentencia.append(" upper(saldoAnt.gen_descripcion) gen_descripcion,\n");
            sentencia.append(" upper(saldoAnt.gru_descripcion) gru_descripcion, \n");
            sentencia.append(" saldoAnt.nivel,saldoAnt.cuenta_mayor_id, sum(saldoAnt.saldo_ant) saldo_ant\n");
            sentencia.append(" from (");
        }
        //sentencia.append(" from(\n"); 
        sentencia.append("--pasivoHacienda.sentenciaSaldoAnt\n");
        sentencia.append(sentenciaSaldoAnt);
        sentencia.append(") saldoAnt \n");
        //************
        if (getUnidad().equals("0117") && getPrograma().equals("'0000'")) {

            sentencia.append("group by saldoAnt.cuenta_mayor,  \n");
            sentencia.append("         saldoAnt.descripcion, \n");
            sentencia.append("          saldoAnt.gen_descripcion, \n");
            sentencia.append("         saldoAnt.gru_descripcion, \n");
            sentencia.append("         saldoAnt.nivel, saldoAnt.cuenta_mayor_id  \n");
            sentencia.append(") saldoAnt  \n");
        }
        sentencia.append("   on saldo.cuenta_mayor_id(+) = saldoAnt.cuenta_mayor_id\n");
        sentencia.append("   and (saldo.saldo <> 0 or  saldoant.saldo_ant <> 0)");
        if (isExcluye) {
            sentencia.append("and (cuenta_mayor <> 41205 and cuenta_mayor <> 41204) ");
        }
        sentencia.append("and (saldo.saldo <> 0.0 OR saldoAnt.saldo_ant <> 0.0 or saldo.cuenta_mayor=41102 or saldo.cuenta_mayor = 41103) ");
        sentencia.append(" where saldoAnt.cuenta_mayor not in(41203) ");
        if (reporte.equals("ESF")) {
            sentencia.append(" and (saldo.saldo<>0 or saldo.cuenta_mayor=41102 or saldo.cuenta_mayor = 41103)");
        }
        sentencia.append("  order by saldoAnt.cuenta_mayor");
        return sentencia;
    }

    public StringBuffer seleccionaSaldosAnt(Boolean cajaBanco, Boolean isMesAnterior, String mesActual, String mesAnterior, int ejercicioCompara, boolean resultadoEjercicio, String genero, String naturaleza) {
        String banco = cajaBanco == null ? "null" : cajaBanco ? "1" : "0";
        StringBuffer sentencia = new StringBuffer();
        setNivel(!getPrograma().equals("'0000'") && getUnidad().equals("0000") && getAmbito().equals("0000") ? "2" : getNivel());
        String abonoAcum;
        String cargoAcum;
        String unidadAnt;
        mesAnterior = sia.libs.formato.Fecha.getNombreMesCorto(Integer.valueOf(mesAnterior) - 1);
        if (mesAnterior.equals("DIC") && ejercicioCompara < getControlReg().getEjercicio()) {
            unidadAnt = (getUnidad().equals("0117") ? "0104" : getUnidad().equals("0118") ? "0108" : getUnidad());
        } else {
            unidadAnt = getUnidad();
        }
        if (mesAnterior.equals("DIC")) {
            abonoAcum = construirMes("ENE", "abono_ini");
            cargoAcum = construirMes("ENE", "cargo_ini");
        } else {
            abonoAcum = construirMes(mesAnterior, "abono_acum");
            cargoAcum = construirMes(mesAnterior, "cargo_acum");
        }
        // Este quere se integra cuando se compara dic ejercicio actual con dic ejercicio menor al actual y cuando sea programa <> 5
        sentencia.append("select ");
        sentencia.append("            clas.cuenta_mayor,\n"
                + "            upper(clas.descripcion) descripcion,\n"
                + "            upper(gen.descripcion) gen_descripcion,\n"
                + "            upper(gru.descripcion) gru_descripcion, \n"
                + "                   ctas.nivel,\n"
                + "                    ctas.cuenta_mayor_id,");
        sentencia.append("(case when clas.naturaleza ='D' ");
        sentencia.append("then sum(ctas.").append(cargoAcum).append(" - ctas.").append(abonoAcum).append(") ");
        sentencia.append("else sum(ctas.").append(abonoAcum).append(" - ctas.").append(cargoAcum).append(") end) saldo_ant \n");
        sentencia.append(" from ");
        sentencia.append(" rf_tr_cuentas_contables ctas \n");
        sentencia.append("inner join rf_tc_clasificador_cuentas clas \n");
        sentencia.append("on ctas.cuenta_mayor_id = clas.cuenta_mayor_id \n");
        sentencia.append("and nivel = ").append(getNivel());
        if (!programa.equals("'0000'")) {
            sentencia.append("and substr(cuenta_contable,1,5) in (select distinct substr(cuenta_contable,1,5) from RF_TR_CUENTAS_CONTABLES  where substr(cuenta_contable,6,4) in (").append(getPrograma()).append(") and id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta());
            if (!programa.equals("0005") && getControlReg().getEjercicio() > 2010) {
                sentencia.append("and extract(year from fecha_vig_ini)=").append(getControlReg().getEjercicio()).append(") ");
            } else {
                sentencia.append("and extract(year from fecha_vig_ini)=").append(ejercicioCompara).append(") ");
            }
            //sentencia.append("and substr(cuenta_contable, 6, 4) in ("+getPrograma()+")");
        }
        if (getUnidad().equals("0117") && getPrograma().equals("'0000'")) {
            sentencia.append("and substr(cuenta_contable, 6, 4) <> '0005' ");
        }
        if (Integer.valueOf(getNivel()) >= 2) {
            sentencia.append("and substr(cuenta_contable, 10, 4) = '").append(unidadAnt).append("' ");
            sentencia.append("and substr(cuenta_contable, 14, 4) = '").append(getAmbito()).append("' ");
        }
        sentencia.append("and extract(year from ctas.fecha_vig_ini)=").append(getControlReg().getEjercicio());
        sentencia.append("and id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta());
        sentencia.append("and ((").append(cajaBanco).append(" is null) ");
        sentencia.append("or (").append(cajaBanco).append("=1 and (clas.cuenta_mayor = 11201 or clas.cuenta_mayor = 11203)) ");
        sentencia.append("or (").append(cajaBanco).append("=0 and clas.cuenta_mayor not in (11201,11203) and clas.id_grupo in (1,3) )) ");
        sentencia.append("           inner join rf_tc_genero_clasf_cta gen on clas.id_genero = gen.id_genero");
        sentencia.append("\n           inner join rf_tc_grupo_clasf_cta gru on clas.id_grupo = gru.id_grupo");
        sentencia.append("\n                   and clas.id_genero = gru.id_genero");
        sentencia.append("\n                   and clas.id_genero in (").append(genero).append(")\n");
        if (naturaleza == null) {
            sentencia.append(" and clas.naturaleza is not null ");
        } else {
            sentencia.append(" and clas.naturaleza in ('").append(naturaleza).append("') ");
        }

        sentencia.append("\n group by \n");
        sentencia.append("clas.cuenta_mayor,\n"
                + "            upper(clas.descripcion) ,\n"
                + "            upper(gen.descripcion) ,\n"
                + "            upper(gru.descripcion) , ");
        sentencia.append(" nivel, ctas.cuenta_mayor_id, clas.naturaleza \n");
        if (resultadoEjercicio && (!getPrograma().contains("'0001'") && !getPrograma().contains("'0000'"))) {
            sentencia.append(" union\n");
            sentencia.append(" select 4 nivel, 264 cuenta_mayor_id, 0 saldo_ant from dual\n");
        }

        // Este quere se integra cuando se compara dic ejercicio actual con dic ejercicio menor al actual y cuando sea programa 5 y unidad 117
        if (getUnidad().equals("0117") && getPrograma().equals("'0000'")) {
            sentencia.append("union \n ");
            sentencia.append("select ");
            sentencia.append("            clas.cuenta_mayor,\n"
                    + "            upper(clas.descripcion) descripcion,\n"
                    + "            upper(gen.descripcion) gen_descripcion,\n"
                    + "            upper(gru.descripcion) gru_descripcion, \n"
                    + "                   ctas.nivel,\n"
                    + "                    ctas.cuenta_mayor_id,");
            sentencia.append("(case when clas.naturaleza ='D' ");
            sentencia.append("then sum(ctas.").append(cargoAcum).append(" - ctas.").append(abonoAcum).append(") ");
            sentencia.append("else sum(ctas.").append(abonoAcum).append(" - ctas.").append(cargoAcum).append(") end) saldo_ant \n");
            sentencia.append(" from ");
            sentencia.append(" rf_tr_cuentas_contables ctas \n");
            sentencia.append("inner join rf_tc_clasificador_cuentas clas \n");
            sentencia.append("on ctas.cuenta_mayor_id = clas.cuenta_mayor_id \n");
            sentencia.append("and nivel = ").append(getNivel()).append("\n");
            if (!programa.equals("'0000'")) {
                sentencia.append("and substr(cuenta_contable,1,5) in (select distinct substr(cuenta_contable,1,5) from RF_TR_CUENTAS_CONTABLES  where substr(cuenta_contable,6,4) in (").append(getPrograma()).append(") and id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta());
                if (!programa.equals("0005") && getControlReg().getEjercicio() > 2010) {
                    sentencia.append("and extract(year from fecha_vig_ini)=").append(getControlReg().getEjercicio()).append(")\n");
                } else {
                    sentencia.append("and extract(year from fecha_vig_ini)=").append(ejercicioCompara).append(")\n ");
                }
                sentencia.append("and substr(cuenta_contable, 6, 4) in (").append(getPrograma()).append(")\n");
            } else {
                sentencia.append("and substr(cuenta_contable, 6, 4) = '0005'\n ");
            }
            if (Integer.valueOf(getNivel()) >= 2) {
                sentencia.append("and substr(cuenta_contable, 10, 4) = '0108'\n ");
                sentencia.append("and substr(cuenta_contable, 14, 4) = '").append(getAmbito()).append("'\n ");
            }
            sentencia.append("and extract(year from ctas.fecha_vig_ini)=").append(getControlReg().getEjercicio()).append("\n");
            sentencia.append("and id_catalogo_cuenta = ").append(getControlReg().getIdCatalogoCuenta()).append("\n");
            sentencia.append("and ((").append(cajaBanco).append(" is null) ");
            sentencia.append("or (").append(cajaBanco).append("=1 ");
            sentencia.append("and (clas.cuenta_mayor = 11201 or clas.cuenta_mayor = 11203)) ");
            sentencia.append("or (").append(cajaBanco).append("=0 ");
            sentencia.append("and clas.cuenta_mayor not in (11201,11203) and clas.id_grupo in (1,3) )) ");
            sentencia.append("           inner join rf_tc_genero_clasf_cta gen on clas.id_genero = gen.id_genero");
            sentencia.append("\n    inner join rf_tc_grupo_clasf_cta gru on clas.id_grupo = gru.id_grupo");
            sentencia.append("\n                                     and clas.id_genero = gru.id_genero");
            sentencia.append("\n                                     and clas.id_genero in (").append(genero).append(")\n");
            if (naturaleza == null) {
                sentencia.append(" and clas.naturaleza is not null ");
            } else {
                sentencia.append(" and clas.naturaleza in ('").append(naturaleza).append("') ");
            }

            sentencia.append("\n group by \n");
            sentencia.append("clas.cuenta_mayor,\n"
                    + "            upper(clas.descripcion) ,\n"
                    + "            upper(gen.descripcion) ,\n"
                    + "            upper(gru.descripcion) , ");
            sentencia.append(" nivel, ctas.cuenta_mayor_id, clas.naturaleza ");
            if (resultadoEjercicio && (!getPrograma().contains("'0001'") && !getPrograma().contains("'0000'"))) {
                sentencia.append(" union");
                sentencia.append(" select 4 nivel, 264 cuenta_mayor_id, 0 saldo_ant from dual");
            }
        }
        return sentencia;
    } //seleccionaSaldos

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setAmbito(String ambito) {
        this.ambito = ambito;
    }

    public String getAmbito() {
        return ambito;
    }
}
