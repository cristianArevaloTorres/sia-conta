/**
 * Clase  que contiene los metodos que permiten realizar 
 * (A)ltas, (B)ajas y (C)ambios de los registros del catátalogo RF_TC_GRUPO_CLASF_CTA 
 * @version    1.0
 * @author     Jose Alberto Flores Ramirez
 */
package sia.rf.contabilidad.registroContableNuevo.configuracionCuenta;

import java.sql.Connection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sia.db.dao.DaoFactory;
import sia.db.sql.Sentencias;
import sia.db.sql.Vista;

import sia.libs.formato.Error;

public class Grupo extends Genero {

    private int idGrupo;
    
    public Grupo() {
    }

   /** 
    * Genero                Constructror de clase 
    * @param idGrupo        Identificador unico del Grupo (Valores del 1 al 9 ).
    * @param idGenero       Identificador unico del Genero (Valores del 1 al 9 ).
    * @param descripcion    Es el nombre que se le dara al Genero 
    */        
    public Grupo(int idGrupo, int idGenero, String descripcion) {
        setIdGrupo(idGrupo);
        setIdGenero(idGenero);
        setDescripcion(descripcion);
    }
    
    public int getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(int idGrupo) {
        this.idGrupo = idGrupo;
    }
    
    /*Metodo que verifica si existe un registro, si es asi regresa un 1 */
    public int validarGrupo(Connection connection, int idGrupo,  int idGenero ) throws Exception {
        int control = 0;
        Sentencias sentencia    = null;
        Map parametros          = null;
        try {
            parametros = new HashMap();
            sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD, Sentencias.XML);
            parametros.put("idGrupo",  idGrupo);
            parametros.put("idGenero", idGenero);
            List<Vista> registros = new ArrayList<Vista>();
            registros = sentencia.registros("clasificadorCuenta.select.validarGrupo", parametros);
            if(registros != null && registros.size()>0)
                control=1; 
        } catch (Exception e) {
            sia.libs.formato.Error.mensaje(this, e, "SIAFM", "registrarImporteCuenta", e.getMessage());
            connection.rollback();
            throw new Exception(e);
        } finally {
            sentencia  = null;
            parametros = null;
        }
        return control;
    }
    
    public int validarBorradoGrupo(Connection connection, int idGrupo,  int idGenero ) throws Exception {
        int control = 0;
        Sentencias sentencia    = null;
        Map parametros          = null;
        try {
            parametros = new HashMap();
            sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD, Sentencias.XML);
            parametros.put("idGrupo", idGrupo);
            parametros.put("idGenero", idGenero);            
            List<Vista> registros = new ArrayList<Vista>();
            registros = sentencia.registros("clasificadorCuenta.select.validarBorradoGrupo",parametros);
            if(registros != null && registros.size()>0)
                control=1; 
        } catch (Exception e) {
            sia.libs.formato.Error.mensaje(this, e, "SIAFM", "registrarImporteCuenta", e.getMessage());
            connection.rollback();
            throw new Exception(e);
        } finally {
            sentencia  = null;
            parametros = null;
        }
        return control;
    }
    
     public int validarExistenciaRegistro(Connection connection, int idGrupo,  int idGenero ) throws Exception {
        int control = 0;
        Sentencias sentencia    = null;
        Map parametros          = null;
        try {
            parametros = new HashMap();
            sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD, Sentencias.XML);
            parametros.put("idGrupo",    idGrupo);
            parametros.put("idGenero",   idGenero);
            List<Vista> registros = new ArrayList<Vista>();
            registros = sentencia.registros("clasificadorCuenta.select.validarExisteRegistroGrupo",parametros);
            if(registros != null && registros.size()>0)
                control=-1; 
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
    * insertar              Inserta un registro en el catalogo RF_TC_GRUPO_CLASF_CTA 
    * @param connection     Es la conexion al esquema de la B.D.
    * @param idGrupo        Identificador unico del Grupo.
    * @param idGenero       Identificador unico del Genero(Valores del 1 al 9 ).
    * @return int           Regresa un 1 si el registro fue insertado de lo contrario devuelve 0   
    */       
    public int insertar(Connection connection,int idGrupo,  int idGenero, String descripcion ) throws Exception {
        int control = 0;
        Sentencias sentencia    = null;
        Map parametros          = null;
        try {
            parametros = new HashMap();
            sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD, Sentencias.XML);
            parametros.put("idGrupo",     idGrupo);
            parametros.put("idGenero",    idGenero);
            parametros.put("descripcion", descripcion);
            //Pregunta que si existen registros que tengan un idGenero que ya exista en la tabla
            if(validarGrupo(connection,idGrupo,idGenero) == 0){
                control = sentencia.ejecutar(connection,sentencia.getCommand("clasificadorCuenta.insert.insertarGrupo", parametros));
                if(control==1)
                    connection.commit();
            }
        } catch (Exception e) {
            Error.mensaje(this, e, "SIAFM", "registrarImporteCuenta", e.getMessage());
            connection.rollback();
            throw new Exception(e);
        } finally {
            sentencia  = null;
            parametros = null;
        }
        return control;
    }     
     
   /** 
    * eliminar              Elimina un registro del catalogo de la tabla RF_TC_GRUPO_CLASF_CTA 
    * @param connection     Es la conexion al esquema de la B.D.
    * @param idGrupo        Identificador unico del Grupo.
    * @param idGenero       Identificador unico del Genero(Valores del 1 al 9 ).
    * @return int           Regresa un 1 si el registro fue eleminado de lo contrario devuelve 0   
    */       
    public int eliminar(Connection connection, int idGrupo,  int idGenero ) throws Exception {
        int control = 0;
        Sentencias sentencia    = null;
        Map parametros          = null;
        try {
            parametros = new HashMap();
            sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD, Sentencias.XML);
            parametros.put("idGrupo",    idGrupo);
            parametros.put("idGenero",   idGenero);
            //Pregunta si existen regristros de la tabla RF_TC_GRUPO_CLASF_CTA asociados a la
            //tabla RF_TC_CLASE_CLASIF_CTA
            if(validarExistenciaRegistro(connection,idGrupo, idGenero)==0){
            //Pregunta que si no existen registros asociados a la cuenta mayor
                if(validarBorradoGrupo(connection,idGrupo, idGenero) == 1){
                    control = sentencia.ejecutar(connection,sentencia.getCommand("clasificadorCuenta.delete.eliminarGrupo", parametros));
                    if(control==1)
                        connection.commit();
                }
            }
            else control=-1;
        } //end try
        catch (Exception e) {
            sia.libs.formato.Error.mensaje(this, e, "SIAFM", "registrarImporteCuenta", e.getMessage());
            connection.rollback();
            throw new Exception(e);
        } finally {
            sentencia = null;
            parametros = null;
        }
        return control;
    }

   /** 
    * actualizar            Actualiza unicamente la descripcion de un registro de la tabla RF_TC_GRUPO_CLASF_CTA
    * @param connection     Es la conexion al esquema de la B.D.
    * @param idGrupo        Identificador unico del Grupo.
    * @param idGenero       Identificador unico del Genero(Valores del 1 al 9 ).
    * @param descripcion    Es la descripcion del Grupo.Ejem: ACTIVO CIRCULANTE, PASIVO CIRCULANTE etc.
    * @return int           Regresa un 1 si el registro fue actualizado de lo contrario devuelve 0  
    */      
    public int actualizar(Connection connection, int idGrupo,  int idGenero, String descripcion ) throws Exception {
        int control = 0;
        Sentencias sentencia    = null;
        Map parametros          = null;
        try {
            parametros = new HashMap();
            sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD);
            parametros.put("idGenero",   idGenero);
            parametros.put("idGrupo",    idGrupo);
            parametros.put("descripcion",descripcion);
            control = sentencia.ejecutar(sentencia.getCommand("clasificadorCuenta.update.actualizarRegistroGrupo", parametros));
            if(control >= 1)
                connection.commit();
        } catch (Exception e) {
            sia.libs.formato.Error.mensaje(this, e, "SIAFM", "registrarImporteCuenta", e.getMessage());
            connection.rollback();
            throw new Exception(e);
        } finally {
            sentencia  = null;
            parametros = null;
        }
        return control;
    }
}