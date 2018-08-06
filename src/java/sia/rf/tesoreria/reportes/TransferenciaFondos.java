package sia.rf.tesoreria.reportes;

public class TransferenciaFondos {

    private Double montoCargo;
    private Double montoAbono;
    private String banco;
    private String numCuenta;
    private String nomCuenta;
    private String referencia;
    private String grupo;

    public TransferenciaFondos(String banco, String numCuenta, String nomCuenta, Double  montoCargo, Double montoAbono, String referencia, String grupo) {
        this.banco = banco;
        this.numCuenta = numCuenta;
        this.nomCuenta = nomCuenta;
        this.montoCargo = montoCargo;
        this.montoAbono = montoAbono;
        this.referencia = referencia;
        this.grupo = grupo;
    }
    
    public TransferenciaFondos() {
    }

    public void setMontoCargo(Double montoCargo) {
        this.montoCargo = montoCargo;
    }

    public Double getMontoCargo() {
        return montoCargo;
    }

    public void setMontoAbono(Double montoAbono) {
        this.montoAbono = montoAbono;
    }

    public Double getMontoAbono() {
        return montoAbono;
    }

    public void setNomCuenta(String nomCuenta) {
        this.nomCuenta = nomCuenta;
    }

    public String getNomCuenta() {
        return nomCuenta;
    }


    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getGrupo() {
        return grupo;
    }

  public void setBanco(String banco) {
    this.banco = banco;
  }

  public String getBanco() {
    return banco;
  }

  public void setNumCuenta(String numCuenta) {
    this.numCuenta = numCuenta;
  }

  public String getNumCuenta() {
    return numCuenta;
  }
}
