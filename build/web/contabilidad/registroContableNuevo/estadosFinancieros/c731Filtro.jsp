<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html; charset=ISO-8859-1"%>
<%@ page import="java.util.*,java.sql.*,sia.db.dao.*,sia.rf.contabilidad.registroContable.acciones.ControlRegistro,sia.db.sql.Sentencias"%>
<%@ page import="sia.db.dao.*, java.sql.*" %>
<%@ page import="sia.db.sql.Vista" %>

<%
    Sentencias sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD, Sentencias.XML);
    Map parametros = new HashMap();
    String programa = request.getParameter("programa");
    ControlRegistro controlReg = (ControlRegistro) request.getSession().getAttribute("controlRegistro");
    String lsTipoUsuario = String.valueOf(controlReg.getTipoUsuario()); //prueba de subversion
    if (programa.equals("")) {
        programa = "'0000'";
    } else {
        String programas[] = programa.split(",");
        programa = "";
        for (int i = 0; i < programas.length; i++) {
            programa = programa + "'" + programas[i] + "',";
            //programa = programa+"'"+programas[i]+"',";
        }
        programa = programa.substring(0, programa.length() - 1);
    }


    String unidad = controlReg.getUnidad();
    int ambito = controlReg.getAmbito();
    int entidad = controlReg.getEntidad();
    int ejercicio = controlReg.getEjercicio();

%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="Autentifica" class="sia.beans.seguridad.Autentifica" scope="session"/> 
<jsp:useBean id="abCuentaBancos" class="sun.jdbc.rowset.CachedRowSet"
             scope="page">
    <%
        String condicionPrograma = "";
        if (!programa.equals("'0000'")) {
            condicionPrograma = " and substr(cuenta_contable, 6,4) in (" + programa + ") ";
        }

        Connection conexion = null;
        conexion = DaoFactory.getContabilidad();
        abCuentaBancos.setTableName("rf_tr_cuentas_contables");
        abCuentaBancos.setCommand(" select * from ("
            + " SELECT distinct cuenta_contable_id, substr(cuenta_contable, 18, 4) cuenta, descripcion  "
            + " FROM rf_tr_cuentas_contables "
            + " WHERE id_catalogo_cuenta = 1 "
            + "AND EXTRACT(YEAR FROM fecha_vig_ini) = " + controlReg.getEjercicio()
            + "AND(cuenta_contable LIKE '11203%') "
            + "and nivel =5 " + condicionPrograma
            + "and substr(cuenta_contable, 10,4) ='" + controlReg.getUniEjecFormateada() + "' "
            + "and substr(cuenta_contable, 14,4) ='" + controlReg.getAmbEntFormateada() + "') "
            + "order by cuenta");
        System.out.println(abCuentaBancos.getCommand());
        abCuentaBancos.execute(conexion);
        conexion.close();
        conexion = null;
    %>
</jsp:useBean>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252"/>
        <title>c731Filtro</title>
        <link rel="stylesheet" href="../../../Librerias/Estilos/siaexplorer.css"
              type="text/css">
        <script language="JavaScript"
                src="../../../Librerias/Javascript/refrescarCapa.js"
        type="text/javascript"></script>


        <script language="JavaScript" type='text/JavaScript'>
            function activarEtiqueta(){         
                if(document.getElementsByName("activaPreliminar")[0].checked)
                    document.getElementById("activaEtiqueta").value="true";
                else
                    document.getElementById("activaEtiqueta").value="false";
            }
        </script>

        <script language="JavaScript" type='text/JavaScript'>
            function mostrarElementos(){
                elemento = document.getElementsByName("reporte");
                reporte  = -1;
                unidad = document.getElementById("unidad").value;
                ocultarElementos();
                for(var i=0; i<elemento.length; i++) {
                    if(elemento[i].checked)
                        reporte = elemento[i].value;
                }
                
                
                /* if(reporte == '2'){
                     document.getElementById('orientacion').style.display  = "";
                }*/
        
                if(reporte == '8'){
                    document.getElementById('titulo').style.display  = "none";
                    document.getElementById('parametrosRep').style.display  = "";
                    document.getElementById('vaf').style.display  = "";
                }
                if(reporte == '0' || reporte == '1'  || reporte == '2'  || reporte == '13' || reporte == '14' || reporte == '15' 
                    || reporte == '16' || reporte == '17' || reporte == '20' || reporte == '22' || reporte == '23'
                    || reporte == '24' || reporte == '25' ){
                    document.getElementById('parametrosRep').style.display  = "";
                    document.getElementById('titulo').style.display  = "";
                    document.getElementById('firmas').style.display  = "";
                }
                if(reporte == '6'){
                    document.getElementById('cuentasBanco').style.display  = "";
                    document.getElementById('titulo').style.display  = "none";
                    document.getElementById('criterioEstado').checked = "checked";
                }
                if(reporte == '4' || reporte == '5'){
                    document.getElementById('titulo').style.display  = "none";
                    document.getElementById('parametrosRep').style.display  = "none";
                }
                if(reporte == '12')
                    ocultarElementos();
                if(unidad == "100"){
                    document.getElementById('parametrosRep').style.display  = "";
                    document.getElementById('activarTextoPreliminar').style.display  = "";
                }
                
            }

            function ocultarElementos(){
                document.getElementById('parametrosRep').style.display  = "none";
                document.getElementById('titulo').style.display  = "none";
                /*document.getElementById('orientacion').style.display  = "none";*/
                document.getElementById('cuentasBanco').style.display  = "none";
                document.getElementById('vaf').style.display  = "none";
                document.getElementById('vafi').style.display  = "none";
                document.getElementById('firmas').style.display  = "none";
                document.getElementById('activarTextoPreliminar').style.display  = "none";            
            }

            function asignaValorDoc(documento){
                document.getElementById('documento').value = documento;
            }

            function seleccionados(){
                ListaSeleccion = "";
                cuentas = document.getElementById('lstCuenta');
                for(i=0;i<cuentas.length;i++){  
                    //compruebo si la opción actual esta seleccionada por el usuario
                    if(cuentas.options[i].selected == true){
                        //Lo añado a un variable...
                        if(ListaSeleccion == "")
                            ListaSeleccion += cuentas.options[i].value
                        else
                            ListaSeleccion += ","+cuentas.options[i].value; 
                    }
                }
                document.getElementById('lstCuentasBanco').value = ListaSeleccion;
                
                //alert("Seleccionados()");
            }

            function pocicionFinanciera(){
                alerta = "NOTA:\n";
                alert(alerta + 'Recuerde que el criterio de consolidación de éste reporte es por programa, unidad y estado para que se pueda generar el reporte.');
            }

            function verificaCPublica(){
                unidad = document.getElementById("unidad").value;
                mes = document.getElementById("lstMes").value;
                if(mes=="12"){
                    document.getElementById("cpublica").style.display = "";
                }
                else{
                    document.getElementById("cpublica").style.display = "none";
                }
            }
            
            function seleccionCuentaPublica(cierre){
                if(cierre == "3"){
                    if(unidad == "100"){
                        document.getElementById("centralEncabezado").style.display = "";
                        document.getElementById("centralCuerpo").style.display = "";
                    }
                    else{
                        document.getElementById("centralEncabezado").style.display = "none";
                        document.getElementById("centralCuerpo").style.display = "none";
                    }
                }
                else{
                    document.getElementById("centralEncabezado").style.display = "none";
                    document.getElementById("centralCuerpo").style.display = "none";
                }
            }
    
            function verificaCierre(){
                //activar = false    ;
                var tipoUsr = <%=lsTipoUsuario%>;
                unidad = document.getElementById("unidad").value;
                document.getElementById("centralEncabezado").style.display = "none";
                document.getElementById("centralCuerpo").style.display = "none";
                document.getElementById("cpublica").style.display = "none";
                if(unidad != "100"){
                    document.getElementById("eliminacion").style.display = "none";  
                }
                if ( tipoUsr == 2 ||  tipoUsr == 3) {
                    document.getElementById('criterioConsolidado').disabled = "disabled";
                    document.getElementById('criterioPrograma').disabled = "disabled";
                    document.getElementById('criterioUnidad').checked = "checked";
                }
                if ( tipoUsr == 3) {
                    document.getElementById('criterioConsolidado').disabled = "disabled";
                    document.getElementById('criterioPrograma').disabled = "disabled";
                    document.getElementById('criterioUnidad').disabled = "disabled";
                    document.getElementById('criterioEstado').checked = "checked";
                }
            }
            
        </script>


        <script language="JavaScript" type='text/JavaScript'>
            var xmlHttp
            function mostrarMesesComparar(habilitar){
                if(habilitar) {
                    document.getElementById("DIVMesesComparar").style.display = "inline";
                } else  {
                    document.getElementById("DIVMesesComparar").style.display = "none"; 
                }
            }
            
            function mostrarFirmasAutorizadas(){
                xmlHttp = new XMLHttpRequest();
                if (xmlHttp == null){
                    alert ("Tu navegador no soporta AJAX!");
                    return;
                }
        
                hoy = new Date();
                id_refrescar = Math.abs(Math.sin(hoy.getTime()));
        
                var id_mes; 
                var id_documento;
                var id_ejercicio;
                var id_unidad;
                var id_ambito;
                var id_entidad;
                
                id_mes = document.getElementById("lstMes").value;  
                id_unidad = document.getElementById("HFirmasUnidad").value;  
                id_ambito = document.getElementById("HFirmasAmbito").value;  
                id_entidad = document.getElementById("HFirmasEntidad").value;  
                id_ejercicio = document.getElementById("HFirmasEjercicio").value;
                id_documento = document.getElementById("documento").value;
        
                var url = "c731PintarFirmasAutorizadas.jsp";
                url += "?idMes=" + id_mes;
                url += "&idDocumento=" + id_documento;
                url += "&idEjercicio=" + id_ejercicio;
                url += "&idUnidad=" + id_unidad;
                url += "&idAmbito=" + id_ambito;
                url += "&idEntidad=" + id_entidad;
                url += "&id=" + id_refrescar;
        
                //alert("URL Firmas: " + url);
        
                xmlHttp.onreadystatechange = mostrarFirmasAutorizadasResultado;
                xmlHttp.open("GET", url, true);
                xmlHttp.send(null);
            }
    
            function mostrarFirmasAutorizadasResultado(){
                if (xmlHttp.readyState == 4){
                    //alert("X Firmas: " + xmlHttp.responseText);
                    document.getElementById("DIVFirmasAutorizadas").innerHTML = xmlHttp.responseText;
                }
            }
   
            
            function estaVacio(componente){
                return (  (componente == null) || (componente.lenght == 0)  || (componente == "") )
            }
         
            function validarComparacionEstadosFinancieros(ejercicio){
                var listaErrores="";
                var objeto="";
            
                var ejercicio_filtro = parseInt(ejercicio);
                var mes = parseInt(document.getElementById("lstMes").value);
                var mes_comparar = parseInt(document.getElementById("lstMesCompara").value);
                var ejercicio_comparar = parseInt(document.getElementById("ejercicioCompara").value);

                var esComparativo = String(document.getElementById("DIVMesesComparar").style.display);
                if(esComparativo == "inline"){
                    //alert("In: " + esComparativo);
                   
                    //alert("Mes base: " + mes + "  Mes comparar: " + mes_comparar + "   Ejercicio filtro: " + ejercicio_filtro + "   Ejercicio comparar: " + ejercicio_comparar);            
            
                    if(ejercicio_filtro != ejercicio_comparar){                    
                        if( ejercicio_comparar > ejercicio_filtro ) {
                            listaErrores += "     -  El ejercicio a comparar " + ejercicio_comparar + " es mayor que el ejercicio"  + " \n"; 
                            listaErrores += "        seleccionado en el filtro " + ejercicio_filtro  + ". \n";                      
                        } else {
                            if( (ejercicio_filtro - 1) > ejercicio_comparar) {
                                //alert((ejercicio_filtro - 1) + " > " + (ejercicio_comparar + 0));
                                listaErrores += "      - El ejercicio seleccionado en el filtro " + ejercicio_filtro + " solamente " + " \n";
                                listaErrores += "        se puede comparar con el ejercicio " + (ejercicio_filtro - 1) + ". \n";
                            } else {
                                if(mes_comparar != 12) {
                                    listaErrores += "     - Solamente puedes comparar con el mes de diciembre  " + " \n";
                                    listaErrores += "       del ejercicio " + ejercicio_comparar + ". \n";                                  
                                }      
                            }       
                        }
                    } else {
                    
                        if(mes == mes_comparar) {
                            listaErrores += "     - No puedes comparar meses iguales dentro del mismo" + " \n";
                            listaErrores += "        ejercicio " + ejercicio_comparar + ". \n";                              
                        } else {                        
                            if(mes < mes_comparar) {
                                listaErrores += "     - El mes no puedes ser menor que el mes a comparar" + " \n";
                            }
                        }
                    }
                }
                
                if( !estaVacio(listaErrores) ){
                    listaErrores="\nSe generaron los siguientes errores en la validaci\u00f3n:\n" +
                        "------------------------------------------------------------------------\n\n" +
                        listaErrores +
                        "\n------------------------------------------------------------------------\n" +
                        "Por favor presiona aceptar e intenta nuevamente."; 
					 
                    alert(listaErrores);
		
                    if( !estaVacio(objeto) ) {
                        (document.getElementById(objeto)).focus();
                        (document.getElementById(objeto)).select();
                    }
                    
                    return false;

                } else {
                    return true;
                    //return false;
                }
            }
            
    
            function inicializar() {
                asignaValorDoc('BCO');
                mostrarMesesComparar(false);
                mostrarFirmasAutorizadas();
            }
            
        </script> 


    </head>

    <body onload="ocultarElementos();verificaCierre();inicializar();">
        <form id="forma" method="post" action="../estadosFinancieros/c731ImprimirReporte.jsp" target="_blank">
            <%util.tituloPagina(out, "Contabilidad", " ", "Generacion de reportes de estados financieros", "Reportes", true);%>

            <br/><br/>
            <table width="50%" align="center"  border="0" >
                <tr class="azulCla">
                    <td><b>Listado de reportes</b></td>
                </tr>
            </table>

            <table border="0" width="50%" class='sianoborder' align="center">
                <tr align="left">
                    <td>
                        <input id="" type="radio" name="reporte" value="0"  checked
                               onclick="asignaValorDoc('BCO');mostrarElementos();mostrarMesesComparar(false);mostrarFirmasAutorizadas();"/>Balanza de comprobaci&oacute;n
                    </td>
                </tr>
                <% if (!Autentifica.getPerfilAcceso().equals("16")) { %> 
                <tr align="left">
                    <td>
                        <input id="" type="radio" name="reporte" value="1" 
                               onclick="asignaValorDoc('EFC');mostrarElementos();mostrarMesesComparar(true);mostrarFirmasAutorizadas();"/>Estado de Situaci&oacute;n Financiera
                    </td>
                </tr>

                <!--tr align="left">
                  <td><input id="" type="radio" name="reporte" value="2" 
                             onclick="asignaValorDoc('EFD');mostrarElementos();mostrarMesesComparar(true);mostrarFirmasAutorizadas();"/>Estado de Situaci&oacute;n Financiera Detallado</td>
                </tr-->
        
                <!--<tr align="left">
                  <td><input id="" type="radio" name="reporte" value="2" onclick="asignaValorDoc('EFC');cargarDatos();mostrarElementos()"/>Estado de situaci&oacute;n financiera comparativo</td>
                </tr>
                <tr align="left">
                  <td><input id="" type="radio" name="reporte" value="3" onclick="asignaValorDoc('ECS');cargarDatos();mostrarElementos()"/>Estado de cambios en la situaci&oacute;n financiera</td>
                </tr>
                <tr align="left">
                  <td><input id="" type="radio" name="reporte" value="4" onclick="asignaValorDoc('ERE');cargarDatos();mostrarElementos()"/>Estado de resultados</td>
                </tr>
                <tr align="left">
                  <td><input id="" type="radio" name="reporte" value="5" onclick="asignaValorDoc('ERR');cargarDatos();mostrarElementos()"/>Estado de rectificaci&oacute;n de resultados</td>
                </tr>
                <tr align="left">
                  <td><input id="" type="radio" name="reporte" value="6" onclick="asignaValorDoc('PFC');cargarDatos();mostrarElementos();pocicionFinanciera()"/>Posici&oacute;n Financiera de Cheques</td>
                </tr>
                <tr align="left">
                  <td><input id="" type="radio" name="reporte" value="7" onclick="asignaValorDoc('CCO');mostrarElementos()"/>Cuentas colectivas</td>
                </tr>
                <tr align="left">
                  <td><input id="" type="radio" name="reporte" value="8" onclick="asignaValorDoc('VAF');cargarDatos();mostrarElementos()"/>Validaci&oacute;n de activos fijos(bienes muebles)</td>
                </tr>-->
                <!--<tr align="left">
                  <td>
                    <input id="" type="radio" name="reporte" value="9" onclick=""/>
                    Validaci&oacute;n de activos fijos(bienes inmuebles)
                  </td>
                </tr>
                <tr align="left">
                  <td>
                    <input id="" type="radio" name="reporte" value="10" onclick=""/>
                    Validaci&oacute;n para comprobar el registro contable de los
                    recursos retirados financieramente
                  </td>
                </tr>
                <tr align="left">
                  <td>
                    <input id="" type="radio" name="reporte" value="11" onclick=""/>
                    Integracin de entradas y salidas de almacenes de bienes de consumo
                  </td>
                </tr>
                <tr align="left">
                  <td><input id="" type="radio" name="reporte" value="12" onclick="asignaValorDoc('EOAR');mostrarElementos()"/>Estado de origen y aplicaci&oacute;n de recursos </td>
                </tr>-->
                <!--tr align="left">
                    <td><input id="" type="radio" name="reporte" value="13" onclick="asignaValorDoc('EAA');mostrarElementos();mostrarMesesComparar(false);mostrarFirmasAutorizadas();"/>Estado Anal&iacute;tico del Activo</td>
                </tr>
                <tr align="left">
                    <td><input id="" type="radio" name="reporte" value="14" onclick="asignaValorDoc('EAP');mostrarElementos();mostrarMesesComparar(false);mostrarFirmasAutorizadas();"/>Estado  Anal&iacute;tico del Pasivo</td>
                </tr-->
                <tr align="left">
                    <td><input id="" type="radio" name="reporte" value="15" onclick="asignaValorDoc('EAC');mostrarElementos();mostrarMesesComparar(true);mostrarFirmasAutorizadas();"/>Estado de Actividades</td>
                </tr>
                 <% } %> 
                <!--tr align="left">
                  <td><input id="" type="radio" name="reporte" value="20" onclick="asignaValorDoc('EAD');mostrarElementos();mostrarMesesComparar(true);mostrarFirmasAutorizadas();"/>Estado de Actividades Detallado</td>
                </tr>
                <tr align="left">
                    <td><input id="" type="radio" name="reporte" value="16" onclick="asignaValorDoc('EVH');mostrarElementos();mostrarMesesComparar(true);mostrarFirmasAutorizadas();"/>Estado de Variaciones en la Hacienda P&uacute;blica / Patrimonio</td>
                </tr-->
                <!--<tr align="left">
                  <td><input id="" type="radio" name="reporte" value="17" onclick="asignaValorDoc('EFE');cargarDatos();mostrarElementos()"/>Estado de Flujos de Efectivo</td>
                </tr>
                <tr align="left">
                    <td><input id="" type="radio" name="reporte" value="18" onclick="asignaValorDoc('CEC');cargarDatos();mostrarElementos()"/>Cuenta Econ&oacutemica</td>
                </tr>
                <tr align="left">
                    <td><input id="" type="radio" name="reporte" value="19" onclick="asignaValorDoc('CLE');cargarDatos();mostrarElementos()"/>Clasificaci&oacute;n Econ&oacute;mica</td>
                </tr>-->

                <!--tr align="left">
                    <td><input id="" type="radio" name="reporte" value="22" onclick="asignaValorDoc('EDP');mostrarElementos();mostrarMesesComparar(false);mostrarFirmasAutorizadas();"/>Estado Anal&iacute;tico de la Deuda y Otros Pasivos </td>
                </tr>

                <tr align="left">
                    <td><input id="" type="radio" name="reporte" value="23" onclick="asignaValorDoc('ECS');mostrarElementos();mostrarMesesComparar(false);mostrarFirmasAutorizadas();"/>Estado de Cambios en la Situaci&oacute;n Financiera</td>
                </tr-->

                <!--                <tr align="left">
                                    <td><input id="" type="radio" name="reporte" value="24" onclick="asignaValorDoc('EAI');cargarDatos();mostrarElementos();"/>Estado Anal&iacute;tico de Ingresos</td>
                                </tr>-->

                <!--                <tr align="left">
                                    <td><input id="" type="radio" name="reporte" value="25" onclick="asignaValorDoc('EAE');cargarDatos();mostrarElementos();"/>Estado Anal&iacute;tico del Ejercicio</td>
                                </tr>-->
            </table>
            <br></br>

            <div id="DIVMeses">

                <table border='0' width='50%' align='center'>
                    <tr class='azulCla'>
                        <td>
                            <b>Mes del estado financiero</b>
                        </td>
                    </tr>
                </table>

                <table border='0' width='50%' align='center'>
                    <tr>
                        <td>                  
                            <div id="DIVMes">

                                <table border='0' align='center'>
                                    <tr>
                                        <td align='right'>
                                            <b>
                                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                &nbsp;Mes:</b>
                                        </td>
                                        <td align='left'>
                                            <select id='lstMes' name='lstMes' class='cajaTexto' onchange='mostrarFirmasAutorizadas();verificaCPublica();'>
                                                <option value='01'>Enero</option>
                                                <option value='02'>Febrero</option>
                                                <option value='03'>Marzo</option>
                                                <option value='04'>Abril</option>
                                                <option value='05'>Mayo</option>
                                                <option value='06'>Junio</option>
                                                <option value='07'>Julio</option>
                                                <option value='08'>Agosto</option>
                                                <option value='09'>Septiembre</option>
                                                <option value='10'>Octubre</option>
                                                <option value='11'>Noviembre</option>
                                                <option value='12'>Diciembre</option>
                                            </select>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <div id="DIVMesesComparar">

                                <%
                                    List<Vista> registros = new ArrayList<Vista>();

                                    try {

                                        sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD, Sentencias.XML);
                                        registros = sentencia.registros("filtros.select.ejercicios", new HashMap());

                                        if (registros != null) {
                                            out.write("<table border='0' align='center'>");

                                            out.write("<tr>");
                                            out.write("<td align='right'><b>Mes a comparar:</b></td>");
                                            out.write("<td align='left'>");
                                            out.write("<select id='lstMesCompara' name='lstMesCompara' class='cajaTexto'>");
                                            out.write("<option value='01'>Enero</option>");
                                            out.write("<option value='02'>Febrero</option>");
                                            out.write("<option value='03'>Marzo</option>");
                                            out.write("<option value='04'>Abril</option>");
                                            out.write("<option value='05'>Mayo</option>");
                                            out.write("<option value='06'>Junio</option>");
                                            out.write("<option value='07'>Julio</option>");
                                            out.write("<option value='08'>Agosto</option>");
                                            out.write("<option value='09'>Septiembre</option>");
                                            out.write("<option value='10'>Octubre</option>");
                                            out.write("<option value='11'>Noviembre</option>");
                                            out.write("<option value='12'>Diciembre</option>");
                                            out.write("</select>");
                                            out.write("</td>");
                                            out.write("</tr>");

                                            out.write("<tr>");
                                            out.write("<td align='right'><b>Ejercicio a comparar:</b></td>");
                                            out.write("<td align='left'>");
                                            out.write("<select id='ejercicioCompara' name='ejercicioCompara' class='cajaTexto'> ");

                                            for (Vista record : registros) {
                                                out.write("<option value='" + record.getField("ejercicio") + "'>" + record.getField("ejercicio") + "</option>");
                                            }

                                            out.write("</select>");
                                            out.write("</td>");
                                            out.write("</tr>");

                                            out.write("</table>");


                                        }
                                    } catch (Exception e) {
                                        System.err.println("Error al consultar las firmas");
                                    } finally {
                                    }
                                %>

                            </div>
                        </td>
                    </tr>

                </table>
            </div>

            <br/><br/>
            <div id='parametrosRep'>
                <table width="50%" align="center">
                    <tr class="azulCla">
                        <td><b>Par&aacute;metros del reporte</b></td>
                    </tr>
                </table>
            </div>
            <%String etiqueTaPreliminar = "De conformidad con la fracción III del  Artículo 257 "
                    + "del Reglamento de la Ley Federal de Presupuesto y Responsabilidad Hacendaria, "
                    + "estas cifras se considerarán definitivos una vez que la Unidad de Contabilidad "
                    + "Gubernamental e Informes de la Gestión Pública de la Secretaria de Hacienda y "
                    + "Crédito Público, nos otorgue el expediente de cierre de la Cuenta Pública del ejercicio 2011. ";%>            
            <div id='titulo'>
                <table border="0" width="50%" class='sianoborder' align="center">
                    <tr>
                        <td class="negrita"><b>T&iacute;tulo del programa:</b></td>
                        <%
                            String titulo = "";
                            if (programa.equals("'0001'")) {
                                titulo = "CÁMARA DE SENADORES";
                            } else if (programa.equals("'0002'")) {
                                titulo = "CÁMARA DE SENADORES";
                            } else if (programa.equals("'0003'")) {
                                titulo = "CÁMARA DE SENADORES";
                            } else if (programa.equals("'0004'")) {
                                titulo = "CÁMARA DE SENADORES";
                            } else if (programa.equals("'0005'")) {
                                titulo = "CÁMARA DE SENADORES";
                            } else if (programa.equals("'0006'")) {
                                titulo = "CÁMARA DE SENADORES";
                            } else if (programa.equals("'0007'")) {
                                titulo = "CÁMARA DE SENADORES";
                            } else {
                                titulo = "x";
                            }
                        %>
                        <td>
                            <input id='txtTitulo' type='text' name='txtTitulo'  class='cajaTexto' value='<%=titulo%>' size="40"/>
                        </td>
                    </tr>
                </table>
            </div>
            <tr align="left">
            <div id='orientacion'>
                <hr noshade="noshade" width="40%" align="center" style="border-width: 1px;border-style:solid"></hr>        
                <!--<table border="0" width="40%" class='sianoborder' align="center">
                  <tr align="center">
                    <td align="right">
                      <input type="radio" name="orientacion" value="0"
                             checked="checked"/>
                      Horizontal
                    </td>        
                    <td align="left">
                      <input type="radio" name="orientacion" value="1"/>
                      Vertical
                    </td>
                  </tr>
                </table>-->
            </div>
            <div id='cuentasBanco'>
                <hr noshade="noshade" width="50%" align="center"
                    style="border-width: 1px;border-style:solid"></hr>
                <table border="0" width="50%" class='sianoborder' align="center">
                    <tr align="center">
                        <td align="right" class='negrita'>Cuentas de banco:</td>
                        <td align="left">
                            <select id="lstCuenta" name="lstCuenta" class='lst'
                                    multiple="multiple">
                                <%

                                    try {
                                        abCuentaBancos.beforeFirst();
                                        while (abCuentaBancos.next()) {
                                %>
                                <option value='<%=abCuentaBancos.getString("cuenta")%>'>
                                    <%=abCuentaBancos.getString("cuenta") + " " + abCuentaBancos.getString("descripcion")%>
                                </option>
                                <%
                                        } //Fin while
                                    } catch (Exception e) {
                                        System.out.println("Error en lista de programa: " + e.getMessage());
                                    } finally {
                                        sentencia = null;
                                        parametros = null;
                                    }

                                %>
                            </select>
                        </td>
                    </tr>
                </table>
            </div>
            <div id='vaf'>
                <table border="0" width="40%" class='sianoborder' align="center">
                    <tr>
                        <td class="negrita">Iva cap&iacute;tulo 5000:</td>
                        <td>
                            <input id='txtIva' type='text' name='txtIva' class='cajaTexto'
                                   value='' size="12"/>
                        </td>
                        <td class="negrita">Activos en contrato de comodato:</td>
                        <td>
                            <input id='txtActivos' type='text' name='txtActivos'
                                   class='cajaTexto' value='' size="12"/>
                        </td>
                    </tr>
                </table>
            </div>
            <div id='vafi'>
                <br></br><hr noshade="noshade" width="40%" align="center"
                             style="border-width: 1px;border-style:solid"></hr><br></br>

                <table border="0" width="40%" class='sianoborder' align="center">
                    <tr>
                        <td class="negrita">12501 Obras en proceso:</td>
                        <td>
                            <input id='txtObras' type='text' name='txtObras' class='cajaTexto'
                                   value='' size="12"/>
                        </td>
                        <td class="negrita">Relevos de anticipo:</td>
                        <td>
                            <input id='txtRelevos' type='text' name='txtRelevos'
                                   class='cajaTexto' value='' size="12"/>
                        </td>
                    </tr>
                </table>
            </div>
            <div id='activarTextoPreliminar'>
                <table border="0" width="40%" class='sianoborder' align="center">
                    <tr>
                        <td><input type="checkbox" name="activaPreliminar" onclick="activarEtiqueta()"/>Preliminar</td>
                    </tr>
                    <tr>
                        <td><textarea name="textoPreliminar" rows="7" cols="62" class='cajaTexto'><%=etiqueTaPreliminar%></textarea> </td>
                    </tr>
                </table>
            </div>

            <table width="50%" align="center"  border="0" >
                <tr class="azulCla">
                    <td><b>Criterios de consolidaci&oacute;n</b></td>
                </tr>
            </table>

            <table border="0" width="50%" class='sianoborder' align="center">
                <tr>
                    <td>
                        <input id="criterioConsolidado" type="radio" name="criterio" value="1" checked="checked"/>
                        Consolidado general
                    </td>
                </tr>
                <!--tr>
                    <td>
                        <input id="criterioPrograma" type="radio" name="criterio" value="3"/>
                        Consolidado por programa
                    </td>
                </tr>
                <tr>
                    <td>
                        <input id="criterioUnidad" type="radio" name="criterio" value="4"/>
                        Consolidado por programa y unidad
                    </td>
                </tr>
                <tr>
                    <td>
                        <input id="criterioEstado" type="radio" name="criterio" value="5"/>
                        Consolidado por programa, unidad y estado
                    </td>
                </tr-->
            </table>
            <br/><br/>

            <table width="50%" align="center"  border="0" >
                <tr class="azulCla">
                    <td><b>Tipo de cierre</b></td>
                </tr>
            </table>

            <table border="0" width="50%" class='sianoborder' align="center">
                <tr>
                    <td>
                        <input type="radio" name="cierre" value="1" checked="checked" onclick="seleccionCuentaPublica(1)"/>
                        Normal
                    </td>
                </tr>
                <tr>
                    <td id="eliminacion">
                        <input type="radio" name="cierre" value="2" onclick="seleccionCuentaPublica(2)"/>
                        Eliminación
                    </td>
                </tr>
                <tr>
                    <td id="cpublica">
                        <input type="radio" name="cierre" value="3" onclick="seleccionCuentaPublica(3)"/>
                        Cuenta P&uacute;blica
                    </td>
                </tr>
            </table>

            <table id="centralEncabezado" width="100%" align="center">
                <tr class="azulCla"><td>Seleccionar</td></tr>
            </table>

            <table id="centralCuerpo" border="0" width="20%" class='sianoborder' align="center">
                <tr>
                    <td><input type="radio" name="central" value="1" checked="checked"/>Central</td>
                    <td><input type="radio" name="central" value="2"/>Unidades</td>
                </tr>
            </table>

            <iframe style="display:none" name="bufferCurp" id="bufferCurp"></iframe>
            <div id="firmas">
            </div>

            <br/> <br/>
            <table border='0' width='50%' align='center'>
                <tr class='azulCla'>
                    <td>
                        <b>Firmantes autorizados</b>
                    </td>
                </tr>
            </table>
            <br/>
            <table border='0' width='50%' align='center'>
                <tr>
                    <td>            
                        <div id="DIVFirmasAutorizadas">
                        </div>
                    </td>
                </tr>
            </table>


            <br>
            <hr noshade="noshade" width="40%" align="center"
                style="border-width: 1px;border-style:solid"></hr>
            <br/><br/>

            <table cellpadding="5" border="0" width="15%" class="sianoborder"
                   align="center">
                <tr align="center">
                    <td><input type="submit" class="boton" value="Aceptar" onclick="seleccionados();activarEtiqueta();return validarComparacionEstadosFinancieros(<%= ejercicio%>)"/></td>
                    <td>
                        <input type="button" class="boton" value="Regresar"
                               onclick="javascript:history.back();"/>
                    </td>
                </tr>
            </table>

            <input type="hidden" name="unidad" id="unidad" value="<%=controlReg.getUnidad()%>">
            <input type="hidden" name="programa" id="programa" value="<%=programa%>"></input>
            <input type="hidden" name="lstCuentasBanco" id="lstCuentasBanco" value="0"></input>
            <input type="hidden" name="documento" id="documento" value=""></input>
            <input type="hidden" name="activaEtiqueta" id="activaEtiqueta" value=""></input>


            <input type="hidden" name="HFirmasUnidad" id="HFirmasUnidad" value="<%= unidad%>">
            <input type="hidden" name="HFirmasAmbito" id="HFirmasAmbito" value="<%= ambito%>">
            <input type="hidden" name="HFirmasEntidad" id="HFirmasEntidad" value="<%= entidad%>">
            <input type="hidden" name="HFirmaEjercicio" id="HFirmasEjercicio" value="<%= ejercicio%>">


        </form>
    </body>
</html>