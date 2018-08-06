<%@ page contentType="text/html; charset=ISO-8859-1"%>
<%@ page import="sia.db.dao.*,sia.db.sql.Sentencias"%>
<%@ page import="java.util.*,java.sql.*"%>
<%@ page import="sia.rf.contabilidad.registroContable.acciones.ControlRegistro"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>  
<%
  ControlRegistro controlReg = (ControlRegistro)request.getSession().getAttribute("controlRegistro"); 
  String unidad = controlReg.getUnidad();
  String ambito = String.valueOf(controlReg.getAmbito());
  String entidad = String.valueOf(controlReg.getEntidad());
  Sentencias sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD,Sentencias.XML);
  Map parametros = new HashMap();
  Connection conexion=null;
  conexion=DaoFactory.getContabilidad();
  //out.println("conexion = " + conexion + "*");
%>
<jsp:useBean id="abCuentaBancaria" class="sun.jdbc.rowset.CachedRowSet" scope="page">
   <%
      abCuentaBancaria.setTableName("rf_tr_cuentas_bancarias");
      parametros.put("unidad", unidad);
      parametros.put("entidad", entidad);
      parametros.put("ambito", ambito);
//      String query = "select id_cuenta, num_cuenta, nombre_cta from rf_tesoreria_ift.rf_tr_cuentas_bancarias";       
  //    abCuentaBancaria.setCommand(query);
      abCuentaBancaria.setCommand(sentencia.getComando("chequesNominativos.select.cuentasBancarias", parametros));
           //out.println("***" + sentencia.getComando("chequesNominativos.select.cuentasBancarias", parametros).toString() + "***");
      abCuentaBancaria.execute(conexion);
      parametros.clear();
       if (conexion != null){
       conexion.close();
       conexion=null;
     }  
     parametros = null;
     sentencia = null;
   %>
</jsp:useBean> 
<html>
  <head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>c746Filtro</title>
    <link rel="stylesheet" href="../../../../Librerias/Estilos/siaexplorer.css" type="text/css">
    <script language="JavaScript" src="../../../../Librerias/Javascript/refrescarCapa.js" type="text/javascript"></script>
    <script language="JavaScript" src="../../../../Librerias/Javascript/Capturador/funciones.js" type="text/javascript"></script>
    <script language="JavaScript" type='text/JavaScript'>
    function cargaCapa(){
      loadSource('resultado',null,'capaResultado.jsp?','mes=1');
    }
    
    function desactivar(){
      document.getElementById('btnAceptar').disabled='true';
      document.getElementById('btnRegresar').disabled='true';
      document.getElementById('formulario').submit();
    }
    
    function activarPociciones(tipoFormato){
      if(tipoFormato=='central'){
        document.getElementById('pocicionCheque').style.display  = "none";
        document.getElementById('hTipoFormato').value = tipoFormato;
      }
      else
       document.getElementById('pocicionCheque').style.display  = "";
    }
    
    function validaRadio(){
      encontroFormato=false;
      encontroPocicion=false;
      mensaje= '------------------ ALERTA ------------------\n';
      x=0;
      while (x < document.getElementById('formulario').elements.length && (!encontroFormato || !encontroPocicion)){
        Objeto= document.getElementById('formulario').elements[x];
        if(Objeto.type=='radio') {
          if(Objeto.checked && Objeto.name=='tipoFormato')
            encontroFormato= true;
          if(Objeto.checked && Objeto.name=='pocicion')
            encontroPocicion= true;
        } 
        x++;
      }
      if(document.getElementById('hTipoFormato').value == 'central')
        encontroPocicion= true;
      if(!encontroFormato)
        mensaje+='Selecione el tipo de formarto del cheque.\n';
      if(!encontroPocicion)
         mensaje+='Selecione la pocición en que requiere se imprima el cheque.\n';
      if(!encontroFormato || !encontroPocicion)
        alert(mensaje);
      return (encontroFormato && encontroPocicion);
    }// end validarCheckBox()*/
    </script>
        
  </head>
  <body>
  <jsp:scriptlet>util.tituloPagina(out, "Contabilidad", " ", "Filtro", "Cheques", true);</jsp:scriptlet>      
  <form id="formulario" method="POST" action="c746Resultado.jsp">

  <table border="0" align="center">
<thead>
 <tr>
      <td>Cuenta bancaria:</td>
      <td><select name='lstCuentas'  tabindex='9' class= 'cajaTexto'>
<!--      <td><select name='lstCuentas'  tabindex='9' class= 'cajaTexto' onchange='alert(this[selectedIndex].innerText)'>-->
<%
     abCuentaBancaria.beforeFirst();
     int contador=1;
     String valor="";
     while (abCuentaBancaria.next()) {  
%>        
           <option value=<%=abCuentaBancaria.getString("id_cuenta")%>><%=abCuentaBancaria.getString("num_cuenta")%> - <%=abCuentaBancaria.getString("nombre_ctac")%></option>
<%   } %>
        </select></td>
    </tr>
<input type="hidden" id="cuenta_id" value = { document.getElementById('lstCuentas')[selectedIndex].innerText }>
<tr>
<td align="right" class='negrita'>Mes:</td>
<td>		
<select name="cboMes" class="cajaTexto" id="cboMes">
<option value="01">Enero</option>
<option value="02">Febrero</option>
<option value="03">Marzo</option>
<option value="04">Abril</option>
<option value="05">Mayo</option>
<option value="06">Junio</option>
<option value="07">Julio</option>
<option value="08">Agosto</option>
<option value="09">Septiembre</option>
<option value="10">Octubre</option>
<option value="11">Noviembre</option>
<option value="12">Diciembre</option>
</select>
 </td>
</tr>
</thead>
</table>
</br>
<table width="40%" align="center">
 <tr class="azulCla"><td>Tipo de ordenamiento</td></tr>
</table>
<table border="0" width="30%" class='sianoborder' align="center">
    <tr><td><input type="radio" name="ordenamiento" value="1" checked="checked"/>Beneficiario</td></tr>
    <tr><td><input type="radio" name="ordenamiento" value="2"/>Fecha de ministración</td></tr>
    <tr><td><input type="radio" name="ordenamiento"  value="3"/>Operación de pago</td></tr>
    <tr><td><input type="radio" name="ordenamiento"  value="4"/>Origen de pago</td></tr>
  </table><br>

</br>
<table width="40%" align="center">
 <tr class="azulCla"><td>Formato de cheque</td></tr>
</table>
<table border="0" width="30%" class='sianoborder' align="center">
    <tr><td><input type="radio" name="tipoFormato" value="central" onclick="activarPociciones(this.value)"/>Formato centralizado (1 cheque por página con su póliza)</td></tr>
    <!-- <tr><td><input type="radio" name="tipoFormato" value="unidad" onclick="activarPociciones(this.value)"/>Formato de unidades (3 cheques en una página)</td></tr> -->
  </table><br>
<div id='pocicionCheque' style="display:none">
<table width="40%" align="center">
 <tr class="azulCla"><td>Posición cheque</td></tr>
</table>
<table border="0" width="30%" class='sianoborder' align="center">
    <tr><td><input type="radio" name="pocicion" value="1"/>Posición 1</td></tr>
    <tr><td><input type="radio" name="pocicion" value="2"/>Posición 2</td></tr>
    <tr><td><input type="radio" name="pocicion" value="3"/>Posición 3</td></tr>
  </table><br>
  </div>
  <input type="hidden" id="hTipoFormato" value="">
  <hr noshade="noshade" width="40%" align="center" style="border-width: 1px;border-style:solid">
  <br>
  <table cellpadding="5"  border="0" width="15%" class="sianoborder" align="center">
    <tr align="center">
      <td><input type="button" class="boton" id="btnAceptar" value="Aceptar" onclick="if(validaRadio())desactivar();"/></td>
      <td> <input type='button' name='Regresar' id="btnRegresar" value='Regresar' class='boton' onClick="javascript:LlamaPagina('../../filtroGeneral.jsp','');" ></td>
    </tr>
  </table>
  <br>
   <div id='resultado'></div>
    <IFrame style="display:none" name = "bufferCurp" id='bufferCurp'>
    </IFrame>
    <input type="hidden" name="opcion" id="opcion" value="<%=request.getParameter("opcion")%>">
  </form>
  </body>
</html>