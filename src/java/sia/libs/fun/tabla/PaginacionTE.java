package sia.libs.fun.tabla;

import java.io.IOException;

import java.sql.SQLException;

import javax.servlet.jsp.JspWriter;

import sia.db.sql.SentenciasCRS;

public class PaginacionTE extends PaginacionSE {

  private int numMaxRegistros;
  
  public PaginacionTE() {
    setNumMaxRegistros(200);
  }
  
  protected void imprimeSel(JspWriter out) throws IOException {
    out.println("<td width='5%' class='"+ AZUL_OBS +"'>"+ (tipo instanceof SelTipoCheck ?  "<input type='checkbox' value='' onclick=\"selAllJS(this,'"+tipo.getName()+"','tbPagResultado"+noTabla+"')\">":"Sel.")+"</td>");
  }
  
  protected int getTotalReg(int total_reg) {
    int max = total_reg;
    return max > numMaxRegistros ? numMaxRegistros : max;
  }
  
  protected void imprimeNoPagina(JspWriter out,int llamaMis, int hastaaqui, int x, String color) throws IOException {
    out.print("&nbsp;<a onclick=\""+(isOrden()?"ordena"+noTabla+"(this,"+llamaMis+",'"+orden+"')":"pagPagina(this," + llamaMis + "," + noRegaMostrar + ",'tbPagResultado"+noTabla+"')")+"\" title='" + (llamaMis + 1) + 
              " - " + hastaaqui + 
              "' style='cursor:hand'><font color='"+color+"' id='pagColorPagina'>[" + (x + 1) + 
              "]</font></a>&nbsp;");
  }
  
  protected int ImprimeTodosLasCeldas2( 
                                     String Claves,
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
      numReg = registroActual;
      while (crsTabla.next() && numReg <= getNumMaxRegistros()) {
        numReg++;
        out.print("<tr onmouseover='over(this)' onmouseout='out(this)' id='trPagResultado' style='"+ (numReg <= hasta?"":"display:none")+"'".concat(ImprimeFormatoFila(numReg)).concat(" \n"));
        
        String concatClaves = ObtenerClaves2(Claves);
        concatClaves = concatClaves.substring(0, concatClaves.length() - 1);
        
        if (this.tipo!=null)
          ImprimirCeldaCheck(concatClaves, out);
        
        out.print("<td align='center'>" + (registroActual + 1) + "<input type='hidden' id='clave' name='clave' value='"+concatClaves+"'></td>");
        
        
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
  
   public int seleccionarPagina(SentenciasCRS crsTabla, 
                                JspWriter out, int noRegaMostrar, 
                                int PregActual, String ruta, 
                                String CampoClave, SelTipo tipo, int ancho, String tituloTabla, String orden) throws SQLException, 
                                                           Exception {
     this.crsTabla = crsTabla;
     int regActual = 0;
     this.tipo = tipo;
     this.noRegaMostrar = noRegaMostrar;
     this.pagAct =  (PregActual/noRegaMostrar)+1;
     this.regIni = PregActual;
     this.ruta = ruta;
     this.orden = orden;
     try {
       int total_reg = 0;
       setTituloTabla(tituloTabla);
       total_reg = crsTabla.size();
       total_reg = getTotalReg(total_reg);
       ImprimeFunJavaScript(out);
       out.println("<table class='resultado' id='tbPagResultado"+noTabla+"' border='0' width='"+ancho+"%' cellpadding='2' cellspacing='0' align='center'> \n");
       
       ImprimeEncabezado(out);
       imprimePie(out, total_reg, regActual, PregActual);
       ImprimeLosEncabezados2(out);
       regActual =  funcion_de_paso2(CampoClave,  PregActual,
                                     noRegaMostrar, total_reg, out, ruta);
       
       imprimePie(out, total_reg, regActual, PregActual );
       out.println("</table> \n");
       imprimeDesborde(out,crsTabla);
     } catch (Exception e) {
       System.out.println("Ha ocurrido un error en el metodo hpagseleccionada.ImprimeLosEncabezados2");
       e.printStackTrace();
       throw e;
     }
     return regActual;
   }
  
  private void imprimeDesborde(JspWriter out,SentenciasCRS crsTabla) throws IOException {
    if(crsTabla.size() > numMaxRegistros) {
      out.println("<script type='text/javascript' languaje='javascript'> alert('Resultado demasiado grande ["+ crsTabla.size() +"],\\n se recomienda afinar la búsqueda,\\n por seguridad solo se mostrarán ["+numMaxRegistros+"]'); </script>");
    }
  }

  public void setNumMaxRegistros(int numMaxRegistros) {
    this.numMaxRegistros = numMaxRegistros;
  }

  public int getNumMaxRegistros() {
    return numMaxRegistros;
  }
}
