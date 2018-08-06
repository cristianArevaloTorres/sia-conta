/*
 * Date of code 5/04/2008
 */
package sia.menu.outlook;

import com.coolservlets.beans.tree.Tree;
import com.coolservlets.beans.tree.TreeNode;
import com.coolservlets.beans.tree.TreeObject;
import sia.libs.formato.Formatos;

/**
 *
 * @author alejandro.jimenez
 */
public class MenuOutLook {

  private Tree tree;

  public MenuOutLook(Tree tree) {
    this.tree = tree;
  }

  protected Tree getTree() {
    return tree;
  }

  protected void setTree(Tree tree) {
    this.tree = tree;
  }

  protected int getPropertie(String key) {
    int regresar= 0;
    String value= null;
    try {
      value   = Outlook.getInstance().getPropiedad(key);
      regresar= Integer.parseInt(value);
    }
    catch(Exception e) {
      regresar= 0;
    }
    return regresar;
  }
  
  public String render() {
    StringBuffer sb = new StringBuffer();
    int tope  = getPropertie("outlook.tope");
    int start = getPropertie("outlook.cycle.start");
    int end   = getPropertie("outlook.cycle.end");
    int first = getPropertie("outlook.first.level");
    int second= getPropertie("outlook.second.level");
    int x     = 0;
    while(x< tope) {
      String value= Outlook.getInstance().getPropiedad("outlook.line."+ x);
      if(x== start) {
        for (int i = 0; i < getTree().size(); i++) {
          TreeObject item= (TreeObject)getTree().getChild(i);
          int y= x;
          while(y<= end) {
            value= Outlook.getInstance().getPropiedad("outlook.line."+ y);
            if(y== first) {
              Formatos formatos= new Formatos(value, item.toMap());
              value= formatos.getFormateada();
            }
            else
              if(y== second) {
                Tree level= getChildren(getTree(), item.getId());
                if(level!= null) {
                  for (int j = 0; j< level.size(); j++) {
                    TreeObject subItem = (TreeObject)level.getChild(j);
                    value= Outlook.getInstance().getPropiedad("outlook.line."+ y);
                    Formatos formatos= new Formatos(value, subItem.toMap());
                    value= formatos.getFormateada();
                    sb.append(value);
                    if(j!= level.size()- 1)
                      sb.append(",");
                    sb.append("\n");
                  } // for
                } // if  
                value= "";
              } // if
            sb.append(value);
            sb.append("\n");
            y++;
          } // while y
          value= "";
          if(i!= getTree().size()- 1)
            sb.append(",");
        } // for i
        x= end;
      } // if
      sb.append(value);
      sb.append("\n");
      x++;
    } // for x
    return sb.toString();
  }  
  
  protected final Tree getChildren(Tree tree, int id) {
		Tree regresar= null;
    for(int i=0; i< tree.size() && regresar== null; i++) {
			TreeObject treeObject = tree.getChild(i);
      if(treeObject.getType()== Tree.NODE) {
        TreeNode node= (TreeNode)treeObject;
        if(node.getId()!= id)
          regresar= getChildren(node.getChildren(), id);
        else
          return node.getChildren();
      } // if
    } // for   
    return regresar;
  }

  protected final Tree getChildren(Tree tree, String rama) {
		Tree regresar= null;
    for(int i=0; i< tree.size() && regresar== null; i++) {
			TreeObject treeObject = tree.getChild(i);
      if(treeObject.getType()== Tree.NODE) {
        TreeNode node= (TreeNode)treeObject;
        if(!node.getRama().equals(rama))
          regresar= getChildren(node.getChildren(), rama);
        else
          return node.getChildren();
      } // if
    } // for   
    return regresar;
  }

  protected final TreeObject getNode(Tree tree, String rama) {
		TreeObject regresar= null;
    for(int i=0; i< tree.size() && regresar== null; i++) {
			TreeObject treeObject = tree.getChild(i);
      if(treeObject.getRama().equals(rama))
        regresar= treeObject;
      else
        if(treeObject.getType()== Tree.NODE) {
          TreeNode node= (TreeNode)treeObject;
          if(!node.getRama().equals(rama))
            regresar= getNode(node.getChildren(), rama);
        } // if
    } // for   
    return regresar;
  }
  
  @Override
  public void finalize() {
    tree= null;
  }
  
}
