package sia.libs.fun.tabla;

public class Campo {
  
  private String campo;
  private String nombreCol;
  private int alineacion;
  private String tamanoCol;
  private String formato;
  
  private String alinea;
  
  public static final int AL_DER = 1;
  public static final int AL_IZQ = 2;
  public static final int AL_CEN = 3;
  protected boolean ordenado = false;
  
  public static final String FORMATO_DOS_DEC = "%,.2f";
  
  public Campo(String campo, String nombreCol, int alineacion) {
    this(campo, nombreCol, alineacion, null);
  }
  
  public Campo(String campo, String nombreCol) {
    this(campo, nombreCol, -1, null);
  }
  
  public Campo(String campo, String nombreCol, String formato) {
    this(campo, nombreCol, -1, formato);
  }
  
  public Campo(String campo, String nombreCol, int alineacion, String formato) {
    this(-1, campo, nombreCol, alineacion, formato);
  }
  
  public Campo(int tamanoCol, String campo, String nombreCol, int alineacion, String formato) {
    setTamanoCol(String.valueOf(tamanoCol));
    setCampo(campo);
    setNombreCol(nombreCol);
    setAlineacion(alineacion);
    setFormato(formato);
  }

  public void setCampo(String campo) {
    this.campo = campo;
  }

  public String getCampo() {
    return campo;
  }

  public void setNombreCol(String nombreCol) {
    this.nombreCol = nombreCol;
  }

  public String getNombreCol() {
    return nombreCol;
  }

  public void setAlineacion(int alineacion) {
    this.alineacion = alineacion;
  }

  public int getAlineacion() {
    return alineacion;
  }
  
  public String getAlineacionStr() {
    if(alinea == null) {
      switch(alineacion) {
        case AL_DER: alinea = "right"; break;
        case AL_IZQ: alinea = "left"; break;
        case AL_CEN: alinea = "center"; break;
        default: alinea = "left";
      }
    }
    return alinea;
  }

  public void setTamanoCol(String tamanoCol) {
    this.tamanoCol = tamanoCol;
  }

  public String getTamanoCol() {
    return tamanoCol == null ? "" : tamanoCol;
  }

  public void setFormato(String formato) {
    this.formato = formato;
  }

  public String getFormato() {
    return formato;
  }

  public void setOrdenado(boolean ordenado) {
    this.ordenado = ordenado;
  }

  public boolean isOrdenado() {
    return ordenado;
  }
}
