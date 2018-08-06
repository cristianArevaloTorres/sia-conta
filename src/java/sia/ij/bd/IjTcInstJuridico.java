package sia.ij.bd;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class IjTcInstJuridico {
    public IjTcInstJuridico() {
    }
    
    private String idInstJuridico;
    private String descripcion;
    private String siglas;
    private String estatus;


    public void setIdInstJuridico(String idInstJuridico) {
        this.idInstJuridico = idInstJuridico;
    }

    public String getIdInstJuridico() {
        return idInstJuridico;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setSiglas(String siglas) {
        this.siglas = siglas;
    }

    public String getSiglas() {
        return siglas;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getEstatus() {
        return estatus;
    }

    

        
}
