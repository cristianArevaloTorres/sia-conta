package com.coolservlets.beans;

import sia.db.dao.DaoFactory;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.coolservlets.beans.tree.TreeNode;
import com.coolservlets.beans.tree.TreeLeaf;
import com.coolservlets.beans.tree.TreeObject;
import com.coolservlets.beans.tree.Tree;
import sun.jdbc.rowset.CachedRowSet;

/**
 * A TreeBean is a server-side JSP JavaBean that generates trees populated from a JDBC
 * data source, and renders the trees as HTML. State information is maintained using URI
 * parameters.
 */

public class TreeBean {

	private Hashtable trees    = new Hashtable();
	private Hashtable openNodes= new Hashtable();	// holds an easy-lookup of open nodes while rendering HTML
	private ResultSet treeRst;
	private String treeName;						// unique name of this tree
  private String tableName;	// name of the (denormalised) table or view
  private String key;				      		// comma separated list of columns that hold labels
	private String url;// comma separated list of corresponding urls
  private String imageIn;				      // comma separated list of images from labels
	private String imageOver;				    // comma separated list of images from labels
	private String imageOut;	  	      // comma separated list of images from labels
  private String text;
	private String whereClause= "";
  private int children = 2;
	private String treePage;						// Which page to send tree URL links to
	private String leafPage;						// Which page to send tree leaf links to
	private String leafTarget;						// Which frame to send non-tree URL links to
	private String openImage="/Librerias/Imagenes/abierto.gif";			// image for open nodes
	private String closedImage="/Librerias/Imagenes/cerrado.gif";		// image for closed nodes
	private String leafImage="/Librerias/Imagenes/hoja.gif";				// image for leaf nodes
	private String treeStyle;						// Text style of the table
	private int absopen;			// what - no abs()??
	private int open      = 0;			// Passed by URL to this bean stating which node to open (or close)
	private String oldopen= "";	// Automatically maintained URL parameter listing open nodes
	private boolean reload= false;
        
        private String cuenta = null;

	// Getter and Setter methods
  public ResultSet	getTreeResultSet() { return this.treeRst; }
  public void		setTreeResultSet(ResultSet rst) { this.treeRst= rst;}
  public String	getKey() { return this.key; }
  public void		setKey(String str) { this.key = str; }
  public String	getUrl() { return this.url; }
  public void		setUrl(String str) { this.url = str; }
  public String	getImageIn() {  return this.imageIn;  }
  public void		setImageIn(String str) { this.imageIn = str; }
  public String	getImageOver() { return this.imageOver; }
  public void		setImageOver(String str) { this.imageOver = str; }
  public String	getImageOut() { return this.imageOut; }
  public void		setImageOut(String str) { this.imageOut = str; }
  public String	getTreeName() { return this.treeName; }
  public void		setTreeName(String str) { this.treeName = str; }
  public int getChildren() { return this.children; }
  public void setChildren(int x) { this.children=x; }
  public String	getTableName() { return this.tableName; }
  public void	setTableName(String str) { this.tableName = str; }
  public String	getText() { return this.text; }
  public void	setText(String str) { this.text = str; }
  public String	getWhereClause() { return this.whereClause; }
  public void	setWhereClause(String str) { this.whereClause = str; }
	public String getTreePage() { return treePage; }
	public void setTreePage(String treePage) { this.treePage = treePage; }
	public String getLeafPage() { return leafPage; }
	public void setLeafPage(String leafPage) { this.leafPage = leafPage; }
	public String getLeafTarget() { return leafTarget; }
	public void setLeafTarget(String leafTarget) { this.leafTarget = leafTarget; }
	public String getTreeStyle() { return treeStyle; }
	public void setTreeStyle(String treeStyle) { this.treeStyle = treeStyle; }
	public int	getOpen() { return open; }
	public void setOpen(int open) { this.open = open; absopen = open < 0 ? -open : open; }
	public String getOldopen() { return oldopen; }
	public void setOldopen(String oldopen) { this.oldopen = oldopen; }
	public boolean	getReload() { return reload; }
	public void setReload(boolean reload) { this.reload = reload; }
	public void setOpenImage(String str) { this.openImage = str; }
	public void	setClosedImage(String str) { this.closedImage = str; }
	public void	setLeafImage(String str) { this.leafImage = str; }

	// Now on to the real stuff...

	/**
	 * Populates the tree specified in the bean parameter 'treeName', or the default tree
	 * if none was specified. The trees are held statically in a Hashtable, keyed on the tree
	 * name.
	 *
	 * A tree is populated from a denormalised table or a view using JDBC. Ordinarily, this operation
	 * happens only once when the tree is first accessed.
	 *
	 * This method should probably be reorganised to lend itself more to populating from sources other
	 * than JDBC.
	 */

  private synchronized String fieldImage(ResultSet rst, String name) {
    String regresar= null, clave = null;
    try {
       clave= rst.getString(getKey());
      if(rst.findColumn(name)> 0)
        regresar= rst.getString(name);
    } 
    catch (Exception e) {
      regresar= null;
    }
    return regresar;
  } // fieldImage
  
  public synchronized boolean populateJdbc(ResultSet rs2) {
    if(rs2== null)
  		System.out.println("resultset nulo -1-");

		boolean sqlerror = false;
		/** Try to get the named tree from the Hashtable. If no name is provided, use 'default'.
			If a tree matches, then it must already be populated.
		*/
		if(treeName == null || treeName.length() == 0) {
		    setTreeName("default");
		}
		if(trees.get(treeName) != null && reload == false) {
			return true;		// already populated
		}
    if(rs2== null) {
  		System.out.println("resultset nulo -2-");
      // Get a database connection
      Connection connection= null;
      Statement statement  = null;
     try {
        connection= DaoFactory.getConnection();
			  statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
			  StringBuffer sql= new StringBuffer(300);
        sql.append("select distinct * from ").append(getTableName()).append(" ");
        sql.append(whereClause).append(" order by ").append(getKey()).append(" asc");
        CachedRowSet modulos= new CachedRowSet();
        modulos.populate(statement.executeQuery(sql.toString()));
        rs2 = modulos;
		  }
		  catch(Exception e) {
        System.out.println("[com.beans.tree.TreeBean.populateJdbc] Error: "+ e);
			  e.printStackTrace();
			  return false;
		  }
      finally {
        try {
          if(statement!= null)
            statement.close();
          statement= null; 
          if(connection!= null)
            connection.close();
          connection= null; 
        } catch (Exception e) {
          System.out.println("[com.beans.tree.TreeBean.populateJdbc] Error: "+ e);
          e.printStackTrace();
        }
      } // try
    }
    else{
      try {
        rs2.beforeFirst();
      }
      catch(Exception e) {
        System.out.println("[com.beans.tree.TreeBean.populateJdbc] Error: "+ e);
        e.printStackTrace();
      }
    } // if

    int nodeID   = 1;
    TreeNode tn[]= new TreeNode[30];
    Tree root    = new Tree("root");
		try {
   		String[] descripciones = new String[100];	// hardwired maximum depth of 30 levels (!!)
      String[] rutas     = new String[100];
      String[] ramaArbol = new String[100];
      String[] imagenIn  = new String[100];
      String[] imagenOver= new String[100];
      String[] imagenOut = new String[100];
      String clave="";
      String token="";
      int ini;
      int fin;
      int numNiveles = 1;
      int x;
      int j=0;
      String ceros ="";
      for (int z=1; z<=children; z++) ceros+= "0";
      while (rs2.next()) {
        x   = 0;
        ini = 0;
        fin = children;
        clave= rs2.getString(getKey());
        token= clave.substring(ini,fin);
        while (!token.equals(ceros)) {
          x++;
          if (x>numNiveles)
            numNiveles++;
          ini+= children;
          fin+= children;
          if (fin<= clave.length())
            token= clave.substring(ini,fin);
          else
            token= ceros;
        } // while
        if (x>0) x--;
        if (ini>= children) ini-= children;
        if (fin>= children) fin-= children;
        token = clave.substring(ini,fin);
        descripciones[x]= rs2.getString(getText());
        rutas[x]     = rs2.getString(getUrl());
        ramaArbol[x] = rs2.getString(getKey());
        imagenIn[x]  = fieldImage(rs2, getImageIn());
        imagenOver[x]= fieldImage(rs2, getImageOver());
        imagenOut[x] = fieldImage(rs2, getImageOut());
        int y=x+1;
        while (y<numNiveles){
          descripciones[y]= null;
          rutas[y]= null;
          ramaArbol[y]= null;
          y++;
        } // while
        String comparaClave="";
        String comparaToken="";
        if (rs2.next()) {
          comparaClave = rs2.getString(getKey());
          comparaToken = comparaClave.substring(ini,fin);
          rs2.previous();
        }
        else {
          rs2.last();
          comparaToken=" ";
        }
        if((x==numNiveles) | (!token.equals(comparaToken))) {
          try {
            for(j=0; j < x; j++) {
  					  //if(tn[j] == null || !ramaArbol[j].equals(tn[j].getName())) {	// then this is the first or a new item
  					  if(tn[j] == null || tn[j].getRama()== null || !ramaArbol[j].equals(tn[j].getRama())) {	// then this is the first or a new item
      						tn[j] = new TreeNode(nodeID++, descripciones[j], rutas[j], ramaArbol[j], imagenIn[j], imagenOver[j], imagenOut[j]);
						    if(j > 0)
							    tn[j-1].addChild(tn[j]);
						    else
						  	  root.addChild(tn[j]);
						   continue;
					   } // if
    	     } // for
		    	 if(j > 0)
					   tn[j-1].addChild(new TreeLeaf(descripciones[j], rutas[j], ramaArbol[j], imagenIn[j], imagenOver[j], imagenOut[j]));
				   else
					   root.addChild(new TreeLeaf(descripciones[j], rutas[j], ramaArbol[j], imagenIn[j], imagenOver[j], imagenOut[j]));
          }
          catch(Exception e) {
            System.out.println("[com.beans.tree.TreeBean.populateJdbc] Error: "+ e);
            e.printStackTrace();
          } // try
        } // if
      } // while
      trees.put(treeName, root);	// Store the tree in the static Hashtable
		} 
    catch(SQLException e) {
      System.out.println("[com.beans.tree.TreeBean.populateJdbc] Error: "+ e);
			e.printStackTrace();
			sqlerror = true;
		} // try
		return !sqlerror;
	}

	/**
	 * This method is just a call to the method below with a null argument, meaning that the tree
	 * shouldn't get it's state from the session.
	 */
	public final String renderHTML() {
		return renderHTML((HttpServletRequest)null);
	}

	public final String renderHTML(ResultSet rst) {
  	//System.out.println("ResultSet ok");
    setTreeResultSet(rst);
		return renderHTML((HttpServletRequest)null);
	}

	/**
	 * This method first of all builds a Hashtable of open nodes from the request parameter
	 * for easy lookup later. It then submits the root node of the tree named in the bean
	 * parameter 'treeName' (or the default tree) for rendering as HTML. If a HttpServletRequest
	 * is supplied, the method will try to obtain '<treeName>.oldopen' from the session.
	 */
	public final String renderHTML(HttpServletRequest req) {
		if(populateJdbc(getTreeResultSet()) == false)	{		// ensures the tree is populated
			return "<p><strong>The TreeBean has not been successfully initialised. Check the server's stdout for details.</strong></p>";
		}
		// Try to obtain 'oldopen' from the session if a request object was passed
		if(req != null) {
			HttpSession s= req.getSession(false);
      String tmp   = s!= null? (String)s.getAttribute(treeName + ".oldopen") : null;
			if(tmp != null && tmp.length() > 0)
				oldopen = tmp;
		}
		// Make a hash table of the currently open nodes (from the URL parameter)
		StringTokenizer st = new StringTokenizer(oldopen, ":");
		String token;
		while(st.hasMoreElements()) {
			try {
				token = st.nextToken();
				openNodes.put(Integer.valueOf(token), token);
				// strb.append(token).append(":");
			} 
      catch(NumberFormatException e) {
				System.out.println("[com.beans.tree.TreeBean.renderHTML] Warning: TreeBean was passed dodgy parameters! "+ e);
        e.printStackTrace();
			}
		}
		// now add the requested node to the open list (if not a close-request)
		if(open >= 0) {
			openNodes.put(new Integer(open), String.valueOf(open));
			// strb.append(open);
		} else {
			openNodes.remove(new Integer(-open));
		}
		StringBuffer strb = new StringBuffer();
		Enumeration on = openNodes.elements();
		while(on.hasMoreElements()) {
			strb.append(on.nextElement()).append(":");
		} // while
		this.oldopen = strb.toString();
		// Try to write 'oldopen' to the session if a request object was passed
		if(req != null) {
			HttpSession s = req.getSession(false);
			if(s != null)
				s.setAttribute(treeName + ".oldopen", oldopen);
		} // if
		StringBuffer html= new StringBuffer(renderHTML((Tree)trees.get(treeName)));
    String tabla     = "\n<table border=\"0\" width=\"100%\"";
		if(treeStyle != null && treeStyle.length() > 0)
			tabla= tabla.concat(" class=\"").concat(treeStyle).concat("\"");
		tabla= tabla.concat(">\n");
    html.insert(0, tabla);
    html.append("\n</table>\n");
		return html.toString();
	}

	public final String renderHTML( HttpServletRequest req, ResultSet rst ) {
    setTreeResultSet(rst);
    return renderHTML(req);
  }

	private boolean isVisible(TreeNode node) {
		if(openNodes.containsKey(new Integer(node.getId())))
			return true;
		return false;
	}

	/**
	 * Renders the specified tree as HTML by iterating over all it's children. Will call itself
	 * recursively for any visible node children.
	 */
	private final String renderHTML(Tree tree) {
		StringBuffer buf = new StringBuffer();
		buf.append("\n<table border=\"0\"");
		if(treeStyle != null && treeStyle.length() > 0)
		 	buf.append(" class=\"").append(treeStyle).append("\"");
		buf.append(">\n");
		for(int i=0; i < tree.size(); i++) {
			TreeObject treeObject = tree.getChild(i);
			if(treeObject.getType()== Tree.NODE) {
				TreeNode node = (TreeNode)treeObject;
				buf.append(renderNodeHTML(node, tree.size()== i+ 1));
				if(isVisible(node)) {
					buf.append("<tr>");
					buf.append("<td>&nbsp;&nbsp;&nbsp;</td>");
					buf.append("<td>").append(renderHTML(node.getChildren())).append("</td>");
					buf.append("</tr>\n");
				} // if
			}
			else {
				TreeLeaf leaf = (TreeLeaf)treeObject;
				buf.append(renderLeafHTML(leaf, tree.size()== i+ 1));
			} // if
		}
		buf.append("\n</table>\n");
		return buf.toString();
	}
  
  /*
   * Verificar si el nodo tiene una imagen propia para pintarla
   *
   */
  private String checkImage(String imagen, String generica) {
    if(imagen== null || imagen.length()== 0)
      return generica;
    else  
      return imagen;
  }
  
	/**
	 * Renders the specified tree node as HTML.
	 */
	private final String renderNodeHTML(TreeNode node, boolean isLast) {
		StringBuffer buf = new StringBuffer();
		boolean visible = isVisible(node);
		buf.append("<tr style=\"cursor:hand\">");
		buf.append("<td><a ");
		if(absopen== node.getId())	// active link reference
			buf.append("name='A' ");
		buf.append("href='").append(treePage).append("?");
		buf.append("oldopen=").append(oldopen).append("&open=");
	  buf.append(visible? -(node.getId()): node.getId());
		buf.append("' target=\"_self\">");
		if(visible)
			buf.append("<img src='").append(checkImage(node.getImageIn(), openImage)).append("' border='0'>");
		else 
			buf.append("<img src='").append(closedImage).append("' border='0'>");
		buf.append("</a>");
		buf.append("&nbsp;&nbsp;&nbsp;");
		buf.append("</td>");
		buf.append("<td>");
		String link = node.getLink();
		if(link != null && link.length()> 0) {
			buf.append( "<a href='");
			if(leafPage != null && leafPage.length()> 0)	// if a leafPage is specified, then prepend to the link
				buf.append(leafPage);
			buf.append(link).append("' ");
			if(leafTarget != null && leafTarget.length() > 0)
        if(node.getName()!= null && node.getName().toLowerCase().indexOf("cerrar sesi")>= 0)
				  buf.append("target='_parent'");
        else 
				  buf.append("target='").append(leafTarget).append("'");
			buf.append(">").append( node.getName() ).append("</a>");
		}
		else
			buf.append( node.getName() );
		buf.append("</td>");
		buf.append("</tr>\n");
		return buf.toString();
	}
  
  /**
	 * Renders the specified tree leaf as HTML.
	 */
	private final String renderLeafHTML(TreeLeaf leaf, boolean isLast) {
  	StringBuffer buf = new StringBuffer();
		buf.append( "<tr>");
    buf.append( "<td>").append("<img src='").append(checkImage(leaf.getImageIn(), leafImage)).append("' border='0'>");
		buf.append("&nbsp;&nbsp;&nbsp;");
    buf.append("</td>");
    buf.append("<td>\n");
    if(leaf.getLink()!= null && leaf.getLink().length()> 0) { // si el link es diferente de null colocarlo
      buf.append("<a href='");
  		if(leafPage != null && leafPage.length() > 0)	// if a leafPage is specified, then prepend to the link
	  		buf.append(leafPage);
	  	buf.append(leaf.getLink());
      buf.append(leaf.getLink().indexOf("?")>= 0? "&": "?");
      buf.append("rama=");
      buf.append(leaf.getRama());
      buf.append("' ");
  		if(leafTarget != null && leafTarget.length()> 0)
	  		buf.append("target='").append(leafTarget).append("'");
	  	buf.append(">").append( leaf.getName() ).append("</a>");
    }
    else {
      buf.append(leaf.getName());
    } // if
    buf.append("</td>");
    buf.append("</tr>\n");
		return buf.toString();
	}

	/**
	 * Accepts a comma separated list of tokens and returns them as a string array.
	 */
	private String[] csvToArray(String csv) {
		StringTokenizer st = new StringTokenizer(csv,",");
		String[] buf = new String[st.countTokens()];
		int i = 0;
		while(st.hasMoreTokens())
			buf[i++] = st.nextToken();
		return buf;
	}

	public String adjustScrollPosition() {
		return "<script> window.location.href=\"#A\"; if(window.scrollTo) scrollTo(0,(document.body ? document.body.scrollTop : pageYOffset) - 20); </script>";
	}

  public Tree getTree() {
    if(getTreeName()!= null)
      return (Tree)trees.get(getTreeName());
    else
      return null;
  }
  
    //*********************************************************************************************
    //*********************************************************************************************

    public final String renderScript(ResultSet rst) {
        setTreeResultSet(rst);
        return renderScript((HttpServletRequest)null);
    }

    public final String renderScript(HttpServletRequest req) {

        if (populateJdbc(getTreeResultSet()) == 
            false) { // ensures the tree is populated
            return "<p><strong>The CDTreeBean has not been successfully initialised. Check the server's stdout for details.</strong></p>";
        }

        // Try to obtain 'oldopen' from the session if a request object was passed
        if (req != null) {
            HttpSession s = req.getSession(false);
            String tmp = 
                s != null ? (String)s.getAttribute(treeName + ".oldopen") : 
                null;
            if (tmp != null && tmp.length() > 0)
                oldopen = tmp;
        }
        // Make a hash table of the currently open nodes (from the URL parameter)
        StringTokenizer st = new StringTokenizer(oldopen, ":");
        String token;
        while (st.hasMoreElements()) {
            try {
                token = st.nextToken();
                openNodes.put(Integer.valueOf(token), token);
                // strb.append(token).append(":");
            } catch (NumberFormatException e) {
                System.out.println("[com.beans.tree.CSTreeBean.populateJdbc] Warning: TreeBean was passed dodgy parameters! " + 
                                   e + "[" + getCuenta() + "]");
            }
        }
        // now add the requested node to the open list (if not a close-request)
        if (open >= 0) {
            openNodes.put(new Integer(open), String.valueOf(open));
            // strb.append(open);
        } else {
            openNodes.remove(new Integer(-open));
        }
        StringBuffer strb = new StringBuffer();
        Enumeration on = openNodes.elements();
        while (on.hasMoreElements()) {
            strb.append(on.nextElement()).append(":");
        }
        this.oldopen = strb.toString();
        // Try to write 'oldopen' to the session if a request object was passed
        if (req != null) {
            HttpSession s = req.getSession(false);
            if (s != null)
                s.setAttribute(treeName + ".oldopen", oldopen);
        }
        StringBuffer script = new StringBuffer("\n var NODOS_ARBOL = [");
        script.append(renderCoolTree((Tree)trees.get(treeName)));
        script.replace(script.lastIndexOf(","),script.lastIndexOf(",")+1,";");
        return script.toString();
    }

    /**
    * Pinta los nodos que requiere CoolTree
    * @param tree
    * @return nodos
    */
    private final String renderCoolTree(Tree tree) {

        StringBuffer buf = new StringBuffer();

        for (int i = 0; i < tree.size(); i++) {
            TreeObject treeObject = tree.getChild(i);
            if (treeObject.getType() == Tree.NODE) {
                TreeNode node = (TreeNode)treeObject;
                buf.append(renderNodeCoolTree(node));
                //if (isVisible(node)) {
                    buf.append(renderCoolTree(node.getChildren()));
                //}
            } else {
                TreeLeaf leaf = (TreeLeaf)treeObject;
                buf.append(renderLeafCoolTree(leaf));
            }
        }
        buf.replace(buf.lastIndexOf(","),buf.lastIndexOf(","),"]");
        buf.append("\n");
        return buf.toString();
    }

    /**
     * Pinta Nodo
     * @param node
     * @return nodo
     */
    private final String renderNodeCoolTree(TreeNode node) {
        StringBuffer buf = new StringBuffer();
        buf.append("[");

        //Texto de la Rama
        buf.append("\"").append(node.getName()).append("\",");
        
        String link = node.getLink();
        if (link != null && link.length() > 0) {
            buf.append("\"");
            if (leafPage != null && 
                leafPage.length() > 0) // if a leafPage is specified, then prepend to the link
                buf.append(leafPage);
            buf.append(link).append("\" ");

            if (leafTarget != null && leafTarget.length() > 0)
                buf.append(",");
                buf.append("\"").append(leafTarget).append("\",");
                formatImage(node, buf);
                buf.append(",");
        } else {            
            buf.append("null,null,");
            formatImage(node, buf);
            if (node.getChildren()!=null)
                buf.append(",");
            else
                buf.append("]");
        }

        buf.append("\n");
        return buf.toString();
    }

    /**
         * Renders the specified tree leaf as HTML.
         */
    private final String renderLeafCoolTree(TreeLeaf leaf) {
        StringBuffer buf = new StringBuffer();
        
        //Texto de la Rama
        buf.append("[\"").append(leaf.getName()).append("\",");
        
        //URL
        if (leaf.getLink() != null && leaf.getLink().length() > 0) { // si el link es diferente de null colocarlo
            buf.append("\"");
            if (leafPage != null && leafPage.length() > 0) // if a leafPage is specified, then prepend to the link
                buf.append(leafPage);
            buf.append(leaf.getLink());
            buf.append(leaf.getLink().indexOf("?") >= 0 ? "&" : "?");
            buf.append("rama=");
            buf.append(leaf.getRama());
            buf.append("\",");
        } else 
            buf.append("null,");            
        //Target
        if (leafTarget != null && leafTarget.length() > 0){
            buf.append("\"").append(leafTarget).append("\",");
            formatImage(leaf, buf);
        }
        else{
            buf.append("null,");
            formatImage(leaf, buf);
        }            
        buf.append("],");
        return buf.toString();
    }

    private void formatImage(TreeObject node, StringBuffer buf) {
      buf.append("{format:{eimages:[");
//      buf.append(node.getImageIn()!=null?"\"".concat(node.getImageIn()).concat("\","):"\"Librerias/Imagenes/cooltree/folder.gif\",");
//      buf.append(node.getImageOut()!=null?"\"".concat(node.getImageOut()).concat("\","):"\"Librerias/Imagenes/cooltree/folderopen.gif\",");
//      buf.append(node.getImageOver()!=null?"\"".concat(node.getImageOver()).concat("\","):"\"Librerias/Imagenes/cooltree/page.gif\",");
      buf.append("\"Librerias/Imagenes/cooltree/folder.gif\",");
      buf.append("\"Librerias/Imagenes/cooltree/folderopen.gif\",");
      buf.append("\"Librerias/Imagenes/cooltree/page.gif\",");
      
      /*
      buf.append("\"Librerias/Imagenes/cooltree/folder.gif\",\"Librerias/Imagenes/cooltree/folderopen.gif\",\"Librerias/Imagenes/cooltree/page.gif\",");
      */
      buf.append("\"Librerias/Imagenes/cooltree/minus.gif\",\"Librerias/Imagenes/cooltree/minusbottom.gif\",\"Librerias/Imagenes/cooltree/plus.gif\",");
      buf.append("\"Librerias/Imagenes/cooltree/plusbottom.gif\",\"Librerias/Imagenes/cooltree/line.gif\",");
      buf.append("\"Librerias/Imagenes/cooltree/join.gif\",\"Librerias/Imagenes/cooltree/joinbottom.gif\"");
      buf.append("]}}");
    }


    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }

    public String getCuenta() {
        return cuenta;
    }
}
