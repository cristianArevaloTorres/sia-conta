package sia.xml;

import java.io.File;
import java.io.IOException;

import java.lang.reflect.Field;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

//import org.apache.log4j.Logger;

import org.w3c.dom.Document;

import org.xml.sax.SAXException;

import sia.libs.formato.Variables;
import sia.libs.recurso.StringBean;


public class Dml {

    //private static Logger log= Logger.getLogger(Dml.class);
    
    public static final int CONTABILIDAD = 0;
    public static final int PRESUPUESTO = 1;
    public static final int TESORERIA = 2;
    public static final int ADQUISICIONES = 3;
    public static final int SEGURIDAD = 4;
    
    private static final String[] DEFINICIONES = 
    { "contabilidad.xml", "presupuesto.xml", "tesoreria.xml", "adquisiciones.xml", "seguridad.xml" };
    
    private Document documento = null;
    public static final int DELETE = 0;
    public static final int INSERT = 1;
    public static final int SELECT = 2;
    public static final int UPDATE = 3;
    
    private final String EXP_INI = "/dml/submodulo[@id='";
    private final String DEL_EXP = "']/delete[@id='";
    private final String INS_EXP = "']/insert[@id='";
    private final String SEL_EXP = "']/select[@id='";
    private final String UPD_EXP = "']/update[@id='";
    
    protected boolean nulosComoString = false;
    
    public Dml() throws ParserConfigurationException, SAXException, IOException {
        this(SEGURIDAD);
    }
    
    public Dml(boolean nulosComoString) throws ParserConfigurationException, SAXException, IOException {
          this(SEGURIDAD);
          setNulosComoString(nulosComoString);
    }  
    
    public Dml(int archivo) throws ParserConfigurationException, SAXException, IOException {
        String ruta = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        ruta = ruta.substring(0, ruta.indexOf("classes")+8);
        StringBuilder path = new StringBuilder(ruta);
        path.append(DEFINICIONES[archivo]);
        DocumentBuilderFactory fabrica = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = fabrica.newDocumentBuilder();
        setDocumento(builder.parse(new File(path.toString()).toURL().toString()));
    }
    
    public Dml(int archivo, boolean nulosComoString) throws ParserConfigurationException, SAXException, IOException {
        setNulosComoString(nulosComoString);
      String origen = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
    //  System.err.println("origen antes: "+origen);
      //log.error("origen antes:"+origen); 
      origen = origen.substring(0,origen.indexOf("classes")+8);
      //System.err.println("origen despues: "+origen);
      //log.error("origen despues:"+origen); 
      StringBuilder path = new StringBuilder(origen);
        //System.err.println("path: "+path.toString());
      //log.error("path: "+path.toString()); 
        path.append(DEFINICIONES[archivo]);
      //  System.err.println("path2: "+path.toString());
      //log.error("path2: "+path.toString()); 
        DocumentBuilderFactory fabrica = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = fabrica.newDocumentBuilder();
        setDocumento(builder.parse(new File(path.toString()).toURL().toString()));
    }
    
    public Dml(String archivo) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory fabrica = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = fabrica.newDocumentBuilder();
        setDocumento(builder.parse(archivo));
    }
    
    public void setNulosComoString( boolean nulosComoString ) {
        this.nulosComoString = nulosComoString;
    }
    
    public boolean isNulosComoString() {
        return nulosComoString;
    }  
    
    public String getDelete(String subModulo, String id, Map parametros) throws XPathExpressionException {
        return evaluate(EXP_INI.concat(subModulo).concat(DEL_EXP).concat(id).concat("']"), parametros);
    }
    
    public String getDelete(String subModulo, String id, String parametros, char token) throws XPathExpressionException {
        Variables variables = new Variables(parametros, token);
        return getDelete(subModulo, id, variables.getMap());
    }
    
    public String getDelete(String subModulo, String id, String parametros) throws XPathExpressionException {
        return getDelete(subModulo, id, parametros, '|');
    }
    
    public String getInsert(String subModulo, String id, Map parametros) throws XPathExpressionException {
        return evaluate(EXP_INI.concat(subModulo).concat(INS_EXP).concat(id).concat("']"), parametros);
    }
    
    public String getInsert(String subModulo, String id, String parametros, char token) throws XPathExpressionException {
        Variables variables = new Variables(parametros, token);
        return getInsert(subModulo, id, variables.getMap());
    }
    
    public String getInsert(String subModulo, String id, String parametros) throws XPathExpressionException {
        return getInsert(subModulo, id, parametros, '|');
    }
    
    public String getSelect(String subModulo, String id) throws XPathExpressionException {
        return evaluate(EXP_INI.concat(subModulo).concat(SEL_EXP).concat(id).concat("']"));
    }
    
    public String getSelect(String subModulo, String id, Map parametros) throws XPathExpressionException {
        return evaluate(EXP_INI.concat(subModulo).concat(SEL_EXP).concat(id).concat("']"), parametros);
    }
    
    public String getSelect(String subModulo, String id, String parametros, char token) throws XPathExpressionException {
        Variables variables = new Variables(parametros, token);
        return getSelect(subModulo, id, variables.getMap());
    }
    
    public String getSelect(String subModulo, String id, String parametros) throws XPathExpressionException {
        return getSelect(subModulo, id, parametros, '|');
    }
    
    public String getUpdate(String subModulo, String id, Map parametros) throws XPathExpressionException {
        return evaluate(EXP_INI.concat(subModulo).concat(UPD_EXP).concat(id).concat("']"), parametros);
    }
    
    public String getUpdate(String subModulo, String id, String parametros, char token) throws XPathExpressionException {
        Variables variables = new Variables(parametros, token);
        return getUpdate(subModulo, id, variables.getMap());
    }
    
    public String getUpdate(String subModulo, String id, String parametros) throws XPathExpressionException {
        return getUpdate(subModulo, id, parametros, '|');
    }  
    
    public String getDML(String subModulo, String id, Map parametros, int tipoDML) throws XPathExpressionException {
        switch (tipoDML) {
        case DELETE:
          return getDelete(subModulo, id, parametros);
        case INSERT:
          return getInsert(subModulo, id, parametros);
        case SELECT:
          return getSelect(subModulo, id, parametros);
        case UPDATE:
          return getUpdate(subModulo, id, parametros);
        }
        return null;
    }
    
    public String getDML(String subModulo, String id, String parametros, char token, int tipoDML) throws XPathExpressionException {
        Variables variables = new Variables(parametros, token);
        return getDML(subModulo, id, variables.getMap(), tipoDML);
    }
    
    public String getDML(String subModulo, String id, String parametros, int tipoDML) throws XPathExpressionException {
        return getDML(subModulo, id, parametros, '|', tipoDML);
    }
    
    /**
     * Obtiene la sentecia DML y establece los valores a los parametros que vienen en el StringBean
     * @param subModulo Atributo 'id' del elemento 'submodulo'.
     * @param id Atributo 'id' del elemento 'insert, delete, update o select'.
     * @param tipoDML tipo de sentencia DMLL 'insert, delete, update o select'.
     * @param strBean interface que tiene los atributos de donde se va a obtener
     *                a partir de los parametros de la sentencia el valor.
     * @return Sentencia DML solicitada ya con los valores
     * @throws XPathExpressionException
     */
    public String getDML(String subModulo, int tipoDML, String id, StringBean strBean) throws XPathExpressionException {
        Map paramsValue = null;
        String dml = getDML(subModulo, id, paramsValue, tipoDML);
        paramsValue = strBean.parametros(getParametersName(dml));
        return getDML(subModulo, id, paramsValue, tipoDML);
    }
    
    /**
     * Obtiene la sentecia DML y establece los valores a los parametros que
     * vienen en el StringBean
     * @param propiedad cadena formada jerarquicamente por los elementos para
     *                  llegar a la sentencia con la siguiente estructura:
     *                  idSubmodulo.tipoSentenciaDML.idSentencia
     * @param strBean
     * @return
     * @throws XPathExpressionException
     * @throws Exception
     */
    public String getDML(String propiedad, StringBean strBean) throws XPathExpressionException, Exception {
        String[] elementos = propiedad.split("[.]");
        if (elementos.length == 3) {
          try {
            Field campo = this.getClass().getField(elementos[1].toUpperCase());
            String sql = getDML(elementos[0], campo.getInt(null), elementos[2], strBean);
            return sql;
          }
          catch (NoSuchFieldException e) {
            throw new Exception("La propiedad " + elementos[1] + " no es una propiedad valida");
          }
        }
        return "";
    }
    
    /**
     * Obtiene un java.util.List de los parametros que requiere la sentecia DML
     * @param sentenciaDml sentecia recuperada del XML
     * @return Nombre de parametros que se encuentran en la sentencia
     */
    public List getParametersName(String sentenciaDml) {
        Pattern patron = Pattern.compile("'\\d+:\\d+'");
        Matcher match = patron.matcher(sentenciaDml);
        sentenciaDml = match.replaceAll("");
        List<String> lista = new Vector();
        String param;
        do {
          match = Pattern.compile(":\\w+").matcher(sentenciaDml);
          if (match.find()) {
            param = sentenciaDml.substring(match.start() + 1, match.end());
            if (!lista.contains(param))
              lista.add(param);
            sentenciaDml = match.replaceFirst("");
          }
        } while (match.find());
        return lista;
    }
    
    private String evaluate(String expresion, Map parametros) throws XPathExpressionException {
        return replaceParameter(evaluate(expresion), parametros);
    }
    
    private String evaluate(String expresion) throws XPathExpressionException {
        String regresa = null;
        XPathFactory xPFabrica = XPathFactory.newInstance();
        XPath xPath = xPFabrica.newXPath();
        if(getDocumento() == null)
          throw new XPathExpressionException("No se encontro el archivo especificado");
        regresa = xPath.evaluate(expresion, getDocumento()); 
        if(regresa == null || regresa.equals(""))
          throw new XPathExpressionException("No se encontro la sentencia indicada en el xml");
        return regresa;
    }
    
    protected String replaceParameter(String sql, Map parametros) {
        sql = sql.replaceAll("\\\\", "\\\\\\\\").replaceAll("\\$", "\\\\\\$");
         if (parametros != null) {
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
                      if(parametros.get(param)!=null)
                        sbDml.append(match.replaceFirst(parametros.get(param).toString().replaceAll("\\\\", "\\\\\\\\").replaceAll("\\$", "\\\\\\$")));
                      else
                          sbDml.append(match.replaceFirst("null"));
                  }
                  else
                    sbDml.append(linea);
                    
             }
             sql = sbDml.toString();
             if (!nulosComoString)
                sql = sql.replaceAll("'null'","null");
         }// if
         return sql.trim();
    }
    
    protected void setDocumento(Document doumento) {
         this.documento = doumento;
    }
    
    public Document getDocumento() {
        return documento;
    }
    
    public static void main(String... args) throws Exception {
        Dml dml = new Dml(Dml.TESORERIA);
        Map mapa =new HashMap();
        mapa.put("0","para:metro1");
        mapa.put("1","para:metro2");
        String sql = dml.getSelect("movimientos", "existenciaMovimientos", mapa);
        System.out.println(sql);
        dml.setNulosComoString(true);
        sql = dml.getSelect("movimientos", "existenciaMovimientos", mapa);
        System.out.println(sql);
    }


}

