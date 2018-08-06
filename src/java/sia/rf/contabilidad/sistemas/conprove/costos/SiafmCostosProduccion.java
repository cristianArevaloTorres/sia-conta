package sia.rf.contabilidad.sistemas.conprove.costos;

import java.sql.Connection;
import java.sql.SQLException;
import sun.jdbc.rowset.CachedRowSet;
import java.util.HashMap;
/* Para generar a 8 decimales */
import java.util.Locale; 
import java.text.NumberFormat;
import java.text.DecimalFormat;
//
import java.util.Iterator;
import java.util.Set;

public class SiafmCostosProduccion {

// Metodo que trunca una cantidad a 8 decimales sin formato
public static Double decimales(double numero) {
  Locale locale = new Locale("es", "MX");
  Locale.setDefault(locale);
  //NumberFormat formatter = new DecimalFormat("#########0.########");
  NumberFormat formatter = new DecimalFormat("#########0.##");
  return Double.parseDouble(formatter.format(numero));
}

public static void imprimeHM(HashMap arreglo) {
  Set sKey = null;
  String st = "";
  sKey = arreglo.keySet();
  Iterator i = sKey.iterator();
  while(i.hasNext()){
    st = (String) i.next();
    System.out.println("V: " + st + "="+arreglo.get(st));
  }
}

/* -----------------------------------------------------------------------------------------------------
 * Forma:             CA 
 * Descripcion:       Entrada de Materiales   
 * Fecha de Creacion: 24 deAgosto del 2009
 * Author:            JAUV
 * Parametros:        mes  y año
 * Observaciones:     Metodo que genera un crs de la consulta de vales de materiales por mes y año
 *                    calculando el importe total de los vales registrados en la tarjeta de materiales.
 *                    Regresa un HashMap con variables de forma e importes 
 * ----------------------------------------------------------------------------------------------------- */
public HashMap caEntradaMateriales(Connection con, String mes, String anio, String importe) throws SQLException, Exception {
  
  HashMap hm = new HashMap();
  Double valor=0.00;
  
  try{
    valor = Double.parseDouble(importe);
    hm.put("IMPORTE",decimales(valor));
  }
  catch (Exception e){
    System.out.println("Ha ocurrido un error en el metodo SiafmCostosProduccion.caEntradaMateriales");
    System.out.println("SiafmCostosProduccion.caEntradaMateriales");
    throw e;
  }
  finally {
  } 
  return hm; 
}

/* --------------------------------------------------------------------------------------------------
 * Forma:             CB 
 * Descripcion:       Devolucion de Materiales   
 * Fecha de Creacion: 24 deAgosto del 2009
 * Author:            JAUV
 * Parametros:        mes  y año
 * Observaciones:     Metodo que genera un crs de la consulta de salida de materiales por mes y año
 *                    Regresa un HashMap con variables de forma e importes 
 * -------------------------------------------------------------------------------------------------- */
public HashMap cbDevolucionMateriales(Connection con, String mes, String anio, String importe) throws SQLException, Exception {
      
  HashMap hm = new HashMap();
  Double valor=0.00;
    
  try{
    valor = Double.parseDouble(importe);
    hm.put("IMPORTE",decimales(valor));
  }
  catch (Exception e){
    System.out.println("Ha ocurrido un error en el metodo SiafmCostosProduccion.cbDevolucionMateriales");
    System.out.println("SiafmCostosProduccion.cbDevolucionMateriales");
    throw e;
  }
  finally {
  } 
  return hm; 
}

/* --------------------------------------------------------------------------------------------------
 * Formas:            CC 
 * Descripcion:       Consumo de Materia Prima Directa de una Orden de Producci�n Terminada
 * Fecha de Creacion: 24 deAgosto del 2009
 * Author:            JAUV
 * Parametros:        mes, año, numero de orden de produccion, comercializable (0=no y 1=si)
 * Observaciones:     Metodo que genera un crs de la consulta de consumo de materiales por mes,año
 *                    numero de orden de producci�n.
 *                    Regresa un HashMap con variables de forma e importes 
 * -------------------------------------------------------------------------------------------------- */
public HashMap ccMateriaPrimaOP(Connection con, String mes, String anio, String numOrden, String proComerci) throws SQLException, Exception {
          
  HashMap hm = new HashMap();
  CachedRowSet crs = null;
  StringBuffer SQL = new StringBuffer(" ");
  String variable    ="";
  String lsProComerci="";
  Double impMPD = 0.00;
  Double importe= 0.00;
  int i=0;

  SQL.append(" select h.idproceso, sum(mp.importe) importe from ");
  SQL.append(" (select substr(c.cvemanobrc,1,4) clave, sum(t.haber) importe from ctireginsumos c, stitarjetamat t "); 
  SQL.append(" where to_char(c.fechacaptu,'mm')=");
  SQL.append(mes);    
  SQL.append(" and to_char(c.fechacaptu,'yyyy')=");
  SQL.append(anio);  
  SQL.append(" and c.numorden = "); 
  SQL.append(numOrden);
  SQL.append(" and c.conseinsum = t.referencia group by substr(c.cvemanobrc,1,4)) mp, stiprocesoh h ");
  SQL.append(" where h.mes="+mes+" and h.anio="+anio+" and mp.clave(+) = substr(h.cvemanobrc,1,4) ");
  SQL.append(" group by h.idproceso ");

  if (proComerci.equals("0")) lsProComerci="2";
  else lsProComerci="1";

  try{
    crs = new CachedRowSet();
    crs.setCommand(SQL.toString());
    crs.execute(con);
    crs.beforeFirst();
    i=0;
    while (crs.next()) {
      i++;
      impMPD  = decimales(impMPD)+decimales(Double.parseDouble((crs.getString("importe")== null)? "0.00":crs.getString("importe")));
      importe = Double.parseDouble((crs.getString("importe")== null)? "0.00":crs.getString("importe"));
      variable="IMPORTE_"+i+"_"+lsProComerci;
      hm.put(variable,decimales(importe));
    }
    if (impMPD > 0.00) { hm.put("IMPORTE_2105",decimales(impMPD)); }
    else hm.clear();
  }
  catch (Exception e){
    System.out.println("Ha ocurrido un error en el metodo SiafmCostosProduccion.ccMateriaPrimaOP");
    System.out.println("SiafmCostosProduccion.ccMateriaPrimaOP : " + SQL.toString());
    crs.close();
    crs = null;
    throw e;
  }
  finally {
    SQL.setLength(0);
    SQL = null;
    crs= null;
  } 
  return hm; 
}

/* --------------------------------------------------------------------------------------------------
 * Formas:            CC 
 * Descripcion:       Consumo de Materia Prima Directa al Cierre de Mes
 * Fecha de Creacion: 25 deAgosto del 2009
 * Author:            JAUV
 * Parametros:        mes, año
 * Observaciones:     Metodo que genera un crs de la consulta de consumo de materiales por mes,año
 *                    de la Produccion en Proceso
 *                    Regresa un HashMap con variables de forma e importes 
 * -------------------------------------------------------------------------------------------------- */
public HashMap ccMateriaPrimaCM(Connection con, String mes, String anio) throws SQLException, Exception {
              
  HashMap hm = new HashMap();
  CachedRowSet crs = null;
  StringBuffer SQL = new StringBuffer(" ");
  String variable  = "";
  Double impMPD    = 0.00;
  Double impMPDc   = 0.00;
  Double impMPDnc  = 0.00;
  int i=0;

    SQL.append(" select h.idproceso, sum(mpdc.importe) mpdc, sum(mpdnc.importe) mpdnc ");
    SQL.append(" from stiprocesoh h,  ");
    SQL.append(" (select substr(c.cvemanobrc,1,4) clave, sum(t.haber) importe from  ");
    SQL.append(" (select s.numorden, s.mes, s.anio from sticalculos s, etidescrmater e, etiordenprodu o  ");
    SQL.append(" where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo in ('1101','1103','1104')  ");
    SQL.append(" and s.numorden = o.numorden and o.numconrequ = e.numconrequ and e.procomerci_ant=1 and s.stregistro is null) op, ctireginsumos c, stitarjetamat t  ");
    SQL.append(" where op.numorden = c.numorden and to_char(c.fechacaptu,'mm')="+mes+" and to_char(c.fechacaptu,'yyyy')="+anio);
    SQL.append(" and c.conseinsum = t.referencia and t.mes="+mes+" and t.anio="+anio);
    SQL.append(" group by substr(c.cvemanobrc,1,4)) mpdc, ");
    SQL.append(" (select mat.clave, sum(mat.importe) importe from   ");
    SQL.append(" (select substr(c.cvemanobrc,1,4) clave, sum(t.haber) importe from ");
    SQL.append(" (select s.numorden, s.mes, s.anio from sticalculos s, etidescrmater e, etiordenprodu o ");
    SQL.append(" where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo in ('1101','1103','1104') ");
    SQL.append(" and s.numorden = o.numorden and o.numconrequ = e.numconrequ and e.procomerci_ant=0 and s.stregistro is null) op, ctireginsumos c, stitarjetamat t ");
    SQL.append(" where op.numorden = c.numorden and to_char(c.fechacaptu,'mm')="+mes+" and to_char(c.fechacaptu,'yyyy')="+anio);
    SQL.append(" and c.conseinsum = t.referencia and t.mes="+mes+" and t.anio="+anio);
    SQL.append(" group by substr(c.cvemanobrc,1,4) ");
    SQL.append(" union all ");
    SQL.append(" select substr(c.cvemanobrc,1,4) clave, sum(t.haber) importe from ");
    SQL.append(" (select s.cvetrameno, s.mes, s.anio from sticalculos s where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo in ('1105','1106') ");
    SQL.append(" and s.stregistro is null) tm, ctireginsumos c, stitarjetamat t ");
    SQL.append(" where tm.cvetrameno = c.cvetrameno and to_char(c.fechacaptu,'mm')="+mes+" and to_char(c.fechacaptu,'yyyy')="+anio);
    SQL.append(" and c.conseinsum = t.referencia and t.mes="+mes+" and t.anio="+anio);
    SQL.append(" group by substr(c.cvemanobrc,1,4)) mat ");
    SQL.append(" group by mat.clave) mpdnc ");
    SQL.append(" where mpdc.clave(+)  = substr(h.cvemanobrc,1,4) ");
    SQL.append(" and mpdnc.clave(+) = substr(h.cvemanobrc,1,4) ");
    SQL.append(" and h.mes="+mes+" and h.anio="+anio);
    SQL.append(" group by h.idproceso order by h.idproceso ");

  try{
    crs = new CachedRowSet();
    crs.setCommand(SQL.toString());
    crs.execute(con);
    crs.beforeFirst();
    i=0;
    while (crs.next()) {
      i++;
      impMPDc = decimales(Double.parseDouble((crs.getString("mpdc")== null)? "0.00":crs.getString("mpdc")));
      impMPDnc= decimales(Double.parseDouble((crs.getString("mpdnc")== null)? "0.00":crs.getString("mpdnc")));
      impMPD  = decimales(impMPD) + decimales(impMPDc) + decimales(impMPDnc);
      variable="IMPORTE_"+i+"_1";
      hm.put(variable,decimales(impMPDc));
      variable="IMPORTE_"+i+"_2";
      hm.put(variable,decimales(impMPDnc));
    }
    if (impMPD > 0.00) { hm.put("IMPORTE_2105",decimales(impMPD)); }
    else hm.clear();
  }
  catch (Exception e){
    System.out.println("Ha ocurrido un error en el metodo SiafmCostosProduccion.ccMateriaPrimaCM");
    System.out.println("SiafmCostosProduccion.ccMateriaPrimaCM : " + SQL.toString());
    crs.close();
    crs = null;
    throw e;
  }
  finally {
    SQL.setLength(0);
    SQL = null;
    crs= null;
  } 
  return hm; 
}

/* --------------------------------------------------------------------------------------------------
 * Formas:            CD
 * Descripcion:       Consumo de Taller Externo de una Orden de Producci�n Terminada
 * Fecha de Creacion: 24 deAgosto del 2009
 * Author:            JAUV
 * Parametros:        mes, año, numero de orden de produccion, comercializable (0=no y 1=si)
 * Observaciones:     Metodo que genera un crs de la consulta de consumo de materiales por mes,año
 *                    numero de orden de produccion.
 *                    Regresa un HashMap con variables de forma e importes 
 * -------------------------------------------------------------------------------------------------- */
public HashMap cdTallerExternoOP(Connection con, String mes, String anio, String numOrden, String proComerci) throws SQLException, Exception {
              
  HashMap hm = new HashMap();
  CachedRowSet crs = null;
  StringBuffer SQL = new StringBuffer(" ");
  String variable    ="";
  String lsProComerci="";
  Double impTE  = 0.00;
  Double importe= 0.00;
  int i=0;

  SQL.append(" select h.idproceso, sum(te.importe) te from stiprocesoh h, ");
  SQL.append(" (select substr(ta.idproceso,1,4) clave, sum(ta.monto) importe from stitallerext ta ");
  SQL.append(" where ta.numorden="+numOrden+" and ta.estatus='1002' ");
  SQL.append(" group by substr(ta.idproceso,1,4) ");
  SQL.append(" union all ");
  SQL.append(" select substr(co.idproceso,1,4) clave, sum(co.impviatico+co.imppasajes) importe from sticomisiones co ");
  SQL.append(" where co.numorden="+numOrden+" and co.estatus='1002' ");
  SQL.append(" group by substr(co.idproceso,1,4)) te ");
  SQL.append(" where te.clave(+)=substr(h.cvemanobrc,1,4) and h.mes="+mes+" and h.anio="+anio);
  SQL.append(" group by h.idproceso order by h.idproceso ");

  if (proComerci.equals("0")) lsProComerci="2";
  else lsProComerci="1";

  try{
    crs = new CachedRowSet();
    crs.setCommand(SQL.toString());
    crs.execute(con);
    crs.beforeFirst();
    i=0;
    while (crs.next()) {
      i++;
      impTE = decimales(impTE)+decimales(Double.parseDouble((crs.getString("te")== null)? "0.00":crs.getString("te")));
      importe = Double.parseDouble((crs.getString("te")== null)? "0.00":crs.getString("te"));
      variable="IMPORTE_"+i+"_"+lsProComerci;
      hm.put(variable,decimales(importe));
    }
    if (impTE > 0.00) { hm.put("IMPORTE_0005",decimales(impTE)); }
    else hm.clear();
  }
  catch (Exception e){
    System.out.println("Ha ocurrido un error en el metodo SiafmCostosProduccion.ccMateriaPrimaOP");
    System.out.println("SiafmCostosProduccion.ccMateriaPrimaOP : " + SQL.toString());
    crs.close();
    crs = null;
    throw e;
  }
  finally {
    SQL.setLength(0);
    SQL = null;
    crs= null;
  } 
  return hm; 
}

/* --------------------------------------------------------------------------------------------------
 * Formas:            CD
 * Descripci�n:       Consumo de Taller Externo al Cierre de Mes
 * Fecha de Creaci�n: 25 deAgosto del 2009
 * Author:            JAUV
 * Parametros:        mes, a�o
 * Observaciones:     Metodo que genera un crs de la consulta de consumo de taller ecterno y comisiones
 *                    por mes,a�o de la Produccion en Proceso
 *                    Regresa un HashMap con variables de forma e importes 
 * -------------------------------------------------------------------------------------------------- */
public HashMap cdTallerExternoCM(Connection con, String mes, String anio) throws SQLException, Exception {

  HashMap hm = new HashMap();
  CachedRowSet crs = null;
  StringBuffer SQL = new StringBuffer(" ");
  String variable  = "";
  Double impTE     = 0.00;
  Double impTEc    = 0.00;
  Double impTEnc   = 0.00;
  int i=0;

  SQL.append(" select h.idproceso, sum(tec.importe) tec, sum(tenc.importe) tenc from stiprocesoh h, ");
  SQL.append(" (select t.idproceso clave, sum(t.monto) importe from ");
  SQL.append(" (select s.numorden, s.mes, s.anio from sticalculos s, etidescrmater e, etiordenprodu o ");
  SQL.append(" where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo in ('1101','1103','1104') ");
  SQL.append(" and s.numorden = o.numorden and o.numconrequ = e.numconrequ and e.procomerci_ant=1 and s.stregistro is null) op, stitallerext t ");
  SQL.append(" where op.numorden = t.numorden and t.mes="+mes+" and t.anio="+anio);
  SQL.append(" group by t.idproceso ");
  SQL.append(" union all ");
  SQL.append(" select substr(c.idproceso,1,4) clave, sum(c.impviatico+c.imppasajes) importe from ");
  SQL.append(" (select s.numorden, s.mes, s.anio from sticalculos s, etidescrmater e, etiordenprodu o ");
  SQL.append(" where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo in ('1101','1103','1104') ");
  SQL.append(" and s.numorden = o.numorden and o.numconrequ = e.numconrequ and e.procomerci_ant=1 and s.stregistro is null) op, sticomisiones c ");
  SQL.append(" where op.numorden = c.numorden and c.mes="+mes+" and c.anio="+anio);
  SQL.append(" group by c.idproceso) tec, ");

  SQL.append(" (select substr(t.idproceso,1,4)  clave, sum(t.monto) importe from ");
  SQL.append(" (select s.numorden, s.mes, s.anio from sticalculos s, etidescrmater e, etiordenprodu o ");
  SQL.append(" where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo in ('1101','1103','1104') ");
  SQL.append(" and s.numorden = o.numorden and o.numconrequ = e.numconrequ and e.procomerci_ant=0 and s.stregistro is null) op, stitallerext t ");
  SQL.append(" where op.numorden = t.numorden and t.mes="+mes+" and t.anio="+anio);
  SQL.append(" group by t.idproceso ");
  SQL.append(" union all ");
  SQL.append(" select substr(c.idproceso,1,4) clave, sum(c.impviatico+c.imppasajes) importe from ");
  SQL.append(" (select s.numorden, s.mes, s.anio from sticalculos s, etidescrmater e, etiordenprodu o ");
  SQL.append(" where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo in ('1101','1103','1104') ");
  SQL.append(" and s.numorden=o.numorden and o.numconrequ = e.numconrequ and e.procomerci_ant=0 and s.stregistro is null) op, sticomisiones c ");
  SQL.append(" where op.numorden = c.numorden and c.mes="+mes+" and c.anio="+anio);
  SQL.append(" group by c.idproceso) tenc ");

  SQL.append(" where tec.clave(+) = substr(h.cvemanobrc,1,4) and tenc.clave(+) = substr(h.cvemanobrc,1,4) ");
  SQL.append(" and h.mes="+mes+" and h.anio="+anio);
  SQL.append(" group by h.idproceso order by h.idproceso ");

  try{
    crs = new CachedRowSet();
    crs.setCommand(SQL.toString());
    crs.execute(con);
    crs.beforeFirst();
    i=0;
    while (crs.next()) {
      i++;
      impTE  = decimales(impTE)+decimales(Double.parseDouble((crs.getString("tec")== null)?  "0.00":crs.getString("tec")));
      impTE  = decimales(impTE)+decimales(Double.parseDouble((crs.getString("tenc")== null)? "0.00":crs.getString("tenc")));
      impTEc = decimales(impTEc)+decimales(Double.parseDouble((crs.getString("tec")== null)?  "0.00":crs.getString("tec")));
      impTEnc= decimales(impTEc)+decimales(Double.parseDouble((crs.getString("tenc")== null)? "0.00":crs.getString("tenc")));
      variable="IMPORTE_"+i+"_1";
      hm.put(variable,decimales(impTEc));
      variable="IMPORTE_"+i+"_2";
      hm.put(variable,decimales(impTEnc));
    }
    if (impTE > 0.00) { hm.put("IMPORTE_0005",decimales(impTE)); }
    else hm.clear();
  }
  catch (Exception e){
    System.out.println("Ha ocurrido un error en el metodo SiafmCostosProduccion.cdTallerExternoCM");
    System.out.println("SiafmCostosProduccion.cdTallerExternoCM : " + SQL.toString());
    crs.close();
    crs = null;
    throw e;
  }
  finally {
    SQL.setLength(0);
    SQL = null;
    crs= null;
  } 
  return hm; 
}

/* --------------------------------------------------------------------------------------------------
 * Formas:            CE 
 * Descripcion:       Consumo de Mano de Obra Directa de una Orden de Producci�n Terminada
 * Fecha de Creacion: 25 deAgosto del 2009
 * Author:            JAUV
 * Parametros:        mes, año, numero de orden de produccion, comercializable (0=no y 1=si)
 * Observaciones:     Metodo que genera un crs de la consulta de consumo de mano de obra por mes,año y
 *                    numero de orden de produccion.
 *                    Regresa un HashMap con variables de forma e importes 
 * -------------------------------------------------------------------------------------------------- */
public HashMap ceManoDeObraOP(Connection con, String mes, String anio, String numOrden, String proComerci) throws SQLException, Exception {
              
  HashMap hm = new HashMap();
  CachedRowSet crs = null;
  StringBuffer SQL = new StringBuffer(" ");
  String variable    ="";
  String lsProComerci="";
  Double impMOD = 0.00;
  Double importe= 0.00;
  int i=0;
  String mesAnt = "",anioAnt="";
  if (Integer.parseInt(mes)==1){
    mesAnt="12";
    anioAnt=String.valueOf(Integer.parseInt(anio)-1);
  } else {  
    mesAnt = String.valueOf(Integer.parseInt(mes)-1);
    anioAnt= anio;  }

  SQL.append(" select h.idproceso, sum(mo.importe) mod from stiprocesoh h, ");
  SQL.append(" (select mo.clave, sum(mo.tiempo*mo.factor) importe from ");
  SQL.append(" (select substr(c.cvemanobrc,1,4) clave, (c.tiempo/60) tiempo, ");
  SQL.append(" (case when to_char(o.fecterorden, 'dd') < 12 then (Select nvl(F.FMes,0) FMES From stifactormod F where F.Noempleado = a.noempleado ");
  SQL.append(" and F.Anio="+anioAnt+" and F.Mes="+mesAnt +") ");
  SQL.append(" when To_char(o.fecterorden, 'dd') > 11 then (Select nvl(F.Fq1,0) Fq1 From stifactormod F where F.Noempleado = a.noempleado ");
  SQL.append(" and F.Anio= "+anio+" and F.Mes="+mes+") END) as factor ");
  SQL.append(" from ctiregtiempos c, stiplantpers a , etiordenprodu o ");
  SQL.append(" where to_char(c.fechacaptu,'mm')="+mes+" and to_char(c.fechacaptu,'yyyy')="+anio);
  SQL.append(" and c.numorden=o.numorden and o.numorden="+numOrden+" and a.curp = c.curp) mo group by mo.clave) mo ");
  SQL.append(" where mo.clave(+) = substr(h.cvemanobrc,1,4) and h.mes="+mes+" and h.anio="+anio );
  SQL.append(" group by h.idproceso order by h.idproceso ");
  if (proComerci.equals("0")) lsProComerci="2";
  else lsProComerci="1";

  try{
    crs = new CachedRowSet();
    crs.setCommand(SQL.toString());
    crs.execute(con);
    crs.beforeFirst();
    i=0;
    while (crs.next()) {
      i++;
      impMOD = decimales(impMOD)+decimales(Double.parseDouble((crs.getString("mod")== null)? "0.00":crs.getString("mod")));
      importe = Double.parseDouble((crs.getString("mod")== null)? "0.00":crs.getString("mod"));
      variable="IMPORTE_"+i+"_"+lsProComerci;
      hm.put(variable,decimales(importe));
    }
    if (impMOD > 0.00) { hm.put("IMPORTE_0001",decimales(impMOD)); }
    else hm.clear();
  }
  catch (Exception e){
    System.out.println("Ha ocurrido un error en el metodo SiafmCostosProduccion.ceManoDeObraOP");
    System.out.println("SiafmCostosProduccion.ceManoDeObraOP : " + SQL.toString());
    crs.close();
    crs = null;
    throw e;
  }
  finally {
    SQL.setLength(0);
    SQL = null;
    crs= null;
  } 
  return hm; 
}

/* --------------------------------------------------------------------------------------------------
 * Formas:            CE
 * Descripci�n:       Consumo de Mano de Obra Directa al Cierre de Mes
 * Fecha de Creaci�n: 25 deAgosto del 2009
 * Author:            JAUV
 * Parametros:        mes, a�o
 * Observaciones:     Metodo que genera un crs de la consulta de consumo de manode obra por mes y a�o
 *                    de la Produccion en Proceso
 *                    Regresa un HashMap con variables de forma e importes 
 * -------------------------------------------------------------------------------------------------- */
public HashMap ceManoDeObraCM(Connection con, String mes, String anio) throws SQLException, Exception {
                  
  HashMap hm = new HashMap();
  CachedRowSet crs = null;
  StringBuffer SQL = new StringBuffer(" ");
  String variable  = "";
  Double impMOD    = 0.00;
  Double impMODc   = 0.00;
  Double impMODnc  = 0.00;
  Double importe   = 0.00;
  int i=0;

  SQL.append(" select h.idproceso, sum(modc.importe) modc, sum(modnc.importe) modnc from stiprocesoh h, ");
  SQL.append(" (select mo.clave, sum(mo.importe) importe from ");
  SQL.append(" (select substr(c.cvemanobrc,1,4) clave, ");
  SQL.append(" sum(case when c.tiempoextr = 1 then ((f.fmes*2)*(c.tiempo/60)) else ((c.tiempo/60)*f.fmes) end) importe from ");
  SQL.append(" (select s.numconrequ, s.mes, s.anio from sticalculos s, etidescrmater e ");
  SQL.append(" where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo in ('1109','1110') ");
  SQL.append(" and s.numconrequ = e.numconrequ and e.procomerci_ant=1 and s.stregistro is null) req, ctiregtiempos c, stiplantpers p, stifactormod f ");
  SQL.append(" where req.numconrequ = c.numconrequ and to_char(c.fechacaptu,'mm')="+mes+" and to_char(c.fechacaptu,'yyyy')="+anio);
  SQL.append(" and c.curp = p.curp and p.noempleado = f.noempleado and f.mes="+mes+" and f.anio="+anio);
  SQL.append(" group by substr(c.cvemanobrc,1,4) ");
  SQL.append(" union all ");
  SQL.append(" select substr(c.cvemanobrc,1,4) clave, ");
  SQL.append(" sum(case when c.tiempoextr = 1 then ((f.fmes*2)*(c.tiempo/60)) else ((c.tiempo/60)*f.fmes) end) importe from ");
  SQL.append(" (select s.numorden, s.mes, s.anio from sticalculos s, etidescrmater e, etiordenprodu o ");
  SQL.append(" where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo in ('1101','1103','1104') ");
  SQL.append(" and s.numorden = o.numorden and o.numconrequ = e.numconrequ and e.procomerci_ant=1 and s.stregistro is null) op, ");
  SQL.append(" ctiregtiempos c, stiplantpers p, stifactormod f ");
  SQL.append(" where op.numorden = c.numorden and to_char(c.fechacaptu,'mm')="+mes+" and to_char(c.fechacaptu,'yyyy')="+anio);
  SQL.append(" and c.curp = p.curp and p.noempleado = f.noempleado and f.mes="+mes+" and f.anio="+anio);
  SQL.append(" group by substr(c.cvemanobrc,1,4)) mo ");
  SQL.append(" group by mo.clave) modc, ");

  SQL.append(" (select mano.clave, sum(mano.importe) importe from ");
  SQL.append(" (select substr(c.cvemanobrc,1,4) clave, ");
  SQL.append(" sum(case when c.tiempoextr = 1 then ((f.fmes*2)*(c.tiempo/60)) else ((c.tiempo/60)*f.fmes) end) importe from ");
  SQL.append(" (select s.numconrequ, s.mes, s.anio from sticalculos s, etidescrmater e ");
  SQL.append(" where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo in ('1109','1110') ");
  SQL.append(" and s.numconrequ = e.numconrequ and e.procomerci_ant=0 and s.stregistro is null) req, ctiregtiempos c, stiplantpers p, stifactormod f ");
  SQL.append(" where req.numconrequ = c.numconrequ and to_char(c.fechacaptu,'mm')="+mes+" and to_char(c.fechacaptu,'yyyy')="+anio);
  SQL.append(" and c.curp = p.curp and p.noempleado = f.noempleado and f.mes="+mes+" and f.anio="+anio);
  SQL.append(" group by substr(c.cvemanobrc,1,4) ");
  SQL.append(" union all ");
  SQL.append(" select substr(c.cvemanobrc,1,4) clave, ");
  SQL.append(" sum(case when c.tiempoextr = 1 then ((f.fmes*2)*(c.tiempo/60)) else ((c.tiempo/60)*f.fmes) end) importe from ");
  SQL.append(" (select s.numorden, s.mes, s.anio from sticalculos s, etidescrmater e, etiordenprodu o ");
  SQL.append(" where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo in ('1101','1103','1104') ");
  SQL.append(" and s.numorden = o.numorden and o.numconrequ = e.numconrequ and e.procomerci_ant=0 and s.stregistro is null) op, ");
  SQL.append(" ctiregtiempos c, stiplantpers p, stifactormod f ");
  SQL.append(" where op.numorden = c.numorden and to_char(c.fechacaptu,'mm')="+mes+" and to_char(c.fechacaptu,'yyyy')="+anio);
  SQL.append(" and c.curp = p.curp and p.noempleado = f.noempleado and f.mes="+mes+" and f.anio="+anio);
  SQL.append(" group by substr(c.cvemanobrc,1,4) ");
  SQL.append(" union all ");
  SQL.append(" select substr(c.cvemanobrc,1,4) clave, ");
  SQL.append(" sum(case when c.tiempoextr = 1 then ((f.fmes*2)*(c.tiempo/60)) else ((c.tiempo/60)*f.fmes) end) importe from ");
  SQL.append(" (select s.cvetrameno from sticalculos s where s.stcalculo in ('1105','1106') and s.mes="+mes+" and s.anio="+anio+") tm, ");
  SQL.append(" ctiregtiempos c, stiplantpers p, stifactormod f ");
  SQL.append(" where tm.cvetrameno = c.cvetrameno and c.curp = p.curp and p.noempleado = f.noempleado and f.mes="+mes+" and f.anio="+anio);
  SQL.append(" and to_char(c.fechacaptu,'mm')="+mes+" and to_char(c.fechacaptu,'yyyy')="+anio);
  SQL.append(" group by substr(c.cvemanobrc,1,4) ");
  SQL.append(" union all ");
  SQL.append(" select substr(c.cvemanobrc,1,4) clave, ");
  SQL.append(" sum(case when c.tiempoextr = 1 then ((f.fmes*2)*(c.tiempo/60)) else ((c.tiempo/60)*f.fmes) end) importe from ");
  SQL.append(" (select s.cvetrainte from sticalculos s where s.stcalculo in ('1107','1108') and s.mes="+mes+" and s.anio="+anio+") ti, ");
  SQL.append(" ctiregtiempos c, stiplantpers p, stifactormod f ");
  SQL.append(" where ti.cvetrainte = c.cvetrainte and c.curp = p.curp and p.noempleado = f.noempleado and f.mes="+mes+" and f.anio="+anio);
  SQL.append(" and to_char(c.fechacaptu,'mm')="+mes+" and to_char(c.fechacaptu,'yyyy')="+anio);
  SQL.append(" group by substr(c.cvemanobrc,1,4)) mano ");
  SQL.append(" group by mano.clave) modnc ");
  SQL.append(" where modc.clave(+) = substr(h.cvemanobrc,1,4) and modnc.clave(+) = substr(h.cvemanobrc,1,4) ");
  SQL.append(" and h.mes="+mes+" and h.anio="+anio);
  SQL.append(" group by h.idproceso order by h.idproceso ");

  try{
    crs = new CachedRowSet();
    crs.setCommand(SQL.toString());
    crs.execute(con);
    crs.beforeFirst();
    i=0;
    while (crs.next()) {
      i++;
      impMODc = decimales(Double.parseDouble((crs.getString("modc")== null)? "0.00":crs.getString("modc")));
      impMODnc= decimales(Double.parseDouble((crs.getString("modnc")== null)? "0.00":crs.getString("modnc")));
      impMOD  = decimales(impMOD)+decimales(impMODc)+decimales(impMODnc);
      variable="IMPORTE_"+i+"_1_1";
      hm.put(variable,decimales(impMODc));
      variable="IMPORTE_"+i+"_2_1";
      hm.put(variable,decimales(impMODnc));
    }
    if (impMOD > 0.00) { hm.put("IMPORTE_0001",decimales(impMOD)); }
    else hm.clear();
  }
  catch (Exception e){
    System.out.println("Ha ocurrido un error en el metodo SiafmCostosProduccion.ceManoDeObraCM");
    System.out.println("SiafmCostosProduccion.ceManoDeObraCM : " + SQL.toString());
    crs.close();
    crs = null;
    throw e;
  }
  finally {
    SQL.setLength(0);
    SQL = null;
    crs= null;
  } 
  return hm; 
}

/* --------------------------------------------------------------------------------------------------
 * Formas:            CF
 * Descripci�n:       Aplicacion de Gastos Indirectos Estimados
 * Fecha de Creaci�n: 28 deAgosto del 2009
 * Author:            JAUV
 * Parametros:        mes, a�o, numero de orden de produccion, comercializable (0=no y 1=si)
 * Observaciones:     Metodo que genera un crs de la consulta de consumo de importes indirectos 
 *                    estiimados: mano de obra indirecta, materiaprima indirecta y gastos indirectos 
 *                    Regresa un HashMap con variables de forma e importes 
 * -------------------------------------------------------------------------------------------------- */
public HashMap cfGastosIndirectosOP(Connection con, String mes, String anio, String numOrden, String proComerci) throws SQLException, Exception {
                  
  HashMap hm = new HashMap();
  CachedRowSet crs = null;
  StringBuffer SQL = new StringBuffer(" ");
  String variable    ="";
  String lsProComerci="";
  Double impMOI  = 0.00;
  Double impMPI  = 0.00;
  Double impGI   = 0.00;
  Double importe = 0.00;
  int i=0;

  SQL.append(" select h.idproceso, sum(gi.moi) moi, sum(gi.mpi) mpi, sum(gi.gi) gi from stiprocesoh h, ");
  SQL.append(" (select substr(c.cvemanobrc,1,4) clave, sum((c.tiempo/60)*moi.festimado) moi, sum((c.tiempo/60)*mpi.festimado) mpi, sum((c.tiempo/60)*gi.festimado) gi ");
  SQL.append(" from ctiregtiempos c, stifactormoi moi, stifactorgi gi, stifactormpi mpi ");
  SQL.append(" where to_char(c.fechacaptu,'mm')="+mes+" and to_char(c.fechacaptu,'yyyy')="+anio+" and c.numorden="+numOrden);
  SQL.append(" and moi.mes="+mes+" and moi.anio="+anio+" and gi.mes="+mes+" and gi.anio="+anio+" and mpi.mes="+mes+" and mpi.anio="+anio);
  SQL.append(" group by substr(c.cvemanobrc,1,4)) gi ");
  SQL.append(" where gi.clave(+) = substr(h.cvemanobrc,1,4) and h.mes="+mes+" and h.anio="+anio);
  SQL.append(" group by h.idproceso order by h.idproceso ");

  if (proComerci.equals("0")) lsProComerci="2";
  else lsProComerci="1";

  try{
    crs = new CachedRowSet();
    crs.setCommand(SQL.toString());
    crs.execute(con);
    crs.beforeFirst();
    i=0;
    while (crs.next()) {
      i++;
      impMOI = decimales(impMOI)+decimales(Double.parseDouble((crs.getString("moi")== null)? "0.00":crs.getString("moi")));
      impMPI = decimales(impMPI)+decimales(Double.parseDouble((crs.getString("mpi")== null)? "0.00":crs.getString("mpi")));
      impGI  = decimales(impGI)+decimales(Double.parseDouble((crs.getString("gi")== null)? "0.00":crs.getString("gi")));      

      importe = Double.parseDouble((crs.getString("moi")== null)? "0.00":crs.getString("moi"));
      variable="IMPORTE_"+i+"_"+lsProComerci+"_2";
      hm.put(variable,decimales(importe));
      
      importe = Double.parseDouble((crs.getString("mpi")== null)? "0.00":crs.getString("mpi"));
      variable="IMPORTE_"+i+"_"+lsProComerci+"_1";
      hm.put(variable,decimales(importe));
      
      importe = Double.parseDouble((crs.getString("gi")== null)? "0.00":crs.getString("gi"));
      variable="IMPORTE_"+i+"_"+lsProComerci+"_3";
      hm.put(variable,decimales(importe));
      
    }
    if ((impMPI>0.00) || (impMOI>0.00) || (impGI>0.00)) {
      hm.put("IMPORTE_0005",decimales(impMPI));
      hm.put("IMPORTE_0001",decimales(impMOI));
      hm.put("IMPORTE_0002",decimales(impGI)); }
    else hm.clear();      
  }
  catch (Exception e){
    System.out.println("Ha ocurrido un error en el metodo SiafmCostosProduccion.ceGastosIndirectosOP");
    System.out.println("SiafmCostosProduccion.ceGastosIndirectosOP : " + SQL.toString());
    crs.close();
    crs = null;
    throw e;
  }
  finally {
    SQL.setLength(0);
    SQL = null;
    crs= null;
  } 
  return hm; 
}

/* --------------------------------------------------------------------------------------------------
 * Formas:            CG
 * Descripci�n:       Aplicacion de Gastos Indirectos Reales al Cierre de Mes
 * Fecha de Creaci�n: 28 deAgosto del 2009
 * Author:            JAUV
 * Parametros:        mes, a�o
 * Observaciones:     Metodo que genera un crs de la consulta de consumos de gastos indirectos reales
 *                    por mes y a�o al cierre de mes de la produccion en proceso
 *                    Regresa un HashMap con variables de forma e importes 
 * -------------------------------------------------------------------------------------------------- */
public HashMap cgGastosIndirectosCM(Connection con, String mes, String anio) throws SQLException, Exception {
                      
  HashMap hm = new HashMap();
  CachedRowSet crs = null;
  StringBuffer SQL = new StringBuffer(" ");
  String variable  = "";
  int i=0;
  Double impMOI  = 0.00;
  Double impMPI  = 0.00;
  Double impGI   = 0.00;
  Double importe = 0.00;

  SQL.append(" select h.idproceso, sum(gic.importemoi) moic, sum(gic.importempi) mpic, sum(gic.importegi) gic, ");
  SQL.append(" sum(ginc.importemoi) moinc, sum(ginc.importempi) mpinc, sum(ginc.importegi) ginc from stiprocesoh h, ");

  SQL.append(" (select gi.clave, sum(gi.importeMOI) importemoi, sum(gi.importeMPI) importeMPI, sum(gi.importeGI) importeGI from ");
  SQL.append(" (select substr(c.cvemanobrc,1,4) clave, sum((c.tiempo/60)*fmoi.freal) importeMOI, ");
  SQL.append(" sum((c.tiempo/60)*fmpi.freal) importeMPI, sum((c.tiempo/60)*fgi.freal) importeGI from "); 
  SQL.append(" (select s.numconrequ, s.mes, s.anio from sticalculos s, etidescrmater e ");
  SQL.append(" where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo in ('1109','1110') ");
  SQL.append(" and s.numconrequ = e.numconrequ and e.procomerci_ant=1 and s.stregistro is null) req, ctiregtiempos c, stifactormoi fmoi, ");
  SQL.append(" stifactormpi fmpi, stifactorgi fgi ");
  SQL.append(" where req.numconrequ = c.numconrequ and to_char(c.fechacaptu,'mm')="+mes+" and to_char(c.fechacaptu,'yyyy')="+anio);
  SQL.append(" and fmoi.mes="+mes+" and fmoi.anio="+anio);
  SQL.append(" and fmpi.mes="+mes+" and fmpi.anio="+anio+" and fgi.mes="+mes+" and fgi.anio="+anio);
  SQL.append(" group by substr(c.cvemanobrc,1,4) ");
  SQL.append(" union all ");
  SQL.append(" select substr(c.cvemanobrc,1,4) clave, sum((c.tiempo/60)*fmoi.freal) importeMOI, ");
  SQL.append(" sum((c.tiempo/60)*fmpi.freal) importeMPI, sum((c.tiempo/60)*fgi.freal) importeGI from ");
  SQL.append(" (select s.numorden, s.mes, s.anio from sticalculos s, etidescrmater e, etiordenprodu o ");
  SQL.append(" where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo in ('1101','1103','1104') ");
  SQL.append(" and s.numorden = o.numorden and o.numconrequ = e.numconrequ and e.procomerci_ant=1 and s.stregistro is null) op, ");
  SQL.append(" ctiregtiempos c, stifactormoi fmoi, stifactormpi fmpi, stifactorgi fgi ");
  SQL.append(" where op.numorden = c.numorden and to_char(c.fechacaptu,'mm')="+mes+" and to_char(c.fechacaptu,'yyyy')="+anio);
  SQL.append(" and fmoi.mes="+mes+" and fmoi.anio="+anio);
  SQL.append(" and fmpi.mes="+mes+" and fmpi.anio="+anio+" and fgi.mes="+mes+" and fgi.anio="+anio);
  SQL.append(" group by substr(c.cvemanobrc,1,4)) gi ");
  SQL.append(" group by gi.clave) gic, ");

  SQL.append(" (select gasto.clave, sum(gasto.importemoi) importemoi, sum(gasto.importempi) importempi, sum(gasto.importegi) importegi from ");
  SQL.append(" (select substr(c.cvemanobrc,1,4) clave, sum((c.tiempo/60)*fmoi.freal) importeMOI, ");
  SQL.append(" sum((c.tiempo/60)*fmpi.freal) importeMPI, sum((c.tiempo/60)*fgi.freal) importeGI from ");
  SQL.append(" (select s.numconrequ, s.mes, s.anio from sticalculos s, etidescrmater e ");
  SQL.append(" where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo in ('1109','1110') ");
  SQL.append(" and s.numconrequ = e.numconrequ and e.procomerci_ant=0 and s.stregistro is null) req, ctiregtiempos c, stifactormoi fmoi, ");
  SQL.append(" stifactormpi fmpi, stifactorgi fgi ");
  SQL.append(" where req.numconrequ = c.numconrequ and to_char(c.fechacaptu,'mm')="+mes+" and to_char(c.fechacaptu,'yyyy')="+anio);
  SQL.append(" and fmoi.mes="+mes+" and fmoi.anio="+anio+" and fmpi.mes="+mes+" and fmpi.anio="+anio+" and fgi.mes="+mes+" and fgi.anio="+anio);
  SQL.append(" group by substr(c.cvemanobrc,1,4) ");
  SQL.append(" union all ");
  SQL.append(" select substr(c.cvemanobrc,1,4) clave, sum((c.tiempo/60)*fmoi.freal) importeMOI, ");
  SQL.append(" sum((c.tiempo/60)*fmpi.freal) importeMPI, sum((c.tiempo/60)*fgi.freal) importeGI from ");
  SQL.append(" (select s.numorden, s.mes, s.anio from sticalculos s, etidescrmater e, etiordenprodu o ");
  SQL.append(" where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo in ('1101','1103','1104') ");
  SQL.append(" and s.numorden = o.numorden and o.numconrequ = e.numconrequ and e.procomerci_ant=0 and s.stregistro is null) op, ");
  SQL.append(" ctiregtiempos c, stifactormoi fmoi, stifactormpi fmpi, stifactorgi fgi ");
  SQL.append(" where op.numorden = c.numorden and to_char(c.fechacaptu,'mm')="+mes+" and to_char(c.fechacaptu,'yyyy')="+anio);
  SQL.append(" and fmoi.mes="+mes+" and fmoi.anio="+anio+" and fmpi.mes="+mes+" and fmpi.anio="+anio+" and fgi.mes="+mes+" and fgi.anio="+anio);
  SQL.append(" group by substr(c.cvemanobrc,1,4) ");
  SQL.append(" union all ");
  SQL.append(" select substr(c.cvemanobrc,1,4) clave, sum((c.tiempo/60)*fmoi.freal) importeMOI, ");
  SQL.append(" sum((c.tiempo/60)*fmpi.freal) importeMPI, sum((c.tiempo/60)*fgi.freal) importeGI from ");
  SQL.append(" (select s.cvetrameno from sticalculos s where s.stcalculo in ('1105','1106') and s.mes="+mes+" and s.anio="+anio+") tm, ");
  SQL.append(" ctiregtiempos c, stifactormoi fmoi, stifactormpi fmpi, stifactorgi fgi ");
  SQL.append(" where tm.cvetrameno = c.cvetrameno and to_char(c.fechacaptu,'mm')="+mes+" and to_char(c.fechacaptu,'yyyy')="+anio);
  SQL.append(" and fmoi.mes="+mes+" and fmoi.anio="+anio+" and fmpi.mes="+mes+" and fmpi.anio="+anio+" and fgi.mes="+mes+" and fgi.anio="+anio);
  SQL.append(" group by substr(c.cvemanobrc,1,4) ");
  SQL.append(" union all ");
  SQL.append(" select substr(c.cvemanobrc,1,4) clave, sum((c.tiempo/60)*fmoi.freal) importeMOI, ");
  SQL.append(" sum((c.tiempo/60)*fmpi.freal) importeMPI, sum((c.tiempo/60)*fgi.freal) importeGI from ");
  SQL.append(" (select s.cvetrainte from sticalculos s where s.stcalculo in ('1107','1108') and s.mes="+mes+" and s.anio="+anio+") ti, ");
  SQL.append(" ctiregtiempos c, stifactormoi fmoi, stifactormpi fmpi, stifactorgi fgi ");
  SQL.append(" where ti.cvetrainte = c.cvetrainte and to_char(c.fechacaptu,'mm')="+mes+" and to_char(c.fechacaptu,'yyyy')="+anio);
  SQL.append(" and fmoi.mes="+mes+" and fmoi.anio="+anio+" and fmpi.mes="+mes+" and fmpi.anio="+anio+" and fgi.mes="+mes+" and fgi.anio="+anio);
  SQL.append(" group by substr(c.cvemanobrc,1,4)) gasto ");
  SQL.append(" group by gasto.clave) ginc ");

  SQL.append(" where gic.clave(+) = substr(h.cvemanobrc,1,4) and ginc.clave(+) = substr(h.cvemanobrc,1,4) ");
  SQL.append(" and h.mes="+mes+" and h.anio="+anio);
  SQL.append(" group by h.idproceso order by h.idproceso ");

  try{
    crs = new CachedRowSet();
    crs.setCommand(SQL.toString());
    crs.execute(con);
    crs.beforeFirst();
    i=0;
    while (crs.next()) {
      i++;
      importe = Double.parseDouble((crs.getString("moic")== null)? "0.00":crs.getString("moic"));
      variable="IMPORTE_"+i+"_1_2";
      hm.put(variable,decimales(importe));
      impMOI = decimales(impMOI) + decimales(importe);
      importe = Double.parseDouble((crs.getString("moinc")== null)? "0.00":crs.getString("moinc"));
      variable="IMPORTE_"+i+"_2_2";
      hm.put(variable,decimales(importe));
      impMOI = decimales(impMOI) + decimales(importe);
      
      importe = Double.parseDouble((crs.getString("mpic")== null)? "0.00":crs.getString("mpic"));
      variable="IMPORTE_"+i+"_1_1";
      hm.put(variable,decimales(importe));
      impMPI = decimales(impMPI) + decimales(importe);
      importe = Double.parseDouble((crs.getString("mpinc")== null)? "0.00":crs.getString("mpinc"));
      variable="IMPORTE_"+i+"_2_1";
      hm.put(variable,decimales(importe));
      impMPI = decimales(impMPI) + decimales(importe);
        
      importe = Double.parseDouble((crs.getString("gic")== null)? "0.00":crs.getString("gic"));
      variable="IMPORTE_"+i+"_1_3";
      hm.put(variable,decimales(importe));
      impGI  = decimales(impGI) + decimales(importe);
      importe = Double.parseDouble((crs.getString("ginc")== null)? "0.00":crs.getString("ginc"));
      variable="IMPORTE_"+i+"_2_3";
      hm.put(variable,decimales(importe));
      impGI  = decimales(impGI) + decimales(importe);
    }
    if ((impMPI>0.00) || (impMOI>0.00) || (impGI>0.00)) {
      hm.put("IMPORTE_0005",decimales(impMPI));
      hm.put("IMPORTE_0001",decimales(impMOI));
      hm.put("IMPORTE_0002",decimales(impGI));  }
    else hm.clear();      
  }    
  catch (Exception e){
    System.out.println("Ha ocurrido un error en el metodo SiafmCostosProduccion.cgGastosIndirectosCM");
    System.out.println("SiafmCostosProduccion.cgGastosIndirectosCM : " + SQL.toString());
    crs.close();
    crs = null;
    throw e;
  }
  finally {
    SQL.setLength(0);
    SQL = null;
    crs= null;
  } 
  return hm; 
}

/* -------------------------------------------------------------------------------------------------------
 * Formas:            CH
 * Descripci�n:       Poliza de Traspaso de uan Orden de Produccion Terminada en el mes al Almacen Central
 * Fecha de Creaci�n: 31 de Agosto del 2009
 * Author:            JAUV
 * Parametros:        numorden, procomerci, mes, a�o, cveTipPro
 * Observaciones:     Metodo que genera:
 *    - CRS con la Producci�n Terminada
 *    - CRS con la Produccion Terminada No Comercializable
 *    - CRS con la Produccion Terminada por Clasificacion de Producto
 *    Al cierre de mes, consultando por mes y a�o
 *    Regresa un HashMap con variables de forma e importes 
 * ------------------------------------------------------------------------------------------------------- */
 public HashMap chProduccionTerminadaOP(Connection con, String mes, String anio, String numorden, String procomerci, String cvetippro) throws SQLException, Exception {
       
  HashMap hm = new HashMap();
  CachedRowSet crs   = null;
  StringBuffer SQL   = new StringBuffer(" ");

  Double impMOD=0.00, impMPD=0.00, impMOI=0.00,  impMPI=0.00,  impGI=0.00,  impTE=0.00;
  Double importe=0.00;
  Double totalD = 0.00, totalH = 0.00;
  int i=0;

  SQL.append(" select h.idproceso, h.descripcion, ");
  SQL.append(" sum(mod.importemod) mod, sum(mpd.importe) mpd   , sum(gi.importemoi) moi, ");
  SQL.append(" sum(gi.importempi) mpi , sum(gi.importegi) gi   , sum(aj.importemod) amod, ");
  SQL.append(" sum(aj.importemoi) amoi, sum(aj.importempi) ampi, sum(aj.importegi) agi, ");
  SQL.append(" sum(te.importe) te from stiprocesoh h,  ");

  SQL.append(" (select mano.clave, sum(mano.importe) importemod from ");
  SQL.append(" (select substr(c.cvemanobrc,1,4) clave, ");
  SQL.append(" sum(case when c.tiempoextr = 1 then ((f.fmes*2)*(c.tiempo/60)) else ((c.tiempo/60)*f.fmes) end) importe from ");
  SQL.append(" (select sti.numorden, sti.mes, sti.anio from ");
  SQL.append(" (select s.numorden from sticalculos s, etiordenprodu o, etidescrmater e ");
  SQL.append(" where s.mes="+mes+" and s.anio="+anio+" and s.numorden = o.numorden ");
  SQL.append(" and o.numconrequ = e.numconrequ and s.numorden="+numorden+") ord, sticalculos sti ");
  SQL.append(" where ord.numorden = sti.numorden) orden, ctiregtiempos c, stiplantpers p, stifactormod f ");
  SQL.append(" where orden.numorden = c.numorden and to_char(c.fechacaptu,'mm') = orden.mes and to_char(c.fechacaptu,'yyyy') = orden.anio ");
  SQL.append(" and c.curp = p.curp and p.noempleado = f.noempleado and f.mes = orden.mes and f.anio = orden.anio ");
  SQL.append(" group by substr(c.cvemanobrc,1,4) ");
  SQL.append(" union all ");
  SQL.append(" select substr(c.cvemanobrc,1,4) clave, ");
  SQL.append(" sum(case when c.tiempoextr = 1 then ((f.fmes*2)*(c.tiempo/60)) else ((c.tiempo/60)*f.fmes) end) importe from ");
  SQL.append(" (select sti.numconrequ, sti.mes, sti.anio from ");
  SQL.append(" (select e.numconrequ from sticalculos s, etiordenprodu o, etidescrmater e ");
  SQL.append(" where s.mes="+mes+" and s.anio="+anio+" and s.numorden = o.numorden ");
  SQL.append(" and o.numconrequ = e.numconrequ and s.numorden="+numorden+") req, sticalculos sti ");
  SQL.append(" where req.numconrequ = sti.numconrequ) requerimiento, ctiregtiempos c, stiplantpers p, stifactormod f ");
  SQL.append(" where requerimiento.numconrequ = c.numconrequ and to_char(c.fechacaptu,'mm') = requerimiento.mes ");
  SQL.append(" and to_char(c.fechacaptu,'yyyy') = requerimiento.anio and c.curp = p.curp and p.noempleado = f.noempleado ");
  SQL.append(" and f.mes = requerimiento.mes and f.anio = requerimiento.anio ");
  SQL.append(" group by substr(c.cvemanobrc,1,4)) mano ");
  SQL.append(" group by mano.clave) mod, ");

  SQL.append(" (select substr(c.cvemanobrc,1,4) clave, sum(t.haber) importe from ");
  SQL.append(" (select sti.numorden, sti.mes, sti.anio from ");
  SQL.append(" (select s.numorden from sticalculos s, etiordenprodu o, etidescrmater e ");
  SQL.append(" where s.mes="+mes+" and s.anio="+anio+" and s.numorden = o.numorden ");
  SQL.append(" and o.numconrequ = e.numconrequ and s.numorden="+numorden+") ord, sticalculos sti ");
  SQL.append(" where ord.numorden = sti.numorden) orden, ctireginsumos c, stitarjetamat t ");
  SQL.append(" where orden.numorden = c.numorden and to_char(c.fechacaptu,'mm')=orden.mes and to_char(c.fechacaptu,'yyyy')=orden.anio ");
  SQL.append(" and c.conseinsum = t.referencia and t.mes=orden.mes and t.anio = orden.anio ");
  SQL.append(" group by substr(c.cvemanobrc,1,4)) mpd, ");

  SQL.append(" (select materia.clave, sum(materia.importemoi) importemoi, sum(materia.importempi) importempi, sum(materia.importegi) importegi from ");
  SQL.append(" (select substr(c.cvemanobrc,1,4) clave, sum((c.tiempo/60)*fmo.freal) importeMOI, ");
  SQL.append(" sum((c.tiempo/60)*fmp.freal) importeMPI, sum((c.tiempo/60)*fgi.freal) importeGI from ");
  SQL.append(" (select sti.numorden, sti.mes, sti.anio from ");
  SQL.append(" (select s.numorden from sticalculos s, etiordenprodu o, etidescrmater e ");
  SQL.append(" where s.mes="+mes+" and s.anio="+anio+" and s.numorden = o.numorden ");
  SQL.append(" and o.numconrequ = e.numconrequ and s.numorden="+numorden+") ord, sticalculos sti ");
  SQL.append(" where ord.numorden = sti.numorden) orden, ctiregtiempos c, stifactormoi fmo, stifactormpi fmp, stifactorgi fgi ");
  SQL.append(" where orden.numorden = c.numorden and to_char(c.fechacaptu,'mm') = orden.mes and to_char(c.fechacaptu,'yyyy') = orden.anio ");
  SQL.append(" and fmo.mes = orden.mes and fmo.anio = orden.anio ");
  SQL.append(" and fmp.mes = orden.mes and fmp.anio = orden.anio ");
  SQL.append(" and fgi.mes = orden.mes and fgi.anio = orden.anio ");
  SQL.append(" group by substr(c.cvemanobrc,1,4) ");
  SQL.append(" union all ");
  SQL.append(" select substr(c.cvemanobrc,1,4) clave, sum((c.tiempo/60)*fmo.freal) importeMOI, ");
  SQL.append(" sum((c.tiempo/60)*fmp.freal) importeMPI, sum((c.tiempo/60)*fgi.freal) importeGI from ");
  SQL.append(" (select sti.numconrequ, sti.mes, sti.anio from ");
  SQL.append(" (select e.numconrequ from sticalculos s, etiordenprodu o, etidescrmater e ");
  SQL.append(" where s.mes="+mes+" and s.anio="+anio+" and s.numorden = o.numorden ");
  SQL.append(" and o.numconrequ = e.numconrequ and s.numorden="+numorden+") req, sticalculos sti ");
  SQL.append(" where req.numconrequ = sti.numconrequ) requerimiento, ctiregtiempos c, stifactormoi fmo, stifactormpi fmp, stifactorgi fgi ");
  SQL.append(" where requerimiento.numconrequ = c.numconrequ and to_char(c.fechacaptu,'mm') = requerimiento.mes and to_char(c.fechacaptu,'yyyy') = requerimiento.anio ");
  SQL.append(" and fmo.mes = requerimiento.mes and fmo.anio = requerimiento.anio ");
  SQL.append(" and fmp.mes = requerimiento.mes and fmp.anio = requerimiento.anio ");
  SQL.append(" and fgi.mes = requerimiento.mes and fgi.anio = requerimiento.anio ");
  SQL.append(" group by substr(c.cvemanobrc,1,4)) materia ");
  SQL.append(" group by materia.clave) gi, ");

  SQL.append(" (select faj.clave, sum(faj.importeMOD) importemod, ");
  SQL.append(" sum(faj.importeMOI) importemoi, sum(faj.importeMPI) importempi, sum(faj.importeGI) importegi from ");
  SQL.append(" (select substr(c.cvemanobrc,1,4) clave, sum((c.tiempo/60)*aj.fmod) importeMOD, ");
  SQL.append(" sum((c.tiempo/60)*aj.fmoi) importeMOI, sum((c.tiempo/60)*aj.fmpi) importeMPI, sum((c.tiempo/60)*aj.fgi) importeGI from ");
  SQL.append(" (select sti.numorden, sti.mes, sti.anio from ");
  SQL.append(" (select s.numorden from sticalculos s, etiordenprodu o, etidescrmater e ");
  SQL.append(" where s.mes="+mes+" and s.anio="+anio+" and s.numorden = o.numorden ");
  SQL.append(" and o.numconrequ = e.numconrequ and s.numorden="+numorden+") ord, sticalculos sti ");
  SQL.append(" where ord.numorden = sti.numorden) orden, ctiregtiempos c, stiajuste aj ");
  SQL.append(" where orden.numorden = c.numorden and to_char(c.fechacaptu,'mm') = orden.mes and to_char(c.fechacaptu,'yyyy') = orden.anio ");
  SQL.append(" and aj.mes = orden.mes and aj.anio = orden.anio ");
  SQL.append(" group by substr(c.cvemanobrc,1,4) ");
  SQL.append(" union all  ");
  SQL.append(" select substr(c.cvemanobrc,1,4) clave, sum((c.tiempo/60)*aj.fmod) importeMOD, ");
  SQL.append(" sum((c.tiempo/60)*aj.fmoi) importeMOI, sum((c.tiempo/60)*aj.fmpi) importeMPI, sum((c.tiempo/60)*aj.fgi) importeGI from ");
  SQL.append(" (select sti.numconrequ, sti.mes, sti.anio from ");
  SQL.append(" (select e.numconrequ from sticalculos s, etiordenprodu o, etidescrmater e  ");
  SQL.append(" where s.mes="+mes+" and s.anio="+anio+" and s.numorden = o.numorden ");
  SQL.append(" and o.numconrequ = e.numconrequ and s.numorden="+numorden+") req, sticalculos sti ");
  SQL.append(" where req.numconrequ = sti.numconrequ) requerimiento, ctiregtiempos c, stiajuste aj ");
  SQL.append(" where requerimiento.numconrequ = c.numconrequ and to_char(c.fechacaptu,'mm') = requerimiento.mes and to_char(c.fechacaptu,'yyyy') = requerimiento.anio ");
  SQL.append(" and aj.mes = requerimiento.mes and aj.anio = requerimiento.anio ");
  SQL.append(" group by substr(c.cvemanobrc,1,4)) faj ");
  SQL.append(" group by faj.clave) aj, ");

  SQL.append(" (select substr(text.clave,1,4) clave, text.importe from  ");
  SQL.append(" (select t.idproceso clave, sum(t.monto) importe from  ");
  SQL.append(" (select s.numorden, s.mes, s.anio from sticalculos s, etidescrmater e, etiordenprodu o ");
  SQL.append(" where s.mes="+mes+" and s.anio="+anio+" and s.numorden = o.numorden and o.numconrequ = e.numconrequ ");
  SQL.append(" and s.numorden="+numorden+" and s.stregistro is null) op, stitallerext t ");
  SQL.append(" where op.numorden = t.numorden ");
  SQL.append(" group by t.idproceso ");
  SQL.append(" union all ");
  SQL.append(" select c.idproceso clave, sum(c.impviatico+c.imppasajes) importe from  ");
  SQL.append(" (select s.numorden, s.mes, s.anio from sticalculos s, etidescrmater e, etiordenprodu o ");
  SQL.append(" where s.mes="+mes+" and s.anio="+anio+" and s.numorden = o.numorden and o.numconrequ = e.numconrequ ");
  SQL.append(" and s.numorden="+numorden+" and s.stregistro is null) op, sticomisiones c ");
  SQL.append(" where op.numorden = c.numorden ");
  SQL.append(" group by c.idproceso) text) te ");

  SQL.append(" where substr(h.cvemanobrc,1,4) = mod.clave(+) and substr(h.cvemanobrc,1,4) = mpd.clave(+) ");
  SQL.append(" and substr(h.cvemanobrc,1,4) = gi.clave(+)  and substr(h.cvemanobrc,1,4) = aj.clave(+) ");
  SQL.append(" and substr(h.cvemanobrc,1,4) = te.clave(+)  and h.mes="+mes+" and h.anio="+anio);
  SQL.append(" group by h.idproceso, h.descripcion  ");

  try{
    totalH=0.00; totalD=0.00;

    crs = new CachedRowSet();
    crs.setCommand(SQL.toString());
    crs.execute(con);
    crs.beforeFirst();

    while (crs.next()) {
      i++;
      impMOD = Double.parseDouble((crs.getString("mod")== null)? "0.00":crs.getString("mod"));
      impMPD = Double.parseDouble((crs.getString("mpd")== null)? "0.00":crs.getString("mpd"));
      impMOI = Double.parseDouble((crs.getString("moi")== null)? "0.00":crs.getString("moi"));
      impMPI = Double.parseDouble((crs.getString("mpi")== null)? "0.00":crs.getString("mpi"));      
      impGI  = Double.parseDouble((crs.getString("gi")== null)?  "0.00":crs.getString("gi"));      
      impTE  = Double.parseDouble((crs.getString("te")== null)?  "0.00":crs.getString("te"));      
      // Carga variables e Importes
      hm.put("IMPORTE_1_"+i+"_"+procomerci+"_1",decimales(impMPD)); 
      hm.put("IMPORTE_2_"+i+"_"+procomerci+"_1",decimales(impTE));  
      hm.put("IMPORTE_3_"+i+"_"+procomerci+"_1",decimales(impMOD)); 
      hm.put("IMPORTE_4_"+i+"_"+procomerci+"_1",decimales(impMPI)); 
      hm.put("IMPORTE_5_"+i+"_"+procomerci+"_1",decimales(impMOI)); 
      hm.put("IMPORTE_6_"+i+"_"+procomerci+"_1",decimales(impGI));  
      totalH = decimales(totalH)+decimales(impMPD)+ decimales(impTE)+ decimales(impMOD)+ decimales(impMPI)+ decimales(impMOI)+decimales(impGI);
    }
    if (cvetippro.equals("01")) hm.put("IMPORTE_1_1",decimales(totalH));
    if (cvetippro.equals("02")) hm.put("IMPORTE_1_2",decimales(totalH));
    if (cvetippro.equals("03")) hm.put("IMPORTE_1_3",decimales(totalH));
    if (cvetippro.equals("04")) hm.put("IMPORTE_2_1",decimales(totalH));
    if (cvetippro.equals("05")) hm.put("IMPORTE_2_2",decimales(totalH));
    if (cvetippro.equals("06")) hm.put("IMPORTE_2_3",decimales(totalH));
    if (cvetippro.equals("07")) hm.put("IMPORTE_1_4",decimales(totalH));
  }    
  catch (Exception e){
    System.out.println("Ha ocurrido un error en el metodo SiafmCostosProduccion.chProduccionTerminadaCM");
    System.out.println("SiafmCostosProduccion.chProduccionTerminadaOP : " + SQL.toString());
    crs.close();
    crs  = null;
    throw e;
  }
  finally {
    SQL.setLength(0);
    SQL  =null;
    crs  =null;
  } 
  return hm; 
}

/* --------------------------------------------------------------------------------------------------
 * Formas:            CH
 * Descripci�n:       Poliza de Traspaso de la Produccion Terminada al Almacen Central
 * Fecha de Creaci�n: 31 de Agosto del 2009
 * Author:            JAUV
 * Parametros:        mes, a�o
 * Observaciones:     Metodo que genera:
 *                    - CRS con la Produccion Terminada Comercializable
 *                    - CRS con la Produccion Terminada No Comercializable
 *                    - CRS con la Produccion Terminada por Clasificacion de Producto
 *                    Al cierre de mes, consultando por mes y a�o
 *                    Regresa un HashMap con variables de forma e importes 
 * -------------------------------------------------------------------------------------------------- */
 public HashMap chProduccionTerminadaCM(Connection con, String mes, String anio) throws SQLException, Exception {
                       
  HashMap hm = new HashMap();
  CachedRowSet crsC  = null;
  CachedRowSet crsNC = null;
  CachedRowSet crs   = null;
  StringBuffer SQL   = new StringBuffer(" ");
  StringBuffer SQLc  = new StringBuffer(" ");
  StringBuffer SQLnc = new StringBuffer(" ");

  Double impMODc=0.00, impMPDc=0.00, impMOIc=0.00,  impMPIc=0.00,  impGIc=0.00,  impTEc=0.00;
  Double impMODnc=0.00,impMPDnc=0.00,impMOInc=0.00, impMPInc=0.00, impGInc=0.00, impTEnc=0.00;
  Double importe=0.00;
  Double totalD = 0.00, totalH = 0.00;
  Double importe0600 = 0.00;
  String lsCveTipPro="";

  int i=0;

  /* ---------------------------------------
   * Produccion Terminada Comercializable 
   * --------------------------------------- */
  SQLc.append(" select h.idproceso, h.descripcion, ");
  SQLc.append(" sum(mod.importemod) modc, sum(mpd.importe) mpdc   , sum(gi.importemoi) moic, ");
  SQLc.append(" sum(gi.importempi) mpic , sum(gi.importegi) gic   , sum(aj.importemod) amodc, ");
  SQLc.append(" sum(aj.importemoi) amoic, sum(aj.importempi) ampic, sum(aj.importegi) agic, ");
  SQLc.append(" sum(te.importe) tec from stiprocesoh h, ");
  SQLc.append(" (select mano.clave, sum(mano.importe) importemod from ");
  SQLc.append(" (select substr(c.cvemanobrc,1,4) clave, ");
  SQLc.append(" sum(case when c.tiempoextr = 1 then ((f.fmes*2)*(c.tiempo/60)) else ((c.tiempo/60)*f.fmes) end) importe from ");
  SQLc.append(" (select sti.numorden, sti.mes, sti.anio from  ");
  SQLc.append(" (select s.numorden from sticalculos s, etiordenprodu o, etidescrmater e   ");
  SQLc.append(" where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo='1103' and s.numorden = o.numorden  ");
  SQLc.append(" and o.numconrequ = e.numconrequ and e.procomerci_ant=1) ord, sticalculos sti ");
  SQLc.append(" where ord.numorden = sti.numorden) orden, ctiregtiempos c, stiplantpers p, stifactormod f ");
  SQLc.append(" where orden.numorden = c.numorden and to_char(c.fechacaptu,'mm') = orden.mes and to_char(c.fechacaptu,'yyyy') = orden.anio ");
  SQLc.append(" and c.curp = p.curp and p.noempleado = f.noempleado and f.mes = orden.mes and f.anio = orden.anio ");
  SQLc.append(" group by substr(c.cvemanobrc,1,4)  ");
  SQLc.append(" union all ");
  SQLc.append(" select substr(c.cvemanobrc,1,4) clave,  ");
  SQLc.append(" sum(case when c.tiempoextr = 1 then ((f.fmes*2)*(c.tiempo/60)) else ((c.tiempo/60)*f.fmes) end) importe from ");
  SQLc.append(" (select sti.numconrequ, sti.mes, sti.anio from ");
  SQLc.append(" (select e.numconrequ from sticalculos s, etiordenprodu o, etidescrmater e   ");
  SQLc.append(" where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo = '1103' and s.numorden = o.numorden  ");
  SQLc.append(" and o.numconrequ = e.numconrequ and e.procomerci_ant=1) req, sticalculos sti ");
  SQLc.append(" where req.numconrequ = sti.numconrequ) requerimiento, ctiregtiempos c, stiplantpers p, stifactormod f ");
  SQLc.append(" where requerimiento.numconrequ = c.numconrequ and to_char(c.fechacaptu,'mm') = requerimiento.mes  ");
  SQLc.append(" and to_char(c.fechacaptu,'yyyy') = requerimiento.anio and c.curp = p.curp and p.noempleado = f.noempleado  ");
  SQLc.append(" and f.mes = requerimiento.mes and f.anio = requerimiento.anio ");
  SQLc.append(" group by substr(c.cvemanobrc,1,4)) mano ");
  SQLc.append(" group by mano.clave) mod, ");
  SQLc.append(" (select substr(c.cvemanobrc,1,4) clave, sum(t.haber) importe from  ");
  SQLc.append(" (select sti.numorden, sti.mes, sti.anio from  ");
  SQLc.append(" (select s.numorden from sticalculos s, etiordenprodu o, etidescrmater e   ");
  SQLc.append(" where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo = '1103' and s.numorden = o.numorden  ");
  SQLc.append(" and o.numconrequ = e.numconrequ and e.procomerci_ant=1) ord, sticalculos sti ");
  SQLc.append(" where ord.numorden = sti.numorden) orden, ctireginsumos c, stitarjetamat t ");
  SQLc.append(" where orden.numorden = c.numorden and to_char(c.fechacaptu,'mm')=orden.mes and to_char(c.fechacaptu,'yyyy')=orden.anio ");
  SQLc.append(" and c.conseinsum = t.referencia and t.mes=orden.mes and t.anio = orden.anio ");
  SQLc.append(" group by substr(c.cvemanobrc,1,4)) mpd, ");
  SQLc.append(" (select materia.clave, sum(materia.importemoi) importemoi, sum(materia.importempi) importempi, sum(materia.importegi) importegi from "); 
  SQLc.append(" (select substr(c.cvemanobrc,1,4) clave, sum((c.tiempo/60)*fmo.freal) importeMOI, ");
  SQLc.append(" sum((c.tiempo/60)*fmp.freal) importeMPI, sum((c.tiempo/60)*fgi.freal) importeGI from ");
  SQLc.append(" (select sti.numorden, sti.mes, sti.anio from ");
  SQLc.append(" (select s.numorden from sticalculos s, etiordenprodu o, etidescrmater e ");  
  SQLc.append(" where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo = '1103' and s.numorden = o.numorden "); 
  SQLc.append(" and o.numconrequ = e.numconrequ and e.procomerci_ant=1) ord, sticalculos sti ");
  SQLc.append(" where ord.numorden = sti.numorden) orden, ctiregtiempos c, stifactormoi fmo, stifactormpi fmp, stifactorgi fgi ");
  SQLc.append(" where orden.numorden = c.numorden and to_char(c.fechacaptu,'mm') = orden.mes and to_char(c.fechacaptu,'yyyy') = orden.anio ");
  SQLc.append(" and fmo.mes = orden.mes and fmo.anio = orden.anio ");
  SQLc.append(" and fmp.mes = orden.mes and fmp.anio = orden.anio ");
  SQLc.append(" and fgi.mes = orden.mes and fgi.anio = orden.anio ");
  SQLc.append(" group by substr(c.cvemanobrc,1,4) ");
  SQLc.append(" union all ");
  SQLc.append(" select substr(c.cvemanobrc,1,4) clave, sum((c.tiempo/60)*fmo.freal) importeMOI, ");
  SQLc.append(" sum((c.tiempo/60)*fmp.freal) importeMPI, sum((c.tiempo/60)*fgi.freal) importeGI from ");
  SQLc.append(" (select sti.numconrequ, sti.mes, sti.anio from ");
  SQLc.append(" (select e.numconrequ from sticalculos s, etiordenprodu o, etidescrmater e "); 
  SQLc.append(" where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo = '1103' and s.numorden = o.numorden "); 
  SQLc.append(" and o.numconrequ = e.numconrequ and e.procomerci_ant=1) req, sticalculos sti ");
  SQLc.append(" where req.numconrequ = sti.numconrequ) requerimiento, ctiregtiempos c, stifactormoi fmo, stifactormpi fmp, stifactorgi fgi ");
  SQLc.append(" where requerimiento.numconrequ = c.numconrequ and to_char(c.fechacaptu,'mm') = requerimiento.mes and to_char(c.fechacaptu,'yyyy') = requerimiento.anio ");
  SQLc.append(" and fmo.mes = requerimiento.mes and fmo.anio = requerimiento.anio ");
  SQLc.append(" and fmp.mes = requerimiento.mes and fmp.anio = requerimiento.anio ");
  SQLc.append(" and fgi.mes = requerimiento.mes and fgi.anio = requerimiento.anio ");
  SQLc.append(" group by substr(c.cvemanobrc,1,4)) materia ");
  SQLc.append(" group by materia.clave) gi, ");
  SQLc.append(" (select faj.clave, sum(faj.importeMOD) importemod, "); 
  SQLc.append(" sum(faj.importeMOI) importemoi, sum(faj.importeMPI) importempi, sum(faj.importeGI) importegi from ");
  SQLc.append(" (select substr(c.cvemanobrc,1,4) clave, sum((c.tiempo/60)*aj.fmod) importeMOD, "); 
  SQLc.append(" sum((c.tiempo/60)*aj.fmoi) importeMOI, sum((c.tiempo/60)*aj.fmpi) importeMPI, sum((c.tiempo/60)*aj.fgi) importeGI from ");
  SQLc.append(" (select sti.numorden, sti.mes, sti.anio from "); 
  SQLc.append(" (select s.numorden from sticalculos s, etiordenprodu o, etidescrmater e "); 
  SQLc.append(" where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo = '1103' and s.numorden = o.numorden "); 
  SQLc.append(" and o.numconrequ = e.numconrequ and e.procomerci_ant=1) ord, sticalculos sti ");
  SQLc.append(" where ord.numorden = sti.numorden) orden, ctiregtiempos c, stiajuste aj ");
  SQLc.append(" where orden.numorden = c.numorden and to_char(c.fechacaptu,'mm') = orden.mes and to_char(c.fechacaptu,'yyyy') = orden.anio ");
  SQLc.append(" and aj.mes = orden.mes and aj.anio = orden.anio ");
  SQLc.append(" group by substr(c.cvemanobrc,1,4) ");
  SQLc.append(" union all "); 
  SQLc.append(" select substr(c.cvemanobrc,1,4) clave, sum((c.tiempo/60)*aj.fmod) importeMOD, "); 
  SQLc.append(" sum((c.tiempo/60)*aj.fmoi) importeMOI, sum((c.tiempo/60)*aj.fmpi) importeMPI, sum((c.tiempo/60)*aj.fgi) importeGI from ");
  SQLc.append(" (select sti.numconrequ, sti.mes, sti.anio from ");
  SQLc.append(" (select e.numconrequ from sticalculos s, etiordenprodu o, etidescrmater e "); 
  SQLc.append(" where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo='1103' and s.numorden = o.numorden "); 
  SQLc.append(" and o.numconrequ = e.numconrequ and e.procomerci_ant=1) req, sticalculos sti ");
  SQLc.append(" where req.numconrequ = sti.numconrequ) requerimiento, ctiregtiempos c, stiajuste aj ");
  SQLc.append(" where requerimiento.numconrequ = c.numconrequ and to_char(c.fechacaptu,'mm') = requerimiento.mes and to_char(c.fechacaptu,'yyyy') = requerimiento.anio ");
  SQLc.append(" and aj.mes = requerimiento.mes and aj.anio = requerimiento.anio ");
  SQLc.append(" group by substr(c.cvemanobrc,1,4)) faj ");
  SQLc.append(" group by faj.clave) aj, "); 
  SQLc.append(" (select substr(text.clave,1,4) clave, text.importe from "); 
  SQLc.append(" (select t.idproceso clave, sum(t.monto) importe from "); 
  SQLc.append(" (select s.numorden, s.mes, s.anio from sticalculos s, etidescrmater e, etiordenprodu o ");
  SQLc.append(" where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo in ('1103') "); 
  SQLc.append(" and s.numorden = o.numorden and o.numconrequ = e.numconrequ and e.procomerci_ant=1 and s.stregistro is null) op, stitallerext t ");
  SQLc.append(" where op.numorden = t.numorden ");
  SQLc.append(" group by t.idproceso ");
  SQLc.append(" union all ");
  SQLc.append(" select c.idproceso clave, sum(c.impviatico+c.imppasajes) importe from "); 
  SQLc.append(" (select s.numorden, s.mes, s.anio from sticalculos s, etidescrmater e, etiordenprodu o ");
  SQLc.append(" where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo in ('1103') "); 
  SQLc.append(" and s.numorden = o.numorden and o.numconrequ = e.numconrequ and e.procomerci_ant=1 and s.stregistro is null) op, sticomisiones c ");
  SQLc.append(" where op.numorden = c.numorden ");
  SQLc.append(" group by c.idproceso) text) te ");
  SQLc.append(" where substr(h.cvemanobrc,1,4) = mod.clave(+) and substr(h.cvemanobrc,1,4) = mpd.clave(+) ");
  SQLc.append(" and substr(h.cvemanobrc,1,4) = gi.clave(+)  and substr(h.cvemanobrc,1,4) = aj.clave(+) ");
  SQLc.append(" and substr(h.cvemanobrc,1,4) = te.clave(+)  and h.mes="+mes+" and h.anio="+anio);
  SQLc.append(" group by h.idproceso, h.descripcion order by h.idproceso  "); 

  /* ---------------------------------------
   * Produccion Terminada No Comercializable 
   * --------------------------------------- */
  SQLnc.append(" select h.idproceso, h.descripcion, ");
  SQLnc.append(" sum(trunc(mod.importemod,8)) modnc, sum(trunc(mpd.importe,8)) mpdnc   , sum(trunc(gi.importemoi,8)) moinc, ");
  SQLnc.append(" sum(trunc(gi.importempi,8)) mpinc , sum(trunc(gi.importegi,8)) ginc   , sum(trunc(aj.importemod,8)) amodnc, ");
  SQLnc.append(" sum(trunc(aj.importemoi,8)) amoinc, sum(trunc(aj.importempi,8)) ampinc, sum(trunc(aj.importegi,8)) aginc, ");
  SQLnc.append(" sum(trunc(te.importe,8)) tenc from stiprocesoh h, ");
  SQLnc.append(" (select mano.clave, sum(mano.importe) importemod from ");
  SQLnc.append(" (select substr(c.cvemanobrc,1,4) clave, ");
  SQLnc.append(" sum(case when c.tiempoextr=1 then ((f.fmes*2)*(c.tiempo/60)) else ((c.tiempo/60)*f.fmes) end) importe from ");
  SQLnc.append(" (select sti.numorden, sti.mes, sti.anio from "); 
  SQLnc.append(" (select s.numorden from sticalculos s, etiordenprodu o, etidescrmater e ");  
  SQLnc.append(" where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo = '1103' and s.numorden = o.numorden ");
  SQLnc.append(" and o.numconrequ = e.numconrequ and e.procomerci_ant=0) ord, sticalculos sti ");
  SQLnc.append(" where ord.numorden = sti.numorden) orden, ctiregtiempos c, stiplantpers p, stifactormod f ");
  SQLnc.append(" where orden.numorden = c.numorden and to_char(c.fechacaptu,'mm')=orden.mes and to_char(c.fechacaptu,'yyyy') = orden.anio ");
  SQLnc.append(" and c.curp=p.curp and p.noempleado = f.noempleado and f.mes=orden.mes and f.anio = orden.anio ");
  SQLnc.append(" group by substr(c.cvemanobrc,1,4) ");
  SQLnc.append(" union all ");
  SQLnc.append(" select substr(c.cvemanobrc,1,4) clave, ");
  SQLnc.append(" sum(case when c.tiempoextr = 1 then ((f.fmes*2)*(c.tiempo/60)) else ((c.tiempo/60)*f.fmes) end) importe from ");
  SQLnc.append(" (select sti.numconrequ, sti.mes, sti.anio from ");
  SQLnc.append(" (select e.numconrequ from sticalculos s, etiordenprodu o, etidescrmater e ");
  SQLnc.append(" where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo = '1103' and s.numorden = o.numorden ");
  SQLnc.append(" and o.numconrequ = e.numconrequ and e.procomerci_ant=0) req, sticalculos sti ");
  SQLnc.append(" where req.numconrequ = sti.numconrequ) requerimiento, ctiregtiempos c, stiplantpers p, stifactormod f ");
  SQLnc.append(" where requerimiento.numconrequ = c.numconrequ and to_char(c.fechacaptu,'mm') = requerimiento.mes ");
  SQLnc.append(" and to_char(c.fechacaptu,'yyyy') = requerimiento.anio and c.curp = p.curp and p.noempleado = f.noempleado "); 
  SQLnc.append(" and f.mes = requerimiento.mes and f.anio = requerimiento.anio ");
  SQLnc.append(" group by substr(c.cvemanobrc,1,4)) mano ");
  SQLnc.append(" group by mano.clave) mod, ");
  SQLnc.append(" (select substr(c.cvemanobrc,1,4) clave, sum(t.haber) importe from "); 
  SQLnc.append(" (select sti.numorden, sti.mes, sti.anio from "); 
  SQLnc.append(" (select s.numorden from sticalculos s, etiordenprodu o, etidescrmater e ");  
  SQLnc.append(" where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo = '1103' and s.numorden = o.numorden ");
  SQLnc.append(" and o.numconrequ = e.numconrequ and e.procomerci_ant=0) ord, sticalculos sti ");
  SQLnc.append(" where ord.numorden = sti.numorden) orden, ctireginsumos c, stitarjetamat t ");
  SQLnc.append(" where orden.numorden = c.numorden and to_char(c.fechacaptu,'mm')=orden.mes and to_char(c.fechacaptu,'yyyy')=orden.anio ");
  SQLnc.append(" and c.conseinsum = t.referencia and t.mes=orden.mes and t.anio = orden.anio "); 
  SQLnc.append(" group by substr(c.cvemanobrc,1,4)) mpd, ");
  SQLnc.append(" (select materia.clave, sum(materia.importemoi) importemoi, sum(materia.importempi) importempi, sum(materia.importegi) importegi from "); 
  SQLnc.append(" (select substr(c.cvemanobrc,1,4) clave, sum((c.tiempo/60)*fmo.freal) importeMOI, "); 
  SQLnc.append(" sum((c.tiempo/60)*fmp.freal) importeMPI, sum((c.tiempo/60)*fgi.freal) importeGI from ");
  SQLnc.append(" (select sti.numorden, sti.mes, sti.anio from "); 
  SQLnc.append(" (select s.numorden from sticalculos s, etiordenprodu o, etidescrmater e ");  
  SQLnc.append(" where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo = '1103' and s.numorden = o.numorden "); 
  SQLnc.append(" and o.numconrequ = e.numconrequ and e.procomerci_ant=0) ord, sticalculos sti ");
  SQLnc.append(" where ord.numorden = sti.numorden) orden, ctiregtiempos c, stifactormoi fmo, stifactormpi fmp, stifactorgi fgi ");
  SQLnc.append(" where orden.numorden = c.numorden and to_char(c.fechacaptu,'mm') = orden.mes and to_char(c.fechacaptu,'yyyy') = orden.anio ");
  SQLnc.append(" and fmo.mes = orden.mes and fmo.anio = orden.anio ");
  SQLnc.append(" and fmp.mes = orden.mes and fmp.anio = orden.anio ");
  SQLnc.append(" and fgi.mes = orden.mes and fgi.anio = orden.anio ");
  SQLnc.append(" group by substr(c.cvemanobrc,1,4) "); 
  SQLnc.append(" union all "); 
  SQLnc.append(" select substr(c.cvemanobrc,1,4) clave, sum((c.tiempo/60)*fmo.freal) importeMOI, "); 
  SQLnc.append(" sum((c.tiempo/60)*fmp.freal) importeMPI, sum((c.tiempo/60)*fgi.freal) importeGI from ");
  SQLnc.append(" (select sti.numconrequ, sti.mes, sti.anio from ");
  SQLnc.append(" (select e.numconrequ from sticalculos s, etiordenprodu o, etidescrmater e ");  
  SQLnc.append(" where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo='1103' and s.numorden = o.numorden "); 
  SQLnc.append(" and o.numconrequ = e.numconrequ and e.procomerci_ant=0) req, sticalculos sti ");
  SQLnc.append(" where req.numconrequ = sti.numconrequ) requerimiento, ctiregtiempos c, stifactormoi fmo, stifactormpi fmp, stifactorgi fgi ");
  SQLnc.append(" where requerimiento.numconrequ = c.numconrequ and to_char(c.fechacaptu,'mm') = requerimiento.mes and to_char(c.fechacaptu,'yyyy') = requerimiento.anio ");
  SQLnc.append(" and fmo.mes = requerimiento.mes and fmo.anio = requerimiento.anio ");
  SQLnc.append(" and fmp.mes = requerimiento.mes and fmp.anio = requerimiento.anio ");
  SQLnc.append(" and fgi.mes = requerimiento.mes and fgi.anio = requerimiento.anio ");
  SQLnc.append(" group by substr(c.cvemanobrc,1,4)) materia ");
  SQLnc.append(" group by materia.clave) gi, ");
  SQLnc.append(" (select faj.clave, sum(faj.importeMOD) importemod, "); 
  SQLnc.append(" sum(faj.importeMOI) importemoi, sum(faj.importeMPI) importempi, sum(faj.importeGI) importegi from ");
  SQLnc.append(" (select substr(c.cvemanobrc,1,4) clave, sum((c.tiempo/60)*aj.fmod) importeMOD, "); 
  SQLnc.append(" sum((c.tiempo/60)*aj.fmoi) importeMOI, sum((c.tiempo/60)*aj.fmpi) importeMPI, sum((c.tiempo/60)*aj.fgi) importeGI from ");
  SQLnc.append(" (select sti.numorden, sti.mes, sti.anio from "); 
  SQLnc.append(" (select s.numorden from sticalculos s, etiordenprodu o, etidescrmater e  "); 
  SQLnc.append(" where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo = '1103' and s.numorden = o.numorden  ");
  SQLnc.append(" and o.numconrequ = e.numconrequ and e.procomerci_ant=0) ord, sticalculos sti ");
  SQLnc.append(" where ord.numorden = sti.numorden) orden, ctiregtiempos c, stiajuste aj ");
  SQLnc.append(" where orden.numorden = c.numorden and to_char(c.fechacaptu,'mm') = orden.mes and to_char(c.fechacaptu,'yyyy') = orden.anio ");
  SQLnc.append(" and aj.mes = orden.mes and aj.anio = orden.anio ");
  SQLnc.append(" group by substr(c.cvemanobrc,1,4)  ");
  SQLnc.append(" union all  ");
  SQLnc.append(" select substr(c.cvemanobrc,1,4) clave, sum((c.tiempo/60)*aj.fmod) importeMOD, "); 
  SQLnc.append(" sum((c.tiempo/60)*aj.fmoi) importeMOI, sum((c.tiempo/60)*aj.fmpi) importeMPI, sum((c.tiempo/60)*aj.fgi) importeGI from ");
  SQLnc.append(" (select sti.numconrequ, sti.mes, sti.anio from ");
  SQLnc.append(" (select e.numconrequ from sticalculos s, etiordenprodu o, etidescrmater e ");  
  SQLnc.append(" where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo = '1103' and s.numorden = o.numorden "); 
  SQLnc.append(" and o.numconrequ = e.numconrequ and e.procomerci_ant=0) req, sticalculos sti ");
  SQLnc.append(" where req.numconrequ = sti.numconrequ) requerimiento, ctiregtiempos c, stiajuste aj ");
  SQLnc.append(" where requerimiento.numconrequ = c.numconrequ and to_char(c.fechacaptu,'mm') = requerimiento.mes and to_char(c.fechacaptu,'yyyy') = requerimiento.anio ");
  SQLnc.append(" and aj.mes = requerimiento.mes and aj.anio = requerimiento.anio ");
  SQLnc.append(" group by substr(c.cvemanobrc,1,4)) faj ");
  SQLnc.append(" group by faj.clave) aj, "); 
  SQLnc.append(" (select substr(text.clave,1,4) clave, text.importe from "); 
  SQLnc.append(" (select t.idproceso clave, sum(t.monto) importe from  ");
  SQLnc.append(" (select s.numorden, s.mes, s.anio from sticalculos s, etidescrmater e, etiordenprodu o ");
  SQLnc.append(" where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo in ('1103')  ");
  SQLnc.append(" and s.numorden = o.numorden and o.numconrequ = e.numconrequ and e.procomerci_ant=0 and s.stregistro is null) op, stitallerext t ");
  SQLnc.append(" where op.numorden = t.numorden ");
  SQLnc.append(" group by t.idproceso ");
  SQLnc.append(" union all ");
  SQLnc.append(" select c.idproceso clave, sum(c.impviatico+c.imppasajes) importe from  ");
  SQLnc.append(" (select s.numorden, s.mes, s.anio from sticalculos s, etidescrmater e, etiordenprodu o ");
  SQLnc.append(" where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo in ('1103')  ");
  SQLnc.append(" and s.numorden = o.numorden and o.numconrequ = e.numconrequ and e.procomerci_ant=0 and s.stregistro is null) op, sticomisiones c ");
  SQLnc.append(" where op.numorden = c.numorden ");
  SQLnc.append(" group by c.idproceso) text) te ");
  SQLnc.append(" where substr(h.cvemanobrc,1,4) = mod.clave(+) and substr(h.cvemanobrc,1,4) = mpd.clave(+) ");
  SQLnc.append(" and substr(h.cvemanobrc,1,4) = gi.clave(+)  and substr(h.cvemanobrc,1,4) = aj.clave(+) ");
  SQLnc.append(" and substr(h.cvemanobrc,1,4) = te.clave(+)  and h.mes="+mes+" and h.anio="+anio);
  SQLnc.append(" group by h.idproceso, h.descripcion order by  h.idproceso   ");

  /* --------------------------------------------------------
   * Produccion Terminada Con Importes Acumulados por Proceso
   * -------------------------------------------------------- */
  SQL.append(" select gtc.cvetippro, gtc.desctipopro descripcion, (nvl(mod.importe,0)+nvl(mpd.importe,0)+nvl(te.importe,0)+nvl(gi.moi,0)+nvl(gi.mpi,0)+nvl(gi.gi,0)) importe from ");

  SQL.append(" (select oprod.cvetippro, sum(oprod.importe) importe from ");
  SQL.append(" (select r.cvetippro, sum((c.tiempo/60)*f.fmes) importe from ");
  SQL.append(" (select req.numconrequ, substr(eti.cvetippro,1,2) cvetippro, cal.mes, cal.anio from ");
  SQL.append(" (select s.numorden, o.numconrequ from sticalculos s, etiordenprodu o where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo ='1103' "); 
  SQL.append(" and s.numorden = o.numorden) req, sticalculos cal, etidescrmater eti where req.numconrequ = cal.numconrequ ");
  SQL.append(" and req.numconrequ = eti.numconrequ) r, ctiregtiempos c, stiplantpers p, stifactormod f ");
  SQL.append(" where r.numconrequ = c.numconrequ and c.curp = p.curp and p.noempleado = f.noempleado and f.mes = r.mes and f.anio = r.anio ");
  SQL.append(" and to_char(c.fechacaptu,'mm') = r.mes and to_char(c.fechacaptu,'yyyy') = r.anio ");
  SQL.append(" group by r.cvetippro ");
  SQL.append(" union all ");
  SQL.append(" select orden.cvetippro, sum((c.tiempo/60)*f.fmes) importe from ");
  SQL.append(" (select op.numorden, op.cvetippro, cal.mes, cal.anio from ");
  SQL.append(" (select s.numorden, substr(e.cvetippro,1,2) cvetippro from sticalculos s,etiordenprodu o, etidescrmater e where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo ='1103' ");
  SQL.append(" and s.numorden = o.numorden and o.numconrequ = e.numconrequ) op, sticalculos cal ");
  SQL.append(" where op.numorden = cal.numorden) orden, ctiregtiempos c, stiplantpers p, stifactormod f ");
  SQL.append(" where orden.numorden = c.numorden and c.curp = p.curp and p.noempleado = f.noempleado and f.mes = orden.mes and f.anio = orden.anio ");
  SQL.append(" and to_char(c.fechacaptu,'mm') = orden.mes and to_char(c.fechacaptu,'yyyy') = orden.anio ");
  SQL.append(" group by orden.cvetippro) oprod ");
  SQL.append(" group by oprod.cvetippro) mod, "); 
  SQL.append(" (select orden.cvetippro, sum(p.haber) importe from ");
  SQL.append(" (select op.numorden, op.cvetippro, cal.mes, cal.anio from ");
  SQL.append(" (select s.numorden, substr(e.cvetippro,1,2) cvetippro from sticalculos s,etiordenprodu o, etidescrmater e where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo ='1103' ");
  SQL.append(" and s.numorden = o.numorden and o.numconrequ = e.numconrequ) op, sticalculos cal ");
  SQL.append(" where op.numorden = cal.numorden) orden, ctireginsumos c, stitarjetamat p ");
  SQL.append(" where orden.numorden = c.numorden and to_char(c.fechacaptu,'mm') = orden.mes and to_char(c.fechacaptu,'yyyy') = orden.anio ");
  SQL.append(" and c.conseinsum = p.referencia ");
  SQL.append(" group by orden.cvetippro) mpd, ");
  SQL.append(" (select te.cvetippro, sum(te.monto) importe from ");
  SQL.append(" (select op.cvetippro cvetippro, s.monto monto from ");
  SQL.append(" (select s.numorden, substr(e.cvetippro,1,2) cvetippro from sticalculos s,etiordenprodu o, etidescrmater e where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo ='1103' ");
  SQL.append(" and s.numorden = o.numorden and o.numconrequ = e.numconrequ) op, stitallerext s ");
  SQL.append(" where op.numorden = s.numorden ");
  SQL.append(" union all ");
  SQL.append(" select op.cvetippro cvetippro, (h.impviatico+h.imppasajes) monto from ");
  SQL.append(" (select s.numorden, substr(e.cvetippro,1,2) cvetippro from sticalculos s,etiordenprodu o, etidescrmater e where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo ='1103' ");
  SQL.append(" and s.numorden = o.numorden and o.numconrequ = e.numconrequ) op, sticomisiones h ");
  SQL.append(" where op.numorden = h.numorden) te ");
  SQL.append(" group by te.cvetippro) te, ");
  SQL.append(" (select giop.cvetippro, sum(giop.moi) moi, sum(giop.mpi) mpi, sum(giop.gi) gi from  ");
  SQL.append(" (select orden.cvetippro, sum((c.tiempo/60)*m.freal) moi, sum((c.tiempo/60)*p.freal) mpi, sum((c.tiempo/60)*g.freal) gi from ");
  SQL.append(" (select op.numorden, op.cvetippro, cal.mes, cal.anio from ");
  SQL.append(" (select s.numorden, substr(e.cvetippro,1,2) cvetippro from sticalculos s,etiordenprodu o, etidescrmater e where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo ='1103' ");
  SQL.append(" and s.numorden = o.numorden and o.numconrequ = e.numconrequ) op, sticalculos cal ");
  SQL.append(" where op.numorden = cal.numorden) orden, ctiregtiempos c, stifactormoi m, stifactormpi p, stifactorgi g ");
  SQL.append(" where orden.numorden = c.numorden ");
  SQL.append(" and to_char(c.fechacaptu,'mm') = orden.mes and to_char(c.fechacaptu,'yyyy') = orden.anio ");
  SQL.append(" and m.mes = orden.mes and m.anio = orden.anio ");
  SQL.append(" and p.mes = orden.mes and p.anio = orden.anio ");
  SQL.append(" and g.mes = orden.mes and g.anio = orden.anio ");
  SQL.append(" group by orden.cvetippro ");
  SQL.append(" union all ");
  SQL.append(" select requerimiento.cvetippro, sum((c.tiempo/60)*m.freal) moi, sum((c.tiempo/60)*p.freal) mpi, sum((c.tiempo/60)*g.freal) gi from  ");
  SQL.append(" (select req.numconrequ, req.cvetippro, cal.mes, cal.anio from ");
  SQL.append(" (select o.numconrequ, substr(e.cvetippro,1,2) cvetippro from sticalculos s, etiordenprodu o, etidescrmater e ");
  SQL.append(" where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo='1103' and s.numorden = o.numorden and o.numconrequ = e.numconrequ) req, sticalculos cal ");
  SQL.append(" where req.numconrequ = cal.numconrequ) requerimiento, ctiregtiempos c, stifactormoi m, stifactormpi p, stifactorgi g ");
  SQL.append(" where requerimiento.numconrequ = c.numconrequ ");
  SQL.append(" and to_char(c.fechacaptu,'mm') = requerimiento.mes and to_char(c.fechacaptu,'yyyy') = requerimiento.anio ");
  SQL.append(" and m.mes = requerimiento.mes and m.anio = requerimiento.anio ");
  SQL.append(" and p.mes = requerimiento.mes and p.anio = requerimiento.anio ");
  SQL.append(" and g.mes = requerimiento.mes and g.anio = requerimiento.anio ");
  SQL.append(" group by requerimiento.cvetippro) giop ");
  SQL.append(" group by giop.cvetippro ) gi, ");
  SQL.append(" (select orden.cvetippro, sum((c.tiempo/60)*fa.fmod) mod, sum((c.tiempo/60)*fa.fmoi) moi, sum((c.tiempo/60)*fa.fmpi) mpi, sum((c.tiempo/60)*fa.fgi) gi from ");
  SQL.append(" (select op.numorden, op.cvetippro, cal.mes, cal.anio from ");
  SQL.append(" (select s.numorden, substr(e.cvetippro,1,2) cvetippro from sticalculos s,etiordenprodu o, etidescrmater e where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo ='1103' ");
  SQL.append(" and s.numorden = o.numorden and o.numconrequ = e.numconrequ) op, sticalculos cal ");
  SQL.append(" where op.numorden = cal.numorden) orden, ctiregtiempos c, stiajuste fa ");
  SQL.append(" where orden.numorden = c.numorden ");
  SQL.append(" and to_char(c.fechacaptu,'mm') = orden.mes and to_char(c.fechacaptu,'yyyy') = orden.anio ");
  SQL.append(" and fa.mes = orden.mes and fa.anio = orden.anio ");
  SQL.append(" group by orden.cvetippro) aj, gtctipoproduc gtc ");
  SQL.append(" where substr(gtc.cvetippro,1,2) = mod.cvetippro(+) ");
  SQL.append("   and substr(gtc.cvetippro,1,2) = mpd.cvetippro(+) ");
  SQL.append("   and substr(gtc.cvetippro,1,2) = te.cvetippro(+) ");
  SQL.append("   and substr(gtc.cvetippro,1,2) = gi.cvetippro(+) ");
  SQL.append("   and substr(gtc.cvetippro,1,2) = aj.cvetippro(+) ");
  SQL.append("   and substr(gtc.cvetippro,3,2) = '00' ");
  SQL.append(" order by substr(gtc.cvetippro,1,2) ");

  try{
    // CRS Producci�n Terminada al Cierre de Mes Comercializable
    crsC = new CachedRowSet();
    crsC.setCommand(SQLc.toString());
    crsC.execute(con);
    crsC.beforeFirst();
    // CRS Producci�n Terminada al Cierre de Mes No Comercializable
    crsNC = new CachedRowSet();
    crsNC.setCommand(SQLnc.toString());
    crsNC.execute(con);
    crsNC.beforeFirst();
    // CRS Producci�n Terminada al Cierre de Mes Acumulado de Importes por cveTipPro
    crs = new CachedRowSet();
    crs.setCommand(SQL.toString());
    crs.execute(con);
    crs.beforeFirst();
    totalH=0.00; totalD=0.00;
    i=0;
    crsNC.next();
    while (crsC.next()) {
      i++;
      //Importes de Comercializables
      impMODc  = Double.parseDouble((crsC.getString("modc")== null)? "0.00":crsC.getString("modc"));
      impMPDc  = Double.parseDouble((crsC.getString("mpdc")== null)? "0.00":crsC.getString("mpdc"));
      impMOIc  = Double.parseDouble((crsC.getString("moic")== null)? "0.00":crsC.getString("moic"));
      impMPIc  = Double.parseDouble((crsC.getString("mpic")== null)? "0.00":crsC.getString("mpic"));      
      impGIc   = Double.parseDouble((crsC.getString("gic")== null)? "0.00":crsC.getString("gic"));      
      impTEc   = Double.parseDouble((crsC.getString("tec")== null)? "0.00":crsC.getString("tec"));      
      //Importes de No Comercializables
      impMODnc  = Double.parseDouble((crsNC.getString("modnc")== null)? "0.00":crsNC.getString("modnc"));
      impMPDnc  = Double.parseDouble((crsNC.getString("mpdnc")== null)? "0.00":crsNC.getString("mpdnc"));
      impMOInc  = Double.parseDouble((crsNC.getString("moinc")== null)? "0.00":crsNC.getString("moinc"));
      impMPInc  = Double.parseDouble((crsNC.getString("mpinc")== null)? "0.00":crsNC.getString("mpinc"));      
      impGInc   = Double.parseDouble((crsNC.getString("ginc") == null)? "0.00":crsNC.getString("ginc"));      
      impTEnc   = Double.parseDouble((crsNC.getString("tenc") == null)? "0.00":crsNC.getString("tenc"));
      // Carga variables e Importes
      hm.put("IMPORTE_1_"+i+"_1_1",decimales(impMPDc)); hm.put("IMPORTE_1_"+i+"_2_1",decimales(impMPDnc));
      hm.put("IMPORTE_2_"+i+"_1_1",decimales(impTEc));  hm.put("IMPORTE_2_"+i+"_2_1",decimales(impTEnc));
      hm.put("IMPORTE_3_"+i+"_1_1",decimales(impMODc)); hm.put("IMPORTE_3_"+i+"_2_1",decimales(impMODnc));
      hm.put("IMPORTE_4_"+i+"_1_1",decimales(impMPIc)); hm.put("IMPORTE_4_"+i+"_2_1",decimales(impMPInc));      
      hm.put("IMPORTE_5_"+i+"_1_1",decimales(impMOIc)); hm.put("IMPORTE_5_"+i+"_2_1",decimales(impMOInc));
      hm.put("IMPORTE_6_"+i+"_1_1",decimales(impGIc));  hm.put("IMPORTE_6_"+i+"_2_1",decimales(impGInc));
      crsNC.next();
      totalH = decimales(totalH)+decimales(impMPDc)+ decimales(impTEc)+ decimales(impMODc)+ decimales(impMPIc)+ decimales(impMOIc)+decimales(impGIc)+decimales(impMPDnc)+decimales(impTEnc)+decimales(impMODnc)+decimales(impMPInc)+decimales(impMOInc)+decimales(impGInc);
    }
    i=0;
    while (crs.next()) {
      i++;
      lsCveTipPro= ((crs.getString("cvetippro")== null)? "":crs.getString("cvetippro"));
      importe    = Double.parseDouble((crs.getString("importe") == null)? "0.00":crs.getString("importe"));
      totalD=totalD+decimales(importe);
      if (lsCveTipPro.equals("0100")) hm.put("IMPORTE_1_1",decimales(importe));
      if (lsCveTipPro.equals("0200")) hm.put("IMPORTE_1_2",decimales(importe));
      if (lsCveTipPro.equals("0300")) hm.put("IMPORTE_1_3",decimales(importe));
      if (lsCveTipPro.equals("0400")) hm.put("IMPORTE_2_1",decimales(importe));
      if (lsCveTipPro.equals("0500")) hm.put("IMPORTE_2_2",decimales(importe));
      if (lsCveTipPro.equals("0600")) importe0600=decimales(importe);
      if (lsCveTipPro.equals("0700")) hm.put("IMPORTE_1_4",decimales(importe));
    }
    importe0600 = importe0600+(totalH-totalD);
    hm.put("IMPORTE_2_3",decimales(importe0600));
  }    
  catch (Exception e){
    System.out.println("Ha ocurrido un error en el metodo SiafmCostosProduccion.chProduccionTerminadaCM");
    System.out.println("SiafmCostosProduccion.chProduccionTerminadaCM : " + SQL.toString());
    crs.close();
    crsC.close();
    crsNC.close();
    crs  = null;
    crsC = null;
    crsNC= null;
    throw e;
  }
  finally {
    SQL.setLength(0);
    SQL  =null;
    SQLc.setLength(0);
    SQLc =null;
    SQLnc.setLength(0);
    SQLnc=null;
    crs  =null;
    crsC =null;
    crsNC=null;
  } 
  return hm; 
}

/* --------------------------------------------------------------------------------------------------
 * Formas:            CI
 *  Descripci�n:       Poliza de Producci�n Cancelada al Cierre de Mes
 * Fecha de Creaci�n: 1 de Septiembre del 2009
 * Author:            JAUV
 * Parametros:        mes, a�o
 * Observaciones:     Metodo que genera:
 *                    - CRS de consulta de importes de la Produccion Cancelada Comercializable
 *                    - CRS de consulta de importes de la Produccion Cancelada No Comercializable
 *                    Al cierre de mes, consultando por mes y a�o
 *                    Regresa un HashMap con variables de forma e importes 
 * -------------------------------------------------------------------------------------------------- */
 public HashMap ciProduccionCanceladaCM(Connection con, String mes, String anio) throws SQLException, Exception {
                       
  HashMap hm = new HashMap();
  CachedRowSet crsC  = null;
  CachedRowSet crsNC = null;

  StringBuffer SQLc  = new StringBuffer(" ");
  StringBuffer SQLnc = new StringBuffer(" ");

  Double impMO=0.00, impMP=0.00, impGI=0.00;
  Double impMODc=0.00, impMPDc=0.00, impMOIc=0.00,  impMPIc=0.00,  impGIc=0.00,  impTEc=0.00;
  Double impMODnc=0.00,impMPDnc=0.00,impMOInc=0.00, impMPInc=0.00, impGInc=0.00, impTEnc=0.00;
  int i=0;

  /* ---------------------------------------
   * Produccion Cancelada Comercializable 
   * --------------------------------------- */
  SQLc.append(" select h.idproceso, h.descripcion, ");
  SQLc.append(" sum(trunc(mod.importemod,8)) modc, sum(trunc(mpd.importe,8)) mpdc   , sum(trunc(gi.importemoi,8)) moic, ");
  SQLc.append(" sum(trunc(gi.importempi,8)) mpic , sum(trunc(gi.importegi,8)) gic   , sum(trunc(aj.importemod,8)) amodc, ");
  SQLc.append(" sum(trunc(aj.importemoi,8)) amoic, sum(trunc(aj.importempi,8)) ampic, sum(trunc(aj.importegi,8)) agic, ");
  SQLc.append(" sum(trunc(te.importe,8)) tec from stiprocesoh h, ");
  SQLc.append(" (select mano.clave, sum(mano.importe) importemod from ");
  SQLc.append(" (select substr(c.cvemanobrc,1,4) clave, ");
  SQLc.append(" sum(case when c.tiempoextr = 1 then ((f.fmes*2)*(c.tiempo/60)) else ((c.tiempo/60)*f.fmes) end) importe from ");
  SQLc.append(" (select sti.numorden, sti.mes, sti.anio from "); 
  SQLc.append(" (select s.numorden from sticalculos s, etiordenprodu o, etidescrmater e ");
  SQLc.append(" where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo = '1104' and s.numorden = o.numorden ");
  SQLc.append(" and o.numconrequ = e.numconrequ and e.procomerci_ant=1) ord, sticalculos sti ");
  SQLc.append(" where ord.numorden = sti.numorden) orden, ctiregtiempos c, stiplantpers p, stifactormod f ");
  SQLc.append(" where orden.numorden = c.numorden and to_char(c.fechacaptu,'mm') = orden.mes and to_char(c.fechacaptu,'yyyy') = orden.anio ");
  SQLc.append(" and c.curp = p.curp and p.noempleado = f.noempleado and f.mes = orden.mes and f.anio = orden.anio ");
  SQLc.append(" group by substr(c.cvemanobrc,1,4) "); 
  SQLc.append(" union all ");
  SQLc.append(" select substr(c.cvemanobrc,1,4) clave, "); 
  SQLc.append(" sum(case when c.tiempoextr = 1 then ((f.fmes*2)*(c.tiempo/60)) else ((c.tiempo/60)*f.fmes) end) importe from ");
  SQLc.append(" (select sti.numconrequ, sti.mes, sti.anio from ");
  SQLc.append(" (select e.numconrequ from sticalculos s, etiordenprodu o, etidescrmater e ");
  SQLc.append(" where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo = '1104' and s.numorden = o.numorden ");
  SQLc.append(" and o.numconrequ = e.numconrequ and e.procomerci_ant=1) req, sticalculos sti ");
  SQLc.append(" where req.numconrequ = sti.numconrequ) requerimiento, ctiregtiempos c, stiplantpers p, stifactormod f ");
  SQLc.append(" where requerimiento.numconrequ = c.numconrequ and to_char(c.fechacaptu,'mm') = requerimiento.mes ");
  SQLc.append(" and to_char(c.fechacaptu,'yyyy') = requerimiento.anio and c.curp = p.curp and p.noempleado = f.noempleado ");
  SQLc.append(" and f.mes = requerimiento.mes and f.anio = requerimiento.anio ");
  SQLc.append(" group by substr(c.cvemanobrc,1,4)) mano ");
  SQLc.append(" group by mano.clave) mod, ");
  SQLc.append(" (select substr(c.cvemanobrc,1,4) clave, sum(t.haber) importe from ");
  SQLc.append(" (select sti.numorden, sti.mes, sti.anio from ");
  SQLc.append(" (select s.numorden from sticalculos s, etiordenprodu o, etidescrmater e ");
  SQLc.append(" where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo = '1104' and s.numorden = o.numorden ");
  SQLc.append(" and o.numconrequ = e.numconrequ and e.procomerci_ant=1) ord, sticalculos sti ");
  SQLc.append(" where ord.numorden = sti.numorden) orden, ctireginsumos c, stitarjetamat t ");
  SQLc.append(" where orden.numorden = c.numorden and to_char(c.fechacaptu,'mm')=orden.mes and to_char(c.fechacaptu,'yyyy')=orden.anio ");
  SQLc.append(" and c.conseinsum = t.referencia and t.mes=orden.mes and t.anio = orden.anio ");
  SQLc.append(" group by substr(c.cvemanobrc,1,4)) mpd, ");
  SQLc.append(" (select materia.clave, sum(materia.importemoi) importemoi, sum(materia.importempi) importempi, sum(materia.importegi) importegi from ");
  SQLc.append(" (select substr(c.cvemanobrc,1,4) clave, sum((c.tiempo/60)*fmo.freal) importeMOI, ");
  SQLc.append(" sum((c.tiempo/60)*fmp.freal) importeMPI, sum((c.tiempo/60)*fgi.freal) importeGI from ");
  SQLc.append(" (select sti.numorden, sti.mes, sti.anio from ");
  SQLc.append(" (select s.numorden from sticalculos s, etiordenprodu o, etidescrmater e ");
  SQLc.append(" where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo = '1104' and s.numorden = o.numorden ");
  SQLc.append(" and o.numconrequ = e.numconrequ and e.procomerci_ant=1) ord, sticalculos sti ");
  SQLc.append(" where ord.numorden = sti.numorden) orden, ctiregtiempos c, stifactormoi fmo, stifactormpi fmp, stifactorgi fgi ");
  SQLc.append(" where orden.numorden = c.numorden and to_char(c.fechacaptu,'mm') = orden.mes and to_char(c.fechacaptu,'yyyy') = orden.anio ");
  SQLc.append(" and fmo.mes = orden.mes and fmo.anio = orden.anio ");
  SQLc.append(" and fmp.mes = orden.mes and fmp.anio = orden.anio ");
  SQLc.append(" and fgi.mes = orden.mes and fgi.anio = orden.anio ");
  SQLc.append(" group by substr(c.cvemanobrc,1,4) ");
  SQLc.append(" union all ");
  SQLc.append(" select substr(c.cvemanobrc,1,4) clave, sum((c.tiempo/60)*fmo.freal) importeMOI, ");
  SQLc.append(" sum((c.tiempo/60)*fmp.freal) importeMPI, sum((c.tiempo/60)*fgi.freal) importeGI from ");
  SQLc.append(" (select sti.numconrequ, sti.mes, sti.anio from ");
  SQLc.append(" (select e.numconrequ from sticalculos s, etiordenprodu o, etidescrmater e ");
  SQLc.append(" where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo = '1104' and s.numorden = o.numorden ");
  SQLc.append(" and o.numconrequ = e.numconrequ and e.procomerci_ant=1) req, sticalculos sti ");
  SQLc.append(" where req.numconrequ = sti.numconrequ) requerimiento, ctiregtiempos c, stifactormoi fmo, stifactormpi fmp, stifactorgi fgi ");
  SQLc.append(" where requerimiento.numconrequ = c.numconrequ and to_char(c.fechacaptu,'mm') = requerimiento.mes and to_char(c.fechacaptu,'yyyy') = requerimiento.anio ");
  SQLc.append(" and fmo.mes = requerimiento.mes and fmo.anio = requerimiento.anio ");
  SQLc.append(" and fmp.mes = requerimiento.mes and fmp.anio = requerimiento.anio ");
  SQLc.append(" and fgi.mes = requerimiento.mes and fgi.anio = requerimiento.anio ");
  SQLc.append(" group by substr(c.cvemanobrc,1,4)) materia ");
  SQLc.append(" group by materia.clave) gi, ");
  SQLc.append(" (select faj.clave, sum(faj.importeMOD) importemod, ");
  SQLc.append(" sum(faj.importeMOI) importemoi, sum(faj.importeMPI) importempi, sum(faj.importeGI) importegi from ");
  SQLc.append(" (select substr(c.cvemanobrc,1,4) clave, sum((c.tiempo/60)*aj.fmod) importeMOD, ");
  SQLc.append(" sum((c.tiempo/60)*aj.fmoi) importeMOI, sum((c.tiempo/60)*aj.fmpi) importeMPI, sum((c.tiempo/60)*aj.fgi) importeGI from ");
  SQLc.append(" (select sti.numorden, sti.mes, sti.anio from ");
  SQLc.append(" (select s.numorden from sticalculos s, etiordenprodu o, etidescrmater e ");
  SQLc.append(" where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo = '1104' and s.numorden = o.numorden ");
  SQLc.append(" and o.numconrequ = e.numconrequ and e.procomerci_ant=1) ord, sticalculos sti ");
  SQLc.append(" where ord.numorden = sti.numorden) orden, ctiregtiempos c, stiajuste aj ");
  SQLc.append(" where orden.numorden = c.numorden and to_char(c.fechacaptu,'mm') = orden.mes and to_char(c.fechacaptu,'yyyy') = orden.anio ");
  SQLc.append(" and aj.mes = orden.mes and aj.anio = orden.anio ");
  SQLc.append(" group by substr(c.cvemanobrc,1,4) ");
  SQLc.append(" union all "); 
  SQLc.append(" select substr(c.cvemanobrc,1,4) clave, sum((c.tiempo/60)*aj.fmod) importeMOD, "); 
  SQLc.append(" sum((c.tiempo/60)*aj.fmoi) importeMOI, sum((c.tiempo/60)*aj.fmpi) importeMPI, sum((c.tiempo/60)*aj.fgi) importeGI from ");
  SQLc.append(" (select sti.numconrequ, sti.mes, sti.anio from ");
  SQLc.append(" (select e.numconrequ from sticalculos s, etiordenprodu o, etidescrmater e ");
  SQLc.append(" where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo = '1104' and s.numorden = o.numorden ");
  SQLc.append(" and o.numconrequ = e.numconrequ and e.procomerci_ant=1) req, sticalculos sti ");
  SQLc.append(" where req.numconrequ = sti.numconrequ) requerimiento, ctiregtiempos c, stiajuste aj ");
  SQLc.append(" where requerimiento.numconrequ = c.numconrequ and to_char(c.fechacaptu,'mm') = requerimiento.mes and to_char(c.fechacaptu,'yyyy') = requerimiento.anio ");
  SQLc.append(" and aj.mes = requerimiento.mes and aj.anio = requerimiento.anio ");
  SQLc.append(" group by substr(c.cvemanobrc,1,4)) faj ");
  SQLc.append(" group by faj.clave) aj, ");
  SQLc.append(" (select substr(text.clave,1,4) clave, text.importe from ");
  SQLc.append(" (select t.idproceso clave, sum(t.monto) importe from ");
  SQLc.append(" (select s.numorden, s.mes, s.anio from sticalculos s, etidescrmater e, etiordenprodu o ");
  SQLc.append(" where s.mes="+mes+" and s.anio= "+anio+" and s.stcalculo in ('1104') ");
  SQLc.append(" and s.numorden = o.numorden and o.numconrequ = e.numconrequ and e.procomerci_ant=1 and s.stregistro is null) op, stitallerext t ");
  SQLc.append(" where op.numorden = t.numorden ");
  SQLc.append(" group by t.idproceso ");
  SQLc.append(" union all ");
  SQLc.append(" select c.idproceso clave, sum(c.impviatico+c.imppasajes) importe from  ");
  SQLc.append(" (select s.numorden, s.mes, s.anio from sticalculos s, etidescrmater e, etiordenprodu o ");
  SQLc.append(" where s.mes="+mes+" and s.anio= "+anio+" and s.stcalculo in ('1104') "); 
  SQLc.append(" and s.numorden = o.numorden and o.numconrequ = e.numconrequ and e.procomerci_ant=1 and s.stregistro is null) op, sticomisiones c ");
  SQLc.append(" where op.numorden = c.numorden ");
  SQLc.append(" group by c.idproceso) text) te ");
  SQLc.append(" where substr(h.cvemanobrc,1,4) = mod.clave(+) and substr(h.cvemanobrc,1,4) = mpd.clave(+) ");
  SQLc.append(" and substr(h.cvemanobrc,1,4) = gi.clave(+)  and substr(h.cvemanobrc,1,4) = aj.clave(+) ");
  SQLc.append(" and substr(h.cvemanobrc,1,4) = te.clave(+)  and h.mes="+mes+" and h.anio="+anio );
  SQLc.append(" group by h.idproceso, h.descripcion  order by  h.idproceso"); 

 /* ---------------------------------------
  * Produccion Cancelada No Comercializable 
  * --------------------------------------- */
  SQLnc.append(" select h.idproceso, h.descripcion, ");
  SQLnc.append(" sum(trunc(mod.importemod,8)) modnc, sum(trunc(mpd.importe,8)) mpdnc   , sum(trunc(gi.importemoi,8)) moinc, ");
  SQLnc.append(" sum(trunc(gi.importempi,8)) mpinc , sum(trunc(gi.importegi,8)) ginc   , sum(trunc(aj.importemod,8)) amodnc, ");
  SQLnc.append(" sum(trunc(aj.importemoi,8)) amoinc, sum(trunc(aj.importempi,8)) ampinc, sum(trunc(aj.importegi,8)) aginc, ");
  SQLnc.append(" sum(trunc(te.importe,8)) tenc from stiprocesoh h, ");
  SQLnc.append(" (select mano.clave, sum(mano.importe) importemod from ");
  SQLnc.append(" (select substr(c.cvemanobrc,1,4) clave,  ");
  SQLnc.append(" sum(case when c.tiempoextr = 1 then ((f.fmes*2)*(c.tiempo/60)) else ((c.tiempo/60)*f.fmes) end) importe from ");
  SQLnc.append(" (select sti.numorden, sti.mes, sti.anio from "); 
  SQLnc.append(" (select s.numorden from sticalculos s, etiordenprodu o, etidescrmater e ");
  SQLnc.append(" where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo = '1104' and s.numorden = o.numorden  ");
  SQLnc.append(" and o.numconrequ = e.numconrequ and e.procomerci_ant=0) ord, sticalculos sti ");
  SQLnc.append(" where ord.numorden = sti.numorden) orden, ctiregtiempos c, stiplantpers p, stifactormod f ");
  SQLnc.append(" where orden.numorden = c.numorden and to_char(c.fechacaptu,'mm') = orden.mes and to_char(c.fechacaptu,'yyyy') = orden.anio ");
  SQLnc.append(" and c.curp = p.curp and p.noempleado = f.noempleado and f.mes=orden.mes and f.anio = orden.anio ");
  SQLnc.append(" group by substr(c.cvemanobrc,1,4)  ");
  SQLnc.append(" union all ");
  SQLnc.append(" select substr(c.cvemanobrc,1,4) clave, "); 
  SQLnc.append(" sum(case when c.tiempoextr = 1 then ((f.fmes*2)*(c.tiempo/60)) else ((c.tiempo/60)*f.fmes) end) importe from ");
  SQLnc.append(" (select sti.numconrequ, sti.mes, sti.anio from ");
  SQLnc.append(" (select e.numconrequ from sticalculos s, etiordenprodu o, etidescrmater e ");
  SQLnc.append(" where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo = '1104' and s.numorden = o.numorden ");
  SQLnc.append(" and o.numconrequ = e.numconrequ and e.procomerci_ant=0) req, sticalculos sti ");
  SQLnc.append(" where req.numconrequ = sti.numconrequ) requerimiento, ctiregtiempos c, stiplantpers p, stifactormod f ");
  SQLnc.append(" where requerimiento.numconrequ = c.numconrequ and to_char(c.fechacaptu,'mm') = requerimiento.mes ");
  SQLnc.append(" and to_char(c.fechacaptu,'yyyy') = requerimiento.anio and c.curp = p.curp and p.noempleado = f.noempleado ");
  SQLnc.append(" and f.mes = requerimiento.mes and f.anio = requerimiento.anio ");
  SQLnc.append(" group by substr(c.cvemanobrc,1,4)) mano ");
  SQLnc.append(" group by mano.clave) mod, ");
  SQLnc.append(" (select substr(c.cvemanobrc,1,4) clave, sum(t.haber) importe from  ");
  SQLnc.append(" (select sti.numorden, sti.mes, sti.anio from  ");
  SQLnc.append(" (select s.numorden from sticalculos s, etiordenprodu o, etidescrmater e  ");
  SQLnc.append(" where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo = '1104' and s.numorden = o.numorden  ");
  SQLnc.append(" and o.numconrequ = e.numconrequ and e.procomerci_ant=0) ord, sticalculos sti ");
  SQLnc.append(" where ord.numorden = sti.numorden) orden, ctireginsumos c, stitarjetamat t ");
  SQLnc.append(" where orden.numorden = c.numorden and to_char(c.fechacaptu,'mm')=orden.mes and to_char(c.fechacaptu,'yyyy')=orden.anio ");
  SQLnc.append(" and c.conseinsum = t.referencia and t.mes=orden.mes and t.anio = orden.anio ");
  SQLnc.append(" group by substr(c.cvemanobrc,1,4)) mpd, ");
  SQLnc.append(" (select materia.clave, sum(materia.importemoi) importemoi, sum(materia.importempi) importempi, sum(materia.importegi) importegi from  ");
  SQLnc.append(" (select substr(c.cvemanobrc,1,4) clave, sum((c.tiempo/60)*fmo.freal) importeMOI,  ");
  SQLnc.append(" sum((c.tiempo/60)*fmp.freal) importeMPI, sum((c.tiempo/60)*fgi.freal) importeGI from ");
  SQLnc.append(" (select sti.numorden, sti.mes, sti.anio from  ");
  SQLnc.append(" (select s.numorden from sticalculos s, etiordenprodu o, etidescrmater e ");
  SQLnc.append(" where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo = '1104' and s.numorden = o.numorden ");
  SQLnc.append(" and o.numconrequ = e.numconrequ and e.procomerci_ant=0) ord, sticalculos sti ");
  SQLnc.append(" where ord.numorden = sti.numorden) orden, ctiregtiempos c, stifactormoi fmo, stifactormpi fmp, stifactorgi fgi ");
  SQLnc.append(" where orden.numorden = c.numorden and to_char(c.fechacaptu,'mm') = orden.mes and to_char(c.fechacaptu,'yyyy') = orden.anio ");
  SQLnc.append(" and fmo.mes = orden.mes and fmo.anio = orden.anio ");
  SQLnc.append(" and fmp.mes = orden.mes and fmp.anio = orden.anio ");
  SQLnc.append(" and fgi.mes = orden.mes and fgi.anio = orden.anio ");
  SQLnc.append(" group by substr(c.cvemanobrc,1,4) "); 
  SQLnc.append(" union all "); 
  SQLnc.append(" select substr(c.cvemanobrc,1,4) clave, sum((c.tiempo/60)*fmo.freal) importeMOI, "); 
  SQLnc.append(" sum((c.tiempo/60)*fmp.freal) importeMPI, sum((c.tiempo/60)*fgi.freal) importeGI from ");
  SQLnc.append(" (select sti.numconrequ, sti.mes, sti.anio from ");
  SQLnc.append(" (select e.numconrequ from sticalculos s, etiordenprodu o, etidescrmater e ");
  SQLnc.append(" where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo = '1104' and s.numorden = o.numorden ");
  SQLnc.append(" and o.numconrequ = e.numconrequ and e.procomerci_ant=0) req, sticalculos sti ");
  SQLnc.append(" where req.numconrequ = sti.numconrequ) requerimiento, ctiregtiempos c, stifactormoi fmo, stifactormpi fmp, stifactorgi fgi ");
  SQLnc.append(" where requerimiento.numconrequ = c.numconrequ and to_char(c.fechacaptu,'mm') = requerimiento.mes and to_char(c.fechacaptu,'yyyy') = requerimiento.anio ");
  SQLnc.append(" and fmo.mes = requerimiento.mes and fmo.anio = requerimiento.anio ");
  SQLnc.append(" and fmp.mes = requerimiento.mes and fmp.anio = requerimiento.anio ");
  SQLnc.append(" and fgi.mes = requerimiento.mes and fgi.anio = requerimiento.anio ");
  SQLnc.append(" group by substr(c.cvemanobrc,1,4)) materia ");
  SQLnc.append(" group by materia.clave) gi, ");
  SQLnc.append(" (select faj.clave, sum(faj.importeMOD) importemod,  ");
  SQLnc.append(" sum(faj.importeMOI) importemoi, sum(faj.importeMPI) importempi, sum(faj.importeGI) importegi from ");
  SQLnc.append(" (select substr(c.cvemanobrc,1,4) clave, sum((c.tiempo/60)*aj.fmod) importeMOD, ");
  SQLnc.append(" sum((c.tiempo/60)*aj.fmoi) importeMOI, sum((c.tiempo/60)*aj.fmpi) importeMPI, sum((c.tiempo/60)*aj.fgi) importeGI from ");
  SQLnc.append(" (select sti.numorden, sti.mes, sti.anio from ");
  SQLnc.append(" (select s.numorden from sticalculos s, etiordenprodu o, etidescrmater e ");
  SQLnc.append(" where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo = '1104' and s.numorden = o.numorden ");
  SQLnc.append(" and o.numconrequ = e.numconrequ and e.procomerci_ant=0) ord, sticalculos sti ");
  SQLnc.append(" where ord.numorden = sti.numorden) orden, ctiregtiempos c, stiajuste aj ");
  SQLnc.append(" where orden.numorden = c.numorden and to_char(c.fechacaptu,'mm') = orden.mes and to_char(c.fechacaptu,'yyyy') = orden.anio ");
  SQLnc.append(" and aj.mes=orden.mes and aj.anio = orden.anio ");
  SQLnc.append(" group by substr(c.cvemanobrc,1,4) ");
  SQLnc.append(" union all ");
  SQLnc.append(" select substr(c.cvemanobrc,1,4) clave, sum((c.tiempo/60)*aj.fmod) importeMOD, ");
  SQLnc.append(" sum((c.tiempo/60)*aj.fmoi) importeMOI, sum((c.tiempo/60)*aj.fmpi) importeMPI, sum((c.tiempo/60)*aj.fgi) importeGI from ");
  SQLnc.append(" (select sti.numconrequ, sti.mes, sti.anio from ");
  SQLnc.append(" (select e.numconrequ from sticalculos s, etiordenprodu o, etidescrmater e ");
  SQLnc.append(" where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo = '1104' and s.numorden = o.numorden ");
  SQLnc.append(" and o.numconrequ = e.numconrequ and e.procomerci_ant=0) req, sticalculos sti ");
  SQLnc.append(" where req.numconrequ = sti.numconrequ) requerimiento, ctiregtiempos c, stiajuste aj ");
  SQLnc.append(" where requerimiento.numconrequ = c.numconrequ and to_char(c.fechacaptu,'mm') = requerimiento.mes and to_char(c.fechacaptu,'yyyy') = requerimiento.anio ");
  SQLnc.append(" and aj.mes = requerimiento.mes and aj.anio = requerimiento.anio ");
  SQLnc.append(" group by substr(c.cvemanobrc,1,4)) faj ");
  SQLnc.append(" group by faj.clave) aj, ");
  SQLnc.append(" (select substr(text.clave,1,4) clave, text.importe from ");
  SQLnc.append(" (select t.idproceso clave, sum(t.monto) importe from ");
  SQLnc.append(" (select s.numorden, s.mes, s.anio from sticalculos s, etidescrmater e, etiordenprodu o ");
  SQLnc.append(" where s.mes="+mes+" and s.anio= "+anio+" and s.stcalculo in ('1104') ");
  SQLnc.append(" and s.numorden = o.numorden and o.numconrequ = e.numconrequ and e.procomerci_ant=0 and s.stregistro is null) op, stitallerext t ");
  SQLnc.append(" where op.numorden = t.numorden ");
  SQLnc.append(" group by t.idproceso ");
  SQLnc.append(" union all ");
  SQLnc.append(" select c.idproceso clave, sum(c.impviatico+c.imppasajes) importe from  ");
  SQLnc.append(" (select s.numorden, s.mes, s.anio from sticalculos s, etidescrmater e, etiordenprodu o ");
  SQLnc.append(" where s.mes="+mes+" and s.anio= "+anio+" and s.stcalculo in ('1104')  ");
  SQLnc.append(" and s.numorden = o.numorden and o.numconrequ = e.numconrequ and e.procomerci_ant=0 and s.stregistro is null) op, sticomisiones c ");
  SQLnc.append(" where op.numorden = c.numorden ");
  SQLnc.append(" group by c.idproceso) text) te ");
  SQLnc.append(" where substr(h.cvemanobrc,1,4) = mod.clave(+) and substr(h.cvemanobrc,1,4) = mpd.clave(+) ");
  SQLnc.append(" and substr(h.cvemanobrc,1,4) = gi.clave(+)  and substr(h.cvemanobrc,1,4) = aj.clave(+) ");
  SQLnc.append(" and substr(h.cvemanobrc,1,4) = te.clave(+)  and h.mes="+mes+" and h.anio="+anio);
  SQLnc.append(" group by h.idproceso, h.descripcion order by h.idproceso ");

  try{
    // CRS Producci�n Terminada al Cierre de Mes Comercializable
    crsC = new CachedRowSet();
    crsC.setCommand(SQLc.toString());
    crsC.execute(con);
    crsC.beforeFirst();
    // CRS Producci�n Terminada al Cierre de Mes No Comercializable
    crsNC = new CachedRowSet();
    crsNC.setCommand(SQLnc.toString());
    crsNC.execute(con);
    crsNC.beforeFirst();
    i=0;
    crsNC.next();
    while (crsC.next()) {
     i++;
     //Importes de Comercializables
     impMODc  = decimales(Double.parseDouble((crsC.getString("modc")== null)? "0.00":crsC.getString("modc")));
     impMPDc  = decimales(Double.parseDouble((crsC.getString("mpdc")== null)? "0.00":crsC.getString("mpdc")));
     impMOIc  = decimales(Double.parseDouble((crsC.getString("moic")== null)? "0.00":crsC.getString("moic")));
     impMPIc  = decimales(Double.parseDouble((crsC.getString("mpic")== null)? "0.00":crsC.getString("mpic")));      
     impGIc   = decimales(Double.parseDouble((crsC.getString("gic")== null)? "0.00":crsC.getString("gic")));      
     impTEc   = decimales(Double.parseDouble((crsC.getString("tec")== null)? "0.00":crsC.getString("tec")));      
     //Importes de No Comercializables
     impMODnc = decimales(Double.parseDouble((crsNC.getString("modnc")== null)? "0.00":crsNC.getString("modnc")));
     impMPDnc = decimales(Double.parseDouble((crsNC.getString("mpdnc")== null)? "0.00":crsNC.getString("mpdnc")));
     impMOInc = decimales(Double.parseDouble((crsNC.getString("moinc")== null)? "0.00":crsNC.getString("moinc")));
     impMPInc = decimales(Double.parseDouble((crsNC.getString("mpinc")== null)? "0.00":crsNC.getString("mpinc")));
     impGInc  = decimales(Double.parseDouble((crsNC.getString("ginc") == null)? "0.00":crsNC.getString("ginc")));      
     impTEnc  = decimales(Double.parseDouble((crsNC.getString("tenc") == null)? "0.00":crsNC.getString("tenc")));
     //Carga importes a variables
     impMP = decimales(impMP)+decimales(impMPDc)+decimales(impTEc)+decimales(impMPIc)+decimales(impMPDnc)+decimales(impTEnc)+decimales(impMPInc);
     impMO = decimales(impMO)+decimales(impMODc)+decimales(impMOIc)+decimales(impMODnc)+decimales(impMOInc);
     impGI = decimales(impGI)+decimales(impGIc)+decimales(impGInc);
     // Carga variables e Importes
     hm.put("IMPORTE_1_"+i+"_1_1",decimales(impMPDc)); hm.put("IMPORTE_1_"+i+"_2_1",decimales(impMPDnc));
     hm.put("IMPORTE_2_"+i+"_1_1",decimales(impTEc));  hm.put("IMPORTE_2_"+i+"_2_1",decimales(impTEnc));
     hm.put("IMPORTE_3_"+i+"_1_1",decimales(impMODc)); hm.put("IMPORTE_3_"+i+"_2_1",decimales(impMODnc));
     hm.put("IMPORTE_4_"+i+"_1_1",decimales(impMPIc)); hm.put("IMPORTE_4_"+i+"_2_1",decimales(impMPInc));      
     hm.put("IMPORTE_5_"+i+"_1_1",decimales(impMOIc)); hm.put("IMPORTE_5_"+i+"_2_1",decimales(impMOInc));
     hm.put("IMPORTE_6_"+i+"_1_1",decimales(impGIc));  hm.put("IMPORTE_6_"+i+"_2_1",decimales(impGInc));
     crsNC.next();
   }
   
   if ((impMP>0.00)||(impMO>0.00)||(impGI>0.00)) {
     hm.put("IMPORTE_0005",decimales(impMP));
     hm.put("IMPORTE_0001",decimales(impMO));
     hm.put("IMPORTE_0002",decimales(impGI)); }
   else hm.clear();
  }    
  catch (Exception e){
    System.out.println("Ha ocurrido un error en el metodo SiafmCostosProduccion.ciProduccionCanceladaCM");
    System.out.println("SiafmCostosProduccion.ciProduccionCanceladaCM : " + SQLc.toString());
    crsC.close();
    crsC = null;
    crsNC.close();
    crsNC = null;
    throw e;
  }
  finally {
    SQLc.setLength(0);
    SQLc = null;
    crsC= null;
    SQLnc.setLength(0);
    SQLnc = null;
    crsNC= null;
  } 
  return hm; 
}

/* --------------------------------------------------------------------------------------------------
 * Formas:            CN
 * Descripci�n:       Poliza de Cancelaci�n y/o Salida de TMenores y TInternet de Elaboraciones
 * Fecha de Creaci�n: 1 de Septiembre del 2009
 * Author:            JAUV
 * Parametros:        mes, a�o
 * Observaciones:     Metodo que genera un crs de la consulta de los importes de la produccion cancelada
 *                    al cierre de mes, consultando por mes y a�o
 *                    Regresa un HashMap con variables de forma e importes 
 * -------------------------------------------------------------------------------------------------- */
 public HashMap cnProduccionTerminadaTMyTICM(Connection con, String mes, String anio) throws SQLException, Exception {
                    
  HashMap hm = new HashMap();
  CachedRowSet crs = null;
  StringBuffer SQL = new StringBuffer(" ");

  Double impMO=0.00, impMP=0.00, impGI=0.00;
  Double impMOD=0.00,impMPD=0.00,impMOI=0.00,impMPI=0.00,impGII=0.00;
  Double importeA = 0.00, importeH = 0.00, importeD = 0.00;
  int i=0;
  
  SQL.append(" select h.idproceso, h.descripcion, trunc(sum(mod.importe),8) mod, ");
  SQL.append(" sum(trunc(mpd.importe,8)) mpd,sum(trunc(gi.importemoi,8)) moi, ");
  SQL.append(" sum(trunc(gi.importempi,8)) mpi, sum(trunc(gi.importegi,8)) gi, "); 
  SQL.append(" sum(trunc(aj.importemod,8)) amod,sum(trunc(aj.importemoi,8)) amoi, "); 
  SQL.append(" sum(trunc(aj.importempi,8)) ampi, sum(trunc(aj.importegi,8)) agi ");
  SQL.append(" from stiprocesoh h, ");
  SQL.append(" (select mood.clave, sum(mood.importe) importe from ");
  SQL.append(" (select substr(c.cvemanobrc,1,4) clave, sum((c.tiempo/60)*f.fmes) importe ");
  SQL.append(" from ctiregtiempos c, stiplantpers p, stifactormod f, ");
  SQL.append(" (select sti.cvetrameno, sti.mes, sti.anio from sticalculos sti, ");
  SQL.append(" (select s.cvetrameno from sticalculos s where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo = '1106') tm ");
  SQL.append(" where tm.cvetrameno = sti.cvetrameno) tm ");
  SQL.append(" where tm.cvetrameno = c.cvetrameno and to_char(c.fechacaptu,'mm') = tm.mes and to_char(c.fechacaptu,'yyyy') = tm.anio ");
  SQL.append(" and c.curp = p.curp and p.noempleado = f.noempleado and f.mes=tm.mes and f.anio=tm.anio ");
  SQL.append(" group by substr(c.cvemanobrc,1,4)  ");
  SQL.append(" union all ");
  SQL.append(" select substr(c.cvemanobrc,1,4) clave, sum((c.tiempo/60)*f.fmes) importe ");
  SQL.append(" from ctiregtiempos c, stiplantpers p, stifactormod f, ");
  SQL.append(" (select sti.cvetrainte, sti.mes, sti.anio from sticalculos sti, ");
  SQL.append(" (select s.cvetrainte from sticalculos s where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo = '1108') tm ");
  SQL.append(" where tm.cvetrainte = sti.cvetrainte) ti ");
  SQL.append(" where ti.cvetrainte = c.cvetrainte and to_char(c.fechacaptu,'mm') = ti.mes and to_char(c.fechacaptu,'yyyy') = ti.anio ");
  SQL.append(" and c.curp = p.curp and p.noempleado = f.noempleado and f.mes=ti.mes and f.anio=ti.anio ");
  SQL.append(" group by substr(c.cvemanobrc,1,4)) mood ");
  SQL.append(" group by mood.clave) mod, ");
  SQL.append(" (select substr(c.cvemanobrc,1,4) clave, sum(t.haber) importe ");
  SQL.append(" from ctireginsumos c, stitarjetamat t, ");
  SQL.append(" (select sti.cvetrameno, sti.mes, sti.anio from sticalculos sti, ");
  SQL.append(" (select s.cvetrameno from sticalculos s where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo = '1106') tm ");
  SQL.append(" where tm.cvetrameno = sti.cvetrameno) tm ");
  SQL.append(" where tm.cvetrameno = c.cvetrameno and to_char(c.fechacaptu,'mm') = tm.mes and to_char(c.fechacaptu,'yyyy') = tm.anio ");
  SQL.append(" and c.conseinsum = t.referencia and t.mes=tm.mes and t.anio=tm.anio ");
  SQL.append(" group by substr(c.cvemanobrc,1,4)) mpd, ");
  SQL.append(" (select gii.clave, sum(gii.importeMOI) importeMOI, sum(gii.importeMPI) importeMPI, ");
  SQL.append(" sum(gii.importeGI) importeGI from ");
  SQL.append(" (select substr(c.cvemanobrc,1,4) clave, sum((c.tiempo/60)*fmo.freal) importeMOI,  ");
  SQL.append(" sum((c.tiempo/60)*fmp.freal) importeMPI, sum((c.tiempo/60)*fgi.freal) importeGI ");
  SQL.append(" from ctiregtiempos c, stifactormoi fmo, stifactormpi fmp, stifactorgi fgi, ");
  SQL.append(" (select sti.cvetrameno, sti.mes, sti.anio from sticalculos sti, ");
  SQL.append(" (select s.cvetrameno from sticalculos s where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo = '1106') tm ");
  SQL.append(" where tm.cvetrameno = sti.cvetrameno) tm ");
  SQL.append(" where tm.cvetrameno = c.cvetrameno and to_char(c.fechacaptu,'mm') = tm.mes and to_char(c.fechacaptu,'yyyy') = tm.anio ");
  SQL.append(" and fmo.mes=tm.mes and fmo.anio=tm.anio and fmp.mes=tm.mes and fmp.anio=tm.anio ");
  SQL.append(" and fgi.mes=tm.mes and fgi.anio=tm.anio ");
  SQL.append(" group by substr(c.cvemanobrc,1,4)  ");
  SQL.append(" union all  ");
  SQL.append(" select substr(c.cvemanobrc,1,4) clave, sum((c.tiempo/60)*fmo.freal) importeMOI,  ");
  SQL.append(" sum((c.tiempo/60)*fmp.freal) importeMPI, sum((c.tiempo/60)*fgi.freal) importeGI ");
  SQL.append(" from ctiregtiempos c, stifactormoi fmo, stifactormpi fmp, stifactorgi fgi, ");
  SQL.append(" (select sti.cvetrainte, sti.mes, sti.anio from sticalculos sti, ");
  SQL.append(" (select s.cvetrainte from sticalculos s where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo = '1108') tm ");
  SQL.append(" where tm.cvetrainte = sti.cvetrainte) ti ");
  SQL.append(" where ti.cvetrainte = c.cvetrainte and to_char(c.fechacaptu,'mm') = ti.mes and to_char(c.fechacaptu,'yyyy') = ti.anio ");
  SQL.append(" and fmo.mes=ti.mes and fmo.anio=ti.anio and fmp.mes=ti.mes and fmp.anio=ti.anio ");
  SQL.append(" and fgi.mes=ti.mes and fgi.anio=ti.anio ");
  SQL.append(" group by substr(c.cvemanobrc,1,4)) gii ");
  SQL.append(" group by gii.clave) gi, ");
  SQL.append(" (select ajj.clave, sum(ajj.importeMOD) importeMOD, sum(ajj.importeMOI) importeMOI, ");
  SQL.append(" sum(ajj.importeMPI) importeMPI, sum(ajj.importeGI) importeGI from ");
  SQL.append(" (select substr(c.cvemanobrc,1,4) clave,  ");
  SQL.append(" sum((c.tiempo/60)*fa.fmod) importeMOD, sum((c.tiempo/60)*fa.fmoi) importeMOI,  ");
  SQL.append(" sum((c.tiempo/60)*fa.fmpi) importeMPI, sum((c.tiempo/60)*fa.fgi) importeGI ");
  SQL.append(" from ctiregtiempos c, stiajuste fa, ");
  SQL.append(" (select sti.cvetrameno, sti.mes, sti.anio from sticalculos sti, ");
  SQL.append(" (select s.cvetrameno from sticalculos s where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo = '1106') tm ");
  SQL.append(" where tm.cvetrameno = sti.cvetrameno) tm ");
  SQL.append(" where tm.cvetrameno = c.cvetrameno and to_char(c.fechacaptu,'mm') = tm.mes and to_char(c.fechacaptu,'yyyy') = tm.anio ");
  SQL.append(" and fa.mes=tm.mes and fa.anio=tm.anio  ");
  SQL.append(" group by substr(c.cvemanobrc,1,4)  ");
  SQL.append(" union all");
  SQL.append(" select substr(c.cvemanobrc,1,4) clave,  ");
  SQL.append(" sum((c.tiempo/60)*fa.fmod) importeMOD, sum((c.tiempo/60)*fa.fmoi) importeMOI,  ");
  SQL.append(" sum((c.tiempo/60)*fa.fmpi) importeMPI, sum((c.tiempo/60)*fa.fgi) importeGI ");
  SQL.append(" from ctiregtiempos c, stiajuste fa, ");
  SQL.append(" (select sti.cvetrainte, sti.mes, sti.anio from sticalculos sti, ");
  SQL.append(" (select s.cvetrainte from sticalculos s where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo = '1108') tm ");
  SQL.append(" where tm.cvetrainte = sti.cvetrainte) ti ");
  SQL.append(" where ti.cvetrainte = c.cvetrainte and to_char(c.fechacaptu,'mm') = ti.mes and to_char(c.fechacaptu,'yyyy') = ti.anio ");
  SQL.append(" and fa.mes=ti.mes and fa.anio=ti.anio  ");
  SQL.append(" group by substr(c.cvemanobrc,1,4)) ajj ");
  SQL.append(" group by ajj.clave) aj ");
  SQL.append(" where substr(h.cvemanobrc,1,4) = mod.clave(+) and substr(h.cvemanobrc,1,4) = mpd.clave(+) ");
  SQL.append("and substr(h.cvemanobrc,1,4) = gi.clave(+)  and substr(h.cvemanobrc,1,4) = aj.clave(+) ");
  SQL.append("and h.mes="+mes+" and h.anio="+anio);
  SQL.append(" group by h.idproceso, h.descripcion ");
  SQL.append(" order by h.idproceso ");

  try{
     crs = new CachedRowSet();
     crs.setCommand(SQL.toString());
     crs.execute(con);
     crs.beforeFirst();
     i=0;
     while (crs.next()) {
        i++;
        //Importes por Elemento del Costo
        impMOD  = Double.parseDouble((crs.getString("mod")== null)? "0.00":crs.getString("mod"));
        impMPD  = Double.parseDouble((crs.getString("mpd")== null)? "0.00":crs.getString("mpd"));
        impMOI  = Double.parseDouble((crs.getString("moi")== null)? "0.00":crs.getString("moi"));
        impMPI  = Double.parseDouble((crs.getString("mpi")== null)? "0.00":crs.getString("mpi"));
        impGII  = Double.parseDouble((crs.getString("gi") == null)? "0.00":crs.getString("gi"));
        //Carga importes a variables de acumulado
        importeH = importeH+decimales(impMPD)+decimales(impMPI)+decimales(impMOD)+decimales(impMOI)+decimales(impGII);
        impMP = decimales(impMP)+decimales(impMPD)+decimales(impMPI);
        impMO = decimales(impMO)+decimales(impMOD)+decimales(impMOI);
        impGI = decimales(impGI)+decimales(impGII);
        importeD = decimales(impMP)+decimales(impMO)+decimales(impGI);
        // Carga variables e Importes
        hm.put("IMPORTE_1_"+i+"_2_1",decimales(impMPD)); hm.put("IMPORTE_1_"+i+"_2_1",decimales(impMPD));
        hm.put("IMPORTE_2_"+i+"_2_1",decimales(impMOD)); hm.put("IMPORTE_3_"+i+"_2_1",decimales(impMOD));
        hm.put("IMPORTE_3_"+i+"_2_1",decimales(impMPI)); hm.put("IMPORTE_4_"+i+"_2_1",decimales(impMPI));
        hm.put("IMPORTE_4_"+i+"_2_1",decimales(impMOI)); hm.put("IMPORTE_5_"+i+"_2_1",decimales(impMOI));
        hm.put("IMPORTE_5_"+i+"_2_1",decimales(impGII));
      }
      if ((impMP>0.00)||(impMO>0.00)||(impGI>0.00)) {
        importeA = (decimales(importeH)-decimales(importeD));
        hm.put("IMPORTE_0005",decimales(impMP));
        hm.put("IMPORTE_0001",decimales(impMO));
        hm.put("IMPORTE_0002",decimales(impGI)-decimales(importeA));  }
      else hm.clear();     
     }    
     catch (Exception e){
       System.out.println("Ha ocurrido un error en el metodo SiafmCostosProduccion.cnProduccionTerminadaTMyTICM");
       System.out.println("SiafmCostosProduccion.cnProduccionTerminadaTMyTICM : " + SQL.toString());
       crs.close();
       crs = null;
       throw e;
     }
     finally {
       SQL.setLength(0);
       SQL = null;
       crs= null;
     } 
     return hm;
 }
 
/* --------------------------------------------------------------------------------------------------
 * Formas:            CJ
 * Descripci�n:       Poliza de Aplicacion de Ajuste positivo de los Gastos Indirectos
 * Fecha de Creaci�n: 2 de Septiembre del 2009
 * Author:            JAUV
 * Parametros:        mes, a�o
 * Observaciones:     Metodo que genera un crs de la consulta de los importes de ajuste de la produccion 
 *                    en proceso al cierre de mes, consultando por mes y a�o
 *                    Regresa un HashMap con variables de forma e importes 
 * -------------------------------------------------------------------------------------------------- */
public HashMap cjAjustePositivoCM(Connection con, String mes, String anio) throws SQLException, Exception {
                           
  HashMap hm = new HashMap();
  CachedRowSet crs = null;
  StringBuffer SQL = new StringBuffer(" ");

  Double impMO=0.00, impMP=0.00, impGI=0.00;
  Double impMOIc=0.00, impMPIc=0.00, impGIIc=0.00;
  Double impMOInc=0.00,impMPInc=0.00,impGIInc=0.00;
  int i=0;
     
  SQL.append(" select h.idproceso, sum(aj.importeMOD) modc, sum(aj.importeMOI) moic, sum(aj.importeMPI) mpic, sum(aj.importeGI) gic, ");
  SQL.append(" sum(ajnc.importeMOD) modnc, sum(ajnc.importeMOI) moinc, sum(ajnc.importeMPI) mpinc, sum(ajnc.importeGI) ginc from stiprocesoh h, ");
  SQL.append(" (select substr(c.cvemanobrc,1,4) clave, sum((c.tiempo/60)*fa.fmod) importeMOD, ");
  SQL.append(" sum((c.tiempo/60)*fa.fmoi) importeMOI, sum((c.tiempo/60)*fa.fmpi) importeMPI, ");
  SQL.append(" sum((c.tiempo/60)*fa.fgi) importeGI from ");
  SQL.append(" (select s.numconrequ, s.mes, s.anio from sticalculos s, etidescrmater e ");
  SQL.append(" where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo in ('1109','1110') ");
  SQL.append(" and s.numconrequ = e.numconrequ and e.procomerci_ant=1 and s.stregistro is null) req, ctiregtiempos c, stiajuste fa ");
  SQL.append(" where req.numconrequ = c.numconrequ and to_char(c.fechacaptu,'mm')="+mes+" and to_char(c.fechacaptu,'yyyy')="+anio);
  SQL.append(" and fa.mes="+mes+" and fa.anio="+anio);
  SQL.append(" group by substr(c.cvemanobrc,1,4) "); 
  SQL.append(" union all ");
  SQL.append(" select substr(c.cvemanobrc,1,4) clave, sum((c.tiempo/60)*fa.fmod) importeMOD, ");
  SQL.append(" sum((c.tiempo/60)*fa.fmoi) importeMOI, sum((c.tiempo/60)*fa.fmpi) importeMPI,  ");
  SQL.append(" sum((c.tiempo/60)*fa.fgi) importeGI from  ");
  SQL.append(" (select s.numorden, s.mes, s.anio from sticalculos s, etidescrmater e, etiordenprodu o ");
  SQL.append(" where s.mes="+mes+" and s.anio= "+anio+" and s.stcalculo in ('1101','1103','1104') "); 
  SQL.append(" and s.numorden = o.numorden and o.numconrequ = e.numconrequ and e.procomerci_ant=1 and s.stregistro is null) op, ");
  SQL.append(" ctiregtiempos c, stiajuste fa ");
  SQL.append(" where op.numorden = c.numorden and to_char(c.fechacaptu,'mm')="+mes+" and to_char(c.fechacaptu,'yyyy')="+anio);
  SQL.append(" and fa.mes="+mes+" and fa.anio="+anio);
  SQL.append(" group by substr(c.cvemanobrc,1,4)) aj, ");
  SQL.append(" (select faj.clave, sum(faj.importemod) importemod, sum(faj.importeMOI) importemoi, sum(faj.importeMPI) importempi, ");
  SQL.append(" sum(faj.importeGI) importegi from ");
  SQL.append(" (select substr(c.cvemanobrc,1,4) clave, sum((c.tiempo/60)*fa.fmoi) importeMOI, sum((c.tiempo/60)*fa.fmpi) importeMPI,  ");
  SQL.append(" sum((c.tiempo/60)*fa.fmod) importeMOD, sum((c.tiempo/60)*fa.fgi) importeGI from "); 
  SQL.append(" (select s.numconrequ, s.mes, s.anio from sticalculos s, etidescrmater e ");
  SQL.append(" where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo in ('1109','1110')  ");
  SQL.append(" and s.numconrequ = e.numconrequ and e.procomerci_ant=0 and s.stregistro is null) req, ctiregtiempos c, stiajuste fa ");
  SQL.append(" where req.numconrequ = c.numconrequ and to_char(c.fechacaptu,'mm')="+mes+" and to_char(c.fechacaptu,'yyyy')="+anio);
  SQL.append(" and fa.mes="+mes+" and fa.anio="+anio+" group by substr(c.cvemanobrc,1,4)  ");
  SQL.append(" union all ");
  SQL.append(" select substr(c.cvemanobrc,1,4) clave, sum((c.tiempo/60)*fa.fmoi) importeMOI, sum((c.tiempo/60)*fa.fmpi) importeMPI,  ");
  SQL.append(" sum((c.tiempo/60)*fa.fmod) importeMOD, sum((c.tiempo/60)*fa.fgi) importeGI from  ");
  SQL.append(" (select s.numorden, s.mes, s.anio from sticalculos s, etidescrmater e, etiordenprodu o ");
  SQL.append(" where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo in ('1101','1103','1104')  ");
  SQL.append(" and s.numorden = o.numorden and o.numconrequ = e.numconrequ and e.procomerci_ant=0 and s.stregistro is null) op, ");
  SQL.append(" ctiregtiempos c, stiajuste fa ");
  SQL.append(" where op.numorden = c.numorden and to_char(c.fechacaptu,'mm')="+mes+" and to_char(c.fechacaptu,'yyyy')="+anio);
  SQL.append(" and fa.mes="+mes+" and fa.anio="+anio+" group by substr(c.cvemanobrc,1,4) ");
  SQL.append(" union all ");
  SQL.append(" select substr(c.cvemanobrc,1,4) clave, sum((c.tiempo/60)*fa.fmoi) importeMOI, sum((c.tiempo/60)*fa.fmpi) importeMPI,  ");
  SQL.append(" sum((c.tiempo/60)*fa.fmod) importeMOD, sum((c.tiempo/60)*fa.fgi) importeGI from  ");
  SQL.append(" (select s.cvetrameno from sticalculos s where s.stcalculo in ('1105','1106') and s.mes="+mes+" and s.anio="+anio+") tm, ");
  SQL.append(" ctiregtiempos c, stiajuste fa ");
  SQL.append(" where tm.cvetrameno = c.cvetrameno and to_char(c.fechacaptu,'mm')="+mes+" and to_char(c.fechacaptu,'yyyy')="+anio);
  SQL.append(" and fa.mes="+mes+" and fa.anio = "+anio+" group by substr(c.cvemanobrc,1,4) ");
  SQL.append(" union all ");
  SQL.append(" select substr(c.cvemanobrc,1,4) clave, sum((c.tiempo/60)*fa.fmoi) importeMOI, sum((c.tiempo/60)*fa.fmpi) importeMPI,  ");
  SQL.append(" sum((c.tiempo/60)*fa.fmod) importeMOD, sum((c.tiempo/60)*fa.fgi) importeGI from  ");
  SQL.append(" (select s.cvetrainte from sticalculos s where s.stcalculo in ('1107','1108') and s.mes="+mes+" and s.anio="+anio+") ti, ");
  SQL.append(" ctiregtiempos c, stiajuste fa ");
  SQL.append(" where ti.cvetrainte = c.cvetrainte and to_char(c.fechacaptu,'mm')="+mes+" and to_char(c.fechacaptu,'yyyy')="+anio);
  SQL.append(" and fa.mes="+mes+" and fa.anio = "+anio+" group by substr(c.cvemanobrc,1,4)) faj ");
  SQL.append(" group by faj.clave) ajnc ");
  SQL.append(" where aj.clave(+) = substr(h.cvemanobrc,1,4) and ajnc.clave = substr(h.cvemanobrc,1,4) ");
  SQL.append(" and h.mes="+mes+" and h.anio="+anio+" ");
  SQL.append(" group by h.idproceso order by  h.idproceso");

  try{
    crs = new CachedRowSet();
    crs.setCommand(SQL.toString());
    crs.execute(con);
    crs.beforeFirst();
    i=0;
    while (crs.next()) {
      i++;
     //Importes por Elemento del Costo
     impMOIc  = Double.parseDouble((crs.getString("moic")== null)? "0.00":crs.getString("moic"));
     impMPIc  = Double.parseDouble((crs.getString("mpic")== null)? "0.00":crs.getString("mpic"));
     impGIIc  = Double.parseDouble((crs.getString("gic")== null)? "0.00":crs.getString("gic"));
     impMOInc = Double.parseDouble((crs.getString("moinc")== null)? "0.00":crs.getString("moinc"));
     impMPInc = Double.parseDouble((crs.getString("mpinc")== null)? "0.00":crs.getString("mpinc"));
     impGIInc = Double.parseDouble((crs.getString("ginc")== null)? "0.00":crs.getString("ginc"));
     //Carga importes a variables de acumulado
     if (impMOIc  > 0.00) { impMO = decimales(impMO)+decimales(impMOIc);  hm.put("IMPORTE_"+i+"_1_2",decimales(impMOIc));  }
     if (impMOInc > 0.00) { impMO = decimales(impMO)+decimales(impMOInc); hm.put("IMPORTE_"+i+"_2_2",decimales(impMOInc)); }
     if (impMPIc  > 0.00) { impMP = decimales(impMP)+decimales(impMPIc);  hm.put("IMPORTE_"+i+"_1_1",decimales(impMPIc));  }
     if (impMPInc > 0.00) { impMP = decimales(impMP)+decimales(impMPInc); hm.put("IMPORTE_"+i+"_2_1",decimales(impMPInc)); }
     if (impGIIc  > 0.00) { impGI = decimales(impGI)+decimales(impGIIc);  hm.put("IMPORTE_"+i+"_1_3",decimales(impGIIc));  }
     if (impGIInc > 0.00) { impGI = decimales(impGI)+decimales(impGIInc); hm.put("IMPORTE_"+i+"_2_3",decimales(impGIInc)); }
   }
   if ((impMP>0.00)||(impMO>0.00)||(impGI>0.00)) {
     hm.put("IMPORTE_0005",decimales(impMP));
     hm.put("IMPORTE_0001",decimales(impMO));
     hm.put("IMPORTE_0002",decimales(impGI));  }
   else hm.clear();     
  }    
  catch (Exception e){
    System.out.println("Ha ocurrido un error en el metodo SiafmCostosProduccion.cjAjustePositivoCM");
    System.out.println("SiafmCostosProduccion.cjAjustePositivoCM : " + SQL.toString());
    crs.close();
    crs = null;
    throw e;
  }
  finally {
    SQL.setLength(0);
    SQL = null;
    crs= null;
  } 
  return hm; 
}

/* --------------------------------------------------------------------------------------------------
 * Formas:            CK
 * Descripci�n:       Poliza de Aplicacion de Ajuste negativo de los Gastos Indirectos
 * Fecha de Creaci�n: 2 de Septiembre del 2009
 * Author:            JAUV
 * Parametros:        mes, a�o
 * Observaciones:     Metodo que genera un crs de la consulta de los importes de ajuste de la produccion 
 *                    en proceso al cierre de mes, consultando por mes y a�o
 *                    Regresa un HashMap con variables de forma e importes 
 * -------------------------------------------------------------------------------------------------- */
public HashMap ckAjusteNegativoCM(Connection con, String mes, String anio) throws SQLException, Exception {
                               
  HashMap hm = new HashMap();
  CachedRowSet crs = null;
  StringBuffer SQL = new StringBuffer(" ");

  Double impMO=0.00, impMP=0.00, impGI=0.00;
  Double impMODc=0.00,impMPDc=0.00,impMOIc=0.00,impMPIc=0.00,impGIIc=0.00;
  Double impMODnc=0.00,impMPDnc=0.00,impMOInc=0.00,impMPInc=0.00,impGIInc=0.00;
  int i=0;
         
  SQL.append(" select h.idproceso, sum(aj.importeMOD) modc, sum(aj.importeMOI) moic, sum(aj.importeMPI) mpic, sum(aj.importeGI) gic, ");
  SQL.append(" sum(ajnc.importeMOD) modnc, sum(ajnc.importeMOI) moinc, sum(ajnc.importeMPI) mpinc, sum(ajnc.importeGI) ginc from stiprocesoh h, ");
  SQL.append(" (select substr(c.cvemanobrc,1,4) clave, sum((c.tiempo/60)*fa.fmod) importeMOD, ");
  SQL.append(" sum((c.tiempo/60)*fa.fmoi) importeMOI, sum((c.tiempo/60)*fa.fmpi) importeMPI, ");
  SQL.append(" sum((c.tiempo/60)*fa.fgi) importeGI from ");
  SQL.append(" (select s.numconrequ, s.mes, s.anio from sticalculos s, etidescrmater e ");
  SQL.append(" where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo in ('1109','1110') ");
  SQL.append(" and s.numconrequ = e.numconrequ and e.procomerci_ant=1 and s.stregistro is null) req, ctiregtiempos c, stiajuste fa ");
  SQL.append(" where req.numconrequ = c.numconrequ and to_char(c.fechacaptu,'mm')="+mes+" and to_char(c.fechacaptu,'yyyy')="+anio);
  SQL.append(" and fa.mes="+mes+" and fa.anio="+anio);
  SQL.append(" group by substr(c.cvemanobrc,1,4) "); 
  SQL.append(" union all ");
  SQL.append(" select substr(c.cvemanobrc,1,4) clave, sum((c.tiempo/60)*fa.fmod) importeMOD, ");
  SQL.append(" sum((c.tiempo/60)*fa.fmoi) importeMOI, sum((c.tiempo/60)*fa.fmpi) importeMPI,  ");
  SQL.append(" sum((c.tiempo/60)*fa.fgi) importeGI from  ");
  SQL.append(" (select s.numorden, s.mes, s.anio from sticalculos s, etidescrmater e, etiordenprodu o ");
  SQL.append(" where s.mes="+mes+" and s.anio= "+anio+" and s.stcalculo in ('1101','1103','1104') "); 
  SQL.append(" and s.numorden = o.numorden and o.numconrequ = e.numconrequ and e.procomerci_ant=1 and s.stregistro is null) op, ");
  SQL.append(" ctiregtiempos c, stiajuste fa ");
  SQL.append(" where op.numorden = c.numorden and to_char(c.fechacaptu,'mm')="+mes+" and to_char(c.fechacaptu,'yyyy')="+anio);
  SQL.append(" and fa.mes="+mes+" and fa.anio="+anio);
  SQL.append(" group by substr(c.cvemanobrc,1,4)) aj, ");
  SQL.append(" (select faj.clave, sum(faj.importemod) importemod, sum(faj.importeMOI) importemoi, sum(faj.importeMPI) importempi, ");
  SQL.append(" sum(faj.importeGI) importegi from ");
  SQL.append(" (select substr(c.cvemanobrc,1,4) clave, sum((c.tiempo/60)*fa.fmoi) importeMOI, sum((c.tiempo/60)*fa.fmpi) importeMPI,  ");
  SQL.append(" sum((c.tiempo/60)*fa.fmod) importeMOD, sum((c.tiempo/60)*fa.fgi) importeGI from "); 
  SQL.append(" (select s.numconrequ, s.mes, s.anio from sticalculos s, etidescrmater e ");
  SQL.append(" where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo in ('1109','1110')  ");
  SQL.append(" and s.numconrequ = e.numconrequ and e.procomerci_ant=0 and s.stregistro is null) req, ctiregtiempos c, stiajuste fa ");
  SQL.append(" where req.numconrequ = c.numconrequ and to_char(c.fechacaptu,'mm')="+mes+" and to_char(c.fechacaptu,'yyyy')="+anio);
  SQL.append(" and fa.mes="+mes+" and fa.anio="+anio+" group by substr(c.cvemanobrc,1,4)  ");
  SQL.append(" union all ");
  SQL.append(" select substr(c.cvemanobrc,1,4) clave, sum((c.tiempo/60)*fa.fmoi) importeMOI, sum((c.tiempo/60)*fa.fmpi) importeMPI,  ");
  SQL.append(" sum((c.tiempo/60)*fa.fmod) importeMOD, sum((c.tiempo/60)*fa.fgi) importeGI from  ");
  SQL.append(" (select s.numorden, s.mes, s.anio from sticalculos s, etidescrmater e, etiordenprodu o ");
  SQL.append(" where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo in ('1101','1103','1104')  ");
  SQL.append(" and s.numorden = o.numorden and o.numconrequ = e.numconrequ and e.procomerci_ant=0 and s.stregistro is null) op, ");
  SQL.append(" ctiregtiempos c, stiajuste fa ");
  SQL.append(" where op.numorden = c.numorden and to_char(c.fechacaptu,'mm')="+mes+" and to_char(c.fechacaptu,'yyyy')="+anio);
  SQL.append(" and fa.mes="+mes+" and fa.anio="+anio+" group by substr(c.cvemanobrc,1,4) ");
  SQL.append(" union all ");
  SQL.append(" select substr(c.cvemanobrc,1,4) clave, sum((c.tiempo/60)*fa.fmoi) importeMOI, sum((c.tiempo/60)*fa.fmpi) importeMPI,  ");
  SQL.append(" sum((c.tiempo/60)*fa.fmod) importeMOD, sum((c.tiempo/60)*fa.fgi) importeGI from  ");
  SQL.append(" (select s.cvetrameno from sticalculos s where s.stcalculo in ('1105','1106') and s.mes="+mes+" and s.anio="+anio+") tm, ");
  SQL.append(" ctiregtiempos c, stiajuste fa ");
  SQL.append(" where tm.cvetrameno = c.cvetrameno and to_char(c.fechacaptu,'mm')="+mes+" and to_char(c.fechacaptu,'yyyy')="+anio);
  SQL.append(" and fa.mes="+mes+" and fa.anio = "+anio+" group by substr(c.cvemanobrc,1,4) ");
  SQL.append(" union all ");
  SQL.append(" select substr(c.cvemanobrc,1,4) clave, sum((c.tiempo/60)*fa.fmoi) importeMOI, sum((c.tiempo/60)*fa.fmpi) importeMPI,  ");
  SQL.append(" sum((c.tiempo/60)*fa.fmod) importeMOD, sum((c.tiempo/60)*fa.fgi) importeGI from  ");
  SQL.append(" (select s.cvetrainte from sticalculos s where s.stcalculo in ('1107','1108') and s.mes="+mes+" and s.anio="+anio+") ti, ");
  SQL.append(" ctiregtiempos c, stiajuste fa ");
  SQL.append(" where ti.cvetrainte = c.cvetrainte and to_char(c.fechacaptu,'mm')="+mes+" and to_char(c.fechacaptu,'yyyy')="+anio);
  SQL.append(" and fa.mes="+mes+" and fa.anio = "+anio+" group by substr(c.cvemanobrc,1,4)) faj ");
  SQL.append(" group by faj.clave) ajnc ");
  SQL.append(" where aj.clave(+) = substr(h.cvemanobrc,1,4) and ajnc.clave = substr(h.cvemanobrc,1,4) ");
  SQL.append(" and h.mes="+mes+" and h.anio="+anio+" ");
  SQL.append(" group by h.idproceso order by  h.idproceso ");

  try{
    crs = new CachedRowSet();
    crs.setCommand(SQL.toString());
    crs.execute(con);
    crs.beforeFirst();
    i=0;
    while (crs.next()) {
      i++;
     //Importes por Elemento del Costo
     impMOIc  = Double.parseDouble((crs.getString("moic")== null)? "0.00":crs.getString("moic"));
     impMPIc  = Double.parseDouble((crs.getString("mpic")== null)? "0.00":crs.getString("mpic"));
     impGIIc  = Double.parseDouble((crs.getString("gic")== null)? "0.00":crs.getString("gic"));
     impMOInc = Double.parseDouble((crs.getString("moinc")== null)? "0.00":crs.getString("moinc"));
     impMPInc = Double.parseDouble((crs.getString("mpinc")== null)? "0.00":crs.getString("mpinc"));
     impGIInc = Double.parseDouble((crs.getString("ginc")== null)? "0.00":crs.getString("ginc"));
     //Carga importes a variables de acumulado
     if (impMOIc  < 0.00) { impMO = decimales(impMO)+decimales(impMOIc);  hm.put("IMPORTE_"+i+"_1_2",decimales(impMOIc));  }
     if (impMOInc < 0.00) { impMO = decimales(impMO)+decimales(impMOInc); hm.put("IMPORTE_"+i+"_2_2",decimales(impMOInc)); }
     if (impMPIc  < 0.00) { impMP = decimales(impMP)+decimales(impMPIc);  hm.put("IMPORTE_"+i+"_1_1",decimales(impMPIc));  }
     if (impMPInc < 0.00) { impMP = decimales(impMP)+decimales(impMPInc); hm.put("IMPORTE_"+i+"_2_1",decimales(impMPInc)); }
     if (impGIIc  < 0.00) { impGI = decimales(impGI)+decimales(impGIIc);  hm.put("IMPORTE_"+i+"_1_3",decimales(impGIIc));  }
     if (impGIInc < 0.00) { impGI = decimales(impGI)+decimales(impGIInc); hm.put("IMPORTE_"+i+"_2_3",decimales(impGIInc)); }
   }
   if ((impMP>0.00)||(impMO>0.00)||(impGI>0.00)) {   
     hm.put("IMPORTE_0005",decimales(impMP));
     hm.put("IMPORTE_0001",decimales(impMO));
     hm.put("IMPORTE_0002",decimales(impGI));  }
   else hm.clear();
  }    
  catch (Exception e){
    System.out.println("Ha ocurrido un error en el metodo SiafmCostosProduccion.ckAjusteNegativoCM");
    System.out.println("SiafmCostosProduccion.ckAjusteNegativoCM : " + SQL.toString());
    crs.close();
    crs = null;
    throw e;
  }
  finally {
    SQL.setLength(0);
    SQL = null;
    crs= null;
  } 
  return hm; 
}

/* --------------------------------------------------------------------------------------------------
 * Formas:            CL
 * Descripci�n:       Poliza de Aplicacion de Ajuste Positivo de la Mano de Obra Directa
 * Fecha de Creaci�n: 2 de Septiembre del 2009
 * Author:            JAUV
 * Parametros:        mes, a�o, numero de orden de produccion, comercializable (0=no y 1=si)
 * Observaciones:     Metodo que genera un crs de la consulta de consumo de mano de obra por mes,a�o y
 *                    numero de orden de producci�n.
 *                    Regresa un HashMap con variables de forma e importes 
 * -------------------------------------------------------------------------------------------------- */
public HashMap clAjustePositivoMOCM(Connection con, String mes, String anio) throws SQLException, Exception {
                  
  HashMap hm = new HashMap();
  CachedRowSet crs = null;
  StringBuffer SQL = new StringBuffer(" ");

  Double impMO = 0.00;
  Double impMODc =0.00, impMODnc = 0.00;
  int i=0;

  SQL.append(" select h.idproceso, sum(aj.importeMOD) modc, sum(ajnc.importeMOD) modnc from stiprocesoh h, ");
  SQL.append(" (select substr(c.cvemanobrc,1,4) clave, sum((c.tiempo/60)*fa.fmod) importeMOD from ");
  SQL.append(" (select s.numconrequ, s.mes, s.anio from sticalculos s, etidescrmater e ");
  SQL.append(" where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo in ('1109','1110') "); 
  SQL.append(" and s.numconrequ = e.numconrequ and e.procomerci_ant=1 and s.stregistro is null) req, ctiregtiempos c, stiajuste fa ");
  SQL.append(" where req.numconrequ = c.numconrequ and to_char(c.fechacaptu,'mm')="+mes+" and to_char(c.fechacaptu,'yyyy')="+anio);
  SQL.append(" and fa.mes="+mes+" and fa.anio="+anio);
  SQL.append(" group by substr(c.cvemanobrc,1,4)  ");
  SQL.append(" union all ");
  SQL.append(" select substr(c.cvemanobrc,1,4) clave, sum((c.tiempo/60)*fa.fmod) importeMOD from  ");
  SQL.append(" (select s.numorden, s.mes, s.anio from sticalculos s, etidescrmater e, etiordenprodu o ");
  SQL.append(" where s.mes="+mes+" and s.anio= "+anio+" and s.stcalculo in ('1101','1103','1104')  ");
  SQL.append(" and s.numorden = o.numorden and o.numconrequ = e.numconrequ and e.procomerci_ant=1 and s.stregistro is null) op, ");
  SQL.append(" ctiregtiempos c, stiajuste fa ");
  SQL.append(" where op.numorden = c.numorden and to_char(c.fechacaptu,'mm')="+mes+" and to_char(c.fechacaptu,'yyyy')="+anio);
  SQL.append(" and fa.mes="+mes+" and fa.anio="+anio);
  SQL.append(" group by substr(c.cvemanobrc,1,4)) aj, ");
  SQL.append(" (select faj.clave, sum(faj.importemod) importemod from ");
  SQL.append(" (select substr(c.cvemanobrc,1,4) clave, sum((c.tiempo/60)*fa.fmod) importeMOD from  ");
  SQL.append(" (select s.numconrequ, s.mes, s.anio from sticalculos s, etidescrmater e ");
  SQL.append(" where s.mes="+mes+" and s.anio= "+anio+" and s.stcalculo in ('1109','1110')  ");
  SQL.append(" and s.numconrequ = e.numconrequ and e.procomerci_ant=0 and s.stregistro is null) req, ctiregtiempos c, stiajuste fa ");
  SQL.append(" where req.numconrequ = c.numconrequ and to_char(c.fechacaptu,'mm')="+mes+" and to_char(c.fechacaptu,'yyyy')="+anio);
  SQL.append(" and fa.mes="+mes+" and fa.anio = "+anio+" group by substr(c.cvemanobrc,1,4)  ");
  SQL.append(" union all ");
  SQL.append(" select substr(c.cvemanobrc,1,4) clave, sum((c.tiempo/60)*fa.fmod) importeMOD from  ");
  SQL.append(" (select s.numorden, s.mes, s.anio from sticalculos s, etidescrmater e, etiordenprodu o ");
  SQL.append(" where s.mes="+mes+" and s.anio= "+anio+" and s.stcalculo in ('1101','1103','1104')  ");
  SQL.append(" and s.numorden = o.numorden and o.numconrequ = e.numconrequ and e.procomerci_ant=0 and s.stregistro is null) op, ");
  SQL.append(" ctiregtiempos c, stiajuste fa ");
  SQL.append(" where op.numorden = c.numorden and to_char(c.fechacaptu,'mm')="+mes+" and to_char(c.fechacaptu,'yyyy')="+anio);
  SQL.append(" and fa.mes="+mes+" and fa.anio = "+anio+" group by substr(c.cvemanobrc,1,4) ");
  SQL.append(" union all ");
  SQL.append(" select substr(c.cvemanobrc,1,4) clave, sum((c.tiempo/60)*fa.fmod) importeMOD from  ");
  SQL.append(" (select s.cvetrameno from sticalculos s where s.stcalculo in ('1105','1106') and s.mes="+mes+" and s.anio="+anio+") tm, ");
  SQL.append(" ctiregtiempos c, stiajuste fa ");
  SQL.append(" where tm.cvetrameno = c.cvetrameno and to_char(c.fechacaptu,'mm')="+mes+" and to_char(c.fechacaptu,'yyyy')="+anio);
  SQL.append(" and fa.mes="+mes+" and fa.anio = "+anio+" group by substr(c.cvemanobrc,1,4) ");
  SQL.append(" union all ");
  SQL.append(" select substr(c.cvemanobrc,1,4) clave, sum((c.tiempo/60)*fa.fmod) importeMOD from  ");
  SQL.append(" (select s.cvetrainte from sticalculos s where s.stcalculo in ('1107','1108') and s.mes="+mes+" and s.anio="+anio+") ti, ");
  SQL.append(" ctiregtiempos c, stiajuste fa ");
  SQL.append(" where ti.cvetrainte = c.cvetrainte and to_char(c.fechacaptu,'mm')="+mes+" and to_char(c.fechacaptu,'yyyy')="+anio);
  SQL.append(" and fa.mes="+mes+" and fa.anio = "+anio+" group by substr(c.cvemanobrc,1,4)) faj ");
  SQL.append(" group by faj.clave) ajnc ");
  SQL.append(" where aj.clave(+) = substr(h.cvemanobrc,1,4) and ajnc.clave = substr(h.cvemanobrc,1,4) ");
  SQL.append(" and h.mes="+mes+" and h.anio="+anio);
  SQL.append(" group by h.idproceso order by  h.idproceso ");

  try{
    crs = new CachedRowSet();
    crs.setCommand(SQL.toString());
    crs.execute(con);
    crs.beforeFirst();
    i=0;
    while (crs.next()) {
      i++;
      impMODc  = Double.parseDouble((crs.getString("modc")== null)? "0.00":crs.getString("modc"));
      if (impMODc  > 0.00) { impMO = decimales(impMO) + decimales(impMODc);   hm.put("IMPORTE_"+i+"_1_1",decimales(impMODc));  }
      impMODnc  = Double.parseDouble((crs.getString("modnc")== null)? "0.00":crs.getString("modnc"));
      if (impMODnc > 0.00) { impMO = decimales(impMO) + decimales(impMODnc);  hm.put("IMPORTE_"+i+"_2_1",decimales(impMODnc));  }
    }
    if (impMO>0.00) { hm.put("IMPORTE_0001",decimales(impMO)); }
    else hm.clear();
  }
  catch (Exception e){
    System.out.println("Ha ocurrido un error en el metodo SiafmCostosProduccion.clAjustePositivoMOCM");
    System.out.println("SiafmCostosProduccion.clAjustePositivoMOCM : " + SQL.toString());
    crs.close();
    crs = null;
    throw e;
  }
  finally {
    SQL.setLength(0);
    SQL = null;
    crs= null;
  } 
  return hm; 
}

/* --------------------------------------------------------------------------------------------------
 * Formas:            CM
 * Descripci�n:       Poliza de Aplicacion de Ajuste Negativo de la Mano de Obra Directa
 * Fecha de Creaci�n: 2 de Septiembre del 2009
 * Author:            JAUV
 * Parametros:        mes, a�o, numero de orden de produccion, comercializable (0=no y 1=si)
 * Observaciones:     Metodo que genera un crs de la consulta de consumo de mano de obra por mes,a�o y
 *                    numero de orden de producci�n.
 *                    Regresa un HashMap con variables de forma e importes 
 * -------------------------------------------------------------------------------------------------- */
public HashMap cmAjusteNegativoMOCM(Connection con, String mes, String anio) throws SQLException, Exception {
                      
  HashMap hm = new HashMap();
  CachedRowSet crs = null;
  StringBuffer SQL = new StringBuffer(" ");
 
  Double impMO = 0.00;
  Double impMODc =0.00, impMODnc = 0.00;
  int i=0;

  SQL.append(" select h.idproceso, sum(aj.importeMOD) modc, sum(ajnc.importeMOD) modnc from stiprocesoh h, ");
  SQL.append(" (select substr(c.cvemanobrc,1,4) clave, sum((c.tiempo/60)*fa.fmod) importeMOD from ");
  SQL.append(" (select s.numconrequ, s.mes, s.anio from sticalculos s, etidescrmater e ");
  SQL.append(" where s.mes="+mes+" and s.anio="+anio+" and s.stcalculo in ('1109','1110') "); 
  SQL.append(" and s.numconrequ = e.numconrequ and e.procomerci_ant=1 and s.stregistro is null) req, ctiregtiempos c, stiajuste fa ");
  SQL.append(" where req.numconrequ = c.numconrequ and to_char(c.fechacaptu,'mm')="+mes+" and to_char(c.fechacaptu,'yyyy')="+anio);
  SQL.append(" and fa.mes="+mes+" and fa.anio="+anio);
  SQL.append(" group by substr(c.cvemanobrc,1,4)  ");
  SQL.append(" union all ");
  SQL.append(" select substr(c.cvemanobrc,1,4) clave, sum((c.tiempo/60)*fa.fmod) importeMOD from  ");
  SQL.append(" (select s.numorden, s.mes, s.anio from sticalculos s, etidescrmater e, etiordenprodu o ");
  SQL.append(" where s.mes="+mes+" and s.anio= "+anio+" and s.stcalculo in ('1101','1103','1104')  ");
  SQL.append(" and s.numorden = o.numorden and o.numconrequ = e.numconrequ and e.procomerci_ant=1 and s.stregistro is null) op, ");
  SQL.append(" ctiregtiempos c, stiajuste fa ");
  SQL.append(" where op.numorden = c.numorden and to_char(c.fechacaptu,'mm')="+mes+" and to_char(c.fechacaptu,'yyyy')="+anio);
  SQL.append(" and fa.mes="+mes+" and fa.anio="+anio);
  SQL.append(" group by substr(c.cvemanobrc,1,4)) aj, ");
  SQL.append(" (select faj.clave, sum(faj.importemod) importemod from ");
  SQL.append(" (select substr(c.cvemanobrc,1,4) clave, sum((c.tiempo/60)*fa.fmod) importeMOD from  ");
  SQL.append(" (select s.numconrequ, s.mes, s.anio from sticalculos s, etidescrmater e ");
  SQL.append(" where s.mes="+mes+" and s.anio= "+anio+" and s.stcalculo in ('1109','1110')  ");
  SQL.append(" and s.numconrequ = e.numconrequ and e.procomerci_ant=0 and s.stregistro is null) req, ctiregtiempos c, stiajuste fa ");
  SQL.append(" where req.numconrequ = c.numconrequ and to_char(c.fechacaptu,'mm')="+mes+" and to_char(c.fechacaptu,'yyyy')="+anio);
  SQL.append(" and fa.mes="+mes+" and fa.anio = "+anio+" group by substr(c.cvemanobrc,1,4)  ");
  SQL.append(" union all ");
  SQL.append(" select substr(c.cvemanobrc,1,4) clave, sum((c.tiempo/60)*fa.fmod) importeMOD from  ");
  SQL.append(" (select s.numorden, s.mes, s.anio from sticalculos s, etidescrmater e, etiordenprodu o ");
  SQL.append(" where s.mes="+mes+" and s.anio= "+anio+" and s.stcalculo in ('1101','1103','1104')  ");
  SQL.append(" and s.numorden = o.numorden and o.numconrequ = e.numconrequ and e.procomerci_ant=0 and s.stregistro is null) op, ");
  SQL.append(" ctiregtiempos c, stiajuste fa ");
  SQL.append(" where op.numorden = c.numorden and to_char(c.fechacaptu,'mm')="+mes+" and to_char(c.fechacaptu,'yyyy')="+anio);
  SQL.append(" and fa.mes="+mes+" and fa.anio = "+anio+" group by substr(c.cvemanobrc,1,4) ");
  SQL.append(" union all ");
  SQL.append(" select substr(c.cvemanobrc,1,4) clave, sum((c.tiempo/60)*fa.fmod) importeMOD from  ");
  SQL.append(" (select s.cvetrameno from sticalculos s where s.stcalculo in ('1105','1106') and s.mes="+mes+" and s.anio="+anio+") tm, ");
  SQL.append(" ctiregtiempos c, stiajuste fa ");
  SQL.append(" where tm.cvetrameno = c.cvetrameno and to_char(c.fechacaptu,'mm')="+mes+" and to_char(c.fechacaptu,'yyyy')="+anio);
  SQL.append(" and fa.mes="+mes+" and fa.anio = "+anio+" group by substr(c.cvemanobrc,1,4) ");
  SQL.append(" union all ");
  SQL.append(" select substr(c.cvemanobrc,1,4) clave, sum((c.tiempo/60)*fa.fmod) importeMOD from  ");
  SQL.append(" (select s.cvetrainte from sticalculos s where s.stcalculo in ('1107','1108') and s.mes="+mes+" and s.anio="+anio+") ti, ");
  SQL.append(" ctiregtiempos c, stiajuste fa ");
  SQL.append(" where ti.cvetrainte = c.cvetrainte and to_char(c.fechacaptu,'mm')="+mes+" and to_char(c.fechacaptu,'yyyy')="+anio);
  SQL.append(" and fa.mes="+mes+" and fa.anio = "+anio+" group by substr(c.cvemanobrc,1,4)) faj ");
  SQL.append(" group by faj.clave) ajnc ");
  SQL.append(" where aj.clave(+) = substr(h.cvemanobrc,1,4) and ajnc.clave = substr(h.cvemanobrc,1,4) ");
  SQL.append(" and h.mes="+mes+" and h.anio="+anio);
  SQL.append(" group by h.idproceso order by  h.idproceso ");

  try{
    crs = new CachedRowSet();
    crs.setCommand(SQL.toString());
    crs.execute(con);
    crs.beforeFirst();
    i=0;
    while (crs.next()) {
      i++;
      impMODc  = Double.parseDouble((crs.getString("modc")== null)? "0.00":crs.getString("modc"));
      if (impMODc  < 0.00) { impMO = decimales(impMO)+decimales(impMODc);   hm.put("IMPORTE_"+i+"_1_1",decimales(impMODc));  }
      impMODnc  = Double.parseDouble((crs.getString("modnc")== null)? "0.00":crs.getString("modnc"));
      if (impMODnc < 0.00) { impMO = decimales(impMO)+decimales(impMODnc);  hm.put("IMPORTE_"+i+"_2_1",decimales(impMODnc));  }
    }
    if (impMO>0.00) { hm.put("IMPORTE_0001",decimales(impMO)); }
    else hm.clear();
  }
  catch (Exception e){
    System.out.println("Ha ocurrido un error en el metodo SiafmCostosProduccion.cmAjusteNegativoMOCM");
    System.out.println("SiafmCostosProduccion.cmAjusteNegativoMOCM : " + SQL.toString());
    crs.close();
    crs = null;
    throw e;
  }
  finally {
    SQL.setLength(0);
    SQL = null;
    crs= null;
  } 
  return hm; 
}


} // END SiafmCostosProduccion
