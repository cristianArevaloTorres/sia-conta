package sia.rf.tesoreria.bancas.acciones;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import sia.db.dao.DaoFactory;
import sia.db.sql.SentennciasSE;
import sia.db.sql.Vista;
import sia.libs.formato.Fecha;


public class Criterios {

    public Criterios() {
    }
    
    public List<Vista> getCuentasSinSaldoInicialArrastra(Map parametro, String seccionXML){
      List<Vista> regresa;
      SentennciasSE sentenciasSE = new SentennciasSE(DaoFactory.CONEXION_TESORERIA);
      try  {
          regresa = sentenciasSE.registros(seccionXML,parametro);
          if (regresa!= null ){
              System.out.println(regresa);
              System.err.println(regresa);
          }
          else {
              System.err.println("Todas las cuentas tienen saldo inicial");
          }
      } catch (Exception e)  {
          e.printStackTrace();
          regresa = null;
        }
      return regresa;
    } 
    
    public void arrastaSaldosInicial(int iProg, String fecha, Connection conn, HttpServletRequest req){
     CallableStatement st = null;
     StringBuffer sbProceso = new StringBuffer();
     sbProceso.append("begin TSR.RELLENA_DIAS_SIN_SALDO(");
     sbProceso.append(iProg);
     sbProceso.append(", to_date('");
     sbProceso.append(fecha);
     sbProceso.append("','dd/mm/yyyy')); end;");
     try {
       st = conn.prepareCall(sbProceso.toString());
       st.execute();
     } catch (Exception ex)  { 
       ex.printStackTrace();
       
       req.setAttribute("mensaje","Error en proceso almacenado de arrastre de saldos");
     } 
    }
    
    private int getRegistrosInt(Map parametro, String seccionXML){
      int regresa;  
      List <Vista> reg;
      SentennciasSE sentenciasSE = new SentennciasSE(DaoFactory.CONEXION_TESORERIA);
      try  {
         reg = sentenciasSE.registros(seccionXML,parametro);
         if (reg !=null && reg.size() > 0) {
              regresa = reg.size();
          }
         else {
              regresa = 0;
          }
      } catch (Exception e)  {
          e.printStackTrace();
          regresa = 0;
        }
      return regresa;
    }
    
    public int registrosDuplicados(){
      return getRegistrosInt(null,"criterios.select.registrosDuplicados.bancas");
    }
    
    public StringBuffer obtieneSentencia(Map parametro, String seccionXML){
        StringBuffer regresa = new StringBuffer();
        String sentencia;
        SentennciasSE sentenciasSE = new SentennciasSE(DaoFactory.CONEXION_TESORERIA);
        try  {
            sentencia = sentenciasSE.getComando(seccionXML,parametro);
            if (sentencia!= null ){
                regresa = new StringBuffer(sentencia);
            }
            else {
                regresa = null;
            }
        } catch (Exception e)  {
            e.printStackTrace();
            regresa = null; 
          }
       return regresa; 
    }
    
    private String[] getSaldosBanco (int idCuenta, String fechaImportada, String idProgS) throws SQLException {
      String[] regresa = new String[2];
      List <Vista> registros;
      Map parametro = new HashMap(); 
      SentennciasSE sentenciasSE;
      String nomSent;
      try  {
          sentenciasSE = new SentennciasSE(DaoFactory.CONEXION_TESORERIA);
          parametro.put("fecha",fechaImportada);
          parametro.put("idCuenta",idCuenta);
          if (idProgS.equals("10")) {
              nomSent = "criterios.select.saldoInicialFinal.bancas";
          }
          else if (idProgS.equals("7")) {
              nomSent = "criterios.select.saldoInicialFinalBMX.bancas";
          } 
               else {
              nomSent = "criterios.select.saldoInicialFinal.bancas";
          }
          registros = sentenciasSE.registros(nomSent,parametro);
          //registros = sentenciasSE.registros("criterios.select.saldoInicialFinal.bancas",parametro);
          if (registros !=null) {
            regresa[0] = registros.get(0).getField("SALDOINICIAL");
            regresa[1] = registros.get(registros.size()-1).getField("SALDO");
          }    
      } catch (Exception e)  {
            e.printStackTrace();
            regresa = null; 
          }
      return regresa;
    }
    
    private String getSecuenciaSaldosDiarios(){
      String regresa = null;
      List <Vista> registros;
      SentennciasSE sentenciasSE;
      Map parametro = new HashMap(); 
      try  {
          sentenciasSE = new SentennciasSE(DaoFactory.CONEXION_TESORERIA);
          registros = sentenciasSE.registros("criterios.select.seqSaldoDiario.bancas",parametro);
          if (registros !=null) {
              regresa = registros.get(0).getField("seq");
          }
      } catch (Exception e)  {
            e.printStackTrace();
            regresa = null; 
          }
      return regresa;
    }
    
    public boolean insertaDatos(StringBuffer listaCampos, StringBuffer listaValores, Connection conn, String tabla){
      boolean regresa; 
      int ejecuto;
      SentennciasSE sentenciasSE;
      try  {
        sentenciasSE = new SentennciasSE(DaoFactory.CONEXION_TESORERIA);
        ejecuto = sentenciasSE.ejecutar(conn,"insert into ".concat(tabla).concat(" (").concat(listaCampos.toString()).concat(") values (").concat(listaValores.toString()).concat(")"));
        if (ejecuto!=-1) {
              regresa = true;
          }
        else {
              regresa =false;
          }
      } catch (Exception e)  {
            e.printStackTrace();
            regresa = false;
          }
      return regresa; 
    }
    
    private boolean existeDiferencia(int idCuenta, String fechaImportada){
      boolean regresa = false;
      List <Vista> registros;
      Map parametro = new HashMap(); 
      SentennciasSE sentenciasSE;
      try  {
        sentenciasSE = new SentennciasSE(DaoFactory.CONEXION_TESORERIA);  
        parametro.put("idCuenta",idCuenta);
        parametro.put("fecha",fechaImportada);
        registros = sentenciasSE.registros("criterios.select.diferenciaSaldo.bancas",parametro);
        if(registros!=null){
            if (registros.size() > 0) {
                regresa = true;
            }
            else {
                regresa = false;
            }
        }
      } catch (Exception e)  {
            e.printStackTrace();
            regresa = false; 
          }
      return regresa;
    }
    
    private boolean registraDiferenciaSaldo(int idCuenta, String seqSaldosDiarios, String fechaInicio, BigDecimal montoMov, BigDecimal saldoBanco, Connection conn){
      boolean regresa;
      StringBuffer listaValores = new StringBuffer();
      StringBuffer listaCampos = new StringBuffer();
      try  {
        listaValores.append(idCuenta);
        listaValores.append(", ");
        listaCampos.append("id_cuenta, ");
        listaValores.append(seqSaldosDiarios);
        listaValores.append(", ");
        listaCampos.append("id_saldo_diario, ");
        listaValores.append("to_date('".concat(fechaInicio).concat("','dd/mm/yyyy')").concat(", "));
        listaCampos.append("fecha_inicio, ");
        listaValores.append(new BigDecimal(2));
        listaValores.append(", ");
        listaCampos.append("id_estatus_dif, ");
        listaValores.append(saldoBanco.subtract(montoMov));
        listaValores.append(", ");
        listaCampos.append("diferencia_real, ");
        listaValores.append(montoMov);
        listaValores.append(", ");
        listaCampos.append("operaciones_calculado, ");
        listaValores.append(saldoBanco);
        listaCampos.append("operaciones_banco");
        regresa = insertaDatos(listaCampos,listaValores, conn, "RF_TR_DIFERENCIAS_CUENTAS");
      } catch (Exception ex)  {
          ex.printStackTrace();
          regresa = false; 
      }
      return regresa;
    }
    
    
    public boolean registraSaldos(List<Vista> regSaldos, Connection conn, String fechaCarga, String idProgS) throws SQLException {
      boolean regresa = true;
      StringBuffer listaValores = new StringBuffer();
      StringBuffer listaCampos = new StringBuffer();
      String[] saldos;
      String seqSaldos;
      BigDecimal saldoCalculadoAnt;
      BigDecimal saldoFinBanco;
      BigDecimal saldoIniBanco;
      for (Vista reg : regSaldos ) {
        if(regresa){
          listaValores.append(reg.getField("ID_CUENTA").concat(", "));
          listaCampos.append("ID_CUENTA, ");
          listaValores.append("to_date('".concat(fechaCarga).concat("','dd/mm/yyyy')").concat(", "));
          listaCampos.append("FECHA, ");
          if (reg.getField("TOTMOV") == null || reg.getField("TOTMOV").equals("0")){
            listaValores.append(reg.getField("SALDO_BANCO") == null ? "0, " : reg.getField("SALDO_BANCO").concat(", "));
            listaCampos.append("SALDO_BANCO, ");
            listaValores.append(reg.getField("SALDO_CALCULADO") == null ? "0, " : reg.getField("SALDO_CALCULADO").concat(", "));
            listaCampos.append("SALDO_CALCULADO, ");
            seqSaldos = getSecuenciaSaldosDiarios();
            listaCampos.append("ID_SALDO_DIARIO");
            listaValores.append(seqSaldos);
            regresa = insertaDatos(listaCampos,listaValores, conn, "RF_TR_SALDOS_DIARIOS");
          }
          else{
            saldos = getSaldosBanco(Integer.parseInt(reg.getField("ID_CUENTA")),fechaCarga,idProgS);
            saldoIniBanco = new BigDecimal(saldos[0] == null? "0":saldos[0].toString() );
            saldoFinBanco = new BigDecimal(saldos[1] == null? "0":saldos[1].toString());
            listaCampos.append("SALDO_BANCO, ");
            listaValores.append(saldoFinBanco == null ? new BigDecimal(0): saldoFinBanco);
            listaValores.append(", ");
            
            BigDecimal montoMov = new BigDecimal(reg.getField("TOTAL").toString());
            // Number montoMov = new Number(reg.getField("TOTAL").toString() == null? "0":reg.getField("TOTAL").toString());
            saldoCalculadoAnt = new BigDecimal(reg.getField("SALDO_CALCULADO")==null?"0":reg.getField("SALDO_CALCULADO").toString());
            //saldoCalculadoAnt = new Number(reg.getField("SALDO_CALCULADO")== null ? "0": reg.getField("SALDO_CALCULADO"));
            if (saldoCalculadoAnt == null) {
                  saldoCalculadoAnt = new BigDecimal(0);
              }
            BigDecimal saldoActual = saldoCalculadoAnt.add(montoMov);
            listaValores.append(saldoActual == null ? new BigDecimal(0): new BigDecimal(Math.round(saldoActual.doubleValue() * 100.00) / 100.00));
            listaValores.append(", ");
            listaCampos.append("SALDO_CALCULADO, ");
            seqSaldos = getSecuenciaSaldosDiarios();
            listaCampos.append("ID_SALDO_DIARIO");
            listaValores.append(seqSaldos);
            regresa = insertaDatos(listaCampos,listaValores, conn, "RF_TR_SALDOS_DIARIOS");
            if ( (!montoMov.equals(saldoFinBanco.subtract(new BigDecimal(reg.getField("SALDO_BANCO")==null?"0":reg.getField("SALDO_BANCO").toString()))) &&
                  !existeDiferencia(Integer.parseInt(reg.getField("ID_CUENTA")),fechaCarga)) ||
                  (existeDiferencia(Integer.parseInt(reg.getField("ID_CUENTA")),fechaCarga) &&
                  !montoMov.equals(saldoFinBanco.subtract(saldoIniBanco)))
               ){
              regresa = registraDiferenciaSaldo(Integer.parseInt(reg.getField("ID_CUENTA")),seqSaldos,fechaCarga,montoMov,saldoFinBanco.subtract( new BigDecimal(reg.getField("SALDO_BANCO")==null?"0":reg.getField("SALDO_BANCO").toString())),conn);
            }
          }
          listaCampos.replace(0,listaCampos.length(),"");
          listaValores.replace(0,listaValores.length(),"");
        }
      }
      return regresa;
    }
    
    public boolean actualizaBitacora(int numEmpleado, int idProg, String fechaCarga, Connection conn){
      boolean regresa;
      String mensaje = " ";
      int iNumEmp;
      StringBuffer listaValores = new StringBuffer();
      StringBuffer listaCampos = new StringBuffer();
      System.out.println("Criterios.actualizaBitacora.numEmpleado "+numEmpleado);
      System.out.println("Criterios.actualizaBitacora.idProg "+idProg);
      System.out.println("Criterios.actualizaBitacora.fechaCarga "+fechaCarga);
      try  {
        iNumEmp = numEmpleado;
        listaValores.append("to_date('".concat(fechaCarga).concat("','dd/mm/yyyy'),"));
        listaCampos.append("fecha_movimientos, ");
        listaValores.append(iNumEmp);
        listaValores.append(", ");
        listaCampos.append("num_empleado, ");
        listaValores.append(idProg);
        listaValores.append(", ");
        listaCampos.append("id_tipo_programa, ");
        listaValores.append("to_date('".concat(Fecha.getHoy()).concat("','dd/mm/yyyy'), "));
        listaCampos.append("fecha_carga, ");
        listaValores.append("'".concat(mensaje).concat("', "));
        listaCampos.append("observaciones, ");
        listaValores.append("SEQ_TR_BITACORA_ACTUALIZACION.nextval");
        listaCampos.append("id_reg_bitacora");
        System.out.println("Criterios.actualizaBitacora antes del query");
        regresa = insertaDatos(listaCampos,listaValores, conn, "RF_TR_BITACORA_ACTUALIZACION");
        System.out.println("Criterios.actualizaBitacora después del query");
      } catch (Exception ex)  {
          ex.printStackTrace();
          regresa = false; 
      }
      return regresa; 
    }
    
    
    public boolean registraBitacoraLocal(int numEmpleado, int idEvento, int idProceso, String parametros, String estatus, Connection conn){
      boolean regresa;
      int iNumEmp;
      StringBuffer listaValores = new StringBuffer();
      StringBuffer listaCampos = new StringBuffer();
      try  {
        iNumEmp = numEmpleado;
        listaCampos.append("idevento, idproceso, parametros, numempleado, fechasolicitud, estatus ");
        listaValores.append(idEvento);              
        listaValores.append(", ");
        listaValores.append(idProceso);              
        listaValores.append(", ");
        listaValores.append("'".concat(parametros).concat("',"));              
        listaValores.append(iNumEmp);
        listaValores.append(", ");
        listaValores.append("to_date('".concat(Fecha.getHoy()).concat("','dd/mm/yyyy'), "));
        listaValores.append("'".concat(estatus).concat("'"));              
        regresa = insertaDatos(listaCampos,listaValores, conn, "BITACORALOCAL");
      } catch (Exception ex)  {
          ex.printStackTrace();
          regresa = false; 
      } 
      return regresa;
    }
 
    private boolean ejecutaSentencia(String sentencia, Connection conn){
      boolean regresa; 
      int ejecuto;
      SentennciasSE sentenciasSE;
      try  {
        sentenciasSE = new SentennciasSE(DaoFactory.CONEXION_TESORERIA);  
        ejecuto = sentenciasSE.ejecutar(conn,sentencia);
        if (ejecuto!=-1) {
              regresa = true;
          }
        else {
              regresa = false;
          } 
      } catch (Exception ex)  {
          ex.printStackTrace();
          regresa = false; 
      }
      return regresa;
    }
 
 
    public boolean actualizaSaldo(int idPrograma, String fechaImportada, Connection conn){
      boolean regresa;
      Map parametro = new HashMap(); 
      SentennciasSE sentenciasSE;
      try  {
          sentenciasSE = new SentennciasSE(DaoFactory.CONEXION_TESORERIA);  
          if (idPrograma == 7) {
              parametro.put("idPrograma", Integer.toString(idPrograma).concat(",9"));
          }
          else {
              parametro.put("idPrograma", idPrograma);
          }
          parametro.put("fecha",fechaImportada);
          regresa = ejecutaSentencia(sentenciasSE.getComando("criterios.update.actualizaSaldos.bancas",parametro),conn);    
      } catch (Exception ex)  {
          ex.printStackTrace();
          regresa = false;
      }
      return regresa;
    }
 
}
