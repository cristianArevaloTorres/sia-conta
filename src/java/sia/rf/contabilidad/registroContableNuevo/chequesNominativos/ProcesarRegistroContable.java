package sia.rf.contabilidad.registroContableNuevo.chequesNominativos;

import java.sql.Connection;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import java.util.Map;

import sia.db.dao.DaoFactory;
import sia.db.sql.Sentencias;
import sia.db.sql.Vista;

import sia.libs.formato.Cadena;
import sia.libs.formato.Error;
import sia.libs.formato.Fecha;

import sia.libs.formato.Numero;

import sia.rf.contabilidad.registroContable.formas.Areas;

import sia.rf.contabilidad.registroContable.formas.servicios.CuentasForma;
import sia.rf.contabilidad.registroContable.procesos.polizas.servicios.Polizas;

import sia.ws.publicar.contabilidad.definicion.FormaLista;
import sia.ws.publicar.contabilidad.definicion.PrepararForma;

import sia.rf.contabilidad.registroContableNuevo.bcCheque;
import sia.rf.contabilidad.registroContableNuevo.bcPoliza;
import sia.rf.contabilidad.registroContableNuevo.bcCuentasCheques;

public class ProcesarRegistroContable {

  public static final int MIPF = 1;
  public static final int SIA_VIATICOS = 2;
  public static final int SIA_GASTOS_CAMPO = 5;
  public static final int MIPF_ENTEROS_SP = 7;
  public static final int MIPF_NOMINA = 8;
  public static final int ANTICIPOS_SUELDO = 9;
  public static final int OTROS_PAGOS_SP = 10;
  public static final int NOMINA_CENSOS = 11;
  public static final int SP_DEVOLUCIONES = 15;
  public static final int SP_PENSIONES = 14;
  public static final int COM_INTERNACIONALES= 13;
  private String unidad;
  private String ambito;
  private int ejercicio;
  private String entidad;
  private int numEmpleado;
  private String idCheques;
  public String salida;

  public ProcesarRegistroContable(String unidad, String entidad, String ambito, int ejercicio, int numEmpleado) {
    setUnidad(unidad);
    setAmbito(ambito);
    setEjercicio(ejercicio);
    setEntidad(entidad);
    setNumEmpleado(numEmpleado);
    setIdCheques("");
  }
  
  public void setUnidad(String unidad) {
    this.unidad = unidad;
  }

  public String getUnidad() {
    return unidad;
  }

  public void setAmbito(String ambito) {
    this.ambito = ambito;
  }

  public String getAmbito() {
    return ambito;
  }

  public void setEjercicio(int ejercicio) {
    this.ejercicio = ejercicio;
  }

  public int getEjercicio() {
    return ejercicio;
  }

  public void setEntidad(String entidad) {
    this.entidad = entidad;
  }

  public String getEntidad() {
    return entidad;
  }
  
  public void setNumEmpleado(int numEmpleado) {
    this.numEmpleado = numEmpleado;
  }

  public int getNumEmpleado() {
    return numEmpleado;
  }
  
  public StringBuffer procesar(Connection conexion, List<Vista> cheques) throws Exception{
    return procesarCheques(conexion, cheques);
  }
  
  private StringBuffer procesarCheques(Connection conexion, List<Vista> cheques) throws Exception{
    String cadenaVariables = null;
    String partida;
    String campo;
    String forma = null;
    StringBuffer resultado = null;
    String arrayOperacionPago[];
    String operacionPago;
    int idOrigen;
    String fechaPoliza;
    String totalCxl[];
    String  cxlPoliza;
    //String cxl = null;
    //Connection conexionMipf = null;
    String unidadEjecutora = Cadena.rellenar(getUnidad(),4,'0',true);
//    System.out.println("Unidad ejecutora: " + unidadEjecutora);
    String entidadAmbito = Cadena.rellenar(getEntidad().concat(getAmbito()),4,'0',true);
//    System.out.println("Ambito: " + entidadAmbito);
    String consecutivoCheque;
    String polizaGenerada;
    bcPoliza bcPoliza;
    try{
    //conexionMipf=DaoFactory.getConnection("jdbc/mipfDS");
    resultado = new StringBuffer();
    bcPoliza =new bcPoliza();
    for(Vista cheque:cheques){
      campo = "PARTIDA";
      arrayOperacionPago=  cheque.getField("OPERACION_PAGO").split(",");
      operacionPago = "'";
      for(int i=0; i<arrayOperacionPago.length; i++){
        operacionPago = operacionPago.concat(arrayOperacionPago[i]).concat("','");
      }
      operacionPago = operacionPago.substring(0,operacionPago.length()-2);
      fechaPoliza = cheque.getField("FECHA_PAGO").substring(6,10).concat(cheque.getField("FECHA_PAGO").substring(3,5)).concat(cheque.getField("FECHA_PAGO").substring(0,2));
  //    System.out.println("cuenta bancaria:" + cheque.getField("CUENTA_BANCARIA"));
      if(cheque.getField("CUENTA_BANCARIA").equals("0114567455----")){//AREA CENTRAL
        System.out.println("CXL Area central.");
        forma = "CB"; //Originalmente forma NC
        idOrigen = Integer.valueOf(cheque.getField("ID_ORIGEN_CON").split(",")[0]);
       
        //histeje = "4_7";
        String variables = null;
        switch(idOrigen){
          case MIPF:
          case MIPF_ENTEROS_SP:
          case MIPF_NOMINA:
          case ANTICIPOS_SUELDO:
          case OTROS_PAGOS_SP:
          case NOMINA_CENSOS: 
          case SP_DEVOLUCIONES:
          case SP_PENSIONES:
            partida = queryHisteje(conexion, operacionPago, campo);
            variables = partidaCentral(partida, idOrigen);
        System.out.println("Pasó partidaCentral ok");
            if(variables.contains("-1"))
              throw new Exception(",Partida fuera de rango: "+ partida.replace(',','-'));
            cadenaVariables = "";
            cadenaVariables = cadenaVariables.concat("UNIDAD=").concat(unidadEjecutora).concat("|");
            cadenaVariables = cadenaVariables.concat("AMBITO=").concat(entidadAmbito).concat("|");
            cadenaVariables = cadenaVariables.concat("IMPORTE=").concat(cheque.getField("IMPORTE")).concat("|");
            cadenaVariables = cadenaVariables.concat("REFERENCIA=").concat(cheque.getField("BENEFICIARIO")).concat("|");
            cadenaVariables = cadenaVariables.concat("NIVEL7=").concat(variables.split(",")[0]).concat("|");
            cadenaVariables = cadenaVariables.concat("NIVEL8=").concat(variables.split(",")[1]).concat("|");
            cadenaVariables = cadenaVariables.concat("NIVEL7_1112=").concat(variables.split(",")[2]).concat("|");
            System.out.println("Cadena de variables de CXL Central: "+ cadenaVariables);
          break;
          default: partida = "El origen " +idOrigen+ " se encuentra fuera de rango"; break;
        }
      }else{         
        if(operacionPago.contains("OFR")){            // OFICIO DE FONDO ROTATORIO
          System.out.println("Cheques de fondo rotatorio.");
          forma = "CC"; // Originalmente NB
          cadenaVariables = "";
          cadenaVariables = cadenaVariables.concat("UNIDAD=").concat(unidadEjecutora).concat("|");
          cadenaVariables = cadenaVariables.concat("AMBITO=").concat(entidadAmbito).concat("|");
          cadenaVariables = cadenaVariables.concat("IMPORTE=").concat(cheque.getField("IMPORTE")).concat("|");
          cadenaVariables = cadenaVariables.concat("REFERENCIA=").concat(cheque.getField("BENEFICIARIO")).concat("|");
          System.out.println("Cadena de variables de OFR: "+ cadenaVariables);
        }
        else{              
          if(!cheque.getField("CUENTA_BANCARIA").equals("0114567455-")){
            System.out.println("ProcesarRegistroContable.java Cheques de CXL de unidades");
            forma = "CS"; //Originalmente NA, luego fue CB
            partida = queryHisteje(conexion, operacionPago, campo);
            
            partida = partida.substring(0,1) + "000"; 
            System.out.println("partida quedó como " + partida);
            /*
            if(partida.startsWith("1"))
              partida = "1000";
            else
              partida = "2000";*/
            //obtenerVariables();
            System.out.println("CHEQUE: " + cheque);
            cadenaVariables = obtenerVariables(conexion,cheque,partida);
          }
        }
      }      
      //Proceso para la poliza
//      System.out.println("Empieza generacion de póliza. AVM");
      
      totalCxl = cheque.getField("OPERACION_PAGO").split(",");
      cxlPoliza = "";
      for(String cxl:totalCxl){
        cxlPoliza = cxlPoliza.concat(cheque.getField("EJERCICIO").substring(2,4).concat("-").concat(cxl).concat(","));
        //solo se requiere guardar la ultima CXL
        //cxlPoliza = cheque.getField("EJERCICIO").substring(2,4).concat("-").concat(cxl);
      }
      cxlPoliza = cxlPoliza.substring(0,cxlPoliza.length()-1);
  //    System.out.println("Antes de procesar póliza: ");
      polizaGenerada = procesarPoliza(conexion, cadenaVariables, forma, fechaPoliza,cxlPoliza.concat("-").concat(cheque.getField("CONCEPTO")),cheque.getField("OPERACION_PAGO"));
      //System.out.println("Termina generacion de poliza. ");//if (true){ return new StringBuffer(1);}
      //Inserta cheque en tabla y actualiza la informacion en el mipf
      //System.out.println("Empieza generacion de cheque");
      consecutivoCheque = insertarCehque(conexion,cheque,polizaGenerada.substring(0,(polizaGenerada.indexOf(","))),cxlPoliza);
      //System.out.println("Termina generacion de cheque");
      bcPoliza.update_referencia_consecutivoCheque(conexion,polizaGenerada.substring(0,(polizaGenerada.indexOf(","))),consecutivoCheque);
      resultado.append("<tr>");
      resultado.append("<td>").append(cheque.getField("OPERACION_PAGO")).append("</td>");
      resultado.append("<td align=\"center\">").append(polizaGenerada.split(",")[1]).append("</td>");
      resultado.append("<td align=\"center\">").append(polizaGenerada.split(",")[2]).append("</td>");
      resultado.append("<td align=\"center\">").append(polizaGenerada.split(",")[3]).append("</td>");
      resultado.append("<td align=\"right\">").append(consecutivoCheque).append("</td>");
      //resultado.append(polizaGenerada.).append(" - Consecutivo cheque ").append(consecutivoCheque);
      resultado.append("</tr>");
    }
    //resultado.append(getIdCheques().concat(","));
    
  }catch(Exception e){
     e.printStackTrace();
     conexion.rollback();
     throw new Exception(e.getMessage());
   }
   finally{
    /* if(conexionMipf != null);
       conexionMipf.close();
     conexionMipf = null;*/
     arrayOperacionPago= null;
   }
   return resultado;
  }
  
  
  private String queryHisteje(Connection conexion,String cxl, String campo){
    Sentencias sentencia;
    List<Vista> registros;
    Map parametros;
    String regresa = null;
    try{
      sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD,Sentencias.XML);
      parametros = new HashMap();
      registros = new ArrayList<Vista>();
      parametros.put("cxl",cxl);
      parametros.put("ejercicio",getEjercicio());
      //System.out.println("queryhisteje: ");
      System.out.println(sentencia.getComando("chequesNominativos.select.partidasHisteje",parametros));
      registros = sentencia.registros(sentencia.getComando("chequesNominativos.select.partidasHisteje",parametros),conexion);
      if(registros != null){
        regresa = "";
        for(Vista registro:registros){
          regresa = regresa.concat(registro.getField(campo)).concat(",");
        }
        if(registros.size()==1)
          regresa = regresa.substring(0,regresa.length()-1);
      }
    }catch(Exception e){
      e.printStackTrace();
    }
    finally{
      sentencia = null;
      parametros = null;
      registros = null; //System.out.println("regreso de queryhisteje: " + regresa);
    }
    return regresa;
  }
  
  private List<Vista> asientoDesglozado(Connection conexion, Map parametros){
    Sentencias sentencia = null;
    List<Vista> registros = null;
    //Map parametros = null;
    //String condicion = null;
    String campos = null;
    try{
      sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD,Sentencias.XML);
      //parametros = new HashMap();
      registros = new ArrayList<Vista>();
      //condicion = " and oc.id_operacion in ("+idOperacion+") and cn.id_cheque_nominativo in ("+idChequeNominativo+")";
      campos = " beneficiario, fecha_pago, cuenta_bancaria, id_origen, id_cuenta , id_operacion, id_cheque_nominativo, tipo_registro";
      //parametros.put("condicion",condicion);
      parametros.put("campos",campos);
      parametros.put("camposGrupo",campos);
      parametros.put("ordenar","");
      //System.out.println(sentencia.getComando("chequesNominativos.select.verificaCheques",parametros));
      registros = sentencia.registros(sentencia.getComando("chequesNominativos.select.verificaCheques",parametros),conexion);
    }catch(Exception e){
      e.printStackTrace();
    }
    finally{
      sentencia = null;
      //parametros = null;
    }
    return registros;
  }
  
  private String obtenerConsecutivoCheque(Connection conexion, String idCuentaBancaria){
    Sentencias sentencia = null;
    List<Vista> registros = null;
    Map parametros = null;
    String regresa = null;
    try{
      sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD,Sentencias.XML);
      parametros = new HashMap();
      registros = new ArrayList<Vista>();
      parametros.put("idCuenta",idCuentaBancaria);
      parametros.put("ejercicio",getEjercicio());
      parametros.put("unidad",getUnidad());
      parametros.put("entidad",getEntidad());
      parametros.put("ambito",getAmbito());
      //System.out.println(sentencia.getComando("chequesNominativos.select.consecutivoCheque",parametros));
      registros = sentencia.registros(sentencia.getComando("chequesNominativos.select.consecutivoCheque",parametros),conexion);
      if(registros != null){
        for(Vista registro:registros){
          regresa = registro.getField("CUENTA_CHEQUES_ID").concat(",").concat(registro.getField("ULTIMO_CONSECUTIVO"));
        }
      }
    }catch(Exception e){
      e.printStackTrace();
    }
    finally{
      sentencia = null;
      parametros = null;
      registros = null;
    }
    return regresa;
  }
  
  private String origenCxl(String partida, String idOrigen){
    String origen = null;
//    System.out.println("partida: " + partida);
   // System.out.println("idOrigen: " + idOrigen);
    if(partida.equals("1000")){
      switch(Integer.valueOf(idOrigen)){
        case MIPF: origen="0016";break;
        case MIPF_ENTEROS_SP: origen="0019";break;
        case OTROS_PAGOS_SP: origen="0020";break;
        case MIPF_NOMINA: origen="0021";break;
        case ANTICIPOS_SUELDO: origen="0022";break;
        case NOMINA_CENSOS: origen="0023";break;
        case SP_PENSIONES: origen="0024";break;
        case SP_DEVOLUCIONES: origen="0025";break;
        default: origen = "No Identificado";break;
      }
    }else{
      switch(Integer.valueOf(idOrigen)){
        case MIPF: origen="0016";break;
        case SIA_VIATICOS: origen="0017";break;
        case SIA_GASTOS_CAMPO: origen="0018";break;
        case SP_DEVOLUCIONES: origen="0025";break;
        case COM_INTERNACIONALES: origen = "0026";break;
        case MIPF_ENTEROS_SP: origen="0019";break;
        default: origen = "No Identificado";break;
      }
    }
    return origen;
  }
  
  private String obtenerVariables(Connection conexion, Vista cheque, String partida) throws Exception {
    List<Vista> chequesDesagregado = null;
    String arrayIdOrigen[] = cheque.getField("ID_ORIGEN_CON").split(",");
    HashSet<String>  listIdOrigen = new HashSet<String>();
    String cadenaVariables = null;
    String origen = null;
    String idOrigen = null;
    Map parametros = new HashMap();
    String tipoBene = cheque.getField("TIPO_REGISTRO");
    //Se verifica si el origen es igual no desagrega la informacion y si es diferente desagrega la informacion
//    System.out.println("Entra metodo para obtener variables de CXL Unidades");
    for(int i=0; i<arrayIdOrigen.length; i++){
      listIdOrigen.add(arrayIdOrigen[i]);
    }
    if(listIdOrigen.size() == 1){
      //No se desagrega la informacion
       idOrigen = cheque.getField("ID_ORIGEN_CON").split(",")[0];
       origen = origenCxl(partida,idOrigen);
       if(origen.startsWith("No"))
         throw new Exception("El origen que se pretende procesar no existe, favor de notificarlo.");
       cadenaVariables = "";
       cadenaVariables = cadenaVariables.concat("UNIDAD=").concat(getUnidad()).concat("|");
       cadenaVariables = cadenaVariables.concat("AMBITO=").concat(getEntidad()+getAmbito()).concat("|");
       cadenaVariables = cadenaVariables.concat("IMPORTE=").concat(cheque.getField("IMPORTE")).concat("|");
       cadenaVariables = cadenaVariables.concat("REFERENCIA=").concat(cheque.getField("BENEFICIARIO")).concat("|");
       cadenaVariables = cadenaVariables.concat("TIGA_CHEQUE=").concat(partida).concat("|");
       cadenaVariables = cadenaVariables.concat("ORIGEN=").concat(origen).concat("|");
       cadenaVariables = cadenaVariables.concat("CC_PRESUP_PAGADO=").concat(cheque.getField("CC_PRESUP_PAGADO")).concat("|");
       cadenaVariables = cadenaVariables.concat("IMPORTE_PRESUP_PAGADO=").concat(cheque.getField("IMPORTE_PRESUP_PAGADO")).concat("|");
       cadenaVariables = cadenaVariables.concat("CC_PRESUP_EJERCIDO=").concat(cheque.getField("CC_PRESUP_EJERCIDO")).concat("|");
       cadenaVariables = cadenaVariables.concat("IMPORTE_PRESUP_EJERCIDO=").concat(cheque.getField("IMPORTE_PRESUP_EJERCIDO")).concat("|");
       cadenaVariables = cadenaVariables.concat("CC_PRESUP_DEVENGADO=").concat(cheque.getField("CC_PRESUP_DEVENGADO")).concat("|");
       cadenaVariables = cadenaVariables.concat("IMPORTE_PRESUP_DEVENGADO=").concat(cheque.getField("IMPORTE_PRESUP_DEVENGADO")).concat("|");
       cadenaVariables = cadenaVariables.concat("CC_CXP_CP=").concat(cheque.getField("CC_CXP_CP")).concat("|");
       cadenaVariables = cadenaVariables.concat("IMPORTE_CXP_CP=").concat(cheque.getField("IMPORTE_CXP_CP")).concat("|");
       cadenaVariables = cadenaVariables.concat("CC_ACTIVO=").concat(cheque.getField("CC_ACTIVO")).concat("|");
       cadenaVariables = cadenaVariables.concat("IMPORTE_ACTIVO=").concat(cheque.getField("IMPORTE_ACTIVO")).concat("|");
    }else{
      //Se desagrega la informacion para afectar las subcuentas correspondientes
       chequesDesagregado = new ArrayList<Vista>();
       parametros.put("condicionINE",tipoBene.equals("CXL3")?" and oc.id_operacion in ("+cheque.getField("ID_OPERACION")+") and cn.id_cheque_nominativo in ("+cheque.getField("ID_CHEQUE_NOMINATIVO")+")":"and oc.id_operacion = 0 and cn.id_cheque_nominativo = 0");
      parametros.put("condicionPR",tipoBene.equals("CXL1")?" and oc.id_operacion in ("+cheque.getField("ID_OPERACION")+")":" and oc.id_operacion = 0");
      parametros.put("condicionSP",tipoBene.equals("CXL2")?" and oc.id_operacion in ("+cheque.getField("ID_OPERACION")+")":" and oc.id_operacion = 0");
      parametros.put("condicionOFR",tipoBene.equals("OFR")?" and oc.id_operacion in ("+cheque.getField("ID_OPERACION")+") and cn.id_cheque_nominativo in ("+cheque.getField("ID_CHEQUE_NOMINATIVO")+")":"and oc.id_operacion = 0 and cn.id_cheque_nominativo = 0");
      /*for(String operacion: listaSeleccionada){
         if(operacion.split(",")[0].equals("CXL1")){
           idOperacionPR = idOperacionPR.concat(operacion.split(",")[1]).concat("','");
           //idOperacionPR = idOperacionPR.substring(0,idOperacionPR.length()-2);
           //condicionPR = "and oc.id_operacion in (".concat(idOperacionPR).concat(")");
         }else{
           if(operacion.split(",")[0].equals("CXL2")){
             idOperacionSP = idOperacionSP.concat(operacion.split(",")[1]).concat("','");
             //idOperacionSP = idOperacionSP.substring(0,idOperacionSP.length()-2);
             //condicionSP = "and oc.id_operacion in (".concat(idOperacionPR).concat(")");
           }else{
             if(operacion.split(",")[0].equals("CXL3")){
               idOperacionINE = idOperacionINE.concat(operacion.split(",")[1]).concat("','");
               idChequeINE = idChequeINE.concat(operacion.split(",")[2]).concat("','");
               //idOperacionINE = idOperacionINE.substring(0,idOperacionINE.length()-2);
               //idChequeINE = idChequeINE.substring(0,idChequeINE.length()-2);
               //condicionINE = "and oc.id_operacion in (".concat(idOperacionINE).concat(") and cn.id_cheque_nominativo(").concat(idChequeINE).concat(")");
             }else{
               if(operacion.split(",")[0].equals("OFR")){
                 idOperacionOFR = idOperacionOFR.concat(operacion.split(",")[1]).concat("','");
                 idChequeOFR = idChequeOFR.concat(operacion.split(",")[2]).concat("','");
                 //idOperacionOFR = idOperacionOFR.substring(0,idOperacionINE.length()-2);
                 //idChequeOFR = idChequeOFR.substring(0,idChequeOFR.length()-2);
                 //condicionOFR = "and oc.id_operacion in (".concat(idOperacionOFR).concat(") and cn.id_cheque_nominativo(").concat(idChequeOFR).concat(")");
               }
             }
           }
         }
         
         //idOperacion = idOperacion.concat(operacion.split(",")[0]).concat("','");
         //idCheque = idCheque.concat(operacion.split(",")[1]).concat("','");
       }*/
       chequesDesagregado = asientoDesglozado(conexion, parametros);
       
       cadenaVariables = "";
       for(Vista chequeDesagregado: chequesDesagregado){
         origen = origenCxl(partida,chequeDesagregado.getField("ID_ORIGEN_CON"));
         cadenaVariables = cadenaVariables.concat("UNIDAD=").concat(getUnidad()).concat("|");
         cadenaVariables = cadenaVariables.concat("AMBITO=").concat(getEntidad()+getAmbito()).concat("|");
         cadenaVariables = cadenaVariables.concat("IMPORTE=").concat(chequeDesagregado.getField("IMPORTE")).concat("|");
         cadenaVariables = cadenaVariables.concat("REFERENCIA=").concat(cheque.getField("BENEFICIARIO")).concat("|");
         cadenaVariables = cadenaVariables.concat("TIGA_CHEQUE=").concat(partida).concat("|");
         cadenaVariables = cadenaVariables.concat("ORIGEN=").concat(origen).concat("|").concat("~");
        cadenaVariables = cadenaVariables.concat("CC_PRESUP_PAGADO=").concat(cheque.getField("CC_PRESUP_PAGADO")).concat("|");
        cadenaVariables = cadenaVariables.concat("IMPORTE_PRESUP_PAGADO=").concat(cheque.getField("IMPORTE_PRESUP_PAGADO")).concat("|");
        cadenaVariables = cadenaVariables.concat("CC_PRESUP_EJERCIDO=").concat(cheque.getField("CC_PRESUP_EJERCIDO")).concat("|");
        cadenaVariables = cadenaVariables.concat("IMPORTE_PRESUP_EJERCIDO=").concat(cheque.getField("IMPORTE_PRESUP_EJERCIDO")).concat("|");
        cadenaVariables = cadenaVariables.concat("CC_PRESUP_DEVENGADO=").concat(cheque.getField("CC_PRESUP_DEVENGADO")).concat("|");
        cadenaVariables = cadenaVariables.concat("IMPORTE_PRESUP_DEVENGADO=").concat(cheque.getField("IMPORTE_PRESUP_DEVENGADO")).concat("|");
        cadenaVariables = cadenaVariables.concat("CC_CXP_CP=").concat(cheque.getField("CC_CXP_CP")).concat("|");
        cadenaVariables = cadenaVariables.concat("IMPORTE_CXP_CP=").concat(cheque.getField("IMPORTE_CXP_CP")).concat("|");
        cadenaVariables = cadenaVariables.concat("CC_ACTIVO=").concat(cheque.getField("CC_ACTIVO")).concat("|");
        cadenaVariables = cadenaVariables.concat("IMPORTE_ACTIVO=").concat(cheque.getField("IMPORTE_ACTIVO")).concat("|");
       }
       
    }
  //  System.out.println("Variables a reemplazar de CXL Unidades: "+cadenaVariables);
    return cadenaVariables;
  }

  
  private String partidaCentral(String partida, int origen){   
    String regresa = null;
    String[] partidas = partida.split(",");
    String partidasNomina = "11301|12201|13101|13201|13202|13301|15402|15901|15401|15402|15403|";
    boolean contenido = false;
    System.out.println("Obtiene valor de las subcuentas de CXL Central");
    if(partida.contains("17102")){//era 17102
      regresa = "100,40"; 
    }else{
      if(partida.contains("39401")){
        regresa = "100,46"; 
      }else{
        if(partida.contains("15301")){
          regresa = "101,1"; 
        }else{
          if(partida.contains("15202")){
            regresa = "100,41"; 
          }else{
            for(int i=0; i < partidas.length; i++){
              if(partidasNomina.contains(partidas[i])){
                regresa = "100,9";
              }else{
                 contenido = true;
                 if(contenido)
                   regresa = "-1";
                 else
                  regresa = "100,47";
              }
            }
          }
        }
      }
    }
    if(partida.contains("15301"))
      regresa = regresa.concat(",2");
    else
      regresa = regresa.concat(",1");
    System.out.println("Subcuenta a afectar: "+regresa);
    return regresa;
  }
  
  private String procesarPoliza(Connection conexion, String cadenaVariables, String forma, String fechaPoliza, String concepto, String referencia) throws Exception {
    String msjNotificacion      = null;
    PrepararForma prepararForma                 = null;        
    Polizas poliza                              = null;
    String consecutivoPoliza = null;
    FormaLista formaLista       = null;
    String detalle [] = null;
    List<CuentasForma> cuentas  = new ArrayList<CuentasForma>();
    try{
//    System.out.println("*Cadena variables: " + cadenaVariables);
      detalle= cadenaVariables.split("~");  //antes estaba un ~
      if(detalle.length>1) {
//          System.out.println("Testing hay detalle, tamaño " + detalle.length);
        prepararForma = new PrepararForma(forma, getUnidad(), getEntidad(), getAmbito(), cadenaVariables, referencia, getNumEmpleado(), 1,fechaPoliza.concat(((Fecha.formatear(Fecha.HORA_LARGA,Fecha.getRegistro())).replace(":","")).trim()));
        prepararForma.setCuentasNoexiten(new ArrayList());
        for(int i=0; i<detalle.length; i++) {
          prepararForma.setVariables(detalle[i]);
          if (forma.equals("CS")){
              formaLista = prepararForma.getFormaPoliza("001","1","9", Areas.MIPF,String.valueOf(Calendar.getInstance().get(Calendar.YEAR)), conexion);
          }
          else{
          formaLista = prepararForma.getFormaPoliza("001","1","9", Areas.MIPF,String.valueOf(getEjercicio()), conexion);  
          }
          cuentas = isExisteCuenta(cuentas, formaLista.getCuentas());
          //cuentas.addAll(formaLista.getCuentas()); 
       }
        if(prepararForma.getCuentasNoexiten().size()>0)
          throw new Exception("No existe la cuenta "+ prepararForma.getCuentasNoexiten());
        poliza= prepararForma.getFormas(formaLista.getId(), cuentas, concepto,"-1", conexion);
      }
      else {
//          System.out.println("Testing no hay detalle");
        prepararForma = new PrepararForma(forma, getUnidad(), getEntidad(), getAmbito(), cadenaVariables, referencia, getNumEmpleado(), 1,fechaPoliza.concat(((Fecha.formatear(Fecha.HORA_LARGA,Fecha.getRegistro())).replace(":","")).trim()));
        //System.out.println("antes de preparar forma poliza: ");
          if (forma.equals("CS")){
              formaLista = prepararForma.getFormaPoliza("001","1","9", Areas.MIPF,String.valueOf(Calendar.getInstance().get(Calendar.YEAR)), conexion);
          }
          else{
          formaLista = prepararForma.getFormaPoliza("001","1","9", Areas.MIPF,String.valueOf(getEjercicio()), conexion);  
          }
 //       System.out.println("despues de preparar forma poliza: ");
        cuentas.addAll(formaLista.getCuentas());                    
        //System.out.println("despues de add all cuentas: ");
        //System.out.println("*cuentas.size: *" + cuentas.size() + "**");
        poliza= prepararForma.getFormas(formaLista.getId(), cuentas, concepto,"-1", conexion);
       // System.out.println("despues de poliza= prepararForma.getFormas");
      }

      //prepararForma = new PrepararForma(forma, getUnidad(), getEntidad(), getAmbito(), cadenaVariables, referencia, getNumEmpleado(), 1,fechaPoliza.concat(((Fecha.formatear(Fecha.HORA_LARGA,Fecha.getRegistro())).replace(":","")).trim()));
      //formaLista = prepararForma.getFormaPoliza("100","1","menso1", Areas.MIPF_NO_ACUMULADO,String.valueOf(getEjercicio()), conexion);   
      //poliza= prepararForma.getFormas(formaLista.getId(), formaLista.getCuentas(), concepto,"-1", conexion);
      //poliza= prepararForma.getFormas(formaLista.getId(), cuentas, concepto,idEvento, conexionContabilidad);
      if(poliza.registrarPolizaAfectacionWs(conexion,Polizas.REGISTRAR_POLIZA,0)){
        consecutivoPoliza = poliza.getAbreviaturaPoliza().concat("-").concat(Cadena.rellenar(String.valueOf(poliza.getConsecutivo()),5,'0',true));       
        msjNotificacion =String.valueOf(poliza.getPolizaId()).concat(",").
                                    concat(poliza.getAbreviaturaPoliza()).concat("-").
                                    concat(Cadena.rellenar(String.valueOf(poliza.getConsecutivo()), 5, '0', true)).concat(",").
                                    concat(Fecha.formatear(2,poliza.getFechaAfectacion())).concat(",").concat(Fecha.formatear(2,poliza.getFecha()));
        //System.out.println(msjNotificacion);
      } 
    }catch(Exception e){
      Error.mensaje(e,"Contabilidad");
      throw new Exception(e.getMessage());
    }
    return msjNotificacion;
  }
  
  private List<CuentasForma> isExisteCuenta(List<CuentasForma> cuentas, List<CuentasForma> cuentasABuscar) throws  Exception {
    CuentasForma cuentaForma = null;
    List<CuentasForma> cuentasAcumuladas  = new ArrayList<CuentasForma>();
    try{
      if(cuentas.size()==0){
        cuentas.addAll(cuentasABuscar); 
      }
      else{
        for(int pocicion=0; pocicion < cuentasABuscar.size();pocicion++){
          Iterator iteratorLista = cuentas.iterator();
          int agrego = 0;
          while (iteratorLista.hasNext()) {
            cuentaForma = (CuentasForma)iteratorLista.next();
            if (cuentasABuscar.get(pocicion).getCuentaContableId() == cuentaForma.getCuentaContableId() &&
              cuentasABuscar.get(pocicion).getTipoOperacion().equals(cuentaForma.getTipoOperacion()) && 
              cuentasABuscar.get(pocicion).getReferencia().equals(cuentaForma.getReferencia())) {
                cuentaForma.setImporte(sumarImporte(cuentaForma.getImporte(), cuentasABuscar.get(pocicion).getImporte()));
                agrego++;
            }
          }
          if(agrego==0){
            cuentasAcumuladas.add(cuentasABuscar.get(pocicion));
          }
        }
        cuentas.addAll(cuentasAcumuladas);
        cuentasAcumuladas = null;
        cuentasAcumuladas  = new ArrayList<CuentasForma>();
      }
    }
    catch(Exception e){
      Error.mensaje(e, "SIAFM");
      throw e;  
    }
    finally{
      cuentaForma = null;
      cuentasAcumuladas = null;
    }
    return cuentas;
  }
  
  private String sumarImporte(String importe1, String importe2){
    return String.valueOf (Numero.formatear(Numero.NUMERO_DECIMALES,(Double.valueOf(importe1)+Double.valueOf(importe2))));
  }
  
  private String insertarCehque(Connection conexion, Vista cheque, String idPoliza, String cxlCheque) throws Exception{
    String regresar = null;
    String consecutivoCheque[] = null;
    String lsChequeId = null;
    bcCheque bcCheque = null;
    String  tabla = "SAPFIN_PA.rf_tr_cheque_nominativo c";
    String condicion = "";
    bcCuentasCheques bcCuentaCheque = null;
    String operacionPago[] = cheque.getField("OPERACION_PAGO").split(",");
    String lsDigitoVer=null;
    String existeConsecutivo  = null;
    Sentencias sentencia = null;
    Map parametros = null;
    try{
      sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD,Sentencias.XML);
      parametros = new HashMap();
      bcCheque = new bcCheque();
      bcCuentaCheque = new bcCuentasCheques();
      consecutivoCheque = obtenerConsecutivoCheque(conexion,cheque.getField("ID_CUENTA")).split(",");
      //consecutivoCheque = "55,1158".split(",");
      lsDigitoVer=bcCheque.select_digito_verificador(conexion,consecutivoCheque[1]);
      existeConsecutivo =  bcCheque.select_consecutivo(conexion,consecutivoCheque[0],consecutivoCheque[1],String.valueOf(getEjercicio()));
      if(!existeConsecutivo.equals("-1"))
         throw new Exception("Ya existe un cheque capturado con el consecutivo "+consecutivoCheque[1]+" con Concepto " + cheque.getField("CONCEPTO") +", Beneficiario "+cheque.getField("BENEFICIARIO")+ ". Favor de verificar la informacion");
      lsChequeId=bcCheque.select_SEQ_rf_tr_cheques(conexion);
      bcCheque.setCheque_id(lsChequeId);
      bcCheque.setCuenta_cheques_id(consecutivoCheque[0]);
      bcCheque.setConsecutivo(consecutivoCheque[1]);
      bcCheque.setAbreviatura("CC");
      bcCheque.setReferencia(cheque.getField("OPERACION_PAGO").concat(" - ").concat(cheque.getField("CONCEPTO"))); //11042011 se cambio por concepto en vez de referencia
      bcCheque.setBeneficiario(cheque.getField("BENEFICIARIO"));
      bcCheque.setNumEmpleado(String.valueOf(getNumEmpleado()));
      bcCheque.setPoliza_id(idPoliza);
      bcCheque.setImporte(cheque.getField("IMPORTE"));
      bcCheque.setDigitoVerificador(lsDigitoVer);
      bcCheque.setFechaCheque(cheque.getField("FECHA_PAGO"));
      bcCheque.setOrigenDocto(cheque.getField("ID_ORIGEN_CON").contains(",")?"2":cheque.getField("ID_ORIGEN_CON"));
      bcCheque.setCxl(operacionPago[0]);
      bcCheque.setCxlSup(operacionPago[operacionPago.length-1]);
      bcCheque.setFecha_impresion("sysdate");
      bcCheque.setEstatus(" 1");
      bcCheque.insert_rf_tr_cheques(conexion);
      bcCuentaCheque.setConsecutivo(consecutivoCheque[1]);
      bcCuentaCheque.update_rf_tc_cuentas_consecutivo(conexion,consecutivoCheque[0]);
      if(cheque.getField("TIPO_REGISTRO").equals("CXL1") || cheque.getField("TIPO_REGISTRO").equals("CXL2")){
        tabla = "SAPFIN_PA.rf_tr_operaciones_cheques  c";
        condicion = "where c.id_operacion in (".concat(cheque.getField("ID_OPERACION")).concat(")");
      }else
        condicion = "where c.id_operacion in (".concat(cheque.getField("ID_OPERACION")).concat(") and c.id_cheque_nominativo in (").concat(cheque.getField("ID_CHEQUE_NOMINATIVO")).concat(")");
      parametros.put("idOperacion",cheque.getField("ID_OPERACION"));
      parametros.put("idChequeNominativo",cheque.getField("ID_CHEQUE_NOMINATIVO"));
      parametros.put("numCheque",consecutivoCheque[1]);
      parametros.put("idChequeConta",lsChequeId);
      parametros.put("tabla", tabla);
      parametros.put("condicion", condicion);
       //     System.out.println("insertarCheque paso 5.5");
      //System.out.println(sentencia.getComando("chequesNominativos.update.actualizaChequeNominativo",parametros));
      if(sentencia.ejecutar(conexion,"chequesNominativos.update.actualizaChequeNominativo", parametros) > 0){
        regresar = consecutivoCheque[1];
      //System.out.println("insertarCheque paso 6");
        setIdCheques(getIdCheques().concat(lsChequeId).concat(","));
      }
      else
        throw new Exception("Ocurrio un error al momento de generar el cheque");
      //registros(sentencia.getComando("chequesNominativos.select.actualizaChequeNominativo".concat(histeje),parametros),conexion);
    }catch(Exception e){
      Error.mensaje(e,"Contabilidad");
      throw new Exception(e.getMessage());
    }
    finally{
      consecutivoCheque = null;
      bcCheque = null;
      bcCuentaCheque = null;
      sentencia = null;
      parametros = null;
    }
    return regresar;
  }


  public void setIdCheques(String idCheques) {
    this.idCheques = idCheques;
  }

  public String getIdCheques() {
    return idCheques;
  }
  
  public void setOut(String salida){
  this.salida = salida;
  }

}
