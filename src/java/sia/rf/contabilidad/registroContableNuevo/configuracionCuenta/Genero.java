/**
 * Clase que contiene los metodos que permiten realizar 
 * (A)ltas, (B)ajas y (C)ambios de los registros del catátalogo RF_TC_GENERO_CLASF_CTA 
 * @version    1.0
 * @author     Jose Alberto Flores Ramirez
 */

package sia.rf.contabilidad.registroContableNuevo.configuracionCuenta;

import java.sql.Connection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sia.db.sql.Sentencias;
import sia.libs.formato.Error;
import sia.db.dao.DaoFactory;
import sia.db.sql.Vista;

public class Genero {

    private int idGenero;
    private String descripcion;

    public Genero() {
        
    }
 
   /** 
    * Genero                Constructror de clase 
    * @param idGenero       Identificador unico del Genero (Valores del 1 al 9 ).
    * @param descripcion    Es el nombre que se le dara al Genero 
    */     
    public Genero(int idGenero, String descripcion) {
        setIdGenero(idGenero);
        setDescripcion(descripcion);
    }

    public int getIdGenero() {
        return idGenero;
    }

    public void setIdGenero(int idGenero) {
        this.idGenero = idGenero;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int validarGenero(Connection connection, int idGenero ) throws Exception {
        int control = 0;
        Sentencias sentencia    = null;
        Map parametros          = null;
        try {
            parametros = new HashMap();
            sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD, Sentencias.XML);
            parametros.put("idGenero", idGenero);
            List<Vista> registros = new ArrayList<Vista>();
            registros = sentencia.registros("clasificadorCuenta.select.validarGenero", parametros);
            if(registros != null && registros.size()>0)
                control=1;
            else control =-1;
        } catch (Exception e) {
            sia.libs.formato.Error.mensaje(this, e, "SIAFM", "validaGenero", e.getMessage());
            connection.rollback();
            throw new Exception(e);
        } finally {
            sentencia  = null;
            parametros = null;
        }
        return control;
    }
    
    public int validarExistenciaRegistro(Connection connection, int idGenero ) throws Exception {
        int control = 0;
        Sentencias sentencia    = null;
        Map parametros          = null;
        try {
            parametros = new HashMap();
            sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD, Sentencias.XML);
            parametros.put("idGenero", idGenero);            
            List<Vista> registros = new ArrayList<Vista>();
            registros = sentencia.registros("clasificadorCuenta.select.validarExisteRegistroGenero",parametros);
            if(registros != null && registros.size()>0)
                control = 1;
            else control = 0; 
        } catch (Exception e) {
            sia.libs.formato.Error.mensaje(this, e, "SIAFM", "validarBorradoGenero", e.getMessage());
            connection.rollback();
            throw new Exception(e);
        } finally {
            sentencia  = null;
            parametros = null;
        }
        return control;
    }
    
    public int validarBorradoGenero(Connection connection, int idGenero ) throws Exception {
        int control = 0;
        Sentencias sentencia    = null;
        Map parametros          = null;
        try {
            parametros = new HashMap();
            sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD, Sentencias.XML);
            parametros.put("idGenero", idGenero);            
            List<Vista> registros = new ArrayList<Vista>();
            registros = sentencia.registros("clasificadorCuenta.select.validarBorradoGenero",parametros);
            if(registros != null && registros.size()>0)
                control=1; 
        } catch (Exception e) {
            sia.libs.formato.Error.mensaje(this, e, "SIAFM", "validarBorradoGenero", e.getMessage());
            connection.rollback();
            throw new Exception(e);
        } finally {
            sentencia  = null;
            parametros = null;
        }
        return control;
    }

   /** 
    * insertar              Inserta un registro al catalogo de la tabla RF_TC_GENERO_CLASF_CTA 
    * @param connection     Es la conexion al esquema de la B.D.
    * @param idGenero       Identificador unico del Genero (Valores del 1 al 9 ).
    * @param descripcion    Es la descripcion del Genero.Ejem: ACTIVO, PASIVO etc. 
    * @return int           Regresa un 1 si el registro fue insertado, de lo contrario devuelve 0 
    */    
    public int insertar(Connection connection, int idGenero, String descripcion ) throws Exception {
        int control = 0;
        Sentencias sentencia    = null;
        Map parametros          = null;
        try {
            parametros = new HashMap();
            sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD, Sentencias.XML);
            parametros.put("idGenero", idGenero);
            parametros.put("descripcion", descripcion);
            //Pregunta que si existen registros que tengan un idGenero que ya exista en la tabla
            if(validarGenero(connection,idGenero) == -1){
                control = sentencia.ejecutar(connection,sentencia.getCommand("clasificadorCuenta.insert.insertarGenero", parametros));
                if(control==1){
                    connection.commit();
                }
            }else control = 0;    
        } catch (Exception e) {
            Error.mensaje(this, e, "SIAFM", "insertarGenero", e.getMessage());
            connection.rollback();
            throw new Exception(e);
        } finally {
            sentencia  = null;
            parametros = null;
        }
        return control;
    } 
    

   /** 
    * eliminar              Elimina un registro del catalogo de la tabla RF_TC_GENERO_CLASF_CTA 
    * @param connection     Es la conexion al esquema de la B.D.
    * @param idGenero       Identificador unico del Genero (Valores del 1 al 9 ).
    * @return int           Regresa un 1 si el registro fue eleminado de lo contrario devuelve 0 
    */     
    public int eliminar(Connection connection, int idGenero ) throws Exception {
        int control = 0;
        Sentencias sentencia    = null;
        Map parametros          = null;
        try {
            parametros = new HashMap();
            sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD, Sentencias.XML);
            parametros.put("idGenero",   idGenero);
            //Pregunta que si no existen registros asociados a la cuenta mayor
            if(validarExistenciaRegistro(connection,idGenero) == 0){
                //Pregunta que si existe un registro con el id que recibe como parametro para borrarlo
                if(validarBorradoGenero(connection,idGenero) == 0){
                    control = sentencia.ejecutar(connection,sentencia.getCommand("clasificadorCuenta.delete.eliminarGenero", parametros));
                    if(control==1)
                        connection.commit();
                }
            } else control=-1;
        } catch (Exception e) {
            sia.libs.formato.Error.mensaje(this, e, "SIAFM", "eliminarGenero", e.getMessage());
            connection.rollback();
            throw new Exception(e);
        } finally {
            sentencia  = null;
            parametros = null;
        }
        return control;
    }    

    /** 
    * actualizar            Actualiza unicamente la descripcion de un registro de la tabla RF_TC_GENERO_CLASF_CTA 
    * @param connection     Es la conexion al esquema de la B.D.
    * @param idGenero       Identificador unico del Genero(Valores del 1 al 9 ).
    * @param descripcion    Es la descripcion del Genero.Ejem: ACTIVO, PASIVO etc.
    * @return int           Regresa un 1 si el registro fue actualizado de lo contrario devuelve 0      
    */         
    public int actualizar(Connection connection, int idGenero, String descripcion ) throws Exception {
        int control = 0;
        Sentencias sentencia    = null;
        Map parametros          = null;
        try {
            parametros = new HashMap();
            sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD);
            parametros.put("idGenero",   idGenero);
            parametros.put("descripcion",descripcion);
            control = sentencia.ejecutar(sentencia.getCommand("clasificadorCuenta.update.actualizarRegistroGenero", parametros));
            if(control >= 1)
                connection.commit();
        } catch (Exception e) {
            sia.libs.formato.Error.mensaje(this, e, "SIAFM", "actualizarGenero", e.getMessage());
            connection.rollback();
            throw new Exception(e);
        } finally {
            sentencia  = null;
            parametros = null;
        }
        return control;
    }
}