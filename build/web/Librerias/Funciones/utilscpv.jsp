<%@page import="sun.jdbc.rowset.CachedRowSet" %>
<%@page import="sia.db.sql.SentenciasCRS"%>
<%@page import="java.util.*" %>
<%@page import="java.sql.*"%>
<%@page import="java.text.*"%>


<!--
Utilerias realizadas para el proyecto CONPROVE
Noviembre de 2003
-->

<%!
public void CRSComboBoxMedPap(CachedRowSet crsTabla,JspWriter out,String Columna1,String MedHor,String MedVer, String Columna2,String Valor) {
    try {
      crsTabla.beforeFirst();
      while (crsTabla.next()) {
        if(Valor.compareTo(crsTabla.getString(Columna1).trim()) == 0)
           out.println("<option value="+crsTabla.getString(Columna1)+"H"+crsTabla.getString(MedHor)+"V"+crsTabla.getString(MedVer)+" selected>"+ crsTabla.getString(Columna2)+"</option>");
        else
           out.println("<option value="+crsTabla.getString(Columna1)+"H"+crsTabla.getString(MedHor)+"V"+crsTabla.getString(MedVer)+">"+ crsTabla.getString(Columna2)+"</option>");
      }
    }
    catch (Exception e) {
      System.out.println("Error en CRSComboBoxMedPap: "+ e.getMessage());
    }
  };
  
  public void CRSComboBox(SentenciasCRS crsTabla,JspWriter out,String Columna1,String Columna2,String Valor) {
    try {
      crsTabla.beforeFirst();
      while (crsTabla.next()) {
        //System.out.println("campo - "+crsTabla.getString(Columna1)+" - valor - "+Valor+" *** "+Valor.equals(crsTabla.getString(Columna1).trim()));
        if(Valor!=null && Valor.equals(crsTabla.getString(Columna1).trim())) {
           out.println("<option value="+crsTabla.getString(Columna1)+" selected>"+  getColumna2(crsTabla,Columna2.split(","))+"</option>");
           
        }else{
           out.println("<option value="+crsTabla.getString(Columna1)+">"+ getColumna2(crsTabla,Columna2.split(","))+"</option>");}
      }
    }
    catch (Exception e) {
      System.out.println("Error en CRSComboBox: "+ e.getMessage());
    }
  };
  
/// Metodo que pinta un componete select, seleccionando multiples valores
/// Valor es cadena separada por comas ejeplo "1,2,3"
  public void CRSMultipleComboBox(SentenciasCRS crsTabla,JspWriter out,String Columna1,String Columna2,String Valor) {
    try {
      crsTabla.beforeFirst();
      Valor = ","+Valor+",";
      while (crsTabla.next()) {
        if(Valor!=null && Valor.indexOf(","+crsTabla.getString(Columna1)+",")>-1) {
           out.println("<option value="+crsTabla.getString(Columna1)+" selected>"+  getColumna2(crsTabla,Columna2.split(","))+"</option>");
        }else{
           out.println("<option value="+crsTabla.getString(Columna1)+">"+ getColumna2(crsTabla,Columna2.split(","))+"</option>");}
      }
    }
    catch (Exception e) {
      System.out.println("Error en CRSComboBox: "+ e.getMessage());
    }
  };
  
  public void CRSComboBox(SentenciasCRS crsTabla,JspWriter out,String Columna1,String Columna2,String Valor, int maxResultados) {
    try {
      crsTabla.beforeFirst();
      int cont=0;
      while (crsTabla.next() && cont++ < maxResultados) {
        if(Valor!=null && Valor.compareTo(crsTabla.getString(Columna1).trim()) == 0)
           out.println("<option value="+crsTabla.getString(Columna1)+" selected>"+  getColumna2(crsTabla,Columna2.split(","))+"</option>");
        else
           out.println("<option value="+crsTabla.getString(Columna1)+">"+ getColumna2(crsTabla,Columna2.split(","))+"</option>");
      }
    }
    catch (Exception e) {
      System.out.println("Error en CRSComboBox: "+ e.getMessage());
    }
  };
  
  private String getValores(SentenciasCRS valores, String campo) throws Exception {
    
    String regresa = null;
    if(valores!=null && valores.size()!=0 && campo!=null && !campo.equals("")) {
      StringBuffer conca = new StringBuffer();
      conca.append("|");
      valores.beforeFirst();
      while(valores.next()) {
        conca.append(valores.getString(campo)).append("|");
      }
      if(conca.toString().length()>1)
        regresa = conca.toString();
    }
    return regresa;
  }
  
  private String getColumna2(SentenciasCRS crsTabla, String[] campos) throws Exception {
    StringBuffer regresa = new StringBuffer();
    for(String campo:campos) {
      regresa.append(crsTabla.getString(campo)==null?"":crsTabla.getString(campo)).append("&nbsp;");
      //System.out.println(crsTabla.getString(campo));
    }
    return regresa.toString();
  }
  
  public void CRSComboBoxMultiple(SentenciasCRS crsTabla,JspWriter out,String Columna1,String Columna2, String valoresStr) {
    
    if(crsTabla!=null){
      try {
        
        crsTabla.beforeFirst();
        String[] Columna21 = Columna2.split(",");
        while (crsTabla.next()) {
          if(valoresStr!=null && (valoresStr.indexOf("|".concat(crsTabla.getString(Columna1).trim()).concat("|")) != -1 || valoresStr.equals(crsTabla.getString(Columna1).trim())) )
             out.println("<option value="+crsTabla.getString(Columna1)+" selected>"+ getColumna2(crsTabla,Columna21)+"</option>");
          else
             out.println("<option value="+crsTabla.getString(Columna1)+">"+ getColumna2(crsTabla,Columna21)+"</option>");
        }
      }
      catch (Exception e) {
        System.out.println("Error en CRSComboBox: "+ e.getMessage());
      }
    }
  };
  
  public void CRSComboBoxMultiple(SentenciasCRS crsTabla,JspWriter out,String Columna1,String Columna2,SentenciasCRS valores, String campoValores) {
    String valoresStr = null;
    
      try {
        valoresStr = getValores(valores, campoValores);
        CRSComboBoxMultiple(crsTabla,out,Columna1,Columna2,campoValores);
      }
      catch (Exception e) {
        System.out.println("Error en CRSComboBox: "+ e.getMessage());
      }
    
  };
  
  

public void CRSComboBox(CachedRowSet crsTabla,JspWriter out,String Columna1,String Columna2,String Valor) {
    try {
      crsTabla.beforeFirst();
      while (crsTabla.next()) {
        if(Valor!=null && Valor.compareTo(crsTabla.getString(Columna1).trim()) == 0)
           out.println("<option value="+crsTabla.getString(Columna1)+" selected>"+ crsTabla.getString(Columna2)+"</option>");
        else
           out.println("<option value="+crsTabla.getString(Columna1)+">"+ crsTabla.getString(Columna2)+"</option>");
      }
    }
    catch (Exception e) {
      System.out.println("Error en CRSComboBox: "+ e.getMessage());
    }
  };


public void CRSComboBoxSinAlgunos(CachedRowSet crsTabla,JspWriter out,String Columna1,String Columna2,String Valor,Vector vENoVan) {
    try {
      crsTabla.beforeFirst();
      while (crsTabla.next()) {
       if (vENoVan.indexOf(crsTabla.getString(Columna1))==-1){
        if(Valor.compareTo(crsTabla.getString(Columna1).trim()) == 0)
           out.println("<option value="+crsTabla.getString(Columna1)+" selected>"+ crsTabla.getString(Columna2)+"</option>");
        else
           out.println("<option value="+crsTabla.getString(Columna1)+">"+ crsTabla.getString(Columna2)+"</option>");
       }
      }
    }
    catch (Exception e) {
      System.out.println("Error en CRSComboBoxSinAlgunos: "+ e.getMessage());
    }
  };

public void CRSComboBoxIndentado(CachedRowSet crsTabla,JspWriter out,String Columna1,String Columna2,String Valor, String vNivel) {
// Construye las opciones de un componente ComboBox de HTML a partir de un CachedRowSet
// En donde el campo que se graba es diferente al que despliega.
//
// Paramentros
//   crsTabla    : Indica el CacheRowSet a Utilizar
//   out         : Indica la pagina desde la cual es llamado
//   Columna1    : Contiene el nombre del campo a partir del cual se llenara el componente en su propiedad value.
//   Columna2    : Contiene el nombre del campo que se desplegara en el ComboBox.
//   Valor       : Contiene el valor que debe estar seleccionado inicialmente en el ComboBox.
//   vNivel      : Indica el nombre del campop que contiene el nivel para representar la indentacion.
  String indenta;

    try {
      crsTabla.beforeFirst();
      while (crsTabla.next()) {
        indenta = " ";
        for (int i=0; i < Integer.parseInt(crsTabla.getString(vNivel)); i++){
           indenta += "&nbsp;&nbsp;&nbsp;";
        }
        if(Valor.compareTo(crsTabla.getString(Columna1).trim()) == 0)
           out.println("<option value="+crsTabla.getString(Columna1)+" selected>"+ indenta + crsTabla.getString(Columna2)+"</option>");
        else
           out.println("<option value="+crsTabla.getString(Columna1)+">"+ indenta + crsTabla.getString(Columna2)+"</option>");
      }
    }
    catch (Exception e) {
      System.out.println("Error en CRSComboBoxIndentado: "+ e.getMessage());
    }
};

public void CRSRadioButton(SentenciasCRS crsTabla,JspWriter out,String nombreObj, String Columna1,String Columna2,String Valor){
// Construye las opciones de un componente RadioButton de HTML a partir de un CachedRowSet
// En donde el campo que se graba es diferente al que despliega.
//
// Paramentros
//   crsTabla  : Indica el CacheRowSet a Utilizar
//   out       : Indica la pagina desde la cual es llamado
//   nombreObj : Contiene el nombre del Objeto RadioButton.
//   Columna1  : Contiene el nombre del campo a partir del cual se llenara el componente en su propiedad value.
//   Columna2  : Contiene el nombre del campo que se desplegara en el ComboBox.
//   Valor     : Contiene el valor que debe estar seleccionado inicialmente en el ComboBox.
    try {
      crsTabla.beforeFirst();
      while (crsTabla.next()) {
        if(Valor.compareTo(crsTabla.getString(Columna1).trim()) == 0)
           out.println("<INPUT TYPE='radio' NAME='" + nombreObj + "' VALUE='"+crsTabla.getString(Columna1)+"' checked>"+ crsTabla.getString(Columna2)+"<br>");
        else
           out.println("<INPUT TYPE='radio' NAME='" + nombreObj + "' VALUE='"+crsTabla.getString(Columna1)+"'>"+ crsTabla.getString(Columna2)+"<br>");
      }
    }
    catch (Exception e) {
      System.out.println("Error en CRSRadioButton: "+ e.getMessage());
    }
};

public void CRSComboBoxSeleccionado(CachedRowSet crsTabla,JspWriter out,String Columna1,String Columna2,String Valor) {
    try {
      crsTabla.beforeFirst();
      while (crsTabla.next()) {
        if(Valor.compareTo(crsTabla.getString(Columna1).trim()) == 0)
          out.println("<option value="+crsTabla.getString(Columna1)+" selected>"+ crsTabla.getString(Columna2)+"</option>");
        else
           out.println("<option value="+crsTabla.getString(Columna1)+">"+ crsTabla.getString(Columna2)+"</option>");
      }
    }
    catch (Exception e) {
      System.out.println("Error en CRSComboBoxSeleccionado: "+ e.getMessage());
    }
  };


public void CRSComboBoxUnSeleccionado(CachedRowSet crsTabla,JspWriter out,String Columna1,String Columna2,String Valor) {
    try {
      crsTabla.beforeFirst();
      while (crsTabla.next()) {
        if(Valor.compareTo(crsTabla.getString(Columna1).trim()) == 0)
          out.println("<option value="+crsTabla.getString(Columna1)+" selected>"+ crsTabla.getString(Columna2)+"</option>");
      }
    }
    catch (Exception e) {
      System.out.println("Error en CRSComboBoxUnSeleccionado: "+ e.getMessage());
    }
};


public void CRSComboBoxAreaGen(CachedRowSet crsTabla,JspWriter out,String Columna1,String Columna2,String Valor, String lstAreaGen, String vNivel) {
// Funcion agregada por Martha y Chava 15-Jun/2004
    try {
      String indenta ="";
      crsTabla.beforeFirst();

      while (crsTabla.next()) {
        indenta ="";
        for (int i=0; i < Integer.parseInt(crsTabla.getString(vNivel)); i++){
           indenta += "&nbsp;&nbsp;&nbsp;";
        }
        if (crsTabla.getString(vNivel).equals("1")){
            out.println("<option value =''></option> ");
        }
        if (lstAreaGen.indexOf(crsTabla.getString(Columna1)) > -1) {

          if(Valor.compareTo(crsTabla.getString(Columna1).trim()) == 0){
            out.println("<option value="+crsTabla.getString(Columna1)+" selected>"+ indenta+crsTabla.getString(Columna2)+"</option>");
          }
          else
            out.println("<option value="+crsTabla.getString(Columna1)+">"+ indenta+crsTabla.getString(Columna2)+"</option>");
        }
      }
    }
    catch (Exception e) {
      System.out.println("Error en CRSComboBoxAreaGen: "+ e.getMessage());
    }
  };



public void CRSImprimeBodyTablaLink(CachedRowSet rsTabla, JspWriter out, String pageLink, String paramName,
    String fieldLink, String fieldShow){
// Funcion agregada por Martha y Chava 21-Jun/2004

   ResultSetMetaData rsmd;
   StringBuffer cdgHTML = new StringBuffer();
   int i = 0;
   try{
      rsmd = rsTabla.getMetaData();
      while (rsTabla.next()) {
        if ((i % 2) == 0)
           cdgHTML.append("<tr class=''>");
        else
           cdgHTML.append("<tr class='resGrisClaro'>");
        int j = 1;
        int bLink = 0;
        while (j <= rsmd.getColumnCount()){
           if ((rsmd.getColumnName(j).compareTo(fieldLink) == 0) || (rsmd.getColumnName(j).compareTo(fieldShow) == 0)){
              if (bLink == 0){
                 cdgHTML.append("<td class=general align='LEFT'>");
                 cdgHTML.append("<a href=");
                 cdgHTML.append(pageLink);
                 cdgHTML.append("?");
                 cdgHTML.append(paramName);
                 cdgHTML.append("=");
                 cdgHTML.append(rsTabla.getString(fieldLink));
                 cdgHTML.append(">");
                 cdgHTML.append((rsTabla.getString(fieldShow)!= null)? rsTabla.getString(fieldShow): "-" );
                 cdgHTML.append("</a>");
                 cdgHTML.append("</td>");
                 bLink = 1;
              }
           }
           else {
              cdgHTML.append((rsTabla.getString(j) != null?"<td class=general align='LEFT'>" + rsTabla.getString(j) +
              "</td>":"<td class=general align='CENTER'>-</td>"));
           }
           j++;

        }//while Columnas
        i++;
      }//while crsTabla
      out.println(cdgHTML.toString());
   }
   catch (Exception e){
      System.out.println("Error en CRSImprimeBodyTablaLink: "+ e.getMessage());
   }
};


public void CRSImprimeBodyTablaLinkObject(CachedRowSet rsTabla, JspWriter out, String pageLink, String paramName,
    String fieldLink, String fieldShow, String object, String objectName){
/*
Descripcion:
    Se encarga de Imprimir el cuerpo de una Tabla a la cual previamente se le definieron los
    encabezados y el formato.
    Se incluira un Objeto de captura el cual puede ser un CheckBox o Radio.
    Parametros:
      rsTabla    : Contiene los datos a mostrar en la tabla

      out        : Es el area en la cual se imprimira el resultado de la construccion del codigo HTML

      pageLink   : Nombre de la pagina que se ejecutara al dar clic sobre la liga
      paramName  : Nombre del Parametro a ser enviado a la pagina definida por "pageLink"
      fieldLink  : Nombre del campo contenido en "rsTabla" del cual se obtendran los valores que
                   se asignaran al parametro "paramName" en la ejecucion de la pagina dada por
                   "pageLink"
      fieldShow  : Nombre del campo contenido en "rsTabla" del cual se obtendran los valores que
                   se asignaran para mostrar en la liga de ejecucion de la pagina dada por
                   "pageLink"
      object     : Indica el tipo de objeto que se mostrara a la izquierda de los datos contenidos en
                   "rsTabla" el cual puede ser CHECKBOX o RADIO
      objectName : Nombre que se asignara en el codigo HTML al objeto indicado por el parametro "object".
                   Para poder hacer referencia a el al enviar informacion.
Nota:
  Cabe mencionar que la propiedad value del objeto "object" se asignara con el valor que se obtenga de "fieldLink"

*/
   ResultSetMetaData rsmd;
   StringBuffer cdgHTML = new StringBuffer();
   int i = 0;
   try{
      rsmd = rsTabla.getMetaData();
      while (rsTabla.next()) {
        if ((i % 2) == 0)
           cdgHTML.append("<tr class=''>");
        else
           cdgHTML.append("<tr class='resGrisClaro'>");
        cdgHTML.append("<td class=general align='CENTER'>");
        cdgHTML.append("<INPUT TYPE='");
        cdgHTML.append(object);
        cdgHTML.append("' NAME='");
        cdgHTML.append(objectName);
        cdgHTML.append("' VALUE=");
        cdgHTML.append(rsTabla.getString(fieldLink));
        cdgHTML.append(">");
        cdgHTML.append("</td>");
        int j = 1;
        int bLink = 0;
        while (j <= rsmd.getColumnCount()){
           if ((rsmd.getColumnName(j).compareTo(fieldLink) == 0) || (rsmd.getColumnName(j).compareTo(fieldShow) == 0)){
              if (bLink == 0){
                 cdgHTML.append("<td class=general align='LEFT'>");
                 cdgHTML.append("<a href=");
                 cdgHTML.append(pageLink);
                 cdgHTML.append("?");
                 cdgHTML.append(paramName);
                 cdgHTML.append("=");
                 cdgHTML.append(rsTabla.getString(fieldLink));
                 cdgHTML.append(">");
                 cdgHTML.append((rsTabla.getString(fieldShow)!= null)? rsTabla.getString(fieldShow): "-" );
                 cdgHTML.append("</a>");
                 cdgHTML.append("</td>");
                 bLink = 1;
              }
           }
           else {
              cdgHTML.append((rsTabla.getString(j) != null?"<td class=general align='LEFT'>" + rsTabla.getString(j) +
              "</td>":"<td class=general align='CENTER'>-</td>"));
           }
           j++;

        }//while Columnas
        i++;
      }//while crsTabla
      out.println(cdgHTML.toString());
   }
   catch (Exception e){
      System.out.println("Error en CRSImprimeBodyTablaLinkObject: "+ e.getMessage());
   }
};

public void CRSCheckBox(CachedRowSet crsTabla,JspWriter out,String nombreObj, String nColumnas, String Columna1,String Columna2,String Valores){
// Construye las opciones de un componente CheckBox de HTML a partir de un CachedRowSet
// En donde el campo que se graba es diferente al que despliega. Distribuyendo el resultado en nColumnas
//
// Paramentros
//   crsTabla  : Indica el CacheRowSet a Utilizar
//   out       : Indica la pagina desde la cual es llamado
//   nombreObj : Contiene el nombre del Objeto RadioButton.
//   nColumnas : Indica cuantas columnas se deben generar para mostrar los resultados.
//   Columna1  : Contiene el nombre del campo a partir del cual se llenara el componente en su propiedad value.
//   Columna2  : Contiene el nombre del campo que se desplegara en el CheckBox.
//   Valores     : Contiene la lista separada por comas de los valores seleccionados inicialmente en el CheckBox.
    StringBuffer cdgHTML = new StringBuffer();
    Valores = Valores != null ? Valores : "";
    StringTokenizer sVecValores = new StringTokenizer(Valores, ",");
    int nRegs = crsTabla.size(); //Numero de Registros
    int nRegActual = 0;          //Registro Actual.
    int nCols = Integer.parseInt(nColumnas);
    int nCol = 0; //Columna Actual
    int nRegsxCol = (int)Math.rint(nRegs / nCols); //Registros por columna
    if ((nRegs % nCols) != 0) nRegsxCol++;
    try{
        crsTabla.beforeFirst();
        cdgHTML.append("<Table><tr>");
        while (crsTabla.next()){
            if (nRegActual == nCol * nRegsxCol)
                cdgHTML.append("<td valign='TOP'><table>");
            cdgHTML.append("<tr><td>");
            cdgHTML.append("<input type='checkbox' name='");
            cdgHTML.append(nombreObj);
            
            cdgHTML.append("' value='"+crsTabla.getString(Columna1)+"'");
            sVecValores = new StringTokenizer(Valores, ",");
            while (sVecValores.hasMoreTokens()){
                if (sVecValores.nextToken().compareToIgnoreCase(crsTabla.getString(Columna1))== 0)
                    cdgHTML.append(" CHECKED");
            }//while Tokenizer
            cdgHTML.append(">");
            cdgHTML.append(crsTabla.getString(Columna2));
            cdgHTML.append("</td></tr>");
            if (nRegActual == ((nCol +1) * nRegsxCol)-1) {
                cdgHTML.append("</table></td>");
                nCol ++;
            }
            else
                if (crsTabla.isLast())
                    cdgHTML.append("</table></td>");
            nRegActual ++;
        }
        cdgHTML.append("</tr></Table>");
        out.println(cdgHTML.toString());
    }//Try
    catch (Exception e){
        System.out.println("Error en CRSCheckBox: "+ e.getMessage());
    }
};

public void CRSCheckBoxCRS(SentenciasCRS crsTabla,JspWriter out,String nombreObj, String nColumnas, String Columna1,String Columna2,String Valores){
// Construye las opciones de un componente CheckBox de HTML a partir de un CachedRowSet
// En donde el campo que se graba es diferente al que despliega. Distribuyendo el resultado en nColumnas
//
// Paramentros
//   crsTabla  : Indica el CacheRowSet a Utilizar
//   out       : Indica la pagina desde la cual es llamado
//   nombreObj : Contiene el nombre del Objeto RadioButton.
//   nColumnas : Indica cuantas columnas se deben generar para mostrar los resultados.
//   Columna1  : Contiene el nombre del campo a partir del cual se llenara el componente en su propiedad value.
//   Columna2  : Contiene el nombre del campo que se desplegara en el CheckBox.
//   Valores     : Contiene la lista separada por comas de los valores seleccionados inicialmente en el CheckBox.
    StringBuffer cdgHTML = new StringBuffer();
    Valores = Valores != null ? Valores : "";
    StringTokenizer sVecValores = new StringTokenizer(Valores, ",");
    int nRegs = crsTabla.size(); //Numero de Registros
    int nRegActual = 0;          //Registro Actual.
    int nCols = Integer.parseInt(nColumnas);
    int nCol = 0; //Columna Actual
    int nRegsxCol = (int)Math.rint(nRegs / nCols); //Registros por columna
    if ((nRegs % nCols) != 0) nRegsxCol++;
    try{
        crsTabla.beforeFirst();
        cdgHTML.append("<Table cellpadding='0' cellspacing='0'><tr>");
        while (crsTabla.next()){
            if (nRegActual == nCol * nRegsxCol)
                cdgHTML.append("<td valign='TOP'><table cellpadding='0' cellspacing='0'>");
            cdgHTML.append("<tr><td>");
            cdgHTML.append("<input type='checkbox' name='");
            cdgHTML.append(nombreObj);
            
            cdgHTML.append("' value='"+crsTabla.getString(Columna1)+"'");
            sVecValores = new StringTokenizer(Valores, ",");
            while (sVecValores.hasMoreTokens()){
                if (sVecValores.nextToken().compareToIgnoreCase(crsTabla.getString(Columna1))== 0)
                    cdgHTML.append(" CHECKED");
            }//while Tokenizer
            cdgHTML.append(">");
            cdgHTML.append(crsTabla.getString(Columna2));
            cdgHTML.append("</td></tr>");
            if (nRegActual == ((nCol +1) * nRegsxCol)-1) {
                cdgHTML.append("</table></td>");
                nCol ++;
            }
            else
                if (crsTabla.isLast())
                    cdgHTML.append("</table></td>");
            nRegActual ++;
        }
        cdgHTML.append("</tr></Table>");
        out.println(cdgHTML.toString());
    }//Try
    catch (Exception e){
        System.out.println("Error en CRSCheckBox: "+ e.getMessage());
    }
};

//Agregado por Salvador Mu�oz A.
// 07/Sep/ 2004

public void CRSCheckBoxSector(CachedRowSet crsTabla,JspWriter out,String nombreObj, String nColumnas, String Columna1,String Columna2,String Valores){
// Construye las opciones de un componente CheckBox de HTML a partir de un CachedRowSet
// En donde el campo que se graba es diferente al que despliega. Distribuyendo el resultado en nColumnas
//
// Paramentros
//   crsTabla  : Indica el CacheRowSet a Utilizar
//   out       : Indica la pagina desde la cual es llamado
//   nombreObj : Contiene el nombre del Objeto RadioButton.
//   nColumnas : Indica cuantas columnas se deben generar para mostrar los resultados.
//   Columna1  : Contiene el nombre del campo a partir del cual se llenara el componente en su propiedad value.
//   Columna2  : Contiene el nombre del campo que se desplegara en el CheckBox.
//   Valores     : Contiene la lista separada por comas de los valores seleccionados inicialmente en el CheckBox.
    StringBuffer cdgHTML = new StringBuffer();
    StringTokenizer sVecValores = new StringTokenizer(Valores, ",");
    int nRegs = crsTabla.size(); //Numero de Registros
    int nRegActual = 0;          //Registro Actual.
    int nCols = Integer.parseInt(nColumnas);
    int nCol = 0; //Columna Actual
    int nRegsxCol = (int)Math.rint(nRegs / nCols); //Registros por columna
    if ((nRegs % nCols) != 0) nRegsxCol++;
    try{
        crsTabla.beforeFirst();
        cdgHTML.append("<Table><tr>");
        while (crsTabla.next()){
            if (nRegActual == nCol * nRegsxCol)
                cdgHTML.append("<td valign='TOP'><table>");
            cdgHTML.append("<tr><td>");
            if (crsTabla.getString(Columna1).indexOf("0000") > 2) { // SECTOR
                cdgHTML.append("<p class='Texto03'>" + crsTabla.getString(Columna2) + "</p>");
            }
            else if (crsTabla.getString(Columna1).indexOf("00") > 4) { // RAMA
                cdgHTML.append("<p class='Texto01'>" + crsTabla.getString(Columna2) + "</p>");
            } else{
                cdgHTML.append("<input type='checkbox' name='");
                cdgHTML.append(nombreObj);
                cdgHTML.append("' value='"+crsTabla.getString(Columna1)+"'");
                sVecValores = new StringTokenizer(Valores, ",");
                while (sVecValores.hasMoreTokens()){
                    if (sVecValores.nextToken().compareToIgnoreCase(crsTabla.getString(Columna1))== 0)
                        cdgHTML.append(" CHECKED");
                }//while Tokenizer
                cdgHTML.append(">");
                cdgHTML.append(crsTabla.getString(Columna2));
            }
            cdgHTML.append("</td></tr>");
            if (nRegActual == ((nCol +1) * nRegsxCol)-1) {
                cdgHTML.append("</table></td>");
                nCol ++;
            }
            else
                if (crsTabla.isLast())
                    cdgHTML.append("</table></td>");
            nRegActual ++;
        }
        cdgHTML.append("</tr></Table>");
        out.println(cdgHTML.toString());
    }//Try
    catch (Exception e){
        System.out.println("Error en CRSCheckBoxSector: "+ e.getMessage());
    }
};

public void CRSComboBoxIndentadoTipProd(CachedRowSet crsTabla,JspWriter out,String Columna1,String Columna2,String Valor, String vNivel,int esAreaDifu) {
// Construye las opciones de un componente ComboBox de HTML a partir de un CachedRowSet
// En donde el campo que se graba es diferente al que despliega.
// Aplica para La lista de Clasificaci�n del Producto, validando que solo las areas
// enlistadas en variable mtiConfigura.areasDifus puedan ver Material de Difusi�n (0400)
// en dicho ComboBox despleagado.
//
// Paramentros
//   crsTabla    : Indica el CacheRowSet a Utilizar
//   out         : Indica la pagina desde la cual es llamado
//   Columna1    : Contiene el nombre del campo a partir del cual se llenara el componente en su propiedad value.
//   Columna2    : Contiene el nombre del campo que se desplegara en el ComboBox.
//   Valor       : Contiene el valor que debe estar seleccionado inicialmente en el ComboBox.
//   vNivel      : Indica el nombre del campop que contiene el nivel para representar la indentacion.
    String indenta;
    try {
     crsTabla.beforeFirst();
     while (crsTabla.next()) {
        if(!crsTabla.getString("cvetippro").startsWith("04") || (crsTabla.getString("CVETIPPRO").startsWith("04") && esAreaDifu > 0)){
           indenta = " ";
           for (int i=0; i < Integer.parseInt(crsTabla.getString(vNivel)); i++){
              indenta += "&nbsp;&nbsp;&nbsp;";
           }
           if(Valor.compareTo(crsTabla.getString(Columna1).trim()) == 0)
              out.println("<option value="+crsTabla.getString(Columna1)+" selected>"+ indenta + crsTabla.getString(Columna2)+"</option>");
           else
              out.println("<option value="+crsTabla.getString(Columna1)+">"+ indenta + crsTabla.getString(Columna2)+"</option>");
           }
       }
    }
    catch (Exception e) {
      System.out.println("Error en CRSComboBoxIndentadoTipProd: "+ e.getMessage());
    }
};

 //Agregado por Claudia Orozco Mej�a
//Para indicar n�mero de columnas en Registro de Obra - Rama por RadioButton.
public void CRSRadioButtonCol(CachedRowSet crsTabla,JspWriter out,String nombreObj, String nColumnas, String Columna1,String Columna2,String Valores){
// Construye las opciones de un componente RadioButton de HTML a partir de un CachedRowSet
// En donde el campo que se graba es diferente al que despliega. Distribuyendo el resultado en nColumnas
//
// Paramentros
//   crsTabla  : Indica el CacheRowSet a Utilizar
//   out       : Indica la pagina desde la cual es llamado
//   nombreObj : Contiene el nombre del Objeto RadioButton.
//   nColumnas : Indica cuantas columnas se deben generar para mostrar los resultados.
//   Columna1  : Contiene el nombre del campo a partir del cual se llenara el componente en su propiedad value.
//   Columna2  : Contiene el nombre del campo que se desplegara en el ComboBox.
//   Valores   : Contiene los valores que debe estar seleccionados inicialmente en el ComboBox.
    StringBuffer cdgHTML = new StringBuffer();
    StringTokenizer sVecValores = new StringTokenizer(Valores, ",");
    int nRegs = crsTabla.size(); //Numero de Registros
    int nRegActual = 0;          //Registro Actual.
    int nCols = Integer.parseInt(nColumnas);
    int nCol = 0; //Columna Actual
    int nRegsxCol = (int)Math.rint(nRegs / nCols); //Registros por columna
    if ((nRegs % nCols) != 0) nRegsxCol++;
    try{
        crsTabla.beforeFirst();
        cdgHTML.append("<Table><tr>");
        while (crsTabla.next()){
            if (nRegActual == nCol * nRegsxCol)
                cdgHTML.append("<td valign='TOP'><table>");
            cdgHTML.append("<tr><td>");
            cdgHTML.append("<input type='radio' name='");
            cdgHTML.append(nombreObj);
            cdgHTML.append("' value='"+crsTabla.getString(Columna1)+"'");
            sVecValores = new StringTokenizer(Valores, ",");
            while (sVecValores.hasMoreTokens()){
                if (sVecValores.nextToken().compareToIgnoreCase(crsTabla.getString(Columna1))== 0)
                    cdgHTML.append(" CHECKED");
            }//while Tokenizer
            cdgHTML.append(">");
            cdgHTML.append(crsTabla.getString(Columna2));
            cdgHTML.append("</td></tr>");
            if (nRegActual == ((nCol +1) * nRegsxCol)-1) {
                cdgHTML.append("</table></td>");
                nCol ++;
            }
            else
                if (crsTabla.isLast())
                    cdgHTML.append("</table></td>");
            nRegActual ++;
        }
        cdgHTML.append("</tr></Table>");
        out.println(cdgHTML.toString());
    }//Try
    catch (Exception e){
        System.out.println("Error en CRSRadioButtonCol: "+ e.getMessage());
    }
};

///Funcion Realizada por Salvador Mu�oz Aguilera 30/03/2005
// Esta funcion llena un Objeto Combo Box presentando en la descripcion tanto el valor como la descripcion. Dados por Columna1 y Columna2 Respectivamente.
//Parametros:
//     crsTabla: Contiene el CachedRowSet que contiene los datos a mostrar en el objeto.
//     out     : Contiene la referencia al objeto Response, el cual sera el encargado de
//               imprimir en la pagina el objeto con los datos.
//     Columna1: Indica el Campo contenido en crsTabla que se usara como value en el objeto.
//     Columna2: Indica el Campo contenido en crsTabla que se usara como descripcion junto con el campo Columna1
//     Valor   : Indica el Valor que debe aparecer seleccionado en el objeto de manera inicial.
public void CRSComboBoxValorDesc(CachedRowSet crsTabla,JspWriter out,String Columna1,String Columna2,String Valor) {
    java.text.DecimalFormat df1 = new java.text.DecimalFormat("00000000000000");
    String lsValor = "";
    int i = 0;
    int lCantSP = 0; //Guarda la cantidad de espacios a Agregar al campo

    try {
      crsTabla.beforeFirst();
      while (crsTabla.next()) {
        lsValor = crsTabla.getString(Columna1);
        lCantSP = crsTabla.getString(Columna1).length() - crsTabla.getString(Columna1).trim().length();
        for (i=0; i < lCantSP; i++){
            lsValor = lsValor + "-";
        }
        if(Valor.compareTo(crsTabla.getString(Columna1).trim()) == 0)
           out.println("<option value="+crsTabla.getString(Columna1)+" selected>"+ lsValor + " " +crsTabla.getString(Columna2)+"</option>");
        else
           out.println("<option value="+crsTabla.getString(Columna1)+">"+ lsValor + " " +crsTabla.getString(Columna2)+"</option>");
      }
    }
    catch (Exception e) {
      System.out.println("Error en CRSComboBoxValorDesc: "+ e.getMessage());
    }
  };

public void CRSComboBoxPorOpcion(CachedRowSet crsTabla,JspWriter out,String Columna1,String Columna2,String Valor, String vNivel,String varOpcion, int posVarOpcion) {

// Funcion agregada por Martha  03-Mar-2006
// Construye las opciones de un componente ComboBox de HTML a partir de un CachedRowSet
// en donde la lista de opciones que despliega son seleccionados de acuerdo asi una posici�n
// especificada en una variable String que se recibe como par�metro, tiene asignado
// valor 1 (listarlo) o 0 (no listarlo). Cada posici�n en la cadena con ceros y unos puede
// representar una opcion o un modulo del sistema, un departamento una �rea, etc.
// Puede aplicarse a cualquier lista (cat�logo o tabla) que tenga una variable String en la cual
// de acuerdo al valor (0 � 1) en cada posici�n de la cadena se pueda seleccionar o no el
// registro para ser mostrado en el ComboBox.
//
// Paramentros
//   crsTabla    : Indica el CacheRowSet a Utilizar (por lo general es un cat�logo)
//   out         : Variable que permite que la salida (print) sea a la p�gina que llame a esta funcion
//   Columna1    : Contiene el nombre del campo a partir del cual se llenara el componente en su propiedad value.
//   Columna2    : Contiene el nombre del campo que se desplegara en el ComboBox.
//   Valor       : Contiene el valor que debe estar seleccionado inicialmente en el ComboBox. Si no desea seleccion inicial, usar "".
//   vNivel      : Indica el nombre del campop que contiene el nivel para representar la indentacion. Si no existe este campo de nivel usar ""..
//   varOpcion   : Contiene el nombre de la variable que contiene la cadena con ceros y unos, donde
//                 cada posicion puede representar una opci�n, una pagina, un modulo, un departamento, etc.
//   posVarOpcion: Esta variable indique sobre que posicion de la cadena en varOpcion se verificara el valor 0 o 1.
    String indenta = "";
    try {
      crsTabla.beforeFirst();
      while (crsTabla.next()) {
       if (!vNivel.equals("")) {
          indenta = "";
          for (int i=0; i < Integer.parseInt(crsTabla.getString(vNivel)); i++){
             indenta += "&nbsp;&nbsp;&nbsp;";
          }
      }
       if (crsTabla.getString(varOpcion) != null){
        if (crsTabla.getString(varOpcion).substring(posVarOpcion-1,posVarOpcion).equals("1")) {
          if(Valor.compareTo(crsTabla.getString(Columna1).trim()) == 0){
            out.println("<option value="+crsTabla.getString(Columna1)+" selected>"+ indenta + crsTabla.getString(Columna2)+"</option>");
          }
          else
            out.println("<option value="+crsTabla.getString(Columna1)+">"+ indenta + crsTabla.getString(Columna2)+"</option>");
        }
      }
      }
    }
    catch (Exception e) {
      System.out.println("Error en CRSComboBoxPorOpcion: "+ e.getMessage());
    }
  };

 //funcion Proporcionada Por Norma Capuchino 7/7/2006
 // Esta función permite tomar 2 decimales de una cantidad X

  public String redondeoNumber(double numero) {
      Locale locale = new Locale("es", "MX");
      Locale.setDefault(locale);
      NumberFormat formatter = new DecimalFormat("#,###,###,##0.00");
    return formatter.format(numero);
  }

//funcion Proporcionada Por Norma Capuchino 7/7/2006
 // Esta función permite tomar 2 decimales de una cantidad X sin aplicarle Formato
 // de Cantidad 

  public String redondeoNumberSinFmt(double numero) {
      Locale locale = new Locale("es", "MX");
      Locale.setDefault(locale);
      NumberFormat formatter = new DecimalFormat("###0.00");
    return formatter.format(numero);
  }
  
public String CRSComboBoxUnSeleccionadoString(CachedRowSet crsTabla,String Columna1,String Columna2,String Valor) {
    String lsTempo="";
    try {
      crsTabla.beforeFirst();
      while (crsTabla.next()) {
        if(Valor.compareTo(crsTabla.getString(Columna1).trim()) == 0)
          lsTempo=crsTabla.getString(Columna2);
      }
    }
    catch (Exception e) {
      System.out.println("Error en CRSComboBoxUnSeleccionado: "+ e.getMessage());
    }
    return lsTempo;
};
 
  public String CRSLabel(CachedRowSet crsTabla, String Columna1,String Columna2,String Valores){
// Construye las opciones de un componente CheckBox de HTML a partir de un CachedRowSet
// En donde el campo que se graba es diferente al que despliega. Devolviendo una cadena String con los valores separados
// por una coma.
// Paramentros
//   crsTabla  : Indica el CacheRowSet a Utilizar  
//   Columna1  : Contiene el nombre del campo a partir del cual se llenara el componente en su propiedad value.
//   Columna2  : Contiene el nombre del campo que se desplegara en el CheckBox.
//   Valores     : Contiene la lista separada por comas de los valores seleccionados inicialmente en el CheckBox.
    StringBuffer cdgHTML = new StringBuffer();
    StringTokenizer sVecValores = new StringTokenizer(Valores, ",");
    try{
        crsTabla.beforeFirst();
        while (crsTabla.next()){
            sVecValores = new StringTokenizer(Valores, ",");
            while (sVecValores.hasMoreTokens()){
                if (sVecValores.nextToken().compareToIgnoreCase(crsTabla.getString(Columna1))== 0)
                    cdgHTML.append(crsTabla.getString(Columna2) + "; ");
            }//while Tokenizer
        }
        
    }//Try
    catch (Exception e){
        cdgHTML.setLength(0);
        cdgHTML.append("error");
        System.out.println("Error en CRSLabel: "+ e.getMessage());
    }
    return cdgHTML.toString();
};

public String CRSCheckLabel(CachedRowSet crsTabla, String Columna1,String Columna2,String Valores){
// Construye las opciones de un componente CheckBox de HTML a partir de un CachedRowSet
// En donde el campo que se graba es diferente al que despliega. Distribuyendo el resultado en nColumnas
//
// Paramentros
//   crsTabla  : Indica el CacheRowSet a Utilizar
//   Columna1  : Contiene el nombre del campo a partir del cual se llenara el componente en su propiedad value.
//   Columna2  : Contiene el nombre del campo que se desplegara en el CheckBox.
//   Valores     : Contiene la lista separada por comas de los valores seleccionados inicialmente en el CheckBox.
    StringBuffer cdgHTML = new StringBuffer();
    StringBuffer cdgHTML2 = new StringBuffer();    
    StringBuffer cdgHTML3 = new StringBuffer();    
    StringTokenizer sVecValores = new StringTokenizer(Valores, ",");
    boolean bantuvohijos = false, bantuvonietos = false;
    int i = 0, j = 0, k = 0;
    try{
        crsTabla.beforeFirst();
        while (crsTabla.next()){
            if (crsTabla.getString(Columna1).indexOf("0000") > 2) { // SECTOR
                if(bantuvonietos){
                  if(bantuvohijos){
                     cdgHTML2.append(cdgHTML3.toString().substring(0,cdgHTML3.toString().length() - 2)+"</UL>");
                     bantuvohijos = false;
                  }
                  cdgHTML.append(cdgHTML2.toString()+"</UL>");
                }
                cdgHTML2.setLength(0);// sector
                cdgHTML3.setLength(0);// rama                
                bantuvonietos = false;
                if(k==0){cdgHTML.append("<UL>");}
                k++;
                cdgHTML2.append("<LI><b class='Texto03'>" + crsTabla.getString(Columna2) + "</b>");
                i = 0; j=0;
            }
            else if (crsTabla.getString(Columna1).indexOf("00") > 4) { // RAMA
                if(bantuvohijos){
                  cdgHTML2.append(cdgHTML3.toString().substring(0,cdgHTML3.toString().length() - 2)+"</UL>");
                  bantuvohijos=false;
                } else {j=0;}
                cdgHTML3.setLength(0);
                if(j==0){cdgHTML3.append("<UL>");}
                j++;i = 0;
                cdgHTML3.append("<LI><b class='Texto01'>" + crsTabla.getString(Columna2) + "</b>");
            }//if
            else{
                sVecValores = new StringTokenizer(Valores, ",");
                while (sVecValores.hasMoreTokens()){
                    if(i == 0){cdgHTML3.append("<UL>");}
                    if (sVecValores.nextToken().compareToIgnoreCase(crsTabla.getString(Columna1))== 0){
                         bantuvohijos = true;
                         bantuvonietos = true;
                         cdgHTML3.append("<LI>"+crsTabla.getString(Columna2) + "; ");
                    }//if
                    i++;
                }//while Tokenizer
            }//else
          }//while (crsTabla.next())
          if(bantuvonietos){
             if(bantuvohijos){
                cdgHTML2.append(cdgHTML3.toString().substring(0,cdgHTML3.toString().length() - 2));
                cdgHTML.append(cdgHTML2.toString());
             }else{
                cdgHTML.append("<UL>"+cdgHTML2.toString());
             }   
          }
    }//Try
    catch (Exception e){
        cdgHTML.setLength(0);
        cdgHTML.append("error");
        System.out.println("Error en CRSCheckLabel: "+ e.getMessage());
    }
    return cdgHTML.toString();    
};
//***********


public void CRSRadioButtonOnclick(CachedRowSet crsTabla,JspWriter out,String nombreObj, String Columna1,String Columna2,String Valor,String funcion){
// Construye las opciones de un componente RadioButton de HTML a partir de un CachedRowSet
// En donde el campo que se graba es diferente al que despliega.
//
// Paramentros
//   crsTabla  : Indica el CacheRowSet a Utilizar
//   out       : Indica la pagina desde la cual es llamado
//   nombreObj : Contiene el nombre del Objeto RadioButton.
//   Columna1  : Contiene el nombre del campo a partir del cual se llenara el componente en su propiedad value.
//   Columna2  : Contiene el nombre del campo que se desplegara en el ComboBox.
//   Valor     : Contiene el valor que debe estar seleccionado inicialmente en el ComboBox.
//   funcion   : Indica la funcion que se ejecutara al momento de dar click
    try {
      crsTabla.beforeFirst();
      while (crsTabla.next()) {
        if(Valor.compareTo(crsTabla.getString(Columna1).trim()) == 0)
           out.println("<INPUT TYPE='radio' NAME='" + nombreObj + "' VALUE='"+crsTabla.getString(Columna1)+"' onclick='"+ funcion +"(this.value);' checked>"+ crsTabla.getString(Columna2)+"<br>");
        else
           out.println("<INPUT TYPE='radio' NAME='" + nombreObj + "' VALUE='"+crsTabla.getString(Columna1)+"' onclick='"+ funcion +"(this.value);'>"+ crsTabla.getString(Columna2)+"<br>");
      }
    }
    catch (Exception e) {
      System.out.println("Error en CRSRadioButton: "+ e.getMessage());
    }
};

public void CRSCheckBoxOnclick(CachedRowSet crsTabla,JspWriter out,String nombreObj, String nColumnas, String Columna1,String Columna2,String Valores,String funcion){
// Construye las opciones de un componente CheckBox de HTML a partir de un CachedRowSet
// En donde el campo que se graba es diferente al que despliega. Distribuyendo el resultado en nColumnas
//
// Paramentros
//   crsTabla  : Indica el CacheRowSet a Utilizar
//   out       : Indica la pagina desde la cual es llamado
//   nombreObj : Contiene el nombre del Objeto RadioButton.
//   nColumnas : Indica cuantas columnas se deben generar para mostrar los resultados.
//   Columna1  : Contiene el nombre del campo a partir del cual se llenara el componente en su propiedad value.
//   Columna2  : Contiene el nombre del campo que se desplegara en el CheckBox.
//   Valores     : Contiene la lista separada por comas de los valores seleccionados inicialmente en el CheckBox.
//   funcion   : Indica la funcion que se ejecutara al momento de dar click
    StringBuffer cdgHTML = new StringBuffer();
    StringTokenizer sVecValores = new StringTokenizer(Valores, ",");
    int nRegs = crsTabla.size(); //Numero de Registros
    int nRegActual = 0;          //Registro Actual.
    int nCols = Integer.parseInt(nColumnas);
    int nCol = 0; //Columna Actual
    int nRegsxCol = (int)Math.rint(nRegs / nCols); //Registros por columna
    if ((nRegs % nCols) != 0) nRegsxCol++;
    try{
        crsTabla.beforeFirst();
        cdgHTML.append("<Table><tr>");
        while (crsTabla.next()){
            if (nRegActual == nCol * nRegsxCol)
                cdgHTML.append("<td valign='TOP'><table>");
            cdgHTML.append("<tr><td>");
            cdgHTML.append("<input type='checkbox' name='");
            cdgHTML.append(nombreObj);
            cdgHTML.append("' value='"+crsTabla.getString(Columna1)+"'");
            sVecValores = new StringTokenizer(Valores, ",");
            while (sVecValores.hasMoreTokens()){
                if (sVecValores.nextToken().compareToIgnoreCase(crsTabla.getString(Columna1))== 0)
                    cdgHTML.append(" CHECKED");
            }//while Tokenizer
            cdgHTML.append(" onclick='"+ funcion +"(this.value);'>");
            cdgHTML.append(crsTabla.getString(Columna2));
            cdgHTML.append("</td></tr>");
            if (nRegActual == ((nCol +1) * nRegsxCol)-1) {
                cdgHTML.append("</table></td>");
                nCol ++;
            }
            else
                if (crsTabla.isLast())
                    cdgHTML.append("</table></td>");
            nRegActual ++;
        }
        cdgHTML.append("</tr></Table>");
        out.println(cdgHTML.toString());
    }//Try
    catch (Exception e){
        System.out.println("Error en CRSCheckBox: "+ e.getMessage());
    }
};

public void CRSSelectMultipleOnclick(CachedRowSet crsTabla,JspWriter out,String nombreObj, String Columna1,String Columna2,String Valor,String funcion,String mult){
// Construye las opciones de un componente MULTIPLE de HTML a partir de un CachedRowSet
// En donde el campo que se graba es diferente al que despliega.
//
// Paramentros
//   crsTabla  : Indica el CacheRowSet a Utilizar
//   out       : Indica la pagina desde la cual es llamado
//   nombreObj : Contiene el nombre del Objeto RadioButton.
//   Columna1  : Contiene el nombre del campo a partir del cual se llenara el componente en su propiedad value.
//   Columna2  : Contiene el nombre del campo que se desplegara en el ComboBox.
//   Valor     : Contiene el valor que debe estar seleccionado inicialmente en el ComboBox.
//   funcion   : Indica la funcion que se ejecutara al momento de dar click
    try {
      out.println("<SELECT MULTIPLE='false' class='txa' size='6' name='" + nombreObj + "' onclick='"+ funcion +"(this.value);' style='width:500px;'> >");
      crsTabla.beforeFirst();
      while (crsTabla.next()) {
        if(Valor.compareTo(crsTabla.getString(Columna1).trim()) == 0)
           out.println("<OPTION VALUE='"+crsTabla.getString(Columna1)+"'>"+ crsTabla.getString(Columna2)+"<br>");
        else
           out.println("<OPTION VALUE='"+crsTabla.getString(Columna1)+"'>"+ crsTabla.getString(Columna2)+"<br>");
      }
      out.println("</SELECT>");
    }
    catch (Exception e) {
      System.out.println("Error en CRSSelectMultipleOnclick: "+ e.getMessage());
    }
};

%>