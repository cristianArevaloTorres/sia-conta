package sia.rf.tesoreria.reportes;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class TransferenciaFondosDatasource implements JRDataSource{

    private List <TransferenciaFondos> datosTransferencia = new ArrayList<TransferenciaFondos>();
    private int indiceTransaccion = -1;

    
    public boolean next() throws JRException {
    
      return ++indiceTransaccion < datosTransferencia.size();

    }

    public Object getFieldValue(JRField jrField) throws JRException{
      Object valor = null;
      if("banco".equals(jrField.getName())) 
      { 
          valor = datosTransferencia.get(indiceTransaccion).getBanco(); 
      } 
      else if("numCuenta".equals(jrField.getName())) 
      { 
          valor = datosTransferencia.get(indiceTransaccion).getNumCuenta(); 
      } 
      else if("nomCuenta".equals(jrField.getName())) 
      { 
          valor = datosTransferencia.get(indiceTransaccion).getNomCuenta(); 
      } 
      else if("montoCargo".equals(jrField.getName())) 
      { 
          valor = datosTransferencia.get(indiceTransaccion).getMontoCargo(); 
      } 
      else if("montoAbono".equals(jrField.getName())) 
      { 
          valor = datosTransferencia.get(indiceTransaccion).getMontoAbono(); 
      } 
      else if("referencia".equals(jrField.getName())) 
      { 
          valor = datosTransferencia.get(indiceTransaccion).getReferencia(); 
      } 
      else if("grupo".equals(jrField.getName())) 
      { 
          valor = datosTransferencia.get(indiceTransaccion).getGrupo(); 
      } 
      return valor; 
    }
    
    public void addTransferencia(TransferenciaFondos transferenciaFondos)
       {
          this.datosTransferencia.add(transferenciaFondos);
         
       }
}
