/**
 *  TreeLeaf.java
 */
package com.coolservlets.beans.tree;

public class TreeLeaf extends TreeObject {

  public TreeLeaf(String name, String link, String rama, String imageIn, String imageOver, String imageOut) {
    super(0, name, link, rama, imageIn, imageOver, imageOut, Tree.LEAF);
  }

} 
