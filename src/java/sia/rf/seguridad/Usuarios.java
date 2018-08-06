/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sia.rf.seguridad;

import java.sql.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import sun.jdbc.rowset.*;
import sia.libs.formato.Encriptar;

public class Usuarios {

    private int num_empleado;
    private String usuario;
    private String contrasena;
    private String curp;
    private String nombre;
    private String paterno;
    private String materno;
    private int id_puesto;
    private int id_plaza;
    private String id_unidad;
    private String id_ambito;
    private int id_entidad;
    private String adscripcion;
    private Encriptar encriptar;

    public Usuarios() {
        setAdscripcion("");
        setIdUnidad("000");
        setIdAmbito("0");
        setIdEntidad(0);
        setIdPuesto(0);
        setIdPlaza(0);

        // Objeto necesario para poder encriptar/desenciptar la 
        // contrasena proporcionado para el usuario
        encriptar = new Encriptar();
    }

    public int getNumEmpleado() {
        return num_empleado;
    }

    public void setNumEmpleado(int num_empleado) {
        this.num_empleado = num_empleado;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getCURP() {
        return curp;
    }

    public void setCURP(String curp) {
        this.curp = curp;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPaterno() {
        return paterno;
    }

    public void setPaterno(String paterno) {
        this.paterno = paterno;
    }

    public String getMaterno() {
        return materno;
    }

    public void setMaterno(String materno) {
        this.materno = materno;
    }

    public int getIdPuesto() {
        return id_puesto;
    }

    public void setIdPuesto(int id_puesto) {
        this.id_puesto = id_puesto;
    }

    public int getIdPlaza() {
        return id_plaza;
    }

    public void setIdPlaza(int id_plaza) {
        this.id_plaza = id_plaza;
    }

    public String getIdUnidad() {
        return id_unidad;
    }

    public void setIdUnidad(String id_unidad) {
        this.id_unidad = id_unidad;
    }

    public String getIdAmbito() {
        return id_ambito;
    }

    public void setIdAmbito(String id_ambito) {
        this.id_ambito = id_ambito;
    }

    public String getAdscripcion() {
        return adscripcion;
    }

    public void setAdscripcion(String adscripcion) {
        this.adscripcion = adscripcion;
    }

    public int getIdEntidad() {
        return id_entidad;
    }

    public void setIdEntidad(int id_entidad) {
        this.id_entidad = id_entidad;
    }

    public String getNombreCompleto() {
        return getNombre() + " " + getPaterno() + " " + getMaterno();
    }

    // Obtiene la conformacion de la informacion del un usuario determinado por el numero
    // de empleado, esta informacion se encuentra disttribuida en varias tablas por eso 
    // es necesarios union de varias tablas.
    public void obtener(Connection con, int numEmpleado) throws SQLException, Exception {
        Statement stQuery = null;
        ResultSet rsQuery = null;
        try {
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            StringBuffer SQL = new StringBuffer("SELECT e.num_empleado as num_empleado, ");
            SQL.append(" e.curp as curp,");
            SQL.append(" e.nombres  as nombres,");
            SQL.append(" e.apellido_pat as paterno,");
            SQL.append(" e.apellido_mat as materno,");
            SQL.append(" e.id_puesto as id_puesto,");
            SQL.append(" e.plaza as id_plaza,");
            SQL.append(" p.unidad_ejecutora as id_unidad,");
            SQL.append(" p.entidad as id_entidad,");
            SQL.append(" p.ambito as id_ambito,");
            SQL.append(" p.clave_adsc_func as adscripcion,");
            SQL.append(" u.login as usuario,");
            SQL.append(" u.password as contrasena ");
            SQL.append(" FROM sg_tr_usuarios u, ");
            SQL.append(" rh_tr_empleados e, ");
            SQL.append(" rh_tr_plazas p ");
            SQL.append(" WHERE u.num_empleado = e.num_empleado ");
            SQL.append(" AND e.plaza = p.plaza ");
            SQL.append(" AND u.num_empleado = ").append(numEmpleado);

            rsQuery = stQuery.executeQuery(SQL.toString());
            while (rsQuery.next()) {
                setNumEmpleado((rsQuery.getInt("num_empleado") == 0) ? 0 : rsQuery.getInt("num_empleado"));
                setCURP((rsQuery.getString("curp") == null) ? "" : rsQuery.getString("curp"));
                setPaterno((rsQuery.getString("paterno") == null) ? "" : rsQuery.getString("paterno"));
                setMaterno((rsQuery.getString("materno") == null) ? "" : rsQuery.getString("materno"));
                setNombre((rsQuery.getString("nombres") == null) ? "" : rsQuery.getString("nombres"));
                setIdPuesto((rsQuery.getInt("id_puesto") == 0) ? 0 : rsQuery.getInt("id_puesto"));
                setIdPlaza((rsQuery.getInt("id_plaza") == 0) ? 0 : rsQuery.getInt("id_plaza"));
                setIdUnidad((rsQuery.getString("id_unidad") == null) ? "" : rsQuery.getString("id_unidad"));
                setIdEntidad((rsQuery.getInt("id_entidad") == 0) ? 0 : rsQuery.getInt("id_entidad"));
                setIdAmbito((rsQuery.getString("id_ambito") == null) ? "" : rsQuery.getString("id_ambito"));
                setAdscripcion((rsQuery.getString("adscripcion") == null) ? "" : rsQuery.getString("adscripcion"));
                setUsuario((rsQuery.getString("usuario") == null) ? "" : rsQuery.getString("usuario"));
                setContrasena(encriptar.desencriptar((rsQuery.getString("contrasena") == null) ? "" : rsQuery.getString("contrasena")));
            } // Fin while 
        } //Fin try 
        catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo Usuarios.obtener() " + e.getMessage());
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

    // Obtiene un componente HTML de tipo <SELECT></SELECT> rellenado con la informacion perteneciente 
    // al catalogo de PUESTOS, el VALUE de cada <OPTION> corresponde a ID del Puesto en el catalogo.
    public StringBuffer obtenerCatalogoPuesto(Connection con, boolean soloLectura) throws SQLException, Exception {
        Statement stQuery = null;
        ResultSet rsQuery = null;
        StringBuffer selector = new StringBuffer("<select id='DLPuestos' name='DLPuestos' class='cajaTexto' ");

        if (soloLectura) {
            selector.append(" disabled ");
        }

        selector.append(">");


        try {
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            StringBuffer SQL = new StringBuffer("SELECT id_puesto as id, desc_puesto as descripcion");
            SQL.append(" FROM rh_tr_puestos ");
            SQL.append(" ORDER BY descripcion ");

            int id;
            String descripcion;

            rsQuery = stQuery.executeQuery(SQL.toString());
            while (rsQuery.next()) {
                id = (rsQuery.getInt("id") == 0) ? 0 : rsQuery.getInt("id");
                descripcion = (rsQuery.getString("descripcion") == null) ? "" : rsQuery.getString("descripcion");

                selector.append("<option value='").append(id);
                if (getIdPuesto() == id) {
                    selector.append("' selected ");
                }

                selector.append("'>");

                selector.append(descripcion);
                selector.append("</option>");
            } // Fin while 
        } //Fin try 
        catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo Usuarios.obtenerCatalogoPuestos() " + e.getMessage());
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


        selector.append("</select>");
        return selector;

    } //Fin metodo 

    // Obtiene un componente HTML de tipo <SELECT></SELECT> rellenado con la informacion perteneciente 
    // al catalogo de PLAZAS, el VALUE de cada <OPTION> corresponde a ID de la PLAZA en el catalogo.
    // el LLENADO de los elementos de esta lista es DEPENDIENTE dinamicamente de los valores SELECCIONADOS 
    // en las listas desplegables(<select></select>) de: UNIDAD EJECUTORA, ENTIDA Y AMBITO
    public StringBuffer obtenerCatalogoPlazas(Connection con, String id_unidad, int id_entidad, String id_ambito, boolean soloLectura) throws SQLException, Exception {
        Statement stQuery = null;
        ResultSet rsQuery = null;
        boolean tienePlazas = false;

        StringBuffer selector = new StringBuffer("<select id='DLPlazas' name='DLPlazas' class='cajaTexto' ");

        if (soloLectura) {
            selector.append(" disabled ");
        }

        selector.append(">");

        try {
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            StringBuffer SQL = new StringBuffer("SELECT  p.plaza as id_plaza, ");
            SQL.append("  p.unidad_ejecutora as id_unidad, ");
            SQL.append("  p.entidad as id_entidad, ");
            SQL.append("  p.ambito as id_ambito, ");
            SQL.append("  p.clave_adsc_func as id_adscripcion, ");
            SQL.append("  a.descripcion as descripcion, ");
            SQL.append(" CASE ");
            SQL.append(" WHEN p.plaza < 10 THEN '00' || TO_CHAR(p.plaza) ");
            SQL.append(" WHEN p.plaza < 100 THEN '0' || TO_CHAR(p.plaza) ");
            SQL.append(" ELSE TO_CHAR(p.plaza) ");
            SQL.append(" END || p.unidad_ejecutora || ");
            SQL.append(" CASE ");
            SQL.append(" WHEN p.entidad < 10 THEN '0' || TO_CHAR(p.entidad) ");
            SQL.append(" ELSE TO_CHAR(p.entidad) ");
            SQL.append(" END || ");
            SQL.append(" CASE ");
            SQL.append(" WHEN TO_NUMBER(p.ambito) < 10 THEN '0' || p.ambito ");
            SQL.append(" ELSE p.ambito ");
            SQL.append(" END as id_unico ");
            SQL.append(" FROM  rh_tr_plazas p,");
            SQL.append("       rh_tc_est_funcional a");
            SQL.append(" WHERE p.clave_adsc_func = a.clave_adsc_func");
            SQL.append(" AND p.unidad_ejecutora = '").append(id_unidad).append("'");
            SQL.append(" AND p.entidad = ").append(id_entidad);
            SQL.append(" AND p.ambito = '").append(id_ambito).append("'");

            String descripcion;
            rsQuery = stQuery.executeQuery(SQL.toString());

            while (rsQuery.next()) {
                id_plaza = (rsQuery.getInt("id_plaza") == 0) ? 0 : rsQuery.getInt("id_plaza");
                descripcion = (rsQuery.getString("descripcion") == null) ? "" : rsQuery.getString("descripcion");
                selector.append("<option value='").append(id_plaza);
                if (getIdPlaza() == id_plaza) {
                    selector.append("' selected ");
                } else {
                    selector.append("'");
                }

                selector.append(">");

                selector.append(descripcion);
                selector.append("</option>");
                tienePlazas = true;
            } // Fin while 

            if (!tienePlazas) {
                selector.append("<option value='0'>Ninguna plaza para la Unidad Ejecutora, Entidad y Ambito</option>");
            }

        } //Fin try 
        catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo Usuarios.obtenerCatalogoPlazas() " + e.getMessage());
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

        selector.append("</select>");
        return selector;
    } //Fin metodo 

    // Obtiene un componente HTML de tipo <SELECT></SELECT> rellenado con la informacion perteneciente 
    // al catalogo de UNIDADES EJECUTORAS, el VALUE de cada <OPTION> corresponde a ID de la UNIDAD 
    // EJECUTORA en catalogo.
    public StringBuffer obtenerCatalogoUnidadesEjecutoras(Connection con, boolean soloLectura) throws SQLException, Exception {
        Statement stQuery = null;
        ResultSet rsQuery = null;
        StringBuffer selector = new StringBuffer("<select id='DLUnidades' name='DLUnidades' class='cajaTexto' OnChange='mostrarPlazas()' ");

        if (soloLectura) {
            selector.append(" disabled ");
        }

        selector.append(">");
        selector.append("<option value='000'>-</option>");

        try {
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            StringBuffer SQL = new StringBuffer("SELECT unidad_ejecutora as unidad, ");
            SQL.append(" UPPER(unidad_ejecutora || ' - ' || descripcion) as descripcion ");
            SQL.append(" FROM  rh_tc_uni_ejecutoras where unidad_ejecutora = '001'");
            SQL.append(" ORDER BY descripcion ");

            String unidad;
            String descripcion;

            rsQuery = stQuery.executeQuery(SQL.toString());
            while (rsQuery.next()) {
                unidad = (rsQuery.getString("unidad") == null) ? "" : rsQuery.getString("unidad");
                descripcion = (rsQuery.getString("descripcion") == null) ? "" : rsQuery.getString("descripcion");

                selector.append("<option value='").append(unidad);
                if (getIdUnidad().equals(unidad)) {
                    selector.append("' selected ");
                }
                selector.append("'>");

                selector.append(descripcion);
                selector.append("</option>");
            } // Fin while 
        } //Fin try 
        catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo Usuarios.obtenerCatalogoUnidadEjecutoras() " + e.getMessage());
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


        selector.append("</select>");
        return selector;

    } //Fin metodo 

    // Obtiene un componente HTML de tipo <SELECT></SELECT> rellenado con la informacion perteneciente 
    // al catalogo de ENTIDADES, el VALUE de cada <OPTION> corresponde a ID de la ENTIDAD en el catalogo.
    public StringBuffer obtenerCatalogoEntidades(Connection con, boolean soloLectura) throws SQLException, Exception {
        Statement stQuery = null;
        ResultSet rsQuery = null;
        StringBuffer selector = new StringBuffer("<select id='DLEntidades' name='DLEntidades' class='cajaTexto' OnChange='mostrarPlazas()' ");

        if (soloLectura) {
            selector.append(" disabled ");
        }

        selector.append(">");
        selector.append("<option value='0'>-</option>");

        try {
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            StringBuffer SQL = new StringBuffer("SELECT entidad as id_entidad, ");
            SQL.append(" UPPER( ");
            SQL.append("    CASE  ");
            SQL.append("        WHEN entidad < 10 THEN '0' || TO_CHAR(entidad) ");
            SQL.append("        ELSE TO_CHAR(entidad)");
            SQL.append("    END || ' - ' || descripcion ");
            SQL.append(" ) as descripcion ");
            SQL.append(" FROM  rh_tc_entidades ");
            SQL.append(" ORDER BY descripcion ");

            int id_entidad;
            String descripcion;

            rsQuery = stQuery.executeQuery(SQL.toString());
            while (rsQuery.next()) {
                id_entidad = (rsQuery.getInt("id_entidad") == 0) ? 0 : rsQuery.getInt("id_entidad");
                descripcion = (rsQuery.getString("descripcion") == null) ? "" : rsQuery.getString("descripcion");

                selector.append("<option value='").append(id_entidad);
                if (getIdEntidad() == id_entidad) {
                    selector.append("' selected ");
                }
                selector.append("'>");

                selector.append(descripcion);
                selector.append("</option>");
            } // Fin while 
        } //Fin try 
        catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo Usuarios.obtenerCatalogoEntidades() " + e.getMessage());
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


        selector.append("</select>");
        return selector;

    } //Fin metodo 

    // Obtiene un componente HTML de tipo <SELECT></SELECT> rellenado con la informacion perteneciente 
    // al catalogo de AMBITOS, el VALUE de cada <OPTION> corresponde a ID del AMBITO en el catalogo.
    public StringBuffer obtenerCatalogoAmbitos(Connection con, boolean soloLectura) throws SQLException, Exception {
        Statement stQuery = null;
        ResultSet rsQuery = null;
        StringBuffer selector = new StringBuffer("<select id='DLAmbitos' name='DLAmbitos' class='cajaTexto' OnChange='mostrarPlazas()' ");

        if (soloLectura) {
            selector.append(" disabled ");
        }

        selector.append(">");
        selector.append("<option value='0'>-</option>");

        try {
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            StringBuffer SQL = new StringBuffer("SELECT ambito as id_ambito, ");
            SQL.append(" UPPER( ");
            SQL.append("    CASE  ");
            SQL.append("        WHEN ambito < 10 THEN '0' || TO_CHAR(ambito) ");
            SQL.append("        ELSE TO_CHAR(ambito)");
            SQL.append("    END || ' - ' || descripcion ");
            SQL.append(" ) as descripcion ");
            SQL.append(" FROM  rh_tc_ambitos");
            SQL.append(" ORDER BY descripcion ");

            String idAmbito;
            String descripcion;

            rsQuery = stQuery.executeQuery(SQL.toString());
            while (rsQuery.next()) {
                idAmbito = (rsQuery.getString("id_ambito") == null) ? "" : rsQuery.getString("id_ambito");
                descripcion = (rsQuery.getString("descripcion") == null) ? "" : rsQuery.getString("descripcion");

                selector.append("<option value='").append(idAmbito);
                if (getIdAmbito().equals(idAmbito)) {
                    selector.append("' selected ");
                }
                selector.append("'>");

                selector.append(descripcion);
                selector.append("</option>");
            } // Fin while 
        } //Fin try 
        catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo Usuarios.obtenerCatalogoAmbitos() " + e.getMessage());
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


        selector.append("</select>");
        return selector;

    } //Fin metodo 

    // Metodo que se encargar de calcular el valor del id de usuario a dar de alta, este id lo
    // determinar el numero arrogado por la secuencia SEQ_NUM_EMPLEADO en base a incrementos de +1
    private void obtenerSiguienteNumEmpleado(Connection con) throws Exception {
        Statement stQuery = null;
        StringBuffer SQL = null;
        ResultSet rsQuerySeq = null;

        try {
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            SQL = new StringBuffer("SELECT seq_num_empleado.nextval as num_empleado FROM dual");
            rsQuerySeq = stQuery.executeQuery(SQL.toString());
            while (rsQuerySeq.next()) {
                setNumEmpleado(Integer.parseInt(rsQuerySeq.getString("num_empleado")));
            } //del while

        } catch (Exception e) {
            throw e;
        } finally {
            SQL.setLength(0);
            if (rsQuerySeq != null) {
                rsQuerySeq.close();
            }
            rsQuerySeq = null;
            if (stQuery != null) {
                stQuery.close();
            }
            stQuery = null;
        }
    }

    private void insertarCatalogoUsuariosPerfiles(Connection con, int idPerfil) throws SQLException, Exception {
        Statement stQuery = null;
        try {
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            StringBuffer SQL = new StringBuffer("INSERT INTO sg_tr_usuarios_perfil (");
            SQL.append("num_empleado, cve_perfil) ");
            SQL.append("VALUES(");
            SQL.append(getNumEmpleado()).append(",");
            SQL.append(idPerfil).append(")");

            //System.out.println(SQL.toString()); 
            int rs = -1;
            rs = stQuery.executeUpdate(SQL.toString());
        } //Fin try 
        catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo Usuarios.insertarCatalogoUsuariosPefiles()" + e.getMessage());
            throw e;
        } //Fin catch 
        finally {
            if (stQuery != null) {
                stQuery.close();
                stQuery = null;
            }
        } //Fin finally 
    }

    private void insertarCatalogoUsuarios(Connection con) throws SQLException, Exception {
        Statement stQuery = null;
        try {
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            StringBuffer SQL = new StringBuffer("INSERT INTO sg_tr_usuarios (");
            SQL.append("login, password, num_empleado, fecha) ");
            SQL.append("VALUES(");
            SQL.append("'").append(getUsuario()).append("' ,");
            SQL.append("'").append(encriptar.encriptar(getContrasena())).append("', ");
            SQL.append(getNumEmpleado()).append(", ");
            SQL.append("SYSDATE").append(")");

            //System.out.println(SQL.toString()); 
            int rs = -1;
            rs = stQuery.executeUpdate(SQL.toString());
        } //Fin try 
        catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo Usuarios.insertarCatalogoUsuarios()" + e.getMessage());
            throw e;
        } //Fin catch 
        finally {
            if (stQuery != null) {
                stQuery.close();
                stQuery = null;
            }
        } //Fin finally 

    }

    private void insertarCatalogoEmpleados(Connection con) throws SQLException, Exception {
        Statement stQuery = null;
        try {
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            StringBuffer SQL = new StringBuffer("INSERT INTO rh_tr_empleados ( ");
            SQL.append("num_empleado, curp, nombres, apellido_pat, apellido_mat, id_puesto, plaza) ");
            SQL.append("VALUES(");
            SQL.append(getNumEmpleado()).append(", ");
            SQL.append("'").append(getCURP()).append("', ");
            SQL.append("'").append(getNombre()).append("', ");
            SQL.append("'").append(getPaterno()).append("', ");
            SQL.append("'").append(getMaterno()).append("', ");
            SQL.append(getIdPuesto()).append(", ");
            SQL.append(getIdPlaza()).append(")");

            //System.out.println(SQL.toString()); 
            int rs = -1;
            rs = stQuery.executeUpdate(SQL.toString());
        } //Fin try 
        catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo Usuarios.insertarCatalogoEmpleados() " + e.getMessage());
            throw e;
        } //Fin catch 
        finally {
            if (stQuery != null) {
                stQuery.close();
                stQuery = null;
            }
        } //Fin finally 

    }

    public void insertar(Connection con, int idPerfil) throws SQLException, Exception {
        try {
            obtenerSiguienteNumEmpleado(con);
            insertarCatalogoEmpleados(con);
            insertarCatalogoUsuarios(con);
            insertarCatalogoUsuariosPerfiles(con, idPerfil);

        } //Fin try 
        catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo Usuarios.insertar() " + e.getMessage());
            throw e;
        } //Fin catch 
    } //Fin metodo

    public void actualizarCatalogoUsuarios(Connection con) throws SQLException, Exception {
        Statement stQuery = null;
        try {
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            StringBuffer SQL = new StringBuffer("UPDATE sg_tr_usuarios ");
            SQL.append(" SET login = '" + getUsuario() + "', ");
            SQL.append(" password = '" + encriptar.encriptar(getContrasena()) + "' ");
            SQL.append(" WHERE num_empleado = ").append(getNumEmpleado());
            //System.out.println(SQL.toString());
            int rs = -1;
            rs = stQuery.executeUpdate(SQL.toString());
        } //Fin try
        catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo Usuarios.actualizarCatalogoUsuarios() " + e.getMessage());
            throw e;
        } //Fin catch
        finally {
            if (stQuery != null) {
                stQuery.close();
                stQuery = null;
            }
        } //Fin finally
    } //Fin metodo 

    public void actualizarCatalogoEmpleados(Connection con) throws SQLException, Exception {
        Statement stQuery = null;
        try {
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            StringBuffer SQL = new StringBuffer("UPDATE rh_tr_empleados ");
            SQL.append(" SET curp = '" + getCURP() + "', ");
            SQL.append(" nombres = '" + getNombre() + "', ");
            SQL.append(" apellido_pat = '" + getPaterno() + "', ");
            SQL.append(" apellido_mat = '" + getMaterno() + "', ");
            SQL.append(" id_puesto = " + getIdPuesto() + ", ");
            SQL.append(" plaza = " + getIdPlaza() + " ");
            SQL.append(" WHERE num_empleado = ").append(getNumEmpleado());
            //System.out.println(SQL.toString());
            int rs = -1;
            rs = stQuery.executeUpdate(SQL.toString());
        } //Fin try
        catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo Usuarios.actualizarCatalogoEmpleados() " + e.getMessage());
            throw e;
        } //Fin catch
        finally {
            if (stQuery != null) {
                stQuery.close();
                stQuery = null;
            }
        } //Fin finally
    } //Fin metodo 

    public void actualizar(Connection con) throws SQLException, Exception {
        try {
            actualizarCatalogoUsuarios(con);
            actualizarCatalogoEmpleados(con);
        } //Fin try 
        catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo Usuarios.actualizar() " + e.getMessage());
            throw e;
        } //Fin catch 
    } //Fin metodo 

    private void eliminarCatalogoUsuariosPerfiles(Connection con) throws SQLException, Exception {
        Statement stQuery = null;
        try {
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            StringBuffer SQL = new StringBuffer("DELETE FROM sg_tr_usuarios_perfil ");
            SQL.append("WHERE num_empleado = ");
            SQL.append(getNumEmpleado());

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

    private void eliminarCatalogoUsuarios(Connection con) throws SQLException, Exception {
        Statement stQuery = null;
        try {
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            StringBuffer SQL = new StringBuffer("DELETE FROM sg_tr_usuarios ");
            SQL.append("WHERE num_empleado = ");
            SQL.append(getNumEmpleado());

            //System.out.println(SQL.toString()); 
            int rs = -1;
            rs = stQuery.executeUpdate(SQL.toString());
        } //Fin try 
        catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo Usuarios.eliminarCatalogoUsuarios()" + e.getMessage());
            throw e;
        } //Fin catch 
        finally {
            if (stQuery != null) {
                stQuery.close();
                stQuery = null;
            }
        } //Fin finally 

    }

    private void eliminarCatalogoEmpleados(Connection con) throws SQLException, Exception {
        Statement stQuery = null;
        try {
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            StringBuffer SQL = new StringBuffer("DELETE FROM rh_tr_empleados ");
            SQL.append("WHERE num_empleado = ");
            SQL.append(getNumEmpleado());

            //System.out.println(SQL.toString()); 
            int rs = -1;
            rs = stQuery.executeUpdate(SQL.toString());
        } //Fin try 
        catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo Usuarios.eliminarCatalogoEmpleados() " + e.getMessage());
            throw e;
        } //Fin catch 
        finally {
            if (stQuery != null) {
                stQuery.close();
                stQuery = null;
            }
        } //Fin finally 

    }

    public void eliminar(Connection con) throws SQLException, Exception {
        try {
            eliminarCatalogoUsuariosPerfiles(con);
            eliminarCatalogoUsuarios(con);
            eliminarCatalogoEmpleados(con);
        } //Fin try 
        catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo Usuarios.eliminar() " + e.getMessage());
            throw e;
        } //Fin catch 
    } //Fin metodo 
    
    
    public boolean existeUsuario(Connection con) throws SQLException, Exception {
        Statement stQuery = null;
        ResultSet rsQuery = null;
        boolean existeUsuario = false;
        int cantidad;
        int num_emp;

        try {
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            StringBuffer SQL = new StringBuffer("SELECT count(num_empleado) as cantidad, num_empleado ");
            SQL.append(" FROM sg_tr_usuarios ");
            SQL.append(" WHERE login = '").append(getUsuario()).append("'");
            SQL.append(" GROUP BY num_empleado") ;

            rsQuery = stQuery.executeQuery(SQL.toString());
            while (rsQuery.next()) {
                 cantidad = rsQuery.getInt("cantidad");
                num_emp = (rsQuery.getInt("num_empleado") == 0) ? 0 : rsQuery.getInt("num_empleado");
                
                if (cantidad > 0 && num_emp != getNumEmpleado()) {
                    existeUsuario = true;
                }
            } // Fin while 
        } //Fin try 
        catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo Usuarios.existeUsuario() " + e.getMessage());
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

        return existeUsuario;
    } //Fin metodo 
    

    public boolean tienePolizas(Connection con) throws SQLException, Exception {
        Statement stQuery = null;
        ResultSet rsQuery = null;
        boolean tienePolizas = false;
        int cantidad;

        try {
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            StringBuffer SQL = new StringBuffer("SELECT count(poliza_id) as cantidad ");
            SQL.append(" FROM rf_tr_polizas ");
            SQL.append(" WHERE num_empleado = ").append(getNumEmpleado());

            rsQuery = stQuery.executeQuery(SQL.toString());
            while (rsQuery.next()) {
                cantidad = rsQuery.getInt("cantidad");
                if (cantidad > 0) {
                    tienePolizas = true;
                }
            } // Fin while 
        } //Fin try 
        catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo Usuarios.tienePolizas() " + e.getMessage());
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

        return tienePolizas;
    } //Fin metodo 

    public boolean tienePolizasCargaExcel(Connection con) throws SQLException, Exception {
        Statement stQuery = null;
        ResultSet rsQuery = null;
        boolean tienePolizas = false;
        int cantidad;

        try {
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            StringBuffer SQL = new StringBuffer("SELECT count(poliza_id) as cantidad");
            SQL.append(" FROM rf_tr_polizas_carga ");
            SQL.append(" WHERE numempleado = ").append(getNumEmpleado());

            rsQuery = stQuery.executeQuery(SQL.toString());
            while (rsQuery.next()) {
                cantidad = rsQuery.getInt("cantidad");
                if (cantidad > 0) {
                    tienePolizas = true;
                }
            } // Fin while 
        } //Fin try 
        catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo Usuarios.tienePolizasCargaExcel() " + e.getMessage());
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

        return tienePolizas;
    } //Fin metodo 

    public boolean tieneDocumentosContables(Connection con) throws SQLException, Exception {
        Statement stQuery = null;
        ResultSet rsQuery = null;
        boolean tieneDocumentos = false;
        int cantidad;

        try {
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            StringBuffer SQL = new StringBuffer("SELECT count(documento_contable_id) as cantidad ");
            SQL.append(" FROM rf_tr_documentos_contables ");
            SQL.append(" WHERE num_empleado = ").append(getNumEmpleado());

            rsQuery = stQuery.executeQuery(SQL.toString());
            while (rsQuery.next()) {
                cantidad = rsQuery.getInt("cantidad");
                if (cantidad > 0) {
                    tieneDocumentos = true;
                }

            } // Fin while 
        } //Fin try 
        catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo Usuarios.tieneDocumentosContables() " + e.getMessage());
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

        return tieneDocumentos;
    } //Fin metodo     

    public boolean tieneFirmasAutorizadas(Connection con) throws SQLException, Exception {
        Statement stQuery = null;
        ResultSet rsQuery = null;
        boolean tieneFirmasAutorizadas = false;
        int cantidad;

        try {
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            StringBuffer SQL = new StringBuffer("SELECT count(num_empleado) as cantidad ");
            SQL.append(" FROM rf_tc_firmas_autorizadas");
            SQL.append(" WHERE num_empleado = ").append(getNumEmpleado());

            rsQuery = stQuery.executeQuery(SQL.toString());
            while (rsQuery.next()) {
                cantidad = rsQuery.getInt("cantidad");
                if (cantidad > 0) {
                    tieneFirmasAutorizadas = true;
                }
            } // Fin while 
        } //Fin try 
        catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo Usuarios.tieneFirmasAutorizadas() " + e.getMessage());
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

        return tieneFirmasAutorizadas;
    } //Fin metodo     
} //Fin clase 
