package sia.utilerias.generador;

import java.sql.*;
import java.util.*;
import java.text.*;
import java.util.Date;
import java.io.Writer;
import sun.jdbc.rowset.*;
import javax.servlet.jsp.*;
import javax.servlet.http.*;
import java.util.Calendar;

  // Nota: Estos archivos no deben de ser modificados por ningun motivo, a menos que se detecte
  //       alguna incosistencia, pero antes de hacer la modificación se deberá avisar al jefe
  //       de departamento del cual se depende, y tomar cartas en el asunto.
  //
  //       Si uds. tienen funciones que realicen algun proceso comun y ademas quieran que sean
  //       utilizadas por sus compañero del sia (y no sientan que les estan robando sus códigos),
  //       por favor de depositarlas donde en las carpetas que corresponda y anexar un documento
  //       en txt (texto ascii) donde se expliquen el funcionamiento de la misma mostrando un ejemplo.
  //
  //       En el archivo .JSP debera anotar quien lo hizo, la fecha y hora para saber a quien dirigirse
  //       en caso de que se requiriera una actualizacion o corrección.
  //
  //       Ademas de avisarles por correo a todos lo integrantes del sia, si lo desean conveniente
  //       David tiene las cuentas de correos de todos, el podira ayudarnos con eso.
  //
  //       Recuerden que todo esto es para simplificar nuestros códigos y evitar la duplicidad de
  //       código.
  //
  //       Para cualquier aclaración dirigirse con el jefe inmediato o en su defecto con David.
  //

  // Archivo modificado el dia 02/07/2003 11:35 am

public class funciones {

  private String Separador="$#&~@!%|=^�";

  public funciones() {

  }; // constructor

  public String getSeparador() {
    return Separador;
  }; // getSeparador

  public void InsertarHojaEstilo(Writer writer, String contexto){
           try {
             PrintWriter out= new PrintWriter(writer);
      out.println("<base target=\"_self\">");
      out.println("<script language=\"javascript\">");
      out.println("  if (navigator.appName==\"Netscape\" && parseInt(navigator.appVersion) >= 4)");
      out.println("    document.write('<link rel=\"stylesheet\" type=\"text/css\" href=\"/Librerias/Estilos/sianetscape.css\">');");
      out.println("  else");
      out.println("    document.write('<link rel=\"stylesheet\" type=\"text/css\" href=\"/Librerias/Estilos/siaexplorer.css\">');");
                        out.println("</script>");
                }
                catch  (Exception e) {
                };
        };

  public void InsertarHojaEstilo(Writer out, HttpServletRequest rqt){
    InsertarHojaEstilo(out, rqt.getContextPath());
        }; //  InsertarHojaEstilo

  public void InsertarHojaEstilo(Writer out){
    InsertarHojaEstilo(out, "");
        }; //  InsertarHojaEstilo


  // Funcion que pone el encabezado que debe llevar cada p�gina al inicio de ella.
  //
  public void TituloPagina(Writer writer, String Titulo, String subTitulo, String Texto, boolean Linea) {
    try {
             PrintWriter out= new PrintWriter(writer);
      out.println("<table width='100%' border='0' class='sianoborder'>");
      if(Titulo.length()!= 0) {
        out.println("  <tr>");
        out.println("    <td width='100%' colspan='2' class='sianoborder'><div align='right'><b><font color='#003399'>"+ Titulo+ "</font></b></div></td>");
        out.println("  </tr>");
      };
      out.println("  <tr>");
      out.println("    <td width='85%' class='sianoborder'><b>"+ subTitulo+ "</b></td>");
      if(Texto.length()!= 0) {
        out.println("    <td width='15%' class='sianoborder'><div align='right'><strong><font color='#00CCFF'>("+ Texto+ ")</font></strong></div></td>");
      }
      out.println("  </tr>");
      out.println("</table>");
      out.println((Linea?"<hr noshade size='5'>":""));
    }
    catch(Exception e) {
    };
  };

  public void MsgError(JspWriter out, String msgError) {
          try {
            out.println("<script>");
            out.println("  var Msg= 'Se produjo una execepci�n:';");
                        out.println("  Msg+='"+ msgError.replace('\n', ' ').replace('"', '~').replace('\'', '~')+ "';");
            out.println("  alert(Msg);");
            out.println("</script>");
                }
                catch (Exception e) {
      System.out.println("[MsgError] Error: "+ e.getMessage());
    }; // try
        };

  public String EliminaCaracter(String Campos, char Caracter){
    String Resultado= "";
    for(int x= 0; x< Campos.length(); x++) {
      if (Campos.charAt(x)!= Caracter) {
         Resultado+= Campos.charAt(x);
                        };
    };
          return(Resultado);
  }

  public String nombrePersona(String nombreCampo) {
    String regresar= "";
    boolean letraCapital= true;
          for(int x= 0; x< nombreCampo.length(); x++) {
            regresar+= letraCapital? nombreCampo.substring(x, x+ 1).toUpperCase(): nombreCampo.substring(x, x+ 1).toLowerCase();
            letraCapital= nombreCampo.charAt(x)== ' ';
    }; // for
   return regresar;
  }; // nombrePersona

  public String LetraCapital(String NombreCampo) {
          if(NombreCampo.length()!=0)
            return NombreCampo.substring(0,1).toUpperCase()+ NombreCampo.substring(1).toLowerCase();
          else
            return NombreCampo;
  };

  public String DespliegaFecha(String Fecha)  {
    String[] Meses= {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
    String Anio= "";
    int    Mes= 0;
    String Dia= "";
    String Conversion= "Fecha invalida.";
    if(Fecha.length()==6 || Fecha.length()==8) {
      if(Fecha.length()==6)
        Anio= Fecha.substring(0,2);
      else
        Anio= Fecha.substring(0,4);
      Mes= Integer.parseInt(Fecha.substring(Fecha.length()- 4,Fecha.length()- 2))- 1;
      Dia= Fecha.substring(Fecha.length()- 2,Fecha.length());
      if(Mes>=0 && Mes<12)
        Conversion= Integer.parseInt(Dia)+ " de "+ Meses[Mes]+ " del "+ Anio;
    }
    return Conversion;
  };

  public String FormatearNumero(double Valor, String Formato){
    NumberFormat Formateo= NumberFormat.getCurrencyInstance(Locale.US);
    if (Formateo instanceof DecimalFormat) {
      ((DecimalFormat) Formateo).setDecimalSeparatorAlwaysShown(true);
      ((DecimalFormat) Formateo).applyPattern(Formato);
    }; // if
          return Formateo.format(Valor);
  };

   public String VerificaLetrasCadena(String Msg)  {
     String Valor= "";
     for(int x= 0; x< Msg.length(); x++) {
           if("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_".indexOf(Msg.charAt(x))>=0)
               Valor+=Msg.charAt(x);
         }
         return Valor;
   }


  public void BotonesPiePagina(JspWriter out, String tipo, String tipoNombre, String tipoValor, String tipoClick) {
    BotonesPiePagina(out, tipo, tipoNombre, tipoValor, tipoClick,  true);
  };

  public void BotonesPiePagina(JspWriter out, String tipo, String tipoNombre, String tipoValor, String tipoClick, boolean Limpiar) {
    try {
            out.println("<hr class=\"piePagina\">");
      out.println("<br>");
      out.println("<div align=\"center\">");
                        if(tipoClick.length()> 0)
                          tipoClick= " onClick=\""+ tipoClick+"\"";
      out.println("<input type=\""+ tipo+ "\" name=\""+ tipoNombre+ "\" value=\""+ tipoValor+ "\""+ tipoClick+" class=\"boton\">");
                        if(Limpiar)
        out.println("<input type=\"reset\" name=\"Limpiar\" value=\"Restaurar datos\" class=\"boton\">");
      out.println("</div>");
                }
    catch (Exception e) {
                        MsgError(out, e.getMessage());
    }
  };

  public void CreaTabla(Connection conection, CachedRowSet IdTabla, String NTabla, String Consulta) {
    try {
      IdTabla.setTableName(NTabla);
      IdTabla.setCommand(Consulta);
      IdTabla.execute(conection);
    }
    catch (Exception e) {
      System.out.println("[CreaTabla] Error: "+ e.getMessage());
    }; // try
  };

  public String ObtieneDescripcion(ResultSet Tabla, String Campo, String Valor, String Regresa)  {
    String Dato= Valor;
    try {
      Tabla.beforeFirst();
//      System.out.println("\n\n[ObtieneDescripcion]");
//      System.out.println("Inicia campo:"+ Campo+ " regresa: "+ Regresa+ " Valor: "+ Valor+ " BOF: "+ Tabla.isBeforeFirst());
      while(Tabla.next() && !Tabla.getString(Campo).trim().equals(Valor.trim())) {
//        System.out.println("Entro campo:"+ Campo+ " regresa: "+ Regresa+ " Valor: "+ Valor+ " Info: "+ Tabla.getString(Campo));
      };
//      System.out.println("Salio campo:"+ Campo+ " regresa: "+ Regresa+ " Valor: "+ Valor+ " EOF: "+ Tabla.isAfterLast());
//      System.out.println("campo:"+ Campo+ " posicion: "+ Tabla.findColumn(Campo));
//      System.out.println("regresa:"+ Regresa+ " posicion: "+ Tabla.findColumn(Regresa));
      if(!Tabla.isAfterLast() && Tabla.getString(Regresa)!= null)
        Dato= Tabla.getString(Regresa);
    }
    catch (Exception e) {
      System.out.println("[ObtieneDescripcion] Error: "+ e.getMessage() );
    }
//    System.out.println("\n\n");
    return Dato;
  };

  public void CreaComboCached(ResultSet crsTabla,JspWriter out,String Columna1,String Columna2) {
    try {
      crsTabla.beforeFirst();
      while (crsTabla.next()) {
        out.println("<option value="+crsTabla.getString(Columna1)+">"+crsTabla.getString(Columna1)+ ".- "+ crsTabla.getString(Columna2)+"</option>");
      }
    }
    catch (Exception e) {
      System.out.println("[CreaComboCached] Error: "+ e.getMessage());
    }
  };

  public void CreaComboDoble(ResultSet crsTabla,JspWriter out,String Columna1,String Columna2,String Valor) {
    try {
      crsTabla.beforeFirst();
      while (crsTabla.next() && !crsTabla.getString(Columna1).equals(Valor));
      if(crsTabla.getString(Columna1).equals(Valor))
        if(!Columna1.equals(Columna2))
          out.println("<option value="+crsTabla.getString(Columna1)+">"+crsTabla.getString(Columna1)+ ".- "+ crsTabla.getString(Columna2)+"</option>");
        else
          out.println("<option value="+crsTabla.getString(Columna1)+">"+crsTabla.getString(Columna1)+"</option>");
      crsTabla.beforeFirst();
      while (crsTabla.next()) {
        if(!crsTabla.getString(Columna1).equals(Valor))
          if(!Columna1.equals(Columna2))
            out.println("<option value="+crsTabla.getString(Columna1)+">"+crsTabla.getString(Columna1)+ ".- "+ crsTabla.getString(Columna2)+"</option>");
          else
            out.println("<option value="+crsTabla.getString(Columna1)+">"+crsTabla.getString(Columna1)+"</option>");
      }
    }
    catch (Exception e) {
      System.out.println("[CreaComboDoble] Error: "+ e.getMessage());
    }
  };

  public String CreaComboBox(ResultSet crsTabla,JspWriter out,String Columna1,String Columna2,String Valor) {
          String regresar= Valor;
    try {
      crsTabla.beforeFirst();
      while (crsTabla.next() && !crsTabla.getString(Columna1).equals(Valor));
      if(!crsTabla.isAfterLast() && crsTabla.getString(Columna1).equals(Valor))
        out.println("<option value=\""+crsTabla.getString(Columna1)+"\">"+ crsTabla.getString(Columna2)+"</option>");
                        else {
        crsTabla.beforeFirst();
                           if(crsTabla.next())
                                  regresar= crsTabla.getString(Columna1);
                        };
      crsTabla.beforeFirst();
      while (crsTabla.next()) {
        if(!crsTabla.getString(Columna1).equals(Valor))
          out.println("<option value=\""+crsTabla.getString(Columna1)+"\">"+ crsTabla.getString(Columna2)+"</option>");
      }
    }
    catch (Exception e) {
      System.out.println("[CreaComboBox] Error: "+ e.getMessage());
    }
                return regresar;
  };

   public void CreaComboCached(ResultSet crsTabla,JspWriter out,int NoCol,int columna1,int columna2,int columna3,int columna4,int Valor, int NumCar,String valCmp) throws SQLException,ClassNotFoundException,IllegalAccessException,InstantiationException {
      int Long=NumCar;
      try {
             crsTabla.beforeFirst();
         while (crsTabla.next()) {
                     if (crsTabla.getString(columna1).length()< NumCar)
                                    Long=crsTabla.getString(columna1).length();
                                else
                                   Long=NumCar;
                     switch(NoCol) {
                           case 1 :
                                if (valCmp.equals(crsTabla.getString(Valor)))
                  out.println("<option selected value="+crsTabla.getString(Valor)+">"+crsTabla.getString(columna1).substring(0,Long)+"</option>");
                                else
                                  out.println("<option value="+crsTabla.getString(Valor)+">"+crsTabla.getString(columna1).substring(0,Long)+"</option>");
                                break;
                           case 2 :
                            if (valCmp.equals(crsTabla.getString(Valor)))
                              out.println("<option selected value="+crsTabla.getString(Valor)+">"+crsTabla.getString(columna1)+"*"+crsTabla.getString(columna2)+"</option>");
                                else
                                  out.println("<option value="+crsTabla.getString(Valor)+">"+crsTabla.getString(columna1)+"*"+crsTabla.getString(columna2)+"</option>");
                                break;
                           case 3 :
                            if (valCmp.equals(crsTabla.getString(Valor)))
                                out.println("<option selected value="+crsTabla.getString(Valor)+">"+crsTabla.getString(columna1)+"*"+crsTabla.getString(columna2)+
                                                "*"+crsTabla.getString(columna3)+"</option>");
                            else
                                   out.println("<option value="+crsTabla.getString(Valor)+">"+crsTabla.getString(columna1)+"*"+crsTabla.getString(columna2)+
                                                "*"+crsTabla.getString(columna3)+"</option>");
                                break;
                           case 4 :
                              if (valCmp.equals(crsTabla.getString(Valor)))
                                out.println("<option selected value="+crsTabla.getString(Valor)+">"+crsTabla.getString(columna1)+"*"+crsTabla.getString(columna2)+"*"+
                                                crsTabla.getString(columna3)+"*"+crsTabla.getString(columna4)+"</option>");
                              else
                                    out.println("<option value="+crsTabla.getString(Valor)+">"+crsTabla.getString(columna1)+"*"+crsTabla.getString(columna2)+"*"+
                                                crsTabla.getString(columna3)+"*"+crsTabla.getString(columna4)+"</option>");
                                  break;
                           default :
                             out.println("Verifica tu n�mero de columnas");
                                 break;
                         }
     }

          }
          catch (Exception e) {
        System.out.println("[CreaComboCached] Error: "+ e.getMessage());
          }
   }

  public String cambiarDiagonal(String value, boolean tipo) {
    String tmp= "";
    for(int x= 0; x< value.length(); x++) {
      if(value.charAt(x)=='\\')
        if(tipo)
          tmp+= "\\"+ value.charAt(x);
        else
          tmp+= "/"+ value.charAt(x);
      else
        tmp+= value.charAt(x);
    }; // for
    return tmp;
  };

  public String regresarCampos(ResultSet rst) {
          String regresar= " ";
          try {
      rst.beforeFirst();
                  while(rst.next()) {
        if(rst.getString("CAPTURAR").equals("1")) {
                            regresar+= rst.getString("NOMBRE")+ ",";
                          }; // if
                  }; // while
                }
                catch(SQLException eSQL) {
      System.out.println("[regresarCampos] Error: "+ eSQL.getMessage());
                };
           return regresar.substring(0, regresar.length()- 1);
        }; // regresarCampos

  public String regresarCampos(ResultSet mst, String campos, char caracter) {
          String regresar= "";
                try {
                  String valor= "";
      StringTokenizer st = new StringTokenizer(campos, ",");
      while (st.hasMoreTokens()) {
        valor= st.nextToken();
                    regresar+= mst.getString(valor)+ caracter;
      }; // while
      if(regresar.length()== 0)
        regresar+= caracter;
                }
                catch(SQLException eSQL) {
      System.out.println("[regresarCampos] Error: "+ eSQL.getMessage());
                }
                catch(Exception e) {
      System.out.println("[regresarCampos] Error: "+ e.getMessage());
                }; // try
                return regresar.substring(0, regresar.length()- 1);
  }; // regresarCampos

        public String formatearFecha(Date fecha, String patron) {
                SimpleDateFormat formato= new SimpleDateFormat(patron);
                return formato.format(fecha);
        }; // formatearFecha

  public String getNombreMes(int mes)  {
    String[] nombreMes= {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
    return nombreMes[mes];
  }; // getNombreMes

  public String getNombreDia(int dia) {
          String nombreDia[]= {"Domingo", "Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado"};
    return nombreDia[dia- 1];
  };  //

  public int getDiasEnElMes(int anio, int mes) {
          int dias[]= {31,28,31,30,31,30,31,31,30,31,30,31};
          if(anio % 400 == 0 || (anio % 4 ==0 && anio % 100 != 0))
      dias[1] = 29;
    return dias[mes];
  }; // getDiasEnElMes

  public int diasSiguienteMes(int anio, int mes) {
    if(mes+ 1> 11) {
      anio+= 1;
      mes  = 0;
    }
    else
      mes++;
    return getDiasEnElMes(anio, mes);
  }; // siguienteMes

  public String convierteFecha(String fecha, boolean dia) {
    GregorianCalendar calendario= new GregorianCalendar();
    int xAnio= Integer.parseInt(fecha.substring(0, 4));
    int xMes = Integer.parseInt(fecha.substring(4, 6))- 1;
    int xDia = Integer.parseInt(fecha.substring(6, 8));
    calendario.set(xAnio, xMes, xDia);
    return (dia? getNombreDia(calendario.get(calendario.DAY_OF_WEEK))+ ", ": "")+
           calendario.get(calendario.DATE)+ " de "+
           getNombreMes(calendario.get(calendario.MONTH))+ " del "+
           calendario.get(calendario.YEAR);
  }; // convierteFecha

  public String buscarDescripcion(Connection conection, String formato, String valor) {
          String regresar= "";
    try {
                  String[] elementos= {"","",""};
      StringTokenizer st = new StringTokenizer(formato, "~^");
                        int contador= 0;
      while (st.hasMoreTokens()) {
        elementos[contador]= st.nextToken();
//			 System.out.println(elementos[contador]);
                                contador++;
      }
      regresar= "="+ valor;
      try {
        int i= Integer.parseInt(valor);
      }
      catch (NumberFormatException e) {
        regresar= "='"+ valor+ "'";
      }; // try
      Statement estatuto= conection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            System.out.println("[buscarDescripcion] Select * From "+ elementos[0] + " Where "+ elementos[1]+ regresar);
                  ResultSet crsTabla= estatuto.executeQuery("Select * From "+ elementos[0]+ " Where "+ elementos[1]+ regresar);
      crsTabla.beforeFirst();
      if(crsTabla.next())
                          regresar= regresarCampos(crsTabla, elementos[2], ' ');
                        else
                          regresar= valor;
                  estatuto.close();
    }
    catch (SQLException eSQL) {
      System.out.println("[buscarDescripcion] Error: "+ eSQL.getMessage());
    }
    catch (Exception e) {
      System.out.println("[buscarDescripcion] Error: "+ e.getMessage());
    }
                return regresar;
  };

  public String CreaComboBuscar(Connection conection, JspWriter out, String formato, String Valor, String buscar) {
          String regresar= Valor;
          String sentencia= "";
    try {
      StringTokenizer st= new StringTokenizer(formato, "~");
                  String[] elementos= new String[st.countTokens()];
                        int contador= 0;
      while (st.hasMoreTokens()) {
        elementos[contador]= st.nextToken();
                                contador++;
      }
      Statement estatuto= conection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
      sentencia= "select * from "+ elementos[0];
      if(elementos.length> 3) {
        sentencia+= " where "+ elementos[3]+ "="+ elementos[4];
      };
      System.out.println("Combo buscar=>"+ sentencia);
                   ResultSet crsTabla= estatuto.executeQuery(sentencia);
      crsTabla.last();
      if(crsTabla.getRow()> 0) {
        crsTabla.beforeFirst();
        while (crsTabla.next() && !crsTabla.getString(elementos[2]).equals(Valor));
        if(!crsTabla.isAfterLast() && crsTabla.getString(elementos[2]).equals(Valor)) {
                      regresar= crsTabla.getString(elementos[1])+","+crsTabla.getString(elementos[2]);
          if(!elementos[1].equals(elementos[2]))
            out.println("<option value=\""+crsTabla.getString(elementos[1])+","+crsTabla.getString(elementos[2])+"\">"+
            buscarDescripcion(conection, buscar, regresarCampos(crsTabla, elementos[2], ' '))+ "</option>");
          else
            out.println("<option value="+crsTabla.getString(elementos[1])+">"+
            buscarDescripcion(conection, buscar, regresarCampos(crsTabla, elementos[1], ' '))+ "</option>");
        }
                          else  {
          crsTabla.beforeFirst();
                             if(crsTabla.next())
                                    regresar= crsTabla.getString(elementos[1])+","+crsTabla.getString(elementos[2]);
                          };
        crsTabla.beforeFirst();
        while (crsTabla.next()) {
          if(!crsTabla.getString(elementos[2]).equals(Valor))
            if(!elementos[1].equals(elementos[2]))
              out.println("<option value=\""+crsTabla.getString(elementos[1])+","+crsTabla.getString(elementos[2])+"\">"+
              buscarDescripcion(conection, buscar, regresarCampos(crsTabla, elementos[2], ' '))+ "</option>");
            else
              out.println("<option value="+crsTabla.getString(elementos[1])+">"+
              buscarDescripcion(conection, buscar, regresarCampos(crsTabla, elementos[1], ' '))+ "</option>");
        }
                        }
                        else {
         out.println("<option value=''>Sin informaci�n</option>");
                        }; // if getRow
                  estatuto.close();
    }
    catch (SQLException eSQL) {
      System.out.println("[CreaComboBuscar] Error: "+ eSQL.getMessage());
    }
    catch (Exception e) {
      System.out.println("[CreaComboBuscar] Error: "+ e.getMessage());
    }
                return regresar;
  };  // CreaComboBuscar

  public String CreaComboBox(Connection conection, JspWriter out, String formato, String Valor) {
          String regresar= Valor;
          String sentencia= "";
    try {
      StringTokenizer st= new StringTokenizer(formato, "~");
                  String[] elementos= new String[st.countTokens()];
                        int contador= 0;
      while (st.hasMoreTokens()) {
         elementos[contador]= st.nextToken();
//				 System.out.println(elementos[contador]);
                                 contador++;
      }
      Statement estatuto= conection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
      sentencia= "select * from "+ elementos[0];
      if(elementos.length> 3) {
        sentencia+= " where "+ elementos[3]+ "="+ elementos[4];
//        System.out.println("Combo especial=>"+ sentencia);
      };
                   ResultSet crsTabla= estatuto.executeQuery(sentencia);
      crsTabla.last();
      if(crsTabla.getRow()> 0) {
        crsTabla.beforeFirst();
        while (crsTabla.next() && !crsTabla.getString(elementos[1]).equals(Valor));
        if(!crsTabla.isAfterLast() && crsTabla.getString(elementos[1]).equals(Valor))
          if(!elementos[1].equals(elementos[2]))
            out.println("<option value="+crsTabla.getString(elementos[1])+">"+ regresarCampos(crsTabla, elementos[2], ' ')+ "</option>");
          else
            out.println("<option value="+crsTabla.getString(elementos[1])+">"+ regresarCampos(crsTabla, elementos[1], ' ')+ "</option>");
                          else  {
          crsTabla.beforeFirst();
                             if(crsTabla.next())
                                    regresar= crsTabla.getString(elementos[1]);
                          };
        crsTabla.beforeFirst();
        while (crsTabla.next()) {
          if(!crsTabla.getString(elementos[1]).equals(Valor))
            if(!elementos[1].equals(elementos[2]))
              out.println("<option value="+crsTabla.getString(elementos[1])+">"+ regresarCampos(crsTabla, elementos[2], ' ')+ "</option>");
            else
              out.println("<option value="+crsTabla.getString(elementos[1])+">"+ regresarCampos(crsTabla, elementos[1], ' ')+ "</option>");
        }
                        }
                        else {
         out.println("<option value=''>Sin informaci�n</option>");
                        }; // if getRow
                  estatuto.close();
    }
    catch (SQLException eSQL) {
      System.out.println("[CreaComboBox] Error: "+ eSQL.getMessage());
    }
    catch (Exception e) {
      System.out.println("[CreaComboBox] Error: "+ e.getMessage());
    }
                return regresar;
  }; // CreaComboBox

  public String ArmaLigas(ResultSet crsTabla) {
          String Ant="-1";
                String Act="-1";
                String Des="-1";
          try {
            Act= crsTabla.getString("CONSECUTIVO");
      if(!crsTabla.isLast()) {
                          crsTabla.next();
                          Des= crsTabla.getString("CONSECUTIVO");
                    crsTabla.previous();
                  };
      if(!crsTabla.isFirst()) {
                    crsTabla.previous();
                          Ant= crsTabla.getString("CONSECUTIVO");
                    crsTabla.next();
                        };
                        Ant= "$"+ Ant+ "#"+ Act+ "&"+ Des+ "~"+ crsTabla.getString("INFERIOR")+ "@"+ crsTabla.getString("SUPERIOR")+ "!";
                }
                catch(Exception e) {
                };
                return Ant;
        };

  public void ajgConceptosCombo(ResultSet crsTabla,JspWriter out, String Tipo) {
    try {
      crsTabla.beforeFirst();
      while (crsTabla.next()) {
              if(crsTabla.getString("TIPO_CONC").equals(Tipo)) {
           out.println("<option value="+crsTabla.getString("TIPO_CONC")+ crsTabla.getString("CONCEPTO")+">["+ crsTabla.getString("TIPO_CONC")+ crsTabla.getString("CONCEPTO")+ "] "+ crsTabla.getString("DESCRIPCION")+"</option>");
         }
      }
    }
    catch (Exception e) {
      System.out.println("[ajgConceptosCombo] Error: "+ e.getMessage());
    }
  }

  public void ajgConceptosExp(ResultSet crsTabla,JspWriter out, String Clave, String Descripcion, int Caracteres, String Cuales) {
    try {
      crsTabla.beforeFirst();
      while (crsTabla.next()) {
              if(crsTabla.getString("TIPO_CONC").equals(Cuales) && crsTabla.getString("FORMULA").indexOf("@")<0) {
          if(Caracteres!=0)
            out.println("<option value="+
            crsTabla.getString(Clave)+ "&"+
            crsTabla.getString("CONSECUTIVO")+ "~"+
            crsTabla.getString("FORMULA")+ "@"+
            crsTabla.getString("PROCEDIMIENTO")+ "%"+
            formatearFecha(crsTabla.getDate("VIGENCIA_DESDE"),"dd/MM/yyyy")+ "!"+
            formatearFecha(crsTabla.getDate("VIGENCIA_HASTA"),"dd/MM/yyyy")+ "?"+
            crsTabla.getString("PERFIL")+ "�"+
            crsTabla.getString("PARTIDA")+
            ">["+ crsTabla.getString(Clave).substring(0, Caracteres)+ "] "+ crsTabla.getString(Descripcion)+"</option>");
                else
            out.println("<option value="+
            crsTabla.getString(Clave)+ "&"+
            crsTabla.getString("CONSECUTIVO")+ "~"+
            crsTabla.getString("FORMULA")+ "@"+
            crsTabla.getString("PROCEDIMIENTO")+ "%"+
            formatearFecha(crsTabla.getDate("VIGENCIA_DESDE"),"dd/MM/yyyy")+ "!"+
            formatearFecha(crsTabla.getDate("VIGENCIA_HASTA"),"dd/MM/yyyy")+ "?"+
            crsTabla.getString("PERFIL")+ "�"+
            crsTabla.getString("PARTIDA")+
            ">["+ crsTabla.getString(Clave)+ "] "+ crsTabla.getString(Descripcion)+"</option>");
        }
      }
    }
    catch (Exception e) {
      System.out.println("[ajgConceptosExp] Error: "+ e.getMessage());
    }
  }

  public void ajgFormulasCombo(ResultSet crsTabla,JspWriter out, String Clave, String Descripcion) {
    try {
      crsTabla.beforeFirst();
      while (crsTabla.next()) {
        out.println("<option value="+crsTabla.getString(Clave)+">"+ crsTabla.getString(Descripcion)+"</option>");
      }; // while
    }
    catch (Exception e) {
      System.out.println("[ajgFormulasCombo] Error: "+ e.getMessage());
    }
  };

  public void ajgConstantesCombo(ResultSet crsTabla,JspWriter out, String Clave, String Descripcion) {
    try {
      crsTabla.beforeFirst();
      while (crsTabla.next()) {
        out.println("<option value="+crsTabla.getString(Clave)+">{"+ crsTabla.getString(Clave)+ "} "+ crsTabla.getString(Descripcion)+"</option>");
      }
    }
    catch (Exception e) {
      System.out.println("[ajgConstantesCombo] Error: "+ e.getMessage());
    }
  };

   // *********** Rutinas de generaci�n de busqueda generada por Alejandro 14/01/03 9:20 a.m.******************* //
   public String ObtieneQuincenas(ResultSet Tabla, String Valor, String Regresa)  {
     String Dato= "00000000000000000000000000";
     try {
       Tabla.beforeFirst();
       while(Tabla.next() && !(Tabla.getString("TIPO_CONC").trim()+Tabla.getString("CONCEPTO").trim()+Tabla.getString("CONSECUTIVO").trim()).equals(Valor.trim()));
       if(!Tabla.isAfterLast() && !Tabla.isBeforeFirst())
         Dato= Tabla.getString(Regresa);
     }
     catch (Exception e) {
       System.out.println("[ObtieneQuincenas] Error: "+ e.getMessage());
     }
     return Dato;
   };

  public void CreaComboConceptos(ResultSet crsTabla,JspWriter out,String Valor) {
    try {
      crsTabla.beforeFirst();
      while (crsTabla.next() && !(crsTabla.getString("TIPO_CONC").trim()+crsTabla.getString("CONCEPTO").trim()+ crsTabla.getString("CONSECUTIVO").trim()).equals(Valor.trim()));
      if((crsTabla.getString("TIPO_CONC").trim()+crsTabla.getString("CONCEPTO").trim()+ crsTabla.getString("CONSECUTIVO").trim()).equals(Valor.trim()))
          out.println("<option value="+("$"+ crsTabla.getString("TIPO_CONC").trim()+ "#"+crsTabla.getString("CONCEPTO").trim()+ "&"+ crsTabla.getString("CONSECUTIVO").trim()+"~" )+ ">"+crsTabla.getString("CONCEPTO")+ ".- "+ crsTabla.getString("DESCRIPCION")+"</option>");
      crsTabla.beforeFirst();
      while (crsTabla.next()) {
        if(!(crsTabla.getString("TIPO_CONC").trim()+crsTabla.getString("CONCEPTO").trim()+ crsTabla.getString("CONSECUTIVO").trim()).equals(Valor.trim()))
          out.println("<option value="+("$"+ crsTabla.getString("TIPO_CONC").trim()+ "#"+crsTabla.getString("CONCEPTO").trim()+ "&"+ crsTabla.getString("CONSECUTIVO").trim()+"~" )+ ">"+crsTabla.getString("CONCEPTO")+ ".- "+ crsTabla.getString("DESCRIPCION")+"</option>");
      }
    }
    catch (Exception e) {
      System.out.println("[CreaComboConceptos] Error: "+ e.getMessage());
    }
  };

    public String convierteFecha(String fechaStr){
      String formatoFecha=fechaStr.substring(6,8)+"/"+fechaStr.substring(4,6)+"/"+fechaStr.substring(0,4);
          return formatoFecha;
    }

    public String convierteMes(int MesStr){
             String nombreMes="";
            switch (MesStr){
        case 1: nombreMes="Enero";
                      break;
                    case 2: nombreMes="Febrero";
                      break;
                    case 3: nombreMes="Marzo";
                      break;
               case 4: nombreMes="Abril";
                       break;
              case 5: nombreMes="Mayo";
                      break;
              case 6: nombreMes="Junio";
                      break;
              case 7: nombreMes="Julio";
                       break;
              case 8: nombreMes="Agosto";
                      break;
              case 9: nombreMes="Septiembre";
                      break;
              case 10: nombreMes="Octubre";
                      break;
              case 11: nombreMes="Noviembre";
                      break;
              case 12: nombreMes="Diciembre";
                      break;
            }
            return nombreMes;
    }

        public String fechaHoy(boolean espaniol) {
                SimpleDateFormat formato= new SimpleDateFormat(espaniol? "yyyyMMdd": "dd/MM/yyyy");
    Date fecha = new Date();
    String dateString= formato.format(fecha);
                if(espaniol)
                  dateString= DespliegaFecha(dateString);
                return dateString;
        };

  public String creaComboBox(Connection conection, JspWriter out, String formato, String Valor, boolean blanco) {
          String regresar= blanco? "": Valor;
          String sentencia= "";
    try {
      StringTokenizer st= new StringTokenizer(formato, "~");
                  String[] elementos= new String[st.countTokens()];
                        int contador= 0;
      while (st.hasMoreTokens()) {
         elementos[contador]= st.nextToken();
//				 System.out.println(elementos[contador]);
                                 contador++;
      }
      Statement estatuto= conection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
      sentencia= "select * from "+ elementos[0];
      if(elementos.length> 3) {
        if(elementos.length> 5)
          sentencia+= " where "+ elementos[3]+ elementos[5]+ elementos[4];
        else
          sentencia+= " where "+ elementos[3]+ "="+ elementos[4];
      };
//      System.out.println("Combo especial=>"+ sentencia);
                   ResultSet crsTabla= estatuto.executeQuery(sentencia);
      crsTabla.last();
      if(crsTabla.getRow()> 0) {
        crsTabla.beforeFirst();
        while (crsTabla.next() && !crsTabla.getString(elementos[1]).equals(Valor));
        if(!crsTabla.isAfterLast() && crsTabla.getString(elementos[1]).equals(Valor)) {
          out.println("<option value=\""+crsTabla.getString(elementos[1])+"\">"+ regresarCampos(crsTabla, elementos[2], ' ')+ "</option>");
          regresar= Valor;
        }
                          else  {
          crsTabla.beforeFirst();
                             if(crsTabla.next())
                                    regresar= crsTabla.getString(elementos[1]);
                          };
        crsTabla.beforeFirst();
        while (crsTabla.next()) {
          if(!crsTabla.getString(elementos[1]).equals(Valor))
            out.println("<option value=\""+crsTabla.getString(elementos[1])+"\">"+ regresarCampos(crsTabla, elementos[2], ' ')+ "</option>");
        }; // while
                        }
                        else {
         out.println("<option value=''>Sin informaci�n</option>");
                        }; // if getRow
                  estatuto.close();
    }
    catch (SQLException eSQL) {
      System.out.println("[CreaComboBox] Error: "+ eSQL.getMessage());
    }
    catch (Exception e) {
      System.out.println("[CreaComboBox] Error: "+ e.getMessage());
    }
                return regresar;
  };

  public String tranformaFecha(String fecha, int tipoFormato) {
    String s = "";
    GregorianCalendar calendario= new GregorianCalendar();
    int xAnio= Integer.parseInt(fecha.substring(0, 4));
    int xMes = Integer.parseInt(fecha.substring(4, 6))- 1;
    int xDia = Integer.parseInt(fecha.substring(6, 8));
    calendario.set(xAnio, xMes, xDia);
    switch(tipoFormato) {
      case 0:
        s= getNombreDia(calendario.get(calendario.DAY_OF_WEEK))+ ", "+ calendario.get(calendario.DATE)+ " de "+
           getNombreMes(calendario.get(calendario.MONTH))+ " del "+ calendario.get(calendario.YEAR);
        break;
      case 1:
        s= calendario.get(calendario.DATE)+ " dias del mes de "+
           getNombreMes(calendario.get(calendario.MONTH))+ " del "+ calendario.get(calendario.YEAR);
        break;
      case 2:
        s = fecha.substring(6, 8)+ "/"+ fecha.substring(4, 6)+ "/"+ fecha.substring(0, 4);
        break;
      case 3:
        s= getNombreDia(calendario.get(calendario.DAY_OF_WEEK))+ ", "+ fecha.substring(6, 8)+ "/"+
           fecha.substring(4, 6)+ "/"+ fecha.substring(0, 4);
        break;
      case 4:
        s= calendario.get(calendario.DATE)+ "/"+ getNombreMes(calendario.get(calendario.MONTH))+ "/"+ fecha.substring(0, 4);
        break;
      case 5:
        s= getNombreDia(calendario.get(calendario.DAY_OF_WEEK))+ ", "+ fecha.substring(6, 8)+ "/"+
           getNombreMes(calendario.get(calendario.MONTH))+ "/"+ fecha.substring(0, 4);
        break;
    }; // switch
    return s;
  }; // tranformaFecha

  public String comboQuery(Connection conection, JspWriter out, String sentencia, String oculto, String despliega, String comparacion, String valor) {
          String regresar= valor;
    try {
      Statement estatuto= conection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
//      System.out.println("Combo especial=>"+ sentencia);
                   ResultSet crsTabla= estatuto.executeQuery(sentencia);
      crsTabla.last();
      if(crsTabla.getRow()> 0) {
        crsTabla.beforeFirst();
        while (crsTabla.next() && !crsTabla.getString(comparacion).equals(valor));
        if(!crsTabla.isAfterLast() && crsTabla.getString(comparacion).equals(valor)) {
          out.println("<option value=\""+regresarCampos(crsTabla, oculto, ',')+"\">"+ regresarCampos(crsTabla, despliega, ' ')+ "</option>");
          regresar= valor;
        }
                          else  {
          crsTabla.beforeFirst();
                             if(crsTabla.next())
                                    regresar= crsTabla.getString(comparacion);
                          };
        crsTabla.beforeFirst();
        while (crsTabla.next()) {
          if(!crsTabla.getString(comparacion).equals(valor))
            out.println("<option value=\""+regresarCampos(crsTabla, oculto, ',')+"\">"+ regresarCampos(crsTabla, despliega, ' ')+ "</option>");
        }; // while
                        }
                        else {
         out.println("<option value=''>Sin informaci�n</option>");
                        }; // if getRow
                  estatuto.close();
    }
    catch (SQLException eSQL) {
      System.out.println("[comboQuery] Error: "+ eSQL.getMessage());
    }
    catch (Exception e) {
      System.out.println("[comboQuery] Error: "+ e.getMessage());
    }
                return regresar;
  };

}; // funciones