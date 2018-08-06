/*
 * Date of code 8/04/2008
 */
package sia.menu.outlook;

import com.coolservlets.beans.tree.Tree;
import com.coolservlets.beans.tree.TreeObject;
import sia.db.dao.DaoFactory;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import sia.libs.formato.Error;

/**
 *
 * @author alejandro.jimenez
 */
public class MenuSection extends MenuOutLook {

  public MenuSection(Tree tree) {
    super(tree);
  }
    
  protected int getInteger(String key) {
    int regresar = 0;
    String value = null;
    try {
      value = Outlook.getInstance().getPropiedad(key);
      regresar = Integer.parseInt(value);
    }
    catch (Exception e) {
      regresar = 0;
    }
    return regresar;
  }

  protected String getString(String key) {
    String regresar = null;
    try {
      regresar = Outlook.getInstance().getPropiedad(key);
    }
    catch (Exception e) {
      regresar = null;
    }
    return regresar;
  }

  private String addCharacters(String caracter, int tope) {
    StringBuffer caracteres= new StringBuffer();
    for(int x= 0; x< tope; x++) 
      caracteres.append(caracter);
    return caracteres.toString();
  }; // replicarCaracter
  
  private String deleteCharacters(String rama, String caracteres) {
    int inc= caracteres.length();
    boolean ciclo= true;
    while(rama!= null && rama.length()> 0 && ciclo) {
      if(rama.length()> inc)
        if(caracteres.equals(rama.substring(rama.length()- inc)))
          rama= rama.substring(0, rama.length()- inc);
        else
          ciclo= false;
      else
        rama= "";
    } // while 
    return rama;
  }; // eliminarCaracter

  public String findSection(HttpSession session, HttpServletRequest request) throws Exception {
    StringBuffer regresar= new StringBuffer();
    String rama          = request.getParameter("rama");
    String clave         = null;
    Connection connection= null; 
    Statement stm        = null;
    ResultSet rst        = null;
    try {
      if (rama!= null && rama.length()> 0) {
        int inc = getInteger("section.level.length");
        int tope= rama.length();
        connection= DaoFactory.getConnection();
        stm = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        rst = null;
        rama = deleteCharacters(rama, addCharacters(getString("section.level.mask"), inc));
        int contador= 0;
        while (rama != null && rama.length() > 0) {
          clave= rama.concat(addCharacters(getString("section.level.mask"), (rama.length()> tope ? 0: tope - rama.length())));
          rst  = stm.executeQuery("select clave, des from menus where clave='".concat(clave).concat("'"));
          if (rst.first()) 
            if(rama.length()> inc* 2)
              regresar.insert(0, "<a href=\"table.jsp?rama=".concat(rst.getString("CLAVE")).concat("\" target=\"").concat(getString("section.level.frame")).concat("\" style=\"cursor:hand\"><strong>").concat(rst.getString("DES")).concat("</strong></a>"));
            else
              regresar.insert(0, rst.getString("DES"));
          if (rama.length()> inc) 
            rama= rama.substring(0, rama.length() - inc);
          else 
            rama= "";
          if (rama.length()> 0) 
            regresar.insert(0, " _ ");
          contador++;
        } // while 
        if (regresar.toString().length() > 0) 
          session.setAttribute("seccion", new String(regresar.toString()));
      } // if  
    }
    catch(Exception e) {
      Error.mensaje(e, "SIAFM");
    }
    finally {
      if(rst!= null)
        rst.close();
      rst= null;
      if(stm!= null)
        stm.close();
      stm= null;
      if(connection!= null)
        connection.close();
      connection= null;
    }
    return regresar.toString();
  } //  findSection
  /**
   * Modificado por Mike 24/Junio/2008, innecesario el paso del parámetro session  
   **/
  public String findParent(HttpServletRequest request) throws Exception {
    HttpSession session = request.getSession(false);
    StringBuffer regresar= new StringBuffer();
    String rama          = request.getParameter("rama");
    try {
      if (rama!= null && rama.length()> 0) {
        TreeObject node=  getNode(getTree(), rama);
        while(node!= null) {
          if(node.getType()== Tree.NODE)
            regresar.insert(0, "<a href=\"table.jsp?rama=".concat(node.getRama()).concat("\" target=\"").concat(getString("section.level.frame")).concat("\" style=\"cursor:hand\"><strong>").concat(node.getName()).concat("</strong></a>"));
          else
            regresar.insert(0, "<a href=\"".concat(node.getUrl()).concat("\" target=\"").concat(getString("section.level.frame")).concat("\" style=\"cursor:hand\"><strong>").concat(node.getName()).concat("</strong></a>"));
          regresar.insert(0, " _ ");
          node= node.getParent();
        } // while
        if (regresar.toString().length()> 0) 
          session.setAttribute("seccion", new String(regresar.toString()));
      } // if
    }
    catch(Exception e) {
      Error.mensaje(e, "SIAFM");
    }
    return regresar.toString();
  } //  findParent

  /**
   * Modificado por Mike 24/Junio/2008, innecesario el paso del parámetro request ya que no se usa
   **/
  public String paintSection(HttpSession session) throws Exception {
    String seccion= "";
    if(session.getAttribute("seccion")!= null) 
      seccion= (String)session.getAttribute("seccion");
    return seccion;
  } // paintSection

  public String getMask() {
    return addCharacters(getString("section.level.mask"), getInteger("section.level.max")* getInteger("section.level.length"));
  }
  
}
