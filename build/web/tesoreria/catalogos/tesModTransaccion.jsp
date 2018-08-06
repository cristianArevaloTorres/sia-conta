<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.libs.formato.Fecha" %>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<jsp:useBean id="crsDetTrans" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="crsClaveEgr" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="crsTipoTrans" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="crsClaveTB" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/> 
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesModTransaccion</title>
   <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type="text/css">
    <script language="JavaScript" src="../../Librerias/Javascript/Calendario/Calendar/calendar.js" type="text/javascript"></script>
    <script language="javascript" type="text/javascript">
    
       function regresa(){
          document.getElementById('mensaje').value='';
          document.getElementById('form1').action='tesListaTransacciones.jsp';
          document.getElementById('form1').submit();
       }
       
       
      function ir() {
          document.getElementById('form1').action = 'tesModTransaccionCtrl.jsp';
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
    </script>
  </head>
  <%!
  
   public String quitaCaracter(String cadena, String caracter){
     int longitud= cadena.length();
     while(cadena.indexOf(caracter)!=-1) {
       int posicion= cadena.indexOf(caracter);
       cadena= cadena.substring(0, posicion).concat(cadena.substring(posicion+1, longitud--));
     }
     return cadena;
    }
    
      private  sia.libs.periodo.Fecha estableceFormato(String fecha){
        sia.libs.periodo.Fecha regresa = null;
        try  {
          regresa = new sia.libs.periodo.Fecha(fecha, "-");
        } catch (Exception ex)  {
           ex.printStackTrace();
        }
        return regresa;
    } 
  %>
  <%
        boolean registraAct = false;
        boolean mostrar = request.getParameter("mensaje")!=null?true:false;
    
        String clave = null;
        clave = request.getParameter("clave");
 
        crsDetTrans.addParamVal("idClaveTrans",clave);
        crsDetTrans.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.detTransModificar.catTransacciones");
        crsDetTrans.next();
        
        crsTipoTrans.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.rfTcTipoTransaccion.catTransacciones");
        crsClaveTB.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.rfTcClavesTb.catTransacciones");
        crsClaveEgr.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.obtenerEgresos.catTransacciones");
        
        
         
  %>
  <body onload="tipoSeleccionado('<%=crsDetTrans.getStr("id_tipo_aplica")%>')">
  <form id="form1" name="form1" action="tesModTransaccionCtrl.jsp"  method="POST">
  <%util.tituloPagina(out,"Tesorería","Editar transacciones","Catálogo de transacciones",true);%>
  <input type="hidden" id="clave" name="clave" value="<%=request.getParameter("clave")%>">
  <input type="hidden" id="mensaje" name="mensaje" value="<%=request.getParameter("mensaje")%>">
  <input type="hidden" id="param" name="param" value="<%=request.getParameter("param")%>">  
  <input type="hidden" id="tipoTrans" name="tipoTrans" value="<%=crsDetTrans.getStr("id_tipo_aplica")%>">  
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
             <td width="59%" align="left"><input type="text" name="clave" id="clave" value="<%=crsDetTrans.getStr("clave_trans")%>" class="cajaTexto" disabled="true" size="15"></input>
             </td>
           </tr>
           <tr>
             <td width="6%"></td>
             <td width="8%" align="left">Descripción:</td>
             <td width="3%"></td>
             <td width="59%" align="left"><input type="text" name="descripcion" id="descripcion" value="<%=crsDetTrans.getStr("descripcionClave")%>" class="cajaTexto" size="80"></input></td>
           </tr>
           <tr>
             <td width="6%"></td>
             <td width="8%" align="left">Tipo:</td>
             <td width="3%"></td>
             <td width="59%" align="left">
                  <select class="cajaTexto" id="tipo" name="tipo" disabled="true" > 
                     <option value=''>- Seleccione -</option>
                     <%CRSComboBox(crsTipoTrans, out,"ID_TIPO_APLICA","DESCRIPCION",crsDetTrans.getStr("id_tipo_aplica").equals(null)?"":crsDetTrans.getStr("id_tipo_aplica"));%>
                  </select>
           </tr>
           <tr>
             <td width="6%"></td>
             <td width="8%" align="left">Clave TB:</td>
             <td width="3%"></td>
             <td width="59%" align="left">
                    <select class="cajaTexto" id="claveTb" name="claveTb"> 
                      <option value=''>- Seleccione -</option>
                      <%CRSComboBox(crsClaveTB, out,"ID_CLAVE_TB","ID_CLAVE_TB,DESCRIPCION",crsDetTrans.getStr("id_clave_tb").equals(null)?"00":crsDetTrans.getStr("id_clave_tb"));%>
                    </select>
             </td>
           </tr>
           <tr  id="comisionGenerada" style="display:none">
             <td width="6%"></td>
             <td width="8%" align="left">Comision generada por:</td>
             <td width="3%"></td>
             <td width="59%" align="left">
                    <select class="cajaTexto" id="claveEgreso" name="claveEgreso" disabled="true"> 
                      <option value=''>- Seleccione -</option>
                      <%CRSComboBox(crsClaveEgr, out,"id_clave_trans","claveTrans,descripcion",crsDetTrans.getStr("id_clave_egreso").equals(null)?"No es generada por algín egreso":crsDetTrans.getStr("id_clave_egreso"));%>
                    </select>
            </td>
           </tr>
           
           <tr>
             <td width="6%"></td>
             <td width="8%" align="left">Fecha:</td>
             <td width="3%"></td>
             <td width="59%" align="left"> 
                     <input type="text" name="fecha" id="fecha" value="<%=Fecha.formatear(Fecha.FECHA_CORTA,quitaCaracter(crsDetTrans.getStr("vigencia_ini"),"-"))%>" class="cajaTexto" readonly size="13">
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
           <tr>
             <td width="6%"></td>
             <td width="8%" align="left">Costo: $</td>
             <td width="3%"></td>
             <td width="59%" align="left"><input type="text" name="costo" id="costo" class="cajaTexto" value="<%=crsDetTrans.getStr("costo")%>" size="13"></td>
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