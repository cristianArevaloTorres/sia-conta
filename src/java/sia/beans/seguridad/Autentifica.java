 /*
 * Autentifica.java
 * Created on 1 de mayo de 2005, 11:52 AM
 */
package sia.beans.seguridad;

import com.arm.Matching;
import java.sql.SQLException;
import sia.db.dao.DaoFactory;
import sia.db.sql.SentenciasCRS;
import sia.libs.formato.Encriptar;
import sia.libs.recurso.Propiedades;
import sia.ws.consumir.Directorio;

public class Autentifica {

    private StringBuilder error = new StringBuilder();
    private String curp = null;
    private int numeroEmpleado = 0;
    private int tipo = 0;
    private String nombre = null;
    private String genero = null;
    private String login = null;
    private String password = null;
    private String pais = null;
    private String desPais = null;
    private String entidad = null;
    private String desEntidad = null;
    private String desMunicipio = null;
    private String desSiglasEntidad = null;
    private String municipio = null;
    private String unidadEjecutora = null;
    private String desUnidadEjecutora = null;
    private String desSiglasUE = null;
    private String ambito = null;
    private String desAmbito = null;
    private String claveAdscripcion = null;
    private String claveRama = null;
    private String credencial = null;
    private int exentoRegistro = 0;
    private String consecutivo = null;
    private String desRama = null;
    private String grupoAcceso = null;
    private String perfilAcceso = null;
    private String siaAcceso = null;
    private String pagina = null;
    private StringBuilder bitacora = new StringBuilder();
    private String frase = null;
    private String pwGenerico = "9643ed013efd24c67dd2065ffb39";
    private String perfilMantenimiento = "0";
    private int delega = 0;
    private String perfiles = null;
    private boolean registro = false;
    private boolean enviado = false;
    private String ip = null;
    public static final int PRODUCCION = 1;
    public static final int DESARROLLO = 2;
    public static final int HIBRIDO = 3;
    private String contextName;
    /**
     * Obtiene un CachedRowset con los Modulos que el usuario puede acceder.
     */
    public SentenciasCRS modulosDelUsuario = null;

    /**
     * Constructor del Bean de Seguridad, en este m�todo se inicializan las
     * propiedades del bean y las variables de memoria que utilizara el bean.
     */
    public Autentifica() {
        this("default", "");
    }

    /**
     * Constructor del Bean de Seguridad, en este m�todo se inicializan las
     * propiedades del bean y las variables de memoria que utilizara el bean.
     */
    public Autentifica(String contextName, String ip) 
    {
        try 
        {
            setContextName(contextName);
            inicializaUsuario();
            setSiaAcceso("users");
            setIp(ip);

        } catch (Exception e) {
            //new AccesoError(e,getLogin(),getContextName(),getIp());
            System.out.println("[sia.beans.seguridad.Autentifica.constructor] Error: " + e);
        }
    }

    /**
     * Se encarga de inicializar las propiedades del bean relacionadas con el
     * usuario tales como Login,Password, Entidad, UnidadEjecutora, Ambito,
     * ClaveAdsc, CURP, Nombre a vacio ("")
     */
    public void inicializaUsuario() {
        this.curp = "";
        this.nombre = "";
        this.genero = "";
        this.numeroEmpleado = 0;
        //this.login               = "";
        this.password = "";
        this.pais = "147";
        this.desPais = "Mexico";
        this.entidad = "";
        this.desEntidad = "";
        this.municipio = "";
        this.unidadEjecutora = "";
        this.desUnidadEjecutora = "";
        this.ambito = "";
        this.desAmbito = "";
        this.claveAdscripcion = "";
        this.credencial = "";
        this.pagina = "";
        this.error = new StringBuilder();
        this.perfilMantenimiento = "173";
        this.frase = "";
        this.tipo = -1;
        this.registro = false;
    }

    private void getError(String Error, Exception e) throws Exception {
        if (error.toString().trim().isEmpty()) {
            error.append(e.getMessage() != null ? e.getMessage().toUpperCase().indexOf("POINTER") >= 0 ? Error.concat(": NPE") : e.getMessage() : Error);
        }
        throw new Exception(Error, e);
    }

    private boolean procesarEmpleado() throws SQLException, Exception {
        SentenciasCRS rs = null;
        try {
            rs = new SentenciasCRS();
            rs.addParam("numEmpleado", getNumeroEmpleado());
            rs.registrosMap(DaoFactory.CONEXION_CONTABILIDAD, "aut.select.procesarEmpleado.seguridad");
            if (rs.first()) {
                setCurp(rs.getString("CURP"));
                setNombre(rs.getString("NOMBRES") + " " + rs.getString("APELLIDO_PAT") + " " + (rs.getString("APELLIDO_MAT") == null ? "" : rs.getString("APELLIDO_MAT")));
                setGenero(rs.getString("GENERO"));
                setUnidadEjecutora(rs.getString("UNIDAD_EJECUTORA"));
                setDesUnidadEjecutora(rs.getString("DESUNIDAD"));
                setDesSiglasUE(rs.getString("DESSIGLASUE"));
                setAmbito(rs.getString("AMBITO"));
                setDesAmbito(rs.getString("DESAMBITO"));
                setClaveAdscripcion(rs.getString("CLAVE_ADSC_FUNC"));
                setCredencial(rs.getString("CREDENCIAL"));
                setEntidad(rs.getString("ENTIDAD"));
                setDesEntidad(rs.getString("DESENTIDAD"));
                setDesMunicipio(rs.getString("DESMUNICIPIO"));
                setDesSiglasEntidad(rs.getString("DESSIGLASENTIDAD"));
                setMunicipio(rs.getString("MUNICIPIO"));
                setExentoRegistro(rs.getInt("EXENTO"));
            } else {
                throw new Exception("El usuario no se encuentra registrado en el SIA, fallo al recuperar datos de configuración.\n");
            }
        } catch (Exception e) {
            getError("Error al procesar el empleado", e);
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
        return error.length() == 0;
    } // procesarEmpleado

    /**
     * Asigna los M�dulos del sistema que el usuario actual puede acceder.
     * Mediante las propiedades <B>login</B> y <B>password</B>.
     *
     * @throws SQLException En caso de ocurrir algun error se libera esta
     * excepci�n.
     * @return true si pudo realizar la consulta de los modulos o false si
     * ocurrio un error.
     */
    public boolean procesarModulos() throws SQLException, Exception {

        try {
            modulosDelUsuario = new SentenciasCRS();
            if (Propiedades.getInstance().getPropiedad("sistema.sia.dns").equals("1")) {
                modulosDelUsuario.addParam("ruta", "decode(amf.ruta,'','',s.dns||amf.ruta) ruta");
            } else {
                modulosDelUsuario.addParam("ruta", "amf.ruta");
            }
            modulosDelUsuario.addParam("login", getLogin());
            modulosDelUsuario.addParam("idAplicacion", Propiedades.getInstance().getPropiedad("sistema.sia.idAplicacion"));
            modulosDelUsuario.registrosMap(DaoFactory.CONEXION_CONTABILIDAD, "aut.select.RhTcArbolMenu-Inicial.seguridad");
            if (!modulosDelUsuario.first()) {
                modulosDelUsuario = null;
                throw new Exception("Error: El usuario no tiene acceso a ningun modulo");
            }
        } catch (Exception e) {
            modulosDelUsuario = null;
            getError("Error en el procesamiento de los modulos del empleado", e);
        } finally {
        }
        return error.length() == 0;
    }

    private void accesoComun(String cveGrupo, String cvePerfil) throws Exception {
        setSiaAcceso("total");
        setGrupoAcceso(cveGrupo);
        setPerfilAcceso(cvePerfil);
        boolean ok;
        ok = procesarEmpleado();
        if (ok) {
            procesarModulos();
            consecutivo = "0";
            setDesRama("SIA");
            setPagina("valida.jsp");
        }
    } // accesoComun

    private void cargarPerfiles() {
        SentenciasCRS crs;
        StringBuilder sb;
        try {
            crs = new SentenciasCRS();
            sb = new StringBuilder("|");
            crs.addParamVal("numEmpleado", "and num_empleado = :param", getNumeroEmpleado());
            crs.registrosMap(DaoFactory.CONEXION_CONTABILIDAD, "aut.select.RhTrUsuariosPerfil-tabla.seguridad");
            while (crs.next()) {
                sb.append(crs.getString("cve_perfil")).append("|");
            }
            this.perfiles = sb.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean isPerfil(String perfil) {
        return perfiles.indexOf("|".concat(perfil).concat("|")) >= 0;
    }

    public boolean tieneAccesoBD(int numEmpleado) throws Exception {
        SentenciasCRS rs = null;
        Encriptar palabra;
        String log = null;
        String passw = null;
        try {
            setNumeroEmpleado(numEmpleado);
            palabra = new Encriptar();
            rs = new SentenciasCRS();
            rs.addParam("numEmpleado", numEmpleado);
            rs.registrosMap(DaoFactory.CONEXION_CONTABILIDAD, "aut.select.tieneAccesoBD.seguridad");
            rs.beforeFirst();
            if (rs.next()) {
                tieneAccesoBD(rs.getString("LOGIN"), palabra.desencriptar(rs.getString("PASSWORD"), Encriptar.SIA_CLAVE));
            }
        } catch (Exception ex) {
            //new AccesoError(ex,getLogin(),getContextName(),getIp());
            ex.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            log = null;
            passw = null;
        }
        return isErrores();
    }

    private boolean isErrores() throws Exception {
        if (error.length() != 0) {
            throw new Exception(error.toString());
        }
        return error.length() == 0;
    }

    private boolean tieneAccesoComun(String empleado) throws Exception {
        SentenciasCRS rs = null;
        try {
            rs = new SentenciasCRS();
            setNumeroEmpleado(Integer.parseInt(empleado));
            rs.addParam("numEmpleado", getNumeroEmpleado());
            rs.registrosMap(DaoFactory.CONEXION_CONTABILIDAD, "aut.select.tieneAccesoComun.seguridad");
            if (rs.first()) {
                //setTipo(rs.getInt("TIPO"));
                accesoComun(rs.getString("CVE_GRUPO"), rs.getString("CVE_PERFIL"));
            } else {
                throw new Exception("Error: El usuario no tiene acceso al sitio.\n");
            }
        } catch (Exception e) {
            //new AccesoError(e,getLogin(),getContextName(),getIp());
            getError("Error en el metodo de acceso comun", e);
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
        return error.length() == 0;
    } // tieneAccesoComun

    private boolean tieneAccesoDirectorio() throws Exception {
        SentenciasCRS rs = null;
        try {
            setTipo(0);
            rs = new SentenciasCRS();
            Directorio directorio = new Directorio();
            if (directorio.Authenticate(getLogin(), getPassword())) {
                String empleado = directorio.getNumeroEmpleado();
                if (empleado == null) {
                }  // if empleado== null
                tieneAccesoComun(empleado);
            } else {
                throw new Exception("Error: contrase�a incorrecta");
            }
        } catch (Exception e) {
            //new AccesoError(e,getLogin(),getContextName(),getIp());
            getError("Error en el metodo de acceso a Directorio", e);
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
        return error.length() == 0;
    } // tieneAccesoDirectorio

    public int getTipoServicio() {
        return Integer.parseInt(Propiedades.getInstance().getPropiedad("sistema.sia.produccion"));
    }

    public boolean tieneAccesoBD(String login, String password) throws Exception {
        try {
             inicializaUsuario();
            setLogin(login);
            setPassword(password);
            bitacora.delete(0, bitacora.length());
            //inicializa verificacion
            verificaPermisos();
        } catch (Exception ex) {
            //new AccesoError(ex,getLogin(),getContextName(),getIp());
            getError("Error en el metodo de tiene Acceso a la BD", ex);
        } finally {
        }
        return isErrores();
    }

    private void verificaPermisos() throws Exception {
        if (!verificaAdmin()) {
            switch (getTipoServicio()) {
                case PRODUCCION:
                    tieneAccesoDirectorio();
                    break;
                case DESARROLLO:
                    if (!verificaAccesoBD(false)) {
                        throw new Exception("El usuario no tiene acceso al servidor de desarrollo");
                    }
                    break;
                case HIBRIDO:
                    verificaHibrido();
                    break;
            }
        }
        cargarPerfiles();
        //System.out.println("------------ Es el perfil 850 ? ".concat(String.valueOf(isPerfil("850"))));
    }

    private boolean verificaAdmin() throws Exception {
        return verificaAccesoBD(true);
    }

    private void verificaHibrido() throws Exception {
        if (!verificaAccesoBD(false)) {
            tieneAccesoDirectorio();
        }
    }

    private boolean verificaAccesoBD(boolean adm) throws Exception {
        SentenciasCRS rs = null;
        SentenciasCRS rs1 = null;
        boolean regresa = false;
        try {
            rs1 = new SentenciasCRS();
            rs = new SentenciasCRS();
            rs1.addParam("login", getLogin());
            rs.addParam("login", getLogin());
            rs1.registrosMap(DaoFactory.CONEXION_CONTABILIDAD, "aut.select.verificaAcceso.seguridad");
            if (rs1.first()) {
                rs.registrosMap(DaoFactory.CONEXION_CONTABILIDAD, "aut.select.verificaAccesoBD.seguridad");
                if (rs.first()) {
                    Encriptar palabra = new Encriptar();
                    frase = "";
                    if (rs.getString("PASSWORD") != null && rs.getString("PASSWORD").length() > 0) //frase= palabra.desencriptar(rs.getString("PASSWORD"), palabra.SIA_CLAVE);
                    {
                        frase = rs.getString("PASSWORD");//Para utilizar claves sin encriptar!!!!!!!!!!!!
                    }
                    palabra = null;
                    setFrase(frase);
                    if (adm) {
                        if (Matching.analize(getPassword())) {
                            setNumeroEmpleado(rs.getInt("NUM_EMPLEADO"));
                            //setTipo(rs.getInt("TIPO"));
                            accesoComun(rs.getString("CVE_GRUPO"), rs.getString("CVE_PERFIL"));
                            regresa = true;
                        }
                    } else {
                        if (frase.equals(getPassword())) {
                            setNumeroEmpleado(rs.getInt("NUM_EMPLEADO"));
                            //setTipo(rs.getInt("TIPO")); 
                            accesoComun(rs.getString("CVE_GRUPO"), rs.getString("CVE_PERFIL"));
                            regresa = true;
                        }
                    }
                } else {
                    throw new Exception("No tiene acceso a ningún módulo");
                }
            } else {
                throw new Exception("No tiene acceso al sistema");
            }
        } catch (Exception e) {
            //new AccesoError(e,getLogin(),getContextName(),getIp());
            throw (e);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (rs1 != null) {
                rs1.close();
            }
        }
        return regresa;
    } // tieneAccesoBD

    /**
     * Indica si el usuario actual tiene permisos para acceder al M�dulo del
     * sistema dado por la propiedad <B>Modulo</B> si tiene acceso regresa true,
     * y en caso contrario regresa false.
     *
     * @return true si tiene acceso o false si no.
     */
    public boolean validaAccesoModulo(String modulo) {
        try {
            claveRama = modulo;
            if (!modulo.equals("TOTAL")) {
                if (modulosDelUsuario != null) {
                    if (modulosDelUsuario.first()) {
                        modulosDelUsuario.beforeFirst();
                        setDesRama("");
                        try {
                            while (modulosDelUsuario.next()) {
                                if ((modulosDelUsuario.getString("CLAVE").equals(claveRama))
                                        || (modulosDelUsuario.getString("CONSECUTIVO").equals(claveRama))) {
                                    setDesRama(modulosDelUsuario.getString("DES"));
                                    consecutivo = modulosDelUsuario.getString("CONSECUTIVO");
                                    return true;
                                }
                            }
                        } catch (Exception e) {
                            //new AccesoError(e,getLogin(),getContextName(),getIp());
                            error.append("[sia.beans.seguridad.Autentifica.validaAccesoModulo] Error: ").append(e).append(".\n");
                        }
                    }
                }
                throw new Exception("[sia.beans.seguridad.Autentifica.validaAccesoModulo] Error: El usuario '" + getLogin() + "' no tiene acceso al m�dulo {" + modulo + "}.\n");
            } else {
                return true;
            }
        } catch (Exception e) {
            //new AccesoError(e,getLogin(),getContextName(),getIp());
            return false;
        }
    }

    /**
     * Se encarga de liberar los recursos asignados al usuario actual como es la
     * conexion a la base de datos, la memoria asignada, etc.
     */
    public void cerrarSesion() throws Exception {
        try {
            if (getCurp() != null && getCurp().length() > 0) {
                consecutivo = "0";
                setDesRama("SIA");
                setPagina("cerrarSesion.jsp");
            }
            inicializaUsuario();
            setSiaAcceso("users");
        } catch (Exception e) {
            //new AccesoError(e,getLogin(),getContextName(),getIp());
            throw new Exception("Error al cerrar la sesion", e);
        }
    }

    public void setError(String error) {
        this.error.append(error);
    } // setError

    public String getError() {
        return this.error.toString();
    } // getError

    public void setPagina(String pagina) {
        this.pagina = pagina;
    }

    public String getPagina() {
        return this.pagina;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    private void setFrase(String frase) {
        this.frase = frase;
    }

    public String getFrase() {
        return this.frase;
    }

    public void setPwGenerico(String pwGenerico) {
        this.pwGenerico = pwGenerico;
    }

    public String getPwGenerico() {
        return this.pwGenerico;
    }

    public void setPerfilMantenimiento(String perfilMantenimiento) {
        this.perfilMantenimiento = perfilMantenimiento;
    }

    public String getPerfilMantenimiento() {
        return this.perfilMantenimiento;
    }

    public java.lang.String getGenero() {
        return genero;
    }

    public void setGenero(java.lang.String genero) {
        this.genero = genero;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getPais() {
        return pais;
    }

    public void setEntidad(String entidad) {
        this.entidad = entidad;
    }

    public String getEntidad() {
        return entidad;
    }

    public String getEntidadDelUsuario() {
        return entidad + " " + desEntidad;
    }

    public void setDesPais(String desPais) {
        this.desPais = desPais;
    }

    public String getDesPais() {
        return desPais;
    }

    public void setDesEntidad(String desEntidad) {
        this.desEntidad = desEntidad;
    }

    public String getDesEntidad() {
        return desEntidad;
    }

    public void setDesSiglasEntidad(String desSiglasEntidad) {
        this.desSiglasEntidad = desSiglasEntidad;
    }

    public String getDesSiglasEntidad() {
        return desSiglasEntidad;
    }

    public void setUnidadEjecutora(String unidadEjecutora) {
        this.unidadEjecutora = unidadEjecutora;
    }

    public String getUnidadEjecutora() {
        return unidadEjecutora;
    }

    public void setDesUnidadEjecutora(String desUnidadEjecutora) {
        this.desUnidadEjecutora = desUnidadEjecutora;
    }

    public String getDesUnidadEjecutora() {
        return desUnidadEjecutora;
    }

    public void setDesSiglasUE(String desSiglasUE) {
        this.desSiglasUE = desSiglasUE;
    }

    public String getDesSiglasUE() {
        return desSiglasUE;
    }

    public void setAmbito(String ambito) {
        this.ambito = ambito;
    }

    public String getAmbito() {
        return ambito;
    }

    public void setDesAmbito(String desAmbito) {
        this.desAmbito = desAmbito;
    }

    public String getDesAmbito() {
        return desAmbito;
    }

    public void setClaveAdscripcion(String adscripcion) {
        this.claveAdscripcion = adscripcion;
    }

    public String getClaveAdscripcion() {
        return claveAdscripcion;
    }

    public void setSiaAcceso(String siaAcceso) {
        this.siaAcceso = siaAcceso;
    }

    public String getSiaAcceso() {
        return siaAcceso;
    }

    public SentenciasCRS getModulos() throws SQLException {
        return modulosDelUsuario == null ? null : (SentenciasCRS) modulosDelUsuario;
    }

    public java.lang.String getGrupoAcceso() {
        return grupoAcceso;
    }

    public void setGrupoAcceso(java.lang.String grupoAcceso) {
        this.grupoAcceso = grupoAcceso;
    }

    public java.lang.String getPerfilAcceso() {
        return perfilAcceso;
    }

    public void setPerfilAcceso(java.lang.String perfilAcceso) {
        this.perfilAcceso = perfilAcceso;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public String getCurp() {
        return curp;
    }

    public String getCURPDelUsuario() {
        return curp;
    }

    public java.lang.String getDesRama() {
        return desRama;
    }

    public void setDesRama(java.lang.String desRama) {
        this.desRama = desRama;
    }

    public int getNumeroEmpleado() {
        return numeroEmpleado;
    }

    public void setNumeroEmpleado(int numeroEmpleado) {
        this.numeroEmpleado = numeroEmpleado;
    }

    public java.lang.String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(java.lang.String municipio) {
        this.municipio = municipio;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getDelega() {
        return delega;
    }

    public void setDelega(int delega) {
        this.delega = delega;
    }

    public java.lang.String getCredencial() {
        return credencial;
    }

    public void setCredencial(java.lang.String credencial) {
        this.credencial = credencial;
    }

    public boolean isRegistro() {
        return registro;
    }

    public void setRegistro(boolean registro) {
        this.registro = registro;
    }

    public int getExentoRegistro() {
        return exentoRegistro;
    }

    public void setExentoRegistro(int exentoRegistro) {
        this.exentoRegistro = exentoRegistro;
    }

    public boolean isFirmado() {
        return getNumeroEmpleado() != 0;
    }

    public void setEnviado(boolean enviado) {
        this.enviado = enviado;
    }

    public boolean isEnviado() {
        return enviado;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }

    public void setDesMunicipio(String desMunicipio) {
        this.desMunicipio = desMunicipio;
    }

    public String getDesMunicipio() {
        return desMunicipio;
    }

    public void setContextName(String contextName) {
        this.contextName = contextName;
    }

    public String getContextName() {
        return contextName;
    }
}