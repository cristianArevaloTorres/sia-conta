package sia.db.dao;

import java.sql.Connection;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class DaoFactory {

    private static DaoFactory instance;
    private static Object mutex = null;
    
    public static final int CONEXION_CONTABILIDAD = 0;
    public static final int CONEXION_PRESUPUESTO  = 1;
    public static final int CONEXION_TESORERIA    = 2;
    public static final int CONEXION_ADQUISICIONES= 3;
    public static final int CONEXION_SEGURIDAD    = 4;
    public static final int CONEXION_HERBARIO     = 5;
    public static final int CONEXION_IJURID       = 6;
    public static final int CONEXION_BIBLIOTECA   = 7;

    private static String[] jndiNombresDS= {
      "jdbc/rf_contabilidadDS",
      "jdbc/rf_presupuestoDS",
      "jdbc/rf_tesoreriaDS",
      "jdbc/rm_adquisicionesDS",
      "jdbc/siafmDS",
      "jdbc/rm_w64_bddesa1DS",
      "jdbc/ij_intrumentosDS",
      "jdbc/bibliotecaDS"
    };
	
	public static final String[] rutas = {
	  "contabilidad.cfg.xml",//"jdbc/rf_contabilidadDS",
	  "presupuesto.cfg.xml",//"jdbc/rf_presupuestoDS",
	  "tesoreria.cfg.xml",//"jdbc/rf_tesoreriaDS",
	  "adquisiciones.cfg.xml",//"jdbc/rm_adquisicionesDS",
	  "seguridad.cfg.xml"//"jdbc/siafmDS",
	};
    
    static {
      mutex = new Object();
    }

    private DaoFactory() {
    }

    public static DaoFactory getInstance() {
      if (instance == null) {
        synchronized (mutex) {
          if (instance == null)
            instance = new DaoFactory();
        }
      }
      return instance;
    }

    public static Connection getConnection(String jndi) throws Exception {  //estaba privado
      
      /* ***************** Esto es para el pool de TOMCAT ************************** */
      Connection con = null;
      DataSource ds;
      Context initialContext = null;
      try  {
        initialContext = new InitialContext();
        try
        {
        Context contexto = (Context)initialContext.lookup("java:comp/env");
        ds = (DataSource)contexto.lookup(jndi);
        }
        catch(Exception e)
        {
            System.err.println("DaoFactory "+e.getClass().getName()+": "+e.getMessage());
            ds = (DataSource)initialContext.lookup(jndi);
        }
        //   return ds.getConnection();
        /* Context initialContext = new InitialContext();
        DataSource ds = (DataSource)initialContext.lookup(jndi);*/
        con = ds.getConnection();
        con.createStatement().executeQuery("alter session set nls_numeric_characters='.,'");
        con.commit();
        con.setAutoCommit(false);  
      } catch (Exception ex)  {
        ex.printStackTrace();
        throw ex;
      } finally  {
      }
      return con;
    }
    
    public static Connection getConnection() throws Exception {
      return getConnection(jndiNombresDS[CONEXION_SEGURIDAD]);
    }

    public static Connection getContabilidad() throws Exception {
      return getConnection(jndiNombresDS[CONEXION_CONTABILIDAD]);
    }

    public static Connection getPresupuesto() throws Exception {
      return getConnection(jndiNombresDS[CONEXION_PRESUPUESTO]);
    }

    public static Connection getTesoreria() throws Exception {
      return getConnection(jndiNombresDS[CONEXION_TESORERIA]);
    }

    public static Connection getAdquisiciones() throws Exception {
      return getConnection(jndiNombresDS[CONEXION_ADQUISICIONES]);
    }

    public static Connection getHerbario() throws Exception {
      return getConnection(jndiNombresDS[CONEXION_ADQUISICIONES]);
    }
    
    public static Connection getIJurid() throws Exception {
      return getConnection(jndiNombresDS[CONEXION_IJURID]);
    }
    
    public static Connection getBiblioteca() throws Exception {
      return getConnection(jndiNombresDS[CONEXION_BIBLIOTECA]);
    }

    public static Connection getConnection(int tipoConexion) throws Exception {
      if(isConexionValida(tipoConexion))
        return getConnection(jndiNombresDS[tipoConexion]);
      else
        return null;
    }
    
    public static boolean isConexionValida(int tipoConexion) {
      return tipoConexion>= CONEXION_CONTABILIDAD && tipoConexion<= CONEXION_BIBLIOTECA;
    }
    
    public static void rollback(Connection conn){
      try  {
        if (conn!=null)
          conn.rollback();
      } catch (Exception ex)  {
        System.err.println("Ocurrio un error al deshacer la transaccion");
      } 
    }
    
    public static void closeConnection(Connection conn){
      try  {
        if (conn!=null)
          conn.close();
        conn=null;
      } catch (Exception ex)  {
        System.err.println("Ocurrio un error al cerrar la conexi_n");
      } 
    }
    
}
