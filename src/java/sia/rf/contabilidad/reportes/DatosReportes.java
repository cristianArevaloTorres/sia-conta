package sia.rf.contabilidad.reportes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sia.db.dao.DaoFactory;
import sia.db.sql.Sentencias;
import sia.db.sql.Vista;

import sia.libs.formato.Fecha;

public class DatosReportes {
    public DatosReportes() {
    }
    
    public String getPolizaCancelada(String polizaId, String campo){
      Sentencias sentencia = null;
      Map parametros = null;
      List<Vista> registros = null;
      //String desCiudad = null;
      String regresa = null;
      try{
        sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD,Sentencias.XML);
        parametros = new HashMap();
        registros = new ArrayList<Vista>();
        parametros.put("polizaId",polizaId);
        registros = sentencia.registros("reportes.select.obtenerPolizaCancelada",parametros);
        if(registros!=null){
          for(Vista registro:registros){ 
            if(campo.equals("consecutivo"))
              regresa = registro.getField("POLIZA");
            else{
              regresa = registro.getField("MES");
              regresa = Fecha.getNombreMes(Integer.valueOf(regresa)-1);
            }
          }
          
        }
      }
      catch(Exception e){
        e.printStackTrace();
      }
      finally{
        sentencia = null;
        registros = null;
        parametros = null;
      }
      return regresa;
    }
      
  public List<Vista> obtenerTitulos(String unidad, String entidad, String ambito){
      List<Vista> regresar = null; 
      Sentencias sentencia = null;
      Map parametros = null;
      
      try {
          sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD,Sentencias.XML);
          parametros = new HashMap();
          regresar = new ArrayList<Vista>();
          parametros.put("unidad",unidad);
          if(ambito.equals("3"))
            parametros.put("condicion","and entidad="+entidad);
          else
            parametros.put("condicion","");
          parametros.put("ambito",ambito);
          regresar = sentencia.registros("reportes.select.obtenerTitulos",parametros);
      }
      catch(Exception e){
        e.printStackTrace();
      }
      finally{
        sentencia = null;
        parametros = null;
      }
      return regresar;
  }
  
  public String obtenerProgramas(String programa){
      String regresar = null;
      if(programa.equals("0005"))
        regresar = "DE INGRESOS POR VENTA";
      else
        if(programa.equals("0006"))
          regresar = "DEL BANCO MUNDIAL";
        else
          if(programa.equals("0001"))
            regresar = "DEL PROGRAMA REGULAR";  
          else
            if(programa.equals("0007"))
              regresar = "DE INGRESOS INSTITUCIONALES";  
              else
                if(programa.equals("0007"))
                  regresar = "DEL BANCO MUNDIAL";  
                  else
                    regresar = "";
      return regresar;
  }
}
