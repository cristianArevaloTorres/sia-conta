package sia.rf.contabilidad.registroContable.servicios;

import java.sql.Connection ;


public class SubCuenta {

  private String cuentaCorta;
  private String cuentaContable;
  private Integer longitud;
  private Integer ajuste;
  private String caracter;
  private CuentaPadre cuentaPadre;
  private String tabla;
  private String campoCompara;
  //private ModuloRegistroContableImpl am;
  private int ejercicio;  
  private int idCatalogoCuenta;
  
  public SubCuenta(int ejercicio, int idCatalogoCuenta, Connection conexion) {
    this("", "", 0, 0, "0","","",ejercicio, idCatalogoCuenta, conexion);
  }

  public SubCuenta(String cuentaCorta, String cuentaContable, Integer longitud, Integer ajuste, String caracter,String tabla,String campoCompara,int ejercicio, int idCatalogoCuenta, Connection conexion) {
    //setAm(am);
    setCuentaCorta(cuentaCorta);
    setCuentaContable(cuentaContable);
    setLongitud(longitud);
    setAjuste(ajuste);
    setCaracter(caracter);
    setEjercicio(ejercicio);
    setIdCatalogoCuenta(idCatalogoCuenta);
    
    if(getCuentaContable()!= null)
      setCuentaPadre(new CuentaPadre(getCuentaContable(), getEjercicio(), this.getIdCatalogoCuenta(),conexion));
    else  
      setCuentaPadre(new CuentaPadre(getEjercicio(), this.getIdCatalogoCuenta()));
    setTabla(tabla);
    setCampoCompara(campoCompara); 
  }

  public void setLongitud(Integer longitud) {
    this.longitud = longitud;
  }

  public Integer getLongitud() {
    return longitud;
  }

  public void setAjuste(Integer ajuste) {
    this.ajuste = ajuste;
  }

  public Integer getAjuste() {
    return ajuste;
  }

  public void setCaracter(String caracter) {
    this.caracter = caracter;
  }

  public String getCaracter() {
    return caracter;
  }

  public String toString(){
    StringBuffer sb = new StringBuffer();
    sb.append("Subcuenta[");
    sb.append(getCuentaCorta());
    sb.append(",");
    sb.append(getCuentaContable());
    sb.append(",");
    sb.append(getLongitud());
    sb.append(",");
    sb.append(getAjuste());
    sb.append(",");
    sb.append(getCaracter());
    sb.append(",");
    sb.append(getCuentaPadre());
    sb.append(",");
    sb.append(getTabla());
    sb.append(",");
    sb.append(getCampoCompara());
    sb.append(",");
  //  sb.append(getCuentaMayorId());
    
    sb.append("]");
    return sb.toString();
  }

  public void setCuentaPadre(CuentaPadre cuentaPadre) {
    this.cuentaPadre = cuentaPadre;
  }

  public CuentaPadre getCuentaPadre() {
    return cuentaPadre;
  }

  public void setCuentaCorta(String cuentaCorta) {
    this.cuentaCorta = cuentaCorta;
  }

  public String getCuentaCorta() {
    return cuentaCorta;
  }

  public void setCuentaContable(String cuentaContable) {
    this.cuentaContable = cuentaContable;
  }

  public String getCuentaContable() {
    return cuentaContable;
  }
  
  public String getCuentaNula() {
    StringBuffer sb= new StringBuffer();
    for(int x= 0;  x < getLongitud(); x++) 
      sb.append(getCaracter());
    return sb.toString();
  }

  public void setTabla(String tabla) {
    this.tabla = tabla;
  }

  public String getTabla() {
    return tabla;
  }

  public void setCampoCompara(String campoCompara) {
    this.campoCompara = campoCompara;
  }

  public String getCampoCompara() {
    return campoCompara;
  }


   /* public void setAm(ModuloRegistroContableImpl am) {
        this.am = am;
    }

    public ModuloRegistroContableImpl getAm() {
        return am;
    }*/

  public void setEjercicio(int ejercicio) {
    this.ejercicio = ejercicio;
  }

  public int getEjercicio() {
    return ejercicio;
  }

  public void setIdCatalogoCuenta(int idCatalogoCuenta) {
    this.idCatalogoCuenta = idCatalogoCuenta;
  }

  public int getIdCatalogoCuenta() {
    return idCatalogoCuenta;
  }
}
