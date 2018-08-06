package sia.rf.tesoreria.administrarComisiones.servicios;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sia.db.dao.DaoFactory;
import sia.db.sql.Sentencias;
import sia.db.sql.Vista;

import sia.xml.Dml;

public class DiaAnteriorHabil {
  public DiaAnteriorHabil() {
  }
  

    public String obtenerDiaAnteriorHabil(String fecha, String nombreSelect){
      String  strQuery = null; 
      Map parametros = new HashMap();
      parametros.put("0",fecha);
      Dml dml;
          try {
              dml = new Dml(Dml.TESORERIA);
              strQuery = dml.getSelect("movimientos",nombreSelect,parametros);
          } catch (Exception e) {
              System.out.println();
          }
      return ejecutaSentencia(strQuery);
    }
    
    public String ejecutaSentencia (String strQuery) {
      Sentencias sentencias = null;
      List <Vista> vistaResultado = null;
        try{
            sentencias = new Sentencias(DaoFactory.CONEXION_TESORERIA);
            vistaResultado = sentencias.registros(strQuery) ;    
            if (vistaResultado == null) 
                strQuery = null;
        }
        catch(Exception e){
            System.err.println("[diaAnteriorHabil.ejecutaSentencia] Error: "+e);
            e.printStackTrace();
        }
        finally{
            if (sentencias != null )
                sentencias = null;
        }
        
      return vistaResultado.get(0).getField("FECHA_INICIAL");
    }
    
}
