package sia.rf.contabilidad.reportes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sia.db.dao.DaoFactory;
import sia.db.sql.Sentencias;
import sia.db.sql.Vista;

public class Entidad {
  public Entidad() {
  }
  
  public String getEntidad(int entidad, int ambito, int unidadEjecutora){
    Sentencias sentencia = null;
    Map parametros = null;
    List<Vista> registros = null;
    String desCiudad = null;
    String regresa = null;
    try{
      sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD,Sentencias.XML);
      parametros = new HashMap();
      registros = new ArrayList<Vista>();
      parametros.put("entidad",entidad);
      parametros.put("ambito",String.valueOf(ambito));
      parametros.put("unidad",unidadEjecutora);
      registros = sentencia.registros("registroContable.select.obtenerEntidad",parametros);
      if(registros!=null){
        for(Vista registro:registros){
          desCiudad = registro.getField("DESC_CIUDAD");
          regresa = desCiudad!=null?desCiudad:"";
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
    return regresa==null?"":regresa;
  }
}
