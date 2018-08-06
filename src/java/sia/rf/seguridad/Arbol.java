/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sia.rf.seguridad;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author EST.JUAN.GALINDO
 */
public class Arbol {

    private String clave;
    private String descripcion;
    private String ruta;
    private String padre;
    private int nivel;
    private boolean marcar;

    public boolean getMarcar() {
        return marcar;
    }

    public void setMarcar(boolean marcar) {
        this.marcar = marcar;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getPadre() {
        return padre;
    }

    public void setPadre(String padre) {
        this.padre = padre;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public StringBuffer obtenerArbol(Connection con, int idPerfil) throws SQLException, Exception {
        Statement stQuery = null;
        ResultSet rsQuery = null;

        StringBuffer arbol = new StringBuffer("<table id='TBLArbol' name='TBLArbol' border='0' align='center'>");

        try {
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            StringBuffer SQL = new StringBuffer("SELECT am.clave as clave, \n");
            SQL.append("       (LENGTH(am.clave)/2) as nivel, \n");
            SQL.append("       am.des as descripcion, \n");
            SQL.append("       am.ruta as ruta, \n");
            SQL.append("       SUBSTR(am.clave, 1, LENGTH(am.clave)-2) as padre, \n");
            SQL.append("       (SELECT CASE \n");
            SQL.append("                WHEN COUNT(mp.consecutivo) > 0 THEN 'true' \n");
            SQL.append("                ELSE 'false' \n");
            SQL.append("               END \n");
            SQL.append("            FROM sg_tr_modulos_perfil mp  \n");
            SQL.append("                 WHERE mp.consecutivo = am.consecutivo \n");
            SQL.append("                       AND mp.cve_perfil = ").append(idPerfil);
            SQL.append("        ) AS marcar \n");
            SQL.append(" FROM sg_tc_arbol_menu am \n");
            SQL.append(" ORDER BY am.clave \n");


            rsQuery = stQuery.executeQuery(SQL.toString());
            while (rsQuery.next()) {
                setClave((rsQuery.getString("clave") == null) ? "" : rsQuery.getString("clave"));
                setDescripcion((rsQuery.getString("descripcion") == null) ? "" : rsQuery.getString("descripcion"));
                setRuta((rsQuery.getString("ruta") == null) ? "" : rsQuery.getString("ruta"));
                setPadre((rsQuery.getString("padre") == null) ? "" : rsQuery.getString("padre"));
                setNivel((rsQuery.getInt("nivel") == 0) ? 0 : rsQuery.getInt("nivel"));
                setMarcar(Boolean.parseBoolean(rsQuery.getString("marcar")));

                arbol.append("<tr id='tr" + getPadre() + "' name='tr" + getClave() + "' class='abrir'>");
                arbol.append("<td>");

                for (int n = 1; n < getNivel(); n++) {
                    arbol.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
                }

                if (getRuta().equals("")) {
                    arbol.append("<div id='diva" + getClave() + "' name='diva" + getClave() + "' style='display:inline'>");
                    arbol.append("<a id='" + getClave() + "' href='#' onclick='mostrar(\"tr\" + this.id, \"cerrar\", \"divc\" + this.id, \"diva\" + this.id, 1)'>");
                    arbol.append("<img id='ima' name='ima' height='16px' width='16px' src='../../Librerias/Imagenes/abierto.gif' border='0' alt='Ocultar' >");
                    arbol.append("</div>");
                    arbol.append("<div id='divc" + getClave() + "' name='divc" + getClave() + "'  style='display:none'>");
                    arbol.append("<a id='" + getClave() + "' href='#' onclick='mostrar(\"tr\" + this.id, \"abrir\", \"diva\" + this.id, \"divc\" + this.id, 1)' >");
                    arbol.append("<img id='imc' name='imc' height='16px' width='16px' src='../../Librerias/Imagenes/cerrado.gif' border='0' alt='Mostrar'>");
                    arbol.append("</a>");
                    arbol.append("</div>");

                } else {
                    arbol.append("<img id='ima' name='ima' height='16px' width='16px' src='../../Librerias/Imagenes/hoja.gif' border='0')>");
                }


                arbol.append("&nbsp;<input type='checkbox'  id='" + getPadre() + "' name='" + getPadre() + "' value='" + getClave() + "'");

                if (getMarcar()) {
                    arbol.append(" checked ");
                }

                arbol.append(" onclick='pintarHijos(this.value, this.checked);  pintarPadre(this.value, this.checked); ' ");
                arbol.append(">");
                if (getRuta().equals("")) {
                    arbol.append("<b>");
                    arbol.append(getDescripcion());
                    arbol.append("</b>");
                } else {
                    arbol.append(getDescripcion());
                }

                arbol.append("</td>");
                arbol.append("</tr>");

            } // Fin while 
        } //Fin try 
        catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo Arbol.obtener() " + e.getMessage());
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

        arbol.append("</table>");
        return arbol;
    } //Fin metodo 

    private void eliminarOpcionesArbolMenu(Connection con, String opciones, int idPerfil) throws SQLException, Exception {
        Statement stQuery = null;
        try {
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            StringBuffer SQL = new StringBuffer("DELETE FROM sg_tr_modulos_perfil mp \n");
            SQL.append("      WHERE mp.cve_perfil =  ").append(idPerfil).append(" \n");

            //SQL.append("WHERE mp.consecutivo IN ( \n");
            //SQL.append("            SELECT am.consecutivo \n");
            //SQL.append("                    FROM sg_tc_arbol_menu am \n");
            //SQL.append("                    WHERE am.clave IN ( ").append(opciones).append(" ) \n");
            //SQL.append("      ) \n");
            //SQL.append("      AND mp.cve_perfil =  ").append(idPerfil).append(" \n");

            //System.out.println(SQL.toString()); 
            int rs = -1;
            rs = stQuery.executeUpdate(SQL.toString());
        } //Fin try 
        catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo Usuarios.eliminarCatalogoUsuariosPerfiles()" + e.getMessage());
            throw e;
        } //Fin catch 
        finally {
            if (stQuery != null) {
                stQuery.close();
                stQuery = null;
            }
        } //Fin finally     }
    }

    private void insertarOpcionesArbolMenu(Connection con, String opciones, int idPerfil) throws SQLException, Exception {
        Statement stQuery = null;
        try {
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            StringBuffer SQL = new StringBuffer("INSERT INTO sg_tr_modulos_perfil(cve_perfil, consecutivo)  \n");
            SQL.append("    SELECT ").append(idPerfil).append(", am.consecutivo ");
            SQL.append("        FROM sg_tc_arbol_menu am \n");
            SQL.append("                    WHERE am.clave IN ( ").append(opciones).append(" ) \n");

            //System.out.println(SQL.toString()); 
            int rs = -1;
            rs = stQuery.executeUpdate(SQL.toString());
        } //Fin try 
        catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo Usuarios.eliminarCatalogoUsuariosPerfiles()" + e.getMessage());
            throw e;
        } //Fin catch 
        finally {
            if (stQuery != null) {
                stQuery.close();
                stQuery = null;
            }
        } //Fin finally 
    }

    public void actualizarOpcionesArbolMenu(Connection con, String opciones, int idPerfil) throws SQLException, Exception {
        eliminarOpcionesArbolMenu(con, opciones, idPerfil);

        if (!opciones.equals("")) {
            insertarOpcionesArbolMenu(con, opciones, idPerfil);
        }

    }
}
