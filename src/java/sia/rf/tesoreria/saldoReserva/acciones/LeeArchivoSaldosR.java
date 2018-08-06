package sia.rf.tesoreria.saldoReserva.acciones;

import java.io.BufferedReader;
import java.io.FileReader;

import java.sql.Connection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sia.db.dao.DaoFactory;
import sia.db.sql.SentennciasSE;
import sia.db.sql.Vista;

import sia.libs.formato.Cadena;


public class LeeArchivoSaldosR {

    private final String RUTA_DEFAULT="tesoreria/descargas/saldosReserva";
    private String ruta;
    private String archivoleer;
    
    public LeeArchivoSaldosR(String rutaArchivo, String nombreArchivo) {
        setRuta(rutaArchivo);
        setArchivoleer(nombreArchivo);
    }


  

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getRuta() {
        return ruta;
    }

    public void setArchivoleer(String archivoleer) {
        this.archivoleer = archivoleer;
    }

    public String getArchivoleer() {
        return archivoleer;
    }
    
    private String getIdCuenta(String ctaArchivo, String idPrograma){
      String regresa = null;
      List<Vista> registro = null;
      Map parametros = new HashMap();
      SentennciasSE sentenciasSE = new SentennciasSE(DaoFactory.CONEXION_TESORERIA);
    
      if (idPrograma.equals("7")){
        String[] sucCta = null;
        sucCta = ctaArchivo.split("/");
        parametros.put("numCuenta",Cadena.rellenar(sucCta[0],4,'0',true).concat(Cadena.rellenar(sucCta[1],7,'0',true)));}
      else 
        parametros.put("numCuenta",ctaArchivo);
      parametros.put("idPrograma",idPrograma);
      try  {
        registro = sentenciasSE.registros("criterios.select.obtieneIdCuenta.saldoReserva",parametros);
        if (registro != null)
          regresa = registro.get(0).getField("ID_CUENTA");
        else 
          regresa = null;
      } catch (Exception e)  {
            e.printStackTrace();
            regresa = null; 
      } finally  {
            sentenciasSE = null;
      }
      return regresa;
    }
    
    private String quitaCaracter(String cadena, String caracter){
      int longitud= cadena.length();
      while(cadena.indexOf(caracter)!=-1) {
        int posicion= cadena.indexOf(caracter);
        cadena= cadena.substring(0, posicion).concat(cadena.substring(posicion+1, longitud--));
      }
      return cadena;
    }
    
    private String existeSaldo(String fechaArchivo, String idCuenta){
        String regresa = null;
        Map parametro = new HashMap(); 
        List <Vista> registros= null;
        SentennciasSE sentenciasSE = new SentennciasSE(DaoFactory.CONEXION_TESORERIA);
        parametro.put("fechaCarga",fechaArchivo);
        parametro.put("idCuenta",idCuenta);
        try  {
          registros = sentenciasSE.registros("criterios.select.existeSaldoReserva.saldoReserva",parametro);
          if (registros != null)
            regresa = registros.get(0).getField("ID_CUENTA");
          else 
            regresa = null;
        } catch (Exception e)  {
              e.printStackTrace();
              regresa = null; 
        } finally  {
              sentenciasSE = null;
        }
        return regresa;
    }
    
    private StringBuffer armanValores(String[] valores, int idCuenta, String fecha, String idPrograma){
        StringBuffer regresa = new StringBuffer();
        String[] sucCta = null;
        String saldo = "0.00";
        regresa.append(idCuenta);
        if (idPrograma.equals("7"))
            regresa.append(",null");
        else if (idPrograma.equals("6"))
                regresa.append(",'HBMX'");
            else if (idPrograma.equals("8"))
                    regresa.append(",'BBVA'");
                else if (idPrograma.equals("12"))
                        regresa.append(",'MULTIVA'");
                    else 
                        regresa.append(",'BAJIO'");
        if (idPrograma.equals("7") || idPrograma.equals("8"))
          regresa.append(",null");
        else 
          regresa.append(",'PRO'");
        if (idPrograma.equals("7")){
                sucCta = valores[0].split("/");
                regresa.append(",'".concat(Cadena.rellenar(sucCta[0],4,'0',true)).concat("'"));
            }
        else 
          regresa.append(",null");
        regresa.append(",'$'");  
        if (idPrograma.equals("7")){
            if (valores[1].contains(","))
              saldo = quitaCaracter(valores[1],",");
            else
                if (valores[1].trim().equals("-"))
                  saldo = "0.00";
                else
                  saldo = valores[1];
        }
        else if (idPrograma.equals("6") || idPrograma.equals("8")){
                 if (valores[4].contains(","))
                   saldo = quitaCaracter(valores[4],",");
                 else
                   saldo = valores[4];
             } 
             else if (idPrograma.equals("12")){
                    saldo = valores[3];
                    ///saldo = valores[3].substring(0, valores[3].length()-2).concat(".").concat(valores[3].substring(valores[3].length()-2, valores[3].length()));
                 }
                 else {
                     if (valores[7].contains(","))
                       saldo = quitaCaracter(valores[7],",");
                     else
                       saldo = valores[7];
                 }
      regresa.append(",".concat(saldo.trim()).concat(","));
      regresa.append("to_date('".concat(fecha).concat("','dd/mm/yyyy')"));
      return regresa;
    }
    
    public boolean leeDivideArchivo(Connection conn, String fechaRegistro, String idPrograma){
        boolean regresar = true;
        String idCuentaBD = null;
        StringBuffer ATRIBUTOS = new StringBuffer("ID_CUENTA, BANCO, TIPO_PERTENENCIA, NUM_SUCURSAL, MONEDA, SALDO_CIERRE, FECHA");
        String token = null;
        String saldo = "0.00";
        String fechaArchivo = null;
        int registro = 0;
        boolean actualiza = false;
        boolean inserta = false;
        String[] campos = null;
        StringBuffer sbValores = new StringBuffer();
        GeneraSentenciaSR generaSentenciaSR = new GeneraSentenciaSR(ATRIBUTOS,"RF_TR_SALDOS_RESERVA");
        FileReader fr = null;
        try {
          fr = new FileReader(getRuta() + getArchivoleer());
          BufferedReader linea = new BufferedReader(fr);
          while ((token=linea.readLine()) != null && regresar ) {
            if (!token.equals("")) {
                if (idPrograma.equals("7"))
                    campos = quitaCaracter(token,"\"").split("\t");
                else if (idPrograma.equals("6"))
                        campos = quitaCaracter(token,"*").split(",");
                     else if (idPrograma.equals("8") && registro>0)
                            campos = token.split("\t");
                          else if (idPrograma.equals("12"))
                                    campos = token.split("\t");
                               else 
                                    campos = quitaCaracter(token,"$").split("\t");
                if (!idPrograma.equals("8")){
                    if (idPrograma.equals("12"))
                       idCuentaBD = getIdCuenta(Cadena.rellenar(campos[0],11,'0',true),idPrograma);
                    else
                        idCuentaBD = getIdCuenta(campos[0],idPrograma);
                    if (idCuentaBD != null){
                        if(idPrograma.equals("7"))
                            fechaRegistro = campos[3].trim();
                        if (existeSaldo(fechaRegistro,idCuentaBD)!=null){
                            actualiza = true;
                            inserta = false;
                        }
                        else {
                            actualiza = false;
                            inserta = true;
                        }
                    }
                    else{
                        regresar = false;
                    }
                }
                if (idPrograma.equals("8") && registro>0){
                    idCuentaBD = "1051";
                    fechaArchivo = campos[0].replaceAll("-","/");
                    if (fechaArchivo.equals(fechaRegistro)){
                        if (existeSaldo(fechaRegistro,idCuentaBD)!=null){
                            actualiza = true;
                            inserta = false;
                        }
                        else {
                            actualiza = false;
                            inserta = true;
                        }
                    }
                }
                ++registro;
                if (actualiza){
                    if (idPrograma.equals("7")){
                        if (campos[1].contains(","))
                          saldo = quitaCaracter(campos[1],",");
                        else
                          if (campos[1].trim().equals("-"))
                            saldo = "0.00";
                          else
                            saldo = campos[1];
                    }
                    else if (idPrograma.equals("6") || idPrograma.equals("8")){
                             if (campos[4].contains(","))
                               saldo = quitaCaracter(campos[4],",");
                             else
                               saldo = campos[4];
                         }
                         else if (idPrograma.equals("12")){
                            saldo = campos[3];
                            ///saldo = campos[3].substring(0, campos[3].length()-2).concat(".").concat(campos[3].substring(campos[3].length()-2, campos[3].length()));
                         }
                             else {
                                 if (campos[7].contains(","))
                                   saldo = quitaCaracter(campos[7],",");
                                 else
                                   saldo = campos[7];
                             }
                    regresar = generaSentenciaSR.ejecutaSentenciaUpdate("id_cuenta =".concat(idCuentaBD).concat(" and trunc(fecha) = to_date('").concat(fechaRegistro).concat("','dd/mm/yyyy')"),conn,"RF_TR_SALDOS_RESERVA","saldo_cierre = ".concat(saldo.trim()));           
                }
                if (inserta){
                    sbValores = armanValores(campos,Integer.parseInt(idCuentaBD),fechaRegistro,idPrograma);
                    if (sbValores !=null)
                      regresar = generaSentenciaSR.ejecutaSentenciaInsert(sbValores,conn);
                }
            
            }  // if (!token.equals(""))
          }// while
          linea.close();
          linea = null;
          fr.close();
          fr = null;
        }
        catch (Exception e) {
            regresar = false;
            e.printStackTrace();
        }
        return regresar;
    }
    
    public boolean leeArchivoStdr(Connection conn){
        boolean regresa = true;
        String token = null;
        FileReader fr = null;
        String existeCuenta = null;
        String cuentaArch = null;
        String saldo = "0.00";
        String fechaRegistro = null;
        StringBuffer sbValores = new StringBuffer();
        StringBuffer ATRIBUTOS = new StringBuffer("ID_CUENTA, BANCO, TIPO_PERTENENCIA, NUM_SUCURSAL, MONEDA, SALDO_CIERRE, FECHA");
        GeneraSentenciaSR generaSentenciaSR = new GeneraSentenciaSR(ATRIBUTOS,"RF_TR_SALDOS_RESERVA");
        try {
            fr = new FileReader(getRuta() + getArchivoleer());
            BufferedReader linea = new BufferedReader(fr);
            while ((token=linea.readLine()) != null && regresa ) {
                if (!token.equals("")) {
                cuentaArch =  token.substring(0,16).trim();
                fechaRegistro = token.substring(56,58).concat("/").concat(token.substring(58,60)).concat("/").concat(token.substring(60,64));
                existeCuenta = getIdCuenta(cuentaArch,"10");
                if (existeCuenta!=null){
                    if (existeSaldo(fechaRegistro,existeCuenta)!=null){
                        saldo = token.substring(69,82).concat(".").concat(token.substring(82,84));
                        regresa = generaSentenciaSR.ejecutaSentenciaUpdate("id_cuenta =".concat(existeCuenta).concat(" and trunc(fecha) = to_date('").concat(fechaRegistro).concat("','dd/mm/yyyy')"),conn,"RF_TR_SALDOS_RESERVA","saldo_cierre = ".concat(saldo.trim()));           
                    }
                    else{
                        sbValores.append(existeCuenta);
                        sbValores.append(",'STDR'");
                        sbValores.append(",'PRO'");
                        sbValores.append(",null");
                        sbValores.append(",'$',");
                        sbValores.append(token.substring(69,82).concat(".").concat(token.substring(82,84)));
                        sbValores.append(",to_date('".concat(fechaRegistro).concat("','dd/mm/yyyy')"));
                        regresa = generaSentenciaSR.ejecutaSentenciaInsert(sbValores,conn);
                    }
                }
                else 
                    regresa = false;
                }  // if (!token.equals(""))
            }// while
        linea.close();
        linea = null;
        fr.close();
        fr = null;
      }
      catch (Exception e) {
        regresa = false;
        e.printStackTrace();
      } 
     return regresa;
    }
    
}
