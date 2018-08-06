package sia.rf.seguridad;

import java.sql.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import sun.jdbc.rowset.*;

public class Grupos {

    private int cve_grupo;
    private String descripcion;
    private int siguiente;

    public Grupos() {
        inicializar();
    }

    public int getClaveGrupo() {
        return cve_grupo;
    }

    public void setClaveGrupo(int cve_grupo) {
        this.cve_grupo = cve_grupo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void inicializar() {
        cve_grupo = 0;
        descripcion = "";
        siguiente = 0;
    }

    public int getSiguiente() {
        return siguiente;
    }

    // Obtiene un grupo en base a su id en el catalogo
    public void obtener(Connection con, int claveGrupo) throws SQLException, Exception {
        Statement stQuery = null;
        ResultSet rsQuery = null;
        try {
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            StringBuffer SQL = new StringBuffer("SELECT g.cve_grupo AS cve_grupo, ");
            SQL.append(" UPPER(g.descripcion) AS descripcion");
            SQL.append(" FROM sg_tc_grupo g ");
            SQL.append(" WHERE g.cve_grupo = ").append(claveGrupo);

            rsQuery = stQuery.executeQuery(SQL.toString());
            while (rsQuery.next()) {
                this.cve_grupo = (rsQuery.getInt("cve_grupo") == 0) ? 0 : rsQuery.getInt("cve_grupo");
                this.descripcion = (rsQuery.getString("descripcion") == null) ? "" : rsQuery.getString("descripcion");
            } // Fin while 
        } //Fin try 
        catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo Grupos.obtener() " + e.getMessage());
            throw e;
        } //Fin catch 
        finally {
            if (rsQuery != null) {
                rsQuery.close();
                rsQuery = null;
            }
            if (stQuery != null) {
                stQuery.close();
                stQuery = null;
            }
        } //Fin finally 
    } //Fin metodo 

    // Calcula el numero del id del grupo, este id es necesario calculando ya que correspondera
    // al id con el que se dara de alta un nuevo grupo.
    public void obtenerSiguienteClave(Connection con) throws SQLException, Exception {
        Statement stQuery = null;
        ResultSet rsQuery = null;
        try {
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            StringBuffer SQL = new StringBuffer("SELECT (MAX(g.cve_grupo) + 1) AS Siguiente ");
            SQL.append(" FROM sg_tc_grupo g ");
            rsQuery = stQuery.executeQuery(SQL.toString());
            while (rsQuery.next()) {
                siguiente = (rsQuery.getInt("Siguiente") == 0) ? 0 : rsQuery.getInt("Siguiente");
            } // Fin while 
        } //Fin try 
        catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo Grupos.obtenerSiguienteClave() " + e.getMessage());
            throw e;
        } //Fin catch 
        finally {
            if (rsQuery != null) {
                rsQuery.close();
                rsQuery = null;
            }
            if (stQuery != null) {
                stQuery.close();
                stQuery = null;
            }
        } //Fin finally 
    }

    // Metodo para insertar un nuevo grupo con el id del usuario que previamente se tiene que calcular.
    public void insertar(Connection con) throws SQLException, Exception {
        Statement stQuery = null;
        try {

            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            StringBuffer SQL = new StringBuffer("INSERT INTO sg_tc_grupo ( cve_grupo, descripcion) ");
            SQL.append("VALUES(");
            SQL.append(getClaveGrupo()).append(",");
            SQL.append("'").append(getDescripcion()).append("'").append(")");

            //System.out.println(SQL.toString()); 
            int rs = -1;
            rs = stQuery.executeUpdate(SQL.toString());
        } //Fin try 
        catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo Grupos.insertar() " + e.getMessage());
            throw e;
        } //Fin catch 
        finally {
            if (stQuery != null) {
                stQuery.close();
                stQuery = null;
            }
        } //Fin finally 
    } //Fin metodo

    // Actualiza la informacion de un grupo seleccionado
    public void actualizar(Connection con) throws SQLException, Exception {
        Statement stQuery = null;
        try {
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            StringBuffer SQL = new StringBuffer("UPDATE sg_tc_grupo ");
            SQL.append(" SET descripcion = '" + getDescripcion() + "' ");
            SQL.append(" WHERE cve_grupo = ").append(getClaveGrupo());
            //System.out.println(SQL.toString());
            int rs = -1;
            rs = stQuery.executeUpdate(SQL.toString());
        } //Fin try
        catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo Grupos.actualizar() " + e.getMessage());
            throw e;
        } //Fin catch
        finally {
            if (stQuery != null) {
                stQuery.close();
                stQuery = null;
            }
        } //Fin finally
    } //Fin metodo 

    // Metodo para eliminar un grupo deseado
    public void eliminar(Connection con) throws SQLException, Exception {
        Statement stQuery = null;
        try {
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            StringBuffer SQL = new StringBuffer("DELETE FROM sg_tc_grupo ");
            SQL.append("WHERE cve_grupo = ").append(getClaveGrupo());
            //System.out.println(SQL.toString()); 
            int rs = -1;
            rs = stQuery.executeUpdate(SQL.toString());
        } //Fin try 
        catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo Grupos.eliminar() " + e.getMessage());
            throw e;
        } //Fin catch 
        finally {
            if (stQuery != null) {
                stQuery.close();
                stQuery = null;
            }
        } //Fin finally 
    } //Fin metodo 

    // Metodo necesario para preguntar si el grupo que se desea eliminar tiene perfiles asociados, en 
    // caso de ser cierta esta condicion no se permitira eliminar el grupo hasta que el usuario haya 
    // eliminado los perfiles pertenecientes al grupo.
    public boolean tienePerfiles(Connection con) throws SQLException, Exception {
        Statement stQuery = null;
        ResultSet rsQuery = null;
        boolean tienePerfiles = false;
        try {
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            StringBuffer SQL = new StringBuffer("SELECT p.cve_perfil AS cve_perfil ");
            SQL.append(" FROM sg_tc_perfil p ");
            SQL.append(" WHERE p.cve_grupo = ").append(getClaveGrupo());

            rsQuery = stQuery.executeQuery(SQL.toString());
            while (rsQuery.next()) {
                tienePerfiles = true;
                break;
            } // Fin while 
        } //Fin try 
        catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo Grupos.tienePerfiles() " + e.getMessage());
            throw e;
        } //Fin catch 
        finally {
            if (rsQuery != null) {
                rsQuery.close();
                rsQuery = null;
            }
            if (stQuery != null) {
                stQuery.close();
                stQuery = null;
            }
        } //Fin finally 

        return tienePerfiles;
    } //Fin metodo 
} //Fin clase 
