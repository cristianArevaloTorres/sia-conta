/**
 * Clase  que contiene los metodos que permiten realizar 
 * (A)ltas, (B)ajas y (C)ambios de los registros del catátalogo RF_TC_CLASE_CLASF_CTA 
 * @version    1.0
 * @author     Jose Alberto Flores Ramirez
 */

package sia.rf.contabilidad.registroContableNuevo.configuracionCuenta;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import sia.db.dao.DaoFactory;
import sia.db.sql.Sentencias;
import sia.db.sql.Vista;

public class Clase extends Grupo {
    
    private int idClase;
    
    public Clase() {
    }
    
   /** 
    * Clase                 Constructor de la clase 
    * @param idClase        Identificador unico de la clase(Rubro).
    * @param idGrupo        Identificador unico del Grupo.
    * @param idGenero       Identificador unico del Genero.
    * @páram descripcion    Es la descripcion de la clase     
    */      
    public Clase(int idClase, int idGenero, int idGrupo, String descripcion) {
        setIdClase(idClase);
        setIdGenero(idGenero);
        setIdGrupo(idGrupo);
        setDescripcion(descripcion);
    }

    public int getIdClase() {
        return idClase;
    }

    public void setIdClase(int idClase) {
        this.idClase = idClase;
    }

    public int validarClase(Connection connection, int idClase, int idGrupo,  int idGenero ) throws Exception {
        int control = 0;
        Sentencias sentencia = null;
        Map parametros       = null;
        try {
            parametros = new HashMap();
            sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD, Sentencias.XML);
            parametros.put("idClase", idClase);
            parametros.put("idGrupo", idGrupo);
            parametros.put("idGenero", idGenero);
            List<Vista> registros = new ArrayList<Vista>();
            registros = sentencia.registros("clasificadorCuenta.select.validarClase",parametros);
            if(registros != null && registros.size()>0)
                control=1; 
        } catch (Exception e) {
            sia.libs.formato.Error.mensaje(this, e, "SIAFM", "validarClase", e.getMessage());
            connection.rollback();
            throw new Exception(e);
        } finally {
            sentencia  = null;
            parametros = null;
        }
        return control;
    }

   /** 
    * validarBorradoClase   Verifica la existencia de registros de la tabla RF_TC_CLASE_CLASIF_CTA 
    *                       asociados con el catalogo de cuentas de mayor RF_TC_CLASIFICADOR_CUENTAS 
    * @param connection     Es la conexion al esquema de la B.D.
    * @param idClase        Identificador unico de la clase(Rubro).
    * @param idGrupo        Identificador unico del Grupo.
    * @param idGenero       Identificador unico del Genero.
    * @return int           Regresa el numero de registros arrojados por el por el Query en cuestion      
    */     
    public int validarBorradoClase(Connection connection, int idClase, int idGrupo,  int idGenero ) throws Exception {
        int control = 0;
        Sentencias sentencia = null;
        Map parametros       = null;
        StringBuffer generoGrupoClase = new StringBuffer();
        generoGrupoClase.append(Integer.toString(idGenero)).append(Integer.toString(idGrupo)).append(Integer.toString(idClase));
        try {
            parametros = new HashMap();
            sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD, Sentencias.XML);
            parametros.put("generoGrupoClase",generoGrupoClase.toString());
            List<Vista> registros = new ArrayList<Vista>();
            registros = sentencia.registros("clasificadorCuenta.select.validarBorradoClase",parametros);
            if(registros != null && registros.size()>0)
                control=1; 
        } catch (Exception e) {
            sia.libs.formato.Error.mensaje(this, e, "SIAFM", "validarBorradoClase", e.getMessage());
            connection.rollback();
            throw new Exception(e);
        } finally {
            sentencia  = null;
            parametros = null;
        }
        return control;
    }    
    
    public int validarExistenciaRegistro(Connection connection, int idClase, int idGrupo,  int idGenero ) throws Exception {
        int control = 0;
        Sentencias sentencia = null;
        Map parametros       = null;
        try {
            parametros = new HashMap();
            sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD, Sentencias.XML);
            parametros.put("idClase",    idClase);
            parametros.put("idGrupo",    idGrupo);
            parametros.put("idGenero",   idGenero);
            List<Vista> registros = new ArrayList<Vista>();
            registros = sentencia.registros("clasificadorCuenta.select.validarExisteRegistroClase",parametros);
            if(registros != null && registros.size()>0)
                control=1; 
            else control= -1;
        } catch (Exception e) {
            sia.libs.formato.Error.mensaje(this, e, "SIAFM", "validarBorradoClase", e.getMessage());
            connection.rollback();
            throw new Exception(e);
        } finally {
            sentencia  = null;
            parametros = null;
        }
        return control;
    }

   /** 
    * insertar              Inserta un registro de la tabla RF_TC_CLASE_CLASIF_CTA
    * @param connection     Es la conexion al esquema de la B.D.
    * @param idClase        Identificador unico de la clase(Rubro).
    * @param idGrupo        Identificador unico del Grupo.
    * @param idGenero       Identificador unico del Genero.
    * @param descripcion    Es la descripcion de la clase.Ejem: RESUMEN DE INGRESOS Y GASTOS
    * @return int           Regresa un 1 si el registro fue insertado de lo contrario devuelve 0      
    */     
    public int insertar(Connection connection, int idClase, int idGrupo,  int idGenero,  String descripcion ) throws Exception {
        int control = 0;
        Sentencias sentencia = null;
        Map parametros       = null;
        try {
            parametros  = new HashMap();
            sentencia   = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD, Sentencias.XML);
            parametros.put("idClase",    idClase);
            parametros.put("idGrupo",    idGrupo);
            parametros.put("idGenero",   idGenero);
            parametros.put("descripcion",descripcion);
            //Pregunta que si el Grupo existe y que la clase no exista para que se pueda insertar
            if(validarGrupo(connection,idGrupo,idGenero) == 1 && validarClase(connection,idClase,idGrupo,idGenero) == 0){
                //Si no existen registros, se procede a la insercion del registro  
                control = sentencia.ejecutar(connection,sentencia.getCommand("clasificadorCuenta.insert.insertarClase", parametros));
                if(control==1)
                    connection.commit();
            }
        } catch (Exception e) {
            sia.libs.formato.Error.mensaje(this, e, "SIAFM", "insertarClase", e.getMessage());
            connection.rollback();
            throw new Exception(e);
        } finally {
            sentencia  = null;
            parametros = null;
        }
        return control;
    } 

   /** 
    * eliminar              Elimina un registro de la tabla RF_TC_CLASIF_CTA
    * @param connection     Es la conexion al esquema de la B.D.
    * @param idClase        Identificador unico de la clase.
    * @param idGrupo        Identificador unico del Grupo.
    * @return int           Regresa un 1 si el registro fue eleminado de lo contrario devuelve 0 
    */ 
    public int eliminar(Connection connection, int idClase, int idGrupo,  int idGenero ) throws Exception {
        int control = 0;
        Sentencias sentencia = null;
        Map parametros       = null;
        try {
            parametros  = new HashMap();
            sentencia   = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD, Sentencias.XML);
            parametros.put("idClase",    idClase);
            parametros.put("idGrupo",    idGrupo);
            parametros.put("idGenero",   idGenero);
            //Valida que el registro exista primero
            if(validarExistenciaRegistro(connection,idClase,idGrupo, idGenero) == 1){
                if(validarBorradoClase(connection,idClase,idGrupo, idGenero) == 0){
                    //Si no existen registros, se procede a la insercion del registro  
                    control = sentencia.ejecutar(connection,sentencia.getCommand("clasificadorCuenta.delete.eliminarClase", parametros));
                    if(control==1)
                        connection.commit();
                }
            } else control=-1;
        } catch (Exception e) {
            sia.libs.formato.Error.mensaje(this, e, "SIAFM", "eliminarClase", e.getMessage());
            connection.rollback();
            throw new Exception(e);
        } finally {
            sentencia  = null;
            parametros = null;
        }
        return control;
    } 
   
    /** 
    * actualizar()          Actualiza los datos de un registro de la tabla RF_TC_CLASE_CLASIF_CTA
    * @param connection     Es la conexion al esquema de la B.D.
    * @param idClase        Identificador unico de la clase(Rubro).
    * @param idGrupo        Identificador unico del Grupo.
    * @param idGenero       Identificador unico del Genero.
    * @param descripcion    Es la descripcion de la clase.Ejem: RESUMEN DE INGRESOS Y GASTOS
    * @return int           Regresa un 1 si el registro fue actualizado de lo contrario devuelve 0 
    */     
    public int actualizar(Connection connection, int idClase, int idGrupo,  int idGenero, String descripcion ) throws Exception {
        int control = 0;
        Sentencias sentencia  = null;
        Map parametros        = null;
        try {
            parametros  = new HashMap();
            sentencia   = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD);
            //Recepcion de los parametros con los que se ejecutar la consulta(Query)
            parametros.put("idClase",    idClase);
            parametros.put("idGrupo",    idGrupo);
            parametros.put("idGenero",   idGenero);
            parametros.put("descripcion",descripcion);
            control = sentencia.ejecutar(sentencia.getCommand("clasificadorCuenta.update.actualizarRegistroClase", parametros));
            if(control >= 1)
                connection.commit();
        } catch (Exception e) {
            sia.libs.formato.Error.mensaje(this, e, "SIAFM", "actualizarClase", e.getMessage());
            connection.rollback();
            throw new Exception(e);
        } finally {
            sentencia = null;
            parametros = null;
        }
        return control;
    }
}