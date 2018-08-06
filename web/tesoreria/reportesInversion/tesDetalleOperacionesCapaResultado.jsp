<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ page import="sia.libs.fun.tabla.*" %>
<jsp:useBean id="pbDocumentos" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="pag" class="sia.libs.fun.tabla.PaginacionSE" scope="page"/>
<jsp:useBean id="crs" class="sia.db.sql.SenCRSR" scope="page"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesDetalleOperacionesCapaResultado</title>
  </head>
    <%
      /// onload="parent.loadSourceFinish('dResultado')"
      String fechaInicio           =  request.getParameter("fechaIni");
      String fechaFinal            =  request.getParameter("fechaFin");

      String idCuentaInversion     =  request.getParameter("idCuentaInversion");
      boolean mostrar = false;
      mostrar = request.getParameter("proceso").equals("1")?false:true;
      
      try  {
      
      if (!idCuentaInversion.equals(""))
        crs.addParam("idCuentaInversion","and id_cuenta_inversion=".concat(idCuentaInversion));
      crs.addParam("periodo","and (trunc(fecha) >= to_date('".concat(fechaInicio).concat("','dd/mm/yyyy') and  trunc(fecha) <= to_date('").concat(fechaFinal).concat("','dd/mm/yyyy'))") );
      
      crs.registrosMap(DaoFactory.CONEXION_TESORERIA,"consultas.select.detalleOperaciones.reportesInversiones");
      
      crs.maxRegistros = 1000;
   /*
   pag.addCampo(new Campo("fecha","Fecha",Campo.AL_CEN,null));
      pag.addCampo(new Campo("plazo","Plazo",Campo.AL_CEN,null));
      pag.addCampo(new Campo("orden","Orden",Campo.AL_IZQ,null));
      pag.addCampo(new Campo("descripcion","Descripción",Campo.AL_IZQ,null));
      pag.addCampo(new Campo("tipoinv","Tipo de inversión",Campo.AL_IZQ,null));
      pag.addCampo(new Campo("emision","Emisión",Campo.AL_IZQ,null));
      pag.addCampo(new Campo("titulos","Títulos",Campo.AL_DER,Campo.FORMATO_DOS_DEC));
      pag.addCampo(new Campo("importe","Importe",Campo.AL_DER,Campo.FORMATO_DOS_DEC));
      pag.addCampo(new Campo("tasa","Tasa",Campo.AL_DER,Campo.FORMATO_DOS_DEC));
      pag.addCampo(new Campo("saldo_anterior","Saldo anterior",Campo.AL_DER,Campo.FORMATO_DOS_DEC));
      pag.addCampo(new Campo("saldo_actual","Saldo actual",Campo.AL_DER,Campo.FORMATO_DOS_DEC));
      pag.addCampo(new Campo("saldo_real","Saldo real",Campo.AL_DER,Campo.FORMATO_DOS_DEC));
      
      int param = request.getParameter("param") != null ? Integer.parseInt(request.getParameter("param")) : 0;
      
      pag.seleccionarPagina(pbDocumentos, 
                                 out, 15, 
                                 param, "../../", 
                                 "ID_CUENTA_INVERSION",null,"Listado de operaciones para la cuenta seleccionada");  
*/
      } catch(Exception e) {
        
      }
    %>
  <body onload="parent.loadSourceFinish('dResultado');parent.crearPaginacion('tbResultado','trPagResultado',15);">
   <table align='center' cellspacing="0" width="98%">
      <thead></thead>
      <tbody>
        <tr>
          <td><%crs.limite(out);%></td>
        </tr>
      </tbody>
    </table><br>
    <table align='center' class='general' cellspacing="0" cellpadding="2" id='tbResultado' width="98%">
      <thead><tr><td colspan="13" class="azulCla" height="20px">Listado de operaciones para la cuenta seleccionada</td></tr></thead>
      <thead id='thencabezado'><tr><td colspan="13" id='tdencabezado' class="azulObs"></td></tr></thead>
      <thead>
        <tr>
          <th class='azulObsOrd' width="40px" onclick="fnOrdNum(this,'tbResultado','trPagResultado','tdNumReg')">No.</th>
          <th class='azulObsOrd' onclick="fnOrdTex(this,'tbResultado','trPagResultado','tdFecha')">Fecha</th>
          <th class='azulObsOrd' onclick="fnOrdTex(this,'tbResultado','trPagResultado','tdPlazo')">Plazo</th>
          <th class='azulObsOrd' onclick="fnOrdTex(this,'tbResultado','trPagResultado','tdOrden')">Orden</th>
          <th class='azulObsOrd' onclick="fnOrdTex(this,'tbResultado','trPagResultado','tdDescrip')">Descripción</th>
          <th class='azulObsOrd' onclick="fnOrdTex(this,'tbResultado','trPagResultado','tdTipoInv')">Tipo de inversión</th>
          <th class='azulObsOrd' onclick="fnOrdTex(this,'tbResultado','trPagResultado','tdEmision')">Emisión</th>
          <th class='azulObsOrd' onclick="fnOrdTex(this,'tbResultado','trPagResultado','tdTitulos')">Títulos</th>
          <th class='azulObsOrd' onclick="fnOrdNum(this,'tbResultado','trPagResultado','tdImporte')">Importe</th>
          <th class='azulObsOrd' onclick="fnOrdTex(this,'tbResultado','trPagResultado','tdTasa')">Tasa</th>
          <th class='azulObsOrd' onclick="fnOrdNum(this,'tbResultado','trPagResultado','tdSaldoAnt')">Saldo anterior</th>
          <th class='azulObsOrd' onclick="fnOrdNum(this,'tbResultado','trPagResultado','tdSaldoAct')">Saldo actual</th>
          <th class='azulObsOrd' onclick="fnOrdNum(this,'tbResultado','trPagResultado','tdSaldoReal')">Saldo real</th>
        </tr>
      </thead>
      <%
        int noReg = 0;
        while(crs.next()) {
        %>
      <tbody id='trPagResultado'>
         
        <tr>
          <td align="center" height="30px" id='tdNumReg'><%=++noReg%></td>
          <td id='tdFecha'><%=crs.getStr("fecha")%></td>
          <td id='tdPlazo'><%=crs.getStr("plazo")%></td>
          <td id='tdOrden'><%=crs.getStr("orden")%></td>
          <td id='tdDescrip'><%=crs.getStr("descripcion")%></td>
          <td id='tdTipoInv'><%=crs.getStr("tipoinv")%></td>
          <td id='tdEmision'><%=crs.getStr("emision")%></td>
          <td id='tdTitulos'><%=crs.getStr("titulos")%></td>
          <td id='tdImporte'><%=crs.getStr("importe")%></td>
          <td id='tdTasa'><%=crs.getStr("tasa")%></td>
          <td id='tdSaldoAnt'><%=crs.getStr("saldo_anterior")%></td>
          <td id='tdSaldoAct'><%=crs.getStr("saldo_actual")%></td>
          <td id='tdSaldoReal'><%=crs.getStr("saldo_real")%></td>
        </tr>
        
       
      </tbody>
      
      <% } %>
      <thead id='thpie'><tr><td colspan="13" id='tdpie' class="azulObs"></td></tr></thead>
    </table>
  
  
      <br>
      <hr class="piePagina">
      <br>
      <table align='center'>
        <thead></thead>
        <tbody>
          <tr>
            <td><input type="button" class="boton" value="Generar PDF" onclick="asignaValorRep('pdf')"></td>
            <td><input type="button" class="boton" value="Generar XLS" onclick="asignaValorRep('xls')"></td>
            <td <%=mostrar?"style='display:none'":""%>><input type="button" class="boton" value="Regresar" onclick="regresar()"></td>
          </tr>
        </tbody>
      </table>
  </body>
</html>