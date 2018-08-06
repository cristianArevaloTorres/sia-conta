package sia.rf.tesoreria.libs.archivos;

import java.sql.Connection;

import sia.db.dao.DaoFactory;
import sia.db.sql.Sentencias;

import sia.libs.formato.Error;

public class GeneraSentencia {

  private StringBuffer campos = new StringBuffer();
  private String tabla = null;
  
  
  public GeneraSentencia(StringBuffer campos, String tabla) {
    setCampos(campos);
    setTabla(tabla);
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
      Error.mensaje(e,"TESORERIA");
      regresa = false; 
    } finally {
      sentencias = null;
    }
    return regresa;
  }
  
  
  public boolean ejecutaSentenciaUpdate(String condicion, Connection conn, String tabla, String camposValores){
    boolean regresa = false;
    int ejecuto = -1;
    Sentencias sentencias = new Sentencias(DaoFactory.CONEXION_TESORERIA);
   try  {
   /// UPDATE RF_TR_SALDOS_RESERVA
   // SET  saldo_cierre = 10.00
   // WHERE id_cuenta =1051 and trunc(fecha) = to_date('22/09/2010','dd/mm/yyyy') and banco = 'BBVA';
      ejecuto = sentencias.ejecutar(conn,"update ".concat(tabla).concat(" set ").concat(camposValores).concat(" where ").concat(condicion));
      if (ejecuto!=-1)
        regresa = true;
      else 
        regresa = false; 
    } catch (Exception e) {
      Error.mensaje(e,"TESORERIA");
      regresa = false; 
    } finally {
      sentencias = null;
    }
    return regresa;
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
}
