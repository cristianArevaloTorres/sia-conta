/**
 *  TreeObject.java
 */
package com.coolservlets.beans.tree;

import java.util.HashMap;
import java.util.Map;

public class TreeObject {

  private int id;
  private String name;
  private String link;
  private String rama;
  private String imageIn;
  private String imageOver;
  private String imageOut;
  private int type;
  private TreeObject parent;

  public TreeObject(int id, String name, String link, String rama, String imageIn, String imageOver, String imageOut, int type) {
    setId(id);
    setName(name);
    setLink(link);
    setRama(rama);
    setImageIn(imageIn);
    setImageOut(imageOut);
    setImageOver(imageOver);
    setType(type);
    setParent(null);
  }

  public int getId() {
    return this.id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
  
  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }

  public java.lang.String getRama() {
    return rama;
  }

  public void setRama(java.lang.String rama) {
    this.rama = rama;
  }

  public java.lang.String getImageIn() {
    return imageIn;
  }

  public void setImageIn(java.lang.String imageIn) {
    this.imageIn = imageIn;
  }

  public String getImageOut() {
    return imageOut;
  }

  public void setImageOut(String imageOut) {
    this.imageOut = imageOut;
  }

  public String getImageOver() {
    return imageOver;
  }

  public void setImageOver(String imageOver) {
    this.imageOver = imageOver;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public TreeObject getParent() {
    return parent;
  }

  public void setParent(TreeObject parent) {
    this.parent = parent;
  }

  public String getUrl() {
    StringBuffer sb= new StringBuffer();
    String params  = "id="+ getId()+ "&rama=".concat(getRama());
    if(getLink()!= null && getLink().trim().length()> 0) {
      sb.append(getLink().trim());
      if(getLink().indexOf("?")> 0)
        sb.append("&");
      else
        sb.append("?");
      sb.append(params);
    } // if
    return sb.toString();
  }
    
  public Map toMap() {
    Map regresar = new HashMap();
    regresar.put("text", getName());
    regresar.put("url", getUrl());
    regresar.put("imageIn", getImageIn()==null?"Librerias/Imagenes/Outlook/sinicono.png":getImageIn());
    //regresar.put("imageIn", getImageIn()==null?"sinicono":getImageIn());
    regresar.put("imageOver", getImageOver());
    regresar.put("imageOut", getImageOut());
//    regresar.put("imageOver", getImageOver()==null?"Librerias/Imagenes/Outlook/sinicono.png":getImageIn());
 //   regresar.put("imageOut", getImageOut()==null?"Librerias/Imagenes/Outlook/sinicono.png":getImageIn());
    return regresar;
  }

} 
