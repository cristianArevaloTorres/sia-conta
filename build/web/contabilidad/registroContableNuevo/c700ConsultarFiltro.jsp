<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page  import="java.util.*,java.sql.*"%>
<%@ page  import="sia.db.dao.*"%>
<%@ page import="sia.rf.contabilidad.registroContable.acciones.ControlRegistro"%>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>   
<jsp:useBean id="Autentifica" class="sia.beans.seguridad.Autentifica" scope="session"/> 
<jsp:useBean id="controlRegistro" class="sia.rf.contabilidad.registroContable.acciones.ControlRegistro" scope="session"/>
<jsp:useBean id="fechaAfectacion" class="sia.rf.contabilidad.registroContable.servicios.FechaAfectacion" scope="page"/>


<%
    if (controlRegistro.getFechaAfectacion() == null) {
        fechaAfectacion.establecerFechaAfectacion(request, fechaAfectacion.obtenerFechaAfectacion());
    }
    ControlRegistro controlReg = (ControlRegistro) request.getSession().getAttribute("controlRegistro");
    String lsUniEje = controlReg.getUnidad();
    String lsEntidad = String.valueOf(controlReg.getEntidad());
    String lsAmbito = String.valueOf(controlReg.getAmbito());
    String lsEjercicio = String.valueOf(controlReg.getEjercicio());
    String lsCatCuenta = String.valueOf(controlReg.getIdCatalogoCuenta());
    System.out.println(lsUniEje);
%>


<jsp:useBean id="abOperaciones" class="sun.jdbc.rowset.CachedRowSet" scope="page">     
    <%
        abOperaciones.setTableName("rf_tc_maestro_operaciones");
        String SQL = "select maestro_operacion_id, consecutivo, descripcion from rf_tc_maestro_operaciones where  unidad_ejecutora='" + lsUniEje + "' and ambito='" + lsAmbito
            + "' and entidad=" + lsEntidad + " and pais=147 " + " and id_catalogo_cuenta=" + lsCatCuenta + " and  extract(year from fecha_vig_ini) =" + lsEjercicio
            + " order by consecutivo";
        abOperaciones.setCommand(SQL);
        Connection con = null;
        try {
            con = DaoFactory.getContabilidad();
            abOperaciones.execute(con);
        } catch (Exception e) {
            System.out.println("Ocurrio un error al accesar el metodo abOperaciones " + e.getMessage());
        } //Fin catch
        finally {
            if (con != null) {
                con.close();
                con = null;
            }
        } //Fin finally
%>
</jsp:useBean> 

<jsp:useBean id="abpoliza" class="sun.jdbc.rowset.CachedRowSet" scope="page">
    <%
        abpoliza.setTableName("rf_tc_tipos_polizas");
        String SQL2 = ("select tipo_poliza_id,descripcion,registro,abreviatura "
            + " from rf_tc_tipos_polizas"
            + " order by tipo_poliza_id");
        abpoliza.setCommand(SQL2);
        Connection con2 = null;
        try {
            con2 = DaoFactory.getContabilidad();
            abpoliza.execute(con2);
        } catch (Exception e) {
            System.out.println("Ocurrio un error al accesar el metodo abpoliza " + e.getMessage());
        } //Fin catch
        finally {
            if (con2 != null) {
                con2.close();
                con2 = null;
            }
        } //Fin finally
%>
</jsp:useBean> 




<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
        <title>c700consultarFiltro</title>
        <link rel="stylesheet" href="../../Librerias/Estilos/siastyle.css" type='text/css'>
        <script language="JavaScript" src="../../Librerias/Javascript/Capturador/funciones.js" type="text/javascript"></script>
        <script language="JavaScript" src="../../Librerias/Javascript/Capturador/fcgencValida2.js" type="text/javascript"></script>
        <script language="JavaScript" src="../../Librerias/Javascript/Capturador/calendar.js" type="text/javascript"></script>


        <script language="JavaScript">
            function revisaFormulario() {
                var valida01=true, valida02=true, valida03=true;
                var cadenaError='';
                var date01,date02;

                var mes_afectacion = document.filtro.lstMes.value;
                if(mes_afectacion == "") {
                    valida01 = false;
                    cadenaError=cadenaError+"Es necesario seleccionar un mes de afectacion\n";
                }
    

                if ((document.filtro.txtFecha01.value=="") && (!document.filtro.txtFecha02.value=="")){
                    valida01=false;
                    cadenaError=cadenaError+"Es necesario escribir la fecha de inicio del Periodo\n";
                }
 
   
                if ((!document.filtro.txtFecha01.value=="") && (document.filtro.txtFecha02.value=="")){
                    valida01=false;
                    cadenaError=cadenaError+"Es necesario escribir la fecha final del Periodo\n";
                }
   
                if (valida01){
                    if (!(document.filtro.txtFecha01.value=="" && document.filtro.txtFecha02.value=="")) {      
                        if (!(document.filtro.txtFecha01.value=="")) {
                            valida02=ValidarObjeto(document.filtro.txtFecha01.value,"F");
                            if (!(valida02)) {
                                cadenaError=cadenaError+"Error en el formato de la fecha, verifique que sea correcto(dd/mm/aaaa): "+document.filtro.txtFecha01.value+"\n";
                                valida01=false; 
                            }
                            else date01=convierte(document.filtro.txtFecha01.value);
                        }//txtFecha01
      
    
                        if (!(document.filtro.txtFecha02.value=="")) {
                            valida02=ValidarObjeto(document.filtro.txtFecha02.value,"F");
                            if (!(valida02)) {
                                cadenaError=cadenaError+"Error en el formato de la fecha, verifique que sea correcto(dd/mm/aaaa): "+document.filtro.txtFecha02.value+"\n";
                                valida01=false; 
                            }
                            else date02=convierte(document.filtro.txtFecha02.value);            
                        }//txtFecha02
      
  
                        if (valida02) {
                            if (!(date01<=date02)) {
                                valida01=false;
                                cadenaError=cadenaError+"La fecha inicial debe de ser menor o igual a la fecha final,por favor verifique sus datos\n"; 
                            }
                        }//valida02
                    }//txtFecha01 y txtFEcha02
                }//valida01   
   
   
                if(!(valida01))
                    alert(cadenaError);
                else  {           
                    document.filtro.submit();
      
      
      
      
                }
            }


            function validarSiNumero(object)
            {
                div = document.getElementById('dopval');
                div.style.display = 'none';
                if(object.value!='') {
                    numero = object.value.substring(object.value.length-1,object.value.length);
                    if (!/\d/.test(numero)) {
                        object.value = object.value.substring(0,object.value.length-1);
                        div.style.display = '';
                    }
                }  
            }
          
            function mostrarFirmasAutorizadas(id_unidad, id_entidad, id_ambito, id_ejercicio, id_mes, id_documento){
                xmlHttp = new XMLHttpRequest();
                if (xmlHttp == null){
                    alert ("Tu navegador no soporta AJAX!");
                    return;
                }
         
                //alert("Mes de afectacion: " + id_mes);
                //alert("mostarFirmasAutorizadas()");
                hoy = new Date();
                id_refrescar = Math.abs(Math.sin(hoy.getTime()));
          
                //var id_mes = 1;
                //var id_documento;
                //var id_ejercicio;
                //var id_unidad;
                //var id_ambito;
                //var id_entidad;
                   
                //id_unidad = unidad;
                //id_ambito = ambito;
                //id_entidad = entidad;
                //id_ejercicio = ejercicio;
                //id_documento = "PLZ";
          
                var url = "c700PintarFirmasAutorizadas.jsp";
                url += "?idMes=" + id_mes;
                url += "&idDocumento=" + id_documento;
                url += "&idEjercicio=" + id_ejercicio;
                url += "&idUnidad=" + id_unidad;
                url += "&idAmbito=" + id_ambito;
                url += "&idEntidad=" + id_entidad;
                url += "&id=" + id_refrescar;
          
                //alert("URL: " + url);
                if (id_mes == "") { 
                    document.getElementById("DIVFirmasAutorizadas").style.display = "none";
                    document.getElementById("DIVFirmasAutorizadasTitulo").style.display = "none";
                } else {
                    xmlHttp.onreadystatechange = mostrarFirmasAutorizadasResultado;
                    xmlHttp.open("GET", url, true);
                    xmlHttp.send(null);  
              
                }

            }
      
            function mostrarFirmasAutorizadasResultado(){
                if (xmlHttp.readyState == 4){
                    //alert( xmlHttp.responseText);
                    document.getElementById("DIVFirmasAutorizadas").innerHTML = xmlHttp.responseText;
                    document.getElementById("DIVFirmasAutorizadas").style.display = "inline";
                    document.getElementById("DIVFirmasAutorizadasTitulo").style.display = "inline";
                }
            }
            //Fin Ajax

    

        </script>
    </head>
    <body>

        <jsp:scriptlet>util.tituloPagina(out, "Contabilidad", " ", "Filtro de pólizas", "Modificar/Cancelar", true);</jsp:scriptlet>    
        <br><br>
        <b>Fecha Actual</b> [ <%=sia.libs.formato.Fecha.formatear(sia.libs.formato.Fecha.FECHA_CORTA, controlRegistro.getFechaAfectacion())%> ]

        <br><br>

        <H2>Informaci&oacute;n de la P&oacute;liza: Unidad ejecutora=<%=lsUniEje%>, Entidad=<%=lsEntidad%>, Ambito=<%=lsAmbito%> y Ejercicio=<%=lsEjercicio%> </H2>

        <br>

        <FORM name="filtro" method="post"  action="c700ConsultarResultado.jsp" target="_blank">       
            <table align="center" border="0">
                <tr><td align="right"><input type="checkbox" name="chk_poliz" value="si"></td>
                    <td class="negrita">Sólo pólizas de la unidad</td></tr>

                <tr><td  class="negrita" align="right">Operación Contable </td>
                    <td><select id='lstOperaciones' name='lstOperaciones' class='cajaTexto' onchange='capaOperacion();'>
                            <option value="">-Seleccione-</option>
                            <%
                                abOperaciones.beforeFirst();
                                while (abOperaciones.next()) {
                            %>
                            <option value='<%=abOperaciones.getString("maestro_operacion_id")%>'><%=abOperaciones.getString("consecutivo")%>-<%=abOperaciones.getString("descripcion")%></option>

                            <%
                                }
                            %>
                    </td>      
                </tr>  

                <tr><td  class="negrita" align="right">Tipo de Póliza: </td>
                    <td ><select id="lstpoliza" name="lstpoliza" class="cajaTexto">
                            <option value=""> -Seleccione-</option>

                            <%
                                abpoliza.beforeFirst();
                                while (abpoliza.next()) {
                            %>
                            <option value="<%=abpoliza.getString("tipo_poliza_id")%>"><%=abpoliza.getString("descripcion")%></option>

                            <%
                                }
                            %>           
                        </select> 
                    </td>      
                </tr>     


                </td>      
                </tr>     

                <tr>
                    <td  class="negrita" align="right">Referencia General: </td>
                    <td><INPUT TYPE="text" NAME="txtReferencia" SIZE=30  class=cajaTexto > ejem. KA101</td></tr>
                <tr>
                    <td  class="negrita" align="right">Concepto: </td>
                    <td ><INPUT TYPE="text" NAME="txtConcepto" SIZE=30  class=cajaTexto ></td></tr>
                <tr>
                    <td class="negrita" align="right">Mes de Afectación: </td>
                    <td><select name="lstMes" class='cajaTexto' onchange="mostrarFirmasAutorizadas('<%= lsUniEje%>', <%=lsEntidad%>, <%=lsAmbito%>, <%=lsEjercicio%>, this.value, 'PLZ' )">
                            <option SELECTED Value="">- Seleccione -</option>
                            <option Value="01">Enero</option>
                            <option Value="02">Febrero</option>
                            <option Value="03">Marzo</option>
                            <option Value="04">Abril</option>
                            <option Value="05">Mayo</option>
                            <option Value="06">Junio</option>
                            <option Value="07">Julio</option>
                            <option Value="08">Agosto</option>
                            <option Value="09">Septiembre</option>
                            <option Value="10">Octubre</option>
                            <option Value="11">Noviembre</option>
                            <option Value="12">Diciembre</option>
                        </select>
                    </td>      
                </tr>      
                <tr><td class="negrita" align="right">Período del: </td>
                    <td><input type='text' name='txtFecha01' size='10' maxlength='10' class='cajaTexto'><a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" onClick="open_calendar('../../Librerias/Javascript/Capturador/calendar.html','document.forms[0].txtFecha01')"><img src="../../Librerias/Imagenes/calendar.gif" name="imgCalendar" width="34" height="21" border="0" alt=""></a> dd/mm/aaaa</td></tr><tr>
                    <td class="negrita" align="right">Al:</td><td><input type='text' name='txtFecha02' size='10' maxlength='10' class='cajaTexto'><a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" onClick="open_calendar('../../Librerias/Javascript/Capturador/calendar.html','document.forms[0].txtFecha02')"><img src="../../Librerias/Imagenes/calendar.gif" name="imgCalendar" width="34" height="21" border="0" alt=""></a> dd/mm/aaaa</td></tr>
                <tr><td class="negrita" align="right">Rango de pólizas: </td><td><input type='text' name='txtPoliza01' SIZE=20 maxlength='10' class='cajaTexto'> ejem. E1,I8,D5,D8</td></tr>


                <tr><td class="negrita" align="right">Importe:</td>
                    <td><input type='text' name='txtImporte' SIZE=50 maxlength='10' class='cajaTexto' onkeyup="validarSiNumero(this)"> <div id="dopval" style="display:none"><font color="red">Solo se permiten numeros</font></div> </td>
                <div id="dopval" style="display:none"><font color="red">Solo se permiten numeros</font></div>
                </tr>


                <tr><td><br></td>
                    <td>
                    </td>
                </tr>



                <tr>
                    <td rowspan="3"  class="negrita" align="right">Estatus de pólizas:</td>
                    <td><input id="todas_p" type="radio" name="st_poliza" value="1" onclick="" checked/>Todas las pólizas </td></tr>
                <tr>
                    <td><input id="p_act" type="radio" name="st_poliza" value="2" onclick=""/>Pólizas activas</td></tr>
                <tr>
                    <td><input id="p_canc" type="radio" name="st_poliza" value="3" onclick=""/>Pólizas canceladas</td>
                </tr>
                <tr><td><br></td>
                    <td>
                    </td>

                <tr>
                    <td rowspan="3"  class="negrita" align="right">Formato del reporte:</td>
                     
                    <td><input id="ctas_comp" type="radio" name="chk_formato" value="1" onclick="" checked/>Cuentas completas </td>
                    
                </tr>
                <tr>
                    <% if (!Autentifica.getPerfilAcceso().equals("16")) { %> 
                    <td><input id="ahorro" type="radio" name="chk_formato" value="2" onclick=""  <% if (Autentifica.getPerfilAcceso().equals("16")) { %> checked <% } %> />Sólo cuentas de último nivel (Ahorro de Papel)</td>
                    <% } %> 
                </tr>


                <tr><td><br></td><td><br></td></tr>
                 
                <tr <% if (Autentifica.getPerfilAcceso().equals("16")) { %> style="display: none;" <% } %> >
                    <td rowspan="5"  class="negrita" align="right">Tipo de ordenamiento:</td>
                    <td><input id="unidad_or" type="radio" name="tip_or" value="1" onclick="" checked/>Unidad</td></tr>
                <% if (!Autentifica.getPerfilAcceso().equals("16")) { %> 
                <tr>
                    <td><input id="ea_or" type="radio" name="tip_or" value="2" onclick=""/>Entidad y ámbito</td></tr>
                <tr>
                    <td><input id="pol_or" type="radio" name="tip_or" value="3" onclick=""/>Tipo de póliza</td></tr>
                <tr>
                    <td><input id="cons_or" type="radio" name="tip_or" value="4" onclick=""/>Consecutivo</td></tr>
                <tr>
                    <td><input id="dhc_or" type="radio" name="tip_or" value="5" onclick=""/>Debe-Haber y cuenta contable</td></tr>
                 <% } %> 
                <tr>

                <tr>
                    <td  class="negrita" align="right">
                        <div id="DIVFirmasAutorizadasTitulo" name="DIVFirmasAutorizadasTitulo" style="display:none">Firmantes Autorizados:</div>
                    </td>
                    <td>
                        <br>
                        <div id="DIVFirmasAutorizadas" name="DIVFirmasAutorizadas" ></div>
                        <br>
                    </td>
                </tr>
                <td  align="right"><input type='button' name='btnAceptar' value='Aceptar' class='boton' onClick="javascript:revisaFormulario();"></td>
                <td><input type='button' name='btnRegresar' value='Regresar' class='boton' onClick="javascript:LlamaPagina('c700OpcionesPolizas.jsp','');"></td></tr>
            </table>   
            <input type='hidden' name='txtIdCatalogo' value='<%=String.valueOf(controlReg.getIdCatalogoCuenta())%>'>
            <input type='hidden' name='txtUnidad' value='<%=controlReg.getUnidad()%>'>
            <input type='hidden' name='txtAmbito' value='<%=String.valueOf(controlReg.getAmbito())%>'>
            <input type='hidden' name='txtEntidad' value='<%=String.valueOf(controlReg.getEntidad())%>'>
            <input type='hidden' name='txtEjercicio' value='<%=String.valueOf(controlReg.getEjercicio())%>'>              
            <input type='hidden' name='txtOpcion' value='<%=request.getParameter("opcion")%>'>
            <input type='hidden' name='txtfechaActual' value='<%=sia.libs.formato.Fecha.formatear(sia.libs.formato.Fecha.FECHA_CORTA, controlRegistro.getFechaAfectacion())%>'>              

        </FORM>
    </body>
</html>