package sia.rf.contabilidad.registroContable.clasificadorCuentas.servicios;

import java.io.File;

import java.sql.Connection;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import sia.beans.seguridad.Autentifica;

import sia.db.dao.DaoFactory;

import sia.db.sql.Sentencias;

import sia.db.sql.Vista;

import sia.libs.formato.Cadena;

import sia.libs.formato.Rangos;

import sia.rf.contabilidad.registroContable.procesos.polizas.servicios.Campo;
import sia.rf.contabilidad.registroContable.servicios.Cuenta;

public class ProcesoCatalogoCuentas {

    public static final int COLUMNA_CUENTA_1 = 0;
    public static final int COLUMNA_CUENTA_2 = 1;
    public static final int COLUMNA_CUENTA_3 = 2;
    public static final int COLUMNA_CUENTA_4 = 3;
    public static final int COLUMNA_CUENTA_5 = 4;
    public static final int COLUMNA_CUENTA_6 = 5;
    public static final int COLUMNA_CUENTA_7 = 6;
    public static final int COLUMNA_CUENTA_8 = 7;
    public static final int COLUMNA_CUENTA_9 = 8;
    public static final int COLUMNA_DESCRIPCION = 9;
    public static final int SALDO_CARGO = 10;
    public static final int SALDO_ABONO = 11;
    private String fechaIni;
    private String fechaFin;
    private String fechaRegistro;
    private String idCatalogoCuenta;
    private String nivelMaximo;
    private String accion;
    private String mes;
    private String tipoApertura;
    private StringBuffer regresaCadena;
    private String ejercicio;
    private int numEmpleado;

    public ProcesoCatalogoCuentas(String idCatalogoCuenta, String accion, String ejercicio, String nivelMaximo, int numEmpleado) {
        setIdCatalogoCuenta(idCatalogoCuenta);
        setNivelMaximo(nivelMaximo);
        setAccion(accion);
        setRegresaCadena(new StringBuffer());
        setEjercicio(ejercicio);
        setNumEmpleado(numEmpleado);
    }
    
    public ProcesoCatalogoCuentas(String fechaIni, String fechaFin, String fechaRegistro, String idCatalogoCuenta, String nivelMaximo, String accion, int numEmpleado) {
        setFechaIni(fechaIni);
        setFechaFin(fechaFin);
        setFechaRegistro(fechaRegistro);
        setIdCatalogoCuenta(idCatalogoCuenta);
        setNivelMaximo(nivelMaximo);
        setAccion(accion);
        setRegresaCadena(new StringBuffer());
        setNumEmpleado(numEmpleado);
    }

    public StringBuffer procesarArchivo(String rutaCompleta) throws SQLException, Exception {
        Connection conexion = null;
        File archivo = null;
        Workbook woorkBook = null;
        Sheet sheet = null;
        try {
            conexion = DaoFactory.getContabilidad();
            conexion.setAutoCommit(false);
            archivo = new File(rutaCompleta);
            woorkBook = Workbook.getWorkbook(archivo);
            sheet = woorkBook.getSheet(0);
            if (sheet.getRows() > 0)
                System.out.println("El archivo " + archivo.getName() + " contiene " + sheet.getRows() + " registros.");
            switch (Integer.valueOf(getAccion())) {
                case 1:
                    cargarCuentas(sheet, conexion);
                    break;
                case 2:
                    actualizarSaldos(sheet, conexion);
                    break;
                case 3:
                    actualizarDescripcion(sheet, conexion);
                    break;
                case 4:
                    eliminarCuentas(sheet,conexion);
                  break;
            }
            conexion.commit();
        } catch (Exception e) {
            e.printStackTrace();
            conexion.rollback();
        } finally {
            if (conexion != null)
                conexion.close();
            conexion = null;
        }
        return getRegresaCadena();
    }

    public boolean cargarCuentas(Sheet sheet,  Connection conexion) throws SQLException, Exception {
        Cell[] fila = null;
        List<String> cuentas = null;
        List<String> ctaMayorNoExiste = null;
        HashSet<String> rangosInvalidos = null;
        HashSet<String> sinConfiguracion = null;
        List<String> cuentasErroneas = null;
        HashSet<String> ctaPadreNoExiste = null;
        HashSet<String> ctaPadreConPoliza = null;
        String cuentaContable = null;
        String validaCtaMayor = "";
        String descripcion = null;
        int ejercicio = -1;
        int numeroFila = 1;
        String nivel = "";
        int cuentasExistentes = 0;
        int cuentaMayorId = -1;
        int cuentasAgregadas = 0;
        int cuentaContableId = 0;
        int diferenciaNivel = Integer.valueOf(getNivelMaximo()) - 1 - COLUMNA_CUENTA_9;
        String cuentaContablePadre = null;
        String propiedadIdCuenta = "registroContable.select.obtenerCuentaContableId";
        String propiedadIdCuentaMayor = "validaCuentaContable.select.cuentaMayor";
        //String propiedadInsertaCuenta = "validaCuentaContable.insert.insertarCuentaContable";
        Map parametros = null;
        StringBuffer tablaCtasExistentes = null;
        String codigo = "";
        try {
            parametros = new HashMap();
            cuentas = new ArrayList<String>();
            ctaMayorNoExiste = new ArrayList<String>();
            rangosInvalidos = new HashSet<String>();
            sinConfiguracion = new HashSet<String>();
            cuentasErroneas = new ArrayList<String>();
            ctaPadreNoExiste = new HashSet<String>();
            ctaPadreConPoliza = new HashSet<String>();
            tablaCtasExistentes = new StringBuffer();
            for (; numeroFila < sheet.getRows(); numeroFila++) {
                fila = sheet.getRow(numeroFila);
                cuentas = obtenerCuentaContable(fila);
                if (!validaCtaMayor.equals(cuentas.get(0))) {
                    cuentaContable = "";
                    for (int pocicion = 0; pocicion < cuentas.size(); pocicion++) {
                        cuentaContable += cuentas.get(pocicion);
                    }
                    nivel = obtenerNivelCuenta(cuentas);
                    if(nivel.length()==1){
                      descripcion = fila[COLUMNA_DESCRIPCION + diferenciaNivel].getContents();
                      descripcion = (descripcion == null) ? "Pendiente" : descripcion;
                    cuentaContablePadre = construirCuentaPadre(Integer.valueOf(nivel), cuentas);
                    ejercicio = Integer.valueOf(fechaIni.substring(6, 10));
                    parametros.put("ejercicio", ejercicio);
                    parametros.put("idCatalogoCuenta", getIdCatalogoCuenta());
                    parametros.put("cuentaContable", cuentaContable);
                    cuentaContableId = buscarId(conexion, parametros, propiedadIdCuenta);
                    if (cuentaContableId != -1) {
                        validaCtaMayor = "";
                        cuentasExistentes++;
                        //System.out.println("*** La cuenta ya existe: "+cuentaContable+" ***");
                        tablaCtasExistentes.append("<tr><td>" +  cuentaContable +  "</td></tr>");
                    } else {
  
                        parametros.put("cuentaMayor", cuentas.get(0));
                        cuentaMayorId =  buscarId(conexion, parametros, propiedadIdCuentaMayor);
                        if (cuentaMayorId != -1) {
                            if (!nivel.equals("1")) {
                                if (detallePoliza(conexion,  cuentaContablePadre) != null) {
                                    //System.out.println("** "+cuentaContable+" no puede darse de alta ya que existen polizas en la cuenta padre: "+cuentaContablePadre+" en el nivel "+(nivel-1)+" **");
                                    ctaPadreConPoliza.add(cuentaContablePadre);
                                } else {
                                    parametros.put("cuentaContable",  cuentaContablePadre);
                                    cuentaContableId = buscarId(conexion, parametros,  propiedadIdCuenta);
                                    if (cuentaContableId == -1)
                                        //System.out.println("** "+cuentaContable+" no puede darse de alta ya que no existe la cuenta padre "+cuentaContablePadre+" en el nivel "+(nivel-1)+" **");
                                        ctaPadreNoExiste.add(cuentaContablePadre);
                                        //if(nivel==1)
                                          //break
                                    else {
                                        //validar el rango que aceptar por nivel
                                         /* vista = this.crearVistaCodigo(iCuentaContableId, iNivel+1);
                                          if(vista.getRowCount()!=0){
                                            String codigo = (String)vista.first().getAttribute(6);
                                            Rangos rangos = new Rangos(codigo);
                                            if(!rangos.isDentro(Double.parseDouble(strClave))){
                                              int suma = iNivel+1;
                                              script3.setText("alert ('El nivel "+suma+" solo acepta rangos de "+codigo+"')");*/
                                        
                                        cuentaContableId = buscarId(conexion, parametros, propiedadIdCuenta);
                                        codigo = obtenerRangos(conexion,cuentaContableId,Integer.valueOf(nivel));
                                        if(!codigo.equals("") ){
                                            Rangos rangos = new Rangos(codigo);
                                            if(rangos.isDentro(Double.parseDouble(cuentas.get(Integer.valueOf(nivel)-1)))){
                                               insertarCuenta(conexion, cuentaMayorId,    cuentaContable, Integer.valueOf(nivel), descripcion);
                                               cuentasAgregadas++; 
                                            }else{
                                                rangosInvalidos.add("El valor " +cuentas.get(Integer.valueOf(nivel)-1) + " en el nivel " +nivel+ " de la cuenta de mayor " + cuentas.get(0));
                                                //System.out.println("El valor " +cuentas.get(Integer.valueOf(nivel)-1) + " en el nivel " +nivel+ " de la cuenta contable " + cuentaContable + "no entra dentro del rango configurado");
                                            }
                                        }else{
                                          sinConfiguracion.add("El nivel " + nivel + " de la cuenta de mayor " + cuentas.get(0)+" con valor " +cuentas.get(Integer.valueOf(nivel)-1));
                                          //System.out.println("Falta configurar el nivel " + nivel + " de la cuenta de mayor " + cuentas.get(0));  
                                        }
                                        
                                    }
                                }
                            } else {
                                insertarCuenta(conexion, cuentaMayorId,   cuentaContable, Integer.valueOf(nivel),    descripcion);
                            }
                        } else {
                            ctaMayorNoExiste.add(cuentas.get(0));
                            validaCtaMayor = cuentas.get(0);
                        }
                    }
                  }
                  else{
                      //Cuando se obtenga el nivel y la cuenta no este correcta regresara la cuenta erroena que se insetara en la lista
                      cuentasErroneas.add(nivel);
                  }
                }
            }
            System.out.println("Termino el proceso...");
            getRegresaCadena().append("<p>Termino proceso de carga de cuentas contables.</p>");
            if (cuentasAgregadas == 0)
                getRegresaCadena().append("<p>No se pudieron agregar las cuentas, verifique que el archivo este correcto.</p>");
            else
                getRegresaCadena().append("<p>Se agregaron " +  cuentasAgregadas + " registros</p>");
            if (cuentasExistentes > 0) {
                getRegresaCadena().append("<p>Total de cuentas existentes " + cuentasExistentes + "</p>");
                getRegresaCadena().append("<table width='30%' align='lefth' class='resultado' name='tCuentas' id='tCuentas'>");
                getRegresaCadena().append("<tr><td width='30%' bgcolor='#B6D0F1'>Cuenta contable</td></tr>");
                getRegresaCadena().append(tablaCtasExistentes);
                getRegresaCadena().append("</table>");
            }
            if (ctaMayorNoExiste.size() != 0) {
                getRegresaCadena().append("<p>Las siguientes Cuentas de mayor que no existen,  " + ctaMayorNoExiste + "</p>");
                getRegresaCadena().append("<p>Es necesarios darlas de alta con su configuracion para que se puedan cargar sus cuentas</p>");
            }
            if (ctaPadreNoExiste.size() != 0) {
                getRegresaCadena().append("<p>Las siguientes cuentas Padres no existen por lo tanto no se pudieron dar de alta sus subcuentas.</p>");
                getRegresaCadena().append("<table width='30%' align='lefth' class='resultado' name='tCuentasPadres' id='tCuentasPadres'>");
                getRegresaCadena().append("<tr><td width='30%' bgcolor='#B6D0F1'>Cuentas padres no existentes</td></tr>");
                for (String elemento: ctaPadreNoExiste) {
                    getRegresaCadena().append("<tr><td>" + elemento +   "</td></tr>");
                }
                getRegresaCadena().append("</table>");
            }
            getRegresaCadena().append("<br/><br/>");
            if (cuentasErroneas.size() > 0) {
                getRegresaCadena().append("<p>Las siguientes cuentas contables son erroneas por lo tanto no se cargaron.</p>");
                //getRegresaCadena().append("<p>Total de cuentas existentes "+ cuentasExistentes +"</p>");
                getRegresaCadena().append("<table width='30%' align='lefth' class='resultado' name='tCuentasErroneas' id='tCuentasErroneas'>");
                getRegresaCadena().append("<tr><td width='30%' bgcolor='#B6D0F1'>Cuenta contable erronea</td></tr>");
                for (String elemento: cuentasErroneas) {
                    getRegresaCadena().append("<tr><td>" + elemento + "</td></tr>");
                }
                getRegresaCadena().append("</table>");
            }
            if (rangosInvalidos.size() > 0) {
                getRegresaCadena().append("<p>Rangos no permitidos en el nivel de las cuentas contables</p>");
                //getRegresaCadena().append("<p>Total de cuentas existentes "+ cuentasExistentes +"</p>");
                getRegresaCadena().append("<table width='40%' align='lefth' class='resultado' name='tCuentasErroneas' id='tCuentasErroneas'>");
                getRegresaCadena().append("<tr><td width='40%' bgcolor='#B6D0F1'>Nivel fuera de rango</td></tr>");
                for (String elemento: rangosInvalidos) {
                    getRegresaCadena().append("<tr><td>" + elemento + "</td></tr>");
                }
                getRegresaCadena().append("</table>");
            }
            if (sinConfiguracion.size() > 0) {
                getRegresaCadena().append("<p>Falta configurar los siguientes niveles</p>");
                //getRegresaCadena().append("<p>Total de cuentas existentes "+ cuentasExistentes +"</p>");
                getRegresaCadena().append("<table width='40%' align='lefth' class='resultado' name='tCuentasErroneas' id='tCuentasErroneas'>");
                getRegresaCadena().append("<tr><td width='40%' bgcolor='#B6D0F1'>Nivel sin configuracion</td></tr>");
                for (String elemento: sinConfiguracion) {
                    getRegresaCadena().append("<tr><td>" + elemento + "</td></tr>");
                }
                getRegresaCadena().append("</table>");
            }
            setRegresaCadena(getRegresaCadena());
            /*System.out.println("Termino el proceso de carga de cuentas....");
      System.out.println("Termino proceso de carga de cuentas contables.");
      System.out.println("Se agregaron "+cuentasAgregadas+" registros");
      System.out.println("Total de cuentas existentes "+ cuentasExistentes);
      System.out.println("Cuentas  de mayor que no existen "+ctaMayorNoExiste);
      System.out.println("Las siguientes cuentas Padres no existen "+ctaPadreNoExiste+", por lo tanto no se pudieron dar de alta sus subcuentas.");
      System.out.println("Las siguientes cuentas Padres tienen asociadas polizas "+ctaPadreConPoliza+", por lo tanto no se pudieron dar de alta sus subcuentas.");*/
        } catch (Exception e) {
            e.printStackTrace();
            conexion.rollback();
            getRegresaCadena().append("<p>Error al procesar la fila:" +(numeroFila+1) + "</p>");
            getRegresaCadena().append("<p>Favor de verificar el archivo. Si es la ultima fila sera necesario eliminar al menos unas 10 filas que no tengan datos</p>");
            System.out.println("Error al procesar la fila:" + (numeroFila+1));
        }
        finally{
            parametros = null;
            cuentas = null;
            ctaMayorNoExiste = null;
            rangosInvalidos = null;
            sinConfiguracion = null;
            cuentasErroneas = null;
            ctaPadreNoExiste = null;
            ctaPadreConPoliza = null;
        }
        return true;
    }
    
    public void cargarCuentasConSaldo(Connection conexion, List cuentas, String descripcion){
      String validaCtaMayor = "";
      String cuentaContable = null;
      String nivel = "";
      String cuentaContablePadre = null;
      Map parametros = null;
      int cuentaContableId = 0;
      List<String> ctaMayorNoExiste = null;
      HashSet<String> rangosInvalidos = null;
      HashSet<String> sinConfiguracion = null;
      List<String> cuentasErroneas = null;
      HashSet<String> ctaPadreNoExiste = null;
      HashSet<String> ctaPadreConPoliza = null;
      StringBuffer tablaCtasExistentes = null;
      //List<String> ctaMayorNoExiste = null;
      String codigo = "";
      int cuentasExistentes = 0;
      int cuentaMayorId = -1;
      int cuentasAgregadas = 0;
      String propiedadIdCuenta = "registroContable.select.obtenerCuentaContableId";
      String propiedadIdCuentaMayor = "validaCuentaContable.select.cuentaMayor";
      //String propiedadInsertaCuenta = "validaCuentaContable.insert.cuentaContableSaldo";
      try{
        parametros = new HashMap();
        //cuentas = new ArrayList<String>();
        ctaMayorNoExiste = new ArrayList<String>();
        rangosInvalidos = new HashSet<String>();
        sinConfiguracion = new HashSet<String>();
        cuentasErroneas = new ArrayList<String>();
        ctaPadreNoExiste = new HashSet<String>();
        ctaPadreConPoliza = new HashSet<String>();
        tablaCtasExistentes = new StringBuffer();
        if (!validaCtaMayor.equals(cuentas.get(0))) {
            cuentaContable = "";
            for (int pocicion = 0; pocicion < cuentas.size(); pocicion++) {
                cuentaContable += cuentas.get(pocicion);
            }
            nivel = obtenerNivelCuenta(cuentas);
            if(nivel.length()==1){
              //descripcion = fila[COLUMNA_DESCRIPCION + diferenciaNivel].getContents();
              //descripcion = (descripcion == null) ? "Pendiente" : descripcion;
            cuentaContablePadre = construirCuentaPadre(Integer.valueOf(nivel), cuentas);
            //ejercicio =  getEjercicio();
            parametros.put("ejercicio",  getEjercicio());
            parametros.put("idCatalogoCuenta", getIdCatalogoCuenta());
            parametros.put("cuentaContable", cuentaContable);
            cuentaContableId = buscarId(conexion, parametros, propiedadIdCuenta);
            if (cuentaContableId != -1) {
                validaCtaMayor = "";
                cuentasExistentes++;
                //System.out.println("*** La cuenta ya existe: "+cuentaContable+" ***");
                tablaCtasExistentes.append("<tr><td>" +  cuentaContable +  "</td></tr>");
            } else {

                parametros.put("cuentaMayor", cuentas.get(0));
                cuentaMayorId =  buscarId(conexion, parametros, propiedadIdCuentaMayor);
                if (cuentaMayorId != -1) {
                    if (!nivel.equals("1")) {
                      setFechaIni("01/01/2013");
                        if (detallePoliza(conexion,  cuentaContablePadre) != null) {
                            //System.out.println("** "+cuentaContable+" no puede darse de alta ya que existen polizas en la cuenta padre: "+cuentaContablePadre+" en el nivel "+(nivel-1)+" **");
                            ctaPadreConPoliza.add(cuentaContablePadre);
                        } else {
                            parametros.put("cuentaContable",  cuentaContablePadre);
                            cuentaContableId = buscarId(conexion, parametros,  propiedadIdCuenta);
                            if (cuentaContableId == -1)
                                //System.out.println("** "+cuentaContable+" no puede darse de alta ya que no existe la cuenta padre "+cuentaContablePadre+" en el nivel "+(nivel-1)+" **");
                                ctaPadreNoExiste.add(cuentaContablePadre);
                                //if(nivel==1)
                                  //break
                            else {
                                //validar el rango que aceptar por nivel
                                 /* vista = this.crearVistaCodigo(iCuentaContableId, iNivel+1);
                                  if(vista.getRowCount()!=0){
                                    String codigo = (String)vista.first().getAttribute(6);
                                    Rangos rangos = new Rangos(codigo);
                                    if(!rangos.isDentro(Double.parseDouble(strClave))){
                                      int suma = iNivel+1;
                                      script3.setText("alert ('El nivel "+suma+" solo acepta rangos de "+codigo+"')");*/

                                cuentaContableId = buscarId(conexion, parametros, propiedadIdCuenta);
                                codigo = obtenerRangos(conexion,cuentaContableId,Integer.valueOf(nivel));
                                if(!codigo.equals("") ){
                                    Rangos rangos = new Rangos(codigo);
                                    if(rangos.isDentro(Double.parseDouble(cuentas.get(Integer.valueOf(nivel)-1).toString()))){
                                       insertarCuenta(conexion, cuentaMayorId,    cuentaContable, Integer.valueOf(nivel), descripcion);
                                       cuentasAgregadas++; 
                                    }else{
                                        rangosInvalidos.add("El valor " +cuentas.get(Integer.valueOf(nivel)-1) + " en el nivel " +nivel+ " de la cuenta de mayor " + cuentas.get(0));
                                        //System.out.println("El valor " +cuentas.get(Integer.valueOf(nivel)-1) + " en el nivel " +nivel+ " de la cuenta contable " + cuentaContable + "no entra dentro del rango configurado");
                                    }
                                }else{
                                  sinConfiguracion.add("El nivel " + nivel + " de la cuenta de mayor " + cuentas.get(0)+" con valor " +cuentas.get(Integer.valueOf(nivel)-1));
                                  //System.out.println("Falta configurar el nivel " + nivel + " de la cuenta de mayor " + cuentas.get(0));  
                                }

                            }
                        }
                    } else {
                        insertarCuenta(conexion, cuentaMayorId, cuentaContable, Integer.valueOf(nivel), descripcion);
                    }
                } else {
                    ctaMayorNoExiste.add(cuentas.get(0).toString());
                    validaCtaMayor = cuentas.get(0).toString();
                }
            }
          }
          else{
              //Cuando se obtenga el nivel y la cuenta no este correcta regresara la cuenta erroena que se insetara en la lista
              cuentasErroneas.add(nivel);
          }

        }
        if (cuentasAgregadas == 0)
                getRegresaCadena().append("<p>No se pudieron agregar las cuentas, verifique que el archivo este correcto.</p>");
            else
                getRegresaCadena().append("<p>Se agregaron " +  cuentasAgregadas + " registros</p>");
            if (cuentasExistentes > 0) {
                getRegresaCadena().append("<p>Total de cuentas existentes " + cuentasExistentes + "</p>");
                getRegresaCadena().append("<table width='30%' align='lefth' class='resultado' name='tCuentas' id='tCuentas'>");
                getRegresaCadena().append("<tr><td width='30%' bgcolor='#B6D0F1'>Cuenta contable</td></tr>");
                getRegresaCadena().append(tablaCtasExistentes);
                getRegresaCadena().append("</table>");
            }
            if (ctaMayorNoExiste.size() != 0) {
                getRegresaCadena().append("<p>Las siguientes Cuentas de mayor que no existen,  " + ctaMayorNoExiste + "</p>");
                getRegresaCadena().append("<p>Es necesarios darlas de alta con su configuracion para que se puedan cargar sus cuentas</p>");
            }
            if (ctaPadreNoExiste.size() != 0) {
                getRegresaCadena().append("<p>Las siguientes cuentas Padres no existen por lo tanto no se pudieron dar de alta sus subcuentas.</p>");
                getRegresaCadena().append("<table width='30%' align='lefth' class='resultado' name='tCuentasPadres' id='tCuentasPadres'>");
                getRegresaCadena().append("<tr><td width='30%' bgcolor='#B6D0F1'>Cuentas padres no existentes</td></tr>");
                for (String elemento: ctaPadreNoExiste) {
                    getRegresaCadena().append("<tr><td>" + elemento +   "</td></tr>");
                }
                getRegresaCadena().append("</table>");
            }
            getRegresaCadena().append("<br/><br/>");
            if (cuentasErroneas.size() > 0) {
                getRegresaCadena().append("<p>Las siguientes cuentas contables son erroneas por lo tanto no se cargaron.</p>");
                //getRegresaCadena().append("<p>Total de cuentas existentes "+ cuentasExistentes +"</p>");
                getRegresaCadena().append("<table width='30%' align='lefth' class='resultado' name='tCuentasErroneas' id='tCuentasErroneas'>");
                getRegresaCadena().append("<tr><td width='30%' bgcolor='#B6D0F1'>Cuenta contable erronea</td></tr>");
                for (String elemento: cuentasErroneas) {
                    getRegresaCadena().append("<tr><td>" + elemento + "</td></tr>");
                }
                getRegresaCadena().append("</table>");
            }
            if (rangosInvalidos.size() > 0) {
                getRegresaCadena().append("<p>Rangos no permitidos en el nivel de las cuentas contables</p>");
                //getRegresaCadena().append("<p>Total de cuentas existentes "+ cuentasExistentes +"</p>");
                getRegresaCadena().append("<table width='40%' align='lefth' class='resultado' name='tCuentasErroneas' id='tCuentasErroneas'>");
                getRegresaCadena().append("<tr><td width='40%' bgcolor='#B6D0F1'>Nivel fuera de rango</td></tr>");
                for (String elemento: rangosInvalidos) {
                    getRegresaCadena().append("<tr><td>" + elemento + "</td></tr>");
                }
                getRegresaCadena().append("</table>");
            }
            if (sinConfiguracion.size() > 0) {
                getRegresaCadena().append("<p>Falta configurar los siguientes niveles</p>");
                //getRegresaCadena().append("<p>Total de cuentas existentes "+ cuentasExistentes +"</p>");
                getRegresaCadena().append("<table width='40%' align='lefth' class='resultado' name='tCuentasErroneas' id='tCuentasErroneas'>");
                getRegresaCadena().append("<tr><td width='40%' bgcolor='#B6D0F1'>Nivel sin configuracion</td></tr>");
                for (String elemento: sinConfiguracion) {
                    getRegresaCadena().append("<tr><td>" + elemento + "</td></tr>");
                }
                getRegresaCadena().append("</table>");
            }
            setRegresaCadena(getRegresaCadena());
      }catch(Exception e){
        e.printStackTrace();
      }
    }

    public boolean actualizarSaldos(Sheet sheet, Connection conexion) throws SQLException, Exception {
        int numeroFila = 1;
        List<String> cuentas = null;
        List<String> cuentaContableNoExiste = null;
        Cell[] fila = null;
        String cuentaContable = null;
        Map parametros = null;
        double saldoCargo = 0.00;
        double saldoAbono = 0.00;
        int cuentaContableId = -1;
        int ejercicio = -1;
        int regActualizados=0;
        Campo campo = null;
        //String campoInicial =  getTipoApertura().equals("1") ? "_ini" : "_ini_eli";
        //String propiedadImporte = "webService.select.modificarImporteCuentaContableCarga";
        String propiedadImporte = "webService.select.modificarImporteCuentaContable";
        String propiedadIdCuenta = "registroContable.select.obtenerCuentaContableId";
        String descripcion = "";
        try {
            parametros = new HashMap();
            cuentas = new ArrayList<String>();
            cuentaContableNoExiste = new ArrayList<String>();
            for (; numeroFila < sheet.getRows(); numeroFila++) {
                fila = sheet.getRow(numeroFila);
                cuentas = obtenerCuentaContable(fila);
                cuentaContable = "";
                for (int pocicion = 0; pocicion < cuentas.size(); pocicion++) {
                    cuentaContable += cuentas.get(pocicion);
                }
                saldoCargo = Double.valueOf(fila[SALDO_CARGO].getContents());
                saldoAbono = Double.valueOf(fila[SALDO_ABONO].getContents());
                //ejercicio = Integer.valueOf(fechaIni.substring(6, 10));
                descripcion = fila[COLUMNA_DESCRIPCION].getContents();
                descripcion = (descripcion == null) ? "Pendiente" : descripcion;
                parametros.put("idCatalogoCuenta", getIdCatalogoCuenta());
                parametros.put("cuentaContable", cuentaContable);
                parametros.put("ejercicio", getEjercicio());
                cuentaContableId = buscarId(conexion, parametros, propiedadIdCuenta);
                //parametros.clear();
                System.out.println("messsssssss"+getMes());
                      
                        
                if (cuentaContableId != -1) {
                    parametros.clear();
                    Cuenta cuenta = new Cuenta(cuentaContableId, conexion);
                    ArrayList listaCuenta = cuenta.getPadreId(cuenta.getNivel());
                    campo = new Campo("", String.valueOf(getEjercicio()) + getMes()+"01");
                    for (int i = 0; i < listaCuenta.size(); i++) {
                      if (Integer.valueOf(listaCuenta.get(i).toString()) != -1) {
                        if   (getMes().equals("01")){
                        
                          parametros.put("signo", "+");
                          parametros.put("cuentaContableId", String.valueOf(listaCuenta.get(i)));                        
                          
                          campo.setTipo("cargo");
                          parametros.put("mes", "ene_cargo_ini");
                          parametros.put("importe", saldoCargo);
                          actualizarRegistros(conexion, parametros, propiedadImporte);                    
                          
                          campo.setTipo("abono");
                          parametros.put("mes", "ene_abono_ini");
                          parametros.put("importe", saldoAbono);
                          actualizarRegistros(conexion, parametros, propiedadImporte);
                          regActualizados++;
                        //set :mesCargoIni = :importeCargo, :mesCargoIniEli=:importeCargo, :mesAbonoIni = :importeAbono, :mesAbonoIniEli=:importeAbono
                        /*parametros.put("mesCargoIni", String.valueOf(campo.construirMes()));
                        campo.setTipo("cargo" + "_ini_eli");
                        parametros.put("mesCargoIniEli", String.valueOf(campo.construirMes()));
                        campo.setTipo("abono" + "_ini");
                        parametros.put("mesAbonoIni", String.valueOf(campo.construirMes()));
                        campo.setTipo("abono" + "_ini_eli");
                        parametros.put("mesAbonoIniEli", String.valueOf(campo.construirMes()));
                        parametros.put("importeCargo", saldoCargo);
                        parametros.put("importeAbono", saldoAbono);*/
                        //set :mes = (:mes :signo (:importe)) 
                        }
                        else{                        
                          parametros.put("signo", "+");
                          parametros.put("cuentaContableId", String.valueOf(listaCuenta.get(i)));                        
                          campo.setTipo("cargo");
                          parametros.put("mes", String.valueOf(campo.construirAcum()));
                          parametros.put("importe", saldoCargo);
                          actualizarRegistros(conexion, parametros, propiedadImporte);                    
                          campo.setTipo("abono");
                          parametros.put("mes", String.valueOf(campo.construirAcum()));
                          parametros.put("importe", saldoAbono);
                          actualizarRegistros(conexion, parametros, propiedadImporte);
                          regActualizados++;
                        }
                      }else{
                        System.out.println("El nivel " +i+ "de la cuenta contable " +cuentaContable+ "no pudo modificarse su saldo porque la cuenta no existe.");                                
                      }
                    }
                } else {
                    cuentaContableNoExiste.add(cuentaContable);
                    //insertar la cuenta contable con su saldo correspondiente
                    /*cargarCuentasConSaldo(conexion, cuentas, descripcion);
                    cuentaContableId = buscarId(conexion, parametros, propiedadIdCuenta);
                    parametros.clear();
                    Cuenta cuenta = new Cuenta(cuentaContableId, conexion);
                    ArrayList listaCuenta = cuenta.getPadreId(cuenta.getNivel());
                    campo = new Campo("", String.valueOf(getEjercicio()) + getMes()+"01");
                    for (int i = 0; i < listaCuenta.size(); i++) {
                      if (Integer.valueOf(listaCuenta.get(i).toString()) != -1) {
                        campo.setTipo("cargo");
                        parametros.put("signo", "+");
                        parametros.put("cuentaContableId", String.valueOf(listaCuenta.get(i)));
                        parametros.put("mes", String.valueOf(campo.construirAcum()));
                        parametros.put("importe", saldoCargo);
                        actualizarRegistros(conexion, parametros, propiedadImporte);
                        campo.setTipo("abono");
                        parametros.put("mes", String.valueOf(campo.construirAcum()));
                        parametros.put("importe", saldoAbono);
                        actualizarRegistros(conexion, parametros, propiedadImporte);
                        regActualizados++;
                      }else{
                        System.out.println("El nivel " +i+ "de la cuenta contable " +cuentaContable+ "no pudo modificarse su saldo porque la cuenta no existe.");                                
                      }
                    }
                }*/}
            }
            //conexion.co-
            getRegresaCadena().append("<p>Termino el proceso de actualizacion de saldos.</p>");
            getRegresaCadena().append("<p>Se actualizaron "+String.valueOf(regActualizados)+" registros de un total de "+ String.valueOf(sheet.getRows()) +"</p>");
            System.out.println("Las siguientes cuentas no existen " +  cuentaContableNoExiste +     ", por lo tanto no se pudieron actualizar sus saldos");
        } catch (Exception e) {
            e.printStackTrace();
            conexion.rollback();
            System.out.println("Error al procesar la fila:" + numeroFila);
        } finally {
            campo = null;
            parametros.clear();
            parametros = null;
            cuentas = null;
            cuentaContableNoExiste = null;
            fila = null;
        }
        return true;
    }

    public boolean actualizarDescripcion(Sheet sheet, Connection conexion) throws SQLException, Exception {
        List<String> cuentas = null;
        List<String> cuentaContableNoExiste = null;
        Cell[] fila = null;
        String cuentaContable = null;
        Map parametros = null;
        int numeroFila = 1;
        int nivel = 0;
        String descripcion = null;
        int idCuentaContable = 0;
        String propiedadDescripcion =  "validaCuentaContable.update.cargarCuentasSaldos";
        String propiedadIdCuenta =  "registroContable.select.obtenerCuentaContableId";
        int registrosActualizados = 0;
        try {
            parametros = new HashMap();
            cuentas = new ArrayList<String>();
            cuentaContableNoExiste = new ArrayList<String>();
            for (; numeroFila < sheet.getRows(); numeroFila++) {
                fila = sheet.getRow(numeroFila);
                cuentas = obtenerCuentaContable(fila);
                cuentaContable = "";
                for (int pocicion = 0; pocicion < cuentas.size(); pocicion++) {
                    cuentaContable += cuentas.get(pocicion);
                }
                descripcion = fila[COLUMNA_DESCRIPCION].getContents();
                nivel = Integer.valueOf(obtenerNivelCuenta(cuentas));
                parametros.put("nivel", nivel);
                parametros.put("ejercicio", getEjercicio());
                parametros.put("idCatalogoCuenta", getIdCatalogoCuenta());
                parametros.put("cuentaContable", cuentaContable);
                idCuentaContable =  buscarId(conexion, parametros, propiedadIdCuenta);
                if (idCuentaContable != -1) {
                    parametros.put("idCuentaContable", getIdCatalogoCuenta());
                    parametros.put("descripcion", descripcion);
                    actualizarRegistros(conexion, parametros,  propiedadDescripcion);
                    registrosActualizados++;
                    parametros.clear();
                } else {
                    cuentaContableNoExiste.add(cuentaContable);
                }
            }
            getRegresaCadena().append("<p>Termino el proceso de actualizacion de descripciones.</p>");
            getRegresaCadena().append("<p>Se actualizaron " +  registrosActualizados +  ", registros</p>");
            //System.out.println("Termino el proceso de actualizacion de cuentas....");
            //System.out.println("Se actualizaron "+registrosActualizados+", registros");
            //System.out.println("Las siguientes cuentas no existen "+cuentaContableNoExiste+", por lo tanto no se pudieron actualizar sus descripciones");
            if (cuentaContableNoExiste.size() > 0) {
                getRegresaCadena().append("<p>Las siguientes cuentas contables no existen por lo tanto no se pudieron actualizar sus descripciones</p>");
                //getRegresaCadena().append("<p>Total de cuentas existentes "+ cuentasExistentes +"</p>");
                getRegresaCadena().append("<table width='30%' align='lefth' class='resultado' name='tCuentas' id='tCuentas'>");
                getRegresaCadena().append("<tr><td width='30%' bgcolor='#B6D0F1'>Cuenta contable</td></tr>");
                for (String elemento: cuentaContableNoExiste) {
                    getRegresaCadena().append("<tr><td>" + elemento + 
                                              "</td></tr>");
                }
                getRegresaCadena().append("</table>");
            }
        } catch (Exception e) {
            e.printStackTrace();
            conexion.rollback();
            System.out.println("Error al procesar la fila:" + (numeroFila+1));
            getRegresaCadena().append("<p>Error al procesar la fila:" + (numeroFila+1) + "</p>");
            getRegresaCadena().append("<p>Favor de verificar la fila en la que marca error el archivo. </p>");
        } finally {
            parametros.clear();
            parametros = null;
            cuentas = null;
            cuentaContableNoExiste = null;
            fila = null;
        }
        return true;
    }
    
    public boolean eliminarCuentas(Sheet sheet, Connection conexion) throws SQLException, Exception {
        List<String> cuentas = null;
        List<String> cuentaContableNoExiste = null;
        List<String> cuentasConPoliza = null;
        List<String> cuentasErroneas = null;
        HashSet<Integer> noEliminarCtaPadre = null;
        Cell[] fila = null;
        String cuentaContable = null;
        Map parametros = null;
        int numeroFila = 1;
        String nivel = "";
        int idCuentaContable = -1;
        int polizaId = -1;
        int opercionId = -1;
        String propiedadEliminar =  "validaCuentaContable.delete.eliminarCuenta";
        String propiedadIdCuenta =  "registroContable.select.obtenerCuentaContableId";
        String propiedadPolizaAsociada =  "validaCuentaContable.select.polizaAsociada";
        String propiedadOperacionAsociada =  "validaCuentaContable.select.operacionAsociada";
        int registrosActualizados = 0;
        //Cuenta cuenta = null;
        try {
            parametros = new HashMap();
            cuentas = new ArrayList<String>();
            cuentaContableNoExiste = new ArrayList<String>();
            cuentasConPoliza = new ArrayList<String>();
            cuentasErroneas = new ArrayList<String>();
            noEliminarCtaPadre = new HashSet<Integer>();
            for (; numeroFila < sheet.getRows(); numeroFila++) {
                fila = sheet.getRow(numeroFila);
                cuentas = obtenerCuentaContable(fila);
                cuentaContable = "";
                for (int pocicion = 0; pocicion < cuentas.size(); pocicion++) {
                    cuentaContable += cuentas.get(pocicion);
                }
                
                nivel = obtenerNivelCuenta(cuentas);
                if(nivel.length()==1){
                  parametros.put("nivel", nivel);
                  parametros.put("ejercicio", getEjercicio());
                  parametros.put("idCatalogoCuenta", getIdCatalogoCuenta());
                  parametros.put("cuentaContable", cuentaContable);
                  idCuentaContable =  buscarId(conexion, parametros, propiedadIdCuenta);
                  parametros.clear();
                  if (idCuentaContable != -1) {
                    
                    //Traer los ids de las cuentas padres de la cuenta que tiene asociada una poliza para que no se eliminen
                    parametros.put("cuentaContableId", idCuentaContable);
                    polizaId= buscarId(conexion, parametros, propiedadPolizaAsociada);
                    opercionId= buscarId(conexion, parametros, propiedadOperacionAsociada);
                    if(polizaId == -1 && opercionId == -1){
                     // if(!noEliminarCtaPadre.contains(idCuentaContable)){
                        parametros.put("cuentaContableId", idCuentaContable);
                        actualizarRegistros(conexion, parametros,  propiedadEliminar);
                        registrosActualizados++;
                        parametros.clear();
                      /*}else{
                          cuentasConPoliza.add(cuentaContable);
                      }*/
                    }
                    else{
                        //Cuenta cuenta =  new Cuenta(idCuentaContable,conexion);
                        //cuenta.getPadreId(cuenta.getNivel());
                        //noEliminarCtaPadre.addAll(cuenta.getPadreId(cuenta.getNivel()));
                        cuentasConPoliza.add(cuentaContable);
                        parametros.clear();
                    }
                } else {
                    cuentaContableNoExiste.add(cuentaContable);
                    parametros.clear();
                }
              }
              else{
                  cuentasErroneas.add(cuentaContable);
              }
            }
            getRegresaCadena().append("<p>Termino el proceso de eliminacion de cuentas.</p>");
            getRegresaCadena().append("<p>Se eliminaron " +  registrosActualizados +  ", registros de "+sheet.getRows()+" </p>");
            //System.out.println("Termino el proceso de actualizacion de cuentas....");
            //System.out.println("Se actualizaron "+registrosActualizados+", registros");
            //System.out.println("Las siguientes cuentas no existen "+cuentaContableNoExiste+", por lo tanto no se pudieron actualizar sus descripciones");
            if (cuentaContableNoExiste.size() > 0) {
                getRegresaCadena().append("<p>Las siguientes cuentas contables no existen por lo tanto no se pudieron eliminar</p>");
                //getRegresaCadena().append("<p>Total de cuentas existentes "+ cuentasExistentes +"</p>");
                getRegresaCadena().append("<table width='20%' align='lefth' class='resultado' name='tCuentas' id='tCuentas'>");
                getRegresaCadena().append("<tr><td width='20%' bgcolor='#B6D0F1'>Cuenta contable</td></tr>");
                for (String elemento: cuentaContableNoExiste) {
                    getRegresaCadena().append("<tr><td>" + elemento + 
                                              "</td></tr>");
                }
                getRegresaCadena().append("</table>");
            }
            if (cuentasConPoliza.size() > 0) {
                getRegresaCadena().append("<p>Los siguientes registros no se pueden eliminar porque la cuenta o algunas de sus subcuentas tienen asociadas polizas u operaciones tipo </p>");
                //getRegresaCadena().append("<p>Total de cuentas existentes "+ cuentasExistentes +"</p>");
                getRegresaCadena().append("<table width='20%' align='lefth' class='resultado' name='tCuentas' id='tCuentas'>");
                getRegresaCadena().append("<tr><td width='20%' bgcolor='#B6D0F1'>Cuenta contable</td></tr>");
                for (String elemento: cuentasConPoliza) {
                    getRegresaCadena().append("<tr><td>" + elemento + 
                                              "</td></tr>");
                }
                getRegresaCadena().append("</table>");
            }
            if (cuentasErroneas.size() > 0) {
                getRegresaCadena().append("<p>Las siguientes cuentas contables son errorneas por lo tanto no se pueden eliminar</p>");
                //getRegresaCadena().append("<p>Total de cuentas existentes "+ cuentasExistentes +"</p>");
                getRegresaCadena().append("<table width='20%' align='lefth' class='resultado' name='tCuentas' id='tCuentas'>");
                getRegresaCadena().append("<tr><td width='20%' bgcolor='#B6D0F1'>Cuenta contable</td></tr>");
                for (String elemento: cuentasErroneas) {
                    getRegresaCadena().append("<tr><td>" + elemento + 
                                              "</td></tr>");
                }
                getRegresaCadena().append("</table>");
            }
        } catch (Exception e) {
            e.printStackTrace();
            conexion.rollback();
            System.out.println("Error al procesar la fila:" + (numeroFila+1));
            getRegresaCadena().append("<p>Error al procesar la fila:" + (numeroFila+1) + "</p>");
            getRegresaCadena().append("<p>Favor de verificar la fila en la que marca error el archivo. </p>");
        } finally {
            parametros.clear();
            parametros = null;
            cuentas = null;
            cuentaContableNoExiste = null;
            fila = null;
        }
        return true;
    }

    public List obtenerCuentaContable(Cell[] fila) {
        List<String> cuentas = new ArrayList<String>();
        cuentas.add(fila[COLUMNA_CUENTA_1].getContents());
        cuentas.add(fila[COLUMNA_CUENTA_2].getContents() == null || fila[COLUMNA_CUENTA_2].getContents() == "" ? "0" :fila[COLUMNA_CUENTA_2].getContents());
        cuentas.add(Cadena.rellenar(fila[COLUMNA_CUENTA_3].getContents() == null ? "" :fila[COLUMNA_CUENTA_3].getContents(), 4, '0', true));
        cuentas.add(Cadena.rellenar(fila[COLUMNA_CUENTA_4].getContents() == null ? "" :fila[COLUMNA_CUENTA_4].getContents(), 4, '0', true));
        cuentas.add(Cadena.rellenar(fila[COLUMNA_CUENTA_5].getContents() == null ? "" :fila[COLUMNA_CUENTA_5].getContents(), 4, '0', true));
        cuentas.add(Cadena.rellenar(fila[COLUMNA_CUENTA_6].getContents() == null ? "" :fila[COLUMNA_CUENTA_6].getContents(), 4, '0', true));
        cuentas.add(Cadena.rellenar(fila[COLUMNA_CUENTA_7].getContents() == null ? "" :fila[COLUMNA_CUENTA_7].getContents(), 4, '0', true));
        cuentas.add(Cadena.rellenar(fila[COLUMNA_CUENTA_8].getContents() == null ? "" :fila[COLUMNA_CUENTA_8].getContents(), 4, '0', true));
        cuentas.add(Cadena.rellenar(fila[COLUMNA_CUENTA_9].getContents() == null ? "" :fila[COLUMNA_CUENTA_9].getContents(), 4, '0', true));
        if (Integer.valueOf(getNivelMaximo()) == 10)
            cuentas.add(Cadena.rellenar(fila[COLUMNA_CUENTA_9 + 1].getContents() == null ? "" :  fila[COLUMNA_CUENTA_9].getContents(),4, '0', true));
        if (Integer.valueOf(getNivelMaximo()) == 11)
            cuentas.add(Cadena.rellenar(fila[COLUMNA_CUENTA_9 + 2].getContents() == null ? "" : fila[COLUMNA_CUENTA_9].getContents(),4, '0', true));
        if (Integer.valueOf(getNivelMaximo()) == 12)
            cuentas.add(Cadena.rellenar(fila[COLUMNA_CUENTA_9 +3].getContents() == null ? "" :fila[COLUMNA_CUENTA_9].getContents(), 
                                        4, '0', true));
        if (Integer.valueOf(getNivelMaximo()) == 13) 
          cuentas.add(Cadena.rellenar(fila[COLUMNA_CUENTA_9 + 4].getContents() == null ? "" :fila[COLUMNA_CUENTA_9].getContents(), 4, '0', true));
        return cuentas;
    }

    public String obtenerNivelCuenta(List<String> cuentas) {
        String regresa = "";
        if (!cuentas.get(0).equals("0000") && cuentas.get(1).equals("0") && cuentas.get(2).equals("0000") && cuentas.get(3).equals("0000") && cuentas.get(4).equals("0000") && cuentas.get(5).equals("0000") && cuentas.get(6).equals("0000") && cuentas.get(7).equals("0000") && cuentas.get(8).equals("0000"))
            regresa = "1";
        else if (!cuentas.get(0).equals("0000") && !cuentas.get(1).equals("0") && cuentas.get(2).equals("0000") && cuentas.get(3).equals("0000") && cuentas.get(4).equals("0000") && cuentas.get(5).equals("0000") && cuentas.get(6).equals("0000") && cuentas.get(7).equals("0000") && cuentas.get(8).equals("0000"))
            regresa = "2";
        else if (!cuentas.get(0).equals("0000") && !cuentas.get(1).equals("0") && !cuentas.get(2).equals("0000") && cuentas.get(3).equals("0000") && cuentas.get(4).equals("0000") && cuentas.get(5).equals("0000") && cuentas.get(6).equals("0000") && cuentas.get(7).equals("0000") && cuentas.get(8).equals("0000"))
            regresa = "3";
        else if (!cuentas.get(0).equals("0000") && !cuentas.get(1).equals("0") && !cuentas.get(2).equals("0000") && !cuentas.get(3).equals("0000") && cuentas.get(4).equals("0000") && cuentas.get(5).equals("0000") && cuentas.get(6).equals("0000") && cuentas.get(7).equals("0000") && cuentas.get(8).equals("0000"))
            regresa = "4";
        else if (!cuentas.get(0).equals("0000") && !cuentas.get(1).equals("0") && !cuentas.get(2).equals("0000") && !cuentas.get(3).equals("0000") && !cuentas.get(4).equals("0000") && cuentas.get(5).equals("0000") && cuentas.get(6).equals("0000") && cuentas.get(7).equals("0000") && cuentas.get(8).equals("0000"))
            regresa = "5";
        else if (!cuentas.get(0).equals("0000") && !cuentas.get(1).equals("0") && !cuentas.get(2).equals("0000") && !cuentas.get(3).equals("0000") && !cuentas.get(4).equals("0000") && !cuentas.get(5).equals("0000") && cuentas.get(6).equals("0000") && cuentas.get(7).equals("0000") && cuentas.get(8).equals("0000"))
            regresa = "6";
        else if (!cuentas.get(0).equals("0000") && !cuentas.get(1).equals("0") && !cuentas.get(2).equals("0000") && !cuentas.get(3).equals("0000") && !cuentas.get(4).equals("0000") && !cuentas.get(5).equals("0000") && !cuentas.get(6).equals("0000") && cuentas.get(7).equals("0000") && cuentas.get(8).equals("0000"))
            regresa = "7";
        else if (!cuentas.get(0).equals("0000") && !cuentas.get(1).equals("0") && !cuentas.get(2).equals("0000") && !cuentas.get(3).equals("0000") && !cuentas.get(4).equals("0000") && !cuentas.get(5).equals("0000") && !cuentas.get(6).equals("0000") && !cuentas.get(7).equals("0000") && cuentas.get(8).equals("0000"))
            regresa = "8";
        else if (!cuentas.get(0).equals("0000") && !cuentas.get(1).equals("0") && !cuentas.get(2).equals("0000") && !cuentas.get(3).equals("0000") && !cuentas.get(4).equals("0000") && !cuentas.get(5).equals("0000") && !cuentas.get(6).equals("0000") && !cuentas.get(7).equals("0000") && !cuentas.get(8).equals("0000"))
            regresa = "9";
        else
           regresa = cuentas.get(0).concat(cuentas.get(1).concat(cuentas.get(2)).concat(cuentas.get(3)).concat(cuentas.get(4)).concat(cuentas.get(5)).concat(cuentas.get(6)).concat(cuentas.get(7)).concat(cuentas.get(8)));
           //System.out.println("La cuenta es incorrecta por lo tanto no puede darse de alta");
        return regresa;
    }

    public int buscarId(Connection conexion, Map parametros, String propiedad) throws SQLException, Exception {
        Sentencias sentencia = null;
        List<Vista> vista = null;
        int id = -1;
        try {
            vista = new ArrayList();
            sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD, Sentencias.XML);
            vista = sentencia.registros(sentencia.getComando(propiedad, parametros), conexion);
            if (vista != null) {
                for (Vista reg: vista) {
                  if (propiedad.lastIndexOf("obtenerCuentaContableId") != -1)
                    id = Integer.parseInt(reg.getField("CUENTA_CONTABLE_ID"));
                  else
                    id = Integer.parseInt(reg.getField("ID"));
                  break;

                }         
            }
        } catch (Exception e) {
            e.printStackTrace();
            conexion.rollback();
        } finally {
            sentencia = null;
            vista = null;
        }
        return id;
    }

    public int actualizarRegistros(Connection conexion, Map parametros, String propiedad) throws SQLException, Exception {
        Sentencias sentencia = null;
        int total = 0;
        try {
            sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD, Sentencias.XML);
            total = sentencia.ejecutar(conexion, propiedad, parametros);
        } catch (Exception e) {
            e.printStackTrace();
            conexion.rollback();
        } finally {
            sentencia = null;
        }
        return total;
    }

    public String construirCuentaPadre(int nivel, List<String> cuentas) {
        String cuentaContablePadre = null;
        switch (nivel - 1) {
        case 1:
            {
                cuentaContablePadre = 
                        cuentas.get(0).concat("00000000000000000000000000000");
                break;
            }
        case 2:
            {
                cuentaContablePadre = 
                        cuentas.get(0).concat(cuentas.get(1)).concat("0000000000000000000000000000");
                break;
            }
        case 3:
            {
                cuentaContablePadre = 
                        cuentas.get(0).concat(cuentas.get(1)).concat(cuentas.get(2)).concat("000000000000000000000000");
                break;
            }
        case 4:
            {
                cuentaContablePadre = 
                        cuentas.get(0).concat(cuentas.get(1)).concat(cuentas.get(2)).concat(cuentas.get(3)).concat("00000000000000000000");
                break;
            }
        case 5:
            {
                cuentaContablePadre = 
                        cuentas.get(0).concat(cuentas.get(1)).concat(cuentas.get(2)).concat(cuentas.get(3)).concat(cuentas.get(4)).concat("0000000000000000");
                break;
            }
        case 6:
            {
                cuentaContablePadre = 
                        cuentas.get(0).concat(cuentas.get(1)).concat(cuentas.get(2)).concat(cuentas.get(3)).concat(cuentas.get(4)).concat(cuentas.get(5)).concat("000000000000");
                break;
            }
        case 7:
            {
                cuentaContablePadre = 
                        cuentas.get(0).concat(cuentas.get(1)).concat(cuentas.get(2)).concat(cuentas.get(3)).concat(cuentas.get(4)).concat(cuentas.get(5)).concat(cuentas.get(6)).concat("00000000");
                break;
            }
        case 8:
            {
                cuentaContablePadre = 
                        cuentas.get(0).concat(cuentas.get(1)).concat(cuentas.get(2)).concat(cuentas.get(3)).concat(cuentas.get(4)).concat(cuentas.get(5)).concat(cuentas.get(6)).concat(cuentas.get(7)).concat("0000");
                break;
            }
        case 9:
            {
                cuentaContablePadre = 
                        cuentas.get(0).concat(cuentas.get(1)).concat(cuentas.get(2)).concat(cuentas.get(3)).concat(cuentas.get(4)).concat(cuentas.get(5)).concat(cuentas.get(6)).concat(cuentas.get(7)).concat(cuentas.get(8));
                break;
            }
        }
        return cuentaContablePadre;
    }

    public List<Vista> detallePoliza(Connection conexion, 
                                     String cuentaContablePadre) throws SQLException, 
                                                                        Exception {
        Sentencias sentencia = null;
        List<Vista> registros = null;
        Map parametros = null;
        try {
            parametros = new HashMap();
            registros = new ArrayList<Vista>();
            sentencia = 
                    new Sentencias(DaoFactory.CONEXION_CONTABILIDAD, Sentencias.XML);
            parametros.put("cuentaContable", cuentaContablePadre);
            parametros.put("ejercicio", getFechaIni().substring(6, 10));
            parametros.put("idCatalogoCuenta", getIdCatalogoCuenta());
            registros = sentencia.registros(sentencia.getComando("validaCuentaContable.select.consultaDetPol", parametros), conexion);
        } catch (Exception e) {
            e.printStackTrace();
            conexion.rollback();
        } finally {
            sentencia = null;
            parametros.clear();
            parametros = null;
        }
        return registros;
    }
    
    public String obtenerRangos(Connection conexion, int cuentaContableId, int nivel) throws SQLException, Exception {
        Sentencias sentencia = null;
        List<Vista> registros = null;
        String regresa = "";
        Map parametros = null;
        try {
            parametros = new HashMap();
            registros = new ArrayList<Vista>();
            sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD, Sentencias.XML);
            parametros.put("cuentaContableId", cuentaContableId);
            parametros.put("orden", nivel);
            parametros.put("idCatalogoCuenta", getIdCatalogoCuenta());
            registros = sentencia.registros(sentencia.getComando("validaCuentaContable.select.configuracionCuenta", parametros), conexion);
            if(registros != null){
              for(Vista registro: registros){
                regresa = registro.getField("CODIGO");
              }
            }
        } catch (Exception e) {
            e.printStackTrace();
            conexion.rollback();
        } finally {
            sentencia = null;
            parametros.clear();
            parametros = null;
        }
        return regresa;
    }

    private boolean insertarCuenta(Connection conexion, int cuentaMayorId, String cuentaContable, int nivel, String descripcion) throws SQLException, Exception {
        Map parametros = null;
        Sentencias sentencias = null;
        boolean inserto = false;
        try {
            sentencias = 
                    new Sentencias(DaoFactory.CONEXION_CONTABILIDAD, Sentencias.XML);
            parametros = new HashMap();
            parametros.put("idCuentaMayor", cuentaMayorId);
            parametros.put("cuentaContable", cuentaContable);
            parametros.put("nivel", nivel);
            parametros.put("descripcion", descripcion);
            parametros.put("fechaIni", getFechaIni());
            parametros.put("fechaFin", getFechaFin());
            parametros.put("numEmpleado", getNumEmpleado());
            parametros.put("fechaAlta", getFechaRegistro());
            parametros.put("idCatalogoCuenta", idCatalogoCuenta);
            if (sentencias.ejecutar(conexion, "validaCuentaContable.insert.insertarCuentaContable", parametros) != -1)
                inserto = true;
        } catch (Exception e) {
            e.printStackTrace();
            conexion.rollback();
        }
        return inserto;
    }

    public void setFechaIni(String fechaIni) {
        this.fechaIni = fechaIni;
    }

    public String getFechaIni() {
        return fechaIni;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setIdCatalogoCuenta(String idCatalogoCuenta) {
        this.idCatalogoCuenta = idCatalogoCuenta;
    }

    public String getIdCatalogoCuenta() {
        return idCatalogoCuenta;
    }

    public void setNivelMaximo(String nivelMaximo) {
        this.nivelMaximo = nivelMaximo;
    }
    
    public String getNivelMaximo() {
        return nivelMaximo;
    }

   /* public String getNivelMaximo() {
        System.out.println("Metodo para obtener el nivel maximo");
        int nivelMaximo = -1;
        Sentencias sentencia = null;
        List<Vista> vista = null;
        int id = -1;
        try {
            
            vista = new ArrayList();
            sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD, Sentencias.XML);
            vista = sentencia.registros(sentencia.getComando("validaCuentaContable.select.longitudMax", parametros), conexion);
            if (vista != null) {
                for (Vista reg: vista) {
                    if (propiedad.lastIndexOf("obtenerCuentaContableId") != -1)
                        id = Integer.parseInt(reg.getField("CUENTA_CONTABLE_ID"));
                    else
                        id = Integer.parseInt(reg.getField("CUENTA_MAYOR_ID"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            conexion.rollback();
        } finally {
            sentencia = null;
            vista = null;
        }
        return nivelMaximo;
    }*/

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getAccion() {
        return accion;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getMes() {
        return mes;
    }

    public void setTipoApertura(String tipoApertura) {
        this.tipoApertura = tipoApertura;
    }

    public String getTipoApertura() {
        return tipoApertura;
    }

    public void setRegresaCadena(StringBuffer regresaCadena) {
        this.regresaCadena = regresaCadena;
    }

    public StringBuffer getRegresaCadena() {
        return regresaCadena;
    }

    public void setEjercicio(String ejercicio) {
        this.ejercicio = ejercicio;
    }

    public String getEjercicio() {
        return ejercicio;
    }


    public void setNumEmpleado(int numEmpleado) {
        this.numEmpleado = numEmpleado;
    }

    public int getNumEmpleado() {
        return numEmpleado;
    }
}
