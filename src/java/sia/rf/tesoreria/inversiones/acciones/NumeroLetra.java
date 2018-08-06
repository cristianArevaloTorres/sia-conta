package sia.rf.tesoreria.inversiones.acciones;

import java.sql.CallableStatement;
import java.sql.Connection;

import java.sql.Types;

import sia.libs.formato.Error;

public class NumeroLetra {

  public NumeroLetra() {
  }
  
  public String getCantidadLetra (Number monto, Connection conn){
   String regresa = null;
   CallableStatement st = null;
   StringBuffer sbProceso = new StringBuffer();
   sbProceso.append("begin ? :=  CANTIDADCONLETRA(");
   sbProceso.append(monto);
   sbProceso.append("); end;");
   try {
     st = conn.prepareCall(sbProceso.toString());
     st.registerOutParameter(1,Types.VARCHAR);
     st.execute();
     regresa = st.getString(1);
   } catch (Exception ex)  { 
     Error.mensaje(ex,"TESORERIA");
   } 
   return regresa;
  }
}
