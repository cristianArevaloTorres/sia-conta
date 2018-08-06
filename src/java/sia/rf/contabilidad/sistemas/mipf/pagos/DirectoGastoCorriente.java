package sia.rf.contabilidad.sistemas.mipf.pagos;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.HashMap;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import sia.rf.contabilidad.sistemas.mipf.modDescarga.Aportacion;
import sia.rf.contabilidad.sistemas.mipf.modDescarga.Funcion;
import sia.rf.contabilidad.sistemas.mipf.transformacion.Totales;

import sun.jdbc.rowset.CachedRowSet;

public class DirectoGastoCorriente {

    private Totales totales=null;
    private Funcion funcion=null;
    private CachedRowSet crsSentenciaFuncion=null;

    public DirectoGastoCorriente() {
      totales = new Totales();
      funcion = new Funcion();
    }
    
    public Double calcula(Connection conexionContabilidad, Connection conexionSistema, String evento, String parametros) throws SQLException, Exception{
      totales.calcutaTotales(conexionContabilidad,conexionSistema,evento, parametros);
      System.out.println("totales "+totales.getTotalBruto());
      return totales.getTotalBruto();  
    }
    
    public Double totalBruto(){
        return totales.getTotalBruto();
    }
    
    public String creaHashMap(Connection conexionContabilidad, Connection conexionSistema, int numForma) throws SQLException, Exception{
        Context context = Context.enter();
        Scriptable scope = context.initStandardObjects();
        context.getWrapFactory().setJavaPrimitiveWrap(false);
        org.mozilla.javascript.Script compiled = null;
        Object r = null;
        
        String anticipo="";
        String capitulo="";
        Object resultado=0;
        String llave=null;
        double valor=0;
        boolean bandera=false;
        
        HashMap variables=null; 
        variables = new HashMap();
        try{
          crsSentenciaFuncion=funcion.select_rf_tc_FuncionesPorForma( numForma); 
          crsSentenciaFuncion.beforeFirst();
          while (crsSentenciaFuncion.next()){
            anticipo="1"; // De donde lo vamos a tomar
            capitulo="1000"; // De donde lo vamos a tomar
            bandera=false;
            resultado="0";
            if (crsSentenciaFuncion.getString("regla_contable").equals("SIN")){ 
              bandera=true;
            }
            else{          
              ScriptableObject.putProperty(scope,"anticipo",Context.javaToJS(anticipo,scope));
              ScriptableObject.putProperty(scope,"capitulo",Context.javaToJS(capitulo,scope));
              //compiled = context.compileString("function GPONALPROV(partida, tipoEje, tipoDoc){ if ( (partida==1408) && (!((tipoEje==11) || (tipoEje==12) || (tipoEje==21) || (tipoEje==22) || (tipoEje==23) || (tipoEje==24))) )    return '1'; else return '0'; } var resultado=GPONALPROV(partida, tipoEje, tipoDoc); ","defLoaf",0,null);
              System.out.println("regla: "+crsSentenciaFuncion.getString("regla_contable"));
              compiled = context.compileString(crsSentenciaFuncion.getString("regla_contable"),"defLoaf",0,null);             
              compiled.exec(context,scope);
              resultado=ScriptableObject.getProperty(scope,"resultado"); 
              if (resultado.equals("1")){
                bandera=true;
              }  
            }
            if (bandera==true){
              llave=crsSentenciaFuncion.getString("nombreVariable");
              if (crsSentenciaFuncion.getString("idVariable").equals("1"))  // HASTA AHORITA TOTAL BRUTO
                valor=totales.getTotalBruto();
              variables.put(llave,valor);
            }
          }  
        } 
        catch(Exception e){ 
           System.out.println("Ocurrio un error al accesar al metodo creaHashMap "+e.getMessage()); 
           throw e; 
         } //Fin catch 
         finally{    
         }    
      return "2";
    }
}
