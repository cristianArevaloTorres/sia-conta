/**
 *  TreeNode.java
 */
package com.coolservlets.beans.tree;

public class TreeNode extends TreeObject implements TreeInterface {

  private boolean visible;
  private Tree children;

  public TreeNode(int id, String name,  String rama, String imageIn, String imageOver, String imageOut) {
    this(id, name, null, rama, imageIn, imageOver, imageOut);
  }

  public TreeNode(int id, String name, String link, String rama, String imageIn, String imageOver, String imageOut) {
    super(id, name, link, rama, imageIn, imageOver, imageOut, Tree.NODE);
    setVisible(false);
    children = new Tree();
  }

  public void addChild(TreeObject child) {
    child.setParent(this);
    children.addChild(child);
  }

  public void setVisible(boolean value) {
    visible = value;
  }

  public boolean isVisible() {
    return visible;
  }

  public void toggleVisible() {
    visible = !visible;
  }

  public Tree getChildren() {
    return children;
  }
  
}
