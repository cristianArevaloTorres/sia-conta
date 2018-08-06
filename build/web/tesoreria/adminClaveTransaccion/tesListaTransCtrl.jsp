<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.db.dao.DaoFactory"%>
<%@ page import="sia.db.sql.SentennciasSE" %>
<%@ page import="sia.libs.formato.Fecha" %>
<%@ page import="sia.db.sql.Vista" %>
<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/> 
<jsp:useBean id="bcRfTrMovimientosCuenta" class="sia.rf.tesoreria.registro.movimientos.bcRfTrMovimientosCuenta" scope="page"/>
<jsp:useBean id="crsClavesTran" class="sia.db.sql.SentenciasCRS" scope="page"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesListaTransCtrl</title>
     <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type="text/css">
        <script language="javascript" type="text/javascript">
       
        function regresar(){
           document.getElementById('form1').action='tesListaTransacciones.jsp';
           document.getElementById('form1').target='';
           document.getElementById('form1').submit();
       } 
       
       
       function actualizaDtos(){
           document.getElementById('form1').action='tesModificaIdClave.jsp';
           document.getElementById('form1').target='';
           document.getElementById('form1').submit();
       }
    
    </script>
  </head>
  <%!
  
    public String getNumeroCuenta(String idCtaS){
        String regresa = null;
        SentennciasSE sentenciasSE = new SentennciasSE(DaoFactory.CONEXION_TESORERIA);
        Map parametros = new HashMap();
        List<Vista> registros = null;
        try  {
            parametros.put("idCuenta",idCtaS);
            registros = sentenciasSE.registros("criterios.select.obtenerNumCuenta.modClaveTrans",parametros);
            if (registros!=null){
                regresa = registros.get(0).getField("NUM_CUENTA");
            }
        } catch (Exception e)  {
            e.printStackTrace();
            regresa = "''"; 
        } finally  {
            sentenciasSE = null;
            registros = null;
        }
        return regresa; 
    }
  %>
  <%
    String[] clave = null;
    String numCuentaS = null;
 
    try  {
      
        clave = request.getParameter("clave").split(",");
        bcRfTrMovimientosCuenta.setIdMovimiento(clave[0]);
        bcRfTrMovimientosCuenta.setIdCuenta(clave[1]);
        bcRfTrMovimientosCuenta.select(DaoFactory.CONEXION_TESORERIA);
        numCuentaS = getNumeroCuenta(clave[1]);

        crsClavesTran.addParamVal("fechaMov",bcRfTrMovimientosCuenta.getFechaHora());
        crsClavesTran.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.RfTcClavesTransaccion.modClaveTrans");
    } catch (Exception ex)  {
        ex.printStackTrace();
    } finally  {
    }
    
        
  %>
  <body>
  <form id="form1" name="form1" method="POST">
    <%util.tituloPagina(out,"Tesorería","Detalle del movimiento bancario","Edición de clave de transacción",true);%>
    <input type="hidden" id="nomProg" name="nomProg" value="<%=request.getParameter("nomProg")%>">
    <input type="hidden" id="opcion" name="opcion" value="<%=request.getParameter("opcion")%>">
    <input type="hidden" id="proceso" name="proceso" value="<%=request.getParameter("proceso")%>">
    <input type="hidden" id="idProgramaS" name="idProgramaS" value="<%=request.getParameter("idProgramaS")%>">
    <input type="hidden" name="fechaInicio" id="fechaInicio" value="<%=request.getParameter("fechaInicio")%>"/>
    <input type="hidden" name="fechaFinal" id="fechaFinal" value="<%=request.getParameter("fechaFinal")%>"/>
    <input type="hidden" id="ctaSelec" name="ctaSelec" value="<%=request.getParameter("ctaSelec")%>"/>
    <input type="hidden" id="clave" name="clave" value="<%=request.getParameter("clave")%>"/>
    <table>
        <thead></thead>
        <tbody>
          <tr align="left">
            <td style="color:rgb(0,99,148); font-style:italic; font-weight:bold;"><%=request.getParameter("opcion")%> >> <%=request.getParameter("nomProg")%></td>
          </tr>
        </tbody>
      </table>
      <br>
      <br>
      <table width="45%" align="center">
        <thead></thead>
        <tbody>
            <tr class="azulCla">
                <td colspan="3">Datos del movimiento</td>
            </tr>
        </tbody>
      </table>
      <table width="45%" style="background-color:transparent; border-color:rgb(0,132,198); border-style:solid; border-width:2.0px;" align="center" cellpadding="10" >
      <thead></thead>
      <tbody>
        <tr> 
          <td> 
            <table>
            <thead></thead>
            <tbody>
                <tr>
                    <td width="20%"></td>
                    <td width="1%"></td>
                    <td width="24%"></td>
                </tr>
                <tr>
                    <td width="20%" align="right">No. cuenta:</td>
                    <td width="1%"></td>
                    <td width="24%"><%=numCuentaS%></td>
                </tr>
                <tr>
                    <td width="20%" align="right">Fecha:</td>
                    <td width="1%"></td>
                    <td width="24%" align="left"><%=bcRfTrMovimientosCuenta.getFechaHora()%></td>
                </tr>
                <tr>
                    <td width="20%" align="right">Monto:</td>
                    <td width="1%"></td>
                    <td width="24%" align="left"><%=bcRfTrMovimientosCuenta.getMonto()%></td>
                </tr>
                <tr>
                    <td width="20%" align="right">Clave transacción:</td>
                    <td width="1%"></td>
                    <td width="24%" align="left"><%=bcRfTrMovimientosCuenta.getClaveTrans()%></td>
                </tr>
                <tr>
                    <td width="20%" align="right">No. cheque:</td>
                    <td width="1%"></td>
                    <td width="24%" align="left"><%=bcRfTrMovimientosCuenta.getNumeroCheque()%></td>
                </tr>
                <tr>
                    <td width="20%" align="right">Descripción:</td>
                    <td width="1%"></td>
                    <td width="24%" align="left"><%=bcRfTrMovimientosCuenta.getDescripcion()%></td>
                </tr>
                <tr>
                    <td width="20%" align="right">Referencia:</td>
                    <td width="1%"></td>
                    <td width="24%" align="left"><%=bcRfTrMovimientosCuenta.getReferencia()%></td>
                </tr>
                <tr>
                    <td width="20%" align="right">Transacción reclasificada:</td>
                    <td width="1%"></td>
                    <td width="24%" align="left"><%=bcRfTrMovimientosCuenta.getClaveTransRecla()%></td>
                </tr>
                <tr>
                    <td width="20%" align="right">Nueva reclasificación:</td>
                    <td width="1%"></td>
                    <td width="24%">
                        <select class="cajaTexto" id="nuevaClave" name="nuevaClave"> 
                            <option value=''>- Seleccione -</option>
                            <%CRSComboBox(crsClavesTran, out,"ID_CLAVE_TRANS","CLAVE_TRANS,DESCRIPCION","");%>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td width="20%"></td>
                    <td width="1%"></td>
                    <td width="24%"></td>
                </tr>
            </tbody>
            </table>
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
          <td><input type="button" class="boton" value="Guardar" onclick="actualizaDtos()"></td>
          <td><input type="button" class="boton" value="Cancelar" onclick="regresar()"></td>
        </tr>
      </tbody>
    </table>
    </form>
  </body>
</html>