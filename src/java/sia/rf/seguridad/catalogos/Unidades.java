/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sia.rf.seguridad.catalogos;

import java.sql.Connection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sia.db.sql.Sentencias;
import sia.libs.formato.Error;
import sia.db.dao.DaoFactory;
import sia.db.sql.Vista;

public class Unidades {

    private String unidad_ejecutora;
    private String descripcion;
    private int ambito;

    public Unidades() {
        
    }
    
    public Unidades(String unidad_ejecutora, String descripcion, int ambito) {
        this.unidad_ejecutora = unidad_ejecutora;
        this.descripcion = descripcion;
        this.ambito=ambito;
    }

    public String getIdUnidad() {
        return unidad_ejecutora;
    }

    public void setIdUnidad(String unidad_ejecutora) {
        this.unidad_ejecutora = unidad_ejecutora;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public int getAmbito() {
        return ambito;
    }

    public void setAmbito(int ambito) {
        this.ambito = ambito;
    }

     public int validarUnidad(Connection connection, String unidad_ejecutora, String descripcion, int ambito ) throws Exception {
        int control = 0;
        Sentencias sentencia = null;
        Map parametros = null;
        try {
            parametros = new HashMap();
            sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD, Sentencias.XML);
            parametros.put("unidad_ejecutora", unidad_ejecutora);
            List<Vista> registros = new ArrayList<Vista>();
            registros = sentencia.registros("catalogosAdmin.select.validarUnidad", parametros);
            if(registros != null && registros.size()>0)
                control=1;
            else control =-1;
        } //end try
        catch (Exception e) {
            sia.libs.formato.Error.mensaje(this, e, "SIAFM", "validarUnidad", e.getMessage());
            connection.rollback();
            throw new Exception(e);
        } //end catch(Exception e)
        finally {
            sentencia = null;
            parametros = null;
        }
        return control;
    }
    
    public int insertarUnidad(Connection connection, String unidad_ejecutora, String descripcion, int ambito ) throws Exception {
        int control = 0;
        Sentencias sentencia = null;
        Map parametros = null;
        try {
            parametros = new HashMap();
            sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD, Sentencias.XML);
            parametros.put("unidad_ejecutora", unidad_ejecutora);
            parametros.put("descripcion", descripcion);
            parametros.put("ambito", ambito);
            //Pregunta que si existen registros que tengan un unidad_ejecutora que ya exista en la tabla
             if(validarUnidad(connection,unidad_ejecutora,descripcion,ambito) == -1){
                System.out.println("SQL DE INSERTAR UNIDAD "+sentencia.getCommand("catalogosAdmin.insert.insertarUnidad", parametros));
                  control = sentencia.ejecutar(connection,sentencia.getCommand("catalogosAdmin.insert.insertarUnidad", parametros));
                if(control==1){
                    connection.commit();
                }
            }else control = 0;    
        } //end try
        catch (Exception e) {
            Error.mensaje(this, e, "SIAFM", "insertarUnidad", e.getMessage());
            connection.rollback();
            throw new Exception(e);
        } //end catch(Exception e)
        finally {
            sentencia = null;
            parametros = null;
        }
        return control;
    } //end public int registrarImporteCuenta(int cuentaContableId, double importe.....*/

    public int validarExistenciaRegistro(Connection connection, String unidad_ejecutora ) throws Exception {
        int control = 0;
        Sentencias sentencia = null;
        Map parametros = null;
        try {
            parametros = new HashMap();
            sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD, Sentencias.XML);
            parametros.put("unidad_ejecutora", unidad_ejecutora);            
            List<Vista> registros = new ArrayList<Vista>();
            registros = sentencia.registros("clasificadorCuenta.select.validarExisteRegistroUnidad",parametros);
            if(registros != null && registros.size()>0)
                control = 1;
            else control = 0; 
        } //end try
        catch (Exception e) {
            sia.libs.formato.Error.mensaje(this, e, "SIAFM", "validarBorradoUnidad", e.getMessage());
            connection.rollback();
            throw new Exception(e);
        } //end catch(Exception e)
        finally {
            sentencia = null;
            parametros = null;
        }
        return control;
    }
    
    public int validarBorradoUnidad(Connection connection, String unidad_ejecutora ) throws Exception {
        int control = 0;
        Sentencias sentencia = null;
        Map parametros = null;
        try {
            parametros = new HashMap();
            sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD, Sentencias.XML);
            parametros.put("unidad_ejecutora", unidad_ejecutora);            
            List<Vista> registros = new ArrayList<Vista>();
            registros = sentencia.registros("clasificadorCuenta.select.validarBorradoGenero",parametros);
            if(registros != null && registros.size()>0)
                control=1; 
        } //end try
        catch (Exception e) {
            sia.libs.formato.Error.mensaje(this, e, "SIAFM", "validarBorradoUnidad", e.getMessage());
            connection.rollback();
            throw new Exception(e);
        } //end catch(Exception e)
        finally {
            sentencia = null;
            parametros = null;
        }
        return control;
    }
    
    public int eliminarUnidad(Connection connection, String unidad_ejecutora ) throws Exception {
        int control = 0;
        Sentencias sentencia = null;
        Map parametros = null;
        try {
            parametros = new HashMap();
            sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD, Sentencias.XML);
            parametros.put("unidad_ejecutora",unidad_ejecutora);
            //Pregunta que si no existen registros asociados a la cuenta mayor
            if(validarExistenciaRegistro(connection,unidad_ejecutora) == 0){
                if(validarBorradoUnidad(connection,unidad_ejecutora) == 0){
                    control = sentencia.ejecutar(connection,sentencia.getCommand("catalogosAdmin.delete.eliminarUnidad", parametros));
                    if(control==1)
                        connection.commit();
                }
            } else control=-1;
        } //end try
        catch (Exception e) {
            sia.libs.formato.Error.mensaje(this, e, "SIAFM", "eliminarUnidad", e.getMessage());
            connection.rollback();
            throw new Exception(e);
        } //end catch(Exception e)
        finally {
            sentencia = null;
            parametros = null;
        }
        return control;
    } //end eliminarClase    
    
    public int actualizarUnidad(Connection connection, String unidad_ejecutora, String descripcion, Integer ambito ) throws Exception {
        int control = 0;
        Sentencias sentencia = null;
        Map parametros = null;
        try {
            parametros = new HashMap();
            sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD);
            parametros.put("unidad_ejecutora",   unidad_ejecutora);
            parametros.put("descripcion", descripcion);
            parametros.put("ambito", ambito);
            control = sentencia.ejecutar(sentencia.getCommand("catalogosAdmin.update.actualizarUnidad", parametros));
            if(control >= 1)
                connection.commit();
        } //end try
        catch (Exception e) {
            sia.libs.formato.Error.mensaje(this, e, "SIAFM", "actualizarUnidad", e.getMessage());
            connection.rollback();
            throw new Exception(e);
        } //end catch(Exception e)
        finally {
            sentencia = null;
            parametros = null;
        }
        return control;
    } //end actualizarClase.
}
