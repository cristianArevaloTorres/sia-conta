<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page  import="sia.db.dao.*"%>
<%@ page  import="java.util.*,java.sql.*"%>
<%@ page import="sia.rf.contabilidad.registroContable.acciones.ControlRegistro"%>
<%@ page import="sia.db.sql.Sentencias,sia.db.dao.DaoFactory,sia.db.sql.Vista"%>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>   

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>c746VerificarResultado</title>
        <link rel="stylesheet" href="../../../../Librerias/Estilos/siaexplorer.css" type="text/css">
    <script type="text/javascript" language="javascript">
    
    function regresaVerificar(param){
        if(param == 'regresa'){
          document.getElementById("imprimir").disabled="true";
          document.getElementById("btnRegresar").disabled="true";
          document.getElementById("formularioVerifica").action="c746Resultado.jsp?cboMes="+document.formularioVerifica.cboMes.value
                                                                              +"&lstCuentas="+document.formularioVerifica.lstCuentas.value
                                                                              +"&ordenamiento="+document.formularioVerifica.ordenamiento.value
                                                                              +"&pocicion="+document.formularioVerifica.pocicion.value
                                                                              +"&tipoFormato="+document.formularioVerifica.tipoFormato.value;;
          document.getElementById("formularioVerifica").submit();
        }else{
        var pregunta= confirm('Al realizar esta acción se generará su cheque y póliza correspondiente, ¿Está seguro de continuar?');
         if (pregunta == true){
         //alert('hola');
          

          document.getElementById("imprimir").disabled="true";
          document.getElementById("formularioVerifica").action="c746Control.jsp";
          document.getElementById("formularioVerifica").target="_self";
          document.getElementById("formularioVerifica").submit();
          }
        }
      }
      function cerrar() {
        window.open('','_parent','');
        window.close();
      }
      
      function controlaPagina(control){
        alert('entra funcion controlaPagina  '+ control);
        document.controlPagina.value=control;
        alert(document.controlPagina.value);
        regresar();
        //alert(control);
      }
      
      function regresar() {
      alert(document.controlPagina.value);
          //cerrar();
          window.opener.regresaVerifiar(document.getElementById('controlPagina').value, document.getElementById('idOperacion').value, document.getElementById('idCheque').value);
      }
    </script>
  </head>
   <body id="bVerificar">
   <jsp:scriptlet>util.tituloPagina(out, "Contabilidad", " ", "Procesar cheques", "Imprimir", true);</jsp:scriptlet>  
<%
String listaSeleccionada[] = request.getParameterValues("cheque");
String idOperacion = "'";
String idCheque = "'";
String idCuentaBancaria = request.getParameter("idCuenta");
String mes= request.getParameter("mes");
String orden= request.getParameter("orden");
String tipoFormato = request.getParameter("tipoFormato");
String pocicion = request.getParameter("pocicion");
String condicionINE = null;
String condicionPR = null;
String condicionSP = null;
String condicionOFR = null;
String campos = null;
String campos2 = null;
String camposD = null;
String camposGrupo = null;
String camposGrupo2 = null;
String camposGrupoD = null;
int consecutivo = 0;
int numero=0;
  Connection conexion = null;
  Sentencias sentencia = null;
  List<Vista> registros = null;
  Map parametros = null;
  String ordernarQuery = "order by ";
  String idOperacionINE = "'";
  String idChequeINE = "'";
  String idOperacionPR = "'";
  String idOperacionSP = "'";
  String idOperacionOFR = "'";
  String idChequeOFR = "'";
  //UnificaCheque unificaCheque = null;
  //out.println(listaSeleccionada[0]);
  try{
    //unificaCheque = new UnificaCheque();
     switch(Integer.parseInt(orden)){
      case 1: ordernarQuery = ordernarQuery.concat("beneficiario");break;
      case 2: ordernarQuery = ordernarQuery.concat("fecha_pago");break;
      case 3: ordernarQuery = ordernarQuery.concat("operacion_pago");break;
      case 4: ordernarQuery = ordernarQuery.concat("origen_operacion");break;
      //default: ordernarQuery = ordernarQuery.concat("beneficiario");break;
    }
    for(String operacion: listaSeleccionada){
      if(operacion.split(",")[0].equals("CXL1")){
        idOperacionPR = idOperacionPR.concat(operacion.split(",")[1]).concat("','");
            idOperacionINE = idOperacionINE.concat(operacion.split(",")[1]).concat("','");
            idChequeINE = idChequeINE.concat(operacion.split(",")[2]).concat("','");
        idOperacionPR = idOperacionPR.concat(operacion.split(",")[1]).concat("','"); //Se agrega para identificar cheques de senado AVargas 20170111
        //idOperacionPR = idOperacionPR.substring(0,idOperacionPR.length()-2);
        //condicionPR = "and oc.id_operacion in (".concat(idOperacionPR).concat(")");
      }else{
        if(operacion.split(",")[0].equals("CXL2")){
//		out.println(operacion.split(",")[0]);
          idOperacionSP = idOperacionSP.concat(operacion.split(",")[1]).concat("','");
	//	out.println(idOperacionSP);
          //idOperacionSP = idOperacionSP.substring(0,idOperacionSP.length()-2);
          //condicionSP = "and oc.id_operacion in (".concat(idOperacionPR).concat(")");
        }else{
          if(operacion.split(",")[0].equals("CXL3")){
        idOperacionPR = idOperacionPR.concat(operacion.split(",")[1]).concat("','"); //Se agrega para identificar cheques de senado AVargas 20170111
            idOperacionINE = idOperacionINE.concat(operacion.split(",")[1]).concat("','");
            idChequeINE = idChequeINE.concat(operacion.split(",")[2]).concat("','");
            //System.out.println("idChequeINE: " + idChequeINE);
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
    }
    if(!idOperacionPR.equals("'") ){
        idOperacionPR = idOperacionPR.substring(0,idOperacionPR.length()-2);
        condicionPR = "oc.id_operacion in (".concat(idOperacionPR).concat(")");
      }
      
      if(!idOperacionSP.equals("'")){
        idOperacionSP = idOperacionSP.substring(0,idOperacionSP.length()-2);
        condicionSP = "oc.id_operacion in (".concat(idOperacionSP).concat(")");
      }
      
      if(!idOperacionINE.equals("'")){
        idOperacionINE = idOperacionINE.substring(0,idOperacionINE.length()-2);
        idChequeINE = idChequeINE.substring(0,idChequeINE.length()-2);
//        condicionINE = "and oc.id_operacion in (".concat(idOperacionINE).concat(") and cn.id_cheque_nominativo in (").concat(idChequeINE).concat(")");
        condicionINE = "and cn.id_cheque_nominativo in (".concat(idChequeINE).concat(")");
//System.out.println("condicionINE: " + condicionINE);
             }
      if(!idOperacionOFR.equals("'")){
        idOperacionOFR = idOperacionOFR.substring(0,idOperacionOFR.length()-2);
        idChequeOFR = idChequeOFR.substring(0,idChequeOFR.length()-2);
        condicionOFR = "and oc.id_operacion in (".concat(idOperacionOFR).concat(") and cn.id_cheque_nominativo in (").concat(idChequeOFR).concat(")");
      }
    //idOperacion = idOperacion.substring(0,idOperacion.length()-2);
    //idCheque = idCheque.substring(0,idCheque.length()-2);
    condicionINE = condicionINE == null?"and oc.id_operacion = 0 and cn.id_cheque_nominativo = 0":condicionINE;
    condicionPR = condicionPR == null?"oc.id_operacion = 0":condicionPR;
    condicionSP = condicionSP == null?"oc.id_operacion = 0":condicionSP;
    condicionOFR = condicionOFR == null?"and oc.id_operacion = 0 and cn.id_cheque_nominativo = 0":condicionOFR;
    conexion = DaoFactory.getContabilidad();
    sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD,Sentencias.XML);
    parametros = new HashMap();
    registros = new ArrayList<Vista>();
    //parametros.put("idOperacion",idOperacion);
    //parametros.put("idCheque",idCheque);
    
    //condicion = " and oc.id_operacion in ("+idOperacion+") and cn.id_cheque_nominativo in ("+idCheque+")";
    campos2 = "beneficiario, fecha_pago, cuenta_bancaria, id_cuenta, tipo_registro,"+
             "rtrim(xmlagg(xmlelement(e, id_cheque_nominativo || ',')).extract('//text()'),',') id_cheque_nominativo, "+
             "rtrim(xmlagg(xmlelement(e, id_operacion || ',')).extract('//text()'),',') id_operacion ";
    campos = "beneficiario, fecha_pago, cuenta_bancaria, id_cuenta, tipo_registro, ejercicio,"+
         "rtrim(xmlagg(xmlelement(e, id_cheque_nominativo || ',')).extract('//text()'),',') id_cheque_nominativo, "+
         "rtrim(xmlagg(xmlelement(e, id_operacion || ',')).extract('//text()'),',') id_operacion, " +
         "REFERENCIA, CC_PRESUP_PAGADO, IMPORTE_PRESUP_PAGADO, CC_PRESUP_EJERCIDO, IMPORTE_PRESUP_EJERCIDO, " +
         "CC_PRESUP_DEVENGADO, IMPORTE_PRESUP_DEVENGADO, CC_CXP_CP, IMPORTE_CXP_CP, CC_ACTIVO, IMPORTE_ACTIVO";
    camposD = "beneficiario, fecha_pago, cuenta_bancaria, id_cuenta, tipo_registro, ejercicio,"+
         " id_cheque_nominativo, id_operacion, REFERENCIA, 0 CC_PRESUP_PAGADO, "+
         " SUM(IMPORTE_PRESUP_PAGADO) IMPORTE_PRESUP_PAGADO, 0 CC_PRESUP_EJERCIDO, " +
         " SUM(IMPORTE_PRESUP_EJERCIDO) IMPORTE_PRESUP_EJERCIDO, 0 CC_PRESUP_DEVENGADO, " +
         " SUM(IMPORTE_PRESUP_DEVENGADO) IMPORTE_PRESUP_DEVENGADO, CC_CXP_CP, IMPORTE_CXP_CP, CC_ACTIVO, IMPORTE_ACTIVO, " +
         " OPERACION_PAGO, CONCEPTO, ID_ORIGEN_CON, TOTAL, IMPORTE, TOTAL_CHEQUE " ;
    camposGrupo2 = "beneficiario, fecha_pago, cuenta_bancaria, id_cuenta, tipo_registro";
    camposGrupo = "beneficiario, fecha_pago, cuenta_bancaria, id_cuenta, tipo_registro, ejercicio, " +
                "REFERENCIA, CC_PRESUP_PAGADO, IMPORTE_PRESUP_PAGADO, CC_PRESUP_EJERCIDO, IMPORTE_PRESUP_EJERCIDO," +
                "CC_PRESUP_DEVENGADO, IMPORTE_PRESUP_DEVENGADO, CC_CXP_CP, IMPORTE_CXP_CP, CC_ACTIVO, IMPORTE_ACTIVO";
    camposGrupoD = "beneficiario, fecha_pago, cuenta_bancaria, id_cuenta, tipo_registro, ejercicio, ID_CHEQUE_NOMINATIVO, ID_OPERACION," +
                "REFERENCIA, " + //CC_PRESUP_PAGADO, CC_PRESUP_EJERCIDO, " +
                //"CC_PRESUP_DEVENGADO," + 
                "CC_CXP_CP, IMPORTE_CXP_CP, CC_ACTIVO, IMPORTE_ACTIVO, " +
                "OPERACION_PAGO, CONCEPTO, ID_ORIGEN_CON, TOTAL, IMPORTE, TOTAL_CHEQUE";
    parametros.put("condicionINE",condicionINE);
    parametros.put("condicionPR",condicionPR);
    parametros.put("condicionSP",condicionSP);
    parametros.put("condicionOFR",condicionOFR);
    parametros.put("campos",campos);
    parametros.put("camposGrupo",camposGrupo);
    parametros.put("camposD",camposD);
    parametros.put("camposGrupoD",camposGrupoD);
    parametros.put("ordenar",ordernarQuery);   

    //cheques = unificaCheque.obtenerChequesUnificados(idOperacion);
       registros = sentencia.registros("chequesNominativos.select.verificaChequesD",parametros);
/*    if (idCuentaBancaria.equals("2") || idCuentaBancaria.equals("6")){
       registros = sentencia.registros("chequesNominativos.select.verificaCheques",parametros);
//    out.println(sentencia.getComando("chequesNominativos.select.verificaCheques",parametros));
}
       else{
       registros = sentencia.registros("chequesNominativos.select.verificaChequesD",parametros);
  //  out.println(sentencia.getComando("chequesNominativos.select.verificaChequesD",parametros));
       } */
    parametros.clear();
    parametros.put("idCuenta",idCuentaBancaria);
    //consecutivo = Integer.valueOf(sentencia.consultar("chequesNominativos.select.consecutivoCheque",parametros).toString());
%>
  <form id="formularioVerifica"  name="formularioVerifica" method="POST">
   
    <br>
    <table width="90%" align="center" class="resultado" border="1" id="chequesNom">
<%
    if(registros!=null){
    %>
    <tr>
    <th class="general">No.</th>
    <th class="general" width="30%">Beneficiario</th>
    <th class="general">Fecha de ministración</th>
    <th class="general">Cuenta bancaria</th>
    <th class="general">Operación pago</th>
    <th class="general" align="center">Total importe</th>
    <th class="general" align="center">Total cheques</th>
    </tr>
    <%for(Vista cheque:registros) {
      numero++;
      //consecutivo++;
      if(numero%2==0){
    %>
      <tr class="resGrisClaro">
   <%}else{%>
      <tr class="">
      <%}%>
    <td width="2%"><%=numero%></td>
    <td width="22%"><%=cheque.getField("BENEFICIARIO")%></td>
    <td align="center" width="10%"><%=cheque.getField("FECHA_PAGO")%></td>
    <td align="center" width="12%"><%=cheque.getField("CUENTA_BANCARIA")%></td>
    <td align="center" width="12%"><%=cheque.getField("OPERACION_PAGO")%></td>
    <td align="right" width="11%"><%=cheque.getField("TOTAL")%></td>
    <td  width="7%" align="center"><%=cheque.getField("TOTAL_CHEQUE")%></td>
    </tr>
    <%}
    }else{
    %>
    <tr><th class="general">No existen resultados que desplegar.</th>
    <%}%>
    </table>
    <input type="hidden" name="ultimoConsecutivoCheque" id="ultimoConsecutivoCheque" value="<%=consecutivo%>">
    <input type="hidden" name="controlPagina" id="controlPagina" value="">
    <input type="hidden" name="idOperacion" id="idOperacion" value="<%=idOperacion%>">
    <input type="hidden" name="idCheque" id="idCheque" value="<%=idCheque%>">
    <input type="hidden" name="hcondicionPR" id="hcondicionPR" value="<%=condicionPR%>">
    <input type="hidden" name="hcondicionSP" id="hcondicionSP" value="<%=condicionSP%>">
    <input type="hidden" name="hcondicionINE" id="hcondicionINE" value="<%=condicionINE%>">
    <input type="hidden" name="hcondicionOFR" id="hcondicionOFR" value="<%=condicionOFR%>">
    <input type="hidden" name="registrosCheques" id="registrosCheques" value="<%=registros%>">
    <input type="hidden" name="cboMes" id="cboMes" value="<%=mes%>">
    <input type="hidden" name="ordenamiento" id="ordenamiento" value="<%=orden%>">
     <input type="hidden" name="lstCuentas" id="lstCuentas" value="<%=idCuentaBancaria%>">
     <input type="hidden" name="tipoFormato" id="tipoFormato" value="<%=tipoFormato%>">
     <input type="hidden" name="pocicion" id="pocicion" value="<%=pocicion%>">
<%
  }
  catch(Exception e){
    e.printStackTrace();
    System.err.println("Error al crear la tabla de verificación cheques nominativos");
  }    
  finally{
    if (conexion != null){
      conexion.close();
      conexion=null;
    }
  }
%>
  <br>
  <hr noshade="noshade" width="40%" align="center" style="border-width: 1px;border-style:solid">
  <br>
   <table align="center" cellpadding="3">
    <thead></thead>
    <tbody>
      <tr>
        <td><input id="btnRegresar" type="button" class="boton" value="Regresar" onclick="regresaVerificar('regresa')"></td>
        <td><input id="imprimir" name="imprimir" type="button" class="boton" value="Generar cheques" onclick="regresaVerificar('imprime')"></td>
        
        
      </tr>
    </tbody>
  </table>
  </form>
  </body>
</html>