/*
 * Date of code 5/04/2008
 */

package sia.menu.outlook;

import com.coolservlets.beans.tree.Tree;
import com.coolservlets.beans.tree.TreeObject;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author alejandro.jimenez
 */
public class TableOutlook extends MenuOutLook {
  
  private int id;
  private int columns;
  private String rama;
  private HttpServletRequest request;

  public TableOutlook(int id, int columns, Tree tree) {
    super(tree);
    this.id     = id;
    this.columns= columns;
  }

  public TableOutlook(String rama, int columns, Tree tree) {
    super(tree);
    this.rama   = rama;
    this.columns= columns;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getColumns() {
    return columns;
  }

  public void setColumns(int columns) {
    this.columns = columns;
  }

  public String getRama() {
    return rama;
  }

  public void setRama(String rama) {
    this.rama = rama;
  }

  @Override
  public String render() {
    StringBuffer sb= new StringBuffer();
    Tree children  = null;
    if(detectKey()) 
      children= getChildren(getTree(), getId());
    else
      children= getChildren(getTree(), getRama());
    sb.append("<table align=\"center\" cellspacing=\"10pt\" cellpadding=\"10pt\"  border=\"0\" width = \"100%\">");
    int contador = 0;
    int rows    = getRows(children);
    for (int y= 0; y < rows; y++) {
      sb.append("<tr>");
      for (int x = 0; x < getColumns(); x++) {
        boolean show= children!= null && contador< children.size();
        sb.append("<td align=\"center\" height=\"160\" valign=\"bottom\" style=\"font: 8pt arial; "+ (show? "cursor: hand;": "") +"\" width=\""+ 100/getColumns() + "%\"  >");
        if(show) {
          TreeObject node= (TreeObject)children.getChild(contador);
          String name    = "option_"+ contador;
          if (node.getImageIn()==null)
            node.setImageIn("Librerias/Imagenes/Outlook/sinicono.png"); // gif
        /*  else 
            if (!node.getImageIn().contains("."))
              node.setImageIn("images/".concat(node.getImageIn()).concat(".png")); //gif*/
          sb.append("<div id=\"".concat(name).concat("\" onclick=\"window.location.href='"));
          sb.append(node.getUrl().concat("';\" style=\"width:100%; height:100% valign=bottom ").concat(show?"cursor: hand;": ""));
          //sb.append("\" onmouseout=\"imageOut('".concat(name).concat("','").concat(node.getImageIn()).concat("')\" "));
          sb.append("\" onmouseout=\"imageOut('".concat(name).concat("',1.5").concat(")\" "));
          sb.append("onmouseover=\"imageOver('".concat(name));
          //sb.append("','".concat(node.getImageIn()!=null? node.getImageIn().substring(0,node.getImageIn().indexOf(".")).concat("_gde.png')\""):node.getImageIn()));         
          sb.append("',1.5)  \"");
          sb.append(" title=\"".concat(node.getName()).concat("\">"));
          sb.append("    <img id=\"_".concat(name).concat("\" width=\"64\"; height=\"64\" src='").concat(node.getImageIn()).concat("'  />"));
          sb.append("    <table align=\"center\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\">");
          sb.append("      <tr>");
          sb.append("      <td align=\"center\" valign=\"bottom\" style=\"font: 8pt tahoma;\" >");
          sb.append(node.getName());
          sb.append("      </td>");
          sb.append("     </tr>");
          sb.append("    </table>");
          sb.append("  </div>");
        }
        else
          sb.append("");
        sb.append("</td>");
        contador++;
      } // for x
      sb.append("</tr>");
    } // for y
    sb.append("</table>");
    return sb.toString();
  }


  private boolean detectKey() {
    return getRama()== null;
  }

  private int getRows(Tree children) {
    if(children== null)
      return -1;
    else  
      return (int)(children.size()/ getColumns())+ 1;
  }

}
