package sia.ws.publicar.contabilidad;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import sia.db.dao.DaoFactory;
import sia.libs.formato.Error;
import sia.rf.contabilidad.registroContable.formas.Areas;
import sia.rf.contabilidad.registroContable.procesos.polizas.servicios.Polizas;
import sia.rf.contabilidad.registroContableNuevo.bcClasificadorCuentas;
import sia.rf.contabilidad.registroContableNuevo.bcCuentaContable;
import sia.ws.publicar.contabilidad.definicion.PrepararPoliza;

public class Registro {

    private RegistroContable registroContable = null;

    public Registro() {
        registroContable = new RegistroContable();
    }
    private String idregistro;
    private String forma;
    private String unidadeje;
    private String entidad;
    private String ambito;
    private String referencia;
    private String variables;
    private int noempleado;
    private String clave;
    private String fecafecta;
    private String fecenvio;
    private String numpoliza;
    private String stregistro;
    private String modulo;
    private int stwebser;
    private String catalogocuenta;
    private String concepto;
    private String idEvento;
    private String ejercicio;

    /**
     * idregistro
     *
     * @return idregistro
     */
    public String getIdregistro() {
        return idregistro;
    }

    /**
     * idregistro
     *
     * @param idregistro
     */
    public void setIdregistro(String idregistro) {
        this.idregistro = idregistro;
    }

    /**
     * forma
     *
     * @return forma
     */
    public String getForma() {
        return forma;
    }

    /**
     * forma
     *
     * @param forma
     */
    public void setForma(String forma) {
        this.forma = forma;
    }

    /**
     * unidadeje
     *
     * @return unidadeje
     */
    public String getUnidadeje() {
        return unidadeje;
    }

    /**
     * unidadeje
     *
     * @param unidadeje
     */
    public void setUnidadeje(String unidadeje) {
        this.unidadeje = unidadeje;
    }

    /**
     * entidad
     *
     * @return entidad
     */
    public String getEntidad() {
        return entidad;
    }

    /**
     * entidad
     *
     * @param entidad
     */
    public void setEntidad(String entidad) {
        this.entidad = entidad;
    }

    /**
     * ambito
     *
     * @return ambito
     */
    public String getAmbito() {
        return ambito;
    }

    /**
     * ambito
     *
     * @param ambito
     */
    public void setAmbito(String ambito) {
        this.ambito = ambito;
    }

    /**
     * referencia
     *
     * @return referencia
     */
    public String getReferencia() {
        return referencia;
    }

    /**
     * referencia
     *
     * @param referencia
     */
    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    /**
     * variables
     *
     * @return variables
     */
    public String getVariables() {
        return variables;
    }

    /**
     * variables
     *
     * @param variables
     */
    public void setVariables(String variables) {
        this.variables = variables;
    }

    /**
     * noempleado
     *
     * @return noempleado
     */
    public int getNoempleado() {
        return noempleado;
    }

    /**
     * noempleado
     *
     * @param noempleado
     */
    public void setNoempleado(int noempleado) {
        this.noempleado = noempleado;
    }

    /**
     * clave
     *
     * @return clave
     */
    public String getClave() {
        return clave;
    }

    /**
     * clave
     *
     * @param clave
     */
    public void setClave(String clave) {
        this.clave = clave;
    }

    /**
     * fecafecta
     *
     * @return fecafecta
     */
    public String getFecafecta() {
        return fecafecta;
    }

    /**
     * fecafecta
     *
     * @param fecafecta
     */
    public void setFecafecta(String fecafecta) {
        this.fecafecta = fecafecta;
    }

    /**
     * fecenvio
     *
     * @return fecenvio
     */
    public String getFecenvio() {
        return fecenvio;
    }

    /**
     * fecenvio
     *
     * @param fecenvio
     */
    public void setFecenvio(String fecenvio) {
        this.fecenvio = fecenvio;
    }

    /**
     * numpoliza
     *
     * @return numpoliza
     */
    public String getNumpoliza() {
        return numpoliza;
    }

    /**
     * numpoliza
     *
     * @param numpoliza
     */
    public void setNumpoliza(String numpoliza) {
        this.numpoliza = numpoliza;
    }

    /**
     * stregistro
     *
     * @return stregistro
     */
    public String getStregistro() {
        return stregistro;
    }

    /**
     * stregistro
     *
     * @param stregistro
     */
    public void setStregistro(String stregistro) {
        this.stregistro = stregistro;
    }

    /**
     * modulo
     *
     * @return modulo
     */
    public String getModulo() {
        return modulo;
    }

    /**
     * modulo
     *
     * @param modulo
     */
    public void setModulo(String modulo) {
        this.modulo = modulo;
    }

    /**
     * stwebser
     *
     * @return stwebser
     */
    public int getStwerser() {
        return stwebser;
    }

    /**
     * stwebser
     *
     * @param stwebser
     */
    public void setStwebser(int stwebser) {
        this.stwebser = stwebser;
    }

    /**
     * catalogocuenta
     *
     * @return catalogocuenta
     */
    public String getCatalogocuenta() {
        return catalogocuenta;
    }

    /**
     * catalogocuenta
     *
     * @param catalogocuenta
     */
    public void setCatalogocuenta(String catalogocuenta) {
        this.catalogocuenta = catalogocuenta;
    }

    /**
     * concepto
     *
     * @return concepto
     */
    public String getConcepto() {
        return concepto;
    }

    /**
     * concepto
     *
     * @param concepto
     */
    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    /**
     * Metodo que lee la informacion de stiRegistroContable Fecha de creacion:
     * 06 de Noviembre del 2008 Autor: Jorge Luis Perez Navarro Fecha de
     * modificacion: Modificado por:
     */
    public void select_stiRegistroContable(Connection con, String clave) throws SQLException, Exception {
        Statement stQuery = null;
        ResultSet rsQuery = null;
        try {
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            StringBuilder SQL = new StringBuilder("SELECT a.idregistro,a.forma,a.unidadeje,a.entidad,a.ambito,a.referencia,a.variables,a.noempleado,a.clave,a.fecafecta,a.fecenvio,a.numpoliza,a.stregistro,a.modulo");
            SQL.append(" FROM stiRegistroContable a ");
            SQL.append(" WHERE a.LLAVE='").append(clave).append("'");
            System.out.println(SQL.toString());
            rsQuery = stQuery.executeQuery(SQL.toString());
            while (rsQuery.next()) {
                idregistro = (rsQuery.getString("idregistro") == null) ? "" : rsQuery.getString("idregistro");
                forma = (rsQuery.getString("forma") == null) ? "" : rsQuery.getString("forma");
                unidadeje = (rsQuery.getString("unidadeje") == null) ? "" : rsQuery.getString("unidadeje");
                entidad = (rsQuery.getString("entidad") == null) ? "" : rsQuery.getString("entidad");
                ambito = (rsQuery.getString("ambito") == null) ? "" : rsQuery.getString("ambito");
                referencia = (rsQuery.getString("referencia") == null) ? "" : rsQuery.getString("referencia");
                variables = (rsQuery.getString("variables") == null) ? "" : rsQuery.getString("variables");
                noempleado = (rsQuery.getString("noempleado") == null) ? 0 : rsQuery.getInt("noempleado");
                clave = (rsQuery.getString("clave") == null) ? "" : rsQuery.getString("clave");
                fecafecta = (rsQuery.getString("fecafecta") == null) ? "" : rsQuery.getString("fecafecta");
                fecenvio = (rsQuery.getString("fecenvio") == null) ? "" : rsQuery.getString("fecenvio");
                numpoliza = (rsQuery.getString("numpoliza") == null) ? "" : rsQuery.getString("numpoliza");
                stregistro = (rsQuery.getString("stregistro") == null) ? "" : rsQuery.getString("stregistro");
                modulo = (rsQuery.getString("modulo") == null) ? "" : rsQuery.getString("modulo");
                noempleado = (rsQuery.getString("stwebser") == null) ? -1 : rsQuery.getInt("stwebser");
            } // Fin while 
        } //Fin try 
        catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo select_stiRegistroContable " + e.getMessage());
            throw e;
        } //Fin catch 
        finally {
            if (rsQuery != null) {
                rsQuery.close();
            }
            if (stQuery != null) {
                stQuery.close();
            }
        } //Fin finally 
    } //Fin metodo select_stiRegistroContable 

    /**
     * Metodo que inserta la informacion de stiRegistroContable Fecha de
     * creacion: Autor: Fecha de modificacion: Modificado por:
     */
    public void insert_stiRegistroContable(Connection con) throws SQLException, Exception {
        Statement stQuery = null;
        try {
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            StringBuilder SQL = new StringBuilder("INSERT INTO stiRegistroContable( idregistro,forma,unidadeje,entidad,ambito,referencia,variables,noempleado,clave,fecafecta,fecenvio,numpoliza,stregistro,modulo,stwebser) ");
            SQL.append("VALUES(");
            SQL.append("seqRegContable.nextval").append(",");
            SQL.append("'").append(forma).append("',");
            SQL.append("'").append(unidadeje).append("',");
            SQL.append("'").append(entidad).append("',");
            SQL.append("'").append(ambito).append("',");
            SQL.append("'").append(referencia).append("',");
            SQL.append("'").append(variables).append("',");
            SQL.append(noempleado).append(",");
            SQL.append("'").append(clave).append("',");
            SQL.append("to_date('").append(fecafecta).append("','dd/mm/yyyy'),");
            SQL.append("to_date('").append(fecenvio).append("','dd/mm/yyyy'),");
            SQL.append("'").append(numpoliza).append("',");
            SQL.append("'").append(stregistro).append("',");
            SQL.append("'").append(modulo).append("',");
            SQL.append(stwebser).append(")");

            System.out.println(SQL.toString());
            int rs = -1;
            rs = stQuery.executeUpdate(SQL.toString());
        } //Fin try 
        catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo insert_stiRegistroContable " + e.getMessage());
            throw e;
        } //Fin catch 
        finally {
            if (stQuery != null) {
                stQuery.close();
            }
        } //Fin finally 
    } //Fin metodo insert_stiRegistroContable 

    /**
     * Metodo que modifica la informacion de stiRegistroContable Fecha de
     * creacion: Autor: Fecha de modificacion: Modificado por:
     */
    public void update_stiRegistroContable(Connection con, String clave) throws SQLException, Exception {
        Statement stQuery = null;
        try {
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            StringBuilder SQL = new StringBuilder("UPDATE stiRegistroContable");
            SQL.append(" SET idregistro=").append(idregistro).append(",");
            SQL.append("forma=").append("'").append(forma).append("',");
            SQL.append("unidadeje=").append("'").append(unidadeje).append("',");
            SQL.append("entidad=").append("'").append(entidad).append("',");
            SQL.append("ambito=").append("'").append(ambito).append("',");
            SQL.append("referencia=").append("'").append(referencia).append("',");
            SQL.append("variables=").append("'").append(variables).append("',");
            SQL.append("noempleado=").append(noempleado).append(",");
            SQL.append("clave=").append("'").append(clave).append("',");
            SQL.append("fecafecta=").append("to_date('").append(fecafecta).append("','dd/mm/yyyy'),");
            SQL.append("fecenvio=").append("to_date('").append(fecenvio).append("','dd/mm/yyyy'),");
            SQL.append("numpoliza=").append("'").append(numpoliza).append("',");
            SQL.append("stregistro=").append("'").append(stregistro).append("',");
            SQL.append("modulo=").append("'").append(modulo).append("',");
            SQL.append("stwebser=").append(stwebser);
            SQL.append(" WHERE LLAVE='").append(clave).append("'");
            System.out.println(SQL.toString());
            int rs = -1;
            rs = stQuery.executeUpdate(SQL.toString());
        } //Fin try 
        catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo update_stiRegistroContable " + e.getMessage());
            throw e;
        } //Fin catch 
        finally {
            if (stQuery != null) {
                stQuery.close();
            }
        } //Fin finally 
    } //Fin metodo update_stiRegistroContable 

    /**
     * Metodo que borra la informacion de stiRegistroContable Fecha de creacion:
     * Autor: Fecha de modificacion: Modificado por:
     */
    public void delete_stiRegistroContable(Connection con, String clave) throws SQLException, Exception {
        Statement stQuery = null;
        try {
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            StringBuilder SQL = new StringBuilder("DELETE FROM stiRegistroContable a ");
            SQL.append("WHERE a.LLAVE='").append(clave).append("'");
            System.out.println(SQL.toString());
            int rs = -1;
            rs = stQuery.executeUpdate(SQL.toString());
        } //Fin try 
        catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo delete_stiRegistroContable " + e.getMessage());
            throw e;
        } //Fin catch 
        finally {
            if (stQuery != null) {
                stQuery.close();
            }
        } //Fin finally 
    } //Fin metodo delete_stiRegistroContable 

    /**
     * Metodo que lee la informacion de stiRegistroContable Fecha de creacion:
     * 06 de Noviembre del 2008 Autor: Jorge Luis Perez Navarro Fecha de
     * modificacion: Modificado por:
     */
    public String aplicaRegistroContable(Connection conexionContabilidad, String pCatalogoCuenta, int area) throws SQLException, Exception {
        String resultado = "";
        try {
            System.out.println("Cadena sin variables: " + forma + "*" + unidadeje + "*" + entidad + "*" + ambito + "*" + referencia + "*" + noempleado + "*" + clave + "*" + fecenvio + "*" + fecenvio.substring(6, 10) + fecenvio.substring(3, 5) + fecenvio.substring(0, 2));
            if (pCatalogoCuenta.equals("1")) {
                System.out.println("Registro.aplicaRegistroContable.area " + area);
                if (area == Areas.MIPF_NO_ACUMULADO) {
                    resultado = registroContable.formasNoAcumulado(forma, unidadeje, entidad, ambito, referencia, concepto, variables, noempleado, clave, fecenvio.substring(6, 10) + fecenvio.substring(3, 5) + fecenvio.substring(0, 2), conexionContabilidad, idEvento, ejercicio);
                } else {
                    resultado = registroContable.formas(forma, unidadeje, entidad, ambito, referencia, concepto, variables, noempleado, clave, fecenvio.substring(6, 10) + fecenvio.substring(3, 5) + fecenvio.substring(0, 2), conexionContabilidad, idEvento, ejercicio);
                }
            } else if (pCatalogoCuenta.equals("2")) // resultado="";
            {
                resultado = registroContable.formasVentas(forma, unidadeje, entidad, ambito, referencia, concepto, variables, noempleado, clave, fecenvio.substring(6, 10) + fecenvio.substring(3, 5) + fecenvio.substring(0, 2), conexionContabilidad, idEvento, ejercicio);
            } else if (pCatalogoCuenta.equals("3")) //     resultado="";
            {
                resultado = registroContable.formasCostos(forma, unidadeje, entidad, ambito, referencia, concepto, variables, noempleado, clave, fecenvio.substring(6, 10) + fecenvio.substring(3, 5) + fecenvio.substring(0, 2), conexionContabilidad, idEvento, ejercicio);
            }
            //resultado= (String) call.invoke(new Object[] { "BP","112","1","1","10011ADP00173","|UNIDAD=0112|AMBITO=0011|CI=0001|MOVIMIENTO=0002|IMPORTE_MAGNETICO=990|IMPORTE_PUBLICACION=990|REFERENCIA=Prueba Forma BP|",27704,"d90d52f14c86db60f2","20090304"});   
            // System.out.println("Invocar WsContabilidad: " + resultado);
        } catch (Exception e) {
            System.out.println("*Ocurrio un error al accesar al metodo aplicaRegistroContable " + e.getMessage());
            throw e;
        } finally {
        }
        return resultado;
    } //Fin metodo aplicaRegistroContable

    public String AgregaCuentaContable(String cuentaMayor, String subCuentaMayor, String programa, String unidadEjecutora, String entidadAmbito, String nivel6, String nivel7, String pFecha, String descripcion, String nivel, String catalogoCuenta) throws SQLException, Exception {
        return "mal";
    }

    /*   public String AgregaCuentaContable(String cuentaMayor, String subCuentaMayor, String programa, String unidadEjecutora, String entidadAmbito,String nivel6, String nivel7, String pFecha, String descripcion, String nivel, String catalogoCuenta ) throws SQLException, Exception{
     return "mal";
     }
     */
    public String AgregaCuentaContable(String cuentaMayor, String subCuentaMayor, String programa, String unidadEjecutora, String entidadAmbito, String nivel6, String nivel7, String nivel8, String nivel9, String pFecha, String descripcion, String nivel, String catalogoCuenta) throws SQLException, Exception {
        PrepararPoliza prepararPoliza = null;
        String msjNotificacion = null;
        Polizas poliza = null;
        ValidaWs validaWs = null;
        bcCuentaContable bcCuenta = new bcCuentaContable();
        bcClasificadorCuentas bcClasificador = new bcClasificadorCuentas();
        // System.out.println("aaaCuentaContable "+cuentaMayor+"*"+"*"+subCuentaMayor+"*"+programa+"*"+unidadEjecutora+"*"+ entidadAmbito+"*"+ nivel6+"*"+  nivel7+"*"+ nivel8 +"*"+ nivel9+"*"+ pFecha+"*"+descripcion+"*"+ catalogoCuenta);
        String lsResul;
        String lsFecha = pFecha;
        String lsAno = lsFecha.substring(6);
        //String nivel8="";
        //if (lsAno.equals("2011")){
        //      nivel8="0000";
        //}    

        String lsCuentaContable = cuentaMayor + subCuentaMayor + programa + unidadEjecutora + entidadAmbito + nivel6 + nivel7 + nivel8 + nivel9;
        Connection conexion = null;
        try {
            conexion = DaoFactory.getContabilidad();
            lsResul = bcCuenta.select_rf_tr_cuentas_contables_existe(conexion, lsCuentaContable, lsFecha);
            // System.out.println("dddCuentaContableExiste "+lsResul);
            if (!(lsResul.equals(""))) {
                msjNotificacion = "2, La cuenta ya existe.";
            } else {
                lsResul = bcClasificador.select_RF_TC_CLASIFICADOR_CUENTAS_mayor(conexion, cuentaMayor);
                // System.out.println("dddClasificador "+lsResul);
                if (lsResul.equals("")) {
                    msjNotificacion = "0, La cuenta de mayor " + cuentaMayor + " no existe.";
                    System.out.println("0, La cuenta de mayor " + cuentaMayor + " no existe.");
                    throw new Exception("0, La cuenta de mayor " + cuentaMayor + " no existe.");
                } else {
                    bcCuenta.setFecha_vig_ini("01/01/" + lsAno);
                    bcCuenta.setFecha_vig_fin("31/12/2099");
                    bcCuenta.setCuenta_mayor_id(bcClasificador.getCuenta_mayor_id());
                    bcCuenta.setCuenta_contable(lsCuentaContable);
                    bcCuenta.setDescripcion(descripcion);
                    bcCuenta.setId_catalogo_cuenta(catalogoCuenta);
                    bcCuenta.setNivel(nivel);
                    bcCuenta.setRegistro(lsFecha);
                    bcCuenta.insert_rf_tr_cuentas_contables_general(conexion);
                    msjNotificacion = "1, La cuenta fue dada de alta al catalogo de cuentas contables: " + lsCuentaContable;
                }
            }
            //System.out.println("dddCuentaContable "+cuentaMayor);
        } catch (Exception e) {
            // System.out.println("eeeCuentaContable "+cuentaMayor);
            Error.mensaje(e, "SIAFM");
            msjNotificacion = "0," + e.getMessage();
            throw e;
        } finally {
            if (conexion != null) {
                conexion.close();
            }
        }
        return msjNotificacion;
    }

    /**
     * Metodo que busca un cliente para las facturas a credito y si no existe lo
     * da de alta fecha de creacion: 29 de julio de 2009 Autores: Eliud Karina
     * Salazar Martin Jorge Luis Perez Navarro Fecha de modificacion: Modificado
     * por:
     *
     * @param unidadEjecutora
     * @param entidadAmbito
     * @param claAlmContabilidad
     * @param claCliente
     * @param nombreCliente
     * @param pFecha
     * @param clave
     * @return
     * @throws SQLException
     * @throws Exception
     */
    /*    public String aplicaRegistroContableVentas(String unidadEjecutora, String entidadAmbito,String claAlmContabilidad, String claCliente, String nombreCliente, String pFecha, String clave)throws SQLException, Exception{ 
     Service service=null;
     String resultado="";
     try {
     // String endpoint = "http://10.26.4.16:8988/siafm/services/Contabilidad";
     String endpoint= "http://siacontabilidad.senado.gob.mx/siafm/services/Contabilidad";
     service= new Service();
     Call     call   = (Call) service.createCall();
     call.setTargetEndpointAddress(endpoint);
     call.setOperationName("AgregaClienteCuentaContable");
     System.out.println("regConVta"+unidadEjecutora+"*"+entidadAmbito+"*"+claAlmContabilidad+"*"+claCliente+"*"+nombreCliente+"*"+pFecha+"*"+clave);
     resultado= (String) call.invoke(new Object[] {unidadEjecutora, entidadAmbito,claAlmContabilidad, claCliente, nombreCliente, pFecha, clave});
     System.out.println("Invocar WsContabilidad: " + resultado);
     }  
     catch (Exception e) {
     System.out.println("Ocurrio un error al accesar al metodo aplicaRegistroContableVentas "+e.getMessage()); 
     throw e; 
     }finally{
     service=null;
     }
     return resultado;
     } //Fin metodo aplicaRegistroContableVentas
     */
    /**
     * Metodo que lee la informacion de stiRegistroContable Fecha de creacion:
     * 06 de Noviembre del 2008 Autor: Jorge Luis P�rez Navarro Fecha de
     * modificacion: Modificado por:
     */
    /*    public String aplicaRegistroContableTasa()throws SQLException, Exception{
     Service service=null;
     String resultado="";
     try {
     // String endpoint = "http://intranet.siafmpru.senado.gob.mx/siafm/services/Contabilidad";
     // String endpoint = "http://10.26.4.16:8988/siafm/services/Contabilidad";
     String endpoint= "http://siacontabilidad.senado.gob.mx/siafm/services/Contabilidad";
     service= new Service();
     Call     call   = (Call) service.createCall();
     call.setTargetEndpointAddress(endpoint);
     call.setOperationName("formasVentas");
     System.out.println("RegConTasa"+forma+"*"+unidadeje+"*"+entidad+"*"+ambito+"*"+referencia+"*"+variables+"*"+noempleado+"*"+clave+"*"+fecenvio+"*"+fecenvio.substring(6,10)+fecenvio.substring(3,5)+fecenvio.substring(0,2));
     //resultado= (String) call.invoke(new Object[] { "BP","112","1","1","10011ADP00173","|UNIDAD=0112|AMBITO=0011|CI=0001|MOVIMIENTO=0002|IMPORTE_MAGNETICO=990|IMPORTE_PUBLICACION=990|REFERENCIA=Prueba Forma BP|",27704,"d90d52f14c86db60f2","20090304"});
     resultado= (String) call.invoke(new Object[] { forma,unidadeje,entidad,ambito,referencia,variables,noempleado,clave,fecenvio.substring(6,10)+fecenvio.substring(3,5)+fecenvio.substring(0,2)});
     System.out.println("Invocar WsContabilidad: " + resultado);
     }
     catch (Exception e) {
     System.out.println("Ocurrio un error al accesar al metodo aplicaRegistroContable "+e.getMessage());
     throw e;
     }finally{
     service=null;
     }
     return resultado;
     } //Fin metodo aplicaRegistroContable
     */
    /**
     * Metodo que busca un cliente para las facturas a credito y si no existe lo
     * da de alta fecha de creacion: 29 de julio de 2009 Autores: Eliud Karina
     * Salazar Martin Jorge Luis Perez Navarro Fecha de modificacion: Modificado
     * por:
     *
     * @param unidadEjecutora
     * @param entidadAmbito
     * @param claAlmContabilidad
     * @param claCliente
     * @param nombreCliente
     * @param pFecha
     * @param clave
     * @return
     * @throws SQLException
     * @throws Exception
     */
    /*      public String aplicaRegistroContablePromotor(String unidadEjecutora, String entidadAmbito,String claAlmContabilidad, String claPromotor, String nombrePromotor, String pFecha, String clave)throws SQLException, Exception{
     Service service=null;
     String resultado="";
     try {
     //String endpoint = "http://10.26.3.116:8989/siafm/services/Contabilidad";
     String endpoint= "http://siacontabilidad.senado.gob.mx/siafm/services/Contabilidad";
     service= new Service();
     Call     call   = (Call) service.createCall();
     call.setTargetEndpointAddress(endpoint);
     call.setOperationName("AgregaPromotorCuentaContable");
     System.out.println("RegConPro"+unidadEjecutora+"*"+entidadAmbito+"*"+claAlmContabilidad+"*"+claPromotor+"*"+nombrePromotor+"*"+pFecha+"*"+clave);
     resultado= (String) call.invoke(new Object[] {unidadEjecutora, entidadAmbito,claAlmContabilidad, claPromotor, nombrePromotor, pFecha, clave});
     System.out.println("Invocar WsContabilidad: " + resultado);
     }
     catch (Exception e) {
     System.out.println("Ocurrio un error al accesar al metodo aplicaRegistroContablePromotor "+e.getMessage());
     throw e;
     }finally{
     service=null;
     }
     return resultado;
     } */ //Fin metodo aplicaRegistroContablePromotor          
    public void setIdEvento(String idEvento) {
        this.idEvento = idEvento;
    }

    public String getIdEvento() {
        return idEvento;
    }

    public void setEjercicio(String ejercicio) {
        this.ejercicio = ejercicio;
    }

    public String getEjercicio() {
        return ejercicio;
    }
} //Fin clase bcRegistroContable 