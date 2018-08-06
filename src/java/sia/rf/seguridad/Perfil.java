/**
 *  Clase que contiene los metodos que permiten realizar (A)ltas, (B)ajas y (C)ambios 
 *  de los registros del catátalogo SG_TC_PERFIL
 *
 * @version 1.0
 * @author Jose Alberto Flores Ramirez
 */
package sia.rf.seguridad;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import sia.db.dao.DaoFactory;
import sia.db.sql.Sentencias;
import sia.db.sql.Vista;
import sia.libs.formato.Error;

public class Perfil {

    private int    idGrupo;
    private int    idPerfil;
    private String descripcion;
    private String grupo;

    public Perfil() {
    }

    /**
     * Perfil   Constructror de clase
     * @param   idPerfil Identificador unico del Perfil(SG_TC_PERFIL.cve_perfil).
     * @param   descripcion Es el nombre que se le dara al Perfil
     */
    public Perfil(int idPerfil, String descripcion) {
        setIdPerfil(idPerfil);
        setDescripcion(descripcion);
    }

    public int getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(int idPerfil) {
        this.idPerfil = idPerfil;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(int idGrupo) {
        this.idGrupo = idGrupo;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    /**
     * obtenerPerfilGrupo   Obtiene el ID del perfil, nombre del perfil, ID Grupo y el nombre del grupo asignandolos a el propiedades respectivas de la clase.
     * @param connection    Es la conexion al esquema de la B.D.
     * @param idPerfil      Identificador unico del Perfil(SG_TC_PERFIL.cve_perfil) a buscar
     */
    public void obtenerPerfilGrupo(Connection con, int idPerfil) throws SQLException, Exception {
        Statement stQuery = null;
        ResultSet rsQuery = null;
        try {
            stQuery             = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            StringBuffer SQL    = new StringBuffer("SELECT p.cve_perfil AS cve_perfil, ");
            SQL.append("  UPPER(p.descripcion) AS perfil, ");
            SQL.append("  p.cve_grupo AS cve_grupo, ");
            SQL.append("  UPPER(g.descripcion) AS grupo ");
            SQL.append("FROM sg_tc_perfil p, ");
            SQL.append("     sg_tc_grupo g ");
            SQL.append("WHERE p.cve_grupo = g.cve_grupo ");
            SQL.append("      AND  p.cve_perfil = ").append(idPerfil);
            rsQuery = stQuery.executeQuery(SQL.toString());
            while(rsQuery.next()) {
                    setIdPerfil     ((rsQuery.getInt("cve_perfil") == 0) ? 0    : rsQuery.getInt("cve_perfil"));
                    setDescripcion  ((rsQuery.getString("perfil") == null) ? "" : rsQuery.getString("perfil"));
                    setIdGrupo      ((rsQuery.getInt("cve_grupo") == 0) ? 0     : rsQuery.getInt("cve_grupo"));
                    setGrupo        ((rsQuery.getString("grupo") == null) ? ""  : rsQuery.getString("grupo"));
            } // Fin while 
        } catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo Perfiles.obtenerPerfilGrupo() " + e.getMessage());
            throw e;
        } finally {
            if (rsQuery != null) {
                rsQuery.close();
                rsQuery = null;
            }
        } //Fin finally 
    } //Fin metodo 

    /**
     * validarExistenciaUsuarios Busca si un determinado perfil tiene asociados usuarios en SG_TR_USUARIOS_PERFIL
     * @param connection         Es la conexion al esquema de la B.D.
     * @param idPerfil           Identificador unico del Perfil(SG_TC_PERFIL.cve_perfil) a buscar
     * @param descripcion        Es la descripcion del perfl buscar.
     * @return int               Regresa un 1 si el perfil tiene asociados usuarios, de lo contrario devuelve 0
     */
    public int validarExistenciaUsuarios(Connection connection, int idPerfil) throws Exception {
        int control = 0;
        Sentencias sentencia = null;
        Map parametros       = null;
        try {
            parametros  = new HashMap();
            sentencia   = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD, Sentencias.XML);
            parametros.put("idPerfil", idPerfil);
            List<Vista> registros = new ArrayList<Vista>();
            registros = sentencia.registros("Seguridad.select.validarExistenciaUsuariosPerfil", parametros);
            if (registros != null && registros.size() > 0) {
                control = 1;
            } else control = 0;
        } catch (Exception e) {
            sia.libs.formato.Error.mensaje(this, e, "SIAFM", "validarExistenciaUsuariosPerfil", e.getMessage());
            connection.rollback();
            throw new Exception(e);
        } finally {
            sentencia   = null;
            parametros  = null;
        }
        return control;
    }
    
    /**
     * validarExistenciaPerfil   Busca si un determinado perfil tiene asociados modulos en SG_TR_MODULOS_PERFIL
     * @param connection         Es la conexion al esquema de la B.D.
     * @param idPerfil           Identificador unico del Perfil(SG_TC_PERFIL.cve_perfil) a buscar
     * @param descripcion        Es la descripcion del perfl buscar.
     * @return int               Regresa un 1 si el perfil tiene asociados usuarios, de lo contrario devuelve 0
     */
    public int validarExistenciaModulosPerfil(Connection connection, int idPerfil) throws Exception {
        int control = 0;
        Sentencias sentencia = null;
        Map parametros       = null;
        try {
            parametros  = new HashMap();
            sentencia   = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD, Sentencias.XML);
            parametros.put("idPerfil", idPerfil);
            List<Vista> registros = new ArrayList<Vista>();
            registros = sentencia.registros("Seguridad.select.validarExistenciaModulosPerfil", parametros);
            if (registros != null && registros.size() > 0) {
                control = 1;
            } else control = 0;
        } catch (Exception e) {
            sia.libs.formato.Error.mensaje(this, e, "SIAFM", "validarExistenciaModulosPerfil", e.getMessage());
            connection.rollback();
            throw new Exception(e);
        } finally {
            sentencia   = null;
            parametros  = null;
        }
        return control;
    }

    /**
     * validarPerfil        Busca un perfil con una descripcion y un idGrupo determinado en SG_TC_PERFIL
     * @param connection    Es la conexion al esquema de la B.D.
     * @param idPerfil      Identificador unico del Perfil(SG_TC_PERFIL.cve_perfil) a buscar
     * @param descripcion   Es la descripcion del perfl buscar.
     * @return int          Regresa un 1 si el registro fue encontrado, de lo contrario devuelve 0
     */
    public int validarPerfil(Connection connection, String descripcion, int idGrupo) throws Exception {
        int control = 0;
        Sentencias sentencia = null;
        Map parametros       = null;
        try {
            parametros  = new HashMap();
            sentencia   = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD, Sentencias.XML);
            parametros.put("idGrupo", idGrupo);
            parametros.put("descripcion", descripcion);
            List<Vista> registros = new ArrayList<Vista>();
            registros = sentencia.registros("Seguridad.select.validarPerfil", parametros);
            if (registros != null && registros.size() > 0) {
                control = 1;
            } else control = -1;
        } catch (Exception e) {
            sia.libs.formato.Error.mensaje(this, e, "SIAFM", "validarPerfil", e.getMessage());
            connection.rollback();
            throw new Exception(e);
        } finally {
            sentencia   = null;
            parametros  = null;
        }
        return control;
    }

    /**
     * insertar             Inserta un registro(perfil) de la tabla SG_TC_PERFIL
     * @param connection    Es la conexion al esquema de la B.D.
     * @param idPerfil      Identificador unico del Perfil(SG_TC_PERFIL.cve_perfil)
     * @param descripcion   Es la descripcion del perfl.
     * @return int          Regresa un 1 si el registro fue insertado, de lo contrario devuelve 0
     */
    public int insertar(Connection connection, int idPerfil, String descripcion, int idGrupo) throws Exception {
        int control = 0;
        Sentencias sentencia = null;
        Map parametros       = null;
        try {
            parametros  = new HashMap();
            sentencia   = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD, Sentencias.XML);
            parametros.put("idGrupo", idGrupo);
            parametros.put("idPerfil", idPerfil);
            parametros.put("descripcion", descripcion);
            //Pregunta que si existen registros que tengan un idGenero que ya exista en la tabla
            if (validarPerfil(connection, descripcion, idGrupo) == -1) {
                control = sentencia.ejecutar(connection, sentencia.getCommand("Seguridad.insert.insertarPerfil", parametros));
                if (control == 1) 
                    connection.commit();
            } else control = 0;
        } catch (Exception e) {
            Error.mensaje(this, e, "SIAFM", "insertarPerfil", e.getMessage());
            connection.rollback();
            throw new Exception(e);
        } finally {
            sentencia   = null;
            parametros  = null;
        }
        return control;
    }

    /**
     * eliminar             Elimina un registro(perfil) de la tabla SG_TC_PERFIL
     * @param connection    Es la conexion al esquema de la B.D.
     * @param idPerfil      Identificador unico del Perfil(SG_TC_PERFIL.cve_perfil)
     * @return int          Regresa un 1 si el registro fue eleminado de lo contrario devuelve 0
     */
    public int eliminar(Connection connection, int idPerfil) throws Exception {
        int control = 0;
        Sentencias sentencia = null;
        Map parametros       = null;
        try {
            parametros  = new HashMap();
            sentencia   = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD, Sentencias.XML);
            parametros.put("idPerfil", idPerfil);
            //Pregunta que si no existen registros asociados a la cuenta mayor
            if (validarExistenciaModulosPerfil(connection, idPerfil) == 0) {
                if (validarExistenciaUsuarios(connection, idPerfil) == 0) {
                    control = sentencia.ejecutar(connection, sentencia.getCommand("Seguridad.delete.eliminarPerfil", parametros));
                    if (control == 1) 
                        connection.commit();
                } else control = -1;
            } else control = -2;
        } catch (Exception e) {
            sia.libs.formato.Error.mensaje(this, e, "SIAFM", "eliminarPerfil", e.getMessage());
            connection.rollback();
            throw new Exception(e);
        } finally {
            sentencia   = null;
            parametros  = null;
        }
        return control;
    }

    /**
     * actualizar           Actualiza unicamente la descripcion de un registro de la tabla SG_TC_PERFIL
     * @param connection    Es la conexion al esquema de la B.D.
     * @param idPerfil      Identificador unico del Genero(Valores del 1 al 9 ).
     * @param descripcion   Es la descripcion del perfil
     * @return int          Regresa un 1 si el registro fue actualizado de lo contrario devuelve 0
     */
    public int actualizar(Connection connection, int idPerfil, String descripcion) throws Exception {
        int control = 0;
        Sentencias sentencia = null;
        Map parametros       = null;
        try {
            parametros  = new HashMap();
            sentencia   = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD);
            parametros.put("idPerfil", idPerfil);
            parametros.put("descripcion", descripcion);
            control = sentencia.ejecutar(sentencia.getCommand("Seguridad.update.actualizarRegistroPerfil", parametros));
            if (control >= 1)  
                connection.commit();
        } catch (Exception e) {
            sia.libs.formato.Error.mensaje(this, e, "SIAFM", "actualizarPerfil", e.getMessage());
            connection.rollback();
            throw new Exception(e);
        } finally {
            sentencia   = null;
            parametros  = null;
        }
        return control;
    }
}