<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ page import="sia.libs.formato.Numero" %>
<%@ page import="sia.libs.formato.Fecha" %>
<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<jsp:useBean id="dtsProceso" class="sia.rf.tesoreria.VariablesSession" scope="session"/>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/> 
<jsp:useBean id="crsDetalleDif" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="crsDetBitacora" class="sia.db.sql.SentenciasCRS" scope="page"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesBitacoraAclaracion</title>
    <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type="text/css">
    <script language="JavaScript" src="../../Librerias/Javascript/Calendario/Calendar/calendar.js" type="text/javascript"></script>
    <script language="javascript" src="../../Librerias/Javascript/crearElementos.js" type="text/javascript"></script>
    <script language="JavaScript" src="../../Librerias/Javascript/refrescarCapa.js" type="text/javascript"></script>
    <script language="JavaScript" src="../../../Librerias/Javascript/tabla.js" type="text/javascript"></script>
    <script language="javascript" type="text/javascript">
    
    function regresa(){
          document.getElementById('form1').action='tesListaDiferencias.jsp';
          document.getElementById('form1').submit();
       }
       
       
    function asignaPagina() {
          document.getElementById('form1').action = 'tesRegistraActividades.jsp';
          document.getElementById('form1').submit();
    }
    
    function irControl(accionSel,idSeguimiento) {
          document.getElementById('form1').action = 'tesBitacoraAclaracionCtrl.jsp?accionSel='+accionSel+'&idSeguimiento='+idSeguimiento;
          document.getElementById('form1').submit();
    }
    
    function modificarReg(idSeguimiento) {
          document.getElementById('form1').action = 'tesBitacoraModificar.jsp?idSeguimiento='+idSeguimiento;
          document.getElementById('form1').submit();
    }
    
    </script>
    <%
    
        boolean estatusCerrar = false;
        boolean estatusReabrir = false;
        boolean registraAct = false;
        boolean existeReg = false;
        String[] clave = null;
        clave = request.getParameter("clave").split(",");
        crsDetalleDif.addParamVal("estatus",request.getParameter("idEstatus"));
        crsDetalleDif.addParamVal("programa",request.getParameter("idProgramaS"));
        crsDetalleDif.addParamVal("idCuenta",clave[0]);
        crsDetalleDif.addParamVal("idSaldoD",clave[1]);
        crsDetalleDif.registrosMap(DaoFactory.CONEXION_TESORERIA,"criterios.select.detalleDiferencia.diferenciaSaldo");
        crsDetalleDif.next();
        
        crsDetBitacora.addParamVal("idCuenta",clave[0]);
        crsDetBitacora.addParamVal("idSaldoDiario",clave[1]);
        crsDetBitacora.addParamVal("idProg",request.getParameter("idProgramaS"));
        crsDetBitacora.registrosMap(DaoFactory.CONEXION_TESORERIA,"criterios.select.obtenSeguimiento.diferenciaSaldo");
        
        if (request.getParameter("idEstatus").equals("2")){
            estatusCerrar=true;
            estatusReabrir=false;
            registraAct = true;
        } else if (request.getParameter("idEstatus").equals("3")){
                    estatusCerrar=false; 
                    estatusReabrir=true;
                    registraAct = false;
                }
                else if (request.getParameter("idEstatus").equals("1")){
                        estatusCerrar=false; 
                        estatusReabrir=false;
                        registraAct = false;
                     }
    %>
  </head>
  <body>
  <form id="form1" name="form1" action="tesListaDiferencias.jsp"  method="POST">
  <%util.tituloPagina(out,"Tesorería","Reporte de registro de saldos con diferencia","Diferencia en saldos",true);%>
  <input type="hidden" id="nomProg" name="nomProg" value="<%=dtsProceso.getNomProg()%>">
  <input type="hidden" id="opcion" name="opcion" value="<%=dtsProceso.getOpcion()%>">
  <input type="hidden" id="idProgramaS" name="idProgramaS" value="<%=request.getParameter("idProgramaS")%>">
  <input type="hidden" id="proceso" name="proceso" value="<%=request.getParameter("proceso")%>">
  <input type="hidden" id="estatusDef" name="estatusDef" value="<%=request.getParameter("idEstatus")%>">
  <input type="hidden" id="estatusDif" name="estatusDif" value="<%=request.getParameter("idEstatus")%>">
  <input type="hidden" id="idEstatus" name="idEstatus" value="<%=request.getParameter("idEstatus")%>">
  <input type="hidden" id="clave" name="clave" value="<%=clave[0].concat(",").concat(clave[1])%>">
  <input type="hidden" id="mensaje" name="mensaje" value="">
  <br>
      <table>
      <thead></thead>
        <tbody>
          <tr align="left">
            <td style="color:rgb(0,99,148); font-style:italic; font-weight:bold;"><%=dtsProceso.getOpcion()%> >> <%=dtsProceso.getNomProg()%></td>
          </tr>
        </tbody>
      </table>
    <br>
   <br>
   <table width="80%" align="center" class="general" cellpadding="3">
   <thead></thead>
        <tbody>
           <tr class="azulCla">
             <td colspan="7">Datos de la cuenta que presenta la diferencia en saldo</td>
           </tr>
           <tr>
             <td width="6%"></td>
             <td width="12%">No. cuenta:</td>
             <td width="19%"><%=crsDetalleDif.getStr("num_cuenta")%></td>
             <td width="6%"></td>
             <td width="12%">Nombre de la cuenta:</td>
             <td width="19%"><%=crsDetalleDif.getStr("nombre_cta")%></td>
             <td width="6%"></td>
           </tr>
           <tr>
             <td width="6%"></td>
             <td width="12%">Fecha de inicio:</td>
             <td width="19%"><%=Fecha.formatear(2,crsDetalleDif.getStr("fechaInicioF"))%></td>
             <td width="6%"></td>
             <td width="12%">Fecha de término:</td>
             <td width="19%"><%=crsDetalleDif.getStr("fecha_fin")==null?"":Fecha.formatear(2,crsDetalleDif.getStr("fechaFinF"))%></td>
             <td width="6%"></td>
           </tr>
           <tr>
             <td width="6%"></td>
             <td width="12%">Saldo INEGI:</td>
             <td width="19%"><%=Numero.formatear(2,Double.parseDouble(crsDetalleDif.getStr("operaciones_calculado")))%></td>
             <td width="6%"></td>
             <td width="12%"></td>
             <td width="19%"></td>
             <td width="6%"></td>
           </tr>
           <tr>
             <td width="6%"></td>
             <td width="12%">Saldo banco:</td>
             <td width="19%"><%=Numero.formatear(2,Double.parseDouble(crsDetalleDif.getStr("operaciones_banco")))%></td>
             <td width="6%"></td>
             <td width="12%">Diferencia:</td>
             <td width="19%"><%=Numero.formatear(2,Double.parseDouble(crsDetalleDif.getStr("diferencia_real")))%></td>
             <td width="6%"></td>
           </tr>
        </tbody>
   </table> 
   <br>
   <table align='center' class='general' id="tbBitacora"  width="80%">
        <thead>
          <tr class="azulCla">
            <td colspan="3">Bitácora de aclaración de diferencia en saldo</td>
          </tr>
          <tr>
            <th class='azulObs'>Fecha</th>
            <th class='azulObs'>Observaciones</th>
            <th class='azulObs'>Operaciones</th>
          </tr>
        </thead>
        <tbody>
          <% try  {
              while(crsDetBitacora.next()) {
                  existeReg = true;
              %>
                <tr>
              <td><%=crsDetBitacora.getStr("SDFecha")%></td>
              <td><%=crsDetBitacora.getStr("OBSERVACIONES")%></td>
              <td align="center">
                   <img src='../../Librerias/Imagenes/botonEliminar2.gif' id='img' name='img' alt="Eliminar documento" onclick="irControl('eliminar','<%=crsDetBitacora.getStr("ID_SEGUIMIENTO")%>')">
                   <img src='../../Librerias/Imagenes/botonEditar2.gif' id='imgM' name='imgM' alt="Modificar documento" onclick="modificarReg('<%=crsDetBitacora.getStr("ID_SEGUIMIENTO")%>')">
                 
              </td>
            </tr> <%
              }
            } catch (Exception ex)  {
              ex.printStackTrace();
            } finally  {
            }
          %>
        </tbody>
   </table>
   <br>
   <hr class="piePagina">
   <br>
    <table align="center">
      <thead></thead>
      <tbody>
        <tr> 
          <td <%=registraAct ?"":"style='display:none'"%>><input type="button" class="boton" value="Registrar aclaración" onclick="asignaPagina()"></td>
          <td <%=estatusCerrar && existeReg?"":"style='display:none'"%>><input type="button" class="boton" value="Cerrar bitácora" onclick="irControl('cerrar','')"></td>
          <td <%=estatusReabrir?"":"style='display:none'"%>><input type="button" class="boton" value="Reabrir bitácora" onclick="irControl('reabrir','')"></td>
          <td><input type="button" class="boton" value="Regresar" onclick="regresa()"></td>
        </tr>
      </tbody>
    </table>
  </form>
  </body>
</html>