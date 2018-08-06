package sia.rf.tesoreria.saldoReserva.acciones;

import java.sql.Connection;

import sia.db.dao.DaoFactory;
import sia.db.sql.Sentencias;

public class GeneraSentenciaSR {

    private StringBuffer campos = new StringBuffer();
    private String tabla = null;

    public GeneraSentenciaSR(StringBuffer campos, String tabla) {
        setCampos(campos);
        setTabla(tabla);
    }

    public void setCampos(StringBuffer campos) {
        this.campos = campos;
    }

    public StringBuffer getCampos() {
        return campos;
    }

    public void setTabla(String tabla) {
        this.tabla = tabla;
    }

    public String getTabla() {
        return tabla;
    }
    
    public boolean ejecutaSentenciaUpdate(String condicion, Connection conn, String tabla, String camposValores){
      boolean regresa = false;
      int ejecuto = -1;
      Sentencias sentencias = new Sentencias(DaoFactory.CONEXION_TESORERIA);
      try  {
        ejecuto = sentencias.ejecutar(conn,"update ".concat(tabla).concat(" set ").concat(camposValores).concat(" where ").concat(condicion));
        if (ejecuto!=-1)
          regresa = true;
        else 
          regresa = false; 
      } catch (Exception e) {
            e.printStackTrace();
            regresa = false; 
      } finally {
        sentencias = null;
      }
      return regresa;
    }
    
    
    public boolean ejecutaSentenciaInsert(StringBuffer valores, Connection conn){
      boolean regresa = false;
      int ejecuto = -1;
      Sentencias sentencias = new Sentencias(DaoFactory.CONEXION_TESORERIA);
     try  {
        ejecuto = sentencias.ejecutar(conn,"insert into ".concat(getTabla()).concat(" (").concat(getCampos().toString()).concat(") values (").concat(valores.toString()).concat(")"));
        if (ejecuto!=-1)
          regresa = true;
        else 
          regresa = false; 
      } catch (Exception e) {
            e.printStackTrace();
            regresa = false; 
      } finally {
        sentencias = null;
      }
      return regresa;
    }
}
