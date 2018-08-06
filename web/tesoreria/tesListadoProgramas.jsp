<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.db.dao.DaoFactory"%>
<%@ include file="../Librerias/Funciones/utilscpv.jsp"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="crsPrograma" class="sia.db.sql.SentenciasCRS" scope="page"/>
<html>
<head>
<meta http-equiv="Content-Type"  content="text/html; charset=ISO-8859-1"/>
<title>tesListadoProgramas</title>
<%

    String contexto = request.getContextPath();
%>
<link rel="stylesheet" href="<%=contexto%>/Librerias/Estilos/siaexplorer.css" type="text/css">
<script language="JavaScript"  type="text/javascript">

 function guardaDes(){
   document.getElementById('nomProg').value = document.getElementById('listaProg').options[document.getElementById('listaProg').selectedIndex].text;
 }

 function asignaPagina(opcion){
    document.getElementById('form1').action='<%=contexto%>/tesoreria/tesListadoProgramasControl.jsp';
    document.getElementById('form1').submit();
 }
 
</script>
</head>
  <%
    String[][] titulos = {
        {"tesoreria.actualizarInfo", "Actualizar informacion" },
        {"tesoreria.consultarDif", "Consulta diferencia en saldos"},
        {"tesoreria.validarSaldos", "Validacion de saldos"},
        {"tesoreria.retirarRecursos","Retiro de recursos de cuentas dispersoras"},
        {"tesoreria.adminComisiones","Administrador de comisiones"},
        {"tesoreria.modiCveTrans", "Modificacion de clave de transaccion" },
        {"tesoreria.repMovimientos", "Reportes de movimientos en cuentas" },
        {"tesoreria.comisionesind","Comisiones indebidas"},
        {"tesoreria.claveRastreo","Carga de clave de rastreo"},
        {"tesoreria.bancaBanamex","Transaccionalidad bancaria"},
        {"tesoreria.arrastraSaldos","Arrastre de saldos bancarios"},
        {"tesoreria.saldosReserva","Carga saldos de reserva"},
        {"tesoreria.arrastraSaldosD","Arrastre de saldos diarios - bancas"}
    };
    String titulo = null;
    String tituloPagina = null;
    if (request.getParameter("proceso")!=null || request.getAttribute("proceso")!=null){
        
        titulo = request.getParameter("proceso")!=null?request.getParameter("proceso"):(String)request.getAttribute("proceso");
        for (int i = 0; titulo != null && i < titulos.length; i++) {
            if ( titulo.equals( titulos[i][0] ) )
                titulo = titulos[i][1]; 
        }
        tituloPagina = titulo;
    }
    
   if ((tituloPagina.equals("Actualizar informacion")))
        crsPrograma.addParamVal("filtroProg"," AND ID_TIPO_PROGRAMA in (:param)","6,7,8,10,11,12");     
   else if ( (tituloPagina.equals("Consulta diferencia en saldos")) ||  (tituloPagina.equals("Reportes de movimientos en cuentas")))
            crsPrograma.addParamVal("filtroProg"," AND ID_TIPO_PROGRAMA in (:param)","1,6,7,8,9,10,11,12"); 
        else if (tituloPagina.equals("Validacion de saldos"))
                crsPrograma.addParamVal("filtroProg"," AND ID_TIPO_PROGRAMA in (:param)","1,2,3,4,6,7,8,9,10,11"); 
             else if ((tituloPagina.equals("Carga saldos de reserva")) || (tituloPagina.equals("Arrastre de saldos bancarios")) )
                     crsPrograma.addParamVal("filtroProg"," AND ID_TIPO_PROGRAMA in (:param)","10,7,6,8,11,12");     
                  else if (tituloPagina.equals("Arrastre de saldos diarios - bancas"))
                            crsPrograma.addParamVal("filtroProg"," AND ID_TIPO_PROGRAMA in (:param)","10,7,6,8,11,12");     
                       else if ((tituloPagina.equals("Modificacion de clave de transaccion")))
                                crsPrograma.addParamVal("filtroProg"," AND ID_TIPO_PROGRAMA in (:param)","6,7,8,9");     

    crsPrograma.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.RfTcTipoPrograma.bancas");
  
  %>
  <body onload="guardaDes()">
  <form id="form1" name="form1">
    <%util.tituloPagina(out,"Tesorería","Selección de programa",tituloPagina,true);%>
    <input type="hidden" value="<%=tituloPagina%>" id="opcion" name="opcion">
    <input type="hidden" id="nomProg" name="nomProg">
    <input type="hidden" value="<%=request.getParameter("proceso")!=null?request.getParameter("proceso"):(String)request.getAttribute("proceso")%>" id="proceso" name="proceso">
    <input type="hidden" value="<%=request.getParameter("administrador")!=null?request.getParameter("administrador"):(String)request.getAttribute("administrador")%>" id="administrador" name="administrador">
    <br>
    <table align="center">
      <thead></thead>
      <tbody>
        <tr>
          <td>Seleccione programa:</td>
          <td>
            <select class="cajaTexto" id="listaProg" name="listaProg" onchange="guardaDes()"> 
                <%//CRSComboBox(crsPrograma, out,"ID_TIPO_PROGRAMA","DESCRIPCION","");%>
                <%while(crsPrograma.next()) {%>
                    <option value="<%=crsPrograma.getStr("ID_TIPO_PROGRAMA")%>"><%=crsPrograma.getStr("descripcion")%></option>
                <%}%>
            </select>
          </td>
        </tr>
      </tbody>
    </table>
    <br>
    <hr class="piePagina">
    <br>
    <table align="center">
      <thead></thead>
      <tbody>
        <tr>
          <td><input type="button" class="boton" value="Aceptar" onclick="asignaPagina()"></td>
        </tr>
      </tbody>
    </table>
  </form>
  </body>
</html>