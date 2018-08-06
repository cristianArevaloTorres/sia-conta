<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.libs.formato.Fecha" %>
<%@ page import="sia.db.dao.DaoFactory"%>
<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<jsp:useBean id="crsTipoTrans" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="crsClaveTB" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="crsClaveEgr" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/> 
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesAgregarTrans</title>
   <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type="text/css">
    <script language="JavaScript" src="../../Librerias/Javascript/Calendario/Calendar/calendar.js" type="text/javascript"></script>
    <script language="javascript" type="text/javascript">
    
       function regresa(){
          document.getElementById('mensaje').value='';
          document.getElementById('form1').action='tesListaTransacciones.jsp';
          document.getElementById('form1').submit();
       }
       
       
      function ir() {
          document.getElementById('form1').action = 'tesAgregarTransCtrl.jsp';
          document.getElementById('form1').submit();
      }
      
      
      function tipoSeleccionado() {
        if(document.getElementById('tipo').value=='C'){
            document.getElementById('listaCostos').style.display='';    
            document.getElementById('comisionGenerada').style.display='';
          }
        else{
            document.getElementById('listaCostos').style.display='none';    
            document.getElementById('comisionGenerada').style.display='none'; 
        }
      }
      
      
      function noSpei() {
        if (document.getElementById('spei').checked){
            document.getElementById('limInf').style.display='';
            document.getElementById('limSup').style.display='';  
            document.getElementById('esSpei').value='1';
          }
        else{
            document.getElementById('limInf').style.display='none';
            document.getElementById('limSup').style.display='none';  
            document.getElementById('esSpei').value='0';
        }
      }
      
    
    </script>
  </head>
  <% 
    
    boolean mostrar = request.getParameter("mensaje")!=null?true:false;
    crsTipoTrans.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.rfTcTipoTransaccion.catTransacciones");
    crsClaveTB.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.rfTcClavesTb.catTransacciones");
    crsClaveEgr.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.obtenerEgresos.catTransacciones");
    
  %>
  <body>
  <form id="form1" name="form1" action="tesAgregarTransCtrl.jsp"  method="POST">
  <%util.tituloPagina(out,"Tesorería","Agregar transacción bancaria","Catálogo de transacciones bancarias",true);%>
  <input type="hidden" name="param" id="param" value="<%=request.getParameter("param")%>"/>  
  <input type="hidden" name="mensaje" id="mensaje" value="<%=request.getParameter("mensaje")%>"/>  
    <br>
   <table width="80%" align="center" class="general" cellpadding="3">
   <thead></thead>
        <tbody>
           <tr class="azulCla">
             <td colspan="4">Datos de la transacción</td>
           </tr>
           <tr>
             <td width="6%"></td>
             <td width="8%" align="left">Clave:</td>
             <td width="3%"></td>
             <td width="59%" align="left"><input type="text" name="clave" id="clave" class="cajaTexto" size="15"></td>
           </tr>
           <tr>
             <td width="6%"></td>
             <td width="8%" align="left">Descrpción:</td>
             <td width="3%"></td>
             <td width="59%" align="left"><input type="text" name="descripcion" id="descripcion" class="cajaTexto" size="80"></td>
           </tr>
           <tr>
             <td width="6%"></td>
             <td width="8%" align="left">Tipo:</td>
             <td width="3%"></td>
             <td width="59%" align="left">
                <select class="cajaTexto" id="tipo" name="tipo" onchange="tipoSeleccionado()"> 
                  <option value=''>- Seleccione -</option>   
                  <%CRSComboBox(crsTipoTrans, out,"ID_TIPO_APLICA","DESCRIPCION","");%>
                </select>
             </td>
           </tr>
           <tr>
             <td width="6%"></td>
             <td width="8%" align="left">Clave TB:</td>
             <td width="3%"></td>
             <td width="59%" align="left">
                <select class="cajaTexto" id="claveTb" name="claveTb" > 
                  <option value=''>- Seleccione -</option>   
                  <%CRSComboBox(crsClaveTB, out,"ID_CLAVE_TB","ID_CLAVE_TB,DESCRIPCION","");%>
                </select>
             </td>
           </tr>
             <tr id="comisionGenerada" style="display:none">
             <td width="6%"></td>
             <td width="8%" align="left">Comisión generada por:</td>
             <td width="3%"></td>
             <td width="59%" align="left">
                <select class="cajaTexto" id="claveEgreso" name="claveEgreso" > 
                  <option value=''>- Seleccione -</option>   
                  <%CRSComboBox(crsClaveEgr, out,"id_clave_trans","claveTrans,descripcion","");%>
                </select>
             </td>
           </tr>
           <tr>
             <td width="6%"></td>
             <td width="8%" align="left">Vigencia a partir de:</td>
             <td width="3%"></td>
             <td width="59%" align="left">  
                     <input type="text" name="fecha" id="fecha" value="<%=Fecha.formatear(Fecha.FECHA_CORTA,Fecha.getRegistro())%>" class="cajaTexto" readonly size="13">
                     <a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" 
                     onClick="open_calendar('../../Librerias/Javascript/Calendario/Calendar/calendar.html','document.forms[0].fecha')">
                     <img src="../../Librerias/Javascript/Calendario/Calendar/cal.gif" name="imgCalendar" border="0" alt=""></a>
             </td>
           </tr>
        </tbody>
   </table> 
   <br>
   <table width="80%" align="center" class="general" cellpadding="3" id="listaCostos" style="display:none">
   <thead></thead>
        <tbody>
           <tr class="azulCla">
             <td colspan="4">Datos de la comisión</td>
           </tr>
         <!--     <tr>
             <td width="6%"></td>
             <td width="8%" align="left"></td>
             <td width="3%"></td>
             <td width="59%" align="left"><input type="checkbox"  name="spei" id="spei" onclick="noSpei()">SPEI</td>
           </tr>
        <tr id="limInf" style="display:''">
             <td width="6%"></td>
             <td width="8%" align="left">Límite inferior: $</td>
             <td width="3%"></td>
             <td width="59%" align="left"><input type="text" name="limiteInf" id="limiteInf" class="cajaTexto" size="13"></td>
           </tr>
           <tr id="limSup" style="display:''">
             <td width="6%"></td>
             <td width="8%" align="left">Límite superior: $</td>
             <td width="3%"></td>
             <td width="59%" align="left"><input type="text" name="limiteSup" id="limiteSup" class="cajaTexto" size="13"></td>
           </tr>
-->
           <tr>
             <td width="6%"></td>
             <td width="8%" align="left">Costo: $</td>
             <td width="3%"></td>
             <td width="59%" align="left"><input type="text" name="costo" id="costo" class="cajaTexto" value="0.00" size="13"></td>
           </tr>
        </tbody>
   </table> 
   <br>
   <table align="center" <%=mostrar?"style='display:'":"style='display:none'"%>>
        <thead></thead>
           <tbody>
             <tr align="center">
               <td  style="font-size:medium;"><%=request.getParameter("mensaje")%></td>
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
          <td><input type="button" class="boton" value="Aceptar" onclick="ir()"></td>
          <td><input type="button" class="boton" value="Cancelar" onclick="regresa()"></td>
        </tr>
      </tbody>
    </table>
  </form>
  </body>
</html>