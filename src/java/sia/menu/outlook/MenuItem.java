/*
 * Date of code 7/04/2008
 */

package sia.menu.outlook;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author alejandro.jimenez
 */
public class MenuItem {
  
  private int id;
  private String text;
  private String url;
  private String imagenIn;
  private String imagenOver;
  private String imagenOut;
  private boolean father;

  public MenuItem(int id, String text, String url, String imagenIn, String imagenOver, String imagenOut, boolean father) {
    this.id = id;
    this.text = text;
    this.url = url;
    this.imagenIn = imagenIn;
    this.imagenOver = imagenOver;
    this.imagenOut = imagenOut;
    this.father = father;
  }

  public MenuItem() {
    this(-1, null, null, null, null, null, false);
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public boolean isFather() {
    return father;
  }

  public void setFather(boolean father) {
    this.father = father;
  }

  public String getImagenIn() {
    return imagenIn;
  }

  public void setImagenIn(String imagenIn) {
    this.imagenIn = imagenIn;
  }

  public String getImagenOut() {
    return imagenOut;
  }

  public void setImagenOut(String imagenOut) {
    this.imagenOut = imagenOut;
  }

  public String getImagenOver() {
    return imagenOver;
  }

  public void setImagenOver(String imagenOver) {
    this.imagenOver = imagenOver;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public Map toMap() {
    Map regresar= new HashMap();
    regresar.put("text", getText());
    regresar.put("url", getUrl());
    regresar.put("imageIn", getImagenIn());
    regresar.put("imageOver", getImagenOver());
    regresar.put("imageOut", getImagenOut());
    regresar.put("coma", ",");
    return regresar;
  }
  
}
