<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.db.dao.DaoFactory"%>
<%@ page import="sia.db.sql.SentennciasSE" %>
<%@ page import="sia.libs.formato.Fecha" %>
<%@ page import="sia.db.sql.Vista" %>
<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/> 
<jsp:useBean id="Autentifica" class="sia.beans.seguridad.Autentifica" scope="session"/>
<jsp:useBean id="crsCtaBanDGC" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="crsCtaBanDSP" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="crsCtaBanCGC" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="crsCtaBanCSP" class="sia.db.sql.SentenciasCRS" scope="page"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesListaCtasBanca</title>
 <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type="text/css">
    <script language="JavaScript" src="../../Librerias/Javascript/Calendario/Calendar/calendar.js" type="text/javascript"></script>
    <script src="../../Librerias/Javascript/validacionesFecha.js" type="text/javascript" >
    </script>
    <script language="JavaScript"  type="text/javascript">


     function validarFechas(){  
       correcto=true; 
       mensaje='------  ALERTA  ------ \n';
       fInicio=document.getElementById('fechaInicio');
       ffinal=document.getElementById('fechaFinal');
       correcto = validaFIniFFin(fInicio,ffinal);
       if (correcto == false){
         mensaje=mensaje+'Verifique el periodo de fechas \n';
         correcto=false;
       }
       if(!correcto)
         throw new Error(mensaje);
       return correcto;
     }
     
      function dispersorasGC() { 
            if (document.form1.elements['cbDispGC'].checked){
                for (i=0; i<document.form1.elements['smDispGC'].options.length; i++) 
                    document.form1.elements['smDispGC'][i].selected = false;
            }
            if ( document.form1.elements['smDispGC'].selectedIndex == -1) { 
                for (i=0; i<document.form1.elements['smDispGC'].options.length; i++) 
                   document.form1.elements['smDispGC'][i].selected = true;
            } 
            else { 
                for (i=0; i<document.form1.elements['smDispGC'].options.length; i++) 
                   document.form1.elements['smDispGC'][i].selected = false;
                }    
        }
        
        function noSeleccionaDispGC() {
          document.form1.elements['cbDispGC'].checked = false;
        }
        
      
      function dispersorasSP () { 
            if (document.form1.elements['cbDispSP'].checked){
                for (i=0; i<document.form1.elements['smDispSP'].options.length; i++) 
                    document.form1.elements['smDispSP'][i].selected = false;
            }
            if ( document.form1.elements['smDispSP'].selectedIndex == -1) { 
                for (i=0; i<document.form1.elements['smDispSP'].options.length; i++) 
                   document.form1.elements['smDispSP'][i].selected = true;
            } 
            else { 
                for (i=0; i<document.form1.elements['smDispSP'].options.length; i++) 
                   document.form1.elements['smDispSP'][i].selected = false;
                }    
        }
        
        function noSeleccionaDispSP() {
          document.form1.elements['cbDispSP'].checked = false;
        }
    
        function chequerasGC () { 
            if (document.form1.elements['cbDChqGC'].checked){
                for (i=0; i<document.form1.elements['smChqGC'].options.length; i++) 
                    document.form1.elements['smChqGC'][i].selected = false;
            }
            if ( document.form1.elements['smChqGC'].selectedIndex == -1) { 
                for (i=0; i<document.form1.elements['smChqGC'].options.length; i++) 
                   document.form1.elements['smChqGC'][i].selected = true;
            } 
            else { 
                for (i=0; i<document.form1.elements['smChqGC'].options.length; i++) 
                   document.form1.elements['smChqGC'][i].selected = false;
                }    
        }
        
        function noSeleccionaChqGC() {
          document.form1.elements['cbDChqGC'].checked = false;
        }   
    
        function chequerasSP() { 
            if (document.form1.elements['cbChqSP'].checked){
                for (i=0; i<document.form1.elements['smChqSP'].options.length; i++) 
                    document.form1.elements['smChqSP'][i].selected = false;
            }
            if ( document.form1.elements['smChqSP'].selectedIndex == -1) { 
                for (i=0; i<document.form1.elements['smChqSP'].options.length; i++) 
                   document.form1.elements['smChqSP'][i].selected = true;
            } 
            else { 
                for (i=0; i<document.form1.elements['smChqSP'].options.length; i++) 
                   document.form1.elements['smChqSP'][i].selected = false;
                }    
        }
        
        function noSeleccionaChqSP() {
          document.form1.elements['cbChqSP'].checked = false;
        }  
    
        function verificaSeleccionCta(){
          correcto=true; 
          mensaje='------  ALERTA  ------ \n';
          if ( (document.getElementById("smDispGC")==null || document.form1.elements['smDispGC'].selectedIndex == -1) &&
               (document.getElementById("smDispSP")==null || document.form1.elements['smDispSP'].selectedIndex == -1) && 
               (document.getElementById("smChqGC")==null || document.form1.elements['smChqGC'].selectedIndex == -1) && 
               (document.getElementById("smChqSP")==null || document.form1.elements['smChqSP'].selectedIndex == -1) )
                correcto = false; 
          if (correcto == false){
            mensaje=mensaje+'Debe seleccionar al menos una cuenta bancaria \n';
            correcto=false;
          }
          if(!correcto)
            throw new Error(mensaje);
          return correcto;
        }
        
        
        function asignaValorRep(boton){
          var combos = ['smDispGC','smDispSP','smChqGC','smChqSP'];
          var selCombo;
          var idCuentasSelecciondas = new Array();
          var indiceCta = 0;
          try{
              if (validarFechas() && verificaSeleccionCta()){
               
              for (var i=0;i < combos.length;i++){
                selCombo =  document.getElementById(combos[i]);
                for (var j=0;j < selCombo.options.length;j++){
                    if (selCombo.options[j].selected){
                        idCuentasSelecciondas[indiceCta] = selCombo.options[j].value;
                        indiceCta++;
                    } 
                 }
              }
                document.getElementById('ctaSelec').value=idCuentasSelecciondas;
                document.getElementById('form1').action='tesListaTransacciones.jsp';
                document.getElementById('form1').target='';
                document.getElementById('form1').submit();
              }
          } catch(e){
            alert(e.message);
          }
        }
        
        function regresar(){
           document.getElementById('form1').action='../tesListadoProgramas.jsp';
           document.getElementById('form1').target='';
           document.getElementById('form1').submit();
        } 
        
    </script>
  </head>
<%!
 
    public String fomatearFecha(String fecha)  {
    String regresa = null;
    if (fecha!=null)
      regresa = Fecha.formatear(2,fecha.replace("-",""));
    return regresa;
  }
  
    public String fechaUltimaMovimientos(String programa){
    String regresa = null;
    SentennciasSE sentenciasSE = new SentennciasSE(DaoFactory.CONEXION_TESORERIA);
    Map parametros = new HashMap();
    List<Vista> registros = null;
    try  {
      parametros.put("idPrograma",programa);
      registros = sentenciasSE.registros("criterios.select.ultimaFechaCargada.modClaveTrans",parametros);
      if (registros!=null){
          regresa = registros.get(0).getField("FECHA");
      }
    } catch (Exception e)  {
          e.printStackTrace();
          regresa = "''"; 
    } finally  {
      sentenciasSE = null;
      registros = null;
    }
    return fomatearFecha(regresa); 
  }
  
%>
<%
 String fechaUltCarga = null;
 fechaUltCarga = fechaUltimaMovimientos(request.getParameter("idProgramaS"));
 
 if (request.getParameter("idProgramaS").equals("8")){
     crsCtaBanDGC.addParamVal("tipoCuenta"," id_estatus_cta_prog='1' and (id_tipo_cta='4' or id_tipo_cta='3') and id_tipo_programa=:param",request.getParameter("idProgramaS"));
     crsCtaBanDGC.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.RfTrCuentasBancarias.modClaveTrans");
 } else {
     crsCtaBanDGC.addParamVal("tipoCuenta"," id_estatus_cta_prog='1' and (id_tipo_cta='4' or (id_tipo_cta = '3' and UPPER(nombre_cta) like '%CONCENTRADORA%')) and id_tipo_programa=:param",request.getParameter("idProgramaS"));
     crsCtaBanDGC.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.RfTrCuentasBancarias.modClaveTrans");  
 }
     crsCtaBanDSP.addParamVal("tipoCuenta"," id_estatus_cta_prog = '1' and id_tipo_cta = '5' and id_tipo_programa =:param",request.getParameter("idProgramaS"));
     crsCtaBanDSP.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.RfTrCuentasBancarias.modClaveTrans");
     crsCtaBanCGC.addParamVal("tipoCuenta"," id_estatus_cta_prog = '1' and id_tipo_cta = '1' and id_tipo_programa =:param",request.getParameter("idProgramaS"));
     crsCtaBanCGC.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.RfTrCuentasBancarias.modClaveTrans");
     crsCtaBanCSP.addParamVal("tipoCuenta"," id_estatus_cta_prog = '1' and id_tipo_cta = '2' and id_tipo_programa =:param",request.getParameter("idProgramaS"));
     crsCtaBanCSP.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.RfTrCuentasBancarias.modClaveTrans");
     
     
%>
  <body>
    <form id="form1" name="form1" action="tesListaTransacciones.jsp" method="POST">
      <%util.tituloPagina(out,"Tesorería","Filtro cuentas bancarias","Modificación clave de transacción",true);%>
      <input type="hidden" id="nomProg" name="nomProg" value="<%=request.getParameter("nomProg")%>">
      <input type="hidden" id="opcion" name="opcion" value="<%=request.getParameter("opcion")%>">
      <input type="hidden" id="idProgramaS" name="idProgramaS" value="<%=request.getParameter("idProgramaS")%>">
      <input type="hidden" id="proceso" name="proceso" value="<%=request.getParameter("proceso")%>">
      <input type="hidden" id="ctaSelec" name="ctaSelec" >
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
      <table width="80%" align="center" >
      <thead></thead>
      <tbody>
        <tr>
          <td align="left">Cuentas Bancarias</td>
        </tr>
      </tbody>
      </table>
      <table width="80%" style="background-color:transparent; border-color:rgb(0,132,198); border-style:solid; border-width:2.0px;" align="center">
      <thead></thead>
      <tbody>
        <tr> 
          <td> 
            <table>
            <thead></thead>
            <tbody>
                <%if(crsCtaBanDGC.size()>0){%>                  
                <tr>
                  <td></td><td></td><td></td>
                </tr>
                <tr>
                  <td></td><td>Dispersoras de gasto corriente:</td><td></td>
                </tr>
                <tr>
                  <td></td>
                  <td><input type="checkbox" name="cbDispGC" id="cbDispGC" onclick="dispersorasGC()"> Todas las cuentas dispersoras de gasto corriente</td>
                  <td></td>
                </tr>
                <tr>
                  <td></td>
                  <td><select  class="cajaTexto" id="smDispGC" name="smDispGC" multiple="multiple" onclick="noSeleccionaDispGC()"> 
                        <%CRSComboBox(crsCtaBanDGC, out,"ID_CUENTA","NUM_CUENTA,NOMBRE_CTA","");%>
                      </select>
                  </td><td></td>
                </tr>
                <%}%>
                <%if(crsCtaBanDSP.size()>0){%>
                <tr>
                  <td height="20"></td><td></td><td></td>
                </tr>
                <tr>
                  <td></td><td>Dispersoras de servicios personales:</td><td></td>
                </tr>
                <tr>
                  <td></td>
                  <td><input type="checkbox" name="cbDispSP" id="cbDispSP" onclick="dispersorasSP()"> Todas las cuentas dispersoras de servicios personales</td>
                  <td></td>
                </tr>
                <tr>
                  <td></td>
                  <td><select  class="cajaTexto" id="smDispSP" name="smDispSP" multiple="multiple" onclick="noSeleccionaDispSP()"> 
                        <%CRSComboBox(crsCtaBanDSP, out,"ID_CUENTA","NUM_CUENTA,NOMBRE_CTA","");%>
                      </select>
                  </td><td></td>
                </tr>
                <%}%>
                <%if(crsCtaBanCGC.size()>0){%>
                <tr>
                  <td height="20"></td><td></td><td></td>
                </tr>
                <tr>
                  <td></td><td>Chequeras de gasto corriente:</td><td></td>
                </tr>
                <tr>
                  <td></td>
                  <td><input type="checkbox" name="cbDChqGC" id="cbChqGC" onclick="chequerasGC()"> Todas las chequeras de gasto corriente</td>
                  <td></td>
                </tr>
                <tr>
                  <td></td> 
                  <td><select  class="cajaTexto" id="smChqGC" name="smChqGC" multiple="multiple" onclick="noSeleccionaChqGC()"> 
                        <%CRSComboBox(crsCtaBanCGC, out,"ID_CUENTA","NUM_CUENTA,NOMBRE_CTA","");%>
                      </select>
                  </td><td></td>
                </tr>
                <%}%>
                <%if(crsCtaBanCSP.size()>0){%>
                <tr>
                  <td height="20"></td><td></td><td></td>
                </tr>
                <tr>
                  <td></td><td>Chequeras de servicios personales:</td><td></td>
                </tr>
                <tr>
                  <td></td>
                  <td><input type="checkbox" name="cbChqSP" id="cbChqSP" onclick="chequerasSP()"> Todas las chequeras de servicios personales</td>
                  <td></td>
                </tr>
                <tr>
                  <td></td>
                  <td><select  class="cajaTexto" id="smChqSP" name="smChqSP" multiple="multiple" onclick="noSeleccionaChqSP()"> 
                        <%CRSComboBox(crsCtaBanCSP, out,"ID_CUENTA","NUM_CUENTA,NOMBRE_CTA","");%>
                      </select>
                  </td><td></td>
                </tr>
                <%}%>
            </tbody>
            </table>
          </td>
        </tr>
      </tbody>
      </table>
      <br>
      <br>
      <table align="center">
        <thead></thead>
        <tbody>
          <tr>
            <td align="right">Periodo del: </td>
            <td>
              <table align='left'>
              <thead></thead>
                <tbody>
                <tr >
                  <td id="colFI"><input type="text" name="fechaInicio" id="fechaInicio" value="<%=fechaUltCarga%>" class="cajaTexto" readonly size="13">
                      <a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" 
                      onClick="open_calendar('../../Librerias/Javascript/Calendario/Calendar/calendar.html','document.forms[0].fechaInicio')">
                      <img src="../../Librerias/Javascript/Calendario/Calendar/cal.gif" name="imgCalendar" border="0" alt=""></a> al: </td>
                  <td><input type="text" name="fechaFinal" id="fechaFinal" value="<%=fechaUltCarga%>" class="cajaTexto" readonly size="13">
                     <a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" 
                     onClick="open_calendar('../../Librerias/Javascript/Calendario/Calendar/calendar.html','document.forms[0].fechaFinal')">
                     <img src="../../Librerias/Javascript/Calendario/Calendar/cal.gif" name="imgCalendar" border="0" alt=""></a></td>
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
          <td><input type="button" class="boton" value="Ver transacciones bancarias" onclick="asignaValorRep('pdf')"></td>
          <td><input type="button" class="boton" value="Regresar" onclick="regresar()"></td>
        </tr>
      </tbody>
    </table>
    </form>
  </body>
</html>