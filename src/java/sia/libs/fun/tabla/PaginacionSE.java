package sia.libs.fun.tabla;

import java.io.IOException;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import java.util.StringTokenizer;

import javax.servlet.jsp.JspWriter;

import sia.db.sql.SentenciasCRS;

import sia.libs.formato.Cadena;

public class PaginacionSE {
  
  private List<Campo> campos;
  protected SentenciasCRS crsTabla;
  private List imgs;
  private List<Img> imgsLt;
  protected SelTipo tipo;
  private String tituloTabla;
  protected String ruta;
  protected final static String AZUL_CLA      = "azulCla";
  private final static String AZUL_CLA_CEN  = "azulClaCen";
  protected final static String AZUL_OBS      = "azulObs";
  protected int pagAct; 
  protected int regIni; 
  private int registro=0; 
  protected String orden;
  protected String noTabla=""; // en caso de que en la pagina se pinten varias tablas a la vez
  protected int noRegaMostrar;
  
  public PaginacionSE() {
  }
  
  public void addCampo(Campo campo) {
    if(campos == null)
      campos = new ArrayList();
    if(campo != null)
      campos.add(campo);
  }
  
  public void addCampo1(CampoOrdena campo) {
    if(campos == null)
      campos = new ArrayList();
    if(campo != null)
      campos.add(campo);
  }
  
  public void addImg(Img img) {
    if(imgs == null)
      imgs = new ArrayList();
    if(img != null)
      imgs.add(img);
  }
  
  public void addImgLt(Img img) {
    if(imgsLt == null)
      imgsLt = new ArrayList();
    if(imgsLt != null)
      imgsLt.add(img);
  }
  
  public void addImg(Img ... img) {
    List<Img> imagenes = new ArrayList();
    for (int i = 0; i < imagenes.size(); i++)  {
      imagenes.add(imagenes.get(i));
    }
    if(imgs == null)
      imgs = new ArrayList();
    if(img != null)
      imgs.add(imagenes);
  }
  
  protected void ImprimeFunJavaScript(JspWriter out) {
    try {
      out.println("<script languaje='javascript' type='text/javascript'>");
      out.println("  function fullscreen(url, w, h) {");
      out.println("    var l = (screen.availWidth-500)/2");
      ;
      out.println("    var t = (screen.availHeight-400)/2;");
      out.println("    var features = 'width='+ w+ ', height='+ h+ ', left='+l+', top='+t+', screenX=0, screenY=0';");
      out.println("    features+= 'location=0, scrollbars=1, resizable=0, menubar=0, toolbar=0, status=0';");
      out.println("    window.open(url, '', features); }");
      
      out.println("    var ant;");
      out.println("  function over(tis) {");
      out.println("    ant = tis.className;");
      out.println("    tis.className = 'resGrisOscuroOver';");
      out.println("  }");
      
      out.println("  function out(tis) {");
      out.println("    tis.className = ant;");
      out.println("  }");
      out.println("</script>");
    } catch (Exception e) {
      System.out.println(e.getMessage() != null ? e.getMessage() : ""); 
      
    }
  }
  
  protected int getColspan() {
    return (tipo != null ? 1 : 0) + 1 + (campos.size()) + (imgs != null ? imgs.size() : 0);
  }
  
  private void armaImagenesTitulo(JspWriter out, List<Img> imgs) throws IOException {
    String onClick=null;
    if(imgsLt!=null) {
      out.println("<table>  \n");
      out.println("<tr>  \n");
      for(Img img:imgs) {
        onClick = img.getOnClick() != null && !img.getOnClick().equals("") ? " onclick='".concat(img.getOnClick()).concat("'"):"";
        out.println("<td>  \n");
        out.println("  <img src='" + ruta + img.getRutaImagen() + "'  title='" + img.getAlt() +"' "+ onClick + " border='0' >");
        out.println("</td>  \n");
      }
      out.println("</tr>  \n");
      out.println("</table>  \n");
    }        
  }
  
  protected void ImprimeEncabezado(JspWriter out) throws IOException {
    
    if(tituloTabla != null || imgsLt!=null) {
      out.println("<tr>  \n");out.println("<td  class='"+ AZUL_CLA +"' colspan='"+(getColspan())+"'>  \n");
      out.println("<table>  \n");
      out.println("<tr>  \n");
      out.println("<td>  \n");
      armaImagenesTitulo(out,imgsLt);
      out.println("</td>  \n");
      out.println("<td  class='blanco'><b>"+ getTituloTabla() +"</b></td>");
      out.println("<td>  \n");
      out.println("</td>  \n");
      out.println("</tr>  \n");
      out.println("</table>  \n");out.println("</td>  \n");
      out.println("</tr> \n ");
    }
  }
  
  protected void imprimeSel(JspWriter out) throws IOException {
    out.println("<td width='5%' class='"+ AZUL_OBS +"'>"+ (tipo instanceof SelTipoCheck ?  "<input type='checkbox' value='' onclick=\"selAll(this,'"+tipo.getName()+"')\">":"Sel.")+"</td>");
  }
  
  protected void ImprimeLosEncabezados2(JspWriter out) throws SQLException, 
                                                                   Exception {
    try { 
      
      //out.println("");
      out.println("<tr> \n");
      if(tipo != null)
        imprimeSel(out);
      out.println("<td width='5%' class='"+ AZUL_OBS +"'>No.</td>");

      for(Campo campo : campos) {
        out.println("<td class='"+ AZUL_OBS +"' width='" + campo.getTamanoCol() + "'>" + 
        (campo.isOrdenado()? "<a class='blanco' onClick='ordena"+noTabla+"("+regIni+",\""+campo.getCampo()+"\")'>"+ Cadena.letraCapital(campo.getNombreCol())+"</a>": Cadena.letraCapital(campo.getNombreCol()))
        + "</td> \n");
      }
      if(imgs != null && imgs.size() != 0) {
        out.println("<td class='"+ AZUL_OBS +"' width='10%'><div align='center'>Opciones</div></td> \n");
      }
      out.println("</tr> \n");
      out.println("</thead>");

    } catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo hpagseleccionada.ImprimeLosEncabezados2");
      throw e;
    }
  }
  
  protected String ImprimeFormatoFila(int numReg) {
    String regresa = null;
    registro++;
    regresa = registro % 2 != 0 ? ">" : " class='resGrisClaro'>";
     // if (numReg % 2 == 0) 
     //   regresa = " class='resGrisClaro'>";
    return regresa;
  }
  
  private boolean validaCeldaCheck() throws SQLException {
    boolean regresa=false;
    if(tipo.getCampoCondicion()!=null && tipo.getValorCondicion()!=null) {
      if(crsTabla.getStr(tipo.getCampoCondicion()).equals(tipo.getValorCondicion()))
        regresa = true;        
    } else regresa = true;
    return regresa;
  }
  
  protected void ImprimirCeldaCheck(String concatClaves, 
                                   JspWriter out) {
    try {
      out.println("<td width='5%' align='center'>");
      if(validaCeldaCheck()) {
        String vl = "<input type='" + tipo.getTipo() + "' value='" + (tipo.getValue() != null ? tipo.getValue() : concatClaves) + 
              "' name='" + tipo.getName() + "' id='" + tipo.getName() + "'>";
        out.println(vl);
      }
      out.println("</td>");
      
    } catch (Exception e) {
      System.out.println(e.getMessage() != null ? e.getMessage() : ""); 
    }
  }
  
  protected String ObtenerClaves2(String Claves) {
    String concatClaves = "";
    String Token = "";
    try {
      StringTokenizer stClaves = new StringTokenizer(Claves, ",");
      while (stClaves.hasMoreTokens()) {
        Token = stClaves.nextToken();
        if(crsTabla.getString(Token)==null)
          throw new Exception("No se encontro una de las llaves dentro de la consulta");
        concatClaves += crsTabla.getString(Token) + ",";
      } // while
      return (concatClaves);
    } catch (Exception e) {
      System.err.println(e.getMessage() == null ? "" : e.getMessage());
      return ("");
    } // try
  }
  
  private String Formateado(String Celda, String Formato) {
    if(Formato != null && Celda != null) {
      if (Formato.equals("%,.2f")) {
        boolean isDouble = false;
        try {
          Double.valueOf(Celda);
          isDouble = true;
        } catch (Exception e) {
          isDouble = false;
        }
        if (isDouble) {
          double CeldaD = Double.valueOf(Celda).doubleValue();
          Celda = String.format(Formato, CeldaD);
        } 
      } 
    }
    return Celda == null ? "" : Celda;
  }
  
  private String getParametros(CampoSel campSel) throws SQLException {
    String key = null;
    if(campSel.getKey() != null && !campSel.getKey().equals("")) {
      key = "?key=";
      String[] keys = campSel.getKey().split(",");
      for(String k : keys) {
        key = key.concat(crsTabla.getString(k)).concat(",");
      }
      key = key.substring(0,key.length()-1);
    }
    if(campSel.getParametros() != null && !campSel.getParametros().equals("")) {
      key = key != null ? key.concat("&").concat(campSel.getParametros()) : "?".concat(campSel.getParametros());
    }
    return key;
  }
  
  protected void ImprimeCeldas(JspWriter out) {
    try {
      CampoSel campSel = null;
      for(Campo camp : campos) {
        if(camp instanceof CampoSel) {
          campSel = (CampoSel)camp;
          campSel.getRuta();
          out.println("<td align=\"" + campSel.getAlineacionStr() + "\" width='" + campSel.getTamanoCol() + "'><a href='"+ campSel.getRuta() + getParametros(campSel) + "'>" + 
                      Formateado(crsTabla.getString(campSel.getCampo()), 
                                 campSel.getFormato()) + "</a> </td> ");
        }
        else {
          out.println("<td align=\"" + camp.getAlineacionStr() + "\" width='" + camp.getTamanoCol() + "'>" + 
                      Formateado(crsTabla.getString(camp.getCampo()), 
                                 camp.getFormato()) + "</td>");
        }
      }
      
    } catch (Exception e) {
      System.out.println("Error: " + e);
    }
  }
  
  private void ImprimeImagen(JspWriter out,Img img,String ruta,String concatClaves) throws SQLException,Exception {
    String target = null;
    String onClick = null;
    if(validarImg(img,false)) {
      target = img.isNuevaVentana()?"target='_blank'":"";
      onClick = img.getOnClick() != null && !img.getOnClick().equals("") ? " onclick=\"".concat(img.getOnClick()).concat("\""):"";
      //out.println(" <a href='" + img.getPaginaDestino() + "?" + img.getAccion() + "&clave=" + concatClaves + "&" + img.getParam() + "' " + target + " " + onClick + "> <img src='" + ruta + 
       if(img.getPaginaDestino()!=null && !img.getPaginaDestino().equals("")) {
         out.println(" <a href='" + img.getPaginaDestino() + "?clave=" + concatClaves + "&" + img.getParam() + "' " + target + " " + onClick + "> <img src='" + ruta + 
                     img.getRutaImagen() + "'  title='" + img.getAlt() + "' border='0' ></a>");
       } else {
         out.println(" <img src='" + ruta + 
                     img.getRutaImagen() + "'  title='" + img.getAlt() + "' border='0' " + onClick + ">");
       }
    }
  }
  
  private boolean validarImg(Img img,boolean variasImagenes) throws SQLException, Exception {
    boolean regresa = true;
    if(variasImagenes || (img.getCampoCondicion()!=null && img.getValorCondicion()!=null)) {
      if(img.getCampoCondicion()==null && img.getValorCondicion()==null)
        regresa = false;
      else
        if(!crsTabla.getString(img.getCampoCondicion()).equals(img.getValorCondicion()))
          regresa = false;
    }
    //System.out.println(crsTabla.getString(img.getCampoCondicion())+" - "+img.getValorCondicion());
    return regresa;
  }
  
  private void ImprimeImagenes(JspWriter out,List<Img> imgs,String ruta,String concatClaves)  throws SQLException, 
                                                         Exception{
    String target = null;
    String onClick = null;
    for(Img img : imgs) {
      if(validarImg(img,true)) {
        target = img.isNuevaVentana()?"target='_blank'":"";
        onClick = img.getOnClick() != null && !img.getOnClick().equals("") ? " onclick='".concat(img.getOnClick()).concat("'"):"";
        if(img.getPaginaDestino()!=null && !img.getPaginaDestino().equals("")) {
          out.println(" <a href='" + img.getPaginaDestino() + "?" + img.getAccion() + "&clave=" + concatClaves + "&" + img.getParam() + "' " + target + " " + onClick + "> <img src='" + ruta + 
                       img.getRutaImagen() + "'  title='" + img.getAlt() + "' border='0' ></a>");
        } else
          out.println("<img src='" + ruta + 
                       img.getRutaImagen() + "'  title='" + img.getAlt() + "' border='0' >");
      }
    }
  }
  
  protected void ImprimeOpciones(JspWriter out,String ruta,String concatClaves)  throws SQLException, 
                                                         Exception {
    if(imgs != null && imgs.size() != 0) {
      out.println("<td align='right'>");
      out.println("<table> \n");
      out.println("<tr> \n");
      
      for(Object img : imgs) {
        out.println("<td height='16px' width='16px'> \n");
        if(img instanceof Img) {
          ImprimeImagen(out,(Img)img,ruta,concatClaves);
        } else {
          ImprimeImagenes(out,(List<Img>)img,ruta,concatClaves);
        }
        out.println("</td> \n");
        
      }
      out.println("</tr>");
      out.println("</table> \n");
      out.println("</td>");
    }
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
      if (registroActual > 0) {
        crsTabla.absolute(registroActual);
      }
      numReg = registroActual;
      while (crsTabla.next() && numReg < hasta) {
        numReg++;
        out.print("<tr onmouseover='over(this)' onmouseout='out(this)' id='trPagResultado'".concat(ImprimeFormatoFila(numReg)).concat(" \n"));
        
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
  
  public int funcion_de_paso2(String CampoClave, 
                              int registroActual, 
                              int registrosDesplegar, int total_reg, 
                              JspWriter out, 
                              String ruta) throws SQLException, Exception {
    try {
      int numCol = 0;
      
      String Claves = CampoClave;
      
      numCol = 
          ImprimeTodosLasCeldas2(Claves, registroActual, 
                                 registrosDesplegar, total_reg, out, 
                                 ruta);
      
      
      return numCol;
    } catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo hpagseleccionada.ImprimeLosEncabezados2" + e.getMessage() == null ? "" : e.getMessage());
      throw e;
      //       return 0;
    }
  }
  
  protected void imprimeNoPagina(JspWriter out,int llamaMis, int hastaaqui, int x, String color) throws IOException {
    out.print("&nbsp;<a onclick=\""+(isOrden()?"ordena"+noTabla+"("+llamaMis+",'"+orden+"')":"llamamisma"+noTabla+"(" + llamaMis + ")")+"\" title='" + (llamaMis + 1) + 
              " - " + hastaaqui + 
              "' style='cursor:hand'><font color='"+color+"' id='pagColorPagina'>[" + (x + 1) + 
              "]</font></a>&nbsp;");
  }
  
  protected void imprimePie(JspWriter out, int total_reg, int regActual, int PregActual) {
    try  {
      int residuo = total_reg % noRegaMostrar;
      int llamaMis = 0;
      int pagAct = (PregActual/noRegaMostrar)+1;
      
      int regFin = ((pagAct -1) * noRegaMostrar) + noRegaMostrar; 
      regFin = regFin > total_reg ? total_reg : regFin;
      if (total_reg > 0) {
        //---- LAS SIG. LINEAS SON LOS BOTONES DE ANTERIOR Y SIGUIENTE
        
        out.print("<tr> \n");
        out.print("<td width='100%' colspan='"+ getColspan() +"' class='"+ AZUL_OBS +"' > \n");
        
        out.print("<table width='100%'> \n");
        out.print("<tr> \n");
          out.print("<td align='left' width='33%' class='blanco'><b> <font id='pagPregActual'>" + 
                    (PregActual + 1) + "</font> - <font id='pagRegFin'>" + regFin + "</font></b> registros de <b>" + 
                    total_reg + "</b></td>");
         
          out.print("<td align='center' width='33%' nowrap='false'><b>");
          int hastaaqui = 0;
          int suma = 0;
          if (residuo == 0) {
          } else {
            suma = 1;
          }
          for (int x = 0; x < ((total_reg / noRegaMostrar) + suma); x++) {
            hastaaqui = (((x * noRegaMostrar)) + noRegaMostrar);
            if (hastaaqui > total_reg) {
              hastaaqui = total_reg;
            }
            llamaMis = x == 0 ? x : x * noRegaMostrar;
            if (x == PregActual/noRegaMostrar) {
              //              out.print("<a href='?reg_actual="+x+"' title='"+(x+1)+" - "+hastaaqui+"'><font color='white'>["+(x+1)+"]</font></a>");
               imprimeNoPagina(out,llamaMis, hastaaqui, x, "#19FF05");
            } else {
              //              out.print("<a href='?reg_actual="+((x*noRegaMostrar))+"' title='"+((x*noRegaMostrar)+1)+" - "+hastaaqui+"'><font color='white'>["+(x+1)+"]</font></a>");
               imprimeNoPagina(out,llamaMis, hastaaqui, x, "white");
            }
            if(x % 20 == 0 && x != 0)
              out.print("<br>");
          } //for
           int pagTot = (total_reg / noRegaMostrar) + suma;
           out.print("<input name='reg_actual' type='hidden' id='reg_actual' value='" + 
                     regActual + "'>");
          out.print("</b></td>");
          out.print("<td align='right' width='33%' class='blanco'>P&aacute;gina <b><font id='pagPagAct'>"+pagAct+"</font></b> de <b>"+pagTot+"</b></td>");
          
          
            out.print("</tr> \n");
            out.print("</table> \n");
            
            out.print("</td> \n");
            out.print("</tr> \n");
          } //if
       
        

      else {
        
        out.print("<tr \n>");
        out.print("<td width='100%' colspan='"+ getColspan() +"'  align='center' class=" + AZUL_CLA + " > 0 registros </td>");
        out.print("</tr>");
        
      }
     
    } catch (Exception ex)  {
      ex.printStackTrace();
    } 
    
    
  }
  
  protected boolean isOrden() {
    return orden!=null && !orden.equals("") && !orden.equals("null");
  }
  public int seleccionarPagina(SentenciasCRS crsTabla, 
                               JspWriter out, int noRegaMostrar, 
                               int PregActual, String ruta, 
                               String CampoClave, SelTipo tipo) throws SQLException, 
                                                          Exception {
    return seleccionarPagina(crsTabla, out, noRegaMostrar, PregActual, ruta, CampoClave, tipo, 100, null);
  }
  
  public int seleccionarPagina(SentenciasCRS crsTabla, 
                               JspWriter out, int noRegaMostrar, 
                               int PregActual, String ruta, 
                               String CampoClave, SelTipo tipo, int ancho) throws SQLException, 
                                                          Exception {
    return seleccionarPagina(crsTabla, out, noRegaMostrar, PregActual, ruta, CampoClave, tipo, ancho, null);
  }
  
  public int seleccionarPagina(SentenciasCRS crsTabla, 
                               JspWriter out, int noRegaMostrar, 
                               int PregActual, String ruta, 
                               String CampoClave, SelTipo tipo, String tituloTabla) throws SQLException, 
                                                          Exception {
    return seleccionarPagina(crsTabla, out, noRegaMostrar, PregActual, ruta, CampoClave, tipo, 100, tituloTabla);
  }
  
  public int seleccionarPagina(SentenciasCRS crsTabla, 
                               JspWriter out, int noRegaMostrar, 
                               int PregActual, String ruta, 
                               String CampoClave, SelTipo tipo, int ancho, String tituloTabla) throws SQLException, Exception {
  return seleccionarPagina(crsTabla, out, noRegaMostrar, PregActual, ruta, CampoClave, tipo, ancho, tituloTabla, null);                                                                                                                
                                                          }
  
  /*
  * funcion principal para la paginacion.
  * parametros:
  * crsTabla : cachedRowset a desplegar
  * noRegaMostrar : no. de registros que se mostraran en pantalla en cada pagina
  * PregActual : no. de registro actual
  * total_reg: total de registros
  * Pagina : pagina la cual sera llamada
  * CampoClave : es la clave que se va a enviar en la sig. pagina
  */
  
   protected int getTotalReg(int total_reg) {
     return crsTabla.size();
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
      crsTabla.beforeFirst();
      ImprimeFunJavaScript(out);
      out.println("<table class='resultado' id='tbPagResultado"+noTabla+"' border='0' width='"+ancho+"%' cellpadding='2' cellspacing='0' align='center'> \n");
      
      ImprimeEncabezado(out);
      imprimePie(out, total_reg, regActual, PregActual);
      ImprimeLosEncabezados2(out);
      regActual =  funcion_de_paso2(CampoClave,  PregActual,
                                    noRegaMostrar, total_reg, out, ruta);
      
      imprimePie(out, total_reg, regActual, PregActual );
      out.println("</table> \n");
    } catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo hpagseleccionada.ImprimeLosEncabezados2");
      e.printStackTrace();
      throw e;
    }
    return regActual;
  }


  public void setTituloTabla(String tituloTabla) {
    this.tituloTabla = tituloTabla;
  }

  public String getTituloTabla() {
    return tituloTabla;
  }

  public void setNoTabla(String noTabla) {
    this.noTabla = noTabla;
  }

  public String getNoTabla() {
    return noTabla;
  }
}
