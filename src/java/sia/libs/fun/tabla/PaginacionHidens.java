package sia.libs.fun.tabla;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspWriter;

import sia.db.sql.SentenciasCRS;

public class PaginacionHidens extends PaginacionSE {
  
  private List<Campo> hidens;
  
  public PaginacionHidens() {
  }
  
  public void addHidden(Campo campo) {
    if(hidens==null) {
      hidens = new ArrayList();
    }
    hidens.add(campo);
  }


  protected int ImprimeTodosLasCeldas2(SentenciasCRS crsTabla, String Claves,
                                       int registroActual,
                                       int registrosDesplegar, int total_reg,
                                       JspWriter out,
                                       String ruta) throws SQLException,
                                                           Exception {
                                                           
    int numReg = 0, j = 0;
    try {
      int hasta = 
        registroActual + registrosDesplegar; //----------- VARIABLE "HASTA" CONTROLA LA CANTIDAD DE REGISTROS QUE SE MOSTRANRAN EN PANTALLA ------------ 
      if (hasta >= (total_reg)) {
        hasta = total_reg;
      }
      if (registroActual > 0) {
        crsTabla.absolute(registroActual);
      }
      numReg = registroActual;
      while (crsTabla.next() && numReg < hasta) {
        numReg++;
        out.print("<tr onmouseover='over(this)' onmouseout='out(this)'".concat(ImprimeFormatoFila(numReg)).concat(" \n"));
        
        String concatClaves = ObtenerClaves2(Claves);
        concatClaves = concatClaves.substring(0, concatClaves.length() - 1);
        
        if (this.tipo!=null)
          ImprimirCeldaCheck(concatClaves, out);
        
        out.print("<td align='center'>" + (registroActual + 1) + "");
        for(Campo hiden:hidens) {
          out.print("<input type='hidden' name='"+hiden.getNombreCol()+"' id='"+hiden.getNombreCol()+"' value='"+crsTabla.getString(hiden.getCampo())+"'>");
        }
        out.print("</td>");
        ImprimeCeldas(out);
        //ImprimirCeldaVerMas(verMas, numReg, Tabla, Condiciones, out);
        ImprimeOpciones(out,ruta,concatClaves);
       
        out.println("</tr>");
        registroActual++;
        j++;
      } //while
      return (registroActual);
    } catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo hpagseleccionada.ImprimeTodosLasCeldas2 "+ e.getMessage()==null?"":e.getMessage());
      throw e;
      //        return 0;
    }
  }
}
