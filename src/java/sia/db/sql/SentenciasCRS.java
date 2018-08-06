package sia.db.sql;

import com.sun.rowset.CachedRowSetImpl;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import sia.db.dao.DaoFactory;
import sia.libs.formato.Error;

public class SentenciasCRS extends CachedRowSetImpl {

    private Map parametros;

    public SentenciasCRS() throws Exception, SQLException {
    }

    public void addParamVal(String k, String cad, Object v) {
        if (cad != null && !cad.equals("") && v != null && !v.equals("") && !v.equals("null")) {
            addParam(k, cad.replaceAll(":param", v.toString()));
        } else {
            addParam(k, "");
        }
    }

    public void addParamVal(String k, String cad, Object... v) {
        String param;
        if (v != null) {
            for (int i = 0; i < v.length; i++) {
                if (cad != null && !cad.equals("") && v[i] != null && !v[i].equals("")) {
                    param = v.length == 1 ? ":param" : ":param".concat(String.valueOf(i));
                    cad = cad.replaceAll(param, v[i].toString());
                } else {
                    cad = "";
                    break;
                }
            }
        } else {
            cad = "";
        }
        addParam(k, cad);
    }

    public void addParam(String k, Object v) {
        if (parametros == null) {
            parametros = new HashMap();
        }
        if (k != null && v != null) {
            parametros.put(k, v);
        }
    }

    public void registros(int tipoConexion, String sentencia) throws Exception, SQLException {
        Connection connection = null;
        //System.out.println("sentencia: " + sentencia + " conexion: " + tipoConexion);
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DaoFactory.getConnection(tipoConexion);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sentencia);

            //setCommand(sentencia);
            //execute(connection);
            populate(resultSet);
        } catch (SQLException e) {
            Error.mensaje(e, "SIAFM");
            throw e;
        } finally {

            if (resultSet != null) {
                resultSet.close();
            }

            if (statement != null) {
                statement.close();
            }
            DaoFactory.closeConnection(connection);
        }
    }

    /*public void registros(String sentencia, Connection connection) throws SQLException {
    
     try {
     setCommand(sentencia);
     execute(connection);
     } catch (SQLException e) {
     Error.mensaje(e, "SIAFM");
     throw e;
     }
     }*/
    public String getComando(int tipoConexion, String propiedad, String parametros,
            char token) throws Exception, SQLException {
        SentennciasSE sen;
        String regresa;
        try {
            sen = new SentennciasSE(tipoConexion);
            regresa = sen.getComando(propiedad, parametros, token);
        } catch (Exception ex) {
            System.err.println("Error al obtener los registros");
            throw ex;
        }
        return regresa;
    }

    public String getComando(int tipoConexion, String propiedad, Map parametros) throws Exception, SQLException {
        SentennciasSE sen;
        String regresa;
        try {
            sen = new SentennciasSE(tipoConexion);
            regresa = sen.getCommand(propiedad, parametros);
        } catch (Exception ex) {
            System.err.println("Error al obtener los registros");
            throw ex;
        }
        return regresa;
    }

    public String getComando(int tipoConexion, String propiedad) throws Exception, SQLException {
        return getComando(tipoConexion, propiedad, getParametros());
    }

    public void registros(int tipoConexion, String propiedad, String parametros,
            char token) throws Exception, SQLException {
        registros(tipoConexion, getComando(tipoConexion, propiedad, parametros, token));
    }

    public void registros(int tipoConexion, String propiedad, String parametros) throws Exception {
        registros(tipoConexion, propiedad, parametros, '|');
    }

    //public void registrosMap(Connection con, int tipoConexion, String propiedad) throws Exception {
    //registros(getComando(tipoConexion, propiedad, parametros), con);
    //}
    public void registrosMap(int tipoConexion, String propiedad) throws Exception {
        registros(tipoConexion, getComando(tipoConexion, propiedad, parametros));
    }

    public void liberaParametros() {
        if (parametros != null) {
            Iterator it = parametros.keySet().iterator();
            List keys = new ArrayList();
            while (it.hasNext()) {
                keys.add(it.next());
            }
            for (Object key : keys) {
                parametros.remove(key);
            }
            parametros = null;
        }
    }

    public String getStr(String campo) throws SQLException {
        String regresa = "";
        try {
            regresa = getString(campo) != null && !getString(campo).equals("null") && !getString(campo).equals("") ? getString(campo) : "";
        } catch (Exception ex) {
            System.out.println("Error al obtener el campo: " + campo);
        }
        return regresa;
    }

    public void cerrar() {
        try {
            close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        }
    }

    public void setParametros(Map parametros) {
        this.parametros = parametros;
    }

    public Map getParametros() {
        return parametros;
    }
}
