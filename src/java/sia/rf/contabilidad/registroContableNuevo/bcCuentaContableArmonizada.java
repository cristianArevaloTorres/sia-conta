package sia.rf.contabilidad.registroContableNuevo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class bcCuentaContableArmonizada extends bcCuentaContable {
  
  public static final int NIVEL_OPERACIONES = 4;
  public static final int NIVEL_UNIDAD = 3;
  public static final int NIVEL_AMBITO = 4;
  
  public bcCuentaContableArmonizada() {
  }
  
  protected String getCondicion(String pCatCuentaId,String pnumEmpleado,String pUniEje,String temAmbito,int nivRep) {
    String regresa = null;
    if(!pUniEje.equals("0100")) {
      if   ((!pCatCuentaId.equals("2"))&&(!(pnumEmpleado.equals("39547")||pnumEmpleado.equals("16107")||pnumEmpleado.equals("46168")||pnumEmpleado.equals("78258")||pnumEmpleado.equals("46657")||pnumEmpleado.equals("21068")||pnumEmpleado.equals("3509")))){
        regresa= getCond(pUniEje,temAmbito, nivRep);
      } 
    }
    return regresa;
  
  }
  
  protected String getCond(String pUniEje,String temAmbito,int nivRep) {
    String condicion=null;
    if(nivRep == NIVEL_UNIDAD)  
      condicion=" and substr(cuenta_contable,10,4) = '" + pUniEje +"'"; 
    if(nivRep >= NIVEL_AMBITO)
      condicion= " and substr(cuenta_contable,10,8) = '" + pUniEje + temAmbito + "'"; 
    return condicion;
  }

  public ResultSet cargaSubcuentasArm(Connection con, String pCatCuentaId, String pUniEje, String pEntidad, String pAmbito, String pEjercicio, String pnumEmpleado, int nivRep, String configuracion, String ctas) throws SQLException, Exception {
    Statement stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
    ResultSet rsQuery = null;
      String x="";
      StringBuffer SQL = new StringBuffer("");
      String condicion="";
      String condicionUniEje="";
      String temAmbito="";
      
      if (pEntidad.length()==1)
        temAmbito="00"+pEntidad+pAmbito;
      else  
         temAmbito="0"+pEntidad+pAmbito;
      if (pUniEje.length()==3) 
        pUniEje="0"+pUniEje;
      
      //Cambio agregado por CLHL 05/08/2009 para que para el catálogo de cuentas 2 (Ingresos x ventas - banquito)
      //Le muestre todas las unidades ejecutoras, ya que ellos entrar con la 100 pero pueden agregarle a cualquier unidad.
      condicion = getCondicion(pCatCuentaId,pnumEmpleado,pUniEje,temAmbito,nivRep);
      condicion = condicion==null ? "":condicion;
      // StringBuffer SQL = new StringBuffer("select nivel"+nivRep+" cuenta,descrip from rtcCuentas where nivel1='"+niv1);
      if (nivRep == 1){
          x ="select F_EXTRAER_NIVEL2(cuenta_contable,"+nivRep+",0,'"+configuracion+"') cuenta, descripcion from RF_TR_CUENTAS_CONTABLES r where nivel = "+ nivRep+ " and id_catalogo_cuenta = '"+pCatCuentaId+"'"+" and extract(year from fecha_vig_ini)="+pEjercicio+" order by cuenta";        
          // x ="select cuenta_contable cuenta, descripcion from RF_TR_CUENTAS_CONTABLES r where nivel = "+ nivRep+ " and id_catalogo_cuenta = '"+pCatCuentaId+"'"+" and extract(year from fecha_vig_ini)="+pEjercicio+" order by cuenta";        
      }
      else {
          x ="select F_EXTRAER_NIVEL2(cuenta_contable,"+nivRep+",0,'"+configuracion+"') cuenta, descripcion from RF_TR_CUENTAS_CONTABLES r where cuenta_contable like '"+ctas+"%' and nivel = "+ nivRep+ " and id_catalogo_cuenta = '"+pCatCuentaId+"'"+ condicion + " and extract(year from fecha_vig_ini)="+pEjercicio+" order by cuenta";        
          // x ="select cuenta_contable cuenta, descripcion from RF_TR_CUENTAS_CONTABLES r where cuenta_contable like '"+ctas+"%' and nivel = "+ nivRep+ " and id_catalogo_cuenta = '"+pCatCuentaId+"'"+ condicion + " and extract(year from fecha_vig_ini)="+pEjercicio+" order by cuenta";        
      }
      SQL.append(x);
      //System.out.println("va "+SQL.toString());
      rsQuery = stQuery.executeQuery(SQL.toString());
      return rsQuery;
  }
  public ResultSet cargaSubcuentasArmAlta(Connection con, String pCatCuentaId, String pUniEje, String pEntidad, String pAmbito, String pEjercicio, String pnumEmpleado, int nivRep, String configuracion, String ctas) throws SQLException, Exception {
    Statement stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
    ResultSet rsQuery = null;
      String x="";
      StringBuffer SQL = new StringBuffer("");
      String condicion="";
      String condicionUniEje="";
      String temAmbito="";
      
      if (pEntidad.length()==1)
        temAmbito="00"+pEntidad+pAmbito;
      else  
         temAmbito="0"+pEntidad+pAmbito;
      if (pUniEje.length()==3) 
        pUniEje="0"+pUniEje;
      
      //Cambio agregado por CLHL 05/08/2009 para que para el catálogo de cuentas 2 (Ingresos x ventas - banquito)
      //Le muestre todas las unidades ejecutoras, ya que ellos entrar con la 100 pero pueden agregarle a cualquier unidad.
      condicion = getCondicion(pCatCuentaId,pnumEmpleado,pUniEje,temAmbito,nivRep);
      condicion = condicion==null ? "":condicion;
      // StringBuffer SQL = new StringBuffer("select nivel"+nivRep+" cuenta,descrip from rtcCuentas where nivel1='"+niv1);
      if (nivRep == 1){
          //x ="select F_EXTRAER_NIVEL2(cuenta_contable,"+nivRep+",0,'"+configuracion+"') cuenta, descripcion from RF_TR_CUENTAS_CONTABLES r where nivel = "+ nivRep+ " and id_catalogo_cuenta = '"+pCatCuentaId+"'"+" and extract(year from fecha_vig_ini)="+pEjercicio+" order by cuenta";        
          x ="select cuenta_mayor as cuenta, descripcion as descripcion from RF_TC_CLASIFICADOR_CUENTAS where extract(year from fecha_vig_fin)>="+pEjercicio+" and conf_cve_cta_cont_id <>0 order by cuenta_mayor";
          // x ="select cuenta_contable cuenta, descripcion from RF_TR_CUENTAS_CONTABLES r where nivel = "+ nivRep+ " and id_catalogo_cuenta = '"+pCatCuentaId+"'"+" and extract(year from fecha_vig_ini)="+pEjercicio+" order by cuenta";        
      }
      else {
          x ="select F_EXTRAER_NIVEL2(cuenta_contable,"+nivRep+",0,'"+configuracion+"') cuenta, descripcion from RF_TR_CUENTAS_CONTABLES r where cuenta_contable like '"+ctas+"%' and nivel = "+ nivRep+ " and id_catalogo_cuenta = '"+pCatCuentaId+"'"+ condicion + " and extract(year from fecha_vig_ini)="+pEjercicio+" order by cuenta";        
          // x ="select cuenta_contable cuenta, descripcion from RF_TR_CUENTAS_CONTABLES r where cuenta_contable like '"+ctas+"%' and nivel = "+ nivRep+ " and id_catalogo_cuenta = '"+pCatCuentaId+"'"+ condicion + " and extract(year from fecha_vig_ini)="+pEjercicio+" order by cuenta";        
      }
      SQL.append(x);
      //System.out.println("va "+SQL.toString());
      rsQuery = stQuery.executeQuery(SQL.toString());
      return rsQuery;
  }
}

