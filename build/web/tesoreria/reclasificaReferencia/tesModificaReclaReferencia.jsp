<%@ page contentType="text/html;charset=ISO-8859-1"%>
<% response.setHeader("Pragma","no-cache");%>
<% response.setHeader("Cache-Control","no-store");%>
<% response.setDateHeader("Expires",-1);%>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ page import="sia.libs.formato.Numero" %>
<%@ page import="sia.libs.formato.Error" %>
<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="crsReferencias" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="bcRfTrMovimientosCuenta" class="sia.rf.tesoreria.registro.movimientos.bcRfTrMovimientosCuenta" scope="page"/>

<html>
  <head>
    <meta http-equiv="Content-Tynope" content="text/html; charset=ISO-8859-1"/>
    <title>tesModificaReclaReferencia</title>
    <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type="text/css">
      <script language="JavaScript"  type="text/javascript">
      
       function validarDatos() {
        referenciaOK =  document.form1.referenciaV.value;
        var mensaje = "------- Alerta -------\n";
        var correcto = true;
        if (referenciaOK.value == ""){
          mensaje = mensaje + "Debe capturar la referencia correcta \n";
          correcto = false;
        }
        if (!correcto){
          alert(mensaje);
        }
        return correcto;
      }
      
      
      function filtrar(obj,lista) {
      sel = document.getElementById(lista);
        for (i=0; opt = sel.options[i]; i++) {
          txt = opt.text;
          opt.style.display = (txt.indexOf(obj.value) == 0) ? 'block' : 'none';
        }
      // seleccionar primer item visible
        for (i=0; opt = sel.options[i]; i++) 
          if (opt.style.display=='block') {
            sel.selectedIndex=i;
            break;
          }
      } 
      
      function asignaPag(){
         document.form1.action="tesListadoMovReclaReferencia.jsp";
         document.form1.submit();
      }
      
    </script>
  </head>
  <%
    String[] clave = null;
    Connection conn = null;
    try  {
      conn = DaoFactory.getTesoreria();
      clave = request.getParameter("clave").split(",");
      bcRfTrMovimientosCuenta.setIdCuenta(clave[0]);
      bcRfTrMovimientosCuenta.setIdMovimiento(clave[1]);
      bcRfTrMovimientosCuenta.select(conn);
    } catch (Exception ex)  {
      Error.mensaje(ex,"Tesoreria");
    } finally  {
      DaoFactory.closeConnection(conn);
      conn = null;
    }
   
  %>
  <body>
   <form id="form1" name="form1" action="tesModReclaReferenciaControl.jsp"  method="POST">
        <%util.tituloPagina(out,"Tesorería","Reclasificación de referencia bancaria ","Modifica",true);%>
        <br>
        <br>
        <input type="hidden" value="<%=request.getParameter("pagina")%>" name="pagina" id="pagina"/>
        <input type="hidden" value="<%=request.getParameter("cuentas")%>" name="cuentas" id="cuentas" />
        <input type="hidden" value="<%=request.getParameter("fechaInicio")%>" name="fechaInicio" id="fechaInicio" />
        <input type="hidden" value="<%=request.getParameter("fechaFinal")%>" name="fechaFinal" id="fechaFinal" />
        <input type="hidden" value="<%=bcRfTrMovimientosCuenta.getIdMovimiento()%>" name="idMovimientoCta" id="idMovimientoCta" />
        <input type="hidden" value="<%=bcRfTrMovimientosCuenta.getIdCuenta()%>" name="idCuentaSelec" id="idCuentaSelec" />
        <table align="center" cellpadding="6">
          <thead></thead>
            <tbody>
              <tr>
                <td>Cuenta bancaria: </td>
                <td><%=clave[2]%></td>
              </tr>
              <tr>
                <td>Fecha:</td>
                <td><%=bcRfTrMovimientosCuenta.getFechaHora()%></td>
              </tr>
              <tr>
                <td>Monto:</td>
                <td><%=Numero.formatear(Numero.NUMERO_MILES_CON_DECIMALES,Double.parseDouble(bcRfTrMovimientosCuenta.getMonto()))%></tr>
              <tr>
                <td>Clave de transacción:</td>
                <td><%=bcRfTrMovimientosCuenta.getClaveTransRecla()%></td>
              </tr>
              <tr>
                <td>Descripción:</td>
                <td><%=bcRfTrMovimientosCuenta.getDescripcion()%></td>
              </tr>
              <tr>
                <td>Referencia original</td>  
                <td ><%=bcRfTrMovimientosCuenta.getReferenciaAnt()%></td>
              </tr>
              <tr valign="top">
                <td>Reclasificación de referencia</td>  
                <td>
                  <div id="referenciasValidasCaja">
                    <input type="text"  onkeyup=filtrar(this,'catReferencias'); size="13" name="referenciaV" id="referenciaV" class="cajaTexto" >  
                  </div>
                  <%
                    crsReferencias.addParam("idCuenta",request.getParameter("cuentas"));
                    crsReferencias.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.RfTrRefenciasIxv.reclaReferencia");
                    crsReferencias.liberaParametros(); 
                  %>
                  <div id="transferenciaCuenta">
                    <select class="cajaTexto" id="catReferencias" name="catReferencias" size="5" onclick="form1.referenciaV.value=form1.catReferencias.options[this.selectedIndex].text"> 
                      <option value=''>- Seleccione -</option>
                      <%CRSComboBox(crsReferencias, out,"idcuenta","REFERENCIA","");%>
                    </select>  
                  </div>
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
          <td><input type="submit" class="boton" value="Actualizar" onclick="return validarDatos()"></td>
          <td><input type="button" class="boton" value="Cancelar" onclick="asignaPag()"></td>
        </tr>
      </tbody>
    </table>
    </form>
  </body>
</html>