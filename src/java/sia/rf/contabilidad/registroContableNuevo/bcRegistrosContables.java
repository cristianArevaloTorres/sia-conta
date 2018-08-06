package registroContableNuevo;
import java.sql.*;
import java.lang.*;
import java.util.Vector;
import sun.jdbc.rowset.*;
import javax.servlet.http.*;


/**
 * <p>Nombre:bcRegistrosContables </p>
 * <p>Description: </p>
 * <p>Fecha de Creaci�n: 30/Ago/2006</p>
 * <p>Ultima modificaci�n: </p>
 * <p>Fecha de �ltima modificaci�n:</p>
 * <p>Autor de �ltima modificaci�n:</p>
 * @author Martha Luna
 */

public class bcRegistrosContables {
// Variables de rtcCuentas
  private String idCuenta;
  private String nivel1;
  private String nivel2;
  private String nivel3;
  private String nivel4;
  private String nivel5;
  private String nivel6;
  private String nivel7;
  private String descrip;
  private String aplica;
  private String stCuenta;
  private String nivelMax;
// Variables de rtiPoliza
  private String tipoModulo;
  private String anio;
  private String mes;
  private String tipoPoliza;
  private String numero;
  private String fechaCrea;
  private String concepto;
  private String refGral;
  private String cveAutoriza;
  private String cveValida;
  private String cveElabora;
  private String stPoliza;
  private String capturo;
// Variables rtiPolizaDetalle
  private String tipoModD;
  private String anioD;
  private String mesD;
  private String tipoPolizaD;
  private String numeroD;
  private String idCuentaD;
  private String importeD;
  private String tipImporteD;
  private String secuenciaD;
//Variables rtcPlantilla y rtrPlantCuentas
  private String idPlantilla;
  private String idCuentaP;
  private String descPlant;
  private String fechaCreaP;
  private String secuenciaP;
  private String tipImporteP;

      public String getIdCuenta() {
              return idCuenta;
      }

      public void setIdCuenta(String idCta) {
              idCuenta = idCta;
      }

      public String getNivel1() {
              return nivel1;
      }

      public void setNivel1(String niv1 ) {
              nivel1 = niv1;
      }

      public String getNivel2() {
              return nivel2;
      }

      public void setNivel2(String niv2 ) {
              nivel1 = niv2;
      }

      public String getNivel3() {
              return nivel3;
      }

      public void setNivel3(String niv3 ) {
              nivel1 = niv3;
      }
      public String getNivel4() {
              return nivel4;
      }

      public void setNivel4(String niv4 ) {
              nivel1 = niv4;
      }
      public String getNivel5() {
              return nivel5;
      }

      public void setNivel5(String niv5 ) {
              nivel1 = niv5;
      }
      public String getNivel6() {
              return nivel6;
      }

      public void setNivel6(String niv6 ) {
              nivel1 = niv6;
      }
      public String getNivel7() {
              return nivel7;
      }

      public void setNivel7(String niv7 ) {
              nivel1 = niv7;
      }
      public String getDescrip() {
              return descrip;
      }

      public void setDescrip(String descr ) {
              descrip = descr;
      }
      public String getAplica() {
              return aplica;
      }

      public void setAplica(String apl ) {
              aplica = apl;
      }
      public String getStCuenta() {
              return stCuenta;
      }

      public void setStCuenta(String stcta ) {
              stCuenta = stcta;
      }
      public String getNivelMax() {
              return nivelMax;
      }

      public void setNivelMax(String nmax ) {
              nivelMax = nmax;
      }
    // Variables de rtiPoliza
    public String getTipoModulo() {
            return tipoModulo;
    }
    public void setTipoModulo(String p ) {
            tipoModulo = p;
    }
    public String getAnio() {
            return anio;
    }
    public void setAnio(String p ) {
            anio = p;
    }
    public String getMes() {
            return mes;
    }
    public void setMes(String p ) {
            mes = p;
    }
    public String getTipoPoliza() {
            return tipoPoliza;
    }
    public void setTipoPoliza(String p ) {
            tipoPoliza = p;
    }
    public String getNumero() {
           return numero;
    }
    public void setNumero(String p ) {
            numero = p;
    }
    public String getFechaCrea() {
           return fechaCrea;
    }
    public void setFechaCrea(String p ) {
            fechaCrea = p;
    }
    public String getConcepto() {
           return concepto;
    }
    public void setConcepto(String p ) {
            concepto = p;
    }
    public String getRefGral() {
           return refGral;
    }
    public void setRefGral(String p ) {
            refGral = p;
    }
    public String getCveAutoriza() {
           return cveAutoriza;
    }
    public void setCveAutoriza(String p ) {
            cveAutoriza = p;
    }
    public String getCveValida() {
           return cveValida;
    }
    public void setCveValida(String p ) {
            cveValida = p;
    }
    public String getCveElabora() {
           return cveElabora;
    }
    public void setCveElabora(String p ) {
            cveElabora = p;
    }
    public String getStPoliza() {
           return stPoliza;
    }
    public void setStPoliza(String p ) {
            stPoliza = p;
    }
    public String getCapturo() {
           return capturo;
    }
    public void setCapturo(String p ) {
            capturo = p;
    }
// Variables rtiPolizaDetalle
    public String getTipoModD() {
           return tipoModD;
    }
    public void setTipoModD(String p ) {
            tipoModD = p;
    }
    public String getAnioD() {
           return anioD;
    }
    public void setAnioD(String p ) {
            anioD = p;
    }
    public String getMesD() {
           return mesD;
    }
    public void setMesD(String p ) {
            mesD = p;
    }
    public String getTipoPolizaD() {
           return tipoPolizaD;
    }
    public void setTipoPolizaD(String p ) {
            tipoPolizaD = p;
    }
    public String getNumeroD() {
           return numeroD;
    }
    public void setNumeroD(String p ) {
            numeroD = p;
    }
    public String getIdCuentaD() {
           return idCuentaD;
    }
    public void setIdCuentaD(String p ) {
            idCuentaD = p;
    }
    public String getImporteD() {
           return importeD;
    }
    public void setImporteD(String p ) {
            importeD = p;
    }
    public String getTipImporteD() {
           return tipImporteD;
    }
    public void setTipImporteD(String p ) {
            tipImporteD = p;
    }
    public String getSecuenciaD() {
           return secuenciaD;
    }
    public void setSecuenciaD(String p ) {
            secuenciaD = p;
    }
//Variables rtcPlantilla y rtrPlantCuentas
    public String getIdPlantilla() {
           return idPlantilla;
    }
    public void setIdPlantilla(String p ) {
            idPlantilla = p;
    }
    public String getIdCuentaP() {
           return idCuentaP;
    }
    public void setIdCuentaP(String p ) {
            idCuentaP = p;
    }
    public String getDescPlant() {
           return descPlant;
    }
    public void setDescPlant(String p ) {
            descPlant = p;
    }
    public String getFechaCreaP() {
           return fechaCreaP;
    }
    public void setFechaCreaP(String p ) {
            fechaCreaP = p;
    }
    public String getSecuenciaP() {
           return secuenciaP;
    }
    public void setSecuenciaP(String p ) {
            secuenciaP = p;
    }
    public String getTipImporteP() {
           return tipImporteP;
    }
    public void setTipImporteP(String p ) {
            tipImporteP = p;
    }
      public ResultSet cargaSubcuenta(Connection con, int nivRep, String niv1, String niv2, String niv3, String niv4, String niv5, String niv6, String niv7) throws SQLException, Exception  {
        Statement stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rsQuery = null;
          String x="";
          StringBuffer SQL = new StringBuffer("select nivel"+nivRep+" cuenta,descrip from rtcCuentas where nivel1='"+niv1);
          if (nivRep == 2){
              x ="' and nivel2 is not null and nivel3 is null";
          } else if (nivRep ==3){
              x ="' and nivel2='"+niv2+"' and nivel3 is not null and nivel4 is null";
          } else if (nivRep ==4){
              x ="' and nivel2='"+niv2+"' and nivel3='"+niv3+"' and nivel4 is not null and nivel5 is null";
          } else if (nivRep ==5){
              x ="' and nivel2='"+niv2+"' and nivel3='"+niv3+"' and nivel4='"+niv4+"' and nivel5 is not null and nivel6 is null";
          } else if (nivRep ==6){
            x ="' and nivel2='"+niv2+"' and nivel3='"+niv3+"' and nivel4='"+niv4+"' and nivel5="+niv5+" and nivel6 is not null and nivel7 is null";
          } else if (nivRep ==7){
            x = "' and nivel2='" + niv2 + "' and nivel3='" + niv3 + "' and nivel4='" +
                niv4 + "' and nivel5='" + niv5 + "' and nivel6='" + niv6 +
                "' and nivel7 is not null";
          }
          SQL.append(x);
          rsQuery = stQuery.executeQuery(SQL.toString());
          System.out.println("va "+SQL.toString());
          return rsQuery;
      }

    public ResultSet cargaSubcuenta2(Connection con, int nivRep, String niv1, String niv2, String niv3, String niv4, String niv5, String niv6, String niv7) throws SQLException, Exception  {
      Statement stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      ResultSet rsQuery = null;
        String x="";
        StringBuffer SQL = new StringBuffer("");
        
        // StringBuffer SQL = new StringBuffer("select nivel"+nivRep+" cuenta,descrip from rtcCuentas where nivel1='"+niv1);
        if (nivRep == 1){
          x ="select substr(cuenta_contable,1,5) cuenta, descripcion from RF_TR_CUENTAS_CONTABLES r where nivel = "+ nivRep+" order by cuenta";        
        }
        else if (nivRep == 2){
            x ="select substr(cuenta_contable,6,4) cuenta, descripcion from RF_TR_CUENTAS_CONTABLES r where substr(cuenta_contable,1,5) = "+niv1+" and nivel = "+ nivRep+" order by cuenta";        
        } else if (nivRep ==3){
            x ="select substr(cuenta_contable,10,4) cuenta, descripcion from RF_TR_CUENTAS_CONTABLES r where substr(cuenta_contable,1,9) = "+niv1+niv2+" and nivel = "+ nivRep+" order by cuenta";                
        } else if (nivRep ==4){
            x ="select substr(cuenta_contable,14,4) cuenta, descripcion from RF_TR_CUENTAS_CONTABLES r where substr(cuenta_contable,1,13) = "+niv1+niv2+niv3+" and nivel = "+ nivRep+" order by cuenta";                
        } else if (nivRep ==5){
            x ="select substr(cuenta_contable,18,4) cuenta, descripcion from RF_TR_CUENTAS_CONTABLES r where substr(cuenta_contable,1,17) = "+niv1+niv2+niv3+niv4+" and nivel = "+ nivRep+" order by cuenta";                
        } else if (nivRep ==6){
            x ="select substr(cuenta_contable,22,4) cuenta, descripcion from RF_TR_CUENTAS_CONTABLES r where substr(cuenta_contable,1,21) = "+niv1+niv2+niv3+niv4+niv5+" and nivel = "+ nivRep+" order by cuenta";                    
        } else if (nivRep ==7){
            x ="select substr(cuenta_contable,26,4) cuenta, descripcion from RF_TR_CUENTAS_CONTABLES r where substr(cuenta_contable,1,25) = "+niv1+niv2+niv3+niv4+niv5+niv6+" and nivel = "+ nivRep+" order by cuenta";                        
        }
        SQL.append(x);
        System.out.println("va "+SQL.toString());
        rsQuery = stQuery.executeQuery(SQL.toString());
        return rsQuery;
    }



      public void obtenCuenta(Connection con, int nivRep, String niv1, String niv2, String niv3, String niv4, String niv5, String niv6, String niv7) throws SQLException, Exception{
/**      CachedRowSet crs = null;
 //       try {
          crs = new CachedRowSet();
          String x="";
          StringBuffer SQL = new StringBuffer("select idCuenta from rtcCuentas where nivel1='"+niv1);
          if (nivRep == 2){
              x ="' and nivel2 ='"+niv2+"' and nivel3 is null";
          } else if (nivRep ==3){
              x ="' and nivel2='"+niv2+"' and nivel3 ='"+niv3+"' and nivel4 is null";
          } else if (nivRep ==4){
              x ="' and nivel2='"+niv2+"' and nivel3='"+niv3+"' and nivel4 ='"+niv4+"' and nivel5 is null";
          } else if (nivRep ==5){
              x ="' and nivel2='"+niv2+"' and nivel3='"+niv3+"' and nivel4='"+niv4+"' and nivel5 ='"+niv5+"' and nivel6 is null";
          } else if (nivRep ==6){
            x ="' and nivel2='"+niv2+"' and nivel3='"+niv3+"' and nivel4='"+niv4+"' and nivel5="+niv5+" and nivel6 ='"+niv6+"' and nivel7 is null";
          } else if (nivRep ==7){
            x = "' and nivel2='" + niv2 + "' and nivel3='" + niv3 + "' and nivel4='" +
                niv4 + "' and nivel5='" + niv5 + "' and nivel6='" + niv6 +
                "' and nivel7 ='"+niv7+"'";
          }
          SQL.append(x);
          System.out.println("Query obtenCuenta : "+ SQL.toString());
          crs.setCommand(SQL.toString());
          crs.execute(sbAutentifica.getConexion());
          crs.beforeFirst();
          while (crs.next()){
            idCuentaD = crs.getString("idCuenta");
          }
          crs.close();
          crs = null; **/
/**        }
        catch (SQLException ex) {
          try{
            crs.close();
            crs = null;
          }
          catch (SQLException ex1) {
            crs = null;
          }
        } **/
          Statement stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
          ResultSet rsQuery = null;
          String x="";
          StringBuffer SQL = new StringBuffer("select idCuenta from rtcCuentas where nivel1='"+niv1);
          if (nivRep == 2){
              x ="' and nivel2 ='"+niv2+"' and nivel3 is null";
          } else if (nivRep ==3){
              x ="' and nivel2='"+niv2+"' and nivel3 ='"+niv3+"' and nivel4 is null";
          } else if (nivRep ==4){
              x ="' and nivel2='"+niv2+"' and nivel3='"+niv3+"' and nivel4 ='"+niv4+"' and nivel5 is null";
          } else if (nivRep ==5){
              x ="' and nivel2='"+niv2+"' and nivel3='"+niv3+"' and nivel4='"+niv4+"' and nivel5 ='"+niv5+"' and nivel6 is null";
          } else if (nivRep ==6){
              x ="' and nivel2='"+niv2+"' and nivel3='"+niv3+"' and nivel4='"+niv4+"' and nivel5="+niv5+" and nivel6 ='"+niv6+"' and nivel7 is null";
          } else if (nivRep ==7){
              x = "' and nivel2='" + niv2 + "' and nivel3='" + niv3 + "' and nivel4='" +
                niv4 + "' and nivel5='" + niv5 + "' and nivel6='" + niv6 +
                "' and nivel7 ='"+niv7+"'";
          }
          SQL.append(x);
 //         System.out.println("Query obtenCuenta : "+ SQL.toString());
          rsQuery = stQuery.executeQuery(SQL.toString());
          rsQuery.beforeFirst();
          while (rsQuery.next()){
                 idCuentaD = rsQuery.getString("idCuenta");
          }
          rsQuery.close();
          rsQuery = null;
          stQuery.close();
          stQuery=null;
      }
      public int insertPoliza(Connection con) throws SQLException, Exception  {
        Statement stQuery0 = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rsQuery = null;
          StringBuffer SQL0 = new StringBuffer(
              "select nvl(max(to_number(numero)),0) numMax from rtiPoliza where tipoModulo ='" +
              tipoModulo +
              "' and  anio='" + anio + "' and mes = '" + mes +
              "' and tipoPoliza = '" + tipoPoliza + "'");
//          System.out.println("Query insertPoliza,busca max numero : " + SQL0.toString());
          rsQuery = stQuery0.executeQuery(SQL0.toString());
          rsQuery.beforeFirst();
//          System.out.println("maximo num poliza1 : " + numero);
          while (rsQuery.next()) {
//            System.out.println("maximo num poliza2 : " + rsQuery.getInt("numMax"));
            numero = Integer.toString(rsQuery.getInt("numMax") + 1);
          }
//          System.out.println("maximo num poliza : " + numero);
          rsQuery.close();
          rsQuery = null;
          stQuery0.close();
          stQuery0 = null;
          Statement stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
          StringBuffer SQL = new StringBuffer("insert into rtiPoliza (tipoModulo, anio,mes,tipoPoliza,numero,fechaCrea,concepto,refGral,cveAutoriza,cveValida,cveElabora,stPoliza,capturo) ");
          SQL.append("values ('");
          SQL.append(tipoModulo);
          SQL.append("','");
          SQL.append(anio);
          SQL.append("','");
          SQL.append(mes);
          SQL.append("','");
          SQL.append(tipoPoliza);
          SQL.append("',");
          SQL.append(numero);
          SQL.append(",to_date('");
          SQL.append(fechaCrea);
          SQL.append("','dd/mm/yyyy'),");
          SQL.append( (concepto == null) ? "null" : "'" + concepto + "'");
          SQL.append(",");
          SQL.append( (refGral == null) ? "null" : "'" + refGral + "'");
          SQL.append(",");
          SQL.append( (cveAutoriza == null) ? "null" : "'" + cveAutoriza + "'");
          SQL.append(",");
          SQL.append( (cveValida == null) ? "null" : "'" + cveValida + "'");
          SQL.append(",");
          SQL.append( (cveElabora == null) ? "null" : "'" + cveElabora + "'");
          SQL.append(",");
          SQL.append( (stPoliza == null) ? "null" : "'" + stPoliza + "'");
          SQL.append(",");
          SQL.append( (capturo == null) ? "null" : "'" + capturo + "'");
          SQL.append(")");
          System.out.println("Query insertPoliza: " + SQL.toString());
          int rs = -1;
    //        rs = sbAutentifica.executeUpdate("RTIPOLIZA", SQL.toString(),stQuery);
          rs = stQuery.executeUpdate(SQL.toString());
          stQuery.close();
          stQuery = null;
          return rs;

      }
      public int insertPolizaDetalle(Connection con) throws SQLException, Exception  {
        Statement stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
//        try {
        StringBuffer SQL = new StringBuffer("insert into rtiPolizaDetalle (tipoModulo, anio,mes,tipoPoliza,numero,idCuenta,importe,tipImporte,secuencia) ");
        SQL.append("values ('");
        SQL.append(tipoModulo);
        SQL.append("','");
        SQL.append(anio);
        SQL.append("','");
        SQL.append(mes);
        SQL.append("','");
        SQL.append(tipoPoliza);
        SQL.append("',");
        SQL.append(numero);
        SQL.append(",");
        SQL.append(idCuentaD);
        SQL.append(",");
        SQL.append(importeD);
        SQL.append(",'");
        SQL.append(tipImporteD);
        SQL.append("',");
        SQL.append(secuenciaD);
        SQL.append(")");
        System.out.println("Query insertPolizaDetalle: " + SQL.toString());
        int rs = -1;
//        rs = sbAutentifica.executeUpdate("RTIPOLIZADETALLE", SQL.toString(),stQuery);
          rs = stQuery.executeUpdate(SQL.toString());
          stQuery.close();
          stQuery = null;
          return rs;
//        } catch (SQLException ex1) {
//             return -1;
//        }finally{
//          stQuery.close();
//          stQuery = null;
//        }

      }
      public int insertPlantilla(Connection con) throws SQLException, Exception {
        Statement stQuery0 = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rsQuery = null;
        String SQL0 = "select seqrtiplantilla.nextval idPlantilla from dual";
        rsQuery = stQuery0.executeQuery(SQL0.toString());
        rsQuery.beforeFirst();
        while (rsQuery.next()) {
            idPlantilla = rsQuery.getString("idPlantilla");
        }
        rsQuery.close();
        rsQuery = null;
        stQuery0.close();
        stQuery0 = null;

        Statement stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        StringBuffer SQL = new StringBuffer("insert into rtcPlantilla (idPlantilla, descPlant, fechaCrea) ");
        SQL.append("values (");
        SQL.append(idPlantilla);
        SQL.append(",'");
        SQL.append(descPlant);
        SQL.append("',sysdate)");
        System.out.println("Query insertPlantilla: " + SQL.toString());
        int rs = -1;
//        rs = sbAutentifica.executeUpdate("RTIPLANTILLA", SQL.toString(),stQuery);
        rs = stQuery.executeUpdate(SQL.toString());
        stQuery.close();
        stQuery = null;
        return rs;
      }
      public int insertPlantCuentas(Connection con) throws SQLException, Exception  {
        Statement stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        StringBuffer SQL = new StringBuffer("insert into rtrPlantCuentas (idPlantilla, idCuenta, secuencia, tipImporte) ");
        SQL.append("values (");
        SQL.append(idPlantilla);
        SQL.append(",");
        SQL.append(idCuentaP);
        SQL.append(",");
        SQL.append(secuenciaP);
        SQL.append(",'");
        SQL.append(tipImporteP);
        SQL.append("')");
        System.out.println("Query insertPlantCuentas: " + SQL.toString());
        int rs = -1;
//        rs = sbAutentifica.executeUpdate("RTRPLANTCUENTAS", SQL.toString(),stQuery);
        rs = stQuery.executeUpdate(SQL.toString());
        stQuery.close();
        stQuery = null;
        return rs;
      }
      public ResultSet selectPlantCuentas(Connection con, String pplant) throws SQLException, Exception  {
        Statement stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rsQuery = null;
        try {
          String x="";
          StringBuffer SQL = new StringBuffer("select secuencia,nvl(c.nivel1,'') nivel1, nvl(c.nivel2,'') nivel2, ");
          SQL.append(" nvl(c.nivel3,'') nivel3, nvl(c.nivel4,'') nivel4, nvl(c.nivel5,'') nivel5, nvl(c.nivel6,'') nivel6, nvl(c.nivel7,'') nivel7,c.descrip,t.tipimporte,");
          SQL.append("decode(nivel1,null,0,decode(nivel2,null,1,decode(nivel3,null,2,decode(nivel4,null,3,");
          SQL.append("decode(nivel5,null,4,decode(nivel6,null,5,decode(nivel7,null,6,7))))))) maxniv");
          SQL.append(" from rtrplantcuentas t, rtccuentas c");
          SQL.append(" where t.idcuenta = c.idcuenta and t.idplantilla =");
          SQL.append(pplant);
          SQL.append(" order by secuencia");
          System.out.println("Query selectPlantCuetas : "+ SQL.toString());
          rsQuery = stQuery.executeQuery(SQL.toString());
          return rsQuery;
        }
        catch (SQLException ex) {
          try{
            rsQuery.close();
            rsQuery = null;
            return null;
          }
          catch (SQLException ex1) {
            rsQuery = null;
            return null;
          }
        }
      }
      public void selectPoliza(Connection con) throws SQLException, Exception {
        Statement stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rsQuery = null;
          String x="";
          StringBuffer SQL = new StringBuffer("select to_char(fechaCrea,'dd/mm/yyyy') fechaCrea, concepto,refGral,cveAutoriza,cveValida,cveElabora,stPoliza,capturo");
          SQL.append(" from rtiPoliza where tipoModulo ='");
          SQL.append(tipoModulo);
          SQL.append("' and anio = '");
          SQL.append(anio);
          SQL.append("' and mes = '");
          SQL.append(mes);
          SQL.append("' and tipoPoliza = '");
          SQL.append(tipoPoliza);
          SQL.append("' and numero ='");
          SQL.append(numero);
          SQL.append("'");
          System.out.println("Query selectPoliza : "+ SQL.toString());
          rsQuery = stQuery.executeQuery(SQL.toString());
          rsQuery.beforeFirst();
          while ( rsQuery.next()){
                fechaCrea = rsQuery.getString("fechaCrea");
                concepto = (rsQuery.getString("concepto")==null)?"":rsQuery.getString("concepto");
                refGral = (rsQuery.getString("refGral")==null)?"":rsQuery.getString("refGral");
                cveAutoriza = (rsQuery.getString("cveAutoriza")==null)?"":rsQuery.getString("cveAutoriza");
                cveValida = (rsQuery.getString("cveValida")==null)?"":rsQuery.getString("cveValida");
                cveElabora = (rsQuery.getString("cveElabora")==null)?"":rsQuery.getString("cveElabora");
                capturo = (rsQuery.getString("capturo")==null)?"":rsQuery.getString("capturo");
          }
          rsQuery.close();
          rsQuery = null;
          stQuery.close();
          stQuery = null;
      }
      public ResultSet selectPolizaDetalle(Connection con) throws SQLException, Exception  {
          Statement stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
          ResultSet rsQuery = null;
          String x="";
          StringBuffer SQL = new StringBuffer("select t.idCuenta, secuencia,nvl(c.nivel1,'') nivel1, nvl(c.nivel2,'') nivel2, ");
          SQL.append(" nvl(c.nivel3,'') nivel3, nvl(c.nivel4,'') nivel4, nvl(c.nivel5,'') nivel5, nvl(c.nivel6,'') nivel6, nvl(c.nivel7,'') nivel7,c.descrip,t.tipimporte,t.importe,");
          SQL.append("decode(nivel1,null,0,decode(nivel2,null,1,decode(nivel3,null,2,decode(nivel4,null,3,");
          SQL.append("decode(nivel5,null,4,decode(nivel6,null,5,decode(nivel7,null,6,7))))))) maxniv ");
          SQL.append(" from rtiPolizaDetalle t, rtccuentas c");
          SQL.append(" where t.idcuenta = c.idcuenta and tipoModulo ='");
          SQL.append(tipoModulo);
          SQL.append("' and anio = '");
          SQL.append(anio);
          SQL.append("' and mes = '");
          SQL.append(mes);
          SQL.append("' and tipoPoliza = '");
          SQL.append(tipoPoliza);
          SQL.append("' and numero ='");
          SQL.append(numero);
          SQL.append("' order by secuencia");
          System.out.println("Query selectPolizaDetalle : "+ SQL.toString());
          rsQuery = stQuery.executeQuery(SQL.toString());
          return rsQuery;
    }

}