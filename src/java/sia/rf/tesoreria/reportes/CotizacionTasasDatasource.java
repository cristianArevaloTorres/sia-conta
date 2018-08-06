package sia.rf.tesoreria.reportes;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;


public class CotizacionTasasDatasource implements JRDataSource {

  private List <CotizacionTasas> datosCotizacion = new ArrayList<CotizacionTasas>();
  private int indiceCotizacion = -1; 
  

  public boolean next() throws JRException {
  
    return ++indiceCotizacion < datosCotizacion.size();

  }

  public Object getFieldValue(JRField jrField) throws JRException{
    Object valor = null;
    if("institucionFinanciera".equals(jrField.getName())) 
    { 
        valor = datosCotizacion.get(indiceCotizacion).getInstitucionFinanciera(); 
    } 
    else if("dia1".equals(jrField.getName())) 
    { 
        valor = datosCotizacion.get(indiceCotizacion).getDia1(); 
    } 
    else if("dias7".equals(jrField.getName())) 
    { 
        valor = datosCotizacion.get(indiceCotizacion).getDias7(); 
    } 
    else if("dias14".equals(jrField.getName())) 
    { 
        valor = datosCotizacion.get(indiceCotizacion).getDias14(); 
    } 
    else if("dias21".equals(jrField.getName())) 
    { 
        valor = datosCotizacion.get(indiceCotizacion).getDias21(); 
    } 
    else if("dias28".equals(jrField.getName())) 
    { 
        valor = datosCotizacion.get(indiceCotizacion).getDias28(); 
    } 
    else if("plazo1".equals(jrField.getName())) 
    { 
        valor = datosCotizacion.get(indiceCotizacion).getPlazo1(); 
    } 
    else if("plazoMes".equals(jrField.getName())) 
    { 
        valor = datosCotizacion.get(indiceCotizacion).getPlazoMes(); 
    } 
    else if("plazoRango".equals(jrField.getName())) 
    { 
        valor = datosCotizacion.get(indiceCotizacion).getPlazoRango(); 
    } 
    else if("plazoSemana".equals(jrField.getName())) 
    { 
        valor = datosCotizacion.get(indiceCotizacion).getPlazoSemana(); 
    }       
    return valor; 
  }
  
  
  public void addCotizacionTasas(CotizacionTasas cotizacionTasas)
     {
        this.datosCotizacion.add(cotizacionTasas);
       
     }

}
