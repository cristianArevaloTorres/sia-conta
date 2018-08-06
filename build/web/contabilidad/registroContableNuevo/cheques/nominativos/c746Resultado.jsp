<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page  import="sia.db.dao.*"%>
<%@ page  import="java.util.*,java.sql.*"%>
<%@ page import="sia.rf.contabilidad.registroContable.acciones.ControlRegistro"%>
<%@ page import="sia.db.sql.Sentencias,sia.db.dao.DaoFactory,sia.db.sql.Vista"%>
<%@ page import="sia.rf.contabilidad.registroContable.acciones.ControlRegistro"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>  
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>c746Resultado</title>
    <link rel="stylesheet" href="../../../../Librerias/Estilos/siaexplorer.css" type="text/css">
    <script language="JavaScript" src="../../../../Librerias/Javascript/Capturador/funciones.js" type="text/javascript"></script>
    <script language="JavaScript" src="../../../../Librerias/Javascript/tabla.js" type="text/javascript"></script>
    <script type="text/javascript" language="javascript">
    
    function verificarSeleccion() {
        document.getElementById("btnVerificar").disabled="true";
        document.getElementById("btnRegresar").disabled="true";
        document.formulario.action="../../../registroContableNuevo/cheques/nominativos/c746VerificarResultado.jsp";
        document.getElementById("formulario").submit();
    }
      
    function cerrar() {
        window.open('','_parent','');
        window.close();
    }
      
    function irPagina(){
      document.getElementById("formulario").action="c746Control.jsp";
      document.getElementById("formulario").submit();
    }
      
    function validar(){
      for(i=0; ele=document.formulario.elements[i]; i++) {
        if (ele.type=='checkbox') {
          if (ele.checked){ 
            regresa=true; 
            break;
           } else{ regresa=false;}
        }   
      }
        if(regresa)
          verificarSeleccion();
        else  
          alert('Seleccionar al menos un cheque');
    }//function validar()
 
</script>
  </head>
  <body id="bResultado">
    <jsp:scriptlet>util.tituloPagina(out, "Contabilidad", " ", "Resultado", "Cheques Nominativos", true);</jsp:scriptlet>   
    <!--La propiedad target="blank" es para abrir una página emergente-->
  <form id="formulario" name="formulario" method="POST">
  <%
  ControlRegistro controlReg = (ControlRegistro)request.getSession().getAttribute("controlRegistro"); 
  Connection conexion = null;
  Sentencias sentencia = null;
  List<Vista> registros = null;
  List<Vista> registrosCheque = null;
  int consecutivoSigCheque = -1;
  Map parametros = null;
  int numero=0;
  String mes = request.getParameter("cboMes");
  String idCuentaBancaria = request.getParameter("lstCuentas");
  int ordenamiento = request.getParameter("ordenamiento")!=null?Integer.valueOf(request.getParameter("ordenamiento")):0;
  String tipoFormato = request.getParameter("tipoFormato");
  String pocicion = request.getParameter("pocicion");
  String ordernarQuery = "order by ";
  try{
    switch(ordenamiento){
      case 1: ordernarQuery = ordernarQuery.concat("beneficiario");break;
      case 2: ordernarQuery = ordernarQuery.concat("fecha_pago");break;
      case 3: ordernarQuery = ordernarQuery.concat("operacion_pago");break;
      case 4: ordernarQuery = ordernarQuery.concat("origen_operacion");break;
      //default: ordernarQuery = ordernarQuery.concat("beneficiario");break;
    }
    conexion = DaoFactory.getContabilidad();
    sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD,Sentencias.XML);
    parametros = new HashMap();
    registros = new ArrayList<Vista>();
    //parametros.put("unidad",controlReg.getUnidad());
    parametros.put("mes",mes);
    parametros.put("idCuenta",idCuentaBancaria);
    parametros.put("ejercicio",controlReg.getEjercicio());   
    parametros.put("ordenar",ordernarQuery);   
//    out.println("id_cuenta_bancaria: " + idCuentaBancaria);
    registros = sentencia.registros("chequesNominativos.select.imprimirCheques",parametros);
    //out.println(sentencia.getComando("chequesNominativos.select.imprimirCheques",parametros));
    parametros.put("unidad", controlReg.getUnidad());
    parametros.put("ambito", controlReg.getAmbito());
    parametros.put("entidad", controlReg.getEntidad());
//    out.println(parametros);
    registrosCheque = sentencia.registros("reportes.select.consecutivoSigCheque", parametros);
//    out.println(sentencia.getComando("reportes.select.consecutivoSigCheque",parametros));
    for(Vista registroCheque:registrosCheque ){
      consecutivoSigCheque = registroCheque.getInt("ULTIMO_CONSECUTIVO");
    }
    registrosCheque = null;
    %>
     <br>
    <table width="100%">
    <tr><td  class="negrita"><font color="Red">ADVERTENCIA. El folio consecutivo puede variar si otros usuarios se encuentran afectando la misma chequera de manera simultánea.</font></td></tr>
    </table>
    <br>
    <table width="100%">
    <tr><td width="3%" class="negrita">Folio: </td>
       <td ><INPUT TYPE="text" NAME="txtNoCheque" SIZE='12' readonly="readonly" class='cajaTexto' value='<%=consecutivoSigCheque%>'> </td>
       </tr>
    </table>
    <br>
    <table width="100%" align="center" class="resultado" border="1" id="chequesNom">
<%
    if(registros !=null){
    %>
    
    <tr>
    <th class="general"><input type="checkbox"  onclick="selTodo(this,'cheque','chequesNom')"></th>
    <th class="general">No.</th>
    <th class="general" width="10%">Beneficiario</th>
    <th class="general">Importe</th>
    <th class="general">Fecha de ministración</th>
    <th class="general">Operación de pago</th>
    <th class="general">Tipo de operación</th>
    <th class="general">Origen de operación</th>
    <th class="general">Cuenta bancaria</th>
    </tr>
    <%for (Vista record : registros) {
      numero++;
      if(numero%2==0){
    %>
    <%  %>
      <tr class="resGrisClaro">
   <%
      }else{  %>
      <tr class="">
      <%  }%>
    <td><input type="checkbox" value="<%=record.getField("TIPO_REGISTRO")%>,<%=record.getField("ID_OPERACION")%>,<%=record.getField("ID_CHEQUE_NOMINATIVO")%>" id="cheque" name="cheque"></td>
    <td><%=numero%></td>
    <td width="22%"><%=record.getField("BENEFICIARIO")%></td>
    <td align="right"><%=record.getField("IMPORTE")%></td>
    <td align="center" width="8%"><%=record.getField("FECHA_PAGO")%></td>
    <td align="center" width="11%"><%=record.getField("OPERACION_PAGO")%></td>
    <td  width="15%"><%=record.getField("TIPO_OPERACION")%></td>
    <td  width="15%"><%=record.getField("ORIGEN_OPERACION")%></td>
    <td align="center" width="9%"><%=record.getField("CUENTA_BANCARIA")%></td>
    </tr>
    <input type="hidden" name="idCuenta" id="idCuenta" value="<%=record.getField("ID_CUENTA")%>">
    <%
        }
    }else{
    %>
    <tr><th class="general">No existen resultados que desplegar.</th>
    <%}%>
    </table>
    <input type="hidden" name="mes" id="mes" value="<%=mes%>">
    <input type="hidden" name="ctaBancaria" id="ctaBancaria" value="<%=idCuentaBancaria%>">
    <input type="hidden" name="orden" id="orden" value="<%=ordenamiento%>">
    <input type="hidden" name="tipoFormato" id="tipoFormato" value="<%=tipoFormato%>">
    <input type="hidden" name="pocicion" id="pocicion" value="<%=pocicion%>">
    <%
  }
  catch(Exception e){
    System.err.println("Error al crear la tabla de resultado de cheques nominativos");
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
  <table cellpadding="5"  border="0" width="15%" class="sianoborder" align="center">
    <tr align="center" >
      <td><input type="button" name='btnVerificar' id='btnVerificar' class="boton" value="Verificar" onclick="validar();"/></td>
      <td> <input type='button' name='btnRegresar' id='btnRegresar' value='Regresar' class='boton' onClick="javascript:LlamaPagina('c746Filtro.jsp','');" ></td>
    </tr>
  </table>
    <input type="hidden" name="opcion" id="opcion" value="<%=request.getParameter("opcion")%>">
  </form>
  </body>
</html>