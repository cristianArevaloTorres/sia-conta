package sia.rf.contabilidad.reportes.catalogoCuentas;

import java.io.File;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.sql.SQLException;


public class documentoXml {
  
  private String file;
  private List<HashMap<String, HashMap<String, String>>> tags;

  public documentoXml(String file) throws SQLException, Exception {
    this.file= file;
    this.tags= new ArrayList<HashMap<String, HashMap<String, String>>>();
    init();
  }
  
   void init()throws SQLException, Exception {
    tags.add(new HashMap<String, HashMap<String, String>>(){{
      put("Catalogo", new HashMap<String, String>() {{ 
        put("Version", "1.0.0.1");
        put("RFC", "INEGI");
        put("TotalCtas", "X");
        put("Mes", "07");
        put("Anio", "2014");
      }});
    }});
      Connection con=null;
      Statement stQuery=null;
      ResultSet rsQuery=null;
      String totalReg=null;
      try{
       Class.forName("oracle.jdbc.driver.OracleDriver");
       con = DriverManager.getConnection("jdbc:oracle:thin:@10.1.8.207:1521:sia_pru","rf_contabilidad","c4nt1b3l3d1d");  
       //con=DaoFactory.getContabilidad();
       stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
       StringBuffer SQL=new StringBuffer("SELECT a.cod_agrup,a.numcta,a.descripcion,a.subctade,a.nivel,a.natur"); 
       SQL.append(" FROM rf_tc_catalogo_digital a ");
       System.out.println(SQL.toString());
       rsQuery = stQuery.executeQuery(SQL.toString());
       int contador=0;
       while (rsQuery.next()){
           Map<String, HashMap<String, String>> etiqueta= new HashMap<String, HashMap<String, String>>();
           Map<String, String> atributos                = new HashMap<String, String>();
           etiqueta.put("Ctas", (HashMap)atributos); 
           atributos.put("CodAgrup", rsQuery.getString("cod_agrup"));
           atributos.put("NumCta", rsQuery.getString("numcta"));
           atributos.put("Desc", rsQuery.getString("descripcion"));
           atributos.put("subctade", rsQuery.getString("subctade"));
           atributos.put("nivel", rsQuery.getString("nivel"));
           atributos.put("natur", rsQuery.getString("natur"));
           tags.add((HashMap)etiqueta);
           contador++;
       }//while rsQuery.next()
       totalReg= Integer.toString(contador);
       tags.get(0).get("Catalogo").put("TotalCtas",totalReg);
      }catch(Exception e){
          //System.out.println("Ocurrio un error al accesar al metodo init() "+e.getMessage());
          e.printStackTrace();
      }finally{
        if (rsQuery!=null){
          rsQuery.close();
          rsQuery=null;
        }
        if (stQuery!=null){
          stQuery.close();
          stQuery=null;
        }
      } //Fin finally
 
  }
  
  private void atributos(Document document, Element father, Map<String, String> attributes) {
    for(String key: attributes.keySet()) {
      Attr attribute = document.createAttribute(key);
      // aqui buscar los valores para cada etiqueta y su atributo segun aplique
      attribute.setValue(attributes.get(key));
      father.setAttributeNode(attribute);
    } // for  
  }

  private void etiquetas(Document document, Element father) {
    for(HashMap<String, HashMap<String, String>> item: tags) {
      for(String key: item.keySet()) {
        Element child= document.createElement(key);
        father.appendChild(child);
        atributos(document, child, item.get(key));//se pasa como parámetros el mapa de los atributos/child=etiqueta/ item.get(key)= atrigubos
      } // for
    } // for
  }
  
  private void guardar(String target, Document documento) throws TransformerConfigurationException, TransformerException {
    // write the content into xml file
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = transformerFactory.newTransformer();
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    //transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
    DOMSource source = new DOMSource(documento);
    StreamResult result = new StreamResult(new File(target));
    // Output to console for testing
    // StreamResult result = new StreamResult(System.out);
    transformer.transform(source, result);
  } 
  
  public void construir() {
    try {
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
      // root elements
      Document doc = docBuilder.newDocument();
      Element root = doc.createElement("estructura");
      doc.appendChild(root);
      etiquetas(doc, root);
      guardar(this.file, doc);
      System.out.println("Archivo generado de forma exitosa en ".concat(this.file).concat("!"));
    } // try
    catch (Exception e) {
      e.printStackTrace();
    } // catch
  }
  
  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) throws SQLException, Exception {
    documentoXml documento= new documentoXml("d:\\test.xml");
    documento.construir();
  } // main
  
}
