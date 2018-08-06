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

public class Puestos {

    private int id_puesto;
    private String desc_puesto;
   // private int ambito;

    public Puestos() {
        
    }
    
    public Puestos(int id_puesto, String desc_puesto) {
        this.id_puesto = id_puesto;
        this.desc_puesto = desc_puesto;
        //this.ambito=ambito;
    }

    public Integer getIdPuestos() {
        return id_puesto;
    }

    public void setIdPuesto(Integer id_puesto) {
        this.id_puesto = id_puesto;
    }

    public String getDesc_puesto() {
        return desc_puesto;
    }

    public void setDesc_puesto(String desc_puesto) {
        this.desc_puesto = desc_puesto;
    }
    
    public int validarPuestos(Connection connection, Integer id_puesto, String desc_puesto) throws Exception {
        int control = 0;
        Sentencias sentencia = null;
        Map parametros = null;
        try {
            parametros = new HashMap();
            sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD, Sentencias.XML);
            parametros.put("id_puesto", id_puesto);
            List<Vista> registros = new ArrayList<Vista>();
            registros = sentencia.registros("catalogosAdmin.select.validarPuestos", parametros);
            if(registros != null && registros.size()>0)
                control=1;
            else control =-1;
        } //end try
        catch (Exception e) {
            sia.libs.formato.Error.mensaje(this, e, "SIAFM", "validarPuestos", e.getMessage());
            connection.rollback();
            throw new Exception(e);
        } //end catch(Exception e)
        finally {
            sentencia = null;
            parametros = null;
        }
        return control;
    }
    
    public int insertarPuesto(Connection connection, Integer id_puesto, String desc_puesto ) throws Exception {
        int control = 0;
        Sentencias sentencia = null;
        Map parametros = null;
        try {
            parametros = new HashMap();
            sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD, Sentencias.XML);
            parametros.put("id_puesto", id_puesto);
            parametros.put("desc_puesto", desc_puesto);
            //Pregunta que si existen registros que tengan un unidad_ejecutora que ya exista en la tabla
             if(validarPuestos(connection,id_puesto,desc_puesto) == -1){
                System.out.println("SQL DE INSERTAR PUESTO "+sentencia.getCommand("catalogosAdmin.insert.insertarPuesto", parametros));
                  control = sentencia.ejecutar(connection,sentencia.getCommand("catalogosAdmin.insert.insertarPuesto", parametros));
                if(control==1){
                    connection.commit();
                }
            }else control = 0;    
        } //end try
        catch (Exception e) {
            Error.mensaje(this, e, "SIAFM", "insertarPuesto", e.getMessage());
            connection.rollback();
            throw new Exception(e);
        } //end catch(Exception e)
        finally {
            sentencia = null;
            parametros = null;
        }
        return control;
    } //end public int registrarImporteCuenta(int cuentaContableId, double importe.....*/

    public int validarExistenciaRegistro(Connection connection, Integer id_puesto ) throws Exception {
        int control = 0;
        Sentencias sentencia = null;
        Map parametros = null;
        try {
            parametros = new HashMap();
            sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD, Sentencias.XML);
            parametros.put("id_puesto", id_puesto);            
            List<Vista> registros = new ArrayList<Vista>();
            registros = sentencia.registros("catalogosAdmin.select.validarExisteRegistroPuesto",parametros);
            if(registros != null && registros.size()>0)
                control = 1;
            else control = 0; 
        } //end try
        catch (Exception e) {
            sia.libs.formato.Error.mensaje(this, e, "SIAFM", "validarBorradoPuesto", e.getMessage());
            connection.rollback();
            throw new Exception(e);
        } //end catch(Exception e)
        finally {
            sentencia = null;
            parametros = null;
        }
        return control;
    }
    
    public int validarBorradoPuesto(Connection connection, Integer id_puesto ) throws Exception {
        int control = 0;
        Sentencias sentencia = null;
        Map parametros = null;
        try {
            parametros = new HashMap();
            sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD, Sentencias.XML);
            parametros.put("id_puesto", id_puesto);            
            List<Vista> registros = new ArrayList<Vista>();
            registros = sentencia.registros("catalogosAdmin.select.validarBorradoPuesto",parametros);
            if(registros != null && registros.size()>0)
                control=1; 
        } //end try
        catch (Exception e) {
            sia.libs.formato.Error.mensaje(this, e, "SIAFM", "validarBorradoPuesto", e.getMessage());
            connection.rollback();
            throw new Exception(e);
        } //end catch(Exception e)
        finally {
            sentencia = null;
            parametros = null;
        }
        return control;
    }
    
    public int eliminarPuesto(Connection connection, Integer id_puesto ) throws Exception {
        int control = 0;
        Sentencias sentencia = null;
        Map parametros = null;
        try {
            parametros = new HashMap();
            sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD, Sentencias.XML);
            parametros.put("id_puesto",id_puesto);
            //Pregunta que si no existen registros asociados a algún empleado
            if(validarExistenciaRegistro(connection,id_puesto) == 0){
                if(validarBorradoPuesto(connection,id_puesto) == 0){
                    control = sentencia.ejecutar(connection,sentencia.getCommand("catalogosAdmin.delete.eliminarPuesto", parametros));
                    if(control==1)
                        connection.commit();
                }
            } else control=-1;
        } //end try
        catch (Exception e) {
            sia.libs.formato.Error.mensaje(this, e, "SIAFM", "eliminarPuesto", e.getMessage());
            connection.rollback();
            throw new Exception(e);
        } //end catch(Exception e)
        finally {
            sentencia = null;
            parametros = null;
        }
        return control;
    } //end eliminarClase    
    
    public int actualizarPuesto(Connection connection, Integer id_puesto, String desc_puesto ) throws Exception {
        int control = 0;
        Sentencias sentencia = null;
        Map parametros = null;
        try {
            parametros = new HashMap();
            sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD);
            parametros.put("id_puesto",   id_puesto);
            parametros.put("desc_puesto",desc_puesto);
            control = sentencia.ejecutar(sentencia.getCommand("catalogosAdmin.update.actualizarPuesto", parametros));
            if(control >= 1)
                connection.commit();
        } //end try
        catch (Exception e) {
            sia.libs.formato.Error.mensaje(this, e, "SIAFM", "actualizarPuesto", e.getMessage());
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

