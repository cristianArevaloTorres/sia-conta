package sia.utilerias.generador;

import java.sql.*;
import java.util.*;
import java.text.*;
import sun.jdbc.rowset.*;
import javax.servlet.jsp.*;

public class hpaginacion extends funciones {

  public hpaginacion() {

  }; // constructor

  private int geNumColumna;
  private String gsReferencia;
  private String gsParametros;
  private String gsParRef;
  private boolean gbCheck;

  private String Alineacion(String Token){
                String pos="";
    if (Token.charAt(0)=='-') {
      pos="RIGTH";
    }
    else if (Token.charAt(Token.length()-1)=='-') {
      pos="LEFT";
    }
    else{
      pos="CENTER";
                }
                return(pos);
  }

        private void ImprimeTituloTabla(String Titulo, int numCol, JspWriter out) {
    try {
      out.println("<tr>");
      out.println("<th class=general colspan='"+ numCol+ "'> <b>"+ LetraCapital(Titulo)+ "</b></th>");
      out.println("</tr>");
          }catch(Exception e){
          }
        }

        private void ImprimeNombreTabla(String Tabla, int numCol, JspWriter out, ResultSet rsCatCampos, Statement sentencia){
    try {
                  String Titulo= "";
      String Query= "Select Tabla_Nombre From RH_TC_Tablas Where Nombre_Fisico = '"+Tabla+"'";
            rsCatCampos=sentencia.executeQuery(Query);
      if (rsCatCampos.first()) {
                          Titulo= rsCatCampos.getString("TABLA_NOMBRE");
                        }
                  ImprimeTituloTabla(Titulo, numCol, out);
          }catch(Exception e){
          }
        }

  private void ImprimeEncabezados(String Tabla, String Campos, ResultSet rsCatCampos, Statement sentencia, JspWriter out, String Porcientos){
    try{
          String Token="";
          String Valor= "";
          StringTokenizer stCampos = new StringTokenizer(Campos,",");
          StringTokenizer stPorcientos= new StringTokenizer(Porcientos,",");
    String Query= "Select C.Posicion, C.Descripcion, C.Alias  From RH_TC_Tablas T, RH_TC_Campos C Where T.Tabla=C.Tabla And T.Nombre_Fisico = '"+Tabla+"'";
    rsCatCampos=sentencia.executeQuery(Query);
                String BuscarPor="";
    while (stCampos.hasMoreTokens()){
      Token= stCampos.nextToken();
                  Valor= stPorcientos.hasMoreTokens()?stPorcientos.nextToken():"20%";
      Token= EliminaCaracter(Token, '-');
            rsCatCampos.beforeFirst();
                        try {
                          Integer.parseInt(Token);
                                BuscarPor="Posicion";
                        }catch(Exception e){
                          BuscarPor="Descripcion";
                        }
                        while ((rsCatCampos.next()) && (Token.compareTo(rsCatCampos.getString(BuscarPor))!=0));
            if (!rsCatCampos.isAfterLast())
        out.println("<th class=general width='"+ Valor+ "'>"+LetraCapital(rsCatCampos.getString("Alias"))+"</th>");
                  }// while
    } // try
           catch(Exception e){
                        System.out.println("Error: "+e);
          }
        }

        private String Formateado(String Celda, String Formato){
                if (Formato.equals("$")){
                        boolean isDouble=false;
                        try{
                                Double.valueOf(Celda);
                                isDouble=true;
                        }catch(Exception e){
                          isDouble=false;
                        }

                        if (isDouble){
                                double CeldaD = Double.valueOf(Celda).doubleValue();
                                Celda	= FormatearNumero(CeldaD, "$###,###.##");
                                return Celda;
                        }else{
                          return Celda;
                        }
                }else{
                  return Celda;
                }
        }


  private void ImprimeCeldas(String Campos, String Formatos, ResultSet crsTabla, JspWriter out, String Porcientos,int peNumColumna, String psReferencia, String psParametros, String psParRef){
    try{
                        String Token="";
                        String Valor="";
                        String Formato="";
                        String pos="";
                        String referencia;
                        String parametros;
                        String parRef;
                        String opeReferencia;
                        String campo;
                        String campo2;
                        int cont=1;
                        StringTokenizer stCampos = new StringTokenizer(Campos,",");
                        StringTokenizer stPorcientos = new StringTokenizer(Porcientos,",");
                        StringTokenizer stFormatos = new StringTokenizer(Formatos,",");
                        while (stCampos.hasMoreTokens()){
                                Token= stCampos.nextToken();
                                Valor= stPorcientos.hasMoreTokens()?stPorcientos.nextToken():"20%";
                                Formato= stFormatos.hasMoreTokens()?stFormatos.nextToken():"";
                                pos  = Alineacion(Token);
                                Token= EliminaCaracter(Token, '-');
                         if (cont==peNumColumna){
                           referencia=psReferencia;
                           opeReferencia="</a>";
                           parametros=psParametros;
                           parRef=psParRef;
                           campo=Formateado(crsTabla.getString(Token),Formato);
                           campo2=campo;
                         }
                         else{
                           referencia="";
                           opeReferencia="";
                           parametros="";
                           parRef="";
                           campo="";
                           campo2=Formateado(crsTabla.getString(Token),Formato);
                         }
                                cont++;
                                try{
                                        if (crsTabla.getString(Integer.parseInt(Token))!=null){
                                                out.println("<td class=general align=\""+pos+"\" width='"+ Valor+ "'>"+Formateado(crsTabla.getString(Integer.parseInt(Token)),Formato)+"</td>");
                                                //out.println("<td class=general align=\""+pos+"\" width='"+ Valor+ "'>"+crsTabla.getString(Integer.parseInt(Token))+"</td>");
                                        }else{
                                                out.println("<td class=general align=\""+pos+"\" width='"+ Valor+ "'> - </td>");
                                        }
                                }catch(Exception e){
                                        if (crsTabla.getString(Token)!=null){
                                               out.println("<td class=general align=\""+pos+"\" width='"+ Valor+ "'>"+referencia+parametros+campo+parRef+campo2+opeReferencia+"</td>");
                                               // out.println("<td class=general align=\""+pos+"\" width='"+ Valor+ "'>"+referencia+Formateado(crsTabla.getString(Token),Formato)+opeReferencia+"</td>");
                                                // out.println("<td class=general align=\""+pos+"\" width='"+ Valor+ "'><a href=xxx.html> "+Formateado(crsTabla.getString(Token),Formato)+"</a></td>");
                                                //out.println("<td class=general align=\""+pos+"\" width='"+ Valor+ "'>"+crsTabla.getString(Token)+"</td>");
                                        }else{
                                                out.println("<td class=general align=\""+pos+"\" width='"+ Valor+ "'> - </td>");
                                        }
                                }
                        }// while
                }catch(Exception e) {
                        System.out.println("Error: "+e);
                }
  }

  private String ObtenerTodosLosCampos(String Tabla, ResultSet rsCatCampos, Statement sentencia, JspWriter out){
    try{
                        String Campos="";
                        String Query= "Select C.Descripcion From RH_TC_Tablas T, RH_TC_Campos C Where T.Tabla=C.Tabla And T.Nombre_Fisico = '"+ Tabla+ "'";
                        rsCatCampos=sentencia.executeQuery(Query);
                        rsCatCampos.beforeFirst();
                        while (rsCatCampos.next()){
                          Campos+= rsCatCampos.getString("Descripcion")+",";
                        }
                        return (Campos);
                }catch(Exception e){
                  return ("");
                }
  }

  private void ImprimeFunJavaScript(JspWriter out){
    try{
      out.println("<script languaje=JavaScript>");
      out.println("  function fullscreen(url, w, h) {");
      out.println("    var l = (screen.availWidth-500)/2");;
            out.println("    var t = (screen.availHeight-400)/2;");
      out.println("    var features = 'width='+ w+ ', height='+ h+ ', left='+l+', top='+t+', screenX=0, screenY=0';");
      out.println("    features+= 'location=0, scrollbars=1, resizable=0, menubar=0, toolbar=0, status=0';");
      out.println("    window.open(url, '', features);");
      out.println("}</script>");
                }catch (Exception e){
                }
        }

        private void ImprimeEncabezadoCheck(boolean Lugar, boolean Inserta, String Tipo, JspWriter out){
    try{
                        String texto="";
      if (Lugar && Inserta) {
              if (Tipo.toUpperCase().equals("TEXT")) texto="Captura";
                                else texto = "Selecciona";
                                out.println("<th class=general width='5%'>"+texto+"</th>");
                        }
                }// try
                catch(Exception e){
          }
  }

        private void ImprimeEncabezadoVerMas(boolean verMas, JspWriter out){
    try{
            if (verMas){
              out.println("<th class=general width='10%'>Ver más</th>");
            }
                }catch(Exception e){
          }
  }

  private int ObtenerNumColumnas(String Campos, boolean verMas, boolean Inputs){
          StringTokenizer stCampos = new StringTokenizer(Campos,",");
                                  // VerMas          Radio o Checked
    return(stCampos.countTokens()+ (verMas?1:0)+ (Inputs?1:0));
  }

  private void ImprimeFormatoFila(int numReg, JspWriter out, int Colores){
    try{
                        String ColorIni= "";
                        String ColorFin= "";
                        switch(Colores) {
                                case 1:
                                        ColorIni="resGrisClaro";
                                        ColorFin="resGrisOscuro";
                                        break;
                                case 2:
                                        ColorIni="resGrisClaro";
                                        break;
                                case 3:
                                        ColorIni="resAzulClaro";
                                        ColorFin="resAzulOscuro";
                                        break;
                                case 4:
                                        ColorIni="resAzulClaro";
                                        break;	  }if (numReg % 2 ==0){
                                out.println(" class=\""+ ColorIni+ "\">");
                        }
                        else{
                                out.println(" class=\""+ ColorFin+ "\">");
                        }
                }catch(Exception e){
                }
  }

  public String ObtenerClaves(String Claves, ResultSet crsTabla){
    int x=0;
    String concatClaves="";
          String Token="";
          try{
      StringTokenizer stClaves = new StringTokenizer(Claves,",");
      while (stClaves.hasMoreTokens()){
              Token=stClaves.nextToken();
              concatClaves+=crsTabla.getString(Token)+ getSeparador().charAt(x++);
             }// while
             return (concatClaves);
          }catch(Exception e){
            return ("");
           } // try
  }

        private void ImprimirCeldaCheck(boolean Lugar, boolean Insertar, String Tipo, String Propiedades, String concatClaves, String Nombre, JspWriter out){
          try{
             if(Tipo.toUpperCase().equals("TEXT")){
                                 if (concatClaves.indexOf("$")>=0) {
                                         Nombre += concatClaves.substring (concatClaves.indexOf("$")+1,concatClaves.length()-1);
                                   concatClaves = concatClaves.substring (0,concatClaves.indexOf("$"));
                                 }
                                 Propiedades += " class=cajaTexto";
                         }

       if (Lugar && Insertar){
                                 if(!Tipo.toUpperCase().equals("RADIO") && !Tipo.toUpperCase().equals("CHECKBOX") && !Tipo.toUpperCase().equals("TEXT") )
                                         out.println("<td class=general width='5%' align=\"center\"> - </td>");
                                 else
                       out.println("<td class=general width='5%' align=\"center\"><input type='"+Tipo +"' "+Propiedades+" value='"+concatClaves+"' name='"+ Nombre+ "'></td>");
       }
    }catch (Exception e){
          }
        }

  private void ImprimirCeldaVerMas(boolean verMas, int numReg, String Tabla, String Condiciones, JspWriter out){
    try{
            if (verMas){
        out.println("<td class=general width=\"10%\" align=\"center\"><a href=\"javascript:fullscreen('/Librerias/Funciones/edl_vermas.jsp?varNumReg="+numReg+"&varTabla="+Tabla+"&varCondiciones="+Condiciones+"', 500, 350)\" > Click aquí </a></td>");
            } // try
          }catch(Exception e){
          }
        }

        private void ImprimeTodosLosEncabezados(ResultSet rsCatCampos, String Tabla, String Campos, boolean Lugar,
                                          boolean verMas, int numCol, Statement sentencia, boolean Check, String Tipo,
                                                                                                                                                                        JspWriter out, String Porcientos){
    try{
      ImprimeNombreTabla(Tabla,numCol,out,rsCatCampos,sentencia);
             out.println("<tr>");
       ImprimeEncabezadoCheck(Check, Lugar?true:false, Tipo, out);
            ImprimeEncabezados(Tabla, Campos, rsCatCampos, sentencia, out, Porcientos);
            ImprimeEncabezadoVerMas(verMas, out);
      ImprimeEncabezadoCheck(Check, Lugar?false:true, Tipo, out);
            out.println("</tr>");
          }catch(Exception e){
           }
        }

        private int ImprimeTodosLasCeldas(ResultSet crsTabla, String  Tabla, String Condiciones, String Campos, String Formatos, String Claves, boolean Lugar, String Tipo, String Propiedades,
                     String Nombre, boolean verMas, int registroActual, int registrosDesplegar, JspWriter out, String Porcientos, int Colores){
                int numReg=0;
                try{
                        if(registroActual<= 1) {
                                crsTabla.beforeFirst();
                                registroActual= 1;
                        }
                        else
                                crsTabla.absolute(registroActual- 1);
                        while (crsTabla.next() && numReg< registrosDesplegar){
                                numReg++;
                                out.print("<tr");
                                ImprimeFormatoFila(numReg, out, Colores);
                                String concatClaves= ObtenerClaves(Claves,crsTabla);
                                if (gbCheck)
                                  ImprimirCeldaCheck(Nombre.length()!=0, Lugar?true:false, Tipo, Propiedades, concatClaves, Nombre, out);
                                ImprimeCeldas(Campos,Formatos,crsTabla, out, Porcientos,geNumColumna,gsReferencia,gsParametros,gsParRef);
                                ImprimirCeldaVerMas(verMas, numReg, Tabla, Condiciones, out);
                                if (gbCheck)
                                  ImprimirCeldaCheck(Nombre.length()!=0, Lugar?false:true, Tipo, Propiedades, concatClaves, Nombre, out);
                                out.println("</tr>");
                        }
                        return(numReg);
                }catch(Exception e){
                        return 0;
                }
  }

        public int funcion_intermedia(Connection conexion, ResultSet crsTabla, String []defTablas, String []defCampos, String []defInputs,
                                       boolean verMas, int registroActual, int registrosDesplegar, JspWriter out) {
    try {
      int    numCol       = 0;
      String Tabla        = "";
                        String Condiciones  = "";
            int    Colores      = 0;
                        if (defTablas!=null){
                                Tabla        = defTablas[0]!=null?EliminaCaracter(defTablas[0].toUpperCase(), ' '):"";
                                try {	  Condiciones  = defTablas[5]!=null?defTablas[5].toUpperCase():"";
                                }catch (Exception e){	  Condiciones ="";	}
                                Colores      = defTablas[3]!=null?Integer.parseInt(EliminaCaracter(defTablas[3].toUpperCase(), ' ')):0;
                        }


      String Campos       = "";
            String Porcientos   = "";
                        String Formatos     = "";
                        if (defCampos!=null){
                                Campos       = defCampos[0]!=null?EliminaCaracter(defCampos[0].toUpperCase(), ' '):"";
                                Porcientos   = defCampos[1]!=null?EliminaCaracter(defCampos[1].toUpperCase(), ' '):"";
                                try {Formatos = defCampos[2]!=null?EliminaCaracter(defCampos[2].toUpperCase(), ' '):"";
                                }catch(Exception e) {Formatos = "";}
                        }

                        String Claves     = "";
            String Tipo       = "";
            String Nombre     = "";
            boolean Lugar     = false;
                        String Propiedades= "";
                        if (defInputs!=null){
                                Claves      = defInputs[0]!= null?EliminaCaracter(defInputs[0].toUpperCase(), ' '):"";
                                Tipo       	= defInputs[1]!= null?defInputs[1].toUpperCase():"";
                                Nombre     	= defInputs[2]!= null?defInputs[2].toUpperCase():"";
                                Lugar     	= defInputs[3]!= null?defInputs[3].toUpperCase().equals("LEFT"):false;
                                Propiedades = defInputs[4]!= null?defInputs[4]:"";
                        }

//       = ObtenerConexion(ajgBaseDatos, ajgUser, ajgPassword);
      Statement sentencia= conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
      ResultSet rsCatCampos=null;
      String Query="";
            if (Campos.compareTo("*")==0) {
        Campos= ObtenerTodosLosCampos(Tabla, rsCatCampos, sentencia, out);
            }
      numCol= ObtenerNumColumnas(Campos, verMas, defInputs!=null);
      out.println("<TABLE border=1 class=general width="+ defTablas[1]+ " align="+ defTablas[2]+ ">");
            ImprimeTodosLosEncabezados(rsCatCampos, Tabla, Campos, Lugar, verMas, numCol, sentencia, defInputs!= null, Tipo, out, Porcientos);
      numCol= ImprimeTodosLasCeldas(crsTabla, Tabla, Condiciones, Campos, Formatos, Claves, Lugar, Tipo, Propiedades, Nombre, verMas, registroActual, registrosDesplegar, out, Porcientos, Colores);
      out.println("</TABLE>");
            ImprimeFunJavaScript(out);
            return numCol;
    }
    catch (Exception e) {
      System.out.println( "-Error: "+ e.getMessage() );
            return 0;
    }
        }

        public int Total_Registros(ResultSet crsTabla, String Query, JspWriter out){
          try {
      crsTabla.last();
                        if(crsTabla.getRow()< 1) {
                          out.println("<br><div align=\"center\"><b>No existen registros que desplegar...</b></div><br>");
                     //     out.println("<br><div align=\"center\"><b>Query:</b>"+ Query+ "</div><br>");
                        }return crsTabla.getRow();
                }
                catch(Exception e) {
                  return 0;
                }
        }

        public void SeleccionarHorizontalTabla(Connection conexion, ResultSet crsTabla, String []defTablas, String []defCampos, String []defInputs, boolean verMas, JspWriter out) {
    int totalRegistros= Total_Registros(crsTabla, "Es el contenido del CachedRowSet "+ (defTablas[0]!=null?defTablas[0].toUpperCase():"no definido."), out);
          if(totalRegistros>0)
      funcion_intermedia(conexion, crsTabla, defTablas, defCampos, defInputs, verMas, 1, totalRegistros, out);
  }

  private void paginacion(String []defTablas, int registroActual, int registrosDesplegar, int totalRegistros, int Contador, String Pagina, JspWriter out, String Formulario){
          try {
      int numPaginas= 0;
      out.println("<TABLE border= 0  width="+ defTablas[1]+ " align="+ defTablas[2]+ ">");
                        out.println("<tr><td align=\"left\">");
      out.println("Total registros <b>"+ totalRegistros+"</b>  desplegando <b>"+ registroActual+ " - "+ (registroActual+ Contador- 1)+ "<b>");
                        out.println("</td><td align=\"right\">");
      if (registroActual>1){
                           if (Formulario!=null){
                                        out.println(" <a href='javascript:EnviarFormularioPaginacion(1)'> <img src='/Librerias/Imagenes/navegar_primero.gif' width='18' height='13' border=\"none\"></a> ");
                                        out.println(" <a href='javascript:EnviarFormularioPaginacion("+(registroActual- registrosDesplegar)+")'><img src='/Librerias/Imagenes/navegar_anterior.gif' width='14' height='13' border=\"none\"></a> ");
                                }else{
                                        out.println(" <a href='"+ Pagina+ "?registroActual="+ 1+ "'><img src='/Librerias/Imagenes/navegar_primero.gif' width='18' height='13' border=\"none\"></a> ");
                                        out.println(" <a href='"+ Pagina+ "?registroActual="+(registroActual- registrosDesplegar)+"'><img src='/Librerias/Imagenes/navegar_anterior.gif' width='14' height='13' border=\"none\"></a> ");
                                }
      }

                  numPaginas= (int)(totalRegistros/ registrosDesplegar)+ ((totalRegistros% registrosDesplegar)!=0?1:0);
                        int numPaginasDesplegar = 20;

            int inicioPagina= ((int)(registroActual / registrosDesplegar) + ((registroActual% registrosDesplegar)!=0?1:0))- 10;
                        inicioPagina--;
                        if (inicioPagina<0)
                          inicioPagina=0;
                        if (numPaginas<numPaginasDesplegar)
                          inicioPagina=0;

                        numPaginasDesplegar += inicioPagina;
                        if (numPaginasDesplegar > numPaginas)
                          numPaginasDesplegar = numPaginas;

            for(int x= inicioPagina; x< numPaginasDesplegar; x++) {
                          if (((x+ 1)* registrosDesplegar+ 1)!= (registroActual + registrosDesplegar)){
                            if (Formulario!=null)	 out.print("<a href='javascript:EnviarFormularioPaginacion("+ (x* registrosDesplegar+ 1)+ ")'><b>"+ (x+1)+ "</b></a> ");
                                        else									 out.println(" <a href='"+ Pagina+ "?registroActual="+ (x* registrosDesplegar+ 1)+"'><b>"+ (x+ 1)+ "</b></a> ");
                                }
                                else
                            out.println(" <b><font color = red>"+ (x+ 1)+ "</font></b> ");
                  }

                        if ((registroActual+Contador)<= totalRegistros){
                           if (Formulario!=null){
                                        out.println(" <a href='javascript:EnviarFormularioPaginacion("+(registroActual+ Contador)+")'><img src='/Librerias/Imagenes/navegar_siguiente.gif' width='14' height='13' border=\"none\"></a> ");
                                        out.println(" <a href='javascript:EnviarFormularioPaginacion("+(totalRegistros- (totalRegistros% registrosDesplegar)+ 1)+")'><img src='/Librerias/Imagenes/navegar_ultimo.gif' width='18' height='13' border=\"none\"></a> ");
                                }else{
                                        out.println(" <a href='"+ Pagina+ "?registroActual="+(registroActual+ Contador)+"'><img src='/Librerias/Imagenes/navegar_siguiente.gif' width='14' height='13' border=\"none\"></a> ");
                                        out.println(" <a href='"+ Pagina+ "?registroActual="+(totalRegistros- (totalRegistros% registrosDesplegar)+ 1)+ "'><img src='/Librerias/Imagenes/navegar_ultimo.gif' width='18' height='13' border=\"none\"></a> ");
                                }
      }
                        out.println("</td>");
                        out.println("</tr>");
      out.println("</TABLE>");

                        if (Formulario!=null){
                                out.println("<input type=hidden name=registroActual value=0>");
                                out.print("<script>");
                                out.print("  function EnviarFormularioPaginacion(registro) {");
                                out.print("    "+ Formulario+ ".registroActual.value = registro;");
                                out.print("    "+ Formulario+ ".submit();");
                                out.print("  };");
                                out.print("</script>");
                        }
                }
                catch(Exception e){
                }
        }

        public void DesplegarHorizontalTabla(Connection conexion, ResultSet crsTabla, String []defTablas, String []defCampos, boolean verMas, int registroActual, int registrosDesplegar, String Pagina, JspWriter out, String Formulario) {
    try{
      int Contador= 0;
      int totalRegistros= Total_Registros(crsTabla, "Es el contenido del CachedRowSet "+ (defTablas[0]!=null?defTablas[0].toUpperCase():"no definido."), out);
                        if(totalRegistros>0) {
        Contador= funcion_intermedia(conexion, crsTabla, defTablas, defCampos, null, verMas, registroActual, registrosDesplegar, out);
                          paginacion(defTablas, registroActual, registrosDesplegar, totalRegistros, Contador, Pagina, out, Formulario);
                        }
                }
                catch(Exception e){
                }
        }

        private void ImprimeCeldaEncabezados(String Campos, String Alias, String Porcientos, JspWriter out){
    try{
            String Token="";
            String Nombre= "";
            String Valor= "";
            StringTokenizer stCampos    = new StringTokenizer(Campos,",");
            StringTokenizer stAlias     = new StringTokenizer(Alias,",");
            StringTokenizer stPorcientos= new StringTokenizer(Porcientos,",");
      while (stCampos.hasMoreTokens()){
        Token = stCampos.nextToken();
                                Nombre= stAlias.hasMoreElements()?stAlias.nextToken():"No definido";
                    Valor = stPorcientos.hasMoreElements()?stPorcientos.nextToken():"20%";
        out.println("<th class=general width='"+ Valor+ "'>"+ LetraCapital(Nombre)+ "</th>");
                  }// while
    } // try
           catch(Exception e){
          }
        }

  private void ImprimeLosEncabezados(String Campos, boolean Lugar,
                                     boolean verMas, int numCol, boolean Check, String Tipo,JspWriter out, String Alias, String Porcientos, String Titulo){
    try{
      ImprimeTituloTabla(Titulo, numCol, out);
             out.println("<tr>");
      if (gbCheck)
        ImprimeEncabezadoCheck(Check, Lugar?true:false, Tipo, out);
      ImprimeCeldaEncabezados(Campos, Alias, Porcientos, out);
      ImprimeEncabezadoVerMas(verMas, out);
      if (gbCheck)
       ImprimeEncabezadoCheck(Check, Lugar?false:true, Tipo, out);
      out.println("</tr>");
          }catch(Exception e){
           }
        }

        public int funcion_de_paso(ResultSet crsTabla, String []defTablas, String []defCampos, String []defInputs,
                                   boolean verMas, int registroActual, int registrosDesplegar, JspWriter out) {
    try {
      int    numCol    = 0;

            int    Colores   = 0;
            String Titulo    = "";
                        if (defTablas!=null){
                                Colores   = defTablas[3]!=null?Integer.parseInt(EliminaCaracter(defTablas[3].toUpperCase(), ' ')):0;
                                Titulo    = defTablas[4]!=null?defTablas[4].toUpperCase():"";
                        }

                        String Campos    = "";
            String Porcientos= "";
      String Alias     = "";
                        String Formatos  = "";
                        if (defCampos!=null){
                                Campos    = defCampos[0]!=null?EliminaCaracter(defCampos[0].toUpperCase(), ' '):"";
                                Porcientos= defCampos[1]!=null?EliminaCaracter(defCampos[1].toUpperCase(), ' '):"";
                                Alias     = defCampos[2]!=null?defCampos[2].toUpperCase():"";
                                try { Formatos = defCampos[3]!=null?defCampos[3].toUpperCase():"";}
                                catch (Exception e) {Formatos ="";}
                        }

                        String	Claves    = "";
                        String	Tipo      = "";
                        String 	Nombre    = "";
                        boolean	Lugar     = false;
                        String Propiedades="";
                        if (defInputs!=null){
                                Claves    = defInputs[0]!= null?EliminaCaracter(defInputs[0].toUpperCase(), ' '):"";
                                Tipo      = defInputs[1]!= null?defInputs[1].toUpperCase():"";
                                Nombre    = defInputs[2]!= null?defInputs[2].toUpperCase():"";
                                Lugar    = defInputs[3]!= null?defInputs[3].toUpperCase().equals("LEFT"):false;
                                try { Propiedades=defInputs[4]!= null?defInputs[4]:"";
                                }catch (Exception e) {Propiedades="";}
                        }


                        numCol           = ObtenerNumColumnas(Campos, verMas, defInputs!=null);

                        out.println("<TABLE border=1 class=general width="+ defTablas[1]+ " align="+ defTablas[2]+ ">");
           ImprimeLosEncabezados(Campos, Lugar, verMas, numCol, defInputs!= null, Tipo, out, Alias, Porcientos, Titulo);
                        numCol= ImprimeTodosLasCeldas(crsTabla, "", "", Campos, Formatos, Claves, Lugar, Tipo, Propiedades, Nombre, verMas, registroActual, registrosDesplegar, out, Porcientos, Colores);
      out.println("</TABLE>");
            ImprimeFunJavaScript(out);
            return numCol;
    }
    catch (Exception e) {
      System.out.println( "Error: "+ e.getMessage() );
            return 0;
    }
        }

        public void DesplegarHorizontalQuery(Connection conexion, String []defTablas, String []defCampos, int registroActual, int registrosDesplegar, String Pagina, JspWriter out, String Formulario) {
    try{
      int Contador= 0;
      Statement sentencia= conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                        ResultSet crsTabla= sentencia.executeQuery(defTablas[0]!=null?defTablas[0].toUpperCase():"");
      int totalRegistros= Total_Registros(crsTabla, (defTablas[0]!=null?defTablas[0].toUpperCase():"no definido."), out);
                        if(totalRegistros>0) {
        Contador= funcion_de_paso(crsTabla, defTablas, defCampos, null, false, registroActual, registrosDesplegar, out);
                          paginacion(defTablas, registroActual, registrosDesplegar, totalRegistros, Contador, Pagina, out, Formulario);
                        }
                }
                catch(Exception e){
      System.out.println( "Error: "+ e.getMessage() );
                }
  }

    public void SeleccionarHorizontalQuery(Connection conexion, String []defTablas, String []defCampos, String []defInputs, JspWriter out,int peNumColumna, String psReferencia, String psParametros, String psParRef, boolean pbCheck) {
    try{
      geNumColumna=peNumColumna;
      gsReferencia=psReferencia;
      gsParametros=psParametros;
      gsParRef=psParRef;
      gbCheck=pbCheck;
      int Contador= 0;
      Statement sentencia= conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
      ResultSet crsTabla= sentencia.executeQuery(defTablas[0]!=null?defTablas[0].toUpperCase():"");
      int totalRegistros= Total_Registros(crsTabla,(defTablas[0]!=null?defTablas[0].toUpperCase():"no definido."), out);
      if(totalRegistros>0)
        funcion_de_paso(crsTabla, defTablas, defCampos, defInputs, false, 0, totalRegistros, out);
      crsTabla.close();
      crsTabla=null;
      sentencia.close();
      sentencia=null;
    }
    catch(Exception e){
      System.out.println( "Error: "+ e.getMessage() );
    }
  }

}; // hpaginacion