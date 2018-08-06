package sia.libs.fun.tabla;

public class Img {
  
  public static final int IMG_AGREGAR       = 1;
  public static final int IMG_MODIFICAR     = 2;
  public static final int IMG_ELIMINAR      = 3;
  public static final int IMG_CONSULTAR     = 4;
  public static final int IMG_IMPRIMIR      = 5;
  public static final int PALOMA_GRIS       = 6;
  public static final int IMG_DOCUMENTO     = 7;
  public static final int IMG_PDF           = 8;
  public static final int IMG_TRANSFER      = 9;
  public static final int IMG_HTML          = 10;
  public static final int IMG_SEG_CONSULTAR = 11;
  public static final int IMG_SEG_USUARIOS  = 12;
  public static final int IMG_SEG_ARBOL_MENU = 13;
  public static final int IMG_AGREGAR_DES   = -1;
  public static final int IMG_MODIFICAR_DES = -2;
  public static final int IMG_ELIMINAR_DES  = -3;
  public static final int IMG_CONSULTAR_DES = -4;
  public static final int IMG_REGRESARNIV   = -5;
  public static final int IMG_TRANSFER_DES   = -6;
  public static final int IMG_DOCUMENTO_DES   = -7;
  private int imagen;
  private String paginaDestino;
  private String param;
  private String campoParam;
  private boolean nuevaVentana;
  private String alt;
  private String onClick;
  
  private String campoCondicion;
  private String valorCondicion;
  
  
  private String accion;
  private String rutaImagen;
  
  
  public Img(String paginaDestino,int imagen, String param, String campoParam, boolean nuevaVentana, String alt) {
    this(paginaDestino,imagen,param,campoParam,nuevaVentana,alt,null,null,null);
  }
  
  public Img(String paginaDestino,int imagen, String param, String campoParam, boolean nuevaVentana, String alt, String onClick) {
    this(paginaDestino,imagen,param,campoParam,nuevaVentana,alt,onClick,null,null);
  }
  
  public Img(String paginaDestino,int imagen, String param, String campoParam, 
              boolean nuevaVentana, String alt, String onClick, String campoCondicion, String valorCondicion) {
    setPaginaDestino(paginaDestino);
    setImagen(imagen);
    setParam(param);
    setCampoParam(campoParam);
    setNuevaVentana(nuevaVentana);
    setAlt(alt);
    setOnClick(onClick);
    setCampoCondicion(campoCondicion);
    setValorCondicion(valorCondicion);
  }

  public void setImagen(int imagen) {
    this.imagen = imagen;
  }

  public int getImagen() {
    return imagen;
  }

  public void setPaginaDestino(String paginaDestino) {
    this.paginaDestino = paginaDestino;
  }

  public String getPaginaDestino() {
    return paginaDestino;
  }

  public void setParam(String param) {
    this.param = param;
  }

  public String getParam() {
    return param;
  }

  public void setCampoParam(String campoParam) {
    this.campoParam = campoParam;
  }

  public String getCampoParam() {
    return campoParam;
  }
  
  public String getAccion() {
    if(accion == null) {
      StringBuffer regresa = new StringBuffer();
      regresa.append("accion=");
      switch(imagen) {
        case IMG_AGREGAR : regresa = regresa.append(IMG_AGREGAR); break;
        case IMG_MODIFICAR : regresa = regresa.append(IMG_MODIFICAR); break;
        case IMG_CONSULTAR : regresa = regresa.append(IMG_CONSULTAR); break;
        case IMG_ELIMINAR : regresa = regresa.append(IMG_ELIMINAR); break;
      }
      accion = regresa.toString();
      regresa.setLength(0);
      regresa = null;
    } 
    return accion;
  }
  
  public String getRutaImagen() {
    if(rutaImagen == null) {
      switch(imagen) {
        case IMG_AGREGAR : rutaImagen= "Librerias/Imagenes/botonAgregar2.gif"; break;
        case IMG_MODIFICAR : rutaImagen= "Librerias/Imagenes/botonEditar2.gif"; break;
        case IMG_CONSULTAR : rutaImagen= "Librerias/Imagenes/botonConsultar2.gif"; break;
        case IMG_ELIMINAR : rutaImagen= "Librerias/Imagenes/botonEliminar2.gif"; break;
        case IMG_IMPRIMIR : rutaImagen= "Librerias/Imagenes/botonImpresora.gif"; break;
        case PALOMA_GRIS  : rutaImagen= "Librerias/Imagenes/palomaGris.gif"; break;
        case IMG_DOCUMENTO  : rutaImagen= "Librerias/Imagenes/botonDocto.gif"; break;
        case IMG_PDF: rutaImagen= "Librerias/Imagenes/botonPdf.gif"; break;
        case IMG_TRANSFER: rutaImagen= "Librerias/Imagenes/botonTransferirInfo.gif"; break;
        case IMG_HTML: rutaImagen= "Librerias/Imagenes/botonDocto.gif"; break;
        case IMG_REGRESARNIV:rutaImagen= "Librerias/Imagenes/botonRegresarNivel.gif"; break;
        case IMG_TRANSFER_DES:rutaImagen= "Librerias/Imagenes/botonTransferirInfoDesac.gif"; break;
        case IMG_DOCUMENTO_DES:rutaImagen= "Librerias/Imagenes/botonDoctoDesac2.gif"; break;
        case IMG_MODIFICAR_DES:rutaImagen= "Librerias/Imagenes/botonEditarDesac2.gif"; break;
        case IMG_CONSULTAR_DES:rutaImagen= "Librerias/Imagenes/botonConsultarDesac2.gif"; break;
        case IMG_SEG_CONSULTAR: rutaImagen= "Librerias/Imagenes/perfiles.gif"; break;
        case IMG_SEG_USUARIOS: rutaImagen= "Librerias/Imagenes/usuarios.gif"; break;     
        case IMG_SEG_ARBOL_MENU: rutaImagen= "Librerias/Imagenes/botonDistribucion.png"; break;     
        
      }
    } 
    return rutaImagen;
  }

  public void setNuevaVentana(boolean nuevaVentana) {
    this.nuevaVentana = nuevaVentana;
  }

  public boolean isNuevaVentana() {
    return nuevaVentana;
  }

  public void setAlt(String alt) {
    this.alt = alt;
  }

  public String getAlt() {
    if(alt == null)
      alt = "";
    return alt;
  }

  public void setOnClick(String onClick) {
    this.onClick = onClick;
  }

  public String getOnClick() {
    return onClick;
  }

  public void setCampoCondicion(String campoCondicion) {
    this.campoCondicion = campoCondicion;
  }

  public String getCampoCondicion() {
    return campoCondicion;
  }

  public void setValorCondicion(String valorCondicion) {
    this.valorCondicion = valorCondicion;
  }

  public String getValorCondicion() {
    return valorCondicion;
  }
}
