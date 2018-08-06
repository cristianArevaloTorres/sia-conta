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
<%@ page import="sia.rf.contabilidad.registroContableNuevo.bcConfiguracionGeneral" %>
<%@ page import="sia.rf.contabilidad.registroContableNuevo.bcConfiguracionRenglon" %>


<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/> 
<jsp:useBean id="controlRegistro" class="sia.rf.contabilidad.registroContable.acciones.ControlRegistro" scope="session" />
<jsp:useBean id="maestroOper" class="sia.rf.contabilidad.registroContableNuevo.formas.bcRfTcMaestroOperaciones" scope="page"/>
<jsp:useBean id="detalle" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="crsConfiguraCta" class="sia.db.sql.SentenciasCRS" scope="page"/>


<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>c714OperacionesTipoConsultar</title>
        <script type="text/javascript" src="../../../Librerias/Javascript/jquery.js"></script>
        <link rel="stylesheet" href="../../../Librerias/Estilos/siaexplorer.css" type="text/css">
        <script language="JavaScript" src="../../../Librerias/Javascript/Capturador/funciones.js" type="text/javascript"></script>
        
        
        <script language="javascript" type="text/javascript">
        </script>
        
        <%
          String consecutivo = request.getParameter("hdconsecutivo") != null ? request.getParameter("hdconsecutivo"): "";
          String descripcion = request.getParameter("hddescripcion") != null ? request.getParameter("hddescripcion"): "";
          String accion = request.getParameter("accion") != null ? request.getParameter("accion"): "4";
          StringBuffer cadenaConfiguracion = new StringBuffer();
          int nivelesConfigura = 8;
          int porcentajeCelda = 60;
        
        try  {
          
          maestroOper.setMaestroOperacionId(request.getParameter("key"));
          maestroOper.select(DaoFactory.CONEXION_CONTABILIDAD);   
          crsConfiguraCta.addParam("ejercicio",controlRegistro.getEjercicio());
          crsConfiguraCta.addParam("idCatalogo",controlRegistro.getIdCatalogoCuenta());
          crsConfiguraCta.registrosMap(DaoFactory.CONEXION_CONTABILIDAD,"formasContables.select.RfTcDetalleConfCve");
          
          while(crsConfiguraCta.next()) {
            if(crsConfiguraCta.isLast())
              cadenaConfiguracion.append(crsConfiguraCta.getString("tamanio"));
            else
              cadenaConfiguracion.append(crsConfiguraCta.getString("tamanio")).append(",");
            
          }
          
          nivelesConfigura = crsConfiguraCta.size();
          porcentajeCelda = 60 / nivelesConfigura;  
        
        
          
        %>
  </head>
  <body onLoad="">
    <%util.tituloPagina(out,"Contabilidad","Consulta de formas contables","Consulta",true);%>
    <form method="POST" action="c714OperacionesTipoResultado.jsp" id="formulario">
    
      <IFrame style="display:none" name = "bufferFrame">
      </IFrame>
      <input type="hidden" name="accion" value="<%=accion%>"/>
      <input type="hidden" name="idCatalogoCuenta" value="<%=controlRegistro.getIdCatalogoCuenta()%>"/>
      <input type="hidden" name="hLsEjercicio" value="<%=controlRegistro.getEjercicio()%>"/>
      <input type="hidden" name="unidadEjecutora" value="<%=controlRegistro.getUnidad()%>"/>
      <input type="hidden" name="entidad" value="<%=controlRegistro.getEntidad()%>"/>
      <input type="hidden" name="ambito" value="<%=controlRegistro.getAmbito()%>"/>
      <input type="hidden" name="registro" value="<%=Fecha.formatear(Fecha.FECHA_CORTA,Fecha.getRegistro())%>"/>
      <input type="hidden" id="configura" name="configura" value="<%=cadenaConfiguracion.toString()%>"/>
      <input type="hidden" id="nivelesConfigura" name="nivelesConfigura" value="<%=nivelesConfigura%>"/>
      <input type="hidden" name="hdconsecutivo" id="hdconsecutivo" value="<%=consecutivo%>">
      <input type="hidden" name="hddescripcion" id="hddescripcion" value="<%=descripcion%>">

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
      
      <table border="0" cellpadding="5">
        <thead></thead>
        <tr>
          <td>No operación:</td>
          <td><input type="text" disabled id="consecutivo" name="consecutivo" size="3" maxlength="2" class="cajaTexto" value="<%=maestroOper.getConsecutivo()%>"/></td>
          <td rowspan="3" valign="top">Descripción:</td>
          <td rowspan="3"><textarea cols="40" rows="5" disabled id="descripcion" name="descripcion" class="cajaTexto"><%=maestroOper.getDescripcion()%></textarea></td>
        </tr>
        <tr>
          <td>Fecha de inicio:</td>
          <td><input type="text" id="fechaVigIni" disabled name="fechaVigIni" size="12" maxlength="12" class="cajaTexto" value="<%=maestroOper.getFechaVigIni()%>"/></td>
        </tr>
        <tr>
          <td>Fecha de termino:</td>
          <td><input type="text" id="fechaVigFin" disabled name="fechaVigFin" size="12" maxlength="12" class="cajaTexto" value="<%=maestroOper.getFechaVigFin()%>"/></td>
        </tr>
      </table>      
      
      
          

     

      <br>      
          
          <table id='TablaPrueba' border="1" class="general" width="100%">
            <thead class='tabla01'>
             <tr>
                <td class="azulObs" width="<%=porcentajeCelda*nivelesConfigura%>%" colspan="<%=nivelesConfigura%>"><strong>Cuentas y subcuentas</strong></td>
                <td class="azulObs" width="10%" align="center"><strong>Debe / Haber</strong></td>
                
             </tr>
            </thead>
            
        <%
          try {
            detalle.addParam("maestro",maestroOper.getMaestroOperacionId());
            detalle.registrosMap(DaoFactory.CONEXION_CONTABILIDAD,"formasContables.select.detalleFormulario");
            
            List<String> cuenta = null;
            for(int x=0; detalle.next(); x++) {
              cuenta = bcConfiguracionGeneral.separarClave(cadenaConfiguracion.toString(), detalle.getString("cuenta_contable"), detalle.getInt("nivel"));
            %> 
              <tr>
                <%
                  for (int i = 0; i < nivelesConfigura; i++)  {
                    if(cuenta.size() > i && cuenta.get(i) != null) {
                      %><td width="10%" align="center"><input type="text" class="cajaTexto" value="<%=cuenta.get(i)%>" size="5" disabled></td><%
                    } else {
                      %><td width="10%"></td><%
                    }
                  }
                  
                %>
                <td align="center"> 
                  <input type="radio" name="chbTipoOper<%=x%>" disabled value="0" <%=detalle.getString("operacion_contable_id").equals("0") ? "checked" : ""%>/> 
                  <input type="radio" name="chbTipoOper<%=x%>" disabled value="1" <%=detalle.getString("operacion_contable_id").equals("1") ? "checked" : ""%>/>
                </td> 
                
              </tr>
            <%
            }
            %>
                
                
            
                
          <%
            
            
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
            <input type="submit" class="boton" value="Regresar"/>
          </td>
        </tr>
      </table>
      <%
        } catch (Exception ex)  {
          ex.printStackTrace();
        } finally  {
        }
      %>
    </form>
  </body>
</html>