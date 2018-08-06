package sia.rf.tesoreria;

public class VariablesSession {

    private String opcion;
    private String nomProg;
    private String idProgramaS;
    private String proceso;
    private String administrador;

    public VariablesSession() {
    
    }

    public void setOpcion(String opcion) {
        this.opcion = opcion;
    }

    public String getOpcion() {
        return opcion;
    }

    public void setNomProg(String nomProg) {
        this.nomProg = nomProg;
    }

    public String getNomProg() {
        return nomProg;
    }

    public void setIdProgramaS(String idProgramaS) {
        this.idProgramaS = idProgramaS;
    }

    public String getIdProgramaS() {
        return idProgramaS;
    }

    public void setProceso(String proceso) {
        this.proceso = proceso;
    }

    public String getProceso() {
        return proceso;
    }

    public void setAdministrador(String administrador) {
        this.administrador = administrador;
    }

    public String getAdministrador() {
        return administrador;
    }
}
