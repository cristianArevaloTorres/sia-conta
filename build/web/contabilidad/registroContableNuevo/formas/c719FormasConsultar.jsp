<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page  import="java.util.*,java.sql.*"%>
<%@ page  import="sia.db.dao.*"%>
<%@ page  import="sia.libs.formato.Fecha"%>
<%@ page import="java.sql.Connection" %>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ page import="java.util.*" %>
<%@ page import="sia.rf.contabilidad.registroContableNuevo.formas.*" %>


<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/> 
<jsp:useBean id="controlRegistro" class="sia.rf.contabilidad.registroContable.acciones.ControlRegistro" scope="session" />
<jsp:useBean id="formaContable" class="sia.rf.contabilidad.registroContableNuevo.formas.bcRfTcFormasContables" scope="page"/>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>c719FormasConsultar</title>
        <script type="text/javascript" src="../../../Librerias/Javascript/jquery.js"></script>
        <link rel="stylesheet" href="../../../Librerias/Estilos/siaexplorer.css" type="text/css">
        <script language="JavaScript" src="../../../Librerias/Javascript/Capturador/funciones.js" type="text/javascript"></script>
        
        
        <script language="javascript" type="text/javascript">
         function FiltroGeneral() {
       formulario.action="../filtroGeneral.jsp?opcion=irCapturaForma&idCatalogoCuenta=1";
       formulario.submit();
       //window.open('../../registroContable/filtroGenerarRegistroContableHB.jspx?pagina=irCapturaForma','_self');
      }
        </script>
        
        <%
        Connection con = null;
        try  {
          con = DaoFactory.getConnection(DaoFactory.CONEXION_CONTABILIDAD);
          formaContable.setFormaContableId(request.getParameter("key"));
          formaContable.select(con);        
        } catch (Exception ex)  {
          ex.printStackTrace();
        } finally  {
        }
        
          
        %>
  </head>
  <body onLoad="">
    <%util.tituloPagina(out,"Contabilidad","Consulta de formas contables","Consulta",true);%>
    <form method="POST" action="c713FormasControl.jsp" id="formulario" >
    
      <IFrame style="display:none" name = "bufferFrame">
      </IFrame>
      <input type="hidden" name="accion" value="1"/>
      <input type="hidden" name="idCatalogoCuenta" value="<%=controlRegistro.getIdCatalogoCuenta()%>"/>
      <input type="hidden" name="hLsEjercicio" value="<%=controlRegistro.getEjercicio()%>"/>
      <input type="hidden" name="unidadEjecutora" value="<%=controlRegistro.getUnidad()%>"/>
      <input type="hidden" name="entidad" value="<%=controlRegistro.getEntidad()%>"/>
      <input type="hidden" name="ambito" value="<%=controlRegistro.getAmbito()%>"/>
      <input type="hidden" name="registro" value="<%=Fecha.formatear(Fecha.FECHA_CORTA,Fecha.getRegistro())%>"/>
      <input type="hidden" name="esmanual" value="1"/>
      <input type="hidden" name="registros" value=""/>
      
      <table align="center" width="50%" class="general">
        <thead></thead>
        <tr>
         <td class="azulObs">Unidad ejecutora</td>
         <td class="azulObs">Entidad</td>
         <td class="azulObs">Ambito</td>
        </tr>
        <tr>
         <td align="center"><%=controlRegistro.getUnidad()%></td>
         <td align="center"><%=controlRegistro.getEntidad()%></td>
         <td align="center"><%=controlRegistro.getAmbito()%></td>
        </tr>
      </table>
      <br>
      <hr class="piePagina">
      <br>
      <%
        int[] tam = {5,4,4,4,4,4,4,4};
        
      %>
      <table border="0" cellpadding="5">
        <thead></thead>
        <tr>
          <td>Forma:</td>
          <td><input type="text" id="forma" name="forma" size="3" maxlength="2" class="cajaTexto" value="<%=formaContable.getForma()%>" disabled="disabled"/></td>
          <td rowspan="3" valign="top">Concepto:</td>
          <td rowspan="3"><textarea cols="40" rows="5" id="concepto" name="concepto" class="cajaTexto" disabled="disabled"><%=formaContable.getConcepto()%></textarea></td>
            
          
        </tr>
        <tr>
          <td>Documento fuente:</td>
          <td><input type="text" id="documentoFuente" name="documentoFuente" size="50" maxlength="250" class="cajaTexto"  value="<%=formaContable.getDocumentoFuente()%>" disabled="disabled"/></td>
        </tr>
        <tr>
          <td>Tipo poliza:</td>
          <td>
            <select class="cajaTexto" id="tipoPolizaId" name="tipoPolizaId" disabled="disabled"> 
              <option value="1" <%=formaContable.getTipoPolizaId().equals("1")?"selected":""%>>Diario</option>
              <option value="2" <%=formaContable.getTipoPolizaId().equals("2")?"selected":""%>>Cheques</option>
              <option value="3" <%=formaContable.getTipoPolizaId().equals("3")?"selected":""%>>Egreso</option>
              <option value="4" <%=formaContable.getTipoPolizaId().equals("4")?"selected":""%>>Ingreso</option>
              
            </select>
          </td>
        </tr>
      </table>
      <br>
      <table id='TablaPrueba' border="1" class="general" width="100%">
            <thead class='tabla01'>
             <tr>
                <td class="azulObs" width="4%"><strong>Cuenta</strong></td>
                <td class="azulObs" width="4%" align="center"><strong>SUB</strong></td>
                <td class="azulObs" width="4%" align="center" ><strong>S-Sub<br></strong></td>
                <td class="azulObs" width="4%" align="center" ><strong>S-S-Sub<br></strong></td>
                <td class="azulObs" width="4%" align="center" ><strong>S-S-S-Sub<br></strong></td>
                <td class="azulObs" width="4%" align="center" ><strong>S-S-S-S-Sub<br></strong></td>
                <td class="azulObs" width="4%" align="center" ><strong>S-S-S-S-S-Sub<br></strong></td>
                <%
          if (controlRegistro.getIdCatalogoCuenta() == 3){
                %>
                <td class="azulObs" width="4%" align="center" ><strong>S-S-S-S-S-S-Sub<br></strong></td>
        <%} %>        
                <td class="azulObs" width="10%" align="center" ><strong>Importe</strong></td>
                <td class="azulObs" width="10%" align="center"><strong>Referencia</strong></td>
                <td class="azulObs" width="10%" align="center"><strong>Debe / Haber</strong></td>
                
             </tr>
            </thead>
        <%
          try {
            int maxSubCuentas = controlRegistro.getIdCatalogoCuenta() == 3 ? 10 : 9;
            List<Integer> valores = new ArrayList();
            valores.add(1);
            valores.add(2);
            valores.add(3);
            valores.add(4);
            valores.add(5);
            valores.add(6);
            valores.add(7);   
            if (controlRegistro.getIdCatalogoCuenta() == 3) {
              valores.add(8);
            }
            valores.add(98);
            valores.add(99);
            List<bcRfTcSecuenciaFormaPojo> secs = formaContable.getDetSec();
            int numSec = 0;
            for(bcRfTcSecuenciaFormaPojo sec : secs) {
            %>
              <tr>
            <%
              List<bcRfTcConfiguraFormaPojo> cfgs = sec.getDetConf();
              String valCuenta = ""; 
              String signo = null;
              boolean band = false;
              int x = 0;
              for(bcRfTcConfiguraFormaPojo cfg : cfgs) {
                band = false;
                while(cfg == null || (valores.get(x) != Integer.parseInt( cfg.getNivel() ) )) {
                  band = true;
                  x++;
                  %><td></td><%
                }
                //x = band ? x : x + 1; 
                x++;
                valCuenta = cfg != null && cfg.getCodigo() != null ? cfg.getCodigo() : "";
                signo = cfg != null && cfg.getSigno() != null && signo == null ? cfg.getSigno():signo;
            %>
                
                <td align="center">
                  <input type="text" class="cajaTexto" name="cta<%=cfg.getNivel()%>" size="<%=cfg.getNivel().equals("98")?15:10%>" maxlength="" disabled value="<%=valCuenta%>"/> 
                </td>
            <%}%>
                <td align="center"> 
                  <input type="radio" name="chbTipoOper<%=numSec%>" disabled value="0" <%=sec.getOperacionContableId().equals("0")?"checked":""%>/> 
                  <input type="radio" name="chbTipoOper<%=numSec%>" disabled value="1" <%=sec.getOperacionContableId().equals("1")?"checked":""%>/>
                </td> 
                
              </tr>
          <%
            numSec++;
            }
          } catch(Exception e) {
            out.print(e.getMessage()); 
          }
        %>
          </table>
        <!--</div>-->
     
      
      <hr class="piePagina">
      <table cellpadding="5" align="center">
        <thead></thead>
        <tr>
          <td>
            <input type="button" class="boton" value="Regresar" onclick="FiltroGeneral()"/>
          </td>
        </tr>
      </table>
    </form>
  </body>
</html>