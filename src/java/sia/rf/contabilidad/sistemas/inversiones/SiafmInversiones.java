package sia.rf.contabilidad.sistemas.inversiones;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;

import sia.db.dao.DaoFactory;
import sia.db.sql.Sentencias;
import sia.db.sql.Vista;

import sia.rf.contabilidad.registroContableEvento.Cadena;

import sun.jdbc.rowset.CachedRowSet;

public class SiafmInversiones {
  public SiafmInversiones() {
  } 
    
  public String registrosContablesHSBC(String fForma,String fechaInicio, String fechaFin,String numMov) throws SQLException, Exception {
    HashMap hm = new HashMap();
    List<Vista> registros = null;
    Sentencias sentencia = null;
    StringBuffer cadenaTem = new StringBuffer("");
    StringBuffer SQL= new StringBuffer("");
    List<Vista> registroCondicion = null;
    try {
      registros = new ArrayList<Vista>();
      registroCondicion = new ArrayList<Vista>();
      registroCondicion = condicionHSBC(numMov);
      sentencia = new Sentencias(DaoFactory.CONEXION_TESORERIA); 
      int c=0;
        SQL.append(" select t.id_cuenta_inversion,t.id_tipo_operacion, ");
//        SQL.append(registroCondicion.get(0).getField("COND_VARIABLES")+" COND_VARIABLES,");
        SQL.append("        t.importe monto,t.fecha, ");
        SQL.append("        ('idTOp'||' '||t.id_tipo_operacion||'. MV: ' || ");
        SQL.append(numMov);
        SQL.append("    ||' ID_MOV: ' || t.id_operacion) referencia ");
        SQL.append("    \nfrom RF_TR_OPERACIONES_INVERSION  t");
        SQL.append("    \nwhere ");
        SQL.append(" (t.id_cuenta_inversion='2') ");
        SQL.append(registroCondicion.get(0).getField("CONDICION"));
        SQL.append(" and to_date(to_char(t.fecha,'dd/mm/yyyy'),'dd/mm/yyyy') >= to_date('"+fechaInicio+"', 'dd/mm/yyyy')  ");
        SQL.append(" and to_date(to_char(t.fecha,'dd/mm/yyyy'),'dd/mm/yyyy') < to_date('"+fechaFin+"', 'dd/mm/yyyy') + 1 ");
        SQL.append("\norder by monto");
        
      System.out.println("SiafmInversiones.selectFormaHSBC: "+SQL.toString());
      registros = sentencia.registros(SQL.toString());
        registros = sentencia.registros(SQL.toString());
        if(registros != null){
          c=0;
          for(Vista registro: registros){
              hm.put ("REFERENCIA",registro.getField("REFERENCIA")==null?" ":registro.getField("REFERENCIA"));
              hm.put ("IMPORTE",registro.getField("MONTO"));
              cadenaTem.append(Cadena.construyeCadena(hm));
              cadenaTem.append("~");
              hm.clear();
              c=c+1;
          }
        }
    }
    catch (Exception e){
      System.out.println ("Ha ocurrido un error en el metodo selectFormaHSBC");
      System.out.println ("SiafmInversiones.registrosContablesHSBC: " + SQL.toString());
      throw e;
    } 
    finally {
      SQL.setLength(0);
      SQL=null;
      sentencia = null;
    }
    return cadenaTem.toString();
    
  }
  
  private List<Vista> condicionHSBC(String movimiento) throws SQLException, Exception {
    List<Vista> registros = null;
    Sentencias sentencia = null;
    StringBuffer SQL= new StringBuffer("select * from rf_tr_hsbc_regcontab_arm where mov=" + movimiento+ " and id_banco=2");
    try{
      registros = new ArrayList<Vista>();
      sentencia = new Sentencias(DaoFactory.CONEXION_TESORERIA); 
      registros = sentencia.registros(SQL.toString());
      System.out.println ("SiafmInversiones.condicionHSBC: " + SQL.toString());
    }
    catch (Exception e){
      System.out.println ("Ha ocurrido un error en el metodo condicionHSBC");
      System.out.println ("SiafmInversiones.condicionHSBC: " + SQL.toString());
      throw e;
    } 
    finally {
      SQL.setLength(0);
      SQL=null;
      sentencia = null;
    }
    return registros;
  }  
  
  public String registrosContablesBMX(String fForma,String fechaInicio, String fechaFin,String numMov) throws SQLException, Exception {
      HashMap hm = new HashMap();
      List<Vista> registros = null;
      List<Vista> registroCondicion = null;
     
      Sentencias sentencia = null;
      StringBuffer cadenaTem = new StringBuffer("");
      StringBuffer SQL= new StringBuffer("");
   
      try {
        registros = new ArrayList<Vista>();
        registroCondicion = new ArrayList<Vista>();
        registroCondicion = condicionBMX(numMov);
        sentencia = new Sentencias(DaoFactory.CONEXION_TESORERIA);
        int c=0;
   //query general
          SQL.append(" select t.id_cuenta_inversion,t.id_tipo_operacion, ");
          SQL.append(registroCondicion.get(0).getField("COND_VARIABLES"));
          SQL.append("        t.importe MONTO,t.fecha, ");
          SQL.append("        ('idTOp'||' '||t.id_tipo_operacion||'. MV: ' || ");
          SQL.append(numMov);
          SQL.append("    ||' ID_MOV ' || t.id_operacion) REFERENCIA ");
          SQL.append("    \nfrom RF_TR_OPERACIONES_INVERSION  t");
          SQL.append("    \nwhere ");
          SQL.append(" (t.id_cuenta_inversion='1' or t.id_cuenta_inversion='13')");
          SQL.append(registroCondicion.get(0).getField("CONDICION"));
          SQL.append(" and to_date(to_char(t.fecha,'dd/mm/yyyy'),'dd/mm/yyyy') >= to_date('"+fechaInicio+"', 'dd/mm/yyyy')  ");
          SQL.append(" and to_date(to_char(t.fecha,'dd/mm/yyyy'),'dd/mm/yyyy') < to_date('"+fechaFin+"', 'dd/mm/yyyy') + 1 ");
          SQL.append("\norder by monto");
          System.out.println("SiafmInversiones.registrosContablesBanamex: "+SQL.toString());
         
          registros = sentencia.registros(SQL.toString());
          if(registros != null){
            c=0;
            for(Vista registro: registros){
                hm.put ("VAR1",registro.getField("VAR1"));
                hm.put ("REFERENCIA",registro.getField("REFERENCIA")==null?" ":registro.getField("REFERENCIA"));
                hm.put ("IMPORTE",registro.getField("MONTO"));
                cadenaTem.append(Cadena.construyeCadena(hm));
                cadenaTem.append("~");
                hm.clear();
                c=c+1;
            }
          }
      }
      catch (Exception e){
        System.out.println ("Ha ocurrido un error en el metodo registrosContablesBanamex");
        System.out.println ("SiafmInversiones.registroscontablesBanamex: " + SQL.toString());
        throw e;
      }
      finally {
        SQL.setLength(0);
        SQL=null;
        sentencia = null;
      }
      return cadenaTem.toString();
      }
   
  private List<Vista> condicionBMX(String movimiento) throws SQLException, Exception {
    List<Vista> registros = null;
    Sentencias sentencia = null;
    StringBuffer SQL= new StringBuffer("select * from rf_tr_hsbc_regcontab_arm where id_banco=1 and mov=" + movimiento);
    try{
      registros = new ArrayList<Vista>();
      sentencia = new Sentencias(DaoFactory.CONEXION_TESORERIA);
      registros = sentencia.registros(SQL.toString());
      System.out.println ("SiafmInversiones.condicionBMX: " + SQL.toString());
    }
    catch (Exception e){
      System.out.println ("Ha ocurrido un error en el metodo Lista");
      System.out.println ("SiafmInversiones.condicionBMX: ");
      throw e;
    }
    finally {
     SQL.setLength(0);
      SQL=null;
      sentencia = null;
    }
    return registros;
  }

}

  
