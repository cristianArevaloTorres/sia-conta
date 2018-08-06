package sia.rf.tesoreria.reportes;
import sia.scriptlets.BarraProgreso;

public class scriptletTesoreria extends BarraProgreso {
    public scriptletTesoreria() {
    }
    
    public String getNombreMes(int mes)  {
      String[] nombreMes= {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
      return nombreMes[mes];
    }; // getNombreMes        
}
