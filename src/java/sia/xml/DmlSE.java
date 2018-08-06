package sia.xml;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

//import org.apache.log4j.BasicConfigurator;
//import org.apache.log4j.Level;
//import org.apache.log4j.Logger;

import org.xml.sax.SAXException;

public class DmlSE extends Dml {


//private static Logger log= Logger.getLogger(DmlSE.class);
 





  private static final Modulo[] MODULOS = { new Modulo("contabilidad","rf/contabilidad"),
      new Modulo("presupuesto","rf/presupuesto"),new Modulo("tesoreria","rf/tesoreria"),
      new Modulo("adquisiciones","rf/adquisiciones"),new Modulo("seguridad","sg/seguridad"),
      new Modulo("juridico","ij"),new Modulo("biblioteca","ij")};

  public DmlSE(int tipoConexion) throws ParserConfigurationException,
                                        SAXException, IOException {
    super(tipoConexion);
  }
  
  public DmlSE(int archivo,String modulo) throws ParserConfigurationException, SAXException, IOException {
   //   log.setLevel(Level.INFO);
       //BasicConfigurator.configure();

      String origen = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
      // System.err.println("origen antes:"+origen);
      //log.error("origen antes:"+origen); 
      origen = origen.substring(0,origen.indexOf("classes")+8);
       //System.err.println("origen despues:"+origen);
    //log.error("origen despues:"+origen); 
      StringBuilder path = new StringBuilder(origen);
      path.append("sia/").append(MODULOS[archivo].getRuta()).append("/xml/").append(modulo).append(".xml");
    //  System.err.println("path:"+path.toString());
    //log.error("path:"+path.toString()); 
     DocumentBuilderFactory fabrica = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = fabrica.newDocumentBuilder();
      setDocumento(builder.parse(new File(path.toString()).toURL().toString()));
  }

  public String replaceParameter(String sql, Map parametros) {
    sql = sql.replaceAll("\\\\", "\\\\\\\\").replaceAll("\\$", "\\\\\\$");
     //if (parametros != null) {
         Pattern patron = Pattern.compile("'\\d+:\\d+'");
         Matcher match = patron.matcher(sql);
         List<String> lista = new ArrayList();
         int fin=0;       
         do {
            //match = Pattern.compile(":[a-zA-Z]\\w*").matcher(sql);
            match = Pattern.compile(":\\w+").matcher(sql);
            if (match.find()){
                fin = match.end();
                lista.add(sql.substring(0,fin));
                sql = sql.substring(fin);
             }             
         } while (match.find());
         lista.add(sql);   
         String param;
         StringBuffer sbDml = new StringBuffer();
         for (String linea:lista)  {
             
             match = Pattern.compile(":\\w+").matcher(linea);
             if (match.find()) {
                  param = linea.substring(match.start() + 1, match.end());
                  //if(!param.toUpperCase().equals("SS") && !param.toUpperCase().equals("MI")) {
                    if(parametros!=null && parametros.get(param)!=null)
                      sbDml.append(match.replaceFirst(parametros.get(param).toString().replaceAll("\\\\", "\\\\\\\\").replaceAll("\\$", "\\\\\\$")));
                    else
                      if(!param.toUpperCase().equals("SS") && !param.toUpperCase().equals("MI"))
                        sbDml.append(match.replaceFirst(""));
                      else
                        sbDml.append(match.replaceFirst((param.toUpperCase().equals("SS")?":ss":":mi").replaceAll("\\\\", "\\\\\\\\").replaceAll("\\$", "\\\\\\$")));
                  //}
              }
              else
                sbDml.append(linea);
           //System.out.println(sbDml.toString());
                
         }
         sql = sbDml.toString();
         if (!nulosComoString)
            sql = sql.replaceAll("'null'","");
     //}// if
     return sql.trim();
  }
}
